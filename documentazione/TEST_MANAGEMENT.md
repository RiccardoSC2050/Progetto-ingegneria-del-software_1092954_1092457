# Test Management
## Progetto: Gestione Dipendenti

Questo documento descrive in modo completo, dettagliato e consapevole le
attività di **testing del software** svolte per il progetto
**Gestione Dipendenti**, sviluppato nell’ambito del corso di
**Ingegneria del Software (AA 2025/26)**.

Il documento riflette **lo stato finale del progetto**, a valle della completa
implementazione del codice e delle attività di verifica e validazione.
Il testing non è descritto in modo teorico, ma come **attività reale**, condotta
sul codice effettivamente sviluppato.

Il contenuto di questo documento è coerente e integrato con:
- `SRC.md`
- `DESIGN.md`
- `PROJECT_PLAN.md`
- `MAINTENANCE.md`

---

## 1. Ruolo del testing nel progetto

Nel progetto **Gestione Dipendenti**, il testing ha avuto un ruolo centrale e
non marginale.

Non è stato considerato:
- una fase accessoria,
- un semplice adempimento formale,
- una verifica superficiale del funzionamento.

Al contrario, il testing è stato utilizzato come:
- strumento di verifica funzionale;
- mezzo di validazione del design;
- supporto alle attività di refactoring;
- criterio di valutazione della qualità del codice.

Il testing ha accompagnato le fasi finali dello sviluppo ed è stato determinante
per il consolidamento del sistema.

---

## 2. Obiettivi del testing

Gli obiettivi principali delle attività di testing sono stati:

- verificare la corretta implementazione dei requisiti definiti nella SRC;
- validare i flussi funzionali principali del sistema;
- individuare errori logici e di integrazione;
- garantire stabilità e affidabilità del software;
- supportare la manutenzione finale del codice;
- valutare la qualità del design attraverso la testabilità dei moduli.

Il testing ha quindi avuto una duplice funzione:
- **verifica del comportamento**
- **valutazione della struttura interna del software**

---

## 3. Inquadramento metodologico

Il testing è stato condotto all’interno di un **modello di sviluppo ibrido**,
coerente con quanto descritto nel Project Plan.

In particolare:
- le fasi di analisi e progettazione seguono una logica a cascata;
- il modello a **V** è stato utilizzato come riferimento concettuale per la
  verifica e validazione;
- lo sviluppo incrementale ha portato a cicli ripetuti di implementazione,
  testing e refactoring.

Il flusso operativo adottato è stato il seguente:

1. progettazione delle classi;
2. implementazione del codice;
3. definizione dei casi di test;
4. esecuzione dei test;
5. analisi dei risultati;
6. correzione degli errori;
7. refactoring del codice;
8. riesecuzione dei test.

Questo processo è stato ripetuto più volte fino al consolidamento finale.

---

## 4. Tipologie di testing effettuate

### 4.1 Unit Testing

Il **testing di unità** è stato effettuato utilizzando **JUnit**.

L’obiettivo dell’unit testing è stato:
- verificare il comportamento dei singoli metodi;
- testare le classi in isolamento;
- validare i casi normali e i casi limite.

Sono stati testati in modo estensivo:
- modelli di dominio;
- classi di servizio;
- componenti di utilità;
- metodi di gestione dei dati.

L’unit testing ha evidenziato:
- metodi facilmente testabili grazie a buona coesione;
- metodi difficili da testare a causa di complessità eccessiva.

---

### 4.2 Acceptance Testing

Il **testing di accettazione** è stato condotto verificando la conformità del
sistema ai requisiti definiti nella `SRC.md`.

Ogni requisito funzionale è stato validato tramite uno o più casi di test,
garantendo che:
- il sistema soddisfi le funzionalità richieste;
- il comportamento sia coerente con le specifiche.

---

## 5. Organizzazione dei test e struttura dei package

I test sono stati organizzati seguendo una struttura coerente con quella del
codice applicativo.

In particolare:
- ogni package applicativo ha un package di test corrispondente;
- i test riflettono la suddivisione architetturale del sistema;
- la struttura facilita la tracciabilità tra codice e test.

