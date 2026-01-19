package it.unibg.progetto.api.infrastructure.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import it.unibg.progetto.api.cli.components.Constant;
import it.unibg.progetto.api.cli.components.GlobalScanner;

public class CsvFileManager {

	public CsvFileManager() {
	}

	/**
	 * CHECK SU FOLDER TEMPORANEO LO CREA SE NON ESISTE Ritorna l'oggetto File che
	 * rappresenta la cartella temporanea. Se non esiste, la crea. OK
	 */
	public static File getTempFolder() {
		File folder = new File(Constant.getFilePathCsv());
		if (!folder.exists()) {
			folder.mkdirs(); // la crea se non esiste
		}
		return folder;
	}

	/**
	 * 
	 * RITORNA UN FILE CSV DI TIPO FILE, SPECIFICO AL NOME Restituisce il File
	 * relativo ad un CSV (aziendale o temporaneo) dato il nome SENZA estensione.
	 *
	 */
	public static File getTempCsvFile(String nameFile) {
		File folder = getTempFolder();
		return new File(folder, nameFile + ".csv");
	}

	/**
	 * crea un file csv con un nome specifico
	 * 
	 * @param nameFile
	 */
	public static void createFileCsvOnFolder(String nameFile) {
		String p = Constant.getFilePathCsv() + nameFile + ".csv";

		try (FileWriter fileCsv = new FileWriter(p)) {

		} catch (Exception e) {

		}
	}

	public static void readFileCsv(String name) {
		name = name.strip();
		if (name.endsWith(".csv"))
			name = name.substring(0, name.length() - 4);

		String p = Constant.getFilePathCsv() + name + ".csv";

		List<String[]> rows = new ArrayList<>();
		int[] maxWidths = null;

		try (BufferedReader br = new BufferedReader(new FileReader(p))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] cols = line.split(",", -1); // -1 mantiene anche colonne vuote
				rows.add(cols);

				// inizializzo maxWidths alla prima riga
				if (maxWidths == null)
					maxWidths = new int[cols.length];

				// se una riga ha più colonne, espando maxWidths
				if (cols.length > maxWidths.length) {
					maxWidths = Arrays.copyOf(maxWidths, cols.length);
				}

				for (int i = 0; i < cols.length; i++) {
					String cell = cols[i] == null ? "" : cols[i].strip();
					maxWidths[i] = Math.max(maxWidths[i], cell.length());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		if (rows.isEmpty()) {
			System.out.println("(file vuoto)");
			return;
		}

		// stampa con padding + separatori
		for (int r = 0; r < rows.size(); r++) {
			String[] cols = rows.get(r);

			// riga
			System.out.print("| ");
			for (int i = 0; i < maxWidths.length; i++) {
				String cell = (i < cols.length && cols[i] != null) ? cols[i].strip() : "";
				System.out.print(padRight(cell, maxWidths[i]));
				System.out.print(" | ");
			}
			System.out.println();

			// separatore dopo header (prima riga)
			if (r == 0) {
				System.out.print("|-");
				for (int i = 0; i < maxWidths.length; i++) {
					System.out.print("-".repeat(maxWidths[i]));
					System.out.print("-|-");
				}
				System.out.println();
			}
		}
	}

	private static String padRight(String s, int width) {
		if (s == null)
			s = "";
		if (s.length() >= width)
			return s;
		return s + " ".repeat(width - s.length());
	}

	/**
	 * UTILE PER LE RICERCHE MIRATE SU FILE
	 * 
	 * Legge tutte le righe (come array di String) di un file CSV della cartella
	 * temporanea (o del file aziendale, se nameFile = BASE_FILE_NAME). OK
	 *
	 * @param nameFile   nome del file SENZA .csv
	 * @param skipHeader se true salta la prima riga (intestazione)
	 * @return lista di righe, dove ogni riga è un array di colonne
	 */
	public static List<String[]> readAllRows(String nameFile, boolean skipHeader) {
		File csvFile = getTempCsvFile(nameFile);
		List<String[]> rows = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

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
			throw new RuntimeException("Errore nella lettura del file CSV: " + csvFile.getPath(), e);
		}

		return rows;
	}

	/**
	 * Legge e restituisce SOLO la prima riga (header) di un file CSV. OK
	 *
	 * @param nameFile nome del file SENZA .csv
	 * @return header come stringa (la riga completa così com'è nel file), oppure
	 *         null se il file è vuoto
	 */
	public static String readHeader(String nameFile) {
		File csvFile = getTempCsvFile(nameFile);

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			return br.readLine();
		} catch (IOException e) {
			throw new RuntimeException("Errore nella lettura dell'header del file CSV: " + csvFile.getPath(), e);
		}
	}

	// ciao
	public static void printRows(List<String[]> rows) {
		if (rows == null || rows.isEmpty()) {
			System.out.println("(nessun dato)");
			return;
		}

		// calcola numero massimo di colonne
		int colsCount = 0;
		for (String[] row : rows) {
			if (row != null) {
				colsCount = Math.max(colsCount, row.length);
			}
		}

		// calcola larghezza massima per colonna
		int[] maxWidths = new int[colsCount];
		for (String[] row : rows) {
			if (row == null)
				continue;
			for (int i = 0; i < row.length; i++) {
				String cell = row[i] == null ? "" : row[i];
				maxWidths[i] = Math.max(maxWidths[i], cell.length());
			}
		}

		// stampa righe
		for (String[] row : rows) {
			if (row == null)
				continue;

			System.out.print("| ");
			for (int i = 0; i < colsCount; i++) {
				String cell = (i < row.length && row[i] != null) ? row[i] : "";
				System.out.print(padRight(cell, maxWidths[i]));
				System.out.print(" | ");
			}
			System.out.println();
		}
	}

	public static void writeCsvLikeEditor(String filePath) throws Exception {

		Path path = Paths.get(filePath);

		if (!Files.exists(path)) {
			System.out.println("Errore: il file non esiste.");
			return;
		}

		Scanner scanner = GlobalScanner.scanner; // NON creare un nuovo Scanner
		List<String> out = new ArrayList<>();

		System.out.println("--- MODALITÀ SCRITTURA CSV ---");
		System.out.println("Scrivi righe libere.");
		System.out.println("Digita :wq per salvare e uscire");
		System.out.println("-----------------------------");

		while (true) {
			System.out.print("> ");
			String line = scanner.nextLine();

			String cmd = line.strip();
			if (cmd.equalsIgnoreCase(":wq"))
				break;

			out.add(line + System.lineSeparator()); // newline garantita
		}

		Files.write(path, out, StandardCharsets.UTF_8, StandardOpenOption.APPEND);

		System.out.println("File salvato correttamente.");
	}

}