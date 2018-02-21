-- Function: public.ibmutl001_unlock_transaction()

-- DROP FUNCTION public.ibmutl001_unlock_transaction();

CREATE OR REPLACE FUNCTION ibmutl001_unlock_transaction()
  RETURNS void AS
$BODY$
DECLARE

  aSID text;

BEGIN
  select to_char(pg_backend_pid(), '999999999') into aSID;
  delete from TRANSLOCK where
     sid = aSID;
END;


$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;