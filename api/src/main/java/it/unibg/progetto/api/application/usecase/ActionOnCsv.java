package it.unibg.progetto.api.application.usecase;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.api.cli.components.Constant;
import it.unibg.progetto.api.domain.rules.CsvStandard;
import it.unibg.progetto.api.infrastructure.csv.ManageCsvFile;
import it.unibg.progetto.api.mapping.CsvMapper;
import it.unibg.progetto.api.security.Session;
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

	// QUANDO SI è LOGGATI OGNI FILE NEL FOLDER TEMPORANEO è DELL'UTENTE
	// TUTTI QUELLI PRESI PER ESSERE VISTI VENGONO DOPO LA LETTURA ELIMINATI DAL
	// FOLDER

	/**
	 * CSVDTO -> File.csv
	 * 
	 * da csvdto a file csv nel folder una volta convertito il file da csv a csvdto
	 * si puo aggiungere il file nel folder temporaneo
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createLocalFileFromCsvDto(CsvDto c) throws Exception {
		if (c == null) {
			System.out.println("ERRORE, il file/ oggetto non esiste");
		}
		String nameFile = c.getFileName();
		Path localFile = Paths.get(Constant.getFilePathCsv() + nameFile + ".csv");

		Files.write(localFile, c.getData());

	}

	/**
	 * File.csv -> CSVDTO
	 * 
	 * converte un file.csv in un csvdto legge nome del file controlla se esiste e
	 * poi lo converte in base il tipo di utente che è loggato crenando un oggetto
	 * senza id quindi se si usa questo sarà necessario un metodo che controlla se
	 * il nome file e uuid sono gia presneti nel data
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public CsvDto convertFileCsvToCsvDto(String name, Session current) throws IOException {
		Path p = Paths.get(Constant.getFilePathCsv() + name + ".csv");

		if (!Files.exists(p)) {
			throw new RuntimeException("Il file non esiste: " + p);
		}
		byte[] bytes = Files.readAllBytes(p);
		CsvDto cdto = new CsvDto(name, current.getUuid(), bytes);

		return cdto;
	}

	/**
	 * CSVDTO -> CSV senza id
	 * 
	 * @param c
	 * @return
	 */
	public Csv conversionFromCsvdtoToCsvNoId(CsvDto c) {
		Csv csv = csvMapper.toCsvFromCsvDtoNoId(c);

		return csv;
	}

	/**
	 * CSVDTO -> CSV con id
	 * 
	 * @param c
	 * @return
	 */
	public Csv conversionFromCsvdtoToCsvWithId(CsvDto c) {
		Csv csv = csvMapper.toCsvFromCsvDtoWithId(c);

		return csv;
	}

	/**
	 * CSV -> CSVDTO converte un csv in un csvdto
	 * 
	 * @param csv
	 * @return
	 */
	public CsvDto conversionFromCsvToCsvDto(Csv csv) {
		CsvDto c = csvMapper.csvConverterFromData(csv);
		return c;
	}

	/**
	 * SALVA UN OGGETTO CSVDTO NELLA CSV TABLE
	 * 
	 * @param c
	 */
	public void addNewFileInCsvTableFromCsvDto(CsvDto c) {

		csvMapper.addCSVToDataBase(csvService, conversionFromCsvdtoToCsvNoId(c));
	}

