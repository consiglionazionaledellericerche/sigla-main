--------------------------------------------------------
--  DDL for View V_DOCUMENTI_FLUSSO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOCUMENTI_FLUSSO" ("CD_CDS", "ESERCIZIO", "PG_DOCUMENTO", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_DOCUMENTO_CONT", "TI_DOCUMENTO", "TI_COMPETENZA_RESIDUO", "DS_DOCUMENTO", "STATO", "CD_TIPO_DOCUMENTO_AMM", "PG_DOC_AMM", "DT_EMISSIONE", "DT_TRASMISSIONE", "DT_PAGAMENTO", "DT_ANNULLAMENTO", "IM_DOCUMENTO", "IM_PAGATO", "STATO_TRASMISSIONE", "DT_RITRASMISSIONE", "CD_TERZO", "CD_ANAG", "DENOMINAZIONE_SEDE", "CD_SIOPE", "CD_CUP", "DT_REGISTRAZIONE_SOSP", "TI_ENTRATA_SPESA", "CD_SOSPESO", "IM_SOSPESO", "VIA_SEDE", "CAP_COMUNE_SEDE", "DS_COMUNE", "CD_PROVINCIA", "CODICE_FISCALE", "ABI", "CAB", "CIN", "NUMERO_CONTO", "BIC", "CD_ISO", "CODICE_IBAN", "ASSOGGETTAMENTO_BOLLO", "CAUSALE_BOLLO", "MODALITA_PAGAMENTO", "IM_ASSOCIATO", "IMPORTO_CGE", "IMPORTO_CUP", "DT_PAGAMENTO_RICHIESTA") AS 
  select  v.cd_cds,
        v.esercizio,
        v.pg_reversale,
        v.cd_unita_organizzativa,
        v.cd_cds_origine,
        v.cd_uo_origine,
        v.cd_tipo_documento_cont,
        v.ti_reversale,
        v.ti_competenza_residuo,
        v.ds_reversale,
        v.stato,
        null,--r.CD_TIPO_DOCUMENTO_AMM, -- in questo caso non serve raggruppare
				null,--r.PG_DOC_AMM,       --in questo caso non serve raggruppare
        v.dt_emissione,
        v.dt_trasmissione,
        v.dt_incasso,
        v.dt_annullamento,
        v.im_reversale,
        v.im_incassato,
        v.stato_trasmissione,
        v.dt_ritrasmissione,
        v.cd_terzo,
        v.cd_anag,
        v.denominazione_sede,
        cge.CD_SIOPE,
        c.CD_CUP, --eliminato commento per inserire nella causale i cup cmq indicati
        s.dt_registrazione,
        sosp.ti_entrata_spesa,
        sosp.cd_sospeso,
        s.IM_SOSPESO,
        terzo_uo.via_sede via_ben,
        terzo_uo.cap_comune_sede cap_ben,
        com_ben.ds_comune comune_ben,
        com_ben.cd_provincia provincia_ben,
        a.codice_fiscale,
        null, -- abi
        null, -- cab
        null, -- codiceCin
        b.numero_conto,
        null,
        substr(b.codice_iban,1,2) cd_iso,
        b.codice_iban,
        bollo.assoggettamento_bollo,
        bollo.ds_tipo_bollo,
        r.cd_modalita_pag,
        sosp.im_associato,
        --sum(sosp.im_associato),
        sum(cge.importo) importo_cge,
        0, --sum(c.importo) importo_cup,
        null --dt_pagamento_richiesta
        from v_reversale_terzo v,reversale_riga r, reversale_siope cge,reversale_siope_cup c, --OMESSA PER LE ENTRATE NON PREVISTA DA FLUSSO
        terzo terzo_uo,banca b,
         sospeso_det_etr sosp, sospeso s,comune com_ben, terzo t,anagrafico a,nazione n,tipo_bollo bollo, reversale_terzo rt
              where
              v.cd_anag = a.cd_anag    and
              v.cd_terzo = t.cd_terzo  and
              v.cd_cds = rt.cd_cds             and
              v.esercizio = rt.esercizio and
              v.pg_reversale = rt.pg_reversale    and
              v.cd_terzo = rt.cd_terzo  and
              rt.cd_tipo_bollo = bollo.cd_tipo_bollo and
              com_ben.pg_comune = t.PG_COMUNE_SEDE and
              v.cd_cds = r.cd_cds             and
              v.esercizio = r.esercizio and
              v.pg_reversale = r.pg_reversale    and
              terzo_uo.cd_terzo = b.cd_terzo  and
              r.pg_banca = b.pg_banca  and
              --
              r.cd_cds = cge.cd_cds 		and
              r.esercizio = cge.esercizio and
              r.pg_reversale = cge.pg_reversale and
              r.esercizio_accertamento = cge.esercizio_accertamento and
        		  r.esercizio_ori_accertamento =	cge.esercizio_ori_accertamento and
							r.pg_accertamento = cge.pg_accertamento and
        		  r.pg_accertamento_scadenzario = cge.pg_accertamento_scadenzario  and
							r.cd_cds_doc_amm = cge.cd_cds_doc_amm   and
        	    r.cd_uo_doc_amm = cge.cd_uo_doc_amm  and
        		  r.esercizio_doc_amm =  cge.esercizio_doc_amm  and
        			r.cd_tipo_documento_amm = cge.cd_tipo_documento_amm  and
       			  r.pg_doc_amm = cge.pg_doc_amm  and
       			  cge.importo!=0 and
        			/*eliminato commento per inserire nella causale i cup cmq indicati*/
              c.cd_cds (+)= cge.cd_cds 		and
              c.esercizio(+) = cge.esercizio and
              c.pg_reversale(+) = cge.pg_reversale and
              c.esercizio_accertamento(+) = cge.esercizio_accertamento and
        		  c.esercizio_ori_accertamento(+) =	cge.esercizio_ori_accertamento and
							c.pg_accertamento (+)= cge.pg_accertamento and
        		  c.pg_accertamento_scadenzario(+) = cge.pg_accertamento_scadenzario  and
							c.cd_cds_doc_amm (+)= cge.cd_cds_doc_amm   and
              c.cd_uo_doc_amm (+)= cge.cd_uo_doc_amm   and
              c.esercizio_doc_amm (+)= cge.esercizio_doc_amm  and
              c.cd_tipo_documento_amm (+)=  cge.cd_tipo_documento_amm    and
              c.pg_doc_amm (+)= cge.pg_doc_amm   and
              c.esercizio_siope (+)= cge.esercizio_siope    and
              c.ti_gestione (+)= cge.ti_gestione and
						  c.cd_siope (+)= cge.cd_siope   and
              /*eliminato commento per inserire nella causale i cup cmq indicati*/
              terzo_uo.cd_unita_organizzativa = v.cd_uo_origine and
              --
              v.cd_cds = sosp.cd_cds_reversale(+)  and
              v.esercizio = sosp.esercizio    (+)and
              v.pg_reversale = sosp.pg_reversale    (+)and
                     s.cd_cds(+) = sosp.cd_cds  and
                  s.esercizio(+) = sosp.esercizio    and
                    s.ti_entrata_spesa(+) = sosp.ti_entrata_spesa    and
                  s.ti_sospeso_riscontro(+) = sosp.ti_sospeso_riscontro and
                  s.cd_sospeso(+) = sosp.cd_sospeso and
                   sosp.ti_sospeso_riscontro (+)='S' and
                   sosp.stato(+)='N' and
                    com_ben.pg_nazione = n.pg_nazione
