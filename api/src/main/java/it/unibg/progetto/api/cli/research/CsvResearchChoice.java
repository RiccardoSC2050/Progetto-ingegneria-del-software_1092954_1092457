package it.unibg.progetto.api.cli.research;


import java.nio.file.Paths;

import it.unibg.progetto.api.application.usecase.CsvUseCase;
import it.unibg.progetto.api.cli.components.Constant;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.cli.components.Quit;
import it.unibg.progetto.api.domain.rules.CsvStandard;
import it.unibg.progetto.api.domain.rules.StringValues;

public class CsvResearchChoice {

	private static void menuOfMainReserch() {
		System.out.println();
		System.out.println("==========================================");
		System.out.println("      RICERCHE SU DOCUMENTO AZIENDALE     ");
		System.out.println("==========================================");
		System.out.println("1) Cerca per ruolo");
		System.out.println("2) Cerca per anno di inizio (>= anno)");
		System.out.println("3) Cerca per nome");
		System.out.println("4) Cerca per cognome");
		System.out.println("5) Cerca per indirizzo email");
		System.out.println("6) Cerca per numero di telefono");
		System.out.println("7) Cerca per numero di richiami");
		System.out.println("8) Cerca per ID");
		System.out.println("------------------------------------------");
		System.out.println("q) Torna al menu precedente");
		System.out.println("------------------------------------------");
		System.out.print("Scelta: ");
	}

	private static void menuOfMainStatisticalReserch() {
		System.out.println();
		System.out.println("==========================================");
		System.out.println("   STATISTICHE SU DOCUMENTO AZIENDALE     ");
		System.out.println("==========================================");
		System.out.println("1) Numero totale dipendenti (righe valide)");
		System.out.println("2) Conteggio dipendenti per ruolo");
		System.out.println("3) Percentuali per ruolo");
		System.out.println("4) Anni di inizio: minimo / massimo");
		System.out.println("5) Anni di inizio: media (anzianità media)");
		System.out.println("6) Dipendenti per anno di inizio (distribuzione)");
		System.out.println("7) Richiami: totale / media / minimo / massimo");
		System.out.println("8) Top 5 dipendenti con più richiami");
		System.out.println("9) Dipendenti con 0 richiami");
		System.out.println("10) Qualità dati: campi mancanti (nome/cognome/email/tel/ruolo/anno)");
		System.out.println("------------------------------------------");
		System.out.println("q) Torna al menu precedente");
		System.out.println("------------------------------------------");
		System.out.print("Scelta: ");
	}

	/**
	 * classe gestione finale delle ricerche
	 * 
	 * Chiede all'utente il tipo di ricerca e la esegue sul DOCUMENTO_AZIENDALE.
	 * Restituisce la lista di righe trovate (o null se scelta non valida / errore).
	 * 
	 * @throws Exception
	 */
	public static void askAndExecuteSearch() throws Exception {
		boolean f;
		do {
			f = true;
			menuOfMainReserch();

			String choice = GlobalScanner.scanner.nextLine().trim();

			switch (choice) {
			case "1":
				CsvResearchManager.searchAndMaybeSave(StringValues.RUOLO);

				break;

			case "2":
				CsvResearchManager.searchAndMaybeSaveByAnnoInizio();

				break;

			case "3":
				CsvResearchManager.searchAndMaybeSave(StringValues.NOME);

				break;

			case "4":
				CsvResearchManager.searchAndMaybeSave(StringValues.COGNOME);

				break;

			case "5":
				CsvResearchManager.searchAndMaybeSave(StringValues.MAIL);

				break;

			case "6":
				CsvResearchManager.searchAndMaybeSave(StringValues.NUMERO_TELEFONO);

				break;

			case "7":
				CsvResearchManager.searchAndMaybeSaveByMarker();

				break;

			case "8":
				CsvResearchManager.searchAndMaybeSave(StringValues.ID);

				break;

			case "q":
				Quit.quit(choice);
				f = false;
				break;

			default:
				System.out.println("Scelta non valida.\n");

			}
			CsvUseCase.getIstnce().deleteOneFileInRepo(
					Paths.get(Constant.getFilePathCsv() + CsvStandard.DOCUMENTO_AZIENDALE.toString() + ".csv"));
		} while (f);
	}

	public static void askAndExecuteStatisticSearch() throws Exception {
		boolean f;
		do {
			f = true;
			menuOfMainStatisticalReserch();

			String choice = GlobalScanner.scanner.nextLine().trim();

			switch (choice) {
			case "1":
				System.out.println("Ci sono " + CsvStatisticsResearch.countTotalValidEmployees() + " dipendenti");

				break;

			case "2":
				CsvResearchManager.statsAndMaybeSaveCountByRole();

				break;

			case "3":
				CsvResearchManager.statsAndMaybeSavePercentByRole();

				break;

			case "4":
				CsvResearchManager.statsAndMaybeSaveMinMaxAnnoInizio();

				break;

			case "5":
				CsvResearchManager.statsAndMaybeSaveAverageAnnoInizio();

				break;

			case "6":
				CsvResearchManager.statsAndMaybeSaveDistributionByStartYear();

				break;

			case "7":
				CsvResearchManager.statsAndMaybeSaveRichiamiSummary();

				break;

			case "8":
				CsvResearchManager.statsAndMaybeSaveTop5Richiami();

				break;
				
			case "9":
				CsvResearchManager.statsAndMaybeSaveZeroRichiami();

				break;
				
			case "10":
				CsvResearchManager.statsAndMaybeSaveMissingFields();

				break;

			case "q":
				Quit.quit(choice);
				f = false;
				break;

			default:
				System.out.println("Scelta non valida.\n");

			}

			CsvUseCase.getIstnce().deleteOneFileInRepo(
					Paths.get(Constant.getFilePathCsv() + CsvStandard.DOCUMENTO_AZIENDALE.toString() + ".csv"));

		} while (f);
	}
}
