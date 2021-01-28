--------------------------------------------------------
--  DDL for Package Body CNRMIG080
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRMIG080" is

 lPgExec number;

 procedure IMPOSTA_DATA_CESSAZIONE (aAnnoRif Number, aMeseRif Number, aUtente varchar2) is
 	dt_cessazione date;
 	aAnnoRif_prec number;
 	aMeseRif_prec number;
 Begin
 --Dbms_Output.put_line('IMPOSTA_DATA_CESSAZIONE');
 	if aMeseRif = 1 then
 		aMeseRif_prec := 12;
 		aAnnoRif_prec := aAnnoRif-1;
 	else
 		aMeseRif_prec := aMeseRif-1;
 		aAnnoRif_prec := aAnnoRif;
 	end if;

 	--dt_cessazione := last_day(to_date(''||aMeseRif_prec||'/'||aAnnoRif||'', 'MM/YYYY'));
 	dt_cessazione := trunc(sysdate);
 	gPgLog := ibmutl200.LOGSTART('Migrazione Anagarfica dipendenti' , aUtente, 1, 1);
 	ibmutl200.logInf(gPgLog ,'Chiusura Rapporto e Inquadramento per i dipendenti non pervenuti','','');
 	for aAna in (select distinct matricola_dipendente, cd_anag
			from rapporto
		       where matricola_dipendente in (select matricola from cnr_anadip
							where anno_rif = aAnnoRif_prec
							and   mese_rif = aMeseRif_prec
							and data_cessazione is null
			  			      minus
			  				select matricola from cnr_anadip
							where anno_rif = aAnnoRif
							and   mese_rif = aMeseRif
							and (uo_tit is null or uo_tit!='900300')
							)) loop
		update rapporto
		set dt_fin_validita = dt_cessazione,
			duva = trunc(sysdate),
			utuv = 'CED'
		where matricola_dipendente = aAna.matricola_dipendente
		and cd_tipo_rapporto='DIP'
		--and dt_fin_validita = to_date('31/12/2200','dd/mm/yyyy')
		and dt_fin_validita > trunc(sysdate);

		update inquadramento
		set dt_fin_validita = dt_cessazione,
			duva = trunc(sysdate),
			utuv = 'CED',
			pg_ver_rec=pg_ver_rec+1
		where cd_anag = aAna.cd_anag
		and cd_tipo_rapporto='DIP'
		--and dt_fin_validita = to_date('31/12/2200','dd/mm/yyyy')
		and dt_fin_validita > trunc(sysdate);

	end loop;

	caricaAnagDipendenti(aAnnoRif,aMeseRif,aUtente);

end;

procedure caricaAnagDipendenti(aAnnoRif Number, aMeseRif Number, aUtente varchar2) is
 lData date;
 lAnagraficoOld anagrafico%rowtype;
 cod_fis_mod NUMBER :=0;
 Begin
 --Dbms_Output.put_line('caricaAnagDipendenti');

 	  -- La tabella CNR_ANADIP in questa fase deve essere già caricata con i dati
	  -- relativi alla migrazione.
	  -- La tabella in esame risulta caricata con i dati dei dipendenti
	  -- provenienti dal sistema SCI.
 	  -- Per ogni dipendente presente nella tabella di lavoro CNR_ANADIP
	  -- si devono controllare e valorizzare dei campi.
	  lData := TRUNC(sysdate);

	  LOCK TABLE cnr_anadip IN EXCLUSIVE MODE NOWAIT;

	  for aAnaDip in (select *
	  	  		  	  from cnr_anadip
					  where anno_rif = aAnnoRif
					  and   mese_rif = aMeseRif
					  and (uo_tit is null or uo_tit!='900300')
					  ORDER BY MATRICOLA) loop
	  	  begin

	  	  	  	ibmutl200.logInf(gPgLog ,'Processando matricola' || aAnaDip.matricola , '' , '');
			    if chkPresenzaAnag(aAnaDip, lAnagraficoOld) then
			   	   MODIFICAANAGRAFICO(aAnaDip, lAnagraficoOld, lData, aUtente);
			    Else
			      Select Count(0) Into cod_fis_mod
			      From rapporto ,anagrafico
			      Where
			        anagrafico.cd_anag = rapporto.cd_anag And
			        dt_fine_rapporto Is Null And
			        (matricola_dipendente =aAnaDip.matricola or
			        (partita_iva is not null and
			        anagrafico.codice_fiscale = aAnaDip.DIP_COD_FIS));

			       If(cod_fis_mod=0) Then
			   	        CREAANAGRAFICO(aAnaDip,lData, aUtente);
			   	   Else
			   	      --RP 24/04/2009
			   	      -- Scommentare dopo un periodo di test con il messaggio bloccante
			   	    update anagrafico set dt_fine_rapporto = trunc(sysdate) ,
			   	                           causale_fine_rapporto='Codice fiscale dip. modificato o con partita iva',
			   	                           duva =sysdate,
			   	                           utuv=aUtente
			   	                           where (cd_anag in(select cd_anag from rapporto where  matricola_dipendente =aAnaDip.matricola) or
			   	                                  codice_fiscale = aAnaDip.dip_cod_fis);
			   	     update terzo set dt_fine_rapporto = trunc(sysdate) ,
			   	                           note='Codice fiscale dip. modificato o con partita iva',
			   	                           duva =sysdate,
			   	                           utuv=aUtente
			   	                           where (cd_anag in(select cd_anag from rapporto where  matricola_dipendente =aAnaDip.matricola) or
			   	                                  cd_anag in(select cd_anag from anagrafico where codice_fiscale = aAnaDip.dip_cod_fis));

			   	      CREAANAGRAFICO(aAnaDip,lData, aUtente);
			   	      Dbms_Output.PUT_LINE('Errore bloccante Codice fiscale modificato o presenza partita iva matricola dipendente '||aAnaDip.matricola);
			   	      IBMUTL200.logErr(gPgLog, DBMS_UTILITY.FORMAT_ERROR_STACK,'Codice fiscale modificato  o presenza partita iva matricola dipendente '||aAnaDip.matricola,null);

			   	  End If;
			    end if;
	  	  end;
-- Flavia -- modifica: ALLINEA_MONTANTE era fuori dal loop, è stata spostata perchè sono cambiati i parametri di input
	  	  --ALLINEA_MONTANTE(lData, aUtente, aAnnoRif, aMeseRif, aAnaDip);
  	  end loop;
end;

procedure CREAANAGRAFICO(aAnaDip cnr_anadip%rowtype, aData Date, aUtente VARCHAR2) is
 lAnagrafico anagrafico%rowtype;
 aRapporto rapporto%rowtype;
 aRapportoPrec rapporto%rowtype;
 lPgAnagrafico number;

 lItaliano CHAR(1);
 lPgComuneNascita number;
 lPgComuneFiscale number;

 lPgNazioneFiscale number;
 lPgNazioneNascita number;
 lPgNazionalita number;

 begin
--Dbms_Output.put_line('CREAANAGRAFICO '||' matricola '||aAnaDip.matricola);
 	  ibmutl200.logInf(gPgLog ,'Creazione anagrafico nuovo matricola' || aAnaDip.matricola , '' , '');
 	  -- recuperiamo il progressivo da attribuire alla'angrafico
 	  begin
		  select cd_corrente + 1
		  into lPgAnagrafico
		  from numerazione_base
		  where tabella ='ANAGRAFICO' for update nowait;
	  exception when no_data_found then
	  			ibmerr001.RAISE_ERR_GENERICO('Manca la configurazione della tabella ANAGRAFICO');
	  end;

      if substr(aAnaDip.cod_comune,1,1)= 'Z' then
	  	 lItaliano := 'E';
	  else
	  	 lItaliano := 'I';
	  end if;

	  lPgComuneNascita  := getComuneNascita(aAnaDip,  lAnagrafico,lPgNazioneNascita);
	  lPgComuneFiscale  := getComuneFiscale(aAnaDip,  lAnagrafico,lPgNazioneFiscale);

	  begin
		  select pg_nazione
		  into lPgNazionalita
		  from nazione
		  where cd_nazione = aAnaDip.naz_nas
		  and 	rownum=1;
	  exception when no_data_found then
	  			lPgNazionalita := 0;
			  	ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Impostata Nazionalita Default (0) in creAnagrafico'|| ' la nazionane '|| aAnaDip.naz_nas || ' non risulta nella tabella Nazione' , '');
	  end;

 	  lAnagrafico.CD_ANAG                    	 := lPgAnagrafico;
	  lAnagrafico.CD_CLASSIFIC_ANAG 		 := NULL;
	  lAnagrafico.TI_ENTITA 			 := 'F';
	  lAnagrafico.TI_ENTITA_FISICA 			 := 'A';
	  lAnagrafico.TI_ENTITA_GIURIDICA 		 := NULL;
	  lAnagrafico.FL_SOGGETTO_IVA 			 := 'N';
	  lAnagrafico.CODICE_FISCALE 			 := aAnaDip.DIP_COD_FIS;
	  lAnagrafico.PARTITA_IVA 			 := NULL;
	  lAnagrafico.ID_FISCALE_ESTERO 		 := NULL;
	  lAnagrafico.RAGIONE_SOCIALE 			 := NULL;
	  lAnagrafico.NOME 				 := SUBSTR(aAnaDip.NOMINATIVO, (aAnaDip.NCAR_COGN + 1), (LENGTH(aAnaDip.NOMINATIVO) - aAnaDip.NCAR_COGN + 1));
	  lAnagrafico.COGNOME 				 := SUBSTR(aAnaDip.NOMINATIVO,1,(aAnaDip.NCAR_COGN - 1));
	  lAnagrafico.TI_SESSO 				 := aAnaDip.sesso;
	  lAnagrafico.DT_NASCITA 			 := aAnaDip.data_nascita;
	  lAnagrafico.PG_COMUNE_NASCITA 		 := lPgComuneNascita;
	  lAnagrafico.VIA_FISCALE 			 := aAnaDip.ind_resid;
	  --lAnagrafico.NUM_CIVICO_FISCALE 		 := NULL;
	  lAnagrafico.PG_COMUNE_FISCALE 		 := lPgComuneFiscale;
	  lAnagrafico.CAP_COMUNE_FISCALE 		 := aAnaDip.cap_resid ;
	  lAnagrafico.FRAZIONE_FISCALE 			 := NULL;
	  lAnagrafico.PG_NAZIONE_FISCALE 		 := lPgNazioneFiscale;
	  lAnagrafico.PG_NAZIONE_NAZIONALITA 	         := lPgNazionalita;
	  lAnagrafico.FL_FATTURAZIONE_DIFFERITA          := 'N';
	  lAnagrafico.FL_OCCASIONALE 			 := 'N';
	  lAnagrafico.TI_ITALIANO_ESTERO 		 := lItaliano;
 	  lAnagrafico.CD_ATTIVITA_INPS 			 := NULL;
 	  lAnagrafico.ALTRA_ASS_PREVID_INPS 	         := NULL;
 	  lAnagrafico.ALIQUOTA_FISCALE 			 := NULL;
 	  lAnagrafico.CODICE_FISCALE_CAF 		 := NULL;
 	  lAnagrafico.DENOMINAZIONE_CAF 		 := NULL;
 	  lAnagrafico.SEDE_INAIL 			 := NULL;
 	  lAnagrafico.MATRICOLA_INAIL 			 := NULL;
 	  lAnagrafico.CONTO_NUMERARIO_CREDITO 	         := NULL; -- Non Usare
 	  lAnagrafico.CONTO_NUMERARIO_DEBITO 		 := NULL; -- Non Usare
 	  lAnagrafico.NUM_ISCRIZ_CCIAA 			 := NULL;
 	  lAnagrafico.NUM_ISCRIZ_ALBO 			 := NULL;
-- 	  lAnagrafico.DT_FINE_RAPPORTO 			 := aAnaDip.data_cessazione;
-- 	  lAnagrafico.CAUSALE_FINE_RAPPORTO 	         := aAnaDip.desc_causa_Cess;
 	  lAnagrafico.DT_CANC 				 := NULL;
 	  lAnagrafico.DT_ANTIMAFIA 			 := NULL;
 	  lAnagrafico.CD_ENTE_APPARTENENZA 		 := NULL;
 	  lAnagrafico.NOTE 				 := NULL;
 	  lAnagrafico.DACR 				 := aData;
 	  lAnagrafico.UTCR 				 := aUtente;
 	  lAnagrafico.DUVA 				 := aData;
 	  lAnagrafico.UTUV 				 := aUtente;
 	  lAnagrafico.PG_VER_REC 			 := 1;

	  INS_ANAGRAFICO(lAnagrafico);

	  -- Aggiornamento del numeratore dell'anagrafico
	  update numerazione_base set cd_corrente = cd_corrente +1  where tabella ='ANAGRAFICO';

	  -- Creazione del rapporto
	  CREARAPPORTO (aAnaDip,lAnagrafico, aRapporto, aRapportoPrec, aData, aUtente);

	  -- Creazione Inquadramento
	  CREAINQUADRAMENTO (aAnaDip,lAnagrafico, aRapporto, aRapportoPrec, aData, aUtente);

	  -- creazione terzo associato all'anagrafico
	  CREATERZO(aAnaDip, lAnagrafico, aData, aUtente );

	  -- creazione del montante
	  CREAMONTANTE(aAnaDip, lAnagrafico , aData , aUtente );
 end;



 procedure CREATERZO(aAnaDip cnr_anadip%rowtype,aAnagrafico anagrafico%rowtype, aData Date, aUtente VARCHAR2) is
 lTerzo terzo%rowtype;
 lPgTerzo Number;
 Begin
 --Dbms_Output.put_line('CREATERZO'||' matricola '||aAnaDip.matricola);
 	  -- Recuperiamo il progressivo per il terzo
 	  begin
		  select cd_corrente + 1
		  into lPgTerzo
		  from numerazione_base
		  where tabella ='TERZO'
		  for update nowait;
	  exception when no_data_found then
	  			ibmerr001.RAISE_ERR_GENERICO('Manca la configurazione della tabella TERZO');
	  end;


	  lTerzo.CD_TERZO                 := lPgTerzo;
	  lTerzo.FRAZIONE_SEDE  		  := NULL;
	  lTerzo.CD_ANAG 	   			  := aAnagrafico.cd_anag;
	  lTerzo.DT_FINE_RAPPORTO   	  := aAnagrafico.dt_fine_rapporto;
	  lTerzo.TI_TERZO		    	  := 'E';
	  lTerzo.CD_PRECEDENTE	    	  := aAnaDip.matricola;
	  lTerzo.DENOMINAZIONE_SEDE 	  := aAnaDip.nominativo;
	  lTerzo.VIA_SEDE				  := aAnaDip.ind_resid;
	  --lTerzo.NUMERO_CIVICO_SEDE 	  := NULL;
	  lTerzo.PG_COMUNE_SEDE     	  := aAnagrafico.pg_comune_fiscale;
	  lTerzo.CAP_COMUNE_SEDE 		  := aAnagrafico.cap_comune_fiscale;
	  lTerzo.PG_RAPP_LEGALE			  := NULL;
	  lTerzo.CD_UNITA_ORGANIZZATIVA   := NULL;
	  lTerzo.NOME_UNITA_ORGANIZZATIVA := NULL;
	  lTerzo.NOTE 					  := NULL;
	  lTerzo.DT_CANC 				  := NULL;
	  lTerzo.DACR 					  := aData;
	  lTerzo.UTCR 					  := aUtente;
	  lTerzo.DUVA 					  := aData;
	  lTerzo.UTUV 					  := aUtente;
	  lTerzo.PG_VER_REC 			  := 1;

	  INS_TERZO(lTerzo);

	  update numerazione_base set cd_Corrente = cd_Corrente +1 where tabella ='TERZO';

	  -- Creazione della banca
	  CREABANCA(aAnaDip ,aAnagrafico ,lTerzo , aData , aUtente );

	  -- creazione della modalita pagamento
	  CREAMODPAG(aAnaDip, lTerzo, aData , aUtente);
 end;

