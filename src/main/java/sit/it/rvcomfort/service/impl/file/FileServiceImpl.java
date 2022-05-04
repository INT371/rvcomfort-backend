package sit.it.rvcomfort.service.impl.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import sit.it.rvcomfort.service.FileService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class FileServiceImpl implements FileService {

    private final Path directory;

    @Override
    @PostConstruct
    public void init() {
        try {
            log.info("[FileService] Initialize FileService Path: {}", directory);
            Files.createDirectories(directory);
        } catch (Exception e) {
            log.error("[FileService] Image file system initialization failed.");
            e.printStackTrace();
            throw new RuntimeException("Could not create directory for upload.");
        }
    }

    @Override
    public void deleteAll() {
        log.info("[FileService] Delete all file from {}", directory);
        FileSystemUtils.deleteRecursively(directory.toFile());
    }

    @Override
    public void deleteOne(String filename) {
        try {
            log.info("[FileService] delete file {} from path {}", filename, directory);
            FileSystemUtils.deleteRecursively(directory.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public String save(MultipartFile file) {
        try {
            String filename = generateRandomString(file);
            Files.copy(file.getInputStream(), this.directory.resolve(filename));
            log.info("[FileService] Successfully save file with filename: {}", filename);
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("Could not store file. Error: "+e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            log.info("[FileService] Load file {} from {}", filename, directory);
            Path file = directory.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.directory, 1)
                    .filter(path -> !path.equals(this.directory))
                    .map(this.directory::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    private String generateRandomString(MultipartFile file) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0,12);
        return uuid+"-"+file.getOriginalFilename();
    }
}
