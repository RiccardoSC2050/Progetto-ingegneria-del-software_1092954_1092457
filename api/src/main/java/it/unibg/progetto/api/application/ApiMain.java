package it.unibg.progetto.api.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.api.operators.ConversionUseRS;
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
    public CommandLineRunner createDefaultUser(UserMapper userMapper, UsersService service, ConversionUseRS conversionUseRS) {
        return args -> {
            System.out.println("Creazione utente di default...");
            
            Root root = Root.getInstanceRoot(userMapper, service, conversionUseRS);
            
            root.createUser("Rick", "@Rick", 3);
            root.createUser("MICK", "MICK", 3);

            
            
            
            
        };
    }
}
