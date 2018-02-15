--------------------------------------------------------
--  DDL for Package Body CNRCTB046
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB046" is

  Function getElementoVoceNew (aEvOld elemento_voce%rowtype)
  Return elemento_voce%rowtype Is
    recElementoVoceNew elemento_voce%rowtype;
  Begin
    Begin
      Select elemento_voce.* into recElementoVoceNew
      From   ass_evold_evnew, elemento_voce
      Where  ass_evold_evnew.esercizio_old = aEvOld.ESERCIZIO
      And    ass_evold_evnew.ti_appartenenza_old = aEvOld.TI_APPARTENENZA
      And    ass_evold_evnew.ti_gestione_old = aEvOld.TI_GESTIONE
      And    ass_evold_evnew.cd_elemento_voce_old = aEvOld.CD_ELEMENTO_VOCE
      aND    ass_evold_evnew.esercizio_new = elemento_voce.ESERCIZIO
      And    ass_evold_evnew.ti_appartenenza_new = elemento_voce.TI_APPARTENENZA
      And    ass_evold_evnew.ti_gestione_new = elemento_voce.TI_GESTIONE
      And    ass_evold_evnew.cd_elemento_voce_new = elemento_voce.CD_ELEMENTO_VOCE;
    Exception
      When no_data_found Then
        null;
      When too_many_rows Then
        ibmerr001.RAISE_ERR_GENERICO('Esistono piÃ¹ voci associate alla voce '||aEvOld.CD_ELEMENTO_VOCE||', appartenenza '||aEvOld.TI_APPARTENENZA||', gestione '||aEvOld.TI_GESTIONE||', esercizio '||aEvOld.ESERCIZIO||' indicata sull''impegno. Impossibile individuare la voce su cui ribaltare.');
    End;
    return recElementoVoceNew;
  End;

  Function getElementoVoceNew (aObb obbligazione%rowtype)
  Return elemento_voce%rowtype Is
    recElementoVoceNew elemento_voce%rowtype;
  Begin
    Begin
      Select elemento_voce.* into recElementoVoceNew
      From   ass_evold_evnew, elemento_voce
      Where  ass_evold_evnew.esercizio_old = aObb.ESERCIZIO
      And    ass_evold_evnew.ti_appartenenza_old = aObb.TI_APPARTENENZA
      And    ass_evold_evnew.ti_gestione_old = aObb.TI_GESTIONE
      And    ass_evold_evnew.cd_elemento_voce_old = aObb.CD_ELEMENTO_VOCE
      aND    ass_evold_evnew.esercizio_new = elemento_voce.ESERCIZIO
      And    ass_evold_evnew.ti_appartenenza_new = elemento_voce.TI_APPARTENENZA
      And    ass_evold_evnew.ti_gestione_new = elemento_voce.TI_GESTIONE
      And    ass_evold_evnew.cd_elemento_voce_new = elemento_voce.CD_ELEMENTO_VOCE;
    Exception
      When no_data_found Then
        null;
      When too_many_rows Then
        ibmerr001.RAISE_ERR_GENERICO('Esistono piÃ¹ voci associate alla voce '||aObb.CD_ELEMENTO_VOCE||', appartenenza '||aObb.TI_APPARTENENZA||', gestione '||aObb.TI_GESTIONE||', esercizio '||aObb.ESERCIZIO||' indicata sull''impegno. Impossibile individuare la voce su cui ribaltare.');
    End;

    If recElementoVoceNew.esercizio is null Then
      If aObb.ESERCIZIO_EV_NEXT is not null and aObb.TI_APPARTENENZA_EV_NEXT is not null and
        aObb.TI_GESTIONE_EV_NEXT is not null and aObb.CD_ELEMENTO_VOCE_NEXT is not null Then
        Begin
          Select * into recElementoVoceNew
          From   elemento_voce
          Where  esercizio = aObb.ESERCIZIO_EV_NEXT
          And    ti_appartenenza = aObb.TI_APPARTENENZA_EV_NEXT
          And    ti_gestione = aObb.TI_GESTIONE_EV_NEXT
          And    cd_elemento_voce = aObb.CD_ELEMENTO_VOCE_NEXT;
        Exception
          When no_data_found Then
            ibmerr001.RAISE_ERR_GENERICO('Elemento voce '||aObb.CD_ELEMENTO_VOCE_NEXT||', appartenenza '||aObb.TI_APPARTENENZA_EV_NEXT||', gestione '||aObb.TI_GESTIONE_EV_NEXT||', esercizio '||aObb.ESERCIZIO_EV_NEXT||' indicato sull''impegno inesistente. Impossibile ribaltare.');
        End;
      Else
        Begin
          Select elemento_voce.* into recElementoVoceNew
          From   elemento_voce
          Where  elemento_voce.ESERCIZIO = aObb.ESERCIZIO + 1
          And    elemento_voce.TI_APPARTENENZA = aObb.TI_APPARTENENZA
          And    elemento_voce.TI_GESTIONE = aObb.TI_GESTIONE
          And    elemento_voce.CD_ELEMENTO_VOCE = aObb.CD_ELEMENTO_VOCE;
        Exception
          When no_data_found Then
            ibmerr001.RAISE_ERR_GENERICO('Elemento voce '||aObb.CD_ELEMENTO_VOCE||', appartenenza '||aObb.TI_APPARTENENZA||', gestione '||aObb.TI_GESTIONE||' non presente nell''esercizio '||to_char(aObb.ESERCIZIO+1)||'. Impossibile ribaltare.');
        End;
      End If;
    End If;
    return recElementoVoceNew;
  End;

  Function getElementoVoceNew (aAcc accertamento%rowtype)
  Return elemento_voce%rowtype Is
    recElementoVoceNew elemento_voce%rowtype;
  Begin
    Begin
      Select elemento_voce.* into recElementoVoceNew
      From   ass_evold_evnew, elemento_voce
      Where  ass_evold_evnew.esercizio_old = aAcc.ESERCIZIO
      And    ass_evold_evnew.ti_appartenenza_old = aAcc.TI_APPARTENENZA
      And    ass_evold_evnew.ti_gestione_old = aAcc.TI_GESTIONE
      And    ass_evold_evnew.cd_elemento_voce_old = aAcc.CD_ELEMENTO_VOCE
      And    ass_evold_evnew.esercizio_new = elemento_voce.ESERCIZIO
      And    ass_evold_evnew.ti_appartenenza_new = elemento_voce.TI_APPARTENENZA
      And    ass_evold_evnew.ti_gestione_new = elemento_voce.TI_GESTIONE
      And    ass_evold_evnew.cd_elemento_voce_new = elemento_voce.CD_ELEMENTO_VOCE;
    Exception
      When no_data_found Then
        null;
      When too_many_rows Then
        ibmerr001.RAISE_ERR_GENERICO('Esistono piÃ¹ voci associate alla voce '||aAcc.CD_ELEMENTO_VOCE||', appartenenza '||aAcc.TI_APPARTENENZA||', gestione '||aAcc.TI_GESTIONE||', esercizio '||aAcc.ESERCIZIO||' indicata sull''accertamento. Impossibile individuare la voce su cui ribaltare.');
    End;

    If recElementoVoceNew.esercizio is null Then
      If aAcc.ESERCIZIO_EV_NEXT is not null and aAcc.TI_APPARTENENZA_EV_NEXT is not null and
        aAcc.TI_GESTIONE_EV_NEXT is not null and aAcc.CD_ELEMENTO_VOCE_NEXT is not null Then
        Begin
          Select * into recElementoVoceNew
          From   elemento_voce
          Where  esercizio = aAcc.ESERCIZIO_EV_NEXT
          And    ti_appartenenza = aAcc.TI_APPARTENENZA_EV_NEXT
          And    ti_gestione = aAcc.TI_GESTIONE_EV_NEXT
          And    cd_elemento_voce = aAcc.CD_ELEMENTO_VOCE_NEXT;
        Exception
          When no_data_found Then
            ibmerr001.RAISE_ERR_GENERICO('Elemento voce '||aAcc.CD_ELEMENTO_VOCE_NEXT||', appartenenza '||aAcc.TI_APPARTENENZA_EV_NEXT||', gestione '||aAcc.TI_GESTIONE_EV_NEXT||', esercizio '||aAcc.ESERCIZIO_EV_NEXT||' indicato sull''accertamento inesistente. Impossibile ribaltare.');
        End;
      Else
        Begin
          Select elemento_voce.* into recElementoVoceNew
          From   elemento_voce
          Where  elemento_voce.ESERCIZIO = aAcc.ESERCIZIO + 1
          And    elemento_voce.TI_APPARTENENZA = aAcc.TI_APPARTENENZA
          And    elemento_voce.TI_GESTIONE = aAcc.TI_GESTIONE
          And    elemento_voce.CD_ELEMENTO_VOCE = aAcc.CD_ELEMENTO_VOCE;
        Exception
          When no_data_found Then
            ibmerr001.RAISE_ERR_GENERICO('Elemento voce '||aAcc.CD_ELEMENTO_VOCE||', appartenenza '||aAcc.TI_APPARTENENZA||', gestione '||aAcc.TI_GESTIONE||' non presente nell''esercizio '||to_char(aAcc.ESERCIZIO+1)||'. Impossibile ribaltare.');
        End;
      End If;
    End If;
    return recElementoVoceNew;
 End;

 Function GET_ESERCIZIO_ORIGINALE (aCds varchar2,
                                   aEs number,
                                   aEsOri number,
                                   aPg number,
                                   aTiGestione VARCHAR2) Return NUMBER Is
 tipo         obbligazione.CD_TIPO_DOCUMENTO_CONT%Type;
 anno_origine obbligazione.esercizio_originale%Type;

 Begin
  If aTiGestione = 'S' Then
    Select CD_TIPO_DOCUMENTO_CONT, esercizio_originale
    Into   tipo, anno_origine
    From   obbligazione
    Where  cd_cds = aCds And
           esercizio = aEs  And
           esercizio_originale = aEsOri  And
           pg_obbligazione = aPg;
    If tipo In (cnrctb018.TI_DOC_OBB, cnrctb018.TI_DOC_IMP, cnrctb018.TI_DOC_OBB_PGIRO) Then
      Return aEs;
    Elsif tipo In (cnrctb018.TI_DOC_OBB_RES_PRO, cnrctb018.TI_DOC_OBB_RES_IMPRO,
                   cnrctb018.TI_DOC_IMP_RES) Then
      Return anno_origine;
    End If;

  Elsif aTiGestione = 'E' Then

    Select CD_TIPO_DOCUMENTO_CONT, esercizio_originale
    Into   tipo, anno_origine
    From   accertamento
    Where  cd_cds = aCds And
           esercizio = aEs  And
           esercizio_originale = aEsOri  And
           pg_accertamento = aPg;
    If tipo In (cnrctb018.TI_DOC_ACC, cnrctb018.TI_DOC_ACC_PGIRO, cnrctb018.TI_DOC_ACC_SIST) Then
      Return aEs;
    Elsif tipo In (cnrctb018.TI_DOC_ACC_RES) Then
      Return anno_origine;
    End If;
  End If;
 End;

 procedure aggiornaConnPgiroCds(aAcc accertamento%rowtype,aAccNew accertamento%rowtype) is
 begin
