# SOFTWARE MAINTENANCE

Questo documento descrive in modo completo e approfondito le attività di
**manutenzione del software** svolte durante e dopo lo sviluppo del progetto
**Gestione Dipendenti**, realizzato nell’ambito del corso di
**Ingegneria del Software (AA 2025/26)**.

La manutenzione non è stata intesa come una fase finale isolata, ma come un
**processo continuo**, strettamente collegato alle attività di progettazione,
implementazione e testing.

---

## 1. Obiettivi della manutenzione

Gli obiettivi principali delle attività di manutenzione sono stati:

- migliorare la qualità complessiva del codice;
- ridurre il debito tecnico accumulato durante lo sviluppo;
- aumentare la manutenibilità e la leggibilità del sistema;
- correggere difetti emersi durante il testing;
- adattare il software all’evoluzione dell’architettura.

La manutenzione è stata considerata una parte integrante del ciclo di vita del
software, in linea con quanto discusso durante il corso.

---

## 2. Tipologie di manutenzione applicate

Nel progetto sono state applicate diverse tipologie di manutenzione, spesso in
modo combinato.

### 2.1 Manutenzione correttiva

La manutenzione correttiva ha riguardato:

- correzione di errori logici individuati durante il testing;
- gestione di casi limite non previsti inizialmente;
- correzione di comportamenti incoerenti tra moduli.

Questi interventi sono stati effettuati principalmente:
- a seguito dell’esecuzione dei test JUnit;
- durante l’integrazione dei moduli;
- in fase di validazione finale del sistema.

---

### 2.2 Manutenzione migliorativa

La manutenzione migliorativa ha avuto un ruolo centrale nel progetto ed è stata
svolta principalmente tramite **attività di refactoring**.

Gli obiettivi della manutenzione migliorativa sono stati:
- migliorare la struttura interna del codice;
- ridurre la complessità dei moduli;
- chiarire le responsabilità delle classi;
- migliorare l’allineamento con il design architetturale.

---

### 2.3 Manutenzione adattiva

Alcune attività di manutenzione sono state di tipo **adattivo**, in quanto
necessarie per:

- adattare il codice all’introduzione di Spring Boot;
- integrare la GUI a falso terminale;
- modificare la struttura dei package in seguito all’evoluzione del progetto.

Queste modifiche non hanno introdotto nuove funzionalità, ma hanno permesso al
sistema di adattarsi a un contesto tecnico più complesso.

---

## 3. Refactoring strutturale del codice

### 3.1 Riorganizzazione dei package

Una delle principali attività di manutenzione ha riguardato la
**riorganizzazione dei package**.

In particolare:
- sono stati separati i package di dominio da quelli applicativi;
- è stata migliorata la distinzione tra logica di business e persistenza;
- sono stati ridotti riferimenti incrociati non necessari.

Questa riorganizzazione ha contribuito a:
- migliorare la leggibilità del progetto;
- facilitare il testing per package;
- rendere più chiara l’architettura complessiva.

---

### 3.2 Miglioramento della separazione Domain / Application

Durante la manutenzione si è cercato di mitigare i problemi emersi nella
separazione tra **domain layer** e **application layer**.

Le attività svolte includono:
- spostamento di logica impropriamente collocata nel dominio;
- riduzione delle dipendenze dirette tra livelli;
- introduzione di metodi di servizio più chiari.

Nonostante questi interventi, non è stato possibile ottenere una separazione
perfettamente pulita in ogni punto, a causa della struttura ormai consolidata
del codice.

Questa limitazione è stata accettata come compromesso realistico.

---

## 4. Manutenzione guidata dal testing

Il testing ha svolto un ruolo fondamentale nel guidare le attività di
manutenzione.

In particolare:
- i test hanno evidenziato metodi troppo complessi;
- alcuni moduli sono risultati difficili da testare in isolamento;
- il testing ha reso visibili accoppiamenti nascosti.

A seguito di questi risultati, sono stati effettuati:
- refactoring mirati;
- semplificazioni di metodi;
- suddivisione di operazioni troppo complesse.

Questo approccio conferma il ruolo del testing come strumento di
**validazione architetturale**, non solo di verifica funzionale.

---

## 5. Manutenzione della gestione dei dati

La gestione dei dati e del database ha richiesto interventi di manutenzione
specifici.

Le attività principali includono:
- miglioramento della gestione delle connessioni;
- consolidamento dell’uso del pattern Singleton per il database;
- riduzione di accessi diretti non controllati ai dati.

Questi interventi hanno aumentato:
- la stabilità del sistema;
- la prevedibilità dei comportamenti;
- la sicurezza complessiva della gestione dei dati.

---

## 6. Manutenzione della sicurezza

Durante le attività di manutenzione è stata rivista la gestione della sicurezza,
in particolare per quanto riguarda:

- hashing delle password;
- gestione degli identificativi utente;
- accesso ai dati sensibili.

È stata individuata una criticità nella gestione uniforme dei controlli di
sicurezza, che non è stata completamente risolta, ma è stata:

- compresa;
- documentata;
- identificata come possibile miglioramento futuro.

---

## 7. Manutenzione della GUI

La GUI a falso terminale ha richiesto attività di manutenzione specifiche,
legate principalmente a:

- miglioramento della chiarezza dell’interazione;
- gestione più ordinata dei flussi di input/output;
- integrazione stabile con il `main` originale tramite il ponte.

La manutenzione della GUI si è concentrata sulla **stabilità e coerenza**,
piuttosto che sull’introduzione di nuove funzionalità.

---

## 8. Tracciabilità delle attività di manutenzione

Le attività di manutenzione sono tracciabili tramite:

- commit Git descrittivi;
- modifiche incrementali;
- riorganizzazione progressiva del codice.

Questo consente di ricostruire:
- l’evoluzione del progetto;
- le motivazioni delle modifiche;
- l’impatto delle attività di manutenzione.

---

## 9. Lezioni apprese

Le attività di manutenzione hanno evidenziato diversi aspetti rilevanti:

- l’importanza di una buona separazione architetturale fin dall’inizio;
- il ruolo centrale del testing nel miglioramento del codice;
- la difficoltà di integrare nuovi framework in progetti esistenti;
- la necessità di accettare compromessi realistici.

Queste lezioni rappresentano uno degli aspetti più significativi del progetto
dal punto di vista formativo.

---

## 10. Conclusioni

La manutenzione del progetto **Gestione Dipendenti** non è stata un’attività
secondaria, ma una parte essenziale del ciclo di vita del software.

Le attività svolte dimostrano:
- attenzione alla qualità del codice;
- capacità di analisi critica;
- comprensione dei concetti di manutenzione software.

Questo documento conclude la descrizione delle attività di manutenzione e si
integra con gli altri file di progetto, fornendo una visione completa e
realistica del lavoro svolto.
