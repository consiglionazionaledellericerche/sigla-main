--------------------------------------------------------
--  DDL for View V_SIP_MODULI_NUOVA_PREVISIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SIP_MODULI_NUOVA_PREVISIONE" ("ESERCIZIO", "PG_PROGETTO", "TIPO_FASE") AS 
  Select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista di estrazione di progetti impiegati in fase Previsionale
--
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunto l'anno e la tipologia PREV/GEST nella selezione del progetto
--
-- Body:
--
esercizio, pg_progetto, 'P'
From (Select Distinct esercizio, pg_progetto
      From pdg_modulo
      Union
      Select Distinct esercizio, pg_progetto
      From   pdg_modulo_costi
      Union
      Select Distinct esercizio, pg_progetto
      From   pdg_modulo_spese
      Union
      Select Distinct esercizio, pg_progetto
      From   pdg_modulo_entrate);
