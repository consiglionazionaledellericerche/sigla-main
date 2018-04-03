--------------------------------------------------------
--  DDL for Package Body CNRCTB560
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB560" AS

 procedure ins_ASS_COMP_DOC_CONT_NMP (aDest ASS_COMP_DOC_CONT_NMP%rowtype) is
  begin
   insert into ASS_COMP_DOC_CONT_NMP (
     ESERCIZIO_COMPENSO
    ,CD_UO_COMPENSO
    ,PG_COMPENSO
    ,CD_CDS_DOC
    ,ESERCIZIO_DOC
    ,PG_DOC
    ,CD_TIPO_DOC
    ,UTCR
    ,UTUV
    ,DACR
    ,DUVA
    ,PG_VER_REC
    ,CD_CDS_COMPENSO
   ) values (
     aDest.ESERCIZIO_COMPENSO
    ,aDest.CD_UO_COMPENSO
    ,aDest.PG_COMPENSO
    ,aDest.CD_CDS_DOC
    ,aDest.ESERCIZIO_DOC
    ,aDest.PG_DOC
    ,aDest.CD_TIPO_DOC
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DACR
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS_COMPENSO
    );
 end;

-- =============================================================================
-- Main procedura
-- =============================================================================

 procedure gestioneCori(
  inEsercizio number,
  aCompenso compenso%rowtype,
  aCori contributo_ritenuta%rowtype,
  aRev in out reversale%rowtype,
  aRevRighe in out CNRCTB038.righeReversaleList,
  aMan in out mandato%rowtype,
  aManRighe in out CNRCTB038.righeMandatoList,
  aUser varchar2,
  aTSNow date
 ) is
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aRevRiga reversale_riga%rowtype;
  aManRiga mandato_riga%rowtype;
  aAssCoriFin ass_tipo_cori_ev%rowtype;
  aVoceF voce_f%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aListGenRighe CNRCTB100.docGenRigaList;
  isCoriEntrata boolean;
  aContoColl ass_partita_giro%rowtype;
  aCdTerzoUO number(8);
  aCdModPagUO varchar2(10);
  aPgBancaUO number(10);
  aCdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE;
  aUOENTE unita_organizzativa%rowtype;
  aCdTerzoEnte number(8);
  aCdModPagEnte varchar2(10);
  aPgBancaEnte number(10);
  isCoriEnteSuCompNeg boolean;
  aDateCont date;
  parametriCnr  parametri_cnr%rowtype;
 begin
   aDateCont:=CNRCTB008.getTimestampContabile(inEsercizio,aTSNow);

   -- Se l'importo del CORi � non esegue alcuna operazione
   if aCori.ammontare = 0 then
    return;
   end if;

   -- Se la tipologia di contributo ritenuta �VA o RIVALSA non esegue la contabilizzazione

   aCdClassificazioneCori:=CNRCTB545.getTipoCoriDaRigaCompenso(aCori);
   IF (aCdClassificazioneCori = CNRCTB545.isCoriIva and aCompenso.fl_split_payment ='Y' and aCompenso.ti_istituz_commerc ='C' ) then
     return;
   Elsif ((aCdClassificazioneCori = CNRCTB545.isCoriIva and aCompenso.fl_split_payment ='N') OR
       aCdClassificazioneCori = CNRCTB545.isCoriRivalsa) THEN
      RETURN;
    Elsif (aCdClassificazioneCori = CNRCTB545.isCoriIva and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE) THEN
      RETURN;
   END IF;

   isCoriEnteSuCompNeg:=false;
   if (aCompenso.im_totale_compenso < 0) and (aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE) then
    isCoriEnteSuCompNeg:=true;
    -- Estrae il terzo associato all'UO ENTE con le sue modalit�i pagamento di tipo bancario pi� recenti
    aUOENTE:=CNRCTB020.GETUOENTE(inEsercizio);
    CNRCTB080.getTerzoPerUO(aUOENTE.cd_unita_organizzativa, aCdTerzoEnte, aCdModPagEnte, aPgBancaEnte,aCompenso.esercizio);
   end if;

   -- Estrae il terzo associato all'UO del compenso e le sue modalit�i pagamento di tipo bancario pi� recenti
   CNRCTB080.getTerzoPerUO(aCompenso.cd_uo_origine, aCdTerzoUO, aCdModPagUO, aPgBancaUO,aCompenso.esercizio);

		select * into parametriCnr from parametri_cnr
   where esercizio = inEsercizio;
   if (parametriCnr.fl_nuovo_pdg='N') then
	   begin
	    select * into aAssCoriFin from ass_tipo_cori_ev where
	         cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
	 	 and esercizio = inEsercizio
	     and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
		 and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
		 and ti_ente_percepiente = aCori.ti_ente_percipiente;
	   exception when NO_DATA_FOUND then
	    IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato al contributo '||aCori.cd_contributo_ritenuta||' non trovato');
	   end;
	  else
	   begin
	    select * into aAssCoriFin from ass_tipo_cori_ev where
	         cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
	 	 and esercizio = inEsercizio
	     and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
		 and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
		 and ti_ente_percepiente = aCori.ti_ente_percipiente;
	   exception when NO_DATA_FOUND then
	    IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato al contributo '||aCori.cd_contributo_ritenuta||' non trovato');
	   end;
	  end if;

   if (parametriCnr.fl_nuovo_pdg='N') then
		   begin
		    select * into aVoceF from voce_f where
			     esercizio = inEsercizio
		     and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
		     and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
			 and cd_titolo_capitolo = aAssCoriFin.cd_elemento_voce
			 and ti_voce = CNRCTB001.CAPITOLO;
		   exception when NO_DATA_FOUND then
		    IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato al contributo '||aCori.cd_contributo_ritenuta||'non trovato');
		   end;
   end if;
   -- Per ogni CORI stabilisce l'effetto COFI e determina la partita di giro
   if aCori.ammontare > 0 then
    isCoriEntrata:=true;
    aAcc:=null;
    aAccScad:=null;
    aAcc.CD_CDS:=aCompenso.cd_cds;
    aAcc.ESERCIZIO:=inEsercizio;
    aAcc.ESERCIZIO_ORIGINALE:=inEsercizio;
    aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO;
    aAcc.CD_UNITA_ORGANIZZATIVA:=aCompenso.cd_unita_organizzativa;
    aAcc.CD_CDS_ORIGINE:=aCompenso.cd_cds;
    aAcc.CD_UO_ORIGINE:=aCompenso.cd_unita_organizzativa;
    aAcc.TI_APPARTENENZA:=aAssCoriFin.ti_appartenenza;
    aAcc.TI_GESTIONE:=aAssCoriFin.ti_gestione;
    aAcc.CD_ELEMENTO_VOCE:=aAssCoriFin.cd_elemento_voce;
    if (parametriCnr.fl_nuovo_pdg='N') then
    	aAcc.CD_VOCE:=aVoceF.cd_voce;
    else
    	aAcc.CD_VOCE:=aAssCoriFin.cd_elemento_voce;
    end if;

    aAcc.DT_REGISTRAZIONE:=TRUNC(aDateCont);
    aAcc.DS_ACCERTAMENTO:='CORI-D cn.'||aCompenso.pg_compenso||' '||aCori.cd_contributo_ritenuta;
    aAcc.NOTE_ACCERTAMENTO:='';
		aAcc.CD_TERZO:=aCompenso.cd_terzo;
    aAcc.IM_ACCERTAMENTO:=aCori.ammontare;
    aAcc.FL_PGIRO:='Y';
    aAcc.RIPORTATO:='N';
    aAcc.DACR:=aTSNow;
    aAcc.UTCR:=aUser;
    aAcc.DUVA:=aTSNow;
    aAcc.UTUV:=aUser;
    aAcc.PG_VER_REC:=1;
    aAcc.ESERCIZIO_COMPETENZA:=inEsercizio;
    CNRCTB040.CREAACCERTAMENTOPGIRO(false,aAcc,aAccScad,aObb,aObbScad,trunc(aTSNow));
   else
    isCoriEntrata:=false;
	begin
 	 select * into aContoColl from ass_partita_giro where
	      esercizio = inEsercizio
	  and ti_appartenenza = aAssCoriFin.ti_appartenenza
	  and ti_gestione = aAssCoriFin.ti_gestione
	  and cd_voce = aAssCoriFin.cd_elemento_voce;
    exception when NO_DATA_FOUND then
	 IBMERR001.RAISE_ERR_GENERICO('Conto associato in partita di giro non trovato per voce di entrata:'||aAssCoriFin.cd_elemento_voce);
	end;
	aObb:=null;
    aObbScad:=null;
    aObb.CD_CDS:=aCompenso.cd_cds;
    aObb.ESERCIZIO:=inEsercizio;
    aObb.ESERCIZIO_ORIGINALE:=inEsercizio;
    aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
    aObb.CD_UNITA_ORGANIZZATIVA:=aCompenso.cd_unita_organizzativa;
    aObb.CD_CDS_ORIGINE:=aCompenso.cd_cds;
    aObb.CD_UO_ORIGINE:=aCompenso.cd_unita_organizzativa;
    aObb.TI_APPARTENENZA:=aContoColl.ti_appartenenza_clg;
    aObb.TI_GESTIONE:=aContoColl.ti_gestione_clg;
    aObb.CD_ELEMENTO_VOCE:=aContoColl.cd_voce_clg;
    aObb.DT_REGISTRAZIONE:=TRUNC(aDateCont);
    aObb.DS_OBBLIGAZIONE:='CORI-A cn.'||aCompenso.pg_compenso||' '||aCori.cd_contributo_ritenuta;
    aObb.NOTE_OBBLIGAZIONE:='';
    if isCoriEnteSuCompNeg then
     aObb.CD_TERZO:=aCdTerzoEnte;
    else
     aObb.CD_TERZO:=aCompenso.cd_terzo;
	end if;
	aObb.IM_OBBLIGAZIONE:=abs(aCori.ammontare);
    aObb.stato_obbligazione:=CNRCTB035.STATO_DEFINITIVO;
    aObb.im_costi_anticipati:=0;
    aObb.fl_calcolo_automatico:='N';
    aObb.fl_spese_costi_altrui:='N';
    aObb.FL_PGIRO:='Y';
    aObb.RIPORTATO:='N';
    aObb.DACR:=aTSNow;
    aObb.UTCR:=aUser;
    aObb.DUVA:=aTSNow;
    aObb.UTUV:=aUser;
    aObb.PG_VER_REC:=1;
    aObb.ESERCIZIO_COMPETENZA:=inEsercizio;
    CNRCTB030.CREAOBBLIGAZIONEPGIRO(false,aObb,aObbScad,aAcc,aAccScad,trunc(aTSNow));
   end if;

