CREATE OR REPLACE procedure PCIR009.popola_pcc_modello3 is
begin
declare
cursor testata_pag is

select  fattura_passiva.esercizio,fattura_passiva.cd_unita_organizzativa,fattura_passiva.pg_fattura_passiva,ti_fattura,fattura_passiva.cd_terzo,
				fattura_passiva.cd_cds,fattura_passiva.im_totale_fattura,fattura_passiva.STATO_PAGAMENTO_FONDO_ECO, fattura_passiva.PG_COMPENSO,FL_FATTURA_COMPENSO ,
				DT_FATTURA_FORNITORE,NR_FATTURA_FORNITORE,fattura_passiva.identificativo_sdi,
				cd_iso,ti_nazione,terzo_uo.codice_univoco_ufficio_ipa,
anagrafico.partita_iva ana_partita_iva,anagrafico.codice_fiscale ana_codice_fiscale ,
fattura_passiva.fl_split_payment split,sum(fattura_passiva_riga.im_iva) tot_iva,sum(fattura_passiva_riga.im_imponibile) tot_imp,fattura_passiva.TI_ISTITUZ_COMMERC
from fattura_passiva,nazione,anagrafico,terzo,terzo terzo_uo,fattura_passiva_riga
where
	fattura_passiva.cd_cds = fattura_passiva_riga.cd_cds  and
   fattura_passiva.cd_unita_organizzativa = fattura_passiva_riga.cd_unita_organizzativa and
   fattura_passiva.esercizio = fattura_passiva_riga.esercizio  and
   fattura_passiva.pg_fattura_passiva =   fattura_passiva_riga.pg_fattura_passiva and
	 terzo_uo.cd_unita_organizzativa = fattura_passiva.cd_unita_organizzativa and
   anagrafico.cd_anag = terzo.cd_anag and


   terzo.cd_terzo  = fattura_passiva.cd_terzo and
   (terzo_uo.cd_terzo = (select min(cd_terzo) from terzo uo where
   uo.cd_unita_organizzativa =   fattura_passiva.cd_unita_organizzativa and
   fattura_passiva.identificativo_sdi is null) or
   (fattura_passiva.identificativo_sdi is not null and
   terzo_uo.cd_unita_organizzativa =   fattura_passiva.cd_unita_organizzativa
   and exists(select 1 from documento_ele_trasmissione where
   documento_ele_trasmissione.identificativo_sdi = fattura_passiva.identificativo_sdi and
   documento_ele_trasmissione.codice_destinatario = terzo_uo.CODICE_UNIVOCO_UFFICIO_IPA))) and
   nazione.pg_nazione =  anagrafico.pg_nazione_fiscale and
   (DT_FATTURA_FORNITORE    > (select dt01 from configurazione_cnr where
   cd_chiave_primaria   = 'REGISTRO_UNICO_FATPAS' and
   cd_chiave_secondaria = 'DATA_INIZIO') or
   (fattura_passiva.identificativo_sdi is not null))
   and (fattura_passiva.stato_cofi in('P','Q') or
   	    STATO_PAGAMENTO_FONDO_ECO = 'R' or
   	    (FL_FATTURA_COMPENSO = 'Y' and
   	     exists(select 1 from compenso
   	     where
   	     compenso.stato_cofi='P' and
   	     fattura_passiva.ESERCIZIO_COMPENSO = compenso.esercizio and
   	     fattura_passiva.CDS_COMPENSO       = compenso.cd_cds and
   	     fattura_passiva.UO_COMPENSO				= compenso.cd_unita_organizzativa and
   	     fattura_passiva.PG_COMPENSO			  = compenso.pg_compenso)))
   and exists
   (select 1 from modello2_pcc
   where
   nvl(modello2_pcc.codice_segnalazione,'OK')='OK' and


   modello2_pcc.NUMERO_FATTURA = fattura_passiva.NR_FATTURA_FORNITORE AND
   modello2_pcc.data_emissione = fattura_passiva.DT_FATTURA_FORNITORE AND
   modello2_pcc.id_fiscale_IVA =substr(decode(nazione.TI_NAZIONE,'E',nvl(anagrafico.partita_iva,anagrafico.codice_fiscale),decode(nazione.cd_iso||anagrafico.partita_iva,nazione.cd_iso,anagrafico.codice_fiscale,nazione.cd_iso||anagrafico.partita_iva)),0,16))
   and not exists
   (select 1 from modello3_pcc
   where
   nvl(modello3_pcc.codice_segnalazione,'OK')='OK' and
   modello3_pcc.azione='CP' and
   modello3_pcc.importo_pagato=fattura_passiva.im_totale_fattura  and
   modello3_pcc.NUMERO_FATTURA = fattura_passiva.NR_FATTURA_FORNITORE AND
   modello3_pcc.data_emissione = fattura_passiva.DT_FATTURA_FORNITORE AND
   modello3_pcc.id_fiscale_IVA =substr(decode(nazione.TI_NAZIONE,'E',nvl(anagrafico.partita_iva,anagrafico.codice_fiscale),decode(nazione.cd_iso||anagrafico.partita_iva,nazione.cd_iso,anagrafico.codice_fiscale,nazione.cd_iso||anagrafico.partita_iva)),0,16))
   group by fattura_passiva.esercizio,fattura_passiva.cd_unita_organizzativa,fattura_passiva.pg_fattura_passiva,ti_fattura,fattura_passiva.cd_terzo,
   fattura_passiva.cd_cds,fattura_passiva.im_totale_fattura,fattura_passiva.STATO_PAGAMENTO_FONDO_ECO, fattura_passiva.PG_COMPENSO,FL_FATTURA_COMPENSO ,
   DT_FATTURA_FORNITORE,NR_FATTURA_FORNITORE,fattura_passiva.identificativo_sdi,
   cd_iso,ti_nazione,terzo_uo.codice_univoco_ufficio_ipa,
	anagrafico.partita_iva ,anagrafico.codice_fiscale  ,fattura_passiva.fl_split_payment,fattura_passiva.TI_ISTITUZ_COMMERC
   order by fattura_passiva.esercizio,fattura_passiva.cd_unita_organizzativa,fattura_passiva.pg_fattura_passiva;


  cursor testata_cont is

