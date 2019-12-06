--------------------------------------------------------
--  DDL for Package Body CNRCTB060
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB060" is

 -- Imposta gli importi sul dettaglio di PDG da creare

 procedure setImporto(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype,
                      aDestS in out pdg_preventivo_spe_det%rowtype) is
--  isTempoDeterminato boolean;
 begin

/*
  isTempoDeterminato:=false;

  if aASSCDPLA.ti_rapporto = CDP_TI_RAPP_DETERMINATO then
   isTempoDeterminato:=true;
  end if;

  -- Se si tratta di tempo determinato, gli importi vanno su una sola colonna nel primo anno
  if isTempoDeterminato then
   aDestS.IM_RO_CSS_ALTRI_COSTI:=aASSCDPLA.im_a1;
   return;
  end if;
*/

  if aDestS.categoria_dettaglio=CNRCTB050.DETTAGLIO_SCARICO then
   aDestS.IM_RL_CCS_SPESE_OGC_ALTRA_UO:=aASSCDPLA.im_a1;
  else
   aDestS.IM_RK_CCS_SPESE_OGC:=aASSCDPLA.im_a1;
  end if;
  aDestS.IM_RH_CCS_COSTI:=aASSCDPLA.im_a1;

  if aDestS.categoria_dettaglio=CNRCTB050.DETTAGLIO_SCARICO then
   aDestS.IM_RAF_A2_SPESE_OGC_ALTRA_UO:=aASSCDPLA.im_a2;
  else
   aDestS.IM_RAE_A2_SPESE_OGC:=aASSCDPLA.im_a2;
  end if;
  aDestS.IM_RAA_A2_COSTI_FINALI:=aASSCDPLA.im_a2;

  if aDestS.categoria_dettaglio=CNRCTB050.DETTAGLIO_SCARICO then
   aDestS.IM_RAO_A3_SPESE_OGC_ALTRA_UO:=aASSCDPLA.im_a3;
  else
   aDestS.IM_RAN_A3_SPESE_OGC:=aASSCDPLA.im_a3;
  end if;
  aDestS.IM_RAH_A3_COSTI_FINALI:=aASSCDPLA.im_a3;
 end;

 procedure setImportoDettaglioCollegato(aDestSColl in out pdg_preventivo_spe_det%rowtype, aDestS pdg_preventivo_spe_det%rowtype) is
 begin
   aDestSColl.IM_RU_SPESE_COSTI_ALTRUI:=aDestS.IM_RL_CCS_SPESE_OGC_ALTRA_UO;
   aDestSColl.IM_RAG_A2_SPESE_COSTI_ALTRUI:=aDestS.IM_RAF_A2_SPESE_OGC_ALTRA_UO;
   aDestSColl.IM_RAP_A3_SPESE_COSTI_ALTRUI:=aDestS.IM_RAO_A3_SPESE_OGC_ALTRA_UO;
 end;

 procedure setImportoTFR(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype, aDestS in out pdg_preventivo_spe_det%rowtype) is
 begin
  aDestS.IM_RO_CSS_ALTRI_COSTI:=aASSCDPLA.im_a1;
  aDestS.IM_RAA_A2_COSTI_FINALI:=aASSCDPLA.im_a2;
  aDestS.IM_RAH_A3_COSTI_FINALI:=aASSCDPLA.im_a3;
 end;

-- Funzione di accantonamento dei rotti di scarico

 procedure fill_round_table(aASSCDPLA V_CDP_SPACCATO_CDR_LA_VOCE%rowtype, aTSNow date, aUser varchar2) is
  aAssCdpRnd ASS_CDP_ROUND%rowtype;
 begin
    if
	     aASSCDPLA.IM_RND_A1 = 0
	 and aASSCDPLA.IM_RND_A2 = 0
	 and aASSCDPLA.IM_RND_A3 = 0
	then
     return;
	end if;
	aAssCdpRnd:=null;
    aAssCdpRnd.ESERCIZIO:=aASSCDPLA.ESERCIZIO;
    aAssCdpRnd.CD_CDR_ROOT:=aASSCDPLA.CD_CDR_ROOT;
    aAssCdpRnd.TI_APPARTENENZA:=aASSCDPLA.TI_APPARTENENZA;
    aAssCdpRnd.TI_GESTIONE:=aASSCDPLA.TI_GESTIONE;
    aAssCdpRnd.CD_ELEMENTO_VOCE:=aASSCDPLA.CD_ELEMENTO_VOCE;
    aAssCdpRnd.TI_RAPPORTO:=aASSCDPLA.TI_RAPPORTO;
    begin
	 select * into aAssCdpRnd from ass_cdp_round where
	      ESERCIZIO=aAssCdpRnd.esercizio
      and CD_CDR_ROOT=aAssCdpRnd.cd_cdr_root
      and TI_APPARTENENZA=aAssCdpRnd.ti_appartenenza
      and TI_GESTIONE=aAssCdpRnd.ti_gestione
      and CD_ELEMENTO_VOCE=aAssCdpRnd.cd_elemento_voce
      and TI_RAPPORTO=aAssCdpRnd.ti_rapporto
	  for update nowait;
     update ass_cdp_round set
      IM_ARR_A1=IM_ARR_A1+aASSCDPLA.im_rnd_a1,
      IM_ARR_A2=IM_ARR_A2+aASSCDPLA.im_rnd_a2,
      IM_ARR_A3=IM_ARR_A3+aASSCDPLA.im_rnd_a3
	 where
	      ESERCIZIO=aAssCdpRnd.esercizio
      and CD_CDR_ROOT=aAssCdpRnd.cd_cdr_root
      and TI_APPARTENENZA=aAssCdpRnd.ti_appartenenza
      and TI_GESTIONE=aAssCdpRnd.ti_gestione
      and CD_ELEMENTO_VOCE=aAssCdpRnd.cd_elemento_voce
      and TI_RAPPORTO=aAssCdpRnd.ti_rapporto;
	exception when NO_DATA_FOUND then
     aAssCdpRnd.IM_ARR_A1:=aASSCDPLA.im_rnd_a1;
     aAssCdpRnd.IM_ARR_A2:=aASSCDPLA.im_rnd_a2;
     aAssCdpRnd.IM_ARR_A3:=aASSCDPLA.im_rnd_a3;
     aAssCdpRnd.IM_ARR_A1_NON_DISTR:=0;
     aAssCdpRnd.IM_ARR_A2_NON_DISTR:=0;
     aAssCdpRnd.IM_ARR_A3_NON_DISTR:=0;
     aAssCdpRnd.DACR:=aTSNow;
     aAssCdpRnd.UTUV:=aUser;
     aAssCdpRnd.UTCR:=aUser;
     aAssCdpRnd.DUVA:=aTSNow;
     aAssCdpRnd.PG_VER_REC:=1;
     ins_ASS_CDP_ROUND(aAssCdpRnd);
	end;
 end;

 procedure scaricaCDPSuPdg(aEsercizio number, aCdCdr varchar2, aUser varchar2) is
  aCDRRUO cdr%rowtype;
  aUOAfferenza unita_organizzativa%rowtype;
  aPgDettaglio number(10);
  aPgDettaglioColl number(10);
  aDestS pdg_preventivo_spe_det%rowtype;
  aDestSColl pdg_preventivo_spe_det%rowtype;
  aTSNow date;
  aAss ass_cdp_pdg%rowtype;
  aCDRPersonale cdr%rowtype;
  isCDRInUOPersonale boolean;
  aLASAUOP linea_attivita%rowtype;
  aNum number(8);
  aVoceTFR elemento_voce%rowtype;
  aVoceONERICNR elemento_voce%rowtype;