procedure CREABANCA(aAnaDip cnr_anadip%rowtype,aAnagrafico anagrafico%rowtype,aTerzo terzo%rowtype, aData Date, aUtente VARCHAR2) is
 lNumBanche number;
 lInsBanca boolean;
 lBanca banca%rowtype;
 lRifPagamento rif_modalita_pagamento%rowtype;
 lCdModPagamento rif_modalita_pagamento.CD_MODALITA_PAG%type;

 begin
--Dbms_Output.put_line('CREABANCA'||' matricola '||aAnaDip.matricola);
 	  -- verifichiamo che abi e cab della tabella cnr anadip siano validi
 	  select count(*)
	  into lNumBanche
	  from abicab
	  where abi = aAnaDip.abi_pag
	  and   cab = aAnaDip.cab_pag;

	  -- decodifichiamo la modalita di pagamento
	  begin
		 select cd_modalita_pag_cir
		 into lCdModPagamento
		 from cnr_cir_rif_mod_pag
		 where cd_modalita_pag_cnr = Rtrim(aAnaDip.mod_pag)
		 for update nowait;

	  exception when no_data_found then
		 ibmutl200.logInf(gPgLog ,'Il terzo con cd_precedente = ' || aAnaDip.matricola , ' Risulta non avere una decodifica per la modalita di pagamento. In CNR_CIR_RIF_MOD_PAG non esiste cd_modalita_pag = ' || aAnaDip.mod_pag , '');

		 select cd_modalita_pag_cir
		 into lCdModPagamento
		 from cnr_cir_rif_mod_pag
		 where cd_modalita_pag_cnr = Nvl(Rtrim(aAnaDip.mod_pag),1)
		 for update nowait;
	  end;

  	  -- recuperiamo la modalita pagamento
	  select *
	  into lRifPagamento
	  from rif_modalita_pagamento
	  where cd_modalita_pag = lCdModPagamento;

  	  -- recuperiamo il progreassivo della banca
  	  select nvl(max(pg_banca),0) + 1
	  into lBanca.pg_banca
	  from banca
	  where cd_terzo = aTerzo.cd_terzo;

	  -- lNumBanche >0 implica che abi e cab sono validi e che quindi la banca deve
	  -- essere inserita
	  if lNumBanche > 0 then
	  	 lInsBanca := true;
	  else
	  	 lInsBanca := false;
	  end if;

	  if lInsBanca then
		 lBanca.CD_TERZO  		:= aTerzo.cd_terzo;
		 lBanca.CAB				:= aAnaDip.cab_pag;
		 lBanca.ABI				:= aAnaDip.abi_pag;
		 lBanca.TI_PAGAMENTO	:= lRifPagamento.ti_pagamento;
		 lBanca.INTESTAZIONE	:= aAnaDip.NOMINATIVO;
		 lBanca.QUIETANZA		:= NULL;
		 -- 23/03/2016
		 if(aAnaDip.IBAN_PAG is not null and aAnaDip.IBAN_PAG like 'IT'||'%') then
		 		lBanca.NUMERO_CONTO	:= substr(aAnaDip.IBAN_PAG,16,12);
		 else
		 	  lBanca.NUMERO_CONTO	:= lpad(aAnaDip.NRC_PAG,12,'0');
		 end if;
		 lBanca.CODICE_IBAN		:= aAnaDip.IBAN_PAG;
		 lBanca.CODICE_SWIFT	:= NUll;
		 lBanca.DACR			:= aData;
		 lBanca.UTCR			:= aUtente;
		 lBanca.DUVA			:= aData;
		 lBanca.UTUV			:= aUtente;
		 lBanca.PG_VER_REC		:= 1;
		 lBanca.FL_CANCELLATO	:= 'N';
		 lBanca.CD_TERZO_DELEGATO := NULL;
		 lBanca.PG_BANCA_DELEGATO := NULL;
		 lBanca.ORIGINE := 'S';
		 lBanca.FL_CC_CDS := 'N';
		 lBanca.CIN :=aAnaDip.CIN_PAG;
		 -- inseriamo la banca
		 INS_BANCA(lBanca);
	  else
	  	 -- nel caso in cui l'abi e il cab non risultano validi
		 -- controlliamo se la modalita di pagamento risulta essere di tipo ALTRO = 'A'
		 -- e che abi e cab siano null, in questo caso la banca deve essere inserita
		 if lRifPagamento.ti_pagamento ='A'
		 and aAnaDip.abi_pag is null
		 and aAnaDip.cab_pag is null
		 then
			 lBanca.CD_TERZO  		:= aTerzo.cd_terzo;
			 lBanca.CAB				:= null;
			 lBanca.ABI				:= null;
			 lBanca.TI_PAGAMENTO	:= lRifPagamento.ti_pagamento;
			 lBanca.INTESTAZIONE	:= aAnaDip.NOMINATIVO;
			 lBanca.QUIETANZA		:= NULL;
			 lBanca.NUMERO_CONTO	:= null;-- ?? Chiedere
			 lBanca.CODICE_IBAN		:= NULL;
			 lBanca.CODICE_SWIFT	:= NUll;
			 lBanca.DACR			:= aData;
			 lBanca.UTCR			:= aUtente;
			 lBanca.DUVA			:= aData;
			 lBanca.UTUV			:= aUtente;
			 lBanca.PG_VER_REC		:= 1;
			 lBanca.FL_CANCELLATO	:= 'N';
			 lBanca.CD_TERZO_DELEGATO := NULL;
			 lBanca.PG_BANCA_DELEGATO := NULL;
			 lBanca.ORIGINE := 'S';
			 lBanca.FL_CC_CDS := 'N';
			 lBanca.CIN := null;
			 -- inseriamo la banca
			 INS_BANCA(lBanca);
		 else
		  	 ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , ' Abi Cab non validi', '');
		 end if;

	  end if;

 end;

 procedure CREAMODPAG(aAnaDip cnr_anadip%rowtype, aTerzo terzo%rowtype, aData date , aUtente varchar2) is
 lRifPagamento rif_modalita_pagamento%rowtype;
 lInsPagamento boolean := true;
 lCdModPagamento rif_modalita_pagamento.CD_MODALITA_PAG%type;
 lModPagamento modalita_pagamento%rowtype;
 begin
--Dbms_Output.put_line('CREAMODPAG'||' matricola '||aAnaDip.matricola);
	  -- decodifichiamo la modalita di pagamento
	  begin
		 select cd_modalita_pag_cir
		 into lCdModPagamento
		 from cnr_cir_rif_mod_pag
		 where cd_modalita_pag_cnr = Rtrim(aAnaDip.mod_pag)
		 for update nowait;

	  exception when no_data_found then
		 ibmutl200.logInf(gPgLog ,'Il terzo con matricola = ' || aAnaDip.matricola , ' Risulta non avere una decodifica per la modalita di pagamento. In CNR_CIR_RIF_MOD_PAG non esiste cd_modalita_pag = ' || aAnaDip.mod_pag , '');
		 select cd_modalita_pag_cir
		 into lCdModPagamento
		 from cnr_cir_rif_mod_pag
		 where cd_modalita_pag_cnr = Nvl(Rtrim(aAnaDip.mod_pag),1)
		 for update nowait;
	  end;
 	  begin
		  -- recuperiamo la rifPagamento
		  select *
		  into lRifPagamento
		  from rif_modalita_pagamento
		  where cd_modalita_pag= lCdModPagamento;
 	  exception when no_data_found then
		  lInsPagamento := false;
		  ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , ' non trovato in RIF_MODALITA_PAGAMENTO il cd_modalita_pag '||aAnaDip.mod_pag, '');
 	  end;
	  if lInsPagamento then
	  	 lModPagamento.CD_TERZO := aTerzo.cd_terzo;
	  	 lModPagamento.CD_MODALITA_PAG := lRifPagamento.cd_modalita_pag;
	  	 lModPagamento.CD_TERZO_DELEGATO := null;
	  	 lModPagamento.DACR := aData;
	  	 lModPagamento.UTCR := aUtente;
	  	 lModPagamento.DUVA := aData;
	  	 lModPagamento.UTUV := aUtente;
	  	 lModPagamento.PG_VER_REC := 1;
		 INS_MOD_PAGAMENTO(lModPagamento, aAnadip);
	  else
		  ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , ' non trovato in RIF_MODALITA_PAGAMENTO il cd_modalita_pag '||aAnaDip.mod_pag, '');
	  end if;
 end;


 procedure CREAINQUADRAMENTO (aAnaDip cnr_Anadip%rowtype
 		   					 ,aAnagrafico anagrafico%rowtype
							 ,aRapporto  rapporto%rowtype
							 ,aRapportoPrec rapporto%rowtype
							 ,aData date
							 ,aUtente varchar2) is
 lCdTipoRapporto rapporto.cd_tipo_rapporto%type;
 lPgRifInquadramento number;
 lPgRifInquadramentoOld number;
 lContinuaEsecuzione boolean := true;
 lInquadPrec boolean := true;
 lDataInizioInqPrec date;
 lInquadramento inquadramento%rowtype;
 lInquadramentoPrec inquadramento%rowtype;
 begin
--Dbms_Output.put_line('CREAINQUADRAMENTO'||' matricola '||aAnaDip.matricola);
	  lCdTipoRapporto := cTIPO_RAPPORTO;

	  begin
	 	  select pg_rif_inquadramento
		  into lPgRifInquadramento
	 	  from rif_inquadramento
	 	  where CD_PROFILO =  aAnaDip.livello_1
		  and   CD_PROGRESSIONE = aAnaDip.livello_2
		  and   cd_livello is null;
	  exception when no_data_found then
	  			lContinuaEsecuzione := false;
			  	ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Non Inserito Inquadramento', '');
	  end;

	  if lContinuaEsecuzione then
	  	 -- questo caso lContinuaEsecuzione = true implica che l'inquadramento attuale
		 -- è valido
	 	 lInquadramento.CD_TIPO_RAPPORTO         := lCdTipoRapporto;
		 lInquadramento.CD_ANAG                  := aAnagrafico.cd_anag;
		 lInquadramento.DT_INI_VALIDITA_RAPPORTO := aRapporto.dt_ini_validita;
		 lInquadramento.PG_RIF_INQUADRAMENTO     := lPgRifInquadramento;
		 lInquadramento.DT_INI_VALIDITA	  		 := aAnadip.giu_dec_inizio;
		 lInquadramento.DT_FIN_VALIDITA	   		 := nvl(aAnaDip.data_cessazione,to_date('31122200','ddmmyyyy'));
		 lInquadramento.DACR			  		 := aData;
		 lInquadramento.UTCR			  		 := aUtente;
		 lInquadramento.DUVA			  		 := aData;
		 lInquadramento.UTUV			  		 := aUtente;
		 lInquadramento.PG_VER_REC		  		 := 1;
		 -- Inseriamo inquadramento attuale

		 INS_INQUADRAMENTO(lInquadramento);
		 if ( trunc(aAnadip.data_assunzione) > trunc(aAnadip.profilo_prec_fine) ) then
					 ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'La data di assunzione è maggiore della data fine profilo precedente', '');
		 end if;


	  end if; --lContinuaEsecuzione

 end;

procedure CREARAPPORTO (aAnaDip cnr_Anadip%rowtype
 		   				 ,aAnagrafico anagrafico%rowtype
						 ,aRapporto in out rapporto%rowtype
						 ,aRapportoPrec in out rapporto%rowtype
						 ,aData date
						 ,aUtente varchar2) is
 lCdTipoRapporto rapporto.cd_tipo_rapporto%type;
 begin
--Dbms_Output.put_line('CREARAPPORTO'||' matricola '||aAnaDip.matricola);
	  lCdTipoRapporto := cTIPO_RAPPORTO;

		  aRapporto.CD_TIPO_RAPPORTO      :=  lCdTipoRapporto;
		  aRapporto.CD_ANAG  		 	  := aAnagrafico.cd_anag;
		  aRapporto.DT_INI_VALIDITA  	  := aAnaDip.data_assunzione;
		  aRapporto.MATRICOLA_DIPENDENTE  := aAnadip.matricola;
		  aRapporto.CAUSALE_FINE_RAPPORTO := aAnaDip.desc_causa_cess;
		  aRapporto.DT_FIN_VALIDITA		  := nvl(aAnaDip.data_cessazione,to_date('31122200','ddmmyyyy'));
		  aRapporto.DACR				  := aData;
		  aRapporto.UTCR				  := aUtente;
		  aRapporto.DUVA				  := aData;
		  aRapporto.UTUV				  := aUtente;
		  aRapporto.PG_VER_REC			  := 1;
		  aRapporto.CD_ENTE_PREV_STI := aAnaDip.ente_prev;	-- flavia
                  aRapporto.CD_RAPP_IMPIEGO_STI  := aAnaDip.RAPP_IMPIEGO;
		  INS_RAPPORTO (aRapporto);
 End;

 procedure CREAMONTANTE(aAnaDip cnr_Anadip%rowtype, aAnagrafico anagrafico%rowtype, aData date, aUtente varchar2 ) as
 lMontante montanti%rowtype;
 lImpFiscaleNetto number(16,3);
 lImpFiscaleLordo number(16,3);
 lImpPrevidenziale number(16,3);


 lMeseRiferimento number(2);
 lEsisteMontante boolean;
 lEntePrevValido boolean;
-- lVMontStip v_stipendi_montanti%rowtype;
 lAggiornaMontante boolean;
 lMesiAssenza number(2):=0;

 begin
 	--Dbms_Output.put_line('CREAMONTANTE---------------------------');
--Dbms_Output.put_line('CREAMONTANTE'||' matricola '||aAnaDip.matricola||'- cd_anag '||aAnagrafico.cd_anag);
 	  lImpFiscaleNetto := nvl(aAnaDip.IMP_FISC,0);
	  lImpFiscaleLordo := nvl(aAnaDip.IMP_IMPO1,0);
	  lMeseRiferimento := nvl(aAnaDip.mese_rif,0);
	  lImpPrevidenziale := nvl(aAnaDip.IMP_PREV,0);

		begin
			select count(*) into lMesiAssenza
			from cnr_anadip
			where matricola = aAnaDip.matricola
			and assenza is not null
			and nvl(effetto,0) = 0
			and mese_rif != 0
			and anno_rif = aAnaDip.anno_rif;

		exception when no_data_found then
			lMesiAssenza := 0;
		end;

	  if lMeseRiferimento <= 0 or lMeseRiferimento > 12 then
	  	 ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Impossibile definire il montante il mese di riferimento risulta ' || to_char(aAnaDip.mese_rif), '');
	  else
	  	  begin
		  	   -- verifichiamo che l'informazione dell'ente previdenziale su cnr_anadip sia corretta
						  if (aAnaDip.ENTE_PREV <> cINPS
						  	  and aAnaDip.ENTE_PREV <> cINPGI
						  	  and aAnaDip.ENTE_PREV <> cFONDOFS
						  	  and aAnaDip.ENTE_PREV <> cCPDEL
						  	  and aAnaDip.ENTE_PREV <> cENPDEP
						  	  and aAnaDip.ENTE_PREV <> cCPS)
						  then
							  ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Impossibile definire il montante, ente previdenziale non valido' || aAnaDip.ENTE_PREV, '');
							  lEntePrevValido := false;
						  else
						  	  lEntePrevValido := true;

						  end if;

		  	 end;

		  if lEntePrevValido then
		 	 	  -- Verifichiamo che esista il montante per l'anagrafico in esame

