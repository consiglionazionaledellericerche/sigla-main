--------------------------------------------------------
--  DDL for Package CNRCTB107
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB107" as
--
-- CNRCTB107 - Congelamento fatture esercizi precedenti chiusi
-- Date: 13/07/2006
-- Version: 1.10
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 26/07/2003
-- Version: 1.0
-- Creazione
--
-- Date: 28/07/2003
-- Version: 1.1
-- Test
--
-- Date: 04/08/2003
-- Version: 1.2
-- Fix evita il reproc. delle congelate
-- Aggiunti commit
--
-- Date: 05/08/2003
-- Version: 1.3
-- Fix per gestione note di credito (fasate finanziariamente con la sezione del documento) e debito eventualmente collegate
--
-- Date: 06/08/2003
-- Version: 1.4
-- Controllo che non esista mandato emesso su fattura da congelare
-- Controllo che la fattura non transiti da fondo economale
-- Controllo che la fattura non sia associata a moduli 1210 associati a sospeso
--
-- Date: 06/08/2003
-- Version: 1.5
-- Annullamento scadenza di obbligazione e accertamento
--
-- Date: 26/08/2003
-- Version: 1.6
-- Filtrate le fatture non ancora protocollate sui registri IVA
--
-- Date: 28/08/2003
-- Version: 1.7
-- Segnalato il fatto che nessun documento è stato processato in log
--
-- Date: 28/08/2003
-- Version: 1.8
-- Modifiche per approfondimento analisi:
--  il congelamento è effettuato solo in esercizio > esercizio origine del documento
--  l'esercizio precedente a quello di congelamento è chiuso
--  quello di congelamento aperto
--  vengono sganciate e "annullate" scadenze di obb/acc dell'esercizio di congelamento
--  su tali scadenze NON devono insistere NOTE di credito debito create nell'anno di congelamento
--  le scadenze da sganciare e "annullare" non devono essere collegate a mandati/reversali emessi/riscontrati
--
-- Date: 11/09/2003
-- Version: 1.9
-- Documentazione di dettaglio
--
-- Date: 13/07/2006
-- Version: 1.10
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

TIPO_LOG_JOB_CONG_FAT_ES_CH CONSTANT VARCHAR2(20):='CONG_FAT_ES_CH';

-- Functions e Procedures:

-- Congelamento massivo di fatture (attive/passive)

-- Note:

-- 1.	La funzione di congelamento massivo di fatture è irreversibile, cioè non è possibile ripristinare le fatture che sono state messe in stato congelato. Sarà cura dell'utente verificare l'idoneità di tale operazione prima di invocarla.
-- 2.	Il sistema consente di congelare anche le fatture parzialmente pagate/incassate cioè le fatture con alcune righe già associate a mandati/reversali. In tal caso lo scollegamento delle obbligazioni/accertamenti riguarda solo la parte non pagata/incassata e analogamente il valore della scrittura coep riguarda solo questa parte. Inoltre Il sistema deve impedire l'annullamento di un mandato/reversale che paga/incassa righe di fatture congelate.


-- Pre-post name: L'esercizio specificato non deve essere quello di partenza
-- Pre: L'esercizio aEs è quello di partenza
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: L'esercizio specificato deve essere aperto
-- Pre: L'esercizio aEs non è aperto
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: L'esercizio precedente a quello specificato non è chiuso
-- Pre: L'esercizio aEs-1 non è chiuso
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: L'esercizio precedente a quello specificato non è chiuso
-- Pre: L'esercizio aEs-1 non è chiuso
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Check che non ci siano mandati emessi/esitati nell'anno di congelamento sulla fattura
-- Pre: Esiste almeno un mandato emesso o esitato nell'esercizio di congelamento per la fattura in processo
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Check che la fattura passiva non debba transiatare o sia transitata da fondo economale
-- Pre: La fattura in processo è marcata come associabile o associata a fondo economale (ST_PAGAMENTO_FONDO_ECO)
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Check che NON ci siano lettere di pagamento con sospeso associate alla fattura passiva nell'anno di congelamento
-- Pre: Esiste almeno una lettera di pagamento estero (mod. 1210) associata alla fattura passiva nell'anno di congelamento
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Check che la fattura non sia collegata finanziariamente a note di credito/debito emesse nell'esercizio di congelamento
-- Pre: Esiste almeno una riga della fattura che insiste su scadenza di accert./obbligaz. su cui insiste una nota di credito o debito emessa
--      nell'esercizio di congelamento
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Check che la fattura non sia collegata finanziariamente a note di credito/debito collegate anche ad altre fatture
-- Pre: Esiste almeno una riga della fattura che insiste su scadenza di accert./obbligaz. su cui insiste una nota di credito o debito emessa
--      che insiste finanziariamente anche su righe di altre fatture
-- Post: Viene sollevata un'eccezione
--
-- Pre-post name: Congelamento di fatture attive/passive
-- Pre: Nessuna delle precondizioni è verificata
-- Post:
--  Esecuzione del congelamento
--	 Per ogni fattura congelabile il sistema:
--    a. imposta un flag di 'congelato' sul documento in modo da impedire all'utente di effettuare modifiche
--       al documento stesso tramite l'applicazione on-line: il 'congelamento' della fattura viene fatto a livello
--       di testata della fattura cioè tutte le righe della fattura risultano congelate
--	  b. scollega tutte le scadenze delle obbligazioni/accertamenti su cui erano state contabilizzate le righe delle
--       fatture. Imposta al valore 0 l'importo di tutte le scadenze delle obbligazioni/accertamenti (e dei relativi
--       dettagli di scadenza) su cui erano state contabilizzate le righe delle fatture e aggiorna l'importo di testata
--       delle obbligazioni/accertamenti se anche l'importo di testata dell'obbligazione/accertamento risulta essere 0
--       il sistema procede all'annullamento dell'intera obbligazione. Quindi decrementa i saldi dei capitoli finanziari
--    c. se la fattura è contabilizzata su scadenze di obbligazioni sulle quali sono state anche contabilizzate note di credito
--       il sistema procede al congelamento anche di queste ultime

-- Parametri
--   job,pg_exec,next_date -> parametri impliciti per gestione via batch
--   aTiDoc -> Tipo di documento amministrativo (FATTURA_A o FATTURA_P)
--   aCdCds -> Centro di spesa (null -> tutti i cds)
--   aEs -> Esercizio in cui avviene il congelamento della fattura
--   aCdUo -> Unità organizzativa documento amministrativo (null -> tutte le UO)
--   aPgDoc -> Progressivo documento amministrativo (null -> tutti i documenti)

 procedure job_congelaFatturaEsChiuso(job number, pg_exec number, next_date date, aTiDoc varchar2, aCdCds varchar2, aEs number, aCdUo varchar2, aPgDoc number);

end;
