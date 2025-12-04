package it.unibg.progetto.api.action_on;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.unibg.progetto.api.access_session.Session;
import it.unibg.progetto.api.conditions.CsvStandard;
import it.unibg.progetto.api.csv_manage.ManageCsvFile;
import it.unibg.progetto.api.dto.CsvDto;
import it.unibg.progetto.api.mapper.CsvMapper;
import it.unibg.progetto.data.Csv;
import it.unibg.progetto.service.CsvService;

@Component
public class ActionOnCsv {

    // ---------------- SINGLETON SPRING + STATIC ----------------

    private static ActionOnCsv istance;

    private final CsvService csvService;
    private final CsvMapper csvMapper;

    @Autowired
    public ActionOnCsv(CsvService csvService, CsvMapper csvMapper) {
        this.csvService = csvService;
        this.csvMapper = csvMapper;
        istance = this;
    }

    public static ActionOnCsv getIstnce() {
        return istance;
    }

    // QUANDO SI è LOGGATI OGNI FILE NEL FOLDER TEMPORANEO è DELL'UTENTE
    // TUTTI QUELLI PRESI PER ESSERE VISTI VENGONO DOPO LA LETTURA ELIMINATI DAL
    // FOLDER (se richiesto)

    // -------------------------------------------------------------------------
    // 1) CSVDTO <-> FILE.LOCALE
    // -------------------------------------------------------------------------

    /**
     * CSVDTO -> File.csv (nella cartella temporanea)
     *
     * Converte un CsvDto in un file CSV nel folder temporaneo.
     */
    public void createLocalFileFromCsvDto(CsvDto c) throws Exception {
        if (c == null) {
            System.out.println("ERRORE, il file/oggetto non esiste");
            return;
        }
        String nameFile = c.getFileName();
        File localFile = ManageCsvFile.getTempCsvFile(nameFile);

        Files.write(localFile.toPath(), c.getData());
    }

    /**
     * File.csv (in cartella temporanea) -> CsvDto
     *
     * Legge il file CSV dalla cartella temporanea e crea un CsvDto senza id
     * (sarà il DB a generare l'id).
     */
    public CsvDto convertFileCsvToCsvDro(String name, Session current) throws IOException {
        File f = ManageCsvFile.getTempCsvFile(name);

        if (!f.exists()) {
            throw new RuntimeException("Il file non esiste: " + f.getPath());
        }

        byte[] bytes = Files.readAllBytes(f.toPath());
        return new CsvDto(name, current.getUuid(), bytes);
    }

    // -------------------------------------------------------------------------
    // 2) CSVDTO <-> CSV (ENTITY)
    // -------------------------------------------------------------------------

    /**
     * CSVDTO -> CSV senza id.
     */
    public Csv conversionFromCsvdtoToCsvNoId(CsvDto c) {
        return csvMapper.toCsvFromCsvDtoNoId(c);
    }

    /**
     * CSVDTO -> CSV con id.
     */
    public Csv conversionFromCsvdtoToCsvWithId(CsvDto c) {
        return csvMapper.toCsvFromCsvDtoWithId(c);
    }

    /**
     * CSV -> CSVDTO
     */
    public CsvDto conversionFromCsvToCsvDto(Csv csv) {
        return csvMapper.csvConverterFromData(csv);
    }

    /**
     * SALVA UN OGGETTO CSVDTO NELLA CSV TABLE (DB).
     */
    public void addNewFileInCsvTableFromCsvDto(CsvDto c) {
        csvMapper.addCSVToDataBase(csvService, conversionFromCsvdtoToCsvNoId(c));
    }

    // -------------------------------------------------------------------------
    // 3) RINOMINA FILE STANDARD -> NOME FINALE
    // -------------------------------------------------------------------------

    /**
     * Dopo aver creato una ricerca salvata come STANDARD, la rinomina con il nome
     * finale scelto dall'utente.
     */
    private boolean changeNameFile(String nameFile) throws IOException {
        File oldFile = ManageCsvFile.getTempCsvFile(CsvStandard.STANDARD.toString());

        if (!oldFile.exists()) {
            throw new RuntimeException("Il file non esiste: " + oldFile.getPath());
        }

        File newFile = ManageCsvFile.getTempCsvFile(nameFile);

        Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return true;
    }

    // -------------------------------------------------------------------------
    // 4) CARICAMENTO / SCARICAMENTO CSV DAL DB
    // -------------------------------------------------------------------------

    /**
     * TRASFERISCE NEL FOLDER TEMPORANEO I FILE CSV DELL'UTENTE LOGGATO
     * (dal DB alla cartella temporanea).
     */
    public void saveAllFileCsvFromDataOfUser(Session current) throws Exception {
        String uuid = current.getUuid();

        List<CsvDto> csvdtoList = csvMapper.ListCsvFromDatabaseWithSpecificUUID(csvService, uuid);

        for (CsvDto c : csvdtoList) {
            createLocalFileFromCsvDto(c);
        }
    }

