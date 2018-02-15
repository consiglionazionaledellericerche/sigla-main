--------------------------------------------------------
--  DDL for Package CNRMAR010
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRMAR010" as
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