-- Per la Lordizzazione delle missioni prendiamo solo i dipendente che hanno lo stipendio.
-- Non prendiamo quelli che hanno stipendio a zero.

							if( aAnaDip.assenza is not null and nvl(aAnaDip.effetto,0) = 0)
								then
									lAggiornaMontante := false;
								else
									lAggiornaMontante := true;
							end if;

							if lAggiornaMontante then

					begin

						  select *
						  into lMontante
						  from montanti
						  where esercizio = aAnaDip.anno_rif
						  and  cd_anag = aAnagrafico.cd_anag
						  for update nowait;


					  -- Esiste il Montante
					  	lEsisteMontante := true;
			 	  		exception when no_data_found then
				  			-- non esiste il montante
					  		lEsisteMontante := false;
		 	     end;
		  	  -- siccome gli importi di Dicembre(12) arrivano cumulati (stipendio + tredicesima)
			  -- allora per avere una distribuzione scalare INPS, IRPEF più precisa divideremo
			  -- gli impori per 13 piuttosto che per 12
		  	  if lMeseRiferimento = 12 then
			  	 lMeseRiferimento := 13;
			  	end if;

			  if not lEsisteMontante then

			 	  -- Costruiamo la struttura del montante
				  lMontante.ESERCIZIO	 	 		   := aAnaDip.anno_rif;
				  lMontante.CD_ANAG					   := aAnagrafico.cd_anag;
			  	  lMontante.IRPEF_DIPENDENTI :=  (lImpFiscaleNetto * 13 / (lMeseRiferimento-lMesiAssenza));
				  lMontante.IRPEF_ALTRI := 0;
				  lMontante.INPS_DIPENDENTI := 0;
				  lMontante.INPS_TESORO_DIPENDENTI := 0;
				  lMontante.INPS_ALTRI := 0;
				  lMontante.FONDO_FS_DIPENDENTI := 0;


				  if (aAnaDip.ENTE_PREV = cINPS)
				  	 or (aAnaDip.ENTE_PREV = cINPGI) then
				  	 lMontante.INPS_DIPENDENTI := (lImpPrevidenziale * 13 /(lMeseRiferimento-lMesiAssenza));
				  end if;
				  if aAnaDip.ENTE_PREV = cFONDOFS  then
					 lMontante.FONDO_FS_DIPENDENTI := (lImpPrevidenziale * 13 /(lMeseRiferimento-lMesiAssenza));
				  end if;
				  if (aAnaDip.ENTE_PREV = cCPDEL)
				  	 or (aAnaDip.ENTE_PREV = cENPDEP)
				  	 or (aAnaDip.ENTE_PREV = cCPS) then
					 lMontante.INPS_TESORO_DIPENDENTI := (lImpPrevidenziale * 13 /(lMeseRiferimento-lMesiAssenza));
				  end if;

				  if aAnadip.IMP_IMPO1 > 0 then
				  	 lMontante.IRPEF_LORDO_DIPENDENTI	   := (lImpFiscaleLordo * 13 / (lMeseRiferimento-lMesiAssenza));
				  else
				  	 lMontante.IRPEF_LORDO_DIPENDENTI	   := (lImpFiscaleNetto * 13 / (lMeseRiferimento-lMesiAssenza));
				  end if;
				  lMontante.IRPEF_LORDO_ALTRI		   := 0;
				  lMontante.DEDUZIONE_IRPEF_DIPENDENTI := lMontante.IRPEF_LORDO_DIPENDENTI - lMontante.IRPEF_DIPENDENTI;
				  lMontante.DEDUZIONE_IRPEF_ALTRI	   := 0;
				  lMontante.RIDUZ_DIPENDENTI		   := 0;
				  lMontante.RIDUZ_ALTRI			   := 0;
				  lMontante.INAIL_ALTRI			   := 0;
				  lMontante.INPS_OCCASIONALI		   := 0;
				  lMontante.DEDUZIONE_FAMILY_ALTRI	   := 0;
                  lMontante.INPGI_ALTRI	   := 0;
				  lMontante.DACR			   := aData;
				  lMontante.UTCR			   := aUtente;
				  lMontante.DUVA			   := aData;
				  lMontante.UTUV			   := aUtente;
				  lMontante.PG_VER_REC			   := 1;
 					lMontante.ENPAPI_ALTRI	   := 0;

				  INS_MONTANTE(lMontante);
			  else

			 	  -- Costruiamo la struttura del montante
	 			  -- lMontante.ESERCIZIO	 	 		   := aAnaDip.anno_rif;
	 			  -- lMontante.CD_ANAG					   := aAnagrafico.cd_anag;

				  lMontante.IRPEF_DIPENDENTI :=  (lImpFiscaleNetto * 13 / (lMeseRiferimento-lMesiAssenza)) ;

				  if (aAnaDip.ENTE_PREV = cINPS)
				  	 or (aAnaDip.ENTE_PREV = cINPGI) then
				  	 lMontante.INPS_DIPENDENTI := (aAnaDip.IMP_PREV * 13 /(lMeseRiferimento-lMesiAssenza)) ;
				  end if;
				  if aAnaDip.ENTE_PREV = cFONDOFS  then
					 lMontante.FONDO_FS_DIPENDENTI := (aAnaDip.IMP_PREV * 13 /(lMeseRiferimento-lMesiAssenza)) ;
				  end if;
				  if (aAnaDip.ENTE_PREV = cCPDEL)
				  	 or (aAnaDip.ENTE_PREV = cENPDEP)
				  	 or (aAnaDip.ENTE_PREV = cCPS) then
					 lMontante.INPS_TESORO_DIPENDENTI := (aAnaDip.IMP_PREV * 13 /(lMeseRiferimento-lMesiAssenza)) ;
				  end if;
				  if aAnaDip.IMP_IMPO1 > 0 then
				  	 lMontante.IRPEF_LORDO_DIPENDENTI	   := (aAnaDip.IMP_IMPO1 * 13 / (lMeseRiferimento-lMesiAssenza)) ;
				  else
				  	 lMontante.IRPEF_LORDO_DIPENDENTI	   := (aAnaDip.IMP_FISC * 13 / (lMeseRiferimento-lMesiAssenza)) ;
				  end if;
				  lMontante.DEDUZIONE_IRPEF_DIPENDENTI := lMontante.IRPEF_LORDO_DIPENDENTI - lMontante.IRPEF_DIPENDENTI;
				  lMontante.DUVA					   := aData;
				  lMontante.UTUV					   := aUtente;
				  lMontante.PG_VER_REC				   := lMontante.PG_VER_REC + 1;


				  UPD_MONTANTE(lMontante);
			  end if; -- lEsisteMontante
		  end if; -- lEntePrevValido
		  		end if; -- lAggiornaMontante
	  end if; -- lMeseRiferimento <= 0 or lMeseRiferimento > 12
 end;


procedure ALLINEA_INQUADRAMENTO(aAnaDip cnr_anadip%rowtype,
 		   					aAnagrafico anagrafico%rowtype,
							aData date,
							aUtente varchar2) is
 lRapporto rapporto%rowtype;
 lInquadramento inquadramento%rowtype;
 lInquadramentoNew inquadramento%rowtype;
 lInquadramentoOld inquadramento%rowtype;
 lDtIniziInquadramento date;
 lPgRifInquadramento number(10);
 lPgRifInquadramentoOld number(10);
 lEsisteRapporto boolean;
 aMeseRif_prec number(2);
 aAnnoRif_prec number(4);
 v_contratto_old varchar2(7);
 v_livprec_1 	varchar2(3);
 v_livprec_2	varchar2(3);



 begin


 	  -- Selezioniamo l'ultimo rapporto in ordine cronologico
	  -- che intendiamo essere il rapporto attualmente in corso
	  begin
	 	  select * into lRapporto
		  from rapporto
		  where CD_TIPO_RAPPORTO = cTIPO_RAPPORTO
		  and 	CD_ANAG 		 = aAnagrafico.cd_anag
		  and 	DT_INI_VALIDITA  = (SELECT max(DT_INI_VALIDITA) from rapporto
		  						    	where CD_TIPO_RAPPORTO = cTIPO_RAPPORTO
	  						   		and   CD_ANAG 	       = aAnagrafico.cd_anag);

	   	  lEsisteRapporto := True;

	  exception
	  when no_data_found then
	  	   ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola,'Impossibile allineare rapporto, Non esiste nessun rapporto per l''anagrafico ' || aAnagrafico.cd_anag, '');
		   lEsisteRapporto := False;

	  end;

--29/05/2006 un rapporto viene allineato solo quando la data cessazione della tabella cnr_anadip è valorizzata
	-- altrimenti se è valorizzata solo profilo_prec_fine deve essere allineato solo l'inquadramento


	  if lEsisteRapporto  then

		  begin

 	if aAnaDip.mese_rif = 1 then
 		aMeseRif_prec := 12;
 		aAnnoRif_prec := aAnaDip.anno_rif-1;
 	else
 		aMeseRif_prec := aAnaDip.mese_rif-1;
 		aAnnoRif_prec := aAnaDip.anno_rif;
 	end if;


 	begin
			select livello_1, livello_2 into v_livprec_1, v_livprec_2
			from cnr_anadip
			where anno_rif  = aAnnoRif_prec
			and mese_rif = aMeseRif_prec
			and matricola = aAnaDip.matricola;
	exception
		when no_data_found then
			if aAnaDip.livprec_1 is not null and  aAnaDip.livprec_2 is not null then

				select livprec_1, livprec_2 into v_livprec_1, v_livprec_2
				from cnr_anadip
				where anno_rif  = aAnaDip.anno_rif
				and mese_rif = aAnaDip.mese_rif
				and matricola = aAnaDip.matricola;
			else
				ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola,'Impossibile allineare l''inquadramento, non esiste l''inquadramento precedente per l''anagrafico ' || aAnagrafico.cd_anag, '');
				--dbms_output.put_line('Impossibile allineare l''inquadramento, non esiste l''inquadramento precedente per l''anagrafico ' || aAnagrafico.cd_anag);
			end if;

	end;

        If v_livprec_1 Is Not Null And v_livprec_2  Is Not Null Then
		  	  select pg_rif_inquadramento
			  into lPgRifInquadramentoOld
		 	  from rif_inquadramento
		 	  where CD_PROFILO =  v_livprec_1
			  and   CD_PROGRESSIONE = v_livprec_2
			  and   cd_livello is null;
        End If;

			  select pg_rif_inquadramento
			  into lPgRifInquadramento
		 	  from rif_inquadramento
		 	  where CD_PROFILO =  aAnaDip.livello_1
			  and   CD_PROGRESSIONE = aAnaDip.livello_2
			  and   cd_livello is null;


			  -- Se esiste il rapporto ma è cambiato il pg_rif_inquadramento deve essere inserito un nuovo record
			  -- con il pg_rif_inquadramento nuovo e chiuso il precedente
			  if lPgRifInquadramentoOld != lPgRifInquadramento and lPgRifInquadramentoOld is not null then

			  	select * into lInquadramentoOld
			  	from inquadramento
			  	where pg_rif_inquadramento = lPgRifInquadramentoOld
			  	and cd_anag = aAnagrafico.cd_anag
			  	and dt_fin_validita = (SELECT max(DT_FIN_VALIDITA) from inquadramento
		  						    	where CD_TIPO_RAPPORTO = cTIPO_RAPPORTO
	  						   		and   CD_ANAG 	       = aAnagrafico.cd_anag
	  						   		and pg_rif_inquadramento = lPgRifInquadramentoOld
	  						   		and dt_ini_validita_rapporto = lRapporto.DT_INI_VALIDITA);

			  	update inquadramento
				set dt_fin_validita = aAnadip.giu_dec_inizio-1,
				 	utuv	  = aUtente,
				 	duva 	  = aData,
				  	pg_ver_rec = pg_ver_rec + 1
			      	  where CD_TIPO_RAPPORTO = cTIPO_RAPPORTO
			  	  and 	CD_ANAG 		 = lInquadramentoOld.cd_anag
			  	  and 	DT_INI_VALIDITA  = lInquadramentoOld.dt_ini_validita
			  	  and   dt_ini_validita_rapporto = lInquadramentoOld.dt_ini_validita_rapporto
				  and   PG_RIF_INQUADRAMENTO = lPgRifInquadramentoOld;

			  	lInquadramentoNew.CD_TIPO_RAPPORTO         := cTIPO_RAPPORTO;
				lInquadramentoNew.CD_ANAG                  := aAnagrafico.cd_anag;
				lInquadramentoNew.DT_INI_VALIDITA_RAPPORTO := lRapporto.dt_ini_validita;
				lInquadramentoNew.PG_RIF_INQUADRAMENTO     := lPgRifInquadramento;
				lInquadramentoNew.DT_INI_VALIDITA	   := aAnadip.giu_dec_inizio;
				lInquadramentoNew.DT_FIN_VALIDITA	   := nvl(aAnadip.data_cessazione,to_date('31/12/2200','dd/mm/yyyy'));
				lInquadramentoNew.DACR			   := aData;
				lInquadramentoNew.UTCR			   := aUtente;
				lInquadramentoNew.DUVA			   := aData;
				lInquadramentoNew.UTUV			   := aUtente;
				lInquadramentoNew.PG_VER_REC		   := 1;

				INS_INQUADRAMENTO(lInquadramentoNew);

			  end if;


				-- Selezionaniamo l'ultimo inquadramento valido che corrisponde al rapporto esistente
			 begin
				select * into lInquadramento
		  		from inquadramento
		  		where cd_anag = aAnagrafico.cd_anag
		  		and cd_tipo_rapporto = cTIPO_RAPPORTO
		  		and pg_rif_inquadramento = lPgRifInquadramento
		  		and dt_ini_validita_rapporto = lRapporto.DT_INI_VALIDITA
		  		and dt_ini_validita = (SELECT max(DT_INI_VALIDITA) from inquadramento
		  						    	where CD_TIPO_RAPPORTO = cTIPO_RAPPORTO
	  						   		and   CD_ANAG 	       = aAnagrafico.cd_anag
	  						   		and pg_rif_inquadramento = lPgRifInquadramento
	  						   		and dt_ini_validita_rapporto = lRapporto.DT_INI_VALIDITA);


			  	-- Se la data di fine validita del rapporto è diversa dalla data di fine validita
			  	-- deve essere aggiornata la data dell'inquadramento
			  	if  lRapporto.DT_FIN_VALIDITA != lInquadramento.DT_FIN_VALIDITA
			  	and lRapporto.DT_INI_VALIDITA = lInquadramento.dt_ini_validita_rapporto then


		  		update inquadramento
				  set 	 dt_fin_validita = lRapporto.DT_FIN_VALIDITA,
				  		 utuv			  = aUtente,
				  		 duva 			  = aData,
				  		 pg_ver_rec 	  = pg_ver_rec + 1
			      	  where CD_TIPO_RAPPORTO = cTIPO_RAPPORTO
			  	  and 	CD_ANAG 		 = lInquadramento.cd_anag
			  	  and 	DT_INI_VALIDITA  = lInquadramento.dt_ini_validita
			  	  and   dt_ini_validita_rapporto = lInquadramento.dt_ini_validita_rapporto
				  and   PG_RIF_INQUADRAMENTO = lPgRifInquadramento;

				end if;
                 Exception When No_Data_Found Then
                       Null;
                 End;
