--------------------------------------------------------
--  DDL for Package Body CNRCTB800
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB800" is

 procedure acquisisciSemaforo(aEs number, aCdUO varchar2, aTipoSem varchar2, aUser varchar2) is
  aSem semaforo_base%rowtype;
  aTSNow date;
 begin
  aTSNow:=sysdate;
  begin
   select * into aSem from SEMAFORO_BASE where
         cd_tipo_semaforo = aTipoSem
     and esercizio = aEs
     and cd_unita_organizzativa = aCdUO
 	 and cd_cds = INDEFINITO
 	 and cd_centro_responsabilita = INDEFINITO for update nowait;
   update semaforo_base set
    utuv=aUser,
	duva=aTSNow,
	pg_ver_rec=pg_ver_rec+1
   where
         cd_tipo_semaforo = aTipoSem
     and esercizio = aEs
     and cd_unita_organizzativa = aCdUO
 	 and cd_cds = INDEFINITO
 	 and cd_centro_responsabilita = INDEFINITO;
  exception
   when NO_DATA_FOUND then
    aSem.CD_TIPO_SEMAFORO:=aTipoSem;
    aSem.CD_CDS:=INDEFINITO;
    aSem.CD_UNITA_ORGANIZZATIVA:=aCdUO;
    aSem.CD_CENTRO_RESPONSABILITA:=INDEFINITO;
    aSem.ESERCIZIO:=aEs;
    aSem.UTCR:=aUser;
    aSem.DACR:=aTSNow;
    aSem.UTUV:=aUser;
    aSem.DUVA:=aTSNow;
    aSem.PG_VER_REC:=1;
    ins_SEMAFORO_BASE(aSem);
    select * into aSem from SEMAFORO_BASE where
         cd_tipo_semaforo = aTipoSem
     and esercizio = aEs
     and cd_unita_organizzativa = aCdUO
 	 and cd_cds = INDEFINITO
 	 and cd_centro_responsabilita = INDEFINITO for update nowait;
   when DUP_VAL_ON_INDEX  then
    IBMERR001.RAISE_ERR_GENERICO('Errore interno di acquisizione semaforo applicativo: più di un semaforo trovato per la funzione specificata');
   end;
 end;




 procedure ins_SEMAFORO_BASE (aDest SEMAFORO_BASE%rowtype) is
  pragma autonomous_transaction;
 begin
   insert into SEMAFORO_BASE (
     CD_TIPO_SEMAFORO
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CENTRO_RESPONSABILITA
    ,ESERCIZIO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.CD_TIPO_SEMAFORO
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.ESERCIZIO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
  commit;
 exception when OTHERS then
  rollback;
  raise;
 end;

 -- SEMAFORO STATICO

 function getStatoSemStaticoCds(aEs number, aCdCds varchar2, aTipoSem varchar2, isLock boolean) return char is
  aStato char;
 begin
  if isLock then
   select stato into aStato from SEMAFORO_STATICO where
        cd_tipo_semaforo = aTipoSem
    and esercizio = aEs
    and cd_unita_organizzativa = INDEFINITO
	and cd_cds = aCdCds
    and cd_centro_responsabilita = INDEFINITO
	for update nowait;
  else
   select stato into aStato from SEMAFORO_STATICO where
        cd_tipo_semaforo = aTipoSem
    and esercizio = aEs
    and cd_unita_organizzativa = INDEFINITO
	and cd_cds = aCdCds
    and cd_centro_responsabilita = INDEFINITO;
  end if;
  return aStato;
 end;

 procedure acquisisciSemStaticoCds(aEs number, aCdCds varchar2, aTipoSem varchar2, aUser varchar2) is
  aSem semaforo_statico%rowtype;
  aTSNow date;
  pragma autonomous_transaction;
 begin
  aTSNow:=sysdate;
  begin
   if (getStatoSemStaticoCds(aEs, aCdCds, aTipoSem, true)=SEMAFORO_ROSSO) then
    IBMERR001.RAISE_ERR_GENERICO('Operazione non permessa al momento (operazione già in corso per un altro utente o semaforo applicativo bloccato '||'sem:'||aTipoSem||' es:'||aEs||' cds:'||aCdCds);
   end if;
   update semaforo_statico set
    stato=SEMAFORO_ROSSO,
    utuv=aUser,
	duva=aTSNow,
	pg_ver_rec=pg_ver_rec+1
   where
         cd_tipo_semaforo = aTipoSem
     and esercizio = aEs
     and cd_unita_organizzativa = INDEFINITO
 	 and cd_cds = aCdCds
 	 and cd_centro_responsabilita = INDEFINITO;
   COMMIT;
  exception
   when NO_DATA_FOUND then
    aSem.CD_TIPO_SEMAFORO:=aTipoSem;
    aSem.CD_CDS:=aCdCds;
    aSem.CD_UNITA_ORGANIZZATIVA:=INDEFINITO;
    aSem.CD_CENTRO_RESPONSABILITA:=INDEFINITO;
    aSem.ESERCIZIO:=aEs;
	aSem.STATO:=SEMAFORO_ROSSO;
    aSem.UTCR:=aUser;
    aSem.DACR:=aTSNow;
    aSem.UTUV:=aUser;
    aSem.DUVA:=aTSNow;
    aSem.PG_VER_REC:=1;
    ins_SEMAFORO_STATICO(aSem);
    COMMIT;
   when OTHERS then
    ROLLBACK;
    IBMERR001.RAISE_ERR_GENERICO('Errore in fase di acquisizione del semaforo applicativo');
   end;
 end;

 function isSemStaticoCdsBloccato(aEs number, aCdCds varchar2, aTipoSem varchar2) return boolean is
 begin
  if(getStatoSemStaticoCds(aEs, aCdCds, aTipoSem, false)=SEMAFORO_ROSSO) then
   return true;
  end if;
  return false;
 exception when NO_DATA_FOUND then
  return false;
 end;

 procedure liberaSemStaticoCds(aEs number, aCdCds varchar2, aTipoSem varchar2, aUser varchar2) is
  aTSNow date;
  pragma autonomous_transaction;
 begin
  aTSNow:=sysdate;
  begin
   -- La lettura non è effettuata in modo lockante per evitare che al momento del riulascio del semaforo
   -- Qualcun altro se ne sia impadronito (anche solo per controllo)
   if not (getStatoSemStaticoCds(aEs, aCdCds, aTipoSem,false)=SEMAFORO_ROSSO) then
    IBMERR001.RAISE_ERR_GENERICO('Tentativo di liberazione di un semaforo statico non acquisito in precedenza');
   end if;
   update semaforo_statico set
    stato=SEMAFORO_VERDE,
    utuv=aUser,
	duva=aTSNow,
	pg_ver_rec=pg_ver_rec+1
   where
         cd_tipo_semaforo = aTipoSem
     and esercizio = aEs
     and cd_unita_organizzativa = INDEFINITO
 	 and cd_cds = aCdCds
 	 and cd_centro_responsabilita = INDEFINITO;
   COMMIT;
  exception
   when NO_DATA_FOUND then
    ROLLBACK;
    IBMERR001.RAISE_ERR_GENERICO('Tentativo di liberazione di un semaforo statico non acquisito in precedenza');
   when OTHERS then
    ROLLBACK;
    IBMERR001.RAISE_ERR_GENERICO('Errore in fase di rilascio del semaforo applicativo');
  end;
 end;

 procedure ins_SEMAFORO_STATICO (aDest SEMAFORO_STATICO%rowtype) is
 begin
   insert into SEMAFORO_STATICO (
     CD_TIPO_SEMAFORO
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CENTRO_RESPONSABILITA
    ,ESERCIZIO
	,STATO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.CD_TIPO_SEMAFORO
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.ESERCIZIO
	,aDest.STATO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

end;
