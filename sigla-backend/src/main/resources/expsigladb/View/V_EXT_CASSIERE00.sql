--------------------------------------------------------
--  DDL for View V_EXT_CASSIERE00
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_EXT_CASSIERE00" ("ESERCIZIO", "NOME_FILE") AS 
  select
--
-- Date: 11/04/2003
-- Version: 1.0
--
-- Vista su EXT_CASSIERE00
-- La vista estrae semplicemente la distinct sui campi ESERCIZIO, NOME_FILE, per
-- la visualizzazione all'utente dei file presenti nel DB
--
-- History:
--
-- Date: 11/04/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
 esercizio
,nome_file
from EXT_CASSIERE00
GROUP BY  esercizio
		 ,nome_file
;

   COMMENT ON TABLE "V_EXT_CASSIERE00"  IS 'Vista su EXT_CASSIERE00
La vista estrae semplicemente la distinct sui campi ESERCIZIO, NOME_FILE, per
la visualizzazione all''utente dei file presenti nel DB';