/*	if aAnaDip.mese_rif = 1 then
 		aMeseRif_prec := 12;
 		aAnnoRif_prec := aAnaDip.anno_rif-1;
 	else
 		aMeseRif_prec := aAnaDip.mese_rif-1;
 		aAnnoRif_prec := aAnaDip.anno_rif;
 	end if;
 		begin
 			select contratto into v_contratto_old
			from cnr_anadip
			where anno_rif  = aAnnoRif_prec
			and mese_rif = aMeseRif_prec
			and matricola = aAnaDip.matricola;
		exception
		when no_data_found then
			if aAnaDip.contratto_prec is not null then
				select contratto_prec into v_contratto_old
				from cnr_anadip
				where anno_rif  = aAnaDip.anno_rif
				and mese_rif = aAnaDip.mese_rif
				and matricola = aAnaDip.matricola;
			else
				ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola,'Impossibile allineare l''inquadramento, non esiste il contratto precedente per l''anagrafico ' || aAnagrafico.cd_anag, '');
				--dbms_output.put_line('Impossibile allineare l''inquadramento, non esiste il contratto precedente per l''anagrafico ' || aAnagrafico.cd_anag);
			end if;
		end;
			if aAnaDip.contratto != v_contratto_old And (lPgRifInquadramentoOld = lPgRifInquadramento And lPgRifInquadramentoOld Is Not Null) then
				update inquadramento
				  set 	 dt_fin_validita = aAnaDip.profilo_prec_fine,
				  		 utuv			  = aUtente,
				  		 duva 			  = aData,
				  		 pg_ver_rec 	  = pg_ver_rec + 1
			      	  where CD_TIPO_RAPPORTO = cTIPO_RAPPORTO
			  	  and 	CD_ANAG 		 = lInquadramento.cd_anag
			  	  and 	DT_INI_VALIDITA  = lInquadramento.dt_ini_validita
			  	  and   dt_ini_validita_rapporto = lRapporto.DT_INI_VALIDITA
				  and   PG_RIF_INQUADRAMENTO = lPgRifInquadramento;



				lInquadramentoNew.CD_TIPO_RAPPORTO         := cTIPO_RAPPORTO;
				lInquadramentoNew.CD_ANAG                  := aAnagrafico.cd_anag;
				lInquadramentoNew.DT_INI_VALIDITA_RAPPORTO := lRapporto.dt_ini_validita;
				lInquadramentoNew.PG_RIF_INQUADRAMENTO     := lPgRifInquadramento;
				lInquadramentoNew.DT_INI_VALIDITA	  	  := aAnadip.giu_dec_inizio;
				lInquadramentoNew.DT_FIN_VALIDITA	   	  := lRapporto.DT_FIN_VALIDITA;
				lInquadramentoNew.DACR			  		  := aData;
				lInquadramentoNew.UTCR			  		  := aUtente;
				lInquadramentoNew.DUVA			  		  := aData;
				lInquadramentoNew.UTUV			  		  := aUtente;
				lInquadramentoNew.PG_VER_REC		  		  := 1;

				INS_INQUADRAMENTO(lInquadramentoNew);
		     end If;
		      end if;
*/
		end;

	end if;
	exception
	           When Others Then
	                dbms_output.put_line('errore '||Sqlerrm||' per l''anagrafico '||aAnagrafico.cd_anag);
End;


procedure MODIFICAANAGRAFICO(aAnaDip cnr_anadip%rowtype,lAnagraficoOld in out anagrafico%rowtype, aData Date, aUtente VARCHAR2) is
 lAnagrafico anagrafico%rowtype;
 aRapporto rapporto%rowtype;
 aRapportoPrec rapporto%rowtype;
 lPgAnagrafico number;

 lItaliano CHAR(1);
 lPgComuneNascita number;
 lPgComuneFiscale number;

 lPgNazioneFiscale number;
 lPgNazioneNascita number;
 lPgNazionalita number;

 begin
--Dbms_Output.put_line('MODIFICAANAGRAFICO'||' matricola '||aAnaDip.matricola);

 	  ibmutl200.logInf(gPgLog ,'Modifica anagrafico matricola' || aAnaDip.matricola , '' , '');
      if substr(aAnaDip.cod_comune,1,1)= 'Z' then
	  	 lItaliano := 'E';
	  else
	  	 lItaliano := 'I';
	  end if;

	  lPgComuneNascita  := getComuneNascita(aAnaDip,  lAnagrafico,lPgNazioneNascita);
	  lPgComuneFiscale  := getComuneFiscale(aAnaDip,  lAnagrafico,lPgNazioneFiscale);

	  begin
		  select pg_nazione
		  into lPgNazionalita
		  from nazione
		  where (cd_nazione = aAnaDip.naz_nas
		  Or  cd_iso = aAnaDip.naz_nas)
		  and rownum=1;
	  exception when no_data_found then
	  			lPgNazionalita := 0;
			  	ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Impostata Nazionalita Default (0) in MODIFICAANAGRAFICO' || ' la nazionane '|| aAnaDip.naz_nas || ' non risulta nella tabella Nazione', '');
	  end;

-- 	  lAnagrafico.CD_ANAG                    := lPgAnagrafico;
-- 	  lAnagraficoOld.CD_CLASSIFIC_ANAG 		 	 := NULL; -- ?? Chiedere
-- 	  lAnagraficoOld.TI_ENTITA 				 	 := 'F';
-- 	  lAnagraficoOld.TI_ENTITA_FISICA 			 := 'A';
-- 	  lAnagraficoOld.TI_ENTITA_GIURIDICA 		 := NULL;
-- 	  lAnagraficoOld.FL_SOGGETTO_IVA 			 := 'N';
	  lAnagraficoOld.CODICE_FISCALE 			 := aAnaDip.DIP_COD_FIS;
-- 	  lAnagraficoOld.PARTITA_IVA 				 := NULL;
-- 	  lAnagraficoOld.ID_FISCALE_ESTERO 		 	 := NULL;
-- 	  lAnagraficoOld.RAGIONE_SOCIALE 			 := NULL;
	  lAnagraficoOld.NOME 						 := SUBSTR(aAnaDip.NOMINATIVO, (aAnaDip.NCAR_COGN + 1), (LENGTH(aAnaDip.NOMINATIVO) - aAnaDip.NCAR_COGN + 1));
	  lAnagraficoOld.COGNOME 					 := SUBSTR(aAnaDip.NOMINATIVO,1,(aAnaDip.NCAR_COGN - 1));
	  lAnagraficoOld.TI_SESSO 					 := aAnaDip.sesso;
	  lAnagraficoOld.DT_NASCITA 				 := aAnaDip.data_nascita;
	  lAnagraficoOld.PG_COMUNE_NASCITA 		 	 := lPgComuneNascita;
	  lAnagraficoOld.VIA_FISCALE 				 := aAnaDip.ind_resid;
	  --lAnagraficoOld.NUM_CIVICO_FISCALE 		 := NULL;
	  lAnagraficoOld.PG_COMUNE_FISCALE 		 	 := lPgComuneFiscale;
	  lAnagraficoOld.CAP_COMUNE_FISCALE 		 := aAnaDip.cap_resid ;
--	  lAnagraficoOld.FRAZIONE_FISCALE 			 := NULL; -- ?? Chiedere
	  lAnagraficoOld.PG_NAZIONE_FISCALE 		 := lPgNazioneFiscale;
	  lAnagraficoOld.PG_NAZIONE_NAZIONALITA 	 := lPgNazionalita;
-- 	  lAnagraficoOld.FL_FATTURAZIONE_DIFFERITA   := 'N';
-- 	  lAnagraficoOld.FL_OCCASIONALE 			 := 'N';
	  lAnagraficoOld.TI_ITALIANO_ESTERO 		 := lItaliano;
--  	  lAnagraficoOld.CD_ATTIVITA_INPS 			 := NULL;
--  	  lAnagraficoOld.ALTRA_ASS_PREVID_INPS 	 	 := NULL;
--  	  lAnagraficoOld.ALIQUOTA_FISCALE 			 := NULL;
--  	  lAnagraficoOld.CODICE_FISCALE_CAF 		 := NULL; -- ?? Chiedere
--  	  lAnagraficoOld.DENOMINAZIONE_CAF 		 	 := NULL; -- ?? Chiedere
--  	  lAnagraficoOld.SEDE_INAIL 				 := NULL; -- ?? Chiedere
--  	  lAnagraficoOld.MATRICOLA_INAIL 			 := NULL; -- ?? Chiedere
--  	  lAnagraficoOld.CONTO_NUMERARIO_CREDITO 	 := NULL; -- Non Usare
--  	  lAnagraficoOld.CONTO_NUMERARIO_DEBITO 	 := NULL; -- Non Usare
--  	  lAnagraficoOld.NUM_ISCRIZ_CCIAA 			 := NULL;
--  	  lAnagraficoOld.NUM_ISCRIZ_ALBO 			 := NULL;
-- 	  	  lAnagraficoOld.DT_FINE_RAPPORTO 		     := aAnaDip.data_cessazione;
-- 	  	  lAnagraficoOld.CAUSALE_FINE_RAPPORTO 	 	 := aAnaDip.desc_causa_Cess;
--  	  lAnagraficoOld.DT_CANC 					 := NULL;
--  	  lAnagraficoOld.DT_ANTIMAFIA 				 := NULL;
--  	  lAnagraficoOld.CD_ENTE_APPARTENENZA 		 := NULL;
--  	  lAnagraficoOld.NOTE 						 := NULL;
 	  lAnagraficoOld.DUVA 						 := aData;
 	  lAnagraficoOld.UTUV 						 := aUtente;
 	  lAnagraficoOld.PG_VER_REC 				 := lAnagraficoOld.PG_VER_REC + 1;


	  UPD_ANAGRAFICO(lAnagraficoOld);

	  if aAnaDip.fl_gestione_manuale ='Y' then
	  	ALLINEA_INQUADRAMENTO(aAnaDip,lAnagraficoOld, aData, aUtente);
	  end if;
	  UPD_RAPPORTO (aAnaDip,lAnagraficoOld, aRapporto, aRapportoPrec, aData, aUtente );

	  UPD_INQUADRAMENTO (aAnaDip,lAnagraficoOld, aRapporto, aRapportoPrec, aData, aUtente );

	  MODIFICATERZO(aAnaDip, lAnagraficoOld, aData, aUtente );

	  CREAMONTANTE(aAnaDip, lAnagraficoOld, aData , aUtente );
 end;


procedure MODIFICATERZO(aAnaDip cnr_anadip%rowtype,aAnagrafico anagrafico%rowtype, aData Date, aUtente VARCHAR2) is
 lTerzo terzo%rowtype;

 begin
--Dbms_Output.put_line('MODIFICATERZO'||' matricola '||aAnaDip.matricola);
 	  -- Recuperiamo il terzo da modificare
 	  begin
		  select *
		  into lTerzo
		  from terzo
		  where cd_terzo = (select min(cd_terzo) from terzo where cd_anag = aAnagrafico.cd_anag and dt_fine_rapporto is null)
		  for update nowait;
	  exception when no_data_found then
			  	ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Manca il TERZO associato', '');
	  			ibmerr001.RAISE_ERR_GENERICO('Manca il TERZO associato');
	  end;

--	  lTerzo.CD_TERZO                 := lPgTerzo;
--	  lTerzo.FRAZIONE_SEDE  		  := NULL;
--	  lTerzo.CD_ANAG 	   			  := aAnagrafico.cd_anag;
--	  lTerzo.DT_FINE_RAPPORTO   	  := aAnagrafico.dt_fine_rapporto;
--	  lTerzo.TI_TERZO		    	  := 'E';
	  lTerzo.CD_PRECEDENTE	    	  := aAnaDip.matricola;
	  lTerzo.DENOMINAZIONE_SEDE 	  := aAnaDip.nominativo;
	  lTerzo.VIA_SEDE				  := aAnaDip.ind_resid;
	 -- lTerzo.NUMERO_CIVICO_SEDE 	  := NULL;
	  lTerzo.PG_COMUNE_SEDE     	  := aAnagrafico.pg_comune_fiscale;
	  lTerzo.CAP_COMUNE_SEDE 		  := aAnagrafico.cap_comune_fiscale;
-- 	  lTerzo.PG_RAPP_LEGALE			  := NULL;
-- 	  lTerzo.CD_UNITA_ORGANIZZATIVA   := NULL;
-- 	  lTerzo.NOME_UNITA_ORGANIZZATIVA := NULL;
-- 	  lTerzo.NOTE 					  := NULL;
-- 	  lTerzo.DT_CANC 				  := NULL;
	  lTerzo.DUVA 					  := aData;
	  lTerzo.UTUV 					  := aUtente;
	  lTerzo.PG_VER_REC 			  := lTerzo.PG_VER_REC + 1 ;

	  -- Modifica terzo
	  UPD_TERZO(lTerzo);

	  -- Modifica banca
	  MODIFICABANCA(aAnaDip ,aAnagrafico ,lTerzo , aData , aUtente );

	  -- Modifica modalita pagamento
  	  CREAMODPAG(aAnaDip, lTerzo, aData , aUtente);

 end;

procedure MODIFICABANCA(aAnaDip cnr_anadip%rowtype,aAnagrafico anagrafico%rowtype,aTerzo terzo%rowtype, aData Date, aUtente VARCHAR2) is
 lNumBanche number;
 lNumBancheValide number;
 lInsBanca boolean;
 lBanca banca%rowtype;
 lRifPagamento rif_modalita_pagamento%rowtype;
 lCdModPagamento rif_modalita_pagamento.CD_MODALITA_PAG%type;
 num_conti number;
 num_conti_agg number;
 pgbanca_prec number;
 pgbanca_canc number;
 Fl_Da_Cancellare boolean;
 Fl_Da_Cancellare_agg boolean;
 Numrighe number;
 pg_esistente number;
 flag BANCA.FL_CANCELLATO%type;


 begin
