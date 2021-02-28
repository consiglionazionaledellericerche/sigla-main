--------------------------------------------------------
--  DDL for Package Body CNRCTB018
--------------------------------------------------------

CREATE OR REPLACE PACKAGE BODY "CNRCTB018" is

  procedure aggiornaNumeratori(aEsercizio number, aCdCds varchar2, aUser varchar2) is
   aNewNum NUMERAZIONE_DOC_CONT%rowtype;
   aTSNow date;
   aTipoUnita varchar2(50);
  begin
     aTSNow:=sysdate;

	begin
	 select cd_tipo_unita into aTipoUnita from unita_organizzativa where
	  cd_unita_organizzativa = aCdCds;
	exception when NO_DATA_FOUND then
	  IBMERR001.RAISE_ERR_GENERICO('CDS o ENTE non trovati!');
	end;

	-- Caso CDS ENTE
	if aTipoUnita = CNRCTB020.TIPO_ENTE then
     begin
	  aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_IMP;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PRI;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PRI-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PRI;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
     exception when dup_val_on_index then
	  NULL;
 	 end;

     begin
	  aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_IMP_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_RES;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_RES-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_RES;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
     exception when dup_val_on_index then
	  NULL;
 	 end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PRI;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PRI-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PRI;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	  NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_RES;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_RES-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_RES;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	  NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_PLUR;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PLUR;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PLUR-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PLUR;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	  NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_MAN;
      aNewNum.PRIMO:=FIRST_DOC_CNR;
      aNewNum.CORRENTE:=FIRST_DOC_CNR-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_REV;
      aNewNum.PRIMO:=FIRST_DOC_CNR;
      aNewNum.CORRENTE:=FIRST_DOC_CNR-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_PGIRO_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_PGIRO_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACR_MOD;
      aNewNum.PRIMO:=FIRST_DOC_CNR;
      aNewNum.CORRENTE:=FIRST_DOC_CNR-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;
	-- Caso CDS NON ENTE
   else

	begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB;
      aNewNum.PRIMO:=FIRST_DOC_CDS;
      aNewNum.CORRENTE:=FIRST_DOC_CDS-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_PGIRO;
      aNewNum.PRIMO:=FIRST_DOC_CDS_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CDS_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_PGIRO;
      aNewNum.PRIMO:=FIRST_DOC_CDS_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CDS_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_SIST;
      aNewNum.PRIMO:=FIRST_DOC_CDS;
      aNewNum.CORRENTE:=FIRST_DOC_CDS-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_MAN;
      aNewNum.PRIMO:=FIRST_DOC_CDS;
      aNewNum.CORRENTE:=FIRST_DOC_CDS-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_REV;
      aNewNum.PRIMO:=FIRST_DOC_CDS;
      aNewNum.CORRENTE:=FIRST_DOC_CDS-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_REV_PROVV;
      aNewNum.PRIMO:=FIRST_DOC_CDS_PROVV;
      aNewNum.CORRENTE:=FIRST_DOC_CDS_PROVV-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS_PROVV;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_RES_PRO;
      aNewNum.PRIMO:=FIRST_DOC_CDS_RES;
      aNewNum.CORRENTE:=FIRST_DOC_CDS_RES-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS_RES;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;
			  dbms_output.put_line('1005');

    declare
      pLastResimpro number;
      pIncremento NUMBER := 10000000;
      pFirst NUMBER := 9000000001;
      conta NUMBER := 1;
    begin
      Select max(primo) into pLastResimpro
      from NUMERAZIONE_DOC_CONT
      where ESERCIZIO = aEsercizio-1
      and   CD_TIPO_DOCUMENTO_CONT = TI_DOC_OBB_RES_IMPRO;

      If pLastResimpro IS null THEN
        pLastResimpro := pFirst;
      Else
        pLastResimpro := pLastResimpro + pIncremento;
      End If;

      While conta>0 LOOP
        SELECT count(0) INTO conta
        FROM NUMERAZIONE_DOC_CONT
        where CD_CDS = aCdCds
        AND   CD_TIPO_DOCUMENTO_CONT = TI_DOC_OBB_RES_IMPRO
        AND   primo BETWEEN pLastResimpro AND pLastResimpro+pIncremento-1;

        If CONTA > 0 THEN
          pLastResimpro := pLastResimpro + pIncremento;
        End If;
      End Loop;

      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_RES_IMPRO;
      aNewNum.PRIMO:=pLastResimpro;
      aNewNum.CORRENTE:=pLastResimpro-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS_RESIM;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_PGIRO_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_PGIRO_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_MOD;
      aNewNum.PRIMO:=FIRST_DOC_CNR;
      aNewNum.CORRENTE:=FIRST_DOC_CNR-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

   end if;
  end;

  function getNextNumDocCont(aTipo varchar2, aEs number, aCdCDS varchar2, aUser varchar2) return number is
   aNum numerazione_doc_cont%rowtype;
   aTSNow date;
   AtIPOnEW VARCHAR2(10);
  begin
   aTSNow:=sysdate;
   Begin

   /* 05.01.2006 STANI */

   aTipoNEW := aTipo;

   If aTipo = CNRCTB018.TI_DOC_OBB_PGIRO_RES Then
     aTipoNEW := CNRCTB018.TI_DOC_OBB_PGIRO;
   Elsif aTipo = CNRCTB018.TI_DOC_ACC_PGIRO_RES Then
     aTipoNEW := CNRCTB018.TI_DOC_ACC_PGIRO;
   End If;

    select * into aNum from numerazione_doc_cont where
            esercizio = aEs
  	  and cd_cds = aCdCDS
  	  and cd_tipo_documento_cont = aTipoNew
    for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Configurazione numerazione doc. cont. di tipo '||aTipo||' non trovata per esercizio: '||aEs||' su STO:'||aCdCDS);
   end;
   if aNum.corrente + 1 > aNum.ultimo then
    IBMERR001.RAISE_ERR_GENERICO('Numerazione esaurita per doc. cont. di tipo '||aTipo||' in esercizio: '||aEs||' su STO:'||aCdCDS);
   end if;
   update numerazione_doc_cont set
    corrente=corrente+1,
    utuv=aUser,
    duva=aTSNow,
    pg_ver_rec=pg_ver_rec+1
   where
        esercizio=aEs
    and cd_cds = aCdCDS
    and cd_tipo_documento_cont = aTipoNew;
   return aNum.corrente + 1;
  end;

  procedure ins_NUMERAZIONE_DOC_CONT (aDest NUMERAZIONE_DOC_CONT%rowtype) is
  begin
   insert into NUMERAZIONE_DOC_CONT (
     ESERCIZIO
    ,CD_CDS
    ,CD_TIPO_DOCUMENTO_CONT
    ,PRIMO
    ,CORRENTE
    ,ULTIMO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDS
    ,aDest.CD_TIPO_DOCUMENTO_CONT
    ,aDest.PRIMO
    ,aDest.CORRENTE
    ,aDest.ULTIMO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;
end;
