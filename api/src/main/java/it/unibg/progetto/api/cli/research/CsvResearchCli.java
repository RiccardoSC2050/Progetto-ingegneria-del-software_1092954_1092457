package it.unibg.progetto.api.cli.research;


import java.util.List;

import it.unibg.progetto.api.domain.rules.CsvStandard;
import it.unibg.progetto.api.domain.rules.StringValues;
import it.unibg.progetto.api.infrastructure.csv.CsvFileManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CsvResearchCli {

	/**
	 * CONTOLLA SE IL ! è PRESNETE IN FONDO ALLA PAROLA O MENO
	 * 
	 * @param value
	 * @return
	 */
	private static boolean controlofSpecialChar(String value) {
		int l = value.length();
		if (value.contains("!")) {
			if (value.charAt(l - 1) == '!') {
				return true;
			}
		} else if (!value.contains("!")) {
			return true;
		}
		return false;
	}

	/**
	 * CREA LA BASE PER LA RICERCA MIRATA
	 * 
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
		List<String[]> allRows = CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true); // true =
																												// salta
																												// header

		// controllo di '!'
		if (controlofSpecialChar(value) == false) {
			System.out.println("errore di inserimento");
			return null;
		}

		List<String[]> result = new ArrayList<>();

		for (String[] row : allRows) {
			if (columnIndex < 0 || columnIndex >= row.length) {
				continue;
			}
			String cell = row[columnIndex];

			if (columnIndex == 4) {
				value.toLowerCase().strip().replace("+39", "").strip();
				if (value.toLowerCase().contains("!")
						&& value.toLowerCase().replace("!", "").strip().equals(cell.toLowerCase()))
					result.add(row);
				// aggiunge una riga se inizia con lettere dell'attributo colonna
				else if (cell.toLowerCase().replace("+39", "").strip().startsWith(value.toLowerCase().strip()))
					result.add(row);
			}

			else if (cell != null)

			{
				// aggiunge una riga se contiene punto esclamativo in fondo (ontrollato prima) e
				// se ovviaente è uguale al valore colonna
				if (value.toLowerCase().contains("!")
						&& value.toLowerCase().replace("!", "").strip().equals(cell.toLowerCase()))
					result.add(row);
				// aggiunge una riga se inizia con lettere dell'attributo colonna
				else if (cell.toLowerCase().startsWith(value.toLowerCase().strip()))
					result.add(row);
			}
		}

		return result;

	}

	/**
	 * RICERCA MIRATA SU UNA STRINGA valore di colonna sul doc DI LAVORatori NELLA
	 * AZIENDA
	 * 
	 * Esempio di ricerca: tutti i dipendenti con un certo ruolo (Developer, HR,
	 * Manager, ...). OK
	 * 
	 * @param ruolo ruolo da cercare
	 * @return lista di righe corrispondenti
	 */
	public static List<String[]> searchByStringValue(StringValues v, String value) {
		// nella struttura del file aziendale la colonna "ruolo" è la numero 5 (0-based)
		return searchOnBaseFileByColumnEquals(v.getIndex(), value);
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
		List<String[]> allRows = CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true);
		List<String[]> result = new ArrayList<>();

		for (String[] row : allRows) {
			if (row.length <= StringValues.ANNO_INIZIO.getIndex()) {
				continue;
			}
			try {
				int annoInizio = Integer.parseInt(row[StringValues.ANNO_INIZIO.getIndex()]);
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
	 * ritorna tutti quelli che sono dopo l'anno inserito
	 * 
	 * Esempio di ricerca: tutti i dipendenti assunti a partire da un certo anno. OK
	 *
	 * @param annoInizioMin anno minimo (incluso)
	 * @return lista di righe corrispondenti
	 */
	public static List<String[]> searchByMarker(int marker) {
		List<String[]> allRows = CsvFileManager.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true);
		List<String[]> result = new ArrayList<>();

		for (String[] row : allRows) {
			if (row.length <= StringValues.RICHIAMI.getIndex()) {
				continue;
			}
			try {
				int mark = Integer.parseInt(row[StringValues.RICHIAMI.getIndex()]);
				if (mark == marker) {
					result.add(row);
				}
			} catch (NumberFormatException e) {
				// riga con MARKER non valido -> la salto
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
		File csvFile = CsvFileManager.getTempCsvFile(newFileName);

		try (FileWriter writer = new FileWriter(csvFile)) {

			if (includeHeader) {
				String header = CsvFileManager.readHeader(CsvStandard.DOCUMENTO_AZIENDALE.toString());
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