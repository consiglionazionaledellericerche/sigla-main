--------------------------------------------------------
--  DDL for Package CNRCTB038
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB038" as
--
-- CNRCTB038 - Package funcioni comuni per mandato/reversale
-- Date: 13/07/2006
-- Version: 2.1
--
-- Package per la gestione DB dell'obbligazione
--
-- Dependency:
--
-- History:
--
-- Date: 09/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 12/05/2002
-- Version: 1.1
-- Estensione con nuove costanti
--
-- Date: 28/05/2002
-- Version: 1.2
-- Aggiunte nuove costanti di stato dei documenti
--
-- Date: 29/05/2002
-- Version: 1.3
-- Aggiunti nuovi campi di importo ritenuta in Mandato, Mandato Riga, Reversale.
--
-- Date: 10/06/2002
-- Version: 1.4
-- Sistemazione costanti
--
-- Date: 24/06/2002
-- Version: 1.5
-- Aggiunta associazione mandato-mandato
--
-- Date: 19/12/2002
-- Version: 1.6
-- Aggiunta ins_SOSPESO_DET_ETR
--
-- Date: 20/02/2003
-- Version: 1.7
-- Aggiunti metodi descrittivi di mandato/reversale
--
-- Date: 28/02/2003
-- Version: 1.8
-- Aggiunta ins_SOSPESO
--
-- Date: 14/03/2003
-- Version: 1.9
-- Aggiunta funzione nextProgressivoSospeso
--
-- Date: 28/05/2003
-- Version: 2.0
-- Aggiunti metodi per riscontro automatico cassiere
--
-- Date: 13/07/2006
-- Version: 2.1
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

TYPE GenericCurTyp IS REF CURSOR;
type righeMandatoList is table of mandato_riga%rowtype index by binary_integer;
type righeReversaleList is table of reversale_riga%rowtype index by binary_integer;

REV_VUOTA reversale%rowtype;
MAN_VUOTO mandato%rowtype;
REV_RIGHE_VUOTA righeReversaleList;
MAN_RIGHE_VUOTA righeMandatoList;

-- Classificazione per tipologia di reversale

TI_REV_ACCRED CONSTANT CHAR(1):='A'; -- Accreditamento. Generata in automatico da trasferimento CNR --> CdS. Esiste solo per CdS.
TI_REV_INC CONSTANT CHAR(1):='I'; -- Reversale di iincasso. Normale reversale di incasso.
TI_REV_REG CONSTANT CHAR(1):='R'; -- Regolarizzazione. Reversale di regolarizzazione
TI_REV_SOS CONSTANT CHAR(1):='S'; -- Sospeso. Reversale a regolamento di sospeso

-- Identificativo di reversale in competenza o a residuo. Tale distinzione rileva solo per le reversali CNR.

--TI_MAN_COMP CONSTANT CHAR(1):='C';
--TI_MAN_RES CONSTANT CHAR(1):='R';

-- Stato del documento autorizzatorio

STATO_AUT_ANN CONSTANT CHAR(1) := 'A'; -- Annullato
STATO_AUT_EME CONSTANT CHAR(1) := 'E'; -- Emesso
STATO_AUT_ESI CONSTANT CHAR(1) := 'P'; -- Pagato/Incassato

-- Stato trasmissione a cassiere del documento autorizzatorio

STATO_AUT_TRASCAS_NODIST CONSTANT CHAR(1) := 'N'; -- Non inserito in distinta
STATO_AUT_TRASCAS_DIST  CONSTANT CHAR(1) := 'I'; -- Inserito in distinta
STATO_AUT_TRASCAS_TRAS  CONSTANT CHAR(1) := 'T'; -- Trasmesso

-- Stato della Reversale. Per ora si ipotizza una gestione omogenea tra testata e righe della reversale.
-- Si porta nell'attributo stato_trasmissione il riferimento alla gestione della distinta cassiere in quanto il processo non è sempre lineare; una reversale può risultare incassata ma non trasmessa.


STATO_REV_ANN CONSTANT CHAR(1) := STATO_AUT_ANN; -- Annullata
STATO_REV_EME CONSTANT CHAR(1) := STATO_AUT_EME; -- Emessa
STATO_REV_PAG CONSTANT CHAR(1) := STATO_AUT_ESI; -- Incassata. La reversale risulta incassata; la reversale passa a questo stato  quando tutte le sue righe risultano incassate

-- Stato della reversale per trasmissione.

STATO_REV_TRASCAS_NODIST CONSTANT CHAR(1) := STATO_AUT_TRASCAS_NODIST; -- Non inserita in distinta
STATO_REV_TRASCAS_DIST  CONSTANT CHAR(1) := STATO_AUT_TRASCAS_DIST; -- Inserita in distinta
STATO_REV_TRASCAS_TRAS  CONSTANT CHAR(1) := STATO_AUT_TRASCAS_TRAS; -- Trasmessa

