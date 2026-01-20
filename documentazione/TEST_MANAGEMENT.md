# Software Testing Specification (STP)

Questo documento descrive in modo completo e definitivo le attività di
**Software Testing** svolte per il progetto **Gestione Dipendenti**, sviluppato
nell’ambito del corso di **Ingegneria del Software (AA 2025/26)**.

Il testing è stato affrontato come **fase strutturata e conclusiva** del ciclo
di sviluppo, con l’obiettivo di validare il comportamento del sistema, la
correttezza dell’implementazione e la conformità ai requisiti definiti nella
SRC.

---

## 1. Obiettivi del testing

Le attività di testing hanno avuto i seguenti obiettivi:

- verificare l’implementazione dei requisiti funzionali;
- individuare errori logici e di integrazione;
- validare la stabilità dei moduli principali;
- supportare il refactoring finale del codice;
- garantire un livello di qualità coerente con un progetto accademico avanzato.

Il testing è stato utilizzato come **strumento di verifica formale** e non come
semplice controllo manuale del software.

---

## 2. Approccio metodologico

Il progetto segue un **modello di sviluppo ibrido**, che combina:

- approccio a cascata per analisi e progettazione;
- **V-Model** per verifica e validazione;
- sviluppo incrementale ispirato a Scrum.

In coerenza con il V-Model, il testing è stato eseguito **dopo il completamento
dell’implementazione**, seguendo una logica sequenziale e controllata:

1. progettazione delle classi;
2. implementazione dei metodi;
3. definizione dei casi di test;
4. esecuzione dei test;
5. analisi dei risultati;
6. correzioni e refactoring;
7. riesecuzione dei test.

---

## 3. Tipologie di testing adottate

### 3.1 Unit Testing

Il **testing di unità** è stato effettuato utilizzando **JUnit** ed è stato
applicato a tutte le componenti testabili del sistema.

Caratteristiche principali:
- test focalizzati su singoli metodi e classi;
- verifica di casi normali e casi limite;
- isolamento della logica di business;
- controllo esplicito dei risultati attesi.

Sono stati testati:
- domain layer;
- application / service layer;
- moduli di utilità;
- componenti di gestione dei dati.

---

### 3.2 Integration Testing

Il **testing di integrazione** ha verificato la corretta cooperazione tra:

- servizi applicativi e dominio;
- logica applicativa e persistenza;
- moduli coinvolti negli stessi flussi funzionali.

Questa fase ha permesso di individuare errori non rilevabili tramite unit test
isolati.

---

### 3.3 System Testing

Il **testing di sistema** ha validato il comportamento complessivo
dell’applicazione, simulando scenari di utilizzo realistici.

Sono stati verificati:
- flussi funzionali completi;
- gestione delle eccezioni;
- coerenza dei risultati;
- stabilità del sistema.

---

### 3.4 Acceptance Testing

Il **testing di accettazione** ha verificato la conformità del sistema ai
requisiti definiti nella **SRC**.

Ogni requisito funzionale è stato validato tramite uno o più casi di test,
garantendo la tracciabilità tra requisiti, codice e test.

---

## 4. Copertura del testing

Le attività di testing hanno raggiunto una **copertura superiore al 75% per ogni
modulo e package testabile** del sistema.

In particolare:
- tutti i moduli di dominio sono stati testati;
- tutti i servizi applicativi sono stati verificati;
- la persistenza è stata testata tramite test dedicati;
- le parti non testabili automaticamente sono state identificate e motivate.

Il livello di copertura ottenuto è considerato **ampiamente soddisfacente** per
la dimensione e la complessità del progetto.

---

## 5. Organizzazione dei test

I test sono organizzati in modo coerente con la struttura del codice
applicativo:

- ogni package di test riflette il package corrispondente del codice;
- è rispettata la separazione tra domain, application e data;
- i test sono facilmente individuabili e manutenibili.

Questa struttura favorisce:
- tracciabilità;
- manutenibilità;
- analisi rapida degli errori.

---

## 6. Testing del database

Il database è stato testato in un **ambiente dedicato e isolato**.

La strategia adottata prevede:
- utilizzo di un **pattern Singleton** per la gestione della connessione;
- inizializzazione controllata dello stato dei dati;
- ripristino dello stato al termine dei test.

Questo approccio garantisce:
- indipendenza tra i casi di test;
- risultati deterministici;
- ripetibilità delle esecuzioni.

---

## 7. Limiti emersi durante il testing

