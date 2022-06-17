CREATE OR REPLACE PROCEDURE SPG_CONGUAGLIO
--
-- Date: 17/05/2007
-- Version: 1.6
--
-- Protocollo VPG per stampa del prospetto del conguaglio
--
--
-- History:
--
-- Date: 10/11/2003
-- Version: 1.0
-- Creazione
--
-- Date: 27/01/2004
-- Version: 1.1
-- Corretta estrazione mandato principale del compenso
-- (esclusione dei mandati annullati)
--
-- Date: 30/01/2004
-- Version: 1.2
-- Normalizzazione del numero di giorni di spettanza al numero di giorni
-- dell'esercizio del conguaglio (richiesta n. 755)
--
-- Date: 14/12/2004
-- Version: 1.3
-- Errore nel recupero del terzo DUP_VAL_ON_INDEX
--
-- Date: 23/05/2006
-- Version: 1.4
-- Aggiunti i campi IM_DEDUZIONE_DOVUTO,IM_DEDUZIONE_GODUTO, IM_DEDUZIONE_FAMILY_DOVUTO, IM_FAMILY_DOVUTO, IM_DEDUZIONE_FAMILY_GODUTO,
-- IM_FAMILY_GODUTO per adeguamento alla no tax area e alla family area.
--
-- Date: 18/07/2006
-- Version: 1.5
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 17/05/2007
-- Version: 1.6
-- Aggiunti i campi FL_NOTAXAREA,FL_NOFAMILYAREA,FL_NO_DETRAZIONI_ALTRE,FL_NO_DETRAZIONI_FAMILY,FL_DETRAZIONI_ALTRI_TIPI,
-- IM_REDDITO_COMPLESSIVOIM_REDDITO_ABITAZ_PRINC,FL_APPLICA_DETR_PERS_MAX
--
-- Body:
(
 inCdCds in varchar2,
 inCdUo in varchar2,
 inEs in number,
 inPg in number
) is
 aId number;
 i number;
 aConguaglio conguaglio%rowtype;
 aAnagTerzo v_anagrafico_terzo%rowtype;
 aNum1 number;
 aTmp1 char(2);
 aTmp2 char(2);
 aTmp6 char(2);
 aIm1 number;
 aIm2 number;
 IM_CORI_SOSP_CONG number;
 AMMONTARE number;
 aNumGGYear number;
 aNumGGNormal number;
 aFlCarichiFamiliari char(1);
 conta number:=0;