--Dbms_Output.put_line('MODIFICABANCA'||' matricola '||aAnaDip.matricola);
 		  -- decodifichiamo la modalita di pagamento
				    begin
						 select cd_modalita_pag_cir
						 into lCdModPagamento
						 from cnr_cir_rif_mod_pag
						 where cd_modalita_pag_cnr = Rtrim(aAnaDip.mod_pag)
						 for update nowait;

				    exception when no_data_found then
						 ibmutl200.logInf(gPgLog ,'Il terzo con cd_precedente = ' || aAnaDip.matricola , ' Risulta non avere una decodifica per la modalita di pagamento. In CNR_CIR_RIF_MOD_PAG non esiste cd_modalita_pag = ' || aAnaDip.mod_pag , '');

						 select cd_modalita_pag_cir
						 into lCdModPagamento
						 from cnr_cir_rif_mod_pag
						 where cd_modalita_pag_cnr = Nvl(Rtrim(aAnaDip.mod_pag),1)
						 for update nowait;
					end;

					  	  -- recuperiamo la modalita pagamento
						  select *
						  into lRifPagamento
						  from rif_modalita_pagamento
						  where cd_modalita_pag = lCdModPagamento;

					  	  -- recuperiamo il progreassivo della banca
					  	  select nvl(max(pg_banca),0) + 1
						  into lBanca.pg_banca
						  from banca
						  where cd_terzo = aTerzo.cd_terzo;

						  -- controlliamo se la banca risulta modificata
					 	  select count(*)
						  into lNumBanche
						  from banca
						  where cd_terzo = aTerzo.cd_terzo
						  --and nvl(abi,'0') = nvl(aAnaDip.abi_pag,'0')
						  --and nvl(cab,'0') = nvl(aAnaDip.cab_pag,'0')
						  --and   nvl(numero_conto,'0') = nvl(aAnaDip.nrc_pag,'0')
						  and   ti_pagamento = lRifPagamento.ti_pagamento
						  --and   Nvl(cin,'0') = Nvl(aAnaDip.cin_pag,'0')
						  and   Nvl(codice_iban,'0') = Nvl(aAnaDip.iban_pag,'0');


	  if lNumBanche > 0 then
	  	 -- la banca esiste
		 lInsBanca := false;

         -- controlliamo se la banca che esiste ha il fl_cancellato = Y, se si deve essere aggiornata
         -- altrimenti se è impostato a N  non bisogna fare niente.
         -- inizio
            select count(*) into Numrighe
				    from(
						select max(pg_banca), fl_cancellato
						from banca
						where cd_terzo = aTerzo.cd_terzo
						and   ti_pagamento =lRifPagamento.ti_pagamento
						-- 23/03/2016 non aggiorniamo i conti disabilitati se da ripristinare bisogna verificare aAnaDip.nrc_pag
						-- con substr(IBAN_PAG,16,12)
						--and   fl_cancellato= 'N'
						and   Nvl(codice_iban,'0') = Nvl(aAnaDip.iban_pag,'0')
						group by fl_cancellato);

				if Numrighe = 1 then

                    select max(pg_banca), fl_cancellato into pg_esistente, flag
										from banca
										where cd_terzo = aTerzo.cd_terzo
										and   ti_pagamento =lRifPagamento.ti_pagamento
										and   Nvl(codice_iban,'0') = Nvl(aAnaDip.iban_pag,'0')
										group by fl_cancellato;


							if pg_esistente is not null and flag = 'Y' and Nvl(aAnaDip.iban_pag,'0')!='0' then

                                begin
											update banca
											set fl_cancellato = 'N',
												duva = sysdate,
												utuv = 'CED',
												origine = 'S',
												intestazione = aAnaDip.nominativo,
												numero_conto = lpad(aAnaDip.nrc_pag,12,'0'),
												abi = aAnaDip.abi_pag,
												cab = aAnaDip.cab_pag,
												cin = aAnaDip.cin_pag
												where pg_banca = pg_esistente
												and cd_terzo = aTerzo.cd_terzo
												and ti_pagamento = 'B'
												and fl_cancellato = 'Y';

										    begin
													select count(*) into num_conti_agg
												  	from banca
												  	where cd_terzo = aTerzo.cd_terzo
												  	and origine = 'S'
												  	and fl_cancellato = 'N'
												  	and ti_pagamento =lRifPagamento.ti_pagamento
												  	group by cd_terzo,origine,fl_cancellato;

												  	if num_conti_agg > 1 then
													--dbms_output.put_line('DA AGG - cd_terzo ='|| aTerzo.cd_terzo||' ha più di un conto');

												  			select min(pg_banca) into pgbanca_canc
													  		from banca
													  		where cd_terzo = aTerzo.cd_terzo
													  		and origine = 'S'
													  		and fl_cancellato = 'N'
												        and Nvl(codice_iban,'0') != Nvl(aAnaDip.iban_pag,'0')
												        and ti_pagamento =  lRifPagamento.ti_pagamento;

												       		    Fl_Da_Cancellare_agg := true;
											--dbms_output.put_line('DA AGG - cd_terzo ='|| aTerzo.cd_terzo||' pg');
												     else
													  		      Fl_Da_Cancellare_agg := false;
													end if;

										 	exception
	  												when no_data_found then
	  														Fl_Da_Cancellare_agg := false;
	  									    end;

                                  if Fl_Da_Cancellare_agg then
                                    --dbms_output.put_line('DA AGG - numero c/c, cd_terzo ='|| aTerzo.cd_terzo||' progressivo banca' ||pgbanca_prec);
                                    update banca
                                    set fl_cancellato = 'Y',
                                        utuv = 'CED',
                                        duva = sysdate
                                    where cd_terzo = aTerzo.cd_terzo
                                    and origine = 'S'
                                    and fl_cancellato = 'N'
                                    and ti_pagamento = lRifPagamento.ti_pagamento
                                    and pg_banca = pgbanca_canc;
                                  end if;

								exception
									when others then
											dbms_output.put_line('Errore nell''aggiornamento delle coordinate bancarie per il terzo '||aTerzo.cd_terzo);
								end;
					    end if; --pg_esistente is not null...
				end if;	--Numrighe = 1

         -- fine la banca esiste ed è stata aggiornata!

	  else -- lNumBanche>0
	  	  -- la banca non esiste
		  -- controlliamo se abi e cab sono validi
	   	  select count(*)
			  into lNumBancheValide
			  from abicab
			  where abi = aAnaDip.abi_pag
			  and   cab = aAnaDip.cab_pag;

		  if lNumBancheValide > 0 then
	  	  	 lInsBanca := true;
		  else
	  	  	 lInsBanca := false;
		  	 if aAnaDip.abi_pag is not null and aAnaDip.cab_pag is not null then
			 			ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , ' Banca non recuperata - ABI, CAB non presenti nella tabella ABICAB ', '');
			 end if;  --aAnaDip.abi_pag is not null ...
		 end if; --lNumBancheValide > 0

	  end if;-- lNumBanche>0

	  if lInsBanca then
	  	 -- Creo questa banca se e solo se non esiste banca e abi e cab sono validi
		 lBanca.CD_TERZO  		:= aTerzo.cd_terzo;
		 lBanca.CAB				:= aAnaDip.cab_pag;
		 lBanca.ABI				:= aAnaDip.abi_pag;
		 lBanca.TI_PAGAMENTO	:= lRifPagamento.ti_pagamento;
		 lBanca.INTESTAZIONE	:= aAnaDip.NOMINATIVO;
		 lBanca.QUIETANZA		:= NULL;
		 -- 23/03/2016
		 if(aAnaDip.IBAN_PAG is not null and aAnaDip.IBAN_PAG like 'IT'||'%') then
		 		lBanca.NUMERO_CONTO	:= substr(aAnaDip.IBAN_PAG,16,12);
		 else
		 	  lBanca.NUMERO_CONTO	:= lpad(aAnaDip.NRC_PAG,12,'0');
		 end if;
		 lBanca.CODICE_IBAN		:= aAnaDip.IBAN_PAG;
		 lBanca.CODICE_SWIFT	:= NUll;
		 lBanca.DACR			:= aData;
		 lBanca.UTCR			:= aUtente;
		 lBanca.DUVA			:= aData;
		 lBanca.UTUV			:= aUtente;
		 lBanca.PG_VER_REC		:= 1;
		 lBanca.FL_CANCELLATO	:= 'N';
		 lBanca.CD_TERZO_DELEGATO := NULL;
		 lBanca.PG_BANCA_DELEGATO := NULL;
		 lBanca.ORIGINE := 'S';
		 lBanca.FL_CC_CDS := 'N';
		 lBanca.CIN		:= aAnaDip.CIN_PAG;

		 -- inseriamo la banca
		 INS_BANCA(lBanca);

--	  else -- lInsBanca

	  	  -- in questo caso posso arrivarci in due modi
		  -- 1) la banca esiste lNumBanche > 1
		  -- 2) la banca NON esiste lNumBanche = 0 e abi e cab NON sono validi
		  --    in questo caso creamo la banca se e solo se abi = cab = NULL
		  --    and ti_pagamento ='A'

	/*	 if  lRifPagamento.ti_pagamento ='A'
		 			and aAnaDip.abi_pag is null
		 			and aAnaDip.cab_pag is null
		 			and lNumBanche = 0
		 then
		 dbms_output.put_line('terzo con lNumBanche=0 '||aTerzo.cd_terzo);
			 lBanca.CD_TERZO  		:= aTerzo.cd_terzo;
			 lBanca.CAB				:= null;
			 lBanca.ABI				:= null;
			 lBanca.TI_PAGAMENTO	:= lRifPagamento.ti_pagamento;
			 lBanca.INTESTAZIONE	:= aAnaDip.NOMINATIVO;
			 lBanca.QUIETANZA		:= NULL;
			 lBanca.NUMERO_CONTO	:= null;
			 lBanca.CODICE_IBAN		:= NULL;
			 lBanca.CODICE_SWIFT	:= NUll;
			 lBanca.DACR			:= aData;
			 lBanca.UTCR			:= aUtente;
			 lBanca.DUVA			:= aData;
			 lBanca.UTUV			:= aUtente;
			 lBanca.PG_VER_REC		:= 1;
			 lBanca.FL_CANCELLATO	:= 'N';
			 lBanca.CD_TERZO_DELEGATO := NULL;
			 lBanca.PG_BANCA_DELEGATO := NULL;
			 lBanca.ORIGINE := 'S';
			 lBanca.FL_CC_CDS := 'N';
			 lBanca.CIN := null;
			 -- inseriamo la banca
			 INS_BANCA(lBanca);
		 end if; -- lRifPagamento.ti_pagamento ='A'...
	*/
		 -- Nel primo caso, in cui la banca esiste
		 -- controlliamo che abbia il campo ORIGINE ='S''
		 -- se non è così allora inseriamo la banca, altrimenti NO

/*		 if lRifPagamento.ti_pagamento ='A'
				 and aAnaDip.abi_pag is null
				 and aAnaDip.cab_pag is null
				 and lNumBanche > 0
		 then

		dbms_output.put_line('terzo con lNumBanche > 0 '||aTerzo.cd_terzo);
		 	 select count(*)
			 into lNumBanche
			 from banca
			 where cd_terzo = aTerzo.cd_terzo
			 --and   nvl(abi,'0') = nvl(aAnaDip.abi_pag,'0')
			 --and   nvl(cab,'0') = nvl(aAnaDip.cab_pag,'0')
			 --and   nvl(numero_conto,'0') = nvl(aAnaDip.nrc_pag,'0')
			 and   ti_pagamento = lRifPagamento.ti_pagamento
			 and   origine ='S'
			 --and   Nvl(cin,'0') = Nvl(aAnaDip.cin_pag,'0')
	     and   Nvl(codice_iban,'0') = Nvl(aAnaDip.iban_pag,'0');
*/
			/* if lNumBanche = 0 then
				 lBanca.CD_TERZO  		:= aTerzo.cd_terzo;
				 lBanca.CAB				:= null;
				 lBanca.ABI				:= null;
				 lBanca.TI_PAGAMENTO	:= lRifPagamento.ti_pagamento;
				 lBanca.INTESTAZIONE	:= aAnaDip.NOMINATIVO;
				 lBanca.QUIETANZA		:= NULL;
				 lBanca.NUMERO_CONTO	:= null;
				 lBanca.CODICE_IBAN		:= NULL;
				 lBanca.CODICE_SWIFT	:= NUll;
				 lBanca.DACR			:= aData;
				 lBanca.UTCR			:= aUtente;
				 lBanca.DUVA			:= aData;
				 lBanca.UTUV			:= aUtente;
				 lBanca.PG_VER_REC		:= 1;
				 lBanca.FL_CANCELLATO	:= 'N';
				 lBanca.CD_TERZO_DELEGATO := NULL;
				 lBanca.PG_BANCA_DELEGATO := NULL;
				 lBanca.ORIGINE := 'S';
				 lBanca.FL_CC_CDS := 'N';
				 lBanca.CIN := null;
				 -- inseriamo la banca
				 INS_BANCA(lBanca);
			 else
		 	 	 ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , ' Banca non recuperata', '');
			 end if;*/

	--	 end if;-- lRifPagamento.ti_pagamento ='A' ... and lNumBanche > 0


--FLAVIA--
		-- recuperiamo il record precedente. se è cambiato il numero di conto e il precedente non è stato reso inutilizzabile
		-- è necessario impostare il fl_cancellato = 'Y'

	  begin

	  	select count(*) into num_conti
	  	from banca
	  	where cd_terzo = aTerzo.cd_terzo
	  	and origine = 'S'
	  	and fl_cancellato = 'N'
	  	and ti_pagamento =lRifPagamento.ti_pagamento
	  	group by cd_terzo,origine,fl_cancellato;

	  	if num_conti>1 then
	  		--dbms_output.put_line('numero c/c, cd_terzo ='|| aTerzo.cd_terzo);
	  		select min(pg_banca) into pgbanca_prec
	  		from banca
	  		where cd_terzo = aTerzo.cd_terzo
	  		and origine = 'S'
	  		and fl_cancellato = 'N';

	  		Fl_Da_Cancellare := true;
	  	else
	  		Fl_Da_Cancellare := false;
	  	end if;

	  exception
	  	when no_data_found then
	  		Fl_Da_Cancellare := false;
	  end;

	  if Fl_Da_Cancellare then
	  	--dbms_output.put_line('DA INS - numero c/c, cd_terzo ='|| aTerzo.cd_terzo||' progressivo banca' ||pgbanca_prec);
	  	update banca
	  	set fl_cancellato = 'Y',
	  		utuv = 'CED',
	  		duva = trunc(sysdate)
	  	where cd_terzo = aTerzo.cd_terzo
	  	and origine = 'S'
	  	and fl_cancellato = 'N'
	  	and pg_banca = pgbanca_prec;
	  end if;
	end if;	-- lInsBanca
end;


function chkPresenzaAnag(aAnaDip cnr_anadip%rowtype, aAnagrafico in out anagrafico%rowtype) return boolean is
 lNum NUMBER;
 lGiaPresente boolean;
 begin
--Dbms_Output.put_line('chkPresenzaAnag'||' matricola '||aAnaDip.matricola);
 	-- il controllo sul codice fiscale viene eseguito anche per i tipi rapporto non dipendenti
	-- non serve la join con la tabella rapporto
	-- è stata aggiunta la condizione che la dt_fine_rapporto sia null a causa delle duplicazioni
	--  dt_fine_rapporto is null indica che l'anagrafico è valido
 	  select a.*
	  into aAnagrafico
	  from anagrafico a --, rapporto r
	  where a.CODICE_FISCALE = aAnaDip.DIP_COD_FIS
	  and dt_fine_rapporto is null
	  and partita_iva is null
	  --and r.cd_anag = a.cd_anag
	 -- and r.cd_tipo_rapporto = 'DIP'
	 -- and rownum <2
	  for update nowait;

          lGiaPresente := true;
	  return lGiaPresente;

 exception when no_data_found Then
      	 lGiaPresente := false;
   	 return lGiaPresente;
 end chkPresenzaAnag;

 function getComuneNascita(aAnaDip cnr_anadip%rowtype,  aAnagrafico IN OUT anagrafico%rowtype, aPgNazioneNascita in out number) return number is
 lItaliano char(1);
 lNumNazioni number;
 lPgComuneNascita number;
 begin
