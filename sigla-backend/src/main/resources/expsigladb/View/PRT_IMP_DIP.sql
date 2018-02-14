--------------------------------------------------------
--  DDL for View PRT_IMP_DIP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_IMP_DIP" ("CODANAG", "MATRICOLA", "NOMINATIVO", "CFIS", "SEDELAV", "UO", "ESERC", "FERROVIA", "INPS", "CPDELCPS", "IMPFISNET", "IMPFISC", "DESCR") AS 
  SELECT
--
-- Date:08/04/2004
-- Version:1.3
--
-- Vista di stampa Imponibili Fiscali e Previdenziali Dipendenti
--
-- History:
--
-- Date :03/04/2003
-- Version: 1.0
-- Creazione
--
-- Date :30/05/2003
-- Version: 1.1
-- Modifica: Modificata la tabella CNR_ANADIP con CNR_ANADIP_ORIGINE - aggiunti i campi irpef_lordo_dipendenti,
-- irpef_dipendenti, inps_dipendenti,inps_tesoro_dipendenti, fondo_fs_dipendenti. Aggiunte le tabelle
-- RAPPORTO e MONTANTI
--
-- Date :03/06/2003
-- Version: 1.2
-- Modifica: MInserito il distinct su tabella RAPPORTO per prendere un solo CD_ANAG
--
-- Date :08/04/2004
-- Version: 1.3
-- Modifica: Inserito il campo Esercizio per prendere un solo ANNO
--
-- Body
--
DISTINCT (RAPPORTO.CD_ANAG),
CNR_ANADIP_ORIGINE.MATRICOLA,
CNR_ANADIP_ORIGINE.NOMINATIVO,
CNR_ANADIP_ORIGINE.DIP_COD_FIS,
CNR_ANADIP_ORIGINE.SEDE_LAVORO,
CNR_ANADIP_ORIGINE.UO_TIT,
MONTANTI.ESERCIZIO,
MONTANTI.FONDO_FS_DIPENDENTI,
MONTANTI.INPS_DIPENDENTI,
MONTANTI.INPS_TESORO_DIPENDENTI,
MONTANTI.IRPEF_DIPENDENTI,
MONTANTI.IRPEF_LORDO_DIPENDENTI,
UNITA_ORGANIZZATIVA.DS_UNITA_ORGANIZZATIVA
FROM CNR_ANADIP_ORIGINE, UNITA_ORGANIZZATIVA,MONTANTI,RAPPORTO
WHERE
UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA= SUBSTR(CNR_ANADIP_ORIGINE.UO_TIT,1,3) || '.' || SUBSTR(CNR_ANADIP_ORIGINE.UO_TIT,4,3)
AND RAPPORTO.MATRICOLA_DIPENDENTE = CNR_ANADIP_ORIGINE.MATRICOLA
AND RAPPORTO.CD_ANAG = MONTANTI.CD_ANAG
ORDER BY CNR_ANADIP_ORIGINE.UO_TIT,CNR_ANADIP_ORIGINE.NOMINATIVO
;

   COMMENT ON TABLE "PRT_IMP_DIP"  IS 'Vista di stampa Imponibili Fiscali e Previdenziali Dipendenti';
