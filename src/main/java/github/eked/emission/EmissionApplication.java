package github.eked.emission;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJlc3RlciIsImV4cCI6MTU4NjUxODIxMH0.GJOvssArsY5LvHdlsXIRszm816HIXVx0NuGkTOqgAiFeT38KCbDYqztJXEJiU8ZPAS-UGzMemXjtrEmJpgsWsA
 */
@SpringBootApplication
@EnableCaching
@EnableSwagger2
public class EmissionApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmissionApplication.class, args);
	}

}
