--------------------------------------------------------
--  DDL for Package Body IBMERR001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "IBMERR001" as
 function msg_weak_restrict(aTabName varchar2, aRefTabName varchar2) return varchar2 is
 begin
  return 'Dato non eliminabile da '||UPPER(aTabName)||' perche referenziato da '||UPPER(aRefTabName);
 end;

 procedure raise_err_weak_restrict(aTabName varchar2, aRefTabName varchar2) is
 begin
  raise_application_error(ERR_WEAK_RESTRICT,msg_weak_restrict(aTabName,aRefTabName),TRUE);
 end;



 function msg_no_change_pk(aTabName varchar2) return varchar2 is
 begin
  return 'La chiave primaria della tabella '||UPPER(aTabName)||' non e modificabile';
 end;

 procedure raise_no_change_pk(aTabName varchar2) is
 begin
  raise_application_error(ERR_NO_CHANGE_PK,msg_no_change_pk(aTabName),TRUE);
 end;



 function msg_no_delete_pk(aTabName varchar2) return varchar2 is
 begin
  return 'Dato non eliminabile dalla tabella '||UPPER(aTabName);
 end;

 procedure raise_no_delete_pk(aTabName varchar2) is
 begin
  raise_application_error(ERR_NO_DELETE, msg_no_delete_pk(aTabName),TRUE);
 end;


 procedure raise_no_change(aMessage varchar2) is
 begin
  raise_application_error(ERR_NO_CHANGE,aMessage,TRUE);
 end;


 procedure raise_err_generico(aMessage varchar2) is
 begin
  raise_application_error(ERR_GENERICO,aMessage,TRUE);
 end;

 procedure raise_wrn_generico(aMessage varchar2) is
 begin
  raise_application_error(WRN_GENERICO,aMessage,TRUE);
 end;

end;
