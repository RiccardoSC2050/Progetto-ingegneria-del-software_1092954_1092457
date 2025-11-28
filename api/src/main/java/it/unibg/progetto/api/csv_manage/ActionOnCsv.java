package it.unibg.progetto.api.csv_manage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unibg.progetto.api.access_session.Session;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.conditions.CsvStandard;
import it.unibg.progetto.data.Csv;
import it.unibg.progetto.service.CsvService;

@Component
public class ActionOnCsv {

	private static ActionOnCsv istance;
	private final CsvService csvService;
	private final CsvMapper csvMapper;

	@Autowired
	public ActionOnCsv(CsvService csvService, CsvMapper csvMapper) {
		this.csvService = csvService;
		this.csvMapper = csvMapper;
		istance = this;
	}

	public static ActionOnCsv getIstnce() {
		return istance;
	}

	/*
	 * creazione file csv
	 * 
	 * si identifica prima il livello utente fatto si prende id utente fatto si
	 * chiede nome file si salva in locale e contemporanemante in data
	 * 
	 */

	/**
	 * se l'utente può salvare il file per se, allora dopo avere controllato il suo
	 * livello di accesso e dopo avere prseo il suo uuid si chiede il nome file, se
	 * il nome file non è gia tra i suoi nomifile, allora converte il nome del file
	 * ricerca come lo vuole lui e poi si aggiorna il database
	 * 
	 * @param current
	 * @throws IOException
	 */
	public void saveFileCsvOnData(Session current) throws IOException {
		if (!(current.getAccessLevel() >= AccessLevel.AL2.getLevel()
				&& AccessLevel.isAPossibleValue(current.getAccessLevel()))) {
			System.out.println("ERRORE: non ti è possibile salavre il file");
		}
		String uuidUser = current.getUuid();
		boolean flag;
		String nameFile;
		do {
			System.out.print("Inserire il nome del file: ");
			nameFile = GlobalScaner.scanner.nextLine();
			flag = checkNamefile(nameFile, uuidUser); // true means no equals name

		} while (!flag);
		// change filename in local csv
		changeNameFile(nameFile);
		// save on database
		csvMapper.addCSVToDataBase(csvService, nameFile, uuidUser);
	}

	// controllo nome esiste già nei suoi file?
	private boolean checkNamefile(String nameFile, String uuidUser) {
		List<CsvDto> csvListPrivate = csvMapper.ListCsvFromDatabaseWithSpecificUUID(csvService, uuidUser);
		if (csvListPrivate == null) {
			return true;
		}
		for (CsvDto c : csvListPrivate) {

			if (c.getFileName().equals(nameFile)) {
				return false;
			}
		}
		return true;
	}

	// metodo che converte il nome del file standard creato dalla ricerca, nel nome
	// file utente
	// valore standard name = file -> assegnato automaticamente dal software quando
	// si svolge una ricerca

	private boolean changeNameFile(String nameFile) throws IOException {
		Path oldPath = Paths.get("../api/temporary_fileCSV_saving/" + CsvStandard.STANDARD + ".csv");

		if (!Files.exists(oldPath)) {
			throw new RuntimeException("Il file non esiste: " + oldPath);
		}

		Path newPath = Paths.get("../api/temporary_fileCSV_saving/" + nameFile + ".csv");

		Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
		return true;
	}

	public void createLocalFile(CsvDto c) throws Exception {
		csvMapper.createLocalFileFromCsvDto(c);
	}

	public void saveAllFileCsvFromDataOfUser(Session current) throws Exception {
		String uuid = current.getUuid();

		List<CsvDto> csvdtoList = csvMapper.ListCsvFromDatabaseWithSpecificUUID(csvService, uuid);

		for (CsvDto c : csvdtoList) {
			createLocalFile(c);
		}

	}

	public void deleteAllFileInRepo() {
		File folder = new File("../api/temporary_fileCSV_saving/");
		File[] allFiles = folder.listFiles();

		if (allFiles != null) {
			for (File f : allFiles) {
				if (f.isFile()) {
					f.delete();
				}
			}
		}
	}

	public void importFileFromLocalPc(String path) throws IOException {
		Path p = Paths.get(path);
		byte[] bytes = Files.readAllBytes(p);
		Path standardname = Paths.get("../api/temporary_fileCSV_saving/" + CsvStandard.STANDARD + ".csv");

		Files.write(standardname, bytes);
	}

}
