CREATE OR REPLACE TRIGGER AI_L_ESERCIZIO AFTER  INSERT  ON ESERCIZIO for each row
DECLARE
lESERCIZIO L_ESERCIZIO%rowtype;
lTbName varchar2(30);
lUser varchar2(30);
lLogRegistry log_registry%rowtype;
lPG_STORICO number;
begin
--
-- Trigger attivato su  INSERT  di record da ESERCIZIO
--
-- Date: 19/03/2004
-- Version: 1.0
--
-- Dependency:CLESERCIZIO
--
-- History:
--
-- Date: 19/03/2004
-- Version: 1.0
-- Creazione
--
-- Body:
  select user into lUser from dual;
  lTbName :='ESERCIZIO';
  select nvl(max(PG_STORICO_),0) into lPG_STORICO from L_ESERCIZIO;
  lESERCIZIO.CD_CDS := :NEW.CD_CDS;
  lESERCIZIO.ESERCIZIO := :NEW.ESERCIZIO;
  lESERCIZIO.DS_ESERCIZIO := :NEW.DS_ESERCIZIO;
  lESERCIZIO.ST_APERTURA_CHIUSURA := :NEW.ST_APERTURA_CHIUSURA;
  lESERCIZIO.DACR := :NEW.DACR;
  lESERCIZIO.UTCR := :NEW.UTCR;
  lESERCIZIO.DUVA := :NEW.DUVA;
  lESERCIZIO.UTUV := :NEW.UTUV;
  lESERCIZIO.PG_VER_REC := :NEW.PG_VER_REC;
  lESERCIZIO.IM_CASSA_INIZIALE := :NEW.IM_CASSA_INIZIALE;
  lESERCIZIO.PG_STORICO_ := lPG_STORICO + 1;
  lESERCIZIO.USER_ := lUser;
  lESERCIZIO.DT_TRANSACTION_ :=  sysdate;
  lESERCIZIO.ACTION_ := 'I';
  CLESERCIZIO.insertRiga(lTbName,lESERCIZIO);
  CLESERCIZIO.updateRegistry(lTbName,'I',lUser);
END;
/


