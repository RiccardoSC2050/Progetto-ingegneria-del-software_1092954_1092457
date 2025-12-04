package it.unibg.progetto.api.application;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import it.unibg.progetto.api.access_session.ManagerSession;
import it.unibg.progetto.api.access_session.Session;
import it.unibg.progetto.api.action_on.ActionOnCsv;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.conditions.CsvStandard;
import it.unibg.progetto.api.csv_manage.ManageCsvFile;
import it.unibg.progetto.api.dto.CsvDto;

public class AppBlocksManageCsv {

    public AppBlocksManageCsv() {
    }

    // =====================================================================
    // 1) VISUALIZZARE I MIEI CSV (DAL DB)
    // =====================================================================

    public void showMyCsvFiles() {
        Session current = ManagerSession.getCurrent();
        String uuid = current.getUuid();

        List<CsvDto> myFiles = ActionOnCsv.getIstnce().returnAllFileCsvDtoFromDataOfUser(uuid);

        if (myFiles == null || myFiles.isEmpty()) {
            System.out.println("Non hai ancora nessun file CSV salvato.");
            System.out.println("Usa il comando 'write' per creare un nuovo file.");
            return;
        }

        System.out.println("=== I TUOI FILE CSV ===");
        for (CsvDto c : myFiles) {
            System.out.println("- " + c.getFileName() + " (ownerId=" + c.getOwnerId() + ")");
        }
        System.out.println();
    }

    // =====================================================================
    // 2) FILE AZIENDALE DI RIFERIMENTO (DOCUMENTO_AZIENDALE)
    // =====================================================================

