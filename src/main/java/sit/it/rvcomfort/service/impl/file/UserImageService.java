package sit.it.rvcomfort.service.impl.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sit.it.rvcomfort.config.properties.PathProperties;

import java.nio.file.Paths;

@Slf4j
@Service
public class UserImageService extends FileServiceImpl {

    public UserImageService(PathProperties properties) {
        super(Paths.get(properties.getImage().getUser()));
    }

}
