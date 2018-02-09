CREATE OR REPLACE package CNRMIG090 as
--
-- CNRMIG090 - Package di migrazione delle anagrafiche
--
-- Date: 03/02/2002
-- Version: 1.11
--
-- Dependency:
--
-- History:
--
-- Date: 22/11/2002
-- Version: 1.0
-- Creazione
--
-- Date: 26/11/2002
-- Version: 1.1
-- Corretto inserimento del terzo (lTerzo.CAP_COMUNE_SEDE := lComuneSede.cd_cap)
--
-- Date: 02/12/2002
-- Version: 1.2
-- Corretto ricerca comune di default
--
-- Date: 03/12/2002
-- Version: 1.3
-- Corretta il mwetodo di ricera per anagrafico
--
-- Date: 04/12/2002
-- Version: 1.4
-- Corretta il metodo di ricera per anagrafico parte 2
--
-- Date: 06/12/2002
-- Version: 1.5
-- Corretta il metodo di ricera per anagrafico parte 3
--
-- Date: 09/12/2002
-- Version: 1.6
-- Inserita valorizzazione default per intestazione banca
--
-- Date: 16/12/2002
-- Version: 1.7
-- Modificata la ricerca del comune di nascita,
-- inserendo un ulteriore ricerca per codice catastale estratto dal Codice fiscale
--
-- Date: 16/12/2002
-- Version: 1.8
-- Corretta la ricerca del comune di nascita per codice catastale
--
-- Date: 17/12/2002
-- Version: 1.9
-- Inserito exception no_data_found in ricerca del comune di nascita per codice catastale
--
-- Date: 18/12/2002
-- Version: 1.10
-- Inserita ORDER BY nella query di lettura sulla tabella CNR_ANAGRAFICO
-- in modo da rispettare l'ordine di inserimento definito dal timestamp
-- della tabella.
--
-- Date: 03/02/2003
-- Version: 1.11
-- Gestione della eccezione su i dati di pagamento e bancari non trovati
--
-- Constants:
--
cgUtente constant varchar2(20) :='$$$$$MIGRAZIONE$$$$$';
cgData constant date :=sysdate;
gPgLog number;
-- Functions e Procedures:
--
 procedure caricaAnagrafica;

 procedure creaAnagrafico(aAnag cnr_anagrafico%rowtype);

 procedure creaTerzi(aAnagSci cnr_anagrafico%rowtype,aAnagrafico anagrafico%rowtype, aPgLog number default 0);

 procedure creaRecapiti(aTerzo terzo%rowtype, aTerzoSci cnr_terzo%rowtype);

 procedure creaPagamento(aTerzoCir terzo%rowtype, aTerzoSci cnr_terzo%rowtype);

 procedure creaBanca(aTerzo terzo%rowtype, aTerzoSCI cnr_terzo%rowtype);

 function chkPresenzaAnag(aAnag cnr_anagrafico%rowtype, aAnagrafico in out anagrafico%rowtype) return boolean;

 procedure getComuneNascita(aAnag cnr_anagrafico%rowtype,  aComuneNascita IN OUT comune%rowtype);

 procedure getComuneNascitaPerCod (aAnag cnr_anagrafico%rowtype,  aComuneNascitaPerCod IN OUT comune%rowtype);

 procedure getComuneFiscale(aAnag cnr_anagrafico%rowtype,  aComuneFiscale IN OUT comune%rowtype);

 procedure getComuneSede(aTerzoSCI cnr_terzo%rowtype,  aComuneSede IN OUT comune%rowtype);

 procedure getNazionalita(aAnag cnr_anagrafico%rowtype,  aNazionalita IN OUT nazione%rowtype);

 procedure INS_ANAGRAFICO(aAnagrafico anagrafico%rowtype);

 procedure INS_TERZO(aTerzo Terzo%rowtype);

 procedure INS_BANCA (aBanca banca%rowtype);

 procedure INS_PAGAMENTO (aPagamento modalita_pagamento%rowtype);

 procedure INS_RECAPITO (aRecapito telefono%rowtype);
end;
/


