-- Function: public.cnrctb850_getandlock(controllo_accesso)

-- DROP FUNCTION public.cnrctb850_getandlock(controllo_accesso);

CREATE OR REPLACE FUNCTION cnrctb850_getandlock(INOUT adest controllo_accesso)
  RETURNS controllo_accesso AS
$BODY$
BEGIN
  begin
   aDest.stato := 'A';
   aDest.dacr := clock_timestamp();
   aDest.duva := clock_timestamp();
   aDest.utcr := aDest.utuv;
   aDest.pg_ver_rec := 1;
   PERFORM cnrctb850_ins_controllo_accesso(aDest);
  exception when unique_violation then
   begin
    select * into STRICT aDest from controllo_accesso where
                esercizio = aDest.esercizio
      and cd_cds = aDest.cd_cds
    for update nowait;
   exception when SQLSTATE '50003' then
    begin
     select * into STRICT aDest from controllo_accesso where
                 esercizio = aDest.esercizio
       and cd_cds = aDest.cd_cds
     for update nowait;
    exception when SQLSTATE '50003' then
     begin
      select * into STRICT aDest from controllo_accesso where
                 esercizio = aDest.esercizio
        and cd_cds = aDest.cd_cds
      for update nowait;
     exception when SQLSTATE '50003' then
      PERFORM ibmerr001_raise_err_generico('Applicazione temporaneamente non accessibile per l''esercizio e il cds selezionati.');
     end; -- End exception terzo tentativo fallito
    end; -- End exception secondo tentativo fallito
   end; -- End exception primo tentativo fallito
  end;
 end;


$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;
