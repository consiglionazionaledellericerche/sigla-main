CREATE OR REPLACE package IBMUTL001 as
--==================================================================================================
--
-- IBMUTL001 - Package di utilita generiche
--
-- Date: 17/03/2004
-- Version: 2.11
--
-- Procedure e Funzioni di servizio generale
--
-- History:
--
-- Date: 20/09/2001
-- Version: 1.1
-- Aggiunta la funzione di conteggio delle occorrenze di una stringa in un'altra
--
-- Date: 16/03/2002
-- Version: 1.2
-- Allineamento con correttiva/evolutiva 1.1.0
-- Funzione di check stringa numerica
--
-- Date: 21/03/2002
-- Version: 1.3
-- Inserimento funzione getLocalTransactionID
-- Funzione che restituisce l'identificativo della Transazione Oracle in uso
--
-- Date: 25/03/2002
-- Version: 1.4
-- Eliminazione output da conta occorrenze
--
-- Date: 26/03/2002
-- Version: 1.5
-- Aggiunti metodi manipolazione stringhe (tokenizzazione, stripping, frazionamento)
--
-- Date: 26/03/2002
-- Version: 1.6
-- Aggiunto metodo di replace token
--
-- Date: 17/05/2002
-- Version: 1.7
-- Aggiunti metodi per dynamic sql
--
-- Date: 26/06/2002
-- Version: 1.8
--
-- Aggiunti metodi gestione date
--
-- Date: 28/06/2002
-- Version: 1.9
--
-- Fix errore in calcolo del numero dei mesi che intercorrono tra due date
--
-- Date: 10/07/2002
-- Version: 2.0
--
-- Fix errore di conversione della data
--
-- Date: 26/07/2002
-- Version: 2.1
--
-- Aggiunte routine per calcolo differenza giorni e rettifica calcolo matematico delle date
--
-- Date: 01/08/2002
-- Version: 2.2
--
-- Fix errori in routine date
--
-- Date: 05/08/2002
-- Version: 2.3
--
-- Inserita routine per calcolo giorni tra due date in anno commerciale
--
-- Date: 17/09/2002
-- Version: 2.4
--
-- Inserita function inlettere() per la conversione di un numero in lettere
--
-- Date: 15/10/2002
-- Version: 2.5
--
-- Fix in calcolo numero giorni tra due date in anno commerciale se entrambe le date rappresentano
-- il giorno 15 di un mese
--
-- Date: 18/03/2003
-- Version: 2.6
--
-- Aggiunta procedura di deferred commit
--
-- Date: 02/04/2003
-- Version: 2.7
--
-- Aggiunto callback di recupero connessione
-- Aggiunto metodo lock della transazione per deferred commit
--
-- Date: 03/04/2003
-- Version: 2.8
-- Fix su unlock transation per on-line
--
-- Date: 20/05/2003
-- Version: 2.9
-- Corretta funzione conversione in lettere (lunghezza stringa)
--
-- Date: 17/02/2004
-- Version: 2.10
-- Corretta funzione isNumeric: errore 765
--
-- Date: 17/03/2004
-- Version: 2.11
--
-- Aggiunta routine per tornare il mese data una data
--
-- Date: 27/02/2005
-- Version: 2.12
--
-- Aggiunta funzione per tornare la data di refresh del DB
--
--==================================================================================================
--
-- Constants:
--

   isIntervalloDateMensile CONSTANT CHAR(1):='Y';
   isNotIntervalloDateMensile CONSTANT CHAR(1):='N';
   isIntervalloDateMese CONSTANT CHAR(1):='Y';
   isNotIntervalloDateMese CONSTANT CHAR(1):='N';

type stringList is table of varchar2(1000) index by binary_integer;

-- per le look-up-table e per la collezione di token di 3 elementi
TYPE V_ARR IS VARRAY(20) OF VARCHAR2(20);

