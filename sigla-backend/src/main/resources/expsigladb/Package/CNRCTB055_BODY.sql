--------------------------------------------------------
--  DDL for Package Body CNRCTB055
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB055" is


/*
 function checkSpeseBilFinCdsCnr(aEs number, aCdCds varchar2, aTiAppartenenza char) return number is
  aVal number(15,2);
  aBilFin bilancio_preventivo%rowtype;
 begin
  if aTiAppartenenza = CNRCTB001.APPARTENENZA_CNR then
   return 0;
  end if;
  begin
   select * into aBilFin from bilancio_preventivo where
        esercizio = aEs
	and cd_cds = aCdCds
	and ti_appartenenza = aTiAppartenenza
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Preventivo finanziario non trovato per CDS:'||aCdCds);
  end;
  begin
   select nvl(im_spese_cnr,0) + nvl(im_cassa_iniziale,0) - nvl(im_spese_cds,0) into aVal from v_prev_fase_spe_cnr_cds where
   esercizio = aEs
   and cd_Cds = aCdCds
   and ti_appartenenza = aTiAppartenenza;
  exception when NO_DATA_FOUND then
   return 0;
  end;
  if aVal < 0 then
   IBMERR001.RAISE_ERR_GENERICO('Il totale delle spese supera quello delle corrispondenti spese nel bilancio finaniziario CNR.');
   -- La differenza è pari a: '||to_char(abs(aVal),'FM999G999G999G999G999D99'));
  end if;
  return aVal;
 end;
*/

/* stani 18.06.2009
 function checkEntrataBilFinCdsCnr(aEs number, aCdCds varchar2, aTiAppartenenza char, aTiGestione char, aCdVoce varchar2) return number is
  aVal number(15,2);
  aBilFin bilancio_preventivo%rowtype;
  aValSaldo number(15,2);
 begin
  if aTiAppartenenza = CNRCTB001.APPARTENENZA_CNR then
   return 0;
  end if;
  if aTiGestione = CNRCTB001.GESTIONE_SPESE then
   return 0;
  end if;
  begin
   select * into aBilFin from bilancio_preventivo where
        esercizio = aEs
	and cd_cds = aCdCds
	and ti_appartenenza = aTiAppartenenza
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Preventivo finanziario non trovato per CDS:'||aCdCds);
  end;
  begin
   select nvl(im_articolo,0) into aVal from v_prev_fase_etr_cnr_cds where
   esercizio = aEs
   and cd_Cds = aCdCds
   and ti_appartenenza = aTiAppartenenza
   and cd_voce = aCdVoce;
  exception when NO_DATA_FOUND then
   aVal:=0;
  end;
  begin
   select nvl(im_stanz_iniziale_a1,0) into aValSaldo from voce_f_saldi_cmp where
        esercizio = aEs
    and ti_appartenenza = aTiAppartenenza
    and ti_gestione = aTiGestione
    and cd_cds = aCdCds
    and cd_voce = aCdVoce
    and ti_competenza_residuo = CNRCTB054.TI_COMPETENZA;
  exception when NO_DATA_FOUND then
   aValSaldo:=0;
  end;
--  Il controllo non è più bloccante
--  if aVal - aValSaldo < 0 then
--   IBMERR001.RAISE_ERR_GENERICO('L''importo iscritto sull''entrata CDS:'||aCdVoce||' supera l''importo corrispondente su articoli di spesa bilancio CNR.');
--  La differenza è pari a: '||to_char(abs(aVal-aValSaldo),'FM999G999G999G999G999D99'));
--  end if;
  return aVal - aValSaldo;
 end;
*/

 function checkSpareggioBilancio(aEs number, aCdCds varchar2, aTiAppartenenza char) return number is
 begin
  return checkSpareggioBilancio(aEs, aCdCds, aTiAppartenenza, 'N');
 end;

 function checkSpareggioBilancio(aEs number, aCdCds varchar2, aTiAppartenenza char, forzaPareggio char) return number is
  aVal number(15,2);
  aBilFin bilancio_preventivo%rowtype;
 begin
  if aTiAppartenenza = CNRCTB001.APPARTENENZA_CNR then
   return 0;
  end if;
  begin
   select * into aBilFin from bilancio_preventivo where
        esercizio = aEs
	and cd_cds = aCdCds
	and ti_appartenenza = aTiAppartenenza
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Preventivo finanziario non trovato per CDS:'||aCdCds);
  end;
  begin
   select nvl(im_entrate,0) - nvl(im_spese,0) into aVal from v_preventivo_pareggio where
   esercizio = aEs
   and cd_Cds = aCdCds
   and ti_appartenenza = aTiAppartenenza;
  exception when NO_DATA_FOUND then
   return 0;
  end;
  if aVal != 0 and forzaPareggio = 'Y' then
   IBMERR001.RAISE_ERR_GENERICO('Bilancio non in pareggio. La differenza entrate spese è pari a: '||to_char(aVal,'FM999G999G999G999G999D99'));
  end if;
  if aVal < 0 and forzaPareggio = 'N' then
   IBMERR001.RAISE_ERR_GENERICO('Il totale delle spese supera quello delle entrate. La differenza è pari a: '||to_char(abs(aVal),'FM999G999G999G999G999D99'));
  end if;
  return aVal;
 end;

 function getPgVariazione(aEs number, aCdCds varchar2) return number is
  aPgVar number(10);
 begin
  begin
   select pg_variazione into aPgVar from var_bilancio where
        cd_cds = aCdCds
	and esercizio = aEs
	and pg_variazione = (select max(pg_variazione) from var_bilancio where
                                cd_cds = aCdCds
                        	and esercizio = aEs
	)
   for update nowait;
   aPgVar:=aPgVar+1;
  exception when NO_DATA_FOUND then
   aPgVar:=1;
  end;
  return aPgVar;
 end;

 -- Funzione interna di calcolo ed esecuzione della ripartizione automatica della entrate
 -- per i-esimo anno (aNumAnno in 1,2,3)
 procedure creaRipartEntrateBase(aEs number, aCdCds varchar2, aCdUo varchar2, aNumAnno number, aUser varchar2, aTSNow date) is
  aVar var_bilancio%rowtype;
  aVarDet var_bilancio_det%rowtype;
  aTotVar number(15,2);
  aOldVoceAgg varchar2(30);
  aVoceF voce_f%rowtype;
  aPgVar number(10);
 begin
  -- Creo una variazione di bilancio con n dettagli corrispondenti ai dati di storno entrate
  -- Determino il numero della variazione
  aPgVar:=getPgVariazione(aEs, aCdCds);
  -- Creo la variazione
  aVar.CD_CDS:=aCdCds;
  aVar.ESERCIZIO:=aEs;
  aVar.ESERCIZIO_IMPORTI:=aEs + aNumAnno - 1;
  aVar.TI_APPARTENENZA:=CNRCTB001.APPARTENENZA_CNR;
  aVar.PG_VARIAZIONE:=aPgVar;
  aVar.DS_VARIAZIONE:='UO:'||aCdUo||'-VARIAZIONE AUTOMATICA PER RIPARTIZIONE ENTRATE CNR. IMPORTI ANNO '||(aEs + aNumAnno - 1);
  aVar.DS_DELIBERA:=null;
  aVar.TI_VARIAZIONE:=CNRCTB054.TI_VAR_STORNO_E;
  aVar.CD_CAUSALE_VAR_BILANCIO:=CNRCTB054.CAU_VAR_RIP_AUT_E;
  aVar.STATO:=CNRCTB054.STATO_VARIAZIONE_PROVVISORIA;
  aVar.DUVA:=aTSNow;
  aVar.UTUV:=aUser;
  aVar.DACR:=aTSNow;
  aVar.UTCR:=aUser;
  aVar.PG_VER_REC:=1;
  CNRCTB054.INS_VAR_BILANCIO(aVar);
  aTotVar:=0;
  aOldVoceAgg:=null;
  for aUOData in (
   select * from v_pdg_entrate_uo where
          esercizio = aEs
	  and cd_uo_aggregante = aCdUo
	  order by cd_voce_agg
  ) loop
   if aOldVoceAgg is null then
    aOldVoceAgg:=aUOData.cd_voce_agg;
   end if;
   if aOldVoceAgg <> aUOData.cd_voce_agg then
     begin
 	  select * into aVoceF from voce_f where
	       esercizio = aEs
	   and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
	   and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
	   and cd_voce = aOldVoceAgg;
     exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('Articolo finanziario di entrata CNR non trovato:'||aOldVoceAgg);
	 end;
	 aVarDet:=null;
     aVarDet.CD_CDS:=aVar.cd_cds;
     aVarDet.ESERCIZIO:=aVar.esercizio;
     aVarDet.TI_APPARTENENZA:=aVar.ti_appartenenza;
     aVarDet.PG_VARIAZIONE:=aVar.pg_variazione;
     aVarDet.TI_GESTIONE:=CNRCTB001.GESTIONE_ENTRATE;
     aVarDet.CD_VOCE:=aOldVoceAgg;
     aVarDet.IM_VARIAZIONE:=0-aTotVar;
     aVarDet.DUVA:=aTSNow;
     aVarDet.UTUV:=aUser;
     aVarDet.DACR:=aTSNow;
     aVarDet.UTCR:=aUser;
     aVarDet.PG_VER_REC:=1;
     CNRCTB054.INS_VAR_BILANCIO_DET(aVarDet);
     aOldVoceAgg:=aUOData.cd_voce_agg;
     aTotVar:=0;
   end if;
   if aNumAnno = 1 then
    aTotVar:=aTotVar + aUOData.im_stanz_iniziale_a1;
    aVarDet.IM_VARIAZIONE:=aUOData.im_stanz_iniziale_a1;
   elsif aNumAnno = 2 then
    aTotVar:=aTotVar + aUOData.im_stanz_iniziale_a2;
    aVarDet.IM_VARIAZIONE:=aUOData.im_stanz_iniziale_a2;
   else
    aTotVar:=aTotVar + aUOData.im_stanz_iniziale_a3;
    aVarDet.IM_VARIAZIONE:=aUOData.im_stanz_iniziale_a3;
   end if;
   aVarDet.CD_CDS:=aVar.cd_cds;
   aVarDet.ESERCIZIO:=aVar.esercizio;
   aVarDet.TI_APPARTENENZA:=aVar.ti_appartenenza;
   aVarDet.PG_VARIAZIONE:=aVar.pg_variazione;
   aVarDet.TI_GESTIONE:=CNRCTB001.GESTIONE_ENTRATE;
   aVarDet.CD_VOCE:=aUOData.cd_voce;
   aVarDet.DUVA:=aTSNow;
   aVarDet.UTUV:=aUser;
   aVarDet.DACR:=aTSNow;
   aVarDet.UTCR:=aUser;
   aVarDet.PG_VER_REC:=1;
   CNRCTB054.INS_VAR_BILANCIO_DET(aVarDet);
  end loop;
  if aOldVoceAgg is not null then
   begin
    select * into aVoceF from voce_f where
         esercizio = aEs
     and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
     and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
     and cd_voce = aOldVoceAgg;
    exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Articolo finanziario di entrata CNR non trovato:'||aOldVoceAgg);
   end;
   aVarDet.CD_CDS:=aVar.cd_cds;
   aVarDet.ESERCIZIO:=aVar.esercizio;
   aVarDet.TI_APPARTENENZA:=aVar.ti_appartenenza;
   aVarDet.PG_VARIAZIONE:=aVar.pg_variazione;
   aVarDet.TI_GESTIONE:=CNRCTB001.GESTIONE_ENTRATE;
   aVarDet.CD_VOCE:=aVoceF.cd_voce;
   aVarDet.IM_VARIAZIONE:=0-aTotVar;
   aVarDet.DUVA:=aTSNow;
   aVarDet.UTUV:=aUser;
   aVarDet.DACR:=aTSNow;
   aVarDet.UTCR:=aUser;
   aVarDet.PG_VER_REC:=1;
   CNRCTB054.INS_VAR_BILANCIO_DET(aVarDet);
   checkVariazioneBilancio(aVar);
   esitaVariazioneBilancio(aVar);
  end if;
 end;

 procedure creaRipartEntrate(aEs number,aCdCdr varchar2, aUser varchar2) is
  aTSNow date;
  aUOCDSENTE unita_organizzativa%rowtype;
  aBil bilancio_preventivo%rowtype;
  aCdUo varchar2(30);
  aCdr cdr%rowtype;
  aUO unita_organizzativa%rowtype;
  aPDG pdg_preventivo%rowtype;
  aVar var_bilancio%rowtype;
 begin
  aCdr:=CNRCTB020.GETCDRVALIDO(aEs,aCdCdr);
  aUO:=CNRCTB020.GETUOVALIDA(aEs,aCdr.cd_unita_organizzativa);

  -- Controllo che il CDR sia primo livello o responsabile di area (quelli della contrattazione)
  if not (aCdr.livello = 1 or (aCdr.livello = 2 and aUO.cd_tipo_unita = CNRCTB020.TIPO_AREA)) then
   return;
  end if;

  aCdUo:=aCdr.cd_unita_organizzativa;

  -- calcolo del timestamp corrente
  aTSNow:=sysdate;

  -- Lock del piano di gestione

  select * into aPDG from pdg_preventivo where
       esercizio = aEs
   and cd_centro_responsabilita = aCdCdr
  for update nowait;

  if aPDG.stato <> CNRCTB050.STATO_PDG_FINALE then
   IBMERR001.RAISE_ERR_GENERICO('Piano di gestione del CDR:'||aCdCdr||' non è in stato di chiusura definitiva');
  end if;

  -- Leggo l'UO CDS ENTE
  select * into aUOCDSENTE from unita_organizzativa where
       cd_tipo_unita = CNRCTB020.TIPO_ENTE
   and fl_uo_cds  ='Y';
  aUOCDSENTE:=CNRCTB020.GETUOVALIDA(aEs,aUOCDSENTE.cd_unita_organizzativa);

  -- Leggo con lock il bilancio finanziario dell'ente

  begin
   select * into aBil from bilancio_preventivo where
        esercizio = aEs
    and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
    and cd_cds = aUOCDSENTE.cd_unita_padre
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Bilancio finanziario dell''ente non trovato');
  end;

  begin
   select * into aVar from var_bilancio where
        esercizio = aEs
    and substr(ds_variazione,4,length(aCdUo)) = aCdUo
    and cd_cds = aUOCDSENTE.cd_unita_padre
    and CD_CAUSALE_VAR_BILANCIO=CNRCTB054.CAU_VAR_RIP_AUT_E;
   IBMERR001.RAISE_ERR_GENERICO('Ripartizione automatica delle entrate CNR già effettuata');
  exception
  when NO_DATA_FOUND then
   null;
  when TOO_MANY_ROWS then
   IBMERR001.RAISE_ERR_GENERICO('Ripartizione automatica delle entrate CNR già effettuata');
  end;

  if aBil.stato <> CNRCTB054.STATO_PREVENTIVO_APPROVATO then
   IBMERR001.RAISE_ERR_GENERICO('Bilancio finanziario non ancora approvato');
  end if;

  -- Creo una variazione di bilancio con n dettagli corrispondenti ai dati di storno entrate
  -- per anno 1
  creaRipartEntrateBase(aEs, aUOCDSENTE.cd_unita_padre, aCdUo, 1, aUser, aTSNow);
  -- per anno 2
  creaRipartEntrateBase(aEs, aUOCDSENTE.cd_unita_padre, aCdUo, 2, aUser, aTSNow);
  -- per anno 3
  creaRipartEntrateBase(aEs, aUOCDSENTE.cd_unita_padre, aCdUo, 3, aUser, aTSNow);
 end;


 -- Ritorna l'assestato per variazioni sui tre anni
 function getAssestato(aSaldo voce_f_saldi_cdr_linea%rowtype, aVarBilancio var_bilancio%rowtype) return number is
 Begin
  If aVarBilancio.esercizio_importi < aVarBilancio.esercizio Then -- residui
   return aSaldo.IM_OBBL_RES_PRO + aSaldo.VAR_PIU_OBBL_RES_PRO - aSaldo.VAR_MENO_OBBL_RES_PRO;
  Elsif aVarBilancio.esercizio_importi = aVarBilancio.esercizio Then -- competenza
   return aSaldo.im_stanz_iniziale_a1 + aSaldo.variazioni_piu - aSaldo.variazioni_meno;
  Elsif aVarBilancio.esercizio_importi = aVarBilancio.esercizio + 1 Then -- anni pluriennali NON GESTITI
   return aSaldo.im_stanz_iniziale_a2;
  Elsif aVarBilancio.esercizio_importi = aVarBilancio.esercizio + 2 Then -- anni pluriennali NON GESTITI
   return aSaldo.im_stanz_iniziale_a3;
  End if;
  IBMERR001.RAISE_ERR_GENERICO('Anno della variazione: '||aVarBilancio.esercizio_importi||' non compatibile con esercizio di scrivania: '||aVarBilancio.esercizio);
 End;

