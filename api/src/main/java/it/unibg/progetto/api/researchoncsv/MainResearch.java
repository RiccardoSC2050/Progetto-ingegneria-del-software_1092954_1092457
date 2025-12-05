package it.unibg.progetto.api.researchoncsv;

import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import it.unibg.progetto.api.conditions.CsvStandard;
import it.unibg.progetto.api.csv_manage.ManageCsvFile;

public class MainResearch {

	/**
	 * CREA LA BASE PER LA RICERCA MIRATA
	 * 
	 * Esegue una ricerca generica su DOCUMENTO_AZIENDALE.csv filtrando per
	 * uguaglianza su una colonna. OK
	 *
	 * @param columnIndex indice della colonna (0 = id, 1 = nome, 2 = cognome, 3 =
	 *                    mail, 4 = numero_di_telefono, 5 = ruolo, 6 = anno_inizio,
	 *                    7 = richiami)
	 * @param value       valore da cercare (confronto equals ignorando
	 *                    maiuscole/minuscole)
	 * @return lista di righe (senza header) che soddisfano la condizione
	 */
	public static List<String[]> searchOnBaseFileByColumnEquals(int columnIndex, String value) {
		List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true); // true =
																												// salta
																												// header
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
	 * RICERCA MIRATA SUL RUOLO DI LAVORO NELLA AZIENDA
	 * 
	 * Esempio di ricerca: tutti i dipendenti con un certo ruolo (Developer, HR,
	 * Manager, ...). OK
	 * 
	 * @param ruolo ruolo da cercare
	 * @return lista di righe corrispondenti
	 */
	public static List<String[]> searchByRuolo(String ruolo) {
		// nella struttura del file aziendale la colonna "ruolo" è la numero 5 (0-based)
		return searchOnBaseFileByColumnEquals(5, ruolo);
	}

	/**
	 * ritorna tutti quelli che sono dopo l'anno inserito
	 * 
	 * Esempio di ricerca: tutti i dipendenti assunti a partire da un certo anno. OK
	 *
	 * @param annoInizioMin anno minimo (incluso)
	 * @return lista di righe corrispondenti
	 */
	public static List<String[]> searchByAnnoInizioMaggioreUguale(int annoInizioMin) {
		List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true);
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

	/**
	 * Salva una lista di righe (risultato di una ricerca) in un nuovo file CSV
	 * nella cartella temporanea. OK
	 * 
	 *
	 * @param newFileName   nome del nuovo file SENZA .csv
	 * @param rows          righe da scrivere (tipicamente risultato di una ricerca)
	 * @param includeHeader se true scrive in cima l'header uguale a quello del file
	 *                      aziendale
	 */
	public static void saveSearchResult(String newFileName, List<String[]> rows, boolean includeHeader) {
		File csvFile = ManageCsvFile.getTempCsvFile(newFileName);

		try (FileWriter writer = new FileWriter(csvFile)) {

			if (includeHeader) {
				String header = ManageCsvFile.readHeader(CsvStandard.DOCUMENTO_AZIENDALE.toString());
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
			throw new RuntimeException(
					"Errore nel salvataggio del risultato della ricerca nel file: " + csvFile.getPath(), e);
		}
	}

}