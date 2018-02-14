--------------------------------------------------------
--  DDL for View PRT_PDCECON
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_PDCECON" ("ESERCIZIO", "VOCE", "DESCRIZIONE_VOCE", "GRUPPO", "CAPOCONTO", "DES_GRUPPO", "DES_CAPOCONTO", "LIVELLO", "TIPO", "VOCE_EP_NEW") AS 
  SELECT
--
-- Date: 03/12/2003
-- Version: 1.1
--
-- Vista di stampa Piano dei conti economico
--
-- History:
--
-- Date: 19/02/2003
-- Version: 1.0
-- Creazione: questi commenti non erano stati forniti da CNR
--
-- Date: 03/12/2003
-- Version: 1.1
-- Modifica: eliminati gli outerjoin per ottimizzare la stampa (Cineca)
-- Body
--
DISTINCT a.ESERCIZIO,a.cd_voce_ep,a.ds_voce_ep,SUBSTR(a.cd_voce_ep,1,1) AS gruppo,
   SUBSTR(a.cd_voce_ep,1,4) AS capoconto, b.ds_voce_ep AS des_gruppo,c.ds_voce_ep AS des_capoconto,null, null,null as voce_ep_new
FROM VOCE_EP a, VOCE_EP b, VOCE_EP c,parametri_cnr
WHERE a.ti_voce_ep='C' AND
      b.esercizio=a.esercizio AND
      c.esercizio=a.esercizio AND
      b.cd_voce_ep=SUBSTR(a.cd_voce_ep,1,1) AND
      c.cd_voce_ep=SUBSTR(a.cd_voce_ep,1,4) and
      parametri_cnr.esercizio=a.esercizio AND
      fl_nuovo_pdg='N'
union
SELECT
DISTINCT a.ESERCIZIO,a.cd_classificazione,a.DS_CLASSIFICAZIONE, null AS gruppo,
   null AS capoconto, null as des_gruppo, null AS des_capoconto,NR_LIVELLO,tipo,voce_ep.cd_voce_ep voce_ep_new
FROM v_classificazione_voci_ep a,parametri_cnr,voce_ep
WHERE
      voce_ep.id_classificazione (+)= a.id_classificazione  and
      parametri_cnr.esercizio=a.esercizio AND
      fl_nuovo_pdg='Y'
      	 order by 1,9,4,5,2,8;
