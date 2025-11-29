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
import it.unibg.progetto.api.conditions.CsvStandard;
import it.unibg.progetto.api.csv_manage.ManageCsvFile;
import it.unibg.progetto.api.dto.CsvDto;

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

	public void saveAllFileInFolderIntoCsvTable(Session current) throws IOException {
		// creo lista dei file
		File folder = new File("../api/temporary_fileCSV_saving/");
		File[] allFiles = folder.listFiles();

		for (File f : allFiles) {
			String nameFile = f.getName().toString().replace(".csv", "");

			ActionOnCsv.getIstnce()
					.addNewFileInCsvTableFromCsvDto(ActionOnCsv.getIstnce().convertFileCsvToCsvDro(nameFile, current));
		}
	}

}
