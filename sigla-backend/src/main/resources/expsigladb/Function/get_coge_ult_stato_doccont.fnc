CREATE OR REPLACE function         get_coge_ult_stato_doccont(
 aTipoDocCont varchar2,
 aCdCdsDocAmm varchar2,
 aEsObbAcc number,
 aEsOriObbAcc number,
 aPgObbAcc number
) return char is
--==================================================================================================
 aStato  char(1);
begin
  if aTipoDocCont = 'MAN' then
   select
       a.stato_coge_doccont into aStato
   from obbligazione a
    where
  		 a.esercizio = aEsObbAcc
   and  a.esercizio_originale = aEsOriObbAcc
	 and a.cd_cds = aCdCdsDocAmm
	 and a.pg_obbligazione = aPgObbAcc;
   else
    select
         a.stato_coge_doccont into aStato
   from accertamento a
    where
  		 a.esercizio = aEsObbAcc
   and a.esercizio_originale = aEsOriObbAcc
	 and a.cd_cds = aCdCdsDocAmm
	 and a.pg_accertamento = aPgObbAcc;
   end if;
  return aStato;

end;
/


