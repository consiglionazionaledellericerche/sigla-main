--------------------------------------------------------
--  DDL for Package Body CNRCTB680
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB680" AS

 function getDescCori(aCori stipendi_cofi_cori%rowtype) return varchar2 is
 begin
  return ' mese:'||aCori.mese||' es:'||aCori.esercizio||' CORI:'||aCori.cd_contributo_ritenuta;
 end;

 function getInizioCompetenza(aStip stipendi_cofi%rowtype) return varchar2 is
 begin
  If aStip.mese = 13 Then
    return to_date(aStip.esercizio||'1201','YYYYMMDD');
  Elsif aStip.mese = 15 Then
    return Sysdate;
  Else
    return to_date(aStip.esercizio||lpad(aStip.mese,2,'0')||'01','YYYYMMDD');
  End If;
 end;

 function getFineCompetenza(aStip stipendi_cofi%rowtype) return varchar2 is
 begin
  If aStip.mese in (12,13) then
    return to_date(aStip.esercizio||'1231','YYYYMMDD');
  Elsif aStip.mese in (15) then
    return Sysdate;
  Else
    Return to_date(aStip.esercizio||lpad(aStip.mese+1,2,'0')||'01','YYYYMMDD')-1;
  End If;
 end;

-- =============================================================================
-- Main procedura
-- =============================================================================

 function gestioneCori(
  aStip stipendi_cofi%rowtype,
  aComp compenso%rowtype,
  aCori stipendi_cofi_cori%rowtype,
  aUOPersonale unita_organizzativa%rowtype,
  aTerzoVersamento terzo%rowtype,
  aManP in mandato%rowtype,
  aRev in out reversale%rowtype,
  aUser varchar2,
  aTSNow date
 ) return contributo_ritenuta%rowtype is
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aRevRighe CNRCTB038.righeReversaleList;
  aMan mandato%rowtype;
  aManRighe CNRCTB038.righeMandatoList;
  aRevRiga reversale_riga%rowtype;
  aManRiga mandato_riga%rowtype;
  aAssCoriFin ass_tipo_cori_ev%rowtype;
  aVoceF voce_f%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aListGenRighe CNRCTB100.docGenRigaList;
  aContoColl ass_partita_giro%rowtype;
  aCdTerzoUO number(8);
  aCdModPagUO varchar2(10);
  aPgBancaUO number(10);
  aCdModPagL varchar2(10);
  aPgBancaL number(10);
  isCoriSpeciale boolean;
  aClassCoriSpec CLASSIFICAZIONE_CORI.cd_classificazione_cori%type;
  aClassCori CLASSIFICAZIONE_CORI.cd_classificazione_cori%type;
  aCoriComp contributo_ritenuta%rowtype;
  aTipoCoriLoc tipo_contributo_ritenuta%rowtype;
  isCoriEntrata boolean;
  aDateCont date;
  parametriCnr parametri_cnr%rowtype;
  existRev boolean := false;
 begin
  aDateCont:=CNRCTB008.getTimestampContabile(aStip.esercizio,aTSNow);

  begin
   select * into aTipoCoriLoc from tipo_contributo_ritenuta where
       cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
   and dt_ini_validita <= trunc(aDateCont)
   and dt_fin_validita >= trunc(aDateCont);
  exception
    when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Cori specificato in interfaccia stipendi non trovato o attualmente non valido:'||aCori.cd_contributo_ritenuta);
    when TOO_MANY_ROWS then
     IBMERR001.RAISE_ERR_GENERICO('Configurazione CORI non consistente per tipo contributo ritenuta:'||aCori.cd_contributo_ritenuta);
  end;

  aClassCoriSpec:=CNRCTB015.GETVAL01PERCHIAVE(aStip.esercizio, CORI_SPECIALE, CORI_STIPENDI_EXTRA);

   -- Estrae la prima occorrenza di TIPO CORI presente in TIPO_CONTRIBUTO RITENUTA e registra la classificazione CORI di tale occorrenza
  begin
    select distinct cd_classificazione_cori into aClassCori from tipo_contributo_ritenuta where
             cd_contributo_ritenuta=aCori.cd_contributo_ritenuta
     and rownum=1;
  exception
    when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Contributo ritenuta non trovato in tabella CORI:'||aCori.cd_contributo_ritenuta);
  end;

  isCoriSpeciale:=false;
  if aClassCori = aClassCoriSpec then
    isCoriSpeciale:=true;
  end if;

  -- Estrae il terzo associato all'UO di stipendi_cofi e le sue modalità di pagamento di tipo bancario più recenti
  CNRCTB080.getTerzoPerUO(aUOPersonale.cd_unita_organizzativa, aCdTerzoUO, aCdModPagUO, aPgBancaUO,aCori.esercizio);
  if(parametriCnr.fl_nuovo_pdg ='N') then
  begin
   select * into aAssCoriFin from ass_tipo_cori_ev where
        cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
   and esercizio = aCori.esercizio
    and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
 and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
 and ti_ente_percepiente = aCori.ti_ente_percipiente; -- I CORI degli stipendi sono sempre di tipo ente
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato al contributo '||getDescCori(aCori)||' non trovato');
  end;
else
begin
   select * into aAssCoriFin from ass_tipo_cori_ev where
        cd_contributo_ritenuta = aCori.cd_contributo_ritenuta
   and esercizio = aCori.esercizio
    and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
 and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
 and ti_ente_percepiente = aCori.ti_ente_percipiente; -- I CORI degli stipendi sono sempre di tipo ente
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato al contributo '||getDescCori(aCori)||' non trovato');
  end;
end if;
  if aCori.ammontare = 0 then
   IBMERR001.RAISE_ERR_GENERICO('Cori specificato in interfaccia stipendi con importo nullo non supportati in liquidazione mensile stipendi');
  end if;
 if(parametriCnr.fl_nuovo_pdg ='N') then
  begin
   select * into aVoceF from voce_f where
         esercizio = aStip.esercizio
     and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
     and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
     and cd_titolo_capitolo = aAssCoriFin.cd_elemento_voce
     and ti_voce = CNRCTB001.CAPITOLO;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato al contributo '||getDescCori(aCori)||' non trovato');
  end;
end if;
  -- Per ogni CORI stabilisce l'effetto COFI e determina la partita di giro
  if aCori.ammontare >0 then
   isCoriEntrata:=true;
   aAcc:=null;
   aAccScad:=null;
   aAcc.CD_CDS:=aUOPersonale.cd_unita_padre;
   aAcc.ESERCIZIO:=aStip.esercizio;
   aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO;
   aAcc.CD_UNITA_ORGANIZZATIVA:=aUOPersonale.cd_unita_organizzativa;
   aAcc.CD_CDS_ORIGINE:=aUOPersonale.cd_unita_padre;
   aAcc.CD_UO_ORIGINE:=aUOPersonale.cd_unita_organizzativa;
   aAcc.TI_APPARTENENZA:=aAssCoriFin.ti_appartenenza;
   aAcc.TI_GESTIONE:=aAssCoriFin.ti_gestione;
   aAcc.CD_ELEMENTO_VOCE:=aAssCoriFin.cd_elemento_voce;
   if(parametriCnr.fl_nuovo_pdg ='N') then
      aAcc.CD_VOCE:=aVoceF.cd_voce;
   else
      aAcc.CD_VOCE:=aAssCoriFin.cd_elemento_voce;
   end if;
   aAcc.DT_REGISTRAZIONE:=TRUNC(aDateCont);
   aAcc.DS_ACCERTAMENTO:='CORI-D '||getDescCori(aCori);
   aAcc.NOTE_ACCERTAMENTO:='';
   aAcc.CD_TERZO:=aTerzoVersamento.cd_terzo;
   aAcc.IM_ACCERTAMENTO:=aCori.ammontare;
   aAcc.FL_PGIRO:='Y';
   aAcc.RIPORTATO:='N';
   aAcc.DACR:=aTSNow;
   aAcc.UTCR:=aUser;
   aAcc.DUVA:=aTSNow;
   aAcc.UTUV:=aUser;
   aAcc.PG_VER_REC:=1;
   aAcc.ESERCIZIO_COMPETENZA:=aStip.esercizio;

   if isCoriSpeciale then
    CNRCTB040.CREAACCERTAMENTOPGIROTRONC(false,aAcc,aAccScad,aObb,aObbScad,trunc(aTSNow));
   else
    CNRCTB040.CREAACCERTAMENTOPGIRO(false,aAcc,aAccScad,aObb,aObbScad,trunc(aTSNow));
   end if;
  else
    isCoriEntrata:=false;
   begin
   select * into aContoColl from ass_partita_giro where
            esercizio = aStip.esercizio
        and ti_appartenenza = aAssCoriFin.ti_appartenenza
        and ti_gestione = aAssCoriFin.ti_gestione
        and cd_voce = aAssCoriFin.cd_elemento_voce;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Conto associato in partita di giro non trovato per voce di entrata:'||aAssCoriFin.cd_elemento_voce);
   end;
   aObb:=null;
   aObbScad:=null;
   aObb.CD_CDS:=aUOPersonale.cd_unita_padre;
   aObb.ESERCIZIO:=aStip.esercizio;
   aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
   aObb.CD_UNITA_ORGANIZZATIVA:=aUOPersonale.cd_unita_organizzativa;
   aObb.CD_CDS_ORIGINE:=aUOPersonale.cd_unita_padre;
   aObb.CD_UO_ORIGINE:=aUOPersonale.cd_unita_organizzativa;
   aObb.TI_APPARTENENZA:=aContoColl.ti_appartenenza_clg;
   aObb.TI_GESTIONE:=aContoColl.ti_gestione_clg;
   aObb.CD_ELEMENTO_VOCE:=aContoColl.cd_voce_clg;
   aObb.DT_REGISTRAZIONE:=TRUNC(aDateCont);
   aObb.DS_OBBLIGAZIONE:='CORI-D '||getDescCori(aCori);
   aObb.NOTE_OBBLIGAZIONE:='';
   aObb.CD_TERZO:=aTerzoVersamento.cd_terzo;
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
   aObb.ESERCIZIO_COMPETENZA:=aStip.esercizio;
   if isCoriSpeciale then
    CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObb,aObbScad,aAcc,aAccScad,trunc(aTSNow));
   else
    CNRCTB030.CREAOBBLIGAZIONEPGIRO(false,aObb,aObbScad,aAcc,aAccScad,trunc(aTSNow));
   end if;
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
  aGen.DT_DA_COMPETENZA_COGE:=getInizioCompetenza(aStip);
  aGen.DT_A_COMPETENZA_COGE:=getFineCompetenza(aStip);
  aGen.DS_DOCUMENTO_GENERICO:='CORI - '||getDescCori(aCori);
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
  aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoUO;
  aGenRiga.CD_TERZO:=aAcc.CD_TERZO;
  aGenRiga.RAGIONE_SOCIALE:=null;
  aGenRiga.NOME:=null;
  aGenRiga.COGNOME:=null;
  aGenRiga.CODICE_FISCALE:=null;
  aGenRiga.PARTITA_IVA:=null;
