CREATE OR REPLACE PACKAGE         CNRCTB054 as
--
-- CNRCTB054 - Package di gestione della tabella VOCE_F_SALDI_CMP e BILANCIO_PREVENTIVO
-- Date: 11/09/2003
-- Version: 1.9
--
-- Dependency: CNRCTB 001 IBMERR 001
--
-- History:
--
-- Date: 21/02/2002
-- Version: 1.0
-- Creazione
--
-- Date: 24/04/2002
-- Version: 1.1
-- Aggiunto metodo di check stato preventivo iniziale
--
-- Date: 30/05/2002
-- Version: 1.2
-- Aggiunte costanti variazioni di bilancio
--
-- Date: 18/06/2002
-- Version: 1.3
-- Aggiunte costanti per variazioni automatiche di bilancio
--
-- Date: 18/07/2002
-- Version: 1.4
-- Aggiornamento documentazione
--
-- Date: 20/08/2002
-- Version: 1.5
-- Introdotta la variazione di bilancio pluriennale
--
-- Date: 02/09/2002
-- Version: 1.6
-- Aggiunte costanti
--
-- Date: 11/09/2002
-- Version: 1.7
-- Aggiunto metodo di aggiornamento dei saldi documenti contabili
--
-- Date: 07/07/2003
-- Version: 1.8
-- Aggiunto metodo di aggiornamento stanziamento iniziale saldi residui
--
-- Date: 11/09/2003
-- Version: 1.9
-- Aggiornamento documentazione
--
-- Constants:

-- Stati variazione di bilancio preventivo CNR/CDS
ERR_LOCK EXCEPTION;

PRAGMA EXCEPTION_INIT(ERR_LOCK,-30006);

STATO_VARIAZIONE_DEFINITIVA CONSTANT VARCHAR2(5):='D';
STATO_VARIAZIONE_PROVVISORIA CONSTANT VARCHAR2(5):='P';

-- Stati del bilancio preventivo CNR/CDS

STATO_PREVENTIVO_INIZIALE CONSTANT VARCHAR2(5):='A';
STATO_PREVENTIVO_PREDISPOSTO CONSTANT VARCHAR2(5):='B';
STATO_PREVENTIVO_APPROVATO CONSTANT VARCHAR2(5):='C';

-- Origine del dettaglio perv. finanzizario

-- Creati da aggregati CDR I
ORIGINE_PDG CONSTANT VARCHAR2(5):='PDG';

-- Imputati manualemente
ORIGINE_DIR CONSTANT VARCHAR2(5):='DIR';

-- Inizializzazione
ORIGINE_INI CONSTANT VARCHAR2(5):='INI';

-- Indicazione competenza/residui
TI_COMPETENZA CONSTANT CHAR(1):='C';
TI_RESIDUI CONSTANT CHAR(1):='R';

-- Tipi di variazione di bilancio

TI_VAR_LIBERA CONSTANT    VARCHAR2(10) := 'VAR_LIBERA';
TI_VAR_QUAD CONSTANT      VARCHAR2(10) := 'VAR_QUAD';
TI_VAR_STORNO_E CONSTANT  VARCHAR2(10) := 'STORNO_E';
TI_VAR_STORNO_S CONSTANT  VARCHAR2(10) := 'STORNO_S';
TI_VAR_ECO_RES_S CONSTANT VARCHAR2(10) := 'VAR_ECO';
TI_VAR_PREL_FON CONSTANT  VARCHAR2(10) := 'PREL_FON';

-- Tipi di variazione al PDG

TI_VARPDG_STORNO_S_STESSO_IST  Constant VARCHAR2(15) := 'STO_S_CDS';
TI_VARPDG_STORNO_E_STESSO_IST  Constant VARCHAR2(15) := 'STO_E_CDS';
TI_VARPDG_STORNO_S_IST_DIVERSI Constant VARCHAR2(15) := 'STO_S_TOT';
TI_VARPDG_STORNO_E_IST_DIVERSI Constant VARCHAR2(15) := 'STO_E_TOT';
TI_VARPDG_VAR_POS_STESSO_IST   Constant VARCHAR2(15) := 'VAR_PIU_CDS';
TI_VARPDG_VAR_NEG_STESSO_IST   Constant VARCHAR2(15) := 'VAR_MENO_CDS';
TI_VARPDG_VAR_POS_IST_DIVERSI  Constant VARCHAR2(15) := 'VAR_PIU_TOT';
TI_VARPDG_VAR_NEG_IST_DIVERSI  Constant VARCHAR2(15) := 'VAR_MENO_TOT';
TI_VARPDG_VAR_STESSO_IST       Constant VARCHAR2(15) := 'VAR_CDS';
TI_VARPDG_VAR_IST_DIVERSI      Constant VARCHAR2(15) := 'VAR_TOT';
TI_VARPDG_PRELIEVO_FONDI       Constant VARCHAR2(15) := 'PREL_FON';
TI_VARPDG_RESTITUZIONE_FONDI   Constant VARCHAR2(15) := 'REST_FON';
TI_VARPDG_VAR_POS_FONDI        Constant VARCHAR2(15) := 'VAR_PIU_FON';
TI_VARPDG_VAR_NEG_FONDI        Constant VARCHAR2(15) := 'VAR_MENO_FON';
TI_VARPDG_NON_DEFINITO         Constant VARCHAR2(15) := 'NO_TIPO';

-- Causali di var bilancio di sistema

-- Ripartizione automatica entrate CNR
CAU_VAR_RIP_AUT_E CONSTANT VARCHAR2(10):='RIP_AUT_EN';

-- CAUSALI VARIAZIONI BILANCIO:

        -- GENERATE AUTOMATICAMENTE PER APPROVAZIONE VAR. STANZ. RESIDUO
CAU_VAR_STO_STANZ_RES Constant VARCHAR2(10) := 'STO_ST_RES';
CAU_ECO_STANZ_RES     Constant VARCHAR2(10) := 'ECO_ST_RES';

        -- GENERATE AUTOMATICAMENTE PER APPROVAZIONE VAR. AL PDG
CAU_VAR_STO_S_PDG     Constant VARCHAR2(10) := 'STO_S_PDG';
CAU_ECO_VAR_PDG       Constant VARCHAR2(10) := 'VAR_PDG';
CAU_PREL_FON          Constant VARCHAR2(10) := 'PREL_FON';

-- Elementi voce speciali
ELEMENTO_VOCE_SPECIALE CONSTANT VARCHAR2(50):='ELEMENTO_VOCE_SPECIALE';
FONDO_RISERVA_CDS CONSTANT VARCHAR2(100):='FONDO_RISERVA_CDS';
TIT_SPE_FUNZ_CDS CONSTANT VARCHAR2(100):='TITOLO_SPESE_FUNZIONAMENTO_CDS';

-- Functions e Procedures:

-- Lettura lockante del bilancio finanziario

 procedure lockBilFin(aEs number, aCDCDS varchar2);

-- Procedura di inserimento in VOCE_F_SALDI_CMP

 procedure ins_VOCE_F_SALDI_CMP (aDest VOCE_F_SALDI_CMP%rowtype);

-- Procedura di inserimento in VOCE_F_SALDI_CDR_LINEA

 procedure ins_VOCE_F_SALDI_CDR_LINEA (aDest VOCE_F_SALDI_CDR_LINEA%rowtype);

-- procedura di creazione/aggiornamento saldi per chiamate PL/SQL

 Procedure RESET_IMPORTI_SALDI (aDeltaSaldo  In Out voce_f_saldi_cdr_linea%Rowtype);

 procedure crea_aggiorna_saldi(aDeltaSaldo voce_f_saldi_cdr_linea%Rowtype, COD_CHIAMANTE VARCHAR2,
                               AGGIORNA_STANZ_RES CHAR);

-- Procedura di inserimento della testata BILANCIO_PREVENTIVO

 procedure ins_BILANCIO_PREVENTIVO (aDest BILANCIO_PREVENTIVO%rowtype);

--  Procedura di inserimento della testata variazione di bilancio preventivo
 procedure ins_VAR_BILANCIO (aDest VAR_BILANCIO%rowtype);

--  Procedura di inserimento del dettaglio di variazione di bilancio preventivo
 procedure ins_VAR_BILANCIO_DET (aDest VAR_BILANCIO_DET%rowtype);

 -- Funzione di test che il bilancio CNR sia in stato approvato
 --
 -- pre-post-name: Test bilancio preventivo CNR approvato
 -- pre: Richiesto stato approvazione bilancio preventivo CNR
 -- post:
 --    Approvato -> Ritorna Y
 --    Non approvato -> Ritorna N
 --
 -- Parametri:
 --  aEsercizio -> esercizio contabile

 function isBilancioCNRApprovato(aEsercizio NUMBER) return CHAR;

 -- Funzione di test che il bilancio CNR sia in stato predisposto
 --
 -- pre-post-name: Test bilancio preventivo CNR predisposto
 -- pre: Richiesto stato predisposizione bilancio preventivo CNR
 -- post:
 --    Predisposto -> Ritorna Y
 --    Non predisposto -> Ritorna N
 --
 -- Parametri:
 --  aEsercizio -> esercizio contabile

 function isBilancioCNRPredisposto(aEsercizio NUMBER) return CHAR;

 -- Funzione di test che il bilancio CNR sia in stato iniziale
 --
 -- pre-post-name: Test bilancio preventivo CNR iniziale
 -- pre: Richiesto stato iniziale bilancio preventivo CNR
 -- post:
 --    Iniziale -> Ritorna Y
 --    Non iniziale -> Ritorna N
 --
 -- Parametri:
 --  aEsercizio -> esercizio contabile

 function isBilancioCNRIniziale(aEsercizio NUMBER) return CHAR;

-- Aggiorna il saldo di capitolo finanziario CNR/CDS

 procedure aggiornaSaldi(aSaldo voce_f_saldi_cmp%rowtype,aTiImporto char, aDelta number, aUser varchar2,aTSNow date);

-- Aggiorna lo stanziamento iniziale della voce al ribaltamento/deribaltamento di un residuo

 procedure aggiornaStanziamentoResidui(aSaldo voce_f_saldi_cmp%rowtype, aDelta number, aUser varchar2,aTSNow date);
end;
/


