# Design and Architecture
## Gestione Dipendenti

Questo documento descrive le **scelte di design e architettura software**
adottate per il progetto **Gestione Dipendenti**, sviluppato nell’ambito del
corso di **Ingegneria del Software (AA 2025/26)**.

Il documento nasce inizialmente come descrizione delle scelte architetturali
preliminari, ma è stato successivamente **esteso e consolidato** per riflettere
l’evoluzione reale del progetto fino alla sua conclusione.

Non si tratta quindi di un design puramente teorico, ma di un **design
a posteriori consapevole**, che integra:
- progettazione iniziale;
- implementazione effettiva;
- risultati del testing;
- difficoltà incontrate;
- compromessi architetturali adottati.

Il contenuto è coerente e allineato con:
- `PROJECT_PLAN.md`
- `SRC.md`
- `SCRUM_MANAGEMENT.md`
- `TEST_MANAGEMENT.md`
- `MAINTENANCE.md`

---

## 1. Visione architetturale generale (estesa)

Il sistema **Gestione Dipendenti** è stato progettato seguendo un’architettura
modulare, ispirata a una suddivisione a livelli, con l’obiettivo di:

- separare le responsabilità;
- ridurre la complessità cognitiva;
- favorire la manutenibilità;
- rendere il sistema estendibile.

Fin dalle prime fasi di progettazione si è cercato di distinguere tra:

- **Domain layer**
- **Application layer**
- **Infrastructure / Data layer**
- **Presentation layer (CLI / GUI)**

Questa separazione è stata perseguita come **obiettivo architetturale**, pur
consapevoli delle difficoltà pratiche legate a un progetto accademico di media
complessità.

L’architettura finale rappresenta un **compromesso realistico** tra:
- correttezza teorica;
- tempo disponibile;
- complessità tecnologica;
- maturità delle competenze.

---

## 2. Separazione Domain / Application: obiettivo e realtà

### 2.1 Intento progettuale iniziale

L’intento iniziale era mantenere una separazione netta tra:

- **Domain**: entità, regole di business, modelli concettuali
- **Application**: casi d’uso, orchestrazione delle operazioni, coordinamento

Questo approccio era motivato da:
- miglior testabilità;
- riduzione dell’accoppiamento;
- maggiore chiarezza architetturale;
- possibilità di evoluzione futura.

---

### 2.2 Problemi emersi durante lo sviluppo

Con l’aumentare delle funzionalità implementate, è emerso che la **quantità di
responsabilità** concentrate nel sistema ha reso difficile mantenere una
separazione perfettamente pulita.

In particolare:
- alcune classi hanno iniziato a contenere logica mista;
- alcune operazioni applicative richiedevano conoscenza diretta del dominio;
- il domain layer, in certi punti, è risultato influenzato da scelte applicative.

Questo ha portato a:
- **accoppiamento eccessivo domain–application**
- difficoltà nella scrittura di test unitari puri
- necessità di refactoring progressivo

È importante sottolineare che questi problemi **non derivano da assenza di
design**, ma da un tentativo consapevole di applicare principi avanzati in un
contesto didattico complesso.

---

## 3. Function Decomposition e Object-Oriented Design (approfondimento)

### 3.1 Approccio ibrido adottato

Durante il progetto è stato adottato un approccio ibrido che combina:

- **Function Decomposition**
- **Object-Oriented Design**

La function decomposition è stata utilizzata per:
- scomporre problemi complessi;
- isolare operazioni logiche;
- rendere il codice più leggibile.

L’orientamento agli oggetti è stato invece usato per:
- modellare il dominio;
- rappresentare entità e relazioni;
- incapsulare lo stato.

---

### 3.2 Squilibri emersi

Nel corso dello sviluppo è emerso che la combinazione dei due approcci non è
sempre stata equilibrata.

In particolare:
- alcune classi hanno accumulato un numero elevato di metodi;
- alcuni metodi sono diventati troppo complessi;
- la decomposizione top-down non sempre si è integrata bene con l’aggregazione
  bottom-up tipica dell’OOP.

Questo ha avuto impatto diretto su:
- testabilità dei moduli;
- leggibilità del codice;
- necessità di refactoring in fase di testing.

---

## 4. Modularità, coesione e accoppiamento (rafforzamento)

Il progetto ha cercato di applicare i principi di:

- modularità
- alta coesione
- basso accoppiamento

