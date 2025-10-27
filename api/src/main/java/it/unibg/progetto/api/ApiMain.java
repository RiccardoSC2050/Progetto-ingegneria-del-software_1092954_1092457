package it.unibg.progetto.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "it.unibg.progetto")
@EnableJpaRepositories(basePackages = "it.unibg.progetto")
@EntityScan(basePackages = "it.unibg.progetto.data")
public class ApiMain {

	public static void main(String[] args) {

		SpringApplication.run(ApiMain.class, args);
	}

}
