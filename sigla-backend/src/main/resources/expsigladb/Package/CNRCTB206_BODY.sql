--------------------------------------------------------
--  DDL for Package Body CNRCTB206
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB206" is

 -- Verifica che l'esercizio finanziario sia chiuso per tutti i CDS
 function checkChiusuraEsFin(pg_exec number, aEs number) return boolean is
 begin
	  for aLCds in (select * from v_unita_organizzativa_valida where fl_cds = 'Y' and esercizio = aEs) loop
       -- Se l'esercizio finanziario non è chiuso per tutti i cds la modalita diventa di prova
       if not CNRCTB008.ISESERCIZIOCHIUSO(aEs,aLCds.cd_unita_organizzativa) then
        IBMUTL200.logErr(pg_exec, 'Non tutti gli esercizio finanziari sono chiusi: la modalità di esecuzione rimane in prova',null,null);
		return false;
       end if;
      end loop;
      return true;
 end;

 procedure chiusuraCogeCoanCds(pg_exec number, modalitaProva char, aEs number, aCdCds varchar2, aUser varchar2, aTSNow date) is
  aCCoep chiusura_coep%rowtype;
  aNumRigaStart number;
  aNumErr number;
  aStatoFinale CHAR(1):=CNRCTB200.STATO_PROVA_CHIUSURA;
 begin
  begin
   rollback;
   IBMUTL015.setRbsBig;

   aNumRigaStart:=1;

   if modalitaProva = 'N' then
    begin
     select nvl(pg_riga + 1,1) into aNumRigaStart from batch_log_riga where pg_esecuzione = pg_exec;
    exception when NO_DATA_FOUND then
     null;
    end;
   end if;
   -- Se l'esercizio n+1 non e aperto
   if not CNRCTB008.ISESERCIZIOAPERTO(aEs+1,aCdCds) then
    IBMERR001.RAISE_ERR_GENERICO('L''esercizio finanziario ('||(aEs+1)||') per il cds:'||aCdCds||' deve essere aperto');
   end if;

    begin
	 aCCoep:=CNRCTB200.GETCHIUSURACOEP(aEs,aCdCds);
     -- Se la chiusura COEP e gia stata fatta in prova e necessario che sia stata ANNULLATA per rieseguirla
	 if aCCoep.stato <> CNRCTB200.STATO_PROVA_ANNULLATA then
      IBMERR001.RAISE_ERR_GENERICO('Prova di chiusura o chiusura economica definitiva gia'' eseguita in esercizio finanziario ('||aEs||') per il cds:'||aCdCds||' deve essere chiuso per chiudere l''esercizio economico in modo definitivo');
	 end if;
	exception when NO_DATA_FOUND then
     -- Se si tratta della prima operazione di chiusura (definitiva o simulata) crea il record nella tabella CHIUSURA COEP
     aCCoep.CD_CDS:=aCdCds;
     aCCoep.ESERCIZIO:=aEs;
     aCCoep.STATO:=CNRCTB200.STATO_PROVA_ANNULLATA;
     aCCoep.DACR:=aTSNow;
     aCCoep.UTCR:=aUser;
     aCCoep.DUVA:=aTSNow;
     aCCoep.UTUV:=aUser;
     aCCoep.PG_VER_REC:=1;
     CNRCTB200.ins_CHIUSURA_COEP(aCCoep);
    end;
    IBMUTL015.commitRbsBig;
    Begin
     -- annulla l'eventuale Ammortamento gia' calcolato
     CNRCTB400.annullaAmmortBeniInv (pg_exec, aEs, aCdCds, aUser, aTSNow);
     IBMUTL015.commitRbsBig;
     -- Ammortamento finanziario
     IBMUTL200.logInf(pg_exec, 'START AMMORTAMENTO CDS-'||aCdCds,null,null);
     CNRCTB400.ammortamentoBeniInv (pg_exec, aEs, aCdCds, aUser, aTSNow);
     IBMUTL015.commitRbsBig;
     regAmmortamentoCOGE(modalitaProva, aEs, aCdCds, aUser, aTSNow);
     IBMUTL200.logInf(pg_exec, 'FINE REG. AMMORTAMENTO COGE CDS-'||aCdCds,null,null);
    exception when OTHERS then
Dbms_Output.PUT_LINE ('OTHERS 1');
     IBMUTL015.rollbackRbsBig;
     IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,null);
    end;

    IBMUTL200.logInf(pg_exec, 'START RISCONTI P1 e P2 CDS-'||aCdCds,null,null);

    Begin

     for aDocTst in (select * from v_doc_amm_coge_tsta a
                     Where  esercizio = aEs
   		        and cd_cds_origine = aCdCds
   		        and dt_a_competenza_coge >= to_date('0101'||(aEs+1),'DDMMYYYY')
   		        and stato_coge = CNRCTB100.STATO_COEP_CON
 		     ) loop

      regRiscontiCOGE(modalitaProva, aDocTst, aUser, aTSNow);
     end loop;
     IBMUTL200.logInf(pg_exec, 'OK RISCONTI P1 e P2 CDS-'||aCdCds,null,null);
    exception when OTHERS then
Dbms_Output.PUT_LINE ('OTHERS 2');
     IBMUTL015.rollbackRbsBig;
     IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,null);
    end;

    IBMUTL200.logInf(pg_exec, 'START CONTO ECON. CDS-'||aCdCds,null,null);
    begin
     chiusuraContoEconomicoCOGE(modalitaProva, aEs, aCdCds, aUser, aTSNow);
     IBMUTL200.logInf(pg_exec, 'OK CHIUSURA CONTO ECON. CDS-'||aCdCds,null,null);
    exception when OTHERS then
Dbms_Output.PUT_LINE ('OTHERS 3');
     IBMUTL015.rollbackRbsBig;
     IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,null);
    end;

    IBMUTL200.logInf(pg_exec, 'START CHIUSURA STATO PATR. CDS-'||aCdCds,null,null);
    begin
     chiusuraStatoPatrimonialeCOGE(modalitaProva, aEs, aCdCds, aUser, aTSNow);
     IBMUTL200.logInf(pg_exec, 'OK CHIUSURA STATO PATR. CDS-'||aCdCds,null,null);
    exception when OTHERS then
Dbms_Output.PUT_LINE ('OTHERS 4');
     IBMUTL015.rollbackRbsBig;
     IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,null);
    end;

    IBMUTL200.logInf(pg_exec, 'START RILEVAZIONE UTILE CDS-'||aCdCds,null,null);
    begin
     rilevazioneUtilePerditaCOGE(modalitaProva, aEs, aCdCds, aUser, aTSNow);
     IBMUTL200.logInf(pg_exec, 'OK RILEVAZIONE UTILE CDS-'||aCdCds,null,null);
    exception when OTHERS then
Dbms_Output.PUT_LINE ('OTHERS 5');
     IBMUTL015.rollbackRbsBig;
     IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,null);
    end;

    IBMUTL200.logInf(pg_exec, 'START RIPERTURA STATO PATR. CDS-'||aCdCds,null,null);
    begin
     riaperturaStPatrimonialeCOGE(modalitaProva, aEs+1, aCdCds, aUser, aTSNow);
     IBMUTL200.logInf(pg_exec, 'OK RIPERTURA STATO PATR. CDS-'||aCdCds,null,null);
    exception when OTHERS then
Dbms_Output.PUT_LINE ('OTHERS 6');
     IBMUTL015.rollbackRbsBig;
     IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,null);
    end;

    IBMUTL200.logInf(pg_exec, 'START SCRITTURA RATEI P2 CDS-'||aCdCds,null,null);

    begin
     for aDocTst in (select distinct a.*
                     From v_doc_amm_coge_tsta a, scrittura_partita_doppia
                     Where a.esercizio = aEs+1 And
                           a.cd_cds_origine = aCdCds And
                           scrittura_partita_doppia.esercizio = aEs And -- Esercizio in chiusura
 	                   scrittura_partita_doppia.cd_cds_documento  =a.cd_cds And
 			   scrittura_partita_doppia.cd_uo_documento = a.cd_unita_organizzativa And
 			   scrittura_partita_doppia.esercizio_documento_amm = a.esercizio And
 			   scrittura_partita_doppia.cd_tipo_documento  = a.cd_tipo_documento And
 			   scrittura_partita_doppia.pg_numero_documento  =a.pg_numero_documento And
 			   scrittura_partita_doppia.cd_causale_coge = CNRCTB200.CAU_RATEI) Loop
      regDocAmmRateiParte2COGE(modalitaProva, aDocTst, aUser, aTSNow);
     end loop;
     IBMUTL200.logInf(pg_exec, 'OK SCRITTURA RATEI P2 CDS-'||aCdCds,null,null);
     IBMUTL015.commitRbsBig;
Exception when OTHERS then
Dbms_Output.PUT_LINE ('OTHERS 7');
IBMUTL015.rollbackRbsBig;
IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,null);
    end;

    -- Tentativo di chiusura definitiva per il cds specifico
    if modalitaProva = 'N' then
     -- Verifica che non ci siano errori nelle operazioni di chiusura
	 aNumErr:=0;
     select count(*) into aNumErr from batch_log_riga
	  where
	       pg_esecuzione = pg_exec
	   and ti_messaggio = IBMUTL200.TI_ERRORE
	   and pg_riga >= aNumRigaStart;

     begin
      if aNumErr > 0 then
       IBMERR001.RAISE_ERR_GENERICO('La chiusura del cds: '||aCdCds||' e stata effettuata in prova essendo stati sollevati errori');
 	  end if;
	  -- Verifica che non siano state effettuate altre scritture nel frattempo
	  checkChiudibilitaDef(aEs, aCdCds);
	  -- Verifica che non siano stati riaperti esercizi finanziari nel frattempo
	  if checkChiusuraEsFin(pg_exec, aEs) then
       aStatoFinale:=CNRCTB200.STATO_CHIUSURA_DEF;
	  end if;
	 exception when OTHERS then
      IBMUTL015.rollbackRbsBig;
      IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,'Chiusura definitiva esercizio fallita per CDS:'||aCdCds,null);
     end;
	end if;
   exception when OTHERS then