--  isTempoDeterminato boolean;
  aLATmp linea_attivita%rowtype;
  isDettRedistrRottiFound boolean;
  isProcessato boolean;
  matricola_err         VARCHAR2(3000);
 begin
  aTSNow:=sysdate;

  -- Leggo il CDR

  aCDRRUO:=CNRCTB020.GETCDRVALIDO(aEsercizio, aCdCdr);

  if to_number(aCDRRUO.cd_proprio_cdr) != 0 then
   IBMERR001.RAISE_ERR_GENERICO('Operazione permessa solo su cdr di tipo RUO!');
  end if;

  -- Lock del PDG del CDR in processo

  CNRCTB050.LOCKPDG(aEsercizio, aCdCdr);

  -- Leggo il CDR del personale e lock del PDG

  aCDRPersonale:=CNRCTB020.GETCDRPERSONALE(aEsercizio);
  CNRCTB050.LOCKPDG(aEsercizio, aCDRPersonale.cd_centro_responsabilita);

  -- Verifico che l'aggregato del CDR del personale NON sia chiuso in stato B

  if CNRCTB050.checkStatoAggregato(aEsercizio,
                                   aCDRPersonale.cd_centro_responsabilita,
								   CNRCTB050.STATO_AGGREGATO_FINALE) = 'Y'
  then
   IBMERR001.RAISE_ERR_GENERICO('L''agregato del CDR del personale risulta chiuso. Non è possibile effettuare lo scarico dei costi del personale');
  end if;

  if aCdCdr = aCDRPersonale.cd_centro_responsabilita then
   select count(*) into aNum from ass_cdp_pdg a, pdg_preventivo_spe_det b where
         a.esercizio = aEsercizio
	and  b.esercizio = a.esercizio
	and  b.cd_centro_responsabilita = a.cd_centro_responsabilita
	and  b.cd_linea_attivita = a.cd_linea_attivita
	and  b.ti_appartenenza = a.ti_appartenenza
	and  b.ti_gestione = a.ti_gestione
	and  b.cd_elemento_voce = a.cd_elemento_voce
	and  b.pg_spesa = a.pg_spesa
    and  b.origine=CNRCTB050.ORIGINE_STIPENDI
    and  b.categoria_dettaglio in (CNRCTB050.DETTAGLIO_SINGOLO,CNRCTB050.DETTAGLIO_SCARICO)
    and  a.cd_centro_responsabilita in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		        esercizio = aEsercizio
		    and cd_cdr_root =  aCdCdr
		   );
  else
   select count(*) into aNum from ass_cdp_pdg where
        esercizio = aEsercizio
    and  cd_centro_responsabilita in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		        esercizio = aEsercizio
		    and cd_cdr_root =  aCdCdr
		   );
  end if;

  if aNum > 0 then
   IBMERR001.RAISE_ERR_GENERICO('Operazione gia effettuata su cdr '||aCdCdr);
  end if;

  -- Stabilisco se il CDR ricevente e dell'UO Personale o no
  isCDRInUOPersonale:=false;
  if aCDRPersonale.cd_unita_organizzativa = aCDRRUO.cd_unita_organizzativa then
   isCDRInUOPersonale:=true;
  end if;

  aUOAfferenza:=CNRCTB020.getUOAfferenza(aCDRRUO);

  if not (CNRCTB050.GETSTATO(aEsercizio,aCDRPersonale.cd_centro_responsabilita) in (
	  CNRCTB050.STATO_PDG_INIZIALE,
	  CNRCTB050.STATO_PDG_PRE_CHIUSURA,
	  CNRCTB050.STATO_PDG_RC,
	  CNRCTB050.STATO_PDG_RC_PRE_CHIUSURA
	 )) then
   IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione del CDR del personale '||aCDRPersonale.cd_centro_responsabilita||' non e in stato iniziale o di pre-chiusura!');
  end if;

  for aPDG in PDG_CON_CONFIG_SCR(aEsercizio, aCDRRUO.cd_centro_responsabilita) loop
   CNRCTB050.LOCKPDG(aEsercizio, aCDRRUO.cd_centro_responsabilita);
   if not (aPDG.stato in (
	   CNRCTB050.STATO_PDG_INIZIALE,
	   CNRCTB050.STATO_PDG_PRE_CHIUSURA,
	   CNRCTB050.STATO_PDG_RC,
	   CNRCTB050.STATO_PDG_RC_PRE_CHIUSURA
	  )) then
    IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione del CDR NRUO '||aPDG.cd_centro_responsabilita||' non e in stato iniziale o di pre-chiusura!');
   end if;
  end loop;

  -- Il totale di scarico configurato dei dipendenti dell'UO e 100%  (comprendento le quote verso altre UO accettate)

  select count(*) into aNum from V_CDP_TOT_PRC where
         esercizio = aEsercizio
     and mese = 0
	 and cd_unita_organizzativa = aCDRRUO.cd_unita_organizzativa
	 and (
	         prc_a1 < 100
		  or prc_a2 < 100
		  or prc_a3 < 100
		 );

  if aNum > 0 Then

    For rec_err In     (Select Distinct id_matricola
                       From V_CDP_TOT_PRC where
                          esercizio = aEsercizio And
                          mese = 0 And
                          cd_unita_organizzativa = aCDRRUO.cd_unita_organizzativa
	                  and (prc_a1 < 100 Or
	                       prc_a2 < 100 Or
	                       prc_a3 < 100)) Loop

        matricola_err := matricola_err||' - '||rec_err.id_matricola;

    End Loop;

   IBMERR001.RAISE_ERR_GENERICO('La configurazione di scarico dei dipendenti dell''UO '||aCDRRUO.cd_unita_organizzativa||' non e'' completa per n. '||anum||' matricola/e ('||matricola_err||')');
  end if;

  -- Carico le voci del piano TFR e ONERICNR

  aVoceONERICNR:=CNRCTB000.GETVOCEONERICNR(aEsercizio);
  aVoceTFR:=CNRCTB000.GETVOCETFR(aEsercizio);

  -- Start dello scarico su PDG dei costi del personale

  for aASSCDPLA in (
      select * from V_CDP_SPACCATO_CDR_LA_VOCE where
	          esercizio = aEsercizio
		  and mese = 0
		  and cd_cdr_root = aCDRRUO.cd_centro_responsabilita order by cd_cdr) loop  -- Cicla sulle configurazioni di scarico

	-- Accantonamento degli arrotondamenti dovuti allo scarico corrente (per elemento voce e tipo rapporto)
    fill_round_table(aASSCDPLA, aTSNow, aUser);

    -- Determina il tipo di rapporto dalla tabella COSTO_DEL_DIPENDENTE

