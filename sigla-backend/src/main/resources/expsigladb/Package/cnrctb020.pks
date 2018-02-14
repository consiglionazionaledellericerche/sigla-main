CREATE OR REPLACE package CNRCTB020 as
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

 function getUOPersonale return unita_organizzativa%rowtype;

-- Estrae il CDR del personale leggendolo da configurazione CNR

 function getCDRPersonale return cdr%rowtype;

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
end;


CREATE OR REPLACE package body CNRCTB020 is


 function getCDCDSENTE(aEs number) return unita_organizzativa.CD_unita_organizzativa%Type Is
  aUO unita_organizzativa.CD_unita_organizzativa%Type;
 begin
   select CD_unita_organizzativa
   into aUO from unita_organizzativa where
	    fl_cds = 'Y'
    and cd_tipo_unita = TIPO_ENTE
	and esercizio_inizio <= aEs
	and esercizio_fine >= aEs;
  return aUO;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('CDS ente valido non trovato in esercizio:'||aEs);
 end;

function getCdCdrEnte return cdr.cd_centro_responsabilita%Type Is
  aCDREnte cdr%rowtype;
begin
   aCDREnte:=getCdrEnte;
   return aCDREnte.cd_centro_responsabilita;
end;

 Function getUOENTE(aEs number) return unita_organizzativa%rowtype is
  aUO unita_organizzativa%rowtype;
 begin
   select * into aUO from unita_organizzativa where
	    fl_cds = 'N'
	and to_number(cd_proprio_unita)=0
    and cd_tipo_unita = TIPO_ENTE
	and esercizio_inizio <= aEs
	and esercizio_fine >= aEs;
  return aUO;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('UO ente valida non trovata in esercizio:'||aEs);
 end;

 function getCDSValido(aEs number, aCdCds varchar2) return unita_organizzativa%rowtype is
  aCDS unita_organizzativa%rowtype;
 begin
  begin
   select * into aCDS from unita_organizzativa a where
	    a.fl_cds = 'Y'
    and a.cd_unita_organizzativa = aCdCds
    and exists (select 1 from v_unita_organizzativa_valida where
	     esercizio = aEs
	 and cd_unita_organizzativa = a.cd_unita_organizzativa);
   return aCDS;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('CDS non valido o non definito in esercizio '||aEs);
  end;
 end;

 function getCDSSACValido(aEs number) return unita_organizzativa%rowtype is
  aCDS unita_organizzativa%rowtype;
 begin
  if (gEsercizioCDSSACValido Is Null Or gEsercizioCDSSACValido != aEs) Then
    gEsercizioCDSSACValido := aEs;
    begin
         select * into aCDS from unita_organizzativa where
              aEs >= esercizio_inizio
          and (
              esercizio_fine is null
         	 or aEs <= esercizio_fine
          )
      	and cd_tipo_unita = TIPO_SAC
      	and fl_cds = 'Y';
     gCDSSACValido := aCDS;
     return gCDSSACValido;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('CDS SAC non valido o non definito in esercizio '||aEs);
    end;
  Else
    Return gCDSSACValido;
  End If;
 end;

 Function getcdCDSSACValido(aEs number) Return VARCHAR2 is
  aCDS unita_organizzativa%rowtype;
 begin
  if (gEsercizioCDCDSSACValido Is Null Or gEsercizioCDCDSSACValido != aEs) Then
    gEsercizioCDCDSSACValido := aEs;
    begin
     select * into aCDS from unita_organizzativa where
          aEs >= esercizio_inizio
      and (
          esercizio_fine is null
     	 or aEs <= esercizio_fine
      )
	  and cd_tipo_unita = TIPO_SAC
	  and fl_cds = 'Y';
     gCDCDSSACValido := aCDS.cd_unita_organizzativa;
     return gCDCDSSACValido;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('CDS SAC non valido o non definito in esercizio '||aEs);
    end;
  Else
    Return gCDCDSSACValido;
  End If;
 end;

 function getUOValida(aEs number,aCdUO varchar2) return unita_organizzativa%rowtype is
  aUO unita_organizzativa%rowtype;
 begin
  begin
   select * into aUO from unita_organizzativa where
        aEs >= esercizio_inizio
    and (
        esercizio_fine is null
   	 or aEs <= esercizio_fine
    )
	and cd_unita_organizzativa = aCdUO;
   return aUO;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Unita organizzativa '||aCdUO||'non valida o non definita in esercizio '||aEs);
  end;
 end;


 function getCDRValido(aEs number,aCdCDR varchar2) return cdr%rowtype is
  aCDR cdr%rowtype;
 begin
  begin
   select * into aCDR from cdr where
        aEs >= esercizio_inizio
    and (
        esercizio_fine is null
   	 or aEs <= esercizio_fine
    )
	and cd_centro_responsabilita = aCdCDR;
   return aCDR;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('CDR '||aCdCDR||'non valido o non definito in esercizio '||aEs);
  end;
 end;



 function getCDRPrimoLivello(aCDR cdr %rowtype) return cdr%rowtype is
  aCDRPrimo cdr%rowtype;
 begin
  if(aCDR.livello = 1) then return aCDR; end if;
  begin
   select * into aCDRPrimo from cdr where
        cd_centro_responsabilita = aCDR.cd_cdr_afferenza;
   return aCDRPrimo;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Cdr di primo livello del CDR '||aCDR.cd_centro_responsabilita||' non trovato!');
  end;
 end;