Dbms_Output.PUT_LINE ('others aa');
    IBMUTL015.rollbackRbsBig;
    IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,null);
  end;

  begin
   -- Aggiorna lo stato di chiusura COEP
   update chiusura_coep set
    stato = aStatoFinale,
    utuv=aUser,
    duva=aTSNow,
    pg_ver_rec=pg_ver_rec+1
   where
   	     esercizio=aEs
 	 and cd_cds=aCdCds;
   IBMUTL015.commitRbsBig;
  exception when OTHERS then
Dbms_Output.PUT_LINE ('others bb');
   IBMUTL015.rollbackRbsBig;
   IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,null);
  end;
 End;

 procedure job_chiusuraCogeCoan(job number, pg_exec number, next_date date, modalitaProva char, aEs number, aCdCds varchar2) is
  aUser varchar2(20);
  aTSNow date;
  aMod varchar2(50);
  aCCoep chiusura_coep%rowtype;
  locModalitaProva char;
 begin
  rollback;
  aTSNow:=sysdate;
  IBMUTL015.setRbsBig;
  aUser:=IBMUTL200.getUserFromLog(pg_exec);

  if modalitaProva = 'Y' then
   aMod:='prova';
  else
   aMod:='definitiva';
  end if;

  -- Aggiorna le info di testata del log
  IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_JOB_CHIUSURA_COGE, job, 'Batch di chiusura economica in modalita '||aMod||'. Cds: '||aCdCds||' Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));
  if aCdCds is null then
   IBMERR001.RAISE_ERR_GENERICO('CDS non specificato');
  end if;
  -- Controlla che in modalità definitiva, l'esercizio finanziario sia chiuso per tutti i CDS
  for aCds in (select * from v_unita_organizzativa_valida where fl_cds = 'Y' and esercizio = aEs and cd_unita_organizzativa=decode(aCdCds,'*',cd_unita_organizzativa,aCdCds)) loop
   locModalitaProva:=modalitaProva;
   if modalitaProva = 'N' then
    if not checkChiusuraEsFin(pg_exec, aEs) then
        locModalitaProva:='Y';
    end if;
   end if;
   if locModalitaProva = 'N' then
    CNRCTB800.ACQUISISCISEMSTATICOCDS(aEs,aCds.cd_unita_organizzativa,CNRCTB200.SEMAFORO_CHIUSURA,aUser);
   end if;
   chiusuraCogeCoanCds(pg_exec, locModalitaProva, aEs, aCds.cd_unita_organizzativa, aUser, aTSNow);
   if locModalitaProva = 'N' then
    CNRCTB800.LIBERASEMSTATICOCDS(aEs,aCds.cd_unita_organizzativa,CNRCTB200.SEMAFORO_CHIUSURA,aUser);
   end if;
  end loop;
  IBMUTL200.logInf(pg_exec,'FINE JOB CHIUSURACOGECOAN','Termine esecuzione','Termine esecuzione');
 End;


 procedure job_annullaChiusuraCOGECOAN(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2) is
  aListaMovimenti CNRCTB200.movimentiList;
  aListaMovimentiAn CNRCTB200.movAnalitList;
  aScritturaAn scrittura_analitica%rowtype;
  aScrittura scrittura_partita_doppia%rowtype;
  aUser varchar2(20);
  aTSNow date;
  aCCoep chiusura_coep%rowtype;
 begin
  rollback;
  aTSNow:=sysdate;
  aUser:=IBMUTL200.getUserFromLog(pg_exec);
  IBMUTL015.setRbsBig;

  -- Aggiorna le info di testata del log
  IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_JOB_CHIUSURA_COGE, job, 'Batch di annullamento chiusura economica Cds: '||aCdCds||' Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));

  if aCdCds is null then
   IBMERR001.RAISE_ERR_GENERICO('CDS non specificato');
  End if;

  for aCds in (select * from v_unita_organizzativa_valida where fl_cds = 'Y' and esercizio = aEs and cd_unita_organizzativa=decode(aCdCds,'*',cd_unita_organizzativa,aCdCds)) loop
   begin
    begin
	 aCCoep:=CNRCTB200.GETCHIUSURACOEP(aEs,aCds.cd_unita_organizzativa);
	 if
	        aCCoep.stato = CNRCTB200.STATO_CHIUSURA_DEF
	 then
      IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico ('||aEs||') e'' chiuso definitivamente per il cds:'||aCds.cd_unita_organizzativa);
     elsif aCCoep.stato = CNRCTB200.STATO_PROVA_ANNULLATA then
      IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico ('||aEs||') non e'' stato chiuso in prova per il cds:'||aCds.cd_unita_organizzativa);
     end if;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('L''esercizio economico ('||aEs||') non e mai stato chiuso (definitivamente o in prova) per il cds:'||aCds.cd_unita_organizzativa);
	end;
 	-- Annullamento ammortamento finanziario
   	-- r.p. viene annullato al rilancio
   	-- CNRCTB400.annullaAmmortBeniInv (pg_exec, aEs, aCds.cd_unita_organizzativa, aUser, aTSNow);
  	--  IBMUTL015.commitRbsBig;
       -- Eliminazione scritture in esercizio che viene chiuso
    for aScr in (select * from scrittura_partita_doppia where
                      esercizio = aEs
				  and cd_cds = aCds.cd_unita_organizzativa
				  and cd_causale_coge in (
				   CNRCTB200.CAU_RISCONTI,
				   CNRCTB200.CAU_CHIUSURA_CONTO_ECONOMICO,
				   CNRCTB200.CAU_CHIUSURA_ST_PATRIMONIALE,
				   CNRCTB200.CAU_DET_UTILE_PERDITA,
				   CNRCTB200.CAU_AMMORTAMENTO
				  )
			   ) loop
      aListaMovimenti.delete;
	  aScrittura:=aScr;
      CNRCTB200.GETSCRITTURAEPLOCK(aScrittura,aListaMovimenti);
      CNRCTB200.ELIMINASCRITTCOGE(aScrittura,aListaMovimenti,aUser,aTSNow);
     end loop;
    -- Eliminazione scritture in esercizio che viene aperto
    for aScr in (select * from scrittura_partita_doppia where
                      esercizio = aEs+1
				  and cd_cds = aCds.cd_unita_organizzativa
				  and cd_causale_coge in (
				   CNRCTB200.CAU_RIAPERTURA_CONTI,
				   CNRCTB200.CAU_RATEI_P2,
				   CNRCTB200.CAU_RISCONTI_P2
				  )
			   ) loop
     aListaMovimenti.delete;
	 aScrittura:=aScr;
     CNRCTB200.GETSCRITTURAEPLOCK(aScrittura,aListaMovimenti);
     CNRCTB200.ELIMINASCRITTCOGE(aScrittura,aListaMovimenti,aUser,aTSNow);
    end loop;
    for aScr2 in (select * from scrittura_analitica where esercizio = aEs and cd_cds = aCds.cd_unita_organizzativa and origine_scrittura = CNRCTB200.ORIGINE_CHIUSURA for update nowait) loop
     aListaMovimentiAn.delete;
     aScritturaAn:=aScr2;
     CNRCTB200.GETSCRITTURAANLOCK(aScritturaAn,aListaMovimentiAn);
     CNRCTB200.ELIMINASCRITTCOAN(aScritturaAn,aListaMovimentiAn,aUser,aTSNow);
    end loop;
	update chiusura_coep set
	 stato = CNRCTB200.STATO_PROVA_ANNULLATA,
	 utuv=aUser,
	 duva=aTSNow,
	 pg_ver_rec=pg_ver_rec+1
	where
   	      esercizio=aCCoep.esercizio
	  and cd_cds=aCCoep.cd_cds;
    IBMUTL015.commitRbsBig;
   exception when others then
    IBMUTL015.rollbackRbsBig;
    IBMUTL200.logErr(pg_exec, DBMS_UTILITY.FORMAT_ERROR_STACK,null,null);
   end;
  end loop;
  IBMUTL200.logInf(pg_exec,'FINE','Termine esecuzione','Termine esecuzione');
 end;

 procedure chiusuraContoEconomicoCOGE(modalitaProva char, aEs number, aCdCds varchar2, aUser varchar2, aTSNow date) is
  aVoceEP voce_ep%rowtype;
  aVocePP voce_ep%rowtype;
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
 begin
  if modalitaProva is null or modalitaProva not in ('Y','N') then
   IBMERR001.RAISE_ERR_GENERICO('Modalita operazione non correttamente specificata');
  end if;

  -- L'operazione deve effettuare n-scritture che per CDS e UO girino il saldo sul conto economico di Profitti/Perdite
  for aUO in (select distinct cd_cds, cd_unita_organizzativa, cd_terzo
              From   saldo_coge
              where  esercizio =aEs and
                     cd_cds = aCdCds
              order by cd_cds, cd_unita_organizzativa, cd_terzo) loop
   aListaMovimenti.delete;
   for aSaldo in (select a.* from saldo_coge a, voce_ep b,parametri_cnr
                  Where     a.cd_voce_ep =b.cd_voce_ep
			and a.cd_cds = aUO.cd_cds
                        And a.esercizio = aEs -- stani 15.12.2004
			and a.cd_unita_organizzativa = aUO.cd_unita_organizzativa
			and a.cd_terzo = aUO.cd_terzo
			and a.esercizio = b.esercizio
			 and parametri_cnr.esercizio = A.ESERCIZIO and
        ((parametri_cnr.fl_nuovo_pdg ='Y' and  b.livello=1) or
        ( parametri_cnr.fl_nuovo_pdg='N' and b.livello=3 ) )
			and (b.natura_voce in ('EEC','EER')  or (RIEPILOGA_A ='CEC'))-- Conto di costo o di ricavo
   ) loop
    aVoceEp:=CNRCTB002.GETVOCEEP(aSaldo.esercizio,aSaldo.cd_voce_ep);
    aVocePP:=CNRCTB002.GETVOCEEPCONTOECONOMICO(aEs);
    if aSaldo.tot_dare > aSaldo.tot_avere then
	 CNRCTB204.buildMovPrinc(aSaldo.cd_cds,aSaldo.esercizio,aSaldo.cd_unita_organizzativa,aVoceEP,abs(aSaldo.tot_dare-aSaldo.tot_avere),CNRCTB200.IS_AVERE,to_date('3112'||aEs,'DDMMYYYY'),to_date('3112'||aEs,'DDMMYYYY'),0,aSaldo.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	 CNRCTB204.buildMovPrinc(aSaldo.cd_cds,aSaldo.esercizio,aSaldo.cd_unita_organizzativa,aVocePP,abs(aSaldo.tot_dare-aSaldo.tot_avere),CNRCTB200.IS_DARE,to_date('3112'||aEs,'DDMMYYYY'),to_date('3112'||aEs,'DDMMYYYY'),0,aSaldo.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
    elsif aSaldo.tot_dare < aSaldo.tot_avere then
	 CNRCTB204.buildMovPrinc(aSaldo.cd_cds,aSaldo.esercizio,aSaldo.cd_unita_organizzativa,aVoceEP,abs(aSaldo.tot_dare-aSaldo.tot_avere),CNRCTB200.IS_DARE,to_date('3112'||aEs,'DDMMYYYY'),to_date('3112'||aEs,'DDMMYYYY'),0,aSaldo.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	 CNRCTB204.buildMovPrinc(aSaldo.cd_cds,aSaldo.esercizio,aSaldo.cd_unita_organizzativa,aVocePP,abs(aSaldo.tot_dare-aSaldo.tot_avere),CNRCTB200.IS_AVERE,to_date('3112'||aEs,'DDMMYYYY'),to_date('3112'||aEs,'DDMMYYYY'),0,aSaldo.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
    end if;
   end loop; -- del saldo
   aScrittura:=null;
   aScrittura.CD_CDS:=aCdCds;
   aScrittura.ESERCIZIO:=aEs;
   aScrittura.CD_UNITA_ORGANIZZATIVA:=aUO.cd_unita_organizzativa;
   aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_CHIUSURA;
   aScrittura.CD_TIPO_DOCUMENTO:=null;
   aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_CHIUSURA_CONTO_ECONOMICO;
   -- La chiave del documento viene costruita per concatenazione delle componenti della chiave del buono di scarico
   aScrittura.CD_COMP_DOCUMENTO:='CDS-'||aCdCds||' UO-'||aUO.cd_unita_organizzativa;
   aScrittura.CD_CDS_DOCUMENTO:=aCdCds;
   aScrittura.CD_UO_DOCUMENTO:=aUO.cd_unita_organizzativa;
   aScrittura.PG_NUMERO_DOCUMENTO:=NULL;
   aScrittura.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura
   aScrittura.DT_CONTABILIZZAZIONE:=to_date('3112'||aEs,'DDMMYYYY');
   aScrittura.DT_PAGAMENTO:=null;
   aScrittura.DT_CANCELLAZIONE:=null;
   aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_SINGOLA;
   aScrittura.CD_TERZO:=aUO.cd_terzo;
   aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
   aScrittura.CD_DIVISA:=NULL;
   aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non e chiaro
   aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE
   aScrittura.DS_SCRITTURA := 'Chiusura Conto Economico per CDS '||auo.cd_cds||', UO '||
                              auo.cd_unita_organizzativa||' e Terzo '||auo.cd_terzo;
   aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
   aScrittura.ATTIVA:='Y';
   aScrittura.ESERCIZIO_DOCUMENTO_AMM:=null;
   aScrittura.DACR:=aTSNow;
   aScrittura.UTCR:=aUser;
   -- Registro la scrittura
   CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
  end loop; --del cds/uo/terzo
 end;

 procedure chiusuraStatoPatrimonialeCOGE(modalitaProva char, aEs number, aCdCds varchar2, aUser varchar2, aTSNow date) is
  aVoceEP voce_ep%rowtype;
  aVoceSPI voce_ep%rowtype;
  aListaScritture CNRCTB200.scrittureList;
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
 begin
  if modalitaProva is null or modalitaProva not in ('Y','N') then
   IBMERR001.RAISE_ERR_GENERICO('Modalita operazione non correttamente specificata');
  end if;

  -- L'operazione deve effettuare n-scritture che per CDS e UO girino il saldo sul conto economico di Profitti/Perdite
  for aUO in (select distinct cd_cds, cd_unita_organizzativa, cd_terzo
              From saldo_coge where esercizio =aEs and cd_cds = aCdCds order by cd_cds, cd_unita_organizzativa, cd_terzo) loop
   aListaMovimenti.delete;
   for aSaldo in (select a.* from saldo_coge a, voce_ep b,parametri_cnr where
                a.cd_voce_ep =b.cd_voce_ep
			and a.cd_cds = aUO.cd_cds
			and a.cd_unita_organizzativa = aUO.cd_unita_organizzativa
      And a.esercizio = aEs -- stani 15.12.2004
      and parametri_cnr.esercizio = A.ESERCIZIO and
      -- necessario per riportare solo i conti nuovi (purtroppo vista la ricostruzione deb/cred i saldi dei vecchi conti si azzerano a livello di ente non di uo
     ((parametri_cnr.fl_nuovo_pdg ='Y' and  b.livello=1) or
      ( parametri_cnr.fl_nuovo_pdg='N' and b.livello=3 ))
			and a.cd_terzo = aUO.cd_terzo
			and a.esercizio = b.esercizio
			and (b.natura_voce not in ('EEC','EER') or( b.natura_voce is null and b.RIEPILOGA_A !='CEC')) -- Conto di costo o di ricavo
   ) loop

    aVoceEp:=CNRCTB002.GETVOCEEP(aSaldo.esercizio,aSaldo.cd_voce_ep);
    aVoceSPI:=CNRCTB002.GETVOCEEPSTATOPATRIMONIALE(aEs);

    if aSaldo.tot_dare > aSaldo.tot_avere then
	 CNRCTB204.buildMovPrinc(aSaldo.cd_cds,aSaldo.esercizio,aSaldo.cd_unita_organizzativa,aVoceEP,abs(aSaldo.tot_dare-aSaldo.tot_avere),CNRCTB200.IS_AVERE,to_date('3112'||aEs,'DDMMYYYY'),to_date('3112'||aEs,'DDMMYYYY'),0,aSaldo.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	 CNRCTB204.buildMovPrinc(aSaldo.cd_cds,aSaldo.esercizio,aSaldo.cd_unita_organizzativa,aVoceSPI,abs(aSaldo.tot_dare-aSaldo.tot_avere),CNRCTB200.IS_DARE,to_date('3112'||aEs,'DDMMYYYY'),to_date('3112'||aEs,'DDMMYYYY'),0,aSaldo.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	elsif aSaldo.tot_dare < aSaldo.tot_avere then
	 CNRCTB204.buildMovPrinc(aSaldo.cd_cds,aSaldo.esercizio,aSaldo.cd_unita_organizzativa,aVoceEP,abs(aSaldo.tot_dare-aSaldo.tot_avere),CNRCTB200.IS_DARE,to_date('3112'||aEs,'DDMMYYYY'),to_date('3112'||aEs,'DDMMYYYY'),0,aSaldo.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	 CNRCTB204.buildMovPrinc(aSaldo.cd_cds,aSaldo.esercizio,aSaldo.cd_unita_organizzativa,aVoceSPI,abs(aSaldo.tot_dare-aSaldo.tot_avere),CNRCTB200.IS_AVERE,to_date('3112'||aEs,'DDMMYYYY'),to_date('3112'||aEs,'DDMMYYYY'),0,aSaldo.ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
	end if;
   end loop;
   aScrittura:=null;
   aScrittura.CD_CDS:=aCdCds;
   aScrittura.ESERCIZIO:=aEs;
   aScrittura.CD_UNITA_ORGANIZZATIVA:=aUO.cd_unita_organizzativa;
   aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_CHIUSURA;
   aScrittura.CD_TIPO_DOCUMENTO:=null;
   aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_CHIUSURA_ST_PATRIMONIALE;
   -- La chiave del documento viene costruita per concatenazione delle componenti della chiave del buono di scarico
   aScrittura.CD_COMP_DOCUMENTO:='CDS-'||aCdCds||' UO-'||aUO.cd_unita_organizzativa;
   aScrittura.CD_CDS_DOCUMENTO:=aCdCds;
   aScrittura.CD_UO_DOCUMENTO:=aUO.cd_unita_organizzativa;
   aScrittura.PG_NUMERO_DOCUMENTO:=NULL;
   aScrittura.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura
   aScrittura.DT_CONTABILIZZAZIONE:=to_date('3112'||aEs,'DDMMYYYY');
   aScrittura.DT_PAGAMENTO:=null;
   aScrittura.DT_CANCELLAZIONE:=null;
   aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_SINGOLA;
   aScrittura.CD_TERZO:=aUO.cd_terzo;
   aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
   aScrittura.CD_DIVISA:=NULL;
   aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non e chiaro
   aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE
   aScrittura.DS_SCRITTURA := 'Chiusura Stato Patrimoniale per CDS '||auo.cd_cds||', UO '||
                              auo.cd_unita_organizzativa||' e Terzo '||auo.cd_terzo;
   aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
   aScrittura.ATTIVA:='Y';
   aScrittura.ESERCIZIO_DOCUMENTO_AMM:=null;
   aScrittura.DACR:=aTSNow;
   aScrittura.UTCR:=aUser;
   -- Registro la scrittura
   CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
  end loop;
 end;

 procedure rilevazioneUtilePerditaCOGE(modalitaProva char, aEs number, aCdCds varchar2, aUser varchar2, aTSNow date) is
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aNewScrittura scrittura_partita_doppia%rowtype;
  aNewListaMovimenti CNRCTB200.movimentiList;
  aListaScritture CNRCTB200.scrittureList;
  aVoceEpContoEcon voce_ep%rowtype;
  aVoceEpStPatr voce_ep%rowtype;
  aVoceEpUtilePerdita voce_ep%rowtype;

 begin

  if modalitaProva is null or modalitaProva not in ('Y','N') then
   IBMERR001.RAISE_ERR_GENERICO('Modalita operazione non correttamente specificata');
  end if;

  aVoceEpContoEcon      :=      CNRCTB002.GETVOCEEPCONTOECONOMICO(aEs);
  aVoceEpStPatr         :=      CNRCTB002.GETVOCEEPSTATOPATRIMONIALE(aEs);
  aVoceEpUtilePerdita   :=      CNRCTB002.GETVOCEEPUTILEPERDITAESERCIZIO(aEs);

-- giro su CONTO ECONOMICO

CNRCTB204.getScrittureLock(aEs, aCdCds, CNRCTB200.CAU_CHIUSURA_CONTO_ECONOMICO, CNRCTB200.ORIGINE_CHIUSURA, aListaScritture);

For i in 1..aListaScritture.count loop

   CNRCTB200.GETSCRITTURAEPLOCK(aListaScritture(i),aListaMovimenti);
   aNewListaMovimenti.delete;

   for j in 1..aListaMovimenti.count loop

    if aVoceEpContoEcon.cd_voce_ep = aListaMovimenti(j).cd_voce_ep then
     aListaMovimenti(j).esercizio := aEs;
     aListaMovimenti(j).dt_da_competenza_coge := to_date('3112'||aEs,'DDMMYYYY');
     aListaMovimenti(j).dt_a_competenza_coge := to_date('3112'||aEs,'DDMMYYYY');
     aListaMovimenti(j).sezione := CNRCTB200.GETSEZIONEOPPOSTA(aListaMovimenti(j).sezione);
     aNewListaMovimenti(aNewListaMovimenti.count+1) := aListaMovimenti(j);
    end if;
   end loop;

-- procedure rilevazioneUtilePerditaCOGE
   CNRCTB204.BUILDCHIUSURASCRITTURA(aCdCds, aEs, aListaScritture(i).cd_unita_organizzativa,
                                    aVoceEpUtilePerdita, to_date('3112'||aEs,'DDMMYYYY'),
                                    to_date('3112'||aEs,'DDMMYYYY'),
                                    aNewListaMovimenti(aNewListaMovimenti.count).cd_terzo,
                                    CNRCTB100.TI_PROMISCUO,
                                    --CNRCTB100.TI_ISTITUZIONALE,
                                    aNewListaMovimenti,aUser,aTSNow);

   aListaScritture(i).esercizio:=aEs;
   aListaScritture(i).dt_contabilizzazione:=to_date('3112'||aEs,'DDMMYYYY');
   aListaScritture(i).cd_causale_coge:=CNRCTB200.CAU_DET_UTILE_PERDITA;
   aListaScritture(i).utuv:=aUser;
   aListaScritture(i).utcr:=aUser;
   aListaScritture(i).duva:=aTSNow;
   aListaScritture(i).dacr:=aTSNow;
   aListaScritture(i).pg_ver_rec:=1;
   aNewScrittura:=aListaScritture(i);
   -- Registro la scrittura
   CNRCTB200.CREASCRITTCOGE(aNewScrittura,aNewListaMovimenti);
End loop;

aNewListaMovimenti.delete;
aListaScritture.delete;

-- giro su STATO PATRIMONIALE

  CNRCTB204.getScrittureLock(aEs, aCdCds, CNRCTB200.CAU_CHIUSURA_ST_PATRIMONIALE, CNRCTB200.ORIGINE_CHIUSURA, aListaScritture);
  for i in 1..aListaScritture.count loop

   CNRCTB200.GETSCRITTURAEPLOCK(aListaScritture(i),aListaMovimenti);
   aNewListaMovimenti.delete;
   for j in 1..aListaMovimenti.count loop

    if aVoceEpStPatr.cd_voce_ep = aListaMovimenti(j).cd_voce_ep then
     aListaMovimenti(j).esercizio:=aEs;
     aListaMovimenti(j).dt_da_competenza_coge:=to_date('3112'||aEs,'DDMMYYYY');
     aListaMovimenti(j).dt_a_competenza_coge:=to_date('3112'||aEs,'DDMMYYYY');
     aListaMovimenti(j).sezione:=CNRCTB200.GETSEZIONEOPPOSTA(aListaMovimenti(j).sezione);
     aNewListaMovimenti(aNewListaMovimenti.count+1):=aListaMovimenti(j);
    end if;
   end loop;
-- procedure rilevazioneUtilePerditaCOGE
   CNRCTB204.BUILDCHIUSURASCRITTURA(aCdCds,aEs,aListaScritture(i).cd_unita_organizzativa,
                                    aVoceEpUtilePerdita,to_date('3112'||aEs,'DDMMYYYY'),
                                    to_date('3112'||aEs,'DDMMYYYY'),
                                    aNewListaMovimenti(aNewListaMovimenti.count).cd_terzo,
                                    CNRCTB100.TI_PROMISCUO,
                                    --CNRCTB100.TI_ISTITUZIONALE,
                                    aNewListaMovimenti,aUser,aTSNow);
   aListaScritture(i).esercizio:=aEs;
   aListaScritture(i).dt_contabilizzazione:=to_date('3112'||aEs,'DDMMYYYY');
   aListaScritture(i).cd_causale_coge:=CNRCTB200.CAU_DET_UTILE_PERDITA;
   aListaScritture(i).utuv:=aUser;
   aListaScritture(i).utcr:=aUser;
   aListaScritture(i).utcr:=aTSNow;
   aListaScritture(i).utcr:=aTSNow;
   aNewScrittura:=aListaScritture(i);
   -- Registro la scrittura
   CNRCTB200.CREASCRITTCOGE(aNewScrittura,aNewListaMovimenti);
  end loop;
 end;


 procedure riaperturaStPatrimonialeCOGE(modalitaProva char, aEsNext number, aCdCds varchar2, aUser varchar2, aTSNow date) is
  aVoceEP voce_ep%rowtype;
  aVoceSPI voce_ep%rowtype;
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aListaScritture CNRCTB200.scrittureList;
  aNewListaMovimenti CNRCTB200.movimentiList;
  aNewScrittura scrittura_partita_doppia%rowtype;
 begin
  if modalitaProva is null or modalitaProva not in ('Y','N') then
   IBMERR001.RAISE_ERR_GENERICO('Modalita operazione non correttamente specificata');
  end if;

  CNRCTB204.getScrittureLock(aEsNext-1, aCdCds, CNRCTB200.CAU_CHIUSURA_ST_PATRIMONIALE,CNRCTB200.ORIGINE_CHIUSURA, aListaScritture);
  for i in 1..aListaScritture.count loop
   CNRCTB200.GETSCRITTURAEPLOCK(aListaScritture(i),aListaMovimenti);
   aNewListaMovimenti.delete;
   for j in 1..aListaMovimenti.count loop
    dbms_output.put_line('riapro '||aListaMovimenti(j).cd_voce_ep);
    aListaMovimenti(j).esercizio:=aEsNext;
    aListaMovimenti(j).dt_da_competenza_coge:=to_date('0101'||aEsNext,'DDMMYYYY');
    aListaMovimenti(j).dt_a_competenza_coge:=to_date('0101'||aEsNext,'DDMMYYYY');
    aListaMovimenti(j).sezione:=CNRCTB200.GETSEZIONEOPPOSTA(aListaMovimenti(j).sezione);
    aNewListaMovimenti(aNewListaMovimenti.count+1):=aListaMovimenti(j);
   end loop;
   aListaScritture(i).esercizio:=aEsNext;
   aListaScritture(i).dt_contabilizzazione:=to_date('0101'||aEsNext,'DDMMYYYY');
   aListaScritture(i).cd_causale_coge:=CNRCTB200.CAU_RIAPERTURA_CONTI;
   aListaScritture(i).utuv:=aUser;
   aListaScritture(i).utcr:=aUser;
   aListaScritture(i).duva:=aTSNow;
   aListaScritture(i).dacr:=aTSNow;
   aListaScritture(i).pg_ver_rec:=1;
   aNewScrittura:=aListaScritture(i);
   -- Registro la scrittura
   CNRCTB200.CREASCRITTCOGE(aNewScrittura,aNewListaMovimenti);
  end loop;
 end;

 procedure regAmmortamentoCOGE(modalitaProva char, aEs number, aCdCds varchar2, aUser varchar2, aTSNow date) is
  aCatGruppoVoceEp CATEGORIA_GRUPPO_VOCE_EP%rowtype;
  aVoceEPCG voce_ep%rowtype;
  aVoceEPCGContr voce_ep%rowtype;
  aListaScritture CNRCTB200.scrittureList;
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aResiduo number(15,2);
  aTotDistr number(15,2);
  aImpLaCdr number(15,2);
  aLa linea_attivita%rowtype;
  lMovimenti CNRCTB200.movAnalitList;
  rScrittura scrittura_analitica%rowtype;
  aNumMov number(15,2);
  aTempMov movimento_coan%rowtype;
  recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);

   CNRCTB204.GETSCRITTUREAMMORTAMENTOLOCK(aEs,aCdCds,aListaScritture);
   if aListaScritture.count > 0 then
    IBMERR001.RAISE_ERR_GENERICO('Scrittura di ammortamento gia eseguita per cds:'||aCdCds||' in esercizio '||aEs);
   end if;


  lock table categoria_gruppo_invent in exclusive mode;

    For aAmmInv In(Select * From AMMORTAMENTO_BENE_INV
  Where CD_CDS_UBICAZIONE       =aCdCds FOR UPDATE NOWAIT) Loop
  Null;
  End Loop;
 For aBeneInv In(Select * From INVENTARIO_BENI
  Where cd_cds =aCdCds FOR UPDATE NOWAIT) Loop
  	Null;
  End Loop;

   for aInventario in (select * from id_inventario) loop

    for aInventBene in (select c.cd_cds,c.cd_unita_organizzativa, b.cd_categoria_padre categoria,
                        C.TI_COMMERCIALE_ISTITUZIONALE, sum(a.im_movimento_ammort) im_ammortamento
                        from ammortamento_bene_inv a, categoria_gruppo_invent b, inventario_beni c
                        Where  c.pg_inventario = aInventario.pg_inventario
                           and c.nr_inventario = a.nr_inventario
                           and c.progressivo = a.progressivo
                           and c.cd_cds = aCdCds
                    	   And a.pg_inventario = aInventario.pg_inventario
                    	   And a.cd_cds_ubicazione = c.cd_cds
                    	   and a.esercizio = aEs
                    	   And a.fl_storno ='N'
                    	   and a.cd_categoria_gruppo  =b.cd_categoria_gruppo
                    	   and recParametriCNR.fl_nuovo_pdg = 'N'
                     	group by c.cd_cds,c.cd_unita_organizzativa, b.cd_categoria_padre,
                     	         TI_COMMERCIALE_ISTITUZIONALE
                     	 union
                     	  select c.cd_cds,c.cd_unita_organizzativa, c.cd_categoria_gruppo categoria,
                        C.TI_COMMERCIALE_ISTITUZIONALE, sum(a.im_movimento_ammort) im_ammortamento
                        from ammortamento_bene_inv a, inventario_beni c
                        Where  c.pg_inventario = aInventario.pg_inventario
                           and c.nr_inventario = a.nr_inventario
                           and c.progressivo = a.progressivo
                           and c.cd_cds = aCdCds
                    	   And a.pg_inventario = aInventario.pg_inventario
                    	   And a.cd_cds_ubicazione = c.cd_cds
                    	   and a.esercizio = aEs
                    	   And a.fl_storno ='N'
                    	   and recParametriCNR.fl_nuovo_pdg = 'Y'
                     	group by c.cd_cds,c.cd_unita_organizzativa, c.cd_categoria_gruppo,TI_COMMERCIALE_ISTITUZIONALE) loop

     begin
      select * into aCatGruppoVoceEp
      from  CATEGORIA_GRUPPO_VOCE_EP
      Where cd_categoria_gruppo = aInventBene.categoria
        And sezione = CNRCTB200.IS_DARE
        And ESERCIZIO = aEs
        and fl_default ='Y' ;
     exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('Nessun conto economico associato a categoria:'||aInventBene.categoria);
     end;

     aListaMovimenti.delete;
     aVoceEPCG:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.esercizio,aCatGruppoVoceEp.cd_voce_ep);
     aVoceEPCGContr:=CNRCTB002.getVoceEp(aCatGruppoVoceEp.esercizio,aCatGruppoVoceEp.cd_voce_ep_contr);

-- procedure regAmmortamentoCOGE
     CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aEs,aInventBene.cd_unita_organizzativa,aVoceEPCG,
                             aInventBene.im_ammortamento,CNRCTB200.IS_DARE,
                             to_date('3112'||aEs,'DDMMYYYY'),to_date('3112'||aEs,'DDMMYYYY'),0,
                             aInventBene.TI_COMMERCIALE_ISTITUZIONALE, --CNRCTB100.TI_ISTITUZIONALE,
                             aListaMovimenti,aUser,aTSnow);

-- procedure regAmmortamentoCOGE
     CNRCTB204.buildMovPrinc(aInventBene.cd_cds,aEs,aInventBene.cd_unita_organizzativa,aVoceEPCGContr,
                             aInventBene.im_ammortamento,CNRCTB200.IS_AVERE,
                             to_date('3112'||aEs,'DDMMYYYY'),to_date('3112'||aEs,'DDMMYYYY'),0,
                             aInventBene.TI_COMMERCIALE_ISTITUZIONALE, --CNRCTB100.TI_ISTITUZIONALE,
                             aListaMovimenti,aUser,aTSnow);

     aScrittura:=null;
     aScrittura.CD_CDS:=aInventBene.cd_cds;
     aScrittura.ESERCIZIO:=aEs;
     aScrittura.CD_UNITA_ORGANIZZATIVA:=aInventBene.cd_unita_organizzativa;
     aScrittura.ORIGINE_SCRITTURA:=CNRCTB200.ORIGINE_CHIUSURA;
     aScrittura.CD_TIPO_DOCUMENTO:=null;
     aScrittura.CD_CAUSALE_COGE:=CNRCTB200.CAU_AMMORTAMENTO;
     -- La chiave del documento viene costruita per concatenazione delle componenti della chiave del buono di scarico
     aScrittura.CD_COMP_DOCUMENTO:='INVENTARIO-'||aInventario.pg_inventario||' CATEGORIA-'||aInventBene.categoria;
     aScrittura.CD_CDS_DOCUMENTO:=aInventBene.cd_cds;
     aScrittura.CD_UO_DOCUMENTO:=aInventBene.cd_unita_organizzativa;
     aScrittura.PG_NUMERO_DOCUMENTO:=NULL;
     aScrittura.IM_SCRITTURA:=null; -- Impostato come totale di sezione in realizzazione della scrittura
     aScrittura.DT_CONTABILIZZAZIONE:=to_date('3112'||aEs,'DDMMYYYY');
     aScrittura.DT_PAGAMENTO:=null;
     aScrittura.DT_CANCELLAZIONE:=null;
     aScrittura.TI_SCRITTURA:=CNRCTB200.TI_SCRITTURA_SINGOLA;
     aScrittura.CD_TERZO:=0;
     aScrittura.STATO:=CNRCTB200.STATO_DEFINITIVO;
     aScrittura.CD_DIVISA:=NULL;
     aScrittura.COSTO_PLURIENNALE:=null; -- chi lo valorizza e come non e chiaro
     aScrittura.PG_ENTE:=null; -- serve a rinumerare le scritture  livello di ENTE

     aScrittura.DS_SCRITTURA := 'Scrittura di ammortamento per CDS '||aInventBene.cd_cds
                                ||', UO '||aInventBene.cd_unita_organizzativa||
                                ', Inventario '||aInventario.pg_inventario||', Categoria '||aInventBene.categoria;

     aScrittura.PG_SCRITTURA_ANNULLATA:=null; -- codice della scrittura annullata in effettuazione storno
     aScrittura.ATTIVA:='Y';
     aScrittura.ESERCIZIO_DOCUMENTO_AMM:=null;
     aScrittura.DACR:=aTSNow;
     aScrittura.UTCR:=aUser;
     -- Registro la scrittura
     CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);

	 -- Registrazione ammortamento in analitica
     For aInventBeneD in (select c.pg_inventario, c.nr_inventario, c.progressivo, c.cd_cds,
                                 c.cd_unita_organizzativa, aInventBene.categoria,
                                 a.im_movimento_ammort im_ammortamento
                          From  ammortamento_bene_inv a,  inventario_beni c
	                  Where     c.pg_inventario = aInventario.pg_inventario
                                and c.nr_inventario = a.nr_inventario
                                And c.progressivo = a.progressivo
		                            and c.cd_cds = aInventBene.cd_cds
                                and c.cd_unita_organizzativa = aInventBene.cd_unita_organizzativa
		                						And a.pg_inventario = aInventario.pg_inventario
		                						and a.esercizio = aEs
		                						And a.cd_cds_ubicazione = c.cd_cds
		                						and c.fl_ammortamento = 'Y'
		                order by c.cd_cds,c.cd_unita_organizzativa, aInventBene.categoria) Loop
	  aNumMov:=0;
	  aTotDistr:=0;
	  lMovimenti.delete;

      For aInvUtil in (select * from inventario_utilizzatori_la
                       Where  pg_inventario = aInventBeneD.pg_inventario
                          and nr_inventario = aInventBeneD.nr_inventario
		-- Modifica del 16/11/2004
		-- I dati di utilizzatore sono sul bene padre (progressivo=0)
                          and progressivo = 0
                 	  for update nowait) loop

       aNumMov:=aNumMov+1;
       aTempMov:=null;
       aTempMov.CD_CDS := aScrittura.CD_CDS;
       aTempMov.ESERCIZIO := aScrittura.ESERCIZIO;
       aTempMov.CD_UNITA_ORGANIZZATIVA := aScrittura.CD_UNITA_ORGANIZZATIVA;
       aTempMov.PG_SCRITTURA := NULL;
       aTempMov.CD_VOCE_EP := aVoceEPCG.CD_VOCE_EP;
       aTempMov.PG_MOVIMENTO := NULL;
       aTempMov.SEZIONE := CNRCTB200.IS_DARE;
       aTempMov.CD_CENTRO_RESPONSABILITA :=  aInvUtil.CD_UTILIZZATORE_CDR;
       aImpLaCdr:=aInventBeneD.im_ammortamento*(aInvUtil.percentuale_utilizzo_cdr/100)*(aInvUtil.percentuale_utilizzo_la/100);
       aTotDistr:=aTotDistr+aImpLaCdr;
       aTempMov.IM_MOVIMENTO := aImpLaCdr;
	   select * into aLa
	   from  linea_attivita
	   Where cd_centro_responsabilita=aInvUtil.cd_utilizzatore_cdr And
	         cd_linea_attivita=aInvUtil.cd_linea_attivita;
       aTempMov.CD_TERZO := Null; --0;
       aTempMov.CD_FUNZIONE := aLa.CD_FUNZIONE;
       aTempMov.CD_NATURA := aLa.CD_NATURA;
       aTempMov.STATO :='D';
       aTempMov.DS_MOVIMENTO := 'Ammortamento bene inv n.'||aInvUtil.pg_inventario||' nr.'||aInvUtil.nr_inventario||' progr.'||aInventBeneD.progressivo;
       aTempMov.CD_LINEA_ATTIVITA :=  aInvUtil.CD_LINEA_ATTIVITA;
       aTempMov.PG_NUMERO_DOCUMENTO := null;
       aTempMov.TI_ISTITUZ_COMMERC := ainventbene.TI_COMMERCIALE_ISTITUZIONALE;
       lMovimenti(aNumMov):=aTempMov;

         End loop;  -- degli utilizzatori

	  -- Redistribuzione dei rotti
      if lMovimenti.count = 0 then
       IBMERR001.RAISE_ERR_GENERICO('Esistono beni per cui non sono definiti utilizzatori. Bene inv n.'||aInventBeneD.pg_inventario||' nr.'||aInventBeneD.nr_inventario||' progr.0');
      end if;

      if lMovimenti.count > 0 then
          if aTotDistr - aInventBeneD.im_ammortamento > 0 then
           lMovimenti(lMovimenti.count).IM_MOVIMENTO:=lMovimenti(lMovimenti.count).IM_MOVIMENTO-(aTotDistr - aInventBeneD.im_ammortamento);
          elsif aTotDistr - aInventBeneD.im_ammortamento < 0 then
           lMovimenti(lMovimenti.count).IM_MOVIMENTO:=lMovimenti(lMovimenti.count).IM_MOVIMENTO-(aTotDistr - aInventBeneD.im_ammortamento);
          else
           null;
          end if;
       rScrittura.CD_CDS := aScrittura.CD_CDS;
       rScrittura.ESERCIZIO := aScrittura.ESERCIZIO;
       rScrittura.CD_UNITA_ORGANIZZATIVA := aScrittura.CD_UNITA_ORGANIZZATIVA;
       rScrittura.PG_SCRITTURA := NULL;
       rScrittura.ORIGINE_SCRITTURA := CNRCTB200.ORIGINE_CHIUSURA;
       rScrittura.CD_TERZO := Null; --0;
       rScrittura.CD_CDS_DOCUMENTO := null;
       rScrittura.CD_UO_DOCUMENTO := null;
       rScrittura.CD_TIPO_DOCUMENTO := null;
       rScrittura.PG_NUMERO_DOCUMENTO := null;
       rScrittura.CD_COMP_DOCUMENTO := NULL;
       rScrittura.IM_SCRITTURA := 0;
       rScrittura.TI_SCRITTURA := CNRCTB200.TI_SCRITTURA_SINGOLA;
       rScrittura.DT_CONTABILIZZAZIONE := aScrittura.dt_contabilizzazione;
       rScrittura.DT_CANCELLAZIONE := NULL;
       rScrittura.STATO := CNRCTB200.STATO_DEFINITIVO;
       rScrittura.DS_SCRITTURA := 'Ammortamento bene inv n.'||aInventBeneD.pg_inventario||' nr.'||aInventBeneD.nr_inventario||' progr.'||aInventBeneD.progressivo;
       rScrittura.PG_SCRITTURA_ANNULLATA := NULL;
       rScrittura.ATTIVA := 'Y';
       rScrittura.ESERCIZIO_DOCUMENTO_AMM := null;
       rScrittura.DACR := aScrittura.dacr;
       rScrittura.UTCR := aScrittura.utcr;
       CNRCTB200.CREASCRITTCOAN(rScrittura,lMovimenti);
      end if;

    End loop;  -- degli ammortamenti
  End loop;  -- dei beni
End loop;  -- degli inventari

End;

 procedure regRiscontiCOGE(modalitaProva char, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2, aTSNow date) is
  aScrittura scrittura_partita_doppia%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aListaMovimentiNewEs CNRCTB200.movimentiList;
  aMovimento movimento_coge%rowtype;
  aContoEp voce_ep%rowtype;
  aContoRis voce_ep%rowtype;
  aListaScritture CNRCTB200.scrittureList;
  aListaScrittureP2 CNRCTB200.scrittureList;
  aListaScritturePrime CNRCTB200.scrittureList;
  aListaMovimentiPrimi CNRCTB200.movimentiList;
  aEsNext number(4);
  aFrazioneNext number(15,10);
  aPeriodoBase number(15,2);
  isProcessaCOEP boolean;
  aNewDa date;
 begin
  if modalitaProva is null or modalitaProva not in ('Y','N') then
   IBMERR001.RAISE_ERR_GENERICO('Modalita operazione non correttamente specificata');
  end if;

  -- Lock del documento
  CNRCTB100.LOCKDOCAMM(
   aDocTst.cd_tipo_documento,
   aDocTst.cd_cds,
   aDocTst.esercizio,
   aDocTst.cd_unita_organizzativa,
   aDocTst.pg_numero_documento
  );

  -- Se il documento NON ha data fine competenza coge in esercizio successivo esco
  if not (aDocTst.dt_a_competenza_coge >= to_date('0101'||(aDocTst.esercizio+1),'DDMMYYYY')) then
   return;
  end if;

  -- cerca eventuali scritture gia registrate per il documento

  begin
   CNRCTB204.getScritturePEPLock(aDocTst,CNRCTB200.CAU_RISCONTI,aListaScritture);
  exception when NO_DATA_FOUND then
   null;
  end;
  -- Se la scrittura di risconto e gia stata fatta sollevo eccezione
  if aListaScritture.count > 0 then
   IBMERR001.RAISE_ERR_GENERICO('Scrittura di risconto parte 1 gia effettuata per il documento '||CNRCTB204.getDescDocumento(aDocTst));
  end if;

  begin
   CNRCTB204.getScritturePEPLock(aDocTst.esercizio+1,aDocTst,CNRCTB200.CAU_RISCONTI_P2,aListaScrittureP2);
  exception when NO_DATA_FOUND then
   null;
  end;
  -- Se ls scrittura di risconto parte 2 gia stata fatta nel nuovo esercizio sollevo eccezione
  if aListaScrittureP2.count > 0 then
   IBMERR001.RAISE_ERR_GENERICO('Scrittura di risconto parte 2 gia effettuata per il documento '||CNRCTB204.getDescDocumento(aDocTst));
  end if;

  begin
   CNRCTB204.getScritturePEPLock(aDocTst,aListaScritturePrime);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Scrittura prima non trovata per documento '||CNRCTB204.getDescDocumento(aDocTst));
  end;

If aListaScritturePrime.count > 0 then

For i in 1 .. aListaScritturePrime.count loop

    aEsNext:=aListaScritturePrime(i).esercizio+1;
    CNRCTB200.getScritturaEPLOCK(aListaScritturePrime(i),aListaMovimentiPrimi);
    aListaMovimenti.delete;

   For j in 1 .. aListaMovimentiPrimi.count loop

     aContoEp:=CNRCTB002.GETVOCEEP(aListaScritturePrime(i).esercizio, aListaMovimentiPrimi(j).cd_voce_ep);
     if ((Upper(TRIM(aContoEp.NATURA_VOCE)) = 'EEC') Or (UPPER(trim(aContoEp.NATURA_VOCE)) = 'EER') or (Upper(TRIM(aContoEp.RIEPILOGA_A)) = 'CEC' )) then
       if aListaMovimentiPrimi(j).dt_a_competenza_coge >= to_date('0101'||aEsNext,'DDMMYYYY') then
       -- Estrae la parte di costo/ricavo da riscontare su esercizio futuro
       aPeriodoBase:=aListaMovimentiPrimi(j).dt_a_competenza_coge-aListaMovimentiPrimi(j).dt_da_competenza_coge+1;

         if to_date('0101'||aEsNext,'DDMMYYYY') > aListaMovimentiPrimi(j).dt_da_competenza_coge then
          aFrazioneNext:=aListaMovimentiPrimi(j).dt_a_competenza_coge-to_date('0101'||aEsNext,'DDMMYYYY')+1;
          aNewDa:=to_date('0101'||aEsNext,'DDMMYYYY');
         else
          aFrazioneNext:=aListaMovimentiPrimi(j).dt_a_competenza_coge-aListaMovimentiPrimi(j).dt_da_competenza_coge+1;
          aNewDa:=aListaMovimentiPrimi(j).dt_da_competenza_coge;
         end if;

       aFrazioneNext:=aFrazioneNext/aPeriodoBase;

-- se il costo/ricavo è totalmente anno successivo
-- storna anche l'imputazione del conto sull'anno successivo
-- per toglierla totalmente dal saldo che chiuderà

If aListaMovimentiPrimi(j).dt_da_competenza_coge >= to_date('0101'||aEsNext,'DDMMYYYY') Then

       CNRCTB204.buildMovPrinc(aListaScritturePrime(i).cd_cds,aListaScritturePrime(i).esercizio,
                               aListaScritturePrime(i).cd_unita_organizzativa,aContoEp,
                               aListaMovimentiPrimi(j).im_movimento*aFrazioneNext,
                               CNRCTB200.getsezioneopposta(aListaMovimentiPrimi(j)),
                               aNewDa,
                               aListaMovimentiPrimi(j).dt_a_competenza_coge,
                               aListaScritturePrime(i).cd_terzo,
                               aListaMovimentiPrimi(j).ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);

Else

-- se il costo/ricavo è a cavallo tra anno corrente e anno successivo
-- storna la quota sull'anno corrente per toglierla dal saldo che chiuderà

       CNRCTB204.buildMovPrinc(aListaScritturePrime(i).cd_cds,aListaScritturePrime(i).esercizio,
                               aListaScritturePrime(i).cd_unita_organizzativa,aContoEp,
                               aListaMovimentiPrimi(j).im_movimento*aFrazioneNext,
                               CNRCTB200.getsezioneopposta(aListaMovimentiPrimi(j)),
                               to_date('3112'||aListaScritturePrime(i).esercizio,'DDMMYYYY'),--aNewDa,
                               to_date('3112'||aListaScritturePrime(i).esercizio,'DDMMYYYY'),--aListaMovimentiPrimi(j).dt_a_competenza_coge,
                               aListaScritturePrime(i).cd_terzo,
                               aListaMovimentiPrimi(j).ti_istituz_commerc,aListaMovimenti,aUser,aTSnow);
End If;

       CNRCTB204.buildMovPrinc(aListaScritturePrime(i).cd_cds,aEsNext,
                               aListaScritturePrime(i).cd_unita_organizzativa,aContoEp,
                               aListaMovimentiPrimi(j).im_movimento*aFrazioneNext,
                               aListaMovimentiPrimi(j).sezione,
                               aNewDa,
                               aListaMovimentiPrimi(j).dt_a_competenza_coge,
                               aListaScritturePrime(i).cd_terzo,
                               aListaMovimentiPrimi(j).ti_istituz_commerc,aListaMovimentiNewEs,
                               aUser,aTSnow);
       end if;
     End if;

    end loop; -- J

    if aListaMovimenti.count = 0 then
	 return;
    end if;
    if CNRCTB204.getSezioneChiusuraScr(aListaMovimenti) = CNRCTB200.IS_DARE then
     aContoRis:=CNRCTB002.GETVOCEEPRISCONTIATTIVI(aListaScritturePrime(i).esercizio);
    else
     aContoRis:=CNRCTB002.GETVOCEEPRISCONTIPASSIVI(aListaScritturePrime(i).esercizio);
    end if;

	-- Crea scrittura risconto parte 1
--  procedure regRiscontiCOGE
    CNRCTB204.buildChiusuraScrittura(aListaScritturePrime(i).cd_cds,aListaScritturePrime(i).esercizio,
	                             aListaScritturePrime(i).cd_unita_organizzativa,aContoRis,
	                             to_date('3112'||(aEsNext-1),'DDMMYYYY'),
	                             to_date('3112'||(aEsNext-1),'DDMMYYYY'),
	                             aListaScritturePrime(i).cd_terzo,
                                     --CNRCTB200.TI_ISTITUZIONALE,
                                     aDocTst.TI_ISTITUZ_COMMERC,
                                     aListaMovimenti,aUser,aTSnow);

    aScrittura:=null;
    aScrittura:=aListaScritturePrime(i);
    aScrittura.pg_scrittura:=null;
    aScrittura.cd_causale_coge:=CNRCTB200.CAU_RISCONTI;
    aScrittura.DT_CONTABILIZZAZIONE:=to_date('3112'||aDocTst.esercizio,'DDMMYYYY');
    aScrittura.UTUV:=aUser;
    aScrittura.UTCR:=aUser;
    aScrittura.DACR:=aTSNow;
    aScrittura.DUVA:=aTSNow;
    aScrittura.PG_VER_REC:=1;
    CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimenti);
	-- Crea scrittura risconto parte 2
--  procedure regRiscontiCOGE
    CNRCTB204.buildChiusuraScrittura(aListaScritturePrime(i).cd_cds,aEsNext,
                                     aListaScritturePrime(i).cd_unita_organizzativa,aContoRis,
                                     To_Date('0101'||aEsNext,'DDMMYYYY'),
                                     to_date('0101'||aEsNext,'DDMMYYYY'),
                                     aListaScritturePrime(i).cd_terzo,
                                     --    CNRCTB200.TI_ISTITUZIONALE,
                                     aDocTst.TI_ISTITUZ_COMMERC,
                                     aListaMovimentiNewEs,aUser,aTSnow);
    aScrittura:=null;
    aScrittura:=aListaScritturePrime(i);
    aScrittura.pg_scrittura:=null;
    aScrittura.esercizio:=aEsNext;
    aScrittura.cd_causale_coge:=CNRCTB200.CAU_RISCONTI_P2;
    aScrittura.DT_CONTABILIZZAZIONE:=to_date('0101'||aEsNext,'DDMMYYYY');
    aScrittura.UTUV:=aUser;
    aScrittura.UTCR:=aUser;
    aScrittura.DACR:=aTSNow;
    aScrittura.DUVA:=aTSNow;
    aScrittura.PG_VER_REC:=1;
    CNRCTB200.CREASCRITTCOGE(aScrittura,aListaMovimentiNewEs);
   end loop;
  end if;
 end;


Procedure regDocAmmRateiParte2COGE(modalitaProva char, aDocTst V_DOC_AMM_COGE_TSTA%rowtype, aUser varchar2, aTSNow date) is
  aScrittura scrittura_partita_doppia%rowtype;
  aMovimento movimento_coge%rowtype;
  aListaMovimenti CNRCTB200.movimentiList;
  aNewListMov   CNRCTB200.movimentiList;
  aNewMov       movimento_coge%rowtype;
  aVoceEp voce_ep%rowtype;
  aVoceIVAEp voce_ep%rowtype;
  aListaScritture CNRCTB200.scrittureList;
  aListaScrittureP2 CNRCTB200.scrittureList;
  aListaMovimentiP2 CNRCTB200.movimentiList;
  aListaVecchieScrittureP2 CNRCTB200.scrittureList;
  aNewListaScrittureP2 CNRCTB200.scrittureList;
  aDoc v_doc_amm_coge_riga%rowtype;
  a NUMBER := 0;
  tot_doc NUMBER := 0;

Begin

Dbms_Output.PUT_LINE ('Entro con il documento '||aDocTst.CD_TIPO_DOCUMENTO||' '||aDocTst.CD_CDS||' '||aDocTst.CD_UNITA_ORGANIZZATIVA||' '||aDocTst.ESERCIZIO||' '||
aDocTst.CD_CDS_ORIGINE||' '||aDocTst.CD_UO_ORIGINE||' '||aDocTst.PG_NUMERO_DOCUMENTO);

if modalitaProva is null or modalitaProva not in ('Y','N') then
   IBMERR001.RAISE_ERR_GENERICO('Modalita operazione non correttamente specificata');
End if;

  -- Lock del documento
CNRCTB100.LOCKDOCAMM(
   aDocTst.cd_tipo_documento,
   aDocTst.cd_cds,
   aDocTst.esercizio,
   aDocTst.cd_unita_organizzativa,
   aDocTst.pg_numero_documento);

  -- Se la competenza non e fuori esercizio non effettua operazioni

If Not (CNRCTB204.getCompetenzaFuoriEsercizio(aDocTst) Or CNRCTB204.getCompetenzaacavalloconEsPrec(aDocTst)) Then
Dbms_Output.PUT_LINE ('RETURN 1');
   Return;
End If;

Savepoint CNRCTB205_SP_002;

  -- cerca eventuali scritture gia registrate per il documento
  -- se la competenza è fuori esercizio cerca i ratei parte 1

CNRCTB204.getScritturePEPLock(aDocTst.esercizio-1, aDocTst, CNRCTB200.CAU_RATEI, aListaScritture);

Dbms_Output.PUT_LINE ('DI TESTATE RATEO DA CHIUDERE TROVA N. '||aListaScritture.count||' RIGHE');

CNRCTB204.getScritturePEPLock(aDocTst.esercizio, aDocTst, CNRCTB200.CAU_RATEI_P2, aListaScrittureP2);

-- Se esistono scritture di rateo parte 2 solleva eccezione
--If aListaScrittureP2.count > 0 then
   --IBMERR001.RAISE_ERR_GENERICO('Scrittura di rateo parte 2 gia effettuata per il documento '||CNRCTB204.getDescDocumento(aDocTst));
--End if;

For i in 1 .. aListaScritture.count loop
   aNewListMov.delete;
   aScrittura:=aListaScritture(i);
   aListaMovimenti.delete;
   CNRCTB200.GETSCRITTURAEPLOCK(aScrittura,aListaMovimenti);

Dbms_Output.PUT_LINE ('DI RIGHE RATEO DA CHIUDERE TROVA N. '||aListaMovimenti.count||' RIGHE');

   aScrittura.esercizio := aScrittura.esercizio+1;
   aScrittura.dt_contabilizzazione := to_date('0101'||(aScrittura.esercizio),'DDMMYYYY'); --Trunc(aTSNow); -- stani
   aScrittura.cd_causale_coge := CNRCTB200.CAU_RATEI_P2;
   aScrittura.ti_scrittura := CNRCTB200.TI_SCRITTURA_PRIMA;
   aScrittura.utcr := aUser;
   aScrittura.utuv := aUser;
   aScrittura.duva := aTSNow;
   aScrittura.dacr := aTSNow;
   aScrittura.pg_ver_rec := 1;

   For j in 1 .. aListaMovimenti.count Loop

    --If aListaMovimenti(j).fl_mov_terzo = 'Y' Then
    If aListaMovimenti(j).CD_VOCE_EP In (CNRCTB015.getVal01PerChiave(aScrittura.esercizio, 'VOCEEP_SPECIALE','RATEI_PASSIVI'),
                                         CNRCTB015.getVal01PerChiave(aScrittura.esercizio, 'VOCEEP_SPECIALE','RATEI_ATTIVI'),
                                         CNRCTB015.getVal01PerChiave(aScrittura.esercizio, 'VOCEEP_SPECIALE','FATTURE_DA_EMETTERE'),
                                         CNRCTB015.getVal01PerChiave(aScrittura.esercizio, 'VOCEEP_SPECIALE','FATTURE_DA_RICEVERE')) Then

Dbms_Output.PUT_LINE ('riga '||j);

         aNewMov                     := aListaMovimenti(j);

	 aNewMov.esercizio           := aScrittura.esercizio;
	 aNewMov.fl_mov_terzo        := null;
	 aNewMov.ti_istituz_commerc  := aListaMovimenti(j).ti_istituz_commerc;
	 aNewMov.sezione             := CNRCTB200.GETSEZIONEOPPOSTA(aNewMov.sezione);

         aNewListMov(aNewListMov.Count+1) := aNewMov;
 	 --exit;

-- per ogni riga fa l'eventuale parte iva solo se prevista

   If aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA Or aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_PASSIVA
      Or aDocTst.cd_tipo_documento = CNRCTB100.TI_COMPENSO Then

-- 20.06.2006 STANI LE SCRITTURE DI RATEI PARTE 2 DEVONO ALIMENTARE L'IVA (trascurata alla contabilizzazione (205))
--            MA "NON" I COSTI PER RITENUTE CARICO ENTE PER I COMPENSI (CHE VENGONO PRESE L'ANNO PRIMA)
					declare
						fl_split  char(1):='N';
						begin	
					   If  aDocTst.cd_tipo_documento = CNRCTB100.TI_FATTURA_ATTIVA Then
							select FL_LIQUIDAZIONE_DIFFERITA into fl_split
							from fattura_attiva
							where  
							fattura_attiva.ti_fattura =  aDocTst.ti_fattura and
					    fattura_attiva.esercizio = aDocTst.ESERCIZIO and
					    fattura_attiva.cd_cds_origine = aDocTst.CD_CDS_ORIGINE and
					    fattura_attiva.cd_uo_origine = aDocTst.CD_UO_ORIGINE and
					    fattura_attiva.pg_fattura_attiva = aDocTst.PG_NUMERO_DOCUMENTO;
						else 
						  fl_split:='N';
						end if;
						if  (fl_split = 'N')  then 
                 For aDocCori in (Select *
                                  From   V_DOC_AMM_COGE_CORI
                                  Where  cd_cds                 = aDocTst.cd_cds And
                                         cd_unita_organizzativa = aDocTst.cd_unita_organizzativa And
                                         esercizio              = aDocTst.esercizio And
                                         pg_numero_documento    = aDocTst.pg_numero_documento And
                                         cd_tipo_documento      = aDocTst.cd_tipo_documento And
                                         cd_contributo_ritenuta = CNRCTB015.GETVAL01PERCHIAVE(CNRCTB575.TI_CORI_SPECIALE,CNRCTB575.TI_CORI_IVA) And
            -- AGGIUNTO IL 21.06.2006 PERCHE', PER I COMPENSI, METTEVA ANCHE I CONTRIBUTI CARICO PERCIPIENTE COME COSTI
                                         cd_terzo               = aListaMovimenti(j).cd_terzo And
                                         TI_ISTITUZ_COMMERC     = aListaMovimenti(j).TI_ISTITUZ_COMMERC) Loop

                    aNewMov := aNewListMov(aNewListMov.Count);
                    aVoceIVAEp := CNRCTB204.trovaContoEp(aDocTst, aDocCori);
                    aNewMov.cd_voce_ep := aVoceIVAEp.cd_voce_ep;
                    -- 20.06.2006 STANI LE SCRITTURE DI RATEI PARTE 2 METTEVA SULL'IVA LO STESSO IMPORTO RIAPERTO DEL CONTO RATEI-FATT. DA EM.-FATT. DA RIC
                    --            PERCHE' NON RICOPRIVA L'IMPORTO
                    aNewMov.IM_MOVIMENTO := aDocCori.AMMONTARE;
            	    aNewMov.sezione := CNRCTB204.getSezione(aDocTst, aDocCori);

                    aNewListMov(aNewListMov.Count+1) := aNewMov;
                 End Loop;
     End if;
     end;
   End If;  -- del tipo documento per il giro sull'iva
			aNewMov := aNewListMov(aNewListMov.Count);
      tot_doc:=aNewListMov(aNewListMov.Count).IM_MOVIMENTO;
	For aCDoc In (Select  cd_terzo,TI_APPARTENENZA_EV,ti_gestione_ev,cd_elemento_voce_ev,SUM(IM_IMPONIBILE)  TOT
              From V_DOC_AMM_COGE_RIGA
              Where  cd_cds = aDocTst.cd_cds
                 And cd_unita_organizzativa = aDocTst.cd_unita_organizzativa
                 And esercizio = aDocTst.esercizio
    	         And pg_numero_documento = aDocTst.pg_numero_documento
    	         And cd_tipo_documento = aDocTst.cd_tipo_documento
    	         AND NOT EXISTS(SELECT 1 FROM SCRITTURA_PARTITA_DOPPIA WHERE
    	            CD_CAUSALE_COGE=CNRCTB200.CAU_RATEI_P2 AND
    	            SCRITTURA_PARTITA_DOPPIA.ESERCIZIO=aScrittura.esercizio and
								    cd_cds_documento = aDocTst.cd_cds
							    and esercizio_documento_amm = aDocTst.esercizio
								and cd_uo_documento = aDocTst.cd_unita_organizzativa
								and pg_numero_documento = aDocTst.pg_numero_documento
								and cd_tipo_documento = aDocTst.cd_tipo_documento
								and origine_scrittura = CNRCTB200.ORIGINE_DOCUMENTO_AMM
								and im_scrittura = tot_doc
								and attiva = 'Y')
    	         group by cd_terzo,TI_APPARTENENZA_EV,ti_gestione_ev,cd_elemento_voce_ev) Loop
    	         	--if (avoceEp.cd_voce_ep is not null) then
    	         	  --    aVoceEp:=null;
										  --exit;
								--end if;
               --aVoceEp:=null;
		Dbms_Output.PUT_LINE ('inizio loop sui V_DOC_AMM_COGE_RIGA per terzo '||Acdoc.cd_terzo||' '||acdoc.cd_elemento_voce_ev||' '||to_Char(aNewListMov(aNewListMov.Count).IM_MOVIMENTO)||' tot '||to_char(aCDoc.tot));
					      begin

                  select * into aDoc from V_DOC_AMM_COGE_RIGA
									                Where   cd_cds = aDocTst.cd_cds
									 		    and cd_unita_organizzativa = aDocTst.cd_unita_organizzativa
									 		    and esercizio = aDocTst.esercizio
									 		    and pg_numero_documento = aDocTst.pg_numero_documento
									 		    And cd_tipo_documento = aDocTst.cd_tipo_documento
									 		    and im_imponibile = tot_doc
									 	            and cd_terzo = aCDoc.cd_terzo
									 	            and (cd_elemento_voce_ev = aCDoc.cd_elemento_voce_ev ); -- COmpenso senza calcolo iva
											   -- and fl_pgiro = 'N';
										aVoceEp := CNRCTB204.trovaContoEp(aDocTst,aDoc);
									exception
											when no_data_found then
											  if(aVoceEp.cd_voce_ep is null) then
					      		   	dbms_output.put_line('no data');
												 select * into aDoc from V_DOC_AMM_COGE_RIGA
									                Where   cd_cds = aDocTst.cd_cds
									 		    and cd_unita_organizzativa = aDocTst.cd_unita_organizzativa
									 		    and esercizio = aDocTst.esercizio
									 		    and pg_numero_documento = aDocTst.pg_numero_documento
									 		    And cd_tipo_documento = aDocTst.cd_tipo_documento
									 	            and cd_terzo = aCDoc.cd_terzo
									 	             and (cd_elemento_voce_ev = aCDoc.cd_elemento_voce_ev ) -- COmpenso senza calcolo iva
											    --and fl_pgiro = 'N'
											    and rownum=1;
				             		aVoceEp := CNRCTB204.trovaContoEp(aDocTst,aDoc);
				                end if;
				             end;
				             Dbms_Output.PUT_LINE ('rateo '||aVoceEp.cd_voce_ep);
				             aNewListMov(aNewListMov.Count) := aNewMov;
		end loop;
		Dbms_Output.put_line ('aaaa');


   --aVoceEp := CNRCTB204.TROVACONTOANAG(aScrittura.esercizio, aNewListMov(aNewListMov.count).cd_terzo, aScrittura.cd_tipo_documento, aDocTst.ti_fattura,null,aNewListMov(aNewListMov.count).cd_voce_ep);
  	if(aVoceEp.cd_voce_ep not in ('A00068')) then
       aVoceEp := CNRCTB204.trovaContoContrEp(aScrittura.esercizio, null, null, null,aVoceEp.cd_voce_ep);
   -- else
     --   aNewListMov(aNewListMov.count-1).im_movimento := aNewListMov(aNewListMov.count-1).im_movimento+aNewListMov(aNewListMov.count).im_movimento;
    end if;
--Dbms_Output.put_line ('bbbb count '||aNewListMov.count||' '||aNewListMov(Count).sezione);

   CNRCTB204.BUILDCHIUSURASCRITTURA(aScrittura.cd_cds, aScrittura.esercizio, aScrittura.cd_unita_organizzativa, aVoceEp,
                                    aNewListMov(aNewListMov.count).dt_da_competenza_coge, aNewListMov(aNewListMov.count).dt_a_competenza_coge,
                                    aNewListMov(aNewListMov.count).cd_terzo,
                                    aNewListMov(aNewListMov.count).ti_istituz_commerc, aNewListMov, aUser, aTSNow);
      aVoceEp:=null;
    End If; -- del conto rateo/fatt. da emettere/ricevere

Dbms_Output.put_line ('end loop');

   End Loop; -- delle righe di ratei da chiudere

   If aNewListMov.count = 0 then
    return;
   End if;

   CNRCTB200.CREASCRITTCOGE(aScrittura, aNewListMov);
   aNewListaScrittureP2(aNewListaScrittureP2.count+1) := aScrittura;
End Loop;

If not CNRCTB200.isModificata(aListaVecchieScrittureP2, aNewListaScrittureP2) Then
   rollback to savepoint CNRCTB205_SP_002;
End If;
Dbms_Output.PUT_LINE ('FINE');
End;

 -- Operazioni di controllo per chiusura economica definitiva del CDS
 -- L'operazione viene fatta in una transazione
 --
 -- 1. Non devono esserci scritture con numerazione superiore alla prima scrittura generata in chiusura

 procedure checkChiudibilitaDef(aEs number, aCdCds varchar2) is
  aNumScrPostChiusura number;
  aPgStartChiusura number;
 begin
  if aCdCds is null then
   IBMERR001.RAISE_ERR_GENERICO('CDS non specificato');
  end if;

  for aUO in (select * from v_unita_organizzativa_valida where
             esercizio=aEs
		 and cd_unita_padre=aCdCds
		 order by cd_unita_organizzativa)
  loop
   -- Blocca numerazioni COGE e COAN
   for aNum in (select * from numerazione_coge_coan where
                      esercizio = aEs
				  and cd_cds = aCdCds
				  and cd_unita_organizzativa=aUO.cd_unita_organizzativa
				for update nowait
   ) loop
    null;
   end loop;
   aPgStartChiusura:=0;
   begin
    select min(pg_scrittura) into aPgStartChiusura from scrittura_partita_doppia
    where
                      esercizio = aEs
				  and cd_cds = aCdCds
				  and cd_unita_organizzativa=aUO.cd_unita_organizzativa
				  and cd_causale_coge in (
				    CNRCTB200.CAU_RISCONTI,
				    CNRCTB200.CAU_CHIUSURA_CONTO_ECONOMICO,
				    CNRCTB200.CAU_CHIUSURA_ST_PATRIMONIALE,
				    CNRCTB200.CAU_DET_UTILE_PERDITA,
				    CNRCTB200.CAU_AMMORTAMENTO
				  );
	exception when NO_DATA_FOUND then
     null;
	end;
	aNumScrPostChiusura:=0;
    select count(*) into aNumScrPostChiusura from scrittura_partita_doppia where
                      esercizio = aEs
				  and cd_cds = aCdCds
				  and cd_unita_organizzativa=aUO.cd_unita_organizzativa
				  and cd_causale_coge not in
				  (
				   CNRCTB200.CAU_RISCONTI,
				   CNRCTB200.CAU_CHIUSURA_CONTO_ECONOMICO,
				   CNRCTB200.CAU_CHIUSURA_ST_PATRIMONIALE,
				   CNRCTB200.CAU_DET_UTILE_PERDITA,
				   CNRCTB200.CAU_AMMORTAMENTO
				  )
				  and pg_scrittura > aPgStartChiusura;
   if aNumScrPostChiusura > 0 then
    IBMERR001.RAISE_ERR_GENERICO('Durante l''operazione di chiusura sono state registrate delle scritture economiche in cds:'||aCdCds||' per esercizio '||aEs);
   end if;
   aPgStartChiusura:=0;
   begin
    select min(pg_scrittura) into aPgStartChiusura from scrittura_analitica
    where
                      esercizio = aEs
				  and cd_cds = aCdCds
				  and cd_unita_organizzativa=aUO.cd_unita_organizzativa
				  and origine_scrittura = CNRCTB200.ORIGINE_CHIUSURA;
	exception when NO_DATA_FOUND then
     null;
	end;
	aNumScrPostChiusura := 0;
    select count(*) into aNumScrPostChiusura from scrittura_analitica where
                      esercizio = aEs
				  and cd_cds = aCdCds
				  and cd_unita_organizzativa=aUO.cd_unita_organizzativa
				  and origine_scrittura <> CNRCTB200.ORIGINE_CHIUSURA
				  and pg_scrittura > aPgStartChiusura;
   if aNumScrPostChiusura > 0 then
    IBMERR001.RAISE_ERR_GENERICO('Durante l''operazione di chiusura sono state registrate delle scritture analitiche in cds:'||aCdCds||' per esercizio '||aEs);
   end if;
  end loop;
 end;
end;