    /**
     * CANCELLA OGNI FILE NEL FOLDER TEMPORANEO (tranne il DOCUMENTO_AZIENDALE).
     * Uso tipico: post-logout.
     */
    public void deleteAllFileInRepo() {
        File[] allFiles = ManageCsvFile.listTempFiles();
        if (allFiles.length == 0) {
            return;
        }

        for (File f : allFiles) {
            if (!f.isFile()) continue;

            // NON cancello il file aziendale di base
            if (f.getName().equals(CsvStandard.DOCUMENTO_AZIENDALE + ".csv")) {
                continue;
            }

            f.delete();
        }
    }

    /**
     * CANCELLA UN FILE NEL FOLDER TEMPORANEO.
     */
    public void deleteOneFileInRepo(Path p) {
        File file = new File(p.toString());
        if (file.exists()) {
            file.delete();
        }
    }

    // -------------------------------------------------------------------------
    // 5) LETTURA DATI DAL DB (LISTE CSVDTO / CSV)
    // -------------------------------------------------------------------------

    /**
     * Ritorna una lista di CsvDto per TUTTI i file CSV salvati nel DB.
     */
    public List<CsvDto> returnAllFileCsvDtoFromData() {
        return csvMapper.ListCsvFromDatabase(csvService);
    }

    /**
     * Ritorna una lista di CsvDto di file CSV che l'utente (uuid) ha salvato.
     */
    public List<CsvDto> returnAllFileCsvDtoFromDataOfUser(String uuid) {
        return csvMapper.ListCsvFromDatabaseWithSpecificUUID(csvService, uuid);
    }

    /**
     * Ritorna la lista di tutte le entità Csv (entity).
     */
    public List<Csv> returnListCsv() {
        return csvMapper.ListCSv(csvService);
    }

    // -------------------------------------------------------------------------
    // 6) CHECK ESISTENZA NOMI FILE (TEMP + DB)
    // -------------------------------------------------------------------------

    /**
     * CONTROLLA SE IL NOME FILE ESISTE GIA, SE ESISTE RITORNA TRUE ALTRIMENTI FALSE
     * Controlla sia nel DB (per l'utente) che nel folder temporaneo.
     */
    public boolean checknameFileAlreadyExist(String name, Session current) {

        if (checknameFileAlreadyExistOnlyInFolder(name))
            return true;

        if (checknameFileAlreadyExistOnlyInData(name, current))
            return true;

        return false;
    }