group by v.cd_cds,
        v.esercizio,
        v.pg_reversale,
        v.cd_unita_organizzativa,
        v.cd_cds_origine,
        v.cd_uo_origine,
        v.cd_tipo_documento_cont,
        v.ti_reversale,
        v.ti_competenza_residuo,
        v.ds_reversale,
        v.stato,
        null, --r.CD_TIPO_DOCUMENTO_AMM,   --in questo caso non serve raggruppare
				null, --r.PG_DOC_AMM,       in questo caso non serve raggruppare
        v.dt_emissione,
        v.dt_trasmissione,
        v.dt_incasso,
        v.dt_annullamento,
        v.im_reversale,
        v.im_incassato,
        v.stato_trasmissione,
        v.dt_ritrasmissione,
        v.cd_terzo,
        v.cd_anag,
        v.denominazione_sede,
        cge.CD_SIOPE,
        c.CD_CUP, /*eliminato commento per inserire nella causale i cup cmq indicati*/
        s.dt_registrazione,
        sosp.ti_entrata_spesa,
        sosp.cd_sospeso,
        s.IM_SOSPESO,
        terzo_uo.via_sede,
        terzo_uo.cap_comune_sede,
        com_ben.ds_comune,
        com_ben.cd_provincia,
        a.codice_fiscale,
        null, -- abi
        null, -- cab
        null, -- codiceCin
        b.numero_conto,
        substr(b.codice_iban,1,2),
        b.codice_iban,
        bollo.assoggettamento_bollo,
        bollo.ds_tipo_bollo,
        r.cd_modalita_pag,
        sosp.im_associato,
        com_ben.ti_italiano_estero
