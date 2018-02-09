CREATE OR REPLACE TRIGGER AI_ESERCIZIO
  AFTER INSERT
  on ESERCIZIO
  for each row
declare
 aEsercizio esercizio%rowtype;
begin
--
-- Trigger di attivato sull'inserimento di un nuovo esercizio
--
-- Date: 14/11/2002
-- Version: 1.2
--
-- Dependency: CNRCTB005
--
-- History:
--
-- Date: 01/06/2001
-- Version: 1.0
-- Creazione
-- Date: 18/10/2001
-- Version: 1.1
-- Introduzione di cd_cds in esercizio
-- Date: 14/11/2002
-- Version: 1.2
-- Introduzione della tabella ESERCIZIO_BASE e suo riempimento automatico
--
-- Body:
--

 begin
  insert into esercizio_base (esercizio,dacr,utcr,duva,utuv,pg_ver_rec)
                      values (:new.esercizio,:new.dacr,:new.utcr,:new.dacr,:new.utcr,1);
 exception when DUP_VAL_ON_INDEX then
  null;
 end;

 aEsercizio.esercizio:=:new.esercizio;
 aEsercizio.cd_cds:=:new.cd_cds;
 aEsercizio.ds_Esercizio:=:new.ds_esercizio;
 aEsercizio.st_apertura_chiusura:=:new.st_apertura_chiusura;
 aEsercizio.utcr:=:new.utcr;
 aEsercizio.dacr:=:new.dacr;
 aEsercizio.duva:=:new.duva;
 aEsercizio.utuv:=:new.utuv;
 CNRCTB005.onCreazioneEsercizio(aEsercizio);
end;
/


