CREATE OR REPLACE PACKAGE CLBANCA AS
  --
  -- CLBANCA- Package di gestione inserimenti nella tabella di Log :L_BANCA
  -- Date: 19/03/2004
  -- Version: 1.0
  --
  -- Dependency:
  --
  -- History:
  --
  -- Date: 19/03/2004
  -- Version: 1.0
  -- Creazione
  --
  -- Constants:
  --
  -- Functions e Procedures:
  --
  -- Pre-post-name: Inserimento di una riga nella tabella di LOG : 'L_BANCA'
  --
  -- pre: esistenza della tabella di LOG : 'L_BANCA'
  -- post: Viene inserita una riga nella tabella di LOG,
  --
  -- parametri:
  --   aL_BANCA-> Riga da inserire
  procedure INSERTRIGA(aTbName varchar2, aL_BANCA L_BANCA%rowtype);
  --
  -- Pre-post-name: aggiornamento della tabella di registro dei LOG : LOG_REGISTRY
  --
  -- pre: esistenza della tabella di LOG_REGISTRY
  -- post: aggiornamento della tabella di LOG_REGISTRY per lo stato della
  --       tabella L_BANCA
  --
  -- parametri:
  --   aTbName -> Nome della tabella sorgente
  --   aTipoTrg -> Tipologia del trigger che ha scatenato la modifica
  --            'I' -> Trigger di Inserimento
  --            'D' -> Trigger di Cancellazione
  --            'U' -> Trigger di Aggiornamento
  --   aUser -> Utente che ha scatenato la modifica
  procedure updateRegistry(aTbName varchar2, aTipoTrg char, aUser varchar2 );
  --
  -- Pre-post-name: attivazione del trigger sulla tabella :BANCA
  --
  -- pre: Esistenza del trigger in esame, sulla tabella BANCA, e stato del trigger in esame = Disable
  -- post: attivazione del trigger in esame sulla tabella BANCA
  --
  -- parametri:
  --   aNomeTrg -> Nome del trigger che vogliamo attivare
  procedure attivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 ) ;
  --
  -- Pre-post-name: disattivazione del trigger sulla tabella :BANCA
  --
  -- pre: Esistenza del trigger in esame, sulla tabella BANCA, e stato del trigger in esame = Enable
  -- post: disattivazione del trigger in esame sulla tabella BANCA
  --
  -- parametri:
  --   aNomeTrg -> Nome del trigger che vogliamo disattivare
  procedure disattivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 );
end;
/


CREATE OR REPLACE PACKAGE BODY CLBANCA AS
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
    CLBANCA.UpdRegStatoTrg(aNomeTab, aNomeTrg, 'A', aUser );
  end;

  procedure disattivaTrg(aNomeTab varchar2, aNomeTrg varchar2, aUser varchar2 ) as
  lStrSql varchar2(100);
  begin
    lStrSql := 'ALTER TRIGGER ' || aNomeTrg || ' DISABLE';
    execute immediate lStrSql;
    CLBANCA.UpdRegStatoTrg(aNomeTab, aNomeTrg, 'D', aUser );
  end;

  procedure INSERTRIGA(aTbName varchar2, aL_BANCA L_BANCA%rowtype) is
  lUser varchar2(20);
  lLogRegistry LOG_REGISTRY%rowtype;
  begin
    select user into lUser from dual;
    select * into lLogRegistry from LOG_REGISTRY
    where nome_table_src = aTbName;
    if lLogRegistry.NUM_MAX_RIGHE_TABLE_LOG >= aL_BANCA.PG_STORICO_ then
      insert into L_BANCA(
        PG_STORICO_,
        CD_TERZO,
        PG_BANCA,
        CAB,
        ABI,
        DACR,
        INTESTAZIONE,
        QUIETANZA,
        NUMERO_CONTO,
        TI_PAGAMENTO,
        CODICE_IBAN,
        CODICE_SWIFT,
        UTCR,
        DUVA,
        UTUV,
        PG_VER_REC,
        FL_CANCELLATO,
        CD_TERZO_DELEGATO,
        PG_BANCA_DELEGATO,
        ORIGINE,
        FL_CC_CDS,
        CIN,
        USER_ ,
        DT_TRANSACTION_,
        ACTION_
      ) VALUES (
        aL_BANCA.PG_STORICO_,
        aL_BANCA.CD_TERZO,
        aL_BANCA.PG_BANCA,
        aL_BANCA.CAB,
        aL_BANCA.ABI,
        aL_BANCA.DACR,
        aL_BANCA.INTESTAZIONE,
        aL_BANCA.QUIETANZA,
        aL_BANCA.NUMERO_CONTO,
        aL_BANCA.TI_PAGAMENTO,
        aL_BANCA.CODICE_IBAN,
        aL_BANCA.CODICE_SWIFT,
        aL_BANCA.UTCR,
        aL_BANCA.DUVA,
        aL_BANCA.UTUV,
        aL_BANCA.PG_VER_REC,
        aL_BANCA.FL_CANCELLATO,
        aL_BANCA.CD_TERZO_DELEGATO,
        aL_BANCA.PG_BANCA_DELEGATO,
        aL_BANCA.ORIGINE,
        aL_BANCA.FL_CC_CDS,
        aL_BANCA.CIN,
        aL_BANCA.USER_ ,
        aL_BANCA.DT_TRANSACTION_,
        aL_BANCA.ACTION_
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
/


