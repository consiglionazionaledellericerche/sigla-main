--------------------------------------------------------
--  DDL for View V_MANDATO_VOCE_F_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_MANDATO_VOCE_F_CDS" ("CD_CDS_VOCE", "CD_PROPRIO_VOCE", "CD_CDS", "ESERCIZIO", "PG_MANDATO", "IM_MANDATO_RIGA") AS 
  SELECT
-- =================================================================================================
--
-- Date: 18/07/2006
-- Version: 1.2
--
-- Estrae i mandati di Regolarizzazione per un dato CdS, estratto dalla Voce_f corrispondente alla obbligazione
--  indicata nella riga di mandato.
--
-- History:
--
-- Date: 07/10/2003
-- Version: 1.0
-- Creazione
--
-- Date: 20/04/2004
-- Version: 1.1
-- Errore n.815
--
-- Date: 18/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
-- =================================================================================================
	 voce.CD_CDS
	,voce.cd_proprio_voce
	,m.CD_CDS
	,m.ESERCIZIO
	,m.PG_MANDATO
	,r.IM_MANDATO_RIGA
FROM MANDATO m
	,MANDATO_RIGA r
	,OBBLIGAZIONE_SCAD_VOCE o_scad
	,VOCE_F voce
WHERE m.TI_MANDATO = 'R'
AND   m.STATO	  <> 'A'
-- join mandato <-> mandato_riga
AND   r.CD_CDS     = m.CD_CDS
AND	  r.ESERCIZIO  = m.ESERCIZIO
AND	  r.PG_MANDATO = m.PG_MANDATO
-- join o_scad <-> mandato_riga
AND   o_scad.CD_CDS          = r.CD_CDS
AND   o_scad.ESERCIZIO 		 = r.ESERCIZIO_OBBLIGAZIONE
AND   o_scad.ESERCIZIO_ORIGINALE = r.ESERCIZIO_ORI_OBBLIGAZIONE
AND   o_scad.PG_OBBLIGAZIONE = r.PG_OBBLIGAZIONE
AND   o_scad.PG_OBBLIGAZIONE_SCADENZARIO = r.PG_OBBLIGAZIONE_SCADENZARIO
-- join voce <-> o_scad
AND	  voce.ESERCIZIO 	   = o_scad.ESERCIZIO
AND   voce.CD_VOCE	 	   = o_scad.CD_VOCE
AND	  voce.TI_APPARTENENZA = o_scad.TI_APPARTENENZA
AND	  voce.TI_GESTIONE	   = o_scad.TI_GESTIONE;

   COMMENT ON TABLE "V_MANDATO_VOCE_F_CDS"  IS 'Vista di estrazione dei mandati di Regolarizzazione per un dato CdS,
estratto dalla Voce_f corrispondente alla obbligazione
indicata nella riga di mandato.';
