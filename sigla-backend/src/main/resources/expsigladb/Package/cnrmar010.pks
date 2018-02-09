CREATE OR REPLACE package CNRMAR010 as
--
-- CNRMAR010 - Package martello saldi (batch)
-- Date: 14/07/2006
-- Version: 1.13
--
-- Package per il martellamento/verifica disallineamento saldi
--
-- Dependency: IBMUTL 200/210
--
-- History:
--
-- Date: 26/09/2002
-- Version: 1.0
-- Creazione
--
-- Date: 29/09/2002
-- Version: 1.1
-- Estensioni
--
-- Date: 14/05/2003
-- Version: 1.2
-- Esteso controllo per competenza e residuo
--
-- Date: 19/05/2003
-- Version: 1.3
-- Inserita procedura Main per invocare tutti i controlli contemporaneamente.
--
-- Date: 21/05/2003
-- Version: 1.4
-- Modificata struttura log in output
--
-- Date: 21/05/2003
-- Version: 1.5
-- Modificata 2 struttura log in output
--
-- Date: 22/05/2003
-- Version: 1.6
-- Modificata procedura job_ControlloQuadratura per renderla eseguibile tramite batch dinamico
--
-- Date: 18/11/2003
-- Version: 1.7
-- Inserita variabile lImporto number (15,2)  per gestire un errore di arrotondamento
--
-- Date: 21/11/2003
-- Version: 1.8
-- Inserita variabile lImporto number (15,2) per tutti i calcoli degli importi
--
-- Date: 27/11/2003
-- Version: 1.9
-- Inserito due nuovi controlli:
-- Controllo 1) per tutte le voci in voce_f_saldi_cmp in competenza viene controllato che
--                       importo variazione piu = sum (var_bilancio_det per var +)
--                       importo variazione meno = sum (var_bilancio_det per var -)
-- Controllo 2) in voce_f_saldi_cmp per
--                              per cd_cds = 999
--                              and capitolo parte uno (capitolo inizia per 1.%)
--                              in competenza
--                              Spesa
--                              deve risultare che:
--                       importo stanz_ini_a1 + varpiu - varmeno = importo unica obbligazione associata
--
-- Date: 28/11/2003
-- Version: 1.10
-- nel controllo 1) precedentemente inserito sono state scaritate le variazioni non definitive
--
-- Date: 01/12/2003
-- Version: 1.11
-- Nel controllo 1) sono state escluse le variazioni che non rispettano
-- la seguente condizione: nella tabella VAR_BILANCIO ,
--             esercizio = esercizio_importi = esercizio in esame
--
-- Date: 03/02/20034
-- Version: 1.12
-- Inserito exception sul begin iniziale di job_mar_saldi00
--
-- Date: 14/07/2006
-- Version: 1.13
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:
--
TIPO_LOG_MAR_SALDI CONSTANT VARCHAR2(20):='MAR_SALDI00';
--
-- Functions e Procedures:
--
-- Parametri:

