CREATE OR REPLACE Procedure PRT_S_CE_RICLASSIFICATO_JLELLO
--
-- Date: 06/08/2004
-- Version: 1.2
--
-- Vista di stampa Conto Economico Riclassificato
--
-- History:
--
-- Date: 28/07/2004
-- Version: 1.0
-- Creazione
--
-- Date: 06/08/2004
-- Version: 1.1
-- Modificati il nome della view e della procedure, eliminato il nome dello
-- schema, aggiunto history nella procedure
--
-- Date: 23/09/2005
-- Version: 1.2
-- Aggiunto HINT RULE alle select perch? si affossava
--
-- Body
--
(IST_COMM       IN VARCHAR2,
 inEs           in number,
 CDS            in varchar2,
 uo             in VARCHAR2,
 conti_sn       In CHAR
) is
 aId    number;
 i      number;
 INDICE NUMBER := 0;
 aNum1  number;
 LIV1   VARCHAR2(10);
 LIV2   VARCHAR2(10);
 LIV3   VARCHAR2(10);
 LIV4   VARCHAR2(10);
 PARZ1  NUMBER(20,3);
 TOT1   NUMBER(20,3);
 PARZ2  NUMBER(20,3);
 TOT2   NUMBER(20,3);
 TOT_INCR1  NUMBER(20,3);
 TOT_INCR2  NUMBER(20,3);
 DARE1  NUMBER(20,3);
 AVERE1 NUMBER(20,3);
 DARE_CONTO  NUMBER(20,3);
 AVERE_CONTO NUMBER(20,3);
 M1     NUMBER;
 M2     NUMBER;
 M3     NUMBER;
 M4     NUMBER;
 FLAG_TOT VARCHAR2(1);
 CHIUS_COEP CHIUSURA_COEP%Rowtype;
 aConto voce_ep%Rowtype;

CURSOR SCHEMA_CE_RICLASSIFICATO IS
 SELECT *
 FROM   CNR_GRUPPO_EP
 WHERE  CD_PIANO_GRUPPI = 'CE'
 ORDER BY SEQUENZA;

CURSOR CONTI_ASSOCIATI (CONTO_RICLASSIFICATO IN VARCHAR, ANNO_COMP NUMBER) IS
 SELECT ESERCIZIO, CD_VOCE_EP, SEZIONE
 FROM   CNR_ASS_CONTO_GRUPPO_EP
 WHERE  CD_PIANO_GRUPPI = 'CE' AND
        ESERCIZIO = ANNO_COMP AND
        CD_GRUPPO_EP = CONTO_RICLASSIFICATO;

 CONTI_ASS       CONTI_ASSOCIATI%ROWTYPE;
 SCHEMA_CE       SCHEMA_CE_RICLASSIFICATO%ROWTYPE;

 CONTO_AVANZO   VOCE_EP.CD_VOCE_EP%TYPE;
 PROF_PERD      VOCE_EP.CD_VOCE_EP%TYPE;

begin

 select IBMSEQ00_CR_PACKAGE.nextval
 into aId
 from dual;

 SELECT VAL01
 INTO   CONTO_AVANZO
 FROM   CONFIGURAZIONE_CNR
 WHERE  ESERCIZIO = INES AND
        CD_UNITA_FUNZIONALE = '*' AND
        CD_CHIAVE_PRIMARIA   = 'VOCEEP_SPECIALE' AND
        CD_CHIAVE_SECONDARIA = 'UTILE_PERDITA_ESERCIZIO';

 SELECT VAL01
 INTO   PROF_PERD
 FROM   CONFIGURAZIONE_CNR
 WHERE  ESERCIZIO = INES AND
        CD_UNITA_FUNZIONALE = '*' AND
        CD_CHIAVE_PRIMARIA   = 'VOCEEP_SPECIALE' AND
        CD_CHIAVE_SECONDARIA = 'CONTO_ECONOMICO';

 i := 0;