--P.R. Da Verificare dopo l'inserimento dell'area di afferenza sul PDG
 function getCDRArea(aCDR cdr %rowtype) return cdr%rowtype is
  aCDRArea cdr%rowtype;
 begin
  begin
   select d.* into aCDRArea from
    unita_organizzativa a, -- unita organizzativa di aCDR
	unita_organizzativa b, -- cds AREA
	unita_organizzativa c, -- uo cds AREA
	cdr d -- cdr responsabile del CDS AREA
   where
        a.cd_unita_organizzativa = aCDR.cd_unita_organizzativa
    and b.cd_unita_organizzativa = a.cd_area_ricerca
    and c.cd_unita_padre = b.cd_unita_organizzativa
	and c.fl_uo_cds = 'Y'
    and d.cd_unita_organizzativa = c.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   null;
  end;
  return aCDRArea;
 end;

 function getCDREnte return cdr%rowtype is
  aCDREnte cdr%rowtype;
 begin
  begin
   select b.* into aCDREnte from
    unita_organizzativa a, -- unita organizzativa ENTE
	cdr b                  -- cdr responsabile ENTE
   where
        a.cd_unita_organizzativa = b.cd_unita_organizzativa
	and a.cd_tipo_unita = CNRCTB020.TIPO_ENTE;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('CDR Ente non trovato!');
  end;
  return aCDREnte;
 end;

 function getUOPresidenteArea(aEs NUMBER, aCDSArea unita_organizzativa %rowtype) return unita_organizzativa%rowtype is
  aUOPresArea unita_organizzativa%rowtype;
 begin
  begin
   select b.* into aUOPresArea From
    ass_uo_area a,
    unita_organizzativa b
   Where a.esercizio = aEs
   And   a.cd_area_ricerca = aCDSArea.cd_unita_organizzativa
   And   a.fl_presidente_area = 'Y'
   And   a.cd_unita_organizzativa = b.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   null;
  end;
  return aUOPresArea;
 end;


 function getCDSPresidenteArea(aEs NUMBER, aCDSArea unita_organizzativa %rowtype) return unita_organizzativa%rowtype is
  aUOPresArea unita_organizzativa%rowtype;
  aCDSPresArea unita_organizzativa%rowtype;
 begin
  aUOPresArea:=getUOPresidenteArea(aEs, aCDSArea);
  if aUOPresArea.cd_unita_organizzativa is not null then
   begin
    select * into aCDSPresArea from
     unita_organizzativa
    where
         cd_unita_organizzativa = aUOPresArea.cd_unita_padre
 	 and fl_cds = 'Y';
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;
  return aCDSPresArea;
 end;

 function getUOPresidenteArea(aEs NUMBER, aCDRArea cdr %rowtype) return unita_organizzativa%rowtype is
  aUOPresArea unita_organizzativa%rowtype;
 begin
  begin
   select c.* into aUOPresArea from
	unita_organizzativa a, -- uo dell'area
	ass_uo_area b, -- aree associate alla uo
    unita_organizzativa c  -- uo presidente dell'area
   Where a.cd_unita_organizzativa = aCDRArea.cd_unita_organizzativa
   And   b.esercizio = aEs
   And   b.cd_area_ricerca = a.cd_unita_padre
   And   b.fl_presidente_area = 'Y'
   And   b.cd_unita_organizzativa = c.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   null;
  end;
  return aUOPresArea;
 end;

 function getCDSPresidenteArea(aEs NUMBER, aCDRArea cdr %rowtype) return unita_organizzativa%rowtype is
  aUOPresArea unita_organizzativa%rowtype;
  aCDSPresArea unita_organizzativa%rowtype;
 begin
  aUOPresArea:=getUOPresidenteArea(aEs, aCDRArea);
  if aUOPresArea.cd_unita_organizzativa is not null then
   begin
    select * into aCDSPresArea from
     unita_organizzativa
    where
         cd_unita_organizzativa = aUOPresArea.cd_unita_padre
 	 and fl_cds = 'Y';
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;
  return aCDSPresArea;
 end;

 function getUOCDS(aCDR cdr%rowtype) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
  aUO unita_organizzativa%rowtype;
 begin
  -- estraggo l'UO di appartenenza del CDR
  begin
   select * into aUO from unita_organizzativa where
       cd_unita_organizzativa = aCDR.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO di appartenenza del CDR '||aCDR.cd_centro_responsabilita||' non trovata!');
  end;

  begin
   -- estraggo l'UO CDS dall'UO
   select * into aUOCDS from unita_organizzativa where
       cd_unita_padre = aUO.cd_unita_padre
   and fl_uo_cds = 'Y';
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO CDS dell''UO '||aUO.cd_unita_organizzativa||' non trovata!');
  end;
  return aUOCDS;
 end;

 function getUO(aCDR cdr%rowtype) return unita_organizzativa%rowtype is
  aUO unita_organizzativa%rowtype;
 begin
  -- estraggo l'UO di appartenenza del CDR
  begin
   select * into aUO from unita_organizzativa where
       cd_unita_organizzativa = aCDR.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO di appartenenza del CDR '||aCDR.cd_centro_responsabilita||' non trovata!');
  end;
  return aUO;
 end;

 Function getCDUO(aCDR cdr.cd_unita_organizzativa%Type) Return VARCHAR2 is
  aUO unita_organizzativa.cd_unita_organizzativa%Type;
 begin
  -- estraggo l'UO di appartenenza del CDR
  begin
   Select cd_unita_organizzativa Into aUO from cdr where
       cd_centro_responsabilita = aCDR;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO di appartenenza del CDR '||aCDR||' non trovata!');
  end;
  return aUO;
 end;

