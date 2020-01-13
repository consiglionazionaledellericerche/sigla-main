--------------------------------------------------------
--  DDL for Package Body CNRCTB043
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB043" is
 procedure modificaPraticaObb(aEs number,aCdCds varchar2,aEsOri number,aPgObb number,aImDelta number,aTSNow date,aUser VARCHAR2,aAccConScad CHAR Default 'N', aggiornaDocGenerico CHAR Default 'S') is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAssPGiro ass_obb_acr_pgiro%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aDocGen documento_generico%rowtype;
  aDocGenRiga documento_generico_riga%rowtype;
  aRev reversale%rowtype;
  aRevRiga reversale_riga%rowtype;
  aSaldoCdrLineaAcc voce_f_saldi_cdr_linea%Rowtype;
  im1 NUMBER;
  im2 NUMBER;
  im3 NUMBER;
 begin
  aObb.esercizio:=aEs;
  aObb.cd_cds:=aCdCds;
  aObb.esercizio_originale:=aEsOri;
  aObb.pg_obbligazione:=aPgObb;
  -- Carica la partita di giro a aprtire dalla controparte della parte principale
  If aAccConScad = 'N' Then             -- da liquidazione IVA
     CNRCTB035.GETPGIROCDSINV(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);
  Else                                  -- da liquidazione CORI
     CNRCTB035.GETPGIROCDSINV(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce,aAccConScad,aTSNow,aUser);
  End If;

  if aObbScad.im_associato_doc_amm <> 0 or aObbScad.im_associato_doc_contabile <> 0 then
   IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' di versamento CORI al centro giא utilizzata '||CNRCTB035.getDesc(aObb));
  end if;

  if aObb.stato_obbligazione <> CNRCTB035.STATO_STORNATO then
   update obbligazione
    set
     IM_OBBLIGAZIONE=IM_OBBLIGAZIONE+aImDelta,
 	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aObbScadVoce.cd_cds
     and ESERCIZIO=aObbScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
     and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione;

   update obbligazione_scadenzario
    set
     IM_SCADENZA=IM_SCADENZA+aImDelta,
	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aObbScadVoce.cd_cds
     and ESERCIZIO=aObbScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
     and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione
	 and PG_OBBLIGAZIONE_SCADENZARIO=aObbScadVoce.pg_obbligazione_scadenzario;

   update obbligazione_scad_voce
    set
     IM_VOCE=IM_VOCE+aImDelta,
 	 UTUV=aUser,
 	 DUVA=aTSNow,
 	 PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aObbScadVoce.cd_cds
    and ESERCIZIO=aObbScadVoce.esercizio
    and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
    and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione
	and PG_OBBLIGAZIONE_SCADENZARIO=aObbScadVoce.pg_obbligazione_scadenzario;
   -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
   CNRCTB035.aggiornaSaldoDettScad(aObb,aObbScadVoce,aImDelta,false,aUser, aTSNow);
  end if;

  -- Aggiorno la pratica parte entrata

  -- Se l'accertamento risulta cancellato bloccco l'operazione
  if aAcc.dt_cancellazione is not null then
   IBMERR001.RAISE_ERR_GENERICO('L''accertamento che ha aperto la partita di giro risulta cancellato: '||CNRCTB035.getDesc(aAcc));
  end if;

  update accertamento
   set
    IM_ACCERTAMENTO=IM_ACCERTAMENTO+aImDelta,
	   UTUV=aUser,
	   DUVA=aTSNow,
	   PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aAcc.cd_cds
    and ESERCIZIO=aAcc.esercizio
    and ESERCIZIO_ORIGINALE=aAcc.esercizio_originale
    and PG_ACCERTAMENTO=aAcc.pg_accertamento;

  -- poichט possono esserci piש scadenze, devo controllare che se aImDelta ט negativo (annullamento liq)
  -- la modifica non deve rendere la scadenza negativa
  Select IM_SCADENZA+aImDelta, IM_ASSOCIATO_DOC_AMM+aImDelta, IM_ASSOCIATO_DOC_CONTABILE+aImDelta
  Into im1, im2, im3
  From accertamento_scadenzario
  Where CD_CDS=aAcc.cd_cds
    and ESERCIZIO=aAcc.esercizio
    and ESERCIZIO_ORIGINALE=aAcc.esercizio_originale
    and PG_ACCERTAMENTO=aAcc.pg_accertamento
    And pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;
  If im1 < 0 Or im2 < 0 Or im3 < 0 Then
     IBMERR001.RAISE_ERR_GENERICO('La modifica rende negativa la scadenza '||To_Char(aAccScad.pg_accertamento_scadenzario)||' dell''accertamento : '||CNRCTB035.getDesc(aAcc));
  End If;

  update accertamento_scadenzario
   set
    IM_SCADENZA=IM_SCADENZA+aImDelta,
	   IM_ASSOCIATO_DOC_AMM=IM_ASSOCIATO_DOC_AMM+aImDelta,
	   IM_ASSOCIATO_DOC_CONTABILE=IM_ASSOCIATO_DOC_CONTABILE+aImDelta,
	   UTUV=aUser,
	   DUVA=aTSNow,
	   PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aAcc.cd_cds
    and ESERCIZIO=aAcc.esercizio
    and ESERCIZIO_ORIGINALE=aAcc.esercizio_originale
    and PG_ACCERTAMENTO=aAcc.pg_accertamento
    And pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;

  Select IM_VOCE+aImDelta
  Into im1
  From accertamento_scad_voce
  Where CD_CDS=aAcc.cd_cds
    And ESERCIZIO=aAcc.esercizio
    And ESERCIZIO_ORIGINALE=aAcc.esercizio_originale
    And PG_ACCERTAMENTO=aAcc.pg_accertamento
    And pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;

  If im1 < 0 Then
     IBMERR001.RAISE_ERR_GENERICO('La modifica rende negativa la scadenza '||To_Char(aAccScad.pg_accertamento_scadenzario)||' dell''accertamento : '||CNRCTB035.getDesc(aAcc));
  End If;

  update accertamento_scad_voce
   set
    IM_VOCE=IM_VOCE+aImDelta,
	   UTUV=aUser,
	   DUVA=aTSNow,
	   PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aAcc.cd_cds
    and ESERCIZIO=aAcc.esercizio
    and ESERCIZIO_ORIGINALE=aAcc.esercizio_originale
    and PG_ACCERTAMENTO=aAcc.pg_accertamento
    And pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;

  -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  For aAccScad_voce in (select * from accertamento_scad_voce
                        Where cd_cds = aAcc.cd_cds
 	   		  And esercizio = aAcc.esercizio
 	   		  And esercizio_originale = aAcc.esercizio_originale
 	   		  And pg_accertamento = aAcc.pg_accertamento
 	   		  And pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario) Loop
    aSaldoCdrLineaAcc.ESERCIZIO := aAccScad_voce.ESERCIZIO;
    aSaldoCdrLineaAcc.ESERCIZIO_RES := aAccScad_voce.ESERCIZIO_ORIGINALE;
    aSaldoCdrLineaAcc.CD_CENTRO_RESPONSABILITA := aAccScad_voce.CD_CENTRO_RESPONSABILITA;
    aSaldoCdrLineaAcc.CD_LINEA_ATTIVITA := aAccScad_voce.CD_LINEA_ATTIVITA;
    aSaldoCdrLineaAcc.TI_APPARTENENZA := aAcc.TI_APPARTENENZA;
    aSaldoCdrLineaAcc.TI_GESTIONE := aAcc.TI_GESTIONE;
    aSaldoCdrLineaAcc.CD_VOCE := aAcc.CD_VOCE;

    CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLineaAcc);

    aSaldoCdrLineaAcc.UTUV := aAcc.utcr;

         If aAcc.esercizio = aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_ACC_COMP := aImDelta;
         Elsif aAcc.esercizio > aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_RES_PRO := aImDelta;
         End If;

    CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLineaAcc, '043.modificaPraticaObb', 'N');

  End Loop;
  CNRCTB035.aggiornaSaldoDettScad(aAcc,aImDelta,false,aUser, aTSNow);
  
  if aggiornaDocGenerico = 'S' Then

    begin
     select * into aDocGenRiga from documento_generico_riga where
         	    cd_cds = aAcc.cd_cds
  	   	and esercizio = aAcc.esercizio
  	   	and cd_unita_organizzativa = aAcc.cd_unita_organizzativa
  	   	and cd_cds_accertamento = aAcc.cd_cds
  	   	and esercizio_accertamento = aAcc.esercizio
  	   	and esercizio_ori_accertamento=aAcc.esercizio_originale
  	   	and pg_accertamento = aAcc.pg_accertamento
  	   	and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario
  	   for update nowait;
     select * into aDocGen from documento_generico where
         	    cd_cds = aDocGenRiga.cd_cds
  	   	and esercizio = aDocGenRiga.esercizio
  	   	and cd_unita_organizzativa = aDocGenRiga.cd_unita_organizzativa
  	   	and cd_tipo_documento_amm = aDocGenRiga.cd_tipo_documento_amm
  	   	and pg_documento_generico = aDocGenRiga.pg_documento_generico
  	   for update nowait;
    exception
     when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('Documento generico non trovato');
     when TOO_MANY_ROWS then
      IBMERR001.RAISE_ERR_GENERICO('Documento generico con piש di una riga non compatibile');
    end;

    -- Aggiornamento Generico

    update documento_generico set
     im_totale = im_totale + aImDelta,
     utuv = aUser,
     duva = aTSNow,
     pg_ver_rec = pg_ver_rec + 1
    where
         cd_tipo_documento_amm = aDocGenRiga.cd_tipo_documento_amm
     and cd_cds = aDocGenRiga.cd_cds
     and esercizio = aDocGenRiga.esercizio
     and cd_unita_organizzativa = aDocGenRiga.cd_unita_organizzativa
     and pg_documento_generico = aDocGenRiga.pg_documento_generico;

    update documento_generico_riga set
     im_riga = im_riga + aImDelta,
     im_riga_divisa = im_riga_divisa + aImDelta,
     utuv = aUser,
     duva = aTSNow,
     pg_ver_rec = pg_ver_rec + 1
    where
         cd_tipo_documento_amm = aDocGenRiga.cd_tipo_documento_amm
     and cd_cds = aDocGenRiga.cd_cds
     and esercizio = aDocGenRiga.esercizio
     and cd_unita_organizzativa = aDocGenRiga.cd_unita_organizzativa
     and pg_documento_generico = aDocGenRiga.pg_documento_generico
     and progressivo_riga = aDocGenRiga.progressivo_riga;

    -- Aggiornamento Reversale provvisoria

    begin
     select * into aRevRiga from reversale_riga where
             cd_cds = aAccScad.cd_cds
  	      and esercizio = aAccScad.esercizio
  	      and esercizio_ori_accertamento=aAccScad.esercizio_originale
  	      and pg_accertamento = aAccScad.pg_accertamento
  	      and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario
     for update nowait;
     select * into aRev from reversale where
             esercizio = aRevRiga.esercizio
  	      and cd_cds = aRevRiga.cd_cds
  	      and pg_reversale = aRevRiga.pg_reversale
  	      and cd_tipo_documento_cont=CNRCTB018.TI_DOC_REV_PROVV
     for update nowait;
    exception
     when NO_DATA_FOUND then

      Declare
        count_rev NUMBER;
        tutta_rev  reversale%Rowtype;
      Begin
         Select Count(0) into count_rev
         from reversale_riga
         Where cd_cds = aAccScad.cd_cds
      	   and esercizio = aAccScad.esercizio
      	   and esercizio_ori_accertamento=aAccScad.esercizio_originale
      	   and pg_accertamento = aAccScad.pg_accertamento
      	   and pg_accertamento_scadenzario = aAccScad.pg_accertamento_scadenzario;

          If count_rev = 0 Then
             IBMERR001.RAISE_ERR_GENERICO('Non esiste alcuna Reversale sull''accertamento '||
                                          aAccScad.cd_cds||'/'||
                                          aAccScad.esercizio||'/'||
                                          aAccScad.esercizio_originale||'/'||
                                          aAccScad.pg_accertamento||'/'||
                                          aAccScad.pg_accertamento_scadenzario);
          End If;

         select * into tutta_rev
         from reversale
         Where   esercizio = aRevRiga.esercizio
      	   and cd_cds = aRevRiga.cd_cds
      	   and pg_reversale = aRevRiga.pg_reversale;

         If tutta_rev.cd_tipo_documento_cont != CNRCTB018.TI_DOC_REV_PROVV Then
              IBMERR001.RAISE_ERR_GENERICO('La reversale '||CNRCTB038.getDesc(tutta_rev)||
              ' ('||tutta_rev.DS_REVERSALE||') non risulta Provvisoria');
         End If;

  -- per eventuali (non dovrebbero essercene) altri casi non gestiti lascio il messaggio
  -- vecchio (stani 28.04.2005)

        IBMERR001.RAISE_ERR_GENERICO('Reversale Provvisoria '||CNRCTB038.getDesc(tutta_rev)||
              ' non trovata.');

      End;

     when TOO_MANY_ROWS then
      IBMERR001.RAISE_ERR_GENERICO('Reversale con piש di una riga non compatibile '||CNRCTB038.getDesc(aRev));
    end;
    aRev.im_reversale:=aRev.im_reversale+aImDelta;
    if aRev.im_reversale < 0 then
      IBMERR001.RAISE_ERR_GENERICO('La modifica richiesta rende negativa la reversale '||CNRCTB038.getDesc(aRev));
     end if;
     CNRCTB037.modificaRevProvvPgiro(aRev, aTSNow, aUser);
   END IF;  
 end;



 procedure modificaPraticaAcc(aEs number,aCdCds varchar2,aEsOri number,aPgAcc number,aImDelta number,aTSNow date,aUser varchar2) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAssPGiro ass_obb_acr_pgiro%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aDocGen documento_generico%rowtype;
  aDocGenRiga documento_generico_riga%rowtype;
  aRev reversale%rowtype;
  aRevRiga reversale_riga%rowtype;
  aSaldoCdrLineaAcc voce_f_saldi_cdr_linea%Rowtype;
 begin
  aAcc.esercizio:=aEs;
  aAcc.cd_cds:=aCdCds;
  aAcc.esercizio_originale:=aEsOri;
  aAcc.pg_accertamento:=aPgAcc;
  -- Carica la partita di giro a aprtire dalla controparte della parte principale
  CNRCTB035.GETPGIROCDSINV(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);

  if aAccScad.im_associato_doc_amm <> 0 or aAccScad.im_associato_doc_contabile <> 0 then
   IBMERR001.RAISE_ERR_GENERICO(cnrutil.getLabelObbligazione()||' di versamento CORI al centro giא utilizzata '||CNRCTB035.getDesc(aObb));
  end if;

  if aAcc.dt_cancellazione is null then
   update accertamento
   set
    IM_ACCERTAMENTO=IM_ACCERTAMENTO+aImDelta,
    UTUV=aUser,
    DUVA=aTSNow,
    PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aAcc.cd_cds
    and ESERCIZIO=aAcc.esercizio
    and ESERCIZIO_ORIGINALE=aAcc.esercizio_originale
    and PG_ACCERTAMENTO=aAcc.pg_accertamento;

   update accertamento_scadenzario
   set
    IM_SCADENZA=IM_SCADENZA+aImDelta,
    IM_ASSOCIATO_DOC_AMM=IM_ASSOCIATO_DOC_AMM+aImDelta,
    IM_ASSOCIATO_DOC_CONTABILE=IM_ASSOCIATO_DOC_CONTABILE+aImDelta,
    UTUV=aUser,
    DUVA=aTSNow,
    PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aAcc.cd_cds
    and ESERCIZIO=aAcc.esercizio
    and ESERCIZIO_ORIGINALE=aAcc.esercizio_originale
    and PG_ACCERTAMENTO=aAcc.pg_accertamento;

   update accertamento_scad_voce
   set
    IM_VOCE=IM_VOCE+aImDelta,
    UTUV=aUser,
    DUVA=aTSNow,
    PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aAcc.cd_cds
    and ESERCIZIO=aAcc.esercizio
    and ESERCIZIO_ORIGINALE=aAcc.esercizio_originale
    and PG_ACCERTAMENTO=aAcc.pg_accertamento;
  -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  For aAccScad_voce in (select * from accertamento_scad_voce
                        Where cd_cds = aAcc.cd_cds
 			  And esercizio = aAcc.esercizio
 			  And esercizio_originale = aAcc.esercizio_originale
 			  And pg_accertamento = aAcc.pg_accertamento) Loop
    aSaldoCdrLineaAcc.ESERCIZIO := aAccScad_voce.ESERCIZIO;
    aSaldoCdrLineaAcc.ESERCIZIO_RES := aAccScad_voce.ESERCIZIO_ORIGINALE;
    aSaldoCdrLineaAcc.CD_CENTRO_RESPONSABILITA := aAccScad_voce.CD_CENTRO_RESPONSABILITA;
    aSaldoCdrLineaAcc.CD_LINEA_ATTIVITA := aAccScad_voce.CD_LINEA_ATTIVITA;
    aSaldoCdrLineaAcc.TI_APPARTENENZA := aAcc.TI_APPARTENENZA;
    aSaldoCdrLineaAcc.TI_GESTIONE := aAcc.TI_GESTIONE;
    aSaldoCdrLineaAcc.CD_VOCE := aAcc.CD_VOCE;

    CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLineaAcc);

    aSaldoCdrLineaAcc.UTUV := aAcc.utcr;

         If aAcc.esercizio = aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_ACC_COMP := aImDelta;
         Elsif aAcc.esercizio > aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_RES_PRO := aImDelta;
         End If;

    CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLineaAcc, '043.modificaPraticaAcc', 'N');
  End Loop;

   CNRCTB035.aggiornaSaldoDettScad(aAcc,aImDelta,false,aUser, aTSNow);
  end if;
  -- Aggiorno la pratica parte spesa

  -- Se l'obbligazione che ha aperto la partita di giro risulta cancellata bloccco l'operazione
  if aObb.stato_obbligazione = CNRCTB035.STATO_STORNATO then
   IBMERR001.RAISE_ERR_GENERICO('L'''||cnrutil.getLabelObbligazioneMin()||' che ha aperto la partita di giro risulta cancellata: '||CNRCTB035.getDesc(aObb));
  end if;

  update obbligazione
    set
     IM_OBBLIGAZIONE=IM_OBBLIGAZIONE+aImDelta,
 	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aObbScadVoce.cd_cds
     and ESERCIZIO=aObbScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
     and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione;

  update obbligazione_scadenzario
    set
     IM_SCADENZA=IM_SCADENZA+aImDelta,
	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aObbScadVoce.cd_cds
     and ESERCIZIO=aObbScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
     and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione
	 and PG_OBBLIGAZIONE_SCADENZARIO=aObbScadVoce.pg_obbligazione_scadenzario;

  update obbligazione_scad_voce
    set
     IM_VOCE=IM_VOCE+aImDelta,
 	 UTUV=aUser,
 	 DUVA=aTSNow,
 	 PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aObbScadVoce.cd_cds
    and ESERCIZIO=aObbScadVoce.esercizio
    and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
    and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione
	and PG_OBBLIGAZIONE_SCADENZARIO=aObbScadVoce.pg_obbligazione_scadenzario;
  -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  CNRCTB035.aggiornaSaldoDettScad(aObb,aObbScadVoce,aImDelta,false,aUser, aTSNow);

 end;

 procedure troncaPraticaAccPgiro(aEs number,aCdCds varchar2,aEsOri number,aPgAcc number,aTSNow date,aUser varchar2) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAssPGiro ass_obb_acr_pgiro%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
 begin
  aAcc.esercizio:=aEs;
  aAcc.cd_cds:=aCdCds;
  aAcc.esercizio_originale:=aEsOri;
  aAcc.pg_accertamento:=aPgAcc;
  -- Carica la partita di giro
  CNRCTB035.GETPGIROCDS(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
  if aObbScad.im_associato_doc_amm <> 0 or aObbScad.im_associato_doc_contabile <> 0 then
    If cnrutil.isLabelObbligazione() Then
      IBMERR001.RAISE_ERR_GENERICO('Obbligazione da troncare giא utilizzata '||CNRCTB035.getDesc(aObb));
    Else
      IBMERR001.RAISE_ERR_GENERICO('Impegno da troncare giא utilizzato '||CNRCTB035.getDesc(aObb));
    End If;
  end if;

  if aObb.stato_obbligazione = CNRCTB035.STATO_STORNATO then
    If cnrutil.isLabelObbligazione() Then
      IBMERR001.RAISE_ERR_GENERICO('Obbligazione da troncare giא stornata '||CNRCTB035.getDesc(aObb));
    Else
      IBMERR001.RAISE_ERR_GENERICO('Impegno da troncare giא stornato '||CNRCTB035.getDesc(aObb));
    End If;
  end if;
  update obbligazione
    set
     IM_OBBLIGAZIONE=0,
	 STATO_OBBLIGAZIONE=CNRCTB035.STATO_STORNATO,
	 DT_CANCELLAZIONE=trunc(aAcc.dacr),
 	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aObbScadVoce.cd_cds
     and ESERCIZIO=aObbScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
     and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione;

  update obbligazione_scadenzario
    set
     IM_SCADENZA=0,
	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aObbScadVoce.cd_cds
     and ESERCIZIO=aObbScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
     and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione
	 and PG_OBBLIGAZIONE_SCADENZARIO=aObbScadVoce.pg_obbligazione_scadenzario;

  update obbligazione_scad_voce
    set
     IM_VOCE=0,
 	 UTUV=aUser,
 	 DUVA=aTSNow,
 	 PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aObbScadVoce.cd_cds
    and ESERCIZIO=aObbScadVoce.esercizio
    and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
    and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione
	and PG_OBBLIGAZIONE_SCADENZARIO=aObbScadVoce.pg_obbligazione_scadenzario;
  -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  CNRCTB035.aggiornaSaldoDettScad(aObb,aObbScadVoce,0-aObbScadVoce.im_voce,false,aUser, aTSNow);
 end;

procedure troncaPraticaAccPgiroInv(aEs number,aCdCds varchar2,aEsOri number,aPgAcc number,aTSNow date,aUser varchar2) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAssPGiro ass_obb_acr_pgiro%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
 begin
  aAcc.esercizio:=aEs;
  aAcc.cd_cds:=aCdCds;
  aAcc.esercizio_originale:=aEsOri;
  aAcc.pg_accertamento:=aPgAcc;
  -- Carica la partita di giro
  CNRCTB035.GETPGIROCDSINV(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);

  if aObbScad.im_associato_doc_amm <> 0 or aObbScad.im_associato_doc_contabile <> 0 then
    If cnrutil.isLabelObbligazione() Then
      IBMERR001.RAISE_ERR_GENERICO('Obbligazione da troncare giא utilizzata '||CNRCTB035.getDesc(aObb));
    Else
      IBMERR001.RAISE_ERR_GENERICO('Impegno da troncare giא utilizzato '||CNRCTB035.getDesc(aObb));
    End If;
  end if;

  if aObb.stato_obbligazione = CNRCTB035.STATO_STORNATO then
    If cnrutil.isLabelObbligazione() Then
      IBMERR001.RAISE_ERR_GENERICO('Obbligazione da troncare giא stornata '||CNRCTB035.getDesc(aObb));
    Else
      IBMERR001.RAISE_ERR_GENERICO('Impegno da troncare giא stornato '||CNRCTB035.getDesc(aObb));
    End If;
  end if;
  update obbligazione
    set
     IM_OBBLIGAZIONE=0,
	 STATO_OBBLIGAZIONE=CNRCTB035.STATO_STORNATO,
	 DT_CANCELLAZIONE=trunc(aAcc.dacr),
 	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aObbScadVoce.cd_cds
     and ESERCIZIO=aObbScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
     and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione;

  update obbligazione_scadenzario
    set
     IM_SCADENZA=0,
	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aObbScadVoce.cd_cds
     and ESERCIZIO=aObbScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
     and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione
	 and PG_OBBLIGAZIONE_SCADENZARIO=aObbScadVoce.pg_obbligazione_scadenzario;

  update obbligazione_scad_voce
    set
     IM_VOCE=0,
 	 UTUV=aUser,
 	 DUVA=aTSNow,
 	 PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aObbScadVoce.cd_cds
    and ESERCIZIO=aObbScadVoce.esercizio
    and ESERCIZIO_ORIGINALE=aObbScadVoce.esercizio_originale
    and PG_OBBLIGAZIONE=aObbScadVoce.pg_obbligazione
	and PG_OBBLIGAZIONE_SCADENZARIO=aObbScadVoce.pg_obbligazione_scadenzario;
  -- L'aggiornamento viene eseguito all'interno della procedura VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  CNRCTB035.aggiornaSaldoDettScad(aObb,aObbScadVoce,0-aObbScadVoce.im_voce,false,aUser, aTSNow);
 end;

 procedure troncaPraticaObbPgiro(aEs number,aCdCds varchar2,aEsOri number,aPgObb number,aTSNow date,aUser varchar2) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAssPGiro ass_obb_acr_pgiro%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aSaldoCdrLineaAcc voce_f_saldi_cdr_linea%Rowtype;
 begin
  aObb.esercizio:=aEs;
  aObb.cd_cds:=aCdCds;
  aObb.esercizio_originale:=aEsOri;
  aObb.pg_obbligazione:=aPgObb;
  -- Carica la partita di giro
  CNRCTB035.GETPGIROCDS(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);

  if aAccScad.im_associato_doc_amm <> 0 or aAccScad.im_associato_doc_contabile <> 0 then
    IBMERR001.RAISE_ERR_GENERICO('Accertamento da troncare giא utilizzato '||CNRCTB035.getDesc(aAcc));
  end if;

  if aAcc.dt_cancellazione is not null then
    IBMERR001.RAISE_ERR_GENERICO('Accertamento da troncare giא cancellato '||CNRCTB035.getDesc(aAcc));
  end if;
  update accertamento
    set
     DT_CANCELLAZIONE=trunc(aObb.dacr),
     IM_ACCERTAMENTO=0,
 	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aAccScadVoce.cd_cds
     and ESERCIZIO=aAccScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aAccScadVoce.esercizio_originale
     and PG_ACCERTAMENTO=aAccScadVoce.pg_accertamento;

  update accertamento_scadenzario
    set
     IM_SCADENZA=0,
	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aAccScadVoce.cd_cds
     and ESERCIZIO=aAccScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aAccScadVoce.esercizio_originale
     and PG_ACCERTAMENTO=aAccScadVoce.pg_accertamento
	 and PG_ACCERTAMENTO_SCADENZARIO=aAccScadVoce.pg_accertamento_scadenzario;

  update accertamento_scad_voce
    set
     IM_VOCE=0,
 	 UTUV=aUser,
 	 DUVA=aTSNow,
 	 PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aAccScadVoce.cd_cds
    and ESERCIZIO=aAccScadVoce.esercizio
    and ESERCIZIO_ORIGINALE=aAccScadVoce.esercizio_originale
    And PG_ACCERTAMENTO=aAccScadVoce.pg_accertamento
	and PG_ACCERTAMENTO_SCADENZARIO=aAccScadVoce.pg_accertamento_scadenzario;
  -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  For aAccScad_voce in (select * from accertamento_scad_voce
                        Where cd_cds = aAcc.cd_cds
 			  And esercizio = aAcc.esercizio
 			  And esercizio_originale = aAcc.esercizio_originale
 			  And pg_accertamento = aAcc.pg_accertamento) Loop
    aSaldoCdrLineaAcc.ESERCIZIO := aAccScad_voce.ESERCIZIO;
    aSaldoCdrLineaAcc.ESERCIZIO_RES := aAccScad_voce.ESERCIZIO_ORIGINALE;
    aSaldoCdrLineaAcc.CD_CENTRO_RESPONSABILITA := aAccScad_voce.CD_CENTRO_RESPONSABILITA;
    aSaldoCdrLineaAcc.CD_LINEA_ATTIVITA := aAccScad_voce.CD_LINEA_ATTIVITA;
    aSaldoCdrLineaAcc.TI_APPARTENENZA := aAcc.TI_APPARTENENZA;
    aSaldoCdrLineaAcc.TI_GESTIONE := aAcc.TI_GESTIONE;
    aSaldoCdrLineaAcc.CD_VOCE := aAcc.CD_VOCE;

    CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLineaAcc);

         If aAcc.esercizio = aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_ACC_COMP := 0 - aAccScad_voce.IM_VOCE;
         Elsif aAcc.esercizio > aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_RES_PRO := 0 - aAccScad_voce.IM_VOCE;
         End If;

    aSaldoCdrLineaAcc.UTUV := aAcc.utcr;

    CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLineaAcc, '043.troncaPraticaObbPgiro', 'N');
  End Loop;
  CNRCTB035.aggiornaSaldoDettScad(aAcc,0-aAcc.im_accertamento,false,aUser, aTSNow);
 end;

