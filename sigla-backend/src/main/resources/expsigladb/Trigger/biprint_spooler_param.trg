CREATE OR REPLACE TRIGGER BIPRINT_SPOOLER_PARAM
BEFORE INSERT
ON PRINT_SPOOLER_PARAM
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
BEGIN
  If :new.valore_param is Null Then
    :new.valore_param := '*';
  End If;
  if :new.valore_param = '9999999999' and :new.param_type = 'java.lang.Integer' Then
    :new.valore_param := '999999';
  end if;
  if :new.param_type = 'java.lang.Date' then
    :new.param_type := 'java.util.Date';
  end If;
END BIPRINT_SPOOLER_PARAM;
/


