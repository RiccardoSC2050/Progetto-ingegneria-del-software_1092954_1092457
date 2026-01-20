package it.unibg.progetto.api.app;

public class ManualHelper {
	
	public static void printMan() {
		
        System.out.println("""
============================================================
                    MANUALE COMANDI - APP
============================================================

PROMPT
  <utente> >   (es: root>)

USO
  Scrivi un comando e premi INVIO.
  Alcuni comandi richiedono di aver effettuato il login.

------------------------------------------------------------
COMANDI GENERALI
------------------------------------------------------------
  man
      Mostra questo manuale.

  clear
      Pulisce lo schermo del terminale.

  exit
      Salva i CSV nel database, pulisce la cartella temporanea e chiude l app.

  out
      Logout dell utente corrente (chiude sessione) e torna al login.

  in -u
      Mostra/gestisce file di altri utenti (in base ai permessi).

------------------------------------------------------------
CSV / FILE
------------------------------------------------------------
  w -c
      Crea un file CSV generale (template) e salva i file nel database.

  r -c
      Legge e stampa un file CSV (con formattazione a tabella).

  ls -f
      Mostra i file CSV dell’utente.

  e -f
      Modifica un file CSV e poi salva i file nel database.

  save
      Salva tutti i file presenti nella cartella nel database.
      (Mostra: File CSV salvati nel database.)

  dlt -c
      Elimina un CSV dal database (relativo all’utente / contesto).

------------------------------------------------------------
UTENTI / PERMESSI (solo ROOT dove indicato)
------------------------------------------------------------
  crt -u
      [ROOT] Crea un nuovo utente.

  dlt -u
      [ROOT] Elimina un utente.

  new -al
      [ROOT] Cambia Access Level di un utente.

  new p
      Cambia password dell’utente corrente.

  show -a -u
      [ROOT] Mostra utenti (modalità neutra).

  show -t -u
      [ROOT] Mostra utenti (modalità affermativa/dettagliata).

------------------------------------------------------------
RICERCA / STATISTICHE
------------------------------------------------------------
  search
      Ricerca su base e possibilità di salvare.

  search -s
      Ricerca statistica (summary).

------------------------------------------------------------
NOTE
------------------------------------------------------------
- Se non esiste una sessione attiva, verrà richiesto il login.
- I comandi non riconosciuti stampano: Comando errato o non esistente.

============================================================
""");
    }

}