-- IBMERR001.RAISE_ERR_GENERICO('metto '||aAcc.pg_accertamento||' su '||aAccNew.cd_cds||'/'||aAccNew.esercizio||'/'||aAccNew.pg_accertamento);
  Update accertamento
  Set    cd_cds_ori_riporto = aAcc.cd_cds,
         esercizio_ori_riporto = aAcc.esercizio,
         esercizio_ori_ori_riporto=aAcc.esercizio_originale,
         pg_accertamento_ori_riporto = aAcc.pg_accertamento,
         pg_ver_rec = pg_ver_rec + 1
  Where  cd_cds=aAccNew.cd_cds
     and esercizio=aAccNew.esercizio
     and esercizio_originale=aAccNew.esercizio_originale
     and pg_accertamento=aAccNew.pg_accertamento;

  update accertamento_scadenzario set
   pg_acc_scad_ori_riporto = 1,
   pg_ver_rec = pg_ver_rec + 1
  where cd_cds = aAccNew.cd_cds
    and esercizio = aAccNew.esercizio
    and esercizio_originale=aAccNew.esercizio_originale
    and pg_accertamento = aAccNew.pg_accertamento
    and pg_accertamento_scadenzario = 1;

  update accertamento set
   riportato = 'Y',
   pg_ver_rec=pg_ver_rec+1
  where
       cd_cds=aAcc.cd_cds
   and esercizio=aAcc.esercizio
   and esercizio_originale=aAcc.esercizio_originale
   and pg_accertamento=aAcc.pg_accertamento;
 end;

 procedure aggiornaConnPgiroCds(aObb obbligazione%rowtype,aObbNew obbligazione%rowtype) is
 begin

  update obbligazione set
   cd_cds_ori_riporto=aObb.cd_cds,
   esercizio_ori_riporto=aObb.esercizio,
   esercizio_ori_ori_riporto=aObb.esercizio_originale,
   pg_obbligazione_ori_riporto=aObb.pg_obbligazione,
   pg_ver_rec = pg_ver_rec + 1
  where
        cd_cds=aObbNew.cd_cds
    and esercizio=aObbNew.esercizio
    and esercizio_originale=aObbNew.esercizio_originale
    and pg_obbligazione=aObbNew.pg_obbligazione;

  update obbligazione_scadenzario set
   pg_obbl_scad_ori_riporto = 1,
   pg_ver_rec = pg_ver_rec +1
  where cd_cds = aObbNew.cd_cds
    and esercizio = aObbNew.esercizio
    And esercizio_originale=aObbNew.esercizio_originale
	and pg_obbligazione = aObbNew.pg_obbligazione
	and pg_obbligazione_scadenzario = 1;

  update obbligazione set
    riportato = 'Y',
    pg_ver_rec=pg_ver_rec+1
  where
        cd_cds=aObb.cd_cds
    and esercizio=aObb.esercizio
    and esercizio_originale=aObb.esercizio_originale
    and pg_obbligazione=aObb.pg_obbligazione;
 end;


 procedure ripPgiroCdsInt(
  aAcc IN OUT accertamento%rowtype,
  aAccNew IN OUT accertamento%rowtype,
  aElementoVoce elemento_voce%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aObbNew obbligazione%rowtype;
  aObbScadNew obbligazione_scadenzario%rowtype;
  aAccScadNew accertamento_scadenzario%rowtype;
  aVoceF voce_f%rowtype;
  aImRiscontrato number;

 begin
  CNRCTB048.checkEsercizio(aAcc.esercizio, aAcc.cd_cds);
  CNRCTB035.getPgiroCds(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);

  aAccNew       :=aAcc;
  aAccScadNew   :=aAccScad;

  aAccNew.esercizio     :=aAcc.esercizio + 1;
  aAccNew.esercizio_competenza := aAccNew.esercizio;
  aAccNew.dt_registrazione := to_date('0101'||aAccNew.esercizio,'DDMMYYYY');

  /* 05.01.2006 stani */
  aAccNew.esercizio_originale    := aAcc.esercizio_originale;
  aAccNew.CD_TIPO_DOCUMENTO_CONT := cnrctb018.TI_DOC_ACC_PGIRO_RES; -- part giro res

  if aElementoVoce.esercizio is not null then
  -- cambio imputazione finanziaria
	  begin
	  	select * into aVoceF
		from voce_f
		where esercizio 		 = aAcc.esercizio + 1
		  and ti_appartenenza 	 = aElementoVoce.ti_appartenenza
		  and ti_gestione	  	 = aElementoVoce.ti_gestione
		  and cd_titolo_capitolo = aElementoVoce.cd_elemento_voce;
	  exception when NO_DATA_FOUND then
			ibmerr001.RAISE_ERR_GENERICO('Capitolo finanziario non trovato in corrispondenza dell''elemento voce '||aElementoVoce.cd_elemento_voce||', appartenenza '||aElementoVoce.ti_appartenenza||'gestione '||aElementoVoce.ti_gestione||', esercizio '||aAcc.esercizio + 1||', impossibile ribaltare');
	  end;
	  aAccNew.TI_APPARTENENZA		  	  :=aElementoVoce.TI_APPARTENENZA;
	  aAccNew.TI_GESTIONE			  	  :=aElementoVoce.TI_GESTIONE;
	  aAccNew.CD_ELEMENTO_VOCE		  	  :=aElementoVoce.CD_ELEMENTO_VOCE;
	  aAccNew.CD_VOCE				  	  :=aVoceF.cd_voce;
  end if;
  aAccNew.dacr := aTSNow;
  aAccNew.utcr := aUser;
  aAccScadNew.esercizio:=aAccNew.esercizio;
  -- partita di giro tronca se tronca in origine o se contropartita riscontrata
  aImRiscontrato := getIm_riscontrato(aObb.esercizio,aObb.cd_cds,aObb.esercizio_originale,aObb.pg_obbligazione,aObb.ti_gestione);

  if (aObb.dt_cancellazione is null and aImRiscontrato = 0) then

   CNRCTB040.creaAccertamentoPgiro(false,aAccNew,aAccScadNew,aObbNew,aObbScadNew,CNRCTB048.GETDTSCADENZA(aAccNew.esercizio,aAccScad.dt_scadenza_incasso));

   if aObbScad.im_associato_doc_amm > 0 then
	 update obbligazione_scadenzario
	 set im_associato_doc_amm = aObbScad.im_associato_doc_amm,
	 	 pg_ver_rec = pg_ver_rec + 1
	 where cd_cds = aObbScadNew.cd_cds
	   and esercizio = aObbscadNew.esercizio
	   and esercizio_originale = aObbscadNew.esercizio_originale
	   and pg_obbligazione = aObbScadNew.pg_obbligazione
	   and pg_obbligazione_scadenzario = aObbScadNew.pg_obbligazione_scadenzario;
     CNRCTB048.aggiornaDocAmm(aObbScad,aObbScadNew,aUser,aTSNow);
   end if;

  else

   CNRCTB040.creaAccertamentoPgiroTronc(false,aAccNew,aAccScadNew,aObbNew,aObbScadNew,CNRCTB048.GETDTSCADENZA(aAccNew.esercizio,aAccScad.dt_scadenza_incasso));

  end if;

  aggiornaConnPgiroCds(aAcc,aAccNew);
  -- 15.05.2006 stani: anche le ribaltate tronche devono riportare nel documento a zero la chiave dell'originale
  aggiornaConnPgiroCds(aObb,aObbNew);

  if aImriscontrato = 0 then -- escludo pgiro normali ricreate tronche
  	 aggiornaConnPgiroCds(aObb,aObbNew);
  end if;

  if aAccScad.im_associato_doc_amm > 0 then
	update accertamento_scadenzario
	set im_associato_doc_amm = aAccScad.im_associato_doc_amm,
	    pg_ver_rec = pg_ver_rec + 1
	where cd_cds = aAccScadNew.cd_cds
	  and esercizio = aAccScadNew.esercizio
	  and esercizio_originale = aAccScadNew.esercizio_originale
	  and pg_accertamento = aAccScadNew.pg_accertamento
	  and pg_accertamento_scadenzario = aAccScadNew.pg_accertamento_scadenzario;
    CNRCTB048.aggiornaDocAmm(aAccScad,aAccScadNew,aUser,aTSNow);
  end if;
 end;

 procedure ripPgiroCdsInt(
  aObb IN OUT obbligazione%rowtype,
  aAccNew IN OUT accertamento%rowtype,
  aElementoVoce elemento_voce%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aObbNew obbligazione%rowtype;
  aObbScadNew obbligazione_scadenzario%rowtype;
  aAccScadNew accertamento_scadenzario%rowtype;
  aVoceF voce_f%rowtype;
 begin
  CNRCTB048.checkEsercizio(aObb.esercizio,aObb.cd_cds);
  CNRCTB035.getPgiroCds(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);
  aAccNew:=aAcc;
  aAccScadNew:=aAccScad;
  aAccNew.esercizio:=aAcc.esercizio+1;
  aAccNew.esercizio_competenza := aAccNew.esercizio;
  aAccNew.dt_registrazione := to_date('0101'||aAccNew.esercizio,'DDMMYYYY');

  /* 05.01.2006 stani */
  aAccNew.esercizio_originale    := aAcc.esercizio_originale;
  aAccNew.CD_TIPO_DOCUMENTO_CONT := cnrctb018.TI_DOC_ACC_PGIRO_RES; -- part giro res

  if aElementoVoce.esercizio is not null then
  -- cambio imputazione finanziaria
	  begin
	  	select * into aVoceF
		from voce_f
		where esercizio 		 = aAcc.esercizio + 1
		  and ti_appartenenza 	 = aElementoVoce.ti_appartenenza
		  and ti_gestione	  	 = aElementoVoce.ti_gestione
		  and cd_titolo_capitolo = aElementoVoce.cd_elemento_voce;
	  exception when NO_DATA_FOUND then
			ibmerr001.RAISE_ERR_GENERICO('Capitolo finanziario non trovato in corrispondenza dell''elemento voce '||aElementoVoce.cd_elemento_voce||', appartenenza '||aElementoVoce.ti_appartenenza||'gestione '||aElementoVoce.ti_gestione||', esercizio '||aAcc.esercizio + 1||', impossibile ribaltare');
	  end;
	  aAccNew.TI_APPARTENENZA		  	  :=aElementoVoce.TI_APPARTENENZA;
	  aAccNew.TI_GESTIONE			  	  :=aElementoVoce.TI_GESTIONE;
	  aAccNew.CD_ELEMENTO_VOCE		  	  :=aElementoVoce.CD_ELEMENTO_VOCE;
	  aAccNew.CD_VOCE				  	  :=aVoceF.cd_voce;
  end if;
  aAccNew.dacr := aTSNow;
  aAccNew.utcr := aUser;
  aAccScadNew.esercizio:=aAccNew.esercizio;

-- IBMERR001.RAISE_ERR_GENERICO('stani ribaltamento 2');

  CNRCTB040.creaAccertamentoPgiroTronc(false,aAccNew,aAccScadNew,aObbNew,aObbScadNew,CNRCTB048.GETDTSCADENZA(aAccNew.esercizio,aAccScad.dt_scadenza_incasso));

  aggiornaConnPgiroCds(aAcc,aAccNew);
  -- 15.05.2006 stani: anche le ribaltate tronche devono riportare nel documento a zero la chiave dell'originale
  aggiornaConnPgiroCds(aObb,aObbNew);

--   if aObbScad.im_associato_doc_amm > 0 then
--     CNRCTB048.aggiornaDocAmm(aObbScad,aObbScadNew,aUser,aTSNow);
--   end if;
  if aAccScad.im_associato_doc_amm > 0 then
--	aAccScadNew.im_associato_doc_amm := aAccScad.im_associato_doc_amm;
	update accertamento_scadenzario
	set im_associato_doc_amm = aAccScad.im_associato_doc_amm,
	    pg_ver_rec = pg_ver_rec + 1
	where cd_cds = aAccScadNew.cd_cds
	  and esercizio = aAccScadNew.esercizio
	  and esercizio_originale = aAccScadNew.esercizio_originale
	  and pg_accertamento = aAccScadNew.pg_accertamento
	  and pg_accertamento_scadenzario = aAccScadNew.pg_accertamento_scadenzario;
    CNRCTB048.aggiornaDocAmm(aAccScad,aAccScadNew,aUser,aTSNow);
  end if;
 end;

Procedure ripPgiroCdsEntrambe(
  aObb            obbligazione%Rowtype,
  aObbScad        obbligazione_scadenzario%Rowtype,
  aObbScadVoce    obbligazione_scad_voce%Rowtype,
  aAcc            accertamento%Rowtype,
  aAccScad        accertamento_scadenzario%Rowtype,
  aAccScadVoce    accertamento_scad_voce%Rowtype,
  aElementoVoceE  elemento_voce%Rowtype,
  aElementoVoceS  elemento_voce%Rowtype,
  aTi_Origine     CHAR,
  aTSNow          DATE,
  aUser           VARCHAR2,
  aObbNew         Out obbligazione%Rowtype,
  aAccNew         Out accertamento%Rowtype) Is

aDettScadCObb        obbligazione_scad_voce%rowtype;
aDettScadCObbList    CNRCTB035.scadVoceListS;

--aObbNew              obbligazione%Rowtype;
--aAccNew              accertamento%Rowtype;

aObbScadNew          obbligazione_scadenzario%rowtype;
aAccScadNew          accertamento_scadenzario%rowtype;

aDettScadCAcc        accertamento_scad_voce%rowtype;
aDettScadCAccList    CNRCTB035.scadVoceListE;

aImRiscontratoEnt    NUMBER;
aImRiscontratoSpe    NUMBER;

aGAE_dedicata_CDS_E  linea_attivita%Rowtype;
aCdCdrE              VARCHAR2(30);
aCdLaE               VARCHAR2(10);

aGAE_dedicata_CDS_S  linea_attivita%Rowtype;
aCdCdrS              VARCHAR2(30);
aCdLaS               VARCHAR2(10);

aEVSPE               elemento_voce%Rowtype;
aVoceFSPE            voce_f%Rowtype;

aEVENT               elemento_voce%Rowtype;
aVoceFENT            voce_f%Rowtype;

aCDS                 unita_organizzativa%Rowtype;
aCDSOrigine          unita_organizzativa%Rowtype;

aAssDocPGiro         ass_obb_acr_pgiro%Rowtype;
evNewS               elemento_voce%rowtype;
evNewE               elemento_voce%rowtype;
recParametriCNR      PARAMETRI_CNR%Rowtype;
Begin
recParametriCNR := CNRUTL001.getRecParametriCnr(aObb.esercizio+1);
--------------------------------------------------------------------------------------------------------------
---------------------------------------     CONTROLLI  PRELIMINARI       -------------------------------------
--------------------------------------------------------------------------------------------------------------

-- PER LA SPESA

CNRCTB048.checkEsercizio(aObb.esercizio, aObb.cd_cds);

If aObb.fl_pgiro is null or aObb.fl_pgiro <> 'Y' then
   IBMERR001.RAISE_ERR_GENERICO('Impegno non su partita di giro.');
End if;

If aObb.cd_tipo_documento_cont Is Null Or
                aObb.cd_tipo_documento_cont Not In (CNRCTB018.TI_DOC_OBB_PGIRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES) Then
   IBMERR001.RAISE_ERR_GENERICO('Non si tratta di un ribaltamento di Impegno per partita di giro.');
End if;

Begin
   Select * into aEVSpe
   From   elemento_voce
   Where  esercizio = aObb.esercizio And
          ti_gestione = aObb.ti_gestione And
          ti_appartenenza = aObb.ti_appartenenza And
          cd_elemento_voce = aObb.cd_elemento_voce And
          fl_partita_giro = 'Y';
Exception
  When NO_DATA_FOUND Then
        IBMERR001.RAISE_ERR_GENERICO('Voce del piano '||aObb.cd_elemento_voce||' non trovata o non di tipo "partita di giro".');
End;

If recParametriCNR.fl_nuovo_pdg='N' Then
  Begin
     Select * into aVoceFSPE
     From   voce_f
     Where  esercizio = aEVSpe.esercizio And
            ti_gestione = aEVSpe.ti_gestione And
            ti_appartenenza = aEVSpe.ti_appartenenza And
            cd_voce = aObb.cd_elemento_voce And
            fl_mastrino = 'Y';
  Exception
    When NO_DATA_FOUND Then
          IBMERR001.RAISE_ERR_GENERICO('Conto finanziario '||aObb.cd_elemento_voce||' non trovato.');
  End;

  -- verifico congruenza tra ti_appartenenza e tipo CDS

  aCDS := CNRCTB020.GETCDSVALIDO(aObb.esercizio, aObb.cd_cds);
  aCDSOrigine := CNRCTB020.GETCDSVALIDO(aObb.esercizio, aObb.cd_cds_origine);

  If aCDS.cd_tipo_unita = CNRCTB020.TIPO_ENTE and aEVSpe.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS then
     IBMERR001.RAISE_ERR_GENERICO('La voce del piano specificata non Ã¨ una voce del piano dell''ENTE.');
  End if;

  If aCDS.cd_tipo_unita <> CNRCTB020.TIPO_ENTE and aEVSpe.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR then
     IBMERR001.RAISE_ERR_GENERICO('La voce del piano specificata non Ã¨ una voce del piano di CDS.');
  End if;
End If;

-- 22.12.2008 eliminato controllo per anomalia su ribaltamenti

--If aObb.esercizio < aObb.esercizio_competenza then
--   IBMERR001.RAISE_ERR_GENERICO('4. Generazione automatica di '||cnrutil.getLabelObbligazioneMin()||' in esercizi futuri non supportata !');
--End if;


-- PER L'ENTRATA

CNRCTB048.checkEsercizio(aAcc.esercizio, aAcc.cd_cds);

If aAcc.fl_pgiro is null Or aAcc.fl_pgiro <> 'Y' Then
   IBMERR001.RAISE_ERR_GENERICO('Accertamento non su partita di giro.');
End If;

If aAcc.cd_tipo_documento_cont is null Or
           aAcc.cd_tipo_documento_cont Not In (CNRCTB018.TI_DOC_ACC_PGIRO, CNRCTB018.TI_DOC_ACC_PGIRO_RES) Then
        IBMERR001.RAISE_ERR_GENERICO ('Non si tratta di un ribaltamento di Accertamento per partita di giro.');
End If;

Begin
   Select * into aEVENT
   From   elemento_voce
   Where  esercizio = aAcc.esercizio And
          ti_gestione = aAcc.ti_gestione And
          ti_appartenenza = aAcc.ti_appartenenza And
          cd_elemento_voce = aAcc.cd_elemento_voce And
          fl_partita_giro = 'Y';
Exception
  When NO_DATA_FOUND Then
        IBMERR001.RAISE_ERR_GENERICO('Voce del piano '||aAcc.cd_elemento_voce||' non trovata o non di tipo "partita di giro".');
End;

If recParametriCNR.fl_nuovo_pdg='N' Then
  Begin
    Select * into aVoceFENT
    From   voce_f
    Where  esercizio = aEVEnt.esercizio And
           ti_gestione = aEVEnt.ti_gestione And
           ti_appartenenza = aEVEnt.ti_appartenenza And
           cd_voce = aAcc.cd_voce And
           fl_mastrino = 'Y';
  Exception
    When NO_DATA_FOUND Then
        IBMERR001.RAISE_ERR_GENERICO('Conto finanziario '||aAcc.cd_elemento_voce||' non trovato.');
  End;

  aCDS        := CNRCTB020.GETCDSVALIDO(aAcc.esercizio, aAcc.cd_cds);
  aCDSOrigine := CNRCTB020.GETCDSVALIDO(aAcc.esercizio, aAcc.cd_cds_origine);

  If aCDS.cd_tipo_unita = CNRCTB020.TIPO_ENTE and aEVEnt.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS then
    IBMERR001.RAISE_ERR_GENERICO('La voce del piano specificata non Ã¨ una voce del piano dell''ENTE.');
  End If;

  If aCDS.cd_tipo_unita <> CNRCTB020.TIPO_ENTE and aEVEnt.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR then
    IBMERR001.RAISE_ERR_GENERICO('La voce del piano specificata non Ã¨ una voce del piano di CDS.');
  End If;
End If;

-- 22.12.2008 eliminato controllo per anomalia su ribaltamento

--If aAcc.esercizio != aAcc.esercizio_competenza Then
--   IBMERR001.RAISE_ERR_GENERICO('Generazione automatica di accertamento in esercizi futuri non supportata !');
--End if;

---------------------------- FINE CONTROLLI, INIZIO RIBALTAMENTO VERO E PROPRIO -------------------------------

-- Recupero il riscontrato dell'obbligazione per vedere se deve essere tronca oppure no
  aImRiscontratoSpe := getIm_riscontrato (aObb.esercizio, aObb.cd_cds, aObb.esercizio_originale, aObb.pg_obbligazione,
                                          aObb.ti_gestione);

-- Ribaltamento dell'OBBLIGAZIONE in nuovo esercizio

  aObbNew     := aObb;

  aObbNew.esercizio              := aObb.esercizio+1;
  aObbNew.esercizio_originale    := aObb.esercizio_originale;
  aObbNew.esercizio_competenza   := aObbNew.esercizio;
  aObbNew.dt_registrazione       := to_date('0101'||aObbNew.esercizio,'DDMMYYYY');
  aObbNew.CD_TIPO_DOCUMENTO_CONT := cnrctb018.TI_DOC_OBB_PGIRO_RES; -- part giro res

  -- se Ã¨ riscontrato o se era giÃ  tronca (cancellata) allora --> PARTITA DI GIRO TRONCA
  If aObb.dt_cancellazione is Not null Or aImRiscontratoSpe != 0 Or
      /* new ! */ CNRCTB048.getImNonPagatoRiscosso(aObb.cd_cds, aObb.esercizio, aObb.esercizio_originale, aObb.pg_obbligazione, aObb.ti_gestione) = 0 Then
    aObbNew.im_obbligazione  := 0;
    aObbNew.dt_cancellazione := to_date('0101'||aObbNew.esercizio, 'DDMMYYYY');
    aObbNew.stato_obbligazione := 'S';
  Else
    aObbNew.im_obbligazione := CNRCTB048.getImNonPagatoRiscosso(aObb.cd_cds, aObb.esercizio, aObb.esercizio_originale, aObb.pg_obbligazione, aObb.ti_gestione);
  End If;

  -- SE E' STATA PASSATA LA VOCE DI SPESA LA METTO: cambio imputazione finanziaria
  --                                               (SENZA CONTROLLARE CHE SIA DI P DI GIRO ??? MAH !)

  if aElementoVoceS.esercizio is not null then
     aObbNew.TI_APPARTENENZA	:= aElementoVoceS.TI_APPARTENENZA;
     aObbNew.TI_GESTIONE	:= aElementoVoceS.TI_GESTIONE;
     aObbNew.CD_ELEMENTO_VOCE	:= aElementoVoceS.CD_ELEMENTO_VOCE;
  Else
     evNewS := getElementoVoceNew(aObb);
     aObbNew.TI_APPARTENENZA  := evNewS.TI_APPARTENENZA;
     aObbNew.TI_GESTIONE  := evNewS.TI_GESTIONE;
     aObbNew.CD_ELEMENTO_VOCE := evNewS.CD_ELEMENTO_VOCE;
  end if;

  aObbNew.dacr            := aTSNow;
  aObbNew.utcr            := aUser;
  aObbNew.duva            := aTSNow;
  aObbNew.utuv            := aUser;
  aObbNew.pg_ver_rec      := CNRCTB048.getPgVerRec(aObbNew);

-- 22.01.2008
  aObbNew.cd_cds_ori_riporto          := aObb.cd_cds;
  aObbNew.esercizio_ori_riporto       := aObb.esercizio;
  aObbNew.esercizio_ori_ori_riporto   := aObb.esercizio_originale;
  aObbNew.pg_obbligazione_ori_riporto := aObb.pg_obbligazione;

  CNRCTB035.INS_OBBLIGAZIONE(aObbNew);

  aObbScadNew             := aObbScad;   -- TRAVASO LA SCADENZA

  aObbScadNew.esercizio                   := aObbNew.esercizio;
  aObbScadNew.cd_cds                      := aObbNew.cd_cds;
  aObbScadNew.esercizio                   := aObbNew.esercizio;
  aObbScadNew.esercizio_originale         := aObbNew.esercizio_originale;
  aObbScadNew.pg_obbligazione             := aObbNew.pg_obbligazione;
  aObbScadNew.pg_obbligazione_scadenzario := 1;
  aObbScadNew.dt_scadenza                 := TRUNC(nvl(CNRCTB048.GETDTSCADENZA(aObbNew.esercizio, aObbScad.dt_scadenza), aObb.dt_registrazione));
  aObbScadNew.ds_scadenza                 := aObbNew.ds_obbligazione;

  -- come per le obbligazioni
  aObbScadNew.im_scadenza                 := aObbScad.im_scadenza - aObbScad.im_associato_doc_contabile;
  aObbScadNew.im_associato_doc_amm        := aObbScad.im_associato_doc_amm - aObbScad.im_associato_doc_contabile;
  aObbScadNew.im_associato_doc_contabile  := 0;

  aObbScadNew.dacr                        := aObbNew.dacr;
  aObbScadNew.utcr                        := aObbNew.utcr;
  aObbScadNew.duva                        := aObbNew.duva;
  aObbScadNew.utuv                        := aObbNew.utuv;
  aObbScadNew.pg_ver_rec                  := CNRCTB048.getPgVerRec(aObbNew);

  aObbScadNew.pg_obbl_scad_ori_riporto    := 1;

  If CNRCTB015.UtilizzaGAEdedicataPgiroCDS (aObb.esercizio, aObb.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_SPESE) Then
    aGAE_dedicata_CDS_S := CNRCTB015.get_LINEA_PGIRO_cds(aObb.esercizio, aObb.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_SPESE);
    aCdCdrS := aGAE_dedicata_CDS_S.CD_CENTRO_RESPONSABILITA;
    aCdLaS  := aGAE_dedicata_CDS_S.CD_LINEA_ATTIVITA;
  Else
    aCdCdrS := CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_SPESA_ENTE);
    aCdLaS := CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_SPESA_ENTE);
  End If;

  aDettScadCObb.cd_cds                      := aObbNew.cd_cds;
  aDettScadCObb.esercizio                   := aObbNew.esercizio;
  aDettScadCObb.esercizio_originale         := aObbNew.esercizio_originale;
  aDettScadCObb.pg_obbligazione             := aObbNew.pg_obbligazione;
  aDettScadCObb.pg_obbligazione_scadenzario := 1;
  aDettScadCObb.ti_appartenenza             := aObbNew.ti_appartenenza;
  aDettScadCObb.ti_gestione                 := aObbNew.ti_gestione;
  aDettScadCObb.cd_voce                     := aObbNew.cd_elemento_voce;
  aDettScadCObb.cd_centro_responsabilita    := aCdCdrS;
  aDettScadCObb.cd_linea_attivita           := aCdLaS;
  aDettScadCObb.im_voce                     := aObbNew.im_obbligazione;
  aDettScadCObb.cd_fondo_ricerca            := null;
  aDettScadCObb.dacr                        := aObbNew.dacr;
  aDettScadCObb.utcr                        := aObbNew.utcr;
  aDettScadCObb.duva                        := aObbNew.duva;
  aDettScadCObb.utuv                        := aObbNew.utuv;
  aDettScadCObb.pg_ver_rec                  := CNRCTB048.getPgVerRec(aObbNew);

  aDettScadCObbList(1)                      := aDettScadCObb;

