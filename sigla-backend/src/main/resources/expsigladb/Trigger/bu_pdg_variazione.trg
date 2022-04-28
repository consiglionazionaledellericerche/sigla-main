CREATE OR REPLACE TRIGGER BU_PDG_VARIAZIONE
  BEFORE UPDATE OF STATO
  ON PDG_VARIAZIONE
  FOR EACH ROW
BEGIN

 IF :OLD.STATO = 'APF' AND :NEW.STATO = 'ANN' THEN
  IBMERR001.RAISE_ERR_GENERICO('La variazione risulta approvata formalmente, annullamento impossibile.');
 END IF;
END;
/