//ancora da fare il metodo che usa questo metodo
	/**
	 * DOPO AVER CREATO UNA RICERCA QUESTA VIENE MOMENTANEAMENTE SALVATA CON MOME
	 * STANDARD, MA QUANDO L'UTENTE DECIDE DI SALVARLA ALLORA SI USA QUESTO METODO
	 * CHE SERVE PER CAMBIARE IL NOME DELLA RICERCA IN UN NOME DIVERSO DA STANDAD E
	 * ALLORA SI CAMBIA
	 * 
	 * @param nameFile
	 * @return
	 * @throws IOException
	 */
	public boolean changeNameFile(String nameFile) throws IOException {
		Path oldPath = Paths.get(Constant.getFilePathCsv() + CsvStandard.STANDARD + ".csv");

		if (!Files.exists(oldPath)) {
			throw new RuntimeException("Il file non esiste: " + oldPath);

		}

		Path newPath = Paths.get(Constant.getFilePathCsv() + nameFile + ".csv");

		Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
		return true;
	}

	/**
	 * TRASFERISCE NEL FOLDER TEMPORANEO I FILE CSV DELL'UETNTE LOGGATO
	 * 
	 * @param current
	 * @throws Exception
	 */
	public void saveAllFileCsvFromDataOfUser(Session current) throws Exception {
		String uuid = current.getUuid();

		List<CsvDto> csvdtoList = csvMapper.ListCsvFromDatabaseWithSpecificUUID(csvService, uuid);

		for (CsvDto c : csvdtoList) {
			createLocalFileFromCsvDto(c);
		}

	}

	/**
	 * RITORNA UN solo specifico FILE ESISTENTE NEL DATA SOLO DAL NOME
	 * 
	 * @param current
	 * @throws Exception
	 */
	public void saveOneFileCsvFromData(String name) throws Exception {

		List<CsvDto> csvdtoList = csvMapper.ListCsvFromDatabase(csvService);

		for (CsvDto c : csvdtoList) {
			if (c.getFileName().equals(name))
				createLocalFileFromCsvDto(c);
		}

	}

	/**
	 * CANCELLA OGNI FILE NEL FOLDER TEMPORANEO USO SPECIFICO NEL POST LOGOUT
	 */
	public void deleteAllFileInRepo() {
		File folder = new File(Constant.getFilePathCsv());
		File[] allFiles = folder.listFiles();

		for (File f : allFiles) {
			if (f.isFile()) {
				f.delete();

			}
		}
	}

	/**
	 * CANCELLA UN FILE NEL FOLDER TEMPORANEO USO SPECIFICO NEL POST LOGOUT
	 */
	public void deleteOneFileInRepo(Path p) {
		File file = new File(p.toString());

		if (file != null)
			file.delete();

	}

	/**
	 * ritorna una lista di oggettI csvdto che contengono info di ogni file csv
	 * salvato
	 * 
	 * @return
	 */
	public List<CsvDto> returnAllFileCsvDtoFromData() {
		return csvMapper.ListCsvFromDatabase(csvService);

	}

	/**
	 * RITORNA UNA LISTA DI UUID DEI SOLI UTENTI CHE HANNO ALMENO UN FILE
	 * 
	 * @param csvdtoList
	 * @return
	 */
	public List<String> returnOnlyUserThatHaveAFile(List<CsvDto> csvdtoList) {
		List<String> idList = new ArrayList<String>();
		for (CsvDto c : csvdtoList) {
			if (!idList.contains(c.getOwnerId())) {
				idList.add(c.getOwnerId());
			}
		}
		return idList;
	}

	/**
	 * RITORNA UNA LISTA DI CSVDTO DI FILE CSV DEL FILE CHE L'UTENTE HA SALVATO
	 * 
	 * @param uuid
	 * @return
	 */
	public List<CsvDto> returnAllFileCsvDtoFromDataOfUser(String uuid) {
		return csvMapper.ListCsvFromDatabaseWithSpecificUUID(csvService, uuid);

	}

	public List<Csv> returnListCsv() {
		return csvMapper.ListCSv(csvService);
	}

	/**
	 * CONTROLLA SE IL NOME FILE ESISTE GIA, SE ESISTE RITORNA TRUE ALTRIMENTI FALSE
	 * CONTROLLA SIA NEL DATA CHE NEL FOLDER
	 * 
	 * @param name
	 * @param current
	 * @return
	 */
	public boolean checknameFileAlreadyExist(String name, String uuid) {

		if (checknameFileAlreadyExistOnlyInFolder(name))
			return true;

		if (checknameFileAlreadyExistOnlyInData(name, uuid))
			return true;

		return false;
	}

	/**
	 * CONTROLLA SE IL NOME FILE ESISTE GIA IN DATA, SE ESISTE RITORNA TRUE
	 * ALTRIMENTI FALSE
	 * 
	 * 
	 * @param name
	 * @param current
	 * @return
	 */
	public boolean checknameFileAlreadyExistOnlyInData(String name, String uuid) {

		List<CsvDto> csvdtoLsit = returnAllFileCsvDtoFromDataOfUser(uuid);
		for (CsvDto c : csvdtoLsit) {
			if (name.equals(c.getFileName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * RITORNA IL NOME FILE CERCATO IN DATA
	 * 
	 * 
	 * @param name
	 * @param current
	 * @return
	 */
	private Csv whatIsTheLuckyNameFile(String name) {
		List<Csv> csvList = returnListCsv();
		for (Csv c : csvList) {
			if (name.equals(c.getFileName())) {
				return c;
			}
		}
		return null;
	}

	/**
	 * CONTROLLA SE IL NOME FILE ESISTE GIA IN FOLDER, SE ESISTE RITORNA TRUE
	 * ALTRIMENTI FALSE
	 * 
	 * 
	 * @param name
	 * @param current
	 * @return
	 */
	public boolean checknameFileAlreadyExistOnlyInFolder(String name) {
		File folder = new File(Constant.getFilePathCsv());
		File[] allFiles = folder.listFiles();

		for (File f : allFiles) {
			String nameFile = f.getName().toString().replace(".csv", "");
			if (name.equals(nameFile)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * STAMPA LISTA DI TUTTI I FILE CHE L'UTENTE POSSIEDE
	 */
	public void stampListOfMyCsv(String uuid) {
		List<CsvDto> cList = returnAllFileCsvDtoFromDataOfUser(uuid);

		for (CsvDto c : cList) {
			System.out.println(c.getFileName());
		}
	}

	/**
	 * STAMPA IL FILE CHE L'UTENTE VUOLE LEGGERE, SE IL FILE NON è NE PRESENTE IN
	 * DATA NE IN LOCALE, NON STAMPA NULLA MA RITORNA FALSE
	 * 
	 * @param name
	 * @param current
	 * @return
	 * @throws Exception
	 */
	public boolean showFileContent(String name, String uuid) throws Exception {
		try {
			if (checknameFileAlreadyExistOnlyInFolder(name)) {
				ManageCsvFile.readFileCsv(name);
				return true;
			} else if (checknameFileAlreadyExistOnlyInData(name, uuid)) {
				conversionFromCsvToCsvDto(whatIsTheLuckyNameFile(name));
				createLocalFileFromCsvDto(conversionFromCsvToCsvDto(whatIsTheLuckyNameFile(name)));
				ManageCsvFile.readFileCsv(name);
				return true;
			} else
				return false;
		} catch (

		Exception e) {
		}
		return false;
	}

	// CARICAMENTO FILE AZIENDALE
	/**
	 * salva in folder temporaneo IL FILE AZIENDALE DI RIFERIMENTO
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void importFileFromLocalPc(String path, Session current) throws IOException {
		Path p = Paths.get(path);
		byte[] bytes = Files.readAllBytes(p);
		Path standardname = Paths.get(Constant.getFilePathCsv() + CsvStandard.DOCUMENTO_AZIENDALE + ".csv");

		// CREO IL FILE IN LOCALE
		Files.write(standardname, bytes);

		// DOPO AVERLO CREATO LO SALVIAMO IN DATA

		addNewFileInCsvTableFromCsvDto(convertFileCsvToCsvDto(CsvStandard.DOCUMENTO_AZIENDALE.toString(), current));

		// ORA LO ELIMINIAMO DAL LOCALE
		deleteOneFileInRepo(standardname);
	}

	/**
	 * 
	 * 
	 * 
	 * Elimina il CSV in posizione "index" nella lista dell'utente.
	 */
	public boolean deleteMyCsvByIndex(int index, Session current) {
		List<CsvDto> list = returnAllFileCsvDtoFromDataOfUser(current.getUuid());
		if (list == null || index < 0 || index >= list.size()) {
			return false;
		}

		CsvDto dto = list.get(index);
		deleteOneFileInData(dto);
		return true;
	}

	/**
	 * ELIMINA UN CSV ENTITY PARTENDO DA UN CSVDTO
	 * 
	 * @param c
	 */
	public void deleteOneFileInData(CsvDto c) {
		csvMapper.deleteCsvEntityFromData(csvService, c);
	}

}