CREATE OR REPLACE PACKAGE BODY         CNRCTB054 is

 procedure aggiornaStanziamentoResidui(aSaldo voce_f_saldi_cmp%rowtype, aDelta number, aUser varchar2,aTSNow date) is
 begin
          update voce_f_saldi_cmp
          set im_stanz_iniziale_a1 = im_stanz_iniziale_a1 + aDelta,
                  duva                             = aTSNow,
                  utuv                             = aUser,
                  pg_ver_rec               = pg_ver_rec + 1
          where cd_cds                        = aSaldo.cd_cds
            and esercizio                         = aSaldo.esercizio
            and ti_appartenenza           = aSaldo.ti_appartenenza
            and ti_gestione                       = aSaldo.ti_gestione
            and cd_voce                           = aSaldo.cd_voce
            and ti_competenza_residuo = aSaldo.ti_competenza_residuo;
 end;

 procedure aggiornaSaldi(aSaldo voce_f_saldi_cmp%rowtype,aTiImporto char, aDelta number, aUser varchar2,aTSNow date) is
 begin
  if aDelta=0 then
   return;
  end if;
  if aTiImporto = 'P' then
   update voce_f_saldi_cmp set
     im_obblig_imp_acr=im_obblig_imp_acr+aDelta,
         duva=aTSNow,
         utuv=aUser,
         pg_ver_rec=pg_ver_rec+1
   where
         esercizio = aSaldo.esercizio
     and cd_cds = aSaldo.cd_cds
     and ti_appartenenza = aSaldo.ti_appartenenza
     and ti_gestione = aSaldo.ti_gestione
     and cd_voce = aSaldo.cd_voce
     and ti_competenza_residuo = aSaldo.ti_competenza_residuo;
  elsif  aTiImporto = 'A' then
   update voce_f_saldi_cmp set
     im_mandati_reversali=im_mandati_reversali+aDelta,
         duva=aTSNow,
         utuv=aUser,
         pg_ver_rec=pg_ver_rec+1
   where
         esercizio = aSaldo.esercizio
     and cd_cds = aSaldo.cd_cds
     and ti_appartenenza = aSaldo.ti_appartenenza
     and ti_gestione = aSaldo.ti_gestione
     and cd_voce = aSaldo.cd_voce
     and ti_competenza_residuo = aSaldo.ti_competenza_residuo;
  elsif aTiImporto = 'U' then
   update voce_f_saldi_cmp set
     im_pagamenti_incassi=im_pagamenti_incassi+aDelta,
         duva=aTSNow,
         utuv=aUser,
         pg_ver_rec=pg_ver_rec+1
   where
         esercizio = aSaldo.esercizio
     and cd_cds = aSaldo.cd_cds
     and ti_appartenenza = aSaldo.ti_appartenenza
     and ti_gestione = aSaldo.ti_gestione
     and cd_voce = aSaldo.cd_voce
     and ti_competenza_residuo = aSaldo.ti_competenza_residuo;
  else
   IBMERR001.RAISE_ERR_GENERICO('Errore di invocazione funzione di aggiornamento dei saldi finanziari');
  end if;
 end;

 procedure lockBilFin(aEs number, aCDCDS varchar2) is
  aTemp varchar2(20);
 begin
  begin
   select utuv into aTemp from BILANCIO_PREVENTIVO where
        esercizio = aEs
    and cd_cds = aCDCDS
   for update nowait;
  exception when no_data_found then
   IBMERR001.RAISE_ERR_GENERICO('Bilancio preventivo di '||aCDCDS||' non ancora definito!');
  end;
 end;

 function isBilancioCNRIniziale(aEsercizio NUMBER) return CHAR is
  aStato varchar2(5);
 begin
  select stato into aStato from bilancio_preventivo where
       esercizio = aEsercizio
   and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
  if aStato = STATO_PREVENTIVO_INIZIALE then
   return 'Y';
  end if;
  return 'N';
 exception when NO_DATA_FOUND then
  return 'N';
 end;

 function isBilancioCNRPredisposto(aEsercizio NUMBER) return CHAR is
  aStato varchar2(5);
 begin
  select stato into aStato from bilancio_preventivo where
       esercizio = aEsercizio
   and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
  if aStato = STATO_PREVENTIVO_PREDISPOSTO then
   return 'Y';
  end if;
  return 'N';
 exception when NO_DATA_FOUND then
  return 'N';
 end;

 function isBilancioCNRApprovato(aEsercizio NUMBER) return CHAR is
  aStato varchar2(5);
 begin
  select stato into aStato from bilancio_preventivo where
       esercizio = aEsercizio
   and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR;
  if aStato = STATO_PREVENTIVO_APPROVATO then
   return 'Y';
  end if;
  return 'N';
 exception when NO_DATA_FOUND then
  return 'N';
 end;

 procedure ins_VOCE_F_SALDI_CMP (aDest VOCE_F_SALDI_CMP%rowtype) is
  begin
   insert into VOCE_F_SALDI_CMP (
     ESERCIZIO
    ,CD_CDS
    ,TI_APPARTENENZA
    ,TI_GESTIONE
        ,TI_COMPETENZA_RESIDUO
    ,CD_VOCE
        ,FL_SOLA_LETTURA
    ,ORIGINE
    ,IM_STANZ_INIZIALE_A1
    ,IM_STANZ_INIZIALE_A2
    ,IM_STANZ_INIZIALE_A3
    ,VARIAZIONI_PIU
    ,VARIAZIONI_MENO
    ,IM_OBBLIG_IMP_ACR
    ,IM_MANDATI_REVERSALI
    ,IM_PAGAMENTI_INCASSI
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDS
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
        ,aDest.TI_COMPETENZA_RESIDUO
    ,aDest.CD_VOCE
        ,aDest.FL_SOLA_LETTURA
    ,aDest.ORIGINE
    ,aDest.IM_STANZ_INIZIALE_A1
    ,aDest.IM_STANZ_INIZIALE_A2
    ,aDest.IM_STANZ_INIZIALE_A3
    ,aDest.VARIAZIONI_PIU
    ,aDest.VARIAZIONI_MENO
    ,aDest.IM_OBBLIG_IMP_ACR
    ,aDest.IM_MANDATI_REVERSALI
    ,aDest.IM_PAGAMENTI_INCASSI
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_VOCE_F_SALDI_CDR_LINEA (aDest VOCE_F_SALDI_CDR_LINEA%rowtype) is
  begin
   insert into VOCE_F_SALDI_CDR_LINEA (
     ESERCIZIO,   ESERCIZIO_RES,
     CD_CENTRO_RESPONSABILITA,  CD_LINEA_ATTIVITA,
     TI_APPARTENENZA,  TI_GESTIONE,
     CD_VOCE,
     IM_STANZ_INIZIALE_A1, IM_STANZ_INIZIALE_A2,  IM_STANZ_INIZIALE_A3,
     VARIAZIONI_PIU,  VARIAZIONI_MENO,
     IM_STANZ_INIZIALE_CASSA, VARIAZIONI_PIU_CASSA, VARIAZIONI_MENO_CASSA,
     IM_OBBL_ACC_COMP, IM_STANZ_RES_IMPROPRIO,
     VAR_PIU_STANZ_RES_IMP, VAR_MENO_STANZ_RES_IMP,
     IM_OBBL_RES_IMP, VAR_PIU_OBBL_RES_IMP, VAR_MENO_OBBL_RES_IMP,
     IM_OBBL_RES_PRO, VAR_PIU_OBBL_RES_PRO, VAR_MENO_OBBL_RES_PRO,
     IM_MANDATI_REVERSALI_PRO, IM_MANDATI_REVERSALI_IMP,
     IM_PAGAMENTI_INCASSI, DACR, UTCR, DUVA, UTUV, PG_VER_REC, CD_ELEMENTO_VOCE)
Values (
     adest.ESERCIZIO, adest.ESERCIZIO_RES,
     adest.CD_CENTRO_RESPONSABILITA, adest.CD_LINEA_ATTIVITA,
     adest.TI_APPARTENENZA,  adest.TI_GESTIONE,
     adest.CD_VOCE,
     adest.IM_STANZ_INIZIALE_A1, adest.IM_STANZ_INIZIALE_A2,  adest.IM_STANZ_INIZIALE_A3,
     adest.VARIAZIONI_PIU,  adest.VARIAZIONI_MENO,
     adest.IM_STANZ_INIZIALE_CASSA, adest.VARIAZIONI_PIU_CASSA, adest.VARIAZIONI_MENO_CASSA,
     adest.IM_OBBL_ACC_COMP, adest.IM_STANZ_RES_IMPROPRIO,
     adest.VAR_PIU_STANZ_RES_IMP, adest.VAR_MENO_STANZ_RES_IMP,
     adest.IM_OBBL_RES_IMP, adest.VAR_PIU_OBBL_RES_IMP, adest.VAR_MENO_OBBL_RES_IMP,
     adest.IM_OBBL_RES_PRO, adest.VAR_PIU_OBBL_RES_PRO, adest.VAR_MENO_OBBL_RES_PRO,
     adest.IM_MANDATI_REVERSALI_PRO, adest.IM_MANDATI_REVERSALI_IMP,
     adest.IM_PAGAMENTI_INCASSI, adest.DACR, adest.UTCR, adest.DUVA, adest.UTUV,
     adest.PG_VER_REC, adest.CD_ELEMENTO_VOCE);
 end;

Procedure concatenaCampo (Stringa In Out VARCHAR2,valore In NUMBER,campo In VARCHAR2, trovato In Out VARCHAR2) Is
Begin
    If Nvl(valore,0) != 0 Then
      If trovato = 'N' Then
        Stringa := Stringa ||campo;
      Else
        Stringa := Stringa ||','||campo;
      End If;
      trovato := 'S';
    End If;
End;
Procedure concatenaCampoForUpdate (Stringa In Out VARCHAR2,valoreOld In NUMBER,valoreNew In NUMBER,campo In VARCHAR2, trovato In Out VARCHAR2) Is
  totale NUMBER := valoreOld + valoreNew;
  apici        VARCHAR2(1) := '''';
Begin
    If Nvl(valoreNew,0) != 0 Then
      If trovato = 'N' Then
        Stringa := Stringa ||' '||campo||' = '||apici||totale||apici;
      Else
        Stringa := Stringa ||', '||campo||' = '||apici||totale||apici;
      End If;
      trovato := 'S';
    End If;
End;

Procedure eseguiAggiornaSaldi(aOldSaldo In voce_f_saldi_cdr_linea%Rowtype,
                              aDeltaSaldo In voce_f_saldi_cdr_linea%Rowtype) Is
    CURSOR_NAME  INTEGER;
    appo         integer;
    p_stringa    VARCHAR2(2000);
    apici        VARCHAR2(1) := '''';
    trovato      VARCHAR2(1) := 'N';
Begin
    CURSOR_NAME:=DBMS_SQL.OPEN_CURSOR;
    p_stringa := 'UPDATE VOCE_F_SALDI_CDR_LINEA SET ';
    concatenaCampoForUpdate (p_stringa,Nvl(aOldSaldo.IM_STANZ_INIZIALE_A1,0), Nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A1,0),'IM_STANZ_INIZIALE_A1',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.IM_STANZ_INIZIALE_A2,0)    ,nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A2,0)    ,'IM_STANZ_INIZIALE_A2',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.IM_STANZ_INIZIALE_A3,0)    ,nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A3,0)    ,'IM_STANZ_INIZIALE_A3',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.VARIAZIONI_PIU,0)          ,nvl(aDeltaSaldo.VARIAZIONI_PIU,0)          ,'VARIAZIONI_PIU',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.VARIAZIONI_MENO,0)         ,nvl(aDeltaSaldo.VARIAZIONI_MENO,0)         ,'VARIAZIONI_MENO',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.IM_STANZ_INIZIALE_CASSA,0) ,nvl(aDeltaSaldo.IM_STANZ_INIZIALE_CASSA,0) ,'IM_STANZ_INIZIALE_CASSA',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.VARIAZIONI_PIU_CASSA,0)    ,nvl(aDeltaSaldo.VARIAZIONI_PIU_CASSA,0)    ,'VARIAZIONI_PIU_CASSA',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.VARIAZIONI_MENO_CASSA,0)   ,nvl(aDeltaSaldo.VARIAZIONI_MENO_CASSA,0)   ,'VARIAZIONI_MENO_CASSA',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.IM_OBBL_ACC_COMP,0)        ,nvl(aDeltaSaldo.IM_OBBL_ACC_COMP,0)        ,'IM_OBBL_ACC_COMP',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.IM_STANZ_RES_IMPROPRIO,0)  ,nvl(aDeltaSaldo.IM_STANZ_RES_IMPROPRIO,0)  ,'IM_STANZ_RES_IMPROPRIO',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.VAR_PIU_STANZ_RES_IMP,0)   ,nvl(aDeltaSaldo.VAR_PIU_STANZ_RES_IMP,0)   ,'VAR_PIU_STANZ_RES_IMP',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.VAR_MENO_STANZ_RES_IMP,0)  ,nvl(aDeltaSaldo.VAR_MENO_STANZ_RES_IMP,0)  ,'VAR_MENO_STANZ_RES_IMP',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.IM_OBBL_RES_IMP,0)         ,nvl(aDeltaSaldo.IM_OBBL_RES_IMP,0)         ,'IM_OBBL_RES_IMP',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.VAR_PIU_OBBL_RES_IMP,0)    ,nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_IMP,0)    ,'VAR_PIU_OBBL_RES_IMP',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.VAR_MENO_OBBL_RES_IMP,0)   ,nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_IMP,0)   ,'VAR_MENO_OBBL_RES_IMP',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.IM_OBBL_RES_PRO,0)         ,nvl(aDeltaSaldo.IM_OBBL_RES_PRO,0)         ,'IM_OBBL_RES_PRO',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.VAR_PIU_OBBL_RES_PRO,0)    ,nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO,0)    ,'VAR_PIU_OBBL_RES_PRO',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.VAR_MENO_OBBL_RES_PRO,0)   ,nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO,0)   ,'VAR_MENO_OBBL_RES_PRO',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.IM_MANDATI_REVERSALI_PRO,0),nvl(aDeltaSaldo.IM_MANDATI_REVERSALI_PRO,0),'IM_MANDATI_REVERSALI_PRO',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.IM_MANDATI_REVERSALI_IMP,0),nvl(aDeltaSaldo.IM_MANDATI_REVERSALI_IMP,0),'IM_MANDATI_REVERSALI_IMP',trovato);
    concatenaCampoForUpdate (p_stringa,nvl(aOldSaldo.IM_PAGAMENTI_INCASSI,0)    ,nvl(aDeltaSaldo.IM_PAGAMENTI_INCASSI,0)    ,'IM_PAGAMENTI_INCASSI',trovato);
    If trovato = 'N' Then
      p_stringa := p_stringa||' DUVA = sysdate, PG_VER_REC = PG_VER_REC +1, UTUV = '||Apici||aDeltaSaldo.utuv||Apici;
    Else
      p_stringa := p_stringa||', DUVA = sysdate, PG_VER_REC = PG_VER_REC +1, UTUV = '||Apici||aDeltaSaldo.utuv||Apici;
    End If;
    p_stringa := p_stringa||
                ' WHERE ESERCIZIO = '||aOldSaldo.ESERCIZIO||' AND '||
                'ESERCIZIO_RES = '||aOldSaldo.ESERCIZIO_RES||' AND '||
                'CD_CENTRO_RESPONSABILITA = '||apici||aOldSaldo.CD_CENTRO_RESPONSABILITA||apici||' AND '||
                'CD_LINEA_ATTIVITA        = '||apici||aOldSaldo.CD_LINEA_ATTIVITA||apici||' AND '||
                'TI_APPARTENENZA          = '||apici||aOldSaldo.TI_APPARTENENZA||apici||' AND '||
                'TI_GESTIONE              = '||apici||aOldSaldo.TI_GESTIONE||apici||' AND '||
                'CD_VOCE                  = '||apici||aOldSaldo.CD_VOCE||apici;
    dbms_sql.parse(CURSOR_NAME, p_stringa, DBMS_SQL.V7);
    appo := dbms_sql.execute(CURSOR_NAME);
    dbms_sql.close_cursor(CURSOR_NAME);