-- Functions e Procedures:
 -- Callbakc chiamato ad ogni richiesta connessione a DATASOURCE
 procedure trace_user_connection(aUser varchar2, aTSNow date, aHTTPSID varchar2);
 -- Funzione di lock applicativo della transazione per il deferred commit
 procedure LOCK_TRANSACTION;
 -- Funzione di commit deferred per la corretta chiusura di transazione applicative
 PROCEDURE DEFERRED_COMMIT;
 -- Funzione di unlock della transazione per invocazione client
 PROCEDURE UNLOCK_TRANSACTION;

 -- Dot Concat: concatena aS1 ed aS2 con un punto separatore
 function dotConcat(aS1 varchar2, aS2 varchar2) return varchar2;

 -- Conta il numero di occorrenze della stringa aPattern in aOrigine
 function contaOccorrenze(aOrigine varchar2, aPattern varchar2) return NUMBER;

 -- Ritorna 'Y' se la stringa aStr rappresenta un numero
 function isNumeric(aStr varchar2) return char;

  -- Restituisce il LOCAL_TRANSACTION_ID
 function getLocalTransactionID(createTransaction BOOLEAN) return VARCHAR2;

 -- Ritorna la stringa epurata delle occorrenze del pattern aPattern
 function strip(aStr varchar2, aPattern varchar2) return varchar2;

 -- Ritorna la stringa epurata delle occorrenze del pattern aPattern
 function replaceToken(aStr varchar2, aWhat varchar2, aWith varchar2) return varchar2;

 -- Funzioni di spezzamento
 function rightOfFirst(aStr varchar2, aPattern varchar2) return varchar2;
 function leftOfFirst(aStr varchar2, aPattern varchar2) return varchar2;
 function rightOfLast(aStr varchar2, aPattern varchar2) return varchar2;
 function leftOfLast(aStr varchar2, aPattern varchar2) return varchar2;

 function getErrorMessage Return varchar2;
 -- Function che ritorna una stringList di tokens estratti da aStr con spezzmento aPattern
 function tokenize(aStr varchar2, aPattern varchar2) return stringList;

 -- Ritorna la stringa da utilizzare in una query dinamica per la data come timestamp o date

 function asDynTimestamp(aTS date) return varchar2;
 function asDynDate(aTS date) return varchar2;

 -- Data una data ritorna 'Y' o 'N' se la data ? un inizio

 FUNCTION isFirstDayOfMonth(aData DATE) RETURN CHAR;

 -- Data una data ritorna la corrispondente data di inizio mese

 FUNCTION getFirstDayOfMonth(aData DATE) RETURN DATE;

 -- Data una data ritorna 'Y' o 'N' se la data ? un fine mese

 FUNCTION isLastDayOfMonth(aData DATE) RETURN CHAR;

 -- Data una data ritorna la corrispondente data di fine mese

 FUNCTION getLastDayOfMonth(aData DATE) RETURN DATE;

 -- Data una data ritorna l'intero rappresentante il primo giorno del mese

 FUNCTION getDayFirstDayOfMonth(aData DATE) RETURN NUMBER;

 -- Data una data ritorna l'intero rappresentante l'ultimo giorno del mese

 FUNCTION getDayLastDayOfMonth(aData DATE) RETURN NUMBER;

-- Data una data ritorna l'intero rappresentante il giorno del mese

 FUNCTION getDayOfDate(aData DATE) RETURN NUMBER;

-- Data una data ritorna l'intero rappresentante il giorno del mese

 FUNCTION getMonthOfDate(aData DATE) RETURN NUMBER;

-- Data una data ritorna l'intero rappresentante l'anno

 FUNCTION getYearOfDate(aData DATE) RETURN NUMBER;

 -- Ritorna 'Y' o 'N' se la distanza tra due date rappresenta un intervallo mensile.

 FUNCTION isDifferenzaMensile(aDataDa DATE, aDataA DATE) RETURN CHAR;

 -- Ritorna 'Y' o 'N' se la distanza tra due date rappresenta un intervallo mensile.

 FUNCTION isDifferenzaMese(aDataDa DATE, aDataA DATE) RETURN CHAR;

 -- Ritorna la data sommata o sottratta di un numero di mesi

 FUNCTION getAddMonth(aData DATE, aNumero INTEGER) RETURN DATE;

 -- Ritorna i mesi di differenza tra due date.

 FUNCTION getMonthsBetween (aDataDa DATE, aDataA DATE) RETURN NUMBER;

 -- Ritorna i giorni di differenza tra due date.

 FUNCTION getDaysBetween (aDataDa DATE, aDataA DATE) RETURN NUMBER;

 -- Ritorna i giorni di differenza tra due date (anno commerciale).

 FUNCTION getDaysCommBetween (aDataDa DATE, aDataA DATE) RETURN NUMBER;

