--------------------------------------------------------
--  DDL for Package CNRCTB680
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB680" AS
--==================================================================================================
--
-- CNRCTB680 - Contabilizzazione finanziaria flusso stipendiale
--
-- Date: 14/07/2006
-- Version: 4.6
--
-- Dependency: CNRCTB 001/018/020/037/038/040/80/100/110  IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 18/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 14/10/2002
-- Version: 1.1
-- Generazione n-CORI senza limite di numero + partita di giro tronca nel caso di cori extraerariali speciali
--
-- Date: 16/10/2002
-- Version: 1.2
-- Enforce controlli e gestione corretta importi scadenza da interfaccia stipendi
--
-- Date: 16/10/2002
-- Version: 1.3
-- Corretto aggiornamento della testata della stipendi_cofi
--
-- Date: 16/10/2002
-- Version: 1.4
-- Aggiornamento di tutti i campi del compenso
--
-- Date: 16/10/2002
-- Version: 1.5
-- Aggiunta economica stipendi
--
-- Date: 17/10/2002
-- Version: 1.6
-- Doc generico di versamento di tipo speciale
--
-- Date: 17/10/2002
-- Version: 1.7
-- Fix recupero terzo da obbligazione
-- Fix testata genericolegato ad UO personale
-- Aggiunti importi base testata compenso a fini rilevazione economica
--
-- Date: 17/10/2002
-- Version: 1.8
-- Fix generali su struttura documenti automatici creati
--
-- Date: 17/10/2002
-- Version: 1.9
-- Fix su terzo uo cds reversale cori e partita di giro tronca
--
-- Date: 18/10/2002
-- Version: 2.0
-- Aggiornamento associazione compenso e reversali cori
--
-- Date: 23/10/2002
-- Version: 2.1
-- Utilizzo metodi di recupero date competenza coge in generazione compenso
--
-- Date: 23/10/2002
-- Version: 2.2
-- Fix su recupero ultimo giorno del mese 12
--
-- Date: 25/10/2002
-- Version: 2.3
-- Documentazione
--
-- Date: 10/11/2002
-- Version: 2.4
-- Impostata in testata dei doc generici la competenza economica della scarico mensile degli stipendi (inizio e fine mese di rif.)
--
-- Date: 18/11/2002
-- Version: 2.5
--
-- Adeguamento package alla struttura modificata della tabella COMPENSO. Aggiunti attributi
-- fl_compenso_minicarriera e aliquota_irpef_da_missione.
--
-- Date: 25/11/2002
-- Version: 2.6
-- Fix creazione CORI compenso fittizio
--
-- Date: 26/11/2002
-- Version: 2.7
-- Sistemazione dell'insert CONTRIBUTO_RITENUTA rifatto in locale per completezza
--
-- Date: 02/01/2003
-- Version: 2.8
-- Fix su data registrazione compenso e suoi dati interni (dacr utcr duva utuv)
--
-- Date: 07/01/2003
-- Version: 2.9
-- Fix errore controllo consistenza unità organizzativa con UO pdersonale obbligazioni stipendi
--
-- Date: 07/01/2003
-- Version: 3.0
-- Gestione nuovi campi in compenso per tassazione separata: FL_COMPENSO_MCARRIERA_TASSEP:='N', ALIQUOTA_IRPEF_TASSEP:=0
--
-- Date: 17/01/2003
-- Version: 3.1
-- Gestione campi aggiuntivi in COMPENSO e CONTRIBUTO_RITENUTA per adeguamenti normativi finanziaria 2003
--
-- Date: 29/01/2003
-- Version: 3.2
-- Nuovo campo in compenso per adeguamenti normativi
--
-- Date: 26/03/2003
-- Version: 3.3
--
-- Aggiornamento package, in sede di creazione compenso, per inserimento di nuovi attributi.
-- (fl_escludi_qvaria_deduzione, fl_intera_qfissa_deduzione, im_detrazione_personale_anag)
--
-- Date: 21/05/2003
-- Version: 4.0
-- Gestione CORI negativi
--
-- Date: 03/07/2003
-- Version: 4.1
-- Fix errore registrazione partita giro aperta da parte spesa in CORI compenso stipendi
--
-- Date: 09/07/2003
-- Version: 4.2
--
-- Richiesta CINECA n. ???. Attivazione gestione recupero rate.
-- Mapping nuovo attributo fl_recupero_rate in tabella COMPENSO
--
-- Date: 21/07/2003
-- Version: 4.3
-- Sistemazione data di contabilizzazione 3112 se data di sistema successiva al 3112 dell'anno
--
-- Date: 21/07/2003
-- Version: 4.4
-- Gestione tredicesima (mese=12)
--
-- Date: 27/11/2003
-- Version: 4.5
--
-- Rilascio richiesta CINECA n. 471. Gestione rateizzazione addizionali territorio. (allinea DB)
--
-- Date: 14/07/2006
-- Version: 4.6
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 29/09/2006
-- Version: 4.7
-- Aggiunte nuove procedure per la gestione dei Flussi provenienti dal Personale
--
-- Date: 11/02/2008
-- Version: 4.8
-- Gestito il nuovo Flusso delle Addizionali Comunali provenienti dal Personale.
-- Nell'F24 on line devono essere inseriti gli importi per ogni comune e quindi tale informazione deve
-- essere recuperata dal personale; inoltre, poichè vanno versati solo i comuni con addizionale positiva,
-- viene modificato l'importo del versameno proveniente dal personale mettendo la differenza in una
-- ritenuta creata appositamente ed inserita nella Configurazione.
--
-- Date: 08/07/2008
-- Version: 4.9
-- Gestito il nuovo Flusso dei Rimborsi provenienti dal Personale.
--
-- Date: 17/11/2008
-- Version: 4.10
-- Gestito il nuovo Flusso per il mese 14 (Recupero Addizionali).
--
--
-- Creazione Package.
--
--==================================================================================================
--
-- Constants
--

