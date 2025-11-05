# Documento di Stima dei Costi e delle Risorse

## 1. Introduzione

Il presente documento ha lo scopo di stimare i **costi teorici** e le **risorse necessarie** per la realizzazione del progetto *Gestione Dipendenti*.
L’obiettivo è fornire una valutazione quantitativa e realistica dell’impegno richiesto in termini di **ore/uomo**, **costi teorici**, e **durata complessiva**, in coerenza con il *Piano di Progetto* (capitolo 12) e con le linee guida dei modelli di stima **COCOMO** e **Function Point Analysis (FPA)**.

Il progetto ha una durata stimata di **circa 4 mesi**, con un team composto da due sviluppatori principali e la supervisione accademica del Prof. Gargantini.

---

## 2. Metodologia di stima

Sono stati utilizzati due approcci complementari:

1. **COCOMO Semplificato (Constructive Cost Model)** → per stimare lo sforzo in persone-mese (PM).
2. **Function Point Analysis (FPA)** → per stimare la complessità funzionale e validare la dimensione del sistema.

I risultati sono integrati in una **stima a sistema** per ore e costi complessivi.

---

## 3. Metodo 1 – Modello COCOMO semplificato

### 3.1 Formula

[ PM = a \times (KLOC)^b ]
[ TDEV = c \times (PM)^d ]

### 3.2 Coefficienti di riferimento

| Tipo di progetto | a       | b        | c       | d        |
| ---------------- | ------- | -------- | ------- | -------- |
| **Organico**     | **2.4** | **1.05** | **2.5** | **0.38** |
| Semi-Detached    | 3.0     | 1.12     | 2.5     | 0.35     |
| Embedded         | 3.6     | 1.20     | 2.5     | 0.32     |

**Valori adottati per il progetto:** Organico → a = **2.4**, b = **1.05**, c = **2.5**, d = **0.38**

### 3.3 Calcoli

| Parametro                 | Valore                |
| ------------------------- | --------------------- |
| KLOC                      | 4                     |
| PM = 2.4 × (4)^1.05       | **10.3 persone-mese** |
| TDEV = 2.5 × (10.3)^0.38  | **5.6 mesi-persona**  |
| Ore totali (10.3 × 160)   | **1.648 ore**         |
| Ore per sviluppatore (÷2) | **≈ 825 ore**         |

### 3.4 Fattori COCOMO esclusi

| Categoria                         | Fattori esclusi                                                                                | Motivazione                                                                     |
| --------------------------------- | ---------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------- |
| Cost Drivers (Effort Multipliers) | RELY, DATA, CPLX, TIME, STOR, RUSE, DOCU, PVOL, ACAP, PCAP, AEXP, PEXP, LTEX, TOOL, SITE, SCED | Assenza di dati storici e team ridotto (progetto accademico)                    |
| Scale Factors (COCOMO II)         | PREC, FLEX, RESL, TEAM, PMAT                                                                   | Non applicabili a team ≤ 2; rischio già mitigato dal modello di processo ibrido |

---

## 4. Metodo 2 – Function Point Analysis (FPA)

### 4.1 Tabelle ufficiali dei pesi di complessità

| Tipo funzione                      | Bassa | Media | Alta |
| ---------------------------------- | ----- | ----- | ---- |
| **External Inputs (EI)**           | 3     | 4     | 6    |
| **External Outputs (EO)**          | 4     | 5     | 7    |
| **External Inquiries (EQ)**        | 3     | 4     | 6    |
| **Internal Logical Files (ILF)**   | 7     | 10    | 15   |
| **External Interface Files (EIF)** | 5     | 7     | 10   |

### 4.2 Funzioni individuate nel progetto

| Funzione                                    | Tipo | Complessità   | Peso | Totale |
| ------------------------------------------- | ---- | ------------- | ---- | ------ |
| Gestione dipendenti (CRUD)                  | EI   | **Medio (4)** | 4    | 4      |
| Ricerca e filtri dipendenti                 | EQ   | **Medio (4)** | 4    | 4      |
| Gestione utenti e ruoli                     | EI   | **Alto (6)**  | 6    | 6      |
| Statistiche e report                        | EO   | **Medio (5)** | 5    | 5      |
| Import/Export CSV                           | EIF  | **Basso (3)** | 3    | 3      |
| Login e sicurezza                           | EI   | **Alto (6)**  | 6    | 6      |
| API REST / Database                         | EIF  | **Medio (4)** | 4    | 4      |
| **Totale UFP (Unadjusted Function Points)** |      |               |      | **32** |

