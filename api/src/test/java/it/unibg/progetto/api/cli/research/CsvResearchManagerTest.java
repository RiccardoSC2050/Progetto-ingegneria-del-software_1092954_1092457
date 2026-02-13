package it.unibg.progetto.api.cli.research;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;

import java.util.Map;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.api.application.usecase.CsvUseCase;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.domain.rules.CsvStandard;
import it.unibg.progetto.api.domain.rules.StringValues;
import it.unibg.progetto.api.infrastructure.csv.CsvFileManager;
import it.unibg.progetto.api.security.session.Session;
import it.unibg.progetto.api.security.session.SessionManager;

class CsvResearchManagerTest {

	@AfterEach
	void resetScanner() {
		GlobalScanner.scanner = new Scanner(System.in);
	}

	private static void invokeAskAndSaveResult(Session current, List<String[]> result) throws Exception {
		Method m = CsvResearchManager.class.getDeclaredMethod("askAndSaveResult", Session.class, List.class);
		m.setAccessible(true);
		m.invoke(null, current, result); 
	}

	@Test
	void askAndSaveResult_whenAnswerIsNo_doesNothing() throws Exception {
		// input: "n"
		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("n\n".getBytes(StandardCharsets.UTF_8)));

		Session session = mock(Session.class);
		List<String[]> result = java.util.Collections.singletonList(new String[] { "1", "mario" });

