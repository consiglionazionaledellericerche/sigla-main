CREATE OR REPLACE function isScrInterno(aCdCdr varchar2,aCdCdrColl varchar2) return number is
--==================================================================================================
--
-- Date: 10/04/2003
-- Version: 1.0
--
-- Dati 2 codici di cdr, verifica che non appartengano allo stesso CDR di primo livello
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
 CDR_C cdr%rowtype;
begin
 begin
  select * into CR from cdr where
   cd_centro_responsabilita = aCdCdr;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Cdr non trovato:'||aCdCdr);
 end;
 begin
  select * into CDR_C from cdr where
   cd_centro_responsabilita = aCdCdrColl;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Cdr non trovato:'||aCdCdrColl);
 end;
 if
      CR.livello = 2 and CDR_C.livello = 1 and CR.cd_cdr_afferenza = CDR_C.cd_centro_responsabilita
   or CR.livello = 1 and CDR_C.livello = 2 and CR.cd_centro_responsabilita = CDR_C.cd_cdr_afferenza
   or CR.livello = 2 and CDR_C.livello = 2 and CR.cd_cdr_afferenza = CDR_C.cd_cdr_afferenza
 then
  return 1;
 else
  return 0;
 end if;
end;
/


