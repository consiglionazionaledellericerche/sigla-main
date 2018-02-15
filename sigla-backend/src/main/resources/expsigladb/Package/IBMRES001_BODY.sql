--------------------------------------------------------
--  DDL for Package Body IBMRES001
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "IBMRES001" is
 procedure creaFoto(aTempPrefix char, aNumImmagine number) is
  aNumTXTables number;
  aSql varchar2(1000);
  aCount number;
 begin
  if aNumImmagine is null or aNumImmagine > 9 or aNumImmagine < 0 then
   IBMERR001.RAISE_ERR_GENERICO('Il numero di immagine deve essere compreso tra 0 e 9.' );
  end if;
  select count(*) into aNumTXTables from user_tables where table_name like aTempPrefix||aNumImmagine||DOLLARO||'%';
  if aNumTXTables > 0 then
   IBMERR001.RAISE_ERR_GENERICO('Esiste un save set '||aTempPrefix||aNumImmagine||DOLLARO||'* nella base dati. Eliminarlo prima di rieffettuare l''operazione di creazione.' );
   return;
  end if;
  for aTab in (select * from user_tables where 
                              substr(table_name,1,3) not like TEMP_PREFIX||'_$'
                          and substr(table_name,1,3) not like BKUP_TEMP_PREFIX||'_$'
			   order by table_name
			  ) loop
   aSql:='select count(*) from '||aTab.table_name;
   execute immediate aSql into aCount;
   if aCount > 0 then
    aSql:='drop table '||aTempPrefix||aNumImmagine||DOLLARO||aTab.table_name||' cascade constraints';
    begin
     execute immediate aSql using aCount;
    exception when others then
     null;
    end;
    aSql:='create table '||aTempPrefix||aNumImmagine||DOLLARO||aTab.table_name||' as (select * from '||aTab.table_name||')';
    execute immediate aSql;   
   end if;
  end loop;
 end;
 
 procedure eliminaFoto(aTempPrefix char, aNumImmagine number) is
  aSql varchar2(500);
 begin
  if aNumImmagine is null or aNumImmagine > 9 or aNumImmagine < 0 then
   IBMERR001.RAISE_ERR_GENERICO('Il numero di immagine deve essere compreso tra 0 e 9.' );
  end if;
  for aTab in (select * from user_tables where table_name like aTempPrefix||aNumImmagine||DOLLARO||'%' order by table_name) loop
   aSql:='drop table '||aTab.table_name||' cascade constraints';
   execute immediate aSql;
  end loop;
 end;

 procedure creaFoto(aNumImmagine number) is
 begin
  creaFoto(TEMP_PREFIX, aNumImmagine);
 end; 

 procedure eliminaFoto(aNumImmagine number) is
 begin
  eliminaFoto(TEMP_PREFIX, aNumImmagine);
 end;

 procedure creaFotoBackup(aNumImmagine number) is
 begin
  creaFoto(BKUP_TEMP_PREFIX, aNumImmagine);
 end; 

 procedure eliminaFotoBackup(aNumImmagine number) is
 begin
  eliminaFoto(BKUP_TEMP_PREFIX, aNumImmagine);
 end;
 
-- Restore di una foto precedentemente effettuata
 procedure restoreFoto(aNumImmagine number) is
  aSql varchar2(500);
  aNumTXTables number;
 begin
 -- Verifica che il save set da restorare esista
  select count(*) into aNumTXTables from user_tables where table_name like TEMP_PREFIX||aNumImmagine||DOLLARO||'%';
  if aNumTXTables = 0 then
   IBMERR001.RAISE_ERR_GENERICO('Non esiste un save set '||TEMP_PREFIX||aNumImmagine||DOLLARO||'* nella base dati.' );
   return;
  end if;
 -- SALVA IL CONTENUTO ATTUALE DELLE TABELLE
--  creaFotoBackup(aNumImmagine);
 -- DISABILITA I CONSTRAINTS
  for aTab in (select * from user_tables order by table_name) loop
   for aCons in (select * from user_constraints where
              table_name = aTab.table_name
 		 and constraint_type != 'P'
   ) loop
    EXECUTE IMMEDIATE 'alter table '||aTab.table_name||' modify constraint '||aCons.constraint_name||' disable'; 
   end loop;
   for aTrig in (select * from user_triggers where
          table_name = aTab.table_name
   ) loop
    EXECUTE IMMEDIATE 'alter trigger '||aTrig.trigger_name||' disable'; 
   end loop;
  end loop;
 -- FINE DISABILITAZIONE DEI CONSTRAINTS
 -- START TRUNCATING ACTUAL TABLE CONTENT
  for aTab in (select * from user_tables where    
       substr(table_name,1,3) not like TEMP_PREFIX||'_$'
   and substr(table_name,1,3) not like BKUP_TEMP_PREFIX||'_$'
  order by table_name) loop
   aSql:='lock table '||aTab.table_name||' in exclusive mode';
   execute immediate aSql;
   aSql:='truncate table '||aTab.table_name;
   execute immediate aSql;
  end loop;
  for aTab in (select * from user_tables a where exists (select 1 from user_tables where table_name = TEMP_PREFIX||aNumImmagine||DOLLARO||a.table_name) order by table_name) loop
   aSql:='lock table '||aTab.table_name||' in exclusive mode';
   execute immediate aSql;
   aSql:='insert into '||aTab.table_name||' (select * from '||TEMP_PREFIX||aNumImmagine||DOLLARO||aTab.table_name||')';
   execute immediate aSql;
   commit;
  end loop;
  -- RIABILITA I CONSTRAINTS
  for aTab in (select * from user_tables order by table_name) loop
   for aCons in (select * from user_constraints where
              table_name = aTab.table_name
 		 and constraint_type != 'P'
   ) loop
    EXECUTE IMMEDIATE 'alter table '||aTab.table_name||' modify constraint '||aCons.constraint_name||' enable'; 
   end loop;
   for aTrig in (select * from user_triggers where
          table_name = aTab.table_name
   ) loop
    EXECUTE IMMEDIATE 'alter trigger '||aTrig.trigger_name||' enable';
   end loop;
  end loop;
 end;
 
end;
