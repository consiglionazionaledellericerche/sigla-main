CREATE OR REPLACE package CNRCTB038 as
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
-- Si porta nell'attributo stato_trasmissione il riferimento alla gestione della distinta cassiere in quanto il processo non ? sempre lineare; una reversale pu? risultare incassata ma non trasmessa.


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
-- Si porta nell'attributo stato_trasmissione il riferimento alla gestione della distinta cassiere in quanto il processo non ? sempre lineare; un mandato pu? risultare pagato ma non trasmesso.

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


CREATE OR REPLACE package body CNRCTB038 is


 function getDesc(aMan mandato%rowtype) return varchar2 is
 begin
  return ' N.: '||aMan.pg_mandato||' Cds: '||aMan.cd_cds||' Es.: '||aMan.esercizio;
 end;

 function getDesc(aRev reversale%rowtype) return varchar2 is
 begin
  return ' N.: '||aRev.pg_reversale||' Cds: '||aRev.cd_cds||' Es.: '||aRev.esercizio;
 end;

procedure ins_MANDATO (aDest MANDATO%rowtype) is
  begin
   insert into MANDATO (
     CD_CDS
    ,ESERCIZIO
    ,PG_MANDATO
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,CD_TIPO_DOCUMENTO_CONT
    ,TI_MANDATO
    ,TI_COMPETENZA_RESIDUO
    ,DS_MANDATO
    ,STATO
    ,DT_EMISSIONE
    ,DT_TRASMISSIONE
    ,DT_PAGAMENTO
    ,DT_ANNULLAMENTO
    ,IM_MANDATO
    ,IM_PAGATO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
	,STATO_TRASMISSIONE
	,DT_RITRASMISSIONE
    ,STATO_COGE
    ,IM_RITENUTE
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_MANDATO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.CD_TIPO_DOCUMENTO_CONT
    ,aDest.TI_MANDATO
    ,aDest.TI_COMPETENZA_RESIDUO
    ,aDest.DS_MANDATO
    ,aDest.STATO
    ,aDest.DT_EMISSIONE
    ,aDest.DT_TRASMISSIONE
    ,aDest.DT_PAGAMENTO
    ,aDest.DT_ANNULLAMENTO
    ,aDest.IM_MANDATO
    ,aDest.IM_PAGATO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
	,aDest.STATO_TRASMISSIONE
	,aDest.DT_RITRASMISSIONE
	,aDest.STATO_COGE
	,aDest.IM_RITENUTE
    );
 end;

 procedure ins_MANDATO_RIGA (aDest MANDATO_RIGA%rowtype) is
  begin
   insert into MANDATO_RIGA (
     CD_CDS
    ,ESERCIZIO
    ,PG_MANDATO
    ,ESERCIZIO_OBBLIGAZIONE
    ,ESERCIZIO_ORI_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE_SCADENZARIO
    ,CD_CDS_DOC_AMM
    ,CD_UO_DOC_AMM
    ,ESERCIZIO_DOC_AMM
    ,CD_TIPO_DOCUMENTO_AMM
    ,PG_DOC_AMM
    ,DS_MANDATO_RIGA
    ,STATO
    ,CD_TERZO
    ,PG_BANCA
    ,CD_MODALITA_PAG
    ,IM_MANDATO_RIGA
    ,FL_PGIRO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
	,IM_RITENUTE_RIGA
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_MANDATO
    ,aDest.ESERCIZIO_OBBLIGAZIONE
    ,aDest.ESERCIZIO_ORI_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE_SCADENZARIO
    ,aDest.CD_CDS_DOC_AMM
    ,aDest.CD_UO_DOC_AMM
    ,aDest.ESERCIZIO_DOC_AMM
    ,aDest.CD_TIPO_DOCUMENTO_AMM
    ,aDest.PG_DOC_AMM
    ,aDest.DS_MANDATO_RIGA
    ,aDest.STATO
    ,aDest.CD_TERZO
    ,aDest.PG_BANCA
    ,aDest.CD_MODALITA_PAG
    ,aDest.IM_MANDATO_RIGA
    ,aDest.FL_PGIRO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.IM_RITENUTE_RIGA
    );
 end;

