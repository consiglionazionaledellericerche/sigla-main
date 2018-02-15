--------------------------------------------------------
--  DDL for Package CNRCTB500
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB500" AS
--==================================================================================================
--
-- CNRCTB500 - package di utilità per gestione MISSIONI
--
-- Date: 22/04/2003
-- Version: 1.13
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 03/05/2002
-- Version: 1.0
--
-- Creazione Package.
-- Funzione per estrazione dei record relativi alle tabelle di trascodifica per missioni.
-- E' esposto, a parità di codice, il primo record della fetch ordinato per:
-- codice asc, inquadramento desc, nazione desc area geografica desc.
-- Tabelle gestite: MISSIONE_TIPO_SPESA
--                  MISSIONE_TIPO_PASTO
--                  MISSIONE_RIMBORSO_KM
--
-- LEGENDA PARAMETRI IN INPUT
-- +----------------------+---------------------------------------+------------+
-- |PARAMETRO             |DESCRIZIONE                            |OBBLIGATORIO|
-- +----------------------+---------------------------------------+------------+
-- |idTabella             |Indicatore della tabella in lettura    |     SI     |
-- |                      |01 = MISSIONE_TIPO_SPESA               |            |
-- |                      |02 = MISSIONE_TIPO_PASTO               |            |
-- |                      |03 = MISSIONE_RIMBORSO_KM              |            |
-- |aCodice               |Codice tipo spesa, tipo pasto,         |     SI     |
-- |                      |rimborso KM                            |            |
-- |tiAreaGeografica      |Riferimento all'area geografica        |     SI     |
-- |                      |I = Italia                             |            |
-- |                      |E = Estero                             |            |
-- |                      |* = Indiferente                        |            |
-- |pgNazione             |Riferimento alla nazione               |     SI     |
-- |                      |valore = Riferimento ad una specifica  |            |
-- |                      |         nazione                       |            |
-- |                      |0      = Indiferente                   |            |
-- |pgRifInquadramento    |Riferimento al'inquadramento del       |     SI     |
-- |                      |soggetto anagrafico                    |            |
-- |                      |valore = Riferimento ad uno specifico  |            |
-- |                      |         inquadramento                 |            |
-- |                      |0      = Indiferente                   |            |
-- |aData                 |Data di riferimento per storico        |     SI     |
-- +----------------------+---------------------------------------+------------+
--
-- Date: 12/06/2002
-- Version: 1.1
--
-- Inserita lettura delle tabelle:
-- 1) MISSIONE
-- 2) MISSIONE_DIARIA
-- 3) MISSIONE_QUOTA_ESENTE
-- Inserita routine di calcolo ore tappa.
-- Inserita routine di cancellazione delle righe di MISSIONE_DETTAGLIO
-- Inserita routine di inserimento delle righe di MISSIONE_DETTAGLIO
--
-- Date: 27/06/2002
-- Version: 1.2
--
-- Definizione della matrice tappe per il calcolo delle diarie.
-- Introdotto il lock sulla lettura della testata missione
--
-- Date: 04/07/2002
-- Version: 1.3
--
-- Allineamento package alla base dati
--
-- Date: 19/07/2002
-- Version: 1.4
--
-- Aggiornamento documentazione
--
-- Date: 08/08/2002
-- Version: 1.5
--
-- Inserito schema matrice per calcolo lordizzazzione
--
-- Date: 05/12/2002
-- Version: 1.6
--
-- Conversione in euro della diaria in divisa estera
--
-- Date: 09/01/2003
-- Version: 1.7
--
-- Fix su recupero del cambio più recente tra quelli accettabili per per la data valuta specificata
--
-- Date: 10/01/2003
-- Version: 1.8
--
-- Il cambio più recente è quello con dt_inizio_validita maggiore a parità di condizioni
--
-- Date: 10/01/2003
-- Version: 1.9
--
-- Fix errore
--
-- Date: 14/01/2003
-- Version: 1.10
--
-- Fix STM 2700 - aggiunto condizione nella ricerca del tipo spesa, tipo pasto
--                rimborso_km (dt_cancellazione is null)
--
-- Date: 14/01/2003
-- Version: 1.11
--
-- Modificato commenti
--
-- Date: 28/02/2003
-- Version: 1.12
--
-- Modificato funzione "getMissioneDiaria" : il cambio da utilizzare per convertire,
-- se necessario, l'importo della diaria e' il cambio della tappa.
--
-- Date: 22/04/2003
-- Version: 1.13
--
-- Fix errore CINECA n. 582. Modifica base dati, aggiunto attributo MISSIONE_DETTAGLIO.im_maggiorazione_euro.
--
--==================================================================================================
--
-- Constants
--

   -- Dichiarazioni tabelle PL/SQL

   -- Matrice tappe per il calcolo delle diarie e controllo abbattimenti

   TYPE matriceTappeRec IS RECORD
       (
        tDtInizio MISSIONE_TAPPA.dt_inizio_tappa%TYPE,
        tDtFine MISSIONE_TAPPA.dt_fine_tappa%TYPE,
        tNumeroOre NUMBER,
        tAreaGeografica MISSIONE_ABBATTIMENTI.ti_area_geografica%TYPE,
        tPgNazione MISSIONE_TAPPA.pg_nazione%TYPE,
        tCdAreaEstera RIF_AREE_PAESI_ESTERI.CD_AREA_ESTERA%TYPE,
        tIsComuneProprio MISSIONE_TAPPA.fl_comune_proprio%TYPE,
        tPgRifInquadramento MISSIONE_ABBATTIMENTI.pg_rif_inquadramento%TYPE,
        tFlPasto MISSIONE_ABBATTIMENTI.fl_pasto%TYPE,
        tFlAlloggio MISSIONE_ABBATTIMENTI.fl_alloggio%TYPE,
        tFlTrasporto MISSIONE_ABBATTIMENTI.fl_trasporto%TYPE,
        tFlNavigazione MISSIONE_ABBATTIMENTI.fl_navigazione%TYPE,
        tFlVittoGratuito MISSIONE_ABBATTIMENTI.fl_vitto_gratuito%TYPE,
        tFlAlloggioGratuito MISSIONE_ABBATTIMENTI.fl_alloggio_gratuito%TYPE,
        tFlVittoAlloggioGratuito MISSIONE_ABBATTIMENTI.fl_vitto_alloggio_gratuito%TYPE,
        tOkAbbattimento CHAR,
        tPercentualeAbbattimento MISSIONE_ABBATTIMENTI.percentuale_abbattimento%TYPE,
        tFlNoDiaria MISSIONE_TAPPA.fl_no_diaria%TYPE,
        tDiariaLorda MISSIONE_DETTAGLIO.im_diaria_lorda%TYPE,
        tDiariaNetto MISSIONE_DETTAGLIO.im_diaria_netto%TYPE,
        tQuotaEsente MISSIONE_DETTAGLIO.im_quota_esente%TYPE,
        tFlRimborso MISSIONE_TAPPA.fl_rimborso%TYPE,
        tRimborso MISSIONE_DETTAGLIO.im_rimborso%TYPE,
        tNumeroMinuti NUMBER
       );
   TYPE matriceTappeTab IS TABLE OF matriceTappeRec
        INDEX BY BINARY_INTEGER;

   -- Matrice codici trattamento in calcolo lordizzazione

   TYPE coriLordizzaRec IS RECORD
       (
        tCdCori TIPO_CONTRIBUTO_RITENUTA.cd_contributo_ritenuta%TYPE,
        tIsIrpef CHAR
       );
   TYPE coriLordizzaTab IS TABLE OF coriLordizzaRec
        INDEX BY BINARY_INTEGER;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures
--

-- Ritorna un record della tabella MISSIONE eventualmente con lock

   FUNCTION getMissione
      (
       aCdCds MISSIONE.cd_cds%TYPE,
       aCdUo MISSIONE.cd_unita_organizzativa%TYPE,
       aEsercizio MISSIONE.esercizio%TYPE,
       aPgMissione MISSIONE.pg_missione%TYPE,
       eseguiLock CHAR
      ) RETURN MISSIONE%ROWTYPE;

-- Ritorna un record della tabella MISSIONE_DIARIA

   FUNCTION getMissioneDiaria
      (
       aRecMissioneTappa MISSIONE_TAPPA%ROWTYPE,
       aCdGruppoInquadramento VARCHAR2,
       aDataRif DATE
      ) RETURN MISSIONE_DIARIA%ROWTYPE;


-- Ritorna un record della tabella MISSIONE_QUOTA_ESENTE

   FUNCTION getMissioneQuotaEsente
      (
       aTiItaliaEstero CHAR,
       aDataRif DATE
      ) RETURN MISSIONE_QUOTA_ESENTE%ROWTYPE;

-- Ritorna un record della tabella MISSIONE_QUOTA_RIMBORSO

   FUNCTION getMissioneQuotaRimborso
      (
       aRecMissioneTappa MISSIONE_TAPPA%ROWTYPE,
       aCdGruppoInquadramento VARCHAR2,
       aCdAreaEstera VARCHAR2,
       aDataRif DATE
      ) RETURN MISSIONE_QUOTA_RIMBORSO%ROWTYPE;