-- Dato il codice unita_organizzativa, ritorna la descrizione
 Function getdesUO(aCDUO unita_organizzativa.cd_unita_organizzativa%Type) return VARCHAR2 Is
 des    VARCHAR2(4000);
 Begin
   Select DS_UNITA_ORGANIZZATIVA
   Into   des
   From   UNITA_ORGANIZZATIVA
   Where  CD_UNITA_ORGANIZZATIVA = aCDUO;
   Return des;
 Exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('La UO '||aCDUO||' non esiste!');
 End;

 function getUOCDS(aEs number, aCdCds varchar2) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
 begin
  begin
   -- estraggo l'UO CDS dall'UO
   select * into aUOCDS from unita_organizzativa a where
       a.cd_unita_padre = aCdCds
   and a.fl_uo_cds = 'Y'
   and exists (select 1 from v_unita_organizzativa_valida where
                     esercizio = aEs
				 and cd_unita_organizzativa = a.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO CDS del CDS '||aCdCds||' non trovata!');
  end;
  return aUOCDS;
 end;

 function getUOVersCori(aEs number) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
 begin
  begin
   -- estraggo l'UO di servizio versamento cori (tipo SAC)
   select * into aUOCDS from unita_organizzativa a where
       a.cd_unita_organizzativa = CNRCTB015.GETVAL01PERCHIAVE('UO_SPECIALE','UO_VERSAMENTO_CORI')
   and cd_tipo_unita = TIPO_SAC
   and exists (select 1 from v_unita_organizzativa_valida where
                     esercizio = aEs
				 and cd_unita_organizzativa = a.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO SAC responsabile del versamento CORI accentrato non trovata');
  end;
  return aUOCDS;
 end;

 function getUOVersCoriTuttaSAC(aEs number) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
 begin
  begin
   -- estraggo l'UO di servizio versamento cori unificato per tutta la SAC(tipo SAC)
   select * into aUOCDS from unita_organizzativa a where
       a.cd_unita_organizzativa = CNRCTB015.GETVAL01PERCHIAVE('UO_SPECIALE','UO_VERSAMENTO_CORI_TUTTA_SAC')
   and cd_tipo_unita = TIPO_SAC
   and exists (select 1 from v_unita_organizzativa_valida where
                     esercizio = aEs
		 and cd_unita_organizzativa = a.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO SAC responsabile del versamento CORI unificato non trovata');
  end;
  return aUOCDS;
 end;

 function getUOVersCoriContoBI(aEs number) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
 begin
  begin
   -- estraggo l'UO di servizio versamento cori su Conto Banca d'Ialia
   select * into aUOCDS from unita_organizzativa a where
       a.cd_unita_organizzativa = CNRCTB015.GETVAL01PERCHIAVE('UO_SPECIALE','UO_VERSAMENTO_CORI_CONTO_BI')
   and exists (select 1 from v_unita_organizzativa_valida where
                     esercizio = aEs
		 and cd_unita_organizzativa = a.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO responsabile del versamento CORI su Conto BI non trovata');
  end;
  return aUOCDS;
 end;

 function getUOVersIVA(aEs number) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
 begin
  begin
   -- estraggo l'UO di servizio versamneto IVA (deve essere di tipo SAC)
   select * into aUOCDS from unita_organizzativa a where
       a.cd_unita_organizzativa = CNRCTB015.GETVAL01PERCHIAVE('UO_SPECIALE','UO_VERSAMENTO_IVA')
   and cd_tipo_unita = TIPO_SAC
   and exists (select 1 from v_unita_organizzativa_valida where
                     esercizio = aEs
				 and cd_unita_organizzativa = a.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO SAC responsabile del versamento IVA non trovata');
  end;
  return aUOCDS;
 end;

 function isCDRENTE(aCDR cdr%rowtype) return boolean is
  aUO unita_organizzativa%rowtype;
  aLC lunghezza_chiavi%rowtype;
 begin
  begin
   select * into aLC from lunghezza_chiavi where
        tabella = 'UNITA_ORGANIZZATIVA'
    and attributo = 'CD_UNITA_ORGANIZZATIVA'
    and esercizio = 0
    and livello = 1;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Configurazione della lunghezza chiavi Struttura Organizzativa non trovata!');
  end;
  if substr(aCDR.cd_centro_responsabilita,1,instr(aCDR.cd_centro_responsabilita,'.') - 1) = lpad('9',aLC.lunghezza,'9') then
   return true;
  end if;
  return false;
 end;

 function getUOPersonale return unita_organizzativa%rowtype is
  aUO unita_organizzativa%rowtype;
  aCDR cdr%rowtype;
  aCdUo varchar2(30);
 begin
  aCDR:=getCDRPersonale;
  aCdUO:=aCDR.cd_unita_organizzativa;
  if aCdUo is null then
   return null;
  end if;

  select * into aUO from unita_organizzativa where
   cd_unita_organizzativa = aCdUo;
  return aUO;
 end;

 function getCDRPersonale return cdr%rowtype is
  aCDR cdr%rowtype;
  aCdCDR varchar2(30);
 begin
  aCdCDR:=CNRCTB015.GETVAL01PERCHIAVE(0,'CDR_SPECIALE','CDR_PERSONALE');
  if aCdCDR is null then
   return null;
  end if;

  select * into aCDR from cdr where
   cd_centro_responsabilita = aCdCDR;
  return aCDR;
 end;


 function getCDRResponsabileUO(aUO unita_organizzativa%rowtype) return cdr%rowtype is
  aCdr cdr%rowtype;
 begin
  begin
   select b.* into aCdr from cdr b, unita_organizzativa a where
       a.cd_unita_organizzativa = aUO.cd_unita_organizzativa
   and b.cd_unita_organizzativa = a.cd_unita_organizzativa
   and to_number(b.cd_proprio_cdr) = 0;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('CDR responsabilile dell''UO'||aUO.cd_unita_organizzativa||' non trovato!');
  end;
  return aCDR;
 end;

 function getCDRResponsabileUO(aCdUO VARCHAR2) return cdr%rowtype is
  aCdr cdr%rowtype;
 begin
  begin
   select b.* into aCdr from cdr b, unita_organizzativa a where
       a.cd_unita_organizzativa = aCdUO
   and b.cd_unita_organizzativa = a.cd_unita_organizzativa
   and to_number(b.cd_proprio_cdr) = 0;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('CDR responsabilile dell''UO ' || aCdUO ||' non trovato!');
  end;
  return aCDR;
 end;

 function getUOAfferenza(aCDR cdr%rowtype) return unita_organizzativa%rowtype is
  aUO unita_organizzativa%rowtype;
 begin
  begin
   select * into aUO from unita_organizzativa where
       cd_unita_organizzativa = aCDR.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO di afferenza del CDR '||aCDR.cd_centro_responsabilita||' non trovata!');
  end;
  return aUO;
 end;

 Procedure ins_UNITA_ORGANIZZATIVA (aDest UNITA_ORGANIZZATIVA%rowtype) is
  begin
   insert into UNITA_ORGANIZZATIVA (
     FL_RUBRICA
    ,LIVELLO
    ,PRC_COPERTURA_OBBLIG_2
    ,PRC_COPERTURA_OBBLIG_3
    ,CD_RESPONSABILE
    ,CD_RESPONSABILE_AMM
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,FL_PRESIDENTE_AREA
    ,CD_AREA_RICERCA
    ,CD_UNITA_PADRE
    ,ESERCIZIO_INIZIO
    ,ESERCIZIO_FINE
    ,CD_UNITA_ORGANIZZATIVA
    ,DS_UNITA_ORGANIZZATIVA
    ,CD_PROPRIO_UNITA
    ,CD_TIPO_UNITA
    ,FL_CDS
    ,FL_UO_CDS
	,CD_AREA_SCIENTIFICA
   ) values (
     aDest.FL_RUBRICA
    ,aDest.LIVELLO
    ,aDest.PRC_COPERTURA_OBBLIG_2
    ,aDest.PRC_COPERTURA_OBBLIG_3
    ,aDest.CD_RESPONSABILE
    ,aDest.CD_RESPONSABILE_AMM
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.FL_PRESIDENTE_AREA
    ,aDest.CD_AREA_RICERCA
    ,aDest.CD_UNITA_PADRE
    ,aDest.ESERCIZIO_INIZIO
    ,aDest.ESERCIZIO_FINE
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.DS_UNITA_ORGANIZZATIVA
    ,aDest.CD_PROPRIO_UNITA
    ,aDest.CD_TIPO_UNITA
    ,aDest.FL_CDS
    ,aDest.FL_UO_CDS
	,aDest.CD_AREA_SCIENTIFICA
    );
 end;

Function getdeslivello(aEs NUMBER, ati_gestione VARCHAR, aliv1 VARCHAR2, aliv2 VARCHAR2, aliv3 VARCHAR2,
                                                 aliv4 VARCHAR2, aliv5 VARCHAR2, aliv6 VARCHAR2,
                                                 aliv7 VARCHAR2) return VARCHAR2 Is

RET      CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE%Type;

Begin

If aliv7 IS NOT NULL THEN

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 = aliv3 And
        CD_LIVELLO4 = aliv4 And
        CD_LIVELLO5 = aliv5 And
        CD_LIVELLO6 = aliv6 And
        CD_LIVELLO7 = aliv7;

Elsif aliv6 IS NOT NULL Then

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 = aliv3 And
        CD_LIVELLO4 = aliv4 And
        CD_LIVELLO5 = aliv5 And
        CD_LIVELLO6 = aliv6 And
        CD_LIVELLO7 Is Null;

Elsif aliv5 IS NOT NULL THEN

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 = aliv3 And
        CD_LIVELLO4 = aliv4 And
        CD_LIVELLO5 = aliv5 And
        CD_LIVELLO6 Is Null;

Elsif aliv4 IS NOT NULL Then

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 = aliv3 And
        CD_LIVELLO4 = aliv4 And
        CD_LIVELLO5 Is Null;

Elsif aliv3 IS NOT NULL THEN

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 = aliv3 And
        CD_LIVELLO4 Is Null;

Elsif aliv2 IS NOT NULL THEN

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 Is Null;

Elsif aliv1 IS NOT NULL THEN

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 Is Null;

End If;

Return RET;

Exception
   When Others Then Return (' ');
End;

Function GETDESDIPARTIMENTO (aCd_dip VARCHAR2) return VARCHAR2 Is
ret     dipartimento.cd_dipartimento%Type;
Begin
 Select DS_DIPARTIMENTO
 Into   ret
 From   DIPARTIMENTO
 Where CD_DIPARTIMENTO = aCd_dip;

 Return ret;
Exception
 When Others Then Return (' ');
End;


Procedure ins_CDR (aDest CDR%rowtype) is
  begin
   insert into CDR (
     CD_CENTRO_RESPONSABILITA
    ,ESERCIZIO_INIZIO
    ,ESERCIZIO_FINE
    ,CD_UNITA_ORGANIZZATIVA
    ,LIVELLO
    ,CD_PROPRIO_CDR
    ,DS_CDR
    ,CD_RESPONSABILE
    ,INDIRIZZO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,CD_CDR_AFFERENZA
    ,PG_VER_REC
   ) values (
     aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.ESERCIZIO_INIZIO
    ,aDest.ESERCIZIO_FINE
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.LIVELLO
    ,aDest.CD_PROPRIO_CDR
    ,aDest.DS_CDR
    ,aDest.CD_RESPONSABILE
    ,aDest.INDIRIZZO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.CD_CDR_AFFERENZA
    ,aDest.PG_VER_REC
    );
 end;
 function isUOSAC(aUO varchar2) return boolean is
  conta number;
 begin
   select count(1) into conta
   from unita_organizzativa
   where cd_unita_organizzativa = aUO
    and fl_cds = 'N'
    and cd_tipo_unita = TIPO_SAC;

    if conta > 0 then
	  	 return true;
	  else
	  	 return false;
	  end if;
 end;
end;


