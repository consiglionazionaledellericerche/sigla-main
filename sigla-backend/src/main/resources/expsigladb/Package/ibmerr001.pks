CREATE OR REPLACE package IBMERR001 as
--
-- IBMERR001 - Package di definizione errori Oracle generali
-- Date: 15/07/2002
-- Version: 1.4
--
-- Definizione errori sollevati mediante raise_application_error function
-- Errori di scope generale
--
-- History:
--
-- Date: 31/08/2001
-- Version: 1.1
-- Introduzione errore generico e messaggi estesi
--
-- Date: 07/03/2002
-- Version: 1.2
-- Preserva lo stack trace dell'erore nel raise di application error
--
-- Date: 14/03/2002
-- Version: 1.3
-- Fix 1.2 estesa a tutti i metodi
--
-- Date: 15/07/2002
-- Version: 1.4
-- Introduzione warning generico
--
-- Constants:

-- weak RESTRICT CONSTRAINT ERROR: il dato e referenziato in unl'altra tabella ma
-- non e possibile gestire il vincolo attraverso FK con ON DELETE RESTRICT
 ERR_WEAK_RESTRICT CONSTANT NUMBER := -20010;

-- Errore di eliminazione di un record
 ERR_NO_DELETE CONSTANT NUMBER := -20012;

-- Errore causato da cambiamento della chiave primaria della tabella
 ERR_NO_CHANGE_PK CONSTANT NUMBER := -20014;

-- Errore di modifica del record
 ERR_NO_CHANGE CONSTANT NUMBER := -20016;

-- Errore applicativo generico
 ERR_GENERICO CONSTANT NUMBER := -20020;

-- Warning applicativo generico
 WRN_GENERICO CONSTANT NUMBER := -20025;

-- Functions e Procedures:
 --
 -- Stampa un messaggio di errore per indicare che il dato non e eliminabile dalla tabella aTabName
 -- perche esiste un riferimento debole da un'altra tabella aRefTabName sulla pk
 function msg_weak_restrict(aTabName varchar2, aRefTabName varchar2) return varchar2;
 -- Raise exception di weak restrict constraint
 procedure raise_err_weak_restrict(aTabName varchar2, aRefTabName varchar2);


 -- Stampa un messaggio di errore per indicare che il dato non e eliminabile dalla tabella aTabName
 function msg_no_delete_pk(aTabName varchar2) return varchar2;
 -- Raise exception di non elikminabilita record per PK
 procedure raise_no_delete_pk(aTabName varchar2);

 -- Stampa un messaggio di errore per indicare che la chiave primaria della tabella aTabName non puo essere modificata
 function msg_no_change_pk(aTabName varchar2) return varchar2;
 -- Raise exception di non modificabilita PK
 procedure raise_no_change_pk(aTabName varchar2);

 -- Raise exception di non modificabilita del record
 procedure raise_no_change(aMessage varchar2);

 -- Raise exception generica: identificata solo da messaggio aMessage
 procedure raise_err_generico(aMessage varchar2);

 -- Raise exception warning generico: identificata solo da messaggio aMessage
 procedure raise_wrn_generico(aMessage varchar2);

end;


CREATE OR REPLACE package body IBMERR001 as
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


