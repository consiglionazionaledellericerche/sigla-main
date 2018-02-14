--------------------------------------------------------
--  DDL for View V_DOC_AMM_COGE_RIGHE_ANNULLATE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_COGE_RIGHE_ANNULLATE" ("CD_TIPO_DOCUMENTO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "PG_NUMERO_DOCUMENTO", "CD_TERZO", "PG_RIGA", "CD_BENE_SERVIZIO", "DT_DA_COMPETENZA_COGE", "DT_A_COMPETENZA_COGE", "TI_ISTITUZ_COMMERC", "IM_TOTALE_DIVISA", "IM_IMPONIBILE", "IM_IVA", "IM_DIPONIBILE_NC", "ESERCIZIO_DOC", "CD_CDS_DOC", "ESERCIZIO_ORI_DOC", "PG_DOC", "FL_PGIRO", "ESERCIZIO_EV", "TI_APPARTENENZA_EV", "TI_GESTIONE_EV", "CD_ELEMENTO_VOCE_EV", "IS_DOC_OBB", "ESERCIZIO_DOC_ORI_RIPORTO", "CD_CDS_DOC_ORI_RIPORTO", "ESERCIZIO_DOC_ORI_ORI_RIPORTO", "PG_DOC_ORI_RIPORTO", "STATO_COGE", "STATO_COGE_DOCAMM", "STATO_COGE_DOCCONT") AS 
  (
--
-- Date: 07/05/2007
-- Version: 1.0
--
-- Vista di estrazione dei dettagli annullati di documento amministrativo con valorizzazioni a fini COGE
--
-- History:
--
-- Date: 07/05/2007
-- Version: 1.0
-- Creazione
--
--
  SELECT -- Estrazione fattura attiva
     'FATTURA_A'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.cd_cds_origine
    ,a.cd_uo_origine
    ,a.PG_FATTURA_ATTIVA
    ,a.CD_TERZO
    ,DECODE(a.TI_CAUSALE_EMISSIONE,'B',b.progressivo_riga,NULL)  -- pg riga supportato solo per vendita di bene durevole
    ,NULL -- Bene servizio non supportato in fattura attiva
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,b.DT_DA_COMPETENZA_COGE
    ,b.DT_A_COMPETENZA_COGE
    ,'C'
    ,SUM(b.IM_TOTALE_DIVISA)
    ,SUM(b.IM_IMPONIBILE)
    ,SUM(b.IM_IVA)
    ,SUM(b.IM_DIPONIBILE_NC)
    ,DECODE(b.pg_obbligazione,NULL,b.esercizio_accertamento,b.esercizio_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.cd_cds_accertamento,b.cd_cds_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.esercizio_ori_accertamento,b.esercizio_ori_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.pg_accertamento,b.pg_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,d.fl_pgiro,c.fl_pgiro)
    ,DECODE(b.pg_obbligazione,NULL,d.ESERCIZIO,c.ESERCIZIO)
    ,DECODE(b.pg_obbligazione,NULL,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,DECODE(b.pg_obbligazione,NULL,d.TI_GESTIONE,c.TI_GESTIONE)
    ,DECODE(b.pg_obbligazione,NULL,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,DECODE(b.pg_obbligazione,NULL,'N','Y')
    ,DECODE(b.pg_obbligazione,NULL,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,A.STATO_COGE
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
FROM FATTURA_ATTIVA a, FATTURA_ATTIVA_RIGA b, OBBLIGAZIONE c, ACCERTAMENTO d
Where b.cd_cds = a.cd_cds
 AND  b.cd_unita_organizzativa = a.cd_unita_organizzativa
 AND  b.ESERCIZIO = a.ESERCIZIO
 AND  b.pg_fattura_attiva = a.pg_fattura_attiva
 And  a.stato_cofi != 'A'
 AND  b.stato_cofi = 'A'
 And  c.cd_cds (+)= b.cd_cds_obbligazione
 AND  c.ESERCIZIO (+)= b.esercizio_obbligazione
 AND  c.esercizio_originale (+)= b.esercizio_ori_obbligazione
 AND  c.pg_obbligazione (+)= b.pg_obbligazione
 AND  d.cd_cds (+)= b.cd_cds_accertamento
 AND  d.ESERCIZIO (+)= b.esercizio_accertamento
 AND  d.esercizio_originale (+)= b.esercizio_ori_accertamento
 AND  d.pg_accertamento (+)= b.pg_accertamento
GROUP BY
     a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.cd_cds_origine
    ,a.cd_uo_origine
    ,a.PG_FATTURA_ATTIVA
    ,a.CD_TERZO
    ,DECODE(a.TI_CAUSALE_EMISSIONE,'B',b.progressivo_riga,NULL)
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,b.DT_DA_COMPETENZA_COGE
    ,b.DT_A_COMPETENZA_COGE
    ,DECODE(b.pg_obbligazione,NULL,b.esercizio_accertamento,b.esercizio_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.cd_cds_accertamento,b.cd_cds_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.esercizio_ori_accertamento,b.esercizio_ori_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.pg_accertamento,b.pg_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,d.fl_pgiro,c.fl_pgiro)
    ,DECODE(b.pg_obbligazione,NULL,d.ESERCIZIO,c.ESERCIZIO)
    ,DECODE(b.pg_obbligazione,NULL,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,DECODE(b.pg_obbligazione,NULL,d.TI_GESTIONE,c.TI_GESTIONE)
    ,DECODE(b.pg_obbligazione,NULL,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,DECODE(b.pg_obbligazione,NULL,'N','Y')
    ,DECODE(b.pg_obbligazione,NULL,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,A.STATO_COGE
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
UNION ALL
SELECT -- Estrazione documento generico
     a.cd_tipo_documento_amm
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.cd_cds_origine
    ,a.cd_uo_origine
    ,a.PG_DOCUMENTO_GENERICO
    ,b.CD_TERZO
    ,b.progressivo_riga -- pg riga non supportato in fattura passiva
	,NULL -- codice bene servizio non supportato per generico
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,b.DT_DA_COMPETENZA_COGE
    ,b.DT_A_COMPETENZA_COGE
    ,a.ti_istituz_commerc
    ,SUM(b.IM_RIGA_DIVISA)
    ,SUM(b.IM_RIGA)
    ,0
    ,0
    ,DECODE(b.pg_obbligazione,NULL,b.esercizio_accertamento,b.esercizio_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.cd_cds_accertamento,b.cd_cds_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.esercizio_ori_accertamento,b.esercizio_ori_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.pg_accertamento,b.pg_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,d.fl_pgiro,c.fl_pgiro)
    ,DECODE(b.pg_obbligazione,NULL,d.ESERCIZIO,c.ESERCIZIO)
    ,DECODE(b.pg_obbligazione,NULL,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,DECODE(b.pg_obbligazione,NULL,d.TI_GESTIONE,c.TI_GESTIONE)
    ,DECODE(b.pg_obbligazione,NULL,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,DECODE(b.pg_obbligazione,NULL,'N','Y')
    ,DECODE(b.pg_obbligazione,NULL,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,B.STATO_COGE
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
FROM DOCUMENTO_GENERICO a, DOCUMENTO_GENERICO_RIGA b, OBBLIGAZIONE c, ACCERTAMENTO d
WHERE
     b.cd_cds = a.cd_cds
 AND b.cd_tipo_documento_amm = a.cd_tipo_documento_amm
 AND b.cd_unita_organizzativa = a.cd_unita_organizzativa
 AND b.ESERCIZIO = a.ESERCIZIO
 AND b.pg_documento_generico = a.pg_documento_generico
 AND b.stato_cofi = 'A'
 And A.STATO_COFI != 'A'
 And c.cd_cds (+)= b.cd_cds_obbligazione
 AND c.ESERCIZIO (+)= b.esercizio_obbligazione
 AND c.esercizio_originale (+)= b.esercizio_ori_obbligazione
 AND c.pg_obbligazione (+)= b.pg_obbligazione
 AND d.cd_cds (+)= b.cd_cds_accertamento
 AND d.ESERCIZIO (+)= b.esercizio_accertamento
 AND d.esercizio_originale (+)= b.esercizio_ori_accertamento
 AND d.pg_accertamento (+)= b.pg_accertamento
 GROUP BY
     a.cd_tipo_documento_amm
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.cd_cds_origine
    ,a.cd_uo_origine
    ,a.PG_DOCUMENTO_GENERICO
    ,b.CD_TERZO
    ,b.progressivo_riga
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,b.DT_DA_COMPETENZA_COGE
    ,b.DT_A_COMPETENZA_COGE
    ,a.ti_istituz_commerc
    ,DECODE(b.pg_obbligazione,NULL,b.esercizio_accertamento,b.esercizio_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.cd_cds_accertamento,b.cd_cds_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.esercizio_ori_accertamento,b.esercizio_ori_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,b.pg_accertamento,b.pg_obbligazione)
    ,DECODE(b.pg_obbligazione,NULL,d.fl_pgiro,c.fl_pgiro)
    ,DECODE(b.pg_obbligazione,NULL,d.ESERCIZIO,c.ESERCIZIO)
    ,DECODE(b.pg_obbligazione,NULL,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,DECODE(b.pg_obbligazione,NULL,d.TI_GESTIONE,c.TI_GESTIONE)
    ,DECODE(b.pg_obbligazione,NULL,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,DECODE(b.pg_obbligazione,NULL,'N','Y')
    ,DECODE(b.pg_obbligazione,NULL,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,DECODE(b.pg_obbligazione,NULL,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,B.STATO_COGE
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
);