CORI_SPECIALE CONSTANT VARCHAR2(50):='CLASSIFICAZIONE_CORI_SPEC';
CORI_STIPENDI_EXTRA CONSTANT VARCHAR2(100):='CORI_STIP_EXTRA';

TRATTAMENTO_SPECIALE CONSTANT VARCHAR2(50):='TIPO_TRATTAMENTO_SPECIALE';
TRATTAMENTO_STIPENDI CONSTANT VARCHAR2(100):='TRATTAMENTO_STIPENDI';

TERZO_SPECIALE CONSTANT VARCHAR2(50):='TERZO_SPECIALE';
DIVERSI_STIPENDI CONSTANT VARCHAR2(100):='DIVERSI_STIPENDI';

TIPO_RAPPORTO_STIPENDI CONSTANT VARCHAR2(10):='STI';


TI_ISTITUZIONALE CONSTANT CHAR(1) := 'I';

FLUSSO_PRINCIPALE CONSTANT CHAR(1):='S';
FLUSSO_IRAP CONSTANT CHAR(1):='I';
FLUSSO_ANNULLI CONSTANT CHAR(1):='A';
FLUSSO_RIMBORSI CONSTANT CHAR(1):='R';
FLUSSO_CONTR_FIGURATIVI CONSTANT CHAR(1):='F';
FLUSSO_ADDIZIONALE_COMUNALE Constant CHAR(1):='C';

tipoLog VARCHAR2(20):='ELAB_STIP00';
gPgLog  NUMBER:=0;
--
-- Functions e Procedures
--

