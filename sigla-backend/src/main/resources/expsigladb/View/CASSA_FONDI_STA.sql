--------------------------------------------------------
--  DDL for View CASSA_FONDI_STA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "CASSA_FONDI_STA" ("CD_CDS", "ESERCIZIO", "PG_MANDATO", "TIPO", "TOT_MAN", "TOT_RIT", "CONTO", "SEZIONE", "TOT_MOV") AS 
  SELECT CD_CDS, ESERCIZIO, PG_MANDATO, TIPO, SUM(IM_MANDATO_RIGA), SUM(IM_RITENUTE_RIGA), NULL, NULL, 0
FROM   MANDATO_RIGA_STA
WHERE  ESERCIZIO = 2007 AND
       TIPO IN ('Apertura Fondo Economale', 'Apertura Fondo Economale senza Fondo associato', 'Incrementi Fondo Economale', 'Reintegri Fondo Economale',
               'Regolarizzazione per chiusura Fondo Economale')
GROUP BY CD_CDS, ESERCIZIO, PG_MANDATO, TIPO
UNION ALL
SELECT  CD_CDS_DOCUMENTO, ESERCIZIO, PG_NUMERO_DOCUMENTO, NULL, 0, 0, CD_VOCE_EP, SEZIONE, SUM(im_movimento)
FROM    V_COGE_SCR_MOV
WHERE   V_COGE_SCR_MOV.ESERCIZIO = 2007 AND
	V_COGE_SCR_MOV.ATTIVA = 'Y' AND
        --CD_VOCE_EP IN ('A.08.001', 'A.08.002', 'A.08.003') AND
        cd_tipo_documento = 'MAN' AND
       (ESERCIZIO, CD_CDS_DOCUMENTO, PG_NUMERO_DOCUMENTO) IN
       (SELECT DISTINCT ESERCIZIO, CD_CDS, PG_MANDATO
        FROM MANDATO_RIGA_STA)
Group By CD_CDS_DOCUMENTO, ESERCIZIO, PG_NUMERO_DOCUMENTO, NULL, 0, 0, CD_VOCE_EP, SEZIONE;
