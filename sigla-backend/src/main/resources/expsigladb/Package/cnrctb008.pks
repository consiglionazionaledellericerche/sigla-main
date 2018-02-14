CREATE OR REPLACE package CNRCTB008 as
--
-- CNRCTB008 - Package di gestione dell'esercizio
-- Date: 24/09/2004
-- Version: 1.10
--
-- Gestisce operationi e costanti legate alla tabella ESERCIZIO
--
-- Dependency:
--
-- History:
--
-- Date: 16/11/2001
-- Version: 1.0
-- Creazione
-- Date: 09/02/2002
-- Version: 1.1
-- Introdotte le costanti degli stati dell'esercizio
-- Date: 27/01/2002
-- Version: 1.2
-- Esercizio infinito impostato a 2100
-- Date: 27/10/2002
-- Version: 1.3
-- Esercizio di partenza in produzione dell'applicazione
-- Date: 12/12/2002
-- Version: 1.4
-- Introduzione funzione estrazione esercizio di partenza da configurazione CNR
--
-- Date: 03/06/2003
-- Version: 1.5
-- Introdotte funzioni getStatoEsercizio, isEsercizioAperto, isEsercizioChiuso
--
-- Date: 13/06/2003
-- Version: 1.6
-- Aggiunto metodo per la restituzione della data contabile in relazione all'esercizio
--
-- Date: 16/07/2003
-- Version: 1.7
-- Lo stato di esercizi precedenti a quello di partenza ? sempre CHIUSO
--
-- Date: 03/09/2003
-- Version: 1.8
-- Aggiunto metodo isEsercizioChiuso/Aperto/YesNo per gestione applicativa
--
-- Date: 04/09/2003
-- Version: 1.9
-- Aggiunto metodo isEsChiusoPerAlmenoUnCdsYesNo per gestione applicativa
--
-- Date: 24/09/2004
-- Version: 1.10
-- Aggiunto metodo isApertoOChiuso
--
-- Constants:

-- Esercizio infinito
 ESERCIZIO_INFINITO CONSTANT NUMBER(4) := 2100;

 CONST_ESERCIZIO_SPECIALE CONSTANT VARCHAR2(50) := 'ESERCIZIO_SPECIALE';
 CONST_ESERCIZIO_PARTENZA CONSTANT VARCHAR2(50) := 'ESERCIZIO_PARTENZA';

-- Esercizio partenza
 function ESERCIZIO_PARTENZA return number;

-- Stati dell'esercizio
 STATO_INIZIALE CONSTANT VARCHAR2(1) := 'I';
 STATO_APERTURA_PDG CONSTANT VARCHAR2(1) := 'G';
 STATO_APERTURA CONSTANT VARCHAR2(1) := 'A';
 STATO_CHIUSURA_PROVVISORIA CONSTANT VARCHAR2(1) := 'P';
 STATO_CHIUSURA_DEFINITIVA CONSTANT VARCHAR2(1) := 'C';

-- Ritorna lo stato dell'esercizio per il Cds
function getStatoEsercizio (aEs number, aCdCds varchar2) return varchar2;

-- Verifica se lo stato APERTO dell'esercizio per il Cds
function isEsercizioAperto (aEs number, aCdCds varchar2) return boolean;

-- Ritorna true se lo stato dell'esercizio ? APERTO o CHIUSO per il Cds
function isEsercizioApertoOChiuso (aEs number, aCdCds varchar2) return boolean;

-- Verifica se lo stato CHIUSO dell'esercizio per il Cds
function isEsercizioChiuso (aEs number, aCdCds varchar2) return boolean;

-- Verifica se l'esercizio ? aperto ('Y') o meno ('N')
function isEsercizioApertoYesNo (aEs number, aCdCds varchar2) return varchar2;
-- Verifica se l'esercizio ? chiuso ('Y') o meno ('N')
function isEsercizioChiusoYesNo (aEs number, aCdCds varchar2) return varchar2;

-- Verifica se l'esercizio ? chiuso ('Y') per almeno un cds o meno ('N')
function isEsChiusoPerAlmenoUnCdsYesNo (aEs number) return varchar2;

-- Ritorna un timestamp costruito con le seguenti regole:
--
-- L'esercizio specificato = a quello di aTSNow
--    ritorna aTSNow
-- L'esercizio specificato + 1 = a qhello di aTSNow
--    ritorna un timestamp 31/12/<esercizio specificato> HHMISS (attuali)
-- Altrimenti solleva eccezione

function getTimestampContabile(aEs number, aTSNow date) return date;
-- Aggiunta funzione senza blocco se esercizio/cds non esistente - richiamata nella verifica delle contabilizzazioni con competenza in anni precedenti
-- alla creazione del cds
function isEsercizioApertoSenzaBlocco(aEs number, aCdCds varchar2) return boolean;
end;


