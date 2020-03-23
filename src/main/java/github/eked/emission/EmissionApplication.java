package github.eked.emission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class EmissionApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmissionApplication.class, args);
	}

}
