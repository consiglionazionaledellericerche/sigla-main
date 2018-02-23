--------------------------------------------------------
--  DDL for Package Body IBMUTL100
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "IBMUTL100" is
  function getPkCols(aTableName varchar2) return IBMCST001.vcarray is
   aA IBMCST001.vcarray:=IBMCST001.vcarray();
   k NUMBER;
   aOwner varchar2(35);
  begin
   select username into aOwner from user_users;
   k:=1;
   for aVar in (select column_name from all_cons_columns a, all_constraints b
      where
            a.table_name = b.table_name
        and a.constraint_name = b.constraint_name
        and a.owner=b.owner
        and constraint_type='P'
        and a.table_name = upper(aTableName)
        and a.owner = upper(aOwner)
   ) loop
    aA.EXTEND;
    aA(k):=aVar.column_name;
    k:=k+1;
   end loop;
   return aA;
  end;


  procedure copiaDatiEter(destinazione varchar2, origine varchar2) is
      aStatement varchar2(4000);
      cid NUMBER;
      k INTEGER;
      nRows INTEGER;
      aOwner varchar2(35);
     begin
	  select username into aOwner from user_users;
      k:=0;
      aStatement:='insert into '||aOwner||'.'||destinazione||' (';
      for aCol in (
       select a.* from all_tab_columns a, all_tab_columns b
        where
              a.owner = b.owner
     	 and a.table_name = origine
     	 and b.table_name = destinazione
          and a.column_name = b.column_name
      ) loop
       if k=0 then
        aStatement:=aStatement||aCol.column_name;
       else
        aStatement:=aStatement||','||aCol.column_name;
       end if;
       k:=k+1;
      end loop;
      aStatement:=aStatement||') (select ';
      k:=0;
      for aCol in (
       select a.* from all_tab_columns a, all_tab_columns b
        where
              a.owner = b.owner
     	 and a.table_name = origine
     	 and b.table_name = destinazione
          and a.column_name = b.column_name
      ) loop
       if k=0 then
        aStatement:=aStatement||aCol.column_name;
       else
        aStatement:=aStatement||','||aCol.column_name;
       end if;
       k:=k+1;
      end loop;
      aStatement:=aStatement||' from '||aOwner||'.'||origine||')';
     -- insert into prova values(aStatement);
      begin
       /* Open new cursor and return cursor ID. */
       cid := DBMS_SQL.OPEN_CURSOR;
       /* Parse and immediately execute dynamic SQL statement built by
           concatenating table name to DROP TABLE command. */
              DBMS_SQL.PARSE(cid, aStatement, dbms_sql.NATIVE);
       nRows:=DBMS_SQL.EXECUTE(cid);
       DBMS_SQL.CLOSE_CURSOR(cid);
      EXCEPTION
       WHEN OTHERS THEN
           DBMS_SQL.CLOSE_CURSOR(cid);
       RAISE;  -- reraise the exception
      end;
     end;

  procedure creaInsertSelectStatement(destinazione varchar2, origine varchar2) is
      k INTEGER;
      nRows INTEGER;
      aOwner varchar2(35);
     begin
	  select username into aOwner from user_users;
      k:=0;
      IBMUTL010.SCRIVI('insert into '||aOwner||'.'||destinazione||' (');
      for aCol in (
       select a.* from all_tab_columns a, all_tab_columns b
        where
              a.owner = b.owner
     	 and a.table_name = origine
     	 and b.table_name = destinazione
          and a.column_name = b.column_name
		  order by a.COLUMN_ID
      ) loop
       if k=0 then
        IBMUTL010.SCRIVI(aCol.column_name);
       else
        IBMUTL010.SCRIVI(','||aCol.column_name);
       end if;
       k:=k+1;
      end loop;
      IBMUTL010.SCRIVI(') (select ');
      k:=0;
      for aCol in (
       select a.* from all_tab_columns a, all_tab_columns b
        where
              a.owner = b.owner
     	 and a.table_name = origine
     	 and b.table_name = destinazione
          and a.column_name = b.column_name
		  order by a.COLUMN_ID
      ) loop
       if k=0 then
        IBMUTL010.SCRIVI(aCol.column_name);
       else
        IBMUTL010.SCRIVI(','||aCol.column_name);
       end if;
       k:=k+1;
      end loop;
      IBMUTL010.SCRIVI(' from '||aOwner||'.'||origine||')');
     end;

    procedure creaCpIntEserProcStatement(destinazione varchar2) is
      k INTEGER;
	  aNomeCol varchar2(50);
	  aOwner varchar2(35);
     begin
	  select username into aOwner from user_users;
      k:=0;
      IBMUTL010.SCRIVI(' procedure coie_'||destinazione||' (aEsDest esercizio%rowtype) is ');
      IBMUTL010.SCRIVI('  begin ');
      IBMUTL010.SCRIVI('   insert into '||destinazione||' (');
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
      ) loop
       if k=0 then
        IBMUTL010.SCRIVI('     '||aCol.column_name);
       else
        IBMUTL010.SCRIVI('    ,'||aCol.column_name);
       end if;
       k:=k+1;
      end loop;
        IBMUTL010.SCRIVI('   ) select ');
      k:=0;
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
      ) loop
	   if upper(aCol.column_name)='ESERCIZIO' then
	    aNomeCol:='aEsDest.esercizio';
	   elsif upper(aCol.column_name)='ESERCIZIO' then
	    aNomeCol:='aEsDest.esercizio';
	   elsif upper(aCol.column_name)='UTCR' then
	    aNomeCol:='aEsDest.utcr';
	   elsif upper(aCol.column_name)='UTUV' then
	    aNomeCol:='aEsDest.utuv';
	   elsif upper(aCol.column_name)='DACR' then
	    aNomeCol:='aEsDest.dacr';
	   elsif upper(aCol.column_name)='DUVA' then
	    aNomeCol:='aEsDest.duva';
	   else
	    aNomeCol:=aCol.column_name;
	   end if;

       if k=0 then
        IBMUTL010.SCRIVI('     '||aNomeCol);
       else
        IBMUTL010.SCRIVI('    ,'||aNomeCol);
       end if;
       k:=k+1;
      end loop;
        IBMUTL010.SCRIVI('  from '||destinazione);
        IBMUTL010.SCRIVI('    where esercizio = 0;');
        IBMUTL010.SCRIVI(' end;');
     end;

  procedure creaInsertProcStatement(destinazione varchar2) is
      k INTEGER;
      aOwner varchar2(35);
     begin
	  select username into aOwner from user_users;
      k:=0;
      IBMUTL010.SCRIVI(' procedure ins_'||destinazione||' (aDest '||destinazione||'%rowtype) is ');
      IBMUTL010.SCRIVI('  begin ');
      IBMUTL010.SCRIVI('   insert into '||destinazione||' (');
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
      ) loop
       if k=0 then
        IBMUTL010.SCRIVI('     '||aCol.column_name);
       else
        IBMUTL010.SCRIVI('    ,'||aCol.column_name);
       end if;
       k:=k+1;
      end loop;
        IBMUTL010.SCRIVI('   ) values (');
      k:=0;
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
      ) loop
       if k=0 then
        IBMUTL010.SCRIVI('     aDest.'||aCol.column_name);
       else
        IBMUTL010.SCRIVI('    ,aDest.'||aCol.column_name);
       end if;
       k:=k+1;
      end loop;
        IBMUTL010.SCRIVI('    );');
        IBMUTL010.SCRIVI(' end;');
     end;

  procedure creaAliasColonne(destinazione varchar2, aColPrefix varchar2, replaceOriginWith varchar2, aAliasPrefix varchar2) is
     aOwner varchar2(35);
    begin
	  select username into aOwner from user_users;
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
		 order by column_id
      ) loop
	    if replaceOriginWith is null then
         IBMUTL010.SCRIVI('     ,'||aColPrefix||aCol.column_name||' '||aAliasPrefix||aCol.column_name);
        else
         IBMUTL010.SCRIVI('     ,'||replaceOriginWith||' '||aAliasPrefix||aCol.column_name);
		end if;
	  end loop;
    end;

  procedure creaAliasColonne(destinazione varchar2, aPrefix varchar2) is
     aOwner varchar2(35);
    begin
	  select username into aOwner from user_users;
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
		 order by column_id
      ) loop
        IBMUTL010.SCRIVI('     ,'||aCol.column_name||' '||aPrefix||aCol.column_name);
      end loop;
    end;

  procedure creaSumColonne(destinazione varchar2) is
     aOwner varchar2(35);
    begin
	  select username into aOwner from user_users;
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
		 order by column_id
      ) loop
        IBMUTL010.SCRIVI('     ,sum('||aCol.column_name||')');
      end loop;
    end;

  procedure creaAssignmentStatement(destinazione varchar2, aRT varchar2) is
     aOwner varchar2(35);
    begin
	  select username into aOwner from user_users;
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
		 order by column_id
      ) loop
	   if aCol.data_type = 'NUMBER' then
        IBMUTL010.SCRIVI('     '||aRT||'.'||aCol.column_name||':=0;');
       elsif aCol.column_name in ('DACR','DUVA') then
        IBMUTL010.SCRIVI('     '||aRT||'.'||aCol.column_name||':=aTSNow;');
       elsif aCol.column_name in ('UTUV','UTCR') then
        IBMUTL010.SCRIVI('     '||aRT||'.'||aCol.column_name||':=aUser;');
       elsif aCol.column_name = 'PG_VER_REC' then
        IBMUTL010.SCRIVI('     '||aRT||'.'||aCol.column_name||':=1;');
	   else
        IBMUTL010.SCRIVI('     '||aRT||'.'||aCol.column_name||':=;');
	   end if;
      end loop;
    end;

   procedure creaAssignmentStatement(destinazione varchar2, aRT varchar2, aRO varchar2) is
     aOwner varchar2(35);
    begin
	  select username into aOwner from user_users;
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
		 order by column_id
      ) loop
        IBMUTL010.SCRIVI('     '||aRT||'.'||aCol.column_name||':='||aRO||'.'||aCol.column_name||';');
      end loop;
    end;


  procedure view_builder(aName varchar2, tabelle IBMCST001.vcarray) as
      cid NUMBER;
      k INTEGER;
      aUser varchar2(50);
      aTableList varchar2(1000);
      aPkColArray IBMCST001.vcarray;
     begin
      k:=1;
      select username into aUser from user_users;
      aTableList:='';
      IBMUTL010.scrivi('create or replace view '||aName||' (');
      for i in tabelle.FIRST .. tabelle.LAST loop
       if i = tabelle.FIRST then
        aTableList:=aTableList||upper(tabelle(i))||' '||CHR(i+96);
       else
        aTableList:=aTableList||','||upper(tabelle(i))||' '||CHR(i+96);
       end if;
       IBMUTL010.scrivi(' -- Table: '||upper(tabelle(i)));
       for aCol in (
        select * from all_tab_columns
         where
               owner = aUser
      	  and table_name = upper(tabelle(i))
       ) loop
        if k=1 then
         IBMUTL010.scrivi('    '||'/* '||LPAD(k,3)||' */  '||aCol.column_name);
        else
         IBMUTL010.scrivi('    '||'/* '||LPAD(k,3)||' */ ,'||aCol.column_name);
        end if;
        k:=k+1;
       end loop;
       IBMUTL010.scrivi('');
      end loop;
      IBMUTL010.scrivi(') as (select ');
      IBMUTL010.scrivi('');
      k:=1;
      for i in tabelle.FIRST .. tabelle.LAST loop
       IBMUTL010.scrivi(' -- Table: '||upper(tabelle(i)));
       for aCol in (
        select * from all_tab_columns
         where
               owner = aUser
      	  and table_name = upper(tabelle(i))
       ) loop
        if k=1 then
         IBMUTL010.scrivi('    '||'/* '||LPAD(k,3)||' */  '||CHR(i+96)||'.'||aCol.column_name);
        else
         IBMUTL010.scrivi('    '||'/* '||LPAD(k,3)||' */ ,'||CHR(i+96)||'.'||aCol.column_name);
        end if;
        k:=k+1;
       end loop;
       IBMUTL010.scrivi('');
      end loop;
      IBMUTL010.scrivi(' from '||aTableList||' where ');
      IBMUTL010.scrivi('');

      for k in tabelle.FIRST .. tabelle.LAST - 1 loop
       aPkColArray:=getPkCols(tabelle(k));
       for i in aPkColArray.FIRST .. aPkColArray.LAST loop
        if k=tabelle.FIRST and i = aPkColArray.FIRST then
         IBMUTL010.scrivi('      '||CHR(k+96)||'.'||aPkColArray(i)||'='||CHR(k+1+96)||'.'||aPkColArray(i));
        else
         IBMUTL010.scrivi('  and '||CHR(k+96)||'.'||aPkColArray(i)||'='||CHR(k+1+96)||'.'||aPkColArray(i));
        end if;
       end loop;
      end loop;

      IBMUTL010.scrivi(');');
     end;

	 procedure extractFKStatements(tabName varchar2) is
       aStatementFK varchar2(2000);
       cid INTEGER;
       k INTEGER;
       aStatement varchar2(2000);
       aOwner varchar2(35);
	  begin
	   select username into aOwner from user_users;
        --
        -- Vincoli di integrita referenziale
        --
       for aFK in (
        select distinct child_table_name, constraint_name from vs_fk_chain_00 where
             parent_table_name=tabName
         and owner = aOwner
       ) loop
        dbms_output.put_line('alter table '||aFK.child_table_name|| ' add constraint '||aFK.constraint_name||' foreign key ');
        k:=0;
        aStatement:='(';
        for aCol in (  select * from vs_fk_chain_00 where
             parent_table_name=tabName
         and child_table_name=aFK.child_table_name
         and constraint_name=aFK.constraint_name
         and owner = aOwner
         order by parent_table_name, child_table_name, constraint_name, position
        ) loop
         if k=0 then
          aStatement:=aStatement||aCol.column_name;
         else
          aStatement:=aStatement||','||aCol.column_name;
         end if;
         k:=k+1;
        end loop;
        aStatement:=aStatement||') references '||tabName;
        dbms_output.put_line(aStatement);
        k:=0;
        aStatement:='(';
        for aCol in (  select * from vs_fk_chain_00 where
             parent_table_name=tabName
         and child_table_name=aFK.child_table_name
         and constraint_name=aFK.constraint_name
         and owner = aOwner
         order by parent_table_name, child_table_name, constraint_name, position
        ) loop
         if k=0 then
          aStatement:=aStatement||aCol.r_column_name;
         else
          aStatement:=aStatement||','||aCol.r_column_name;
         end if;
         k:=k+1;
        end loop;
        aStatement:=aStatement||');';
        dbms_output.put_line(aStatement);
        dbms_output.put_line('');
       end loop;
      end;

  procedure generaStoricoPer(destinazione varchar2) is
      aOwner varchar2(35);
      aList varchar2(500);
	 begin

	  select username into aOwner from user_users;

	  EXECUTE IMMEDIATE 'create table '||destinazione||'_S as select * from '||destinazione||' where pg_ver_rec = -1';
      EXECUTE IMMEDIATE 'alter table '||destinazione||'_S add pg_storico_ number(10)';
      EXECUTE IMMEDIATE 'alter table '||destinazione||'_S add ds_storico_ varchar2(300)';

      for aConCol in (
       select b.* from all_constraints a, all_cons_columns b where
            a.owner =b.owner
        and a.table_name = b.table_name
        and a.constraint_name = b.constraint_name
        and a.constraint_type = 'P'
        and b.owner = aowner and b.table_name = destinazione
       order by position
      ) loop
	   aList:=aList||aConCol.column_name||',';
      end loop;

	  aList:=aList||'pg_storico_';

	  EXECUTE IMMEDIATE 'alter table '||destinazione||'_S add constraint PX_'||destinazione||'_S primary key ('||aList||')';

     end;

  procedure creaInsertStoricoProcStatement(destinazione varchar2) is
      aOwner varchar2(35);
     begin
	  select username into aOwner from user_users;
      IBMUTL010.SCRIVI(' procedure sto_'||destinazione||' (aPgStorico number, aDsStorico varchar2, aDest '||destinazione||'%rowtype) is ');
      IBMUTL010.SCRIVI('  begin ');
      IBMUTL010.SCRIVI('   insert into '||destinazione||'_S (');
      IBMUTL010.SCRIVI('     pg_storico_');
      IBMUTL010.SCRIVI('    ,ds_storico_');
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
      ) loop
        IBMUTL010.SCRIVI('    ,'||aCol.column_name);
      end loop;
        IBMUTL010.SCRIVI('   ) values (');
        IBMUTL010.SCRIVI('     aPgStorico');
        IBMUTL010.SCRIVI('    ,aDsStorico');
      for aCol in (
       select * from all_tab_columns
        where
     	     table_name = destinazione
         and owner = aOwner
      ) loop
        IBMUTL010.SCRIVI('    ,aDest.'||aCol.column_name);
      end loop;
        IBMUTL010.SCRIVI('    );');
        IBMUTL010.SCRIVI(' end;');
     end;
end;
