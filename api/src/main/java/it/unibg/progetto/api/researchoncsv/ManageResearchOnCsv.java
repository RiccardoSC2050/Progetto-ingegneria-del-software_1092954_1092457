package it.unibg.progetto.api.researchoncsv;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.unibg.progetto.api.access_session.ManagerSession;
import it.unibg.progetto.api.access_session.Session;
import it.unibg.progetto.api.action_on.ActionOnCsv;
import it.unibg.progetto.api.components.GlobalScaner;
import it.unibg.progetto.api.conditions.AccessLevel;
import it.unibg.progetto.api.conditions.Checks;
import it.unibg.progetto.api.conditions.CsvStandard;
import it.unibg.progetto.api.conditions.StringValue;
import it.unibg.progetto.api.csv_manage.ManageCsvFile;

public class ManageResearchOnCsv {

	// SISTEMA STANDARD PER SALVARE
	/**
	 * Chiede se salvare i risultati e, se sì, li salva in STANDARD.csv e poi nel DB
	 * con il nome scelto dall'utente.
	 */
	private static void askAndSaveResult(Session current, List<String[]> result) throws Exception {
		System.out.print("Vuoi salvare questi risultati in un nuovo CSV? [s/n]: ");
		String answer = GlobalScaner.scanner.nextLine().trim();
		if (!answer.equalsIgnoreCase("s")) {
			return;
		}

		System.out.print("Inserisci il nome del nuovo file (senza .csv): ");
		String finalName = GlobalScaner.scanner.nextLine().trim();

		// 1) salvo i risultati in STANDARD.csv nella folder temporanea
		MainResearch.saveSearchResult(CsvStandard.STANDARD.toString(), result, true);

		// 2) mando STANDARD nel DB con il nome scelto
		ActionOnCsv.getIstnce().changeNameFile(finalName);

		ActionOnCsv.getIstnce()
				.addNewFileInCsvTableFromCsvDto(ActionOnCsv.getIstnce().convertFileCsvToCsvDto(finalName, current));

		System.out.println("Ricerca salvata come file CSV \"" + finalName + "\" nel database.\n");
	}

