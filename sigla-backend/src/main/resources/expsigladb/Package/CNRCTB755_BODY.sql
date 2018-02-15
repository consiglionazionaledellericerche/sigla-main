--------------------------------------------------------
--  DDL for Package Body CNRCTB755
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB755" AS

 function getDesc(aR ext_cassiere00%rowtype) return varchar2 is
 begin
  return 'Riga:'||lpad(aR.pg_rec,5,'0')||' TR'||substr(aR.data,1,2);
 end;

 procedure print(aR T01) is
 begin
  IBMUTL010.SCRIVI(
   'T01-Codice SIA:'||aR.codice_sia||' Data elab.:'||aR.data_elab||' Ora elab. :'||aR.ora_elab
  );
 end;

 procedure print(aR T30) is
 begin
  IBMUTL010.SCRIVI(
   ' T30-Numero risc.:'||aR.numero_ordinativo
  );
 end;

 procedure print(aR T32) is
 begin
  IBMUTL010.SCRIVI(
   ' T32-Numero sosp:'||aR.numero_sosp
  );
 end;

 function isT01(aBuff varchar2) return boolean is
 begin
  return substr(aBuff,1,2)='01';
 end;

 function isT30(aBuff varchar2) return boolean is
 begin
  return substr(aBuff,1,2)='30';
 end;

 function isT32(aBuff varchar2) return boolean is
 begin
  return substr(aBuff,1,2)='32';
 end;

 function parseT01(aBuff varchar2) return T01 is
  aRec T01;
 begin
  aRec.TIPO_RECO        := substr(aBuff,1,2);
  aRec.CODICE_ENTE      := substr(aBuff,3,6);
  aRec.CODICE_SIA       := substr(aBuff,9,5);
  aRec.TIPO_REND        := substr(aBuff,14,2);
  aRec.TIPO_SERV        := substr(aBuff,16,1);
  aRec.ANNO_ESER        := to_number(substr(aBuff,17,4));
  aRec.DESCRI_ENTE      := substr(aBuff,21,40);
  aRec.SPORTE_TESO      := substr(aBuff,61,6);
  aRec.CAB              := to_number(substr(aBuff,67,5));
  aRec.NUMERO_CONT_TESO := substr(aBuff,72,12);
  aRec.TIPOLO_CONT      := substr(aBuff,84,1);
  aRec.DESCRI_SPOR_TESO := substr(aBuff,85,40);
  aRec.DATA_ELAB        := to_number(substr(aBuff,125,8));
  aRec.ORA_ELAB         := to_number(substr(aBuff,133,6));
  aRec.DA_DATA_CONT     := to_number(substr(aBuff,139,8));
  aRec.A_DATA_CONT      := to_number(substr(aBuff,147,8));
  aRec.CODICE_MINI      := substr(aBuff,155,7);
  aRec.CODICE_DIVI      := substr(aBuff,162,3);
  return aRec;
 end;

 function parseT30(aBuff varchar2) return T30 is
  aRec T30;
 begin
  aRec.TIPO_RECORD                    := substr(aBuff,1,2);
  aRec.NUMERO_ORDINATIVO              := substr(aBuff,3,14);
  aRec.SUFFISSO                       := to_number(trim(substr(aBuff,17,2)));
  aRec.TIPO_ORDINATIVO                := substr(aBuff,19,1);
  aRec.SINGOLOCOLLETTIVO              := substr(aBuff,20,1);
  aRec.VOCE_DI_BILANCIO               := substr(aBuff,21,10);
  aRec.ANNO_RESIDUI                   := to_number(trim(substr(aBuff,31,4)));
  aRec.TIPO_PAGAMENTO                 := substr(aBuff,35,2);
  aRec.TIPO_OPERAZIONE                := substr(aBuff,37,2);
  aRec.STATO_ORDINATIVO               := substr(aBuff,39,2);
  aRec.IMPORTO_CARICATO               := to_number(substr(aBuff,41,15)/100);
  aRec.SEGNO_IMPORTO_CARICATO         := TO_NUMBER(trim(IBMUTL001.REPLACETOKEN(IBMUTL001.REPLACETOKEN(substr(aBuff,56,1),'+','1'),'-','-1')));
  aRec.NUMERO_BUONO                   := to_number(trim(substr(aBuff,57,4)));
  aRec.NUMERO_SOTTOBUONO              := to_number(trim(substr(aBuff,61,4)));
  aRec.PROGRESSIVO                    := to_number(trim(substr(aBuff,65,5)));
  aRec.DESCRIZIONE_BENEFICIARIODEBITO := substr(aBuff,70,50);
  aRec.DATA_ESECUZIONE                := to_number(trim(substr(aBuff,120,8)));
  aRec.IMPORTO_OPERAZIONE             := to_number(substr(aBuff,128,15)/100);
  aRec.SEGNO_IMPORTO_OPERAZIONE       := TO_NUMBER(trim(IBMUTL001.REPLACETOKEN(IBMUTL001.REPLACETOKEN(substr(aBuff,143,1),'+','1'),'-','-1')));
  aRec.IMPORTO_RESIDUO_GENERALE       := to_number(substr(aBuff,144,15)/100);
  aRec.SEGNO_IMPORTO_RESIDUO          := TO_NUMBER(trim(IBMUTL001.REPLACETOKEN(IBMUTL001.REPLACETOKEN(substr(aBuff,159,1),'+','1'),'-','-1')));
  aRec.NUMERO_BOLLETTAQUIETANZA       := to_number(trim(substr(aBuff,160,7)));
  aRec.CAUSALE                        := substr(aBuff,167,50);
  aRec.CODICE_SPORTELLO_OPERANTE      := substr(aBuff,217,6);
  aRec.CAB_SPORTELLO_OPERANTE         := substr(aBuff,223,5);
  aRec.ABI_BENEFICIARIODEBITORE       := substr(aBuff,228,5);
  aRec.CAB_BENEFICIARIODEBITORE       := substr(aBuff,233,5);
  aRec.NUMERO_CC_BENEFICIARIODEBITORE := substr(aBuff,238,12);
  aRec.CRO                            := substr(aBuff,250,22);
  aRec.CRI                            := substr(aBuff,272,22);
  aRec.ORDINATIVO_RETTIFICATO         := substr(aBuff,294,14);
  aRec.NUMERO_SOSPESO                 := substr(aBuff,308,18);
  aRec.ENTE_SOSPESO                   := substr(aBuff,326,6);
  aRec.DATA_ESECUZIONE_POOL           := substr(aBuff,332,8);
  aRec.FILLER                         := substr(aBuff,340,101);
  aRec.TIMESTAMP                      := substr(aBuff,441,19);
  aRec.CODICE_DIVISA                  := substr(aBuff,460,3);
  return aRec;
 end;

 function parseT32(aBuff varchar2) return T32 is
  aRec T32;
 begin
  aRec.TIPO_RECO:=substr(aBuff,1,2);
  aRec.NUMERO_SOSP:=substr(aBuff,3,18);
  aRec.TIPO_VOCE:=substr(aBuff,21,1);
  aRec.TIPO_SOSP:=substr(aBuff,22,6);
  aRec.TIPO_PAGA:=substr(aBuff,28,2);
  aRec.TIPO_OPER:=substr(aBuff,30,2);
  aRec.STATO_SOSP:=substr(aBuff,32,2);
  aRec.DESCRI_BENE:=substr(aBuff,34,50);
  aRec.DATA_CARI:=to_number(substr(aBuff,84,8));
  aRec.IMPORT:=to_number(substr(aBuff,92,15)/100);
  aRec.SEGNO_IMPO:=TO_NUMBER(IBMUTL001.REPLACETOKEN(IBMUTL001.REPLACETOKEN(substr(aBuff,107,1),'+','1'),'-','-1'));
  aRec.DATA_OPER:=to_number(substr(aBuff,108,8));
  aRec.DATA_VALU:=to_number(substr(aBuff,116,8));
  aRec.IMPORT_OPER:=to_number(substr(aBuff,124,15)/100);
  aRec.SEGNO_OPER:=TO_NUMBER(IBMUTL001.REPLACETOKEN(IBMUTL001.REPLACETOKEN(substr(aBuff,139,1),'+','1'),'-','-1'));
  aRec.IMPORT_RESI_OPER:=to_number(substr(aBuff,140,15)/100);
  aRec.SEGNO_IMPO_RESI_OPER:=TO_NUMBER(IBMUTL001.REPLACETOKEN(IBMUTL001.REPLACETOKEN(substr(aBuff,155,1),'+','1'),'-','-1'));
  aRec.CAUSAL_1A_PART:=substr(aBuff,156,50);
  aRec.CODICE_SPOR_OPER:=substr(aBuff,206,6);
  aRec.CAB_SPOR_OPER:=substr(aBuff,212,5);
  aRec.ABI_BENE:=substr(aBuff,217,5);
  aRec.CAB_BENE:=substr(aBuff,222,5);
  aRec.NUMERO_CC_BENE:=substr(aBuff,227,12);
  aRec.CRO:=substr(aBuff,239,22);
  aRec.CRI:=substr(aBuff,261,22);
  aRec.NUMERO_CC_RETT:=substr(aBuff,283,12);
  aRec.INDIRI_BENE:=substr(aBuff,295,40);
  aRec.LOCALI_BENE:=substr(aBuff,335,30);
  aRec.CAP_BENE:=substr(aBuff,365,5);
  aRec.PROVIN_BENE:=substr(aBuff,370,3);
  aRec.CAUSAL_2A_PART:=substr(aBuff,373,60);
  aRec.DATA_ORDI:=to_number(substr(aBuff,433,8));
  aRec.TIMEST:=substr(aBuff,441,19);
  aRec.CODICE_DIVI:=substr(aBuff,460,3);
  aRec.NUMERO_SOSP_SOST:=substr(aBuff,463,15);
  return aRec;
 end;

 procedure ins_EXT_CASSIERE00 (aDest EXT_CASSIERE00%rowtype) is
 begin
  insert into EXT_CASSIERE00 (
    ESERCIZIO
   ,NOME_FILE
   ,PG_REC
   ,TR
   ,STATO
   ,DATA
   ,DACR
   ,UTCR
   ,DUVA
   ,UTUV
   ,PG_VER_REC
  ) values (
    aDest.ESERCIZIO
   ,aDest.NOME_FILE
   ,aDest.PG_REC
   ,aDest.TR
   ,aDest.STATO
   ,aDest.DATA
   ,aDest.DACR
   ,aDest.UTCR
   ,aDest.DUVA
   ,aDest.UTUV
   ,aDest.PG_VER_REC
   );
 end;
 procedure ins_EXT_CASSIERE00_LOGS (aDest EXT_CASSIERE00_LOGS%rowtype) is
  begin
   insert into EXT_CASSIERE00_LOGS (
     ESERCIZIO
    ,NOME_FILE
    ,PG_ESECUZIONE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.NOME_FILE
    ,aDest.PG_ESECUZIONE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
END;
