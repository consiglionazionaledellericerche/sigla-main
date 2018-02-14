--------------------------------------------------------
--  DDL for View V_LIQUID_CAPITOLI_CORI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_CAPITOLI_CORI" ("CD_GRUPPO_CR", "ESERCIZIO", "CD_CONTRIBUTO_RITENUTA", "TI_APPARTENENZA", "TI_GESTIONE", "TI_ENTE_PERCEPIENTE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE") AS 
  select
--
-- Date: 19/06/2002
-- Version: 1.0
--
-- View di estrazione dei capitoli relativi ai vari Contributi
--
-- History:
--
-- Date: 19/06/2002
-- Version: 1.0
-- Creazione vista
--
-- Body:
--
   a.CD_GRUPPO_CR,
   a.ESERCIZIO,
   a.CD_CONTRIBUTO_RITENUTA,
   c.TI_APPARTENENZA,
   c.TI_GESTIONE,
   c.TI_ENTE_PERCEPIENTE,
   d.CD_ELEMENTO_VOCE,
   d.DS_ELEMENTO_VOCE
from tipo_cr_base a,  ass_tipo_cori_ev c, elemento_voce d
where
        a.CD_CONTRIBUTO_RITENUTA = c.CD_CONTRIBUTO_RITENUTA
	and c.ESERCIZIO = a.ESERCIZIO
	and d.ESERCIZIO = a.ESERCIZIO
	and d.TI_APPARTENENZA = c.TI_APPARTENENZA
	and d.TI_GESTIONE = c.TI_GESTIONE
	and d.CD_ELEMENTO_VOCE = c.CD_ELEMENTO_VOCE
;

   COMMENT ON TABLE "V_LIQUID_CAPITOLI_CORI"  IS 'View di estrazione dei capitoli relativi ai vari Contributi';
