-- Function: public.decode(text, text, text, text)

-- DROP FUNCTION public.decode(text, text, text, text);

CREATE OR REPLACE FUNCTION decode(
    arg1 text,
    arg2 text,
    arg3 text,
    arg4 text)
  RETURNS text AS
$BODY$
BEGIN
	return CASE WHEN arg1=arg2 THEN arg3 ELSE arg4 END;
END;


$BODY$
  LANGUAGE plpgsql STABLE SECURITY DEFINER
  COST 100;