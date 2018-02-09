CREATE OR REPLACE TRIGGER AI_LIQUIDAZIONE_IVA
  AFTER INSERT
  on LIQUIDAZIONE_IVA
  for each row
DISABLE
begin
--
-- Trigger di attivato sull'inserimento in tabella liquidazione_iva
--
-- Date: 07/02/2003
-- Version: 1.0
--
-- Dependency: CNRCTB005
--
-- History:
--
-- Date: 07/02/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
 if :new.report_id = 0 then
  IBMERR001.RAISE_ERR_GENERICO('Funzione temporanemante sospesa');
 end if;
end;
/


