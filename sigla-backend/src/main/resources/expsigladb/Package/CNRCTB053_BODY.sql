--------------------------------------------------------
--  DDL for Package Body CNRCTB053
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB053" is

--------------------------------- VECCHIA GESTIONE -----------------------------------
 Procedure INS_ASS_GAE_ESERCIZIO(
    recParametriCnr PARAMETRI_CNR%rowtype,
    aLA linea_attivita%Rowtype,
    aTSNow date,
    aUser VARCHAR2)
 Is
    aAssLAEsercizio  ass_linea_attivita_esercizio%rowtype;
 Begin
    If aLa.PG_PROGETTO Is Not Null Then
       aAssLAEsercizio.ESERCIZIO                := aLA.ESERCIZIO_INIZIO;
       aAssLAEsercizio.CD_CENTRO_RESPONSABILITA := aLA.CD_CENTRO_RESPONSABILITA;
       aAssLAEsercizio.CD_LINEA_ATTIVITA        := aLA.CD_LINEA_ATTIVITA;
       aAssLAEsercizio.PG_PROGETTO              := aLA.PG_PROGETTO;
       If nvl(recParametriCnr.fl_nuovo_pdg,'N') = 'N' and aLA.ESERCIZIO_FINE > 2015 Then
          aAssLAEsercizio.ESERCIZIO_FINE        := 2015;
       Else
          aAssLAEsercizio.ESERCIZIO_FINE        := aLA.ESERCIZIO_FINE;
       End If;
       aAssLAEsercizio.DACR                     := aTSNow;
       aAssLAEsercizio.UTCR                     := aUser;
       aAssLAEsercizio.DUVA                     := aTSNow;
       aAssLAEsercizio.UTUV                     := aUser;
       aAssLAEsercizio.PG_VER_REC               := 1;
       CNRCTB010.ins_ASS_LINEA_ATTIVITA_ESER(aAssLAEsercizio);
    End If;
 End INS_ASS_GAE_ESERCIZIO;

 procedure ribaltaSuAreaPDGInt(
  aEsercizio number,
  aCdCentroResponsabilita varchar2,
  aCDR cdr%rowtype,
  aCDRArea cdr%rowtype,
  aEVCollegato elemento_voce%rowtype,
  aDett pdg_preventivo_spe_det%rowtype,
  recParametriCnr PARAMETRI_CNR%rowtype,
  aTSNow date,
  aUser varchar2
 ) is
  aUOCDS unita_organizzativa%rowtype;
  aLA linea_attivita%rowtype;
  aLACollegataS linea_attivita%rowtype;
  aLACollegataE linea_attivita%rowtype;
  aPgDettaglio NUMBER;
  aPgDettaglioColl NUMBER;
  aDestE pdg_preventivo_etr_det%rowtype;
  aDestS pdg_preventivo_spe_det%rowtype;
  aEV elemento_voce%rowtype;
  aStatoRibaltamento char(1);
  aFlagRibaltato boolean;
  aInsieme insieme_la%rowtype;
  aAssPdgVarCDRE ass_pdg_variazione_cdr%Rowtype;
  aAssPdgVarCDRS ass_pdg_variazione_cdr%Rowtype;
begin
    -- Leggo la linea di attività del dettaglio del centro servito

  begin
        select * into aLA from linea_attivita where
               CD_CENTRO_RESPONSABILITA = aCDR.cd_centro_responsabilita
         and CD_LINEA_ATTIVITA=aDett.cd_linea_attivita;
       exception
        when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Linea di attività del dettaglio del centro servito non trovata!');
         end;

  -- Ricerca o creazione della nuova linea di attività nel servente AREA

  begin
      -- Devo selezionare la linea di entrata CSSAC (una sola in quest'area con natura 5)
        select * into aLACollegataE from linea_attivita where
               CD_CENTRO_RESPONSABILITA = aCDRArea.cd_centro_responsabilita
           and CD_TIPO_LINEA_ATTIVITA = CNRCTB010.TI_LA_CSSAC
           and CD_NATURA = aLA.cd_natura
       and CD_NATURA = '5'     ; -- Dettaglio di natura 5
       exception
        when NO_DATA_FOUND then
            aInsieme:=null;
            aInsieme:=CNRCTB010.creaInsiemeScrArea(aEsercizio, aCDRArea.cd_centro_responsabilita, aUser);
            aLACollegataE.ESERCIZIO_INIZIO:= aEsercizio;
            aLACollegataE.ESERCIZIO_FINE:= CNRCTB008.ESERCIZIO_INFINITO;
            aLACollegataE.CD_CENTRO_RESPONSABILITA:=aCDRArea.cd_centro_responsabilita;
            aLACollegataE.CD_NATURA := aLA.cd_natura;
            aLACollegataE.CD_TIPO_LINEA_ATTIVITA := CNRCTB010.TI_LA_CSSAC;
            aLACollegataE.ds_linea_attivita:=aLA.ds_linea_attivita;
            aLACollegataE.denominazione:=CNRCTB010.SPESE_PER_COSTI_ALTRUI;
            aLACollegataE.CD_LINEA_ATTIVITA := CNRCTB010.getNextCodice(CNRCTB010.TI_TIPO_LA_SISTEMA,aLACollegataE);
            aLACollegataE.TI_GESTIONE := CNRCTB010.TI_GESTIONE_ENTRATE;
            aLACollegataE.CD_INSIEME_LA := aInsieme.cd_insieme_la;
            aLACollegataE.DACR:=aTSNow;
            aLACollegataE.UTCR:=aUser;
            aLACollegataE.DUVA:=aTSNow;
            aLACollegataE.UTUV:=aUser;
            aLACollegataE.PG_VER_REC:=1;
            CNRCTB010.ins_LINEA_ATTIVITA(aLACollegataE);

	    INS_ASS_GAE_ESERCIZIO(recParametriCnr, aLACollegataE, aTSNow, aUser);

            select * into aLACollegataE from linea_attivita where
                   CD_CENTRO_RESPONSABILITA=aCDRArea.cd_centro_responsabilita
             and CD_LINEA_ATTIVITA = aLACollegataE.CD_LINEA_ATTIVITA;
     end;

     -- Calcolo il progressivo del dettaglio (Ricavo figurativo) nel servente

         aDestE:=NULL;
         aPgDettaglioColl:=0;
         select NVL(max(pg_entrata),0) into aPgDettaglioColl from PDG_PREVENTIVO_ETR_DET where
             ESERCIZIO=aEsercizio
            and CD_CENTRO_RESPONSABILITA=aCDRArea.cd_centro_responsabilita
            and CD_LINEA_ATTIVITA=aLACollegataE.cd_linea_attivita
            and TI_APPARTENENZA=aEVCollegato.ti_appartenenza
            and TI_GESTIONE=aEVCollegato.ti_gestione
            and CD_ELEMENTO_VOCE=aEVCollegato.cd_elemento_voce;

         aPgDettaglioColl:=aPgDettaglioColl+1;

     -- Inserisco il ricavo figurativo nel servente AREA

         aDestE.ESERCIZIO:=aEsercizio;
         aDestE.CD_CENTRO_RESPONSABILITA:=aCDRArea.cd_centro_responsabilita;
         aDestE.CD_LINEA_ATTIVITA:=aLACollegataE.cd_linea_attivita;
         aDestE.TI_APPARTENENZA:=aEVCollegato.ti_appartenenza;
         aDestE.TI_GESTIONE:=aEVCollegato.ti_gestione;
         aDestE.CD_ELEMENTO_VOCE:=aEVCollegato.cd_elemento_voce;
         aDestE.PG_ENTRATA:=aPgDettaglioColl;
         aDestE.DT_REGISTRAZIONE:=aTSNow;
         aDestE.DESCRIZIONE:='Ricavo figurativo';
         aDestE.STATO:='Y';
         aDestE.ORIGINE:=CNRCTB050.ORIGINE_DIRETTA;
         aDestE.CATEGORIA_DETTAGLIO:=CNRCTB050.DETTAGLIO_CARICO;
         aDestE.FL_SOLA_LETTURA:='Y';

         aDestE.CD_CENTRO_RESPONSABILITA_CLGS:=aCDR.cd_centro_responsabilita;
         aDestE.CD_LINEA_ATTIVITA_CLGS:=aLA.cd_linea_attivita;
         aDestE.CD_FUNZIONE:=aLA.cd_funzione;
         aDestE.CD_NATURA:=aLA.cd_natura;
         aDestE.TI_APPARTENENZA_CLGS:=aDett.ti_appartenenza;
         aDestE.TI_GESTIONE_CLGS:=aDett.ti_gestione;
         aDestE.CD_ELEMENTO_VOCE_CLGS:=aDett.cd_elemento_voce;
         aDestE.PG_SPESA_CLGS:=aDett.pg_spesa;

         aDestE.IM_RB_RSE:=
            aDett.IM_RH_CCS_COSTI
           +aDett.IM_RM_CSS_AMMORTAMENTI
           +aDett.IM_RN_CSS_RIMANENZE
           +aDett.IM_RO_CSS_ALTRI_COSTI;

     aDestE.IM_RD_A2_RICAVI:=
            aDett.IM_RAA_A2_COSTI_FINALI;

     aDestE.IM_RF_A3_RICAVI:=
            aDett.IM_RAH_A3_COSTI_FINALI;

         aDestE.IM_RA_RCE:=0;
         aDestE.IM_RC_ESR:=0;
         aDestE.IM_RE_A2_ENTRATE:=0;
         aDestE.IM_RG_A3_ENTRATE:=0;

         aDestE.ESERCIZIO_PDG_VARIAZIONE:=aDett.ESERCIZIO_PDG_VARIAZIONE;
         aDestE.PG_VARIAZIONE_PDG:=aDett.PG_VARIAZIONE_PDG;

         aDestE.DACR:=aTSNow;
         aDestE.UTCR:=aUser;
         aDestE.DUVA:=aTSNow;
         aDestE.UTUV:=aUser;
         aDestE.PG_VER_REC:=1;
         CNRCTB050.ins_PDG_PREVENTIVO_ETR_DET (aDestE);

        -- Aggiorno il dettaglio del centro servito con i dati del ricavo figurativo nel servente area

       update PDG_PREVENTIVO_SPE_DET set
        PG_ENTRATA_CLGE = aDestE.pg_entrata,
           CD_CENTRO_RESPONSABILITA_CLGE=aDestE.cd_centro_responsabilita,
           CD_LINEA_ATTIVITA_CLGE=aDestE.cd_linea_attivita,
           TI_APPARTENENZA_CLGE=aDestE.ti_appartenenza,
           TI_GESTIONE_CLGE=aDestE.ti_gestione,
           CD_ELEMENTO_VOCE_CLGE=aDestE.cd_elemento_voce,
           CATEGORIA_DETTAGLIO=CNRCTB050.DETTAGLIO_SCARICO
       where
               ESERCIZIO=aDett.esercizio
           and CD_CENTRO_RESPONSABILITA=aDett.cd_centro_responsabilita
           and CD_LINEA_ATTIVITA=aDett.cd_linea_attivita
           and TI_APPARTENENZA=aDett.ti_appartenenza
           and TI_GESTIONE=aDett.ti_gestione
           and CD_ELEMENTO_VOCE=aDett.cd_elemento_voce
         and PG_SPESA=aDett.pg_spesa;

        -- Se il dettaglio proviene da una variazione PDG di un istituto sulla natura 5
        -- carico l'associazione tra la variazione PDG e il CDR dell'area
         If aDestE.ESERCIZIO_PDG_VARIAZIONE Is Not Null And aDestE.PG_VARIAZIONE_PDG Is Not Null Then
             Begin
            select * into aAssPdgVarCDRE from ass_pdg_variazione_cdr where
                   ESERCIZIO = aDestE.ESERCIZIO And
                   PG_VARIAZIONE_PDG = aDestE.PG_VARIAZIONE_PDG And
                   CD_CENTRO_RESPONSABILITA = aDestE.cd_centro_responsabilita;
           Exception
             When NO_DATA_FOUND Then
               aAssPdgVarCDRE.ESERCIZIO := aDestE.ESERCIZIO;
               aAssPdgVarCDRE.PG_VARIAZIONE_PDG := aDestE.PG_VARIAZIONE_PDG;
               aAssPdgVarCDRE.CD_CENTRO_RESPONSABILITA := aDestE.cd_centro_responsabilita;
               aAssPdgVarCDRE.IM_ENTRATA := 0;
               aAssPdgVarCDRE.IM_SPESA := 0;
                 aAssPdgVarCDRE.DACR:=aTSNow;
                 aAssPdgVarCDRE.UTCR:=aUser;
                 aAssPdgVarCDRE.DUVA:=aTSNow;
                 aAssPdgVarCDRE.UTUV:=aUser;
                 aAssPdgVarCDRE.PG_VER_REC:=1;
                 CNRCTB050.ins_ASS_PDG_VARIAZIONE_CDR(aAssPdgVarCDRE);
             End;
           End If;

     -- Creo la spesa nel centro servente

     -- Per prima cosa è necessario creare la linea di attività collegata a quella di partenza (spesa servito)
     begin
      -- Devo selezionare la linea di spesa collegata
      -- Si tratta di una linea di attività di spesa propria dell'AREA
        select * into aLACollegataS from linea_attivita where
               CD_CENTRO_RESPONSABILITA = aCDRArea.cd_centro_responsabilita
         and CD_LA_COLLEGATO=aLA.cd_linea_attivita
         and CD_CDR_COLLEGATO=aCDR.cd_centro_responsabilita
           and CD_TIPO_LINEA_ATTIVITA = CNRCTB010.TI_LA_PROP
       and TI_GESTIONE = CNRCTB010.TI_GESTIONE_SPESE;
       exception
        when NO_DATA_FOUND then
            aInsieme:=CNRCTB010.getInsiemeScrArea(aEsercizio, aCDRArea.cd_centro_responsabilita);
            aLACollegataS.ESERCIZIO_INIZIO:= aEsercizio;
            aLACollegataS.ESERCIZIO_FINE:= CNRCTB008.ESERCIZIO_INFINITO;
            aLACollegataS.CD_CENTRO_RESPONSABILITA:=aCDRArea.cd_centro_responsabilita;
            aLACollegataS.CD_FUNZIONE := aLA.cd_funzione;
            aLACollegataS.CD_NATURA := aLA.cd_natura;
            aLACollegataS.CD_TIPO_LINEA_ATTIVITA := CNRCTB010.TI_LA_PROP;
            aLACollegataS.ds_linea_attivita:=aLA.ds_linea_attivita;
            aLACollegataS.denominazione:=CNRCTB010.SPESE_PER_COSTI_ALTRUI;
            aLACollegataS.cd_la_collegato:=aLA.cd_linea_attivita;
            aLACollegataS.cd_cdr_collegato:=aCDR.cd_centro_responsabilita;
            aLACollegataS.CD_LINEA_ATTIVITA := CNRCTB010.getNextCodice(CNRCTB010.TI_TIPO_LA_PROPRIA,aLACollegataS);
            aLACollegataS.TI_GESTIONE := CNRCTB010.TI_GESTIONE_SPESE;
            aLACollegataS.CD_INSIEME_LA := aInsieme.CD_INSIEME_LA;
            aLACollegataS.DACR:=aTSNow;
            aLACollegataS.UTCR:=aUser;
            aLACollegataS.DUVA:=aTSNow;
            aLACollegataS.UTUV:=aUser;
            aLACollegataS.PG_VER_REC:=1;
            CNRCTB010.ins_LINEA_ATTIVITA(aLACollegataS);

            INS_ASS_GAE_ESERCIZIO(recParametriCnr, aLACollegataS, aTSNow, aUser);

            CNRCTB010.COPIARISULTATI(aLACollegataS,aLA);

            select * into aLACollegataS from linea_attivita where
                   CD_CENTRO_RESPONSABILITA=aCDRArea.cd_centro_responsabilita
             and CD_LINEA_ATTIVITA = aLACollegataS.cd_linea_attivita;
         end;

         aDestS:=NULL;
         aPgDettaglioColl:=0;
         select NVL(max(pg_spesa),0) into aPgDettaglioColl from PDG_PREVENTIVO_SPE_DET where
             ESERCIZIO=aEsercizio
            and CD_CENTRO_RESPONSABILITA=aCDRArea.cd_centro_responsabilita
            and CD_LINEA_ATTIVITA=aLACollegataS.cd_linea_attivita
            and TI_APPARTENENZA=aDett.ti_appartenenza
            and TI_GESTIONE=aDett.ti_gestione
            and CD_ELEMENTO_VOCE=aDett.cd_elemento_voce;

         aPgDettaglioColl:=aPgDettaglioColl+1;

     -- Il dettaglio di spesa nell'area = a quello di spesa del servito

     aDestS:=NULL;
         aDestS.ESERCIZIO:=aEsercizio;
         aDestS.CD_CENTRO_RESPONSABILITA:=aCDRArea.CD_CENTRO_RESPONSABILITA;
         aDestS.CD_LINEA_ATTIVITA:=aLACollegataS.CD_LINEA_ATTIVITA;
         aDestS.CD_FUNZIONE:=aLACollegataS.cd_funzione;
         aDestS.CD_NATURA:=aLACollegataS.cd_natura;

         aDestS.TI_APPARTENENZA:=aDett.TI_APPARTENENZA;
         aDestS.TI_GESTIONE:=aDett.TI_GESTIONE;
         aDestS.CD_ELEMENTO_VOCE:=aDett.CD_ELEMENTO_VOCE;
         aDestS.PG_SPESA:=aPgDettaglioColl;
         aDestS.DT_REGISTRAZIONE:=aTSNow;
         aDestS.DESCRIZIONE:=aDett.DESCRIZIONE;
         aDestS.STATO:=aDett.STATO;
         aDestS.ORIGINE:=aDett.ORIGINE;
         aDestS.CATEGORIA_DETTAGLIO:=aDett.CATEGORIA_DETTAGLIO;
         aDestS.FL_SOLA_LETTURA:=aDett.FL_SOLA_LETTURA;

--         aDestS.CD_CENTRO_RESPONSABILITA_CLGE:=aDett.CD_CENTRO_RESPONSABILITA_CLGE;
--         aDestS.CD_LINEA_ATTIVITA_CLGE:=aDett.CD_LINEA_ATTIVITA_CLGE;
--         aDestS.TI_APPARTENENZA_CLGE:=aDett.TI_APPARTENENZA_CLGE;
--         aDestS.TI_GESTIONE_CLGE:=aDett.TI_GESTIONE_CLGE;
--         aDestS.CD_ELEMENTO_VOCE_CLGE:=aDett.CD_ELEMENTO_VOCE_CLGE;
--         aDestS.PG_ENTRATA_CLGE:=aDett.PG_ENTRATA_CLGE;

         aDestS.IM_RH_CCS_COSTI:=aDett.IM_RH_CCS_COSTI;
         aDestS.IM_RI_CCS_SPESE_ODC:=aDett.IM_RI_CCS_SPESE_ODC;
         aDestS.IM_RJ_CCS_SPESE_ODC_ALTRA_UO:=aDett.IM_RJ_CCS_SPESE_ODC_ALTRA_UO;
         aDestS.IM_RK_CCS_SPESE_OGC:=aDett.IM_RK_CCS_SPESE_OGC;
         aDestS.IM_RL_CCS_SPESE_OGC_ALTRA_UO:=aDett.IM_RL_CCS_SPESE_OGC_ALTRA_UO;
         aDestS.IM_RM_CSS_AMMORTAMENTI:=aDett.IM_RM_CSS_AMMORTAMENTI;
         aDestS.IM_RN_CSS_RIMANENZE:=aDett.IM_RN_CSS_RIMANENZE;
         aDestS.IM_RO_CSS_ALTRI_COSTI:=aDett.IM_RO_CSS_ALTRI_COSTI;
         aDestS.IM_RP_CSS_VERSO_ALTRO_CDR:=aDett.IM_RP_CSS_VERSO_ALTRO_CDR;
         aDestS.IM_RQ_SSC_COSTI_ODC:=aDett.IM_RQ_SSC_COSTI_ODC;
         aDestS.IM_RR_SSC_COSTI_ODC_ALTRA_UO:=aDett.IM_RR_SSC_COSTI_ODC_ALTRA_UO;
         aDestS.IM_RS_SSC_COSTI_OGC:=aDett.IM_RS_SSC_COSTI_OGC;
         aDestS.IM_RT_SSC_COSTI_OGC_ALTRA_UO:=aDett.IM_RT_SSC_COSTI_OGC_ALTRA_UO;
         aDestS.IM_RU_SPESE_COSTI_ALTRUI:=aDett.IM_RU_SPESE_COSTI_ALTRUI;
         aDestS.IM_RV_PAGAMENTI:=aDett.IM_RV_PAGAMENTI;
         aDestS.IM_RAA_A2_COSTI_FINALI:=aDett.IM_RAA_A2_COSTI_FINALI;
         aDestS.IM_RAB_A2_COSTI_ALTRO_CDR:=aDett.IM_RAB_A2_COSTI_ALTRO_CDR;
         aDestS.IM_RAC_A2_SPESE_ODC:=aDett.IM_RAC_A2_SPESE_ODC;
         aDestS.IM_RAD_A2_SPESE_ODC_ALTRA_UO:=aDett.IM_RAD_A2_SPESE_ODC_ALTRA_UO;
         aDestS.IM_RAE_A2_SPESE_OGC:=aDett.IM_RAE_A2_SPESE_OGC;
         aDestS.IM_RAF_A2_SPESE_OGC_ALTRA_UO:=aDett.IM_RAF_A2_SPESE_OGC_ALTRA_UO;
         aDestS.IM_RAG_A2_SPESE_COSTI_ALTRUI:=aDett.IM_RAG_A2_SPESE_COSTI_ALTRUI;
         aDestS.IM_RAH_A3_COSTI_FINALI:=aDett.IM_RAH_A3_COSTI_FINALI;
         aDestS.IM_RAI_A3_COSTI_ALTRO_CDR:=aDett.IM_RAI_A3_COSTI_ALTRO_CDR;
         aDestS.IM_RAL_A3_SPESE_ODC:=aDett.IM_RAL_A3_SPESE_ODC;
         aDestS.IM_RAM_A3_SPESE_ODC_ALTRA_UO:=aDett.IM_RAM_A3_SPESE_ODC_ALTRA_UO;
         aDestS.IM_RAN_A3_SPESE_OGC:=aDett.IM_RAN_A3_SPESE_OGC;
         aDestS.IM_RAO_A3_SPESE_OGC_ALTRA_UO:=aDett.IM_RAO_A3_SPESE_OGC_ALTRA_UO;
         aDestS.IM_RAP_A3_SPESE_COSTI_ALTRUI:=aDett.IM_RAP_A3_SPESE_COSTI_ALTRUI;

         aDestS.ESERCIZIO_PDG_VARIAZIONE:=aDett.ESERCIZIO_PDG_VARIAZIONE;
         aDestS.PG_VARIAZIONE_PDG:=aDett.PG_VARIAZIONE_PDG;

         aDestS.DACR:=aTSNow;
         aDestS.UTCR:=aUser;
         aDestS.DUVA:=aTSNow;
         aDestS.UTUV:=aUser;
         aDestS.PG_VER_REC:=1;

  -- Mantengo il collegamento con il dettaglio verso altra UO esistente eventualmente nel servito

         aDestS.CD_CENTRO_RESPONSABILITA_CLGS:=aDett.CD_CENTRO_RESPONSABILITA_CLGS;
         aDestS.CD_LINEA_ATTIVITA_CLGS:=aDett.CD_LINEA_ATTIVITA_CLGS;
         aDestS.TI_APPARTENENZA_CLGS:=aDett.TI_APPARTENENZA_CLGS;
         aDestS.TI_GESTIONE_CLGS:=aDett.TI_GESTIONE_CLGS;
         aDestS.CD_ELEMENTO_VOCE_CLGS:=aDett.CD_ELEMENTO_VOCE_CLGS;
         aDestS.PG_SPESA_CLGS:=aDett.PG_SPESA_CLGS;

         CNRCTB050.ins_PDG_PREVENTIVO_SPE_DET (aDestS);

     -- Aggiorna il dettaglio eventualmente precedentemente collegato al servito e ora ollegato a dettaglio dell'AREA
         update pdg_preventivo_spe_det set
          cd_centro_responsabilita_clgs=aDestS.CD_CENTRO_RESPONSABILITA,
          CD_LINEA_ATTIVITA_CLGS=aDestS.CD_LINEA_ATTIVITA,
          TI_APPARTENENZA_CLGS=aDestS.TI_APPARTENENZA,
          TI_GESTIONE_CLGS=aDestS.TI_GESTIONE,
          CD_ELEMENTO_VOCE_CLGS=aDestS.CD_ELEMENTO_VOCE,
          PG_SPESA_CLGS=aDestS.PG_SPESA,
      pg_ver_rec=pg_ver_Rec+1,
      utuv=aUser,
      duva=aTSNow
     where
               esercizio = aDestS.esercizio
           And CD_CENTRO_RESPONSABILITA=aDestS.cd_centro_responsabilita_clgs
           and CD_LINEA_ATTIVITA=aDestS.cd_linea_attivita_clgs
           and TI_APPARTENENZA=aDestS.ti_appartenenza_clgs
           and TI_GESTIONE=aDestS.ti_gestione_clgs
           and CD_ELEMENTO_VOCE=aDestS.cd_elemento_voce_clgs
         and PG_SPESA=aDestS.pg_spesa_clgs;



         -- Aggiornamento del dettaglio del centro servito a livello di importi (aCDR)

         update pdg_preventivo_spe_det set

     -- Sposto gli importi nelle nuove colonne
      IM_RP_CSS_VERSO_ALTRO_CDR=
            IM_RH_CCS_COSTI
           +IM_RM_CSS_AMMORTAMENTI
           +IM_RN_CSS_RIMANENZE
           +IM_RO_CSS_ALTRI_COSTI
         ,IM_RAB_A2_COSTI_ALTRO_CDR=
            IM_RAA_A2_COSTI_FINALI
         ,IM_RAI_A3_COSTI_ALTRO_CDR=
            IM_RAH_A3_COSTI_FINALI

     -- Azzero i costi (H) e le colonne che li determinano (I,J,K,L)
         ,IM_RH_CCS_COSTI=0
         ,IM_RM_CSS_AMMORTAMENTI=0
         ,IM_RN_CSS_RIMANENZE=0
         ,IM_RO_CSS_ALTRI_COSTI=0

         ,IM_RAA_A2_COSTI_FINALI=0
         ,IM_RAH_A3_COSTI_FINALI=0

     ,IM_RI_CCS_SPESE_ODC=0
         ,IM_RK_CCS_SPESE_OGC=0

     -- Azzero il collegamento con il centro servito - è stato scaricato sull'area

         ,cd_centro_responsabilita_clgs=null
         ,CD_LINEA_ATTIVITA_CLGS=null
         ,TI_APPARTENENZA_CLGS=null
         ,TI_GESTIONE_CLGS=null
         ,CD_ELEMENTO_VOCE_CLGS=null
         ,PG_SPESA_CLGS=null

         ,IM_RJ_CCS_SPESE_ODC_ALTRA_UO=0
         ,IM_RL_CCS_SPESE_OGC_ALTRA_UO=0
         ,IM_RR_SSC_COSTI_ODC_ALTRA_UO=0
         ,IM_RT_SSC_COSTI_OGC_ALTRA_UO=0
         ,IM_RAD_A2_SPESE_ODC_ALTRA_UO=0
         ,IM_RAF_A2_SPESE_OGC_ALTRA_UO=0
         ,IM_RAM_A3_SPESE_ODC_ALTRA_UO=0
         ,IM_RAO_A3_SPESE_OGC_ALTRA_UO=0

     ,IM_RQ_SSC_COSTI_ODC=0
         ,IM_RS_SSC_COSTI_OGC=0
         ,IM_RAC_A2_SPESE_ODC=0
         ,IM_RAE_A2_SPESE_OGC=0
         ,IM_RAL_A3_SPESE_ODC=0
         ,IM_RAN_A3_SPESE_OGC=0

     ,IM_RU_SPESE_COSTI_ALTRUI=0
     ,IM_RAP_A3_SPESE_COSTI_ALTRUI=0
     ,IM_RAG_A2_SPESE_COSTI_ALTRUI=0
     ,IM_RV_PAGAMENTI=0

     ,pg_ver_rec=pg_ver_Rec+1
     ,utuv=aUser
     ,duva=aTSNow
     where
               esercizio = aDett.esercizio
           And CD_CENTRO_RESPONSABILITA=aDett.cd_centro_responsabilita
           and CD_LINEA_ATTIVITA=aDett.cd_linea_attivita
           and TI_APPARTENENZA=aDett.ti_appartenenza
           and TI_GESTIONE=aDett.ti_gestione
           and CD_ELEMENTO_VOCE=aDett.cd_elemento_voce
         and PG_SPESA=aDett.pg_spesa;

        -- Se il dettaglio proviene da una variazione PDG di un istituto sulla natura 5
        -- carico l'associazione tra la variazione PDG e il CDR dell'area
        If aDestS.ESERCIZIO_PDG_VARIAZIONE Is Not Null And aDestS.PG_VARIAZIONE_PDG Is Not Null Then
             Begin
            select * into aAssPdgVarCDRS from ass_pdg_variazione_cdr where
                   ESERCIZIO = aDestS.ESERCIZIO And
                   PG_VARIAZIONE_PDG = aDestS.PG_VARIAZIONE_PDG And
                   CD_CENTRO_RESPONSABILITA = aDestS.cd_centro_responsabilita;
           Exception
             When NO_DATA_FOUND Then
               aAssPdgVarCDRS.ESERCIZIO := aDestS.ESERCIZIO;
               aAssPdgVarCDRS.PG_VARIAZIONE_PDG := aDestS.PG_VARIAZIONE_PDG;
               aAssPdgVarCDRS.CD_CENTRO_RESPONSABILITA := aDestS.cd_centro_responsabilita;
               aAssPdgVarCDRS.IM_ENTRATA := 0;
               aAssPdgVarCDRS.IM_SPESA := 0;
                 aAssPdgVarCDRS.DACR:=aTSNow;
                 aAssPdgVarCDRS.UTCR:=aUser;
                 aAssPdgVarCDRS.DUVA:=aTSNow;
                 aAssPdgVarCDRS.UTUV:=aUser;
                 aAssPdgVarCDRS.PG_VER_REC:=1;
                 CNRCTB050.ins_ASS_PDG_VARIAZIONE_CDR(aAssPdgVarCDRS);
             End;
           End If;
 End;

-------------------------------------------------------------------------------------------------
-------------------------------------- NUOVA GESTIONE -------------------------------------------
-------------------------------------------------------------------------------------------------

Procedure ribaltaSuAreaPDGG_etr_Int(
  aEsercizio              number,
  aCdCentroResponsabilita varchar2,
  aCDR                    cdr%rowtype,
  aCDRArea                cdr%rowtype,
  aDett                   pdg_modulo_entrate_gest%rowtype,
  RIGHE_VAR_ENT           pdg_variazione_riga_gest%rowtype,
  recParametriCNR         PARAMETRI_CNR%Rowtype,
  aTSNow                  date,
  aUser                   VARCHAR2) Is

  aLA                   linea_attivita%Rowtype;
  aLACollegataE         linea_attivita%Rowtype;
  aDestE                pdg_modulo_entrate_gest%Rowtype;
  aRighe_var_ent        pdg_variazione_riga_gest%Rowtype;
  aAssPdgVarCDRE        ass_pdg_variazione_cdr%Rowtype;

Begin
    -- Leggo la linea di attività del dettaglio del CDR Assegnatario che deve scaricare sull'Area

-- L'nvl c'è perchè vedendo se è pieno il record del gestionale o delle variazioni capisco di cosa si tratta,
-- se dello scarico del PdG o dei dettagli di variazione

  begin
        Select *
        Into  aLA
        From  linea_attivita
        Where CD_CENTRO_RESPONSABILITA = Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_ENT.CD_CDR_ASSEGNATARIO) And
              CD_LINEA_ATTIVITA        = Nvl(aDett.cd_linea_attivita, RIGHE_VAR_ENT.cd_linea_attivita);
      exception
        when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Linea di attività di entrata del dettaglio del CdR che scarica su Area non trovata!');
        End;

  -- Ricerca o creazione della nuova linea di attività dell'AREA creata dalla Linea
  -- del CdR (finale) che sta scaricando

  begin
        -- Seleziono la linea di entrata con gli stessi CdR/Linea di origine

        select *
        into  aLACollegataE
        from  linea_attivita
        Where CD_CENTRO_RESPONSABILITA = aCDRArea.CD_CENTRO_RESPONSABILITA And
              CD_CDR_COLLEGATO = Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_ENT.CD_CDR_ASSEGNATARIO) And
                CD_LA_COLLEGATO  = Nvl(aDett.CD_LINEA_ATTIVITA, RIGHE_VAR_ENT.CD_LINEA_ATTIVITA);

       exception
        when NO_DATA_FOUND then

            aLACollegataE.CD_CENTRO_RESPONSABILITA      := aCDRArea.cd_centro_responsabilita;
            aLACollegataE.CD_LINEA_ATTIVITA             := CNRCTB010.getNextCodice(CNRCTB010.TI_TIPO_LA_PROPRIA,aLACollegataE);
            aLACollegataE.CD_TIPO_LINEA_ATTIVITA        := CNRCTB010.TI_LA_PROP;
            aLACollegataE.denominazione                 := 'Linea generata da '||Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_ENT.CD_CDR_ASSEGNATARIO)||
                                                           ' ('||aCDRArea.DS_CDR||') / '||
                                                           Nvl(aDett.CD_LINEA_ATTIVITA, RIGHE_VAR_ENT.CD_LINEA_ATTIVITA);
            aLACollegataE.CD_NATURA                     := aLA.cd_natura;
            aLACollegataE.ds_linea_attivita             := aLA.ds_linea_attivita ||'('||Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_ENT.CD_CDR_ASSEGNATARIO)||
                                                           '/'||Nvl(aDett.CD_LINEA_ATTIVITA, RIGHE_VAR_ENT.CD_LINEA_ATTIVITA)||')';
            aLACollegataE.ESERCIZIO_INIZIO              := aEsercizio;
            aLACollegataE.ESERCIZIO_FINE                := CNRCTB008.ESERCIZIO_INFINITO;
            aLACollegataE.CD_INSIEME_LA                 := Null;
            aLACollegataE.TI_GESTIONE                   := CNRCTB010.TI_GESTIONE_ENTRATE;
            aLACollegataE.DACR                          := aTSNow;
            aLACollegataE.UTCR                          := aUser;
            aLACollegataE.DUVA                          := aTSNow;
            aLACollegataE.UTUV                          := aUser;
            aLACollegataE.PG_VER_REC                    := 1;
            aLACollegataE.CD_CDR_COLLEGATO              := Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_ENT.CD_CDR_ASSEGNATARIO);
            aLACollegataE.CD_LA_COLLEGATO               := Nvl(aDett.CD_LINEA_ATTIVITA, RIGHE_VAR_ENT.CD_LINEA_ATTIVITA);
            aLACollegataE.PG_PROGETTO                   := Nvl(aDett.pg_progetto, aLa.pg_progetto);
            aLACollegataE.CD_RESPONSABILE_TERZO         := aLA.CD_RESPONSABILE_TERZO;
            aLACollegataE.FL_LIMITE_ASS_OBBLIG          := 'N'; -- DEFAULT SFONDABILE

            aLACollegataE.CD_MISSIONE                   := aLA.cd_missione;
            If Nvl(aDett.pg_progetto, aLa.pg_progetto) = aLA.pg_progetto Then
               aLACollegataE.CD_PROGRAMMA               := aLA.cd_programma;
            End If;