Questa scelta consente:
- maggiore leggibilità;
- migliore manutenibilità;
- individuazione rapida dei problemi.

---

## 6. Strategia di testing del database

Il database rappresenta una componente critica del sistema ed è stato testato
in modo specifico e strutturato.

### 6.1 Isolamento dell’ambiente di test

I test che coinvolgono il database sono eseguiti in un ambiente controllato,
separato dal contesto di utilizzo reale.

Questo consente di:
- evitare interferenze;
- garantire risultati ripetibili;
- controllare lo stato iniziale dei dati.

---

### 6.2 Uso del Singleton per la connessione

La connessione al database nei test è gestita tramite il **pattern Singleton**.

Questa scelta permette di:
- garantire un unico punto di accesso;
- evitare connessioni multiple non controllate;
- mantenere coerenza durante l’esecuzione dei test.

Il Singleton è stato fondamentale per:
- stabilità dei test;
- controllo delle risorse;
- ripetibilità delle esecuzioni.

---

### 6.3 Sequenza controllata dei test

Il testing del database segue una logica **sequenziale controllata**:

1. inizializzazione dei dati;
2. esecuzione delle operazioni;
3. verifica dei risultati;
4. pulizia dello stato finale.

Questo approccio evita effetti collaterali tra i test.

---

## 7. Copertura del testing

Il progetto ha raggiunto una **copertura di test superiore al 75% per ciascun
modulo e package**, escludendo consapevolmente la GUI.

La copertura è stata verificata utilizzando strumenti di analisi della coverage
ed è stata considerata un indicatore di qualità, non un obiettivo puramente
numerico.

Dove la copertura risultava difficile da aumentare, la causa era spesso legata a:
- metodi troppo complessi;
- accoppiamento eccessivo tra componenti;
- logica non facilmente isolabile.

---

## 8. Testing e difficoltà di Function Decomposition e OOP

Durante il testing sono emerse difficoltà legate alla combinazione tra:

- **function decomposition**
- **object-oriented design**

In particolare:
- alcuni moduli presentano un elevato numero di metodi;
- alcuni metodi risultano troppo complessi;
- alcune operazioni combinano più responsabilità.

Queste caratteristiche hanno reso:
- difficile la scrittura di test unitari puri;
- necessario l’uso di test di integrazione più ampi;
- inevitabile l’accettazione di alcuni compromessi.

Queste difficoltà sono state riconosciute e documentate come limiti progettuali.

## 9. Testing della GUI: motivazioni e limiti dichiarati

La **GUI Java a falso terminale** non è stata sottoposta a testing automatico
(JUnit o strumenti di GUI testing).

Questa scelta non è stata dovuta a negligenza o sottovalutazione del problema,
ma è il risultato di una **valutazione consapevole**, basata su vincoli reali e
priorità accademiche.

### 9.1 Motivazioni della mancata automazione dei test GUI

Le principali motivazioni sono state:

- elevato costo temporale per apprendere strumenti di GUI testing avanzati;
- complessità intrinseca del testing automatico di interfacce grafiche;
- necessità di concentrarsi sulla preparazione di altri esami universitari;
- priorità data al testing della logica di business, oggetto principale del corso.

Il testing automatico della GUI avrebbe richiesto:
- strumenti dedicati;
- ulteriore configurazione del progetto;
- un tempo non compatibile con le scadenze accademiche.

---

### 9.2 Validazione funzionale manuale della GUI

Nonostante l’assenza di test automatici, la GUI è stata:

- testata manualmente;
- verificata in diversi scenari di utilizzo;
- validata dal punto di vista funzionale.

In particolare è stato verificato che:
- i flussi di input/output siano coerenti;
- il comportamento della GUI rifletta quello del main originale;
- non vi siano duplicazioni di logica di business;
- il sistema risponda correttamente alle azioni dell’utente.

---

### 9.3 Relazione tra GUI e architettura del sistema

Dal punto di vista architetturale, la GUI:

