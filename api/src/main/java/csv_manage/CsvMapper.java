package csv_manage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.unibg.progetto.data.Csv;
import it.unibg.progetto.service.CsvService;

@Component
public class CsvMapper {

	public CsvDto csvConverterFromData(Csv c) {
		return new CsvDto(c.getFileName(), c.getOwnerId(), c.getData());
	}

	public List<CsvDto> ListCsvFromDatabase(CsvService csvService) {
		List<Csv> csvListData = csvService.getAllFileCsv();
		List<CsvDto> csvList = new ArrayList<>();

		for (Csv c : csvListData) {
			CsvDto dto = new CsvDto(c.getFileName(), c.getOwnerId(), c.getData());

			csvList.add(dto);
		}

		return csvList;
	}

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

	public void addCSVToDataBase(CsvService csvService, String fileName, String ownerId) {
		csvService.addFileCSV(fileName, ownerId);
	}

	// csv->csvdto->file.csv
	/**
	 * converte il CSVDTO in ujn file locale reale
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createLocalFileFromCsvDto(CsvDto c) throws Exception {
		if (c == null) {
			System.out.println("ERRORE, il file/ oggetto non esiste");
		}
		String nameFile = c.getFileName();
		Path localFile = Paths.get("/api/temporary_fileCSV_saving/" + nameFile + ".csv");

		Files.write(localFile, c.getData());

	}

}