-- Creo il documento generico di entrata su partita di giro per il CORI specifico

   aGen:=null;
   aGenRiga:=null;

   if isCoriEntrata then
    aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_ACC_ENTRATA;
    aGen.CD_CDS:=aAcc.cd_cds;
    aGen.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
    aGen.ESERCIZIO:=aAcc.esercizio;
    aGen.CD_CDS_ORIGINE:=aAcc.cd_cds;
    aGen.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
    aGen.IM_TOTALE:=aAcc.im_accertamento;
   else
    aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_ACC_SPESA;
    aGen.CD_CDS:=aObb.cd_cds;
    aGen.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;
    aGen.ESERCIZIO:=aObb.esercizio;
    aGen.CD_CDS_ORIGINE:=aObb.cd_cds;
    aGen.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;
    aGen.IM_TOTALE:=aObb.im_obbligazione;
   end if;

   aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
   aGen.DT_DA_COMPETENZA_COGE:=aCompenso.DT_DA_COMPETENZA_COGE;
   aGen.DT_A_COMPETENZA_COGE:=aCompenso.DT_A_COMPETENZA_COGE;
   aGen.DS_DOCUMENTO_GENERICO:='CORI - cn.'||aCompenso.pg_compenso||' '||aCori.cd_contributo_ritenuta;
   aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
   aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
   aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
   aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
-- Massimo Iaccarino Inizio
   aGen.CD_DIVISA:='EURO';
   aGen.CAMBIO:=1;
-- Massimo Iaccarino Fine
--   aGen.ESERCIZIO_LETTERA:=0;
--   aGen.PG_LETTERA:=0;
   aGen.DACR:=aTSNow;
   aGen.UTCR:=aUser;
   aGen.DUVA:=aTSNow;
   aGen.UTUV:=aUser;
   aGen.PG_VER_REC:=1;
   aGen.DT_SCADENZA:=TRUNC(aTSNow);
   aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
   aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;

   aGenRiga.CD_CDS:=aGen.CD_CDS;
   aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
   aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
   aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
   aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
-- Massimo Iaccarino Inizio
   aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
   aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
--   aGenRiga.CD_TERZO_CESSIONARIO:=aGen.CD_TERZO_CESSIONARIO;
   aGenRiga.RAGIONE_SOCIALE:=aCompenso.RAGIONE_SOCIALE;
   aGenRiga.NOME:=aCompenso.NOME;
   aGenRiga.COGNOME:=aCompenso.COGNOME;
   aGenRiga.CODICE_FISCALE:=aCompenso.CODICE_FISCALE;
   aGenRiga.PARTITA_IVA:=aCompenso.PARTITA_IVA;
--   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
--   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
--   aGenRiga.NOTE:=aGen.NOTE;
   aGenRiga.DT_DA_COMPETENZA_COGE:=aCompenso.DT_DA_COMPETENZA_COGE;
   aGenRiga.DT_A_COMPETENZA_COGE:=aCompenso.DT_A_COMPETENZA_COGE;
   aGenRiga.STATO_COFI:=aGen.STATO_COFI;
--   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
   if isCoriEntrata then
    aGenRiga.CD_TERZO:=aCompenso.CD_TERZO;
    aGenRiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
    aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
    aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
    aGenRiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
    aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
    aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoUO;
    aGenRiga.PG_BANCA_UO_CDS:=aPgBancaUO;
    aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagUO;
   else
    if isCoriEnteSuCompNeg then
     aGenRiga.CD_TERZO:=aCdTerzoEnte;
     aGenRiga.CD_MODALITA_PAG:=aCdModPagEnte;
     aGenRiga.PG_BANCA:=aPgBancaEnte;
    else
     aGenRiga.CD_TERZO:=aCompenso.CD_TERZO;
     aGenRiga.CD_MODALITA_PAG:=aCompenso.cd_modalita_pag;
     aGenRiga.PG_BANCA:=aCompenso.pg_banca;
	end if;
	aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.CD_CDS;
    aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.ESERCIZIO;
    aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.ESERCIZIO_ORIGINALE;
    aGenRiga.PG_OBBLIGAZIONE:=aObb.PG_OBBLIGAZIONE;
    aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aObbScad.PG_OBBLIGAZIONE_SCADENZARIO;
   end if;
   aGenRiga.DACR:=aGen.DACR;
   aGenRiga.UTCR:=aGen.UTCR;
   aGenRiga.UTUV:=aGen.UTUV;
   aGenRiga.DUVA:=aGen.DUVA;
   aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
   aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
   aListGenRighe(1):=aGenRiga;
   CNRCTB110.CREAGENERICO(aGen,aListGenRighe);

   -- AGGIUNGO ALLA COLLEZIONE DELLE REVERSALI LA REVERSALE CORI CON I COLLEGAMENTI AL GENERICO IMPOSTATI

   if isCoriEntrata then
    aRev:=null;
    aRevRiga:=null;
    aRev.CD_CDS:=aAcc.cd_cds;
    aRev.ESERCIZIO:=aAcc.esercizio;
    aRev.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
    aRev.CD_CDS_ORIGINE:=aAcc.cd_cds;
    aRev.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
    aRev.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV;
    aRev.TI_REVERSALE:=CNRCTB038.TI_REV_INC;
    aRev.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
    aRev.DS_REVERSALE:='CORI-D cn.'||aCori.pg_compenso||' '||aCori.cd_contributo_ritenuta;
    aRev.STATO:=CNRCTB038.STATO_REV_EME;
    aRev.DT_EMISSIONE:=TRUNC(aDateCont);
 --   aRev.DT_TRASMISSIONE:=;
 --   aRev.DT_INCASSO:=;
 --   aRev.DT_ANNULLAMENTO:=;
    aRev.IM_REVERSALE:=aAcc.im_accertamento;
    aRev.IM_INCASSATO:=0;
    aRev.DACR:=aTSNow;
    aRev.UTCR:=aUser;
    aRev.DUVA:=aTSNow;
    aRev.UTUV:=aUser;
    aRev.PG_VER_REC:=1;
    aRev.STATO_TRASMISSIONE:=CNRCTB038.STATO_REV_TRASCAS_NODIST;
    aRevRiga.CD_CDS:=aRev.cd_cds;
    aRevRiga.ESERCIZIO:=aRev.esercizio;
    aRevRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.esercizio;
    aRevRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.esercizio_originale;
    aRevRiga.PG_ACCERTAMENTO:=aAcc.pg_accertamento;
    aRevRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
    aRevRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
    aRevRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
    aRevRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
    aRevRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
    aRevRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
    aRevRiga.DS_REVERSALE_RIGA:=aRev.ds_reversale;
    aRevRiga.STATO:=aRev.STATO;
    aRevRiga.CD_TERZO:=aGenRiga.cd_terzo;
    aRevRiga.CD_TERZO_UO:=aGenRiga.cd_terzo_uo_cds;
    aRevRiga.PG_BANCA:=aGenRiga.pg_banca_uo_cds;
    aRevRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag_uo_cds;
    aRevRiga.IM_REVERSALE_RIGA:=aRev.im_reversale;
    aRevRiga.FL_PGIRO:='Y';
    aRevRiga.UTCR:=aUser;
    aRevRiga.DACR:=aTSNow;
    aRevRiga.UTUV:=aUser;
    aRevRiga.DUVA:=aTSNow;
    aRevRiga.PG_VER_REC:=1;
    aRevRighe(1):=aRevRiga;
    -- Aggiornamento saldi scadenza accertamento
    update accertamento_scadenzario set
        im_associato_doc_amm = im_associato_doc_amm + aGen.im_totale,
        duva=aTSNow,
        utuv=aUser,
    	pg_ver_rec = pg_ver_rec+1
    where
            cd_cds = aAccScad.cd_cds
    	and esercizio = aAccScad.esercizio
    	and esercizio_originale = aAccScad.esercizio_originale
    	and pg_accertamento= aAccScad.pg_accertamento
    	and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;

    -- Aggiornamento CORI
    update contributo_ritenuta set
        stato_cofi_cr = CNRCTB100.STATO_COM_COFI_TOT_MR,
    	cd_cds_accertamento = aAcc.cd_cds,
    	esercizio_accertamento = aAcc.esercizio,
    	esercizio_ori_accertamento = aAcc.esercizio_originale,
    	pg_accertamento = aAcc.pg_accertamento,
    	pg_accertamento_scadenzario = 1, -- La scadenza �empre una
        duva=aTSNow,
        utuv=aUser,
    	pg_ver_rec = pg_ver_rec+1
    where
            cd_cds = aCompenso.cd_cds
    	and esercizio = aCompenso.esercizio
    	and cd_unita_organizzativa = aCompenso.cd_unita_organizzativa
    	and pg_compenso = aCompenso.pg_compenso
        and cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
		and ti_ente_percipiente = aCori.ti_ente_percipiente;
   else
    aMan:=null;
    aManRiga:=null;
    aMan.CD_CDS:=aObb.cd_cds;
    aMan.ESERCIZIO:=aObb.esercizio;
    aMan.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;
    aMan.CD_CDS_ORIGINE:=aObb.cd_cds;
    aMan.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;
    aMan.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
    aMan.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
    aMan.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
    aMan.DS_MANDATO:='CORI-C cn.'||aCori.pg_compenso||' '||aCori.cd_contributo_ritenuta;
    aMan.STATO:=CNRCTB038.STATO_AUT_EME;
    aMan.DT_EMISSIONE:=TRUNC(aDateCont);
 --   aMan.DT_TRASMISSIONE:=;
 --   aMan.DT_INCASSO:=;
 --   aMan.DT_ANNULLAMENTO:=;
    aMan.IM_MANDATO:=aObb.im_obbligazione;
    aMan.IM_RITENUTE:=0;
    aMan.IM_PAGATO:=0;
 -- Massimo Iaccarino Fine
    aMan.DACR:=aTSNow;
    aMan.UTCR:=aUser;
    aMan.DUVA:=aTSNow;
    aMan.UTUV:=aUser;
    aMan.PG_VER_REC:=1;
    aMan.STATO_TRASMISSIONE:=CNRCTB038.STATO_AUT_TRASCAS_NODIST;
    aManRiga.CD_CDS:=aMan.cd_cds;
    aManRiga.ESERCIZIO:=aMan.esercizio;
    aManRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.esercizio;
    aManRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.esercizio_originale;
    aManRiga.PG_OBBLIGAZIONE:=aObb.pg_obbligazione;
    aManRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
    aManRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
    aManRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
    aManRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
    aManRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
    aManRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
    aManRiga.DS_MANDATO_RIGA:=aMan.ds_mandato;
    aManRiga.STATO:=aMan.STATO;
    aManRiga.CD_TERZO:=aGenRiga.cd_terzo;
 --   aManRiga.CD_TERZO_UO:=0;
    aManRiga.PG_BANCA:=aGenRiga.PG_BANCA;
    aManRiga.CD_MODALITA_PAG:=aGenRiga.CD_MODALITA_PAG;
    aManRiga.IM_MANDATO_RIGA:=aMan.im_mandato;
    aManRiga.IM_RITENUTE_RIGA:=0;
    aManRiga.FL_PGIRO:='Y';
    aManRiga.UTCR:=aUser;
    aManRiga.DACR:=aTSNow;
    aManRiga.UTUV:=aUser;
    aManRiga.DUVA:=aTSNow;
    aManRiga.PG_VER_REC:=1;
    aManRighe(1):=aManRiga;
    -- Aggiornamento saldi scadenza accertamento
    update obbligazione_scadenzario set
        im_associato_doc_amm = im_associato_doc_amm + aGen.im_totale,
        duva=aTSNow,
        utuv=aUser,
    	pg_ver_rec = pg_ver_rec+1
    where
            cd_cds = aObbScad.cd_cds
    	and esercizio = aObbScad.esercizio
    	and esercizio_originale = aObbScad.esercizio_originale
    	and pg_obbligazione= aObbScad.pg_obbligazione
    	and pg_obbligazione_scadenzario = aObbScad.pg_obbligazione_scadenzario;

    -- Aggiornamento CORI
    update contributo_ritenuta set
        stato_cofi_cr = CNRCTB100.STATO_COM_COFI_TOT_MR,
    	cd_cds_obbligazione = aObb.cd_cds,
    	esercizio_obbligazione = aObb.esercizio,
    	esercizio_ori_obbligazione = aObb.esercizio_originale,
    	pg_obbligazione = aObb.pg_obbligazione,
    	pg_obbligazione_scadenzario = 1, -- La scadenza �empre una
        duva=aTSNow,
        utuv=aUser,
    	pg_ver_rec = pg_ver_rec+1
    where
            cd_cds = aCompenso.cd_cds
    	and esercizio = aCompenso.esercizio
    	and cd_unita_organizzativa = aCompenso.cd_unita_organizzativa
    	and pg_compenso = aCompenso.pg_compenso
        and cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
		and ti_ente_percipiente = aCori.ti_ente_percipiente;
   end if;
 end;

