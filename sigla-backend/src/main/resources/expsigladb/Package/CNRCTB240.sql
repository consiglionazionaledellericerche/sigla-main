--------------------------------------------------------
--  DDL for Package CNRCTB240
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB240" as
--==================================================================================================
--
-- CNRCTB240 - Package per la gestione delle liquidazione da Interfaccia
--
-- Date: 15/09/2003
-- Version: 1.8
--
-- Dependency: CNRCTB  015/250/255/260/265/270 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 03/03/2003
-- Version: 1.0
-- Creazione
--
-- Date: 11/03/2003
-- Version: 1.1
-- Modificata selezione delle uo di appartenenza
--
-- Date: 12/03/2003
-- Version: 1.2
-- Modificato il msg di errore per la mancanza di liquidazione uo da interfaccia
--
-- Date: 13/03/2003
-- Version: 1.3
-- Inserito calcolo della liquidazione provvisoria.
--
-- Date: 14/03/2003
-- Version: 1.4
-- Inserito controllo bloccante sul calcolo della liquidazione def/prov
-- nel caso in cui risulti già effettualta la liquidazione definitiva
-- non è possibile calcolare la liquidazione DEF/PROV ma solo la RISTAMPA.
--
-- Date: 18/03/2003
-- Version: 1.5
-- Inserito chiusura cursore nelle exception
--
-- Date: 20/03/2003
-- Version: 1.6
--
-- Revisione controlli in lancio liquidazione iva da interfaccia e modifica per cursore esplicito
--
-- Date: 21/03/2003
-- Version: 1.7
--
-- Inserito controllo se non vi è nulla da elaborare
--
-- Date: 15/09/2003
-- Version: 1.8
--
-- Attivazione gestione liquidazione IVA del mese di dicembre da eseguirsi sempre in esercizio
-- successivo. (chiusure).
-- La chiamata è fatta sempre con periodo dicembre esercizio ed esercizio = esercizio + 1.
--
--==================================================================================================
--
--
-- Constants:

   -- Tipi liquidazione IVA

   -- Commerciale
   TI_LIQ_IVA_COMMERC CONSTANT CHAR(1) :='C';
   -- Istituzionale intraue
   TI_LIQ_IVA_ISTINTR CONSTANT CHAR(1) :='I';
   -- San Marino senz'iva
   TI_LIQ_IVA_ISTSMSI CONSTANT CHAR(1) :='S';

--    K_PRIMA_GSTIVA CONSTANT CONFIGURAZIONE_CNR.cd_chiave_primaria%TYPE := 'GESTIONE_IVA';
--    K_SECONDA_PRORATA_GSTIVA CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'ATTIVA_PRORATA';

--   TI_LIQUIDAZIONE CONSTANT VARCHAR2(50) :='LIQUIDAZIONE';

-- Variabili globali

   TYPE GenericCurTyp IS REF CURSOR;
--
-- Functions e Procedures:
--

-- Calcola la liquidazione IVA

   PROCEDURE elaboraLiquidInterf
      (
       aCdCdsOrigine VARCHAR2,
       aCdUoOrigine VARCHAR2,
       aEsercizio NUMBER,
       aCdTipoSezionale VARCHAR2,
       aDataInizio DATE,
       aDataFine DATE,
       aTipoStampa VARCHAR2,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       aRistampa VARCHAR2,
       aRepID INTEGER,
       msg_out IN OUT VARCHAR2,
       aUtente VARCHAR2,
       aGruppoReport VARCHAR2,
       aEsercizioReale NUMBER,
       aTipoReportStato VARCHAR2
      );

   PROCEDURE preparaLiquidInterf
      (
       aRecLiquidazioneIvaBase LIQUIDAZIONE_IVA%ROWTYPE,
       aLiquidazioneInterf LIQUID_IVA_INTERF%ROWTYPE,
       aTipoReport VARCHAR2,
       aTipoRegistro CHAR,
       aEsercizioReale NUMBER
      );

   PROCEDURE calcolaLiquidInterf
      (
       aRecLiquidazioneIvaBase LIQUIDAZIONE_IVA%ROWTYPE,
       aLiquidazioneInterf LIQUID_IVA_INTERF%ROWTYPE,
       aTipoRegistro VARCHAR2,
       aTipoReport VARCHAR2,
       aGruppoStm CHAR,
       aGestioneProrata CHAR,
       aEsercizioReale NUMBER
      );

   PROCEDURE segnaLiquidazione
      (
       aLiquidazioneInterf LIQUID_IVA_INTERF%ROWTYPE,
       aRecLiquidazioneIvaBase LIQUIDAZIONE_IVA%ROWTYPE,
       aEsercizioReale NUMBER
      );

END;