OPEN SCHEMA_CE_RICLASSIFICATO;
Loop -- SCHEMA_CE_RICLASSIFICATO

  FETCH SCHEMA_CE_RICLASSIFICATO INTO SCHEMA_CE;
  EXIT WHEN SCHEMA_CE_RICLASSIFICATO%NOTFOUND;

  PARZ1 := NULL;
  TOT1  := NULL;
  PARZ2 := NULL;
  TOT2  := NULL;

  FLAG_TOT := 'N';

  IF SCHEMA_CE.CD_GRUPPO_PADRE IS NULL THEN
    LIV1 := SCHEMA_CE.NOME;
    LIV2 := NULL;
    LIV3 := NULL;
  ELSIF LOWER(SUBSTR(SCHEMA_CE.NOME,1,1)) BETWEEN 'a' AND 'z' THEN
    LIV1 := NULL;
    LIV2 := NULL;
    LIV3 := SCHEMA_CE.NOME;
  ELSE
    LIV1 := NULL;
    LIV2 := SCHEMA_CE.NOME;
    LIV3 := NULL;
  END IF;

--- GIRO SUI CONTI ASSOCIATI AL CONTO RICLASSIFICATO SOLO PER I MASTRI

IF SCHEMA_CE.FL_MASTRINO = 'Y' THEN

   OPEN CONTI_ASSOCIATI (SCHEMA_CE.CD_GRUPPO_EP, INES);
   Loop -- CONTI_ASSOCIATI

     Fetch CONTI_ASSOCIATI Into CONTI_ASS;
     Exit When CONTI_ASSOCIATI%Notfound;

aConto := cnrctb002.getVoceEp(CONTI_ASS.Esercizio, CONTI_ASS.Cd_Voce_ep);

---------------------------------------------------------------------------------
------------ INIZIO GIRO SULLE CIFRE DELL'ESERCIZIO IN GESTIONE -----------------
---------------------------------------------------------------------------------

Dbms_Output.PUT_LINE ('CONTI_ASS '||CONTI_ASS.CD_VOCE_EP);

-- nuovo cursore sui CDS validi

DARE1 := 0;
AVERE1 := 0;

/* PER EVITARE DI FARE IL LOOP QUANDO NON SERVE */

Declare
   CONTA_CDS_VALIDI     NUMBER;
   CONTA_CHIUSI         NUMBER;