procedure gestioneNoCori(
  inEsercizio number,
  aCompenso compenso%rowtype,
  aRev in out reversale%rowtype,
  aRevRighe in out CNRCTB038.righeReversaleList,
  aUser varchar2,
  aTSNow date
 ) is
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aRevRiga reversale_riga%rowtype;
  aManRiga mandato_riga%rowtype;
  aElementoVoce elemento_voce.cd_elemento_voce%Type;
  aVoceF voce_f%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aListGenRighe CNRCTB100.docGenRigaList;
  aCdTerzoUO number(8);
  aCdModPagUO varchar2(10);
  aPgBancaUO number(10);
  aDateCont date;
  denominazione VARCHAR2(102);
   parametriCnr  parametri_cnr%rowtype;
 begin
   aDateCont:=CNRCTB008.getTimestampContabile(inEsercizio,aTSNow);

   -- Estrae il terzo associato all'UO del compenso e le sue modalit�i pagamento di tipo bancario pi� recenti
   CNRCTB080.getTerzoPerUO(aCompenso.cd_uo_origine, aCdTerzoUO, aCdModPagUO, aPgBancaUO,aCompenso.esercizio);

   -- Prendo da CONFIGURAZIONE_CNR l'elemento voce
   aElementoVoce := CNRCTB015.getVal01PerChiave(inEsercizio, 'ELEMENTO_VOCE_SPECIALE','NETTO_DA_TRATTENERE');
	if (parametriCnr.fl_nuovo_pdg='N') then
	   Begin
	      Select *
	      Into aVoceF
	      From voce_f
	      Where esercizio = inEsercizio
	        And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
	        And ti_gestione = CNRCTB001.GESTIONE_ENTRATE
	        And cd_titolo_capitolo = aElementoVoce
	        And ti_voce = CNRCTB001.CAPITOLO;
	   Exception when NO_DATA_FOUND then
	      IBMERR001.RAISE_ERR_GENERICO('Voce finanziaria associato all''elemento voce '||aElementoVoce||'non trovata');
	   End;


   end if;
   If aCompenso.cognome Is Not Null Then
       denominazione := aCompenso.cognome||' '||aCompenso.nome;
   Else
       denominazione := aCompenso.ragione_sociale;
   End If;

   -- Creo la PGIRO
    aAcc:=null;
    aAccScad:=null;
    aAcc.CD_CDS:=aCompenso.cd_cds;
    aAcc.ESERCIZIO:=inEsercizio;
    aAcc.ESERCIZIO_ORIGINALE:=inEsercizio;
    aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO;
    aAcc.CD_UNITA_ORGANIZZATIVA:=aCompenso.cd_unita_organizzativa;
    aAcc.CD_CDS_ORIGINE:=aCompenso.cd_cds;
    aAcc.CD_UO_ORIGINE:=aCompenso.cd_unita_organizzativa;
    aAcc.TI_APPARTENENZA:=aVoceF.ti_appartenenza;
    aAcc.TI_GESTIONE:=aVoceF.ti_gestione;
    aAcc.CD_ELEMENTO_VOCE:=aVoceF.cd_elemento_voce;
    if(parametriCnr.fl_nuovo_pdg='N' ) then
    	aAcc.TI_APPARTENENZA:=aVoceF.ti_appartenenza;
    	aAcc.TI_GESTIONE:=aVoceF.ti_gestione;
    	aAcc.CD_ELEMENTO_VOCE:=aVoceF.cd_elemento_voce;
    	aAcc.CD_VOCE:=aVoceF.cd_voce;
    else
    	aAcc.TI_APPARTENENZA:=CNRCTB001.APPARTENENZA_CNR;
    	aAcc.TI_GESTIONE:=CNRCTB001.GESTIONE_ENTRATE;
    	aAcc.CD_ELEMENTO_VOCE:=aElementoVoce;
    	aAcc.CD_VOCE:=aElementoVoce;
    end if;
    aAcc.DT_REGISTRAZIONE:=TRUNC(aDateCont);
    aAcc.DS_ACCERTAMENTO:='Sospensione del pagamento netto su Compenso '||aCompenso.pg_compenso||' - '||denominazione;
    aAcc.NOTE_ACCERTAMENTO:='';
    aAcc.CD_TERZO:=aCompenso.cd_terzo;
    aAcc.IM_ACCERTAMENTO:=aCompenso.im_netto_da_trattenere;
    aAcc.FL_PGIRO:='Y';
    aAcc.RIPORTATO:='N';
    aAcc.DACR:=aTSNow;
    aAcc.UTCR:=aUser;
    aAcc.DUVA:=aTSNow;
    aAcc.UTUV:=aUser;
    aAcc.PG_VER_REC:=1;
    aAcc.ESERCIZIO_COMPETENZA:=inEsercizio;
    CNRCTB040.CREAACCERTAMENTOPGIRO(false,aAcc,aAccScad,aObb,aObbScad,trunc(aTSNow));

    -- Etichetto le PGIRO create
    Update accertamento
    Set fl_netto_sospeso = 'Y'
    Where cd_cds = aAcc.cd_cds
      And esercizio = aAcc.esercizio
      And esercizio_originale = aAcc.esercizio_originale
      And pg_accertamento = aAcc.pg_accertamento;

    -- Poich�a PGIRO di spesa viene creata con terzo = creditori e debitori diversi,
    -- forzo il terzo uguale a quello della PGIRO di entrata
    Update obbligazione
    Set fl_netto_sospeso = 'Y',
        cd_terzo = aCompenso.cd_terzo
    Where cd_cds = aObb.cd_cds
      And esercizio = aObb.esercizio
      And esercizio_originale = aObb.esercizio_originale
      And pg_obbligazione = aObb.pg_obbligazione;

    -- Creo il documento generico di entrata sulla partita di giro appena creata
    aGen:=null;
    aGenRiga:=null;
    --il tipo deve essere necessariamente TI_GEN_CORI_ACC_ENTRATA perch�eve essere gestito come un cori
    --cio�nnullato quando si cancella il mandato del compenso
    aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_ACC_ENTRATA;
    aGen.CD_CDS:=aAcc.cd_cds;
    aGen.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
    aGen.ESERCIZIO:=aAcc.esercizio;
    aGen.CD_CDS_ORIGINE:=aAcc.cd_cds;
    aGen.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
    aGen.IM_TOTALE:=aAcc.im_accertamento;
    aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
    aGen.DT_DA_COMPETENZA_COGE:=aCompenso.DT_DA_COMPETENZA_COGE;
    aGen.DT_A_COMPETENZA_COGE:=aCompenso.DT_A_COMPETENZA_COGE;
    aGen.DS_DOCUMENTO_GENERICO:='Sospensione del pagamento netto su Compenso '||aCompenso.pg_compenso||' - '||denominazione;
    aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
    aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
    aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
    aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
    aGen.CD_DIVISA:='EURO';
    aGen.CAMBIO:=1;
    aGen.DACR:=aTSNow;
    aGen.UTCR:=aUser;
    aGen.DUVA:=aTSNow;
    aGen.UTUV:=aUser;
    aGen.PG_VER_REC:=1;
    aGen.DT_SCADENZA:=TRUNC(aTSNow);
    aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
    aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;

    aGenRiga.CD_CDS:=aGen.CD_CDS;
    aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
    aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
    aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
    aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
    aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
    aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
    aGenRiga.RAGIONE_SOCIALE:=aCompenso.RAGIONE_SOCIALE;
    aGenRiga.NOME:=aCompenso.NOME;
    aGenRiga.COGNOME:=aCompenso.COGNOME;
    aGenRiga.CODICE_FISCALE:=aCompenso.CODICE_FISCALE;
    aGenRiga.PARTITA_IVA:=aCompenso.PARTITA_IVA;
    aGenRiga.DT_DA_COMPETENZA_COGE:=aCompenso.DT_DA_COMPETENZA_COGE;
    aGenRiga.DT_A_COMPETENZA_COGE:=aCompenso.DT_A_COMPETENZA_COGE;
    aGenRiga.STATO_COFI:=aGen.STATO_COFI;
    aGenRiga.CD_TERZO:=aCompenso.CD_TERZO;
    aGenRiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
    aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
    aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
    aGenRiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
    aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
    aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoUO;
    aGenRiga.PG_BANCA_UO_CDS:=aPgBancaUO;
    aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagUO;
    aGenRiga.DACR:=aGen.DACR;
    aGenRiga.UTCR:=aGen.UTCR;
    aGenRiga.UTUV:=aGen.UTUV;
    aGenRiga.DUVA:=aGen.DUVA;
    aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
    aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
    aListGenRighe(1):=aGenRiga;
    CNRCTB110.CREAGENERICO(aGen,aListGenRighe);

    -- AGGIUNGO ALLA COLLEZIONE DELLE REVERSALI LA REVERSALE CORI CON I COLLEGAMENTI AL GENERICO IMPOSTATI
    aRev:=null;
    aRevRiga:=null;
    aRev.CD_CDS:=aAcc.cd_cds;
    aRev.ESERCIZIO:=aAcc.esercizio;
    aRev.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
    aRev.CD_CDS_ORIGINE:=aAcc.cd_cds;
    aRev.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
    aRev.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV;
    aRev.TI_REVERSALE:=CNRCTB038.TI_REV_INC;
    aRev.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
    aRev.DS_REVERSALE:='Sospensione del pagamento netto su Compenso '||aCompenso.pg_compenso||' - '||denominazione;
    aRev.STATO:=CNRCTB038.STATO_REV_EME;
    aRev.DT_EMISSIONE:=TRUNC(aDateCont);
    aRev.IM_REVERSALE:=aAcc.im_accertamento;
    aRev.IM_INCASSATO:=0;
    aRev.DACR:=aTSNow;
    aRev.UTCR:=aUser;
    aRev.DUVA:=aTSNow;
    aRev.UTUV:=aUser;
    aRev.PG_VER_REC:=1;
    aRev.STATO_TRASMISSIONE:=CNRCTB038.STATO_REV_TRASCAS_NODIST;
    aRevRiga.CD_CDS:=aRev.cd_cds;
    aRevRiga.ESERCIZIO:=aRev.esercizio;
    aRevRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.esercizio;
    aRevRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.esercizio_originale;
    aRevRiga.PG_ACCERTAMENTO:=aAcc.pg_accertamento;
    aRevRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
    aRevRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
    aRevRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
    aRevRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
    aRevRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
    aRevRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
    aRevRiga.DS_REVERSALE_RIGA:=aRev.ds_reversale;
    aRevRiga.STATO:=aRev.STATO;
    aRevRiga.CD_TERZO:=aGenRiga.cd_terzo;
    aRevRiga.CD_TERZO_UO:=aGenRiga.cd_terzo_uo_cds;
    aRevRiga.PG_BANCA:=aGenRiga.pg_banca_uo_cds;
    aRevRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag_uo_cds;
    aRevRiga.IM_REVERSALE_RIGA:=aRev.im_reversale;
    aRevRiga.FL_PGIRO:='Y';
    aRevRiga.UTCR:=aUser;
    aRevRiga.DACR:=aTSNow;
    aRevRiga.UTUV:=aUser;
    aRevRiga.DUVA:=aTSNow;
    aRevRiga.PG_VER_REC:=1;
    aRevRighe(1):=aRevRiga;
    -- Aggiornamento saldi scadenza accertamento
    update accertamento_scadenzario set
        im_associato_doc_amm = im_associato_doc_amm + aGen.im_totale,
        duva=aTSNow,
        utuv=aUser,
    	pg_ver_rec = pg_ver_rec+1
    where
            cd_cds = aAccScad.cd_cds
    	and esercizio = aAccScad.esercizio
    	and esercizio_originale = aAccScad.esercizio_originale
    	and pg_accertamento= aAccScad.pg_accertamento
    	and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;
