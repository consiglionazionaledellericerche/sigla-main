--------------------------------------------------------
--  DDL for View V_F24EP2
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_F24EP2" ("TIPO_RIGA_F24", "CODICE_TRIBUTO", "CODICE_ENTE", "MESE_RIF", "ANNO_RIF", "IMPORTO_DEBITO", "ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "PG_LIQUIDAZIONE", "PROG", "CD_MATRICOLA_INPS", "PERIODO_DA", "PERIODO_A") AS 
  Select
 cd_tipo_riga_f24,
 CODICE_TRIBUTO,
 CODICE_ENTE,
 MESE_RIF,
 ANNO_RIF,
 IMPORTO_DEBITO,
 ESERCIZIO,
 CD_CDS,
 CD_UNITA_ORGANIZZATIVA ,
 --CD_CDS_COMPENSO,
 --CD_UO_COMPENSO,
 PG_LIQUIDAZIONE,
 --PG_LIQUIDAZIONE_ORIG,
 ROWNUM,
 cd_matricola_inps,
 periodo_da,
 periodo_a from(
Select
 cd_tipo_riga_f24,
 CODICE_TRIBUTO,
 CODICE_ENTE,
 MESE_RIF,
 ANNO_RIF,
 sum(IMPORTO_DEBITO) IMPORTO_DEBITO,
 ESERCIZIO,
 CD_CDS,
 CD_UNITA_ORGANIZZATIVA ,
 PG_LIQUIDAZIONE,
 cd_matricola_inps,
 periodo_da,
 periodo_a
 From(
        Select
        e.cd_tipo_riga_f24,
        e.cd_tributo_erario CODICE_TRIBUTO,
        Decode(liq_det.cd_regione,'*',Decode(liq_det.pg_comune,'0',Null,COMUNE.CD_CATASTALE),regione.cd_regione_uff) CODICE_ENTE,
        To_Char(cori.dt_a,'mm') mese_rif,
        Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)) anno_rif,
        Sum(b.ammontare) IMPORTO_DEBITO,
        liq_det.esercizio ESERCIZIO,
        liq_det.cd_cds CD_CDS,
        liq_det.cd_unita_organizzativa CD_UNITA_ORGANIZZATIVA,
        --a.cd_cds CD_CDS_COMPENSO,
        --a.cd_unita_organizzativa CD_UO_COMPENSO,
        liq_det.pg_liquidazione PG_LIQUIDAZIONE
        --,liq_det.pg_liquidazione PG_LIQUIDAZIONE_ORIG
        ,cd_matricola_inps
        --,To_Char(cori.dt_da,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_da,'yyyy'),To_Char(To_Number(To_Char(cori.dt_da,'yyyy'))-1)) periodo_da
        ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)) periodo_da
        ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)) periodo_a
        From
        compenso a, contributo_ritenuta b, tipo_contributo_ritenuta tipo, gruppo_cr e,
        LIQUID_GRUPPO_CORI_DET liq_det,liquid_cori cori,COMUNE,regione
        where
             b.cd_cds                   = a.cd_cds
         and b.esercizio                = a.esercizio
         and b.cd_unita_organizzativa   = a.cd_unita_organizzativa
         and b.pg_compenso              = a.pg_compenso
         and CNRCTB545.getIsAddComunale(tipo.cd_classificazione_cori)='N'
         and tipo.cd_contributo_ritenuta= b.cd_contributo_ritenuta
         and tipo.dt_ini_validita       = b.dt_ini_validita
         and b.esercizio                = liq_det.esercizio_contributo_ritenuta
         and b.cd_cds                   = liq_det.cd_cds_origine
         and b.cd_unita_organizzativa   = liq_det.cd_uo_origine
         and b.pg_compenso              = liq_det.pg_compenso
         and b.cd_contributo_ritenuta   = liq_det.cd_contributo_ritenuta
         and b.ti_ente_percipiente      = liq_det.ti_ente_percipiente
         and liq_det.esercizio          = e.esercizio
         and liq_det.cd_gruppo_cr       = e.cd_gruppo_cr
         And liq_det.esercizio          = cori.esercizio
         And liq_det.cd_cds             = cori.cd_cds
         And liq_det.cd_unita_organizzativa = cori.cd_unita_organizzativa
         and liq_det.pg_liquidazione   = cori.pg_liquidazione
         And liq_det.pg_comune        = comune.PG_COMUNE
         And ti_italiano_estero 	='I'
         And dt_canc is Null
         And regione.cd_regione = liq_det.cd_regione
         and (e.fl_f24online = 'Y' or
         e.FL_F24ONLINE_PREVID ='Y' )
 Group By
         e.cd_tipo_riga_f24,
         e.cd_tributo_erario,
         Decode(liq_det.cd_regione,'*',Decode(liq_det.pg_comune,'0',Null,COMUNE.CD_CATASTALE),regione.cd_regione_uff),
         To_Char(cori.dt_a,'mm'),
         Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)),
         liq_det.esercizio,
         liq_det.cd_cds,
         liq_det.cd_unita_organizzativa,
         --a.cd_cds,
         --a.cd_unita_organizzativa,
         liq_det.pg_liquidazione
         --,liq_det.pg_liquidazione
         ,cd_matricola_inps
         --,To_Char(cori.dt_da,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_da,'yyyy'),To_Char(To_Number(To_Char(cori.dt_da,'yyyy'))-1))
         ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1))
         ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1))
