CREATE OR REPLACE PACKAGE BODY PCIR009."CNRCTB570" AS
 procedure annullaLiquidazione(aLGCori liquid_gruppo_cori%rowtype, aUser varchar2) is
  aLC liquid_cori%rowtype;
  aUOVERSACC unita_organizzativa%rowtype;
  aUOVERSCONTOBI unita_organizzativa%rowtype;
  aTSNow date;
  aGC liquid_gruppo_centro%rowtype;
  aLGCC liquid_gruppo_centro_comp%rowtype;
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aAccScadVoce accertamento_scad_voce%rowtype;
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbScadVoce obbligazione_scad_voce%rowtype;
  aAccC accertamento%rowtype;
  aAccCScad accertamento_scadenzario%rowtype;
  aAccCScadVoce accertamento_scad_voce%rowtype;
  aObbC obbligazione%rowtype;
  aObbCScad obbligazione_scadenzario%rowtype;
  aObbCScadVoce obbligazione_scad_voce%rowtype;
  recParametriCNR PARAMETRI_CNR%Rowtype;
  ESERCIZIO_UO_SPECIALI number;
 Begin
  aTSNow:=sysdate;
  recParametriCNR := CNRUTL001.getRecParametriCnr(aLGCori.esercizio);

  -- Leggo la testata della liquidazione (lock)
  select * into aLC from liquid_cori where
       cd_cds = aLGCori.cd_cds
   and esercizio = aLGCori.esercizio
   and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
   and pg_liquidazione = aLGCori.pg_liquidazione
   for update nowait;

  ESERCIZIO_UO_SPECIALI := to_number(To_Char(aLC.DT_DA,'YYYY'));

  aUOVERSACC:=CNRCTB020.getUOVersCori(ESERCIZIO_UO_SPECIALI);
  aUOVERSCONTOBI:=CNRCTB020.getUOVersCoriContoBI(ESERCIZIO_UO_SPECIALI);
  If aLGCori.cd_unita_organizzativa = aUOVERSCONTOBI.cd_unita_organizzativa then
        IBMERR001.RAISE_ERR_GENERICO('Non Ã¨ possibile eliminare il versamento dei CORI che rientrano nel Modello F24EP');
  End If;

  -- Se il versamento Ã¨ quello dell'UO di versamento, e il gruppo di cui annullare la liquidazione Ã¨ composto di SOLI
  -- dati propri dell'UO che versa, posso smontare la liquidazione di quel gruppo

  if aLGCori.cd_unita_organizzativa = aUOVERSACC.cd_unita_organizzativa then
   declare
    aTempN number;
   begin
    select distinct 1 into aTempN from liquid_gruppo_centro where
         cd_cds_lc = aLGCori.cd_cds
     and esercizio = aLGCori.esercizio
     and cd_uo_lc = aLGCori.cd_unita_organizzativa
     and pg_lc = aLGCori.pg_liquidazione
     and cd_gruppo_cr = aLGCori.cd_gruppo_cr
     and cd_regione = aLGCori.cd_regione
     and pg_comune = aLGCori.pg_comune;
    IBMERR001.RAISE_ERR_GENERICO('Non Ã¨ possibile eliminare il versamento accentrato dei CORI perchÃ¨ la liquidazione raccoglie versamenti di altre UO per il gruppo'||CNRCTB575.getDesc(aLGCori));
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;

  -- Devo modificare la partita di giro sull'UO che versa legata a aLGCori con fl_accentrato a Y

  if aLGCori.fl_accentrato = 'Y' then
   -- Solo se lo stato Ã¨ diverso da liquidato posso smontare
   if aLGCori.stato = CNRCTB575.STATO_LIQUIDATO then
    IBMERR001.RAISE_ERR_GENERICO('La liquidazione CORI al centro risulta giÃ  chiusa per il gruppo CORI '||CNRCTB575.getDesc(aLGCori));
   end if;
   begin
    aGC:=getGruppoCentro(aLC, aLGCori);
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Riferimento a gruppo centro di liquidazione non trovato per il gruppo CORI '||CNRCTB575.getDesc(aLGCori));
   end;
   if aGC.stato = CNRCTB575.STATO_GRUPPO_CENTRO_CHIUSO then
    IBMERR001.RAISE_ERR_GENERICO('La liquidazione CORI al centro risulta giÃ  chiusa per il gruppo CORI '||CNRCTB575.getDesc(aLGCori));
   end if;
   if aLGCori.im_liquidato > 0 then
    IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' THEN
/*      aObbGiro := null;
      aObbGiro.esercizio:=aGC.esercizio_obb_accentr;
      aObbGiro.cd_cds:=aGC.cd_cds_obb_accentr;
      aObbGiro.esercizio_originale:=aGC.esercizio_ori_obb_accentr;
      aObbGiro.pg_obbligazione:=aGC.pg_obb_accentr;

      CNRCTB035.GETPGIROCDSINV(aObbGiro,aObbScadGiro,aObbScadVoceGiro,aAccGiro,aAccScadGiro,aAccScadVoceGiro);

      CNRCTB043.modificaPraticaAcc(aAccGiro.esercizio,aAccGiro.cd_cds,aAccGiro.esercizio_originale,aAccGiro.pg_accertamento,0-aLGCori.im_liquidato,aTSNow,aUser);*/
      IBMERR001.RAISE_ERR_GENERICO('Impossibile annullare la liquidazione CORI nel caso di tesoreria unica');
    else
      CNRCTB043.modificaPraticaObb(aGC.esercizio_obb_accentr,aGC.cd_cds_obb_accentr,aGC.esercizio_ori_obb_accentr,aGC.pg_obb_accentr,0-aLGCori.im_liquidato,aTSNow,aUser,'Y');
    end if;
   elsif aLGCori.im_liquidato < 0 then
    -- Elimina la partita di giro di restituzione e la riga in LIQUID_GRUPPO_ENTRO_COMP
  begin
     select * into aLGCC from liquid_gruppo_centro_comp where
                 esercizio = aLGCori.esercizio
             and cd_gruppo_cr = aLGCori.cd_gruppo_cr
             and cd_regione = aLGCori.cd_regione
             and pg_comune = aLGCori.pg_comune
       and pg_gruppo_centro = aLGCori.pg_gruppo_centro
       and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
       for update nowait;
   aAcc.cd_cds:=aLGCC.cd_cds_acc_accentr;
   aAcc.esercizio:=aLGCC.esercizio_acc_accentr;
   aAcc.esercizio_originale:=aLGCC.esercizio_ori_acc_accentr;
   aAcc.pg_accertamento:=aLGCC.pg_acc_accentr;
    IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' THEN
      IBMERR001.RAISE_ERR_GENERICO('Impossibile annullare la liquidazione CORI nel caso di tesoreria unica');
    else
      CNRCTB035.GETPGIROCDSINV(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
      CNRCTB035.annullaObbligazione(aObb.cd_cds,aObb.esercizio,aObb.esercizio_originale,aObb.pg_obbligazione,aUser);
      -- Elimina la partita di giro di compensazione locale
      if aLGCori.pg_acc_compens is not null then
        aAccC.cd_cds:=aLGCori.cd_cds_acc_compens;
        aAccC.esercizio:=aLGCori.esercizio_acc_compens;
        aAccC.esercizio_originale:=aLGCori.esercizio_ori_acc_compens;
        aAccC.pg_accertamento:=aLGCori.pg_acc_compens;
        CNRCTB035.GETPGIROCDSINV(aAccC,aAccCScad,aAccCScadVoce,aObbC,aObbCScad,aObbCScadVoce);
        CNRCTB035.annullaObbligazione(aObbC.cd_cds,aObbC.esercizio,aObbC.esercizio_originale,aObbC.pg_obbligazione,aUser);
      end if;
    end if;
     delete from liquid_gruppo_centro_comp where
                 esercizio = aLGCori.esercizio
             and cd_gruppo_cr = aLGCori.cd_gruppo_cr
             and cd_regione = aLGCori.cd_regione
             and pg_comune = aLGCori.pg_comune
       and pg_gruppo_centro = aLGCori.pg_gruppo_centro
       and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa;
    exception when NO_DATA_FOUND then
   null;
  end;
   else
    null;
   end if;
  end if; -- FINE AGGIORNAMENTO PARTITA DI GIRO CREATA AL CENTRO

  -- Elimino il dettaglio minimo della liquidazione di aLGCori
  delete from liquid_gruppo_cori_det where
       cd_cds = aLGCori.cd_cds
   and esercizio = aLGCori.esercizio
   and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
   and pg_liquidazione = aLGCori.pg_liquidazione
   and cd_cds_origine = aLGCori.cd_cds_origine
   and cd_uo_origine = aLGCori.cd_uo_origine
   and pg_liquidazione_origine = aLGCori.pg_liquidazione_origine
   and cd_gruppo_cr = aLGCori.cd_gruppo_cr
   and cd_regione = aLGCori.cd_regione
   and pg_comune = aLGCori.pg_comune;

  delete from ASS_PGIRO_GRUPPO_CENTRO where
       cd_cds = aLGCori.cd_cds
   and esercizio = aLGCori.esercizio
   and cd_UO = aLGCori.cd_unita_organizzativa
   and pg_liq = aLGCori.pg_liquidazione
   and cd_cds_orig = aLGCori.cd_cds_origine
   and cd_uo_orig = aLGCori.cd_uo_origine
   and pg_liq_orig = aLGCori.pg_liquidazione_origine
   and cd_gr_cr = aLGCori.cd_gruppo_cr
   and cd_regione = aLGCori.cd_regione
   and pg_comune = aLGCori.pg_comune;

  -- Elimino aLGCori
  delete from liquid_gruppo_cori where
       cd_cds = aLGCori.cd_cds
   and esercizio = aLGCori.esercizio
   and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
   and pg_liquidazione = aLGCori.pg_liquidazione
   and cd_cds_origine = aLGCori.cd_cds_origine
   and cd_uo_origine = aLGCori.cd_uo_origine
   and pg_liquidazione_origine = aLGCori.pg_liquidazione_origine
   and cd_gruppo_cr = aLGCori.cd_gruppo_cr
   and cd_regione = aLGCori.cd_regione
   and pg_comune = aLGCori.pg_comune;

   -- Se non ci sono sotto la liquidazione gruppi TRASFERITI (stato T) posso mettere la liquidazione a Liquidata (L)
   declare
    aTempC char(1);
   begin
    select distinct stato into aTempC from liquid_gruppo_cori where
         cd_cds = aLGCori.cd_cds
     and esercizio = aLGCori.esercizio
     and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
     and pg_liquidazione = aLGCori.pg_liquidazione
   and stato = CNRCTB575.STATO_TRASFERITO;
   exception when NO_DATA_FOUND Then
    update liquid_cori
  set
   stato = CNRCTB575.STATO_LIQUIDATO,
     duva = aTSNow,
     utuv = aUser,
   pg_ver_rec = pg_ver_rec + 1
  where
        cd_cds = aLGCori.cd_cds
    and esercizio = aLGCori.esercizio
    and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
    and pg_liquidazione = aLGCori.pg_liquidazione;
   end;

   -- Se non ci sono altre righe nella liquidazione, elimino anche la testata (LIQUID_CORI)
   -- LIQUID_CORI per aUOVERSACC.cd_unita_organizzativa potrebbe avere altri dettagli, quindi
   -- viene trattata a parte
   declare
    aLocG liquid_gruppo_cori%rowtype;
   begin
    select * into aLocG from liquid_gruppo_cori where
            esercizio = aLGCori.esercizio
        and pg_liquidazione = aLGCori.pg_liquidazione
        And cd_cds_origine = aLGCori.cd_cds_origine
        and cd_uo_origine = aLGCori.cd_uo_origine
  for update nowait;
   exception
    when TOO_MANY_ROWS then
   null;
    when NO_DATA_FOUND Then
     delete from liquid_cori where
            cd_cds = aLGCori.cd_cds_origine
        and esercizio = aLGCori.esercizio
        and cd_unita_organizzativa = aLGCori.cd_uo_origine
        and pg_liquidazione = aLGCori.pg_liquidazione
        And cd_unita_organizzativa != aUOVERSACC.cd_unita_organizzativa
        And cd_unita_organizzativa != aUOVERSCONTOBI.cd_unita_organizzativa;
   end;
   -- Se non ci sono altre righe nella liquidazione per aUOVERSACC.cd_unita_organizzativa,
   -- elimino anche la testata (LIQUID_CORI)
   declare
    aLocG liquid_gruppo_cori%rowtype;
   begin
    select * into aLocG from liquid_gruppo_cori where
            cd_cds = aLGCori.cd_cds
        and esercizio = aLGCori.esercizio
        and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
        and pg_liquidazione = aLGCori.pg_liquidazione
        And cd_unita_organizzativa = aUOVERSACC.cd_unita_organizzativa
  for update nowait;
   exception
    when TOO_MANY_ROWS then
   null;
    when NO_DATA_FOUND Then
     delete from liquid_cori where
            cd_cds = aLGCori.cd_cds
        and esercizio = aLGCori.esercizio
        and cd_unita_organizzativa = aLGCori.cd_unita_organizzativa
        and pg_liquidazione = aLGCori.pg_liquidazione
        And cd_unita_organizzativa = aUOVERSACC.cd_unita_organizzativa;
   end;
  null;
 end;

 function getGruppoCentroAperto(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype) return liquid_gruppo_centro%rowtype is
  aLGC liquid_gruppo_centro%rowtype;
 begin
  select * into aLGC from liquid_gruppo_centro where
       esercizio = aAggregato.esercizio
   and cd_gruppo_cr = aAggregato.cd_gruppo_cr
   and cd_regione = aAggregato.cd_regione
   and pg_comune = aAggregato.pg_comune
   and da_esercizio_precedente  = aL.da_esercizio_precedente
   and stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
   for update nowait;
  return aLGC;
 end;

 function getGruppoCentro(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype) return liquid_gruppo_centro%rowtype is
  aLGC liquid_gruppo_centro%rowtype;
 begin
  select * into aLGC from liquid_gruppo_centro where
       esercizio = aAggregato.esercizio
   and cd_gruppo_cr = aAggregato.cd_gruppo_cr
   and cd_regione = aAggregato.cd_regione
   and pg_comune = aAggregato.pg_comune
   and pg_gruppo_centro = aAggregato.pg_gruppo_centro
   and da_esercizio_precedente  = aL.da_esercizio_precedente
   for update nowait;
  return aLGC;
 end;

 function creaGruppoCentroComp(aL liquid_cori%rowtype, aLGC liquid_gruppo_centro%rowtype, aAggregato liquid_gruppo_cori%rowtype,aTSNow date, aUser varchar2) return liquid_gruppo_centro_comp%rowtype is
  aLGCC liquid_gruppo_centro_comp%rowtype;
 begin
  aLGCC.ESERCIZIO:=aLGC.esercizio;
  aLGCC.CD_GRUPPO_CR:=aLGC.cd_gruppo_cr;
  aLGCC.CD_REGIONE:=aLGC.cd_regione;
  aLGCC.PG_COMUNE:=aLGC.pg_comune;
  aLGCC.PG_GRUPPO_CENTRO:=aLGC.pg_gruppo_centro;
  aLGCC.cd_unita_organizzativa:=aAggregato.cd_unita_organizzativa;
  aLGCC.DACR:=aTSNow;
  aLGCC.UTCR:=aUser;
  aLGCC.DUVA:=aTSNow;
  aLGCC.UTUV:=aUser;
  aLGCC.PG_VER_REC:=1;
  begin
   CNRCTB575.INS_LIQUID_GRUPPO_CENTRO_COMP(aLGCC);
  exception when DUP_VAL_ON_INDEX then
   -- L'inserimento puÃ² dare un errore di chiave duplicata se due sessioni tentano di inserire
   -- il primo record per lo stesso gruppo di versamento: la prima inserisce, la seconda deve essere bloccata
   -- e restituire l'errore di risorsa occupata
   IBMERR001.RAISE_ERR_GENERICO('Esiste giÃ  una liquidazione con saldo negativo sul gruppo'||aLGCC.cd_gruppo_cr||'.'||aLGCC.cd_regione||'.'||aLGCC.pg_comune||' per l''UO:'||aLGCC.cd_unita_organizzativa);
  end;
  return aLGCC;
 end;

 procedure restituzioneCrediti(aLiquid liquid_cori%rowtype,Cds In Out VARCHAR2,Uo In Out VARCHAR2,terzo_versamento In Out VARCHAR2, aLGC liquid_gruppo_centro%rowtype,
    aTotReversale IN OUT number, aRevP IN OUT reversale%rowtype, aListRigheRevP IN OUT CNRCTB038.righeReversaleList,
    aTotMandato IN OUT number, aManP IN OUT mandato%rowtype, aListRigheManP IN OUT CNRCTB038.righeMandatoList, aUser varchar2, aTSNow date) is
   aGen documento_generico%rowtype;
   aGenRiga documento_generico_riga%rowtype;
   aGenOpp documento_generico%rowtype;
   aGenOppRiga documento_generico_riga%rowtype;
   aListGenRighe CNRCTB100.docGenRigaList;
   aGenVS documento_generico%rowtype;
   aGenVSRiga documento_generico_riga%rowtype;
   aGenVE documento_generico%rowtype;
   aGenVERiga documento_generico_riga%rowtype;
   aListGenVERighe CNRCTB100.docGenRigaList;
   aListGenVSRighe CNRCTB100.docGenRigaList;
   aAcc accertamento%rowtype;
   aAccScad accertamento_scadenzario%rowtype;
   aAccScadVoce accertamento_scad_voce%rowtype;
   aAccNew accertamento%rowtype;
   aAccScadNew accertamento_scadenzario%rowtype;
   aAccScadVoceNew accertamento_scad_voce%rowtype;
   aObb obbligazione%rowtype;
   aObbScad obbligazione_scadenzario%rowtype;
   aObbScadVoce obbligazione_scad_voce%rowtype;
   aObbNew obbligazione%rowtype;
   aObbScadNew obbligazione_scadenzario%rowtype;
   aObbScadVoceNew obbligazione_scad_voce%rowtype;
   aCdTerzoRes number(8);
   aCdModPagRes varchar2(10);
   aPgBancaRes number(10);
   aCdTerzoVE number(8);
   aCdModPagVE varchar2(10);
   aPgBancaVE number(10);
   aManPRiga mandato_riga%rowtype;
   aAnagTst anagrafico%rowtype;
   aAnagVer anagrafico%rowtype;
   aRevPRiga reversale_riga%rowtype;
   aDateCont date;
   aDivisaEuro varchar2(50);
   aCdTerzoUo number(8);
   aCdModPagUo varchar2(10);
   aPgBancaUo number(10);
   aUOVERSCONTOBI unita_organizzativa%rowtype;
   aCdEV varchar2(20);
   elementoVoce elemento_voce%rowtype;
   recParametriCNR PARAMETRI_CNR%Rowtype;
   ESERCIZIO_UO_SPECIALI number;
   IMPORTO_DA_REGOLARIZZARE NUMBER := 0;
   aRevRegolarizzazione  reversale%rowtype;
   aListRigheRevRegolarizzazione  CNRCTB038.righeReversaleList;
   aManRegolarizzazione mandato%rowtype;
   aListRigheManRegolarizzazione CNRCTB038.righeMandatoList;
   aAnagVerRegolarizzazione anagrafico%rowtype;
   aListGenVERigheRegolarizz CNRCTB100.docGenRigaList;
   aGenRegolarizzazione documento_generico%rowtype;
   aGenRigaRegolarizzazione documento_generico_riga%rowtype;
    ES_MAN NUMBER;
    NUM_MAN NUMBER;
    IM_REV NUMBER;
 begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aLiquid.esercizio);
  aDateCont:=CNRCTB008.getTimestampContabile(aLGC.esercizio,aTSNow);
  aDivisaEuro:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB100.CCNR_DIVISA,CNRCTB100.CCNR_EURO);

  ESERCIZIO_UO_SPECIALI := to_number(To_Char(aLiquid.DT_DA,'YYYY'));
  aUOVERSCONTOBI:=CNRCTB020.getUOVersCoriContoBI(ESERCIZIO_UO_SPECIALI);

  aGenVE:=null;
  -- Creo il documento generico di entrata su partita di giro collegato all'annotazione di spesa su pgiro del contributo ritenuta
  aGenVE.CD_CDS:=Cds;
  aGenVE.CD_UNITA_ORGANIZZATIVA:=Uo;
  aGenVE.ESERCIZIO:=aLGC.esercizio;
  aGenVE.CD_CDS_ORIGINE:=Cds;
  aGenVE.CD_UO_ORIGINE:=Uo;
  aGenVE.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_ENTRATA;
  aGenVE.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
  aGenVE.DS_DOCUMENTO_GENERICO:='CORI-COMPENSAZIONE gruppo cr:'||aLGC.cd_gruppo_cr||'.'||aLGC.cd_regione||'.'||aLGC.pg_comune;
  aGenVE.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
