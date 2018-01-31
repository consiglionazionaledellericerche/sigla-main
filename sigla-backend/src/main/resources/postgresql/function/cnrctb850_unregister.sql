-- Function: public.cnrctb850_unregister(text)

-- DROP FUNCTION public.cnrctb850_unregister(text);

CREATE OR REPLACE FUNCTION cnrctb850_unregister(aidsessione text)
  RETURNS void AS
$BODY$
DECLARE
  aDest CONTROLLO_ACCESSO_UTENTE;
BEGIN
  select * into STRICT aDest from CONTROLLO_ACCESSO_UTENTE where
      id_sessione=aIdSessione
   for update nowait;
  delete from controllo_accesso_utente where
       cd_cds = aDest.cd_cds
   and esercizio = aDest.esercizio
   and id_sessione = aIdSessione;
 exception when NO_DATA_FOUND then
    RETURN;
 END;


$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;

