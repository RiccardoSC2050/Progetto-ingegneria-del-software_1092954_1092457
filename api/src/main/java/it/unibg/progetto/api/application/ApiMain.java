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
    public CommandLineRunner createDefaultUser(
            UserMapper userMapper,
            UsersService service,
            ActionOnUseRS conversionUseRS,
            RootMapper rootMapper
    ) {
        return args -> {
            AppBlocks ab = new AppBlocks();

            // Configurazione iniziale di Root (crea/legge root dal DB)
            Root.configurationOfRoot();

            // Primo tentativo di login gestito da AppBlocks
            ab.loginSession();

            // Loop principale dell'applicazione
            while (true) {
                // Controllo la sessione corrente
                Session current = ManagerSession.getCurrent();

                if (current == null) {
                    // Nessuna sessione attiva: chiedo di rifare il login
                    System.out.println("Nessuna sessione attiva. Effettua nuovamente il login.");
                    ab.loginSession();
                    current = ManagerSession.getCurrent();

                    // Se ancora null, esco in modo pulito invece di lanciare un NPE
                    if (current == null) {
                        System.out.println("Login non riuscito. Chiusura applicazione.");
                        return;
                    }
                }

                // A questo punto current NON è null
                System.out.print(current.getName() + "> ");
                String input = GlobalScaner.scanner.nextLine();

                switch (input) {
                    case "exit":
                        Exit.exit(input);
                        break;

                    case "clear":
                        ClearTerminal.clearTerminal(input);
                        break;

                    case "out":
                        // logout dell'utente corrente
                        ab.logoutSession();
                        // al prossimo giro del while, current sarà null
                        // e verrà richiesto un nuovo login
                        break;

                    default:
                        System.out.print("Comando errato o non esistente\n\n");
                        break;
                }
            }
        };
    }
}