-- Classificazione per tipologia del mandato

TI_MAN_ACCRED CONSTANT CHAR(1):='A'; -- Accreditamento. Generata in automatico da trasferimento CNR --> CdS. Esiste solo per CdS.
TI_MAN_PAG CONSTANT CHAR(1):='P'; -- Mandato di pagamento. Normale mandato di pagamento
TI_MAN_REG CONSTANT CHAR(1):='R'; -- Regolarizzazione. Mandato di regolarizzazione
TI_MAN_SOS CONSTANT CHAR(1):='S'; -- Sospeso. Mandato a regolamento di sospeso

-- Tipo del mandato in competenza o a residuo. Tale distinzione rileva solo per mandati C.N.R.

TI_MAN_COMP CONSTANT CHAR(1):='C';
TI_MAN_RES CONSTANT CHAR(1):='R';

-- Stato del Mandato. Per ora si ipotizza una gestione omogenea tra testata e righe del mandato.
-- Si porta nell'attributo stato_trasmissione il riferimento alla gestione della distinta cassiere in quanto il processo non è sempre lineare; un mandato può risultare pagato ma non trasmesso.

STATO_MAN_ANN CONSTANT CHAR(1) := STATO_AUT_ANN; -- Annullato
STATO_MAN_EME CONSTANT CHAR(1) := STATO_AUT_EME; -- Emesso
STATO_MAN_PAG CONSTANT CHAR(1) := STATO_AUT_ESI; -- Pagato. Il mandato passa a questo stato solo quando tutte le sue righe sono pagate

-- Stato del Mandato per trasmissione.
STATO_MAN_TRASCAS_NODIST CONSTANT CHAR(1) := STATO_AUT_TRASCAS_NODIST; -- Non inserito in distinta
STATO_MAN_TRASCAS_DIST  CONSTANT CHAR(1) := STATO_AUT_TRASCAS_DIST; -- Inserito in distinta
STATO_MAN_TRASCAS_TRAS  CONSTANT CHAR(1) := STATO_AUT_TRASCAS_TRAS; -- Trasmesso

-- Stato del dettaglio del sospeso/riscontro
STATO_SOSPESO_DET_DEFAULT	CONSTANT CHAR(1) := 'N';
STATO_SOSPESO_DET_ANNULLATO	CONSTANT CHAR(1) := 'A';

-- Prefisso di numerazione dei riscontri effettuati via interfaccia cassiere
-- Deve essere di 4 caratteri in lunghezza
RISC_PREFIX CONSTANT VARCHAR2(4):='XSRC';

-- Functions e Procedures:

-- Funzioni di inserimento MANDATO

 procedure ins_MANDATO (aDest MANDATO%rowtype);
 procedure ins_MANDATO_RIGA (aDest MANDATO_RIGA%rowtype);
 Procedure ins_MANDATO_SIOPE (aDest MANDATO_SIOPE%rowtype);
 procedure ins_MANDATO_TERZO (aDest MANDATO_TERZO%rowtype);

-- Funzioni di inserimento REVERSALE

 procedure ins_REVERSALE (aDest REVERSALE%rowtype);
 procedure ins_REVERSALE_RIGA (aDest REVERSALE_RIGA%rowtype);
 Procedure ins_REVERSALE_SIOPE (aDest REVERSALE_SIOPE%rowtype);
 procedure ins_REVERSALE_TERZO (aDest REVERSALE_TERZO%rowtype);


 procedure ins_ASS_MANDATO_MANDATO (aDest ASS_MANDATO_MANDATO%rowtype);
 procedure ins_ASS_MANDATO_REVERSALE (sTestaMandato mandato%rowtype, num_mandato_iniziale number, sTestaReversale reversale%rowtype);
 procedure ins_SOSPESO_DET_ETR (aDest SOSPESO_DET_ETR%rowtype);
 procedure ins_SOSPESO_DET_USC (aDest SOSPESO_DET_USC%rowtype);
 procedure ins_SOSPESO(aDest SOSPESO%rowtype);
 function nextProgressivoSospeso( aProg varchar2) return varchar2;

 -- Estrae il progressivo da assegnare al riscontro
 function nextProgressivoRiscontro(aCdCds varchar2, aEs number, aTiES char) return varchar2;

-- Funzioni di estrazione descrizione mandato/reversale
 function getDesc(aMan mandato%rowtype) return varchar2;
 function getDesc(aRev reversale%rowtype) return varchar2;

 end;
