package de.thd.pms;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class FahrtenbuchApplication {
	@SuppressWarnings("unused")
	private static Logger log = LogManager.getLogger(FahrtenbuchApplication.class);

	public static void main(String[] args) {

		SpringApplication app = new SpringApplication(FahrtenbuchApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", "8083"));
		app.run(args);

	}

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
					.info(new Info().title("Fahrtenbuch API").description("THD Spring sample application").version("v1")
				);
	}

}
