package sit.it.rvcomfort.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileService {

    void init();
    void deleteAll();
    void deleteOne(String filename);

    String save(MultipartFile file);
    Resource load(String filename);

    Stream<Path> loadAll();

}