--Dbms_Output.put_line('getComuneNascita'||' matricola '||aAnaDip.matricola);

	  -- Controlliamo se l'anagrafico in esame risuta nato in Italia o No
      if substr(aAnaDip.tab_cod_comnas,1,1)= 'Z' then
	  	 lItaliano := 'E';
	  else
	  	 lItaliano := 'I';
	  end if;
	  -- Se italiamo
	  	 -- recuperiamo il primo comune tramite il codice catastale
	  -- Altrimenti
	  	 -- recuperiamo prima la nazione tramite il codice catastale
	  if lItaliano='I' then
	      -- Recuperiamo il codice e cap del comune di nascita
 		  begin
		  	   SELECT pg_comune, pg_nazione
			   into  lPgComuneNascita, aPgNazioneNascita
	           FROM   Comune
	           WHERE  cd_catastale    = aAnaDip.tab_cod_comnas
			   and rownum=1;
		  exception when no_data_found then
		  			-- Comune non definito
					lPgComuneNascita := 0;
					aPgNazioneNascita :=0;
				  	ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Comune Nascita di default' || ' il codice catastale ' || aAnaDip.tab_cod_comnas || ' non risulta nella tabella comune', '');
		  end;
	  end if;

	  if lItaliano <> 'I' then
	  	 begin
		 	 -- Contiamo le nazioni a parita di cd_catastale
	  	     SELECT count(*)
		   	 into   lNumNazioni
           	 FROM   Nazione
           	 WHERE  cd_catastale    = aAnaDip.tab_cod_comnas;

			 -- Non Esiste Nazione
			 if lNumNazioni = 0 then
	  			 -- Nazione non definita
				 aPgNazioneNascita:=0;
				 ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Nazione Nascita di default' || ' il codice catastale ' || aAnaDip.tab_cod_comnas ||' non risulta nella tabella nazione' , '');
				 -- Comune non definito
				 lPgComuneNascita:=0;
				 ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Comune Nascita di default' || ' il comune ' || aAnaDip.comune_nasc ||' non risulta nella tabella comune', '');
			 end if;

			 -- Esiste solo una Nazione
			 if lNumNazioni = 1 then
			     -- Recuperiamo il codice della Nazione di nascita
				 SELECT pg_Nazione
				 into   aPgNazioneNascita
			     FROM   Nazione
			     WHERE  cd_catastale    = aAnaDip.tab_cod_comnas;

				 begin
					 SELECT pg_comune
					 into   lPgComuneNascita
				     FROM   Comune
				     WHERE  pg_nazione   = aPgNazioneNascita
					 and    ds_comune    = aAnaDip.comune_nasc
					 and rownum=1;
				 exception when no_data_found then
				  			-- Comune non definito
							-- Si potrebbwe pensare di mettere la nazione
							lPgComuneNascita:=0;
					  		ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Comune Nascita di default' || ' il comune ' || aAnaDip.comune_nasc ||' non risulta nella tabella comune', '');
				 end;
			 end if;

			 -- Esiste piu di una Nazione
			 if lNumNazioni > 1 then
			     -- Recuperiamo il codice della Nazione di nascita
				 begin
				  	   SELECT pg_Nazione
					   into   aPgNazioneNascita
			           FROM   Nazione
			           WHERE  cd_catastale      = aAnaDip.tab_cod_comnas
					   and    ds_nazione  		= aAnaDip.comune_nasc;
				 exception when no_data_found then
				  			-- Nazione non definita
							aPgNazioneNascita:=0;
					  		ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Nazione Nascita di default' || ' il codice catastale ' || aAnaDip.tab_cod_comnas ||' non risulta nella tabella nazione' , '');
				 end;
				 begin
				  	   SELECT pg_comune
					   into   lPgComuneNascita
			           FROM   Comune
			           WHERE  pg_nazione   = aPgNazioneNascita
					   and    ds_comune    = aAnaDip.comune_nasc
					   and rownum=1;
				 exception when no_data_found then
				  			-- Comune non definito
							-- Si potrebbwe pensare di mettere la nazione
							lPgComuneNascita:=0;
					  		ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Comune Nascita di default' || ' il comune ' || aAnaDip.comune_nasc ||' non risulta nella tabella comune', '');
				 end;
			 end if;
	  	 end;
	  end if;
      return lPgComuneNascita;

 end;

 function getComuneFiscale(aAnaDip cnr_anadip%rowtype,  aAnagrafico IN OUT anagrafico%rowtype, aPgNazioneFiscale in out number) return number is
 lItaliano char(1);
 lNumNazioni number;
 lPgComuneFiscale number;
 begin
--Dbms_Output.put_line('getComuneFiscale'||' matricola '||aAnaDip.matricola);
	  -- Controlliamo se l'anagrafico in esame risuta nato in Italia o No
      if substr(aAnaDip.cod_comune,1,1)= 'Z' then
	  	 lItaliano := 'E';
	  else
	  	 lItaliano := 'I';
	  end if;
	  -- Se italiamo
	  	 -- recuperiamo il primo comune tramite il codice catastale
	  -- Altrimenti
	  	 -- recuperiamo prima la nazione tramite il codice catastale
	  if lItaliano='I' then
	      -- Recuperiamo il codice e cap del comune di nascita
 		  begin
		  	   SELECT pg_comune, pg_nazione
			   into  lPgComuneFiscale,aPgNazioneFiscale
	           FROM   Comune
	           WHERE  cd_catastale    = aAnaDip.cod_comune
			   and rownum=1; --codice comune residenza ??
		  exception when no_data_found then
		  			-- Comune non definito
					lPgComuneFiscale := 0;
					aPgNazioneFiscale := 0;
				  	ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Comune Fiscale di default' || ' il codice catastale ' || aAnaDip.cod_comune ||' non risulta nella tabella comune', '');
		  end;
	  end if;

	  if lItaliano <> 'I' then

	  	 begin
		 	 -- Contiamo le nazioni a parita di cd_catastale
	  	     SELECT count(*)
		   	 into   lNumNazioni
           	 FROM   Nazione
           	 WHERE  cd_catastale    = aAnaDip.cod_comune;

			 -- Non Esiste Nazione
			 if lNumNazioni = 0 then
	  			 -- Nazione non definita
				 aPgNazioneFiscale:=0;
				 ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Nazione Fiscale di default' || ' il codice catastale ' || aAnaDip.cod_comune ||' non risulta nella tabella nazione' , '');
				 -- Comune non definito
				 lPgComuneFiscale:=0;
				 ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Comune Fiscale di default' || ' il comune ' || aAnaDip.com_resid ||' non risulta nella tabella comune', '');
			 end if;

			 -- Esiste solo una Nazione
			 if lNumNazioni = 1 then
			     -- Recuperiamo il codice della Nazione di nascita
				 SELECT pg_Nazione
				 into   aPgNazioneFiscale
			     FROM   Nazione
			     WHERE  cd_catastale    = aAnaDip.cod_comune;

				 begin
					 SELECT pg_comune
					 into   lPgComuneFiscale
				     FROM   Comune
				     WHERE  pg_nazione   = aPgNazioneFiscale
					 and    ds_comune    = aAnaDip.com_resid
					 and rownum=1;
				 exception when no_data_found then
				  			-- Comune non definito
							-- Si potrebbwe pensare di mettere la nazione
							lPgComuneFiscale:=0;
					  		ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Comune Fiscale di default' || ' il comune ' || aAnaDip.com_resid ||' non risulta nella tabella comune', '');
				 end;
			 end if;

			 -- Esiste piu di una Nazione
			 if lNumNazioni > 1 then
			     -- Recuperiamo il codice della Nazione di nascita
				 begin
				  	   SELECT pg_Nazione
					   into   aPgNazioneFiscale
			           FROM   Nazione
			           WHERE  cd_catastale      = aAnaDip.cod_comune
					   and    ds_nazione  		= aAnaDip.com_resid;
				 exception when no_data_found then
				  			-- Nazione non definita
							aPgNazioneFiscale:=0;
					  		ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Nazione Fiscale di default' || ' il codice catastale ' || aAnaDip.cod_comune ||' non risulta nella tabella nazione' , '');
				 end;
				 begin
				  	   SELECT pg_comune
					   into   lPgComuneFiscale
			           FROM   Comune
			           WHERE  pg_nazione   = aPgNazioneFiscale
					   and    ds_comune    = aAnaDip.com_resid
					   and rownum=1;
				 exception when no_data_found then
				  			-- Comune non definito
							-- Si potrebbwe pensare di mettere la nazione
							lPgComuneFiscale:=0;
					  		ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Associato con Comune Fiscale di default' || ' il comune ' || aAnaDip.com_resid ||' non risulta nella tabella comune', '');
				 end;
			 end if;
	  	 end;

 	  end if;
 	  return lPgComuneFiscale;
 end;


 procedure INS_ANAGRAFICO(aAnagrafico anagrafico%rowtype) is
 begin
--Dbms_Output.put_line('INS_ANAGRAFICO'||' anag '||aAnagrafico.cd_anag);
	 insert into anagrafico(
			CD_ANAG,
			CD_CLASSIFIC_ANAG,
			TI_ENTITA,
			TI_ENTITA_FISICA,
			TI_ENTITA_GIURIDICA,
			FL_SOGGETTO_IVA,
			CODICE_FISCALE,
			PARTITA_IVA,
			ID_FISCALE_ESTERO,
			RAGIONE_SOCIALE,
			NOME,
			COGNOME,
			TI_SESSO,
			DT_NASCITA ,
			PG_COMUNE_NASCITA,
			VIA_FISCALE,
			NUM_CIVICO_FISCALE,
			PG_COMUNE_FISCALE ,
			CAP_COMUNE_FISCALE ,
			FRAZIONE_FISCALE,
			PG_NAZIONE_FISCALE,
			PG_NAZIONE_NAZIONALITA,
			FL_FATTURAZIONE_DIFFERITA,
			FL_OCCASIONALE,
			TI_ITALIANO_ESTERO,
			CD_ATTIVITA_INPS,
			ALTRA_ASS_PREVID_INPS,
			ALIQUOTA_FISCALE,
			CODICE_FISCALE_CAF,
			DENOMINAZIONE_CAF,
			SEDE_INAIL,
			MATRICOLA_INAIL,
			CONTO_NUMERARIO_CREDITO,
			CONTO_NUMERARIO_DEBITO,
			NUM_ISCRIZ_CCIAA,
			NUM_ISCRIZ_ALBO,
			DT_FINE_RAPPORTO,
			CAUSALE_FINE_RAPPORTO,
			DT_CANC,
			DT_ANTIMAFIA,
			CD_ENTE_APPARTENENZA,
			NOTE,
			DACR ,
			UTCR,
			DUVA,
			UTUV,
			PG_VER_REC)
	 values(
 	  aAnagrafico.CD_ANAG,
	  aAnagrafico.CD_CLASSIFIC_ANAG,
	  aAnagrafico.TI_ENTITA,
	  aAnagrafico.TI_ENTITA_FISICA,
	  aAnagrafico.TI_ENTITA_GIURIDICA,
	  aAnagrafico.FL_SOGGETTO_IVA,
	  aAnagrafico.CODICE_FISCALE,
	  aAnagrafico.PARTITA_IVA,
	  aAnagrafico.ID_FISCALE_ESTERO,
	  aAnagrafico.RAGIONE_SOCIALE,
	  aAnagrafico.NOME,
	  aAnagrafico.COGNOME,
	  aAnagrafico.TI_SESSO,
	  aAnagrafico.DT_NASCITA,
	  aAnagrafico.PG_COMUNE_NASCITA,
	  aAnagrafico.VIA_FISCALE,
	  aAnagrafico.NUM_CIVICO_FISCALE,
	  aAnagrafico.PG_COMUNE_FISCALE,
	  aAnagrafico.CAP_COMUNE_FISCALE,
	  aAnagrafico.FRAZIONE_FISCALE,
	  aAnagrafico.PG_NAZIONE_FISCALE,
	  aAnagrafico.PG_NAZIONE_NAZIONALITA,
	  aAnagrafico.FL_FATTURAZIONE_DIFFERITA,
	  aAnagrafico.FL_OCCASIONALE,
	  aAnagrafico.TI_ITALIANO_ESTERO,
 	  aAnagrafico.CD_ATTIVITA_INPS,
 	  aAnagrafico.ALTRA_ASS_PREVID_INPS,
 	  aAnagrafico.ALIQUOTA_FISCALE,
 	  aAnagrafico.CODICE_FISCALE_CAF,
 	  aAnagrafico.DENOMINAZIONE_CAF,
 	  aAnagrafico.SEDE_INAIL,
 	  aAnagrafico.MATRICOLA_INAIL,
 	  aAnagrafico.CONTO_NUMERARIO_CREDITO,
 	  aAnagrafico.CONTO_NUMERARIO_DEBITO,
 	  aAnagrafico.NUM_ISCRIZ_CCIAA,
 	  aAnagrafico.NUM_ISCRIZ_ALBO,
 	  aAnagrafico.DT_FINE_RAPPORTO,
 	  aAnagrafico.CAUSALE_FINE_RAPPORTO,
 	  aAnagrafico.DT_CANC,
 	  aAnagrafico.DT_ANTIMAFIA,
 	  aAnagrafico.CD_ENTE_APPARTENENZA,
 	  aAnagrafico.NOTE,
 	  aAnagrafico.DACR,
 	  aAnagrafico.UTCR,
 	  aAnagrafico.DUVA,
 	  aAnagrafico.UTUV,
 	  aAnagrafico.PG_VER_REC
	 );
	 exception
	 		  when others then
			  	   dbms_output.put_line('Errore '||substr(sqlerrm,1,200)||' per l'' anagrafico: '||aAnagrafico.CD_ANAG);
 end;

 procedure INS_TERZO(aTerzo Terzo%rowtype) is
 begin
 --Dbms_Output.put_line('INS_TERZO'||' anag '||aTerzo.cd_anag);
 	  insert into terzo (
	  		 CD_TERZO,
			 FRAZIONE_SEDE,
			 CD_ANAG,
			 DT_FINE_RAPPORTO,
			 TI_TERZO,
			 CD_PRECEDENTE,
			 DENOMINAZIONE_SEDE,
			 VIA_SEDE,
			 NUMERO_CIVICO_SEDE,
			 PG_COMUNE_SEDE,
			 CAP_COMUNE_SEDE,
			 PG_RAPP_LEGALE,
			 CD_UNITA_ORGANIZZATIVA,
			 NOME_UNITA_ORGANIZZATIVA,
			 NOTE,
			 DT_CANC,
			 DACR,
			 UTCR,
			 DUVA,
			 UTUV,
			 PG_VER_REC)
 	  values (
	  		 aTerzo.CD_TERZO,
			 aTerzo.FRAZIONE_SEDE,
			 aTerzo.CD_ANAG,
			 aTerzo.DT_FINE_RAPPORTO,
			 aTerzo.TI_TERZO,
			 aTerzo.CD_PRECEDENTE,
			 aTerzo.DENOMINAZIONE_SEDE,
			 aTerzo.VIA_SEDE,
			 aTerzo.NUMERO_CIVICO_SEDE,
			 aTerzo.PG_COMUNE_SEDE,
			 aTerzo.CAP_COMUNE_SEDE,
			 aTerzo.PG_RAPP_LEGALE,
			 aTerzo.CD_UNITA_ORGANIZZATIVA,
			 aTerzo.NOME_UNITA_ORGANIZZATIVA,
			 aTerzo.NOTE,
			 aTerzo.DT_CANC,
			 aTerzo.DACR,
			 aTerzo.UTCR,
			 aTerzo.DUVA,
			 aTerzo.UTUV,
			 aTerzo.PG_VER_REC
	  		 );
	exception
	 when others then
			dbms_output.put_line('Errore '||substr(sqlerrm,1,200)||' per il terzo: '||aTerzo.CD_TERZO);

 end;

 procedure INS_BANCA(aBanca banca%rowtype) is
 begin
 --Dbms_Output.put_line('INS_BANCA'||' terzo '||aBanca.cd_terzo);
 	  insert into banca (
				 		 CD_TERZO,
						 PG_BANCA,
						 CAB, ABI,
						 INTESTAZIONE,
						 QUIETANZA,
						 NUMERO_CONTO,
						 TI_PAGAMENTO,
						 CODICE_IBAN,
						 CODICE_SWIFT,
						 DACR,
						 UTCR,
						 DUVA,
						 UTUV,
						 PG_VER_REC,
						 FL_CANCELLATO,
						 CD_TERZO_DELEGATO,
		 				 PG_BANCA_DELEGATO,
		 				 ORIGINE,
		 				 FL_CC_CDS,
		 				 CIN
						 )
 	  values(
						 aBanca.CD_TERZO,
						 aBanca.PG_BANCA,
						 aBanca.CAB,
						 aBanca.ABI,
						 aBanca.INTESTAZIONE,
						 aBanca.QUIETANZA,
						 aBanca.NUMERO_CONTO,
						 aBanca.TI_PAGAMENTO,
						 aBanca.CODICE_IBAN,
						 aBanca.CODICE_SWIFT,
						 aBanca.DACR,
						 aBanca.UTCR,
						 aBanca.DUVA,
						 aBanca.UTUV,
						 aBanca.PG_VER_REC,
						 aBanca.FL_CANCELLATO,
		 				 aBanca.CD_TERZO_DELEGATO,
		 				 aBanca.PG_BANCA_DELEGATO,
		 				 aBanca.ORIGINE,
		 				 aBanca.FL_CC_CDS,
		 				 aBanca.CIN
		);
 end;

 procedure INS_MOD_PAGAMENTO (aModPagamento modalita_pagamento%rowtype,aAnaDip cnr_anadip%rowtype ) is
 begin
 --Dbms_Output.put_line('INS_MOD_PAGAMENTO'||' matricola '||aAnaDip.matricola);
 	  INSERT INTO MODALITA_PAGAMENTO
					(CD_TERZO,
					CD_MODALITA_PAG,
					CD_TERZO_DELEGATO,
					DACR,
					UTCR,
					DUVA,
					UTUV,
					PG_VER_REC)
			  VALUES (aModPagamento.CD_TERZO,
					aModPagamento.CD_MODALITA_PAG,
					aModPagamento.CD_TERZO_DELEGATO,
					aModPagamento.DACR,
					aModPagamento.UTCR,
					aModPagamento.DUVA,
					aModPagamento.UTUV,
					aModPagamento.PG_VER_REC);
 exception
 when dup_val_on_index then
 	  ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , ' risulta già inserita la modalita di pagamento ' || aModPagamento.CD_MODALITA_PAG, '');
 end;

 procedure INS_RAPPORTO (aRapporto rapporto%rowtype) is
 begin
