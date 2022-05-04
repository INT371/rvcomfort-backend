package sit.it.rvcomfort;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ConfigurationPropertiesScan("sit.it.rvcomfort.config.properties")
@SpringBootApplication
public class RvcomfortApplication {

	public static void main(String[] args) {
		SpringApplication.run(RvcomfortApplication.class, args);
	}



	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