select fattura_passiva.*,cd_iso,ti_nazione,terzo_uo.codice_univoco_ufficio_ipa,
anagrafico.partita_iva ana_partita_iva,anagrafico.codice_fiscale ana_codice_fiscale
from fattura_passiva,nazione,anagrafico,terzo,terzo terzo_uo
where
	 terzo_uo.cd_unita_organizzativa = fattura_passiva.cd_unita_organizzativa and
   anagrafico.cd_anag = terzo.cd_anag and
   terzo.cd_terzo  = fattura_passiva.cd_terzo and
   (terzo_uo.cd_terzo = (select min(cd_terzo) from terzo uo where
   uo.cd_unita_organizzativa =   fattura_passiva.cd_unita_organizzativa and
   fattura_passiva.identificativo_sdi is null) or
   (fattura_passiva.identificativo_sdi is not null and
   terzo_uo.cd_unita_organizzativa =   fattura_passiva.cd_unita_organizzativa
   and exists(select 1 from documento_ele_trasmissione where
   documento_ele_trasmissione.identificativo_sdi = fattura_passiva.identificativo_sdi and
   documento_ele_trasmissione.codice_destinatario = terzo_uo.CODICE_UNIVOCO_UFFICIO_IPA))) and
   nazione.pg_nazione =  anagrafico.pg_nazione_fiscale and
   DT_FATTURA_FORNITORE    > (select dt01 from configurazione_cnr where
   cd_chiave_primaria   = 'REGISTRO_UNICO_FATPAS' and
   cd_chiave_secondaria = 'DATA_INIZIO')


   and exists
   (select 1 from modello2_pcc
   where
   modello2_pcc.NUMERO_FATTURA = fattura_passiva.NR_FATTURA_FORNITORE AND
   modello2_pcc.data_emissione = fattura_passiva.DT_FATTURA_FORNITORE AND
   nvl(modello2_pcc.codice_segnalazione,'OK')='OK' and


   modello2_pcc.id_fiscale_IVA =substr(decode(nazione.TI_NAZIONE,'E',nvl(anagrafico.partita_iva,anagrafico.codice_fiscale),decode(nazione.cd_iso||anagrafico.partita_iva,nazione.cd_iso,anagrafico.codice_fiscale,nazione.cd_iso||anagrafico.partita_iva)),0,16))
   and not exists
   		 (select 1 from modello3_pcc
   				where
   				 (modello3_pcc.CODICE_UFFICIO =terzo_uo.codice_univoco_pcc or
   				 modello3_pcc.CODICE_UFFICIO =terzo_uo.codice_univoco_ufficio_ipa) and
				   modello3_pcc.azione  ='CP' and
				   nvl(modello3_pcc.codice_segnalazione,'OK')='OK' and
				   modello3_pcc.data_emissione = fattura_passiva.dt_fattura_fornitore and
				   modello3_pcc.id_fiscale_IVA =substr(decode(nazione.TI_NAZIONE,'E',nvl(anagrafico.partita_iva,anagrafico.codice_fiscale),decode(nazione.cd_iso||anagrafico.partita_iva,nazione.cd_iso,anagrafico.codice_fiscale,nazione.cd_iso||anagrafico.partita_iva)),0,16))
   order by esercizio,fattura_passiva.cd_unita_organizzativa,pg_fattura_passiva;

	cursor cig_cupImpegno(es number,cds varchar2,uo varchar2,pg number,cd_terzo_in number) is
	select
	ds_obbligazione,ds_elemento_voce,sum(v_doc_passivo_obbligazione.im_scadenza)imp,cd_cig,cd_cup, decode(FL_INV_BENI_COMP,'Y','CA',decode(FL_INV_BENI_PATR,'Y','CA','CO')) natura,
	v_doc_passivo_obbligazione.esercizio_obbligazione,v_doc_passivo_obbligazione.cd_cds_obbligazione,v_doc_passivo_obbligazione.pg_obbligazione,v_doc_passivo_obbligazione.esercizio_ori_obbligazione
  from v_doc_passivo_obbligazione,obbligazione,obbligazione_scadenzario,contratto,elemento_voce
        where
        v_doc_passivo_obbligazione.cd_cds  = cds  and
      	v_doc_passivo_obbligazione.cd_unita_organizzativa  = uo and
        v_doc_passivo_obbligazione.esercizio               = es  and
        v_doc_passivo_obbligazione.PG_DOCUMENTO_AMM      = pg  and
        v_doc_passivo_obbligazione.CD_TERZO              =cd_terzo_in  AND
        v_doc_passivo_obbligazione.CD_TIPO_DOCUMENTO_AMM in('FATTURA_P','COMPENSO') and
        v_doc_passivo_obbligazione.stato_cofi not in('A') and
        	  obbligazione.cd_cds                 = obbligazione_scadenzario.cd_cds
        AND obbligazione.esercizio              = obbligazione_scadenzario.esercizio
        AND obbligazione.esercizio_originale    = obbligazione_scadenzario.esercizio_originale
        AND obbligazione.pg_obbligazione        = obbligazione_scadenzario.pg_obbligazione
        AND obbligazione_scadenzario.cd_cds                      = v_doc_passivo_obbligazione.cd_cds_obbligazione
        AND obbligazione_scadenzario.esercizio                   = v_doc_passivo_obbligazione.esercizio_obbligazione
        AND obbligazione_scadenzario.esercizio_originale         = v_doc_passivo_obbligazione.esercizio_ori_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione             = v_doc_passivo_obbligazione.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario = v_doc_passivo_obbligazione.pg_obbligazione_scadenzario
        and elemento_voce.esercizio 			= obbligazione.esercizio
        AND elemento_voce.ti_appartenenza = obbligazione.ti_appartenenza
        AND elemento_voce.ti_gestione 		= obbligazione.ti_gestione
        AND elemento_voce.cd_elemento_voce = obbligazione.cd_elemento_voce
        AND contratto.esercizio         (+)= obbligazione.esercizio_contratto
        AND contratto.stato             (+)= obbligazione.stato_contratto
        AND contratto.pg_contratto      (+)= obbligazione.pg_contratto
  group by ds_obbligazione,ds_elemento_voce,cd_cig,cd_cup, decode(FL_INV_BENI_COMP,'Y','CA',decode(FL_INV_BENI_PATR,'Y','CA','CO')),
	v_doc_passivo_obbligazione.esercizio_obbligazione,v_doc_passivo_obbligazione.cd_cds_obbligazione,v_doc_passivo_obbligazione.pg_obbligazione,v_doc_passivo_obbligazione.esercizio_ori_obbligazione;

  cursor cig_cupMandato(es number,cds varchar2,uo varchar2,pg number,cd_terzo_in number,dt_fat date,nr_fat varchar2) is
	select dt_emissione,
	ds_mandato,ds_elemento_voce,sum(nvl(mandato_siope.importo,mandato_riga.im_mandato_riga)) imp,cd_cig,mandato_siope_cup.cd_cup, sum(nvl(mandato_siope_cup.importo,0)) imp_cup,decode(FL_INV_BENI_COMP,'Y','CA',decode(FL_INV_BENI_PATR,'Y','CA','CO')) natura,mandato_riga.esercizio,mandato_riga.cd_cds,mandato_riga.pg_mandato,
	v_doc_passivo_obbligazione.esercizio_obbligazione,v_doc_passivo_obbligazione.cd_cds_obbligazione,v_doc_passivo_obbligazione.pg_obbligazione,v_doc_passivo_obbligazione.esercizio_ori_obbligazione,v_doc_passivo_obbligazione.pg_obbligazione_scadenzario,
  ANAGRAFICO.PARTITA_IVA,ANAGRAFICO.CODICE_FISCALE,cd_iso,ti_nazione,decode(v_doc_passivo_obbligazione.CD_TIPO_DOCUMENTO_AMM,'FATTURA_P', v_doc_passivo_obbligazione.IM_IMPONIBILE_DOC_AMM,fattura_passiva.IM_TOTALE_IMPONIBILE) tot_imp,v_doc_passivo_obbligazione.IM_TOTALE_DOC_AMM tot_doc
	from v_doc_passivo_obbligazione,obbligazione,obbligazione_scadenzario,contratto,
				mandato_riga,mandato_siope_cup,elemento_voce,nazione,anagrafico,terzo,MANDATO,mandato_siope,fattura_passiva
          where
            anagrafico.cd_anag = terzo.cd_anag and
   				  terzo.cd_terzo  = nvl(mandato_riga.cd_terzo_cedente,mandato_riga.cd_terzo) and
   					nazione.pg_nazione =  anagrafico.pg_nazione_fiscale and
          	v_doc_passivo_obbligazione.cd_cds  = cds  and
          	v_doc_passivo_obbligazione.cd_unita_organizzativa  = uo and
        		v_doc_passivo_obbligazione.esercizio               = es  and
        		v_doc_passivo_obbligazione.PG_DOCUMENTO_AMM      = pg  and
        		v_doc_passivo_obbligazione.CD_TERZO              =cd_terzo_in  AND
        		((v_doc_passivo_obbligazione.DT_FATTURA_FORNITORE = dt_fat and
        		v_doc_passivo_obbligazione.NR_FATTURA_FORNITORE = nr_fat and
        		v_doc_passivo_obbligazione.CD_TIPO_DOCUMENTO_AMM ='FATTURA_P' AND
        		fattura_passiva.esercizio = v_doc_passivo_obbligazione.esercizio and
   	      	fattura_passiva.cd_cds     = v_doc_passivo_obbligazione.cd_cds and
   	      	fattura_passiva.cd_unita_organizzativa			= v_doc_passivo_obbligazione.cd_unita_organizzativa and
   	      	fattura_passiva.pg_fattura_passiva		  = v_doc_passivo_obbligazione.PG_DOCUMENTO_AMM ) or
            (v_doc_passivo_obbligazione.CD_TIPO_DOCUMENTO_AMM ='COMPENSO'  AND
            fattura_passiva.DT_FATTURA_FORNITORE = dt_fat and
        		fattura_passiva.NR_FATTURA_FORNITORE = nr_fat and
            fattura_passiva.ESERCIZIO_COMPENSO = v_doc_passivo_obbligazione.esercizio and
            fattura_passiva.CDS_COMPENSO       = v_doc_passivo_obbligazione.cd_cds and
            fattura_passiva.UO_COMPENSO			 	 = v_doc_passivo_obbligazione.cd_unita_organizzativa and
            fattura_passiva.PG_COMPENSO			   = v_doc_passivo_obbligazione.PG_DOCUMENTO_AMM )) and
        	  obbligazione.cd_cds                 = obbligazione_scadenzario.cd_cds  and
        	  obbligazione.esercizio              = obbligazione_scadenzario.esercizio
        AND obbligazione.esercizio_originale    = obbligazione_scadenzario.esercizio_originale
        AND obbligazione.pg_obbligazione        = obbligazione_scadenzario.pg_obbligazione
        AND obbligazione_scadenzario.cd_cds                      = v_doc_passivo_obbligazione.cd_cds_obbligazione
        AND obbligazione_scadenzario.esercizio                   = v_doc_passivo_obbligazione.esercizio_obbligazione
        AND obbligazione_scadenzario.esercizio_originale         = v_doc_passivo_obbligazione.esercizio_ori_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione             = v_doc_passivo_obbligazione.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario = v_doc_passivo_obbligazione.pg_obbligazione_scadenzario
        and elemento_voce.esercizio 			= obbligazione.esercizio
        AND elemento_voce.ti_appartenenza = obbligazione.ti_appartenenza
        AND elemento_voce.ti_gestione 		= obbligazione.ti_gestione
        AND elemento_voce.cd_elemento_voce = obbligazione.cd_elemento_voce
        AND contratto.esercizio         (+)= obbligazione.esercizio_contratto
        AND contratto.stato             (+)= obbligazione.stato_contratto
        AND contratto.pg_contratto      (+)= obbligazione.pg_contratto
        and v_doc_passivo_obbligazione.CD_TIPO_DOCUMENTO_AMM =mandato_riga.CD_TIPO_DOCUMENTO_AMM
        and v_doc_passivo_obbligazione.cd_cds 						= mandato_riga.cd_cds_doc_amm
        AND v_doc_passivo_obbligazione.cd_unita_organizzativa = mandato_riga.cd_uo_doc_amm
        AND v_doc_passivo_obbligazione.esercizio 							=  mandato_riga.esercizio_doc_amm
        AND v_doc_passivo_obbligazione.PG_DOCUMENTO_AMM 		= mandato_riga.pg_doc_amm
        AND v_doc_passivo_obbligazione.cd_cds_obbligazione 		= mandato_riga.cd_cds
        AND v_doc_passivo_obbligazione.esercizio_obbligazione = mandato_riga.esercizio_obbligazione
        AND v_doc_passivo_obbligazione.pg_obbligazione 				=  mandato_riga.pg_obbligazione
        AND v_doc_passivo_obbligazione.pg_obbligazione_scadenzario =  mandato_riga.pg_obbligazione_scadenzario
        AND v_doc_passivo_obbligazione.esercizio_ori_obbligazione  =   mandato_riga.esercizio_ori_obbligazione

        and mandato_riga.cd_cds = mandato_siope.cd_cds (+)
        AND mandato_riga.esercizio = mandato_siope.esercizio(+)
        AND mandato_riga.pg_mandato = mandato_siope.pg_mandato(+)
        AND mandato_riga.esercizio_obbligazione = mandato_siope.esercizio_obbligazione(+)
        AND mandato_riga.esercizio_ori_obbligazione = mandato_siope.esercizio_ori_obbligazione(+)
        AND mandato_riga.pg_obbligazione = mandato_siope.pg_obbligazione(+)
        AND mandato_riga.pg_obbligazione_scadenzario =mandato_siope.pg_obbligazione_scadenzario(+)
        AND mandato_riga.cd_cds_doc_amm = mandato_siope.cd_cds_doc_amm(+)
        AND mandato_riga.cd_uo_doc_amm = mandato_siope.cd_uo_doc_amm(+)
        AND mandato_riga.esercizio_doc_amm =mandato_siope.esercizio_doc_amm(+)
        AND mandato_riga.cd_tipo_documento_amm =mandato_siope.cd_tipo_documento_amm(+)
        AND mandato_riga.pg_doc_amm = mandato_siope.pg_doc_amm (+)
        and mandato_siope.cd_cds = mandato_siope_cup.cd_cds (+)
        AND mandato_siope.esercizio = mandato_siope_cup.esercizio (+)
        AND mandato_siope.pg_mandato = mandato_siope_cup.pg_mandato(+)
        AND mandato_siope.esercizio_obbligazione = mandato_siope_cup.esercizio_obbligazione(+)
        AND mandato_siope.esercizio_ori_obbligazione = mandato_siope_cup.esercizio_ori_obbligazione(+)
        AND mandato_siope.pg_obbligazione = mandato_siope_cup.pg_obbligazione(+)
        AND mandato_siope.pg_obbligazione_scadenzario =mandato_siope_cup.pg_obbligazione_scadenzario(+)
        AND mandato_siope.cd_cds_doc_amm = mandato_siope_cup.cd_cds_doc_amm(+)
        AND mandato_siope.cd_uo_doc_amm = mandato_siope_cup.cd_uo_doc_amm(+)
        AND mandato_siope.esercizio_doc_amm =mandato_siope_cup.esercizio_doc_amm(+)
        AND mandato_siope.cd_tipo_documento_amm =mandato_siope_cup.cd_tipo_documento_amm(+)
        AND mandato_siope.pg_doc_amm = mandato_siope_cup.pg_doc_amm(+)
        AND mandato_siope.esercizio_siope =mandato_siope_cup.esercizio_siope(+)
        AND mandato_siope.ti_gestione =mandato_siope_cup.ti_gestione(+)
        AND mandato_siope.cd_siope = mandato_siope_cup.cd_siope(+)
        AND mandato_siope_cup.importo(+) !=0
        AND mandato.cd_cds = mandato_riga.cd_cds
        AND mandato.esercizio = mandato_riga.esercizio
        AND mandato.pg_mandato = mandato_riga.pg_mandato
        and mandato.stato='P'
        and mandato_riga.stato!='A'
        and not exists
   		 (select 1 from modello3_pcc,nazione n2,anagrafico a2,terzo t2
   				where
   				 a2.cd_anag = t2.cd_anag and
   				 t2.cd_terzo  = v_doc_passivo_obbligazione.cd_terzo and
   				 n2.pg_nazione =  a2.pg_nazione_fiscale and
				   modello3_pcc.azione  ='CP' and
				   modello3_pcc.ESTREMI_IMPEGNO_CP = v_doc_passivo_obbligazione.esercizio_ori_obbligazione||'/'||v_doc_passivo_obbligazione.esercizio_obbligazione||'/'||v_doc_passivo_obbligazione.cd_cds_obbligazione||'/'||v_doc_passivo_obbligazione.pg_obbligazione and
				   modello3_pcc.numero_mandato=mandato.esercizio||'/'||mandato.cd_cds||'/'||mandato.pg_mandato and
				   modello3_pcc.NUMERO_FATTURA = nr_fat AND
				   modello3_pcc.data_emissione = dt_fat AND
				   modello3_pcc.id_fiscale_IVA =substr(decode(n2.TI_NAZIONE,'E',nvl(a2.partita_iva,a2.codice_fiscale),decode(n2.cd_iso||a2.partita_iva,n2.cd_iso,a2.codice_fiscale,n2.cd_iso||a2.partita_iva)),0,16))
		group by ds_mandato,ds_elemento_voce,dt_emissione,cd_cig,mandato_siope_cup.cd_cup, decode(FL_INV_BENI_COMP,'Y','CA',decode(FL_INV_BENI_PATR,'Y','CA','CO')),mandato_riga.esercizio,mandato_riga.cd_cds,mandato_riga.pg_mandato,
	 v_doc_passivo_obbligazione.esercizio_obbligazione,v_doc_passivo_obbligazione.cd_cds_obbligazione,v_doc_passivo_obbligazione.pg_obbligazione,v_doc_passivo_obbligazione.esercizio_ori_obbligazione,v_doc_passivo_obbligazione.pg_obbligazione_scadenzario,
	 ANAGRAFICO.PARTITA_IVA,ANAGRAFICO.CODICE_FISCALE,cd_iso,ti_nazione,decode(v_doc_passivo_obbligazione.CD_TIPO_DOCUMENTO_AMM,'FATTURA_P', v_doc_passivo_obbligazione.IM_IMPONIBILE_DOC_AMM,fattura_passiva.IM_TOTALE_IMPONIBILE),v_doc_passivo_obbligazione.IM_TOTALE_DOC_AMM
		order by
	 mandato_riga.pg_mandato,v_doc_passivo_obbligazione.esercizio_obbligazione,v_doc_passivo_obbligazione.cd_cds_obbligazione,v_doc_passivo_obbligazione.pg_obbligazione,v_doc_passivo_obbligazione.esercizio_ori_obbligazione,v_doc_passivo_obbligazione.pg_obbligazione_scadenzario,
	 ANAGRAFICO.PARTITA_IVA,ANAGRAFICO.CODICE_FISCALE,cd_iso,ti_nazione,mandato_siope_cup.cd_cup;

	   cursor testata_scad is

