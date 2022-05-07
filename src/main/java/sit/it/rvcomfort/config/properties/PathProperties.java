package sit.it.rvcomfort.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "rvcomfort.path")
public class PathProperties {

        private String base;
        private String file;
        private Image image;

        @Data
        public static class Image {
                private String room;
                private String user;
        }
}

