--------------------------------------------------------
--  DDL for View V_BFRAME_BLOB_FILE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_BFRAME_BLOB_FILE" ("CD_TIPO", "FL_DIR", "RELATIVEPATH", "PATH", "FILENAME", "STATO", "DS_FILE", "DS_UTENTE", "TI_VISIBILITA", "DACR", "DUVA", "UTUV", "UTCR", "PG_VER_REC") AS 
  select
--
-- Date: 23/12/2003
-- Version: 1.0
--
-- Vista di estrazione da bframe_blob; elenca tutti i file
-- il campo PATH contiene il percorso completo
-- di ROOT da BFRAME_BLOB_TIPO, mentre il percorso relativo
-- è contenuto in RELATIVE_PATH
--
-- History:
--
-- Date: 23/12/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
	   bframe_blob.cd_tipo,
	   'N',
	   PATH,
	   bframe_blob_tipo.ROOT || PATH,
	   FILENAME,
	   STATO,
	   DS_FILE,
	   DS_UTENTE,
	   TI_VISIBILITA,
	   bframe_blob.DACR,
	   bframe_blob.DUVA,
	   bframe_blob.UTUV,
	   bframe_blob.UTCR,
	   bframe_blob.PG_VER_REC
from
	 bframe_blob,bframe_blob_tipo
where
	bframe_blob.CD_TIPO = bframe_blob_tipo.CD_TIPO
;

   COMMENT ON TABLE "V_BFRAME_BLOB_FILE"  IS 'Vista di estrazione da bframe_blob; elenca tutti i file
il campo PATH contiene il percorso completo
di ROOT da BFRAME_BLOB_TIPO, mentre il percorso relativo
è contenuto in RELATIVE_PATH';
