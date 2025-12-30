# Design and Architecture
## Gestione Dipendenti

Questo documento descrive le **scelte di design e architettura software**
adottate per il progetto **Gestione Dipendenti**, sviluppato nell’ambito del
corso di **Ingegneria del Software (AA 2025/26)**.

Il documento ha lo scopo di:
- descrivere l’architettura complessiva del sistema;
- motivare le principali scelte progettuali;
- evidenziare i principi di ingegneria del software adottati;
- analizzare in modo critico le difficoltà incontrate e i compromessi effettuati.

Il contenuto è coerente con quanto definito in:
- `PROJECT_PLAN.md`
- `SRC.md`
- `SCRUM_MANAGEMENT.md`
- `TEST_MANAGEMENT.md`
- `MAINTENANCE.md`

---

## 1. Visione architetturale generale

Il sistema **Gestione Dipendenti** è stato progettato seguendo un’architettura
modulare, ispirata a una suddivisione a livelli, con l’obiettivo di:

- separare le responsabilità;
- ridurre la complessità;
- favorire la manutenibilità;
- migliorare la leggibilità del codice.

Fin dalle prime fasi di progettazione si è cercato di distinguere in modo chiaro
tra:
- **dominio applicativo (domain)**;
- **logica applicativa (application)**;
- **livello di persistenza e infrastruttura (data / infrastructure)**;
- **livello di presentazione (CLI / GUI a falso terminale)**.

Questa suddivisione è coerente con i principi discussi nel corso e con le
architetture moderne a livelli.

---

## 2. Separazione dei componenti software

### 2.1 Obiettivo iniziale

L’obiettivo iniziale era mantenere una **separazione netta** tra i componenti
software, in particolare tra:

- **Domain layer**: entità e logica di business;
- **Application layer**: orchestrazione dei casi d’uso;
- **Infrastructure layer**: database, persistenza, configurazione;
- **Presentation layer**: interazione con l’utente.

Questa separazione era pensata per:
- migliorare la testabilità;
- ridurre l’accoppiamento;
- favorire il riuso del dominio;
- supportare una crescita futura del sistema.

---

### 2.2 Problemi incontrati nella separazione Domain / Application

Durante lo sviluppo è emerso che la **quantità di funzionalità** e la
complessità crescente del sistema hanno reso difficile mantenere una
separazione perfettamente pulita tra **domain** e **application**.

In particolare:
- alcune responsabilità sono risultate difficili da collocare;
- parte della logica di business è stata talvolta richiamata direttamente
  dall’application layer;
- il dominio è risultato, in alcuni casi, dipendente da decisioni applicative.

Questo ha creato:
- **accoppiamento eccessivo** tra domain e application;
- difficoltà tecniche durante la fase di testing;
- maggiore complessità nella gestione delle dipendenze.

Questi problemi non derivano da una mancanza di progettazione, ma da un
tentativo ambizioso di applicare principi architetturali avanzati in un
progetto di dimensione accademica.

---

## 3. Function Decomposition e Object-Oriented Design

### 3.1 Approccio seguito

Il progetto ha cercato di combinare due approcci:
- **Function Decomposition**, per scomporre il problema in operazioni chiare;
- **Object-Oriented Design**, per modellare il dominio in termini di entità e
  responsabilità.

L’intento era:
- ridurre la complessità tramite decomposizione;
- assegnare responsabilità chiare agli oggetti;
- mantenere coesione all’interno dei moduli.

---

### 3.2 Limiti riscontrati

Nonostante l’impegno, il risultato ottenuto non è stato **completamente stabile**.

In particolare:
- alcune classi hanno accumulato più responsabilità del previsto;
- la decomposizione funzionale ha talvolta prevalso su una modellazione
  puramente orientata agli oggetti;
- il confine tra funzioni applicative e comportamento del dominio è risultato
  meno netto del desiderato.

Questo ha contribuito allo squilibrio già descritto tra domain e application,
rendendo necessarie successive attività di refactoring.

---

## 4. Modularità, coesione e accoppiamento

### 4.1 Modularità e coesione

Fin dall’inizio si è cercato di progettare **moduli coesi**, ovvero componenti
con responsabilità ben definite e internamente consistenti.

