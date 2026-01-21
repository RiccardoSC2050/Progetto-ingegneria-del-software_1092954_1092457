# Project Development Log
## Tracciato cronologico e organizzativo dello sviluppo
### Progetto: Gestione Dipendenti

Questo documento descrive il **percorso di sviluppo del progetto Gestione
Dipendenti**, ricostruito in modo cronologico e organizzativo a partire:

- dallo storico dei commit Git;
- dalle pull request;
- dalle issue chiuse;
- dalle milestone di progetto.

Lo scopo del documento è dimostrare:
- la **costanza del lavoro nel tempo**;
- la **collaborazione tra i membri del team**;
- l’evoluzione progressiva del sistema;
- l’assenza di sviluppo “tutto alla fine”.

Il tracciato presentato è coerente con lo storico del repository e con la
documentazione tecnica finale.

---

## 1. Fase di avvio e impostazione del progetto

### Periodo: fine settembre 2025

Lo sviluppo del progetto ha avuto inizio con una fase di avvio dedicata alla
creazione dell’infrastruttura di base.

In questo periodo sono stati effettuati:
- creazione del progetto Java;
- primo commit iniziale;
- aggiornamento del README;
- creazione della cartella di documentazione.

I commit di questa fase riflettono:
- l’impostazione dell’ambiente di sviluppo;
- la volontà di strutturare il progetto fin dall’inizio;
- l’attenzione alla documentazione sin dalle prime fasi.

Il lavoro è stato svolto in modo coordinato, con confronto iniziale sulle
scelte tecnologiche e organizzative.

---

## 2. Prime attività di progettazione e UML

### Periodo: inizio – metà ottobre 2025

Successivamente, il team ha concentrato le attività sulla progettazione del
sistema.

In questa fase:
- sono stati effettuati i primi tentativi di modellazione UML;
- sono stati creati diagrammi iniziali delle classi;
- è stato utilizzato Papyrus per la rappresentazione grafica;
- sono state corrette e adattate le classi effettivamente implementabili.

I commit mostrano:
- sperimentazione iniziale;
- correzioni progressive;
- allineamento tra modello e codice reale.

Questa fase è stata fondamentale per:
- comprendere il dominio;
- impostare la struttura delle classi;
- prevenire errori concettuali grossolani.

---

## 3. Inizio implementazione del core applicativo

### Periodo: metà ottobre 2025

Dopo la fase di progettazione iniziale, il team ha iniziato
l’implementazione del core del sistema.

Le attività principali includono:
- creazione delle prime classi concrete;
- definizione del main applicativo;
- implementazione dei metodi fondamentali;
- sistemazione delle annotazioni e della struttura del codice.

I commit di questo periodo mostrano:
- avanzamento graduale;
- piccoli passi incrementali;
- continua verifica del codice scritto.

Il lavoro è stato svolto in modo coordinato, con scambi frequenti sulle
decisioni implementative.

---

## 4. Sviluppo delle funzionalità principali e gestione utenti

### Periodo: fine ottobre – inizio novembre 2025

In questa fase il progetto entra nel vivo dello sviluppo funzionale.

Le attività principali sono state:
- implementazione delle funzionalità di gestione utenti;
- definizione delle classi di supporto;
- sviluppo delle operazioni principali;
- primi test manuali sul funzionamento del sistema.

I commit indicano:
- progressi costanti;
- introduzione di nuove funzionalità;
- sistemazione di problemi emersi durante l’uso del sistema.

Il lavoro di squadra è evidente dalla continuità dei commit e dalla
distribuzione delle attività.

---

## 5. Introduzione del database e primi test strutturati

### Periodo: inizio – metà novembre 2025

Con l’aumento della complessità del sistema, il team ha introdotto la gestione
del database.

Le attività includono:
- creazione di un database dedicato ai test;
- implementazione delle connessioni;
- gestione degli utenti persistenti;
- primi test JUnit su componenti chiave.

I commit di questo periodo mostrano:
- attenzione alla stabilità del sistema;
- introduzione graduale del testing;
- correzione di bug legati alla persistenza.

Questa fase segna l’inizio di un uso più sistematico del testing.

---

## 6. Rafforzamento del testing e stabilizzazione del main

### Periodo: metà – fine novembre 2025

Il team ha dedicato un periodo specifico al rafforzamento delle attività di
testing e alla stabilizzazione della logica centrale.

Sono state svolte le seguenti attività:
- incremento dei test unitari;
- test della logica del main;
- gestione dei casi limite;
- correzione di errori logici;
- refactoring mirati.

I commit evidenziano:
- messaggi espliciti legati al testing;
- sequenza logica di correzione → verifica;
- attenzione alla qualità complessiva del codice.

---

## 7. Lavoro costante e collaborazione documentata

Durante tutte le fasi sopra descritte:
- i commit sono stati frequenti;
- il lavoro è stato distribuito nel tempo;
- non si osservano lunghi periodi di inattività;
- le modifiche sono incrementali.

Questo dimostra:
- continuità nello sviluppo;
- collaborazione reale tra i membri del team;
- approccio serio e organizzato al progetto.

## 8. Introduzione e sviluppo della gestione CSV

### Periodo: fine novembre – inizio dicembre 2025

Dopo la stabilizzazione delle funzionalità principali e della gestione utenti,
il team ha avviato una fase specifica dedicata alla **gestione dei file CSV**.