----DBMS_OUTPUT.PUT_LINE ('INSERISCE LA '||aLACollegataE.CD_CENTRO_RESPONSABILITA||' '||aLACollegataE.CD_LINEA_ATTIVITA);

            CNRCTB010.ins_LINEA_ATTIVITA(aLACollegataE);

	    INS_ASS_GAE_ESERCIZIO(recParametriCnr, aLACollegataE, aTSNow, aUser);

            select *
            into  aLACollegataE
            from  linea_attivita
            Where CD_CENTRO_RESPONSABILITA = aCDRArea.cd_centro_responsabilita And
                  CD_LINEA_ATTIVITA        = aLACollegataE.CD_LINEA_ATTIVITA;
     end;

If aDett.esercizio Is Not Null Then     -- scarico del Piano di Gestione gestionale, non per variazione

         aDestE := NULL;

     -- Inserisco la riga per L'Area (scaricata)

         aDestE.ESERCIZIO                        := aDett.esercizio;
         aDestE.CD_CENTRO_RESPONSABILITA         := aDett.CD_CENTRO_RESPONSABILITA;
         aDestE.PG_PROGETTO                      := aDett.PG_PROGETTO;
         aDestE.CD_NATURA                        := aDett.CD_NATURA;
         aDestE.ID_CLASSIFICAZIONE               := aDett.ID_CLASSIFICAZIONE;
         aDestE.CD_CDS_AREA                      := aDett.CD_CDS_AREA;
         aDestE.PG_DETTAGLIO                     := aDett.PG_DETTAGLIO;
         aDestE.CD_CDR_ASSEGNATARIO              := aCDRArea.cd_centro_responsabilita;
         aDestE.CD_LINEA_ATTIVITA                := aLACollegataE.cd_linea_attivita;
         aDestE.TI_APPARTENENZA                  := aDett.TI_APPARTENENZA;
         aDestE.TI_GESTIONE                      := aDett.TI_GESTIONE;
         aDestE.CD_ELEMENTO_VOCE                 := aDett.CD_ELEMENTO_VOCE;
         aDestE.DT_REGISTRAZIONE                 := aDett.DT_REGISTRAZIONE;
         aDestE.DESCRIZIONE                      := aDett.DESCRIZIONE;
         aDestE.ORIGINE                          := aDett.ORIGINE;             -- cioè DIR
         aDestE.CATEGORIA_DETTAGLIO              := aDett.CATEGORIA_DETTAGLIO; -- cioè PRE
         aDestE.FL_SOLA_LETTURA                  := 'Y';
         aDestE.IM_ENTRATA                       := aDett.IM_ENTRATA;
         aDestE.IM_INCASSI                       := aDett.IM_INCASSI;
         aDestE.CD_CDR_ASSEGNATARIO_CLGE         := aDett.CD_CDR_ASSEGNATARIO;
         aDestE.CD_LINEA_ATTIVITA_CLGE           := aDett.CD_LINEA_ATTIVITA;
         aDestE.ESERCIZIO_PDG_VARIAZIONE         := aDett.ESERCIZIO_PDG_VARIAZIONE;
         aDestE.PG_VARIAZIONE_PDG                := aDett.PG_VARIAZIONE_PDG;
         aDestE.DACR                             := aTSNow;
         aDestE.UTCR                             := aUser;
         aDestE.DUVA                             := aTSNow;
         aDestE.UTUV                             := aUser;
         aDestE.PG_VER_REC                       := 1;

         CNRCTB051.ins_PDG_MODULO_ENTRATE_GEST (aDestE);

        -- Aggiorno il dettaglio ORIGINALE con:
        -- il CdR/Linea della riga che ha creato
        -- la Categoria_dettaglio "Scaricato"

       Update PDG_MODULO_ENTRATE_GEST
       Set    CD_CDR_ASSEGNATARIO_CLGE = aCDRArea.cd_centro_responsabilita,
                CD_LINEA_ATTIVITA_CLGE   = aLACollegataE.cd_linea_attivita,
              CATEGORIA_DETTAGLIO      = CNRCTB050.DETTAGLIO_SCARICO -- SCR
       Where ESERCIZIO                = aDett.ESERCIZIO                and
             CD_CENTRO_RESPONSABILITA = aDett.CD_CENTRO_RESPONSABILITA and
             PG_PROGETTO              = aDett.PG_PROGETTO              and
             CD_NATURA                = aDett.CD_NATURA                and
             ID_CLASSIFICAZIONE       = aDett.ID_CLASSIFICAZIONE       and
             CD_CDS_AREA              = aDett.CD_CDS_AREA              and
             PG_DETTAGLIO             = aDett.PG_DETTAGLIO             and
             CD_CDR_ASSEGNATARIO      = aDett.CD_CDR_ASSEGNATARIO      and
             CD_LINEA_ATTIVITA        = aDett.CD_LINEA_ATTIVITA        and
             TI_APPARTENENZA          = aDett.TI_APPARTENENZA          and
             TI_GESTIONE              = aDett.TI_GESTIONE              and
             CD_ELEMENTO_VOCE         = aDett.CD_ELEMENTO_VOCE;

Elsif RIGHE_VAR_ENT.esercizio Is Not Null Then -- scarico delle variazioni al PdG

        -- Se il dettaglio proviene da una variazione PDG di un istituto sulla natura 5
        -- carico l'associazione tra la variazione PDG e il CDR dell'area

             Begin
            select  *
            into    aAssPdgVarCDRE
            from    ass_pdg_variazione_cdr
            where   ESERCIZIO = RIGHE_VAR_ENT.ESERCIZIO And
                        PG_VARIAZIONE_PDG = RIGHE_VAR_ENT.PG_VARIAZIONE_PDG And
                        CD_CENTRO_RESPONSABILITA = aCDRArea.cd_centro_responsabilita;
           Exception
             When NO_DATA_FOUND Then
               aAssPdgVarCDRE.ESERCIZIO                := RIGHE_VAR_ENT.ESERCIZIO;
               aAssPdgVarCDRE.PG_VARIAZIONE_PDG        := RIGHE_VAR_ENT.PG_VARIAZIONE_PDG;
               aAssPdgVarCDRE.CD_CENTRO_RESPONSABILITA := aCDRArea.cd_centro_responsabilita;
               aAssPdgVarCDRE.IM_ENTRATA               := 0;
               aAssPdgVarCDRE.IM_SPESA                 := 0;
                 aAssPdgVarCDRE.DACR                     := aTSNow;
                 aAssPdgVarCDRE.UTCR                     := aUser;
                 aAssPdgVarCDRE.DUVA                     := aTSNow;
                 aAssPdgVarCDRE.UTUV                     := aUser;
                 aAssPdgVarCDRE.PG_VER_REC               := 1;
                 CNRCTB050.ins_ASS_PDG_VARIAZIONE_CDR(aAssPdgVarCDRE);
             End;

         aRighe_var_ent := NULL;

   -- Inserisco la riga per L'Area (scaricata)

         aRighe_var_ent.ESERCIZIO                        := Righe_var_ent.ESERCIZIO;
         aRighe_var_ent.PG_VARIAZIONE_PDG                := Righe_var_ent.PG_VARIAZIONE_PDG;

         Select Nvl(Max(PG_RIGA), 0) + 1
         Into   aRighe_var_ent.PG_RIGA
         From   pdg_variazione_riga_gest
         Where  ESERCIZIO = Righe_var_ent.Esercizio And
                PG_VARIAZIONE_PDG = Righe_var_ent.PG_VARIAZIONE_PDG;

         aRighe_var_ent.CD_CDR_ASSEGNATARIO              := aCDRArea.cd_centro_responsabilita;
         aRighe_var_ent.CD_LINEA_ATTIVITA                := aLACollegataE.cd_linea_attivita;
         aRighe_var_ent.CD_CDS_AREA                      := Righe_var_ent.CD_CDS_AREA;
         aRighe_var_ent.TI_APPARTENENZA                  := Righe_var_ent.TI_APPARTENENZA;
         aRighe_var_ent.TI_GESTIONE                      := Righe_var_ent.TI_GESTIONE;
         aRighe_var_ent.CD_ELEMENTO_VOCE                 := Righe_var_ent.CD_ELEMENTO_VOCE;
         aRighe_var_ent.DT_REGISTRAZIONE                 := Righe_var_ent.DT_REGISTRAZIONE;
         aRighe_var_ent.DESCRIZIONE                      := Righe_var_ent.DESCRIZIONE;
         aRighe_var_ent.CATEGORIA_DETTAGLIO              := Righe_var_ent.CATEGORIA_DETTAGLIO;
         aRighe_var_ent.IM_ENTRATA                       := Righe_var_ent.IM_ENTRATA;
         aRighe_var_ent.IM_SPESE_GEST_DECENTRATA_INT     := 0;
         aRighe_var_ent.IM_SPESE_GEST_DECENTRATA_EST     := 0;
         aRighe_var_ent.IM_SPESE_GEST_ACCENTRATA_INT     := 0;
         aRighe_var_ent.IM_SPESE_GEST_ACCENTRATA_EST     := 0;
         aRighe_var_ent.PG_RIGA_CLGS                     := Righe_var_ent.PG_RIGA;
         aRighe_var_ent.CD_CDR_ASSEGNATARIO_CLGS         := Righe_var_ent.CD_CDR_ASSEGNATARIO;
         aRighe_var_ent.CD_LINEA_ATTIVITA_CLGS           := Righe_var_ent.CD_LINEA_ATTIVITA;
         aRighe_var_ent.DACR                             := aTSNow;
         aRighe_var_ent.UTCR                             := aUser;
         aRighe_var_ent.DUVA                             := aTSNow;
         aRighe_var_ent.UTUV                             := aUser;
         aRighe_var_ent.PG_VER_REC                       := 1;

         CNRCTB051.ins_PDG_VARIAZIONE_RIGA_GEST (aRighe_var_ent);

        -- Aggiorno il dettaglio ORIGINALE con:
        -- la RIGA generata (+ CDR/LINEA GENERATI);
        -- la Categoria_dettaglio "Scaricato"

       Update PDG_VARIAZIONE_RIGA_GEST
       Set    PG_RIGA_CLGS              = aRighe_var_ent.PG_RIGA,
                CD_CDR_ASSEGNATARIO_CLGS  = aRighe_var_ent.CD_CDR_ASSEGNATARIO,
                CD_LINEA_ATTIVITA_CLGS    = aRighe_var_ent.CD_LINEA_ATTIVITA,
              CATEGORIA_DETTAGLIO      = CNRCTB050.DETTAGLIO_SCARICO -- SCR
       Where  ESERCIZIO         = Righe_var_ent.ESERCIZIO And
                PG_VARIAZIONE_PDG = Righe_var_ent.PG_VARIAZIONE_PDG And
                PG_RIGA           = Righe_var_ent.PG_RIGA;

End If;

End;

 procedure ribaltaSuAreaPDGG_spe_Int(
  aEsercizio              number,
  aCdCentroResponsabilita varchar2,
  aCDR                    cdr%rowtype,
  aCDRArea                cdr%rowtype,
  aDett                   pdg_modulo_spese_gest%rowtype,
  RIGHE_VAR_SPE           PDG_VARIAZIONE_RIGA_GEST%rowtype,
  recParametriCNR         PARAMETRI_CNR%Rowtype,
  aTSNow                  date,
  aUser                   varchar2
 ) is
  aLA                   linea_attivita%rowtype;
  aLACollegataS         linea_attivita%rowtype;
  aDestS                pdg_modulo_spese_gest%rowtype;
  aRighe_var_spe        pdg_variazione_riga_gest%Rowtype;
  aAssPdgVarCDRS        ass_pdg_variazione_cdr%Rowtype;
  LIVELLO_COFOG       number:=0;
 begin
    -- Leggo la linea di attività del dettaglio del CDR Assegnatario che deve scaricare sull'Area