/* stani
    isTempoDeterminato:=false;
    if aASSCDPLA.ti_rapporto = CDP_TI_RAPP_DETERMINATO then
     isTempoDeterminato:=true;
    end if;
*/

    aDestS:=null;
	 -- CREO IL DETTAGLIO LA PRIMA VOLTA
    aDestS:=null;
    aPgDettaglio:=0;
	select NVL(max(pg_spesa),0) into aPgDettaglio from PDG_PREVENTIVO_SPE_DET where
            ESERCIZIO=aEsercizio
        and CD_CENTRO_RESPONSABILITA=aASSCDPLA.cd_cdr
        and CD_LINEA_ATTIVITA=aASSCDPLA.cd_linea_attivita
        and TI_APPARTENENZA=aASSCDPLA.ti_appartenenza
        and TI_GESTIONE=aASSCDPLA.ti_gestione
        and CD_ELEMENTO_VOCE=aASSCDPLA.cd_elemento_voce;

 	aPgDettaglio:=aPgDettaglio + 1;

 	 -- Leggo la linea di attivita per aggiornare il dettaglio del PDG con funzione e natura
 	select * into aLATmp from linea_attivita where
 	       cd_linea_attivita = aASSCDPLA.cd_linea_attivita
 	   and cd_centro_responsabilita = aASSCDPLA.cd_cdr;

    if
	       aUOAfferenza.cd_area_ricerca is null
       and aLaTmp.cd_natura = '5'
	then
	  IBMERR001.RAISE_ERR_GENERICO('Esiste una configurazione di scarico con LA di natura 5 su CDR collegato ad UO non collegata ad Area di Ricerca');
	end if;

    aDestS.ESERCIZIO:=aEsercizio;
    aDestS.CD_CENTRO_RESPONSABILITA:=aASSCDPLA.cd_cdr;
    aDestS.CD_LINEA_ATTIVITA:=aASSCDPLA.cd_linea_attivita;
    aDestS.CD_FUNZIONE:=aLATmp.cd_funzione;
    aDestS.CD_NATURA:=aLATmp.cd_natura;
    aDestS.TI_APPARTENENZA:=aASSCDPLA.ti_appartenenza;
    aDestS.TI_GESTIONE:=aASSCDPLA.ti_gestione;
    aDestS.CD_ELEMENTO_VOCE:=aASSCDPLA.cd_elemento_voce;
    aDestS.PG_SPESA:=aPgDettaglio;
    aDestS.DT_REGISTRAZIONE:=aTSNow;

    if aASSCDPLA.cd_elemento_voce = aVoceONERICNR.cd_elemento_voce then
       aDestS.DESCRIZIONE:=DESC_DETT_ONERI_CNR||' '||aASSCDPLA.ti_rapporto;
 	elsif aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce then
       aDestS.DESCRIZIONE:=DESC_DETT_TFR||' '||aASSCDPLA.ti_rapporto;
    else
       aDestS.DESCRIZIONE:=DESC_DETT||' '||aASSCDPLA.ti_rapporto;
 	end if;

    aDestS.STATO:='Y';
    aDestS.ORIGINE:=CNRCTB050.ORIGINE_STIPENDI;
-- stani
    if isCDRInUOPersonale /*or isTempoDeterminato*/ Or
        (aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce) then
        aDestS.CATEGORIA_DETTAGLIO:=CNRCTB050.DETTAGLIO_SINGOLO;
    else
        aDestS.CATEGORIA_DETTAGLIO:=CNRCTB050.DETTAGLIO_SCARICO;
    end if;

 	aDestS.FL_SOLA_LETTURA:='Y'; -- I DETTAGLI STIPENDIALI NON SONO TOCCABILI!!!

  	CNRCTB050.resetCampiImporto(aDestS);

 	if aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce then
     setImportoTFR(aASSCDPLA, aDestS);
    else
     setImporto(aASSCDPLA, aDestS);
    end if;

    aDestS.DACR:=aTSNow;
    aDestS.UTCR:=aUser;
    aDestS.DUVA:=aTSNow;
    aDestS.UTUV:=aUser;
    aDestS.PG_VER_REC:=1;
    CNRCTB050.ins_PDG_PREVENTIVO_SPE_DET (aDestS);

    aAss.ESERCIZIO:=aDestS.esercizio;
    aAss.TI_PREV_CONS:=aASSCDPLA.ti_prev_cons;
    aAss.ID_MATRICOLA:=aASSCDPLA.ti_rapporto;
    aAss.CD_CENTRO_RESPONSABILITA:=aDestS.cd_centro_responsabilita;
    aAss.CD_LINEA_ATTIVITA:=aDestS.cd_linea_attivita;
    aAss.TI_APPARTENENZA:=aDestS.ti_appartenenza;
    aAss.TI_GESTIONE:=aDestS.ti_gestione;
    aAss.CD_ELEMENTO_VOCE:=aDestS.cd_elemento_voce;
    aAss.PG_SPESA:=aDestS.pg_spesa;
    aAss.DACR:=aTSNow;
    aAss.UTCR:=aUser;
    aAss.DUVA:=aTSNow;
    aAss.UTUV:=aUser;
    aAss.PG_VER_REC:=1;
    INS_ASS_CDP_PDG(aAss);
      -- Se il CDR d'origine non appartiene all'UO Personale e la matricola non e a tempo determinato,
      -- creo il dettaglio collegato nel CDR responsabile dell'UO del Personale
