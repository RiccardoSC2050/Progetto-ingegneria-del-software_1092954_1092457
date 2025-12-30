# Project Collaboration, Scrum Management & Development Timeline

## 1. Introduzione

Il presente documento descrive il processo di sviluppo del progetto di
Ingegneria del Software, con particolare attenzione alla gestione del lavoro,
alla collaborazione tra i membri del team e all’adozione di un approccio
incrementale ispirato al framework Scrum.

Il README ha lo scopo di:
- documentare il metodo di lavoro adottato;
- evidenziare la collaborazione effettiva tra i membri del team;
- collegare le attività svolte ai commit Git e alle issue di progetto;
- fornire una visione temporale e strutturata dell’evoluzione del sistema.

Tutte le informazioni riportate sono derivate esclusivamente dall’analisi
della cronologia dei commit, delle pull request e delle issue presenti nel
repository GitHub del progetto.

---

## 2. Team di progetto

Il team di progetto è composto da due membri che hanno collaborato
attivamente durante l’intero ciclo di sviluppo.

La collaborazione è avvenuta principalmente tramite:
- repository Git condiviso;
- utilizzo di branch dedicati alle funzionalità;
- pull request per l’integrazione del codice;
- issue GitHub per il tracciamento delle attività;
- revisione e refactoring incrementale del codice.

Questo approccio ha permesso una comunicazione continua e verificabile,
riducendo la necessità di riunioni formali frequenti e favorendo un modello
di coordinamento asincrono.

---

## 3. Metodo di lavoro adottato

Il progetto è stato sviluppato seguendo un approccio ispirato al framework
Scrum, adattato a un contesto di team ridotto.

Le caratteristiche principali del metodo adottato sono:
- sviluppo incrementale;
- iterazioni successive (sprint);
- ispezione e adattamento continuo;
- rilascio progressivo delle funzionalità;
- miglioramento iterativo del codice.

Lo sviluppo non è stato di tipo sequenziale (waterfall), ma ha seguito un
modello evolutivo, come dimostrato dalla ripetizione controllata di attività
quali testing, refactoring, miglioramento della logica e revisione
architetturale.

---

## 4. Scrum events (interpretazione dai dati Git)

In assenza di cerimonie Scrum formali e sincrone, gli eventi Scrum sono stati
realizzati in forma **asincrona**, sfruttando le funzionalità offerte da Git
e GitHub.

### 4.1 Sprint Planning

Lo Sprint Planning può essere identificato:
- all’inizio di ogni periodo di sviluppo significativo;
- nella creazione di nuove issue;
- nell’apertura di branch dedicati a specifiche funzionalità.

Ogni sprint è stato caratterizzato da uno o più obiettivi tecnici ben definiti,
come l’implementazione della core logic, la gestione del login, l’integrazione
del database o lo sviluppo della GUI.

---

### 4.2 Daily Scrum (asincrono)

Il Daily Scrum è stato realizzato in modo asincrono tramite:
- commit frequenti e progressivi;
- messaggi di commit descrittivi;
- aggiornamenti incrementali del codice.

La frequenza dei commit dimostra una comunicazione continua tra i membri del
team e un costante allineamento sullo stato di avanzamento delle attività.

---

### 4.3 Sprint Review

La Sprint Review è identificabile:
- nel completamento di una funzionalità;
- nella chiusura delle issue;
- nel merge delle pull request verso il branch principale.

Questi momenti rappresentano la verifica del lavoro svolto e l’accettazione
delle funzionalità implementate.

---

### 4.4 Sprint Retrospective

La Sprint Retrospective emerge indirettamente attraverso:
- attività di refactoring;
- rinomina di classi e package;
- miglioramenti architetturali;
- pulizia generale del codice;
- correzione di errori e debito tecnico.

Queste attività dimostrano una riflessione continua sulle scelte progettuali
e una volontà di migliorare la qualità complessiva del sistema.

---

## 5. Avvio del progetto (Sprint 0)

### Periodo
23 settembre 2025 – 25 settembre 2025

### Evidenze dai commit
- Initial commit
- Creazione del progetto Java
- Primo aggiornamento del README
- Aggiunta della cartella di documentazione

### Obiettivi dello sprint
- inizializzazione del repository;
- configurazione dell’ambiente di sviluppo;
- definizione della struttura di base del progetto;
- predisposizione della documentazione iniziale.

### Stato
Sprint completato con successo.

Questo sprint rappresenta la fase di bootstrap del progetto e costituisce la
base per tutte le iterazioni successive.

---

## 6. Modellazione e architettura del sistema (Sprint 1)

### Periodo
1 ottobre 2025 – 17 ottobre 2025

### Evidenze dai commit
- UML-project
- Papyrus Class Diagram
- Fix bug che non mostrava il grafico
- Correzioni a classi effettivamente realizzate
- Sistemazione delle classi Root e dei relativi metodi

### Obiettivi dello sprint
- analizzare il dominio applicativo;
- modellare il sistema tramite diagrammi UML;
- definire le principali classi del dominio;
- impostare l’architettura generale del sistema;
- allineare il modello concettuale con l’implementazione futura.

### Attività svolte
Durante questo sprint il team ha concentrato il lavoro sulla fase di
modellazione, utilizzando UML come strumento di supporto alla progettazione.
I diagrammi sono stati progressivamente raffinati, correggendo incongruenze
emerse durante la revisione e allineando il modello alle reali esigenze del
progetto.

La presenza di commit di correzione e aggiornamento dei diagrammi indica un
processo di progettazione iterativo, in cui il modello è stato adattato e
migliorato sulla base delle decisioni tecniche emerse.