- non contiene logica di business;
- agisce esclusivamente come livello di presentazione;
- comunica con il sistema tramite un controller e un ponte verso il main.

Grazie a questa separazione:
- l’assenza di test sulla GUI non compromette la correttezza del sistema;
- la parte critica dell’applicazione è comunque ampiamente testata;
- il rischio complessivo rimane contenuto.

---

## 10. Uso del testing come guida al refactoring

Il testing ha avuto un ruolo fondamentale nel guidare le attività di
**refactoring del codice**.

Durante l’esecuzione dei test sono emerse diverse situazioni che hanno richiesto
interventi mirati, tra cui:

- metodi con responsabilità multiple;
- dipendenze non evidenti;
- accoppiamenti eccessivi tra classi;
- gestione non ottimale delle eccezioni.

In risposta a questi problemi sono stati effettuati:
- semplificazioni di metodi;
- riorganizzazione delle classi;
- separazione parziale delle responsabilità;
- miglioramento della leggibilità del codice.

Il testing ha quindi agito come:
> strumento di diagnosi della qualità architetturale del sistema.

---

## 11. Gestione dei casi limite e degli errori

Una parte significativa del testing è stata dedicata alla verifica dei
**casi limite** e della **gestione degli errori**.

Sono stati testati, ove possibile:
- input non validi;
- condizioni di errore del database;
- file CSV malformati;
- dati mancanti o incompleti;
- situazioni di stato non previsto.

Questi test hanno permesso di:
- migliorare la robustezza del sistema;
- individuare errori non evidenti;
- rafforzare la gestione delle eccezioni.

---

## 12. Supporto del vibe coding nelle attività di testing

Nel contesto del testing, è stato utilizzato il **vibe coding** come strumento
di supporto, in modo controllato e consapevole.

Il suo utilizzo ha riguardato in particolare:
- definizione di strutture di test complesse;
- gestione di casi particolari (file CSV, byte array);
- comprensione di pattern di testing;
- supporto nella configurazione dei test per il database.

È importante sottolineare che:
- il vibe coding non ha sostituito la progettazione dei test;
- i casi di test sono stati definiti e compresi dal team;
- il supporto è stato utilizzato per chiarire e non per delegare.

Si stima che circa **il 60% dei test** sia stato supportato da strumenti di
vibe coding, principalmente per casi tecnici specifici.

---

## 13. Tracciabilità tra requisiti e test

Le attività di testing sono state condotte mantenendo una chiara relazione con
i requisiti definiti nella `SRC.md`.

Per ogni requisito funzionale:
- sono stati identificati uno o più casi di test;
- è stato verificato il comportamento atteso;
- sono stati analizzati eventuali scostamenti.

Questa tracciabilità consente di:
- dimostrare la copertura dei requisiti;
- garantire la conformità del sistema;
- facilitare eventuali verifiche future.

---

## 14. Valutazione complessiva del testing

Il testing del progetto **Gestione Dipendenti** può essere valutato come
**ampiamente soddisfacente** in relazione agli obiettivi del corso.

### Punti di forza
- copertura superiore al 75% per ogni modulo;
- testing esteso della logica applicativa;
- testing del database strutturato;
- uso del testing come guida al refactoring;
- documentazione trasparente delle scelte.

### Limiti riconosciuti
- assenza di testing automatico sulla GUI;
- difficoltà nel testing di metodi complessi;
- compromessi dovuti alla combinazione FD / OOP.

Questi limiti sono stati:
- riconosciuti;
- motivati;
- documentati.

---

## 15. Conclusioni sul testing

Le attività di testing svolte dimostrano:

- attenzione alla qualità del software;
- capacità di analisi critica del codice;
- comprensione dei limiti progettuali;
- applicazione pratica dei concetti studiati.

Il testing non è stato perfetto, ma è stato:
- realistico;
- consapevole;
- coerente con il contesto accademico;
- adeguato alla complessità del progetto.

Questo documento conclude la descrizione delle attività di testing e si integra
con la restante documentazione fornendo una visione completa e difendibile del
lavoro svolto.