-- L'nvl c'è perchè vedendo se è pieno il record del gestionale o delle variazioni capisco di cosa si tratta,
-- se dello scarico del PdG o dei dettagli di variazione

  begin
        select *
        into  aLA
        from  linea_attivita
        Where CD_CENTRO_RESPONSABILITA = Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO) And
              CD_LINEA_ATTIVITA        = Nvl(aDett.cd_linea_attivita, RIGHE_VAR_SPE.cd_linea_attivita);
      exception
        when NO_DATA_FOUND then
       IBMERR001.RAISE_ERR_GENERICO('Linea di attività di spesa del dettaglio del CdR che scarica su Area non trovata!');
        End;

  -- Ricerca o creazione della nuova linea di attività dell'AREA creata dalla Linea
  -- del CdR (finale) che sta scaricando

  begin
        -- verifico se per l'esercizio è necessario gestire il cofog
        select LIVELLO_PDG_COFOG into LIVELLO_COFOG
        from parametri_cnr
        where
          esercizio = aEsercizio;

        -- Seleziono la linea di spesa con gli stessi CdR/Linea di origine
        Begin
          select *
          into  aLACollegataS
          from  linea_attivita
          Where CD_CENTRO_RESPONSABILITA = aCDRArea.CD_CENTRO_RESPONSABILITA And
                CD_CDR_COLLEGATO =  aLA.cd_centro_responsabilita And
                CD_LA_COLLEGATO  =  aLA.CD_LINEA_ATTIVITA and
                --esercizio_inizio >= aEsercizio and
                esercizio_fine >= aEsercizio;
        Exception
          When too_many_rows Then
            For recLACollegataS in (select *
                                    from  linea_attivita
                                    Where CD_CENTRO_RESPONSABILITA = aCDRArea.CD_CENTRO_RESPONSABILITA And
                                          CD_CDR_COLLEGATO =  aLA.cd_centro_responsabilita And
                                          CD_LA_COLLEGATO  =  aLA.CD_LINEA_ATTIVITA and
                                          esercizio_fine >= aEsercizio
                                          Order By esercizio_inizio Desc) Loop
               aLACollegataS := recLACollegataS;
               Exit;
            End Loop;
        End;

        if (LIVELLO_COFOG!=0) then
          update linea_attivita set cd_cofog= aLa.cd_cofog
              where
                CD_CENTRO_RESPONSABILITA = aCDRArea.CD_CENTRO_RESPONSABILITA And
                CD_CDR_COLLEGATO =  aLA.cd_centro_responsabilita And
                CD_LA_COLLEGATO  =  aLA.CD_LINEA_ATTIVITA and
                --esercizio_inizio >= aEsercizio and
                esercizio_fine >= aEsercizio;
        end if;
       exception
        when NO_DATA_FOUND then

            aLACollegataS.CD_CENTRO_RESPONSABILITA      := aCDRArea.cd_centro_responsabilita;
            aLACollegataS.CD_LINEA_ATTIVITA             := CNRCTB010.getNextCodice(CNRCTB010.TI_TIPO_LA_PROPRIA, aLACollegataS);
            aLACollegataS.CD_TIPO_LINEA_ATTIVITA        := CNRCTB010.TI_LA_PROP;
            aLACollegataS.denominazione                 := 'Linea generata da '||Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO)||' ('||aCDRArea.DS_CDR||') / '||
                                                           Nvl(aDett.CD_LINEA_ATTIVITA, RIGHE_VAR_SPE.cd_linea_attivita);
            aLACollegataS.CD_NATURA                     := aLA.cd_natura;
            aLACollegataS.CD_FUNZIONE                   := aLA.CD_FUNZIONE;
            If (Length(aLA.ds_linea_attivita ||'('||Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO)||'/'||Nvl(aDett.cd_linea_attivita, RIGHE_VAR_SPE.cd_linea_attivita)||')') > 300) Then
              aLACollegataS.ds_linea_attivita           := aLA.ds_linea_attivita;
            Else
              aLACollegataS.ds_linea_attivita           := aLA.ds_linea_attivita ||'('||Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO)||
                                                           '/'||Nvl(aDett.cd_linea_attivita, RIGHE_VAR_SPE.cd_linea_attivita)||')';
            End If;
            aLACollegataS.ESERCIZIO_INIZIO              := aEsercizio;
            aLACollegataS.ESERCIZIO_FINE                := CNRCTB008.ESERCIZIO_INFINITO;
            aLACollegataS.CD_INSIEME_LA                 := Null;
            aLACollegataS.TI_GESTIONE                   := CNRCTB010.TI_GESTIONE_SPESE;
            aLACollegataS.DACR                          := aTSNow;
            aLACollegataS.UTCR                          := aUser;
            aLACollegataS.DUVA                          := aTSNow;
            aLACollegataS.UTUV                          := aUser;
            aLACollegataS.PG_VER_REC                    := 1;
            aLACollegataS.CD_CDR_COLLEGATO              := Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO);
            aLACollegataS.CD_LA_COLLEGATO               := Nvl(aDett.cd_linea_attivita, RIGHE_VAR_SPE.cd_linea_attivita);
            aLACollegataS.PG_PROGETTO                   := Nvl(aDett.pg_progetto, aLa.pg_progetto);
            aLACollegataS.CD_RESPONSABILE_TERZO         := aLA.CD_RESPONSABILE_TERZO;
            aLACollegataS.FL_LIMITE_ASS_OBBLIG          := 'Y'; -- DEFAULT NON SFONDABILE
            aLACollegataS.CD_COFOG                      := aLA.CD_COFOG;
            aLACollegataS.CD_MISSIONE                   := aLA.cd_missione;
            If Nvl(aDett.pg_progetto, aLa.pg_progetto) = aLA.pg_progetto Then
               aLACollegataS.CD_PROGRAMMA               := aLA.cd_programma;
            End If;

            CNRCTB010.ins_LINEA_ATTIVITA(aLACollegataS);

	    INS_ASS_GAE_ESERCIZIO(recParametriCnr, aLACollegataS, aTSNow, aUser);

            select *
            into  aLACollegataS
            from  linea_attivita
            Where CD_CENTRO_RESPONSABILITA = aCDRArea.cd_centro_responsabilita And
                  CD_LINEA_ATTIVITA        = aLACollegataS.CD_LINEA_ATTIVITA;
     end;

If aDett.esercizio Is Not Null Then -- scarico del Piano di Gestione gestionale, non per variazione

         aDestS := NULL;

     -- Inserisco la riga per L'Area (scaricata)

         aDestS.ESERCIZIO                     := aDett.esercizio;
         aDestS.CD_CENTRO_RESPONSABILITA      := aDett.CD_CENTRO_RESPONSABILITA;
         aDestS.PG_PROGETTO                   := aDett.PG_PROGETTO;
         aDestS.ID_CLASSIFICAZIONE            := aDett.ID_CLASSIFICAZIONE;
         aDestS.CD_CDS_AREA                   := aDett.CD_CDS_AREA;
         aDestS.PG_DETTAGLIO                  := aDett.PG_DETTAGLIO;
         aDestS.CD_CDR_ASSEGNATARIO           := aCDRArea.cd_centro_responsabilita;
         aDestS.CD_LINEA_ATTIVITA             := aLACollegataS.cd_linea_attivita;
         aDestS.TI_APPARTENENZA               := aDett.TI_APPARTENENZA;
         aDestS.TI_GESTIONE                   := aDett.TI_GESTIONE;
         aDestS.CD_ELEMENTO_VOCE              := aDett.CD_ELEMENTO_VOCE;
         aDestS.DT_REGISTRAZIONE              := aDett.DT_REGISTRAZIONE;
         aDestS.DESCRIZIONE                   := aDett.DESCRIZIONE;
         aDestS.ORIGINE                       := aDett.ORIGINE;              -- cioè DIR
         aDestS.CATEGORIA_DETTAGLIO           := aDett.CATEGORIA_DETTAGLIO;  -- cioè PRE
         aDestS.FL_SOLA_LETTURA               := 'Y';
         aDestS.IM_SPESE_GEST_DECENTRATA_INT  := aDett.IM_SPESE_GEST_DECENTRATA_INT;
         aDestS.IM_SPESE_GEST_DECENTRATA_EST  := aDett.IM_SPESE_GEST_DECENTRATA_EST;
         aDestS.IM_SPESE_GEST_ACCENTRATA_INT  := aDett.IM_SPESE_GEST_ACCENTRATA_INT;
         aDestS.IM_SPESE_GEST_ACCENTRATA_EST  := aDett.IM_SPESE_GEST_ACCENTRATA_EST;
         aDestS.IM_PAGAMENTI                  := aDett.IM_PAGAMENTI;
         aDestS.CD_CDR_ASSEGNATARIO_CLGS      := aDett.CD_CDR_ASSEGNATARIO;
         aDestS.CD_LINEA_ATTIVITA_CLGS        := aDett.CD_LINEA_ATTIVITA;
         aDestS.PG_PROGETTO_CLGS              := aDett.PG_PROGETTO;
         aDestS.ESERCIZIO_PDG_VARIAZIONE      := aDett.ESERCIZIO_PDG_VARIAZIONE;
         aDestS.PG_VARIAZIONE_PDG             := aDett.PG_VARIAZIONE_PDG;
         aDestS.UTCR                          := aUser;
         aDestS.DACR                          := aTSNow;
         aDestS.UTUV                          := aUser;
         aDestS.DUVA                          := aTSNow;
         aDestS.PG_VER_REC                    := 1;

         CNRCTB051.ins_PDG_MODULO_SPESE_GEST (aDestS);

        -- Aggiorno il dettaglio ORIGINALE con:
        -- il CdR/Linea della riga che ha creato
        -- la Categoria_dettaglio "Scaricato"

       update PDG_MODULO_SPESE_GEST
       Set    CD_CDR_ASSEGNATARIO_CLGS = aCDRArea.cd_centro_responsabilita,
                CD_LINEA_ATTIVITA_CLGS   = aLACollegataS.cd_linea_attivita,
                PG_PROGETTO_CLGS         = aDett.PG_PROGETTO,
              CATEGORIA_DETTAGLIO      = CNRCTB050.DETTAGLIO_SCARICO -- SCR
       Where  ESERCIZIO                = aDett.ESERCIZIO                and
                CD_CENTRO_RESPONSABILITA = aDett.CD_CENTRO_RESPONSABILITA and
                PG_PROGETTO              = aDett.PG_PROGETTO              and
                ID_CLASSIFICAZIONE       = aDett.ID_CLASSIFICAZIONE       and
                CD_CDS_AREA              = aDett.CD_CDS_AREA              and
                PG_DETTAGLIO             = aDett.PG_DETTAGLIO             and
                CD_CDR_ASSEGNATARIO      = aDett.CD_CDR_ASSEGNATARIO      and
                CD_LINEA_ATTIVITA        = aDett.CD_LINEA_ATTIVITA        and
                TI_APPARTENENZA          = aDett.TI_APPARTENENZA          and
                TI_GESTIONE              = aDett.TI_GESTIONE              and
                CD_ELEMENTO_VOCE         = aDett.CD_ELEMENTO_VOCE;

Elsif RIGHE_VAR_SPE.esercizio Is Not Null Then -- scarico delle variazioni al PDG

   -- Se il dettaglio proviene da una variazione PDG di un istituto carico l'associazione tra la variazione PDG e il CDR dell'area

             Begin
            select  *
            into    aAssPdgVarCDRS
            from    ass_pdg_variazione_cdr
            where   ESERCIZIO = RIGHE_VAR_SPE.ESERCIZIO And
                        PG_VARIAZIONE_PDG = RIGHE_VAR_SPE.PG_VARIAZIONE_PDG And
                        CD_CENTRO_RESPONSABILITA = aCDRArea.cd_centro_responsabilita;
           Exception
             When NO_DATA_FOUND Then
               aAssPdgVarCDRS.ESERCIZIO                := RIGHE_VAR_SPE.ESERCIZIO;
               aAssPdgVarCDRS.PG_VARIAZIONE_PDG        := RIGHE_VAR_SPE.PG_VARIAZIONE_PDG;
               aAssPdgVarCDRS.CD_CENTRO_RESPONSABILITA := aCDRArea.cd_centro_responsabilita;
               aAssPdgVarCDRS.IM_ENTRATA               := 0;
               aAssPdgVarCDRS.IM_SPESA                 := 0;
                 aAssPdgVarCDRS.DACR                     := aTSNow;
                 aAssPdgVarCDRS.UTCR                     := aUser;
                 aAssPdgVarCDRS.DUVA                     := aTSNow;
                 aAssPdgVarCDRS.UTUV                     := aUser;
                 aAssPdgVarCDRS.PG_VER_REC               := 1;
                 CNRCTB050.ins_ASS_PDG_VARIAZIONE_CDR(aAssPdgVarCDRS);
             End;

         aRighe_var_spe := NULL;

   -- Inserisco la riga per L'Area (scaricata)

         aRighe_var_spe.ESERCIZIO                        := Righe_var_spe.ESERCIZIO;
         aRighe_var_spe.PG_VARIAZIONE_PDG                := Righe_var_spe.PG_VARIAZIONE_PDG;

         Select Nvl(Max(PG_RIGA), 0) + 1
         Into   aRighe_var_spe.PG_RIGA
         From   pdg_variazione_riga_gest
         Where  ESERCIZIO = Righe_var_spe.Esercizio And
                PG_VARIAZIONE_PDG = Righe_var_spe.PG_VARIAZIONE_PDG;

         aRighe_var_spe.CD_CDR_ASSEGNATARIO              := aCDRArea.cd_centro_responsabilita;
         aRighe_var_spe.CD_LINEA_ATTIVITA                := aLACollegataS.cd_linea_attivita;
         aRighe_var_spe.CD_CDS_AREA                      := Righe_var_spe.CD_CDS_AREA;
         aRighe_var_spe.TI_APPARTENENZA                  := Righe_var_spe.TI_APPARTENENZA;
         aRighe_var_spe.TI_GESTIONE                      := Righe_var_spe.TI_GESTIONE;
         aRighe_var_spe.CD_ELEMENTO_VOCE                 := Righe_var_spe.CD_ELEMENTO_VOCE;
         aRighe_var_spe.DT_REGISTRAZIONE                 := Righe_var_spe.DT_REGISTRAZIONE;
         aRighe_var_spe.DESCRIZIONE                      := Righe_var_spe.DESCRIZIONE;
         aRighe_var_spe.CATEGORIA_DETTAGLIO              := Righe_var_spe.CATEGORIA_DETTAGLIO;
         aRighe_var_spe.IM_ENTRATA                       := 0;
         aRighe_var_spe.IM_SPESE_GEST_DECENTRATA_INT     := Righe_var_spe.IM_SPESE_GEST_DECENTRATA_INT;
         aRighe_var_spe.IM_SPESE_GEST_DECENTRATA_EST     := Righe_var_spe.IM_SPESE_GEST_DECENTRATA_EST;
         aRighe_var_spe.IM_SPESE_GEST_ACCENTRATA_INT     := Righe_var_spe.IM_SPESE_GEST_ACCENTRATA_INT;
         aRighe_var_spe.IM_SPESE_GEST_ACCENTRATA_EST     := Righe_var_spe.IM_SPESE_GEST_ACCENTRATA_EST;
         aRighe_var_spe.PG_RIGA_CLGS                     := Righe_var_spe.PG_RIGA;
         aRighe_var_spe.CD_CDR_ASSEGNATARIO_CLGS         := Righe_var_spe.CD_CDR_ASSEGNATARIO;
         aRighe_var_spe.CD_LINEA_ATTIVITA_CLGS           := Righe_var_spe.CD_LINEA_ATTIVITA;
         aRighe_var_spe.DACR                             := aTSNow;
         aRighe_var_spe.UTCR                             := aUser;
         aRighe_var_spe.DUVA                             := aTSNow;
         aRighe_var_spe.UTUV                             := aUser;
         aRighe_var_spe.PG_VER_REC                       := 1;

         CNRCTB051.ins_PDG_VARIAZIONE_RIGA_GEST (aRighe_var_spe);

        -- Aggiorno il dettaglio ORIGINALE con:
        -- la RIGA generata (+ CDR/LINEA GENERATI);
        -- la Categoria_dettaglio "Scaricato"

       Update PDG_VARIAZIONE_RIGA_GEST
       Set    PG_RIGA_CLGS              = aRighe_var_spe.PG_RIGA,
                CD_CDR_ASSEGNATARIO_CLGS  = aRighe_var_spe.CD_CDR_ASSEGNATARIO,
                CD_LINEA_ATTIVITA_CLGS    = aRighe_var_spe.CD_LINEA_ATTIVITA,
              CATEGORIA_DETTAGLIO      = CNRCTB050.DETTAGLIO_SCARICO -- SCR
       Where  ESERCIZIO         = Righe_var_spe.ESERCIZIO And
                PG_VARIAZIONE_PDG = Righe_var_spe.PG_VARIAZIONE_PDG And
                PG_RIGA           = Righe_var_spe.PG_RIGA;

End If;

End;

------------------------------------------------------------------------------------------
---------------------------- RIBALTAMENTO SU ACCENTRATORE --------------------------------
------------------------------------------------------------------------------------------

Procedure ribaltaSuAccentratPDGG_spe_Int(
  aEsercizio              number,
  aCdCentroResponsabilita varchar2,
  aCDR                    cdr%rowtype,
  aCDRArea                cdr%rowtype,
  aDett                   PDG_MODULO_SPESE_GEST%Rowtype,
  RIGHE_VAR_ACCENTRATE    PDG_VARIAZIONE_RIGA_GEST%Rowtype,
  recParametriCnr         PARAMETRI_CNR%rowtype,
  aTSNow                  date,
  aUser                   varchar2
 ) is
  aLA_origine           linea_attivita%rowtype;
  aASS_LA_CLASS         ass_la_class_voci%rowtype;
  ACDRLINEA_DETT        LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA%Type;
  ALINEA_DETT           LINEA_ATTIVITA.CD_LINEA_aTTIVITA%Type;
  aCV                   classificazione_voci%Rowtype;
  aLACollegataS         linea_attivita%rowtype;
  aAssLAEsercizioS      ass_linea_attivita_esercizio%rowtype;
  aDestS                pdg_modulo_spese_gest%rowtype;
  aAssPdgVarCDRE        ass_pdg_variazione_cdr%Rowtype;
  aAssPdgVarCDRS        ass_pdg_variazione_cdr%Rowtype;
  aDettaglioAttuale     pdg_modulo_spese_gest%rowtype;
  aDettaglioNuovo       pdg_modulo_spese_gest%rowtype;
  aRighe_var_spe        PDG_VARIAZIONE_RIGA_GEST%rowtype;
  aTestata_mod_sac      pdg_modulo%rowtype;
  aCDRPersonale         cdr%rowtype;
  aLASAUOP              linea_attivita%rowtype;
  ID_VARIAZIONE         NUMBER;
  LIVELLO_COFOG         NUMBER:=0;
  cofogFin              Varchar2(10);
begin
  If recParametriCNR.esercizio is null Then
     IBMERR001.RAISE_ERR_GENERICO('Parametri CNR per l''esercizio '||aEsercizio||' inesistenti!');
  End If;

  LIVELLO_COFOG := recParametriCNR.LIVELLO_PDG_COFOG;

-- RECUPERO LA CLASSIFICAZIONE O DAL DETTAGLIO DEL PDGG O DALLA VOCE (PER LE VARIAZIONI)

    If adett.id_classificazione Is Not Null Then -- PIANO DI GESTIONE, NON VARIAZIONI
      Select *
      Into   aCV
      From   classificazione_voci
      Where  id_classificazione = adett.id_classificazione;
    Elsif RIGHE_VAR_ACCENTRATE.CD_ELEMENTO_VOCE Is Not Null Then -- VARIAZIONI
      Select ID_CLASSIFICAZIONE
      Into   ID_VARIAZIONE
      From   ELEMENTO_VOCE
      Where  ESERCIZIO        = RIGHE_VAR_ACCENTRATE.ESERCIZIO And
             TI_APPARTENENZA  = RIGHE_VAR_ACCENTRATE.TI_APPARTENENZA And
             TI_GESTIONE      = RIGHE_VAR_ACCENTRATE.TI_GESTIONE And
             CD_ELEMENTO_VOCE = RIGHE_VAR_ACCENTRATE.CD_ELEMENTO_VOCE;
      Select *
      Into   aCV
      From   classificazione_voci
      Where  id_classificazione = ID_VARIAZIONE;
    End If;

    -- Leggo la linea di attività creata dal CdR Assegnatario (che scarica su Accentratore)
    -- per l'ID_CLASSIFICAZIONE,  se esiste. Altrimenti la inserisce
    Begin
        If (LIVELLO_COFOG!=0) then
            select *
            into  aLA_origine
            from  linea_attivita
            Where CD_centro_responsabilita = Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_ACCENTRATE.CD_CDR_ASSEGNATARIO) And
                    cd_linea_attivita      = Nvl(aDett.cd_linea_attivita, RIGHE_VAR_ACCENTRATE.cd_linea_attivita) and
                    cd_cofog is not null;
        else
            select *
            into  aLA_origine
            from  linea_attivita
            Where CD_centro_responsabilita = Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_ACCENTRATE.CD_CDR_ASSEGNATARIO) And
                    cd_linea_attivita        = Nvl(aDett.cd_linea_attivita, RIGHE_VAR_ACCENTRATE.cd_linea_attivita);
        end if;
    Exception
        when NO_DATA_FOUND then
         IBMERR001.RAISE_ERR_GENERICO('Linea di attività del dettaglio del PdG gestionale ('||
                                    Nvl(aDett.cd_linea_attivita, RIGHE_VAR_ACCENTRATE.cd_linea_attivita)||') non trovata !');
    End;

    Declare
      progettoSAC varchar2(100) := CNRCTB015.getVal01PerChiave(aEsercizio, 'MODULO_SPECIALE', 'MODULO_SAC');
      aLaTipoNatura  natura.tipo%Type;
    Begin
        If progettoSAC is null Then
         IBMERR001.RAISE_ERR_GENERICO('Progetto di default per la SAC non trovato!');
        End If;

        -- Leggo il CDR del personale e la sua linea SAUOP

        aCDRPersonale := CNRCTB020.GETCDRPERSONALE(aEsercizio);

        If nvl(recParametriCnr.fl_nuovo_pdg,'N') = 'Y' Then
          If aLa_origine.cd_programma is null or aLa_origine.cd_missione is null Then
           IBMERR001.RAISE_ERR_GENERICO('Linea di attività del dettaglio del PdG gestionale ('||
                                      Nvl(aDett.cd_linea_attivita, RIGHE_VAR_ACCENTRATE.cd_linea_attivita)||') senza indicazione del programma o della missione !');
          End If;

          Begin
            Select tipo Into aLaTipoNatura
            From Natura
            Where CD_NATURA = aLa_origine.cd_natura;
          Exception
            When No_Data_Found Then
              IBMERR001.RAISE_ERR_GENERICO('Errore nella individuazione del tipo natura indicato sulla Gae di origine! Operazione non possibile!');
          End;

          Begin
            Select Distinct CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA
            Into  ACDRLINEA_DETT, ALINEA_DETT
            From  V_LINEA_ATTIVITA_VALIDA, NATURA
            Where CD_CENTRO_RESPONSABILITA = aCV.CDR_ACCENTRATORE
            And   CD_TIPO_LINEA_ATTIVITA = CNRCTB010.TI_LA_PROP
            And   V_LINEA_ATTIVITA_VALIDA.CD_NATURA = NATURA.CD_NATURA
            And   NATURA.TIPO = aLaTipoNatura
            And   TI_GESTIONE = CNRCTB010.TI_GESTIONE_SPESE
            and    V_LINEA_ATTIVITA_VALIDA.esercizio = aEsercizio
            And   (aCV.CDR_ACCENTRATORE = aCDRPersonale.CD_CENTRO_RESPONSABILITA Or
            			(aCV.CDR_ACCENTRATORE != aCDRPersonale.CD_CENTRO_RESPONSABILITA and
                   (PG_PROGETTO = progettoSAC And
                    ((LIVELLO_COFOG=0 or cd_cofog=aLa_origine.cd_cofog)))
                  ))
            And   CD_PROGRAMMA = aLa_origine.cd_programma
            And   CD_MISSIONE = aLa_origine.cd_missione
            And   rownum < 2;
            -- Condizione inserita per evitare Too_Many_Rows.
            -- E' stato chiesto di prendere sempre una anche se ne esistono tante e di non far scattare l'errore
          Exception
            When No_Data_Found Then
              If aCV.CDR_ACCENTRATORE = aCDRPersonale.CD_CENTRO_RESPONSABILITA Then
                IBMERR001.RAISE_ERR_GENERICO('Linea di attività per il CDR accentratore '||aCV.CDR_ACCENTRATORE||' con Natura '||aLaTipoNatura||
                                             ', Programma '||aLa_origine.cd_programma||' e Missione '||aLa_origine.cd_missione||' non presente! Operazione non possibile!');
              Else
                Raise No_Data_Found;
              End If;
            When Too_Many_Rows Then
              IBMERR001.RAISE_ERR_GENERICO('Esiste più di una linea di attività per il CDR accentratore '||aCV.CDR_ACCENTRATORE||' con Natura '||aLaTipoNatura||
                                           ', Programma '||aLa_origine.cd_programma||' e Missione '||aLa_origine.cd_missione||'! Operazione non possibile!');
          End;
        ElsIf aCV.CDR_ACCENTRATORE = aCDRPersonale.CD_CENTRO_RESPONSABILITA Then
          aLASAUOP := CNRCTB010.getLASAUOP(aEsercizio, aCDRPersonale.cd_centro_responsabilita);
          ACDRLINEA_DETT := aLASAUOP.CD_CENTRO_RESPONSABILITA;
          ALINEA_DETT    := aLASAUOP.CD_LINEA_ATTIVITA;
        Else
          Select CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA
          Into  ACDRLINEA_DETT, ALINEA_DETT
          From  ASS_LA_CLASS_VOCI
          Where CD_CDR_ORIGINE    = Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_ACCENTRATE.CD_CDR_ASSEGNATARIO) And
                  ID_CLASS_ORIGINE  = Nvl(aDett.ID_CLASSIFICAZIONE, aCV.ID_CLASSIFICAZIONE)  And
                  CD_NATURA_ORIGINE = aLA_origine.CD_NATURA and
                  ((LIVELLO_COFOG!=0 and cd_cofog=aLa_origine.cd_cofog) or LIVELLO_COFOG=0 );
        End If;

           select cd_cofog into cofogFin from linea_attivita
             where
              CD_CENTRO_RESPONSABILITA = ACDRLINEA_DETT and
              cd_linea_attivita = ALINEA_DETT;
          if (LIVELLO_COFOG!=0 and cofogFin is null) then
              update linea_attivita set cd_cofog= aLa_origine.cd_cofog
              where
              CD_CENTRO_RESPONSABILITA = ACDRLINEA_DETT and
              cd_linea_attivita = ALINEA_DETT;

           end if;
      Exception
        when NO_DATA_FOUND then

            aLACollegataS.CD_CENTRO_RESPONSABILITA      := aCV.CDR_ACCENTRATORE;
            aLACollegataS.CD_LINEA_ATTIVITA             := CNRCTB010.getNextCodice(CNRCTB010.TI_TIPO_LA_PROPRIA, aLACollegataS);
            aLACollegataS.CD_TIPO_LINEA_ATTIVITA        := CNRCTB010.TI_LA_PROP;
            aLACollegataS.denominazione                 := Null;--'Linea generata da '||aDett.CD_CDR_ASSEGNATARIO||' ('||aCDRArea.DS_CDR||') per la Voce '||aDett.ds_classificazione;

            If aCV.CDR_ACCENTRATORE = aCDRPersonale.CD_CENTRO_RESPONSABILITA Then
              aLACollegataS.CD_NATURA                     := '1';
            Else
              aLACollegataS.CD_NATURA                     := aLa_origine.cd_natura;
            End if;

            aLACollegataS.CD_FUNZIONE                   := aLa_origine.cd_funzione;
            aLACollegataS.ds_linea_attivita             := 'Linea generata da '||Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_ACCENTRATE.CD_CDR_ASSEGNATARIO)||
                                                           ' per la Voce '||aCV.ds_classificazione;
            aLACollegataS.ESERCIZIO_INIZIO              := aEsercizio;
            aLACollegataS.ESERCIZIO_FINE                := CNRCTB008.ESERCIZIO_INFINITO;
            aLACollegataS.CD_INSIEME_LA                 := Null;
            aLACollegataS.TI_GESTIONE                   := CNRCTB010.TI_GESTIONE_SPESE;
            aLACollegataS.DACR                          := aTSNow;
            aLACollegataS.UTCR                          := aUser;
            aLACollegataS.DUVA                          := aTSNow;
            aLACollegataS.UTUV                          := aUser;
            aLACollegataS.PG_VER_REC                    := 1;
            aLACollegataS.CD_CDR_COLLEGATO              := Null;
            aLACollegataS.CD_LA_COLLEGATO               := Null;
            aLACollegataS.PG_PROGETTO                   := progettoSAC;
            aLACollegataS.CD_RESPONSABILE_TERZO         := aLa_origine.CD_RESPONSABILE_TERZO;
            aLACollegataS.FL_LIMITE_ASS_OBBLIG          := 'Y'; -- DEFAULT NON SFONDABILE
            aLACollegataS.CD_COFOG                      := aLa_origine.cd_cofog;
            aLACollegataS.CD_PROGRAMMA                  := aLa_origine.cd_programma;
            aLACollegataS.CD_MISSIONE                   := aLa_origine.cd_missione;
            CNRCTB010.ins_LINEA_ATTIVITA(aLACollegataS);

	    INS_ASS_GAE_ESERCIZIO(recParametriCnr, aLACollegataS, aTSNow, aUser);

            If nvl(recParametriCnr.fl_nuovo_pdg,'N') = 'N' Then
              aASS_LA_CLASS.CD_CENTRO_RESPONSABILITA := aLACollegataS.CD_CENTRO_RESPONSABILITA;
              aASS_LA_CLASS.CD_LINEA_ATTIVITA        := aLACollegataS.CD_LINEA_ATTIVITA;
              aASS_LA_CLASS.CD_CDR_ORIGINE           := Nvl(aDett.CD_CDR_ASSEGNATARIO, RIGHE_VAR_ACCENTRATE.CD_CDR_ASSEGNATARIO);
              aASS_LA_CLASS.CD_NATURA_ORIGINE        := aLA_origine.CD_NATURA;
              aASS_LA_CLASS.ID_CLASS_ORIGINE         := Nvl(aDett.ID_CLASSIFICAZIONE, aCV.ID_CLASSIFICAZIONE);
              aASS_LA_CLASS.DS_ASSOCIAZIONE          := 'Associazione automatica per scarico su accentratore';
              aASS_LA_CLASS.UTCR                     := aUser;
              aASS_LA_CLASS.DACR                     := aTSNow;
              aASS_LA_CLASS.UTUV                     := aUser;
              aASS_LA_CLASS.DUVA                     := aTSNow;
              aASS_LA_CLASS.PG_VER_REC               := 1;
              aASS_LA_CLASS.CD_COFOG                 := aLa_origine.cd_cofog;
              CNRCTB010.ins_ASS_LA_CLASS_VOCI(aASS_LA_CLASS);
            End If;

