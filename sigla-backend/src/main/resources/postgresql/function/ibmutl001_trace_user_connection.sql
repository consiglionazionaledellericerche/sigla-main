-- Function: public.ibmutl001_trace_user_connection(text, timestamp without time zone, text)

-- DROP FUNCTION public.ibmutl001_trace_user_connection(text, timestamp without time zone, text);

CREATE OR REPLACE FUNCTION ibmutl001_trace_user_connection(
    auser text,
    atsnow timestamp without time zone,
    ahttpsid text)
  RETURNS void AS
$BODY$
BEGIN
  --DBMS_APPLICATION_INFO.SET_CLIENT_INFO(rpad(aUser,20,' ')|| to_char(aTSNow,'YYYY-MM-DD HH:MI:SS')||aHTTPSID);
END;


$BODY$
  LANGUAGE plpgsql STABLE SECURITY DEFINER
  COST 100;