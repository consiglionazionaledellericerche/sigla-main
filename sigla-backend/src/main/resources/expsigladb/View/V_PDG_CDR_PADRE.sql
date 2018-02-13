--------------------------------------------------------
--  DDL for View V_PDG_CDR_PADRE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_CDR_PADRE" ("CD_CDR_ROOT", "ESERCIZIO", "ESERCIZIO_INIZIO", "ESERCIZIO_FINE", "CD_CENTRO_RESPONSABILITA", "CD_UNITA_ORGANIZZATIVA", "DS_CDR", "LIVELLO", "CD_PROPRIO_CDR", "CD_RESPONSABILE", "INDIRIZZO", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "CD_CDR_AFFERENZA") AS 
  SELECT  
--  
-- Date: 13/11/2001   
-- Version: 1.2   
--  
-- Vista di estrazione dei CDR padri di CDR_ROOT (con codice cd_cdr_root) secondo la gerarchia imposta dal PDG  
-- Le regole gerarchiche sono:  
--  
--	CDR di I livello del SAC -> la vista non estrae nulla  
--	CDR di I livello di macroistituto -> la vista non estrae nulla  
--	CDR di II livello di macroistituto RUO -> la vista estrae il CDR di primo livello a cui afferisce  
--	CDR di II livello di macroistituto NRUO -> la vista estrae il CDR con codice 0 dell''unita organizzativa a cui appartiene  
-- Verifica la validita di STO  
--  
-- History:  
-- Date: 10/10/2001  
-- Version: 1.0  
-- Creazione  
--  
-- Version: 1.1  
-- Date: 08/11/2001  
-- Eliminazione esercizio da STO  
--  
-- Date: 13/11/2001   
-- Version: 1.2   
-- ELiminazione esercizio da STO
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
V_CDR_VALIDO CDR_ROOT  
WHERE  
CDR_ROOT.ESERCIZIO = CDR.ESERCIZIO and  
(  
(  
CDR_ROOT.LIVELLO = 1 and  
CDR.LIVELLO = 1 and  
CDR_ROOT.cd_cdr_afferenza = CDR.cd_centro_responsabilita  
) or  
(  
CDR_ROOT.LIVELLO = 2 and  
(  
(  
TO_NUMBER(CDR_ROOT.CD_PROPRIO_CDR) = 0 and  
CDR_ROOT.cd_cdr_afferenza = CDR.cd_centro_responsabilita  
) or (  
TO_NUMBER(CDR_ROOT.CD_PROPRIO_CDR) != 0 and  
CDR_ROOT.CD_UNITA_ORGANIZZATIVA = CDR.CD_UNITA_ORGANIZZATIVA and  
TO_NUMBER(CDR.CD_PROPRIO_CDR) = 0  
)  
)  
)  
);

   COMMENT ON TABLE "V_PDG_CDR_PADRE"  IS 'Vista di estrazione dei CDR padri di CDR_ROOT (con codice cd_cdr_root) secondo la gerarchia imposta dal PDG  
Le regole gerarchiche sono:  

	CDR di I livello del SAC -> la vista non estrae nulla
	CDR di I livello di macroistituto -> la vista non estrae nulla
	CDR di II livello di macroistituto RUO -> la vista estrae il CDR di primo livello a cui afferisce
	CDR di II livello di macroistituto NRUO -> la vista estrae il CDR con codice 0 dell''''unita organizzativa a cui appartiene 
Verifica la validita di STO';