begin
 select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;
 i:=0;

 -- conguaglio da stampare

 select * into aConguaglio
 from conguaglio
 where cd_cds 			  	  = inCdCds
   and cd_unita_organizzativa = inCdUo
   and esercizio 			  = inEs
   and pg_conguaglio		  = inPg;

 select cd_anag into aNum1
 from terzo
 where cd_terzo = aConguaglio.cd_terzo;

 select * into aAnagTerzo
 from v_anagrafico_terzo
 where cd_anag = aNum1
   And cd_terzo = aConguaglio.cd_terzo;

 -- dati anagrafici del percipiente --> TIPO A
 insert into vpg_conguaglio(ID,
							CHIAVE,
							TIPO,
							SEQUENZA,
							CD_CDS,
							CD_UNITA_ORGANIZZATIVA,
							ESERCIZIO,
							PG_CONGUAGLIO,
							CD_TERZO,
							CD_ANAG,
							NOME,
							COGNOME,
							RAGIONE_SOCIALE,
							DT_NASCITA,
							DS_COMUNE_NASCITA,
							DS_PROVINCIA_NASCITA,
							CD_PROVINCIA_NASCITA,
							VIA_SEDE,
							DS_COMUNE_SEDE,
							CD_PROVINCIA_SEDE,
							DS_PROVINCIA_SEDE,
							FRAZIONE_SEDE,
							VIA_FISCALE,
							DS_COMUNE_FISCALE,
							CD_PROVINCIA_FISCALE,
							DS_PROVINCIA_FISCALE,
							FRAZIONE_FISCALE,
							CAP_COMUNE_FISCALE,
							CODICE_FISCALE,
							PARTITA_IVA)
 values (aId,
 		'chiave',
		'A',
		i,
		aConguaglio.cd_cds,
		aConguaglio.cd_unita_organizzativa,
		aConguaglio.esercizio,
		aConguaglio.pg_conguaglio,
		aAnagTerzo.cd_terzo,
		aAnagTerzo.cd_anag,
		aConguaglio.nome,
		aConguaglio.cognome,
		aConguaglio.ragione_sociale,
		aAnagTerzo.dt_nascita,
		aAnagTerzo.ds_comune_nascita,
		aAnagTerzo.ds_provincia_nascita,
		aAnagTerzo.cd_provincia_nascita,
		aAnagTerzo.via_sede||decode(aAnagTerzo.numero_civico_sede,null,null,' n. '||aAnagTerzo.numero_civico_sede),
		aAnagTerzo.ds_comune_sede,
		aAnagTerzo.cd_provincia_sede,
		aAnagTerzo.ds_provincia_sede,
		aAnagTerzo.frazione_sede,
		aAnagTerzo.via_fiscale||decode(aAnagTerzo.num_civico_fiscale,null,null,' n. '||aAnagTerzo.num_civico_fiscale),
		aAnagTerzo.ds_comune_fiscale,
		aAnagTerzo.cd_provincia_fiscale,
		aAnagTerzo.ds_provincia_fiscale,
		aAnagTerzo.frazione_fiscale,
		aAnagTerzo.cap_comune_fiscale,
		aAnagTerzo.codice_fiscale,
		aAnagTerzo.partita_iva);

 -- dati relativi al conguaglio --> TIPO B
 i := i + 1;
 -- letti dalla tabella del conguaglio i flag delle detrazioni aTmp3,aTmp4,aTmp5
 begin
 	  select decode(FL_NOTAXAREA,'Y','SI','NO'), decode(FL_NOFAMILYAREA,'Y','SI','NO'),
 	  	 decode(FL_APPLICA_DETR_PERS_MAX,'Y','SI','NO')
 	  into aTmp1,aTmp2,aTmp6
	  from anagrafico_esercizio
	  where cd_anag   = aAnagTerzo.cd_anag
	    and esercizio = aConguaglio.esercizio;
 exception when no_data_found then
 		    aTmp1 := 'NO';
 		    aTmp2 := 'NO';
 		    aTmp6 := 'NO';
 end;

Select Decode(Count(1),0,'N','Y') into aFlCarichiFamiliari
 From CARICO_FAMILIARE_ANAG
 Where CARICO_FAMILIARE_ANAG.CD_ANAG = aAnagTerzo.CD_ANAG;

begin
	select nvl(IM_REDDITO_COMPLESSIVO,0), nvl(IM_REDDITO_ABITAZ_PRINC,0) into aIm1,aIm2
	from compenso comp
	where exists (select 1 from ass_compenso_conguaglio
	 	      where cd_cds_conguaglio    = aConguaglio.cd_cds
		      and cd_uo_conguaglio     = aConguaglio.cd_unita_organizzativa
		      and esercizio_conguaglio = aConguaglio.esercizio
	              and pg_conguaglio		= aConguaglio.pg_conguaglio
		      and cd_cds_compenso		= comp.cd_cds
	              and cd_uo_compenso		= comp.cd_unita_organizzativa
		      and esercizio_compenso	= comp.esercizio
		      and pg_compenso			= comp.pg_compenso)
	and FL_COMPENSO_CONGUAGLIO='Y';
exception when no_data_found then
 		    aIm1 := 0;
 		    aIm2 := 0;
