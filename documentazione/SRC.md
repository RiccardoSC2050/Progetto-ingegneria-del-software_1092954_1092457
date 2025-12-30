# Software Requirements Specification (SRC)
## Gestione Dipendenti

Questo documento descrive la **Specifica dei Requisiti del Software (SRC)** del progetto
**Gestione Dipendenti**, sviluppato nell’ambito del corso di **Ingegneria del Software
(AA 2025/26)**.

Il README ha lo scopo di fornire una descrizione chiara, strutturata e verificabile
dei requisiti del sistema, in linea con:
- lo standard **IEEE 830**;
- i **requisiti ufficiali del corso**;
- il **Project Plan** approvato;
- lo **stato reale di avanzamento del progetto**.

---

## 1. Obiettivi del documento

Questo documento intende:
- definire i **requisiti funzionali, non funzionali e di processo** del sistema;
- fungere da riferimento condiviso tra i membri del team e il supervisore accademico;
- supportare le attività di **progettazione, implementazione, testing e manutenzione**;
- garantire la **tracciabilità** tra requisiti, codice sorgente e casi di test.

La documentazione è mantenuta in formato Markdown per favorire:
- versionamento tramite Git;
- aggiornamenti incrementali;
- coerenza con il processo di sviluppo adottato.

---

## 2. Scopo del sistema

Il sistema **Gestione Dipendenti** è un’applicazione software progettata per supportare
la **gestione centralizzata del personale aziendale**, superando i limiti di sistemi
legacy obsoleti e soggetti a errori.

Il sistema consente di:
- gestire utenti applicativi e ruoli;
- controllare l’accesso alle funzionalità in base ai permessi;
- gestire i dati anagrafici e contrattuali dei dipendenti;
- effettuare ricerche e filtraggi mirati;
- elaborare statistiche aziendali;
- importare ed esportare dati tramite file CSV;
- garantire tracciabilità e sicurezza delle operazioni.

---

## 3. Contesto di utilizzo

Il software è sviluppato in:
- **Java 17**
- **Spring Boot**
- **JPA / Hibernate**
- **Maven** per la gestione delle dipendenze

Il sistema è progettato per funzionare:
- in ambiente standalone;
- con database relazionali embedded o locali;
- senza dipendenze da servizi esterni obbligatori.

L’interazione con l’utente avviene tramite:
- **CLI evoluta / GUI a falso terminale**, sviluppata per simulare un’interfaccia
  testuale strutturata;
- possibile estensione futura verso GUI grafiche o API REST.

---

## 4. Architettura concettuale e approccio MVC

Il sistema è progettato seguendo una **separazione logica delle responsabilità**
ispirata al pattern **Model–View–Controller (MVC)**.

### 4.1 Model
Il *Model* rappresenta il dominio applicativo:
- entità di dominio (Dipendente, Utente, Ruolo, ecc.);
- logica di business;
- gestione della persistenza tramite JPA/Hibernate.

### 4.2 View
La *View* è implementata tramite una **GUI a falso terminale**:
- interfaccia testuale strutturata;
- navigazione guidata tramite menu;
- separazione chiara tra logica di presentazione e logica applicativa.

Questa scelta è stata adottata per:
- mantenere semplicità e controllabilità del sistema;
- simulare una GUI coerente senza introdurre complessità grafiche premature;
- garantire testabilità e modularità.

### 4.3 Controller
Il *Controller* coordina:
- l’interazione tra View e Model;
- la validazione degli input;
- l’invocazione dei servizi applicativi.

---

## 5. Considerazioni sulla separazione Domain / Application

Durante lo sviluppo è emersa una **difficoltà non banale** nel mantenere una
separazione perfettamente rigida tra:
- **Domain layer** (logica di dominio pura);
- **Application layer** (orchestrazione dei casi d’uso).

