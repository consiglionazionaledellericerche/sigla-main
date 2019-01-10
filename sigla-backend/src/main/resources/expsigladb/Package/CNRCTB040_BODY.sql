--------------------------------------------------------
--  DDL for Package Body CNRCTB040
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB040" is

 procedure creaAccertamentoPgiroInt(
  isControlloBloccante boolean,
  isTrunc boolean,
  aAcc IN OUT accertamento%rowtype,
  aAccScad IN OUT accertamento_scadenzario%rowtype,
  aObb OUT obbligazione%rowtype,
  aObbScad OUT obbligazione_scadenzario%rowtype,
  aDtScadenza date
 ) is
  aNumeratore number;
  aDettScadC accertamento_scad_voce%rowtype;
  aDettScadCList CNRCTB035.scadVoceListE;
  aDettScadenza obbligazione_scad_voce%rowtype;
  aDettScadenzaList CNRCTB035.scadVoceListS;
  aEV elemento_voce%rowtype;
  aEVContr elemento_voce%rowtype;
  aAssPGiro ass_partita_giro%rowtype;
  aCdCdr varchar2(30);
  aCdLa varchar2(10);
  aVoceF voce_f%rowtype;
  aVoceFContr voce_f%rowtype;
  aCdTerzoContropartita number(10);
  aCDS unita_organizzativa%rowtype;
  aCDSOrigine unita_organizzativa%rowtype;
  aAssDocPGiro ass_obb_acr_pgiro%rowtype;
  aTipoDocContr varchar2(10);
  aSaldoCdrLineaAcc voce_f_saldi_cdr_linea%Rowtype;
  aGAE_dedicata_CDS  linea_attivita%Rowtype;
  parametriCnr parametri_cnr%rowtype;
 begin
  if aAcc.fl_pgiro is null or aAcc.fl_pgiro <> 'Y' then
   IBMERR001.RAISE_ERR_GENERICO('Documento non su partita di giro');
  end if;
  if aAcc.cd_tipo_documento_cont is null or aAcc.cd_tipo_documento_cont not in (CNRCTB018.TI_DOC_ACC_PGIRO, CNRCTB018.TI_DOC_ACC,
                                                                                CNRCTB018.TI_DOC_ACC_PGIRO_RES,CNRCTB018.TI_DOC_ACC_RES) then
   IBMERR001.RAISE_ERR_GENERICO('L''accertamento che si sta tentando di generare ('||aAcc.cd_cds||'/'||aAcc.esercizio||'/'||aAcc.esercizio_originale||'/'||aAcc.pg_accertamento||
') ha una tipologia ('||aAcc.cd_tipo_documento_cont||') non compatibile.');
  end if;
  begin
   select * into aEV from elemento_voce where
        esercizio = aAcc.esercizio
	and ti_gestione = aAcc.ti_gestione
	and ti_appartenenza = aAcc.ti_appartenenza
    and cd_elemento_voce = aAcc.cd_elemento_voce
    and fl_partita_giro = 'Y';
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Voce del piano partita di giro di entrata non trovata');
  end;
 	aCDS:=CNRCTB020.GETCDSVALIDO(aAcc.esercizio, aAcc.cd_cds);
  aCDSOrigine:=CNRCTB020.GETCDSVALIDO(aAcc.esercizio, aAcc.cd_cds_origine);

  if(parametriCnr.fl_nuovo_pdg ='N' ) then
		  begin
		   select * into aVoceF from voce_f where
		        esercizio = aEV.esercizio
			and ti_gestione = aEV.ti_gestione
			and ti_appartenenza = aEV.ti_appartenenza
			and cd_voce = aAcc.cd_voce
			and fl_mastrino='Y';
		  exception when NO_DATA_FOUND then
		   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario partita di giro di entrata non trovato');
		  end;
	  if aCDS.cd_tipo_unita = CNRCTB020.TIPO_ENTE and aEV.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS then
	   IBMERR001.RAISE_ERR_GENERICO('La voce del piano specificata non è una voce del piano dell''ENTE');
	  end if;

	  if aCDS.cd_tipo_unita <> CNRCTB020.TIPO_ENTE and aEV.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR then
	   IBMERR001.RAISE_ERR_GENERICO('La voce del piano specificata non è una voce del piano di CDS');
	  end if;
 end if;
  if aAcc.esercizio != aAcc.esercizio_competenza then
   IBMERR001.RAISE_ERR_GENERICO('Generazione automatica di accertamento in esercizi futuri non supportata!');
  end if;
  If aObb.cd_tipo_documento_cont != CNRCTB018.TI_DOC_ACC_PGIRO_RES or aAcc.PG_accertamento is null Then
    aNumeratore    := CNRCTB018.getNextNumDocCont(aAcc.cd_tipo_documento_cont, aAcc.esercizio, aAcc.cd_cds, aAcc.utcr);
  else
    aNumeratore    := aAcc.PG_accertamento;
  end if;
  --Se l'esercizio originale non è valorizzato lo inizializzo con quello dell'esercizio
  If (aAcc.esercizio_originale Is Null) Then
     aAcc.esercizio_originale:=aAcc.esercizio;
  End If;

  aAcc.PG_accertamento:=aNumeratore;
  aAcc.dt_registrazione:=trunc(aAcc.dt_registrazione);
  aAcc.duva:=aAcc.dacr;
  aAcc.utuv:=aAcc.utcr;
  aAcc.pg_ver_rec:=1;
  CNRCTB035.INS_ACCERTAMENTO(aAcc);
  CNRCTB035.aggiornaSaldoDettScad(aAcc,aAcc.im_accertamento,false,aAcc.utcr,aAcc.dacr);

  aAccScad.cd_cds := aAcc.cd_cds;
  aAccScad.esercizio := aAcc.esercizio;
  aAccScad.esercizio_originale := aAcc.esercizio_originale;
  aAccScad.pg_accertamento:=aAcc.pg_accertamento;
  aAccScad.pg_accertamento_scadenzario:=1;
  aAccScad.dt_scadenza_emissione_fattura := TRUNC(nvl(aDtScadenza,aAcc.dt_registrazione));
  aAccScad.dt_scadenza_incasso := TRUNC(nvl(aDtScadenza,aAcc.dt_registrazione));
  aAccScad.ds_scadenza := aAcc.ds_accertamento;
  aAccScad.im_scadenza := aAcc.im_accertamento;
  aAccScad.im_associato_doc_amm := 0;
  aAccScad.im_associato_doc_contabile := 0;
  aAccScad.dacr:=aAcc.dacr;
  aAccScad.utcr:=aAcc.utcr;
  aAccScad.duva:=aAcc.dacr;
  aAccScad.utuv:=aAcc.utcr;
  aAccScad.pg_ver_rec:=1;

  If CNRCTB015.UtilizzaGAEdedicataPgiroCDS (aAcc.esercizio, aAcc.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_ENTRATE) Then
    aGAE_dedicata_CDS := CNRCTB015.get_LINEA_PGIRO_cds(aAcc.esercizio, aAcc.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_ENTRATE);
    aCdCdr := aGAE_dedicata_CDS.CD_CENTRO_RESPONSABILITA;
    aCdLa  := aGAE_dedicata_CDS.CD_LINEA_ATTIVITA;
  Else
    aCdCdr:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
    aCdLa:=CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_ENTRATA_ENTE);
  End If;

  aDettScadC.cd_cds := aAcc.cd_cds;
  aDettScadC.esercizio := aAcc.esercizio;
  aDettScadC.esercizio_originale := aAcc.esercizio_originale;
  aDettScadC.pg_accertamento:=aAcc.pg_accertamento;
  aDettScadC.pg_accertamento_scadenzario:=1;
  aDettScadC.cd_centro_responsabilita := aCdCdr;
  aDettScadC.cd_linea_attivita := aCdLa;
  aDettScadC.im_voce := aAcc.im_accertamento;
  aDettScadC.dacr:=aAcc.dacr;
  aDettScadC.utcr:=aAcc.utcr;
  aDettScadC.duva:=aAcc.dacr;
  aDettScadC.utuv:=aAcc.utcr;
  aDettScadC.pg_ver_rec:=1;
  aDettScadCList(1):=aDettScadC;
  -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  CNRCTB035.creaScadAccertamento(aAcc,aAccScad,1,aDettScadCList);

  -- gestione contropartita

  aCdTerzoContropartita:=CNRCTB015.GETIM01PERCHIAVE(CNRCTB035.TERZO_SPECIALE,CNRCTB035.CODICE_DIVERSI_PGIRO);

  begin
   select * into aAssPgiro from ass_partita_giro where
        esercizio = aAcc.esercizio
    and ti_appartenenza = aAcc.ti_appartenenza
    and ti_gestione = aAcc.ti_gestione
    and cd_voce = aAcc.cd_elemento_voce;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Conto di partita di giro associato non trovato');
  end;
  begin
   select * into aEVContr from elemento_voce where
        esercizio = aAssPgiro.esercizio
	and ti_gestione = aAssPgiro.ti_gestione_clg
	and ti_appartenenza = aAssPgiro.ti_appartenenza_clg
    and cd_elemento_voce = aAssPgiro.cd_voce_clg
    and fl_partita_giro = 'Y';
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Voce del piano partita di giro di controparte spesa non trovato');
  end;
	if(parametriCnr.fl_nuovo_pdg ='N' ) then
		  begin
		   select * into aVoceFContr from voce_f where
		        esercizio = aEVContr.esercizio
			and ti_gestione = aEvContr.ti_gestione
			and ti_appartenenza = aEVContr.ti_appartenenza
			and cd_titolo_capitolo = aEVContr.cd_elemento_voce
			and fl_mastrino='Y';
		  exception when NO_DATA_FOUND then
		   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario partita di giro di controparte spesa non trovato');
		  end;
	end if;
  If aCDS.cd_tipo_unita = CNRCTB020.TIPO_ENTE Then

   aObb.cd_cds_origine := aAcc.cd_cds;
   aObb.cd_uo_origine := aAcc.cd_unita_organizzativa;

   If aAcc.esercizio = aAcc.esercizio_originale Then
     aTipoDocContr := CNRCTB018.TI_DOC_IMP;
   Elsif aAcc.esercizio > aAcc.esercizio_originale Then
     aTipoDocContr := CNRCTB018.TI_DOC_IMP_RES;
   End If;
  Else

   aObb.cd_cds_origine := aAcc.cd_cds_origine;
   aObb.cd_uo_origine := aAcc.cd_uo_origine;

   If aAcc.esercizio = aAcc.esercizio_originale Then
     aTipoDocContr := CNRCTB018.TI_DOC_OBB_PGIRO;
     aNumeratore:=CNRCTB018.getNextNumDocCont(aTipoDocContr, aAcc.esercizio, aAcc.cd_cds, aAcc.utcr);
   Elsif aAcc.esercizio > aAcc.esercizio_originale Then
     aTipoDocContr := CNRCTB018.TI_DOC_OBB_PGIRO_RES;
     aNumeratore:=CNRCTB018.getNextNumDocCont(aTipoDocContr, aAcc.esercizio_originale, aAcc.cd_cds, aAcc.utcr);
   End If;

  End if;


  aObb.cd_cds := aAcc.cd_cds;
  aObb.esercizio := aAcc.esercizio;
  aObb.esercizio_competenza := aAcc.esercizio_competenza;
  aObb.cd_tipo_documento_cont:=aTipoDocContr;
  aObb.cd_unita_organizzativa := aAcc.cd_unita_organizzativa;
  aObb.esercizio_originale := aAcc.esercizio_originale;
  aObb.PG_OBBLIGAZIONE:=aNumeratore;
  aObb.dt_registrazione := aAcc.dt_registrazione;
  aObb.ds_obbligazione := aAcc.ds_accertamento;
  aObb.cd_terzo := aCdTerzoContropartita;
  aObb.ti_gestione:=aEVContr.ti_gestione;
  aObb.ti_appartenenza:=aEVContr.ti_appartenenza;
  aObb.cd_elemento_voce:=aEVContr.cd_elemento_voce;
  if isTrunc then
   aObb.im_obbligazione:=0;
   aObb.stato_obbligazione:=CNRCTB035.STATO_STORNATO;
   aObb.dt_cancellazione:=trunc(aAcc.dacr);
  else
   aObb.im_obbligazione:=aAcc.im_accertamento;
   aObb.stato_obbligazione:=CNRCTB035.STATO_DEFINITIVO;
  end if;
  aObb.im_costi_anticipati:=0;
  aObb.fl_calcolo_automatico:='N';
  aObb.fl_spese_costi_altrui:='N';
  aObb.fl_pgiro:='Y';
  aObb.riportato:='N';
  aObb.dacr:=aAcc.dacr;
  aObb.utcr:=aAcc.utcr;
  aObb.duva:=aAcc.dacr;
  aObb.utuv:=aAcc.utcr;
  aObb.pg_ver_rec:=1;

  CNRCTB035.INS_OBBLIGAZIONE(aObb);

  aObbScad.cd_cds := aObb.cd_cds;
  aObbScad.esercizio := aObb.esercizio;
  aObbScad.dt_scadenza := aAccScad.dt_scadenza_emissione_fattura;
  aObbScad.ds_scadenza := aObb.ds_obbligazione;
  aObbScad.im_scadenza := aObb.im_obbligazione;
  aObbScad.im_associato_doc_amm := 0;
  aObbScad.im_associato_doc_contabile := 0;
  aObbScad.dacr:=aObb.dacr;
  aObbScad.utcr:=aObb.utcr;
  aObbScad.duva:=aObb.dacr;
  aObbScad.utuv:=aObb.utcr;
  aObbScad.pg_ver_rec:=1;

  If CNRCTB015.UtilizzaGAEdedicataPgiroCDS (aObb.esercizio, aObb.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_SPESE) Then
    aGAE_dedicata_CDS := CNRCTB015.get_LINEA_PGIRO_cds(aObb.esercizio, aObb.CD_CDS_ORIGINE, CNRCTB001.GESTIONE_SPESE);
    aCdCdr := aGAE_dedicata_CDS.CD_CENTRO_RESPONSABILITA;
    aCdLa  := aGAE_dedicata_CDS.CD_LINEA_ATTIVITA;
  Else
    aCdCdr := CNRCTB015.GETVAL01PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_SPESA_ENTE);
    aCdLa  := CNRCTB015.GETVAL02PERCHIAVE(CNRCTB035.LA_SPECIALE,CNRCTB035.LA_SPESA_ENTE);
  End If;



  aDettScadenza.cd_cds := aObb.cd_cds;
  aDettScadenza.esercizio := aObb.esercizio;
  aDettScadenza.ti_appartenenza := aObb.ti_appartenenza;
  aDettScadenza.ti_gestione := aObb.ti_gestione;
  if(parametriCnr.fl_nuovo_pdg ='N' ) then
  	aDettScadenza.cd_voce := aVoceFContr.cd_voce;
  else
  	aDettScadenza.cd_voce := aEVContr.cd_elemento_voce;
  end if;
  aDettScadenza.cd_centro_responsabilita := aCdCdr;
  aDettScadenza.cd_linea_attivita := aCdLa;
  aDettScadenza.im_voce := aObb.im_obbligazione;
  aDettScadenza.dacr:=aObb.dacr;
  aDettScadenza.utcr:=aObb.utcr;
  aDettScadenza.duva:=aObb.dacr;
  aDettScadenza.utuv:=aObb.utcr;
  aDettScadenza.pg_ver_rec:=1;
  aDettScadenzaList(1):=aDettScadenza;
  CNRCTB035.creaScadObbligazione(aObb,aObbScad,1,aDettScadenzaList,false);

  aAssDocPGiro.cd_cds:=aObb.cd_cds;
  aAssDocPGiro.esercizio:=aObb.esercizio;
  aAssDocPGiro.esercizio_ori_obbligazione:=aObb.esercizio_originale;
  aAssDocPGiro.pg_obbligazione:=aObb.pg_obbligazione;
  aAssDocPGiro.esercizio_ori_accertamento:=aAcc.esercizio_originale;
  aAssDocPGiro.pg_accertamento:=aAcc.pg_accertamento;
  aAssDocPGiro.ti_origine:=CNRCTB001.GESTIONE_ENTRATE;
  aAssDocPGiro.dacr:=aObb.dacr;
  aAssDocPGiro.utcr:=aObb.utcr;
  aAssDocPGiro.duva:=aObb.dacr;
  aAssDocPGiro.utuv:=aObb.utcr;
  aAssDocPGiro.pg_ver_rec:=1;
  CNRCTB035.INS_ASS_OBB_ACR_PGIRO(aAssDocPGiro);
 end;

 procedure creaAccertamentoPgiro(
  isControlloBloccante boolean,
  aAcc IN OUT accertamento%rowtype,
  aAccScad IN OUT accertamento_scadenzario%rowtype,
  aObb OUT obbligazione%rowtype,
  aObbScad OUT obbligazione_scadenzario%rowtype,
  aDtScadenza date
 ) is
 begin
  creaAccertamentoPgiroInt(
   isControlloBloccante,
   false, -- no trunc controparte
   aAcc,
   aAccScad,
   aObb,
   aObbScad,
   aDtScadenza);
 end;

 procedure creaAccertamentoPgiroTronc(
  isControlloBloccante boolean,
  aAcc IN OUT accertamento%rowtype,
  aAccScad IN OUT accertamento_scadenzario%rowtype,
  aObb OUT obbligazione%rowtype,
  aObbScad OUT obbligazione_scadenzario%rowtype,
  aDtScadenza date
 ) is
 begin
  creaAccertamentoPgiroInt(
   isControlloBloccante,
   true, -- trunc controparte
   aAcc,
   aAccScad,
   aObb,
   aObbScad,
   aDtScadenza);
 end;



 procedure creaAccertamento(
  isControlloBloccante boolean,
  aAcc IN OUT accertamento%rowtype,
  aScadenza1 in out accertamento_scadenzario%rowtype,
  aDettScadenza1 in out CNRCTB035.scadVoceListE
 ) is
  aFakeAccScad accertamento_scadenzario%rowtype;
 begin
  creaAccertamento(
   isControlloBloccante,
   aAcc,
   aScadenza1,
   aDettScadenza1,
   aFakeAccScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_E,
   aFakeAccScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_E,
   aFakeAccScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_E,
   aFakeAccScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_E,
   aFakeAccScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_E,
   aFakeAccScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_E,
   aFakeAccScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_E,
   aFakeAccScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_E,
   aFakeAccScad,
   CNRCTB035.LISTA_SCAD_VOCE_VUOTA_E
 );
 end;

 -- Prepara un dettaglio di scadenza di accertamento CNR per CDS arricchito nel caso siano specificate linee di attività comuni

 procedure preparaDettaglioScadenza(aDettScadenza in out CNRCTB035.scadVoceListE, aEV elemento_voce%rowtype) is
  aLA linea_attivita%rowtype;
  aTipoLa tipo_linea_attivita%rowtype;
  aNewScadVoce accertamento_scad_voce%rowtype;
 begin
  for i in 1..aDettScadenza.count loop
   begin
    Select *
    Into   aLA
    From   linea_attivita a
    Where  cd_centro_responsabilita = aDettScadenza(i).cd_centro_responsabilita And
           cd_linea_attivita        = aDettScadenza(i).cd_linea_attivita And
           esercizio_inizio        <= Nvl(aDettScadenza(i).ESERCIZIO_ORIGINALE, aDettScadenza(i).ESERCIZIO) And
           esercizio_fine          >= Nvl(aDettScadenza(i).ESERCIZIO_ORIGINALE, aDettScadenza(i).ESERCIZIO) And
           exists (Select 1
	           From   ass_ev_ev
	           Where  esercizio = aDettScadenza(i).esercizio And
	                  ti_gestione = aEV.ti_gestione And
	                  ti_appartenenza = aEV.ti_appartenenza And
                          cd_elemento_voce = aEV.cd_elemento_voce And
                          cd_natura = a.cd_natura);
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Linea di attività: '||aDettScadenza(i).cd_linea_attivita||' non valida o con natura non compatibile con la voce del piano: '||aEV.cd_elemento_voce||' per l''esercizio '||aDettScadenza(i).ESERCIZIO_ORIGINALE);
   end;
   select * into aTipoLa from tipo_linea_attivita where
       cd_tipo_linea_attivita = aLA.cd_tipo_linea_attivita;

   -- Estrae tutte le linee di attività comuni finalizzate ai cdr
   if aTipoLa.ti_tipo_la = CNRCTB010.TI_TIPO_LA_COMUNE then
    for aLACom in (select * from linea_attivita where
	     cd_centro_responsabilita != aDettScadenza(i).cd_centro_responsabilita
	 and cd_tipo_linea_attivita = aLA.cd_tipo_linea_attivita
	 and esercizio_inizio <= aDettScadenza(i).esercizio_originale
	 and esercizio_fine >= aDettScadenza(i).esercizio_originale) loop
         aNewScadVoce:=aDettScadenza(i);
	 aNewScadVoce.im_voce:=0;
	 aNewScadVoce.cd_centro_responsabilita:=aLACom.cd_centro_responsabilita;
	 aDettScadenza(aDettScadenza.count+1):=aNewScadVoce;
	end loop;
   end if;
  end loop;
 end;


 procedure creaAccertamento(
  isControlloBloccante boolean,
  aAcc IN OUT accertamento%rowtype,
  aScadenza1 in out accertamento_scadenzario%rowtype,
  aDettScadenza1 in out CNRCTB035.scadVoceListE,
  aScadenza2 in out accertamento_scadenzario%rowtype,
  aDettScadenza2 in out CNRCTB035.scadVoceListE,
  aScadenza3 in out accertamento_scadenzario%rowtype,
  aDettScadenza3 in out CNRCTB035.scadVoceListE,
  aScadenza4 in out accertamento_scadenzario%rowtype,
  aDettScadenza4 in out CNRCTB035.scadVoceListE,
  aScadenza5 in out accertamento_scadenzario%rowtype,
  aDettScadenza5 in out CNRCTB035.scadVoceListE,
  aScadenza6 in out accertamento_scadenzario%rowtype,
  aDettScadenza6 in out CNRCTB035.scadVoceListE,
  aScadenza7 in out accertamento_scadenzario%rowtype,
  aDettScadenza7 in out CNRCTB035.scadVoceListE,
  aScadenza8 in out accertamento_scadenzario%rowtype,
  aDettScadenza8 in out CNRCTB035.scadVoceListE,
  aScadenza9 in out accertamento_scadenzario%rowtype,
  aDettScadenza9 in out CNRCTB035.scadVoceListE,
  aScadenza10 in out accertamento_scadenzario%rowtype,
  aDettScadenza10 in out CNRCTB035.scadVoceListE
 ) is
  aNumeratore number;
  aEV elemento_voce%rowtype;
  aCDSSAC unita_organizzativa%rowtype;
  aUOENTE unita_organizzativa%rowtype;
 Begin
  if aAcc.cd_tipo_documento_cont not in (CNRCTB018.TI_DOC_ACC) then
   IBMERR001.RAISE_ERR_GENERICO('L''accertamento che si sta tentando di generare ('||aAcc.cd_cds||'/'||aAcc.esercizio||'/'||aAcc.esercizio_originale||'/'||aAcc.pg_accertamento||
') ha una tipologia ('||aAcc.cd_tipo_documento_cont||') non compatibile.');
  end if;
  if aAcc.fl_pgiro is null or aAcc.fl_pgiro = 'Y' then
   IBMERR001.RAISE_ERR_GENERICO('L''accertamento che si sta tentando di generare ('||aAcc.cd_cds||'/'||aAcc.esercizio||'/'||aAcc.esercizio_originale||'/'||aAcc.pg_accertamento||
') è una partita di giro e quindi non è non compatibile.');
  end if;

  if aAcc.esercizio != aAcc.esercizio_competenza then
   IBMERR001.RAISE_ERR_GENERICO('Generazione automatica di accertamenti in esercizi futuri non supportata');
  end if;

  aUOENTE:=CNRCTB020.GETUOENTE(aAcc.esercizio);

  if
       aAcc.cd_cds is null
	or aAcc.cd_cds_origine is null
	or aAcc.cd_unita_organizzativa is null
	or aAcc.cd_uo_origine is null
	or aAcc.cd_cds <> aUOENTE.cd_unita_padre
	or aAcc.cd_unita_organizzativa <> aUOENTE.cd_unita_organizzativa
	or aAcc.cd_uo_origine = aAcc.cd_unita_organizzativa
	or aAcc.cd_cds_origine = aAcc.cd_cds
	or aAcc.ti_gestione <> CNRCTB001.GESTIONE_ENTRATE
	or aAcc.ti_appartenenza <> CNRCTB001.APPARTENENZA_CNR
  then
   IBMERR001.RAISE_ERR_GENERICO('Documento non compatibile (d)');
  end if;

  if aAcc.esercizio != aAcc.esercizio_competenza then
   IBMERR001.RAISE_ERR_GENERICO('Generazione automatica di accertamento in esercizi futuri non supportata');
  end if;

  aCDSSAC:=CNRCTB020.GETCDSSACVALIDO(aAcc.esercizio);

  -- Estrazione della voce del piano

  begin
   select * into aEV from elemento_voce where
        esercizio = aAcc.esercizio
	and ti_gestione = aAcc.ti_gestione
	and ti_appartenenza = aAcc.ti_appartenenza
    and cd_elemento_voce = aAcc.cd_elemento_voce
    and fl_partita_giro = 'N'
	and (
	    fl_voce_sac = 'N'
	 or fl_voce_sac = 'Y' and (aCDSSAC.cd_unita_organizzativa = aAcc.cd_cds_origine)
	);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Conto di partita di giro non trovato');
  end;

  -- Check di compatibilità tra natura linea di attività ed elemento voce

  preparaDettaglioScadenza(aDettScadenza1,aEV);
  preparaDettaglioScadenza(aDettScadenza2,aEV);
  preparaDettaglioScadenza(aDettScadenza3,aEV);
  preparaDettaglioScadenza(aDettScadenza4,aEV);
  preparaDettaglioScadenza(aDettScadenza5,aEV);
  preparaDettaglioScadenza(aDettScadenza6,aEV);
  preparaDettaglioScadenza(aDettScadenza7,aEV);
  preparaDettaglioScadenza(aDettScadenza8,aEV);
  preparaDettaglioScadenza(aDettScadenza9,aEV);
  preparaDettaglioScadenza(aDettScadenza10,aEV);

  aNumeratore:=CNRCTB018.getNextNumDocCont(aAcc.cd_tipo_documento_cont, aAcc.esercizio, aAcc.cd_cds, aAcc.utcr);

  --Se l'esercizio originale non è valorizzato lo inizializzo con quello dell'esercizio
  If (aAcc.esercizio_originale Is Null) Then
     aAcc.esercizio_originale:=aAcc.esercizio;
  End If;

  aAcc.PG_accertamento:=aNumeratore;
  aAcc.dt_registrazione:=trunc(aAcc.dt_registrazione);
  aAcc.duva:=aAcc.dacr;
  aAcc.utuv:=aAcc.utcr;
  aAcc.pg_ver_rec:=1;
  CNRCTB035.INS_accertamento(aAcc);
  CNRCTB035.aggiornaSaldoDettScad(aAcc,aAcc.im_accertamento,isControlloBloccante,aAcc.utcr,aAcc.dacr);
  -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  CNRCTB035.creaScadAccertamento(aAcc,aScadenza1,1,aDettScadenza1);
  CNRCTB035.creaScadAccertamento(aAcc,aScadenza2,2,aDettScadenza2);
  CNRCTB035.creaScadAccertamento(aAcc,aScadenza3,3,aDettScadenza3);
  CNRCTB035.creaScadAccertamento(aAcc,aScadenza4,4,aDettScadenza4);
  CNRCTB035.creaScadAccertamento(aAcc,aScadenza5,5,aDettScadenza5);
  CNRCTB035.creaScadAccertamento(aAcc,aScadenza6,6,aDettScadenza6);
  CNRCTB035.creaScadAccertamento(aAcc,aScadenza7,7,aDettScadenza7);
  CNRCTB035.creaScadAccertamento(aAcc,aScadenza8,8,aDettScadenza8);
  CNRCTB035.creaScadAccertamento(aAcc,aScadenza9,9,aDettScadenza9);
  CNRCTB035.creaScadAccertamento(aAcc,aScadenza10,10,aDettScadenza10);
 end;
end;
