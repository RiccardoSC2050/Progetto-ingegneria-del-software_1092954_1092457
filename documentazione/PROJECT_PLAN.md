# Descrizione del Progetto – Piano di Progetto (MODIFICABILE)

## 1. Introduzione
Il progetto nasce dall’esigenza di un’azienda che necessita di uno strumento moderno e affidabile per la **gestione dei dipendenti**.  
Il vecchio programma in uso si è rivelato obsoleto e soggetto a errori, pertanto si è reso necessario lo sviluppo di un nuovo sistema, costruito con tecnologie aggiornate (**Java**), che garantisca **fluidità, stabilità e facilità di utilizzo**.  

Il nuovo sistema dovrà permettere:
- la ricerca mirata dei dipendenti sulla base di criteri specifici;  
- la memorizzazione delle informazioni in un database o in file CSV;  
- la consultazione di informazioni generali e statistiche, come numero di dipendenti, anzianità, media dei richiami aziendali, ecc.  

**Obiettivo principale:** fornire all’azienda un software efficiente, moderno e intuitivo, capace di eliminare gli errori gestionali precedenti e migliorare i processi interni.  

**Responsabili del progetto:**  
- Riccardo Scola Colombo  
- Micheal Leone Tomasoni  

**Supervisione:** Prof. Angelo Gargantini (consulente e supervisore accademico)  

**Referente esterno:** l’azienda committente.  

---

## 2. Modello di processo

Il progetto seguirà un approccio **ibrido** che combina i principi del **modello a cascata (Waterfall)** con le buone pratiche del **modello a V** e del **framework Scrum**.
L’obiettivo è mantenere una struttura chiara e controllata delle fasi, garantendo al contempo flessibilità, feedback continuo e controllo iterativo della qualità.

### 2.1 Struttura generale

Lo sviluppo sarà organizzato in **fasi sequenziali**, ciascuna delle quali prevede:

* **attività di progettazione, implementazione e verifica**,
* **testing dedicato** prima del passaggio alla fase successiva.

Il processo seguirà la logica del **modello a V**, in cui ogni fase di sviluppo è associata a una specifica fase di validazione e test:

| Fase di sviluppo             | Attività di testing corrispondente                              |
| ---------------------------- | --------------------------------------------------------------- |
| Analisi dei requisiti        | Verifica e validazione dei requisiti (Acceptance Test Planning) |
| Progettazione architetturale | Test di integrazione dei moduli                                 |
| Progettazione di dettaglio   | Test di unità e componenti                                      |
| Implementazione              | Testing funzionale e di sistema                                 |
| Collaudo e rilascio          | Test di accettazione finale                                     |

Questo garantisce una **verifica continua** della qualità e la possibilità di **identificare e correggere errori precocemente**.

---

### 2.2 Iteratività e aggiornamenti

Nonostante la struttura a cascata, ogni fase prevede **micro-iterazioni** per aggiornare o migliorare artefatti esistenti (documentazione, codice, modelli UML).
Questo approccio ibrido permette di integrare modifiche o feedback del supervisore e del committente senza compromettere la stabilità del processo.

Ogni iterazione termina con:

* revisione tecnica del lavoro svolto;
* aggiornamento della documentazione;
* pianificazione della fase successiva.

---

### 2.3 Gestione Scrum e milestone

Per il monitoraggio continuo, il progetto adotterà **principi agili del framework Scrum**:

* il lavoro sarà suddiviso in **sprint settimanali** (iterazioni brevi e regolari);
* alla fine di ogni sprint sarà effettuata una **revisione (Sprint Review)** per valutare i risultati e discutere criticità;
* saranno organizzati brevi incontri di coordinamento (anche informali) per analizzare lo stato d’avanzamento e le difficoltà incontrate.

---

### 2.4 Benefici dell’approccio ibrido

L’integrazione di Waterfall, V-Model e Scrum garantisce:

* **Struttura e tracciabilità** tipiche del Waterfall.
* **Qualità e controllo** propri del modello a V (testing in ogni fase).
* **Flessibilità e miglioramento continuo** tipici di Scrum.

Questo approccio è particolarmente adatto per progetti di ambito accademico o aziendale che richiedono sia rigore metodologico sia adattabilità ai feedback continui.


## 3. Organizzazione del progetto
Il team di progetto è composto da:
- **Riccardo Scola Colombo**  
- **Micheal Leone Tomasoni**  

Entrambi ricoprono ruoli equivalenti, con responsabilità condivise e collaborazione su tutte le attività (analisi, progettazione, implementazione e test).  

**Supervisione esterna:** il Prof. Angelo Gargantini svolgerà il ruolo di consulente e supervisore, garantendo la correttezza metodologica e il supporto accademico.  

La distribuzione dei ruoli è quindi **paritaria**: le decisioni e i compiti verranno affrontati congiuntamente.  

---

## 4. Standard, linee guida e procedure

Il progetto seguirà principi di **ingegneria del software** applicati allo sviluppo in **Java**, con l’integrazione del framework **Spring Boot** per la gestione delle componenti applicative e la persistenza dei dati tramite **JPA** e **Hibernate**.
Saranno adottate regole chiare per:

* l’organizzazione dei pacchetti e delle classi;
* la gestione delle dipendenze e dei moduli tramite **Maven**;
* la configurazione e integrazione del database relazionale;
* la scrittura di codice leggibile, scalabile e manutenibile.

### 4.1 Architettura a tre livelli

L’implementazione seguirà un’**architettura a 3 livelli (Three-Layer Architecture)** per garantire separazione delle responsabilità, modularità e manutenibilità del sistema. I livelli sono:

1. **Data Layer (Project Data)**

   * Responsabile della gestione della persistenza dei dati tramite **JPA/Hibernate**.
   * Include le entità e i repository che mappano le tabelle del database.
   * Comunicazione diretta con il database relazionale (es. PostgreSQL o MySQL).

2. **Service Layer (Project Service)**

   * Contiene la **logica di business** e gestisce le operazioni core del sistema.
   * Implementa i servizi che orchestrano le operazioni di lettura/scrittura e calcolo.
   * Applica validazioni, controlli e logiche aziendali.

3. **API Layer (Project API)**

   * Gestisce la **comunicazione tra client e sistema** tramite **Spring Boot**.
   * Espone le operazioni tramite **REST API**, permettendo un accesso standardizzato ai servizi.
   * Fornisce endpoint per inserimento, ricerca, aggiornamento, eliminazione e statistiche sui dipendenti.


### 4.2 Benefici dell’approccio

Questa architettura garantisce:

* **Separazione netta** tra logica di business, gestione dati e interfaccia API;
* **Scalabilità e estendibilità** per future evoluzioni (GUI o servizi remoti);
* **Facilità di manutenzione** e test indipendente dei moduli;
* **Aderenza ai principi SOLID** e alle best practice di sviluppo con Spring Boot.

---

Per il **controllo di versione** si utilizzerà **GitHub**, con commit regolari e branching dedicato per ogni feature o bugfix, garantendo tracciabilità e condivisione del lavoro.
La **documentazione** tecnica e utente sarà mantenuta aggiornata a ogni iterazione del progetto.


## 5. Attività di gestione
Il progetto sarà gestito tramite:  
- **check settimanali** per monitorare lo stato di avanzamento;  
- operatività costante con più sessioni di sviluppo a settimana;  
- definizione di **obiettivi a breve termine** da raggiungere entro la settimana.  

Al momento non sono state fissate **priorità rigide** tra le varie attività: l’approccio sarà adattivo, stabilendo i focus principali di volta in volta.  

Entrambi i membri del team saranno responsabili della **redazione di report e resoconti**, così da garantire trasparenza e tracciabilità dei progressi.  

---

