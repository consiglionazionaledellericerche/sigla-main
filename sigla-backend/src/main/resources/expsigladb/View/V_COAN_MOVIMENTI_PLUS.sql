--------------------------------------------------------
--  DDL for View V_COAN_MOVIMENTI_PLUS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_COAN_MOVIMENTI_PLUS" ("CD_TIPO_DOCUMENTO_AMM", "CD_TERZO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_NUMERO_DOCUMENTO", "CD_CDS_OBB_ACC", "ESERCIZIO_OBB_ACC", "ESERCIZIO_ORI_OBB_ACC", "PG_OBB_ACC", "PG_OBB_ACC_SCADENZARIO", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_CENTRO_RESPONSABILITA", "CD_LINEA_ATTIVITA", "CD_FUNZIONE", "CD_NATURA", "IM_TOTALE", "IM_SCADENZA", "IM_VOCE", "PESO", "IM_PARZIALE") AS 
  SELECT
--
-- Date: 18/07/2006
-- Version: 1.4
--
-- Vista che distribuisce i documenti amministrativi prima sulle scadenze e poi sulle Linee di attivita associate
--
-- History:
--
-- Date: 03/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 10/06/2002
-- Version: 1.1
-- INTRODUZIONE DEGLI ACCERTAMENTI
--
-- Date: 14/06/2002
-- Version: 1.2
-- Fix : La join tra obbligazioni accertamenti e v_coan_scadenze, è stata sostituita con una union
--
-- Date: 24/06/2002
-- Version: 1.3
-- Fix : Eliminazione della tabella (OBBLIGAZIONE_SCADENZARIO e ACCERTAMENTO_SCADENZARIO) dalla JOIN
--
-- Date: 18/07/2006
-- Version: 1.4
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
        V.CD_TIPO_DOCUMENTO_AMM
	   ,V.CD_TERZO
       ,V.CD_CDS
	   ,V.CD_UNITA_ORGANIZZATIVA
	   ,V.ESERCIZIO
	   ,V.PG_NUMERO_DOCUMENTO
	   ,V.CD_CDS_OBB_ACC
	   ,V.ESERCIZIO_OBB_ACC
	   ,V.ESERCIZIO_ORI_OBB_ACC
	   ,V.PG_OBB_ACC
	   ,V.PG_OBB_ACC_SCADENZARIO
	   ,V.TI_APPARTENENZA
	   ,V.TI_GESTIONE
	   ,V.CD_ELEMENTO_VOCE
	   ,O_SC_VOCE.CD_CENTRO_RESPONSABILITA
	   ,O_SC_VOCE.CD_LINEA_ATTIVITA
	   ,LA1.CD_FUNZIONE
	   ,LA1.CD_NATURA
	   ,V.IM_TOTALE
	   ,V.IM_SCADENZA
	   ,O_SC_VOCE.IM_VOCE
	   ,(O_SC_VOCE.IM_VOCE/V.IM_SCADENZA)  PESO
	   ,V.IM_TOTALE * (O_SC_VOCE.IM_VOCE/V.IM_SCADENZA) IM_PARZIALE
FROM V_COAN_SCADENZE V
	 ,OBBLIGAZIONE_SCAD_VOCE   O_SC_VOCE
	 ,LINEA_ATTIVITA LA1
WHERE v.FL_OBBIGAZIONE	 	   	    = 'Y'
  AND V.CD_CDS_OBB_ACC              = O_SC_VOCE.CD_CDS (+)
  AND V.ESERCIZIO_OBB_ACC           = O_SC_VOCE.ESERCIZIO (+)
  AND V.ESERCIZIO_ORI_OBB_ACC       = O_SC_VOCE.ESERCIZIO_ORIGINALE (+)
  AND V.PG_OBB_ACC                  = O_SC_VOCE.PG_OBBLIGAZIONE (+)
  AND V.PG_OBB_ACC_SCADENZARIO      = O_SC_VOCE.PG_OBBLIGAZIONE_SCADENZARIO (+)
  AND O_SC_VOCE.CD_CENTRO_RESPONSABILITA = LA1.CD_CENTRO_RESPONSABILITA  (+)
  AND O_SC_VOCE.CD_LINEA_ATTIVITA        = LA1.CD_LINEA_ATTIVITA (+)
  AND V.IM_SCADENZA<>0
UNION
SELECT
        V.CD_TIPO_DOCUMENTO_AMM
	   ,V.CD_TERZO
       ,V.CD_CDS
	   ,V.CD_UNITA_ORGANIZZATIVA
	   ,V.ESERCIZIO
	   ,V.PG_NUMERO_DOCUMENTO
	   ,V.CD_CDS_OBB_ACC
	   ,V.ESERCIZIO_OBB_ACC
	   ,V.ESERCIZIO_ORI_OBB_ACC
	   ,V.PG_OBB_ACC
	   ,V.PG_OBB_ACC_SCADENZARIO
	   ,V.TI_APPARTENENZA
	   ,V.TI_GESTIONE
	   ,V.CD_ELEMENTO_VOCE
	   ,A_SC_VOCE.CD_CENTRO_RESPONSABILITA
	   ,A_SC_VOCE.CD_LINEA_ATTIVITA
	   ,LA2.CD_FUNZIONE
	   ,LA2.CD_NATURA
	   ,V.IM_TOTALE
	   ,V.IM_SCADENZA
	   ,A_SC_VOCE.IM_VOCE
	   ,(A_SC_VOCE.IM_VOCE/V.IM_SCADENZA)  PESO
	   ,V.IM_TOTALE * (A_SC_VOCE.IM_VOCE/V.IM_SCADENZA) IM_PARZIALE
FROM V_COAN_SCADENZE V
	 ,ACCERTAMENTO_SCAD_VOCE   A_SC_VOCE
	 ,LINEA_ATTIVITA LA2
WHERE V.FL_OBBIGAZIONE	 	   	    = 'N'
  AND V.CD_CDS_OBB_ACC                   = A_SC_VOCE.CD_CDS (+)
  AND V.ESERCIZIO_OBB_ACC                = A_SC_VOCE.ESERCIZIO (+)
  AND V.ESERCIZIO_ORI_OBB_ACC            = A_SC_VOCE.ESERCIZIO_ORIGINALE (+)
  AND V.PG_OBB_ACC                       = A_SC_VOCE.PG_ACCERTAMENTO (+)
  AND V.PG_OBB_ACC_SCADENZARIO           = A_SC_VOCE.PG_ACCERTAMENTO_SCADENZARIO (+)
  AND A_SC_VOCE.CD_CENTRO_RESPONSABILITA = LA2.CD_CENTRO_RESPONSABILITA  (+)
  AND A_SC_VOCE.CD_LINEA_ATTIVITA        = LA2.CD_LINEA_ATTIVITA (+)
  AND V.IM_SCADENZA<>0;

   COMMENT ON TABLE "V_COAN_MOVIMENTI_PLUS"  IS 'Vista di estrazione del dettaglio dei movimenti coan';