union All
        Select
        e.cd_tipo_riga_f24,
        e.cd_tributo_erario CODICE_TRIBUTO,
        Decode(liq_det.cd_regione,'*',Decode(liq_det.pg_comune,'0',Null,COMUNE.CD_CATASTALE),regione.cd_regione_uff) CODICE_ENTE,
        To_Char(cori.dt_a,'mm') mese_rif,
        Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)) anno_rif,
        Sum(b.ammontare) IMPORTO_DEBITO,
        v_liq.esercizio ESERCIZIO,
        v_liq.cd_cds CD_CDS,
        v_liq.cd_unita_organizzativa CD_UNITA_ORGANIZZATIVA,
        --a.cd_cds CD_CDS_COMPENSO,
        --a.cd_unita_organizzativa CD_UO_COMPENSO,
        v_liq.pg_liquidazione PG_LIQUIDAZIONE
        --,liq_det.pg_liquidazione PG_LIQUIDAZIONE_ORIG
        ,cd_matricola_inps
        --,To_Char(cori.dt_da,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_da,'yyyy'),To_Char(To_Number(To_Char(cori.dt_da,'yyyy'))-1)) periodo_da
        ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)) periodo_da
        ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)) periodo_a
        From
        compenso a, contributo_ritenuta b, tipo_contributo_ritenuta tipo, gruppo_cr e, V_LIQUID_CENTRO_UO v_liq,
        LIQUID_GRUPPO_CORI_DET liq_det,liquid_cori cori,COMUNE,regione
        where
             b.cd_cds                   = a.cd_cds
         and b.esercizio                = a.esercizio
         and b.cd_unita_organizzativa   = a.cd_unita_organizzativa
         and b.pg_compenso              = a.pg_compenso
         --and CNRCTB545.getIsAddComunale(tipo.cd_classificazione_cori)='N'
         and tipo.cd_contributo_ritenuta= b.cd_contributo_ritenuta
         and tipo.dt_ini_validita       = b.dt_ini_validita
         and b.esercizio                = liq_det.esercizio_contributo_ritenuta
         and b.cd_cds                   = liq_det.cd_cds_origine
         and b.cd_unita_organizzativa   = liq_det.cd_uo_origine
         and b.pg_compenso              = liq_det.pg_compenso
         and b.cd_contributo_ritenuta   = liq_det.cd_contributo_ritenuta
         and b.ti_ente_percipiente      = liq_det.ti_ente_percipiente
         and liq_det.esercizio          = e.esercizio
         and liq_det.cd_gruppo_cr       = e.cd_gruppo_cr
         And liq_det.pg_comune          = comune.PG_COMUNE
         And ti_italiano_estero 	='I'
         And dt_canc is null
         and (e.fl_f24online = 'Y' or
         e.FL_F24ONLINE_PREVID ='Y' )
         And regione.cd_regione               = liq_det.cd_regione
         and liq_det.esercizio                = v_liq.esercizio
         and liq_det.cd_unita_organizzativa   = v_liq.cd_uo_origine
         and liq_det.cd_cds                   = v_liq.cd_cds_origine
         and liq_det.pg_liquidazione          = v_liq.pg_liquidazione_origine
         and liq_det.CD_GRUPPO_CR             = v_liq.CD_GRUPPO_CR
         and liq_det.CD_REGIONE               = v_liq.CD_REGIONE
         and liq_det.PG_COMUNE                = v_liq.PG_COMUNE
         And v_liq.esercizio                  = cori.esercizio
         And v_liq.cd_cds                     = cori.cd_cds
         And v_liq.cd_unita_organizzativa     = cori.cd_unita_organizzativa
         and v_liq.pg_liquidazione            = cori.pg_liquidazione