Dbms_Output.PUT_LINE ('CREA SCADENZA OBB '||aDettScadCObb.cd_cds||' '||aDettScadCObb.esercizio||' '||aDettScadCObb.esercizio_originale||' '||
aDettScadCObb.pg_obbligazione||' '||aDettScadCObb.pg_obbligazione_scadenzario);

  CNRCTB035.creaScadObbligazione(aObbNew, aObbScadNew, 1, aDettScadCObbList, false);

------------------------------------ RIBALTAMENTO DELL'ACCERTAMENTO --------------------------------

  -- recupero il riscontrato dell'accertamento collegato per vedere se deve essere tronca oppure no
  aImRiscontratoEnt := getIm_riscontrato (aAcc.esercizio, aAcc.cd_cds, aAcc.esercizio_originale, aAcc.pg_accertamento,
                                          aAcc.ti_gestione);

Dbms_Output.put_line ('riscontrato entrata '||aImRiscontratoEnt||' e data cancellazione '||To_Char(aAcc.dt_cancellazione));

  -- travaso prima il record
  aAccNew     := aAcc;

  -- e poi faccio le modifiche ai singoli campi

  aAccNew.esercizio              := aAcc.esercizio+1;
  aAccNew.esercizio_originale    := aAcc.esercizio_originale;
  aAccNew.dt_registrazione       := to_date('0101'||aAccNew.esercizio,'DDMMYYYY');
  aAccNew.CD_TIPO_DOCUMENTO_CONT := cnrctb018.TI_DOC_ACC_PGIRO_RES; -- part giro res

  -- se Ã¨ riscontrato o se era giÃ  tronca (cancellata) allora --> PARTITA DI GIRO TRONCA
  If aAcc.dt_cancellazione is Not null Or aImRiscontratoEnt != 0 Or
      /* new ! */ CNRCTB048.getImNonPagatoRiscosso(aAcc.cd_cds, aAcc.esercizio, aAcc.esercizio_originale, aAcc.pg_accertamento, aAcc.ti_gestione) = 0 Then
    aAccNew.im_accertamento := 0;
    aAccNew.dt_cancellazione := to_date('0101'||aAccNew.esercizio,'DDMMYYYY');
  Else
    aAccNew.im_accertamento := CNRCTB048.getImNonPagatoRiscosso(aAcc.cd_cds, aAcc.esercizio, aAcc.esercizio_originale, aAcc.pg_accertamento, aAcc.ti_gestione);
  End If;

  If aElementoVoceE.esercizio is not null then
  -- cambio imputazione finanziaria
    aAccNew.TI_APPARTENENZA	:= aElementoVoceE.TI_APPARTENENZA;
    aAccNew.TI_GESTIONE	:= aElementoVoceE.TI_GESTIONE;
    aAccNew.CD_ELEMENTO_VOCE	:= aElementoVoceE.CD_ELEMENTO_VOCE;
  Else
    evNewE := getElementoVoceNew(aAcc);
    aAccNew.TI_APPARTENENZA  := evNewE.TI_APPARTENENZA;
    aAccNew.TI_GESTIONE  := evNewE.TI_GESTIONE;
    aAccNew.CD_ELEMENTO_VOCE := evNewE.CD_ELEMENTO_VOCE;
  End If;

  If recParametriCNR.fl_nuovo_pdg='Y' Then
    aAccNew.CD_VOCE := aAccNew.CD_ELEMENTO_VOCE;
  End if;

-- 22.01.2008
  aAccNew.cd_cds_ori_riporto          := aAcc.cd_cds;
  aAccNew.esercizio_ori_riporto       := aAcc.esercizio;
  aAccNew.esercizio_ori_ori_riporto   := aAcc.esercizio_originale;
  aAccNew.pg_accertamento_ori_riporto := aAcc.pg_accertamento;

  aAccNew.dacr            := aTSNow;
  aAccNew.utcr            := aUser;
  aAccNew.duva            := aTSNow;   --aAcc.dacr;
  aAccNew.utuv            := aUser;    --aAcc.utcr;
  aAccNew.pg_ver_rec      := CNRCTB048.getPgVerRec(aAccNew);

  dbms_output.put_line('Pippo '||aAccNew.ESERCIZIO||'/'||aAccNew.TI_APPARTENENZA||'/'||aAccNew.TI_GESTIONE||'/'||aAccNew.cd_elemento_voce);
  CNRCTB035.INS_ACCERTAMENTO(aAccNew);
dbms_output.put_line('POSTPippo ');
  aAccScadNew := aAccScad;   -- tutto il record

  aAccScadNew.esercizio                     := aAccNew.esercizio;
  aAccScadNew.cd_cds                        := aAccNew.cd_cds;
  aAccScadNew.esercizio                     := aAccNew.esercizio;
  aAccScadNew.esercizio_originale           := aAccNew.esercizio_originale;
  aAccScadNew.pg_accertamento               := aAccNew.pg_accertamento;
  aAccScadNew.pg_accertamento_scadenzario   := 1;
  aAccScadNew.dt_scadenza_emissione_fattura := Trunc(nvl(CNRCTB048.GETDTSCADENZA(aAccNew.esercizio, aAccScadNew.dt_scadenza_emissione_fattura), aAcc.dt_registrazione));
  aAccScadNew.dt_scadenza_incasso           := Trunc(nvl(CNRCTB048.GETDTSCADENZA(aAccNew.esercizio, aAccScadNew.dt_scadenza_incasso), aAcc.dt_registrazione));
  aAccScadNew.ds_scadenza                   := aAccNew.ds_accertamento;

  -- come per gli accertamenti normali
  aAccScadNew.im_scadenza                   := aAccScad.im_scadenza - aAccScad.im_associato_doc_contabile;
  aAccScadNew.im_associato_doc_amm          := aAccScad.im_associato_doc_amm - aAccScad.im_associato_doc_contabile;

  aAccScadNew.im_associato_doc_contabile    := 0;
  aAccScadNew.dacr                          := aAccNew.dacr;
  aAccScadNew.utcr                          := aAccNew.utcr;
  aAccScadNew.duva                          := aAccNew.duva;
  aAccScadNew.utuv                          := aAccNew.utuv;
  aAccScadNew.pg_ver_rec                    := CNRCTB048.getPgVerRec(aAccNew);

-- 22.01.2008
  aAccScadNew.pg_acc_scad_ori_riporto := 1;

  CNRCTB035.aggiornaSaldoDettScad(aAccNew,aAccNew.im_accertamento,false,aAccNew.utcr,aAccNew.dacr);

  If CNRCTB015.UtilizzaGAEdedicataPgiroCDS (aAccNew.esercizio, aAccNew.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_ENTRATE) Then
    aGAE_dedicata_CDS_E := CNRCTB015.get_LINEA_PGIRO_cds(aAccNew.esercizio, aAccNew.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_ENTRATE);
    aCdCdrE := aGAE_dedicata_CDS_E.CD_CENTRO_RESPONSABILITA;
    aCdLaE  := aGAE_dedicata_CDS_E.CD_LINEA_ATTIVITA;
  Else
    aCdCdrE :=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
    aCdLaE  :=CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
  End If;

  aDettScadCAcc.cd_cds                      := aAccNew.cd_cds;
  aDettScadCAcc.esercizio                   := aAccNew.esercizio;
  aDettScadCAcc.esercizio_originale         := aAccNew.esercizio_originale;
  aDettScadCAcc.pg_accertamento             := aAccNew.pg_accertamento;
  aDettScadCAcc.pg_accertamento_scadenzario := 1;
  aDettScadCAcc.cd_centro_responsabilita    := aCdCdrE;
  aDettScadCAcc.cd_linea_attivita           := aCdLaE;
  aDettScadCAcc.im_voce                     := aAccNew.im_accertamento; -- se tronca e' automatico a zero
  aDettScadCAcc.dacr                        := aAccNew.dacr;
  aDettScadCAcc.utcr                        := aAccNew.utcr;
  aDettScadCAcc.duva                        := aAccNew.duva;
  aDettScadCAcc.utuv                        := aAccNew.utuv;
  aDettScadCAcc.pg_ver_rec                  := CNRCTB048.getPgVerRec(aAccNew);

  aDettScadCAccList(1)                      := aDettScadCAcc;
  -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  CNRCTB035.creaScadAccertamento(aAccNew, aAccScadNew, 1, aDettScadCAccList);

----------------------- FINE RIBALTAMENTO DELL'ACCERTAMENTO, RIBALTO LE ASSOCIAZIONI -------------------------

  aAssDocPGiro.cd_cds                     := aAccNew.cd_cds;
  aAssDocPGiro.esercizio                  := aAccNew.esercizio;
  aAssDocPGiro.esercizio_ori_accertamento := aAccNew.esercizio_originale;
  aAssDocPGiro.pg_accertamento            := aAccNew.pg_accertamento;
  aAssDocPGiro.esercizio_ori_obbligazione := aObbNew.esercizio_originale;
  aAssDocPGiro.pg_obbligazione            := aObbNew.pg_obbligazione;
  aAssDocPGiro.ti_origine                 := aTi_Origine;
  aAssDocPGiro.dacr                       := aAccNew.dacr;
  aAssDocPGiro.utcr                       := aAccNew.utcr;
  aAssDocPGiro.duva                       := aAccNEw.duva;
  aAssDocPGiro.utuv                       := aAccNew.utuv;
  aAssDocPGiro.pg_ver_rec                 := 1;

  CNRCTB035.INS_ASS_OBB_ACR_PGIRO(aAssDocPGiro);

-- 15.05.2006 stani: anche le ribaltate tronche devono riportare nel documento a zero la chiave dell'originale
-- 22.01.2008 NON CON LE PROCEDURE TRADIZIONALI ALTRIMENTI POPOLA GLI STORICI

--aggiornaConnPgiroCds(aObb, aObbNew);
--aggiornaConnPgiroCds(aAcc, aAccNew);

  Update obbligazione
  Set    riportato = 'Y',
         pg_ver_rec=pg_ver_rec + 1
  Where  cd_cds              = aObb.cd_cds And
         esercizio           = aObb.esercizio And
         esercizio_originale = aObb.esercizio_originale And
         pg_obbligazione     = aObb.pg_obbligazione;

  Update accertamento
  Set    riportato = 'Y',
         pg_ver_rec          = pg_ver_rec + 1
  Where  cd_cds              = aAcc.cd_cds And
         esercizio           = aAcc.esercizio And
         esercizio_originale = aAcc.esercizio_originale And
         pg_accertamento     = aAcc.pg_accertamento;

-- FINE MODIFICHE 22.01.2008


-- se la partita di giro di entrata che si ribalta si porta dietro documenti amministrativi
-- allora sposto sul documento amministrativo la nuova chiave dell'accertamento

If aAccScadNew.im_associato_doc_amm > 0 then
    CNRCTB048.aggiornaDocAmm(aAccScad, aAccScadNew, aUser, aTSNow);
End If;

-- se la partita di giro di spesa che si ribalta si porta dietro documenti amministrativi
-- allora sposto sul documento amministrativo la nuova chiave dell'obbligazione

If aObbScadNew.im_associato_doc_amm > 0 then
    CNRCTB048.aggiornaDocAmm(aObbScad, aObbScadNew, aUser, aTSNow);
End if;

