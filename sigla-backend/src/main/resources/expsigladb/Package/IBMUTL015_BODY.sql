--------------------------------------------------------
--  DDL for Package Body IBMUTL015
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "IBMUTL015" is

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
