package it.unibg.progetto.api.csv_manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe di utilità per lavorare con i file CSV.
 *
 * - contiene il "file aziendale" di riferimento (DOCUMENTO_AZIENDALE.csv)
 * - permette di fare ricerche su quel file
 * - permette di salvare i risultati delle ricerche in nuovi file CSV nella cartella temporanea
 */
public class ManageCsvFile {

    /** Cartella dove si trovano sia il file aziendale sia i file temporanei. */
	private static final String CSV_FOLDER = "temporary_fileCSV_saving/";

    /** Nome (senza estensione) del file aziendale di base. */
	private static final String BASE_FILE_NAME = "DOCUMENTO_AZIENDALE";

    public ManageCsvFile() {
        // costruttore vuoto: la classe viene usata solo con metodi statici
    }

    /**
     * Crea un file CSV vuoto nella cartella temporanea.
     *
     * @param nameFile nome del file SENZA .csv
     */
    public static void createFileCsvOnFolder(String nameFile) {
        String path = CSV_FOLDER + nameFile + ".csv";

        try (FileWriter fileCsv = new FileWriter(path)) {
            // file creato (vuoto)
        } catch (IOException e) {
            // in un progetto reale si potrebbe loggare l'errore
            throw new RuntimeException("Errore nella creazione del file CSV: " + path, e);
        }
    }

    /**
     * Stampa a console il contenuto di un file CSV presente nella cartella temporanea.
     *
     * @param name nome del file SENZA .csv
     */
    public static void readFileCsv(String name) {
        String path = CSV_FOLDER + name + ".csv";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");

                // stampa in formato tabella
                for (String col : columns) {
                    System.out.print(col + "\t"); // \t = tabulazione
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // 1) METODI DI SUPPORTO PER LE RICERCHE
    // -------------------------------------------------------------------------

    /**
     * Legge tutte le righe (come array di String) di un file CSV della cartella
     * temporanea.
     *
     * @param nameFile   nome del file SENZA .csv
     * @param skipHeader se true salta la prima riga (intestazione)
     * @return lista di righe, dove ogni riga è un array di colonne
     */
    public static List<String[]> readAllRows(String nameFile, boolean skipHeader) {
    	 String path = CSV_FOLDER + nameFile + ".csv";
    	 List<String[]> rows = new ArrayList<>();

    	 try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first && skipHeader) {
                    first = false;
                    continue;
                }
                String[] columns = line.split(",");
                rows.add(columns);
                first = false;
            }

    	 } catch (IOException e) {
    	        throw new RuntimeException("Errore nella lettura del file CSV: " + path, e);
    	    }

        return rows;
    }

    /**
     * Legge e restituisce SOLO la prima riga (header) di un file CSV.
     *
     * @param nameFile nome del file SENZA .csv
     * @return header come stringa (la riga completa così com'è nel file), oppure null se il file è vuoto
     */
    public static String readHeader(String nameFile) {
        String path = CSV_FOLDER + nameFile + ".csv";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Errore nella lettura dell'header del file CSV: " + path, e);
        }
    }

    // -------------------------------------------------------------------------
    // 2) RICERCHE SUL FILE AZIENDALE (DOCUMENTO_AZIENDALE.csv)
    // -------------------------------------------------------------------------

    /**
     * Esegue una ricerca generica su DOCUMENTO_AZIENDALE.csv filtrando per
     * uguaglianza su una colonna.
     *
     * @param columnIndex indice della colonna (0 = id, 1 = nome, 2 = cognome, 3 = mail,
     *                    4 = numero_di_telefono, 5 = ruolo, 6 = anno_inizio, 7 = richiami)
     * @param value       valore da cercare (confronto equals ignorando maiuscole/minuscole)
     * @return lista di righe (senza header) che soddisfano la condizione
     */
    public static List<String[]> searchOnBaseFileByColumnEquals(int columnIndex, String value) {
        List<String[]> allRows = readAllRows(BASE_FILE_NAME, true); // true = salta header
        List<String[]> result = new ArrayList<>();

        for (String[] row : allRows) {
            if (columnIndex < 0 || columnIndex >= row.length) {
                continue;
            }
            String cell = row[columnIndex];
            if (cell != null && cell.equalsIgnoreCase(value)) {
                result.add(row);
            }
        }

        return result;
    }

    /**
     * Esempio di ricerca: tutti i dipendenti con un certo ruolo (Developer, HR, Manager, ...).
     *
     * @param ruolo ruolo da cercare
     * @return lista di righe corrispondenti
     */
    public static List<String[]> searchByRuolo(String ruolo) {
        // nella struttura del file aziendale la colonna "ruolo" è la numero 5 (0-based)
        return searchOnBaseFileByColumnEquals(5, ruolo);
    }

    /**
     * Esempio di ricerca: tutti i dipendenti assunti a partire da un certo anno.
     *
     * @param annoInizioMin anno minimo (incluso)
     * @return lista di righe corrispondenti
     */
    public static List<String[]> searchByAnnoInizioMaggioreUguale(int annoInizioMin) {
        List<String[]> allRows = readAllRows(BASE_FILE_NAME, true);
        List<String[]> result = new ArrayList<>();

        for (String[] row : allRows) {
            if (row.length <= 6) {
                continue;
            }
            try {
                int annoInizio = Integer.parseInt(row[6]);
                if (annoInizio >= annoInizioMin) {
                    result.add(row);
                }
            } catch (NumberFormatException e) {
                // riga con anno non valido -> la salto
            }
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // 3) SALVATAGGIO RISULTATI RICERCA IN UN NUOVO FILE CSV
    // -------------------------------------------------------------------------

    /**
     * Salva una lista di righe (risultato di una ricerca) in un nuovo file CSV
     * nella cartella temporanea.
     *
     * @param newFileName   nome del nuovo file SENZA .csv
     * @param rows          righe da scrivere (tipicamente risultato di una ricerca)
     * @param includeHeader se true scrive in cima l'header uguale a quello del file aziendale
     */
    public static void saveSearchResult(String newFileName, List<String[]> rows, boolean includeHeader) {
        String path = CSV_FOLDER + newFileName + ".csv";

        try (FileWriter writer = new FileWriter(path)) {

            if (includeHeader) {
                String header = readHeader(BASE_FILE_NAME);
                if (header != null) {
                    writer.write(header);
                    writer.write("\n");
                }
            }

            for (String[] row : rows) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }

        } catch (IOException e) {
            throw new RuntimeException("Errore nel salvataggio del risultato della ricerca nel file: " + path, e);
        }
    }

    /**
     * Metodo di utilità: stampa a console una lista di righe (uguale al modo in cui
     * readFileCsv stampa un file).
     *
     * @param rows righe da stampare
     */
    public static void printRows(List<String[]> rows) {
        for (String[] row : rows) {
            for (String col : row) {
                System.out.print(col + "\t");
            }
            System.out.println();
        }
    }
    
    
}
