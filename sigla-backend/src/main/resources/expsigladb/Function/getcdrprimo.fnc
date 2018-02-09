CREATE OR REPLACE function getCdrPrimo(aCdCdr varchar2) return varchar2 is
--==================================================================================================
--
-- Date: 10/04/2003
-- Version: 1.0
--
-- Dato il codice di cdr ritorna il cdr di primo livello o area di afferenza
--
-- History:
--
-- Date: 10/04/2003
-- Version: 1.0
-- Creazione function
--
-- Body:
--
--==================================================================================================
 CR cdr%rowtype;
begin
 begin
  select * into CR from cdr where
   cd_centro_responsabilita = aCdCdr;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Cdr non trovato:'||aCdCdr);
 end;
 if CR.livello = 1 or (CR.livello = 2 and CR.cd_cdr_afferenza is null) then
  return CR.cd_centro_responsabilita;
 else
  return CR.cd_cdr_afferenza;
 end if;
end;
/


