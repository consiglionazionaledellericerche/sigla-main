CREATE OR REPLACE package CNRMAR050 as
--
-- CNRMAR050 - Package di controllo di quadratura del fondo economale
-- Date: 16/10/2003
-- Version: 1.0
--
-- Dependency:
--
-- History:
--
-- Date: 16/10/2003
-- Version: 1.0
-- Creazione
--
-- Constants
--
TIPO_LOG_CHK_FONDO CONSTANT VARCHAR2(20):='CHK_FONDO_ECO';
--NOTA_CREDITO CONSTANT CHAR(1):='C';
--STATO_ANNULLATO CONSTANT CHAR(1):='A';
--STATO_DEFINITIVO CONSTANT CHAR(1):='D';
-- Tipi di disallineamenti supportati

D_FONDO CONSTANT VARCHAR2(10):='D_FONDO';
D_FONDO000 CONSTANT VARCHAR2(70):='D_FONDO000-IM_TOT_SPESE<>SUM(SPESE)-SUM(REINTEGRI)';
D_FONDO005 CONSTANT VARCHAR2(70):='D_FONDO005-IM_TOT_NET_SPESE<>SUM(NETTO_SPESE)';
D_FONDO010 CONSTANT VARCHAR2(70):='D_FONDO010-IM_TOT_REINTEGRI<>SUM(REINTEGRI)';
D_FONDO015 CONSTANT VARCHAR2(70):='D_FONDO015-IM_AMM_INIZIALE<>MANDATOAPERTURA.IM_AMNDATO';
D_FONDO020 CONSTANT VARCHAR2(70):='D_FONDO020-IM_AMM_FONDO<>MAN_APE.IM_MAN+SUM(MAN_REI.IM_MAN)';
D_FONDO025 CONSTANT VARCHAR2(70):='D_FONDO025-IM_RES_FONDO<>MAN_APE+TOT_MAN_REI.IM_MAN-SPESE+REIN';

D_SPESA000 CONSTANT VARCHAR2(70):='D_SPESA000-STATO_PAGAMENTO_FONDO';
D_SPESA005 CONSTANT VARCHAR2(70):='D_SPESA005-DATA_PAGAMENTO_FONDO';
--
--
-- Parametri:
-- aEs -> Esercizio
-- aCDS -> cd_cds
--
-- Functions e Procedures
--
 procedure job_mar_primi00(job number, pg_exec number, next_date date, aEsercizio number, aCdCds varchar2);

 function MSG_DIS_PRIMI(aTipo varchar2,aFondo fondo_Economale%rowtype,aNota varchar2) return varchar2;

end;


CREATE OR REPLACE package body CNRMAR050 is

 function fnum(anum number) return varchar2 is
 begin
  	  return to_char(anum,'9999999999999D99');
 end;

 function descFondo(aFondo fondo_economale%rowtype) return varchar2 is
 begin
  	  return 'FONDO cds:'||aFondo.cd_cds||' esercizio:'||aFondo.esercizio||' uo:'||aFondo.cd_unita_organizzativa||' cd_fondo:'||aFondo.cd_codice_fondo;
 end;

 function descSpesa(aSpesa fondo_spesa%rowtype) return varchar2 is
 begin
  	  return 'SPESA cds:'||aSpesa.cd_cds||' esercizio:'||aSpesa.esercizio||' uo:'||aSpesa.cd_unita_organizzativa||' cd_fondo:'||aSpesa.cd_codice_fondo||' pg_spesa:'||aSpesa.pg_fondo_spesa;
 end;

 function MSG_DIS_PRIMI(aTipo varchar2,aFondo fondo_Economale%rowtype,aNota varchar2) return varchar2 is
 aOut varchar2(1000);
 begin
  	  aOut:=aTipo||' '||descFondo(aFondo)||' '||aNota;
  	  return aOut;
 end;

 function MSG_DIS_PRIMI(aTipo varchar2,aSpesa fondo_spesa%rowtype,aNota varchar2) return varchar2 is
 aOut varchar2(1000);
 begin
  	  aOut:=aTipo||' '||descSpesa(aSpesa)||' '||aNota;
  	  return aOut;
 end;


 procedure job_mar_primi00(job number, pg_exec number, next_date date, aEsercizio number, aCdCds varchar2) is
  lPgExec number;
  lTSNow date;
  lUser varchar2(20);
  lEndT date;
  lStartT date;
  lEnd varchar2(80);
  lStart varchar2(80);
  lMsgTipoMar varchar2(30);
  lCds unita_organizzativa%rowtype;
  lNumFondi number;
  lNumTotSpese number;
  lNumSpese number;
  lTotSpeseNonReint number(15,2);
  lTotReintegri number(15,2);
  lTotNettoSpese number(15,2);
  lTotMandatiReintegro number(15,2);
  lDelta varchar2(80);
  lMandatoApertura mandato%rowtype;
  lNumDocAmmNonValidi number;
  begin -- MAIN
  	  lTSNow:=sysdate;

	  -- recuperiamo utente connessione
	  select user
	  into lUser
	  from dual;
