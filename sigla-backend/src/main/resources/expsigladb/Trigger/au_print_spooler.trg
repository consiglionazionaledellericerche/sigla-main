CREATE OR REPLACE TRIGGER AU_PRINT_SPOOLER
AFTER UPDATE
ON PRINT_SPOOLER 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
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
 if
  :old.stato <> :new.stato and :new.stato not in ('C','X','E')
 then
  begin
   for aRepGen in (
    select id,chiave,tipo,sequenza from report_generico 
		   Where id=:new.id_report_generico
           And   :New.fl_email ='N' And :New.dt_partenza Is Null
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
 end if;
end;
/


