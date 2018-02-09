CREATE OR REPLACE TRIGGER AI_SEMAFORO_STATICO
  AFTER INSERT
  on SEMAFORO_STATICO
  for each row
declare
 aTS semaforo_tipo%rowtype;
 aPattern varchar2(5);
begin
--
-- Trigger attivato su inserimento in SEMAFORO STATICO
--
-- Date: 23/09/2004
-- Version: 1.0
--
-- Dependency:
--
-- Date: 23/09/2004
-- Version: 1.0
-- Creazione
--
-- Body:
--
 if
     :new.cd_cds is null
  or :new.cd_unita_organizzativa is null
  or :new.cd_centro_responsabilita is null
  or :new.esercizio is null
  or :new.cd_tipo_semaforo is null
 then
  IBMERR001.RAISE_ERR_GENERICO('Errore interno in acquisizione semaforo applicativo: parametri non completamente specificati');
 end if;
 begin
  select * into aTS from semaforo_tipo where
      cd_tipo_semaforo = :new.cd_tipo_semaforo;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Errore interno. Il tipo di semaforo applicativo specificato:'||:new.cd_tipo_semaforo||' non risulta definito');
 end;
 aPattern:='';
 if :new.cd_cds = '*' then
  aPattern:='*';
 else
  aPattern:='$';
 end if;
 if :new.cd_unita_organizzativa = '*' then
  aPattern:=aPattern||'*';
 else
  aPattern:=aPattern||'$';
 end if;
 if :new.cd_centro_responsabilita = '*' then
  aPattern:=aPattern||'*';
 else
  aPattern:=aPattern||'$';
 end if;
 if :new.esercizio = 0 then
  aPattern:=aPattern||'*';
 else
  aPattern:=aPattern||'$';
 end if;
 if aTS.pattern is null or aPattern != aTS.pattern then
  IBMERR001.RAISE_ERR_GENERICO('Errore interno. Il semaforo applicativo specificato:'||:new.cd_tipo_semaforo||' presuppone parametri differenti da quelli passati');
 end if;
end;
/