Procedure checkVariazioneBilancio(aVarBilancio var_bilancio%rowtype) is
  aSaldo        voce_f_saldi_cdr_linea%rowtype;
  aBil          bilancio_preventivo%rowtype;
  aVoceF        voce_f%rowtype;
  ti_comp_res   CHAR(1);
  fl_2006       CHAR(1);
	anewSaldo        voce_f_saldi_cdr_linea%rowtype;
	aCd_elemento_voce VARCHAR2(30);
Begin

-- VARIAZIONI AL BILANCIO DI SERVIZIO SOLO PER TI_APPARTENENZA = 'C'

  If aVarBilancio.ti_appartenenza != CNRCTB001.APPARTENENZA_CNR Then
    IBMERR001.RAISE_ERR_GENERICO('Le variazioni al Bilancio di Servizio sono consentite solo per voci di appartenenza CNR.');
  End If;


/* ATTENZIONE !!!! IL CONTROLLO CHE IL BILANCIO_PREVENTIVO DEL 999 DEVE ESSERE APPROVATO PER POTER FARE VARIAZIONI A COMPETENZA
                   VIENE FATTO SOLO FINO AL 2005 */

  Begin
    Select  Nvl(FL_REGOLAMENTO_2006, 'N')
    Into    fl_2006
    From    parametri_cnr
    Where   esercizio = aVarBilancio.ESERCIZIO;
  Exception
    When No_Data_Found Then
     IBMERR001.RAISE_ERR_GENERICO('Attenzione !!! Non esistono i Parametri CNR per l''Esercizio '||aVarBilancio.ESERCIZIO);
  End;

  If FL_2006 = 'N' Then
     Select * into aBil
     From bilancio_preventivo
     Where esercizio = aVarBilancio.esercizio
       and cd_cds = aVarBilancio.cd_cds
       and ti_appartenenza = aVarBilancio.ti_appartenenza
     For Update Nowait;

     If aBil.stato != CNRCTB054.STATO_PREVENTIVO_APPROVATO And aVarBilancio.ESERCIZIO = aVarBilancio.ESERCIZIO_IMPORTI then
      IBMERR001.RAISE_ERR_GENERICO('Nell''esercizio '||aVarBilancio.esercizio||
'    il bilancio preventivo del CDS '||aVarBilancio.cd_cds||
'    non è stato ancora approvato ! (è in stato "'||aBil.stato||'"). Sono consentite solo Variazioni ai Residui.');
     End If;
  End If;

-- LOOP SUI DETTAGLI

  For aDet in (Select * From var_bilancio_det
               Where esercizio       = aVarBilancio.esercizio And
                     cd_cds          = aVarBilancio.cd_cds And
                     ti_appartenenza = aVarBilancio.ti_appartenenza And
                     pg_variazione   = aVarBilancio.pg_variazione) Loop

-- PER OGNI DETTAGLIO RECUPERO IL SALDO E LA VOCE_F

    Begin
     Select * into aSaldo
     From   voce_f_saldi_cdr_linea
     Where  esercizio       = aDet.esercizio And
            esercizio_res   = aVarBilancio.ESERCIZIO_IMPORTI And
            ti_appartenenza = aDet.ti_appartenenza And
            ti_gestione     = aDet.ti_gestione And
            cd_voce         = aDet.cd_voce
     For Update nowait;
    Exception
     When No_Data_Found Then
						Begin
							SELECT CD_ELEMENTO_VOCE INTO aCd_elemento_voce
							FROM   VOCE_F
							WHERE  ESERCIZIO = aDet.ESERCIZIO
							   AND Ti_appartenenza = aDet.TI_APPARTENENZA
							   AND Ti_gestione = aDet.TI_GESTIONE
							   AND Cd_voce = aDet.CD_VOCE;
						Exception
						When No_Data_Found Then
						     IBMERR001.RAISE_ERR_GENERICO('Per l''Esercizio '||aDet.ESERCIZIO||' non esiste la Voce '||
								aDet.ESERCIZIO||'/'||aDet.TI_APPARTENENZA||'/'||aDet.TI_GESTIONE||'/'||aDet.CD_VOCE);
						End;
					aNewSaldo.ESERCIZIO                      := aDet.ESERCIZIO;
					aNewSaldo.ESERCIZIO_RES                  := aVarBilancio.ESERCIZIO_IMPORTI;
					aNewSaldo.CD_CENTRO_RESPONSABILITA       := cnrctb020.getcdcdrente;
					aNewSaldo.CD_LINEA_ATTIVITA              := CNRCTB015.getVal02PerChiave(0, 'LINEA_ATTIVITA_SPECIALE',  'LINEA_ATTIVITA_SPESA_ENTE');
					aNewSaldo.TI_APPARTENENZA                := aDet.TI_APPARTENENZA;
					aNewSaldo.TI_GESTIONE                    := aDet.TI_GESTIONE;
					aNewSaldo.CD_VOCE                        := aDet.CD_VOCE;
					aNewSaldo.IM_STANZ_INIZIALE_A1           := 0;
					aNewSaldo.IM_STANZ_INIZIALE_A2           := 0;
					aNewSaldo.IM_STANZ_INIZIALE_A3           := 0;
					aNewSaldo.VARIAZIONI_PIU                 := 0;
					aNewSaldo.VARIAZIONI_MENO                := 0;
					aNewSaldo.IM_STANZ_INIZIALE_CASSA        := 0;
					aNewSaldo.VARIAZIONI_PIU_CASSA           := 0;
					aNewSaldo.VARIAZIONI_MENO_CASSA          := 0;
					aNewSaldo.IM_OBBL_ACC_COMP               := 0;
					aNewSaldo.IM_STANZ_RES_IMPROPRIO         := 0;
					aNewSaldo.VAR_PIU_STANZ_RES_IMP          := 0;
					aNewSaldo.VAR_MENO_STANZ_RES_IMP         := 0;
					aNewSaldo.IM_OBBL_RES_IMP                := 0;
					aNewSaldo.VAR_PIU_OBBL_RES_IMP           := 0;
					aNewSaldo.VAR_MENO_OBBL_RES_IMP          := 0;
					aNewSaldo.IM_OBBL_RES_PRO                := 0;
					aNewSaldo.VAR_PIU_OBBL_RES_PRO           := 0;
					aNewSaldo.VAR_MENO_OBBL_RES_PRO          := 0;
					aNewSaldo.IM_MANDATI_REVERSALI_PRO       := 0;
					aNewSaldo.IM_MANDATI_REVERSALI_IMP       := 0;
					aNewSaldo.IM_PAGAMENTI_INCASSI           := 0;
					aNewSaldo.CD_ELEMENTO_VOCE               := aCd_elemento_voce;
					aNewSaldo.DACR                           := Sysdate;
					aNewSaldo.UTCR                           := aDet.UTUV;
					aNewSaldo.DUVA                           := Sysdate;
					aNewSaldo.UTUV                           := aDet.UTUV;
					aNewSaldo.PG_VER_REC                     := 1;

					cnrctb054.ins_VOCE_F_SALDI_CDR_LINEA (aNewSaldo);
     			aSaldo := aNewSaldo;
       -- IBMERR001.RAISE_ERR_GENERICO('Non esiste la riga di saldo per la seguente combinazione contabile: Es. '||aDet.esercizio||', Es. Residuo '||
       -- aVarBilancio.ESERCIZIO_IMPORTI||', Voce '||aDet.cd_voce);
    End;
     Select *
     Into   aVoceF
     From   voce_f
     Where  esercizio       = aDet.esercizio And
            ti_appartenenza = aDet.ti_appartenenza And
            ti_gestione     = aDet.ti_gestione And
            cd_voce         = aDet.cd_voce;

-- SE LA VOCE E' DI PARTE 2 CONTROLLA CHE SIA IN COMPETENZA

     If aVarBilancio.esercizio > aVarBilancio.esercizio_importi And aVoceF.cd_parte = CNRCTB001.PARTE2 Then
       IBMERR001.RAISE_ERR_GENERICO('Le variazioni al Bilancio di Servizio su voci di Spesa di Parte 2 sono consentite solo a Competenza.');
     End If;

-- SE PARTE 1 CONTROLLA CHE LA VARIAZIONE NON RENDA NEGATIVO LO (STANZIATO A COMPETENZA ASSESTATO - IL PAGATO)

     If aSaldo.ti_gestione = CNRCTB001.GESTIONE_SPESE Then -- ente 999

        If aVarBilancio.esercizio = aVarBilancio.esercizio_importi Then  -- competenza

            If Nvl(aSaldo.im_stanz_iniziale_a1, 0) + Nvl(aSaldo.variazioni_piu, 0) - Nvl(aSaldo.variazioni_meno, 0) + Nvl(aDet.im_variazione, 0) -
               Nvl(aSaldo.IM_MANDATI_REVERSALI_PRO, 0) < 0 Then
    	         IBMERR001.RAISE_ERR_GENERICO('(1) La variazione produce o non sana una disponibilità negativa sulla Voce: '||aSaldo.cd_voce);
            End If;

        Elsif aVarBilancio.esercizio > aVarBilancio.esercizio_importi Then  -- residuo

            If Nvl(aSaldo.IM_OBBL_RES_PRO, 0) + Nvl(aSaldo.VAR_PIU_OBBL_RES_PRO, 0) - Nvl(aSaldo.VAR_MENO_OBBL_RES_PRO, 0) +
               Nvl(aDet.im_variazione, 0) - Nvl(aSaldo.IM_MANDATI_REVERSALI_PRO, 0) < 0 Then
    	         IBMERR001.RAISE_ERR_GENERICO('(1) La variazione produce o non sana una disponibilità negativa sulla Voce: '||aSaldo.cd_voce);
            End If;

        End If; -- competenza o residuo

     End If; -- controllo di disponibilità solo per la spesa

     If getAssestato(aSaldo, aVarBilancio) + aDet.im_variazione < 0 Then
      IBMERR001.RAISE_ERR_GENERICO('La variazione produce uno stanziamento assestato negativo sulla Voce '||aSaldo.cd_voce||' per l''esercizio '||aVarBilancio.esercizio||' ed esercizio residuo '||aVarBilancio.esercizio_importi||'.');
     End if;

End loop;

End;

 procedure checkVariazioneBilancio(aEsercizio number, aCodiceCds varchar2, aAppartenenza char, aPgVariazione number) is
  aVarBilancio var_bilancio%rowtype;
  aBil bilancio_preventivo%rowtype;
 begin
  select * into aVarBilancio from var_bilancio where
        esercizio = aEsercizio
    and cd_cds = aCodiceCds
    and ti_appartenenza = aAppartenenza
    and pg_variazione = aPgVariazione
    for update nowait;
  checkVariazioneBilancio(aVarBilancio);
 end;

 procedure esitaVariazioneBilancio(aEsercizio number, aCodiceCds varchar2, aAppartenenza varchar2, aPgVariazione number, aUser varchar2) is
  aVarBilancio var_bilancio%rowtype;
  gAppartenenza CHAR(1) := TRIM(aAppartenenza);
 begin
  begin
   select * into aVarBilancio from var_bilancio where
        esercizio = aEsercizio
    and cd_cds = aCodiceCds
    and ti_appartenenza = gAppartenenza
    and pg_variazione = aPgVariazione
	and stato = CNRCTB054.STATO_VARIAZIONE_PROVVISORIA
    for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('La variazione di bilancio è già definitiva');
  end;
  esitaVariazioneBilancio(aVarBilancio);
 end;

Procedure esitaVariazioneBilancio(aVarBilancio var_bilancio%rowtype) is
  aSaldo         voce_f_saldi_cmp%rowtype;
  aSaldoCdrLinea VOCE_F_SALDI_CDR_LINEA%rowtype;
  aBil           bilancio_preventivo%rowtype;
  aTmpNum        number(15,2);
  aElementoVoce  ELEMENTO_VOCE%RowType;

Begin

checkVariazioneBilancio(aVarBilancio);

For aDet in (select * from var_bilancio_det
             Where     esercizio = aVarBilancio.esercizio
                   and cd_cds = aVarBilancio.cd_cds
                   and ti_appartenenza = aVarBilancio.ti_appartenenza
                   and pg_variazione = aVarBilancio.pg_variazione) Loop

  Begin
     Select elemento_voce.* into aElementoVoce
     from voce_f, elemento_voce
     where voce_f.esercizio = aDet.esercizio
     and   voce_f.ti_appartenenza = aDet.ti_appartenenza
     and   voce_f.ti_gestione = aDet.ti_gestione
     and   voce_f.cd_voce = aDet.cd_voce
     and   voce_f.esercizio = elemento_voce.esercizio
     and   voce_f.ti_appartenenza = elemento_voce.ti_appartenenza
     and   voce_f.ti_gestione = elemento_voce.ti_gestione
     and   voce_f.cd_elemento_voce = elemento_voce.cd_elemento_voce;
  Exception
     When no_data_found Then
        IBMERR001.RAISE_ERR_GENERICO('Voce di Bilancio '||aDet.esercizio||'/'||aDet.ti_appartenenza||'/'||
             aDet.ti_gestione||'/'||aDet.cd_voce||', presente nella variazione, inesistente !');
  End;

  If aVarBilancio.esercizio_importi < aVarBilancio.esercizio then -- Variazione su anno RESIDUO NEW !!!!

    If aDet.im_variazione >= 0 then
       update voce_f_saldi_cmp
       Set  variazioni_piu = variazioni_piu + aDet.im_variazione,
        utuv = aVarBilancio.utuv,
        duva = aVarBilancio.duva,
        pg_ver_rec = pg_ver_rec + 1
       Where    esercizio = aVarBilancio.esercizio
            and cd_cds = aDet.cd_cds
            and ti_appartenenza = aDet.ti_appartenenza
            and ti_gestione = aDet.ti_gestione
	    and cd_voce = aDet.cd_voce
	    and ti_competenza_residuo = CNRCTB054.TI_RESIDUI;
    Else
      update voce_f_saldi_cmp
      Set variazioni_meno = variazioni_meno + abs(aDet.im_variazione),
        utuv = aVarBilancio.utuv,
        duva = aVarBilancio.duva,
        pg_ver_rec = pg_ver_rec + 1
      Where     esercizio = aVarBilancio.esercizio
            and cd_cds = aDet.cd_cds
            and ti_appartenenza = aDet.ti_appartenenza
            and ti_gestione = aDet.ti_gestione
	    and cd_voce = aDet.cd_voce
	    and ti_competenza_residuo = CNRCTB054.TI_RESIDUI;
    End if;

    If aDet.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR And aDet.ti_gestione = CNRCTB001.GESTIONE_SPESE And
       aElementoVoce.fl_voce_fondo = 'N'
        Then
	  -- Aggiorna l'impegno RESIDUO su capitolo NEW !!!!