### Stato
Sprint completato.

---

## 7. Implementazione delle classi iniziali e core logic (Sprint 2)

### Periodo
18 ottobre 2025 – 4 novembre 2025

### Evidenze dai commit
- Starting to code initial classes
- Root and their methods
- Add check accesslevel method
- Complete all setup
- Merge pull request da branch di coding e database

### Obiettivi dello sprint
- tradurre il modello UML in codice;
- implementare le classi principali del dominio;
- sviluppare la logica centrale dell’applicazione;
- impostare i primi meccanismi di controllo e accesso.

### Attività svolte
In questo sprint è iniziata l’implementazione concreta del sistema. Le classi
principali identificate nella fase di modellazione sono state codificate e
dotate dei primi metodi funzionali.

La logica del sistema è stata costruita in modo incrementale, con commit
successivi che mostrano un’evoluzione progressiva del codice piuttosto che
un’implementazione monolitica.

L’integrazione tramite pull request evidenzia una collaborazione strutturata
tra i membri del team e un controllo sull’inserimento del codice nel branch
principale.

### Stato
Sprint completato.

---

## 8. Gestione login, database e testing iniziale (Sprint 3)

### Periodo
5 novembre 2025 – 21 novembre 2025

### Evidenze dai commit
- Gestione login
- Bisogna gestire ancora il login
- Finito login
- Accesso al sistema
- Metodi database
- Root testing
- Testing operators
- Chiusura issue “testing + database (operators)”

### Obiettivi dello sprint
- implementare il sistema di login;
- gestire l’accesso controllato al sistema;
- integrare il database;
- avviare le attività di testing funzionale;
- verificare il corretto comportamento delle classi principali.

### Attività svolte
Lo sviluppo del login è avvenuto in modo iterativo, come dimostrato dalla
presenza di più commit dedicati alla stessa funzionalità. Questo approccio è
coerente con Scrum e con la gestione incrementale delle funzionalità.

Parallelamente, è stata integrata la gestione del database e sono stati avviati
test specifici sulle classi Root e sugli operatori. La chiusura delle issue
associate indica il raggiungimento degli obiettivi prefissati per lo sprint.

### Stato
Sprint completato.

---

## 9. Consolidamento, refactoring e miglioramenti architetturali (Sprint 4)

### Periodo
22 novembre 2025 – 7 dicembre 2025

### Evidenze dai commit
- Sistemazione quasi definitiva
- Fix testing with hash code for password
- Resolve merge conflicts with main
- Fixed some architectural code
- MAYBE HUGE FIX UPDATE
- Pulizia generale
- Controllo e fix

### Obiettivi dello sprint
- ridurre il debito tecnico;
- migliorare la qualità del codice;
- correggere errori emersi durante il testing;
- rendere l’architettura più robusta;
- preparare il sistema a nuove funzionalità.

### Attività svolte
Questo sprint è stato fortemente orientato alla qualità. Le attività di
refactoring e pulizia del codice dimostrano una fase di riflessione critica
sulle scelte precedenti.

La risoluzione dei conflitti di merge e i fix architetturali indicano un
processo di integrazione attento e una gestione consapevole dell’evoluzione
del sistema.

### Stato
Sprint completato.

---

## 10. Gestione CSV e funzionalità avanzate sui dati (Sprint 5)

### Periodo
8 dicembre 2025 – 23 dicembre 2025

### Evidenze dai commit
- Sviluppo gestione CSV con data
- Correzione visualizzazione CSV
- Gestione spazi indesiderati
- Aggiunta metodi di lettura file utenti
- Aggiunta metodi statistici
- Issue “sviluppo gestione csv con data” (75% completata)

### Obiettivi dello sprint
- implementare la gestione di file CSV;
- integrare i dati esterni con il database;
- migliorare l’elaborazione dei dati;
- introdurre funzionalità statistiche.

### Attività svolte
La gestione dei file CSV è stata sviluppata in modo incrementale, con più
commit dedicati a miglioramenti progressivi e correzioni.

La percentuale di completamento delle issue indica che alcune attività sono
state completate mentre altre risultano ancora in corso, in linea con la
natura incrementale del processo Scrum.

### Stato
Sprint parzialmente completato.

---

## 11. Sviluppo interfaccia grafica (GUI) e rifiniture finali (Sprint 6)

### Periodo
24 dicembre 2025 – in corso

### Evidenze dai commit
- Java GUI branch
- Schermata GUI
- GUI completata
- Aggiornato man
- Sistemazione alcuni metodi
- Gestione ultima eccezione
- Aggiunta modifica A.L.
- Issue “java gui” (66% completata)

### Obiettivi dello sprint
- progettare e implementare l’interfaccia grafica;
- collegare la GUI alla logica di business esistente;
- migliorare l’usabilità del sistema;
- gestire eccezioni e casi limite;
- rifinire la documentazione operativa.

### Attività svolte
In questo sprint il team ha avviato lo sviluppo della GUI, lavorando su un
branch dedicato. L’interfaccia grafica è stata implementata in modo
incrementale, partendo dalle schermate principali fino alla gestione delle
interazioni più complesse.

I commit mostrano un’alternanza tra sviluppo funzionale e rifinitura, con
particolare attenzione alla gestione delle eccezioni e al miglioramento
dell’esperienza utente.

L’aggiornamento della documentazione (“man”) indica una fase di consolidamento
e preparazione all’uso del sistema da parte dell’utente finale.

### Stato
Sprint in corso.

---