End;

 procedure ripPgiroCdsInt(
  aObb IN OUT obbligazione%rowtype,
  aObbNew IN OUT obbligazione%rowtype,
  aElementoVoce elemento_voce%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aObbScadNew obbligazione_scadenzario%rowtype;
  aAccNew accertamento%rowtype;
  aAccScadNew accertamento_scadenzario%rowtype;
  aImRiscontrato number;

 Begin

 Dbms_Output.put_line ('ripPgiroCdsInt 1');

  CNRCTB048.checkEsercizio(aObb.esercizio, aObb.cd_cds);

  CNRCTB035.getPgiroCds(aObb,aObbScad,aObbScadVoce, aAcc,aAccScad,aAccScadVoce);

  -- Crea (ribalta) obbligazione in nuovo esercizio

  aObbNew:=aObb;
  aObbScadNew:=aObbScad;
  aObbNew.esercizio:=aObb.esercizio+1;
  aObbNew.esercizio_competenza := aObbNew.esercizio;
  aObbNew.dt_registrazione := to_date('0101'||aObbNew.esercizio,'DDMMYYYY');
  /* 05.01.2006 stani */
  aObbNew.esercizio_originale    := aObb.esercizio_originale;
  aObbNew.CD_TIPO_DOCUMENTO_CONT := cnrctb018.TI_DOC_OBB_PGIRO_RES; -- part giro res

  if aElementoVoce.esercizio is not null then
  -- cambio imputazione finanziaria
     aObbNew.TI_APPARTENENZA	:= aElementoVoce.TI_APPARTENENZA;
     aObbNew.TI_GESTIONE	:= aElementoVoce.TI_GESTIONE;
     aObbNew.CD_ELEMENTO_VOCE	:= aElementoVoce.CD_ELEMENTO_VOCE;
  end if;

  aObbNew.dacr := aTSNow;
  aObbNew.utcr := aUser;
  aObbScadNew.esercizio := aObbNew.esercizio;

  -- partita di giro tronca se tronca in origine, oppure controparte riscontrata
  aImRiscontrato := getIm_riscontrato(aAcc.esercizio,aAcc.cd_cds,aAcc.esercizio_originale,aAcc.pg_accertamento,aAcc.ti_gestione);


if (aAcc.dt_cancellazione is null and aImRiscontrato = 0) then

 Dbms_Output.put_line ('ripPgiroCdsInt 2 chiamata CNRCTB030.creaObbligazionePgiro');

   CNRCTB030.creaObbligazionePgiro(false,aObbNew,aObbScadNew,aAccNew,aAccScadNew,CNRCTB048.GETDTSCADENZA(aObbNew.esercizio,aObbScad.dt_scadenza));

-- quando gli ritorna l'accertamento valorizza l'importo associato a documenti amministrativi
   if aAccScad.im_associato_doc_amm > 0 then
	  update accertamento_scadenzario
	  set    im_associato_doc_amm = aAccScad.im_associato_doc_amm,
		 pg_ver_rec = pg_ver_rec + 1
	  where  cd_cds = aAccScadNew.cd_cds And
	         esercizio = aAccScadNew.esercizio And
	         esercizio_originale = aAccScadNew.esercizio_originale And
	         pg_accertamento = aAccScadNew.pg_accertamento And
	         pg_accertamento_scadenzario = aAccScadNew.pg_accertamento_scadenzario;
      CNRCTB048.aggiornaDocAmm(aAccScad,aAccScadNew,aUser,aTSNow);
   end if;

else

Dbms_Output.put_line ('ripPgiroCdsInt 3 chiamata CNRCTB030.creaObbligazionePgiroTronc');

  CNRCTB030.creaObbligazionePgiroTronc(false,aObbNew,aObbScadNew,aAccNew,aAccScadNew,CNRCTB048.GETDTSCADENZA(aObbNew.esercizio,aObbScad.dt_scadenza));

end if;

  aggiornaConnPgiroCds(aObb,aObbNew);
  -- 15.05.2006 stani: anche le ribaltate tronche devono riportare nel documento a zero la chiave dell'originale

  aggiornaConnPgiroCds(aAcc,aAccNew);

  if aImRiscontrato = 0 then -- escludo pgiro ricreate tronche
     aggiornaConnPgiroCds(aAcc,aAccNew);
  end if;

  if aObbScad.im_associato_doc_amm > 0 then
	update obbligazione_scadenzario
	set im_associato_doc_amm = aObbScad.im_associato_doc_amm,
	    pg_ver_rec = pg_ver_rec + 1
	where cd_cds = aObbScadNew.cd_cds
	  and esercizio = aObbScadNew.esercizio
	  and esercizio_originale = aObbScadNew.esercizio_originale
	  and pg_obbligazione = aObbScadNew.pg_obbligazione
	  and pg_obbligazione_scadenzario = aObbScadNew.pg_obbligazione_scadenzario;
    CNRCTB048.aggiornaDocAmm(aObbScad,aObbScadNew,aUser,aTSNow);
  end if;
End;

Procedure ripPgiroCdsInt(
  aAcc IN OUT accertamento%rowtype,
  aObbNew IN OUT obbligazione%rowtype,
  aElementoVoce elemento_voce%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
  aObb          obbligazione%rowtype;
  aObbScad      obbligazione_scadenzario%rowtype;
  aObbScadVoce  obbligazione_scad_voce%rowtype;
  aAccScad      accertamento_scadenzario%rowtype;
  aAccScadVoce  accertamento_scad_voce%rowtype;
  aObbScadNew   obbligazione_scadenzario%rowtype;
  aAccNew       accertamento%rowtype;
  aAccScadNew   accertamento_scadenzario%rowtype;

 begin
  CNRCTB048.checkEsercizio(aAcc.esercizio,aAcc.cd_cds);
  CNRCTB035.getPgiroCds(aAcc, aAccScad, aAccScadVoce, aObb, aObbScad, aObbScadVoce);

  aObbNew                       := aObb;
  aObbScadNew                   := aObbScad;

  aObbNew.esercizio             := aObb.esercizio+1;
  aObbNew.esercizio_competenza  := aObbNew.esercizio;

  aObbNew.dt_registrazione := to_date('0101'||aObbNew.esercizio,'DDMMYYYY');

  /* 05.01.2006 stani */
  aObbNew.esercizio_originale    := aObb.esercizio_originale;
  aObbNew.CD_TIPO_DOCUMENTO_CONT := cnrctb018.TI_DOC_OBB_PGIRO_RES; -- part giro res

  if aElementoVoce.esercizio is not null then
  -- cambio imputazione finanziaria
	  aObbNew.TI_APPARTENENZA		  	  :=aElementoVoce.TI_APPARTENENZA;
	  aObbNew.TI_GESTIONE			  	  :=aElementoVoce.TI_GESTIONE;
	  aObbNew.CD_ELEMENTO_VOCE		  	  :=aElementoVoce.CD_ELEMENTO_VOCE;
  end if;

  aObbNew.dacr := aTSNow;
  aObbNew.utcr := aUser;
  aObbScadNew.esercizio:=aObbNew.esercizio;

  CNRCTB030.creaObbligazionePgiroTronc(false,aObbNew,aObbScadNew,aAccNew,aAccScadNew,CNRCTB048.GETDTSCADENZA(aObbNew.esercizio,aObbScad.dt_scadenza));

  aggiornaConnPgiroCds(aObb,aObbNew);
  -- 15.05.2006 stani: anche le ribaltate tronche devono riportare nel documento a zero la chiave dell'originale
  aggiornaConnPgiroCds(aAcc,aAccNew);

  if aObbScad.im_associato_doc_amm > 0 then
--    aObbScadNew.im_associato_doc_amm := aObbScad.im_associato_doc_amm;
	update obbligazione_scadenzario
	set im_associato_doc_amm = aObbScad.im_associato_doc_amm,
	    pg_ver_rec = pg_ver_rec + 1
	where cd_cds = aObbScadNew.cd_cds
	  and esercizio = aObbScadNew.esercizio
	  and esercizio_originale = aObbScadNew.esercizio_originale
	  and pg_obbligazione = aObbScadNew.pg_obbligazione
	  and pg_obbligazione_scadenzario = aObbScadNew.pg_obbligazione_scadenzario;
    CNRCTB048.aggiornaDocAmm(aObbScad,aObbScadNew,aUser,aTSNow);
  end if;
--   if aAccScad.im_associato_doc_amm > 0 then
--     CNRCTB048.aggiornaDocAmm(aAccScad,aAccScadNew,aUser,aTSNow);
--   end if;
 end;

 procedure deriportoEsNextDocAmm(aEsScrivania number, aTiDoc varchar2, aCdCdsDoc varchar2, aEsDoc number, aCdUoDoc varchar2, aPgDoc number, aUser varchar2) is
  aObb obbligazione%rowtype;
  aAcc accertamento%rowtype;
  aTSNow date;
  isFoundSpesa boolean;
  aObbPrev obbligazione%rowtype;
  aAccPrev accertamento%rowtype;
  contatore number := 0;
 begin
  aTSNow:=sysdate;
  isFoundSpesa:=false;
  for aDoc in (select distinct cd_cds_obbligazione, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione from V_DOC_AMM_OBB d
				    where
					      cd_tipo_documento_amm = aTiDoc
					  and cd_cds	= aCdCdsDoc
					  and esercizio	= aEsDoc
					  and cd_unita_organizzativa = aCdUoDoc
					  and pg_documento_amm = aPgDoc
					  and esercizio_obbligazione > aEsDoc
  ) loop
   contatore := contatore + 1;
   isFoundSpesa:=true;
   if aDoc.esercizio_obbligazione = aEsScrivania then
    IBMERR001.RAISE_ERR_GENERICO('Esercizio di scrivania non valido per questa operazione');
   end if;
   if aDoc.esercizio_obbligazione - 1 <> aEsScrivania then
    IBMERR001.RAISE_ERR_GENERICO('Esercizio di effettuazione operazione e quello del documento contabile non sono compatibili');
   end if;

   aObb:=null;
   aObb.cd_cds:=aDoc.cd_cds_obbligazione;
   aObb.esercizio:=aDoc.esercizio_obbligazione;
   aObb.esercizio_originale:=aDoc.esercizio_ori_obbligazione;
   aObb.pg_obbligazione:=aDoc.pg_obbligazione;
   cnrctb035.LOCKDOCFULL(aObb);
   aObbPrev := CNRCTB048.GETDOCORIGINE(aObb);

   deriportoEsNextObb(aObbPrev, aUser, aTSNow);

  end loop;
  if not isFoundSpesa then
   for aDoc in (select distinct cd_cds_accertamento, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento from V_DOC_AMM_ACC d
				    where
					      cd_tipo_documento_amm = aTiDoc
					  and cd_cds	= aCdCdsDoc
					  and esercizio	= aEsDoc
					  and cd_unita_organizzativa = aCdUoDoc
					  and pg_documento_amm = aPgDoc
					  and esercizio_accertamento > aEsDoc
   ) loop
    contatore := contatore + 1;
    if aDoc.esercizio_accertamento = aEsScrivania then
     IBMERR001.RAISE_ERR_GENERICO('Esercizio di scrivania non valido per questa operazione');
    end if;
    if aDoc.esercizio_accertamento - 1 <> aEsScrivania then
     IBMERR001.RAISE_ERR_GENERICO('Esercizio di effettuazione operazione e quello del documento contabile non sono compatibili');
    end if;
    isFoundSpesa:=true;
    aAcc:=null;
    aAcc.cd_cds:=aDoc.cd_cds_accertamento;
    aAcc.esercizio:=aDoc.esercizio_accertamento;
    aAcc.esercizio_originale:=aDoc.esercizio_ori_accertamento;
    aAcc.pg_accertamento:=aDoc.pg_accertamento;
	cnrctb035.LOCKDOCFULL(aAcc);
	aAccPrev := CNRCTB048.GETDOCORIGINE(aAcc);
    deriportoEsNextAcc(aAccPrev, aUser, aTSNow);
   end loop;
  end if;
  if contatore = 0 then
  	 ibmerr001.RAISE_ERR_GENERICO('Documenti contabili non riportabili all''esercizio precedente');
  end if;
 end;


 procedure riportoEsNextDocAmm(aEsScrivania number, aTiDoc varchar2, aCdCdsDoc varchar2,
                               aEsDoc number, aCdUoDoc varchar2, aPgDoc number, aUser varchar2) is
  aObb obbligazione%rowtype;
  aAcc accertamento%rowtype;
  aTSNow date;
  isFoundSpesa boolean;
  contatore number := 0;
 begin
  aTSNow:=sysdate;

-- Rospuc 09/03/2017 sospesa contabilizzazione


--   Se il documento viene riportato nel suo esercizio di competenza (esDoc = esScriv) allora
--   deve essere processato in economica (il client si preoccupa di mettere lo stato da riprocessare)
--   Se invece il riporto avviene in esercizi successivi a quello di origine del documento,
--   allora l'economica Ã¨ giÃ  chiusa quindi il documento non puÃ² essere riprocessato e deve essere riportato
--   il suo stato a "contabilizzato"
  if (aEsScrivania = aEsDoc) then
	  -- contabilizzazione in economica del doc amm
	  CNRCTB205.regDocAmmCOGE(aEsDoc, aCdCdsDoc, aCdUoDoc, aTiDoc, aPgDoc, aUser, aTSNow);

	  -- contabilizzazione in analitica del doc amm
	  CNRCTB210.regDocAmmCoan(aEsDoc, aCdCdsDoc, aCdUoDoc, aTiDoc, aPgDoc, aUser, aTSNow);
  else
    CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
				    aTiDoc,
				    aCdCdsDoc,
				    aEsDoc,
				    aCdUoDoc,
				    aPgDoc,
				    'stato_coge='''||CNRCTB100.STATO_COEP_CON||'''',
				    null);
	CNRCTB100.UPDATEDOCAMM_NODUVAUTUV(
				    aTiDoc,
				    aCdCdsDoc,
				    aEsDoc,
				    aCdUoDoc,
				    aPgDoc,
				    'stato_coan='''||CNRCTB100.STATO_COEP_CON||'''',
				    null);
  end if;
 -- fine Rospuc 09/03/2017

   isFoundSpesa:=false;
  for aDoc in (select distinct cd_cds_obbligazione, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione from V_DOC_AMM_OBB d
				    where
					      cd_tipo_documento_amm = aTiDoc
					  and cd_cds	= aCdCdsDoc
					  and esercizio	= aEsDoc
					  and cd_unita_organizzativa = aCdUoDoc
					  and pg_documento_amm = aPgDoc
					  and isEligibileRibalt( CNRCTB001.GESTIONE_SPESE,
						   					 cd_cds_obbligazione,
											 esercizio_obbligazione,
											 esercizio_ori_obbligazione,
											 pg_obbligazione) = 'Y'
  ) loop
   contatore := contatore + 1;
   isFoundSpesa:=true;

   if aDoc.esercizio_obbligazione + 1 = aEsScrivania then
    IBMERR001.RAISE_ERR_GENERICO('Esercizio di scrivania non valido per questa operazione');
   end if;
   if aDoc.esercizio_obbligazione <> aEsScrivania then
    IBMERR001.RAISE_ERR_GENERICO('Esercizio di effettuazione operazione e quello del documento contabile non sono compatibili');
   end if;

   aObb:=null;
   aObb.cd_cds:=aDoc.cd_cds_obbligazione;
   aObb.esercizio:=aDoc.esercizio_obbligazione;
   aObb.esercizio_originale:=aDoc.esercizio_ori_obbligazione;
   aObb.pg_obbligazione:=aDoc.pg_obbligazione;

   -- controllo lo stato dell'esercizio: per ribaltare doc amm, l'esercizio corrente ed il successivo devono essere aperti
   -- 1. verifico es corrente aperto e es successivo esistente (cond suff per riporto doc cont)
   CNRCTB048.CHECKESERCIZIO(aObb.esercizio,aObb.cd_cds);
   -- 2. verifico es successivo aperto (cond necessaria per riporto doc amm)
   if not CNRCTB008.ISESERCIZIOAPERTO(aObb.esercizio + 1, aObb.cd_cds) then
   	  ibmerr001.RAISE_ERR_GENERICO('L''esercizio '||(aObb.esercizio + 1)||' non Ã¨ aperto per il cds:'||aObb.cd_cds);
   end if;
   riportoEsNextObb(aObb, null, null,aUser, aTSNow);

  end loop;
  if not isFoundSpesa then
   for aDoc in (select distinct cd_cds_accertamento, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento from V_DOC_AMM_ACC d
				    where
					      cd_tipo_documento_amm = aTiDoc
					  and cd_cds	= aCdCdsDoc
					  and esercizio	= aEsDoc
					  and cd_unita_organizzativa = aCdUoDoc
					  and pg_documento_amm = aPgDoc
					  and isEligibileRibalt (CNRCTB001.GESTIONE_ENTRATE,
						   					 cd_cds_accertamento,
											 esercizio_accertamento,
											 esercizio_ori_accertamento,
											 pg_accertamento) = 'Y'
   ) loop
    contatore := contatore + 1;
    if aDoc.esercizio_accertamento + 1 = aEsScrivania then
     IBMERR001.RAISE_ERR_GENERICO('Esercizio di scrivania non valido per questa operazione');
    end if;
    if aDoc.esercizio_accertamento <> aEsScrivania then
     IBMERR001.RAISE_ERR_GENERICO('Esercizio di effettuazione operazione e quello del documento contabile non sono compatibili');
    end if;

    isFoundSpesa:=true;
    aAcc:=null;
    aAcc.cd_cds:=aDoc.cd_cds_accertamento;
    aAcc.esercizio:=aDoc.esercizio_accertamento;
    aAcc.esercizio_originale:=aDoc.esercizio_ori_accertamento;
    aAcc.pg_accertamento:=aDoc.pg_accertamento;

	-- controllo lo stato dell'esercizio: per ribaltare doc amm, l'esercizio corrente ed il successivo devono essere aperti
   	-- 1. verifico es corrente aperto e es successivo esistente (cond suff per riporto doc cont)
    CNRCTB048.CHECKESERCIZIO(aAcc.esercizio,aAcc.cd_cds);
    -- 2. verifico es successivo aperto (cond necessaria per riporto doc amm)
    if not CNRCTB008.ISESERCIZIOAPERTO(aAcc.esercizio + 1, aAcc.cd_cds) then
   	  ibmerr001.RAISE_ERR_GENERICO('L''esercizio '||(aAcc.esercizio + 1)||' non Ã¨ aperto per il cds:'||aAcc.cd_cds);
    end if;

    riportoEsNextAcc(aAcc, null,aUser, aTSNow);

   end loop;
  end if;
  if contatore = 0 then
  	 ibmerr001.RAISE_ERR_GENERICO('Documenti contabili non riportabili all''esercizio successivo');
  end if;
 end;


procedure riportoEsNextObbPGiro(aObb in out obbligazione%rowtype, aElementoVoce elemento_voce%rowtype,
                                aUser varchar2, aTSNow date) is
aObbNew obbligazione%rowtype;
aObbScad obbligazione_scadenzario%rowtype;
aObbScadVoce obbligazione_scad_voce%rowtype;
aAcc accertamento%rowtype;
aAccScad accertamento_scadenzario%rowtype;
aAccScadVoce accertamento_scad_voce%rowtype;
aprePGiro boolean;

begin
	-- verifico se l'elemento voce, se specificato, Ã¨ su pgiro
	if aElementoVoce.esercizio is not null and aElementoVoce.fl_partita_giro = 'N' then
	   ibmerr001.RAISE_ERR_GENERICO('L''elemento voce specificato non Ã¨ su partita di giro, impossibile ribaltare');
	end if;

	-- verifico se l'obbligazione ha aperto la partita di giro
	aprePGiro := CNRCTB035.ISAPREPGIRO(aObb);

	if aprePGiro then
Dbms_Output.put_line ('riportoEsNextObbPGiro 1');
	   CNRCTB035.getPgiroCds(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);
	else
Dbms_Output.put_line ('riportoEsNextObbPGiro 2');
	   CNRCTB035.getPgiroCdsInv(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);
	end if;

	-- lock della controparte
	cnrctb035.LOCKDOCFULL(aAcc);

	-- verifico se la controparte Ã¨ ribaltabile
	CNRCTB048.checkRiportaEsNext(aAcc,false, true);

	if aElementoVoce.esercizio is null then
	-- non cambia imputazione finanziaria
		if aprePgiro then
Dbms_Output.put_line ('riportoEsNextObbPGiro 3');
		   ripPgiroCds(aObb,aObbNew,aTSNow,aUser);
		else
Dbms_Output.put_line ('riportoEsNextObbPGiro 4');
		   ripPgiroCds(aAcc,aObbNew,aTSNow,aUser);
		end if;
	else
		if aprePgiro then
Dbms_Output.put_line ('riportoEsNextObbPGiro 5');
		   ripPgiroCdsInt(aObb,aObbNew,aElementoVoce,aTSNow,aUser);
		else
Dbms_Output.put_line ('riportoEsNextObbPGiro 6');
		   ripPgiroCdsInt(aAcc,aObbNew,aElementoVoce,aTSNow,aUser);
		end if;
	end if;
end;

Procedure riportoEsNextObbAccPGiro(aObbIn         obbligazione%Rowtype,
                                   aAccIn         accertamento%Rowtype,
                                   aElementoVoceS elemento_voce%Rowtype,
                                   aElementoVoceE elemento_voce%Rowtype,
                                   aUser          VARCHAR2,
                                   aTSNow         DATE) Is

aObb            obbligazione%Rowtype;
aObbScad        obbligazione_scadenzario%Rowtype;
aObbScadVoce    obbligazione_scad_voce%Rowtype;

aAcc            accertamento%Rowtype;
aAccScad        accertamento_scadenzario%Rowtype;
aAccScadVoce    accertamento_scad_voce%Rowtype;

aprePGiro       Boolean;
aTi_Origine     CHAR;

aObbNew         obbligazione%Rowtype;
aAccNew         accertamento%Rowtype;

Begin

-- verifico se l'elemento voce di spesa, se specificato, Ã¨ su pgiro
If aElementoVoceS.esercizio is not null and aElementoVoceS.fl_partita_giro = 'N' then
   ibmerr001.RAISE_ERR_GENERICO('L''elemento voce di spesa specificato ('||aElementoVoceS.cd_elemento_voce||') non Ã¨ su partita di giro, impossibile ribaltare.');
End If;

-- verifico se l'elemento voce di entrata, se specificato, Ã¨ su pgiro
If aElementoVoceE.esercizio is not null and aElementoVoceE.fl_partita_giro = 'N' then
   ibmerr001.RAISE_ERR_GENERICO('L''elemento voce di entrata specificato ('||aElementoVoceE.cd_elemento_voce||') non Ã¨ su partita di giro, impossibile ribaltare.');
End If;

-- recupero i documenti contabili da ribaltare

Dbms_Output.put_line ('riportoEsNextObbAccPGiro 1');

-- se l'impegno Ã¨ valorizzato recupera l'accertamento

If aObbIn.esercizio Is Not Null Then

Dbms_Output.put_line ('riportoEsNextObbAccPGiro 2');

  aObb := aObbIn;

  -- verifico se l'obbligazione ha aperto la partita di giro
  aprePGiro := CNRCTB035.ISAPREPGIRO(aObb);

  -- recupera comunque sempre l'obbligazione e l'accertamento
  If aprePGiro then
     CNRCTB035.getPgiroCds(aObb, aObbScad, aObbScadVoce, aAcc, aAccScad, aAccScadVoce);
     aTi_Origine := CNRCTB001.GESTIONE_SPESE;
  Else
     CNRCTB035.getPgiroCdsInv(aObb, aObbScad, aObbScadVoce, aAcc, aAccScad, aAccScadVoce);
     aTi_Origine := CNRCTB001.GESTIONE_ENTRATE;
  End If;

  -- lock della controparte (accertamento)
  cnrctb035.LOCKDOCFULL(aAcc);

  -- verifico se la controparte Ã¨ ribaltabile
  Dbms_Output.put_line ('CNRCTB048.checkRiportaEsNext');
  CNRCTB048.checkRiportaEsNext(aAcc, false, true);

Elsif aAccIn.esercizio Is Not Null Then

Dbms_Output.put_line ('riportoEsNextObbAccPGiro 3');

-- se l'accertamento Ã¨ valorizzato recupera l'impegno

  aAcc := aAccIn;

  -- verifico se l'accertamento ha aperto la partita di giro
  aprePGiro := CNRCTB035.ISAPREPGIRO(aAcc);

  -- recupera comunque sempre l'obbligazione e l'accertamento
  If aprePGiro then
Dbms_Output.put_line ('riportoEsNextObbAccPGiro 4');
     CNRCTB035.getPgiroCds(aAcc, aAccScad, aAccScadVoce, aObb, aObbScad, aObbScadVoce);
     aTi_Origine := CNRCTB001.GESTIONE_ENTRATE;
  Else
Dbms_Output.put_line ('riportoEsNextObbAccPGiro 5');
     CNRCTB035.getPgiroCdsInv(aAcc, aAccScad, aAccScadVoce, aObb, aObbScad, aObbScadVoce);
     aTi_Origine := CNRCTB001.GESTIONE_SPESE;
  End If;

  -- lock della controparte (obbligazione)
  cnrctb035.LOCKDOCFULL(aObb);

  -- verifico se la controparte Ã¨ ribaltabile
Dbms_Output.put_line ('riportoEsNextObbAccPGiro 6');
  CNRCTB048.checkRiportaEsNext(aObb, False);

End If;

--------------------------- Ribalto SEMPRE entrambe le partite di giro !!!! ----------------------------

Dbms_Output.put_line ('riportoEsNextObbAccPGiro 7 chiamata a ripPgiroCdsEntrambe');
ripPgiroCdsEntrambe(aObb, aObbScad, aObbScadVoce, aAcc, aAccScad, aAccScadVoce, aElementoVoceE, aElementoVoceS,
                    aTI_ORIGINE, aTSNow, aUser, aObbNew, aAccNew);

End;

procedure riportoEsNextAccPGiro(aAcc in out accertamento%rowtype, aElementoVoce elemento_voce%rowtype, aUser varchar2, aTSNow date) is
aAccNew accertamento%rowtype;
aAccScad accertamento_scadenzario%rowtype;
aAccScadVoce accertamento_scad_voce%rowtype;
aObb obbligazione%rowtype;
aObbScad obbligazione_scadenzario%rowtype;
aObbScadVoce obbligazione_scad_voce%rowtype;
aprePgiro boolean;
begin
	-- verifico se l'elemento voce, se specificato, Ã¨ su pgiro
	if aElementoVoce.esercizio is not null and aElementoVoce.fl_partita_giro = 'N' then
	   ibmerr001.RAISE_ERR_GENERICO('L''elemento voce specificato non Ã¨ su partita di giro, impossibile ribaltare');
	end if;

	-- verifico se l'accertamento ha aperto la partita di giro
	aprePGiro := CNRCTB035.ISAPREPGIRO(aAcc);

	if aprePGiro then
	   CNRCTB035.getPgiroCds(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
	else
	   CNRCTB035.getPgiroCdsInv(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
	end if;

	-- lock della controparte
	cnrctb035.LOCKDOCFULL(aObb);

	-- verifico se la controparte Ã¨ ribaltabile
	CNRCTB048.checkRiportaEsNext(aObb,false);

	if aElementoVoce.esercizio is null then
	-- non cambia imputazione finanziaria
		if aprePgiro then
		   ripPgiroCds(aAcc,aAccNew,aTSNow,aUser);
		else
		   ripPgiroCds(aObb,aAccNew,aTSNow,aUser);
		end if;
	else
		if aprePgiro then
		   ripPgiroCdsInt(aAcc,aAccNew,aElementoVoce,aTSNow,aUser);
		else
		   ripPgiroCdsInt(aObb,aAccNew,aElementoVoce,aTSNow,aUser);
		end if;
	end if;
end;

procedure riportoObb(aObb in out obbligazione%rowtype, aElementoVoce elemento_voce%rowtype, aVoceF voce_f%rowtype,aUser varchar2, aTSNow date) is
aCdTipoDoc varchar2(10);
aObbNext obbligazione%rowtype;
aObbScadNext obbligazione_scadenzario%rowtype;
aObbScadDettNext obbligazione_scad_voce%rowtype;
ListaObbScadVoceNext CNRCTB035.scadVoceListS;
aIm number;
isScadRibaltabile char(1);
posizione number:=0;
i number;
aLdA v_linea_attivita_valida%rowtype;
aVoce voce_f%rowtype;
isControlloBloccante boolean := false;
aEs number;
aErrMsn varchar2(4000);
aSaldo voce_f_saldi_cmp%rowtype;
aSaldocdrlinea voce_f_saldi_cdr_linea%rowtype;
aEV elemento_voce%rowtype;
cambiaImputFinImp boolean;
cambiaImputFinObb boolean;
esisteImpGiaRiportato boolean := false;
recParametriCNR PARAMETRI_CNR%Rowtype;
recParametriCDS PARAMETRI_CDS%Rowtype;
begin
	aEs := aObb.esercizio + 1;

  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  recParametriCDS := CNRUTL001.getRecParametriCds(aEs, aObb.cd_cds);

	-- verifico lo stato dell'esercizio

	cnrctb048.CHECKESERCIZIO(aObb.esercizio,aObb.cd_cds);

	-- se cambia imputazione finanziaria:
	-- per impegni, impegni residui no pgiro ===> voce_f
	-- 	   		===> cambiaImputFinImp = true, cambiaImputFinObb = false
	-- impegni, impegni residui pgiro, obbligazioni ===> elemento_voce
	-- 			===> cambiaImputFinImp = false, cambiaImputFinObb = true

	if aElementoVoce.esercizio is Null And aVoceF.esercizio is Null then
		cambiaImputFinImp := false;
		cambiaImputFinObb := false;
	elsif aElementoVoce.esercizio is Null And aVoceF.esercizio is not Null Then
		cambiaImputFinImp := true;
		cambiaImputFinObb := false;
	elsif aElementoVoce.esercizio is not Null And aVoceF.esercizio is Null Then
		cambiaImputFinImp := false;
		cambiaImputFinObb := true;
	else
		aErrMsn := 'Errore nella chiamata di CNRCTB046.riportoObb(): deve essere valorizzato elemento_voce o voce_f';
		ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	end if;


	if aObb.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_IMP, CNRCTB018.TI_DOC_IMP_RES) then

	   -- impegni, competenza e residui bilancio ente, sia pgiro che no pgiro
	   aCdTipoDoc := CNRCTB018.TI_DOC_IMP_RES;
	   if cambiaImputFinObb and aObb.fl_pgiro = 'N' then
	   	  aErrMsn := 'Errore nella chiamata di CNRCTB046.riportoObb(): per riportare impegni non su partita di giro valorizzare voce_f';
		  ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	   end if;

	   if cambiaImputFinImp and aObb.fl_pgiro = 'Y' then
	   	  aErrMsn := 'Errore nella chiamata di CNRCTB046.riportoObb(): per riportare impegni su partita di giro valorizzare elemento_voce';
		  ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	   end if;

	elsif aObb.cd_tipo_documento_cont = CNRCTB018.TI_DOC_OBB then

	   -- obbligazioni no pgiro (DIVENTANO OBB_RES_PRO)
	   aCdTipoDoc := CNRCTB018.TI_DOC_OBB_RES_PRO;
	   if cambiaImputFinImp then
	   	  aErrMsn := 'Errore nella chiamata di CNRCTB046.riportoObb(): per riportare '||cnrutil.getLabelObbligazioniMin()||' valorizzare elemento_voce';
		  ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	   end if;

	elsif aObb.cd_tipo_documento_cont In (CNRCTB018.TI_DOC_OBB_RES_PRO, CNRCTB018.TI_DOC_OBB_RES_IMPRO) Then

	   -- obbligazioni RESIDUE PROPRIE no pgiro (RESTANO OBB_RES)
	   aCdTipoDoc := CNRCTB018.TI_DOC_OBB_RES_PRO;
	   if cambiaImputFinImp then
	   	  aErrMsn := 'Errore nella chiamata di CNRCTB046.riportoObb(): per riportare '||cnrutil.getLabelObbligazioniMin()||' valorizzare elemento_voce';
		  ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	   end if;

	elsif aObb.cd_tipo_documento_cont In (CNRCTB018.TI_DOC_OBB_PGIRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES) Then

	   -- obbligazioni PARTITE DI GIRO COMPETENZA/RESIDUE SI RIBALTANO NELL'ALTRO RAMO
   	  aErrMsn := 'Errore nella chiamata di CNRCTB046.riportoObb(): il documento Ã¨ una partita di giro.';
	  ibmerr001.RAISE_ERR_GENERICO(aErrMsn);

	else
	   aErrMsn := 'Errore nella chiamata di CNRCTB046.riportoObb(): documento non gestito ('||aObb.cd_tipo_documento_cont||')';
	   ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	end if;


        aObbNext.CD_TIPO_DOCUMENTO_CONT := aCdTipoDoc;

	begin
		if cambiaImputFinImp then
	   	   select * into aEV
		   from elemento_voce
		   where esercizio 	  	  = aEs
		     and ti_appartenenza  = aVoceF.ti_appartenenza
			 and ti_gestione	  = aVoceF.ti_gestione
			 and cd_elemento_voce = aVoceF.cd_titolo_capitolo;
		elsif cambiaImputFinObb then
		   aEV := aElementoVoce;
		else  -- non cambia imputazione finanziaria
       select * into aEV
       from elemento_voce
       where esercizio        = aEs
       and ti_appartenenza  = aObb.TI_APPARTENENZA
       and ti_gestione    = aObb.TI_GESTIONE
       and cd_elemento_voce = aObb.CD_ELEMENTO_VOCE;
		end if;
    exception when NO_DATA_FOUND then
   		aErrMsn := 'Elemento voce '||nvl(aVoceF.cd_titolo_capitolo,aObb.CD_ELEMENTO_VOCE)||', appartenenza '||nvl(aVoceF.ti_appartenenza,aObb.TI_APPARTENENZA)||', gestione '||nvl(aVoceF.ti_gestione,aObb.TI_GESTIONE)||', esercizio '||aEs||' non trovato';
		ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
    end;
	-- verifico se l'elemento voce Ã¨ su pgiro
	if aEV.fl_partita_giro = 'Y' and aObb.fl_pgiro = 'N' then
	   ibmerr001.RAISE_ERR_GENERICO('L''elemento voce specificato Ã¨ su partita di giro, impossibile ribaltare');
	end if;

	if aEV.fl_partita_giro = 'N' and aObb.fl_pgiro = 'Y' then
	   ibmerr001.RAISE_ERR_GENERICO('L''elemento voce specificato non Ã¨ su partita di giro, impossibile ribaltare');
	end if;

	aObbNext.TI_APPARTENENZA  :=aEV.TI_APPARTENENZA;
	aObbNext.TI_GESTIONE	  :=aEV.TI_GESTIONE;
	aObbNext.CD_ELEMENTO_VOCE :=aEV.CD_ELEMENTO_VOCE;

	CNRCTB048.creaTestataObb(aObb,aObbNext,aEs, aUser, aTSNow);

	-- se si tratta di obbligazione pluriennale nell'esercizio di partenza,
	-- controllo il limite di assunzione obbligazioni

/* 17/06/2009 eliminato perchÃ¨ non serve piÃ¹ (eliminazione SALDI_CMP)

	if aObb.esercizio <> aObb.esercizio_competenza And CNRCTB030.CHECKASSUNZOBBLIG(aObbNext.esercizio_competenza,
	   							   aObbNext.esercizio,
								   aObbNext.cd_cds,
								   aObbNext.cd_unita_organizzativa,
								   aObbNext.cd_elemento_voce,
								   aObbNext.im_obbligazione) = 'N' then

		aErrmsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' non ha superato il controllo assunzione obbligazioni nel nuovo esercizio';
		ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	end if;
*/

	for aScad in (select * from obbligazione_scadenzario
			  	  where cd_cds 			= aObb.cd_cds
				    and esercizio 		= aObb.esercizio
				    and esercizio_originale	= aObb.esercizio_originale
				    and pg_obbligazione = aObb.pg_obbligazione) loop
		if CNRCTB048.getStatoRibaltabileScad(aEs,aScad) = 'Y' then
		-- escludo le scadenze non ribaltabili, per controlli precedenti
		-- dovrebbe esserci almeno una scadenza ribaltabile

		   CNRCTB048.creaScadObb(aObbNext,aScad,aObbScadNext,aEs,aUser,aTSNow);

		   posizione := posizione + 1;
		   i:= 0; -- progressivo dettaglio scadenza
		   for aDettScad in (select * from obbligazione_scad_voce
				     where cd_cds = aScad.cd_cds
				     and esercizio = aScad.esercizio
				     and esercizio_originale = aScad.esercizio_originale
				     and pg_obbligazione = aScad.pg_obbligazione
				     and pg_obbligazione_scadenzario = aScad.pg_obbligazione_scadenzario) loop
			   i := i+1;
			   -- verifico la mappatura per LdA
			   aLdA := cnrctb048.getLdA(aDettScad, aObb, aEs);
			   -- verifico la validitÃ  della LdA sul nuovo esercizio
         if aLdA.esercizio_inizio is null Then
            aErrmsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' ha scadenze sulla linea di attivita'' '||aDettScad.cd_linea_attivita||' non associata a progetto o non valida nell''esercizio '||aEs||'.';
            ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
         elsif aLdA.esercizio_fine < aObbNext.ESERCIZIO_ORIGINALE then  -- non Ã¨ valida
				    aErrmsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' ha scadenze sulla linea di attivita'' '||aDettScad.cd_linea_attivita||' non valida nell''esercizio '||aObbNext.ESERCIZIO_ORIGINALE;
				    ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
			   end if;

			   aObbScadDettNext.CD_CENTRO_RESPONSABILITA := aLdA.CD_CENTRO_RESPONSABILITA;
			   aObbScadDettNext.CD_LINEA_ATTIVITA		 := aLdA.CD_LINEA_ATTIVITA;

         --Se attiva la nuova gestione pdgp verifico che sulla linea attivitÃ  sia stato inserito il progetto nell'anno di
         --ribaltamento
         If recParametriCDS.fl_commessa_obbligatoria='Y' and aLdA.pg_progetto is null Then
            aErrmsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' ha scadenza su gae '||aLdA.cd_centro_responsabilita||'/'||aLdA.cd_linea_attivita||' per cui non Ã¨ stato indicato il progetto nell''anno contabile '||aEs||'.';
            ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
         End If;

				 --  controllo indicazione Cofog prima di procedere al ribaltamento
				 -- Non faccio il controllo per le Obbligazioni su partita di Giro e sugli Impegni ente
				 if aObb.cd_tipo_documento_cont not in (CNRCTB018.TI_DOC_IMP, CNRCTB018.TI_DOC_IMP_RES,CNRCTB018.TI_DOC_OBB_PGIRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES) then
					 if recParametriCNR.LIVELLO_PDG_COFOG!=0 and aLdA.cd_cofog is null then
			         aErrmsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' ha scadenza su gae '||aLdA.cd_centro_responsabilita||'/'||aLdA.cd_linea_attivita||' per cui non Ã¨ stato indicato il cofog.';
			  			 ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
					 end if;
				 end if;
				 -- fine  06/11/2013 Rospuc
			   -- se non cambia imputazione finanziaria
			   if recParametriCNR.fl_nuovo_pdg='Y' Then
            aObbScadDettNext.CD_VOCE := aObbNext.CD_ELEMENTO_VOCE;
         Else
            If not cambiaImputFinImp and not cambiaImputFinObb then
  				   -- determino se capitolo voce_f Ã¨ valido sul nuovo esercizio
	 			      begin
				   	    aVoce := CNRCTB048.getVoceF(aObb, aLdA, aDettscad, aEs);
				      exception when NO_DATA_FOUND then
				   	     aErrMsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' ha scadenze il cui capitolo non Ã¨ valido sul nuovo esercizio';
					      ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
				      end;
			     Elsif cambiaImputfinImp and aObb.fl_pgiro = 'N' and aObbNext.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES then
			   	   aVoce := aVoceF;
			     Elsif cambiaImputFinObb and aObb.fl_pgiro = 'Y' and aObbNext.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES then
				      begin
				   	    aVoce := CNRCTB048.getVoceF(aObb, aLdA, aEV, aDettScad, aEs);
				      exception when NO_DATA_FOUND then
				   	    aErrMsn := 'L''impegno '||CNRCTB035.GETDESC(aObb)||' ha scadenze il cui capitolo non Ã¨ valido sul nuovo esercizio';
					      ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
				      end;
			      Else -- cambiaImputfinObb and cd_tipo_documento_cont = OBB_PGIRO
			   	    begin
				   	    aVoce := CNRCTB048.getVoceF(aObb, aLdA, aEV, aDettScad, aEs);
				      exception when NO_DATA_FOUND then
				   	    aErrMsn := 'L'''||cnrutil.getLabelObbligazioneMin()||' '||CNRCTB035.GETDESC(aObb)||' ha scadenze il cui capitolo finanziario non Ã¨ valido sul nuovo esercizio';
					      ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
				      end;
			      End if;
			      aObbScadDettNext.CD_VOCE := aVoce.CD_VOCE;
         End If;

			   CNRCTB048.creaScadDettObb(aObbScadNext, aDettScad, aObbScadDettNext, aUser, aTSNow);

         if recParametriCNR.fl_nuovo_pdg='Y' Then
            aObbScadDettNext.TI_APPARTENENZA := 'D';
         end if;

			   -- per impegni, impegni residui (impegnoni - generano imp_res nell'esercizio successivo)
			   -- sovrascrivo l'im_voce con l'importo della scadenza
			   if aObbNext.CD_TIPO_DOCUMENTO_CONT = CNRCTB018.TI_DOC_IMP_RES then
			   	  aObbScadDettNext.IM_VOCE := aObbScadNext.IM_SCADENZA;
			   end if;

                        ListaObbScadVoceNext(i) := aObbScadDettNext;

			   -- se si genera un residuo nel nuovo esercizio, devo incrementare
			   -- l'importo stanziamento iniziale anno 1 per il capitolo finanziario
			   if recParametriCNR.fl_nuovo_pdg='N' and aObbNext.CD_TIPO_DOCUMENTO_CONT = CNRCTB018.TI_DOC_IMP_RES then
 				    begin
  				   	  select * into aSaldo
  					    from voce_f_saldi_cmp
      					where cd_cds 		 	    = aObbScadDettNext.cd_cds
      					  and esercizio 		    = aObbScadDettNext.esercizio
      					  and ti_appartenenza 	    = aObbScadDettNext.ti_appartenenza
      					  and ti_gestione	  	    = aObbScadDettNext.ti_gestione
      					  and cd_voce				= aObbScadDettNext.cd_voce
      					  and ti_competenza_residuo = CNRCTB054.TI_RESIDUI
      					for update nowait;
  				  exception when NO_DATA_FOUND then
  				   	  aErrMsn := 'Saldo residuo non trovato per voce finanziaria '||aObbScadDettNext.cd_voce||', gestione '||aObbScadDettNext.ti_gestione||', esercizio '||aObbScadDettNext.esercizio;
  					    ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
  				  end;
				    CNRCTB054.AGGIORNASTANZIAMENTORESIDUI(aSaldo,aObbScadDettNext.im_voce,aUser,aTSNow);
			   end if;
		   end loop; -- dettagli scadenze

		   if posizione = 1 then
		   	  -- inserisco 1! volta la testata
			  declare
			       isStornato number;
		   	  begin
Dbms_Output.put_line ('ins_obb '||aObbNext.cd_cds||' '||aObbNext.esercizio||' '||aObbNext.pg_obbligazione);
		   	  	   CNRCTB035.ins_OBBLIGAZIONE(aObbNext);
Dbms_Output.put_line ('dopo ins_obb');
			  exception when DUP_VAL_ON_INDEX then
			  -- documento ente giÃ  riportato e deriportato, ma non cancellato fisicamente
			  -- si effettua un aggiornamento dell'esistente, e non un inserimento

			  -- verifico che il documento residuo sia stornato (previsto al deriporto
			  -- nel caso di mancata cancellazione fisica)
			  	 select decode(stato_obbligazione,'S',1,0) into isStornato
				 from obbligazione
				 where cd_cds = aObbNext.cd_cds
				   and esercizio = aObbNext.esercizio
				   and esercizio_originale = aObbNext.esercizio_originale
				   and pg_obbligazione = aObbNext.pg_obbligazione;

			  	 if aObbNext.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES
				 	and isStornato = 1
				 then
				 	CNRCTB048.aggiornaImpNext(aObbNext);
					esisteImpGiaRiportato := true;
				 else -- non dovrebbe mai succedere
				   	  aErrMsn := 'Documento '||CNRCTB035.GETDESC(aObbNext)||' giÃ  esistente e attivo';
					  ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
				 end if;

			  end;
		   end if;

		   if esisteImpGiaRiportato then
		   	   CNRCTB048.aggiornaScadImpNext(aObbNext,aObbScadNext,posizione,ListaObbScadVoceNext,isControlloBloccante);
		   else -- non esiste imp res giÃ  riportato
		   	   CNRCTB035.CREASCADOBBLIGAZIONE(aObbNext,aObbScadNext,posizione,ListaObbScadVoceNext,isControlloBloccante);
		   end if;


		   -- aggiornamento dei documenti amministrativi collegati
		   if aObbScadNext.im_associato_doc_amm > 0 then
				CNRCTB048.aggiornaDocAmm(aScad, aObbScadNext,aUser, aTSNow);
		   end if;
Dbms_Output.put_line (' a 6');
		end if;
Dbms_Output.put_line (' a 6.5');
	end loop; -- scadenze
Dbms_Output.put_line (' a 7');
	-- Aggiorno la testata del documento come riportato
	update obbligazione
	set riportato = 'Y',
		duva = aTSNow,
		utuv = aUser,
		pg_ver_rec = pg_ver_rec + 1
	where cd_cds   	 	  = aObb.cd_cds
	  and esercizio 	  = aObb.esercizio
	  and esercizio_originale = aObb.esercizio_originale
	  and pg_obbligazione = aObb.pg_obbligazione;
Dbms_Output.put_line ('fine');
end;

procedure riportoAcc(aAcc in out accertamento%rowtype,aElementoVoce elemento_voce%rowtype,aUser varchar2,aTSNow date) is
aCdTipoDoc varchar2(10);
aAccNext accertamento%rowtype;
aAccScadNext accertamento_scadenzario%rowtype;
aAccDettScadNext accertamento_scad_voce%rowtype;
ListaAccScadVoceNext CNRCTB035.scadVoceListE;
posizione number:=0;
i number;
aLdA v_linea_attivita_valida%rowtype;
aEs number;
aErrMsn varchar2(4000);
aVoceF voce_f%rowtype;
aSaldo voce_f_saldi_cmp%rowtype;
aSaldocdrlinea voce_f_saldi_cdr_linea%rowtype;
recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  aEs := aAcc.esercizio + 1;

  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);

	-- verifico lo stato dell'esercizio
	cnrctb048.CHECKESERCIZIO(aAcc.esercizio,aAcc.cd_cds);

	-- verifico se l'elemento voce, se specificato, Ã¨ su pgiro
	if aElementoVoce.esercizio is not Null And
	   aElementoVoce.fl_partita_giro = 'Y' And
	   aAcc.fl_pgiro = 'N' Then
	   ibmerr001.RAISE_ERR_GENERICO('L''elemento voce specificato Ã¨ su partita di giro, impossibile ribaltare');
	end if;

	if aElementoVoce.esercizio is not Null and aElementoVoce.fl_partita_giro = 'N' And aAcc.fl_pgiro = 'Y' Then
	   ibmerr001.RAISE_ERR_GENERICO('L''elemento voce specificato non Ã¨ su partita di giro, impossibile ribaltare');
	end if;

	if aAcc.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_ACC, CNRCTB018.TI_DOC_ACC_RES) then
	     -- accertamento, accertamento residuo (bilancio ente)
 	     aCdTipoDoc := CNRCTB018.TI_DOC_ACC_RES;
	elsif (aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_PLUR And aAcc.esercizio_competenza = aEs) then
	     -- accertamento pluriennale a competenza nel nuovo esercizio
	     aCdTipoDoc := CNRCTB018.TI_DOC_ACC;
	elsif (aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_PLUR And aAcc.esercizio_competenza > aEs) then
	     -- accertamento pluriennale a competenza nel nuovo esercizio
	     aCdTipoDoc := CNRCTB018.TI_DOC_ACC_PLUR;
	elsif aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_PGIRO_RES Then
	     aCdTipoDoc := CNRCTB018.TI_DOC_ACC_PGIRO_RES;
	else
 	     aErrMsn := 'Errore nella chiamata di CNRCTB046.riportoAcc(): documento non gestito ('||aAcc.cd_tipo_documento_cont||')';
	     ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	end if;

	aAccNext.CD_TIPO_DOCUMENTO_CONT := aCdTipoDoc;

	if aElementoVoce.esercizio is null then
	  -- non c'Ã¨ cambio di imputazione finanziaria
	  aAccNext.TI_APPARTENENZA     := aAcc.TI_APPARTENENZA;
	  aAccNext.TI_GESTIONE	       := aAcc.TI_GESTIONE;
	  aAccNext.CD_ELEMENTO_VOCE    := aAcc.CD_ELEMENTO_VOCE;
	  aAccNext.CD_VOCE             := aAcc.CD_VOCE;
	  aAccNext.esercizio_originale := aAcc.esercizio;
	else
    aAccNext.TI_APPARTENENZA     :=aElementoVoce.TI_APPARTENENZA;
    aAccNext.TI_GESTIONE         :=aElementoVoce.TI_GESTIONE;
    aAccNext.CD_ELEMENTO_VOCE    :=aElementoVoce.CD_ELEMENTO_VOCE;
    aAccNext.esercizio_originale :=aAcc.esercizio;

    if recParametriCNR.fl_nuovo_pdg='Y' Then
      aAccNext.CD_VOCE := aElementoVoce.CD_ELEMENTO_VOCE;
    else
  	  begin
	     	select * into aVoceF
		    from voce_f
  		  where esercizio              = aEs
  		  and ti_appartenenza        = aElementoVoce.ti_appartenenza
  		  and ti_gestione            = aElementoVoce.ti_gestione
  		  and cd_titolo_capitolo     = aElementoVoce.cd_elemento_voce
  		  and cd_unita_organizzativa = aAcc.cd_uo_origine;

        aAccNext.CD_VOCE           :=aVoceF.cd_voce;
	    exception
        when NO_DATA_FOUND then
  	   	  aErrMsn := 'Capitolo finanziario non trovato in corrispondenza dell''elemento voce'||aElementoVoce.cd_elemento_voce||', appartenenza '||aElementoVoce.ti_appartenenza||', gestione '||aElementoVoce.ti_gestione||', esercizio'||aEs||', impossibile ribaltare';
    		  ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	    end;
    end if;
	end if;

	CNRCTB048.creaTestataAcc(aAcc,aAccNext,aEs,aUser,aTSNow);

	-- se si genera un residuo nel nuovo esercizio, devo incrementare
	-- l'importo stanziamento iniziale anno 1 per il capitolo finanziario
	if recParametriCNR.fl_nuovo_pdg='N' and aAccNext.CD_TIPO_DOCUMENTO_CONT = CNRCTB018.TI_DOC_ACC_RES then
Dbms_Output.put_line ('7');
	   begin
	   		select * into aSaldo
			from voce_f_saldi_cmp
			where cd_cds 		 	    = aAccNext.cd_cds
			  and esercizio 		    = aAccNext.esercizio
			  and ti_appartenenza 	    = aAccNext.ti_appartenenza
			  and ti_gestione	  	    = aAccNext.ti_gestione
			  and cd_voce				= aAccNext.cd_voce
			  and ti_competenza_residuo = CNRCTB054.TI_RESIDUI
			for update nowait;
	   exception when NO_DATA_FOUND then
	   	    aErrMsn := 'Saldo residuo non trovato per voce finanziaria'||aAccNext.cd_voce||', gestione '||aAccNext.ti_gestione||' esercizio '||aAccNext.esercizio;
			ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
	   end;
Dbms_Output.put_line ('8');
	   CNRCTB054.AGGIORNASTANZIAMENTORESIDUI(aSaldo,aAccNext.im_accertamento,aUser,aTSNow);
Dbms_Output.put_line ('9');
	end if;

Dbms_Output.put_line ('10 '||aAcc.cd_cds||'/'||aAcc.esercizio||'/'||aAcc.pg_accertamento);
	for aScad in (select * from accertamento_scadenzario
			  	  where cd_cds 			= aAcc.cd_cds
				    and esercizio 		= aAcc.esercizio
				    and esercizio_originale	= aAcc.esercizio_originale
					and pg_accertamento = aAcc.pg_accertamento) loop
Dbms_Output.put_line ('11');
		if CNRCTB048.getStatoRibaltabileScad(aEs,aScad) = 'Y' then
Dbms_Output.put_line ('12');
		-- escludo le scadenze non ribaltabili, per controlli precedenti
		-- dovrebbe esserci almeno una scadenza ribaltabile
		   CNRCTB048.creaScadAcc(aAccNext,aScad,aAccScadNext,aEs,aUser,aTSNow);

		   posizione := posizione + 1;
		   i := 0; -- progressivo dettaglio scadenza
		   for aDettScad in (select * from accertamento_scad_voce
		   	   			 	 where cd_cds = aScad.cd_cds
							   and esercizio = aScad.esercizio
							   and esercizio_originale = aScad.esercizio_originale
							   and pg_accertamento = aScad.pg_accertamento
							   and pg_accertamento_scadenzario = aScad.pg_accertamento_scadenzario) loop
			   i := i+1;
			   -- verifico la mappatura per LdA
         aLdA := cnrctb048.getLdA(aDettScad, aAcc, aEs);
			   -- verifico la validitÃ  della LdA sul nuovo esercizio
         if aLdA.esercizio_inizio is null Then
            aErrmsn := 'L''accertamento '||CNRCTB035.GETDESC(aAcc)||' ha scadenze sulla linea di attivita'' '||aDettScad.cd_linea_attivita||' non associata a progetto o non valida nell''esercizio '||aEs||'.';
            ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
         elsif aLdA.esercizio_fine < aAccNext.esercizio_originale then  -- non Ã¨ valida
  				  aErrmsn := 'L''accertamento '||CNRCTB035.GETDESC(aAcc)||' ha scadenze sulla linea di attivita'' '||aDettScad.cd_linea_attivita||' non valida nell''esercizio '||aAccNext.esercizio_originale;
				    ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
			   end if;
Dbms_Output.put_line ('13');
			   aAccDettScadNext.CD_CENTRO_RESPONSABILITA := aLdA.CD_CENTRO_RESPONSABILITA;
			   aAccDettScadNext.CD_LINEA_ATTIVITA		 := aLdA.CD_LINEA_ATTIVITA;
Dbms_Output.put_line ('13.a '||aAcc.cd_tipo_documento_cont||'/'||aDettScad.CD_CENTRO_RESPONSABILITA||
'/'||aDettScad.CD_LINEA_ATTIVITA);
			   -- verifico la validitÃ  della LdA per l'elemento voce
			   -- esclusi accertamenti residui 2003, con linea attivitÃ  di sistema

-- ELIMINATO QUESTO FILTRO !!!! 23.01.2006

--If not (aAcc.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES and
--	aDettScad.CD_CENTRO_RESPONSABILITA = CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE) and
--        aDettScad.CD_LINEA_ATTIVITA = CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE)
--       ) Then

 If aAccNext.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_ACC, CNRCTB018.TI_DOC_ACC_RES, CNRCTB018.TI_DOC_ACC_PLUR)
                and aAccNext.fl_pgiro = 'N' Then
					  declare
					  	  lCdNat char(1);
					  begin

					  	  select cd_natura into lCdNat
		  				  from ass_ev_ev
						  where esercizio 		 = aAccNext.esercizio
						    and ti_appartenenza  = aAccNext.ti_appartenenza
						    and ti_gestione	     = aAccNext.ti_gestione
						    and cd_elemento_voce = aAccNext.cd_elemento_voce
							and cd_natura		 = aLda.cd_natura;
					  exception when NO_DATA_FOUND then
				   	      aErrMsn := 'L''accertamento '||CNRCTB035.GETDESC(aAcc)||' ha dettagli di scadenze la cui linea di attivitÃ  nel nuovo esercizio non ha natura compatibile con l''elemento voce';
					      ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
					  end;
				   end if;