union
select  v.cd_cds,
        v.esercizio,
        v.pg_mandato,
        v.cd_unita_organizzativa,
        v.cd_cds_origine,
        v.cd_uo_origine,
        v.cd_tipo_documento_cont,
        v.ti_mandato,
        v.ti_competenza_residuo,
        v.ds_mandato,
        v.stato,
        ' ' CD_TIPO_DOCUMENTO_AMM,--decode(c.cd_cup,null,' ',m.CD_TIPO_DOCUMENTO_AMM) CD_TIPO_DOCUMENTO_AMM , -- se il cup è null non serve raggruppare
        0 PG_DOC_AMM,--decode(c.cd_cup,null,0,m.PG_DOC_AMM) PG_DOC_AMM , -- se il cup è null non serve raggruppare
				v.dt_emissione,
        v.dt_trasmissione,
        v.dt_pagamento,
        v.dt_annullamento,
        v.im_mandato,
        v.im_pagato,
        v.stato_trasmissione,
        v.dt_ritrasmissione,
        v.cd_terzo,
        v.cd_anag,
        v.denominazione_sede,
        cge.CD_SIOPE,
        c.CD_CUP,
        s.dt_registrazione,
        sosp.ti_entrata_spesa,
        sosp.cd_sospeso,
        s.IM_SOSPESO,
        terzo.via_sede via_ben,
        decode(a.ti_italiano_estero,'I',decode(com_ben.ti_italiano_estero,'I',terzo.cap_comune_sede,'00185'),'00185') cap_ben,
        com_ben.ds_comune comune_ben,
        decode(a.ti_italiano_estero,'I',decode(com_ben.ti_italiano_estero,'I',com_ben.cd_provincia,'RM'),'RM') provincia_ben,
        a.codice_fiscale,
        b.abi,
				b.cab,
			  b.cin,
        b.numero_conto,
        b.CODICE_SWIFT,
        substr(b.codice_iban,1,2) cd_iso,
        b.codice_iban,
        bollo.assoggettamento_bollo,
        bollo.ds_tipo_bollo,
        m.cd_modalita_pag,
        sosp.im_associato,
        --sum(sosp.im_associato),
        (select sum(mandato_siope.importo) from mandato_siope
        where
            v.cd_cds = mandato_siope.cd_cds         and
            v.esercizio = mandato_siope.esercizio    and
            v.pg_mandato = mandato_siope.pg_mandato  and
            cge.esercizio_siope= mandato_siope.esercizio_siope and
            cge.ti_gestione= mandato_siope.ti_gestione     and
            cge.cd_siope= mandato_siope.cd_siope
            ) importo_cge,
        sum(c.importo) importo_cup,
        mandato.dt_pagamento_richiesta
        from v_mandato_terzo v,mandato_riga m, mandato_siope cge,mandato_siope_cup c,terzo,banca b,
         sospeso_det_usc sosp, sospeso s,comune com_ben, terzo t,anagrafico a,nazione n,mandato_terzo mt,tipo_bollo bollo,mandato
              where
              mandato.cd_cds = mt.cd_cds             and
              mandato.esercizio = mt.esercizio and
              mandato.pg_mandato = mt.pg_mandato    and
              v.cd_anag = a.cd_anag    and
              v.cd_terzo = t.cd_terzo  and
              v.cd_cds = mt.cd_cds             and
              v.esercizio = mt.esercizio and
              v.pg_mandato = mt.pg_mandato    and
              v.cd_terzo = mt.cd_terzo  and
              mt.cd_tipo_bollo = bollo.cd_tipo_bollo and
              com_ben.pg_comune = t.PG_COMUNE_SEDE and
              v.cd_cds = m.cd_cds             and
              v.esercizio = m.esercizio and
              v.pg_mandato = m.pg_mandato    and
              terzo.cd_terzo = b.cd_terzo  and
              m.cd_terzo = b.cd_terzo   and
              m.pg_banca = b.pg_banca  and
              m.cd_cds = cge.cd_cds            and
              m.esercizio = cge.esercizio    and
              m.pg_mandato = cge.pg_mandato   and
        			m.esercizio_obbligazione = cge.esercizio_obbligazione  and
         			m.esercizio_ori_obbligazione = cge.esercizio_ori_obbligazione  and
        			m.pg_obbligazione = cge.pg_obbligazione   and
        		  m.pg_obbligazione_scadenzario =cge.pg_obbligazione_scadenzario  and
              m.cd_cds_doc_amm = cge.cd_cds_doc_amm   and
              m.cd_uo_doc_amm = cge.cd_uo_doc_amm   and
              m.esercizio_doc_amm = cge.esercizio_doc_amm  and
              m.cd_tipo_documento_amm =  cge.cd_tipo_documento_amm    and
              m.pg_doc_amm = cge.pg_doc_amm   and
              m.IM_MANDATO_RIGA!=0 and
              cge.importo!=0   and
       				--
              c.cd_cds (+)= cge.cd_cds            and
              c.esercizio (+)= cge.esercizio    and
              c.pg_mandato (+)= cge.pg_mandato   and
        			c.esercizio_obbligazione (+)= cge.esercizio_obbligazione  and
         			c.esercizio_ori_obbligazione (+)= cge.esercizio_ori_obbligazione  and
        			c.pg_obbligazione (+)= cge.pg_obbligazione   and
        		  c.pg_obbligazione_scadenzario (+)=cge.pg_obbligazione_scadenzario  and
              c.cd_cds_doc_amm (+)= cge.cd_cds_doc_amm   and
              c.cd_uo_doc_amm (+)= cge.cd_uo_doc_amm   and
              c.esercizio_doc_amm (+)= cge.esercizio_doc_amm  and
              c.cd_tipo_documento_amm (+)=  cge.cd_tipo_documento_amm    and
              c.pg_doc_amm (+)= cge.pg_doc_amm   and
              c.esercizio_siope (+)= cge.esercizio_siope    and
              c.ti_gestione (+)= cge.ti_gestione and
						  c.cd_siope (+)= cge.cd_siope   and
              --
              v.cd_cds = sosp.cd_cds_mandato(+)  and
              v.esercizio = sosp.esercizio    (+)and
              v.pg_mandato = sosp.pg_mandato    (+)and
              s.cd_cds(+) = sosp.cd_cds  and
              s.esercizio(+) = sosp.esercizio    and
              s.ti_entrata_spesa(+) = sosp.ti_entrata_spesa    and
              s.ti_sospeso_riscontro(+) = sosp.ti_sospeso_riscontro and
              s.cd_sospeso(+) = sosp.cd_sospeso and
              sosp.ti_sospeso_riscontro (+)='S' and
              sosp.stato (+)='N' and
              com_ben.pg_nazione = n.pg_nazione
              and c.importo(+) !=0
