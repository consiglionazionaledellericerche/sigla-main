--------------------------------------------------------
--  DDL for View V_PDG_PDG_FIGLI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_PDG_FIGLI" ("CD_CDR_ROOT", "ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "STATO", "ANNOTAZIONI", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "FL_RIBALTATO_SU_AREA") AS 
  SELECT  
--  
-- Date: 05/10/2001  
-- Version: 1.0  
--  
-- Estrae tutti i PDG dei CDR dipendenti dal CDR_ROOT di codice cd_cdr_root  
--
-- History:  
--  
-- Date: 05/10/2001  
-- Version: 1.0  
-- Creazione  
--
-- Body:
-- 
VCDR.CD_CDR_ROOT,  
VCDR.ESERCIZIO,  
VCDR.CD_CENTRO_RESPONSABILITA,
P.STATO,  
P.ANNOTAZIONI,  
P.DACR,  
P.UTCR,  
P.DUVA,  
P.UTUV,  
P.PG_VER_REC,  
P.FL_RIBALTATO_SU_AREA  
FROM  
PDG_PREVENTIVO P,  
V_PDG_CDR_FIGLI VCDR  
WHERE  
P.ESERCIZIO = VCDR.ESERCIZIO AND  
P.CD_CENTRO_RESPONSABILITA = VCDR.CD_CENTRO_RESPONSABILITA;

   COMMENT ON TABLE "V_PDG_PDG_FIGLI"  IS 'Estrae tutti i PDG dei CDR dipendenti dal CDR_ROOT di codice cd_cdr_root
Utilizza la vista v_pdg_cdr_figli';