select fattura_passiva.*,cd_iso,ti_nazione,terzo_uo.codice_univoco_ufficio_ipa
from fattura_passiva,nazione,anagrafico,terzo,terzo terzo_uo
where
	 terzo_uo.cd_unita_organizzativa = fattura_passiva.cd_unita_organizzativa and
   anagrafico.cd_anag = terzo.cd_anag and
   terzo.cd_terzo  = fattura_passiva.cd_terzo and
  (terzo_uo.cd_terzo = (select min(cd_terzo) from terzo uo where
   uo.cd_unita_organizzativa =   fattura_passiva.cd_unita_organizzativa and
   fattura_passiva.identificativo_sdi is null) or
   (fattura_passiva.identificativo_sdi is not null and
   terzo_uo.cd_unita_organizzativa =   fattura_passiva.cd_unita_organizzativa
   and exists(select 1 from documento_ele_trasmissione where
   documento_ele_trasmissione.identificativo_sdi = fattura_passiva.identificativo_sdi and
   documento_ele_trasmissione.codice_destinatario = terzo_uo.CODICE_UNIVOCO_UFFICIO_IPA))) and
   nazione.pg_nazione =  anagrafico.pg_nazione_fiscale and
   DT_FATTURA_FORNITORE    > (select dt01 from configurazione_cnr where
   cd_chiave_primaria   = 'REGISTRO_UNICO_FATPAS' and
   cd_chiave_secondaria = 'DATA_INIZIO')
   and stato_liquidazione ='LIQ'
   and dt_scadenza <TO_DATE('01'||TO_CHAR(SYSDATE,'MMYYYY'),'DDMMYYYY')
   and (fattura_passiva.stato_cofi  not in('P','A') and
   	    STATO_PAGAMENTO_FONDO_ECO = 'N' and
   	    ((FL_FATTURA_COMPENSO = 'Y' and
   	     exists(select 1 from compenso
   	     where
   	     compenso.stato_cofi  not in('P','A') and
   	     fattura_passiva.ESERCIZIO_COMPENSO = compenso.esercizio and
   	     fattura_passiva.CDS_COMPENSO       = compenso.cd_cds and
   	     fattura_passiva.UO_COMPENSO				= compenso.cd_unita_organizzativa and
   	     fattura_passiva.PG_COMPENSO			  = compenso.pg_compenso))
        or FL_FATTURA_COMPENSO ='N'))
   and exists(Select 1 from fattura_passiva_riga
		where
     nvl(im_diponibile_nc,0)!=0 and
     				fattura_passiva_riga.stato_cofi  not in('P','A') and
						fattura_passiva_riga.cd_cds  = fattura_passiva.cd_cds  and
          	fattura_passiva_riga.cd_unita_organizzativa  = fattura_passiva.cd_unita_organizzativa and
        		fattura_passiva_riga.esercizio               = fattura_passiva.esercizio     and
        		fattura_passiva_riga.pg_fattura_passiva      = fattura_passiva.pg_fattura_passiva )
   and exists
   (select 1 from modello2_pcc
   where
   modello2_pcc.NUMERO_FATTURA = fattura_passiva.nr_fattura_fornitore AND
   modello2_pcc.data_emissione = fattura_passiva.dt_fattura_fornitore AND
   modello2_pcc.id_fiscale_IVA =substr(decode(nazione.TI_NAZIONE,'E',nvl(anagrafico.partita_iva,anagrafico.codice_fiscale),decode(nazione.cd_iso||anagrafico.partita_iva,nazione.cd_iso,anagrafico.codice_fiscale,nazione.cd_iso||anagrafico.partita_iva)),0,16))
   and not exists
   		 (select 1 from modello3_pcc,nazione n2,anagrafico a2,terzo t2
   				where
   				 a2.cd_anag = t2.cd_anag and
   				 t2.cd_terzo  = fattura_passiva.cd_terzo and
   				 n2.pg_nazione =  a2.pg_nazione_fiscale and
				   modello3_pcc.azione  ='CS' and
				   modello3_pcc.dt_scadenza = fattura_passiva.dt_scadenza and
				   modello3_pcc.numero_fattura = fattura_passiva.nr_fattura_fornitore and
				   modello3_pcc.data_emissione = fattura_passiva.dt_fattura_fornitore and
				   modello3_pcc.id_fiscale_IVA =substr(decode(n2.TI_NAZIONE,'E',nvl(a2.partita_iva,a2.codice_fiscale),decode(n2.cd_iso||a2.partita_iva,n2.cd_iso,a2.codice_fiscale,n2.cd_iso||a2.partita_iva)),0,16))
		order by esercizio,fattura_passiva.cd_unita_organizzativa,pg_fattura_passiva;

   pag testata_pag%rowtype;
   cont testata_cont%rowtype;
   scad testata_scad%rowtype;
   oldStato varchar2(20):=null;
   newStato varchar2(20):=null;
   stato varchar2(20):=null;
   causale varchar2(20):=null;
   c cig_cupMandato%rowtype;
   cOld cig_cupMandato%rowtype:=null;
   i cig_cupImpegno%rowtype;
   Tot_associato_cup number:=0;
   tot_da_pagare number(15,2):=0;