    /**
     * CONTROLLO PRESENZA DEL FILE DI RIFERIMENTO AZIENDALE (DOCUMENTO_AZIENDALE)
     */
    private boolean checkControlImportMainFileCsv() {
        List<CsvDto> csvDtoList = ActionOnCsv.getIstnce().returnAllFileCsvDtoFromData();
        if (csvDtoList == null) {
            return false;
        }
        for (CsvDto c : csvDtoList) {
            if (c.getFileName().equals(CsvStandard.DOCUMENTO_AZIENDALE.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * CARICAMENTO DEL FILE AZIENDALE DI RIFERIMENTO DA PERCORSO LOCALE
     */
    private void importMainFile() {
        try {
            System.out.println("Inserisci file aziendale di riferimento:");
            System.out.println("Percorso completo del file CSV:");
            String path = GlobalScaner.scanner.nextLine();

            ActionOnCsv.getIstnce().importFileFromLocalPc(path, ManagerSession.getCurrent());
            System.out.println("File aziendale importato correttamente.\n");
        } catch (Exception e) {
            System.out.println("Errore nell'import del file aziendale: " + e.getMessage());
        }
    }

    /**
     * CASO IN CUI SI STA AVVIANDO E CONFIGURANDO PER LA PRIMA VOLTA L'APP.
     * Controlla se il DOCUMENTO_AZIENDALE esiste nel DB, altrimenti lo richiede.
     */
    public void manageImplementationOfMainFileCsv() throws IOException {
        if (!checkControlImportMainFileCsv()) {
            System.out.println("IMPORTANTE: devi caricare il file di riferimento dell'azienda (DOCUMENTO_AZIENDALE).");
            importMainFile();
        }
    }

    // =====================================================================
    // 3) PULIZIA CARTELLA TEMPORANEA (FILE PERSONALI, NON L'AZIENDALE)
    // =====================================================================

    /**
     * Pulisce la cartella temporanea da tutti i CSV utente,
     * lasciando intatto il DOCUMENTO_AZIENDALE.
     * (Delegato ad ActionOnCsv per tenere la logica in un solo posto.)
     */
    public void clearFolderCsv() {
        ActionOnCsv.getIstnce().deleteAllFileInRepo();
    }

    // =====================================================================
    // 4) CREAZIONE DI UN PROPRIO FILE CSV (COMANDO "write")
    // =====================================================================

    /**
     * Permette di creare un file CSV "vuoto" nella cartella temporanea.
     * Sarà un file personale dell'utente, che potrà poi essere salvato nel DB.
     */
    public void createGeneralFileCsv() {
        Session current = ManagerSession.getCurrent();
        String name;

        while (true) {
            System.out.println("Inserire il nome del file (senza .csv):");
            name = GlobalScaner.scanner.nextLine().trim();

            if (!ActionOnCsv.getIstnce().checknameFileAlreadyExist(name, current)) {
                break;
            }

            System.out.println("Nome già esistente (in locale o nel DB). Scegli un altro nome.\n");
        }

        // Creo il file vuoto in cartella temporanea
        ManageCsvFile.createTempCsv(name);
        System.out.println("File temporaneo '" + name + ".csv' creato.\n");
    }

    // =====================================================================
    // 5) SALVARE TUTTI I FILE LOCALI NEL DB (COMANDO "save")
    // =====================================================================

    /**
     * SALVA TUTTI I FILE TEMPORANEI (NON GIÀ ESISTENTI IN DATA) NELLA TABELLA CSV.
     * Dopo il salvataggio, puoi richiamare clearFolderCsv() per svuotare la cartella.
     */
    public void saveAllFileInFolderIntoCsvTable(Session current) {
        try {
            ActionOnCsv.getIstnce().saveAllTempCsvOfUser(current);
            System.out.println("File CSV salvati nel database.\n");
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio dei file CSV nel database: " + e.getMessage());
        }
    }

    // =====================================================================
    // 6) LETTURA DEI MIEI FILE CSV (LOCALE + DB) - COMANDO "read"
    // =====================================================================

    /**
     * MOSTRA I FILE DELL'UTENTE E PERMETTE DI LEGGERNE UNO (se esiste).
     */
    public void readFileCsv() throws Exception {
        Session current = ManagerSession.getCurrent();

        System.out.println("I tuoi file:");

        // 1) prendo la lista dei CSV dell'utente dal DB
        List<CsvDto> myFiles = ActionOnCsv.getIstnce().returnAllFileCsvDtoFromDataOfUser(current.getUuid());

        // 2) se la lista è vuota → esco subito
        if (myFiles == null || myFiles.isEmpty()) {
            System.out.println("  (non hai ancora nessun file CSV salvato)");
            System.out.println("  Usa il comando 'write' per creare un nuovo file.\n");
            return;
        }

        // 3) stampo i nomi dei file
        for (CsvDto c : myFiles) {
            System.out.println("- " + c.getFileName());
        }
        System.out.println();

        // 4) chiedo quale file aprire
        boolean ok = false;
        do {
            System.out.println("Quale vuoi visualizzare? (nome del file, oppure 'exit' per tornare indietro)");
            String name = GlobalScaner.scanner.nextLine().trim();

            if (name.equalsIgnoreCase("exit") || name.equalsIgnoreCase("q")) {
                System.out.println("Uscita dalla lettura dei file CSV.\n");
                break;
            }

            ok = ActionOnCsv.getIstnce().showFileContent(name, current);

            if (!ok) {
                System.out.println("Nessun file trovato con quel nome.");
                System.out.println("Riprova oppure digita 'exit' per uscire.\n");
            }
        } while (!ok);
    }

    // =====================================================================
    // 7) RICERCA PER RUOLO + SALVATAGGIO EVENTUALE
    // =====================================================================

    public void searchAndMaybeSaveByRuolo() throws Exception {
        Session current = ManagerSession.getCurrent();

        System.out.println("Inserisci il ruolo da cercare (es. Developer, HR, Manager):");
        String ruolo = GlobalScaner.scanner.nextLine();

        // 1) Faccio la ricerca sul file aziendale
        List<String[]> result = ManageCsvFile.searchByRuolo(ruolo);

        if (result.isEmpty()) {
            System.out.println("Nessun dipendente trovato con ruolo: " + ruolo);
            return;
        }

        System.out.println("Risultato della ricerca:");
        ManageCsvFile.printRows(result);

        // 2) Controllo il livello di accesso
        if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
            System.out.println("Non hai i permessi per salvare la ricerca (access level < 2).");
            return;
        }

        // 3) Chiedo se vuole salvare
        System.out.println("Vuoi salvare questa ricerca come file CSV? (s/n)");
        String answer = GlobalScaner.scanner.nextLine();

        if (!answer.equalsIgnoreCase("s")) {
            return; // l'utente non vuole salvare
        }

        System.out.println("Inserisci il nome del file da salvare (senza .csv):");
        String finalName = GlobalScaner.scanner.nextLine();

        // 4) Salvo temporaneamente la ricerca con nome STANDARD.csv
        ManageCsvFile.saveSearchResult(CsvStandard.STANDARD.toString(), result, true);

        // 5) Uso ActionOnCsv per rinominare STANDARD -> finalName, salvarlo in DB e cancellare il file locale
        ActionOnCsv.getIstnce().saveSearchStandardInDatabase(finalName, current);
    }

    // =====================================================================
    // 8) RICERCA GENERALE SU BASE + SALVATAGGIO EVENTUALE
    // =====================================================================

    // Metodo principale: gestisce il flusso generale
    public void searchOnBaseAndMaybeSave() throws Exception {
        Session current = ManagerSession.getCurrent();

        // 1) chiedo che tipo di ricerca fare e la eseguo
        List<String[]> result = askAndExecuteSearch();

        if (result == null || result.isEmpty()) {
            System.out.println("Nessun risultato trovato.\n");
            return;
        }

        // 2) stampo i risultati
        System.out.println("\nRisultati trovati:");
        ManageCsvFile.printRows(result);
        System.out.println();

        // 3) se l'utente è livello 1 può solo vedere
        if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
            System.out.println("Hai livello di accesso 1: puoi solo visualizzare i risultati, non salvarli.\n");
            return;
        }

        // 4) AL2 o AL3: posso chiedere se vuole salvare
        askAndSaveResult(current, result);
    }

    /**
     * Chiede all'utente il tipo di ricerca e la esegue sul DOCUMENTO_AZIENDALE.
     * Restituisce la lista di righe trovate (o null se scelta non valida / errore).
     */
    private List<String[]> askAndExecuteSearch() {
        System.out.println("=== RICERCHE SU DOCUMENTO_AZIENDALE ===");
        System.out.println("1) Cerca per ruolo");
        System.out.println("2) Cerca per anno di inizio (>= anno)");
        System.out.print("Scelta: ");

        String choice = GlobalScaner.scanner.nextLine().trim();
        List<String[]> result = null;

        switch (choice) {
            case "1":
                System.out.print("Inserisci il ruolo da cercare (es. Developer, HR, Manager): ");
                String ruolo = GlobalScaner.scanner.nextLine().trim();
                result = ManageCsvFile.searchByRuolo(ruolo);
                break;

            case "2":
                System.out.print("Inserisci l'anno minimo di inizio (es. 2018): ");
                String annoStr = GlobalScaner.scanner.nextLine().trim();
                try {
                    int anno = Integer.parseInt(annoStr);
                    result = ManageCsvFile.searchByAnnoInizioMaggioreUguale(anno);
                } catch (NumberFormatException e) {
                    System.out.println("Anno non valido.\n");
                }
                break;

            default:
                System.out.println("Scelta non valida.\n");
                break;
        }

        return result;
    }

    /**
     * Chiede se salvare i risultati e, se sì, li salva in STANDARD.csv e poi nel DB
     * con il nome scelto dall'utente.
     */
    private void askAndSaveResult(Session current, List<String[]> result) throws Exception {
        System.out.print("Vuoi salvare questi risultati in un nuovo CSV? [y/n]: ");
        String answer = GlobalScaner.scanner.nextLine().trim();
        if (!answer.equalsIgnoreCase("y")) {
            return;
        }

        System.out.print("Inserisci il nome del nuovo file (senza .csv): ");
        String finalName = GlobalScaner.scanner.nextLine().trim();

        // 1) salvo i risultati in STANDARD.csv nella folder temporanea
        ManageCsvFile.saveSearchResult(CsvStandard.STANDARD.toString(), result, true);

        // 2) mando STANDARD nel DB con il nome scelto
        ActionOnCsv.getIstnce().saveSearchStandardInDatabase(finalName, current);

        System.out.println("Ricerca salvata come file CSV \"" + finalName + "\" nel database.\n");
    }

    // =====================================================================
    // 9) VISUALIZZARE I CSV DIRETTAMENTE DAL DB (COMANDO "viewcsv")
    // =====================================================================

    public void viewMyCsvFromDatabase() {
        Session current = ManagerSession.getCurrent();

        List<CsvDto> list = ActionOnCsv.getIstnce().getMyCsvList(current);
        if (list == null || list.isEmpty()) {
            System.out.println("Non hai ancora nessun file CSV salvato nel database.\n");
            return;
        }

        System.out.println("=== I TUOI CSV SALVATI NEL DB ===");
        for (int i = 0; i < list.size(); i++) {
            CsvDto dto = list.get(i);
            System.out.println((i + 1) + ") " + dto.getFileName());
        }

        System.out.print("Quale vuoi visualizzare? [numero, oppure 'q' per annullare]: ");
        String input = GlobalScaner.scanner.nextLine().trim();
        if (input.equalsIgnoreCase("q")) {
            return;
        }

        int index;
        try {
            index = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Scelta non valida.\n");
            return;
        }

        if (index < 0 || index >= list.size()) {
            System.out.println("Indice fuori intervallo.\n");
            return;
        }

        CsvDto chosen = list.get(index);
        printCsvDtoContent(chosen);
    }

    private void printCsvDtoContent(CsvDto dto) {
        byte[] data = dto.getData();
        if (data == null || data.length == 0) {
            System.out.println("(File vuoto o senza dati)\n");
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(data), StandardCharsets.UTF_8))) {

            String line;
            System.out.println("\n=== CONTENUTO DI " + dto.getFileName() + ".csv ===");
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();

        } catch (IOException e) {
            System.out.println("Errore nella lettura del CSV dal database: " + e.getMessage());
        }
    }