group by v.cd_cds,
        v.esercizio,
        v.pg_mandato,
        v.cd_unita_organizzativa,
        v.cd_cds_origine,
        v.cd_uo_origine,
        v.cd_tipo_documento_cont,
        v.ti_mandato,
        v.ti_competenza_residuo,
        v.ds_mandato,
        v.stato,
        ' ',--decode(c.cd_cup,null,' ',m.CD_TIPO_DOCUMENTO_AMM),
				0,--decode(c.cd_cup,null,0,m.PG_DOC_AMM),
        v.dt_emissione,
        v.dt_trasmissione,
        v.dt_pagamento,
        v.dt_annullamento,
        v.im_mandato,
        v.im_pagato,
        v.stato_trasmissione,
        v.dt_ritrasmissione,
        v.cd_terzo,
        v.cd_anag,
        v.denominazione_sede,
        cge.esercizio_siope,
        cge.ti_gestione,
        cge.CD_SIOPE,
        c.CD_CUP,
        s.dt_registrazione,
        sosp.ti_entrata_spesa,
        sosp.cd_sospeso,
        s.IM_SOSPESO,
        terzo.via_sede,
        terzo.cap_comune_sede,
        com_ben.ds_comune,
        com_ben.cd_provincia,
        a.codice_fiscale,
        b.abi,
				b.cab,
			  b.cin,
        b.numero_conto,
        b.CODICE_SWIFT,
        substr(b.codice_iban,1,2),
        b.codice_iban,
        bollo.assoggettamento_bollo,
        bollo.ds_tipo_bollo,
        m.cd_modalita_pag,
        a.ti_italiano_estero,
        sosp.im_associato,
        com_ben.ti_italiano_estero,
        mandato.dt_pagamento_richiesta;