Begin
   Select Count(*)
   Into   CONTA_CDS_VALIDI
   From   v_unita_organizzativa_valida
   Where  esercizio = INES And
          fl_cds = 'Y';

   Select Count(*)
   Into CONTA_CHIUSI
   From chiusura_coep
   Where esercizio = INES And
         STATO In ('P', 'C');

        Dbms_Output.PUT_LINE ('CONTA_CDS_VALIDI '||CONTA_CDS_VALIDI||' CONTA_CHIUSI ');

   If CONTA_CDS_VALIDI = CONTA_CHIUSI And CDS = '*' And UO = '*' Then

        --- NON SERVE CICLARE IL LOOP, FACCIO UNA SELECT SOLA

       IF CONTI_ASS.CD_VOCE_EP = CONTO_AVANZO THEN

           FLAG_TOT := 'S';

            Dbms_Output.PUT_LINE ('SELECT SU CONTO '||CONTI_ASS.CD_VOCE_EP);

                SELECT Nvl(sum(DECODE(D.SEZIONE, 'D', D.im_movimento)), 0),
                       Nvl(sum(DECODE(D.SEZIONE, 'A', D.im_movimento)), 0)
                INTO   DARE_CONTO, AVERE_CONTO
                FROM   MOVIMENTO_COGE D, SCRITTURA_PARTITA_DOPPIA T
                WHERE  T.CD_CDS  = D.CD_CDS   AND
                       T.ESERCIZIO = D.ESERCIZIO AND
                       T.CD_UNITA_ORGANIZZATIVA = D.CD_UNITA_ORGANIZZATIVA and
                       T.PG_SCRITTURA = D.PG_SCRITTURA AND
                       T.ATTIVA = 'Y' AND
                     ((T.CD_CAUSALE_COGE IS NULL) OR
                      (T.CD_CAUSALE_COGE = 'DETERMINAZIONE_UTILE_PERDITA' AND CONTI_ASS.CD_VOCE_EP = CONTO_AVANZO)) AND
                       T.ESERCIZIO  = INES                 AND
                       D.CD_VOCE_EP = CONTI_ASS.CD_VOCE_EP AND
        	      (Nvl(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES) OR
         	       NVL(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES)) AND
                       D.TI_ISTITUZ_COMMERC = Decode(IST_COMM, '*', D.TI_ISTITUZ_COMMERC, IST_COMM)
                       AND EXISTS
                        (SELECT 1
                         FROM MOVIMENTO_COGE M2
                         WHERE M2.CD_CDS                  = D.CD_CDS                 AND
                               M2.ESERCIZIO               = D.ESERCIZIO              AND
                               M2.CD_UNITA_ORGANIZZATIVA  = D.CD_UNITA_ORGANIZZATIVA AND
                               M2.PG_SCRITTURA            = D.PG_SCRITTURA AND
                               M2.cd_voce_ep = PROF_PERD);

            If conti_sn = 'Y' Then
               I := I + 1;
               -- inserimento dello schema di riclassificazione NELLA VIEW (FISSO)
               insert into PRT_VPG_BIL_RICLASSIFICATO (ID, CHIAVE, TIPO, SEQUENZA,
                       ORDINE, CONTO_RICLASS, I_LIVELLO, II_LIVELLO, III_LIVELLO, IV_LIVELLO,
                       DESCRIZIONE, PARZIALE_I_ANNO, TOTALE_I_ANNO, PARZIALE_II_ANNO, TOTALE_II_ANNO,
                       SN_TOTALE)
               VALUES (aId, 'chiave', 't', i,
                       SCHEMA_CE.SEQUENZA, Null, Null, Null, Null, NULL,
                       CONTI_ASS.CD_VOCE_EP||' '||aConto.ds_voce_ep,
                       Decode(CONTI_ASS.SEZIONE, 'D', DARE_CONTO-AVERE_CONTO, 'A', AVERE_CONTO-DARE_CONTO),
                       Null, Null, Null, 'N');
            End If;

            DARE1 := DARE1 + DARE_CONTO;
            AVERE1 := AVERE1 + AVERE_CONTO;

       ELSE

            Dbms_Output.PUT_LINE ('SELECT SU CONTO NON AVANZO '||CONTI_ASS.CD_VOCE_EP);

            -- PER CONTI NORMALI

                SELECT Nvl(sum(DECODE(D.SEZIONE, 'D', D.im_movimento)), 0),
                       Nvl(sum(DECODE(D.SEZIONE, 'A', D.im_movimento)), 0)
                INTO   DARE_CONTO, AVERE_CONTO
                FROM   MOVIMENTO_COGE D, SCRITTURA_PARTITA_DOPPIA T
                WHERE  T.CD_CDS  = D.CD_CDS   AND
                       T.ESERCIZIO = D.ESERCIZIO AND
                       T.CD_UNITA_ORGANIZZATIVA = D.CD_UNITA_ORGANIZZATIVA and
                       T.PG_SCRITTURA = D.PG_SCRITTURA AND
                       T.ATTIVA = 'Y' AND
                      ((T.CD_CAUSALE_COGE IS NULL) OR
                      (T.CD_CAUSALE_COGE != 'CHIUSURA_CONTO_ECONOMICO' AND
                       T.CD_CAUSALE_COGE != 'CHIUSURA_STATO_PATRIMONIALE' AND
                       T.CD_CAUSALE_COGE != 'DETERMINAZIONE_UTILE_PERDITA')) AND
                       T.ESERCIZIO  = INES                 AND
                       D.CD_VOCE_EP = CONTI_ASS.CD_VOCE_EP AND
                -- ANNO IN CORSO (SOLA COMPETENZA)
                ((Nvl(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES) And Nvl(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES)) Or
                -- A CAVALLO TRA ANNO PRECEDENTE ED ANNO ATTUALE
                 (Nvl(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES-1) And Nvl(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES)) Or
                -- A CAVALLO TRA ANNO ATTUALE ED ANNO SUCCESSIVO (TANTO CI SONO I RISCONTI)
                 (Nvl(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES) And Nvl(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES+1)) Or
                -- O COMPLETAMENTE IN ESERCIZIO SUCCESSIVO (TANTO CI SONO I RISCONTI)
                 (Nvl(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES+1) And Nvl(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES+1)) ) And
                               D.TI_ISTITUZ_COMMERC = Decode(IST_COMM, '*', D.TI_ISTITUZ_COMMERC, IST_COMM);

            If conti_sn = 'Y' Then
               I := I + 1;
               -- inserimento dello schema di riclassificazione NELLA VIEW (FISSO)
               insert into PRT_VPG_BIL_RICLASSIFICATO (ID, CHIAVE, TIPO, SEQUENZA,
                       ORDINE, CONTO_RICLASS, I_LIVELLO, II_LIVELLO, III_LIVELLO, IV_LIVELLO,
                       DESCRIZIONE, PARZIALE_I_ANNO, TOTALE_I_ANNO, PARZIALE_II_ANNO, TOTALE_II_ANNO,
                       SN_TOTALE)
               VALUES (aId, 'chiave', 't', i,
                       SCHEMA_CE.SEQUENZA, Null, Null, Null, Null, NULL,
                       CONTI_ASS.CD_VOCE_EP||' '||aConto.ds_voce_ep,
                       Decode(CONTI_ASS.SEZIONE, 'D', DARE_CONTO-AVERE_CONTO, 'A', AVERE_CONTO-DARE_CONTO),
                       Null, Null, Null, 'N');
            End If;

           DARE1 := DARE1 + DARE_CONTO;
           AVERE1 := AVERE1 + AVERE_CONTO;

       End If;

   Else
        Declare
            DARE_CDS NUMBER(20,3) := 0;
            AVERE_CDS NUMBER(20,3) := 0;
        Begin
            For aCDS in (select CD_UNITA_ORGANIZZATIVA
        	     from   v_unita_organizzativa_valida
                     Where  esercizio = INES And
                     	    fl_cds = 'Y' And
        		    CD_UNITA_ORGANIZZATIVA = Decode (cds, '*', CD_UNITA_ORGANIZZATIVA, cds)
                     Order by cd_unita_organizzativa) Loop  -- CDS VALIDI

               IF CONTI_ASS.CD_VOCE_EP = CONTO_AVANZO THEN

                  FLAG_TOT := 'S';

                  SELECT DARE_CDS + NVL(sum(DECODE(D.SEZIONE, 'D', D.im_movimento)), 0),
                         AVERE_CDS + NVL(sum(DECODE(D.SEZIONE, 'A', D.im_movimento)), 0)
                  INTO   DARE_CDS, AVERE_CDS
                  FROM   MOVIMENTO_COGE D, SCRITTURA_PARTITA_DOPPIA T
                  WHERE  T.CD_CDS  = D.CD_CDS   AND
                         T.ESERCIZIO = D.ESERCIZIO AND
                         T.CD_UNITA_ORGANIZZATIVA = D.CD_UNITA_ORGANIZZATIVA and
                         T.PG_SCRITTURA = D.PG_SCRITTURA AND
                         T.ATTIVA = 'Y' AND
                       ((T.CD_CAUSALE_COGE IS NULL) OR
                        (T.CD_CAUSALE_COGE = 'DETERMINAZIONE_UTILE_PERDITA' AND CONTI_ASS.CD_VOCE_EP = CONTO_AVANZO)) AND
                         T.CD_CDS     = aCDS.CD_UNITA_ORGANIZZATIVA AND
                         T.ESERCIZIO  = INES                 AND
                         T.CD_UNITA_ORGANIZZATIVA = decode(UO, '*', T.CD_UNITA_ORGANIZZATIVA, UO) AND
                         D.CD_VOCE_EP = CONTI_ASS.CD_VOCE_EP AND
                    (Nvl(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES) OR
                     NVL(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES)) AND
                         D.TI_ISTITUZ_COMMERC = Decode(IST_COMM, '*', D.TI_ISTITUZ_COMMERC, IST_COMM)
                         AND EXISTS
                          (SELECT 1
                           FROM MOVIMENTO_COGE M2
                           WHERE M2.CD_CDS                  = D.CD_CDS                 AND
                                 M2.ESERCIZIO               = D.ESERCIZIO              AND
                                 M2.CD_UNITA_ORGANIZZATIVA  = D.CD_UNITA_ORGANIZZATIVA AND
                                 M2.PG_SCRITTURA            = D.PG_SCRITTURA AND
                                 M2.cd_voce_ep = PROF_PERD);
               ELSE

                  -- PER CONTI NORMALI

                  SELECT DARE_CDS + NVL(sum(DECODE(D.SEZIONE, 'D', D.im_movimento)), 0),
                         AVERE_CDS + NVL(sum(DECODE(D.SEZIONE, 'A', D.im_movimento)), 0)
                  INTO   DARE_CDS, AVERE_CDS
                  FROM   MOVIMENTO_COGE D, SCRITTURA_PARTITA_DOPPIA T
                  WHERE  T.CD_CDS  = D.CD_CDS   AND
                         T.ESERCIZIO = D.ESERCIZIO AND
                         T.CD_UNITA_ORGANIZZATIVA = D.CD_UNITA_ORGANIZZATIVA and
                         T.PG_SCRITTURA = D.PG_SCRITTURA AND
                         T.ATTIVA = 'Y' AND
                        ((T.CD_CAUSALE_COGE IS NULL) OR
                        (T.CD_CAUSALE_COGE != 'CHIUSURA_CONTO_ECONOMICO' AND
                         T.CD_CAUSALE_COGE != 'CHIUSURA_STATO_PATRIMONIALE' AND
                         T.CD_CAUSALE_COGE != 'DETERMINAZIONE_UTILE_PERDITA')) AND
                         T.CD_CDS     = ACDS.CD_UNITA_ORGANIZZATIVA AND
                         T.ESERCIZIO  = INES                 AND
                         T.CD_UNITA_ORGANIZZATIVA = decode(UO, '*', T.CD_UNITA_ORGANIZZATIVA, UO) AND
                         D.CD_VOCE_EP = CONTI_ASS.CD_VOCE_EP AND
                  -- ANNO IN CORSO (SOLA COMPETENZA)
                   ((Nvl(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES) And Nvl(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES)) Or
                  -- A CAVALLO TRA ANNO PRECEDENTE ED ANNO ATTUALE
                   (Nvl(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES-1) And Nvl(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES)) Or
                  -- A CAVALLO TRA ANNO ATTUALE ED ANNO SUCCESSIVO (TANTO CI SONO I RISCONTI)
                   (Nvl(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES) And Nvl(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES+1)) Or
                  -- O COMPLETAMENTE IN ESERCIZIO SUCCESSIVO (TANTO CI SONO I RISCONTI)
                  (Nvl(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES+1) And Nvl(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES+1)) ) And
                       D.TI_ISTITUZ_COMMERC = Decode(IST_COMM, '*', D.TI_ISTITUZ_COMMERC, IST_COMM);

                  -- TOLGO MANUALMENTE LA QUOTA DI RISCONTO SOLO SE NON E' STATA FATTA LA CHIUSURA (CHE LI FA)
                  Begin
                    Select * Into CHIUS_COEP
                    From chiusura_coep
                    Where esercizio = INES And
                          cd_cds = ACDS.CD_UNITA_ORGANIZZATIVA;
                  Exception
                	WHEN No_Data_Found Then
        	           CHIUS_COEP.STATO := Null;
                  End;

                  If CHIUS_COEP.STATO Is Null Or CHIUS_COEP.STATO Not In ('P', 'C') Then
                     SELECT DARE_CDS - NVL(SUM(ROUND(IM_MOVIMENTO*ROUND((D.DT_A_COMPETENZA_COGE-to_date('0101'||TO_CHAR(INES+1),'DDMMYYYY')+1)/(D.DT_A_COMPETENZA_COGE - D.DT_DA_COMPETENZA_COGE+1), 2), 2)), 0),
                            AVERE_CDS - NVL(SUM(ROUND(IM_MOVIMENTO*ROUND((D.DT_A_COMPETENZA_COGE-to_date('0101'||TO_CHAR(INES+1),'DDMMYYYY')+1)/(D.DT_A_COMPETENZA_COGE - D.DT_DA_COMPETENZA_COGE+1), 2), 2)), 0)
                     INTO   DARE_CDS, AVERE_CDS
                     FROM   MOVIMENTO_COGE D, SCRITTURA_PARTITA_DOPPIA T
                     WHERE  T.CD_CDS  = D.CD_CDS   AND
                            T.ESERCIZIO = D.ESERCIZIO AND
                            T.CD_UNITA_ORGANIZZATIVA = D.CD_UNITA_ORGANIZZATIVA and
                            T.PG_SCRITTURA = D.PG_SCRITTURA AND
                            T.ATTIVA = 'Y' AND
                           ((T.CD_CAUSALE_COGE IS NULL) OR
                           (T.CD_CAUSALE_COGE != 'CHIUSURA_CONTO_ECONOMICO' AND
                            T.CD_CAUSALE_COGE != 'CHIUSURA_STATO_PATRIMONIALE' AND
                            T.CD_CAUSALE_COGE != 'DETERMINAZIONE_UTILE_PERDITA')) AND
                            T.CD_CDS     = ACDS.CD_UNITA_ORGANIZZATIVA AND
                            T.ESERCIZIO  = INES                 AND
                            T.CD_UNITA_ORGANIZZATIVA = decode(UO, '*', T.CD_UNITA_ORGANIZZATIVA, UO) AND
                            D.CD_VOCE_EP = CONTI_ASS.CD_VOCE_EP AND
                            NVL(TO_CHAR(D.DT_DA_COMPETENZA_COGE, 'YYYY'), D.ESERCIZIO) = TO_CHAR(INES) AND
                            NVL(TO_CHAR(D.DT_A_COMPETENZA_COGE,  'YYYY'), D.ESERCIZIO) = TO_CHAR(INES+1) AND
                            D.TI_ISTITUZ_COMMERC = Decode(IST_COMM, '*', D.TI_ISTITUZ_COMMERC, IST_COMM);
                  End If; -- DELLA CHIUSURA COEP
               END IF;  -- CONTO AVANZO / CONTO NORMALE
            End Loop;  -- FINE CURSORE SU CDS

            If conti_sn = 'Y' Then
               I := I + 1;
               -- inserimento dello schema di riclassificazione NELLA VIEW (FISSO)
               insert into PRT_VPG_BIL_RICLASSIFICATO (ID, CHIAVE, TIPO, SEQUENZA,
                       ORDINE, CONTO_RICLASS, I_LIVELLO, II_LIVELLO, III_LIVELLO, IV_LIVELLO,
                       DESCRIZIONE, PARZIALE_I_ANNO, TOTALE_I_ANNO, PARZIALE_II_ANNO, TOTALE_II_ANNO,
                       SN_TOTALE)
               VALUES (aId, 'chiave', 't', i,
                       SCHEMA_CE.SEQUENZA, Null, Null, Null, Null, NULL,
                       CONTI_ASS.CD_VOCE_EP||' '||aConto.ds_voce_ep,
                       Decode(CONTI_ASS.SEZIONE, 'D', DARE_CDS-AVERE_CDS, 'A', AVERE_CDS-DARE_CDS),
                       Null, Null, Null, 'N');
            End If;

            DARE1 := DARE1 + DARE_CDS;
            AVERE1 := AVERE1 + AVERE_CDS;
        End;
   End If;
