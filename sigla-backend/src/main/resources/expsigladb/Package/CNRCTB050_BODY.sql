--------------------------------------------------------
--  DDL for Package Body CNRCTB050
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB050" is

 function getStatoAggregato(aEs number, aCdCdr varchar2) return char is
  aStatoAgg char;
 begin
  begin
   select stato into aStatoAgg from pdg_aggregato where
           esercizio = aEs
	   and cd_centro_responsabilita = getCdrAggregatore(aEs, aCdCdr)
	   for update nowait;
   return aStatoAgg;
  exception when NO_DATA_FOUND then
   return null;
  end;
 end;

 function getCdrAggregatore(aEs number, aCdCdr varchar2) return varchar2 is
  aCDR cdr%rowtype;
  aUO unita_organizzativa%rowtype;
  aCdCdrAgg varchar2(30);
 begin
  aCDR:=CNRCTB020.GETCDRVALIDO(aEs,aCdCdr);
  aUO:=CNRCTB020.GETUOVALIDA(aEs,aCDR.cd_unita_organizzativa);
  if aCDR.livello=1 or (aCDR.livello=2 and aUO.cd_tipo_unita = CNRCTB020.TIPO_AREA) then
   aCdCdrAgg:=aCDR.cd_centro_responsabilita;
  else
   aCdCdrAgg:=aCDR.cd_cdr_afferenza;
  end if;
  return aCdCdrAgg;
 end;


 function checkDettScarConfermati (aEs number, aCdCdr varchar2) return char is
   n_discr integer;
 begin
      n_discr:=0;
 	 SELECT COUNT(*) INTO N_DISCR FROM pdg_preventivo_spe_det
 	 WHERE
     	   esercizio = aEs
 	   and cd_centro_responsabilita = aCdCdr
 	   and categoria_dettaglio in ('CAR','SCR')
 	   and stato = STATO_DETT_INDEF;
 	 IF N_DISCR>0 THEN
 	 	return 'N';
 	 END IF;
      n_discr:=0;
 	 SELECT COUNT(*) INTO N_DISCR FROM pdg_preventivo_etr_det
 	 WHERE
 	       esercizio = aEs
 	   and cd_centro_responsabilita = aCdCdr
 	   and categoria_dettaglio in ('CAR','SCR')
 	   and stato = STATO_DETT_INDEF;
 	 IF N_DISCR>0 THEN
 	 	return 'N';
 	 END IF;
 	 return 'Y';
 end;


 function check_discrepanze_insieme_la (aEs number, aCdCdr varchar2) return char is
  n_discr integer;
 begin
	 SELECT COUNT(*) INTO N_DISCR FROM V_DISCREPANZE_INSIEME
	 WHERE V_DISCREPANZE_INSIEME.CD_CENTRO_RESPONSABILITA = aCdCdr
	 AND V_DISCREPANZE_INSIEME.ESERCIZIO = aEs
	 AND ((V_DISCREPANZE_INSIEME.IM_1_ETR - V_DISCREPANZE_INSIEME.IM_1_SPE)<>0 OR
	 	 (V_DISCREPANZE_INSIEME.IM_2_ETR - V_DISCREPANZE_INSIEME.IM_2_SPE)<>0 OR
		 (V_DISCREPANZE_INSIEME.IM_3_ETR - V_DISCREPANZE_INSIEME.IM_3_SPE)<>0);
	 IF N_DISCR>0 THEN
	 	return 'Y';
	 END IF;
	 return 'N';
 end;

 function getStato(aEs number, aCdCdr varchar2) return varchar2 is
  aStato varchar2(5);
 begin
  select stato into aStato from pdg_preventivo where
       esercizio = aEs
   and cd_centro_responsabilita = aCdCdr;
  return aStato;
 end;

 function getStato_PDG_ESERCIZIO(aEs number, aCdCdr varchar2) return varchar2 is
  aStato varchar2(5);
 begin
  select stato into aStato from pdg_ESERCIZIO where
       esercizio = aEs
   and cd_centro_responsabilita = aCdCdr;
  return aStato;
 end;

 procedure lockPdg(aEs number, aCdCdr varchar2) is
  aTemp varchar2(20);
 begin
  begin
   select utuv into aTemp from pdg_preventivo where
        esercizio = aEs
    and cd_centro_responsabilita = aCdCdr
   for update nowait;
  exception when no_data_found then
   IBMERR001.RAISE_ERR_GENERICO('Piano di gestione del CDR '||Nvl(aCdCdr, 'NULLO2')||' non ancora aperto!');
  end;
 end;

