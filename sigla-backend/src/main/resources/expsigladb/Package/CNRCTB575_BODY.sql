--------------------------------------------------------
--  DDL for Package Body CNRCTB575
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB575" AS
 procedure checkLiquidCentroAperta(aEs number) is
 begin
  if not ISLIQUIDCENTROAPERTA(aEs) then
   	 IBMERR001.RAISE_ERR_GENERICO('La liquidazione CORI al centro per esercizio '||aEs||' risulta chiusa');
  end if;
 end;

 procedure checkLiquidLocaleAperta(aEs number) is
 begin
  if not ISLIQUIDLOCALEAPERTA(aEs) then
   	 IBMERR001.RAISE_ERR_GENERICO('La liquidazione CORI al centro per esercizio '||aEs||' risulta chiusa');
  end if;
 end;

 function isLiquidLocaleAperta(aEs number) return boolean is
 begin
  if CNRCTB015.GETVAL01PERCHIAVE(aEs,LC,LC_LOCALE_ATTIVA)='Y' then
   return true;
  else
   return false;
  end if;
 end;

 function isLiquidCentroAperta(aEs number) return boolean is
 begin
  if CNRCTB015.GETVAL01PERCHIAVE(aEs,LC,LC_CENTRO_ATTIVA)='Y' then
   return true;
  else
   return false;
  end if;
 end;

 function getDesc(aLGC liquid_gruppo_cori%rowtype) return varchar2 is
 begin
  return ' liq.n.:'||aLGC.pg_liquidazione||' es.'||aLGC.esercizio||' cds:'||aLGC.cd_cds||' uo:'||aLGC.cd_unita_organizzativa||' gr.:'||aLGC.cd_gruppo_cr||' reg.:'||aLGC.cd_regione||' com.:'||aLGC.pg_comune;
 end;

 function getNextNumLiquid(aCdCds varchar2, aEs number, aCdUo varchar2) return number is
  aNum number(10);
 begin
  begin
   select pg_liquidazione into aNum from liquid_cori where
        esercizio = aEs
    and cd_cds = aCdCds
    and cd_unita_organizzativa = aCdUo
    and pg_liquidazione = (select max(pg_liquidazione) from liquid_cori where
                               esercizio = aEs
                           and cd_cds = aCdCds
                           and cd_unita_organizzativa = aCdUo
						 )
   for update nowait;
  exception when NO_DATA_FOUND then
   return 0;
  end;
  return aNum+1;
 end;

 function isLiquidaCoriInviato(aEs number) return char is
 begin
  if CNRCTB015.GETVAL01PERCHIAVE(aEs,GESTIONE_CORI_SPEC,STATO_LIQUIDA_CORI) = STATO_LIQUID_CORI_INV then
   return 'Y';
  else
   return 'N';
  end if;
 end;

 procedure ins_LIQUID_CORI (aDest LIQUID_CORI%rowtype) is
  begin
   insert into LIQUID_CORI (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_LIQUIDAZIONE
    ,DA_ESERCIZIO_PRECEDENTE
    ,DT_DA
    ,DT_A
    ,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_LIQUIDAZIONE
    ,aDest.DA_ESERCIZIO_PRECEDENTE
    ,aDest.DT_DA
    ,aDest.DT_A
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_LIQUID_GRUPPO_CORI (aDest LIQUID_GRUPPO_CORI%rowtype) is
  begin
   insert into LIQUID_GRUPPO_CORI (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_LIQUIDAZIONE
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,PG_LIQUIDAZIONE_ORIGINE
    ,CD_GRUPPO_CR
    ,CD_REGIONE
    ,PG_COMUNE
    ,IM_LIQUIDATO
	,FL_ACCENTRATO
	,ESERCIZIO_DOC
    ,CD_CDS_DOC
    ,PG_DOC
    ,CD_CDS_OBB_ACCENTR
    ,ESERCIZIO_OBB_ACCENTR
    ,ESERCIZIO_ORI_OBB_ACCENTR
    ,PG_OBB_ACCENTR
	,STATO
	,PG_GRUPPO_CENTRO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
	,CD_CDS_ACC_COMPENS
	,ESERCIZIO_ACC_COMPENS
	,ESERCIZIO_ORI_ACC_COMPENS
	,PG_ACC_COMPENS
	,CD_CDS_REV
	,ESERCIZIO_REV
	,PG_REV
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_LIQUIDAZIONE
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.PG_LIQUIDAZIONE_ORIGINE
    ,aDest.CD_GRUPPO_CR
    ,aDest.CD_REGIONE
    ,aDest.PG_COMUNE
    ,aDest.IM_LIQUIDATO
	,aDest.FL_ACCENTRATO
	,aDest.ESERCIZIO_DOC
    ,aDest.CD_CDS_DOC
    ,aDest.PG_DOC
    ,aDest.CD_CDS_OBB_ACCENTR
    ,aDest.ESERCIZIO_OBB_ACCENTR
    ,aDest.ESERCIZIO_ORI_OBB_ACCENTR
    ,aDest.PG_OBB_ACCENTR
	,aDest.STATO
	,aDest.PG_GRUPPO_CENTRO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
	,aDest.CD_CDS_ACC_COMPENS
	,aDest.ESERCIZIO_ACC_COMPENS
	,aDest.ESERCIZIO_ORI_ACC_COMPENS
	,aDest.PG_ACC_COMPENS
	,aDest.CD_CDS_REV
	,aDest.ESERCIZIO_REV
	,aDest.PG_REV
    );
 end;
 procedure ins_LIQUID_GRUPPO_CORI_DET (aDest LIQUID_GRUPPO_CORI_DET%rowtype) is
  Begin
   insert into LIQUID_GRUPPO_CORI_DET (
     UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_LIQUIDAZIONE
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,PG_LIQUIDAZIONE_ORIGINE
    ,CD_GRUPPO_CR
    ,CD_REGIONE
    ,PG_COMUNE
    ,CD_CONTRIBUTO_RITENUTA
    ,PG_COMPENSO
    ,TI_ENTE_PERCIPIENTE
    ,DACR
	,ESERCIZIO_CONTRIBUTO_RITENUTA
   ) values (
     aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_LIQUIDAZIONE
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.PG_LIQUIDAZIONE_ORIGINE
    ,aDest.CD_GRUPPO_CR
    ,aDest.CD_REGIONE
    ,aDest.PG_COMUNE
    ,aDest.CD_CONTRIBUTO_RITENUTA
    ,aDest.PG_COMPENSO
    ,aDest.TI_ENTE_PERCIPIENTE
    ,aDest.DACR
	,aDest.ESERCIZIO_CONTRIBUTO_RITENUTA
    );
 End;

 procedure ins_LIQUID_GRUPPO_CENTRO (aDest LIQUID_GRUPPO_CENTRO%rowtype) is
  begin
   insert into LIQUID_GRUPPO_CENTRO (
     ESERCIZIO
    ,CD_GRUPPO_CR
    ,CD_REGIONE
    ,PG_COMUNE
    ,PG_GRUPPO_CENTRO
    ,STATO
    ,CD_CDS_OBB_ACCENTR
    ,ESERCIZIO_OBB_ACCENTR
    ,ESERCIZIO_ORI_OBB_ACCENTR
    ,PG_OBB_ACCENTR
    ,CD_CDS_LC
    ,CD_UO_LC
    ,PG_LC
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
	,DA_ESERCIZIO_PRECEDENTE
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_GRUPPO_CR
    ,aDest.CD_REGIONE
    ,aDest.PG_COMUNE
    ,aDest.PG_GRUPPO_CENTRO
    ,aDest.STATO
    ,aDest.CD_CDS_OBB_ACCENTR
    ,aDest.ESERCIZIO_OBB_ACCENTR
    ,aDest.ESERCIZIO_ORI_OBB_ACCENTR
    ,aDest.PG_OBB_ACCENTR
    ,aDest.CD_CDS_LC
    ,aDest.CD_UO_LC
    ,aDest.PG_LC
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
	,aDest.DA_ESERCIZIO_PRECEDENTE
    );
 end;

 procedure ins_LIQUID_GRUPPO_CENTRO_COMP (aDest LIQUID_GRUPPO_CENTRO_COMP%rowtype) is
  begin
   insert into LIQUID_GRUPPO_CENTRO_COMP (
     ESERCIZIO
    ,CD_GRUPPO_CR
    ,CD_REGIONE
    ,PG_COMUNE
    ,PG_GRUPPO_CENTRO
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CDS_ACC_ACCENTR
    ,ESERCIZIO_ACC_ACCENTR
    ,ESERCIZIO_ORI_ACC_ACCENTR
    ,PG_ACC_ACCENTR
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_GRUPPO_CR
    ,aDest.CD_REGIONE
    ,aDest.PG_COMUNE
    ,aDest.PG_GRUPPO_CENTRO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CDS_ACC_ACCENTR
    ,aDest.ESERCIZIO_ACC_ACCENTR
    ,aDest.ESERCIZIO_ORI_ACC_ACCENTR
    ,aDest.PG_ACC_ACCENTR
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
END;
