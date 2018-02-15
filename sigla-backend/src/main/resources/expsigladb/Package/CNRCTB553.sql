--------------------------------------------------------
--  DDL for Package CNRCTB553
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB553" AS
-- =================================================================================================
--
-- CNRCTB553 - Gestione Acconto per le addizionali
--
-- Date: 27/02/2007
-- Version: 1.0
--
-- Dependency: CNRCTB 545 IBMUTL 001
--
-- History:
--
--
-- Date: 15/05/2007
-- Version: 1.1
--
-- Gestione degli acconti nei conguagli
--
-- =================================================================================================
--
-- Constants
--

--
-- Variabili globali
--
   dataOdierna DATE;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;


   CONST_ACCONTO_ADD_COM CONSTANT VARCHAR2(50) := 'ACCONTO_ADD_COM';
   CONST_PERIODO_VALIDITA CONSTANT VARCHAR2(50) := 'PERIODO_VALIDITA';
--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------
-- Inserimento di un record in ACCONTO_CLASSIFIC_CORI

   Procedure insAccontoClassificCori
      (
       aRecAccontoClassificCori ACCONTO_CLASSIFIC_CORI%Rowtype
      );

   Procedure calcolaAddTerritorioAcconto
      (
       inEsercizio  ACCONTO_CLASSIFIC_CORI.esercizio%Type,
       inPerc	    NUMBER,
       inUtente     ACCONTO_CLASSIFIC_CORI.utuv%Type
      );

   Procedure calcolaImponibileAnnoPrec
      (
       inEsercizio NUMBER,
       inRepID     INTEGER,
       inUtente    VARCHAR2
      );

-- Ritorna i record di ACCONTO_CLASSIFIC_CORI per addizionali territorio

   PROCEDURE getAccontoAddTerritorio
      (
       aEsercizio NUMBER,
       aCdAnag NUMBER,
       eseguiLock CHAR,
       aRecAccontoClassificCoriC0 IN OUT ACCONTO_CLASSIFIC_CORI%Rowtype
      );

-- I valori dei montanti sono ridotti di quanto calcolato dal compenso origine in caso di modifica se
-- coincidono il riferimento all'anagrafico e quello del tipo compenso dipendente o altro

   PROCEDURE modAccontoAddTerritorio
      (
       aRecCompenso COMPENSO%ROWTYPE,
       segno CHAR,
       aRecAccontoClassificCoriC0 IN OUT ACCONTO_CLASSIFIC_CORI%Rowtype
      );

-- Calcolo dell'importo della rata di addebito delle addizionali territorio in rateizzazione

   FUNCTION calcolaAddComAccontoRata
      (
       isOrigineCompenso INTEGER,
       cdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE,
       aEsercizio NUMBER,
       aDataRifDa DATE,
       aDataRifA DATE,
       aRecAccontoClassificCoriC0 IN OUT ACCONTO_CLASSIFIC_CORI%Rowtype
      ) RETURN NUMBER;

   FUNCTION getMinDataAcconto
      (
       aEsercizio NUMBER
      )
       Return DATE;

   FUNCTION getMaxDataAcconto
      (
       aEsercizio NUMBER
      )
       Return DATE;

End CNRCTB553;