-- nuova gestione pdg gestionale

 procedure lockPdg_esercizio(aEs number, aCdCdr varchar2) is
  aTemp varchar2(20);
 begin
  begin
   select utuv into aTemp from pdg_esercizio where
        esercizio = aEs
    and cd_centro_responsabilita = aCdCdr
   for update nowait;
  exception when no_data_found then
   IBMERR001.RAISE_ERR_GENERICO('Piano di gestione del CDR '||Nvl(aCdCdr, 'NULLO')||' non ancora aperto ! (Esercizio)');
  end;
 end;

 function checkAggregatoChiuso(aEs number, aCdCdr varchar2) return char is
  aCDR cdr%rowtype;
  aCdCDRAggregato varchar2(30);
  aStato varchar(5);
 begin
  aCDR:=CNRCTB020.GETCDRVALIDO(aEs, aCdCdr);
  if aCDR.cd_cdr_afferenza is null then
   aCdCDRAggregato:=aCDR.cd_centro_responsabilita;
  else
   aCdCDRAggregato:=aCDR.cd_cdr_afferenza;
  end if;
  begin
   select stato into aStato from pdg_aggregato where
        esercizio = aEs
    and cd_centro_responsabilita = aCdCDRAggregato
   for update nowait;
  exception when NO_DATA_FOUND then
   return 'N';
  end;
  if aStato = STATO_AGGREGATO_FINALE then
   return 'Y';
  end if;
  return 'N';
 end;

 function checkStatoAggregato(aEs number, aCdCdr varchar2,stato varchar2) return char is
  aCDR cdr%rowtype;
  aCdCDRAggregato varchar2(30);
  aStato varchar(5);
 begin
  aCDR:=CNRCTB020.GETCDRVALIDO(aEs, aCdCdr);
  if aCDR.cd_cdr_afferenza is null then
   aCdCDRAggregato:=aCDR.cd_centro_responsabilita;
  else
   aCdCDRAggregato:=aCDR.cd_cdr_afferenza;
  end if;
  begin
   select stato into aStato from pdg_aggregato where
        esercizio = aEs
    and cd_centro_responsabilita = aCdCDRAggregato
   for update nowait;
  exception when NO_DATA_FOUND then
   return 'N';
  end;
  if aStato = stato then
   return 'Y';
  end if;
  return 'N';
 end;

 procedure checkAttualizzScrAltraUo(aEs number, aCdCdrPrimo varchar2) is
  aNum number;
  aPdgAggregato pdg_aggregato%rowtype;
 begin
  begin
   -- Lettura lockante della testata del PDG AGGREGATO
   select * into aPdgAggregato from  pdg_aggregato where
        esercizio = aEs
	and cd_centro_responsabilita = aCdCdrPrimo
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Aggregato dei piani di gestione non trovato per cdr:'||aCdCdrPrimo||' in esercizio '||aEs);
  end;
  for aPdgAgg in (select * from pdg_aggregato_spe_det where
              ESERCIZIO=aEs
          AND CD_CENTRO_RESPONSABILITA=aCdCdrPrimo
          AND TI_AGGREGATO=TI_AGGREGATO_MODIFICATO) loop
   aNum:=0;
   select count(*) into aNum from pdg_aggregato_spe_det where
              ESERCIZIO=aPdgAgg.esercizio
          AND CD_CENTRO_RESPONSABILITA=aPdgAgg.cd_centro_responsabilita
          AND TI_APPARTENENZA=aPdgAgg.TI_APPARTENENZA
          AND TI_GESTIONE=aPdgAgg.TI_GESTIONE
          AND CD_ELEMENTO_VOCE=aPdgAgg.CD_ELEMENTO_VOCE
          AND CD_FUNZIONE=aPdgAgg.CD_FUNZIONE
          AND CD_NATURA=aPdgAgg.CD_NATURA
          AND CD_CDS=aPdgAgg.CD_CDS
          AND TI_AGGREGATO=TI_AGGREGATO_INIZIALE
		  AND (
		      IM_RJ_CCS_SPESE_ODC_ALTRA_UO<>aPdgAgg.IM_RJ_CCS_SPESE_ODC_ALTRA_UO
           OR IM_RL_CCS_SPESE_OGC_ALTRA_UO<>aPdgAgg.IM_RL_CCS_SPESE_OGC_ALTRA_UO
           OR IM_RR_SSC_COSTI_ODC_ALTRA_UO<>aPdgAgg.IM_RR_SSC_COSTI_ODC_ALTRA_UO
           OR IM_RT_SSC_COSTI_OGC_ALTRA_UO<>aPdgAgg.IM_RT_SSC_COSTI_OGC_ALTRA_UO
           OR IM_RAD_A2_SPESE_ODC_ALTRA_UO<>aPdgAgg.IM_RAD_A2_SPESE_ODC_ALTRA_UO
           OR IM_RAF_A2_SPESE_OGC_ALTRA_UO<>aPdgAgg.IM_RAF_A2_SPESE_OGC_ALTRA_UO
           OR IM_RAM_A3_SPESE_ODC_ALTRA_UO<>aPdgAgg.IM_RAM_A3_SPESE_ODC_ALTRA_UO
           OR IM_RAO_A3_SPESE_OGC_ALTRA_UO<>aPdgAgg.IM_RAO_A3_SPESE_OGC_ALTRA_UO
		  );
    if aNum > 0 then
     IBMERR001.RAISE_ERR_GENERICO('Esistono importi di scarico verso altra UO non attualizzati in spese nei piani di gestione del cdr di primo livello in processo:'||aCdCdrPrimo||' in esercizio '||aEs);
	end if;
   end loop;
 end;

 procedure inizializzaAggregatoPDG(aEs number, aCdCdr varchar2, aUser varchar2) is
 Begin
   inizializzaAggregatoPDG(aEs, aCdCdr, aUser , 'N');
 End;
 --
 procedure inizializzaAggregatoPDG(aEs number, aCdCdr varchar2, aUser VARCHAR2, daVariazione CHAR) is
  aCDR cdr%rowtype;
  aPdgAggregato pdg_aggregato%rowtype;
  aPdgAggregatoSpe pdg_aggregato_spe_det%rowtype;
  aPdgAggregatoEtr pdg_aggregato_etr_det%rowtype;
  aTSNow date;
  isAggregatoGiaCreato boolean;
  aDetAggregatoModificato pdg_aggregato_spe_det%rowtype;
  aPdg pdg_preventivo%rowtype;
  err_col VARCHAR2(3000);
 begin
  aTSNow:=sysdate;

  aCDR:=CNRCTB020.GETCDRVALIDO(aEs, aCdCdr);

  -- Se si tratta di CDR ente non viene eseguita alcuna operazione
  if CNRCTB020.ISCDRENTE(aCDR) then
   return;
  end if;

  -- Inserisco la testata dell'aggregato in stato INIZIALE se non giא presente

  isAggregatoGiaCreato:=FALSE;
  begin
   -- Lettura lockante della testata del PDG AGGREGATO
   select * into aPdgAggregato from  pdg_aggregato where
        esercizio = aEs
	and cd_centro_responsabilita = aCDR.cd_centro_responsabilita
   for update nowait;
   isAggregatoGiaCreato:=TRUE;
  exception when NO_DATA_FOUND then
   aPdgAggregato.ESERCIZIO:=aEs;
   aPdgAggregato.CD_CENTRO_RESPONSABILITA:=aCDR.cd_centro_responsabilita;
   aPdgAggregato.STATO:=STATO_AGGREGATO_INIZIALE;
   aPdgAggregato.UTCR:=aUser;
   aPdgAggregato.DACR:=aTSNow;
   aPdgAggregato.UTUV:=aUser;
   aPdgAggregato.DUVA:=aTSNow;
   aPdgAggregato.PG_VER_REC:=1;
   ins_PDG_AGGREGATO (aPdgAggregato);
  end;

  begin
   select * into aPdg from pdg_preventivo where
        esercizio = aEs
    and cd_centro_responsabilita = aCDR.cd_centro_responsabilita
    for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Piano di gestione non trovato per cdr:'||aCDR.cd_centro_responsabilita);
  end;

  -- Annullo una eventuale operazione precedente eliminando tutti i dettagli di aggregazione

  delete from pdg_aggregato_spe_det where
       esercizio = aEs
   and cd_centro_responsabilita = aCdCdr
   and ti_aggregato = TI_AGGREGATO_INIZIALE;

  delete from pdg_aggregato_etr_det where
       esercizio = aEs
   and cd_centro_responsabilita = aCdCdr
   and ti_aggregato = TI_AGGREGATO_INIZIALE;
  /* In caso sto approvando una variazione definitiva aggiorno anche il Modificato */
  if daVariazione = 'Y' Then
    delete from pdg_aggregato_spe_det where
         esercizio = aEs
     and cd_centro_responsabilita = aCdCdr
     and ti_aggregato = TI_AGGREGATO_MODIFICATO;

    delete from pdg_aggregato_etr_det where
         esercizio = aEs
     and cd_centro_responsabilita = aCdCdr
     and ti_aggregato = TI_AGGREGATO_MODIFICATO;
  End If;
  -- Inserisco l'aggregato parte spese ed entrate

  for aVPdgAggregato in (select * from v_dpdg_aggregato_spe_det_spn where
       esercizio = aEs
   and cd_centro_responsabilita = aCDR.cd_centro_responsabilita) loop
     if         isAggregatoGiaCreato
	    -- Il controllo viene effettuato solo se non sono oltre lo stato B (riaperture per proposte di variazione)
	    and (not (aPdg.stato in (CNRCTB070.STATO_PDG_PRECHIUSURA_PER_VAR, CNRCTB070.STATO_PDG_APERTURA_PER_VAR))
	         And DaVariazione = 'N')
	 then
      begin
       select * into aDetAggregatoModificato from pdg_aggregato_spe_det where
              ESERCIZIO=aVPdgAggregato.ESERCIZIO
          AND CD_CENTRO_RESPONSABILITA=aVPdgAggregato.CD_CENTRO_RESPONSABILITA
          AND CD_CDS=aVPdgAggregato.CD_CDS
          AND TI_APPARTENENZA=aVPdgAggregato.TI_APPARTENENZA
          AND TI_GESTIONE=aVPdgAggregato.TI_GESTIONE
          AND CD_ELEMENTO_VOCE=aVPdgAggregato.CD_ELEMENTO_VOCE
          AND CD_FUNZIONE=aVPdgAggregato.CD_FUNZIONE
          AND CD_NATURA=aVPdgAggregato.CD_NATURA
          AND TI_AGGREGATO=TI_AGGREGATO_MODIFICATO;
       if
        aDetAggregatoModificato.IM_RH_CCS_COSTI<aVPdgAggregato.IM_RH_CCS_COSTI or
        aDetAggregatoModificato.IM_RI_CCS_SPESE_ODC<aVPdgAggregato.IM_RI_CCS_SPESE_ODC or
        aDetAggregatoModificato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO or
        aDetAggregatoModificato.IM_RK_CCS_SPESE_OGC<aVPdgAggregato.IM_RK_CCS_SPESE_OGC or
        aDetAggregatoModificato.IM_RL_CCS_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RL_CCS_SPESE_OGC_ALTRA_UO or
        aDetAggregatoModificato.IM_RM_CSS_AMMORTAMENTI<aVPdgAggregato.IM_RM_CSS_AMMORTAMENTI or
        aDetAggregatoModificato.IM_RN_CSS_RIMANENZE<aVPdgAggregato.IM_RN_CSS_RIMANENZE or
        aDetAggregatoModificato.IM_RO_CSS_ALTRI_COSTI<aVPdgAggregato.IM_RO_CSS_ALTRI_COSTI or
        aDetAggregatoModificato.IM_RP_CSS_VERSO_ALTRO_CDR<aVPdgAggregato.IM_RP_CSS_VERSO_ALTRO_CDR or
        aDetAggregatoModificato.IM_RQ_SSC_COSTI_ODC<aVPdgAggregato.IM_RQ_SSC_COSTI_ODC or
        aDetAggregatoModificato.IM_RR_SSC_COSTI_ODC_ALTRA_UO<aVPdgAggregato.IM_RR_SSC_COSTI_ODC_ALTRA_UO or
        aDetAggregatoModificato.IM_RS_SSC_COSTI_OGC<aVPdgAggregato.IM_RS_SSC_COSTI_OGC or
        aDetAggregatoModificato.IM_RT_SSC_COSTI_OGC_ALTRA_UO<aVPdgAggregato.IM_RT_SSC_COSTI_OGC_ALTRA_UO or
      --  aDetAggregatoModificato.IM_RU_SPESE_COSTI_ALTRUI<aVPdgAggregato.IM_RU_SPESE_COSTI_ALTRUI or -- La contrattazione non ט fatta su queste colonne
      --  aDetAggregatoModificato.IM_RV_PAGAMENTI<aVPdgAggregato.IM_RV_PAGAMENTI or
        aDetAggregatoModificato.IM_RAA_A2_COSTI_FINALI<aVPdgAggregato.IM_RAA_A2_COSTI_FINALI or
        aDetAggregatoModificato.IM_RAB_A2_COSTI_ALTRO_CDR<aVPdgAggregato.IM_RAB_A2_COSTI_ALTRO_CDR or
        aDetAggregatoModificato.IM_RAC_A2_SPESE_ODC<aVPdgAggregato.IM_RAC_A2_SPESE_ODC or
        aDetAggregatoModificato.IM_RAD_A2_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RAD_A2_SPESE_ODC_ALTRA_UO or
        aDetAggregatoModificato.IM_RAE_A2_SPESE_OGC<aVPdgAggregato.IM_RAE_A2_SPESE_OGC or
        aDetAggregatoModificato.IM_RAF_A2_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RAF_A2_SPESE_OGC_ALTRA_UO or
      --  aDetAggregatoModificato.IM_RAG_A2_SPESE_COSTI_ALTRUI<aVPdgAggregato.IM_RAG_A2_SPESE_COSTI_ALTRUI or
        aDetAggregatoModificato.IM_RAH_A3_COSTI_FINALI<aVPdgAggregato.IM_RAH_A3_COSTI_FINALI or
        aDetAggregatoModificato.IM_RAI_A3_COSTI_ALTRO_CDR<aVPdgAggregato.IM_RAI_A3_COSTI_ALTRO_CDR or
        aDetAggregatoModificato.IM_RAL_A3_SPESE_ODC<aVPdgAggregato.IM_RAL_A3_SPESE_ODC or
        aDetAggregatoModificato.IM_RAM_A3_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RAM_A3_SPESE_ODC_ALTRA_UO or
        aDetAggregatoModificato.IM_RAN_A3_SPESE_OGC<aVPdgAggregato.IM_RAN_A3_SPESE_OGC or
        aDetAggregatoModificato.IM_RAO_A3_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RAO_A3_SPESE_OGC_ALTRA_UO
      --  aDetAggregatoModificato.IM_RAP_A3_SPESE_COSTI_ALTRUI<aVPdgAggregato.IM_RAP_A3_SPESE_COSTI_ALTRUI
       then