    /**
     * CONTROLLA SE IL NOME FILE ESISTE GIA IN DATA (per l'utente),
     * SE ESISTE RITORNA TRUE ALTRIMENTI FALSE.
     */
    public boolean checknameFileAlreadyExistOnlyInData(String name, Session current) {

        List<CsvDto> csvdtoList = returnAllFileCsvDtoFromDataOfUser(current.getUuid());
        for (CsvDto c : csvdtoList) {
            if (name.equals(c.getFileName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * RITORNA IL CSV (entity) CHE HA QUEL NOME FILE, SE ESISTE.
     */
    private Csv whatIsTheLuckyNameFile(String name) {
        List<Csv> csvList = returnListCsv();
        for (Csv c : csvList) {
            if (name.equals(c.getFileName())) {
                return c;
            }
        }
        return null;
    }

    /**
     * CONTROLLA SE IL NOME FILE ESISTE GIA NEL FOLDER TEMPORANEO,
     * SE ESISTE RITORNA TRUE ALTRIMENTI FALSE.
     */
    public boolean checknameFileAlreadyExistOnlyInFolder(String name) {
        File[] allFiles = ManageCsvFile.listTempFiles();

        if (allFiles.length == 0) {
            return false;
        }

        for (File f : allFiles) {
            if (!f.isFile()) continue;

            String nameFile = f.getName().replace(".csv", "");
            if (name.equals(nameFile)) {
                return true;
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // 7) STAMPA / VISUALIZZAZIONE FILE
    // -------------------------------------------------------------------------

    /**
     * STAMPA TUTTI I FILE CHE L'UTENTE POSSIEDE (dal DB).
     */
    public void stampListOfMyCsv(Session current) {
        List<CsvDto> cList = returnAllFileCsvDtoFromDataOfUser(current.getUuid());

        for (CsvDto c : cList) {
            System.out.println(c.getFileName());
        }
    }

    /**
     * STAMPA IL FILE CHE L'UTENTE VUOLE LEGGERE.
     * Se il file non è presente né in folder né in DB, ritorna false.
     */
    public boolean showFileContent(String name, Session current) throws Exception {
        try {
            if (checknameFileAlreadyExistOnlyInFolder(name)) {
                // Se il file è già nel folder temp, lo leggo direttamente
                ManageCsvFile.readFileCsv(name);
                return true;
            } else if (checknameFileAlreadyExistOnlyInData(name, current)) {
                // Lo prendo dal DB, lo ricreo in locale e poi lo leggo
                Csv csvEntity = whatIsTheLuckyNameFile(name);
                if (csvEntity == null) {
                    return false;
                }
                CsvDto dto = conversionFromCsvToCsvDto(csvEntity);
                createLocalFileFromCsvDto(dto);
                ManageCsvFile.readFileCsv(name);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // volendo si può loggare l'errore
        }
        return false;
    }

    // -------------------------------------------------------------------------
    // 8) CARICAMENTO FILE AZIENDALE DA PC LOCALE
    // -------------------------------------------------------------------------

    /**
     * CARICAMENTO FILE AZIENDALE:
     * - copia il file indicato dal path nella cartella temporanea come DOCUMENTO_AZIENDALE.csv
     * - salva/aggiorna anche nel DB come file dell'utente corrente
     */
    public void importFileFromLocalPc(String path, Session current) throws IOException {
        Path sorgente = Paths.get(path);
        File destFile = ManageCsvFile.getTempCsvFile(CsvStandard.DOCUMENTO_AZIENDALE.toString());

        // 1) Copio (o sovrascrivo) il file aziendale nella cartella temporanea
        Files.copy(sorgente, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // 2) Lo salvo/aggiorno anche nel DB
        CsvDto dto = convertFileCsvToCsvDro(CsvStandard.DOCUMENTO_AZIENDALE.toString(), current);
        addNewFileInCsvTableFromCsvDto(dto);

        // 3) NON lo cancello dalla cartella: serve per le ricerche
    }

    // -------------------------------------------------------------------------
    // 9) SALVATAGGIO DI UNA RICERCA STANDARD NEL DB
    // -------------------------------------------------------------------------

    /**
     * Salva nel database la ricerca che è stata scritta temporaneamente in
     * STANDARD.csv, usando un nome finale scelto dall'utente (finalName).
     */
    public boolean saveSearchStandardInDatabase(String finalName, Session current) throws IOException {

        // controllo che NON esista già un file con quel nome
        if (checknameFileAlreadyExist(finalName, current)) {
            return false;
        }

        // rinomino STANDARD.csv -> <finalName>.csv
        changeNameFile(finalName);

        // converto in DTO e salvo in DB
        CsvDto dto = convertFileCsvToCsvDro(finalName, current);
        addNewFileInCsvTableFromCsvDto(dto);

        // cancello il file locale
        File f = ManageCsvFile.getTempCsvFile(finalName);
        deleteOneFileInRepo(f.toPath());

        return true;
    }

    // -------------------------------------------------------------------------
    // 10) FUNZIONI "MIE" DI SUPPORTO PER L'UTENTE LOGGATO
    // -------------------------------------------------------------------------

    /**
     * Restituisce la lista di CsvDto del SOLO utente loggato.
     */
    public List<CsvDto> getMyCsvList(Session current) {
        return csvMapper.ListCsvFromDatabaseWithSpecificUUID(csvService, current.getUuid());
    }

    /**
     * Elimina il CSV in posizione "index" nella lista dell'utente.
     */
    public boolean deleteMyCsvByIndex(int index, Session current) {
        List<CsvDto> list = getMyCsvList(current);
        if (list == null || index < 0 || index >= list.size()) {
            return false;
        }

        CsvDto dto = list.get(index);
        Csv entity = csvMapper.toCsvFromCsvDtoWithId(dto);
        csvService.deleteFileCsv(entity);
        return true;
    }

    /**
     * Salva nel DB tutti i CSV presenti nella cartella temporanea
     * (tranne il DOCUMENTO_AZIENDALE) come file dell'utente corrente.
     *
     * Questo è il metodo che puoi chiamare da AppBlocksManageCsv
     * quando fai il comando "save" o in uscita.
     */
    public void saveAllLocalCsvOfUser(Session current) throws IOException {
        File[] allFiles = ManageCsvFile.listTempFiles();
        if (allFiles.length == 0) {
            return;
        }

        for (File f : allFiles) {
            if (!f.isFile()) continue;

            String nameFile = f.getName().replace(".csv", "");

            // non salvo il documento aziendale come "file personale"
            if (nameFile.equals(CsvStandard.DOCUMENTO_AZIENDALE.toString())) {
                continue;
            }

            if (!checknameFileAlreadyExistOnlyInData(nameFile, current)) {
                CsvDto dto = convertFileCsvToCsvDro(nameFile, current);
                addNewFileInCsvTableFromCsvDto(dto);
            }
        }
    }

    /**
     * Alias più "parlante" se vuoi usarlo altrove
     * (puoi anche usare direttamente saveAllLocalCsvOfUser).
     */
    public void saveAllTempCsvOfUser(Session current) throws IOException {
        saveAllLocalCsvOfUser(current);
    }
}