CREATE OR REPLACE package body CNRMIG090 is
 procedure caricaAnagrafica is
 lData date;
 lAnagraficoOld anagrafico%rowtype;
 lContaAnag number;
 begin
 	  -- La tabella CNR_ANAGRAFICO in questa fase deve essere gi? caricata con i dati
	  -- relativi alla migrazione.
	  -- La tabella in esame risulta caricata con i dati degli anagrafici
	  -- provenienti dal sistema SCI.
 	  -- Per ogni anagrafico presente nella tabella di lavoro CNR_ANAGRAFICO
	  -- si devono controllare e valorizzare dei campi.
	  lData := sysdate;
 	  gPgLog := ibmutl200.LOGSTART('Migrazione Anagarfica Generale ' , cgUtente, 1, 1);
	  for aAnag in (select * from cnr_anagrafico order by id_anagrafico, timestamp) loop
	  	  begin
		  	   LOCK TABLE cnr_anagrafico IN EXCLUSIVE MODE NOWAIT;
			   lContaAnag := lContaAnag + 1;
			   -- Passo 1
			   -- Controlliamo che il dipendente in esame non risulti gi?
			   -- caricato in anagrafica, il controllo avviene tramite il
			   -- codice fiscale o la partita IVA
		  	   ibmutl200.logInf(gPgLog ,'Processando anagrafico ' || aAnag.id_anagrafico || ' numero '|| lContaAnag,'', '');
			   if chkPresenzaAnag(aAnag, lAnagraficoOld) then
			   	  -- errore
			  	  ibmutl200.logInf(gPgLog ,aAnag.id_anagrafico || ', gi? presente, come cd_anagrafico ' ||lAnagraficoOld.cd_anag , '','');
			   else
			   	  creaAnagrafico(aAnag);
			   end if;
	  	  end;
  	  end loop;
 end;

 procedure creaAnagrafico(aAnag cnr_anagrafico%rowtype) is
 lAnagrafico anagrafico%rowtype;
 lPgAnagrafico number;

 lComuneNascita comune%rowtype;
 lComuneFiscale comune%rowtype;
 lNazioneFiscale nazione%rowtype;
 lNazionalita nazione%rowtype;

 begin
	  LOCK TABLE ANAGRAFICO IN EXCLUSIVE MODE NOWAIT;
 	  -- recuperiamo il progressivo da attribuire alla'angrafico
 	  begin
		  select cd_corrente + 1
		  into lPgAnagrafico
		  from numerazione_base
		  where tabella ='ANAGRAFICO'
		  for update nowait;
	  exception when no_data_found then
	  			ibmerr001.RAISE_ERR_GENERICO('Manca la configurazione della tabella ANAGRAFICO');
	  end;

	  getComuneNascita(aAnag,lComuneNascita);

	  getComuneFiscale(aAnag,lComuneFiscale);

	  select *
	  into lNazioneFiscale
	  from nazione
	  where pg_nazione = lComuneFiscale.pg_nazione;

	  getNazionalita(aAnag,lNazionalita);
