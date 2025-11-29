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
	 * AGGIUNGE UN FILE CSV NUOVO PARTENDO DA NOME FILE IN LOCALE E DA UUID UTENTE
	 * 
	 * @param fileName
	 * @param idOwner
	 * @return
	 */
	public Csv addNewFileCSV(Csv c) {

		return repository.save(c);

	}

	/**
	 * aggiorna un csv preciso che ha un preciso id che deve esssere contriollato in
	 * un metodo che richiama il seguente
	 * 
	 * @param c
	 * @param bytes
	 */
	public void updateCsvEntitybytes(Csv c, byte[] bytes) {

		c.setData(bytes);

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
