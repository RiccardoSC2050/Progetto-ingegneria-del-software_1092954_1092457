package it.unibg.progetto.api.application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import it.unibg.progetto.api.access_session.ManagerSession;
import it.unibg.progetto.api.access_session.Session;
import it.unibg.progetto.api.action_on.ActionOnCsv;
import it.unibg.progetto.api.action_on.ActionOnUseRS;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.conditions.CsvStandard;
import it.unibg.progetto.api.csv_manage.ManageCsvFile;
import it.unibg.progetto.api.dto.CsvDto;
import it.unibg.progetto.api.researchoncsv.MainResearch;
import it.unibg.progetto.api.researchoncsv.ResearchChoice;


public class AppBlocksManageCsv {

	public AppBlocksManageCsv() {
	}

	/**
	 * CONTROLLO PRESENZA DEL FILE DI RIFERIMENTO AZIENDALE (DOCUMENTO_AZIENDALE)
	 * 
	 * @return
	 */
	private boolean checkControlImportMainFileCsv() {
		List<CsvDto> csvDtoList = ActionOnCsv.getIstnce().returnAllFileCsvDtoFromData();
		if (csvDtoList == null)
			return false;
		for (CsvDto c : csvDtoList) {
			if (c.getFileName().equals(CsvStandard.DOCUMENTO_AZIENDALE.toString())) {
				return true;
			}
		}
		return false;

	}

	/**
	 * CARICAMENTO DEL FILE AZIENDALE DI RIFERIMENTO
	 * 
	 * @throws IOException
	 */
	private void importMainFile() throws IOException {

		try {
			System.out.println("inserisci file aziendale di riferimento:");
			System.out.println("inserire percorso file");
			String path = GlobalScaner.scanner.nextLine();

			ActionOnCsv.getIstnce().importFileFromLocalPc(path, ManagerSession.getCurrent());
		} catch (Exception e) {
		}
	}

	public void clearFolderCsv() {
		ActionOnCsv.getIstnce().deleteAllFileInRepo();
	}

	/**
	 * CASO IN CUI SI STA AVVIANDO E CONFIGURANDO PER LA PRIMA VOLTA L'APP
	 * 
	 * @throws IOException
	 */
	public void manageImplementationOfMainFileCsv() throws IOException {
		if (!checkControlImportMainFileCsv()) {
			System.out.println("IMPORTANTE CARICARE IL FILE DI RIFERIMENTO DELL'AZIENDA");
			importMainFile();
		}
	}

	// CREAZIONE DI UN PROPRIO FILE CSV
	/**
	 * permette di creare un file csv a caso nel folder sarà un file personale
	 * dell'utente
	 */
	public void createGeneralFileCsv() {
		String name;
		do {
			System.out.println("inserire il nome del file casuale");
			name = GlobalScaner.scanner.nextLine();
			if (!ActionOnCsv.getIstnce().checknameFileAlreadyExist(name, ManagerSession.getCurrent()))
				break;
		} while (true);
		ManageCsvFile.createFileCsvOnFolder(name);
	}

	/**
	 * SALVA TUTTI I FILE NON ESISTENTI IN DATA
	 * 
	 * @param current
	 * @throws IOException
	 */
	public void saveAllFileInFolderIntoCsvTable(Session current) throws IOException {
		// creo lista dei file
		File folder = new File("../api/temporary_fileCSV_saving/");
		File[] allFiles = folder.listFiles();

		for (File f : allFiles) {
			String nameFile = f.getName().toString().replace(".csv", "");

			if (!ActionOnCsv.getIstnce().checknameFileAlreadyExistOnlyInData(nameFile, ManagerSession.getCurrent()))
				ActionOnCsv.getIstnce().addNewFileInCsvTableFromCsvDto(
						ActionOnCsv.getIstnce().convertFileCsvToCsvDto(nameFile, current));
		}
	}

	/**
	 * MOSTRA I FILE IN LETTURA
	 * 
	 * @throws Exception
	 */
	public void readFileCsv() throws Exception {
		System.out.println("I tuoi file:");
		ActionOnCsv.getIstnce().stampListOfMyCsv(ManagerSession.getCurrent());
		System.out.println();
		boolean f;
		do {
			System.out.println("Quale vuoi visualizzare?");
			String name = GlobalScaner.scanner.nextLine();
			f = ActionOnCsv.getIstnce().showFileContent(name, ManagerSession.getCurrent());

		} while (!f);
	}