-- stani
    if not (isCDRInUoPersonale /*or isTempoDeterminato*/ Or
            (aASSCDPLA.cd_elemento_voce = aVoceTFR.cd_elemento_voce)) then

       -- Estraggo la linea di attivita di tipo SAUOP per il CDR in questione
       begin
 	    aLASAUOP:=CNRCTB010.getLASAUOP(aEsercizio, aCDRPersonale.cd_centro_responsabilita);
 	   exception when NO_DATA_FOUND then
 	    aLASAUOP:=CNRCTB010.creaLASAUOP(aEsercizio,aCDRPersonale.cd_centro_responsabilita,aUser);
 	   end;

       aPgDettaglioColl:=0;
 	   select NVL(max(pg_spesa),0) into aPgDettaglioColl from PDG_PREVENTIVO_SPE_DET where
            ESERCIZIO=aEsercizio
        and CD_CENTRO_RESPONSABILITA=aCDRPersonale.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aLASAUOP.cd_linea_attivita
        and TI_APPARTENENZA=aASSCDPLA.ti_appartenenza
        and TI_GESTIONE=aASSCDPLA.ti_gestione
        and CD_ELEMENTO_VOCE=aASSCDPLA.cd_elemento_voce;

 	   aPgDettaglioColl:=aPgDettaglioColl + 1;

 	   aDestSColl:=null;
       aDestSColl.ESERCIZIO:=aEsercizio;
       aDestSColl.CD_CENTRO_RESPONSABILITA:=aCDRPersonale.cd_centro_responsabilita;
       aDestSColl.CD_LINEA_ATTIVITA:=aLASAUOP.cd_linea_attivita;
       aDestSColl.CD_FUNZIONE:=aLASAUOP.cd_funzione;
       aDestSColl.CD_NATURA:=aLASAUOP.cd_natura;
       aDestSColl.TI_APPARTENENZA:=aASSCDPLA.ti_appartenenza;
       aDestSColl.TI_GESTIONE:=aASSCDPLA.ti_gestione;
       aDestSColl.CD_ELEMENTO_VOCE:=aASSCDPLA.cd_elemento_voce;
       aDestSColl.PG_SPESA:=aPgDettaglioColl;
       aDestSColl.DT_REGISTRAZIONE:=aTSNow;

 	   if aASSCDPLA.cd_elemento_voce = aVoceONERICNR.cd_elemento_voce then
        aDestSColl.DESCRIZIONE:=DESC_DETT_ONERI_CNR||' '||aASSCDPLA.ti_rapporto;
       else
        aDestSColl.DESCRIZIONE:=DESC_DETT||' '||aASSCDPLA.ti_rapporto;
       end if;

       aDestSColl.STATO:='Y';
       aDestSColl.ORIGINE:=CNRCTB050.ORIGINE_STIPENDI;
       aDestSColl.CATEGORIA_DETTAGLIO:=CNRCTB050.DETTAGLIO_CARICO;

       -- Richiesta 137R

       aDestSColl.FL_SOLA_LETTURA:='N';

 	   aDestSColl.PG_SPESA_CLGS:=aDestS.PG_SPESA;
       aDestSColl.CD_CENTRO_RESPONSABILITA_CLGS:=aDestS.CD_CENTRO_RESPONSABILITA;
       aDestSColl.CD_LINEA_ATTIVITA_CLGS:=aDestS.CD_LINEA_ATTIVITA;
       aDestSColl.TI_APPARTENENZA_CLGS:=aDestS.TI_APPARTENENZA;
       aDestSColl.TI_GESTIONE_CLGS:=aDestS.TI_GESTIONE;
       aDestSColl.CD_ELEMENTO_VOCE_CLGS:=aDestS.CD_ELEMENTO_VOCE;

 	   CNRCTB050.resetCampiImporto(aDestSColl);
 	   setImportoDettaglioCollegato(aDestSColl, aDestS);

       aDestSColl.DACR:=aTSNow;
       aDestSColl.UTCR:=aUser;
       aDestSColl.DUVA:=aTSNow;
       aDestSColl.UTUV:=aUser;
       aDestSColl.PG_VER_REC:=1;
       CNRCTB050.ins_PDG_PREVENTIVO_SPE_DET (aDestSColl);

 	  -- Aggiorno il dettaglio originale con i dati del dettaglio collegato

 	   update PDG_PREVENTIVO_SPE_DET set
 	   PG_SPESA_CLGS = aPgDettaglioColl,
        CD_CENTRO_RESPONSABILITA_CLGS=aDestSColl.cd_centro_responsabilita,
        CD_LINEA_ATTIVITA_CLGS=aDestSColl.cd_linea_attivita,
        TI_APPARTENENZA_CLGS=aDestSColl.ti_appartenenza,
        TI_GESTIONE_CLGS=aDestSColl.ti_gestione,
        CD_ELEMENTO_VOCE_CLGS=aDestSColl.cd_elemento_voce
 	   where
            ESERCIZIO=aDestS.esercizio
        and CD_CENTRO_RESPONSABILITA=aDestS.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aDestS.cd_linea_attivita
        and TI_APPARTENENZA=aDestS.ti_appartenenza
        and TI_GESTIONE=aDestS.ti_gestione
        and CD_ELEMENTO_VOCE=aDestS.cd_elemento_voce
 	    and PG_SPESA=aDestS.pg_spesa;

 	  -- Inserisce il collegamento tra CDP e dettaglio di CARICO su UO Personale
       aAss.ESERCIZIO:=aDestSColl.esercizio;
       aAss.TI_PREV_CONS:=aASSCDPLA.ti_prev_cons;
       aAss.ID_MATRICOLA:=aASSCDPLA.ti_rapporto;
	   aAss.CD_CENTRO_RESPONSABILITA:=aDestSColl.cd_centro_responsabilita;
       aAss.CD_LINEA_ATTIVITA:=aDestSColl.cd_linea_attivita;
       aAss.TI_APPARTENENZA:=aDestSColl.ti_appartenenza;
       aAss.TI_GESTIONE:=aDestSColl.ti_gestione;
       aAss.CD_ELEMENTO_VOCE:=aDestSColl.cd_elemento_voce;
       aAss.PG_SPESA:=aDestSColl.pg_spesa;
       aAss.DACR:=aTSNow;
       aAss.UTCR:=aUser;
       aAss.DUVA:=aTSNow;
       aAss.UTUV:=aUser;
       aAss.PG_VER_REC:=1;
       INS_ASS_CDP_PDG(aAss);
 	end if;
    update ass_cdp_la set
	     stato = STATO_CDP_SCARICATO
	    ,dt_scarico = aTSNow
		,utuv=aUser
		,duva=aTSNow
		,pg_ver_rec = pg_ver_rec + 1
    where
	       esercizio = aASSCDPLA.esercizio
	   and mese = 0
	   and cd_centro_responsabilita = aASSCDPLA.cd_cdr
	   and cd_linea_attivita = aASSCDPLA.cd_linea_attivita
	   and stato = STATO_CDP_NON_SCARICATO;
  end loop;

  -- Distribuzione dei rotti su dettagli creati

  for aAssCdpRnd in (select * from ass_cdp_round where
	          esercizio = aEsercizio
		  and cd_cdr_root = aCDRRUO.cd_centro_responsabilita
		  for update nowait) loop  -- Cicla sulle configurazioni dei ROTTI
   isDettRedistrRottiFound:=false;
   for aModSpeDet in (select p.* from pdg_preventivo_spe_det p, V_PDG_CDR_RUO_NRUO c where
                p.esercizio=aAssCdpRnd.esercizio
			and c.cd_cdr_root = aAssCdpRnd.cd_cdr_root
			and c.esercizio=p.esercizio
            and p.cd_centro_responsabilita=c.cd_centro_responsabilita
			and p.origine=CNRCTB050.ORIGINE_STIPENDI
			and p.ti_appartenenza = aAssCdpRnd.ti_appartenenza
			and p.ti_gestione = aAssCdpRnd.ti_gestione
			and p.cd_elemento_voce = aAssCdpRnd.cd_elemento_voce
			and p.categoria_dettaglio in (CNRCTB050.DETTAGLIO_SINGOLO,CNRCTB050.DETTAGLIO_SCARICO)
			and exists (select 1 from ass_cdp_pdg where
			           ESERCIZIO=p.esercizio
                   and CD_CENTRO_RESPONSABILITA=p.cd_centro_responsabilita
                   and CD_LINEA_ATTIVITA=p.cd_linea_attivita
                   and TI_APPARTENENZA=p.ti_appartenenza
                   and TI_GESTIONE=p.ti_gestione
                   and CD_ELEMENTO_VOCE=p.cd_elemento_voce
                   and PG_SPESA=p.pg_spesa
				   and ti_prev_cons=TIPO_PREVENTIVO
				   and id_matricola=aAssCdpRnd.ti_rapporto
			)
   ) loop
    isProcessato:=false;
