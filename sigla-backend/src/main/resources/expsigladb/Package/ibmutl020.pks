CREATE OR REPLACE package IBMUTL020 as
--
-- IBMUTL020 - Package di utilita per protocollo VSX
-- Date: 07/06/2002
-- Version: 1.0
--
-- History:
--
-- Date: 07/06/2002
-- Version: 1.0
-- Creazione
--
--
-- Functions & Procedures:

-- Ritorna il progressivo da utilizzare per la chiamata VSX

 function vsx_get_pg_call return number;
end;


CREATE OR REPLACE package body IBMUTL020 is
 function vsx_get_pg_call return number is
  aNum number;
 begin
  aNum:=0;
  select IBMSEQ00_GENERIC_STP_CALL.nextval into aNum from dual;
  return aNum;
 end;
end;