-- per convertire un numero in lettere nella parte intera e
-- concatenare la parte decimale in cifre
FUNCTION inlettere(valore NUMBER) RETURN VARCHAR2;

-- per convertire un NUMBER in VARCHAR2 con il formato specificato
FUNCTION to_varchar(valore NUMBER) RETURN VARCHAR2;

-- per convertire un intero in lettere
FUNCTION converti(numero VARCHAR2) RETURN VARCHAR2;
--
-- per convertire in lettere i token di 3 elementi
FUNCTION parse3Digit(token VARCHAR2,posizione INT) RETURN VARCHAR2;
--
-- per suddividere l'intero in tokens di 3 elementi
FUNCTION spezzaGruppiDi3(numero VARCHAR2) RETURN V_ARR;
--
-- Ritorna la data di refresh del DB
Function getDBRefreshDate RETURN DATE;

end;


CREATE OR REPLACE package body IBMUTL001 is

-- look-up-tables:
   lut1 V_ARR:= V_ARR('','uno','due','tre','quattro','cinque','sei','sette','otto','nove','dieci','undici',
   			   'dodici','tredici','quattordici','quindici','sedici','diciassette','diciotto','diciannove');
   lut2p1 V_ARR:= V_ARR(NULL,'vent','trent','quarant','cinquant','sessant','settant','ottant','novant');
   lut2p2 V_ARR:= V_ARR(NULL,'venti','trenta','quaranta','cinquanta','sessanta','settanta','ottanta','novanta');
   lut3 V_ARR:= V_ARR('cento','mila','milioni','miliardi','bilioni','trilioni','quadrilioni');
   lut4 V_ARR:= V_ARR(NULL,'mille','unmilione','unmiliardo','unbilione','untrilione','unquadrilione');

 PROCEDURE DEFERRED_COMMIT IS
  aSID varchar2(50);
 BEGIN
  UNLOCK_TRANSACTION;
  commit;
 END;

 procedure trace_user_connection(aUser varchar2, aTSNow date, aHTTPSID varchar2) is
 begin
  DBMS_APPLICATION_INFO.SET_CLIENT_INFO(rpad(aUser,20,' ')|| to_char(aTSNow,'YYYY-MM-DD HH:MI:SS')||aHTTPSID);
 end;

 PROCEDURE UNLOCK_TRANSACTION IS
  aSID varchar2(50);
 BEGIN
  aSID:=DBMS_SESSION.UNIQUE_SESSION_ID;
  delete from TRANSLOCK where
     sid = aSID;
 END;

 procedure LOCK_TRANSACTION is
 begin
  insert into TRANSLOCK (SID,STATUS) VALUES (DBMS_SESSION.UNIQUE_SESSION_ID,0);
 end;

FUNCTION to_varchar(valore NUMBER) RETURN VARCHAR2 IS
   BEGIN
   		RETURN TO_CHAR(valore,'FM999999999999999999990.00');
   END to_varchar;

FUNCTION inlettere(valore NUMBER) RETURN VARCHAR2 IS
   stringa VARCHAR2(400) := '';
   intero VARCHAR2(25);
   decimale VARCHAR2(4);
   numero VARCHAR2(25);
   BEGIN
   		numero := to_varchar(abs(valore));
   		intero := SUBSTR(numero,1,INSTR(numero,'.')-1);
		decimale := SUBSTR(numero,INSTR(numero,'.')+1,2);
		IF LENGTH(intero) > 21 THEN
		   IBMERR001.RAISE_ERR_GENERICO('Numero troppo grande - non gestito');
		END IF;
		IF intero = '0' THEN
		    stringa := 'zero/'||decimale;
		ELSE
			stringa := converti(intero)||'/'||decimale;
		END IF;
		RETURN stringa;
   END inlettere;

