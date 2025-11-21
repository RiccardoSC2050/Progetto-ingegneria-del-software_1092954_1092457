package it.unibg.progetto.api.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import it.unibg.progetto.api.access_session.ManagerSession;
import it.unibg.progetto.api.access_session.Session;
import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.components.ClearTerminal;
import it.unibg.progetto.api.components.Exit;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.mapper.RootMapper;
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
	@Profile("!test")
	public CommandLineRunner createDefaultUser(UserMapper userMapper, UsersService service,
			ActionOnUseRS conversionUseRS, RootMapper rootMapper) {
		return args -> {
			AppBlocks ab = new AppBlocks();
			String input;

			Root.configurationOfRoot();
			ab.loginSession();

			do {
				System.out.print(ManagerSession.getCurrent().getName() + "> ");
				input = GlobalScaner.scanner.nextLine();

				switch (input) {

				case "exit":
					Exit.exit(input);
					break;

				case "clear":
					ClearTerminal.clearTerminal(input);
					break;

				case "out":
					ab.logoutSession();
					break;

				default:
					System.out.print("Comando errato o non esistente\n\n");
					break;
				}

			} while (true);

		};
	}
}