begin

open testata_pag;
loop
fetch testata_pag  into pag;
exit when testata_pag%notfound;

  Tot_associato_cup:=0;
  cOld:=null;
  open cig_cupMandato(pag.esercizio,pag.cd_cds,pag.cd_unita_organizzativa,nvl(pag.pg_compenso,pag.pg_fattura_passiva),pag.cd_terzo,pag.dt_FATTURA_FORNITORE,pag.NR_FATTURA_FORNITORE);
  loop
    fetch cig_cupMandato into c;
        exit when cig_cupMandato%notfound;

             	if(cOld.esercizio_ori_obbligazione is not null and
             	(	cOld.esercizio_ori_obbligazione!=c.esercizio_ori_obbligazione or
						  	cOld.esercizio_obbligazione			!= c.esercizio_obbligazione or
						  	cOld.cd_cds_obbligazione				!= c.cd_cds_obbligazione or
								cOld.pg_obbligazione						!= c.pg_obbligazione or
								cOld.PG_OBBLIGAZIONE_SCADENZARIO!= c.PG_OBBLIGAZIONE_SCADENZARIO or
								cOld.pg_mandato									!= c.pg_mandato)) then
							if (cOld.imp>Tot_associato_cup and Tot_associato_cup!=0 ) then
							dbms_output.put_line('insert 1');
								insert into modello3_pcc(lotto,
					   													CODICE_FISCALE_AMM ,
																			CODICE_UFFICIO     ,
																			CODICE_FISCALE     ,
																			ID_FISCALE_IVA     ,
																			AZIONE             ,
																			PROGR_REGISTRAZIONE,
																			NUMERO_FATTURA     ,
																			DATA_EMISSIONE     ,
																			IMPORTO_TOTALE     ,
																			numero_protocollo,
																			data_protocollo,
																			note_rc,
																			data_rifiuto,
																			descrizione_rf,
																			IMPORTO_MOVIMENTO  ,
																			NATURA_SPESA_CO    ,
																			CAPITOLI_SPESA_CO  ,
																			STATO_DEBITO       ,
																			CAUSALE            ,
																			DESCRIZIONE_CO     ,
																			ESTREMI_IMPEGNO_CO ,
																			CODICE_CIG_CO      ,
																			CODICE_CUP_CO      ,
																			comunica_scadenza  ,
 																			importo_scadenza   ,
 																		  dt_scadenza  			 ,
																			IMPORTO_PAGATO     ,
																			NATURA_SPESA_CP    ,
																			CAPITOLI_SPESA_CP  ,
																			ESTREMI_IMPEGNO_CP ,
																			NUMERO_MANDATO     ,
																			DATA_MANDATO       ,
																			ID_FISCALE_IVA_CP  ,
																			CODICE_CIG_CP      ,
																			CODICE_CUP_CP      ,
																			DESCRIZIONE_CP     ,
																			CODICE_SEGNALAZIONE,
																			DESCRIZIONE_SEGNALAZIONE) values
					 (to_date(to_char(sysdate,'dd/mm/yyyy hh24:mi'),'dd/mm/yyyy hh24:mi'),'80054330586',pag.codice_univoco_ufficio_ipa,
						substr(nvl(pag.ana_codice_fiscale,pag.ana_partita_iva),0,16),
						substr(decode(pag.TI_NAZIONE,'E',nvl(pag.ana_partita_iva,pag.ana_codice_fiscale),decode(pag.cd_iso||pag.ana_partita_iva,pag.cd_iso,pag.ana_codice_fiscale,pag.cd_iso||pag.ana_partita_iva)),0,16),
						'CP',
						nvl(to_char(pag.identificativo_sdi),'NA'),
						pag.NR_FATTURA_FORNITORE,pag.dt_FATTURA_FORNITORE,
						decode(pag.FL_FATTURA_COMPENSO,'Y',cOld.imp,pag.im_totale_fattura),

						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null, null,null,

						decode (pag.split,'N',(cOld.imp-Tot_associato_cup),decode(pag.TI_ISTITUZ_COMMERC,'C',(cOld.imp-Tot_associato_cup) ,round(((cOld.imp-Tot_associato_cup)*cOld.tot_imp)/(cOld.tot_doc),2))),
						cOld.natura,
						substr(cOld.ds_elemento_voce,1,100),
						cOld.esercizio_ori_obbligazione||'/'||cOld.esercizio_obbligazione||'/'||cOld.cd_cds_obbligazione||'/'||cOld.pg_obbligazione,
						cOld.esercizio||'/'||cOld.cd_cds||'/'||cOld.pg_mandato,
						cOld.dt_emissione,
						decode(pag.STATO_PAGAMENTO_FONDO_ECO,'N',substr(decode(cOld.TI_NAZIONE,'E',nvl(cOld.partita_iva,cOld.codice_fiscale),decode(cOld.cd_iso||cOld.partita_iva,cOld.cd_iso,cOld.codice_fiscale,cOld.cd_iso||cOld.partita_iva)),0,16),substr(decode(pag.TI_NAZIONE,'E',nvl(pag.ana_partita_iva,pag.ana_codice_fiscale),decode(pag.cd_iso||pag.ana_partita_iva,pag.cd_iso,pag.ana_codice_fiscale,pag.cd_iso||pag.ana_partita_iva)),0,16)),
						nvl(cOld.cd_cig,'NA'),
						'NA',
						substr(cOld.ds_mandato,1,95),
						null,null);
				end if;
				Tot_associato_cup:=0;
			end if;

					  if ( nvl(c.imp_cup ,0)!= 0 or nvl(c.imp ,0)!= 0) then
					   insert into modello3_pcc(lotto,
					   													CODICE_FISCALE_AMM ,
																			CODICE_UFFICIO     ,
																			CODICE_FISCALE     ,
																			ID_FISCALE_IVA     ,
																			AZIONE             ,
																			PROGR_REGISTRAZIONE,
																			NUMERO_FATTURA     ,
																			DATA_EMISSIONE     ,
																			IMPORTO_TOTALE     ,
																			numero_protocollo,
																			data_protocollo,
																			note_rc,
																			data_rifiuto,
																			descrizione_rf,
																			IMPORTO_MOVIMENTO  ,
																			NATURA_SPESA_CO    ,
																			CAPITOLI_SPESA_CO  ,
																			STATO_DEBITO       ,
																			CAUSALE            ,
																			DESCRIZIONE_CO     ,
																			ESTREMI_IMPEGNO_CO ,
																			CODICE_CIG_CO      ,
																			CODICE_CUP_CO      ,
																			comunica_scadenza  ,
 																			importo_scadenza   ,
 																		  dt_scadenza  			 ,
																			IMPORTO_PAGATO     ,
																			NATURA_SPESA_CP    ,
																			CAPITOLI_SPESA_CP  ,
																			ESTREMI_IMPEGNO_CP ,
																			NUMERO_MANDATO     ,
																			DATA_MANDATO       ,
																			ID_FISCALE_IVA_CP  ,
																			CODICE_CIG_CP      ,
																			CODICE_CUP_CP      ,
																			DESCRIZIONE_CP     ,
																			CODICE_SEGNALAZIONE,
																			DESCRIZIONE_SEGNALAZIONE) values
					 (to_date(to_char(sysdate,'dd/mm/yyyy hh24:mi'),'dd/mm/yyyy hh24:mi'),'80054330586',pag.codice_univoco_ufficio_ipa,
						substr(nvl(pag.ana_codice_fiscale,pag.ana_partita_iva),0,16),
						substr(decode(pag.TI_NAZIONE,'E',nvl(pag.ana_partita_iva,pag.ana_codice_fiscale),decode(pag.cd_iso||pag.ana_partita_iva,pag.cd_iso,pag.ana_codice_fiscale,pag.cd_iso||pag.ana_partita_iva)),0,16),
						'CP',
						nvl(to_char(pag.identificativo_sdi),'NA'),
						pag.NR_FATTURA_FORNITORE,pag.dt_FATTURA_FORNITORE,
						decode(pag.FL_FATTURA_COMPENSO,'Y',c.imp,pag.im_totale_fattura),

						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null, null,null,
						round(decode(pag.split,'N',decode(c.imp_cup,0,c.imp,c.imp_cup),decode(pag.TI_ISTITUZ_COMMERC,'C', decode(c.imp_cup,0,c.imp,c.imp_cup),decode(c.imp_cup,0,c.imp,c.imp_cup)*c.tot_imp/c.tot_doc)),2),



						c.natura,
						substr(c.ds_elemento_voce,1,100),
						c.esercizio_ori_obbligazione||'/'||c.esercizio_obbligazione||'/'||c.cd_cds_obbligazione||'/'||c.pg_obbligazione,
						c.esercizio||'/'||c.cd_cds||'/'||c.pg_mandato,
						c.dt_emissione,
						decode(pag.STATO_PAGAMENTO_FONDO_ECO,'N',substr(decode(c.TI_NAZIONE,'E',nvl(c.partita_iva,c.codice_fiscale),decode(c.cd_iso||c.partita_iva,c.cd_iso,c.codice_fiscale,c.cd_iso||c.partita_iva)),0,16),substr(decode(pag.TI_NAZIONE,'E',nvl(pag.ana_partita_iva,pag.ana_codice_fiscale),decode(pag.cd_iso||pag.ana_partita_iva,pag.cd_iso,pag.ana_codice_fiscale,pag.cd_iso||pag.ana_partita_iva)),0,16)),
						nvl(c.cd_cig,'NA'),
						nvl(c.cd_cup,'NA'),
						substr(c.ds_mandato,1,95),
						null,null);
						cOld:=c;
						Tot_associato_cup:=Tot_associato_cup+c.imp_cup;
					end if;