-- Calcola ore di una tappa

   FUNCTION calcolaOreTappa
      (
       aDataInizio DATE,
       aDataFine DATE
      ) RETURN NUMBER;

-- Calcola ore di una tappa

   FUNCTION calcolaMinutiTappa
      (
       aDataInizio DATE,
       aDataFine DATE
      ) RETURN NUMBER;

-- Cancellazione di tutte le righe, di tipo diaria o spesa, per una data missione

   PROCEDURE delMissioneDettaglio
      (
       aCdCds VARCHAR2,
       aCdUnitaOrganizzativa VARCHAR2,
       aEsercizio NUMBER,
       aPgMissione NUMBER,
       aTipoRiga MISSIONE_DETTAGLIO.ti_spesa_diaria%TYPE
      );

-- Inserimento riga in MISSIONE_DETTAGLIO

   PROCEDURE insMissioneDettaglio
      (
       aRecMissioneDettaglio MISSIONE_DETTAGLIO%ROWTYPE
      );


-- Estrazione dei record relativi alle tabelle di trascodifica per missioni. E' esposto, a parità di codice, il primo record della fetch ordinato per: codice asc, inquadramento desc, nazione desc area geografica desc.
--
-- pre-post-name: Lettura del tipo di spesa
-- pre: idTabella = '01' (Tipo di Spesa)
-- post: Ritorna una stringa ottenuta concatenando:
--     Codice tipo di spesa
--     Tipo di area geografica
--     Progressivo Nazione
--     Progressivo inquadramento
--     Data inizio validità (fomato gg/mm/anno)
--     Data fine validità (fomato gg/mm/anno)
-- Con le condizioni che
--    Codice del tipo spesa = aCodice
--    Data inizio validità <= aData <= Data di fine vliadità
--    Tipo di area geografica = tiAreaGeografica o '*'
--    Progressivo Nazione = pgNazione o 0
--    Progressivo inquadramento = pgRifInquadramento o 0
-- estraendo la prima ricorrenza trovata sulla lista ordinata per
--    Progressivo Inquadramento (decrescente), Progressivo Nazione (decrescente), Tipo di area geografica (decrescente)
--
-- pre-post-name: Lettura del tipo di spesa
-- pre: idTabella = '02' (Tipo di Pasto)
-- post: Ritorna una stringa ottenuta concatenando:
--     Codice tipo di pasto
--     Tipo di area geografica
--     Progressivo Nazione
--     Progressivo inquadramento
--     Data inizio validità (fomato gg/mm/anno)
--     Data fine validità (fomato gg/mm/anno)
-- Con le condizioni che
--    Codice del tipo pasto= aCodice
--    Data inizio validità <= aData <= Data di fine vliadità
--    Tipo di area geografica = tiAreaGeografica o '*'
--    Progressivo Nazione = pgNazione o 0
--    Progressivo inquadramento = pgRifInquadramento o 0
-- estraendo la prima ricorrenza trovata sulla lista ordinata per
--    Progressivo Inquadramento (decrescente), Progressivo Nazione (decrescente), Tipo di area geografica (decrescente)
--
-- pre-post-name: Lettura del tipo di spesa
-- pre: idTabella = '03' (Rimborso Chilometrico)
-- post: Ritorna una stringa ottenuta concatenando:
--     Tipo di auto
--     Progressivo Nazione
--     Data inizio validità (fomato gg/mm/anno)
--     Data fine validità (fomato gg/mm/anno)
-- Con le condizioni che
--    Tipo di auto= aCodice
--    Data inizio validità <= aData <= Data di fine vliadità
--    Tipo di area geografica = tiAreaGeografica o '*'
--    Progressivo Nazione = pgNazione o 0
-- estraendo la prima ricorrenza trovata sulla lista ordinata per
--    Progressivo Nazione (decrescente), Tipo di area geografica (decrescente)
--
-- Parametri:
--     idTabella -> Identificativo della tabella di riferimento: 01 = MISSIONE_TIPO_SPESA 02 = MISSIONE_TIPO_PASTO 03 = MISSIONE_RIMBORSO_KM
--     aCodice -> Codice
--     tiAreaGeografica -> Tipo area geografica
--     pgNazione -> Codice nazione
--     pgRifInquadramento -> Progressivo inquadramento
--     aData -> Data di riferimento

   FUNCTION getFirstTabMissione
      (
       idTabella VARCHAR2,
       aCodice VARCHAR2,
       tiAreaGeografica VARCHAR2,
       pgNazione NUMBER,
       pgRifInquadramento NUMBER,
       aData  DATE
      ) RETURN VARCHAR2;

END CNRCTB500;
