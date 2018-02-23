--------------------------------------------------------
--  DDL for Package Body CLCONFIGURAZIONE_CNR
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CLCONFIGURAZIONE_CNR" AS
  procedure UpdRegStatoTrg(aNomeTab varchar2, aNomeTrg varchar2, aStato char, aUser varchar2 ) as
  lStrSql varchar2(100);
  lTipoTrg char(1);
  lOwner varchar2(30);
  begin
    select user into lOwner from dual;
    lTipoTrg := substr(aNomeTrg,2,1) ;
    if lTipoTrg = 'D' then
      if aStato ='D' then
        update LOG_REGISTRY
        set STATO_TRG_AD = aStato,
        DT_DISATTIVAZIONE_AD = sysdate,
        DUVA = sysdate,
        UTUV = aUser,
        PG_VER_REC = PG_VER_REC + 1
        where  nome_table_src = aNomeTab ;
      else
        update LOG_REGISTRY
        set STATO_TRG_AD = aStato,
        DT_ATTIVAZIONE_AD = sysdate,
        DUVA = sysdate,
        UTUV = aUser,
        PG_VER_REC = PG_VER_REC + 1
        where  nome_table_src = aNomeTab;
      end if;
    end if;

    if lTipoTrg = 'I' then
      if aStato ='D' then
        update LOG_REGISTRY
        set STATO_TRG_AI = aStato,
        DT_DISATTIVAZIONE_AI = sysdate,
        DUVA = sysdate,
        UTUV = aUser ,
        PG_VER_REC = PG_VER_REC + 1
        where  nome_table_src = aNomeTab;
      else
        update LOG_REGISTRY
        set STATO_TRG_AI = aStato,
        DT_ATTIVAZIONE_AI = sysdate,
        DUVA = sysdate,
        UTUV = aUser ,
        PG_VER_REC = PG_VER_REC + 1
        where  nome_table_src = aNomeTab;
      end if;
    end if;

    if lTipoTrg = 'U' then
      if aStato ='D' then
        update LOG_REGISTRY
        set STATO_TRG_AU = aStato,
        DT_DISATTIVAZIONE_AU = sysdate,
        DUVA = sysdate,
        UTUV = aUser ,
        PG_VER_REC = PG_VER_REC + 1
        where  nome_table_src = aNomeTab;
      else
        update LOG_REGISTRY
        set STATO_TRG_AU = aStato,
        DT_ATTIVAZIONE_AU = sysdate,
        DUVA = sysdate,
        UTUV = aUser ,
        PG_VER_REC = PG_VER_REC + 1
        where  nome_table_src = aNomeTab;
      end if;
    end if;
  end;

  procedure attivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 ) as
  lStrSql varchar2(100);
  begin
    lStrSql := 'ALTER TRIGGER ' || aNomeTrg || ' ENABLE';
    execute immediate lStrSql;
    CLCONFIGURAZIONE_CNR.UpdRegStatoTrg(aNomeTab, aNomeTrg, 'A', aUser );
  end;

  procedure disattivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 ) as
  lStrSql varchar2(100);
  begin
    lStrSql := 'ALTER TRIGGER ' || aNomeTrg || ' DISABLE';
    execute immediate lStrSql;
    CLCONFIGURAZIONE_CNR.UpdRegStatoTrg(aNomeTab, aNomeTrg, 'D', aUser );
  end;

  procedure INSERTRIGA(aTbName varchar2, aL_CONFIGURAZIONE_CNR L_CONFIGURAZIONE_CNR%rowtype) is
  lUser varchar2(20);
  lLogRegistry LOG_REGISTRY%rowtype;
  begin
    select user into lUser from dual;
    select * into lLogRegistry from LOG_REGISTRY
    where nome_table_src = aTbName;
    if lLogRegistry.NUM_MAX_RIGHE_TABLE_LOG >= aL_CONFIGURAZIONE_CNR.PG_STORICO_ then
      insert into L_CONFIGURAZIONE_CNR(
        PG_STORICO_,
        ESERCIZIO,
        CD_UNITA_FUNZIONALE,
        CD_CHIAVE_PRIMARIA,
        CD_CHIAVE_SECONDARIA,
        VAL01,
        VAL02,
        VAL03,
        VAL04,
        IM01,
        IM02,
        DT01,
        DT02,
        DACR,
        UTCR,
        DUVA,
        UTUV,
        PG_VER_REC,
        USER_ ,
        DT_TRANSACTION_,
        ACTION_
      ) VALUES (
        aL_CONFIGURAZIONE_CNR.PG_STORICO_,
        aL_CONFIGURAZIONE_CNR.ESERCIZIO,
        aL_CONFIGURAZIONE_CNR.CD_UNITA_FUNZIONALE,
        aL_CONFIGURAZIONE_CNR.CD_CHIAVE_PRIMARIA,
        aL_CONFIGURAZIONE_CNR.CD_CHIAVE_SECONDARIA,
        aL_CONFIGURAZIONE_CNR.VAL01,
        aL_CONFIGURAZIONE_CNR.VAL02,
        aL_CONFIGURAZIONE_CNR.VAL03,
        aL_CONFIGURAZIONE_CNR.VAL04,
        aL_CONFIGURAZIONE_CNR.IM01,
        aL_CONFIGURAZIONE_CNR.IM02,
        aL_CONFIGURAZIONE_CNR.DT01,
        aL_CONFIGURAZIONE_CNR.DT02,
        aL_CONFIGURAZIONE_CNR.DACR,
        aL_CONFIGURAZIONE_CNR.UTCR,
        aL_CONFIGURAZIONE_CNR.DUVA,
        aL_CONFIGURAZIONE_CNR.UTUV,
        aL_CONFIGURAZIONE_CNR.PG_VER_REC,
        aL_CONFIGURAZIONE_CNR.USER_ ,
        aL_CONFIGURAZIONE_CNR.DT_TRANSACTION_,
        aL_CONFIGURAZIONE_CNR.ACTION_
      );
    else
      null;
    end if;
  end;

  procedure updateRegistry(aTbName varchar2, aTipoTrg char, aUser varchar2 ) as
  begin
    if aTipoTrg = 'D' then
      update LOG_REGISTRY
      set STATO_TRG_AD = 'A',
        DT_ULTIMO_START_AD = sysdate,
        NUM_RIGHE_IN_TABLE_LOG = nvl(NUM_RIGHE_IN_TABLE_LOG,0) + 1,
        DUVA = sysdate,
        UTUV = aUser,
        PG_VER_REC = PG_VER_REC + 1
      where  nome_table_src = aTbName ;
    end if;

    if aTipoTrg = 'I' then
      update LOG_REGISTRY
      set STATO_TRG_AI = 'A',
        DT_ULTIMO_START_AI = sysdate,
        NUM_RIGHE_IN_TABLE_LOG = nvl(NUM_RIGHE_IN_TABLE_LOG,0) + 1,
        DUVA = sysdate,
        UTUV = aUser ,
        PG_VER_REC = PG_VER_REC + 1
      where  nome_table_src = aTbName;
    end if;

    if aTipoTrg = 'U' then
      update LOG_REGISTRY
      set STATO_TRG_AU = 'A',
        DT_ULTIMO_START_AU = sysdate,
        NUM_RIGHE_IN_TABLE_LOG = nvl(NUM_RIGHE_IN_TABLE_LOG,0) + 1,
        DUVA = sysdate,
        UTUV = aUser ,
        PG_VER_REC = PG_VER_REC + 1
      where  nome_table_src = aTbName;
    end if;
  end;
end;