/* stani     if
	      not isProcessato
	  and     aAssCdpRnd.ti_rapporto=CDP_TI_RAPP_DETERMINATO
	  and not aAssCdpRnd.cd_elemento_voce = aVoceTFR.cd_elemento_voce
	  and aModSpeDet.CATEGORIA_DETTAGLIO=CNRCTB050.DETTAGLIO_SINGOLO
	then
     if
                 (aModSpeDet.IM_RO_CSS_ALTRI_COSTI = 0 and aAssCdpRnd.im_arr_a1 <> 0)
			  or aModSpeDet.IM_RO_CSS_ALTRI_COSTI+aAssCdpRnd.im_arr_a1 < 0
     then
      null;
     else
      update pdg_preventivo_spe_det set
       IM_RO_CSS_ALTRI_COSTI=IM_RO_CSS_ALTRI_COSTI+aAssCdpRnd.im_arr_a1
	  where
            ESERCIZIO=aModSpeDet.esercizio
        and CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aModSpeDet.cd_linea_attivita
        and TI_APPARTENENZA=aModSpeDet.ti_appartenenza
        and TI_GESTIONE=aModSpeDet.ti_gestione
        and CD_ELEMENTO_VOCE=aModSpeDet.cd_elemento_voce
 	    and PG_SPESA=aModSpeDet.pg_spesa;
      isProcessato:=true;
     end if;
	end if;
*/
	if
	        not isProcessato
	    and aAssCdpRnd.cd_elemento_voce = aVoceTFR.cd_elemento_voce
	    and aModSpeDet.CATEGORIA_DETTAGLIO=CNRCTB050.DETTAGLIO_SINGOLO
	then
     if
              (aModSpeDet.IM_RO_CSS_ALTRI_COSTI=0 and aAssCdpRnd.im_arr_a1 <> 0)
           or (aModSpeDet.IM_RAA_A2_COSTI_FINALI=0 and aAssCdpRnd.im_arr_a2 <> 0)
           or (aModSpeDet.IM_RAH_A3_COSTI_FINALI=0 and aAssCdpRnd.im_arr_a3 <> 0)
           or aModSpeDet.IM_RO_CSS_ALTRI_COSTI+aAssCdpRnd.im_arr_a1 < 0
           or aModSpeDet.IM_RAA_A2_COSTI_FINALI+aAssCdpRnd.im_arr_a2 < 0
           or aModSpeDet.IM_RAH_A3_COSTI_FINALI+aAssCdpRnd.im_arr_a3 < 0
     then
      null;
     else
      update pdg_preventivo_spe_det set
       IM_RO_CSS_ALTRI_COSTI=IM_RO_CSS_ALTRI_COSTI+aAssCdpRnd.im_arr_a1
      ,IM_RAA_A2_COSTI_FINALI=IM_RAA_A2_COSTI_FINALI+aAssCdpRnd.im_arr_a2
      ,IM_RAH_A3_COSTI_FINALI=IM_RAH_A3_COSTI_FINALI+aAssCdpRnd.im_arr_a3
	  where
            ESERCIZIO=aModSpeDet.esercizio
        and CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aModSpeDet.cd_linea_attivita
        and TI_APPARTENENZA=aModSpeDet.ti_appartenenza
        and TI_GESTIONE=aModSpeDet.ti_gestione
        and CD_ELEMENTO_VOCE=aModSpeDet.cd_elemento_voce
 	    and PG_SPESA=aModSpeDet.pg_spesa;
      isProcessato:=true;
     end if;
    end if;

	if
	      not isProcessato
	  and not (
	      aAssCdpRnd.cd_elemento_voce = aVoceTFR.cd_elemento_voce
	 -- stani  or aAssCdpRnd.ti_rapporto=CDP_TI_RAPP_DETERMINATO
	  ) and aModSpeDet.CATEGORIA_DETTAGLIO=CNRCTB050.DETTAGLIO_SINGOLO
	then
     if
              (aModSpeDet.IM_RK_CCS_SPESE_OGC = 0 and aAssCdpRnd.im_arr_a1 <> 0)
           or (aModSpeDet.IM_RAE_A2_SPESE_OGC = 0 and aAssCdpRnd.im_arr_a2 <> 0)
           or (aModSpeDet.IM_RAN_A3_SPESE_OGC = 0 and aAssCdpRnd.im_arr_a3 <> 0)
           or aModSpeDet.IM_RK_CCS_SPESE_OGC+aAssCdpRnd.im_arr_a1 < 0
           or aModSpeDet.IM_RAE_A2_SPESE_OGC+aAssCdpRnd.im_arr_a2 < 0
           or aModSpeDet.IM_RAN_A3_SPESE_OGC+aAssCdpRnd.im_arr_a3 < 0
     then
      null;
     else
      update pdg_preventivo_spe_det set
       IM_RK_CCS_SPESE_OGC=IM_RK_CCS_SPESE_OGC+aAssCdpRnd.im_arr_a1
      ,IM_RH_CCS_COSTI=IM_RH_CCS_COSTI+aAssCdpRnd.im_arr_a1
      ,IM_RAE_A2_SPESE_OGC=IM_RAE_A2_SPESE_OGC+aAssCdpRnd.im_arr_a2
      ,IM_RAA_A2_COSTI_FINALI=IM_RAA_A2_COSTI_FINALI+aAssCdpRnd.im_arr_a2
      ,IM_RAN_A3_SPESE_OGC=IM_RAN_A3_SPESE_OGC+aAssCdpRnd.im_arr_a3
      ,IM_RAH_A3_COSTI_FINALI=IM_RAH_A3_COSTI_FINALI+aAssCdpRnd.im_arr_a3
	  where
            ESERCIZIO=aModSpeDet.esercizio
        and CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aModSpeDet.cd_linea_attivita
        and TI_APPARTENENZA=aModSpeDet.ti_appartenenza
        and TI_GESTIONE=aModSpeDet.ti_gestione
        and CD_ELEMENTO_VOCE=aModSpeDet.cd_elemento_voce
 	    and PG_SPESA=aModSpeDet.pg_spesa;
      isProcessato:=true;
     end if;
    end if;
	if
          not isProcessato
	  and not (
	      aAssCdpRnd.cd_elemento_voce = aVoceTFR.cd_elemento_voce
	   -- stani or aAssCdpRnd.ti_rapporto=CDP_TI_RAPP_DETERMINATO
	  ) and
	  aModSpeDet.CATEGORIA_DETTAGLIO=CNRCTB050.DETTAGLIO_SCARICO
	then
     if
              (aModSpeDet.IM_RL_CCS_SPESE_OGC_ALTRA_UO=0 and aAssCdpRnd.im_arr_a1 <> 0)
           or (aModSpeDet.IM_RAF_A2_SPESE_OGC_ALTRA_UO=0 and aAssCdpRnd.im_arr_a2 <> 0)
           or (aModSpeDet.IM_RAO_A3_SPESE_OGC_ALTRA_UO=0 and aAssCdpRnd.im_arr_a3 <> 0)
           or aModSpeDet.IM_RL_CCS_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a1 < 0
           or aModSpeDet.IM_RAF_A2_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a2 < 0
           or aModSpeDet.IM_RAO_A3_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a3 < 0
     then
      null;
     else
      update pdg_preventivo_spe_det set
       IM_RL_CCS_SPESE_OGC_ALTRA_UO=IM_RL_CCS_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a1
      ,IM_RH_CCS_COSTI=IM_RH_CCS_COSTI+aAssCdpRnd.im_arr_a1
      ,IM_RAF_A2_SPESE_OGC_ALTRA_UO=IM_RAF_A2_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a2
      ,IM_RAA_A2_COSTI_FINALI=IM_RAA_A2_COSTI_FINALI+aAssCdpRnd.im_arr_a2
      ,IM_RAO_A3_SPESE_OGC_ALTRA_UO=IM_RAO_A3_SPESE_OGC_ALTRA_UO+aAssCdpRnd.im_arr_a3
      ,IM_RAH_A3_COSTI_FINALI=IM_RAH_A3_COSTI_FINALI+aAssCdpRnd.im_arr_a3
	  where
            ESERCIZIO=aModSpeDet.esercizio
        and CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita
        and CD_LINEA_ATTIVITA=aModSpeDet.cd_linea_attivita
        and TI_APPARTENENZA=aModSpeDet.ti_appartenenza
        and TI_GESTIONE=aModSpeDet.ti_gestione
        and CD_ELEMENTO_VOCE=aModSpeDet.cd_elemento_voce
 	    and PG_SPESA=aModSpeDet.pg_spesa;
      update pdg_preventivo_spe_det set
         IM_RU_SPESE_COSTI_ALTRUI=IM_RU_SPESE_COSTI_ALTRUI+aAssCdpRnd.im_arr_a1
        ,IM_RAG_A2_SPESE_COSTI_ALTRUI=IM_RAG_A2_SPESE_COSTI_ALTRUI+aAssCdpRnd.im_arr_a2
        ,IM_RAP_A3_SPESE_COSTI_ALTRUI=IM_RAP_A3_SPESE_COSTI_ALTRUI+aAssCdpRnd.im_arr_a3
	  where
            ESERCIZIO=aModSpeDet.esercizio
        and CD_CENTRO_RESPONSABILITA=aModSpeDet.cd_centro_responsabilita_clgs
        and CD_LINEA_ATTIVITA=aModSpeDet.cd_linea_attivita_clgs
        and TI_APPARTENENZA=aModSpeDet.ti_appartenenza_clgs
        and TI_GESTIONE=aModSpeDet.ti_gestione_clgs
        and CD_ELEMENTO_VOCE=aModSpeDet.cd_elemento_voce_clgs
 	    and PG_SPESA=aModSpeDet.pg_spesa_clgs;
	   isProcessato:=true;
     end if;
    end if;
    if isProcessato then
	 update ass_cdp_round set
	     cd_centro_responsabilita = aModSpeDet.cd_centro_responsabilita
	    ,cd_linea_attivita = aModSpeDet.cd_linea_attivita
	    ,pg_spesa = aModSpeDet.pg_spesa
		,im_arr_a1_non_distr = round(aAssCdpRnd.im_arr_a1 - round(aAssCdpRnd.im_arr_a1,2),30)
		,im_arr_a2_non_distr = round(aAssCdpRnd.im_arr_a2 - round(aAssCdpRnd.im_arr_a2,2),30)
		,im_arr_a3_non_distr = round(aAssCdpRnd.im_arr_a3 - round(aAssCdpRnd.im_arr_a3,2),30)
	 where
            ESERCIZIO=aAssCdpRnd.esercizio
        and CD_CDR_ROOT=aAssCdpRnd.CD_CDR_ROOT
        and TI_APPARTENENZA=aAssCdpRnd.ti_appartenenza
        and TI_GESTIONE=aAssCdpRnd.ti_gestione
        and CD_ELEMENTO_VOCE=aAssCdpRnd.cd_elemento_voce
 	    and TI_RAPPORTO=aAssCdpRnd.ti_rapporto;
     isDettRedistrRottiFound:=true;
     exit;
    end if;
   end loop;
   if not isDettRedistrRottiFound then
    IBMERR001.RAISE_ERR_GENERICO('Impossibile redistribuire i rotti per cdr RUO:'||aAssCdpRnd.cd_cdr_root||' es:'||aAssCdpRnd.esercizio||'voce del piano:'||aAssCdpRnd.cd_elemento_voce||' tipo rapporto:'||aAssCdpRnd.ti_rapporto);
   end if;
  end loop;

 end;

 procedure annullaCDPSuPdg(aEsercizio number, aCdCdr varchar2, aUser varchar2) is
  aTSNow date;
  aCDRRUO cdr%rowtype;
  aCDRPersonale cdr%rowtype;
  aNum number(8);
 begin
  aTSNow:=sysdate;

  -- Leggo il CDR

  aCDRRUO:=CNRCTB020.GETCDRVALIDO(aEsercizio, aCdCdr);

  if to_number(aCDRRUO.cd_proprio_cdr) != 0 then
   IBMERR001.RAISE_ERR_GENERICO('Operazione permessa solo su CDR di tipo RUO!');
  end if;

  select count(*) into aNum from ass_cdp_pdg where
   esercizio = aEsercizio
   and cd_centro_responsabilita in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		        esercizio = aEsercizio
		    and cd_cdr_root =  aCdCdr
		   );

  if aNum = 0 then
   IBMERR001.RAISE_ERR_GENERICO('Operazione di scarico non ancora effettuata per cdr '||aCdCdr);
  end if;

  -- Leggo il CDR del personale

  aCDRPersonale:=CNRCTB020.GETCDRPERSONALE(aEsercizio);

  -- Lock PDG del CDR del personale

  CNRCTB050.LOCKPDG(aEsercizio, aCDRPersonale.cd_centro_responsabilita);

  -- Verifico che l'aggregato del CDR del personale NON sia chiuso in stato B

  if CNRCTB050.checkStatoAggregato(aEsercizio,
                                   aCDRPersonale.cd_centro_responsabilita,
								   CNRCTB050.STATO_AGGREGATO_FINALE) = 'Y'
  then
   IBMERR001.RAISE_ERR_GENERICO('L''agregato del CDR del personale risulta chiuso. Non è possibile annullare lo scarico dei costi del personale');
  end if;

  -- Controllo modificabilita PDG CDR Personale

  if not (CNRCTB050.GETSTATO(aEsercizio,aCDRPersonale.cd_centro_responsabilita) in (
	                 CNRCTB050.STATO_PDG_INIZIALE,
	                 CNRCTB050.STATO_PDG_PRE_CHIUSURA,
	                 CNRCTB050.STATO_PDG_RC,
	                 CNRCTB050.STATO_PDG_RC_PRE_CHIUSURA
        )) then
   IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione del CDR del personale '||aCDRPersonale.cd_centro_responsabilita||' non e attualmente modificabile!');
  end if;

  -- Controllo modificabilita PDG CDR RUO/NRUO

  for aPDG in PDG_CON_CONFIG_SCR(aEsercizio, aCDRRUO.cd_centro_responsabilita) loop
   if not (aPDG.stato in (
	            CNRCTB050.STATO_PDG_INIZIALE,
	            CNRCTB050.STATO_PDG_PRE_CHIUSURA,
	            CNRCTB050.STATO_PDG_RC,
	            CNRCTB050.STATO_PDG_RC_PRE_CHIUSURA
          )) then
    IBMERR001.RAISE_ERR_GENERICO('Il piano di gestione del CDR '||aPDG.cd_centro_responsabilita||' non e in stato iniziale o di pre-chiusura!');
   end if;
  end loop;

  -- Eliminazione dei dettagli collegati nel CDR del personale

  delete from pdg_preventivo_spe_det where
         esercizio = aEsercizio
	 and cd_centro_responsabilita = aCDRPersonale.cd_centro_responsabilita
	 and cd_centro_responsabilita_clgs in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		       esercizio = aEsercizio
		   and cd_cdr_root =  aCdCdr
		  )
     and categoria_dettaglio in (CNRCTB050.DETTAGLIO_CARICO)
     and origine = CNRCTB050.ORIGINE_STIPENDI;

  -- Eliminazione del dettaglio principale nel CDR di origine

  delete from pdg_preventivo_spe_det where
         esercizio = aEsercizio
	 and cd_centro_responsabilita in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		        esercizio = aEsercizio
		    and cd_cdr_root =  aCdCdr
		 )
     and categoria_dettaglio in (CNRCTB050.DETTAGLIO_SINGOLO, CNRCTB050.DETTAGLIO_SCARICO)
     and origine = CNRCTB050.ORIGINE_STIPENDI;

  update ass_cdp_la set
	     stato = STATO_CDP_NON_SCARICATO
	    ,dt_scarico = null
		,utuv = aUser
		,duva = aTSNow
		,pg_ver_rec = pg_ver_rec + 1
	where
	     esercizio = aEsercizio
     and mese = 0
	 and (
	          cd_centro_responsabilita = aCdCdr
		   or cd_centro_responsabilita in (select cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
		        esercizio = aEsercizio
		    and cd_cdr_root =  aCdCdr
           )
         );
 end;

 -- Verifica di scarico completo dei dipendenti su di un certo CDR
 --
 -- 1. Per ogni CDR responsabile di UO verifico che:
 --   1a. non esistono pending da altra UO
 --   1b. che tutte le  percentuali di matricole
 --       di altra UO accettate dall'UO in processo siano state configurate in ripartizione nell'UO al 100%
 --
 -- 2. Per ogni CDR verifico che:
 --     2a. tutti i costi configurati sul quel CDR siano stati scaricati su PDG

 function checkScaricoCDPCompleto(aEsercizio number, aCdCdr varchar2) return char is
  aNum NUMBER(8);
  aCdr cdr%rowtype;
  aCDRPersonale cdr%rowtype;
  aPDGConScarichi pdg_preventivo%rowtype;
  aTotNonConfig number;
  aNumConfNonCompl number;
 begin


  -- Leggo il CDR del personale e lock del PDG

  aCDRPersonale:=CNRCTB020.GETCDRPERSONALE(aEsercizio);

  -- Se il cdr del personale e il CDR in processo devo applicare il controllo a tutti i CDR validi

  if aCDRPersonale.cd_centro_responsabilita = aCdCdr then
   aTotNonConfig:=0;
   -- Verifico che non esistano matricole non configurate in tutto il CNR
   select count(*) into aTotNonConfig from costo_del_dipendente a where
       a.esercizio=aEsercizio
   and a.mese = 0
   and not exists (
    select 1 from ass_cdp_la where
	     esercizio = a.esercizio
	 and id_matricola = a.id_matricola
     and mese = 0
   ) and not exists (
    select 1 from ass_cdp_uo where
     	 esercizio = a.esercizio
	 and id_matricola = a.id_matricola
     and mese = 0
	 and stato != STATO_ALTRAUO_RIFIUTATO
   );
   if aTotNonConfig > 0 then
    IBMERR001.RAISE_ERR_GENERICO('Esistono matricole non configurate in termini di CDR/LA');
   end if;

   -- locko il cdr del personale
   CNRCTB050.LOCKPDG(aEsercizio, aCDRPersonale.cd_centro_responsabilita);
   for aCdrNonPersonale in (select * from v_cdr_valido where
                                  esercizio = aEsercizio
							  and cd_centro_responsabilita != aCDRPersonale.cd_centro_responsabilita) loop
    if checkScaricoCDPCompleto(aEsercizio, aCdrNonPersonale.cd_centro_responsabilita)='N' then
	 return 'N';
	end if;
