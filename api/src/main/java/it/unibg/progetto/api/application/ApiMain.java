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
import it.unibg.progetto.api.action_on.ActionOnCsv;
import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.components.ClearTerminal;
import it.unibg.progetto.api.components.Exit;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.conditions.Checks;
import it.unibg.progetto.api.mapper.CsvMapper;
import it.unibg.progetto.api.mapper.RootMapper;
import it.unibg.progetto.api.mapper.UserMapper;
import it.unibg.progetto.api.operators.Root;
import it.unibg.progetto.service.CsvService;
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
			ActionOnUseRS conversionUseRS, RootMapper rootMapper, CsvService sercCsvService, ActionOnCsv actionOnCsv,
			CsvMapper csvMapper) {
		return args -> {
			AppBlocksManageUsers blockUser = new AppBlocksManageUsers();
			AppBlocksManageCsv blockCsv = new AppBlocksManageCsv();
			String input;

			blockCsv.clearFolderCsv();
			Root.configurationOfRoot();
			blockUser.loginSession();

			do {
				blockCsv.controllOnFolderCsv();
				blockCsv.manageImplementationOfMainFileCsv();
				System.out.print(ManagerSession.getCurrent().getName() + "> ");
				input = GlobalScaner.scanner.nextLine();

				switch (input) {

				case "exit": // end program
					blockCsv.saveAllFileInFolderIntoCsvTable();
					blockCsv.clearFolderCsv();
					Exit.exit(input);
					break;

				case "clear": // clear terminal
					ClearTerminal.clearTerminal(input);
					break;

				case "w -c": // create an file csv
					blockCsv.createGeneralFileCsv();
					break;

				case "r -c": // read an your file csv
					blockCsv.readFileCsv();
					blockCsv.clearFolderCsv();
					break;

				case "ls -f": // mostrami tutti i file
					blockCsv.lsFileUser();
					break;

				case "crt -u": // crea utente se sono root
					blockUser.createUserIfRoot();
					break;

				case "dlt -u": // elimino utente se sono root
					blockUser.deleteUserIfRoot();
					break;

				case "dlt -c": // limina un mio file csv
					blockCsv.deleteMyCsvFromDatabase();
					break;

				case "show -a -u": // mostra tutti gli utenti nome
					blockUser.showUsersIfRoot(Checks.neutral);
					break;

				case "show -t -u": // mostra tutti gli utenti nome
					blockUser.showUsersIfRoot(Checks.affermative);
					break;

				case "search": // ricerca mirata
					blockCsv.searchOnBaseAndMaybeSave();
					break;

				case "save": // salva i miei file
					blockCsv.saveAllFileInFolderIntoCsvTable();
					blockCsv.clearFolderCsv();
					System.out.println("File CSV salvati nel database.\n");
					break;

				case "out": // logout
					blockCsv.saveAllFileInFolderIntoCsvTable();
					blockCsv.clearFolderCsv();
					blockUser.logoutSession();
					break;

				default:
					System.out.print("Comando errato o non esistente\n\n");
					break;
				}

			} while (true);

		};
	}
}