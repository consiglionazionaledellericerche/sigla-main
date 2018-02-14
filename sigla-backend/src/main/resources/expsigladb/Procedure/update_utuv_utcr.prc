CREATE OR REPLACE PROCEDURE UPDATE_UTUV_UTCR(NOME_SCHEMA IN  VARCHAR2) IS
   nome_tabella VARCHAR2(30);
   stringa_sql  VARCHAR2(1000);
   CURSOR CUR_TAB IS
         SELECT UV.TABLE_NAME
         FROM
                  (SELECT TAB.TABLE_NAME
                   FROM ALL_TAB_COLUMNS COL,
                        ALL_TABLES TAB
                   WHERE TAB.OWNER= UPPER(NOME_SCHEMA)
                     AND TAB.OWNER=COL.OWNER
                     AND TAB.TABLE_NAME= COL.TABLE_NAME
                     AND UPPER(COL.COLUMN_NAME)='UTUV') UV,
                  (SELECT TAB.TABLE_NAME
                   FROM ALL_TAB_COLUMNS COL,
                        ALL_TABLES TAB
                   WHERE TAB.OWNER= UPPER(NOME_SCHEMA)
                     AND TAB.OWNER=COL.OWNER
                     AND TAB.TABLE_NAME= COL.TABLE_NAME
                     AND UPPER(COL.COLUMN_NAME)='UTCR') CR
         WHERE UV.TABLE_NAME =CR.TABLE_NAME;
BEGIN
     FOR rec_table IN CUR_TAB LOOP
     stringa_sql := 'UPDATE ' || rec_table.TABLE_NAME || ' SET UTCR = ''$$$$$MIGRAZIONE$$$$$'' WHERE UTCR =''_____MIGRAZIONE_____''';
     EXECUTE IMMEDIATE stringa_sql;
     stringa_sql := 'UPDATE ' || rec_table.TABLE_NAME || ' SET UTUV = ''$$$$$MIGRAZIONE$$$$$'' WHERE UTUV =''_____MIGRAZIONE_____''';
     EXECUTE IMMEDIATE stringa_sql;
     END LOOP;
EXCEPTION
WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('errore' || substr(sqlerrm(sqlcode),1,200));
    ROLLBACK;
END;
/


