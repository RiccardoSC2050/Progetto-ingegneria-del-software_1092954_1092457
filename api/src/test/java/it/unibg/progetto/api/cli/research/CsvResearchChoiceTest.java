package it.unibg.progetto.api.cli.research;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.application.usecase.CsvUseCase;
import it.unibg.progetto.api.cli.components.Constant;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.cli.components.Quit;

import it.unibg.progetto.api.domain.rules.CsvStandard;
import it.unibg.progetto.api.domain.rules.StringValues;

class CsvResearchChoiceTest {

	@AfterEach
	void resetScanner() {
		GlobalScanner.scanner = new Scanner(System.in);
	}

	@Test
	void askAndExecuteSearch_choice1_thenQuit_callsSearchByRuolo_andDeletesRepoEachIteration() throws Exception {
		// input: "1" (ruolo) poi "q" per uscire
		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("1\nq\n".getBytes(StandardCharsets.UTF_8)));

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		String basePath = "C:\\tmp\\csv\\";
		Path expected = Paths.get(basePath + CsvStandard.DOCUMENTO_AZIENDALE.toString() + ".csv");

		try (MockedStatic<CsvResearchManager> rm = mockStatic(CsvResearchManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<Constant> cst = mockStatic(Constant.class);
				MockedStatic<Quit> qt = mockStatic(Quit.class)) {

			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
			cst.when(Constant::getFilePathCsv).thenReturn(basePath);

		
			qt.when(() -> Quit.quit("q")).thenReturn(true);

			CsvResearchChoice.askAndExecuteSearch();

			
			rm.verify(() -> CsvResearchManager.searchAndMaybeSave(StringValues.RUOLO));

			
			verify(csvUseCase, times(2)).deleteOneFileInRepo(expected);
		}
	}

	@Test
	void askAndExecuteSearch_invalidChoice_thenQuit_doesNotCallResearchManager_butStillDeletesRepo() throws Exception {
		
		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("x\nq\n".getBytes(StandardCharsets.UTF_8)));

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		String basePath = "/tmp/csv/";
		Path expected = Paths.get(basePath + CsvStandard.DOCUMENTO_AZIENDALE.toString() + ".csv");

		try (MockedStatic<CsvResearchManager> rm = mockStatic(CsvResearchManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<Constant> cst = mockStatic(Constant.class);
				MockedStatic<Quit> qt = mockStatic(Quit.class)) {

			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
			cst.when(Constant::getFilePathCsv).thenReturn(basePath);
			qt.when(() -> Quit.quit("q")).thenReturn(true);

			CsvResearchChoice.askAndExecuteSearch();

			
			rm.verifyNoInteractions();

			verify(csvUseCase, times(2)).deleteOneFileInRepo(expected);
		}
	}

	@Test
	void askAndExecuteStatisticSearch_choice1_thenQuit_callsCountTotalValidEmployees_andDeletesRepoEachIteration()
			throws Exception {
		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("1\nq\n".getBytes(StandardCharsets.UTF_8)));

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		String basePath = "C:\\tmp\\csv\\";
		Path expected = Paths.get(basePath + CsvStandard.DOCUMENTO_AZIENDALE.toString() + ".csv");

		try (MockedStatic<CsvStatisticsResearch> stats = mockStatic(CsvStatisticsResearch.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<Constant> cst = mockStatic(Constant.class);
				MockedStatic<Quit> qt = mockStatic(Quit.class)) {

			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
			cst.when(Constant::getFilePathCsv).thenReturn(basePath);
			qt.when(() -> Quit.quit("q")).thenReturn(true);

			stats.when(CsvStatisticsResearch::countTotalValidEmployees).thenReturn(5);

			CsvResearchChoice.askAndExecuteStatisticSearch();

			stats.verify(CsvStatisticsResearch::countTotalValidEmployees);

			verify(csvUseCase, times(2)).deleteOneFileInRepo(expected);
		}
	}

	@Test
	void askAndExecuteStatisticSearch_choice10_thenQuit_callsMissingFieldsStats() throws Exception {
		
		GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("10\nq\n".getBytes(StandardCharsets.UTF_8)));

		CsvUseCase csvUseCase = mock(CsvUseCase.class);

		String basePath = "/tmp/csv/";
		Path expected = Paths.get(basePath + CsvStandard.DOCUMENTO_AZIENDALE.toString() + ".csv");

		try (MockedStatic<CsvResearchManager> rm = mockStatic(CsvResearchManager.class);
				MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
				MockedStatic<Constant> cst = mockStatic(Constant.class);
				MockedStatic<Quit> qt = mockStatic(Quit.class)) {

			uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
			cst.when(Constant::getFilePathCsv).thenReturn(basePath);
			qt.when(() -> Quit.quit("q")).thenReturn(true);

			CsvResearchChoice.askAndExecuteStatisticSearch();

			rm.verify(CsvResearchManager::statsAndMaybeSaveMissingFields);
			verify(csvUseCase, times(2)).deleteOneFileInRepo(expected);
		}
	}
}
