-- Function: public.ibmutl001_getdbrefreshdate()

-- DROP FUNCTION public.ibmutl001_getdbrefreshdate();

CREATE OR REPLACE FUNCTION ibmutl001_getdbrefreshdate()
  RETURNS timestamp without time zone AS
$BODY$
DECLARE

  DateRefresh  timestamp;

BEGIN
  SELECT (pg_stat_file('base/'||oid ||'/PG_VERSION')).modification  into DateRefresh FROM pg_database
  where datname = current_database();
   Return DateRefresh;
Exception
  When Others Then
    Return clock_timestamp();
END;

$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;