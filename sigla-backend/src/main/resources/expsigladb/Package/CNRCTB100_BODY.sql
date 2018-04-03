--------------------------------------------------------
--  DDL for Package Body CNRCTB100
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB100" IS
 FUNCTION getBeneServScontoAbbuono RETURN VARCHAR2 IS
 BEGIN
  RETURN Cnrctb015.GETVAL01PERCHIAVE(BENE_SERVIZIO_SPECIALE,BENE_SERVIZIO_SA);
 END;

 FUNCTION isBeneServScontoAbbuono(aCdBeneServ VARCHAR2) RETURN BOOLEAN IS
 BEGIN
  IF getBeneServScontoAbbuono = aCdBeneServ THEN
   RETURN TRUE;
  ELSE
   RETURN FALSE;
  END IF;
 END;

 PROCEDURE updateDocAmmInt(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2,
  isNotDuvaUtuv boolean,
  aUser VARCHAR2,
  aTSNow DATE
 ) IS
  aStr VARCHAR2(2000);
  aStatement varchar2(2000);
 BEGIN
  if isNotDuvaUtuv is null then
   IBMERR001.RAISE_ERR_GENERICO('Specificare il tipo di agg. dei parametri sistemistici del record');
  end if;
  if aSetClause is null then
  	 aStatement := '';
  else
  	 aStatement := aSetClause||', ';
  end if;
  aStr:='update '||Cnrctb100.GETTABELLA(aCdTipoDocumento);
  if isNotDuvaUtuv then
   aStr:=aStr||' set '||aStatement||' pg_ver_rec=pg_ver_rec+1 ';
  else
   aStr:=aStr||' set '||aStatement||' duva='||Ibmutl001.asDynTimestamp(aTSNow)||', utuv='''||aUser||''', pg_ver_rec=pg_ver_rec+1 ';
  end if;
  aStr:=aStr||Cnrctb100.GETTSTAWHERECONDFORKEY(aCdTipoDocumento,aCdCds,aEs,aCdUo,aPgDocumento);
  IF aWhereClause IS NOT NULL THEN
   aStr:=aStr||' and ('||aWhereClause||')';
  END IF;
  EXECUTE IMMEDIATE aStr;
 END;

 PROCEDURE updateDocAmm(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2,
  aUser VARCHAR2,
  aTSNow DATE
 ) IS
 begin
  updateDocAmmInt(
   aCdTipoDocumento,
   aCdCds,
   aEs,
   aCdUo,
   aPgDocumento,
   aSetClause,
   aWhereClause,
   false, -- update verrec duva utuv
   aUser,
   aTSNow);
 end;

 PROCEDURE updateDocAmm_noDuvaUtuv(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2
 ) IS
 begin
  updateDocAmmInt(
   aCdTipoDocumento,
   aCdCds,
   aEs,
   aCdUo,
   aPgDocumento,
   aSetClause,
   aWhereClause,
   true, -- update solo verrec
   null,
   null);
 end;

 PROCEDURE updateDocAmmRiga(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aPgRiga NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2,
  aUser VARCHAR2,
  aTSNow DATE
 ) IS
  aStr VARCHAR2(2000);
  aTab VARCHAR2(40);
  aStatement varchar2(2000);
 BEGIN
  aTab:=Cnrctb100.GETTABELLADETT(aCdTipoDocumento);
  IF aTab IS NULL THEN
   RETURN;
  END IF;

  if aSetClause is null then
  	 aStatement := '';
  else
  	 aStatement := aSetClause||', ';
  end if;
  aStr:='update '||aTab;
  aStr:=aStr||' set '||aStatement||' duva='||Ibmutl001.asDynTimestamp(aTSNow)||', utuv='''||aUser||''', pg_ver_rec=pg_ver_rec+1 ';
  aStr:=aStr||Cnrctb100.GETRIGAWHERECONDFORKEY(aCdTipoDocumento,aCdCds,aEs,aCdUo,aPgDocumento,aPgRiga);
  IF aWhereClause IS NOT NULL THEN
   aStr:=aStr||' and ('||aWhereClause||')';
  END IF;
  EXECUTE IMMEDIATE aStr;
 END;

 Procedure updateDocAmmRiga_noDuvaUtuv(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aPgRiga NUMBER,
  aSetClause VARCHAR2,
  aWhereClause VARCHAR2
 ) IS
  aStr VARCHAR2(2000);
  aTab VARCHAR2(40);
  aStatement varchar2(2000);

 Begin
  aTab:=Cnrctb100.GETTABELLADETT(aCdTipoDocumento);
  IF aTab IS NULL THEN
   RETURN;
  END IF;
  if aSetClause is null then
  	 aStatement := '';
  else
  	 aStatement := aSetClause||', ';
  end if;
  aStr:='update '||aTab;
  aStr:=aStr||' set '||aStatement||' pg_ver_rec=pg_ver_rec+1 ';
  aStr:=aStr||Cnrctb100.GETRIGAWHERECONDFORKEY(aCdTipoDocumento,aCdCds,aEs,aCdUo,aPgDocumento,aPgRiga);
  IF aWhereClause IS NOT NULL THEN
   aStr:=aStr||' and ('||aWhereClause||')';
  END IF;
  EXECUTE IMMEDIATE aStr;
 END;

 PROCEDURE lockDocAmm(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER
 ) IS
 BEGIN
  EXECUTE IMMEDIATE 'select * '||GETTSTAFROMCONDFORKEY(aCdTipoDocumento,aCdCds,aEs,aCdUo,aPgDocumento)||' for update nowait';
 END;

 PROCEDURE lockDocAmmRiga(
  aCdTipoDocumento VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDocumento NUMBER,
  aPgRiga NUMBER
 ) IS
  aTab VARCHAR2(40);
 BEGIN
  aTab:=Cnrctb100.GETTABELLADETT(aCdTipoDocumento);
  IF aTab IS NULL THEN
   RETURN;
  END IF;
  EXECUTE IMMEDIATE 'select * from '||aTab||GETRIGAWHERECONDFORKEY(aCdTipoDocumento,aCdCds,aEs,aCdUo,aPgDocumento,aPgRiga)||' for update nowait';
 END;

 FUNCTION getTstaWhereCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER
 ) RETURN VARCHAR2 IS
  aStr VARCHAR2(2000);
  aTab VARCHAR2(40);
 BEGIN
  IF
       aCdTipoDoc IS NULL
    OR aCdCds IS NULL
    OR aEs IS NULL
    OR aCdUo IS NULL
    OR aPgDoc IS NULL
  THEN
   Ibmerr001.RAISE_ERR_GENERICO('Chiave documento non completamente valorizzata');
  END IF;
  aTab:=GETTABELLA(aCdTipoDoc);
  aStr:=' where '||aTab||'.cd_cds = '''||aCdCds||''' and '||aTab||'.esercizio = '||aEs||' and '||aTab||'.cd_unita_organizzativa = '''||aCdUo||''' ';
  IF isInTabellaGenerico(aCdTipoDoc) = 'Y' THEN
   aStr:=aStr||' and '||aTab||'.cd_tipo_documento_amm = '''||aCdTipoDoc||''' ';
  END IF;
  aStr:=aStr||' and '||aTab||'.'||getNomePg(aCdTipoDoc)||'='||aPgDoc;
  RETURN aStr;
 END;

 FUNCTION getTstaFromCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER
 ) RETURN VARCHAR2 IS
 BEGIN
  RETURN ' from '||GETTABELLA(aCdTipoDoc)||getTstaWhereCondForKey (
                                                 aCdTipoDoc,
                                                 aCdCds,
                                                 aEs,
                                                 aCdUo,
                                                 aPgDoc
                                            );
 END;

 FUNCTION getRigaWhereCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER,
  aPgDocDett NUMBER
 ) RETURN VARCHAR2 IS
  aStr VARCHAR2(2000);
  aTab VARCHAR2(40);
 BEGIN
  aTab:=GETTABELLADETT(aCdTipoDoc);

  IF
       aCdTipoDoc IS NULL
    OR aCdCds IS NULL
    OR aEs IS NULL
    OR aCdUo IS NULL
    OR aPgDoc IS NULL
  THEN
   Ibmerr001.RAISE_ERR_GENERICO('Chiave documento non completamente valorizzata');
  END IF;
  IF aTab IS NULL THEN
   RETURN NULL;
  END IF;
  aStr:=' where '||aTab||'.cd_cds = '''||aCdCds||''' and '||aTab||'.esercizio = '||aEs||' and '||aTab||'.cd_unita_organizzativa = '''||aCdUo||''' ';
  IF isInTabellaGenerico(aCdTipoDoc) = 'Y' THEN
   aStr:=aStr||' and '||aTab||'.cd_tipo_documento_amm = '''||aCdTipoDoc||''' ';
  END IF;
  aStr:=aStr||' and '||aTab||'.'||getNomePg(aCdTipoDoc)||'='||aPgDoc;
  IF aPgDocDett IS NOT NULL THEN
   aStr:=aStr||' and '||aTab||'.'||getNomePgDett(aCdTipoDoc)||'='||aPgDocDett;
  END IF;
  RETURN aStr;
 END;

 FUNCTION getRigaFromCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER,
  aPgDocDett NUMBER
 ) RETURN VARCHAR2 IS
  aTab VARCHAR2(40);
 BEGIN
  aTab:=GETTABELLADETT(aCdTipoDoc);
  IF aTab IS NULL THEN
   RETURN NULL;
  END IF;
  RETURN ' from '||aTab||getRigaWhereCondForKey (
                                                 aCdTipoDoc,
                                                 aCdCds,
                                                 aEs,
                                                 aCdUo,
                                                 aPgDoc,
                                                 aPgDocDett);
 END;

 FUNCTION getTstaRigaCondForJoin (
  aCdTipoDoc VARCHAR2
 ) RETURN VARCHAR2 IS
  aStr VARCHAR2(2000);
  aTestata VARCHAR2(40);
  aRiga VARCHAR2(40);
 BEGIN
  IF
       aCdTipoDoc IS NULL
  THEN
   Ibmerr001.RAISE_ERR_GENERICO('Tipo documento non specificato');
  END IF;
   aTestata:=GETTABELLA(aCdTipoDoc);
   aRiga:=GETTABELLADETT(aCdTipoDoc);
   IF aRiga IS NULL THEN
    RETURN NULL;
   END IF;
   aStr:=
                    aRiga||'.cd_cds = '||aTestata||'.cd_cds'
         ||' and '||aRiga||'.esercizio = '||aTestata||'.esercizio'
         ||' and '||aRiga||'.cd_unita_organizzativa = '||aTestata||'.cd_unita_organizzativa'
         ||' and '||aRiga||'.'||getNomePg(aCdTipoDoc)||'='||aTestata||'.'||getNomePg(aCdTipoDoc);

  IF isInTabellaGenerico(aCdTipoDoc) = 'Y' THEN
   aStr:=aStr||' and '||aRiga||'.cd_tipo_documento_amm = '||aTestata||'.cd_tipo_documento_amm';
  END IF;
  RETURN aStr;
 END;

 FUNCTION getTstaRigaFromCondForKey (
  aCdTipoDoc VARCHAR2,
  aCdCds VARCHAR2,
  aEs NUMBER,
  aCdUo VARCHAR2,
  aPgDoc NUMBER
 ) RETURN VARCHAR2 IS
  aStr VARCHAR2(2000);
  aRiga VARCHAR2(40);
 BEGIN
  aRiga:=GETTABELLADETT(aCdTipoDoc);
  IF aRiga IS NULL THEN
   RETURN NULL;
  END IF;
  aStr:=' from '||GETTABELLA(aCdTipoDoc)||','||GETTABELLADETT(aCdTipoDoc);
  aStr:=aStr||getTstaWhereCondForKey (
               aCdTipoDoc,
               aCdCds,
               aEs,
               aCdUo,
               aPgDoc
              ) || ' and ' || getTstaRigaCondForJoin (aCdTipoDoc);
  RETURN aStr;
 END;

 FUNCTION getNextNum(aCdCds VARCHAR2, aEs NUMBER, aCdUo VARCHAR2, aTipoDocumento VARCHAR2, aUser VARCHAR2, aTSNow DATE DEFAULT SYSDATE) RETURN NUMBER IS
  aNum NUMERAZIONE_DOC_AMM%ROWTYPE;
  aTipoDoc TIPO_DOCUMENTO_AMM%ROWTYPE;
 BEGIN
  BEGIN
    IF aTipoDocumento LIKE '%$' THEN
     Ibmerr001.RAISE_ERR_GENERICO('Tipo di documento provvisorio non gestito da questo numeratore: '||aTipoDocumento);
	END IF;
    BEGIN
     SELECT * INTO aTipoDoc FROM TIPO_DOCUMENTO_AMM WHERE
      cd_tipo_documento_amm = aTipoDocumento;
    EXCEPTION WHEN NO_DATA_FOUND THEN
     Ibmerr001.RAISE_ERR_GENERICO('Tipo di documento non esistente: '||aTipoDocumento);
    END;
    SELECT * INTO aNum FROM NUMERAZIONE_DOC_AMM WHERE
         ESERCIZIO = aEs
     AND cd_cds = aCdCds
     AND cd_unita_organizzativa = aCdUo
     AND cd_tipo_documento_amm = aTipoDocumento FOR UPDATE NOWAIT;
  EXCEPTION WHEN NO_DATA_FOUND THEN
      aNum.CD_CDS:=aCdCds;
      aNum.CD_UNITA_ORGANIZZATIVA:=aCdUo;
      aNum.ESERCIZIO:=aEs;
      aNum.CD_TIPO_DOCUMENTO_AMM:=aTipoDocumento;
      aNum.CORRENTE:=0;
      aNum.UTCR:=aUser;
      aNum.DACR:=aTSNow;
      aNum.UTUV:=aUser;
      aNum.DUVA:=aTSNow;
      aNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_AMM(aNum);
  END;
  UPDATE NUMERAZIONE_DOC_AMM SET
       corrente=corrente+1,
	   utuv=aUser,
	   duva=aTSNow,
	   pg_ver_rec=pg_ver_rec+1
  WHERE
       ESERCIZIO = aEs
   AND cd_cds = aCdCds
   AND cd_unita_organizzativa = aCdUo
   AND cd_tipo_documento_amm = aTipoDocumento;
  RETURN aNum.corrente+1;
 END;


 FUNCTION getTabella(aTipoDocAmm VARCHAR2) RETURN VARCHAR2 IS
 BEGIN
  IF isInTabellaFatturaPassiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'FATTURA_PASSIVA';
  END IF;
  IF isInTabellaFatturaAttiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'FATTURA_ATTIVA';
  END IF;
  IF isInTabellaGenerico(aTipoDocAmm) = 'Y' THEN
   RETURN 'DOCUMENTO_GENERICO';
  END IF;
  IF isInTabellaCompenso(aTipoDocAmm) = 'Y' THEN
   RETURN 'COMPENSO';
  END IF;
  IF isInTabellaAnticipo(aTipoDocAmm) = 'Y' THEN
   RETURN 'ANTICIPO';
  END IF;
  IF isInTabellaRimborso(aTipoDocAmm) = 'Y' THEN
   RETURN 'RIMBORSO';
  END IF;
  IF isInTabellaMissione(aTipoDocAmm) = 'Y' THEN
   RETURN 'MISSIONE';
  END IF;
  IF isInTabellaAutofattura(aTipoDocAmm) = 'Y' THEN
   RETURN 'AUTOFATTURA';
  END IF;
  Ibmerr001.RAISE_ERR_GENERICO('Nessuna associazione a tabella per il tipo di documento specificato');
 END;

 FUNCTION getTabellaDett(aTipoDocAmm VARCHAR2) RETURN VARCHAR2 IS
 BEGIN
  IF isInTabellaFatturaPassiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'FATTURA_PASSIVA_RIGA';
  END IF;
  IF isInTabellaFatturaAttiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'FATTURA_ATTIVA_RIGA';
  END IF;
  IF isInTabellaGenerico(aTipoDocAmm) = 'Y' THEN
   RETURN 'DOCUMENTO_GENERICO_RIGA';
  END IF;
  IF isInTabellaCompenso(aTipoDocAmm) = 'Y' THEN
   RETURN 'COMPENSO_RIGA';
  END IF;
  IF isInTabellaMissione(aTipoDocAmm) = 'Y' THEN
   RETURN 'MISSIONE_RIGA';
  END IF;
  IF isInTabellaAnticipo(aTipoDocAmm) = 'Y'
   OR isInTabellaRimborso(aTipoDocAmm) = 'Y'
   OR isInTabellaAutofattura(aTipoDocAmm) = 'Y'
  THEN
   RETURN NULL;
  END IF;
  Ibmerr001.RAISE_ERR_GENERICO('Nessuna associazione a tabella dettagli per il tipo di documento specificato');
 END;

 FUNCTION getSezioneEconomica(aTipoDocAmm VARCHAR2) RETURN CHAR IS
  aSezione CHAR(1);
  aTipoEntrataSpesa CHAR(1);
 BEGIN
  BEGIN
   SELECT ti_entrata_spesa INTO aTipoEntrataSpesa FROM TIPO_DOCUMENTO_AMM WHERE
      cd_tipo_documento_amm = aTipoDocAmm;
  EXCEPTION WHEN NO_DATA_FOUND THEN
   Ibmerr001.RAISE_ERR_GENERICO('Tipo di documento non supportato');
  END;

  IF aTipoEntrataSpesa = Cnrctb001.GESTIONE_SPESE THEN
   aSezione:=IS_DARE;
  ELSE
   aSezione:=IS_AVERE;
  END IF;
  RETURN aSezione;
 END;

 FUNCTION getSezioneEconomicaCori(aTipoDocAmm VARCHAR2) RETURN CHAR IS
  aSezione CHAR(1);
 BEGIN
  RETURN getSezioneEconomica(aTipoDocAmm);
 END;

 FUNCTION getNomePg(aTipoDocAmm VARCHAR2) RETURN VARCHAR2 IS
 BEGIN
  IF isInTabellaFatturaPassiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_FATTURA_PASSIVA';
  END IF;
  IF isInTabellaFatturaAttiva(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_FATTURA_ATTIVA';
  END IF;
  IF isInTabellaGenerico(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_DOCUMENTO_GENERICO';
  END IF;
  IF isInTabellaCompenso(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_COMPENSO';
  END IF;
  IF isInTabellaAnticipo(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_ANTICIPO';
  END IF;
  IF isInTabellaRimborso(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_RIMBORSO';
  END IF;
  IF isInTabellaMissione(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_MISSIONE';
  END IF;
  IF isInTabellaAutofattura(aTipoDocAmm) = 'Y' THEN
   RETURN 'PG_AUTOFATTURA';
  END IF;
  Ibmerr001.RAISE_ERR_GENERICO('Nessuna associazione a tabella per il tipo di documento specificato');
 END;

 FUNCTION getNomePgDett(aTipoDocAmm VARCHAR2) RETURN VARCHAR2 IS
 BEGIN
  IF
        isInTabellaFatturaPassiva(aTipoDocAmm) = 'Y'
     OR isInTabellaFatturaAttiva(aTipoDocAmm) = 'Y'
  	 OR isInTabellaGenerico(aTipoDocAmm) = 'Y'
     OR isInTabellaCompenso(aTipoDocAmm) = 'Y'
     OR isInTabellaMissione(aTipoDocAmm) = 'Y'
  THEN
   RETURN 'PROGRESSIVO_RIGA';
  END IF;
  IF
       isInTabellaAnticipo(aTipoDocAmm) = 'Y'
	OR isInTabellaRimborso(aTipoDocAmm) = 'Y'
	OR isInTabellaMissione(aTipoDocAmm) = 'Y'
	OR isInTabellaAutofattura(aTipoDocAmm) = 'Y'
  THEN
   RETURN NULL;
  END IF;
  Ibmerr001.RAISE_ERR_GENERICO('Nessuna associazione a tabella per il tipo di documento specificato');
 END;

 FUNCTION isInTabellaFatturaPassiva(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_FATTURA_PASSIVA) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;
 FUNCTION isInTabellaGenerico(aTipoDocAmm VARCHAR2) RETURN CHAR IS
  aTipo VARCHAR2(10);
 BEGIN
  BEGIN
   SELECT cd_tipo_documento_amm INTO aTipo FROM TIPO_DOCUMENTO_AMM WHERE
        CD_TIPO_DOCUMENTO_AMM = aTipoDocAmm
	AND FL_DOC_GENERICO = 'Y';
   RETURN 'Y';
  EXCEPTION WHEN NO_DATA_FOUND THEN
   RETURN 'N';
  END;
 END;
 FUNCTION isInTabellaFatturaAttiva(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_FATTURA_ATTIVA) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 FUNCTION isInTabellaCompenso(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_COMPENSO) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 FUNCTION isInTabellaAnticipo(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_ANTICIPO) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 FUNCTION isInTabellaRimborso(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_RIMBORSO) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 FUNCTION isInTabellaAutofattura(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_AUTOFATTURA) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 FUNCTION isInTabellaMissione(aTipoDocAmm VARCHAR2) RETURN CHAR IS
 BEGIN
  IF aTipoDocAmm IN (TI_MISSIONE) THEN
   RETURN 'Y';
  END IF;
  RETURN 'N';
 END;

 PROCEDURE ins_DOCUMENTO_GENERICO (aDest DOCUMENTO_GENERICO%ROWTYPE) IS
  BEGIN
   INSERT INTO DOCUMENTO_GENERICO (
     CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO
    ,CD_TIPO_DOCUMENTO_AMM
    ,PG_DOCUMENTO_GENERICO
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,DATA_REGISTRAZIONE
    ,DS_DOCUMENTO_GENERICO
    ,TI_ISTITUZ_COMMERC
    ,IM_TOTALE
    ,DT_PAGAMENTO_FONDO_ECO
    ,STATO_COFI
    ,STATO_COGE
    ,CD_DIVISA
    ,CAMBIO
    ,DT_CANCELLAZIONE
    ,ESERCIZIO_LETTERA
    ,PG_LETTERA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,DT_SCADENZA
    ,STATO_COAN
    ,STATO_PAGAMENTO_FONDO_ECO
    ,TI_ASSOCIATO_MANREV
	,DT_DA_COMPETENZA_COGE
	,DT_A_COMPETENZA_COGE
   ) VALUES (
     aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ESERCIZIO
    ,aDest.CD_TIPO_DOCUMENTO_AMM
    ,aDest.PG_DOCUMENTO_GENERICO
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.DATA_REGISTRAZIONE
    ,aDest.DS_DOCUMENTO_GENERICO
    ,aDest.TI_ISTITUZ_COMMERC
    ,aDest.IM_TOTALE
    ,aDest.DT_PAGAMENTO_FONDO_ECO
    ,aDest.STATO_COFI
    ,aDest.STATO_COGE
    ,aDest.CD_DIVISA
    ,aDest.CAMBIO
    ,aDest.DT_CANCELLAZIONE
    ,aDest.ESERCIZIO_LETTERA
    ,aDest.PG_LETTERA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.DT_SCADENZA
    ,aDest.STATO_COAN
    ,aDest.STATO_PAGAMENTO_FONDO_ECO
    ,aDest.TI_ASSOCIATO_MANREV
	,aDest.DT_DA_COMPETENZA_COGE
	,aDest.DT_A_COMPETENZA_COGE
    );
 END;
 PROCEDURE ins_DOCUMENTO_GENERICO_RIGA (aDest DOCUMENTO_GENERICO_RIGA%ROWTYPE) IS
  BEGIN
   INSERT INTO DOCUMENTO_GENERICO_RIGA (
     CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO
    ,CD_TIPO_DOCUMENTO_AMM
    ,PG_DOCUMENTO_GENERICO
    ,PROGRESSIVO_RIGA
    ,DS_RIGA
    ,IM_RIGA_DIVISA
    ,IM_RIGA
    ,CD_TERZO
    ,CD_TERZO_CESSIONARIO
    ,CD_TERZO_UO_CDS
    ,RAGIONE_SOCIALE
    ,NOME
    ,COGNOME
    ,CODICE_FISCALE
    ,PARTITA_IVA
    ,CD_MODALITA_PAG
    ,PG_BANCA
    ,CD_TERMINI_PAG
    ,CD_TERMINI_PAG_UO_CDS
    ,PG_BANCA_UO_CDS
    ,CD_MODALITA_PAG_UO_CDS
    ,NOTE
    ,DT_DA_COMPETENZA_COGE
    ,DT_A_COMPETENZA_COGE
    ,STATO_COFI
    ,DT_CANCELLAZIONE
    ,CD_CDS_OBBLIGAZIONE
    ,ESERCIZIO_OBBLIGAZIONE
    ,ESERCIZIO_ORI_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE_SCADENZARIO
    ,CD_CDS_ACCERTAMENTO
    ,ESERCIZIO_ACCERTAMENTO
    ,ESERCIZIO_ORI_ACCERTAMENTO
    ,PG_ACCERTAMENTO
    ,PG_ACCERTAMENTO_SCADENZARIO
    ,DACR
    ,UTCR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,TI_ASSOCIATO_MANREV
   ) VALUES (
     aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ESERCIZIO
    ,aDest.CD_TIPO_DOCUMENTO_AMM
    ,aDest.PG_DOCUMENTO_GENERICO
    ,aDest.PROGRESSIVO_RIGA
    ,aDest.DS_RIGA
    ,aDest.IM_RIGA_DIVISA
    ,aDest.IM_RIGA
    ,aDest.CD_TERZO
    ,aDest.CD_TERZO_CESSIONARIO
    ,aDest.CD_TERZO_UO_CDS
    ,aDest.RAGIONE_SOCIALE
    ,aDest.NOME
    ,aDest.COGNOME
    ,aDest.CODICE_FISCALE
    ,aDest.PARTITA_IVA
    ,aDest.CD_MODALITA_PAG
    ,aDest.PG_BANCA
    ,aDest.CD_TERMINI_PAG
    ,aDest.CD_TERMINI_PAG_UO_CDS
    ,aDest.PG_BANCA_UO_CDS
    ,aDest.CD_MODALITA_PAG_UO_CDS
    ,aDest.NOTE
    ,aDest.DT_DA_COMPETENZA_COGE
    ,aDest.DT_A_COMPETENZA_COGE
    ,aDest.STATO_COFI
    ,aDest.DT_CANCELLAZIONE
    ,aDest.CD_CDS_OBBLIGAZIONE
    ,aDest.ESERCIZIO_OBBLIGAZIONE
    ,aDest.ESERCIZIO_ORI_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE_SCADENZARIO
    ,aDest.CD_CDS_ACCERTAMENTO
    ,aDest.ESERCIZIO_ACCERTAMENTO
    ,aDest.ESERCIZIO_ORI_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO_SCADENZARIO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    ,aDest.TI_ASSOCIATO_MANREV
    );
 END;

 PROCEDURE ins_NUMERAZIONE_DOC_AMM (aDest NUMERAZIONE_DOC_AMM%ROWTYPE) IS
 BEGIN
  INSERT INTO NUMERAZIONE_DOC_AMM (
    CD_CDS
   ,CD_UNITA_ORGANIZZATIVA
   ,ESERCIZIO
   ,CD_TIPO_DOCUMENTO_AMM
   ,CORRENTE
   ,UTCR
   ,DACR
   ,UTUV
   ,DUVA
   ,PG_VER_REC
  ) VALUES (
    aDest.CD_CDS
   ,aDest.CD_UNITA_ORGANIZZATIVA
   ,aDest.ESERCIZIO
   ,aDest.CD_TIPO_DOCUMENTO_AMM
   ,aDest.CORRENTE
   ,aDest.UTCR
   ,aDest.DACR
   ,aDest.UTUV
   ,aDest.DUVA
   ,aDest.PG_VER_REC
   );
 END;

--==================================================================================================
-- Funzione di controllo ammissibilità della data di registrazione di fatture in base ai periodi
-- iva stampati
--==================================================================================================
PROCEDURE chkDtRegistrazPerIva
   (inCdCds FATTURA_PASSIVA.cd_cds_origine%TYPE,
    inCdUo FATTURA_PASSIVA.cd_uo_origine%TYPE,
    inEsercizio FATTURA_PASSIVA.ESERCIZIO%TYPE,
    inCdTipoSezionale FATTURA_PASSIVA.cd_tipo_sezionale%TYPE,
    inDtRegistrazione FATTURA_PASSIVA.dt_registrazione%TYPE
   ) IS
   aRecReportStato REPORT_STATO%ROWTYPE;

   ti_sez CHAR(1); -- Il tipo sezionale può essere di V=Vendite, A= acquisti
   aTiRegistro VARCHAR2(100);
BEGIN

   BEGIN
	  SELECT TI_ACQUISTI_VENDITE
	  INTO ti_sez
	  FROM TIPO_SEZIONALE
	  WHERE cd_tipo_sezionale =inCdTipoSezionale;

--Codice eliminato in quanto non esiste piu' la distinzione tra Registro Acquisti e Vendite
--Esiste solo il tipo 'REGISTRO_IVA'
/*
	  IF ti_sez ='A' THEN -- FATTURE_PASSIVE
		aTiRegistro := 'REGISTRO_IVA_ACQUISTI';
	  ELSE
		aTiRegistro := 'REGISTRO_IVA_VENDITE';
	  END IF;
*/

      aTiRegistro := 'REGISTRO_IVA';

      SELECT * INTO aRecReportStato
      FROM   REPORT_STATO A
      WHERE  A.cd_cds = inCdCds AND
             A.cd_unita_organizzativa = inCdUo AND
             A.ESERCIZIO = inEsercizio AND
             A.cd_tipo_sezionale = inCdTipoSezionale AND
             A.ti_documento = '*' AND
             A.tipo_report = aTiRegistro AND
             (stato = 'B' OR
              stato = 'C') AND
             A.dt_inizio =
                (SELECT MAX(B.dt_inizio)
                 FROM   REPORT_STATO B
                 WHERE  B.cd_cds = A.cd_cds AND
                        B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                        B.ESERCIZIO = A.ESERCIZIO AND
                        B.cd_tipo_sezionale = A.cd_tipo_sezionale AND
                        B.ti_documento = A.ti_documento AND
                        B.tipo_report = A.tipo_report AND
                        (B.stato = 'B' Or
                         B.stato = 'C'));

      IF Trunc(inDtRegistrazione) <= Trunc(aRecReportStato.dt_fine) THEN
         Ibmerr001.RAISE_ERR_GENERICO('Data registrazione inferiore/uguale a ultimo periodo definitivo di stampa registri IVA ' ||
                                      TO_CHAR(aRecReportStato.dt_fine, 'DD/MM/YYYY'));
      END IF;


   EXCEPTION

      WHEN NO_DATA_FOUND THEN
           NULL;

   END;

   RETURN;

END chkDtRegistrazPerIva;

PROCEDURE ins_FATTURA_PASSIVA
   (aDest FATTURA_PASSIVA%rowtype
   ) is

BEGIN

   INSERT INTO FATTURA_PASSIVA
          (CD_CDS,
           CD_UNITA_ORGANIZZATIVA,
           ESERCIZIO,
           PG_FATTURA_PASSIVA,
           CD_CDS_ORIGINE,
           CD_UO_ORIGINE,
           CD_TIPO_SEZIONALE,
           TI_FATTURA,
           PROTOCOLLO_IVA,
           PROTOCOLLO_IVA_GENERALE,
           DT_REGISTRAZIONE,
           NR_FATTURA_FORNITORE,
           DT_FATTURA_FORNITORE,
           ESERCIZIO_FATTURA_FORNITORE,
           DT_SCADENZA,
           DT_CANCELLAZIONE,
           DS_FATTURA_PASSIVA,
           TI_ISTITUZ_COMMERC,
           FL_INTRA_UE,
           FL_EXTRA_UE,
           FL_SAN_MARINO_CON_IVA,
           FL_SAN_MARINO_SENZA_IVA,
           FL_AUTOFATTURA,
           FL_BOLLA_DOGANALE,
           FL_SPEDIZIONIERE,
           FL_FATTURA_COMPENSO,
           CD_TERZO_CESSIONARIO,
           CD_TERZO_UO_CDS,
           CD_TERZO,
           RAGIONE_SOCIALE,
           NOME,
           COGNOME,
           CODICE_FISCALE,
           PARTITA_IVA,
           CD_TERMINI_PAG,
           CD_TERMINI_PAG_UO_CDS,
           PG_BANCA,
           PG_BANCA_UO_CDS,
           CD_MODALITA_PAG,
           CD_MODALITA_PAG_UO_CDS,
           IM_TOTALE_IMPONIBILE_DIVISA,
           IM_TOTALE_IMPONIBILE,
           IM_TOTALE_IVA,
           IM_TOTALE_FATTURA,
           CD_DIVISA,
           CAMBIO,
           STATO_COFI,
           STATO_COGE,
           DT_PAGAMENTO_FONDO_ECO,
           NUMERO_PROTOCOLLO,
           NOTE,
           DT_DA_COMPETENZA_COGE,
           DT_A_COMPETENZA_COGE,
           ESERCIZIO_LETTERA,
           PG_LETTERA,
           DACR,
           UTCR,
           DUVA,
           UTUV,
           PG_VER_REC,
           TI_ASSOCIATO_MANREV,
           STATO_COAN,
           STATO_PAGAMENTO_FONDO_ECO,
           IM_TOTALE_QUADRATURA,
           TI_BENE_SERVIZIO,
           CD_CDS_FAT_CLGS,
           CD_UO_FAT_CLGS,
           ESERCIZIO_FAT_CLGS,
           PG_FATTURA_PASSIVA_FAT_CLGS,
           FL_CONGELATA,
           fl_liquidazione_differita,
           data_protocollo,
					 esercizio_compenso,
					 cds_compenso,
					 uo_compenso,
					 pg_compenso
           )
   VALUES (aDest.CD_CDS,
           aDest.CD_UNITA_ORGANIZZATIVA,
           aDest.ESERCIZIO,
           aDest.PG_FATTURA_PASSIVA,
           aDest.CD_CDS_ORIGINE,
           aDest.CD_UO_ORIGINE,
           aDest.CD_TIPO_SEZIONALE,
           aDest.TI_FATTURA,
           aDest.PROTOCOLLO_IVA,
           aDest.PROTOCOLLO_IVA_GENERALE,
           aDest.DT_REGISTRAZIONE,
           aDest.NR_FATTURA_FORNITORE,
           aDest.DT_FATTURA_FORNITORE,
           aDest.ESERCIZIO_FATTURA_FORNITORE,
           aDest.DT_SCADENZA,
           aDest.DT_CANCELLAZIONE,
           aDest.DS_FATTURA_PASSIVA,
           aDest.TI_ISTITUZ_COMMERC,
           aDest.FL_INTRA_UE,
           aDest.FL_EXTRA_UE,
           aDest.FL_SAN_MARINO_CON_IVA,
           aDest.FL_SAN_MARINO_SENZA_IVA,
           aDest.FL_AUTOFATTURA,
           aDest.FL_BOLLA_DOGANALE,
           aDest.FL_SPEDIZIONIERE,
           aDest.FL_FATTURA_COMPENSO,
           aDest.CD_TERZO_CESSIONARIO,
           aDest.CD_TERZO_UO_CDS,
           aDest.CD_TERZO,
           aDest.RAGIONE_SOCIALE,
           aDest.NOME,
           aDest.COGNOME,
           aDest.CODICE_FISCALE,
           aDest.PARTITA_IVA,
           aDest.CD_TERMINI_PAG,
           aDest.CD_TERMINI_PAG_UO_CDS,
           aDest.PG_BANCA,
           aDest.PG_BANCA_UO_CDS,
           aDest.CD_MODALITA_PAG,
           aDest.CD_MODALITA_PAG_UO_CDS,
           aDest.IM_TOTALE_IMPONIBILE_DIVISA,
           aDest.IM_TOTALE_IMPONIBILE,
           aDest.IM_TOTALE_IVA,
           aDest.IM_TOTALE_FATTURA,
           aDest.CD_DIVISA,
           aDest.CAMBIO,
           aDest.STATO_COFI,
           aDest.STATO_COGE,
           aDest.DT_PAGAMENTO_FONDO_ECO,
           aDest.NUMERO_PROTOCOLLO,
           aDest.NOTE,
           aDest.DT_DA_COMPETENZA_COGE,
           aDest.DT_A_COMPETENZA_COGE,
           aDest.ESERCIZIO_LETTERA,
           aDest.PG_LETTERA,
           aDest.DACR,
           aDest.UTCR,
           aDest.DUVA,
           aDest.UTUV,
           aDest.PG_VER_REC,
           aDest.TI_ASSOCIATO_MANREV,
           aDest.STATO_COAN,
           aDest.STATO_PAGAMENTO_FONDO_ECO,
           aDest.IM_TOTALE_QUADRATURA,
           aDest.TI_BENE_SERVIZIO,
           aDest.CD_CDS_FAT_CLGS,
           aDest.CD_UO_FAT_CLGS,
           aDest.ESERCIZIO_FAT_CLGS,
           aDest.PG_FATTURA_PASSIVA_FAT_CLGS,
           aDest.FL_CONGELATA,
           aDest.fl_liquidazione_differita,
           aDest.data_protocollo,
           aDest.esercizio_compenso,
           aDest.cds_compenso,
           aDest.uo_compenso,
           aDest.pg_compenso);

END;

 procedure ins_FATTURA_PASSIVA_RIGA (aDest FATTURA_PASSIVA_RIGA%rowtype) is
  begin
   insert into FATTURA_PASSIVA_RIGA (
     PG_OBBLIGAZIONE_SCADENZARIO
    ,CD_CDS_ACCERTAMENTO
    ,ESERCIZIO_ACCERTAMENTO
    ,ESERCIZIO_ORI_ACCERTAMENTO
    ,PG_ACCERTAMENTO
    ,PG_ACCERTAMENTO_SCADENZARIO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CDS_ASSNCNA_FIN
    ,CD_UO_ASSNCNA_FIN
    ,ESERCIZIO_ASSNCNA_FIN
    ,PG_FATTURA_ASSNCNA_FIN
    ,PG_RIGA_ASSNCNA_FIN
    ,CD_CDS_ASSNCNA_ECO
    ,CD_UO_ASSNCNA_ECO
    ,ESERCIZIO_ASSNCNA_ECO
    ,PG_FATTURA_ASSNCNA_ECO
    ,PG_RIGA_ASSNCNA_ECO
    ,TI_ASSOCIATO_MANREV
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,ESERCIZIO
    ,PG_FATTURA_PASSIVA
    ,PROGRESSIVO_RIGA
    ,TI_ISTITUZ_COMMERC
    ,CD_BENE_SERVIZIO
    ,DS_RIGA_FATTURA
    ,PREZZO_UNITARIO
    ,QUANTITA
    ,IM_TOTALE_DIVISA
    ,IM_IMPONIBILE
    ,CD_VOCE_IVA
    ,FL_IVA_FORZATA
    ,IM_IVA
    ,IM_DIPONIBILE_NC
    ,STATO_COFI
    ,DT_DA_COMPETENZA_COGE
    ,DT_A_COMPETENZA_COGE
    ,DT_CANCELLAZIONE
    ,CD_CDS_OBBLIGAZIONE
    ,ESERCIZIO_OBBLIGAZIONE
    ,ESERCIZIO_ORI_OBBLIGAZIONE
    ,PG_OBBLIGAZIONE
    ,CD_TERZO_CESSIONARIO
    ,CD_TERZO
    ,CD_TERMINI_PAG
    ,PG_BANCA
    ,CD_MODALITA_PAG
    ,data_esigibilita_iva
   ) values (
     aDest.PG_OBBLIGAZIONE_SCADENZARIO
    ,aDest.CD_CDS_ACCERTAMENTO
    ,aDest.ESERCIZIO_ACCERTAMENTO
    ,aDest.ESERCIZIO_ORI_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO
    ,aDest.PG_ACCERTAMENTO_SCADENZARIO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS_ASSNCNA_FIN
    ,aDest.CD_UO_ASSNCNA_FIN
    ,aDest.ESERCIZIO_ASSNCNA_FIN
    ,aDest.PG_FATTURA_ASSNCNA_FIN
    ,aDest.PG_RIGA_ASSNCNA_FIN
    ,aDest.CD_CDS_ASSNCNA_ECO
    ,aDest.CD_UO_ASSNCNA_ECO
    ,aDest.ESERCIZIO_ASSNCNA_ECO
    ,aDest.PG_FATTURA_ASSNCNA_ECO
    ,aDest.PG_RIGA_ASSNCNA_ECO
    ,aDest.TI_ASSOCIATO_MANREV
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.ESERCIZIO
    ,aDest.PG_FATTURA_PASSIVA
    ,aDest.PROGRESSIVO_RIGA
    ,aDest.TI_ISTITUZ_COMMERC
    ,aDest.CD_BENE_SERVIZIO
    ,aDest.DS_RIGA_FATTURA
    ,aDest.PREZZO_UNITARIO
    ,aDest.QUANTITA
    ,aDest.IM_TOTALE_DIVISA
    ,aDest.IM_IMPONIBILE
    ,aDest.CD_VOCE_IVA
    ,aDest.FL_IVA_FORZATA
    ,aDest.IM_IVA
    ,aDest.IM_DIPONIBILE_NC
    ,aDest.STATO_COFI
    ,aDest.DT_DA_COMPETENZA_COGE
    ,aDest.DT_A_COMPETENZA_COGE
    ,aDest.DT_CANCELLAZIONE
    ,aDest.CD_CDS_OBBLIGAZIONE
    ,aDest.ESERCIZIO_OBBLIGAZIONE
    ,aDest.ESERCIZIO_ORI_OBBLIGAZIONE
    ,aDest.PG_OBBLIGAZIONE
    ,aDest.CD_TERZO_CESSIONARIO
    ,aDest.CD_TERZO
    ,aDest.CD_TERMINI_PAG
    ,aDest.PG_BANCA
    ,aDest.CD_MODALITA_PAG
    ,aDest.data_esigibilita_iva
    );
 end;
procedure aggiorna_data_differita(es NUMBER,cds VARCHAR2,uo VARCHAR2,pg_fattura NUMBER,riga NUMBER) Is
Begin
  Update fattura_passiva_riga Set data_esigibilita_iva=Trunc(Sysdate)
  Where
    esercizio = es And
    cd_cds   = cds And
    cd_unita_organizzativa = uo And
    pg_fattura_passiva = pg_fattura And
    progressivo_riga=riga;
End;
procedure aggiorna_data_differita_attive(es NUMBER,cds VARCHAR2,uo VARCHAR2,pg_fattura NUMBER,riga NUMBER) Is
Begin
  Update fattura_attiva_riga Set data_esigibilita_iva=Trunc(Sysdate)
  Where
    esercizio = es And
    cd_cds   = cds And
    cd_unita_organizzativa = uo And
    pg_fattura_attiva = pg_fattura And
    progressivo_riga=riga;
End;
procedure insProgrUnivocoFatturaPassiva(es NUMBER,data_a date)is
num  varchar2(10);
begin
for fatture in
 (select * from fattura_passiva
 where
   esercizio            = es and
   dt_registrazione    > (select dt01 from configurazione_cnr where
   cd_chiave_primaria   = 'REGISTRO_UNICO_FATPAS' and
   cd_chiave_secondaria = 'DATA_INIZIO') and
   dt_registrazione <= data_a  and
   PROGR_UNIVOCO is null
order by esercizio,dt_registrazione,cd_cds,cd_unita_organizzativa,pg_fattura_passiva
for update nowait) loop
  begin
  select cd_corrente into num from numerazione_base where
    esercizio = es and
    colonna='PG_REGISTRO_UNICO_FATPAS' and
    tabella ='FATTURA_PASSIVA';
 exception when no_data_found then
	Insert into NUMERAZIONE_BASE
   (ESERCIZIO, COLONNA, TABELLA, CD_CORRENTE, CD_MASSIMO, DUVA, UTUV, DACR, UTCR, PG_VER_REC, CD_INIZIALE)
 Values
   (es, 'PG_REGISTRO_UNICO_FATPAS', 'FATTURA_PASSIVA', '0', '99999999', sysdate, 'RIBALTAMENTO', sysdate, 'RIBALTAMENTO', 1, '1');
   num:='0';
end;
update fattura_passiva
 	set PROGR_UNIVOCO= to_number(num)+1
where
			esercizio = fatture.esercizio and
      cd_cds    = fatture.cd_cds  and
      cd_unita_organizzativa = fatture.cd_unita_organizzativa and
      pg_fattura_passiva = fatture.pg_fattura_passiva;
update NUMERAZIONE_BASE set cd_corrente =(num+1) where esercizio = es and colonna='PG_REGISTRO_UNICO_FATPAS' and tabella='FATTURA_PASSIVA';
end loop;
exception when CNRCTB850.RESOURCE_BUSY then
      IBMERR001.RAISE_ERR_GENERICO('Funzionalità temporaneamente non accessibile per l''esercizio e la data selezionata.');
end;
END;