--DBMS_OUTPUT.PUT_LINE ('INSERISCE QUESTA '||aLACollegataS.CD_CENTRO_RESPONSABILITA||' '||aLACollegataS.CD_LINEA_ATTIVITA);

-- MI SELEZIONO QUELLA INSERITA

            Select CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA
            Into  ACDRLINEA_DETT, ALINEA_DETT
            From  linea_attivita
            Where CD_CENTRO_RESPONSABILITA = aLACollegataS.CD_CENTRO_RESPONSABILITA And
                  CD_LINEA_ATTIVITA        = aLACollegataS.CD_LINEA_ATTIVITA;

        End;

-- vedo se esiste già la riga dell'accentratore

If aDett.ESERCIZIO Is Not Null Then -- PIANO DI GESTIONE GESTIONALE, NON VARIAZIONE

        Begin
          Select *
          Into  aDettaglioAttuale
          From  PDG_MODULO_SPESE_GEST
          Where ESERCIZIO                = aEsercizio And
                CD_CENTRO_RESPONSABILITA = aDett.CD_CENTRO_RESPONSABILITA And
                PG_PROGETTO              = aDett.pg_progetto And
                ID_CLASSIFICAZIONE       = aCV.ID_CLASSIFICAZIONE And
                CD_CDS_AREA              = aDett.CD_CDS_AREA And
                PG_DETTAGLIO             = aDett.PG_DETTAGLIO And
                CD_CDR_ASSEGNATARIO      = aCV.CDR_ACCENTRATORE And
                CD_LINEA_ATTIVITA        = ALINEA_DETT And
                TI_APPARTENENZA          = aDett.TI_APPARTENENZA And
                TI_GESTIONE              = aDett.TI_GESTIONE And
                CD_ELEMENTO_VOCE         = aDett.CD_ELEMENTO_VOCE
          For Update NOWAIT;

----DBMS_OUTPUT.PUT_LINE ('VALORE ALA CDR '||Nvl(aLA.CD_CENTRO_RESPONSABILITA, 'NULLO')||' LA '|| Nvl(aLa.CD_LINEA_ATTIVITA, 'NULLA'));
-- se già esiste la incremento

          Update PDG_MODULO_SPESE_GEST
          Set    IM_SPESE_GEST_DECENTRATA_INT = nvl(aDettaglioAttuale.IM_SPESE_GEST_DECENTRATA_INT, 0) + Nvl(aDett.IM_SPESE_GEST_DECENTRATA_INT, 0),
                 IM_SPESE_GEST_DECENTRATA_EST = nvl(aDettaglioAttuale.IM_SPESE_GEST_DECENTRATA_EST, 0) + nvl(aDett.IM_SPESE_GEST_DECENTRATA_EST, 0),
                 IM_SPESE_GEST_ACCENTRATA_INT = nvl(aDettaglioAttuale.IM_SPESE_GEST_ACCENTRATA_INT, 0) + nvl(aDett.IM_SPESE_GEST_ACCENTRATA_INT, 0),
                 IM_SPESE_GEST_ACCENTRATA_EST = nvl(aDettaglioAttuale.IM_SPESE_GEST_ACCENTRATA_EST, 0) + nvl(aDett.IM_SPESE_GEST_ACCENTRATA_EST, 0),
                 IM_PAGAMENTI                 = nvl(aDettaglioAttuale.IM_PAGAMENTI,0) + nvl(aDett.IM_PAGAMENTI, 0)
          Where ESERCIZIO                = aEsercizio And
                CD_CENTRO_RESPONSABILITA = aDett.CD_CENTRO_RESPONSABILITA And
                PG_PROGETTO              = aDett.pg_progetto And
                ID_CLASSIFICAZIONE       = aCV.ID_CLASSIFICAZIONE And
                CD_CDS_AREA              = aDett.CD_CDS_AREA And
                PG_DETTAGLIO             = aDett.PG_DETTAGLIO And
                CD_CDR_ASSEGNATARIO      = aCV.CDR_ACCENTRATORE And
                CD_LINEA_ATTIVITA        = ALINEA_DETT And
                TI_APPARTENENZA          = aDett.TI_APPARTENENZA And
                TI_GESTIONE              = aDett.TI_GESTIONE And
                CD_ELEMENTO_VOCE         = aDett.CD_ELEMENTO_VOCE;
         Exception
           When No_Data_Found Then
                -- se non esiste la creo e Inserisco la riga per L'Accentratore
             Begin
                aDettaglioNuovo.ESERCIZIO                     := aDett.esercizio;
                aDettaglioNuovo.CD_CENTRO_RESPONSABILITA      := aDett.CD_CENTRO_RESPONSABILITA;
                aDettaglioNuovo.PG_PROGETTO                   := aDett.pg_progetto;
                aDettaglioNuovo.ID_CLASSIFICAZIONE            := aDett.ID_CLASSIFICAZIONE;
                aDettaglioNuovo.CD_CDS_AREA                   := aDett.CD_CDS_AREA;
                aDettaglioNuovo.CD_CDR_ASSEGNATARIO           := ACDRLINEA_DETT; -- ACCENTRATORE
                aDettaglioNuovo.CD_LINEA_ATTIVITA             := ALINEA_DETT;
                aDettaglioNuovo.TI_APPARTENENZA               := aDett.TI_APPARTENENZA;
                aDettaglioNuovo.TI_GESTIONE                   := aDett.TI_GESTIONE;
                aDettaglioNuovo.CD_ELEMENTO_VOCE              := aDett.CD_ELEMENTO_VOCE;
                aDettaglioNuovo.DT_REGISTRAZIONE              := aDett.DT_REGISTRAZIONE;
                aDettaglioNuovo.DESCRIZIONE                   := aDett.DESCRIZIONE;
                aDettaglioNuovo.ORIGINE                       := aDett.ORIGINE;              -- cioè DIR
                aDettaglioNuovo.CATEGORIA_DETTAGLIO           := aDett.CATEGORIA_DETTAGLIO;  -- cioè PRE
                aDettaglioNuovo.FL_SOLA_LETTURA               := 'Y';
                aDettaglioNuovo.IM_SPESE_GEST_DECENTRATA_INT  := 0;
                aDettaglioNuovo.IM_SPESE_GEST_DECENTRATA_EST  := 0;
                aDettaglioNuovo.IM_SPESE_GEST_ACCENTRATA_INT  := aDett.IM_SPESE_GEST_ACCENTRATA_INT;
                aDettaglioNuovo.IM_SPESE_GEST_ACCENTRATA_EST  := aDett.IM_SPESE_GEST_ACCENTRATA_EST;
                aDettaglioNuovo.IM_PAGAMENTI                  := 0;
                aDettaglioNuovo.CD_CDR_ASSEGNATARIO_CLGS      := aDett.CD_CDR_ASSEGNATARIO;
                aDettaglioNuovo.CD_LINEA_ATTIVITA_CLGS        := aDett.CD_LINEA_ATTIVITA;
                aDettaglioNuovo.PG_PROGETTO_CLGS              := aDett.PG_PROGETTO;
                aDettaglioNuovo.ESERCIZIO_PDG_VARIAZIONE      := aDett.ESERCIZIO_PDG_VARIAZIONE;
                aDettaglioNuovo.PG_VARIAZIONE_PDG             := aDett.PG_VARIAZIONE_PDG;
                aDettaglioNuovo.UTCR                          := aUser;
                aDettaglioNuovo.DACR                          := aTSNow;
                aDettaglioNuovo.UTUV                          := aUser;
                aDettaglioNuovo.DUVA                          := aTSNow;
                aDettaglioNuovo.PG_VER_REC                    := 1;
                aDettaglioNuovo.PG_DETTAGLIO                  := aDett.PG_DETTAGLIO;
                CNRCTB051.ins_PDG_MODULO_SPESE_GEST (aDettaglioNuovo);

             Exception when dup_val_on_index then
                    null;
             End;
        End;

Elsif RIGHE_VAR_ACCENTRATE.ESERCIZIO Is Not Null Then -- VARIAZIONE, NON PDG GESTIONALE

        -- Se il dettaglio proviene da una variazione PDG di un istituto carico l'associazione tra la variazione PDG e il CDR dell'area

    Begin
      select  *
      into    aAssPdgVarCDRS
      from    ass_pdg_variazione_cdr
      where   ESERCIZIO = RIGHE_VAR_ACCENTRATE.ESERCIZIO And
                PG_VARIAZIONE_PDG = RIGHE_VAR_ACCENTRATE.PG_VARIAZIONE_PDG And
                CD_CENTRO_RESPONSABILITA = ACDRLINEA_DETT;
    Exception
       When NO_DATA_FOUND Then
         aAssPdgVarCDRS.ESERCIZIO                := RIGHE_VAR_ACCENTRATE.ESERCIZIO;
         aAssPdgVarCDRS.PG_VARIAZIONE_PDG        := RIGHE_VAR_ACCENTRATE.PG_VARIAZIONE_PDG;
         aAssPdgVarCDRS.CD_CENTRO_RESPONSABILITA := ACDRLINEA_DETT;
         aAssPdgVarCDRS.IM_ENTRATA               := 0;
         aAssPdgVarCDRS.IM_SPESA                 := 0;
         aAssPdgVarCDRS.DACR                     := aTSNow;
         aAssPdgVarCDRS.UTCR                     := aUser;
         aAssPdgVarCDRS.DUVA                     := aTSNow;
         aAssPdgVarCDRS.UTUV                     := aUser;
         aAssPdgVarCDRS.PG_VER_REC               := 1;
         CNRCTB050.ins_ASS_PDG_VARIAZIONE_CDR(aAssPdgVarCDRS);
    End;

         aRighe_var_spe := NULL;

   -- Inserisco la riga per L'Area (scaricata)

         aRighe_var_spe.ESERCIZIO                        := RIGHE_VAR_ACCENTRATE.ESERCIZIO;
         aRighe_var_spe.PG_VARIAZIONE_PDG                := RIGHE_VAR_ACCENTRATE.PG_VARIAZIONE_PDG;

         Select Nvl(Max(PG_RIGA), 0) + 1
         Into   aRighe_var_spe.PG_RIGA
         From   pdg_variazione_riga_gest
         Where  ESERCIZIO = RIGHE_VAR_ACCENTRATE.Esercizio And
                PG_VARIAZIONE_PDG = RIGHE_VAR_ACCENTRATE.PG_VARIAZIONE_PDG;

         aRighe_var_spe.CD_CDR_ASSEGNATARIO              := ACDRLINEA_DETT;
         aRighe_var_spe.CD_LINEA_ATTIVITA                := ALINEA_DETT;
         aRighe_var_spe.CD_CDS_AREA                      := RIGHE_VAR_ACCENTRATE.CD_CDS_AREA;
         aRighe_var_spe.TI_APPARTENENZA                  := RIGHE_VAR_ACCENTRATE.TI_APPARTENENZA;
         aRighe_var_spe.TI_GESTIONE                      := RIGHE_VAR_ACCENTRATE.TI_GESTIONE;
         aRighe_var_spe.CD_ELEMENTO_VOCE                 := RIGHE_VAR_ACCENTRATE.CD_ELEMENTO_VOCE;
         aRighe_var_spe.DT_REGISTRAZIONE                 := RIGHE_VAR_ACCENTRATE.DT_REGISTRAZIONE;
         aRighe_var_spe.DESCRIZIONE                      := RIGHE_VAR_ACCENTRATE.DESCRIZIONE;
         aRighe_var_spe.CATEGORIA_DETTAGLIO              := RIGHE_VAR_ACCENTRATE.CATEGORIA_DETTAGLIO;
         aRighe_var_spe.IM_ENTRATA                       := 0;
         aRighe_var_spe.IM_SPESE_GEST_DECENTRATA_INT     := 0;
         aRighe_var_spe.IM_SPESE_GEST_DECENTRATA_EST     := 0;
         aRighe_var_spe.IM_SPESE_GEST_ACCENTRATA_INT     := RIGHE_VAR_ACCENTRATE.IM_SPESE_GEST_ACCENTRATA_INT;
         aRighe_var_spe.IM_SPESE_GEST_ACCENTRATA_EST     := RIGHE_VAR_ACCENTRATE.IM_SPESE_GEST_ACCENTRATA_EST;
         aRighe_var_spe.PG_RIGA_CLGS                     := RIGHE_VAR_ACCENTRATE.PG_RIGA;
         aRighe_var_spe.CD_CDR_ASSEGNATARIO_CLGS         := RIGHE_VAR_ACCENTRATE.CD_CDR_ASSEGNATARIO;
         aRighe_var_spe.CD_LINEA_ATTIVITA_CLGS           := RIGHE_VAR_ACCENTRATE.CD_LINEA_ATTIVITA;
         aRighe_var_spe.DACR                             := aTSNow;
         aRighe_var_spe.UTCR                             := aUser;
         aRighe_var_spe.DUVA                             := aTSNow;
         aRighe_var_spe.UTUV                             := aUser;
         aRighe_var_spe.PG_VER_REC                       := 1;

         CNRCTB051.ins_PDG_VARIAZIONE_RIGA_GEST (aRighe_var_spe);

        -- Aggiorno il dettaglio ORIGINALE con:
        -- la RIGA generata (+ CDR/LINEA GENERATI);
        -- la Categoria_dettaglio "Scaricato"

       Update PDG_VARIAZIONE_RIGA_GEST
       Set    PG_RIGA_CLGS              = aRighe_var_spe.PG_RIGA,
                CD_CDR_ASSEGNATARIO_CLGS  = aRighe_var_spe.CD_CDR_ASSEGNATARIO,
                CD_LINEA_ATTIVITA_CLGS    = aRighe_var_spe.CD_LINEA_ATTIVITA,
              CATEGORIA_DETTAGLIO      = CNRCTB050.DETTAGLIO_SCARICO -- SCR
       Where  ESERCIZIO         = RIGHE_VAR_ACCENTRATE.ESERCIZIO And
                PG_VARIAZIONE_PDG = RIGHE_VAR_ACCENTRATE.PG_VARIAZIONE_PDG And
                PG_RIGA           = RIGHE_VAR_ACCENTRATE.PG_RIGA;

End If;

End;


 Procedure ribaltaSuAreaPDG(
  aEsercizio number,
  aCdCentroResponsabilita varchar2,
  aUser varchar2
 ) is
  aCDRPrimo cdr%rowtype;
  aCDR cdr%rowtype;
  aCDRArea cdr%rowtype;
  aUOCDS unita_organizzativa%rowtype;
  aTSNow date;
  aEVCollegato elemento_voce%rowtype;
  aStatoRibaltamento char(1);
  aPDG pdg_preventivo%rowtype;
  aFlagRibaltato boolean;
  recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEsercizio);

  If recParametriCNR.esercizio is null Then
     IBMERR001.RAISE_ERR_GENERICO('Parametri CNR per l''esercizio '||aEsercizio||' inesistenti!');
  End If;

  aCDRPrimo:=CNRCTB020.GETCDRVALIDO(aEsercizio,aCdCentroResponsabilita);

  -- Verifica che il CDR non sia un CDR di AREA

  aUOCDS:=CNRCTB020.GETUOCDS(aCDRPrimo);

  if aUOCDS.cd_tipo_unita = CNRCTB020.TIPO_AREA then
   IBMERR001.RAISE_ERR_GENERICO('Ribaltamento non permesso per CDR appartenente ad area!');
  end if;

  if aCDRPrimo.livello <> 1 then
   IBMERR001.RAISE_ERR_GENERICO('CDR non di primo livello!');
  end if;

  -- Verifica che il PDG del CDR non sia già stato ribaltato sull'area

  select fl_ribaltato_su_area into aStatoRibaltamento from pdg_preventivo
   where
        esercizio = aEsercizio
  and cd_centro_responsabilita = aCdCentroResponsabilita;

  if aStatoRibaltamento = 'Y' then
   IBMERR001.RAISE_ERR_GENERICO('Ribaltamento del cdr '||aCdCentroResponsabilita||' già effettuato');
  end if;

  -- Verifica che il BILANCIO finanziario dell'ente sia stato approvato

  if CNRCTB054.isBilancioCnrApprovato(aEsercizio) = 'N' then
   IBMERR001.RAISE_ERR_GENERICO('Bilancio preventivo CNR non ancora approvato!');
  end if;

  if CNRCTB020.ISCDRENTE(aCDRPrimo) then
   IBMERR001.RAISE_ERR_GENERICO('Il CDR '||aCdCentroResponsabilita||' è un CDR ENTE!');
  end if;

  aTSNow:=sysdate;

  CNRCTB050.lockPdg(aEsercizio, aCDRPrimo.cd_centro_responsabilita);

  if not (CNRCTB050.getStato(aEsercizio, aCDRPrimo.cd_centro_responsabilita)=CNRCTB050.STATO_PDG_FINALE) then
   IBMERR001.raise_err_generico('Il piano di gestione di primo livello non è in stato definitivo!');
  end if;

  aFlagRibaltato:=false;

  -- Da qui in poi comincia la vera operazione di ribaltamento che va ripetuta su tutti i CDR figli
  -- del CDR di primo livello considerato

  for aPDG in CNRCTB055.TUTTI_PDG_LINEA(aEsercizio, aCDRPrimo) loop

   CNRCTB050.lockPdg(aEsercizio, aPDG.cd_centro_responsabilita);

   select * into aCDR from cdr where
    cd_centro_responsabilita = aPDG.cd_centro_responsabilita;

   -- estraggo l'area

   aCDRArea:=CNRCTB020.getCDRArea(aCDR);

   -- se l'area non viene trovata non c'è niente da ribaltare
   if aCDRArea.cd_centro_responsabilita is not null then

    CNRCTB050.lockPdg(aEsercizio, aCDRArea.cd_centro_responsabilita);

    -- Estraggo il conto di ricavo figurativo collegato

  aEVCollegato:=CNRCTB000.GETVOCERICFIGALTROCDR(aEsercizio);

    -- Ciclo sui dettagli del PDG con natura 5

    for aDett in CNRCTB050.DETTAGLI_PDG_NATURA_5(aEsercizio, aCDR.cd_centro_responsabilita) loop
   ribaltaSuAreaPDGInt(
      aEsercizio,
      aCdCentroResponsabilita,
      aCDR,
      aCDRArea,
      aEVCollegato,
      aDett,
      recParametriCNR,
      aTSNow,
      aUser
     );
    end loop;

  -- Che esistano o meno dettagli da scaricare sull'area, viene aggiornato lo stato del piano come scaricato su area
  -- Inoltre setto il flag aFlagRibaltato atrue per poi aggiornare lo stato di ribaltato su AREA del CDR di primo livello per indicare
  -- che la sua linea è stata ribaltata su AREA

     aFlagRibaltato:=true;
     update pdg_preventivo set
       fl_ribaltato_su_area = 'Y'
      ,duva=aTSNow
      ,utuv=aUser
      ,pg_ver_rec=pg_ver_rec+1
     where
          esercizio = aEsercizio
      and cd_centro_responsabilita =  aCDR.cd_centro_responsabilita;
   end if;
  end loop;

  if aFlagRibaltato = true then
   update pdg_preventivo set
     fl_ribaltato_su_area = 'Y'
    ,duva=aTSNow
    ,utuv=aUser
    ,pg_ver_rec=pg_ver_rec+1
   where
        esercizio = aEsercizio
    and cd_centro_responsabilita =  aCDRPrimo.cd_centro_responsabilita;
  end if;
 end;


Function getVoce_FdaEV (aEV elemento_voce%Rowtype, aLinea_attivita linea_attivita%Rowtype) Return  voce_f%Rowtype is

aVocef  voce_f%Rowtype;
aCDR    cdr%Rowtype;
UO_IN   unita_organizzativa%Rowtype;
CDS_IN  unita_organizzativa.cd_unita_organizzativa%Type;
cds_sac unita_organizzativa%Rowtype;

Begin

-- recupero il CDS SAC che mi serve dopo per

CDS_SAC := CNRCTB020.GETCDSSACVALIDO (aEv.esercizio);

-- prendo tutta la riga del CDR (della linea) da aggiornare

Select *
Into  aCDR
From  cdr
Where cd_centro_responsabilita = aLinea_attivita.cd_centro_responsabilita;

-- CDS del CDR da aggiornare

CDS_IN := CNRUTL001.GETCDSFROMCDR(aCDR.cd_centro_responsabilita);

-- UO del CDR da aggiornare

UO_IN := cnrctb020.getuo(aCDR);

------- LA FUNZIONE RESTITUISCE VALORI SOLO PER SPESE CDS ES ENTRATE CNR

If aEV.ti_gestione = 'S' Then -- SPESA

   If aEV.ti_appartenenza = 'C' Then -- SPESE PER IL BILANCIO DELL'ENTE

        aVocef := Null;