--  	  lUser:=IBMUTL200.getUserFromLog(pg_exec);
	  -- impostiamo msg output
	  lMsgTipoMar:='VERIFICA';
	  -- impostiamo la data di inizio del processo di controllo
  	  lStartT:=sysdate;
  	  lStart:=to_char(sysdate,'YYYYMMDD HH:MI:SS');

	  if Pg_exec is null then
	 	 lPgExec := ibmutl200.LOGSTART('Procedura per il controllo della quadratura del fondo economale' , lUser, null, null);
	  else
     	 lPgExec := Pg_exec;
	  end if;

	  IBMUTL200.logInf(lPgExec,'Controllo Quadratura Fondo Economale START  at: '||lStart||' cd_cds.'|| aCdCds||' esercizio.'||aEsercizio,aEsercizio||aCdCds,NULL);

	  -- recuperiamo il pg_esecuzione per inserire le righe di log
  	  IBMUTL210.logStartExecutionUpd(lPgExec,
	  								 TIPO_LOG_CHK_FONDO,
									 job,
									 D_FONDO||'-'||lMsgTipoMar||' (disallinamenti fondo economale). Start:'||to_char(lTSNow,'YYYY/MM/DD HH-MI-SS'),
									 D_FONDO);

  	  -- recuperiamo le informazioni del cd_cds
	  begin
	  	   select *
		   into lCds
		   from unita_organizzativa
		   where cd_unita_organizzativa = aCdCds
		   and fl_cds = 'Y'
		   for update nowait;
  	  exception
  	  when NO_DATA_FOUND then
	  	   IBMERR001.RAISE_ERR_GENERICO('Cds non trovato:'||aCdCds);
  	  end ;

	  begin -- Corpo Controllo fondo
			lNumTotSpese := 0;
			lNumFondi := 0;
		    for lFondo in (select *
					   	   from fondo_economale
					 	   where esercizio = aEsercizio
						   and cd_cds = aCdCds
						   for update nowait)
		    loop  -- fondo
		  		lNumFondi := lNumFondi + 1;
				lTotSpeseNonReint := 0;
				lTotReintegri := 0;
				lTotNettoSpese := 0;
				lTotMandatiReintegro := 0;

				select nvl(sum(decode(fl_reintegrata,'Y',im_ammontare_spesa,0)),0),nvl(sum( decode(fl_reintegrata,'Y',0,im_ammontare_spesa)),0),sum(im_netto_spesa), count(*)
				into lTotReintegri, lTotSpeseNonReint, lTotNettoSpese, lNumSpese
				from fondo_spesa
				where cd_cds = lFondo.cd_cds
				and   esercizio = lFondo.esercizio
				and   cd_unita_organizzativa = lFondo.cd_unita_organizzativa
				and   cd_codice_fondo = lFondo.cd_codice_fondo;

		  		lNumTotSpese := lNumTotSpese + lNumSpese;

				-- controllo D_FONDO000 : FONDO.IM_TOTALE_SPESE = SUM(SPESE)
				if lFondo.im_totale_spese <> lTotSpeseNonReint  then
				   IBMUTL200.logInf(lPgExec,
				   					MSG_DIS_PRIMI(D_FONDO000,lFondo,/*nessuna nota*/''),
									'delta:'||fnum(lFondo.im_totale_spese - lTotSpeseNonReint),
									null);
				end if;

				-- controllo D_FONDO005 : FONDO.IM_TOTALE_NETTO_SPESE = SUM(NETTO_SPESE)
				if lFondo.im_totale_netto_spese <> lTotNettoSpese then
				   IBMUTL200.logInf(lPgExec,
				   					MSG_DIS_PRIMI(D_FONDO005,lFondo,/*nessuna nota*/''),
									'delta:'||fnum(lFondo.im_totale_netto_spese - lTotNettoSpese ),
									null);
				end if;

				-- controllo D_FONDO010 : FONDO.IM_TOTALE_REINTEGRI = SUM(REINTEGRI)
				if lFondo.im_totale_reintegri <> lTotReintegri then
				   IBMUTL200.logInf(lPgExec,
				   					MSG_DIS_PRIMI(D_FONDO010,lFondo,/*nessuna nota*/''),
									'delta:'||fnum(lFondo.im_totale_reintegri - lTotReintegri),
									null);
				end if;

				-- controllo D_FONDO015 : FONDO.IM_AMMONTARE_INIZIALE = MANDATO_APERTURA.IM_MANDATO
				-- recuperiamo il mandato di apertura fondo_economale
				begin
					select *
					into lMandatoApertura
					from mandato
					where cd_cds = lFondo.cd_cds
					and   esercizio = lFondo.esercizio
					and   pg_mandato = lFondo.pg_mandato
					for update nowait;

					-- controllo D_FONDO015 : FONDO.IM_AMMONTARE_INIZIALE = MANDATO_APERTURA.IM_MANDATO
					if lFondo.im_ammontare_iniziale <> lMandatoApertura.im_mandato then
					   IBMUTL200.logInf(lPgExec,
					   					MSG_DIS_PRIMI(D_FONDO015,lFondo,/*nessuna nota*/''),
										'delta:'||fnum(lFondo.im_ammontare_iniziale - lMandatoApertura.im_mandato),
										null);
					end if;
				exception
				when no_data_found then
	   				 IBMERR001.RAISE_ERR_GENERICO('Mandato di apertura non trovato per il fondo :' || descFondo(lFondo));
				end;

				-- Calcolo somma mandati di reintegro fondo
				begin
					 select nvl(sum(man.im_mandato),0)
					 into lTotMandatiReintegro
					 from ass_fondo_eco_mandato ass, mandato man
					 where ass.cd_cds_mandato         = man.cd_cds
					 and   ass.esercizio_mandato      = man.esercizio
					 and   ass.pg_mandato 			  = man.pg_mandato
					 and   ass.cd_cds 				  = lFondo.cd_cds
					 and   ass.esercizio 			  = lFondo.esercizio
					 and   ass.cd_unita_organizzativa = lFondo.cd_unita_organizzativa
					 and   ass.cd_codice_fondo 		  = lFondo.cd_codice_fondo;
				exception
				when no_data_found then
					 lTotMandatiReintegro := 0;
					 IBMUTL200.logInf(lPgExec,
					   				  MSG_DIS_PRIMI(D_FONDO,lFondo,/*nessuna nota*/'Non esistono mandati di reintegro'),
									  'delta: 0',
									  null);
				end;

				-- controllo D_FONDO020 : FONDO.IM_AMMONTARE_FONDO = MANDATO_APERTURA.IM_MANDATO + SUM(MANDATI_REINTEGRO)
				if lFondo.im_ammontare_fondo <> lMandatoApertura.im_mandato + lTotMandatiReintegro then
				   IBMUTL200.logInf(lPgExec,
				   					MSG_DIS_PRIMI(D_FONDO020,lFondo,/*nessuna nota*/''),
									'delta:'||fnum(lFondo.im_ammontare_fondo - lMandatoApertura.im_mandato - lTotMandatiReintegro),
									null);
				end if;

				-- controllo D_FONDO025 :
				-- FONDO.IM_RESIDUO_FONDO = MANDATO_APERTURA.IM_MANDATO + SUM(MANDATI_REINTEGRO)
				-- 						    - SUM(SPESE) + SUM(REINTEGRI)
				if lFondo.im_residuo_fondo <> lMandatoApertura.im_mandato + lTotMandatiReintegro - lTotSpeseNonReint then
				   IBMUTL200.logInf(lPgExec,
				   					MSG_DIS_PRIMI(D_FONDO025,lFondo,/*nessuna nota*/''),
									'delta:'||fnum(lFondo.im_residuo_fondo - lMandatoApertura.im_mandato - lTotMandatiReintegro + lTotSpeseNonReint),
									null);
				end if;

			    begin -- Inizio Controllo spese documentate
					 lNumDocAmmNonValidi := 0;
					 for lSpesa in (select *
						 		    from fondo_spesa
									where cd_cds                 = lFondo.Cd_Cds
									and   esercizio 	         = lFondo.Esercizio
									and   cd_unita_organizzativa = lFondo.cd_unita_organizzativa
									and   cd_codice_fondo 		 = lFondo.cd_codice_fondo
									and   fl_documentata 		 = 'Y'
									for update nowait)
					 loop
					 	 select COUNT(*)
						 into lNumDocAmmNonValidi
						 from  v_fondo_doc_amm
						 where cd_tipo_documento_amm = lSpesa.cd_tipo_documento_amm
						 and   cd_cds = lSpesa.cd_cds_doc_amm
						 and   esercizio = lSpesa.esercizio_doc_amm
						 and   cd_unita_organizzativa = lSpesa.cd_uo_doc_amm
						 and   pg_documento = lSpesa.pg_documento_amm
						 and   (st_pagamento_fondo <> 'R' or dt_pagamento_fondo is null);

						 if lNumDocAmmNonValidi > 0 then
					   	 	IBMUTL200.logInf(lPgExec,
					   						 MSG_DIS_PRIMI(D_SPESA000,lSpesa,/*nessuna nota*/''),
											 'La data di pagamento e/o lo stato del fondo di '|| lNumDocAmmNonValidi || ' documenti amministrativi non sono validi',
											 null);

						 end if;
					 end loop;
				  end; -- Fine Controllo spese documentate

		    end loop;  -- fondo

	  end; -- Fine Corpo Controllo fondo


	  lEndT:=sysdate;
	  lEnd:=to_char(lEndT,'YYYYMMDD HH:MI:SS');
	  lDelta:=to_char((lEndT-lStartT)*24*3600,'999999');
	  IBMUTL200.logInf(lPgExec,
		   			   D_FONDO||'-PROCESSATI '||lNumFondi||' FONDI + '||lNumTotSpese||' SPESE',
					   aEsercizio||aCdCds,
					   NULL);
	  IBMUTL200.logInf(lPgExec,
		   			   D_FONDO||'-END at: '||lEnd||' tot exec time(s):'||lDelta||' es.'||aEsercizio||' cds.'||aCdCds,
		   			   aEsercizio||aCdCds,
		   			   NULL);
  exception -- MAIN
  when others then
	   rollback;
	   IBMUTL200.logErr(lPgExec,
		   			    SQLERRM(SQLCODE),
					    DBMS_UTILITY.FORMAT_ERROR_STACK,
					    NULL);
  end; -- MAIN
end;