procedure troncaPraticaObbPgiroInv(aEs number,aCdCds varchar2,aEsOri number,aPgObb number,aTSNow date,aUser varchar2) is
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAssPGiro ass_obb_acr_pgiro%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aSaldoCdrLineaAcc voce_f_saldi_cdr_linea%Rowtype;
 begin
  aObb.esercizio:=aEs;
  aObb.cd_cds:=aCdCds;
  aObb.esercizio_originale:=aEsOri;
  aObb.pg_obbligazione:=aPgObb;
  -- Carica la partita di giro
  CNRCTB035.GETPGIROCDSINV(aObb,aObbScad,aObbScadVoce,aAcc,aAccScad,aAccScadVoce);

  if aAccScad.im_associato_doc_amm <> 0 or aAccScad.im_associato_doc_contabile <> 0 then
    IBMERR001.RAISE_ERR_GENERICO('Accertamento da troncare giא utilizzato '||CNRCTB035.getDesc(aAcc));
  end if;

  if aAcc.dt_cancellazione is not null then
    IBMERR001.RAISE_ERR_GENERICO('Accertamento da troncare giא cancellato '||CNRCTB035.getDesc(aAcc));
  end if;
  update accertamento
    set
     DT_CANCELLAZIONE=trunc(aObb.dacr),
     IM_ACCERTAMENTO=0,
 	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aAccScadVoce.cd_cds
     and ESERCIZIO=aAccScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aAccScadVoce.esercizio_originale
     and PG_ACCERTAMENTO=aAccScadVoce.pg_accertamento;

  update accertamento_scadenzario
    set
     IM_SCADENZA=0,
	 UTUV=aUser,
	 DUVA=aTSNow,
	 PG_VER_REC=PG_VER_REC+1
    where
         CD_CDS=aAccScadVoce.cd_cds
     and ESERCIZIO=aAccScadVoce.esercizio
     and ESERCIZIO_ORIGINALE=aAccScadVoce.esercizio_originale
     and PG_ACCERTAMENTO=aAccScadVoce.pg_accertamento
	 and PG_ACCERTAMENTO_SCADENZARIO=aAccScadVoce.pg_accertamento_scadenzario;

  update accertamento_scad_voce
    set
     IM_VOCE=0,
 	 UTUV=aUser,
 	 DUVA=aTSNow,
 	 PG_VER_REC=PG_VER_REC+1
   where
        CD_CDS=aAccScadVoce.cd_cds
    and ESERCIZIO=aAccScadVoce.esercizio
    and ESERCIZIO_ORIGINALE=aAccScadVoce.esercizio_originale
    And PG_ACCERTAMENTO=aAccScadVoce.pg_accertamento
	and PG_ACCERTAMENTO_SCADENZARIO=aAccScadVoce.pg_accertamento_scadenzario;
  -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005
  For aAccScad_voce in (select * from accertamento_scad_voce
                        Where cd_cds = aAcc.cd_cds
 			  And esercizio = aAcc.esercizio
 			  And esercizio_originale = aAcc.esercizio_originale
 			  And pg_accertamento = aAcc.pg_accertamento) Loop
    aSaldoCdrLineaAcc.ESERCIZIO := aAccScad_voce.ESERCIZIO;
    aSaldoCdrLineaAcc.ESERCIZIO_RES := aAccScad_voce.ESERCIZIO_ORIGINALE;
    aSaldoCdrLineaAcc.CD_CENTRO_RESPONSABILITA := aAccScad_voce.CD_CENTRO_RESPONSABILITA;
    aSaldoCdrLineaAcc.CD_LINEA_ATTIVITA := aAccScad_voce.CD_LINEA_ATTIVITA;
    aSaldoCdrLineaAcc.TI_APPARTENENZA := aAcc.TI_APPARTENENZA;
    aSaldoCdrLineaAcc.TI_GESTIONE := aAcc.TI_GESTIONE;
    aSaldoCdrLineaAcc.CD_VOCE := aAcc.CD_VOCE;

    CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLineaAcc);

         If aAcc.esercizio = aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_ACC_COMP := 0 - aAccScad_voce.IM_VOCE;
         Elsif aAcc.esercizio > aAcc.esercizio_originale Then
              aSaldoCdrLineaAcc.IM_OBBL_RES_PRO := 0 - aAccScad_voce.IM_VOCE;
         End If;

    aSaldoCdrLineaAcc.UTUV := aAcc.utcr;

    CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLineaAcc, '043.troncaPraticaObbPgiroInv', 'N');
  End Loop;
  CNRCTB035.aggiornaSaldoDettScad(aAcc,0-aAcc.im_accertamento,false,aUser, aTSNow);
 end;

end;