/*
      If CNRCTB020.ISCDRENTE(aCDR) Then -- spesa ente per SAC
         Select *
         Into   aVocef
         From   voce_f
         Where  esercizio = aEv.esercizio And
                ti_appartenenza = aEv.ti_appartenenza And
                ti_gestione = aEv.ti_gestione And
                cd_titolo_capitolo = aEv.cd_elemento_voce And
                cd_cds = '000' And
                cd_natura = aLinea_attivita.cd_natura And
                cd_funzione = aLinea_attivita.cd_funzione;
      Elsif Not CNRCTB020.ISCDRENTE(aCDR) Then -- spesa ente NON SAC
         Null;
      End If;
*/
   Elsif aEV.ti_appartenenza = 'D' Then -- SPESA CDS

--------------------- SPESE PER IL CDS

----DBMS_OUTPUT.put_line ('1. CDS IN '||CDS_IN||' SAC '||CDS_SAC.cd_unita_organizzativa);


      If CDS_IN = CDS_SAC.cd_unita_organizzativa Then -- CDS SAC
        Begin
         Select *
         Into   aVocef
         From   voce_f
         Where  esercizio = aEv.esercizio And
                ti_appartenenza = aEv.ti_appartenenza And
                ti_gestione = aEv.ti_gestione And
                cd_titolo_capitolo = aEv.cd_elemento_voce And
                cd_CENTRO_RESPONSABILITA = aLinea_attivita.cd_centro_responsabilita And
                cd_funzione = aLinea_attivita.cd_funzione;
        Exception
          When No_Data_Found Then
            IBMERR001.RAISE_ERR_GENERICO('1. Voce_f non trovata per Esercizio '||aEv.esercizio||
', App. '||aEv.ti_appartenenza||', gest. '||aEv.ti_gestione||', El. Voce '||aEv.cd_elemento_voce||
', Cdr '||aLinea_attivita.cd_centro_responsabilita||', Funzione '||aLinea_attivita.cd_funzione);
        End;

      Elsif CDS_IN != CDS_SAC.cd_unita_organizzativa Then -- ALTRI CDS
        Begin
         Select *
         Into   aVocef
         From   voce_f
         Where  esercizio = aEv.esercizio And
                ti_appartenenza = aEv.ti_appartenenza And
                ti_gestione = aEv.ti_gestione And
                cd_titolo_capitolo = aEv.cd_elemento_voce And
                cd_unita_organizzativa = UO_IN.CD_UNITA_ORGANIZZATIVA And
                cd_funzione = aLinea_attivita.cd_funzione;
        Exception
          When No_Data_Found Then
            IBMERR001.RAISE_ERR_GENERICO('2. Voce_f non trovata per Esercizio '||aEv.esercizio||
', App. '||aEv.ti_appartenenza||', gest. '||aEv.ti_gestione||', El. Voce '||aEv.cd_elemento_voce||
', UO '||UO_IN.CD_UNITA_ORGANIZZATIVA||', Funzione '||aLinea_attivita.cd_funzione);
        End;
      End If;
   End If;

Elsif aEV.ti_gestione = 'E' Then -- ENTRATA

   If aEV.ti_appartenenza = 'C' Then -- ENTRATA ENTE/di fatto cds

--------------------- ENTRATE PER IL CDS
        Begin
         Select *
         Into   aVocef
         From   voce_f
         Where  esercizio = aEv.esercizio And
                ti_appartenenza = aEv.ti_appartenenza And
                ti_gestione = aEv.ti_gestione And
                cd_titolo_capitolo = aEv.cd_elemento_voce And
                ti_voce = 'A' and
                (cd_unita_organizzativa = UO_IN.CD_UNITA_ORGANIZZATIVA or
                cd_unita_organizzativa is null);
        Exception
          When No_Data_Found Then
            IBMERR001.RAISE_ERR_GENERICO('3. Voce_f non trovata per Esercizio '||aEv.esercizio||
', App. '||aEv.ti_appartenenza||', gest. '||aEv.ti_gestione||', El. Voce '||aEv.cd_elemento_voce||
', UO '||UO_IN.CD_UNITA_ORGANIZZATIVA);
        End;
   Elsif aEV.ti_appartenenza = 'D' Then -- ENTRATA CDS

--------------------- ENTRATE PER IL BILANCIO DEL CNR

        aVocef := Null;

   End If;

End If; -- ENTRATA/SPESA

Return AVOCEF;

End;

Function getVoce_FdaEV (aEsercizio NUMBER, aTI_APPARTENENZA CHAR, aTI_GESTIONE CHAR, aCD_ELEMENTO_VOCE VARCHAR2,
                        acd_centro_responsabilita VARCHAR2, acd_linea_attivita VARCHAR2) Return  VARCHAR2 Is
aRecEv elemento_voce%Rowtype;
aRecLA linea_attivita%Rowtype;
aVoceLunga voce_f%Rowtype;

  recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEsercizio);

If nvl(recParametriCnr.fl_nuovo_pdg,'N')='Y' Then
  return aCD_ELEMENTO_VOCE;
End If;

Select * Into aRecEv
From elemento_voce
Where ESERCIZIO = aEsercizio And
TI_APPARTENENZA = aTI_APPARTENENZA And
TI_GESTIONE = aTI_GESTIONE And
CD_ELEMENTO_VOCE = aCD_ELEMENTO_VOCE;

Select * Into aRecLA
From linea_attivita
Where cd_centro_responsabilita = acd_centro_responsabilita And
      cd_linea_attivita = acd_linea_attivita;

aVoceLunga := getVoce_FdaEV (aRecEV, aRecLA);

Return aVoceLunga.cd_voce;

End;

---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
-----------------------                   NUOVA GESTIONE                  -------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------

 procedure ribaltaSuAreaPDG_da_gest(
  aEsercizio number,
  aCdCentroResponsabilita varchar2,
  aUser varchar2
 ) Is
  aCDRPrimo cdr%rowtype;
  aCDR cdr%rowtype;
  aCDRArea cdr%rowtype;
  aUOCDS unita_organizzativa%rowtype;
  aTSNow date;
  aEVCollegato elemento_voce%rowtype;
  aStatoRibaltamento char(1);
  aFlagRibaltato boolean;
  recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEsercizio);

  If recParametriCNR.esercizio is null Then
     IBMERR001.RAISE_ERR_GENERICO('Parametri CNR per l''esercizio '||aEsercizio||' inesistenti!');
  End If;

  aCDRPrimo:=CNRCTB020.GETCDRVALIDO(aEsercizio,aCdCentroResponsabilita);

  -- Verifica che il CDR non sia un CDR di AREA se si tratta ancora della vecchia gestione pdg
  If nvl(recParametriCnr.fl_nuovo_pdg,'N')='N' Then
    aUOCDS:=CNRCTB020.GETUOCDS(aCDRPrimo);

    if aUOCDS.cd_tipo_unita = CNRCTB020.TIPO_AREA then
     IBMERR001.RAISE_ERR_GENERICO('Ribaltamento non permesso per CDR appartenente ad area!');
    end if;
  End If;

--  if aCDRPrimo.livello <> 1 then
   --IBMERR001.RAISE_ERR_GENERICO('CDR non di primo livello!');
--  end if;

  if CNRCTB020.ISCDRENTE(aCDRPrimo) then
   IBMERR001.RAISE_ERR_GENERICO('Il CDR '||aCdCentroResponsabilita||' è un CDR ENTE!');
  end if;

  aTSNow:=sysdate;
-- VERIFICARE, PRIMA CONTROLLAVA CHE FOSSE CHIUSO, SOLO CHE LO STATO LO CAMBIA ALLA FINE.
-- FORSE è MEGLIO CHE LO CAMBI ALL'INIZIO.
  if not (CNRCTB050.getStato_PDG_ESERCIZIO(aEsercizio, aCDRPrimo.cd_centro_responsabilita)=CNRCTB050.STATO_PDG2_APERTO_GEST) then
   IBMERR001.raise_err_generico('1. Il piano di gestione del CdR '||aCDRPrimo.cd_centro_responsabilita||' non è in stato definitivo !');
  end if;

  -- Da qui in poi comincia la vera operazione di ribaltamento del CDR

   CNRCTB050.lockPdg_esercizio(aEsercizio, aCdCentroResponsabilita);

    -- Ciclo sui dettagli del PDGG ENTRATA da ribaltare su AREA (non c'entra più nulla la Natura 5)

    for aDett in CNRCTB050.PDGG_ETR_DA_RIB_SU_AREA(aEsercizio, aCDRPrimo.cd_centro_responsabilita) loop

        -- RECUPERO L'UNICO CDR DEL CDS AREA
       Begin
        Select cdr.*
        Into   aCDRArea
        From   cdr, unita_organizzativa uo
        Where  aDett.Cd_cds_Area = uo.cd_unita_padre And
               cdr.cd_unita_organizzativa = uo.cd_unita_organizzativa;
       Exception
        When No_Data_Found Then
             IBMERR001.RAISE_ERR_GENERICO('Il CDS Area '||aDett.Cd_cds_Area||' non possiede alcun CdR !');
        When Too_Many_Rows Then
             IBMERR001.RAISE_ERR_GENERICO('1. Il CDS Area '||aDett.Cd_cds_Area||' possiede più di un CdR !');
       End;

        -- lock anche del PDG esercizio dell'Area NON SERVE, L'AREA NON HA TESTATA
        --  CNRCTB050.lockPdg_esercizio(aEsercizio, aCDRArea.cd_centro_responsabilita);

  ribaltaSuAreaPDGG_etr_Int(aEsercizio, aCdCentroResponsabilita, aCDR, aCDRArea, aDett, Null, recParametriCNR, aTSNow, aUser);
    end loop;

    -- Ciclo sui dettagli del PDGG SPESA da ribaltare su AREA (non c'entra più nulla la Natura 5)

    for aDett in CNRCTB050.PDGG_SPE_DA_RIB_SU_AREA(aEsercizio, aCdCentroResponsabilita) loop

        -- RECUPERO L'UNICO CDR DEL CDS AREA
       Begin
        Select cdr.*
        Into   aCDRArea
        From   cdr, unita_organizzativa uo
        Where  cdr.cd_unita_organizzativa = uo.cd_unita_organizzativa And
               uo.cd_unita_padre = aDett.Cd_cds_Area;
       Exception
        When No_Data_Found Then
             IBMERR001.RAISE_ERR_GENERICO('Il CDS Area '||aDett.Cd_cds_Area||' non possiede alcun CdR !');
        When Too_Many_Rows Then
             IBMERR001.RAISE_ERR_GENERICO('2. Il CDS Area '||aDett.Cd_cds_Area||' possiede più di un CdR !');
       End;

        -- lock anche del PDG esercizio dell'Area, NON SERVE, L'AREA NON HA TESTATA
        --CNRCTB050.lockPdg_ESERCIZIO(aEsercizio, aCDRArea.cd_centro_responsabilita);

  ribaltaSuAreaPDGG_spe_Int(aEsercizio, aCdCentroResponsabilita, aCDR, aCDRArea, aDett, Null, recParametriCNR, aTSNow, aUser);
    end loop;

    -- Ciclo sui dettagli del PDGG SPESA da ribaltare su Accentratore

-- NUOVA GESTIONE !!!! Cursore di lettura dei dettagli del PDG Gestionale SPESA da ribaltare sul
--                     CdR Accentratore
-- aEs -> esercizio contabile
-- aCdCdr -> cd centro responsabilità

    for aDett in (select PDG.*
                From   PDG_MODULO_SPESE_GEST PDG, CLASSIFICAZIONE_VOCI CV
    where PDG.esercizio = aEsercizio
      And PDG.CD_CENTRO_RESPONSABILITA = aCdCentroResponsabilita
      And PDG.ID_CLASSIFICAZIONE = CV.ID_CLASSIFICAZIONE
      And pdg.CD_CDR_ASSEGNATARIO != Nvl(CV.CDR_ACCENTRATORE, pdg.CD_CDR_ASSEGNATARIO)
                  And origine = 'PRE'
                  And CATEGORIA_DETTAGLIO In ('DIR', 'STI')
                  And IM_SPESE_GEST_ACCENTRATA_INT + IM_SPESE_GEST_ACCENTRATA_EST > 0
                  for update nowait) Loop

        -- lock anche del PDG esercizio dell'Area NON SERVE !!!
        --CNRCTB050.lockPdg(aEsercizio, aDett.Cd_cds_Area);

  ribaltaSuAccentratPDGG_spe_Int(aEsercizio, aCdCentroResponsabilita, aCDR, aCDRArea, aDett, Null, recParametriCNR, aTSNow, aUser);
    end loop;

 End;

-- 27.04.2006 Ribaltamento delle variazioni al PdG competenza 2006

 Procedure ribaltaSuAreaPDG_da_gest_var(
  aEsercizio number,
  aPG_VARIAZIONE_PDG NUMBER,
  aUser varchar2
 ) Is

---- copiato dal pdgg

  aCDRPrimo cdr%rowtype;
  aCDR cdr%rowtype;
  aCDRArea cdr%rowtype;
  aUOCDS unita_organizzativa%rowtype;
  aTSNow date;
  aEVCollegato elemento_voce%rowtype;
  aStatoRibaltamento char(1);
  aFlagRibaltato boolean;

  recParametriCNR PARAMETRI_CNR%Rowtype;
Begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEsercizio);

  If recParametriCNR.esercizio is null Then
     IBMERR001.RAISE_ERR_GENERICO('Parametri CNR per l''esercizio '||aEsercizio||' inesistenti!');
  End If;

-- MI GIRO LE RIGHE DI ENTRATA DELLA VARIAZIONE CHE DEVONO ESSERE SCARICATE SU AREA

For RIGHE_VAR_ENT In (Select *
                        From  PDG_VARIAZIONE_RIGA_GEST
            Where esercizio = aEsercizio
              And PG_VARIAZIONE_PDG = aPG_VARIAZIONE_PDG
              And TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE
              And CNRUTL001.getCdsFromCdr(CD_CDR_ASSEGNATARIO) != cd_cds_area
                          And CATEGORIA_DETTAGLIO = 'DIR'
                          For update nowait) Loop

  aCDRPrimo := CNRCTB020.getCDRPrimoLivello(CNRCTB020.GETCDRVALIDO(aEsercizio, RIGHE_VAR_ENT.CD_CDR_ASSEGNATARIO));

  -- Verifica che il CDR non sia un CDR di AREA

  aUOCDS := CNRCTB020.GETUOCDS(aCDRPrimo);

  if aUOCDS.cd_tipo_unita = CNRCTB020.TIPO_AREA then
   IBMERR001.RAISE_ERR_GENERICO('Ribaltamento non permesso per CDR appartenente ad area!');
  end if;

  if CNRCTB020.ISCDRENTE(aCDRPrimo) then
   IBMERR001.RAISE_ERR_GENERICO('Il CDR '||RIGHE_VAR_ENT.CD_CDR_ASSEGNATARIO||' è un CDR ENTE!');
  end if;

  aTSNow := sysdate;

  if not (CNRCTB050.getStato_PDG_ESERCIZIO(aEsercizio, aCDRPrimo.cd_centro_responsabilita) = CNRCTB050.STATO_PDG2_CHIUSO_GEST) then
   IBMERR001.raise_err_generico('Il piano di gestione del CdR '||aCDRPrimo.cd_centro_responsabilita||' non è in stato definitivo !');
  end if;

  -- Da qui in poi comincia la vera operazione di ribaltamento del CDR

       -- RECUPERO L'UNICO CDR DEL CDS AREA
       Begin
        Select cdr.*
        Into   aCDRArea
        From   cdr, unita_organizzativa uo
        Where  RIGHE_VAR_ENT.Cd_cds_Area = uo.cd_unita_padre And
               cdr.cd_unita_organizzativa = uo.cd_unita_organizzativa;
       Exception
        When No_Data_Found Then
             IBMERR001.RAISE_ERR_GENERICO('Il CDS Area '||RIGHE_VAR_ENT.Cd_cds_Area||' non possiede alcun CdR !');
        When Too_Many_Rows Then
             IBMERR001.RAISE_ERR_GENERICO('1. Il CDS Area '||RIGHE_VAR_ENT.Cd_cds_Area||' possiede più di un CdR !');
       End;

       ribaltaSuAreaPDGG_etr_Int(aEsercizio, RIGHE_VAR_ENT.CD_CDR_ASSEGNATARIO, aCDR, aCDRArea, Null, RIGHE_VAR_ENT, recParametriCNR, aTSNow, aUser);

End Loop; -- DELL'ENTRATA


-- MI GIRO LE RIGHE DI SPESA DELLA VARIAZIONE CHE DEVONO ESSERE SCARICATE SU AREA

For RIGHE_VAR_SPE In (Select *
                      From  PDG_VARIAZIONE_RIGA_GEST
                Where esercizio = aEsercizio
                  And PG_VARIAZIONE_PDG = aPG_VARIAZIONE_PDG
                  And TI_GESTIONE = CNRCTB001.GESTIONE_SPESE
                  And CNRUTL001.getCdsFromCdr(CD_CDR_ASSEGNATARIO) != cd_cds_area
                        And CATEGORIA_DETTAGLIO = 'DIR'
                      For update nowait) Loop

  aCDRPrimo := CNRCTB020.getCDRPrimoLivello(CNRCTB020.GETCDRVALIDO(aEsercizio, RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO));

  -- Verifica che il CDR non sia un CDR di AREA

  aUOCDS := CNRCTB020.GETUOCDS(aCDRPrimo);

  if aUOCDS.cd_tipo_unita = CNRCTB020.TIPO_AREA then
   IBMERR001.RAISE_ERR_GENERICO('Ribaltamento non permesso per CDR appartenente ad area!');
  end if;

  if CNRCTB020.ISCDRENTE(aCDRPrimo) then
   IBMERR001.RAISE_ERR_GENERICO('Il CDR '||RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO||' è un CDR ENTE!');
  end if;

  aTSNow := sysdate;

  if not (CNRCTB050.getStato_PDG_ESERCIZIO(aEsercizio, aCDRPrimo.cd_centro_responsabilita) = CNRCTB050.STATO_PDG2_CHIUSO_GEST) then
   IBMERR001.raise_err_generico('Il piano di gestione del CdR '||aCDRPrimo.cd_centro_responsabilita||' non è in stato definitivo !');
  end if;

  -- Da qui in poi comincia la vera operazione di ribaltamento del CDR

        -- RECUPERO L'UNICO CDR DEL CDS AREA
       Begin
        Select cdr.*
        Into   aCDRArea
        From   cdr, unita_organizzativa uo
        Where  cdr.cd_unita_organizzativa = uo.cd_unita_organizzativa And
               uo.cd_unita_padre = RIGHE_VAR_SPE.Cd_cds_Area;
       Exception
        When No_Data_Found Then
             IBMERR001.RAISE_ERR_GENERICO('Il CDS Area '||RIGHE_VAR_SPE.Cd_cds_Area||' non possiede alcun CdR !');
        When Too_Many_Rows Then
             IBMERR001.RAISE_ERR_GENERICO('2. Il CDS Area '||RIGHE_VAR_SPE.Cd_cds_Area||' possiede più di un CdR !');
       End;

        -- lock anche del PDG esercizio dell'Area, NON SERVE, L'AREA NON HA TESTATA
        --CNRCTB050.lockPdg_ESERCIZIO(aEsercizio, aCDRArea.cd_centro_responsabilita);

       ribaltaSuAreaPDGG_spe_Int(aEsercizio, RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO, aCDR, aCDRArea, Null, RIGHE_VAR_SPE, recParametriCNR, aTSNow, aUser);

End Loop; -- DELLA SPESA

-- Ciclo sui dettagli della Variazione da ribaltare su Accentratore

-- NUOVA GESTIONE !!!! Cursore di lettura dei dettagli del PDG Gestionale SPESA da ribaltare sul
--                     CdR Accentratore
-- aEs -> esercizio contabile
-- aCdCdr -> cd centro responsabilità

For RIGHE_VAR_ACCENTRATE in (Select PDG.*
                             From   PDG_VARIAZIONE_RIGA_GEST PDG, ELEMENTO_VOCE EV, CLASSIFICAZIONE_VOCI CV_VOCE
                           Where  PDG.ESERCIZIO = aEsercizio
                             And  PG_VARIAZIONE_PDG = aPG_VARIAZIONE_PDG
                             And  PDG.TI_GESTIONE = CNRCTB001.GESTIONE_SPESE
                               And  PDG.ESERCIZIO        = EV.ESERCIZIO
                               And  PDG.TI_APPARTENENZA  = EV.TI_APPARTENENZA
                               And  PDG.TI_GESTIONE      = EV.TI_GESTIONE
                               And  PDG.CD_ELEMENTO_VOCE = EV.CD_ELEMENTO_VOCE
                   And  EV.ID_CLASSIFICAZIONE = CV_VOCE.ID_CLASSIFICAZIONE
                   And  PDG.CD_CDR_ASSEGNATARIO != Nvl(CV_VOCE.CDR_ACCENTRATORE, PDG.CD_CDR_ASSEGNATARIO)
                               And  CATEGORIA_DETTAGLIO In ('DIR', 'STI')
                               And  IM_SPESE_GEST_ACCENTRATA_INT + IM_SPESE_GEST_ACCENTRATA_EST > 0
                               For Update Nowait) Loop
        aTSNow := sysdate;
  ribaltaSuAccentratPDGG_spe_Int(aEsercizio, RIGHE_VAR_ACCENTRATE.CD_CDR_ASSEGNATARIO, aCDR, aCDRArea, Null, RIGHE_VAR_ACCENTRATE, recParametriCNR, aTSNow, aUser);
End Loop;

End;


---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
--------------------------------- FINE COPIA DELLA VECCHIA GESTIONE -------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------

 Procedure creasaldicdrlineavocedagest(
  aEsercizio number,
  aCdCentroResponsabilita varchar2,
  aUser VARCHAR2) Is

  aCDRPrimo cdr%rowtype;
  aCDR cdr%rowtype;
  aCDRArea cdr%rowtype;
  aCDSENTE    unita_organizzativa%rowtype;
  aUOCDS unita_organizzativa%rowtype;
  aTSNow date;
  aEVCollegato elemento_voce%rowtype;
  aStatoRibaltamento char(1);
  aFlagRibaltato boolean;
  aNuovoSaldo     Voce_f_saldi_cdr_linea%Rowtype;
  aElVoce       elemento_voce%Rowtype;
  aLinea        linea_attivita%Rowtype;
  aVoceLunga    voce_f%Rowtype;
  CDR_ACCENTRATORE_CLAS CDR.CD_CENTRO_RESPONSABILITA%TYPE;
  livello_pgd_dec_spe  number(1);
  livello_clas_spe  number(1);
  recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEsercizio);

  -- recupero il CDS ENTE
  select * into aCDSENTE
  from  unita_organizzativa
  Where cd_tipo_unita = CNRCTB020.TIPO_ENTE
    and fl_cds  ='Y';

  aCDSENTE := CNRCTB020.GETCDSVALIDO(aEsercizio,aCDSENTE.cd_unita_organizzativa);

----------- RIFA' TUTTI I CONTROLLI CHE FA PER IL RIBALTAMENTO SU AREA --------------

  aCDRPrimo:=CNRCTB020.GETCDRVALIDO(aEsercizio,aCdCentroResponsabilita);

  -- Verifica che il CDR non sia un CDR di AREA

  aUOCDS:=CNRCTB020.GETUOCDS(aCDRPrimo);
If nvl(recParametriCnr.fl_nuovo_pdg,'N')='N' Then
  if aUOCDS.cd_tipo_unita = CNRCTB020.TIPO_AREA then
   IBMERR001.RAISE_ERR_GENERICO('Ribaltamento non permesso per CDR appartenente ad area!');
  end if;
end if;
--  if aCDRPrimo.livello <> 1 then
--   IBMERR001.RAISE_ERR_GENERICO('CDR non di primo livello!');
--  end if;

  if CNRCTB020.ISCDRENTE(aCDRPrimo) then
   IBMERR001.RAISE_ERR_GENERICO('Il CDR '||aCdCentroResponsabilita||' è un CDR ENTE!');
  end if;

  aTSNow:=sysdate;

  if not (CNRCTB050.getStato_PDG_ESERCIZIO(aEsercizio, aCDRPrimo.cd_centro_responsabilita)=CNRCTB050.STATO_PDG2_APERTO_GEST) then
   IBMERR001.raise_err_generico('2. Il piano di gestione del CdR '||aCDRPrimo.cd_centro_responsabilita||' non è in stato definitivo !');
  end if;

  -- Da qui in poi comincia la vera operazione di CREAZIONE DEI SALDI
    CNRCTB050.lockPdg_esercizio(aEsercizio, aCdCentroResponsabilita); -- ???????????????????

    -- Ciclo sui dettagli del PDGG ENTRATA finali per i quali creare i SALDI

    for aDett in (Select * From PDG_MODULO_ENTRATE_GEST
      where esercizio = aEsercizio
        And CD_CENTRO_RESPONSABILITA = aCDRPrimo.cd_centro_responsabilita
                    And origine = 'PRE'
                    And CATEGORIA_DETTAGLIO != 'SCR'
                    For update nowait) Loop