Sono stati applicati i seguenti principi:
- **Single Responsibility Principle**;
- separazione per package logici;
- riduzione delle dipendenze dirette.

---

### 4.2 Accoppiamento eccessivo

Nonostante l’attenzione alla coesione, si è verificato un **accoppiamento
eccessivo** tra alcuni moduli, in particolare tra domain e application.

Questo accoppiamento ha causato:
- maggiore fragilità del sistema;
- difficoltà nella scrittura dei test;
- necessità di refactoring durante le fasi finali.

L’esperienza ha evidenziato come la modularità richieda non solo buone
intenzioni progettuali, ma anche una forte disciplina nella gestione delle
dipendenze.

---

## 5. Sicurezza e gestione delle credenziali

### 5.1 Obiettivi di sicurezza

La sicurezza del sistema è stata affrontata con l’obiettivo di:
- proteggere le informazioni sensibili degli utenti;
- limitare l’accesso non autorizzato alle funzionalità;
- ridurre l’esposizione diretta dei dati critici;
- garantire un livello di sicurezza coerente con il contesto del progetto.

Pur trattandosi di un progetto accademico, è stato perseguito un approccio
il più possibile vicino a quello professionale.

---

### 5.2 Scelte adottate

Le principali scelte progettuali in ambito di sicurezza includono:

- utilizzo di **hashing delle password**;
- generazione di **UUID** per l’identificazione univoca degli utenti;
- definizione di una password minima di **8 caratteri**;
- memorizzazione delle informazioni sensibili esclusivamente nel database;
- riduzione dell’accessibilità diretta ai dati critici.

L’idea di fondo è stata quella di applicare il principio di **information hiding**,
limitando la visibilità e l’uso delle informazioni sensibili ai soli componenti
strettamente necessari.

---

### 5.3 Criticità individuate

Durante l’analisi finale dell’architettura è emersa una criticità importante
legata alla gestione delle password.

In particolare:
- non tutti i metodi applicativi richiamano in modo uniforme il meccanismo di
  verifica della password;
- alcune operazioni accedono ai dati senza passare sempre dallo stesso livello
  di controllo;
- questa incoerenza può introdurre **potenziali rischi di sicurezza**, anche
  se le password risultano hashate.

Questa problematica evidenzia un limite architetturale e sottolinea l’importanza
di:
- centralizzare i controlli di sicurezza;
- evitare bypass involontari dei meccanismi di autenticazione;
- rafforzare la separazione delle responsabilità.

La criticità è stata identificata come **area di miglioramento futura**.

---

## 6. Architettura Spring Boot e configurazione applicativa

### 6.1 Scelte architetturali

Il progetto è basato su **Spring Boot**, utilizzato come framework principale
per la gestione dell’applicazione.

Spring Boot è stato scelto per:
- semplificare la configurazione dell’applicazione;
- gestire in modo strutturato il ciclo di vita dei componenti;
- supportare l’integrazione con JPA/Hibernate;
- favorire una configurazione coerente e centralizzata.

L’applicazione è configurata secondo pratiche standard:
- uso di file di configurazione dedicati;
- gestione dei bean tramite dependency injection;
- separazione tra configurazione e logica applicativa.

---

### 6.2 Assenza di interfaccia web e API REST

Il progetto **non include un’interfaccia web** e non sfrutta direttamente
controller REST per l’esposizione di API HTTP.

Questa scelta è stata deliberata e motivata da:
- focus sul dominio e sulla logica applicativa;
- riduzione della complessità tecnologica;
- volontà di concentrare lo sforzo progettuale sull’architettura interna.

L’assenza di una web UI non compromette la struttura dell’applicazione, che
rimane comunque predisposta per una futura estensione verso servizi REST.

---

## 7. Interfaccia utente e GUI a falso terminale

### 7.1 Scelte progettuali

L’interfaccia utente è stata realizzata tramite una **GUI a falso terminale**,
pensata per simulare un’interazione strutturata senza introdurre una vera
interfaccia grafica web.

Questa soluzione consente di:
- mantenere il controllo completo sul flusso applicativo;
- separare la presentazione dalla logica di business;
- ridurre la dipendenza da tecnologie front-end.

---

### 7.2 Pattern Model–View–Controller (MVC)

La GUI a falso terminale è organizzata secondo il pattern
**Model–View–Controller (MVC)**:

