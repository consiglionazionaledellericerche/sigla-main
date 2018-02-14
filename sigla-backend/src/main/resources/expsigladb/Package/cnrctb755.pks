CREATE OR REPLACE PACKAGE CNRCTB755 AS
--==================================================================================================
--
-- CNRCTB755 - package di servizio per gestione cassiere
--
-- Date: 29/05/2003
-- Version: 1.4
--
-- Dependency:
--
-- History:
--
-- Date: 20/03/2003
-- Version: 1.0
-- Creazione package
--
-- Date: 15/04/2003
-- Version: 1.1
-- Modifica struttura EXT_CASSIERE00
--
-- Date: 26/04/2003
-- Version: 1.2
-- Inserimento in indice dei logs per file cassiere
--
-- Date: 25/05/2003
-- Version: 1.3
-- parse record T30
--
-- Date: 29/05/2003
-- Version: 1.4
-- Modifica gestione record T30
--
--==================================================================================================
--
-- Constants
--

 -- Stato in EXT_CASSIERE00

 STATO_RECORD_INIZIALE CONSTANT CHAR(1) := 'I';
 STATO_RECORD_PROCESSATO CONSTANT CHAR(1) := 'P';

 -- Tipo log cassiere

 LOG_TIPO_INTERF_CASS00 CONSTANT VARCHAR2(20) := 'INTERF_CASS00';

 TYPE aGRC IS REF CURSOR;

 TYPE T01 IS RECORD (
  TIPO_RECO varchar2(2),                  -- Tipo record -> "01" -> 2 AN
  CODICE_ENTE varchar2(6),                -- Codice Ente -> Codice Ente -> 6 AN
  CODICE_SIA varchar2(5),                 -- Codice SIA -> Codice Sia -> 5 AN
  TIPO_REND varchar2(2),                  -- Tipo rendicontazione -> Tipo rendicontazione            "01"=Giornaliera di cassa             "02"=Quadro di raccordo             "XX"= .......................... -> 2 AN
  TIPO_SERV varchar2(1),                  -- Tipo servizio -> Tipo servizio (T=Tesoreria - C=Cassa) -> 1 AN
  ANNO_ESER number(4),                    -- Anno esercizio -> Anno di esercizio -> 4 N(AAAA)
  DESCRI_ENTE varchar2(40),               -- Descrizione Ente -> Descrizione ente -> 40 AN
  SPORTE_TESO varchar2(6),                -- Sportello Tesoriere -> Sportello tesoriere -> 6 AN
  CAB number(5),                          -- CAB -> CAB -> 5 N
  NUMERO_CONT_TESO varchar2(12),          -- Numero conto Tesoriere -> Numero conto Tesoriere -> 12 AN
  TIPOLO_CONT varchar2(1),                -- Tipologia conto -> Tipologia conto(1) (2) (3) -> 1 AN
  DESCRI_SPOR_TESO varchar2(40),          -- Descrizione sportello Tesoriere -> Descrizione sportello tesoriere -> 40 AN
  DATA_ELAB number(8),                    -- Data elaborazione -> Data di elaborazione -> 8 N (AAAAMMGG)
  ORA_ELAB number(6),                     -- Ora elaborazione -> Ora di elaborazione  -> 6 N (HHMMSS)
  DA_DATA_CONT number(8),                 -- Da data contabile -> Inizio data contabile -> 8 N (AAAAMMGG)
  A_DATA_CONT number(8),                  -- A data contabile -> Fine data contabile -> 8 N (AAAAMMGG)
  CODICE_MINI varchar2(7),                -- Codice Ministeriale -> Codice Ministeriale (valido per Enti LOCALI, ASL e IACP) -> 7 AN
  CODICE_DIVI varchar2(3)                 -- Codice divisa -> ITL = Lire , EUR = Euro . Il campo ha valore per l.intero flusso -> 3 AN
 );

 TYPE T30 IS RECORD (
  TIPO_RECORD varchar2(2),                      -- Tipo record -> """"""30""""""" -> 2 AN
  NUMERO_ORDINATIVO varchar2(14),               -- Numero ordinativo -> Numero ordinativo" -> 14 AN
  SUFFISSO number(2),                           -- Suffisso -> Suffisso numero ordinativo" -> 2 N
  TIPO_ORDINATIVO varchar2(1),                  -- Tipo ordinativo -> ""Voce di entrata o uscita :           - """"R"""" -> 1 AN
  SINGOLOCOLLETTIVO varchar2(1),                -- Singolo/Collettivo -> ""Tipologia ordinativo                  - """"S""" -> 1 AN
  VOCE_DI_BILANCIO varchar2(10),                -- Voce di bilancio -> ""Capitolo (6 N)Articolo (3 N)Lettera (1 N)Enti Lo -> 10 AN
  ANNO_RESIDUI number(4),                       -- Anno residui -> Anno riferimento residui(Ente con bilancio)" -> 4 N        (AAAA)
  TIPO_PAGAMENTO varchar2(2),                   -- Tipo pagamento -> Tipo pagamento(rif. Tabella)" -> 2 AN
  TIPO_OPERAZIONE varchar2(2),                  -- Tipo operazione -> Tipo operazione(rif. Tabella)" -> 2 AN
  STATO_ORDINATIVO varchar2(2),                 -- Stato ordinativo -> ""Codice per raggruppamento degli ordinativi in ba -> 2 AN
  IMPORTO_CARICATO number(13,2),                -- Importo caricato -> Importo caricato (facciale)" -> 15 N
  SEGNO_IMPORTO_CARICATO number(1),             -- Segno importo caricato -> Segno dell'ordinativo caricato" -> 1 AN
  NUMERO_BUONO number(4),                       -- Numero buono -> Numero buono(solo per gli ordini di accreditamento -> 4 N
  NUMERO_SOTTOBUONO number(4),                  -- Numero sottobuono -> Numero sotto buono(solo per gli ordini di accredit -> 4 N
  PROGRESSIVO number(5),                        -- Progressivo -> Progressivo relativo al beneficiario/debitore" -> 5 N
  DESCRIZIONE_BENEFICIARIODEBITO varchar2(50),  -- Descrizione beneficiario/debitore -> Descrizione beneficiario/debitore" -> 50 AN
  DATA_ESECUZIONE number(8),                    -- Data esecuzione -> Data esecuzione" -> 8 N(AAAAMMGG)
  IMPORTO_OPERAZIONE number(13,2),              -- Importo operazione -> Importo operazione" -> 15 N
  SEGNO_IMPORTO_OPERAZIONE number(1),           -- Segno importo operazione -> Segno dell'operazione" -> 1 AN
  IMPORTO_RESIDUO_GENERALE number(13,2),        -- Importo residuo generale -> Importo residuo generale" -> 15 N
  SEGNO_IMPORTO_RESIDUO number(1),              -- Segno importo residuo -> Segno del residuo generale" -> 1 AN
  NUMERO_BOLLETTAQUIETANZA number(7),           -- Numero bolletta/quietanza -> Numero bolletta per reversale / quietanza per mand -> 7 N
  CAUSALE varchar2(50),                         -- Causale -> Descrizione causale" -> 50 AN
  CODICE_SPORTELLO_OPERANTE varchar2(6),        -- Codice sportello operante -> Codice dello sportello che effettua l'operazione" -> 6 AN
  CAB_SPORTELLO_OPERANTE varchar2(5),           -- CAB sportello operante -> CAB dello sportello che effettua l'operazione" -> 5 AN
  ABI_BENEFICIARIODEBITORE varchar2(5),         -- ABI beneficiario/debitore -> Codice ABI del conto del beneficiario/debitore" -> 5 AN
  CAB_BENEFICIARIODEBITORE varchar2(5),         -- CAB beneficiario/debitore -> Codice CAB del conto del beneficiario/debitore" -> 5 AN
  NUMERO_CC_BENEFICIARIODEBITORE varchar2(12),  -- Numero c/c beneficiario/debitore -> Numero conto corrente del beneficiario/debitore" -> 12 AN
  CRO varchar2(22),                             -- C.R.O. -> Codice di Riferimento dell' Operazione" -> 22 AN
  CRI varchar2(22),                             -- C.R.I. -> Codice di Riferimento Interno dell' Operazione" -> 22 AN
  ORDINATIVO_RETTIFICATO varchar2(14),          -- Ordinativo rettificato -> Numero dell'ordinativo che viene rettificato dall' -> 14 AN
  NUMERO_SOSPESO varchar2(18),                  -- Numero sospeso -> Numero sospeso evaso dall'ordinativo" -> 18 AN
  ENTE_SOSPESO varchar2(6),                     -- Ente sospeso -> Codice dell'ente che crea il sospeso" -> 6 AN
  DATA_ESECUZIONE_POOL varchar2(8),             -- Data esecuzione pool -> Data esecuzione per enti in regime di pool di Teso -> 8 AN
  FILLER varchar2(101),                         -- Filler -> Spazi" -> 101 AN
  TIMESTAMP varchar2(19),                       -- Timestamp -> Giorno - Orario temporale in cui viene creato il m -> 19 AN
  CODICE_DIVISA varchar2(3)                    -- Codice Divisa -> ITL=Lire EUR = EuroIl campo ? la ripetizione di qu -> 3 AN
 );

 TYPE T32 IS RECORD (
  TIPO_RECO varchar2(2),                  -- Tipo record -> """32""" -> 2 AN
  NUMERO_SOSP varchar2(18),               -- Numero sospeso -> Numero sospeso -> 18 AN
  TIPO_VOCE varchar2(1),                  -- Tipo voce -> "Voce di entrata o di uscita             ""E""=Entrata  - ""U""=Uscita     " -> 1 AN
  TIPO_SOSP varchar2(6),                  -- Tipo sospeso -> Tipologia sospeso(per usi futuri) -> 6 AN
  TIPO_PAGA varchar2(2),                  -- Tipo pagamento -> Tipo pagamento (rif. tabella) -> 2 AN
  TIPO_OPER varchar2(2),                  -- Tipo operazione -> Tipo operazione (rif. tabella) -> 2 AN
  STATO_SOSP varchar2(2),                 -- Stato sospeso -> "Codice per raggruppamento dei sospesi in base allo stato: .01. = aperto -> 2 AN
  DESCRI_BENE varchar2(50),               -- Descrizione beneficiario/debitore -> Descrizione beneficiario/debitore -> 50 AN
  DATA_CARI number(8),                    -- Data carico -> Data carico del movimento -> 8 N (AAAAMMGG)
  IMPORT number(13,2),                    -- Importo -> Importo del sospeso -> 15 N
  SEGNO_IMPO number(1),                   -- Segno importo -> Segno dell'importo del sospeso(+ /-) -> 1 AN
  DATA_OPER number(8),                    -- Data operazione -> Data operazione -> 8 N (AAAAMMGG)
  DATA_VALU number(8),                    -- Data valuta -> Data valuta del sposeso -> 8 N (AAAAMMGG)
  IMPORT_OPER number(13,2),               -- Importo operazione -> Importo dell' operazione -> 15 N
  SEGNO_OPER number(1),                   -- Segno operazione -> Segno dell' operazione -> 1 AN
  IMPORT_RESI_OPER number(13,2),          -- Importo residuo operazione -> Importo residuo dell' operazione -> 15 N
  SEGNO_IMPO_RESI_OPER number(1),         -- Segno Importo residuo operazione ->  Segno dell' importo residuo del sospeso -> 1 AN
  CAUSAL_1A_PART varchar2(50),            -- Causale . 1a parte -> Descrizione causale . 1a parte -> 50 AN
  CODICE_SPOR_OPER varchar2(6),           -- Codice sportello operante -> Codice dello sportello che effettua l'operazione -> 6 AN
  CAB_SPOR_OPER varchar2(5),              -- CAB sportello operante -> CAB dello sportello che effettua l'operazione -> 5 AN
  ABI_BENE varchar2(5),                   -- ABI beneficiario/debitore -> Codice ABI del conto del beneficiario/debitore -> 5 AN
  CAB_BENE varchar2(5),                   -- CAB beneficiario/debitore -> Codice CAB del conto del beneficiario/debitore -> 5 AN
  NUMERO_CC_BENE varchar2(12),            -- Numero c/c beneficiario/debitore -> Numero conto corrente del beneficiario/debitore -> 12 AN
  CRO varchar2(22),                       -- C.R.O. -> Codice di Riferimento dell' Operazione -> 22 AN
  CRI varchar2(22),                       -- C.R.I. -> Codice di Riferimento Interno dell' Operazione -> 22 AN
  NUMERO_CC_RETT varchar2(12),            -- Numero c/c rettificato -> Numero conto corrente rettificato -> 12 AN
  INDIRI_BENE varchar2(40),               -- Indirizzo beneficiario/debitore -> Indirizzo del beneficiario/debitore -> 40 AN
  LOCALI_BENE varchar2(30),               -- Localit? beneficiario/debitore -> Localit? del beneficiario/debitore -> 30 AN
  CAP_BENE varchar2(5),                   -- Cap Beneficiario/debitore -> Codice di avviamento postale del beneficiario/debitore -> 5 AN
  PROVIN_BENE varchar2(3),                -- Provincia beneficiario/debitore -> Provincia del beneficiario/debitore -> 3 AN
  CAUSAL_2A_PART varchar2(60),            -- Causale . 2a parte -> Descrizione causale . 2a parte -> 60 AN
  DATA_ORDI number(8),                    -- Data ordine -> Data nella quale viene effettuato il versamento -> 8 N
  TIMEST varchar2(19),                    -- Timestamp -> Data . Ora di creazione del movimento -> 19 AN
  CODICE_DIVI varchar2(3),                -- Codice Divisa -> ITL=Lire EUR = EuroIl campo ? la ripetizione di quello gi? contenuto nel record di testa -> 3 AN
  NUMERO_SOSP_SOST varchar2(15)           -- Numero Sospeso Sostituito -> Nel caso che il sospeso sia in sostituzione di un sospeso precedentemente aperto -> 15 AN
 );

 function isT01(aBuff varchar2) return boolean;
 function isT30(aBuff varchar2) return boolean;
 function isT32(aBuff varchar2) return boolean;

 function parseT01(aBuff varchar2) return T01;
 function parseT30(aBuff varchar2) return T30;
 function parseT32(aBuff varchar2) return T32;

 procedure print(aR T01);
 procedure print(aR T30);
 procedure print(aR T32);

 procedure ins_EXT_CASSIERE00 (aDest EXT_CASSIERE00%rowtype);
 procedure ins_EXT_CASSIERE00_LOGS (aDest EXT_CASSIERE00_LOGS%rowtype);

 function getDesc(aR EXT_CASSIERE00%rowtype) return varchar2;

END;


CREATE OR REPLACE PACKAGE BODY CNRCTB755 AS

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


