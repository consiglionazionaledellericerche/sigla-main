CREATE OR REPLACE procedure TORNA_ALLE (Istante in TIMESTAMP) is
xSCN number;
Begin

    DBMS_FLASHBACK.DISABLE;
    Select SYSTEMCN
        into xSCN
        from TAB_SCN_TIME
        where TSTAMP = (Select Max(TSTAMP)
        From TAB_SCN_TIME
        Where TSTAMP <= Istante);

    DBMS_FLASHBACK.ENABLE_AT_SYSTEM_CHANGE_NUMBER(xSCN);

    Exception
    When no_data_found then Null;
End;
/


