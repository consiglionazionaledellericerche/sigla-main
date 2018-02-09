CREATE OR REPLACE TRIGGER AU_ESERCIZIO
  AFTER UPDATE
  on ESERCIZIO
  for each row
declare
 aEsercizio esercizio%rowtype;
begin
--
-- Trigger attivato sull'aggiornamento di un nuovo esercizio
--
-- Date: 09/01/2002
-- Version: 1.2
--
-- Dependency: IBMERR001
--
-- History:
--
-- Date: 01/06/2001
-- Version: 1.0
-- Creazione
-- Date: 18/10/2001
-- Version: 1.1
-- Introduzione di cd_cds in esercizio
-- Date: 09/01/2002
-- Version: 1.2
-- Aggiornamento delle tabelle di numerazione dei documenti contabili all'apertura dell'esercizio
--
-- Body:
--
-- La modifica della chiave primaria non ? permessa
 if
     :old.esercizio != :new.esercizio
  or :old.cd_cds != :new.cd_cds
 then
  IBMERR001.raise_no_change_pk('Esercizio');
 end if;
 if
      :old.st_apertura_chiusura != CNRCTB008.STATO_APERTURA
  and :new.st_apertura_chiusura = CNRCTB008.STATO_APERTURA
 then
  CNRCTB018.AGGIORNANUMERATORI(:new.esercizio,:new.cd_cds,:new.utuv);
 end if;
end;
/


