--------------------------------------------------------
--  DDL for View V_INVENTARIO_PER_CONTO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INVENTARIO_PER_CONTO" ("ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "CD_VOCE_EP", "DS_VOCE_EP", "CONTABILIZZAZIONE", "DARE", "AVERE", "SALDO") AS 
  SELECT
DETT.ESERCIZIO,
BENI.cd_unita_organizzativa,
CATEGORIA_GRUPPO_VOCE_EP.cd_voce_ep,ds_voce_ep,decode(stato_coge ,'N', 'Da Inventario','C','Da Inventario', 'Da Documento') Contabilizzazione,
SUM(DECODE(DETT.ti_documento,'C',valore_unitario,0))dare, SUM(DECODE(DETT.ti_documento,'S',valore_unitario,0)) avere, SUM(DECODE(DETT.ti_documento,'C',valore_unitario,-valore_unitario)) SALDO
FROM INVENTARIO_BENI BENI,BUONO_CARICO_SCARICO_DETT DETT,CATEGORIA_GRUPPO_VOCE_EP,voce_ep,parametri_cnr
WHERE
SUBSTR(BENI.CD_CATEGORIA_GRUPPO,1,INSTR(BENI.CD_CATEGORIA_GRUPPO,'.')-1)=CATEGORIA_GRUPPO_VOCE_EP.cd_categoria_gruppo AND
CATEGORIA_GRUPPO_VOCE_EP.sezione='A' AND
CATEGORIA_GRUPPO_VOCE_EP.ESERCIZIO=DETT.ESERCIZIO AND
DETT.PG_INVENTARIO = BENI.PG_INVENTARIO AND
DETT.NR_INVENTARIO= BENI.NR_INVENTARIO AND
DETT.PROGRESSIVO   = BENI.PROGRESSIVO  and
voce_ep.esercizio  = dett.esercizio   and
voce_ep.cd_voce_ep = CATEGORIA_GRUPPO_VOCE_EP.cd_voce_ep AND
PARAMETRI_CNR.ESERCIZIO = dett.esercizio  AND
PARAMETRI_CNR.FL_NUOVO_PDG ='N'
GROUP BY DETT.ESERCIZIO,categoria_gruppo_voce_ep.cd_voce_ep,BENI.cd_unita_organizzativa,ds_voce_ep,decode(stato_coge ,'N', 'Da Inventario','C','Da Inventario', 'Da Documento')
union
SELECT
DETT.ESERCIZIO,
BENI.cd_unita_organizzativa,
CATEGORIA_GRUPPO_VOCE_EP.cd_voce_ep,ds_voce_ep,decode(stato_coge ,'N', 'Da Inventario','C','Da Inventario', 'Da Documento') Contabilizzazione,
SUM(DECODE(DETT.ti_documento,'C',valore_unitario,0))dare, SUM(DECODE(DETT.ti_documento,'S',valore_unitario,0)) avere, SUM(DECODE(DETT.ti_documento,'C',valore_unitario,-valore_unitario)) SALDO
FROM INVENTARIO_BENI BENI,BUONO_CARICO_SCARICO_DETT DETT,CATEGORIA_GRUPPO_VOCE_EP,voce_ep,parametri_cnr
WHERE
DETT.STATO_COGE !='X' AND
BENI.CD_CATEGORIA_GRUPPO=CATEGORIA_GRUPPO_VOCE_EP.cd_categoria_gruppo AND
CATEGORIA_GRUPPO_VOCE_EP.FL_DEFAULT='Y'   AND
CATEGORIA_GRUPPO_VOCE_EP.ESERCIZIO=DETT.ESERCIZIO AND
CATEGORIA_GRUPPO_VOCE_EP.SEZIONE = 'A' AND
DETT.PG_INVENTARIO = BENI.PG_INVENTARIO AND
DETT.NR_INVENTARIO= BENI.NR_INVENTARIO AND
DETT.PROGRESSIVO   = BENI.PROGRESSIVO  and
voce_ep.esercizio  = dett.esercizio   and
voce_ep.cd_voce_ep = CATEGORIA_GRUPPO_VOCE_EP.cd_voce_ep  AND
PARAMETRI_CNR.ESERCIZIO = dett.esercizio  AND
PARAMETRI_CNR.FL_NUOVO_PDG ='Y'
GROUP BY DETT.ESERCIZIO,categoria_gruppo_voce_ep.cd_voce_ep,BENI.cd_unita_organizzativa,ds_voce_ep,decode(stato_coge ,'N', 'Da Inventario','C','Da Inventario', 'Da Documento')
union
SELECT
DETT.ESERCIZIO,
BENI.cd_unita_organizzativa,
CATEGORIA_GRUPPO_VOCE_EP.cd_voce_ep,ds_voce_ep,decode(stato_coge ,'N', 'Da Inventario','C','Da Inventario', 'Da Documento') Contabilizzazione,
SUM(DECODE(DETT.ti_documento,'C',DETT.valore_unitario,0))dare, SUM(DECODE(DETT.ti_documento,'S',DETT.valore_unitario,0)) avere,
SUM(DECODE(DETT.ti_documento,'C',DETT.valore_unitario,-DETT.valore_unitario)) SALDO
FROM INVENTARIO_BENI BENI,BUONO_CARICO_SCARICO_DETT DETT,CATEGORIA_GRUPPO_VOCE_EP,voce_ep,parametri_cnr
WHERE
(DETT.esercizio ,decode(DETT.ti_documento,'C','Carico','Scarico'),DETT.pg_inventario  ,DETT.nr_inventario  ,DETT.pg_buono_c_s  ,DETT.progressivo) in
 (select  v_impegni_inventario_dett.esercizio,v_impegni_inventario_dett.ti_documento, v_impegni_inventario_dett.pg_inventario,
  v_impegni_inventario_dett.nr_inventario, v_impegni_inventario_dett.pg_buono_c_s,v_impegni_inventario_dett.progressivo
  from
 v_impegni_inventario_dett,OBBLIGAZIONE
 where
 obbligazione.cd_cds = v_impegni_inventario_dett.cds_IMP           AND
 obbligazione.esercizio = v_impegni_inventario_dett.esercizio_imp        AND
 obbligazione.pg_obbligazione =  v_impegni_inventario_dett.pg_obbligazione   AND
 obbligazione.esercizio_originale =v_impegni_inventario_dett.esercizio_originale AND
 CATEGORIA_GRUPPO_VOCE_EP.CD_ELEMENTO_VOCE = OBBLIGAZIONE.CD_ELEMENTO_VOCE
 ) AND