--  aGenVE.IM_TOTALE:=;
  aGenVE.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
  aGenVE.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
  aGenVE.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
  aGenVE.CD_DIVISA:=aDivisaEuro;
  aGenVE.CAMBIO:=1;
  aGenVE.DACR:=aTSNow;
  aGenVE.UTCR:=aUser;
  aGenVE.DUVA:=aTSNow;
  aGenVE.UTUV:=aUser;
  aGenVE.PG_VER_REC:=1;
  aGenVE.DT_SCADENZA:=TRUNC(aTSNow);
  aGenVE.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
  aGenVE.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
  aGenVE.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
  aGenVE.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

  aAnagVer:=CNRCTB080.GETANAG(terzo_versamento);

  for aLGCC in (select * from liquid_gruppo_centro_comp where
                        ESERCIZIO=aLGC.esercizio
                    and CD_GRUPPO_CR=aLGC.cd_gruppo_cr
                    and CD_REGIONE=aLGC.cd_regione
                    and PG_COMUNE=aLGC.pg_comune
                    and PG_GRUPPO_CENTRO=aLGC.pg_gruppo_centro
        for update nowait
  ) loop
         -- Estraggo il terzo UO relativo all'UO SAC che accentra i versamenti
         CNRCTB080.GETTERZOPERUO(aLGCC.cd_unita_organizzativa,aCdTerzoRes, aCdModPagRes, aPgBancaRes,aLGC.esercizio);
         aAnagTst:=CNRCTB080.GETANAG(aCdTerzoRes);

         aGen:=null;
         aGenRiga:=null;


         --aAcc si chiude con la reversale di versamento
         --aObb si chiude con il mandato di restituzione
         --Se la UO Ã¨ 999.000, solo la reversale di versamento deve uscire dalla 999, mentre il mandato di restituzione deve uscire dalla SAC
         --quindi devo annullare aAcc sulla SAC (cioÃ¨ rendere tronca aObb) e ricrearla tronca sulla 999
      If recParametriCNR.FL_TESORERIA_UNICA != 'Y' then
         aAcc.cd_cds:=aLGCC.cd_cds_acc_accentr;
         aAcc.esercizio:=aLGCC.esercizio_acc_accentr;
         aAcc.esercizio_originale:=aLGCC.esercizio_ori_acc_accentr;
         aAcc.pg_accertamento:=aLGCC.pg_acc_accentr;
         CNRCTB035.GETPGIROCDSINV(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
         if Uo = aUOVERSCONTOBI.cd_unita_organizzativa then
               CNRCTB043.troncaPraticaObbPgiro(aObb.esercizio,aObb.cd_cds,aObb.esercizio_originale,aObb.pg_obbligazione,aTSNow,aUser);
               -- devo creare la pgiro tronca sulla 999
                  aAccNew:=null;
                  aAccScadNew:=null;
                  aObbNew:=null;
                  aObbScadNew:=null;
                  --determino il capitolo da mettere sulla pgiro
                  Begin
                     Select distinct a.cd_elemento_voce
                     Into aCdEV
                     From ass_tipo_cori_ev a, tipo_cr_base b
                     Where b.esercizio = aLGCC.esercizio
                       And b.cd_gruppo_cr = aLGCC.cd_gruppo_cr
                       And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
                       And a.esercizio = aLGCC.esercizio
                       And a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
                       And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
                  Exception
                          when TOO_MANY_ROWS then
                               IBMERR001.RAISE_ERR_GENERICO('Esiste piÃ¹ di un conto finanziario associato a CORI del gruppo '||aLGCC.cd_gruppo_cr);
                          when NO_DATA_FOUND then
                              IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aLGCC.cd_gruppo_cr||' non trovato');
                  End;
                  Begin
                     Select *
                     Into elementoVoce
                     From elemento_voce
                     Where esercizio = aLGCC.esercizio
                       And ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
                       And ti_gestione = CNRCTB001.GESTIONE_ENTRATE
                       And cd_elemento_voce = aCdEV;
                  Exception
                          when NO_DATA_FOUND then
                             IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aLGCC.cd_gruppo_cr||' non esistente');
                  End;

                  aAccNew.CD_CDS:=Cds;
                  aAccNew.ESERCIZIO:=aAcc.esercizio;
                  aAccNew.ESERCIZIO_ORIGINALE:=aAcc.esercizio_originale;
                  If aAcc.esercizio = aAcc.esercizio_originale Then
                       aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC;
                  Else
                       aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_RES;
                  End If;
                  aAccNew.CD_UNITA_ORGANIZZATIVA:=Uo;
                  aAccNew.CD_CDS_ORIGINE:=Cds;
                  aAccNew.CD_UO_ORIGINE:=Uo;
                  aAccNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
                  aAccNew.TI_GESTIONE:=elementoVoce.ti_gestione;
                  aAccNew.CD_ELEMENTO_VOCE:=elementoVoce.cd_elemento_voce;
                  aAccNew.CD_VOCE:=elementoVoce.cd_elemento_voce;
                  aAccNew.DT_REGISTRAZIONE:=TRUNC(trunc(aTSNow));
                  aAccNew.DS_ACCERTAMENTO:='PGiro creata in automatico da liquidazione CORI';
                  aAccNew.NOTE_ACCERTAMENTO:='';
                  aAccNew.CD_TERZO:=aAcc.CD_TERZO;
                  aAccNew.IM_ACCERTAMENTO:=aObb.IM_OBBLIGAZIONE;    --aAcc.IM_ACCERTAMENTO Ã¨ stato annullato
                  aAccNew.FL_PGIRO:='Y';
                  aAccNew.RIPORTATO:='N';
                  aAccNew.DACR:=aTSNow;
                  aAccNew.UTCR:=aUser;
                  aAccNew.DUVA:=aTSNow;
                  aAccNew.UTUV:=aUser;
                  aAccNew.PG_VER_REC:=1;
                  aAccNew.ESERCIZIO_COMPETENZA:=aAcc.esercizio;

                  CNRCTB040.CREAACCERTAMENTOPGIROTRONC(false,aAccNew,aAccScadNew,aObbNew,aObbScadNew,trunc(aTSNow));

                  CREALIQUIDCORIASSPGIRO(aLiquid,aLGCC.cd_gruppo_cr,aLGCC.cd_regione,aLGCC.pg_comune,'E',null,null,aAccNew,aAcc,aUser,trunc(aTSNow));

                  aAcc     := aAccNew;
                  aAccScad := aAccScadNew;
         End If;

         -- Creo il documento generico di spesa su partita di giro collegato all'annotazione di entrata su pgiro
         -- per versamento accentrato
         aGen.CD_CDS:=aObb.cd_cds;       --Cds;
         aGen.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;     --Uo;
         aGen.ESERCIZIO:=aLGC.esercizio;
         aGen.CD_CDS_ORIGINE:=aObb.cd_cds;     --Cds;
         aGen.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;    --Uo;
         aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_SPESA;
         aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
         aGen.DS_DOCUMENTO_GENERICO:='CORI-RESTITUZIONE A UO:'||aLGCC.cd_unita_organizzativa||' gruppo cr:'||aLGC.cd_gruppo_cr||'.'||aLGC.cd_regione||'.'||aLGC.pg_comune;
         aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
         aGen.IM_TOTALE:=aObb.im_obbligazione;
         aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
         aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
         aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
         aGen.CD_DIVISA:=aDivisaEuro;
         aGen.CAMBIO:=1;
         --  aGen.ESERCIZIO_LETTERA:=0;
         --  aGen.PG_LETTERA:=0;
         aGen.DACR:=aTSNow;
         aGen.UTCR:=aUser;
         aGen.DUVA:=aTSNow;
         aGen.UTUV:=aUser;
         aGen.PG_VER_REC:=1;
         aGen.DT_SCADENZA:=TRUNC(aTSNow);
         aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
         aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
         aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
         aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

         aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
         aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
         aGenRiga.CD_CDS:=aGen.CD_CDS;
         aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
         aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
         aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
         aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
         aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
         aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
         aGenRiga.CD_TERZO:=aCdTerzoRes;
         aGenRiga.CD_MODALITA_PAG:=aCdModPagRes;

         aGenRiga.PG_BANCA:=aPgBancaRes;
         aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
         aGenRiga.NOME:=aAnagTst.NOME;
         aGenRiga.COGNOME:=aAnagTst.COGNOME;
         aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
         aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
         --   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
         --   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
         --   aGenRiga.PG_BANCA_UO_CDS:=aGen.PG_BANCA_UO_CDS;
         --   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aGen.CD_MODALITA_PAG_UO_CDS;
         --   aGenRiga.NOTE:=aGen.NOTE;
         aGenRiga.STATO_COFI:=aGen.STATO_COFI;
         --   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
         --   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
         --   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
         --   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
         --   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
         --   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
         aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.cd_cds;
         aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.esercizio;
         aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.esercizio_originale;
         aGenRiga.PG_OBBLIGAZIONE:=aObb.pg_obbligazione;
         aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
         aGenRiga.DACR:=aGen.DACR;
         aGenRiga.UTCR:=aGen.UTCR;
         aGenRiga.UTUV:=aGen.UTUV;
         aGenRiga.DUVA:=aGen.DUVA;
         aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
         aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
         --
         aListGenRighe(1):=aGenRiga;
         CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

         aListRigheManP.delete;
         aManP:=null;
         aManP.CD_CDS:=aGen.cd_cds;
         aManP.ESERCIZIO:=aGen.esercizio;
         aManP.CD_UNITA_ORGANIZZATIVA:=aGen.cd_unita_organizzativa;
         aManP.CD_CDS_ORIGINE:=aGen.cd_cds;
         aManP.CD_UO_ORIGINE:=aGen.cd_unita_organizzativa;
         aManP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
         aManP.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
         aManP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
         aManP.DS_MANDATO:='Mandato di resituzione crediti versamento gruppo CORI: '||aLGC.cd_gruppo_cr||'.'||aLGC.cd_regione||'.'||aLGC.pg_comune;
         aManP.STATO:=CNRCTB038.STATO_MAN_EME;
         aManP.DT_EMISSIONE:=TRUNC(aDateCont);
         aManP.IM_RITENUTE:=0;
         aManP.IM_MANDATO:=aGen.IM_TOTALE;
         --  aManP.DT_TRASMISSIONE:=;
         --  aManP.DT_PAGAMENTO:=;
         --  aManP.DT_ANNULLAMENTO:=;
         aManP.IM_PAGATO:=0;
         aManP.UTCR:=aUser;
         aManP.DACR:=aTSNow;
         aManP.UTUV:=aUser;
         aManP.DUVA:=aTSNow;
         aManP.PG_VER_REC:=1;
         aManP.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;
         -- Generazione righe mandato
         aManPRiga:=null;
         aManPRiga.CD_CDS:=aGen.cd_cds;
         aManPRiga.ESERCIZIO:=aGen.esercizio;
         aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
         aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
         aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
         aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
         aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
         aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
         aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
         aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
         aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
         aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
         aManPRiga.STATO:=aManP.stato;
         aManPRiga.CD_TERZO:=aGenRiga.CD_TERZO;
         aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
         aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
         aManPRiga.IM_MANDATO_RIGA:=aGen.IM_TOTALE;
         aManPRiga.IM_RITENUTE_RIGA:=0;
         aManPRiga.FL_PGIRO:='Y';
         aManPRiga.UTCR:=aUser;
         aManPRiga.DACR:=aTSNow;
         aManPRiga.UTUV:=aUser;
         aManPRiga.DUVA:=aTSNow;
         aManPRiga.PG_VER_REC:=1;
         aListRigheManP(aListRigheManP.count+1):=aManPRiga;
         CNRCTB037.GENERADOCUMENTO(aManP,aListRigheManP);
      else
        if aLGCC.CD_CDS_OBB_ACCENTR_OPP is not null and recParametriCNR.FL_TESORERIA_UNICA = 'Y' then
          begin
           Select * into aObb
           From     obbligazione
           Where    cd_cds = aLGCC.CD_CDS_OBB_ACCENTR_OPP And
                    esercizio = aLGCC.ES_OBB_ACCENTR_OPP And
                    esercizio_originale = aLGCC.ES_ORIG_OBB_ACCENTR_OPP And
                    pg_obbligazione = aLGCC.PG_OBB_ACCENTR_OPP And
                    fl_pgiro = 'Y'
           For update nowait;

           IMPORTO_DA_REGOLARIZZARE := aObb.IM_OBBLIGAZIONE;

            Select * into aAccScad
            From     accertamento_scadenzario
            Where    cd_cds = aLGCC.cd_cds_acc_accentr And
                       esercizio = aLGCC.esercizio_acc_accentr And
                       esercizio_originale = aLGCC.esercizio_ori_acc_accentr And
                       pg_accertamento = aLGCC.pg_acc_accentr;
           CNRCTB035.sdoppiaScadenzaAccertamento(aAccScad, aAccScad.im_SCADENZA - IMPORTO_DA_REGOLARIZZARE, aUser);
          exception
            when NO_DATA_FOUND then
             IMPORTO_DA_REGOLARIZZARE := 0;
          end;
        ELSE
         IMPORTO_DA_REGOLARIZZARE := 0;
        end if;
         Begin
            Select * into aAcc
            From     accertamento
            Where    cd_cds = aLGCC.cd_cds_acc_accentr And
                     esercizio = aLGCC.esercizio_acc_accentr And
                     esercizio_originale = aLGCC.esercizio_ori_acc_accentr And
                     pg_accertamento = aLGCC.pg_acc_accentr And
                     fl_pgiro = 'Y'
            For update nowait;

            Select * into aAccScad
            From     accertamento_scadenzario
            Where    cd_cds = aLGCC.cd_cds_acc_accentr And
                     esercizio = aLGCC.esercizio_acc_accentr And
                     esercizio_originale = aLGCC.esercizio_ori_acc_accentr And
                     pg_accertamento = aLGCC.pg_acc_accentr and
                     pg_accertamento_scadenzario = 1
            For update nowait;

            Select * into aAccScadVoce
            From     accertamento_scad_voce
            Where    cd_cds = aLGCC.cd_cds_acc_accentr And
                     esercizio = aLGCC.esercizio_acc_accentr And
                     esercizio_originale = aLGCC.esercizio_ori_acc_accentr And
                     pg_accertamento = aLGCC.pg_acc_accentr and
                     pg_accertamento_scadenzario = 1
            For Update nowait;
         Exception
           when NO_DATA_FOUND then
             IBMERR001.RAISE_ERR_GENERICO('Accertamento non trovato '||aLGCC.cd_cds_acc_accentr||'-'||aLGCC.esercizio_acc_accentr||'-'||aLGCC.esercizio_ori_acc_accentr||'-'||aLGCC.pg_acc_accentr);
         end;
       End if;
       -- Crea righe di generico e di reversale per compensazione liquidazione al centro

       aGenVERiga:=null;

       aGenVERiga.DT_DA_COMPETENZA_COGE:=aGenVE.DT_DA_COMPETENZA_COGE;
       aGenVERiga.DT_A_COMPETENZA_COGE:=aGenVE.DT_A_COMPETENZA_COGE;
       aGenVERiga.CD_CDS:=aGenVE.CD_CDS;
       aGenVERiga.CD_UNITA_ORGANIZZATIVA:=aGenVE.CD_UNITA_ORGANIZZATIVA;
       aGenVERiga.ESERCIZIO:=aGenVE.ESERCIZIO;
       aGenVERiga.CD_TIPO_DOCUMENTO_AMM:=aGenVE.CD_TIPO_DOCUMENTO_AMM;
       aGenVERiga.DS_RIGA:=aGenVE.DS_DOCUMENTO_GENERICO;
       aGenVERiga.IM_RIGA_DIVISA:=aAccScad.im_SCADENZA;
       aGenVERiga.IM_RIGA:=aAccScad.im_SCADENZA;
       -- Utilizzo delle corrette informazioni di pagamento nel caso di accentramento o non accentramento
       aGenVERiga.CD_TERZO:=terzo_versamento;

       If Uo = aUOVERSCONTOBI.cd_unita_organizzativa then
            CNRCTB080.getTerzoPerEnteContoBI(Uo, aCdTerzoVE, aCdModPagVE, aPgBancaVE);
       Else
            aCdTerzoVE := aCdTerzoRes;
            aPgBancaVE := aPgBancaRes;
            aCdModPagVE := aCdModPagRes;
       End If;

       aGenVERiga.CD_TERZO_UO_CDS:=aCdTerzoVE;
       aGenVERiga.PG_BANCA_UO_CDS:=aPgBancaVE;
       aGenVERiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagVE;

       aGenVERiga.RAGIONE_SOCIALE:=aAnagVer.RAGIONE_SOCIALE;
       aGenVERiga.NOME:=aAnagVer.NOME;
       aGenVERiga.COGNOME:=aAnagVer.COGNOME;
       aGenVERiga.CODICE_FISCALE:=aAnagVer.CODICE_FISCALE;
       aGenVERiga.PARTITA_IVA:=aAnagVer.PARTITA_IVA;
       aGenVERiga.STATO_COFI:=aGenVE.STATO_COFI;
       aGenVERiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
       aGenVERiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
       aGenVERiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
       aGenVERiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
       aGenVERiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
       aGenVERiga.DACR:=aGenVE.DACR;
       aGenVERiga.UTCR:=aGenVE.UTCR;
       aGenVERiga.UTUV:=aGenVE.UTUV;
       aGenVERiga.DUVA:=aGenVE.DUVA;
       aGenVERiga.PG_VER_REC:=aGenVE.PG_VER_REC;
       aGenVERiga.TI_ASSOCIATO_MANREV:=aGenVE.TI_ASSOCIATO_MANREV;
       aListGenVERighe(aListGenVERighe.count+1):=aGenVERiga;
       -- Generazione righe reversale
       aRevPRiga:=null;
       aRevPRiga.CD_CDS:=aGenVE.cd_cds;
       aRevPRiga.ESERCIZIO:=aGenVE.esercizio;
       aRevPRiga.ESERCIZIO_ACCERTAMENTO:=aGenVERiga.esercizio_ACCERTAMENTO;
       aRevPRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aGenVERiga.esercizio_ORI_ACCERTAMENTO;
       aRevPRiga.PG_ACCERTAMENTO:=aGenVERiga.pg_accertamento;
       aRevPRiga.PG_ACCERTAMENTO_SCADENZARIO:=aGenVERiga.pg_accertamento_scadenzario;
       aRevPRiga.CD_CDS_DOC_AMM:=aGenVE.cd_cds;
       aRevPRiga.CD_UO_DOC_AMM:=aGenVE.cd_unita_organizzativa;
       aRevPRiga.ESERCIZIO_DOC_AMM:=aGenVE.esercizio;
       aRevPRiga.CD_TIPO_DOCUMENTO_AMM:=aGenVE.cd_tipo_documento_amm;
       aRevPRiga.PG_DOC_AMM:=-1000;
       aRevPRiga.DS_REVERSALE_RIGA:=aRevP.ds_reversale;
       aRevPRiga.STATO:=aRevP.stato;
       aRevPRiga.CD_TERZO:=aGenVERiga.cd_terzo;
       aRevPRiga.CD_TERZO_UO:=aGenVERiga.cd_terzo_uo_cds;
       aRevPRiga.PG_BANCA:=aGenVERiga.pg_banca_uo_cds;
       aRevPRiga.CD_MODALITA_PAG:=aGenVERiga.cd_modalita_pag_uo_cds;
       aRevPRiga.IM_REVERSALE_RIGA:=aAccScad.im_SCADENZA;
       aRevPRiga.FL_PGIRO:='Y';
       aRevPRiga.UTCR:=aUser;
       aRevPRiga.DACR:=aTSNow;
       aRevPRiga.UTUV:=aUser;
       aRevPRiga.DUVA:=aTSNow;
       aRevPRiga.PG_VER_REC:=1;
       aTotReversale:=aTotReversale+aRevPRiga.im_reversale_riga;
       aListRigheRevP(aListRigheRevP.count+1):=aRevPRiga;

       if aLGCC.CD_CDS_OBB_ACCENTR_OPP is not null and importo_da_regolarizzare > 0 and recParametriCNR.FL_TESORERIA_UNICA = 'Y' then

         Select * into aObbScad
         From     obbligazione_scadenzario
         Where    cd_cds = aLGCC.CD_CDS_OBB_ACCENTR_OPP And
                  esercizio = aLGCC.ES_OBB_ACCENTR_OPP And
                  esercizio_originale = aLGCC.ES_ORIG_OBB_ACCENTR_OPP And
                  pg_obbligazione = aLGCC.PG_OBB_ACCENTR_OPP
         For update nowait;

         Select * into aObbScadVoce
         From     obbligazione_scad_voce
         Where    cd_cds = aLGCC.CD_CDS_OBB_ACCENTR_OPP And
                  esercizio = aLGCC.ES_OBB_ACCENTR_OPP And
                  esercizio_originale = aLGCC.ES_ORIG_OBB_ACCENTR_OPP And
                  pg_obbligazione = aLGCC.PG_OBB_ACCENTR_OPP
         For Update nowait;

        aGenVS:=null;
        -- Creo il documento generico di spesa su partita di giro opposta collegato all'annotazione di spesa su pgiro del contributo ritenuta
        aGenVS.CD_CDS:=Cds;
        aGenVS.CD_UNITA_ORGANIZZATIVA:=Uo;
        aGenVS.ESERCIZIO:=aLGC.esercizio;
        aGenVS.CD_CDS_ORIGINE:=Cds;
        aGenVS.CD_UO_ORIGINE:=Uo;
        aGenVS.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_SPESA;
        aGenVS.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
        aGenVS.DS_DOCUMENTO_GENERICO:='CORI-COMPENSAZIONE OPPOSTA gruppo cr:'||aLGC.cd_gruppo_cr||'.'||aLGC.cd_regione||'.'||aLGC.pg_comune;
        aGenVS.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
        aGenVS.IM_TOTALE:=aObb.im_obbligazione;
        aGenVS.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
        aGenVS.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
        aGenVS.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
        aGenVS.CD_DIVISA:=aDivisaEuro;
        aGenVS.CAMBIO:=1;
        aGenVS.DACR:=aTSNow;
        aGenVS.UTCR:=aUser;
        aGenVS.DUVA:=aTSNow;
        aGenVS.UTUV:=aUser;
        aGenVS.PG_VER_REC:=1;
        aGenVS.DT_SCADENZA:=TRUNC(aTSNow);
        aGenVS.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
        aGenVS.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
        aGenVS.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
        aGenVS.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

         aGenVSRiga:=null;

         aGenVSRiga.DT_DA_COMPETENZA_COGE:=aGenVS.DT_DA_COMPETENZA_COGE;
         aGenVSRiga.DT_A_COMPETENZA_COGE:=aGenVS.DT_A_COMPETENZA_COGE;
         aGenVSRiga.CD_CDS:=aGenVS.CD_CDS;
         aGenVSRiga.CD_UNITA_ORGANIZZATIVA:=aGenVS.CD_UNITA_ORGANIZZATIVA;
         aGenVSRiga.ESERCIZIO:=aGenVS.ESERCIZIO;
         aGenVSRiga.CD_TIPO_DOCUMENTO_AMM:=aGenVS.CD_TIPO_DOCUMENTO_AMM;
         aGenVSRiga.DS_RIGA:=aGenVS.DS_DOCUMENTO_GENERICO;
         aGenVSRiga.IM_RIGA_DIVISA:=aObb.im_obbligazione;
         aGenVSRiga.IM_RIGA:=aObb.im_obbligazione;
         -- Utilizzo delle corrette informazioni di pagamento nel caso di accentramento o non accentramento
         aGenVSRiga.CD_TERZO:=aObb.cd_terzo;

         CNRCTB080.getModPagUltime(aObb.cd_terzo, aCdModPagVE, aPgBancaVE);
         aGenVSRiga.CD_MODALITA_PAG:=aCdModPagVE;
         aGenVSRiga.PG_BANCA:=aPgBancaVE;

         aAnagVerRegolarizzazione :=CNRCTB080.GETANAG(aObb.cd_terzo);
         aGenVSRiga.RAGIONE_SOCIALE:=aAnagVerRegolarizzazione.RAGIONE_SOCIALE;
         aGenVSRiga.NOME:=aAnagVerRegolarizzazione.NOME;
         aGenVSRiga.COGNOME:=aAnagVerRegolarizzazione.COGNOME;
         aGenVSRiga.CODICE_FISCALE:=aAnagVerRegolarizzazione.CODICE_FISCALE;
         aGenVSRiga.PARTITA_IVA:=aAnagVerRegolarizzazione.PARTITA_IVA;
         aGenVSRiga.STATO_COFI:=aGenVS.STATO_COFI;
         aGenVSRiga.DT_CANCELLAZIONE:=aGenVS.DT_CANCELLAZIONE;
         aGenVSRiga.CD_CDS_OBBLIGAZIONE:=aObb.CD_CDS;
         aGenVSRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.ESERCIZIO;
         aGenVSRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.ESERCIZIO_ORIGINALE;
         aGenVSRiga.PG_OBBLIGAZIONE:=aObb.PG_OBBLIGAZIONE;
         aGenVSRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
         aGenVSRiga.DACR:=aGenVS.DACR;
         aGenVSRiga.UTCR:=aGenVS.UTCR;
         aGenVSRiga.UTUV:=aGenVS.UTUV;
         aGenVSRiga.DUVA:=aGenVS.DUVA;
         aGenVSRiga.PG_VER_REC:=aGenVS.PG_VER_REC;
         aGenVSRiga.TI_ASSOCIATO_MANREV:=aGenVS.TI_ASSOCIATO_MANREV;

         aListGenVSRighe (1):=aGenVSRiga;

         -- Generazione righe MANDATO
         aManRegolarizzazione:=null;
         aManRegolarizzazione.CD_CDS:=aGenVS.cd_cds;
         aManRegolarizzazione.ESERCIZIO:=aGenVS.esercizio;
         aManRegolarizzazione.CD_UNITA_ORGANIZZATIVA:=aGenVS.cd_unita_organizzativa;
         aManRegolarizzazione.CD_CDS_ORIGINE:=aGenVS.cd_cds;
         aManRegolarizzazione.CD_UO_ORIGINE:=aGenVS.cd_unita_organizzativa;
         aManRegolarizzazione.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
         aManRegolarizzazione.TI_MANDATO:=CNRCTB038.TI_MAN_REG;
         aManRegolarizzazione.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
         aManRegolarizzazione.DS_MANDATO:='Mandato di regolarizzazione restituzione crediti versamento gruppo CORI: '||aLGC.cd_gruppo_cr||'.'||aLGC.cd_regione||'.'||aLGC.pg_comune||' per la uo di origine '||aLGCC.cd_unita_organizzativa;
         aManRegolarizzazione.STATO:=CNRCTB038.STATO_MAN_PAG;
         aManRegolarizzazione.DT_EMISSIONE:=TRUNC(aDateCont);
         aManRegolarizzazione.IM_RITENUTE:=0;
         aManRegolarizzazione.IM_MANDATO:=aGenVS.IM_TOTALE;
         --  aManP.DT_TRASMISSIONE:=;
         --  aManP.DT_PAGAMENTO:=;
         --  aManP.DT_ANNULLAMENTO:=;
         aManRegolarizzazione.IM_PAGATO:=0;
         aManRegolarizzazione.UTCR:=aUser;
         aManRegolarizzazione.DACR:=aTSNow;
         aManRegolarizzazione.UTUV:=aUser;
         aManRegolarizzazione.DUVA:=aTSNow;
         aManRegolarizzazione.PG_VER_REC:=1;
         aManRegolarizzazione.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;


         aManPRiga:=null;
         aManPRiga.CD_CDS:=aGenVS.cd_cds;
         aManPRiga.ESERCIZIO:=aGenVS.esercizio;
         aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenVSRiga.esercizio_OBBLIGAZIONE;
         aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenVSRiga.esercizio_ORI_OBBLIGAZIONE;
         aManPRiga.PG_OBBLIGAZIONE:=aGenVSRiga.pg_OBBLIGAZIONE;
         aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenVSRiga.pg_OBBLIGAZIONE_scadenzario;
         aManPRiga.CD_CDS_DOC_AMM:=aGenVS.cd_cds;
         aManPRiga.CD_UO_DOC_AMM:=aGenVS.cd_unita_organizzativa;
         aManPRiga.ESERCIZIO_DOC_AMM:=aGenVS.esercizio;
         aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGenVS.cd_tipo_documento_amm;
         aManPRiga.PG_DOC_AMM:=-1000;
         aManPRiga.DS_MANDATO_RIGA:=aManRegolarizzazione.ds_mandato;
         aManPRiga.STATO:=aManRegolarizzazione.stato;
         aManPRiga.CD_TERZO:=aGenVSRiga.cd_terzo;
         aManPRiga.IM_MANDATO_RIGA:=aObb.im_obbligazione;
         aManPRiga.IM_RITENUTE_RIGA:=0;
         aManPRiga.FL_PGIRO:='Y';
         aManPRiga.UTCR:=aUser;
         aManPRiga.DACR:=aTSNow;
         aManPRiga.UTUV:=aUser;
         aManPRiga.DUVA:=aTSNow;
         aManPRiga.PG_VER_REC:=1;
         aListRigheManRegolarizzazione (1):=aManPRiga;
         CNRCTB110.CREAGENERICOAGGOBBACC(aGenVS,aListGenVSRighe);
         for i in 1..aListRigheManRegolarizzazione.count loop
          if aListRigheManRegolarizzazione (i).pg_doc_amm=-1000 then
            aListRigheManRegolarizzazione(i).pg_doc_amm:=aGenVS.pg_documento_generico;
          end if;
         end loop;
         aManRegolarizzazione.IM_PAGATO:=aManRegolarizzazione.IM_MANDATO;
         CNRCTB037.generaDocumento(aManRegolarizzazione,aListRigheManRegolarizzazione);
         CNRCTB037.RISCONTROMANDATO (aManRegolarizzazione.esercizio, aManRegolarizzazione.cd_cds, aManRegolarizzazione.pg_mandato, 'I', aManRegolarizzazione.utcr);



        aGenRegolarizzazione:=null;
        aGenRegolarizzazione.CD_CDS:=Cds;
        aGenRegolarizzazione.CD_UNITA_ORGANIZZATIVA:=Uo;
        aGenRegolarizzazione.ESERCIZIO:=aLGC.esercizio;
        aGenRegolarizzazione.CD_CDS_ORIGINE:=Cds;
        aGenRegolarizzazione.CD_UO_ORIGINE:=Uo;
        aGenRegolarizzazione.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GENERICO_REGOLA_E;
        aGenRegolarizzazione.DATA_REGISTRAZIONE:=TRUNC(aDateCont);    -- TRUNC(aTSNow);
        aGenRegolarizzazione.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont); -- TRUNC(aTSNow);
        aGenRegolarizzazione.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);  -- TRUNC(aTSNow);
        aGenRegolarizzazione.DS_DOCUMENTO_GENERICO:='CORI-COMPENSAZIONE OPPOSTA regolarizzazione gruppo cr:'||aLGC.cd_gruppo_cr||'.'||aLGC.cd_regione||'.'||aLGC.pg_comune||' per la uo di origine '||aLGCC.cd_unita_organizzativa;
        aGenRegolarizzazione.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
        aGenRegolarizzazione.IM_TOTALE:=aManRegolarizzazione.IM_MANDATO;
        aGenRegolarizzazione.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
        aGenRegolarizzazione.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
        aGenRegolarizzazione.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
        aGenRegolarizzazione.CD_DIVISA:=aDivisaEuro;
        aGenRegolarizzazione.CAMBIO:=1;
        aGenRegolarizzazione.DACR:=aTSNow;
        aGenRegolarizzazione.UTCR:=aUser;
        aGenRegolarizzazione.DUVA:=aTSNow;
        aGenRegolarizzazione.UTUV:=aUser;
        aGenRegolarizzazione.PG_VER_REC:=1;
        aGenRegolarizzazione.DT_SCADENZA:=TRUNC(aTSNow);
        aGenRegolarizzazione.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
        aGenRegolarizzazione.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
        aGenRegolarizzazione.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
        aGenRegolarizzazione.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

         aGenRigaRegolarizzazione:=null;

         aGenRigaRegolarizzazione.DT_DA_COMPETENZA_COGE:=aGenRegolarizzazione.DT_DA_COMPETENZA_COGE;
         aGenRigaRegolarizzazione.DT_A_COMPETENZA_COGE:=aGenRegolarizzazione.DT_A_COMPETENZA_COGE;
         aGenRigaRegolarizzazione.CD_CDS:=aGenRegolarizzazione.CD_CDS;
         aGenRigaRegolarizzazione.CD_UNITA_ORGANIZZATIVA:=aGenRegolarizzazione.CD_UNITA_ORGANIZZATIVA;
         aGenRigaRegolarizzazione.ESERCIZIO:=aGenRegolarizzazione.ESERCIZIO;
         aGenRigaRegolarizzazione.CD_TIPO_DOCUMENTO_AMM:=aGenRegolarizzazione.CD_TIPO_DOCUMENTO_AMM;
         aGenRigaRegolarizzazione.DS_RIGA:=aGenRegolarizzazione.DS_DOCUMENTO_GENERICO;
         aGenRigaRegolarizzazione.IM_RIGA_DIVISA:=IMPORTO_DA_REGOLARIZZARE;
         aGenRigaRegolarizzazione.IM_RIGA:=IMPORTO_DA_REGOLARIZZARE;
         -- Utilizzo delle corrette informazioni di pagamento nel caso di accentramento o non accentramento
         aGenRigaRegolarizzazione.CD_TERZO:=aAcc.cd_terzo;

         CNRCTB080.getTerzoPerUO(aGenRegolarizzazione.CD_UNITA_ORGANIZZATIVA, aCdTerzoUO, aCdModPagUO, aPgBancaUO,aGenRegolarizzazione.esercizio);

         aGenRigaRegolarizzazione.CD_TERZO_UO_CDS:=aCdTerzoUO;
         aGenRigaRegolarizzazione.PG_BANCA_UO_CDS:=aPgBancaUO;
         aGenRigaRegolarizzazione.CD_MODALITA_PAG_UO_CDS:=aCdModPagUO;
         aGenRigaRegolarizzazione.STATO_COFI:=aGenRegolarizzazione.STATO_COFI;
         aGenRigaRegolarizzazione.DT_CANCELLAZIONE:=aGenRegolarizzazione.DT_CANCELLAZIONE;
         aGenRigaRegolarizzazione.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
         aGenRigaRegolarizzazione.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
         aGenRigaRegolarizzazione.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
         aGenRigaRegolarizzazione.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
         aGenRigaRegolarizzazione.PG_ACCERTAMENTO_SCADENZARIO:=2;
         aGenRigaRegolarizzazione.DACR:=aGenRegolarizzazione.DACR;
         aGenRigaRegolarizzazione.UTCR:=aGenRegolarizzazione.UTCR;
         aGenRigaRegolarizzazione.UTUV:=aGenRegolarizzazione.UTUV;
         aGenRigaRegolarizzazione.DUVA:=aGenRegolarizzazione.DUVA;
         aGenRigaRegolarizzazione.PG_VER_REC:=aGenRegolarizzazione.PG_VER_REC;
         aGenRigaRegolarizzazione.TI_ASSOCIATO_MANREV:=aGenRegolarizzazione.TI_ASSOCIATO_MANREV;

         aListGenVERigheRegolarizz (1):=aGenRigaRegolarizzazione;
BEGIN
         CNRCTB110.creaGenericoAggObbAcc(aGenRegolarizzazione,aListGenVERigheRegolarizz);
Exception
  WHEN Others THEN RAISE_APPLICATION_ERROR(-20100,aGenRigaRegolarizzazione.DS_RIGA||' '||aAcc.PG_ACCERTAMENTO);
END;

      --- creo la reversale di regolarizzazione
         aRevRegolarizzazione.CD_CDS:=Cds;
         aRevRegolarizzazione.ESERCIZIO:=aLGC.esercizio;
         aRevRegolarizzazione.CD_UNITA_ORGANIZZATIVA:=uo;
         aRevRegolarizzazione.CD_CDS_ORIGINE:=Cds;
         aRevRegolarizzazione.CD_UO_ORIGINE:=uo;
         aRevRegolarizzazione.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV;
         aRevRegolarizzazione.TI_REVERSALE:=CNRCTB038.TI_REV_REG;
         aRevRegolarizzazione.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
         aRevRegolarizzazione.DS_REVERSALE:='Reversale di regolarizzazione restituzione crediti versamento gruppo CORI: '||aLGC.cd_gruppo_cr||'.'||aLGC.cd_regione||'.'||aLGC.pg_comune||' per la uo di origine '||aLGCC.cd_unita_organizzativa;
         aRevRegolarizzazione.STATO:=CNRCTB038.STATO_REV_PAG;
         aRevRegolarizzazione.DT_EMISSIONE:=TRUNC(aDateCont); -- TRUNC(aTSNow);
         aRevRegolarizzazione.STATO_COGE := CNRCTB100.STATO_COEP_EXC;
         aRevRegolarizzazione.IM_REVERSALE:=aManRegolarizzazione.IM_MANDATO;
         aRevRegolarizzazione.IM_INCASSATO:=aManRegolarizzazione.IM_MANDATO;
         aRevRegolarizzazione.DACR:=aTSNow;
         aRevRegolarizzazione.UTCR:=aUser;
         aRevRegolarizzazione.DUVA:=aTSNow;
         aRevRegolarizzazione.UTUV:=aUser;
         aRevRegolarizzazione.PG_VER_REC:=1;
         aRevRegolarizzazione.STATO_TRASMISSIONE:=CNRCTB038.STATO_REV_TRASCAS_NODIST;

        -- per ogni riga di doc.generico creato per la chiusra del fondo viene creata una riga di reversale
            aRevPRiga:=null;
            aRevPRiga.CD_CDS:=aRevRegolarizzazione.cd_cds;
            aRevPRiga.ESERCIZIO:=aRevRegolarizzazione.esercizio;
            aRevPRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.esercizio;
            aRevPRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.esercizio_oriGINALE;
            aRevPRiga.PG_ACCERTAMENTO:=aAcc.pg_accertamento;
            aRevPRiga.PG_ACCERTAMENTO_SCADENZARIO:=2;
            aRevPRiga.CD_CDS_DOC_AMM:=aGenRegolarizzazione.cd_cds;
            aRevPRiga.CD_UO_DOC_AMM:=aGenRegolarizzazione.cd_unita_organizzativa;
            aRevPRiga.ESERCIZIO_DOC_AMM:=aGenRegolarizzazione.esercizio;
            aRevPRiga.CD_TIPO_DOCUMENTO_AMM:=aGenRegolarizzazione.cd_tipo_documento_amm;
            aRevPRiga.PG_DOC_AMM:=aGenRegolarizzazione.pg_documento_generico;
            aRevPRiga.DS_REVERSALE_RIGA:=aRevRegolarizzazione.ds_reversale;
            aRevPRiga.STATO:=aRevRegolarizzazione.STATO;
            aRevPRiga.CD_TERZO:=aGenRigaRegolarizzazione.cd_terzo;
            aRevPRiga.CD_TERZO_UO:=aGenRigaRegolarizzazione.cd_terzo_uo_cds;
            aRevPRiga.IM_REVERSALE_RIGA:=aManRegolarizzazione.IM_MANDATO;
            aRevPRiga.FL_PGIRO:='Y';
            aRevPRiga.UTCR:=aUser;
            aRevPRiga.DACR:=aTSNow;
            aRevPRiga.UTUV:=aUser;
            aRevPRiga.DUVA:=aTSNow;
            aRevPRiga.PG_VER_REC:=1;
            aListRigheRevRegolarizzazione (1):=aRevPRiga;

            cnrctb037.GENERAREVERSALE( aRevRegolarizzazione,aListRigheRevRegolarizzazione);
           cnrctb038.ins_ASS_MANDATO_REVERSALE ( aManRegolarizzazione, aManRegolarizzazione.pg_mandato, aRevRegolarizzazione);

         -- Aggiorna il campo IM_PAGAMENTI_INCASSI su VOCE_F_SALDI_CMP
           CNRCTB037.RISCONTROREVERSALE (aRevRegolarizzazione.esercizio, aRevRegolarizzazione.cd_cds, aRevRegolarizzazione.pg_reversale, 'I', aManRegolarizzazione.utcr);
       end if;
   end loop;
   if aListGenVERighe.count > 0 then
    CNRCTB110.CREAGENERICOAGGOBBACC(aGenVE,aListGenVERighe);
   end if;
   for i in 1..aListRigheRevP.count loop
    if aListRigheRevP(i).pg_doc_amm=-1000 then
   aListRigheRevP(i).pg_doc_amm:=aGenVE.pg_documento_generico;
  end if;
   end loop;

 end;

 function creaGruppoCentro(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype,aTSNow date, aUser varchar2) return liquid_gruppo_centro%rowtype is
  aLGC liquid_gruppo_centro%rowtype;
  aPgGruppoCentro number(10);
 begin
  begin
   select pg_gruppo_centro+1 into aPgGruppoCentro from liquid_gruppo_centro where
         ESERCIZIO=aAggregato.esercizio
     AND CD_GRUPPO_CR=aAggregato.cd_gruppo_cr
     AND CD_REGIONE=aAggregato.cd_regione
     AND PG_COMUNE=aAggregato.pg_comune
     AND pg_gruppo_centro=(select max(pg_gruppo_centro) from liquid_gruppo_centro
       where
            ESERCIZIO=aAggregato.esercizio
        AND CD_GRUPPO_CR=aAggregato.cd_gruppo_cr
        AND CD_REGIONE=aAggregato.cd_regione
        AND PG_COMUNE=aAggregato.pg_comune
   )
   for update nowait;
  exception when NO_DATA_FOUND then
   aPgGruppoCentro:=1;
  end;
  aLGC.ESERCIZIO:=aAggregato.esercizio;
  aLGC.CD_GRUPPO_CR:=aAggregato.cd_gruppo_cr;
  aLGC.CD_REGIONE:=aAggregato.cd_regione;
  aLGC.PG_COMUNE:=aAggregato.pg_comune;
  aLGC.PG_GRUPPO_CENTRO:=aPgGruppoCentro;
  aLGC.STATO:=CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE;
  aLGC.CD_CDS_LC:=null;
  aLGC.CD_UO_LC:=null;
  aLGC.PG_LC:=null;
  aLGC.DACR:=aTSNow;
  aLGC.UTCR:=aUser;
  aLGC.DUVA:=aTSNow;
  aLGC.UTUV:=aUser;
  aLGC.PG_VER_REC:=1;
  aLGC.DA_ESERCIZIO_PRECEDENTE:=aL.da_esercizio_precedente;
  begin
   CNRCTB575.INS_LIQUID_GRUPPO_CENTRO(aLGC);
  exception when DUP_VAL_ON_INDEX then
   -- L'inserimento puÃ² dare un errore di chiave duplicata se due sessioni tentano di inserire
   -- il primo record per lo stesso gruppo di versamento: la prima inserisce, la seconda deve essere bloccata
   -- e restituire l'errore di risorsa occupata
   IBMERR001.RAISE_ERR_GENERICO('Risorsa occupata riprovare piÃ¹ tardi');
  end;
  return aLGC;
 end;

 procedure aggiornaPraticaGruppoCentro(aL liquid_cori%rowtype, aAggregato liquid_gruppo_cori%rowtype,aUOVERSACC unita_organizzativa%rowtype,aTSNow date,aUser varchar2, tb_ass_pgiro IN OUT tab_ass_pgiro) is
  aAcc accertamento%rowtype;
  aAccScad accertamento_scadenzario%rowtype;
  aObb obbligazione%rowtype;
  aObbScad obbligazione_scadenzario%rowtype;
  aObbNew obbligazione%rowtype;
  aObbScadNew obbligazione_scadenzario%rowtype;
  aAccNew accertamento%rowtype;
  aAccScadNew accertamento_scadenzario%rowtype;
  aObbPG obbligazione%rowtype;
  aObbGiro obbligazione%rowtype;
  aObbScadGiro  obbligazione_scadenzario%rowtype;
  aObbScadVoceGiro  obbligazione_scad_voce%rowtype;
  aObbScadVoce  obbligazione_scad_voce%rowtype;
  aAccScadVoce  accertamento_scad_voce%rowtype;
  aObbPGScad obbligazione_scadenzario%rowtype;
  aAccGiro accertamento%rowtype;
  aAccScadGiro accertamento_scadenzario%rowtype;
  aAccScadVoceGiro  accertamento_scad_voce%rowtype;
  aAccPG accertamento%rowtype;
  aAccPGScad accertamento_scadenzario%rowtype;
  aCdEV varchar2(20);
  elementoVoce elemento_voce%rowtype;
  aLGC liquid_gruppo_centro%rowtype;
  aLGCC liquid_gruppo_centro_comp%rowtype;
  aCdTerzoAcc number(8);
  aCdModPagAcc varchar2(10);
  aPgBancaAcc number(10);
  aCdTerzoAcc_unica number(8);
  aCdModPagAcc_unica varchar2(10);
  aPgBancaAcc_unica number(10);
  aCdTerzoUo number(8);
  aCdModPagUo varchar2(10);
  aPgBancaUo number(10);
  aGen documento_generico%rowtype;
  aGenRiga documento_generico_riga%rowtype;
  aListGenRighe CNRCTB100.docGenRigaList;
  aRev reversale%rowtype;
  aRevRiga reversale_riga%rowtype;
  aAnagTst anagrafico%rowtype;
  aContoColl ass_partita_giro%rowtype;
  aDateCont date;
  aAccConScad varchar2(1):='Y';
  recParametriCNR PARAMETRI_CNR%Rowtype;
  TROVATO_DETTAGLIO_P_GIRO VARCHAR2(1);
  SOMMA_OPPOSTA NUMBER := 0;
  aObbGiroOpp obbligazione%rowtype;
  aObbScadGiroOpp  obbligazione_scadenzario%rowtype;
  aObbScadVoceGiroOpp  obbligazione_scad_voce%rowtype;
  aAccGiroOpp accertamento%rowtype;
  aAccScadGiroOpp accertamento_scadenzario%rowtype;
  aAccScadVoceGiroOpp  accertamento_scad_voce%rowtype;
Begin


/***************GGGGGGGGGGGGGGG PENSO CHE BISOGNA MODIFICARE QUESTA PARTE....DEVO SOMMARE GLI IMPORTI DI SEGNO INVERSO CHE TROVO SU CONTRIBUTO_RITENUTA CHE DEVO METTERE IN JOIN
 CON LIQUID_GRUPPO_CORI_DET....POI L'IMPORTO IN VALORE ASSOLUTO LO DEVO SOMMARE ALL'IMPORTO LIQUIDATO E DEVO CREARE I DOCUMENTI CONTABILI E AMMINISTRATIVI DI SEGNO OPPOSTO...

Nel 2017 ci sono accertamenti e impegni in partita di giro sul cds dell'istituto per ogni compenso...questi vengono ribaltati a 0.
Nel 2018:

 nel caso di ammontare positivo nel 2018 viene creato un unico impegno a residuo tronco (con accertamento a 0) per tutti gli istituti...sulla uo 000.407......l'impegno viene registrato su liquid_gruppo_cori,
e l'importo dell'impegno deriva dalla somma di tutte le liquidazioni positive degli istituti, somma che si puÃ² ricavare da questa select:


select sum(im_liquidato)
from liquid_gruppo_cori
where
 ESERCIZIO = 2018
 AND cd_gruppo_cr = '1004'
AND IM_LIQUIDATO > 0
 AND cd_cds  != '000'
 AND PG_LIQUIDAZIONE = 0
 AND (ESERCIZIO_ORI_OBB_ACCENTR = 2017 or
 ESERCIZIO_ORI_OBB_ACCENTR is null)

 nel caso di ammontare negativo per ciascun istituto viene creato un accertamento a residuo tronco (con impegno a 0)...sulla uo 000.407...per ogni istituto che ha ritenute da versare...l'accertamento viene registrato su liquid_gruppo_centro_comp,
e l'importo dell'accertamento deriva dalla somma algebrica di impegni e accertamenti dell'istituto,

le somme si possono ricavare da questa select:

select sum(ammontare) from
contributo_ritenuta c, liquid_gruppo_Cori_Det l
where c.esercizio = l.esercizio_contributo_ritenuta and
c.pg_compenso = l.pg_compenso and
C.CD_UNITA_ORGANIZZATIVA = l.cd_uo_origine and
c.cd_contributo_ritenuta = l.cd_contributo_ritenuta and
l.ESERCIZIO = 2018 and
l.cd_uo_origine = '014.000' and pg_liquidazione = 0 and
l.cd_gruppo_cr = '1004'




 CONTROLLARE BENE COME FA CON I GENERICI I MANDATI E LE REVERSALI.... FORSE DEVO MODIFICARE ANCHE ALTRO */



  if aAggregato.im_liquidato = 0 then
   -- ATTENZIONE!!! Se l'importo da liquidare Ã¨ 0 non c'Ã¨ niente da aggiornare al centro
   return;
  end if;

  recParametriCNR := CNRUTL001.getRecParametriCnr(aL.esercizio);
  aDateCont:=CNRCTB008.getTimestampContabile(aL.esercizio,aTSNow);

  aLGC:=null;

--pipe.send_message('1 '||  aL.esercizio);
  begin
   aLGC:=getGruppoCentroAperto(aL, aAggregato);
  exception when NO_DATA_FOUND then
   aLGC:=creaGruppoCentro(aL, aAggregato,aTSNow,aUser);
  end;

  -- Estraggo il terzo UO relativo all'UO SAC che accentra i versamenti
  CNRCTB080.GETTERZOPERUO(aUOVERSACC.cd_unita_organizzativa,aCdTerzoAcc, aCdModPagAcc, aPgBancaAcc,aL.esercizio);

--GGG1 DA CAMBIARE
  if aAggregato.im_liquidato < 0 Then
  --and recParametriCNR.FL_TESORERIA_UNICA != 'Y'
    aLGCC:=creaGruppoCentroComp(aL, aLGC, aAggregato,aTSNow,aUser);
    IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' THEN