In molti casi questi obiettivi sono stati raggiunti, ma in altri:
- la pressione funzionale ha portato a compromessi;
- la separazione tra moduli è risultata meno netta.

L’esperienza ha mostrato come:
> la modularità non sia solo una scelta progettuale iniziale,  
> ma una disciplina da mantenere durante tutto lo sviluppo.

---

## 5. Sicurezza: scelte, limiti e consapevolezza

La sicurezza è stata affrontata con un approccio pragmatico e coerente con il
contesto del progetto.

Sono state adottate le seguenti misure:
- hashing delle password;
- uso di UUID per identificazione utenti;
- password minima di 8 caratteri;
- memorizzazione dei dati sensibili solo nel database.

Tuttavia, l’analisi finale ha evidenziato una criticità importante:

- la verifica delle credenziali non è sempre centralizzata;
- alcuni metodi accedono ai dati senza passare da un unico punto di controllo;
- questo può introdurre potenziali vulnerabilità logiche.

Questa problematica è **riconosciuta e documentata** come limite
architetturale e come area di miglioramento futuro.

---

## 6. Spring Boot: uso professionale e limiti di contesto

Spring Boot è stato utilizzato come framework principale per:

- gestione del ciclo di vita dell’applicazione;
- dependency injection;
- configurazione centralizzata;
- integrazione con il database.

L’uso di Spring Boot è stato:
- consapevole;
- coerente;
- aderente alle best practice di base.

Non è stata sviluppata una web application REST:
- per scelta progettuale;
- per limitare la complessità;
- per mantenere il focus sul dominio.

L’architettura rimane comunque predisposta a una futura estensione REST.

---

## 7. GUI a falso terminale e MVC (integrazione finale)

La GUI è stata progettata come **falso terminale**, non come interfaccia grafica
tradizionale.

Questa scelta ha permesso di:
- mantenere la logica di business invariata;
- evitare duplicazioni;
- creare un ponte verso il main originale.

La GUI segue il pattern **Model–View–Controller**, con:
- Model: dominio e servizi
- View: terminale simulato
- Controller: gestione input e flussi

La GUI non è stata sottoposta a testing automatico per:
- vincoli di tempo;
- complessità degli strumenti;
- priorità accademiche su altri moduli

Questa scelta è **dichiarata e motivata**, non nascosta.

---

## 8. UML come strumento di supporto reale

L’UML non è stato usato come semplice adempimento formale, ma come:
- strumento di comprensione;
- guida alla progettazione;
- mezzo di comunicazione tra i membri del team.

I diagrammi sono stati:
- creati progressivamente;
- aggiornati durante lo sviluppo;
- verificati a progetto concluso.

---

## 9. Relazione tra design, testing e manutenzione

Il design ha influenzato direttamente:
- la qualità del testing;
- la facilità di manutenzione.

Il testing ha a sua volta:
- evidenziato problemi architetturali;
- guidato refactoring mirati;
- rafforzato la consapevolezza progettuale.

Questo ciclo continuo rappresenta uno degli aspetti più importanti del progetto
dal punto di vista dell’ingegneria del software.

## 10. Testing come strumento di validazione del design

Durante lo sviluppo del progetto, il testing non è stato considerato
semplicemente come una fase di verifica funzionale, ma come un vero e proprio
**strumento di validazione del design architetturale**.

L’esecuzione dei test ha permesso di evidenziare:

- classi con responsabilità eccessive;
- metodi troppo complessi;
- accoppiamenti non immediatamente visibili;
- difficoltà nella separazione dei livelli.

In particolare, le difficoltà di testing emerse hanno confermato che alcune
scelte di design iniziali, pur corrette dal punto di vista concettuale, hanno
mostrato limiti nella loro applicazione pratica.

---

## 11. Impatto della complessità sul testing

La complessità del sistema ha influito direttamente sulla strategia di testing.

Sono risultati facilmente testabili:
- moduli con responsabilità ben definite;
- classi con funzioni semplici e coese;
- componenti con dipendenze limitate.

Sono risultati invece più complessi da testare:
- moduli con forte interazione tra domain e application;
- metodi che combinano più operazioni (file system, parsing, database);
- flussi applicativi articolati.

Questo ha portato alla consapevolezza che:
> un design apparentemente corretto può risultare fragile se non supportato
> da una decomposizione adeguata delle responsabilità.

---

## 12. Copertura del testing e relazione con il design

