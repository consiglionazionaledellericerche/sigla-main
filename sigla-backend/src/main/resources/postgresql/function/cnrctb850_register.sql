-- Function: public.cnrctb850_register(text, bigint, text, text, text)

-- DROP FUNCTION public.cnrctb850_register(text, bigint, text, text, text);

CREATE OR REPLACE FUNCTION cnrctb850_register(
    acdcds text,
    aes bigint,
    acdutente text,
    aidsessione text,
    aidclone text)
  RETURNS void AS
$BODY$
DECLARE

  aDest CONTROLLO_ACCESSO%rowtype;
  aDestUtente CONTROLLO_ACCESSO_UTENTE%rowtype;
  aDestCds varchar(30);
BEGIN
  if coalesce(aCdCds::text, '') = '' then
   select cd_cds_configuratore into STRICT aDestCds from utente where cd_utente = aCdUtente;
   if aDestCds = '*' then
    select cd_unita_organizzativa into STRICT aDestCds from unita_organizzativa where cd_tipo_unita = 'ENTE' and fl_cds = 'Y';
   end if;
   aDest.cd_cds:=aDestCds;
  else
   aDest.cd_cds:=aCdCds;
  end if;
  aDest.esercizio:=aEs;
  aDest.utuv:=aCdUtente;
  aDest := cnrctb850_getandlock(aDest);
  aDestUtente.cd_cds := aDest.cd_cds;
  aDestUtente.esercizio := aDest.esercizio;
  aDestUtente.cd_utente := aCdUtente;
  aDestUtente.id_sessione := aIdSessione;
  aDestUtente.id_clone := aIdClone;
  aDestUtente.dacr := clock_timestamp();
  aDestUtente.duva := clock_timestamp();
  aDestUtente.utcr := aCdUtente;
  aDestUtente.utuv := aCdUtente;
  aDestUtente.pg_ver_rec := 1;
  begin
   PERFORM cnrctb850_ins_controllo_accesso_utente(aDestUtente);
  exception when unique_violation then
   null;
  end;
 end;

$BODY$
  LANGUAGE plpgsql STABLE SECURITY DEFINER
  COST 100;