## 6. Rischi
I principali rischi individuati sono:  
- difficoltà nella **gestione e modellazione del database**;  
- complessità nello sviluppo di una **possibile interfaccia grafica**; 

Per mitigare questi rischi, il team si impegna a:  
- affrontare da subito la parte di analisi e comprensione del database;  
- suddividere in step chiari lo sviluppo dell’interfaccia, riducendo la complessità;  
- mantenere un ritmo di lavoro costante, con obiettivi settimanali per garantire un avanzamento continuo.  

---

## 7. Personale
Il team di progetto è composto esclusivamente dai due sviluppatori principali:  
- **Riccardo Scola Colombo**  
- **Micheal Leone Tomasoni**  

Non sono previsti altri collaboratori esterni, se non il **professore Angelo Gargantini**, che ricoprirà il ruolo di revisore e supervisore accademico.  

Le competenze richieste coprono le attività standard di un progetto software:  
- sviluppo in **Java** con gestione tramite **Maven**;  
- modellazione e gestione di **database**;  
- interfaccia a riga di comando e possibilità di sviluppo di un’interfaccia grafica o web.  

Il team prevede inoltre attività di **formazione e approfondimento** su:  
- gestione e modellazione di database;  
- sviluppo di interfacce grafiche in Java (GUI);  
- ulteriori tecnologie e strumenti che si riveleranno necessari durante il progetto.  

---

## 8. Metodi e tecniche

Il progetto seguirà un approccio di **ingegneria del software strutturata**, basato su fasi distinte e documentate, ognuna con propri **obiettivi, deliverable e attività di verifica**.
L’intero ciclo sarà sviluppato secondo un modello ibrido **Waterfall–V–Scrum**, garantendo controllo formale, revisione continua e miglioramento progressivo.

---

### 8.1 Ingegneria dei requisiti

* I requisiti saranno raccolti tramite incontri, interviste e analisi delle esigenze aziendali.
* Verranno classificati in **funzionali**, **non funzionali** e **di processo**, e tracciati in modo bidirezionale.
* La formalizzazione avverrà nel documento **SRC – Specifica dei Requisiti del Software**, conforme allo standard *IEEE 830*.
* Ogni requisito sarà collegato ai relativi casi d’uso UML e successivamente ai test di validazione.
* Le modifiche ai requisiti saranno gestite tramite il **registro delle modifiche (Change Log)** e approvate dal team di progetto.

**Deliverable:**
📄 *Documento SRC* – Specifica dei Requisiti del Software

---

### 8.2 Progettazione del sistema

La progettazione sarà sviluppata in due fasi:

1. **Progettazione architetturale (high-level)** – definizione dell’architettura generale a 3 livelli (*Data, Service, API*) con Spring Boot, JPA e Hibernate.
2. **Progettazione di dettaglio (low-level)** – definizione dei moduli, classi, diagrammi UML e flussi di controllo.

Saranno prodotti documenti tecnici specifici per la progettazione e pianificata la **stima dei costi e delle risorse**:

* Stima delle **ore uomo**, **complessità dei moduli** e **costi di sviluppo** (basata su metriche COCOMO semplificate).
* Definizione della **pianificazione di progetto** con milestone e carico di lavoro stimato per ciascun membro del team.

**Deliverable:**
📄 *Documento di stima costi e risorse*

---

### 8.3 Implementazione

* Lo sviluppo avverrà in linguaggio **Java 17**, con framework **Spring Boot** e gestione del progetto tramite **Maven**.
* Ogni modulo sarà gestito in repository GitHub dedicato, con branch di feature, pull request e revisioni periodiche.
* L’integrazione dei moduli seguirà test incrementali di compatibilità tra i livelli (API, Service, Data).

---

## 9. Garanzia di qualità
La qualità del software sarà garantita attraverso l’applicazione rigorosa delle **buone pratiche di ingegneria del software** e l’adozione di un’architettura chiara, efficiente e ben documentata.  