-- 	  cnr_anagrafico  anagrafico
 	  lAnagrafico.CD_ANAG                    := lPgAnagrafico;
	  lAnagrafico.CD_CLASSIFIC_ANAG 		 := NULL;
	  lAnagrafico.TI_ENTITA 				 := aAnag.TI_ENTITA;
	  lAnagrafico.TI_ENTITA_FISICA 			 := aAnag.TI_ENTITA_FISICA;
	  lAnagrafico.TI_ENTITA_GIURIDICA 		 := aAnag.TI_ENTITA_GIURIDICA;
	  lAnagrafico.FL_SOGGETTO_IVA 			 := aAnag.FL_SOGGETTO_IVA;
	  lAnagrafico.CODICE_FISCALE 			 := aAnag.CODICE_FISCALE;
	  lAnagrafico.PARTITA_IVA 				 := aAnag.PARTITA_IVA;
	  lAnagrafico.ID_FISCALE_ESTERO 		 := aAnag.ID_FISCALE_ESTERO;
	  lAnagrafico.RAGIONE_SOCIALE 			 := aAnag.RAGIONE_SOCIALE;
	  lAnagrafico.NOME 						 := aAnag.NOME;
	  lAnagrafico.COGNOME 					 := aAnag.COGNOME;
	  lAnagrafico.TI_SESSO 					 := aAnag.TI_SESSO;
	  lAnagrafico.DT_NASCITA 				 := aAnag.DT_NASCITA;
	  lAnagrafico.PG_COMUNE_NASCITA 		 := lComuneNascita.pg_comune;
	  lAnagrafico.VIA_FISCALE 				 := aAnag.VIA_FISCALE;
	  lAnagrafico.NUM_CIVICO_FISCALE 		 := aAnag.NUM_CIVICO_FISCALE;
	  lAnagrafico.PG_COMUNE_FISCALE 		 := lComuneFiscale.pg_comune;
	  lAnagrafico.CAP_COMUNE_FISCALE 		 := lComuneFiscale.cd_cap;
	  lAnagrafico.FRAZIONE_FISCALE 			 := NULL;
	  lAnagrafico.PG_NAZIONE_FISCALE 		 := lNazioneFiscale.pg_nazione;
	  lAnagrafico.PG_NAZIONE_NAZIONALITA 	 := lNazionalita.pg_nazione;
	  lAnagrafico.FL_FATTURAZIONE_DIFFERITA  := 'N';
	  lAnagrafico.FL_OCCASIONALE 			 := 'N';
	  lAnagrafico.TI_ITALIANO_ESTERO 		 := lNazioneFiscale.ti_nazione;
 	  lAnagrafico.CD_ATTIVITA_INPS 			 := NULL;
 	  lAnagrafico.ALTRA_ASS_PREVID_INPS 	 := NULL;
 	  lAnagrafico.ALIQUOTA_FISCALE 			 := NULL;
 	  lAnagrafico.CODICE_FISCALE_CAF 		 := NULL;
 	  lAnagrafico.DENOMINAZIONE_CAF 		 := NULL;
 	  lAnagrafico.SEDE_INAIL 				 := NULL;
 	  lAnagrafico.MATRICOLA_INAIL 			 := NULL;
 	  lAnagrafico.CONTO_NUMERARIO_CREDITO 	 := NULL; -- Non Usare
 	  lAnagrafico.CONTO_NUMERARIO_DEBITO 	 := NULL; -- Non Usare
 	  lAnagrafico.NUM_ISCRIZ_CCIAA 			 := NULL;
 	  lAnagrafico.NUM_ISCRIZ_ALBO 			 := NULL;
 	  lAnagrafico.DT_FINE_RAPPORTO 			 := NULL;
 	  lAnagrafico.CAUSALE_FINE_RAPPORTO 	 := NULL;
 	  lAnagrafico.DT_CANC 					 := NULL;
 	  lAnagrafico.DT_ANTIMAFIA 				 := NULL;
 	  lAnagrafico.CD_ENTE_APPARTENENZA 		 := NULL;
 	  lAnagrafico.NOTE 						 := NULL;
 	  lAnagrafico.DACR 						 := aAnag.timestamp;
 	  lAnagrafico.UTCR 						 := cgUtente;
 	  lAnagrafico.DUVA 						 := aAnag.timestamp;
 	  lAnagrafico.UTUV 						 := cgUtente;
 	  lAnagrafico.PG_VER_REC 				 := 1;

	  -- Inseriamo l'anagrafico
	  INS_ANAGRAFICO(lAnagrafico);

	  update numerazione_base
	  set cd_corrente = cd_corrente +1
	  where tabella ='ANAGRAFICO';

	  creaTerzi(aAnag, lAnagrafico);

 end;