end;

 -- normalizzazione del numero di giorni di spettanza
 aNumGGYear := to_date(aConguaglio.esercizio+1||'0101','YYYYMMDD')-to_date(aConguaglio.esercizio||'0101','YYYYMMDD');
 if aConguaglio.NUMERO_GIORNI > aNumGGYear then
 	aNumGGNormal := aNumGGYear;
 else
 	aNumGGNormal := aConguaglio.NUMERO_GIORNI;
 end if;

	                 -- PRENDO il compenso che paga il conguaglio
        	                 Select Sum(Nvl(IM_CORI_SOSPESO,0)) Into IM_CORI_SOSP_CONG
        	                 From CONTRIBUTO_RITENUTA
        	                 Where
        	                 CONTRIBUTO_RITENUTA.ESERCIZIO                  = aConguaglio.esercizio_compenso               And
        	                 CONTRIBUTO_RITENUTA.CD_CDS                     = aConguaglio.cd_cds_Compenso                  And
        	                 CONTRIBUTO_RITENUTA.cd_unita_organizzativa     = aConguaglio.CD_UO_COMPENSO  And
        	                 CONTRIBUTO_RITENUTA.pg_compenso                = aConguaglio.pg_compenso;

        Select Nvl(Sum(Nvl(AMMONTARE,0)),0) Into  AMMONTARE
                      	                 From CONTRIBUTO_RITENUTA,tipo_contributo_ritenuta
        	                 Where
        	                 tipo_contributo_ritenuta.cd_contributo_ritenuta = contributo_ritenuta.cd_contributo_ritenuta And
        	                 tipo_contributo_ritenuta.dt_ini_validita        = contributo_ritenuta.dt_ini_validita              And
        	                 CONTRIBUTO_RITENUTA.ESERCIZIO                  = aConguaglio.esercizio_compenso               And
        	                 CONTRIBUTO_RITENUTA.CD_CDS                     = aConguaglio.cd_cds_Compenso                  And
        	                 CONTRIBUTO_RITENUTA.cd_unita_organizzativa     = aConguaglio.CD_UO_COMPENSO  And
        	                 CONTRIBUTO_RITENUTA.pg_compenso                = aConguaglio.pg_compenso And
                                 CNRCTB545.getIsIrpefScaglioni(tipo_contributo_ritenuta.cd_classificazione_cori,
                                             tipo_contributo_ritenuta.pg_classificazione_montanti,
                                             tipo_contributo_ritenuta.fl_scrivi_montanti) = 'Y';

 insert into vpg_conguaglio(ID,
							CHIAVE,
							TIPO,
							SEQUENZA,
							CD_CDS,
							CD_UNITA_ORGANIZZATIVA,
							ESERCIZIO,
							PG_CONGUAGLIO,
							DT_REGISTRAZIONE,
							DS_CONGUAGLIO,
							ALIQUOTA_FISCALE,
							IM_DETRAZIONE_PERSONALE_ANAG,
							FL_NOTAXAREA,
							FL_NOFAMILYAREA,
							FL_ESCLUDI_QVARIA_DEDUZIONE,
							FL_INTERA_QFISSA_DEDUZIONE,
							DETRAZIONI_PE_DOVUTO,
							DETRAZIONI_PE_GODUTO,
							DETRAZIONI_LA_DOVUTO,
							DETRAZIONI_LA_GODUTO,
							DETRAZIONI_CO_DOVUTO,
							DETRAZIONI_CO_GODUTO,
							DETRAZIONI_FI_DOVUTO,
							DETRAZIONI_FI_GODUTO,
							DETRAZIONI_AL_DOVUTO,
							DETRAZIONI_AL_GODUTO,
							IM_DEDUZIONE_DOVUTO,
							IM_DEDUZIONE_GODUTO,
							IM_IRPEF_DOVUTO,
							IM_IRPEF_GODUTO,
							FL_ACCANTONA_ADD_TERR,
							IM_ADDREG_DOVUTO,
							IM_ADDREG_GODUTO,
							IM_ADDPROV_DOVUTO,
							IM_ADDPROV_GODUTO,
							IM_ADDCOM_DOVUTO,
							IM_ADDCOM_GODUTO,
							NUMERO_GIORNI,
							IMPONIBILE_IRPEF_LORDO,
							IMPONIBILE_IRPEF_NETTO,
							IM_DEDUZIONE_FAMILY_DOVUTO,
							IM_FAMILY_DOVUTO,
							IM_DEDUZIONE_FAMILY_GODUTO,
							IM_FAMILY_GODUTO,
							FL_CARICHI_FAMILIARI,
							FL_NO_DETRAZIONI_ALTRE,
							FL_NO_DETRAZIONI_FAMILY,
							FL_DETRAZIONI_ALTRI_TIPI,
							IM_REDDITO_COMPLESSIVO,
							IM_REDDITO_ABITAZ_PRINC,
							FL_APPLICA_DETR_PERS_MAX,
							FL_DETRAZIONI_FAM_INTERO_ANNO,
							CD_ANAG,
							CODICE_FISCALE_ESTERNO,
							DT_DA_COMPETENZA_ESTERNO,
              DT_A_COMPETENZA_ESTERNO,
              IMPONIBILE_FISCALE_ESTERNO,
              IM_IRPEF_ESTERNO,
              IM_ADDREG_ESTERNO,
              IM_ADDPROV_ESTERNO,
              IM_ADDCOM_ESTERNO,
              DETRAZIONI_LA_ESTERNO,
              DETRAZIONI_PE_ESTERNO,
              DETRAZIONI_CO_ESTERNO,
              DETRAZIONI_FI_ESTERNO,
              DETRAZIONI_AL_ESTERNO,
              NUMERO_GIORNI_ESTERNO,
              PG_COMUNE_ADDCOM_ESTERNO,
              IM_CORI_SOSPESO,
              IM_CORI_SOSP_CONG,
              AMMONTARE,
              FL_NO_CREDITO_IRPEF,
              FL_NO_CREDITO_CUNEO_IRPEF,
              FL_NO_DETR_CUNEO_IRPEF,
              IM_CREDITO_IRPEF_GODUTO,
              IM_CREDITO_IRPEF_DOVUTO,
              IM_BONUS_IRPEF_GODUTO,
              IM_BONUS_IRPEF_DOVUTO,
              DETRAZIONE_RID_CUNEO_ESTERNO,
              DETRAZIONE_RID_CUNEO_GODUTO,
              DETRAZIONE_RID_CUNEO_DOVUTO)
 values (aId,
 		'chiave',
		'B',
		i,
		aConguaglio.cd_cds,
		aConguaglio.cd_unita_organizzativa,
		aConguaglio.esercizio,
		aConguaglio.pg_conguaglio,
		aConguaglio.dt_registrazione,
		aConguaglio.ds_conguaglio,
		aAnagTerzo.aliquota_fiscale,
		aConguaglio.IM_DETRAZIONE_PERSONALE_ANAG,
		aTmp1,
		aTmp2,
		decode(aConguaglio.FL_ESCLUDI_QVARIA_DEDUZIONE,'Y','SI','NO'),
		decode(aConguaglio.FL_INTERA_QFISSA_DEDUZIONE,'Y','SI','NO'),
		aConguaglio.DETRAZIONI_PE_DOVUTO,
		aConguaglio.DETRAZIONI_PE_GODUTO,
		aConguaglio.DETRAZIONI_LA_DOVUTO,
		aConguaglio.DETRAZIONI_LA_GODUTO,
		aConguaglio.DETRAZIONI_CO_DOVUTO,
		aConguaglio.DETRAZIONI_CO_GODUTO,
		aConguaglio.DETRAZIONI_FI_DOVUTO,
		aConguaglio.DETRAZIONI_FI_GODUTO,
		aConguaglio.DETRAZIONI_AL_DOVUTO,
		aConguaglio.DETRAZIONI_AL_GODUTO,
		aConguaglio.IM_DEDUZIONE_DOVUTO,
		aConguaglio.IM_DEDUZIONE_GODUTO,
		aConguaglio.IM_IRPEF_DOVUTO,
		aConguaglio.IM_IRPEF_GODUTO,
		aConguaglio.FL_ACCANTONA_ADD_TERR,
		aConguaglio.IM_ADDREG_DOVUTO,
		aConguaglio.IM_ADDREG_GODUTO,
		aConguaglio.IM_ADDPROV_DOVUTO,
		aConguaglio.IM_ADDPROV_GODUTO,
		aConguaglio.IM_ADDCOM_DOVUTO,
		aConguaglio.IM_ADDCOM_GODUTO,
		aNumGGNormal,
		aConguaglio.IMPONIBILE_IRPEF_LORDO,
		aConguaglio.IMPONIBILE_IRPEF_NETTO,
		aConguaglio.IM_DEDUZIONE_FAMILY_DOVUTO,
		aConguaglio.IM_FAMILY_DOVUTO,
		aConguaglio.IM_DEDUZIONE_FAMILY_GODUTO,
		aConguaglio.IM_FAMILY_GODUTO,
		aFlCarichiFamiliari,
	  decode(aConguaglio.FL_NO_DETRAZIONI_ALTRE,'Y','SI','NO'), decode(aConguaglio.FL_NO_DETRAZIONI_FAMILY,'Y','SI','NO'),decode(aConguaglio.FL_DETRAZIONI_ALTRI_TIPI,'Y','SI','NO'),
		aIm1,
		aIm2,
		aTmp6,
		aConguaglio.FL_DETRAZIONI_FAM_INTERO_ANNO,
		aAnagTerzo.cd_anag,
		aConguaglio.CODICE_FISCALE_ESTERNO,
		aConguaglio.DT_DA_COMPETENZA_ESTERNO,
    aConguaglio.DT_A_COMPETENZA_ESTERNO,
    aConguaglio.IMPONIBILE_FISCALE_ESTERNO,
    aConguaglio.IM_IRPEF_ESTERNO,
    aConguaglio.IM_ADDREG_ESTERNO,
    aConguaglio.IM_ADDPROV_ESTERNO,
    aConguaglio.IM_ADDCOM_ESTERNO,
    aConguaglio.DETRAZIONI_LA_ESTERNO,
    aConguaglio.DETRAZIONI_PE_ESTERNO,
    aConguaglio.DETRAZIONI_CO_ESTERNO,
    aConguaglio.DETRAZIONI_FI_ESTERNO,
    aConguaglio.DETRAZIONI_AL_ESTERNO,
    aConguaglio.NUMERO_GIORNI_ESTERNO,
    aConguaglio.PG_COMUNE_ADDCOM_ESTERNO,
    aConguaglio.IM_CORI_SOSPESO,
    IM_CORI_SOSP_CONG,
    AMMONTARE,
    decode(aConguaglio.FL_NO_CREDITO_IRPEF,'Y','SI','NO'),
    decode(aConguaglio.FL_NO_CREDITO_CUNEO_IRPEF,'Y','SI','NO'),
    decode(aConguaglio.FL_NO_DETR_CUNEO_IRPEF,'Y','SI','NO'),
    aConguaglio.IM_CREDITO_IRPEF_GODUTO + aConguaglio.IM_CRED_IRPEF_PAR_DET_GODUTO,
    aConguaglio.IM_CREDITO_IRPEF_DOVUTO + aConguaglio.IM_CRED_IRPEF_PAR_DET_DOVUTO,
    aConguaglio.IM_BONUS_IRPEF_GODUTO,
    aConguaglio.IM_BONUS_IRPEF_DOVUTO,
    aConguaglio.DETRAZIONE_RID_CUNEO_ESTERNO,
    aConguaglio.DETRAZIONE_RID_CUNEO_GODUTO,
    aConguaglio.DETRAZIONE_RID_CUNEO_DOVUTO);

 -- dettagli compensi inseriti in conguaglio --> TIPO C
 for aComp in (select * from compenso comp
 	 	   	   where exists (select 1 from ass_compenso_conguaglio
 	 	   	   		 		 where cd_cds_conguaglio    = aConguaglio.cd_cds
			     			   and cd_uo_conguaglio     = aConguaglio.cd_unita_organizzativa
				 			   and esercizio_conguaglio = aConguaglio.esercizio
				 			   and pg_conguaglio		= aConguaglio.pg_conguaglio
							   and cd_cds_compenso		= comp.cd_cds
							   and cd_uo_compenso		= comp.cd_unita_organizzativa
							   and esercizio_compenso	= comp.esercizio
							   and pg_compenso			= comp.pg_compenso)
			   order by pg_compenso) loop
	-- inizio loop 1 (compensi in conguaglio)
	--dbms_output.put_line('compenso '||Acomp.pg_compenso);
	i := i + 1;
	if not(aComp.cd_cds 	   	  		= aConguaglio.cd_cds_compenso
	   and aComp.cd_unita_organizzativa = aConguaglio.cd_uo_compenso
	   and aComp.esercizio				= aConguaglio.esercizio_compenso
	   and aComp.pg_compenso			= aConguaglio.pg_compenso) then
	   -- escludo il compenso che paga il conguaglio
   --dbms_output.put_line('entro compenso '||Acomp.pg_compenso);
		-- determino il progressivo del mandato principale
		if aComp.pg_obbligazione is not null
		   and aComp.stato_pagamento_fondo_eco = 'N' then
			begin
				 select mriga.pg_mandato into aNum1
				 from mandato_riga mriga,
				 	  mandato man
				 where mriga.cd_cds 	   			     = aComp.cd_cds_obbligazione
				   and mriga.esercizio_obbligazione		 = aComp.esercizio_obbligazione
				   and mriga.esercizio_ori_obbligazione		 = aComp.esercizio_ori_obbligazione
				   and mriga.pg_obbligazione			 = aComp.pg_obbligazione
				   and mriga.pg_obbligazione_scadenzario = aComp.pg_obbligazione_scadenzario
				   and mriga.cd_cds_doc_amm			   	 = aComp.cd_cds
				   and mriga.cd_uo_doc_amm			   	 = aComp.cd_unita_organizzativa
				   and mriga.esercizio_doc_amm		   	 = aComp.esercizio
				   and mriga.cd_tipo_documento_amm	   	 = 'COMPENSO'
				   and mriga.pg_doc_amm				   	 = aComp.pg_compenso
				   and man.cd_cds				   		 = mriga.cd_cds
				   and man.esercizio			   		 = mriga.esercizio
				   and man.pg_mandato			   		 = mriga.pg_mandato
				   and man.dt_annullamento is null;
			exception when NO_DATA_FOUND then
					  aNum1 := null;
			end;
		else -- non esiste mandato principale
			aNum1 := null;
		end if;

		begin
		  select 1 into conta
		  	from tipo_trattamento tt
		where tt.CD_TRATTAMENTO  = aComp.cd_trattamento
		  and tt.DT_INI_VALIDITA <= aComp.dt_registrazione
		  and tt.DT_FIN_VALIDITA >  aComp.dt_registrazione;
		exception when no_data_found then
		   insert into vpg_conguaglio (ID,
									CHIAVE,
									TIPO,
									SEQUENZA,
									CD_CDS,
									CD_UNITA_ORGANIZZATIVA,
									ESERCIZIO,
									PG_CONGUAGLIO,
									CD_CDS_COMPENSO,
									CD_UO_COMPENSO,
									ESERCIZIO_COMPENSO,
									PG_COMPENSO,
									CD_TERZO_COMPENSO,
									DS_TRATTAMENTO,
									FL_SENZA_CALCOLI,
									FL_PAGAM_FONDO_ECO,
									PG_MANDATO)
									values
				( aId,
			   'chiave',
			   'C',
			   i,
			   aConguaglio.cd_cds,
			   aConguaglio.cd_unita_organizzativa,
			   aConguaglio.esercizio,
			   aConguaglio.pg_conguaglio,
			   aComp.cd_cds,
			   aComp.cd_unita_organizzativa,
			   aComp.esercizio,
			   aComp.pg_compenso,
			   aComp.cd_terzo,
			   'Trattamento chiuso',
			   decode(aComp.fl_senza_calcoli,'Y','SI','NO'),
			   decode(aComp.stato_pagamento_fondo_eco,'N','NO','SI'),
			   aNum1);
		end;

		insert into vpg_conguaglio (ID,
									CHIAVE,
									TIPO,
									SEQUENZA,
									CD_CDS,
									CD_UNITA_ORGANIZZATIVA,
									ESERCIZIO,
									PG_CONGUAGLIO,
									CD_CDS_COMPENSO,
									CD_UO_COMPENSO,
									ESERCIZIO_COMPENSO,
									PG_COMPENSO,
									CD_TERZO_COMPENSO,
									DS_TRATTAMENTO,
									FL_SENZA_CALCOLI,
									FL_PAGAM_FONDO_ECO,
									PG_MANDATO)
		select aId,
			   'chiave',
			   'C',
			   i,
			   aConguaglio.cd_cds,
			   aConguaglio.cd_unita_organizzativa,
			   aConguaglio.esercizio,
			   aConguaglio.pg_conguaglio,
			   aComp.cd_cds,
			   aComp.cd_unita_organizzativa,
			   aComp.esercizio,
			   aComp.pg_compenso,
			   aComp.cd_terzo,
			   tt.DS_TI_TRATTAMENTO,
			   decode(aComp.fl_senza_calcoli,'Y','SI','NO'),
			   decode(aComp.stato_pagamento_fondo_eco,'N','NO','SI'),
			   aNum1
		from tipo_trattamento tt
		where tt.CD_TRATTAMENTO  = aComp.cd_trattamento
		  and tt.DT_INI_VALIDITA <= aComp.dt_registrazione
		  and tt.DT_FIN_VALIDITA >  aComp.dt_registrazione;
	end if;
 end loop; -- fine loop 1 (compensi in conguaglio)

-- open tc for
-- 	  select * from VPG_CONGUAGLIO where id = aId;

--  close tc; --- ELIMINARE PER RICHIAMARE DA CR !!!

end;
/


