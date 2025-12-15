package it.unibg.progetto.api.mapping;


import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.data.Csv;
import it.unibg.progetto.service.CsvService;

@Component
public class CsvMapper {

	/**
	 * converte un file csv dal data a file csv java modificabile
	 * 
	 * @param c
	 * @return
	 */
	public CsvDto csvConverterFromData(Csv c) {
		return new CsvDto(c.getId(), c.getFileName(), c.getOwnerId(), c.getData());
	}

	/**
	 * converte csvdto in csv da poter aggiungere alla tabella csv
	 * 
	 * @param csvdto
	 * @return
	 */
	public Csv toCsvFromCsvDtoNoId(CsvDto csvdto) {
		return new Csv(csvdto.getFileName(), csvdto.getOwnerId(), csvdto.getData());
	}

	/**
	 * converte un csvdto in una entita csv full dati, per manipolare la tabella (di
	 * solito quando bisogna eliminare o modificare i csv della tabella)
	 * 
	 * @param csvdto
	 * @return
	 */
	public Csv toCsvFromCsvDtoWithId(CsvDto csvdto) {
		return new Csv(csvdto.getId(), csvdto.getFileName(), csvdto.getOwnerId(), csvdto.getData());
	}

	/**
	 * converte la lista di csv tabella in una lista di csv dto
	 * 
	 * @param csvService
	 * @return
	 */
	public List<CsvDto> ListCsvFromDatabase(CsvService csvService) {
		List<Csv> csvListData = csvService.getAllFileCsv();
		List<CsvDto> csvList = new ArrayList<>();

		for (Csv c : csvListData) {
			CsvDto dto = csvConverterFromData(c);

			csvList.add(dto);
		}

		return csvList;
	}

	/**
	 * converte la lista csv in lista di csvdto tenendo conto di prendere file solo
	 * di uno specifico utente
	 * 
	 * @param csvService
	 * @param uuid
	 * @return
	 */
	public List<CsvDto> ListCsvFromDatabaseWithSpecificUUID(CsvService csvService, String uuid) {
		List<Csv> csvListData = csvService.getAllFileCsv();
		List<CsvDto> csvList = new ArrayList<>();

		for (Csv c : csvListData) {
			if (c.getOwnerId().equals(uuid)) {
				CsvDto dto = csvConverterFromData(c);

				csvList.add(dto);
			}
		}
		return csvList;
	}

	public List<Csv> ListCSv(CsvService csvService) {
		return csvService.getAllFileCsv();
	}

	/**
	 * salva un file csvdto nella tabella csv
	 * 
	 * @param csvService
	 * @param c
	 */
	public void addCSVToDataBase(CsvService csvService, Csv c) {
		csvService.addNewFileCSV(c);
	}

	/**
	 * cancella un file csv dalla tabella partendo da un file csvdto
	 * 
	 * @param csvService
	 * @param c
	 */
	public void deleteCsvEntityFromData(CsvService csvService, CsvDto c) {
		toCsvFromCsvDtoWithId(c);
		csvService.deleteFileCsv(null);

	}
}