Questa criticità è stata affrontata in modo consapevole e professionale:
- cercando di isolare il più possibile la logica di dominio;
- limitando le dipendenze dirette tra livelli;
- accettando, in alcuni casi, compromessi pragmatici dovuti alla dimensione
  del progetto e al contesto accademico.

Nonostante ciò, il team ha perseguito **al 100% l’obiettivo di una struttura
professionale**, mantenendo:
- coesione interna ai package;
- accoppiamento controllato;
- responsabilità ben definite.

Questa esperienza ha rappresentato un importante momento di crescita tecnica
e progettuale.

---

## 6. Stakeholder

| Ruolo | Descrizione |
|------|-------------|
| Committente | Azienda richiedente il sistema gestionale |
| Utenti finali | Personale HR, amministratori, operatori |
| Sviluppatori | Riccardo Scola Colombo, Micheal Leone Tomasoni |
| Supervisore | Prof. Angelo Gargantini |

---

## 7. Vincoli e assunzioni

- Il sistema deve essere sviluppato in **Java** come progetto Eclipse/Maven.
- Tutto il codice deve essere versionato su **GitHub**.
- Le modifiche devono essere tracciabili tramite commit e issue.
- Il sistema deve essere funzionante e dimostrabile all’orale.
- Il testing sarà completato prima della consegna finale.
---

## 8. Requisiti funzionali

I requisiti funzionali descrivono le funzionalità che il sistema deve fornire
agli utenti e agli amministratori.  
Ogni requisito è identificato in modo univoco per favorire la **tracciabilità**
con i casi d’uso, l’implementazione e i test.

### 8.1 Gestione utenti e ruoli

| ID | Descrizione |
|----|------------|
| RF-01 | Il sistema deve consentire la creazione di nuovi utenti applicativi. |
| RF-02 | Il sistema deve permettere la modifica e la rimozione degli utenti. |
| RF-03 | Il sistema deve assegnare a ciascun utente un ruolo specifico. |
| RF-04 | Il sistema deve limitare le operazioni disponibili in base al ruolo. |
| RF-05 | Il sistema deve garantire l’accesso al sistema solo a utenti autenticati. |

---

### 8.2 Gestione dipendenti

| ID | Descrizione |
|----|------------|
| RF-06 | Il sistema deve consentire l’inserimento di un nuovo dipendente. |
| RF-07 | Il sistema deve permettere la modifica dei dati di un dipendente. |
| RF-08 | Il sistema deve consentire la cancellazione di un dipendente. |
| RF-09 | Il sistema deve permettere la visualizzazione dei dati dei dipendenti. |
| RF-10 | Il sistema deve supportare la ricerca dei dipendenti tramite filtri. |

---

### 8.3 Gestione dati e statistiche

| ID | Descrizione |
|----|------------|
| RF-11 | Il sistema deve consentire l’importazione dei dati tramite file CSV. |
| RF-12 | Il sistema deve consentire l’esportazione dei dati in formato CSV. |
| RF-13 | Il sistema deve generare statistiche sui dipendenti. |
| RF-14 | Il sistema deve fornire riepiloghi e report testuali. |

---

### 8.4 Gestione errori e feedback utente

| ID | Descrizione |
|----|------------|
| RF-15 | Il sistema deve fornire messaggi chiari in caso di errore. |
| RF-16 | Il sistema deve confermare all’utente le operazioni eseguite con successo. |
| RF-17 | Il sistema deve gestire in modo controllato input non validi. |

---

## 9. Requisiti non funzionali

I requisiti non funzionali definiscono le **qualità del sistema**, in linea con
le indicazioni del corso e con gli standard di qualità del software.

### 9.1 Prestazioni

| ID | Requisito |
|----|-----------|
| RNF-01 | Il sistema deve rispondere alle operazioni principali entro 2 secondi. |
| RNF-02 | Le operazioni di import/export CSV devono essere completate in tempi accettabili. |

---

### 9.2 Usabilità

