package it.unibg.progetto.api.application;

import java.util.List;

import it.unibg.progetto.api.access_session.ManagerSession;
import it.unibg.progetto.api.access_session.Session;
import it.unibg.progetto.api.dto.CsvDto;
import it.unibg.progetto.api.mapper.CsvMapper;
import it.unibg.progetto.service.CsvService;

public class AppBlocksCsv {

	private final CsvService csvService;
	private final CsvMapper csvMapper;

	public AppBlocksCsv(CsvService csvService, CsvMapper csvMapper) {
		this.csvService = csvService;
		this.csvMapper = csvMapper;
	}

	/** Stampa tutti i CSV dell’utente loggato. */
	public void showMyCsvFiles() {
		Session current = ManagerSession.getCurrent();
		String uuid = current.getUuid(); // o getUuid(), dipende dal tuo Session

		List<CsvDto> myFiles = csvMapper.ListCsvFromDatabaseWithSpecificUUID(csvService, uuid);

		if (myFiles.isEmpty()) {
			System.out.println("Non hai ancora nessun file CSV salvato.");
			return;
		}

		System.out.println("=== I TUOI FILE CSV ===");
		for (CsvDto c : myFiles) {
			System.out.println("- " + c.getFileName() + " (ownerId=" + c.getOwnerId() + ")");
		}
	}
}
