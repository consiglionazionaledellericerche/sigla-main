CREATE OR REPLACE TRIGGER AU_L_BANCA AFTER  UPDATE  ON BANCA for each row
DECLARE
lBANCA L_BANCA%rowtype;
lTbName varchar2(30);
lUser varchar2(30);
lLogRegistry log_registry%rowtype;
lPG_STORICO number;
begin
--
-- Trigger attivato su  UPDATE  di record da BANCA
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
  lBANCA.CIN := :OLD.CIN;
  lBANCA.CD_TERZO := :OLD.CD_TERZO;
  lBANCA.PG_BANCA := :OLD.PG_BANCA;
  lBANCA.CAB := :OLD.CAB;
  lBANCA.ABI := :OLD.ABI;
  lBANCA.DACR := :OLD.DACR;
  lBANCA.INTESTAZIONE := :OLD.INTESTAZIONE;
  lBANCA.QUIETANZA := :OLD.QUIETANZA;
  lBANCA.NUMERO_CONTO := :OLD.NUMERO_CONTO;
  lBANCA.TI_PAGAMENTO := :OLD.TI_PAGAMENTO;
  lBANCA.CODICE_IBAN := :OLD.CODICE_IBAN;
  lBANCA.CODICE_SWIFT := :OLD.CODICE_SWIFT;
  lBANCA.UTCR := :OLD.UTCR;
  lBANCA.DUVA := :OLD.DUVA;
  lBANCA.UTUV := :OLD.UTUV;
  lBANCA.PG_VER_REC := :OLD.PG_VER_REC;
  lBANCA.FL_CANCELLATO := :OLD.FL_CANCELLATO;
  lBANCA.CD_TERZO_DELEGATO := :OLD.CD_TERZO_DELEGATO;
  lBANCA.PG_BANCA_DELEGATO := :OLD.PG_BANCA_DELEGATO;
  lBANCA.ORIGINE := :OLD.ORIGINE;
  lBANCA.FL_CC_CDS := :OLD.FL_CC_CDS;
  lBANCA.PG_STORICO_ := lPG_STORICO + 1;
  lBANCA.USER_ := lUser;
  lBANCA.DT_TRANSACTION_ :=  sysdate;
  lBANCA.ACTION_ := 'U';
  CLBANCA.insertRiga(lTbName,lBANCA);
  CLBANCA.updateRegistry(lTbName,'U',lUser);
END;
/


