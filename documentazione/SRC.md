# Specifica dei Requisiti del Software (SRC)

## 1. Introduzione

### 1.1 Scopo del documento

La presente **Specifica dei Requisiti del Software (SRC)** definisce in modo formale, completo e verificabile i **requisiti funzionali**, **non funzionali** e **di processo** del sistema **Gestione Dipendenti**.

L’obiettivo è la realizzazione di un **prodotto software nuovo e pienamente funzionante**, conforme agli standard professionali e progettato per una gestione sicura, moderna ed efficiente del personale aziendale.

### 1.2 Scopo del sistema

Il sistema deve permettere la **gestione centralizzata dei dipendenti e degli utenti aziendali**, supportando funzioni di:

* **Gestione utenti**: creazione, modifica e rimozione di utenti del sistema;
* **Gestione ruoli e permessi**: suddivisione delle funzionalità e dei metodi accessibili per ciascun ruolo (Amministratore, Operatore HR, Utente standard);
* **Gestione dati dipendenti**: inserimento, modifica, cancellazione, ricerca e visualizzazione dei dati anagrafici e contrattuali;
* **Elaborazione statistiche e report**;
* **Gestione sicura e tracciabile** delle operazioni e dei dati archiviati.

Il sistema dovrà funzionare in ambiente **Spring Boot** con **JPA/Hibernate**, utilizzando **database relazionali** e file **CSV** per la persistenza e lo scambio dati.

### 1.3 Stakeholder

| Ruolo             | Descrizione                                                 |
| ----------------- | ----------------------------------------------------------- |
| **Committente**   | Azienda richiedente il nuovo sistema gestionale.            |
| **Utenti finali** | Personale HR, amministratori di sistema e operatori.        |
| **Sviluppatori**  | Riccardo Scola Colombo, Micheal Leone Tomasoni.             |
| **Supervisore**   | Prof. Angelo Gargantini – revisione metodologica e tecnica. |

### 1.4 Riferimenti

* IEEE Std 830-1998 – *Software Requirements Specification Standard*
* ISO/IEC 9126 – *Software Product Quality*
* Van Vliet, *Software Engineering: Principles and Practice*
* Documenti interni di progetto (*SDD*, *STP*, *Project Plan*)

---

## 2. Descrizione generale

### 2.1 Contesto operativo

Il software sarà sviluppato in **Java 17** con **Spring Boot** e architettura a **tre livelli (API, Service, Data)**.
Il sistema dovrà garantire:

* compatibilità con database relazionali (**PostgreSQL**, **MySQL**);
* esportazione e importazione dati in formato **CSV**;
* interfacce dedicate per la navigazione e gestione (CLI iniziale, estendibile a GUI e API REST).

### 2.2 Funzionalità principali

* Gestione utenti e autenticazione.
* Creazione e assegnazione ruoli (Amministratore, HR, Utente base).
* Gestione dei dati anagrafici e contrattuali dei dipendenti.
* Calcolo e visualizzazione di statistiche aziendali.
* Import/export dati in formato CSV.
* Accesso controllato alle operazioni in base ai permessi del ruolo.
* Validazione e controllo consistenza dei dati.
* Interfaccia utente intuitiva e coerente con le policy aziendali di sicurezza.

### 2.3 Vincoli e assunzioni

* Compatibilità con Java 17+ e framework Spring Boot.
* Uso esclusivo di tecnologie open source.
* Funzionamento in rete locale o in ambiente standalone.
* Ogni modifica dovrà essere tracciata e revisionata nel repository GitHub.

---

## 3. Requisiti funzionali

| ID        | Descrizione                                                                                 | Priorità | Verifica             |
| --------- | ------------------------------------------------------------------------------------------- | -------- | -------------------- |
| **RF-01** | Il sistema deve consentire la creazione di un nuovo utente con ruolo specifico.             | Alta     | Test funzionale      |
| **RF-02** | Il sistema deve permettere all’amministratore di modificare o eliminare utenti.             | Alta     | Test funzionale      |
| **RF-03** | Il sistema deve gestire i permessi e differenziare le funzionalità per ruolo.               | Alta     | Test di sicurezza    |
| **RF-04** | Il sistema deve consentire l’inserimento, modifica e cancellazione dei dati dei dipendenti. | Alta     | Test di integrazione |
| **RF-05** | Il sistema deve permettere la ricerca dei dipendenti per vari criteri.                      | Alta     | Test funzionale      |
| **RF-06** | Il sistema deve consentire l’importazione e l’esportazione dei dati in formato CSV.         | Media    | Test funzionale      |
| **RF-07** | Il sistema deve generare statistiche e report sui dipendenti.                               | Media    | Test di accettazione |
| **RF-08** | Il sistema deve garantire la sicurezza e la validazione dei dati archiviati.                | Alta     | Test di sicurezza    |
| **RF-09** | Il sistema deve registrare le operazioni svolte dagli utenti per tracciabilità.             | Media    | Test di sistema      |
| **RF-10** | Il sistema deve fornire messaggi chiari di errore o conferma.                               | Alta     | Test di usabilità    |

---

## 4. Requisiti non funzionali

| ID         | Descrizione                                                                                        | Categoria            | Verifica                 |
| ---------- | -------------------------------------------------------------------------------------------------- | -------------------- | ------------------------ |
| **RNF-01** | Il sistema deve garantire un tempo di risposta inferiore a 2 secondi per le operazioni principali. | Prestazioni          | Test di performance      |
| **RNF-02** | L’interfaccia utente deve essere intuitiva, accessibile e coerente.                                | Usabilità            | Test di usabilità        |       |
| **RNF-03** | Il sistema deve supportare la scalabilità e l’aggiunta di nuovi moduli (GUI, API REST).            | Manutenibilità       | Revisione architetturale |
| **RNF-04** | Ogni versione del software deve essere documentata e tracciabile.                                  | Qualità del processo | Audit documentale        |

---

## 5. Requisiti di processo

* **Metodologia:** modello ibrido *Waterfall–V–Scrum* con iterazioni e testing per ogni fase.
* **Gestione del codice:** tramite GitHub, con branching e revisioni periodiche.
* **Testing:** pianificazione dettagliata (unità, integrazione, sistema e accettazione).
* **Documentazione:** completa e aggiornata, includendo *SRC*, *SDD*, *STP*, *Manuale tecnico* e *Manuale utente*.

---

## 6. Modellazione UML

Il sistema prevede:

* **Diagramma delle classi**
* **Diagrammi dei casi d’uso** 
* **Componenti**

---

## 7. Criteri di accettazione

Il software sarà considerato conforme se:

1. È pienamente funzionante e privo di errori critici.
2. Implementa correttamente la gestione utenti, ruoli e permessi.
3. Garantisce la persistenza e la sicurezza dei dati nel database e nei file CSV.
4. Supera tutti i test funzionali e di integrazione pianificati.
5. Include una documentazione aggiornata e coerente con gli standard di qualità.

---

## 8. Tracciabilità dei requisiti

Ogni requisito sarà associato a:

* i **test case** che ne verificano la corretta implementazione;
* i **moduli software** e i livelli architetturali che lo realizzano.

---

## 9. Conclusioni

La presente **Specifica dei Requisiti del Software (SRC)** definisce in modo rigoroso le funzionalità, i vincoli e i criteri di qualità del sistema **Gestione Dipendenti**.
Il prodotto risultante sarà un’applicazione nuova, sicura e modulare, in grado di gestire utenti, ruoli e dati in modo efficiente e conforme agli standard di ingegneria del software.