Dbms_Output.PUT_LINE (' det f agg imp '||aDet.esercizio||' '||aVarBilancio.ESERCIZIO_IMPORTI||' '||aDet.cd_voce||' '||aDet.im_variazione);
	  CNRCTB030.AGGIORNAIMPEGNOCAPITOLOVAR(aDet.esercizio, aVarBilancio.ESERCIZIO_IMPORTI, aDet.cd_voce, aDet.im_variazione, aDet.utuv);
    End if;

  Elsif aVarBilancio.esercizio_importi = aVarBilancio.esercizio then -- Variazione su anno 1

    If aDet.im_variazione >= 0 then
       update voce_f_saldi_cmp
       Set  variazioni_piu = variazioni_piu + aDet.im_variazione,
        utuv = aVarBilancio.utuv,
        duva = aVarBilancio.duva,
        pg_ver_rec = pg_ver_rec + 1
       Where    esercizio = aVarBilancio.esercizio
            and cd_cds = aDet.cd_cds
            and ti_appartenenza = aDet.ti_appartenenza
            and ti_gestione = aDet.ti_gestione
	    and cd_voce = aDet.cd_voce
	    and ti_competenza_residuo = CNRCTB054.TI_COMPETENZA;
    Else
      update voce_f_saldi_cmp
      Set variazioni_meno = variazioni_meno + abs(aDet.im_variazione),
        utuv = aVarBilancio.utuv,
        duva = aVarBilancio.duva,
        pg_ver_rec = pg_ver_rec + 1
      Where     esercizio = aVarBilancio.esercizio
            and cd_cds = aDet.cd_cds
            and ti_appartenenza = aDet.ti_appartenenza
            and ti_gestione = aDet.ti_gestione
	    and cd_voce = aDet.cd_voce
	    and ti_competenza_residuo = CNRCTB054.TI_COMPETENZA;
    End if;

  -- Aggiorno VOCE_F_SALDI_CDR_LINEA M.S. 19/12/2005

  Declare
    cdr_ente   cdr%rowtype;

  Begin

  cdr_ente := CNRCTB020.getCDREnte;

  aSaldoCdrLinea.ESERCIZIO := aVarBilancio.esercizio;
  aSaldoCdrLinea.ESERCIZIO_RES := aVarBilancio.esercizio;
  aSaldoCdrLinea.CD_CENTRO_RESPONSABILITA := cdr_ente.CD_CENTRO_RESPONSABILITA;
  aSaldoCdrLinea.CD_LINEA_ATTIVITA := CNRCTB015.getVal02PerChiave(0, 'LINEA_ATTIVITA_SPECIALE',  'LINEA_ATTIVITA_SPESA_ENTE');
  aSaldoCdrLinea.TI_APPARTENENZA := aDet.ti_appartenenza;
  aSaldoCdrLinea.TI_GESTIONE := aDet.ti_gestione;
  aSaldoCdrLinea.CD_VOCE := aDet.cd_voce;
  aSaldoCdrLinea.UTUV := aDet.UTUV;

  CNRCTB054.RESET_IMPORTI_SALDI (aSaldoCdrLinea);

  If aDet.im_variazione > 0 Then
    aSaldocdrlinea.VARIAZIONI_PIU := Abs(aDet.im_variazione);
  Elsif aDet.im_variazione < 0 Then
    aSaldocdrlinea.VARIAZIONI_MENO := Abs(aDet.im_variazione);
  End If;

  CNRCTB054.crea_aggiorna_saldi(aSaldoCdrLinea, '055.esitaVariazioneBilancio', 'N');

 End;

/* */

    If aDet.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR And aDet.ti_gestione = CNRCTB001.GESTIONE_SPESE  And
       aElementoVoce.fl_voce_fondo = 'N' Then
	  -- Aggiorna l'impegno su capitolo
Dbms_Output.PUT_LINE (' det h agg imp');
	  CNRCTB030.AGGIORNAIMPEGNOCAPITOLOVAR(aDet.esercizio, aVarBilancio.esercizio_importi, aDet.cd_voce, aDet.im_variazione, aDet.utuv);
    End if;

Dbms_Output.PUT_LINE (' det i');

    -- Gestione dell'avanzo di amministrazione CNR
    -- (controllo che non vengano valorizzati spesa ed entrata contemp.)
    -- e cassa CDS (controllo + modifica cassa iniziale in tabella esercizio)
    setcassainiassucc(aVarBilancio.esercizio, aDet.cd_cds, aDet.ti_appartenenza, aDet.ti_gestione, aDet.cd_voce, CNRCTB054.TI_COMPETENZA, aVarBilancio.utuv);

  Elsif aVarBilancio.esercizio_importi = aVarBilancio.esercizio + 1 then -- Variazione su anno 2

      update voce_f_saldi_cmp
      Set  im_stanz_iniziale_a2=im_stanz_iniziale_a2 + aDet.im_variazione,
        utuv = aVarBilancio.utuv,
        duva = aVarBilancio.duva,
        pg_ver_rec = pg_ver_rec + 1
      Where esercizio = aVarBilancio.esercizio
        and cd_cds = aDet.cd_cds
        and ti_appartenenza = aDet.ti_appartenenza
        and ti_gestione = aDet.ti_gestione
	and cd_voce = aDet.cd_voce
	    and ti_competenza_residuo = CNRCTB054.TI_COMPETENZA;

  Else 							  								       -- Variazione su anno 3
      update voce_f_saldi_cmp
      Set  im_stanz_iniziale_a3=im_stanz_iniziale_a3 + aDet.im_variazione,
        utuv = aVarBilancio.utuv,
        duva = aVarBilancio.duva,
        pg_ver_rec = pg_ver_rec + 1
     Where esercizio = aVarBilancio.esercizio
       and cd_cds = aDet.cd_cds
       and ti_appartenenza = aDet.ti_appartenenza
       and ti_gestione = aDet.ti_gestione
       and cd_voce = aDet.cd_voce
       and ti_competenza_residuo = CNRCTB054.TI_COMPETENZA;
  End if;

End loop;

  -- Aggiorna lo stato della variazione
  Update var_bilancio set
   stato = CNRCTB054.STATO_VARIAZIONE_DEFINITIVA,
   utuv = aVarBilancio.utuv,
   duva = aVarBilancio.duva,
   pg_ver_rec = pg_ver_rec + 1
  Where esercizio = aVarBilancio.esercizio
    and cd_cds = aVarBilancio.cd_cds
    and ti_appartenenza = aVarBilancio.ti_appartenenza
    and pg_variazione = aVarBilancio.pg_variazione;
End;

/* -- stani 18.06.2009
Procedure predisponeBilFinCDS(aEsercizio varchar2, aCodiceCDS varchar2, aUtente varchar2) is
  aCDR cdr%rowtype;
  aCDS unita_organizzativa%rowtype;
  aUOAfferenza unita_organizzativa%rowtype;
  aSaldo VOCE_F_SALDI_CMP%rowtype;
  aTSNow date;
  aAssEvEv ass_ev_ev%rowtype;
  aCodiceTitolo varchar2(10);
  aPDGCorr pdg_preventivo%rowtype;
  aCdrLinea cdr%rowtype;
 begin
  aTSNow:=sysdate;

  -- Verifica che il BILANCIO finanziario dell'ente sia stato approvato

  if CNRCTB054.isBilancioCnrApprovato(aEsercizio) = 'N' then
   IBMERR001.RAISE_ERR_GENERICO('Bilancio preventivo CNR non ancora approvato !');
  end if;

  aCDS:=CNRCTB020.GETCDSVALIDO(aEsercizio, aCodiceCDS);

  if aCDS.cd_tipo_unita = CNRCTB020.TIPO_ENTE then
   IBMERR001.RAISE_ERR_GENERICO('Il CDS specificato non può essere l''ENTE !');
  end if;


  -- Lock della testata del preventivo

  CNRCTB054.lockBilFin(aEsercizio, aCodiceCDS);

  -- Ripulisco il bilancio finanziario corrente mettendo gli importi attuali a 0 per
  -- i capitoli di spesa parte 1 e quelli di entrata

  -- lock tabella saldi

  for aSal in (select * from voce_f_saldi_cmp a
    where
        esercizio=aEsercizio
    and cd_cds=aCodiceCds
	and ti_competenza_residuo = CNRCTB054.TI_COMPETENZA
    and
    (
     exists (select 1 from voce_f where
          esercizio = aEsercizio
 	  and ti_gestione = CNRCTB001.GESTIONE_SPESE
	  and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
	  and cd_voce = a.cd_voce
	  and cd_parte = CNRCTB001.PARTE1
     )
     or ti_gestione = CNRCTB001.GESTIONE_ENTRATE
	) for update nowait
  ) loop
    null;
  end loop;


  update voce_f_saldi_cmp a set
      IM_STANZ_INIZIALE_A1=0,
      IM_STANZ_INIZIALE_A2=0,
      IM_STANZ_INIZIALE_A3=0,
	  utuv=aUtente,
	  duva=aTSNow,
	  pg_ver_rec = pg_ver_rec + 1
  where
       esercizio = aEsercizio
   and cd_cds=aCodiceCds
   and ti_competenza_residuo = CNRCTB054.TI_COMPETENZA
   and
   (
    exists (select 1 from voce_f where
         esercizio = aEsercizio
	 and ti_gestione = CNRCTB001.GESTIONE_SPESE
	 and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
	 and cd_voce = a.cd_voce
	 and cd_parte = CNRCTB001.PARTE1
    )
     or ti_gestione = CNRCTB001.GESTIONE_ENTRATE
   );

  -- Effettuo i controlli necessari su tutti i CDR di primo livello o AREA del CDS in processo
  for aTCDR in (
     select a.* from v_cdr_valido a, v_unita_organizzativa_valida b where
	       b.cd_unita_padre = aCodiceCDS
	   and b.esercizio = aEsercizio
	   and a.esercizio = aEsercizio
       and a.cd_unita_organizzativa  = b.cd_unita_organizzativa
       and
       (
 	    (
	         a.livello=1
	    ) or
	    (
	         a.livello=2
	     and b.cd_tipo_unita = CNRCTB020.TIPO_AREA
	    )
	   )
  ) loop

    aCDR:=CNRCTB020.GETCDRVALIDO(aEsercizio, aTCDR.cd_centro_responsabilita);

    -- 1. Il CDR di primo livello deve essere in stato Finale
    -- 2. Tutti i CDR legati ad area devono aver ribaltato su area
    -- 3. Tutti i CDR di II livello responsabili di UO CDS AREA devono aver ricevuto il ribaltamento dei CDR afferenti all'area
    --    di tutti i CDR che afferiscono via UO all'area

    if aCDS.cd_tipo_unita = CNRCTB020.TIPO_AREA then
     for aPDG in TUTTI_PDG_AFFERENTI_AREA(aEsercizio, aCDR) loop
      if aPDG.fl_ribaltato_su_area = 'N' then
       IBMERR001.RAISE_ERR_GENERICO('Il CDR: '||aPDG.cd_centro_responsabilita||', che afferisce all''area, non ha effettuato il ribaltamento sull''area!');
  	  end if;
     end loop;
    end if;

    -- Ciclo di lock del PDG del primo CDR e di tutti i PDG dei CDR figli (RUO/NRUO)

    for aPDG in TUTTI_PDG_LINEA(aEsercizio, aCDR) loop
     if aPDG.stato!=CNRCTB050.STATO_PDG_FINALE then
      IBMERR001.RAISE_ERR_GENERICO('Il CDR: '||aPDG.cd_centro_responsabilita||', non è chiuso definitivamente!');
     end if;
     select * into aCdrLinea from cdr
       where
        cd_centro_responsabilita = aPDG.cd_centro_responsabilita;
     aUOAfferenza:=CNRCTB020.GETUOAFFERENZA(aCDRLinea);
--P.R. Da Verificare dopo l'inserimento dell'area di afferenza sul PDG
     if aUOAfferenza.cd_area_ricerca is not null then
       if aPDG.fl_ribaltato_su_area='N' then
        IBMERR001.RAISE_ERR_GENERICO('Il CDR: '||aPDG.cd_centro_responsabilita||', non ha effettuato il ribaltamento sull''area!');
       end if;
     end if;
    end loop;

    -- gestione della parte spese: leggo dall'aggregato per rubrica ed inserisco il bilancio fin. CDS spese
    for aDettS in DETTAGLI_PDG_SPESA_PER_RUBRICA(aEsercizio,aCDR) loop
  	 begin
        select * into aSaldo from voce_f_saldi_cmp where
             ESERCIZIO=aEsercizio
         and CD_CDS=aCodiceCDS
         and TI_APPARTENENZA=CNRCTB001.APPARTENENZA_CDS
         and TI_GESTIONE=CNRCTB001.GESTIONE_SPESE
	     and TI_COMPETENZA_RESIDUO = CNRCTB054.TI_COMPETENZA
         and CD_VOCE=aDettS.CD_VOCE;
     exception when NO_DATA_FOUND then
        aSaldo.ESERCIZIO:=aEsercizio;
        aSaldo.CD_CDS:=aCodiceCDS;
        aSaldo.TI_APPARTENENZA:=CNRCTB001.APPARTENENZA_CDS;
        aSaldo.TI_GESTIONE:=CNRCTB001.GESTIONE_SPESE;
	    aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_COMPETENZA;
        aSaldo.CD_VOCE:=aDettS.CD_VOCE;
  	    aSaldo.FL_SOLA_LETTURA:='N';
        aSaldo.ORIGINE:=CNRCTB054.ORIGINE_PDG;
        aSaldo.VARIAZIONI_PIU:=0;
        aSaldo.VARIAZIONI_MENO:=0;
        aSaldo.IM_OBBLIG_IMP_ACR:=0;
        aSaldo.IM_MANDATI_REVERSALI:=0;
        aSaldo.IM_PAGAMENTI_INCASSI:=0;
        aSaldo.IM_STANZ_INIZIALE_A1:=0;
        aSaldo.IM_STANZ_INIZIALE_A2:=0;
        aSaldo.IM_STANZ_INIZIALE_A3:=0;
        aSaldo.dacr:=aTSNow;
        aSaldo.duva:=aTSNow;
        aSaldo.utuv:=aUtente;
        aSaldo.utcr:=aUtente;
        aSaldo.pg_ver_rec:=1;
        CNRCTB054.ins_VOCE_F_SALDI_CMP (aSaldo);
     end;
       -- Il bilancio dei CDS è annuale, inserisco solo 1 importo
  	 update voce_f_saldi_cmp set
      IM_STANZ_INIZIALE_A1=IM_STANZ_INIZIALE_A1 + aDettS.IM_STANZ_INIZIALE_A1
  	 where
             ESERCIZIO=aEsercizio
         and CD_CDS=aCodiceCDS
         and TI_APPARTENENZA=CNRCTB001.APPARTENENZA_CDS
         and TI_GESTIONE=CNRCTB001.GESTIONE_SPESE
	     and TI_COMPETENZA_RESIDUO=CNRCTB054.TI_COMPETENZA
         and CD_VOCE=aDettS.CD_VOCE;
    end loop;
   end loop; -- termine loop gestione spese

   -- gestione della parte entrate
   -- gli importi vanno derivati dagli importi del bilancio finanziario dell'ente parte spesa
   -- attraverso l'associazione tra titolo spesa CNR + tipo CDS + natura e capitolo CDS
   -- Una volta determinati i sottoarticoli eligibili alla definizione di entrate CDS, attraverso l'associazione
   -- SPESE CNR ENTRATE CDS identifico il capitolo di entrata CDS a cui imputare gli importi
   -- Quindi inserisco il saldo

   for aDetSpePrevCnr in IMPORTI_SPESA_CNR_PER_ETR_CDS(aEsercizio,aCodiceCDS) loop
    begin
     	 -- Estraggo il codice del titolo dal codice della voce:

     	 -- ATTENZIONE OPERAZIONE CABLATA
     	 aCodiceTitolo:=substr(aDetSpePrevCnr.cd_voce,1,4);
       -- ATTENZIONE OPERAZIONE CABLATA

       -- cerco nell'associazione tra titolo di spesa CNR + natura - capitolo di entrata CDS
   	   begin
          select * into aAssEvEv from ass_ev_ev where
    	       esercizio = aEsercizio
    	   and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
    	   and ti_gestione = CNRCTB001.GESTIONE_SPESE
           and ti_elemento_voce = CNRCTB001.TITOLO
    	   and cd_elemento_voce = aCodiceTitolo
    	   and cd_cds = aCDS.cd_tipo_unita
    	   and cd_natura = aDetSpePrevCnr.cd_natura
    	   and ti_appartenenza_coll = CNRCTB001.APPARTENENZA_CDS
    	   and ti_gestione_coll = CNRCTB001.GESTIONE_ENTRATE
    	   and ti_elemento_voce_coll = CNRCTB001.CAPITOLO;
  	   exception when NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Associazione in esercizio: '||aDetSpePrevCnr.esercizio||' tra titolo:'||aCodiceTitolo||' + natura:'||aDetSpePrevCnr.cd_natura||' + tipo CDS:'||aCDS.cd_tipo_unita||' e capitolo entrata CDS non definita!');
  	   end;
  	   begin
        select * into aSaldo from voce_f_saldi_cmp where
             ESERCIZIO=aEsercizio
         and CD_CDS=aCodiceCDS
         and TI_APPARTENENZA=CNRCTB001.APPARTENENZA_CDS
         and TI_GESTIONE=CNRCTB001.GESTIONE_ENTRATE
	     and TI_COMPETENZA_RESIDUO=CNRCTB054.TI_COMPETENZA
         and CD_VOCE=aAssEvEv.cd_elemento_voce_coll;
       exception when NO_DATA_FOUND then
        aSaldo.ESERCIZIO:=aEsercizio;
        aSaldo.CD_CDS:=aCodiceCDS;
        aSaldo.TI_APPARTENENZA:=CNRCTB001.APPARTENENZA_CDS;
        aSaldo.TI_GESTIONE:=CNRCTB001.GESTIONE_ENTRATE;
	    aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_COMPETENZA;
        aSaldo.CD_VOCE:=aAssEvEv.cd_elemento_voce_coll;
        aSaldo.FL_SOLA_LETTURA:='N';
        aSaldo.ORIGINE:=CNRCTB054.ORIGINE_PDG;
        aSaldo.VARIAZIONI_PIU:=0;
        aSaldo.VARIAZIONI_MENO:=0;
        aSaldo.IM_OBBLIG_IMP_ACR:=0;
        aSaldo.IM_MANDATI_REVERSALI:=0;
        aSaldo.IM_PAGAMENTI_INCASSI:=0;
        aSaldo.IM_STANZ_INIZIALE_A1:=0;
        aSaldo.IM_STANZ_INIZIALE_A2:=0;
        aSaldo.IM_STANZ_INIZIALE_A3:=0;
        aSaldo.dacr:=aTSNow;
        aSaldo.duva:=aTSNow;
        aSaldo.utuv:=aUtente;
        aSaldo.utcr:=aUtente;
        aSaldo.pg_ver_rec:=1;
        CNRCTB054.ins_VOCE_F_SALDI_CMP (aSaldo);
  	   end;
       -- Il bilancio dei CDS è annuale, inserisco solo 1 importo
  	   update voce_f_saldi_cmp set
        IM_STANZ_INIZIALE_A1=IM_STANZ_INIZIALE_A1 + aDetSpePrevCnr.IM_STANZ_INIZIALE_A1
  	   where
             ESERCIZIO=aEsercizio
         and CD_CDS=aCodiceCDS
         and TI_APPARTENENZA=CNRCTB001.APPARTENENZA_CDS
         and TI_GESTIONE=CNRCTB001.GESTIONE_ENTRATE
	     and TI_COMPETENZA_RESIDUO=CNRCTB054.TI_COMPETENZA
         and CD_VOCE=aAssEvEv.cd_elemento_voce_coll;
    exception when NO_DATA_FOUND then
  	 null;
  	end;
   end loop; -- trmine loop gestione entrate
   -- update dello stato del bilancio preventivo del CDS
   update BILANCIO_PREVENTIVO set
    stato = CNRCTB054.STATO_PREVENTIVO_PREDISPOSTO,
    duva = aTSNow,
    utuv = aUtente,
	pg_ver_rec=pg_ver_rec+1
   where
        esercizio = aEsercizio
    and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
    and cd_cds = aCodiceCDS;
 end;
*/

