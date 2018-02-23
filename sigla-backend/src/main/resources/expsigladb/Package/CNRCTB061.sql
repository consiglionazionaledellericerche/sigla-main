--------------------------------------------------------
--  DDL for Package CNRCTB061
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB061" as
--
-- CNRCTB061 - Package di gestione dello scarico dei costi del personale su PDGP e PDG
--
-- Date: 09/11/2006
-- Version: 1.4
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
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Constants:
--
-- Stato di scarico del dettaglio del personale su piano di gestione
--
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

-- Scarica sul PDGP di aCdCdr i costi leggendo i dati da COSTO_DEL_DIPENDENTE
-- e utilizzando la configurazione impostata in ASS_CDP_LA per la ripartizione dei costi sulle linee di attivita
-- Il cdr di codice aCdCdr e un RUO
--
--  Infine mette a 'SP' lo stato di tutti gli ASS_CDP_LA per i cdr dell'uo del
--  cdr specficato e imposta la data di scarico alla data odierna

-- Parametri:
-- aEsercizio -> anno di esercizio
-- aCdCdr -> Codice del centro di responsabilita che scarica CDP
-- aUser -> Utente che effettua l'operazione

 procedure scaricaCDPSuPdgP(aEsercizio number, aCdCdr varchar2, aUser varchar2);


-- Annulla uno scarico su PDGP effettuato precedentemente per il CDR RUO di codice aCdCdr
--
-- Il cdr di codice cd_cdr e un RUO
--
-- Riporta a 'I' lo stato degli ASS_CDP_LA per i CDR che stanno nell'UO del cdr specificato
--
-- Parametri:
-- aEsercizio -> anno di esercizio
-- aCdCdr -> Codice del centro di responsabilita che ha scaricato CDP
-- aUser -> Utente che effettua l'operazione

 procedure annullaCDPSuPdgP(aEsercizio number, aCdCdr varchar2, aUser varchar2);

-- Scarica sul PDG di aCdCdr i costi leggendo i dati da COSTO_DEL_DIPENDENTE
-- e utilizzando la configurazione impostata in ASS_CDP_LA per la ripartizione dei costi sulle linee di attivita
-- Il cdr di codice aCdCdr e un RUO
--
--  Infine mette a 'S' lo stato di tutti gli ASS_CDP_LA per i cdr dell'uo del
--  cdr specficato e imposta la data di scarico alla data odierna

-- Parametri:
-- aEsercizio -> anno di esercizio
-- aCdCdr -> Codice del centro di responsabilita che scarica CDP
-- aUser -> Utente che effettua l'operazione

 procedure scaricaCDPSuPdg(aEsercizio number, aCdCdr varchar2, aUser varchar2);

-- Annulla uno scarico su PDG effettuato precedentemente per il CDR RUO di codice aCdCdr
--
-- Il cdr di codice cd_cdr e un RUO
--
-- Riporta a 'SP' lo stato degli ASS_CDP_LA per i CDR che stanno nell'UO del cdr specificato
--
-- Parametri:
-- aEsercizio -> anno di esercizio
-- aCdCdr -> Codice del centro di responsabilita che ha scaricato CDP
-- aUser -> Utente che effettua l'operazione

 procedure annullaCDPSuPdg(aEsercizio number, aCdCdr varchar2, aUser varchar2);

End;