Le principali misure di garanzia saranno:  
- sviluppo in Java/Maven con **struttura modulare e revisionabile**;  
- esecuzione regolare di **unit test** e **test di integrazione**;  
- **revisioni settimanali** del lavoro svolto per individuare eventuali correzioni o aggiunte;  
- valutazione periodica e finale da parte del **professore Angelo Gargantini**;  
- test di accettazione con il committente per verificare il rispetto dei requisiti aziendali.

---

## 10. Pacchetti di lavoro (Work Packages)
Il progetto sarà suddiviso in **macro-attività gerarchiche**, organizzate in una **struttura di scomposizione del lavoro (WBS)**.  

I principali pacchetti di lavoro previsti sono:  
1. Analisi dei requisiti  
2. Descrizione del progetto  
3. Progettazione architetturale  
4. Modellazione UML  
5. Definizione della struttura del codice  
6. Implementazione del database  
7. Implementazione della logica di gestione e ricerca  
8. Realizzazione dei test (unit test, integrazione, accettazione)  
9. Stesura della documentazione tecnica e utente  
10. Sviluppo dell’interfaccia (linea di comando → eventuale GUI/Web)  
11. Consegna finale  

Questa struttura rappresenta una **prima ipotesi di sviluppo**, da dettagliare e perfezionare man mano che il progetto avanza.  

---

## 11. Risorse
Il progetto utilizzerà le seguenti risorse:  
- **Hardware**: PC personali dei membri del team;  
- **Software**: IDE **Eclipse** con Java, Maven, strumenti di documentazione;  
- **Strumenti di supporto**: GitHub per il controllo di versione.  

Non sono necessarie **licenze a pagamento** né risorse aggiuntive (server, storage dedicato, ecc.).  

---

## 12. Budget e programma

Il progetto non dispone di un **budget economico ufficiale**, poiché si tratta di un’attività accademica svolta a fini formativi.
Tuttavia, al fine di mantenere un approccio professionale e realistico, sarà comunque effettuata una **stima dei costi e delle risorse** legata alle attività di sviluppo.

La stima includerà:

* il numero di **ore uomo** impiegate nelle diverse fasi (analisi, progettazione, implementazione, test e documentazione);
* una valutazione teorica dei **costi orari** equivalenti, calcolati su base di mercato;
* la distribuzione delle risorse e degli sforzi in funzione delle **milestone** e delle priorità di progetto.

Questa analisi consentirà di fornire una valutazione quantitativa del lavoro svolto, utile per comprendere l’impegno necessario e per eventuali futuri progetti aziendali o accademici.

La **durata stimata** complessiva rimane di circa **4 mesi**, con eventuale estensione in base ai progressi e agli aggiornamenti introdotti nelle iterazioni successive.

---

## 13. Gestione dei cambiamenti
Eventuali **richieste di variazione** da parte del committente verranno valutate in termini di impatto su **tempi** e **attività**.  
Se approvate, saranno integrate in una **fase di revisione specifica**.  

Non sono previste modifiche sostanziali dopo l’avvio dell’implementazione, poiché si tratta di un progetto con **finalità didattiche** e **tempi limitati e definiti**.  
Eventuali cambiamenti saranno quindi gestiti con attenzione, tenendo conto della fattibilità e delle tempistiche residue del progetto.  

---

## 14. Consegna
Il progetto sarà consegnato in duplice forma:  
- **Eseguibile funzionante (se implementata la GUI)**;  
- **Raccolta completa dei file di progetto (codice sorgente, configurazioni, risorse)**.  

È inoltre prevista una **presentazione formale** del progetto, durante la quale verranno illustrati i punti chiave e gli aspetti più significativi al revisore/professore.  

Oltre al software, saranno consegnati anche i **documenti prodotti** nelle fasi di progettazione e sviluppo, così da fornire una visione completa del lavoro svolto.  

**Obiettivo finale:** presentare un programma funzionante, compilabile e accompagnato dalla documentazione necessaria per comprenderne l’uso e la realizzazione.  