| ID | Requisito |
|----|-----------|
| RNF-03 | L’interfaccia a falso terminale deve essere semplice e guidata. |
| RNF-04 | I menu devono essere coerenti e facilmente navigabili. |

---

### 9.3 Manutenibilità e qualità del codice

| ID | Requisito |
|----|-----------|
| RNF-05 | Il codice deve essere organizzato in package coerenti. |
| RNF-06 | Il sistema deve favorire la separazione delle responsabilità. |
| RNF-07 | Le modifiche devono poter essere effettuate senza impatti estesi. |

---

### 9.4 Sicurezza e affidabilità

| ID | Requisito |
|----|-----------|
| RNF-08 | Il sistema deve garantire la validazione dei dati in input. |
| RNF-09 | Le operazioni critiche devono essere protette da controlli di accesso. |
| RNF-10 | Il sistema deve prevenire stati inconsistenti dei dati. |

---

## 10. Requisiti di processo

Il processo di sviluppo del sistema segue un **modello ibrido
Waterfall – V-Model – Scrum**, come descritto nel Project Plan.

In particolare:
- i requisiti sono stati definiti e raffinati in modo incrementale;
- ogni fase di sviluppo è associata ad attività di verifica e validazione;
- lo sviluppo è organizzato in sprint con rilasci progressivi;
- il controllo di versione è gestito tramite GitHub;
- l’uso di branch, pull request e issue è parte integrante del processo.

---

## 11. Modellazione UML

La modellazione UML è parte fondamentale del progetto, in conformità ai
requisiti del corso.

Sono previsti (e in parte già realizzati):
- diagrammi dei **casi d’uso**;
- diagrammi delle **classi** (realizzati in Papyrus);
- diagrammi di **sequenza**;
- diagrammi di **attività**;
- diagrammi di **stato**;
- diagrammi di **componenti** e **package**.

I diagrammi sono utilizzati sia come strumento di progettazione sia come
supporto alla documentazione del sistema.

Alcuni diagrammi risultano ancora in fase di completamento e verranno
finalizzati insieme alle ultime attività di testing.

---

## 12. Testing (stato attuale)

Le attività di testing sono pianificate in conformità ai requisiti del corso e
al Project Plan.

Attualmente:
- sono stati realizzati test iniziali su componenti critiche;
- sono in corso test di integrazione e validazione;
- i casi di test definitivi verranno completati prima della consegna finale.

La documentazione completa dei test sarà raccolta in un documento dedicato
(**TESTING.md**), che includerà:
- descrizione dei casi di test;
- collegamento ai requisiti;
- eventuali misure di copertura.

---

## 13. Tracciabilità dei requisiti

Ogni requisito definito in questo documento è tracciabile rispetto a:
- uno o più casi d’uso UML;
- i moduli software che lo implementano;
- i casi di test che ne verificano il corretto funzionamento.

La tracciabilità è garantita attraverso:
- naming coerente dei requisiti;
- struttura modulare del codice;
- documentazione versionata tramite Git.

---

## 14. Stato del documento

Questo README rappresenta la **versione corrente della Specifica dei Requisiti
del Software**.

Il documento potrà subire aggiornamenti minori in seguito:
- al completamento delle attività di testing;
- alla finalizzazione dei diagrammi UML;
- a eventuali rifiniture documentali richieste in fase di revisione finale.

---

## 15. Considerazioni finali

La definizione dei requisiti ha rappresentato una fase centrale del progetto.
Il team ha cercato di mantenere un equilibrio tra:
- rigore metodologico;
- applicabilità pratica;
- complessità gestibile in un contesto accademico.

Le difficoltà incontrate, in particolare nella separazione netta tra dominio e
livello applicativo, sono state affrontate in modo consapevole, con l’obiettivo
di avvicinarsi il più possibile a una struttura professionale reale.

Il documento riflette fedelmente lo stato del progetto e costituisce una base
solida per la conclusione delle attività di sviluppo e testing.
