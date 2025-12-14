package it.unibg.progetto.api.researchoncsv;

import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.unibg.progetto.api.action_on.ActionOnCsv;
import it.unibg.progetto.api.conditions.CsvStandard;
import it.unibg.progetto.api.conditions.StringValue;
import it.unibg.progetto.api.csv_manage.ManageCsvFile;

public class StatisticResearch {

	/**
	 * STAT 1) Numero totale dipendenti (righe valide).
	 *
	 * Considero "valida" una riga se: - ha almeno tutte le colonne fino a RICHIAMI
	 * - ha ID non vuoto - ha NOME e COGNOME non vuoti
	 *
	 * (puoi rendere più o meno "stretta" la validazione a piacere)
	 * 
	 * @throws Exception
	 */
	public static int countTotalValidEmployees() throws Exception {
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
		List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true); // skip
																												// header
		int count = 0;

		for (String[] row : allRows) {
			if (isValidEmployeeRow(row)) {
				count++;
			}
		}

		return count;
	}

	/**
	 * Helper: controlla se la riga è "valida" secondo le regole sopra.
	 */
	private static boolean isValidEmployeeRow(String[] row) {
		if (row == null)
			return false;

		// deve contenere tutte le colonne previste (fino a RICHIAMI incluso)
		if (row.length <= StringValue.RICHIAMI.getIndex())
			return false;

		String id = row[StringValue.ID.getIndex()];
		String nome = row[StringValue.NOME.getIndex()];
		String cognome = row[StringValue.COGNOME.getIndex()];

		if (id == null || id.isBlank())
			return false;
		if (nome == null || nome.isBlank())
			return false;
		if (cognome == null || cognome.isBlank())
			return false;

		return true;
	}

	/**
	 * STAT 2) Conteggio dipendenti per ruolo.
	 *
	 * Ritorna una mappa: ruolo -> numero dipendenti. Conta solo le righe "valide"
	 * (usa isValidEmployeeRow).
	 * 
	 * @throws Exception
	 */
	public static Map<String, Integer> countEmployeesByRole() throws Exception {

		List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true); // skip
																												// header
		Map<String, Integer> counts = new LinkedHashMap<>();

		for (String[] row : allRows) {

			if (!isValidEmployeeRow(row)) {
				continue;
			}

			// colonna RUOLO (0-based) secondo il tuo enum StringValue
			String ruolo = row[StringValue.RUOLO.getIndex()];

			if (ruolo == null || ruolo.isBlank()) {
				ruolo = "NON_SPECIFICATO";
			} else {
				ruolo = ruolo.strip();
			}

			counts.put(ruolo, counts.getOrDefault(ruolo, 0) + 1);
		}

		return counts;
	}

	/**
	 * STAT 3) Percentuali dipendenti per ruolo. Usa countEmployeesByRole() e
	 * calcola le percentuali sul totale delle righe valide.
	 *
	 * @return mappa ruolo -> percentuale (0..100)
	 * @throws Exception
	 */
	public static Map<String, Double> percentEmployeesByRole() throws Exception {
		Map<String, Integer> counts = countEmployeesByRole();
		Map<String, Double> perc = new LinkedHashMap<>();

		if (counts == null || counts.isEmpty()) {
			return perc;
		}

		int total = 0;
		for (int n : counts.values())
			total += n;

		if (total == 0)
			return perc;

		for (Map.Entry<String, Integer> e : counts.entrySet()) {
			double p = (e.getValue() * 100.0) / total;
			perc.put(e.getKey(), p);
		}

		return perc;
	}

	/**
	 * STAT 4) Anni di inizio: minimo / massimo.
	 *
	 * @return int[]{min, max} oppure null se non trova nessun anno valido.
	 */
	public static int[] minMaxAnnoInizio() {
		List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true);

		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		boolean found = false;

		for (String[] row : allRows) {
			if (row == null || row.length <= StringValue.ANNO_INIZIO.getIndex())
				continue;

			String cell = row[StringValue.ANNO_INIZIO.getIndex()];
			if (cell == null || cell.isBlank())
				continue;

			try {
				int anno = Integer.parseInt(cell.strip());
				found = true;
				if (anno < min)
					min = anno;
				if (anno > max)
					max = anno;
			} catch (NumberFormatException e) {
				// anno non valido -> skip
			}
		}

		if (!found)
			return null;
		return new int[] { min, max };
	}

	/**
	 * STAT 5) Media anno di inizio + anzianità media (in anni).
	 *
	 * @return double[]{mediaAnnoInizio, anzianitaMedia} oppure null se nessun dato
	 *         valido.
	 */
	public static double[] averageAnnoInizioAndSeniority() {
		List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true);

		int currentYear = Year.now().getValue();

		long sumYears = 0;
		long sumSeniority = 0;
		int count = 0;

		for (String[] row : allRows) {
			if (row == null || row.length <= StringValue.ANNO_INIZIO.getIndex())
				continue;

			String cell = row[StringValue.ANNO_INIZIO.getIndex()];
			if (cell == null || cell.isBlank())
				continue;

			try {
				int anno = Integer.parseInt(cell.strip());

				// filtro "sensato" (puoi cambiarlo se vuoi)
				if (anno < 1900 || anno > currentYear)
					continue;

				sumYears += anno;
				sumSeniority += (currentYear - anno);
				count++;

			} catch (NumberFormatException e) {
				// anno non valido -> skip
			}
		}

		if (count == 0)
			return null;

		double mediaAnno = sumYears / (double) count;
		double anzianitaMedia = sumSeniority / (double) count;

		return new double[] { mediaAnno, anzianitaMedia };
	}

	/**
	 * STAT 6) Distribuzione dipendenti per anno di inizio.
	 *
	 * @return mappa ordinata anno -> conteggio
	 */
	public static Map<Integer, Integer> countEmployeesByStartYear() {
		List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true);

		Map<Integer, Integer> map = new TreeMap<>(); // ordinata per anno crescente
		int currentYear = Year.now().getValue();

		for (String[] row : allRows) {
			if (row == null || row.length <= StringValue.ANNO_INIZIO.getIndex())
				continue;

			String cell = row[StringValue.ANNO_INIZIO.getIndex()];
			if (cell == null || cell.isBlank())
				continue;

			try {
				int anno = Integer.parseInt(cell.strip());

				// filtro sensato
				if (anno < 1900 || anno > currentYear)
					continue;

				map.put(anno, map.getOrDefault(anno, 0) + 1);

			} catch (NumberFormatException e) {
				// anno non valido -> skip
			}
		}

		return map;
	}
	
	/**
	 * STAT 7) Richiami: totale / media / minimo / massimo.
	 *
	 * @return double[]{totale, media, min, max} oppure null se nessun dato valido.
	 */
	public static double[] statsRichiami() {
		List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true);

		long sum = 0;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		int count = 0;

		for (String[] row : allRows) {
			if (row == null || row.length <= StringValue.RICHIAMI.getIndex()) continue;

			String cell = row[StringValue.RICHIAMI.getIndex()];
			if (cell == null || cell.isBlank()) continue;

			try {
				int richiami = Integer.parseInt(cell.strip());

				// filtro sensato (nel tuo progetto li usi 0-4)
				if (richiami < 0 || richiami > 4) continue;

				sum += richiami;
				if (richiami < min) min = richiami;
				if (richiami > max) max = richiami;
				count++;

			} catch (NumberFormatException e) {
				// non valido -> skip
			}
		}

		if (count == 0) return null;

		double totale = (double) sum;
		double media = sum / (double) count;

		return new double[] { totale, media, min, max };
	}
	
	/**
	 * STAT 8) Top N dipendenti con più richiami.
	 *
	 * @param topN quanti risultati (es. 5)
	 * @return lista righe (ID, NOME, COGNOME, RUOLO, RICHIAMI)
	 */
	public static List<String[]> topEmployeesByRichiami(int topN) {
		List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true);

		List<String[]> valid = new ArrayList<>();

		for (String[] row : allRows) {
			if (row == null || row.length <= StringValue.RICHIAMI.getIndex()) continue;

			String cell = row[StringValue.RICHIAMI.getIndex()];
			if (cell == null || cell.isBlank()) continue;

			try {
				int richiami = Integer.parseInt(cell.strip());
				if (richiami < 0 || richiami > 4) continue;

				String id = safeCell(row, StringValue.ID.getIndex());
				String nome = safeCell(row, StringValue.NOME.getIndex());
				String cognome = safeCell(row, StringValue.COGNOME.getIndex());
				String ruolo = safeCell(row, StringValue.RUOLO.getIndex());

				valid.add(new String[] { id, nome, cognome, ruolo, String.valueOf(richiami) });

			} catch (NumberFormatException e) {
				// skip
			}
		}

		// ordina per richiami desc, poi cognome/nome
		valid.sort(new Comparator<String[]>() {
			@Override
			public int compare(String[] a, String[] b) {
				int ra = Integer.parseInt(a[4]);
				int rb = Integer.parseInt(b[4]);
				if (rb != ra) return Integer.compare(rb, ra);

				// tie-break
				int c = a[2].compareToIgnoreCase(b[2]);
				if (c != 0) return c;
				return a[1].compareToIgnoreCase(b[1]);
			}
		});

		if (topN <= 0) topN = 1;
		if (valid.size() > topN) {
			return new ArrayList<>(valid.subList(0, topN));
		}
		return valid;
	}

	/** Helper safe */
	private static String safeCell(String[] row, int idx) {
		if (row == null) return "";
		if (idx < 0 || idx >= row.length) return "";
		String v = row[idx];
		return (v == null) ? "" : v.strip();
	}

	/**
	 * STAT 9) Dipendenti con 0 richiami.
	 *
	 * @return lista righe (ID, NOME, COGNOME, RUOLO, RICHIAMI)
	 */
	public static List<String[]> employeesWithZeroRichiami() {
	    List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true);
	    List<String[]> result = new ArrayList<>();

	    for (String[] row : allRows) {
	        if (row == null || row.length <= StringValue.RICHIAMI.getIndex()) continue;

	        String cell = row[StringValue.RICHIAMI.getIndex()];
	        if (cell == null || cell.isBlank()) continue;

	        try {
	            int richiami = Integer.parseInt(cell.strip());
	            if (richiami != 0) continue;

	            String id = safeCell(row, StringValue.ID.getIndex());
	            String nome = safeCell(row, StringValue.NOME.getIndex());
	            String cognome = safeCell(row, StringValue.COGNOME.getIndex());
	            String ruolo = safeCell(row, StringValue.RUOLO.getIndex());

	            result.add(new String[] { id, nome, cognome, ruolo, "0" });

	        } catch (NumberFormatException e) {
	            // skip
	        }
	    }

	    return result;
	}

	/**
	 * STAT 10) Qualità dati: conteggio campi mancanti (null o blank).
	 *
	 * Conta i missing su: NOME, COGNOME, EMAIL, TELEFONO, RUOLO, ANNO_INIZIO.
	 *
	 * @return mappa "CAMPO" -> numero mancanti
	 */
	public static Map<String, Integer> countMissingFields() {
		List<String[]> allRows = ManageCsvFile.readAllRows(CsvStandard.DOCUMENTO_AZIENDALE.toString(), true);

		Map<String, Integer> miss = new LinkedHashMap<>();
		miss.put("NOME", 0);
		miss.put("COGNOME", 0);
		miss.put("EMAIL", 0);
		miss.put("TELEFONO", 0);
		miss.put("RUOLO", 0);
		miss.put("ANNO_INIZIO", 0);

		for (String[] row : allRows) {
			if (row == null) continue;

			if (isMissing(row, StringValue.NOME.getIndex())) miss.put("NOME", miss.get("NOME") + 1);
			if (isMissing(row, StringValue.COGNOME.getIndex())) miss.put("COGNOME", miss.get("COGNOME") + 1);
			if (isMissing(row, StringValue.MAIL.getIndex())) miss.put("EMAIL", miss.get("EMAIL") + 1);
			if (isMissing(row, StringValue.NUMERO_TELEFONO.getIndex())) miss.put("TELEFONO", miss.get("TELEFONO") + 1);
			if (isMissing(row, StringValue.RUOLO.getIndex())) miss.put("RUOLO", miss.get("RUOLO") + 1);
			if (isMissing(row, StringValue.ANNO_INIZIO.getIndex())) miss.put("ANNO_INIZIO", miss.get("ANNO_INIZIO") + 1);
		}

		return miss;
	}

	private static boolean isMissing(String[] row, int idx) {
		if (idx < 0 || idx >= row.length) return true;
		String v = row[idx];
		return (v == null || v.isBlank());
	}



}
