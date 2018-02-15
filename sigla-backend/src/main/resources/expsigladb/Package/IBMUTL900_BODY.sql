--------------------------------------------------------
--  DDL for Package Body IBMUTL900
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "IBMUTL900" as

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
       aPkColArray:=IBMUTL100.getPkCols(tabelle(k));
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
end;
