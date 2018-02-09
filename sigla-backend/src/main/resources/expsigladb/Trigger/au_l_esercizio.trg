CREATE OR REPLACE TRIGGER AU_L_ESERCIZIO AFTER  UPDATE  ON ESERCIZIO for each row
DECLARE
lESERCIZIO L_ESERCIZIO%rowtype;
lTbName varchar2(30);
lUser varchar2(30);
lLogRegistry log_registry%rowtype;
lPG_STORICO number;
begin
--
-- Trigger attivato su  UPDATE  di record da ESERCIZIO
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
  lESERCIZIO.CD_CDS := :OLD.CD_CDS;
  lESERCIZIO.ESERCIZIO := :OLD.ESERCIZIO;
  lESERCIZIO.DS_ESERCIZIO := :OLD.DS_ESERCIZIO;
  lESERCIZIO.ST_APERTURA_CHIUSURA := :OLD.ST_APERTURA_CHIUSURA;
  lESERCIZIO.DACR := :OLD.DACR;
  lESERCIZIO.UTCR := :OLD.UTCR;
  lESERCIZIO.DUVA := :OLD.DUVA;
  lESERCIZIO.UTUV := :OLD.UTUV;
  lESERCIZIO.PG_VER_REC := :OLD.PG_VER_REC;
  lESERCIZIO.IM_CASSA_INIZIALE := :OLD.IM_CASSA_INIZIALE;
  lESERCIZIO.PG_STORICO_ := lPG_STORICO + 1;
  lESERCIZIO.USER_ := lUser;
  lESERCIZIO.DT_TRANSACTION_ :=  sysdate;
  lESERCIZIO.ACTION_ := 'U';
  CLESERCIZIO.insertRiga(lTbName,lESERCIZIO);
  CLESERCIZIO.updateRegistry(lTbName,'U',lUser);
END;
/


