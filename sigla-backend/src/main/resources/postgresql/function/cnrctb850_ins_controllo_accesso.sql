-- Function: public.cnrctb850_ins_controllo_accesso(controllo_accesso)

-- DROP FUNCTION public.cnrctb850_ins_controllo_accesso(controllo_accesso);

CREATE OR REPLACE FUNCTION cnrctb850_ins_controllo_accesso(adest controllo_accesso)
  RETURNS void AS
$BODY$
BEGIN
   insert into CONTROLLO_ACCESSO(
     CD_CDS
    ,ESERCIZIO
    ,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;