FUNCTION converti(numero VARCHAR2) RETURN VARCHAR2 IS
   aColl V_ARR := V_ARR();
   token VARCHAR2(3);
   pToken VARCHAR2(100);
   pStringa VARCHAR2(400) := '';
   BEGIN
   		aColl := spezzaGruppiDi3(numero);
		FOR i IN 1..aColl.LAST LOOP
			token := aColl(i);
			pToken := parse3Digit(token,aColl.LAST-i+1);
			pStringa := pStringa||pToken;
		END LOOP;
		RETURN pStringa;
   END converti;

FUNCTION parse3Digit(token VARCHAR2,posizione INT) RETURN VARCHAR2 IS
   pToken VARCHAR2(100) := '';
   hd INTEGER;
   tlh INTEGER;
   tlt INTEGER;
   tail INTEGER;
   BEGIN
   		IF token = '000' THEN RETURN ''; END IF;
		IF token = '001' AND posizione!= 1 THEN
		   pToken := lut4(posizione);
		   RETURN pToken;
		END IF;
		hd := TO_NUMBER(SUBSTR(token,1,1));
		tail := TO_NUMBER(SUBSTR(token,2,2));
		IF hd != 0 THEN
		   IF hd != 1 THEN pToken:=pToken||lut1(hd+1); END IF;
		   pToken:=pToken||lut3(1);
		END IF;
		IF tail<20 THEN
		   pToken := pToken||lut1(tail+1);
		ELSE
			tlh := TO_NUMBER(SUBSTR(token,2,1));
			tlt := TO_NUMBER(SUBSTR(token,3,1));
			IF tlt=1 OR tlt=8 THEN
			   pToken:=pToken||lut2p1(tlh);
			ELSE
				pToken:=pToken||lut2p2(tlh);
			END IF;
			pToken:=pToken||lut1(tlt+1);
		END IF;
		IF posizione > 1 THEN
		   pToken := pToken||lut3(posizione);
		END IF;
   		RETURN pToken;
   END parse3Digit;