--			   end if;


			   CNRCTB048.creaScadDettAcc(aAccScadNext,
			                             aDettScad,
			                             aAccDettScadNext,
			                             aUser,
			                             aTSNow);
			   ListaAccScadVoceNext(i) := aAccDettScadNext;

		   end loop; -- dettagli scadenza

		   if posizione = 1 then
		   	  -- inserisco 1! la testata e aggiorno i saldi
			  declare
			  	   isAnnullato number;
			  Begin
		   	  	   CNRCTB035.INS_ACCERTAMENTO(aAccNext);
			  exception when DUP_VAL_ON_INDEX then
			  -- documento ente giÃ  riportato e deriportato, ma non cancellato fisicamente
			  -- si effettua un aggiornamento dell'esistente, e non un inserimento

				   -- verifico se il residuo Ã¨ annullato (previsto nel caso in cui
				   -- il deriporto non abbia effettuato la cancellazione fisica)
				   select decode(dt_cancellazione,null,0,1) into isAnnullato
				   from accertamento
				   where cd_cds 	     = aAccNext.cd_cds
				     and esercizio 		 = aAccNext.esercizio
				     and esercizio_originale	 = aAccNext.esercizio_originale
					 and pg_accertamento = aAccNext.pg_accertamento;

			  	   if aAccNext.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES
				      and isAnnullato = 1
				   then

					  -- aggiorno doc esistente
					  CNRCTB048.aggiornaAccNext(aAccNext);

					  -- resetto la posizione (ovvero numero della scadenza) al numero
					  -- massimo + 1 del pg_accertamento_scadenzario giÃ  esistente
					  select nvl((max(pg_accertamento_scadenzario) + 1),1) into posizione
					  from accertamento_scadenzario
					  where cd_cds 			= aAccNext.cd_cds
					    and esercizio 		= aAccNext.esercizio
				            and esercizio_originale	= aAccNext.esercizio_originale
						and pg_accertamento = aAccNext.pg_accertamento;
				   else -- non dovrebbe mai succedere
				   	  aErrMsn := 'Documento '||CNRCTB035.GETDESC(aAccNext)||' giÃ  esistente e attivo';
					  ibmerr001.RAISE_ERR_GENERICO(aErrMsn);
				   end if;
			  end;
        CNRCTB035.AGGIORNASALDODETTSCAD(aAccNext,aAccNext.im_accertamento,false, aUser,aTSNow);
		   end if;
                   -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
		   CNRCTB035.CREASCADACCERTAMENTO(aAccNext,aAccScadNext,posizione,ListaAccScadVoceNext);

		   -- aggiornamento dei documenti amministrativi collegati alla scadenza
		   if aAccScadNext.im_associato_doc_amm > 0 then
		   	  CNRCTB048.aggiornaDocAmm(aScad,aAccScadNext,aUser,aTSNow);
		   end if;

		end if;
	end loop; -- scadenze

	-- aggiorno la testata del documento come riportato
	update accertamento
	set riportato  = 'Y',
		utuv	   = aUser,
		duva	   = aTSNow,
		pg_ver_rec = pg_ver_rec + 1
	where cd_cds   	      = aAcc.cd_cds
	  and esercizio	 	  = aAcc.esercizio
	  and esercizio_originale = aAcc.esercizio_originale
	  and pg_accertamento = aAcc.pg_accertamento;

