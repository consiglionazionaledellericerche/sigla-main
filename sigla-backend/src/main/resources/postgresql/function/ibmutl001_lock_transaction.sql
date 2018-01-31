-- Function: public.ibmutl001_lock_transaction()

-- DROP FUNCTION public.ibmutl001_lock_transaction();

CREATE OR REPLACE FUNCTION ibmutl001_lock_transaction()
  RETURNS void AS
$BODY$
DECLARE
  aSID integer;
BEGIN
  select pg_backend_pid() into aSID;
  insert into TRANSLOCK(SID,STATUS) VALUES (aSID,0);
END;


$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;