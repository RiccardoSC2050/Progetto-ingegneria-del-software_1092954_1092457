package it.unibg.progetto.api.researchoncsv;


import java.util.List;

import it.unibg.progetto.api.access_session.ManagerSession;
import it.unibg.progetto.api.access_session.Session;
import it.unibg.progetto.api.action_on.ActionOnCsv;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.conditions.CsvStandard;
import it.unibg.progetto.api.csv_manage.ManageCsvFile;

public class ManageResearchOnCsv {

	/**
	 * da mettere in un'altra classe
	 * 
	 * @throws Exception
	 */
	public static void searchAndMaybeSaveByRuolo() throws Exception {
		Session current = ManagerSession.getCurrent();

		System.out.println("Inserisci il ruolo da cercare (es. Developer, HR, Manager):");
		String ruolo = GlobalScaner.scanner.nextLine();

		// 1) Faccio la ricerca sul file aziendale
		List<String[]> result = MainResearch.searchByRuolo(ruolo);

		if (result.isEmpty()) {
			System.out.println("Nessun dipendente trovato con ruolo: " + ruolo);
			return;
		}

		System.out.println("Risultato della ricerca:");
		ManageCsvFile.printRows(result);

		// 2) Controllo il livello di accesso
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la ricerca (access level < 2).");
			return;
		}

		// 3) Chiedo se vuole salvare
		System.out.println("Vuoi salvare questa ricerca come file CSV? (s/n)");
		String answer = GlobalScaner.scanner.nextLine();

		if (!answer.equalsIgnoreCase("s")) {
			return; // l'utente non vuole salvare
		}

		System.out.println("Inserisci il nome del file da salvare (senza .csv):");
		String finalName = GlobalScaner.scanner.nextLine();

		// 4) Salvo temporaneamente la ricerca con nome STANDARD.csv
		MainResearch.saveSearchResult(CsvStandard.STANDARD.toString(), result, true);

		// 5) Uso ActionOnCsv per rinominare STANDARD -> finalName, salvarlo in DB e
		// cancellare il file locale
		ActionOnCsv.getIstnce().changeNameFile(finalName);

		ActionOnCsv.getIstnce().addNewFileInCsvTableFromCsvDto(
				ActionOnCsv.getIstnce().convertFileCsvToCsvDto(finalName, ManagerSession.getCurrent()));
	}
}
