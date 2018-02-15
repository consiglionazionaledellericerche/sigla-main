--------------------------------------------------------
--  DDL for Package CNRCTB552
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB552" AS
-- =================================================================================================
--
-- CNRCTB552 - Calcolo e scrittura COMPENSI. Estratto da CNRCTB550 per gestione recupero rate
--
-- Date: 09/12/2003
-- Version: 1.0
--
-- Dependency: CNRCTB 545 IBMUTL 001
--
-- History:
--
--
-- Date: 09/12/2003
-- Version: 1.0
--
-- Rilascio richiesta CINECA n. 471. Gestione rateizzazione addizionali territorio (seconda fase =
-- addebito della rata in esercizio successivo).
--
-- =================================================================================================
--
-- Constants
--

--
-- Variabili globali
--

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------

-- Ritorna i record di RATEIZZA_CLASSIFIC_CORI per addizionali territorio

   PROCEDURE getRateizzaAddTerritorio
      (
       aEsercizio NUMBER,
       aCdAnag NUMBER,
       eseguiLock CHAR,
       aRecRateizzaClassificCoriC0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
       aRecRateizzaClassificCoriP0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
       aRecRateizzaClassificCoriR0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE
      );

-- I valori dei montanti sono ridotti di quanto calcolato dal compenso origine in caso di modifica se
-- coincidono il riferimento all'anagrafico e quello del tipo compenso dipendente o altro

   PROCEDURE modRateizzaAddTerritorio
      (
       aRecCompenso COMPENSO%ROWTYPE,
       segno CHAR,
       aRecRateizzaClassificCoriC0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
       aRecRateizzaClassificCoriP0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
       aRecRateizzaClassificCoriR0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE
      );

-- Calcolo dell'importo della rata di addebito delle addizionali territorio in rateizzazione

   FUNCTION calcolaAddTerritorioRecRate
      (
       isOrigineCompenso INTEGER,
       cdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE,
       aEsercizio NUMBER,
       aDataRifDa DATE,
       aDataRifA DATE,
       aRecRateizzaClassificCoriC0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
       aRecRateizzaClassificCoriP0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
       aRecRateizzaClassificCoriR0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE
      ) RETURN NUMBER;

END CNRCTB552;