### 4.3 Calcolo del Value Adjustment Factor (VAF)

| Fattore                          | Valore (0–5) |
| -------------------------------- | ------------ |
| Comunicazioni dati               | 3            |
| Funzionalità distribuita         | 2            |
| Prestazioni richieste            | 3            |
| Efficienza utente finale         | 3            |
| Complessità elaborazioni interne | 3            |
| Riusabilità del codice           | 2            |
| Facilità d’installazione         | 3            |
| Facilità d’uso                   | 4            |
| Portabilità                      | 3            |
| Scalabilità futura               | 3            |
| **Somma totale (VAF)**           | **20**       |

**Fattore di aggiustamento = (0.65 + 0.01 × 20) = 0.85**
**Function Points finali (FP) = 32 × 0.85 = 27.2**
**Conversione LOC (50 LOC/FP) = ≈ 1.360 LOC**

---

## 5. Riepilogo valori numerici

### 5.1 COCOMO

| Parametro  | Valore                |
| ---------- | --------------------- |
| a          | **2.4**               |
| b          | **1.05**              |
| c          | **2.5**               |
| d          | **0.38**              |
| KLOC       | **4**                 |
| PM         | **10.3 persone-mese** |
| TDEV       | **5.6 mesi-persona**  |
| Ore totali | **1.648**             |

### 5.2 Function Point Analysis

| Parametro                | Valore      |
| ------------------------ | ----------- |
| UFP                      | **32**      |
| VAF                      | **20**      |
| Fattore di aggiustamento | **0.85**    |
| FP finali                | **27.2**    |
| LOC equivalenti          | **≈ 1.360** |

---

## 6. Sviluppo dei costi

### 6.1 Distribuzione costi COCOMO

| Fase                      | % Tempo  | Ore         | Costo (€25/h) |
| ------------------------- | -------- | ----------- | ------------- |
| Analisi e requisiti       | 10%      | 165 h       | €4.125        |
| Progettazione             | 15%      | 247 h       | €6.175        |
| Implementazione           | 40%      | 659 h       | €16.475       |
| Testing e validazione     | 20%      | 330 h       | €8.250        |
| Documentazione e consegna | 15%      | 247 h       | €6.175        |
| **Totale (COCOMO)**       | **100%** | **1.648 h** | **€41.200**   |

### 6.2 Distribuzione costi FPA

| Fase                      | % Tempo  | Ore         | Costo (€25/h) |
| ------------------------- | -------- | ----------- | ------------- |
| Analisi e requisiti       | 10%      | 136 h       | €3.400        |
| Progettazione             | 15%      | 204 h       | €5.100        |
| Implementazione           | 40%      | 544 h       | €13.600       |
| Testing e validazione     | 20%      | 272 h       | €6.800        |
| Documentazione e consegna | 15%      | 204 h       | €5.100        |
| **Totale (FPA)**          | **100%** | **1.360 h** | **€34.000**   |

### 6.3 Confronto sintetico

| Metodo              | Ore totali | Costo orario (€) | Costo totale (€) | Differenza (%) |
| ------------------- | ---------- | ---------------- | ---------------- | -------------- |
| **COCOMO**          | 1.648      | 25               | 41.200           | +17%           |
| **FPA**             | 1.360      | 25               | 34.000           | -17%           |
| **Media integrata** | **1.500**  | **25**           | **€37.500**      | –              |

---

## 7. Resoconto finale

**Durata stimata complessiva:** 4 mesi
**Team:** 2 sviluppatori + supervisore accademico
**Ore totali:** 1.500
**Costo orario medio:** €25/h
**Costo complessivo teorico:** **€37.500**

### Osservazioni sintetiche

1. Il metodo **COCOMO** risulta più conservativo (stime superiori di circa il 17%).
2. La **Function Point Analysis** fornisce una stima coerente con la complessità funzionale osservata.
3. La media integrata tra i due approcci è considerata la più realistica per il progetto in oggetto.
4. La distribuzione dei costi mostra che la fase di **implementazione** assorbe circa il 40% del totale.
5. La stima è accademica e teorica, ma riflette l’impegno e la scala di un progetto professionale reale di piccole-medie dimensioni.

**Valore finale stimato del progetto Gestione Dipendenti: ≈ €37.500 per 4 mesi di sviluppo.**
