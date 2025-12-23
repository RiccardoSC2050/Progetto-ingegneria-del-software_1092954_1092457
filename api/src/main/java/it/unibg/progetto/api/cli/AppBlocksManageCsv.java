package it.unibg.progetto.api.cli;


import java.io.File;
import java.io.IOException;

import java.nio.file.Paths;
import java.util.List;

import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.api.application.usecase.CsvUseCase;
import it.unibg.progetto.api.cli.components.Constant;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.cli.components.Quit;
import it.unibg.progetto.api.cli.research.CsvResearchChoice;
import it.unibg.progetto.api.domain.rules.CsvStandard;
import it.unibg.progetto.api.infrastructure.csv.CsvFileManager;
import it.unibg.progetto.api.security.session.Session;
import it.unibg.progetto.api.security.session.SessionManager;

public class AppBlocksManageCsv {

	public AppBlocksManageCsv() {
	}

	public void controllOnFolderCsv() {
		CsvFileManager.getTempFolder();
	}

	/**
	 * CONTROLLO PRESENZA DEL FILE DI RIFERIMENTO AZIENDALE (DOCUMENTO_AZIENDALE)
	 * 
	 * @return
	 */
	private boolean checkControlImportMainFileCsv() {
		List<CsvDto> csvDtoList = CsvUseCase.getIstnce().returnAllFileCsvDtoFromData();
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
			String path = GlobalScanner.scanner.nextLine().strip();

			CsvUseCase.getIstnce().importFileFromLocalPc(path, SessionManager.getCurrent());
		} catch (Exception e) {
		}
	}

	public void clearFolderCsv() {
		controllOnFolderCsv();
		CsvUseCase.getIstnce().deleteAllFileInRepo();
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
			name = GlobalScanner.scanner.nextLine().strip();
			if (!CsvUseCase.getIstnce().checknameFileAlreadyExist(name, SessionManager.getCurrent().getUuid()))
				break;
		} while (true);
		CsvFileManager.createFileCsvOnFolder(name);
	}

	/**
	 * SALVA TUTTI I FILE NON ESISTENTI IN DATA
	 * 
	 * @param current
	 * @throws IOException
	 */
	public void saveAllFileInFolderIntoCsvTable() throws IOException {
		// creo lista dei file
		File folder = new File(Constant.getFilePathCsv());
		File[] allFiles = folder.listFiles();

		for (File f : allFiles) {
			String nameFile = f.getName().toString().replace(".csv", "");

			if (!CsvUseCase.getIstnce().checknameFileAlreadyExistOnlyInData(nameFile,
					SessionManager.getCurrent().getUuid()))
				CsvUseCase.getIstnce().addNewFileInCsvTableFromCsvDto(
						CsvUseCase.getIstnce().convertFileCsvToCsvDto(nameFile, SessionManager.getCurrent()));
		}
	}

	/**
	 * MOSTRA I FILE IN LETTURA
	 * 
	 * @throws Exception
	 */
	public void readFileCsv() throws Exception {
		lsFileUser();
		boolean f;
		do {
			System.out.println("Quale vuoi visualizzare?");
			String name = GlobalScanner.scanner.nextLine().strip();
			if (Quit.quit(name))
				return;
			f = CsvUseCase.getIstnce().showFileContent(name, SessionManager.getCurrent().getUuid());

		} while (!f);
	}

	/**
	 * ritorna i file dell'utente loggato
	 */
	public void lsFileUser() {
		System.out.println("I tuoi file:");
		CsvUseCase.getIstnce().stampListOfMyCsv(SessionManager.getCurrent().getUuid());
		System.out.println();
	}

	/**
	 * AVVIA RICERCA MIRATA
	 * 
	 * @throws Exception
	 */
	// Metodo principale: gestisce il flusso generale
	public void searchOnBaseAndMaybeSave() throws Exception {

		// 1) chiedo che tipo di ricerca fare e la eseguo
		CsvResearchChoice.askAndExecuteSearch();

		CsvUseCase.getIstnce().deleteAllFileInRepo();
	}

	/**
	 * AVVIA RICERCA STATISTICA
	 * 
	 * @throws Exception
	 */
	// Metodo principale: gestisce il flusso generale
	public void searchOnBaseStatistic() throws Exception {

		// 1) chiedo che tipo di ricerca fare e la eseguo
		CsvResearchChoice.askAndExecuteStatisticSearch();
		CsvUseCase.getIstnce().deleteAllFileInRepo();
	}

	/**
	 * METODO CHE ELIMINA UN FILE CSV DAL DATABASE
	 */
	public void deleteMyCsvFromDatabase() {
		Session current = SessionManager.getCurrent();

		List<CsvDto> list = CsvUseCase.getIstnce().returnAllFileCsvDtoFromDataOfUser(current.getUuid());
		if (list == null || list.isEmpty()) {
			System.out.println("Non hai ancora nessun file CSV salvato nel database.\n");
			return;
		}

		System.out.println("=== I FILE CSV CHE PUOI CANCELLARE ===");
		CsvUseCase.getIstnce().stampListOfMyCsv(SessionManager.getCurrent().getUuid());

		System.out.print("Quale vuoi eliminare? [numero, oppure 'q' per annullare]: ");

		String input = GlobalScanner.scanner.nextLine().trim();
		if (Quit.quit(input))
			return;

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
		System.out.print("Sei sicuro di voler eliminare \"" + chosen.getFileName() + "\"? [s/n]: ");
		String confirm = GlobalScanner.scanner.nextLine().trim();
		if (!confirm.equalsIgnoreCase("s")) {
			System.out.println("Eliminazione annullata.\n");
			return;
		}

		boolean deleted = CsvUseCase.getIstnce().deleteMyCsvByIndex(index, current);
		if (deleted) {
			System.out.println("File \"" + chosen.getFileName() + "\" eliminato.\n");
		} else {
			System.out.println("Impossibile eliminare il file.\n");
		}
	}
	
	

}