- **Model**: rappresenta il dominio e la logica di business;
- **View**: rappresenta il terminale simulato e la presentazione dei dati;
- **Controller**: gestisce l’interazione tra utente e sistema.

Questa struttura consente di:
- isolare la logica di presentazione;
- migliorare la manutenibilità;
- facilitare eventuali estensioni future.

---

### 7.3 Ponte con il main applicativo

Per integrare la GUI con il sistema esistente, è stato realizzato un **ponte**
che consente di sfruttare il *main* originale dell’applicazione.

Questo approccio permette di:
- riutilizzare la logica già sviluppata;
- evitare duplicazioni di codice;
- mantenere un punto di ingresso unico per l’applicazione.

La GUI agisce quindi come livello di presentazione aggiuntivo, senza alterare
la struttura fondamentale del sistema.

---

## 8. Information Hiding e riduzione della complessità

Un principio guida durante lo sviluppo del progetto è stato quello
dell’**information hiding**, con l’obiettivo di ridurre la complessità
complessiva del sistema e limitare la propagazione delle dipendenze.

In particolare, si è cercato di:
- nascondere i dettagli implementativi dietro interfacce e servizi;
- limitare l’accesso diretto ai dati persistenti;
- evitare che i livelli superiori dipendessero da dettagli interni dei livelli
  inferiori.

Questo approccio ha contribuito a:
- migliorare la leggibilità del codice;
- facilitare il testing;
- rendere il sistema più comprensibile e manutenibile.

Tuttavia, l’introduzione di nuove tecnologie come **Spring Boot** e la gestione
di una GUI personalizzata hanno richiesto un periodo di adattamento, durante il
quale non sempre è stato possibile applicare l’information hiding in modo
rigoroso e uniforme.

---

## 9. Modellazione UML e relazione con il codice

La modellazione UML è stata utilizzata come strumento di supporto alla
progettazione e alla comprensione del sistema.

I diagrammi UML realizzati includono:
- diagrammi dei casi d’uso;
- diagrammi delle classi;
- diagrammi di sequenza;
- diagrammi di attività;
- diagrammi di stato;
- diagrammi dei componenti e dei package.

L’UML è stato utilizzato con i seguenti obiettivi:
- comprendere e discutere la struttura del sistema;
- supportare le scelte di design;
- documentare le principali relazioni tra i componenti.

Durante lo sviluppo, alcuni diagrammi sono stati aggiornati per riflettere
l’evoluzione del codice, evidenziando come la progettazione e
l’implementazione siano avvenute in modo iterativo.

---

## 10. Allineamento tra architettura, testing e manutenzione

Le scelte architetturali sono state strettamente collegate alle attività di
testing e manutenzione.

In particolare:
- le difficoltà nella separazione tra domain e application hanno influenzato
  la progettazione dei test;
- il testing ha evidenziato accoppiamenti eccessivi;
- tali criticità hanno guidato attività di refactoring e manutenzione.

Questo ciclo continuo tra architettura, testing e manutenzione è coerente con
il modello di sviluppo adottato e rappresenta un esempio concreto di
**ingegneria del software applicata**.

---

## 11. Valutazione complessiva dell’architettura

L’architettura del sistema **Gestione Dipendenti** rappresenta un compromesso
tra:
- obiettivi progettuali ambiziosi;
- vincoli temporali e didattici;
- complessità tecnologica;
- dimensione del progetto.

Pur non risultando perfetta, l’architettura è:
- coerente;
- completa;
- funzionalmente valida;
- sufficientemente modulare.

Le criticità emerse sono state comprese, analizzate e documentate, costituendo
un importante elemento di crescita tecnica e progettuale.

---

## 12. Conclusioni

Il progetto **Gestione Dipendenti** ha consentito di applicare in modo concreto
i principali concetti di **design e architettura software** affrontati durante
il corso.

Le scelte effettuate, così come le difficoltà incontrate, dimostrano:
- consapevolezza architetturale;
- capacità di analisi critica;
- attenzione alla qualità del software;
- approccio professionale allo sviluppo.

Questo documento conclude la documentazione architetturale del progetto e si
integra con gli altri file fornendo una visione completa, realistica e
difendibile del sistema sviluppato.