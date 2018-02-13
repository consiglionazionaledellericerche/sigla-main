--------------------------------------------------------
--  DDL for View PRT_STAMPA_OBBL_RIPORTABILI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_OBBL_RIPORTABILI" ("CD_UNITA_ORGANIZZATIVA", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "ESERCIZIO", "STATO_OBBLIGAZIONE", "RIPORTATO", "DS_OBBLIGAZIONE", "IM_SCADENZA", "CD_ELEMENTO_VOCE") AS 
  SELECT 
-- 
-- Date: 11/12/2006 
-- Version: 1.5 
-- 
-- Stampa obbligazioni riportabili 
-- 
-- 
-- History: 
-- 
-- Date: 22/1/2004 
-- Version: 1.0 
-- Creazione 
-- 
-- Date: 05/02/2004 
-- Version: 1.1 
-- eliminato esercizio dalla where condition 
-- 
-- Date: 10/03/2004 
-- Version: 1.2 
-- Corretta join tra obbligazione ed obbligazione_scadenzario 
-- Corretto stato stornato dell'obbligazione in S 
-- 
-- Date: 06/06/2005 
-- Version: 1.3 
-- Eliminata la condizione sulla descrizione 
-- Aggiunta la condizione tipo documento contabile = 'OBB' 
-- 
-- Date: 18/07/2006 
-- Version: 1.4 
-- Gestione Impegni/Accertamenti Residui: 
-- gestito il nuovo campo ESERCIZIO_ORIGINALE 
-- 
-- Date: 11/12/2006 
-- Version: 1.5 
-- Modifica stampa in seguito alla modifica della versione 1.4 (Esercizio_Originale) 
-- e aggiunta degli impegni residui propri e impropri 
-- 
-- Body: 
-- 
o.CD_UNITA_ORGANIZZATIVA, o.ESERCIZIO_ORIGINALE, o.PG_OBBLIGAZIONE, 
os.PG_OBBLIGAZIONE_SCADENZARIO, o.ESERCIZIO, 
o.stato_obbligazione, o.RIPORTATO, o.ds_obbligazione, 
os.IM_SCADENZA, o.CD_ELEMENTO_VOCE 
FROM OBBLIGAZIONE o, OBBLIGAZIONE_SCADENZARIO os 
WHERE o.cd_cds = os.cd_cds 
AND o.ESERCIZIO = os.ESERCIZIO 
AND o.esercizio_originale = os.esercizio_originale 
AND o.pg_obbligazione = os.pg_obbligazione 
AND os.IM_ASSOCIATO_DOC_CONTABILE < os.IM_SCADENZA 
AND o.dt_cancellazione IS NULL 
AND o.stato_obbligazione <> 'S' 
AND o.cd_tipo_documento_cont IN ('OBB','OBB_RES', 'OBB_RESIM')
and o.pg_obbligazione > 0 
--and o.ds_obbligazione not like 'CORI%' 
--and o.cd_tipo_documento_cont not like 'IMP%' 
ORDER BY o.CD_UNITA_ORGANIZZATIVA ASC ;