Procedure ins_MANDATO_SIOPE (aDest MANDATO_SIOPE%rowtype) Is
Begin
 Insert Into MANDATO_SIOPE (
        CD_CDS,
        ESERCIZIO,
        PG_MANDATO,
        ESERCIZIO_OBBLIGAZIONE,
        ESERCIZIO_ORI_OBBLIGAZIONE,
        PG_OBBLIGAZIONE,
        PG_OBBLIGAZIONE_SCADENZARIO,
        CD_CDS_DOC_AMM,
        CD_UO_DOC_AMM,
        ESERCIZIO_DOC_AMM,
        CD_TIPO_DOCUMENTO_AMM,
        PG_DOC_AMM,
        ESERCIZIO_SIOPE,
        TI_GESTIONE,
        CD_SIOPE,
        IMPORTO,
        UTCR,
        DACR,
        UTUV,
        DUVA,
        PG_VER_REC)
 Values
    (aDest.CD_CDS,
     aDest.ESERCIZIO,
     aDest.PG_MANDATO,
     aDest.ESERCIZIO_OBBLIGAZIONE,
     aDest.ESERCIZIO_ORI_OBBLIGAZIONE,
     aDest.PG_OBBLIGAZIONE,
     aDest.PG_OBBLIGAZIONE_SCADENZARIO,
     aDest.CD_CDS_DOC_AMM,
     aDest.CD_UO_DOC_AMM,
     aDest.ESERCIZIO_DOC_AMM,
     aDest.CD_TIPO_DOCUMENTO_AMM,
     aDest.PG_DOC_AMM,
     aDest.ESERCIZIO_SIOPE,
     aDest.TI_GESTIONE,
     aDest.CD_SIOPE,
     aDest.IMPORTO,
     aDest.UTCR,
     aDest.DACR,
     aDest.UTUV,
     aDest.DUVA,
     aDest.PG_VER_REC);
