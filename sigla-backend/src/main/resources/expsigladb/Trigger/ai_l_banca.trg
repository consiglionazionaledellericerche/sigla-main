CREATE OR REPLACE TRIGGER AI_L_BANCA AFTER  INSERT  ON BANCA for each row
DECLARE
lBANCA L_BANCA%rowtype;
lTbName varchar2(30);
lUser varchar2(30);
lLogRegistry log_registry%rowtype;
lPG_STORICO number;
begin
--
-- Trigger attivato su  INSERT  di record da BANCA
--
-- Date: 19/03/2004
-- Version: 1.0
--
-- Dependency:CLBANCA
--
-- History:
--
-- Date: 19/03/2004
-- Version: 1.0
-- Creazione
--
-- Body:
  select user into lUser from dual;
  lTbName :='BANCA';
  select nvl(max(PG_STORICO_),0) into lPG_STORICO from L_BANCA;
  lBANCA.CIN := :NEW.CIN;
  lBANCA.CD_TERZO := :NEW.CD_TERZO;
  lBANCA.PG_BANCA := :NEW.PG_BANCA;
  lBANCA.CAB := :NEW.CAB;
  lBANCA.ABI := :NEW.ABI;
  lBANCA.DACR := :NEW.DACR;
  lBANCA.INTESTAZIONE := :NEW.INTESTAZIONE;
  lBANCA.QUIETANZA := :NEW.QUIETANZA;
  lBANCA.NUMERO_CONTO := :NEW.NUMERO_CONTO;
  lBANCA.TI_PAGAMENTO := :NEW.TI_PAGAMENTO;
  lBANCA.CODICE_IBAN := :NEW.CODICE_IBAN;
  lBANCA.CODICE_SWIFT := :NEW.CODICE_SWIFT;
  lBANCA.UTCR := :NEW.UTCR;
  lBANCA.DUVA := :NEW.DUVA;
  lBANCA.UTUV := :NEW.UTUV;
  lBANCA.PG_VER_REC := :NEW.PG_VER_REC;
  lBANCA.FL_CANCELLATO := :NEW.FL_CANCELLATO;
  lBANCA.CD_TERZO_DELEGATO := :NEW.CD_TERZO_DELEGATO;
  lBANCA.PG_BANCA_DELEGATO := :NEW.PG_BANCA_DELEGATO;
  lBANCA.ORIGINE := :NEW.ORIGINE;
  lBANCA.FL_CC_CDS := :NEW.FL_CC_CDS;
  lBANCA.PG_STORICO_ := lPG_STORICO + 1;
  lBANCA.USER_ := lUser;
  lBANCA.DT_TRANSACTION_ :=  sysdate;
  lBANCA.ACTION_ := 'I';
  CLBANCA.insertRiga(lTbName,lBANCA);
  CLBANCA.updateRegistry(lTbName,'I',lUser);
END;
/