FUNCTION spezzaGruppiDi3(numero VARCHAR2) RETURN V_ARR IS
   aColl V_ARR := V_ARR();
   m INT; --numero cifre del numero
   tot INT := 1; -- numero gruppi di 3
   resto INT;
   BEGIN
   		m := LENGTH(numero);
		resto := MOD(m,3);
		IF resto = 0 THEN tot := m/3;
		ELSE tot := TRUNC(m/3)+1;
		END IF;
		aColl.EXTEND(tot);
   		FOR i IN 1..tot LOOP
			  IF i = 1 THEN
			  	 IF resto = 0 THEN
				 	aColl(i) := SUBSTR(numero,1,3);
				 ELSE
				 	 aColl(i) := LPAD(SUBSTR(numero,1,resto),3,0);
				 END IF;
			  ELSE
			  	  IF resto = 0 THEN
				  	 aColl(i) := SUBSTR(numero,(i-1)*3+1,3);
				  ELSE
				  	 aColl(i) := SUBSTR(numero,resto+(i-2)*3+1,3);
				  END IF;
			  END IF;
		END LOOP;
		RETURN aColl;
   END spezzaGruppiDi3;

 function asDynTimestamp(aTS date) return varchar2 is
 begin
  return 'to_date('''||to_char(aTS,'YYYYMMDD HH24:MI:SS')||''',''YYYYMMDD HH24:MI:SS'')';
 end;

 function asDynDate(aTS date) return varchar2 is
 begin
  return 'to_date('''||to_char(TRUNC(aTS),'YYYYMMDD')||''',''YYYYMMDD'')';
 end;

 function strip(aStr varchar2, aPattern varchar2) return varchar2 is
  aRis varchar2(4000);
  aStringList stringList;
 begin
  aRis:='';
  aStringList:=IBMUTL001.TOKENIZE(aStr,aPattern);
  for aIndex in 1 .. aStringList.count loop
   aRis:=aRis||aStringList(aIndex);
  end loop;
  return aRis;
 end;

 function replaceToken(aStr varchar2, aWhat varchar2, aWith varchar2) return varchar2 is
  aRis varchar2(4000);
  aStringList stringList;
 begin
  aRis:='';
  aStringList:=IBMUTL001.TOKENIZE(aStr,aWhat);
  aRis:=aStringList(1);
  for aIndex in 2 .. aStringList.count loop
   aRis:=aRis||aWith||aStringList(aIndex);
  end loop;
  return aRis;
 end;

 function getErrorMessage Return varchar2 is
  aStrList stringList;
 Begin
  aStrList := IBMUTL001.TOKENIZE(DBMS_UTILITY.FORMAT_ERROR_STACK,chr(10));
  return Substr(aStrList(1),11);
 End;

 function tokenize(aStr varchar2, aPattern varchar2) return stringList is
  aStrList stringList;
  aIndex number;
  aPos number;
  aResiduo varchar2(4000);
 begin
  aPos:=1;
  aResiduo:=aStr;
  aIndex:=instr(aResiduo,aPattern);
  while aIndex != 0 loop
   aStrList(aPos):=substr(aResiduo,1,aIndex-1);
   aResiduo:=substr(aResiduo,aIndex+length(aPattern),length(aResiduo)-aIndex);
   aIndex:=instr(aResiduo,aPattern);
   aPos:=aPos+1;
  end loop;
  aStrList(aPos):=aResiduo;
  return aStrList;
 end;

 function leftOfFirst(aStr varchar2, aPattern varchar2) return varchar2 is
  aIndex number;
 begin
  aIndex:=instr(aStr,aPattern);
  if aIndex = 0 then
   return '';
  end if;
  return substr(aStr,1,aIndex-1);
 end;

 function leftOfLast(aStr varchar2, aPattern varchar2) return varchar2 is
  aIndex number;
 begin
  aIndex:=instr(aStr,aPattern,-1);
  if aIndex = 0 then
   return '';
  end if;
  return substr(aStr,1,aIndex-1);
 end;

 function rightOfLast(aStr varchar2, aPattern varchar2) return varchar2 is
  aIndex number;
 begin
  aIndex:=instr(aStr,aPattern,-1);
  if aIndex = 0 then
   return '';
  end if;
  return substr(aStr,aIndex + length(aPattern),length(aStr) - (aIndex + length(aPattern)) + 1);
 end;

 function rightOfFirst(aStr varchar2, aPattern varchar2) return varchar2 is
  aIndex number;
 begin
  aIndex:=instr(aStr,aPattern);
  if aIndex = 0 then
   return '';
  end if;
  return substr(aStr,aIndex + length(aPattern),length(aStr) - (aIndex + length(aPattern)) + 1);
 end;

 function dotConcat(aS1 varchar2, aS2 varchar2) return varchar2 is
 begin
  return aS1||'.'||aS2;
 end;

 function contaOccorrenze(aOrigine varchar2, aPattern varchar2) return NUMBER is
  aS1 varchar2(4000);
  aNum NUMBER;
  aPos NUMBER;
 begin
  aPos:=instr(aOrigine,aPattern);
  if aPos =0 then return 0; end if;
  aS1:=substr(aOrigine,aPos+length(aPattern),length(aOrigine));
  aNum:=0;
  aNum:=aNum+1;
  aNum:=aNum+contaOccorrenze(aS1,aPattern);
--  dbms_output.put_line(aS1);
  return aNum;
 end;

 function isNumeric(aStr varchar2) return char is
 begin
  for aIndex in 1 .. length(aStr) loop
   if not (substr(aStr,aIndex,1) in ('0','1','2','3','4','5','6','7','8','9')) then
    return'N';
   end if;
  end loop;
  return 'Y';
 end;

 -- Restituisce il LOCAL_TRANSACTION_ID
 function getLocalTransactionID(createTransaction BOOLEAN) return VARCHAR2 is
 errMsg varchar2(200);
 id_transazione varchar2(200);
 begin
 	  id_transazione := dbms_transaction.local_transaction_id(createTransaction);
	  return id_transazione;
 exception
     when others then
          errMsg := substr(SQLERRM, 1, 200);
          IBMERR001.RAISE_ERR_GENERICO(SQLCODE ||' -  ' || errMsg);
          RETURN '0';
 end;

-- =================================================================================================
-- Ritorna 'Y' o 'N' se la data ? un inizio mese
-- =================================================================================================
FUNCTION isFirstDayOfMonth
   (aData DATE)
   RETURN CHAR IS

BEGIN

   IF TO_CHAR(aData,'DD') = '01' THEN
      RETURN 'Y';
   ELSE
      RETURN 'N';
   END IF;

END isFirstDayOfMonth;

-- =================================================================================================
-- Ritorna la data di inizio mese
-- =================================================================================================
FUNCTION getFirstDayOfMonth
   (aData DATE)
   RETURN DATE IS

BEGIN

   RETURN TO_DATE('01' || TO_CHAR(aData,'MMYYYY'),'DDMMYYYY');

END getFirstDayOfMonth;

-- =================================================================================================
-- Ritorna 'Y' o 'N' se la data ? un fine mese
-- =================================================================================================
FUNCTION isLastDayOfMonth
   (aData DATE)
   RETURN CHAR IS

BEGIN

   IF aData = LAST_DAY(aData) THEN
      RETURN 'Y';
   ELSE
      RETURN 'N';
   END IF;

END isLastDayOfMonth;

-- =================================================================================================
-- Ritorna la data di fine mese
-- =================================================================================================
FUNCTION getLastDayOfMonth
   (aData DATE)
   RETURN DATE IS

BEGIN

   RETURN LAST_DAY(aData);

END getLastDayOfMonth;

-- =================================================================================================
-- Data una data ritorna l'intero rappresentante il primo giorno del mese
-- =================================================================================================
FUNCTION getDayFirstDayOfMonth
   (
    aData DATE
   ) RETURN NUMBER IS

BEGIN

   RETURN TO_NUMBER(TO_CHAR(getFirstDayOfMonth(aData),'DD'));

END getDayFirstDayOfMonth;

-- =================================================================================================
-- Data una data ritorna l'intero rappresentante l'ultimo giorno del mese
-- =================================================================================================
FUNCTION getDayLastDayOfMonth
   (
    aData DATE
   ) RETURN NUMBER IS

BEGIN

   RETURN TO_NUMBER(TO_CHAR(getLastDayOfMonth(aData),'DD'));

END getDayLastDayOfMonth;

-- =================================================================================================
-- Data una data ritorna l'intero rappresentante il giorno del mese
-- =================================================================================================
FUNCTION getDayOfDate
   (
    aData DATE
   ) RETURN NUMBER IS

BEGIN

   RETURN TO_NUMBER(TO_CHAR(aData,'DD'));

END getDayOfDate;

-- =================================================================================================
-- Data una data ritorna l'intero rappresentante il giorno del mese
-- =================================================================================================
FUNCTION getMonthOfDate
   (
    aData DATE
   ) RETURN NUMBER IS

BEGIN

   RETURN TO_NUMBER(TO_CHAR(aData,'MM'));

END getMonthOfDate;
-- =================================================================================================
-- Data una data ritorna l'intero rappresentante l'anno
-- =================================================================================================
FUNCTION getYearOfDate
   (
    aData DATE
   ) RETURN NUMBER IS

BEGIN

   RETURN TO_NUMBER(TO_CHAR(aData,'YYYY'));

END getYearOfDate;

-- =================================================================================================
-- Ritorna 'Y' o 'N' se la distanza tra due date rappresenta intervalli mensili.
-- Si comprendono i seguenti casi
-- 1) le due date in confronto sono rispettivamente una data di fine mese e una di inizio mese
-- 2) le due date in confronto hanno tra loro la differenza di un giorno (da > a) tenuto conto del
--    fatto che i mesi non sono omogenei nel numero di giorni
-- =================================================================================================
FUNCTION isDifferenzaMensile
   (
    aDataDa DATE,
    aDataA DATE
   ) RETURN CHAR IS
   aGGDataDa NUMBER;
   aGGDataA NUMBER;
   aGGFineMeseDataDa NUMBER;
   aGGFineMeseDataA NUMBER;

BEGIN

   -- Le date DA e A sono rispettivamente un inizio e una fine mese. Si torna che questo ? un
   -- intervallo mensile

   IF (isFirstDayOfMonth(aDataDa) = 'Y' AND
       isLastDayOfMonth(aDataA) = 'Y') THEN
      RETURN isIntervalloDateMensile;
   END IF;

   -- Estraggo, sia per DA che per A, rispettivamente il giorno proprio e quello di fine mese

   aGGDataDa:=getDayOfDate(aDataDa);
   aGGFineMeseDataDa:=getDayLastDayOfMonth(aDataDa);
   aGGDataA:=getDayOfDate(aDataA);
   aGGFineMeseDataA:=getDayLastDayOfMonth(aDataA);

   -- Le due date sono omogenee per data di fine mese vale la regola ordinaria della distanza di un giorno
   -- tra le due date (da > a)

   IF aGGFineMeseDataDa = aGGFineMeseDataA THEN
      IF aGGDataDa = (aGGDataA + 1) THEN
         RETURN isIntervalloDateMensile;
      ELSE
         RETURN isNotIntervalloDateMensile;
      END IF;
   END IF;

   -- Le due date non sono omogenee per data di fine mese. Si fanno i seguenti casi:
   -- 1) La data di fine del mese DA ? maggiore di quella del mese A. In questo caso:
   --    Se il giorno del mese DA ? < o = a quello di fine del mese A vale la regola ordinaria della distanza di
   --    un giorno tra le due date (da > a)
   --    Se il giorno del mese DA ? > di quello di fine del mese A si accetta solo il fine mese di A
   -- 2) La data di fine del mese DA ? < di quella del mese A. In questo caso:
   --    Vale sempre la regola ordinaria della distanza di un giorno tra le due date (da > a)

   IF aGGFineMeseDataDa > aGGFineMeseDataA THEN
      IF aGGDataDa <= aGGFineMeseDataA THEN
         IF aGGDataDa = (aGGDataA + 1) THEN
            RETURN isIntervalloDateMensile;
         END IF;
      ELSE
         IF aGGDataA = aGGFineMeseDataA THEN
            RETURN isIntervalloDateMensile;
         END IF;
      END IF;
   ELSE
      IF aGGDataDa = aGGDataA + 1 THEN
         RETURN isIntervalloDateMensile;
      END IF;
   END IF;

   RETURN isNotIntervalloDateMensile;

END isDifferenzaMensile;

-- =================================================================================================
-- Ritorna 'Y' o 'N' se la distanza tra due date rappresenta un mese un mese intero.
-- Si corregge il calcolo matematico e si comprendono i seguenti casi
-- 1) le due date in confronto sono rispettivamente una data di fine mese e una di inizio mese
-- 2) le due date in confronto hanno tra loro la differenza di un giorno (da > a) tenuto conto del
--    fatto che i mesi non sono omogenei nel numero di giorni
-- =================================================================================================
FUNCTION isDifferenzaMese
   (
    aDataDa DATE,
    aDataA DATE
   ) RETURN CHAR IS

BEGIN

   -- Le date DA e A sono rispettivamente un inizio e una fine mese. Si torna che questo ? un
   -- intervallo mese

   IF (isFirstDayOfMonth(aDataDa) = 'Y' AND
       isLastDayOfMonth(aDataA) = 'Y') THEN
      RETURN isIntervalloDateMese;
   END IF;

   RETURN isNotIntervalloDateMese;

END isDifferenzaMese;


-- =================================================================================================
-- Ritorna la data di refresh del DB
-- =================================================================================================
FUNCTION getDBRefreshDate
   RETURN DATE IS
  DateRefresh  DATE;
  CURSOR_NAME  INTEGER;
  dummy        INTEGER;
  Stringa      VARCHAR2(200);
BEGIN
   Stringa := 'Select Nvl(RESETLOGS_TIME,Sysdate) From database_info';
   CURSOR_NAME:=DBMS_SQL.OPEN_CURSOR;
   Dbms_Sql.PARSE(CURSOR_NAME, Stringa, DBMS_SQL.V7);
   Dbms_Sql.DEFINE_COLUMN(cursor_name,1,DateRefresh);
   dummy := DBMS_SQL.execute_and_fetch(cursor_name);
   Dbms_Sql.column_value(cursor_name,1,DateRefresh);
   Dbms_Sql.CLOSE_CURSOR(CURSOR_NAME);

   Return DateRefresh;
Exception
  When Others Then
    Return Sysdate;
END getDBRefreshDate;
-- =================================================================================================
-- Ritorna la data sommata o sottratta di un numero di mesi
-- =================================================================================================
FUNCTION getAddMonth
   (aData DATE,
    aNumero INTEGER)
   RETURN DATE IS

BEGIN

   RETURN ADD_MONTHS(aData, aNumero);

END getAddMonth;

-- =================================================================================================
-- Ritorna i mesi di differenza tra due date.
-- Si corregge la gestione matematica delle date e si forza la restituzione di un intero quando:
-- 1) le due date in confronto sono rispettivamente una data di fine mese e una di inizio mese
-- 2) le due date in confronto rappresentano un intervallo mensile
-- Negli altri casi si forza il ritorno di un numero decimale.
-- =================================================================================================
FUNCTION getMonthsBetween
   (
    aDataDa DATE,
    aDataA DATE
   ) RETURN NUMBER IS

BEGIN

   -- L'intervallo date rappresenta un mese intero

   IF isIntervalloDateMese = isDifferenzaMese(aDataDa, aDataA) THEN
      RETURN CEIL(MONTHS_BETWEEN(aDataA, aDataDa));
   END IF;

   IF isIntervalloDateMensile = isDifferenzaMensile(aDataDa, aDataA) THEN
      RETURN CEIL(MONTHS_BETWEEN(getFirstDayOfMonth(aDataA), (getFirstDayOfMonth(aDataDa) - 1)) -1);
   END IF;

   RETURN MONTHS_BETWEEN(aDataA, aDataDa) + 0.99;

END getMonthsBetween;

-- =================================================================================================
-- Ritorna i giorni di differenza tra due date.
-- =================================================================================================
FUNCTION getDaysBetween
   (aDataDa DATE, aDataA DATE)
   RETURN NUMBER IS

BEGIN

    RETURN aDataA - aDataDa + 1;

END getDaysBetween;

-- =================================================================================================
-- Ritorna i giorni di differenza tra due date  (anno commerciale).
-- =================================================================================================
FUNCTION getDaysCommBetween
   (
    aDataDa DATE,
    aDataA DATE
   ) RETURN NUMBER IS
   aNumero NUMBER;
   aGGDa NUMBER;
   aGGA NUMBER;

BEGIN

   -- L'intervallo date rappresenta un mese intero o un intervallo mensile

   IF (isDifferenzaMese(aDataDa, aDataA) = isIntervalloDateMese OR
       isDifferenzaMensile(aDataDa, aDataA) = isIntervalloDateMensile) THEN
      aNumero:=(getMonthsBetween(aDataDa, aDataA) * 30);

   -- L'intervallo date non rappresenta un mese intero o un intervallo mensile. Se aGGDa = 15 vale 16,
   -- se aGGA = 14 vale 15.

   ELSE
      aNumero:=(getMonthsBetween((getLastDayOfMonth(aDataDa) + 1), (getFirstDayOfMonth(aDataA) - 1)) * 30);

      aGGDa:=getDayOfDate(aDataDa);
      IF aGGDa > 30 THEN
         aGGDa:=30;
      END IF;
      aGGA:=getDayOfDate(aDataA);
      IF aGGA > 30 THEN
         aGGA:=30;
      END IF;

      -- Normalizzazione del giorno per gli esempi forniti su minicarriere

      IF (aGGDa = 15 AND
          aGGA = 30) THEN
         aGGDa:=16;
      END IF;

      IF (aGGA = 14 AND
          aGGDa = 1) THEN
         aGGA:=15;
      END IF;

      aNumero:=aNumero + (30 - aGGDa + 1) + aGGA;
   END IF;

   RETURN aNumero;

END getDaysCommBetween;

END;


