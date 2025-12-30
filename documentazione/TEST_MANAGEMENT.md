# Software Testing Specification (STP)
## Gestione Dipendenti

Questo documento descrive le attività di **Software Testing** svolte per il
progetto **Gestione Dipendenti**, sviluppato nell’ambito del corso di
Ingegneria del Software (AA 2025/26).

Il testing è stato progettato, realizzato ed eseguito come **fase conclusiva
del progetto**, con l’obiettivo di verificare la correttezza funzionale,
l’integrazione dei componenti e la qualità complessiva del software.

---

## 1. Obiettivi del testing

Le attività di testing hanno avuto i seguenti obiettivi:

- verificare la corretta implementazione dei requisiti definiti nella SRC;
- individuare e correggere errori logici e di integrazione;
- validare il comportamento del sistema in condizioni normali e limite;
- garantire stabilità, affidabilità e coerenza del software;
- supportare la manutenzione e il refactoring finale del codice.

Il testing rappresenta la **fase di validazione finale** del progetto,
in coerenza con il modello di sviluppo adottato.

---

## 2. Inquadramento metodologico

Il progetto segue un **modello di sviluppo ibrido** che combina:

- approccio a cascata per analisi e progettazione;
- **V-Model** per verifica e validazione;
- iterazioni Scrum per lo sviluppo incrementale.

In questo contesto, il testing è stato eseguito **dopo l’implementazione delle
funzionalità**, secondo una logica di verifica strutturata e controllata.

Il flusso adottato è stato:

1. progettazione delle classi;
2. implementazione del codice;
3. definizione dei casi di test;
4. esecuzione dei test;
5. analisi dei risultati;
6. correzione e refactoring;
7. riesecuzione dei test.

---

## 3. Tipologie di testing effettuate

### 3.1 Unit Testing

Il **testing di unità** è stato utilizzato per verificare il corretto
funzionamento dei singoli metodi e delle singole classi.

Caratteristiche principali:
- utilizzo di **JUnit**;
- test focalizzati su una sola unità alla volta;
- verifica di casi normali e casi limite;
- isolamento della logica di business.

Le classi testate includono:
- entità di dominio;
- servizi applicativi;
- componenti di utilità.

---

### 3.2 Integration Testing

Il **testing di integrazione** ha verificato la corretta interazione tra:

- service layer e data layer;
- logica applicativa e database;
- componenti che cooperano nello stesso flusso funzionale.

Questa fase ha permesso di individuare errori non rilevabili tramite unit test
isolati.

---

### 3.3 System Testing

Il **testing di sistema** ha validato il comportamento complessivo
dell’applicazione, simulando l’utilizzo reale del sistema.

Sono stati verificati:
- flussi completi di utilizzo;
- controlli di accesso;
- gestione degli errori;
- coerenza dei risultati prodotti.

---

### 3.4 Acceptance Testing

Il **testing di accettazione** ha verificato la conformità del sistema ai
requisiti funzionali definiti nella SRC.

Ogni requisito è stato validato tramite uno o più casi di test,
garantendo che il sistema soddisfi le aspettative previste.

---

## 4. Organizzazione dei package di test

I test sono organizzati seguendo una struttura coerente con quella del codice
applicativo, al fine di migliorare leggibilità, manutenibilità e tracciabilità.

---

## 5. Strategia di testing del database

Il testing della componente di persistenza è stato progettato e realizzato come
parte integrante del processo di validazione finale del sistema.

Data la centralità del database nel progetto, è stata adottata una strategia
di testing mirata a garantire **consistenza dei dati, affidabilità delle
operazioni e ripetibilità dei test**.

### 5.1 Isolamento dell’ambiente di test

Tutti i test che coinvolgono il database sono eseguiti in un **ambiente di test
dedicato**, separato dal contesto di utilizzo principale del sistema.

Questo approccio consente di:
- evitare interferenze tra dati di test e dati applicativi;
- garantire risultati deterministici;
- prevenire effetti collaterali indesiderati.

Lo stato iniziale del database è noto e controllato prima dell’esecuzione
dei test.

---

### 5.2 Gestione della connessione tramite Singleton

La connessione al database nei test è gestita tramite il **pattern Singleton**.

Questa scelta progettuale consente di:
- garantire un unico punto di accesso alla risorsa database;
- evitare la creazione non controllata di connessioni multiple;
- mantenere coerenza tra i test che operano sullo stesso contesto dati.

L’utilizzo del Singleton migliora inoltre:
- la stabilità dell’esecuzione dei test;
- la ripetibilità;
- il controllo delle risorse.

---

### 5.3 Controllo dello stato del database

Prima dell’esecuzione dei test:
- il database viene inizializzato con un insieme noto di dati;
- vengono verificate le condizioni di partenza attese.

Al termine dei test:
- lo stato del database viene ripristinato o ripulito;
- nessuna modifica introdotta dai test persiste oltre l’esecuzione.

Questo garantisce:
- indipendenza tra i casi di test;
- prevedibilità dei risultati;
- maggiore facilità di analisi in caso di errore.

---

## 6. Testing e approccio sequenziale controllato

Il testing è stato eseguito seguendo una logica **sequenziale e controllata**,
coerente con il modello a cascata e con il **V-Model** definito nel Project Plan.

Il flusso adottato è stato il seguente:

1. progettazione delle classi;
2. implementazione dei metodi;
3. definizione dei casi di test;
4. esecuzione dei test sui metodi;
5. analisi dei risultati;
6. modifica o refactoring del codice;
7. riesecuzione dei test.

Questo approccio consente di:
- verificare il comportamento del codice dopo la sua implementazione;
- individuare errori logici in modo sistematico;
- migliorare progressivamente la qualità del software.

Il testing è utilizzato come **strumento di verifica formale**, non come fase
esplorativa.

---

## 7. Integrazione del testing con la struttura a package

La struttura dei test riflette direttamente la struttura del codice
applicativo, in coerenza con l’architettura del sistema.

In particolare:
- il **domain layer** è testato tramite unit test mirati;
- l’**application layer** è testato tramite test di servizio;
- il livello di persistenza è testato tramite test specifici sul database;
- l’interazione complessiva è verificata tramite integration e system test.

Questa organizzazione consente di:
- rispettare la separazione delle responsabilità;
- individuare rapidamente il livello in cui si manifesta un errore;
- mantenere i test semplici, leggibili e manutenibili.

---

## 8. Gestione degli errori e dei casi limite

I casi di test includono:
- scenari di utilizzo standard;
- casi limite;
- input non validi;
- situazioni di errore previste.

Il sistema è stato verificato affinché:
- gli errori siano gestiti in modo controllato;
- non si verifichino stati inconsistenti;
- l’applicazione mantenga stabilità in ogni condizione testata.

---

## 9. Tracciabilità tra requisiti e testing

Ogni requisito funzionale definito nella SRC è associato ad almeno un caso di
test.

La tracciabilità è garantita tramite:
- identificativi coerenti dei requisiti;
- organizzazione dei test per responsabilità;
- collegamento logico tra requisiti, codice e test.

Questo consente di dimostrare in modo chiaro che **tutti i requisiti sono stati
verificati**.

---

## 10. Conclusioni

Le attività di testing confermano che il sistema **Gestione Dipendenti** è
corretto dal punto di vista funzionale, stabile e conforme ai requisiti
definiti.

Il completamento del testing rappresenta la **chiusura tecnica del progetto**,
garantendo un prodotto software completo, verificato e pronto alla consegna.