	// RICERCA PER STRINGA
	/**
	 * da mettere in un'altra classe
	 * 
	 * @throws Exception
	 */
	public static void searchAndMaybeSave(StringValue v) throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
		boolean f;
		List<String[]> result = new ArrayList<String[]>();
		String ruolo;
		do {
			f = true;
			ruolo = "";
			System.out.println("Inserisci per cercare [ricerca per " + v.toString()
					+ "]\n[SE SAI CHE VUOI RICERCARE PROPRIO UNA PAROLA SPECIFICA INSERISCI ! ALLA FINE]:");
			ruolo = GlobalScaner.scanner.nextLine();

			// 1) Faccio la ricerca sul file aziendale
			result = MainResearch.searchByStringValue(v, ruolo);

			if (result != null) {
				f = false;
			} else if (result == null)
				f = true;

		} while (f);

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
		askAndSaveResult(ManagerSession.getCurrent(), result);
	}

	/**
	 * RICERCA MIRATA SU PERSONE POST ANNO INSERITO
	 * 
	 * @throws Exception
	 */
	public static void searchAndMaybeSaveByAnnoInizio() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

		System.out.print("Inserisci l'anno minimo di inizio (es. 2018): ");
		String annoStr = GlobalScaner.scanner.nextLine().trim();
		int anno = Integer.parseInt(annoStr);
		List<String[]> result = MainResearch.searchByAnnoInizioMaggioreUguale(anno);

		System.out.println("Risultato della ricerca:");
		ManageCsvFile.printRows(result);

		// 2) Controllo il livello di accesso
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la ricerca (access level < 2).");
			return;
		}

		// 3) Chiedo se vuole salvare
		askAndSaveResult(ManagerSession.getCurrent(), result);
	}

	public static void searchAndMaybeSaveByMarker() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());
		int marker;
		do {
			System.out.print("Inserisci il numero di segnalazioni (0-4): ");
			String mark = GlobalScaner.scanner.nextLine().trim();

			marker = Integer.parseInt(mark);
		} while (!(marker >= 0 && marker <= 4));
		List<String[]> result = MainResearch.searchByMarker(marker);

		System.out.println("Risultato della ricerca:");
		ManageCsvFile.printRows(result);

		// 2) Controllo il livello di accesso
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la ricerca (access level < 2).");
			return;
		}

		// 3) Chiedo se vuole salvare
		askAndSaveResult(ManagerSession.getCurrent(), result);
	}

	// ricerche statistiche

	public static void statsAndMaybeSaveCountByRole() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

		// 1) Calcolo statistica
		Map<String, Integer> counts = StatisticResearch.countEmployeesByRole();

		if (counts == null || counts.isEmpty()) {
			System.out.println("Nessun dato disponibile per la statistica (conteggio per ruolo).");
			return;
		}

		// 2) Converto in righe stampabili/salvabili
		List<String[]> reportRows = new ArrayList<>();
		reportRows.add(new String[] { "RUOLO", "CONTEGGIO" }); // header per la stampa (non è header aziendale)

		int total = 0;
		for (Map.Entry<String, Integer> e : counts.entrySet()) {
			reportRows.add(new String[] { e.getKey(), String.valueOf(e.getValue()) });
			total += e.getValue();
		}

		System.out.println("Totale righe valide conteggiate: " + total);
		System.out.println("------------------------------------------");

		// stampo il report (uso la tua printRows)
		ManageCsvFile.printRows(reportRows);

		// 3) Controllo livello accesso (stesso stile)
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la statistica (access level < 2).");
			return;
		}

		// 4) Chiedo se salvare
		// NB: askAndSaveResult salva STANDARD.csv con header del documento aziendale
		// (per te includeHeader=true).
		// Qui NON vogliamo quell'header, quindi passiamo includeHeader=false e salviamo
		// solo reportRows (senza header finto)
		// -> rimuovo la prima riga (RUOLO,CONTEGGIO) e salvo solo i dati.
		List<String[]> onlyData = reportRows.subList(1, reportRows.size());
		askAndSaveResult(ManagerSession.getCurrent(), onlyData);
	}

	public static void statsAndMaybeSavePercentByRole() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

		// 1) Calcolo statistica
		Map<String, Double> perc = StatisticResearch.percentEmployeesByRole();

		if (perc == null || perc.isEmpty()) {
			System.out.println("Nessun dato disponibile per la statistica (percentuali per ruolo).");
			return;
		}

		// 2) Preparo righe report (stampabili/salvabili)
		List<String[]> reportRows = new ArrayList<>();
		reportRows.add(new String[] { "RUOLO", "PERCENTUALE" });

		for (Map.Entry<String, Double> e : perc.entrySet()) {
			// formato con 2 decimali (usa punto decimale per CSV)
			String p = String.format(Locale.US, "%.2f%%", e.getValue());
			reportRows.add(new String[] { e.getKey(), p });
		}

		System.out.println("==========================================");
		System.out.println("        STATISTICA: % DIPENDENTI PER RUOLO");
		System.out.println("==========================================");
		System.out.println("------------------------------------------");

		ManageCsvFile.printRows(reportRows);

		// 3) Controllo permessi
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la statistica (access level < 2).");
			return;
		}

		// 4) Salvataggio (solo dati, senza header finto)
		List<String[]> onlyData = reportRows.subList(1, reportRows.size());
		askAndSaveResult(ManagerSession.getCurrent(), onlyData);
	}

	public static void statsAndMaybeSaveMinMaxAnnoInizio() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

		int[] mm = StatisticResearch.minMaxAnnoInizio();

		if (mm == null) {
			System.out.println("Nessun anno di inizio valido trovato nel documento aziendale.");
			return;
		}

		int min = mm[0];
		int max = mm[1];

		System.out.println("==========================================");
		System.out.println("      STATISTICA: ANNO INIZIO MIN/MAX     ");
		System.out.println("==========================================");
		System.out.println("Anno minimo: " + min);
		System.out.println("Anno massimo: " + max);
		System.out.println("------------------------------------------");

		// preparo righe salvabili
		List<String[]> reportRows = new ArrayList<>();
		reportRows.add(new String[] { "ANNO_MIN", "ANNO_MAX" });
		reportRows.add(new String[] { String.valueOf(min), String.valueOf(max) });

		// stampa stile tuo
		ManageCsvFile.printRows(reportRows);

		// permessi
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la statistica (access level < 2).");
			return;
		}

		// salvo solo i dati (senza header finto)
		List<String[]> onlyData = reportRows.subList(1, reportRows.size());
		askAndSaveResult(ManagerSession.getCurrent(), onlyData);
	}
	
	public static void statsAndMaybeSaveAverageAnnoInizio() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

		double[] res = StatisticResearch.averageAnnoInizioAndSeniority();

		if (res == null) {
			System.out.println("Nessun anno di inizio valido trovato per calcolare la media.");
			return;
		}

		double mediaAnno = res[0];
		double anzianitaMedia = res[1];

		System.out.println("==========================================");
		System.out.println("   STATISTICA: MEDIA ANNO INIZIO / ANZIANITA");
		System.out.println("==========================================");
		System.out.println("Media anno di inizio: " + String.format(Locale.US, "%.2f", mediaAnno));
		System.out.println("Anzianità media (anni): " + String.format(Locale.US, "%.2f", anzianitaMedia));
		System.out.println("------------------------------------------");

		// preparo righe salvabili + stampabili
		List<String[]> reportRows = new ArrayList<>();
		reportRows.add(new String[] { "MEDIA_ANNO_INIZIO", "ANZIANITA_MEDIA" });
		reportRows.add(new String[] {
				String.format(Locale.US, "%.2f", mediaAnno),
				String.format(Locale.US, "%.2f", anzianitaMedia)
		});

		ManageCsvFile.printRows(reportRows);

		// permessi
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la statistica (access level < 2).");
			return;
		}

		// salvo solo i dati (senza header finto)
		List<String[]> onlyData = reportRows.subList(1, reportRows.size());
		askAndSaveResult(ManagerSession.getCurrent(), onlyData);
	}

	public static void statsAndMaybeSaveDistributionByStartYear() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

		Map<Integer, Integer> dist = StatisticResearch.countEmployeesByStartYear();

		if (dist == null || dist.isEmpty()) {
			System.out.println("Nessun dato disponibile per la distribuzione per anno di inizio.");
			return;
		}

		System.out.println("==========================================");
		System.out.println("   STATISTICA: DIPENDENTI PER ANNO INIZIO ");
		System.out.println("==========================================");

		// preparo righe per stampa/salvataggio
		List<String[]> reportRows = new ArrayList<>();
		reportRows.add(new String[] { "ANNO_INIZIO", "CONTEGGIO" });

		int total = 0;
		for (Map.Entry<Integer, Integer> e : dist.entrySet()) {
			reportRows.add(new String[] { String.valueOf(e.getKey()), String.valueOf(e.getValue()) });
			total += e.getValue();
		}

		System.out.println("Totale righe conteggiate: " + total);
		System.out.println("------------------------------------------");

		ManageCsvFile.printRows(reportRows);

		// permessi
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la statistica (access level < 2).");
			return;
		}

		// salvo solo i dati
		List<String[]> onlyData = reportRows.subList(1, reportRows.size());
		askAndSaveResult(ManagerSession.getCurrent(), onlyData);
	}
	
	public static void statsAndMaybeSaveRichiamiSummary() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

		double[] s = StatisticResearch.statsRichiami();

		if (s == null) {
			System.out.println("Nessun dato valido sui richiami trovato.");
			return;
		}

		double totale = s[0];
		double media = s[1];
		int min = (int) s[2];
		int max = (int) s[3];

		System.out.println("==========================================");
		System.out.println("     STATISTICA: RICHIAMI (TOT/MEDIA/MIN/MAX)");
		System.out.println("==========================================");
		System.out.println("Totale richiami: " + String.format(Locale.US, "%.0f", totale));
		System.out.println("Media richiami:  " + String.format(Locale.US, "%.2f", media));
		System.out.println("Min richiami:    " + min);
		System.out.println("Max richiami:    " + max);
		System.out.println("------------------------------------------");

		List<String[]> reportRows = new ArrayList<>();
		reportRows.add(new String[] { "TOTALE", "MEDIA", "MIN", "MAX" });
		reportRows.add(new String[] {
				String.format(Locale.US, "%.0f", totale),
				String.format(Locale.US, "%.2f", media),
				String.valueOf(min),
				String.valueOf(max)
		});

		ManageCsvFile.printRows(reportRows);

		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la statistica (access level < 2).");
			return;
		}

		List<String[]> onlyData = reportRows.subList(1, reportRows.size());
		askAndSaveResult(ManagerSession.getCurrent(), onlyData);
	}
	
	public static void statsAndMaybeSaveTop5Richiami() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

		List<String[]> top = StatisticResearch.topEmployeesByRichiami(5);

		if (top == null || top.isEmpty()) {
			System.out.println("Nessun dato valido per calcolare la Top 5 richiami.");
			return;
		}

		System.out.println("==========================================");
		System.out.println("      STATISTICA: TOP 5 PER RICHIAMI      ");
		System.out.println("==========================================");

		// preparo righe report con header
		List<String[]> reportRows = new ArrayList<>();
		reportRows.add(new String[] { "ID", "NOME", "COGNOME", "RUOLO", "RICHIAMI" });
		reportRows.addAll(top);

		ManageCsvFile.printRows(reportRows);

		// permessi
		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la statistica (access level < 2).");
			return;
		}

		// salvo solo i dati
		List<String[]> onlyData = reportRows.subList(1, reportRows.size());
		askAndSaveResult(ManagerSession.getCurrent(), onlyData);
	}
	
	public static void statsAndMaybeSaveZeroRichiami() throws Exception {
	    Session current = ManagerSession.getCurrent();
	    ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

	    List<String[]> rows = StatisticResearch.employeesWithZeroRichiami();

	    if (rows == null || rows.isEmpty()) {
	        System.out.println("Nessun dipendente con 0 richiami trovato.");
	        return;
	    }

	    System.out.println("==========================================");
	    System.out.println("     STATISTICA: DIPENDENTI CON 0 RICHIAMI");
	    System.out.println("==========================================");
	    System.out.println("Totale: " + rows.size());
	    System.out.println("------------------------------------------");

	    List<String[]> reportRows = new ArrayList<>();
	    reportRows.add(new String[] { "ID", "NOME", "COGNOME", "RUOLO", "RICHIAMI" });
	    reportRows.addAll(rows);

	    ManageCsvFile.printRows(reportRows);

	    if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
	        System.out.println("Non hai i permessi per salvare la statistica (access level < 2).");
	        return;
	    }

	    List<String[]> onlyData = reportRows.subList(1, reportRows.size());
	    askAndSaveResult(ManagerSession.getCurrent(), onlyData);
	}
	
	public static void statsAndMaybeSaveMissingFields() throws Exception {
		Session current = ManagerSession.getCurrent();
		ActionOnCsv.getIstnce().saveOneFileCsvFromData(CsvStandard.DOCUMENTO_AZIENDALE.toString());

		Map<String, Integer> miss = StatisticResearch.countMissingFields();

		if (miss == null || miss.isEmpty()) {
			System.out.println("Nessun dato disponibile per il controllo qualità dati.");
			return;
		}

		System.out.println("==========================================");
		System.out.println("      STATISTICA: QUALITA' DATI (MISSING) ");
		System.out.println("==========================================");

		List<String[]> reportRows = new ArrayList<>();
		reportRows.add(new String[] { "CAMPO", "MANCANTI" });

		for (Map.Entry<String, Integer> e : miss.entrySet()) {
			reportRows.add(new String[] { e.getKey(), String.valueOf(e.getValue()) });
		}

		ManageCsvFile.printRows(reportRows);

		if (current.getAccessLevel() < AccessLevel.AL2.getLevel()) {
			System.out.println("Non hai i permessi per salvare la statistica (access level < 2).");
			return;
		}

		List<String[]> onlyData = reportRows.subList(1, reportRows.size());
		askAndSaveResult(ManagerSession.getCurrent(), onlyData);
	}






}
