CREATE OR REPLACE function         get_coge_ult_voce(
 aTipoDocCont varchar2,
 aCdCdsDocAmm varchar2,
 aEsObbAcc number,
 aEsOriObbAcc number,
 aPgObbAcc number
) return varchar2 is
--==================================================================================================
 aVoce elemento_voce.cd_elemento_voce%type;

begin
  if aTipoDocCont = 'MAN' then
   select
       a.cd_elemento_voce into aVoce
   from obbligazione a
    where
  		 a.esercizio = aEsObbAcc
   and  a.esercizio_originale = aEsOriObbAcc
	 and a.cd_cds = aCdCdsDocAmm
	 and a.pg_obbligazione = aPgObbAcc;
   else
    select
       a.cd_elemento_voce into aVoce
   from accertamento a
    where
  		 a.esercizio = aEsObbAcc
   and a.esercizio_originale = aEsOriObbAcc
	 and a.cd_cds = aCdCdsDocAmm
	 and a.pg_accertamento = aPgObbAcc;
   end if;
  return aVoce;

end;
/