Begin
  Select *
  Into   aElVoce
  From   elemento_voce
  Where  ESERCIZIO        = aDett.ESERCIZIO       and
         TI_APPARTENENZA  = aDett.TI_APPARTENENZA and
         TI_GESTIONE      = aDett.TI_GESTIONE     and
         CD_ELEMENTO_VOCE = aDett.CD_ELEMENTO_VOCE;
Exception
  When No_Data_Found Then
   IBMERR001.raise_err_generico('L''Elemento Voce indicato sul PdG gestionale '||aDett.ESERCIZIO||'/'||aDett.TI_APPARTENENZA||'/'||aDett.TI_GESTIONE||'/'||aDett.CD_ELEMENTO_VOCE||' non esiste !');
End;

Begin
  Select *
  Into   aLinea
  From   linea_attivita
  Where  cd_centro_responsabilita = aDett.cd_cdr_assegnatario And
         cd_linea_attivita        = aDett.cd_linea_attivita;
Exception
  When No_Data_Found Then
   IBMERR001.raise_err_generico('La Linea indicata sul PdG gestionale '||aDett.cd_cdr_assegnatario||'/'||aDett.cd_linea_attivita||' non esiste !');
End;

                    aNuovoSaldo.ESERCIZIO                 := aesercizio;
                    aNuovoSaldo.ESERCIZIO_RES             := aesercizio;
                    aNuovoSaldo.CD_CENTRO_RESPONSABILITA  := aDett.CD_CDR_ASSEGNATARIO;
                    aNuovoSaldo.CD_LINEA_ATTIVITA         := aDett.cd_linea_attivita;
                    aNuovoSaldo.TI_APPARTENENZA           := aDett.TI_APPARTENENZA;
                    aNuovoSaldo.TI_GESTIONE               := aDett.TI_GESTIONE;

                    If nvl(recParametriCnr.fl_nuovo_pdg,'N')='N' Then
                      aVoceLunga := getVoce_FdaEV(aElVoce, aLinea);
                      aNuovoSaldo.CD_VOCE := aVoceLunga.cd_voce;
                      aNuovoSaldo.cd_elemento_voce := aVoceLunga.cd_elemento_voce;
                    Else
                      aNuovoSaldo.CD_VOCE := aDett.CD_ELEMENTO_VOCE;
                      aNuovoSaldo.cd_elemento_voce := aDett.CD_ELEMENTO_VOCE;
                    End If;

                    CNRCTB054.RESET_IMPORTI_SALDI (aNuovoSaldo);

                    aNuovoSaldo.CD_ELEMENTO_VOCE          := aDett.CD_ELEMENTO_VOCE;
                    aNuovoSaldo.IM_STANZ_INIZIALE_A1      := aDett.IM_ENTRATA;
                    aNuovoSaldo.IM_STANZ_INIZIALE_A2      := 0;
                    aNuovoSaldo.IM_STANZ_INIZIALE_A3      := 0;
                    aNuovoSaldo.VARIAZIONI_PIU            := 0;
                    aNuovoSaldo.VARIAZIONI_MENO           := 0;
                    aNuovoSaldo.IM_STANZ_INIZIALE_CASSA   := aDett.IM_INCASSI;
                    aNuovoSaldo.VARIAZIONI_PIU_CASSA      := 0;
                    aNuovoSaldo.VARIAZIONI_MENO_CASSA     := 0;
                    aNuovoSaldo.IM_OBBL_ACC_COMP          := 0;
                    aNuovoSaldo.IM_STANZ_RES_IMPROPRIO    := 0;
                    aNuovoSaldo.VAR_PIU_STANZ_RES_IMP     := 0;
                    aNuovoSaldo.VAR_MENO_STANZ_RES_IMP    := 0;
                    aNuovoSaldo.IM_OBBL_RES_IMP           := 0;
                    aNuovoSaldo.VAR_PIU_OBBL_RES_IMP      := 0;
                    aNuovoSaldo.VAR_MENO_OBBL_RES_IMP     := 0;
                    aNuovoSaldo.IM_OBBL_RES_PRO           := 0;
                    aNuovoSaldo.VAR_PIU_OBBL_RES_PRO      := 0;
                    aNuovoSaldo.VAR_MENO_OBBL_RES_PRO     := 0;
                    aNuovoSaldo.IM_MANDATI_REVERSALI_PRO  := 0;
                    aNuovoSaldo.IM_MANDATI_REVERSALI_IMP  := 0;
                    aNuovoSaldo.IM_PAGAMENTI_INCASSI      := 0;
                    aNuovoSaldo.DACR                      := aTSNow;
                    aNuovoSaldo.UTCR                      := aUser;
                    aNuovoSaldo.DUVA                      := aTSNow;
                    aNuovoSaldo.UTUV                      := aUser;
                    aNuovoSaldo.PG_VER_REC                := 1;

                    CNRCTB054.crea_aggiorna_saldi (aNuovoSaldo, '053.creasaldicdrlineavocedagest 1', 'N');

                    If nvl(recParametriCnr.fl_nuovo_pdg,'N')!='Y' Then
                      --Aggiorno la tabella VOCE_F_SALDI_CMP
                      Declare
                        aSaldoCmp                 Voce_f_saldi_cmp%Rowtype;
                      Begin
                        Select * into aSaldoCmp from voce_f_saldi_cmp
                        Where CD_CDS = aCDSENTE.CD_UNITA_ORGANIZZATIVA
                        And   ESERCIZIO = aNuovoSaldo.ESERCIZIO
                        And   TI_APPARTENENZA=aNuovoSaldo.TI_APPARTENENZA
                        And   TI_GESTIONE=aNuovoSaldo.TI_GESTIONE
                        And   CD_VOCE=aNuovoSaldo.CD_VOCE
                        And   TI_COMPETENZA_RESIDUO=CNRCTB054.TI_COMPETENZA
                        For update nowait;

                        CNRCTB054.aggiornaStanziamentoResidui(aSaldoCmp,aNuovoSaldo.IM_STANZ_INIZIALE_A1,aUser,aTSNow);
                      Exception when NO_DATA_FOUND then
                        IBMERR001.RAISE_ERR_GENERICO('Capitolo finanziario non trovato:'||aNuovoSaldo.CD_VOCE);
                      End;
                    End If;
    end loop;

  Begin
    Select  Nvl(LIVELLO_PDG_DECIS_SPE, 0)
    Into    livello_pgd_dec_spe
    From    PARAMETRI_CNR
    Where   ESERCIZIO = aEsercizio;

    Select  max(nr_livello)
    Into    livello_clas_spe
    From    v_classificazione_voci
    Where   ESERCIZIO =aEsercizio
    AND FL_MASTRINO = 'Y'
    AND TI_GESTIONE = 'S';
  Exception
  When No_Data_Found Then
   IBMERR001.raise_err_generico('Parametri necessari al controllo dei limiti per l''esercizio '||aEsercizio||' non trovati !');
  End;

    -- Ciclo sui dettagli del PDGG SPESE finali per i quali creare i SALDI

    for aDett in (Select PDG.*
                  From PDG_MODULO_SPESE_GEST PDG--, CLASSIFICAZIONE_VOCI CV
      where PDG.esercizio = aEsercizio
        And PDG.CD_CENTRO_RESPONSABILITA = aCDRPrimo.cd_centro_responsabilita
--        And PDG.ID_CLASSIFICAZIONE = CV.ID_CLASSIFICAZIONE
                    And PDG.origine = 'PRE'
                    And PDG.CATEGORIA_DETTAGLIO != 'SCR'
--                    And Nvl(CV.CDR_ACCENTRATORE, CD_CDR_ASSEGNATARIO) = CD_CDR_ASSEGNATARIO
                    For update nowait) Loop

-- RECUPERO IL CDR ACCENTRATORE DELLA CLASSIFICAZIONE

Select CDR_ACCENTRATORE
Into   CDR_ACCENTRATORE_CLAS
From   CLASSIFICAZIONE_VOCI
Where  ID_CLASSIFICAZIONE = aDett.ID_CLASSIFICAZIONE;


Begin
  Select *
  Into   aElVoce
  From   elemento_voce
  Where  ESERCIZIO        = aDett.ESERCIZIO       and
         TI_APPARTENENZA  = aDett.TI_APPARTENENZA and
         TI_GESTIONE      = aDett.TI_GESTIONE     and
         CD_ELEMENTO_VOCE = aDett.CD_ELEMENTO_VOCE;
Exception
  When No_Data_Found Then
   IBMERR001.raise_err_generico('L''Elemento Voce indicato sul PdG gestionale '||aDett.ESERCIZIO||'/'||aDett.TI_APPARTENENZA||'/'||aDett.TI_GESTIONE||'/'||aDett.CD_ELEMENTO_VOCE||' non esiste !');
End;

Begin
  Select *
  Into   aLinea
  From   linea_attivita
  Where  cd_centro_responsabilita = aDett.cd_cdr_assegnatario And
         cd_linea_attivita        = aDett.cd_linea_attivita;
Exception
  When No_Data_Found Then
   IBMERR001.raise_err_generico('La Linea indicata sul PdG gestionale '||aDett.cd_cdr_assegnatario||'/'||aDett.cd_linea_attivita||' non esiste !');
End;

                    aNuovoSaldo.ESERCIZIO                 := aesercizio;
                    aNuovoSaldo.ESERCIZIO_RES             := aesercizio;
                    aNuovoSaldo.CD_CENTRO_RESPONSABILITA  := aDett.CD_CDR_ASSEGNATARIO;
                    aNuovoSaldo.CD_LINEA_ATTIVITA         := aDett.cd_linea_attivita;
                    aNuovoSaldo.TI_APPARTENENZA           := aDett.TI_APPARTENENZA;
                    aNuovoSaldo.TI_GESTIONE               := aDett.TI_GESTIONE;

                    If nvl(recParametriCnr.fl_nuovo_pdg,'N')='N' Then
                      aVoceLunga := getVoce_FdaEV(aElVoce, aLinea);
                      aNuovoSaldo.CD_VOCE := aVoceLunga.cd_voce;
                      aNuovoSaldo.cd_elemento_voce := aVoceLunga.cd_elemento_voce;
                   Else
                      aNuovoSaldo.CD_VOCE := aDett.CD_ELEMENTO_VOCE;
                      aNuovoSaldo.cd_elemento_voce := aDett.CD_ELEMENTO_VOCE;
                    End If;

                    CNRCTB054.RESET_IMPORTI_SALDI (aNuovoSaldo);

                    aNuovoSaldo.CD_ELEMENTO_VOCE          := aDett.CD_ELEMENTO_VOCE;

/* inizio pezzo aggiunto il 09.02.2006 alle 19.33 per test dopo 037 */

-- se l'assegnatario è anche l'accentratore stanzia tutte le colonne (acc + dec)
-- se l'assegnatario non è anche l'accentratore stanzia solo il decentrato
-- perchè si tratta di voce sia accentrata che decentrata
-- N.B. i dettagli di origine di quelli generati sull'accentratore restano DIR per le classificazioni
-- doppia (sia acc che dec) (((((deciso al momento)))))

If ((CDR_ACCENTRATORE_CLAS Is Null) Or
    (CDR_ACCENTRATORE_CLAS Is Not Null And CDR_ACCENTRATORE_CLAS = aDett.CD_CDR_ASSEGNATARIO)) Then

    aNuovoSaldo.IM_STANZ_INIZIALE_A1 := Nvl(aDett.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                                        Nvl(aDett.IM_SPESE_GEST_DECENTRATA_EST, 0) +
                                        Nvl(aDett.IM_SPESE_GEST_ACCENTRATA_INT, 0) +
                                        Nvl(aDett.IM_SPESE_GEST_ACCENTRATA_EST, 0);
Elsif CDR_ACCENTRATORE_CLAS Is Not Null And CDR_ACCENTRATORE_CLAS != aDett.CD_CDR_ASSEGNATARIO Then
    aNuovoSaldo.IM_STANZ_INIZIALE_A1 := Nvl(aDett.IM_SPESE_GEST_DECENTRATA_INT, 0) +
                                        Nvl(aDett.IM_SPESE_GEST_DECENTRATA_EST, 0);
End If;

/* fine pezzo aggiunto il 09.02.2006 alle 19.33 per test dopo 037 */

                    aNuovoSaldo.IM_STANZ_INIZIALE_A2      := 0;
                    aNuovoSaldo.IM_STANZ_INIZIALE_A3      := 0;
                    aNuovoSaldo.VARIAZIONI_PIU            := 0;
                    aNuovoSaldo.VARIAZIONI_MENO           := 0;
                    aNuovoSaldo.IM_STANZ_INIZIALE_CASSA   := aDett.IM_PAGAMENTI;
                    aNuovoSaldo.VARIAZIONI_PIU_CASSA      := 0;
                    aNuovoSaldo.VARIAZIONI_MENO_CASSA     := 0;
                    aNuovoSaldo.IM_OBBL_ACC_COMP          := 0;
                    aNuovoSaldo.IM_STANZ_RES_IMPROPRIO    := 0;
                    aNuovoSaldo.VAR_PIU_STANZ_RES_IMP     := 0;
                    aNuovoSaldo.VAR_MENO_STANZ_RES_IMP    := 0;
                    aNuovoSaldo.IM_OBBL_RES_IMP           := 0;
                    aNuovoSaldo.VAR_PIU_OBBL_RES_IMP      := 0;
                    aNuovoSaldo.VAR_MENO_OBBL_RES_IMP     := 0;
                    aNuovoSaldo.IM_OBBL_RES_PRO           := 0;
                    aNuovoSaldo.VAR_PIU_OBBL_RES_PRO      := 0;
                    aNuovoSaldo.VAR_MENO_OBBL_RES_PRO     := 0;
                    aNuovoSaldo.IM_MANDATI_REVERSALI_PRO  := 0;
                    aNuovoSaldo.IM_MANDATI_REVERSALI_IMP  := 0;
                    aNuovoSaldo.IM_PAGAMENTI_INCASSI      := 0;
                    aNuovoSaldo.DACR                      := aTSNow;
                    aNuovoSaldo.UTCR                      := aUser;
                    aNuovoSaldo.DUVA                      := aTSNow;
                    aNuovoSaldo.UTUV                      := aUser;
                    aNuovoSaldo.PG_VER_REC                := 1;

                    CNRCTB054.crea_aggiorna_saldi (aNuovoSaldo, '053.creasaldicdrlineavocedagest 2', 'N');


                    If nvl(recParametriCnr.fl_nuovo_pdg,'N')!='Y' Then
                      --Aggiorno la tabella VOCE_F_SALDI_CMP
                      Declare
                        aSaldoCmp                 Voce_f_saldi_cmp%Rowtype;
                        CdsAssegnatario           unita_organizzativa.cd_unita_organizzativa%Type;
                      Begin
                        -- Recupero il CDS del CDR da aggiornare
                        CdsAssegnatario := CNRUTL001.GETCDSFROMCDR(aNuovoSaldo.CD_CENTRO_RESPONSABILITA);

                        Select * into aSaldoCmp from voce_f_saldi_cmp
                        Where CD_CDS = CdsAssegnatario
                        And   ESERCIZIO = aNuovoSaldo.ESERCIZIO
                        And   TI_APPARTENENZA=aNuovoSaldo.TI_APPARTENENZA
                        And   TI_GESTIONE=aNuovoSaldo.TI_GESTIONE
                        And   CD_VOCE=aNuovoSaldo.CD_VOCE
                        And   TI_COMPETENZA_RESIDUO=CNRCTB054.TI_COMPETENZA
                        For update nowait;

                        CNRCTB054.aggiornaStanziamentoResidui(aSaldoCmp,aNuovoSaldo.IM_STANZ_INIZIALE_A1,aUser,aTSNow);
                      Exception when NO_DATA_FOUND then
                        IBMERR001.RAISE_ERR_GENERICO('Capitolo finanziario non trovato:'||aNuovoSaldo.CD_VOCE);
                      End;
                    End If;

                  -- verifico che il livello utilizzato nel decisionale parte spese non sia il capitolo per aggiornare i limiti di spesa
                    if  livello_pgd_dec_spe!= livello_clas_spe then
                      -- Aggiornamento Limite spesa
                          aggiornaLimiteSpesa(aElVoce,aNuovoSaldo,aUser);
                    end if;
    end loop;
End;

 Procedure ribaltaSuAreaPDGVar(
  aEsercizio number,
  aCdCentroResponsabilita varchar2,
  aUser VARCHAR2,
  aEsVar NUMBER Default Null,
  aNumVar NUMBER Default Null
 ) is
  aCDRPrimo cdr%rowtype;
  aCDR cdr%rowtype;
  aCDRArea cdr%rowtype;
  aUOCDS unita_organizzativa%rowtype;
  aTSNow date;
  aEVCollegato elemento_voce%rowtype;
  aPDG pdg_preventivo%rowtype;
   recParametriCNR PARAMETRI_CNR%Rowtype;
 begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aEsercizio);

  If recParametriCNR.esercizio is null Then
     IBMERR001.RAISE_ERR_GENERICO('Parametri CNR per l''esercizio '||aEsercizio||' inesistenti!');
  End If;

  -- Verifica che il CDR non sia un CDR di AREA, in tal caso termina subito

  aCDRPrimo:=CNRCTB020.GETCDRVALIDO(aEsercizio,aCdCentroResponsabilita);

  aUOCDS:=CNRCTB020.GETUOCDS(aCDRPrimo);

  if aUOCDS.cd_tipo_unita = CNRCTB020.TIPO_AREA then
   return;
  end if;

  if aCDRPrimo.livello <> 1 then
   IBMERR001.RAISE_ERR_GENERICO('CDR non di primo livello!');
  end if;

  -- Verifica che il BILANCIO finanziario dell'ente sia stato approvato

  if CNRCTB054.isBilancioCnrApprovato(aEsercizio) = 'N' then
   IBMERR001.RAISE_ERR_GENERICO('Bilancio preventivo CNR non ancora approvato!');
  end if;

  if CNRCTB020.ISCDRENTE(aCDRPrimo) then
   IBMERR001.RAISE_ERR_GENERICO('Il CDR '||aCdCentroResponsabilita||' è un CDR ENTE!');
  end if;

  aTSNow:=sysdate;

  CNRCTB050.lockPdg(aEsercizio, aCDRPrimo.cd_centro_responsabilita);

  if not (CNRCTB050.getStato(aEsercizio, aCDRPrimo.cd_centro_responsabilita)=CNRCTB050.STATO_PDG_FINALE) then
   IBMERR001.raise_err_generico('Il piano di gestione di primo livello non è in stato definitivo!');
  end if;


  -- Da qui in poi comincia la vera operazione di ribaltamento che va ripetuta su tutti i CDR figli
  -- del CDR di primo livello considerato

  for aPDG in CNRCTB055.TUTTI_PDG_LINEA(aEsercizio, aCDRPrimo) loop

   CNRCTB050.lockPdg(aEsercizio, aPDG.cd_centro_responsabilita);

   select * into aCDR from cdr where
    cd_centro_responsabilita = aPDG.cd_centro_responsabilita;

   -- estraggo l'area

   aCDRArea:=CNRCTB020.getCDRArea(aCDR);

   -- se l'area non viene trovata non c'è niente da ribaltare
   if aCDRArea.cd_centro_responsabilita is not null then

    CNRCTB050.lockPdg(aEsercizio, aCDRArea.cd_centro_responsabilita);

    -- Estraggo il conto di ricavo figurativo collegato

  aEVCollegato:=CNRCTB000.GETVOCERICFIGALTROCDR(aEsercizio);

    -- Ciclo sui dettagli del PDG con natura 5
    If (aEsVar Is Not Null And aNumVar Is Not Null) Then
            for aDett in CNRCTB050.DETTAGLI_PDG_NATURA_5_VAR(aEsercizio, aCDR.cd_centro_responsabilita,aEsVar,aNumVar) loop
           ribaltaSuAreaPDGInt(
              aEsercizio,
              aCdCentroResponsabilita,
              aCDR,
              aCDRArea,
              aEVCollegato,
              aDett,
              recParametriCNR,
              aTSNow,
              aUser
             );
            end loop;

    Else
            for aDett in CNRCTB050.DETTAGLI_PDG_NATURA_5(aEsercizio, aCDR.cd_centro_responsabilita) loop
           ribaltaSuAreaPDGInt(
              aEsercizio,
              aCdCentroResponsabilita,
              aCDR,
              aCDRArea,
              aEVCollegato,
              aDett,
              recParametriCNR,
              aTSNow,
              aUser
             );
            end loop;
    End If;
   end if;
  end loop;
 end;

-- 28.04.2006 genera la variazione di bilancio ente da variazione al pdg approvata

Procedure genera_varente_da_Var_pdg (aEsercizio         NUMBER,
                                     aPG_VARIAZIONE     NUMBER,
                                     aUser              VARCHAR2,
                                     cds_var_bil    Out VARCHAR2,
                                     es_var_bil     Out NUMBER,
                                     ti_app_var_bil Out CHAR,
                                     pg_var_bil     Out NUMBER) Is

  aVar                  var_bilancio%rowtype;
  aVarDet               var_bilancio_det%rowtype;
  aTotVar               number(15,2);
  aOldVoceAgg           varchar2(30);
  aVoceF                voce_f%rowtype;
  aPgVar                number(10);
  aTSNow                DATE;
  TESTATA_CREATA        CHAR(1);
  aSaldoCdrLinea        voce_f_saldi_cdr_linea%Rowtype;
  aVAR_PDG              PDG_VARIAZIONE%Rowtype;
  aPdgVariazioneRigaEnte	PDG_VARIAZIONE_RIGA_GEST%Rowtype;
  variazione_da_fare    NUMBER;
  TIPO                  VAR_BILANCIO.TI_VARIAZIONE%Type;
  CAUSALE               VAR_BILANCIO.CD_CAUSALE_VAR_BILANCIO%Type;
  aVAR_TIPO             TIPO_VARIAZIONE%Rowtype;
  recParametriCNR       PARAMETRI_CNR%Rowtype;
  aAssPdgVariazioneCdrEnte    ass_pdg_variazione_cdr%Rowtype;
  ultima_riga          number:=0;
  aTotVarEnte               number(15,2);