	/**
	 * da appblock
	 * 
	 * @throws Exception
	 */
	// Metodo principale: gestisce il flusso generale
	public void searchOnBaseAndMaybeSave() throws Exception {
		Session current = ManagerSession.getCurrent();

		// 1) chiedo che tipo di ricerca fare e la eseguo
		List<String[]> result = ResearchChoice.askAndExecuteSearch();

		if (result == null || result.isEmpty()) {
			System.out.println("Nessun risultato trovato.\n");
			return;
		}

		// 2) stampo i risultati
		System.out.println("\nRisultati trovati:");
		ManageCsvFile.printRows(result);
		System.out.println();

		// 3) se l'utente è livello 1 può solo vedere
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Hai livello di accesso 1: puoi solo visualizzare i risultati, non salvarli.\n");
			return;
		}

		// 4) AL2 o AL3: posso chiedere se vuole salvare
		askAndSaveResult(current, result);
	}

	/**
	 * Chiede se salvare i risultati e, se sì, li salva in STANDARD.csv e poi nel DB
	 * con il nome scelto dall'utente.
	 */
	private void askAndSaveResult(Session current, List<String[]> result) throws Exception {
		System.out.print("Vuoi salvare questi risultati in un nuovo CSV? [y/n]: ");
		String answer = GlobalScaner.scanner.nextLine().trim();
		if (!answer.equalsIgnoreCase("y")) {
			return;
		}

		System.out.print("Inserisci il nome del nuovo file (senza .csv): ");
		String finalName = GlobalScaner.scanner.nextLine().trim();

		// 1) salvo i risultati in STANDARD.csv nella folder temporanea
		MainResearch.saveSearchResult(CsvStandard.STANDARD.toString(), result, true);

		// 2) mando STANDARD nel DB con il nome scelto
		ActionOnCsv.getIstnce()
				.addNewFileInCsvTableFromCsvDto(ActionOnCsv.getIstnce().convertFileCsvToCsvDto(finalName, current));

		System.out.println("Ricerca salvata come file CSV \"" + finalName + "\" nel database.\n");
	}

	/**
	 * forse da appbloc
	 */
	public void deleteMyCsvFromDatabase() {
		Session current = ManagerSession.getCurrent();

		List<CsvDto> list = ActionOnCsv.getIstnce().returnAllFileCsvDtoFromDataOfUser(current.getUuid());
		if (list == null || list.isEmpty()) {
			System.out.println("Non hai ancora nessun file CSV salvato nel database.\n");
			return;
		}

		System.out.println("=== I TUOI CSV SALVATI NEL DB ===");
		ActionOnCsv.getIstnce().stampListOfMyCsv(ManagerSession.getCurrent());

		System.out.print("Quale vuoi eliminare? [numero, oppure 'q' per annullare]: ");

		String input = GlobalScaner.scanner.nextLine().trim();
		if (input.equalsIgnoreCase("q")) {
			return;
		}

		int index;
		try {
			index = Integer.parseInt(input) - 1;
		} catch (NumberFormatException e) {
			System.out.println("Scelta non valida.\n");
			return;
		}

		if (index < 0 || index >= list.size()) {
			System.out.println("Indice fuori intervallo.\n");
			return;
		}

		CsvDto chosen = list.get(index);
		System.out.print("Sei sicuro di voler eliminare \"" + chosen.getFileName() + "\"? [y/n]: ");
		String confirm = GlobalScaner.scanner.nextLine().trim();
		if (!confirm.equalsIgnoreCase("y")) {
			System.out.println("Eliminazione annullata.\n");
			return;
		}

		boolean deleted = ActionOnCsv.getIstnce().deleteMyCsvByIndex(index, current);
		if (deleted) {
			System.out.println("File \"" + chosen.getFileName() + "\" eliminato.\n");
		} else {
			System.out.println("Impossibile eliminare il file.\n");
		}
	}

}