-- 24.11.2005 Con l'introduzione del PdGP gestionale la predisposizione del Bilancio del CNR
--            viene fatta solo per la spesa e con nuovi criteri

-- VECCHIA GESTIONE !!!!

/*
 procedure predisponeBilFinCNRInt(aEsercizio varchar2, aUtente varchar2) is
  aCDRPrimo cdr%rowtype;
  aUO unita_organizzativa%rowtype;
  aUOCDS unita_organizzativa%rowtype;
  aUOCDSENTE unita_organizzativa%rowtype;
  aVoce voce_f%rowtype;
  aSaldo VOCE_F_SALDI_CMP%rowtype;
  aTSNow date;
  aTempDate date;
  aCodiceCDSCap varchar2(30);
  aCodiceCDSSottoart varchar2(30);
  aCDSPresidenteArea unita_organizzativa%rowtype;
  aCDRArea cdr%rowtype;
  aUOArea unita_organizzativa%rowtype;
  aCodiceVoce varchar2(50);
  aAss ass_ev_ev%rowtype;
  aPdgAggregato pdg_aggregato%rowtype;
  aEV elemento_voce%rowtype;
  aTemp varchar2(50);
  aCategoria varchar2(1);
  isAggregatoChiusoPerTutti boolean;
  aEsRow esercizio%rowtype;
 begin
  -- calcolo del timestamp corrente

  aTSNow:=sysdate;

  -- Leggo l'UO CDS ENTE
  select * into aUOCDSENTE from unita_organizzativa where
       cd_tipo_unita = CNRCTB020.TIPO_ENTE
   and fl_uo_cds  ='Y';
  aUOCDSENTE:=CNRCTB020.GETUOVALIDA(aEsercizio,aUOCDSENTE.cd_unita_organizzativa);

  select * into aEsRow from esercizio where
   cd_cds = aUOCDSENTE.cd_unita_padre
   and esercizio =aEsercizio;

  if aEsRow.st_apertura_chiusura != CNRCTB008.STATO_APERTURA then
   IBMERR001.RAISE_ERR_GENERICO('Per produrre il bilancio finanziario dell''Ente l''esercizio contabile deve essere aperto');
  end if;

  CNRCTB054.lockBilFin(aEsercizio, aUOCDSENTE.cd_unita_padre);

  if not (CNRCTB054.isBilancioCNRIniziale(aEsercizio) = 'Y') then
   IBMERR001.RAISE_ERR_GENERICO('Bilancio finanziario CNR già prodotto o approvato!');
  end if;

  -- Controlli da attivare per la derivazione del bilancio finanziario dell'ente
  -- Verifico che per ogni CDR di I livello o CDR resp. di AREA esista l'aggregato iniziale

  isAggregatoChiusoPerTutti:=true;
  for aPDG in TUTTI_PDG_PER_BIL_CNR(aEsercizio) loop
   begin
    select * into aPdgAggregato from PDG_AGGREGATO where
        esercizio = aPDG.esercizio
    and cd_centro_responsabilita = aPDG.cd_centro_responsabilita;
   exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione del CDR: '||aPDG.cd_centro_responsabilita||' non ha prodotto l''aggregato per il centro!');
   end;
   if not (aPdgAggregato.stato = CNRCTB050.STATO_AGGREGATO_FINALE) then
    isAggregatoChiusoPerTutti:=false;
   end if;
  end loop;
  if isAggregatoChiusoPerTutti then
   -- update dello stato del piano
   update BILANCIO_PREVENTIVO set
    stato = CNRCTB054.STATO_PREVENTIVO_PREDISPOSTO,
	duva = aTSNow,
	utuv = aUtente,
	pg_ver_rec = pg_ver_rec + 1
   where
            esercizio = aEsercizio
        and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
        and cd_cds = aUOCDSENTE.cd_unita_padre;
  else -- non cambia lo stato del piano ma devo aggiornare i dati duva, utuv e pg_ver_rec comunque
   update BILANCIO_PREVENTIVO set
	duva = aTSNow,
	utuv = aUtente,
	pg_ver_rec = pg_ver_rec + 1
   where
            esercizio = aEsercizio
        and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
        and cd_cds = aUOCDSENTE.cd_unita_padre;
  end if;

  -- Ripulisco il bilancio finanziario corrente mettendo gli importi attuali a 0 per
  -- i capitoli di spesa parte 1 e quelli di entrata

  -- lock tabella saldi

  for aSal in (select * from voce_f_saldi_cmp a
    where
        esercizio = aEsercizio
    and a.cd_cds=aUOCDSENTE.cd_unita_padre
	and a.ti_appartenenza=CNRCTB001.APPARTENENZA_CNR
	and a.ti_competenza_residuo=CNRCTB054.TI_COMPETENZA
    and
    (
     exists (select 1 from voce_f where
          esercizio = aEsercizio
 	  and ti_gestione = CNRCTB001.GESTIONE_SPESE
	  and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
	  and cd_voce = a.cd_voce
	  and cd_parte = CNRCTB001.PARTE1
     )
     or a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
    ) for update nowait
   ) loop
    null;
   end loop;

  update voce_f_saldi_cmp a set
      IM_STANZ_INIZIALE_A1=0,
      IM_STANZ_INIZIALE_A2=0,
      IM_STANZ_INIZIALE_A3=0,
	  utuv=aUtente,
	  duva=aTSNow,
	  pg_ver_rec = pg_ver_rec + 1
  where
       esercizio = aEsercizio
   and cd_cds=aUOCDSENTE.cd_unita_padre
   and ti_competenza_residuo=CNRCTB054.TI_COMPETENZA
   and
   (
    exists (select 1 from voce_f where
         esercizio = aEsercizio
	 and ti_gestione = CNRCTB001.GESTIONE_SPESE
	 and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
	 and cd_voce = a.cd_voce
	 and cd_parte = CNRCTB001.PARTE1
    )
    or ti_gestione = CNRCTB001.GESTIONE_ENTRATE
   );

  -- Legge i dettagli dell'aggregato parte spese
  --
  -- Le regole di imputazione su bilancio finanziario CNR parte spese sono:
  -- CDR di I livello e dettaglio di natura 1-4 -> Sottoarticolo proprio del capitolo
  -- CDR di I livello e dettaglio di natura 5   -> Sottoarticolo nat. 5 area del CDS pres. dell' area
  -- CDR di II livello (CDS area) natura 1-4    -> Sottoarticolo nat. 1-4 area del CDS pres. area

  for aDettS in DETTAGLI_PDG_AGGREGATO_SPE(aEsercizio) loop

   aCDRPrimo:=CNRCTB020.GETCDRVALIDO(aEsercizio,aDettS.cd_centro_responsabilita);

   -- Leggo l'UO del dettaglio
   aUO:=CNRCTB020.GETUOVALIDA(aEsercizio,aCDRPrimo.cd_unita_organizzativa);

   -- Leggo l'UO CDS del dettaglio
   select * into aUOCDS from unita_organizzativa where
        cd_unita_padre  = aUO.cd_unita_padre
    and fl_uo_cds = 'Y';
   aUOCDS:=CNRCTB020.GETUOVALIDA(aEsercizio,aUOCDS.cd_unita_organizzativa);

   aCodiceCDSCap:=aUOCDS.cd_unita_padre;      -- Di default il capitolo e il CDS del CDR di primo livello che aggrega
   aCodiceCDSSottoart := aUOCDS.cd_unita_padre; -- Di default il Codice proprio del sottoarticolo è = al codice del CDS

   if aUOCDS.cd_tipo_unita = CNRCTB020.TIPO_AREA then
    aCDSPresidenteArea:=CNRCTB020.getCDSPresidenteArea(aEsercizio, aCDRPrimo);
    if aCDSPresidenteArea.cd_unita_organizzativa is not null then
	 aCodiceCDSCap:=aCDSPresidenteArea.cd_unita_organizzativa;
	else
	 IBMERR001.RAISE_ERR_GENERICO('Cds presidente dell''AREA: '''||aUOCDS.cd_unita_padre||''' non trovato!!!');
	end if;
   end if;

   if aDettS.cd_natura=CNRCTB001.NATURA_5 then -- Se i dettagli sono di natura 5 devo recuperare il CDS presidente di area
	-- Estraggo l'area da cd_cds del dettaglio di aggregazione
    begin
	 select * into aUOArea from unita_organizzativa where
          cd_unita_padre=aDettS.cd_cds
	  and fl_uo_cds='Y';
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Area di ricerca con codice '''||aDettS.cd_cds||''' non trovata!');
	end;
	aUOArea:=CNRCTB020.GETUOVALIDA(aDettS.esercizio,aUOArea.cd_unita_organizzativa);
	select * into aCDRArea from cdr where
         cd_unita_organizzativa=aUOArea.cd_unita_organizzativa
	 and to_number(cd_proprio_cdr) = 0;
	aCDRArea:=CNRCTB020.GETCDRVALIDO(aEsercizio,aCDRArea.cd_centro_responsabilita);

	-- Dall'area estraggo il presidente dell'area
    aCDSPresidenteArea:=CNRCTB020.getCDSPresidenteArea(aEsercizio, aCDRArea);
    if aCDSPresidenteArea.cd_unita_organizzativa is not null then
	 aCodiceCDSCap:=aCDSPresidenteArea.cd_unita_organizzativa;
	 aCodiceCDSSottoart:=aDettS.cd_cds;
	else
	 IBMERR001.RAISE_ERR_GENERICO('Cds presidente dell''AREA: '''||aUOArea.cd_unita_padre||''' non trovato!!!');
	end if;
   end if;

   -- Leggo la voce_f su cui mettere i valori
   -- Si tratta di sottoarticolo spesa CNR dove entro con
   --  cd_proprio_voce = codice cds (Sottoarticolo proprio del CDS)
   -- cd_cds codice del CDS
   --  cd_natura = quella dell'aggregato
   --  cd_funzione = quella dell'aggregato
   --  ti_voce = 'E' (Sottoarticolo)
   --  cd_categoria = '1' per MACROISTITUTI e '2' per SAC
   --  cd_voce like cd_titolo.%

    if  -- Se il dettaglio è del SAC e non è di natura 5
	     aUOCDS.cd_tipo_unita = CNRCTB001.TIPOCDS_SAC
	 and not aDettS.cd_natura=CNRCTB001.NATURA_5
	then
	 aCodiceVoce:=aDettS.cd_elemento_voce; -- in aCodiceVoce c'è la tipologia di intervento (che va in CATEGORIA 2 parte 1 spesa CNR)
	else
	 aTemp:=aDettS.cd_elemento_voce;
	 begin
	  select * into aAss from ass_ev_ev where
	       esercizio = aEsercizio
	   and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
	   and ti_gestione = CNRCTB001.GESTIONE_SPESE
	   and ti_elemento_voce = CNRCTB001.TITOLO
	   and ti_appartenenza_coll = CNRCTB001.APPARTENENZA_CDS
	   and ti_gestione_coll = CNRCTB001.GESTIONE_SPESE
	   and ti_elemento_voce_coll = CNRCTB001.TITOLO
	   and cd_elemento_voce_coll = aTemp;
	 exception when NO_DATA_FOUND then
	  IBMERR001.RAISE_ERR_GENERICO('Nessuna associazione con titolo spesa CNR trovata per titolo spesa CDS: '||aDettS.cd_elemento_voce);
	 end;
	 aCodiceVoce:=aAss.cd_elemento_voce;
	end if;

    begin
     if  -- Se il dettaglio è del SAC e non è di natura 5 la categoria è 2, altrimenti 1
	      aUOCDS.cd_tipo_unita = CNRCTB001.TIPOCDS_SAC
	  and aDettS.cd_natura!=CNRCTB001.NATURA_5
	 then
	  aCategoria:=CNRCTB001.CATEGORIA2_SPESE_CNR;
	 else
      aCategoria:=CNRCTB001.CATEGORIA1_SPESE_CNR;
     end if;
     select * into aVoce from voce_f where
          esercizio = aEsercizio
	  and ti_gestione = CNRCTB001.GESTIONE_SPESE
	  and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
      and cd_proprio_voce = aCodiceCDSSottoart
      and cd_cds = aCodiceCDSCap
      and cd_natura = aDettS.cd_natura
      and cd_funzione = aDettS.cd_funzione
      and ti_voce = CNRCTB001.SOTTOARTICOLO
      and cd_categoria = aCategoria
	  -- Per la categoria 1 entro con una cd_voce like aCodiceVoce=titolo
	  -- Per la categoria 2 identifico la voce dal campo cd_titolo_capitolo = aCodiceVoce=tipologia di intervento
	  and (
	          (aCategoria = CNRCTB001.CATEGORIA2_SPESE_CNR and cd_titolo_capitolo = aCodiceVoce)
	       or (aCategoria = CNRCTB001.CATEGORIA1_SPESE_CNR and cd_voce like aCodiceVoce||'.%')
      );
	 aSaldo:=null;
	 begin
      aSaldo.ESERCIZIO:=aVoce.esercizio;
      aSaldo.CD_CDS:=aUOCDSENTE.cd_unita_padre;
      aSaldo.TI_APPARTENENZA:=aVoce.ti_appartenenza;
      aSaldo.TI_GESTIONE:=aVoce.ti_gestione;
	  aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_COMPETENZA;
      aSaldo.CD_VOCE:=aVoce.cd_voce;
      aSaldo.FL_SOLA_LETTURA:='N';
      aSaldo.ORIGINE:=CNRCTB054.ORIGINE_PDG;
      aSaldo.dacr:=aTSNow;
      aSaldo.duva:=aTSNow;
      aSaldo.utuv:=aUtente;
      aSaldo.utcr:=aUtente;
      aSaldo.pg_ver_rec:=1;
      aSaldo.VARIAZIONI_PIU:=0;
      aSaldo.VARIAZIONI_MENO:=0;
      aSaldo.IM_OBBLIG_IMP_ACR:=0;
      aSaldo.IM_MANDATI_REVERSALI:=0;
      aSaldo.IM_PAGAMENTI_INCASSI:=0;
      aSaldo.IM_STANZ_INIZIALE_A1:=0;
      aSaldo.IM_STANZ_INIZIALE_A2:=0;
	  aSaldo.IM_STANZ_INIZIALE_A3:=0;
	  CNRCTB054.ins_VOCE_F_SALDI_CMP (aSaldo);
     exception when dup_val_on_index then
	  null;
	 end;
     begin
	  aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_RESIDUI;
	  CNRCTB054.ins_VOCE_F_SALDI_CMP (aSaldo);
     exception when dup_val_on_index then
	  null;
	 end;

	 update voce_f_saldi_cmp set
      IM_STANZ_INIZIALE_A1=IM_STANZ_INIZIALE_A1
                  + aDettS.IM_RI_CCS_SPESE_ODC
                  + aDettS.IM_RK_CCS_SPESE_OGC
                  + aDettS.IM_RQ_SSC_COSTI_ODC
                  + aDettS.IM_RS_SSC_COSTI_OGC
                  + aDettS.IM_RU_SPESE_COSTI_ALTRUI,
      IM_STANZ_INIZIALE_A2=IM_STANZ_INIZIALE_A2
                  + aDettS.IM_RAC_A2_SPESE_ODC
                  + aDettS.IM_RAE_A2_SPESE_OGC
                  + aDettS.IM_RAG_A2_SPESE_COSTI_ALTRUI,
      IM_STANZ_INIZIALE_A3=IM_STANZ_INIZIALE_A3
                  + aDettS.IM_RAL_A3_SPESE_ODC
                  + aDettS.IM_RAN_A3_SPESE_OGC
                  + aDettS.IM_RAP_A3_SPESE_COSTI_ALTRUI,
	  duva = aTSNow,
	  utuv = aUtente,
	  pg_ver_rec = pg_ver_rec + 1
	 where
           ESERCIZIO=aVoce.esercizio
       and CD_CDS=aUOCDSENTE.cd_unita_padre
       and TI_APPARTENENZA=aVoce.ti_appartenenza
       and TI_GESTIONE=aVoce.ti_gestione
	   and TI_COMPETENZA_RESIDUO=CNRCTB054.TI_COMPETENZA
       and CD_VOCE=aVoce.cd_voce;

   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Sottoarticolo di spesa non trovata per voce del piano: '||aCodiceVoce||' cap:'||aCodiceCDSCap||' sottart: '||aCodicecdssottoart||' natura:'||aDettS.cd_natura||' funzione:'||aDettS.cd_funzione||' cat.:'||aCategoria);
   end;
  end loop;

  -- ******************************************************
  --
  -- B I L A N C I O    C N R   P A R T E    E N T R A T E
  --
  -- ******************************************************

  -- Legge i dettagli di aggregato parte entrate

  for aDettE in DETTAGLI_PDG_AGGREGATO_ETR(aEsercizio) loop
   -- leggo il cdr del dettaglio
   select * into aCDRPrimo from cdr where
            cd_centro_responsabilita = aDettE.cd_centro_responsabilita;

   -- Leggo l'UO del dettaglio
   aUO:=CNRCTB020.GETUOVALIDA(aEsercizio, aCDRPrimo.cd_unita_organizzativa);

   -- Non serve più caricare l'UO CDS
   -- select * into aUOCDS from unita_organizzativa where
   --     cd_unita_padre = aUO.cd_unita_padre
   -- and fl_uo_cds  ='Y';
   -- aUOCDS:=CNRCTB020.GETUOVALIDA(aEsercizio, aUOCDS.cd_unita_organizzativa);

   -- Leggo la voce_f su cui mettere i valori
   -- Si tratta di articolo di entrata CNR dove entro con
   --  cd_unita_organizzativa = codice uo collegata al CDR di primo livello (UO CDS per ISTITUTI e AREE, UO RUBRICA PER SAC)
   --  ti_voce = 'A' (Articolo)
   begin
    select * into aVoce from voce_f where
        esercizio = aEsercizio
	 and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
	 and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
     and cd_cds = aUO.cd_unita_padre
     and ti_voce = CNRCTB001.ARTICOLO
     and cd_unita_organizzativa = aUO.cd_unita_organizzativa
	 and cd_titolo_capitolo = aDettE.cd_elemento_voce;
	exception when NO_DATA_FOUND then
	 IBMERR001.RAISE_ERR_GENERICO('Articolo entrata non trovato per ev: '||aDettE.cd_elemento_voce||' uo:'||aUO.cd_unita_organizzativa);
	end;

	aSaldo:=null;
	begin -- Parte competenza
	 -- Inserisco i dati di bilancio finanziario CNR entrate predisposto
     aSaldo.ESERCIZIO:=aVoce.esercizio;
     aSaldo.CD_CDS:=aUOCDSENTE.cd_unita_padre;
     aSaldo.TI_APPARTENENZA:=aVoce.ti_appartenenza;
     aSaldo.TI_GESTIONE:=aVoce.ti_gestione;
	 aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_COMPETENZA;
     aSaldo.CD_VOCE:=aVoce.CD_VOCE;
	 aSaldo.FL_SOLA_LETTURA:='N';
     aSaldo.ORIGINE:=CNRCTB054.ORIGINE_PDG;
     aSaldo.dacr:=aTSNow;
     aSaldo.duva:=aTSNow;
     aSaldo.utuv:=aUtente;
     aSaldo.utcr:=aUtente;
     aSaldo.pg_ver_rec:=1;
     aSaldo.VARIAZIONI_PIU:=0;
     aSaldo.VARIAZIONI_MENO:=0;
     aSaldo.IM_OBBLIG_IMP_ACR:=0;
     aSaldo.IM_MANDATI_REVERSALI:=0;
     aSaldo.IM_PAGAMENTI_INCASSI:=0;
     aSaldo.IM_STANZ_INIZIALE_A1:=0;
     aSaldo.IM_STANZ_INIZIALE_A2:=0;
     aSaldo.IM_STANZ_INIZIALE_A3:=0;
     CNRCTB054.ins_VOCE_F_SALDI_CMP (aSaldo);
    exception when dup_val_on_index then
     null;
	end;
	begin -- Parte residui
	 aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_RESIDUI;
     CNRCTB054.ins_VOCE_F_SALDI_CMP (aSaldo);
	exception when dup_val_on_index then
	 null;
	end;
    update voce_f_saldi_cmp set
     IM_STANZ_INIZIALE_A1=IM_STANZ_INIZIALE_A1 + aDettE.IM_RA_RCE + aDettE.IM_RC_ESR,
     IM_STANZ_INIZIALE_A2=IM_STANZ_INIZIALE_A2+aDettE.IM_RE_A2_ENTRATE,
     IM_STANZ_INIZIALE_A3=IM_STANZ_INIZIALE_A3+aDettE.IM_RG_A3_ENTRATE,
     duva = aTSNow,
     utuv = aUtente,
     pg_ver_rec = pg_ver_rec + 1
    where
         ESERCIZIO=aVoce.esercizio
     and CD_CDS=aUOCDSENTE.cd_unita_padre
     and TI_APPARTENENZA=aVoce.ti_appartenenza
     and TI_GESTIONE=aVoce.ti_gestione
     and TI_COMPETENZA_RESIDUO=CNRCTB054.TI_COMPETENZA
     and CD_VOCE=aVoce.cd_voce;
  end loop;

  -- Blocco della parte I ed Entrate nel caso il bilancio preventivo venga predisposto da aggregati tutti chiusi
  -- per l'ultima volta

  if isAggregatoChiusoPerTutti then
   -- Aggiornamento parte spese
   update voce_f_saldi_cmp a set
    a.fl_sola_lettura = 'Y'
   where
        a.esercizio = aEsercizio
    and a.cd_cds=aUOCDSENTE.cd_unita_padre
	and a.ti_appartenenza=CNRCTB001.APPARTENENZA_CNR
	and a.ti_competenza_residuo=CNRCTB054.TI_COMPETENZA
    and
    (
      exists (select 1 from voce_f where
           esercizio = aEsercizio
 	   and ti_gestione = CNRCTB001.GESTIONE_SPESE
	   and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
	   and cd_voce = a.cd_voce
	   and cd_parte = CNRCTB001.PARTE1
      )
      or a.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
    );
  end if;

  -- Crea gli impegni per capitolo
  CNRCTB030.CREAIMPEGNICNR(aEsercizio);
 end;
*/
--------------------------------------------------------------------------------------
----------------------------- FINE VECCHIA GESTIONE !!!! -----------------------------
--------------------------------------------------------------------------------------