Group By
        e.cd_tipo_riga_f24,
        e.cd_tributo_erario,
         Decode(liq_det.cd_regione,'*',Decode(liq_det.pg_comune,'0',Null,COMUNE.CD_CATASTALE),regione.cd_regione_uff),
         To_Char(cori.dt_a,'mm'),
         Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)),
         v_liq.esercizio,
         v_liq.cd_cds,
         v_liq.cd_unita_organizzativa,
         --a.cd_cds,
         --a.cd_unita_organizzativa,
         v_liq.pg_liquidazione
         --,liq_det.pg_liquidazione
         ,cd_matricola_inps
         --,To_Char(cori.dt_da,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_da,'yyyy'),To_Char(To_Number(To_Char(cori.dt_da,'yyyy'))-1))
         ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1))
         ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1))
 Union All
        Select
        e.cd_tipo_riga_f24,
        e.cd_tributo_erario CODICE_TRIBUTO,
        Decode(liq_det.cd_regione,'*',Decode(liq_det.pg_comune,'0',Null,COMUNE.CD_CATASTALE),regione.cd_regione_uff) CODICE_ENTE,
        To_Char(cori.dt_a,'mm') mese_rif,
        Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)) anno_rif,
        Sum(b.ammontare) IMPORTO_DEBITO,
        liq_det.esercizio ESERCIZIO,
        liq_det.cd_cds CD_CDS,
        liq_det.cd_unita_organizzativa CD_UNITA_ORGANIZZATIVA,
        --a.cd_cds CD_CDS_COMPENSO,
        --a.cd_unita_organizzativa CD_UO_COMPENSO,
        liq_det.pg_liquidazione PG_LIQUIDAZIONE
        --,liq_det.pg_liquidazione PG_LIQUIDAZIONE_ORIG
        ,cd_matricola_inps
        --,To_Char(cori.dt_da,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_da,'yyyy'),To_Char(To_Number(To_Char(cori.dt_da,'yyyy'))-1)) periodo_da
        ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)) periodo_da
        ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)) periodo_a
        From
        compenso a, contributo_ritenuta b, tipo_contributo_ritenuta tipo, gruppo_cr e,
        LIQUID_GRUPPO_CORI_DET liq_det,liquid_cori cori,COMUNE,regione
        where
             b.cd_cds                   = a.cd_cds
         and b.esercizio                = a.esercizio
         and b.cd_unita_organizzativa   = a.cd_unita_organizzativa
         and b.pg_compenso              = a.pg_compenso
         and CNRCTB545.getIsAddComunale(tipo.cd_classificazione_cori)='Y'
         AND FL_COMPENSO_STIPENDI = 'N'
         and tipo.cd_contributo_ritenuta= b.cd_contributo_ritenuta
         and tipo.dt_ini_validita       = b.dt_ini_validita
         and b.esercizio                = liq_det.esercizio_contributo_ritenuta
         and b.cd_cds                   = liq_det.cd_cds_origine
         and b.cd_unita_organizzativa   = liq_det.cd_uo_origine
         and b.pg_compenso              = liq_det.pg_compenso
         and b.cd_contributo_ritenuta   = liq_det.cd_contributo_ritenuta
         and b.ti_ente_percipiente      = liq_det.ti_ente_percipiente
         and liq_det.esercizio          = e.esercizio
         and liq_det.cd_gruppo_cr       = e.cd_gruppo_cr
         And liq_det.esercizio          = cori.esercizio
         And liq_det.cd_cds             = cori.cd_cds
         And liq_det.cd_unita_organizzativa = cori.cd_unita_organizzativa
         and liq_det.pg_liquidazione   = cori.pg_liquidazione
         And liq_det.pg_comune        = comune.PG_COMUNE
         And ti_italiano_estero 	='I'
         And dt_canc is null
         and (e.fl_f24online = 'Y' or
         e.FL_F24ONLINE_PREVID ='Y' )
         And regione.cd_regione = liq_det.cd_regione
 Group By
         e.cd_tipo_riga_f24,
         e.cd_tributo_erario,
         Decode(liq_det.cd_regione,'*',Decode(liq_det.pg_comune,'0',Null,COMUNE.CD_CATASTALE),regione.cd_regione_uff),
         To_Char(cori.dt_a,'mm'),
         Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1)),
         liq_det.esercizio,
         liq_det.cd_cds,
         liq_det.cd_unita_organizzativa,
         --a.cd_cds,
         --a.cd_unita_organizzativa,
         liq_det.pg_liquidazione
         --,liq_det.pg_liquidazione
         ,cd_matricola_inps
        -- ,To_Char(cori.dt_da,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_da,'yyyy'),To_Char(To_Number(To_Char(cori.dt_da,'yyyy'))-1))
         ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1))
         ,To_Char(cori.dt_a,'mm')||Decode(e.fl_anno_prec,'N',To_Char(cori.dt_a,'yyyy'),To_Char(To_Number(To_Char(cori.dt_a,'yyyy'))-1))
