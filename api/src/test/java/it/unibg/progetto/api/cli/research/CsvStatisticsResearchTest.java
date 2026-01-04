package it.unibg.progetto.api.cli.research;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.application.usecase.CsvUseCase;
import it.unibg.progetto.api.domain.rules.CsvStandard;
import it.unibg.progetto.api.domain.rules.StringValues;
import it.unibg.progetto.api.infrastructure.csv.CsvFileManager;

class CsvStatisticsResearchTest {

	// ---- helper: crea una row abbastanza lunga e imposta le celle per indice enum
	private static String[] row(Object... pairs) {
		int max = 0;
		for (StringValues v : StringValues.values()) {
			max = Math.max(max, v.getIndex());
		}
		String[] r = new String[max + 1];
		// default null va bene, ma per evitare NPE in alcuni accessi metto "" ovunque
		for (int i = 0; i < r.length; i++) r[i] = "";

		for (int i = 0; i < pairs.length; i += 2) {
			StringValues key = (StringValues) pairs[i];
			String val = (String) pairs[i + 1];
			r[key.getIndex()] = val;
		}
		return r;
	}

	// =========================
	// 1) countTotalValidEmployees
	// =========================
	@Test
	void countTotalValidEmployees_countsOnlyValidRows() throws Exception {
		List<String[]> rows = Arrays.asList(
				row(StringValues.ID, "1", StringValues.NOME, "Mario", StringValues.COGNOME, "Rossi"),
				row(StringValues.ID, "",  StringValues.NOME, "Luigi", StringValues.COGNOME, "Bianchi"), // invalid (id blank)
				row(StringValues.ID, "3", StringValues.NOME, "Anna",  StringValues.COGNOME, "Verdi")
		);

		CsvUseCase ucInstance = mock(CsvUseCase.class);

		try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
			 MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			uc.when(CsvUseCase::getIstnce).thenReturn(ucInstance);
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true)).thenReturn(rows);

			int n = CsvStatisticsResearch.countTotalValidEmployees();

			assertEquals(2, n);
			verify(ucInstance).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
		}
	}

	// =========================
	// 2) countEmployeesByRole
	// =========================
	@Test
	void countEmployeesByRole_countsRolesSkippingInvalidRows() throws Exception {
		List<String[]> rows = Arrays.asList(
				row(StringValues.ID, "1", StringValues.NOME, "Mario", StringValues.COGNOME, "Rossi", StringValues.RUOLO, "DEV"),
				row(StringValues.ID, "2", StringValues.NOME, "Anna",  StringValues.COGNOME, "Verdi", StringValues.RUOLO, "QA"),
				row(StringValues.ID, "",  StringValues.NOME, "X",     StringValues.COGNOME, "Y",     StringValues.RUOLO, "DEV"), // invalid
				row(StringValues.ID, "3", StringValues.NOME, "Luca",  StringValues.COGNOME, "Neri",  StringValues.RUOLO, "DEV")
		);

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true)).thenReturn(rows);

			Map<String, Integer> m = CsvStatisticsResearch.countEmployeesByRole();

			assertEquals(Integer.valueOf(2), m.get("DEV"));
			assertEquals(Integer.valueOf(1), m.get("QA"));
		}
	}

	// =========================
	// 3) percentEmployeesByRole
	// =========================
	@Test
	void percentEmployeesByRole_computesPercentages() throws Exception {
		// 2 DEV, 1 QA => DEV 66.666.. QA 33.333..
		List<String[]> rows = Arrays.asList(
				row(StringValues.ID, "1", StringValues.NOME, "A", StringValues.COGNOME, "A", StringValues.RUOLO, "DEV"),
				row(StringValues.ID, "2", StringValues.NOME, "B", StringValues.COGNOME, "B", StringValues.RUOLO, "DEV"),
				row(StringValues.ID, "3", StringValues.NOME, "C", StringValues.COGNOME, "C", StringValues.RUOLO, "QA")
		);

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true)).thenReturn(rows);

			Map<String, Double> p = CsvStatisticsResearch.percentEmployeesByRole();

			assertTrue(p.containsKey("DEV"));
			assertTrue(p.containsKey("QA"));
			assertEquals(66.666, p.get("DEV"), 0.5);
			assertEquals(33.333, p.get("QA"), 0.5);
		}
	}

	// =========================
	// 4) minMaxAnnoInizio
	// =========================
	@Test
	void minMaxAnnoInizio_returnsMinAndMax() {
		List<String[]> rows = Arrays.asList(
				row(StringValues.ANNO_INIZIO, "2018"),
				row(StringValues.ANNO_INIZIO, "2010"),
				row(StringValues.ANNO_INIZIO, "2020")
		);

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true)).thenReturn(rows);

			int[] mm = CsvStatisticsResearch.minMaxAnnoInizio();

			assertNotNull(mm);
			assertEquals(2010, mm[0]);
			assertEquals(2020, mm[1]);
		}
	}

	// =========================
	// 5) averageAnnoInizioAndSeniority
	// =========================
	@Test
	void averageAnnoInizioAndSeniority_computesAverages() {
		int current = Year.now().getValue();

		List<String[]> rows = Arrays.asList(
				row(StringValues.ANNO_INIZIO, "2010"),
				row(StringValues.ANNO_INIZIO, "2020")
		);

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true)).thenReturn(rows);

			double[] res = CsvStatisticsResearch.averageAnnoInizioAndSeniority();

			assertNotNull(res);
			assertEquals((2010 + 2020) / 2.0, res[0], 0.0001);

			double s1 = current - 2010;
			double s2 = current - 2020;
			assertEquals((s1 + s2) / 2.0, res[1], 0.0001);
		}
	}

	// =========================
	// 6) countEmployeesByStartYear
	// =========================
	@Test
	void countEmployeesByStartYear_countsByYearSorted() {
		List<String[]> rows = Arrays.asList(
				row(StringValues.ANNO_INIZIO, "2020"),
				row(StringValues.ANNO_INIZIO, "2020"),
				row(StringValues.ANNO_INIZIO, "2019")
		);

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true)).thenReturn(rows);

			Map<Integer, Integer> m = CsvStatisticsResearch.countEmployeesByStartYear();

			assertEquals(Integer.valueOf(1), m.get(2019));
			assertEquals(Integer.valueOf(2), m.get(2020));
		}
	}

	// =========================
	// 7) statsRichiami
	// =========================
	@Test
	void statsRichiami_returnsTotalAvgMinMax() {
		List<String[]> rows = Arrays.asList(
				row(StringValues.RICHIAMI, "0"),
				row(StringValues.RICHIAMI, "2"),
				row(StringValues.RICHIAMI, "4")
		);

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true)).thenReturn(rows);

			double[] s = CsvStatisticsResearch.statsRichiami();

			assertNotNull(s);
			assertEquals(6.0, s[0], 0.0001);     // totale
			assertEquals(2.0, s[1], 0.0001);     // media
			assertEquals(0.0, s[2], 0.0001);     // min
			assertEquals(4.0, s[3], 0.0001);     // max
		}
	}

	// =========================
	// 8) topEmployeesByRichiami
	// =========================
	@Test
	void topEmployeesByRichiami_returnsTopNOrderedDesc() {
		List<String[]> rows = Arrays.asList(
				row(StringValues.ID, "1", StringValues.NOME, "Mario", StringValues.COGNOME, "Rossi", StringValues.RUOLO, "DEV", StringValues.RICHIAMI, "3"),
				row(StringValues.ID, "2", StringValues.NOME, "Anna",  StringValues.COGNOME, "Verdi", StringValues.RUOLO, "QA",  StringValues.RICHIAMI, "4"),
				row(StringValues.ID, "3", StringValues.NOME, "Luca",  StringValues.COGNOME, "Neri",  StringValues.RUOLO, "DEV", StringValues.RICHIAMI, "1")
		);

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true)).thenReturn(rows);

			List<String[]> top2 = CsvStatisticsResearch.topEmployeesByRichiami(2);

			assertEquals(2, top2.size());
			assertEquals("2", top2.get(0)[0]); // ID con 4 richiami
			assertEquals("4", top2.get(0)[4]);
			assertEquals("1", top2.get(1)[0]); // ID con 3 richiami
			assertEquals("3", top2.get(1)[4]);
		}
	}

	// =========================
	// 9) employeesWithZeroRichiami
	// =========================
	@Test
	void employeesWithZeroRichiami_returnsOnlyZero() {
		List<String[]> rows = Arrays.asList(
				row(StringValues.ID, "1", StringValues.NOME, "A", StringValues.COGNOME, "A", StringValues.RUOLO, "DEV", StringValues.RICHIAMI, "0"),
				row(StringValues.ID, "2", StringValues.NOME, "B", StringValues.COGNOME, "B", StringValues.RUOLO, "QA",  StringValues.RICHIAMI, "2"),
				row(StringValues.ID, "3", StringValues.NOME, "C", StringValues.COGNOME, "C", StringValues.RUOLO, "DEV", StringValues.RICHIAMI, "0")
		);

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true)).thenReturn(rows);

			List<String[]> z = CsvStatisticsResearch.employeesWithZeroRichiami();

			assertEquals(2, z.size());
			assertEquals("0", z.get(0)[4]);
			assertEquals("0", z.get(1)[4]);
		}
	}

	// =========================
	// 10) countMissingFields
	// =========================
	@Test
	void countMissingFields_countsMissingByColumn() {
		// r1 completo, r2 manca EMAIL e TELEFONO, r3 manca NOME
		String[] r1 = row(
				StringValues.ID, "1",
				StringValues.NOME, "Mario",
				StringValues.COGNOME, "Rossi",
				StringValues.MAIL, "m@x.it",
				StringValues.NUMERO_TELEFONO, "123",
				StringValues.RUOLO, "DEV",
				StringValues.ANNO_INIZIO, "2020"
		);

		String[] r2 = row(
				StringValues.ID, "2",
				StringValues.NOME, "Anna",
				StringValues.COGNOME, "Verdi",
				StringValues.MAIL, "",                 // missing
				StringValues.NUMERO_TELEFONO, "   ",    // missing
				StringValues.RUOLO, "QA",
				StringValues.ANNO_INIZIO, "2019"
		);

		String[] r3 = row(
				StringValues.ID, "3",
				StringValues.NOME, "",                  // missing
				StringValues.COGNOME, "Neri",
				StringValues.MAIL, "c@x.it",
				StringValues.NUMERO_TELEFONO, "999",
				StringValues.RUOLO, "DEV",
				StringValues.ANNO_INIZIO, "2018"
		);

		List<String[]> rows = Arrays.asList(r1, r2, r3);

		try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
			fm.when(() -> CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true)).thenReturn(rows);

			Map<String, Integer> miss = CsvStatisticsResearch.countMissingFields();

			assertEquals(Integer.valueOf(1), miss.get("NOME"));
			assertEquals(Integer.valueOf(0), miss.get("COGNOME"));
			assertEquals(Integer.valueOf(1), miss.get("EMAIL"));
			assertEquals(Integer.valueOf(1), miss.get("TELEFONO"));
			assertEquals(Integer.valueOf(0), miss.get("RUOLO"));
			assertEquals(Integer.valueOf(0), miss.get("ANNO_INIZIO"));
		}
	}
}