end;

procedure riportoEsNextObb(aObb          in out obbligazione%rowtype,
                           aElementoVoce elemento_voce%rowtype,
                           aVoceF        voce_F%rowtype,
                           aUser         varchar2,
                           aTSNow        date) is
  evNew elemento_voce%rowtype;
begin
	-- lock del documento
	cnrctb035.LOCKDOCFULL(aObb);

  evNew := aElementoVoce;

  If evNew.esercizio is null then
     evNew := getElementoVoceNew(aObb);
  End If;

	-- verifico se il documento Ã¨ ribaltabile
	CNRCTB048.checkRiportaEsNext(aObb, true);

	if aObb.cd_tipo_documento_cont In (CNRCTB018.TI_DOC_OBB_PGIRO, CNRCTB018.TI_DOC_OBB_PGIRO_RES) Then
    -- obbligazioni partite di giro bilancio cds
    riportoEsNextObbAccPGiro(aObb, Null, evNew, Null, aUser, aTSNow);
	else
  	-- impegni (pgiro e no pgiro) bilancio ente
	  riportoObb(aObb, evNew, aVoceF, aUser, aTSNow);
	end if;
End;

/* stani 02.01.2006 chiamata da job */

procedure riportoEsNextAcc(aAcc          in out accertamento%rowtype,
                           aElementoVoce elemento_voce%rowtype,
                           aUser         varchar2,
                           aTSNow        date) is
  evNew elemento_voce%rowtype;