/*
	begin
	 select * into aPDGConScarichi from pdg_preventivo a where
	      a.esercizio = aEsercizio
	  and a.cd_centro_responsabilita = aCdrNonPersonale.cd_centro_responsabilita
	  and exists (select 1 from ass_cdp_la where
	       esercizio = aEsercizio
	   and cd_centro_responsabilita = a.cd_centro_responsabilita
       and mese = 0
	  );
	  if not (aPDGConScarichi.stato = CNRCTB050.STATO_PDG_CHIUSURA or aPDGConScarichi.stato = CNRCTB050.STATO_PDG_FINALE) then
	   return 'N';
	  end if;
	exception when NO_DATA_FOUND then
	 null;
	end;
*/
   end loop;
  end if;

  -- Leggo il CDR

  aCDR:=CNRCTB020.GETCDRVALIDO(aEsercizio, aCdCdr);

  -- Se si tratta di CDR RUO (non del personale) devo verificare che NON esistano matricole NON configurate appartenenti all'UO

  if aCDR.cd_centro_responsabilita != aCDRPersonale.cd_centro_responsabilita and to_number(aCDR.cd_proprio_cdr) = 0 then -- Cdr responsabile di UO
      aTotNonConfig:=0;
	  -- Verifico che non esistano matricole non configurate e appartenenti all'UO di cui aCDR e responsabile
      select count(*) into aTotNonConfig from costo_del_dipendente a where
           a.esercizio=aEsercizio
	   and a.mese=0
       and a.cd_unita_organizzativa=aCDR.cd_unita_organizzativa
	   and not exists (
          select 1 from ass_cdp_la where
   	           esercizio = a.esercizio
   	       and id_matricola = a.id_matricola
           and mese = 0
       )
	   and not exists (
          select 1 from ass_cdp_uo where
        	   esercizio = a.esercizio
   	       and id_matricola = a.id_matricola
           and mese = 0
	       and stato != STATO_ALTRAUO_RIFIUTATO
       );
      if aTotNonConfig > 0 then
       IBMERR001.RAISE_ERR_GENERICO('Esistono matricole non configurate in termini di CDR/LA');
      end if;
  end if;

  -- 1. Per ogni CDR responsabile di UO verifico che:
  --   1a. non esistono pending da altra UO
  --   1b. che tutte le  percentuali di matricole
  --       di altra UO accettate dall'UO in processo siano state configurate in ripartizione nell'UO al 100%

  if to_number(aCDR.cd_proprio_cdr) = 0 then

   -- Il totale di scarico configurato dei dipendenti dell'UO e 100%  (comprendento le quote verso altre UO accettate)

   aNumConfNonCompl:=0;
   select count(*) into aNumConfNonCompl from V_CDP_TOT_PRC where
        esercizio = aEsercizio
    and mese = 0
    and cd_unita_organizzativa = aCDR.cd_unita_organizzativa
    and (
          prc_a1 < 100
       or prc_a2 < 100
       or prc_a3 < 100
    );
   if aNumConfNonCompl > 0 then
    IBMERR001.RAISE_ERR_GENERICO('La configurazione di scarico dei dipendenti dell''UO '||aCDR.cd_unita_organizzativa||' non e completa!');
   end if;

   -- Non esistono pending da altra UO

   for aAssCDPUO in (
    select * from ASS_CDP_UO
     where
 	      esercizio = aEsercizio
      and mese = 0
	  and cd_unita_organizzativa = aCDR.cd_unita_organizzativa
	  and stato = STATO_ALTRAUO_NONDEFINITO
   ) loop
    return 'N';
   end loop;


   -- Tutto cio che e stato accettato da altra UO risulta configurato per lo scrico all'interno dell'UO al 100%

   for aAssCDPUO in (
    select * from ASS_CDP_UO
     where
 	      esercizio = aEsercizio
      and mese =0
	  and cd_unita_organizzativa = aCDR.cd_unita_organizzativa
	  and stato = STATO_ALTRAUO_ACCETTATO
   ) loop
    aNum:=0;
    select count(*) into aNum from (select id_matricola from ASS_CDP_LA where
         esercizio = aEsercizio
     and mese=0
     and id_matricola = aAssCDPUO.id_matricola
	 and fl_dip_altra_uo = 'Y'
	 and (esercizio, cd_centro_responsabilita) in (
	  select esercizio, cd_centro_responsabilita from V_PDG_CDR_RUO_NRUO where
     	   esercizio = aEsercizio
	   and cd_cdr_root = aCDR.cd_centro_responsabilita
	 )
     group by id_matricola
     having
	     sum(prc_la_a1) != 100
      or sum(prc_la_a2) != 100
	  or sum(prc_la_a3) != 100
    );
    if aNum > 0 then
     return 'N';
    end if;
   end loop;

  end if; -- Fine blocco applicato a CDR responsabile di UO

 -- 2. Per ogni CDR verifico che:
 --     2a. tutti i costi configurati sul quel CDR siano stati scaricati su PDG

  aNum:=0;
  select count(*) into aNum from ASS_CDP_LA where
        esercizio = aEsercizio
    and mese =0
    and cd_centro_responsabilita = aCdCdr
    and stato <> STATO_CDP_SCARICATO;
  if aNum > 0 then
   return 'N';
  end if;

  return 'Y';
 end;

 procedure ins_ASS_CDP_PDG (aDest ASS_CDP_PDG%rowtype) is
  begin
   insert into ASS_CDP_PDG (
     ESERCIZIO
    ,TI_PREV_CONS
    ,ID_MATRICOLA
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,PG_SPESA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.TI_PREV_CONS
    ,aDest.ID_MATRICOLA
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.PG_SPESA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_COSTO_DEL_DIPENDENTE (aDest COSTO_DEL_DIPENDENTE%rowtype) is
  begin
   insert into COSTO_DEL_DIPENDENTE (
     ESERCIZIO
    ,TI_PREV_CONS
    ,ID_MATRICOLA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_UNITA_ORGANIZZATIVA
    ,TI_RAPPORTO
    ,IM_A1
    ,IM_A2
    ,IM_A3
    ,IM_ONERI_CNR_A1
    ,IM_ONERI_CNR_A2
    ,IM_ONERI_CNR_A3
    ,IM_TFR_A1
    ,IM_TFR_A2
    ,IM_TFR_A3
    ,DT_SCARICO
    ,DACR
    ,UTCR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.TI_PREV_CONS
    ,aDest.ID_MATRICOLA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.TI_RAPPORTO
    ,aDest.IM_A1
    ,aDest.IM_A2
    ,aDest.IM_A3
    ,aDest.IM_ONERI_CNR_A1
    ,aDest.IM_ONERI_CNR_A2
    ,aDest.IM_ONERI_CNR_A3
    ,aDest.IM_TFR_A1
    ,aDest.IM_TFR_A2
    ,aDest.IM_TFR_A3
    ,aDest.DT_SCARICO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_ASS_CDP_ROUND (aDest ASS_CDP_ROUND%rowtype) is
  begin
   insert into ASS_CDP_ROUND (
     ESERCIZIO
    ,CD_CDR_ROOT
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,TI_RAPPORTO
    ,IM_ARR_A1
    ,IM_ARR_A2
    ,IM_ARR_A3
    ,IM_ARR_A1_NON_DISTR
    ,IM_ARR_A2_NON_DISTR
    ,IM_ARR_A3_NON_DISTR
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,PG_SPESA
    ,DACR
    ,UTUV
    ,UTCR
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDR_ROOT
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.TI_RAPPORTO
    ,aDest.IM_ARR_A1
    ,aDest.IM_ARR_A2
    ,aDest.IM_ARR_A3
    ,aDest.IM_ARR_A1_NON_DISTR
    ,aDest.IM_ARR_A2_NON_DISTR
    ,aDest.IM_ARR_A3_NON_DISTR
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.PG_SPESA
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

end;