Durante le attività di testing sono emerse alcune criticità strutturali:

- difficoltà nell’integrare in modo ottimale **function decomposition** e
  **object-oriented design**;
- presenza di moduli con un numero elevato di metodi;
- metodi eccessivamente complessi, difficili da testare in modo isolato;
- accoppiamento tra componenti che ha limitato la testabilità di alcune parti.

Queste criticità rappresentano **limiti architetturali emersi durante la fase di
validazione** e sono stati analizzati e documentati anche nel file di Design.

---

## 8. Testing della GUI (scelta progettuale)

La **GUI a falso terminale** non è stata sottoposta a testing automatico.

La GUI è stata sviluppata:
- seguendo il pattern **Model–View–Controller**;
- utilizzando un **ponte** verso il `main` originale;
- riutilizzando integralmente la logica già testata del sistema.

La scelta di non testare automaticamente la GUI è stata motivata da:
- natura testuale e non web dell’interfaccia;
- separazione completa dalla logica di business;
- vincoli temporali accademici e universitari.

La GUI è stata comunque:
- verificata manualmente;
- valutata in termini di funzionamento ed efficienza;
- considerata correttamente integrata nel sistema.

---

## 9. Strumenti utilizzati

- **JUnit** per unit e integration testing;
- **Maven** per la gestione del build;
- **Eclipse** come ambiente di sviluppo;
- database locale / embedded per i test.

---

## 10. Conclusioni

Le attività di testing confermano che il sistema **Gestione Dipendenti** è:

- funzionalmente corretto;
- stabile;
- conforme ai requisiti definiti;
- testato in modo esteso e sistematico.

Il testing automatico del core del sistema, unito alla validazione manuale della
GUI, consente di considerare **conclusa la fase di testing del progetto**.

---

## 11. Criteri di completamento del testing

La fase di testing è stata considerata **completata** sulla base di criteri
chiari e misurabili, definiti prima della chiusura del progetto.

In particolare, il testing è stato ritenuto concluso quando:

- tutti i requisiti funzionali definiti nella **SRC** risultano implementati;
- ogni modulo e package testabile ha raggiunto una **copertura superiore al 75%**;
- il domain layer e l’application layer sono stati completamente testati
  tramite JUnit;
- i flussi funzionali principali sono stati verificati tramite system e
  integration test;
- le parti non testate automaticamente sono state **identificate, motivate e
  documentate**.

Il soddisfacimento di questi criteri consente di considerare il sistema
sufficientemente verificato rispetto agli obiettivi del progetto.

---

## 12. Valutazione del rischio residuo

Come in ogni progetto software reale, al termine della fase di testing rimane
un **rischio residuo**, legato alle parti non coperte da test automatici.

Nel progetto Gestione Dipendenti, il rischio residuo è limitato a:

- la **GUI a falso terminale**, non sottoposta a test automatici.

Tale rischio è considerato **accettabile** per i seguenti motivi:

- la GUI rappresenta esclusivamente un livello di presentazione;
- tutta la logica di business sottostante è già stata testata;
- la GUI riutilizza il `main` originale tramite un ponte e un controller;
- eventuali malfunzionamenti della GUI non compromettono l’integrità dei dati
  né la correttezza del dominio.

La GUI è stata comunque sottoposta a **validazione manuale**, riducendo ulteriormente
il rischio operativo.

---

## 13. Allineamento con il Project Plan

Le attività di testing sono state svolte in piena coerenza con quanto definito
nel **Project Plan**.

In particolare:
- il testing è previsto come fase conclusiva del ciclo di sviluppo;
- le attività di verifica e validazione sono allineate al **V-Model**;
- il completamento del testing coincide con la chiusura tecnica del progetto.

Il rispetto del piano di progetto e il raggiungimento degli obiettivi di
testing confermano che il sistema ha raggiunto un livello di qualità adeguato
alla consegna finale.

---

## 14. Considerazioni finali sul testing

Il testing del progetto Gestione Dipendenti non si è limitato a una verifica
superficiale del codice, ma ha rappresentato un’attività strutturata,
sistematica e consapevole.

I risultati ottenuti dimostrano che:
- il core del sistema è stato ampiamente testato;
- i limiti emersi sono noti e documentati;
- le scelte progettuali sono state validate in modo critico.

Alla luce di quanto sopra, la fase di testing può essere considerata
**completata e conclusa**, fornendo un software stabile, verificato e coerente
con gli obiettivi del progetto.
