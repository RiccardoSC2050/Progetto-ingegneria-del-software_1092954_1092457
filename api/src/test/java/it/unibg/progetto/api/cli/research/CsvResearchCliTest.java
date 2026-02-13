package it.unibg.progetto.api.cli.research;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.domain.rules.CsvStandard;
import it.unibg.progetto.api.infrastructure.csv.CsvFileManager;

class CsvResearchCliTest {

	@Test
	void searchOnBaseFileByColumnEquals_whenBangNotAtEnd_returnsNull() {

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true))
					.thenReturn(java.util.Collections.singletonList(new String[] { "1", "mario" }));

			List<String[]> res = CsvResearchCli.searchOnBaseFileByColumnEquals(1, "ma!rio");

			assertTrue(res.isEmpty());

		}
	}

	@Test
	void searchOnBaseFileByColumnEquals_generalColumn_startsWith_caseInsensitive() {
		List<String[]> allRows = List.of(new String[] { "1", "Mario", "Rossi" },
				new String[] { "2", "Luca", "Bianchi" });

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true))
					.thenReturn(allRows);

			
			List<String[]> res = CsvResearchCli.searchOnBaseFileByColumnEquals(1, "ma");

			assertNotNull(res);
			assertEquals(1, res.size());
			assertEquals("Mario", res.get(0)[1]);
		}
	}

	@Test
	void searchOnBaseFileByColumnEquals_generalColumn_equalsWhenBangAtEnd() {

		List<String[]> allRows = java.util.List.of(new String[] { "1", "mario" }, new String[] { "2", "marino" });

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			fm.when(() -> CsvFileManager.readAllRows(org.mockito.Mockito.anyString(), org.mockito.Mockito.anyBoolean()))
					.thenReturn(allRows);

			List<String[]> res = CsvResearchCli.searchOnBaseFileByColumnEquals(1, "mario!");

			fm.verify(() -> CsvFileManager.readAllRows(org.mockito.Mockito.anyString(),
					org.mockito.Mockito.anyBoolean()));

			assertNotNull(res);

		}
	}

	@Test
	void searchOnBaseFileByColumnEquals_phoneColumn_startsWith_ignoresPlus39InCell() {
		
		String[] r1 = new String[] { "1", "a", "b", "c", "+39 333123", "dev", "2020", "0" };
		String[] r2 = new String[] { "2", "a", "b", "c", "444000", "dev", "2020", "0" };

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true))
					.thenReturn(List.of(r1, r2));

			List<String[]> res = CsvResearchCli.searchOnBaseFileByColumnEquals(4, "333");

			assertNotNull(res);
			assertEquals(1, res.size());
			assertEquals("1", res.get(0)[0]);
		}
	}

	@Test
	void searchByAnnoInizioMaggioreUguale_filtersAndSkipsInvalidYear() {
		
		String[] ok1 = new String[] { "1", "a", "b", "c", "d", "dev", "2020", "0" };
		String[] ok2 = new String[] { "2", "a", "b", "c", "d", "dev", "2010", "0" };
		String[] bad = new String[] { "3", "a", "b", "c", "d", "dev", "xx", "0" };

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true))
					.thenReturn(List.of(ok1, ok2, bad));

			List<String[]> res = CsvResearchCli.searchByAnnoInizioMaggioreUguale(2015);

			assertEquals(1, res.size());
			assertEquals("1", res.get(0)[0]);
		}
	}

	@Test
	void searchByMarker_filtersAndSkipsInvalidMarker() {
		
		String[] ok1 = new String[] { "1", "a", "b", "c", "d", "dev", "2020", "2" };
		String[] ok2 = new String[] { "2", "a", "b", "c", "d", "dev", "2020", "1" };
		String[] bad = new String[] { "3", "a", "b", "c", "d", "dev", "2020", "xx" };

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true))
					.thenReturn(List.of(ok1, ok2, bad));

			List<String[]> res = CsvResearchCli.searchByMarker(2);

			assertEquals(1, res.size());
			assertEquals("1", res.get(0)[0]);
		}
	}

	@Test
	void saveSearchResult_writesHeaderAndRows() throws Exception {
		File tmp = File.createTempFile("search-result", ".csv");
		tmp.deleteOnExit();

		List<String[]> rows = List.of(new String[] { "a", "b" }, new String[] { "1", "2" });

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.getTempCsvFile("out")).thenReturn(tmp);

			fm.when(() -> CsvFileManager.readHeader(CsvStandard.DOCUMENTO_AZIENDALE.toString())).thenReturn("h1,h2");

			CsvResearchCli.saveSearchResult("out", rows, true);

			List<String> lines = Files.readAllLines(tmp.toPath());

			assertEquals(3, lines.size());
			assertEquals("h1,h2", lines.get(0));
			assertEquals("a,b", lines.get(1));
			assertEquals("1,2", lines.get(2));
		}
	}
}