DETT.STATO_COGE ='X' AND
BENI.CD_CATEGORIA_GRUPPO=CATEGORIA_GRUPPO_VOCE_EP.cd_categoria_gruppo AND
CATEGORIA_GRUPPO_VOCE_EP.TI_GESTIONE='S'   AND
CATEGORIA_GRUPPO_VOCE_EP.ESERCIZIO=DETT.ESERCIZIO AND
CATEGORIA_GRUPPO_VOCE_EP.SEZIONE = 'A' AND
DETT.PG_INVENTARIO = BENI.PG_INVENTARIO AND
DETT.NR_INVENTARIO= BENI.NR_INVENTARIO AND
DETT.PROGRESSIVO   = BENI.PROGRESSIVO  and
voce_ep.esercizio  = dett.esercizio   and
voce_ep.cd_voce_ep = CATEGORIA_GRUPPO_VOCE_EP.cd_voce_ep  AND
PARAMETRI_CNR.ESERCIZIO = dett.esercizio  AND
PARAMETRI_CNR.FL_NUOVO_PDG ='Y'
GROUP BY DETT.ESERCIZIO,categoria_gruppo_voce_ep.cd_voce_ep,BENI.cd_unita_organizzativa,ds_voce_ep,decode(stato_coge ,'N', 'Da Inventario','C','Da Inventario', 'Da Documento')
union
SELECT
DETT.ESERCIZIO,
BENI.cd_unita_organizzativa,
CATEGORIA_GRUPPO_VOCE_EP.cd_voce_ep,ds_voce_ep,decode(stato_coge ,'N', 'Da Inventario','C','Da Inventario', 'Da Documento non coll.') Contabilizzazione,
SUM(DECODE(DETT.ti_documento,'C',DETT.valore_unitario,0))dare, SUM(DECODE(DETT.ti_documento,'S',DETT.valore_unitario,0)) avere,
SUM(DECODE(DETT.ti_documento,'C',DETT.valore_unitario,-DETT.valore_unitario)) SALDO
FROM INVENTARIO_BENI BENI,BUONO_CARICO_SCARICO_DETT DETT,CATEGORIA_GRUPPO_VOCE_EP,voce_ep,parametri_cnr
WHERE
(DETT.esercizio ,DETT.ti_documento,DETT.pg_inventario  ,DETT.nr_inventario  ,DETT.pg_buono_c_s  ,DETT.progressivo) not in
 (select  ass.ESERCIZIO,ass.TI_DOCUMENTO,ass.pg_inventario,ass.nr_inventario,ass.PG_BUONO_C_S	,ass.progressivo
  from
 ass_inv_bene_fattura ass
 ) AND
DETT.STATO_COGE ='X' AND
BENI.CD_CATEGORIA_GRUPPO=CATEGORIA_GRUPPO_VOCE_EP.cd_categoria_gruppo AND
CATEGORIA_GRUPPO_VOCE_EP.TI_GESTIONE='S'   AND
CATEGORIA_GRUPPO_VOCE_EP.ESERCIZIO=DETT.ESERCIZIO AND
CATEGORIA_GRUPPO_VOCE_EP.SEZIONE = 'A' AND
CATEGORIA_GRUPPO_VOCE_EP.FL_DEFAULT='Y'   AND
DETT.PG_INVENTARIO = BENI.PG_INVENTARIO AND
DETT.NR_INVENTARIO= BENI.NR_INVENTARIO AND
DETT.PROGRESSIVO   = BENI.PROGRESSIVO  and
voce_ep.esercizio  = dett.esercizio   and
voce_ep.cd_voce_ep = CATEGORIA_GRUPPO_VOCE_EP.cd_voce_ep  AND
PARAMETRI_CNR.ESERCIZIO = dett.esercizio  AND
PARAMETRI_CNR.FL_NUOVO_PDG ='Y'
GROUP BY DETT.ESERCIZIO,categoria_gruppo_voce_ep.cd_voce_ep,BENI.cd_unita_organizzativa,ds_voce_ep,decode(stato_coge ,'N', 'Da Inventario','C','Da Inventario', 'Da Documento non coll.');
