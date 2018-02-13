--------------------------------------------------------
--  DDL for View V_TERZO_SIP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_TERZO_SIP" ("CD_TERZO", "CD_ANAG", "DENOMINAZIONE_SEDE", "TI_ENTITA", "CODICE_FISCALE", "PARTITA_IVA", "NOME", "COGNOME", "MATRICOLA", "QUALIFICA", "DESC_QUALIFICA", "TIPO_RAPPORTO", "UO", "ANNO_RIF", "MESE_RIF", "DATA_CESSAZIONE", "LIVELLO_1", "FASCIA", "PERC_PART_TIME", "DT_INI_VALIDITA", "DT_FIN_VALIDITA", "ANNO_MESE", "COSTO") AS 
  SELECT
	   TERZO.CD_TERZO,
	   TERZO.CD_ANAG,
	   TERZO.DENOMINAZIONE_SEDE,
	   ANAGRAFICO.TI_ENTITA,
	   ANAGRAFICO.CODICE_FISCALE,
	   ANAGRAFICO.PARTITA_IVA,
	   ANAGRAFICO.NOME,
	   ANAGRAFICO.COGNOME,
	   null matricola,
	   null qualifica,
	   null desc_qualifica,
	   null rapporto,
	   null UO,
	   null anno_rif,
	   null mese_rif,
	   TERZO.DT_FINE_RAPPORTO DATA_CESSAZIONE,
	   Null livello_1,
	   Null fascia,
	   Null perc_part_time,
	   r.dt_ini_validita dt_ini_validita,
	   r.dt_fin_validita dt_fin_validita,
	   Null anno_mese,
	   Null costo
FROM
	TERZO,
	ANAGRAFICO,
	rapporto r
WHERE
	 TERZO.CD_ANAG = ANAGRAFICO.CD_ANAG And
	 r.cd_anag=anagrafico.cd_anag       And
	 r.cd_tipo_rapporto!='DIP'
Union
Select     TERZO.CD_TERZO,
	   TERZO.CD_ANAG,
	   TERZO.DENOMINAZIONE_SEDE,
	   ANAGRAFICO.TI_ENTITA,
	   ANAGRAFICO.CODICE_FISCALE,
	   ANAGRAFICO.PARTITA_IVA,
	   ANAGRAFICO.NOME,
	   ANAGRAFICO.COGNOME,
	   CNR_ANADIP.MATRICOLA matricola,
	   profilo qualifica,
	   DESC_PROFILO desc_qualifica,
	   CNR_ANADIP.RAPP_IMPIEGO rapporto,
	   decode(nvl(CNR_ANADIP.TIT_AFFEREN,' '),' ',null,SUBSTR(CNR_ANADIP.TIT_AFFEREN,1,3)||'.'||SUBSTR(CNR_ANADIP.TIT_AFFEREN,4,3)) UO,
	   CNR_ANADIP.ANNO_RIF anno_rif,
	   CNR_ANADIP.MESE_RIF mese_rif,
	   TERZO.DT_FINE_RAPPORTO DATA_CESSAZIONE,
	   CNR_ANADIP.livello_1,
	   CNR_ANADIP.FASCIA_ECOCLASSE fascia,
	   CNR_ANADIP.att_rid_stip perc_part_time,
	   RAPPORTO.dt_ini_validita dt_ini_validita,
	   RAPPORTO.dt_fin_validita dt_fin_validita,
	   CNR_ANADIP.ANNO_RIF||lpad(CNR_ANADIP.MESE_RIF,2,'0') anno_mese,
	  sum(costo_del_dipendente.IM_A1 +costo_del_dipendente.IM_ONERI_CNR_A1+costo_del_dipendente.IM_TFR_A1) costo
 From   TERZO,
	ANAGRAFICO,
	CNR_ANADIP,
	RAPPORTO,
	costo_del_dipendente
Where
         mese_rif!='0'                                          And
         TERZO.CD_ANAG = ANAGRAFICO.CD_ANAG                     And
         RAPPORTO.CD_ANAG = ANAGRAFICO.CD_ANAG                  And
	 RAPPORTO.CD_TIPO_RAPPORTO ='DIP'                       And
	 ANAGRAFICO.CODICE_FISCALE=CNR_ANADIP.DIP_COD_FIS       And
	 (CNR_ANADIP.uo_tit!='900300' Or
	 cnr_anadip.uo_tit Is Null)   and
	 costo_del_dipendente.id_matricola (+)=cnr_anadip.matricola and
	 costo_del_dipendente.mese (+)=mese_rif and
	 costo_del_dipendente.esercizio (+)=anno_rif and
	 costo_del_dipendente.ti_prev_cons(+)!='P'
	 group by
	    TERZO.CD_TERZO,
	   TERZO.CD_ANAG,
	   TERZO.DENOMINAZIONE_SEDE,
	   ANAGRAFICO.TI_ENTITA,
	   ANAGRAFICO.CODICE_FISCALE,
	   ANAGRAFICO.PARTITA_IVA,
	   ANAGRAFICO.NOME,
	   ANAGRAFICO.COGNOME,
	   CNR_ANADIP.MATRICOLA,
	   profilo,
	   DESC_PROFILO,
	   CNR_ANADIP.RAPP_IMPIEGO,
	   decode(nvl(CNR_ANADIP.TIT_AFFEREN,' '),' ',null,SUBSTR(CNR_ANADIP.TIT_AFFEREN,1,3)||'.'||SUBSTR(CNR_ANADIP.TIT_AFFEREN,4,3)),
	   CNR_ANADIP.ANNO_RIF ,
	   CNR_ANADIP.MESE_RIF,
	   TERZO.DT_FINE_RAPPORTO ,
	   CNR_ANADIP.livello_1,
	   CNR_ANADIP.FASCIA_ECOCLASSE ,
	   CNR_ANADIP.att_rid_stip ,
	   RAPPORTO.dt_ini_validita ,
	   RAPPORTO.dt_fin_validita ,
	   CNR_ANADIP.ANNO_RIF||lpad(CNR_ANADIP.MESE_RIF,2,'0');
