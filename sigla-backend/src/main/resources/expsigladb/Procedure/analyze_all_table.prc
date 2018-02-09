CREATE OR REPLACE procedure Analyze_all_table (aSchema varchar2, aUtente varchar2) as
lNomeTabella varchar2(30);
lStrSql varchar2(300);
lSchema varchar2(30);
gPgLog number;
begin
 	 gPgLog := ibmutl200.LOGSTART('Analyze tabelle ' || UPPER(aSchema) ,aUtente , null, null);
	 for lTabella in (select * from all_tables where upper(owner) = upper(aSchema))
	 loop
	 	 lNomeTabella := lTabella.table_name;
	 	 begin
			 lStrSql := 'ANALYZE TABLE ' || aSchema ||'.'||lNomeTabella || ' ESTIMATE STATISTICS SAMPLE 30 PERCENT ';
			 execute immediate lStrSql;
		 exception 
		 when others then
		  	   ibmutl200.logInf(gPgLog ,'Analyze Fallita' , aSchema||'.'||lNomeTabella,'');
		 end;
	 end loop;  
end;
/


