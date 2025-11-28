package it.unibg.progetto.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

import it.unibg.progetto.data.Csv;
import it.unibg.progetto.data.CsvRepository;

@Service
public class CsvService {

	private final CsvRepository repository;

	public CsvService(CsvRepository repository) {
		super();
		this.repository = repository;
	}

	/**
	 * 
	 * @param fileName
	 * @param idOwner
	 * @return
	 */
	public Csv addFileCSV(String fileName, String idOwner) {

		Path filePath = Paths.get("../api/temporary_fileCSV_saving/", fileName + ".csv");

		if (!Files.exists(filePath)) {
			throw new RuntimeException("Il file CSV non esiste: " + filePath.toAbsolutePath());
		}

		try {
			byte[] bytes = Files.readAllBytes(filePath);

			Csv csv = new Csv(fileName, idOwner);
			csv.setData(bytes);

			return repository.save(csv);

		} catch (IOException e) {
			throw new RuntimeException("Errore nella lettura del CSV: " + filePath, e);
		}
	}

	/**
	 * 
	 * @param fileName
	 * @param bytes
	 * @throws IOException
	 */
	public void covertBytesInCSV(String fileName, byte[] bytes) throws IOException {

		Path filePath = Paths.get("../api/temporary_fileCSV_saving/", fileName + ".csv");
		Files.write(filePath, bytes);
	}

	/**
	 * 
	 * @return
	 */
	public List<Csv> getAllFileCsv() {
		return repository.findAll();
	}

	/**
	 * 
	 * @param csv
	 */
	public void deleteFileCsv(Csv csv) {
		repository.delete(csv);
	}

}