Union All
        Select
        e.cd_tipo_riga_f24,
        e.cd_tributo_erario CODICE_TRIBUTO,
        STIP.CD_CATASTALE CODICE_ENTE,
        Decode(To_Char(stip.mese),'13','12','15','11',lpad(To_Char(stip.mese),2,'0'))  mese_rif,
        Decode(e.fl_anno_prec,'N',To_Char(stip.esercizio),To_Char(stip.esercizio-1)) anno_rif,
        Sum(stip.saldo) IMPORTO_DEBITO,
        liq_det.esercizio ESERCIZIO,
        liq_det.cd_cds CD_CDS,
        liq_det.cd_unita_organizzativa CD_UNITA_ORGANIZZATIVA,
        --a.cd_cds CD_CDS_COMPENSO,
        --a.cd_unita_organizzativa CD_UO_COMPENSO,
        liq_det.pg_liquidazione PG_LIQUIDAZIONE
        --,liq_det.pg_liquidazione PG_LIQUIDAZIONE_ORIG
        ,cd_matricola_inps
        ,Decode(To_Char(stip.mese),'13','12','15','11',lpad(To_Char(stip.mese),2,'0'))||Decode(e.fl_anno_prec,'N',To_Char(stip.esercizio),To_Char(stip.esercizio-1)) periodo_da
        ,Decode(To_Char(stip.mese),'13','12','15','11',lpad(To_Char(stip.mese),2,'0'))||Decode(e.fl_anno_prec,'N',To_Char(stip.esercizio),To_Char(stip.esercizio-1)) periodo_a
        From
        compenso a, contributo_ritenuta b, tipo_contributo_ritenuta tipo, gruppo_cr e,
        LIQUID_GRUPPO_CORI_DET liq_det,comune,
	stipendi_cofi_cori_dett stip,stipendi_cofi cofi
        where
             b.cd_cds                   = a.cd_cds
         and b.esercizio                = a.esercizio
         and b.cd_unita_organizzativa   = a.cd_unita_organizzativa
         and b.pg_compenso              = a.pg_compenso
         and CNRCTB545.getIsAddComunale(tipo.cd_classificazione_cori)='Y'
         AND FL_COMPENSO_STIPENDI = 'Y'
         and tipo.cd_contributo_ritenuta= b.cd_contributo_ritenuta
         and tipo.dt_ini_validita       = b.dt_ini_validita
         and b.esercizio                = liq_det.esercizio_contributo_ritenuta
         and b.cd_cds                   = liq_det.cd_cds_origine
         and b.cd_unita_organizzativa   = liq_det.cd_uo_origine
         and b.pg_compenso              = liq_det.pg_compenso
         and b.cd_contributo_ritenuta   = liq_det.cd_contributo_ritenuta
         and b.ti_ente_percipiente      = liq_det.ti_ente_percipiente
         and liq_det.esercizio          = e.esercizio
         and liq_det.cd_gruppo_cr       = e.cd_gruppo_cr
         And stip.saldo 		 >0
         and stip.tipo_flusso		='C'
         And stip.cd_catastale          = comune.cd_catastale
         And ti_italiano_estero 	='I'
         And dt_canc is null
         and cofi.cd_cds_comp            = a.cd_cds
         and cofi.esercizio_comp         = a.esercizio
         and cofi.cd_uo_comp 		 = a.cd_unita_organizzativa
         and cofi.pg_comp                = a.pg_compenso
         and stip.esercizio              = cofi.esercizio
         and stip.mese 			 = cofi.mese
         and stip.cd_contributo_ritenuta = liq_det.cd_contributo_ritenuta
         and stip.ti_ente_percipiente    = liq_det.ti_ente_percipiente
         And STATO_COFI!='A'
         And cofi.stato = 'P'
         and (e.fl_f24online = 'Y' or
         e.FL_F24ONLINE_PREVID ='Y' )
 Group By
        e.cd_tipo_riga_f24,
        e.cd_tributo_erario,
        STIP.CD_CATASTALE,
        Decode(To_Char(stip.mese),'13','12','15','11',lpad(To_Char(stip.mese),2,'0')),
        Decode(e.fl_anno_prec,'N',To_Char(stip.esercizio),To_Char(stip.esercizio-1)),
        liq_det.esercizio,
        liq_det.cd_cds,
        liq_det.cd_unita_organizzativa,
        --a.cd_cds,
        --a.cd_unita_organizzativa,
        liq_det.pg_liquidazione
        --,liq_det.pg_liquidazione
        ,cd_matricola_inps
        ,Decode(To_Char(stip.mese),'13','12','15','11',lpad(To_Char(stip.mese),2,'0'))||Decode(e.fl_anno_prec,'N',To_Char(stip.esercizio),To_Char(stip.esercizio-1))
        ,Decode(To_Char(stip.mese),'13','12','15','11',lpad(To_Char(stip.mese),2,'0'))||Decode(e.fl_anno_prec,'N',To_Char(stip.esercizio),To_Char(stip.esercizio-1))
) group by
 cd_tipo_riga_f24,
 CODICE_TRIBUTO,
 CODICE_ENTE,
 MESE_RIF,
 ANNO_RIF,
 ESERCIZIO,
 CD_CDS,
 CD_UNITA_ORGANIZZATIVA ,
 PG_LIQUIDAZIONE
 ,cd_matricola_inps
 ,periodo_da
 ,periodo_a
);
