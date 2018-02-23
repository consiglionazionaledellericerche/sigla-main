--------------------------------------------------------
--  DDL for Package CNRCTB061_LELLO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB061_LELLO" as
--
-- CNRCTB061_lello - Package di gestione dello scarico dei costi del personale su PDGP e PDG
--
-- Date: 27/03/2006
-- Version: 1.3
--
-- Dependency: CNRCTB000/020/010/050 IBMERR 001
--
-- History:
--
-- Date: 25/10/2005
-- Version: 1.0
-- Creazione
--
-- Date: 02/12/2005
-- Version: 1.1
-- Modificato per gestire l'annullamento dei rotti
--
-- Date: 19/12/2005
-- Version: 1.2
-- Aggiunta la procedure scaricaPdgPSuPdg per gestire il rilascio del PDGP sul PDG
--
-- Date: 27/03/2006
-- Version: 1.3
-- Gestito lo scarico di una UO CDS
--
-- Constants:
--
-- Stato di scarico del dettaglio del personale su piano di gestione
--
contaaASSCDPLA NUMBER := 0;
contatabModuloSpeseGest NUMBER := 0;
diff NUMBER := 0;
trovato Boolean;

STATO_CDP_NON_SCARICATO CONSTANT VARCHAR2(5) := 'I';
STATO_CDP_SCARICATO CONSTANT VARCHAR2(5) := 'S';
STATO_CDP_PDGP_SCARICATO CONSTANT VARCHAR2(5) := 'SP';
--
-- Origine del dettaglio del piano di gestione gestionale
--
ORIGINE_UTENTE CONSTANT VARCHAR2(5) := 'DIR';
ORIGINE_PREVISIONE CONSTANT VARCHAR2(5) := 'PRE';
ORIGINE_PROPOSTA_VARIAZIONE CONSTANT VARCHAR2(5) := 'PDV';
ORIGINE_VARIAZIONE_APPROVATA CONSTANT VARCHAR2(5) := 'APP';
--
-- Categoria del dettaglio del piano di gestione gestionale
--
CATEGORIA_DIRETTA CONSTANT VARCHAR2(5) := 'DIR';
CATEGORIA_SCARICO CONSTANT VARCHAR2(5) := 'SCR';
CATEGORIA_STIPENDI CONSTANT VARCHAR2(5) := 'STI';
--
-- Dominio per individuare tabella COSTI/SPESE
--
TAB_COSTI CONSTANT VARCHAR2(5):='C';
TAB_SPESE CONSTANT VARCHAR2(5):='S';
--
-- Codici di DEFAULT
--
DEFAULT_ID_CLASSIFICAZIONE CONSTANT NUMBER(1):=0;
DEFAULT_CD_CDS_AREA CONSTANT VARCHAR2(5):='XXXXX';
--
CDP_TI_RAPP_DETERMINATO CONSTANT VARCHAR2(10) :='DET';
CDP_TI_RAPP_INDETERMINATO CONSTANT VARCHAR2(10) :='IND';
--
--
-- Stato di scarico del dettaglio del personale su piano di gestione preliminare

-- Legge tutti i PDGP RUO/NRUO del CDR RUO aCDRRUO NO LOCK POSSIBILE
--
-- aEs -> Esercizio contabile
-- aCdCDRRUO -> Codice del centro di responsabilita RUO

 cursor PDGP_CON_CONFIG_SCR(aEs number, aCdCDRRUO VARCHAR2, aUO unita_organizzativa%Rowtype) RETURN PDG_MODULO%ROWTYPE is (
   Select a.* from PDG_MODULO a, V_PDGP_CDR_RUO_NRUO b
   Where b.esercizio = aEs
   And   b.cd_cdr_root = aCdCDRRUO
   And  (aUO.cd_tipo_unita != CNRCTB020.TIPO_SAC Or
 	 b.cd_unita_organizzativa = aUO.cd_unita_organizzativa)
   And   a.esercizio = aEs
   And   a.cd_centro_responsabilita = b.cd_centro_responsabilita
   And   Exists (select 1 from ASS_CDP_LA
                 Where esercizio = aEs
                 And   mese=0
                 And   cd_centro_responsabilita = b.cd_centro_responsabilita) );

 -- Functions e Procedures:

 Procedure scaricaCDPSuPdgLello(aEsercizio number, aCdCdr varchar2, aUser varchar2);

End;