End;

/* FINE PER EVITARE DI FARE IL LOOP QUANDO NON SERVE */

  IF CONTI_ASS.SEZIONE = 'D' THEN
          PARZ1 := NVL(PARZ1, 0) + NVL(DARE1, 0) - NVL(AVERE1, 0);
  ELSIF CONTI_ASS.SEZIONE = 'A' THEN
          PARZ1 := NVL(PARZ1, 0) + NVL(AVERE1, 0) - NVL(DARE1, 0);
  END IF;

  TOT1  := NULL;

End LOOP;  -- CONTI_ASSOCIATI
Close CONTI_ASSOCIATI;


ELSIF SCHEMA_CE.FORMULA IS NOT NULL THEN

   PARZ1 := NULL;
   PARZ2 := NULL;

   TOT1 := NULL;
   TOT2 := NULL;

   FLAG_TOT := 'S';

---------------------------------------------------------------------------------
------------------------ CALCOLO DEI TOTALI CON LA FORMULA ----------------------
---------------------------------------------------------------------------------

   INDICE := 0;

   Loop  -- PER TOTALI

     INDICE := INDICE + 1;

     SELECT INSTR(SCHEMA_CE.FORMULA, '[', 1, INDICE) INTO M1 FROM DUAL;
     SELECT INSTR(SCHEMA_CE.FORMULA, ']', 1, INDICE) INTO M2 FROM DUAL;
     SELECT INSTR(SCHEMA_CE.FORMULA, ',', 1, INDICE) INTO M3 FROM DUAL;
     SELECT INSTR(SCHEMA_CE.FORMULA, '}', 1, INDICE) INTO M4 FROM DUAL;

     IF M1 > 0 THEN
      SELECT NVL(PARZIALE_I_ANNO, 0), NVL(PARZIALE_II_ANNO, 0)
      INTO   TOT_INCR1, TOT_INCR2
      FROM   PRT_VPG_BIL_RICLASSIFICATO
      WHERE  CONTO_RICLASS = SUBSTR(SCHEMA_CE.FORMULA, M3+1, M4-(M3+1));

      IF SUBSTR(SCHEMA_CE.FORMULA, M1+1, M2-(M1+1)) = '+' THEN
            TOT1 :=  NVL(TOT1, 0) + TOT_INCR1;
            TOT2 :=  NVL(TOT2, 0) + TOT_INCR2;
      ELSIF SUBSTR(SCHEMA_CE.FORMULA, M1+1, M2-(M1+1)) = '-' THEN
            TOT1 :=  NVL(TOT1, 0) - TOT_INCR1;
            TOT2 :=  NVL(TOT2, 0) - TOT_INCR2;
      END IF;

     END IF;

   EXIT WHEN M1 = 0;

   END LOOP;  -- TOTALI