end loop;
	   					if (c.imp>Tot_associato_cup and Tot_associato_cup!=0 ) then
	   					dbms_output.put_line('insert 3');
			 					insert into modello3_pcc(lotto,
					   													CODICE_FISCALE_AMM ,
																			CODICE_UFFICIO     ,
																			CODICE_FISCALE     ,
																			ID_FISCALE_IVA     ,
																			AZIONE             ,
																			PROGR_REGISTRAZIONE,
																			NUMERO_FATTURA     ,
																			DATA_EMISSIONE     ,
																			IMPORTO_TOTALE     ,
																			numero_protocollo,
																			data_protocollo,
																			note_rc,
																			data_rifiuto,
																			descrizione_rf,
																			IMPORTO_MOVIMENTO  ,
																			NATURA_SPESA_CO    ,
																			CAPITOLI_SPESA_CO  ,
																			STATO_DEBITO       ,
																			CAUSALE            ,
																			DESCRIZIONE_CO     ,
																			ESTREMI_IMPEGNO_CO ,
																			CODICE_CIG_CO      ,
																			CODICE_CUP_CO      ,
																			comunica_scadenza  ,
 																			importo_scadenza   ,
 																		  dt_scadenza  			 ,
																			IMPORTO_PAGATO     ,
																			NATURA_SPESA_CP    ,
																			CAPITOLI_SPESA_CP  ,
																			ESTREMI_IMPEGNO_CP ,
																			NUMERO_MANDATO     ,
																			DATA_MANDATO       ,
																			ID_FISCALE_IVA_CP  ,
																			CODICE_CIG_CP      ,
																			CODICE_CUP_CP      ,
																			DESCRIZIONE_CP     ,
																			CODICE_SEGNALAZIONE,
																			DESCRIZIONE_SEGNALAZIONE) values
					 (to_date(to_char(sysdate,'dd/mm/yyyy hh24:mi'),'dd/mm/yyyy hh24:mi'),'80054330586',pag.codice_univoco_ufficio_ipa,
						substr(nvl(pag.ana_codice_fiscale,pag.ana_partita_iva),0,16),
						substr(decode(pag.TI_NAZIONE,'E',nvl(pag.ana_partita_iva,pag.ana_codice_fiscale),decode(pag.cd_iso||pag.ana_partita_iva,pag.cd_iso,pag.ana_codice_fiscale,pag.cd_iso||pag.ana_partita_iva)),0,16),
						'CP',
						nvl(to_char(pag.identificativo_sdi),'NA'),
						pag.NR_FATTURA_FORNITORE,pag.dt_FATTURA_FORNITORE,
						decode(pag.FL_FATTURA_COMPENSO,'Y',c.imp,pag.im_totale_fattura),

						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null, null,null,

						decode (pag.split,'N',(c.imp-Tot_associato_cup),decode(pag.TI_ISTITUZ_COMMERC,'C',(c.imp-Tot_associato_cup) ,round(((c.imp-Tot_associato_cup)*c.tot_imp)/(c.tot_doc),2))),
						c.natura,
						substr(c.ds_elemento_voce,1,100),
						c.esercizio_ori_obbligazione||'/'||c.esercizio_obbligazione||'/'||c.cd_cds_obbligazione||'/'||c.pg_obbligazione,
						c.esercizio||'/'||c.cd_cds||'/'||c.pg_mandato,
						c.dt_emissione,
						decode(pag.STATO_PAGAMENTO_FONDO_ECO,'N',substr(decode(c.TI_NAZIONE,'E',nvl(c.partita_iva,c.codice_fiscale),decode(c.cd_iso||c.partita_iva,c.cd_iso,c.codice_fiscale,c.cd_iso||c.partita_iva)),0,16),substr(decode(pag.TI_NAZIONE,'E',nvl(pag.ana_partita_iva,pag.ana_codice_fiscale),decode(pag.cd_iso||pag.ana_partita_iva,pag.cd_iso,pag.ana_codice_fiscale,pag.cd_iso||pag.ana_partita_iva)),0,16)),
						nvl(c.cd_cig,'NA'),
						'NA',
						substr(c.ds_mandato,1,95),
						null,null);
				end if;
