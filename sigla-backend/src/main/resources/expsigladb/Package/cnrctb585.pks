CREATE OR REPLACE PACKAGE CNRCTB585 AS
--==============================================================================
--
-- CNRCTB585 - Gestione tabelle anticipo/rimborso
--
-- Date: 19/07/2006
-- Version: 1.3
--
-- Dependency:
--
-- History:
--
-- Date: 24/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 25/06/2002
-- Version: 1.1
-- Fix errore
--
-- Date: 30/07/2002
-- Version: 1.2
-- Aggiunti dati terzo
--
-- Date: 19/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Creazione Package.
--
--==============================================================================
--
-- Constants
--

ELEMENTO_VOCE_SPECIALE CONSTANT VARCHAR2(50):='ELEMENTO_VOCE_SPECIALE';
RIMBORSO_ANTICIPI CONSTANT VARCHAR2(100):='RIMBORSO_ANTICIPO';

--
-- Functions e Procedures
--
 procedure ins_RIMBORSO (aDest RIMBORSO%rowtype);

END;
/


CREATE OR REPLACE PACKAGE BODY CNRCTB585 AS
 procedure ins_RIMBORSO (aDest RIMBORSO%rowtype) is
  begin
   insert into RIMBORSO (
     CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO
    ,PG_RIMBORSO
    ,CD_CDS_ANTICIPO
    ,CD_UO_ANTICIPO
    ,ESERCIZIO_ANTICIPO
    ,PG_ANTICIPO
    ,DT_DA_COMPETENZA_COGE
    ,DT_A_COMPETENZA_COGE
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,DT_REGISTRAZIONE
    ,DS_RIMBORSO
    ,TI_ANAGRAFICO
    ,CD_TERZO
    ,CD_TERZO_UO_CDS
    ,CD_MODALITA_PAG_UO_CDS
    ,PG_BANCA_UO_CDS
    ,IM_RIMBORSO
    ,STATO_COFI
    ,STATO_COGE
    ,STATO_COAN
    ,TI_ASSOCIATO_MANREV
    ,DT_CANCELLAZIONE
    ,CD_CDS_ACCERTAMENTO
    ,ESERCIZIO_ACCERTAMENTO
    ,ESERCIZIO_ORI_ACCERTAMENTO
    ,PG_ACCERTAMENTO
    ,PG_ACCERTAMENTO_SCADENZARIO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,RAGIONE_SOCIALE
    ,NOME
    ,COGNOME
    ,CODICE_FISCALE
    ,PARTITA_IVA
    ,CD_TERMINI_PAG
    ,CD_MODALITA_PAG
    ,PG_BANCA
   ) values (
     aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ESERCIZIO
    ,aDest.PG_RIMBORSO
    ,aDest.CD_CDS_ANTICIPO
    ,aDest.CD_UO_ANTICIPO
    ,aDest.ESERCIZIO_ANTICIPO
    ,aDest.PG_ANTICIPO
    ,aDest.DT_DA_COMPETENZA_COGE
    ,aDest.DT_A_COMPETENZA_COGE
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.DT_REGISTRAZIONE
    ,aDest.DS_RIMBORSO
    ,aDest.TI_ANAGRAFICO
    ,aDest.CD_TERZO
    ,aDest.CD_TERZO_UO_CDS
    ,aDest.CD_MODALITA_PAG_UO_CDS
    ,aDest.PG_BANCA_UO_CDS
    ,aDest.IM_RIMBORSO
    ,aDest.STATO_COFI
    ,aDest.STATO_COGE
    ,aDest.STATO_COAN
    ,aDest.TI_ASSOCIATO_MANREV
    ,aDest.DT_CANCELLAZIONE
    ,aDest.CD_CDS_ACCERTAMENTO
    ,aDest.ESERCIZIO_ACCERTAMENTO
    ,aDest.ESERCIZIO_ORI_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO_SCADENZARIO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.RAGIONE_SOCIALE
    ,aDest.NOME
    ,aDest.COGNOME
    ,aDest.CODICE_FISCALE
    ,aDest.PARTITA_IVA
    ,aDest.CD_TERMINI_PAG
    ,aDest.CD_MODALITA_PAG
    ,aDest.PG_BANCA
    );
 end;
END;
/


