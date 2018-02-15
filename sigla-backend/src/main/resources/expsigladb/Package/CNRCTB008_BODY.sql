--------------------------------------------------------
--  DDL for Package Body CNRCTB008
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB008" as
 function getTimestampContabile(aEs number, aTSNow date) return date is
 begin
  if to_char(aTSNow,'YYYY') = aEs + 1 then
   return to_date(aEs||'1231-'||to_char(aTSNow,'HHMISS'),'YYYYMMDD-HHMISS');
  elsif to_char(aTSNow,'YYYY') = aEs then
   return aTSNow;
  Elsif to_char(aTSNow,'YYYY') < aEs then
   return to_date(aEs+1||'0101-'||to_char(aTSNow,'HHMISS'),'YYYYMMDD-HHMISS');
  else
   IBMERR001.RAISE_ERR_GENERICO('La data di sistema ('||To_Char(aTSNow, 'DD/MM/YYYY')||') è superiore all''esercizio di scrivania ('||aEs||') di almeno 2 anni');
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