------------------------------------ NUOVA GESTIONE ----------------------------------------
-- 24.11.2005 Con l'introduzione del PdGP gestionale la predisposizione del Bilancio del CNR
--            viene fatta SOLO PER LA SPESA e con NUOVI CRITERI
--------------------------------------------------------------------------------------------

Procedure predispBilFinCNRInt_da_gest(aEsercizio NUMBER, aCdR VARCHAR2, aUtente varchar2) is
  aUO                   unita_organizzativa%rowtype;
  aUOCDS                unita_organizzativa%rowtype;
  aUOCDSENTE            unita_organizzativa%rowtype;
  aCDSENTE              unita_organizzativa%rowtype;
  aCDRENTE              CDR%rowtype;
  aVoce                 voce_f%rowtype;
  aCd_cds_cdr_ori       unita_organizzativa.cd_unita_organizzativa%Type;
  aCDS_voce             unita_organizzativa.cd_unita_organizzativa%Type;
  aNuovoSaldo           VOCE_F_SALDI_CDR_LINEA%rowtype;
  aSaldoCmp             VOCE_F_SALDI_CMP%rowtype;
  ACD_TIT_CAP_FISSO     VOCE_F.CD_TITOLO_CAPITOLO%Type;
  aCategoria            VARCHAR2(1);
  aTSNow                date;
  aCodiceCDSSottoart    varchar2(30);
  aUOPresidenteArea     unita_organizzativa.cd_unita_organizzativa%Type;
  aCDSPresidenteArea    unita_organizzativa%Rowtype;
  aPdgEsercizio         pdg_esercizio%rowtype;
  aCdsSAC               unita_organizzativa%Rowtype;
  isGestionaleChiuso    boolean;
  aEsRow                esercizio%rowtype;
  aLINEA_S_ENTE         LINEA_ATTIVITA.CD_LINEA_ATTIVITA%Type;
  ACLASSIFICAZIONE      CLASSIFICAZIONE_VOCI%Rowtype;
  IMP_DA_AGGIUNGERE_INT     NUMBER := 0;
  IMP_DA_AGGIUNGERE_EST     NUMBER := 0;

Begin
  -- calcolo del timestamp corrente

  aTSNow:=sysdate;

---------------------- CONTROLLO L'APERTURA DEL BILANCIO DEL 999 ----------------------

  -- recupero il CDS ENTE
  select * into aCDSENTE
  from  unita_organizzativa
  Where cd_tipo_unita = CNRCTB020.TIPO_ENTE
    and fl_cds  ='Y';

  aCDSENTE := CNRCTB020.GETCDSVALIDO(aEsercizio,aCDSENTE.cd_unita_organizzativa);

  -- recupero la UO ENTE
  select * into aUOCDSENTE
  from  unita_organizzativa
  Where cd_tipo_unita = CNRCTB020.TIPO_ENTE
    and fl_uo_cds  ='Y';

  -- recupero la UO ENTE
  aUOCDSENTE := CNRCTB020.GETUOVALIDA(aEsercizio,aUOCDSENTE.cd_unita_organizzativa);

  -- recupero il CDR Ente
  aCDRENTE := CNRCTB020.GETCDRENTE;

  -- recupero il CDS della SAC
  aCdsSAC := CNRCTB020.GETCDSSACVALIDO(aEsercizio);

  select * into aEsRow
  from  esercizio
  Where cd_cds = aUOCDSENTE.cd_unita_padre
    and esercizio =aEsercizio;

  -- l'esercizio contabile (tabella ESERCIZIO) deve essere aperto per il CDS 999

  if aEsRow.st_apertura_chiusura != CNRCTB008.STATO_APERTURA then
   IBMERR001.RAISE_ERR_GENERICO
        ('Per produrre il bilancio finanziario dell''Ente l''esercizio contabile '||Aesercizio||' deve essere aperto per il CDS '||aUOCDSENTE.cd_unita_padre);
  end if;

------------------------ LOCCA tabella BILANCIO_PREVENTIVO per il CDS 999 ----------------------

  CNRCTB054.lockBilFin(aEsercizio, aUOCDSENTE.cd_unita_padre);

  if not (CNRCTB054.isBilancioCNRIniziale(aEsercizio) = 'Y') then
   IBMERR001.RAISE_ERR_GENERICO('Bilancio finanziario CNR già prodotto o approvato!');
  end if;

------- controllo la chiusura del PdG gestionale del Cdr che scarico sul Bilancio CNR --------

  isGestionaleChiuso := true;

   begin
    select * into aPdgEsercizio
    from    PDG_ESERCIZIO
    Where   esercizio = aesercizio
       and cd_centro_responsabilita = aCdr;

    if not (aPdgesercizio.stato = CNRCTB050.STATO_PDG2_APERTO_GEST) Then
      IBMERR001.RAISE_ERR_GENERICO('Il PdG gestionale del CDR: '||acdr||' non risulta in stato "Aperto" !');
    end if;

   exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('Il CDR CDR: '||acdr||' non risulta avere alcun PdG gestionale !');
   end;

-- NON Ripulisco PIU' il bilancio finanziario corrente mettendo gli importi attuali a 0
-- ALTRI CDR POSSONO INCREMENTANO LE MIE RIGHE (AREE) ED IO POSSO APRIRE RIGHE DI ALTRI CDR !

