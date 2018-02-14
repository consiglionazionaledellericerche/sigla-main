--------------------------------------------------------
--  DDL for View V_BFRAME_BLOB_DIR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_BFRAME_BLOB_DIR" ("CD_TIPO", "FL_DIR", "RELATIVEPATH", "PATH", "FILENAME", "STATO", "DS_FILE", "DS_UTENTE", "TI_VISIBILITA", "DACR", "DUVA", "UTUV", "UTCR", "PG_VER_REC") AS 
  select
--
-- Date: 23/12/2003
-- Version: 1.0
--
-- Vista di estrazione da bframe_blob; elenca solo le
-- directory; le informazioni su UTUV, UTCR... sono
-- forzate a NULL perchè non disponibili a livello di
-- directory; il campo PATH contiene il percorso completo
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
	   'Y',
	   PATH,
	   bframe_blob_tipo.ROOT || PATH,
	   NULL,
	   NULL,
	   NULL,
	   NULL,
	   NULL,
	   TO_DATE(NULL),
	   TO_DATE(NULL),
	   NULL,
	   NULL,
	   1
from
	 bframe_blob,bframe_blob_tipo
where
	bframe_blob.CD_TIPO = bframe_blob_tipo.CD_TIPO
;

   COMMENT ON TABLE "V_BFRAME_BLOB_DIR"  IS 'Vista di estrazione da bframe_blob; elenca solo le
directory; le informazioni su UTUV, UTCR... sono
forzate a NULL perchè non disponibili a livello di
directory; il campo PATH contiene il percorso completo
di ROOT da BFRAME_BLOB_TIPO, mentre il percorso relativo
è contenuto in RELATIVE_PATH';
