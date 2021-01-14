CREATE OR REPLACE procedure PCIR009.popola_pcc_modello2_SEMP is
begin
declare
cursor testata is

select fattura_passiva.*,cd_iso,ti_nazione,terzo_uo.codice_univoco_ufficio_ipa,terzo_uo.denominazione_sede,
anagrafico.cognome ana_cognome,anagrafico.nome ana_nome,
anagrafico.ragione_sociale ana_ragione_sociale,anagrafico.partita_iva ana_partita_iva,anagrafico.codice_fiscale ana_codice_fiscale
from fattura_passiva,nazione,anagrafico,terzo,terzo terzo_uo
where
	 terzo_uo.cd_unita_organizzativa = fattura_passiva.cd_unita_organizzativa and
   anagrafico.cd_anag = terzo.cd_anag and
   terzo.cd_terzo  = fattura_passiva.cd_terzo and
   fattura_passiva.stato_cofi!='A' and
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
   and
   
   
   
   
   
   fl_intra_ue='N' and fl_extra_ue='N' and fl_merce_intra_ue='N' and FL_SAN_MARINO_SENZA_IVA='N' and FL_SAN_MARINO_CON_IVA='N' and
	 terzo_uo.codice_univoco_ufficio_ipa is not null
   and not exists
   (select 1 from modello2_pcc
   where
   modello2_pcc.NUMERO_FATTURA = fattura_passiva.NR_FATTURA_FORNITORE AND
   modello2_pcc.data_emissione = fattura_passiva.DT_FATTURA_FORNITORE AND
   modello2_pcc.id_fiscale_IVA =substr(decode(nazione.TI_NAZIONE,'E',nvl(anagrafico.partita_iva,anagrafico.codice_fiscale),decode(nazione.cd_iso||anagrafico.partita_iva,nazione.cd_iso,anagrafico.codice_fiscale,nazione.cd_iso||anagrafico.partita_iva)),0,16))
   order by esercizio,fattura_passiva.cd_unita_organizzativa,pg_fattura_passiva;



	cursor cig_cupContratto(es number,cds varchar2,uo varchar2,pg number,cd_terzo_in number) is
	select decode(v_doc_passivo_obbligazione.CD_TIPO_DOCUMENTO_AMM,'COMPENSO',sum(v_doc_passivo_obbligazione.IM_scadenza),sum(IM_IMPONIBILE_DOC_AMM+IM_IVA_DOC_AMM )) imp,null cd_cig,null cd_cup
	from v_doc_passivo_obbligazione,obbligazione,obbligazione_scadenzario
        where
        v_doc_passivo_obbligazione.cd_cds  = cds  and
        v_doc_passivo_obbligazione.cd_unita_organizzativa  = uo and
        v_doc_passivo_obbligazione.esercizio               = es  and
        v_doc_passivo_obbligazione.PG_DOCUMENTO_AMM      = pg  and
        v_doc_passivo_obbligazione.CD_TERZO              =cd_terzo_in  AND
        v_doc_passivo_obbligazione.CD_TIPO_DOCUMENTO_AMM in('FATTURA_P','COMPENSO')
        and	obbligazione.cd_cds                 = obbligazione_scadenzario.cd_cds
        AND obbligazione.esercizio              = obbligazione_scadenzario.esercizio
        AND obbligazione.esercizio_originale    = obbligazione_scadenzario.esercizio_originale
        AND obbligazione.pg_obbligazione        = obbligazione_scadenzario.pg_obbligazione
        AND obbligazione_scadenzario.cd_cds                      = v_doc_passivo_obbligazione.cd_cds_obbligazione
        AND obbligazione_scadenzario.esercizio                   = v_doc_passivo_obbligazione.esercizio_obbligazione
        AND obbligazione_scadenzario.esercizio_originale         = v_doc_passivo_obbligazione.esercizio_ori_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione             = v_doc_passivo_obbligazione.pg_obbligazione
        AND obbligazione_scadenzario.pg_obbligazione_scadenzario = v_doc_passivo_obbligazione.pg_obbligazione_scadenzario
        
        
        
        group by v_doc_passivo_obbligazione.CD_TIPO_DOCUMENTO_AMM;
   conta_voce_iva number:=0;
   conta_cig_cup number:=0;
   t testata%rowtype;
   
   c cig_cupContratto%rowtype;
begin