-- Contabilizzazione finanziaria del flusso stipendiale
--
-- Contabilizzazione finanziaria del flusso stipendiale. I passaggi sono:
-- 1. lettura delle informazioni relativi agli stipendi del mese in processo e relative scadenze di obbligazione;
-- 2. creazione del documento generico e mandato di liquidazione;
-- 3. creazione di compenso fittizio per la gestione dei contributi ritenuta relativi al mese in processo;
-- 4. creazione della scrittura economica relativa agli stipendi del mese in processo.
--
-- Ordine dei mesi 1,2,3,4,5,6,7,8,9,10,11,12,13 ->
-- il mese n.12 rappresenta la tredicesima il n.13 Dicembre
--
-- pre-post-name: Mese specificato non valido
-- pre: Il mese specificato deve essere compreso tra 1 e 13
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Controllo liquidazione mese precedente
-- pre: Il mese specificato è maggiore di 1, e la liquidazione degli stipendi del mese precedente non è stata fatta
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Controllo liquidazione mese precedente
-- pre: Il mese specificato è maggiore di 1, e la liquidazione degli stipendi del mese precedente non è stata fatta
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Controllo presenza dati stipendiali per il mese in processo
-- pre: Dati non specificati per la liquidazione stipendi del mese in processo
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Adeguamento obbligazione (mesi da 1 a 12)
-- pre: Il mese >= 1 e <= 12, importo su capitolo per il mese differente rispetto all'importo della scadenza corrispondente
--      in obbligazione presa in origine
-- post: Viene modificato l'importo della scadenza corrente sull'obbligazione originale per adeguarlo a quello specificato in interfaccia
-- per il mese e adeguato l'importo della scadenza successiva mantenendo invariato l'importo di testata dell'obbligazione
--
-- pre-post-name: Adeguamento obbligazione (mese 13)
-- pre: Il mese = 13, importo su capitolo per il mese differente rispetto all'importo della scadenza corrispondente
--      in obbligazione presa in origine
-- post: Viene modificato l'importo della scadenza corrente sull'obbligazione originale per adeguarlo a quello specificato in interfaccia
-- per il mese e adeguato l'importo dell'intera obbligazione per adeguarlo alla modifica dell'utlima scadenza (13)
--
-- pre-post-name: Liquidazione dello stipendio del mese in processo
-- pre: Nessun'altra precondizione verficata
-- post:
--    Vengono lette le scadenze di obbligazione preparate in precedenza per il mese in processo sui capitoli degli stipendi
--      Per ogni scadenza viene creata una riga di documento generico intestato al terzo specificato nelle obbligazioni
--          Viene aggiornato l'importo delle scadenze di obbligazione per adeguarlo a quello presente in interfaccia stipendi (vedi pre-post adeguamento obbligazione)
--      Per ogni riga di generico viene creata la corrispondente riga di mandato per la liquidazione al lordo
--
--    Viene creato un compenso senza calcoli per la gestione dei CORI della liquidazione
--      Le caratteristiche del compenso sono le seguenti:
--       1. Il terzo è specificato in configurazione CNR sotto la voce TERZO_SPECIALE DIVERSI_STIPENDI
--		 2. Tale terzo deve avere un solo rapporto di tipo speciale 'STI'
--		 3. Il rapporto deve essere associato ad un trattamento CORI speciale identificato in configurazione CNR alla voce TIPO_TRATTAMENTO_SPECIALE TRATTAMENTO_STIPENDI
--       4. Gli importi del compenso vengono impostati nel seguente modo:
--           IM_CR_PERCIPIENTE=aTotCoriPercipiente
--           IM_CR_ENTE=aTotCoriEnte
--           IM_NETTO_PERCIPIENTE=Totale lordo mandato stipendi - aTotCoriEnte - aTotCoriPercipiente
--           IM_TOTALE_COMPENSO=Totale lordo mandato stipendi
--        	 IM_LORDO_PERCIPIENTE=Totale lordo mandato stipendi - aTotCoriEnte
--       5. Tale compenso risulta escluso sia da COGE che da COAN
--
--      Tale compenso, senza mandato principale, risulta collegato a n-cori specificati in interfaccia stipendi
--      Per ogni CORI viene aperta una partita di giro dipendente dal segno dell'importo del CORI
--        Nel caso il tipo contributo sia di classificazione CORI speciale (cd_classificazione_cori in CONFIGURAZIONE CNR alla voce CLASSIFICAZIONE_CORI_SPEC CORI_STIP_EXTRA):
--         viene aperta una partita di giro tronca
--        Altrimenti
--         viene aperta una normale partita giro che verrà chiusa dal versamento CORI come per i compensi
--
--    Viene registrata la prima scrittura economica dello scarico stipendiale mensile
--
--
-- Parametri:
-- aEs -> esercizio contabile
-- aMese -> numero del mese di riferimento
-- aUser -> utente che effettua la modifica
--

   PROCEDURE contabilFlussoStipCOFI
      (
	   aEs number,
       aMese number,
	   aUser varchar2
      );

   PROCEDURE elaboraStipDett
      (aEs number,
       aMese number,
       aUser varchar2
      );

   PROCEDURE annullaElabStipDett
      (aEs number,
       aMese number,
       aUser varchar2
      );
   Procedure ins_STIPENDI_COFI_OBB_SCAD (aDest STIPENDI_COFI_OBB_SCAD%rowtype);

   Procedure ins_STIPENDI_COFI_CORI (aDest STIPENDI_COFI_CORI%rowtype);

   Procedure ins_STIPENDI_COFI_LOGS (aLog STIPENDI_COFI_LOGS%Rowtype);

End;
