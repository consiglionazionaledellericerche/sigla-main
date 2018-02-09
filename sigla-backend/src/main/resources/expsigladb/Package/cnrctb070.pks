CREATE OR REPLACE package CNRCTB070 as
--
-- CNRCTB070 - Package di mappatura tabelle per la gestione delle VARIAZIONE AL PIANO DI GESTIONE
-- Date: 28/01/2003
-- Version: 1.3
--
-- Dependency:
--
-- History:
--
-- Date: 17/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 24/01/2002
-- Version: 1.1
-- Correzione: la costante ORIGINE_VARIAZIONE vale 'PDV', non 'VAR'
--
-- Date: 28/01/2003
-- Version: 1.2
-- Aggiunto lo stato sulla testata della variazione
--
-- Date: 28/01/2003
-- Version: 1.3
-- Fix hist.
--
-- Constants:
--
-- Origine dei dettagli PDG
--
-- Origine diretta imputazione utente
ORIGINE_VARIAZIONE CONSTANT VARCHAR2(3):='PDV';
--
-- Stati del piano di gestione per la fase di variazioni
--
STATO_PDG_MODIFICATO CONSTANT VARCHAR2(5):='M';
STATO_PDG_APERTURA_PER_VAR CONSTANT VARCHAR2(5):='G';
STATO_PDG_PRECHIUSURA_PER_VAR CONSTANT VARCHAR2(5):='H';
--
-- Stati del pdg_aggregato per la fase di variazioni
--
STATO_AGGREGATO_MODIFICATO CONSTANT VARCHAR2(5):='M';
STATO_AGGREGATO_ESAMINATO CONSTANT VARCHAR2(5):='E';

--
-- Stati variazione pdg
--
VARIAZIONE_APERTA CONSTANT CHAR(1):='A';
VARIAZIONE_CHIUSA CONSTANT CHAR(1):='C';
--
-- Functions e Procedures:
--
-- Controlla che il cdr specificato sia valido nell'esercizio specificato
-- ed titolare di un pdg aggregato (cdr di 1^ livello (non ente) o responsabile di
-- area
 function isCdrValidoPerAggregato(aEs number,aCdCdr varchar2) return boolean;

-- Wrappers generati automaticamente:
 procedure ins_PDG_PREVENTIVO_VAR (aDest PDG_PREVENTIVO_VAR%rowtype);
 procedure ins_PDG_PREVENTIVO_ETR_VAR (aDest PDG_PREVENTIVO_ETR_VAR%rowtype);
 procedure ins_PDG_PREVENTIVO_SPE_VAR (aDest PDG_PREVENTIVO_SPE_VAR%rowtype);
 procedure ins_PDG_AGGREGATO_ETR_VAR (aDest PDG_AGGREGATO_ETR_VAR%rowtype);
 procedure ins_PDG_AGGREGATO_SPE_VAR (aDest PDG_AGGREGATO_SPE_VAR%rowtype);
end;
/


CREATE OR REPLACE package body CNRCTB070 as
 function isCdrValidoPerAggregato(aEs number,aCdCdr varchar2) return boolean is
 	aCdr cdr%rowtype;
	aUo unita_organizzativa%rowtype;
 begin
  aCdr:=CNRCTB020.GETCDRVALIDO(aEs, aCdCdr);

	if (aCdr.livello = 1) then
		if (CNRCTB020.ISCDRENTE(aCdr)) then
			return false;
		end if;
		return true;
	end if;
	aUo := CNRCTB020.getUOValida(aEs,aCdr.cd_unita_organizzativa);
  if (aUo.cd_tipo_unita = CNRCTB020.TIPO_AREA) then
  	return true;
	end if;
	return false;
 end;

 procedure ins_PDG_PREVENTIVO_VAR (aDest PDG_PREVENTIVO_VAR%rowtype) is
  begin
   insert into PDG_PREVENTIVO_VAR (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,PG_VARIAZIONE_PDG
	,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.PG_VARIAZIONE_PDG
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_PDG_PREVENTIVO_ETR_VAR (aDest PDG_PREVENTIVO_ETR_VAR%rowtype) is
  begin
   insert into PDG_PREVENTIVO_ETR_VAR (
     ESERCIZIO
    ,CD_CDR_RESPONSABILE
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,PG_ENTRATA
    ,PG_VARIAZIONE_PDG
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDR_RESPONSABILE
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.PG_ENTRATA
    ,aDest.PG_VARIAZIONE_PDG
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_PDG_PREVENTIVO_SPE_VAR (aDest PDG_PREVENTIVO_SPE_VAR%rowtype) is
  begin
   insert into PDG_PREVENTIVO_SPE_VAR (
     ESERCIZIO
    ,CD_CDR_RESPONSABILE
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,PG_SPESA
    ,PG_VARIAZIONE_PDG
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDR_RESPONSABILE
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.PG_SPESA
    ,aDest.PG_VARIAZIONE_PDG
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_PDG_AGGREGATO_ETR_VAR (aDest PDG_AGGREGATO_ETR_VAR%rowtype) is
  begin
   insert into PDG_AGGREGATO_ETR_VAR (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_NATURA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,IM_RA_RCE
    ,IM_RB_RSE
    ,IM_RC_ESR
    ,IM_RD_A2_RICAVI
    ,IM_RE_A2_ENTRATE
    ,IM_RF_A3_RICAVI
    ,IM_RG_A3_ENTRATE
    ,PG_VARIAZIONE_PDG
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_NATURA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.IM_RA_RCE
    ,aDest.IM_RB_RSE
    ,aDest.IM_RC_ESR
    ,aDest.IM_RD_A2_RICAVI
    ,aDest.IM_RE_A2_ENTRATE
    ,aDest.IM_RF_A3_RICAVI
    ,aDest.IM_RG_A3_ENTRATE
    ,aDest.PG_VARIAZIONE_PDG
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_PDG_AGGREGATO_SPE_VAR (aDest PDG_AGGREGATO_SPE_VAR%rowtype) is
  begin
   insert into PDG_AGGREGATO_SPE_VAR (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_FUNZIONE
    ,CD_NATURA
    ,CD_CDS
    ,PG_VARIAZIONE_PDG
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
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.CD_FUNZIONE
    ,aDest.CD_NATURA
    ,aDest.CD_CDS
    ,aDest.PG_VARIAZIONE_PDG
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
end;
/