End;

Function creaSelectForUpdate(aDeltaSaldo In voce_f_saldi_cdr_linea%Rowtype, esercizio NUMBER)
        Return voce_f_saldi_cdr_linea%Rowtype Is
    CURSOR_NAME  INTEGER;
    appo         integer;
    p_stringa    VARCHAR2(2000);
    apici        VARCHAR2(1) := '''';
    trovato      VARCHAR2(1) := 'N';
    aSaldo       voce_f_saldi_cdr_linea%Rowtype;
    parametri_esercizio parametri_cnr%rowtype:=null;
    parametri_esercizioNew parametri_cnr%rowtype:=null;
    ev_old  elemento_voce%rowtype:=null;
    ev_new  elemento_voce%rowtype:=null;
    vocef   voce_f%Rowtype;
Begin
	dbms_output.put_line('aDeltaSaldo.ESERCIZIO '||aDeltaSaldo.ESERCIZIO||' - aDeltaSaldo.ESERCIZIO_RES '||aDeltaSaldo.ESERCIZIO_RES||'  - ESERCIZIO: '||esercizio||' - Voce '||aDeltaSaldo.cd_voce);
    if(aDeltaSaldo.ESERCIZIO !=esercizio) then
      parametri_esercizio:=CNRUTL001.getRecParametriCnr(aDeltaSaldo.ESERCIZIO);
      parametri_esercizioNew:=CNRUTL001.getRecParametriCnr(esercizio);
      if(parametri_esercizio.fl_nuovo_pdg='N' and parametri_esercizioNew.fl_nuovo_pdg='Y') then
	      begin
		      select * into vocef from voce_f
		      where
		        esercizio =aDeltaSaldo.ESERCIZIO And
		        ti_gestione = aDeltaSaldo.ti_gestione and
		        ti_appartenenza = aDeltaSaldo.ti_appartenenza and
		        cd_voce = aDeltaSaldo.cd_voce;

		      select * into ev_old from elemento_voce
		      where
		        esercizio =aDeltaSaldo.ESERCIZIO And
		        ti_gestione = aDeltaSaldo.ti_gestione and
		        ti_appartenenza = aDeltaSaldo.ti_appartenenza and
		        cd_elemento_voce = vocef.cd_elemento_voce;

	        ev_new :=cnrctb046.getElementoVoceNew(ev_old);

	      exception when no_data_found then
	       null;
	      end;
	    end if;
	  end if;
  if(ev_new.esercizio is not null ) then
  	p_stringa := 'SELECT ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, '||
                'TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, IM_STANZ_INIZIALE_A1, '||
                'IM_STANZ_INIZIALE_A2, IM_STANZ_INIZIALE_A3, VARIAZIONI_PIU, '||
                'VARIAZIONI_MENO, IM_STANZ_INIZIALE_CASSA, VARIAZIONI_PIU_CASSA, '||
                'VARIAZIONI_MENO_CASSA, IM_OBBL_ACC_COMP, IM_STANZ_RES_IMPROPRIO, '||
                'VAR_PIU_STANZ_RES_IMP, VAR_MENO_STANZ_RES_IMP, IM_OBBL_RES_IMP, '||
                'VAR_PIU_OBBL_RES_IMP, VAR_MENO_OBBL_RES_IMP, IM_OBBL_RES_PRO, '||
                'VAR_PIU_OBBL_RES_PRO, VAR_MENO_OBBL_RES_PRO, IM_MANDATI_REVERSALI_PRO, '||
                'IM_MANDATI_REVERSALI_IMP, IM_PAGAMENTI_INCASSI, '||
                'DACR, UTCR, DUVA, UTUV, PG_VER_REC, CD_ELEMENTO_VOCE '||
                'FROM  VOCE_F_SALDI_CDR_LINEA '||
                'WHERE ESERCIZIO = '||esercizio||' AND '||
                'ESERCIZIO_RES = '||aDeltaSaldo.ESERCIZIO_RES||' AND '||
                'CD_CENTRO_RESPONSABILITA = '||apici||aDeltaSaldo.CD_CENTRO_RESPONSABILITA||apici||' AND '||
                'CD_LINEA_ATTIVITA        = '||apici||aDeltaSaldo.CD_LINEA_ATTIVITA||apici||' AND '||
                'TI_APPARTENENZA          = '||apici||ev_new.TI_APPARTENENZA||apici||' AND '||
                'TI_GESTIONE              = '||apici||aDeltaSaldo.TI_GESTIONE||apici||' AND '||
                'CD_VOCE                  = '||apici||ev_new.CD_ELEMENTO_VOCE||apici||
                ' FOR Update of ';
  else
    p_stringa := 'SELECT ESERCIZIO, ESERCIZIO_RES, CD_CENTRO_RESPONSABILITA, CD_LINEA_ATTIVITA, '||
                'TI_APPARTENENZA, TI_GESTIONE, CD_VOCE, IM_STANZ_INIZIALE_A1, '||
                'IM_STANZ_INIZIALE_A2, IM_STANZ_INIZIALE_A3, VARIAZIONI_PIU, '||
                'VARIAZIONI_MENO, IM_STANZ_INIZIALE_CASSA, VARIAZIONI_PIU_CASSA, '||
                'VARIAZIONI_MENO_CASSA, IM_OBBL_ACC_COMP, IM_STANZ_RES_IMPROPRIO, '||
                'VAR_PIU_STANZ_RES_IMP, VAR_MENO_STANZ_RES_IMP, IM_OBBL_RES_IMP, '||
                'VAR_PIU_OBBL_RES_IMP, VAR_MENO_OBBL_RES_IMP, IM_OBBL_RES_PRO, '||
                'VAR_PIU_OBBL_RES_PRO, VAR_MENO_OBBL_RES_PRO, IM_MANDATI_REVERSALI_PRO, '||
                'IM_MANDATI_REVERSALI_IMP, IM_PAGAMENTI_INCASSI, '||
                'DACR, UTCR, DUVA, UTUV, PG_VER_REC, CD_ELEMENTO_VOCE '||
                'FROM  VOCE_F_SALDI_CDR_LINEA '||
                'WHERE ESERCIZIO = '||esercizio||' AND '||
                'ESERCIZIO_RES = '||aDeltaSaldo.ESERCIZIO_RES||' AND '||
                'CD_CENTRO_RESPONSABILITA = '||apici||aDeltaSaldo.CD_CENTRO_RESPONSABILITA||apici||' AND '||
                'CD_LINEA_ATTIVITA        = '||apici||aDeltaSaldo.CD_LINEA_ATTIVITA||apici||' AND '||
                'TI_APPARTENENZA          = '||apici||aDeltaSaldo.TI_APPARTENENZA||apici||' AND '||
                'TI_GESTIONE              = '||apici||aDeltaSaldo.TI_GESTIONE||apici||' AND '||
                'CD_VOCE                  = '||apici||aDeltaSaldo.CD_VOCE||apici||
                ' FOR Update of ';
    end if;
    concatenaCampo (p_stringa,aDeltaSaldo.IM_STANZ_INIZIALE_A1    ,'IM_STANZ_INIZIALE_A1',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.IM_STANZ_INIZIALE_A2    ,'IM_STANZ_INIZIALE_A2',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.IM_STANZ_INIZIALE_A3    ,'IM_STANZ_INIZIALE_A3',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.VARIAZIONI_PIU          ,'VARIAZIONI_PIU',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.VARIAZIONI_MENO         ,'VARIAZIONI_MENO',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.IM_STANZ_INIZIALE_CASSA ,'IM_STANZ_INIZIALE_CASSA',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.VARIAZIONI_PIU_CASSA    ,'VARIAZIONI_PIU_CASSA',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.VARIAZIONI_MENO_CASSA   ,'VARIAZIONI_MENO_CASSA',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.IM_OBBL_ACC_COMP        ,'IM_OBBL_ACC_COMP',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.IM_STANZ_RES_IMPROPRIO  ,'IM_STANZ_RES_IMPROPRIO',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.VAR_PIU_STANZ_RES_IMP   ,'VAR_PIU_STANZ_RES_IMP',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.VAR_MENO_STANZ_RES_IMP  ,'VAR_MENO_STANZ_RES_IMP',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.IM_OBBL_RES_IMP         ,'IM_OBBL_RES_IMP',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.VAR_PIU_OBBL_RES_IMP    ,'VAR_PIU_OBBL_RES_IMP',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.VAR_MENO_OBBL_RES_IMP   ,'VAR_MENO_OBBL_RES_IMP',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.IM_OBBL_RES_PRO         ,'IM_OBBL_RES_PRO',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.VAR_PIU_OBBL_RES_PRO    ,'VAR_PIU_OBBL_RES_PRO',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.VAR_MENO_OBBL_RES_PRO   ,'VAR_MENO_OBBL_RES_PRO',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.IM_MANDATI_REVERSALI_PRO,'IM_MANDATI_REVERSALI_PRO',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.IM_MANDATI_REVERSALI_IMP,'IM_MANDATI_REVERSALI_IMP',trovato);
    concatenaCampo (p_stringa,aDeltaSaldo.IM_PAGAMENTI_INCASSI    ,'IM_PAGAMENTI_INCASSI',trovato);
    If trovato = 'N' Then
      p_stringa := p_stringa ||' ESERCIZIO ';
    End If;
    p_stringa := p_stringa ||' WAIT 180';
    /*pipe.send_message(Substr(p_stringa,1,250));
    pipe.send_message(Substr(p_stringa,251,250));
    pipe.send_message(Substr(p_stringa,501,250));
    pipe.send_message(Substr(p_stringa,751,250));
    pipe.send_message(Substr(p_stringa,1001,250));*/
Dbms_Output.put_line(p_stringa);
    CURSOR_NAME:=DBMS_SQL.OPEN_CURSOR;
    DBMS_SQL.PARSE(CURSOR_NAME, P_Stringa, DBMS_SQL.V7);

    Dbms_Sql.DEFINE_COLUMN(cursor_name,1 ,aSaldo.ESERCIZIO );
    DBMS_SQL.DEFINE_COLUMN(cursor_name,2 ,aSaldo.ESERCIZIO_RES );
    DBMS_SQL.DEFINE_COLUMN(cursor_name,3 ,aSaldo.CD_CENTRO_RESPONSABILITA,30 );
    DBMS_SQL.DEFINE_COLUMN(cursor_name,4 ,aSaldo.CD_LINEA_ATTIVITA ,10);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,5 ,aSaldo.TI_APPARTENENZA ,1);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,6 ,aSaldo.TI_GESTIONE ,1);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,7 ,aSaldo.CD_VOCE,50 );
    DBMS_SQL.DEFINE_COLUMN(cursor_name,8 ,aSaldo.IM_STANZ_INIZIALE_A1);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,9 ,aSaldo.IM_STANZ_INIZIALE_A2);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,10,aSaldo.IM_STANZ_INIZIALE_A3);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,11,aSaldo.VARIAZIONI_PIU);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,12,aSaldo.VARIAZIONI_MENO);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,13,aSaldo.IM_STANZ_INIZIALE_CASSA);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,14,aSaldo.VARIAZIONI_PIU_CASSA);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,15,aSaldo.VARIAZIONI_MENO_CASSA);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,16,aSaldo.IM_OBBL_ACC_COMP);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,17,aSaldo.IM_STANZ_RES_IMPROPRIO);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,18,aSaldo.VAR_PIU_STANZ_RES_IMP);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,19,aSaldo.VAR_MENO_STANZ_RES_IMP);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,20,aSaldo.IM_OBBL_RES_IMP);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,21,aSaldo.VAR_PIU_OBBL_RES_IMP);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,22,aSaldo.VAR_MENO_OBBL_RES_IMP);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,23,aSaldo.IM_OBBL_RES_PRO);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,24,aSaldo.VAR_PIU_OBBL_RES_PRO);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,25,aSaldo.VAR_MENO_OBBL_RES_PRO);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,26,aSaldo.IM_MANDATI_REVERSALI_PRO);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,27,aSaldo.IM_MANDATI_REVERSALI_IMP);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,28,aSaldo.IM_PAGAMENTI_INCASSI);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,29,aSaldo.DACR);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,30,aSaldo.UTCR,20);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,31,aSaldo.DUVA);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,32,aSaldo.UTUV,20);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,33,aSaldo.PG_VER_REC);
    DBMS_SQL.DEFINE_COLUMN(cursor_name,34,aSaldo.CD_ELEMENTO_VOCE,20);

    appo := DBMS_SQL.execute_and_fetch(cursor_name);

    DBMS_SQL.COLUMN_VALUE(cursor_name,1 ,aSaldo.ESERCIZIO );
    DBMS_SQL.COLUMN_VALUE(cursor_name,2 ,aSaldo.ESERCIZIO_RES );
    DBMS_SQL.COLUMN_VALUE(cursor_name,3 ,aSaldo.CD_CENTRO_RESPONSABILITA );
    DBMS_SQL.COLUMN_VALUE(cursor_name,4 ,aSaldo.CD_LINEA_ATTIVITA );
    DBMS_SQL.COLUMN_VALUE(cursor_name,5 ,aSaldo.TI_APPARTENENZA );
    DBMS_SQL.COLUMN_VALUE(cursor_name,6 ,aSaldo.TI_GESTIONE );
    DBMS_SQL.COLUMN_VALUE(cursor_name,7 ,aSaldo.CD_VOCE );
    DBMS_SQL.COLUMN_VALUE(cursor_name,8 ,aSaldo.IM_STANZ_INIZIALE_A1);
    DBMS_SQL.COLUMN_VALUE(cursor_name,9 ,aSaldo.IM_STANZ_INIZIALE_A2);
    DBMS_SQL.COLUMN_VALUE(cursor_name,10,aSaldo.IM_STANZ_INIZIALE_A3);
    DBMS_SQL.COLUMN_VALUE(cursor_name,11,aSaldo.VARIAZIONI_PIU);
    DBMS_SQL.COLUMN_VALUE(cursor_name,12,aSaldo.VARIAZIONI_MENO);
    DBMS_SQL.COLUMN_VALUE(cursor_name,13,aSaldo.IM_STANZ_INIZIALE_CASSA);
    DBMS_SQL.COLUMN_VALUE(cursor_name,14,aSaldo.VARIAZIONI_PIU_CASSA);
    DBMS_SQL.COLUMN_VALUE(cursor_name,15,aSaldo.VARIAZIONI_MENO_CASSA);
    DBMS_SQL.COLUMN_VALUE(cursor_name,16,aSaldo.IM_OBBL_ACC_COMP);
    DBMS_SQL.COLUMN_VALUE(cursor_name,17,aSaldo.IM_STANZ_RES_IMPROPRIO);
    DBMS_SQL.COLUMN_VALUE(cursor_name,18,aSaldo.VAR_PIU_STANZ_RES_IMP);
    DBMS_SQL.COLUMN_VALUE(cursor_name,19,aSaldo.VAR_MENO_STANZ_RES_IMP);
    DBMS_SQL.COLUMN_VALUE(cursor_name,20,aSaldo.IM_OBBL_RES_IMP);
    DBMS_SQL.COLUMN_VALUE(cursor_name,21,aSaldo.VAR_PIU_OBBL_RES_IMP);
    DBMS_SQL.COLUMN_VALUE(cursor_name,22,aSaldo.VAR_MENO_OBBL_RES_IMP);
    DBMS_SQL.COLUMN_VALUE(cursor_name,23,aSaldo.IM_OBBL_RES_PRO);
    DBMS_SQL.COLUMN_VALUE(cursor_name,24,aSaldo.VAR_PIU_OBBL_RES_PRO);
    DBMS_SQL.COLUMN_VALUE(cursor_name,25,aSaldo.VAR_MENO_OBBL_RES_PRO);
    DBMS_SQL.COLUMN_VALUE(cursor_name,26,aSaldo.IM_MANDATI_REVERSALI_PRO);
    DBMS_SQL.COLUMN_VALUE(cursor_name,27,aSaldo.IM_MANDATI_REVERSALI_IMP);
    DBMS_SQL.COLUMN_VALUE(cursor_name,28,aSaldo.IM_PAGAMENTI_INCASSI);
    DBMS_SQL.COLUMN_VALUE(cursor_name,29,aSaldo.DACR);
    DBMS_SQL.COLUMN_VALUE(cursor_name,30,aSaldo.UTCR);
    DBMS_SQL.COLUMN_VALUE(cursor_name,31,aSaldo.DUVA);
    DBMS_SQL.COLUMN_VALUE(cursor_name,32,aSaldo.UTUV);
    DBMS_SQL.COLUMN_VALUE(cursor_name,33,aSaldo.PG_VER_REC);
    DBMS_SQL.COLUMN_VALUE(cursor_name,34,aSaldo.CD_ELEMENTO_VOCE);

    DBMS_SQL.CLOSE_CURSOR(CURSOR_NAME);

    Return(aSaldo);

End;

Procedure RESET_IMPORTI_SALDI (aDeltaSaldo  In Out voce_f_saldi_cdr_linea%Rowtype) Is
Begin
  aDeltaSaldo.IM_STANZ_INIZIALE_A1      := 0;
  aDeltaSaldo.IM_STANZ_INIZIALE_A2      := 0;
  aDeltaSaldo.IM_STANZ_INIZIALE_A3      := 0;
  aDeltaSaldo.VARIAZIONI_PIU            := 0;
  aDeltaSaldo.VARIAZIONI_MENO           := 0;
  aDeltaSaldo.IM_STANZ_INIZIALE_CASSA   := 0;
  aDeltaSaldo.VARIAZIONI_PIU_CASSA      := 0;
  aDeltaSaldo.VARIAZIONI_MENO_CASSA     := 0;
  aDeltaSaldo.IM_OBBL_ACC_COMP          := 0;
  aDeltaSaldo.IM_STANZ_RES_IMPROPRIO    := 0;
  aDeltaSaldo.VAR_PIU_STANZ_RES_IMP     := 0;
  aDeltaSaldo.VAR_MENO_STANZ_RES_IMP    := 0;
  aDeltaSaldo.IM_OBBL_RES_IMP           := 0;
  aDeltaSaldo.VAR_PIU_OBBL_RES_IMP      := 0;
  aDeltaSaldo.VAR_MENO_OBBL_RES_IMP     := 0;
  aDeltaSaldo.IM_OBBL_RES_PRO           := 0;
  aDeltaSaldo.VAR_PIU_OBBL_RES_PRO      := 0;
  aDeltaSaldo.VAR_MENO_OBBL_RES_PRO     := 0;
  aDeltaSaldo.IM_MANDATI_REVERSALI_PRO  := 0;
  aDeltaSaldo.IM_MANDATI_REVERSALI_IMP  := 0;
  aDeltaSaldo.IM_PAGAMENTI_INCASSI      := 0;
End;

Procedure crea_aggiorna_saldi(aDeltaSaldo VOCE_F_SALDI_CDR_LINEA%Rowtype, COD_CHIAMANTE VARCHAR2,
          AGGIORNA_STANZ_RES CHAR) IS

aOldSaldo       VOCE_F_SALDI_CDR_LINEA%ROWTYPE;
aNewSaldo       VOCE_F_SALDI_CDR_LINEA%ROWTYPE;
aOldSaldo_NEXT  VOCE_F_SALDI_CDR_LINEA%ROWTYPE;
aCd_elemento_voce VOCE_F_SALDI_CDR_LINEA.cd_elemento_voce%TYPE;
alinea          linea_attivita%Rowtype;
aelvoce         elemento_voce%Rowtype;
fl_2006         CHAR(1);

BEGIN
/*
If ADELTASALDO.ESERCIZIO = 2005 And ADELTASALDO.ESERCIZIO_RES = 2005
And ADELTASALDO.CD_CENTRO_RESPONSABILITA = '101.000.000' And
ADELTASALDO.TI_APPARTENENZA = 'C' And
ADELTASALDO.TI_GESTIONE = 'E' Then
   IBMERR001.RAISE_ERR_GENERICO('A');
End If;
     IBMERR001.RAISE_ERR_GENERICO('PARAMETRI PASSATI DAL CHIAMANTE '||cod_chiamante||
' ESERCIZIO                '||ADELTASALDO.ESERCIZIO               ||
' ESERCIZIO_RES            '||ADELTASALDO.ESERCIZIO_RES           ||
' CD_CENTRO_RESPONSABILITA '||ADELTASALDO.CD_CENTRO_RESPONSABILITA||
' CD_LINEA_ATTIVITA        '||ADELTASALDO.CD_LINEA_ATTIVITA       ||
' TI_APPARTENENZA          '||ADELTASALDO.TI_APPARTENENZA         ||
' TI_GESTIONE              '||ADELTASALDO.TI_GESTIONE             ||
' CD_VOCE                  '||ADELTASALDO.CD_VOCE);
*/
--End If;


If aDeltaSaldo.ESERCIZIO IS NULL Then
     IBMERR001.RAISE_ERR_GENERICO('Impossibile aggiornare i Saldi per CdR/Voce/Linea, parametro "Esercizio" nullo ! Cod. Chiamante '||cod_chiamante);
elsif   aDeltaSaldo.ESERCIZIO_RES IS NULL  Then
     IBMERR001.RAISE_ERR_GENERICO('Impossibile aggiornare i Saldi per CdR/Voce/Linea, parametro "Esercizio Residuo" nullo ! Cod. Chiamante '||cod_chiamante);
elsif   aDeltaSaldo.CD_CENTRO_RESPONSABILITA IS NULL  Then
     IBMERR001.RAISE_ERR_GENERICO('Impossibile aggiornare i Saldi per CdR/Voce/Linea, parametro "CdR" nullo ! Cod. Chiamante '||cod_chiamante);
elsif   aDeltaSaldo.CD_LINEA_ATTIVITA IS NULL  Then
     IBMERR001.RAISE_ERR_GENERICO('Impossibile aggiornare i Saldi per CdR/Voce/Linea, parametro "LdA" nullo ! Cod. Chiamante '||cod_chiamante);
elsif   aDeltaSaldo.TI_APPARTENENZA IS NULL  Then
     IBMERR001.RAISE_ERR_GENERICO('Impossibile aggiornare i Saldi per CdR/Voce/Linea, parametro "Tipo Appartenenza" nullo ! Cod. Chiamante '||cod_chiamante);
elsif   aDeltaSaldo.TI_GESTIONE IS NULL  Then
     IBMERR001.RAISE_ERR_GENERICO('Impossibile aggiornare i Saldi per CdR/Voce/Linea, parametro "Tipo Gstione" nullo ! Cod. Chiamante '||cod_chiamante);
elsif   aDeltaSaldo.CD_VOCE IS NULL  Then
     IBMERR001.RAISE_ERR_GENERICO('Impossibile aggiornare i Saldi per CdR/Voce/Linea, parametro "Voce" nullo ! Cod. Chiamante '||cod_chiamante);
elsif   aDeltaSaldo.UTUV IS NULL THEN
     IBMERR001.RAISE_ERR_GENERICO('Impossibile aggiornare i Saldi per CdR/Voce/Linea, parametro "Utente Ultima Variazione" nullo ! Cod. Chiamante '||cod_chiamante);
End IF;

Dbms_Output.PUT_LINE ('ENTRO CON '||aDeltaSaldo.ESERCIZIO||'/'||aDeltaSaldo.ESERCIZIO_RES||'/'||
                                        aDeltaSaldo.CD_CENTRO_RESPONSABILITA||'/'||aDeltaSaldo.CD_LINEA_ATTIVITA||'/'||
                                        aDeltaSaldo.TI_APPARTENENZA||'/'||aDeltaSaldo.TI_GESTIONE||'/'||
                                        aDeltaSaldo.CD_VOCE||' CHIAMENTE '||COD_CHIAMANTE);

Begin
  aOldSaldo := creaSelectForUpdate(aDeltaSaldo,aDeltaSaldo.ESERCIZIO);
  If aOldSaldo.ESERCIZIO Is Null Then
    Raise No_Data_Found;
  End If;
Exception
  When ERR_LOCK Then
    IBMERR001.RAISE_ERR_GENERICO('Impossibile procedere all''operazione. La risorsa non è al momento disponibile. Riprovare.');
End;

If aOldSaldo.CD_elemento_VOCE Is Null Then
   IBMERR001.RAISE_ERR_GENERICO('Sui Saldi relativi a '||aDeltaSaldo.ESERCIZIO||'/'||aDeltaSaldo.ESERCIZIO_RES||'/'||
                                        aDeltaSaldo.CD_CENTRO_RESPONSABILITA||'/'||aDeltaSaldo.CD_LINEA_ATTIVITA||'/'||
                                        aDeltaSaldo.TI_APPARTENENZA||'/'||aDeltaSaldo.TI_GESTIONE||'/'||
                                        aDeltaSaldo.CD_VOCE||' non è indicato l''Elemento Voce');
End If;

Begin
  Select *
  Into  alinea
  From  linea_attivita
  Where cd_centro_responsabilita = aDeltaSaldo.CD_CENTRO_RESPONSABILITA And
        cd_linea_attivita = aDeltaSaldo.CD_LINEA_ATTIVITA;
Exception
  When No_Data_Found Then
   IBMERR001.RAISE_ERR_GENERICO('La G.A.E. '||aDeltaSaldo.CD_CENTRO_RESPONSABILITA||'/'||aDeltaSaldo.CD_LINEA_ATTIVITA||
' non esiste in anagrafica !');
End;

Begin
  Select  *
  Into    aelvoce
  From    elemento_voce
  Where   ESERCIZIO        = aDeltaSaldo.ESERCIZIO AND
          TI_APPARTENENZA  = aDeltaSaldo.TI_APPARTENENZA AND
          TI_GESTIONE      = aDeltaSaldo.TI_GESTIONE AND
          CD_ELEMENTO_VOCE = aOldSaldo.CD_elemento_VOCE;
Exception
  When No_Data_Found Then
   IBMERR001.RAISE_ERR_GENERICO('L''Elemento Voce '||aDeltaSaldo.ESERCIZIO||'/'||aDeltaSaldo.TI_APPARTENENZA||'/'||aDeltaSaldo.TI_GESTIONE||'/'||aOldSaldo.CD_elemento_VOCE||
' non esiste in anagrafica !');
End;

Begin
  Select  Nvl(FL_REGOLAMENTO_2006, 'N')
  Into    fl_2006
  From    parametri_cnr
  Where   esercizio = aDeltaSaldo.ESERCIZIO;
Exception
  When No_Data_Found Then
   IBMERR001.RAISE_ERR_GENERICO('Attenzione !!! Non esistono i Parametri CNR per l''Esercizio '||aDeltaSaldo.ESERCIZIO);
End;

If FL_2006 = 'Y' And
   aDeltaSaldo.ESERCIZIO = aDeltaSaldo.ESERCIZIO_RES And
   aDeltaSaldo.TI_GESTIONE = 'S' And
 ((NVL(aDeltasaldo.IM_OBBL_ACC_COMP, 0) != 0) Or (NVL(aDeltasaldo.VARIAZIONI_MENO, 0)) != 0) And
   Not CNRCTB037.ISLINEASFONDABILE(alinea) And Not CNRCTB037.ISELEMENTOVOCESFONDABILE(aelvoce) And
   Nvl(aOldsaldo.IM_STANZ_INIZIALE_A1, 0) + NVL(aDeltasaldo.IM_STANZ_INIZIALE_A1, 0) +
   Nvl(aOldsaldo.VARIAZIONI_PIU, 0) + NVL(aDeltasaldo.VARIAZIONI_PIU, 0) -
   (Nvl(aOldsaldo.VARIAZIONI_MENO, 0) + NVL(aDeltasaldo.VARIAZIONI_MENO, 0)) -
   (Nvl(aOldsaldo.IM_OBBL_ACC_COMP, 0) + NVL(aDeltasaldo.IM_OBBL_ACC_COMP, 0)) < 0 Then
   IBMERR001.RAISE_ERR_GENERICO('Impossibile effettuare l''operazione, Voce e GAE non sono sfondabili e '||
' la disponibilità di competenza per CdR/GAE/Voce ('||
Nvl(Ltrim(
 To_Char(Nvl(aOldsaldo.IM_STANZ_INIZIALE_A1, 0) +
         Nvl(aDeltasaldo.IM_STANZ_INIZIALE_A1, 0) +
         Nvl(aOldsaldo.VARIAZIONI_PIU, 0) +
         Nvl(aDeltasaldo.VARIAZIONI_PIU, 0) -
         Nvl(aOldsaldo.VARIAZIONI_MENO, 0) +
         Nvl(aDeltasaldo.VARIAZIONI_MENO, 0)), '999g999g999g999g999g990d99'), 0)
||') non è sufficiente a coprire il totale dei documenti ('||
Ltrim(
 To_Char(Nvl(aOldsaldo.IM_OBBL_ACC_COMP, 0) +
         NVL(aDeltasaldo.IM_OBBL_ACC_COMP, 0), '999g999g999g999g999g990d99'))
||'). [Esercizio '||aDeltaSaldo.ESERCIZIO||', Es. Residuo '||aDeltaSaldo.ESERCIZIO_RES||', CdR '||aDeltaSaldo.CD_CENTRO_RESPONSABILITA||', GAE '||aDeltaSaldo.CD_LINEA_ATTIVITA||', Voce '||aDeltaSaldo.CD_VOCE||']');

End If;
/*
Dbms_Output.put_line ('update 54 VAR_MENO_OBBL_RES_IMP  '||NVL(aOldsaldo.IM_OBBL_RES_PRO, 0)||' '||NVL(aDeltasaldo.IM_OBBL_RES_PRO, 0)||' '||cod_chiamante||' '||
aOldSaldo.ESERCIZIO||' '||
aOldSaldo.ESERCIZIO_RES||' '||
aOldSaldo.CD_CENTRO_RESPONSABILITA||' '||
aOldSaldo.CD_LINEA_ATTIVITA||' '||
aOldSaldo.TI_APPARTENENZA||' '||
aOldSaldo.TI_GESTIONE||' '||
aOldSaldo.CD_VOCE);
*/

eseguiAggiornaSaldi(aOldsaldo,aDeltasaldo);

-- SE IL CDS E' GIA' STATO RIBALTATO DEVO AGGIORNARE I SALDI ANCHE NELL'ANNO NUOVO
-- (SOLO QUELLI CHE HANNO INFLUENZA NEL NUOVO ESERCIZIO)
Begin

If CNRCTB048.getcdsribaltato (aDeltaSaldo.ESERCIZIO, CNRUTL001.GETCDSFROMCDR(aDeltaSaldo.CD_CENTRO_RESPONSABILITA)) = 'Y' And
((aDeltaSaldo.TI_GESTIONE = CNRCTB001.GESTIONE_SPESE And (aDeltaSaldo.IM_STANZ_INIZIALE_A1 != 0 Or
                                                          aDeltaSaldo.VARIAZIONI_PIU != 0 Or
                                                          aDeltaSaldo.VARIAZIONI_MENO != 0 Or
                                                          aDeltaSaldo.IM_OBBL_ACC_COMP != 0 Or
                                                          aDeltaSaldo.IM_STANZ_RES_IMPROPRIO != 0 Or
                                                          aDeltaSaldo.VAR_PIU_STANZ_RES_IMP != 0 Or
                                                          aDeltaSaldo.VAR_MENO_STANZ_RES_IMP != 0 Or
                                                          aDeltaSaldo.IM_OBBL_RES_IMP != 0 Or
                                                          aDeltaSaldo.VAR_PIU_OBBL_RES_IMP != 0 Or
                                                          aDeltaSaldo.VAR_MENO_OBBL_RES_IMP != 0 Or
                                                          aDeltaSaldo.IM_OBBL_RES_PRO != 0 Or
                                                          aDeltaSaldo.VAR_PIU_OBBL_RES_PRO != 0 Or
                                                          aDeltaSaldo.VAR_MENO_OBBL_RES_PRO != 0)) Or
(aDeltaSaldo.TI_GESTIONE = CNRCTB001.GESTIONE_ENTRATE And (aDeltaSaldo.IM_OBBL_ACC_COMP != 0 Or
                                                           aDeltaSaldo.IM_OBBL_RES_PRO != 0 Or
                                                           aDeltaSaldo.VAR_PIU_OBBL_RES_PRO != 0 Or
                                                           aDeltaSaldo.VAR_MENO_OBBL_RES_PRO != 0))) Then
Begin
  dbms_output.put_line ('esercizio +1');
  aOldSaldo_NEXT := creaSelectForUpdate(aDeltaSaldo,aDeltaSaldo.ESERCIZIO+1);
  If aOldSaldo_NEXT.ESERCIZIO Is Null Then
    Raise No_Data_Found;
  End If;
Exception
  When ERR_LOCK Then
    IBMERR001.RAISE_ERR_GENERICO('Impossibile procedere all''operazione. La risorsa non è al momento disponibile. Riprovare.');
End;

/*
Dbms_Output.put_line ('aOldsaldo_NEXT.IM_OBBL_RES_PRO '||aOldsaldo_NEXT.IM_OBBL_RES_PRO);
Dbms_Output.put_line ('aDeltaSaldo.IM_OBBL_RES_PRO '||aDeltaSaldo.IM_OBBL_RES_PRO);
Dbms_Output.put_line ('aDeltaSaldo.VAR_PIU_OBBL_RES_PRO '||aDeltaSaldo.VAR_PIU_OBBL_RES_PRO);
Dbms_Output.put_line ('aDeltaSaldo.VAR_MENO_OBBL_RES_PRO '||aDeltaSaldo.VAR_MENO_OBBL_RES_PRO);

If Nvl(aOldsaldo_NEXT.IM_OBBL_RES_PRO, 0) + Nvl(aDeltaSaldo.IM_OBBL_RES_PRO, 0) +
     Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO, 0) - Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO, 0) < 0 Then
      IBMERR001.RAISE_ERR_GENERICO('04. Impossibile effettuare l''operazione ! '||
' Nell''esercizio '||To_Char(aOldSaldo_NEXT.ESERCIZIO)||' e per il CdR '||aDeltaSaldo.CD_CENTRO_RESPONSABILITA||', '||
' Voce '||aDeltaSaldo.CD_VOCE||' e GAE '||aDeltaSaldo.CD_LINEA_ATTIVITA||' il Totale Residui Propri '||
' diventerebbe negativo ('||To_Char(Nvl(aOldsaldo_NEXT.IM_OBBL_RES_PRO, 0) + Nvl(aDeltaSaldo.IM_OBBL_RES_PRO, 0) +
     Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO, 0) - Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO, 0))||')');
End If;

Update VOCE_F_SALDI_CDR_LINEA
SET    IM_OBBL_RES_PRO          = Nvl(aOldsaldo_NEXT.IM_OBBL_RES_PRO, 0) +
                                  Nvl(aDeltaSaldo.IM_OBBL_RES_PRO, 0) +
                                  Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO, 0) -
                                  Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO, 0)
WHERE ESERCIZIO                = aOldSaldo_NEXT.ESERCIZIO                AND
      ESERCIZIO_RES            = aOldSaldo_NEXT.ESERCIZIO_RES            AND
      CD_CENTRO_RESPONSABILITA = aOldSaldo_NEXT.CD_CENTRO_RESPONSABILITA AND
      CD_LINEA_ATTIVITA        = aOldSaldo_NEXT.CD_LINEA_ATTIVITA        AND
      TI_APPARTENENZA          = aOldSaldo_NEXT.TI_APPARTENENZA          AND
      TI_GESTIONE              = aOldSaldo_NEXT.TI_GESTIONE              AND
      CD_VOCE                  = aOldSaldo_NEXT.CD_VOCE;
*/

If AGGIORNA_STANZ_RES = 'Y' Then
  Declare
    newStanzResImproprio voce_f_saldi_cdr_linea.IM_STANZ_RES_IMPROPRIO%Type;
  Begin
    If aDeltaSaldo.ESERCIZIO = aDeltaSaldo.ESERCIZIO_RES Then
      newStanzResImproprio := Nvl(aOldsaldo_NEXT.IM_STANZ_RES_IMPROPRIO, 0) + Nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A1, 0) +
                              Nvl(aDeltaSaldo.VARIAZIONI_PIU, 0) - Nvl(aDeltaSaldo.VARIAZIONI_MENO, 0) - Nvl(aDeltaSaldo.IM_OBBL_ACC_COMP, 0);
      If newStanzResImproprio < 0 Then
        IBMERR001.RAISE_ERR_GENERICO('01. Impossibile effettuare l''operazione ! '||
' Nell''esercizio '||aDeltaSaldo.ESERCIZIO||' e per il CdR '||aDeltaSaldo.CD_CENTRO_RESPONSABILITA||', '||
' Voce '||aDeltaSaldo.CD_VOCE||' e GAE '||aDeltaSaldo.CD_LINEA_ATTIVITA||' lo Stanziamento Residuo Improprio '||
' diventerebbe negativo ('||To_Char(newStanzResImproprio,'999g999g999g999g990d00')||')');
      End If;
    Elsif aDeltaSaldo.ESERCIZIO > aDeltaSaldo.ESERCIZIO_RES Then
      newStanzResImproprio := Nvl(aOldsaldo_NEXT.IM_STANZ_RES_IMPROPRIO, 0) +
                              (Nvl(aDeltaSaldo.IM_STANZ_RES_IMPROPRIO, 0) + Nvl(aDeltaSaldo.VAR_PIU_STANZ_RES_IMP, 0) - Nvl(aDeltaSaldo.VAR_MENO_STANZ_RES_IMP, 0)) -
                              (Nvl(aDeltaSaldo.IM_OBBL_RES_IMP, 0) + Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_IMP, 0) - Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_IMP, 0));
      If newStanzResImproprio < 0 Then
        IBMERR001.RAISE_ERR_GENERICO('01.1 Impossibile effettuare l''operazione ! '||
' Nell''esercizio '||aDeltaSaldo.ESERCIZIO||' e per il CdR '||aDeltaSaldo.CD_CENTRO_RESPONSABILITA||', '||
' Voce '||aDeltaSaldo.CD_VOCE||' e GAE '||aDeltaSaldo.CD_LINEA_ATTIVITA||' lo Stanziamento Residuo Improprio '||
' diventerebbe negativo ('||To_Char(newStanzResImproprio,'999g999g999g999g990d00')||')');
      End If;
    End If;

    If aDeltaSaldo.TI_GESTIONE = CNRCTB001.GESTIONE_SPESE Then
      Update VOCE_F_SALDI_CDR_LINEA
      Set    IM_STANZ_RES_IMPROPRIO   = newStanzResImproprio
      Where ESERCIZIO                = aOldSaldo_NEXT.ESERCIZIO                AND
            ESERCIZIO_RES            = aOldSaldo_NEXT.ESERCIZIO_RES            AND
            CD_CENTRO_RESPONSABILITA = aOldSaldo_NEXT.CD_CENTRO_RESPONSABILITA AND
            CD_LINEA_ATTIVITA        = aOldSaldo_NEXT.CD_LINEA_ATTIVITA        AND
            TI_APPARTENENZA          = aOldSaldo_NEXT.TI_APPARTENENZA          AND
            TI_GESTIONE              = aOldSaldo_NEXT.TI_GESTIONE              AND
            CD_VOCE                  = aOldSaldo_NEXT.CD_VOCE;
    End if;    -- end if (aDeltaSaldo.TI_GESTIONE = CNRCTB001.GESTIONE_SPESE then
  End;
End If; -- AGGIORNA_STANZ_RES = 'Y'

End If;

Exception

When No_Data_Found Then
     IBMERR001.RAISE_ERR_GENERICO('Il CDS '||CNRUTL001.GETCDSFROMCDR(aDeltaSaldo.CD_CENTRO_RESPONSABILITA)||
' risulta ribaltato ma non è stato trovato il saldo per l''esercizio '||To_Char(aDeltaSaldo.ESERCIZIO+1)||
' Es. provenienza '||aDeltaSaldo.ESERCIZIO_RES||
' CDR '||aDeltaSaldo.CD_CENTRO_RESPONSABILITA||
' Linea '||aDeltaSaldo.CD_LINEA_ATTIVITA||
' App. '||aDeltaSaldo.TI_APPARTENENZA||
' Gestione '||aDeltaSaldo.TI_GESTIONE||
' Voce '||aDeltaSaldo.CD_VOCE||
' Delta Importi: '||
' Stanz. Ini. '||Nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A1, 0)||
' Var. + '||Nvl(aDeltaSaldo.VARIAZIONI_PIU, 0)||
' Var. - '||Nvl(aDeltaSaldo.VARIAZIONI_MENO, 0)||
' Obb/acc comp. '||Nvl(aDeltaSaldo.IM_OBBL_ACC_COMP, 0)||
' Stanz. Res. Impr. '||Nvl(aDeltaSaldo.IM_STANZ_RES_IMPROPRIO, 0)||
' Var. + Stanz. Res. '||Nvl(aDeltaSaldo.VAR_PIU_STANZ_RES_IMP, 0)||
' Var. - Stanz. Res. '||Nvl(aDeltaSaldo.VAR_MENO_STANZ_RES_IMP, 0)||
' Obbl. Res. Impr. '||Nvl(aDeltaSaldo.IM_OBBL_RES_IMP, 0)||
' Var. + Obbl. Res. Imp. '||Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_IMP, 0)||
' Var. - Obbl. Res. Imp. '||Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_IMP, 0)||
' Obb/Acc Res. Pro. '||Nvl(aDeltaSaldo.IM_OBBL_RES_PRO, 0)||
' Var + Obb/Acc Res. Pro. '||Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO, 0)||
' Var - Obb/Acc Res. Pro. '||Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO, 0));

End;

Exception

WHEN NO_DATA_FOUND THEN
-- Recupero la voce corta da VOCE_F

Declare
  recParametriCNR PARAMETRI_CNR%Rowtype;
  appoVoce voce_f.cd_voce%type;
Begin
  recParametriCNR := CNRUTL001.getRecParametriCnr(aDeltaSaldo.ESERCIZIO);
  If recParametriCNR.fl_nuovo_pdg='N' Then
    Begin
      SELECT CD_ELEMENTO_VOCE INTO appoVoce
      FROM   VOCE_F
      WHERE  ESERCIZIO = aDeltaSaldo.ESERCIZIO
         AND Ti_appartenenza = aDeltaSaldo.TI_APPARTENENZA
         AND Ti_gestione = aDeltaSaldo.TI_GESTIONE
         AND Cd_voce = aDeltaSaldo.CD_VOCE;
      Exception
    When No_Data_Found Then
         IBMERR001.RAISE_ERR_GENERICO('Per l''Esercizio '||aDeltaSaldo.ESERCIZIO||' non esiste la Voce '||aDeltaSaldo.ESERCIZIO||'/'||aDeltaSaldo.TI_APPARTENENZA||'/'||aDeltaSaldo.TI_GESTIONE||'/'||aDeltaSaldo.CD_VOCE);
    End;
  Else
    appoVoce := aDeltaSaldo.CD_VOCE;
  End If;

  Begin
    Select  *
    Into    aelvoce
    From    elemento_voce
    Where   ESERCIZIO        = aDeltaSaldo.ESERCIZIO AND
            TI_APPARTENENZA  = aDeltaSaldo.TI_APPARTENENZA AND
            TI_GESTIONE      = aDeltaSaldo.TI_GESTIONE AND
            CD_ELEMENTO_VOCE = appoVoce;
  Exception
    When No_Data_Found Then
     IBMERR001.RAISE_ERR_GENERICO('L''Elemento Voce '||aDeltaSaldo.ESERCIZIO||'/'||aDeltaSaldo.TI_APPARTENENZA||'/'||aDeltaSaldo.TI_GESTIONE||'/'||appoVoce||
  ' non esiste in anagrafica !');
  End;

  aCd_elemento_voce := appoVoce;

End;

aNewSaldo.ESERCIZIO                      := aDeltaSaldo.ESERCIZIO;
aNewSaldo.ESERCIZIO_RES                  := aDeltaSaldo.ESERCIZIO_RES;
aNewSaldo.CD_CENTRO_RESPONSABILITA       := aDeltaSaldo.CD_CENTRO_RESPONSABILITA;
aNewSaldo.CD_LINEA_ATTIVITA              := aDeltaSaldo.CD_LINEA_ATTIVITA;
aNewSaldo.TI_APPARTENENZA                := aDeltaSaldo.TI_APPARTENENZA;
aNewSaldo.TI_GESTIONE                    := aDeltaSaldo.TI_GESTIONE;
aNewSaldo.CD_VOCE                        := aDeltaSaldo.CD_VOCE;
aNewSaldo.IM_STANZ_INIZIALE_A1           := NVL(aDeltaSaldo.IM_STANZ_INIZIALE_A1     ,0);
aNewSaldo.IM_STANZ_INIZIALE_A2           := NVL(aDeltaSaldo.IM_STANZ_INIZIALE_A2     ,0);
aNewSaldo.IM_STANZ_INIZIALE_A3           := NVL(aDeltaSaldo.IM_STANZ_INIZIALE_A3     ,0);
aNewSaldo.VARIAZIONI_PIU                 := NVL(aDeltaSaldo.VARIAZIONI_PIU           ,0);
aNewSaldo.VARIAZIONI_MENO                := NVL(aDeltaSaldo.VARIAZIONI_MENO          ,0);
aNewSaldo.IM_STANZ_INIZIALE_CASSA        := NVL(aDeltaSaldo.IM_STANZ_INIZIALE_CASSA  ,0);
aNewSaldo.VARIAZIONI_PIU_CASSA           := NVL(aDeltaSaldo.VARIAZIONI_PIU_CASSA     ,0);
aNewSaldo.VARIAZIONI_MENO_CASSA          := NVL(aDeltaSaldo.VARIAZIONI_MENO_CASSA    ,0);
aNewSaldo.IM_OBBL_ACC_COMP               := NVL(aDeltaSaldo.IM_OBBL_ACC_COMP         ,0);
aNewSaldo.IM_STANZ_RES_IMPROPRIO         := NVL(aDeltaSaldo.IM_STANZ_RES_IMPROPRIO   ,0);
aNewSaldo.VAR_PIU_STANZ_RES_IMP          := NVL(aDeltaSaldo.VAR_PIU_STANZ_RES_IMP    ,0);
aNewSaldo.VAR_MENO_STANZ_RES_IMP         := NVL(aDeltaSaldo.VAR_MENO_STANZ_RES_IMP   ,0);
aNewSaldo.IM_OBBL_RES_IMP                := NVL(aDeltaSaldo.IM_OBBL_RES_IMP          ,0);
aNewSaldo.VAR_PIU_OBBL_RES_IMP           := NVL(aDeltaSaldo.VAR_PIU_OBBL_RES_IMP     ,0);
aNewSaldo.VAR_MENO_OBBL_RES_IMP          := NVL(aDeltaSaldo.VAR_MENO_OBBL_RES_IMP    ,0);
aNewSaldo.IM_OBBL_RES_PRO                := NVL(aDeltaSaldo.IM_OBBL_RES_PRO          ,0);
aNewSaldo.VAR_PIU_OBBL_RES_PRO           := NVL(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO     ,0);
aNewSaldo.VAR_MENO_OBBL_RES_PRO          := NVL(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO    ,0);
aNewSaldo.IM_MANDATI_REVERSALI_PRO       := NVL(aDeltaSaldo.IM_MANDATI_REVERSALI_PRO ,0);
aNewSaldo.IM_MANDATI_REVERSALI_IMP       := NVL(aDeltaSaldo.IM_MANDATI_REVERSALI_IMP ,0);
aNewSaldo.IM_PAGAMENTI_INCASSI           := NVL(aDeltaSaldo.IM_PAGAMENTI_INCASSI     ,0);
aNewSaldo.CD_ELEMENTO_VOCE               := aCd_elemento_voce  ;
aNewSaldo.DACR                           := Sysdate;
aNewSaldo.UTCR                           := aDeltaSaldo.UTUV;
aNewSaldo.DUVA                           := Sysdate;
aNewSaldo.UTUV                           := aDeltaSaldo.UTUV;
aNewSaldo.PG_VER_REC                     := 1;

cnrctb054.ins_VOCE_F_SALDI_CDR_LINEA (aNewSaldo);

-- SE IL CDS E' GIA' STATO RIBALTATO DEVO AGGIORNARE I SALDI ANCHE NELL'ANNO NUOVO
-- (SOLO QUELLI CHE HANNO INFLUENZA NEL NUOVO ESERCIZIO)

If CNRCTB048.getcdsribaltato (aDeltaSaldo.ESERCIZIO, CNRUTL001.GETCDSFROMCDR(aDeltaSaldo.CD_CENTRO_RESPONSABILITA)) = 'Y' And
  (aDeltaSaldo.IM_STANZ_INIZIALE_A1 != 0 Or
   aDeltaSaldo.VARIAZIONI_PIU != 0 Or
   aDeltaSaldo.VARIAZIONI_MENO != 0 Or
   aDeltaSaldo.IM_OBBL_ACC_COMP != 0 Or
   aDeltaSaldo.IM_STANZ_RES_IMPROPRIO != 0 Or
   aDeltaSaldo.VAR_PIU_STANZ_RES_IMP != 0 Or
   aDeltaSaldo.VAR_MENO_STANZ_RES_IMP != 0 Or
   aDeltaSaldo.IM_OBBL_RES_IMP != 0 Or
   aDeltaSaldo.VAR_PIU_OBBL_RES_IMP != 0 Or
   aDeltaSaldo.VAR_MENO_OBBL_RES_IMP != 0 Or
   aDeltaSaldo.IM_OBBL_RES_PRO != 0 Or
   aDeltaSaldo.VAR_PIU_OBBL_RES_PRO != 0 Or
   aDeltaSaldo.VAR_MENO_OBBL_RES_PRO != 0)
   AND (aDeltaSaldo.TI_GESTIONE =CNRCTB001.GESTIONE_SPESE) Then

aNewSaldo := Null;

aNewSaldo.ESERCIZIO                := aDeltaSaldo.ESERCIZIO + 1;
aNewSaldo.ESERCIZIO_RES            := aDeltaSaldo.ESERCIZIO_RES;
aNewSaldo.CD_CENTRO_RESPONSABILITA := aDeltaSaldo.CD_CENTRO_RESPONSABILITA;
aNewSaldo.CD_LINEA_ATTIVITA        := aDeltaSaldo.CD_LINEA_ATTIVITA;
aNewSaldo.TI_APPARTENENZA          := aDeltaSaldo.TI_APPARTENENZA;
aNewSaldo.TI_GESTIONE              := aDeltaSaldo.TI_GESTIONE;
aNewSaldo.CD_VOCE                  := aDeltaSaldo.CD_VOCE;
aNewSaldo.IM_STANZ_RES_IMPROPRIO   := nvl(aDeltaSaldo.IM_STANZ_RES_IMPROPRIO, 0);
aNewSaldo.IM_STANZ_INIZIALE_A1     := Nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A1, 0);
aNewSaldo.IM_STANZ_INIZIALE_A2     := nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A2    , 0);
aNewSaldo.IM_STANZ_INIZIALE_A3     := nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A3    , 0);
aNewSaldo.VARIAZIONI_PIU           := nvl(aDeltaSaldo.VARIAZIONI_PIU          , 0);
aNewSaldo.VARIAZIONI_MENO          := nvl(aDeltaSaldo.VARIAZIONI_MENO         , 0);
aNewSaldo.IM_STANZ_INIZIALE_CASSA  := nvl(aDeltaSaldo.IM_STANZ_INIZIALE_CASSA , 0);
aNewSaldo.VARIAZIONI_PIU_CASSA     := nvl(aDeltaSaldo.VARIAZIONI_PIU_CASSA    , 0);
aNewSaldo.VARIAZIONI_MENO_CASSA    := nvl(aDeltaSaldo.VARIAZIONI_MENO_CASSA   , 0);
aNewSaldo.IM_OBBL_ACC_COMP         := nvl(aDeltaSaldo.IM_OBBL_ACC_COMP        , 0);
aNewSaldo.VAR_PIU_STANZ_RES_IMP    := nvl(aDeltaSaldo.VAR_PIU_STANZ_RES_IMP   , 0);
aNewSaldo.VAR_MENO_STANZ_RES_IMP   := nvl(aDeltaSaldo.VAR_MENO_STANZ_RES_IMP  , 0);
aNewSaldo.VAR_PIU_OBBL_RES_IMP     := nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_IMP    , 0);
aNewSaldo.VAR_MENO_OBBL_RES_IMP    := nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_IMP   , 0);
aNewSaldo.VAR_PIU_OBBL_RES_PRO     := nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO    , 0);
aNewSaldo.VAR_MENO_OBBL_RES_PRO    := nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO   , 0);
aNewSaldo.IM_MANDATI_REVERSALI_PRO := nvl(aDeltaSaldo.IM_MANDATI_REVERSALI_PRO, 0);
aNewSaldo.IM_MANDATI_REVERSALI_IMP := nvl(aDeltaSaldo.IM_MANDATI_REVERSALI_IMP, 0);
aNewSaldo.IM_PAGAMENTI_INCASSI     := nvl(aDeltaSaldo.IM_PAGAMENTI_INCASSI    , 0);

If aDeltaSaldo.ESERCIZIO = aDeltaSaldo.ESERCIZIO_RES Then

  If FL_2006 = 'Y' And
   aDeltaSaldo.TI_GESTIONE = 'S' And
 ((NVL(aDeltasaldo.IM_OBBL_ACC_COMP, 0) != 0) Or (NVL(aDeltasaldo.VARIAZIONI_MENO, 0)) != 0) And
   Not CNRCTB037.ISLINEASFONDABILE(alinea) And Not CNRCTB037.ISELEMENTOVOCESFONDABILE(aelvoce) And
    Nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A1, 0) + Nvl(aDeltaSaldo.VARIAZIONI_PIU, 0) -
     Nvl(aDeltaSaldo.VARIAZIONI_MENO, 0) - Nvl(aDeltaSaldo.IM_OBBL_ACC_COMP, 0) < 0 Then
        IBMERR001.RAISE_ERR_GENERICO('02. Impossibile effettuare l''operazione ! '||
  ' Nell''esercizio '||aNewSaldo.ESERCIZIO||' e per il CdR '||aNewSaldo.CD_CENTRO_RESPONSABILITA||', '||
  ' Voce '||aNewSaldo.CD_VOCE||' e GAE '||aNewSaldo.CD_LINEA_ATTIVITA||' lo stanziamento Residuo Improprio '||
  ' diventerebbe negativo ('||Ltrim(
                                To_Char(Nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A1, 0) +
                                        Nvl(aDeltaSaldo.VARIAZIONI_PIU, 0) -
                                        Nvl(aDeltaSaldo.VARIAZIONI_MENO, 0) -
                                        Nvl(aDeltaSaldo.IM_OBBL_ACC_COMP, 0)),
                                '999g999g999g999g999g990d99')||')');
  End If;
      aNewSaldo.IM_STANZ_RES_IMPROPRIO := Nvl(aDeltaSaldo.IM_STANZ_INIZIALE_A1, 0) +
                                      Nvl(aDeltaSaldo.VARIAZIONI_PIU, 0) -
                                      Nvl(aDeltaSaldo.VARIAZIONI_MENO, 0) -
                                      Nvl(aDeltaSaldo.IM_OBBL_ACC_COMP, 0);
Elsif aDeltaSaldo.ESERCIZIO > aDeltaSaldo.ESERCIZIO_RES Then

  If (NVL(aDeltaSaldo.IM_STANZ_RES_IMPROPRIO, 0) +
                                       NVL(aDeltaSaldo.VAR_PIU_STANZ_RES_IMP , 0) -
                                       NVL(aDeltaSaldo.VAR_MENO_STANZ_RES_IMP, 0)) -
                                      (NVL(aDeltaSaldo.IM_OBBL_RES_IMP       , 0) +
                                       NVL(aDeltaSaldo.VAR_PIU_OBBL_RES_IMP  , 0) -
                                       NVL(aDeltaSaldo.VAR_MENO_OBBL_RES_IMP , 0)) < 0 Then
        IBMERR001.RAISE_ERR_GENERICO('03. Impossibile effettuare l''operazione ! '||
  ' Nell''esercizio '||aNewSaldo.ESERCIZIO||' e per il CdR '||aNewSaldo.CD_CENTRO_RESPONSABILITA||', '||
  ' Voce '||aNewSaldo.CD_VOCE||' e GAE '||aNewSaldo.CD_LINEA_ATTIVITA||' lo stanziamento Residuo Improprio '||
  ' diventerebbe negativo ('||(NVL(aDeltaSaldo.IM_STANZ_RES_IMPROPRIO, 0) +
                                       NVL(aDeltaSaldo.VAR_PIU_STANZ_RES_IMP , 0) -
                                       NVL(aDeltaSaldo.VAR_MENO_STANZ_RES_IMP, 0)) -
                                      (NVL(aDeltaSaldo.IM_OBBL_RES_IMP       , 0) +
                                       NVL(aDeltaSaldo.VAR_PIU_OBBL_RES_IMP  , 0) -
                                       NVL(aDeltaSaldo.VAR_MENO_OBBL_RES_IMP , 0))||')');
  End If;

  aNewSaldo.IM_STANZ_RES_IMPROPRIO := (NVL(aDeltaSaldo.IM_STANZ_RES_IMPROPRIO, 0) +
                                       NVL(aDeltaSaldo.VAR_PIU_STANZ_RES_IMP , 0) -
                                       NVL(aDeltaSaldo.VAR_MENO_STANZ_RES_IMP, 0)) -
                                      (NVL(aDeltaSaldo.IM_OBBL_RES_IMP       , 0) +
                                       NVL(aDeltaSaldo.VAR_PIU_OBBL_RES_IMP  , 0) -
                                       NVL(aDeltaSaldo.VAR_MENO_OBBL_RES_IMP , 0));
End If;

  If Nvl(aDeltaSaldo.IM_OBBL_RES_PRO, 0) + Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO, 0) -
     Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO, 0) < 0 Then
        IBMERR001.RAISE_ERR_GENERICO('05. Impossibile effettuare l''operazione ! '||
' Nell''esercizio '||aNewSaldo.ESERCIZIO||' e per il CdR '||aNewSaldo.CD_CENTRO_RESPONSABILITA||', '||
' Voce '||aNewSaldo.CD_VOCE||' e GAE '||aNewSaldo.CD_LINEA_ATTIVITA||' il totale Residui Propri '||
' diventerebbe negativo ('||Nvl(aDeltaSaldo.IM_OBBL_RES_PRO, 0) + Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO, 0) -
Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO, 0)||')');
  End If;

aNewSaldo.IM_OBBL_RES_PRO          := Nvl(aDeltaSaldo.IM_OBBL_RES_PRO, 0) +
                                      Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO, 0) -
                                      Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO, 0);

  If Nvl(aDeltaSaldo.IM_OBBL_RES_PRO, 0) + Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO, 0) -
     Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO, 0) < 0 Then
        IBMERR001.RAISE_ERR_GENERICO('06. Impossibile effettuare l''operazione ! '||
' Nell''esercizio '||aNewSaldo.ESERCIZIO||' e per il CdR '||aNewSaldo.CD_CENTRO_RESPONSABILITA||', '||
' Voce '||aNewSaldo.CD_VOCE||' e GAE '||aNewSaldo.CD_LINEA_ATTIVITA||' il totale Residui Impropri '||
' diventerebbe negativo ('||Nvl(aDeltaSaldo.IM_OBBL_RES_PRO, 0) + Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_PRO, 0) -
Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_PRO, 0)||')');
  End If;

aNewSaldo.IM_OBBL_RES_IMP          := Nvl(aDeltaSaldo.IM_OBBL_RES_IMP, 0) +
                                      Nvl(aDeltaSaldo.VAR_PIU_OBBL_RES_IMP, 0) -
                                      Nvl(aDeltaSaldo.VAR_MENO_OBBL_RES_IMP, 0);

aNewSaldo.CD_ELEMENTO_VOCE         := aCd_elemento_voce  ;
aNewSaldo.DACR                     := Sysdate;
aNewSaldo.UTCR                     := aDeltaSaldo.UTUV;
aNewSaldo.DUVA                     := Sysdate;
aNewSaldo.UTUV                     := aDeltaSaldo.UTUV;
aNewSaldo.PG_VER_REC               := 1;

cnrctb054.ins_VOCE_F_SALDI_CDR_LINEA (aNewSaldo);

End If;

END;

Procedure ins_BILANCIO_PREVENTIVO (aDest BILANCIO_PREVENTIVO%rowtype) is
  begin
   insert into BILANCIO_PREVENTIVO (
     ESERCIZIO
    ,CD_CDS
    ,TI_APPARTENENZA
    ,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDS
    ,aDest.TI_APPARTENENZA
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_VAR_BILANCIO (aDest VAR_BILANCIO%rowtype) is
  begin
   insert into VAR_BILANCIO (
     CD_CDS
    ,ESERCIZIO
    ,ESERCIZIO_IMPORTI
    ,TI_APPARTENENZA
    ,PG_VARIAZIONE
    ,DS_VARIAZIONE
    ,DS_DELIBERA
    ,TI_VARIAZIONE
    ,CD_CAUSALE_VAR_BILANCIO
    ,STATO
    ,DUVA
    ,UTUV
    ,DACR
    ,UTCR
    ,PG_VER_REC
    ,ESERCIZIO_PDG_VARIAZIONE
    ,PG_VARIAZIONE_PDG
    ,ESERCIZIO_VAR_STANZ_RES
    ,PG_VAR_STANZ_RES
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
        ,aDest.ESERCIZIO_IMPORTI
    ,aDest.TI_APPARTENENZA
    ,aDest.PG_VARIAZIONE
    ,aDest.DS_VARIAZIONE
    ,aDest.DS_DELIBERA
    ,aDest.TI_VARIAZIONE
    ,aDest.CD_CAUSALE_VAR_BILANCIO
    ,aDest.STATO
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.PG_VER_REC
    ,aDest.ESERCIZIO_PDG_VARIAZIONE
    ,aDest.PG_VARIAZIONE_PDG
    ,aDest.ESERCIZIO_VAR_STANZ_RES
    ,aDest.PG_VAR_STANZ_RES           );

 end;
 procedure ins_VAR_BILANCIO_DET (aDest VAR_BILANCIO_DET%rowtype) is
  begin
   insert into VAR_BILANCIO_DET (
     CD_CDS
    ,ESERCIZIO
    ,TI_APPARTENENZA
    ,PG_VARIAZIONE
    ,TI_GESTIONE
    ,CD_VOCE
    ,IM_VARIAZIONE
    ,DUVA
    ,UTUV
    ,DACR
    ,UTCR
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.TI_APPARTENENZA
    ,aDest.PG_VARIAZIONE
    ,aDest.TI_GESTIONE
    ,aDest.CD_VOCE
    ,aDest.IM_VARIAZIONE
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.PG_VER_REC
    );
 end;
end;
/


