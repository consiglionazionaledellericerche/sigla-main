--------------------------------------------------------
--  DDL for Package ACCONTO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "ACCONTO" AS
-- =================================================================================================
--
-- ACCONTO - Gestione Acconto per le addizionali
--
-- Date: 27/02/2007
-- Version: 1.0
--
-- Dependency: CNRCTB 545 IBMUTL 001
--
-- History:
--
--
-- Date: 27/02/2007
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
   dataOdierna DATE;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------
-- Inserimento di un record in ACCONTO_CLASSIFIC_CORI_ALTRI

   Procedure insAccontoClassificCori
      (
       aRecAccontoClassificCori ACCONTO_CLASSIFIC_CORI_ALTRI%Rowtype
      );

   Procedure calcolaAddTerritorioAcconto
      (
       inEsercizio  ACCONTO_CLASSIFIC_CORI_ALTRI.esercizio%Type,
       inPerc	    NUMBER,
       inUtente     ACCONTO_CLASSIFIC_CORI_ALTRI.utuv%Type
      );

   Procedure calcolaImponibileAnnoPrec
      (
       inEsercizio NUMBER,
       inRepID     INTEGER,
       inUtente    VARCHAR2
      );

END ACCONTO;
