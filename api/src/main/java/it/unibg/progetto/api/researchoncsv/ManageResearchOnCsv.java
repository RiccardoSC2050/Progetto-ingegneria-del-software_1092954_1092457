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

	// SISTEMA STANDARD PER SALVARE
	/**
	 * Chiede se salvare i risultati e, se sì, li salva in STANDARD.csv e poi nel DB
	 * con il nome scelto dall'utente.
	 */
	private static void askAndSaveResult(Session current, List<String[]> result) throws Exception {
		System.out.print("Vuoi salvare questi risultati in un nuovo CSV? [s/n]: ");
		String answer = GlobalScaner.scanner.nextLine().trim();
		if (!answer.equalsIgnoreCase("s")) {
			return;
		}

		System.out.print("Inserisci il nome del nuovo file (senza .csv): ");
		String finalName = GlobalScaner.scanner.nextLine().trim();

		// 1) salvo i risultati in STANDARD.csv nella folder temporanea
		MainResearch.saveSearchResult(CsvStandard.STANDARD.toString(), result, true);

		// 2) mando STANDARD nel DB con il nome scelto
		ActionOnCsv.getIstnce().changeNameFile(finalName);

		ActionOnCsv.getIstnce()
				.addNewFileInCsvTableFromCsvDto(ActionOnCsv.getIstnce().convertFileCsvToCsvDto(finalName, current));

		System.out.println("Ricerca salvata come file CSV \"" + finalName + "\" nel database.\n");
	}

	// RICERCA PER RUOLO
	/**
	 * da mettere in un'altra classe
	 * 
	 * @throws Exception
	 */
	public static void searchAndMaybeSaveByRuolo() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(current, CsvStandard.DOCUMENTO_AZIENDALE.toString());

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
		askAndSaveResult(ManagerSession.getCurrent(), result);
	}

	/**
	 * RICERCA MIRATA SU PERSONE POST ANNO INSERITO
	 * 
	 * @throws Exception
	 */
	public static void searchAndMaybeSaveByAnnoInizio() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(current, CsvStandard.DOCUMENTO_AZIENDALE.toString());

		System.out.print("Inserisci l'anno minimo di inizio (es. 2018): ");
		String annoStr = GlobalScaner.scanner.nextLine().trim();
		int anno = Integer.parseInt(annoStr);
		List<String[]> result = MainResearch.searchByAnnoInizioMaggioreUguale(anno);

		System.out.println("Risultato della ricerca:");
		ManageCsvFile.printRows(result);

		// 2) Controllo il livello di accesso
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la ricerca (access level < 2).");
			return;
		}

		// 3) Chiedo se vuole salvare
		askAndSaveResult(ManagerSession.getCurrent(), result);
	}

}