End;

 procedure ins_MANDATO_TERZO (aDest MANDATO_TERZO%rowtype) is
  begin
   insert into MANDATO_TERZO (
     CD_CDS
    ,ESERCIZIO
    ,PG_MANDATO
    ,CD_TERZO
    ,CD_TIPO_BOLLO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_MANDATO
    ,aDest.CD_TERZO
    ,aDest.CD_TIPO_BOLLO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;
 procedure ins_REVERSALE (aDest REVERSALE%rowtype) is
  begin
   insert into REVERSALE (
     CD_CDS
    ,ESERCIZIO
    ,PG_REVERSALE
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,CD_TIPO_DOCUMENTO_CONT
    ,TI_REVERSALE
    ,TI_COMPETENZA_RESIDUO
    ,DS_REVERSALE
    ,STATO
    ,DT_EMISSIONE
    ,DT_TRASMISSIONE
    ,DT_INCASSO
    ,DT_ANNULLAMENTO
    ,IM_REVERSALE
    ,IM_INCASSATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
	,STATO_TRASMISSIONE
	,DT_RITRASMISSIONE
	,STATO_COGE
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_REVERSALE
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.CD_TIPO_DOCUMENTO_CONT
    ,aDest.TI_REVERSALE
    ,aDest.TI_COMPETENZA_RESIDUO
    ,aDest.DS_REVERSALE
    ,aDest.STATO
    ,aDest.DT_EMISSIONE
    ,aDest.DT_TRASMISSIONE
    ,aDest.DT_INCASSO
    ,aDest.DT_ANNULLAMENTO
    ,aDest.IM_REVERSALE
    ,aDest.IM_INCASSATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
	,aDest.STATO_TRASMISSIONE
    ,aDest.DT_RITRASMISSIONE
    ,aDest.STATO_COGE
    );
 end;
 procedure ins_REVERSALE_RIGA (aDest REVERSALE_RIGA%rowtype) is
  begin
   insert into REVERSALE_RIGA (
     CD_CDS
    ,ESERCIZIO
    ,PG_REVERSALE
    ,ESERCIZIO_ACCERTAMENTO
    ,ESERCIZIO_ORI_ACCERTAMENTO
    ,PG_ACCERTAMENTO
    ,PG_ACCERTAMENTO_SCADENZARIO
    ,CD_CDS_DOC_AMM
    ,CD_UO_DOC_AMM
    ,ESERCIZIO_DOC_AMM
    ,CD_TIPO_DOCUMENTO_AMM
    ,PG_DOC_AMM
    ,DS_REVERSALE_RIGA
    ,STATO
    ,CD_TERZO
    ,CD_TERZO_UO
    ,PG_BANCA
    ,CD_MODALITA_PAG
    ,IM_REVERSALE_RIGA
    ,FL_PGIRO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_REVERSALE
    ,aDest.ESERCIZIO_ACCERTAMENTO
    ,aDest.ESERCIZIO_ORI_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO_SCADENZARIO
    ,aDest.CD_CDS_DOC_AMM
    ,aDest.CD_UO_DOC_AMM
    ,aDest.ESERCIZIO_DOC_AMM
    ,aDest.CD_TIPO_DOCUMENTO_AMM
    ,aDest.PG_DOC_AMM
    ,aDest.DS_REVERSALE_RIGA
    ,aDest.STATO
    ,aDest.CD_TERZO
    ,aDest.CD_TERZO_UO
    ,aDest.PG_BANCA
    ,aDest.CD_MODALITA_PAG
    ,aDest.IM_REVERSALE_RIGA
    ,aDest.FL_PGIRO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

Procedure ins_REVERSALE_SIOPE (aDest REVERSALE_SIOPE%rowtype) Is
Begin
 Insert Into REVERSALE_SIOPE (
        CD_CDS,
        ESERCIZIO,
        PG_REVERSALE,
        ESERCIZIO_ACCERTAMENTO,
        ESERCIZIO_ORI_ACCERTAMENTO,
        PG_ACCERTAMENTO,
        PG_ACCERTAMENTO_SCADENZARIO,
        CD_CDS_DOC_AMM,
        CD_UO_DOC_AMM,
        ESERCIZIO_DOC_AMM,
        CD_TIPO_DOCUMENTO_AMM,
        PG_DOC_AMM,
        esercizio_siope,
        TI_GESTIONE,
        CD_SIOPE,
        IMPORTO,
        UTCR,
        DACR,
        UTUV,
        DUVA,
        PG_VER_REC)
 Values
    (aDest.CD_CDS,
     aDest.ESERCIZIO,
     aDest.PG_REVERSALE,
     aDest.ESERCIZIO_ACCERTAMENTO,
     aDest.ESERCIZIO_ORI_ACCERTAMENTO,
     aDest.PG_ACCERTAMENTO,
     aDest.PG_ACCERTAMENTO_SCADENZARIO,
     aDest.CD_CDS_DOC_AMM,
     aDest.CD_UO_DOC_AMM,
     aDest.ESERCIZIO_DOC_AMM,
     aDest.CD_TIPO_DOCUMENTO_AMM,
     aDest.PG_DOC_AMM,
     aDest.esercizio_siope,
     aDest.TI_GESTIONE,
     aDest.CD_SIOPE,
     aDest.IMPORTO,
     aDest.UTCR,
     aDest.DACR,
     aDest.UTUV,
     aDest.DUVA,
     aDest.PG_VER_REC);
End;

 Procedure ins_REVERSALE_TERZO (aDest REVERSALE_TERZO%rowtype) is
  begin
   insert into REVERSALE_TERZO (
     CD_CDS
    ,ESERCIZIO
    ,PG_REVERSALE
    ,CD_TERZO
    ,CD_TIPO_BOLLO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_REVERSALE
    ,aDest.CD_TERZO
    ,aDest.CD_TIPO_BOLLO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_ASS_MANDATO_MANDATO (aDest ASS_MANDATO_MANDATO%rowtype) is
  begin
   insert into ASS_MANDATO_MANDATO (
     CD_CDS
    ,ESERCIZIO
    ,PG_MANDATO
    ,CD_CDS_COLL
    ,ESERCIZIO_COLL
    ,PG_MANDATO_COLL
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_MANDATO
    ,aDest.CD_CDS_COLL
    ,aDest.ESERCIZIO_COLL
    ,aDest.PG_MANDATO_COLL
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_ASS_MANDATO_REVERSALE (sTestaMandato mandato%rowtype, num_mandato_iniziale number, sTestaReversale reversale%rowtype) is
 begin

 	  insert into ASS_MANDATO_REVERSALE(
	  		 	  CD_CDS_MANDATO
				  ,ESERCIZIO_MANDATO
				  ,PG_MANDATO
				  ,CD_CDS_REVERSALE
				  ,ESERCIZIO_REVERSALE
				  ,PG_REVERSALE
				  ,TI_ORIGINE
				  ,DACR
				  ,UTCR
				  ,DUVA
				  ,UTUV
				  ,PG_VER_REC )
 	  values (sTestaMandato.CD_CDS
	  		 ,sTestaMandato.ESERCIZIO
			 ,num_mandato_iniziale
			 ,sTestaReversale.CD_CDS
			 ,sTestaReversale.ESERCIZIO
			 ,sTestaReversale.PG_REVERSALE
			 ,'S'
			 ,sTestaMandato.DACR
			 ,sTestaMandato.UTCR
			 ,sTestaMandato.DUVA
			 ,sTestaMandato.UTUV
			 ,1);
 end;

  procedure ins_SOSPESO_DET_USC (aDest SOSPESO_DET_USC%rowtype) is
  begin
   insert into SOSPESO_DET_USC (
     TI_SOSPESO_RISCONTRO
    ,CD_SOSPESO
    ,IM_ASSOCIATO
    ,STATO
    ,DACR
    ,UTCR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,CD_CDS
    ,ESERCIZIO
    ,PG_MANDATO
    ,TI_ENTRATA_SPESA
    ,CD_CDS_MANDATO
   ) values (
     aDest.TI_SOSPESO_RISCONTRO
    ,aDest.CD_SOSPESO
    ,aDest.IM_ASSOCIATO
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_MANDATO
    ,aDest.TI_ENTRATA_SPESA
    ,aDest.CD_CDS_MANDATO
    );
  end;

  procedure ins_SOSPESO_DET_ETR (aDest SOSPESO_DET_ETR%rowtype) is
  begin
   insert into SOSPESO_DET_ETR (
     DACR
    ,UTCR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,CD_CDS
    ,ESERCIZIO
    ,PG_REVERSALE
    ,TI_ENTRATA_SPESA
    ,TI_SOSPESO_RISCONTRO
    ,CD_SOSPESO
    ,IM_ASSOCIATO
    ,STATO
    ,CD_CDS_REVERSALE
   ) values (
     aDest.DACR
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.PG_REVERSALE
    ,aDest.TI_ENTRATA_SPESA
    ,aDest.TI_SOSPESO_RISCONTRO
    ,aDest.CD_SOSPESO
    ,aDest.IM_ASSOCIATO
    ,aDest.STATO
    ,aDest.CD_CDS_REVERSALE
    );
 end;

 procedure ins_SOSPESO (aDest SOSPESO%rowtype) is
  begin
   insert into SOSPESO (
     CD_PROPRIO_SOSPESO
    ,IM_ASS_MOD_1210
    ,CD_SOSPESO_PADRE
    ,CD_CDS
    ,ESERCIZIO
    ,TI_ENTRATA_SPESA
    ,TI_SOSPESO_RISCONTRO
    ,CD_SOSPESO
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,DT_REGISTRAZIONE
    ,DS_ANAGRAFICO
    ,CAUSALE
    ,TI_CC_BI
    ,FL_STORNATO
    ,IM_SOSPESO
    ,IM_ASSOCIATO
    ,STATO_SOSPESO
    ,DACR
    ,UTCR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.CD_PROPRIO_SOSPESO
    ,aDest.IM_ASS_MOD_1210
    ,aDest.CD_SOSPESO_PADRE
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.TI_ENTRATA_SPESA
    ,aDest.TI_SOSPESO_RISCONTRO
    ,aDest.CD_SOSPESO
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DS_ANAGRAFICO
    ,aDest.CAUSALE
    ,aDest.TI_CC_BI
    ,aDest.FL_STORNATO
    ,aDest.IM_SOSPESO
    ,aDest.IM_ASSOCIATO
    ,aDest.STATO_SOSPESO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

 function nextProgressivoSospeso( aProg varchar2) return varchar2 is
  aNumber number;
 begin
  aNumber := to_number( aProg );
  aNumber := aNumber + 1;
  return lpad( to_char(aNumber), 3, '0');
 end;

 function nextProgressivoRiscontro(aCdCds varchar2, aEs number, aTiES char) return varchar2 is
  aNum number;
  aCurr varchar2(24);
 begin
  if length(RISC_PREFIX) != 4 then
   IBMERR001.RAISE_ERR_GENERICO('La lunghezza del prefisso di numerazione riscontri deve essere di 4 caratteri');
  end if;
  begin
   select cd_sospeso into aCurr from sospeso a where
        cd_cds = aCdCds
    and esercizio = aEs
    and ti_entrata_spesa = aTiEs
    and ti_sospeso_riscontro = 'R'
    and cd_sospeso like RISC_PREFIX||'%'
    and cd_sospeso = (select max(cd_sospeso) from sospeso where
        cd_cds = a.cd_cds
    and esercizio = a.esercizio
    and ti_entrata_spesa = a.ti_entrata_spesa
    and ti_sospeso_riscontro = a.ti_sospeso_riscontro
    and cd_sospeso like RISC_PREFIX||'%')
    for update nowait;
   if length(aCurr) != 14 then
    IBMERR001.RAISE_ERR_GENERICO('Esistono numerazioni di riscontro non compatibili con la numerazione automatica generata dall''interfaccia di riscontro automatica (la lunghezza del codice sospeso ? diversa da 14 caratteri)');
   end if;
   aNum:=substr(aCurr,5,10);
   return RISC_PREFIX||lpad(aNum+1,10,'0');
  exception when NO_DATA_FOUND then
   return RISC_PREFIX||lpad('0',10,'0');
  end;
 end;
end;