--Dbms_Output.put_line('INS_RAPPORTO'||' matricola '||aRapporto.matricola_dipendente);
	  insert into rapporto (
	  CD_TIPO_RAPPORTO
	  ,CD_ANAG
	  ,DT_INI_VALIDITA
	  ,MATRICOLA_DIPENDENTE
	  ,CAUSALE_FINE_RAPPORTO
	  ,DT_FIN_VALIDITA
	  ,DACR
	  ,UTCR
	  ,DUVA
	  ,UTUV
	  ,PG_VER_REC
	  ,CD_ENTE_PREV_STI
	  ,CD_RAPP_IMPIEGO_STI)   --flavia
	  values(
	  aRapporto.CD_TIPO_RAPPORTO
	  ,aRapporto.CD_ANAG
	  ,aRapporto.DT_INI_VALIDITA
	  ,aRapporto.MATRICOLA_DIPENDENTE
	  ,aRapporto.CAUSALE_FINE_RAPPORTO
	  ,aRapporto.DT_FIN_VALIDITA
	  ,aRapporto.DACR
	  ,aRapporto.UTCR
	  ,aRapporto.DUVA
	  ,aRapporto.UTUV
	  ,aRapporto.PG_VER_REC
	  ,aRapporto.CD_ENTE_PREV_STI
	  ,aRapporto.CD_RAPP_IMPIEGO_STI); --flavia

 exception
 when others then
		dbms_output.put_line('Errore '||substr(sqlerrm,1,200)||' per l''anagrafico: '||aRapporto.CD_ANAG||' - '||
		aRapporto.CD_TIPO_RAPPORTO||' - '||aRapporto.DT_INI_VALIDITA||' - '||
		aRapporto.DT_FIN_VALIDITA);
 end;


 procedure INS_INQUADRAMENTO (aInquadramento inquadramento%rowtype) as
 begin
 --Dbms_Output.put_line('INS_INQUADRAMENTO'||' anag '||aInquadramento.cd_anag);
 	  insert into inquadramento (
	  		 CD_TIPO_RAPPORTO,
			 CD_ANAG,
			 DT_INI_VALIDITA_RAPPORTO,
			 PG_RIF_INQUADRAMENTO,
			 DT_INI_VALIDITA,
			 DT_FIN_VALIDITA,
			 DACR,
			 UTCR,
			 DUVA,
			 UTUV,
			 PG_VER_REC)
	  values (
	  		 aInquadramento.CD_TIPO_RAPPORTO,
			 aInquadramento.CD_ANAG,
			 aInquadramento.DT_INI_VALIDITA_RAPPORTO,
			 aInquadramento.PG_RIF_INQUADRAMENTO,
			 aInquadramento.DT_INI_VALIDITA,
			 aInquadramento.DT_FIN_VALIDITA,
			 aInquadramento.DACR,
			 aInquadramento.UTCR,
			 aInquadramento.DUVA,
			 aInquadramento.UTUV,
			 aInquadramento.PG_VER_REC);

Exception
       /*When Dup_Val_On_Index Then
                Null;*/
	when Others Then
		dbms_output.put_line('Errore '||substr(sqlerrm,1,200)||' per l''anagrafico: '||aInquadramento.CD_ANAG||' - '||
		aInquadramento.CD_TIPO_RAPPORTO||' - '||aInquadramento.DT_INI_VALIDITA_RAPPORTO||' - '||
		aInquadramento.PG_RIF_INQUADRAMENTO||' - '||aInquadramento.DT_INI_VALIDITA);
 end;


 procedure INS_MONTANTE(aMontante montanti%rowtype) as
 begin
 --Dbms_Output.put_line('INS_MONTANTE anag '||aMontante.cd_anag);
 	  insert into montanti
		  (ESERCIZIO,
		   CD_ANAG,
		   IRPEF_DIPENDENTI,
		   INPS_DIPENDENTI,
		   INPS_TESORO_DIPENDENTI,
		   RIDUZ_DIPENDENTI,
		   FONDO_FS_DIPENDENTI,
		   IRPEF_ALTRI,
		   INPS_ALTRI,
		   INAIL_ALTRI,
		   RIDUZ_ALTRI,
		   DACR,
		   UTCR,
		   DUVA,
		   UTUV,
		   PG_VER_REC,
		   IRPEF_LORDO_DIPENDENTI,
		   IRPEF_LORDO_ALTRI,
		   DEDUZIONE_IRPEF_DIPENDENTI,
		   DEDUZIONE_IRPEF_ALTRI,
		   INPS_OCCASIONALI,
		   DEDUZIONE_FAMILY_ALTRI,
           INPGI_ALTRI,
           ENPAPI_ALTRI)
 	  values
	  		 (aMontante.ESERCIZIO,
		      	  aMontante.CD_ANAG,
		   	  aMontante.IRPEF_DIPENDENTI,
		   	  aMontante.INPS_DIPENDENTI,
		   	  aMontante.INPS_TESORO_DIPENDENTI,
		   	  aMontante.RIDUZ_DIPENDENTI,
		   	  aMontante.FONDO_FS_DIPENDENTI,
		   	  aMontante.IRPEF_ALTRI,
		   	  aMontante.INPS_ALTRI,
		   	  aMontante.INAIL_ALTRI,
		   	  aMontante.RIDUZ_ALTRI,
		   	  aMontante.DACR,
		   	  aMontante.UTCR,
		   	  aMontante.DUVA,
		   	  aMontante.UTUV,
		   	  aMontante.PG_VER_REC,
		   	  aMontante.IRPEF_LORDO_DIPENDENTI,
		   	  aMontante.IRPEF_LORDO_ALTRI,
		   	  aMontante.DEDUZIONE_IRPEF_DIPENDENTI,
		   	  aMontante.DEDUZIONE_IRPEF_ALTRI,
		   	  aMontante.INPS_OCCASIONALI,
		   	  aMontante.DEDUZIONE_FAMILY_ALTRI,
          aMontante.INPGI_ALTRI,
          aMontante.ENPAPI_ALTRI);
 end;

 procedure UPD_ANAGRAFICO(aAnagrafico anagrafico%rowtype) is
 begin
 --Dbms_Output.put_line('UPD_ANAGRAFICO'||' anag '||aAnagrafico.cd_anag);
	 update anagrafico
	 set
-- 	  CD_ANAG                    := lPgAnagrafico
-- 	  CD_CLASSIFIC_ANAG 		 := NULL -- ?? Chiedere
-- 	  TI_ENTITA 				 := 'F'
-- 	  TI_ENTITA_FISICA 			 := 'A'
-- 	  TI_ENTITA_GIURIDICA 		 := NULL
-- 	  FL_SOGGETTO_IVA 			 := 'N'
	  CODICE_FISCALE 			 = aAnagrafico.CODICE_FISCALE,
-- 	  PARTITA_IVA 				 = NULL
-- 	  ID_FISCALE_ESTERO 		 = NULL
-- 	  RAGIONE_SOCIALE 			 = NULL
	  NOME 						 = aAnagrafico.NOME,
	  COGNOME 					 = aAnagrafico.COGNOME,
	  TI_SESSO 					 = aAnagrafico.TI_SESSO,
	  DT_NASCITA 				 = aAnagrafico.DT_NASCITA,
	  PG_COMUNE_NASCITA 		 = aAnagrafico.PG_COMUNE_NASCITA,
	  VIA_FISCALE 				 = aAnagrafico.VIA_FISCALE,
	  NUM_CIVICO_FISCALE 		 = aAnagrafico.NUM_CIVICO_FISCALE,
	  PG_COMUNE_FISCALE 		 = aAnagrafico.PG_COMUNE_FISCALE,
	  CAP_COMUNE_FISCALE 		 = aAnagrafico.CAP_COMUNE_FISCALE,
--	  FRAZIONE_FISCALE 			 = NULL -- ?? Chiedere
	  PG_NAZIONE_FISCALE 		 = aAnagrafico.PG_NAZIONE_FISCALE,
	  PG_NAZIONE_NAZIONALITA 	 = aAnagrafico.PG_NAZIONE_NAZIONALITA,
-- 	  FL_FATTURAZIONE_DIFFERITA  = 'N',
-- 	  FL_OCCASIONALE 			 = 'N',
	  TI_ITALIANO_ESTERO 		 = aAnagrafico.TI_ITALIANO_ESTERO,
--    CD_ATTIVITA_INPS 			 = NULL,
--    ALTRA_ASS_PREVID_INPS 	 = NULL,
--    ALIQUOTA_FISCALE 			 = NULL,
--    CODICE_FISCALE_CAF 		 = NULL, -- ?? Chiedere
--    DENOMINAZIONE_CAF 		 = NULL, -- ?? Chiedere
--    SEDE_INAIL 				 = NULL, -- ?? Chiedere
--    MATRICOLA_INAIL 			 = NULL, -- ?? Chiedere
--    CONTO_NUMERARIO_CREDITO 	 = NULL, -- Non Usare
--    CONTO_NUMERARIO_DEBITO 	 = NULL, -- Non Usare
--    NUM_ISCRIZ_CCIAA 			 = NULL,
--    NUM_ISCRIZ_ALBO 			 = NULL,
-- 	  DT_FINE_RAPPORTO 		     = aAnaDip.data_cessazione,
-- 	  CAUSALE_FINE_RAPPORTO 	 = aAnaDip.desc_causa_Cess,
--    DT_CANC 					 = NULL,
--    DT_ANTIMAFIA 				 = NULL,
--    CD_ENTE_APPARTENENZA 		 = NULL,
--    NOTE 						 = NULL,
 	  DUVA 						 = aAnagrafico.DUVA,
 	  UTUV 						 = aAnagrafico.UTUV,
 	  PG_VER_REC 				 = aAnagrafico.PG_VER_REC
	 where cd_anag = aAnagrafico.cd_anag;
 end;

 procedure UPD_TERZO(aTerzo Terzo%rowtype) is
 begin
 --Dbms_Output.put_line('UPD_TERZO'||' anag '||aTerzo.cd_anag);
 	  update terzo
	  set
--	  CD_TERZO                = lPgTerzo;
--	  FRAZIONE_SEDE  		  = NULL;
--	  CD_ANAG 	   			  = aAnagrafico.cd_anag;
--	  DT_FINE_RAPPORTO   	  = aAnagrafico.dt_fine_rapporto;
--	  TI_TERZO		    	  = 'E';
	  CD_PRECEDENTE	    	  = aTerzo.CD_PRECEDENTE,
	  DENOMINAZIONE_SEDE 	  = aTerzo.DENOMINAZIONE_SEDE,
	  VIA_SEDE				  = aTerzo.VIA_SEDE,
	  NUMERO_CIVICO_SEDE 	  = aTerzo.NUMERO_CIVICO_SEDE,
	  PG_COMUNE_SEDE     	  = aTerzo.PG_COMUNE_SEDE,
	  CAP_COMUNE_SEDE 		  = aTerzo.CAP_COMUNE_SEDE,
-- 	  PG_RAPP_LEGALE		  = NULL,
-- 	  CD_UNITA_ORGANIZZATIVA  = NULL,
-- 	  NOME_UNITA_ORGANIZZATIVA= NULL,
-- 	  NOTE 					  = NULL,
-- 	  DT_CANC 				  = NULL,
	  DUVA 					  = aTerzo.DUVA,
	  UTUV 					  = aTerzo.UTUV,
	  PG_VER_REC 			  = aTerzo.PG_VER_REC
	  where cd_terzo = aTerzo.cd_terzo;

 end;

 procedure UPD_RAPPORTO (aAnaDip cnr_Anadip%rowtype,aAnagrafico anagrafico%rowtype,
 		   				aRapporto in out rapporto%rowtype, aRapportoPrec in out rapporto%rowtype,
						aData date, aUtente varchar2) is
 lCdTipoRap tipo_rapporto.cd_tipo_rapporto%type;
 lEseguiControllo boolean:=true;
 begin
 --Dbms_Output.put_line('UPD_RAPPORTO'||' matricola '||aAnaDip.matricola);
	  lCdTipoRap := cTIPO_RAPPORTO;

	  Begin

	  	  -- Carichiamo il rapporto attuale
		  select *
		  into aRapporto
		  from rapporto
		  where cd_tipo_rapporto       = lCdTipoRap
		  and   cd_anag 	       = aAnagrafico.cd_anag
		  and   trunc(dt_ini_validita) = trunc(aAnadip.data_assunzione) for update nowait;

                /* Dbms_Output.put_line('rapporto ' ||' tipo_rapporto '||lCdTipoRap||
                'anagrafico '||aAnagrafico.cd_anag||' data assunzione '||trunc(aAnadip.data_assunzione)||
                ' data ini val rapporto '||aRapporto.dt_ini_validita);*/

      if (aRapporto.CD_ENTE_PREV_STI is null or (aRapporto.CD_ENTE_PREV_STI!= aAnaDip.ente_prev)) then
      	update rapporto
				  set  cd_ente_prev_sti =  aAnaDip.ente_prev
				  	  ,DUVA		     = aData
				  	  ,UTUV		     = aUtente
				  	  ,PG_VER_REC        = PG_VER_REC +1
				  where cd_tipo_rapporto = lCdTipoRap
				  and   cd_anag 	 = aAnagrafico.cd_anag
				  and   dt_ini_validita  = aRapporto.dt_ini_validita;

      end if;
		  -- Se esiste un rapporto e risulta data_cessazione is not null e > sysdate
		  if trim(aAnaDip.data_cessazione) is not null then

		  --if trunc(aAnaDip.data_cessazione) > trunc(sysdate) then
			  	  aRapporto.CAUSALE_FINE_RAPPORTO := aAnaDip.desc_causa_cess;
			  	  aRapporto.DT_FIN_VALIDITA       := aAnaDip.data_cessazione;--nvl(aAnaDip.data_cessazione,to_date('31122200','ddmmyyyy'));


				  update rapporto
				  set  CAUSALE_FINE_RAPPORTO = aAnaDip.desc_causa_cess
				  	  ,DT_FIN_VALIDITA   = aAnaDip.data_cessazione--nvl(aAnaDip.data_cessazione,to_date('31122200','ddmmyyyy'))
				  	  ,DUVA		     = aData
				  	  ,UTUV		     = aUtente
				  	  ,PG_VER_REC        = PG_VER_REC +1
				  where cd_tipo_rapporto = lCdTipoRap
				  and   cd_anag 	 = aAnagrafico.cd_anag
				  and   dt_ini_validita  = aRapporto.dt_ini_validita;
		  	  ---end if;

		  end if;