		try (MockedStatic<CsvResearchCli> cli = mockStatic(CsvResearchCli.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class)) {

			invokeAskAndSaveResult(session, result);

			
			cli.verifyNoInteractions();
			uc.verifyNoInteractions();
		}
	}

	@Test
	void askAndSaveResult_whenAnswerIsYes_savesStandard_thenChangesName_thenAddsToDb() throws Exception {
		
		GlobalScanner.scanner = new Scanner(
				new ByteArrayInputStream("s\nrisultato\n".getBytes(StandardCharsets.UTF_8)));

		Session session = mock(Session.class);
		List<String[]> result = java.util.Collections.singletonList(new String[] { "1", "mario" });

		CsvUseCase csvUseCase = mock(CsvUseCase.class);
		CsvDto dto = mock(CsvDto.class);

		when(csvUseCase.convertFileCsvToCsvDto("risultato", session)).thenReturn(dto);

		try (MockedStatic<CsvResearchCli> cli = mockStatic(CsvResearchCli.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class)) {

			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			invokeAskAndSaveResult(session, result);

			
			cli.verify(
					() -> CsvResearchCli.saveSearchResult(eq(CsvStandard.STANDARD.toString()), eq(result), eq(true)));

			
			verify(csvUseCase).changeNameFile("risultato");
			verify(csvUseCase).convertFileCsvToCsvDto("risultato", session);
			verify(csvUseCase).addNewFileInCsvTableFromCsvDto(dto);
		}
	}

	@Test
	void searchAndMaybeSaveByMarker_whenAccessBelowAL2_doesNotAskToSave() throws Exception {
		
		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("9\n2\n".getBytes(StandardCharsets.UTF_8)));

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel()); // < AL2

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		List<String[]> result = java.util.Collections.singletonList(new String[] { "1", "mario" });

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvResearchCli> cli = mockStatic(CsvResearchCli.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			cli.when(() -> CsvResearchCli.searchByMarker(2)).thenReturn(result);

			CsvResearchManager.searchAndMaybeSaveByMarker();

			
			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

			
			cli.verify(() -> CsvResearchCli.searchByMarker(2));

			
			fm.verify(() -> CsvFileManager.printRows(result));

			
			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

	@Test
	void statsAndMaybeSavePercentByRole_whenAccessBelowAL2_doesNotSave() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		Map<String, Double> report = Map.of("DEV", 50.0, "QA", 50.0);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::percentEmployeesByRole).thenReturn(report);

			CsvResearchManager.statsAndMaybeSavePercentByRole();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

			stats.verify(() -> CsvStatisticsResearch.percentEmployeesByRole());

			fm.verify(() -> CsvFileManager.printRows(anyList()));

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}

	}

	@Test
	void searchAndMaybeSave_whenResultEmpty_returnsBeforePrintAndSave() throws Exception {
		
		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("DEV\n".getBytes(StandardCharsets.UTF_8)));

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL3.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);
		List<String[]> empty = java.util.Collections.emptyList();

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvResearchCli> cli = mockStatic(CsvResearchCli.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			
			cli.when(() -> CsvResearchCli.searchByStringValue(eq(StringValues.RUOLO), eq("DEV"))).thenReturn(empty);

			CsvResearchManager.searchAndMaybeSave(StringValues.RUOLO);

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

			
			cli.verify(() -> CsvResearchCli.searchByStringValue(StringValues.RUOLO, "DEV"));

			
			fm.verifyNoInteractions();

			
			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

	@Test
	void searchAndMaybeSave_whenAccessBelowAL2_printsButDoesNotSave() throws Exception {
		
		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("QA\n".getBytes(StandardCharsets.UTF_8)));

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);
		List<String[]> result = java.util.Collections.singletonList(new String[] { "1", "Mario", "Rossi" });

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvResearchCli> cli = mockStatic(CsvResearchCli.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			cli.when(() -> CsvResearchCli.searchByStringValue(eq(StringValues.RUOLO), eq("QA"))).thenReturn(result);

			CsvResearchManager.searchAndMaybeSave(StringValues.RUOLO);

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			cli.verify(() -> CsvResearchCli.searchByStringValue(StringValues.RUOLO, "QA"));

			
			fm.verify(() -> CsvFileManager.printRows(result));

			
			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}



	@Test
	void searchAndMaybeSaveByAnnoInizio_whenAccessBelowAL2_printsButDoesNotSave() throws Exception {
		
		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("2018\n".getBytes(StandardCharsets.UTF_8)));

		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel()); 

		CsvUseCase csvUseCase = mock(CsvUseCase.class);
		List<String[]> result = java.util.Collections.singletonList(new String[] { "7", "Luca", "Bianchi" });

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvResearchCli> cli = mockStatic(CsvResearchCli.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			cli.when(() -> CsvResearchCli.searchByAnnoInizioMaggioreUguale(2018)).thenReturn(result);

			CsvResearchManager.searchAndMaybeSaveByAnnoInizio();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			cli.verify(() -> CsvResearchCli.searchByAnnoInizioMaggioreUguale(2018));
			fm.verify(() -> CsvFileManager.printRows(result));

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}



	@Test
	void statsAndMaybeSaveCountByRole_whenEmptyMap_returnsAndDoesNotPrint() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL3.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
			stats.when(CsvStatisticsResearch::countEmployeesByRole).thenReturn(Map.of());

			CsvResearchManager.statsAndMaybeSaveCountByRole();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			stats.verify(CsvStatisticsResearch::countEmployeesByRole);

			
			fm.verifyNoInteractions();

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

	@Test
	void statsAndMaybeSaveCountByRole_whenAccessBelowAL2_printsButDoesNotSave() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		Map<String, Integer> counts = Map.of("DEV", 2, "QA", 1);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
			stats.when(CsvStatisticsResearch::countEmployeesByRole).thenReturn(counts);

			CsvResearchManager.statsAndMaybeSaveCountByRole();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			stats.verify(CsvStatisticsResearch::countEmployeesByRole);

			// stampa report
			fm.verify(() -> CsvFileManager.printRows(anyList()));

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}



	@Test
	void statsAndMaybeSaveRichiamiSummary_whenNullStats_returnsAndDoesNotPrint() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL3.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::statsRichiami).thenReturn(null);

			CsvResearchManager.statsAndMaybeSaveRichiamiSummary();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			stats.verify(CsvStatisticsResearch::statsRichiami);

			fm.verifyNoInteractions();

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

	@Test
	void statsAndMaybeSaveRichiamiSummary_whenAccessBelowAL2_printsButDoesNotSave() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel()); // < AL2

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		
		double[] s = new double[] { 10.0, 2.5, 0.0, 5.0 };

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::statsRichiami).thenReturn(s);

			CsvResearchManager.statsAndMaybeSaveRichiamiSummary();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			stats.verify(CsvStatisticsResearch::statsRichiami);

			
			fm.verify(() -> CsvFileManager.printRows(anyList()));

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}



	@Test
	void statsAndMaybeSaveTop5Richiami_whenEmptyList_returnsAndDoesNotPrint() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL3.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(() -> CsvStatisticsResearch.topEmployeesByRichiami(5))
					.thenReturn(java.util.Collections.emptyList());

			CsvResearchManager.statsAndMaybeSaveTop5Richiami();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			stats.verify(() -> CsvStatisticsResearch.topEmployeesByRichiami(5));

			fm.verifyNoInteractions();

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

	@Test
	void statsAndMaybeSaveTop5Richiami_whenAccessBelowAL2_printsButDoesNotSave() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel()); 

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		List<String[]> top = List.of(new String[] { "1", "Mario", "Rossi", "DEV", "3" },
				new String[] { "2", "Anna", "Verdi", "QA", "2" });

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(() -> CsvStatisticsResearch.topEmployeesByRichiami(5)).thenReturn(top);

			CsvResearchManager.statsAndMaybeSaveTop5Richiami();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			stats.verify(() -> CsvStatisticsResearch.topEmployeesByRichiami(5));

			fm.verify(() -> CsvFileManager.printRows(anyList()));

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}



	@Test
	void statsAndMaybeSaveZeroRichiami_whenEmpty_returnsAndDoesNotPrint() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL3.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::employeesWithZeroRichiami).thenReturn(java.util.Collections.emptyList());

			CsvResearchManager.statsAndMaybeSaveZeroRichiami();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			stats.verify(CsvStatisticsResearch::employeesWithZeroRichiami);

			fm.verifyNoInteractions();

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

	@Test
	void statsAndMaybeSaveZeroRichiami_whenAccessBelowAL2_printsButDoesNotSave() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		List<String[]> rows = java.util.Collections.singletonList(new String[] { "10", "Paolo", "Neri", "DEV", "0" });

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::employeesWithZeroRichiami).thenReturn(rows);

			CsvResearchManager.statsAndMaybeSaveZeroRichiami();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			stats.verify(CsvStatisticsResearch::employeesWithZeroRichiami);

			fm.verify(() -> CsvFileManager.printRows(anyList()));

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}



	@Test
	void statsAndMaybeSaveMissingFields_whenEmptyMap_returnsAndDoesNotPrint() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL3.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::countMissingFields).thenReturn(Map.of());

			CsvResearchManager.statsAndMaybeSaveMissingFields();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			stats.verify(CsvStatisticsResearch::countMissingFields);

			fm.verifyNoInteractions();

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

	@Test
	void statsAndMaybeSaveMissingFields_whenAccessBelowAL2_printsButDoesNotSave() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		Map<String, Integer> miss = Map.of("EMAIL", 2, "TELEFONO", 1);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::countMissingFields).thenReturn(miss);

			CsvResearchManager.statsAndMaybeSaveMissingFields();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
			stats.verify(CsvStatisticsResearch::countMissingFields);

			fm.verify(() -> CsvFileManager.printRows(anyList()));

			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}


	@Test
	void statsAndMaybeSaveMinMaxAnnoInizio_whenNullStats_returnsAndDoesNotPrintOrSave() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL3.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::minMaxAnnoInizio).thenReturn(null);

			CsvResearchManager.statsAndMaybeSaveMinMaxAnnoInizio();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

			// null => non stampa e non salva
			fm.verifyNoInteractions();
			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

	@Test
	void statsAndMaybeSaveMinMaxAnnoInizio_whenAccessBelowAL2_printsButDoesNotSave() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel()); 

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		int[] mm = new int[] { 2010, 2020 };

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::minMaxAnnoInizio).thenReturn(mm);

			CsvResearchManager.statsAndMaybeSaveMinMaxAnnoInizio();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

			
			fm.verify(() -> CsvFileManager.printRows(anyList()));

			
			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

	@Test
	void statsAndMaybeSaveAverageAnnoInizio_whenNullStats_returnsAndDoesNotPrintOrSave() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL3.getLevel());

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::averageAnnoInizioAndSeniority).thenReturn(null);

			CsvResearchManager.statsAndMaybeSaveAverageAnnoInizio();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

			
			fm.verifyNoInteractions();
			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

	@Test
	void statsAndMaybeSaveAverageAnnoInizio_whenAccessBelowAL2_printsButDoesNotSave() throws Exception {
		Session session = mock(Session.class);
		when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel()); 

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		double[] res = new double[] { 2015.50, 7.25 };

		try (MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

			sm.when(SessionManager::getCurrent).thenReturn(session);
			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

			stats.when(CsvStatisticsResearch::averageAnnoInizioAndSeniority).thenReturn(res);

			CsvResearchManager.statsAndMaybeSaveAverageAnnoInizio();

			verify(csvUseCase).saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

			
			fm.verify(() -> CsvFileManager.printRows(anyList()));

			
			verify(csvUseCase, never()).changeNameFile(anyString());
			verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(any());
		}
	}

}
