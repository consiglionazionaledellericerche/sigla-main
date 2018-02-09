CREATE OR REPLACE TRIGGER AD_ESERCIZIO
  AFTER DELETE
  on ESERCIZIO
  for each row
declare
 aEsercizio esercizio%rowtype;
begin
--
-- Trigger di attivato sull'eliminazione di un nuovo esercizio
--
-- Date: 13/07/2001
-- Version: 1.0
--
-- Dependency: IBMERR001
--
-- History:
-- Date: 13/07/2001
-- Version: 1.0
-- Creazione
--
-- Body:
--
-- L'eliminazione dell'esercizio NON ? permessa
 IBMERR001.raise_no_delete_pk('Esercizio');
end;
/


