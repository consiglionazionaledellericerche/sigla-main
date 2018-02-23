--------------------------------------------------------
--  DDL for Package Body IBMUTL020
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "IBMUTL020" is
 function vsx_get_pg_call return number is
  aNum number;
 begin
  aNum:=0;
  select IBMSEQ00_GENERIC_STP_CALL.nextval into aNum from dual;
  return aNum;
 end;
end;