/*
    -- Aggiornamento CORI
    update contributo_ritenuta set
        stato_cofi_cr = CNRCTB100.STATO_COM_COFI_TOT_MR,
    	cd_cds_accertamento = aAcc.cd_cds,
    	esercizio_accertamento = aAcc.esercizio,
    	esercizio_ori_accertamento = aAcc.esercizio_originale,
    	pg_accertamento = aAcc.pg_accertamento,
    	pg_accertamento_scadenzario = 1, -- La scadenza �empre una
        duva=aTSNow,
        utuv=aUser,
    	pg_ver_rec = pg_ver_rec+1
    where
            cd_cds = aCompenso.cd_cds
    	and esercizio = aCompenso.esercizio
    	and cd_unita_organizzativa = aCompenso.cd_unita_organizzativa
    	and pg_compenso = aCompenso.pg_compenso
        and cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
		and ti_ente_percipiente = aCori.ti_ente_percipiente;
*/
 End;


 PROCEDURE contabilizzaCompensoCOFI
      (
       inCDSCompenso VARCHAR2,
       inUOCompenso VARCHAR2,
       inEsercizio NUMBER,
       inEsercizioOrigine NUMBER,
       inPgCompenso NUMBER,
       aUser varchar2
	  ) IS
  aManP mandato%rowtype;
 begin
  contabilizzaCompensoCOFI
      (
	   'O',
       inCDSCompenso,
       inUOCompenso,
       inEsercizio,
       inEsercizioOrigine,
       inPgCompenso,
       aUser,
	   aManP
	  );
 end;

 PROCEDURE contabilizzaCompensoCOFIFondo
      (
       inCDSCompenso VARCHAR2,
       inUOCompenso VARCHAR2,
       inEsercizio NUMBER,
       inEsercizioCompenso NUMBER,
       inPgCompenso NUMBER,
       aUser varchar2,
	   aManP IN OUT mandato%rowtype
	  ) IS
 begin
  contabilizzaCompensoCOFI
      (
	   'F',
       inCDSCompenso,
       inUOCompenso,
       inEsercizio,
       inEsercizioCompenso,
       inPgCompenso,
       aUser,
	   aManP
	  );
 end;


 function getImp(aLRev IN OUT reversale%rowtype, aLRevRighe IN CNRCTB038.righeReversaleList) return number is
 begin
   if aLRevRighe.count > 0 then
    return aLRev.im_reversale;
   end if;
   return 0;
 end;

 function getImp(aLMan IN OUT mandato%rowtype, aLManRighe IN CNRCTB038.righeMandatoList) return number is
 begin
   if aLManRighe.count > 0 then
    return aLMan.im_mandato;
   end if;
   return 0;
 end;



 procedure setDaProcessareCoge(aLRev IN OUT reversale%rowtype, aLRevRighe IN CNRCTB038.righeReversaleList) is
 begin
   if aLRevRighe.count > 0 then
    aLRev.stato_coge := CNRCTB100.STATO_COEP_INI;
   end if;
 end;

 procedure setDaProcessareCoge(aLMan IN OUT mandato%rowtype, aLManRighe IN CNRCTB038.righeMandatoList) is
 begin
   if aLManRighe.count > 0 then
    aLMan.stato_coge := CNRCTB100.STATO_COEP_INI;
   end if;
 end;

 procedure addAssCompDocNMP(aCompenso compenso%rowtype,aMan mandato%rowtype, aManRighe CNRCTB038.righeMandatoList) is
  aAssCompDocNMP ASS_COMP_DOC_CONT_NMP%rowtype;
 begin
     if aManRighe.count = 0 then
      return;
	 end if;
     aAssCompDocNMP.CD_CDS_COMPENSO:=aCompenso.cd_cds;
     aAssCompDocNMP.ESERCIZIO_COMPENSO:=aCompenso.esercizio;
     aAssCompDocNMP.CD_UO_COMPENSO:=aCompenso.cd_unita_organizzativa;
     aAssCompDocNMP.PG_COMPENSO:=aCompenso.pg_compenso;
     aAssCompDocNMP.CD_CDS_DOC:=aMan.cd_cds;
     aAssCompDocNMP.ESERCIZIO_DOC:=aMan.esercizio;
     aAssCompDocNMP.PG_DOC:=aMan.pg_mandato;
     aAssCompDocNMP.CD_TIPO_DOC:=TI_MANDATO;
     aAssCompDocNMP.UTCR:=aMan.utcr;
     aAssCompDocNMP.UTUV:=aMan.utcr;
     aAssCompDocNMP.DACR:=aMan.dacr;
     aAssCompDocNMP.DUVA:=aMan.dacr;
     aAssCompDocNMP.PG_VER_REC:=1;
     ins_ASS_COMP_DOC_CONT_NMP(aAssCompDocNMP);
 end;

 procedure addAssCompDocNMP(aCompenso compenso%rowtype,aRev reversale%rowtype,aRevRighe CNRCTB038.righeReversaleList) is
  aAssCompDocNMP ASS_COMP_DOC_CONT_NMP%rowtype;
 begin
     if aRevRighe.count = 0 then
      return;
	 end if;
     aAssCompDocNMP.CD_CDS_COMPENSO:=aCompenso.cd_cds;
     aAssCompDocNMP.ESERCIZIO_COMPENSO:=aCompenso.esercizio;
     aAssCompDocNMP.CD_UO_COMPENSO:=aCompenso.cd_unita_organizzativa;
     aAssCompDocNMP.PG_COMPENSO:=aCompenso.pg_compenso;
     aAssCompDocNMP.CD_CDS_DOC:=aRev.cd_cds;
     aAssCompDocNMP.ESERCIZIO_DOC:=aRev.esercizio;
     aAssCompDocNMP.PG_DOC:=aRev.pg_reversale;
     aAssCompDocNMP.CD_TIPO_DOC:=TI_REVERSALE;
     aAssCompDocNMP.UTCR:=aRev.utcr;
     aAssCompDocNMP.UTUV:=aRev.utcr;
     aAssCompDocNMP.DACR:=aRev.dacr;
     aAssCompDocNMP.DUVA:=aRev.dacr;
     aAssCompDocNMP.PG_VER_REC:=1;
     ins_ASS_COMP_DOC_CONT_NMP(aAssCompDocNMP);
 end;

 PROCEDURE checkDisassRevCori(aComp compenso%rowtype,aCori contributo_ritenuta%rowtype,aRev in out reversale%rowtype,aRevRighe in out CNRCTB038.righeReversaleList) is
  aManTemp mandato%rowtype;
  aManTempRighe CNRCTB038.righeMandatoList;
 begin
  aManTemp:=null;
  aManTempRighe.delete;
  if
         aRevRighe.count > 0
	 and aCori.ammontare > 0
     and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_PERCEPIENTE
  then
   CNRCTB037.GENERADOCUMENTO(aManTemp,aManTempRighe,aRev,aRevRighe);
   addAssCompDocNMP(aComp, aRev, aRevRighe);
   aRev:=null;
   aRevRighe.delete;
  end if;
 end;

 PROCEDURE contabilizzaCompensoCOFI
      (
	   inOrigine char,
       inCDSCompenso VARCHAR2,
       inUOCompenso VARCHAR2,
       inEsercizio NUMBER,
       inEsercizioOrigine NUMBER,
       inPgCompenso NUMBER,
       aUser varchar2,
	   aManP IN OUT mandato%rowtype
	  ) IS
  aCompenso compenso%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aDettScadList CNRCTB035.scadVoceListE;
  aAccScadVoce accertamento_Scad_voce%rowtype;
  aVoceF voce_f%rowtype;
  aEvEntrata elemento_voce%rowtype;
  aTSNow date;
  aManPRiga mandato_riga%rowtype;
  aListRigheManP CNRCTB038.righeMandatoList;
  aAcc1 accertamento%rowtype;
  aAcc2 accertamento%rowtype;
  aAcc3 accertamento%rowtype;
  aAcc4 accertamento%rowtype;
  aAcc5 accertamento%rowtype;
  aAcc6 accertamento%rowtype;
  aAcc7 accertamento%rowtype;
  aAcc8 accertamento%rowtype;
  aAcc9 accertamento%rowtype;
  aAcc10 accertamento%rowtype;
  aAccNoCori accertamento%rowtype;
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aListGenRighe CNRCTB100.docGenRigaList;
  aGen1 documento_generico%rowtype;
  aGen2 documento_generico%rowtype;
  aGen3 documento_generico%rowtype;
  aGen4 documento_generico%rowtype;
  aGen5 documento_generico%rowtype;
  aGen6 documento_generico%rowtype;
  aGen7 documento_generico%rowtype;
  aGen8 documento_generico%rowtype;
  aGen9 documento_generico%rowtype;
  aGen10 documento_generico%rowtype;
  aGenNoCori documento_generico%rowtype;
  aRev1 reversale%rowtype;
  aRevRighe1 CNRCTB038.righeReversaleList;
  aRev2 reversale%rowtype;
  aRevRighe2 CNRCTB038.righeReversaleList;
  aRev3 reversale%rowtype;
  aRevRighe3 CNRCTB038.righeReversaleList;
  aRev4 reversale%rowtype;
  aRevRighe4 CNRCTB038.righeReversaleList;
  aRev5 reversale%rowtype;
  aRevRighe5 CNRCTB038.righeReversaleList;
  aRev6 reversale%rowtype;
  aRevRighe6 CNRCTB038.righeReversaleList;
  aRev7 reversale%rowtype;
  aRevRighe7 CNRCTB038.righeReversaleList;
  aRev8 reversale%rowtype;
  aRevRighe8 CNRCTB038.righeReversaleList;
  aRev9 reversale%rowtype;
  aRevRighe9 CNRCTB038.righeReversaleList;
  aRev10 reversale%rowtype;
  aRevRighe10 CNRCTB038.righeReversaleList;
  aRevNoCori reversale%rowtype;
  aRevRigheNoCori CNRCTB038.righeReversaleList;
  aMan1 mandato%rowtype;
  aManRighe1 CNRCTB038.righeMandatoList;
  aMan2 mandato%rowtype;
  aManRighe2 CNRCTB038.righeMandatoList;
  aMan3 mandato%rowtype;
  aManRighe3 CNRCTB038.righeMandatoList;
  aMan4 mandato%rowtype;
  aManRighe4 CNRCTB038.righeMandatoList;
  aMan5 mandato%rowtype;
  aManRighe5 CNRCTB038.righeMandatoList;
  aMan6 mandato%rowtype;
  aManRighe6 CNRCTB038.righeMandatoList;
  aMan7 mandato%rowtype;
  aManRighe7 CNRCTB038.righeMandatoList;
  aMan8 mandato%rowtype;
  aManRighe8 CNRCTB038.righeMandatoList;
  aMan9 mandato%rowtype;
  aManRighe9 CNRCTB038.righeMandatoList;
  aMan10 mandato%rowtype;
  aManRighe10 CNRCTB038.righeMandatoList;
  i number;
  aNumCRNeg number;
  aTotCori number(15,2);
  aTotCoriPositivi number(15,2);
  aTotCoriEntePosLiquid number(15,2);
  aUOENTE unita_organizzativa%rowtype;
  aCdTerzoUo NUMBER(8);
  aCdModPagUo VARCHAR2(10);
  aPgBancaUo NUMBER(10);
  aMissione missione%rowtype;
  aAnticipo anticipo%rowtype;
  aImportoMandato number(15,2);
  aTotManRev number(15,2);
  aCdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE;
  aAnagTerzoUo anagrafico%rowtype;
  isCompMissConAnt boolean;
  aCori1 contributo_ritenuta%rowtype;
  aCori2 contributo_ritenuta%rowtype;
  aCori3 contributo_ritenuta%rowtype;
  aCori4 contributo_ritenuta%rowtype;
  aCori5 contributo_ritenuta%rowtype;
  aCori6 contributo_ritenuta%rowtype;
  aCori7 contributo_ritenuta%rowtype;
  aCori8 contributo_ritenuta%rowtype;
  aCori9 contributo_ritenuta%rowtype;
  aCori10 contributo_ritenuta%rowtype;
  aDateCont date;
  parametriCnr  parametri_cnr%rowtype;
 BEGIN
  -- Check su esercizio
  if
   not CNRCTB008.ISESERCIZIOAPERTO(inEsercizio, inCdsCompenso)
  then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||inEsercizio||' non �perto per il CDS '||inCdsCompenso);
  end if;

  aTSNow:=sysdate;

  aDateCont:=CNRCTB008.getTimestampContabile(inEsercizio,aTSNow);

  if inOrigine not in ('O','F') then
   IBMERR001.RAISE_ERR_GENERICO('Origine di liquidazione non accettabile:'||inOrigine);
  end if;
  -- Fix errore 351
  if inPgCompenso < 0 then
   IBMERR001.RAISE_ERR_GENERICO('I compensi con numerazione provvisoria non sono contabilizzabili');
  end if;

  begin
      select * into aCompenso from compenso where
            cd_cds = inCDSCompenso
    	and esercizio = inEsercizioOrigine
    	and cd_unita_organizzativa = inUOCompenso
    	and pg_compenso = inPgCompenso
	--	and pg_obbligazione is not null -- PER ORA NON SONO GESTITI COMPENSI SU REVERSALE
		and stato_cofi = CNRCTB100.STATO_COM_COFI_CONT
      for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Compenso n.'||inPgCompenso||' cds:'||inCDSCompenso||' uo: '||inUOCompenso||' es:'||inEsercizioOrigine||'non trovato, non contabilizzato o gi�iquidato');
  end;

  -- Se il compenso passa per il fondo, non posso contabilizzarlo direttamente
  if inOrigine = 'O' and not (aCompenso.STATO_PAGAMENTO_FONDO_ECO = CNRCTB100.STATO_NO_PFONDOECO) then
   IBMERR001.RAISE_ERR_GENERICO('Il compenso risulta associato a fondo economale. Compenso n.'||inPgCompenso||' cds:'||inCDSCompenso||' uo: '||inUOCompenso||' es:'||inEsercizioOrigine);
  end if;

  if inOrigine = 'F' and (aCompenso.STATO_PAGAMENTO_FONDO_ECO = CNRCTB100.STATO_NO_PFONDOECO) then
   IBMERR001.RAISE_ERR_GENERICO('Il compenso non risulta essere associato a fondo economale. Compenso n.'||inPgCompenso||' cds:'||inCDSCompenso||' uo: '||inUOCompenso||' es:'||inEsercizioOrigine);
  end if;

  -- Se il compenso �enza obbligazione non carico le informazioni relative all'obbligazione e scadenza
  for aCompensoRiga in (select * from compenso_riga cr
                        where cr.cd_cds = aCompenso.cd_cds
                        and   cr.cd_unita_organizzativa = aCompenso.cd_unita_organizzativa
                        and   cr.esercizio = aCompenso.esercizio
                        and   cr.pg_compenso = aCompenso.pg_compenso) Loop
    declare
      aObb obbligazione%rowtype;
    begin
      select * into aObb from obbligazione 
      where cd_cds = aCompensoRiga.cd_cds
      and   esercizio = aCompensoRiga.esercizio_obbligazione
   	  and   esercizio_originale = aCompensoRiga.esercizio_ori_obbligazione
    	and   pg_obbligazione = aCompensoRiga.pg_obbligazione
      for update nowait;
    exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' n.'||aCompensoRiga.pg_obbligazione||' associata a Compenso n.'||inCDSCompenso||' non trovata');
    end;
    declare
      aObbScad obbligazione_scadenzario%rowtype;
    begin
      select * into aObbScad from obbligazione_scadenzario
      where cd_cds = aCompensoRiga.cd_cds
 	    and   esercizio = aCompensoRiga.esercizio_obbligazione
 	    and   esercizio_originale = aCompensoRiga.esercizio_ori_obbligazione
      and   pg_obbligazione = aCompensoRiga.pg_obbligazione
      and   pg_obbligazione_scadenzario = aCompensoRiga.pg_obbligazione_scadenzario
      for update nowait;
    exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('Scadenza di '||cnrutil.getLabelObbligazioneMin()||' n.'||aCompensoRiga.pg_obbligazione||' associata a Compenso n.'||inCDSCompenso||' non trovata');
    end;
  end loop;

  i:=0;
  aTotCori:=0;
  aTotCoriPositivi:=0;
  aTotCoriEntePosLiquid:=0;
  for aCori in (
   select * from contributo_ritenuta where
        cd_cds = inCDSCompenso
	and esercizio = inEsercizioOrigine
	and cd_unita_organizzativa = inUOCompenso
	and pg_compenso = inPgCompenso
	and ammontare <> 0
  ) loop
   if i>10 then
    IBMERR001.RAISE_ERR_GENERICO('Numero di contributi/ritenute con importo <> 0 superiore a 10 per il compenso '||aCompenso.pg_compenso);
   end if;
   i:=i+1;
   if i=1 then
    gestioneCori(inEsercizio,aCompenso,aCori,aRev1,aRevRighe1,aMan1,aManRighe1,aUser,aTSNow);
    if aRevRighe1.count > 0 and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE then
	 aTotCoriEntePosLiquid:=aTotCoriEntePosLiquid+aCori.ammontare;
	end if;
	aCori1:=aCori;
   end if;
   if i=2 then
    gestioneCori(inEsercizio,aCompenso,aCori,aRev2,aRevRighe2,aMan2,aManRighe2,aUser,aTSNow);
    if aRevRighe2.count > 0 and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE then
	 aTotCoriEntePosLiquid:=aTotCoriEntePosLiquid+aCori.ammontare;
	end if;
	aCori2:=aCori;
   end if;
   if i=3 then
    gestioneCori(inEsercizio,aCompenso,aCori,aRev3,aRevRighe3,aMan3,aManRighe3,aUser,aTSNow);
    if aRevRighe3.count > 0 and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE then
	 aTotCoriEntePosLiquid:=aTotCoriEntePosLiquid+aCori.ammontare;
	end if;
	aCori3:=aCori;
   end if;
   if i=4 then
    gestioneCori(inEsercizio,aCompenso,aCori,aRev4,aRevRighe4,aMan4,aManRighe4,aUser,aTSNow);
    if aRevRighe4.count > 0 and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE then
	 aTotCoriEntePosLiquid:=aTotCoriEntePosLiquid+aCori.ammontare;
	end if;
	aCori4:=aCori;
   end if;
   if i=5 then
    gestioneCori(inEsercizio,aCompenso,aCori,aRev5,aRevRighe5,aMan5,aManRighe5,aUser,aTSNow);
    if aRevRighe5.count > 0 and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE then
	 aTotCoriEntePosLiquid:=aTotCoriEntePosLiquid+aCori.ammontare;
	end if;
	aCori5:=aCori;
   end if;
   if i=6 then
    gestioneCori(inEsercizio,aCompenso,aCori,aRev6,aRevRighe6,aMan6,aManRighe6,aUser,aTSNow);
    if aRevRighe6.count > 0 and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE then
	 aTotCoriEntePosLiquid:=aTotCoriEntePosLiquid+aCori.ammontare;
	end if;
	aCori6:=aCori;
   end if;
   if i=7 then
    gestioneCori(inEsercizio,aCompenso,aCori,aRev7,aRevRighe7,aMan7,aManRighe7,aUser,aTSNow);
    if aRevRighe7.count > 0 and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE then
	 aTotCoriEntePosLiquid:=aTotCoriEntePosLiquid+aCori.ammontare;
	end if;
	aCori7:=aCori;
   end if;
   if i=8 then
    gestioneCori(inEsercizio,aCompenso,aCori,aRev8,aRevRighe8,aMan8,aManRighe8,aUser,aTSNow);
    if aRevRighe8.count > 0 and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE then
	 aTotCoriEntePosLiquid:=aTotCoriEntePosLiquid+aCori.ammontare;
	end if;
	aCori8:=aCori;
   end if;
   if i=9 then
    gestioneCori(inEsercizio,aCompenso,aCori,aRev9,aRevRighe9,aMan9,aManRighe9,aUser,aTSNow);
    if aRevRighe9.count > 0 and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE then
	 aTotCoriEntePosLiquid:=aTotCoriEntePosLiquid+aCori.ammontare;
	end if;
	aCori9:=aCori;
   end if;
   if i=10 then
    gestioneCori(inEsercizio,aCompenso,aCori,aRev10,aRevRighe10,aMan10,aManRighe10,aUser,aTSNow);
    if aRevRighe10.count > 0 and aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_ENTE then
	 aTotCoriEntePosLiquid:=aTotCoriEntePosLiquid+aCori.ammontare;
	end if;
	aCori10:=aCori;
   end if;

   aCdClassificazioneCori:=CNRCTB545.getTipoCoriDaRigaCompenso(aCori);

   IF not ((aCdClassificazioneCori = CNRCTB545.isCoriIva AND aCori.ti_ente_percipiente = CNRCTB100.TI_CARICO_PERCEPIENTE and aCompenso.fl_split_payment ='Y' and aCompenso.ti_istituz_commerc ='I') OR
       aCdClassificazioneCori = CNRCTB545.isCoriIva and (aCompenso.fl_split_payment ='N' or aCompenso.ti_istituz_commerc ='C') or
       aCdClassificazioneCori = CNRCTB545.isCoriRivalsa) THEN
    aTotCori:=aTotCori + aCori.ammontare;
	if aCori.ammontare > 0 then
 	 aTotCoriPositivi:=aTotCoriPositivi + aCori.ammontare;
    end if;
   END IF;
  end loop;

  If Nvl(aCompenso.im_netto_da_trattenere,0) > 0 And Nvl(aCompenso.im_totale_compenso,0) > 0 Then
     gestioneNoCori(inEsercizio,aCompenso,aRevNoCori,aRevRigheNoCori,aUser,aTSNow);
  End If;

  aImportoMandato:=aCompenso.im_totale_compenso;
  isCompMissConAnt:=false;
  if aCompenso.pg_missione is not null then
   begin
    select * into aMissione from missione where
	     esercizio = aCompenso.esercizio_missione
	 and cd_cds = aCompenso.cd_cds_missione
	 and cd_unita_organizzativa =aCompenso.cd_uo_missione
	 and pg_missione = aCompenso.pg_missione
	for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Missione collegata a compenso non trovata.');
   end;
   if aMissione.pg_anticipo is not null then
    begin
     select * into aAnticipo from anticipo where
 	     esercizio = aMissione.esercizio_anticipo
 	 and cd_cds = aMissione.cd_cds_anticipo
 	 and cd_unita_organizzativa =aMissione.cd_uo_anticipo
 	 and pg_anticipo = aMissione.pg_anticipo
 	 for update nowait;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Anticipo collegato a missione collegata a compenso non trovato.');
    end;
    isCompMissConAnt:=true;
	-- Se c'�nticipo sulla missione, prepara l'importo del mandato con la differenza tra netto percipiente e anticipo se > 0 altrimenti 0
	-- A questo importo verr�ommato il totale dei CORI presenti sul compenso.
	-- In questo modo se l'anticipo < del netto percipiente il mandato verr�iquidato per la somma dei cori + la differenza netto percipiente anticipo
    -- Altrimenti il mandato verr�iquidato per il totale dei cori (netto percipiente = 0)
	if aAnticipo.im_anticipo < aCompenso.im_netto_percipiente then
     aImportoMandato:=aCompenso.im_netto_percipiente - aAnticipo.im_anticipo;
    else
     aImportoMandato:=0;
	end if;
   end if;
  end if;
  -- Se l'importo del compenso �egativo, viene generato un accertamento sull'ente per il recupero del credito da terzi
  if aCompenso.im_totale_compenso < 0 then
   begin
    select * into aEvEntrata from elemento_voce where
        esercizio = inEsercizio
    and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
    and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
    and cd_elemento_voce = CNRCTB015.GETVAL01PERCHIAVE(aCompenso.esercizio,ELEMENTO_VOCE_SPECIALE,REC_CRED_DA_TERZI);
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Voce speciale di recupero crediti da terzi non trovata in configurazione cnr');
   end;
   select * into parametriCnr from parametri_cnr
   where esercizio = inEsercizio;
   if (parametriCnr.fl_nuovo_pdg='N') then
		   begin
		    select * into aVoceF from voce_f where
		        esercizio = inEsercizio
		    and ti_appartenenza = aEvEntrata.ti_appartenenza
		    and ti_gestione = aEvEntrata.ti_gestione
		    and cd_titolo_capitolo = aEvEntrata.cd_elemento_voce
		    and( cd_unita_organizzativa = aCompenso.cd_unita_organizzativa or
		      cd_unita_organizzativa is null)
		    and fl_mastrino = 'Y';
		   exception when NO_DATA_FOUND then
		    IBMERR001.RAISE_ERR_GENERICO('Articolo di entrata CNR non trovato per voce del piano:'||aEvEntrata.cd_elemento_voce||' su uo:'||aCompenso.cd_unita_organizzativa);
		   end;
		end if;
   aUOENTE:=CNRCTB020.getUOENTE(inEsercizio);

   CNRCTB080.GETTERZOPERUO(aCompenso.cd_unita_organizzativa, aCdTerzoUo, aCdModPagUo, aPgBancaUo ,aCompenso.esercizio);

   aAnagTerzoUO:=CNRCTB080.GETANAG(aCdterzoUo);

   aAcc:=null;
   aAccScad:=null;
   aAcc.CD_CDS:=aUOEnte.cd_unita_padre;
   aAcc.ESERCIZIO:=inEsercizio;
   aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC;
   aAcc.CD_UNITA_ORGANIZZATIVA:=aUOEnte.cd_unita_organizzativa;
   aAcc.CD_CDS_ORIGINE:=aCompenso.cd_cds;
   aAcc.CD_UO_ORIGINE:=aCompenso.cd_unita_organizzativa;
    if (parametriCnr.fl_nuovo_pdg='N') then
   			aAcc.TI_APPARTENENZA:=aVoceF.ti_appartenenza;
   			aAcc.TI_GESTIONE:=aVoceF.ti_gestione;
   	else
   		aAcc.TI_APPARTENENZA:=CNRCTB001.APPARTENENZA_CNR;
    	aAcc.TI_GESTIONE:=CNRCTB001.GESTIONE_ENTRATE;
   	end if;
   aAcc.CD_ELEMENTO_VOCE:=aEvEntrata.cd_elemento_voce;
   if (parametriCnr.fl_nuovo_pdg='N') then
   		aAcc.CD_VOCE:=aVoceF.cd_voce;
   	else
   		aAcc.CD_VOCE:=aEvEntrata.cd_elemento_voce;
   end if;
   aAcc.DT_REGISTRAZIONE:=TRUNC(aDateCont);
   aAcc.DS_ACCERTAMENTO:='Compenso negativo n.'||aCompenso.pg_compenso||' cds:'||aCompenso.cd_cds||' uo:'||aCompenso.cd_unita_organizzativa;
   aAcc.NOTE_ACCERTAMENTO:='';
   aAcc.CD_TERZO:=aCdTerzoUo; -- Richiesta CINECA del 18/10/2002 old -> aCompenso.cd_terzo;
   aAcc.IM_ACCERTAMENTO:=abs(aCompenso.im_totale_compenso);
   aAcc.FL_PGIRO:='N';
   aAcc.RIPORTATO:='N';
   aAcc.DACR:=aTSNow;
   aAcc.UTCR:=aUser;
   aAcc.DUVA:=aTSNow;
   aAcc.UTUV:=aUser;
   aAcc.PG_VER_REC:=1;
   aAcc.ESERCIZIO_COMPETENZA:=inEsercizio;
   aAccScad.CD_CDS:=aAcc.CD_CDS;
   aAccScad.ESERCIZIO:=aAcc.ESERCIZIO;
   aAccScad.ESERCIZIO_ORIGINALE:=aAcc.ESERCIZIO_ORIGINALE;
   aAccScad.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
   aAccScad.PG_ACCERTAMENTO_SCADENZARIO:=1;
   aAccScad.DT_SCADENZA_EMISSIONE_FATTURA:=trunc(aTSNow);
   aAccScad.DT_SCADENZA_INCASSO:=trunc(aTSNow);
   aAccScad.DS_SCADENZA:=aAcc.DS_ACCERTAMENTO;
   aAccScad.IM_SCADENZA:=aAcc.IM_ACCERTAMENTO;
   aAccScad.IM_ASSOCIATO_DOC_AMM:=aAcc.IM_ACCERTAMENTO;
   aAccScad.IM_ASSOCIATO_DOC_CONTABILE:=0;
   aAccScad.DACR:=aAcc.DACR;
   aAccScad.UTCR:=aAcc.UTCR;
   aAccScad.DUVA:=aAcc.DUVA;
   aAccScad.UTUV:=aAcc.UTUV;
   aAccScad.PG_VER_REC:=aAcc.PG_VER_REC;
   aAccScadVoce.CD_CDS:=aAccScad.CD_CDS;
   aAccScadVoce.ESERCIZIO:=aAccScad.ESERCIZIO;
   aAccScadVoce.ESERCIZIO_ORIGINALE:=aAccScad.ESERCIZIO_ORIGINALE;
   aAccScadVoce.PG_ACCERTAMENTO:=aAccScad.PG_ACCERTAMENTO;
   aAccScadVoce.PG_ACCERTAMENTO_SCADENZARIO:=aAccScad.PG_ACCERTAMENTO_SCADENZARIO;

   if aCompenso.CD_CDR_GENRC is null or aCompenso.CD_LINEA_ATTIVITA_GENRC is null then
    IBMERR001.RAISE_ERR_GENERICO('Linea di attivit�er creazione dell''accertamento di recupero crediti da terzi non specificata in compenso:'||aCompenso.pg_compenso);
   end if;
   aAccScadVoce.CD_CENTRO_RESPONSABILITA:=aCompenso.CD_CDR_GENRC;
   aAccScadVoce.CD_LINEA_ATTIVITA:=aCompenso.CD_LINEA_ATTIVITA_GENRC;

   aAccScadVoce.IM_VOCE:=aAccScad.IM_SCADENZA;
   aAccScadVoce.CD_FONDO_RICERCA:=null;
   aAccScadVoce.DACR:=aAccScad.DACR;
   aAccScadVoce.UTCR:=aAccScad.UTCR;
   aAccScadVoce.DUVA:=aAccScad.DUVA;
   aAccScadVoce.UTUV:=aAccScad.UTUV;
   aAccScadVoce.PG_VER_REC:=aAccScad.PG_VER_REC;
   aDettScadList(1):=aAccScadVoce;
   CNRCTB040.CREAACCERTAMENTO(false,aAcc,aAccScad,aDettScadList);

   aGen:=null;
   aGenRiga:=null;
   aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_REC_CRED_DA_TERZI;
   aGen.CD_CDS:=aAcc.cd_cds;
   aGen.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
   aGen.ESERCIZIO:=aAcc.esercizio;
   aGen.CD_CDS_ORIGINE:=aCompenso.cd_cds;
   aGen.CD_UO_ORIGINE:=aCompenso.cd_unita_organizzativa;
   aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
   aGen.DT_DA_COMPETENZA_COGE:=aCompenso.DT_DA_COMPETENZA_COGE;
   aGen.DT_A_COMPETENZA_COGE:=aCompenso.DT_A_COMPETENZA_COGE;
   aGen.DS_DOCUMENTO_GENERICO:=aAcc.ds_accertamento;
   aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
   aGen.IM_TOTALE:=aAcc.im_accertamento;
   aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_CONT;
   aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
   aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
   aGen.CD_DIVISA:='EURO';
   aGen.CAMBIO:=1;