Il progetto ha raggiunto una **copertura dei test superiore al 75% per ciascun
modulo e package**, escludendo consapevolmente la GUI.

Questa copertura è stata ottenuta tramite:
- test JUnit;
- testing incrementale;
- test focalizzati sui metodi critici;
- verifica dei casi limite.

La copertura non è stata perseguita come obiettivo numerico fine a sé stesso,
ma come indicatore della **qualità del design**.

Dove la copertura risultava difficile da aumentare, spesso la causa era da
ricercare in:
- design poco modulare;
- metodi troppo complessi;
- accoppiamento eccessivo.

---

## 13. Testing della GUI: scelta progettuale motivata

La GUI a falso terminale **non è stata sottoposta a testing automatico**.

Questa scelta è stata:
- consapevole;
- documentata;
- motivata.

Le ragioni principali sono state:
- elevato costo temporale per apprendere strumenti di GUI testing;
- priorità accademiche legate ad altri esami;
- focus del corso sulla progettazione e sul testing della logica applicativa.

La GUI è stata comunque:
- verificata manualmente;
- validata funzionalmente;
- integrata correttamente con il sistema esistente.

Dal punto di vista del design, la separazione MVC e l’uso del ponte verso il
main originale garantiscono che:
- l’assenza di test sulla GUI non comprometta la correttezza della logica;
- la parte critica del sistema sia comunque testata in modo esteso.

---

## 14. Uso consapevole del supporto esterno (vibe coding)

Nel progetto è stato utilizzato il **vibe coding** come strumento di supporto,
non come sostituto dello sviluppatore.

Il suo utilizzo ha riguardato principalmente:
- comprensione di framework complessi (Spring Boot);
- supporto nella scrittura di test complessi;
- gestione di casi particolari (CSV, byte, file system);
- miglioramento della struttura della documentazione.

È importante sottolineare che:
- la progettazione architetturale è stata svolta dal team;
- la maggior parte del codice è stata scritta manualmente;
- il vibe coding è stato usato come strumento di apprendimento e supporto.

Questa scelta è coerente con un approccio moderno e responsabile allo sviluppo
software.

---

## 15. Evoluzione del design nel tempo

Il design del progetto **non è rimasto statico**, ma si è evoluto nel tempo.

In particolare:
- alcune scelte iniziali sono state riviste;
- alcune soluzioni sono state raffinate;
- alcuni compromessi sono stati accettati consapevolmente.

L’evoluzione del design è stata guidata da:
- risultati del testing;
- feedback derivanti dall’uso del sistema;
- difficoltà pratiche incontrate;
- necessità di completare il progetto in modo coerente.

Questo processo riflette un ciclo di vita realistico del software.

---

## 16. Relazione tra design e manutenzione

Il design ha influenzato direttamente le attività di manutenzione.

In particolare:
- moduli ben progettati hanno richiesto pochi interventi;
- moduli complessi hanno richiesto refactoring ripetuti;
- la chiarezza del design ha facilitato la correzione dei bug.

Le attività di manutenzione hanno a loro volta contribuito a:
- migliorare il design;
- ridurre la complessità;
- rendere il codice più leggibile e stabile.

Design e manutenzione sono stati quindi strettamente interconnessi.

---

## 17. Valutazione critica finale del design

Il design finale del progetto **Gestione Dipendenti** presenta:

### Punti di forza
- architettura complessivamente modulare;
- uso consapevole dei framework;
- separazione logica tra componenti;
- buona testabilità della logica applicativa;
- documentazione coerente con il codice.

### Limiti riconosciuti
- separazione non perfetta tra domain e application;
- complessità eccessiva in alcuni moduli;
- testing automatico assente sulla GUI;
- gestione della sicurezza migliorabile.

Questi limiti sono stati:
- riconosciuti;
- documentati;
- contestualizzati.

---

## 18. Conclusioni sul design

Il design del progetto **Gestione Dipendenti** rappresenta il risultato di un
percorso di apprendimento e applicazione pratica dei principi di
**Ingegneria del Software**.

Pur non essendo perfetto, il design è:
- coerente;
- completo;
- realistico;
- difendibile in un contesto accademico.

Il progetto dimostra la capacità del team di:
- progettare sistemi complessi;
- affrontare difficoltà tecniche reali;
- riflettere criticamente sulle scelte effettuate;
- documentare in modo trasparente il lavoro svolto.

