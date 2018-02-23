--------------------------------------------------------
--  DDL for Package CNRCTB000
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB000" as
--
-- CNRCTB000 - Package di gestione tabella ELEMENTO_VOCE E ASSOCIAZIONI
-- Date: 03/11/2005
-- Version: 1.10
--
-- Dependency: CNRCTB 001/015 IBMERR 001
--
-- History:
--
-- Date: 05/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 16/11/2001
-- Version: 1.1
-- Introduzione flag partita-giro in ELEMENTO_VOCE
--
-- Date: 19/11/2001
-- Version: 1.2
-- Introduzione capoconto  finanziario
--
-- Date: 03/12/2001
-- Version: 1.3, 1.4
-- Fix per aggiunta nuovi campi in elemento voce
--
-- Date: 29/12/2001
-- Version: 1.5
-- Aggiunti metodi di lettura delle voci del piano speciali
--
-- Date: 31/12/2001
-- Version: 1.6
-- Agiunto metodo di controllo eliminabilità ass_ev_ev
--
-- Date: 27/01/2002
-- Version: 1.7
-- Aggiunta lettura ordinata per codice in metodi coie_XXX
--
-- Date: 18/07/2002
-- Version: 1.8
-- Aggiornamento della documentazione
--
-- Date: 20/08/2002
-- Version: 1.9
-- Il controllo sull'eliminazione dell'associazione con tipologia di intervento non va fatto
-- se l'esercizio del SAC non è definito o è in stato Iniziale (I)
--
-- Date: 03/11/2005
-- Version: 1.10
-- Aggiunta la ricerca delle voci per contratti a tempo determinato
-- getVoceTFRTEMPODET e getVoceONERICNRTEMPODET
--
-- Constants:

-- Descrizione Tipi Variazioni PDG
DESCR_STO_S_CDS  Constant  VARCHAR(60) := 'Storno di Spesa all''interno dello stesso Istituto';
DESCR_STO_E_CDS  Constant  VARCHAR(60) := 'Storno di Entrata all''interno dello stesso Istituto';
DESCR_STO_S_TOT  Constant  VARCHAR(60) := 'Storno di Spesa tra Istituti diversi';
DESCR_STO_E_TOT  Constant  VARCHAR(60) := 'Storno di Entrata tra Istituti diversi';
DESCR_PREL_FON  Constant  VARCHAR(60) := 'Prelievo dal Fondo';
DESCR_VAR_CDS   Constant  VARCHAR(60) := 'Variazione all''interno dello stesso Istituto';
DESCR_VAR_TOT   Constant  VARCHAR(60) := 'Variazione tra Istituti diversi';

-- Functions e Procedures:

-- Estrae da configurazione CNR l'elemento voce speciale ricavo figurativo altro cdr
-- aEsercizio -> Esercizio contabile

 function getVoceRicFigAltroCDR(aEsercizio number) return elemento_voce%rowtype;

-- Estrae da configurazione CNR l'elemento voce speciale trattamento di fine rapporto
-- aEsercizio -> Esercizio contabile

 function getVoceTFR(aEsercizio number) return elemento_voce%rowtype;

-- Estrae da configurazione CNR l'elemento voce speciale degli oneri cnr
-- aEsercizio -> Esercizio contabile

 function getVoceONERICNR(aEsercizio number) return elemento_voce%rowtype;

-- Estrae da configurazione CNR l'elemento voce speciale trattamento di fine rapporto
-- per personale a tempo determinato
-- aEsercizio -> Esercizio contabile

 function getVoceTFRTEMPODET(aEsercizio number) return elemento_voce%rowtype;

-- Estrae da configurazione CNR l'elemento voce speciale degli oneri cnr
-- per personale a tempo determinato
-- aEsercizio -> Esercizio contabile

 function getVoceONERICNRTEMPODET(aEsercizio number) return elemento_voce%rowtype;

-- dato esercizio/numero della variazione PDG restituisce il tipo

 Function TIPO_VAR_PDG (aEsercizio NUMBER, aNumVar NUMBER)  Return  VARCHAR2;

-- dato il tipo variazione PDG restituisce la descrizione

 Function DESCR_TIPO_VAR_PDG (Tipo VARCHAR) Return  VARCHAR2;

-- data la riga di mandato restituisce la natura del Mandato stesso
 Function TIPO_MANDATO (aMandatoRiga mandato_riga%Rowtype) Return VARCHAR2;

-- data la chiave della riga di mandato restituisce la natura del Mandato stesso
 Function TIPO_MANDATO (aCD_CDS VARCHAR2, aESERCIZIO NUMBER, aPG_MANDATO NUMBER, aESERCIZIO_OBBLIGAZIONE NUMBER,
                        aPG_OBBLIGAZIONE NUMBER, aPG_OBBLIGAZIONE_SCADENZARIO NUMBER, aCD_CDS_DOC_AMM VARCHAR2,
                        aCD_UO_DOC_AMM VARCHAR2, aESERCIZIO_DOC_AMM NUMBER, aCD_TIPO_DOCUMENTO_AMM VARCHAR2,
                        aPG_DOC_AMM NUMBER, aESERCIZIO_ORI_OBBLIGAZIONE NUMBER)  Return VARCHAR2;

-- Inserisce una riga in elemento_voce

 procedure ins_ELEMENTO_VOCE(aDest elemento_voce%rowtype);

-- Inserisce una riga in ass_ev_funz_tipocds

 procedure ins_ASS_EV_FUNZ_TIPOCDS (aDest ASS_EV_FUNZ_TIPOCDS%rowtype);

-- Copia dati interesercizio
-- aEsDest -> row type esercizio di destinazione
--            dal rowtype vengono letti anche utuvu/utcr/duva/dacr

 procedure coie_ELEMENTO_VOCE (aEsDest esercizio%rowtype);

-- Procedura di verifica eliminabilità di una associazione da ass_ev_ev
-- Scatenata da trigger AD_ASS_EV_EV
--
-- Pre:
-- aAss contiene un'associazione in fase di eliminazione
-- Si verifica uno dei seguenti casi:
--
-- Caso 1: Associazione Titolo di spesa CNR -> Titolo di spesa CDS
--  Nella tabella VOCE_F_SALDI_CDR_LINEA esiste già un titolo = Titolo di spesa CNR presente in aAss
--  per l'esercizio specificato in aAss
--
-- Caso 2: Associazione Capitolo di Entrata CNR -> Natura
--  In pdg_preventivo_etr_det esiste almento un dettaglio che contiene la natura specificata
--  per l'esercizio specificato in aAss
--
-- Caso 3: Associazione Capitolo di spesa CDS -> Tipologia di intervento (Capitolo di spesa CNR per categoria 2)
--  Esiste in VOCE_F_SALDI_CDR_LINEA almeno un record con tipologia di intervento = a quella specificata in aAss
--  per l'esercizio specificato in aAss
--
-- Caso 4: Associazione Titolo spesa cds + Tipo di CDS + Natura -> Capitolo di entrata CDS
--  Tale associazione non è eliminabile se esiste almeno un record in VOCE_F_SALDI_CDR_LINEA con capitolo di entrata
--  CDS = a quello specificato in aAss per l'esercizio specificato in aAss
--
-- Post:
-- Viene sollevata l'eccezione generica: Associazione non eliminabile perchè utilizzata
--
-- Parametri:
--
-- aAss -> rowtype contenente l'associazione in fase di eliminazione

 procedure checkEliminAssEvEv(aAss ass_ev_ev%rowtype);

end;
