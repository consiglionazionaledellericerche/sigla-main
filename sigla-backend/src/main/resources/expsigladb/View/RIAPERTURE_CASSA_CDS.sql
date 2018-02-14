--------------------------------------------------------
--  DDL for View RIAPERTURE_CASSA_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "RIAPERTURE_CASSA_CDS" ("CD_CDS_DOCUMENTO", "DARE", "AVERE", "DIFF", "CASSA_INIZIALE") AS 
  Select T.CD_CDS_DOCUMENTO,
       Nvl(sum(DECODE(D.SEZIONE, 'D', D.im_movimento)), 0) DARE,
       NVL(sum(DECODE(D.SEZIONE, 'A', D.im_movimento)), 0) AVERE,
       NVL(sum(DECODE(D.SEZIONE, 'D', D.im_movimento)), 0)-NVL(sum(DECODE(D.SEZIONE, 'A', D.im_movimento)), 0) DIFF,
       (Select IM_CASSA_INIZIALE
        From   ESERCIZIO M2
        Where  M2.ESERCIZIO = 2005 And
               M2.CD_CDS = T.CD_CDS_DOCUMENTO) CASSA_INIZIALE
From   MOVIMENTO_COGE D, SCRITTURA_PARTITA_DOPPIA T
WHERE  T.CD_CDS  = D.CD_CDS   AND
       T.ESERCIZIO = D.ESERCIZIO AND
       T.CD_UNITA_ORGANIZZATIVA = D.CD_UNITA_ORGANIZZATIVA and
       T.PG_SCRITTURA = D.PG_SCRITTURA AND
       T.ATTIVA = 'Y' And
       T.ORIGINE_SCRITTURA = 'CHIUSURA' And
       T.CD_CAUSALE_COGE = 'RIAPERTURA_CONTI' And
       T.ESERCIZIO  = 2005                 And
       D.CD_VOCE_EP In ('A.08.001', 'A.08.002', 'A.08.003', 'A.08.004')
Group By T.CD_CDS_DOCUMENTO
;