begin
	-- lock del documento
	cnrctb035.LOCKDOCFULL(aAcc);

  evNew := aElementoVoce;

  If evNew.esercizio is null then
     evNew := getElementoVoceNew(aAcc);
  End If;

	-- verifico se il documento Ã¨ ribaltabile
  CNRCTB048.checkRiportaEsNext(aAcc,true,true); -- controlla la validitÃ  della voce_f

	if aAcc.cd_tipo_documento_cont In (CNRCTB018.TI_DOC_ACC_PGIRO, CNRCTB018.TI_DOC_ACC_PGIRO_RES) then
  	-- accertamenti partite di giro bilancio cds
    riportoEsNextObbAccPGiro(Null, aAcc, Null, evNew, aUser, aTSNow);
  Else
	  -- accertamenti (pgiro e no pgiro) bilancio ente
  	riportoAcc(aAcc, evNew, aUser, aTSNow);
	end if;
End;

/* stani 02.01.2006 chiamata da job */

procedure riportoEsNextDocCont(aCds varchar2,aEs number, aEsOri number, aPg number, aTiGestione varchar2, aUser varchar2) is
aObb obbligazione%rowtype;
aAcc accertamento%rowtype;
evNew elemento_voce%rowtype;
aTSNow date;

begin
 aTSNow := sysdate;
 if aTiGestione = CNRCTB001.GESTIONE_ENTRATE then  -- accertamento
	aAcc.cd_cds := aCds;
	aAcc.esercizio := aEs;
	aAcc.esercizio_originale := aEsOri;
	aAcc.pg_accertamento := aPg;
	-- lock della risorsa
	CNRCTB035.LOCKDOCFULL(aAcc);
  evNew := getElementoVoceNew(aAcc);
  if evNew.ti_appartenenza=aAcc.ti_appartenenza and evNew.ti_gestione=aAcc.ti_gestione and
     evNew.cd_elemento_voce=aAcc.cd_elemento_voce Then
  	riportoEsNextAcc(aAcc, null, aUser, aTSNow);
  else
    riportoEsNextAcc(aAcc, evNew, aUser, aTSNow);
  end if;
 else -- obbligazione
	aObb.cd_cds := aCds;
	aObb.esercizio := aEs;
	aObb.esercizio_originale := aEsOri;
	aObb.pg_obbligazione := aPg;
	-- lock della risorsa
	CNRCTB035.LOCKDOCFULL(aObb);
  evNew := getElementoVoceNew(aObb);
  if evNew.ti_appartenenza=aObb.ti_appartenenza and evNew.ti_gestione=aObb.ti_gestione and
     evNew.cd_elemento_voce=aObb.cd_elemento_voce Then
	  riportoEsNextObb(aObb, null, null, aUser, aTSNow);
  else
    riportoEsNextObb(aObb, evNew, null, aUser, aTSNow);
  end if;
 end if;
end;

procedure deriportoEsNextDocCont(aCds varchar2,aEs number, aEsOri number, aPg number, aTiGestione varchar2, aUser varchar2) is
aObb obbligazione%rowtype;
aAcc accertamento%rowtype;
aTSNow date;
begin
	aTSNow := sysdate;
	if aTiGestione = CNRCTB001.GESTIONE_ENTRATE then  -- accertamento
		aAcc.cd_cds := aCds;
		aAcc.esercizio := aEs;
		aAcc.esercizio_originale := aEsOri;
		aAcc.pg_accertamento := aPg;

		-- lock della risorsa
		CNRCTB035.LOCKDOCFULL(aAcc);

		deriportoEsNextAcc(aAcc, aUser, aTSNow);

	else -- obbligazione
		aObb.cd_cds := aCds;
		aObb.esercizio := aEs;
		aObb.esercizio_originale := aEsOri;
		aObb.pg_obbligazione := aPg;

		-- lock della risorsa
		CNRCTB035.LOCKDOCFULL(aObb);

		deriportoEsNextObb(aObb, aUser, aTSNow);
	end if;