--   aGenRiga.CD_TERMINI_PAG:=aStip.CD_TERMINI_PAG;
--   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aStip.CD_TERMINI_PAG_UO_CDS;
--   aGenRiga.NOTE:=aGen.NOTE;
  aGenRiga.DT_DA_COMPETENZA_COGE:=getInizioCompetenza(aStip);
  aGenRiga.DT_A_COMPETENZA_COGE:=getFineCompetenza(aStip);
  aGenRiga.STATO_COFI:=aGen.STATO_COFI;

  if isCoriEntrata then
   aGenRiga.CD_TERZO:=aTerzoVersamento.cd_terzo;
   aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoUO;
   aGenRiga.PG_BANCA_UO_CDS:=aPgBancaUO;
   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagUO;

   aGenRiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
   aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
   aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
   aGenRiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
   aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
  else
   aGenRiga.CD_TERZO:=aTerzoVersamento.cd_terzo;

   CNRCTB080.GETMODPAGULTIME(aTerzoVersamento.cd_terzo,aCdModPagL,aPgBancaL);

   aGenRiga.CD_MODALITA_PAG:=aCdModPagL;
   aGenRiga.PG_BANCA:=aPgBancaL;

   aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.CD_CDS;
   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.ESERCIZIO;
   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.ESERCIZIO_ORIGINALE;
   aGenRiga.PG_OBBLIGAZIONE:=aObb.PG_OBBLIGAZIONE;
   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aObbScad.PG_OBBLIGAZIONE_SCADENZARIO;
  end if;

 --   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
  aGenRiga.DACR:=aGen.DACR;
  aGenRiga.UTCR:=aGen.UTCR;
  aGenRiga.UTUV:=aGen.UTUV;
  aGenRiga.DUVA:=aGen.DUVA;
  aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
  aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
  aListGenRighe(1):=aGenRiga;

  CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

  -- AGGIUNGO ALLA COLLEZIONE DELLE REVERSALI LA REVERSALE CORI CON I COLLEGAMENTI AL GENERICO IMPOSTATI

  if isCoriEntrata then
   If aRev.pg_reversale is not null Then
     existRev := true;
     aRev.IM_REVERSALE:=aRev.IM_REVERSALE+aAcc.im_accertamento;
     aRev.DS_REVERSALE:=substr(aRev.DS_REVERSALE||','||aCori.cd_contributo_ritenuta,1,300);
   Else
     existRev := false;
     aRev:=null;
     aRev.CD_CDS:=aAcc.cd_cds;
     aRev.ESERCIZIO:=aAcc.esercizio;
     aRev.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
     aRev.CD_CDS_ORIGINE:=aAcc.cd_cds;
     aRev.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
     aRev.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV;
     aRev.TI_REVERSALE:=CNRCTB038.TI_REV_INC;
     aRev.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
     aRev.DS_REVERSALE:='CORI-D stipendi '||getDescCori(aCori);
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
   End If;
   
   aRevRiga:=null;
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
   aRevRiga.IM_REVERSALE_RIGA:=aAcc.im_accertamento;
   aRevRiga.FL_PGIRO:='Y';
   aRevRiga.UTCR:=aUser;
   aRevRiga.DACR:=aTSNow;
   aRevRiga.UTUV:=aUser;
   aRevRiga.DUVA:=aTSNow;
   aRevRiga.PG_VER_REC:=1;
   aRevRighe(1):=aRevRiga;
   -- Generazione reversale e collegamento a mandato principale
   CNRCTB037.GENERAECOLLEGADOC(
    aManP,
    aRev,
    aRevRighe,
    true
   );
   if not existRev Then
     -- Aggiorno la tabella ASS_COMP_DOC_CONT_NMP visto che il compenso non ha mandato principale
     CNRCTB560.addAssCompDocNMP(aComp,aRev,aRevRighe);
   end if;
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
   aMan.DS_MANDATO:='CORI-D stipendi '||getDescCori(aCori);
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
   -- Generazione mandato e collegamento a mandato principale
   CNRCTB037.GENERAECOLLEGADOC(
    aManP,
    aMan,
    aManRighe
   );
   -- Aggiorno la tabella ASS_COMP_DOC_CONT_NMP visto che il compenso non ha mandato principale
   CNRCTB560.addAssCompDocNMP(aComp,aMan,aManRighe);
  end if;

  aCoriComp.CD_CDS:=aUOPersonale.cd_unita_padre;
  aCoriComp.CD_UNITA_ORGANIZZATIVA:=aUOPersonale.cd_unita_organizzativa;
  aCoriComp.ESERCIZIO:=aCori.ESERCIZIO;
  aCoriComp.PG_COMPENSO:=aComp.pg_compenso;
  aCoriComp.CD_CONTRIBUTO_RITENUTA:=aTipoCoriLoc.CD_CONTRIBUTO_RITENUTA;
  aCoriComp.TI_ENTE_PERCIPIENTE:=aCori.TI_ENTE_PERCIPIENTE;
  aCoriComp.DT_INI_VALIDITA:=aTipoCoriLoc.DT_INI_VALIDITA;
  aCoriComp.MONTANTE:=0;
  aCoriComp.IMPONIBILE:=0;
  aCoriComp.ALIQUOTA:=0;
  aCoriComp.BASE_CALCOLO:=0;
  aCoriComp.AMMONTARE:=aCori.AMMONTARE;
  aCoriComp.STATO_COFI_CR:=CNRCTB100.STATO_COM_COFI_TOT_MR;
  if isCoriEntrata then
   aCoriComp.CD_CDS_ACCERTAMENTO:=aAccScad.cd_cds;
   aCoriComp.ESERCIZIO_ACCERTAMENTO:=aAccScad.esercizio;
   aCoriComp.ESERCIZIO_ORI_ACCERTAMENTO:=aAccScad.esercizio_originale;
   aCoriComp.PG_ACCERTAMENTO:=aAccScad.pg_accertamento;
   aCoriComp.PG_ACCERTAMENTO_SCADENZARIO:=aAccScad.pg_accertamento_scadenzario;
  else
   aCoriComp.CD_CDS_OBBLIGAZIONE:=aObbScad.cd_cds;
   aCoriComp.ESERCIZIO_OBBLIGAZIONE:=aObbScad.esercizio;
   aCoriComp.ESERCIZIO_ORI_OBBLIGAZIONE:=aObbScad.esercizio_originale;
   aCoriComp.PG_OBBLIGAZIONE:=aObbScad.pg_obbligazione;
   aCoriComp.PG_OBBLIGAZIONE_SCADENZARIO:=aObbScad.pg_obbligazione_scadenzario;
  end if;
  aCoriComp.DACR := aTSNow;
  aCoriComp.UTCR := aUser;
  aCoriComp.DUVA := aTSNow;
  aCoriComp.UTUV := aUser;
  aCoriComp.PG_VER_REC:=1;
  aCoriComp.AMMONTARE_LORDO:=aCori.AMMONTARE;
  aCoriComp.IMPONIBILE_LORDO:=0;
  aCoriComp.IM_DEDUZIONE_IRPEF:=0;
  aCoriComp.IM_DEDUZIONE_FAMILY:=0;
  return aCoriComp;
 end;

 procedure creaCompensoStip(
  aStip stipendi_cofi%rowtype,
  aTerzoVersamento terzo%rowtype,
  aManP mandato%rowtype,
  aUser varchar2,
  aTSNow date
 ) is
  aTotCoriEnte number(15,2);
  aTotCoriPercipiente number(15,2);
  aCori contributo_ritenuta%rowtype;
  aUOPersonale unita_organizzativa%rowtype;
  aComp compenso%rowtype;
  aTipoTrattamento tipo_trattamento%rowtype;
  aCdTipoTrattamento varchar2(10);
  isFoundTT boolean;
  aTerzoDivComp terzo%rowtype;
  aAnagTerzoDivComp anagrafico%rowtype;
  aCdModPagDivC varchar2(10);
  aPgBancaDivC number(10);
  isFoundRapp boolean;
  aRapporto rapporto%rowtype;
  aDateCont date;
 begin

  aDateCont:=CNRCTB008.getTimestampContabile(aStip.esercizio,aTSNow);

  aUOPersonale:=CNRCTB020.GETUOPERSONALE(aStip.esercizio);
  aUOPersonale:=CNRCTB020.GETUOVALIDA(aStip.esercizio,aUOPersonale.cd_unita_organizzativa);

  aTerzoDivComp:=CNRCTB080.getTerzo(CNRCTB015.GETIM01PERCHIAVE(TERZO_SPECIALE,DIVERSI_STIPENDI));
  aAnagTerzoDivComp:=CNRCTB080.getAnag(aTerzoDivComp.cd_terzo);

  CNRCTB080.GETMODPAGULTIME(aTerzoDivComp.cd_terzo,aCdModPagDivC,aPgBancaDivC);

  isFoundRapp:=false;
  for aTmpRapporto in (select * from rapporto where
          cd_anag = aAnagTerzoDivComp.cd_anag
    and cd_tipo_rapporto = TIPO_RAPPORTO_STIPENDI
  ) loop
   isFoundRapp:=true;
   aRapporto:=aTmpRapporto;
   exit;
  end loop;
  if not isFoundRapp then
   IBMERR001.RAISE_ERR_GENERICO('Rapporto non trovato per anagrafica diversi stipendi');
  end if;

  -- Cerca il tipo trattamento speciale per compenso stipendi che deve essere associato al tipo di rapporto STI

  isFoundTT:=false;
  aCdTipoTrattamento:=CNRCTB015.GETVAL01PERCHIAVE(TRATTAMENTO_SPECIALE,TRATTAMENTO_STIPENDI);
  for aTmpTipoTrattamento in (select * from tipo_trattamento a where
          cd_trattamento = aCdTipoTrattamento
    and exists (select 1 from ass_ti_rapp_ti_tratt where
                                                  cd_tipo_rapporto = TIPO_RAPPORTO_STIPENDI
                        and cd_trattamento = a.cd_trattamento
               )
  ) loop
   isFoundTT:=true;
   aTipoTrattamento:=aTmpTipoTrattamento;
   exit;
  end loop;
  if not isFoundTT then
   IBMERR001.RAISE_ERR_GENERICO('Tipo trattamento speciale per stipendi non trovato');
  end if;
  aComp.CD_CDS:=aStip.cd_cds_doc_gen;
  aComp.CD_UNITA_ORGANIZZATIVA:=aStip.cd_uo_doc_gen;
  aComp.ESERCIZIO:=aStip.esercizio;
  aComp.PG_COMPENSO:= CNRCTB100.getNextNum(aComp.CD_CDS,aComp.esercizio,aComp.CD_UNITA_ORGANIZZATIVA, CNRCTB100.TI_COMPENSO,aUser,aTSNow);
  aComp.CD_CDS_ORIGINE:=aComp.CD_CDS;
  aComp.CD_UO_ORIGINE:=aComp.CD_UNITA_ORGANIZZATIVA;
  aComp.DT_REGISTRAZIONE:=trunc(aDateCont);
  aComp.DS_COMPENSO:='Liquidazione stipendi mese n.'||aStip.mese||' es.:'||aStip.esercizio;
  aComp.TI_ANAGRAFICO:='D';
  aComp.CD_TERZO:=aTerzoDivComp.cd_terzo;
  aComp.CD_TERZO_UO_CDS:=null;
  aComp.RAGIONE_SOCIALE:=aAnagTerzoDivComp.ragione_sociale;
  aComp.NOME:=aAnagTerzoDivComp.nome;
  aComp.COGNOME:=aAnagTerzoDivComp.cognome;
  aComp.CODICE_FISCALE:=aAnagTerzoDivComp.codice_fiscale;
  aComp.PARTITA_IVA:=aAnagTerzoDivComp.partita_iva;
  aComp.CD_TERMINI_PAG:=null;
  aComp.CD_TERMINI_PAG_UO_CDS:=null;
  aComp.CD_MODALITA_PAG:=aCdModPagDivC;
  aComp.CD_MODALITA_PAG_UO_CDS:=null;
  aComp.PG_BANCA:=aPgBancaDivC;
  aComp.PG_BANCA_UO_CDS:=null;
  aComp.CD_TIPO_RAPPORTO:=TIPO_RAPPORTO_STIPENDI;
  aComp.CD_TRATTAMENTO:=aCdTipoTrattamento;
  aComp.FL_SENZA_CALCOLI:='Y';
  aComp.FL_DIARIA:='N';
  aComp.DT_CANCELLAZIONE:=null;
  aComp.STATO_COFI:=CNRCTB100.STATO_COM_COFI_TOT_MR;
  aComp.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
  aComp.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
  aComp.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV;
  aComp.DT_EMISSIONE_MANDATO:=trunc(aDateCont);
  aComp.DT_TRASMISSIONE_MANDATO:=null;
  aComp.DT_PAGAMENTO_MANDATO:=null;
  aComp.DT_DA_COMPETENZA_COGE:=getInizioCompetenza(aStip);
  aComp.DT_A_COMPETENZA_COGE:=getFineCompetenza(aStip);
  aComp.STATO_PAGAMENTO_FONDO_ECO:='N';
  aComp.DT_PAGAMENTO_FONDO_ECO:=null;
  aComp.IM_TOTALE_COMPENSO:=0;
  aComp.IM_LORDO_PERCIPIENTE:=0;
  aComp.IM_NETTO_PERCIPIENTE:=0;
  aComp.IM_CR_PERCIPIENTE:=0;
  aComp.IM_CR_ENTE:=0;
  aComp.QUOTA_ESENTE:=0;
  aComp.QUOTA_ESENTE_NO_IVA:=0;
  aComp.IM_NO_FISCALE:=0;
  aComp.IMPONIBILE_FISCALE:=0;
  aComp.CD_VOCE_IVA:=null;
  aComp.IMPONIBILE_IVA:=0;
  aComp.PG_COMUNE_ADD:=0;
  aComp.CD_PROVINCIA_ADD:=null;
  aComp.CD_REGIONE_ADD:=null;
  aComp.CD_REGIONE_IRAP:=null;
  aComp.CD_CDS_MISSIONE:=null;
  aComp.ESERCIZIO_MISSIONE:=null;
  aComp.PG_MISSIONE:=null;
  aComp.CD_UO_MISSIONE:=null;
  aComp.CD_CDS_OBBLIGAZIONE:=null;
  aComp.ESERCIZIO_ORI_OBBLIGAZIONE:=Null;
  aComp.PG_OBBLIGAZIONE:=null;
  aComp.PG_OBBLIGAZIONE_SCADENZARIO:=null;
  aComp.CD_CDS_ACCERTAMENTO:=null;
  aComp.ESERCIZIO_ACCERTAMENTO:=null;
  aComp.ESERCIZIO_ORI_ACCERTAMENTO:=Null;
  aComp.PG_ACCERTAMENTO:=null;
  aComp.PG_ACCERTAMENTO_SCADENZARIO:=null;
  aComp.DACR:=aTSNow;
  aComp.UTCR:=aUser;
  aComp.DUVA:=aTSNow;
  aComp.UTUV:=aUser;
  aComp.PG_VER_REC:=1;
  aComp.CD_TIPOLOGIA_RISCHIO:=null;
  aComp.DETRAZIONI_PERSONALI:=0;
  aComp.DETRAZIONI_LA:=0;
  aComp.DETRAZIONE_CONIUGE:=0;
  aComp.DETRAZIONE_FIGLI:=0;
  aComp.DETRAZIONE_ALTRI:=0;
  aComp.DETRAZIONE_RIDUZIONE_CUNEO:=0;
  aComp.DETRAZIONI_PERSONALI_NETTO:=0;
  aComp.DETRAZIONI_LA_NETTO:=0;
  aComp.DETRAZIONE_CONIUGE_NETTO:=0;
  aComp.DETRAZIONE_FIGLI_NETTO:=0;
  aComp.DETRAZIONE_ALTRI_NETTO:=0;
  aComp.DETRAZIONE_RID_CUNEO_NETTO:=0;
  aComp.CD_CDS_DOC_GENRC:=null;
  aComp.CD_UO_DOC_GENRC:=null;
  aComp.ESERCIZIO_DOC_GENRC:=null;
  aComp.CD_TIPO_DOC_GENRC:=null;
  aComp.PG_DOC_GENRC:=null;
  aComp.CD_CDR_GENRC:=null;
  aComp.CD_LINEA_ATTIVITA_GENRC:=null;
  aComp.TI_ISTITUZ_COMMERC:=TI_ISTITUZIONALE;
  aComp.IMPONIBILE_INAIL:=0;
  aComp.ESERCIZIO_FATTURA_FORNITORE:=null;
  aComp.DT_FATTURA_FORNITORE:=null;
  aComp.NR_FATTURA_FORNITORE:=null;
  aComp.FL_GENERATA_FATTURA:='N';
  aComp.FL_COMPENSO_STIPENDI:='Y';
  aComp.FL_COMPENSO_CONGUAGLIO:='N';
  aComp.FL_COMPENSO_MINICARRIERA:='N';
  aComp.ALIQUOTA_IRPEF_DA_MISSIONE:=0;
  aComp.FL_COMPENSO_MCARRIERA_TASSEP:='N';
  aComp.ALIQUOTA_IRPEF_TASSEP:=0;
  aComp.IM_DEDUZIONE_IRPEF:=0;
  aComp.IMPONIBILE_FISCALE_NETTO:=0;
  aComp.NUMERO_GIORNI:=0;
  aComp.FL_ESCLUDI_QVARIA_DEDUZIONE:='N';
  aComp.FL_INTERA_QFISSA_DEDUZIONE:='N';
  aComp.IM_DETRAZIONE_PERSONALE_ANAG:=0;
  aComp.FL_RECUPERO_RATE:='N';
  aComp.FL_ACCANTONA_ADD_TERR:='N';

  CNRCTB545.INSCOMPENSO(aComp);

  aTotCoriEnte:=0;
  aTotCoriPercipiente:=0;

  Declare
    aRevI reversale%rowtype;
  Begin
    For aCoriStip In (Select *
                      From  stipendi_cofi_cori
                      Where esercizio = aStip.esercizio And
                            mese = aStip.mese) Loop
      aCori := gestioneCori(aStip,aComp,aCoriStip,aUOPersonale,aTerzoVersamento,aManP,aRevI,aUser,aTSNow);

      if aCori.esercizio is not null then
        if aCori.ti_ente_percipiente = 'E' then
          aTotCoriEnte:=aTotCoriEnte + aCori.ammontare;
        else
          aTotCoriPercipiente:=aTotCoriPercipiente + aCori.ammontare;
        end if;

        CNRCTB545.insCONTRIBUTORITENUTA(aCori);
      end if;
    End loop;

    -- Aggiorno l'importo e la descrzione della reversale
    update REVERSALE
    Set IM_REVERSALE = aRevI.IM_REVERSALE,
        DS_REVERSALE = substr(aRevI.DS_REVERSALE,1,300)
    Where esercizio = aRevI.esercizio And
          cd_cds = aRevI.cd_cds And
          pg_reversale = aRevI.pg_reversale;

    CNRCTB037.updScadAccertamento(aRevI);
    CNRCTB037.updSaldoCapitoliR(aRevI,'I','A');

    CNRCTB300.leggiMandatoReversale(aRevI.CD_CDS, aRevI.ESERCIZIO, aRevI.PG_REVERSALE, 'REV', 'I', aRevI.utuv);

    -- Aggiorno l'importo ritenute del mandato principale
    update mandato
    Set im_ritenute = im_ritenute + aRevI.IM_REVERSALE
    Where esercizio = aManP.esercizio And
          cd_cds = aManP.cd_cds And
          pg_mandato = aManp.pg_mandato;
  End;

  Update compenso
  Set IM_CR_PERCIPIENTE    = aTotCoriPercipiente,
      IM_CR_ENTE           = aTotCoriEnte,
      IM_NETTO_PERCIPIENTE = aManP.im_mandato - aTotCoriEnte - aTotCoriPercipiente, -- Calcolo il netto percipiente come differenza tra importo del mandato e quello dei cori
      IM_TOTALE_COMPENSO   = aManP.im_mandato,
      IM_LORDO_PERCIPIENTE = aManP.im_mandato - aTotCoriEnte
  Where esercizio=aComp.esercizio And
        cd_cds=aComp.cd_cds And
        cd_unita_organizzativa=aComp.cd_unita_organizzativa And
        pg_compenso=aComp.pg_compenso;

  Update stipendi_cofi
  Set    pg_comp = aComp.pg_compenso,
         cd_cds_comp = aComp.cd_cds,
         cd_uo_comp = aComp.cd_unita_organizzativa,
         esercizio_comp = aComp.esercizio
  Where  mese = aStip.mese And
         esercizio = aStip.esercizio;

 End;

 PROCEDURE contabilFlussoStipCOFI
      (aEs number,
       aMese number,
       aUser varchar2) Is

  aStip stipendi_cofi%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aDettScadList CNRCTB035.scadVoceListE;
  aAccScadVoce accertamento_Scad_voce%rowtype;
  aEvEntrata elemento_voce%rowtype;
  aTSNow date;
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aManP mandato%rowtype;
  aManPRiga mandato_riga%rowtype;
  aListRigheManP CNRCTB038.righeMandatoList;
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aListGenRighe CNRCTB100.docGenRigaList;
  aRev reversale%rowtype;
  aRevRighe CNRCTB038.righeReversaleList;
  aCdModPag VARCHAR2(10);
  aPgBanca NUMBER(10);
  aIndex number(10);
  aUOPersonale unita_organizzativa%rowtype;
  aTerzoVersamento terzo%rowtype;
  aTotRigheMandato number(15,2);
  aComp compenso%rowtype;
  aDateCont date;
 BEGIN
  aTSNow:=sysdate;
  --Il flusso per il mese 15 può essere contabilizzato in qualunque momento
  If aMese != 15 Then
        if aMese < 1 or aMese > 13 then
         IBMERR001.RAISE_ERR_GENERICO('Il mese deve essere compreso tra 1 (Gennaio) e 13 (Mese Tredicesima)');
        end if;

        -- se il mese è > 1 verifico che la liquidazione del mese precedente sia stata fatta
        if aMese > 1 then
           Begin
               Select * into aStip
               From   stipendi_cofi
               Where  mese = aMese-1
                 And  esercizio = aEs
           And stato = CNRCTB100.STATO_COM_COFI_TOT_MR
               For update nowait;
           Exception when NO_DATA_FOUND then
            IBMERR001.RAISE_ERR_GENERICO('Dati stipendiali non trovati o contabilizzazione non ancora effettuata per mese precedente al corrente');
           End;
        End if;
  End If;    -- fine If aMese != 15
  Begin
      Select *
      Into   aStip
      From   stipendi_cofi
      Where  mese = aMese And
      esercizio = aEs And
      stato = CNRCTB100.STATO_COM_COFI_INI
      For Update nowait;
  Exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Dati stipendiali non trovati o contabilizzazione già effettuata per mese n.'||aMese||' es:'||aEs);
  End;


  aDateCont:=CNRCTB008.getTimestampContabile(aEs,aTSNow);

  aUOPersonale:=CNRCTB020.GETUOPERSONALE(aStip.esercizio);
  aUOPersonale:=CNRCTB020.GETUOVALIDA(aStip.esercizio,aUOPersonale.cd_unita_organizzativa);

  aGen:=null;
  aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_VER_STIPENDI;
  aGen.CD_CDS:=aUOPersonale.cd_unita_padre;
  aGen.CD_UNITA_ORGANIZZATIVA:=aUOPersonale.cd_unita_organizzativa;
  aGen.ESERCIZIO:=aStip.esercizio;
  aGen.CD_CDS_ORIGINE:=aUOPersonale.cd_unita_padre;
  aGen.CD_UO_ORIGINE:=aUOPersonale.cd_unita_organizzativa;
  aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
  aGen.DT_DA_COMPETENZA_COGE:=getInizioCompetenza(aStip);
  aGen.DT_A_COMPETENZA_COGE:=getFineCompetenza(aStip);
  aGen.DS_DOCUMENTO_GENERICO:='Generico di versamento stipendi mese:'||aStip.mese;
  aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
  aGen.IM_TOTALE:=0;
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

  aTotRigheMandato:=0;
  aIndex:=0;

  for aStipObb in (Select * from stipendi_cofi_obb_scad
                   Where esercizio = aEs And
                         mese = aMese
                   For update nowait) Loop
   aIndex:=aIndex+1;
   aGenRiga:=null;

   Begin
       select * into aObb from obbligazione where
             cd_cds = aStipObb.cd_cds_obbligazione
      and esercizio = aStipObb.esercizio
      and esercizio_originale = aStipObb.esercizio_ori_obbligazione
    and pg_obbligazione = aStipObb.pg_obbligazione
       for update nowait;
   Exception
      When No_Data_Found Then
      If cnrutil.isLabelObbligazione() Then
        IBMERR001.RAISE_ERR_GENERICO('Obbligazione n. '||aStipObb.pg_obbligazione||' associata a liquidazione stipendi mese n.'||aStip.mese||' es. '||aStip.esercizio);
      Else
        IBMERR001.RAISE_ERR_GENERICO('Impegno n. '||aStipObb.pg_obbligazione||' associato a liquidazione stipendi mese n.'||aStip.mese||' es. '||aStip.esercizio);
      End If;
   End;

   If aIndex=1 then
    Select * into aTerzoVersamento
    From  terzo
    Where cd_terzo = aObb.cd_terzo;
   End If;

   --CERCO LA SCADENZA CON PROGRESSIVO MAGGIORE E, SE NON UGUALE AL MESE PREVISTO, LA SPOSTO FINO
   --AD ARRIVARE A QUELLO CHE MI ASPETTO
   Declare
       maxPgObbScad obbligazione_scadenzario.pg_obbligazione_scadenzario%type;
   Begin
       Select max(pg_obbligazione_scadenzario) into maxPgObbScad
       From  obbligazione_scadenzario
       Where cd_cds = aStipObb.cd_cds_obbligazione And
             esercizio = aStipObb.esercizio And
             esercizio_originale = aStipObb.esercizio_ori_obbligazione And
             pg_obbligazione = aStipObb.pg_obbligazione  and
             im_scadenza!=0 and im_associato_doc_amm =0;

       If (maxPgObbScad > aMese) Then
          IBMERR001.RAISE_ERR_GENERICO('Esiste un progressivo scadenza ('||maxPgObbScad||') di obbligazione n. '||aStipObb.pg_obbligazione||' superiore rispetto al mese del pagamento ('||aMese||')');
       Elsif (maxPgObbScad < aMese) Then
          For currMese in maxPgObbScad..aMese-1 Loop
            Declare
              currObbScad obbligazione_scadenzario%rowtype;
            Begin
              Select * into currObbScad
              From  obbligazione_scadenzario
              Where cd_cds = aStipObb.cd_cds_obbligazione And
                     esercizio = aStipObb.esercizio And
                     esercizio_originale = aStipObb.esercizio_ori_obbligazione And
                     pg_obbligazione = aStipObb.pg_obbligazione And
                     pg_obbligazione_scadenzario = currMese;

              If (aMese=15) Then
                If (currObbScad.im_scadenza<aStipObb.im_totale) Then
                  IBMERR001.RAISE_ERR_GENERICO('Disponibilità residua dell''obbligazione n. '||aStipObb.pg_obbligazione||' non sufficiente per il pagamento del mese n. '||aMese||' es. '||aEs);
                End If;
                CNRCTB030.adeguaObbScadSuccSV(currObbScad,aMese,currObbScad.im_scadenza-aStipObb.im_totale,aGen.UTCR);
                exit;
              Else
                CNRCTB030.adeguaObbScadSuccSV(currObbScad,currMese+1,0,aGen.UTCR);
              End If;
            End;
          End Loop;
       End if;
   End;

   --Riprendo il giro in quanto a questo punto, se necessario, ho spostato le somme sul mese di riferimento
   Begin
       Select * into aObbScad
       From  obbligazione_scadenzario
       Where cd_cds = aStipObb.cd_cds_obbligazione And
             esercizio = aStipObb.esercizio And
             esercizio_originale = aStipObb.esercizio_ori_obbligazione And
             pg_obbligazione = aStipObb.pg_obbligazione And
             pg_obbligazione_scadenzario = aMese
       For Update nowait;
   Exception when NO_DATA_FOUND then
      If cnrutil.isLabelObbligazione() Then
        IBMERR001.RAISE_ERR_GENERICO('Scadenza di obbligazione n. '||aStipObb.pg_obbligazione||' associata a liquidazione stipendi mese n. '||aMese||' es. '||aEs);
      Else
        IBMERR001.RAISE_ERR_GENERICO('Scadenza di impegno n. '||aStipObb.pg_obbligazione||' associato a liquidazione stipendi mese n. '||aMese||' es. '||aEs);
      End If;
   End;

   If aObbScad.im_associato_doc_amm <> 0 or aObbScad.im_associato_doc_contabile <> 0 then
      If cnrutil.isLabelObbligazione() Then
        IBMERR001.RAISE_ERR_GENERICO('Scadenza di obbligazione n. '||aStipObb.pg_obbligazione||' associata a liquidazione stipendi mese n. '||aMese||' es. '||aEs||' già associata a documenti contabili o amministrativi');
      Else
        IBMERR001.RAISE_ERR_GENERICO('Scadenza di impegno n. '||aStipObb.pg_obbligazione||' associato a liquidazione stipendi mese n. '||aMese||' es. '||aEs||' già associata a documenti contabili o amministrativi');
      End If;
   End If;
   If aMese = 14 or aMese > 15 Then
        IBMERR001.RAISE_ERR_GENERICO('Mese '||aMese||' indicato per il pagamento non valido');
   End If;
   If aMese != 15 Then
       -- Anche per il mese di dicembre deve essere aggiornata la scadenza successiva
       --if aMese = 13 then
       -- CNRCTB030.adeguaObbSV(aObbScad,aStipObb.im_totale,aGen.UTCR);
       --else
        CNRCTB030.adeguaObbScadSuccSV(aObbScad,aObbScad.pg_obbligazione_scadenzario+1,aStipObb.im_totale,aGen.UTCR);
       --end if;
   End If;
   If aMese = 15 And aObbScad.im_scadenza != aStipObb.im_totale Then
        IBMERR001.RAISE_ERR_GENERICO('L''importo della scadenza di obbligazione n. '||aStipObb.pg_obbligazione||' associata a liquidazione stipendi mese n. '||aMese||' es. '||aEs||' ('||To_Char(aObbScad.im_scadenza)||') e'' diverso dall''importo '||
' presente sul flusso di spesa ('||To_Char(aStipObb.im_totale, '999G999G999G999G990D00')||')');
   End If;

   CNRCTB080.GETMODPAGULTIME(aObb.cd_terzo,aCdModPag,aPgBanca);

   aGenRiga.CD_CDS:=aGen.cd_cds;
   aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
   aGenRiga.ESERCIZIO:=aGen.esercizio;
   aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
   aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
