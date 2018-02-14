--------------------------------------------------------
--  DDL for View V_PDG_PDG_PADRE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_PDG_PADRE" ("CD_CDR_ROOT", "ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "STATO", "ANNOTAZIONI", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "FL_RIBALTATO_SU_AREA") AS 
  SELECT  
--  
-- Date: 05/10/2001  
-- Version: 1.0  
--  
-- Estrae il PDG del CDR padre dal CDR_ROOT di codice cd_cdr_root  
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
V_PDG_CDR_PADRE VCDR  
WHERE  
P.ESERCIZIO = VCDR.ESERCIZIO AND  
P.CD_CENTRO_RESPONSABILITA = VCDR.CD_CENTRO_RESPONSABILITA;

   COMMENT ON TABLE "V_PDG_PDG_PADRE"  IS 'Estrae il PDG del CDR padre dal CDR_ROOT di codice cd_cdr_root';
