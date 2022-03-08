--------------------------------------------------------
--  DDL for Package Body CNRCTB051
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB051" is
 Function getStato(aEs number, aCdCdr varchar2) return varchar2 is
  aStato varchar2(5);
 begin
  select stato into aStato from pdg_esercizio
  Where esercizio = aEs
  And   cd_centro_responsabilita = aCdCdr;
  return aStato;
 end;

 Function getStato(aEs number, aCdCdr VARCHAR2, aModulo VARCHAR2) return varchar2 is
  aStato varchar2(5);
 begin
  select stato into aStato from pdg_modulo
  Where esercizio = aEs
  And   cd_centro_responsabilita = aCdCdr
  And   pg_progetto = aModulo;
  return aStato;
 end;

 Procedure lockPdgP(aEs number, aCdCdr varchar2) is
  aTemp varchar2(20);
 begin
  begin
   select utuv into aTemp from pdg_esercizio where
        esercizio = aEs
    and cd_centro_responsabilita = aCdCdr
   for update nowait;
  exception when no_data_found then
   IBMERR001.RAISE_ERR_GENERICO('Piano di gestione preliminare del CDR '||aCdCdr||' non ancora aperto!');
  end;
 end;

 Procedure lockPdgP(aEs number, aCdCdr VARCHAR2, aModulo VARCHAR2) Is
  aTemp varchar2(20);
 begin
  begin
   select utuv into aTemp from pdg_modulo where
        esercizio = aEs
    and cd_centro_responsabilita = aCdCdr
    And   pg_progetto = aModulo
   for update nowait;
  exception when no_data_found then
   IBMERR001.RAISE_ERR_GENERICO('Piano di gestione preliminare del CDR '||aCdCdr||' per il modulo '||aModulo||' non ancora aperto!');
  end;
 end;

 Procedure resetCampiImporto(aDett in out pdg_modulo_spese%rowtype) is
 Begin
     aDett.IM_SPESE_GEST_DECENTRATA_INT	:=0;
     aDett.IM_SPESE_GEST_DECENTRATA_EST	:=0;
     aDett.IM_SPESE_GEST_ACCENTRATA_INT	:=0;
     aDett.IM_SPESE_GEST_ACCENTRATA_EST	:=0;
     aDett.IM_SPESE_A2 :=0;
     aDett.IM_SPESE_A3 :=0;
 End;

 Procedure resetCampiImporto(aDett in out pdg_modulo_costi%rowtype) is
 Begin
     aDett.RIS_ES_PREC_TIT_I :=0;
     aDett.RIS_ES_PREC_TIT_II :=0;
     aDett.RIS_PRES_ES_PREC_TIT_I :=0;
     aDett.RIS_PRES_ES_PREC_TIT_II :=0;
     aDett.IM_COSTI_GENERALI :=0;
     aDett.IM_CF_TFR :=0;
     aDett.IM_CF_TFR_DET :=0;
     aDett.IM_CF_AMM_IMMOBILI :=0;
     aDett.IM_CF_AMM_ATTREZZ :=0;
     aDett.IM_CF_AMM_ALTRO :=0;
 End;

 Procedure resetCampiImporto(aDett in out pdg_modulo_entrate%rowtype) is
 Begin
     aDett.IM_ENTRATA_TOT:=0;
     aDett.IM_ENTRATA:=0;
     aDett.IM_ENTRATA_APP:=0;
     aDett.IM_ENTRATA_A2:=0;
     aDett.IM_ENTRATA_A3:=0;
     aDett.IM_SPESE_VIVE:=0;
 End;

 Procedure resetCampiImporto(aDett in out pdg_modulo_spese_gest%rowtype) is
 Begin
     aDett.IM_SPESE_GEST_DECENTRATA_INT	:=0;
     aDett.IM_SPESE_GEST_DECENTRATA_EST	:=0;
     aDett.IM_SPESE_GEST_ACCENTRATA_INT	:=0;
     aDett.IM_SPESE_GEST_ACCENTRATA_EST	:=0;
     aDett.IM_PAGAMENTI :=0;
 End;

 Procedure ins_PDG_MODULO_SPESE (aDest PDG_MODULO_SPESE%rowtype) is
  begin
   Insert into PDG_MODULO_SPESE (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,PG_PROGETTO
    ,ID_CLASSIFICAZIONE
    ,CD_CDS_AREA
    ,IM_SPESE_GEST_DECENTRATA_INT
    ,IM_SPESE_GEST_DECENTRATA_EST
    ,IM_SPESE_GEST_ACCENTRATA_INT
    ,IM_SPESE_GEST_ACCENTRATA_EST
    ,IM_SPESE_A2
    ,IM_SPESE_A3
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,PG_DETTAGLIO
    ,CD_COFOG
    ,cd_missione
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.PG_PROGETTO
    ,aDest.ID_CLASSIFICAZIONE
    ,aDest.CD_CDS_AREA
    ,aDest.IM_SPESE_GEST_DECENTRATA_INT
    ,aDest.IM_SPESE_GEST_DECENTRATA_EST
    ,aDest.IM_SPESE_GEST_ACCENTRATA_INT
    ,aDest.IM_SPESE_GEST_ACCENTRATA_EST
    ,aDest.IM_SPESE_A2
    ,aDest.IM_SPESE_A3
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.pg_dettaglio
    ,aDest.cd_cofog
    ,aDest.cd_missione
    );
 end;

 procedure ins_PDG_MODULO_ENTRATE (aDest PDG_MODULO_ENTRATE%rowtype) is
  begin
   insert into PDG_MODULO_ENTRATE (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,PG_PROGETTO
    ,CDR_LINEA
    ,CD_LINEA_ATTIVITA
    ,CD_NATURA
    ,ID_CLASSIFICAZIONE
    ,PG_DETTAGLIO
    ,CD_TERZO
    ,DS_DETTAGLIO
    ,IM_ENTRATA_TOT
    ,ESERCIZIO_INIZIO
    ,ESERCIZIO_FINE
    ,IM_ENTRATA
    ,IM_ENTRATA_APP
    ,IM_ENTRATA_A2
    ,IM_ENTRATA_A3
    ,IM_SPESE_VIVE
    ,DS_SPESE_VIVE
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,CD_CDS_AREA
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.PG_PROGETTO
    ,aDest.CDR_LINEA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.CD_NATURA
    ,aDest.ID_CLASSIFICAZIONE
    ,aDest.PG_DETTAGLIO
    ,aDest.CD_TERZO
    ,aDest.DS_DETTAGLIO
    ,aDest.IM_ENTRATA_TOT
    ,aDest.ESERCIZIO_INIZIO
    ,aDest.ESERCIZIO_FINE
    ,aDest.IM_ENTRATA
    ,aDest.IM_ENTRATA_APP
    ,aDest.IM_ENTRATA_A2
    ,aDest.IM_ENTRATA_A3
    ,aDest.IM_SPESE_VIVE
    ,aDest.DS_SPESE_VIVE
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS_AREA
    );
 End;

 Procedure ins_PDG_MODULO (aDest PDG_MODULO%rowtype) is
  begin
   insert into PDG_MODULO (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,PG_PROGETTO
    ,STATO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.PG_PROGETTO
    ,aDest.STATO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_PDG_ESERCIZIO (aDest PDG_ESERCIZIO%rowtype) is
  begin
   CNRCTB050.ins_PDG_ESERCIZIO (aDest);
 End;

 procedure ins_PDG_MODULO_COSTI (aDest PDG_MODULO_COSTI%rowtype) is
  begin
   insert into PDG_MODULO_COSTI (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,PG_PROGETTO
    ,RIS_ES_PREC_TIT_I
    ,RIS_ES_PREC_TIT_II
    ,RIS_PRES_ES_PREC_TIT_I
    ,RIS_PRES_ES_PREC_TIT_II
    ,IM_COSTI_GENERALI
    ,IM_CF_TFR
    ,IM_CF_AMM_IMMOBILI
    ,IM_CF_AMM_ATTREZZ
    ,IM_CF_AMM_ALTRO
    ,IM_CF_TFR_DET
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.PG_PROGETTO
    ,aDest.RIS_ES_PREC_TIT_I
    ,aDest.RIS_ES_PREC_TIT_II
    ,aDest.RIS_PRES_ES_PREC_TIT_I
    ,aDest.RIS_PRES_ES_PREC_TIT_II
    ,aDest.IM_COSTI_GENERALI
    ,aDest.IM_CF_TFR
    ,aDest.IM_CF_AMM_IMMOBILI
    ,aDest.IM_CF_AMM_ATTREZZ
    ,aDest.IM_CF_AMM_ALTRO
    ,aDest.IM_CF_TFR_DET
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 Procedure ins_PDG_MODULO_SPESE_GEST (aDest PDG_MODULO_SPESE_GEST%rowtype) is
  Begin
   Insert into PDG_MODULO_SPESE_GEST (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,PG_PROGETTO
    ,ID_CLASSIFICAZIONE
    ,CD_CDS_AREA
    ,CD_CDR_ASSEGNATARIO
    ,CD_LINEA_ATTIVITA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,DT_REGISTRAZIONE
    ,DESCRIZIONE
    ,ORIGINE
    ,CATEGORIA_DETTAGLIO
    ,FL_SOLA_LETTURA
    ,IM_SPESE_GEST_DECENTRATA_INT
    ,IM_SPESE_GEST_DECENTRATA_EST
    ,IM_SPESE_GEST_ACCENTRATA_INT
    ,IM_SPESE_GEST_ACCENTRATA_EST
    ,IM_PAGAMENTI
    ,CD_CDR_ASSEGNATARIO_CLGS
    ,CD_LINEA_ATTIVITA_CLGS
    ,PG_PROGETTO_CLGS
    ,ESERCIZIO_PDG_VARIAZIONE
    ,PG_VARIAZIONE_PDG
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,pg_dettaglio
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.PG_PROGETTO
    ,aDest.ID_CLASSIFICAZIONE
    ,aDest.CD_CDS_AREA
    ,aDest.CD_CDR_ASSEGNATARIO
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DESCRIZIONE
    ,aDest.ORIGINE
    ,aDest.CATEGORIA_DETTAGLIO
    ,aDest.FL_SOLA_LETTURA
    ,aDest.IM_SPESE_GEST_DECENTRATA_INT
    ,aDest.IM_SPESE_GEST_DECENTRATA_EST
    ,aDest.IM_SPESE_GEST_ACCENTRATA_INT
    ,aDest.IM_SPESE_GEST_ACCENTRATA_EST
    ,aDest.IM_PAGAMENTI
    ,aDest.CD_CDR_ASSEGNATARIO_CLGS
    ,aDest.CD_LINEA_ATTIVITA_CLGS
    ,aDest.PG_PROGETTO_CLGS
    ,aDest.ESERCIZIO_PDG_VARIAZIONE
    ,aDest.PG_VARIAZIONE_PDG
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.pg_dettaglio
    );
 End;

 Procedure ins_PDG_MODULO_ENTRATE_GEST (aDest PDG_MODULO_ENTRATE_GEST%rowtype) is
  Begin
   Insert into PDG_MODULO_ENTRATE_GEST (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,PG_PROGETTO
    ,CD_NATURA
    ,ID_CLASSIFICAZIONE
    ,CD_CDS_AREA
    ,PG_DETTAGLIO
    ,CD_CDR_ASSEGNATARIO
    ,CD_LINEA_ATTIVITA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,DT_REGISTRAZIONE
    ,DESCRIZIONE
    ,ORIGINE
    ,CATEGORIA_DETTAGLIO
    ,FL_SOLA_LETTURA
    ,IM_ENTRATA
    ,IM_INCASSI
    ,CD_CDR_ASSEGNATARIO_CLGE
    ,CD_LINEA_ATTIVITA_CLGE
    ,ESERCIZIO_PDG_VARIAZIONE
    ,PG_VARIAZIONE_PDG
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.PG_PROGETTO
    ,aDest.CD_NATURA
    ,aDest.ID_CLASSIFICAZIONE
    ,aDest.CD_CDS_AREA
    ,aDest.PG_DETTAGLIO
    ,aDest.CD_CDR_ASSEGNATARIO
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DESCRIZIONE
    ,aDest.ORIGINE
    ,aDest.CATEGORIA_DETTAGLIO
    ,aDest.FL_SOLA_LETTURA
    ,aDest.IM_ENTRATA
    ,aDest.IM_INCASSI
    ,aDest.CD_CDR_ASSEGNATARIO_CLGE
    ,aDest.CD_LINEA_ATTIVITA_CLGE
    ,aDest.ESERCIZIO_PDG_VARIAZIONE
    ,aDest.PG_VARIAZIONE_PDG
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 End;

 Procedure ins_PDG_VARIAZIONE (aDest PDG_VARIAZIONE%Rowtype) Is
 Begin
 Insert Into PDG_VARIAZIONE (
     ESERCIZIO
    ,PG_VARIAZIONE_PDG
    ,CD_CENTRO_RESPONSABILITA
    ,DT_APERTURA
    ,DT_CHIUSURA
    ,DT_APPROVAZIONE
    ,DT_ANNULLAMENTO
    ,DS_VARIAZIONE
    ,DS_DELIBERA
    ,STATO
    ,RIFERIMENTI
    ,CD_CAUSALE_RESPINTA
    ,DS_CAUSALE_RESPINTA
    ,DT_APP_FORMALE
    ,TIPOLOGIA
    ,TIPOLOGIA_FIN
    ,TI_MOTIVAZIONE_VARIAZIONE
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,FL_VISTO_DIP_VARIAZIONI
    ,STATO_INVIO
    ,DT_FIRMA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.PG_VARIAZIONE_PDG
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.DT_APERTURA
    ,aDest.DT_CHIUSURA
    ,aDest.DT_APPROVAZIONE
    ,aDest.DT_ANNULLAMENTO
    ,aDest.DS_VARIAZIONE
    ,aDest.DS_DELIBERA
    ,aDest.STATO
    ,aDest.RIFERIMENTI
    ,aDest.CD_CAUSALE_RESPINTA
    ,aDest.DS_CAUSALE_RESPINTA
    ,aDest.DT_APP_FORMALE
    ,aDest.TIPOLOGIA
    ,aDest.TIPOLOGIA_FIN
    ,aDest.TI_MOTIVAZIONE_VARIAZIONE
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.FL_VISTO_DIP_VARIAZIONI
    ,aDest.STATO_INVIO
    ,aDest.DT_FIRMA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC);
 End;

 Procedure ins_PDG_VARIAZIONE_RIGA_GEST (aDest PDG_VARIAZIONE_RIGA_GEST%rowtype) Is
 Begin
 Insert Into PDG_VARIAZIONE_RIGA_GEST
 (ESERCIZIO,
  PG_VARIAZIONE_PDG,
  PG_RIGA,
  CD_CDR_ASSEGNATARIO,
  CD_LINEA_ATTIVITA,
  CD_CDS_AREA,
  TI_APPARTENENZA,
  TI_GESTIONE,
  CD_ELEMENTO_VOCE,
  DT_REGISTRAZIONE,
  DESCRIZIONE,
  CATEGORIA_DETTAGLIO,
  IM_SPESE_GEST_DECENTRATA_INT,
  IM_SPESE_GEST_DECENTRATA_EST,
  IM_SPESE_GEST_ACCENTRATA_INT,
  IM_SPESE_GEST_ACCENTRATA_EST,
  IM_ENTRATA,
  PG_RIGA_CLGS,
  CD_CDR_ASSEGNATARIO_CLGS,
  CD_LINEA_ATTIVITA_CLGS,
  UTCR,
  DACR,
  UTUV,
  DUVA,
  PG_VER_REC)
  Values
 (aDest.ESERCIZIO,
  aDest.PG_VARIAZIONE_PDG,
  aDest.PG_RIGA,
  aDest.CD_CDR_ASSEGNATARIO,
  aDest.CD_LINEA_ATTIVITA,
  aDest.CD_CDS_AREA,
  aDest.TI_APPARTENENZA,
  aDest.TI_GESTIONE,
  aDest.CD_ELEMENTO_VOCE,
  aDest.DT_REGISTRAZIONE,
  aDest.DESCRIZIONE,
  aDest.CATEGORIA_DETTAGLIO,
  aDest.IM_SPESE_GEST_DECENTRATA_INT,
  aDest.IM_SPESE_GEST_DECENTRATA_EST,
  aDest.IM_SPESE_GEST_ACCENTRATA_INT,
  aDest.IM_SPESE_GEST_ACCENTRATA_EST,
  aDest.IM_ENTRATA,
  aDest.PG_RIGA_CLGS,
  aDest.CD_CDR_ASSEGNATARIO_CLGS,
  aDest.CD_LINEA_ATTIVITA_CLGS,
  aDest.UTCR,
  aDest.DACR,
  aDest.UTUV,
  aDest.DUVA,
  aDest.PG_VER_REC);
End;

 Procedure ins_VAR_STANZ_RES (aDest VAR_STANZ_RES%rowtype) Is
 Begin
 Insert Into VAR_STANZ_RES (
     ESERCIZIO
    ,PG_VARIAZIONE
    ,CD_CDS
    ,CD_CENTRO_RESPONSABILITA
    ,ESERCIZIO_RES
    ,DT_APERTURA
    ,DS_VARIAZIONE
    ,DS_DELIBERA
    ,DT_CHIUSURA
    ,DT_APPROVAZIONE
    ,DT_ANNULLAMENTO
    ,DS_AGGIUNTIVA
    ,STATO
    ,TIPOLOGIA
    ,TIPOLOGIA_FIN
    ,TI_MOTIVAZIONE_VARIAZIONE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.PG_VARIAZIONE
    ,aDest.CD_CDS
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.ESERCIZIO_RES
    ,aDest.DT_APERTURA
    ,aDest.DS_VARIAZIONE
    ,aDest.DS_DELIBERA
    ,aDest.DT_CHIUSURA
    ,aDest.DT_APPROVAZIONE
    ,aDest.DT_ANNULLAMENTO
    ,aDest.DS_AGGIUNTIVA
    ,aDest.STATO
    ,aDest.TIPOLOGIA
    ,aDest.TIPOLOGIA_FIN
    ,aDest.TI_MOTIVAZIONE_VARIAZIONE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC);
 End;

 Procedure ins_ASS_VAR_STANZ_RES_CDR (aDest ASS_VAR_STANZ_RES_CDR%rowtype) Is
  begin
   Insert Into ASS_VAR_STANZ_RES_CDR (
     ESERCIZIO
    ,PG_VARIAZIONE
    ,CD_CENTRO_RESPONSABILITA
    ,IM_SPESA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.PG_VARIAZIONE
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.IM_SPESA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 Procedure ins_VAR_STANZ_RES_RIGA (aDest VAR_STANZ_RES_RIGA%rowtype) Is
 Begin
 Insert Into VAR_STANZ_RES_RIGA
 (ESERCIZIO,
  PG_VARIAZIONE,
  PG_RIGA,
  ESERCIZIO_VOCE,
  ESERCIZIO_RES,
  CD_CDR,
  CD_LINEA_ATTIVITA,
  TI_APPARTENENZA,
  TI_GESTIONE,
  CD_VOCE,
  CD_ELEMENTO_VOCE,
  IM_VARIAZIONE,
  UTCR,
  DACR,
  UTUV,
  DUVA,
  PG_VER_REC)
  Values
 (aDest.ESERCIZIO,
  aDest.PG_VARIAZIONE,
  aDest.PG_RIGA,
  aDest.ESERCIZIO_VOCE,
  aDest.ESERCIZIO_RES,
  aDest.CD_CDR,
  aDest.CD_LINEA_ATTIVITA,
  aDest.TI_APPARTENENZA,
  aDest.TI_GESTIONE,
  aDest.CD_VOCE,
  aDest.CD_ELEMENTO_VOCE,
  aDest.IM_VARIAZIONE,
  aDest.UTCR,
  aDest.DACR,
  aDest.UTUV,
  aDest.DUVA,
  aDest.PG_VER_REC);
End;


End;