Questa fase ha richiesto un cambio di prospettiva rispetto allo sviluppo
precedente, poiché ha introdotto:
- gestione di input esterni;
- parsing strutturato dei dati;
- conversione dei dati in strutture interne;
- integrazione con il database.

Le attività principali sono state:
- comprensione della struttura dei file CSV;
- definizione del formato dei dati;
- sviluppo dei metodi di lettura e scrittura;
- gestione degli errori e dei casi limite.

I commit di questo periodo mostrano chiaramente:
- una fase iniziale di studio e comprensione;
- successive applicazioni pratiche delle conoscenze acquisite;
- correzioni progressive legate a problemi reali di parsing e visualizzazione.

Il lavoro è stato svolto in modo collaborativo, con continui scambi per chiarire
il formato dei dati e le modalità di integrazione con il resto del sistema.

---

## 9. Testing avanzato e integrazione con la gestione CSV

### Periodo: inizio – metà dicembre 2025

Con l’introduzione della gestione CSV, il testing è diventato ancora più
centrale.

In questa fase il team ha:
- esteso i test esistenti;
- introdotto nuovi casi di test specifici per i CSV;
- verificato la coerenza tra dati importati e dati persistiti;
- testato il comportamento del sistema in presenza di file non validi.

I commit mostrano:
- riferimento esplicito al testing;
- correzione di bug legati a casi limite;
- miglioramento della robustezza del sistema.

Questa fase ha evidenziato:
- l’impatto diretto delle scelte progettuali sulla testabilità;
- la necessità di refactoring mirati;
- l’importanza di un testing strutturato per moduli complessi.

---

## 10. Periodo delle festività natalizie: sviluppo asincrono

### Periodo: metà dicembre 2025 – inizio gennaio 2026

Durante il periodo delle festività natalizie, il ritmo delle comunicazioni
sincrone tra i membri del team è diminuito.

Tuttavia:
- lo sviluppo del progetto non si è fermato;
- il lavoro è proseguito in modo asincrono;
- sono stati effettuati commit significativi;
- sono state chiuse diverse issue rilevanti.

In questo periodo sono state svolte attività quali:
- sistemazione di metodi esistenti;
- miglioramento della visualizzazione dei dati CSV;
- gestione di eccezioni residue;
- riorganizzazione di package e nomi di file.

I commit di questo periodo dimostrano:
- continuità del lavoro anche in assenza di incontri frequenti;
- capacità di procedere individualmente mantenendo coerenza;
- successiva integrazione delle modifiche nel branch principale.

Questo comportamento riflette una dinamica realistica di lavoro in contesto
universitario.

---

## 11. Introduzione e sviluppo della GUI Java

### Periodo: dicembre 2025 – inizio gennaio 2026

In una fase successiva, il team ha avviato lo sviluppo della **GUI Java**, con
l’obiettivo di fornire un’interfaccia alternativa al main testuale.

Le attività principali includono:
- progettazione della GUI come falso terminale;
- implementazione della schermata principale;
- sviluppo dei controller;
- creazione di un ponte verso il main originale.

I commit indicano:
- avanzamento graduale della GUI;
- completamento delle funzionalità principali;
- integrazione senza duplicazione della logica di business.

La GUI è stata sviluppata in modo coerente con l’architettura esistente, seguendo
il pattern **Model–View–Controller**.

---

## 12. Testing finale e consolidamento del progetto

### Periodo: gennaio 2026

Nella fase finale del progetto, il focus si è spostato sul **consolidamento
complessivo** del sistema.

Le attività includono:
- revisione finale del codice;
- completamento dei test rimanenti;
- verifica della copertura dei test;
- sistemazione di bug residui;
- merge delle ultime pull request.

I commit di questa fase mostrano:
- messaggi espliciti di completamento del testing;
- revisione e sistemazione finale;
- conferma che i test del progetto sono stati completati.

Questa fase rappresenta la chiusura formale dello sviluppo.

---

## 13. Collaborazione e lavoro di squadra

L’analisi complessiva dello storico dei commit mostra che:

- il lavoro è stato distribuito nel tempo;
- non sono presenti lunghi periodi di inattività;
- i contributi sono progressivi e incrementali;
- le attività sono state coordinate tramite issue e pull request.

La collaborazione tra i membri del team è stata:
- continua;
- concreta;
- orientata alla risoluzione dei problemi.

Questo dimostra un reale lavoro di squadra e non uno sviluppo individuale.

---

## 14. Considerazioni finali sul percorso di sviluppo

Il percorso di sviluppo del progetto **Gestione Dipendenti** può essere
riassunto come:

- strutturato;
- progressivo;
- coerente;
- tracciabile.

Lo storico dei commit e delle milestone dimostra che:
- il progetto è cresciuto nel tempo;
- le funzionalità sono state aggiunte gradualmente;
- il testing ha accompagnato lo sviluppo;
- la documentazione è stata aggiornata in modo coerente.

---

## 15. Conclusione

Questo documento fornisce una rappresentazione fedele e organizzata del
percorso di sviluppo del progetto **Gestione Dipendenti**.

Insieme alla documentazione tecnica e al codice sorgente, dimostra:
- costanza nel lavoro;
- collaborazione tra i membri del team;
- capacità di gestire un progetto software nel tempo.

Il tracciato qui descritto conclude la rappresentazione dello sviluppo del
progetto.