-- aEs -> Esercizio
-- aCDS -> Esercizio
-- isModifica -> Y = update N preview
 procedure job_ControlloQuadratura(aJob number, aPg_exec number, aNext_date date, aEsercizio number, aCdCds varchar2, aVerAgg varchar2, aUser varchar2);

 procedure job_mar_saldi00(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2, isModifica char);
 procedure job_mar_saldi01(job number, pg_exec number, next_date date, aEs number, aCdCDS varchar2, aCdUO varchar2, aCdCdr varchar2, aCd_voce VARCHAR2, aCd_linea_attivita VARCHAR2, isModifica char);
 Procedure job_mar_saldi02(job number, pg_exec number, next_date date, aEs number, aCdCDS varchar2, aCdUO varchar2, aCdCdr varchar2, aCd_voce VARCHAR2, aCd_linea_attivita VARCHAR2, isModifica char);

 function MSG_DIFF_SALDI(aStringa varchar2,aSaldoDoc v_voce_f_Saldi_cmp%rowtype, aSaldo voce_f_Saldi_cmp%rowtype) return varchar2;
 function MSGCSV_DIFF_SALDI(aDesc varchar2,aSaldoDoc v_voce_f_Saldi_cmp%rowtype, aSaldo voce_f_Saldi_cmp%rowtype) return varchar2;
 function MSG_DIFF_SALDI_CDR_LINEA(aStringa varchar2,aSaldoDoc V_VOCE_F_SALDI_CDR_LINEA%rowtype, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;

-- NEW procedure + FUNCTION

 procedure INSERT_SALDI_VUOTI_UTILIZZATI (aPg_exec NUMBER, INES NUMBER);

 function MSG_D_S_IM_STANZ_INIZIALE_A1(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_VARIAZIONI_PIU(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_VARIAZIONI_MENO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_IM_OBBL_ACC_COMP(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_IM_STANZ_RES_IMPROPRIO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_VAR_PIU_STANZ_RES_IMP(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_VAR_MENO_STANZ_RES_IMP(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_IM_OBBL_RES_IMP(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_IM_OBBL_RES_PRO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_VAR_PIU_OBBL_RES_PRO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_VAR_MENO_OBBL_RES_PRO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_IM_MANDATI_REV_PRO (aStringa varchar2, aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_IM_MANDATI_REV_IMP (aStringa varchar2, aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
 function MSG_D_S_IM_PAGAMENTI_INCASSI(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;

 function MSGCSV_DIFF_SALDI_CDR_LINEA(aDesc varchar2,aSaldoDoc V_VOCE_F_SALDI_CDR_LINEA%rowtype, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2;
end;
/


CREATE OR REPLACE package body CNRMAR010 is
 function fnum(anum number) return varchar2 is
 begin
  return to_char(anum,'9999999999990D99');
 end;

 function descAcc(aAcc accertamento%rowtype) return varchar2 is
 begin
  return 'es:'||aAcc.esercizio||' cds:'||aAcc.cd_cds||' uo:'||aAcc.cd_unita_organizzativa||' esOri:'||aAcc.esercizio_originale||' pg:'||aAcc.pg_accertamento||' cds_ori:'||aAcc.cd_cds_origine||' uo_ori'||aAcc.cd_uo_origine;
 end;

 function descAccScad(aAccScad accertamento_scadenzario%rowtype) return varchar2 is
 begin
  return 'pg_scad:'||aAccScad.pg_accertamento_scadenzario||' date:'||aAccScad.dt_scadenza_incasso;
 end;

 function descAccScadVoce(aAccScadVoce accertamento_scad_voce%rowtype) return varchar2 is
 begin
  return 'cd_cdr:'||aAccScadVoce.cd_centro_responsabilita||' la:'||aAccScadVoce.cd_linea_attivita;
 end;

 function descObb(aObb obbligazione%rowtype) return varchar2 is
 begin
  return 'es:'||aObb.esercizio||' cds:'||aObb.cd_cds||' uo:'||aObb.cd_unita_organizzativa||' esOri:'||aObb.esercizio_originale||' pg:'||aObb.pg_obbligazione||' cds_ori:'||aObb.cd_cds_origine||' uo_ori'||aObb.cd_uo_origine;
 end;

 function descObbScad(aObbScad obbligazione_scadenzario%rowtype) return varchar2 is
 begin
  return 'pg_scad:'||aObbScad.pg_obbligazione_scadenzario||' date:'||aObbScad.dt_scadenza;
 end;

 function descObbScadVoce(aObbScadVoce obbligazione_scad_voce%rowtype) return varchar2 is
 begin
  return 'cd_cdr:'||aObbScadVoce.cd_centro_responsabilita||' la:'||aObbScadVoce.cd_linea_attivita||' voce:'||aObbScadVoce.cd_voce;
 end;


 function MSG_CHIAVE_CAPITOLO(aCapitolo voce_f_Saldi_cmp%rowtype)
 return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=
   'Cds:'||RPAD(aCapitolo.cd_cds,30)||' '||
   'Esercizio:'||RPAD(aCapitolo.esercizio,4)||' '||
   'Appartenenza:'||aCapitolo.ti_appartenenza||' '||
   'Gestione:'||aCapitolo.ti_gestione||' '||
   'voce:'||RPAD(aCapitolo.cd_voce,30)||' '||
   'Com/Res:'||aCapitolo.ti_competenza_residuo||' ';
  return aOut;
 end;

 function MSG_DIFF_SALDI(aStringa varchar2,aSaldoDoc v_voce_f_Saldi_cmp%rowtype, aSaldo voce_f_Saldi_cmp%rowtype)
 return varchar2 is
  aOut varchar2(1000);
 begin

  aOut:=aStringa||
   'Cap:'||RPAD(aSaldoDoc.cd_voce,26)||' '||
   'Cds:'||RPAD(aSaldoDoc.cd_cds,4)||' '||
   'E/S:'||aSaldoDoc.ti_gestione||' '||
   'C/R:'||aSaldoDoc.ti_competenza_residuo||' '||

   'PD:'||LPAD(fnum(aSaldoDoc.IM_OBBLIG_IMP_ACR),17)||' '||Chr(13)||
   'PS:'||LPAD(fnum(aSaldo.IM_OBBLIG_IMP_ACR),17)||' '||Chr(13)||
   'DP:'||LPAD(fnum(aSaldo.IM_OBBLIG_IMP_ACR - aSaldoDoc.IM_OBBLIG_IMP_ACR),17)||' '||Chr(13)||

   'AD:'||LPAD(fnum(aSaldoDoc.IM_MANDATI_REVERSALI),17)||' '||Chr(13)||
   'AS:'||LPAD(fnum(aSaldo.IM_MANDATI_REVERSALI),17)||' '||Chr(13)||
   'DA:'||LPAD(fnum(aSaldo.IM_MANDATI_REVERSALI - aSaldoDoc.IM_MANDATI_REVERSALI),17)||' '||Chr(13)||

   'UD:'||LPAD(fnum(aSaldoDoc.IM_PAGAMENTI_INCASSI),17)||' '||Chr(13)||
   'US:'||LPAD(fnum(aSaldo.IM_PAGAMENTI_INCASSI),17)||' '||Chr(13)||
   'DU:'||LPAD(fnum(aSaldo.IM_PAGAMENTI_INCASSI - aSaldoDoc.IM_PAGAMENTI_INCASSI),17);
  return aOut;
 end;

 function MSG_DIFF_SALDI_CDR_LINEA(aStringa varchar2,aSaldoDoc V_VOCE_F_SALDI_CDR_LINEA%rowtype, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 return varchar2 is
  aOut varchar2(1000);
 begin

  aOut:=aStringa||
   'Cap:'||RPAD(aSaldoDoc.cd_voce,26)||' '||
   'Cdr:'||RPAD(aSaldoDoc.cd_centro_responsabilita,30)||' '||
   'Linea:'||RPAD(aSaldoDoc.cd_linea_attivita,10)||' '||
   'E/S:'||aSaldoDoc.ti_gestione||' '||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||

   Rpad('C-STANZIAMENTI:',20)||LPAD(fnum(aSaldoDoc.STANZIAMENTI),17)||' '||Chr(13)||
   Rpad('S-STANZIAMENTI:',20)||LPAD(fnum(aSaldo.IM_STANZ_INIZIALE_A1),17)||' '||Chr(13)||
   Rpad('D-STANZIAMENTI:',20)||LPAD(fnum(aSaldo.IM_STANZ_INIZIALE_A1 - aSaldoDoc.STANZIAMENTI),17)||' '||Chr(13)||

   Rpad('C-VARIAZIONI_PIU:',20)||LPAD(fnum(aSaldoDoc.VARIAZIONI_PIU),17)||' '||Chr(13)||
   Rpad('S-VARIAZIONI_PIU:',20)||LPAD(fnum(aSaldo.VARIAZIONI_PIU),17)||' '||Chr(13)||
   Rpad('D-VARIAZIONI_PIU:',20)||LPAD(fnum(aSaldo.VARIAZIONI_PIU - aSaldoDoc.VARIAZIONI_PIU),17)||' '||Chr(13)||

   Rpad('C-VARIAZIONI_MENO:',20)||LPAD(fnum(aSaldoDoc.VARIAZIONI_MENO),17)||' '||Chr(13)||
   Rpad('S-VARIAZIONI_MENO:',20)||LPAD(fnum(aSaldo.VARIAZIONI_MENO),17)||' '||Chr(13)||
   Rpad('D-VARIAZIONI_MENO:',20)||LPAD(fnum(aSaldo.VARIAZIONI_MENO - aSaldoDoc.VARIAZIONI_MENO),17)||' '||Chr(13)||

   Rpad('C-OBBLIGAZIONI:',20)||LPAD(fnum(aSaldoDoc.IM_OBBL_ACC_COMP),17)||' '||Chr(13)||
   Rpad('S-OBBLIGAZIONI:',20)||LPAD(fnum(aSaldo.IM_OBBL_ACC_COMP),17)||' '||Chr(13)||
   Rpad('D-OBBLIGAZIONI:',20)||LPAD(fnum(aSaldo.IM_OBBL_ACC_COMP - aSaldoDoc.IM_OBBL_ACC_COMP),17)||' '||Chr(13)||

   Rpad('C-MANDATI:',20)||LPAD(fnum(aSaldoDoc.IM_MANDATI_REVERSALI_PRO),17)||' '||Chr(13)||
   Rpad('S-MANDATI:',20)||LPAD(fnum(aSaldo.IM_MANDATI_REVERSALI_PRO),17)||' '||Chr(13)||
   Rpad('D-MANDATI:',20)||LPAD(fnum(aSaldo.IM_MANDATI_REVERSALI_PRO - aSaldoDoc.IM_MANDATI_REVERSALI_PRO),17)||' '||Chr(13)||

   Rpad('C-PAGAMENTI:',20)||LPAD(fnum(aSaldoDoc.IM_PAGAMENTI_INCASSI),17)||' '||Chr(13)||
   Rpad('S-PAGAMENTI:',20)||LPAD(fnum(aSaldo.IM_PAGAMENTI_INCASSI),17)||' '||Chr(13)||
   Rpad('D-PAGAMENTI:',20)||LPAD(fnum(aSaldo.IM_PAGAMENTI_INCASSI - aSaldoDoc.IM_PAGAMENTI_INCASSI),17);

  return aOut;
 end;


 function MSG_D_S_IM_STANZ_INIZIALE_A1(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 Return varchar2 is
  aOut varchar2(1000);
 begin

  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-STANZIAMENTI:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-STANZIAMENTI:',20)||LPAD(fnum(aSaldo.IM_STANZ_INIZIALE_A1),17)||' '||Chr(13)||
   Rpad('D-STANZIAMENTI:',20)||LPAD(fnum(aSaldo.IM_STANZ_INIZIALE_A1 - aNewValore),17);

  return aOut;
 end;

 function MSG_D_S_VARIAZIONI_PIU(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-VARIAZIONI PIU:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-VARIAZIONI PIU:',20)||LPAD(fnum(aSaldo.VARIAZIONI_PIU),17)||' '||Chr(13)||
   Rpad('D-VARIAZIONI PIU:',20)||LPAD(fnum(aSaldo.VARIAZIONI_PIU - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_VARIAZIONI_MENO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-VARIAZIONI MENO:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-VARIAZIONI MENO:',20)||LPAD(fnum(aSaldo.VARIAZIONI_MENO),17)||' '||Chr(13)||
   Rpad('D-VARIAZIONI MENO:',20)||LPAD(fnum(aSaldo.VARIAZIONI_MENO - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_IM_OBBL_ACC_COMP(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-IM_OBBL_ACC_COMP:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-IM_OBBL_ACC_COMP:',20)||LPAD(fnum(aSaldo.IM_OBBL_ACC_COMP),17)||' '||Chr(13)||
   Rpad('D-IM_OBBL_ACC_COMP:',20)||LPAD(fnum(aSaldo.IM_OBBL_ACC_COMP - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_IM_STANZ_RES_IMPROPRIO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-IM_STANZ_RES_IMPROPRIO:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-IM_STANZ_RES_IMPROPRIO:',20)||LPAD(fnum(aSaldo.IM_STANZ_RES_IMPROPRIO),17)||' '||Chr(13)||
   Rpad('D-IM_STANZ_RES_IMPROPRIO:',20)||LPAD(fnum(aSaldo.IM_STANZ_RES_IMPROPRIO - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_VAR_PIU_STANZ_RES_IMP(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-VAR_PIU_STANZ_RES_IMP:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-VAR_PIU_STANZ_RES_IMP:',20)||LPAD(fnum(aSaldo.VAR_PIU_STANZ_RES_IMP),17)||' '||Chr(13)||
   Rpad('D-VAR_PIU_STANZ_RES_IMP:',20)||LPAD(fnum(aSaldo.VAR_PIU_STANZ_RES_IMP - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_VAR_MENO_STANZ_RES_IMP(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-VAR_MENO_STANZ_RES_IMP:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-VAR_MENO_STANZ_RES_IMP:',20)||LPAD(fnum(aSaldo.VAR_MENO_STANZ_RES_IMP),17)||' '||Chr(13)||
   Rpad('D-VAR_MENO_STANZ_RES_IMP:',20)||LPAD(fnum(aSaldo.VAR_MENO_STANZ_RES_IMP - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_IM_OBBL_RES_IMP(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-IM_OBBL_RES_IMP:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-IM_OBBL_RES_IMP:',20)||LPAD(fnum(aSaldo.IM_OBBL_RES_IMP),17)||' '||Chr(13)||
   Rpad('D-IM_OBBL_RES_IMP:',20)||LPAD(fnum(aSaldo.IM_OBBL_RES_IMP - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_IM_OBBL_RES_PRO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-IM_OBBL_RES_PRO:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-IM_OBBL_RES_PRO:',20)||LPAD(fnum(aSaldo.IM_OBBL_RES_PRO),17)||' '||Chr(13)||
   Rpad('D-IM_OBBL_RES_PRO:',20)||LPAD(fnum(aSaldo.IM_OBBL_RES_PRO - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_VAR_PIU_OBBL_RES_PRO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
 Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-VAR_PIU_OBBL_RES_PRO:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-VAR_PIU_OBBL_RES_PRO:',20)||LPAD(fnum(aSaldo.VAR_PIU_OBBL_RES_PRO),17)||' '||Chr(13)||
   Rpad('D-VAR_PIU_OBBL_RES_PRO:',20)||LPAD(fnum(aSaldo.VAR_PIU_OBBL_RES_PRO - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_VAR_MENO_OBBL_RES_PRO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
  Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-VAR_MENO_OBBL_RES_PRO:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-VAR_MENO_OBBL_RES_PRO:',20)||LPAD(fnum(aSaldo.VAR_MENO_OBBL_RES_PRO),17)||' '||Chr(13)||
   Rpad('D-VAR_MENO_OBBL_RES_PRO:',20)||LPAD(fnum(aSaldo.VAR_MENO_OBBL_RES_PRO - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_IM_MANDATI_REV_PRO(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
  Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-IM_MANDATI_REVERSALI_PRO:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-IM_MANDATI_REVERSALI_PRO:',20)||LPAD(fnum(aSaldo.IM_MANDATI_REVERSALI_PRO),17)||' '||Chr(13)||
   Rpad('D-IM_MANDATI_REVERSALI_PRO:',20)||LPAD(fnum(aSaldo.IM_MANDATI_REVERSALI_PRO - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_IM_MANDATI_REV_IMP(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
  Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-IM_MANDATI_REVERSALI_IMP:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-IM_MANDATI_REVERSALI_IMP:',20)||LPAD(fnum(aSaldo.IM_MANDATI_REVERSALI_IMP),17)||' '||Chr(13)||
   Rpad('D-IM_MANDATI_REVERSALI_IMP:',20)||LPAD(fnum(aSaldo.IM_MANDATI_REVERSALI_IMP - aNewValore),17);
  return aOut;
 end;

 function MSG_D_S_IM_PAGAMENTI_INCASSI(aStringa varchar2,aNewValore VARCHAR2, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype)
  Return varchar2 is
  aOut varchar2(1000);
 begin
  aOut:=aStringa||
   'Es: '||RPAD(aSaldo.esercizio,4)||Chr(13)||
   'Es. Res: '||RPAD(aSaldo.esercizio_res, 4)||Chr(13)||
   'Cdr : '||RPAD(aSaldo.cd_centro_responsabilita,30)||Chr(13)||
   'Linea : '||RPAD(aSaldo.cd_linea_attivita,10)||Chr(13)||
   'App : '||RPAD(aSaldo.ti_appartenenza, 1)||Chr(13)||
   'E/S : '||aSaldo.ti_gestione||Chr(13)||
   'Voce : '||RPAD(aSaldo.cd_voce,26)||Chr(13)||
   'Legenda: "C" Calcolate, "S" Saldi, "D" Differenza'||Chr(13)||
   Rpad('C-IM_PAGAMENTI_INCASSI:',20)||LPAD(fnum(aNewValore),17)||' '||Chr(13)||
   Rpad('S-IM_PAGAMENTI_INCASSI:',20)||LPAD(fnum(aSaldo.IM_PAGAMENTI_INCASSI),17)||' '||Chr(13)||
   Rpad('D-IM_PAGAMENTI_INCASSI:',20)||LPAD(fnum(aSaldo.IM_PAGAMENTI_INCASSI - aNewValore),17);
  return aOut;
 end;

 Function MSGCSV_DIFF_SALDI(aDesc varchar2,aSaldoDoc v_voce_f_Saldi_cmp%rowtype, aSaldo voce_f_Saldi_cmp%rowtype) return varchar2 is
  aOut varchar2(1000);
 begin

  aOut:='"'||aDesc||'",'||
   '"'||aSaldoDoc.cd_voce||'",'||
   '"'||aSaldoDoc.cd_cds||'",'||
   '"'||aSaldoDoc.ti_gestione||'",'||
   '"'||aSaldoDoc.ti_competenza_residuo||'",'||

   aSaldoDoc.IM_OBBLIG_IMP_ACR||','||
   (aSaldo.IM_OBBLIG_IMP_ACR - aSaldoDoc.IM_OBBLIG_IMP_ACR)||','||

   aSaldoDoc.IM_MANDATI_REVERSALI||','||
   (aSaldo.IM_MANDATI_REVERSALI - aSaldoDoc.IM_MANDATI_REVERSALI)||','||

   aSaldoDoc.IM_PAGAMENTI_INCASSI||','||
   (aSaldo.IM_PAGAMENTI_INCASSI - aSaldoDoc.IM_PAGAMENTI_INCASSI);

  return aOut;
 end;

 function MSGCSV_DIFF_SALDI_CDR_LINEA(aDesc varchar2,aSaldoDoc V_VOCE_F_SALDI_CDR_LINEA%rowtype, aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype) return varchar2 is
  aOut varchar2(1000);
 begin

  aOut:='"'||aDesc||'",'||
   '"'||aSaldoDoc.cd_voce||'",'||
   '"'||aSaldoDoc.cd_centro_responsabilita||'",'||
   '"'||aSaldoDoc.cd_linea_attivita||'",'||
   '"'||aSaldoDoc.ti_gestione||'",'||

   aSaldoDoc.IM_OBBL_ACC_COMP||','||
   (aSaldo.IM_OBBL_ACC_COMP - aSaldoDoc.IM_OBBL_ACC_COMP)||','||

   aSaldoDoc.IM_MANDATI_REVERSALI_PRO||','||
   (aSaldo.IM_MANDATI_REVERSALI_PRO - aSaldoDoc.IM_MANDATI_REVERSALI_PRO)||','||

   aSaldoDoc.IM_PAGAMENTI_INCASSI||','||
   (aSaldo.IM_PAGAMENTI_INCASSI - aSaldoDoc.IM_PAGAMENTI_INCASSI);

  return aOut;
 end;

 procedure job_ControlloQuadratura(aJob number, aPg_exec number, aNext_date date, aEsercizio number, aCdCds varchar2, aVerAgg varchar2, aUser varchar2) is
  lJob NUMBER;
  lPgExec NUMBER;
  lNextDate DATE;
  ISMODIFICA CHAR(1);
  lNumCds number;
  aEndT date;
  aStartT date;
  aStart varchar2(80);
  aEnd varchar2(80);
  aDelta varchar2(80);
 begin
         if upper(aVerAgg) = 'VERIFICA' then
                ISMODIFICA :='N';
         else
                 if upper(aVerAgg) = 'AGGIORNAMENTO' then
                        ISMODIFICA :='Y';
                 else
                         ibmerr001.RAISE_ERR_GENERICO ('Il parametro in input aVerAgg puo assumere i valori VERIFICA o AGGIORNAMENTO');
                 end if;
         end if;
         aStart:=to_char(sysdate,'YYYYMMDD HH:MI:SS');
         lJob := aJob;
         lNumCds := 0;
         if aPg_exec is null then
                lPgExec := ibmutl200.LOGSTART('Procedura per il controllo di saldi e quadratura di importi' , aUser, null, null);
         else
                 lPgExec := aPg_exec;
         end if;
         lNextDate := aNext_date;
         IBMUTL200.logInf(lPgExec,'Controllo Quadratura START  at: '||aStart||' es.'||aEsercizio||' cds.'||aCdCds,aEsercizio||aCdCds,'SOC');
         if aCdCds = '*' then
                 for lCdsValida in(select * from v_unita_organizzativa_valida where fl_cds ='Y' and esercizio = aEsercizio)
                 loop
                         lNumCds := lNumCds + 1;
                         -- Verifica Saldi
                         CNRMAR010.JOB_MAR_SALDI00 ( lJob, lPgExec, lNextDate,lCdsValida.esercizio, lCdsValida.cd_unita_organizzativa, ISMODIFICA );
                         -- Verifica Saldi
                         CNRMAR030.JOB_MAR_PRIMI00 ( lJob, lPgExec, lNextDate, lCdsValida.esercizio, lCdsValida.cd_unita_organizzativa, ISMODIFICA );
                         -- Verifica Saldi
                         CNRMAR037.JOB_MAR_AUTOR00 ( lJob, lPgExec, lNextDate, lCdsValida.esercizio, lCdsValida.cd_unita_organizzativa, ISMODIFICA );
                 end loop;
         Else
                for lCdsValida in(select * from v_unita_organizzativa_valida where fl_cds ='Y' and esercizio = aEsercizio)
                 Loop
                   lNumCds := lNumCds + 1;
                 -- Verifica Saldi
                 CNRMAR010.JOB_MAR_SALDI00 ( lJob, lPgExec, lNextDate, lCdsValida.esercizio, lCdsValida.cd_unita_organizzativa, ISMODIFICA );
                 -- Verifica Saldi
                 CNRMAR030.JOB_MAR_PRIMI00 ( lJob, lPgExec, lNextDate, lCdsValida.esercizio, lCdsValida.cd_unita_organizzativa, ISMODIFICA );
                 -- Verifica Saldi
                 CNRMAR037.JOB_MAR_AUTOR00 ( lJob, lPgExec, lNextDate, lCdsValida.esercizio, lCdsValida.cd_unita_organizzativa, ISMODIFICA );
                end loop;
         end if;
     aEndT:=sysdate;
     aEnd:=to_char(aEndT,'YYYYMMDD HH:MI:SS');
     aDelta:=to_char((aEndT-aStartT)*24*3600,'999999');
     IBMUTL200.logInf(lPgExec,'Controllo Quadratura END at: '||aEnd||' tot exec time(s):'||aDelta||' es.'||aEsercizio||' cds.'||aCdCds,aEsercizio||aCdCds,'SOC');
 end;

 procedure getVariazioni (aCapitolo voce_f_saldi_cmp%rowtype, aImVariazionePiu in out number, aImVariazioneMeno in out number) as
 lImVariazionePiu number (15,2);
 lImVariazioneMeno number (15,2);
 begin
          lImVariazionePiu := 0;
          lImVariazioneMeno := 0;

          select sum(decode(abs(det.im_variazione)/det.im_variazione,1,det.im_variazione,0)),
                         sum(decode(abs(det.im_variazione)/det.im_variazione,-1,det.im_variazione,0))
          into lImVariazionePiu, lImVariazioneMeno
          from var_bilancio_det det,
                   var_bilancio var
          where var.cd_cds= det.cd_cds
          and   var.esercizio = det.esercizio
          and   var.ti_appartenenza = det.ti_appartenenza
          and   var.pg_variazione = det.pg_variazione
          and   var.STATO = 'D'
          and   var.esercizio= var.esercizio_importi
          and   det.cd_cds = aCapitolo.cd_cds
          and   det.esercizio = aCapitolo.esercizio
          and   det.ti_appartenenza = aCapitolo.ti_appartenenza
          and   det.ti_gestione =aCapitolo.ti_gestione
          and   det.cd_voce =aCapitolo.cd_voce
          and   det.im_variazione <>0 ;

          aImVariazionePiu := lImVariazionePiu;
          aImVariazioneMeno     := lImVariazioneMeno;
 exception
 when others then
          aImVariazionePiu := lImVariazionePiu;
          aImVariazioneMeno     := lImVariazioneMeno;
 end;

 function getImObbligazione(aPgExec number , aCapitolo voce_f_saldi_cmp%rowtype) return number as
 lRisultato number (15,2);
 lObbVoce obbligazione_scad_voce%rowtype;
 begin
          lRisultato := 0;
          begin
                  select voc.*
                  into lObbVoce
                  from obbligazione obb,
                           obbligazione_scad_voce voc
                  where voc.cd_cds = aCapitolo.cd_cds
                  and   voc.esercizio = aCapitolo.esercizio
                  and   voc.ti_appartenenza = aCapitolo.ti_appartenenza
                  and   voc.ti_gestione = aCapitolo.ti_gestione
                  and   voc.cd_voce = aCapitolo.cd_voce
                  and   obb.cd_cds = voc.cd_cds
                  and   obb.esercizio = voc.esercizio
                  and   obb.esercizio_originale = voc.esercizio_originale
		  and   obb.pg_obbligazione = voc.pg_obbligazione
                  and   obb.cd_tipo_documento_cont  <> 'IMP_RES' ;
          exception
          when others then
          IBMUTL200.logInf(aPgExec,
                                   'D_SALDI060 - SCADENZE MULTIPLE O INESISTENTI ',
                                   MSG_CHIAVE_CAPITOLO(aCapitolo),
                                                   'SOCD');
          end;
          lRisultato :=  lObbVoce.im_voce;

          return lRisultato;
 end;


 procedure job_mar_saldi00(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2, isModifica char) is
  aStringa varchar2(4000);
  aTSNow date;
  aUser varchar2(20);
  aChekLock CHAR(1);
  aChekUpdate CHAR(1);
  aMan mandato%rowtype;
  aRev reversale%rowtype;
  aMsgTipoMar varchar2(100);
  aEndT date;
  aStartT date;
  aEnd varchar2(80);
  aStart varchar2(80);
  aDelta varchar2(80);
  aSaldo voce_f_saldi_cmp%rowtype;
  lImporto number(15,2);
  lImportoTotCap number(15,2);
  lImportoScadVoce number(15,2);
  lImVariazionePiu number(15,2);
  lImVariazioneMeno number(15,2);
 begin
  aTSNow:=sysdate;
  aUser:=IBMUTL200.getUserFromLog(pg_exec);
  if isModifica = 'Y' then
   aMsgTipoMar:='MODIFICA';
  else
   aMsgTipoMar:='VERIFICA';
  end if;
  aStartT:=sysdate;
  aStart:=to_char(sysdate,'YYYYMMDD HH:MI:SS');
  IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_MAR_SALDI, job, 'Batch di '||aMsgTipoMar||' dei saldi. Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));
  BEGIN
   IBMUTL200.logInf(pg_exec,'DIFFSALDI START  at: '||aStart||' es.'||aEs||' cds.'||aCdCds,aEs||aCdCds,'SOC');
   -- Check saldi primi documenti di spesa
   for aSaldoDoc in (
         select * from v_voce_f_saldi_cmp where
              cd_cds = Decode(aCdCds,'TUTTI',cd_cds,aCdCds)
          and esercizio = aEs
   ) loop

    Begin
         select *
         into aSaldo
         from voce_f_saldi_cmp
         Where cd_cds = aSaldoDoc.cd_cds
          and esercizio = aSaldoDoc.esercizio
          and ti_appartenenza = aSaldoDoc.ti_appartenenza
          and ti_gestione = aSaldoDoc.ti_gestione
          and cd_voce = aSaldoDoc.cd_voce
          and ti_competenza_residuo = aSaldoDoc.ti_competenza_residuo
          for update nowait;

        lImporto := 0;
        lImporto := aSaldoDoc.im_obblig_imp_acr;

    if lImporto - aSaldo.im_obblig_imp_acr != 0 Then
     if isModifica = 'Y' Then
       Update voce_f_saldi_cmp
       Set im_obblig_imp_acr = lImporto
       Where cd_cds = aSaldoDoc.cd_cds
         And esercizio = aSaldoDoc.esercizio
         And ti_appartenenza = aSaldoDoc.ti_appartenenza
         And ti_gestione = aSaldoDoc.ti_gestione
         And cd_voce = aSaldoDoc.cd_voce
         And ti_competenza_residuo = aSaldoDoc.ti_competenza_residuo;
     End If;
     IBMUTL200.logInf(pg_exec,MSG_DIFF_SALDI('DIS - DISSALDI010 - IM_OBBLIG_IMP_ACR ',aSaldoDoc, aSaldo),MSGCSV_DIFF_SALDI('DISSALDI010',aSaldoDoc, aSaldo),'SOCD');
    end if;

        lImporto := 0;
        lImporto := aSaldoDoc.im_mandati_reversali;

        if lImporto - aSaldo.im_mandati_reversali != 0 Then
     if isModifica = 'Y' Then
       Update voce_f_saldi_cmp
       Set im_mandati_reversali = lImporto
       Where cd_cds = aSaldoDoc.cd_cds
         And esercizio = aSaldoDoc.esercizio
         And ti_appartenenza = aSaldoDoc.ti_appartenenza
         And ti_gestione = aSaldoDoc.ti_gestione
         And cd_voce = aSaldoDoc.cd_voce
         And ti_competenza_residuo = aSaldoDoc.ti_competenza_residuo;
     End If;
     IBMUTL200.logInf(pg_exec,MSG_DIFF_SALDI('DIS - DISSALDI020 - IM_MANDATI_REVERSALI ',aSaldoDoc, aSaldo),MSGCSV_DIFF_SALDI('DISSALDI020',aSaldoDoc, aSaldo),'SOCD');
    end if;

        lImporto := 0;
        lImporto := aSaldoDoc.im_pagamenti_incassi;

        if lImporto - aSaldo.im_pagamenti_incassi != 0 Then
     if isModifica = 'Y' Then
       Update voce_f_saldi_cmp
       Set im_pagamenti_incassi = lImporto
       Where cd_cds = aSaldoDoc.cd_cds
         And esercizio = aSaldoDoc.esercizio
         And ti_appartenenza = aSaldoDoc.ti_appartenenza
         And ti_gestione = aSaldoDoc.ti_gestione
         And cd_voce = aSaldoDoc.cd_voce
         And ti_competenza_residuo = aSaldoDoc.ti_competenza_residuo;
     End If;
     IBMUTL200.logInf(pg_exec,MSG_DIFF_SALDI('DIS - DISSALDI030 - IM_PAGAMENTI_INCASSI ',aSaldoDoc, aSaldo),MSGCSV_DIFF_SALDI('DISSALDI030',aSaldoDoc, aSaldo),'SOCD');
    end if;

    Exception
      When No_Data_Found Then Null;
    End;

   end loop;


   -- Controllo 1) per tutte le voci in voce_f_saldi_cmp in competenza viene controllato che
   --                    importo variazione piu = sum (var_bilancio_det per var +)
   --                    importo variazione meno = sum (var_bilancio_det per var -)

   for lCapitolo in (select * from voce_f_saldi_cmp
                                                   where cd_cds = aCdCds
                                                   and   esercizio = aEs
                                           and   ti_competenza_residuo = 'C'
                                                   for update nowait)
   loop
       -- Controllo 1) per tutte le voci in voce_f_saldi_cmp in competenza viene controllato che
           --                    importo variazione piu = sum (var_bilancio_det per var +)
           --                    importo variazione meno = sum (var_bilancio_det per var -)
           getVariazioni(lCapitolo,lImVariazionePiu,lImVariazioneMeno);

           if lCapitolo.variazioni_piu <> lImVariazionePiu then
          IBMUTL200.logInf(pg_exec,
                                   'D_SALDI040 - VARIAZIONI_PIU - ' ||MSG_CHIAVE_CAPITOLO(lCapitolo),
                                   'VARIAZIONI_PIU :'|| LPAD(fnum(lCapitolo.variazioni_piu ),17)
                                    ||'TOTALE_VAR_PIU :'|| LPAD(fnum(lImVariazionePiu ),17)
                            ||'Delta :'||LPAD(fnum(lCapitolo.variazioni_piu - lImVariazionePiu),17),
                                                   'SOCD');
           end if;

           if lCapitolo.variazioni_meno <> abs(lImVariazioneMeno) then
          IBMUTL200.logInf(pg_exec,
                                   'D_SALDI050 - VARIAZIONI_MENO - '||MSG_CHIAVE_CAPITOLO(lCapitolo),
                                   'VARIAZIONI_MENO :'|| LPAD(fnum((-1)*lCapitolo.variazioni_meno ),17)
                                    ||'TOTALE_VAR_MENO :'|| LPAD(fnum(lImVariazioneMeno ),17)
                            ||'Delta :'||LPAD(fnum((-1)*lCapitolo.variazioni_meno - lImVariazioneMeno),17),
                                                   'SOCD');
           end if;

           -- Controllo 2) in voce_f_saldi_cmp per
           --                           per cd_cds = 999
           --                           and capitolo parte uno (capitolo inizia per 1.%)
           --                           in competenza
           --                           Spesa
           --                           deve risultare che:
           --                    importo stanz_ini_a1 + varpiu - varmeno = importo unica obbligazione associata
           if lCapitolo.cd_cds =cnrctb020.getCDCDSENTE(aEs)
           and lCapitolo.esercizio = aEs
           and lCapitolo.cd_voce like '1.%'
           and lCapitolo.ti_competenza_residuo ='C'
           and lCapitolo.ti_gestione ='S'
           and lCapitolo.ti_appartenenza ='C'
           then
                   lImportoTotCap := 0;
                   lImportoTotCap := lCapitolo.im_stanz_iniziale_a1 + lCapitolo.variazioni_piu - lCapitolo.variazioni_meno;
                   lImportoScadVoce := getImObbligazione(pg_exec,lCapitolo);
                   if lImportoTotCap <> lImportoScadVoce then
                  IBMUTL200.logInf(pg_exec,
                                           'D_SALDI070 - (STANZ_A1+VARPIU-VARMENO) <> IM_SCADENZA - ' ||MSG_CHIAVE_CAPITOLO(lCapitolo),
                                           '(STANZ_A1+VARPIU-VARMENO):' || LPAD(fnum(lImportoTotCap ),17) ||' - IM_SCADENZA: ' ||LPAD(fnum(lImportoScadVoce),17) || ' - Delta :' ||LPAD(fnum(lImportoTotCap -lImportoScadVoce),17),
                                                           'SOCD');
                   end if;
           end if;

   end loop;

   aEndT:=sysdate;
   aEnd:=to_char(aEndT,'YYYYMMDD HH:MI:SS');
   aDelta:=to_char((aEndT-aStartT)*24*3600,'999999');
   IBMUTL200.logInf(pg_exec,'DIFFSALDI END at: '||aEnd||' tot exec time(s):'||aDelta||' es.'||aEs||' cds.'||aCdCds,aEs||aCdCds,'SOC');
   EXCEPTION
   WHEN OTHERS THEN
                ROLLBACK;
                IBMUTL200.logErr(pg_exec, SQLERRM(SQLCODE),DBMS_UTILITY.FORMAT_ERROR_STACK,'SOC');
--  EXCEPTION WHEN OTHERS THEN
--   ROLLBACK;
--   IBMUTL200.logErr(pg_exec, SQLERRM(SQLCODE),DBMS_UTILITY.FORMAT_ERROR_STACK,'SOC');
  END;
 end;

 Function SetVOCE_F_SALDI_CDR_LINEA(aSaldoDoc V_VOCE_F_SALDI_CDR_LINEA%Rowtype)
   Return VOCE_F_SALDI_CDR_LINEA%Rowtype Is
     aSaldo VOCE_F_SALDI_CDR_LINEA%Rowtype;
 Begin
    Insert INTO VOCE_F_SALDI_CDR_LINEA ( ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA,
    CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, IM_STANZ_INIZIALE_A1,
    IM_STANZ_INIZIALE_A2, IM_STANZ_INIZIALE_A3, VARIAZIONI_PIU, VARIAZIONI_MENO,
    IM_STANZ_INIZIALE_CASSA, VARIAZIONI_PIU_CASSA, VARIAZIONI_MENO_CASSA, IM_OBBL_ACC_COMP,
    IM_STANZ_RES_IMPROPRIO, VAR_PIU_STANZ_RES_IMP, VAR_MENO_STANZ_RES_IMP, IM_OBBL_RES_IMP,
    VAR_PIU_OBBL_RES_IMP, VAR_MENO_OBBL_RES_IMP, IM_OBBL_RES_PRO, VAR_PIU_OBBL_RES_PRO,
    VAR_MENO_OBBL_RES_PRO, IM_MANDATI_REVERSALI_PRO, IM_MANDATI_REVERSALI_IMP, IM_PAGAMENTI_INCASSI,
    DACR, UTCR, DUVA, UTUV, PG_VER_REC,CD_ELEMENTO_VOCE ) VALUES (
    aSaldoDoc.ESERCIZIO, aSaldoDoc.ESERCIZIO, aSaldoDoc.CD_CENTRO_RESPONSABILITA, aSaldoDoc.CD_LINEA_ATTIVITA, aSaldoDoc.TI_APPARTENENZA,
    aSaldoDoc.TI_GESTIONE, aSaldoDoc.CD_VOCE, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    Sysdate, 'SPASIA', Sysdate, 'SPASIA', 1, (SELECT cd_elemento_voce FROM VOCE_F
                                               WHERE VOCE_F.ESERCIZIO = aSaldoDoc.ESERCIZIO
                                                 AND VOCE_F.ti_appartenenza = aSaldoDoc.ti_appartenenza
                                                 AND VOCE_F.ti_gestione = aSaldoDoc.ti_gestione
                                                 And VOCE_F.cd_voce = aSaldoDoc.cd_voce));

    Select * Into aSaldo
    From VOCE_F_SALDI_CDR_LINEA
      Where esercizio = aSaldoDoc.esercizio
        And esercizio_res = aSaldoDoc.esercizio
        And cd_centro_responsabilita = aSaldoDoc.cd_centro_responsabilita
        And cd_linea_attivita = aSaldoDoc.cd_linea_attivita
        And ti_appartenenza = aSaldoDoc.ti_appartenenza
        And ti_gestione = aSaldoDoc.ti_gestione
        And cd_voce = aSaldoDoc.cd_voce
      For update nowait;
    Return aSaldo;
 End;

 Procedure job_mar_saldi01(job number, pg_exec number, next_date date, aEs number, aCdCDS varchar2, aCdUO varchar2, aCdCdr varchar2, aCd_voce VARCHAR2, aCd_linea_attivita VARCHAR2, isModifica char) is
  aStringa varchar2(4000);
  aTSNow date;
  aUser varchar2(20);
  aChekLock CHAR(1);
  aChekUpdate CHAR(1);
  aMan mandato%rowtype;
  aRev reversale%rowtype;
  aMsgTipoMar varchar2(100);
  aEndT date;
  aStartT date;
  aEnd varchar2(80);
  aStart varchar2(80);
  aDelta varchar2(80);
  aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype;
  lImporto number(15,2);
  lImportoTotCap number(15,2);
  lImportoScadVoce number(15,2);
  lImVariazionePiu number(15,2);
  lImVariazioneMeno number(15,2);
 Begin
  aTSNow:=sysdate;
  aUser:=IBMUTL200.getUserFromLog(pg_exec);
  if isModifica = 'Y' then
   aMsgTipoMar:='MODIFICA';
  else
   aMsgTipoMar:='VERIFICA';
  end if;
  aStartT:=sysdate;
  aStart:=to_char(sysdate,'YYYYMMDD HH:MI:SS');
  IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_MAR_SALDI, job, 'Batch di '||aMsgTipoMar||' dei saldi. Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));
  Begin
   IBMUTL200.logInf(pg_exec,'DIFFSALDI START  at: '||aStart||' es.'||aEs||' CDS.'||aCdcds||' UO.'||aCdUO||' cdr.'||aCdCdr||' Voce.'||aCd_voce||' Linea.'||aCd_linea_attivita,' ','SOC');
   For aStruttura in (
         Select cd_centro_responsabilita
         From V_STRUTTURA_ORGANIZZATIVA
         Where esercizio = aEs
           And CD_TIPO_LIVELLO = 'CDR'
           And cd_cds = Decode(aCdCds,'TUTTI',cd_cds,aCdCds)
           And cd_centro_responsabilita = Decode(aCdCdr,'TUTTI',cd_centro_responsabilita,aCdCdr)
           And cd_unita_organizzativa = Decode(aCdUO,'TUTTI',cd_unita_organizzativa,aCdUO)) Loop
    For aSaldoDoc in (
          Select A.*
          From V_VOCE_F_SALDI_CDR_LINEA A
          Where A.esercizio = aEs
            And A.cd_centro_responsabilita = aStruttura.cd_centro_responsabilita
            And A.cd_voce = Decode(aCd_voce,'TUTTI',A.cd_voce, aCd_voce)
            And A.cd_linea_attivita = Decode(aCd_linea_attivita,'TUTTI',A.cd_linea_attivita,aCd_linea_attivita)) Loop
     Begin
       Select * into aSaldo
       From VOCE_F_SALDI_CDR_LINEA
       Where esercizio = aSaldoDoc.esercizio
         And esercizio_res = aSaldoDoc.esercizio
         And cd_centro_responsabilita = aSaldoDoc.cd_centro_responsabilita
         And cd_linea_attivita = aSaldoDoc.cd_linea_attivita
         And ti_appartenenza = aSaldoDoc.ti_appartenenza
         And ti_gestione = aSaldoDoc.ti_gestione
         And cd_voce = aSaldoDoc.cd_voce
       For update nowait;
     Exception
       When No_Data_Found Then
         aSaldo := SetVOCE_F_SALDI_CDR_LINEA(aSaldoDoc);
     End;
     lImporto := aSaldoDoc.im_obbl_acc_comp;
     If lImporto - aSaldo.im_obbl_acc_comp != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set im_obbl_acc_comp = lImporto, utuv='MARTELLO', duva = sysdate
        Where esercizio = aSaldoDoc.esercizio
         And esercizio_res = aSaldoDoc.esercizio
         And cd_centro_responsabilita = aSaldoDoc.cd_centro_responsabilita
         And cd_linea_attivita = aSaldoDoc.cd_linea_attivita
         And ti_appartenenza = aSaldoDoc.ti_appartenenza
         And ti_gestione = aSaldoDoc.ti_gestione
         And cd_voce = aSaldoDoc.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec,MSG_DIFF_SALDI_CDR_LINEA('DIS - DISSALDI010 - im_obbl_acc_comp ',aSaldoDoc, aSaldo),MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',aSaldoDoc, aSaldo),'SOCD');
     End If;
     lImporto := aSaldoDoc.im_mandati_reversali_pro;
     If lImporto - aSaldo.im_mandati_reversali_pro != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set im_mandati_reversali_pro = lImporto, utuv='MARTELLO', duva = sysdate
        Where esercizio = aSaldoDoc.esercizio
          And esercizio_res = aSaldoDoc.esercizio
          And cd_centro_responsabilita = aSaldoDoc.cd_centro_responsabilita
          And cd_linea_attivita = aSaldoDoc.cd_linea_attivita
          And ti_appartenenza = aSaldoDoc.ti_appartenenza
          And ti_gestione = aSaldoDoc.ti_gestione
          And cd_voce = aSaldoDoc.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec,MSG_DIFF_SALDI_CDR_LINEA('DIS - DISSALDI020 - IM_MANDATI_REVERSALI_PRO ',aSaldoDoc, aSaldo),MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI020',aSaldoDoc, aSaldo),'SOCD');
     End If;
     lImporto := aSaldoDoc.im_pagamenti_incassi;
     If lImporto - aSaldo.im_pagamenti_incassi != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set im_pagamenti_incassi = lImporto, utuv='MARTELLO', duva = sysdate
        Where esercizio = aSaldoDoc.esercizio
          And esercizio_res = aSaldoDoc.esercizio
          And cd_centro_responsabilita = aSaldoDoc.cd_centro_responsabilita
          And cd_linea_attivita = aSaldoDoc.cd_linea_attivita
          And ti_appartenenza = aSaldoDoc.ti_appartenenza
          And ti_gestione = aSaldoDoc.ti_gestione
          And cd_voce = aSaldoDoc.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec,MSG_DIFF_SALDI_CDR_LINEA('DIS - DISSALDI030 - IM_PAGAMENTI_INCASSI ',aSaldoDoc, aSaldo),MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI030',aSaldoDoc, aSaldo),'SOCD');
     End If;
     lImporto := aSaldoDoc.stanziamenti;
     If lImporto - aSaldo.IM_STANZ_INIZIALE_A1 != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_STANZ_INIZIALE_A1 = lImporto, utuv='MARTELLO', duva = sysdate
        Where esercizio = aSaldoDoc.esercizio
          And esercizio_res = aSaldoDoc.esercizio
          And cd_centro_responsabilita = aSaldoDoc.cd_centro_responsabilita
          And cd_linea_attivita = aSaldoDoc.cd_linea_attivita
          And ti_appartenenza = aSaldoDoc.ti_appartenenza
          And ti_gestione = aSaldoDoc.ti_gestione
          And cd_voce = aSaldoDoc.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec,MSG_DIFF_SALDI_CDR_LINEA('DIS - DISSALDI030 - IM_STANZ_INIZIALE_A1 ',aSaldoDoc, aSaldo),MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI030',aSaldoDoc, aSaldo),'SOCD');
     End If;
     lImporto := aSaldoDoc.variazioni_piu;
     If lImporto - aSaldo.variazioni_piu != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set variazioni_piu = lImporto, utuv='MARTELLO', duva = sysdate
        Where esercizio = aSaldoDoc.esercizio
          And esercizio_res = aSaldoDoc.esercizio
          And cd_centro_responsabilita = aSaldoDoc.cd_centro_responsabilita
          And cd_linea_attivita = aSaldoDoc.cd_linea_attivita
          And ti_appartenenza = aSaldoDoc.ti_appartenenza
          And ti_gestione = aSaldoDoc.ti_gestione
          And cd_voce = aSaldoDoc.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec,MSG_DIFF_SALDI_CDR_LINEA('DIS - DISSALDI030 - VARIAZIONI_PIU ',aSaldoDoc, aSaldo),MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI030',aSaldoDoc, aSaldo),'SOCD');
     End If;
     lImporto := aSaldoDoc.variazioni_meno;
     If lImporto - aSaldo.variazioni_meno != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set variazioni_meno = lImporto, utuv='MARTELLO', duva = sysdate
        Where esercizio = aSaldoDoc.esercizio
          And esercizio_res = aSaldoDoc.esercizio
          And cd_centro_responsabilita = aSaldoDoc.cd_centro_responsabilita
          And cd_linea_attivita = aSaldoDoc.cd_linea_attivita
          And ti_appartenenza = aSaldoDoc.ti_appartenenza
          And ti_gestione = aSaldoDoc.ti_gestione
          And cd_voce = aSaldoDoc.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec,MSG_DIFF_SALDI_CDR_LINEA('DIS - DISSALDI030 - VARIAZIONI_MENO ',aSaldoDoc, aSaldo),MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI030',aSaldoDoc, aSaldo),'SOCD');
     End If;
    End Loop;
    For aSaldoDelete in (
          Select ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE
          From VOCE_F_SALDI_CDR_LINEA
          Where esercizio = aEs
            And esercizio = esercizio_res
            And cd_centro_responsabilita = aStruttura.cd_centro_responsabilita
            And cd_voce = Decode(aCd_voce,'TUTTI',cd_voce, aCd_voce)
            And cd_linea_attivita = Decode(aCd_linea_attivita,'TUTTI',cd_linea_attivita,aCd_linea_attivita)
            And (im_obbl_acc_comp != 0 Or im_mandati_reversali_pro != 0 Or im_pagamenti_incassi != 0 Or
                 IM_STANZ_INIZIALE_A1 != 0 Or variazioni_piu != 0 Or variazioni_meno != 0)
            And Not Exists
            (Select 1
             From v_voce_f_saldi_cdr_linea  a
             Where a.ESERCIZIO = VOCE_F_SALDI_CDR_LINEA.ESERCIZIO
	       And a.cd_centro_responsabilita = VOCE_F_SALDI_CDR_LINEA.cd_centro_responsabilita
	       And a.cd_linea_attivita =VOCE_F_SALDI_CDR_LINEA.cd_linea_attivita
               And a.ti_appartenenza = VOCE_F_SALDI_CDR_LINEA.ti_appartenenza
	       And a.ti_gestione = VOCE_F_SALDI_CDR_LINEA.ti_gestione
	       And a. cd_voce = VOCE_F_SALDI_CDR_LINEA. cd_voce)) Loop
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set im_obbl_acc_comp = 0, im_mandati_reversali_pro = 0, im_pagamenti_incassi = 0,
            IM_STANZ_INIZIALE_A1 = 0, variazioni_piu = 0, variazioni_meno = 0
        Where esercizio = aSaldoDelete.esercizio
          And esercizio_res = aSaldoDelete.esercizio
          And cd_centro_responsabilita = aSaldoDelete.cd_centro_responsabilita
          And cd_linea_attivita = aSaldoDelete.cd_linea_attivita
          And ti_appartenenza = aSaldoDelete.ti_appartenenza
          And ti_gestione = aSaldoDelete.ti_gestione
          And cd_voce = aSaldoDelete.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec,'Saldo - Cap:'||RPAD(aSaldoDelete.cd_voce,26)||' '||
                               'Cdr:'||RPAD(aSaldoDelete.cd_centro_responsabilita,30)||' '||
                               'Linea:'||RPAD(aSaldoDelete.cd_linea_attivita,10)||' '||
                               'E/S:'||aSaldoDelete.ti_gestione,'Riga di saldo erroneamente presente, annullati gli importi di competenza','SOCD');
    End Loop;
    IBMUTL200.logInf(pg_exec,'CDR:'||aStruttura.cd_centro_responsabilita||' terminato.',' ','SOCD');
    Commit;
   End Loop;
   aEndT:=sysdate;
   aEnd:=to_char(aEndT,'YYYYMMDD HH:MI:SS');
   aDelta:=to_char((aEndT-aStartT)*24*3600,'999999');
   IBMUTL200.logInf(pg_exec,'DIFFSALDI END at: '||aEnd||' tot exec time(s):'||aDelta||' es.'||aEs||' UO.'||aCdUO||' cdr.'||aCdCdr,' ','SOC');
  Exception
   When Others Then
     Rollback;
     IBMUTL200.logErr(pg_exec, SQLERRM(SQLCODE),DBMS_UTILITY.FORMAT_ERROR_STACK,'SOC');
  End;
 End;

/* */

Procedure job_mar_saldi02(job number, pg_exec number, next_date date, aEs number, aCdCDS varchar2, aCdUO varchar2, aCdCdr varchar2, aCd_voce VARCHAR2, aCd_linea_attivita VARCHAR2, isModifica char) is
  aStringa varchar2(4000);
  aTSNow date;
  aUser varchar2(20);
  aChekLock CHAR(1);
  aChekUpdate CHAR(1);
  aMan mandato%rowtype;
  aRev reversale%rowtype;
  aMsgTipoMar varchar2(100);
  aEndT date;
  aStartT date;
  aEnd varchar2(80);
  aStart varchar2(80);
  aDelta varchar2(80);
  aSaldo VOCE_F_SALDI_CDR_LINEA%rowtype;
  lImporto number(15,2);
  lImportoTotCap number(15,2);
  lImportoScadVoce number(15,2);
  lImVariazionePiu number(15,2);
  lImVariazioneMeno number(15,2);
  aSaldi  VOCE_F_SALDI_CDR_LINEA%Rowtype;
  disallineamenti number:=0;
Begin
  aTSNow:=sysdate;
  aUser:=IBMUTL200.getUserFromLog(pg_exec);
  if isModifica = 'Y' then
   aMsgTipoMar:='MODIFICA';
  else
   aMsgTipoMar:='VERIFICA';
  end if;

  aStartT:=sysdate;
  aStart:=to_char(sysdate,'YYYYMMDD HH:MI:SS');
  IBMUTL210.logStartExecutionUpd(pg_exec, TIPO_LOG_MAR_SALDI, job, 'Batch di '||aMsgTipoMar||' dei saldi. Start:'||to_char(aTSNow,'YYYY/MM/DD HH-MI-SS'));

Begin

IBMUTL200.logInf(pg_exec,'DIFFSALDI START  at: '||aStart||' es.'||aEs||' CDS.'||aCdcds||' UO.'||aCdUO||' cdr.'||aCdCdr||' Voce.'||aCd_voce||' Linea.'||aCd_linea_attivita,' ','SOC');

INSERT_SALDI_VUOTI_UTILIZZATI (pg_exec, aEs);

If aEs >= 2006 Then

For aStruttura in (Select cd_centro_responsabilita
                   From V_STRUTTURA_ORGANIZZATIVA
                   Where esercizio = aEs
                     And CD_TIPO_LIVELLO = 'CDR'
                     And cd_cds = Decode(aCdCds,'TUTTI',cd_cds,aCdCds)
                     And cd_centro_responsabilita = Decode(aCdCdr,'TUTTI',cd_centro_responsabilita,aCdCdr)
                     And cd_unita_organizzativa = Decode(aCdUO,'TUTTI',cd_unita_organizzativa,aCdUO)) Loop

For CHIAVE_Saldi in (Select ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE
                     From  VOCE_F_SALDI_CDR_LINEA
                     Where esercizio = aEs
                       And cd_centro_responsabilita = aStruttura.cd_centro_responsabilita
                       And cd_voce = Decode(aCd_voce,'TUTTI',cd_voce, aCd_voce)
                       And cd_linea_attivita = Decode(aCd_linea_attivita,'TUTTI',cd_linea_attivita,aCd_linea_attivita)) Loop

-- IM_STANZ_INIZIALE_A1  ***  SOLO PER COMPETENZA !!! ***
--                       *** SIA ENTRATA CHE SPESA ***

If isModifica = 'Y' then
  Select *
  Into   aSaldi
  From   VOCE_F_SALDI_CDR_LINEA
  Where  ESERCIZIO                 = CHIAVE_Saldi.ESERCIZIO                and
         ESERCIZIO_RES             = CHIAVE_Saldi.ESERCIZIO_RES            and
         CD_CENTRO_RESPONSABILITA  = CHIAVE_Saldi.CD_CENTRO_RESPONSABILITA and
         CD_LINEA_ATTIVITA         = CHIAVE_Saldi.CD_LINEA_ATTIVITA        and
         TI_APPARTENENZA           = CHIAVE_Saldi.TI_APPARTENENZA          and
         TI_GESTIONE               = CHIAVE_Saldi.TI_GESTIONE              and
         CD_VOCE                   = CHIAVE_Saldi.CD_VOCE
  For Update;
Else
  Select *
  Into   aSaldi
  From   VOCE_F_SALDI_CDR_LINEA
  Where  ESERCIZIO                 = CHIAVE_Saldi.ESERCIZIO                and
         ESERCIZIO_RES             = CHIAVE_Saldi.ESERCIZIO_RES            and
         CD_CENTRO_RESPONSABILITA  = CHIAVE_Saldi.CD_CENTRO_RESPONSABILITA and
         CD_LINEA_ATTIVITA         = CHIAVE_Saldi.CD_LINEA_ATTIVITA        and
         TI_APPARTENENZA           = CHIAVE_Saldi.TI_APPARTENENZA          and
         TI_GESTIONE               = CHIAVE_Saldi.TI_GESTIONE              and
         CD_VOCE                   = CHIAVE_Saldi.CD_VOCE;
End If;

-- STANZIAMENTO INIZIALE

If aSaldi.ESERCIZIO = aSaldi.ESERCIZIO_RES And aSaldi.CD_CENTRO_RESPONSABILITA != cnrctb020.getCdCdrEnte Then
  lImporto := CNRUTL002.IM_STANZ_INIZIALE_A1 (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                      aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                      aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);

  If aSaldi.IM_STANZ_INIZIALE_A1 != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_STANZ_INIZIALE_A1 = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_STANZ_INIZIALE_A1('DIS - DISSALDI010 - IM_STANZ_INIZIALE_A1 ', fnum(lImporto), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
  End If;

-- PER I RESIDUI E' POPOLATA PER ERRORE LA COLONNA STANZIAMENTO INIZIALE COMPETENZA
Elsif aSaldi.ESERCIZIO != aSaldi.ESERCIZIO_RES And aSaldi.IM_STANZ_INIZIALE_A1 != 0 Then
    If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_STANZ_INIZIALE_A1 = 0,
            utuv ='MARTELLO06',
            duva = sysdate
       Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
    End If;
    IBMUTL200.logInf(pg_exec, MSG_D_S_IM_STANZ_INIZIALE_A1('DIS - DISSALDI010 - IM_STANZ_INIZIALE_A1 (INCONGRUO) ', fnum(0), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
End If;


/* AGGIUNTO IL 15 MAGGIO 2006 */

-- VARIAZIONI IN PIU AL PDG COMPETENZA >= 2006

If aSaldi.ESERCIZIO = aSaldi.ESERCIZIO_RES Then
   lImporto := CNRUTL002.VARIAZIONI_PIU (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                         aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                         aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);

   If aSaldi.VARIAZIONI_PIU != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set VARIAZIONI_PIU = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
   IBMUTL200.logInf(pg_exec, MSG_D_S_VARIAZIONI_PIU('DIS - DISSALDI010 - VARIAZIONI_PIU ',  fnum(lImporto), aSaldi), MSG_D_S_VARIAZIONI_PIU('DISSALDI010',Null, aSaldi),'SOCD');
   End If;

-- PER I RESIDUI E' POPOLATA PER ERRORE LA COLONNA VARIAZIONI PIU' COMPETENZA
Elsif aSaldi.ESERCIZIO != aSaldi.ESERCIZIO_RES And aSaldi.VARIAZIONI_PIU != 0 Then
     If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set VARIAZIONI_PIU = 0,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
     End If;
   IBMUTL200.logInf(pg_exec, MSG_D_S_VARIAZIONI_PIU('DIS - DISSALDI010 - VARIAZIONI_PIU (INCONGRUO) ',  fnum(0), aSaldi), MSG_D_S_VARIAZIONI_PIU('DISSALDI010',Null, aSaldi),'SOCD');
End If;

-- VARIAZIONI IN MENO AL PDG COMPETENZA >= 2006

If aSaldi.ESERCIZIO = aSaldi.ESERCIZIO_RES Then
   lImporto := Abs(CNRUTL002.VARIAZIONI_MENO (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                              aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                              aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE));

   If aSaldi.VARIAZIONI_MENO != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set VARIAZIONI_MENO = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
   IBMUTL200.logInf(pg_exec, MSG_D_S_VARIAZIONI_MENO('DIS - DISSALDI010 - VARIAZIONI_MENO ',
                fnum(lImporto), aSaldi), MSG_D_S_VARIAZIONI_MENO('DISSALDI010',Null, aSaldi),'SOCD');
   End If;

-- PER I RESIDUI E' POPOLATA PER ERRORE LA COLONNA VARIAZIONI MENO COMPETENZA
Elsif aSaldi.ESERCIZIO != aSaldi.ESERCIZIO_RES And aSaldi.VARIAZIONI_MENO != 0 Then
   If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set VARIAZIONI_MENO = 0,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
   End If;
   IBMUTL200.logInf(pg_exec, MSG_D_S_VARIAZIONI_MENO('DIS - DISSALDI010 - VARIAZIONI_MENO (INCONGRUO) ',
                    fnum(0), aSaldi), MSG_D_S_VARIAZIONI_MENO('DISSALDI010',Null, aSaldi),'SOCD');
End If;

/* FINE AGGIUNTO IL 15 MAGGIO 2006 */

-- IM_OBBL_ACC_COMP   ***  SOLO PER COMPETENZA !!! ***
--                       *** SIA ENTRATA CHE SPESA ***

If aSaldi.ESERCIZIO = aSaldi.ESERCIZIO_RES Then
   lImporto := CNRUTL002.IM_OBBL_ACC_COMP     (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                       aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                       aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);

   If aSaldi.IM_OBBL_ACC_COMP != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_OBBL_ACC_COMP = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_OBBL_ACC_COMP('DIS - DISSALDI010 - IM_OBBL_ACC_COMP ', fnum(lImporto), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
   End If;

-- PER I RESIDUI E' POPOLATA PER ERRORE LA COLONNA IMPEGNATO/ACCERTATO A COMPETENZA
Elsif aSaldi.ESERCIZIO != aSaldi.ESERCIZIO_RES And aSaldi.IM_OBBL_ACC_COMP != 0 Then
   If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_OBBL_ACC_COMP = 0,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
    End If;
    IBMUTL200.logInf(pg_exec, MSG_D_S_IM_OBBL_ACC_COMP('DIS - DISSALDI010 - IM_OBBL_ACC_COMP (INCONGRUO) ', fnum(0), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
End If;

-- IM_STANZ_RES_IMPROPRIO   ***  SOLO PER RESIDUI !!! ***
--                          *** SOLO PER SPESA ***

If aSaldi.ESERCIZIO > aSaldi.ESERCIZIO_RES And aSaldi.TI_GESTIONE = 'S' And aSaldi.cd_centro_responsabilita != cnrctb020.getCdCdrEnte  Then

   lImporto := CNRUTL002.IM_STANZ_RES_IMPROPRIO (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                         aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                         aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);
  If aSaldi.IM_STANZ_RES_IMPROPRIO != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_STANZ_RES_IMPROPRIO = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_STANZ_RES_IMPROPRIO('DIS - DISSALDI010 - IM_STANZ_RES_IMPROPRIO ', fnum(lImporto), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
  End If;

Else
-- SE E' POPOLATA LA COLONNA IM_STANZ_RES_IMPROPRIO IN CASO DI:
   -- COMPETENZA,
   -- ENTRATA,
   -- CDR 999.000.000 ALLORA TRATTASI DI ERRORE E QUINDI LO AZZERO
   If aSALDI.IM_STANZ_RES_IMPROPRIO != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_STANZ_RES_IMPROPRIO = 0,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_STANZ_RES_IMPROPRIO('DIS - DISSALDI010 - IM_STANZ_RES_IMPROPRIO (INCONGRUO) ', fnum(0), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
   End If;
End If;

-- VAR_PIU_STANZ_RES_IMP     ***  SOLO PER RESIDUI !!! ***
--                           *** SOLO PER SPESA ***

If aSaldi.ESERCIZIO > aSaldi.ESERCIZIO_RES And aSaldi.TI_GESTIONE = 'S' And aSaldi.cd_centro_responsabilita != cnrctb020.getCdCdrEnte Then
   lImporto := CNRUTL002.VAR_PIU_STANZ_RES_IMP  (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                         aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                         aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);

   If aSaldi.VAR_PIU_STANZ_RES_IMP != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set VAR_PIU_STANZ_RES_IMP = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_VAR_PIU_STANZ_RES_IMP('DIS - DISSALDI010 - VAR_PIU_STANZ_RES_IMP ', fnum(lImporto), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
   End If;

Else
-- SE E' POPOLATA LA COLONNA VAR_PIU_STANZ_RES_IMP IN CASO DI:
   -- COMPETENZA,
   -- ENTRATA,
   -- CDR 999.000.000 ALLORA TRATTASI DI ERRORE E QUINDI LO AZZERO
   If aSALDI.VAR_PIU_STANZ_RES_IMP != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set VAR_PIU_STANZ_RES_IMP = 0,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_VAR_PIU_STANZ_RES_IMP('DIS - DISSALDI010 - VAR_PIU_STANZ_RES_IMP (INCONGRUO) ', fnum(0), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
   End If;
End If;

-- VAR_MENO_STANZ_RES_IMP  ***  SOLO PER RESIDUI !!! ***
--                          *** SOLO PER SPESA ***

If aSaldi.ESERCIZIO > aSaldi.ESERCIZIO_RES And aSaldi.TI_GESTIONE = 'S' And aSaldi.cd_centro_responsabilita != cnrctb020.getCdCdrEnte Then
   lImporto := CNRUTL002.VAR_MENO_STANZ_RES_IMP (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                         aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                         aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);

  If aSaldi.VAR_MENO_STANZ_RES_IMP != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set VAR_MENO_STANZ_RES_IMP = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_VAR_MENO_STANZ_RES_IMP('DIS - DISSALDI010 - VAR_MENO_STANZ_RES_IMP ', fnum(lImporto), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
  End If;

Else
-- SE E' POPOLATA LA COLONNA VAR_MENO_STANZ_RES_IMP IN CASO DI:
   -- COMPETENZA,
   -- ENTRATA,
   -- CDR 999.000.000 ALLORA TRATTASI DI ERRORE E QUINDI LO AZZERO
  If aSaldi.VAR_MENO_STANZ_RES_IMP != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set VAR_MENO_STANZ_RES_IMP = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
  IBMUTL200.logInf(pg_exec, MSG_D_S_VAR_MENO_STANZ_RES_IMP('DIS - DISSALDI010 - VAR_MENO_STANZ_RES_IMP ', fnum(lImporto), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
  End If;

End If;

--  IM_OBBL_RES_IMP      ***  SOLO PER RESIDUI !!! ***
--                       *** SOLO PER SPESA ***

If aSaldi.ESERCIZIO > aSaldi.ESERCIZIO_RES And aSaldi.TI_GESTIONE = 'S' And aSaldi.cd_centro_responsabilita != cnrctb020.getCdCdrEnte Then
     lImporto := CNRUTL002.IM_OBBL_RES_IMP (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                    aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                    aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);

   If aSaldi.IM_OBBL_RES_IMP != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_OBBL_RES_IMP = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_OBBL_RES_IMP('DIS - DISSALDI010 - IM_OBBL_RES_IMP ', fnum(lImporto), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
   End If;

Else
  -- SE E' POPOLATA LA COLONNA IM_OBBL_RES_IMP IN CASO DI:
   -- COMPETENZA,
   -- ENTRATA,
   -- CDR 999.000.000 ALLORA TRATTASI DI ERRORE E QUINDI LO AZZERO

   If aSaldi.IM_OBBL_RES_IMP != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_OBBL_RES_IMP = 0,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_OBBL_RES_IMP('DIS - DISSALDI010 - IM_OBBL_RES_IMP (INCONGRUO) ', fnum(0), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
   End If;

End If;

-- IM_OBBL_RES_PRO  ***  SOLO PER RESIDUI !!! ***
--                   *** SIA ENTRATA CHE SPESA ***

If aSaldi.ESERCIZIO > aSaldi.ESERCIZIO_RES Then
    lImporto := CNRUTL002.IM_OBBL_RES_PRO (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                   aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                   aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);

   If aSaldi.IM_OBBL_RES_PRO != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_OBBL_RES_PRO = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_OBBL_RES_PRO('DIS - DISSALDI010 - IM_OBBL_RES_PRO ', fnum(lImporto), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
   End If;

Else
  -- SE E' POPOLATA LA COLONNA IM_OBBL_RES_PRO IN CASO DI COMPETENZA LA AZZERO
   If aSaldi.IM_OBBL_RES_PRO != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_OBBL_RES_PRO = 0,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_OBBL_RES_PRO('DIS - DISSALDI010 - IM_OBBL_RES_PRO (INCONGRUO) ', fnum(0), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
   End If;

End If;

-- VAR_PIU_OBBL_RES_PRO

/* INIZIO MODIFICA 08.08.2006 + 21.08.2006 */

-- VARIAZIONI IN PIU A (SOLO RESIDUI):
   -- IMPEGNI RESIDUI ENTE (APPROVAZIONE VARIAZIONI AL BILANCIO DI SERVIZIO)
      -- N.B !!!! IL VALORE PERO' LO PRENDE DALLE VARIAZIONI A COMPETENZA
   -- IMPEGNI RESIDUI PROPRI CDR (NUOVE FUNZIONALITA')

If aSaldi.ESERCIZIO > aSaldi.ESERCIZIO_RES Then

--  CDR ENTE: 999.000.000

  If aSaldi.CD_CENTRO_RESPONSABILITA =  cnrctb020.getCdCdrEnte Then -- NON MI SEMBRA CHE CI SIA UNA GET COL CODICE DEL CDR
    lImporto := CNRUTL002.VARIAZIONI_PIU (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                          aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                          aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);
    If aSaldi.VAR_PIU_OBBL_RES_PRO != lImporto Then
          If isModifica = 'Y' Then
            Update VOCE_F_SALDI_CDR_LINEA
            Set VAR_PIU_OBBL_RES_PRO = lImporto,
                utuv ='MARTELLO06',
                duva = sysdate
            Where esercizio = aSaldi.esercizio
             And esercizio_res = aSaldi.esercizio_res
             And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
             And cd_linea_attivita = aSaldi.cd_linea_attivita
             And ti_appartenenza = aSaldi.ti_appartenenza
             And ti_gestione = aSaldi.ti_gestione
             And cd_voce = aSaldi.cd_voce;
          End If;
       IBMUTL200.logInf(pg_exec, MSG_D_S_VAR_PIU_OBBL_RES_PRO('DIS - DISSALDI010 - VAR_PIU_OBBL_RES_PRO ',  fnum(lImporto), aSaldi), MSG_D_S_VAR_PIU_OBBL_RES_PRO('DISSALDI010',Null, aSaldi),'SOCD');
    End If;

  Elsif aSaldi.CD_CENTRO_RESPONSABILITA != cnrctb020.getCdCdrEnte Then

-- ISTITUTI != 999.000.000

    lImporto := CNRUTL002.VAR_PIU_OBBL_RES_PRO (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);
    If aSaldi.VAR_PIU_OBBL_RES_PRO != lImporto Then
          If isModifica = 'Y' Then
            Update VOCE_F_SALDI_CDR_LINEA
            Set VAR_PIU_OBBL_RES_PRO = lImporto,
                utuv ='MARTELLO06',
                duva = sysdate
            Where esercizio = aSaldi.esercizio
             And esercizio_res = aSaldi.esercizio_res
             And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
             And cd_linea_attivita = aSaldi.cd_linea_attivita
             And ti_appartenenza = aSaldi.ti_appartenenza
             And ti_gestione = aSaldi.ti_gestione
             And cd_voce = aSaldi.cd_voce;
          End If;
       IBMUTL200.logInf(pg_exec, MSG_D_S_VAR_PIU_OBBL_RES_PRO('DIS - DISSALDI010 - VAR_PIU_OBBL_RES_PRO ',  fnum(lImporto), aSaldi), MSG_D_S_VAR_PIU_OBBL_RES_PRO('DISSALDI010',Null, aSaldi),'SOCD');
    End If;
  End If;

Else
  -- SE E' POPOLATA LA COLONNA VAR_PIU_OBBL_RES_PRO IN CASO DI COMPETENZA LA AZZERO
    If aSaldi.VAR_PIU_OBBL_RES_PRO != 0 Then
          If isModifica = 'Y' Then
            Update VOCE_F_SALDI_CDR_LINEA
            Set VAR_PIU_OBBL_RES_PRO = 0,
                utuv ='MARTELLO06',
                duva = sysdate
            Where esercizio = aSaldi.esercizio
             And esercizio_res = aSaldi.esercizio_res
             And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
             And cd_linea_attivita = aSaldi.cd_linea_attivita
             And ti_appartenenza = aSaldi.ti_appartenenza
             And ti_gestione = aSaldi.ti_gestione
             And cd_voce = aSaldi.cd_voce;
          End If;
    IBMUTL200.logInf(pg_exec, MSG_D_S_VAR_PIU_OBBL_RES_PRO('DIS - DISSALDI010 - VAR_PIU_OBBL_RES_PRO (INCONGRUO) ',  fnum(0), aSaldi), MSG_D_S_VAR_PIU_OBBL_RES_PRO('DISSALDI010',Null, aSaldi),'SOCD');
    End If;

End If;


-- VARIAZIONI IN PIU AGLI IMPEGNI RESIDUI ENTE (SOLO RESIDUI)
-- PER ORA SOLO I RESIDUI ENTE (APPROVAZIONE VARIAZIONI AL BILANCIO DI SERVIZIO)
-- N.B !!!! IL VALORE PERO' LO PRENDE DALLE VARIAZIONI A COMPETENZA

If aSaldi.ESERCIZIO > aSaldi.ESERCIZIO_RES Then

-- CDR ENTE 999.000.000

  If aSaldi.CD_CENTRO_RESPONSABILITA = cnrctb020.getCdCdrEnte  Then -- NON MI SEMBRA CHE CI SIA UNA GET COL CODICE DEL CDR
      lImporto := Abs(CNRUTL002.VARIAZIONI_MENO (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                 aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                 aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE));

        If aSaldi.VAR_MENO_OBBL_RES_PRO != lImporto Then
              If isModifica = 'Y' Then
                Update VOCE_F_SALDI_CDR_LINEA
                Set VAR_MENO_OBBL_RES_PRO = lImporto,
                    utuv ='MARTELLO06',
                    duva = sysdate
                Where esercizio = aSaldi.esercizio
                 And esercizio_res = aSaldi.esercizio_res
                 And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
                 And cd_linea_attivita = aSaldi.cd_linea_attivita
                 And ti_appartenenza = aSaldi.ti_appartenenza
                 And ti_gestione = aSaldi.ti_gestione
                 And cd_voce = aSaldi.cd_voce;
              End If;
           IBMUTL200.logInf(pg_exec, MSG_D_S_VAR_MENO_OBBL_RES_PRO('1. DIS - DISSALDI010 - VAR_MENO_OBBL_RES_PRO ',  fnum(lImporto), aSaldi), MSG_D_S_VAR_MENO_OBBL_RES_PRO('DISSALDI010',Null, aSaldi),'SOCD');
        End If;

  Elsif aSaldi.CD_CENTRO_RESPONSABILITA != cnrctb020.getCdCdrEnte  Then
-- CDR ISTITUTO

    lImporto := CNRUTL002.VAR_MENO_OBBL_RES_PRO (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                 aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                 aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);
    If aSaldi.VAR_MENO_OBBL_RES_PRO != lImporto Then
          If isModifica = 'Y' Then
            Update VOCE_F_SALDI_CDR_LINEA
            Set VAR_MENO_OBBL_RES_PRO = lImporto,
                utuv ='MARTELLO06',
                duva = sysdate
            Where esercizio = aSaldi.esercizio
             And esercizio_res = aSaldi.esercizio_res
             And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
             And cd_linea_attivita = aSaldi.cd_linea_attivita
             And ti_appartenenza = aSaldi.ti_appartenenza
             And ti_gestione = aSaldi.ti_gestione
             And cd_voce = aSaldi.cd_voce;
          End If;
       IBMUTL200.logInf(pg_exec, MSG_D_S_VAR_MENO_OBBL_RES_PRO('DIS - DISSALDI010 - VAR_MENO_OBBL_RES_PRO ',  fnum(lImporto), aSaldi), MSG_D_S_VAR_PIU_OBBL_RES_PRO('DISSALDI010',Null, aSaldi),'SOCD');
    End If;
  End If;

Else
    -- SE E' POPOLATA LA COLONNA VAR_MENO_OBBL_RES_PRO IN CASO DI COMPETENZA LA AZZERO
    If aSaldi.VAR_MENO_OBBL_RES_PRO != 0 Then
          If isModifica = 'Y' Then
            Update VOCE_F_SALDI_CDR_LINEA
            Set VAR_MENO_OBBL_RES_PRO = 0,
                utuv ='MARTELLO06',
                duva = sysdate
            Where esercizio = aSaldi.esercizio
             And esercizio_res = aSaldi.esercizio_res
             And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
             And cd_linea_attivita = aSaldi.cd_linea_attivita
             And ti_appartenenza = aSaldi.ti_appartenenza
             And ti_gestione = aSaldi.ti_gestione
             And cd_voce = aSaldi.cd_voce;
          End If;
       IBMUTL200.logInf(pg_exec, MSG_D_S_VAR_MENO_OBBL_RES_PRO('DIS - DISSALDI010 - VAR_MENO_OBBL_RES_PRO (INCONGRUO) ',
                        fnum(0), aSaldi), MSG_D_S_VAR_PIU_OBBL_RES_PRO('DISSALDI010',Null, aSaldi),'SOCD');
    End If;

End If;


/* FINE MODIFICA 08.08.2006 */


-- IM_MANDATI_REVERSALI_PRO   ***  SIA PER COMPETENZA CHE PER RESIDUI !!! ***
--                              *** SIA ENTRATA CHE SPESA ***

lImporto := CNRUTL002.IM_MANDATI_REVERSALI_PRO (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                      aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                      aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);

If aSaldi.IM_MANDATI_REVERSALI_PRO != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_MANDATI_REVERSALI_PRO = lImporto,
            utuv ='MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_MANDATI_REV_PRO('DIS - DISSALDI010 - IM_MANDATI_REVERSALI_PRO ', fnum(lImporto), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
End If;

-- IM_MANDATI_REVERSALI_IMP   ***  SOLO PER RESIDUI !!! ***
--                             *** SOLO PER SPESA ***

If aSaldi.ESERCIZIO > aSaldi.ESERCIZIO_RES And aSaldi.TI_GESTIONE = 'S' Then
    lImporto := CNRUTL002.IM_MANDATI_REVERSALI_IMP (aSaldi.ESERCIZIO, aSaldi.ESERCIZIO_RES, aSaldi.CD_CENTRO_RESPONSABILITA,
                                                            aSaldi.CD_LINEA_ATTIVITA, aSaldi.TI_APPARTENENZA, aSaldi.TI_GESTIONE,
                                                            aSaldi.CD_VOCE, aSaldi.CD_ELEMENTO_VOCE);

   If aSaldi.IM_MANDATI_REVERSALI_IMP != lImporto Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_MANDATI_REVERSALI_IMP = lImporto,
            utuv = 'MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_MANDATI_REV_IMP('DIS - DISSALDI010 - IM_MANDATI_REVERSALI_IMP ', fnum(lImporto), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
   End If;

Else
  -- SE LA COLONNA IM_MANDATI_REVERSALI_IMP E' POPOLATA IN CASO DI COMPETENZA O DI ENTRATA ALLORA LA AZZERO
   If aSaldi.IM_MANDATI_REVERSALI_IMP != 0 Then
      If isModifica = 'Y' Then
        Update VOCE_F_SALDI_CDR_LINEA
        Set IM_MANDATI_REVERSALI_IMP = 0,
            utuv = 'MARTELLO06',
            duva = sysdate
        Where esercizio = aSaldi.esercizio
         And esercizio_res = aSaldi.esercizio_res
         And cd_centro_responsabilita = aSaldi.cd_centro_responsabilita
         And cd_linea_attivita = aSaldi.cd_linea_attivita
         And ti_appartenenza = aSaldi.ti_appartenenza
         And ti_gestione = aSaldi.ti_gestione
         And cd_voce = aSaldi.cd_voce;
      End If;
      IBMUTL200.logInf(pg_exec, MSG_D_S_IM_MANDATI_REV_IMP('DIS - DISSALDI010 - IM_MANDATI_REVERSALI_IMP (INCONGRUO) ', fnum(0), aSaldi), MSGCSV_DIFF_SALDI_CDR_LINEA('DISSALDI010',Null, aSaldi),'SOCD');
   End If;

End If;

End Loop;

IBMUTL200.logInf(pg_exec,'CDR:'||aStruttura.cd_centro_responsabilita||' terminato.',' ','SOCD');
Commit;

End Loop;

  select count(0) into disallineamenti from batch_log_riga where
  pg_esecuzione = pg_exec and
  messaggio like 'DIS -%';

  if (disallineamenti!=0 ) then
    spedisci_mail('sigla@cnr.it','sigla@cnr.it','Disallineamenti saldi','Progressivo esecuzione '||pg_exec);
    --spedisci_mail('sigla@cnr.it','claudia.rosati@cnr.it','Disallineamento saldi','Progressivo esecuzione '||pg_exec);
  end if;

aEndT:=sysdate;
aEnd:=to_char(aEndT,'YYYYMMDD HH:MI:SS');
aDelta:=to_char((aEndT-aStartT)*24*3600,'999999');
IBMUTL200.logInf(pg_exec,'DIFFSALDI END at: '||aEnd||' tot exec time(s):'||aDelta||' es.'||aEs||' UO.'||aCdUO||' cdr.'||aCdCdr,' ','SOC');

Else
IBMUTL200.logInf(pg_exec,'NON EFFETTUATA, ESERCIZIO INFERIORE AL 2006 '||aEs, ' ', 'SOC');
End If;

Exception
   When Others Then
     Rollback;
     IBMUTL200.logErr(pg_exec, SQLERRM(SQLCODE),DBMS_UTILITY.FORMAT_ERROR_STACK,' ', 'SOC');
End;

End;

/* */

Procedure INSERT_SALDI_VUOTI_UTILIZZATI (aPg_exec NUMBER, INES NUMBER) Is
	motivo VARCHAR2(100);
 recParametriCNR PARAMETRI_CNR%Rowtype;
begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(INES);

For saldi_mancanti In
(Select Distinct TIPO, ESERCIZIO, ESERCIZIO_RES, CDR, CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE
From
(Select 'GS' TIPO, G.ESERCIZIO, G.ESERCIZIO ESERCIZIO_RES, G.CD_CDR_ASSEGNATARIO CDR, G.CD_LINEA_ATTIVITA, G.TI_APPARTENENZA, G.TI_GESTIONE,
       CNRCTB053.getVoce_FdaEV (G.Esercizio, G.TI_APPARTENENZA, G.TI_GESTIONE, G.CD_ELEMENTO_VOCE, G.CD_CDR_ASSEGNATARIO, G.cd_linea_attivita) CD_VOCE
From   PDG_MODULO_SPESE_GEST G, PDG_ESERCIZIO E
Where  G.ESERCIZIO = INES AND
       G.ESERCIZIO = E.ESERCIZIO And
       G.CD_CENTRO_RESPONSABILITA = E.CD_CENTRO_RESPONSABILITA And
       E.STATO = CNRCTB050.STATO_PDG2_CHIUSO_GEST And
       G.CATEGORIA_DETTAGLIO != 'SCR'
Union All
Select 'GE', G.ESERCIZIO, G.ESERCIZIO,G.CD_CDR_ASSEGNATARIO, G.CD_LINEA_ATTIVITA, G.TI_APPARTENENZA, G.TI_GESTIONE,
       CNRCTB053.getVoce_FdaEV (G.Esercizio, G.TI_APPARTENENZA, G.TI_GESTIONE, G.CD_ELEMENTO_VOCE, G.CD_CDR_ASSEGNATARIO, G.cd_linea_attivita)
From   PDG_MODULO_ENTRATE_GEST G, PDG_ESERCIZIO E
Where  G.ESERCIZIO = INES AND
       G.ESERCIZIO = E.ESERCIZIO And
       G.CD_CENTRO_RESPONSABILITA = E.CD_CENTRO_RESPONSABILITA And
       E.STATO = CNRCTB050.STATO_PDG2_CHIUSO_GEST And
       G.CATEGORIA_DETTAGLIO != 'SCR'
Union All
Select 'OB', O.ESERCIZIO, O.ESERCIZIO_ORIGINALE, OSV.CD_CENTRO_RESPONSABILITA, OSV.CD_LINEA_ATTIVITA, OSV.TI_APPARTENENZA, OSV.TI_GESTIONE, OSV.CD_VOCE
From   OBBLIGAZIONE_SCAD_VOCE OSV, OBBLIGAZIONE O
Where  O.ESERCIZIO = INES And
       OSV.CD_CDS          = O.CD_CDS          AND
       OSV.ESERCIZIO       = O.ESERCIZIO       AND
       OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE       AND
       OSV.PG_OBBLIGAZIONE = O.PG_OBBLIGAZIONE
Union All
Select 'AC', O.ESERCIZIO, O.ESERCIZIO_ORIGINALE, OSV.CD_CENTRO_RESPONSABILITA, OSV.CD_LINEA_ATTIVITA, O.TI_APPARTENENZA, O.TI_GESTIONE, O.CD_VOCE
From   ACCERTAMENTO_SCAD_VOCE OSV, ACCERTAMENTO O
Where  O.ESERCIZIO = INES And
       OSV.CD_CDS          = O.CD_CDS          AND
       OSV.ESERCIZIO       = O.ESERCIZIO       AND
       OSV.ESERCIZIO_ORIGINALE = O.ESERCIZIO_ORIGINALE       AND
       OSV.PG_ACCERTAMENTO = O.PG_ACCERTAMENTO
Union All
Select 'RR', INES, ESERCIZIO, CD_CDR_LINEA, CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE,
       CNRCTB053.getVoce_FdaEV (Esercizio, TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_CDR_LINEA, cd_linea_attivita)
From   PDG_RESIDUO_DET
Where  ESERCIZIO = INES And
       (ESERCIZIO, CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita)) In
       (Select ESERCIZIO, CD_CDS
        From   PARAMETRI_CDS
        Where  FL_RIBALTATO = 'Y')
Union All
Select 'VR', D.ESERCIZIO_VOCE, D.ESERCIZIO_RES, D.CD_CDR, D.CD_LINEA_ATTIVITA, D.TI_APPARTENENZA, D.TI_GESTIONE, D.CD_VOCE
From   VAR_STANZ_RES_RIGA D, VAR_STANZ_RES T
Where  D.ESERCIZIO     = T.ESERCIZIO     AND
       D.PG_VARIAZIONE = T.PG_VARIAZIONE And
       T.STATO = 'APP' And
       D.ESERCIZIO_VOCE = INES
Union All
Select 'VC', D.ESERCIZIO, D.ESERCIZIO, D.CD_CDR_ASSEGNATARIO, D.CD_LINEA_ATTIVITA, D.TI_APPARTENENZA, D.TI_GESTIONE,
        CNRCTB053.getVoce_FdaEV (D.Esercizio, D.TI_APPARTENENZA, D.TI_GESTIONE, D.CD_ELEMENTO_VOCE, D.CD_CDR_ASSEGNATARIO, D.cd_linea_attivita) CD_VOCE
From   PDG_VARIAZIONE_RIGA_GEST D, PDG_VARIAZIONE T
Where  D.ESERCIZIO     = T.ESERCIZIO     AND
       D.PG_VARIAZIONE_PDG = T.PG_VARIAZIONE_PDG And
       T.STATO In ('APP', 'APF') And
       D.CATEGORIA_DETTAGLIO != 'SCR' And
       D.ESERCIZIO = INES) TAB_UNION
Where  Not Exists
        (Select 1
         From   VOCE_F_SALDI_CDR_LINEA V
         Where  V.ESERCIZIO = TAB_UNION.ESERCIZIO And
                V.ESERCIZIO_RES = TAB_UNION.ESERCIZIO_RES And
                V.CD_CENTRO_RESPONSABILITA = TAB_UNION.CDR And
                V.CD_LINEA_ATTIVITA = TAB_UNION.CD_LINEA_ATTIVITA And
                V.TI_APPARTENENZA = TAB_UNION.TI_APPARTENENZA And
                V.TI_GESTIONE = TAB_UNION.TI_GESTIONE And
                V.CD_VOCE = TAB_UNION.CD_VOCE)
                ) Loop

If nvl(recParametriCnr.fl_nuovo_pdg,'N')='Y' Then
	Insert INTO VOCE_F_SALDI_CDR_LINEA
	(ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, IM_STANZ_INIZIALE_A1,
	 IM_STANZ_INIZIALE_A2, IM_STANZ_INIZIALE_A3, VARIAZIONI_PIU, VARIAZIONI_MENO, IM_STANZ_INIZIALE_CASSA, VARIAZIONI_PIU_CASSA,
	 VARIAZIONI_MENO_CASSA, IM_OBBL_ACC_COMP,IM_STANZ_RES_IMPROPRIO, VAR_PIU_STANZ_RES_IMP, VAR_MENO_STANZ_RES_IMP, IM_OBBL_RES_IMP,
	 VAR_PIU_OBBL_RES_IMP, VAR_MENO_OBBL_RES_IMP, IM_OBBL_RES_PRO, VAR_PIU_OBBL_RES_PRO, VAR_MENO_OBBL_RES_PRO,
	 IM_MANDATI_REVERSALI_PRO, IM_MANDATI_REVERSALI_IMP, IM_PAGAMENTI_INCASSI, DACR, UTCR, DUVA, UTUV, PG_VER_REC, CD_ELEMENTO_VOCE)
Values
(saldi_mancanti.ESERCIZIO, saldi_mancanti.ESERCIZIO_RES, saldi_mancanti.CDR, saldi_mancanti.CD_LINEA_ATTIVITA,
 saldi_mancanti.TI_APPARTENENZA, saldi_mancanti.TI_GESTIONE, saldi_mancanti.CD_VOCE, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
 Sysdate, 'MARTELLO06', Sysdate, 'MARTELLO06', 1, saldi_mancanti.cd_voce);--dal 2016 uguale a cd_elemento_voce
else
	Insert INTO VOCE_F_SALDI_CDR_LINEA
(ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, IM_STANZ_INIZIALE_A1,
 IM_STANZ_INIZIALE_A2, IM_STANZ_INIZIALE_A3, VARIAZIONI_PIU, VARIAZIONI_MENO, IM_STANZ_INIZIALE_CASSA, VARIAZIONI_PIU_CASSA,
 VARIAZIONI_MENO_CASSA, IM_OBBL_ACC_COMP,IM_STANZ_RES_IMPROPRIO, VAR_PIU_STANZ_RES_IMP, VAR_MENO_STANZ_RES_IMP, IM_OBBL_RES_IMP,
 VAR_PIU_OBBL_RES_IMP, VAR_MENO_OBBL_RES_IMP, IM_OBBL_RES_PRO, VAR_PIU_OBBL_RES_PRO, VAR_MENO_OBBL_RES_PRO,
 IM_MANDATI_REVERSALI_PRO, IM_MANDATI_REVERSALI_IMP, IM_PAGAMENTI_INCASSI, DACR, UTCR, DUVA, UTUV, PG_VER_REC, CD_ELEMENTO_VOCE)
Values
(saldi_mancanti.ESERCIZIO, saldi_mancanti.ESERCIZIO_RES, saldi_mancanti.CDR, saldi_mancanti.CD_LINEA_ATTIVITA,
 saldi_mancanti.TI_APPARTENENZA, saldi_mancanti.TI_GESTIONE, saldi_mancanti.CD_VOCE, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
 Sysdate, 'MARTELLO06', Sysdate, 'MARTELLO06', 1, (Select cd_elemento_voce
                                                   From   VOCE_F
                                                   Where  VOCE_F.ESERCIZIO = saldi_mancanti.ESERCIZIO
                                                      And VOCE_F.ti_appartenenza = saldi_mancanti.ti_appartenenza
                                                      And VOCE_F.ti_gestione = saldi_mancanti.ti_gestione
                                                    And VOCE_F.cd_voce = saldi_mancanti.cd_voce));
End If;

If saldi_mancanti.tipo = 'GS' Then
     MOTIVO := 'Gestionale Spese';
Elsif saldi_mancanti.tipo = 'GE' Then
     MOTIVO := 'Gestionale Entrate';
Elsif saldi_mancanti.tipo = 'OB' Then
     MOTIVO := 'Obbligazioni Comp/Res';
Elsif saldi_mancanti.tipo = 'AC' Then
     MOTIVO := 'Accertamenti Comp/Res';
Elsif saldi_mancanti.tipo = 'RR' Then
     MOTIVO := 'Ricostruzione Residui per CDS Ribaltati';
Elsif saldi_mancanti.tipo = 'VR' Then
     MOTIVO := 'Variazioni allo Stanziamento Residuo';
Elsif saldi_mancanti.tipo = 'VC' Then
     MOTIVO := 'Variazioni a Competenza';
End If;

IBMUTL200.logInf(apg_exec,'Inserito Saldo Mancante per assenza '||MOTIVO||
                         ' Es:'||saldi_mancanti.ESERCIZIO||' Es. Res.:'||saldi_mancanti.ESERCIZIO_RES||
                         ' CDR: '||saldi_mancanti.CDR||' Linea: '||saldi_mancanti.CD_LINEA_ATTIVITA||
                         ' App: '||saldi_mancanti.TI_APPARTENENZA||' Gest: '||saldi_mancanti.TI_GESTIONE||
                         ' Voce '||saldi_mancanti.CD_VOCE, ' ','SOC');

End Loop;

End;

End;
/