-- Massimo Iaccarino Inizio
   aGenRiga.IM_RIGA_DIVISA:=aStipObb.im_totale;
   aGenRiga.IM_RIGA:=aStipObb.im_totale;
--   aGenRiga.CD_TERZO_CESSIONARIO:=aGen.CD_TERZO_CESSIONARIO;
--   aGenRiga.CD_TERZO_UO_CDS:=null;
   aGenRiga.CD_TERZO:=aObb.CD_TERZO;
   aGenRiga.RAGIONE_SOCIALE:=null;
   aGenRiga.NOME:=null;
   aGenRiga.COGNOME:=null;
   aGenRiga.CODICE_FISCALE:=null;
   aGenRiga.PARTITA_IVA:=null;
--   aGenRiga.CD_TERMINI_PAG:=aStip.CD_TERMINI_PAG;
--   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aStip.CD_TERMINI_PAG_UO_CDS;
--   aGenRiga.NOTE:=aGen.NOTE;
   aGenRiga.DT_DA_COMPETENZA_COGE:=getInizioCompetenza(aStip);
   aGenRiga.DT_A_COMPETENZA_COGE:=getFineCompetenza(aStip);
   aGenRiga.STATO_COFI:=aGen.STATO_COFI;
--   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
   aGenRiga.CD_CDS_OBBLIGAZIONE:=aObbScad.CD_CDS;
   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObbScad.ESERCIZIO;
   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObbScad.ESERCIZIO_ORIGINALE;
   aGenRiga.PG_OBBLIGAZIONE:=aObbScad.PG_OBBLIGAZIONE;
   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aObbScad.PG_OBBLIGAZIONE_SCADENZARIO;
   aGenRiga.PG_BANCA:=aPgBanca;
   aGenRiga.CD_MODALITA_PAG:=aCdModPag;
   aGenRiga.DACR:=aGen.DACR;
   aGenRiga.UTCR:=aGen.UTCR;
   aGenRiga.UTUV:=aGen.UTUV;
   aGenRiga.DUVA:=aGen.DUVA;
   aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
   aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
   aListGenRighe(aIndex):=aGenRiga;

   aManPRiga.CD_CDS:=aGenRiga.cd_cds;
   aManPRiga.ESERCIZIO:=aGenRiga.esercizio;
   aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio;
   aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
   aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
   aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
   aManPRiga.CD_CDS_DOC_AMM:=aGenRiga.cd_cds;
   aManPRiga.CD_UO_DOC_AMM:=aGenRiga.cd_unita_organizzativa;
   aManPRiga.ESERCIZIO_DOC_AMM:=aGenRiga.esercizio;
   aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGenRiga.cd_tipo_documento_amm;
   aManPRiga.PG_DOC_AMM:=aGenRiga.pg_documento_generico;
   aManPRiga.DS_MANDATO_RIGA:='Riga liquidazione stipendi voce del piano:'||aObb.cd_elemento_voce;
   aManPRiga.STATO:=CNRCTB038.STATO_MAN_EME;
   aManPRiga.CD_TERZO:=aGenRiga.cd_terzo;
   aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
   aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
   aManPRiga.IM_MANDATO_RIGA:=aGenRiga.im_riga;
   aManPRiga.FL_PGIRO:='N';
   aManPRiga.im_ritenute_riga:=0;
   aManPRiga.UTCR:=aUser;
   aManPRiga.DACR:=aTSNow;
   aManPRiga.UTUV:=aUser;
   aManPRiga.DUVA:=aTSNow;
   aManPRiga.PG_VER_REC:=1;
   aTotRigheMandato := aTotRigheMandato + aManPRiga.IM_MANDATO_RIGA;
   aListRigheManP(aIndex) := aManPRiga;
  end loop;

  If aIndex = 0 then
   If cnrutil.isLabelObbligazione() Then
        IBMERR001.RAISE_ERR_GENERICO('Nessuna obbligazione trovata per liquidazione stipendi mese: '||aStip.mese||' es: '||aStip.esercizio);
   Else
        IBMERR001.RAISE_ERR_GENERICO('Nessun impegno trovato per liquidazione stipendi mese: '||aStip.mese||' es: '||aStip.esercizio);
   End If;
  End If;

  CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

  for aRI in 1..aListRigheManP.count loop
   aListRigheManP(aRI).pg_doc_amm:=aGen.pg_documento_generico;
  end loop;

  aManP.CD_CDS:=aManPRiga.cd_cds;
  aManP.ESERCIZIO:=aManPRiga.esercizio;
  aManP.CD_UNITA_ORGANIZZATIVA:=aUOPersonale.cd_unita_organizzativa;
  aManP.CD_CDS_ORIGINE:=aManPRiga.cd_cds;
  aManP.CD_UO_ORIGINE:=aUOPersonale.cd_unita_organizzativa;
  aManP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
  aManP.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
  aManP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
  aManP.DS_MANDATO:='Mandato di liquidazione stipendi_cofi mese:'||aStip.mese;
  aManP.STATO:=CNRCTB038.STATO_MAN_EME;
  aManP.DT_EMISSIONE:=TRUNC(aDateCont);
