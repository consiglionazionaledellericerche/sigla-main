-- Function: public.ibmutl001_lock_transaction()

-- DROP FUNCTION public.ibmutl001_lock_transaction();

CREATE OR REPLACE FUNCTION ibmutl001_lock_transaction()
  RETURNS void AS
$BODY$
DECLARE
  aSID text;
BEGIN
  select to_char(pg_backend_pid(), '999999999') into aSID;
  insert into TRANSLOCK(SID,STATUS) VALUES (aSID,0);
END;


$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;