--   aGen.ESERCIZIO_LETTERA:=0;
--   aGen.PG_LETTERA:=0;
   aGen.DACR:=aTSNow;
   aGen.UTCR:=aUser;
   aGen.DUVA:=aTSNow;
   aGen.UTUV:=aUser;
   aGen.PG_VER_REC:=1;
   aGen.DT_SCADENZA:=TRUNC(aTSNow);
   aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
   aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_NON_ASSOC_MAN_REV ;
   aGenRiga.CD_CDS:=aGen.CD_CDS;
   aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
   aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
   aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
   aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
   aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
   aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
   aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoUO;
--   aGenRiga.CD_MODALITA_PAG:=;
--   aGenRiga.PG_BANCA:=;
   aGenRiga.PG_BANCA_UO_CDS:=aPgBancaUO;
   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagUo;
   aGenRiga.CD_TERZO:=aAcc.CD_TERZO;
   aGenRiga.RAGIONE_SOCIALE:=aAnagTerzoUO.RAGIONE_SOCIALE;
   aGenRiga.NOME:=aAnagTerzoUO.NOME;
   aGenRiga.COGNOME:=aAnagTerzoUO.COGNOME;
   aGenRiga.CODICE_FISCALE:=aAnagTerzoUO.CODICE_FISCALE;
   aGenRiga.PARTITA_IVA:=aAnagTerzoUO.PARTITA_IVA;
