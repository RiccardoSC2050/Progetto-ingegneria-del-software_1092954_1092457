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


