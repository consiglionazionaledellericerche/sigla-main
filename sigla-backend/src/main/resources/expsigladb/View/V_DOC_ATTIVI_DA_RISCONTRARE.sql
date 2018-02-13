--------------------------------------------------------
--  DDL for View V_DOC_ATTIVI_DA_RISCONTRARE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_ATTIVI_DA_RISCONTRARE" ("ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "TIPO", "PROGRESSIVO_DOCUMENTO", "PROTOCOLLO_IVA", "DT_EMISSIONE", "CD_UO_ORIGINE", "CD_TERZO", "COGNOME", "NOME", "RAGIONE_SOCIALE", "CD_MODALITA_PAG", "IM_TOTALE_DOC_AMM", "DA_RISCONTRARE") AS 
  select                             v_doc_attivo.esercizio ,
                                           v_doc_attivo.cd_unita_organizzativa ,
                                           decode(cd_tipo_documento_amm,'FATTURA_A','Fattura Attiva',decode(cd_tipo_documento_amm,'GENERICO_E','Generico Entrata',decode(cd_tipo_documento_amm,'RIMBORSO','Rimborso',cd_tipo_documento_amm))) tipo,
                                           v_doc_attivo.pg_documento_amm ,
                                           protocollo_iva,
                                           v_doc_attivo.dt_emissione ,
                                           v_doc_attivo.cd_uo_origine ,
                                           v_doc_attivo.cd_terzo ,
                                           v_doc_attivo.cognome ,
                                           v_doc_attivo.nome ,
                                           v_doc_attivo.ragione_sociale ,
                                           v_doc_attivo.cd_modalita_pag ,
                                          (select sum(IM_TOTALE_DOC_AMM) from v_doc_attivo_accertamento
                                            where
                                                    v_doc_attivo.cd_cds = v_doc_attivo_accertamento.cd_cds
                                                AND v_doc_attivo.cd_unita_organizzativa = v_doc_attivo_accertamento.cd_unita_organizzativa
                                                AND v_doc_attivo.esercizio = v_doc_attivo_accertamento.esercizio
                                                AND v_doc_attivo.cd_tipo_documento_amm = v_doc_attivo_accertamento.cd_tipo_documento_amm
                                                AND v_doc_attivo.pg_documento_amm = v_doc_attivo_accertamento.pg_documento_amm ) IM_TOTALE_DOC_AMM,
                                           (select sum(IM_TOTALE_DOC_AMM)-sum(IM_ASSOCIATO_DOC_CONTABILE) from v_doc_attivo_accertamento
                                            where
                                                v_doc_attivo.cd_cds = v_doc_attivo_accertamento.cd_cds
                                            AND v_doc_attivo.cd_unita_organizzativa = v_doc_attivo_accertamento.cd_unita_organizzativa
                                            AND v_doc_attivo.esercizio = v_doc_attivo_accertamento.esercizio
                                            AND v_doc_attivo.cd_tipo_documento_amm = v_doc_attivo_accertamento.cd_tipo_documento_amm
                                            AND v_doc_attivo.pg_documento_amm = v_doc_attivo_accertamento.pg_documento_amm) da_riscontrare
from  v_doc_attivo,fattura_attiva where
(    (fattura_attiva.cd_cds(+) = v_doc_attivo.cd_cds)
        AND (fattura_attiva.cd_unita_organizzativa(+) =
                                           v_doc_attivo.cd_unita_organizzativa
            )
        AND (fattura_attiva.pg_fattura_attiva (+)= v_doc_attivo.pg_documento_amm
            )
        AND (fattura_attiva.esercizio (+)= v_doc_attivo.esercizio))and
v_doc_attivo.STATO_COFI not in('A','P')
 AND IM_TOTALE_DOC_AMM > 0
group by v_doc_attivo.esercizio ,v_doc_attivo.cd_cds ,
                                           v_doc_attivo.cd_unita_organizzativa ,
                                           cd_tipo_documento_amm,
                                           v_doc_attivo.pg_documento_amm ,
                                           protocollo_iva,
                                           v_doc_attivo.dt_emissione ,
                                           v_doc_attivo.cd_uo_origine ,
                                           v_doc_attivo.cd_terzo ,
                                           v_doc_attivo.cognome ,
                                           v_doc_attivo.nome ,
                                           v_doc_attivo.ragione_sociale ,
                                           v_doc_attivo.cd_modalita_pag
order by   esercizio,  cd_unita_organizzativa,cd_uo_origine,decode(cd_tipo_documento_amm,'FATTURA_A','Fattura Attiva',decode(cd_tipo_documento_amm,'GENERICO_E','Generico Entrata',decode(cd_tipo_documento_amm,'RIMBORSO','Rimborso',cd_tipo_documento_amm))),pg_documento_amm;