-- inizio

        If aDetAggregatoModificato.IM_RH_CCS_COSTI < aVPdgAggregato.IM_RH_CCS_COSTI Then
          err_col := '\nCosti (H):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RH_CCS_COSTI, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RH_CCS_COSTI, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RH_CCS_COSTI-aVPdgAggregato.IM_RH_CCS_COSTI, '999g999g999g999g999g990d00'))||')';
        End If;

        If aDetAggregatoModificato.IM_RI_CCS_SPESE_ODC<aVPdgAggregato.IM_RI_CCS_SPESE_ODC Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' da contr. propria UO (I):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RH_CCS_COSTI, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RH_CCS_COSTI, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RI_CCS_SPESE_ODC-aVPdgAggregato.IM_RI_CCS_SPESE_ODC, '999g999g999g999g999g990d00'))||')';
        End If;

        If aDetAggregatoModificato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' da contr. altra UO (J):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RJ_CCS_SPESE_ODC_ALTRA_Uo, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO-aVPdgAggregato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RK_CCS_SPESE_OGC<aVPdgAggregato.IM_RK_CCS_SPESE_OGC Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' in essere propria UO (K):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RK_CCS_SPESE_OGC, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RK_CCS_SPESE_OGC, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RK_CCS_SPESE_OGC-aVPdgAggregato.IM_RK_CCS_SPESE_OGC, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RL_CCS_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RL_CCS_SPESE_OGC_ALTRA_UO Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' in essere altra UO (L):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RL_CCS_SPESE_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RL_CCS_SPESE_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RL_CCS_SPESE_OGC_ALTRA_UO-aVPdgAggregato.IM_RL_CCS_SPESE_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RM_CSS_AMMORTAMENTI<aVPdgAggregato.IM_RM_CSS_AMMORTAMENTI Then
          err_col := err_col||'\nAmmortamenti (M):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RM_CSS_AMMORTAMENTI, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RM_CSS_AMMORTAMENTI, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RM_CSS_AMMORTAMENTI-aVPdgAggregato.IM_RM_CSS_AMMORTAMENTI, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RN_CSS_RIMANENZE<aVPdgAggregato.IM_RN_CSS_RIMANENZE Then
          err_col := err_col||'\nRimanenze (N):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RN_CSS_RIMANENZE, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RN_CSS_RIMANENZE, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RN_CSS_RIMANENZE-aVPdgAggregato.IM_RN_CSS_RIMANENZE, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RO_CSS_ALTRI_COSTI<aVPdgAggregato.IM_RO_CSS_ALTRI_COSTI Then
          err_col := err_col||'\nAltri Costi (O):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RO_CSS_ALTRI_COSTI, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RO_CSS_ALTRI_COSTI, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RO_CSS_ALTRI_COSTI-aVPdgAggregato.IM_RO_CSS_ALTRI_COSTI, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RP_CSS_VERSO_ALTRO_CDR<aVPdgAggregato.IM_RP_CSS_VERSO_ALTRO_CDR Then
          err_col := err_col||'\nCosti verso altro CDR (P):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RP_CSS_VERSO_ALTRO_CDR, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RP_CSS_VERSO_ALTRO_CDR, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RP_CSS_VERSO_ALTRO_CDR-aVPdgAggregato.IM_RP_CSS_VERSO_ALTRO_CDR, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RQ_SSC_COSTI_ODC<aVPdgAggregato.IM_RQ_SSC_COSTI_ODC Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' da contr. propria UO (Q):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RQ_SSC_COSTI_ODC, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RQ_SSC_COSTI_ODC, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RQ_SSC_COSTI_ODC-aVPdgAggregato.IM_RQ_SSC_COSTI_ODC, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RR_SSC_COSTI_ODC_ALTRA_UO<aVPdgAggregato.IM_RR_SSC_COSTI_ODC_ALTRA_UO Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' da contr. altra UO (R):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RR_SSC_COSTI_ODC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RR_SSC_COSTI_ODC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RR_SSC_COSTI_ODC_ALTRA_UO-aVPdgAggregato.IM_RR_SSC_COSTI_ODC_ALTRA_UO, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RS_SSC_COSTI_OGC<aVPdgAggregato.IM_RS_SSC_COSTI_OGC Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' in essere propria UO (S):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RS_SSC_COSTI_OGC, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RS_SSC_COSTI_OGC, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RS_SSC_COSTI_OGC-aVPdgAggregato.IM_RS_SSC_COSTI_OGC, '999g999g999g999g999g990d00'))||')';
        End If;

        if aDetAggregatoModificato.IM_RT_SSC_COSTI_OGC_ALTRA_UO<aVPdgAggregato.IM_RT_SSC_COSTI_OGC_ALTRA_UO Then
          err_col := err_col||'\n'||cnrutil.getLabelObbl()||' in essere altra UO (T):   Aggregato '||
          Ltrim(To_Char(aDetAggregatoModificato.IM_RT_SSC_COSTI_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   PdG '||Ltrim(To_Char(aVPdgAggregato.IM_RT_SSC_COSTI_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||
          '   (diff. '||Ltrim(To_Char(aDetAggregatoModificato.IM_RT_SSC_COSTI_OGC_ALTRA_UO-aVPdgAggregato.IM_RT_SSC_COSTI_OGC_ALTRA_UO, '999g999g999g999g999g990d00'))||')';
        End If;
/*
        if aDetAggregatoModificato.IM_RAA_A2_COSTI_FINALI<aVPdgAggregato.IM_RAA_A2_COSTI_FINALI Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAA_A2_COSTI_FINALI||' PdG '||aVPdgAggregato.IM_RAA_A2_COSTI_FINALI ||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAB_A2_COSTI_ALTRO_CDR<aVPdgAggregato.IM_RAB_A2_COSTI_ALTRO_CDR Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAB_A2_COSTI_ALTRO_CDR||' PdG '||aVPdgAggregato.IM_RAB_A2_COSTI_ALTRO_CDR ||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAC_A2_SPESE_ODC<aVPdgAggregato.IM_RAC_A2_SPESE_ODC Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAC_A2_SPESE_ODC||' PdG '||aVPdgAggregato.IM_RAC_A2_SPESE_ODC ||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAD_A2_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RAD_A2_SPESE_ODC_ALTRA_UO Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAD_A2_SPESE_ODC_ALTRA_UO||' PdG '||aVPdgAggregato.IM_RAD_A2_SPESE_ODC_ALTRA_UO ||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAE_A2_SPESE_OGC<aVPdgAggregato.IM_RAE_A2_SPESE_OGC Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAE_A2_SPESE_OGC||' PdG '||aVPdgAggregato.IM_RAE_A2_SPESE_OGC||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAF_A2_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RAF_A2_SPESE_OGC_ALTRA_UO Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAF_A2_SPESE_OGC_ALTRA_UO||' PdG '||aVPdgAggregato.IM_RAF_A2_SPESE_OGC_ALTRA_UO||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAH_A3_COSTI_FINALI<aVPdgAggregato.IM_RAH_A3_COSTI_FINALI Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAH_A3_COSTI_FINALI||' PdG '||aVPdgAggregato.IM_RAH_A3_COSTI_FINALI||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAI_A3_COSTI_ALTRO_CDR<aVPdgAggregato.IM_RAI_A3_COSTI_ALTRO_CDR Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAI_A3_COSTI_ALTRO_CDR||' PdG '||aVPdgAggregato.IM_RAI_A3_COSTI_ALTRO_CDR||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAL_A3_SPESE_ODC<aVPdgAggregato.IM_RAL_A3_SPESE_ODC Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAL_A3_SPESE_ODC||' PdG '||aVPdgAggregato.IM_RAL_A3_SPESE_ODC||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAM_A3_SPESE_ODC_ALTRA_UO<aVPdgAggregato.IM_RAM_A3_SPESE_ODC_ALTRA_UO Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAM_A3_SPESE_ODC_ALTRA_UO||' PdG '||aVPdgAggregato.IM_RAM_A3_SPESE_ODC_ALTRA_UO||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAN_A3_SPESE_OGC<aVPdgAggregato.IM_RAN_A3_SPESE_OGC Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAN_A3_SPESE_OGC||' PdG '||aVPdgAggregato.IM_RAN_A3_SPESE_OGC||'. ';
        End If;

        if aDetAggregatoModificato.IM_RAO_A3_SPESE_OGC_ALTRA_UO<aVPdgAggregato.IM_RAO_A3_SPESE_OGC_ALTRA_UO Then
                err_col := err_col||
aDetAggregatoModificato.IM_RAO_A3_SPESE_OGC_ALTRA_UO||' PdG '||aVPdgAggregato.IM_RAO_A3_SPESE_OGC_ALTRA_UO||'. ';
        End If;
*/
-- fine

        IBMERR001.RAISE_ERR_GENERICO('Il nuovo aggregato supera gli importi stabiliti dal centro su Voce: '||aVPdgAggregato.CD_ELEMENTO_VOCE||' - Funzione: '||aVPdgAggregato.CD_FUNZIONE||' - Natura: '||aVPdgAggregato.CD_NATURA||' cds: '||aVPdgAggregato.CD_CDS||
        '\n\nDettagli:'||err_col);
       end if;
      exception when NO_DATA_FOUND then
       null;
	  end;
     end if;

     aPdgAggregatoSpe.ESERCIZIO:=aVPdgAggregato.ESERCIZIO;
     aPdgAggregatoSpe.CD_CENTRO_RESPONSABILITA:=aVPdgAggregato.CD_CENTRO_RESPONSABILITA;
     aPdgAggregatoSpe.CD_CDS:=aVPdgAggregato.CD_CDS;
     aPdgAggregatoSpe.TI_APPARTENENZA:=aVPdgAggregato.TI_APPARTENENZA;
     aPdgAggregatoSpe.TI_GESTIONE:=aVPdgAggregato.TI_GESTIONE;
     aPdgAggregatoSpe.CD_ELEMENTO_VOCE:=aVPdgAggregato.CD_ELEMENTO_VOCE;
     aPdgAggregatoSpe.CD_FUNZIONE:=aVPdgAggregato.CD_FUNZIONE;
     aPdgAggregatoSpe.CD_NATURA:=aVPdgAggregato.CD_NATURA;

     aPdgAggregatoSpe.TI_AGGREGATO:=TI_AGGREGATO_INIZIALE;

     aPdgAggregatoSpe.IM_RH_CCS_COSTI:=aVPdgAggregato.IM_RH_CCS_COSTI;
     aPdgAggregatoSpe.IM_RI_CCS_SPESE_ODC:=aVPdgAggregato.IM_RI_CCS_SPESE_ODC;
     aPdgAggregatoSpe.IM_RJ_CCS_SPESE_ODC_ALTRA_UO:=aVPdgAggregato.IM_RJ_CCS_SPESE_ODC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RK_CCS_SPESE_OGC:=aVPdgAggregato.IM_RK_CCS_SPESE_OGC;
     aPdgAggregatoSpe.IM_RL_CCS_SPESE_OGC_ALTRA_UO:=aVPdgAggregato.IM_RL_CCS_SPESE_OGC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RM_CSS_AMMORTAMENTI:=aVPdgAggregato.IM_RM_CSS_AMMORTAMENTI;
     aPdgAggregatoSpe.IM_RN_CSS_RIMANENZE:=aVPdgAggregato.IM_RN_CSS_RIMANENZE;
     aPdgAggregatoSpe.IM_RO_CSS_ALTRI_COSTI:=aVPdgAggregato.IM_RO_CSS_ALTRI_COSTI;
     aPdgAggregatoSpe.IM_RP_CSS_VERSO_ALTRO_CDR:=aVPdgAggregato.IM_RP_CSS_VERSO_ALTRO_CDR;
     aPdgAggregatoSpe.IM_RQ_SSC_COSTI_ODC:=aVPdgAggregato.IM_RQ_SSC_COSTI_ODC;
     aPdgAggregatoSpe.IM_RR_SSC_COSTI_ODC_ALTRA_UO:=aVPdgAggregato.IM_RR_SSC_COSTI_ODC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RS_SSC_COSTI_OGC:=aVPdgAggregato.IM_RS_SSC_COSTI_OGC;
     aPdgAggregatoSpe.IM_RT_SSC_COSTI_OGC_ALTRA_UO:=aVPdgAggregato.IM_RT_SSC_COSTI_OGC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RU_SPESE_COSTI_ALTRUI:=aVPdgAggregato.IM_RU_SPESE_COSTI_ALTRUI;
     aPdgAggregatoSpe.IM_RV_PAGAMENTI:=aVPdgAggregato.IM_RV_PAGAMENTI;
     aPdgAggregatoSpe.IM_RAA_A2_COSTI_FINALI:=aVPdgAggregato.IM_RAA_A2_COSTI_FINALI;
     aPdgAggregatoSpe.IM_RAB_A2_COSTI_ALTRO_CDR:=aVPdgAggregato.IM_RAB_A2_COSTI_ALTRO_CDR;
     aPdgAggregatoSpe.IM_RAC_A2_SPESE_ODC:=aVPdgAggregato.IM_RAC_A2_SPESE_ODC;
     aPdgAggregatoSpe.IM_RAD_A2_SPESE_ODC_ALTRA_UO:=aVPdgAggregato.IM_RAD_A2_SPESE_ODC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RAE_A2_SPESE_OGC:=aVPdgAggregato.IM_RAE_A2_SPESE_OGC;
     aPdgAggregatoSpe.IM_RAF_A2_SPESE_OGC_ALTRA_UO:=aVPdgAggregato.IM_RAF_A2_SPESE_OGC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RAG_A2_SPESE_COSTI_ALTRUI:=aVPdgAggregato.IM_RAG_A2_SPESE_COSTI_ALTRUI;
     aPdgAggregatoSpe.IM_RAH_A3_COSTI_FINALI:=aVPdgAggregato.IM_RAH_A3_COSTI_FINALI;
     aPdgAggregatoSpe.IM_RAI_A3_COSTI_ALTRO_CDR:=aVPdgAggregato.IM_RAI_A3_COSTI_ALTRO_CDR;
     aPdgAggregatoSpe.IM_RAL_A3_SPESE_ODC:=aVPdgAggregato.IM_RAL_A3_SPESE_ODC;
     aPdgAggregatoSpe.IM_RAM_A3_SPESE_ODC_ALTRA_UO:=aVPdgAggregato.IM_RAM_A3_SPESE_ODC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RAN_A3_SPESE_OGC:=aVPdgAggregato.IM_RAN_A3_SPESE_OGC;
     aPdgAggregatoSpe.IM_RAO_A3_SPESE_OGC_ALTRA_UO:=aVPdgAggregato.IM_RAO_A3_SPESE_OGC_ALTRA_UO;
     aPdgAggregatoSpe.IM_RAP_A3_SPESE_COSTI_ALTRUI:=aVPdgAggregato.IM_RAP_A3_SPESE_COSTI_ALTRUI;
     aPdgAggregatoSpe.UTCR:=aUser;
     aPdgAggregatoSpe.DACR:=aTSNow;
     aPdgAggregatoSpe.UTUV:=aUser;
     aPdgAggregatoSpe.DUVA:=aTSNow;
     aPdgAggregatoSpe.PG_VER_REC:=1;

	 -- Dettaglio iniziale
     ins_PDG_AGGREGATO_SPE_DET(aPdgAggregatoSpe);
     if daVariazione = 'Y' Then
       aPdgAggregatoSpe.TI_AGGREGATO:=TI_AGGREGATO_MODIFICATO;
       ins_PDG_AGGREGATO_SPE_DET(aPdgAggregatoSpe);
     End If;
	 -- Aggiorna nell'aggregato modificato dal centro le tre colonne degli importi di spesa per costi altrui che non sono contrattabili
	 if
         aPdgAggregatoSpe.IM_RU_SPESE_COSTI_ALTRUI!=0
	  or aPdgAggregatoSpe.IM_RAG_A2_SPESE_COSTI_ALTRUI!=0
	  or aPdgAggregatoSpe.IM_RAP_A3_SPESE_COSTI_ALTRUI!=0
	 then
	  update pdg_aggregato_spe_det set
          IM_RU_SPESE_COSTI_ALTRUI=aPdgAggregatoSpe.IM_RU_SPESE_COSTI_ALTRUI
	     ,IM_RAG_A2_SPESE_COSTI_ALTRUI=aPdgAggregatoSpe.IM_RAG_A2_SPESE_COSTI_ALTRUI
	     ,IM_RAP_A3_SPESE_COSTI_ALTRUI=aPdgAggregatoSpe.IM_RAP_A3_SPESE_COSTI_ALTRUI
		 ,utuv=aUser
		 ,duva=aTSNow
		 ,pg_ver_rec=pg_ver_rec+1
	  where
           esercizio=aPdgAggregatoSpe.ESERCIZIO
       and cd_centro_responsabilita=aPdgAggregatoSpe.CD_CENTRO_RESPONSABILITA
  	   and cd_cds=aPdgAggregatoSpe.CD_CDS
       and ti_appartenenza=aPdgAggregatoSpe.TI_APPARTENENZA
       and ti_gestione=aPdgAggregatoSpe.TI_GESTIONE
       and cd_elemento_voce=aPdgAggregatoSpe.CD_ELEMENTO_VOCE
       and CD_FUNZIONE=aPdgAggregatoSpe.CD_FUNZIONE
       and CD_NATURA=aPdgAggregatoSpe.CD_NATURA
       and TI_AGGREGATO=TI_AGGREGATO_MODIFICATO;
	 end if;

	 -- Dettaglio modificato

	 -- Cerca di inserire le righe di aggregato 'modificato' non presenti per la combinazione di par. di aggregazione che arriva dal
	 -- basso. Se trova giא il dettaglio non effettua operazioni.
	 --
     if isAggregatoGiaCreato then
      aPdgAggregatoSpe.IM_RH_CCS_COSTI:=0;
      aPdgAggregatoSpe.IM_RI_CCS_SPESE_ODC:=0;
      aPdgAggregatoSpe.IM_RJ_CCS_SPESE_ODC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RK_CCS_SPESE_OGC:=0;
      aPdgAggregatoSpe.IM_RL_CCS_SPESE_OGC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RM_CSS_AMMORTAMENTI:=0;
      aPdgAggregatoSpe.IM_RN_CSS_RIMANENZE:=0;
      aPdgAggregatoSpe.IM_RO_CSS_ALTRI_COSTI:=0;
      aPdgAggregatoSpe.IM_RP_CSS_VERSO_ALTRO_CDR:=0;
      aPdgAggregatoSpe.IM_RQ_SSC_COSTI_ODC:=0;
      aPdgAggregatoSpe.IM_RR_SSC_COSTI_ODC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RS_SSC_COSTI_OGC:=0;
      aPdgAggregatoSpe.IM_RT_SSC_COSTI_OGC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RU_SPESE_COSTI_ALTRUI:=0;
      aPdgAggregatoSpe.IM_RV_PAGAMENTI:=0;
      aPdgAggregatoSpe.IM_RAA_A2_COSTI_FINALI:=0;
      aPdgAggregatoSpe.IM_RAB_A2_COSTI_ALTRO_CDR:=0;
      aPdgAggregatoSpe.IM_RAC_A2_SPESE_ODC:=0;
      aPdgAggregatoSpe.IM_RAD_A2_SPESE_ODC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RAE_A2_SPESE_OGC:=0;
      aPdgAggregatoSpe.IM_RAF_A2_SPESE_OGC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RAG_A2_SPESE_COSTI_ALTRUI:=0;
      aPdgAggregatoSpe.IM_RAH_A3_COSTI_FINALI:=0;
      aPdgAggregatoSpe.IM_RAI_A3_COSTI_ALTRO_CDR:=0;
      aPdgAggregatoSpe.IM_RAL_A3_SPESE_ODC:=0;
      aPdgAggregatoSpe.IM_RAM_A3_SPESE_ODC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RAN_A3_SPESE_OGC:=0;
      aPdgAggregatoSpe.IM_RAO_A3_SPESE_OGC_ALTRA_UO:=0;
      aPdgAggregatoSpe.IM_RAP_A3_SPESE_COSTI_ALTRUI:=0;
	 end if;
	 aPdgAggregatoSpe.TI_AGGREGATO:=TI_AGGREGATO_MODIFICATO;
     begin
      ins_PDG_AGGREGATO_SPE_DET(aPdgAggregatoSpe);
     exception when dup_val_on_index then
	  null;
	 end;
  end loop;

  for aVPdgAggregato in (select * from v_dpdg_aggregato_etr_det_spn where
       esercizio = aEs
   and cd_centro_responsabilita = aCDR.cd_centro_responsabilita) loop
     aPdgAggregatoEtr.ESERCIZIO:=aVPdgAggregato.ESERCIZIO;
     aPdgAggregatoEtr.CD_CENTRO_RESPONSABILITA:=aVPdgAggregato.CD_CENTRO_RESPONSABILITA;
     aPdgAggregatoEtr.CD_NATURA:=aVPdgAggregato.CD_NATURA;
     aPdgAggregatoEtr.TI_APPARTENENZA:=aVPdgAggregato.TI_APPARTENENZA;
     aPdgAggregatoEtr.TI_GESTIONE:=aVPdgAggregato.TI_GESTIONE;
     aPdgAggregatoEtr.CD_ELEMENTO_VOCE:=aVPdgAggregato.CD_ELEMENTO_VOCE;

     aPdgAggregatoEtr.TI_AGGREGATO:=TI_AGGREGATO_INIZIALE;

     aPdgAggregatoEtr.IM_RA_RCE:=aVPdgAggregato.IM_RA_RCE;
     aPdgAggregatoEtr.IM_RB_RSE:=aVPdgAggregato.IM_RB_RSE;
     aPdgAggregatoEtr.IM_RC_ESR:=aVPdgAggregato.IM_RC_ESR;
     aPdgAggregatoEtr.IM_RD_A2_RICAVI:=aVPdgAggregato.IM_RD_A2_RICAVI;
     aPdgAggregatoEtr.IM_RE_A2_ENTRATE:=aVPdgAggregato.IM_RE_A2_ENTRATE;
     aPdgAggregatoEtr.IM_RF_A3_RICAVI:=aVPdgAggregato.IM_RF_A3_RICAVI;
     aPdgAggregatoEtr.IM_RG_A3_ENTRATE:=aVPdgAggregato.IM_RG_A3_ENTRATE;
     aPdgAggregatoEtr.UTCR:=aUser;
     aPdgAggregatoEtr.DACR:=aTSNow;
     aPdgAggregatoEtr.UTUV:=aUser;
     aPdgAggregatoEtr.DUVA:=aTSNow;
     aPdgAggregatoEtr.PG_VER_REC:=1;

	 -- Inserimento aggregato iniziale
     ins_PDG_AGGREGATO_ETR_DET(aPdgAggregatoEtr);
     if daVariazione = 'Y' Then
       aPdgAggregatoEtr.TI_AGGREGATO:=TI_AGGREGATO_MODIFICATO;
       ins_PDG_AGGREGATO_ETR_DET(aPdgAggregatoEtr);
     End If;

	 -- Inserimento aggregato modificato
     if isAggregatoGiaCreato then
      aPdgAggregatoEtr.IM_RA_RCE:=0;
      aPdgAggregatoEtr.IM_RB_RSE:=0;
      aPdgAggregatoEtr.IM_RC_ESR:=0;
      aPdgAggregatoEtr.IM_RD_A2_RICAVI:=0;
      aPdgAggregatoEtr.IM_RE_A2_ENTRATE:=0;
      aPdgAggregatoEtr.IM_RF_A3_RICAVI:=0;
      aPdgAggregatoEtr.IM_RG_A3_ENTRATE:=0;
	 end if;

     aPdgAggregatoEtr.TI_AGGREGATO:=TI_AGGREGATO_MODIFICATO;
	 begin
      ins_PDG_AGGREGATO_ETR_DET(aPdgAggregatoEtr);
     exception when dup_val_on_index then
	  null;
	 end;
  end loop;

  -- Aggiornamento utuv, duva e pg_ver_rec della testata del PDG AGGREGATO
  update  pdg_aggregato
  set
   utuv=aUser,
   duva=aTSNow,
   pg_ver_rec=pg_ver_rec+1
  where
       esercizio = aEs
   and cd_centro_responsabilita = aCDR.cd_centro_responsabilita;
 end;

 procedure apriPDG(aEs number, aCdCds varchar2, aUser varchar2) is
  aCDR cdr%rowtype;
  aPdg pdg_preventivo%rowtype;
  aPdgEs pdg_esercizio%rowtype;
  aTSNow date;
  aCDREnte cdr%rowtype;
  recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEs);
  aTSNow:=sysdate;

  aCDREnte:=CNRCTB020.GETCDRENTE;

  -- Lock table pdg_preventivo

  lock table pdg_preventivo in exclusive mode;

  lock table pdg_esercizio in exclusive mode;

  for aCDR in (select a.*, b.cd_tipo_unita from v_cdr_valido a, unita_organizzativa b where
                      a.esercizio = aEs
				  and a.cd_centro_responsabilita <> aCDREnte.cd_centro_responsabilita
				  and a.cd_unita_organizzativa = b.cd_unita_organizzativa
				  and b.cd_unita_padre = aCdCds
			  ) loop
   	If recParametriCNR.fl_nuovo_pdg='N' Then
	   begin
	    aPdg.ESERCIZIO:=aEs;
	    aPdg.CD_CENTRO_RESPONSABILITA:=aCDR.cd_centro_responsabilita;
	    aPdg.STATO:=STATO_PDG_INIZIALE;
	    aPdg.ANNOTAZIONI:='';
	    aPdg.FL_RIBALTATO_SU_AREA:='N';
	    aPdg.DACR:=aTSNow;
	    aPdg.UTCR:=aUser;
	    aPdg.DUVA:=aTSNow;
	    aPdg.UTUV:=aUser;
	    aPdg.PG_VER_REC:=1;
	    ins_PDG_PREVENTIVO (aPdg);
	   exception when dup_val_on_index then
	    null;
	   end;
	End If;
    Begin
    -- per i cdr di primo livello viene creato il record su pdg_esercizio
	If recParametriCNR.fl_nuovo_pdg='N' or aCDR.livello=1 or (
	aCDR.livello=2 and (aCDR.cd_tipo_unita=CNRCTB020.TIPO_SAC or aCDR.cd_tipo_unita=CNRCTB020.TIPO_AREA)) Then
        aPdgEs.ESERCIZIO:=aEs;
        aPdgEs.CD_CENTRO_RESPONSABILITA:=aCDR.cd_centro_responsabilita;
        aPdgEs.STATO:=STATO_PDG2_INIZIALE;
        aPdgEs.DACR:=aTSNow;
        aPdgEs.UTCR:=aUser;
        aPdgEs.DUVA:=aTSNow;
        aPdgEs.UTUV:=aUser;
        aPdgEs.PG_VER_REC:=1;
        ins_PDG_ESERCIZIO (aPdgEs);
    End If;
   Exception When dup_val_on_index then
    null;
   End;
  end loop;
 end;

 function checkQuadRicFig(aEsercizio number, aCdCdr  varchar2) return char is
  aNum number;
 begin
  select count(*) into aNum from V_DPDG_TOT_BIL_RICFIGCDR where
        esercizio = aEsercizio
    and cd_centro_responsabilita = aCdCdr
	and (
	    im_etr_a1 != im_spe_a1
	 or im_etr_a2 != im_spe_a2
	 or im_etr_a3 != im_spe_a3
	);
  if aNum = 0 then
   return 'Y';
  else
   return 'N';
  end if;
 end;

 procedure resetCampiImporto(aDett in out pdg_preventivo_spe_det%rowtype) is
 begin
     aDett.IM_RH_CCS_COSTI:=0;
     aDett.IM_RI_CCS_SPESE_ODC:=0;
     aDett.IM_RJ_CCS_SPESE_ODC_ALTRA_UO:=0;
     aDett.IM_RK_CCS_SPESE_OGC:=0;
     aDett.IM_RL_CCS_SPESE_OGC_ALTRA_UO:=0;
     aDett.IM_RM_CSS_AMMORTAMENTI:=0;
     aDett.IM_RN_CSS_RIMANENZE:=0;
     aDett.IM_RO_CSS_ALTRI_COSTI:=0;
     aDett.IM_RP_CSS_VERSO_ALTRO_CDR:=0;
     aDett.IM_RQ_SSC_COSTI_ODC:=0;
     aDett.IM_RR_SSC_COSTI_ODC_ALTRA_UO:=0;
     aDett.IM_RS_SSC_COSTI_OGC:=0;
     aDett.IM_RT_SSC_COSTI_OGC_ALTRA_UO:=0;
     aDett.IM_RU_SPESE_COSTI_ALTRUI:=0;
     aDett.IM_RV_PAGAMENTI:=0;
     aDett.IM_RAA_A2_COSTI_FINALI:=0;
     aDett.IM_RAB_A2_COSTI_ALTRO_CDR:=0;
     aDett.IM_RAC_A2_SPESE_ODC:=0;
     aDett.IM_RAD_A2_SPESE_ODC_ALTRA_UO:=0;
     aDett.IM_RAE_A2_SPESE_OGC:=0;
     aDett.IM_RAF_A2_SPESE_OGC_ALTRA_UO:=0;
     aDett.IM_RAG_A2_SPESE_COSTI_ALTRUI:=0;
     aDett.IM_RAH_A3_COSTI_FINALI:=0;
     aDett.IM_RAI_A3_COSTI_ALTRO_CDR:=0;
     aDett.IM_RAL_A3_SPESE_ODC:=0;
     aDett.IM_RAM_A3_SPESE_ODC_ALTRA_UO:=0;
     aDett.IM_RAN_A3_SPESE_OGC:=0;
     aDett.IM_RAO_A3_SPESE_OGC_ALTRA_UO:=0;
     aDett.IM_RAP_A3_SPESE_COSTI_ALTRUI:=0;
 end;


 procedure resetCampiImporto(aDett in out pdg_preventivo_etr_det%rowtype) is
 begin
     aDett.IM_RA_RCE:=0;
     aDett.IM_RB_RSE:=0;
     aDett.IM_RC_ESR:=0;
     aDett.IM_RD_A2_RICAVI:=0;
     aDett.IM_RE_A2_ENTRATE:=0;
     aDett.IM_RF_A3_RICAVI:=0;
     aDett.IM_RG_A3_ENTRATE:=0;
 end;

 procedure ins_PDG_PREVENTIVO_ETR_DET (aDest PDG_PREVENTIVO_ETR_DET%rowtype) is
  begin
   insert into PDG_PREVENTIVO_ETR_DET (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,CD_FUNZIONE
    ,CD_NATURA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,PG_ENTRATA
    ,DT_REGISTRAZIONE
    ,DESCRIZIONE
    ,STATO
    ,ORIGINE
    ,CATEGORIA_DETTAGLIO
    ,FL_SOLA_LETTURA
    ,CD_CENTRO_RESPONSABILITA_CLGS
    ,CD_LINEA_ATTIVITA_CLGS
    ,TI_APPARTENENZA_CLGS
    ,TI_GESTIONE_CLGS
    ,CD_ELEMENTO_VOCE_CLGS
    ,PG_SPESA_CLGS
    ,IM_RA_RCE
    ,IM_RB_RSE
    ,IM_RC_ESR
    ,IM_RD_A2_RICAVI
    ,IM_RE_A2_ENTRATE
    ,IM_RF_A3_RICAVI
    ,IM_RG_A3_ENTRATE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,ESERCIZIO_PDG_VARIAZIONE
    ,PG_VARIAZIONE_PDG
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.CD_FUNZIONE
    ,aDest.CD_NATURA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.PG_ENTRATA
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DESCRIZIONE
    ,aDest.STATO
    ,aDest.ORIGINE
    ,aDest.CATEGORIA_DETTAGLIO
    ,aDest.FL_SOLA_LETTURA
    ,aDest.CD_CENTRO_RESPONSABILITA_CLGS
    ,aDest.CD_LINEA_ATTIVITA_CLGS
    ,aDest.TI_APPARTENENZA_CLGS
    ,aDest.TI_GESTIONE_CLGS
    ,aDest.CD_ELEMENTO_VOCE_CLGS
    ,aDest.PG_SPESA_CLGS
    ,aDest.IM_RA_RCE
    ,aDest.IM_RB_RSE
    ,aDest.IM_RC_ESR
    ,aDest.IM_RD_A2_RICAVI
    ,aDest.IM_RE_A2_ENTRATE
    ,aDest.IM_RF_A3_RICAVI
    ,aDest.IM_RG_A3_ENTRATE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.ESERCIZIO_PDG_VARIAZIONE
    ,aDest.PG_VARIAZIONE_PDG
    );
 end;

 procedure ins_PDG_PREVENTIVO_SPE_DET (aDest PDG_PREVENTIVO_SPE_DET%rowtype) is
  begin
   insert into PDG_PREVENTIVO_SPE_DET (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,CD_FUNZIONE
    ,CD_NATURA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,PG_SPESA
    ,DT_REGISTRAZIONE
    ,DESCRIZIONE
    ,STATO
    ,ORIGINE
    ,CATEGORIA_DETTAGLIO
    ,FL_SOLA_LETTURA
    ,CD_CENTRO_RESPONSABILITA_CLGE
    ,CD_LINEA_ATTIVITA_CLGE
    ,TI_APPARTENENZA_CLGE
    ,TI_GESTIONE_CLGE
    ,CD_ELEMENTO_VOCE_CLGE
    ,PG_ENTRATA_CLGE
    ,IM_RH_CCS_COSTI
    ,IM_RI_CCS_SPESE_ODC
    ,IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,IM_RK_CCS_SPESE_OGC
    ,IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,IM_RM_CSS_AMMORTAMENTI
    ,IM_RN_CSS_RIMANENZE
    ,IM_RO_CSS_ALTRI_COSTI
    ,IM_RP_CSS_VERSO_ALTRO_CDR
    ,IM_RQ_SSC_COSTI_ODC
    ,IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,IM_RS_SSC_COSTI_OGC
    ,IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,IM_RU_SPESE_COSTI_ALTRUI
    ,IM_RV_PAGAMENTI
    ,IM_RAA_A2_COSTI_FINALI
    ,IM_RAB_A2_COSTI_ALTRO_CDR
    ,IM_RAC_A2_SPESE_ODC
    ,IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,IM_RAE_A2_SPESE_OGC
    ,IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,IM_RAH_A3_COSTI_FINALI
    ,IM_RAI_A3_COSTI_ALTRO_CDR
    ,IM_RAL_A3_SPESE_ODC
    ,IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,IM_RAN_A3_SPESE_OGC
    ,IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CENTRO_RESPONSABILITA_CLGS
    ,CD_LINEA_ATTIVITA_CLGS
    ,TI_APPARTENENZA_CLGS
    ,TI_GESTIONE_CLGS
    ,CD_ELEMENTO_VOCE_CLGS
    ,PG_SPESA_CLGS
    ,ESERCIZIO_PDG_VARIAZIONE
    ,PG_VARIAZIONE_PDG
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.CD_FUNZIONE
    ,aDest.CD_NATURA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.PG_SPESA
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DESCRIZIONE
    ,aDest.STATO
    ,aDest.ORIGINE
    ,aDest.CATEGORIA_DETTAGLIO
    ,aDest.FL_SOLA_LETTURA
    ,aDest.CD_CENTRO_RESPONSABILITA_CLGE
    ,aDest.CD_LINEA_ATTIVITA_CLGE
    ,aDest.TI_APPARTENENZA_CLGE
    ,aDest.TI_GESTIONE_CLGE
    ,aDest.CD_ELEMENTO_VOCE_CLGE
    ,aDest.PG_ENTRATA_CLGE
    ,aDest.IM_RH_CCS_COSTI
    ,aDest.IM_RI_CCS_SPESE_ODC
    ,aDest.IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RK_CCS_SPESE_OGC
    ,aDest.IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RM_CSS_AMMORTAMENTI
    ,aDest.IM_RN_CSS_RIMANENZE
    ,aDest.IM_RO_CSS_ALTRI_COSTI
    ,aDest.IM_RP_CSS_VERSO_ALTRO_CDR
    ,aDest.IM_RQ_SSC_COSTI_ODC
    ,aDest.IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,aDest.IM_RS_SSC_COSTI_OGC
    ,aDest.IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,aDest.IM_RU_SPESE_COSTI_ALTRUI
    ,aDest.IM_RV_PAGAMENTI
    ,aDest.IM_RAA_A2_COSTI_FINALI
    ,aDest.IM_RAB_A2_COSTI_ALTRO_CDR
    ,aDest.IM_RAC_A2_SPESE_ODC
    ,aDest.IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RAE_A2_SPESE_OGC
    ,aDest.IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,aDest.IM_RAH_A3_COSTI_FINALI
    ,aDest.IM_RAI_A3_COSTI_ALTRO_CDR
    ,aDest.IM_RAL_A3_SPESE_ODC
    ,aDest.IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RAN_A3_SPESE_OGC
    ,aDest.IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CENTRO_RESPONSABILITA_CLGS
    ,aDest.CD_LINEA_ATTIVITA_CLGS
    ,aDest.TI_APPARTENENZA_CLGS
    ,aDest.TI_GESTIONE_CLGS
    ,aDest.CD_ELEMENTO_VOCE_CLGS
    ,aDest.PG_SPESA_CLGS
    ,aDest.ESERCIZIO_PDG_VARIAZIONE
    ,aDest.PG_VARIAZIONE_PDG
    );

 end;


 procedure ins_PDG_PREVENTIVO (aDest PDG_PREVENTIVO%rowtype) is
  begin
   insert into PDG_PREVENTIVO (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,STATO
    ,ANNOTAZIONI
	,FL_RIBALTATO_SU_AREA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.STATO
    ,aDest.ANNOTAZIONI
	,aDest.FL_RIBALTATO_SU_AREA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_PDG_ESERCIZIO (aDest PDG_ESERCIZIO%rowtype) is
  begin
   insert into PDG_ESERCIZIO (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_PDG_AGGREGATO_SPE_DET (aDest PDG_AGGREGATO_SPE_DET%rowtype) is
  begin
   insert into PDG_AGGREGATO_SPE_DET (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
	,CD_CDS
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_FUNZIONE
    ,CD_NATURA
    ,TI_AGGREGATO
    ,IM_RH_CCS_COSTI
    ,IM_RI_CCS_SPESE_ODC
    ,IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,IM_RK_CCS_SPESE_OGC
    ,IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,IM_RM_CSS_AMMORTAMENTI
    ,IM_RN_CSS_RIMANENZE
    ,IM_RO_CSS_ALTRI_COSTI
    ,IM_RP_CSS_VERSO_ALTRO_CDR
    ,IM_RQ_SSC_COSTI_ODC
    ,IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,IM_RS_SSC_COSTI_OGC
    ,IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,IM_RU_SPESE_COSTI_ALTRUI
    ,IM_RV_PAGAMENTI
    ,IM_RAA_A2_COSTI_FINALI
    ,IM_RAB_A2_COSTI_ALTRO_CDR
    ,IM_RAC_A2_SPESE_ODC
    ,IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,IM_RAE_A2_SPESE_OGC
    ,IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,IM_RAH_A3_COSTI_FINALI
    ,IM_RAI_A3_COSTI_ALTRO_CDR
    ,IM_RAL_A3_SPESE_ODC
    ,IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,IM_RAN_A3_SPESE_OGC
    ,IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
	,aDest.CD_CDS
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.CD_FUNZIONE
    ,aDest.CD_NATURA
    ,aDest.TI_AGGREGATO
    ,aDest.IM_RH_CCS_COSTI
    ,aDest.IM_RI_CCS_SPESE_ODC
    ,aDest.IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RK_CCS_SPESE_OGC
    ,aDest.IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RM_CSS_AMMORTAMENTI
    ,aDest.IM_RN_CSS_RIMANENZE
    ,aDest.IM_RO_CSS_ALTRI_COSTI
    ,aDest.IM_RP_CSS_VERSO_ALTRO_CDR
    ,aDest.IM_RQ_SSC_COSTI_ODC
    ,aDest.IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,aDest.IM_RS_SSC_COSTI_OGC
    ,aDest.IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,aDest.IM_RU_SPESE_COSTI_ALTRUI
    ,aDest.IM_RV_PAGAMENTI
    ,aDest.IM_RAA_A2_COSTI_FINALI
    ,aDest.IM_RAB_A2_COSTI_ALTRO_CDR
    ,aDest.IM_RAC_A2_SPESE_ODC
    ,aDest.IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RAE_A2_SPESE_OGC
    ,aDest.IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,aDest.IM_RAH_A3_COSTI_FINALI
    ,aDest.IM_RAI_A3_COSTI_ALTRO_CDR
    ,aDest.IM_RAL_A3_SPESE_ODC
    ,aDest.IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,aDest.IM_RAN_A3_SPESE_OGC
    ,aDest.IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,aDest.IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_PDG_AGGREGATO_ETR_DET (aDest PDG_AGGREGATO_ETR_DET%rowtype) is
  begin
   insert into PDG_AGGREGATO_ETR_DET (
     PG_VER_REC
    ,ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_NATURA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,TI_AGGREGATO
    ,IM_RA_RCE
    ,IM_RB_RSE
    ,IM_RC_ESR
    ,IM_RD_A2_RICAVI
    ,IM_RE_A2_ENTRATE
    ,IM_RF_A3_RICAVI
    ,IM_RG_A3_ENTRATE
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
   ) values (
     aDest.PG_VER_REC
    ,aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_NATURA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.TI_AGGREGATO
    ,aDest.IM_RA_RCE
    ,aDest.IM_RB_RSE
    ,aDest.IM_RC_ESR
    ,aDest.IM_RD_A2_RICAVI
    ,aDest.IM_RE_A2_ENTRATE
    ,aDest.IM_RF_A3_RICAVI
    ,aDest.IM_RG_A3_ENTRATE
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    );
 end;

 procedure ins_ASS_PDG_VARIAZIONE_CDR (aDest ASS_PDG_VARIAZIONE_CDR%rowtype) Is
  begin
   Insert Into ASS_PDG_VARIAZIONE_CDR (
     ESERCIZIO
    ,PG_VARIAZIONE_PDG
    ,CD_CENTRO_RESPONSABILITA
    ,IM_ENTRATA
    ,IM_SPESA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.PG_VARIAZIONE_PDG
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.IM_ENTRATA
    ,aDest.IM_SPESA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_PDG_AGGREGATO (aDest PDG_AGGREGATO%rowtype) is
  begin
   insert into PDG_AGGREGATO (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,STATO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.STATO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

end;