end;

 procedure ripPgiroCds(
  aObb IN OUT obbligazione%rowtype,
  aObbNew IN OUT obbligazione%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
 begin
   ripPgiroCdsInt(aObb,aObbNew,null,aTSNow,aUser);
 end;


 procedure ripPgiroCds(
  aObb IN OUT obbligazione%rowtype,
  aAccNew IN OUT accertamento%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
 begin
 ripPgiroCdsInt(aObb,aAccNew,null,aTSNow,aUser);
 end;

 procedure ripPgiroCds(
  aAcc IN OUT accertamento%rowtype,
  aAccNew IN OUT accertamento%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
 begin
 ripPgiroCdsInt(aAcc, aAccNew, null, aTSNow, aUser);
 end;


 procedure ripPgiroCds(
  aAcc IN OUT accertamento%rowtype,
  aObbNew IN OUT obbligazione%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
 begin
   ripPgiroCdsInt(aAcc,aObbNew,null,aTSNow,aUser);
 end;

 procedure annullaRipPgiroCds(
  aObbNew IN OUT obbligazione%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aObbScadNew obbligazione_scadenzario%rowtype;
  aObbScadVoceNew obbligazione_scad_voce%rowtype;
  aAccNew accertamento%rowtype;
  aAccScadNew accertamento_scadenzario%rowtype;
  aAccScadVoceNew accertamento_scad_voce%rowtype;
  aAssPgiro ass_obb_acr_pgiro%rowtype;
 begin
  CNRCTB048.checkEsercizio(aObbNew.esercizio-1,aObbNew.cd_cds);
  CNRCTB035.getPgiroCds(aObbNew,aObbScadNew,aObbScadVoceNew,aAccNew,aAccScadNew,aAccScadVoceNew);
  begin
   -- Cerco la partita di giro in esercizio origine
   aObb.cd_cds:=aObbNew.cd_cds_ori_riporto;
   aObb.esercizio:=aObbNew.esercizio_ori_riporto;
   aObb.esercizio_originale:=aObbNew.esercizio_ori_ori_riporto;
   aObb.pg_obbligazione:=aObbNew.pg_obbligazione_ori_riporto;
   select * into aAssPgiro from ass_obb_acr_pgiro where
         cd_cds = aObbNew.cd_cds_ori_riporto
		  and esercizio = aObbNew.esercizio_ori_riporto
		  and esercizio_ori_obbligazione = aObbNew.esercizio_ori_ori_riporto
		  and pg_obbligazione = aObbNew.pg_obbligazione_ori_riporto
		  and ti_origine = CNRCTB001.GESTIONE_SPESE;
   -- E' partita di giro riportata completamente
   CNRCTB035.getPgiroCds(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);
   update accertamento set
    riportato = 'N',
    pg_ver_rec=pg_ver_rec+1
   where
        cd_cds=aAcc.cd_cds
    and esercizio=aAcc.esercizio
    and esercizio_originale=aAcc.esercizio_originale
    and pg_accertamento=aAcc.pg_accertamento;
  exception when NO_DATA_FOUND then
   -- E' partita di giro riportata per parte
   CNRCTB035.getPgiroCdsInv(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);
  end;
  update obbligazione set
   riportato = 'N',
   pg_ver_rec=pg_ver_rec+1
  where
       cd_cds=aObb.cd_cds
   and esercizio=aObb.esercizio
   and esercizio_originale=aObb.esercizio_originale
   and pg_obbligazione=aObb.pg_obbligazione;
  CNRCTB035.annullaObbligazione(aObbNew.cd_cds,aObbNew.esercizio,aObbNew.esercizio_originale,aObbNew.pg_obbligazione,aUser);
 end;

 procedure annullaRipPgiroCds(
  aAccNew IN OUT accertamento%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aObbNew obbligazione%rowtype;
  aObbScadNew obbligazione_scadenzario%rowtype;
  aObbScadVoceNew obbligazione_scad_voce%rowtype;
  aAccScadNew accertamento_scadenzario%rowtype;
  aAccScadVoceNew accertamento_scad_voce%rowtype;
  aAssPgiro ass_obb_acr_pgiro%rowtype;
 begin
  CNRCTB048.checkEsercizio(aAccNew.esercizio-1,aAccNew.cd_cds);
  CNRCTB035.getPgiroCds(aAccNew,aAccScadNew,aAccScadVoceNew,aObbNew,aObbScadNew,aObbScadVoceNew);
  begin
   -- Cerco la partita di giro in esercizio origine
   aAcc.cd_cds:=aAccNew.cd_cds_ori_riporto;
   aAcc.esercizio:=aAccNew.esercizio_ori_riporto;
   aAcc.esercizio_originale:=aAccNew.esercizio_ori_ori_riporto;
   aAcc.pg_accertamento:=aAccNew.pg_accertamento_ori_riporto;
   select * into aAssPgiro from ass_obb_acr_pgiro where
          cd_cds = aAccNew.cd_cds_ori_riporto
		  and esercizio = aAccNew.esercizio_ori_riporto
		  and esercizio_ori_obbligazione = aAccNew.esercizio_ori_ori_riporto
		  and pg_obbligazione = aAccNew.pg_accertamento_ori_riporto
		  and ti_origine = CNRCTB001.GESTIONE_ENTRATE;
   -- E' partita di giro riportata completamente
   CNRCTB035.getPgiroCds(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
   update obbligazione set
    riportato = 'N',
    pg_ver_rec=pg_ver_rec+1
   where
        cd_cds=aObb.cd_cds
    and esercizio=aObb.esercizio
    and esercizio_originale=aObb.esercizio_originale
    and pg_obbligazione=aObb.pg_obbligazione;
  exception when NO_DATA_FOUND then
   -- E' partita di giro riportata per parte
   CNRCTB035.getPgiroCdsInv(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
  end;
  update accertamento set
   riportato = 'N',
   pg_ver_rec=pg_ver_rec+1
  where
       cd_cds=aAcc.cd_cds
   and esercizio=aAcc.esercizio
   and esercizio_originale=aAcc.esercizio_originale
   and pg_accertamento=aAcc.pg_accertamento;
  CNRCTB035.annullaAccertamento(aAccNew.cd_cds,aAccNew.esercizio,aAccNew.esercizio_originale,aAccNew.pg_accertamento,aUser);
 end;

  procedure cancellaFisicamente(aObbNew IN OUT obbligazione%rowtype,aUser varchar2, aTSNow date) is
  aAccNew accertamento%rowtype;
  aAccScadNew accertamento_scadenzario%rowtype;
  aAccSCadVoceNew accertamento_scad_voce%rowtype;
  aObbScadNew obbligazione_scadenzario%rowtype;
  aObbScadVoceNew obbligazione_scad_voce%rowtype;
 begin
  savepoint ELIMINADOCOBB;
  begin
   CNRCTB035.lockDoc(aObbNew);
   if aObbNew.fl_pgiro = 'Y' then
    if CNRCTB035.isAprePgiro(aObbNew) and not (aObbNew.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES) then
	 CNRCTB035.GETPGIROCDS(aObbNew,aObbScadNew,aObbScadVoceNew,aAccNew,aAccScadNew,aAccSCadVoceNew);
	 delete from ass_obb_acr_pgiro where
               cd_cds = aObbNew.cd_cds
 		   and esercizio = aObbNew.esercizio
 		   and esercizio_ori_obbligazione = aObbNew.esercizio_originale
 		   and pg_obbligazione = aObbNew.pg_obbligazione
	       and ti_origine = CNRCTB001.GESTIONE_SPESE;
     cancellaFisicamente(aAccNew,aUser,aTSNow);
    end if;
   end if;

   delete from obbligazione where
               cd_cds = aObbNew.cd_cds
		   and esercizio = aObbNew.esercizio
		   and esercizio_originale = aObbNew.esercizio_originale
		   and pg_obbligazione = aObbNew.pg_obbligazione;
   -- il trigger BD_OBBLIGAZIONE cancella in cascata i livelli inferiori
  exception

   when CNRCTB048.ERR_FK_VIOLATED then
    rollback to savepoint ELIMINADOCOBB;
	-- nel caso di impegni residui non basta che l'importo sia stato
	-- portato a zero, Ã¨ necessario stornare il documento
	if aObbNew.cd_tipo_documento_cont = CNRCTB018.TI_DOC_IMP_RES then
		update obbligazione
		set stato_obbligazione = 'S',
			dt_cancellazione = trunc(aTSNow),
			utuv = aUser,
			duva = aTSNow,
			pg_ver_rec = pg_ver_rec + 1
		where cd_cds = aObbNew.cd_cds
		  and esercizio = aObbNew.esercizio
		  and esercizio_originale = aObbNew.esercizio_originale
		  and pg_obbligazione = aObbNew.pg_obbligazione;
	end if;

   when others then
    IBMERR001.RAISE_ERR_GENERICO('Errore in eliminazione fisica dell'' '||cnrutil.getLabelObbligazioneMin()||': '||CNRCTB035.getDesc(aObbNew));
  end;
 end;

 procedure cancellaFisicamente(aAccNew IN OUT accertamento%rowtype,aUser varchar2, aTSNow date) is
  aAccScadNew accertamento_scadenzario%rowtype;
  aAccSCadVoceNew accertamento_scad_voce%rowtype;
  aObbNew obbligazione%rowtype;
  aObbScadNew obbligazione_scadenzario%rowtype;
  aObbScadVoceNew obbligazione_scad_voce%rowtype;
 begin
  savepoint ELIMINADOCACC;
  begin
   CNRCTB035.lockDoc(aAccNew);
   if aAccNew.fl_pgiro = 'Y' then
    if CNRCTB035.isAprePgiro(aAccNew) and not (aAccNew.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES) then
	 CNRCTB035.GETPGIROCDS(aAccNew,aAccScadNew,aAccSCadVoceNew,aObbNew,aObbScadNew,aObbScadVoceNew);
	 delete from ass_obb_acr_pgiro where
               cd_cds = aAccNew.cd_cds
 		   and esercizio = aAccNew.esercizio
 		   and esercizio_ori_accertamento = aAccNew.esercizio_originale
 		   and pg_accertamento = aAccNew.pg_accertamento
	       and ti_origine = CNRCTB001.GESTIONE_ENTRATE;
     cancellaFisicamente(aObbNew,aUser,aTSNow);
    end if;
   end if;

   delete from accertamento where
              cd_cds = aAccNew.cd_cds
		   and esercizio = aAccNew.esercizio
		   and esercizio_originale = aAccNew.esercizio_originale
		   and pg_accertamento = aAccNew.pg_accertamento;
   -- il trigger BD_ACCERTAMENTO cancella in cascaca i livelli inferiori
  exception
   when CNRCTB048.ERR_FK_VIOLATED then
    rollback to savepoint ELIMINADOCACC;
	-- nel caso di accertamento residui non basta portare a zero
	-- l'importo, Ã¨ necessario impostare la data di cancellazione
	if aAccNew.cd_tipo_documento_cont = CNRCTB018.TI_DOC_ACC_RES then
	   update accertamento
	   set dt_cancellazione = trunc(aTSNow),
	   	   utuv = aUser,
		   duva = aTSNow,
		   pg_ver_rec = pg_ver_rec + 1
	   where cd_cds = aAccNew.cd_cds
	     and esercizio = aAccNew.esercizio
	     and esercizio_originale = aAccNew.esercizio_originale
		 and pg_accertamento = aAccNew.pg_accertamento;
	end if;
   when others then
    IBMERR001.RAISE_ERR_GENERICO('Errore in eliminazione fisica dell'' accertamento: '||CNRCTB035.getDesc(aAccNew));
  end;
 end;

-- deriportoPGiroCds
--
-- Riporto dall'esercizio successivo di annotazioni su partita di giro tipo Entrata/Spesa
--
-- pre-post-name: documento contabile su partita di giro non valido
-- pre: il documento non Ã¨ su partita di giro oppure non Ã¨ valido, (v. CNRCTB035.GETPGIROCDS/CNRCTB035.getPgiroCdsInv)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: documento riportato in nuovo esercizio non valido
-- pre: il documento collegato nel nuovo esercizio non esiste o esiste piÃ¹ di un documento nel nuovo esercizio
--	corrispondente al documento dato, (v. CNRCTB048.GETDOCRIPORTATO)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: documento contabile collegato non valido
-- pre: il documento collegato nel nuovo esercizio non Ã¨ su partita di giro oppure non Ã¨ valido, (v. CNRCTB035.GETPGIROCDS)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: annotazione su partita di giro non riportata o variata nell'esercizio successivo
-- pre: l'annotazione su partita di giro non risulta riportata o ha subito delle modifiche/variazioni
--	nell'esercizio successivo, (v. checkDeRiportaEsNext)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: documento contabile associato a documento ammin. o parzialmente pagata
-- pre: il documento contabile dell'esercizio successivo, risulta essere associato a documento amministrativo
--	oppure Ã¨ stato parzialmente pagato, (v. checkDeRiportaScadEsNext)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: il documento contabile apre una partita di giro con controparte non riscontrata
-- pre: l'annotazione su partita di giro non risulta riportata o ha subito delle modifiche/variazioni
--	nell'esercizio successivo, (v. checkDeRiportaEsNext)
-- post: viene sollevata un'eccezione
--
-- pre: il documento contabile dell'esercizio successivo, risulta essere associato a documento amministrativo
--	oppure Ã¨ stato parzialmente pagato, (v. checkDeRiportaScadEsNext)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: cancellazione fisica del documento contabile
-- pre: la cancellazione fisica del documento contabile genera un errore, (v. cancellaFisicamente)
-- post: viene sollevata un'eccezione
--
-- pre-post-name: Riporto dall'esercizio successivo di annotazioni su partita di giro tipo Entrata/Spesa
-- pre: Nessun'altra precondizione verificata
-- post: aggiorna le annotazioni in parte entrate/spese annullando il riporto sull'esercizio successivo.
--	Vengono aggiornati i documenti amministrativi collegati (v. CNRCTB048.aggiornaDocAmm)
--
-- Parametri:
-- aAcc (aObb) -> il documento da riportare
-- aUser -> utente che effettua la modifica
-- aTSNow -> la data di sistema

Procedure deriportoPGiroCds(aObb in out obbligazione%rowtype, aUser varchar2, aTSNow date) is

 aObbScad obbligazione_scadenzario%rowtype;
 aObbScadVoce obbligazione_scad_voce%rowtype;
 aAcc accertamento%rowtype;
 aAccScad accertamento_scadenzario%rowtype;
 aAccScadVoce accertamento_scad_voce%rowtype;
 aObbNext obbligazione%rowtype;
 aObbScadNext obbligazione_scadenzario%rowtype;
 aObbScadVoceNext obbligazione_scad_voce%rowtype;
 aAccNext accertamento%rowtype;
 aAccScadNext accertamento_scadenzario%rowtype;
 aAccScadVoceNext accertamento_scad_voce%rowtype;
 aImriscontratoAcc number;
 aprePgiro boolean;
 begin

	aprePgiro := CNRCTB035.isAprePgiro(aObb);
	if aprePgiro then
	   CNRCTB035.GETPGIROCDS(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);
	else
	   CNRCTB035.getPgiroCdsInv(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);
	end if;

	-- possibili casi:
	-- 1. obb apre pgiro 2003 con acr non riscontrato => obb apre pgiro 2004
	-- 2. obb apre pgiro tronca 2003 => obb apre pgiro tronca 2004
	-- 3. obb apre pgiro 2003 con acr riscontrato => obb apre pgiro tronca 2004
	-- 4. obb chiude pgiro 2003 con acr riscontrato => obb apre pgiro tronca 2004

	aObbNext := CNRCTB048.GETDOCRIPORTATO(aObb);
	CNRCTB035.GETPGIROCDS(aObbNext,aObbScadNext,aObbScadVoceNext,aAccNext,aAccScadNext,aAccScadVoceNext);

	aImriscontratoAcc := getIm_riscontrato(aAcc.esercizio,aAcc.cd_cds,aAcc.esercizio_originale,aAcc.pg_accertamento,aAcc.ti_gestione);

	-- verifiche per il riporta indietro
	CNRCTB048.checkDeRiportaEsNext(aObb,aObbNext);
	CNRCTB048.checkDeRiportaScadEsNext(aObb,aObbScad,aObbNext,aObbScadNext);

	if aprePgiro And aAcc.dt_cancellazione is Null And aImriscontratoAcc = 0 Then
	-- caso 1.
	   CNRCTB048.checkDeRiportaEsNext(aAcc,aAccNext);
	   CNRCTB048.checkDeRiportaScadEsNext(aAcc,aAccScad,aAccNext,aAccScadNext);
	end if;

	-- aggiornamento dei documenti amministrativi collegati
	if aObbScadNext.im_associato_doc_amm > 0 then
	   CNRCTB048.aggiornaDocAmm(aObbScadNext, aObbScad, aUser, aTSNow);
	end if;

	if aprePgiro
	   and aAcc.dt_cancellazione is null
	   and aImriscontratoAcc = 0
	   and aAccScadNext.im_associato_doc_amm > 0
	then
  	   CNRCTB048.aggiornaDocAmm(aAccScadNext,aAccScad,aUser,aTSNow);
	end if;

	update obbligazione_scadenzario
	set    im_associato_doc_amm = 0,
	       pg_ver_rec = pg_ver_rec +1
	where  cd_cds = aObbScadNext.cd_cds And
	       esercizio = aObbScadNext.esercizio And
	       esercizio_originale = aObbScadNext.esercizio_originale And
	       pg_obbligazione = aObbscadNext.pg_obbligazione And
	       pg_obbligazione_scadenzario = aObbScadNext.pg_obbligazione_scadenzario;

	update accertamento_scadenzario
	set    im_associato_doc_amm = 0,
	       pg_ver_rec = pg_ver_rec +1
	where  cd_cds = aAccScadNext.cd_cds And
	       esercizio = aAccScadNext.esercizio And
	       esercizio_originale = aAccScadNext.esercizio_originale And
	       pg_accertamento = aAccScadNext.pg_accertamento And
	       pg_accertamento_scadenzario = aAccScadNext.pg_accertamento_scadenzario;

	cnrctb035.ANNULLAOBBLIGAZIONE(aObbNext.cd_cds, aObbNext.esercizio,aObbNext.esercizio_originale,aObbNext.pg_obbligazione,aUser);

	cancellaFisicamente(aObbNext,aUser,aTSNOw);

	update obbligazione
	set    riportato = 'N',
	       duva = aTSNow,
	       utuv = aUser,
	       pg_ver_rec = pg_ver_rec + 1
	where  cd_cds   	   = aObb.cd_cds And
	       esercizio 	   = aObb.esercizio And
	       esercizio_originale = aObb.esercizio_originale And
	       pg_obbligazione     = aObb.pg_obbligazione;

	update accertamento
	set    riportato = 'N',
	       duva = aTSNow,
	       utuv = aUser,
	       pg_ver_rec = pg_ver_rec + 1
	where  cd_cds              = aAcc.cd_cds And
	       esercizio           = aAcc.esercizio And
	       esercizio_originale = aAcc.esercizio_originale And
	       pg_accertamento     = aAcc.pg_accertamento;
 End;

 procedure deriportoPGiroCds(aAcc in out accertamento%rowtype, aUser varchar2, aTSNow date) is
 aObb obbligazione%rowtype;
 aObbScad obbligazione_scadenzario%rowtype;
 aObbScadVoce obbligazione_scad_voce%rowtype;
 aAccScad accertamento_scadenzario%rowtype;
 aAccScadVoce accertamento_scad_voce%rowtype;
 aObbNext obbligazione%rowtype;
 aObbScadNext obbligazione_scadenzario%rowtype;
 aObbScadVoceNext obbligazione_scad_voce%rowtype;
 aAccNext accertamento%rowtype;
 aAccScadNext accertamento_scadenzario%rowtype;
 aAccScadVoceNext accertamento_scad_voce%rowtype;
 aImRiscontratoObb number;
 aprePgiro boolean;
 begin

	aprePgiro := CNRCTB035.isAprePgiro(aAcc);
	if aprePgiro then
	   CNRCTB035.GETPGIROCDS(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
	else
	   CNRCTB035.getPgiroCdsInv(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
	end if;

	-- possibili casi:
	-- 1. acr apre pgiro 2003 con obb non riscontrato => acr apre pgiro 2004
	-- 2. acr apre pgiro tronca 2003 => acr apre pgiro tronca 2004
	-- 3. acr apre pgiro 2003 con obb riscontrata => acr apre pgiro tronca 2004
	-- 4. acr chiude pgiro 2003 con obb riscontrato => acr apre pgiro tronca 2004
	aAccNext := CNRCTB048.GETDOCRIPORTATO(aAcc);
	CNRCTB035.GETPGIROCDS(aAccNext,aAccScadNext,aAccScadVoceNext,aObbNext,aObbScadNext,aObbScadVoceNext);

	aImRiscontratoObb := getIm_riscontrato(aObb.esercizio,aObb.cd_cds,aObb.esercizio_originale,aObb.pg_obbligazione,aObb.ti_gestione);

	-- verifiche per il riporta indietro
	CNRCTB048.checkDeRiportaEsNext(aAcc,aAccNext);
	CNRCTB048.checkDeRiportaScadEsNext(aAcc,aAccScad,aAccNext,aAccScadNext);
	if aprePgiro
	   and aObb.dt_cancellazione is null
	   and aImRiscontratoObb = 0
	then
	-- caso 1.
	   CNRCTB048.checkDeRiportaEsNext(aObb,aObbNext);
	   CNRCTB048.checkDeRiportaScadEsNext(aObb,aObbScad,aObbNext,aObbScadNext);
	end if;

	-- aggiornamento dei documenti amministrativi collegati
	if aAccScadNext.im_associato_doc_amm > 0 then
	   CNRCTB048.aggiornaDocAmm(aAccScadNext, aAccScad, aUser, aTSNow);
	end if;
	if aprePgiro
	   and aObb.dt_cancellazione is null
	   and aImRiscontratoObb = 0
	   and aObbScadNext.im_associato_doc_amm > 0
	then
		CNRCTB048.aggiornaDocAmm(aObbScadNext,aObbScad,aUser,aTSNow);
	end if;

	update accertamento_scadenzario
	set im_associato_doc_amm = 0,
		pg_ver_rec = pg_ver_rec +1
	where cd_cds = aAccScadNext.cd_cds
	  and esercizio = aAccScadNext.esercizio
	  and esercizio_originale = aAccScadNext.esercizio_originale
	  and pg_accertamento = aAccScadNext.pg_accertamento
	  and pg_accertamento_scadenzario = aAccScadNext.pg_accertamento_scadenzario;

	update obbligazione_scadenzario
	set im_associato_doc_amm = 0,
		pg_ver_rec = pg_ver_rec +1
	where cd_cds = aObbScadNext.cd_cds
	  and esercizio = aObbScadNext.esercizio
	  and esercizio_originale = aObbScadNext.esercizio_originale
	  and pg_obbligazione = aObbscadNext.pg_obbligazione
	  and pg_obbligazione_scadenzario = aObbScadNext.pg_obbligazione_scadenzario;

	cnrctb035.ANNULLAACCERTAMENTO(aAccNext.cd_cds, aAccNext.esercizio,aAccNext.esercizio_originale,aAccNext.pg_accertamento,aUser);

	cancellaFisicamente(aAccNext,aUser,aTSNow);

	update accertamento
	set riportato = 'N',
		duva = aTSNow,
		utuv = aUser,
		pg_ver_rec = pg_ver_rec + 1
	where cd_cds   	 	  = aAcc.cd_cds
	  and esercizio 	  = aAcc.esercizio
	  and esercizio_originale = aAcc.esercizio_originale
	  and pg_accertamento = aAcc.pg_accertamento;

	update obbligazione
	set riportato = 'N',
		duva = aTSNow,
		utuv = aUser,
		pg_ver_rec = pg_ver_rec + 1
	where cd_cds   	 	  = aObb.cd_cds
	  and esercizio 	  = aObb.esercizio
	  and esercizio_originale = aObb.esercizio_originale
	  and pg_obbligazione = aObb.pg_obbligazione;

 end;

 procedure deriportoEsNextObb(aObb in out obbligazione%rowtype, aUser varchar2, aTSNow date) is
  aObbNext obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
  CNRCTB035.lockDoc(aObb);

	-- verifico lo stato dell'esercizio
	cnrctb048.CHECKESERCIZIO(aObb.esercizio,aObb.cd_cds);

	if aObb.fl_pgiro = 'Y'  and not (aObb.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_IMP_RES, CNRCTB018.TI_DOC_IMP)) then
	    deriportoPGiroCds(aObb,aUser,aTSNow);
	else
		aObbNext := CNRCTB048.getDocRiportato(aObb);

    recParametriCNR := CNRUTL001.getRecParametriCnr(aObbNext.esercizio);

		CNRCTB035.lockDocFull(aObbNext);
		-- controlli per il deriporto
		CNRCTB048.checkDeRiportaEsNext(aObb,aObbNext);

		for aScadNext in (select * from obbligazione_scadenzario
		                  Where cd_cds 	= aObbNext.cd_cds   And
		                        esercizio = aObbNext.esercizio And
		                        esercizio_originale = aObbNext.esercizio_originale And
					pg_obbligazione = aObbNext.pg_obbligazione
					for update nowait) loop
		       aObbScad:=CNRCTB048.getOldScad(aObbNext,aScadNext);

		       -- Controlli per il deriporto
	           CNRCTB048.checkDeRiportaScadEsNext(aObb,aObbScad,aObbNext,aScadNext);

			   -- aggiornamento dei documenti amministrativi collegati
			   if aScadNext.im_associato_doc_amm > 0 then
		 		CNRCTB048.aggiornaDocAmm(aScadNext, aObbScad, aUser, aTSNow);
			   end if;
	           update obbligazione_scadenzario set
	                     im_associato_doc_amm = 0,
						 pg_ver_rec=pg_ver_rec+1
	           where
			             cd_cds = aScadNext.cd_cds
					 and esercizio = aScadNext.esercizio
					 and esercizio_originale = aScadNext.esercizio_originale
					 and pg_obbligazione = aScadNext.pg_obbligazione
					 and pg_obbligazione_scadenzario = aScadNext.pg_obbligazione_scadenzario;
			   -- ciclo sui dettagli per decrementare l'importo stanziamento iniziale nei saldi
			   if recParametriCNR.fl_nuovo_pdg='N' and aObbNext.CD_TIPO_DOCUMENTO_CONT = CNRCTB018.TI_DOC_IMP_RES then
			     for aDettScadNext in (select * from obbligazione_scad_voce
			   	   				     where cd_cds = aScadNext.cd_cds
									   and esercizio = aScadNext.esercizio
									   and esercizio_originale = aScadNext.esercizio_originale
									   and pg_obbligazione = aScadNext.pg_obbligazione
									   and pg_obbligazione_scadenzario = aScadNext.pg_obbligazione_scadenzario) loop
				   declare
				      aLSaldo voce_f_saldi_cmp%rowtype;
				   begin
				   		select * into aLSaldo
						from voce_f_saldi_cmp
						where cd_cds 		 	    = aDettScadNext.cd_cds
						  and esercizio 		    = aDettScadNext.esercizio
						  and ti_appartenenza 	    = aDettScadNext.ti_appartenenza
						  and ti_gestione	  	    = aDettScadNext.ti_gestione
						  and cd_voce				= aDettScadNext.cd_voce
						  and ti_competenza_residuo = CNRCTB054.TI_RESIDUI
						for update nowait;
						CNRCTB054.AGGIORNASTANZIAMENTORESIDUI(aLSaldo,0-aDettScadNext.im_voce,aUser,aTSNow);
				   exception when NO_DATA_FOUND then
						ibmerr001.RAISE_ERR_GENERICO('Saldo residuo non trovato per voce finanziaria'||aDettScadNext.cd_voce||', gestione '||aDettScadNext.ti_gestione||', esercizio'||aDettScadNext.esercizio);
				   end;
			     end loop; -- dettagli scadenze
			   end if;
		end loop; -- scadenze
	    CNRCTB035.ANNULLAOBBLIGAZIONE(aObbNext.cd_cds,aObbNext.esercizio,aObbNext.esercizio_originale,aObbNext.pg_obbligazione,aUser);

		-- Aggiorno la testata del documento come non riportato
		update obbligazione
		set riportato = 'N',
			duva = aTSNow,
			utuv = aUser,
			pg_ver_rec = pg_ver_rec + 1
		where cd_cds   	 	  = aObb.cd_cds
		  and esercizio 	  = aObb.esercizio
		  and esercizio_originale = aObb.esercizio_originale
		  and pg_obbligazione = aObb.pg_obbligazione;

	    -- Cerca di eliminare fisicamente il documento
		-- Se non riesce comunque il documento Ã¨ stato cancellato logicamente
	    cancellaFisicamente(aObbNext,aUser,aTSNow);
	end if;
 end;


 procedure deriportoEsNextAcc(aAcc in out accertamento%rowtype, aUser varchar2, aTSNow date) is
  aAccNext accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
    CNRCTB035.lockDoc(aAcc);

	-- verifico lo stato dell'esercizio
	cnrctb048.CHECKESERCIZIO(aAcc.esercizio,aAcc.cd_cds);

	if aAcc.fl_pgiro = 'Y'  and not (aAcc.cd_tipo_documento_cont in (CNRCTB018.TI_DOC_ACC_RES, CNRCTB018.TI_DOC_ACC)) then
	   deriportoPGiroCds(aAcc,aUser,aTSNow);
	else
		aAccNext:=CNRCTB048.getDocRiportato(aAcc);

    recParametriCNR := CNRUTL001.getRecParametriCnr(aAccNext.esercizio);

		CNRCTB035.lockDocFull(aAccNext);
		-- Controlli per il deriporto
	    CNRCTB048.checkDeRiportaEsNext(aAcc,aAccNext);

		for aScadNext in (select * from accertamento_scadenzario where
		                    cd_cds 			= aAccNext.cd_cds
					    and esercizio 		= aAccNext.esercizio
					    and esercizio_originale = aAccNext.esercizio_originale
						and pg_accertamento = aAccNext.pg_accertamento
					    for update nowait
					 ) loop
	           aAccScad:=CNRCTB048.getOldScad(aAccNext,aScadNext);

		       -- Controlli per il deriporto
	           CNRCTB048.checkDeRiportaScadEsNext(aAcc,aAccScad,aAccNext,aScadNext);

			   -- aggiornamento dei documenti amministrativi collegati
			   if aScadNext.im_associato_doc_amm > 0 then
		 		CNRCTB048.aggiornaDocAmm(aScadNext, aAccScad, aUser, aTSNow);
			   end if;

	           update accertamento_scadenzario set
	                     im_associato_doc_amm = 0,
						 pg_ver_rec=pg_ver_rec+1
	           where
			             cd_cds = aScadNext.cd_cds
					 and esercizio = aScadNext.esercizio
					 and esercizio_originale = aScadNext.esercizio_originale
					 and pg_accertamento = aScadNext.pg_accertamento
					 and pg_accertamento_scadenzario = aScadNext.pg_accertamento_scadenzario;
		end loop; -- scadenze
	    CNRCTB035.ANNULLAaccertamento(aAccNext.cd_cds,aAccNext.esercizio,aAccNext.esercizio_originale,aAccNext.pg_accertamento,aUser);

		-- Se Ã¨ un accertamento residuo, decremento lo stanziamento iniziale
		-- del saldo
		if recParametriCNR.fl_nuovo_pdg='N' and aAccNext.CD_TIPO_DOCUMENTO_CONT = CNRCTB018.TI_DOC_ACC_RES then
		   declare
		   	  lSaldo voce_f_saldi_cmp%rowtype;
		   begin
		   		select * into lSaldo
				from voce_f_saldi_cmp
				where cd_cds 		 	    = aAccNext.cd_cds
				  and esercizio 		    = aAccNext.esercizio
				  and ti_appartenenza 	    = aAccNext.ti_appartenenza
				  and ti_gestione	  	    = aAccNext.ti_gestione
				  and cd_voce				= aAccNext.cd_voce
				  and ti_competenza_residuo = CNRCTB054.TI_RESIDUI;
				CNRCTB054.AGGIORNASTANZIAMENTORESIDUI(lSaldo,0-aAccNext.im_accertamento,aUser,aTSNow);
		   exception when NO_DATA_FOUND then
				ibmerr001.RAISE_ERR_GENERICO('Saldo non trovato per voce finanziaria'||aAccNext.cd_voce||', gestione '||aAccNext.ti_gestione||', esercizio'||aAccNext.esercizio);
		   end;
		end if;

		-- Aggiorno la testata del documento come riportato
		update accertamento
		set riportato = 'N',
			duva = aTSNow,
			utuv = aUser,
			pg_ver_rec = pg_ver_rec + 1
		where cd_cds   	 	  = aAcc.cd_cds
		  and esercizio 	  = aAcc.esercizio
		  and esercizio_originale = aAcc.esercizio_originale
		  and pg_accertamento = aAcc.pg_accertamento;

	    -- Cerca di eliminare fisicamente il documento
		-- Se non riesce comunque il documento Ã¨ stato cancellato logicamente
	    cancellaFisicamente(aAccNext,aUser,aTSNow);
	end if;

 end;

end;
