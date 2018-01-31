-- Function: public.ibmutl001_unlock_transaction()

-- DROP FUNCTION public.ibmutl001_unlock_transaction();

CREATE OR REPLACE FUNCTION ibmutl001_unlock_transaction()
  RETURNS void AS
$BODY$
DECLARE

  aSID integer;

BEGIN
  select pg_backend_pid() into aSID;
  delete from TRANSLOCK where
     sid = aSID;
END;


$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;