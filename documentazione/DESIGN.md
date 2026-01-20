# DESIGN AND ARCHITECTURE

Questo documento descrive in modo approfondito, critico e definitivo le scelte
di **design e architettura software** adottate nel progetto **Gestione
Dipendenti**, sviluppato nell’ambito del corso di **Ingegneria del Software
(AA 2025/26)**.

Il documento riflette **lo stato finale del progetto**, a valle del completamento
dell’implementazione e delle attività di testing, ed è strettamente allineato
al codice effettivamente sviluppato e consegnato.

---

## 1. Obiettivi del design

Gli obiettivi principali del design architetturale sono stati:

- strutturare il sistema in componenti chiaramente separati;
- ridurre la complessità complessiva del software;
- favorire testabilità e manutenibilità;
- applicare i principi di ingegneria del software studiati durante il corso;
- mantenere una struttura comprensibile e difendibile in un contesto accademico.

Il design non è stato concepito come un esercizio puramente teorico, ma come un
**processo iterativo**, fortemente influenzato dall’evoluzione del codice, dal
testing e dalle difficoltà tecniche incontrate.

---

## 2. Visione architetturale generale

Il sistema è organizzato secondo un’architettura **a livelli**, ispirata a
modelli architetturali consolidati e adattata alle dimensioni del progetto.

I livelli individuati sono:

- **Domain Layer**
- **Application / Service Layer**
- **Infrastructure / Data Layer**
- **Presentation Layer (GUI a falso terminale)**

Questa suddivisione è stata adottata con l’obiettivo di:
- separare la logica di business dalla logica applicativa;
- isolare la persistenza dei dati;
- limitare la propagazione delle dipendenze;
- consentire un testing mirato dei diversi componenti.

---

## 3. Domain Layer: struttura e criticità reali

### 3.1 Ruolo del Domain Layer

Il **domain layer** rappresenta il nucleo concettuale del sistema e contiene:

- le entità principali (dipendenti, utenti, ruoli, ecc.);
- le strutture dati fondamentali;
- parte delle regole di business.

L’intento iniziale era mantenere il dominio:
- indipendente dal framework;
- privo di logica applicativa;
- facilmente testabile in isolamento.

---

### 3.2 Problemi emersi nella separazione Domain / Application

Durante lo sviluppo reale del progetto è emerso che mantenere una separazione
netta e rigorosa tra **domain layer** e **application layer** è risultato più
complesso del previsto.

In particolare:

- alcune operazioni di business richiedevano il coordinamento di più entità;
- la necessità di gestire flussi applicativi complessi ha portato ad
  interazioni dirette tra dominio e servizi applicativi;
- alcune classi del dominio hanno finito per conoscere dettagli applicativi.

Questo ha generato:
- **accoppiamento eccessivo** tra domain e application;
- difficoltà nel testing isolato di alcune classi;
- maggiore complessità nella gestione delle dipendenze.

Questa criticità non è stata ignorata, ma è stata:
- analizzata;
- documentata;
- parzialmente mitigata tramite refactoring.

---

## 4. Application / Service Layer: orchestrazione e limiti

### 4.1 Ruolo dell’Application Layer

Il **application layer** ha il compito di:

- orchestrare i casi d’uso;
- coordinare dominio e persistenza;
- gestire il flusso delle operazioni applicative.

Questo livello funge da **punto di controllo centrale** del sistema e rappresenta
l’accesso principale alla logica di business.

---

### 4.2 Accoppiamento e complessità nei servizi

Nel progetto reale, alcuni servizi applicativi hanno assunto un numero elevato
di responsabilità, in particolare quando:

- gestivano flussi complessi (es. import/export dati);
- coordinavano più componenti contemporaneamente;
- interagivano con file system e database.

Questo ha portato a:
- servizi con molti metodi;
- metodi con elevata complessità ciclomatica;
- maggiore difficoltà nel testing unitario.

Queste problematiche sono state individuate principalmente durante la fase di
testing, confermando il ruolo del testing come strumento di validazione
architetturale.

---

## 5. Function Decomposition e Object-Oriented Design

### 5.1 Approccio adottato

Durante il progetto si è cercato di combinare:

- **function decomposition**, per suddividere le operazioni in passi chiari;
- **object-oriented design**, per modellare il dominio tramite oggetti.

Questo approccio misto è stato scelto per:
- rendere comprensibili operazioni complesse;
- mantenere una struttura modulare;
- evitare metodi monolitici.

---

### 5.2 Limiti concreti riscontrati

Nel codice reale del progetto, alcune operazioni (ad esempio:
- importazione di file CSV;
- conversione dei dati in strutture interne;
- gestione di file e byte;
- scrittura e lettura da cartelle)

