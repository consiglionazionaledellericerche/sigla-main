--------------------------------------------------------
--  DDL for Package CNRCTB300
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB300" AS
-- =================================================================================================
--
-- CNRCTB300 - Modifica dello STATO_COFI, ed eventuale impostazione dei flag di
--             cancellazione logica, di tutti i documenti amministrativi a
--             fronte della emissione o dell'annullamento di un mandato/reversale
--
-- Date: 14/07/2006
-- Version: 3.19
--
-- =================================================================================================
-- PARAMETRI IN INPUT
-- =================================================================================================
--
-- +----------------------+---------------------------------------+------------+
-- |PARAMETRO             |DESCRIZIONE                            |OBBLIGATORIO|
-- +----------------------+---------------------------------------+------------+
-- |inCDSManrev           |CDS di riferimento man/rev             |     SI     |
-- |inEsercizioManrev     |Esercizio di riferimento man/rev       |     SI     |
-- |inPgManrev            |Progressivo identificativo man/rev     |     SI     |
-- |inTipoManrev          |Dominio:                               |     SI     |
-- |                      |MAN = Mandato                          |            |
-- |                      |REV = Reversale                        |            |
-- |                      |REV_PROVV = Reversale provvisoria      |            |
-- |inAzione              |Dominio:                               |     SI     |
-- |                      |A = Annullamento                       |            |
-- |                      |I = Inserimento                        |            |
-- |inUtente              |Utente applicativo di riferimento      |     SI     |
-- +----------------------+---------------------------------------+------------+
--
-- Dependency: CNRCTB 001/035/100/130 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 08/03/2002
-- Version: 1.0
--
-- Creazione Package.
--
-- Date: 02/04/2002
-- Version: 1.1
--
-- Introdotta gestione, solo per le fatture passive, dell'attributo ti_associato_manrev.
--
-- Date: 08/05/2002
-- Version: 1.2
--
-- Estesa gestione dell'attributo ti_associato_manrev anche a fatture attive e generici.
--
-- Date: 16/05/2002
-- Version: 1.4
-- Aggiornamento stato documenti amministrativi con procedure CNRCTB100
--
-- Date: 17/05/2002
-- Version: 1.5
-- Fix per gestione documenti amm. senza dettaglio con dati contabili in testata
--
-- Date: 22/05/2002
-- Version: 1.6
--
-- Fix per mancato aggiornamento dello stato COFI in testata documenti amministrativi
--
-- Date: 04/07/2002
-- Version: 1.7
--
-- Fix per errato aggiornamento dello stato di testata dei documenti senza dettagli in caso di
-- annullamento del mandato.
-- Introduzione dello stato di annullamento per i documenti che non possono esistere senza il
-- mandato o la reversale
--
-- Date: 06/07/2002
-- Version: 1.8
--
-- Previsione della cancellazione logica dei documenti amministrativi di accantonamento CORI
-- in caso di annullamento dei relativi mandati o reversali
--
-- Date: 08/07/2002
-- Version: 1.9
-- Aggiunta gestione aggiornamento fondo per annullamento mandato di reintegro + fix errori
--
-- Date: 08/07/2002
-- Version: 2.0
-- Smontaggio pratica Accantonamenti nel caso non esistano documenti di versamento
--
-- Date: 18/07/2002
-- Version: 2.1
-- Aggiornamento della documentazione
--
-- Date: 23/07/2002
-- Version: 2.2
-- Eliminazione di mandato legato a CORI
--
-- Date: 25/07/2002
-- Version: 2.3
-- Eliminazione della partita di giro creata sul'UO del centro che versa a seguito dei liquidazione locale accentrata
--
-- Date: 25/07/2002
-- Version: 2.4
-- Aggiunta la condizione su TRASF_S per annullamento doc generici collegati a mandato
--
-- Date: 17/09/2002
-- Version: 2.5
-- Gestione annullamento pagamento di compenso senza mandato principale
-- Gestione annullamento mandato di liquidazione IVA centralizzata e mandato di liquidazione non accentrata CORI decentrati
--
-- Date: 18/09/2002
-- Version: 2.6
-- Gestione tab. assoc. comp. senza man. princ. doc. aut. CORI in cancellazione mandati/reversali cori di comp. senza man. princ.
--
-- Date: 19/09/2002
-- Version: 2.7
-- Eliminazione della pratica al centro su compenso negativo
--
-- Date: 25/09/2002
-- Version: 2.8
-- Il generico di reintegro non produce l'annullamento dell'importo ass. a doc contabili sulla scad. di obb.
--
-- Date: 29/10/2002
-- Version: 2.9
-- Blocco in annullamento mandato mensile stipendi
--
-- Date: 05/11/2002
-- Version: 3.0
-- Gestione dell'annulamento del documento generico di tipo GEN_IVA_E
--
-- Date: 03/12/2002
-- Version: 3.1
--
-- Fix EI2927
-- Modificata la gestione dell'annullamento di mandati e reversali di regolarizzazione.
-- Prima erano sempre annullati tutti i documenti amministrativi associati ad un mandato o reversale
-- di regolarizzazione ora si annulla il solo documento amministrativo di tipo REGOLA_E, le altre
-- tipologie di documenti amministrativi non sono annullate
--
-- Date: 30/12/2002
-- Version: 3.2
-- Vedi -> 3.3
--
-- Date: 02/01/2003
-- Version: 3.3
-- Blocco su annullamento reversale di chiusura del fondo economale
--
-- Date: 07/01/2003
-- Version: 3.4
-- Blocco su annullamento mandato di reintegro e regolarizzazione (generati in reintegri su chiusura del fondo economale)
--
-- Date: 06/02/2003
-- Version: 3.5
--
-- Fix errore interno 2217. Gestione della data di esigibilitא iva sulle fatture attive a seguito di
-- emissione o annullamento di reversale
--
-- Date: 21/02/2003
-- Version: 3.6
--
-- Estrazione del codice terzo della riga mandato per gestire l'aggiornamento dello stato sui
-- documenti amministrativi. Se piש righe di generico insistono sulla stessa scadenza di obbligazione
-- (partite di giro a consumo su CNR) solo quelle che hanno terzo uguale a quello del mandato devono
-- essere aggiornate in modo concorde con inserimento o annullamento di mandati e reversali.
--
-- Date: 21/02/2003
-- Version: 3.7
-- Controllo di non eliminabilitא reversale di accantonamento versamento CORI accentrati
--
-- Date: 26/02/2003
-- Version: 3.8
-- Blocco annullamento eliminazione reversale accantonamento liquidazione iva centro
--
-- Date: 10/03/2003
-- Version: 3.9
--
-- In caso di emissione mandato su generico si selezionano le righe dello stesso come associate al
-- mandato alle seguenti condizioni in OR
-- 1) Codice terzo del mandato = codice terzo della riga del documento amministratico
-- 2) Il codice terzo del documento amministrativo corrisponde ad una anagrafico di tipo diversi
-- 3) Il generico ט un documento di reintegro fondo
-- 4) Il generico ט previsto essere da pagarsi o pagato tramite fondo economale
--
-- Date: 26/03/2003
-- Version: 3.10
--
-- Fix errore CINECA n. 549. Nella associazione tra terzo del mandato e riga del generico si verifica
-- anche l'abbinamento se DOCUMENTO_GENERICO_RIGA.cd_terzo_cessionario non ט nullo e coincide con
-- il terzo del mandato.
--
-- Date: 03/06/2003
-- Version: 3.11
-- Aggiunto callback di controllo di modifica del documento amministrativo basato
-- sul pg_ver_rec ndel documento stesso
--
-- Date: 19/06/2003
-- Version: 3.12
-- gestione dell'annullamento automatico del documento generico di versamento entrata (liq. cori)
--
-- Date: 24/06/2003
-- Version: 3.13
-- Razionalizzazione dei callback speciali effColl ...
--
-- Date: 15/09/2003
-- Version: 3.14
-- Documentazione
--
-- Date: 16/09/2003
-- Version: 3.15
--
-- Fix errore interno. Il sistema non riusciva a smontare le reversali secondarie (o mandati) associati
-- ad un mandato principale su compenso se pagato tramite fondo economale (no match tra terzo del generico
-- e quello del documento autorizzatorio. Abilitato match, indicpendente da codice terzo, per i tipi
-- documento GEN_CORA_E e GEN_CORA_S
--
-- Date: 24/09/2003
-- Version: 3.16
-- Fix su annullamento di mandato o reversale secondario di Compenso con recupero crediti da terzi
-- Ora la pratica al centro viene smontata eliminando il documento generico e annullando l'accertamento al centro.
-- Se l'accertamento al centro risulta riportato a nuovo esercizio, l'operazione di annullamento viene bloccata.
--
-- Date: 06/11/2003
-- Version: 3.17
-- Fix  su aggiornamento a C dello stato del compenso in annullamento mandati/reversali secondari di compenso con totale_compenso=0
--
-- Date: 03/01/2004
-- Version: 3.18
-- Fix errore 722
-- Annullamento non permesso di reversale liquidazione gruppo negativo senza compensazione in UO di versamento centrale
--
-- Date: 14/07/2006
-- Version: 3.19
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- =================================================================================================
--
-- Constants
--

   aUtente UTENTE.cd_utente%TYPE;
   dataOdierna DATE;

   mem_cd_cds DOCUMENTO_GENERICO_RIGA.cd_cds%TYPE;
   mem_cd_uo DOCUMENTO_GENERICO_RIGA.cd_unita_organizzativa%TYPE;
   mem_esercizio DOCUMENTO_GENERICO_RIGA.esercizio%TYPE;
   mem_tipo_docamm DOCUMENTO_GENERICO_RIGA.cd_tipo_documento_amm%TYPE;
   mem_pg_docamm DOCUMENTO_GENERICO_RIGA.pg_documento_generico%TYPE;
   mem_stato_cofi_tes DOCUMENTO_GENERICO_RIGA.stato_cofi%TYPE;

   TYPE GenericCurTyp IS REF CURSOR;

-- Functions e Procedures

----------------------------------------------------------------------------------------------------
-- Main PROCEDURE
----------------------------------------------------------------------------------------------------
-- Effetti su doc. amministrativi della registrazione/annullamento di mandati/reversali.
-- Modifica dello STATO_COFI, ed eventuale impostazione dei flag di cancellazione logica, di tutti i documenti
-- amministrativi a fronte della emissione o dell'annullamento di un mandato/reversale.
-- Valorizzazione o spegnimento della data di esigibilitא differita sulle fatture attive
--
-- pre-post-name: Effettua la modifica di stato COFI del documento amministrativo collegato al documento autorizzatorio
--                in processo.
-- pre:   Nessun'altra precondizione verificata
-- post:  Si estraggono le righe del mandato/reversale in elaborazione con gli estremi della scadenza di obbligazione
--        e del documento amministrativo di riferimento.
--        Per ogni occorrenza di riga del documento autorizzatorio in elaborazione si estraggono i dati degli eventuali
--        dettagli del documento amministrativo in input
--
--        Se il documento amministrativo in processo ha righe
--             Per ogni riga del documento amministrativo:
--                  Se si tratta di inserimento di documento autorizzatorio
--                       Viene aggiornato lo stato cofi della riga di documento aministrativo a 'P' (Pagato/Incassato)
--                       Viene aggiornato il tipo di associazione a mandati reversali a 'T' (Totalmente associato);
--                  Altrimenti
--                       Se il tipo di documento autorizzatorio ט 'A' (Accreditamento) o 'R' (Regolarizzazione)  o se
--                       il tipo di documento amministrativo ט di reintegro fondo economale o accantonamento di entrata
--                       o spesa (CORI)
--                            Viene aggiornato lo stato COFI del documento amministrativo ad 'A' (Annullato)
--                            Viene impostata la data di cancellazione del documento amministrativo
--                       Altrimenti
--                            Se lo stato COFI del documento ט 'P'
--                                 Viene aggiornato lo stato COFI del documento amministrativo a 'C' (Contabilizzato)
--                            Altrimenti
--                                 Viene aggiornato lo stato COFI del documento amministrativo a 'C' (Contabilizzato)
--
--        Per la testata del documento amministrativo:
--            Se il documento amministrativo non ha righe
--                 Se la modalitא ט di inserimento del documento autorizzatorio
--                      Lo stato COFI della testata di documento amministrativo viene posta a 'P'
--                      (Totalmente Pagato/Incassato)
--                 Altrimenti
--                      Lo stato COFI della testata di documento amministrativo viene posta a 'C' (Contabilizzato)
--            Altrimenti
--                 Se la modalitא ט di inserimento del documento autorizzatorio
--                      Se le righe risultano per parte in stato 'P' (Pagate/Incassate) e per parte in 'C' (Contabilizzate)
--                           Viene aggiornato lo stato COFI della testata 'Q' (Parzialmente Pagato/Incassato)
--                      Altrimenti
--                           Viene aggiornato lo stato COFI della testata 'P' (Totalmente Pagato/Incassato)
--                 Altrimenti
--                      Se almeno una riga del documento risulta in stato 'A' (Annullata)
--                           Se nessuna riga del documento amministrativo risulta in stato 'C' (Contabilizzata) o 'P'
--                           (Pagata/Incassata)
--                                Viene aggiornato lo stato COFI della testata a 'A' (Totalmente annullato)
--                           Se le righe risultano per parte in stato 'P' (Pagate/Incassate) e per parte in 'C' (Contabilizzate)
--                                Viene aggiornato lo stato COFI della testata 'Q' (Parzialmente Pagato/Incassato)
--                           Se le righe risultano tutte in stato 'C' (Contabilizzate)
--                                Viene aggiornato lo stato COFI della testata a 'C' (Contabilizzato)
--                           Se le righe risultano tutte in stato 'P' (Pagato/Incassato)
--                                Viene aggiornato lo stato COFI della testata a 'P' (Totalmente Pagato/Incassato)
--     Nel caso di annullamento del documento autorizzatorio
--         Se il tipo di documento amministrativo collegato al mandato ט un accantonamento di entrata o spesa
--              Viene annullata l'obbligazione o accertamento automatici generati con l'accantonamento:
--               Se il tipo di documento autorizzatorio ט 'MAN'
--                     Viene invocata la procedura: CNRCTB035.annullaObbligazione
--                Altrimenti
--                     Viene invocata la procedura: CNRCTB035.annullaAccertamento
--
--      Effetti collaterali su particolari tipi di documento autorizatorio
--      Nel caso il mandato  o reversale sia di versamento IVA
--             viene annullato il generico di versamento/trasferimento al centro e la liquidazione IVA
--      Nel caso il mandato  o reversale sia CORI di compenso senza mandato principale
--             viene riportato a 'C' (Contabilizzato) lo stato del compenso, se = 'P' (Totalmente Pagato/Incassato)
--      Nel caso il mandato in fase di annullamento sia di reintegro del fondo economale:
--             viene invocata la procedura CNRCTB130.ANNULLAREINTEGROSPESEFONDO
--      Nel caso il mandato in fase di annullamento sia di versamento CORI
--             viene invocata la procedura CNRCTB570.ANNULLALIQUIDAZIONE
--
-- Parametri:
--   inCDSManrev -> Codice cds del documento autorizzatorio
--   inEsercizioManrev -> Esercizio del documento autorizzatorio
--   inPgManrev -> Progressivo del documento autorizzatorio
--   inTipoManrev -> Tipo del documento autorizzatorio 'MAN' -> Mandato 'REV' -> Reversale
--   inAzione -> Tipo azione 'A' annullamento 'I' Inserimento del documento autorizzatorio
--   inUtente -> Utente che effettua la modifica
--

-- Lettura dei dati di testata e riga di mandato/reversale in input alla gestione

   PROCEDURE leggiMandatoReversale
      (
       inCDSManrev VARCHAR2,
       inEsercizioManrev NUMBER,
       inPgManrev NUMBER,
       inTipoManrev VARCHAR2,
       inAzione VARCHAR2,
       inUtente VARCHAR2
      );

   PROCEDURE aggDaSospesoRiscontro
      (
       inCDSManrev VARCHAR2,
       inEsercizioManrev NUMBER,
       inPgManrev NUMBER,
       inTipoManrev VARCHAR2,
       inUtente VARCHAR2
      );

----------------------------------------------------------------------------------------------------
-- FUNZIONI e PROCEDURE di servizio
----------------------------------------------------------------------------------------------------

-- Ciclo principale di aggiornamento dello stato_cofi, ed eventuale impostazione
-- dei flag di cancellazione logica, dei documenti amministrativi associati al
-- mandato/reversale in input

   PROCEDURE aggiornaStatoDocamm
      (
       aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE,
       inTipoManrev VARCHAR2,
       inAzione VARCHAR2
      );

-- Ciclo di aggiornamento dello stato_cofi, ed eventuale impostazione dei flag di
-- cancellazione logica, sulle testate dei documenti amministrativi associati al
-- mandato/reversale in input

   PROCEDURE aggiornaStatoTestataDocamm
      (
       aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE,
       inTipoManrev VARCHAR2,
       inAzione VARCHAR2
      );

-- Effetti collaterali ins/ann riga di mandato
-- Viene chiamato per ogni riga processata del mandato/reversale in processo

   PROCEDURE effCollRigaManRev
      (
       aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE,
       inTipoManrev varchar2,
       inAzione varchar2
      );

-- Effetti collaterali annullamento particolari documenti generici
-- Invocato quando il doc amministrativo di tipo GENERICO risulta annullato

PROCEDURE effCollAnnDocGen
      (
       aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE,
       inTipoManrev varchar2
      );

-- Effetti collaterali dovuti all'operazione effettuata sul documento autorizzatorio
-- Effetti esclusivamente dipendenti dalla testata del doc autorizzatorio
-- Tale metodo viene sempre chiamato al termine del processo centrale del package

PROCEDURE effCollFinale(
    inCDSManrev VARCHAR2,
    inEsercizioManrev NUMBER,
    inPgManrev NUMBER,
    inTipoManrev VARCHAR2,
    inAzione VARCHAR2,
    inUtente VARCHAR2
);

-- Controlli preliminari legati alla sola testata del documento autorizzatorio
-- Tale metodo viene chiamato prima di cominciare il processo centrale del package

PROCEDURE checkIniziale(
    inCDSManrev VARCHAR2,
    inEsercizioManrev NUMBER,
    inPgManrev NUMBER,
    inTipoManrev VARCHAR2,
    inAzione VARCHAR2,
    inUtente VARCHAR2
);


-- Composizione query di selezione delle righe dei documenti amministrativi
-- associati al mandato/reversale in input

   FUNCTION componiLeggiDocamm
      (
       aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE,
       inTipoManrev VARCHAR2
      ) RETURN VARCHAR2;

-- Pre-post name: Sull base del pg_ver_rec passato verifica se il documento amministrativo ט cambiato (dopo averlo letto in modo lockante)
-- pre:   Il pg_ver_rec del documento amministrativo ט diverso da quello specificato o quello specificato null o il documento amministrativo non viene trovato
-- post:  Viene sollevata un'eccezione
--
-- Parametri:
--     aTiDocAmm -> Tipo di documento amministrativo
--     aCdCds -> Cds documento amministrativo
--     aEs -> Esercizio documento amministrativo
--     aCdUo -> UO documento amministrativo
--     aPgDocAmm -> Progressivo del documento amministrativo
--     aPgVerRec -> Progressivo di versione del documento amministrativo
--
   procedure checkDocAmmCambiato (aTiDocAmm varchar2,aCdCds varchar2,aEs number,aCdUo varchar2,aPgDocAmm number, aPgVerRec number);

END CNRCTB300;
