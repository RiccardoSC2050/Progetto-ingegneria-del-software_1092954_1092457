package it.unibg.progetto.api.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import it.unibg.progetto.api.application.usecase.ActionOnCsv;
import it.unibg.progetto.api.application.usecase.ActionOnUseRS;
import it.unibg.progetto.api.cli.AppBlocksManageCsv;
import it.unibg.progetto.api.cli.AppBlocksManageUsers;
import it.unibg.progetto.api.cli.components.ClearTerminal;
import it.unibg.progetto.api.cli.components.Exit;
import it.unibg.progetto.api.cli.components.GlobalScaner;
import it.unibg.progetto.api.domain.Root;
import it.unibg.progetto.api.domain.rules.Checks;
import it.unibg.progetto.api.mapping.CsvMapper;
import it.unibg.progetto.api.mapping.RootMapper;
import it.unibg.progetto.api.mapping.UserMapper;
import it.unibg.progetto.api.security.ManagerSession;
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
    public CommandLineRunner createDefaultUser(
            UserMapper userMapper,
            UsersService service,
            ActionOnUseRS conversionUseRS,
            RootMapper rootMapper,
            CsvService sercCsvService,
            ActionOnCsv actionOnCsv,
            CsvMapper csvMapper
    ) {
        return args -> {
            AppBlocksManageUsers blockUser = new AppBlocksManageUsers();
            AppBlocksManageCsv blockCsv = new AppBlocksManageCsv();

            String input;

            blockCsv.clearFolderCsv();
            Root.configurationOfRoot();
            blockUser.loginSession();

            while (true) {

                // Se per qualsiasi motivo la sessione non esiste, richiedi login
                if (ManagerSession.getCurrent() == null) {
                    System.out.println("Nessuna sessione attiva. Effettua nuovamente il login.");
                    blockUser.loginSession();

                    if (ManagerSession.getCurrent() == null) {
                        System.out.println("Login non riuscito. Chiusura applicazione.");
                        return;
                    }
                }

                blockCsv.controllOnFolderCsv();
                blockCsv.manageImplementationOfMainFileCsv();

                System.out.print(ManagerSession.getCurrent().getName() + "> ");
                input = GlobalScaner.scanner.nextLine();

                switch (input) {

                    case "exit":
                        blockCsv.saveAllFileInFolderIntoCsvTable();
                        blockCsv.clearFolderCsv();
                        Exit.exit(input);
                        break;

                    case "clear":
                        ClearTerminal.clearTerminal(input);
                        break;

                    case "w -c":
                        blockCsv.createGeneralFileCsv();
                        break;

                    case "r -c":
                        blockCsv.readFileCsv();
                        blockCsv.clearFolderCsv();
                        break;

                    case "ls -f":
                        blockCsv.lsFileUser();
                        break;

                    case "crt -u":
                        blockUser.createUserIfRoot();
                        break;

                    case "dlt -u":
                        blockUser.deleteUserIfRoot();
                        break;

                    case "dlt -c":
                        blockCsv.deleteMyCsvFromDatabase();
                        break;

                    case "show -a -u":
                        blockUser.showUsersIfRoot(Checks.neutral);
                        break;

                    case "show -t -u":
                        blockUser.showUsersIfRoot(Checks.affermative);
                        break;

                    case "search":
                        blockCsv.searchOnBaseAndMaybeSave();
                        break;

                    case "search -s":
                        blockCsv.searchOnBaseStatistic();
                        break;

                    case "save":
                        blockCsv.saveAllFileInFolderIntoCsvTable();
                        blockCsv.clearFolderCsv();
                        System.out.println("File CSV salvati nel database.\n");
                        break;

                    case "in -u":
                        blockCsv.saveAllFileInFolderIntoCsvTable();
                        blockUser.viewOtherFiles();
                        break;

                    case "out":
                        blockCsv.saveAllFileInFolderIntoCsvTable();
                        blockCsv.clearFolderCsv();
                        blockUser.logoutSession();
                        break;

                    default:
                        System.out.print("Comando errato o non esistente\n\n");
                        break;
                }
            }
        };
    }
}
