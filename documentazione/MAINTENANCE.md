# Software Maintenance & Refactoring
## Gestione Dipendenti

Questo documento descrive le attività di **manutenzione del software** svolte
durante lo sviluppo del progetto **Gestione Dipendenti**, in conformità con
le linee guida del corso di **Ingegneria del Software (AA 2025/26)**.

La manutenzione è intesa non come semplice correzione di errori, ma come
attività sistematica di **miglioramento della qualità del software**, della
sua struttura e della sua manutenibilità.

---

## 1. Obiettivi della manutenzione

Gli obiettivi principali delle attività di manutenzione sono:
- migliorare la **leggibilità e comprensibilità del codice**;
- ridurre il **debito tecnico**;
- aumentare la **manutenibilità** del sistema;
- facilitare l’estensione futura del software;
- migliorare l’allineamento tra architettura progettata e implementazione.

La manutenzione è considerata una parte integrante del ciclo di vita del
software e non una fase secondaria.

---

## 2. Tipologie di manutenzione adottate

Nel progetto sono state applicate principalmente le seguenti tipologie di
manutenzione.

### 2.1 Manutenzione correttiva

La manutenzione correttiva riguarda:
- correzione di errori logici individuati durante lo sviluppo;
- risoluzione di bug emersi durante il testing;
- gestione di casi limite non previsti inizialmente.

Queste attività sono state svolte in modo incrementale e tracciate tramite
commit Git.

---

### 2.2 Manutenzione migliorativa (refactoring)

La manutenzione migliorativa ha avuto un ruolo centrale nel progetto ed è
stata realizzata principalmente tramite **attività di refactoring**.

Il refactoring è stato utilizzato per:
- migliorare la struttura del codice;
- ridurre duplicazioni;
- chiarire le responsabilità delle classi;
- migliorare la coesione dei package.

---

## 3. Attività di refactoring principali

### 3.1 Riorganizzazione dei package

Una delle principali attività di manutenzione ha riguardato la
**riorganizzazione dei package**, al fine di:
- riflettere meglio l’architettura logica del sistema;
- separare le responsabilità;
- ridurre accoppiamenti indesiderati.

La struttura è stata progressivamente allineata a una suddivisione logica
ispirata a:
- domain
- application
- infrastructure
- presentation (CLI / GUI a falso terminale)

---

### 3.2 Miglioramento della separazione Domain / Application

Durante lo sviluppo è emersa una difficoltà concreta nel mantenere una
separazione perfettamente rigida tra **domain layer** e **application layer**.

Questa criticità è stata affrontata in modo consapevole:
- cercando di spostare la logica di business nel dominio;
- limitando la presenza di logica applicativa nelle entità;
- mantenendo l’orchestrazione dei casi d’uso nel livello applicativo.

Pur non raggiungendo una separazione ideale in ogni punto, il team ha
perseguito con decisione un approccio **professionale e realistico**, coerente
con la dimensione e il contesto del progetto.

---

### 3.3 Rinomina di classi e metodi

Sono state effettuate attività di:
- rinomina di classi;
- rinomina di metodi;
- standardizzazione della nomenclatura.

Queste modifiche hanno migliorato:
- la leggibilità del codice;
- la comprensione delle responsabilità;
- la coerenza con le convenzioni Java.

---

### 3.4 Pulizia e semplificazione del codice

Ulteriori attività di manutenzione hanno incluso:
- rimozione di codice non utilizzato;
- semplificazione di metodi troppo complessi;
- suddivisione di metodi con responsabilità multiple;
- miglioramento dei commenti e della documentazione inline.

---

## 4. Collegamento con architettura e testing

Le attività di manutenzione sono state svolte in stretta relazione con:
- la revisione dell’architettura;
- l’evoluzione dei diagrammi UML;
- le attività di testing.

Il refactoring è stato spesso guidato dai risultati dei test:
- errori individuati → analisi → modifica del codice → riesecuzione dei test.

Questo approccio garantisce:
- maggiore affidabilità;
- riduzione del rischio di regressioni;
- miglioramento continuo della qualità.

---

## 5. Tracciabilità delle attività di manutenzione

Le attività di manutenzione sono tracciabili tramite:
- commit Git descrittivi;
- branch dedicati;
- pull request di integrazione.

Questo consente di:
- ricostruire l’evoluzione del sistema;
- comprendere le motivazioni delle modifiche;
- facilitare la revisione in fase di valutazione.

---

## 6. Ruolo della manutenzione nel ciclo di vita

La manutenzione ha accompagnato l’intero ciclo di sviluppo del progetto e non
è stata relegata a una fase finale.

Questo approccio riflette una visione moderna dell’ingegneria del software,
in cui:
- il codice è costantemente migliorato;
- la qualità è un obiettivo continuo;
- il software è considerato un prodotto evolutivo.

---

## 7. Conclusioni

Le attività di manutenzione e refactoring svolte nel progetto
Gestione Dipendenti dimostrano:
- attenzione alla qualità del codice;
- consapevolezza architetturale;
- capacità di migliorare software esistente.

Questo documento completa la documentazione di progetto, evidenziando come il
team abbia applicato in modo concreto i principi della **Software Maintenance**
studiati durante il corso.
