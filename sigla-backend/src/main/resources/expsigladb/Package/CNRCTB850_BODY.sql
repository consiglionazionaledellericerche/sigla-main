--------------------------------------------------------
--  DDL for Package Body CNRCTB850
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB850" as
 procedure getAndLock(aDest IN OUT CONTROLLO_ACCESSO%rowtype) is
 begin
  begin
   aDest.stato := STATO_ACCESSIBILE;
   aDest.dacr := sysdate;
   aDest.duva := sysdate;
   aDest.utcr := aDest.utuv;
   aDest.pg_ver_rec := 1;
   ins_CONTROLLO_ACCESSO (aDest);
  exception when dup_val_on_index then
   begin
    select * into aDest from controllo_accesso where
                esercizio = aDest.esercizio
      and cd_cds = aDest.cd_cds
    for update nowait;
   exception when RESOURCE_BUSY then
    begin
     select * into aDest from controllo_accesso where
                 esercizio = aDest.esercizio
       and cd_cds = aDest.cd_cds
     for update nowait;
    exception when RESOURCE_BUSY then
     begin
      select * into aDest from controllo_accesso where
                 esercizio = aDest.esercizio
        and cd_cds = aDest.cd_cds
      for update nowait;
     exception when RESOURCE_BUSY then
      IBMERR001.RAISE_ERR_GENERICO('Applicazione temporaneamente non accessibile per l''esercizio e il cds selezionati.');
     end; -- End exception terzo tentativo fallito
    end; -- End exception secondo tentativo fallito
   end; -- End exception primo tentativo fallito
  end;
 end;

 procedure register(aCdCds varchar2,aEs number,aCdUtente varchar2,aIdSessione varchar2,aIdClone varchar2) is
  aDest CONTROLLO_ACCESSO%rowtype;
  aDestUtente CONTROLLO_ACCESSO_UTENTE%rowtype;
  aDestCds varchar2(30);
  pragma autonomous_transaction;
 begin
  if aCdCds is null then
   select cd_cds_configuratore into aDestCds from utente where cd_utente = aCdUtente;
   if aDestCds = '*' then
    select cd_unita_organizzativa into aDestCds from unita_organizzativa where cd_tipo_unita = 'ENTE' and fl_cds = 'Y';
   end if;
   aDest.cd_cds:=aDestCds;
  else
   aDest.cd_cds:=aCdCds;
  end if;
  aDest.esercizio:=aEs;
  aDest.utuv:=aCdUtente;
  getAndLock(aDest);
  aDestUtente.cd_cds := aDest.cd_cds;
  aDestUtente.esercizio := aDest.esercizio;
  aDestUtente.cd_utente := aCdUtente;
  aDestUtente.id_sessione := aIdSessione;
  aDestUtente.id_clone := aIdClone;
  aDestUtente.dacr := sysdate;
  aDestUtente.duva := sysdate;
  aDestUtente.utcr := aCdUtente;
  aDestUtente.utuv := aCdUtente;
  aDestUtente.pg_ver_rec := 1;
  begin
   ins_CONTROLLO_ACCESSO_UTENTE (aDestUtente);
  exception when dup_val_on_index then
   null;
  end;
  commit;
 exception when others then
  rollback;
 end;

 procedure unregister(aIdSessione varchar2) is
  aDest CONTROLLO_ACCESSO_UTENTE%rowtype;
  pragma autonomous_transaction;
 begin
  select * into aDest from CONTROLLO_ACCESSO_UTENTE where
      id_sessione=aIdSessione
   for update nowait;
  delete from controllo_accesso_utente where
       cd_cds = aDest.cd_cds
   and esercizio = aDest.esercizio
   and id_sessione = aIdSessione;
  commit;
 exception when others then
  rollback;
 end;

 procedure unregisterAll(aIdClone varchar2) is
  aDest CONTROLLO_ACCESSO_UTENTE%rowtype;
  pragma autonomous_transaction;
 begin
  for aDest in ( select * from CONTROLLO_ACCESSO_UTENTE where
      id_clone=aIdClone
   for update nowait ) loop
   null;
  end loop;
  delete from controllo_accesso_utente where
       id_clone = aIdClone;
  commit;
 exception when others then
  rollback;
 end;

 procedure ins_CONTROLLO_ACCESSO (aDest CONTROLLO_ACCESSO%rowtype) is
  begin
   insert into CONTROLLO_ACCESSO (
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
 procedure ins_CONTROLLO_ACCESSO_UTENTE (aDest CONTROLLO_ACCESSO_UTENTE%rowtype) is
  begin
   insert into CONTROLLO_ACCESSO_UTENTE (
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
end;