--  aManP.DT_TRASMISSIONE:=;
--  aManP.DT_PAGAMENTO:=;
--  aManP.DT_ANNULLAMENTO:=;
  aManP.IM_MANDATO:=aTotRigheMandato;
  aManP.IM_RITENUTE:=0;
  aManP.IM_PAGATO:=0;
  aManP.UTCR:=aUser;
  aManP.DACR:=aTSNow;
  aManP.UTUV:=aUser;
  aManP.DUVA:=aTSNow;
  aManP.PG_VER_REC:=1;
  aManP.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;

  -- Aggiorno lo stato di liquidazione stipendi

  CNRCTB037.GENERADOCUMENTO(aManP, aListRigheManP);

  aStip.cd_cds_doc_gen:=aGen.cd_cds;
  aStip.cd_uo_doc_gen:=aGen.cd_unita_organizzativa;
  aStip.esercizio_doc_gen:=aGen.esercizio;
  aStip.cd_tipo_doc_gen:=aGen.cd_tipo_documento_amm;
  aStip.pg_doc_gen:=aGen.pg_documento_generico;

-- crea il compenso fittizio degli stipendi, crea CONTRIBUTO_RITENUTA e mette il compenso su STIPENDI_COFI

  creaCompensoStip(aStip, aTerzoVersamento, aManP, aUser, aTSnow);

  Update stipendi_cofi
  Set    stato             = CNRCTB100.STATO_COM_COFI_TOT_MR,
   cd_cds_mandato    = aManP.cd_cds,
   esercizio_mandato = aManP.esercizio,
   pg_mandato        = aManP.pg_mandato,
   cd_cds_doc_gen    = aGen.cd_cds,
   cd_uo_doc_gen     = aGen.cd_unita_organizzativa,
   esercizio_doc_gen = aGen.esercizio,
   cd_tipo_doc_gen   = aGen.cd_tipo_documento_amm,
   pg_doc_gen        = aGen.pg_documento_generico,
         duva              = aTSNow,
   utuv              = aUser,
   pg_ver_rec        = pg_ver_rec+1
  Where  mese = aStip.mese And
         esercizio = aStip.esercizio;

   -- 20.07.2007 PER IL MOMENTO PEZZA
   -- POICHE' LA PROCEDURA STANDARD EMETTE PRIMA MANDATI E REVERSALI E POI GENERA CONTRIBUTO_RITENUTA
   -- LA PROCEDURA INS_SIOPE_AUTOMATICO NON TROVA L'ACCERTAMENTO SU CONTRIBUTO_RIITENUTA
   -- MI DEVO FARE UN NUOVO LOOP DOPO

   For RIGHE_REV In (Select RR.*
                     From   STIPENDI_COFI SC, REVERSALE_RIGA RR, ASS_MANDATO_REVERSALE AMR
                     Where  SC.ESERCIZIO            = aStip.ESERCIZIO And
                            SC.MESE                 = aStip.MESE And
                            SC.CD_CDS_MANDATO       = amr.CD_CDS_MANDATO And
                            SC.ESERCIZIO_MANDATO    = amr.ESERCIZIO_MANDATO And
                            SC.PG_MANDATO           = amr.PG_MANDATO And
                            amr.CD_CDS_REVERSALE    = rr.CD_CDS And
                            amr.ESERCIZIO_REVERSALE = rr.ESERCIZIO And
                            amr.PG_REVERSALE        = rr.pg_REVERSALE) Loop

        --ELIMINO EVENTUALI PRECEDENTI CARICAMENTI DELLA TABELLA REVERSALE_SIOPE
        --PER RICARICARLA IN MANIERA PULITA
        DELETE REVERSALE_SIOPE
        WHERE CD_CDS = RIGHE_REV.CD_CDS
        AND   ESERCIZIO = RIGHE_REV.ESERCIZIO
        AND   PG_REVERSALE = RIGHE_REV.PG_REVERSALE
        AND   ESERCIZIO_ACCERTAMENTO = RIGHE_REV.ESERCIZIO_ACCERTAMENTO
        AND   ESERCIZIO_ORI_ACCERTAMENTO = RIGHE_REV.ESERCIZIO_ORI_ACCERTAMENTO
        AND   PG_ACCERTAMENTO = RIGHE_REV.PG_ACCERTAMENTO
        AND   PG_ACCERTAMENTO_SCADENZARIO = RIGHE_REV.PG_ACCERTAMENTO_SCADENZARIO
        AND   CD_CDS_DOC_AMM = RIGHE_REV.CD_CDS_DOC_AMM
        AND   CD_UO_DOC_AMM = RIGHE_REV.CD_UO_DOC_AMM
        AND   ESERCIZIO_DOC_AMM = RIGHE_REV.ESERCIZIO_DOC_AMM
        AND   CD_TIPO_DOCUMENTO_AMM = RIGHE_REV.CD_TIPO_DOCUMENTO_AMM
        AND   PG_DOC_AMM = RIGHE_REV.PG_DOC_AMM;

        Begin
          cnrctb037.inserisci_siope_automatico (RIGHE_REV);
        Exception
          When Others Then Null;
        End;

   End Loop;

  -- CNRCTB207.regStipendiCOGE(aEs, aMese, aUser);
 end;

 Procedure elaboraStipDett (aEs number,
                aMese number,
                aUser varchar2) Is

    aStipObb  STIPENDI_COFI_OBB_SCAD%Rowtype;
    aStipCori STIPENDI_COFI_CORI%Rowtype;

    aStipObbDett  STIPENDI_COFI_OBB_SCAD_DETT%Rowtype;
    aStipCoriDett STIPENDI_COFI_CORI_DETT%Rowtype;

    aLog STIPENDI_COFI_LOGS%Rowtype;

    totaleIrap    Number:=0;
    annulliIrap   Number:=0;
    sommaDettIrap   Number:=0;
    sommaDetAddCom  Number:=0;
    sommaSaldiNegAddCom NUMBER:=0;
    sommaSaldiPosAddCom NUMBER:=0;
    totaleSpesa   NUMBER:=0;
    totaleEntrata NUMBER:=0;
    differenza    NUMBER:=0;
    contaAddCom   Number:=0;
    ritAddCom   TIPO_CONTRIBUTO_RITENUTA.CD_CONTRIBUTO_RITENUTA%Type;
    esistonoDati  varchar2(1):='N';
    errore    varchar2(1):='N';
    meseCompetenza  number(2);
    totaleNegativi  NUMBER:=0;
    totSpesaContribFig NUMBER:=0;
    totEntrataContribFig NUMBER:=0;

    Cursor cObb (tipo VARCHAR2) Is
        Select *
        From STIPENDI_COFI_OBB_SCAD_DETT
        Where ESERCIZIO=aEs
          And MESE=aMese
          And TIPO_FLUSSO = tipo;
    Cursor cCori (tipo VARCHAR2) Is
        Select *
        From STIPENDI_COFI_CORI_DETT
        Where ESERCIZIO=aEs
          And MESE=aMese
          And TIPO_FLUSSO = tipo;
    Cursor cCoriElaborati Is
        Select *
        From STIPENDI_COFI_CORI
        Where ESERCIZIO=aEs
          And MESE=aMese;
  Begin
    gPgLog := ibmutl200.LOGSTART(tipoLog,'Elaborazione archivi stipendi', null, aUser, null, null);

    aLog.ESERCIZIO:=aEs;
    aLog.MESE:=aMese;
    aLog.PG_ESECUZIONE:=gPgLog;
    aLog.DACR:=Sysdate;
    aLog.UTCR:=aUser;
    aLog.DUVA:=Sysdate;
    aLog.UTUV:=aUser;
    aLog.PG_VER_REC:=1;
    ins_STIPENDI_COFI_LOGS(aLog);

    --Flusso Stipendi 'Principale'
    For aStipObbDett In cObb(FLUSSO_PRINCIPALE) Loop
        esistonoDati := 'Y';
          aStipObb.esercizio      := aStipObbDett.esercizio;
          aStipObb.mese       := aStipObbDett.mese;
          aStipObb.cd_cds_obbligazione    := aStipObbDett.cd_cds_obbligazione;
          aStipObb.esercizio_obbligazione   := aStipObbDett.esercizio_obbligazione;
          aStipObb.pg_obbligazione    := aStipObbDett.pg_obbligazione;
          aStipObb.im_totale      := aStipObbDett.im_totale;
          aStipObb.dacr       := Sysdate;
          aStipObb.utcr       := aUser;
          aStipObb.duva       := Sysdate;
          aStipObb.utuv       := aUser;
          aStipObb.pg_ver_rec       := 1;
          aStipObb.esercizio_ori_obbligazione   := aStipObbDett.esercizio_ori_obbligazione;
          ins_STIPENDI_COFI_OBB_SCAD(aStipObb);
    End Loop;
    If esistonoDati = 'Y' Then
       ibmutl200.logInf(gPgLog, '1', 'Flusso Stipendi Principale (parte spesa) elaborato', '');
    Else
       ibmutl200.logInf(gPgLog, '1', 'Non esistono dati da elaborare per il Flusso Stipendi Principale (parte spesa)', '');
    End If;
    esistonoDati := 'N';
    For aStipCoriDett In cCori(FLUSSO_PRINCIPALE) Loop
        esistonoDati := 'Y';
          aStipCori.esercizio       := aStipCoriDett.esercizio;
          aStipCori.mese      := aStipCoriDett.mese;
          aStipCori.cd_contributo_ritenuta  := aStipCoriDett.cd_contributo_ritenuta;
          aStipCori.ti_ente_percipiente   := aStipCoriDett.ti_ente_percipiente;
          aStipCori.ammontare       := aStipCoriDett.ammontare;
          aStipCori.dt_da_competenza_coge   := aStipCoriDett.dt_da_competenza_coge;
          aStipCori.dt_a_competenza_coge  := aStipCoriDett.dt_a_competenza_coge;
          aStipCori.dacr      := Sysdate;
          aStipCori.utcr      := aUser;
          aStipCori.duva      := Sysdate;
          aStipCori.utuv      := aUser;
          aStipCori.pg_ver_rec      := 1;
          ins_STIPENDI_COFI_CORI(aStipCori);
    End Loop;
    If esistonoDati = 'Y' Then
       ibmutl200.logInf(gPgLog, '2', 'Flusso Stipendi Principale (parte entrata) elaborato', '');
    Else
       ibmutl200.logInf(gPgLog, '2', 'Non esistono dati da elaborare per il Flusso Stipendi Principale (parte entrata)', '');
    End If;

    --Flusso Stipendi 'Annulli Stipendiali'
    --Va' elaborato prima della regionalizzazione irap
    esistonoDati := 'N';
    For aStipObbDett In cObb(FLUSSO_ANNULLI) Loop
        esistonoDati := 'Y';
          Update STIPENDI_COFI_OBB_SCAD
          Set im_totale = Nvl(im_totale,0) - Nvl(aStipObbDett.im_totale,0)
          Where esercizio       = aStipObbDett.esercizio
            And mese        = aStipObbDett.mese
            And cd_cds_obbligazione     = aStipObbDett.cd_cds_obbligazione
            And esercizio_obbligazione    = aStipObbDett.esercizio_obbligazione
            And pg_obbligazione     = aStipObbDett.pg_obbligazione
            And esercizio_ori_obbligazione  = aStipObbDett.esercizio_ori_obbligazione;
          If sql%Rowcount = 0 Then
            errore := 'Y';
            ibmutl200.logErr(gPgLog, '6', 'Nel Flusso Annulli Stipendiali è presente l''obbligazione '
            ||To_Char(aStipObbDett.esercizio_obbligazione)||'/'||To_Char(aStipObbDett.esercizio_ori_obbligazione)||'/'||aStipObbDett.cd_cds_obbligazione||'/'||To_Char(aStipObbDett.pg_obbligazione)
            ||' che non esiste nel flusso degli Stipendi Principale', '');
            -- inserisco la riga
            aStipObb.esercizio      := aStipObbDett.esercizio;
            aStipObb.mese         := aStipObbDett.mese;
            aStipObb.cd_cds_obbligazione    := aStipObbDett.cd_cds_obbligazione;
            aStipObb.esercizio_obbligazione   := aStipObbDett.esercizio_obbligazione;
            aStipObb.pg_obbligazione    := aStipObbDett.pg_obbligazione;
            aStipObb.im_totale      := - aStipObbDett.im_totale;
            aStipObb.dacr         := Sysdate;
            aStipObb.utcr         := aUser;
            aStipObb.duva         := Sysdate;
            aStipObb.utuv         := aUser;
            aStipObb.pg_ver_rec       := 1;
            aStipObb.esercizio_ori_obbligazione   := aStipObbDett.esercizio_ori_obbligazione;
            ins_STIPENDI_COFI_OBB_SCAD(aStipObb);
          End If;
    End Loop;
    If esistonoDati = 'Y' Then
       ibmutl200.logInf(gPgLog, '6', 'Flusso Annulli Stipendiali (parte spesa) elaborato', '');
    Else
       ibmutl200.logInf(gPgLog, '6', 'Non esistono dati da elaborare per il Flusso Annulli Stipendiali (parte spesa)', '');
    End If;
    esistonoDati := 'N';
    For aStipCoriDett In cCori(FLUSSO_ANNULLI) Loop
        esistonoDati := 'Y';
          Update STIPENDI_COFI_CORI
          Set ammontare = Nvl(ammontare,0) - Nvl(aStipCoriDett.ammontare,0)
          Where esercizio       = aStipCoriDett.esercizio
            And mese          = aStipCoriDett.mese
            And cd_contributo_ritenuta    = aStipCoriDett.cd_contributo_ritenuta
            And ti_ente_percipiente   = aStipCoriDett.ti_ente_percipiente;

          If sql%Rowcount = 0 Then
            errore := 'Y';
            ibmutl200.logErr(gPgLog, '6', 'Nel Flusso Annulli Stipendiali è presente la ritenuta '
            ||aStipCoriDett.cd_contributo_ritenuta||' che non esiste nel flusso degli Stipendi Principale', '');
            -- inserisco la riga
            aStipCori.esercizio     := aStipCoriDett.esercizio;
            aStipCori.mese      := aStipCoriDett.mese;
            aStipCori.cd_contributo_ritenuta:= aStipCoriDett.cd_contributo_ritenuta;
            aStipCori.ti_ente_percipiente   := aStipCoriDett.ti_ente_percipiente;
            aStipCori.ammontare     := - aStipCoriDett.ammontare;
            aStipCori.dt_da_competenza_coge := aStipCoriDett.dt_da_competenza_coge;
            aStipCori.dt_a_competenza_coge  := aStipCoriDett.dt_a_competenza_coge;
            aStipCori.dacr      := Sysdate;
            aStipCori.utcr      := aUser;
            aStipCori.duva      := Sysdate;
            aStipCori.utuv      := aUser;
            aStipCori.pg_ver_rec    := 1;
            ins_STIPENDI_COFI_CORI(aStipCori);
          End If;

          Declare
             importo NUMBER;
          Begin
             Select ammontare
             Into importo
             From STIPENDI_COFI_CORI
             Where esercizio      = aStipCoriDett.esercizio
               And mese       = aStipCoriDett.mese
               And cd_contributo_ritenuta = aStipCoriDett.cd_contributo_ritenuta
               And ti_ente_percipiente    = aStipCoriDett.ti_ente_percipiente;
             If importo < 0 Then
                ibmutl200.logInf(gPgLog, '7', 'Dopo l''elaborazione del Flusso degli Annulli, la ritenuta '
                ||aStipCoriDett.cd_contributo_ritenuta||' risulta negativa ('||To_Char(importo, '999G999G999G999G990D00')||')', '');
             End If;
          End;
    End Loop;
    If esistonoDati = 'Y' Then
       ibmutl200.logInf(gPgLog, '7', 'Flusso Annulli Stipendiali (parte entrata) elaborato', '');
    Else
       ibmutl200.logInf(gPgLog, '7', 'Non esistono dati da elaborare per il Flusso Annulli Stipendiali (parte entrata)', '');
    End If;

    --Flusso Rimborsi (solo parte entrata)
    esistonoDati := 'N';
    For aStipCoriDett In cCori(FLUSSO_RIMBORSI) Loop
        esistonoDati := 'Y';
          Update STIPENDI_COFI_CORI
          Set ammontare = Nvl(ammontare,0) - Nvl(aStipCoriDett.ammontare,0)
          Where esercizio       = aStipCoriDett.esercizio
            And mese          = aStipCoriDett.mese
            And cd_contributo_ritenuta    = aStipCoriDett.cd_contributo_ritenuta
            And ti_ente_percipiente   = aStipCoriDett.ti_ente_percipiente;

          If sql%Rowcount = 0 Then
            errore := 'Y';
            ibmutl200.logErr(gPgLog, '6', 'Nel Flusso Rimborsi è presente la ritenuta '
            ||aStipCoriDett.cd_contributo_ritenuta||' che non esiste nel flusso degli Stipendi Principale', '');
            -- inserisco la riga
            aStipCori.esercizio     := aStipCoriDett.esercizio;
            aStipCori.mese      := aStipCoriDett.mese;
            aStipCori.cd_contributo_ritenuta:= aStipCoriDett.cd_contributo_ritenuta;
            aStipCori.ti_ente_percipiente   := aStipCoriDett.ti_ente_percipiente;
            aStipCori.ammontare     := - aStipCoriDett.ammontare;
            aStipCori.dt_da_competenza_coge := aStipCoriDett.dt_da_competenza_coge;
            aStipCori.dt_a_competenza_coge  := aStipCoriDett.dt_a_competenza_coge;
            aStipCori.dacr      := Sysdate;
            aStipCori.utcr      := aUser;
            aStipCori.duva      := Sysdate;
            aStipCori.utuv      := aUser;
            aStipCori.pg_ver_rec    := 1;
            ins_STIPENDI_COFI_CORI(aStipCori);
          End If;

          Declare
             importo NUMBER;
          Begin
             Select ammontare
             Into importo
             From STIPENDI_COFI_CORI
             Where esercizio      = aStipCoriDett.esercizio
               And mese       = aStipCoriDett.mese
               And cd_contributo_ritenuta = aStipCoriDett.cd_contributo_ritenuta
               And ti_ente_percipiente    = aStipCoriDett.ti_ente_percipiente;
             If importo < 0 Then
                ibmutl200.logInf(gPgLog, '7', 'Dopo l''elaborazione del Flusso dei Rimborsi, la ritenuta '
                ||aStipCoriDett.cd_contributo_ritenuta||' risulta negativa ('||To_Char(importo, '999G999G999G999G990D00')||')', '');
             End If;
          End;

    End Loop;
    If esistonoDati = 'Y' Then
       ibmutl200.logInf(gPgLog, '7', 'Flusso Rimborsi (parte entrata) elaborato', '');
    Else
       ibmutl200.logInf(gPgLog, '7', 'Non esistono dati da elaborare per il Flusso Rimborsi (parte entrata)', '');
    End If;

    --Flusso Stipendi 'Regionalizzazione IRAP'

    --controllo se l'importo presente in STIPENDI_COFI_CORI per la riga dell'IRAP
    --è uguale alla somma in STIPENDI_COFI_CORI_DETT dei dettagli per singola regione
    Select Nvl(Sum(ammontare),0)
    Into totaleIrap
    From STIPENDI_COFI_CORI_DETT
    Where esercizio = aEs
      And mese = aMese
      And tipo_flusso = FLUSSO_PRINCIPALE
      And cd_contributo_ritenuta In (Select cd_contributo_ritenuta
               From tipo_contributo_ritenuta
               Where cd_classificazione_cori = cnrctb545.isCoriIrap);

    Select Nvl(Sum(ammontare),0)
    Into annulliIrap
    From STIPENDI_COFI_CORI_DETT
    Where esercizio = aEs
      And mese = aMese
      And tipo_flusso = FLUSSO_ANNULLI
      And cd_contributo_ritenuta In (Select cd_contributo_ritenuta
               From tipo_contributo_ritenuta
               Where cd_classificazione_cori = cnrctb545.isCoriIrap);

    Select Nvl(Sum(ammontare),0)
    Into sommaDettIrap
    From STIPENDI_COFI_CORI_DETT
    Where esercizio = aEs
      And mese = aMese
      And tipo_flusso = FLUSSO_IRAP
      And cd_contributo_ritenuta In (Select cd_contributo_ritenuta
               From tipo_contributo_ritenuta
               Where cd_classificazione_cori = cnrctb545.isCoriIrap);
    If (totaleIrap - annulliIrap) != sommaDettIrap Then
       errore := 'Y';
       ibmutl200.logErr(gPgLog, '3', 'Errore di squadratura in Flusso Regionalizzazione IRAP. Nel Flusso principale '||
       'l''importo e'' '||To_Char(totaleIrap, '999G999G999G999G990D00')||' e nel flusso degli annulli l''importo e'' '||To_Char(annulliIrap, '999G999G999G999G990D00')||', mentre la somma dei dettagli e'' '||To_Char(sommaDettIrap, '999G999G999G999G990D00'), '');
    Else
      Delete STIPENDI_COFI_CORI
      Where esercizio = aEs
        And mese = aMese
        And cd_contributo_ritenuta In (Select cd_contributo_ritenuta
                               From tipo_contributo_ritenuta
                     Where cd_classificazione_cori = cnrctb545.isCoriIrap);
      esistonoDati := 'N';
      For aStipCoriDett In cCori(FLUSSO_IRAP) Loop
          esistonoDati := 'Y';
          aStipCori.esercizio       := aStipCoriDett.esercizio;
          aStipCori.mese      := aStipCoriDett.mese;
          aStipCori.cd_contributo_ritenuta  := aStipCoriDett.cd_contributo_ritenuta;
          aStipCori.ti_ente_percipiente   := aStipCoriDett.ti_ente_percipiente;
          aStipCori.ammontare       := aStipCoriDett.ammontare;
          aStipCori.dt_da_competenza_coge   := aStipCoriDett.dt_da_competenza_coge;
          aStipCori.dt_a_competenza_coge  := aStipCoriDett.dt_a_competenza_coge;
          aStipCori.dacr      := Sysdate;
          aStipCori.utcr      := aUser;
          aStipCori.duva      := Sysdate;
          aStipCori.utuv      := aUser;
          aStipCori.pg_ver_rec      := 1;
          ins_STIPENDI_COFI_CORI(aStipCori);
      End Loop;
      If esistonoDati = 'Y' Then
         ibmutl200.logInf(gPgLog, '3', 'Flusso Regionalizzazione IRAP elaborato', '');
      Else
       ibmutl200.logInf(gPgLog, '3', 'Non esistono dati da elaborare per il Flusso Regionalizzazione IRAP', '');
      End If;
    End If;
    --Flusso Stipendi 'Contributi Figurativi'
    -- Controllo di quadratura: il totale delle righe nel flusso di spesa deve essere uguale
    -- a quello nel flusso di entrata
    Select Nvl(Sum(im_totale),0)
    Into totSpesaContribFig
    From STIPENDI_COFI_OBB_SCAD_DETT
    Where esercizio = aEs
      And mese = aMese
      And tipo_flusso = FLUSSO_CONTR_FIGURATIVI;

    Select Nvl(Sum(ammontare),0)
    Into totEntrataContribFig
    From STIPENDI_COFI_CORI_DETT
    Where esercizio = aEs
      And mese = aMese
      And tipo_flusso = FLUSSO_CONTR_FIGURATIVI;

    If totSpesaContribFig != totEntrataContribFig Then
       errore := 'Y';
       ibmutl200.logErr(gPgLog, '3', 'Errore di squadratura nel Flusso dei Contributi Figurativi: Per la parte spesa '||
       'l''importo e'' '||To_Char(totSpesaContribFig, '999G999G999G999G990D00')||' mentre per la parte entrata l''importo e'' '||To_Char(totEntrataContribFig, '999G999G999G999G990D00'), '');
    End if;

    esistonoDati := 'N';
    For aStipObbDett In cObb(FLUSSO_CONTR_FIGURATIVI) Loop
        esistonoDati := 'Y';
          Update STIPENDI_COFI_OBB_SCAD
          Set im_totale = Nvl(im_totale,0) + Nvl(aStipObbDett.im_totale,0)
          Where esercizio       = aStipObbDett.esercizio
            And mese        = aStipObbDett.mese
            And cd_cds_obbligazione     = aStipObbDett.cd_cds_obbligazione
            And esercizio_obbligazione    = aStipObbDett.esercizio_obbligazione
            And pg_obbligazione     = aStipObbDett.pg_obbligazione
            And esercizio_ori_obbligazione  = aStipObbDett.esercizio_ori_obbligazione;
          If sql%Rowcount = 0 Then
            errore := 'Y';
            ibmutl200.logErr(gPgLog, '6', 'Nel Flusso Contributi Figurativi è presente l''obbligazione '
            ||To_Char(aStipObbDett.esercizio_obbligazione)||'/'||To_Char(aStipObbDett.esercizio_ori_obbligazione)||'/'||aStipObbDett.cd_cds_obbligazione||'/'||To_Char(aStipObbDett.pg_obbligazione)
            ||' che non esiste nel flusso degli Stipendi Principale', '');
          End If;
    End Loop;
    If esistonoDati = 'Y' Then
       ibmutl200.logInf(gPgLog, '4', 'Flusso Contributi Figurativi (parte spesa) elaborato', '');
    Else
       ibmutl200.logInf(gPgLog, '4', 'Non esistono dati da elaborare per il Flusso Contributi Figurativi (parte spesa)', '');
    End If;
    esistonoDati := 'N';
    For aStipCoriDett In cCori(FLUSSO_CONTR_FIGURATIVI) Loop
        esistonoDati := 'Y';
          Update STIPENDI_COFI_CORI
          Set ammontare = Nvl(ammontare,0) + Nvl(aStipCoriDett.ammontare,0)
          Where esercizio       = aStipCoriDett.esercizio
            And mese          = aStipCoriDett.mese
            And cd_contributo_ritenuta    = aStipCoriDett.cd_contributo_ritenuta
            And ti_ente_percipiente   = aStipCoriDett.ti_ente_percipiente;
          If sql%Rowcount = 0 Then
            errore := 'Y';
            ibmutl200.logErr(gPgLog, '6', 'Nel Flusso Contributi Figurativi è presente la ritenuta '
            ||aStipCoriDett.cd_contributo_ritenuta||' che non esiste nel flusso degli Stipendi Principale', '');
          End If;
    End Loop;
    If esistonoDati = 'Y' Then
       ibmutl200.logInf(gPgLog, '5', 'Flusso Contributi Figurativi (parte entrata) elaborato', '');
    Else
       ibmutl200.logInf(gPgLog, '5', 'Non esistono dati da elaborare per il Flusso Contributi Figurativi (parte entrata)', '');
    End If;

    --Solo per il Flusso 15 deve essere: importo dell'unica riga del flusso S di spesa = alla somma di tutte le righe del flusso S di entrata.
    If aMese = 15 Then
        Begin
          Select Nvl(Sum(im_totale),0)
          Into totaleSpesa
          From STIPENDI_COFI_OBB_SCAD
          Where esercizio = aEs
            And mese = aMese;

          Select Nvl(Sum(ammontare),0)
          Into totaleEntrata
          From STIPENDI_COFI_CORI
          Where esercizio = aEs
            And mese = aMese;

          If totaleSpesa != totaleEntrata Then
             errore := 'Y';
             ibmutl200.logErr(gPgLog, '15', 'Errore di Squadratura per il mese '||To_Char(aMese)||'. Nel Flusso di spesa '||
           'il totale e'' '||To_Char(totaleSpesa)||' mentre nel flusso di entrata il totale e'' '||To_Char(totaleEntrata), '');
          End If;
        End;
    End If;

    --Flusso Stipendi 'Addizionali Comunali'
    -- A differenza degli altri flussi, quello delle addizionali comunali non viene scaricato su STIPENDI_COFI_CORI
    Begin

      esistonoDati := 'N';

      Select Count(1)
       Into contaAddCom
       From STIPENDI_COFI_CORI_DETT
       Where esercizio = aEs
         And mese = aMese
         And tipo_flusso = FLUSSO_ADDIZIONALE_COMUNALE;

      If contaAddCom > 0 Then
          esistonoDati := 'Y';
      End If;

      Select Count(1)
      Into contaAddCom
      From STIPENDI_COFI_CORI_DETT
      Where esercizio = aEs
        And mese = aMese
        And tipo_flusso = FLUSSO_ADDIZIONALE_COMUNALE
        And cd_catastale Is Null;

      If contaAddCom >0 Then
           errore := 'Y';
         ibmutl200.logErr(gPgLog, '9', 'Errore nel Flusso delle Addizionali Comunali. Alcuni Codici Catastali non sono valorizzati', '');
      Else
           Select Count(1)
           Into contaAddCom
           From STIPENDI_COFI_CORI_DETT
           Where esercizio = aEs
             And mese = aMese
             And tipo_flusso = FLUSSO_ADDIZIONALE_COMUNALE
             And cd_catastale Not In (Select cd_catastale From comune Where ti_italiano_estero = 'I');

           If contaAddCom >0 Then
                errore := 'Y';
                Begin
                   for rec in (select cd_catastale
                              From STIPENDI_COFI_CORI_DETT
                              Where esercizio = aEs
                                And mese = aMese
                                And tipo_flusso = FLUSSO_ADDIZIONALE_COMUNALE
                                And cd_catastale Not In (Select cd_catastale From comune Where ti_italiano_estero = 'I'))
                   loop
                         ibmutl200.logErr(gPgLog, '9', 'Errore nel Flusso delle Addizionali Comunali. Il Codice Catastale '||rec.cd_catastale||' non è corretto', '');
                   end loop;
                End;
              --ibmutl200.logErr(gPgLog, '9', 'Errore nel Flusso delle Addizionali Comunali. Alcuni Codici Catastali non sono corretti', '');
           Else
             If esistonoDati = 'Y' Then
                --per tutti i cori presenti in STIPENDI_COFI_CORI det tipo Addizionale, controllo la quadratura in STIPENDI_COFI_CORI_DETT
                For aStipCori In (Select *
                       From STIPENDI_COFI_CORI
                     Where esercizio = aEs
                        And mese = aMese
                      And cd_contributo_ritenuta In (Select cd_contributo_ritenuta
                                             From tipo_contributo_ritenuta
                                   Where cnrctb545.getIsAddComunale(cd_classificazione_cori) = 'Y')) Loop

            -- Controllo la quadratura con il flusso dei dettagli
          Select Nvl(Sum(ammontare),0)
              Into sommaDetAddCom
              From STIPENDI_COFI_CORI_DETT
              Where esercizio = aEs
                      And mese = aMese
                      And tipo_flusso = FLUSSO_ADDIZIONALE_COMUNALE
                      And cd_contributo_ritenuta = aStipCori.cd_contributo_ritenuta
                      And ti_ente_percipiente = aStipCori.ti_ente_percipiente;

                      If aStipCori.ammontare != sommaDetAddCom Then
               errore := 'Y';
               ibmutl200.logErr(gPgLog, '3', 'Errore di squadratura nel Flusso delle Addizionali Comunali per il cori '||To_Char(aStipCori.cd_contributo_ritenuta)||
               '. Nel Flusso (Principale - Annulli - Rimborsi) l''importo e'' '||To_Char(aStipCori.ammontare, '999G999G999G999G990D00')||' mentre la somma dei dettagli e'' '||To_Char(sommaDetAddCom, '999G999G999G999G990D00'), '');
                End If;

          -- Aggiorno l'importo del gruppo su STIPENDI_COFI_CORI con la somma dei positivi della colonna saldo
          -- cioè con quello che realmente verso con F24
          Select Nvl(Sum(saldo),0)
              Into sommaSaldiPosAddCom
              From STIPENDI_COFI_CORI_DETT
              Where esercizio = aEs
                      And mese = aMese
                      And tipo_flusso = FLUSSO_ADDIZIONALE_COMUNALE
                      And cd_contributo_ritenuta = aStipCori.cd_contributo_ritenuta
                      And ti_ente_percipiente = aStipCori.ti_ente_percipiente
                      And saldo > 0;

                    -- Calcolo la differenza tra quello che mi risulta da versare su STIPENDI_COFI_CORI e quello
                    -- che realmente verserò con F24 (cioè la somma dei positivi della colonna saldo in STIPENDI_COFI_CORI_DETT
                    differenza := sommaSaldiPosAddCom - Nvl(aStipCori.ammontare,0);
                    -- se la differenza è positiva vuol dire che in F24 ho di più di quello che mi risulta da versare
                    -- e quindi aumento l'importo da versare esattamente della differenza
                    -- se la differenza è negativa vuol dire che in F24 ho di meno di quello che mi risulta da versare
                    -- e quindi diminuisco l'importo da versare esattamente della differenza
                    If differenza != 0 Then
                        --devo aggiungere in STIPENDI_COFI_CORI la ritenuta per la differenza delle addizionali comunali
                        ritAddCom := CNRCTB015.getVal01PerChiave(aEs, 'DIFFERENZA_ADD_COM_STIPENDI','CONTRIBUTO_RITENUTA');

                        Update STIPENDI_COFI_CORI
                           Set ammontare = Nvl(ammontare,0) + differenza
                         Where esercizio = aEs
                     And mese = aMese
                     And cd_contributo_ritenuta = aStipCori.cd_contributo_ritenuta
                     And ti_ente_percipiente = aStipCori.ti_ente_percipiente;

                  If differenza > 0 Then
                     ibmutl200.logInf(gPgLog, '5', 'Per il cori '||To_Char(aStipCori.cd_contributo_ritenuta)||' è stato aggiunto all''importo '||
                 To_Char(aStipCori.ammontare, '999G999G999G999G990D00')||', l''importo '||To_Char(Abs(differenza), '999G999G999G999G990D00'), '');
              Else
                 ibmutl200.logInf(gPgLog, '5', 'Per il cori '||To_Char(aStipCori.cd_contributo_ritenuta)||' è stato diminuito l''importo '||
                 To_Char(aStipCori.ammontare, '999G999G999G999G990D00')||', dell''importo '||To_Char(Abs(differenza), '999G999G999G999G990D00'), '');
              End If;

                        --l'importo eventualmente aumentato o diminuito viene inserito con una ritenuta particolare per la quadratura degli stipendi
                    Begin
                       If aMese = 15 Then
                          meseCompetenza := 11;
                       Elsif aMese = 13 Then
                          meseCompetenza := 12;
                       Else
                          meseCompetenza := aMese;
                       End If;

                           aStipCori.esercizio      := aEs;
                 aStipCori.mese       := aMese;
                 aStipCori.cd_contributo_ritenuta   := ritAddCom;
                 aStipCori.ti_ente_percipiente  := 'P';
                 aStipCori.ammontare      := -differenza;
                 aStipCori.dt_da_competenza_coge  := IBMUTL001.getFirstDayOfMonth(TO_DATE('01'|| To_Char(Lpad(meseCompetenza,2,0)) || To_Char(aEs), 'DDMMYYYY'));
                 aStipCori.dt_a_competenza_coge   := IBMUTL001.getLastDayOfMonth(TO_DATE('01'|| To_Char(Lpad(meseCompetenza,2,0)) || To_Char(aEs), 'DDMMYYYY'));
                 aStipCori.dacr       := Sysdate;
                 aStipCori.utcr       := aUser;
                 aStipCori.duva       := Sysdate;
                 aStipCori.utuv       := aUser;
                 aStipCori.pg_ver_rec     := 1;
                 ins_STIPENDI_COFI_CORI(aStipCori);
                    Exception
               When Dup_Val_On_Index Then
                  Update STIPENDI_COFI_CORI
                  Set ammontare = Nvl(ammontare,0) - Nvl(differenza,0)
                  Where esercizio = aEs
                      And mese  = aMese
                      And cd_contributo_ritenuta = ritAddCom
                      And ti_ente_percipiente = 'P';
                      End;

      ibmutl200.logInf(gPgLog, '5', 'Verra'' aggiunta alla ritenuta '||ritAddCom||' l''importo '
                ||To_Char(- differenza, '999G999G999G999G990D00'), '');
              End If;
        End Loop;
         End If;
       End If;
      End If;

      If esistonoDati = 'Y' Then
         ibmutl200.logInf(gPgLog, '3', 'Flusso Addizionali comunali elaborato', '');
      Else
       ibmutl200.logInf(gPgLog, '3', 'Non esistono dati da elaborare per il Flusso Addizionali comunali', '');
      End If;

    End;

    -- Solo per il flusso 15 devo eliminare tutte le ritenute negative ed incrementare l'unica riga di spesa
    If aMese = 15 Then
    Begin
       For aStipCori In cCoriElaborati Loop
          If aStipCori.ammontare < 0 Then
             totaleNegativi := totaleNegativi + aStipCori.ammontare;

             ibmutl200.logInf(gPgLog, '2', 'Eliminata la ritenuta '||To_Char(aStipCori.cd_contributo_ritenuta)||' perche'' negativa. ('||To_Char(aStipCori.ammontare)||')', '');

             Delete STIPENDI_COFI_CORI
             Where esercizio = aStipCori.esercizio
               And mese = aStipCori.mese
               And cd_contributo_ritenuta = aStipCori.cd_contributo_ritenuta
               And ti_ente_percipiente = aStipCori.ti_ente_percipiente;
          End If;
       End Loop;
       If totaleNegativi != 0 Then
         Update stipendi_cofi_obb_scad
         Set im_totale = im_totale + (-totaleNegativi)
         Where esercizio = aEs
           And mese = aMese
           And Rownum < 2;
       End If;
       ibmutl200.logInf(gPgLog, '2', 'Incrementata la riga di spesa dell''importo '||To_Char(-totaleNegativi, '999G999G999G999G990D00')||'.', '');
    End;
    End If; --If aMese = 15 Then

    If errore = 'N' Then
       ibmutl200.logInf(gPgLog, '8', 'Elaborazione terminata con successo', '');
    Else
       ibmutl200.logInf(gPgLog, '8', 'Elaborazione terminata. Si sono verificati degli errori. Controllare il risultato dell''elaborazione', '');
    End If;
  End;

 Procedure ins_STIPENDI_COFI_OBB_SCAD (aDest STIPENDI_COFI_OBB_SCAD%rowtype) is
  begin
   insert into STIPENDI_COFI_OBB_SCAD (
     ESERCIZIO,
     MESE,
     CD_CDS_OBBLIGAZIONE,
     ESERCIZIO_OBBLIGAZIONE,
     PG_OBBLIGAZIONE,
     IM_TOTALE,
     DACR,
     UTCR,
     DUVA,
     UTUV,
     PG_VER_REC,
     ESERCIZIO_ORI_OBBLIGAZIONE
   ) values (
     aDest.ESERCIZIO,
     aDest.MESE,
     aDest.CD_CDS_OBBLIGAZIONE,
     aDest.ESERCIZIO_OBBLIGAZIONE,
     aDest.PG_OBBLIGAZIONE,
     aDest.IM_TOTALE,
     aDest.DACR,
     aDest.UTCR,
     aDest.DUVA,
     aDest.UTUV,
     aDest.PG_VER_REC,
     aDest.ESERCIZIO_ORI_OBBLIGAZIONE
    );
 end;
 Procedure ins_STIPENDI_COFI_CORI (aDest STIPENDI_COFI_CORI%rowtype) is
  begin
   insert into STIPENDI_COFI_CORI (
     ESERCIZIO,
     MESE,
     CD_CONTRIBUTO_RITENUTA,
     TI_ENTE_PERCIPIENTE,
     AMMONTARE,
     DT_DA_COMPETENZA_COGE,
     DT_A_COMPETENZA_COGE,
     DACR,
     UTCR,
     DUVA,
     UTUV,
     PG_VER_REC
   ) values (
     aDest.ESERCIZIO,
     aDest.MESE,
     aDest.CD_CONTRIBUTO_RITENUTA,
     aDest.TI_ENTE_PERCIPIENTE,
     aDest.AMMONTARE,
     aDest.DT_DA_COMPETENZA_COGE,
     aDest.DT_A_COMPETENZA_COGE,
     aDest.DACR,
     aDest.UTCR,
     aDest.DUVA,
     aDest.UTUV,
     aDest.PG_VER_REC
    );
 end;
 Procedure ins_STIPENDI_COFI_LOGS (aLog STIPENDI_COFI_LOGS%rowtype) is
  begin
   insert into STIPENDI_COFI_LOGS (
     ESERCIZIO,
     MESE,
     PG_ESECUZIONE,
     DACR,
     UTCR,
     DUVA,
     UTUV,
     PG_VER_REC
   ) values (
     aLog.ESERCIZIO,
     aLog.MESE,
     aLog.PG_ESECUZIONE,
     aLog.DACR,
     aLog.UTCR,
     aLog.DUVA,
     aLog.UTUV,
     aLog.PG_VER_REC
    );
 end;
 Procedure annullaElabStipDett (aEs number,
                aMese number,
                aUser varchar2) Is
  stato_liquidazione  VARCHAR2(1);
 Begin
    --controllo che per il mese scelto non sia stata già effettuata la liquidazione
    Select stato
    Into stato_liquidazione
    From STIPENDI_COFI
    Where esercizio = aEs
      And mese = aMese;

    If stato_liquidazione = 'P' Then
         IBMERR001.RAISE_ERR_GENERICO('Non è possibile procedere all''annullamento dell''elaborazione poichè è stata già effettuata la liquidazione del mese selezionato');
    Else
         Delete STIPENDI_COFI_OBB_SCAD
       Where esercizio = aEs
           And mese = aMese;

       Delete STIPENDI_COFI_CORI
       Where esercizio = aEs
           And mese = aMese;

       Delete STIPENDI_COFI_LOGS
       Where esercizio = aEs
           And mese = aMese;
    End If;
  End;

END;
