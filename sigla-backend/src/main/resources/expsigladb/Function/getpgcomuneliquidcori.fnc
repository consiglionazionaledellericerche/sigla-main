CREATE OR REPLACE FUNCTION getPgComuneLiquidCori
--==================================================================================================
--
-- Date: 02/02/2004
-- Version: 1.1
--
-- Se aCdClassCori = 'C0' (addizionale comunale) o = 'C9' per addebito rate addizionale comunale
-- o 'C1' (acconto addizionale comunale):
--  Se esiste il gruppo cori corrispondente a aEs, aCdGruppoCori, aCdRegione, aPgComune
--   Ritorna il progressivo del comune
--  Altrimenti ritorna 0
-- Altrimenti ritrona 0
--
-- History:
--
-- Date: 30/01/2003
-- Version: 1.0
-- Creazione function
--
-- Date: 02/02/2004
-- Version: 1.1
-- Gestione classificazione C9 per addebito rate addizionale comunale
--
-- Date: 29/01/2008
-- Version: 1.2
-- Gestione classificazione C1 per addebito acconto addizionale comunale
--
-- Body:
--
--==================================================================================================
(
    aCdClassCori char,
    aEs number,
	aCdGruppoCori varchar2,
	aPgComune number
) RETURN number IS
 aNum number;
BEGIN
 If aCdClassCori = cnrctb545.isCoriAddCom Or        	   -- 'C0'
    aCdClassCori = cnrctb545.isCoriAddComRecRate Or        -- 'C9'
    aCdClassCori = cnrctb545.isCoriAddComAcconto Then      -- 'C1'
  begin
   select 1 into aNum from gruppo_cr_det where
        esercizio = aEs
    and cd_gruppo_cr = aCdGruppoCori
    and cd_regione = '*'
    and pg_comune =aPgComune;
   return aPgComune;
   
  exception when NO_DATA_FOUND then
  	if (aEs >= 2008) then -- esercizio di inizio versamento distinto per comune
     IBMERR001.RAISE_ERR_GENERICO('Configurazione mancante esercizio '||aEs||' gruppo '||aCdGruppoCori||' - Progressivo comune '||aPgComune);
    else
      return 0;
     end if;  
  end;
 else
  return 0;
 end if;
END;
/