si sono rivelate **difficili da scomporre ulteriormente** senza introdurre
complessità aggiuntiva.

Di conseguenza:
- alcuni metodi risultano lunghi e complessi;
- il top-down decomposition non sempre ha prodotto componenti riutilizzabili;
- l’aggregazione finale dei metodi ha generato sovraccarico in alcuni moduli.

Questo ha avuto un impatto diretto sulla testabilità di tali componenti.

---

## 6. Modularità, coesione e information hiding

### 6.1 Obiettivi perseguiti

Il progetto ha cercato di applicare i seguenti principi:

- modularità;
- alta coesione;
- basso accoppiamento;
- **information hiding**.

L’information hiding è stato utilizzato per:
- nascondere dettagli implementativi;
- ridurre la complessità percepita;
- limitare l’accesso diretto ai dati sensibili.

---

### 6.2 Difficoltà con nuove tecnologie

L’introduzione di **Spring Boot** e della **GUI personalizzata** ha rappresentato
una nuova competenza tecnica, che ha influito sulla capacità di applicare questi
principi in modo rigoroso e uniforme.

Le difficoltà non sono state teoriche, ma pratiche:
- gestione del ciclo di vita dei bean;
- configurazione del contesto applicativo;
- integrazione tra componenti già esistenti.

---

## 7. Architettura Spring Boot

Spring Boot è stato utilizzato come framework principale per:

- configurazione dell’applicazione;
- dependency injection;
- gestione del ciclo di vita;
- integrazione con JPA/Hibernate.

L’uso di Spring Boot è stato:
- studiato;
- sperimentato;
- applicato in modo progressivo.

Nonostante la complessità iniziale, il framework ha consentito di strutturare
l’applicazione in modo più ordinato rispetto a una gestione manuale.

---

## 8. Sicurezza: scelte e limiti

### 8.1 Misure adottate

Le misure di sicurezza implementate includono:

- hashing delle password;
- generazione di **UUID** per l’identificazione degli utenti;
- memorizzazione dei dati sensibili esclusivamente nel database;
- limitazione dell’accesso diretto alle informazioni critiche.

---

### 8.2 Criticità individuate

È stata identificata una criticità nella gestione uniforme delle password:

- alcuni metodi applicativi richiamano correttamente i controlli di sicurezza;
- altri accedono ai dati senza un passaggio uniforme dal livello di controllo.

Questo può introdurre potenziali rischi, anche se le password risultano hashate.

La problematica è stata riconosciuta come **area di miglioramento futuro**.

---

## 9. Presentation Layer: GUI a falso terminale

### 9.1 Motivazioni della scelta

L’interfaccia utente è stata realizzata come **GUI a falso terminale**, con
l’obiettivo di:

- evitare la complessità di una web UI;
- concentrarsi sulla logica applicativa;
- mantenere un controllo completo sui flussi.

---

### 9.2 MVC e ponte verso il main

La GUI segue il pattern **Model–View–Controller**:

- **Model**: dominio e servizi;
- **View**: terminale simulato;
- **Controller**: gestione dell’interazione.

È stato implementato un **ponte** verso il `main` originale, consentendo di
riutilizzare integralmente la logica già esistente senza duplicazioni.

La GUI non contiene logica di business.

---

## 10. Uso consapevole del Vibe Coding

Nel progetto è stato adottato il **vibe coding** come strumento di supporto, non
come sostituto dello sviluppatore.

È stato utilizzato per:
- comprendere framework e concetti nuovi;
- supportare la scrittura di test complessi;
- gestire casi specifici (CSV, file, conversioni);
- strutturare la documentazione.

Oltre il **95% del codice applicativo** è stato scritto manualmente.

---

## 11. Modellazione UML

Il progetto è corredato da una modellazione UML completa, utilizzata per:

- progettazione iniziale;
- comprensione dell’architettura;
- documentazione finale.

Sono presenti:
- diagrammi dei casi d’uso;
- diagrammi delle classi;
- diagrammi di sequenza;
- diagrammi di attività;
- diagrammi di stato;
- diagrammi dei componenti e dei package.

---

## 12. Valutazione finale del design

Il design del sistema rappresenta un compromesso realistico tra:

- obiettivi progettuali;
- complessità del problema;
- vincoli temporali;
- apprendimento tecnologico.

Pur non essendo perfetto, il design è:
- coerente;
- completo;
- testato;
- difendibile.

---

## 13. Conclusioni

Il progetto **Gestione Dipendenti** ha permesso di applicare in modo concreto i
principi di **design e architettura software**.

Le difficoltà incontrate sono state comprese, documentate e utilizzate come
strumento di apprendimento, contribuendo alla maturazione tecnica e
progettuale del team.
