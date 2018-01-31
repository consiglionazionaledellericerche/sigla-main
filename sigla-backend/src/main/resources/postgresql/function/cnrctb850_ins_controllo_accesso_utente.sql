-- Function: public.cnrctb850_ins_controllo_accesso_utente(controllo_accesso_utente)

-- DROP FUNCTION public.cnrctb850_ins_controllo_accesso_utente(controllo_accesso_utente);

CREATE OR REPLACE FUNCTION cnrctb850_ins_controllo_accesso_utente(adest controllo_accesso_utente)
  RETURNS void AS
$BODY$
BEGIN
   insert into CONTROLLO_ACCESSO_UTENTE(
     ESERCIZIO
    ,ID_SESSIONE
	,ID_CLONE
    ,CD_UTENTE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CDS
   ) values (
     aDest.ESERCIZIO
    ,aDest.ID_SESSIONE
	,aDest.ID_CLONE
    ,aDest.CD_UTENTE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS
    );
 end;

$BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 100;