-- ATTENZIONE !!!!  L'ENTRATA NON SI FA PIU' !!!

  ------------------------------- NUOVA GESTIONE ---------------------------------

  -- Legge i dettagli dal PdG gestionale chiuso

  -- Le regole di imputazione su bilancio finanziario CNR parte spese sono:
  -- I livello: CDS di origine (o Presidente dell'Area indicata da me)
  -- II livello: La natura, "1" per Fonti Interne e "2" per Fonti Esterne
  -- III livello: CDS/Area a seconda dei casi

  -- INIZIO AGGREGAZIONE VERA E PROPRIA, LEGGO I DETTAGLI DEL GESTIONALE DEL CDR

  -- Cursore sui dettagli del gestionale del CDR per costituzione parte spese bilancio finanziario CNR

For aDettS in (select ESERCIZIO, CD_CENTRO_RESPONSABILITA, CD_CDS_AREA,
                      CD_CDR_ASSEGNATARIO, ID_CLASSIFICAZIONE,
                        Sum(IM_SPESE_GEST_DECENTRATA_INT) tot_dec_int,
                        Sum(IM_SPESE_GEST_DECENTRATA_EST) tot_dec_est,
                        Sum(IM_SPESE_GEST_ACCENTRATA_INT) tot_acc_int,
                        Sum(IM_SPESE_GEST_ACCENTRATA_EST) tot_acc_est
                From   PDG_MODULO_SPESE_GEST
		where  ESERCIZIO = AESERCIZIO And
		       CD_CENTRO_RESPONSABILITA = ACDR And
		       ORIGINE = 'PRE' And
		       CATEGORIA_DETTAGLIO In ('DIR', 'STI')
		GROUP By ESERCIZIO, CD_CENTRO_RESPONSABILITA, CD_CDS_AREA, CD_CDR_ASSEGNATARIO,
		      ID_CLASSIFICAZIONE
		ORDER By ESERCIZIO, CD_CENTRO_RESPONSABILITA, CD_CDS_AREA, CD_CDR_ASSEGNATARIO,
		      ID_CLASSIFICAZIONE ) Loop

Dbms_Output.PUT_LINE ('a');

IMP_DA_AGGIUNGERE_INT := 0;
IMP_DA_AGGIUNGERE_EST := 0;

   -- estraggo il CDS del CdR di Origine per il primo livello della voce
   -- estraggo la Natura per il secondo livello della voce
   -- estraggo l'area per il terzo livello della voce

   -- ESTRAGGO TUTTA LA RIGA DI ID_CLASSIFICAZIONE PER L'ID SELEZIONATO

   Select *
   Into   ACLASSIFICAZIONE
   From   CLASSIFICAZIONE_VOCI
   Where  ID_CLASSIFICAZIONE = ADETTS.ID_CLASSIFICAZIONE;

   -- estraggo il CDS del CdR di I livello originale (per l'identificazione della voce)

aCd_cds_cdr_ori := CNRUTL001.getCdsFromCdr(aCdR);

Dbms_Output.PUT_LINE ('aCd_cds_cdr_ori: '||aCd_cds_cdr_ori);
Dbms_Output.PUT_LINE ('aDettS.cd_cds_area: '||aDettS.cd_cds_area);
Dbms_Output.PUT_LINE ('aCdsSAC.cd_unita_organizzativa: '||aCdsSAC.cd_unita_organizzativa);

                        /***** inizio controllo CdR / CDS / AREE ********/

If aCd_cds_cdr_ori = aDettS.cd_cds_area Then     -- non è un dettaglio di Area, ma è direttamente mio

        -- vedo se è per un accentratore SAC o direttamente del CDR

If ((ACLASSIFICAZIONE.CDR_ACCENTRATORE Is Not Null And ACLASSIFICAZIONE.CDR_ACCENTRATORE = ADETTS.CD_CDR_ASSEGNATARIO) Or
    (cnrctb020.getcdCDSSACValido(aDettS.ESERCIZIO) = CNRUTL001.GETCDSFROMCDR(ADETTS.CD_CDR_ASSEGNATARIO))) Then

Dbms_Output.PUT_LINE ('a');

      aCDS_voce          := aCdsSAC.cd_unita_organizzativa;
      aCodiceCDSSottoart := aCdsSAC.cd_unita_organizzativa;

  -- RECUPERO PARTE/TITOLO/CATEGORIA FISSA DALLA CONFIGURAZIONE_CNR PER LA SAC
  ACD_TIT_CAP_FISSO := CNRCTB015.GETVAL01PERCHIAVE(aESERCIZIO, 'ELEMENTO_VOCE_SPECIALE', 'CD_TITOLO_CAPITOLO_BIL_CNR_SAC');
  aCategoria := CNRCTB001.CATEGORIA2_SPESE_CNR;

  Else
Dbms_Output.PUT_LINE ('b');
      aCDS_voce          := aCd_cds_cdr_ori;
      aCodiceCDSSottoart := aCd_cds_cdr_ori;

  -- RECUPERO PARTE/TITOLO/CATEGORIA FISSA DALLA CONFIGURAZIONE_CNR PER LA NON SAC
  ACD_TIT_CAP_FISSO := CNRCTB015.GETVAL01PERCHIAVE(aESERCIZIO, 'ELEMENTO_VOCE_SPECIALE', 'CD_TITOLO_CAPITOLO_BIL_CNR_NO_SAC');

  aCategoria := CNRCTB001.CATEGORIA1_SPESE_CNR;

  End If;

Elsif aCd_cds_cdr_ori != aDettS.cd_cds_area Then -- Se il dettaglio è di un'area,
                                                    -- vedo qual'è la UO Presidente dell'AREA

  -- RECUPERO PARTE/TITOLO/CATEGORIA FISSA DALLA CONFIGURAZIONE_CNR PER LA NON SAC
  ACD_TIT_CAP_FISSO := CNRCTB015.GETVAL01PERCHIAVE(aESERCIZIO, 'ELEMENTO_VOCE_SPECIALE', 'CD_TITOLO_CAPITOLO_BIL_CNR_NO_SAC');
  aCategoria := CNRCTB001.CATEGORIA1_SPESE_CNR;

Dbms_Output.PUT_LINE ('c');
    Begin
     Select CD_UNITA_ORGANIZZATIVA
     Into   aUOPresidenteArea
     From   ass_uo_area
     Where  esercizio = aEsercizio And
            CD_AREA_RICERCA = aDetts.cd_cds_area And
            FL_PRESIDENTE_AREA = 'Y';
    Exception
      When No_Data_Found Then
        IBMERR001.RAISE_ERR_GENERICO('UO presidente dell''AREA: '''||aDetts.cd_cds_area||''' non trovata !!!');
    End;

   -- recupero il CDS presidente di Area

     Select CDS.*
     Into   aCDSPresidenteAREA
     From   UNITA_ORGANIZZATIVA UO, UNITA_ORGANIZZATIVA CDS
     Where  UO.CD_UNITA_ORGANIZZATIVA = aUOPresidenteArea And
            UO.CD_UNITA_PADRE = CDS.CD_UNITA_ORGANIZZATIVA And
            CDS.FL_CDS = 'Y';

     If aCDSPresidenteAREA.cd_unita_organizzativa = aCd_cds_cdr_ori Then -- se il CDS Presidente dell'Area sono io
      aCDS_voce          := aCd_cds_cdr_ori;
      aCodiceCDSSottoart := aDettS.cd_cds_area;
     Elsif aCd_cds_cdr_ori != aCDSPresidenteAREA.cd_unita_organizzativa Then -- se il CDS Presidente dell'Area NON sono io
      aCDS_voce          := aCDSPresidenteAREA.cd_unita_organizzativa;
      aCodiceCDSSottoart := aDettS.cd_cds_area;
     End If;
End If;
Dbms_Output.PUT_LINE ('aCDS_voce: '||aCDS_voce);
Dbms_Output.PUT_LINE ('aCodiceCDSSottoart: '||aCodiceCDSSottoart);

                        /***** fine controllo CdR / CDS / AREE ********/

  -- RECUPERO LA LINEA FISSA DA ASSEGNARE ALLA TABELLA DEI SALDI
  -- ANCHE SE LA LINEA HA NATURA 1 LA UTILIZZIAMO ANCHE PER VOCI DI NATURA 2
  -- (TANTO LE DISPONIBILITA' STANNO SULLE VOCI, CHE SONO DIVERSE)
  aLINEA_S_ENTE := CNRCTB015.GETVAL02PERCHIAVE(0, 'LINEA_ATTIVITA_SPECIALE', 'LINEA_ATTIVITA_SPESA_ENTE');

   -- SE IL CDR è L'ACCENTRATORE DELLA VOCE OCCORRE SOMMARE, PER LA RIGA, LE COLONNE ACC INT E
   -- DEC INTERNO.
   -- SE IL CDR NON è L'ACCENTRATORE DELLA VOCE OCCORRE PRENDERE SOLO LA COLONNA DECENTRATO INTERNO

    If ADETTS.CD_CDR_ASSEGNATARIO = ACLASSIFICAZIONE.CDR_ACCENTRATORE And
       ACLASSIFICAZIONE.CDR_ACCENTRATORE Is Not Null Then
        IMP_DA_AGGIUNGERE_INT := Nvl(aDetts.tot_dec_int, 0) + Nvl(aDetts.tot_acc_int, 0);
        IMP_DA_AGGIUNGERE_EST := Nvl(aDetts.tot_dec_est, 0) + Nvl(aDetts.tot_acc_est, 0);
    Else
        IMP_DA_AGGIUNGERE_INT := Nvl(aDetts.tot_dec_int, 0);
        IMP_DA_AGGIUNGERE_EST := Nvl(aDetts.tot_dec_est, 0);
    End If;

--Dbms_Output.PUT_LINE ('IMPORTO DA AGGIUNGERE interno '||IMP_DA_AGGIUNGERE_int);
--Dbms_Output.PUT_LINE ('IMPORTO DA AGGIUNGERE esterno '||IMP_DA_AGGIUNGERE_est);

If IMP_DA_AGGIUNGERE_INT > 0 Then -- devo usare la voce con natura 1
   -- Leggo la voce_f su cui mettere i valori

   -- La ricerca è per:
   --   Esercizio
   --   tipo gestione
   --   tipo appartenenza
   --   cd_cds: codice del CDS di origine o del presidente dell'Area
   --   cd_natura = a seconda selle colonne del gestionale (Interna => Natura 1)
   --   cd_funzione = fissa a '01', unica utilizzabile
   --   ti_voce = 'E' (Sottoarticolo)
   --   cd_categoria = '1' fissa per tutti (anche per la SAC)
   --   cd_titolo_capitolo da configurazione (fisso a 1.01.1)

   Begin
     -- SELEZIONO LA VOCE PER LE SPESE INTERNE
     select   *
     into     aVoce
     from     voce_f
     Where    esercizio         = aEsercizio
	  and ti_gestione       = CNRCTB001.GESTIONE_SPESE
	  and ti_appartenenza   = CNRCTB001.APPARTENENZA_CNR
          and cd_proprio_voce   = aCodiceCDSSottoart
          and cd_cds            = aCDS_voce
          And CD_TITOLO_CAPITOLO = ACD_TIT_CAP_FISSO
          and cd_natura         = '1' -- (FONTI INTERNE)
          and cd_funzione       = '01'   -- DA SOSTITUIRE
          and ti_voce           = CNRCTB001.SOTTOARTICOLO
          and cd_categoria      = aCATEGORIA;

----------------------------- INSERIMENTO O UPDATE DEI SALDI ----------------------------
     aNuovoSaldo := null;
     aNuovoSaldo.ESERCIZIO                 := aesercizio;
     aNuovoSaldo.ESERCIZIO_RES             := aesercizio;
     aNuovoSaldo.CD_CENTRO_RESPONSABILITA  := ACDRENTE.CD_CENTRO_RESPONSABILITA;
     aNuovoSaldo.CD_LINEA_ATTIVITA         := aLINEA_S_ENTE;
     aNuovoSaldo.TI_APPARTENENZA           := CNRCTB001.APPARTENENZA_CNR;
     aNuovoSaldo.TI_GESTIONE               := CNRCTB001.GESTIONE_SPESE;
     aNuovoSaldo.CD_VOCE                   := aVoce.cd_voce;

     CNRCTB054.RESET_IMPORTI_SALDI (aNuovoSaldo);

     aNuovoSaldo.CD_ELEMENTO_VOCE          := aVoce.CD_ELEMENTO_VOCE;
     aNuovoSaldo.IM_STANZ_INIZIALE_A1      := IMP_DA_AGGIUNGERE_INT;
     aNuovoSaldo.DUVA                      := aTSNow;
     aNuovoSaldo.UTUV                      := aUtente;

     CNRCTB054.crea_aggiorna_saldi (aNuovoSaldo, '055.predispBilFinCNRInt_da_gest 1', 'N');

     --Aggiorno la tabella VOCE_F_SALDI_CMP
     Begin
       Select * into aSaldoCmp from voce_f_saldi_cmp
       Where CD_CDS = aCDSENTE.CD_UNITA_ORGANIZZATIVA
       And   ESERCIZIO = aNuovoSaldo.ESERCIZIO
       And   TI_APPARTENENZA=aNuovoSaldo.TI_APPARTENENZA
       And   TI_GESTIONE=aNuovoSaldo.TI_GESTIONE
       And   CD_VOCE=aNuovoSaldo.CD_VOCE
       And   TI_COMPETENZA_RESIDUO=CNRCTB054.TI_COMPETENZA
       For update nowait;

       CNRCTB054.aggiornaStanziamentoResidui(aSaldoCmp,aNuovoSaldo.IM_STANZ_INIZIALE_A1,aUtente,aTSNow);
     Exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Capitolo finanziario non trovato (a):'||aNuovoSaldo.CD_VOCE||
' per CDS '||aCDSENTE.CD_UNITA_ORGANIZZATIVA||', esercizio '||aNuovoSaldo.ESERCIZIO||', Appart. '||
aNuovoSaldo.TI_APPARTENENZA||', gestione '||aNuovoSaldo.TI_GESTIONE||', tipo '||CNRCTB054.TI_COMPETENZA);
     End;
   Exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('2.1 Voce non trovata per Esercizio: '||aesercizio||
      ', Titolo/Capitolo: '||ACD_TIT_CAP_FISSO||', CDS: '||aCDS_voce||
      ', Propria Voce: '||aCodiceCDSSottoart||', natura: 1, funzione: 01, cat.:'||aCATEGORIA);
   End;
End If;

    -- SECONDO GIRO PER LE SPESE ESTERNE

    If IMP_DA_AGGIUNGERE_EST > 0 Then

   -- RECUPERO LA VOCE DA USARE PER LE SPESE ESTERNE (NAT 2)

   Begin
     select   *
     into     aVoce
     from     voce_f
     Where    esercizio         = aEsercizio
	  and ti_gestione       = CNRCTB001.GESTIONE_SPESE
	  and ti_appartenenza   = CNRCTB001.APPARTENENZA_CNR
          and cd_proprio_voce   = aCodiceCDSSottoart
          and cd_cds            = aCDS_voce
          And CD_TITOLO_CAPITOLO = ACD_TIT_CAP_FISSO
          and cd_natura         = '2' -- (FONTI ESTERNE)
          and cd_funzione       = '01'   -- DA SOSTITUIRE
          and ti_voce           = CNRCTB001.SOTTOARTICOLO
          and cd_categoria      = aCATEGORIA;

----------------------------- INSERIMENTO O UPDATE DEI SALDI ----------------------------

     aNuovoSaldo := null;
     aNuovoSaldo.ESERCIZIO                 := aesercizio;
     aNuovoSaldo.ESERCIZIO_RES             := aesercizio;
     aNuovoSaldo.CD_CENTRO_RESPONSABILITA  := ACDRENTE.CD_CENTRO_RESPONSABILITA;
     aNuovoSaldo.CD_LINEA_ATTIVITA         := aLINEA_S_ENTE;
     aNuovoSaldo.TI_APPARTENENZA           := CNRCTB001.APPARTENENZA_CNR;
     aNuovoSaldo.TI_GESTIONE               := CNRCTB001.GESTIONE_SPESE;
     aNuovoSaldo.CD_VOCE                   := aVoce.cd_voce;

     CNRCTB054.RESET_IMPORTI_SALDI (aNuovoSaldo);

     aNuovoSaldo.CD_ELEMENTO_VOCE          := aVoce.CD_ELEMENTO_VOCE;
     aNuovoSaldo.IM_STANZ_INIZIALE_A1      := IMP_DA_AGGIUNGERE_EST;
     aNuovoSaldo.DUVA                      := aTSNow;
     aNuovoSaldo.UTUV                      := aUtente;

     CNRCTB054.crea_aggiorna_saldi (aNuovoSaldo, '055.predispBilFinCNRInt_da_gest 2', 'N');

     --Aggiorno la tabella VOCE_F_SALDI_CMP
     Begin
       Select * into aSaldoCmp from voce_f_saldi_cmp
       Where CD_CDS = aCDSENTE.CD_UNITA_ORGANIZZATIVA
       And   ESERCIZIO = aNuovoSaldo.ESERCIZIO
       And   TI_APPARTENENZA=aNuovoSaldo.TI_APPARTENENZA
       And   TI_GESTIONE=aNuovoSaldo.TI_GESTIONE
       And   CD_VOCE=aNuovoSaldo.CD_VOCE
       And   TI_COMPETENZA_RESIDUO=CNRCTB054.TI_COMPETENZA
       For update nowait;

       CNRCTB054.aggiornaStanziamentoResidui(aSaldoCmp,aNuovoSaldo.IM_STANZ_INIZIALE_A1,aUtente,aTSNow);
     Exception when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Capitolo finanziario non trovato (b):'||aNuovoSaldo.CD_VOCE);
     End;

   Exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('1. Voce non trovata per Esercizio: '||aesercizio||
      ', Titolo/Capitolo: '||ACD_TIT_CAP_FISSO||', CDS: '||aCDS_voce||
      ', Propria Voce: '||aCodiceCDSSottoart||', natura: 1, funzione: 01, cat.:'||ACATEGORIA);
   End;
End If;
End Loop;

-- FINE AGGREGAZIONE VERA E PROPRIA

  -- ******************************************************
  -- B I L A N C I O    C N R   P A R T E    E N T R A T E
  -- ******************************************************

  -- ELIMINATA !!! NON SI FA PIU' !!!


  -- Crea gli impegni per capitolo
  CNRCTB030.CREAIMPEGNICNR(aEsercizio, aCDR, aUtente);
 end;

-- stani fine nuova predisposizione bilfincnrint

 procedure inizializzaBilancioPreventivo(aEs number, aCdCDS varchar2, aUser varchar2) is
  aEnte unita_organizzativa%rowtype;
  aCDS unita_organizzativa%rowtype;
  aBP bilancio_preventivo%rowtype;
  aTSNow date;
 begin
  aCDS:=CNRCTB020.GETUOVALIDA(aEs,aCdCDS);
  -- se si tratta di SAC inserisce la testata del bilancio preventivo CNR
  if aCDS.cd_tipo_unita = CNRCTB020.TIPO_ENTE then
   select * into aEnte from unita_organizzativa where
        fl_cds = 'Y'
	and cd_tipo_unita = CNRCTB020.TIPO_ENTE;
   aTSNow:=sysdate;
   aBP.ESERCIZIO:=aEs;
   aBP.ti_appartenenza:=CNRCTB001.APPARTENENZA_CNR;
   aBP.cd_cds:=aEnte.cd_unita_organizzativa;
   aBP.STATO:=CNRCTB054.STATO_PREVENTIVO_INIZIALE;
   aBP.DACR:=aTSNow;
   aBP.UTCR:=aUser;
   aBP.DUVA:=aTSNow;
   aBP.UTUV:=aUser;
   aBP.PG_VER_REC:=1;
   CNRCTB054.ins_BILANCIO_PREVENTIVO (aBP);
  else
   -- inserisce la testata del bilancio preventivo CDS
   aTSNow:=sysdate;
   aBP.ESERCIZIO:=aEs;
   aBP.ti_appartenenza:=CNRCTB001.APPARTENENZA_CDS;
   aBP.cd_cds:=aCDS.cd_unita_organizzativa;
   aBP.STATO:=CNRCTB054.STATO_PREVENTIVO_INIZIALE;
   aBP.DACR:=aTSNow;
   aBP.UTCR:=aUser;
   aBP.DUVA:=aTSNow;
   aBP.UTUV:=aUser;
   aBP.PG_VER_REC:=1;
   CNRCTB054.ins_BILANCIO_PREVENTIVO (aBP);
  end if;
 end;

 procedure creaEsplSaldi(aEsercizio number, aCdCDS varchar2, aUser varchar2 ) is
  aBILCDS bilancio_preventivo%rowtype;
  aSaldo voce_f_saldi_cmp%rowtype;
  aTSNow date;
 begin
  if aEsercizio = 0 then
   return; -- Non gestito esercizio 0
  end if;
  aTSNow:=sysdate;
  select * into aBILCDS from bilancio_preventivo where
       esercizio = aEsercizio
   and cd_cds = aCdCDS;

  if aBILCDS.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR then
   for aV in (select * from voce_f where
                    esercizio = aEsercizio
				and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
				and fl_mastrino = 'Y') loop
	--inserimento competenza
    aSaldo:=null;
    aSaldo.ESERCIZIO:=aEsercizio;
    aSaldo.CD_CDS:=aCdCDS;
    aSaldo.TI_APPARTENENZA:=aV.ti_appartenenza;
    aSaldo.TI_GESTIONE:=aV.ti_gestione;
	aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_COMPETENZA;
    aSaldo.CD_VOCE:=aV.cd_voce;
    aSaldo.ORIGINE:=CNRCTB054.ORIGINE_INI;
    aSaldo.FL_SOLA_LETTURA:='N';
    aSaldo.IM_STANZ_INIZIALE_A1:=0;
    aSaldo.IM_STANZ_INIZIALE_A2:=0;
    aSaldo.IM_STANZ_INIZIALE_A3:=0;
    aSaldo.VARIAZIONI_PIU:=0;
    aSaldo.VARIAZIONI_MENO:=0;
    aSaldo.IM_OBBLIG_IMP_ACR:=0;
    aSaldo.IM_MANDATI_REVERSALI:=0;
    aSaldo.IM_PAGAMENTI_INCASSI:=0;
    aSaldo.DACR:=aTSNow;
    aSaldo.UTCR:=aUser;
    aSaldo.DUVA:=aTSNow;
    aSaldo.UTUV:=aUser;
    aSaldo.PG_VER_REC:=1;
	begin
 	 CNRCTB054.INS_VOCE_F_SALDI_CMP(aSaldo);
    exception when dup_val_on_index then
	 null;
	end;

    -- inserimento residuo
     aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_RESIDUI;
	begin
 	 CNRCTB054.INS_VOCE_F_SALDI_CMP(aSaldo);
    exception when dup_val_on_index then
	 null;
	end;
   end loop;
  else -- Parte CDS
   for aV in (select * from voce_f where
                    esercizio = aEsercizio
				and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
				and fl_mastrino = 'Y'
				and (
				          ti_gestione = CNRCTB001.GESTIONE_SPESE
				     and  (   cd_cds is null
					       or cd_cds = aCdCDS
					      )
					 or   ti_gestione = CNRCTB001.GESTIONE_ENTRATE
					)
				) loop
	--inserimento competenza
    aSaldo:=null;
    aSaldo.ESERCIZIO:=aEsercizio;
    aSaldo.CD_CDS:=aCdCDS;
    aSaldo.TI_APPARTENENZA:=aV.ti_appartenenza;
    aSaldo.TI_GESTIONE:=aV.ti_gestione;
	aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_COMPETENZA;
    aSaldo.CD_VOCE:=aV.cd_voce;
    aSaldo.ORIGINE:=CNRCTB054.ORIGINE_INI;
    aSaldo.FL_SOLA_LETTURA:='N';
    aSaldo.IM_STANZ_INIZIALE_A1:=0;
    aSaldo.IM_STANZ_INIZIALE_A2:=0;
    aSaldo.IM_STANZ_INIZIALE_A3:=0;
    aSaldo.VARIAZIONI_PIU:=0;
    aSaldo.VARIAZIONI_MENO:=0;
    aSaldo.IM_OBBLIG_IMP_ACR:=0;
    aSaldo.IM_MANDATI_REVERSALI:=0;
    aSaldo.IM_PAGAMENTI_INCASSI:=0;
    aSaldo.DACR:=aTSNow;
    aSaldo.UTCR:=aUser;
    aSaldo.DUVA:=aTSNow;
    aSaldo.UTUV:=aUser;
    aSaldo.PG_VER_REC:=1;
	begin
 	 CNRCTB054.INS_VOCE_F_SALDI_CMP(aSaldo);
    exception when dup_val_on_index then
	 null;
	end;
   end loop;
  end if;
 end;

/*
 procedure job_predispone_bil_fin_cnr(job number, pg_exec number, next_date date, aEsercizio number, aUtente varchar2) is
 begin
  IBMUTL210.logStartExecutionUpd(pg_exec, LOG_TIPO_PREDBILFIN,job,'Richista utente:'||aUtente, 'Produzione bilancio preventivo CNR. Start:'||to_char(sysdate,'YYYY/MM/DD HH-MI-SS'));
  begin
   predisponeBilFinCNRInt(aEsercizio, aUtente);
   commit;
   -- Messaggio di operazione completata ad utente
   IBMUTL205.LOGINF('Produzione bilancio preventivo CNR','Produzione bilancio preventivo CNR '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS'),'Operazione completata con successo',aUtente);
  exception when others then
   rollback;
   -- Messaggio di attenzione ad utente
   IBMUTL205.LOGWAR('Produzione bilancio preventivo CNR','Produzione bilancio preventivo CNR '||to_char(sysdate,'DD/MM/YYYY HH:MI:SS')||' (pg_exec='||pg_exec||')',DBMS_UTILITY.FORMAT_ERROR_STACK,aUtente);
  end;
 end;
*/
 -- Batchizzazione della predisposizione del bilancio preventivo finanziario
 procedure predisponeBilFinCNR(aEsercizio varchar2, aUtente varchar2) is
  aProcedure varchar2(4000);
 begin
  aProcedure:='CNRCTB055.job_predispone_bil_fin_cnr(job, pg_exec, next_date, '||aEsercizio||', '''||aUtente||''');';
  IBMUTL210.CREABATCHDINAMICO (
   'Produzione del bilancio preventivo CNR',
   aProcedure,
   aUtente
  );
  IBMUTL001.DEFERRED_COMMIT;
  IBMERR001.RAISE_ERR_GENERICO('Operazione sottomessa per esecuzione. Al completamento l''utente riceverà un messaggio di notifica dello stato dell''operazione');
 end;

 -- Batchizzazione della predisposizione del NUOVO bilancio preventivo finanziario
 procedure predispBilFinCNR_DA_GEST(aEsercizio NUMBER, ACdR varchar2, aUtente varchar2) is
  aProcedure varchar2(4000);
 begin
   predispBilFinCNRInt_da_gest(aEsercizio, acdr, aUtente);
 End;


 procedure creaEsplSaldi(aVoceF voce_f%rowtype) is
  aSaldo voce_f_saldi_cmp%rowtype;
  aTSNow date;
  aCDSENTE unita_organizzativa%rowtype;
  aBilPrev bilancio_preventivo%rowtype;
 begin
  if aVoceF.esercizio = 0 then
   return;
  end if; -- Non gestito esercizio 0
  aTSNow:=sysdate;

  if aVoceF.fl_mastrino is null or aVoceF.fl_mastrino != 'Y' then
   return;
  end if;

  if aVoceF.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR then
    -- ATTENZIONE !!!!! non si possono utilizzare le viste V_XXXX_VALIDX per STO per potere
    -- invocare tale metodo da trigger AI su ESERCIZIO


    select * into aCDSENTE from unita_organizzativa where
           esercizio_inizio <= aVoceF.esercizio
	   and esercizio_fine >= aVoceF.esercizio
	   and fl_cds = 'Y'
	   and cd_tipo_unita = CNRCTB020.TIPO_ENTE;

	begin
     -- Verifica che ci sia la testata in bilancio_preventivo
	  select * into aBilPrev from bilancio_preventivo where
	         esercizio = aVoceF.esercizio
		 and cd_cds = aCDSENTE.cd_unita_organizzativa
         and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;

     --inserimento competenza
      aSaldo:=null;
      aSaldo.ESERCIZIO:=aVoceF.esercizio;
      aSaldo.CD_CDS:=aCDSENTE.cd_unita_organizzativa;
      aSaldo.TI_APPARTENENZA:=aVoceF.ti_appartenenza;
      aSaldo.TI_GESTIONE:=aVoceF.ti_gestione;
      aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_COMPETENZA;
      aSaldo.CD_VOCE:=aVoceF.cd_voce;
      aSaldo.ORIGINE:=CNRCTB054.ORIGINE_INI;
      aSaldo.FL_SOLA_LETTURA:='N';
      aSaldo.IM_STANZ_INIZIALE_A1:=0;
      aSaldo.IM_STANZ_INIZIALE_A2:=0;
      aSaldo.IM_STANZ_INIZIALE_A3:=0;
      aSaldo.VARIAZIONI_PIU:=0;
      aSaldo.VARIAZIONI_MENO:=0;
      aSaldo.IM_OBBLIG_IMP_ACR:=0;
      aSaldo.IM_MANDATI_REVERSALI:=0;
      aSaldo.IM_PAGAMENTI_INCASSI:=0;
      aSaldo.DACR:=aTSNow;
      aSaldo.UTCR:=aVoceF.dacr;
      aSaldo.DUVA:=aTSNow;
      aSaldo.UTUV:=aVoceF.dacr;
      aSaldo.PG_VER_REC:=1;
 	  begin
	   CNRCTB054.INS_VOCE_F_SALDI_CMP(aSaldo);
      exception when dup_val_on_index then
	   null;
	  end;

     -- inserimento residuo
      aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_RESIDUI;
 	  begin
	   CNRCTB054.INS_VOCE_F_SALDI_CMP(aSaldo);
      exception when dup_val_on_index then
	   null;
	  end;
    exception when NO_DATA_FOUND then
	 null;
	end;
  else -- Parte CDS
   -- ATTENZIONE !!!!! non si possono utilizzare le viste V_XXXX_VALIDX per STO per potere
   -- invocare tale metodo da trigger AI su ESERCIZIO
   for aCDS in (select * from unita_organizzativa where
           esercizio_inizio <= aVoceF.esercizio
	   and esercizio_fine >= aVoceF.esercizio
	   and fl_cds = 'Y'
	   and cd_tipo_unita != CNRCTB020.TIPO_ENTE
	   and (
				          aVoceF.ti_gestione = CNRCTB001.GESTIONE_SPESE
				     and  (   aVoceF.cd_cds is null
					       or aVoceF.cd_cds = cd_unita_organizzativa
					      )
					 or   aVoceF.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
			)
   ) loop
    begin
	  begin
	   -- Verifica che ci sia la testata in bilancio_preventivo
	   select * into aBilPrev from bilancio_preventivo where
	         esercizio = aVoceF.esercizio
		 and cd_cds = aCDS.cd_unita_organizzativa
         and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS;

       --inserimento competenza
       aSaldo:=null;
       aSaldo.ESERCIZIO:=aVoceF.esercizio;
       aSaldo.CD_CDS:=aCDS.cd_unita_organizzativa;
       aSaldo.TI_APPARTENENZA:=aVoceF.ti_appartenenza;
       aSaldo.TI_GESTIONE:=aVoceF.ti_gestione;
       aSaldo.TI_COMPETENZA_RESIDUO:=CNRCTB054.TI_COMPETENZA;
       aSaldo.CD_VOCE:=aVoceF.cd_voce;
       aSaldo.ORIGINE:=CNRCTB054.ORIGINE_INI;
       aSaldo.FL_SOLA_LETTURA:='N';
       aSaldo.IM_STANZ_INIZIALE_A1:=0;
       aSaldo.IM_STANZ_INIZIALE_A2:=0;
       aSaldo.IM_STANZ_INIZIALE_A3:=0;
       aSaldo.VARIAZIONI_PIU:=0;
       aSaldo.VARIAZIONI_MENO:=0;
       aSaldo.IM_OBBLIG_IMP_ACR:=0;
       aSaldo.IM_MANDATI_REVERSALI:=0;
       aSaldo.IM_PAGAMENTI_INCASSI:=0;
       aSaldo.DACR:=aTSNow;
       aSaldo.UTCR:=aVoceF.dacr;
       aSaldo.DUVA:=aTSNow;
       aSaldo.UTUV:=aVoceF.dacr;
       aSaldo.PG_VER_REC:=1;
 	   begin
	    CNRCTB054.INS_VOCE_F_SALDI_CMP(aSaldo);
       exception when dup_val_on_index then
	    null;
	   end;
      exception when NO_DATA_FOUND then
	   null;
	  end;
	 end;
   end loop;
  end if;
 end;

 procedure eliminaEsplSaldi(aVoceF voce_f%rowtype) is
  aNum number;
 begin

  if aVoceF.fl_mastrino is null or aVoceF.fl_mastrino != 'Y' then
   return;
  end if;
  select count(*) into aNum from voce_f_saldi_cmp where
        esercizio = aVoceF.esercizio
    and ti_appartenenza = aVoceF.ti_appartenenza
    and ti_gestione = aVoceF.ti_gestione
    and ti_competenza_residuo in (CNRCTB054.TI_COMPETENZA, CNRCTB054.TI_RESIDUI)
    and cd_voce = aVoceF.cd_voce
    and (
        IM_STANZ_INIZIALE_A1!=0
     or IM_STANZ_INIZIALE_A2!=0
     or IM_STANZ_INIZIALE_A3!=0
     or VARIAZIONI_PIU!=0
     or VARIAZIONI_MENO!=0
     or IM_OBBLIG_IMP_ACR!=0
     or IM_MANDATI_REVERSALI!=0
     or IM_PAGAMENTI_INCASSI!=0
    );
  if aNum > 0 then
   IBMERR001.RAISE_ERR_GENERICO('Capitolo finanziario non eliminabile perchè esistono saldi diversi da 0');
  else
   delete from voce_f_saldi_cmp where
        esercizio = aVoceF.esercizio
    and ti_appartenenza = aVoceF.ti_appartenenza
    and ti_gestione = aVoceF.ti_gestione
    and ti_competenza_residuo in (CNRCTB054.TI_COMPETENZA, CNRCTB054.TI_RESIDUI)
    and cd_voce = aVoceF.cd_voce;
  end if;
 end;

 procedure postsalvdett(aEs number, aCdCds varchar2, aTiAppartenenza char, aTiGestione char, aCdVoce varchar2, aTi_competenza_residuo char, aUser varchar2) is
 begin
	  setcassainiassucc(aEs, aCdCds, aTiAppartenenza, aTiGestione, aCdVoce, aTi_competenza_residuo, aUser);
 end;

 procedure setcassainiassucc(aEs number, aCd_cds varchar2, aTi_appartenenza char, aTi_gestione char, aCd_voce varchar2,  aTi_competenza_residuo char, aUser varchar2) is
	aVoceF_configS varchar2(1000);
	aVoceF_configE varchar2(1000);
	aVoceF voce_f_saldi_cmp%rowtype;
	aVoceFControparte voce_f_saldi_cmp%rowtype;
	aVoceS voce_f_saldi_cmp%rowtype;
	aVoceE voce_f_saldi_cmp%rowtype;
	aBilancio bilancio_preventivo%rowtype;
	aEsLock esercizio%rowtype;
	aTSNow date;
    aEsIni number(4);
 begin
		 aTSNow:= SYSDATE;
		 -- Controlla il ti_appartenenza: se è 'C'
		 if aTi_competenza_residuo = CNRCTB054.TI_COMPETENZA then
          -- Se l'esercizio è quello di partenza esce senza sollevare eccezioni
		  if aEs = CNRCTB008.ESERCIZIO_PARTENZA then
		   return;
		  end if;

		  -- Controlla il ti_appartenenza: 'C' CNR
		  if aTi_appartenenza = CNRCTB001.APPARTENENZA_CNR then

				 -- Carica il valore per la Voce_f contenuta in CONFIGURAZIONE_CNR, per il tipo 'S'
				 aVoceF_configS:= CNRCTB015.GETVAL01PERCHIAVE(aEs, 'VOCEF_SPECIALE', 'AVANZO_S_CNR');

				 -- Carica il valore per la Voce_f contenuta in CONFIGURAZIONE_CNR, per il tipo 'E'
				 aVoceF_configE:= CNRCTB015.GETVAL01PERCHIAVE(aEs, 'VOCEF_SPECIALE', 'AVANZO_E_CNR');

				 -- Controlla che le voci presenti nella tabella CONFIGURAZIONE_CNR esistano effettivamente
				 begin
				 	  select * into aVoceS
						   from voce_f_saldi_cmp
						   where CD_CDS      = aCd_cds
						   and	 ESERCIZIO   = aEs
						   and	 TI_APPARTENENZA = aTi_appartenenza
						   and 	 TI_GESTIONE = CNRCTB001.GESTIONE_SPESE
						   and 	 CD_VOCE	 = aVoceF_configS
						   and	 TI_COMPETENZA_RESIDUO = aTi_competenza_residuo
				    	   for update nowait;
				 exception when NO_DATA_FOUND then
				   IBMERR001.RAISE_ERR_GENERICO('Il capitolo ' || aVoceF_configS ||' definito in CONFIGURAZIONE_CNR non esiste.');
				 end;

				 begin
				 	  select * into aVoceE
						   from voce_f_saldi_cmp
						   where CD_CDS      = aCd_cds
						   and	 ESERCIZIO   = aEs
						   and	 TI_APPARTENENZA = aTi_appartenenza
						   and 	 TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE
						   and 	 CD_VOCE	 = aVoceF_configE
						   and	 TI_COMPETENZA_RESIDUO = aTi_competenza_residuo
				    	   for update nowait;
				 exception when NO_DATA_FOUND then
				   IBMERR001.RAISE_ERR_GENERICO('Il capitolo ' || aVoceF_configE ||'  definito in CONFIGURAZIONE_CNR non esiste.');
				 end;


			 	 -- Se il tipo gestione = 'E', controlla che si tratti di una operazione su di un
				 --  cd_voce configurato per la gestione Avanzo/Disavanzo.
				 --	In caso affermativo, controlla che la controparte presente sulla tabella voce_f_saldi_cmp
				 --	non sia stata a sua volta valorizzata: in tal caso invia un messaggio di errore.
			 	 if aTi_gestione = CNRCTB001.GESTIONE_ENTRATE AND aCd_voce=aVoceF_configE then

				   if (aVoceS.IM_STANZ_INIZIALE_A1 +
		     	       aVoceS.VARIAZIONI_PIU 	  -
					   aVoceS.VARIAZIONI_MENO) <>0 then

		           	   IBMERR001.RAISE_ERR_GENERICO('Attenzione: non è possibile impostare contemporaneamente i capitoli relativi ad Avanzo e Disavanzo di amministrazione. Controllare il capitolo di Spesa ' || aVoceF_configS);
				   end if;
				 end if; -- fine if su gestione tipo 'E'

				 if aTi_gestione = CNRCTB001.GESTIONE_SPESE AND aCd_voce=aVoceF_configS then -- Ti_gestione = 'S'

				   if (aVoceE.IM_STANZ_INIZIALE_A1 +
		     	       aVoceE.VARIAZIONI_PIU 	  -
					   aVoceE.VARIAZIONI_MENO) <>0 then
		           	   IBMERR001.RAISE_ERR_GENERICO('Attenzione: non è possibile impostare contemporaneamente i capitoli relativi ad Avanzo e Disavanzo di amministrazione. Controllare il capitolo di Entrata ' || aVoceF_configE);
				   end if;
				 end if; -- fine if su gestione tipo 'S'


		  end if; -- Fine BLOCCO GESTIONE CNR

		  -- Controlla il ti_appartenenza: 'D' CDS
		  if aTi_appartenenza = CNRCTB001.APPARTENENZA_CDS then

		 	-- Carica e locka il BILANCIO_PREVENTIVO per il Cds, Esercizio, ti_appartenenza di riferimento
			SELECT * INTO aBilancio FROM BILANCIO_PREVENTIVO
			WHERE CD_CDS          = aCd_cds
			AND   ESERCIZIO		  = aEs
			AND   TI_APPARTENENZA = aTi_appartenenza FOR UPDATE NOWAIT;

            -- Carica il valore per la Voce_f contenuta in CONFIGURAZIONE_CNR, per il tipo 'S'
            aVoceF_configS:= CNRCTB015.GETVAL01PERCHIAVE(aEs, 'VOCEF_SPECIALE', 'AVANZO_S_CDS');

            -- Carica il valore per la Voce_f contenuta in CONFIGURAZIONE_CNR, per il tipo 'E'
            aVoceF_configE:= CNRCTB015.GETVAL01PERCHIAVE(aEs, 'VOCEF_SPECIALE', 'AVANZO_E_CDS');

            -- Controlla che le voci presenti nella tabella CONFIGURAZIONE_CNR esistano effettivamente
            begin
         	  select * into aVoceS
        		   from voce_f_saldi_cmp
        		   where CD_CDS      = aCd_cds
        		   and	 ESERCIZIO   = aEs
        		   and	 TI_APPARTENENZA = aTi_appartenenza
        		   and 	 TI_GESTIONE = CNRCTB001.GESTIONE_SPESE
        		   and 	 CD_VOCE	 = aVoceF_configS
        		   and	 TI_COMPETENZA_RESIDUO = aTi_competenza_residuo
            	   for update nowait;
            exception when NO_DATA_FOUND then
             IBMERR001.RAISE_ERR_GENERICO('Il capitolo ' || aVoceF_configS ||' definito in CONFIGURAZIONE_CNR non esiste.');
            end;

            begin
         	  select * into aVoceE
        		   from voce_f_saldi_cmp
        		   where CD_CDS      = aCd_cds
        		   and	 ESERCIZIO   = aEs
        		   and	 TI_APPARTENENZA = aTi_appartenenza
        		   and 	 TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE
        		   and 	 CD_VOCE	 = aVoceF_configE
        		   and	 TI_COMPETENZA_RESIDUO = aTi_competenza_residuo
            	   for update nowait;
            exception when NO_DATA_FOUND then
             IBMERR001.RAISE_ERR_GENERICO('Il capitolo ' || aVoceF_configE ||'  definito in CONFIGURAZIONE_CNR non esiste.');
          	end;

    	    -- Controlla che la voceF passata corrisponda ad uno dei capitoli impostati in CONFIGURAZIONE_CNR,
	        --	 altrimenti non fa nulla.
	        if aCd_voce=aVoceF_configE OR aCd_voce=aVoceF_configS then

		 	 -- Se il tipo gestione = 'E', controlla che si tratti di una operazione su di un
			 --  cd_voce configurato per la gestione Avanzo/Disavanzo.
			 --	In caso affermativo, controlla che la controparte presente sulla tabella voce_f_saldi_cmp
			 --	non sia stata a sua volta valorizzata: in tal caso invia un messaggio di errore.
		 	 if aTi_gestione = CNRCTB001.GESTIONE_ENTRATE AND aCd_voce=aVoceF_configE then

			   if (aVoceS.IM_STANZ_INIZIALE_A1 +
	     	       aVoceS.VARIAZIONI_PIU 	  -
				   aVoceS.VARIAZIONI_MENO) <>0 then

	           	   IBMERR001.RAISE_ERR_GENERICO('Attenzione: non è possibile impostare contemporaneamente i capitoli relativi ad Avanzo e Disavanzo di Gestione. Controllare il capitolo di Spesa ' || aVoceF_configS);
			   end if;
			 end if; -- fine if su gestione tipo 'E'

			 if aTi_gestione = CNRCTB001.GESTIONE_SPESE AND aCd_voce=aVoceF_configS then -- Ti_gestione = 'S'

			   if (aVoceE.IM_STANZ_INIZIALE_A1 +
	     	       aVoceE.VARIAZIONI_PIU 	  -
				   aVoceE.VARIAZIONI_MENO) <>0 then
	           	   IBMERR001.RAISE_ERR_GENERICO('Attenzione: non è possibile impostare contemporaneamente i capitoli relativi ad Avanzo e Disavanzo di Gestione. Controllare il capitolo di Entrata ' || aVoceF_configE);
			   end if;
			 end if; -- fine if su gestione tipo 'S'

		     begin
			  select esercizio_inizio into aEsIni from unita_organizzativa where cd_unita_organizzativa=aCd_cds;
             exception when  NO_DATA_FOUND then
			  IBMERR001.RAISE_ERR_GENERICO('Unità organizzativa non definita:'||aCd_cds);
		     end;
             -- Se l'esercizio precedente NON ERA DEFINITO per il CDS in processo esce senza segnalare eccezioni
		     if aEsIni=aEs then
		      return;
	         end if;

             -- Se l'esercizio precedente è chiuso per il CDS in processo ritorno senza segnalare eccezioni
		     if CNRCTB008.ISESERCIZIOCHIUSO(aEs-1,aCd_cds) then
              return;
		     end if;

			 -- Controlla lo stato del BILANCIO_PREVENTIVO: se è diverso da B/C, (predisp./approv), allora esce senza fare nulla.
			 if aBilancio.STATO = CNRCTB054.STATO_PREVENTIVO_PREDISPOSTO OR aBilancio.STATO = CNRCTB054.STATO_PREVENTIVO_APPROVATO then

					   -- Locka la riga di ESERCIZIO che dovrà essere aggiornata
					   SELECT * INTO aEsLock FROM ESERCIZIO
					   WHERE CD_CDS    = aCd_cds
					   AND 	 ESERCIZIO = aEs
					   FOR UPDATE NOWAIT;

					   -- Carica e locka la Voce_f relativa ai parametri passati
						select * into aVoceF
						from voce_f_saldi_cmp
					   	where CD_CDS        = aCd_cds
					   	and ESERCIZIO   	= aEs
					   	and	TI_APPARTENENZA = aTi_appartenenza
					   	and TI_GESTIONE 	= aTi_gestione
					   	and CD_VOCE	 		= aCd_voce
					   	and	TI_COMPETENZA_RESIDUO = aTi_competenza_residuo FOR UPDATE NOWAIT;

					   -- Scrive l'importo nell'esercizio del Cds
					   -- Gestione Entrate
					   if aTi_gestione = CNRCTB001.GESTIONE_ENTRATE then
						   UPDATE esercizio
						   SET IM_CASSA_INIZIALE = (aVoceF.IM_STANZ_INIZIALE_A1 +
						   	   					    aVoceF.VARIAZIONI_PIU 	   	-
						 						    aVoceF.VARIAZIONI_MENO),	-- Gestione di tipo 'E', (Avanzo == positivo)
							   DUVA	= aTSNow,
							   UTUV = aUser,
							   PG_VER_REC = pg_ver_rec + 1
						   WHERE CD_CDS    = aCd_cds
						   AND 	 ESERCIZIO = aEs;
					   else -- Gestione Spese
						   UPDATE esercizio
						   SET IM_CASSA_INIZIALE = (aVoceF.IM_STANZ_INIZIALE_A1 +
						   	   					    aVoceF.VARIAZIONI_PIU 	   	-
						 						    aVoceF.VARIAZIONI_MENO) *(-1), -- Gestione di tipo 'S', (disavanzo), quindi l'importo è negativo
							   DUVA	= aTSNow,
							   UTUV = aUser,
							   PG_VER_REC = pg_ver_rec + 1
						   WHERE CD_CDS    = aCd_cds
						   AND 	 ESERCIZIO = aEs;
					   end if;
			 end if; -- Fine if di controllo sullo STATO del BILANCIO_PREVENTIVO
			end if; --Fine if di controllo su cd_Voce che deve appartenere ad uno di quelli presenti in CONFIGURAZIONE_CNR

		  end if; -- Fine BLOCCO CDS
		end if; -- Fine if di controllo su ti_competenza_residuo
	end;
end;
