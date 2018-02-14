CREATE OR REPLACE FUNCTION getCdRegioneLiquidCori
--==================================================================================================
--
-- Date: 02/02/2004
-- Version: 1.2
--
-- Se aFlCompensoStip = 'Y' allora
--  Cerca in tabella STIPENDI_COFI_CORI_REG il pg della regione da utilizare per cercare il corretto gruppo CORI
--  entrando in tabella con aEs,aCdCori e aTiEntePercipiente. Ritorna la regione trovata li
-- Se non trova la regione per il CORI specificato o se aFlCompensoStip='N'
--  Se aCdClassCori = 'R0' o 'R9' (addebito rate add. reg.)
--   Ritorna aCdRegioneAdd se esiste entry secca in gruppo_cr_det
--  Altrimenti ritorna '*'
--  Se aCdClassCori = 'IP'
--   Ritorna aCdRegioneIrap se esiste entry secca in gruppo_cr_det
--  Altrimenti ritorna '*'
-- Altrimenti ritorna '*'
--
-- History:
--
-- Date: 30/01/2003
-- Version: 1.0
-- Creazione function
--
-- Date: 10/02/2003
-- Version: 1.1
-- Fix estrazione regione
--
-- Date: 02/02/2004
-- Version: 1.2
-- Gestione classificazione R9 per addebito rate addizionale regionale
--
-- Body:
--
--==================================================================================================
(
	aFlCompensoStip char,
    aCdCori varchar2,
	aTiEntePercipiente char,
    aCdClassCori char,
    aEs number,
	aCdGruppoCori varchar2,
	aCdRegioneAdd varchar2,
	aCdRegioneIrap varchar2
) RETURN varchar2 IS
 aNum number;
 aReg varchar2(10);
 aStipCori stipendi_cofi_cori_reg%rowtype;
BEGIN
 if aFlCompensoStip = 'Y' then
  begin
   select cd_regione into aReg from stipendi_cofi_cori_reg where
        esercizio = aEs
	and cd_contributo_ritenuta = aCdCori
	and ti_ente_percipiente = aTiEntePercipiente;
   return aReg;
  exception when NO_DATA_FOUND then
   null;
  end;
 end if;
 if aCdClassCori = 'R0' or aCdClassCori = 'R9' then
  begin
   select 1 into aNum from gruppo_cr_det where
        esercizio = aEs
    and cd_gruppo_cr = aCdGruppoCori
    and cd_regione = aCdRegioneAdd
    and pg_comune =0;
   return aCdRegioneAdd;
  exception when NO_DATA_FOUND then
   return '*';
  end;
 elsif aCdClassCori = 'IP' then
  begin
   select 1 into aNum from gruppo_cr_det where
        esercizio = aEs
    and cd_gruppo_cr = aCdGruppoCori
    and cd_regione = aCdRegioneIrap
    and pg_comune =0;
   return aCdRegioneIrap;
  exception when NO_DATA_FOUND then
   return '*';
  end;
 else
  return '*';
 end if;
END;
/