procedure creaTerzi(aAnagSci cnr_anagrafico%rowtype, aAnagrafico anagrafico%rowtype, aPgLog number default 0 ) is
 lTerzo terzo%rowtype;
 lTerzoSCI cnr_terzo%rowtype;
 lComuneSede comune%rowtype;
 lTelefono telefono%rowtype;

 lNumeroTerzi number;
 lPgTerzoIniziale Number;

 begin
 	  lNumeroTerzi :=0;

 	  -- Recuperiamo il progressivo per il terzo
 	  begin
		  select cd_corrente
		  into lPgTerzoIniziale
		  from numerazione_base
		  where tabella ='TERZO'
		  for update nowait;
	  exception when no_data_found then
	  			ibmerr001.RAISE_ERR_GENERICO('Manca la configurazione della tabella TERZO');
	  end;

	  for lTerzoSCI in (select *
		  			    from cnr_terzo
		  				where id_anagrafico = aAnagSci.id_anagrafico
		  				)
	  loop
	  	  LOCK TABLE cnr_terzo IN EXCLUSIVE MODE NOWAIT;

	  	  lNumeroTerzi := lNumeroTerzi + 1;

		  getComuneSede(lTerzoSCI,  lComuneSede);
		  -- cnr_terzo terzo
		  lTerzo.CD_TERZO                 := lPgTerzoIniziale + lNumeroTerzi;
		  lTerzo.FRAZIONE_SEDE  		  := NULL;
		  lTerzo.CD_ANAG 	   			  := aAnagrafico.cd_anag;
		  lTerzo.DT_FINE_RAPPORTO   	  := NULL;
		  lTerzo.TI_TERZO		    	  := 'E';
		  lTerzo.CD_PRECEDENTE	    	  := lTerzoSCI.cd_precedente;
		  lTerzo.DENOMINAZIONE_SEDE 	  := lTerzoSCI.DENOMINAZIONE_SEDE;
		  lTerzo.VIA_SEDE				  := lTerzoSCI.VIA_SEDE;
		  lTerzo.NUMERO_CIVICO_SEDE 	  := lTerzoSCI.NUM_CIVICO_SEDE;
		  lTerzo.PG_COMUNE_SEDE     	  := lComuneSede.Pg_Comune;
		  lTerzo.CAP_COMUNE_SEDE 		  := lComuneSede.cd_cap;
		  lTerzo.PG_RAPP_LEGALE			  := NULL;
		  lTerzo.CD_UNITA_ORGANIZZATIVA   := NULL;
		  lTerzo.NOME_UNITA_ORGANIZZATIVA := NULL;
		  lTerzo.NOTE 					  := NULL;
		  lTerzo.DT_CANC 				  := NULL;
		  lTerzo.DACR 					  := lTerzoSCI.timestamp;
		  lTerzo.UTCR 					  := cgUtente;
		  lTerzo.DUVA 					  := lTerzoSCI.timestamp;
		  lTerzo.UTUV 					  := cgUtente;
		  lTerzo.PG_VER_REC 			  := 1;

		  ins_terzo(lTerzo);

		  update numerazione_base
		  set cd_Corrente = cd_Corrente +1
		  where tabella ='TERZO';

		  if gPgLog is null then
		  	 gPgLog := aPgLog;
		  end if;
		  creaRecapiti(lTerzo, lTerzoSci);

		  creaPagamento(lTerzo, lTerzoSCI);

		  creaBanca(lTerzo, lTerzoSCI);


	  end loop;

	  if lNumeroTerzi = 0 then
	  	 ibmerr001.RAISE_ERR_GENERICO('Manca il terzo per l''anagrafico SCI '|| aAnagSci.id_anagrafico);
	  end if;


 end;

 procedure creaRecapiti(aTerzo terzo%rowtype, aTerzoSci cnr_terzo%rowtype) is
 lPgIniziale number;
 lContatore number;

 lTelefono telefono%rowtype;
 lFax telefono%rowtype;
 lMail telefono%rowtype;
 begin
	 select nvl(max(PG_RIFERIMENTO),0)
	 into lPgIniziale
	 from telefono
	 where cd_terzo =  aTerzo.cd_terzo;

	 lContatore := 1;

	 if aTerzoSci.num_telefono is not null then
		lTelefono.CD_TERZO:= aTerzo.cd_terzo;
	    lTelefono.PG_RIFERIMENTO := lPgIniziale + lContatore;
		lTelefono.DS_RIFERIMENTO:= 'NUMERO TELEFONO';
		lTelefono.TI_RIFERIMENTO := 'T';
		lTelefono.RIFERIMENTO := aTerzoSci.num_telefono;
		lTelefono.DACR := aTerzoSci.timestamp;
		lTelefono.UTCR := cgUtente ;
		lTelefono.DUVA := aTerzoSci.timestamp;
		lTelefono.UTUV := cgUtente;
		lTelefono.PG_VER_REC := 1;

	 	ins_Recapito (lTelefono);

		lContatore := lContatore + 1 ;
	 end if;

	 if aTerzoSci.num_fax is not null then
		lFax.CD_TERZO:= aTerzo.cd_terzo;
	    lFax.PG_RIFERIMENTO := lPgIniziale + lContatore;
		lFax.DS_RIFERIMENTO:= 'NUMERO FAX';
		lFax.TI_RIFERIMENTO := 'F';
		lFax.RIFERIMENTO := aTerzoSci.num_fax;
		lFax.DACR := aTerzoSci.timestamp;
		lFax.UTCR := cgUtente ;
		lFax.DUVA := aTerzoSci.timestamp;
		lFax.UTUV := cgUtente;
		lFax.PG_VER_REC := 1;

	 	ins_Recapito (lFax);

		lContatore := lContatore + 1 ;
	 end if;

	 if aTerzoSci.indirizzo_e_mail is not null then
		lMail.CD_TERZO:= aTerzo.cd_terzo;
	    lMail.PG_RIFERIMENTO := lPgIniziale + lContatore;
		lMail.DS_RIFERIMENTO:= 'INDIRIZZO E MAIL';
		lMail.TI_RIFERIMENTO := 'E';
		lMail.RIFERIMENTO := aTerzoSci.indirizzo_e_mail;
		lMail.DACR := aTerzoSci.timestamp;
		lMail.UTCR := cgUtente ;
		lMail.DUVA := aTerzoSci.timestamp;
		lMail.UTUV := cgUtente;
		lMail.PG_VER_REC := 1;

	 	ins_Recapito (lMail);

	 end if;

 end;

 procedure creaPagamento(aTerzoCir terzo%rowtype, aTerzoSci cnr_terzo%rowtype) is
 lRifPagamento rif_modalita_pagamento%rowtype;
 lModValida boolean;
 lModalitaPagamento modalita_pagamento%rowtype;
 lNumPagamenti number;
 begin
 	  select count(*)
	  into lNumPagamenti
	  from cnr_pagamento
	  where id_anagrafico = aTerzoSCI.id_anagrafico
	  and progressivo = aTerzoSCI.progressivo;

	  if lNumPagamenti > 0 then

	 	  for lPagamentoSci in (select * from cnr_pagamento
		  	  			    where id_anagrafico = aTerzoSCI.id_anagrafico
							and progressivo = aTerzoSCI.progressivo )
		  loop
		  	  LOCK TABLE cnr_pagamento IN EXCLUSIVE MODE NOWAIT;

		  	  lModValida := true;
			  -- Controlliamo se la modalita di pagamento ? valida
			  begin
				   select *
				   into lRifPagamento
				   from rif_modalita_pagamento
				   where cd_modalita_pag = lPagamentoSci.cd_modalita_pag
				   for update nowait;
			  exception when no_data_found then
			  			lModValida := false;
			  end;

			  if lModValida then
			  	 lModalitaPagamento.CD_TERZO   		   := aTerzoCir.cd_terzo;
				 lModalitaPagamento.CD_MODALITA_PAG	   := lPagamentoSci.cd_modalita_pag;
				 lModalitaPagamento.CD_TERZO_DELEGATO  := NULL;
				 lModalitaPagamento.DACR			   := lPagamentoSci.timestamp;
				 lModalitaPagamento.UTCR			   := cgUtente;
				 lModalitaPagamento.DUVA			   := lPagamentoSci.timestamp;
				 lModalitaPagamento.UTUV			   := cgUtente;
				 lModalitaPagamento.PG_VER_REC		   := 1;

				 ins_pagamento(lModalitaPagamento);
			  else
			  	 ibmutl200.logInf(gPgLog ,aTerzoSCI.id_anagrafico || ', Il cd_terzo = ' || aTerzoCir.cd_terzo || ' Risulta non avere modalita di pagamento valida, in RIF_MODALITA_PAGAMENTO non esiste cd_modalita_pag = ' || lPagamentoSci.cd_modalita_pag ,'', '');
			  end if;
	 	  end loop;
	  else
	  	  ibmutl200.logInf(gPgLog ,aTerzoSCI.id_anagrafico || ', Il cd_terzo = ' || aTerzoCir.cd_terzo || ' Risulta non avere modalita di pagamento'  ,'', '');
	  end if;

 end;

 procedure creaBanca(aTerzo terzo%rowtype, aTerzoSci cnr_terzo%rowtype) is
 lNumBanche number;
 lBancaValida boolean;
 lBancaCir banca%rowtype;
 lNumBancaPerAbiCabConto number;
 lBancaGiaInserita boolean;
 lContBanche number;
 begin
 	  select count(*)
	  into lContBanche
	  from cnr_banca
	  where id_anagrafico = aTerzoSCI.id_anagrafico
	  and progressivo = aTerzoSCI.progressivo;

	  if lContBanche > 0 then
	 	  for lBancaSci in (select * from cnr_banca
		  	  			    where id_anagrafico = aTerzoSCI.id_anagrafico
							and progressivo = aTerzoSCI.progressivo )
		  loop

			  LOCK TABLE cnr_banca IN EXCLUSIVE MODE NOWAIT;

			  if lBancaSci.Ti_pagamento ='B' then
				  -- Controlliamo se la banca ? valida
			 	  select count(*)
				  into lNumBanche
				  from abicab
				  where abi = lBancaSci.abi
				  and   cab = lBancaSci.cab;
				  if lNumBanche > 0 then
				  	 lBancaValida := true;
				  else
				  	 lBancaValida := false;
				  end if;
			  else
			  	  lBancaValida := true;
			  end if;


			  if lBancaValida then
			  	 -- controlliamo se per il terzo in esame risulta gia inserita la banca
				 select count(*)
				 into lNumBancaPerAbiCabConto
				 from banca
				 where abi = lBancaSci.abi
				 and   cab = lBancaSci.cab
				 and   cd_terzo = aTerzo.cd_terzo
				 and   numero_conto = lBancaSci.numero_conto;

				 if lNumBancaPerAbiCabConto > 0 then
				 	lBancaGiaInserita := true;
				 else
				 	lBancaGiaInserita := false;
				 end if;

				 if not lBancaGiaInserita then
				  	 -- recuperiamo il progreassivo della banca
				  	 select nvl(max(pg_banca),0) + 1
					 into lBancaCir.pg_banca
					 from banca
					 where cd_terzo = aTerzo.cd_terzo;

					 lBancaCir.CD_TERZO  		  := aTerzo.cd_terzo;
					 lBancaCir.CAB				  := lBancaSci.cab;
					 lBancaCir.ABI				  := lBancaSci.abi;
					 lBancaCir.TI_PAGAMENTO	  := lBancaSci.ti_pagamento;
					 lBancaCir.INTESTAZIONE	  := nvl(lBancaSci.INTESTAZIONE,'NON DISPONIBILE PER TERZO MIGRATO');
					 lBancaCir.QUIETANZA		  := lBancaSci.QUIETANZA;
					 lBancaCir.NUMERO_CONTO	  := lBancaSci.NUMERO_CONTO;
					 lBancaCir.CODICE_IBAN		  := NULL;
					 lBancaCir.CODICE_SWIFT	  := NULL;
					 lBancaCir.DACR			  := lBancaSci.timestamp;
					 lBancaCir.UTCR			  := cgUtente;
					 lBancaCir.DUVA			  := lBancaSci.timestamp;
					 lBancaCir.UTUV			  := cgUtente;
					 lBancaCir.PG_VER_REC		  := 1;
					 lBancaCir.FL_CANCELLATO	  := 'N';
					 lBancaCir.CD_TERZO_DELEGATO := NULL;
					 lBancaCir.PG_BANCA_DELEGATO := NULL;
					 lBancaCir.ORIGINE			  := 'O';
					 lBancaCir.FL_CC_CDS		  := 'N';

					 -- inseriamo la banca
					 INS_BANCA(lBancaCir);
				 else
			  	   ibmutl200.logInf(gPgLog ,lBancaSci.id_anagrafico || ', La banca con abi' || lBancaSci.abi || ' e cab ' || lBancaSci.CAB ||' risulta gia inserita ', '', '');
				 end if;
			  else
			  	ibmutl200.logInf(gPgLog ,lBancaSci.id_anagrafico || ', La banca con abi' || lBancaSci.abi || ' e cab ' || lBancaSci.CAB ||' non ? valida ', '', '');
			  end if;
	 	  end loop;
	  else
	  	  ibmutl200.logInf(gPgLog ,aTerzoSCI.id_anagrafico ||' non ha banche ', '', '');
	  end if;
 end;

 function chkPresenzaAnag(aAnag cnr_anagrafico%rowtype, aAnagrafico in out anagrafico%rowtype) return boolean is
 lNumCF NUMBER;
 lNumPI NUMBER;
 lGiaPresente boolean:=false;
 begin
 	  if aAnag.codice_fiscale is not null then
	 	  select count(*)
		  into lNumCF
		  from anagrafico
		  where CODICE_FISCALE = aAnag.codice_fiscale ;

  	  	  if lNumCF > 0 then
		 	  select *
			  into aAnagrafico
			  from anagrafico
			  where (CODICE_FISCALE = aAnag.codice_fiscale)
			  and rownum =1
			  for update nowait;

		  	  lGiaPresente := true;
	 	  else
		  	  lGiaPresente := false;
		  end if;

	  else
	  	  if aAnag.PARTITA_IVA is not null then
		  	 select count(*)
		  	 into lNumPI
		  	 from anagrafico
		  	 where (PARTITA_IVA = aAnag.PARTITA_IVA) ;
		  	 if lNumPI >0 then
		 	 	select *
			 	into aAnagrafico
			 	from anagrafico
			 	where (PARTITA_IVA = aAnag.PARTITA_IVA)
			 	and rownum =1
			 	for update nowait;

			 	lGiaPresente := true;
		  	 else
			 	lGiaPresente := false;
		     end if;
	  	  end if;
	  end if;

 	  if aAnag.codice_fiscale is null and aAnag.PARTITA_IVA is null then
	  	 lGiaPresente := true;
		 ibmutl200.logInf(gPgLog ,aAnag.id_anagrafico || ', il codice fiscale e la Partita iva non sono valorizzati ', '', '');
	  end if;

	  return lGiaPresente;

 end chkPresenzaAnag;

 procedure getComuneNascita(aAnag cnr_anagrafico%rowtype,  aComuneNascita IN OUT comune%rowtype) is
 lComuneNascitaPerCod comune%rowtype;
 begin
      -- Recuperiamo il codice e cap del comune di nascita
 	  select com.*
	  into   aComuneNascita
      from   Comune com
      where  ds_comune = trim(aAnag.ds_comune_nascita)
	  and rownum=1;
 exception when no_data_found then
 		   		getComuneNascitaPerCod (aAnag ,  lComuneNascitaPerCod );
	  			-- Selezioniamo il comune di Default
		 	    select com.*
			    into   aComuneNascita
		        from   Comune com
		        where  pg_comune = 0;

				if lComuneNascitaPerCod.pg_comune = aComuneNascita.pg_comune then
			  	   ibmutl200.logInf(gPgLog ,aAnag.id_anagrafico || ' Associato con Comune Nascita di default','', '');
				else
				   aComuneNascita := lComuneNascitaPerCod;
				end if;
 end;

 procedure getComuneNascitaPerCod (aAnag cnr_anagrafico%rowtype,  aComuneNascitaPerCod IN OUT comune%rowtype) as
 lCodComune varchar2(4);
 lComuneDefault comune%rowtype;
 lComune comune%rowtype;
 begin
 	  begin
		  select *
		  into lComuneDefault
		  from comune
		  where pg_comune =0
		  and rownum =1;
 	  exception when no_data_found then
	  		lComuneDefault.pg_comune := 0;
 	  end;
 	  if (aAnag.codice_fiscale is not null) and length(aAnag.codice_fiscale) = 16 then
	   	 lCodComune := substr(aAnag.codice_fiscale,12,4);
		 begin
			 select  *
			 into lComune
			 from   comune
			 where  cd_catastale = lCodComune
			 and rownum = 1;
		 exception when no_data_found then
		 		   lComune := lComuneDefault;
		 end;
 	  else
	  	  lComune := lComuneDefault;
	  end if;
 	  aComuneNascitaPerCod := lComune;
 end;

 procedure getComuneFiscale(aAnag cnr_anagrafico%rowtype,  aComuneFiscale IN OUT comune%rowtype) is
 begin
      -- Recuperiamo il codice e cap del comune di nascita
 	  select com.*
	  into   aComuneFiscale
      from   Comune com
      where  ds_comune = trim(aAnag.ds_comune_fiscale)
	  and rownum=1;
 exception when no_data_found then
	  			-- Comune non definito
		 	    select com.*
			    into   aComuneFiscale
		        from   Comune com
		        where  pg_comune = 0;

			  	ibmutl200.logInf(gPgLog ,aAnag.id_anagrafico || ' Associato con Comune Fiscale di default','', '');
 end;

 procedure getComuneSede(aTerzoSCI cnr_terzo%rowtype,  aComuneSede IN OUT comune%rowtype) is
 begin
      -- Recuperiamo comune della sede
 	  select com.*
	  into   aComuneSede
      from   Comune com
      where  ds_comune = trim(aTerzoSCI.ds_comune_sede)
	  and rownum=1;
 exception when no_data_found then
	  			-- Comune non definito
		 	    select com.*
			    into   aComuneSede
		        from   Comune com
		        where  pg_comune = 0;

			  	ibmutl200.logInf(gPgLog ,aTerzoSCI.id_anagrafico || ' - '  || aTerzoSCI.progressivo || ' Associato con Comune Sede di default','', '');
 end;

   procedure getNazionalita(aAnag cnr_anagrafico%rowtype,  aNazionalita IN OUT nazione%rowtype) is
   begin
        -- Recuperiamo il codice e cap del comune di nascita
   	  select naz.*
  	  into   aNazionalita
	  from   Nazione naz
      where  ds_nazione = trim(aAnag.ds_nazionalita)
  	  and rownum=1;
   exception when no_data_found then
  	  			-- Comune non definito
  		 	    select naz.*
  			    into   aNazionalita
  		        from   Nazione naz
  		        where  pg_nazione = 0;

  			  	ibmutl200.logInf(gPgLog ,aAnag.id_anagrafico || ' Associato con Nazionalita di default','', '');
   end;

 procedure INS_ANAGRAFICO(aAnagrafico anagrafico%rowtype) is
 begin
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
 end;

 procedure INS_TERZO(aTerzo Terzo%rowtype) is
 begin
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
 end;

 procedure INS_BANCA(aBanca banca%rowtype) is
 begin
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
						 FL_CC_CDS )
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
						 aBanca.FL_CC_CDS);
 end;

 procedure INS_PAGAMENTO (aPagamento modalita_pagamento%rowtype) is
 begin
	INSERT INTO MODALITA_PAGAMENTO
		(CD_TERZO,
		CD_MODALITA_PAG,
		CD_TERZO_DELEGATO,
		DACR,
		UTCR,
		DUVA,
		UTUV,
		PG_VER_REC)
	VALUES ( aPagamento.cd_terzo,
		     aPagamento.cd_modalita_pag ,
			 aPagamento.cd_terzo_delegato,
			 aPagamento.dacr,
			 aPagamento.utcr,
			 aPagamento.duva,
			 aPagamento.utuv,
			 aPagamento.pg_ver_rec);

 end;

 procedure INS_RECAPITO (aRecapito telefono%rowtype) is
 begin
 	  insert into telefono (
	  		CD_TERZO,
	    	PG_RIFERIMENTO,
			DS_RIFERIMENTO,
			TI_RIFERIMENTO,
			RIFERIMENTO,
			DACR,
			UTCR,
			DUVA,
			UTUV,
			PG_VER_REC)
	  values(
		aRecapito.CD_TERZO,
	    aRecapito.PG_RIFERIMENTO ,
		aRecapito.DS_RIFERIMENTO,
		aRecapito.TI_RIFERIMENTO,
		aRecapito.RIFERIMENTO,
		aRecapito.DACR ,
		aRecapito.UTCR,
		aRecapito.DUVA,
		aRecapito.UTUV,
		aRecapito.PG_VER_REC);


 end;

end;
/


