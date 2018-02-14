--------------------------------------------------------
--  DDL for View V_TERZI_DA_COMPLETARE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TERZI_DA_COMPLETARE" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_TERZO", "DENOMINAZIONE") AS 
  SELECT 
-- 
-- Date: 10/11/2005 
-- Version: 1.0 
-- 
-- View per la consultazione dei Terzi per i quali e' necessario effettuare il conguaglio 
-- 
-- History: 
-- Date: 10/11/2005 
-- Version: 1.0 
-- Creazione 
-- 
-- Date: 24/01/2006 
-- Version: 1.1 
-- 
-- Aggiunto anche il Tipo Rapporto per visualizzarlo nella Consultazione "Terzi da Conguagliare" 
-- 
-- Body: 
-- 
	DISTINCT C.cd_cds, C.cd_unita_organizzativa, C.cd_terzo, 
	DECODE(A.cognome,NULL,A.ragione_sociale,A.cognome||' '||A.nome) 
FROM   INCARICHI_REPERTORIO C, TERZO T, ANAGRAFICO A 
WHERE  C.cd_terzo = T.cd_terzo 
AND    A.cd_anag = T.cd_anag 
AND    A.titolo_studio IS NULL 
AND    TO_CHAR(DT_FINE_VALIDITA,'YYYY')>=2008
;
