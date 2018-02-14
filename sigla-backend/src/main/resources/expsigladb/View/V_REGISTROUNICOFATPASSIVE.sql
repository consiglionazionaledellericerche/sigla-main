--------------------------------------------------------
--  DDL for View V_REGISTROUNICOFATPASSIVE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_REGISTROUNICOFATPASSIVE" ("PROGR_UNIVOCO", "ESERCIZIO", "DATA_PROTOCOLLO", "NUMERO_PROTOCOLLO", "NR_FATTURA_FORNITORE", "DT_FATTURA_FORNITORE", "RAGIONE_SOCIALE", "COGNOME", "NOME", "CODICE_FISCALE", "PARTITA_IVA", "DESCRIZIONE", "IMPORTO_FATTURA", "DT_SCADENZA", "STATO", "ESERCIZIO_FIN", "CDS_FIN", "PROGR_FIN", "PROGR_RIGA_FIN", "ESERCIZIO_ORI_FIN", "CIG", "CUP", "IMPORTO_CUP", "IVA", "TIPO") AS 
  select "PROGR_UNIVOCO","ESERCIZIO","DATA_PROTOCOLLO","NUMERO_PROTOCOLLO","NR_FATTURA_FORNITORE","DT_FATTURA_FORNITORE","RAGIONE_SOCIALE","COGNOME","NOME","CODICE_FISCALE","PARTITA_IVA","DS_FATTURA_PASSIVA","IM_TOTALE_FATTURA","DT_SCADENZA","STATO","ESERCIZIO_OBBLIGAZIONE","CD_CDS_OBBLIGAZIONE","PG_OBBLIGAZIONE","PG_OBBLIGAZIONE_SCADENZARIO","ESERCIZIO_ORI_OBBLIGAZIONE","CD_CIG","CD_CUP","IMPORTO_CUP","IVA_RILEVANTE","TIPO" from (
SELECT progr_univoco, fattura_passiva.esercizio,nvl(fattura_passiva.data_protocollo,fattura_passiva.dt_registrazione) data_protocollo,nvl(fattura_passiva.numero_protocollo,fattura_passiva.progr_univoco) numero_protocollo, nr_fattura_fornitore,dt_fattura_fornitore,
 ragione_sociale,cognome,nome, codice_fiscale,partita_iva,fattura_passiva.ds_fattura_passiva,im_totale_fattura,fattura_passiva.dt_scadenza,decode(fattura_passiva.Stato_cofi,'C','Contabilizzata','P','Pagata','A','Annullata','Pagata Parzialmente') stato,
 fattura_passiva_riga.esercizio_obbligazione,fattura_passiva_riga.cd_cds_obbligazione,fattura_passiva_riga.pg_obbligazione,fattura_passiva_riga.pg_obbligazione_scadenzario,fattura_passiva_riga.esercizio_ori_obbligazione,
 contratto.cd_cig,contratto.cd_cup,decode(contratto.cd_cup,null,0,sum(decode(IM_DIPONIBILE_NC-im_scadenza,abs(IM_DIPONIBILE_NC-im_scadenza),im_scadenza,IM_DIPONIBILE_NC)))  importo_cup,decode(fattura_passiva.TI_ISTITUZ_COMMERC,'C','Y','N') iva_rilevante,decode(ti_fattura,'F','Passiva','C','Nota Cred.','Nota Deb.') tipo
 FROM fattura_passiva_riga, fattura_passiva, obbligazione,obbligazione_scadenzario,contratto
  WHERE      --fattura_passiva.stato_cofi!='P'     and
  					 fattura_passiva.progr_univoco is not null and
             fattura_passiva.cd_cds                  = fattura_passiva_riga.cd_cds
        AND fattura_passiva.cd_unita_organizzativa  = fattura_passiva_riga.cd_unita_organizzativa
        AND fattura_passiva.esercizio               = fattura_passiva_riga.esercizio
        AND fattura_passiva.pg_fattura_passiva      = fattura_passiva_riga.pg_fattura_passiva
        AND obbligazione.cd_cds                 = obbligazione_scadenzario.cd_cds
        AND obbligazione.esercizio              = obbligazione_scadenzario.esercizio
        AND obbligazione.esercizio_originale    = obbligazione_scadenzario.esercizio_originale
        AND obbligazione.pg_obbligazione        = obbligazione_scadenzario.pg_obbligazione
        AND obbligazione_scadenzario.cd_cds                      = fattura_passiva_riga.cd_cds_obbligazione
        AND obbligazione_scadenzario.esercizio                   = fattura_passiva_riga.esercizio_obbligazione
        AND obbligazione_scadenzario.esercizio_originale         = fattura_passiva_riga.esercizio_ori_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione             = fattura_passiva_riga.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario = fattura_passiva_riga.pg_obbligazione_scadenzario
        AND contratto.esercizio         (+)= obbligazione.esercizio_contratto
        AND contratto.stato             (+)= obbligazione.stato_contratto
        AND contratto.pg_contratto      (+)= obbligazione.pg_contratto
  group by progr_univoco, fattura_passiva.esercizio,nvl(fattura_passiva.data_protocollo,fattura_passiva.dt_registrazione),nvl(fattura_passiva.numero_protocollo,fattura_passiva.progr_univoco), nr_fattura_fornitore,dt_fattura_fornitore,
 ragione_sociale,cognome,nome, codice_fiscale,partita_iva,fattura_passiva.ds_fattura_passiva,im_totale_fattura,fattura_passiva.dt_scadenza,decode(fattura_passiva.Stato_cofi,'C','Contabilizzata','P','Pagata','A','Annullata','Pagata Parzialmente') ,
 fattura_passiva_riga.esercizio_obbligazione,fattura_passiva_riga.cd_cds_obbligazione,fattura_passiva_riga.pg_obbligazione,fattura_passiva_riga.pg_obbligazione_scadenzario,fattura_passiva_riga.esercizio_ori_obbligazione,
 contratto.cd_cig, contratto.cd_cup, decode(fattura_passiva.TI_ISTITUZ_COMMERC,'C','Y','N'),decode(ti_fattura,'F','Passiva','C','Nota Cred.','Nota Deb.')
union all
SELECT progr_univoco,  fattura_passiva.esercizio,nvl(fattura_passiva.data_protocollo,fattura_passiva.dt_registrazione) data_protocollo,nvl(fattura_passiva.numero_protocollo,fattura_passiva.progr_univoco) numero_protocollo, nr_fattura_fornitore,dt_fattura_fornitore,
 ragione_sociale,cognome,nome, codice_fiscale,partita_iva,fattura_passiva.ds_fattura_passiva,im_totale_fattura,fattura_passiva.dt_scadenza,decode(fattura_passiva.Stato_cofi,'C','Contabilizzata','P','Pagata','A','Annullata','Pagata Parzialmente') stato,
 fattura_passiva_riga.esercizio_accertamento,fattura_passiva_riga.cd_cds_accertamento,fattura_passiva_riga.pg_accertamento,fattura_passiva_riga.pg_accertamento_scadenzario,fattura_passiva_riga.esercizio_ori_accertamento,
 contratto.cd_cig,contratto.cd_cup,decode(contratto.cd_cup,null,0,sum(decode(IM_DIPONIBILE_NC-im_scadenza,abs(IM_DIPONIBILE_NC-im_scadenza),im_scadenza,IM_DIPONIBILE_NC))) importo_cup,decode(fattura_passiva.TI_ISTITUZ_COMMERC,'C','Y','N') iva_rilevante,decode(ti_fattura,'F','Passiva','C','Nota Cred.','Nota Deb.') tipo
 FROM fattura_passiva_riga, fattura_passiva, accertamento,accertamento_scadenzario,contratto
  WHERE      --fattura_passiva.stato_cofi!='P'     and
  					 fattura_passiva.progr_univoco is not null and
             fattura_passiva.cd_cds                 = fattura_passiva_riga.cd_cds
        AND fattura_passiva.cd_unita_organizzativa  = fattura_passiva_riga.cd_unita_organizzativa
        AND fattura_passiva.esercizio               = fattura_passiva_riga.esercizio
        AND fattura_passiva.pg_fattura_passiva      = fattura_passiva_riga.pg_fattura_passiva
        AND accertamento.cd_cds                 = accertamento_scadenzario.cd_cds
        AND accertamento.esercizio              = accertamento_scadenzario.esercizio
        AND accertamento.esercizio_originale    = accertamento_scadenzario.esercizio_originale
        AND accertamento.pg_accertamento        = accertamento_scadenzario.pg_accertamento
        AND accertamento_scadenzario.cd_cds                      = fattura_passiva_riga.cd_cds_accertamento
        AND accertamento_scadenzario.esercizio                   = fattura_passiva_riga.esercizio_accertamento
        AND accertamento_scadenzario.esercizio_originale         = fattura_passiva_riga.esercizio_ori_accertamento
        AND accertamento_scadenzario.pg_accertamento             = fattura_passiva_riga.pg_accertamento
        AND accertamento_scadenzario.pg_accertamento_scadenzario = fattura_passiva_riga.pg_accertamento_scadenzario
        AND contratto.esercizio (+)        = accertamento.esercizio_contratto
        AND contratto.stato     (+)        = accertamento.stato_contratto
        AND contratto.pg_contratto (+)     = accertamento.pg_contratto
        group by
  progr_univoco,fattura_passiva.esercizio,nvl(fattura_passiva.data_protocollo,fattura_passiva.dt_registrazione),nvl(fattura_passiva.numero_protocollo,fattura_passiva.progr_univoco), nr_fattura_fornitore,dt_fattura_fornitore,
 ragione_sociale,cognome,nome, codice_fiscale,partita_iva,fattura_passiva.ds_fattura_passiva,im_totale_fattura,fattura_passiva.dt_scadenza,decode(fattura_passiva.Stato_cofi,'C','Contabilizzata','P','Pagata','A','Annullata','Pagata Parzialmente'),
 fattura_passiva_riga.esercizio_accertamento,fattura_passiva_riga.cd_cds_accertamento,fattura_passiva_riga.pg_accertamento,fattura_passiva_riga.pg_accertamento_scadenzario,fattura_passiva_riga.esercizio_ori_accertamento,
 contratto.cd_cig,contratto.cd_cup,decode(fattura_passiva.TI_ISTITUZ_COMMERC,'C','Y','N'),decode(ti_fattura,'F','Passiva','C','Nota Cred.','Nota Deb.')
union all
SELECT progr_univoco,  fattura_passiva.esercizio,nvl(fattura_passiva.data_protocollo,fattura_passiva.dt_registrazione) data_protocollo,nvl(fattura_passiva.numero_protocollo,fattura_passiva.progr_univoco) numero_protocollo,fattura_passiva. nr_fattura_fornitore,fattura_passiva.dt_fattura_fornitore,
 fattura_passiva.ragione_sociale,fattura_passiva.cognome,fattura_passiva.nome, fattura_passiva.codice_fiscale,fattura_passiva.partita_iva,fattura_passiva.ds_fattura_passiva,im_totale_fattura,fattura_passiva.dt_scadenza,
 decode(compenso.Stato_cofi,'C','Contabilizzata','P','Pagata','A','Annullata','Pagata Parzialmente') stato,
 compenso.esercizio_obbligazione,compenso.cd_cds_obbligazione,compenso.pg_obbligazione,compenso.pg_obbligazione_scadenzario,compenso.esercizio_ori_obbligazione,
 contratto.cd_cig,contratto.cd_cup,decode(contratto.cd_cup,null,0,sum(decode(im_totale_fattura-im_scadenza,abs(im_totale_fattura-im_scadenza),im_scadenza,im_totale_fattura)))  importo_cup,decode(fattura_passiva.TI_ISTITUZ_COMMERC,'C','Y','N') iva_rilevante,decode(ti_fattura,'F','Passiva','C','Nota Cred.','Nota Deb.') tipo
 FROM fattura_passiva_riga, fattura_passiva, compenso,obbligazione,obbligazione_scadenzario,contratto
  WHERE      --compenso.stato_cofi !='P' and
  					 FL_FATTURA_COMPENSO = 'Y'          and
             fattura_passiva.progr_univoco is not null and
             fattura_passiva.cd_cds                  = fattura_passiva_riga.cd_cds
        AND fattura_passiva.cd_unita_organizzativa   = fattura_passiva_riga.cd_unita_organizzativa
        AND fattura_passiva.esercizio                = fattura_passiva_riga.esercizio
        AND fattura_passiva.pg_fattura_passiva       = fattura_passiva_riga.pg_fattura_passiva
        and fattura_passiva.esercizio_compenso  = compenso.esercizio
        and fattura_passiva.CDS_COMPENSO              = compenso.cd_cds
        and fattura_passiva.UO_COMPENSO                  = compenso.cd_unita_organizzativa
        and fattura_passiva.PG_COMPENSO                  = compenso.pg_compenso
        AND obbligazione.cd_cds                 = obbligazione_scadenzario.cd_cds
        AND obbligazione.esercizio              = obbligazione_scadenzario.esercizio
        AND obbligazione.esercizio_originale    = obbligazione_scadenzario.esercizio_originale
        AND obbligazione.pg_obbligazione        = obbligazione_scadenzario.pg_obbligazione
        AND obbligazione_scadenzario.cd_cds                      = compenso.cd_cds_obbligazione
        AND obbligazione_scadenzario.esercizio                   = compenso.esercizio_obbligazione
        AND obbligazione_scadenzario.esercizio_originale         = compenso.esercizio_ori_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione             = compenso.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario = compenso.pg_obbligazione_scadenzario
        AND contratto.esercizio    (+)     = obbligazione.esercizio_contratto
        AND contratto.stato          (+)   = obbligazione.stato_contratto
        AND contratto.pg_contratto    (+)  = obbligazione.pg_contratto
 group by progr_univoco, fattura_passiva.esercizio,nvl(fattura_passiva.data_protocollo,fattura_passiva.dt_registrazione),nvl(fattura_passiva.numero_protocollo,fattura_passiva.progr_univoco), fattura_passiva.nr_fattura_fornitore,fattura_passiva.dt_fattura_fornitore,
 fattura_passiva.ragione_sociale,fattura_passiva.cognome,fattura_passiva.nome, fattura_passiva.codice_fiscale,fattura_passiva.partita_iva,fattura_passiva.ds_fattura_passiva,im_totale_fattura,fattura_passiva.dt_scadenza,decode(compenso.Stato_cofi,'C','Contabilizzata','P','Pagata','A','Annullata','Pagata Parzialmente'),
 compenso.esercizio_obbligazione,compenso.cd_cds_obbligazione,compenso.pg_obbligazione,compenso.pg_obbligazione_scadenzario,compenso.esercizio_ori_obbligazione,
 contratto.cd_cig, contratto.cd_cup, decode(fattura_passiva.TI_ISTITUZ_COMMERC,'C','Y','N'),decode(ti_fattura,'F','Passiva','C','Nota Cred.','Nota Deb.'))
        order by progr_univoco;