open testata;
loop
fetch testata  into t;
exit when testata%notfound;
 
  
  
  
  
  open cig_cupContratto(t.esercizio,t.cd_cds,t.cd_unita_organizzativa,nvl(t.pg_compenso,t.pg_fattura_passiva),t.cd_terzo);
  loop
  	
    fetch cig_cupContratto into c;
    exit when cig_cupContratto%notfound;
        
					 
					 
					 	  insert into modello2_pcc(codice_fiscale_amm,
						  CODICE_UFFICIO	,
							DENOMINAZIONE_AMMINISTRAZIONE	,
							CODICE_FISCALE	              ,
							ID_FISCALE_IVA	              ,
							DENOMINAZIONE_FORNITORE	      ,
							DESCRIZIONE_LOTTO             ,
							TIPO_DOCUMENTO		            ,
							NUMERO_FATTURA			          ,
							DATA_EMISSIONE	              ,
							IMPORTO_TOTALE	              ,
							DESCRIZIONE	                  ,
							ART73	                        ,
							TOTALE_IMPONIBILE	            ,
							TOTALE_IMPOSTA                ,
							DATA_TERMINI                  ,
							GG_TERMINI                    ,
							DT_SCADENZA                   ,
							IMPORTO_PAGAMENTO	            ,
							NUMERO_PROTOCOLLO	            ,
							DATA_PROTOCOLLO	            ,
							aliquota_iva,
						 codice_esenzione,
						 totale_imponibile_ali,
						 totale_imposta_ali  ,
						 importo_cig_cup,
						 codice_cig,
						 codice_cup	,
						 CODICE_SEGNALAZIONE,
						 DESCRIZIONE_SEGNALAZIONE ) values
					('80054330586',nvl(t.codice_univoco_ufficio_ipa,'0H-QWX'),nvl(t.denominazione_sede,'Consiglio Nazionale delle Ricerche - CNR - Amministrazione Centrale'),
					
						substr(nvl(t.ana_codice_fiscale,t.ana_partita_iva),0,16),
						substr(decode(t.TI_NAZIONE,'E',nvl(t.ana_partita_iva,t.ana_codice_fiscale),decode(t.cd_iso||t.ana_partita_iva,t.cd_iso,t.ana_codice_fiscale,t.cd_iso||t.ana_partita_iva)) ,0,16),nvl(t.ana_ragione_sociale,t.ana_cognome||' '||t.ana_nome),
						to_char(sysdate,'dd/mm/yyyy hh24:mi'),decode(t.FL_FATTURA_COMPENSO,'Y','TD06',decode(t.ti_fattura,'F','TD01','C','TD04','TD05')),
						t.NR_FATTURA_FORNITORE,t.dt_FATTURA_FORNITORE,
						decode(t.FL_FATTURA_COMPENSO,'Y',c.imp,t.im_totale_fattura),
						
						substr(nvl(t.DS_FATTURA_PASSIVA,'Non indicata'),0,180),null,
						
						
						
						t.IM_TOTALE_IMPONIBILE,
						t.IM_TOTALE_IVA,
						null ,null,null,null,null,null,
						null,null,null,null,
						abs(decode(c.cd_cig,null,decode(c.cd_cup,null,null,c.imp),c.imp)),c.cd_cig,c.cd_cup,DECODE(T.IDENTIFICATIVO_SDI,NULL,NULL,'OK'),DECODE(T.IDENTIFICATIVO_SDI,NULL,NULL,'ELETTRONICA '||t.identificativo_sdi));
					
end loop;
close cig_cupContratto;
update modello2_pcc set DT_SCADENZA = t.dt_scadenza,importo_pagamento=decode(t.FL_FATTURA_COMPENSO,'Y',c.imp,t.im_totale_fattura),
					data_protocollo= nvl(t.data_protocollo,t.dt_registrazione), numero_protocollo= nvl(t.numero_protocollo,t.PROGR_UNIVOCO)
					where
					codice_fiscale=t.ana_codice_fiscale and
					NUMERO_FATTURA = t.NR_FATTURA_FORNITORE and
					data_emissione  = t.dt_FATTURA_FORNITORE and
					tipo_documento=decode(t.ti_fattura,'C','TD04',decode(t.FL_FATTURA_COMPENSO,'Y','TD06','TD01')) and
					DESCRIZIONE_LOTTO like to_char(sysdate,'dd/mm/yyyy hh24:mi')
					and rownum=1;

end loop;
close testata;
end;
end;