close cig_cupMandato;

end loop;
close testata_pag;


open testata_scad;
loop
fetch testata_scad  into scad;
exit when testata_scad%notfound;
	tot_da_pagare:=0;
  select sum(nvl(IM_DIPONIBILE_NC,0)- DECODE(SCAD.FL_SPLIT_PAYMENT,'N',0,IM_IVA)) into tot_da_pagare from fattura_passiva_riga
  where stato_cofi not in('P','A') and
  esercizio = scad.esercizio  and
  cd_cds =scad.cd_cds  and
  cd_unita_organizzativa =scad.cd_unita_organizzativa  and
  pg_fattura_passiva =scad.pg_fattura_passiva;

	if (tot_da_pagare!=0 ) then
					   	insert into modello3_pcc(lotto ,
  				   													CODICE_FISCALE_AMM ,
																			CODICE_UFFICIO     ,
																			CODICE_FISCALE     ,
																			ID_FISCALE_IVA     ,
																			AZIONE             ,
																			PROGR_REGISTRAZIONE,
																			NUMERO_FATTURA     ,
																			DATA_EMISSIONE     ,
																			IMPORTO_TOTALE     ,
																			numero_protocollo,
																			data_protocollo,
																			note_rc,
																			data_rifiuto,
																			descrizione_rf,
																			IMPORTO_MOVIMENTO  ,
																			NATURA_SPESA_CO    ,
																			CAPITOLI_SPESA_CO  ,
																			STATO_DEBITO       ,
																			CAUSALE            ,
																			DESCRIZIONE_CO     ,
																			ESTREMI_IMPEGNO_CO ,
																			CODICE_CIG_CO      ,
																			CODICE_CUP_CO      ,
																			comunica_scadenza  ,
 																			importo_scadenza   ,
 																		  dt_scadenza  			 ,
																			IMPORTO_PAGATO     ,
																			NATURA_SPESA_CP    ,
																			CAPITOLI_SPESA_CP  ,
																			ESTREMI_IMPEGNO_CP ,
																			NUMERO_MANDATO     ,
																			DATA_MANDATO       ,
																			ID_FISCALE_IVA_CP  ,
																			CODICE_CIG_CP      ,
																			CODICE_CUP_CP      ,
																			DESCRIZIONE_CP     ,
																			CODICE_SEGNALAZIONE,
																			DESCRIZIONE_SEGNALAZIONE) values
					 (to_date(to_char(sysdate,'dd/mm/yyyy hh24:mi'),'dd/mm/yyyy hh24:mi'),'80054330586',scad.codice_univoco_ufficio_ipa,
						substr(nvl(scad.codice_fiscale,scad.partita_iva),0,16),
						substr(decode(scad.TI_NAZIONE,'E',nvl(scad.partita_iva,scad.codice_fiscale),decode(scad.cd_iso||scad.partita_iva,scad.cd_iso,scad.codice_fiscale,scad.cd_iso||scad.partita_iva)),0,16),
						'CS',
						nvl(to_char(scad.identificativo_sdi),'NA'),
						scad.NR_FATTURA_FORNITORE,scad.dt_FATTURA_FORNITORE,
					  DECODE(SCAD.FL_SPLIT_PAYMENT,'N',scad.im_totale_fattura,SCAD.IM_TOTALE_IMPONIBILE),

						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						'SI',tot_da_pagare,scad.dt_scadenza,
						null,null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,
						null,null);
					end if;				
end loop;
close testata_scad;   
commit;  
end;
end;
/