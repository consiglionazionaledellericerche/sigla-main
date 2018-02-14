--------------------------------------------------------
--  DDL for View V_STM_PARAMIN_SING_CONTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STM_PARAMIN_SING_CONTO" ("ID_REPORT", "CHIAVE", "TIPO", "SEQUENZA", "CD_CDS", "ESERCIZIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "TI_COMPETENZA_RESIDUO") AS 
  SELECT
--
-- Date: 05/01/2004
-- Version: 1.0
--
-- Vista di memorizzazione dei parametri in input per stampa
-- della situazione singolo conto
--
-- History:
--
-- Date: 05/01/2004
-- Version: 1.0
-- Creazione
--
-- Body:
--
       id,
       chiave,
       tipo,
       sequenza,
       attributo_1, -- cd_cds di scrivania
	   importo_1,   -- esercizio voce_f
       attributo_2, -- ti_appartenenza voce_f
	   attributo_3, -- ti_gestione voce_f
	   attributo_4,	-- cd_voce
	   attributo_5	-- ti_competenza_residuo
FROM   REPORT_GENERICO
;

   COMMENT ON TABLE "V_STM_PARAMIN_SING_CONTO"  IS 'Vista di memorizzazione dei parametri in input per stampa
della situazione singolo conto';
