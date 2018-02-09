CREATE OR REPLACE package IBMUTL015 as
--
-- IBMUTL015 - Package di utilita per utilizzo RBSBIG
-- Date: 06/08/2003
-- Version: 1.0
--
-- History:
--
-- Date: 06/08/2003
-- Version: 1.0
-- Creazione
--
-- Constants:

-- Specifica l'RBS speciale per la transazione

RBSBIG CONSTANT VARCHAR2(30):='SYSTEM';

-- Abilita l'utilizzo di rbs speciale in transazione

ENABLED CONSTANT boolean:=true;

-- Functions :

-- Imposta se abilitato l'RBSBIG sulla transazione
 procedure setRbsBig;
-- Commit e impostazione rbs big
 procedure commitRbsBig;
-- Rollback e impostazione rbs big
 procedure rollbackRbsBig;

end;
/


CREATE OR REPLACE package body IBMUTL015 is

 procedure setRbsBig is
 begin
  if ENABLED Then
  Null;
--     execute immediate 'set transaction use rollback segment '||RBSBIG;
  end if;
 end;

 procedure commitRbsBig is
 begin
  commit;
  setRbsBig;
 end;

 procedure rollbackRbsBig is
 begin
  rollback;
  setRbsBig;
 end;
end;
/