CREATE OR REPLACE package body CNRCTB008 as
 function getTimestampContabile(aEs number, aTSNow date) return date is
 begin
  if to_char(aTSNow,'YYYY') = aEs + 1 then
   return to_date(aEs||'1231-'||to_char(aTSNow,'HHMISS'),'YYYYMMDD-HHMISS');
  elsif to_char(aTSNow,'YYYY') = aEs then
   return aTSNow;
  Elsif to_char(aTSNow,'YYYY') < aEs then
   return to_date(aEs+1||'0101-'||to_char(aTSNow,'HHMISS'),'YYYYMMDD-HHMISS');
  else
   IBMERR001.RAISE_ERR_GENERICO('La data di sistema ('||To_Char(aTSNow, 'DD/MM/YYYY')||') ? superiore all''esercizio di scrivania ('||aEs||') di almeno 2 anni');
  end if;
 end;

 function ESERCIZIO_PARTENZA return number is
  aNum number(4);
 begin
  select im01 into aNum from configurazione_cnr
     where
	      esercizio = 0
      and cd_unita_funzionale = '*'
	  and cd_chiave_primaria = CONST_ESERCIZIO_SPECIALE
      and cd_chiave_secondaria = CONST_ESERCIZIO_PARTENZA;
  if aNum is null then
   IBMERR001.RAISE_ERR_GENERICO('Esercizio di partenza non specificato in configurazione CNR');
  end if;
  return aNum;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Esercizio di partenza non specificato in configurazione CNR');
 end;

function getStatoEsercizio (aEs number, aCdCds varchar2) return varchar2 is
aStato char(1);
begin
    if aEs < ESERCIZIO_PARTENZA then
     return STATO_CHIUSURA_DEFINITIVA;
	end if;
 	begin
		select st_apertura_chiusura into aStato
		from esercizio
		where cd_cds 	  = aCdCds
		  and esercizio   = aEs;
		return aStato;
	exception when NO_DATA_FOUND then
		ibmerr001.RAISE_ERR_GENERICO('Esercizio contabile '||aEs||' non definito per il CdS '||aCdCds);
	end;
end;
function getStatoEsercizioSenzaBlocco (aEs number, aCdCds varchar2) return varchar2 is
aStato char(1);
begin
    if aEs < ESERCIZIO_PARTENZA then
     return STATO_CHIUSURA_DEFINITIVA;
	end if;
 	begin
		select st_apertura_chiusura into aStato
		from esercizio
		where cd_cds 	  = aCdCds
		  and esercizio   = aEs;
		return aStato;
	exception when NO_DATA_FOUND then
	return STATO_CHIUSURA_DEFINITIVA;
		--ibmerr001.RAISE_ERR_GENERICO('Esercizio contabile '||aEs||' non definito per il CdS '||aCdCds);
	end;
end;
function isEsercizioApertoSenzaBlocco(aEs number, aCdCds varchar2) return boolean is
aStato char(1);
isAperto boolean := false;
begin
	aStato := substr(getStatoEsercizioSenzaBlocco( aEs, aCdCds),1,1);
	if aStato = STATO_APERTURA then
		isAperto := true;
	end if;
	return isAperto;
end;

function isEsercizioAperto (aEs number, aCdCds varchar2) return boolean is
aStato char(1);
isAperto boolean := false;
begin
	aStato := substr(getStatoEsercizio( aEs, aCdCds),1,1);
	if aStato = STATO_APERTURA then
		isAperto := true;
	end if;
	return isAperto;
end;

function isEsercizioApertoOChiuso (aEs number, aCdCds varchar2) return boolean is
aStato char(1);
isApertoOChiuso boolean := false;
begin
	aStato := substr(getStatoEsercizio( aEs, aCdCds),1,1);
	if    aStato = STATO_APERTURA
	   or aStato = STATO_CHIUSURA_DEFINITIVA
	   or aStato = STATO_CHIUSURA_PROVVISORIA
	then
		isApertoOChiuso := true;
	end if;
	return isApertoOChiuso;
end;

function isEsercizioChiuso (aEs number, aCdCds varchar2) return boolean is
aStato char(1);
isChiuso boolean := false;
begin
	aStato := substr(getStatoEsercizio( aEs, aCdCds),1,1);
	if aStato = STATO_CHIUSURA_DEFINITIVA then
		isChiuso := true;
	end if;
	return isChiuso;
end;


function isEsChiusoPerAlmenoUnCdsYesNo (aEs number) return varchar2 is
 aNum number;
begin
 begin
  select distinct 1 into aNum from esercizio where
          esercizio=aEs
	  and ST_APERTURA_CHIUSURA=STATO_CHIUSURA_DEFINITIVA;
  return 'Y';
 exception when NO_DATA_FOUND then
  return 'N';
 end;
end;


function isEsercizioChiusoYesNo (aEs number, aCdCds varchar2) return varchar2 is
isChiuso boolean;
begin
	 isChiuso := isEsercizioChiuso(aEs, aCdCds);
	 if isChiuso then
	 	return 'Y';
	 else
	 	return 'N';
	 end if;
end;

function isEsercizioApertoYesNo (aEs number, aCdCds varchar2) return varchar2 is
isAperto boolean;
begin
	 isAperto := isEsercizioAperto(aEs, aCdCds);
	 if isAperto then
	 	return 'Y';
	 else
	 	return 'N';
	 end if;
end;

end;


