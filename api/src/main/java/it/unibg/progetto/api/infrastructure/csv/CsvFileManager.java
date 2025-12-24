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
import java.util.List;
import java.util.Scanner;

import it.unibg.progetto.api.application.usecase.CsvUseCase;
import it.unibg.progetto.api.cli.components.Constant;
import it.unibg.progetto.api.cli.components.GlobalScanner;
import it.unibg.progetto.api.security.session.SessionManager;

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

		try (BufferedReader br = new BufferedReader(new FileReader(p))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains(",")) {
					String[] columns = line.split(",");
					for (String col : columns)
						System.out.print(col + "\t");
					System.out.println();
				} else {
					System.out.println(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public static void printRows(List<String[]> rows) {
		for (String[] row : rows) {
			for (String col : row) {
				System.out.print(col + "\t");
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