/* da testare  nuovo aggiornamento per i rapporti chiusi che vengono ripristinati! */
		  -- se un rapporto era stato chiuso ma il dipendente è rientrato in servizio
		  -- viene aggiornata la data di fine validità del rapporto.

		  if trim(aAnaDip.data_cessazione) is null and trunc(aRapporto.dt_fin_validita) is not null
		  	 and trunc(aRapporto.dt_fin_validita)<to_date('31122200','ddmmyyyy') then

		  Dbms_Output.put_line('aggiorno il rapporto con la nuova data di fine validità - '||' matricola '||aAnaDip.matricola);

		  	 update rapporto
				  set  CAUSALE_FINE_RAPPORTO = aAnaDip.desc_causa_cess
				  	  ,DT_FIN_VALIDITA   = nvl(aAnaDip.data_cessazione,to_date('31122200','ddmmyyyy'))
				  	  ,DUVA		     = aData
				  	  ,UTUV		     = aUtente
				  	  ,PG_VER_REC        = PG_VER_REC +1
				  where cd_tipo_rapporto = lCdTipoRap
				  and   cd_anag 	 = aAnagrafico.cd_anag
				  and   dt_ini_validita  = aRapporto.dt_ini_validita;

		  Dbms_Output.put_line('data di fine validità - '||nvl(aAnaDip.data_cessazione,to_date('31122200','ddmmyyyy')));
		  end if;


 		  --if aAnaDip.profilo_prec_fine is not null then
		  	ALLINEA_INQUADRAMENTO(aAnaDip ,aAnagrafico ,aData ,aUtente );
		 -- else
		  	-- UPD_INQUADRAMENTO (aAnaDip ,aAnagrafico ,aRapporto ,aRapportoPrec,aData,aUtente);
		 -- end if;

	  exception
	  when others then
               -- Dbms_Output.put_line(Sqlerrm);
	  	   -- Se non esiste un rapporto con data inizio = data_assunzione
--		begin
			    if (aRapporto.dt_fin_validita Is Not Null And Trunc(aRapporto.dt_fin_validita) > trunc(aAnadip.data_assunzione)) then
				   -- in questo caso i periodi si sovrappongono
				--ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Non Inserito Rapporto precedente > data inizio rapporto attuale ', '');
                                --Dbms_Output.put_line('Data fine validita rapporto '|| aRapporto.dt_fin_validita||' - data_assunzione '||aAnadip.data_assunzione);
                                ---FLAVIA---  Chiusura del rapporto precedente ed inserimento del nuovo

				update rapporto
				  set  DT_FIN_VALIDITA 		 = nvl(trunc(aAnaDip.data_assunzione)-1,trunc(aRapporto.dt_ini_validita)-1)
				  	  ,DUVA					 = aData
				  	  ,UTUV					 = aUtente
				  	  ,PG_VER_REC			 = PG_VER_REC +1
				  where cd_tipo_rapporto = lCdTipoRap
				  and   cd_anag 		 = aAnagrafico.cd_anag
				  and   dt_fin_validita  = aRapporto.dt_fin_validita;

				 	aRapporto.CD_TIPO_RAPPORTO      :=  cTIPO_RAPPORTO;
				 	aRapporto.CAUSALE_FINE_RAPPORTO := aAnaDip.desc_causa_cess;
				  	aRapporto.CD_ANAG  	    := aAnagrafico.cd_anag;
				  	aRapporto.DT_INI_VALIDITA  	:= aAnaDip.data_assunzione;
				  	aRapporto.MATRICOLA_DIPENDENTE  := aAnadip.matricola;
				  	aRapporto.DT_FIN_VALIDITA	:= nvl(aAnaDip.data_cessazione,to_date('31122200','ddmmyyyy'));
				  	aRapporto.DACR			:= aData;
				  	aRapporto.UTCR			:= aUtente;
				  	aRapporto.DUVA			:= aData;
				  	aRapporto.UTUV			:= aUtente;
				  	aRapporto.PG_VER_REC		:= aRapporto.PG_VER_REC+1;
					aRapporto.CD_ENTE_PREV_STI      := aAnaDip.ente_prev;--- flavia
                                        aRapporto.CD_RAPP_IMPIEGO_STI   := aAnaDip.RAPP_IMPIEGO;
	  				INS_RAPPORTO(aRapporto);

			     else

				    aRapporto.CD_TIPO_RAPPORTO      :=  cTIPO_RAPPORTO;
				  	aRapporto.CD_ANAG  		:= aAnagrafico.cd_anag;
				  	aRapporto.DT_INI_VALIDITA  	:= aAnaDip.data_assunzione;
				  	aRapporto.MATRICOLA_DIPENDENTE  := aAnadip.matricola;
				  	aRapporto.CAUSALE_FINE_RAPPORTO := aAnaDip.desc_causa_cess;
				  	aRapporto.DT_FIN_VALIDITA	:= nvl(aAnaDip.data_cessazione,to_date('31122200','ddmmyyyy'));
				  	aRapporto.DACR			:= aData;
				  	aRapporto.UTCR			:= aUtente;
				  	aRapporto.DUVA			:= aData;
				  	aRapporto.UTUV			:= aUtente;
				  	aRapporto.PG_VER_REC		:= 1;
					  aRapporto.CD_ENTE_PREV_STI      := aAnaDip.ente_prev;  --- flavia
					  aRapporto.CD_RAPP_IMPIEGO_STI   := aAnaDip.RAPP_IMPIEGO;

					  update rapporto
					     set  DT_FIN_VALIDITA 		 = nvl(trunc(aAnaDip.data_assunzione)-1,trunc(aRapporto.dt_ini_validita)-1)
				  	  ,DUVA					 = aData
				  	  ,UTUV					 = aUtente
				  	  ,PG_VER_REC			 = PG_VER_REC +1
				 			 where cd_tipo_rapporto = lCdTipoRap
							  and   cd_anag 		 = aAnagrafico.cd_anag
				 				and   dt_fin_validita  >= nvl(trunc(aAnaDip.data_assunzione)-1,trunc(aRapporto.dt_ini_validita)-1);

	  				INS_RAPPORTO(aRapporto);

				end if;
				--ALLINEA_INQUADRAMENTO(aAnaDip ,aAnagrafico ,aData ,aUtente );
	  end;
 end;



procedure UPD_INQUADRAMENTO (aAnaDip cnr_Anadip%rowtype
 		   					 ,aAnagrafico anagrafico%rowtype
							 ,aRapporto rapporto%rowtype
							 ,aRapportoPrec rapporto%rowtype
							 ,aData date
							 ,aUtente varchar2) is
 lCdTipoRapporto rapporto.cd_tipo_rapporto%type;
 lPgRifInquadramento number;
 lPgRifInquadramentoOld number;
 lDtInizioValiInqPrec date;
 lContinuaEsecuzione boolean := true;
 lInquadrPrec boolean := true;
 lInquadramento inquadramento%rowtype;
 lInquadramentoPrec inquadramento%rowtype;
 lNumInqAperti number;
 conta_inq number;
 dt_up date;
 v_pg_ver_rec number;
 begin
 --Dbms_Output.put_line('UPD_INQUADRAMENTO'||' matricola '||aAnaDip.matricola);
	  lCdTipoRapporto := cTIPO_RAPPORTO;
	  -- Controllo se gli inqudramenti sono validi
	  begin
	 	  select pg_rif_inquadramento
		  into   lPgRifInquadramento
	 	  from rif_inquadramento
	 	  where CD_PROFILO =  aAnaDip.livello_1
		  and   CD_PROGRESSIONE = aAnaDip.livello_2
		  and   cd_livello is null;

 		  lContinuaEsecuzione := true;

	  exception when no_data_found then
				lContinuaEsecuzione := false;
			  	ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Manca il riferimento per inquadramento ', '');
	  end;
	  begin
	 	  select pg_rif_inquadramento
		  into lPgRifInquadramentoOld
	 	  from rif_inquadramento
	 	  where CD_PROFILO =  aAnaDip.livprec_1
		  and   CD_PROGRESSIONE = aAnaDip.livprec_2
		  and   cd_livello is null;

		   lInquadrPrec := true;

	  exception when no_data_found then
				lInquadrPrec := false;
				if aAnaDip.livprec_1 is not null or aAnaDip.livprec_2 is not null then
			  	   ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Manca il riferimento per inquadramento periodo precedente, livprec_1 = '|| nvl(aAnaDip.livprec_1,'NULL') || ', livprec_2 = ' || nvl(aAnaDip.livprec_2,'NULL'), '');
				end if;
	  end;


	  -- Inseriamo o modifichiamo l'inquadramento attuale
	  if  lContinuaEsecuzione then

		  begin

		  	 select count(*)
			   into lNumInqAperti
			   from inquadramento
			   where CD_TIPO_RAPPORTO         = lCdTipoRapporto
			   and 	 CD_ANAG                  = aAnagrafico.cd_anag
			   and   trunc(DT_INI_VALIDITA_RAPPORTO) = trunc(aRapporto.dt_ini_validita)
			   and   PG_RIF_INQUADRAMENTO  	  = lPgRifInquadramento
			   and   trunc(DT_INI_VALIDITA) >=  trunc(aAnadip.giu_dec_inizio);


			if    lNumInqAperti = 0 then

				if trunc(aAnadip.data_cessazione) > trunc(sysdate) or trunc(aAnadip.data_cessazione) is null then

				 	  lInquadramento.CD_TIPO_RAPPORTO         := lCdTipoRapporto;
				 	  lInquadramento.CD_ANAG                  := aAnagrafico.cd_anag;
				 	  lInquadramento.DT_INI_VALIDITA_RAPPORTO := aRapporto.dt_ini_validita;
				 	  lInquadramento.PG_RIF_INQUADRAMENTO     := lPgRifInquadramento;
				 	  lInquadramento.DT_INI_VALIDITA	  	  := aAnadip.giu_dec_inizio;
				 	  lInquadramento.DT_FIN_VALIDITA	   	  := nvl(aAnadip.data_cessazione,to_date('31/12/2200','dd/mm/yyyy'));
				 	  lInquadramento.DACR			  		  := aData;
				 	  lInquadramento.UTCR			  		  := aUtente;
				 	  lInquadramento.DUVA			  		  := aData;
				 	  lInquadramento.UTUV			  		  := aUtente;
				 	  lInquadramento.PG_VER_REC		  		  := 1;

				 	    update Inquadramento
					     set  DT_FIN_VALIDITA 		 = nvl(trunc(aAnaDip.data_assunzione)-1,trunc(aRapporto.dt_ini_validita)-1)
				  	  ,DUVA					 = aData
				  	  ,UTUV					 = aUtente
				  	  ,PG_VER_REC			 = PG_VER_REC +1
				 			 where cd_tipo_rapporto = lCdTipoRapporto
							  and   cd_anag 		 = aAnagrafico.cd_anag
							  and   dt_ini_validita  <= nvl(trunc(aAnaDip.data_assunzione)-1,trunc(aRapporto.dt_ini_validita)-1)
				 				and   dt_fin_validita  >= nvl(trunc(aAnaDip.data_assunzione)-1,trunc(aRapporto.dt_ini_validita)-1);

			 	   INS_INQUADRAMENTO(lInquadramento);
			 	end if;
			 end if;

		  exception
		  when no_data_found then
		  --	dbms_output.put_line('no_data_found per l''anagrafico: '||aAnagrafico.cd_anag);
		  ibmutl200.logInf(gPgLog ,'Matricola ' || aAnaDip.matricola , 'Impossibile inserire l''inquadramento la data di inizio rapporto non è valida', '');
		  end;
	  end if;

 end;

 procedure UPD_MONTANTE (aMontante montanti%rowtype) as
 begin
  --Dbms_Output.put_line('UPD_MONTANTE anag '||aMontante.cd_anag);
 	  update montanti
	  set IRPEF_DIPENDENTI            = aMontante.IRPEF_DIPENDENTI,
		  IRPEF_LORDO_DIPENDENTI	  = aMontante.IRPEF_LORDO_DIPENDENTI,
		  INPS_DIPENDENTI	          = aMontante.INPS_DIPENDENTI,
		  INPS_TESORO_DIPENDENTI  	  = aMontante.INPS_TESORO_DIPENDENTI,
		  --la riduzione erariale viene gestita ed aggiornata in SIGLA quindi non deve essere sovrascritta
		  --RIDUZ_DIPENDENTI     = aMontante.RIDUZ_DIPENDENTI,
		  FONDO_FS_DIPENDENTI	      = aMontante.FONDO_FS_DIPENDENTI,
		  DEDUZIONE_IRPEF_DIPENDENTI  = aMontante.DEDUZIONE_IRPEF_DIPENDENTI,
		  DUVA  			   		  = aMontante.duva,
		  UTUV						  = aMontante.utuv,
		  PG_VER_REC				  = pg_ver_rec + 1
	  where esercizio = aMontante.esercizio
	  and   cd_anag   = aMontante.cd_anag;
 end;

 procedure JOB_AGGIORNA_COORD_BANCARIE(job number, pg_exec number, next_date date, nrGiorniBack number) as
    aTSNow date;
    aUser varchar2(20);
 begin
    aTSNow:=sysdate;
    aUser:=IBMUTL200.getUserFromLog(pg_exec);
    lPgExec := pg_exec;

    -- Aggiorna le info di testata del log
    IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_JOB_NSIP, job, 'Batch di aggiornamento coordinate bancarie dipendenti. Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));

    for recCoordinate in (select * from pea_coordinate
                          where coo_iban is not null
                          and coo_data_op > sysdate-nvl(nrGiorniBack,3)) loop
      declare
        recAnag ANAGRAFICO%ROWTYPE;
        recTerzo TERZO%ROWTYPE;
        recAnadip CNR_ANADIP%ROWTYPE;
      begin
          --recupero il rapporto attivo legato alla matricola
          begin
          	  Select distinct anagrafico.* INTO recAnag
			  From rapporto, anagrafico
			  Where anagrafico.cd_anag = rapporto.cd_anag
              And   dt_fine_rapporto Is Null
              And   matricola_dipendente = Rtrim(recCoordinate.dip_id);

              begin
                 select * into recTerzo
                 from terzo
                 where cd_terzo = (select min(cd_terzo) from terzo where cd_anag = recAnag.cd_anag and dt_fine_rapporto is null);

                 recAnadip.matricola := Rtrim(recCoordinate.DIP_ID);
                 recAnadip.nominativo := Rtrim(recTerzo.DENOMINAZIONE_SEDE);
                 recAnadip.mod_pag := Rtrim(recCoordinate.COO_MOD_PAG);
                 recAnadip.iban_pag := Rtrim(recCoordinate.COO_IBAN);
                 recAnadip.abi_pag := Rtrim(recCoordinate.COO_ABI);
                 recAnadip.cab_pag := Rtrim(recCoordinate.COO_CAB);
                 recAnadip.nrc_pag := Rtrim(recCoordinate.COO_NUM_CONTO);
                 recAnadip.cin_pag := Rtrim(recCoordinate.COO_CIN);

                 IBMUTL015.setRbsBig;

                 MODIFICABANCA(recAnadip, recAnag, recTerzo, aTSNow, aUser);

                 IBMUTL015.commitRbsBig;
              exception when no_data_found then
                 ibmutl200.logErr(pg_exec,'Matricola ' || recCoordinate.dip_id, 'Manca il TERZO associato al record ANAGRAFICO con codice '||recAnag.cd_anag, '');
              end;
          exception
              when no_data_found then
                ibmutl200.logInf(pg_exec,'Matricola ' || recCoordinate.dip_id, 'Dipendente non registrato in SIGLA', '');
              when too_many_rows then
                ibmutl200.logErr(pg_exec,'Matricola ' || recCoordinate.dip_id, 'Troppe anagrafiche attive presenti', '');
            null;
          end;
      end;
    end loop;
    ibmutl200.logInf(pg_exec,'Batch di aggiornamento coordinate bancarie dipendenti.', 'End:'||to_char(sysdate,'YYYY/MM/DD HH-MI-SS'), '');
 end;
end;
