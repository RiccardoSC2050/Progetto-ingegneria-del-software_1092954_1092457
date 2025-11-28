package csv_manage;

import java.util.ArrayList;
import java.util.List;

import it.unibg.progetto.data.Csv;
import it.unibg.progetto.service.CsvService;

public class CsvMapper {

	public CsvDto csvConverterFromData(Csv c) {
		return new CsvDto(c.getId(), c.getFileName(), c.getOwnerId(), c.getData());
	}

	public List<CsvDto> ListCsvFromDatabase(CsvService csvService) {
		List<Csv> csvListData = csvService.getAllFileCsv();
		List<CsvDto> csvList = new ArrayList<>();

		for (Csv c : csvListData) {
			CsvDto dto = new CsvDto(c.getId(), c.getFileName(), c.getOwnerId(), c.getData());

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

}