Begin
				 Begin
	 		    Select  *
				    Into    aVAR_PDG
				    From    PDG_VARIAZIONE
				    Where   ESERCIZIO      = aEsercizio And
				            PG_VARIAZIONE_PDG  = aPG_Variazione;
				 Exception
			     When No_Data_Found Then
			        IBMERR001.RAISE_ERR_GENERICO('Attenzione !!! Non esiste la Variazione al PdG '||aEsercizio||'/'||aPG_VARIAZIONE);
			   End;

			   Begin
			    Select  *
			    Into    aVAR_TIPO
			    From    TIPO_VARIAZIONE
			    Where   ESERCIZIO      = aEsercizio And
			            CD_TIPO_VARIAZIONE  = aVAR_PDG.Tipologia;
			   Exception
			     When No_Data_Found Then
			        IBMERR001.RAISE_ERR_GENERICO('Attenzione !!! Non esiste la Tipologia '||aVAR_PDG.Tipologia||
			                                     ' indicata sulla Variazione '||aEsercizio||'/'||aPG_VARIAZIONE);
			   End;
			   aTSNow := sysdate;
   	recParametriCNR := CNRUTL001.getRecParametriCnr(aEsercizio);
   --27/11/2015: RafPag: Se è attiva la nuova gestione esco perchè non deve essere più creata
   --la variazione a livello Ente
   If recParametriCNR.fl_nuovo_pdg='Y' Then

   -- Rospuc 12/05/2017 inserimento automatico dettaglio variazione ente per le variazioni di prelievo o restituzione al fondo
	   		if aVAR_TIPO.TI_TIPO_VARIAZIONE In (CNRCTB054.TI_VARPDG_PRELIEVO_FONDI, CNRCTB054.TI_VARPDG_RESTITUZIONE_FONDI) Then
					Select  Sum(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0) + Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0) +
                      Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0) + Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)),max(PG_RIGA)
                         		into aTotVar, ultima_riga
                            From   PDG_VARIAZIONE_RIGA_GEST
				    Where   ESERCIZIO      = aEsercizio And
				            PG_VARIAZIONE_PDG  = aPG_Variazione and
				            categoria_dettaglio!='SCR';


				           if aTotVar > 0 then
									  	aTotVarEnte :=-aTotVar;
									  else
									  	aTotVarEnte :=abs(aTotVar);
									 end if;
									   --Riga CDR ente
								    aAssPdgVariazioneCdrEnte.ESERCIZIO:=aEsercizio;
								    aAssPdgVariazioneCdrEnte.PG_VARIAZIONE_PDG := aPG_Variazione;
								    aAssPdgVariazioneCdrEnte.CD_CENTRO_RESPONSABILITA:= cnrctb020.getcdcdrente;
								    aAssPdgVariazioneCdrEnte.IM_ENTRATA := 0;
								    aAssPdgVariazioneCdrEnte.IM_SPESA := aTotVarEnte;
								    aAssPdgVariazioneCdrEnte.DACR:=aTSNow;
								    aAssPdgVariazioneCdrEnte.UTCR:=aUser;
								    aAssPdgVariazioneCdrEnte.DUVA:=aTSNow;
								    aAssPdgVariazioneCdrEnte.UTUV:=aUser;
								    aAssPdgVariazioneCdrEnte.PG_VER_REC:=1;

				           CNRCTB050.ins_ASS_PDG_VARIAZIONE_CDR(aAssPdgVariazioneCdrEnte);

									 aPdgVariazioneRigaEnte.ESERCIZIO := aEsercizio;
							     aPdgVariazioneRigaEnte.PG_VARIAZIONE_PDG := aPG_Variazione;
							     aPdgVariazioneRigaEnte.PG_RIGA := ultima_riga+1;
							     aPdgVariazioneRigaEnte.CD_CDR_ASSEGNATARIO :=  cnrctb020.getcdcdrente;
							     aPdgVariazioneRigaEnte.CD_LINEA_ATTIVITA := CNRCTB015.getVal02PerChiave(0, 'LINEA_ATTIVITA_SPECIALE',  'LINEA_ATTIVITA_SPESA_ENTE');
							     aPdgVariazioneRigaEnte.CD_CDS_AREA := CNRUTL001.GETCDSFROMCDR(cnrctb020.getcdcdrente);
							     aPdgVariazioneRigaEnte.TI_APPARTENENZA := aVAR_PDG.TI_APPARTENENZA;
							     aPdgVariazioneRigaEnte.TI_GESTIONE := aVAR_PDG.TI_GESTIONE;
							     aPdgVariazioneRigaEnte.CD_ELEMENTO_VOCE := aVAR_PDG.CD_ELEMENTO_VOCE;
							     aPdgVariazioneRigaEnte.DT_REGISTRAZIONE := trunc(aTSNow);
							     aPdgVariazioneRigaEnte.CATEGORIA_DETTAGLIO := 'DIR';
							     aPdgVariazioneRigaEnte.DESCRIZIONE := 'Dettaglio automatico';
							     aPdgVariazioneRigaEnte.IM_SPESE_GEST_DECENTRATA_INT := aTotVarEnte;
							     aPdgVariazioneRigaEnte.IM_SPESE_GEST_DECENTRATA_EST := 0;
							     aPdgVariazioneRigaEnte.IM_SPESE_GEST_ACCENTRATA_INT := 0;
							     aPdgVariazioneRigaEnte.IM_SPESE_GEST_ACCENTRATA_EST := 0;
							     aPdgVariazioneRigaEnte.IM_ENTRATA := 0;
							     aPdgVariazioneRigaEnte.FL_VISTO_DIP_VARIAZIONI := 'N';
							     aPdgVariazioneRigaEnte.DACR:=aTSNow;
							     aPdgVariazioneRigaEnte.UTCR:=aUser;
							     aPdgVariazioneRigaEnte.DUVA:=aTSNow;
							     aPdgVariazioneRigaEnte.UTUV:=aUser;
							     aPdgVariazioneRigaEnte.PG_VER_REC:=1;

						     	CNRCTB051.ins_PDG_VARIAZIONE_RIGA_GEST(aPdgVariazioneRigaEnte);

						      begin
						          Select * Into aSaldoCdrLinea
       								From voce_f_saldi_cdr_linea
								       Where esercizio = aEsercizio
								       And   esercizio_res = aEsercizio
								       And   cd_centro_responsabilita = aPdgVariazioneRigaEnte.CD_CDR_ASSEGNATARIO
								       And   cd_linea_attivita = aPdgVariazioneRigaEnte.CD_LINEA_ATTIVITA
								       And   ti_appartenenza = aPdgVariazioneRigaEnte.TI_APPARTENENZA
								       And   ti_gestione = aPdgVariazioneRigaEnte.TI_GESTIONE
								       And   cd_elemento_voce = aPdgVariazioneRigaEnte.CD_ELEMENTO_VOCE;
								  exception when no_data_found then
								        IBMERR001.RAISE_ERR_GENERICO('Saldo iniziale Fondo Mancante ');
                  End;

									if(aTotVarEnte > 0 )then
									 Update voce_f_saldi_cdr_linea
						       Set VARIAZIONI_PIU = VARIAZIONI_PIU + Abs(aTotVarEnte),
						       			DUVA=aTSNow,
							          UTUV=aUser,
							     			PG_VER_REC=PG_VER_REC+1
						       Where
						       			esercizio = aSaldoCdrLinea.esercizio
						       And   esercizio_res = aSaldoCdrLinea.esercizio_res
						       And   cd_centro_responsabilita = aSaldoCdrLinea.CD_CENTRO_RESPONSABILITA
						       And   cd_linea_attivita = aSaldoCdrLinea.CD_LINEA_ATTIVITA
						       And   ti_appartenenza = aSaldoCdrLinea.TI_APPARTENENZA
						       And   ti_gestione = aSaldoCdrLinea.TI_GESTIONE
						       And   cd_elemento_voce = aSaldoCdrLinea.CD_ELEMENTO_VOCE
						       And   cd_voce = aSaldoCdrLinea.CD_VOCE;
									else
						       Update voce_f_saldi_cdr_linea
						       Set VARIAZIONI_MENO = VARIAZIONI_MENO + Abs(aTotVarEnte),
						       			DUVA=aTSNow,
							          UTUV=aUser,
							     			PG_VER_REC=PG_VER_REC+1
						       where
						      			esercizio = aSaldoCdrLinea.esercizio
						       And   esercizio_res = aSaldoCdrLinea.esercizio_res
						       And   cd_centro_responsabilita = aSaldoCdrLinea.CD_CENTRO_RESPONSABILITA
						       And   cd_linea_attivita = aSaldoCdrLinea.CD_LINEA_ATTIVITA
						       And   ti_appartenenza = aSaldoCdrLinea.TI_APPARTENENZA
						       And   ti_gestione = aSaldoCdrLinea.TI_GESTIONE
						       And   cd_elemento_voce = aSaldoCdrLinea.CD_ELEMENTO_VOCE
						       And   cd_voce = aSaldoCdrLinea.CD_VOCE;
									End if;

									return;
				   else
	     				return;
	     		end if;
     End If;

Begin
  Select 1
  Into   variazione_da_fare
  From   V_VAR_PDG_VOCE_CNR
  Where  ESERCIZIO = aVAR_PDG.ESERCIZIO And
         PG_VARIAZIONE_PDG = aVAR_PDG.PG_VARIAZIONE_PDG And
         CATEGORIA_DETTAGLIO In ('DIR', 'STI')
  Group By CD_VOCE_CNR
  Having Sum(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0) + Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0) + Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0) +
             Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)) != 0;
Exception
  When No_Data_Found Then
        variazione_da_fare := 0; -- la variazione al PDG non necessita di variazione al bilancio
  When Too_Many_Rows Then
        variazione_da_fare := 1;
End;

--------- LOOP SULLA VARIAZIONE AL PDG


If variazione_da_fare = 1 Then
-- DEVO CAPIRE CHE COS'E' DALLA TESTATA DELLA VARIZIONE AL PDG PER DARE IL GIUSTO ATTRIBUTO ALLA TESTATA DELLA VARIAZIONE AL BILANCIO

If aVAR_TIPO.TI_TIPO_VARIAZIONE In (CNRCTB054.TI_VARPDG_STORNO_S_IST_DIVERSI, CNRCTB054.TI_VARPDG_STORNO_S_STESSO_IST) Then
        TIPO := CNRCTB054.TI_VAR_STORNO_S;
        CAUSALE := CNRCTB054.CAU_VAR_STO_S_PDG;
Elsif aVAR_TIPO.TI_TIPO_VARIAZIONE In (CNRCTB054.TI_VARPDG_VAR_POS_STESSO_IST,  CNRCTB054.TI_VARPDG_VAR_NEG_STESSO_IST,
                                       CNRCTB054.TI_VARPDG_VAR_POS_IST_DIVERSI, CNRCTB054.TI_VARPDG_VAR_NEG_IST_DIVERSI) Then
        TIPO := CNRCTB054.TI_VAR_QUAD;
        CAUSALE := CNRCTB054.CAU_ECO_VAR_PDG;
Elsif aVAR_TIPO.TI_TIPO_VARIAZIONE In (CNRCTB054.TI_VARPDG_PRELIEVO_FONDI, CNRCTB054.TI_VARPDG_RESTITUZIONE_FONDI,
                                       CNRCTB054.TI_VARPDG_VAR_POS_FONDI, CNRCTB054.TI_VARPDG_VAR_NEG_FONDI) Then
        TIPO := CNRCTB054.TI_VAR_PREL_FON;
        CAUSALE := CNRCTB054.CAU_PREL_FON;
Else
        IBMERR001.RAISE_ERR_GENERICO('Attenzione !!! Tipologia della Variazione al PdG non coerente con le Variazioni di bilancio ('||aVAR_TIPO.TI_TIPO_VARIAZIONE||')');
End If;


For VAR_PDG_RAGGRUPPATE In (Select CD_VOCE_CNR, Sum(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0) + Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0) +
                                                    Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0) + Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)) TOT_VAR_ENTE
                            From   V_VAR_PDG_VOCE_CNR
                            Where  ESERCIZIO = aVAR_PDG.ESERCIZIO And
                                   PG_VARIAZIONE_PDG = aVAR_PDG.PG_VARIAZIONE_PDG And
                                   CATEGORIA_DETTAGLIO In ('DIR', 'STI')
                            Group By CD_VOCE_CNR
                            Having Sum(Nvl(IM_SPESE_GEST_DECENTRATA_INT, 0) + Nvl(IM_SPESE_GEST_DECENTRATA_EST, 0) +
                                       Nvl(IM_SPESE_GEST_ACCENTRATA_INT, 0) + Nvl(IM_SPESE_GEST_ACCENTRATA_EST, 0)) != 0) Loop


--------------------------- FASE FINALE, CREAZIONE DELLA VARIAZIONE ------------------------

-- Creo una variazione di bilancio con n dettagli corrispondenti ai dati raggruppati della variazione al PDG

  If Nvl(TESTATA_CREATA, 'N') = 'N' Then -- PER CREARE UNA SOLA TESTATA PER OGNI VARIAZIONE
    aPgVar := CNRCTB055.getPgVariazione(aVAR_PDG.ESERCIZIO, CNRCTB020.GETCDCDSENTE (aVAR_PDG.ESERCIZIO));
    -- Creo la TESTATA DELLA variazione
    aVar.CD_CDS           := CNRCTB020.GETCDCDSENTE (aVAR_PDG.ESERCIZIO);
    aVar.ESERCIZIO        := aVAR_PDG.ESERCIZIO;
    aVar.ESERCIZIO_IMPORTI:= aVAR_PDG.ESERCIZIO;
    aVar.TI_APPARTENENZA  := CNRCTB001.APPARTENENZA_CNR;
    aVar.PG_VARIAZIONE    := aPgVar;
    aVar.DS_VARIAZIONE    := 'Variazione Automatica per approvazione Variazione al PDG n. '||aVAR_PDG.ESERCIZIO||'/'||aVAR_PDG.PG_VARIAZIONE_PDG;
    aVar.DS_DELIBERA      := null;
    aVar.TI_VARIAZIONE    := TIPO;
    aVar.CD_CAUSALE_VAR_BILANCIO := CAUSALE;
    aVar.STATO            := CNRCTB054.STATO_VARIAZIONE_PROVVISORIA;
    aVar.DUVA             := aTSNow;
    aVar.UTUV             := aUser;
    aVar.DACR             := aTSNow;
    aVar.UTCR             := aUser;
    aVar.PG_VER_REC       := 1;
    aVar.ESERCIZIO_PDG_VARIAZIONE := aEsercizio;
    aVar.PG_VARIAZIONE_PDG := aPG_VARIAZIONE;
    CNRCTB054.INS_VAR_BILANCIO(aVar);
    TESTATA_CREATA := 'Y';
  End If;

-- inserita la testata occorre inserire le righe

     aVarDet := null;

     aVarDet.CD_CDS             := aVar.cd_cds;
     aVarDet.ESERCIZIO          := aVar.esercizio;
     aVarDet.TI_APPARTENENZA    := aVar.ti_appartenenza;
     aVarDet.PG_VARIAZIONE      := aVar.pg_variazione;
     aVarDet.TI_GESTIONE        := CNRCTB001.GESTIONE_SPESE;
     aVarDet.CD_VOCE            := VAR_PDG_RAGGRUPPATE.CD_VOCE_CNR;
     aVarDet.IM_VARIAZIONE      := VAR_PDG_RAGGRUPPATE.TOT_VAR_ENTE;
     aVarDet.DUVA               := aTSNow;
     aVarDet.UTUV               := aUser;
     aVarDet.DACR               := aTSNow;
     aVarDet.UTCR               := aUser;
     aVarDet.PG_VER_REC         := 1;

--Dbms_Output.put_line (aVar.cd_cds||' '||aVar.esercizio||' '||aVar.ti_appartenenza||' '||aVar.pg_variazione||' '||CNRCTB001.GESTIONE_SPESE||' '||VAR_STANZ_RAGGRUPPATE.CD_VOCE_CNR);

     CNRCTB054.INS_VAR_BILANCIO_DET(aVarDet);
     aTotVar := nvl(aTotVar, 0) + aVarDet.IM_VARIAZIONE;

-- NON OCCORRE AGGIORNARE I SALDI PER CDR/LINEA

End Loop;

  If (aVAR_TIPO.TI_TIPO_VARIAZIONE = CNRCTB054.TI_VARPDG_PRELIEVO_FONDI Or
      aVAR_TIPO.TI_TIPO_VARIAZIONE = CNRCTB054.TI_VARPDG_RESTITUZIONE_FONDI) AND NVL(aTotVar, 0) != 0 Then
     aVarDet := null;

     aVarDet.CD_CDS             := aVar.cd_cds;
     aVarDet.ESERCIZIO          := aVar.esercizio;
     aVarDet.TI_APPARTENENZA    := aVar.ti_appartenenza;
     aVarDet.PG_VARIAZIONE      := aVar.pg_variazione;
     aVarDet.TI_GESTIONE        := CNRCTB001.GESTIONE_SPESE;
     aVarDet.CD_VOCE            := aVAR_PDG.CD_ELEMENTO_VOCE;
     aVarDet.IM_VARIAZIONE      := -aTotVar;
     aVarDet.DUVA               := aTSNow;
     aVarDet.UTUV               := aUser;
     aVarDet.DACR               := aTSNow;
     aVarDet.UTCR               := aUser;
     aVarDet.PG_VER_REC         := 1;

     CNRCTB054.INS_VAR_BILANCIO_DET(aVarDet);
  End If;
Else
  If aVAR_TIPO.TI_TIPO_VARIAZIONE In (CNRCTB054.TI_VARPDG_VAR_POS_FONDI, CNRCTB054.TI_VARPDG_VAR_NEG_FONDI) Then
     Select Nvl(Sum(IM_ENTRATA), 0)
     Into aTotVar
     From Pdg_variazione_riga_gest
     Where esercizio = aVAR_PDG.esercizio
     And   pg_variazione_pdg = aVAR_PDG.pg_variazione_pdg
     And   ti_gestione = CNRCTB001.GESTIONE_ENTRATE;

     If aTotVar != 0 Then
        aPgVar := CNRCTB055.getPgVariazione(aVAR_PDG.ESERCIZIO, CNRCTB020.GETCDCDSENTE (aVAR_PDG.ESERCIZIO));
        -- Creo la TESTATA DELLA variazione
        aVar.CD_CDS           := CNRCTB020.GETCDCDSENTE (aVAR_PDG.ESERCIZIO);
        aVar.ESERCIZIO        := aVAR_PDG.ESERCIZIO;
        aVar.ESERCIZIO_IMPORTI:= aVAR_PDG.ESERCIZIO;
        aVar.TI_APPARTENENZA  := CNRCTB001.APPARTENENZA_CNR;
        aVar.PG_VARIAZIONE    := aPgVar;
        aVar.DS_VARIAZIONE    := 'Variazione Automatica per approvazione Variazione al PDG n. '||aVAR_PDG.ESERCIZIO||'/'||aVAR_PDG.PG_VARIAZIONE_PDG;
        aVar.DS_DELIBERA      := null;
        aVar.TI_VARIAZIONE    := CNRCTB054.TI_VAR_PREL_FON;
        aVar.CD_CAUSALE_VAR_BILANCIO := CNRCTB054.CAU_PREL_FON;
        aVar.STATO            := CNRCTB054.STATO_VARIAZIONE_PROVVISORIA;
        aVar.DUVA             := aTSNow;
        aVar.UTUV             := aUser;
        aVar.DACR             := aTSNow;
        aVar.UTCR             := aUser;
        aVar.PG_VER_REC       := 1;
        aVar.ESERCIZIO_PDG_VARIAZIONE := aEsercizio;
        aVar.PG_VARIAZIONE_PDG := aPG_VARIAZIONE;
        CNRCTB054.INS_VAR_BILANCIO(aVar);

        -- Creo il DETTAGLIO DELLA variazione
        aVarDet := null;

        aVarDet.CD_CDS             := aVar.cd_cds;
        aVarDet.ESERCIZIO          := aVar.esercizio;
        aVarDet.TI_APPARTENENZA    := aVar.ti_appartenenza;
        aVarDet.PG_VARIAZIONE      := aVar.pg_variazione;
        aVarDet.TI_GESTIONE        := CNRCTB001.GESTIONE_SPESE;
        aVarDet.CD_VOCE            := aVAR_PDG.CD_ELEMENTO_VOCE;
        aVarDet.IM_VARIAZIONE      := aTotVar;
        aVarDet.DUVA               := aTSNow;
        aVarDet.UTUV               := aUser;
        aVarDet.DACR               := aTSNow;
        aVarDet.UTCR               := aUser;
        aVarDet.PG_VER_REC         := 1;

        CNRCTB054.INS_VAR_BILANCIO_DET(aVarDet);
     End If;
  End If;
End If;

cds_var_bil    := aVar.cd_cds;
es_var_bil     := aVar.esercizio;
ti_app_var_bil := aVar.ti_appartenenza;
pg_var_bil     := aVar.pg_variazione;

End;

Procedure setdavistaredip (aEsercizio         pdg_variazione.esercizio%Type,
                           aPg_variazione_pdg pdg_variazione.pg_variazione_pdg%Type) Is
  rec_varpdg   pdg_variazione%Rowtype;
Begin
  Select * Into rec_varpdg
  From pdg_variazione
  Where esercizio = aEsercizio
  And   pg_variazione_pdg = aPg_variazione_pdg;

  setdavistaredip(rec_varpdg);
Exception
When Others Then Null;
End;

Procedure setdavistaredip (apdg_variazione pdg_variazione%Rowtype) is

GESTIONE_VISTO          CHAR(1);
SOGLIA                  NUMBER(15,2);
VISTO_PER_SALDO_MODULO  CHAR(1);
VISTO_PER_CLASS         CHAR(1);
CLASS_VISTO             NUMBER;
TOT_VARIAZIONE          NUMBER;
aTipoUnita              UNITA_ORGANIZZATIVA.cd_tipo_unita%Type;

Begin

-- METTO TUTTE LE RIGHE A "VISTATE" APPENA ENTRO, TANTO POI O NON C'E' GESTIONE O IN ASSENZA DI QUALSIASI ALTRO REQUISITO
-- E' CORRETTO CHE SIA COSI'.
-- LE RIGHE VENGONO MESSE A "DA VISTARE" SOLO NEI CASI IN CUI COSI' DEBBA ESSERE.

Update  pdg_variazione_riga_gest
Set     FL_VISTO_DIP_VARIAZIONI = 'Y'
Where   ESERCIZIO          = apdg_variazione.ESERCIZIO And
        PG_VARIAZIONE_PDG  = apdg_variazione.PG_VARIAZIONE_PDG;

Select  Nvl(FL_VISTO_DIP_VARIAZIONI, 'N'), Nvl(IMPORTO_MIN_VISTO_DIP, 0)
Into    GESTIONE_VISTO, SOGLIA
From    PARAMETRI_CNR
Where   ESERCIZIO = apdg_variazione.ESERCIZIO;

If GESTIONE_VISTO = 'Y' Then   -- SE C'E' GESTIONE VISTO IMPOSTO LE SINGOLE RIGHE DA VISTARE OPPURE NO A SECONDA DELLE NECESSITA'

   -- ESTRAGGO IL TOTALE COMPLESSIVO DELLA VARIAZIONE

   Select  Max(ABS_TOT_VARIAZIONE)
   Into    TOT_VARIAZIONE
   From    V_CONS_VAR_PDGG
   Where   ESERCIZIO          = apdg_variazione.ESERCIZIO And
           PG_VARIAZIONE_PDG  = apdg_variazione.PG_VARIAZIONE_PDG;

   -- ESTRAGGO I DUE TIPI DI CONTROLLI:

      -- PER SALDO MODULO DIVERSO DA ZERO;
      -- PER ID CLASSIFICAZIONE

   Select Nvl(FL_VISTO_DIP_SALDO_MODULO, 'N'), Nvl(FL_VISTO_DIP_CLASSIFICAZIONE, 'N')
   Into   VISTO_PER_SALDO_MODULO, VISTO_PER_CLASS
   From   TIPO_VARIAZIONE
   Where  ESERCIZIO = apdg_variazione.ESERCIZIO And
          CD_TIPO_VARIAZIONE = apdg_variazione.TIPOLOGIA;

   If VISTO_PER_SALDO_MODULO = 'N' Then

      -- SE E' PREVISTO IL VISTO PER CLASSIFICAZIONE E L'IMPORTO COMPLESSIVO SFONDA LA SOGLIA

      If VISTO_PER_CLASS = 'Y' And TOT_VARIAZIONE >= SOGLIA Then

           -- VEDO SE LA VARIAZIONE CONTIENE UNA CLASSIFICAZIONE CHE PREVEDE IL VISTO

                Select Count(0)
                Into   CLASS_VISTO
                From   V_CONS_VAR_PDGG V, CLASSIFICAZIONE_VOCI CV
                Where  V.ESERCIZIO          = apdg_variazione.ESERCIZIO And
                       V.PG_VARIAZIONE_PDG  = apdg_variazione.PG_VARIAZIONE_PDG And
                       V.ID_CLASSIFICAZIONE = CV.ID_CLASSIFICAZIONE And
                       CV.FL_VISTO_DIP_VARIAZIONI = 'Y';

                If CLASS_VISTO > 0 Then

                      For DETT_VARIAZIONE In (Select ESERCIZIO, PG_VARIAZIONE_PDG, PG_RIGA, CD_CDR_ASSEGNATARIO
                                              From   V_CONS_VAR_PDGG
                                              Where  ESERCIZIO          = apdg_variazione.ESERCIZIO And
                                                     PG_VARIAZIONE_PDG  = apdg_variazione.PG_VARIAZIONE_PDG) Loop


                        Begin
                    Select cd_tipo_unita
                    Into   aTipoUnita
                    From   unita_organizzativa
                    Where  cd_unita_organizzativa = CNRCTB020.getCDUO (DETT_VARIAZIONE.CD_CDR_ASSEGNATARIO);
                  Exception
                    When No_Data_Found Then
                       IBMERR001.RAISE_ERR_GENERICO('UO: '||CNRCTB020.getCDUO (DETT_VARIAZIONE.CD_CDR_ASSEGNATARIO)||' inesistente.');
                  End;

                  -- Caso CDS SAC
                  If aTipoUnita != CNRCTB020.TIPO_SAC Then
                                   -- E POI METTO A "DA VISTARE" SOLO QUELLE CHE LO SONO (le righe della SAC SI AUTOVISTANO)

                                   Update pdg_variazione_riga_gest
                                   Set    FL_VISTO_DIP_VARIAZIONI = 'N'
                                   Where  ESERCIZIO          = DETT_VARIAZIONE.ESERCIZIO And
                                          PG_VARIAZIONE_PDG  = DETT_VARIAZIONE.PG_VARIAZIONE_PDG And
                                          PG_RIGA            = DETT_VARIAZIONE.PG_RIGA;
                        End If;

                      End Loop;

                End If;  -- SE CONTIENE CLASSIFICAZIONI CHE PREVEDONO IL VISTO

      End If;   -- SE IL TOTALE DELLA VARIAZIONE SUPERA LA SOGLIA MINIMA PREVISTA ED E' PREVISTO IL VISTO PER CLASSIFICAZIONE

  Elsif VISTO_PER_SALDO_MODULO = 'Y' Then

      For MODULI_NON_PAREGGIATI In
          (Select pg_modulo,
                  Sum(IM_SPESE_GEST_DECENTRATA_INT) + Sum(IM_SPESE_GEST_DECENTRATA_EST) +
                  Sum(IM_SPESE_GEST_ACCENTRATA_INT) + Sum(IM_SPESE_GEST_ACCENTRATA_EST) saldo_spe,
                  Sum(IM_ENTRATA) saldo_ent
           From   V_CONS_VAR_PDGG
           Where  ESERCIZIO          = apdg_variazione.ESERCIZIO And
                  PG_VARIAZIONE_PDG  = apdg_variazione.PG_VARIAZIONE_PDG
           Group By pg_modulo
           Having Sum(IM_SPESE_GEST_DECENTRATA_INT) + Sum(IM_SPESE_GEST_DECENTRATA_EST) +
                  Sum(IM_SPESE_GEST_ACCENTRATA_INT) + Sum(IM_SPESE_GEST_ACCENTRATA_EST) != 0 Or
                  Sum(IM_ENTRATA) != 0) Loop

           For DETT_MODULI_NON_PAREGGIATI In (Select ESERCIZIO, PG_VARIAZIONE_PDG, PG_RIGA
                                              From   V_CONS_VAR_PDGG
                                              Where  ESERCIZIO  = apdg_variazione.ESERCIZIO And
                                                     PG_VARIAZIONE_PDG  = apdg_variazione.PG_VARIAZIONE_PDG And
                                                     PG_MODULO = MODULI_NON_PAREGGIATI.PG_MODULO) Loop

                   -- E POI METTO A "DA VISTARE" SOLO QUELLE CHE LO SONO

                   Update pdg_variazione_riga_gest
                   Set    FL_VISTO_DIP_VARIAZIONI = 'N'
                   Where  ESERCIZIO          = DETT_MODULI_NON_PAREGGIATI.ESERCIZIO And
                          PG_VARIAZIONE_PDG  = DETT_MODULI_NON_PAREGGIATI.PG_VARIAZIONE_PDG And
                          PG_RIGA            = DETT_MODULI_NON_PAREGGIATI.PG_RIGA;
           End Loop;

      End Loop;

  End If;

  Declare
    conta NUMBER := 0;
  Begin
    Select Count(0) Into conta
    From pdg_variazione_riga_gest
    Where  ESERCIZIO          = apdg_variazione.ESERCIZIO
    And    PG_VARIAZIONE_PDG  = apdg_variazione.PG_VARIAZIONE_PDG
    And    FL_VISTO_DIP_VARIAZIONI = 'N';

    If conta > 0 Then
      Update pdg_variazione
      Set FL_VISTO_DIP_VARIAZIONI = 'N'
      Where  ESERCIZIO          = apdg_variazione.ESERCIZIO
      And    PG_VARIAZIONE_PDG  = apdg_variazione.PG_VARIAZIONE_PDG;
    Else
      Update pdg_variazione
      Set FL_VISTO_DIP_VARIAZIONI = 'Y'
      Where  ESERCIZIO          = apdg_variazione.ESERCIZIO
      And    PG_VARIAZIONE_PDG  = apdg_variazione.PG_VARIAZIONE_PDG;
    End If;
  End;
