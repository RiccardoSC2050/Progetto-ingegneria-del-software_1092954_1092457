package csv_manage;

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
public class CsvConversion {

	private final CsvService csvService;
	private final CsvMapper csvMapper;

	@Autowired
	public CsvConversion(CsvService csvService, CsvMapper csvMapper) {
		this.csvService = csvService;
		this.csvMapper = csvMapper;
	}

	/*
	 * creazione file csv
	 * 
	 * si identifica prima il livello utente fatto si prende id utente fatto si
	 * chiede nome file si salva in locale e contemporanemante in data
	 * 
	 */
	
	/**
	 * 
	 * @param current
	 * @throws IOException
	 */
	public void createFileCsv(Session current) throws IOException {
		if (!(current.getAccessLevel() > AccessLevel.AL2.getLevel()
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
		Path oldPath = Paths.get("/api/temporary_fileCSV_saving/" + CsvStandard.STANDARD + ".csv");

		if (!Files.exists(oldPath)) {
			throw new RuntimeException("Il file non esiste: " + oldPath);
		}

		Path newPath = Paths.get("/api/temporary_fileCSV_saving/" + nameFile + ".csv");

		Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
		return true;
	}

}
