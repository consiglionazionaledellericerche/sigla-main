create or replace package CNRCTB020 as
--
-- CNRCTB020 - Package di gestione struttura organizzativa (UNITA_ORGANIZZATIVA/CDR)
-- Date: 14/09/2005
-- Version: 2.10
--
--
-- Dependency: CNRCTB 015 IBMERR 001
--
-- History:
--
-- Date: 02/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 02/10/2001
-- Version: 1.1
-- Aggiunti metodi per recuperare UO e CDS Presidenti dell'area
-- Aggiunte nuove implementazioni degli estrattori del presidente dell'area
--
-- Date: 08/10/2001
-- Version: 1.2
-- Aggiunzione del metodo per il recupero dell'UOCDS a partire dal CDR
--
-- Date: 16/10/2001
-- Version: 1.3
-- Aggiunta la gestione del tipo unita ENTE in UNITA_ORGANIZZATIVA
--
-- Date: 18/10/2001
-- Version: 1.4
-- Estrazione dell'UO che gestisce le spese del personale
-- Estrazione CDR responsabile UO
-- Estrazione UO di afferenza del CDR
--
-- Date: 02/11/2001
-- Version: 1.5
-- Identificazione dell'ente a partire dal codice del CDR senza rilettura della tabella UNITA_ORGANIZZATIVA
--
-- Date: 08/11/2001
-- Version: 1.6
-- Eliminazione dell'esercizio dalla struttura organizzativa
--
-- Date: 14/11/2001
-- Version: 1.7
-- Fix errore
--
-- Date: 16/11/2001
-- Version: 1.8
-- Enforce error handling
--
-- Date: 23/11/2001
-- Version: 1.9
-- Aggiunto metodo di estrazione del CDR ENTE
--
-- Date: 29/11/2001
-- Version: 2.0
-- Fix errore mancanza esercizio fine in insert STO
--
-- Date: 03/12/2001
-- Version: 2.1
-- Aggiunto il metodo di estrazione del CDR del personale
--
-- Date: 10/01/2002
-- Version: 2.2
-- Aggiunta l'estrazione del CDS valido a partire dal codice
--
-- Date: 03/06/2002
-- Version: 2.3
-- Aggiunto metodo di estrazione dell'UO CDS dato il CDS
--
-- Date: 20/06/2002
-- Version: 2.4
-- Aggiornamento della tabella UO per introduzione AREA scientifica
--
-- Date: 24/06/2002
-- Version: 2.5
-- Estrazione dell'UO ente
--
-- Date: 03/07/2002
-- Version: 2.6
-- Aggiunta estrattore UO SAC di versamento dell'IVA
--
-- Date: 18/07/2002
-- Version: 2.7
-- Aggiornamento documentazione
--
-- Date: 09/11/2004
-- Version: 2.8
-- Aggiunta nuova routine per getCDRResponsabileUO
--
-- Date: 15/04/2005
-- Version: 2.9
-- Aggiunta nuova routine per getUOVersCoriTuttaSAC
--
-- Date: 14/09/2005
-- Version: 2.10
-- Modificate le procedure getUOPresidenteArea e getCDSPresidenteArea
-- per gestire l'associazione multipla di Aree a UO
--
-- Date: 23/11/2010
-- Version: 2.11
-- Aggiunta nuova routine getUOVersCoriContoBI per estrarre l'UO responsabile del versamento CORI su CONTO BANCA D'ITALIA

-- Constants:

-- Tipologie di unita organizzative
--
-- Struttura amministrativa centrale
TIPO_SAC CONSTANT VARCHAR2(10) := 'SAC';
-- Istituto
TIPO_IST CONSTANT VARCHAR2(10) := 'IST';
-- Programma Nazionale/Internazionale di Ricerca
TIPO_PNIR CONSTANT VARCHAR2(10) := 'PNIR';
-- Area di ricerca
TIPO_AREA CONSTANT VARCHAR2(10) := 'AREA';
-- Ente CNR
TIPO_ENTE CONSTANT VARCHAR2(10) := 'ENTE';

-- Functions e Procedures:

-- Estrae il CDS ENTE valida in esercizio aEs

 function getCDCDSENTE(aEs number) return unita_organizzativa.CD_unita_organizzativa%Type;

-- Estrae l'UO ENTE valida in esercizio aEs

 function getUOENTE(aEs number) return unita_organizzativa%rowtype;

-- Estrae il CDS valido in aEs con codice aCdCds

 function getCDSValido(aEs number, aCdCds varchar2) return unita_organizzativa%rowtype;

-- Estrae il CDS del SAC valido in aEs

 function getCDSSACValido(aEs number) return unita_organizzativa%rowtype;

-- idem ma solo il codice

 function getcdCDSSACValido(aEs number) return VARCHAR2;

-- Estrae l'UO valida in aEs

 function getUOValida(aEs number,aCdUO varchar2) return unita_organizzativa%rowtype;

-- Estrae il CDR valido in aEs

 function getCDRValido(aEs number,aCdCDR varchar2) return cdr%rowtype;

-- Estrae il CDR di primo livello corrispondente al CDR aCDR

 function getCDRPrimoLivello(aCDR cdr%rowtype) return cdr%rowtype;

-- Estrae il CDR ENTE

 function getCDREnte return cdr%rowtype;

-- Estrae il CDR AREA collegato al CDR aCDR
-- Se l'area non viene trovata viene ritornato cdr%rowtype con codice cdr null

 function getCDRArea(aCDR cdr%rowtype) return cdr%rowtype;