--      CNRCTB080.getTerzoPerEnteContoBI(aUOVERSACC.cd_unita_organizzativa, aCdTerzoAcc_unica, aCdModPagAcc_unica, aPgBancaAcc_unica);

      BEGIN
        SELECT cd_terzo_versamento, CD_MODALITA_PAGAMENTO, PG_BANCA
        INTO  aCdTerzoAcc_unica, aCdModPagAcc_unica, aPgBancaAcc_unica
        FROM GRUPPO_CR_DET
        WHERE ESERCIZIO = aAggregato.esercizio AND
              CD_GRUPPO_CR = aAggregato.CD_GRUPPO_CR  AND
              CD_REGIONE = aAggregato.cd_regione AND
              PG_COMUNE = aAggregato.pg_comune;
      Exception
        WHEN Others THEN
         IBMERR001.RAISE_ERR_GENERICO('Errore nel recupero del terzo  per il gruppo '||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune||'. Errore:'||sqlerrm);
      END;

      aObbPG:=null;
      aObbPGScad:=null;

      if aL.da_esercizio_precedente = 'Y' then
        select NVL(sum(ammontare),0)
        INTO SOMMA_OPPOSTA
        from
        contributo_ritenuta c, liquid_gruppo_Cori_Det l
        where c.esercizio = l.esercizio_contributo_ritenuta and
        c.pg_compenso = l.pg_compenso and
        C.CD_UNITA_ORGANIZZATIVA = l.cd_uo_origine and
        c.cd_contributo_ritenuta = l.cd_contributo_ritenuta and
        C.TI_ENTE_PERCIPIENTE = l.TI_ENTE_PERCIPIENTE  and
        l.CD_CDS = aAggregato.CD_CDS and
        l.ESERCIZIO = aAggregato.esercizio and
        l.CD_UNITA_ORGANIZZATIVA = aAggregato.CD_UNITA_ORGANIZZATIVA and
        l.PG_LIQUIDAZIONE = aAggregato.PG_LIQUIDAZIONE and
        l.CD_CDS_ORIGINE = aAggregato.CD_CDS_ORIGINE and
        l.CD_UO_ORIGINE = aAggregato.CD_UO_ORIGINE and
        l.PG_LIQUIDAZIONE_ORIGINE = aAggregato.PG_LIQUIDAZIONE_ORIGINE and
        l.CD_GRUPPO_CR = aAggregato.CD_GRUPPO_CR and
        l.CD_REGIONE = aAggregato.CD_REGIONE and
        l.PG_COMUNE = aAggregato.PG_COMUNE and
        C.AMMONTARE > 0;
      END IF;
      begin
       select distinct a.cd_elemento_voce
       into aCdEV
       from ass_tipo_cori_ev a, tipo_cr_base b
       where b.esercizio = aAggregato.esercizio
         and b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
         and a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
         and a.esercizio = aAggregato.esercizio
         and a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
         and a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
      exception when TOO_MANY_ROWS then
         IBMERR001.RAISE_ERR_GENERICO('Esiste piÃ¹ di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
             when NO_DATA_FOUND then
               IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
      end;
      begin
        select  *
        into    elementoVoce
        from    elemento_Voce
        where esercizio = aAggregato.esercizio
          and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
          and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
          and cd_elemento_voce = aCdEV;
      exception when NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
      end;

      aAcc:=null;
      aAccScad:=null;
      aAcc.CD_CDS:=aUOVERSACC.cd_unita_padre;
      aAcc.ESERCIZIO:=aAggregato.esercizio;
      if aL.da_esercizio_precedente = 'Y' then
        aAcc.ESERCIZIO_ORIGINALE:=aAggregato.esercizio - 1;
        aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO_RES;
        aAcc.pg_accertamento:=CNRCTB018.getNextNumDocCont(CNRCTB018.TI_DOC_ACC_PGIRO, aAcc.ESERCIZIO_ORIGINALE, aAcc.CD_CDS, aUser);
      else
        aAcc.ESERCIZIO_ORIGINALE:=aAggregato.esercizio;
        aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO;
      end if;

      aAcc.CD_UNITA_ORGANIZZATIVA:=aUOVERSACC.cd_unita_organizzativa;
      aAcc.CD_CDS_ORIGINE:=aUOVERSACC.cd_unita_padre;
      aAcc.CD_UO_ORIGINE:=aUOVERSACC.cd_unita_organizzativa;
      aAcc.TI_APPARTENENZA:=elementoVoce.TI_APPARTENENZA;
      aAcc.TI_GESTIONE:=elementoVoce.TI_GESTIONE;
      -- Utilizzo come conto il primo conto che trovo di un CORI appartenente al gruppo
      aAcc.CD_ELEMENTO_VOCE:=aCdEV;
      aAcc.CD_VOCE:=elementoVoce.cd_elemento_voce;
      aAcc.DT_REGISTRAZIONE:=TRUNC(aDateCont);
      aAcc.DS_ACCERTAMENTO:='CORI-VA compensazione gruppo cr :'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
      aAcc.NOTE_ACCERTAMENTO:='';
      aAcc.CD_TERZO:=aCdTerzoAcc_unica;
      aAcc.FL_PGIRO:='Y';
      aAcc.RIPORTATO:='N';
      aAcc.DACR:=aTSNow;
      aAcc.UTCR:=aUser;
      aAcc.DUVA:=aTSNow;
      aAcc.UTUV:=aUser;
      aAcc.PG_VER_REC:=1;
      aAcc.ESERCIZIO_COMPETENZA:=aAcc.ESERCIZIO;
      aAcc.IM_ACCERTAMENTO:=abs(aAggregato.im_liquidato) + SOMMA_OPPOSTA;
      CNRCTB040.CREAACCERTAMENTOPGIROTRONC(false,aAcc,aAccScad,aObbPG,aObbPGScad,trunc(aTSNow));
      aAccPG:=aAcc;
      aAccPGScad:=aAccScad;

      aObbPG:=null;
      aObbPGScad:=null;


-- GG MODIFICA PER RIBALTARE ANCHE GLI IMPEGNI E NON CREARE SOLO GLI ACCERTAMENTI...IN QUESTO MODO IL FINE ANNO DEGLI IMPEGNI QUADRA CON L'INIZIO ANNO SUCCESSIVO
      IF SOMMA_OPPOSTA > 0 THEN
        begin
         select distinct a.cd_elemento_voce
         into aCdEV
         from ass_tipo_cori_ev a, tipo_cr_base b
         where b.esercizio = aAggregato.esercizio
           and b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
           and a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
           and a.esercizio = aAggregato.esercizio
           and a.ti_gestione = CNRCTB001.GESTIONE_SPESE
           and a.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS;
        exception when TOO_MANY_ROWS then
           IBMERR001.RAISE_ERR_GENERICO('Esiste piÃ¹ di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
               when NO_DATA_FOUND then
                 IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
        end;
        begin
          select  *
          into    elementoVoce
          from    elemento_Voce
          where esercizio = aAggregato.esercizio
            and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
            and ti_gestione = CNRCTB001.GESTIONE_SPESE
            and cd_elemento_voce = aCdEV;
        exception when NO_DATA_FOUND then
          IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
        end;

        aObb:=null;
        aObbScad:=null;
        aObb.CD_CDS:=aUOVERSACC.cd_unita_padre;
        aObb.ESERCIZIO:=aAggregato.esercizio;
        if aL.da_esercizio_precedente = 'Y' then
          aObb.ESERCIZIO_ORIGINALE:=aAggregato.esercizio - 1;
          aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO_RES;
          aObb.pg_obbligazione:=CNRCTB018.getNextNumDocCont(CNRCTB018.TI_DOC_OBB_PGIRO, aObb.ESERCIZIO_ORIGINALE, aObb.CD_CDS, aUser);
        else
          aObb.ESERCIZIO_ORIGINALE:=aAggregato.esercizio;
          aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
        end if;
        aObb.im_costi_anticipati:=0;

      aObb.fl_calcolo_automatico:='N';
      aObb.fl_spese_costi_altrui:='N';
        aObb.STATO_OBBLIGAZIONE := CNRCTB035.STATO_DEFINITIVO;
        aObb.CD_UNITA_ORGANIZZATIVA:=aUOVERSACC.cd_unita_organizzativa;
        aObb.CD_CDS_ORIGINE:=aUOVERSACC.cd_unita_padre;
        aObb.CD_UO_ORIGINE:=aUOVERSACC.cd_unita_organizzativa;
        aObb.TI_APPARTENENZA:=elementoVoce.TI_APPARTENENZA;
        aObb.TI_GESTIONE:=elementoVoce.TI_GESTIONE;
        -- Utilizzo come conto il primo conto che trovo di un CORI appartenente al gruppo
        aObb.CD_ELEMENTO_VOCE:=aCdEV;
        aObb.DT_REGISTRAZIONE:=TRUNC(aDateCont);
        aObb.DS_OBBLIGAZIONE:='CORI-VA compensazione per il segno opposto gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
        aObb.NOTE_OBBLIGAZIONE:='';
        aObb.CD_TERZO:=aCdTerzoAcc_unica;
        aObb.FL_PGIRO:='Y';
        aObb.RIPORTATO:='N';
        aObb.DACR:=aTSNow;
        aObb.UTCR:=aUser;
        aObb.DUVA:=aTSNow;
        aObb.UTUV:=aUser;
        aObb.PG_VER_REC:=1;
        aObb.ESERCIZIO_COMPETENZA:=aObb.ESERCIZIO;
        aObb.IM_OBBLIGAZIONE:=SOMMA_OPPOSTA;
        CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObb,aObbScad,aAcc,aAccScad,trunc(aTSNow));
        aObbPG:=aObb;
        aObbPGScad:=aObbScad;
      END IF;
      TROVATO_DETTAGLIO_P_GIRO := 'N';
begin
      For i in 1..Nvl(tb_ass_pgiro.Count,0) Loop
        if  aAggregato.cd_cds = tb_ass_pgiro(i).cd_cds and
            aAggregato.esercizio = tb_ass_pgiro(i).esercizio and
            aAggregato.cd_unita_organizzativa = tb_ass_pgiro(i).cd_uo and
            aAggregato.pg_liquidazione = tb_ass_pgiro(i).pg_liq and
            aAggregato.cd_cds_origine = tb_ass_pgiro(i).cd_cds_orig and
            aAggregato.cd_uo_origine = tb_ass_pgiro(i).cd_uo_orig and
            aAggregato.pg_liquidazione_origine = tb_ass_pgiro(i).pg_liq_orig and
            aAggregato.cd_gruppo_cr = tb_ass_pgiro(i).cd_gr_cr and
            aAggregato.cd_regione = tb_ass_pgiro(i).cd_regione and
            aAggregato.pg_comune = tb_ass_pgiro(i).pg_comune then

            TROVATO_DETTAGLIO_P_GIRO := 'S';

            IF SOMMA_OPPOSTA > 0 THEN
              tb_ass_pgiro(i).CD_CDS_OBB_PGIRO_OPP  := aObbPG.cd_cds;
              tb_ass_pgiro(i).ES_OBB_PGIRO_OPP      := aObbPG.esercizio;
              tb_ass_pgiro(i).ES_ORIG_OBB_PGIRO_OPP := aObbPG.esercizio_originale;
              tb_ass_pgiro(i).PG_OBB_PGIRO_OPP      := aObbPG.PG_OBBLIGAZIONE;
            END IF;

            tb_ass_pgiro(i).cd_cds_acc_pgiro  := aAccPG.cd_cds;
            tb_ass_pgiro(i).es_acc_pgiro      := aAccPG.esercizio;
            tb_ass_pgiro(i).es_orig_acc_pgiro := aAccPG.esercizio_originale;
            tb_ass_pgiro(i).pg_acc_pgiro      := aAccPG.pg_accertamento;

            tb_ass_pgiro(i).ti_origine_pgiro := 'E';
        end if;
      End Loop;
 Exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Errore a ');
end;
      IF TROVATO_DETTAGLIO_P_GIRO = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO('Per il cds '||aAggregato.cd_cds||', l''esercizio '||aAggregato.esercizio||', la UO '||aAggregato.cd_unita_organizzativa||', la liquidazione '||aAggregato.pg_liquidazione||
              ', il gruppo '||aAggregato.cd_gruppo_cr||', la regione '||aAggregato.cd_regione||', il comune '||aAggregato.pg_comune||' non Ã¨ stata trovata la partita di giro di dettaglio');
      END IF;

    ELSE
      aAccPG:=null;
      aAccPGScad:=null;
      begin
       select distinct a.cd_elemento_voce
       into aCdEV
       from ass_tipo_cori_ev a, tipo_cr_base b
       where
               b.esercizio = aAggregato.esercizio
              and b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
              and a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
              and a.esercizio = aAggregato.esercizio
              and a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
              and a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
      exception when TOO_MANY_ROWS then
                 IBMERR001.RAISE_ERR_GENERICO('Esiste piÃ¹ di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
               when NO_DATA_FOUND then
                 IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
      end;
      begin
        select * into aContoColl from ass_partita_giro where
            esercizio = aAggregato.esercizio
        and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
        and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
        and cd_voce = aCdEV;
      exception when NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Conto associato in partita di giro non trovato per voce di entrata:'||aCdEV);
      end;

      -- Estraggo il terzo UO relativo all'UO SAC che accentra i versamenti
      CNRCTB080.GETTERZOPERUO(aLGCC.cd_unita_organizzativa,aCdTerzoUo, aCdModPagUo, aPgBancaUo,aAggregato.esercizio);

      aObb:=null;
      aObbScad:=null;
      aObb.CD_CDS:=aUOVERSACC.cd_unita_padre;
      aObb.ESERCIZIO:=aAggregato.esercizio;
      aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
      aObb.CD_UNITA_ORGANIZZATIVA:=aUOVERSACC.cd_unita_organizzativa;
      aObb.CD_CDS_ORIGINE:=aUOVERSACC.cd_unita_padre;
      aObb.CD_UO_ORIGINE:=aUOVERSACC.cd_unita_organizzativa;
      aObb.TI_APPARTENENZA:=aContoColl.ti_appartenenza_clg;
      aObb.TI_GESTIONE:=aContoColl.ti_gestione_clg;
      aObb.CD_ELEMENTO_VOCE:=aContoColl.cd_voce_clg;
      aObb.DT_REGISTRAZIONE:=TRUNC(aDateCont);
      aObb.DS_OBBLIGAZIONE:='CORI-VA compensazione gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
      aObb.NOTE_OBBLIGAZIONE:='';
      aObb.CD_TERZO:=aCdTerzoUo;
      aObb.IM_OBBLIGAZIONE:=abs(aAggregato.im_liquidato);
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
      aObb.ESERCIZIO_COMPETENZA:=aAggregato.esercizio;

      CNRCTB030.CREAOBBLIGAZIONEPGIRO(false,aObb,aObbScad,aAccPG,aAccPGScad,trunc(aTSNow));
    END IF;
    update liquid_gruppo_centro_comp set
           cd_cds_acc_accentr = aAccPG.cd_cds
          ,esercizio_acc_accentr = aAccPG.esercizio
          ,esercizio_ori_acc_accentr = aAccPG.esercizio_originale
          ,pg_acc_accentr = aAccPG.pg_accertamento
          ,cd_cds_OBB_accentr_OPP = aObbPG.cd_cds
          ,ES_OBB_ACCENTR_OPP = aObbPG.esercizio
          ,ES_ORIG_OBB_ACCENTR_OPP = aObbPG.esercizio_originale
          ,PG_OBB_ACCENTR_OPP = aObbPG.pg_obbligazione
    where esercizio = aLGC.esercizio
        and cd_gruppo_cr = aLGC.cd_gruppo_cr
        and cd_regione = aLGC.cd_regione
        and pg_comune = aLGC.pg_comune
        and pg_gruppo_centro = aLGC.pg_gruppo_centro
        and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa;
  else  -- aAggregato.im_liquidato > 0

    if aL.da_esercizio_precedente = 'Y' then
      select abs(NVL(sum(ammontare),0) )
      INTO SOMMA_OPPOSTA
      from
      contributo_ritenuta c, liquid_gruppo_Cori_Det l
      where c.esercizio = l.esercizio_contributo_ritenuta and
      c.pg_compenso = l.pg_compenso and
      C.CD_UNITA_ORGANIZZATIVA = l.cd_uo_origine and
      c.cd_contributo_ritenuta = l.cd_contributo_ritenuta and
      C.TI_ENTE_PERCIPIENTE = l.TI_ENTE_PERCIPIENTE  and
      l.CD_CDS = aAggregato.CD_CDS and
      l.ESERCIZIO = aAggregato.esercizio and
      l.CD_UNITA_ORGANIZZATIVA = aAggregato.CD_UNITA_ORGANIZZATIVA and
      l.PG_LIQUIDAZIONE = aAggregato.PG_LIQUIDAZIONE and
      l.CD_CDS_ORIGINE = aAggregato.CD_CDS_ORIGINE and
      l.CD_UO_ORIGINE = aAggregato.CD_UO_ORIGINE and
      l.PG_LIQUIDAZIONE_ORIGINE = aAggregato.PG_LIQUIDAZIONE_ORIGINE and
      l.CD_GRUPPO_CR = aAggregato.CD_GRUPPO_CR and
      l.CD_REGIONE = aAggregato.CD_REGIONE and
      l.PG_COMUNE = aAggregato.PG_COMUNE and
      C.AMMONTARE < 0;
    END IF;
   if aLGC.pg_obb_accentr is not null then
    --CNRCTB043.modificaPraticaObb(aLGC.esercizio_obb_accentr,aLGC.cd_cds_obb_accentr,aLGC.esercizio_ori_obb_accentr,aLGC.pg_obb_accentr,aAggregato.im_liquidato,aTSNow,aUser);
    --aggiunto l'ultimo parametro aAccConScad = 'Y' per indicare che sono gestite le scadenze per le pgiro di entrata
    IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' THEN
      aObbGiro := null;
      aObbGiro.esercizio:=aLGC.esercizio_obb_accentr;
      aObbGiro.cd_cds:=aLGC.cd_cds_obb_accentr;
      aObbGiro.esercizio_originale:=aLGC.esercizio_ori_obb_accentr;
      aObbGiro.pg_obbligazione:=aLGC.pg_obb_accentr;

      CNRCTB035.GETPGIROCDS(aObbGiro,aObbScadGiro,aObbScadVoceGiro,aAccGiro,aAccScadGiro,aAccScadVoceGiro);

      CNRCTB043.modificaPraticaAcc(aAccGiro.esercizio,aAccGiro.cd_cds,aAccGiro.esercizio_originale,aAccGiro.pg_accertamento,aAggregato.im_liquidato + somma_opposta,aTSNow,aUser);
      TROVATO_DETTAGLIO_P_GIRO := 'N';
begin
      For i in 1..Nvl(tb_ass_pgiro.Count,0) Loop
        if  aAggregato.cd_cds = tb_ass_pgiro(i).cd_cds and
            aAggregato.esercizio = tb_ass_pgiro(i).esercizio and
            aAggregato.cd_unita_organizzativa = tb_ass_pgiro(i).cd_uo and
            aAggregato.pg_liquidazione = tb_ass_pgiro(i).pg_liq and
            aAggregato.cd_cds_origine = tb_ass_pgiro(i).cd_cds_orig and
            aAggregato.cd_uo_origine = tb_ass_pgiro(i).cd_uo_orig and
            aAggregato.pg_liquidazione_origine = tb_ass_pgiro(i).pg_liq_orig and
            aAggregato.cd_gruppo_cr = tb_ass_pgiro(i).cd_gr_cr and
            aAggregato.cd_regione = tb_ass_pgiro(i).cd_regione and
            aAggregato.pg_comune = tb_ass_pgiro(i).pg_comune then

            TROVATO_DETTAGLIO_P_GIRO := 'S';

            tb_ass_pgiro(i).cd_cds_obb_pgiro  := aObbGiro.cd_cds;
            tb_ass_pgiro(i).es_obb_pgiro      := aObbGiro.esercizio;
            tb_ass_pgiro(i).es_orig_obb_pgiro := aObbGiro.esercizio_originale;
            tb_ass_pgiro(i).pg_obb_pgiro      := aObbGiro.pg_obbligazione;
            tb_ass_pgiro(i).ti_origine_pgiro := 'S';
        end if;
      End Loop;
 Exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Errore b ');
end;
      IF TROVATO_DETTAGLIO_P_GIRO = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO('Per il cds '||aAggregato.cd_cds||', l''esercizio '||aAggregato.esercizio||', la UO '||aAggregato.cd_unita_organizzativa||', la liquidazione '||aAggregato.pg_liquidazione||
              ', il gruppo '||aAggregato.cd_gruppo_cr||', la regione '||aAggregato.cd_regione||', il comune '||aAggregato.pg_comune||' non Ã¨ stata trovata la partita di giro di dettaglio');
      END IF;
    ELSE
      CNRCTB043.modificaPraticaObb(aLGC.esercizio_obb_accentr,aLGC.cd_cds_obb_accentr,aLGC.esercizio_ori_obb_accentr,aLGC.pg_obb_accentr,aAggregato.im_liquidato,aTSNow,aUser,aAccConScad);
    END IF;
   else
    aObbPG:=null;
    aObbPGScad:=null;
    IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' THEN
      aObbNew:=null;
      aObbScadNew:=null;
      aAccNew:=null;
      aAccScadNew:=null;
      --determino il capitolo da mettere sulla pgiro
      Begin
         Select distinct a.cd_elemento_voce
         Into aCdEV
         From ass_tipo_cori_ev a, tipo_cr_base b
         Where b.esercizio = aAggregato.esercizio
           And b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
           And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
           And a.esercizio = aAggregato.esercizio
           And a.ti_gestione = CNRCTB001.GESTIONE_SPESE
           And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS;
      Exception
              when TOO_MANY_ROWS then
                   IBMERR001.RAISE_ERR_GENERICO('Esiste piÃ¹ di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
              when NO_DATA_FOUND then
                   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
      End;

      Begin
         Select *
         Into elementoVoce
         From elemento_voce
         Where esercizio = aAggregato.esercizio
           And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
           And ti_gestione = CNRCTB001.GESTIONE_SPESE
           And cd_elemento_voce = aCdEV;
      Exception
              when NO_DATA_FOUND then
                 IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
      End;

      aObbNew.CD_CDS:=aUOVERSACC.cd_unita_padre;
      aObbNew.ESERCIZIO:=aAggregato.esercizio;
      if aL.da_esercizio_precedente = 'Y' then
        aObbNew.ESERCIZIO_ORIGINALE:=aAggregato.esercizio - 1;
        aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO_RES;
        aObbNew.pg_obbligazione:=CNRCTB018.getNextNumDocCont(CNRCTB018.TI_DOC_OBB_PGIRO, aObbNew.ESERCIZIO_ORIGINALE, aObbNew.CD_CDS, aUser);
      else
        aObbNew.ESERCIZIO_ORIGINALE:=aAggregato.esercizio;
        aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
      end if;
      aObbNew.CD_UNITA_ORGANIZZATIVA:=aUOVERSACC.cd_unita_organizzativa;
      aObbNew.CD_CDS_ORIGINE:=aUOVERSACC.cd_unita_padre;
      aObbNew.CD_UO_ORIGINE:=aUOVERSACC.cd_unita_organizzativa;
      aObbNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
      aObbNew.TI_GESTIONE:=elementoVoce.ti_gestione;
      aObbNew.CD_ELEMENTO_VOCE:=aCdEV;
      aObbNew.DT_REGISTRAZIONE:=TRUNC(aDateCont);
      aObbNew.DS_OBBLIGAZIONE:='CORI-VA gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
      aObbNew.NOTE_OBBLIGAZIONE:='';

      aObbNew.CD_TERZO:=CNRCTB015.GETIM01PERCHIAVE(CNRCTB035.TERZO_SPECIALE,CNRCTB035.CODICE_DIVERSI_PGIRO);

      aObbNew.IM_OBBLIGAZIONE:=aAggregato.im_liquidato;
      aObbNew.stato_obbligazione:=CNRCTB035.STATO_DEFINITIVO;
      aObbNew.im_costi_anticipati:=0;
      aObbNew.fl_calcolo_automatico:='N';
      aObbNew.fl_spese_costi_altrui:='N';
      aObbNew.FL_PGIRO:='Y';
      aObbNew.RIPORTATO:='N';
      aObbNew.DACR:=aTSNow;
      aObbNew.UTCR:=aUser;
      aObbNew.DUVA:=aTSNow;
      aObbNew.UTUV:=aUser;
      aObbNew.PG_VER_REC:=1;
      aObbNew.ESERCIZIO_COMPETENZA:=aObbNew.ESERCIZIO;

      CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObbNew,aObbScadNew,aAccNew,aAccScadNew,trunc(aTSNow));
      aObbPG:=aObbNew;
      aObbPGScad:=aObbScadNew;
      aAcc:=aAccNew;
      aAccScad:=aAccScadNew;



      TROVATO_DETTAGLIO_P_GIRO := 'N';


begin
      For i in 1..Nvl(tb_ass_pgiro.Count,0) Loop
        if  aAggregato.cd_cds = tb_ass_pgiro(i).cd_cds and
            aAggregato.esercizio = tb_ass_pgiro(i).esercizio and
            aAggregato.cd_unita_organizzativa = tb_ass_pgiro(i).cd_uo and
            aAggregato.pg_liquidazione = tb_ass_pgiro(i).pg_liq and
            aAggregato.cd_cds_origine = tb_ass_pgiro(i).cd_cds_orig and
            aAggregato.cd_uo_origine = tb_ass_pgiro(i).cd_uo_orig and
            aAggregato.pg_liquidazione_origine = tb_ass_pgiro(i).pg_liq_orig and
            aAggregato.cd_gruppo_cr = tb_ass_pgiro(i).cd_gr_cr and
            aAggregato.cd_regione = tb_ass_pgiro(i).cd_regione and
            aAggregato.pg_comune = tb_ass_pgiro(i).pg_comune then

            TROVATO_DETTAGLIO_P_GIRO := 'S';

            tb_ass_pgiro(i).cd_cds_obb_pgiro  := aObbNew.cd_cds;
            tb_ass_pgiro(i).es_obb_pgiro      := aObbNew.esercizio;
            tb_ass_pgiro(i).es_orig_obb_pgiro := aObbNew.esercizio_originale;
            tb_ass_pgiro(i).pg_obb_pgiro      := aObbNew.pg_obbligazione;
            tb_ass_pgiro(i).ti_origine_pgiro := 'S';
        end if;
      End Loop;
 Exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Errore c1 ');
end;
      IF TROVATO_DETTAGLIO_P_GIRO = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO('Per il cds '||aAggregato.cd_cds||', l''esercizio '||aAggregato.esercizio||', la UO '||aAggregato.cd_unita_organizzativa||', la liquidazione '||aAggregato.pg_liquidazione||
              ', il gruppo '||aAggregato.cd_gruppo_cr||', la regione '||aAggregato.cd_regione||', il comune '||aAggregato.pg_comune||' non Ã¨ stata trovata la partita di giro di dettaglio');
      END IF;
    ELSE
      aObbPG:=null;
      aObbPGScad:=null;
      begin
       select distinct a.cd_elemento_voce
       into aCdEV
       from ass_tipo_cori_ev a, tipo_cr_base b
       where b.esercizio = aAggregato.esercizio
         and b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
         and a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
         and a.esercizio = aAggregato.esercizio
         and a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
         and a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
      exception when TOO_MANY_ROWS then
         IBMERR001.RAISE_ERR_GENERICO('Esiste piÃ¹ di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
             when NO_DATA_FOUND then
               IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
      end;
      begin
        select  *
        into    elementoVoce
        from    elemento_voce
        where esercizio = aAggregato.esercizio
          and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
          and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
          and cd_elemento_voce = aCdEV;
      exception when NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
      end;

      aAcc:=null;
      aAccScad:=null;
      aAcc.CD_CDS:=aUOVERSACC.cd_unita_padre;
      aAcc.ESERCIZIO:=aAggregato.esercizio;
      aAcc.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO;
      aAcc.CD_UNITA_ORGANIZZATIVA:=aUOVERSACC.cd_unita_organizzativa;
      aAcc.CD_CDS_ORIGINE:=aUOVERSACC.cd_unita_padre;
      aAcc.CD_UO_ORIGINE:=aUOVERSACC.cd_unita_organizzativa;
      aAcc.TI_APPARTENENZA:=elementoVoce.ti_APPARTENENZA;
      aAcc.TI_GESTIONE:=elementoVoce.ti_GESTIONE;
      -- Utilizzo come conto il primo conto che trovo di un CORI appartenente al gruppo
      aAcc.CD_ELEMENTO_VOCE:=aCdEV;
      aAcc.CD_VOCE:=elementoVoce.cd_elemento_voce;
      aAcc.DT_REGISTRAZIONE:=TRUNC(aDateCont);
      aAcc.DS_ACCERTAMENTO:='CORI-VA gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
      aAcc.NOTE_ACCERTAMENTO:='';
      aAcc.CD_TERZO:=aCdTerzoAcc;
      aAcc.FL_PGIRO:='Y';
      aAcc.RIPORTATO:='N';
      aAcc.DACR:=aTSNow;
      aAcc.UTCR:=aUser;
      aAcc.DUVA:=aTSNow;
      aAcc.UTUV:=aUser;
      aAcc.PG_VER_REC:=1;
      aAcc.ESERCIZIO_COMPETENZA:=aAggregato.esercizio;
      aAcc.IM_ACCERTAMENTO:=aAggregato.im_liquidato;
      CNRCTB040.CREAACCERTAMENTOPGIRO(false,aAcc,aAccScad,aObbPG,aObbPGScad,trunc(aTSNow));

      -- Crea generico e reversale provvisoria

      aAnagTst:=CNRCTB080.GETANAG(aCdTerzoAcc);

      aGen:=null;
      aGenRiga:=null;
      aListGenRighe.delete;
      aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_ACC_ENTRATA;
      aGen.CD_CDS:=aAcc.cd_cds;
      aGen.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
      aGen.ESERCIZIO:=aAcc.esercizio;
      aGen.CD_CDS_ORIGINE:=aAcc.cd_cds;
      aGen.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
      aGen.IM_TOTALE:=aAcc.im_accertamento;
      aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
      aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
      aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);
      aGen.DS_DOCUMENTO_GENERICO:='CORI-VA gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
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
      aGenRiga.RAGIONE_SOCIALE:=aAnagTst.ragione_sociale;
      aGenRiga.NOME:=aAnagTst.nome;
      aGenRiga.COGNOME:=aAnagTst.cognome;
      aGenRiga.CODICE_FISCALE:=aAnagTst.codice_fiscale;
      aGenRiga.PARTITA_IVA:=aAnagTst.partita_iva;
      aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
      aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
      aGenRiga.STATO_COFI:=aGen.STATO_COFI;
      aGenRiga.CD_TERZO:=aAcc.cd_terzo;
      aGenRiga.CD_CDS_ACCERTAMENTO:=aAcc.CD_CDS;
      aGenRiga.ESERCIZIO_ACCERTAMENTO:=aAcc.ESERCIZIO;
      aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aAcc.ESERCIZIO_ORIGINALE;
      aGenRiga.PG_ACCERTAMENTO:=aAcc.PG_ACCERTAMENTO;
      aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
      aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoAcc;
      aGenRiga.PG_BANCA_UO_CDS:=aPgBancaAcc;
      aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagAcc;
      aGenRiga.DACR:=aGen.DACR;
      aGenRiga.UTCR:=aGen.UTCR;
      aGenRiga.UTUV:=aGen.UTUV;
      aGenRiga.DUVA:=aGen.DUVA;
      aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
      aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
      aListGenRighe(1):=aGenRiga;
      CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

      aRev:=null;
      aRevRiga:=null;
      aRev.CD_CDS:=aAcc.cd_cds;
      aRev.ESERCIZIO:=aAcc.esercizio;
      aRev.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
      aRev.CD_CDS_ORIGINE:=aAcc.cd_cds;
      aRev.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
      aRev.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV_PROVV;
      aRev.TI_REVERSALE:=CNRCTB038.TI_REV_SOS;
      aRev.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
      aRev.DS_REVERSALE:='CORI-VA gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
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
      aRevRiga.PG_ACCERTAMENTO_SCADENZARIO:=aAccScad.pg_accertamento_scadenzario;
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
      CNRCTB037.generaRevProvvPgiro(aRev, aRevRiga, aTSNow, aUser);
    END IF;

    update  liquid_gruppo_centro
    set     cd_cds_obb_accentr = aObbPG.cd_cds
         ,esercizio_obb_accentr = aObbPG.esercizio
         ,esercizio_ori_obb_accentr = aObbPG.esercizio_originale
         ,pg_obb_accentr = aObbPG.pg_obbligazione
    where esercizio = aLGC.esercizio
      And cd_gruppo_cr = aLGC.cd_gruppo_cr
      and cd_regione = aLGC.cd_regione
      and pg_comune = aLGC.pg_comune
      and pg_gruppo_centro = aLGC.pg_gruppo_centro;
-- Fix mancato aggiornamento pg_obb_accentr su liquid_gruppo_cori_locale alla creazione obb accentr
-- sul gruppo CORI al centro

    aLGC.cd_cds_obb_accentr:=aObbPG.cd_cds;
    aLGC.esercizio_obb_accentr:=aObbPG.esercizio;
    aLGC.esercizio_ori_obb_accentr:=aObbPG.esercizio_originale;
    aLGC.pg_obb_accentr:=aObbPG.pg_obbligazione;
   end if;
  end if;

   if somma_opposta > 0 and recParametriCNR.FL_TESORERIA_UNICA = 'Y' then
    if aLGC.PG_ACC_ACCENTR_OPP is not null then
      aObbGiro := null;

      aAccGiroOpp.esercizio:=aLGC.ES_ACC_ACCENTR_OPP;
      aAccGiroOpp.cd_cds:=aLGC.CD_CDS_ACC_ACCENTR_OPP;
      aAccGiroOpp.esercizio_originale:=aLGC.ES_ORIG_ACC_ACCENTR_OPP;
      aAccGiroOpp.pg_ACCERTAMENTO:=aLGC.PG_ACC_ACCENTR_OPP;

      CNRCTB035.GETPGIROCDS(aAccGiroOpp,aAccScadGiroOpp,aAccScadVoceGiroOpp,aObbGiroOpp,aObbScadGiroOpp,aObbScadVoceGiroOpp);

      CNRCTB043.modificaPraticaObb(aObbGiroOpp.esercizio,aObbGiroOpp.cd_cds,aObbGiroOpp.esercizio_originale,aObbGiroOpp.pg_OBBLIGAZIONE,somma_opposta,aTSNow,aUser,'N', 'N');
      TROVATO_DETTAGLIO_P_GIRO := 'N';
begin
      For i in 1..Nvl(tb_ass_pgiro.Count,0) Loop
        if  aAggregato.cd_cds = tb_ass_pgiro(i).cd_cds and
            aAggregato.esercizio = tb_ass_pgiro(i).esercizio and
            aAggregato.cd_unita_organizzativa = tb_ass_pgiro(i).cd_uo and
            aAggregato.pg_liquidazione = tb_ass_pgiro(i).pg_liq and
            aAggregato.cd_cds_origine = tb_ass_pgiro(i).cd_cds_orig and
            aAggregato.cd_uo_origine = tb_ass_pgiro(i).cd_uo_orig and
            aAggregato.pg_liquidazione_origine = tb_ass_pgiro(i).pg_liq_orig and
            aAggregato.cd_gruppo_cr = tb_ass_pgiro(i).cd_gr_cr and
            aAggregato.cd_regione = tb_ass_pgiro(i).cd_regione and
            aAggregato.pg_comune = tb_ass_pgiro(i).pg_comune then

            TROVATO_DETTAGLIO_P_GIRO := 'S';

            tb_ass_pgiro(i).CD_CDS_ACC_PGIRO_OPP  := aAccGiroOpp.cd_cds;
            tb_ass_pgiro(i).ES_ACC_PGIRO_OPP      := aAccGiroOpp.esercizio;
            tb_ass_pgiro(i).ES_ORIG_ACC_PGIRO_OPP := aAccGiroOpp.esercizio_originale;
            tb_ass_pgiro(i).PG_ACC_PGIRO_OPP      := aAccGiroOpp.PG_ACCERTAMENTO;

        end if;
      End Loop;
 Exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Errore b ');
end;
      IF TROVATO_DETTAGLIO_P_GIRO = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO('Per il cds '||aAggregato.cd_cds||', l''esercizio '||aAggregato.esercizio||', la UO '||aAggregato.cd_unita_organizzativa||', la liquidazione '||aAggregato.pg_liquidazione||
              ', il gruppo '||aAggregato.cd_gruppo_cr||', la regione '||aAggregato.cd_regione||', il comune '||aAggregato.pg_comune||' non Ã¨ stata trovata la partita di giro di dettaglio');
      END IF;
    else
      aAccNew:=null;
      aAccScadNew:=null;
      Begin
         Select distinct a.cd_elemento_voce
         Into aCdEV
         From ass_tipo_cori_ev a, tipo_cr_base b
         Where b.esercizio = aAggregato.esercizio
           And b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
           And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
           And a.esercizio = aAggregato.esercizio
           And a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
           And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
      Exception
              when TOO_MANY_ROWS then
                   IBMERR001.RAISE_ERR_GENERICO('Esiste piÃ¹ di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
              when NO_DATA_FOUND then
                   IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
      End;

      Begin
         Select *
         Into elementoVoce
         From elemento_voce
         Where esercizio = aAggregato.esercizio
           And ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
           And ti_gestione = CNRCTB001.GESTIONE_ENTRATE
           And cd_elemento_voce = aCdEV;
      Exception
              when NO_DATA_FOUND then
                 IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
      End;

      aAccNew.CD_CDS:=aUOVERSACC.cd_unita_padre;
      aAccNew.ESERCIZIO:=aAggregato.esercizio;
      if aL.da_esercizio_precedente = 'Y' then
        aAccNew.ESERCIZIO_ORIGINALE:=aAggregato.esercizio - 1;
        aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO_RES;
        aAccNew.pg_accertamento:=CNRCTB018.getNextNumDocCont(CNRCTB018.TI_DOC_ACC_PGIRO, aAccNew.ESERCIZIO_ORIGINALE, aAccNew.CD_CDS, aUser);
      else
        aAccNew.ESERCIZIO_ORIGINALE:=aAggregato.esercizio;
        aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_PGIRO;
      end if;
      aAccNew.CD_UNITA_ORGANIZZATIVA:=aUOVERSACC.cd_unita_organizzativa;
      aAccNew.CD_CDS_ORIGINE:=aUOVERSACC.cd_unita_padre;
      aAccNew.CD_UO_ORIGINE:=aUOVERSACC.cd_unita_organizzativa;
      aAccNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
      aAccNew.TI_GESTIONE:=elementoVoce.ti_gestione;
      aAccNew.CD_ELEMENTO_VOCE:=aCdEV;
      aAccNew.CD_VOCE:=aCdEV;
      aAccNew.DT_REGISTRAZIONE:=TRUNC(aDateCont);
      aAccNew.DS_ACCERTAMENTO:='CORI-VA per il segno opposto gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
      aAccNew.NOTE_ACCERTAMENTO:='';

      aAccNew.CD_TERZO:=CNRCTB015.GETIM01PERCHIAVE(CNRCTB035.TERZO_SPECIALE,CNRCTB035.CODICE_DIVERSI_PGIRO);

      aAccNew.IM_ACCERTAMENTO:=SOMMA_OPPOSTA;
      aAccNew.fl_calcolo_automatico:='N';
      aAccNew.FL_PGIRO:='Y';
      aAccNew.RIPORTATO:='N';
      aAccNew.DACR:=aTSNow;
      aAccNew.UTCR:=aUser;
      aAccNew.DUVA:=aTSNow;
      aAccNew.UTUV:=aUser;
      aAccNew.PG_VER_REC:=1;
      aAccNew.ESERCIZIO_COMPETENZA:=aAggregato.esercizio;

      CNRCTB040.CREAACCERTAMENTOPGIROTRONC(false,aAccNew,aAccScadNew,aObbGiroOpp,aObbScadGiroOpp,trunc(aTSNow));
      aAcc:=aAccNew;
      aAccScad:=aAccScadNew;
      TROVATO_DETTAGLIO_P_GIRO := 'N';


begin
      For i in 1..Nvl(tb_ass_pgiro.Count,0) Loop
        if  aAggregato.cd_cds = tb_ass_pgiro(i).cd_cds and
            aAggregato.esercizio = tb_ass_pgiro(i).esercizio and
            aAggregato.cd_unita_organizzativa = tb_ass_pgiro(i).cd_uo and
            aAggregato.pg_liquidazione = tb_ass_pgiro(i).pg_liq and
            aAggregato.cd_cds_origine = tb_ass_pgiro(i).cd_cds_orig and
            aAggregato.cd_uo_origine = tb_ass_pgiro(i).cd_uo_orig and
            aAggregato.pg_liquidazione_origine = tb_ass_pgiro(i).pg_liq_orig and
            aAggregato.cd_gruppo_cr = tb_ass_pgiro(i).cd_gr_cr and
            aAggregato.cd_regione = tb_ass_pgiro(i).cd_regione and
            aAggregato.pg_comune = tb_ass_pgiro(i).pg_comune then

            TROVATO_DETTAGLIO_P_GIRO := 'S';

            tb_ass_pgiro(i).CD_CDS_ACC_PGIRO_OPP  := aAcc.cd_cds;
            tb_ass_pgiro(i).ES_ACC_PGIRO_OPP      := aAcc.esercizio;
            tb_ass_pgiro(i).ES_ORIG_ACC_PGIRO_OPP := aAcc.esercizio_originale;
            tb_ass_pgiro(i).PG_ACC_PGIRO_OPP      := aAcc.PG_ACCERTAMENTO;
        end if;
      End Loop;
 Exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Errore c1 ');
end;
      IF TROVATO_DETTAGLIO_P_GIRO = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO('Per il cds '||aAggregato.cd_cds||', l''esercizio '||aAggregato.esercizio||', la UO '||aAggregato.cd_unita_organizzativa||', la liquidazione '||aAggregato.pg_liquidazione||
              ', il gruppo '||aAggregato.cd_gruppo_cr||', la regione '||aAggregato.cd_regione||', il comune '||aAggregato.pg_comune||' non Ã¨ stata trovata la partita di giro di dettaglio');
      END IF;
      update  liquid_gruppo_centro
      set     CD_CDS_ACC_ACCENTR_OPP = aAcc.cd_cds
           ,ES_ACC_ACCENTR_OPP = aAcc.esercizio
           ,ES_ORIG_ACC_ACCENTR_OPP = aAcc.esercizio_originale
           ,PG_ACC_ACCENTR_OPP = aAcc.pg_accertamento
      where esercizio = aLGC.esercizio
        And cd_gruppo_cr = aLGC.cd_gruppo_cr
        and cd_regione = aLGC.cd_regione
        and pg_comune = aLGC.pg_comune
        and pg_gruppo_centro = aLGC.pg_gruppo_centro;

      aLGC.CD_CDS_ACC_ACCENTR_OPP:=aAcc.cd_cds;
      aLGC.ES_ACC_ACCENTR_OPP:=aAcc.esercizio;
      aLGC.ES_ORIG_ACC_ACCENTR_OPP:=aAcc.esercizio_originale;
      aLGC.PG_ACC_ACCENTR_OPP:=aAcc.pg_accertamento;
    END IF;
   END IF;

  -- Aggiorna liquid_gruppo_cori con le informazioni relative all'obbligazione pgiro su UO che versa
  if aAggregato.im_liquidato < 0 Then
    update liquid_gruppo_cori
    set pg_gruppo_centro = aLGC.pg_gruppo_centro,
        stato = CNRCTB575.STATO_TRASFERITO
    where esercizio = aAggregato.esercizio
      and cd_cds = aAggregato.cd_cds
      and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
      and cd_cds_origine = aAggregato.cd_cds_origine
      and cd_uo_origine = aAggregato.cd_uo_origine
      and pg_liquidazione = aAggregato.pg_liquidazione
      and pg_liquidazione_origine = aAggregato.pg_liquidazione_origine
      and cd_gruppo_cr = aAggregato.cd_gruppo_cr
      and cd_regione = aAggregato.cd_regione
      and pg_comune = aAggregato.pg_comune;
  else
    update liquid_gruppo_cori
    set   cd_cds_obb_accentr = aLGC.cd_cds_obb_accentr,
          esercizio_obb_accentr = aLGC.esercizio_obb_accentr,
          esercizio_ori_obb_accentr = aLGC.esercizio_ori_obb_accentr,
          pg_obb_accentr = aLGC.pg_obb_accentr,
          pg_gruppo_centro = aLGC.pg_gruppo_centro,
          stato = CNRCTB575.STATO_TRASFERITO
    where esercizio = aAggregato.esercizio
      and cd_cds = aAggregato.cd_cds
      and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
      and cd_cds_origine = aAggregato.cd_cds_origine
      and cd_uo_origine = aAggregato.cd_uo_origine
      and pg_liquidazione = aAggregato.pg_liquidazione
      and pg_liquidazione_origine = aAggregato.pg_liquidazione_origine
      and cd_gruppo_cr = aAggregato.cd_gruppo_cr
      and cd_regione = aAggregato.cd_regione
      and pg_comune = aAggregato.pg_comune;
      IF SOMMA_OPPOSTA > 0 THEN
      update liquid_gruppo_cori
      set   CD_CDS_ACC_ACCENTR_OPP = aLGC.CD_CDS_ACC_ACCENTR_OPP,
            ES_ACC_ACCENTR_OPP = aLGC.ES_ACC_ACCENTR_OPP,
            ES_ORIG_ACC_ACCENTR_OPP = aLGC.ES_ORIG_ACC_ACCENTR_OPP,
            PG_ACC_ACCENTR_OPP = aLGC.PG_ACC_ACCENTR_OPP
      where esercizio = aAggregato.esercizio
        and cd_cds = aAggregato.cd_cds
        and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
        and cd_cds_origine = aAggregato.cd_cds_origine
        and cd_uo_origine = aAggregato.cd_uo_origine
        and pg_liquidazione = aAggregato.pg_liquidazione
        and pg_liquidazione_origine = aAggregato.pg_liquidazione_origine
        and cd_gruppo_cr = aAggregato.cd_gruppo_cr
        and cd_regione = aAggregato.cd_regione
        and pg_comune = aAggregato.pg_comune;
      END IF;
  end if;
  CREA_ASS_PGIRO_GR_C(tb_ass_pgiro, aUser, aTSNow);
 end;

 procedure vsx_liquida_cori(
       pgCall NUMBER
 ) is
   aEs number(4);
   aCdCds varchar2(30);
   aCdUo varchar2(30);
   aUOVERSACC unita_organizzativa%rowtype;
   aUOVERSCONTOBI unita_organizzativa%rowtype;
   CONTO_BI varchar2(1);
   aPgLiq number(8);
   aAssObbAcr ass_obb_acr_pgiro%rowtype;
   aUser varchar2(20);
   aTSNow date;
   aObb obbligazione%rowtype;
   aObbScad obbligazione_scadenzario%rowtype;
   aObbScadVoce obbligazione_scad_voce%rowtype;
   aAcc accertamento%rowtype;
   aAccScad accertamento_scadenzario%rowtype;
   aAccScadVoce accertamento_scad_voce%rowtype;
   aAccOrig accertamento%rowtype;
   aAccVC accertamento%rowtype;
   aObbVC obbligazione%rowtype;
   aGen documento_generico%rowtype;
   aGenRiga documento_generico_riga%rowtype;
   aListGenRighe CNRCTB100.docGenRigaList;
   aTotMandato number(15,2);
   aTotReversale number(15,2);
   aManP mandato%rowtype;
   aManPRiga mandato_riga%rowtype;
   aListRigheManP CNRCTB038.righeMandatoList;
   aRevP reversale%rowtype;
   aRevPRiga reversale_riga%rowtype;
   aListRigheRevP CNRCTB038.righeReversaleList;
   aDivisaEuro varchar2(30);
   aLiquidGruppoCori liquid_gruppo_cori_det%rowtype;
   aLiquidGruppoCoriDet liquid_gruppo_cori_det%rowtype;
   aTipoGenerico varchar2(10);
   aAnagTst anagrafico%rowtype;
   aLiquid liquid_cori%rowtype;
   aLiquidOrig liquid_cori%rowtype;
   isLiquidParzAcc boolean;
   aStatoLiquidazione char(1);
   elementoVoce elemento_voce%rowtype;
   aCdTerzoAcc number(8);
   aCdModPagAcc varchar2(10);
   aPgBancaAcc number(10);
   aCdTerzoUO number(8);
   aCdModPagUO varchar2(10);
   aPgBancaUO number(10);
   aCdTerzoUORev number(8);
   aCdModPagUORev varchar2(10);
   aPgBancaUORev number(10);
   lIsCdsInterDet boolean;
   lIsCdsInterTot boolean;
   aAccTmp accertamento%rowtype;
   aAccScadTmp accertamento_scadenzario%rowtype;
   aAccScadVoceTmp accertamento_scad_voce%rowtype;
   aObbTmp obbligazione%rowtype;
   aObbScadTmp obbligazione_scadenzario%rowtype;
   aObbScadVoceTmp obbligazione_scad_voce%rowtype;
   aCdEV varchar2(20);
   aEVComp elemento_voce%rowtype;
   aDateCont date;
   UOENTE unita_organizzativa%rowtype;
   tipo_ic  CHAR(1);
   aObbOld obbligazione%rowtype;
   aObbScadOld obbligazione_scadenzario%rowtype;
   aObbScadVoceOld obbligazione_scad_voce%rowtype;
   aAccOld accertamento%rowtype;
   aAccScadOld accertamento_scadenzario%rowtype;
   aAccScadVoceOld accertamento_scad_voce%rowtype;
   aObbNew obbligazione%rowtype;
   aObbScadNew obbligazione_scadenzario%rowtype;
   aObbScadVoceNew obbligazione_scad_voce%rowtype;
   aAccNew accertamento%rowtype;
   aAccScadNew accertamento_scadenzario%rowtype;
   aAccScadVoceNew accertamento_scad_voce%rowtype;
   aAccGiroOpp accertamento%rowtype;
   aAccScadGiroOpp accertamento_scadenzario%rowtype;
   aAccScadVoceGiroOpp accertamento_scad_voce%rowtype;
   aObbGiroOpp obbligazione%rowtype;
   aObbScadGiroOpp obbligazione_scadenzario%rowtype;
   aObbScadVoceGiroOpp obbligazione_scad_voce%rowtype;
   recParametriCNR PARAMETRI_CNR%Rowtype;
   ind_pGiro    number := 0;
   ESERCIZIO_UO_SPECIALI NUMBER;
   somma_compensazioni NUMBER;
   msg_acc varchar2(1000) := '';
   aCdTerzoVE number(8);
   aCdModPagVE varchar2(10);
   aPgBancaVE number(10);
 begin
  for aPar in (select * from vsx_liquidazione_cori where pg_call = pgCall
  ) loop
        aEs:=aPar.esercizio;
    aCdCds:=aPar.cd_cds;
    aCdUo:=aPar.cd_unita_organizzativa;
    aPgLiq:=aPar.pg_liquidazione;
    aUser:=aPar.utcr;
    aTSNow:=aPar.dacr;
  exit;
  end loop;

  if aEs is null then
     IBMERR001.RAISE_WRN_GENERICO('Nessun gruppo CORI trovato per la liquidazione');
  end if;
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);

  -- Controllo che la liq. per il centro sia aperta nell'esercizio specificato
  CNRCTB575.CHECKLIQUIDCENTROAPERTA(aEs);

  -- Check su esercizio
  if
   not CNRCTB008.ISESERCIZIOAPERTO(aEs, aCdCds)
  then
   IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||aEs||' non Ã¨ aperto per il CDS '||aCdCds);
  end if;

  aDateCont:=CNRCTB008.getTimestampContabile(aEs,aTSNow);

  begin
  select *
  into aLiquid
  from liquid_cori
  where esercizio = aEs
    and cd_cds = aCdCds
    and cd_unita_organizzativa = aCdUo
    and pg_liquidazione = aPgLiq
  for update nowait;
  exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Liquidazione CORI non trovata n.'||aPgLiq||' es.'||aEs||' cds:'||aCdCds||' uo:'||aCdUo);
  end;

  ESERCIZIO_UO_SPECIALI := to_number(To_Char(aLiquid.DT_DA,'YYYY'));
   -- Elimino il dettaglio minimo della liquidazione di aLGC non selezionati
  delete from liquid_gruppo_cori_det a where
       cd_cds = aCdCds
   and esercizio = aEs
   and cd_unita_organizzativa = aCdUo
   and pg_liquidazione = aPgLiq
   and not exists (select 1 from vsx_liquidazione_cori aParD
           where aParD.pg_call = pgCall
         and aParD.cd_cds = a.cd_cds
         and aParD.esercizio = a.esercizio
         and aParD.cd_unita_organizzativa = a.cd_unita_organizzativa
         and aParD.pg_liquidazione = a.pg_liquidazione
         and aParD.cd_cds_origine = a.cd_cds_origine
         and aParD.cd_uo_origine = a.cd_uo_origine
         and aParD.pg_liquidazione_origine = a.pg_liquidazione_origine
         and aParD.cd_gruppo_cr = a.cd_gruppo_cr
         and aParD.cd_regione = a.cd_regione
         and aParD.pg_comune = a.pg_comune );
   -- Elimino aLGC non selezionati
  delete from liquid_gruppo_cori a Where
       cd_cds = aCdCds
   and esercizio = aEs
   and cd_unita_organizzativa = aCdUo
   and pg_liquidazione = aPgLiq
   and not exists (select 1 from vsx_liquidazione_cori aParD
           where aParD.pg_call = pgCall
               and aParD.cd_cds = a.cd_cds
         and aParD.esercizio = a.esercizio
         and aParD.cd_unita_organizzativa = a.cd_unita_organizzativa
         and aParD.pg_liquidazione = a.pg_liquidazione
         and aParD.cd_cds_origine = a.cd_cds_origine
         and aParD.cd_uo_origine = a.cd_uo_origine
         and aParD.pg_liquidazione_origine = a.pg_liquidazione_origine
         and aParD.cd_gruppo_cr = a.cd_gruppo_cr
         and aParD.cd_regione = a.cd_regione
         and aParD.pg_comune = a.pg_comune);

      -- Flag che determina se l'UO in processo Ã¨ di CDS versato via interfaccia
      lIsCdsInterDet := IsCdsInterfDet(aCdCds,aEs);
      lIsCdsInterTot := IsCdsInterfTot(aCdCds,aEs);

      aDivisaEuro:=CNRCTB015.GETVAL01PERCHIAVE(CNRCTB100.CCNR_DIVISA,CNRCTB100.CCNR_EURO);
      aUOVERSACC:=CNRCTB020.getUOVersCori(ESERCIZIO_UO_SPECIALI);
      aUOVERSCONTOBI:=CNRCTB020.getUOVersCoriContoBI(ESERCIZIO_UO_SPECIALI);

     -- Controllo che la liq. per locale sia aperta nell'esercizio specificato
     if aUOVERSACC.cd_unita_organizzativa <> aCdUO and aUOVERSCONTOBI.cd_unita_organizzativa <> aCdUO then
        CNRCTB575.CHECKLIQUIDLOCALEAPERTA(aEs);
     end if;

     UOENTE:=CNRCTB020.GETUOENTE(aEs);

     -- Check su esercizio
     If Not CNRCTB008.ISESERCIZIOAPERTO(aEs, aUOVERSACC.cd_unita_padre)
     Then
        IBMERR001.RAISE_ERR_GENERICO('L''esercizio '||aEs||' non Ã¨ aperto per il CDS di versamento CORI '||aUOVERSACC.cd_unita_padre);
     End If;

     -- Nel caso di liq. via interfaccia, devo ripulire in interfaccia
     -- il puntatore alla liquidazione per quei gruppi che l'utente ha scelto di NON versare
     -- PER L'INTERFACCIA DI TIPO DETTAGLIO:
     If lIsCdsInterDet Then
       For aLCInterf in (
           Select * From liquid_cori_interf_dett alc
           Where esercizio=decode(aLiquid.da_esercizio_precedente,'Y',aEs-1,aEs)
             And cd_cds = aCdCds
             And cd_unita_organizzativa = aCdUo
             And pg_liquidazione = aPgLiq
             And cd_gruppo_cr is not null
             And Not Exists (
               Select 1 from vsx_liquidazione_cori aParD
               Where aParD.pg_call = pgCall
                 and aParD.cd_cds = aCdCds
                 and aParD.esercizio = aEs
                 and aParD.cd_unita_organizzativa = aCdUo
                 and aParD.pg_liquidazione = aPgLiq
                 and aParD.cd_uo_origine = cd_unita_organizzativa
                 and aParD.cd_gruppo_cr = alc.cd_gruppo_cr
                 and aParD.cd_regione = alc.cd_regione
                 and aParD.pg_comune = alc.pg_comune)
           For update nowait) Loop
         Update liquid_cori_interf_dett
         Set cd_gruppo_cr = null,
              pg_liquidazione = null,
              utuv = aUser,
              duva = aTSNow,
              pg_ver_rec = pg_ver_rec + 1
         Where
               CD_CDS=aLCInterf.CD_CDS AND
               ESERCIZIO=aLCInterf.ESERCIZIO AND
               CD_UNITA_ORGANIZZATIVA=aLCInterf.CD_UNITA_ORGANIZZATIVA AND
               PG_CARICAMENTO=aLCInterf.PG_CARICAMENTO AND
               DT_INIZIO=aLCInterf.DT_INIZIO AND
               DT_FINE=aLCInterf.DT_FINE AND
               MATRICOLA=aLCInterf.MATRICOLA AND
               CODICE_FISCALE=aLCInterf.CODICE_FISCALE AND
               TI_PAGAMENTO=aLCInterf.TI_PAGAMENTO AND
               ESERCIZIO_COMPENSO=aLCInterf.ESERCIZIO_COMPENSO AND
               CD_IMPONIBILE=aLCInterf.CD_IMPONIBILE AND
               TI_ENTE_PERCIPIENTE=aLCInterf.TI_ENTE_PERCIPIENTE AND
               CD_CONTRIBUTO_RITENUTA=aLCInterf.CD_CONTRIBUTO_RITENUTA;
       End Loop;
     End If; --DELL'INTERFACCIA DI TIPO DETTAGLIO
     -- PER L'INTERFACCIA DI TIPO TOTALE:
     If lIsCdsInterTot Then
       For aLCInterf in (
           Select * From liquid_cori_interf alc
           Where esercizio=decode(aLiquid.da_esercizio_precedente,'Y',aEs-1,aEs)
             And cd_cds = aCdCds
             And cd_unita_organizzativa = aCdUo
             And pg_liquidazione = aPgLiq
             And cd_gruppo_cr is not null
             And Not Exists (
               Select 1 from vsx_liquidazione_cori aParD
               Where aParD.pg_call = pgCall
                 and aParD.cd_cds = aCdCds
                 and aParD.esercizio = aEs
                 and aParD.cd_unita_organizzativa = aCdUo
                 and aParD.pg_liquidazione = aPgLiq
                 and aParD.cd_uo_origine = cd_unita_organizzativa
                 and aParD.cd_gruppo_cr = alc.cd_gruppo_cr
                 and aParD.cd_regione = alc.cd_regione
                 and aParD.pg_comune = alc.pg_comune)
           For update nowait) Loop
         Update liquid_cori_interf
         Set pg_liquidazione = null,
             utuv = aUser,
             duva = aTSNow,
             pg_ver_rec = pg_ver_rec + 1
         Where
               CD_CDS=aLCInterf.CD_CDS AND
               ESERCIZIO=aLCInterf.ESERCIZIO AND
               CD_UNITA_ORGANIZZATIVA=aLCInterf.CD_UNITA_ORGANIZZATIVA AND
               PG_CARICAMENTO=aLCInterf.PG_CARICAMENTO And
               CD_GRUPPO_CR=aLCInterf.CD_GRUPPO_CR And
               CD_REGIONE = aLCInterf.CD_REGIONE And
               PG_COMUNE = aLCInterf.PG_COMUNE And
               DT_INIZIO=aLCInterf.DT_INIZIO And
               DT_FINE=aLCInterf.DT_FINE;
       End Loop;
     End If;-- DELL'INTERFACCIA DI TIPO TOTALE
     -- Nel caso di UO di versamento accentrato, devo ripulire nei gruppi centro impattati
     -- il puntatore alla liquidazione per quei gruppi che l'utente ha scelto di NON versare
     If aUOVERSACC.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo then
       For aDGruppoCentro in (
           Select * From liquid_gruppo_centro
           Where esercizio=aEs
             And cd_cds_lc = aCdCds
             And cd_uo_lc = aCdUo
             And pg_lc = aPgLiq
             And stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
             For update nowait)
       Loop

         Declare
           aLNum number;
         Begin
           Select distinct 1 into aLNum
           From vsx_liquidazione_cori aParD
           Where aParD.pg_call = pgCall
             And aParD.cd_cds = aCdCds
             And aParD.esercizio = aEs
             And aParD.cd_unita_organizzativa = aCdUo
             And aParD.pg_liquidazione = aPgLiq
             And aParD.cd_uo_origine = UOENTE.cd_unita_organizzativa
             And aParD.cd_gruppo_cr = aDGruppoCentro.cd_gruppo_cr
             And aParD.cd_regione = aDGruppoCentro.cd_regione
             And aParD.pg_comune = aDGruppoCentro.pg_comune;
         Exception when NO_DATA_FOUND Then
                Update liquid_gruppo_centro
                Set cd_cds_lc = null,
                    cd_uo_lc = null,
                    pg_lc = null,
                    pg_ver_rec=pg_ver_rec+1
                Where esercizio = aEs
                  And cd_gruppo_cr = aDGruppoCentro.cd_gruppo_cr
                  And cd_regione = aDGruppoCentro.cd_regione
                  And pg_comune = aDGruppoCentro.pg_comune
                  And pg_gruppo_centro = aDGruppoCentro.pg_gruppo_centro;
         End;
       End loop;
     End If;

     -- Estraggo il terzo UO relativo all'UO SAC che accentra i versamenti
     CNRCTB080.GETTERZOPERUO(aUOVERSACC.cd_unita_organizzativa,aCdTerzoAcc, aCdModPagAcc, aPgBancaAcc,aEs);
     isLiquidParzAcc:=false;

     -- LIQUIDAZIONE DI UO
     -- CICLO SU AGGREGATI PROPRI
     -- Nel caso di UO di versamento centrale: raccolgo anche i gruppi dei versamenti locali
     -- Nel caso di UO di versamenti unificati: raccolgo anche i gruppi di tutte le UO della SAC

     -- Deve esistere un loop esterno che raggruppa i gruppi, per ognuno dei quali viene creato un Mandato
     -- (prima per ogni aggregato veniva creato un mandato poiche' non potevano esserci due uo con lo stesso gruppo)
     -- e il loop interno, gia' esistente, per ogni gruppo prende tutte le UO

  For aVersamenti In (Select a.cd_gruppo_cr,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato,Sum(a.im_liquidato) tot_liquidato, b.ti_mandato
                   From V_LIQUID_GRUPPO_CORI_CR_DET a, rif_modalita_pagamento b
           Where a.esercizio = aEs
                     And a.cd_cds = aCdCds
                     And a.cd_unita_organizzativa = aCdUo
                     And a.pg_liquidazione = aPgLiq
                     and a.cd_modalita_pagamento = b.cd_modalita_pag
             And Exists (Select 1 From vsx_liquidazione_cori v
              Where v.pg_call = pgCall
                And v.esercizio = a.esercizio
                      And v.cd_cds = a.cd_cds
                And v.cd_unita_organizzativa = a.cd_unita_organizzativa
                And v.cd_cds_origine = a.cd_cds_origine
                And v.cd_uo_origine = a.cd_uo_origine
                And v.cd_gruppo_cr = a.cd_gruppo_cr
                And v.cd_regione = a.cd_regione
                And v.pg_comune = a.pg_comune
                And v.pg_liquidazione = a.pg_liquidazione
                And v.pg_liquidazione_origine = a.pg_liquidazione_origine)
                Group by a.cd_gruppo_cr,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato, b.ti_mandato
                Order by a.cd_gruppo_cr,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato
  ) Loop -- LOOP VERSAMENTO aVersamenti
--pipe.send_message('aVersamenti.cd_gruppo_cr = '||aVersamenti.cd_gruppo_cr);
      select  FL_CONTO_BI
      INTO    CONTO_BI
      from    rif_modalita_pagamento
      WHERE   CD_MODALITA_PAG = aVersamenti.cd_modalita_pagamento;

      If aVersamenti.fl_accentrato = 'Y' then
          if aCdTerzoAcc is null or aCdModPagAcc is null or aPgBancaAcc is null then
             IBMERR001.RAISE_ERR_GENERICO('Dati relativi al terzo di versamento accentrato non trovati o non completi');
          end if;
          aAnagTst:=CNRCTB080.GETANAG(aCdTerzoAcc);
          isLiquidParzAcc:=true; -- Serve per marcare l'intera liquidazione come TRASFERITA (la chiusura dei pending verrÃ  fatta alcentro)
      Else
          if aVersamenti.cd_terzo_versamento is null or aVersamenti.pg_banca is null or aVersamenti.cd_modalita_pagamento is null then
             IBMERR001.RAISE_ERR_GENERICO('Dati relativi al terzo di versamento CORI non trovati o non completi per gruppo:'||aVersamenti.cd_gruppo_cr);
          end if;
          aAnagTst:=CNRCTB080.GETANAG(aVersamenti.cd_terzo_versamento);
      End If;

--pipe.send_message('aVersamenti.cd_gruppo_cr '||aVersamenti.cd_gruppo_cr);
--pipe.send_message('aVersamenti.tot_liquidato '||aVersamenti.tot_liquidato);
      -- Gestione liquidazione gruppi negativi solo da esercizio precedente
      If aVersamenti.tot_liquidato < 0 then
        If aLiquid.da_esercizio_precedente = 'N' Or aVersamenti.fl_accentrato = 'N' Then
           IBMERR001.RAISE_ERR_GENERICO('Importo di liquidazione negativo o nullo per gruppo CORI: '||aVersamenti.cd_gruppo_cr||' - Terzo di versamento: '||aVersamenti.cd_terzo_versamento);
        End if;
      End if;

      -- Per i gruppi dei versamenti locali non devo controllare aVersamenti.tot_liquidato bensÃ¬ devo fare
      -- la somma da V_LIQUID_CENTRO_UO perchÃ¨ aVersamenti.tot_liquidato contiene solo la parte positiva
      -- e se la somma Ã¨ negativa il versamento deve essere bloccato
      --PER IL MOMENTO PENSO CHE DEVO BLOCCARE ANCHE SE UN SOLO DETTAGLIO (ISTITUTO) E' NEGATIVO ma nel secondo loop
      --QUESTO CONTROLLO FORSE VA MODIFICATO TOGLIENDO LA CONDIZIONE SU REGIONE E COMUNE E AGGIUNGENDO IL TERZO DI VERSAMENTO
      --?????????????????????

      If aUOVERSACC.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo Then
        Declare
           totale_negativo    number;
        Begin
           select nvl(sum(l.im_liquidato),0)
           into totale_negativo
           from  V_LIQUID_CENTRO_UO l, GRUPPO_CR_DET g
           where l.cd_cds = aCdCds
             and l.cd_unita_organizzativa = aCdUo
             and l.esercizio = aEs
             and l.pg_liquidazione = aPgLiq
             And l.esercizio = g.esercizio
             And l.cd_gruppo_cr = g.cd_gruppo_cr
             And l.cd_regione = g.cd_regione
             And l.pg_comune = g.pg_comune
             and l.cd_gruppo_cr = aVersamenti.cd_gruppo_cr
             And g.cd_terzo_versamento = aVersamenti.cd_terzo_versamento
             And g.cd_modalita_pagamento = aVersamenti.cd_modalita_pagamento
             And g.pg_banca = aVersamenti.pg_banca
             and im_liquidato < 0
             And Exists (Select 1 From vsx_liquidazione_cori v
           Where v.pg_call = pgCall
             And v.esercizio = aEs
             And v.cd_cds = aCdCds
             And v.cd_unita_organizzativa = aCdUo
           And v.cd_uo_origine = UOENTE.cd_unita_organizzativa
             And v.cd_gruppo_cr = aVersamenti.cd_gruppo_cr
                And v.cd_regione =  l.cd_regione
                And v.pg_comune = l.pg_comune
                And v.pg_liquidazione = aPgLiq
                And v.pg_liquidazione_origine = 0);
--pipe.send_message('aVersamenti.tot_liquidato + totale_negativo '||To_Char(aVersamenti.tot_liquidato + totale_negativo));
            If (aVersamenti.tot_liquidato + totale_negativo) < 0 then
               IBMERR001.RAISE_ERR_GENERICO('Esistono Gruppi Locali negativi che rendono tutta la liquidazione negativa per il gruppo CORI:'||aVersamenti.cd_gruppo_cr||' - Terzo di versamento:'||aVersamenti.cd_terzo_versamento);
            End if;
        End;
      End If;

      -- Questa parte, relativa alla chiusura delle partite di giro dei compensi va tolta per i CDS caricati via interfaccia
      -- Inizio parte 1 esclusa per liq. di uo appartenenti a cds liquidati via interfaccia
      aTotMandato:=0;
      aTotReversale:=0;
      If Not lIsCdsInterDet And Not lIsCdsInterTot then
          aManP:=null;
          aManP.CD_CDS:=aCdCds;
          aManP.ESERCIZIO:=aEs;
          aManP.CD_UNITA_ORGANIZZATIVA:=aCdUo;
          aManP.CD_CDS_ORIGINE:=aCdCds;
          aManP.CD_UO_ORIGINE:=aCdUo;
          aManP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_MAN;
          if aVersamenti.ti_mandato = 'S' THEN
            aManP.TI_MANDATO:=CNRCTB038.TI_MAN_SOS;
          ELSE
            aManP.TI_MANDATO:=CNRCTB038.TI_MAN_PAG;
          END IF;
          aManP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
          aManP.DS_MANDATO:='Mandato di versamento CORI: '||aEs||'-'||aCdCds||' - '||aCdUo||' gruppo CORI:'||aVersamenti.cd_gruppo_cr;
          aManP.STATO:=CNRCTB038.STATO_MAN_EME;
          aManP.DT_EMISSIONE:=TRUNC(aDateCont);
          aManP.IM_RITENUTE:=0;
          --  aManP.DT_TRASMISSIONE:=;
          --  aManP.DT_PAGAMENTO:=;
          --  aManP.DT_ANNULLAMENTO:=;
          aManP.IM_PAGATO:=0;
          aManP.UTCR:=aUser;
          aManP.DACR:=aTSNow;
          aManP.UTUV:=aUser;
          aManP.DUVA:=aTSNow;
          aManP.PG_VER_REC:=1;
          aManP.STATO_TRASMISSIONE:=CNRCTB038.STATO_MAN_TRASCAS_NODIST;
          aRevP:=null;
          aRevP.CD_CDS:=aCdCds;
          aRevP.ESERCIZIO:=aEs;
          aRevP.CD_UNITA_ORGANIZZATIVA:=aCdUo;
          aRevP.CD_CDS_ORIGINE:=aCdCds;
          aRevP.CD_UO_ORIGINE:=aCdUo;
          aRevP.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_REV;
          aRevP.TI_REVERSALE:=CNRCTB038.TI_REV_INC;
          aRevP.TI_COMPETENZA_RESIDUO:=CNRCTB038.TI_MAN_COMP;
          aRevP.DS_REVERSALE:='Reversale di versamento CORI: '||aEs||'-'||aCdCds||' - '||aCdUo||' gruppo CORI:'||aVersamenti.cd_gruppo_cr;
          aRevP.STATO:=CNRCTB038.STATO_REV_EME;
          aRevP.DT_EMISSIONE:=TRUNC(aDateCont);
          --  aRevP.DT_TRASMISSIONE:=;
          --  aRevP.DT_PAGAMENTO:=;
          --  aRevP.DT_ANNULLAMENTO:=;
          aRevP.IM_INCASSATO:=0;
          aRevP.UTCR:=aUser;
          aRevP.DACR:=aTSNow;
          aRevP.UTUV:=aUser;
          aRevP.DUVA:=aTSNow;
          aRevP.PG_VER_REC:=1;
          aRevP.STATO_TRASMISSIONE:=CNRCTB038.STATO_REV_TRASCAS_NODIST;
          -- Modifica del 23/04/2004
          -- Le reversali di versamento CORI DEVONO essere processate in economica come i mandati
          aRevP.STATO_COGE:=CNRCTB100.STATO_COEP_INI;
          aListRigheManP.delete;
          aListRigheRevP.delete;
      End If;    --fine       If not lIsCdsInterDet and not lIsCdsInterTot

  For aGruppi In (Select a.cd_gruppo_cr,a.cd_regione,a.pg_comune,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato,Sum(a.im_liquidato) im_liquidato
                   From V_LIQUID_GRUPPO_CORI_CR_DET a
           Where a.esercizio = aEs
                     And a.cd_cds = aCdCds
                     And a.cd_unita_organizzativa = aCdUo
                     And a.pg_liquidazione = aPgLiq
                     And a.cd_gruppo_cr = aVersamenti.cd_gruppo_cr
                     And a.cd_terzo_versamento = aVersamenti.cd_terzo_versamento
                     And a.cd_modalita_pagamento = aVersamenti.cd_modalita_pagamento
                     And a.pg_banca = aVersamenti.pg_banca
             And Exists (Select 1 From vsx_liquidazione_cori v
              Where v.pg_call = pgCall
                And v.esercizio = a.esercizio
                      And v.cd_cds = a.cd_cds
                And v.cd_unita_organizzativa = a.cd_unita_organizzativa
                And v.cd_cds_origine = a.cd_cds_origine
                And v.cd_uo_origine = a.cd_uo_origine
                And v.cd_gruppo_cr = a.cd_gruppo_cr
                And v.cd_regione = a.cd_regione
                And v.pg_comune = a.pg_comune
                And v.pg_liquidazione = a.pg_liquidazione
                And v.pg_liquidazione_origine = a.pg_liquidazione_origine)
                Group by a.cd_gruppo_cr,a.cd_regione,a.pg_comune,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato
                Order by a.cd_gruppo_cr,a.cd_regione,a.pg_comune,a.esercizio,a.cd_cds,a.cd_unita_organizzativa,
                         a.cd_terzo_versamento,a.cd_modalita_pagamento,a.pg_banca,fl_accentrato
 ) Loop -- loop principale aGruppi
--pipe.send_message('Loop aGruppi');
--pipe.send_message('aGruppi.cd_gruppo_cr '||aGruppi.cd_gruppo_cr);
--pipe.send_message('aGruppi.im_liquidato '||aGruppi.im_liquidato);
      -- Gestione liquidazione gruppi negativi solo da esercizio precedente
      -- DA COMMENTARE SOLO A GENNAIO PER LA LIQ DA ESERCIZIO PRECEDENTE     ---- SI COMMENTA SOLO LA PARTE INTERNA...NON QUELLA CHE EMETTE I MANDATI
--INIZIO PARTE DA COMMENTARE SOLO A GENNAIO
/*
      If aGruppi.im_liquidato < 0 then
        If aLiquid.da_esercizio_precedente = 'N' Or aGruppi.fl_accentrato = 'N' Then
           IBMERR001.RAISE_ERR_GENERICO('Importo di liquidazione negativo o nullo per gruppo CORI: '||aGruppi.cd_gruppo_cr||'.'||aGruppi.cd_regione||'.'||aGruppi.pg_comune);
        End if;
      End if;
*/
--FINE PARTE DA COMMENTARE SOLO A GENNAIO

      -- Per i gruppi dei versamenti locali non devo controllare aGruppi.im_liquidato bensÃ¬ devo fare
      -- la somma da V_LIQUID_CENTRO_UO perchÃ¨ aGruppi.im_liquidato contiene solo la parte positiva
      -- e se la somma Ã¨ negativa il versamento deve essere bloccato
       If aUOVERSACC.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo Then
         Declare
           totale_negativo    number;
         Begin
           select nvl(sum(im_liquidato),0)
           into totale_negativo
           from  V_LIQUID_CENTRO_UO
           where cd_cds = aCdCds
             and cd_unita_organizzativa = aCdUo
             and esercizio = aEs
             and pg_liquidazione = aPgLiq
             and cd_gruppo_cr = aGruppi.cd_gruppo_cr
             and cd_regione = aGruppi.cd_regione
             and pg_comune = aGruppi.pg_comune
             and im_liquidato < 0
             And Exists (Select 1 From vsx_liquidazione_cori v
           Where v.pg_call = pgCall
             And v.esercizio = aEs
             And v.cd_cds = aCdCds
             And v.cd_unita_organizzativa = aCdUo
           And v.cd_uo_origine = UOENTE.cd_unita_organizzativa
             And v.cd_gruppo_cr = aGruppi.cd_gruppo_cr
                And v.cd_regione =  aGruppi.cd_regione
                And v.pg_comune = aGruppi.pg_comune
                And v.pg_liquidazione = aPgLiq
                And v.pg_liquidazione_origine = 0);
--pipe.send_message('aGruppi.im_liquidato + totale_negativo '||To_Char(aGruppi.im_liquidato + totale_negativo));

            -- DA COMMENTARE SOLO A GENNAIO PER LA LIQ DA ESERCIZIO PRECEDENTE---- SI COMMENTA SOLO LA PARTE INTERNA...NON QUELLA CHE EMETTE I MANDATI
--INIZIO PARTE DA COMMENTARE SOLO A GENNAIO
/*
            If (aGruppi.im_liquidato + totale_negativo) < 0 then
               IBMERR001.RAISE_ERR_GENERICO('Esistono Gruppi Locali negativi che rendono tutta la liquidazione negativa per il gruppo CORI:'||aGruppi.cd_gruppo_cr||'.'||aGruppi.cd_regione||'.'||aGruppi.pg_comune);
               --IBMERR001.RAISE_ERR_GENERICO('Esistono Gruppi Locali negativi che rendono tutta la liquidazione negativa per il gruppo CORI:'||aGruppi.cd_gruppo_cr||'.'||aGruppi.cd_regione||'.'||aGruppi.pg_comune||'/'||totale_negativo||'/'||aGruppi.im_liquidato);
            End if;
*/
--FINE PARTE DA COMMENTARE SOLO A GENNAIO
         End;
       End If;

       --
       --UOENTE:=CNRCTB020.GETUOENTE(aEs);

       -- Questa parte, relativa alla chiusura delle partite di giro dei compensi va tolta per i CDS caricati via interfaccia
       -- Inizio parte 1 esclusa per liq. di uo appartenenti a cds liquidati via interfaccia

       -- Devo prendere tutte le UO di origine tranne la 999.000
       For aAggregato in (Select * From V_LIQUID_GRUPPO_CORI_CR_DET a
                Where a.esercizio = aEs
                          And a.cd_cds = aCdCds
                          And a.cd_unita_organizzativa = aCdUo
                          And a.pg_liquidazione = aPgLiq
                          And (-- Proprio
                               a.cd_uo_origine = a.cd_unita_organizzativa
                               -- Non proprio e non presente come proprio
                               Or
                               (a.cd_uo_origine <> a.cd_unita_organizzativa
                               /*
                                And Not Exists (Select 1 From vsx_liquidazione_cori v
                                    Where v.pg_call = pgCall
                                      And v.esercizio = a.esercizio
                                And v.cd_cds = a.cd_cds
                                      And v.cd_unita_organizzativa = a.cd_unita_organizzativa
                                And v.pg_liquidazione = a.pg_liquidazione
                                And v.cd_uo_origine = v.cd_unita_organizzativa
                                                  And v.cd_gruppo_cr = a.cd_gruppo_cr
                                                  And v.cd_regione = a.cd_regione
                                                  And v.pg_comune = a.pg_comune)*/
                                )
                               )
                          And a.cd_uo_origine <> UOENTE.cd_unita_organizzativa     --'999.000'
                          And Exists (Select 1 From vsx_liquidazione_cori v
                                       Where v.pg_call = pgCall
                                         And v.esercizio = a.esercizio
                                         And v.cd_cds = a.cd_cds
                                         And v.cd_unita_organizzativa = a.cd_unita_organizzativa
                                         And v.cd_cds_origine = a.cd_cds_origine
                                         And v.cd_uo_origine = a.cd_uo_origine
                                         And v.cd_gruppo_cr = a.cd_gruppo_cr
                                         And v.cd_regione = a.cd_regione
                                         And v.pg_comune = a.pg_comune
                                         And v.pg_liquidazione = a.pg_liquidazione
                                         And v.pg_liquidazione_origine = a.pg_liquidazione_origine)
                                         And a.cd_gruppo_cr = aGruppi.cd_gruppo_cr
                                         And a.cd_regione = aGruppi.cd_regione
                                         And a.pg_comune = aGruppi.pg_comune
     ) Loop -- loop 1
    tb_ass_pgiro.delete;
--pipe.send_message('Loop aAggregato');
    If not lIsCdsInterDet And Not lIsCdsInterTot then
       -- CICLO SUI CORI DEI MIEI AGGREGATI PER CHIUDERE LE PARTITE DI GIRO APERTE
       -- Questa gestione estrae solo i CORI appartenenti all'UO che liquida
       For aCori in (Select *
                     From contributo_ritenuta a
                     Where
                      -- Tolgo la clausola sull'esercizio per estrarre indipendentemente da quello
                      --    a.esercizio = aEsOrigine
                      --      a.cd_cds = aAggregato.cd_cds
                      --  And a.cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
                          a.cd_cds = aAggregato.cd_cds_origine
                      And a.cd_unita_organizzativa = aAggregato.cd_uo_origine
                      And exists (Select 1 From liquid_gruppo_cori_det a1
                                  Where a1.cd_gruppo_cr = aAggregato.cd_gruppo_cr
                                  And a1.cd_regione = aAggregato.cd_regione
                                  And a1.pg_comune = aAggregato.pg_comune
                                  And a1.cd_cds = aAggregato.cd_cds
                                  And a1.cd_uo_origine = aAggregato.cd_uo_origine
                                  And a1.cd_cds_origine = aAggregato.cd_cds_origine
                                  And a1.cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
                                  And a1.pg_liquidazione = aAggregato.pg_liquidazione
                                  And a1.pg_liquidazione_origine = aAggregato.pg_liquidazione
                                  And a1.esercizio = aAggregato.esercizio
                                  -- Estrae solo i CORI referenziati in LIQUID_GRUPPO_CORI_DET
                                  And a1.esercizio_contributo_ritenuta = a.esercizio
                                  And a1.cd_cds_origine = a.cd_cds
                                  And a1.cd_uo_origine = a.cd_unita_organizzativa
                                  And a1.pg_compenso = a.pg_compenso
                                  And a1.cd_contributo_ritenuta = a.cd_contributo_ritenuta
                                  And a1.ti_ente_percipiente = a.ti_ente_percipiente)
                     For update nowait
       ) Loop -- loop2
           --Il documento generico deve essere Istituzionale o Commerciale cosÃ¬ come il compenso
           Begin
             Select c.ti_istituz_commerc
             Into tipo_ic
             From compenso c
             Where c.cd_cds = aCori.cd_cds
               And c.cd_unita_organizzativa = aCori.cd_unita_organizzativa
               And c.esercizio = aCori.esercizio
               And c.pg_compenso = aCori.pg_compenso;
           Exception
               When Others Then
                  tipo_ic := 'I';
           End;

         -- Gestione dei CORI positivi
         if aCori.ammontare > 0 then
            -- Modifica del 18/11/2002 anche il generico di trasferimento nel caso di CORI accentrati Ã¨ targato come GEN_COR_VER_S

            aTipoGenerico := CNRCTB100.TI_GEN_CORI_VER_SPESA;
--GGGG_TODO VERIFICARE IN CONTRIBUTO_RITENUTA LE DIFFERENZE DI DATI TRA GLI ISTITUTI E LE UO...VERIFICARE ANCHE LE QUERY DEI CURSORI..
            aAccTmp.cd_cds              := aCori.cd_cds_accertamento;
            aAccTmp.esercizio           := aCori.esercizio_accertamento;
            aAccTmp.esercizio_originale := aCori.esercizio_ori_accertamento;
            aAccTmp.pg_accertamento     := aCori.pg_accertamento;
            CNRCTB035.getPgiroCds(aAccTmp, aAccScadTmp, aAccScadVoceTmp, aObbTmp, aObbScadTmp, aObbScadVoceTmp);
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
            If aLiquid.da_esercizio_precedente = 'Y' Then

               -- 09.01.2008 remmata questa chiamata, e utilizzata la nuova gestione che ribalta entrambi i documenti
               -- CNRCTB046.ripPgiroCds (aAccTmp, aObb, aTSNow, aUser);

               -- 09.01.2008 SF PARTITE DI GIRO DA ESERCIZIO PRECEDENTE
               CNRCTB046.ripPgiroCdsEntrambe( aObbTmp, aObbScadTmp, aObbScadVoceTmp, aAccTmp, aAccScadTmp, aAccScadVoceTmp, Null, Null, CNRCTB001.GESTIONE_SPESE,
                                             aTSNow, aUser, aObb, aAcc);
               CNRCTB035.getPgiroCds (aObb, aObbScad, aObbScadVoce, aAcc, aAccScad, aAccScadVoce);

            Else
               aObb     := aObbTmp;
               aObbScad := aObbScadTmp;
            End if;
--*/
--            aObb     := aObbTmp;
--            aObbScad := aObbScadTmp;
-- FINE GG

------------------------------------
            -- se la UO Ã¨ quella del versamento su conto BI occorre rendere tronca la PGIRO in oggetto
            -- e crearla sempre tronca sulla UO di versamento (999.000)
            If aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa
--GG CONDIZIONE AGGIUNTA PER EVITARE LA PARTITA DI GIRO DALLA 000.407 ALLA 999.000
-- da qui non dovrebbe passare piÃ¹
            and aUOVERSACC.cd_unita_organizzativa != aUOVERSCONTOBI.cd_unita_organizzativa then
                  --prendo la pgiro duale (e verifico che sia effettivamente chiusa prima di renderla tronca)
                  --aObbOld.cd_cds:=aGruppoCentro.cd_cds_obb_accentr;
                  --aObbOld.esercizio:=aGruppoCentro.esercizio_obb_accentr;
                  --aObbOld.esercizio_originale:=aGruppoCentro.esercizio_ori_obb_accentr;
                  --aObbOld.pg_obbligazione:=aGruppoCentro.pg_obb_accentr;
                  --CNRCTB035.GETPGIROCDSINV(aObbOld,aObbScadOld,aObbScadVoceOld,aAccOld,aAccScadOld,aAccScadVoceOld);
                  --rendo tronca la stessa (cioÃ¨ annullo la parte spesa)
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
                  If aLiquid.da_esercizio_precedente = 'N' Then
                       CNRCTB043.troncaPraticaAccPgiro(aAccTmp.esercizio,aAccTmp.cd_cds,aAccTmp.esercizio_originale,aAccTmp.pg_accertamento,aTSNow,aUser);
                  Else  -- rendo tronca quella ribaltata

                       CNRCTB043.troncaPraticaAccPgiroInv(aAcc.esercizio,aAcc.cd_cds,aAcc.esercizio_originale,aAcc.pg_accertamento,aTSNow,aUser);
                  End if;
--*/
--                  CNRCTB043.troncaPraticaAccPgiro(aAccTmp.esercizio,aAccTmp.cd_cds,aAccTmp.esercizio_originale,aAccTmp.pg_accertamento,aTSNow,aUser);
-- FINE GG
                  -- devo creare la pgiro tronca sulla 999
                  aObbNew:=null;
                  aObbScadNew:=null;
                  aAccNew:=null;
                  aAccScadNew:=null;
                  --determino il capitolo da mettere sulla pgiro
                  Begin
                     Select distinct a.cd_elemento_voce
                     Into aCdEV
                     From ass_tipo_cori_ev a, tipo_cr_base b
                     Where b.esercizio = aEs   --aCori.esercizio
                       And b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
                       And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
                       And a.esercizio = aEs   --aCori.esercizio
                       And a.ti_gestione = CNRCTB001.GESTIONE_SPESE
                       And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS;
                  Exception
                          when TOO_MANY_ROWS then
                               IBMERR001.RAISE_ERR_GENERICO('Esiste piÃ¹ di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
                          when NO_DATA_FOUND then
                               IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
                  End;
                  Begin
                     Select *
                     Into elementoVoce
                     From elemento_voce
                     Where esercizio = aEs      --aCori.esercizio
                       And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
                       And ti_gestione = CNRCTB001.GESTIONE_SPESE
                       And cd_elemento_voce = aCdEV;
                  Exception
                          when NO_DATA_FOUND then
                             IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
                  End;

                  aObbNew.CD_CDS:=aCdCds;
                  aObbNew.ESERCIZIO:=aObb.esercizio;
                  aObbNew.ESERCIZIO_ORIGINALE:=aObb.esercizio_originale;
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
                  If aLiquid.da_esercizio_precedente = 'N' Then
                     aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_IMP;
                  Else
                     aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_IMP_RES;
                  End If;
--*/
--                  aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_IMP;
-- FINE GG
                  aObbNew.CD_UNITA_ORGANIZZATIVA:=aCdUo;
                  aObbNew.CD_CDS_ORIGINE:=aCdCds;
                  aObbNew.CD_UO_ORIGINE:=aCdUo;
                  aObbNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
                  aObbNew.TI_GESTIONE:=elementoVoce.ti_gestione;
                  aObbNew.CD_ELEMENTO_VOCE:=elementoVoce.cd_elemento_voce;
                  aObbNew.DT_REGISTRAZIONE:=TRUNC(trunc(aTSNow));
                  aObbNew.DS_OBBLIGAZIONE:='PGiro creata in automatico da liquidazione CORI';
                  aObbNew.NOTE_OBBLIGAZIONE:='';
                  aObbNew.CD_TERZO:=aObb.CD_TERZO;
                  aObbNew.IM_OBBLIGAZIONE:=aObb.IM_OBBLIGAZIONE;    --aObbOld.IM_OBBLIGAZIONE Ã¨ stata giÃ  annullata
                  aObbNew.stato_obbligazione:=CNRCTB035.STATO_DEFINITIVO;
                  aObbNew.im_costi_anticipati:=0;
                  aObbNew.fl_calcolo_automatico:='N';
                  aObbNew.fl_spese_costi_altrui:='N';
                  aObbNew.FL_PGIRO:='Y';
                  aObbNew.RIPORTATO:='N';
                  aObbNew.DACR:=aTSNow;
                  aObbNew.UTCR:=aUser;
                  aObbNew.DUVA:=aTSNow;
                  aObbNew.UTUV:=aUser;
                  aObbNew.PG_VER_REC:=1;
                  aObbNew.ESERCIZIO_COMPETENZA:=aEs;

                  CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObbNew,aObbScadNew,aAccNew,aAccScadNew,trunc(aTSNow));

                  CREALIQUIDCORIASSPGIRO(aLiquid,aAggregato.cd_gruppo_cr,aAggregato.cd_regione,aAggregato.pg_comune,'S',aObbNew,aObb,null,null,aUser,trunc(aTSNow));


                  aObb     := aObbNew;
                  aObbScad := aObbScadNew;
            End If;

-------------------------------------
---INIZIO GGG
            IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' and aAggregato.cd_unita_organizzativa <> aUOVERSACC.cd_unita_organizzativa and
                aAggregato.cd_unita_organizzativa <> aUOVERSCONTOBI.cd_unita_organizzativa and aAggregato.fl_accentrato = 'Y' THEN
              If aLiquid.da_esercizio_precedente = 'N' Then
                CNRCTB043.troncaPraticaAccPgiro(aCori.esercizio_accertamento,aCori.cd_cds_accertamento,aCori.esercizio_ori_accertamento,aCori.pg_accertamento,aTSNow,aUser);
              Else  -- rendo tronca quella ribaltata
-- GGG TODO_DA_VERIFICARE COSA PASSARE...DOVREBBE ESSERE OK...CHIEDERE A TILDE
                CNRCTB043.troncaPraticaAccPgiroInv(aAcc.esercizio,aAcc.cd_cds,aAcc.esercizio_oriGINALE,aAcc.pg_accertamento,aTSNow,aUser);
              End if;
              ind_pGiro := Nvl(tb_ass_pgiro.Count,0) + 1;
              tb_ass_pgiro(ind_pGiro).cd_cds := aCori.cd_cds;
              tb_ass_pgiro(ind_pGiro).cd_uo := aCori.cd_unita_organizzativa;
              tb_ass_pgiro(ind_pGiro).cd_cds_orig := aCori.cd_cds;
              tb_ass_pgiro(ind_pGiro).cd_uo_orig := aCori.cd_unita_organizzativa;
              tb_ass_pgiro(ind_pGiro).esercizio := aEs;
              tb_ass_pgiro(ind_pGiro).es_compenso := aCori.esercizio;
              tb_ass_pgiro(ind_pGiro).pg_compenso := aCori.pg_compenso;
              tb_ass_pgiro(ind_pGiro).pg_liq := aPgLiq;
              tb_ass_pgiro(ind_pGiro).pg_liq_orig := aPgLiq;
              tb_ass_pgiro(ind_pGiro).cd_gr_cr := aAggregato.cd_gruppo_cr;
              tb_ass_pgiro(ind_pGiro).cd_regione := aAggregato.cd_regione;
              tb_ass_pgiro(ind_pGiro).pg_comune := aAggregato.pg_comune;
              tb_ass_pgiro(ind_pGiro).cd_cori := aCori.cd_contributo_ritenuta;
              tb_ass_pgiro(ind_pGiro).ti_en_per := aCori.ti_ente_percipiente;
              tb_ass_pgiro(ind_pGiro).ti_origine := 'E';
              tb_ass_pgiro(ind_pGiro).es_acc := aCori.esercizio_accertamento;
              tb_ass_pgiro(ind_pGiro).es_ori_acc := aCori.esercizio_ori_accertamento;
              tb_ass_pgiro(ind_pGiro).cds_acc := aCori.cd_cds_accertamento;
              tb_ass_pgiro(ind_pGiro).pg_acc := aCori.pg_accertamento;
            ELSE
              aGen:=null;
              aGenRiga:=null;
              aListGenRighe.delete;
              -- Creo il documento generico di spesa su partita di giro collegato all'annotazione di entrata su pgiro del contributo ritenuta
              aGen.CD_CDS:=aObb.cd_cds;
              aGen.CD_UNITA_ORGANIZZATIVA:=aObb.cd_unita_organizzativa;
              aGen.ESERCIZIO:=aObb.esercizio;
              aGen.CD_CDS_ORIGINE:=aObb.cd_cds;
              aGen.CD_UO_ORIGINE:=aObb.cd_unita_organizzativa;
              aGen.CD_TIPO_DOCUMENTO_AMM:=aTipoGenerico;
              aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
              aGen.DS_DOCUMENTO_GENERICO:='CORI-D cn.'||aCori.pg_compenso||' '||aCori.cd_contributo_ritenuta;
              --aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
              aGen.TI_ISTITUZ_COMMERC:=tipo_ic;
              aGen.IM_TOTALE:=aObb.im_obbligazione;
              aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
              aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
              aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
              --Massimo Iaccarino Inizio
              aGen.CD_DIVISA:=aDivisaEuro;
              aGen.CAMBIO:=1;
              --Massimo Iaccarino Fine
              --  aGen.ESERCIZIO_LETTERA:=0;
              --  aGen.PG_LETTERA:=0;
              aGen.DACR:=aTSNow;
              aGen.UTCR:=aUser;
              aGen.DUVA:=aTSNow;
              aGen.UTUV:=aUser;
              aGen.PG_VER_REC:=1;
              aGen.DT_SCADENZA:=TRUNC(aTSNow);
              aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
              aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
              aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
              aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

              aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
              aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
              --
              aGenRiga.CD_CDS:=aGen.CD_CDS;
              aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
              aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
              aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
              aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
              aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
              aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
              -- Utilizzo delle corrette informazioni di pagamento nel caso di accentramento o non accentramento
              if aAggregato.fl_accentrato = 'Y' then
                aGenRiga.CD_TERZO:=aCdTerzoAcc;
                aGenRiga.CD_MODALITA_PAG:=aCdModPagAcc;
                aGenRiga.PG_BANCA:=aPgBancaAcc;
              else
                aGenRiga.CD_TERZO:=aAggregato.cd_terzo_versamento;
                aGenRiga.CD_MODALITA_PAG:=aAggregato.CD_MODALITA_PAGAMENTO;
                aGenRiga.PG_BANCA:=aAggregato.PG_BANCA;
              end if;
              --   aGenRiga.CD_TERZO_CESSIONARIO:=aGen.CD_TERZO_CESSIONARIO;
              --   aGenRiga.CD_TERZO_UO_CDS:=aGen.CD_TERZO_UO_CDS;
              aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
              aGenRiga.NOME:=aAnagTst.NOME;
              aGenRiga.COGNOME:=aAnagTst.COGNOME;
              aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
              aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
              --   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
              --   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
              --   aGenRiga.PG_BANCA_UO_CDS:=aGen.PG_BANCA_UO_CDS;
              --   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aGen.CD_MODALITA_PAG_UO_CDS;
              --   aGenRiga.NOTE:=aGen.NOTE;
              aGenRiga.STATO_COFI:=aGen.STATO_COFI;
              --   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
              --   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
              --   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
              --   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
              --   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
              --   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
              aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.CD_CDS;
              aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.ESERCIZIO;
              aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.ESERCIZIO_ORIGINALE;
              aGenRiga.PG_OBBLIGAZIONE:=aObb.PG_OBBLIGAZIONE;
              aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
              aGenRiga.DACR:=aGen.DACR;
              aGenRiga.UTCR:=aGen.UTCR;
              aGenRiga.UTUV:=aGen.UTUV;
              aGenRiga.DUVA:=aGen.DUVA;
              aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
              aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
              aListGenRighe(1):=aGenRiga;
              --
              CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

              -- Generazione righe mandato
              aManPRiga:=null;
              aManPRiga.CD_CDS:=aGen.cd_cds;
              aManPRiga.ESERCIZIO:=aGen.esercizio;
              aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
              aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
              aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
              aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
              aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
              aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
              aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
              aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
              aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
              aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
              aManPRiga.STATO:=aManP.stato;
              aManPRiga.CD_TERZO:=aGenRiga.cd_terzo;
              aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
              aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;

              aManPRiga.IM_MANDATO_RIGA:=aObb.im_obbligazione;
              aManPRiga.IM_RITENUTE_RIGA:=0;
              aManPRiga.FL_PGIRO:='Y';
              aManPRiga.UTCR:=aUser;
              aManPRiga.DACR:=aTSNow;
              aManPRiga.UTUV:=aUser;
              aManPRiga.DUVA:=aTSNow;
              aManPRiga.PG_VER_REC:=1;
              aTotMandato:=aTotMandato+aManPRiga.im_mandato_riga;
              aListRigheManP(aListRigheManP.count+1):=aManPRiga;
            END IF;
---FINE GGG
         -- ================================================================
         -- Se il CORI Ã¨ negativo
         -- ================================================================
         elsif aCori.ammontare < 0 Then
            -- Modifica del 18/11/2002 anche il generico di trasferimento nel caso di CORI accentrati Ã¨ targato come GEN_COR_VER_E
            --if aAggregato.fl_accentrato = 'Y' then
            --  aTipoGenerico := CNRCTB100.TI_GENERICO_TRASF_E;
            -- else
            aTipoGenerico := CNRCTB100.TI_GEN_CORI_VER_ENTRATA;
            -- end if;
            aObbTmp.cd_cds:=aCori.cd_cds_obbligazione;
            aObbTmp.esercizio:=aCori.esercizio_obbligazione;
            aObbTmp.esercizio_originale:=aCori.esercizio_ori_obbligazione;
            aObbTmp.pg_obbligazione:=aCori.pg_obbligazione;
-- recupero l'accertamento collegato alla partita di giro di spesa.
            CNRCTB035.getPgiroCds(aObbTmp,aObbScadTmp,aObbScadVoceTmp,aAccTmp,aAccScadTmp,aAccScadVoceTmp);
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
            If aLiquid.da_esercizio_precedente = 'Y' Then

               -- 09.01.2008 remmata questa chiamata, e utilizzata la nuova gestione che ribalta entrambi i documenti
               -- CNRCTB046.ripPgiroCds(aObbTmp,aAcc,aTSNow,aUser);

               -- 09.01.2008 SF PARTITE DI GIRO DA ESERCIZIO PRECEDENTE
               CNRCTB046.ripPgiroCdsEntrambe(aObbTmp, aObbScadTmp, aObbScadVoceTmp, aAccTmp, aAccScadTmp, aAccScadVoceTmp, Null, Null, CNRCTB001.GESTIONE_ENTRATE,
                                             aTSNow, aUser, aObb, aAcc);

               CNRCTB035.getPgiroCds(aAcc,aAccScad,aAccScadVoce,aObb,aObbScad,aObbScadVoce);
            Else
               aAcc     := aAccTmp;
               aAccScad := aAccScadTmp;
            End If;
--*/
--               aAcc     := aAccTmp;
--               aAccScad := aAccScadTmp;
-- FINE GG
------------------------------------
            -- se la UO Ã¨ quella del versamento su conto BI occorre rendere tronca la PGIRO in oggetto
            -- e crearla sempre tronca sulla UO di versamento (999.000)
            If aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa
--GG CONDIZIONE AGGIUNTA PER EVITARE LA PARTITA DI GIRO DALLA 000.407 ALLA 999.000
            and aUOVERSACC.cd_unita_organizzativa != aUOVERSCONTOBI.cd_unita_organizzativa then
                  --prendo la pgiro duale (e verifico che sia effettivamente chiusa prima di renderla tronca)
                  --aObbOld.cd_cds:=aGruppoCentro.cd_cds_obb_accentr;
                  --aObbOld.esercizio:=aGruppoCentro.esercizio_obb_accentr;
                  --aObbOld.esercizio_originale:=aGruppoCentro.esercizio_ori_obb_accentr;
                  --aObbOld.pg_obbligazione:=aGruppoCentro.pg_obb_accentr;
                  --CNRCTB035.GETPGIROCDSINV(aObbOld,aObbScadOld,aObbScadVoceOld,aAccOld,aAccScadOld,aAccScadVoceOld);
                  --rendo tronca la stessa (cioÃ¨ annullo la parte entrata)
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
                  If aLiquid.da_esercizio_precedente = 'N' Then
                       CNRCTB043.troncaPraticaObbPgiro(aObbTmp.esercizio,aObbTmp.cd_cds,aObbTmp.esercizio_originale,aObbTmp.pg_obbligazione,aTSNow,aUser);
                  Else  -- rendo tronca quella ribaltata
                       CNRCTB043.troncaPraticaObbPgiroInv(aObb.esercizio,aObb.cd_cds,aObb.esercizio_originale,aObb.pg_obbligazione,aTSNow,aUser);
                  End if;
--*/
--                       CNRCTB043.troncaPraticaObbPgiro(aObbTmp.esercizio,aObbTmp.cd_cds,aObbTmp.esercizio_originale,aObbTmp.pg_obbligazione,aTSNow,aUser);
-- FINE GG
                  -- devo creare la pgiro tronca sulla 999
                  aAccNew:=null;
                  aAccScadNew:=null;
                  aObbNew:=null;
                  aObbScadNew:=null;
                  --determino il capitolo da mettere sulla pgiro
                  Begin
                     Select distinct a.cd_elemento_voce
                     Into aCdEV
                     From ass_tipo_cori_ev a, tipo_cr_base b
                     Where b.esercizio = aEs   --aCori.esercizio
                       And b.cd_gruppo_cr = aAggregato.cd_gruppo_cr
                       And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
                       And a.esercizio = aEs   --aCori.esercizio
                       And a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
                       And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
                  Exception
                          when TOO_MANY_ROWS then
                               IBMERR001.RAISE_ERR_GENERICO('Esiste piÃ¹ di un conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr);
                          when NO_DATA_FOUND then
                              IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non trovato');
                  End;
                  Begin
                     Select *
                     Into elementoVoce
                     From elemento_voce
                     Where esercizio = aEs   --aCori.esercizio
                       And ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
                       And ti_gestione = CNRCTB001.GESTIONE_ENTRATE
                       And cd_elemento_voce = aCdEV;
                  Exception
                          when NO_DATA_FOUND then
                             IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aAggregato.cd_gruppo_cr||' non esistente');
                  End;

                  aAccNew.CD_CDS:=aCdCds;
                  aAccNew.ESERCIZIO:=aAcc.esercizio;
                  aAccNew.ESERCIZIO_ORIGINALE:=aAcc.esercizio_originale;
--/* GG REMMATO SOLO PER IL FINE ANNO 2015
                  If aLiquid.da_esercizio_precedente = 'N' Then
                       aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC;
                  Else
                       aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC_RES;
                  End If;
--*/
--                  aAccNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_ACC;
-- FINE GG
                  aAccNew.CD_UNITA_ORGANIZZATIVA:=aCdUo;
                  aAccNew.CD_CDS_ORIGINE:=aCdCds;
                  aAccNew.CD_UO_ORIGINE:=aCdUo;
                  aAccNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
                  aAccNew.TI_GESTIONE:=elementoVoce.ti_gestione;
                  aAccNew.CD_ELEMENTO_VOCE:=elementoVoce.cd_elemento_voce;
                  aAccNew.CD_VOCE:=elementoVoce.cd_elemento_voce;
                  aAccNew.DT_REGISTRAZIONE:=TRUNC(trunc(aTSNow));
                  aAccNew.DS_ACCERTAMENTO:='PGiro creata in automatico da liquidazione CORI';
                  aAccNew.NOTE_ACCERTAMENTO:='';
                  aAccNew.CD_TERZO:=aAcc.CD_TERZO;
                  aAccNew.IM_ACCERTAMENTO:=aAcc.IM_ACCERTAMENTO;
                  aAccNew.FL_PGIRO:='Y';
                  aAccNew.RIPORTATO:='N';
                  aAccNew.DACR:=aTSNow;
                  aAccNew.UTCR:=aUser;
                  aAccNew.DUVA:=aTSNow;
                  aAccNew.UTUV:=aUser;
                  aAccNew.PG_VER_REC:=1;
                  aAccNew.ESERCIZIO_COMPETENZA:=aEs;

                  CNRCTB040.CREAACCERTAMENTOPGIROTRONC(false,aAccNew,aAccScadNew,aObbNew,aObbScadNew,trunc(aTSNow));

                  CREALIQUIDCORIASSPGIRO(aLiquid,aAggregato.cd_gruppo_cr,aAggregato.cd_regione,aAggregato.pg_comune,'E',null,null,aAccNew,aAcc,aUser,trunc(aTSNow));

                  aAcc     := aAccNew;
                  aAccScad := aAccScadNew;
            End If;

-------------------------------------
---INIZIO GGG
            IF recParametriCNR.FL_TESORERIA_UNICA = 'Y' and aAggregato.cd_unita_organizzativa <> aUOVERSACC.cd_unita_organizzativa and
                aAggregato.cd_unita_organizzativa <> aUOVERSCONTOBI.cd_unita_organizzativa and aAggregato.fl_accentrato = 'Y' THEN
            -- rendo tronca LA PARTITA DI GIRO DELL'ISTITUTO
              If aLiquid.da_esercizio_precedente = 'N' Then
                CNRCTB043.troncaPraticaObbPgiro(aCori.esercizio_obbligazione,aCori.cd_cds,aCori.esercizio_ori_obbligazione,aCori.pg_obbligazione,aTSNow,aUser);
              Else  -- rendo tronca quella ribaltata
-- GGG TODO_DA_VERIFICARE COSA PASSARE...DOVREBBE ESSERE OK...CHIEDERE A TILDE
                CNRCTB043.troncaPraticaObbPgiroInv(aObb.esercizio,aObb.cd_cds,aObb.esercizio_originale,aObb.pg_obbligazione,aTSNow,aUser);
              End if;
              ind_pGiro := Nvl(tb_ass_pgiro.Count,0) + 1;
              tb_ass_pgiro(ind_pGiro).cd_cds := aCori.cd_cds;
              tb_ass_pgiro(ind_pGiro).cd_uo := aCori.cd_unita_organizzativa;
              tb_ass_pgiro(ind_pGiro).cd_cds_orig := aCori.cd_cds;
              tb_ass_pgiro(ind_pGiro).cd_uo_orig := aCori.cd_unita_organizzativa;
              tb_ass_pgiro(ind_pGiro).esercizio := aEs;
              tb_ass_pgiro(ind_pGiro).es_compenso := aCori.esercizio;
              tb_ass_pgiro(ind_pGiro).pg_compenso := aCori.pg_compenso;
              tb_ass_pgiro(ind_pGiro).pg_liq := aPgLiq;
              tb_ass_pgiro(ind_pGiro).pg_liq_orig := aPgLiq;
              tb_ass_pgiro(ind_pGiro).cd_gr_cr := aAggregato.cd_gruppo_cr;
              tb_ass_pgiro(ind_pGiro).cd_regione := aAggregato.cd_regione;
              tb_ass_pgiro(ind_pGiro).pg_comune := aAggregato.pg_comune;
              tb_ass_pgiro(ind_pGiro).cd_cori := aCori.cd_contributo_ritenuta;
              tb_ass_pgiro(ind_pGiro).ti_en_per := aCori.ti_ente_percipiente;
              tb_ass_pgiro(ind_pGiro).ti_origine := 'S';
              tb_ass_pgiro(ind_pGiro).es_obb := aCori.esercizio_obbligazione;
              tb_ass_pgiro(ind_pGiro).es_ori_obb := aCori.esercizio_ori_obbligazione;
              tb_ass_pgiro(ind_pGiro).cds_obb := aCori.cd_cds_obbligazione;
              tb_ass_pgiro(ind_pGiro).pg_obb := aCori.pg_obbligazione;
            ELSE
              aGen:=null;
              aGenRiga:=null;
              aListGenRighe.delete;

              -- Creo il documento generico di entrata su partita di giro collegato all'annotazione di spesa su pgiro del contributo ritenuta
              aGen.CD_CDS:=aAcc.cd_cds;
              aGen.CD_UNITA_ORGANIZZATIVA:=aAcc.cd_unita_organizzativa;
              aGen.ESERCIZIO:=aAcc.esercizio;
              aGen.CD_CDS_ORIGINE:=aAcc.cd_cds;
              aGen.CD_UO_ORIGINE:=aAcc.cd_unita_organizzativa;
              aGen.CD_TIPO_DOCUMENTO_AMM:=aTipoGenerico;
              aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
              aGen.DS_DOCUMENTO_GENERICO:='CORI-D cn.'||aCori.pg_compenso||' '||aCori.cd_contributo_ritenuta;
              aGen.TI_ISTITUZ_COMMERC:=tipo_ic;
              aGen.IM_TOTALE:=aAcc.im_accertamento;
              aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
              aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
              aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
              aGen.CD_DIVISA:=aDivisaEuro;
              aGen.CAMBIO:=1;
              aGen.DACR:=aTSNow;
              aGen.UTCR:=aUser;
              aGen.DUVA:=aTSNow;
              aGen.UTUV:=aUser;
              aGen.PG_VER_REC:=1;
              aGen.DT_SCADENZA:=TRUNC(aTSNow);
              aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
              aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
              aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
              aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

              aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
              aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
              aGenRiga.CD_CDS:=aGen.CD_CDS;
              aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
              aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
              aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
              aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
              aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
              aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
              -- Utilizzo delle corrette informazioni di pagamento nel caso di accentramento o non accentramento
              if aAggregato.fl_accentrato = 'Y' then
                  aGenRiga.CD_TERZO:=aCdTerzoAcc;
              else
                  aGenRiga.CD_TERZO:=aAggregato.cd_terzo_versamento;
              end if;
              --   aGenRiga.CD_TERZO_CESSIONARIO:=aGen.CD_TERZO_CESSIONARIO;
              -- Estrae il terzo associato all'UO del compenso e le sue modalitÃ  di pagamento di tipo bancario piÃ¹ recenti
              -- se entro dalla 999 estraggo il terzo associato ad essa con modalitÃ  di pagamento BI
              IF CONTO_BI = 'Y' THEN
                 CNRCTB080.getTerzoPerEnteContoBI(aCdUo, aCdTerzoUO, aCdModPagUO, aPgBancaUO);
              Else
                 CNRCTB080.getTerzoPerUO(aCori.cd_unita_organizzativa, aCdTerzoUO, aCdModPagUO, aPgBancaUO,aAcc.esercizio);
              End If;
              --
              aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoUO;
              aGenRiga.PG_BANCA_UO_CDS:=aPgBancaUO;
              aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagUO;
              aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
              aGenRiga.NOME:=aAnagTst.NOME;
              aGenRiga.COGNOME:=aAnagTst.COGNOME;
              aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
              aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
              --   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
              --   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
              --   aGenRiga.NOTE:=aGen.NOTE;
              aGenRiga.STATO_COFI:=aGen.STATO_COFI;
              --   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
              --   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
              --   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
              --   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
              --   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
              --   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
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
              --
              CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);
              -- Generazione righe reversale
              aRevPRiga:=null;
              aRevPRiga.CD_CDS:=aGen.cd_cds;
              aRevPRiga.ESERCIZIO:=aGen.esercizio;
              aRevPRiga.ESERCIZIO_ACCERTAMENTO:=aGenRiga.esercizio_ACCERTAMENTO;
              aRevPRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aGenRiga.esercizio_ORI_ACCERTAMENTO;
              aRevPRiga.PG_ACCERTAMENTO:=aGenRiga.pg_accertamento;
              aRevPRiga.PG_ACCERTAMENTO_SCADENZARIO:=aGenRiga.pg_accertamento_scadenzario;
              aRevPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
              aRevPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
              aRevPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
              aRevPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
              aRevPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
              aRevPRiga.DS_REVERSALE_RIGA:=aManP.ds_mandato;
              aRevPRiga.STATO:=aManP.stato;
              aRevPRiga.CD_TERZO:=aGenRiga.cd_terzo;
              aRevPRiga.CD_TERZO_UO:=aGenRiga.cd_terzo_uo_cds;
              aRevPRiga.PG_BANCA:=aGenRiga.pg_banca_uo_cds;
              aRevPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag_uo_cds;
              aRevPRiga.IM_REVERSALE_RIGA:=aAcc.im_accertamento;
              aRevPRiga.FL_PGIRO:='Y';
              aRevPRiga.UTCR:=aUser;
              aRevPRiga.DACR:=aTSNow;
              aRevPRiga.UTUV:=aUser;
              aRevPRiga.DUVA:=aTSNow;
              aRevPRiga.PG_VER_REC:=1;
              aTotReversale:=aTotReversale+aRevPRiga.im_reversale_riga;
              aListRigheRevP(aListRigheRevP.count+1):=aRevPRiga;
            END IF;
---FINE GGG
         else -- su ammontare = 0
            null;
         end if; -- if su ammontare

     End Loop; -- FINE DEL CICLO DI LOOP SU CORI --loop2
     -- Creazione della partita di giro di compensazione nel caso di liquidazione negativa (non via interfaccia)
-- GGG TODO....VERIFICARE QUALI MODIFICHE APPORTARE IN QUESTO CASO...ESERCIZIO PRECEDENTE
     If recParametriCNR.FL_TESORERIA_UNICA != 'Y' and
        Not lIsCdsInterDet
        And Not lIsCdsInterTot
        And aAggregato.im_liquidato < 0
        And aLiquid.da_esercizio_precedente = 'Y'
        -- La compensazione viene fatta SOLO per le liquidazioni locali non per quella del centro!!!
        -- I cori negativi del centro vanno nella reversale con le compensazioni negative dei gruppi centro da esercizio precedente
        And aAggregato.cd_unita_organizzativa <> aUOVERSACC.cd_unita_organizzativa
        And aAggregato.cd_unita_organizzativa <> aUOVERSCONTOBI.cd_unita_organizzativa
     Then
        -- Genera pgiro
        -- Riporta pgiro
        -- Crea riga generico e mandato
        aAcc:=null;
        aAccScad:=null;

        -- Devo estrarre l'ELEMENTO_VOCE identificato in CONFIGURAZIONE_CNR come COMPENSAZIONE_CORI

        aCdEV:=CNRCTB015.GETVAL01PERCHIAVE(aAggregato.esercizio,CNRCTB575.ELEMENTO_VOCE_SPECIALE,CNRCTB575.COMPENSAZIONE_CORI);

        Begin
          Select * Into aEVComp
          From elemento_voce
          Where esercizio = aAggregato.esercizio
            And ti_gestione = CNRCTB001.GESTIONE_SPESE
            And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
            And cd_elemento_voce = aCdEV
            And fl_partita_giro = 'Y';
        Exception when NO_DATA_FOUND then
              IBMERR001.RAISE_ERR_GENERICO('Conto su partita di giro per compensazione CORI negativi non trovato: '||aCdEV);
        End;

        aObb:=null;
        aObbScad:=null;
        aObb.CD_CDS:=aAggregato.cd_cds;
        aObb.ESERCIZIO:=aAggregato.esercizio;
        aObb.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_OBB_PGIRO;
        aObb.CD_UNITA_ORGANIZZATIVA:=aAggregato.cd_unita_organizzativa;
        aObb.CD_CDS_ORIGINE:=aAggregato.cd_cds;
        aObb.CD_UO_ORIGINE:=aAggregato.cd_unita_organizzativa;
        aObb.TI_APPARTENENZA:=aEVComp.ti_appartenenza;
        aObb.TI_GESTIONE:=aEVComp.ti_gestione;
        aObb.CD_ELEMENTO_VOCE:=aEVComp.cd_elemento_voce;
        aObb.DT_REGISTRAZIONE:=TRUNC(aDateCont);
        aObb.DS_OBBLIGAZIONE:='CORI-VA compensazione gruppo cr:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
        aObb.NOTE_OBBLIGAZIONE:='';
        If aAggregato.fl_accentrato = 'Y' then
            aObb.CD_TERZO:=aCdTerzoAcc;
        Else
            aObb.CD_TERZO:=aAggregato.cd_terzo_versamento;
        End If;
        aObb.IM_OBBLIGAZIONE:=abs(aAggregato.im_liquidato);
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
        aObb.ESERCIZIO_COMPETENZA:=aAggregato.esercizio;
        CNRCTB030.CREAOBBLIGAZIONEPGIRO(false,aObb,aObbScad,aAcc,aAccScad,trunc(aTSNow));
        aTipoGenerico := CNRCTB100.TI_GEN_CORI_VER_SPESA;
        aGen:=null;
        aGenRiga:=null;
        aListGenRighe.delete;
        -- Creo il documento generico di spesa su partita di giro collegato all'annotazione di entrata su pgiro del contributo ritenuta
        aGen.CD_CDS:=aAggregato.cd_cds;
        aGen.CD_UNITA_ORGANIZZATIVA:=aAggregato.cd_unita_organizzativa;
        aGen.ESERCIZIO:=aAggregato.esercizio;
        aGen.CD_CDS_ORIGINE:=aAggregato.cd_cds;
        aGen.CD_UO_ORIGINE:=aAggregato.cd_unita_organizzativa;
        aGen.CD_TIPO_DOCUMENTO_AMM:=aTipoGenerico;
        aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
        aGen.DS_DOCUMENTO_GENERICO:='COMPENSAZIONE LIQ. GRUPPO CORI:'||aAggregato.cd_gruppo_cr||'.'||aAggregato.cd_regione||'.'||aAggregato.pg_comune;
        aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
        aGen.IM_TOTALE:=aObb.im_obbligazione;
        aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
        aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
        aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
        aGen.CD_DIVISA:=aDivisaEuro;
        aGen.CAMBIO:=1;
        aGen.DACR:=aTSNow;
        aGen.UTCR:=aUser;
        aGen.DUVA:=aTSNow;
        aGen.UTUV:=aUser;
        aGen.PG_VER_REC:=1;
        aGen.DT_SCADENZA:=TRUNC(aTSNow);
        aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
        aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
        aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
        aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

        aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
        aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
        --
        aGenRiga.CD_CDS:=aGen.CD_CDS;
        aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
        aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
        aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
        aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
        aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
        aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
        -- Utilizzo delle corrette informazioni di pagamento nel caso di accentramento o non accentramento
        if aAggregato.fl_accentrato = 'Y' then
           aGenRiga.CD_TERZO:=aCdTerzoAcc;
           aGenRiga.CD_MODALITA_PAG:=aCdModPagAcc;
           aGenRiga.PG_BANCA:=aPgBancaAcc;
        else
           aGenRiga.CD_TERZO:=aAggregato.cd_terzo_versamento;
           aGenRiga.CD_MODALITA_PAG:=aAggregato.CD_MODALITA_PAGAMENTO;
           aGenRiga.PG_BANCA:=aAggregato.PG_BANCA;
        end if;
        --   aGenRiga.CD_TERZO_CESSIONARIO:=aGen.CD_TERZO_CESSIONARIO;
        --   aGenRiga.CD_TERZO_UO_CDS:=aGen.CD_TERZO_UO_CDS;
        aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
        aGenRiga.NOME:=aAnagTst.NOME;
        aGenRiga.COGNOME:=aAnagTst.COGNOME;
        aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
        aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
        --   aGenRiga.CD_TERMINI_PAG:=aCompenso.CD_TERMINI_PAG;
        --   aGenRiga.CD_TERMINI_PAG_UO_CDS:=aCompenso.CD_TERMINI_PAG_UO_CDS;
        --   aGenRiga.PG_BANCA_UO_CDS:=aGen.PG_BANCA_UO_CDS;
        --   aGenRiga.CD_MODALITA_PAG_UO_CDS:=aGen.CD_MODALITA_PAG_UO_CDS;
        --   aGenRiga.NOTE:=aGen.NOTE;
        aGenRiga.STATO_COFI:=aGen.STATO_COFI;
        --   aGenRiga.DT_CANCELLAZIONE:=aGen.DT_CANCELLAZIONE;
        --   aGenRiga.CD_CDS_OBBLIGAZIONE:=aGen.CD_CDS_OBBLIGAZIONE;
        --   aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGen.ESERCIZIO_OBBLIGAZIONE;
        --   aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGen.ESERCIZIO_ORI_OBBLIGAZIONE;
        --   aGenRiga.PG_OBBLIGAZIONE:=aGen.PG_OBBLIGAZIONE;
        --   aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGen.PG_OBBLIGAZIONE_SCADENZARIO;
        aGenRiga.CD_CDS_OBBLIGAZIONE:=aObb.CD_CDS;
        aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObb.ESERCIZIO;
        aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObb.ESERCIZIO_ORIGINALE;
        aGenRiga.PG_OBBLIGAZIONE:=aObb.PG_OBBLIGAZIONE;
        aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
        aGenRiga.DACR:=aGen.DACR;
        aGenRiga.UTCR:=aGen.UTCR;
        aGenRiga.UTUV:=aGen.UTUV;
        aGenRiga.DUVA:=aGen.DUVA;
        aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
        aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
        aListGenRighe(1):=aGenRiga;
        --
        CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

        -- Generazione righe mandato
        aManPRiga:=null;
        aManPRiga.CD_CDS:=aGen.cd_cds;
        aManPRiga.ESERCIZIO:=aGen.esercizio;
        aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
        aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
        aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
        aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
        aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
        aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
        aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
        aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
        aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
        aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
        aManPRiga.STATO:=aManP.stato;
        aManPRiga.CD_TERZO:=aGenRiga.cd_terzo;
        aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
        aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
        aManPRiga.IM_MANDATO_RIGA:=aObb.im_obbligazione;
        aManPRiga.IM_RITENUTE_RIGA:=0;
        aManPRiga.FL_PGIRO:='Y';
        aManPRiga.UTCR:=aUser;
        aManPRiga.DACR:=aTSNow;
        aManPRiga.UTUV:=aUser;
        aManPRiga.DUVA:=aTSNow;
        aManPRiga.PG_VER_REC:=1;
        aTotMandato:=aTotMandato+aManPRiga.im_mandato_riga;
        aListRigheManP(aListRigheManP.count+1):=aManPRiga;
        Update liquid_gruppo_cori
        Set cd_cds_acc_compens=aAcc.cd_cds,
            esercizio_acc_compens=aAcc.esercizio,
            esercizio_ori_acc_compens=aAcc.esercizio_originale,
            pg_acc_compens=aAcc.pg_accertamento,
            utuv=aUser,
            duva=aTSNow,
            pg_ver_rec=pg_ver_rec+1
        Where esercizio = aAggregato.esercizio
          and cd_cds = aAggregato.cd_cds
          and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
          and pg_liquidazione = aAggregato.pg_liquidazione
          and cd_gruppo_cr = aAggregato.cd_gruppo_cr
          and cd_regione = aAggregato.cd_regione
          and pg_comune = aAggregato.pg_comune
          and cd_cds_origine = aAggregato.cd_cds_origine
          and cd_uo_origine = aAggregato.cd_uo_origine
          and pg_liquidazione_origine = aAggregato.pg_liquidazione_origine;
     End If; -- fine generazione pgiro di compensazione su liq. locale negativa

    End If; -- Fine parte 1 esclusa per liq. di uo appartenenti a cds liquidati via interfaccia - If not lIsCdsInterDet And Not lIsCdsInterTot
      -- ================================================================================
      -- AGGIORNAMENTO DELLA PRATICA AL CENTRO (SOLO LIQUIDAZIONE LOCALE)
      -- ================================================================================
      If aAggregato.cd_unita_organizzativa <> aUOVERSACC.cd_unita_organizzativa and
         aAggregato.cd_unita_organizzativa <> aUOVERSCONTOBI.cd_unita_organizzativa and
         aAggregato.fl_Accentrato = 'Y' Then
         Declare
            aLocAggregato liquid_gruppo_cori%rowtype;
      begin
         Begin
             Select * into aLocAggregato
             From liquid_gruppo_cori
             Where cd_cds = aAggregato.cd_cds
               and esercizio = aAggregato.esercizio
               and cd_unita_organizzativa = aAggregato.cd_unita_organizzativa
               and pg_liquidazione = aAggregato.pg_liquidazione
               and cd_cds_origine = aAggregato.cd_cds_origine
               and cd_uo_origine = aAggregato.cd_uo_origine
               and pg_liquidazione_origine = aAggregato.pg_liquidazione_origine
               and cd_gruppo_cr = aAggregato.cd_gruppo_cr
               and cd_regione = aAggregato.cd_regione
               and pg_comune = aAggregato.pg_comune
             For update nowait;
         Exception when NO_DATA_FOUND then
            IBMERR001.RAISE_ERR_GENERICO('Dettaglio gruppo di liquidazione CORI non trovato');
         End;
             aggiornaPraticaGruppoCentro(aLiquid, aLocAggregato,aUOVERSACC,aTSNow,aUser, tb_ass_pgiro);
         End;
      End If;
    End Loop; -- FINE CICLO DI LOOP SU AGGREGATI -- LOOP 1

      -- ================================================================================
      -- GESTIONE DEGLI AGGREGATI RACCOLTI DA ALTRE UO DEL TIPO DI QUELLO IN PROCESSO
      -- ================================================================================
      -- Se l'UO Ã¨ quella di versamento CORI centralizzati
      -- Raccoglie tutti gli aggregati al centro del tipo specificato dall'utente (da es. prec e non)
      -- Non filtra piÃ¹ per UO
      if aCdUo = aUOVERSACC.cd_unita_organizzativa or aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa then
--pipe.send_message('UOENTE.cd_unita_organizzativa = '||UOENTE.cd_unita_organizzativa);
--pipe.send_message('aGruppi.cd_unita_organizzativa = '||aGruppi.cd_unita_organizzativa);
         Declare
            aVSX vsx_liquidazione_cori%rowtype;
            isFoundGruppoCentro boolean;
         Begin
            Select * Into aVSX
            From vsx_liquidazione_cori v
            Where v.pg_call = pgCall
              And v.esercizio = aGruppi.esercizio
              And v.cd_cds = aGruppi.cd_cds
              And v.cd_unita_organizzativa = aGruppi.cd_unita_organizzativa
              And v.cd_gruppo_cr = aGruppi.cd_gruppo_cr
              And v.cd_regione = aGruppi.cd_regione
              And v.pg_comune = aGruppi.pg_comune
              And v.pg_liquidazione = aPgLiq
              And v.cd_uo_origine = UOENTE.cd_unita_organizzativa;         --'999.000'
              --And v.cd_uo_origine <> aGruppi.cd_unita_organizzativa;   ?????????????????????????????????? perchÃ¨ c'Ã¨?????????

            isFoundGruppoCentro:=false;

---- da quÃ¬ parte la creazione di generico e mandati/reversali da liquid_gruppo_centro...

            For aGruppoCentro in (Select * From liquid_gruppo_centro
                                  Where esercizio = aGruppi.esercizio
                                    And cd_gruppo_cr = aGruppi.cd_gruppo_cr
                                    And cd_regione = aGruppi.cd_regione
                                    And pg_comune = aGruppi.pg_comune
                                    And da_esercizio_precedente = aLiquid.da_esercizio_precedente
                                    And stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
                                  For update nowait)
            Loop
               isFoundGruppoCentro:=true;
               Update liquid_gruppo_centro
               Set stato = CNRCTB575.STATO_GRUPPO_CENTRO_CHIUSO,
                   utuv=aUser,
                   duva=aTSNow,
                   pg_ver_rec=pg_ver_rec+1
               Where esercizio = aGruppoCentro.esercizio
                 And cd_gruppo_cr = aGruppoCentro.cd_gruppo_cr
                 And cd_regione = aGruppoCentro.cd_regione
                 And pg_comune = aGruppoCentro.pg_comune
                 And pg_gruppo_centro = aGruppoCentro.pg_gruppo_centro;

            -- Crea i mandati di eventuale restituzione crediti alle UO con gruppi negativi....GIRA SOLO SUI DATI DEGLI ANNI PRECEDENTI
            --restituzioneCrediti(aAggregato,aGruppoCentro,aTotReversale,aRevP,aListRigheRevP,aUser,aTSNow);
-- GGG TODO PER I MANDATI DI RESTITUZIONE..SOLO A FINE ANNO
--            If recParametriCNR.FL_TESORERIA_UNICA != 'Y' then
              restituzioneCrediti(aLiquid,aCdCds,aCdUo,aGruppi.cd_terzo_versamento,aGruppoCentro,aTotReversale,aRevP,aListRigheRevP,aTotMandato,aManP,aListRigheManP,aUser,aTSNow);
--            end if;
            aGen:=null;
            aGenRiga:=null;
            if aGruppoCentro.pg_obb_accentr is not null then
               begin
                 select * into aObbVC
                 from obbligazione
                 where cd_cds = aGruppoCentro.cd_cds_obb_accentr
                 and esercizio = aGruppoCentro.esercizio_obb_accentr
                 and esercizio_originale = aGruppoCentro.esercizio_ori_obb_accentr
                 and pg_obbligazione = aGruppoCentro.pg_obb_accentr
                 for update nowait;
               exception
                 when NO_DATA_FOUND then
                   If cnrutil.isLabelObbligazione() Then
                         IBMERR001.RAISE_ERR_GENERICO('Obbligazione di versamento cori al centro non trovata: '||aGruppoCentro.pg_obb_accentr);
                   Else
                         IBMERR001.RAISE_ERR_GENERICO('Impegno di versamento cori al centro non trovato: '||aGruppoCentro.pg_obb_accentr);
                   End If;
               end;
            -- se la UO Ã¨ quella del versamento su conto BI occorre rendere tronca la PGIRO in oggetto
            -- e crearla sempre tronca sulla UO di versamento (999.000)
--pipe.send_message('aCdUo = '||aCdUo);
--pipe.send_message('aUOVERSCONTOBI.cd_unita_organizzativa = '||aUOVERSCONTOBI.cd_unita_organizzativa);
               If aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa
--GG CONDIZIONE AGGIUNTA PER EVITARE LA PARTITA DI GIRO DALLA 000.407 ALLA 999.000
            and aUOVERSACC.cd_unita_organizzativa != aUOVERSCONTOBI.cd_unita_organizzativa then
                  --prendo la pgiro duale (e verifico che sia effettivamente chiusa prima di renderla tronca)
                  aObbOld.cd_cds:=aGruppoCentro.cd_cds_obb_accentr;
                  aObbOld.esercizio:=aGruppoCentro.esercizio_obb_accentr;
                  aObbOld.esercizio_originale:=aGruppoCentro.esercizio_ori_obb_accentr;
                  aObbOld.pg_obbligazione:=aGruppoCentro.pg_obb_accentr;
                  -- Il parametro 'X' serve solo per baipassare il controllo che non possono esistere piÃ¹ scadenze
                  -- per le pgiro di entrata
                  CNRCTB035.GETPGIROCDSINV(aObbOld,aObbScadOld,aObbScadVoceOld,aAccOld,aAccScadOld,aAccScadVoceOld,'X');
                  --rendo tronca la stessa (cioÃ¨ annullo la parte spesa)
                    CNRCTB043.troncaPraticaAccPgiro(aAccOld.esercizio,aAccOld.cd_cds,aAccOld.esercizio_originale,aAccOld.pg_accertamento,aTSNow,aUser);
                  -- devo creare la pgiro tronca sulla 999
                  aObbNew:=null;
                  aObbScadNew:=null;
                  aAccNew:=null;
                  aAccScadNew:=null;
                  --determino il capitolo da mettere sulla pgiro
                  Begin
                     Select distinct a.cd_elemento_voce
                     Into aCdEV
                     From ass_tipo_cori_ev a, tipo_cr_base b
                     Where b.esercizio = aGruppoCentro.esercizio
                       And b.cd_gruppo_cr = aGruppoCentro.cd_gruppo_cr
                       And a.cd_contributo_ritenuta = b.cd_contributo_ritenuta
                       And a.esercizio = aGruppoCentro.esercizio
                       And a.ti_gestione = CNRCTB001.GESTIONE_SPESE
                       And a.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS;
                  Exception
                          when TOO_MANY_ROWS then
                               IBMERR001.RAISE_ERR_GENERICO('Esiste piÃ¹ di un conto finanziario associato a CORI del gruppo '||aGruppoCentro.cd_gruppo_cr);
                          when NO_DATA_FOUND then
                              IBMERR001.RAISE_ERR_GENERICO('Conto finanziario associato a CORI del gruppo '||aGruppoCentro.cd_gruppo_cr||' non trovato');
                  End;
                  Begin
                     Select *
                     Into elementoVoce
                     From elemento_Voce
                     Where esercizio = aGruppoCentro.esercizio
                       And ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
                       And ti_gestione = CNRCTB001.GESTIONE_SPESE
                       And cd_elemento_Voce = aCdEV;
                  Exception
                          when NO_DATA_FOUND then
                             IBMERR001.RAISE_ERR_GENERICO('Codice voce '||aCdEV||' associata a CORI del gruppo '||aGruppoCentro.cd_gruppo_cr||' non trovato');
                  End;

                  aObbNew.CD_CDS:=aCdCds;
                  aObbNew.ESERCIZIO:=aEs;
                  aObbNew.ESERCIZIO_ORIGINALE:= aObbOld.esercizio_originale;   ------    aEs;
                  If aObbNew.esercizio = aObbNew.esercizio_originale Then
                       aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_IMP;
                  Else
                       aObbNew.CD_TIPO_DOCUMENTO_CONT:=CNRCTB018.TI_DOC_IMP_RES;
                  End If;
                  aObbNew.CD_UNITA_ORGANIZZATIVA:=aCdUo;
                  aObbNew.CD_CDS_ORIGINE:=aCdCds;
                  aObbNew.CD_UO_ORIGINE:=aCdUo;
                  aObbNew.TI_APPARTENENZA:=elementoVoce.ti_appartenenza;
                  aObbNew.TI_GESTIONE:=elementoVoce.ti_gestione;
                  aObbNew.CD_ELEMENTO_VOCE:=elementoVoce.cd_elemento_voce;
                  aObbNew.DT_REGISTRAZIONE:=TRUNC(trunc(aTSNow));
                  aObbNew.DS_OBBLIGAZIONE:='PGiro creata in automatico da liquidazione CORI';
                  aObbNew.NOTE_OBBLIGAZIONE:='';
                  aObbNew.CD_TERZO:=aObbOld.CD_TERZO;
                  aObbNew.IM_OBBLIGAZIONE:=aObbVC.IM_OBBLIGAZIONE;    --aObbOld.IM_OBBLIGAZIONE Ã¨ stata giÃ  annullata
                  aObbNew.stato_obbligazione:=CNRCTB035.STATO_DEFINITIVO;
                  aObbNew.im_costi_anticipati:=0;
                  aObbNew.fl_calcolo_automatico:='N';
                  aObbNew.fl_spese_costi_altrui:='N';
                  aObbNew.FL_PGIRO:='Y';
                  aObbNew.RIPORTATO:='N';
                  aObbNew.DACR:=aTSNow;
                  aObbNew.UTCR:=aUser;
                  aObbNew.DUVA:=aTSNow;
                  aObbNew.UTUV:=aUser;
                  aObbNew.PG_VER_REC:=1;
                  aObbNew.ESERCIZIO_COMPETENZA:=aEs;

                  CNRCTB030.CREAOBBLIGAZIONEPGIROTRONC(false,aObbNew,aObbScadNew,aAccNew,aAccScadNew,trunc(aTSNow));

                  CREALIQUIDCORIASSPGIRO(aLiquid,aGruppoCentro.cd_gruppo_cr,aGruppoCentro.cd_regione,aGruppoCentro.pg_comune,'S',aObbNew,aObbOld,null,null,aUser,trunc(aTSNow));
               End If;

                -- Creo il documento generico di spesa su partita di giro collegato all'annotazione di entrata su pgiro
                -- per versamento accentrato
                aGen.CD_CDS:=aGruppi.cd_cds;
                aGen.CD_UNITA_ORGANIZZATIVA:=aGruppi.cd_unita_organizzativa;
                aGen.ESERCIZIO:=aGruppi.esercizio;
                aGen.CD_CDS_ORIGINE:=aGruppi.cd_cds;
                aGen.CD_UO_ORIGINE:=aGruppi.cd_unita_organizzativa;
                aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_SPESA;
                aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
                aGen.DS_DOCUMENTO_GENERICO:='CORI-ACC'||' cds:'||aGruppi.cd_cds||' uo:'||aGruppi.cd_unita_organizzativa||' pg_liq.:'||aPgLiq||' gruppo cr:'||aGruppi.cd_gruppo_cr;
                aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
                aGen.IM_TOTALE:=aObbVC.im_obbligazione;
                aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
                aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
                aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
                aGen.CD_DIVISA:=aDivisaEuro;
                aGen.CAMBIO:=1;
                --  aGen.ESERCIZIO_LETTERA:=0;
                --  aGen.PG_LETTERA:=0;
                aGen.DACR:=aTSNow;
                aGen.UTCR:=aUser;
                aGen.DUVA:=aTSNow;
                aGen.UTUV:=aUser;
                aGen.PG_VER_REC:=1;
                aGen.DT_SCADENZA:=TRUNC(aTSNow);
                aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
                aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
                aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
                aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

                aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
                aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
                aGenRiga.CD_CDS:=aGen.CD_CDS;
                aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
                aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
                aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
                aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
                aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
                aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
                aGenRiga.CD_TERZO:=aGruppi.cd_terzo_versamento;
                aGenRiga.CD_MODALITA_PAG:=aGruppi.CD_MODALITA_PAGAMENTO;

                aGenRiga.PG_BANCA:=aGruppi.PG_BANCA;
                aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
                aGenRiga.NOME:=aAnagTst.NOME;
                aGenRiga.COGNOME:=aAnagTst.COGNOME;
                aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
                aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
                aGenRiga.STATO_COFI:=aGen.STATO_COFI;
                If aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa
--GG CONDIZIONE AGGIUNTA PER EVITARE LA PARTITA DI GIRO DALLA 000.407 ALLA 999.000
            and aUOVERSACC.cd_unita_organizzativa != aUOVERSCONTOBI.cd_unita_organizzativa then
                    aGenRiga.CD_CDS_OBBLIGAZIONE:=aObbNew.cd_cds;
                    aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aObbNew.esercizio;
                    aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aObbNew.esercizio_originale;
                    aGenRiga.PG_OBBLIGAZIONE:=aObbNew.pg_obbligazione;
                Else
                    aGenRiga.CD_CDS_OBBLIGAZIONE:=aGruppoCentro.cd_cds_obb_accentr;
                    aGenRiga.ESERCIZIO_OBBLIGAZIONE:=aGruppoCentro.esercizio_obb_accentr;
                    aGenRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGruppoCentro.esercizio_ori_obb_accentr;
                    aGenRiga.PG_OBBLIGAZIONE:=aGruppoCentro.pg_obb_accentr;
                End if;
                aGenRiga.PG_OBBLIGAZIONE_SCADENZARIO:=1;
                aGenRiga.DACR:=aGen.DACR;
                aGenRiga.UTCR:=aGen.UTCR;
                aGenRiga.UTUV:=aGen.UTUV;
                aGenRiga.DUVA:=aGen.DUVA;
                aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
                aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
                --
                aListGenRighe(1):=aGenRiga;
                CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

                -- Generazione righe mandato
                aManPRiga:=null;
                aManPRiga.CD_CDS:=aGen.cd_cds;
                aManPRiga.ESERCIZIO:=aGen.esercizio;
                aManPRiga.ESERCIZIO_OBBLIGAZIONE:=aGenRiga.esercizio_obbligazione;
                aManPRiga.ESERCIZIO_ORI_OBBLIGAZIONE:=aGenRiga.esercizio_ori_obbligazione;
                aManPRiga.PG_OBBLIGAZIONE:=aGenRiga.pg_obbligazione;
                aManPRiga.PG_OBBLIGAZIONE_SCADENZARIO:=aGenRiga.pg_obbligazione_scadenzario;
                aManPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
                aManPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
                aManPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
                aManPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
                aManPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
                aManPRiga.DS_MANDATO_RIGA:=aManP.ds_mandato;
                aManPRiga.STATO:=aManP.stato;
                aManPRiga.CD_TERZO:=aGenRiga.CD_TERZO;
                aManPRiga.PG_BANCA:=aGenRiga.pg_banca;
                aManPRiga.CD_MODALITA_PAG:=aGenRiga.cd_modalita_pag;
                aManPRiga.IM_MANDATO_RIGA:=aGen.IM_TOTALE;
                aManPRiga.IM_RITENUTE_RIGA:=0;
                aManPRiga.FL_PGIRO:='Y';
                aManPRiga.UTCR:=aUser;
                aManPRiga.DACR:=aTSNow;
                aManPRiga.UTUV:=aUser;
                aManPRiga.DUVA:=aTSNow;
                aManPRiga.PG_VER_REC:=1;
                --
                aTotMandato:=aTotMandato+aManPRiga.im_mandato_riga;
                aListRigheManP(aListRigheManP.count+1):=aManPRiga;
/*
                if aGruppoCentro.PG_ACC_ACCENTR_OPP is not null then
                    select sum(im_obbligazione)
                    into   somma_compensazioni
                    FROM   liquid_gruppo_centro_comp l, obbligazione o
                    where  cd_cds_OBB_accentr_OPP = o.cd_cds and
                           ES_OBB_ACCENTR_OPP = o.esercizio and
                           ES_ORIG_OBB_ACCENTR_OPP = o.esercizio_originale and
                           PG_OBB_ACCENTR_OPP = o.pg_obbligazione and
                           l.ESERCIZIO = aGruppoCentro.esercizio and
                           l.CD_GRUPPO_CR = aGruppoCentro.cd_gruppo_cr and
                           l.CD_REGIONE = aGruppoCentro.cd_regione and
                           l.PG_COMUNE = aGruppoCentro.pg_comune and
                           l.PG_GRUPPO_CENTRO = aGruppoCentro.pg_gruppo_centro and
                           PG_OBB_ACCENTR_OPP is not null;

                    if somma_compensazioni > 0 then
                      aAccGiroOpp.esercizio:=aGruppoCentro.ES_ACC_ACCENTR_OPP;
                      aAccGiroOpp.cd_cds:=aGruppoCentro.CD_CDS_ACC_ACCENTR_OPP;
                      aAccGiroOpp.esercizio_originale:=aGruppoCentro.ES_ORIG_ACC_ACCENTR_OPP;
                      aAccGiroOpp.pg_ACCERTAMENTO:=aGruppoCentro.PG_ACC_ACCENTR_OPP;

                      CNRCTB035.GETPGIROCDS(aAccGiroOpp,aAccScadGiroOpp,aAccScadVoceGiroOpp,aObbGiroOpp,aObbScadGiroOpp,aObbScadVoceGiroOpp);

                      CNRCTB043.modificaPraticaoBB(aAccGiroOpp.esercizio,aAccGiroOpp.cd_cds,aAccGiroOpp.esercizio_originale,aAccGiroOpp.pg_accertamento,-somma_compensazioni,aTSNow,aUser,'S','N');
                    end if;
                  begin
                    select * into aAccVC
                    from accertamento
                    where cd_cds = aGruppoCentro.CD_CDS_ACC_ACCENTR_OPP
                    and esercizio = aGruppoCentro.ES_ACC_ACCENTR_OPP
                    and esercizio_originale = aGruppoCentro.ES_ORIG_ACC_ACCENTR_OPP
                    and pg_ACCERTAMENTO = aGruppoCentro.PG_ACC_ACCENTR_OPP
                    for update nowait;
                  exception
                    when NO_DATA_FOUND then
                       IBMERR001.RAISE_ERR_GENERICO('Accertamento opposto di versamento cori al centro non trovata: '||aGruppoCentro.PG_ACC_ACCENTR_OPP);
                  end;
                  -- Creo il documento generico opposto di entrata su partita di giro collegato all'annotazione di entrata su pgiro
                  -- per versamento accentrato
                  aGen := null;
                  aGen.CD_CDS:=aGruppi.cd_cds;
                  aGen.CD_UNITA_ORGANIZZATIVA:=aGruppi.cd_unita_organizzativa;
                  aGen.ESERCIZIO:=aGruppi.esercizio;
                  aGen.CD_CDS_ORIGINE:=aGruppi.cd_cds;
                  aGen.CD_UO_ORIGINE:=aGruppi.cd_unita_organizzativa;
                  aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_ENTRATA;
                  aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
                  aGen.DS_DOCUMENTO_GENERICO:='CORI-ACC OPPOSTO'||' cds:'||aGruppi.cd_cds||' uo:'||aGruppi.cd_unita_organizzativa||' pg_liq.:'||aPgLiq||' gruppo cr:'||aGruppi.cd_gruppo_cr;
                  aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
                  aGen.IM_TOTALE:=aAccVC.im_accertamento;
                  aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
                  aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
                  aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
                  aGen.CD_DIVISA:=aDivisaEuro;
                  aGen.CAMBIO:=1;
                  aGen.DACR:=aTSNow;
                  aGen.UTCR:=aUser;
                  aGen.DUVA:=aTSNow;
                  aGen.UTUV:=aUser;
                  aGen.PG_VER_REC:=1;
                  aGen.DT_SCADENZA:=TRUNC(aTSNow);
                  aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
                  aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
                  aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
                  aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

                  aGenRiga := null;

                  aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
                  aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
                  aGenRiga.CD_CDS:=aGen.CD_CDS;
                  aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
                  aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
                  aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
                  aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
                  aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
                  aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
                  aGenRiga.CD_TERZO:=aGruppi.cd_terzo_versamento;


                  CNRCTB080.getTerzoPerUO(aGen.CD_UNITA_ORGANIZZATIVA, aCdTerzoUORev, aCdModPagUORev, aPgBancaUORev,aGruppi.esercizio);
                  aGenRiga.CD_MODALITA_PAG:=aGruppi.CD_MODALITA_PAGAMENTO;

                  aGenRiga.CD_TERZO_UO_CDS:= aCdTerzoUORev;
                  aGenRiga.PG_BANCA:= aPgBancaUORev;

                  aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
                  aGenRiga.NOME:=aAnagTst.NOME;
                  aGenRiga.COGNOME:=aAnagTst.COGNOME;
                  aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
                  aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
                  aGenRiga.STATO_COFI:=aGen.STATO_COFI;
                  aGenRiga.CD_CDS_ACCERTAMENTO:=aGruppoCentro.CD_CDS_ACC_ACCENTR_OPP;
                  aGenRiga.ESERCIZIO_ACCERTAMENTO:=aGruppoCentro.ES_ACC_ACCENTR_OPP;
                  aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aGruppoCentro.ES_ORIG_ACC_ACCENTR_OPP;
                  aGenRiga.PG_ACCERTAMENTO:=aGruppoCentro.PG_ACC_ACCENTR_OPP;
                  aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
                  aGenRiga.DACR:=aGen.DACR;
                  aGenRiga.UTCR:=aGen.UTCR;
                  aGenRiga.UTUV:=aGen.UTUV;
                  aGenRiga.DUVA:=aGen.DUVA;
                  aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
                  aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
                  --
                  aListGenRighe(1):=aGenRiga;
                  CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

                  -- Generazione righe mandato
                  aRevPRiga:=null;
                  aRevPRiga.CD_CDS:=aGen.cd_cds;
                  aRevPRiga.ESERCIZIO:=aGen.esercizio;
                  aRevPRiga.ESERCIZIO_ACCERTAMENTO:=aGenRiga.esercizio_ACCERTAMENTO;
                  aRevPRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aGenRiga.esercizio_ori_ACCERTAMENTO;
                  aRevPRiga.PG_ACCERTAMENTO:=aGenRiga.pg_ACCERTAMENTO;
                  aRevPRiga.PG_ACCERTAMENTO_SCADENZARIO:=aGenRiga.pg_ACCERTAMENTO_scadenzario;
                  aRevPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
                  aRevPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
                  aRevPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
                  aRevPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
                  aRevPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
                  aRevPRiga.DS_REVERSALE_RIGA:=aRevP.ds_REVERSALE;
                  aRevPRiga.STATO:=aRevP.stato;
                  aRevPRiga.CD_TERZO:=aGenRiga.CD_TERZO;
                  aRevPRiga.CD_TERZO_UO:=aGenRiga.CD_TERZO_UO_CDS;
                  aRevPRiga.PG_BANCA:=aGenRiga.pg_banca;
                  aRevPRiga.CD_MODALITA_PAG:=aCdModPagUORev;
                  aRevPRiga.IM_REVERSALE_RIGA:=aGen.IM_TOTALE;
                  aRevPRiga.FL_PGIRO:='Y';
                  aRevPRiga.UTCR:=aUser;
                  aRevPRiga.DACR:=aTSNow;
                  aRevPRiga.UTUV:=aUser;
                  aRevPRiga.DUVA:=aTSNow;
                  aRevPRiga.PG_VER_REC:=1;
                  --
                  aTotReversale:=aTotReversale+aRevPRiga.im_Reversale_riga;
                  aListRigheRevP(aListRigheRevP.count+1):=aRevPRiga;

                end if;
*/
                if aGruppoCentro.PG_ACC_ACCENTR_OPP is not null and aLiquid.da_esercizio_precedente = 'Y' then
                  select NVL(sum(ammontare),0)
                  INTO  somma_compensazioni
                  from  contributo_ritenuta c, liquid_gruppo_Cori_Det l, liquid_gruppo_Cori lc
                  where c.esercizio = l.esercizio_contributo_ritenuta and
                        c.pg_compenso = l.pg_compenso and
                        C.CD_CDS = l.cd_CDS_origine and
                        C.CD_UNITA_ORGANIZZATIVA = l.cd_uo_origine and
                        c.cd_contributo_ritenuta = l.cd_contributo_ritenuta and
                        C.TI_ENTE_PERCIPIENTE = l.TI_ENTE_PERCIPIENTE  and
                        l.ESERCIZIO = aGruppoCentro.esercizio and
                        l.CD_GRUPPO_CR = aGruppoCentro.cd_gruppo_cr and
                        l.CD_REGIONE = aGruppoCentro.cd_regione and
                        l.PG_COMUNE = aGruppoCentro.pg_comune and
                        l.CD_CDS = lc.CD_CDS and
                        l.ESERCIZIO = lc.esercizio and
                        l.CD_UNITA_ORGANIZZATIVA = lc.CD_UNITA_ORGANIZZATIVA and
                        l.PG_LIQUIDAZIONE = lc.PG_LIQUIDAZIONE and
                        l.CD_CDS_ORIGINE = lc.CD_CDS_ORIGINE and
                        l.CD_UO_ORIGINE = lc.CD_UO_ORIGINE and
                        l.PG_LIQUIDAZIONE_ORIGINE = lc.PG_LIQUIDAZIONE_ORIGINE and
                        l.CD_GRUPPO_CR = lc.CD_GRUPPO_CR and
                        l.CD_REGIONE = lc.CD_REGIONE and
                        l.PG_COMUNE = lc.PG_COMUNE and
                        lc.im_liquidato > 0 AND
                        C.AMMONTARE < 0 AND
                        LC.CD_CDS_ACC_ACCENTR_OPP = aGruppoCentro.CD_CDS_ACC_ACCENTR_OPP AND
                        LC.ES_ACC_ACCENTR_OPP = aGruppoCentro.ES_ACC_ACCENTR_OPP AND
                        LC.ES_ORIG_ACC_ACCENTR_OPP = aGruppoCentro.ES_ORIG_ACC_ACCENTR_OPP AND
                        LC.PG_ACC_ACCENTR_OPP = aGruppoCentro.PG_ACC_ACCENTR_OPP;

                    if somma_compensazioni < 0 then
                      somma_compensazioni := abs(somma_compensazioni);
                     -- Creo il documento generico opposto di entrata su partita di giro collegato all'annotazione di entrata su pgiro
                      -- per versamento accentrato
                      aGen := null;
                      aGen.CD_CDS:=aGruppi.cd_cds;
                      aGen.CD_UNITA_ORGANIZZATIVA:=aGruppi.cd_unita_organizzativa;
                      aGen.ESERCIZIO:=aGruppi.esercizio;
                      aGen.CD_CDS_ORIGINE:=aGruppi.cd_cds;
                      aGen.CD_UO_ORIGINE:=aGruppi.cd_unita_organizzativa;
                      aGen.CD_TIPO_DOCUMENTO_AMM:=CNRCTB100.TI_GEN_CORI_VER_ENTRATA;
                      aGen.DATA_REGISTRAZIONE:=TRUNC(aDateCont);
                      aGen.DS_DOCUMENTO_GENERICO:='CORI-ACC OPPOSTO'||' cds:'||aGruppi.cd_cds||' uo:'||aGruppi.cd_unita_organizzativa||' pg_liq.:'||aPgLiq||' gruppo cr:'||aGruppi.cd_gruppo_cr;
                      aGen.TI_ISTITUZ_COMMERC:=CNRCTB100.TI_ISTITUZIONALE;
                      aGen.IM_TOTALE:=somma_compensazioni;
                      aGen.STATO_COFI:=CNRCTB100.STATO_GEN_COFI_TOT_MR;
                      aGen.STATO_COGE:=CNRCTB100.STATO_COEP_EXC;
                      aGen.STATO_COAN:=CNRCTB100.STATO_COEP_EXC;
                      aGen.CD_DIVISA:=aDivisaEuro;
                      aGen.CAMBIO:=1;
                      aGen.DACR:=aTSNow;
                      aGen.UTCR:=aUser;
                      aGen.DUVA:=aTSNow;
                      aGen.UTUV:=aUser;
                      aGen.PG_VER_REC:=1;
                      aGen.DT_SCADENZA:=TRUNC(aTSNow);
                      aGen.STATO_PAGAMENTO_FONDO_ECO:=CNRCTB100.STATO_NO_PFONDOECO;
                      aGen.TI_ASSOCIATO_MANREV:=CNRCTB100.TI_ASSOC_TOT_MAN_REV ;
                      aGen.DT_DA_COMPETENZA_COGE:=TRUNC(aDateCont);
                      aGen.DT_A_COMPETENZA_COGE:=TRUNC(aDateCont);

                      aGenRiga := null;

                      aGenRiga.DT_DA_COMPETENZA_COGE:=aGen.DT_DA_COMPETENZA_COGE;
                      aGenRiga.DT_A_COMPETENZA_COGE:=aGen.DT_A_COMPETENZA_COGE;
                      aGenRiga.CD_CDS:=aGen.CD_CDS;
                      aGenRiga.CD_UNITA_ORGANIZZATIVA:=aGen.CD_UNITA_ORGANIZZATIVA;
                      aGenRiga.ESERCIZIO:=aGen.ESERCIZIO;
                      aGenRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.CD_TIPO_DOCUMENTO_AMM;
                      aGenRiga.DS_RIGA:=aGen.DS_DOCUMENTO_GENERICO;
                      aGenRiga.IM_RIGA_DIVISA:=aGen.IM_TOTALE;
                      aGenRiga.IM_RIGA:=aGen.IM_TOTALE;
                      aGenRiga.CD_TERZO:=aGruppi.cd_terzo_versamento;


                      CNRCTB080.getTerzoPerEnteContoBI(aGen.CD_UNITA_ORGANIZZATIVA, aCdTerzoVE, aCdModPagVE, aPgBancaVE);

                      aGenRiga.CD_TERZO_UO_CDS:=aCdTerzoVE;
                      aGenRiga.PG_BANCA_UO_CDS:=aPgBancaVE;
                      aGenRiga.CD_MODALITA_PAG_UO_CDS:=aCdModPagVE;

                      aGenRiga.RAGIONE_SOCIALE:=aAnagTst.RAGIONE_SOCIALE;
                      aGenRiga.NOME:=aAnagTst.NOME;
                      aGenRiga.COGNOME:=aAnagTst.COGNOME;
                      aGenRiga.CODICE_FISCALE:=aAnagTst.CODICE_FISCALE;
                      aGenRiga.PARTITA_IVA:=aAnagTst.PARTITA_IVA;
                      aGenRiga.STATO_COFI:=aGen.STATO_COFI;
                      aGenRiga.CD_CDS_ACCERTAMENTO:=aGruppoCentro.CD_CDS_ACC_ACCENTR_OPP;
                      aGenRiga.ESERCIZIO_ACCERTAMENTO:=aGruppoCentro.ES_ACC_ACCENTR_OPP;
                      aGenRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aGruppoCentro.ES_ORIG_ACC_ACCENTR_OPP;
                      aGenRiga.PG_ACCERTAMENTO:=aGruppoCentro.PG_ACC_ACCENTR_OPP;
                      aGenRiga.PG_ACCERTAMENTO_SCADENZARIO:=1;
                      aGenRiga.DACR:=aGen.DACR;
                      aGenRiga.UTCR:=aGen.UTCR;
                      aGenRiga.UTUV:=aGen.UTUV;
                      aGenRiga.DUVA:=aGen.DUVA;
                      aGenRiga.PG_VER_REC:=aGen.PG_VER_REC;
                      aGenRiga.TI_ASSOCIATO_MANREV:=aGen.TI_ASSOCIATO_MANREV;
                      --
                      aListGenRighe(1):=aGenRiga;
                      CNRCTB110.CREAGENERICOAGGOBBACC(aGen,aListGenRighe);

                      -- Generazione righe mandato
                      aRevPRiga:=null;
                      aRevPRiga.CD_CDS:=aGen.cd_cds;
                      aRevPRiga.ESERCIZIO:=aGen.esercizio;
                      aRevPRiga.ESERCIZIO_ACCERTAMENTO:=aGenRiga.esercizio_ACCERTAMENTO;
                      aRevPRiga.ESERCIZIO_ORI_ACCERTAMENTO:=aGenRiga.esercizio_ori_ACCERTAMENTO;
                      aRevPRiga.PG_ACCERTAMENTO:=aGenRiga.pg_ACCERTAMENTO;
                      aRevPRiga.PG_ACCERTAMENTO_SCADENZARIO:=aGenRiga.pg_ACCERTAMENTO_scadenzario;
                      aRevPRiga.CD_CDS_DOC_AMM:=aGen.cd_cds;
                      aRevPRiga.CD_UO_DOC_AMM:=aGen.cd_unita_organizzativa;
                      aRevPRiga.ESERCIZIO_DOC_AMM:=aGen.esercizio;
                      aRevPRiga.CD_TIPO_DOCUMENTO_AMM:=aGen.cd_tipo_documento_amm;
                      aRevPRiga.PG_DOC_AMM:=aGen.pg_documento_generico;
                      aRevPRiga.DS_REVERSALE_RIGA:=aGenRiga.DS_RIGA;
                      aRevPRiga.STATO:=aRevP.stato;
                      aRevPRiga.CD_TERZO:=aGenRiga.CD_TERZO;
                      aRevPRiga.CD_TERZO_UO:=aGenRiga.CD_TERZO_UO_CDS;
                      aRevPRiga.PG_BANCA:=aGenRiga.pg_banca;
                      aRevPRiga.CD_MODALITA_PAG:=aCdModPagUORev;
                      aRevPRiga.IM_REVERSALE_RIGA:=aGen.IM_TOTALE;
                      aRevPRiga.FL_PGIRO:='Y';
                      aRevPRiga.UTCR:=aUser;
                      aRevPRiga.DACR:=aTSNow;
                      aRevPRiga.UTUV:=aUser;
                      aRevPRiga.DUVA:=aTSNow;
                      aRevPRiga.PG_VER_REC:=1;
                      --
                      aTotReversale:=aTotReversale+aRevPRiga.im_Reversale_riga;
                      aListRigheRevP(aListRigheRevP.count+1):=aRevPRiga;
                end if;
            end if;    --fine if aGruppoCentro.PG_ACC_ACCENTR_OPP is not null and aL.da_esercizio_precedente = 'Y' then

            end if;    --fine if aGruppoCentro.pg_obb_accentr is not null then



            For aAggregatoOrig in (Select * From liquid_gruppo_cori
                                   Where esercizio = aGruppoCentro.esercizio
                                     And cd_unita_organizzativa <> aGruppi.cd_unita_organizzativa
                                     And cd_gruppo_cr = aGruppoCentro.cd_gruppo_cr
                                     And cd_regione = aGruppoCentro.cd_regione
                                     And pg_comune = aGruppoCentro.pg_comune
                                     And pg_gruppo_centro = aGruppoCentro.pg_gruppo_centro
                                     And stato = CNRCTB575.STATO_TRASFERITO
                                   For update nowait
             ) Loop -- loop 3
               -- Aggiorna lo stato della liquidazione di origine
               select *
               into aLiquidOrig
               from liquid_cori
               where esercizio = aAggregatoOrig.esercizio
               and cd_cds = aAggregatoOrig.cd_cds
               and cd_unita_organizzativa = aAggregatoOrig.cd_unita_organizzativa
               and pg_liquidazione = aAggregatoOrig.pg_liquidazione
               for update nowait;

               -- Aggiorna lo stato della riga di liquidazione in periferia
               update liquid_gruppo_cori set
                              stato = CNRCTB575.STATO_LIQUIDATO,
                  utuv=aUser,
                  duva=aTSNow,
                  pg_ver_rec=pg_ver_rec+1
               Where esercizio = aAggregatoOrig.esercizio
                 and cd_cds = aAggregatoOrig.cd_cds
                             and cd_unita_organizzativa = aAggregatoOrig.cd_unita_organizzativa
                             and pg_liquidazione = aAggregatoOrig.pg_liquidazione
                             and cd_gruppo_cr = aAggregatoOrig.cd_gruppo_cr
                             and cd_regione = aAggregatoOrig.cd_regione
                             and pg_comune = aAggregatoOrig.pg_comune
                             and cd_cds_origine = aAggregatoOrig.cd_cds_origine
                             and cd_uo_origine = aAggregatoOrig.cd_uo_origine
                             and pg_liquidazione_origine = aAggregatoOrig.pg_liquidazione_origine;

               -- Quando tutti i dettagli trasferiti di una liquidazione risultano liquidati
               -- la liquidazione diventa liquidata
               declare
                aLAGG liquid_gruppo_cori%rowtype;
               begin
                 select * into aLAGG from liquid_gruppo_cori
                 where esercizio = aAggregatoOrig.esercizio
                   and cd_cds = aAggregatoOrig.cd_cds
                   and cd_unita_organizzativa = aAggregatoOrig.cd_unita_organizzativa
                   and pg_liquidazione = aAggregatoOrig.pg_liquidazione
                             and stato = CNRCTB575.STATO_TRASFERITO
                 for update nowait;
               exception
                  when TOO_MANY_ROWS then
                     null;
                  when NO_DATA_FOUND then
                     update liquid_cori
                     set stato = CNRCTB575.STATO_LIQUIDATO,
                         utuv=aUser,
                         duva=aTSNow,
                         pg_ver_rec=pg_ver_rec+1
                     where esercizio = aAggregatoOrig.esercizio
                       and cd_cds = aAggregatoOrig.cd_cds
                       and cd_unita_organizzativa = aAggregatoOrig.cd_unita_organizzativa
                       and pg_liquidazione = aAggregatoOrig.pg_liquidazione;
               end;
            End Loop; -- loop 3
        End Loop; -- Chiusura loop sui gruppi centro (sono 2 da esercizio prec o corr.)
        if not isFoundGruppoCentro then
           IBMERR001.RAISE_ERR_GENERICO('Gruppo centro non liquidato non trovato per Gruppo CORI:'||aGruppi.cd_gruppo_cr||'.'||aGruppi.cd_regione||'.'||aGruppi.pg_comune);
        end if;
      Exception when NO_DATA_FOUND then
         null;
      End;
    End If; -- FINE GESTIONE SPECIALE PER UO DI VERSAMENTO ACCENTRATO if aCdUo = aUOVERSACC.cd_unita_organizzativa or aCdUo = aUOVERSCONTOBI.cd_unita_organizzativa

  End Loop; -- FINE CICLO DI LOOP PRINCIPALE aGruppi
     -- Questa parte, relativa alla chiusura delle partite di giro dei compensi va tolta per i CDS caricati via interfaccia
     -- Inizio parte 2 esclusa per liq. di uo appartenenti a cds liquidati via interfaccia
     if not lIsCdsInterDet And Not lIsCdsInterTot then
        aManP.IM_MANDATO:=aTotMandato;
        -- Aggiornamento del 14/03/2003 ERR. 530
        aRevP.IM_REVERSALE:=aTotReversale;
        if aTotMandato - aTotReversale >= 0 then
           aManP.IM_RITENUTE:=aTotReversale;
           CNRCTB037.generaDocumento(aManP,aListRigheManP,aRevP,aListRigheRevP);
        else
           --NON DOVREBBE ENTRARE MAI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
           /*
           CNRCTB037.generaDocumento(aManP,aListRigheManP);
           if aVersamenti.cd_unita_organizzativa = aUOVERSACC.cd_unita_organizzativa then
               CNRCTB037.generaReversale(aRevP,aListRigheRevP);
               -- mette il riferimento alla reversale sciolta su tutte e due le liquidazioni 999 e propria del centro
               update liquid_gruppo_cori l
                  set l.cd_cds_rev = aRevP.cd_cds
                  ,l.esercizio_rev = aRevP.esercizio
                  ,l.pg_rev = aRevP.pg_reversale
               Where l.esercizio = aVersamenti.esercizio
                 And l.cd_cds = aVersamenti.cd_cds
                 And l.cd_unita_organizzativa = aVersamenti.cd_unita_organizzativa
                 And l.pg_liquidazione = aPgLiq
                 And l.cd_gruppo_cr = aVersamenti.cd_gruppo_cr
                 --And cd_regione = aGruppi.cd_regione
                 --And pg_comune = aGruppi.pg_comune;
                 And (l.esercizio,l.cd_gruppo_cr,l.cd_regione,l.pg_comune) In
                      (Select c.esercizio,c.cd_gruppo_cr,c.cd_regione,c.pg_comune
                       From gruppo_cr_det c
                       Where c.cd_terzo_versamento = aVersamenti.cd_terzo_versamento
                         And c.cd_modalita_pagamento = aVersamenti.cd_modalita_pagamento
                         And c.pg_banca = aVersamenti.pg_banca);
           end if;
           */
           IBMERR001.RAISE_ERR_GENERICO('La quota complessiva CORI da versare per Gruppo CORI:'||aVersamenti.cd_gruppo_cr||' e Terzo di versamento .'||aVersamenti.cd_terzo_versamento||' risulta negativa!');
        end if;
        -- Aggiorna liquid_gruppo_cori con le informazioni relative al mandato di versamento/trasferimento
        Update liquid_gruppo_cori l
        Set l.cd_cds_doc = aManP.cd_cds
            ,l.esercizio_doc = aManP.esercizio
            ,l.pg_doc = aManP.pg_mandato
        Where l.esercizio = aVersamenti.esercizio
          And l.cd_cds = aVersamenti.cd_cds
          And l.cd_unita_organizzativa = aVersamenti.cd_unita_organizzativa
          And l.pg_liquidazione = aPgLiq
          And l.cd_gruppo_cr = aVersamenti.cd_gruppo_cr
          --And cd_regione = aGruppi.cd_regione
          --And pg_comune = aGruppi.pg_comune;
          And (l.esercizio,l.cd_gruppo_cr,l.cd_regione,l.pg_comune) In
              (Select c.esercizio,c.cd_gruppo_cr,c.cd_regione,c.pg_comune
               From gruppo_cr_det c
               Where c.cd_terzo_versamento = aVersamenti.cd_terzo_versamento
                 And c.cd_modalita_pagamento = aVersamenti.cd_modalita_pagamento
                 And c.pg_banca = aVersamenti.pg_banca);

     End If; -- Fine parte 2 esclusa per liq. di uo appartenenti a cds liquidati via interfaccia
  End Loop; -- FINE CICLO DI LOOP VERSAMENTO aVersamenti

  -- Aggiorna lo stato della liquidazione corrente
  if isLiquidParzAcc and aUOVERSACC.cd_unita_organizzativa <> aCdUo and aUOVERSCONTOBI.cd_unita_organizzativa <> aCdUo then
    aStatoLiquidazione:=CNRCTB575.STATO_TRASFERITO;
  else
    aStatoLiquidazione:=CNRCTB575.STATO_LIQUIDATO;
  end if;

  update liquid_cori
  set stato = aStatoLiquidazione,
      utuv=aUser,
      duva=aTSNow,
      pg_ver_rec=pg_ver_rec+1
  where esercizio = aEs
    and cd_cds = aCdCds
    and cd_unita_organizzativa = aCdUo
    and pg_liquidazione = aPgLiq;
end;

 procedure aggiorna_totale_gruppo_cori (p_cds in varchar2, p_uo in varchar2, p_aTotLiquid in number, p_aFlAccentrato in varchar2, p_aOldGruppoCr in varchar2, p_aOldRegioneCr in varchar2, p_aOldComuneCr in number, aPgLiq in number, aCdCds in varchar2, aCdUo in varchar2, aEs in number) is
 begin
   Update   liquid_gruppo_cori
     set  im_liquidato = p_aTotLiquid,
            fl_accentrato=p_aFlAccentrato,
          stato=decode(p_aFlAccentrato,'Y',CNRCTB575.STATO_TRASFERITO,CNRCTB575.STATO_LIQUIDATO)
     Where esercizio = aEs
      And   cd_cds = aCdCds
      And   cd_cds_origine = p_cds
      And   cd_unita_organizzativa = aCdUo
      And   cd_uo_origine = p_uo
      And   cd_gruppo_cr = p_aOldGruppoCr
      And   cd_regione = p_aOldRegioneCr
      And   pg_comune = p_aOldComuneCr
      And   pg_liquidazione = aPgLiq
      And   pg_liquidazione_origine = aPgLiq;
 end;

 -- ==========================
 -- Calcolo della liquidazione
 -- ==========================

 procedure calcolaLiquidazione(aCdCds varchar2, aEs number,daEsercizioPrec char, aCdUo varchar2, aPgLiq number, aDtDa date, aDtA date, aUser varchar2) is
  aTSNow date;
  aLiquidCori liquid_cori%rowtype;
  aLiquidGruppoCori liquid_gruppo_cori%rowtype;
  aLiquidGruppoCoriDet liquid_gruppo_cori_det%rowtype;
  aOldGruppoCr varchar2(30);
  aOldComuneCr number(10);
  aOldRegioneCr varchar2(10);
  aTotLiquid number(15,2);
  aOldPgCompenso number(10);
  isLiquidaSuInviato char(1);
  aUOVERSACC unita_organizzativa%rowtype;
  aUOVERSUNIFICATI unita_organizzativa%rowtype;
  aUOVERSCONTOBI unita_organizzativa%rowtype;
  aFlAccentrato char(1);
  aIsCdsInterDet boolean;
  aIsCdsInterTot boolean;
  aObb obbligazione%rowtype;
  UOENTE unita_organizzativa%rowtype;
  ESERCIZIO_UO_SPECIALI NUMBER;

 begin

  if    aPgLiq is null
     or aCdCds is null
     or aCdUo is null
     or aEs is null
     or daEsercizioPrec is null
     or aDtDa is null
     or aDtA is null
     or aUser is null
  then
   IBMERR001.RAISE_ERR_GENERICO('Alcuni parametri di liquidazione non sono stati specificati');
  end if;
  if to_number(to_char(aDtDa,'YYYY')) != to_number(to_char(aDtA,'YYYY')) THEN
     IBMERR001.RAISE_ERR_GENERICO('La date di inizio e la data di fine devono essere dello stesso anno');
  else
    ESERCIZIO_UO_SPECIALI := to_number(To_Char(aDtDa,'YYYY'));
  END IF;
  -- Controllo che la liquidazione al centro sia aperta
  -- Fix errore 729
  CNRCTB575.CHECKLIQUIDCENTROAPERTA(aEs);

  aIsCdsInterDet := IsCdsInterfDet(aCdCds, aEs);
  aIsCdsInterTot := IsCdsInterfTot(aCdCds, aEs);

  if aIsCdsInterDet then
     calcolaLiquidInterf (aCdCds,
                 aEs ,
             daEsercizioPrec ,
             aCdUo ,
             aPgLiq ,
             aDtDa ,
             aDtA ,
             aUser );
  end if;
  If aIsCdsInterTot Then
     calcolaLiquidInterfTot (aCdCds,
           aEs ,
         daEsercizioPrec ,
         aCdUo ,
         aPgLiq ,
         aDtDa ,
         aDtA ,
         aUser );
  end if;

  if not aIsCdsInterDet And Not aIsCdsInterTot  then -- Diramazione calcolo cori inter
   aTSNow:=sysdate;
   aLiquidCori.CD_CDS:=aCdCds;
   aLiquidCori.CD_UNITA_ORGANIZZATIVA:=aCdUo;
   aLiquidCori.ESERCIZIO:=aEs;
   aLiquidCori.PG_LIQUIDAZIONE:=aPgLiq;
   aLiquidCori.DA_ESERCIZIO_PRECEDENTE:=daEsercizioPrec;
   aLiquidCori.DT_DA:=trunc(aDtDa);
   aLiquidCori.DT_A:=trunc(aDtA);
   aLiquidCori.STATO:=CNRCTB575.STATO_INIZIALE;
   aLiquidCori.DACR:=aTSNow;
   aLiquidCori.UTCR:=aUser;
   aLiquidCori.DUVA:=aTSNow;
   aLiquidCori.UTUV:=aUser;
   aLiquidCori.PG_VER_REC:=0;
   CNRCTB575.INS_LIQUID_CORI(aLiquidCori);
   isLiquidaSuInviato:=CNRCTB575.ISLIQUIDACORIINVIATO(aEs);

   -- Estrazione delle UO di versamento CORI accentrato, unificato e
   -- della UO abilitata al versamento direttamente dal conto presso la Banca d'Italia (999.000)
   aUOVERSACC:=CNRCTB020.getUOVersCori(ESERCIZIO_UO_SPECIALI);
   aUOVERSUNIFICATI:=CNRCTB020.getUOVersCoriTuttaSAC(ESERCIZIO_UO_SPECIALI);
   aUOVERSCONTOBI:=CNRCTB020.getUOVersCoriContoBI(ESERCIZIO_UO_SPECIALI);

     -- Controllo che la liq. locale sia aperta nell'esercizio specificato
   if aUOVERSACC.cd_unita_organizzativa <> aCdUO and aUOVERSCONTOBI.cd_unita_organizzativa <> aCdUO then
           CNRCTB575.CHECKLIQUIDLOCALEAPERTA(aEs);
   end if;

   -- Se l'UO di versamento = all'UO in processo e la liquidazione Ã¨ di esercizio CORRENTE
   -- creo le righe relative alla liquidazione al centro dei gruppi locali
   -- targandole con 999.000, 999 rispettivamente in UO e CDS origine
   If aUOVERSACC.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo Then
        UOENTE:=CNRCTB020.GETUOENTE(aEs);
        For aGCentro in (Select * from liquid_gruppo_centro
                         Where esercizio = aEs
                           And da_esercizio_precedente = daEsercizioPrec
                           And stato = CNRCTB575.STATO_GRUPPO_CENTRO_INIZIALE
                           And cd_gruppo_cr = GruppoValido(esercizio, ESERCIZIO_UO_SPECIALI, cd_gruppo_cr, aCdUo)
                         for update nowait)
        Loop -- loop 1
           Update liquid_gruppo_centro set
             cd_cds_lc = aCdCds,
             cd_uo_lc =aCdUo,
             pg_lc =aPgLiq,
             utuv=aUser,
             duva=aTSNow,
             pg_ver_rec=pg_ver_rec+1
           Where esercizio = aGCentro.esercizio
             And pg_gruppo_centro = aGCentro.pg_gruppo_centro
             And cd_gruppo_cr = aGCentro.cd_gruppo_cr
             And cd_regione = aGCentro.cd_regione
             And pg_comune = aGCentro.pg_comune;
           aObb:=null;
           if aGCentro.pg_obb_accentr is not null then
              Begin
                Select * into aObb
                From obbligazione
                Where cd_cds = aGCentro.cd_cds_obb_accentr
                  And esercizio = aGCentro.esercizio_obb_accentr
                  And esercizio_originale = aGCentro.esercizio_ori_obb_accentr
                  And pg_obbligazione = aGCentro.pg_obb_accentr
                For update nowait;
              Exception when NO_DATA_FOUND then
                 If cnrutil.isLabelObbligazione() Then
                    IBMERR001.RAISE_ERR_GENERICO('Obbligazione per versamento del gruppo centro non trovata');
                 Else
                    IBMERR001.RAISE_ERR_GENERICO('Impegno per versamento del gruppo centro non trovato');
                 End If;
              End;
           end if;
           aLiquidGruppoCori.ESERCIZIO:=aGCentro.esercizio;
           aLiquidGruppoCori.CD_CDS:=aCdCds;
           aLiquidGruppoCori.CD_UNITA_ORGANIZZATIVA:=aCdUo;
           aLiquidGruppoCori.PG_LIQUIDAZIONE:=aPgLiq;
           aLiquidGruppoCori.CD_CDS_ORIGINE:=UOENTE.cd_unita_padre;
           aLiquidGruppoCori.CD_UO_ORIGINE:=UOENTE.cd_unita_organizzativa;
           aLiquidGruppoCori.PG_LIQUIDAZIONE_ORIGINE:=0;
           aLiquidGruppoCori.CD_GRUPPO_CR:=aGCentro.cd_gruppo_cr;
           aLiquidGruppoCori.CD_REGIONE:=aGCentro.cd_regione;
           aLiquidGruppoCori.PG_COMUNE:=aGCentro.pg_comune;
           aLiquidGruppoCori.IM_LIQUIDATO:=nvl(aObb.im_obbligazione,0);
           aLiquidGruppoCori.FL_ACCENTRATO:='N';
           aLiquidGruppoCori.STATO:=CNRCTB575.STATO_LIQUIDATO;
           aLiquidGruppoCori.pg_gruppo_centro:=aGCentro.pg_gruppo_centro;
           aLiquidGruppoCori.cd_cds_obb_accentr:=aGCentro.cd_cds_obb_accentr;
           aLiquidGruppoCori.esercizio_obb_accentr:=aGCentro.esercizio_obb_accentr;
           aLiquidGruppoCori.esercizio_ori_obb_accentr:=aGCentro.esercizio_ori_obb_accentr;
           aLiquidGruppoCori.pg_obb_accentr:=aGCentro.pg_obb_accentr;
           aLiquidGruppoCori.ESERCIZIO_DOC:=null;
           aLiquidGruppoCori.CD_CDS_DOC:=null;
           aLiquidGruppoCori.PG_DOC:=null;
           aLiquidGruppoCori.DACR:=aTSNow;
           aLiquidGruppoCori.UTCR:=aUser;
           aLiquidGruppoCori.DUVA:=aTSNow;
           aLiquidGruppoCori.UTUV:=aUser;
           aLiquidGruppoCori.PG_VER_REC:=1;
           CNRCTB575.INS_LIQUID_GRUPPO_CORI(aLiquidGruppoCori);

        End Loop; -- loop 1
   End If;    --If aUOVERSACC.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo


   aLiquidGruppoCori:=null;
   aOldGruppoCr:=null;
   aOldPgCompenso:=null;
   aOldComuneCr:=null;
   aOldRegioneCr:=null;

   -- Si prendono i dati della UO in processo
   For aCori in  (select * from v_calcola_liquid_cori_det a
            where a.esercizio = aEs
      -- Questa clausola definisce la provenienza del compenso:
      -- se daEsercizioPrec='Y'
      -- vengono estratti compensi con mandati/reversali dell'esercizio precedente a quello di scrivania
                    -- altrimenti
      -- vengono estratti compensi con mandati/reversali nell'esercizio di scrivania
        and a.esercizio_doc_cont = decode(daEsercizioPrec,'Y',a.esercizio-1,a.esercizio)
        and a.cd_cds = aCdCds
        and a.cd_unita_organizzativa = aCdUo
        and decode(isLiquidaSuInviato,'Y',a.dt_trasmissione_mandato,a.dt_emissione_mandato) >= trunc(aDtDa)
        and decode(isLiquidaSuInviato,'Y',a.dt_trasmissione_mandato,a.dt_emissione_mandato) < trunc(aDtA + 1)
        and a.cd_gruppo_cr = GruppoValido(aEs, ESERCIZIO_UO_SPECIALI, a.cd_gruppo_cr, aCdUo)
        and not exists (Select 1 from liquid_gruppo_cori_det a1
                Where -- Esclude i cori giÃ  processati
              --a1.cd_cds = a.cd_cds
              a1.esercizio_contributo_ritenuta = a.esercizio_compenso
              --And a1.cd_unita_organizzativa = a.cd_unita_organizzativa
              And a1.cd_cds_origine = a.cd_cds
              And a1.cd_uo_origine = a.cd_unita_organizzativa
              And (a1.pg_liquidazione != aPgLiq
             Or
             a1.esercizio != a.esercizio
             Or
             --se ad es. verso da 000.407 e poi vado nella 000.101, il pg_liq potrebbe
             --essere lo stesso e quindi rivedo il compenso. Devo non farlo vedere
             (a1.pg_liquidazione = aPgLiq And
              a1.esercizio = a.esercizio And
              ( (a1.cd_cds = aUOVERSUNIFICATI.cd_unita_padre And
                 a1.cd_unita_organizzativa = aUOVERSUNIFICATI.cd_unita_organizzativa)
                 Or
                (a1.cd_cds = a.cd_cds And
                 a1.cd_unita_organizzativa = a.cd_unita_organizzativa) )
              )
             )
              And a1.cd_contributo_ritenuta = a.cd_contributo_ritenuta
              And a1.pg_compenso = a.pg_compenso
              And a1.ti_ente_percipiente = a.ti_ente_percipiente)
      order by cd_gruppo_cr, cd_regione, pg_comune, pg_compenso, cd_contributo_ritenuta)
   Loop -- loop 3
     -- Lock del compenso
     if aCori.cd_gruppo_cr is null then
        IBMERR001.RAISE_ERR_GENERICO('Gruppo CORI non trovato per CORI:'||aCori.cd_contributo_ritenuta);
     end if;

     if aCori.cd_regione is null or aCori.pg_comune is null then
        IBMERR001.RAISE_ERR_GENERICO('Dettaglio Gruppo CORI non trovato per CORI:'||aCori.cd_contributo_ritenuta||' di tipo:'||aCori.cd_classificazione_cori||' regione:'||aCori.cd_regione_cori||' comune:'||aCori.pg_comune_cori);
     end if;

     if aOldPgCompenso is null or aOldPgCompenso<>aCori.pg_compenso then
        CNRCTB100.LOCKDOCAMM(CNRCTB100.TI_COMPENSO,aCori.cd_cds,aCori.esercizio_compenso,aCori.cd_unita_organizzativa,aCori.pg_compenso);
      aOldPgCompenso:=aCori.pg_compenso;
     end if;

     if aOldGruppoCr is null
      or aOldRegioneCr is null
      or aOldComuneCr is null
      or aOldGruppoCr<>aCori.cd_gruppo_cr
      or aOldRegioneCr<>aCori.cd_regione
      or aOldComuneCr<>aCori.pg_comune      then
         -- Estraggo l'informazione di versamento accentrato per il gruppo specificato
         -- Aggiorno le informazioni relative ad importo liquidato e flag accentrato del totale precedente
         if aOldGruppoCr is not null then
           aggiorna_totale_gruppo_cori (aCdCds, aCdUo, aTotLiquid, aFlAccentrato, aOldGruppoCr, aOldRegioneCr, aOldComuneCr, aPgLiq, aCdCds, aCdUo, aEs);
         end if;

         Begin
                  -- Calcolo il nuovo valore del flag accentrato
                  select decode(aCori.cd_unita_organizzativa,aUOVERSACC.cd_unita_organizzativa,'N',nvl(d.fl_accentrato,b.fl_accentrato))
                  into aFlAccentrato
                  from gruppo_cr b, gruppo_cr_det c, gruppo_cr_uo d
                  where b.esercizio = ESERCIZIO_UO_SPECIALI
                  and b.cd_gruppo_cr = aCori.cd_gruppo_cr
                  and c.esercizio = ESERCIZIO_UO_SPECIALI
                  and c.cd_gruppo_cr = aCori.cd_gruppo_cr
                  and c.cd_regione = aCori.cd_regione
                  and c.pg_comune = aCori.pg_comune
                  and d.esercizio (+)= c.esercizio
                  and d.cd_gruppo_cr (+)= c.cd_gruppo_cr
                  and d.cd_unita_organizzativa (+)=aCori.cd_unita_organizzativa;
         Exception when NO_DATA_FOUND then
            IBMERR001.RAISE_ERR_GENERICO('Gruppo CORI non trovato');
         End;
         aTotLiquid:=0;
         aOldGruppoCr:=aCori.cd_gruppo_cr;
         aOldRegioneCr:=aCori.cd_regione;
         aOldComuneCr:=aCori.pg_comune;
         aLiquidGruppoCori.ESERCIZIO:=aEs;
         aLiquidGruppoCori.CD_CDS:=aCori.cd_cds;
         aLiquidGruppoCori.CD_UNITA_ORGANIZZATIVA:=aCori.cd_unita_organizzativa;
          aLiquidGruppoCori.CD_CDS_ORIGINE:=aCori.cd_cds;
         aLiquidGruppoCori.CD_UO_ORIGINE:=aCori.cd_unita_organizzativa;
         aLiquidGruppoCori.PG_LIQUIDAZIONE:=aLiquidCori.pg_liquidazione;
         aLiquidGruppoCori.PG_LIQUIDAZIONE_ORIGINE:=aLiquidCori.pg_liquidazione;
         aLiquidGruppoCori.CD_GRUPPO_CR:=aCori.cd_gruppo_cr;
         aLiquidGruppoCori.CD_REGIONE:=aCori.cd_regione;
         aLiquidGruppoCori.PG_COMUNE:=aCori.pg_comune;
         aLiquidGruppoCori.IM_LIQUIDATO:=null;
         aLiquidGruppoCori.FL_ACCENTRATO:='N';
         aLiquidGruppoCori.STATO:=CNRCTB575.STATO_LIQUIDATO;
         aLiquidGruppoCori.ESERCIZIO_DOC:=null;
         aLiquidGruppoCori.CD_CDS_DOC:=null;
         aLiquidGruppoCori.PG_DOC:=null;
         aLiquidGruppoCori.DACR:=aTSNow;
         aLiquidGruppoCori.UTCR:=aUser;
         aLiquidGruppoCori.DUVA:=aTSNow;
         aLiquidGruppoCori.UTUV:=aUser;
         aLiquidGruppoCori.PG_VER_REC:=1;
         aOldGruppoCr:=aLiquidGruppoCori.cd_gruppo_cr;
         CNRCTB575.INS_LIQUID_GRUPPO_CORI(aLiquidGruppoCori);
     end if;    --aOldGruppoCr is null or .....
     aLiquidGruppoCoriDet.CD_CDS:=aCori.cd_cds;
     aLiquidGruppoCoriDet.CD_UNITA_ORGANIZZATIVA:=aCori.cd_unita_organizzativa;
     aLiquidGruppoCoriDet.CD_CDS_ORIGINE:=aCori.cd_cds;
     aLiquidGruppoCoriDet.CD_UO_ORIGINE:=aCori.cd_unita_organizzativa;
     aLiquidGruppoCoriDet.ESERCIZIO:=aEs;
     aLiquidGruppoCoriDet.ESERCIZIO_CONTRIBUTO_RITENUTA:=aCori.esercizio_compenso;
     aLiquidGruppoCoriDet.PG_LIQUIDAZIONE:=aPgLiq;
     aLiquidGruppoCoriDET.PG_LIQUIDAZIONE_ORIGINE:=aPgLiq;
     aLiquidGruppoCoriDet.CD_GRUPPO_CR:=aCori.cd_gruppo_cr;
     aLiquidGruppoCoriDet.CD_REGIONE:=aCori.cd_regione;
     aLiquidGruppoCoriDet.PG_COMUNE:=aCori.pg_comune;
     aLiquidGruppoCoriDet.CD_CONTRIBUTO_RITENUTA:=aCori.cd_contributo_ritenuta;
     aLiquidGruppoCoriDet.PG_COMPENSO:=aCori.pg_compenso;
     aLiquidGruppoCoriDet.TI_ENTE_PERCIPIENTE:=aCori.ti_ente_percipiente;
     aLiquidGruppoCoriDet.DACR:=aTSNow;
     aLiquidGruppoCoriDet.UTCR:=aUser;
     aLiquidGruppoCoriDet.DUVA:=aTSNow;
     aLiquidGruppoCoriDet.UTUV:=aUser;
     aLiquidGruppoCoriDet.PG_VER_REC:=1;
     CNRCTB575.INS_LIQUID_GRUPPO_CORI_DET(aLiquidGruppoCoriDet);
     aTotLiquid:=aTotLiquid + aCori.im_cori;
   End Loop; -- loop 3
     If aOldGruppoCr is not null then
       -- Aggiorno il totale precedente
       aggiorna_totale_gruppo_cori (aCdCds, aCdUo, aTotLiquid, aFlAccentrato, aOldGruppoCr, aOldRegioneCr, aOldComuneCr, aPgLiq, aCdCds, aCdUo, aEs);
     End if;

     -- Se la UO in processo Ã¨ quella abilitata a versare i dati di tutte le UO della SAC oppure Ã¨ la 999.000
     If aUOVERSUNIFICATI.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo Then
       For aCdUoSAC In (Select a.cd_unita_organizzativa, a.cd_unita_padre
              From unita_organizzativa a
              Where a.fl_cds = 'N'
                And a.cd_tipo_unita = CNRCTB020.TIPO_SAC
                And exists (Select 1 from v_unita_organizzativa_valida
                            Where esercizio = aEs
                              And cd_unita_organizzativa = a.cd_unita_organizzativa)
                And exists (Select 1 from gruppo_cr_uo WHERE esercizio = ESERCIZIO_UO_SPECIALI
                              And cd_unita_organizzativa = a.cd_unita_organizzativa)
                              And a.cd_unita_organizzativa <> aCdUo)
       Loop
          aLiquidGruppoCori:=null;
          aOldGruppoCr:=null;
          aOldPgCompenso:=null;
          aOldComuneCr:=null;
          aOldRegioneCr:=null;

          For aCori in (select * from v_calcola_liquid_cori_det a
                        where a.esercizio = aEs
                        -- Questa clausola definisce la provenienza del compenso:
                        -- se daEsercizioPrec='Y'
                        -- vengono estratti compensi con mandati/reversali dell'esercizio precedente a quello di scrivania
                                      -- altrimenti
                        -- vengono estratti compensi con mandati/reversali nell'esercizio di scrivania
                          and a.esercizio_doc_cont = decode(daEsercizioPrec,'Y',a.esercizio-1,a.esercizio)
                          and a.cd_cds = aCdUoSAC.cd_unita_padre
                          and a.cd_unita_organizzativa = aCdUoSAC.cd_unita_organizzativa
                          and decode(isLiquidaSuInviato,'Y',a.dt_trasmissione_mandato,a.dt_emissione_mandato) >= trunc(aDtDa)
                          and decode(isLiquidaSuInviato,'Y',a.dt_trasmissione_mandato,a.dt_emissione_mandato) < trunc(aDtA + 1)
                          and a.cd_gruppo_cr = GruppoValido(aEs, ESERCIZIO_UO_SPECIALI, a.cd_gruppo_cr, aCdUo)
                          and not exists (Select 1 from liquid_gruppo_cori_det a1
                                          Where -- Esclude i cori giÃ  processati
                                          --    a1.cd_cds = a.cd_cds
                                          a1.esercizio_contributo_ritenuta = a.esercizio_compenso
                                          --And a1.cd_unita_organizzativa = a.cd_unita_organizzativa
                                          And a1.cd_cds_origine = a.cd_cds
                                          And a1.cd_uo_origine = a.cd_unita_organizzativa
                                          And (a1.pg_liquidazione != aPgLiq
                                               Or
                                               a1.esercizio != a.esercizio
                                               Or
                                               --se ad es. verso da 000.407 e poi vado nella 000.101, il pg_liq potrebbe
                                               --essere lo stesso e quindi rivedo il compenso. Devo non farlo vedere
                                               (a1.pg_liquidazione = aPgLiq And
                                                a1.esercizio = a.esercizio And
                                                 ( (a1.cd_cds = aUOVERSUNIFICATI.cd_unita_padre And
                                                    a1.cd_unita_organizzativa = aUOVERSUNIFICATI.cd_unita_organizzativa)
                                                    Or
                                                   (a1.cd_cds = a.cd_cds And
                                                    a1.cd_unita_organizzativa = a.cd_unita_organizzativa) )
                                               )
                                               )
                                          And a1.cd_contributo_ritenuta = a.cd_contributo_ritenuta
                                          And a1.pg_compenso = a.pg_compenso
                                          And a1.ti_ente_percipiente = a.ti_ente_percipiente)
                        order by cd_unita_organizzativa, cd_gruppo_cr, cd_regione, pg_comune, pg_compenso, cd_contributo_ritenuta)
          Loop -- loop 3
          -- Lock del compenso
          if aCori.cd_gruppo_cr is null then
             IBMERR001.RAISE_ERR_GENERICO('Gruppo CORI non trovato per CORI:'||aCori.cd_contributo_ritenuta||' e per UO: '||aCori.cd_unita_organizzativa);
          end if;

          if aCori.cd_regione is null or aCori.pg_comune is null then
             IBMERR001.RAISE_ERR_GENERICO('Dettaglio Gruppo CORI non trovato per CORI:'||aCori.cd_contributo_ritenuta||' di tipo:'||aCori.cd_classificazione_cori||' regione:'||aCori.cd_regione_cori||' comune:'||aCori.pg_comune_cori||' e per UO: '||aCori.cd_unita_organizzativa);
          end if;

          if aOldPgCompenso is null or aOldPgCompenso<>aCori.pg_compenso then
             CNRCTB100.LOCKDOCAMM(CNRCTB100.TI_COMPENSO,aCori.cd_cds,aCori.esercizio_compenso,aCori.cd_unita_organizzativa,aCori.pg_compenso);
           aOldPgCompenso:=aCori.pg_compenso;
          end if;

          if aOldGruppoCr is null
             or aOldRegioneCr is null
             or aOldComuneCr is null
             or aOldGruppoCr<>aCori.cd_gruppo_cr
             or aOldRegioneCr<>aCori.cd_regione
             or aOldComuneCr<>aCori.pg_comune      then
              -- Estraggo l'informazione di versamento accentrato per il gruppo specificato
              -- Aggiorno le informazioni relative ad importo liquidato e flag accentrato del totale precedente
              if aOldGruppoCr is not null then
                aggiorna_totale_gruppo_cori(aCdUoSAC.cd_unita_padre,aCdUoSAC.cd_unita_organizzativa,  aTotLiquid, aFlAccentrato, aOldGruppoCr, aOldRegioneCr, aOldComuneCr, aPgLiq, aCdCds, aCdUo, aEs);
              End if;

              Begin
                      -- Calcolo il nuovo valore del flag accentrato
                      select decode(aCori.cd_unita_organizzativa,aUOVERSACC.cd_unita_organizzativa,'N',aUOVERSCONTOBI.cd_unita_organizzativa,'N',nvl(d.fl_accentrato,b.fl_accentrato))
                      into aFlAccentrato
                      from gruppo_cr b, gruppo_cr_det c, gruppo_cr_uo d
                      where b.esercizio = ESERCIZIO_UO_SPECIALI
                      and b.cd_gruppo_cr = aCori.cd_gruppo_cr
                      and c.esercizio = ESERCIZIO_UO_SPECIALI
                      and c.cd_gruppo_cr = aCori.cd_gruppo_cr
                      and c.cd_regione = aCori.cd_regione
                      and c.pg_comune = aCori.pg_comune
                      and d.esercizio (+)= c.esercizio
                      and d.cd_gruppo_cr (+)= c.cd_gruppo_cr
                        and d.cd_unita_organizzativa (+)=aCori.cd_unita_organizzativa;
              Exception when NO_DATA_FOUND then
                 IBMERR001.RAISE_ERR_GENERICO('Gruppo CORI non trovato');
              End;

              aTotLiquid:=0;
              aOldGruppoCr:=aCori.cd_gruppo_cr;
              aOldRegioneCr:=aCori.cd_regione;
              aOldComuneCr:=aCori.pg_comune;
              aLiquidGruppoCori.ESERCIZIO:=aEs;
              aLiquidGruppoCori.CD_CDS:=aCdCds;
              aLiquidGruppoCori.CD_UNITA_ORGANIZZATIVA:=aCdUo;
              aLiquidGruppoCori.CD_CDS_ORIGINE:=aCori.cd_cds;
              aLiquidGruppoCori.CD_UO_ORIGINE:=aCori.cd_unita_organizzativa;
              aLiquidGruppoCori.PG_LIQUIDAZIONE:=aPgLiq;
              aLiquidGruppoCori.PG_LIQUIDAZIONE_ORIGINE:=aPgLiq;--0;
              aLiquidGruppoCori.CD_GRUPPO_CR:=aCori.cd_gruppo_cr;
              aLiquidGruppoCori.CD_REGIONE:=aCori.cd_regione;
              aLiquidGruppoCori.PG_COMUNE:=aCori.pg_comune;
              aLiquidGruppoCori.IM_LIQUIDATO:=null;
              aLiquidGruppoCori.FL_ACCENTRATO:='N';
              aLiquidGruppoCori.STATO:=CNRCTB575.STATO_LIQUIDATO;
              aLiquidGruppoCori.ESERCIZIO_DOC:=null;
              aLiquidGruppoCori.CD_CDS_DOC:=null;
              aLiquidGruppoCori.PG_DOC:=null;
              aLiquidGruppoCori.DACR:=aTSNow;
              aLiquidGruppoCori.UTCR:=aUser;
              aLiquidGruppoCori.DUVA:=aTSNow;
              aLiquidGruppoCori.UTUV:=aUser;
              aLiquidGruppoCori.PG_VER_REC:=1;
              aOldGruppoCr:=aLiquidGruppoCori.cd_gruppo_cr;
              CNRCTB575.INS_LIQUID_GRUPPO_CORI(aLiquidGruppoCori);
          end if;   --if aOldGruppoCr is null or ....
          aLiquidGruppoCoriDet.CD_CDS:=aCdCds;
          aLiquidGruppoCoriDet.CD_UNITA_ORGANIZZATIVA:=aCdUo;
          aLiquidGruppoCoriDet.CD_CDS_ORIGINE:=aCori.cd_cds;
          aLiquidGruppoCoriDet.CD_UO_ORIGINE:=aCori.cd_unita_organizzativa;
          aLiquidGruppoCoriDet.ESERCIZIO:=aEs;
          aLiquidGruppoCoriDet.ESERCIZIO_CONTRIBUTO_RITENUTA:=aCori.esercizio_compenso;
          aLiquidGruppoCoriDet.PG_LIQUIDAZIONE:=aPgLiq;
          aLiquidGruppoCoriDET.PG_LIQUIDAZIONE_ORIGINE:=aPgLiq;--0;
          aLiquidGruppoCoriDet.CD_GRUPPO_CR:=aCori.cd_gruppo_cr;
          aLiquidGruppoCoriDet.CD_REGIONE:=aCori.cd_regione;
          aLiquidGruppoCoriDet.PG_COMUNE:=aCori.pg_comune;
          aLiquidGruppoCoriDet.CD_CONTRIBUTO_RITENUTA:=aCori.cd_contributo_ritenuta;
          aLiquidGruppoCoriDet.PG_COMPENSO:=aCori.pg_compenso;
          aLiquidGruppoCoriDet.TI_ENTE_PERCIPIENTE:=aCori.ti_ente_percipiente;
          aLiquidGruppoCoriDet.DACR:=aTSNow;
          aLiquidGruppoCoriDet.UTCR:=aUser;
          aLiquidGruppoCoriDet.DUVA:=aTSNow;
          aLiquidGruppoCoriDet.UTUV:=aUser;
          aLiquidGruppoCoriDet.PG_VER_REC:=1;
          CNRCTB575.INS_LIQUID_GRUPPO_CORI_DET(aLiquidGruppoCoriDet);

          aTotLiquid:=aTotLiquid + aCori.im_cori;

          End Loop; -- loop 3
       if aOldGruppoCr is not null then

          aggiorna_totale_gruppo_cori(aCdUoSAC.cd_unita_padre,aCdUoSAC.cd_unita_organizzativa,  aTotLiquid, aFlAccentrato, aOldGruppoCr, aOldRegioneCr, aOldComuneCr, aPgLiq, aCdCds, aCdUo, aEs);

       end if;
       End Loop;
     End If;       --  fine If aUOVERSUNIFICATI.cd_unita_organizzativa = aCdUo or aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo
  end if; -- Diramazione calcolo cori inter
 end;

 function IsCdsInterfDet (aCdCds varchar2, aEs number) return boolean is
 coriInterfDet number;
 begin
      select count(*)
      into coriInterfDet
      from LIQUID_CORI_INTERF_CDS
      where esercizio = aEs
      and   cd_cds = aCdCds
      And tipo = 'D';
    if coriInterfDet > 0 then
       return true;
    else
       return false;
    end if;
 end;
 Function IsCdsInterfTot (aCdCds varchar2, aEs number) return boolean is
 coriInterfTot number;
 begin
      select count(*)
      into coriInterfTot
      from LIQUID_CORI_INTERF_CDS
      where esercizio = aEs
      and   cd_cds = aCdCds
      And tipo = 'T';
    if coriInterfTot > 0 then
       return true;
    else
       return false;
    end if;
 end;
 Procedure calcolaLiquidInterf (aCdCds varchar2, aEs number,daEsercizioPrec char,aCdUo varchar2, aPgLiq number, aDtDa date, aDtA date, aUser varchar2) is
 lTSNow date;
 isLiquidaSuInviato char(1);
 lUOVERSACC unita_organizzativa%rowtype;
 lLiquidCori liquid_cori%rowtype;
 lLiquidGruppoCori liquid_gruppo_cori%rowtype;
 lTotLiquidato number (15,2);
 lFlAccentrato char (1);
 aOldCdCori varchar2(10);
 aCdGruppo varchar2(10);

-- lLiquidGruppoCoriInter liquid_cori_interf%rowtype;
 begin
    lTotLiquidato := 0;
    lTSNow:=sysdate;

    lLiquidCori.CD_CDS:=aCdCds;
    lLiquidCori.CD_UNITA_ORGANIZZATIVA:=aCdUo;
    lLiquidCori.ESERCIZIO:=aEs;
    lLiquidCori.PG_LIQUIDAZIONE:=aPgLiq;
    -- La liquidazione via interfaccia non puÃ² essere da esercizio precedente
    lLiquidCori.DA_ESERCIZIO_PRECEDENTE:=daEsercizioPrec;
    lLiquidCori.DT_DA:=trunc(aDtDa);
    lLiquidCori.DT_A:=trunc(aDtA);
    lLiquidCori.STATO:=CNRCTB575.STATO_INIZIALE;
    lLiquidCori.DACR:=lTSNow;
    lLiquidCori.UTCR:=aUser;
    lLiquidCori.DUVA:=lTSNow;
    lLiquidCori.UTUV:=aUser;
    lLiquidCori.PG_VER_REC:=0;
    CNRCTB575.INS_LIQUID_CORI(lLiquidCori);

--    isLiquidaSuInviato:=CNRCTB575.ISLIQUIDACORIINVIATO(aEs);

    -- Estrazione dell'UO di versamento CORI
    lUOVERSACC:=CNRCTB020.getUOVersCori(aEs);

    for aLCID in (
     select * from liquid_cori_interf_dett where
                 cd_cds = aCdCds
           and   cd_unita_organizzativa = aCdUO
           and   esercizio = decode(daEsercizioPrec,'Y',aEs-1,aEs)
           and   pg_liquidazione is null
       and   dt_inizio = trunc(aDtDa)
       and   dt_fine  = trunc(aDtA)
    order by cd_contributo_ritenuta
    for update nowait
    ) loop
     aOldCdCori:=null;
     if aOldCdCori is null or aLCID.cd_contributo_ritenuta <> aOldCdCori then
      aOldCdCori:=aLCID.cd_contributo_ritenuta;
        begin
       select cd_gruppo_cr into aCdGruppo from tipo_cr_base where
                  esercizio=aEs
        and cd_contributo_ritenuta = aLCID.cd_contributo_ritenuta;
      exception when NO_DATA_FOUND then
         IBMERR001.RAISE_ERR_GENERICO('Gruppo cori non trovato per CORI:'||aLCID.cd_contributo_ritenuta);
    end;
     end if;
       update liquid_cori_interf_dett set
        cd_gruppo_cr = aCdGruppo,
        pg_liquidazione = aPgLiq,
        utuv = aUser,
        duva = lTSNow,
        pg_ver_rec=pg_ver_rec+1
       where
        CD_CDS=aLCID.CD_CDS AND
        ESERCIZIO=aLCID.ESERCIZIO AND
        CD_UNITA_ORGANIZZATIVA=aLCID.CD_UNITA_ORGANIZZATIVA AND
        PG_CARICAMENTO=aLCID.PG_CARICAMENTO AND
        DT_INIZIO=aLCID.DT_INIZIO AND
        DT_FINE=aLCID.DT_FINE AND
        MATRICOLA=aLCID.MATRICOLA AND
        CODICE_FISCALE=aLCID.CODICE_FISCALE AND
        TI_PAGAMENTO=aLCID.TI_PAGAMENTO AND
        ESERCIZIO_COMPENSO=aLCID.ESERCIZIO_COMPENSO AND
        CD_IMPONIBILE=aLCID.CD_IMPONIBILE AND
        TI_ENTE_PERCIPIENTE=aLCID.TI_ENTE_PERCIPIENTE AND
        CD_CONTRIBUTO_RITENUTA=aLCID.CD_CONTRIBUTO_RITENUTA;
    end loop;
    for lLiquidGruppoCoriInter in (select
                   CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE,
           sum(IM_RITENUTA) IM_LIQUIDATO
                   from liquid_cori_interf_dett
                    where
                                            cd_cds = aCdCds
                                      and   cd_unita_organizzativa = aCdUO
                                      and   esercizio = decode(daEsercizioPrec,'Y',aEs-1,aEs)
                                      and   pg_liquidazione = aPgLiq
                                      and   dt_inizio = trunc(aDtDa)
                                      and   dt_fine   = trunc(aDtA)
                    group by CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE
                    order by CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE
                   )
    loop
        select decode(lLiquidGruppoCoriInter.cd_unita_organizzativa,
              lUOVERSACC.cd_unita_organizzativa,'N',
            nvl(d.fl_accentrato,b.fl_accentrato))
      into lFlAccentrato
      from gruppo_cr b, gruppo_cr_det c, gruppo_cr_uo d
      where   b.esercizio                  = lLiquidGruppoCoriInter.esercizio
            and b.cd_gruppo_cr         = lLiquidGruppoCoriInter.cd_gruppo_cr
          and c.esercizio          = lLiquidGruppoCoriInter.esercizio
            and c.cd_gruppo_cr         = lLiquidGruppoCoriInter.cd_gruppo_cr
          and c.cd_regione           = lLiquidGruppoCoriInter.cd_regione
          and c.pg_comune            = lLiquidGruppoCoriInter.pg_comune
        and d.esercizio (+)        = c.esercizio
            and d.cd_gruppo_cr (+)           = c.cd_gruppo_cr
          and d.cd_unita_organizzativa (+) = lLiquidGruppoCoriInter.cd_unita_organizzativa;
      if lFlAccentrato = 'Y' then
        lLiquidGruppoCori.CD_CDS                  := lLiquidGruppoCoriInter.cd_cds;
        lLiquidGruppoCori.ESERCIZIO           := aEs;
        lLiquidGruppoCori.CD_UNITA_ORGANIZZATIVA  := lLiquidGruppoCoriInter.CD_UNITA_ORGANIZZATIVA;
        lLiquidGruppoCori.PG_LIQUIDAZIONE       := lLiquidCori.PG_LIQUIDAZIONE;
        lLiquidGruppoCori.CD_CDS_ORIGINE        := lLiquidGruppoCoriInter.cd_cds;
        lLiquidGruppoCori.CD_UO_ORIGINE       := lLiquidGruppoCoriInter.CD_UNITA_ORGANIZZATIVA;
        lLiquidGruppoCori.PG_LIQUIDAZIONE_ORIGINE := lLiquidCori.PG_LIQUIDAZIONE;
        lLiquidGruppoCori.CD_GRUPPO_CR      := lLiquidGruppoCoriInter.CD_GRUPPO_CR;
        lLiquidGruppoCori.CD_REGIONE        := lLiquidGruppoCoriInter.CD_REGIONE;
        lLiquidGruppoCori.PG_COMUNE         := lLiquidGruppoCoriInter.PG_COMUNE;
        lLiquidGruppoCori.IM_LIQUIDATO      := nvl(lLiquidGruppoCoriInter.IM_LIQUIDATO,0);
        lLiquidGruppoCori.FL_ACCENTRATO     := lFlAccentrato;
        lLiquidGruppoCori.STATO             := CNRCTB575.STATO_TRASFERITO;
        lLiquidGruppoCori.CD_CDS_DOC        := NULL;
        lLiquidGruppoCori.ESERCIZIO_DOC     := NULL;
        lLiquidGruppoCori.PG_DOC          := NULL;
        lLiquidGruppoCori.CD_CDS_OBB_ACCENTR    := NULL;
        lLiquidGruppoCori.ESERCIZIO_OBB_ACCENTR := NULL;
        lLiquidGruppoCori.ESERCIZIO_ORI_OBB_ACCENTR := NULL;
        lLiquidGruppoCori.PG_OBB_ACCENTR      := NULL;
        lLiquidGruppoCori.DACR          := lTSNow;
        lLiquidGruppoCori.UTCR          := aUser;
        lLiquidGruppoCori.DUVA          := lTSNow;
        lLiquidGruppoCori.UTUV          := aUser;
        lLiquidGruppoCori.PG_VER_REC        := 1;
        CNRCTB575.INS_LIQUID_GRUPPO_CORI(lLiquidGruppoCori);
        lTotLiquidato := lTotLiquidato + lLiquidGruppoCoriInter.IM_LIQUIDATO;
      else
          IBMERR001.RAISE_ERR_GENERICO('Il gruppo cori relativo all''unita organizzativa risulta essere non accentrato');
      end if;

    end loop;
 end;
Procedure calcolaLiquidInterfTot (aCdCds varchar2, aEs number,daEsercizioPrec char,aCdUo varchar2, aPgLiq number, aDtDa date, aDtA date, aUser varchar2) is
 lTSNow date;
 isLiquidaSuInviato char(1);
 lUOVERSACC unita_organizzativa%rowtype;
 lLiquidCori liquid_cori%rowtype;
 lLiquidGruppoCori liquid_gruppo_cori%rowtype;
 lTotLiquidato number (15,2);
 lFlAccentrato char (1);

 Begin
      lTotLiquidato := 0;
    lTSNow:=sysdate;

    lLiquidCori.CD_CDS:=aCdCds;
    lLiquidCori.CD_UNITA_ORGANIZZATIVA:=aCdUo;
    lLiquidCori.ESERCIZIO:=aEs;
    lLiquidCori.PG_LIQUIDAZIONE:=aPgLiq;
    -- La liquidazione via interfaccia non puÃ² essere da esercizio precedente
    lLiquidCori.DA_ESERCIZIO_PRECEDENTE:=daEsercizioPrec;
    lLiquidCori.DT_DA:=trunc(aDtDa);
    lLiquidCori.DT_A:=trunc(aDtA);
    lLiquidCori.STATO:=CNRCTB575.STATO_INIZIALE;
    lLiquidCori.DACR:=lTSNow;
    lLiquidCori.UTCR:=aUser;
    lLiquidCori.DUVA:=lTSNow;
    lLiquidCori.UTUV:=aUser;
    lLiquidCori.PG_VER_REC:=0;
    CNRCTB575.INS_LIQUID_CORI(lLiquidCori);

    -- Estrazione dell'UO di versamento CORI
    lUOVERSACC:=CNRCTB020.getUOVersCori(aEs);

    --Valorizzazione del progressivo liquidazionje interfaccia
          Update liquid_cori_interf
          Set pg_liquidazione = aPgLiq,
              utuv = aUser,
              duva = lTSNow,
              pg_ver_rec=pg_ver_rec+1
          Where CD_CDS=aCdCds And
                ESERCIZIO=aEs And
                CD_UNITA_ORGANIZZATIVA=aCdUo And
          DT_INIZIO=trunc(aDtDa) And
          DT_FINE=trunc(aDtA) And
          pg_liquidazione Is Null;

    for lLiquidGruppoCoriInter in
      (Select CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR,
        CD_REGIONE, PG_COMUNE, Nvl(Sum(IM_LIQUIDATO),0) IM_LIQUIDATO
     From liquid_cori_interf
     Where cd_cds = aCdCds
                   And   cd_unita_organizzativa = aCdUO
                   And   esercizio = decode(daEsercizioPrec,'Y',aEs-1,aEs)
                   And   pg_liquidazione = aPgLiq
                   And   dt_inizio = trunc(aDtDa)
                   And   dt_fine = trunc(aDtA)
     Group by CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE
     Order by CD_CDS, ESERCIZIO, CD_UNITA_ORGANIZZATIVA, CD_GRUPPO_CR, CD_REGIONE, PG_COMUNE)
    Loop
           Select decode(lLiquidGruppoCoriInter.cd_unita_organizzativa,
            lUOVERSACC.cd_unita_organizzativa,'N',
          nvl(d.fl_accentrato,b.fl_accentrato))
     Into lFlAccentrato
     From gruppo_cr b, gruppo_cr_det c, gruppo_cr_uo d
     Where b.esercizio                  = lLiquidGruppoCoriInter.esercizio
           And b.cd_gruppo_cr          = lLiquidGruppoCoriInter.cd_gruppo_cr
         And c.esercizio           = lLiquidGruppoCoriInter.esercizio
           And c.cd_gruppo_cr          = lLiquidGruppoCoriInter.cd_gruppo_cr
         And c.cd_regione            = lLiquidGruppoCoriInter.cd_regione
         And c.pg_comune           = lLiquidGruppoCoriInter.pg_comune
     And d.esercizio (+)         = c.esercizio
           And d.cd_gruppo_cr (+)           = c.cd_gruppo_cr
         And d.cd_unita_organizzativa (+) = lLiquidGruppoCoriInter.cd_unita_organizzativa;

      if lFlAccentrato = 'Y' then
             lLiquidGruppoCori.CD_CDS                  := lLiquidGruppoCoriInter.cd_cds;
             lLiquidGruppoCori.ESERCIZIO          := aEs;
       lLiquidGruppoCori.CD_UNITA_ORGANIZZATIVA  := lLiquidGruppoCoriInter.CD_UNITA_ORGANIZZATIVA;
       lLiquidGruppoCori.PG_LIQUIDAZIONE        := lLiquidCori.PG_LIQUIDAZIONE;
       lLiquidGruppoCori.CD_CDS_ORIGINE       := lLiquidGruppoCoriInter.cd_cds;
       lLiquidGruppoCori.CD_UO_ORIGINE        := lLiquidGruppoCoriInter.CD_UNITA_ORGANIZZATIVA;
       lLiquidGruppoCori.PG_LIQUIDAZIONE_ORIGINE := lLiquidCori.PG_LIQUIDAZIONE;
       lLiquidGruppoCori.CD_GRUPPO_CR     := lLiquidGruppoCoriInter.CD_GRUPPO_CR;
       lLiquidGruppoCori.CD_REGIONE       := lLiquidGruppoCoriInter.CD_REGIONE;
       lLiquidGruppoCori.PG_COMUNE        := lLiquidGruppoCoriInter.PG_COMUNE;
       lLiquidGruppoCori.IM_LIQUIDATO     := nvl(lLiquidGruppoCoriInter.IM_LIQUIDATO,0);
       lLiquidGruppoCori.FL_ACCENTRATO      := lFlAccentrato;
       lLiquidGruppoCori.STATO              := CNRCTB575.STATO_TRASFERITO;
       lLiquidGruppoCori.CD_CDS_DOC       := NULL;
       lLiquidGruppoCori.ESERCIZIO_DOC      := NULL;
       lLiquidGruppoCori.PG_DOC         := NULL;
       lLiquidGruppoCori.CD_CDS_OBB_ACCENTR   := NULL;
       lLiquidGruppoCori.ESERCIZIO_OBB_ACCENTR  := NULL;
       lLiquidGruppoCori.ESERCIZIO_ORI_OBB_ACCENTR  := NULL;
       lLiquidGruppoCori.PG_OBB_ACCENTR     := NULL;
       lLiquidGruppoCori.DACR         := lTSNow;
       lLiquidGruppoCori.UTCR         := aUser;
       lLiquidGruppoCori.DUVA         := lTSNow;
       lLiquidGruppoCori.UTUV         := aUser;
       lLiquidGruppoCori.PG_VER_REC       := 1;
       CNRCTB575.INS_LIQUID_GRUPPO_CORI(lLiquidGruppoCori);
       lTotLiquidato := lTotLiquidato + lLiquidGruppoCoriInter.IM_LIQUIDATO;
      else
          IBMERR001.RAISE_ERR_GENERICO('Il gruppo cori relativo all''unita organizzativa risulta essere non accentrato');
      end if;

    end loop;
 end;
 function IsGruppoF24EP (aEs number, aGruppo varchar2) return boolean is
    countGruppi number;
 begin
      Select count(*)
      Into countGruppi
      From GRUPPO_CR
      Where esercizio = aEs
        And cd_gruppo_cr = aGruppo
        And (fl_f24online = 'Y' or fl_f24online_previd = 'Y');
    if countGruppi > 0 then
       return true;
    else
       return false;
    end if;
 end;
 function GruppoF24EP (aEs number, aGruppo varchar2) return varchar2 is
    gruppo varchar2(10);
 begin
      Select cd_gruppo_cr
      Into gruppo
      From GRUPPO_CR
      Where esercizio = aEs
        And cd_gruppo_cr = aGruppo
        And (fl_f24online = 'Y' or fl_f24online_previd = 'Y');
      return gruppo;
 exception
     when others then
         gruppo := 'XXXXXXXXXX';
         return gruppo;
 end;

  function GruppoValido (aEs number, ESERCIZIO_UO_SPECIALI number, aGruppo varchar2, aCdUo varchar2) return varchar2 is
    gruppo varchar2(10);
    aUOVERSACC unita_organizzativa%rowtype;
    aUOVERSCONTOBI unita_organizzativa%rowtype;
    CONTA NUMBER;
 begin
      aUOVERSACC:=CNRCTB020.getUOVersCori(ESERCIZIO_UO_SPECIALI);
      aUOVERSCONTOBI:=CNRCTB020.getUOVersCoriContoBI(ESERCIZIO_UO_SPECIALI);
      -- Se la UO di versamento accentrato Ã¨ uguale alla UO dei versamenti su Conto BI tutti i gruppi sono validi
     select count(1) into conta
     from unita_organizzativa u, gruppo_cr_uo c
     where u.cd_unita_organizzativa = aCdUo
      and u.cd_unita_organizzativa = c.cd_unita_organizzativa
      and c.esercizio = ESERCIZIO_UO_SPECIALI
      and fl_cds = 'N'
      and cd_tipo_unita = CNRCTB020.TIPO_SAC;

      IF (aUOVERSACC.cd_unita_organizzativa = aUOVERSCONTOBI.cd_unita_organizzativa) THEN
        RETURN aGruppo;
      ELSif aUOVERSACC.cd_unita_organizzativa = aCdUo or CONTA > 0 then
        Begin
          Select cd_gruppo_cr
          Into gruppo
          From GRUPPO_CR
          Where esercizio = aEs
            And cd_gruppo_cr = aGruppo
            And (fl_f24online = 'N' and fl_f24online_previd = 'N');
        Exception
           when others then
               gruppo := 'XXXXXXXXXX';
               return gruppo;
        End;
      elsif aUOVERSCONTOBI.cd_unita_organizzativa = aCdUo then
        Begin
          Select cd_gruppo_cr
          Into gruppo
          From GRUPPO_CR
          Where esercizio = aEs
            And cd_gruppo_cr = aGruppo
            And (fl_f24online = 'Y' or fl_f24online_previd = 'Y');
        Exception
           when others then
               gruppo := 'XXXXXXXXXX';
               return gruppo;
        End;
      -- la UO in processo non Ã¨ nÃ¨ quella dei versamenti accentrati, nÃ¨ una della SAC, nÃ¨ quella dei versamente su Conto BI
      -- quindi tutti i gruppi sono validi
      else
        return aGruppo;
      end if;

      return gruppo;
 end;
 Procedure CREALIQUIDCORIASSPGIRO(aLiquid liquid_cori%rowtype,aGruppo varchar2, aRegione varchar2, aComune number,aTipo varchar2,aObbNew obbligazione%rowtype,aObbOld obbligazione%rowtype,aAccNew accertamento%rowtype,aAccOld accertamento%rowtype,aUser varchar2,aTSNow date) is
 begin
    if aTipo = 'S' then
        insert into LIQUID_CORI_ASS_PGIRO (
            CD_CDS,
            ESERCIZIO,
            CD_UNITA_ORGANIZZATIVA,
            PG_LIQUIDAZIONE,
            CD_GRUPPO_CR,
            CD_REGIONE,
            PG_COMUNE,
            TIPO_PGIRO,
            CD_CDS_PGIRO_NEW,
            ESERCIZIO_PGIRO_NEW,
            PG_PGIRO_NEW,
            ESERCIZIO_ORI_PGIRO_NEW,
            CD_CDS_PGIRO_ORIGINE,
            ESERCIZIO_PGIRO_ORIGINE,
            PG_PGIRO_ORIGINE,
            ESERCIZIO_ORI_PGIRO_ORIGINE,
            UTCR,
            DACR,
            UTUV,
            DUVA,
            PG_VER_REC)
          values(
            aLiquid.CD_CDS,
            aLiquid.ESERCIZIO,
            aLiquid.CD_UNITA_ORGANIZZATIVA,
            aLiquid.PG_LIQUIDAZIONE,
            aGruppo,
            aRegione,
            aComune,
            aTipo,
            aObbNew.CD_CDS,
            aObbNew.ESERCIZIO,
            aObbNew.PG_OBBLIGAZIONE,
            aObbNew.ESERCIZIO_ORIGINALE,
            aObbOld.CD_CDS,
            aObbOld.ESERCIZIO,
            aObbOld.PG_OBBLIGAZIONE,
            aObbOld.ESERCIZIO_ORIGINALE,
            aUser,
            aTSNow,
            aUser,
            aTSNow,
            1);
    Elsif aTipo = 'E' then
        insert into LIQUID_CORI_ASS_PGIRO (
            CD_CDS,
            ESERCIZIO,
            CD_UNITA_ORGANIZZATIVA,
            PG_LIQUIDAZIONE,
            CD_GRUPPO_CR,
            CD_REGIONE,
            PG_COMUNE,
            TIPO_PGIRO,
            CD_CDS_PGIRO_NEW,
            ESERCIZIO_PGIRO_NEW,
            PG_PGIRO_NEW,
            ESERCIZIO_ORI_PGIRO_NEW,
            CD_CDS_PGIRO_ORIGINE,
            ESERCIZIO_PGIRO_ORIGINE,
            PG_PGIRO_ORIGINE,
            ESERCIZIO_ORI_PGIRO_ORIGINE,
            UTCR,
            DACR,
            UTUV,
            DUVA,
            PG_VER_REC)
          values(
            aLiquid.CD_CDS,
            aLiquid.ESERCIZIO,
            aLiquid.CD_UNITA_ORGANIZZATIVA,
            aLiquid.PG_LIQUIDAZIONE,
            aGruppo,
            aRegione,
            aComune,
            aTipo,
            aAccNew.CD_CDS,
            aAccNew.ESERCIZIO,
            aAccNew.PG_ACCERTAMENTO,
            aAccNew.ESERCIZIO_ORIGINALE,
            aAccOld.CD_CDS,
            aAccOld.ESERCIZIO,
            aAccOld.PG_ACCERTAMENTO,
            aAccOld.ESERCIZIO_ORIGINALE,
            aUser,
            aTSNow,
            aUser,
            aTSNow,
            1);
    End if;
 end;

 Procedure CREA_ASS_PGIRO_GR_C(tb_ass_pgiro tab_ass_pgiro, aUser varchar2, aTSNow date) is
 begin
  For i in 1..Nvl(tb_ass_pgiro.Count,0) Loop
    insert into ASS_PGIRO_GRUPPO_CENTRO (
        cd_cds            ,
        cd_uo             ,
        cd_cds_orig       ,
        cd_uo_orig        ,
        esercizio         ,
        es_compenso       ,
        pg_compenso       ,
        pg_liq            ,
        pg_liq_orig       ,
        cd_gr_cr          ,
        cd_regione        ,
        pg_comune         ,
        cd_cori           ,
        ti_en_per         ,
        ti_origine        ,
        es_acc            ,
        es_ori_acc        ,
        cds_acc           ,
        pg_acc            ,
        uo_acc            ,
        es_obb            ,
        es_ori_obb        ,
        cds_obb           ,
        pg_obb            ,
        uo_obb            ,
        cd_cds_acc_pgiro  ,
        es_acc_pgiro      ,
        es_orig_acc_pgiro ,
        pg_acc_pgiro      ,
        uo_acc_pgiro      ,
        voce_acc_pgiro    ,
        cd_cds_obb_pgiro  ,
        es_obb_pgiro      ,
        es_orig_obb_pgiro ,
        pg_obb_pgiro      ,
        ti_origine_pgiro  ,
        uo_obb_pgiro      ,
        voce_obb_pgiro    ,
        CD_CDS_ACC_PGIRO_OPP,
        ES_ACC_PGIRO_OPP,
        ES_ORIG_ACC_PGIRO_OPP,
        PG_ACC_PGIRO_OPP,
        CD_CDS_OBB_PGIRO_OPP,
        ES_OBB_PGIRO_OPP,
        ES_ORIG_OBB_PGIRO_OPP,
        PG_OBB_PGIRO_OPP,
        DACR              ,
        UTCR              ,
        DUVA              ,
        UTUV              ,
        PG_VER_REC)
      values(
        tb_ass_pgiro(i).cd_cds     ,
        tb_ass_pgiro(i).cd_uo      ,
        tb_ass_pgiro(i).cd_cds_orig,
        tb_ass_pgiro(i).cd_uo_orig ,
        tb_ass_pgiro(i).esercizio  ,
        tb_ass_pgiro(i).es_compenso ,
        tb_ass_pgiro(i).pg_compenso ,
        tb_ass_pgiro(i).pg_liq     ,
        tb_ass_pgiro(i).pg_liq_orig,
        tb_ass_pgiro(i).cd_gr_cr   ,
        tb_ass_pgiro(i).cd_regione ,
        tb_ass_pgiro(i).pg_comune  ,
        tb_ass_pgiro(i).cd_cori  ,
        tb_ass_pgiro(i).ti_en_per,
        tb_ass_pgiro(i).ti_origine,
        tb_ass_pgiro(i).es_acc,
        tb_ass_pgiro(i).es_ori_acc,
        tb_ass_pgiro(i).cds_acc,
        tb_ass_pgiro(i).pg_acc,
        tb_ass_pgiro(i).uo_acc,
        tb_ass_pgiro(i).es_obb,
        tb_ass_pgiro(i).es_ori_obb,
        tb_ass_pgiro(i).cds_obb,
        tb_ass_pgiro(i).pg_obb,
        tb_ass_pgiro(i).uo_obb,
        tb_ass_pgiro(i).cd_cds_acc_pgiro ,
        tb_ass_pgiro(i).es_acc_pgiro      ,
        tb_ass_pgiro(i).es_orig_acc_pgiro,
        tb_ass_pgiro(i).pg_acc_pgiro     ,
        tb_ass_pgiro(i).uo_acc_pgiro,
        tb_ass_pgiro(i).voce_acc_pgiro  ,
        tb_ass_pgiro(i).cd_cds_obb_pgiro ,
        tb_ass_pgiro(i).es_obb_pgiro      ,
        tb_ass_pgiro(i).es_orig_obb_pgiro,
        tb_ass_pgiro(i).pg_obb_pgiro     ,
        tb_ass_pgiro(i).ti_origine_pgiro ,
        tb_ass_pgiro(i).uo_obb_pgiro,
        tb_ass_pgiro(i).voce_obb_pgiro  ,
        tb_ass_pgiro(i).CD_CDS_ACC_PGIRO_OPP,
        tb_ass_pgiro(i).ES_ACC_PGIRO_OPP,
        tb_ass_pgiro(i).ES_ORIG_ACC_PGIRO_OPP,
        tb_ass_pgiro(i).PG_ACC_PGIRO_OPP,
        tb_ass_pgiro(i).CD_CDS_OBB_PGIRO_OPP,
        tb_ass_pgiro(i).ES_OBB_PGIRO_OPP,
        tb_ass_pgiro(i).ES_ORIG_OBB_PGIRO_OPP,
        tb_ass_pgiro(i).PG_OBB_PGIRO_OPP,
        aTSNow,
        aUser,
        aTSNow,
        aUser,
        1);
  End Loop;
 end;
End;
/