Else
  --SE NON C'E' GESTIONE METTO ANCHE SULLA TESTATA IL FLAG A Y
  Update pdg_variazione
  Set FL_VISTO_DIP_VARIAZIONI = 'Y'
  Where  ESERCIZIO          = apdg_variazione.ESERCIZIO
  And    PG_VARIAZIONE_PDG  = apdg_variazione.PG_VARIAZIONE_PDG;
End If; -- SE NON C'E' GESTIONE ESCO DIRETTAMENTE

End;

procedure aggiornaLimiteSpesa(aElVoce elemento_voce%Rowtype,aDett Voce_f_saldi_cdr_linea%Rowtype,aUser VARCHAR2) Is
  fonteLinea    VARCHAR2(3);
  cds           unita_organizzativa.cd_unita_organizzativa%type;
    begin
        if (aElVoce.fl_limite_spesa ='Y') then
                 select tipo into fonteLinea
                 from natura,linea_attivita linea
                 where
                 natura.cd_natura = linea.cd_natura and
                 linea.cd_centro_responsabilita = aDett.cd_centro_responsabilita and
                 linea.cd_linea_attivita        = aDett.cd_linea_attivita;

                 select cd_unita_padre into cds
                 from cdr,unita_organizzativa uo
                 where
                 cdr.cd_centro_responsabilita = aDett.cd_centro_responsabilita and
                 cdr.cd_unita_organizzativa = uo.cd_unita_organizzativa;

                 aggiornaLimiteSpesaImp(aElVoce,aDett.ESERCIZIO,fonteLinea,cds,aDett.IM_STANZ_INIZIALE_A1,aUser);
      end if;
    exception when others then
          IBMERR001.RAISE_ERR_GENERICO('Errore in aggiorna limiti spesa.');
  end;

procedure aggiornaLimiteSpesaVAR(aEsercizio number,apg_variazione number,tipo VARCHAR2,aUser VARCHAR2) Is
  aElemento_voce Elemento_voce%rowtype;
  fonteLinea            natura.tipo%type;
  aCV                   classificazione_voci%Rowtype;
  ACDRLINEA_DETT        LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA%Type;
  ALINEA_DETT           LINEA_ATTIVITA.CD_LINEA_aTTIVITA%Type;
  aCDRPersonale         cdr%rowtype;
  aLASAUOP              linea_attivita%rowtype;
  cds           unita_organizzativa.cd_unita_organizzativa%type;
  RIGHE_VAR_SPE  PDG_VARIAZIONE_RIGA_GEST%rowtype;
  RIGHE_VAR_ACCENTRATE  PDG_VARIAZIONE_RIGA_GEST%rowtype;
  RIGHE_VAR_SPE_RES     VAR_STANZ_RES_RIGA%rowtype;
  im_variazione number;
  begin
  if(tipo = 'C') then
      For RIGHE_VAR_SPE In
     (Select * From  PDG_VARIAZIONE_RIGA_GEST
      Where esercizio = aEsercizio
        And PG_VARIAZIONE_PDG = apg_variazione
        And TI_GESTIONE = CNRCTB001.GESTIONE_SPESE
        And CATEGORIA_DETTAGLIO = 'DIR') Loop
      ACDRLINEA_DETT := null;
      ALINEA_DETT    := null;

      select * into aElemento_voce
      from elemento_voce e
      where
        e.esercizio         = RIGHE_VAR_SPE.esercizio and
        e.ti_appartenenza     = RIGHE_VAR_SPE.ti_appartenenza and
        e.ti_gestione       = RIGHE_VAR_SPE.ti_gestione and
        e.cd_elemento_voce  = RIGHE_VAR_SPE.cd_elemento_voce;
          if(aElemento_voce.fl_limite_spesa ='Y') then
               begin
                          Select PDG.* into RIGHE_VAR_ACCENTRATE
                             From   PDG_VARIAZIONE_RIGA_GEST PDG, ELEMENTO_VOCE EV, CLASSIFICAZIONE_VOCI CV_VOCE
                           Where  PDG.ESERCIZIO = aEsercizio
                             And  PG_VARIAZIONE_PDG = aPG_VARIAZIONE
                             and  pg_riga =RIGHE_VAR_SPE.pg_riga
                             And  PDG.TI_GESTIONE = CNRCTB001.GESTIONE_SPESE
                               And  PDG.ESERCIZIO        = EV.ESERCIZIO
                               And  PDG.TI_APPARTENENZA  = EV.TI_APPARTENENZA
                               And  PDG.TI_GESTIONE      = EV.TI_GESTIONE
                               And  PDG.CD_ELEMENTO_VOCE = EV.CD_ELEMENTO_VOCE
                               And  EV.ID_CLASSIFICAZIONE = CV_VOCE.ID_CLASSIFICAZIONE
                               And  PDG.CD_CDR_ASSEGNATARIO != Nvl(CV_VOCE.CDR_ACCENTRATORE, PDG.CD_CDR_ASSEGNATARIO)
                               And  CATEGORIA_DETTAGLIO In ('DIR', 'STI')
                               And  IM_SPESE_GEST_ACCENTRATA_INT + IM_SPESE_GEST_ACCENTRATA_EST > 0;

                             Select *
                                Into   aCV
                                From   classificazione_voci
                                Where  id_classificazione = aElemento_voce.ID_CLASSIFICAZIONE;

                            -- Leggo il CDR del personale e la sua linea SAUOP
                              aCDRPersonale := CNRCTB020.GETCDRPERSONALE(aEsercizio);
                              aLASAUOP := CNRCTB010.getLASAUOP(aEsercizio, aCDRPersonale.cd_centro_responsabilita);

                              If aCV.CDR_ACCENTRATORE = aCDRPersonale.CD_CENTRO_RESPONSABILITA Then
                                ACDRLINEA_DETT := aLASAUOP.CD_CENTRO_RESPONSABILITA;
                                ALINEA_DETT    := aLASAUOP.CD_LINEA_ATTIVITA;
                              End If;
                  exception
                    when no_data_found then
                     null;
                  end;
                  if(ACDRLINEA_DETT is null) then
                     If (aCV.CDR_ACCENTRATORE is not null) then
                        cds:=CNRUTL001.getCdsFromCdr(aCV.CDR_ACCENTRATORE);
                      else
                        if(CNRUTL001.getCdsFromCdr(RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO) != RIGHE_VAR_SPE.cd_cds_area) then
                          cds:=RIGHE_VAR_SPE.cd_cds_area;
                        else
                          cds:=CNRUTL001.getCdsFromCdr(RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO);
                        end if;
                     end if;
                  else
                       cds:= CNRUTL001.getCdsFromCdr(ACDRLINEA_DETT);
                  end if;

                  select tipo into fonteLinea
                  from natura,linea_attivita linea
                  where
                   natura.cd_natura = linea.cd_natura and
                   linea.cd_centro_responsabilita = nvl(ACDRLINEA_DETT,RIGHE_VAR_SPE.CD_CDR_ASSEGNATARIO) and
                   linea.cd_linea_attivita        = nvl(ALINEA_DETT,RIGHE_VAR_SPE.CD_linea_attivita);

                    select decode(stato,'PRD',1,-1)*(nvl(RIGHE_VAR_SPE.Im_spese_gest_accentrata_int,0)+
                                 nvl(RIGHE_VAR_SPE.Im_spese_gest_accentrata_est,0)+
                                 nvl(RIGHE_VAR_SPE.Im_spese_gest_decentrata_int,0)+
                                 nvl(RIGHE_VAR_SPE.Im_spese_gest_decentrata_est,0)) into im_variazione
                    from pdg_variazione
                    where
                      esercizio = RIGHE_VAR_SPE.esercizio and
                      pg_variazione_pdg = RIGHE_VAR_SPE.pg_variazione_pdg;

                      aggiornaLimiteSpesaImp(aElemento_voce,RIGHE_VAR_SPE.esercizio,fonteLinea,cds,im_variazione,aUser);
          end if; -- soggetta limiti

      End Loop; -- DELLA SPESA
    else  -- Residui
     For RIGHE_VAR_SPE_RES In
     (Select * From  VAR_STANZ_RES_RIGA
      Where esercizio = aEsercizio
        And PG_VARIAZIONE = apg_variazione
        And TI_GESTIONE = CNRCTB001.GESTIONE_SPESE) Loop

      ACDRLINEA_DETT := null;
      ALINEA_DETT    := null;

      select * into aElemento_voce
      from elemento_voce e
      where
        e.esercizio         = RIGHE_VAR_SPE_RES.esercizio_voce and
        e.ti_appartenenza   = RIGHE_VAR_SPE_RES.ti_appartenenza and
        e.ti_gestione       = RIGHE_VAR_SPE_RES.ti_gestione and
        e.cd_elemento_voce  = RIGHE_VAR_SPE_RES.cd_elemento_voce;
          if(aElemento_voce.fl_limite_spesa ='Y') then
                  cds:=CNRUTL001.getCdsFromCdr(RIGHE_VAR_SPE_RES.CD_CDR);

                  select tipo into fonteLinea
                  from natura,linea_attivita linea
                  where
                   natura.cd_natura = linea.cd_natura and
                   linea.cd_centro_responsabilita = RIGHE_VAR_SPE_RES.cd_cdr and
                   linea.cd_linea_attivita        = RIGHE_VAR_SPE_RES.CD_linea_attivita;

                  select decode(stato,'PRD',RIGHE_VAR_SPE_RES.im_variazione,-RIGHE_VAR_SPE_RES.im_variazione) into im_variazione
                  from var_stanz_res
                  where
                      esercizio = RIGHE_VAR_SPE_RES.esercizio and
                      pg_variazione = RIGHE_VAR_SPE_RES.pg_variazione;
                      aggiornaLimiteSpesaImp(aElemento_voce,RIGHE_VAR_SPE_RES.esercizio_res,fontelinea,cds,im_variazione,aUser);
          end if; -- soggetta limiti
       end loop;
     end if; -- residuo
end;
Procedure aggiornaLimiteSpesaImp(aDett elemento_voce%rowtype,aEsercizio number,fonteLinea natura.tipo%type,cds String,im_variazione number,aUser VARCHAR2) is
   aLimite       limite_spesa%Rowtype;
   aLimiteDet    limite_spesa_det%Rowtype;
   fonteAgg      natura.tipo%type:=null;
   begin
                begin
                  select * into aLimite
                  from Limite_spesa
                  where
                    esercizio         = aEsercizio              and
                    ti_appartenenza   = aDett.TI_APPARTENENZA   and
                    TI_GESTIONE       = aDett.TI_GESTIONE       and
                    CD_ELEMENTO_VOCE  = aDett.CD_ELEMENTO_VOCE  and
                    Fonte = fonteLinea;
                    fonteAgg:=fonteLinea;
                  exception when no_data_found then
                   begin
                    select * into aLimite
                    from Limite_spesa
                    where
                      esercizio         = aEsercizio              and
                      ti_appartenenza   = aDett.TI_APPARTENENZA   and
                      TI_GESTIONE       = aDett.TI_GESTIONE       and
                      CD_ELEMENTO_VOCE  = aDett.CD_ELEMENTO_VOCE  and
                      Fonte = '*';
                      fonteAgg:='*';
                    exception when no_data_found then
                      -- Presupponiamo che se non è in testata non è soggetta ai limite(per evitare di inserire dei  limiti fittizi per le fonte esterne)
                      -- IBMERR001.RAISE_ERR_GENERICO('Limite sul controllo della spesa non definito');
                      dbms_output.put_line('aggiorno limite '||aEsercizio||' '||cds||' '||aDett.TI_APPARTENENZA||' '||aDett.TI_GESTIONE||' '||aDett.CD_ELEMENTO_VOCE||' '||fonteLinea);
                      null;
                    end;
                 end;
                 if(fonteAgg is not null) then
                   Begin
                          select * into aLimiteDet
                          from Limite_spesa_det
                            where
                              esercizio         = aEsercizio              and
                              cd_cds            = cds                     and
                              ti_appartenenza   = aDett.TI_APPARTENENZA   and
                              TI_GESTIONE       = aDett.TI_GESTIONE       and
                              CD_ELEMENTO_VOCE  = aDett.CD_ELEMENTO_VOCE  and
                              Fonte = fonteAgg;
                    exception when no_data_found then
                          IBMERR001.RAISE_ERR_GENERICO('Limite sul controllo della spesa non definito per il CdS.');
                    end;
                    if ((aLimiteDet.impegni_assunti+IM_variazione)>aLimiteDet.importo_limite) then
                       IBMERR001.RAISE_ERR_GENERICO('Impossibile procedere al salvataggio, limiti di spesa non rispettati,'||' esercizio '||aEsercizio||
                       ' sulla voce '||aDett.cd_elemento_voce||'. Verificare i limiti sull''apposita consultazione.');
                    else
                      --pipe.send_message('aggiorno limite '||aEsercizio||' '||cds||' '||aDett.TI_APPARTENENZA||' '||aDett.TI_GESTIONE||' '||aDett.CD_ELEMENTO_VOCE||' '||fonteAgg);
                      update limite_spesa_det
                        set impegni_assunti = impegni_assunti+IM_variazione,
                            utuv  = aUser,
                            duva  = sysdate
                      where
                            esercizio         = aEsercizio              and
                            cd_cds            = cds                     and
                            ti_appartenenza   = aDett.TI_APPARTENENZA   and
                            TI_GESTIONE       = aDett.TI_GESTIONE       and
                            CD_ELEMENTO_VOCE  = aDett.CD_ELEMENTO_VOCE  and
                            fonte = fonteAgg;
                    end if;
              end if;
   end;
Procedure aggiornaLimiteSpesaDec(aEsercizio number,cdr VARCHAR2,stato VARCHAR2,aUser VARCHAR2) is
  livello_pgd_dec_spe  number(1);
  livello_clas_spe  number(1);
  aLimite       limite_spesa%Rowtype;
  aLimiteDet    limite_spesa_det%Rowtype;
  fonteAgg      natura.tipo%type:=null;
  segno number:=0;
Begin

  Begin
    Select  Nvl(LIVELLO_PDG_DECIS_SPE, 0)
    Into    livello_pgd_dec_spe
    From    PARAMETRI_CNR
    Where   ESERCIZIO = aEsercizio;

    Select  max(nr_livello)
    Into    livello_clas_spe
    From    v_classificazione_voci
    Where   ESERCIZIO = aEsercizio
    AND FL_MASTRINO = 'Y'
    AND TI_GESTIONE = 'S';
  Exception
  When No_Data_Found Then
   IBMERR001.raise_err_generico('Parametri necessari al controllo dei limiti per l''esercizio '||aEsercizio||' non trovati !');
  End;
  -- Verifico se l'aggiornamento dei limiti deve essere fatto in fase di decisionale
  if (livello_pgd_dec_spe = livello_clas_spe) then
    if(Stato='AC') then
      segno:=1;
    Elsif (stato='PC') then
      segno:=-1;
    end if;
     For RIGHE_SPE In
      (Select id_classificazione,cd_cds_area,
        sum(nvl(im_spese_gest_decentrata_int,0)) im_spese_gest_decentrata_int,
        sum(nvl(im_spese_gest_decentrata_est,0)) im_spese_gest_decentrata_est
        From  Pdg_modulo_spese
        Where esercizio = aEsercizio
          and cd_centro_responsabilita = cdr
          group by id_classificazione,cd_cds_area) Loop
          For RIGHE_VOCI In
           (select * from elemento_voce
           where
             fl_limite_spesa = 'Y' and
             id_classificazione = Righe_spe.id_classificazione) Loop
                if(nvl(RIGHE_SPE.im_spese_gest_decentrata_int,0)!=0) then
                  begin
                    select *  into aLimite from limite_spesa
                    where
                    esercizio     = RIGHE_VOCI.esercizio and
                    ti_gestione   = RIGHE_VOCI.ti_gestione and
                    ti_appartenenza   = RIGHE_VOCI.ti_appartenenza and
                    cd_elemento_voce  = RIGHE_VOCI.cd_elemento_voce and
                    fonte = 'FIN' ;
                    fonteAgg:='FIN';

                  exception when no_data_found then
                    begin
                      select *  into aLimite from limite_spesa
                      where
                      esercizio     = RIGHE_VOCI.esercizio and
                      ti_gestione   = RIGHE_VOCI.ti_gestione and
                      ti_appartenenza   = RIGHE_VOCI.ti_appartenenza and
                      cd_elemento_voce  = RIGHE_VOCI.cd_elemento_voce and
                      fonte = '*';
                      fonteAgg:='*';
                    exception when no_data_found then
                      null;
                    end;
                  end;
                  if(fonteAgg is not null) then
                   Begin
                          select * into aLimiteDet
                          from Limite_spesa_det
                            where
                              esercizio         = aEsercizio              and
                              cd_cds            = RIGHE_SPE.CD_CDS_AREA        and
                              ti_appartenenza   = RIGHE_VOCI.TI_APPARTENENZA   and
                              TI_GESTIONE       = RIGHE_VOCI.TI_GESTIONE       and
                              CD_ELEMENTO_VOCE  = RIGHE_VOCI.CD_ELEMENTO_VOCE  and
                              Fonte = fonteAgg;
                    exception when no_data_found then
                          IBMERR001.RAISE_ERR_GENERICO('Limite sul controllo della spesa non definito per il CdS.');
                    end;
                    if ((aLimiteDet.impegni_assunti+(RIGHE_SPE.im_spese_gest_decentrata_int*segno))>aLimiteDet.importo_limite) then
                       IBMERR001.RAISE_ERR_GENERICO('Impossibile procedere al salvataggio, limiti di spesa non rispettati,'||' esercizio '||aEsercizio||
                       ' sulla voce '||RIGHE_VOCI.cd_elemento_voce||'. Verificare i limiti sull''apposita consultazione.');
                    else
                      update limite_spesa_det
                        set impegni_assunti = impegni_assunti+(RIGHE_SPE.im_spese_gest_decentrata_int*segno),
                            utuv  = aUser,
                            duva  = sysdate
                      where
                            esercizio         = aEsercizio              and
                            cd_cds            = RIGHE_SPE.CD_CDS_AREA        and
                            ti_appartenenza   = RIGHE_VOCI.TI_APPARTENENZA   and
                            TI_GESTIONE       = RIGHE_VOCI.TI_GESTIONE       and
                            CD_ELEMENTO_VOCE  = RIGHE_VOCI.CD_ELEMENTO_VOCE  and
                            fonte = fonteAgg;
                    end if;
                 end if;  -- end if fonteAgg is not null
             end if;      --if(nvl(RIGHE_SPE.im_spese_gest_decentrata_int,0)!=0)
             fonteAgg:=null;
             if(nvl(RIGHE_SPE.im_spese_gest_decentrata_est,0)!=0) then
                  begin
                    select * into aLimite from limite_spesa
                    where
                    esercizio     = RIGHE_VOCI.esercizio and
                    ti_gestione   = RIGHE_VOCI.ti_gestione and
                    ti_appartenenza   = RIGHE_VOCI.ti_appartenenza and
                    cd_elemento_voce  = RIGHE_VOCI.cd_elemento_voce and
                    fonte = 'FES' ;
                    fonteAgg:='FES';
                exception when no_data_found then
                    begin
                      select * into aLimite from limite_spesa
                      where
                      esercizio     = RIGHE_VOCI.esercizio and
                      ti_gestione   = RIGHE_VOCI.ti_gestione and
                      ti_appartenenza   = RIGHE_VOCI.ti_appartenenza and
                      cd_elemento_voce  = RIGHE_VOCI.cd_elemento_voce and
                      fonte = '*';
                      fonteAgg:='*';
                    exception when no_data_found then
                      null;
                    end;
                 end;
                 if(fonteAgg is not null) then
                   Begin
                         select * into aLimiteDet
                         from Limite_spesa_det
                           where
                             esercizio         = aEsercizio              and
                             cd_cds            = RIGHE_SPE.CD_CDS_AREA        and
                             ti_appartenenza   = RIGHE_VOCI.TI_APPARTENENZA   and
                             TI_GESTIONE       = RIGHE_VOCI.TI_GESTIONE       and
                             CD_ELEMENTO_VOCE  = RIGHE_VOCI.CD_ELEMENTO_VOCE  and
                             Fonte = fonteAgg;
                    exception when no_data_found then
                          IBMERR001.RAISE_ERR_GENERICO('Limite sul controllo della spesa non definito per il CdS.');
                    end;
                    if ((aLimiteDet.impegni_assunti+(RIGHE_SPE.im_spese_gest_decentrata_est*segno))>aLimiteDet.importo_limite) then
                       IBMERR001.RAISE_ERR_GENERICO('Impossibile procedere al salvataggio, limiti di spesa non rispettati,'||' esercizio '||aEsercizio||
                       ' sulla voce '||RIGHE_VOCI.cd_elemento_voce||'. Verificare i limiti sull''apposita consultazione.');
                    else
                      update limite_spesa_det
                        set impegni_assunti = impegni_assunti+(RIGHE_SPE.im_spese_gest_decentrata_est*segno),
                            utuv  = aUser,
                            duva  = sysdate
                      where
                            esercizio         = aEsercizio              and
                            cd_cds            = RIGHE_SPE.CD_CDS_AREA        and
                            ti_appartenenza   = RIGHE_VOCI.TI_APPARTENENZA   and
                            TI_GESTIONE       = RIGHE_VOCI.TI_GESTIONE       and
                            CD_ELEMENTO_VOCE  = RIGHE_VOCI.CD_ELEMENTO_VOCE  and
                            fonte = fonteAgg;
                    end if;
                 end if;  -- end if fonteAgg is not null
               end if;  --if(nvl(RIGHE_SPE.im_spese_gest_decentrata_est,0)!=0)
          end loop; -- end loop RIGHE_VOCI
     End loop; -- end loop RIGHE_SPE
 end if;  -- end if livello_pgd_dec_spe = livello_clas_spe
end;
end;
