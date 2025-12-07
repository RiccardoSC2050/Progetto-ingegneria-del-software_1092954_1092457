package it.unibg.progetto.api.researchoncsv;

import java.util.List;

import it.unibg.progetto.api.components.GlobalScaner;

public class ResearchChoice {

	/**
	 * classe gestione finale delle ricerche
	 * 
	 * Chiede all'utente il tipo di ricerca e la esegue sul DOCUMENTO_AZIENDALE.
	 * Restituisce la lista di righe trovate (o null se scelta non valida / errore).
	 * 
	 * @throws Exception
	 */
	public static void askAndExecuteSearch() throws Exception {
		System.out.println("=== RICERCHE SU DOCUMENTO_AZIENDALE ===");
		System.out.println("1) Cerca per ruolo");
		System.out.println("2) Cerca per anno di inizio (>= anno)");
		System.out.print("Scelta: ");

		String choice = GlobalScaner.scanner.nextLine().trim();
		List<String[]> result = null;

		switch (choice) {
		case "1":
			ManageResearchOnCsv.searchAndMaybeSaveByRuolo();
			break;

		case "2":
			ManageResearchOnCsv.searchAndMaybeSaveByAnnoInizio();
			break;

		default:
			System.out.println("Scelta non valida.\n");
			break;
		}

	}
}
