package it.unibg.progetto.api.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.api.application.usecase.CsvUseCase;
import it.unibg.progetto.api.cli.components.Constant;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.cli.components.Quit;
import it.unibg.progetto.api.cli.research.CsvResearchChoice;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.infrastructure.csv.CsvFileManager;
import it.unibg.progetto.api.security.session.Session;
import it.unibg.progetto.api.security.session.SessionManager;

class AppBlocksManageCsvIntegrationTest {

    @AfterEach
    void resetScanner() {
        // evita che altri test ereditino lo scanner
        GlobalScanner.scanner = new Scanner(System.in);
    }

    @Test
    void controllOnFolderCsv_callsCsvFileManagerGetTempFolder() {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
            sut.controllOnFolderCsv();
            fm.verify(CsvFileManager::getTempFolder);
        }
    }

    @Test
    void clearFolderCsv_callsGetTempFolder_and_deleteAllFileInRepo() {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);

        try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class);
             MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

            sut.clearFolderCsv();

            fm.verify(CsvFileManager::getTempFolder);
            verify(csvUseCase).deleteAllFileInRepo();
        }
    }

    @Test
    void editFileCsvFile_deniesWhenAccessLevelIsAL1() throws Exception {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel());

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);

            sut.editFileCsvFile();

            // sempre chiamato a inizio metodo
            verify(csvUseCase).saveAllFileCsvFromDataOfUser(session);

            // deve uscire subito senza tentare altro
            verifyNoMoreInteractions(csvUseCase);
        }
    }

    @Test
    void editFileCsvFile_returns_whenLsFileUserIsFalse() throws Exception {
        AppBlocksManageCsv sut = spy(new AppBlocksManageCsv());

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getAccessLevel()).thenReturn(AccessLevel.AL2.getLevel());

        doReturn(false).when(sut).lsFileUser();

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);

            sut.editFileCsvFile();

            verify(csvUseCase).saveAllFileCsvFromDataOfUser(session);
            verify(sut).lsFileUser();
        }
    }

    @Test
    void editFileCsvFile_returns_whenFileDoesNotExist() throws Exception {
        AppBlocksManageCsv sut = spy(new AppBlocksManageCsv());

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getAccessLevel()).thenReturn(AccessLevel.AL2.getLevel());

        doReturn(true).when(sut).lsFileUser();

        // input: nome file da modificare
        GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("missing\n".getBytes(StandardCharsets.UTF_8)));

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
             MockedStatic<Constant> cst = mockStatic(Constant.class);
             MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);

            // path finto: punta a cartella temporanea vuota
            Path tempDir = Files.createTempDirectory("csvtest");
            cst.when(Constant::getFilePathCsv).thenReturn(tempDir.toString() + File.separator);

            sut.editFileCsvFile();

            fm.verifyNoInteractions(); // non deve aprire editor
        }
    }

    @Test
    void editFileCsvFile_callsWriteCsvLikeEditor_whenFileExists() throws Exception {
        AppBlocksManageCsv sut = spy(new AppBlocksManageCsv());

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getAccessLevel()).thenReturn(AccessLevel.AL2.getLevel());

        doReturn(true).when(sut).lsFileUser();

        GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("doc\n".getBytes(StandardCharsets.UTF_8)));

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
             MockedStatic<Constant> cst = mockStatic(Constant.class);
             MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);

            Path tempDir = Files.createTempDirectory("csvtest");
            cst.when(Constant::getFilePathCsv).thenReturn(tempDir.toString() + File.separator);

            // creo davvero doc.csv così Files.exists() torna true
            Path file = tempDir.resolve("doc.csv");
            Files.write(file, "a,b\n1,2\n".getBytes(StandardCharsets.UTF_8));

            sut.editFileCsvFile();

            fm.verify(() -> CsvFileManager.writeCsvLikeEditor(file.toString()));
        }
    }

    @Test
    void saveAllFileInFolderIntoCsvTable_addsOnlyMissingInData() throws Exception {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getUuid()).thenReturn("U1");

        Path tempDir = Files.createTempDirectory("csvfolder");
        Files.write(tempDir.resolve("doc1.csv"), new byte[] {1});
        Files.write(tempDir.resolve("doc2.csv"), new byte[] {2});

        CsvDto dto1 = new CsvDto("doc1", "U1", new byte[] {1});

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
             MockedStatic<Constant> cst = mockStatic(Constant.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);
            cst.when(Constant::getFilePathCsv).thenReturn(tempDir.toString() + File.separator);

            when(csvUseCase.checknameFileAlreadyExistOnlyInData("doc1", "U1")).thenReturn(false);
            when(csvUseCase.checknameFileAlreadyExistOnlyInData("doc2", "U1")).thenReturn(true);

            when(csvUseCase.convertFileCsvToCsvDto("doc1", session)).thenReturn(dto1);

            sut.saveAllFileInFolderIntoCsvTable();

            verify(csvUseCase).addNewFileInCsvTableFromCsvDto(dto1);
            verify(csvUseCase, never()).addNewFileInCsvTableFromCsvDto(argThat(d -> "doc2".equals(d.getFileName())));
        }
    }

    @Test
    void readFileCsv_returnsImmediately_whenLsFileUserFalse() throws Exception {
        AppBlocksManageCsv sut = spy(new AppBlocksManageCsv());
        doReturn(false).when(sut).lsFileUser();

        sut.readFileCsv();

        verify(sut).lsFileUser();
    }

    @Test
    void readFileCsv_callsShowFileContent_untilTrue() throws Exception {
        AppBlocksManageCsv sut = spy(new AppBlocksManageCsv());
        doReturn(true).when(sut).lsFileUser();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getUuid()).thenReturn("U1");

        // input: nome file
        GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("file\n".getBytes(StandardCharsets.UTF_8)));

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
             MockedStatic<Quit> qt = mockStatic(Quit.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);
            qt.when(() -> Quit.quit(anyString())).thenReturn(false);

            when(csvUseCase.showFileContent("file", "U1")).thenReturn(true);

            sut.readFileCsv();

            verify(csvUseCase).showFileContent("file", "U1");
        }
    }

    @Test
    void lsFileUser_delegatesToStampListOfMyCsv() {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getUuid()).thenReturn("U1");

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);

            when(csvUseCase.stampListOfMyCsv("U1")).thenReturn(true);

            assertTrue(sut.lsFileUser());
            verify(csvUseCase).stampListOfMyCsv("U1");
        }
    }

    @Test
    void searchOnBaseAndMaybeSave_callsResearch_andThenDeletesRepo() throws Exception {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);

        try (MockedStatic<CsvResearchChoice> rc = mockStatic(CsvResearchChoice.class);
             MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

            sut.searchOnBaseAndMaybeSave();

            rc.verify(CsvResearchChoice::askAndExecuteSearch);
            verify(csvUseCase).deleteAllFileInRepo();
        }
    }

    @Test
    void searchOnBaseStatistic_callsResearch_andThenDeletesRepo() throws Exception {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);

        try (MockedStatic<CsvResearchChoice> rc = mockStatic(CsvResearchChoice.class);
             MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);

            sut.searchOnBaseStatistic();

            rc.verify(CsvResearchChoice::askAndExecuteStatisticSearch);
            verify(csvUseCase).deleteAllFileInRepo();
        }
    }

    @Test
    void deleteMyCsvFromDatabase_returns_whenNoFiles() {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getUuid()).thenReturn("U1");

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);

            when(csvUseCase.returnAllFileCsvDtoFromDataOfUser("U1")).thenReturn(Collections.emptyList());

            sut.deleteMyCsvFromDatabase();

            verify(csvUseCase, never()).deleteMyCsvByIndex(anyInt(), any());
        }
    }

    @Test
    void deleteMyCsvFromDatabase_quitStopsFlow() {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getUuid()).thenReturn("U1");

        List<CsvDto> list = Arrays.asList(
                new CsvDto("doc1", "U1", new byte[] {1})
        );

        GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("q\n".getBytes(StandardCharsets.UTF_8)));

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
             MockedStatic<Quit> qt = mockStatic(Quit.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);

            when(csvUseCase.returnAllFileCsvDtoFromDataOfUser("U1")).thenReturn(list);
            qt.when(() -> Quit.quit("q")).thenReturn(true);

            sut.deleteMyCsvFromDatabase();

            verify(csvUseCase, never()).deleteMyCsvByIndex(anyInt(), any());
        }
    }

    @Test
    void deleteMyCsvFromDatabase_confirmYes_deletesCorrectIndex() {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getUuid()).thenReturn("U1");

        List<CsvDto> list = Arrays.asList(
                new CsvDto("doc1", "U1", new byte[] {1}),
                new CsvDto("doc2", "U1", new byte[] {2})
        );

        // input: "2" -> index 1, poi conferma "s"
        GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("2\ns\n".getBytes(StandardCharsets.UTF_8)));

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
             MockedStatic<Quit> qt = mockStatic(Quit.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);
            qt.when(() -> Quit.quit(anyString())).thenReturn(false);

            when(csvUseCase.returnAllFileCsvDtoFromDataOfUser("U1")).thenReturn(list);
            when(csvUseCase.deleteMyCsvByIndex(1, session)).thenReturn(true);

            sut.deleteMyCsvFromDatabase();

            verify(csvUseCase).stampListOfMyCsv("U1");
            verify(csvUseCase).deleteMyCsvByIndex(1, session);
        }
    }

    @Test
    void deleteMyCsvFromDatabase_confirmNo_doesNotDelete() {
        AppBlocksManageCsv sut = new AppBlocksManageCsv();

        CsvUseCase csvUseCase = mock(CsvUseCase.class);
        Session session = mock(Session.class);
        when(session.getUuid()).thenReturn("U1");

        List<CsvDto> list = Arrays.asList(new CsvDto("doc1", "U1", new byte[] {1}));

        // input: "1" poi "n"
        GlobalScanner.scanner = new Scanner(new ByteArrayInputStream("1\nn\n".getBytes(StandardCharsets.UTF_8)));

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
             MockedStatic<Quit> qt = mockStatic(Quit.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(csvUseCase);
            sm.when(SessionManager::getCurrent).thenReturn(session);
            qt.when(() -> Quit.quit(anyString())).thenReturn(false);

            when(csvUseCase.returnAllFileCsvDtoFromDataOfUser("U1")).thenReturn(list);

            sut.deleteMyCsvFromDatabase();

            verify(csvUseCase, never()).deleteMyCsvByIndex(anyInt(), any());
        }
    }
}
