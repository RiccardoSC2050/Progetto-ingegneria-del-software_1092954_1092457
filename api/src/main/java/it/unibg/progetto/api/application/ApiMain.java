package it.unibg.progetto.api.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.api.operators.Root;
import it.unibg.progetto.service.UsersService;

@SpringBootApplication(scanBasePackages = "it.unibg.progetto")
@EnableJpaRepositories(basePackages = "it.unibg.progetto.data")
@EntityScan(basePackages = "it.unibg.progetto.data")
public class ApiMain {

	public static void main(String[] args) {
		SpringApplication.run(ApiMain.class, args);
	}

	@Bean
	public CommandLineRunner createDefaultUser(UserMapper userMapper, UsersService service,
			ActionOnUseRS conversionUseRS) {
		return args -> {


		};
	}
}
