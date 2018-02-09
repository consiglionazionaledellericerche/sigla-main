CREATE OR REPLACE TRIGGER AD_PRINT_SPOOLER
AFTER DELETE
on PRINT_SPOOLER
for each row
begin
--
-- Trigger attivato su aggiornamento della tabella PRINT_SPOOLER
--
-- Date: 05/12/2002
-- Version: 1.0
--
-- Dependency:
--
-- History:
--
-- Date: 05/12/2002
-- Version: 1.0
-- Creazione
--
-- Body:
--

-- Scarico dello storico
  begin
   for aRepGen in (
    select id,chiave,tipo,sequenza from report_generico where
	   id=:old.id_report_generico
	for update nowait
   ) loop
    delete from report_generico where
	     id = aRepGen.id
	 and chiave = aRepGen.chiave
	 and tipo = aRepGen.tipo
	 and sequenza = aRepGen.sequenza;
   end loop;
  exception when others then
   null;
  end;
end;
/


