--------------------------------------------------------
--  DDL for View V_SOSPESO_IM_FIGLI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SOSPESO_IM_FIGLI" ("CD_CDS", "ESERCIZIO", "TI_ENTRATA_SPESA", "TI_SOSPESO_RISCONTRO", "CD_SOSPESO", "IM_SOSPESO", "IM_ASSOCIATO_FIGLI") AS 
  SELECT
--
-- Date: 12/03/2003
-- Version: 1.1
--
-- Per ogni sospeso padre del CNR, calcola la somma degli importi associati di tutti i suoi figli
--
--
-- History:
--
-- Date: 28/02/2003
-- Version: 1.0
-- Creazione
--
-- Date: 12/03/2003
-- Version: 1.1
-- Aggiunta gestione anche per sospesi di spesa del CNR
--
-- Body:
--
A.CD_CDS, A.ESERCIZIO, A.TI_ENTRATA_SPESA, A.TI_SOSPESO_RISCONTRO,
A.CD_SOSPESO_PADRE, sum(im_sospeso), sum(A.IM_ASSOCIATO)
FROM SOSPESO A, UNITA_ORGANIZZATIVA b
WHERE
A.TI_SOSPESO_RISCONTRO = 'S' AND
A.CD_SOSPESO_PADRE IS not NULL and
a.cd_cds = b.cd_unita_organizzativa and
b.CD_TIPO_UNITA = 'ENTE'
GROUP BY A.CD_CDS, A.ESERCIZIO, A.TI_ENTRATA_SPESA, A.TI_SOSPESO_RISCONTRO,
A.CD_SOSPESO_PADRE
;

   COMMENT ON TABLE "V_SOSPESO_IM_FIGLI"  IS 'Per ogni sospeso padre di entrata CNR, calcola la somma degli importi associati di tutti i suoi figli';