    // =====================================================================
    // 10) ELIMINARE CSV DAL DB
    // =====================================================================

    public void deleteMyCsvFromDatabase() {
        Session current = ManagerSession.getCurrent();

        List<CsvDto> list = ActionOnCsv.getIstnce().getMyCsvList(current);
        if (list == null || list.isEmpty()) {
            System.out.println("Non hai ancora nessun file CSV salvato nel database.\n");
            return;
        }

        System.out.println("=== I TUOI CSV SALVATI NEL DB ===");
        for (int i = 0; i < list.size(); i++) {
            CsvDto dto = list.get(i);
            System.out.println((i + 1) + ") " + dto.getFileName());
        }

        System.out.print("Quale vuoi eliminare? [numero, oppure 'q' per annullare]: ");
        String input = GlobalScaner.scanner.nextLine().trim();
        if (input.equalsIgnoreCase("q")) {
            return;
        }

        int index;
        try {
            index = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Scelta non valida.\n");
            return;
        }

        if (index < 0 || index >= list.size()) {
            System.out.println("Indice fuori intervallo.\n");
            return;
        }

        CsvDto chosen = list.get(index);
        System.out.print("Sei sicuro di voler eliminare \"" + chosen.getFileName() + "\"? [y/n]: ");
        String confirm = GlobalScaner.scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Eliminazione annullata.\n");
            return;
        }

        boolean deleted = ActionOnCsv.getIstnce().deleteMyCsvByIndex(index, current);
        if (deleted) {
            System.out.println("File \"" + chosen.getFileName() + "\" eliminato.\n");
        } else {
            System.out.println("Impossibile eliminare il file.\n");
        }
    }
}
