CREATE OR REPLACE package CNRSTO050 as
--
-- CNRSTO050 - Package per la gestione dello storico PIANO DI GESTIONE CDR
-- Date: 09/10/2001
-- Version: 1.0
--
--
-- Dependency: CNRCTB050
--
-- History:
-- Date: 09/10/2001
-- Version: 1.0
-- Creazione

-- Constants:


-- Functions & Procedures:

 -- Procedura di scarico del PDG sullo storico
 -- La procedura non effettuan controlli
 --
 -- aPdg deve contenere un rowtype completo di pdg preventivo

 procedure scaricaSuStorico(aDesc varchar2, aPdg pdg_preventivo%rowtype);

 function descPassaggioStato(vecchioStato varchar2, nuovoStato varchar2) return varchar2;

 -- Procedure di inserimento nelle tabella di storico del piano di gestione

 procedure sto_PDG_PREVENTIVO (aPgStorico number, aDsStorico varchar2, aDest PDG_PREVENTIVO%rowtype);
 procedure sto_PDG_PREVENTIVO_ETR_DET (aPgStorico number, aDsStorico varchar2, aDest PDG_PREVENTIVO_ETR_DET%rowtype);
 procedure sto_PDG_PREVENTIVO_SPE_DET (aPgStorico number, aDsStorico varchar2, aDest PDG_PREVENTIVO_SPE_DET%rowtype);

end;


CREATE OR REPLACE package body CNRSTO050 is

 procedure scaricaSuStorico(aDesc varchar2, aPdg pdg_preventivo%rowtype) is
 begin
  sto_PDG_PREVENTIVO (aPdg.pg_ver_rec, aDesc, aPdg);
  for aDest in (select * from pdg_preventivo_spe_det where
       esercizio = aPdg.esercizio
   and cd_centro_responsabilita = aPdg.cd_centro_responsabilita) loop
   sto_PDG_PREVENTIVO_SPE_DET (aPdg.pg_ver_rec, aDesc, aDest);
  end loop;
  for aDest in (select * from pdg_preventivo_etr_det where
       esercizio = aPdg.esercizio
   and cd_centro_responsabilita = aPdg.cd_centro_responsabilita) loop
   sto_PDG_PREVENTIVO_ETR_DET (aPdg.pg_ver_rec, aDesc, aDest);
  end loop;
 end;

 function descPassaggioStato(vecchioStato varchar2, nuovoStato varchar2) return varchar2 is
 begin
  return 'PSTATO '||vecchioStato||'->'||nuovoStato;
 end;

 procedure sto_PDG_PREVENTIVO (aPgStorico number, aDsStorico varchar2, aDest PDG_PREVENTIVO%rowtype) is
  begin
   insert into PDG_PREVENTIVO_S (
     pg_storico_
    ,ds_storico_
    ,ESERCIZIO
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
     aPgStorico
    ,aDsStorico
    ,aDest.ESERCIZIO
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
 procedure sto_PDG_PREVENTIVO_SPE_DET (aPgStorico number, aDsStorico varchar2, aDest PDG_PREVENTIVO_SPE_DET%rowtype) is
  begin
   insert into PDG_PREVENTIVO_SPE_DET_S (
     pg_storico_
    ,ds_storico_
    ,ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
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
   ) values (
     aPgStorico
    ,aDsStorico
    ,aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
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
    );
 end;
 procedure sto_PDG_PREVENTIVO_ETR_DET (aPgStorico number, aDsStorico varchar2, aDest PDG_PREVENTIVO_ETR_DET%rowtype) is
  begin
   insert into PDG_PREVENTIVO_ETR_DET_S (
     pg_storico_
    ,ds_storico_
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
    ,ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
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
   ) values (
     aPgStorico
    ,aDsStorico
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
    ,aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
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
    );
 end;
end;