ELSE
   PARZ1 := NULL;
   PARZ2 := NULL;

   TOT1  := NULL;
   TOT2  := NULL;
END IF;

I := I + 1;

-- inserimento dello schema di riclassificazione NELLA VIEW (FISSO)

insert into PRT_VPG_BIL_RICLASSIFICATO (ID, CHIAVE, TIPO, SEQUENZA,
        ORDINE, CONTO_RICLASS, I_LIVELLO, II_LIVELLO, III_LIVELLO, IV_LIVELLO,
        DESCRIZIONE, PARZIALE_I_ANNO, TOTALE_I_ANNO, PARZIALE_II_ANNO, TOTALE_II_ANNO,
        SN_TOTALE)
VALUES (aId, 'chiave', 't', i,
        SCHEMA_CE.SEQUENZA, SCHEMA_CE.CD_GRUPPO_EP,
        LIV1, LIV2, LIV3, NULL,
        DECODE(SCHEMA_CE.CD_GRUPPO_EP, 'AVA', SCHEMA_CE.DS_GRUPPO_EP||': '||TO_CHAR(PARZ1, '999G999G999G999D99'), SCHEMA_CE.DS_GRUPPO_EP),
        DECODE(SCHEMA_CE.CD_GRUPPO_EP, 'AVA', NULL, PARZ1),
        DECODE(SCHEMA_CE.CD_GRUPPO_EP, 'AVA', NULL, TOT1),
        PARZ2, TOT2,
        FLAG_TOT);

END LOOP; -- FINE LOOP PRINCIPALE SULLO SCHEMA DI RICLASSIFICAZIONE
Close SCHEMA_CE_RICLASSIFICATO;

End;
/


