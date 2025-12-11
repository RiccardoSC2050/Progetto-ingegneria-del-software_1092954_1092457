package it.unibg.progetto.api.researchoncsv;

import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.components.Quit;
import it.unibg.progetto.api.conditions.StringValue;

public class ResearchChoice {

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

			String choice = GlobalScaner.scanner.nextLine().trim();

			switch (choice) {
			case "1":
				ManageResearchOnCsv.searchAndMaybeSave(StringValue.RUOLO);
				f = false;
				break;

			case "2":
				ManageResearchOnCsv.searchAndMaybeSaveByAnnoInizio();
				f = false;
				break;

			case "3":
				ManageResearchOnCsv.searchAndMaybeSave(StringValue.NOME);
				f = false;
				break;

			case "4":
				ManageResearchOnCsv.searchAndMaybeSave(StringValue.COGNOME);
				f = false;
				break;

			case "5":
				ManageResearchOnCsv.searchAndMaybeSave(StringValue.MAIL);
				f = false;
				break;

			case "6":
				ManageResearchOnCsv.searchAndMaybeSave(StringValue.NUMERO_TELEFONO);
				f = false;
				break;

			case "7":
				ManageResearchOnCsv.searchAndMaybeSaveByMarker();
				f = false;
				break;

			case "8":
				ManageResearchOnCsv.searchAndMaybeSave(StringValue.ID);
				f = false;
				break;

			case "q":
				Quit.quit(choice);
				f = false;
				break;

			default:
				System.out.println("Scelta non valida.\n");
				f = true;

			}

		} while (f);
	}
}
