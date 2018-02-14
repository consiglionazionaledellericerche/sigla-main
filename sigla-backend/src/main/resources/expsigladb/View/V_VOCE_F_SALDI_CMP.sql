--------------------------------------------------------
--  DDL for View V_VOCE_F_SALDI_CMP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_VOCE_F_SALDI_CMP" ("ESERCIZIO", "CD_CDS", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "TI_COMPETENZA_RESIDUO", "IM_OBBLIG_IMP_ACR", "IM_MANDATI_REVERSALI", "IM_PAGAMENTI_INCASSI") AS 
  select
--
-- Date: 19/07/2006
-- Version: 1.4
--
-- Vista per controllo incrociato di coerenza dei saldi dei documenti contabili
--
-- History:
--
-- Date: 29/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 14/05/2003
-- Version: 1.1
-- Modificata decode per individuare se obbligazioni e accertementi
-- sono a residuo o a competenza
--
-- Date: 21/05/2003
-- Version: 1.2
-- Inserito filtro per non controllare obbligazioni e accertamenti cancellati
--
-- Date: 21/05/2003
-- Version: 1.3
-- corretta condizone where per non considerare accertamenti cancellati
--
-- Date: 19/07/2006
-- Version: 1.4
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
     ESERCIZIO
    ,CD_CDS
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_VOCE
    ,TI_COMPETENZA_RESIDUO
    ,sum(IM_OBBLIG_IMP_ACR)
    ,sum(IM_MANDATI_REVERSALI)
    ,sum(IM_PAGAMENTI_INCASSI)
from (
     select
       b.ESERCIZIO
      ,b.CD_CDS
      ,b.TI_APPARTENENZA
      ,b.TI_GESTIONE
      ,b.CD_VOCE
      ,decode(a.cd_tipo_documento_cont, 'IMP_RES', 'R', 'OBB_PGIR_R', 'R', 'OBB_RES', 'R', 'OBB_RESIM', 'R', 'C') TI_COMPETENZA_RESIDUO
	  ,b.im_voce IM_OBBLIG_IMP_ACR
      ,0 IM_MANDATI_REVERSALI
      ,0 IM_PAGAMENTI_INCASSI
	from obbligazione a, obbligazione_scad_voce b where
	      b.pg_obbligazione > 0
	  and a.cd_cds = b.cd_cds
	  and a.esercizio = b.esercizio
	  and a.esercizio_originale = b.esercizio_originale
	  and a.pg_obbligazione = b.pg_obbligazione
	  and a.esercizio = a.esercizio_competenza
	  and a.stato_obbligazione <>'S' -- Stornato
 union all
     select
       a.ESERCIZIO
      ,a.CD_CDS
      ,a.TI_APPARTENENZA
      ,a.TI_GESTIONE
      ,a.CD_VOCE
      ,decode(a.cd_tipo_documento_cont, 'ACR_RES', 'R', 'ACR_PGIR_R', 'R', 'C') TI_COMPETENZA_RESIDUO
      ,a.im_accertamento IM_OBBLIG_IMP_ACR
      ,0 IM_MANDATI_REVERSALI
      ,0 IM_PAGAMENTI_INCASSI
	from accertamento a where
	      a.pg_accertamento > 0
	  and a.esercizio = a.esercizio_competenza
	  and a.dt_cancellazione is null
 union all
     select
       a.ESERCIZIO
      ,a.CD_CDS
      ,a.TI_APPARTENENZA
      ,a.TI_GESTIONE
      ,a.CD_VOCE
      ,a.TI_COMPETENZA_RESIDUO
      ,0 OBBLIG_IMP_ACR
      ,a.IM_CAPITOLO_PESATO IM_MANDATI_REVERSALI
      ,decode(b.stato,'P',a.IM_CAPITOLO_PESATO,0) IM_PAGAMENTI_INCASSI
     from mandato b, v_mandato_reversale_voce a
     Where    b.pg_mandato > 0
	  And b.cd_cds=a.cd_cds
	  and b.esercizio=a.esercizio
	  and b.pg_mandato=a.pg_documento
	  and a.ti_documento='M'
	  and b.stato <> 'A'
 union all
     select
       a.ESERCIZIO
      ,a.CD_CDS
      ,a.TI_APPARTENENZA
      ,a.TI_GESTIONE
      ,a.CD_VOCE
      ,a.TI_COMPETENZA_RESIDUO
	  ,0 OBBLIG_IMP_ACR
      ,a.IM_CAPITOLO_PESATO IM_MANDATI_REVERSALI
      ,decode(b.stato,'P',a.IM_CAPITOLO_PESATO,0) IM_PAGAMENTI_INCASSI
	from reversale b, v_mandato_reversale_voce a where
	        b.pg_reversale > 0
		and b.cd_cds=a.cd_cds
		and b.esercizio=a.esercizio
		and b.pg_reversale=a.pg_documento
		and a.ti_documento='R'
		and b.stato <> 'A'
) group by
     ESERCIZIO
    ,CD_CDS
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_VOCE
    ,TI_COMPETENZA_RESIDUO
;

   COMMENT ON TABLE "V_VOCE_F_SALDI_CMP"  IS 'Vista per controllo incrociato di coerenza dei saldi dei documenti contabili';
