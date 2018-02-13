--------------------------------------------------------
--  DDL for View V_PDG_CDR_GESTIBILI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_CDR_GESTIBILI" ("CD_CDR_ROOT", "ESERCIZIO", "ESERCIZIO_INIZIO", "ESERCIZIO_FINE", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "DS_CDR", "LIVELLO", "CD_PROPRIO_CDR", "CD_RESPONSABILE", "INDIRIZZO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_CDR_AFFERENZA") AS 
  SELECT   
--   
-- Date: 16/11/2001   
-- Version: 1.0
--   
-- Vista di estrazione dei CDR gestibili da CDR_ROOT (con codice cd_cdr_root) secondo la gerarchia imposta dal PDG   
-- Le regole gerarchiche sono:   
--
-- 	 CDR ENTE -> La vista estrae tutti i CDR   
--   CDR di I livello del SAC -> la vista estrae i CDR NRUO appartenenti all'UO di cui il CDR_ROOT e responsabile piu se stesso   
--   CDR di I livello di macroistituto -> la vista estrae i CDR che afferiscono al CDR attraverso cd_cdr_afferenza (RUO ed NRUO) piu se stesso   
--   CDR di II livello di macroistituto RUO -> la vista estrae tutti i CDR NRUO appartenenti alla stessa UO di cui il RUO e responsabile piu se stesso
-- Verifica la validita di STO   
--   
-- History:   
--   
-- Date: 16/11/2001   
-- Version: 1.0   
-- Creazione   
-- 
-- Body:   
--   
CDR_ROOT.CD_CENTRO_RESPONSABILITA,   
CDR.ESERCIZIO,
CDR.ESERCIZIO_INIZIO,
CDR.ESERCIZIO_FINE,   
CDR.CD_CENTRO_RESPONSABILITA,   
CDR.CD_UNITA_ORGANIZZATIVA,   
CDR.DS_CDR,   
CDR.LIVELLO,   
CDR.CD_PROPRIO_CDR,   
CDR.CD_RESPONSABILE,   
CDR.INDIRIZZO,   
CDR.DACR,   
CDR.UTCR,   
CDR.DUVA,   
CDR.UTUV,   
CDR.PG_VER_REC,   
CDR.CD_CDR_AFFERENZA   
FROM   
V_CDR_VALIDO CDR,   
V_CDR_VALIDO CDR_ROOT,
V_UNITA_ORGANIZZATIVA_VALIDA UNITA_ORGANIZZATIVA_ROOT   
WHERE   
CDR_ROOT.ESERCIZIO = CDR.ESERCIZIO AND
CDR_ROOT.ESERCIZIO = UNITA_ORGANIZZATIVA_ROOT.ESERCIZIO AND
UNITA_ORGANIZZATIVA_ROOT.CD_UNITA_ORGANIZZATIVA = CDR_ROOT.CD_UNITA_ORGANIZZATIVA   
and   
(   
(   
CDR_ROOT.LIVELLO = 1 AND UNITA_ORGANIZZATIVA_ROOT.CD_TIPO_UNITA <> 'ENTE' 
and 
(
 	CDR.LIVELLO = 2   
	and CDR.cd_cdr_afferenza = CDR_ROOT.cd_centro_responsabilita
)
or
CDR_ROOT.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA   
)   
or   
(   
CDR_ROOT.LIVELLO = 2   
and CDR_ROOT.CD_UNITA_ORGANIZZATIVA = CDR.CD_UNITA_ORGANIZZATIVA   
and TO_NUMBER(CDR_ROOT.CD_PROPRIO_CDR) = 0   
)
or
UNITA_ORGANIZZATIVA_ROOT.CD_TIPO_UNITA = 'ENTE'
);

   COMMENT ON TABLE "V_PDG_CDR_GESTIBILI"  IS 'Vista di estrazione dei CDR gestibili da CDR_ROOT (con codice cd_cdr_root) secondo la gerarchia imposta dal PDG   
Le regole gerarchiche sono:   

	CDR ENTE -> La vista estrae tutti i CDR   
	CDR di I livello del SAC -> la vista estrae i CDR NRUO appartenenti all''UO di cui il CDR_ROOT e responsabile piu se stesso   
	CDR di I livello di macroistituto -> la vista estrae i CDR che afferiscono al CDR attraverso cd_cdr_afferenza (RUO ed NRUO) piu se stesso   
	CDR di II livello di macroistituto RUO -> la vista estrae tutti i CDR NRUO appartenenti alla stessa UO di cui il RUO e responsabile piu se stesso

Verifica la validita di STO';
