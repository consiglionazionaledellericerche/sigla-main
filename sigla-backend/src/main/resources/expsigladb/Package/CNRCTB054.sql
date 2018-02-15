--------------------------------------------------------
--  DDL for Package CNRCTB054
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB054" as
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
