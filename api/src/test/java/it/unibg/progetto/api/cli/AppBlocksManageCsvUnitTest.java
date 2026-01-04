package it.unibg.progetto.api.cli;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import it.unibg.progetto.api.application.dto.CsvDto;
import it.unibg.progetto.api.application.usecase.CsvUseCase;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.cli.components.Quit;
import it.unibg.progetto.api.domain.rules.AccessLevel;
import it.unibg.progetto.api.infrastructure.csv.CsvFileManager;
import it.unibg.progetto.api.security.session.Session;
import it.unibg.progetto.api.security.session.SessionManager;

class AppBlocksManageCsvUnitTest {

    @AfterEach
    void resetScanner() {
        GlobalScanner.setScanner(new Scanner(System.in));
    }

    @Test
    void controllOnFolderCsv_callsCsvFileManagerGetTempFolder() {
        AppBlocksManageCsv app = new AppBlocksManageCsv();

        try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {
            fm.when(CsvFileManager::getTempFolder).thenReturn(null);

            app.controllOnFolderCsv();

            fm.verify(CsvFileManager::getTempFolder);
        }
    }

    @Test
    void clearFolderCsv_callsTempFolderAndDeletesRepoFiles() {
        AppBlocksManageCsv app = new AppBlocksManageCsv();

        CsvUseCase useCase = mock(CsvUseCase.class);

        try (MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class);
             MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class)) {

            fm.when(CsvFileManager::getTempFolder).thenReturn(null);
            uc.when(CsvUseCase::getIstnce).thenReturn(useCase);

            app.clearFolderCsv();

            fm.verify(CsvFileManager::getTempFolder);
            verify(useCase).deleteAllFileInRepo();
        }
    }

    @Test
    void manageImplementationOfMainFileCsv_importsMainFile_whenNotAlreadyPresent() throws Exception {
        AppBlocksManageCsv app = new AppBlocksManageCsv();

        // input: percorso file
        GlobalScanner.setScanner(new Scanner(new ByteArrayInputStream(("/tmp/company.csv\n").getBytes())));

        CsvUseCase useCase = mock(CsvUseCase.class);
        Session current = mock(Session.class);

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(useCase);
            sm.when(SessionManager::getCurrent).thenReturn(current);

            // lista senza DOCUMENTO_AZIENDALE -> quindi deve importare
            when(useCase.returnAllFileCsvDtoFromData()).thenReturn(
                    Arrays.asList(new CsvDto("altro", "U1", new byte[] {1}))
            );

            app.manageImplementationOfMainFileCsv();

            verify(useCase).importFileFromLocalPc("/tmp/company.csv", current);
        }
    }

    @Test
    void createGeneralFileCsv_createsFile_andDoesNotOpenEditor_whenUserSaysNo() throws Exception {
        AppBlocksManageCsv app = new AppBlocksManageCsv();

        // input: nome file + "n" (non modificare)
        GlobalScanner.setScanner(new Scanner(new ByteArrayInputStream(("miofile\nn\n").getBytes())));

        CsvUseCase useCase = mock(CsvUseCase.class);
        Session current = mock(Session.class);
        when(current.getUuid()).thenReturn("U1");

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
             MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(useCase);
            sm.when(SessionManager::getCurrent).thenReturn(current);

            when(useCase.checknameFileAlreadyExist("miofile", "U1")).thenReturn(false);

            app.createGeneralFileCsv();

            fm.verify(() -> CsvFileManager.createFileCsvOnFolder("miofile"));
            // NON deve aprire editor
            fm.verify(() -> CsvFileManager.writeCsvLikeEditor(anyString()), never());
        }
    }

    @Test
    void editFileCsvFile_returnsImmediately_whenAccessLevelIsAL1() throws Exception {
        AppBlocksManageCsv app = new AppBlocksManageCsv();

        CsvUseCase useCase = mock(CsvUseCase.class);
        Session current = mock(Session.class);
        when(current.getAccessLevel()).thenReturn(AccessLevel.AL1.getLevel());

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
             MockedStatic<CsvFileManager> fm = mockStatic(CsvFileManager.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(useCase);
            sm.when(SessionManager::getCurrent).thenReturn(current);

            app.editFileCsvFile();

            // viene chiamato sempre all'inizio
            verify(useCase).saveAllFileCsvFromDataOfUser(current);
            // ma non deve aprire editor
            fm.verify(() -> CsvFileManager.writeCsvLikeEditor(anyString()), never());
        }
    }

    @Test
    void deleteMyCsvFromDatabase_quitDoesNotDeleteAnything() {
        AppBlocksManageCsv app = new AppBlocksManageCsv();

        // input 'q' per annullare
        GlobalScanner.setScanner(new Scanner(new ByteArrayInputStream(("q\n").getBytes())));

        CsvUseCase useCase = mock(CsvUseCase.class);
        Session current = mock(Session.class);
        when(current.getUuid()).thenReturn("U1");

        try (MockedStatic<CsvUseCase> uc = mockStatic(CsvUseCase.class);
             MockedStatic<SessionManager> sm = mockStatic(SessionManager.class);
             MockedStatic<Quit> qt = mockStatic(Quit.class)) {

            uc.when(CsvUseCase::getIstnce).thenReturn(useCase);
            sm.when(SessionManager::getCurrent).thenReturn(current);

            when(useCase.returnAllFileCsvDtoFromDataOfUser("U1"))
                .thenReturn(Arrays.asList(new CsvDto("f1", "U1", new byte[] {1})));

            qt.when(() -> Quit.quit("q")).thenReturn(true);

            app.deleteMyCsvFromDatabase();

            verify(useCase, never()).deleteMyCsvByIndex(anyInt(), any());
        }
    }
}