--   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
--   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
--   aGenRiga.NOTE:=aGen.NOTE;
   aGenRiga.DT_DA_COMPETENZA_COGE:=aCompenso.DT_DA_COMPETENZA_COGE;
   aGenRiga.DT_A_COMPETENZA_COGE:=aCompenso.DT_A_COMPETENZA_COGE;
   aGenRiga.STATO_COFI:=aGen.STATO_COFI;
--   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
   aGenRiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
   aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
   aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
   aGenRiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
   aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
   aGenRiga.DACR:=aGen.DACR;
   aGenRiga.UTCR:=aGen.UTCR;
   aGenRiga.UTUV:=aGen.UTUV;
   aGenRiga.DUVA:=aGen.DUVA;
   aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
   aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
   aListGenRighe(1):=aGenRiga;
   CNRCTB110.CREAGENERICO(aGen,aListGenRighe);

   -- Aggiornamento del compenso con informazioni relative a doc generico e accertamento generati al centro

   CNRCTB100.updateDocAmm(
    CNRCTB100.TI_COMPENSO,
    aCompenso.cd_cds,
    aCompenso.esercizio,
    aCompenso.cd_unita_organizzativa,
    aCompenso.pg_compenso,
    ' esercizio_accertamento='''||aAccScad.esercizio||''',
      cd_cds_accertamento='''||aAccScad.cd_cds||''',
      esercizio_ori_accertamento='''||aAccScad.esercizio_originale||''',
      pg_accertamento='''||aAccScad.pg_accertamento||''',
      pg_accertamento_scadenzario='''||aAccScad.pg_accertamento_scadenzario||''',
      esercizio_doc_genrc='''||aGen.esercizio||''',
      cd_cds_doc_genrc='''||aGen.cd_cds||''',
      cd_uo_doc_genrc='''||aGen.cd_unita_organizzativa||''',
      pg_doc_genrc='''||aGen.pg_documento_generico||''',
      cd_tipo_doc_genrc='''||aGen.cd_tipo_documento_amm||'''
	',
    null,
    aUser,
    aTSNow
   );

  -- Se l'importo totale del compenso � 0 non genero alcun mandato principale
  elsif  aCompenso.im_totale_compenso = 0  then
   null;
  -- Se l'importo totale del compenso �aggiore di 0, viene registrato il mandato al netto di eventuali anticipi su missione collegati al compenso in processo
  else
   -- Calcolo il totale delle reversali/mandati collegati a mandato principale
   -- Se l'importo � 0 blocco l'operazione
   aTotManRev:=0;
   aTotManRev:=aTotManRev + getImp(aRev1, aRevRighe1);
   aTotManRev:=aTotManRev + getImp(aRev2, aRevRighe2);
   aTotManRev:=aTotManRev + getImp(aRev3, aRevRighe3);
   aTotManRev:=aTotManRev + getImp(aRev4, aRevRighe4);
   aTotManRev:=aTotManRev + getImp(aRev5, aRevRighe5);
   aTotManRev:=aTotManRev + getImp(aRev6, aRevRighe6);
   aTotManRev:=aTotManRev + getImp(aRev7, aRevRighe7);
   aTotManRev:=aTotManRev + getImp(aRev8, aRevRighe8);
   aTotManRev:=aTotManRev + getImp(aRev9, aRevRighe9);
   aTotManRev:=aTotManRev + getImp(aRev10, aRevRighe10);
   aTotManRev:=aTotManRev + getImp(aRevNoCori, aRevRigheNoCori);
   aTotManRev:=aTotManRev - getImp(aMan1, aManRighe1);
   aTotManRev:=aTotManRev - getImp(aMan2, aManRighe2);
   aTotManRev:=aTotManRev - getImp(aMan3, aManRighe3);
   aTotManRev:=aTotManRev - getImp(aMan4, aManRighe4);
   aTotManRev:=aTotManRev - getImp(aMan5, aManRighe5);
   aTotManRev:=aTotManRev - getImp(aMan6, aManRighe6);
   aTotManRev:=aTotManRev - getImp(aMan7, aManRighe7);
   aTotManRev:=aTotManRev - getImp(aMan8, aManRighe8);
   aTotManRev:=aTotManRev - getImp(aMan9, aManRighe9);
   aTotManRev:=aTotManRev - getImp(aMan10, aManRighe10);
   -- Il compenso �ollegato a missione con anticipo, sommo al delta netto compenso meno anticipo il totale CORI
   if isCompMissConAnt then
    aImportoMandato:=aImportoMandato+aTotManRev;
   end if;
   if aImportoMandato > 0 then
    aManP.CD_CDS:=aCompenso.cd_cds;
    aManP.ESERCIZIO:=inEsercizio;
    aManP.CD_UNITA_ORGANIZZATIVA:=aCompenso.cd_unita_organizzativa;
    aManP.CD_CDS_ORIGINE:=aCompenso.cd_cds;
    aManP.CD_UO_ORIGINE:=aCompenso.cd_unita_organizzativa;
    aManP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
    aManP.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
    aManP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
    aManP.DS_MANDATO:='Mandato di liquidazione compenso: '||aCompenso.pg_compenso;
    aManP.STATO:=CNRCTB038.STATO_MAN_EME;
    aManP.DT_EMISSIONE:=TRUNC(aDateCont);
  --  aManP.DT_TRASMISSIONE:=;
  --  aManP.DT_PAGAMENTO:=;
  --  aManP.DT_ANNULLAMENTO:=;
    aManP.IM_MANDATO:=aImportoMandato;
    aManP.IM_PAGATO:=0;
    aManP.UTCR:=aUser;
    aManP.DACR:=aTSNow;
    aManP.UTUV:=aUser;
    aManP.DUVA:=aTSNow;
    aManP.PG_VER_REC:=1;
    aManP.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;
    aManP.im_ritenute:=aTotCoriPositivi+Nvl(aCompenso.im_netto_da_trattenere,0);
    declare 
      resImpMandato number:=aManP.im_mandato;
      resRitenute   number:=aManP.im_ritenute;
      myindex       number:=0;
    begin
      for recCompensoRiga in (select * from Compenso_Riga
                              where cd_cds = aCompenso.cd_cds
                              and   cd_unita_organizzativa = aCompenso.cd_unita_organizzativa
                              and   esercizio = aCompenso.esercizio
                              and   pg_compenso = aCompenso.pg_compenso) Loop
        aManPRiga.CD_CDS:=aManP.cd_cds;
        aManPRiga.ESERCIZIO:=aManP.esercizio;
        aManPRiga.ESERCIZIO_OBBLIGAZIONE:=recCompensoRiga.esercizio_obbligazione;
        aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=recCompensoRiga.esercizio_ori_obbligazione;
        aManPRiga.PG_OBBLIGAZIONE:=recCompensoRiga.pg_obbligazione;
        aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=recCompensoRiga.pg_obbligazione_scadenzario;
        aManPRiga.CD_CDS_DOC_AMM:=aCompenso.cd_cds;
        aManPRiga.CD_UO_DOC_AMM:=aCompenso.cd_unita_organizzativa;
        aManPRiga.ESERCIZIO_DOC_AMM:=aCompenso.esercizio;
        aManPRiga.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_COMPENSO;
        aManPRiga.PG_DOC_AMM:=aCompenso.PG_COMPENSO;
        aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
        aManPRiga.STATO:=aManP.stato;
        aManPRiga.CD_TERZO:=aCompenso.cd_terzo;
        aManPRiga.PG_BANCA:=aCompenso.pg_banca;
        aManPRiga.CD_MODALITA_PAG:=aCompenso.cd_modalita_pag;

        aManPRiga.IM_MANDATO_RIGA:=ROUND(aManP.im_mandato*recCompensoRiga.im_totale_riga_compenso/aCompenso.im_totale_compenso,2);
        aManPRiga.im_ritenute_riga:=ROUND(aManP.im_ritenute*recCompensoRiga.im_totale_riga_compenso/aCompenso.im_totale_compenso,2);
        resImpMandato := resImpMandato-aManPRiga.IM_MANDATO_RIGA;
        resRitenute := resRitenute-aManPRiga.im_ritenute_riga;

        aManPRiga.FL_PGIRO:='N';
        aManPRiga.UTCR:=aUser;
        aManPRiga.DACR:=aTSNow;
        aManPRiga.UTUV:=aUser;
        aManPRiga.DUVA:=aTSNow;
        aManPRiga.PG_VER_REC:=1;
        myindex := myindex + 1;
        aListRigheManP(myindex):=aManPRiga;
      end loop;

      if resImpMandato>0 or resRitenute>0 Then
         aListRigheManP(myindex).IM_MANDATO_RIGA := aListRigheManP(myindex).IM_MANDATO_RIGA+resImpMandato;
         aListRigheManP(myindex).im_ritenute_riga := aListRigheManP(myindex).im_ritenute_riga+resRitenute;
      end if;
    end;
   end if;
  end if;
  -- Aggiorno lo stato del documento
  CNRCTB100.updateDocAmm(
   CNRCTB100.TI_COMPENSO,
    aCompenso.cd_cds,
    aCompenso.esercizio,
    aCompenso.cd_unita_organizzativa,
    aCompenso.pg_compenso,
    ' dt_emissione_mandato = '||IBMUTL001.ASDYNDATE(TRUNC(aDateCont))||', stato_cofi = '''||CNRCTB100.STATO_COM_COFI_TOT_MR||''', ti_associato_manrev = '''||CNRCTB100.TI_ASSOC_TOT_MAN_REV||'''',
    null,
    aUser,
    aTSNow
   );

  -- Impone il flag stato_coge a 'N' sulle reversali svincolate da mandato principale (totale compenso negativo o nullo)
  -- in modo che vengano correttamente processate in COGE
  if aCompenso.im_totale_compenso <= 0 or aImportoMandato <= 0 then
   setDaProcessareCoge(aRev1, aRevRighe1);
   setDaProcessareCoge(aRev2, aRevRighe2);
   setDaProcessareCoge(aRev3, aRevRighe3);
   setDaProcessareCoge(aRev4, aRevRighe4);
   setDaProcessareCoge(aRev5, aRevRighe5);
   setDaProcessareCoge(aRev6, aRevRighe6);
   setDaProcessareCoge(aRev7, aRevRighe7);
   setDaProcessareCoge(aRev8, aRevRighe8);
   setDaProcessareCoge(aRev9, aRevRighe9);
   setDaProcessareCoge(aRev10, aRevRighe10);
   setDaProcessareCoge(aMan1, aManRighe1);
   setDaProcessareCoge(aMan2, aManRighe2);
   setDaProcessareCoge(aMan3, aManRighe3);
   setDaProcessareCoge(aMan4, aManRighe4);
   setDaProcessareCoge(aMan5, aManRighe5);
   setDaProcessareCoge(aMan6, aManRighe6);
   setDaProcessareCoge(aMan7, aManRighe7);
   setDaProcessareCoge(aMan8, aManRighe8);
   setDaProcessareCoge(aMan9, aManRighe9);
   setDaProcessareCoge(aMan10, aManRighe10);
  end if;
  -- Se esiste mandato principale e l'importo del mandato principale
  -- �inore del totale dei CORI positivi liquidati
  if aListRighemanP.count > 0 and aManP.im_mandato < aTotCoriPositivi then
   -- Se l'importo del mandato principale �aggiore o uguale al totale
   -- dei cori ente positivi liquidati (con reversale)
   if aManP.im_mandato >= aTotCoriEntePosLiquid then
   -- Devo disaccoppiare i CORI percipiente positivi liquidati (con reversale) dal mandato principale del compenso
    checkDisassRevCori(aCompenso,aCori1,aRev1,aRevRighe1);
    checkDisassRevCori(aCompenso,aCori2,aRev2,aRevRighe2);
    checkDisassRevCori(aCompenso,aCori3,aRev3,aRevRighe3);
    checkDisassRevCori(aCompenso,aCori4,aRev4,aRevRighe4);
    checkDisassRevCori(aCompenso,aCori5,aRev5,aRevRighe5);
    checkDisassRevCori(aCompenso,aCori6,aRev6,aRevRighe6);
    checkDisassRevCori(aCompenso,aCori7,aRev7,aRevRighe7);
    checkDisassRevCori(aCompenso,aCori8,aRev8,aRevRighe8);
    checkDisassRevCori(aCompenso,aCori9,aRev9,aRevRighe9);
    checkDisassRevCori(aCompenso,aCori10,aRev10,aRevRighe10);
    -- Metto l'importo ritenute uguale al totale dei CORI ente positivi
	aManP.im_ritenute:=aTotCoriEntePosLiquid;
   -- Altrimenti sollevo un'eccezione
   else
    IBMERR001.RAISE_ERR_GENERICO('Il compenso non pu�nerare mandato principale perch� CORI ente liquidati con reversali hanno importo maggiore dell''importo del mandato principale del compenso:'||aCompenso.pg_compenso);
   end if;
  end if;
--PIPE.SEND_MESSAGE('aManP.IM_MANDATO = '||aManP.IM_MANDATO||' -  aManP.IM_RITENUTE = '|| aManP.IM_RITENUTE);
  CNRCTB037.GENERADOCUMENTI(
   aManP,
   aListRigheManP,
   aRev1, aRevRighe1,
   aRev2, aRevRighe2,
   aRev3, aRevRighe3,
   aRev4, aRevRighe4,
   aRev5, aRevRighe5,
   aRev6, aRevRighe6,
   aRev7, aRevRighe7,
   aRev8, aRevRighe8,
   aRev9, aRevRighe9,
   aRev10, aRevRighe10,
   aRevNoCori, aRevRigheNoCori,
   aMan1, aManRighe1,
   aMan2, aManRighe2,
   aMan3, aManRighe3,
   aMan4, aManRighe4,
   aMan5, aManRighe5,
   aMan6, aManRighe6,
   aMan7, aManRighe7,
   aMan8, aManRighe8,
   aMan9, aManRighe9,
   aMan10, aManRighe10
  );
  -- Se non c'�andato principale aggiorno la tabella di associazione con doc autor. CORI per compenso senza mandato
  if aListRigheManP.count = 0 then
   addAssCompDocNMP(aCompenso, aMan1, aManRighe1);
   addAssCompDocNMP(aCompenso, aMan2, aManRighe2);
   addAssCompDocNMP(aCompenso, aMan3, aManRighe3);
   addAssCompDocNMP(aCompenso, aMan4, aManRighe4);
   addAssCompDocNMP(aCompenso, aMan5, aManRighe5);
   addAssCompDocNMP(aCompenso, aMan6, aManRighe6);
   addAssCompDocNMP(aCompenso, aMan7, aManRighe7);
   addAssCompDocNMP(aCompenso, aMan8, aManRighe8);
   addAssCompDocNMP(aCompenso, aMan9, aManRighe9);
   addAssCompDocNMP(aCompenso, aMan10, aManRighe10);
   addAssCompDocNMP(aCompenso, aRev1, aRevRighe1);
   addAssCompDocNMP(aCompenso, aRev2, aRevRighe2);
   addAssCompDocNMP(aCompenso, aRev3, aRevRighe3);
   addAssCompDocNMP(aCompenso, aRev4, aRevRighe4);
   addAssCompDocNMP(aCompenso, aRev5, aRevRighe5);
   addAssCompDocNMP(aCompenso, aRev6, aRevRighe6);
   addAssCompDocNMP(aCompenso, aRev7, aRevRighe7);
   addAssCompDocNMP(aCompenso, aRev8, aRevRighe8);
   addAssCompDocNMP(aCompenso, aRev9, aRevRighe9);
   addAssCompDocNMP(aCompenso, aRev10, aRevRighe10);
  end if;
 END;
END;
