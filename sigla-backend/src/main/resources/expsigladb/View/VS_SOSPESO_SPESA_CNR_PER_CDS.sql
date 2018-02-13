--------------------------------------------------------
--  DDL for View VS_SOSPESO_SPESA_CNR_PER_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VS_SOSPESO_SPESA_CNR_PER_CDS" ("CD_CDS", "ESERCIZIO", "CD_SOSPESO", "TI_SOSPESO_RISCONTRO", "TI_ENTRATA_SPESA", "CD_CDS_ORIGINE", "IM_ASSOCIATO") AS 
  SELECT
--
-- Vista che aggrega i sospesi di spesa del CNR per cds, in base ai mandati associati
-- al sospeso . Utilizzata durante la migrazione dei sospesi per segnaalzioni 424,425 e 518
--
-- Version: 1.0
-- Date: 18/03/2003
--
-- History:
--
-- Body:
--
B.CD_CDS, B.ESERCIZIO, B.CD_SOSPESO, B.TI_ENTRATA_SPESA, B.TI_SOSPESO_RISCONTRO,
A.CD_CDS_ORIGINE, SUM(B.IM_ASSOCIATO)
FROM MANDATO A , SOSPESO_DET_USC B
WHERE
A.CD_CDS = '999' AND
A.CD_CDS = B.CD_CDS_mandato AND
A.ESERCIZIO = B.ESERCIZIO AND
A.PG_MANDATO = B.PG_MANDATO AND
B.TI_SOSPESO_RISCONTRO = 'S' AND
B.TI_ENTRATA_SPESA = 'S' AND
B.STATO = 'N'
GROUP BY
A.CD_CDS_ORIGINE,
B.CD_SOSPESO,
B.TI_ENTRATA_SPESA,
B.TI_SOSPESO_RISCONTRO,
B.ESERCIZIO,
B.CD_CDS;

   COMMENT ON TABLE "VS_SOSPESO_SPESA_CNR_PER_CDS"  IS 'Vista che aggrega i sospesi di spesa del CNR per cds, in base ai mandati associati
al sospeso. Utilizzata SOLO durante la migrazione dei sospesi per segnalazioni 424,425 e 518';