-- Estrae il CDS presidente (via sua UO) dell'area  aCDRArea (o del cds area aCDSArea) per l'esercizio aEs
-- Se non viene trovato viene ritornato unita_organizzativa con codice cds null

 function getUOPresidenteArea(aEs NUMBER, aCDRArea cdr%rowtype) return unita_organizzativa%rowtype;
 function getUOPresidenteArea(aEs NUMBER, aCDSArea unita_organizzativa%rowtype) return unita_organizzativa%rowtype;

-- Estrae l'UO presidente dell'area  aCDRArea (o del cds area aCDSArea) per l'esercizio aEs
-- Se non viene trovato viene ritornato unita_organizzativa con codice uo null

 function getCDSPresidenteArea(aEs NUMBER, aCDRArea cdr%rowtype) return unita_organizzativa%rowtype;
 function getCDSPresidenteArea(aEs NUMBER, aCDSArea unita_organizzativa%rowtype) return unita_organizzativa%rowtype;

-- Dato il cdr, ritorna l'UO CDS di appartenenza

 function getUOCDS(aCDR cdr%rowtype) return unita_organizzativa%rowtype;

-- Dato il cdr, ritorna la UO appartenenza

 function getUO(aCDR cdr%rowtype) return unita_organizzativa%rowtype;

-- Dato il codice cdr, ritorna il CODICE UO appartenenza

 function getCDUO(aCDR cdr.cd_unita_organizzativa%Type) return VARCHAR2;

-- Dato il codice unita_organizzativa, ritorna la descrizione
 Function getdesUO(aCDUO unita_organizzativa.cd_unita_organizzativa%Type) return VARCHAR2;


-- Dato esercizio e codice del cds ritorna l'UO CDS del CDS

 function getUOCDS(aEs number, aCdCds varchar2) return unita_organizzativa%rowtype;


-- Funzione che ritorna TRUE se il CDR appartiene ad UO di tipo ENTE
-- Non rilegge la tabella UNITA_ORGANIZZATIVA ma utilizza la def. del codice dell'ente 9...9
-- dove la lunghezza della stringa dipende dalla tabella lunghezza chiavi

 function isCDRENTE(aCDR cdr%rowtype) return boolean;

-- Estrae l'UO del personale leggendola da configurazione CNR

 function getUOPersonale(aEs number) return unita_organizzativa%rowtype;

-- Estrae il CDR del personale leggendolo da configurazione CNR

 function getCDRPersonale(aEs number) return cdr%rowtype;

-- Estrae il cdr responsabile dell'UO

 function getCDRResponsabileUO(aUO unita_organizzativa%rowtype) return cdr%rowtype;
 function getCDRResponsabileUO(aCdUO VARCHAR2) return cdr%rowtype;

-- Estrae l'UO di afferenza del CDR aCDR

 function getUOAfferenza(aCDR cdr%rowtype) return unita_organizzativa%rowtype;

-- Estrae l'UO del SAC responsabile del versamento CORI accentrato

 function getUOVersCori(aEs number) return unita_organizzativa%rowtype;

-- Estrae l'UO del SAC responsabile del versamento CORI unificato per tutte le UO della SAC

 function getUOVersCoriTuttaSAC(aEs number) return unita_organizzativa%rowtype;

-- Estrae l'UO responsabile del versamento CORI su CONTO BANCA D'ITALIA

 function getUOVersCoriContoBI(aEs number) return unita_organizzativa%rowtype;

-- Estrae l'UO del SAC responsabile del versamento IVA

 function getUOVersIVA(aEs number) return unita_organizzativa%rowtype;

-- ESTRAE LA DESCRIZIONE DEL LIVELLO DA CLASSIFICAZIONE_VOCI
Function getdeslivello(aEs NUMBER, ati_gestione VARCHAR, aliv1 VARCHAR2, aliv2 VARCHAR2, aliv3 VARCHAR2,
                                                 aliv4 VARCHAR2, aliv5 VARCHAR2, aliv6 VARCHAR2,
                                                 aliv7 VARCHAR2) return VARCHAR2;

-- estrae la descrizione del dipartimento
Function GETDESDIPARTIMENTO (aCd_dip VARCHAR2) return VARCHAR2;

-- Procedura di inserimento di unita organizzativa/CDS

 procedure ins_UNITA_ORGANIZZATIVA (aDest UNITA_ORGANIZZATIVA%rowtype);

-- Procedura di inserimento di CDR

 procedure ins_CDR (aDest CDR%rowtype);
 gCDSSACValido unita_organizzativa%Rowtype;
 gEsercizioCDSSACValido Number(4);

 gCDCDSSACValido unita_organizzativa.cd_unita_organizzativa%type;
 gEsercizioCDCDSSACValido Number(4);

 function isUOSAC(aUO varchar2) return boolean;

 function getCdCdrEnte return cdr.cd_centro_responsabilita%type;

 function getCdCDRPersonale(aEs number) return cdr.cd_centro_responsabilita%type;
 function getCdUOVersCori(aEs number) return unita_organizzativa.cd_unita_organizzativa%Type;
 function getCdUOVersCoriTuttaSAC(aEs number) return unita_organizzativa.cd_unita_organizzativa%Type;
 function getCdUOVersCoriContoBI(aEs number) return unita_organizzativa.cd_unita_organizzativa%Type;
 function getCdUOVersIVA(aEs number) return unita_organizzativa.cd_unita_organizzativa%Type;
end;