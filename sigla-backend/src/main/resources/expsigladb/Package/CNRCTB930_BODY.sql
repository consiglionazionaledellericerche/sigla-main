--------------------------------------------------------
--  DDL for Package Body CNRCTB930
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB930" AS

-- =================================================================================================
-- Controllo congruenza parametri in input
-- =================================================================================================
PROCEDURE chkParametriInput
   (
    inEsercizio NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   numero INTEGER;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Verifica congruenza parametri.
   --   aEsercizio, aCdAnag, repID e aUtente non possono essere nulli
   --   aCdAnag può essere nullo o valorizzato

   IF (inEsercizio IS NULL OR
       inRepID IS NULL OR
       inUtente IS NULL) THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Valorizzazione parametri in input errata. ' ||
          'Un identificativo (esercizio, codice anagrafico, identificativo report, id utente) risulta non valorizzato');
   END IF;


END chkParametriInput;

-- =================================================================================================
-- Costruzione del file
-- =================================================================================================
PROCEDURE scriviFileOutput
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   --mioCLOB CLOB;
   aStringaChiave1 VARCHAR2(5);
   aStringaChiave2 VARCHAR2(8);
   aStringaChiave3 VARCHAR2(6);
   aStringa VARCHAR2(4000);
   aDifferenza INTEGER;

   aStringaImporto VARCHAR2(10);

   i BINARY_INTEGER;

   --aRecBframeBlob BFRAME_BLOB%ROWTYPE;
   aRecEstrazione770 vpg_certificazione_770%ROWTYPE;

   gen_cur GenericCurType;
 -- Variabili usate
   aContatore NUMBER(9);
   aTi_entita varchar2(1);
   aCodiceFiscale varchar2(20);
   aCognome varchar2(200);
   aNome varchar2(200);
   aSesso varchar2(1);
   aDataNascita varchar2(8);
   aComuneNascita varchar2(100);
   aProvinciaNascita varchar2(2);
   aCittadinanzaStatoEstero number(3);
   aRagioneSociale varchar2(200);
   aSpazi varchar2(100);
   aComuneDomicilio varchar2(200);
   aProvinciaDomicilio varchar2(2);
   aIndirizzo varchar2(200);
   aCodiceComuneDomicilio varchar2(200);
   aIdentificativoFiscale varchar2(25);
BEGIN
--pipe.send_message('1');
   -------------------------------------------------------------------------------------------------
   -- Inizializzazione CLOB

   aRecBframeBlob.cd_tipo:=IDTIPOBLOB;
   aRecBframeBlob.path:='fileout770/' || inEsercizio || '/';
   If inTiModello = 'S' then
       aRecBframeBlob.filename:='FILEOUT_770_SEMPLIFICATO_'||inQuadro||'_'|| inRepID || '.DAT';
       aRecBframeBlob.ds_file:='FILE OUTPUT ESTRAZIONE 770 SEMPLIFICATO - QUADRO '||inQuadro;
   Else
       aRecBframeBlob.filename:='FILEOUT_770_ORDINARIO_'||inQuadro||'_'|| inRepID || '.DAT';
       aRecBframeBlob.ds_file:='FILE OUTPUT ESTRAZIONE 770 ORDINARIO - QUADRO '||inQuadro;
   End If;
   aRecBframeBlob.ti_visibilita:='U';
   aRecBframeBlob.ds_utente:='FILE OUTPUT ESTRAZIONE 770';
--pipe.send_message('2');
   IBMUTL005.ShIniCBlob(aRecBframeBlob.cd_tipo,
                        aRecBframeBlob.path,
                        aRecBframeBlob.filename,
                        aRecBframeBlob.ti_visibilita,
                        aRecBframeBlob.ds_file,
                        aRecBframeBlob.ds_utente,
                        inUtente,
                        mioCLOB);

--pipe.send_message('3');
   If inTiModello = 'S' then
      If inQuadro = 'SC' then
            scriviFileQuadroSC(inEsercizio,
                               inTiModello,
                               inQuadro,
                               inRepID,
                               inUtente);
                               --aRecBframeBlob,
                               --mioCLOB);
      Elsif inQuadro = 'SY' then
            scriviFileQuadroSY(inEsercizio,
                               inTiModello,
                               inQuadro,
                               inRepID,
                               inUtente);
      Elsif inQuadro = 'SCSY' then
          scriviFileQuadroSCSY(inEsercizio,
                               inTiModello,
                               inQuadro,
                               inRepID,
                               inUtente );
      End If;
   Elsif inTiModello = 'O' then
      If inQuadro = 'SF' then
            scriviFileQuadroSF(inEsercizio,
                               inTiModello,
                               inQuadro,
                               inRepID,
                               inUtente);
      Elsif inQuadro = 'SH' then
            scriviFileQuadroSH(inEsercizio,
                               inTiModello,
                               inQuadro,
                               inRepID,
                               inUtente);
      End If;
   End If;

   -------------------------------------------------------------------------------------------------
   -- Chiusura CLOB

   IBMUTL005.ShCloseClob(aRecBframeBlob.cd_tipo,
                         aRecBframeBlob.path,
                         aRecBframeBlob.filename,
                         mioCLOB);

END scriviFileOutput;
-- =================================================================================================
-- Costruzione del quadro SC
-- =================================================================================================
PROCEDURE scriviFileQuadroSC
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2--,
   ) IS

   --mioCLOB CLOB;
   aStringaChiave1 VARCHAR2(5);
   aStringaChiave2 VARCHAR2(8);
   aStringaChiave3 VARCHAR2(6);
   aStringa VARCHAR2(4000);
   aDifferenza INTEGER;

   aStringaImporto VARCHAR2(10);

   i BINARY_INTEGER;

   aRecEstrazione770 vpg_certificazione_770%ROWTYPE;

   gen_cur GenericCurType;
 -- Variabili usate
   aContatore NUMBER(9);
   aTi_entita varchar2(1);
   aCodiceFiscale varchar2(20);
   aCognome varchar2(200);
   aNome varchar2(200);
   aSesso varchar2(1);
   aDataNascita varchar2(8);
   aComuneNascita varchar2(100);
   aProvinciaNascita varchar2(2);
   aCittadinanzaStatoEstero number(3);
   aRagioneSociale varchar2(200);
   aSpazi varchar2(100);
   aComuneDomicilio varchar2(200);
   aProvinciaDomicilio varchar2(2);
   aIndirizzo varchar2(200);
   aCodiceComuneDomicilio varchar2(200);
   aIdentificativoFiscale varchar2(25);
BEGIN
--pipe.send_message('4');
   -------------------------------------------------------------------------------------------------
   -- Scrittura CLOB (file output estrazione 770)

   BEGIN

      -- Valorizzazione costanti della chiave file -------------------------------------------------
      aContatore :=1;
      aTi_entita :=' ';
      aCodiceFiscale :=' ';
      aCognome :=' ';
      aNome :=' ';
      aSesso :=' ';
      aDataNascita := '00000000';
      aComuneNascita :=' ';
      aProvinciaNascita :=' ';
      aCittadinanzaStatoEstero :=0;
      aRagioneSociale :=' ';
      aSpazi :=' ';
      aComuneDomicilio :=' ';
      aProvinciaDomicilio :=' ';
      aIndirizzo :=' ';
      aCodiceComuneDomicilio := ' ';
      aIdentificativoFiscale :=' ';
--pipe.send_message('5');
      -- Ciclo elaborazione dati 770 ---------------------------------------------------------------

      OPEN gen_cur FOR

           SELECT *
           FROM   vpg_certificazione_770
           where inTiModello ='S'
             and inQuadro = 'SC'
           Order By ID, CHIAVE, TIPO;
      LOOP
         FETCH gen_cur INTO
               aRecEstrazione770;

         EXIT WHEN gen_cur%NOTFOUND;

      IF  (aRecEstrazione770.nome IS NULL AND
           aRecEstrazione770.COGNOME IS NULL AND
           aRecEstrazione770.RAGIONE_SOCIALE IS NULL) THEN

          -- Creazione del record d'estrazione del 770
             aStringa:=NULL;
             aStringa:= ' SC0';
             aStringa:= aStringa || '80054330586     ';  -- codice fiscale dichiatrante
             aStringa:= aStringa || '                ';  -- codice fiscale sostituto
             -- Persona fisica o giuridica
             IF aTi_entita = 'F' THEN   --persona fisica
                aStringa:=aStringa || '1';
                aStringa:= aStringa || '       ';    -- progressivo telematico
                aStringa:= aStringa || '       ';    -- rfu
                aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');         -- codice fiscale
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aCognome), 1, 24), 24, ' ');        -- cognome
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aNome), 1, 20), 20, ' ');           -- nome
                aStringa:=aStringa || RPAD(SUBSTR(aSesso, 1, 1), 1, ' ');                   -- sesso
                aStringa:=aStringa || RPAD(SUBSTR(aDataNascita, 1, 8), 8, '0');             -- data nascita
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aComuneNascita), 1, 40), 40, ' ');  -- comune nascita
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aProvinciaNascita), 1, 2), 2, ' '); -- provincia nascita
             ELSE    --persona giuridica
                aStringa:=aStringa || '2';
                aStringa:= aStringa || '       ';   -- progressivo telematico
                aStringa:= aStringa || '       ';   -- rfu
                aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');   -- codice fiscale
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aRagioneSociale), 1, 60), 60, ' ');    -- denominazione
                aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 35), 35, ' ');   -- spazi
             END IF;

             aStringa:=aStringa || RPAD(SUBSTR(Upper(aComuneDomicilio), 1, 40), 40, ' ');  -- comune domicilio fiscale
             aStringa:=aStringa || RPAD(SUBSTR(Upper(aProvinciaDomicilio), 1, 2), 2, ' ');   -- provincia domicilio fiscale
             aStringa:=aStringa || '  ';  -- codice regione
             aStringa:=aStringa || RPAD(' ', 4, ' ');  -- codice comune (dovrebbe essere valorizzato solo in casi da noi non gestiti)
             --aStringa:=aStringa || RPAD(SUBSTR(aCodiceComuneDomicilio, 1, 4), 4, ' '); -- codice comune domicilio
             aStringa:=aStringa || RPAD(SUBSTR(Upper(aIndirizzo), 1, 40), 40, ' ');   -- indirizzo
             aStringa:=aStringa || '0';   -- eventi eccezionali
             aStringa:=aStringa || RPAD(SUBSTR(Upper(aIdentificativoFiscale), 1, 25), 25, ' ');   -- identificativo fiscale
             -- per i percipienti esteri occorre ripetere il comune e l'indirizzo
             if aCittadinanzaStatoEstero != '0' THEN
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aComuneDomicilio), 1, 40), 40, ' ');  -- comune domicilio fiscale
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aIndirizzo), 1, 40), 40, ' ');   -- indirizzo
             else
                aStringa:=aStringa || RPAD(' ', 80, ' ');
             end if;
             aStringa:=aStringa || LPAD(aCittadinanzaStatoEstero, 3, '0');   -- cittadinanza stato estero
             aStringa:=aStringa || '0';-- erede
             IF aRecEstrazione770.CD_TI_COMPENSO IS NULL THEN
                aStringa:=aStringa || RPAD(' ', 1, ' ');   -- causale
             ELSE
                aStringa:=aStringa || RPAD(SUBSTR(aRecEstrazione770.CD_TI_COMPENSO, 1, 1), 1, ' ');    -- causale
             END IF;
             aStringa:= aStringa || '0000';   -- anno
             aStringa:= aStringa || '0';   -- anticipazione
             aStringa:= aStringa ||  LPAD((aRecEstrazione770.im_Lordo * 100), 16, '0');   -- lordo

             IF aRecEstrazione770.CD_TRATTAMENTO IS NULL THEN
                aStringa:= aStringa ||  LPAD('0', 16, '0');   -- somma no rit a regime convezionale
             ELSE
               IF SUBSTR(aRecEstrazione770.CD_TRATTAMENTO, 1, 4) LIKE 'T017%'
                  and aRecEstrazione770.im_Lordo = aRecEstrazione770.im_netto THEN
                    aStringa:=aStringa || LPAD((aRecEstrazione770.im_lordo  *100), 16, '0');
               ELSE
                    aStringa:= aStringa ||  LPAD('0', 16, '0');
               END IF;
             END IF;
             If aRecEstrazione770.IM_NON_SOGG_RIT is not null and aRecEstrazione770.IM_NON_SOGG_RIT > 0 then
                aStringa:= aStringa || '3';
             Else
                aStringa:= aStringa || '0';
             End if;
             aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_NON_SOGG_RIT * 100), 16, '0');-- somma non soggetta a ritenute
             aStringa:= aStringa ||  LPAD((aRecEstrazione770.IMPONIBILE_FI * 100), 16, '0');-- imponibile

             IF  aRecEstrazione770.TI_RITENUTA = 'A' THEN
                  aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_RITENUTE * 100), 16, '0');  -- Ritenute a titolo di acconto
                  aStringa:= aStringa ||  LPAD('0', 16, '0');
             ELSE
                 aStringa:= aStringa ||  LPAD('0', 16, '0');
                 aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_RITENUTE * 100), 16, '0');   -- Ritenute a titolo di imposta
             END if;
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Ritenute sospese
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Addizionale regionale a titolo d'acconto
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Addizionale regionale a titolo d'imposta
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Addizionale regionale sospesa
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Addizionale comunale a titolo d'acconto
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Addizionale comunale a titolo d'imposta
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Addizionale comunale sospesa
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Imponibile anni precedenti
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Ritenute anni precedenti
             aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_CONTRIBUTI_ENTE * 100), 16, '0');-- Contributi a carico Ente
             aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_CONTRIBUTI * 100), 16, '0');-- Contributi a carico Percipiente
             aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_NON_SOGG_INPS * 100), 16, '0');-- Spese Rimborsate(quota esente inps)
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Ritenute Rimborsate
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Somme corrisposte prima della data del fallimento
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Somme corrisposte dal curatore/commissario
             aStringa:= aStringa ||  LPAD('0', 177, '0');-- Redditi erogati da altri soggetti
             aStringa:= aStringa || RPAD(' ', 2497, ' '); -- spazio
             aStringa:= aStringa || RPAD(' ', 1, ' ');-- tipo operazione
             aStringa:= aStringa || RPAD(' ', 1, ' ');-- flag validazione
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- codice caricamento
             aStringa:= aStringa || RPAD(' ', 7, ' '); -- codice utente
             aStringa:= aStringa || RPAD(' ', 74, ' ');-- spazio
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 1
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 2
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 3


             IF LENGTH(aStringa) != 3500 THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Errore in lunghezza file in output tipo record 770 lunghezza ' || LENGTH(aStringa));
             END IF;
             -- Scrittura CLOB
             IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                                 aRecBframeBlob.path,
                                 aRecBframeBlob.filename,
                                 mioCLOB,
                                 aStringa);
      ELSE

            IF aRecEstrazione770.TI_ENTITA IS NULL THEN
               aTi_entita :='G';
            ELSE
               aTi_entita:= aRecEstrazione770.TI_ENTITA;
            END IF;

            IF aRecEstrazione770.CODICE_FISCALE IS NULL THEN
               IF  aTi_entita = 'G' THEN
                  IF aRecEstrazione770.PARTITA_IVA IS NULL THEN
                      aContatore := aContatore + 1;
                      aCodiceFiscale := 'ESTERO*' || LPAD (aContatore, 9, '0');
                  ELSE
                      aCodiceFiscale := aRecEstrazione770.PARTITA_IVA;
                  END IF;
               ELSE
                  aContatore := aContatore + 1;
                  aCodiceFiscale := 'ESTERO*' || LPAD (aContatore, 9, '0');
               END IF;
            ELSE
               aCodiceFiscale := aRecEstrazione770.CODICE_FISCALE;
            END IF;

            IF aRecEstrazione770.COGNOME IS NULL THEN
               aCognome :=' ';
            ELSE
               aCognome:= aRecEstrazione770.COGNOME;
            END IF;

            IF aRecEstrazione770.NOME IS NULL THEN
               aNome :=' ';
            ELSE
               aNome:= aRecEstrazione770.NOME;
            END IF;

            IF aRecEstrazione770.TI_SESSO IS NULL THEN
               aSesso :=' ';
            ELSE
               aSesso:= aRecEstrazione770.TI_SESSO;
            END IF;

            IF aRecEstrazione770.DT_NASCITA IS NULL THEN
               aDataNascita:=0;
            ELSE
               aDataNascita:=TO_CHAR(aRecEstrazione770.DT_NASCITA, 'DD') ||
                             TO_CHAR(aRecEstrazione770.DT_NASCITA, 'MM') ||
                             TO_CHAR(aRecEstrazione770.DT_NASCITA, 'YYYY');
            END IF;

            IF aRecEstrazione770.DS_COMUNE_NASCITA IS NULL THEN
               aComuneNascita := ' ';
            ELSE
               aComuneNascita:= aRecEstrazione770.DS_COMUNE_NASCITA;
            END IF;

            IF aRecEstrazione770.CD_PROVINCIA_NASCITA IS NULL THEN
               aProvinciaNascita := ' ';
            ELSE
               aProvinciaNascita:= aRecEstrazione770.CD_PROVINCIA_NASCITA;
            END IF;

            IF aRecEstrazione770.CD_NAZIONE_770 IS NULL Or aRecEstrazione770.CD_NAZIONE_770='*' THEN
               aCittadinanzaStatoEstero := '0';
            ELSE
               aCittadinanzaStatoEstero:= aRecEstrazione770.CD_NAZIONE_770;
            END IF;

            IF aRecEstrazione770.RAGIONE_SOCIALE IS NULL THEN
               aRagioneSociale:= ' ';
            ELSE
               aRagioneSociale:= aRecEstrazione770.RAGIONE_SOCIALE;
            END IF;

            IF aRecEstrazione770.DS_COMUNE_FISCALE IS NULL THEN
               aComuneDomicilio := ' ';
            ELSE
               aComuneDomicilio:= aRecEstrazione770.DS_COMUNE_FISCALE;
            END IF;

            IF aRecEstrazione770.CD_PROVINCIA_FISCALE IS NULL THEN
               aProvinciaDomicilio := ' ';
            ELSE
               aProvinciaDomicilio:= aRecEstrazione770.CD_PROVINCIA_FISCALE;
            END IF;

            IF aRecEstrazione770.VIA_NUM_FISCALE IS NULL THEN
               aIndirizzo := ' ';
            ELSE
               aIndirizzo:= aRecEstrazione770.VIA_NUM_FISCALE;
            END IF;

            IF aRecEstrazione770.DS_COMUNE_FISCALE IS NULL THEN
               aCodiceComuneDomicilio := ' ';
            ELSE
               aCodiceComuneDomicilio:= aRecEstrazione770.DS_COMUNE_FISCALE;
            END IF;

            IF aRecEstrazione770.ID_FISCALE_ESTERO IS NULL THEN
               aIdentificativoFiscale := ' ';
            ELSE
               aIdentificativoFiscale:= aRecEstrazione770.ID_FISCALE_ESTERO;
            END IF;
      END IF;


   END LOOP;

   CLOSE gen_cur;
   END;

END scriviFileQuadroSC;
-- =================================================================================================
-- Costruzione del quadro SF
-- =================================================================================================
PROCEDURE scriviFileQuadroSF
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2--,
    --aRecBframeBlob BFRAME_BLOB%ROWTYPE,
    --inCLOB CLOB
   ) IS

   --mioCLOB CLOB;
   aStringaChiave1 VARCHAR2(5);
   aStringaChiave2 VARCHAR2(8);
   aStringaChiave3 VARCHAR2(6);
   aStringa VARCHAR2(4000);
   aDifferenza INTEGER;

   aStringaImporto VARCHAR2(10);

   i BINARY_INTEGER;

   --aRecBframeBlob BFRAME_BLOB%ROWTYPE;
   aRecEstrazione770 vpg_certificazione_770%ROWTYPE;

   gen_cur GenericCurType;
 -- Variabili usate
   aContatore NUMBER(9);
   aTi_entita varchar2(1);
   aCodiceFiscale varchar2(20);
   aCognome varchar2(200);
   aNome varchar2(200);
   aSesso varchar2(1);
   aDataNascita varchar2(8);
   aComuneNascita varchar2(100);
   aProvinciaNascita varchar2(2);
   aCittadinanzaStatoEstero number(3);
   aRagioneSociale varchar2(200);
   aSpazi varchar2(100);
   aComuneDomicilio varchar2(200);
   aProvinciaDomicilio varchar2(2);
   aIndirizzo varchar2(200);
   aCodiceComuneDomicilio varchar2(200);
   aIdentificativoFiscale varchar2(25);
BEGIN
--pipe.send_message('4');
   -------------------------------------------------------------------------------------------------
   -- Inizializzazione CLOB
/*
   aRecBframeBlob.cd_tipo:=IDTIPOBLOB;
   aRecBframeBlob.path:='fileout770/' || inEsercizio || '/';
   If inTiModello = 'S' then
       aRecBframeBlob.filename:='FILEOUT_770_SEMPLIFICATO_' || inRepID || '.DAT';
       aRecBframeBlob.ds_file:='FILE OUTPUT ESTRAZIONE 770 SEMPLIFICATO';
   Else
       aRecBframeBlob.filename:='FILEOUT_770_ORDINARIO_' || inRepID || '.DAT';
       aRecBframeBlob.ds_file:='FILE OUTPUT ESTRAZIONE 770 ORDINARIO';
   End If;
   aRecBframeBlob.ti_visibilita:='U';
   aRecBframeBlob.ds_utente:='FILE OUTPUT ESTRAZIONE 770';

   IBMUTL005.ShIniCBlob(aRecBframeBlob.cd_tipo,
                        aRecBframeBlob.path,
                        aRecBframeBlob.filename,
                        aRecBframeBlob.ti_visibilita,
                        aRecBframeBlob.ds_file,
                        aRecBframeBlob.ds_utente,
                        inUtente,
                        mioCLOB);
*/
   -------------------------------------------------------------------------------------------------
   -- Scrittura CLOB (file output estrazione 770)

   BEGIN

      -- Valorizzazione costanti della chiave file -------------------------------------------------
      aContatore :=1;
      aTi_entita :=' ';
      aCodiceFiscale :=' ';
      aCognome :=' ';
      aNome :=' ';
      aSesso :=' ';
      aDataNascita := '00000000';
      aComuneNascita :=' ';
      aProvinciaNascita :=' ';
      aCittadinanzaStatoEstero :=0;
      aRagioneSociale :=' ';
      aSpazi :=' ';
      aComuneDomicilio :=' ';
      aProvinciaDomicilio :=' ';
      aIndirizzo :=' ';
      aCodiceComuneDomicilio := ' ';
      aIdentificativoFiscale :=' ';
--pipe.send_message('5');
      -- Ciclo elaborazione dati 770 ---------------------------------------------------------------

      OPEN gen_cur FOR

           SELECT *
           FROM  vpg_certificazione_770
           where inTiModello = 'O'
             and inQuadro = 'SF'
           Order By ID, CHIAVE, TIPO;
      LOOP
         FETCH gen_cur INTO
               aRecEstrazione770;

         EXIT WHEN gen_cur%NOTFOUND;

      IF  (aRecEstrazione770.nome IS NULL AND
           aRecEstrazione770.COGNOME IS NULL AND
           aRecEstrazione770.RAGIONE_SOCIALE IS NULL) THEN

          -- Creazione del record d'estrazione del 770
             aStringa:=NULL;
             aStringa:= ' SF0';
             aStringa:= aStringa || '80054330586     ';  -- codice fiscale dichiarante
             aStringa:= aStringa || '                ';  -- codice fiscale sostituto
             -- Persona fisica o giuridica
             IF aTi_entita = 'F' THEN   --persona fisica
                aStringa:=aStringa || '1';
                aStringa:=aStringa || RPAD(' ', 14, ' ');    -- rfu
                aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');         -- codice fiscale
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aCognome), 1, 24), 24, ' ');        -- cognome
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aNome), 1, 20), 20, ' ');           -- nome
                aStringa:=aStringa || RPAD(SUBSTR(aSesso, 1, 1), 1, ' ');                   -- sesso
                aStringa:=aStringa || RPAD(SUBSTR(aDataNascita, 1, 8), 8, '0');             -- data nascita
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aComuneNascita), 1, 40), 40, ' ');  -- comune nascita
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aProvinciaNascita), 1, 2), 2, ' '); -- provincia nascita
             ELSE    --persona giuridica
                aStringa:=aStringa || '2';
                aStringa:=aStringa || RPAD(' ', 14, ' ');    -- rfu
                aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');   -- codice fiscale
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aRagioneSociale), 1, 60), 60, ' ');    -- denominazione
                aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 35), 35, ' ');   -- spazi
             END IF;

             aStringa:=aStringa || LPAD(aCittadinanzaStatoEstero, 3, '0');   -- cittadinanza stato estero
             aStringa:=aStringa || RPAD(SUBSTR(Upper(aComuneDomicilio), 1, 40), 40, ' ');  -- comune domicilio fiscale
             aStringa:=aStringa || RPAD(SUBSTR(Upper(aProvinciaDomicilio), 1, 2), 2, ' ');   -- provincia domicilio fiscale
             aStringa:=aStringa || RPAD(SUBSTR(Upper(aIndirizzo), 1, 40), 40, ' ');   -- indirizzo
             aStringa:=aStringa || RPAD(' ', 4, ' ');
             aStringa:=aStringa || RPAD(' ', 2, ' ');   -- rfu
             aStringa:=aStringa || RPAD(SUBSTR(Upper(aIdentificativoFiscale), 1, 25), 25, ' ');   -- identificativo fiscale
             IF aRecEstrazione770.CD_TI_COMPENSO IS NULL THEN
                aStringa:=aStringa || RPAD(' ', 1, ' ');   -- causale
             ELSE
                aStringa:=aStringa || RPAD(SUBSTR(aRecEstrazione770.CD_TI_COMPENSO, 1, 1), 1, ' ');    -- causale
             END IF;
             aStringa:= aStringa ||  LPAD((aRecEstrazione770.im_Lordo * 100), 16, '0');   -- lordo
             aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_NON_SOGG_RIT * 100), 16, '0');-- somma non soggetta a ritenute

             aStringa:= aStringa ||  LPAD((nvl(aRecEstrazione770.ALIQUOTA,0) * 1000), 6, '0');-- aliquota
             aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_RITENUTE * 100), 16, '0');  -- Ritenuta

             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Ritenute sospese
             aStringa:= aStringa ||  LPAD('0', 16, '0');-- Rimborsi
             --Dati del rappresentante
             aStringa:= aStringa || RPAD(' ', 1, ' ');  -- tipo
             aStringa:= aStringa || RPAD(' ', 16, ' '); -- codice fiscale
             aStringa:= aStringa || RPAD(' ', 24, ' '); -- cognome
             aStringa:= aStringa || RPAD(' ', 20, ' '); -- nome
             aStringa:= aStringa || RPAD(' ', 1, ' ');  -- sesso
             aStringa:= aStringa || LPAD('0', 8, '0');  -- data nascita
             aStringa:= aStringa || RPAD(' ', 40, ' ');  -- comune nascita
             aStringa:= aStringa || RPAD(' ', 2, ' ');  -- provincia nascita
             aStringa:= aStringa || RPAD(' ', 40, ' ');  -- comune domicilio fiscale
             aStringa:= aStringa || RPAD(' ', 2, ' ');  -- provincia
             aStringa:= aStringa || RPAD(' ', 40, ' ');  -- indirizzo
             aStringa:= aStringa || LPAD('0', 3, '0');  -- codice stato estero

             aStringa:= aStringa || RPAD(' ', 2921, ' '); -- spazio
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- codice caricamento
             aStringa:= aStringa || RPAD(' ', 7, ' '); -- codice utente


             IF LENGTH(aStringa) != 3500 THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Errore in lunghezza file in output tipo record 770 lunghezza ' || LENGTH(aStringa));
             END IF;
             -- Scrittura CLOB
             IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                                 aRecBframeBlob.path,
                                 aRecBframeBlob.filename,
                                 mioCLOB,
                                 aStringa);
      ELSE

            IF aRecEstrazione770.TI_ENTITA IS NULL THEN
               aTi_entita :='G';
            ELSE
               aTi_entita:= aRecEstrazione770.TI_ENTITA;
            END IF;

            IF aRecEstrazione770.CODICE_FISCALE IS NULL THEN
               IF  aTi_entita = 'G' THEN
                  IF aRecEstrazione770.PARTITA_IVA IS NULL THEN
                      aContatore := aContatore + 1;
                      aCodiceFiscale := 'ESTERO*' || LPAD (aContatore, 9, '0');
                  ELSE
                      aCodiceFiscale := aRecEstrazione770.PARTITA_IVA;
                  END IF;
               ELSE
                  aContatore := aContatore + 1;
                  aCodiceFiscale := 'ESTERO*' || LPAD (aContatore, 9, '0');
               END IF;
            ELSE
               aCodiceFiscale := aRecEstrazione770.CODICE_FISCALE;
            END IF;

            IF aRecEstrazione770.COGNOME IS NULL THEN
               aCognome :=' ';
            ELSE
               aCognome:= aRecEstrazione770.COGNOME;
            END IF;

            IF aRecEstrazione770.NOME IS NULL THEN
               aNome :=' ';
            ELSE
               aNome:= aRecEstrazione770.NOME;
            END IF;

            IF aRecEstrazione770.TI_SESSO IS NULL THEN
               aSesso :=' ';
            ELSE
               aSesso:= aRecEstrazione770.TI_SESSO;
            END IF;

            IF aRecEstrazione770.DT_NASCITA IS NULL THEN
               aDataNascita:=0;
            ELSE
               aDataNascita:=TO_CHAR(aRecEstrazione770.DT_NASCITA, 'DD') ||
                             TO_CHAR(aRecEstrazione770.DT_NASCITA, 'MM') ||
                             TO_CHAR(aRecEstrazione770.DT_NASCITA, 'YYYY');
            END IF;

            IF aRecEstrazione770.DS_COMUNE_NASCITA IS NULL THEN
               aComuneNascita := ' ';
            ELSE
               aComuneNascita:= aRecEstrazione770.DS_COMUNE_NASCITA;
            END IF;

            IF aRecEstrazione770.CD_PROVINCIA_NASCITA IS NULL THEN
               aProvinciaNascita := ' ';
            ELSE
               aProvinciaNascita:= aRecEstrazione770.CD_PROVINCIA_NASCITA;
            END IF;

            IF aRecEstrazione770.CD_NAZIONE_770 IS NULL Or aRecEstrazione770.CD_NAZIONE_770='*' THEN
               aCittadinanzaStatoEstero := '0';
            ELSE
               aCittadinanzaStatoEstero:= aRecEstrazione770.CD_NAZIONE_770;
            END IF;

            IF aRecEstrazione770.RAGIONE_SOCIALE IS NULL THEN
               aRagioneSociale:= ' ';
            ELSE
               aRagioneSociale:= aRecEstrazione770.RAGIONE_SOCIALE;
            END IF;

            IF aRecEstrazione770.DS_COMUNE_FISCALE IS NULL THEN
               aComuneDomicilio := ' ';
            ELSE
               aComuneDomicilio:= aRecEstrazione770.DS_COMUNE_FISCALE;
            END IF;

            IF aRecEstrazione770.CD_PROVINCIA_FISCALE IS NULL THEN
               aProvinciaDomicilio := ' ';
            ELSE
               aProvinciaDomicilio:= aRecEstrazione770.CD_PROVINCIA_FISCALE;
            END IF;

            IF aRecEstrazione770.VIA_NUM_FISCALE IS NULL THEN
               aIndirizzo := ' ';
            ELSE
               aIndirizzo:= aRecEstrazione770.VIA_NUM_FISCALE;
            END IF;

            IF aRecEstrazione770.DS_COMUNE_FISCALE IS NULL THEN
               aCodiceComuneDomicilio := ' ';
            ELSE
               aCodiceComuneDomicilio:= aRecEstrazione770.DS_COMUNE_FISCALE;
            END IF;

            IF aRecEstrazione770.ID_FISCALE_ESTERO IS NULL THEN
               aIdentificativoFiscale := ' ';
            ELSE
               aIdentificativoFiscale:= aRecEstrazione770.ID_FISCALE_ESTERO;
            END IF;
      END IF;


   END LOOP;

   CLOSE gen_cur;
   END;
/*
   -------------------------------------------------------------------------------------------------
   -- Chiusura CLOB

   IBMUTL005.ShCloseClob(aRecBframeBlob.cd_tipo,
         aRecBframeBlob.path,
                         aRecBframeBlob.filename,
       mioCLOB);
*/
END scriviFileQuadroSF;
-- =================================================================================================
-- Costruzione del quadro SH
-- =================================================================================================
PROCEDURE scriviFileQuadroSH
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2--,
    --aRecBframeBlob BFRAME_BLOB%ROWTYPE,
    --inCLOB CLOB
   ) IS

   --mioCLOB CLOB;
   aStringaChiave1 VARCHAR2(5);
   aStringaChiave2 VARCHAR2(8);
   aStringaChiave3 VARCHAR2(6);
   aStringa VARCHAR2(4000);

   i BINARY_INTEGER;

   --aRecBframeBlob BFRAME_BLOB%ROWTYPE;
   aRecEstrazione770 vpg_certificazione_770%ROWTYPE;
   gen_cur GenericCurType;
 -- Variabili usate
   contaDettagli number;
BEGIN
--pipe.send_message('4');
   -------------------------------------------------------------------------------------------------
   -- Inizializzazione CLOB
/*
   aRecBframeBlob.cd_tipo:=IDTIPOBLOB;
   aRecBframeBlob.path:='fileout770/' || inEsercizio || '/';
   If inTiModello = 'S' then
       aRecBframeBlob.filename:='FILEOUT_770_SEMPLIFICATO_' || inRepID || '.DAT';
       aRecBframeBlob.ds_file:='FILE OUTPUT ESTRAZIONE 770 SEMPLIFICATO';
   Else
       aRecBframeBlob.filename:='FILEOUT_770_ORDINARIO_' || inRepID || '.DAT';
       aRecBframeBlob.ds_file:='FILE OUTPUT ESTRAZIONE 770 ORDINARIO';
   End If;
   aRecBframeBlob.ti_visibilita:='U';
   aRecBframeBlob.ds_utente:='FILE OUTPUT ESTRAZIONE 770';

   IBMUTL005.ShIniCBlob(aRecBframeBlob.cd_tipo,
                        aRecBframeBlob.path,
                        aRecBframeBlob.filename,
                        aRecBframeBlob.ti_visibilita,
                        aRecBframeBlob.ds_file,
                        aRecBframeBlob.ds_utente,
                        inUtente,
                        mioCLOB);
*/
   -------------------------------------------------------------------------------------------------
   -- Scrittura CLOB (file output estrazione 770)

   BEGIN

      -- Valorizzazione costanti della chiave file -------------------------------------------------
      contaDettagli :=0;

--pipe.send_message('5');
      -- Ciclo elaborazione dati 770 ---------------------------------------------------------------

      OPEN gen_cur FOR

           SELECT *
           FROM  vpg_certificazione_770
           where inTiModello = 'O'
             and inQuadro = 'SH'
             and tipo = 'B'
           Order By ID, CHIAVE, TIPO;
      LOOP
         FETCH gen_cur INTO
               aRecEstrazione770;

         EXIT WHEN gen_cur%NOTFOUND;
         contaDettagli := contaDettagli + 1;
         if contaDettagli = 1 then
          -- Creazione del record d'estrazione del 770
             aStringa:=NULL;
             aStringa:= ' SH0';
             aStringa:= aStringa || '80054330586     ';  -- codice fiscale dichiarante
             aStringa:= aStringa || '                ';  -- codice fiscale sostituto
             aStringa:= aStringa || '7';   --prospetto G
             aStringa:=aStringa || RPAD(' ', 14, ' ');    -- rfu
         end if;
         aStringa:= aStringa ||  LPAD((aRecEstrazione770.IMPONIBILE_FI * 100), 16, '0');   -- somme soggette a ritenute
         aStringa:= aStringa ||  LPAD((nvl(aRecEstrazione770.ALIQUOTA,0) * 1000), 6, '0'); -- aliquota
         aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_RITENUTE * 100), 16, '0');     -- Ritenuta operata

         if contaDettagli = 3 then
             aStringa:= aStringa || RPAD(' ', 3318, ' '); -- spazio
             aStringa:= aStringa || RPAD(' ', 10, ' ');   -- codice caricamento
             aStringa:= aStringa || RPAD(' ', 7, ' ');    -- codice utente
             contaDettagli := 0;

             IF LENGTH(aStringa) != 3500 THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Errore in lunghezza file in output tipo record 770 lunghezza ' || LENGTH(aStringa));
             END IF;
             -- Scrittura CLOB
             IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                                 aRecBframeBlob.path,
                                 aRecBframeBlob.filename,
                                 mioCLOB,
                                 aStringa);
         end if;

   END LOOP;

   if contaDettagli < 3 and contaDettagli > 0 then    -- se è 3 non devo fare più nulla
       if contaDettagli = 1 then  -- devo aggiungere zeri per dettagli 2 e 3
             aStringa:= aStringa || LPAD('0', 16, '0');   -- somme soggette a ritenute
             aStringa:= aStringa || LPAD('0', 6, '0');    -- aliquota
             aStringa:= aStringa || LPAD('0', 16, '0');     -- Ritenuta operata
             aStringa:= aStringa || LPAD('0', 16, '0');   -- somme soggette a ritenute
             aStringa:= aStringa || LPAD('0', 6, '0');    -- aliquota
             aStringa:= aStringa || LPAD('0', 16, '0');     -- Ritenuta operata
       elsif contaDettagli = 2 then  -- devo aggiungere zeri per dettaglio 3
             aStringa:= aStringa || LPAD('0', 16, '0');   -- somme soggette a ritenute
             aStringa:= aStringa || LPAD('0', 6, '0');    -- aliquota
             aStringa:= aStringa || LPAD('0', 16, '0');     -- Ritenuta operata
       end if;

       aStringa:= aStringa || RPAD(' ', 3318, ' '); -- spazio
       aStringa:= aStringa || RPAD(' ', 10, ' ');   -- codice caricamento
       aStringa:= aStringa || RPAD(' ', 7, ' ');    -- codice utente

       IF LENGTH(aStringa) != 3500 THEN
             IBMERR001.RAISE_ERR_GENERICO
                ('Errore in lunghezza file in output tipo record 770 lunghezza ' || LENGTH(aStringa));
       END IF;
       -- Scrittura CLOB
       IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                           aRecBframeBlob.path,
                           aRecBframeBlob.filename,
                           mioCLOB,
                           aStringa);
   end if;
   CLOSE gen_cur;
   END;
/*
   -------------------------------------------------------------------------------------------------
   -- Chiusura CLOB

   IBMUTL005.ShCloseClob(aRecBframeBlob.cd_tipo,
         aRecBframeBlob.path,
                         aRecBframeBlob.filename,
       mioCLOB);
*/
END scriviFileQuadroSH;
-- =================================================================================================
-- Costruzione del quadro SY
-- =================================================================================================
PROCEDURE scriviFileQuadroSY
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   aStringaChiave1 VARCHAR2(5);
   aStringaChiave2 VARCHAR2(8);
   aStringaChiave3 VARCHAR2(6);
   aStringa VARCHAR2(4000);
   aDifferenza INTEGER;

   aStringaImporto VARCHAR2(10);

   i BINARY_INTEGER;

   aRecEstrazione770 vpg_certificazione_770%ROWTYPE;

   gen_cur GenericCurType;
 -- Variabili usate
   aCodiceFiscale varchar2(20);

BEGIN
--pipe.send_message('4');
   BEGIN

      -- Valorizzazione costanti della chiave file -------------------------------------------------
      aCodiceFiscale :=' ';

      -- Ciclo elaborazione dati 770 ---------------------------------------------------------------

      OPEN gen_cur FOR

           SELECT *
           FROM  vpg_certificazione_770
           where inTiModello = 'S'
             and inQuadro = 'SY'
           Order By ID, CHIAVE, TIPO,CF_PI_PIGNORATO;
      LOOP
         FETCH gen_cur INTO
               aRecEstrazione770;

         EXIT WHEN gen_cur%NOTFOUND;

      IF  (aRecEstrazione770.nome IS NULL AND
           aRecEstrazione770.COGNOME IS NULL AND
           aRecEstrazione770.RAGIONE_SOCIALE IS NULL) THEN      -- record di tipo B

          -- Creazione del record d'estrazione del 770
             aStringa:=NULL;
             aStringa:= ' SY0';
             aStringa:= aStringa || '80054330586     ';  -- codice fiscale dichiarante
             aStringa:= aStringa || '                ';  -- codice fiscale sostituto
             aStringa:= aStringa || '1';                 -- prospetto
             aStringa:=aStringa || RPAD(' ', 14, ' ');   -- rfu
             aStringa:=aStringa || '0';                  -- tipo invio

             aStringa:=aStringa || RPAD(SUBSTR(nvl(aRecEstrazione770.CF_PI_PIGNORATO,' '), 1, 16), 16, ' ');   -- codice fiscale pignorato
             aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');   -- codice fiscale

             aStringa:= aStringa ||  LPAD((aRecEstrazione770.im_Lordo * 100), 16, '0');   -- somma erogata
             aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_RITENUTE * 100), 16, '0');  -- ritenuta operata

             If aRecEstrazione770.IM_RITENUTE > 0 then
                  aStringa:=aStringa || '0';
             Else
                  aStringa:=aStringa || '1';
             End if;

             aStringa:= aStringa || RPAD(' ', 3383, ' '); -- spazio


             IF LENGTH(aStringa) != 3500 THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Errore in lunghezza file in output tipo record 770 lunghezza ' || LENGTH(aStringa));
             END IF;
             -- Scrittura CLOB
             IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                                 aRecBframeBlob.path,
                                 aRecBframeBlob.filename,
                                 mioCLOB,
                                 aStringa);
      ELSE    -- record di tipo A
            IF aRecEstrazione770.CODICE_FISCALE IS NULL THEN
                 aCodiceFiscale := aRecEstrazione770.PARTITA_IVA;
            ELSE
                 aCodiceFiscale := aRecEstrazione770.CODICE_FISCALE;
            END IF;
      END IF;
   END LOOP;

   CLOSE gen_cur;
   END;

END scriviFileQuadroSY;
-- =================================================================================================
-- Estrazione dati per stampa 770
-- =================================================================================================
PROCEDURE estrazione770Interna
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2,
    pg_exec NUMBER
   ) IS
   tc IBMPRT000.t_cursore;
BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazione variabili

   dataOdierna:=sysdate;

   -------------------------------------------------------------------------------------------------
   -- Inserimento in VPG_CERTIFICAZIONE_770
   If inTiModello = 'S' then
          IBMUTL200.LOGINF(pg_exec,
                           'Estrazione dati 770 - ',
                           'FASE 1',
                           'FASE 1 - Modello Semplificato - Quadro: '||inQuadro);
   Elsif inTiModello = 'O' then
          IBMUTL200.LOGINF(pg_exec,
                           'Estrazione dati 770 - ',
                           'FASE 1',
                           'FASE 1 - Modello Ordinario - Quadro: '||inQuadro);
   End If;


    SPG_CERTIFICAZIONE_770(tc,
                          inEsercizio,
                          inTiModello,
                          inQuadro,
                          '%',
                          null);
   -------------------------------------------------------------------------------------------------
   -- Costruzione del file

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati 770 - Dettaglio elaborazione',
                    'FASE 2',
                    'FASE 2 - creazione CLOB (file output)');

   scriviFileOutput(inEsercizio,
                    inTiModello,
                    inQuadro,
                    inRepID,
                    inUtente);

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati 770 - Dettaglio elaborazione',
                    'FASE 3',
                    'FASE 3 - Fine caricamento');

END estrazione770Interna;


-- =================================================================================================
-- Guscio per gestione estrazione 770 in batch
-- =================================================================================================
PROCEDURE job_estrazione770
   (
    job NUMBER,
    pg_exec NUMBER,
    next_date DATE,
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inMsgError VARCHAR2,
    inUtente VARCHAR2
   ) IS
   aStringa VARCHAR2(2000);
   i BINARY_INTEGER;

BEGIN

   -- Lancio start esecuzione log

   aStringa:=inMsgError;

   IBMUTL210.logStartExecutionUpd(pg_exec, IDTIPOLOG, job, 'Richiesta utente:' || inUtente,
                                  'Estrazione dati 770. Start:' || TO_CHAR(sysdate,'YYYY/MM/DD HH-MI-SS'));

   BEGIN

      estrazione770Interna(inEsercizio,
                           inTiModello,
                           inQuadro,
                           inRepID,
                          -- aStringa,
                           inUtente,
                           pg_exec);

      COMMIT;

      -- Messaggio di operazione completata ad utente

      IBMUTL205.LOGINF('Estrazione dati 770',
                       'Estrazione dati 770 ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS'),
                       'Operazione completata con successo',
                       inUtente);

   EXCEPTION

      WHEN others THEN
           ROLLBACK;

      -- Messaggio di attenzione ad utente

      IBMUTL205.LOGWAR('Estrazione dati 770 ' || errori_tab.COUNT,
                       'Estrazione dati 770 ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                       '(pg_exec = ' || pg_exec || ')', DBMS_UTILITY.FORMAT_ERROR_STACK, inUtente);

      -- Scrittura degli eventuali altri errori

      IF errori_tab.COUNT > 0 THEN

         FOR i IN errori_tab.FIRST .. errori_tab.LAST

         LOOP

            IBMUTL200.LOGWAR(pg_exec,
                             'Estrazione dati 770- Dettaglio errori',
                             'Errore : ' || errori_tab(i).tStringaErr,
                             'Identificativo = ' || errori_tab(i).tStringaKey);

         END LOOP;

      END IF;

   END;

END job_estrazione770;

-- =================================================================================================
-- Main estrazione 770 Standard
-- =================================================================================================
PROCEDURE estrazione770
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inMsgError IN OUT VARCHAR2,
    inUtente VARCHAR2
   ) IS

   aProcedure VARCHAR2(2000);
   aStringa VARCHAR2(2000);

   --inTiModello VARCHAR2(1);
   --inQuadro VARCHAR2(2);

BEGIN

   --inTiModello := 'O';
   --inQuadro    := 'SH';

   -------------------------------------------------------------------------------------------------
   -- Controllo congruenza parametri in input

   chkParametriInput(inEsercizio,
                     inRepID,
                     inUtente);

   -------------------------------------------------------------------------------------------------
   -- Valorizza parametri

   aStringa:=inMsgError;

   -------------------------------------------------------------------------------------------------
   -- Attivazione della gestione batch per estrazione 770

   aProcedure:='CNRCTB930.job_estrazione770(job, ' ||
                                            'pg_exec, ' ||
                                            'next_date, ' ||
                                            inEsercizio || ',''' ||
                                            inTiModello || ''','''||
                                            inQuadro || ''',' ||
                                            inRepID || ',''' ||
                                            aStringa || ''',' ||
                                            '''' || inUtente || ''');';

   IBMUTL210.CREABATCHDINAMICO('Estrazione dati 770',
                               aProcedure,
                               inUtente);

   IBMUTL001.deferred_commit;

   IBMERR001.RAISE_ERR_GENERICO
      ('Operazione sottomessa per esecuzione. Al completamento l''utente riceverà un messaggio di notifica ' ||
       'dello stato dell''operazione');

END estrazione770;
-- Nuovo quadro
PROCEDURE scriviFileQuadroSCSY
   (
    inEsercizio NUMBER,
    inTiModello VARCHAR2,
    inQuadro VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   aStringa_interna_sdoppia VARCHAR2(5000);
   aStringa_sdoppia VARCHAR2(5000);
   aStringa_prima_sdoppia VARCHAR2(5000);
   aStringa_finale VARCHAR2(5000);
   aStringa_anag VARCHAR2(5000);
   aStringa VARCHAR2(5000);
   aStringa1 VARCHAR2(5000);
   aStringa2 VARCHAR2(5000);
   aStringa3 VARCHAR2(5000);
   --mioCLOB_sc1 CLOB;
   --mioCLOB_sc2 CLOB;
   --aRecBframeBlob_sc1 BFRAME_BLOB%ROWTYPE;
   --aRecBframeBlob_sc2 BFRAME_BLOB%ROWTYPE;
   --aRecBframeBlob_sc BFRAME_BLOB%ROWTYPE;
   i BINARY_INTEGER;

   aRecEstrazione770 vpg_certificazione_770%ROWTYPE;
   gen_cur GenericCurType;
 -- Variabili usate
   aContatore NUMBER(9);
   aTi_entita varchar2(1);
   aCdAnag varchar2(6);

   aCodiceFiscale varchar2(20);
   aCognome varchar2(200);
   aNome varchar2(200);
   aSesso varchar2(1);
   aDataNascita varchar2(8);
   aComuneNascita varchar2(100);
   aProvinciaNascita varchar2(2);
   aCittadinanzaStatoEstero number(3);
   aRagioneSociale varchar2(200);
   aSpazi varchar2(100);
   aComuneDomicilio varchar2(200);
   aProvinciaDomicilio varchar2(2);
   aIndirizzo varchar2(200);
   aCodiceComuneDomicilio varchar2(200);
   aIdentificativoFiscale varchar2(25);
   aCodiceFiscalePignorato varchar2(20);
   aImportoLordo number;
BEGIN
   -------------------------------------------------------------------------------------------------
   BEGIN
      -- Valorizzazione costanti della chiave file -------------------------------------------------
      aContatore :=1;
      aTi_entita :=' ';
      aCodiceFiscale :=' ';
      aCodiceFiscalePignorato :=' ';
      aCognome :=' ';
      aNome :=' ';
      aSesso :=' ';
      aCdAnag:=' ';
      aDataNascita := '00000000';
      aComuneNascita :=' ';
      aProvinciaNascita :=' ';
      aCittadinanzaStatoEstero :=0;
      aRagioneSociale :=' ';
      aSpazi :=' ';
      aComuneDomicilio :=' ';
      aProvinciaDomicilio :=' ';
      aIndirizzo :=' ';
      aCodiceComuneDomicilio := ' ';
      aIdentificativoFiscale :=' ';

      -- Ciclo elaborazione dati 770 ---------------------------------------------------------------
    OPEN gen_cur FOR

           SELECT *
           FROM   vpg_certificazione_770
           where TI_MODELLO ='S'
             and (CD_QUADRO = 'SC' or
             (cd_quadro ='SY'  and
             not exists(Select 1 from vpg_certificazione_770 vpg
              where vpg.TI_MODELLO ='S'    and
                    vpg_certificazione_770.cd_anag=vpg.cd_anag   and
                    vpg.CD_QUADRO = 'SC')))
           Order By ID, CHIAVE, TIPO;
      LOOP
         FETCH gen_cur INTO
               aRecEstrazione770;

         EXIT WHEN gen_cur%NOTFOUND;
        -- pipe.send_message('nome '||aRecEstrazione770.nome||' '||aRecEstrazione770.CD_NAZIONE_770);
      IF  (aRecEstrazione770.nome IS NULL AND
           aRecEstrazione770.COGNOME IS NULL AND
           aRecEstrazione770.RAGIONE_SOCIALE IS NULL) THEN

          -- Creazione del record d'estrazione del 770
             aStringa:=NULL;
             aStringa:= ' SC0';
             aStringa:= aStringa || '80054330586     ';  -- codice fiscale dichiatrante
             aStringa:= aStringa || '                ';  -- codice fiscale sostituto
             -- Persona fisica o giuridica
             IF aTi_entita = 'F' THEN   --persona fisica
                aStringa:=aStringa || '1';
                aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');         -- codice fiscale
                aStringa:=aStringa || RPAD(' ', 16, ' '); --Filler
                aStringa:= aStringa || '00';  -- progressivo rapporto Numerico di 2
                aStringa:= aStringa || '0';   -- Filler di 1 - vale zero
                aStringa:= aStringa || '0000';   -- Tipo posizione numerico di 4 - vale zero
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aCognome), 1, 60), 60, ' ');        -- cognome
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aNome), 1, 20), 20, ' ');           -- nome
                aStringa:=aStringa || RPAD(SUBSTR(aSesso, 1, 1), 1, ' ');                   -- sesso
                aStringa:=aStringa || RPAD(SUBSTR(aDataNascita, 1, 8), 8, '0');             -- data nascita
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aComuneNascita), 1, 40), 40, ' ');  -- comune nascita
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aProvinciaNascita), 1, 2), 2, ' '); -- provincia nascita
             ELSE    --persona giuridica
                aStringa:=aStringa || '2';
                aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');   -- codice fiscale
                aStringa:=aStringa || RPAD(' ', 16, ' '); --filler
                aStringa:= aStringa || '00';  -- progressivo rapporto Numerico di 2
                aStringa:= aStringa || '0';   -- Filler di 1 - vale zero
                aStringa:= aStringa || '0000';   -- Tipo posizione numerico di 4 - vale zero
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aRagioneSociale), 1, 60), 60, ' ');    -- denominazione
                aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 20), 20, ' ');           -- nome
                aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 1), 1, ' ');                   -- sesso
                aStringa:=aStringa || RPAD(SUBSTR(aDataNascita, 1, 8), 8, '0');             -- data nascita inizializzata a '00000000'
                aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 40), 40, ' ');  -- comune nascita
                aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 2), 2, ' '); -- provincia nascita
             END IF;
              aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 2), 2, ' '); -- categorie particolari
              aStringa:=aStringa || '0';  -- eventi eccezionali
              aStringa:=aStringa || '0';  -- esclusione precompilata
        if(aCittadinanzaStatoEstero!=0) then
              aStringa:=aStringa || RPAD(' ', 40, ' ');  -- comune domicilio fiscale
              aStringa:=aStringa || RPAD(' ', 2, ' ');   -- provincia domicilio fiscale
        ELSE
              aStringa:=aStringa || RPAD(SUBSTR(Upper(aComuneDomicilio), 1, 40), 40, ' ');  -- comune domicilio fiscale
              aStringa:=aStringa || RPAD(SUBSTR(Upper(aProvinciaDomicilio), 1, 2), 2, ' ');   -- provincia domicilio fiscale
        END IF;
              aStringa:=aStringa || RPAD(' ', 4, ' ');  -- codice comune (dovrebbe essere valorizzato solo in casi da noi non gestiti)
              --aStringa:=aStringa || RPAD(SUBSTR(aCodiceComuneDomicilio, 1, 4), 4, ' '); -- codice comune domicilio
              --aStringa:=aStringa || RPAD(SUBSTR(Upper(aIndirizzo), 1, 40), 40, ' ');   -- indirizzo
              aStringa:=aStringa || RPAD(' ', 4, ' ');  -- codice comune fusione 2017 N
              aStringa:=aStringa || RPAD(' ', 40, ' ');   --comune

              aStringa:=aStringa || RPAD(' ', 2, ' ');   -- provincia domicilio fiscale
              aStringa:=aStringa || RPAD(' ', 4, ' ');  -- codice comune (dovrebbe essere valorizzato solo in casi da noi non gestiti)
              aStringa:=aStringa || RPAD(' ', 4, ' ');  -- codice comune (dovrebbe essere valorizzato solo in casi da noi non gestiti)
              aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 16), 16, ' ');   -- codice fiscale rappresentante incapace

             -- per i percipienti esteri occorre ripetere il comune e l'indirizzo

             if(aCittadinanzaStatoEstero!=0) then
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aIdentificativoFiscale), 1, 25), 25, ' ');   -- identificativo fiscale
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aComuneDomicilio), 1, 40), 40, ' ');  -- comune domicilio fiscale
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aIndirizzo), 1, 40), 40, ' ');   -- indirizzo
                aStringa:=aStringa ||  ' ';   -- schumaker
                aStringa:=aStringa ||LPAD(aCittadinanzaStatoEstero, 3, '0');
             else
                aStringa:=aStringa || RPAD(' ', 106, ' ');
                aStringa:=aStringa ||  '000';   -- codice stato estero numeri di 3
             end if;
             aStringa:=aStringa ||  'A';   -- livello totalizzazione  vale 'A' autonomo
             -- fino a 438 caratteri
               aStringa:= aStringa || RPAD('CONSIGLIO NAZIONALE DELL', 24, ' ');
               aStringa:= aStringa || RPAD('E RICERCHE', 24, ' ');
               aStringa:= aStringa || RPAD(' ', 48, ' ');
               aStringa:= aStringa || LPAD('0', 10, '0'); -- calce
             -- fino a 544 caratteri
               aStringa:= aStringa || RPAD('ROMA', 24, ' '); -- caaf comune
               aStringa:= aStringa || RPAD('RM', 2, ' '); -- caaf prov
               aStringa:= aStringa || RPAD('00185', 5, ' '); -- caaf cap
               aStringa:= aStringa || RPAD('PIAZZALE ALDO MORO 7', 35, ' '); -- caaf ind
               aStringa:= aStringa || RPAD('T0649931 F064', 13, ' '); -- caaf tel
               aStringa:= aStringa || RPAD('9933306', 13, ' '); -- caaf fax
               aStringa:= aStringa || RPAD('MOD730@CNR.IT', 60, ' '); -- caaf mail
               aStringa:= aStringa || RPAD('721909', 6, ' '); -- codice attività
               aStringa:= aStringa || RPAD(' ', 3, ' '); -- codice sede
              --  aStringa:= aStringa || RPAD(' ', 150, ' ');
              -- fino a 705 caratteri

               aStringa:= aStringa || LPAD(to_char(trunc(sysdate),'DDMMYYYY'), 8, '0'); -- data stampa
               aStringa:= aStringa || RPAD('LA PRESIDENTE MARIA CHIARA CARROZZA', 50, ' ');
               aStringa:= aStringa || RPAD(' ', 3, ' ');

               aStringa:= aStringa || LPAD('0', 6, '0'); -- num pagina
               aStringa:= aStringa || RPAD(' ', 78, ' ');
               aStringa:= aStringa || LPAD('0', 5, '0'); -- cap
               aStringa:= aStringa || RPAD(' ', 121, ' ');
               aStringa:= aStringa || LPAD('0', 5, '0'); -- cap laser
               aStringa:= aStringa || RPAD(' ', 5, ' ');
               aStringa:= aStringa || LPAD('0', 5, '0');
               aStringa:= aStringa || LPAD(inEsercizio, 4, '0'); -- anno denuncia
               aStringa:= aStringa || RPAD(' ', 132, ' ');
             -- fino a 1127 caratteri
               aStringa:= aStringa || LPAD('0', 5, '0'); -- progressivo cert.
               aStringa:= aStringa || RPAD(' ', 24, ' ');
               aStringa:= aStringa || '0'; -- FLAG conferma certificazione ????

               aStringa:= aStringa || RPAD(' ', 2722, ' ');
               aStringa:= aStringa || RPAD(' ', 1, ' ');-- flag validazione
               aStringa:= aStringa || RPAD(' ', 10, ' ');-- codice caricamento
               aStringa:= aStringa || RPAD(' ', 7, ' '); -- codice utente

               aStringa:= aStringa || RPAD('9410', 4, ' '); -- codice cliente fisso
               aStringa:= aStringa || RPAD('0000', 4, ' ');--CODICE CRD
               aStringa:= aStringa || RPAD('999999', 10, ' ');--CODICE ROTTURA 1
              aStringa:= aStringa || RPAD(' ', 10, ' ');--CODICE ROTTURA 2
              aStringa:= aStringa || RPAD(' ', 10, ' ');--CODICE ROTTURA 3
              aStringa:= aStringa || RPAD(SUBSTR(Upper(aCognome), 1, 24), 24, ' ');--DATO ORDINATO
              aStringa:= aStringa || RPAD('9410', 4, ' ');--CP  -- fisso da tracciato
              aStringa:= aStringa || RPAD(aCdAnag, 6, ' ');--Ni  -- anagrafica
              aStringa:= aStringa || 'R';-- ROTTURA 1
              aStringa:= aStringa || RPAD(' ', 1, ' ');-- ROTTURA 2

               --aStringa:= aStringa || RPAD(' ', 74, ' ');-- spazio
               aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 1
               aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 2
               aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 3
               --pipe.send_message('sc0');

               IF LENGTH(aStringa) != 4000 THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Errore in lunghezza file in output tipo record 770 lunghezza sc0 ' || LENGTH(aStringa));
                END IF;
                aStringa_anag := aStringa;
             -- Scrittura CLOB
              IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                                 aRecBframeBlob.path,
                                 aRecBframeBlob.filename,
                                 mioCLOB,
                                 aStringa);

              -- Creazione del record d'estrazione del 770 SC1
             aStringa:= ' SC1';
             aStringa:= aStringa || '80054330586     ';  -- codice fiscale dichiatrante
             aStringa:= aStringa || '                ';  -- codice fiscale sostituto

             IF aTi_entita = 'F' THEN   --persona fisica
                aStringa:=aStringa || '1';
                aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');         -- codice fiscale
                aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 16), 16, ' ');   -- filler
                aStringa:= aStringa || '00';  -- progressivo rapporto Numerico di 2
                aStringa:= aStringa || '0';   -- Filler di 1 - vale zero
                aStringa:= aStringa || '0001';   -- Tipo posizione numerico di 4 - vale uno per il primo modulo??
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aCognome), 1, 60), 60, ' ');        -- cognome
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aNome), 1, 20), 20, ' ');           -- nome
             ELSE    --persona giuridica
                aStringa:=aStringa || '2';
                aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');   -- codice fiscale
                aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 16), 16, ' ');   -- filler
                aStringa:= aStringa || '00';  -- progressivo rapporto Numerico di 2
                aStringa:= aStringa || '0';   -- Filler di 1 - vale zero
                aStringa:= aStringa || '0001';   -- Tipo posizione numerico di 4 - vale uno per il primo modulo??
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aRagioneSociale), 1, 60), 60, ' ');    -- denominazione
                aStringa:= aStringa || RPAD(' ', 20, ' '); -- nome
             end if;
             if(aRecEstrazione770.cd_quadro='SC') then
                 IF aRecEstrazione770.CD_TI_COMPENSO IS NULL THEN
                    aStringa:=aStringa || RPAD(' ', 2, ' ');   -- causale
                 ELSE
                    aStringa:=aStringa || RPAD(SUBSTR(aRecEstrazione770.CD_TI_COMPENSO, 1, 2), 2, ' ');    -- causale
                 END IF;
             else
                aStringa:=aStringa || RPAD(' ', 2, ' ');   -- causale
             end if;
             aStringa:= aStringa || '    ';   -- anno
             aStringa:= aStringa || ' ';   -- anticipazione --cambiato su scarto di data manager

             aStringa_prima_sdoppia := aStringa;
             aStringa_sdoppia := null;
             aStringa := '';
             aStringa1 := '';
             aStringa2 := '';
             aStringa3 := '';

             if(aRecEstrazione770.cd_quadro='SC') then
              -- Se l'imponibile fiscale è superiore al lordo mettiamo l'imponibile fiscale su importo lordo
              -- caso Rivalsa Inps
                    if(aRecEstrazione770.IMPONIBILE_FI > aRecEstrazione770.im_Lordo) then
                  aStringa1:= aStringa1 ||  LPAD((aRecEstrazione770.IMPONIBILE_FI * 100), 12, '0');   -- lordo
                else
                  aStringa1:= aStringa1 ||  LPAD((aRecEstrazione770.im_Lordo * 100), 12, '0');   -- lordo
                end if;
                IF aRecEstrazione770.CD_TRATTAMENTO IS NULL THEN
                    aStringa2:= aStringa2 ||  LPAD('0', 12, '0');   -- somma no rit a regime convezionale
                ELSE
                    -- Trattamenti con convenzione
                   IF (SUBSTR(aRecEstrazione770.CD_TRATTAMENTO, 1, 4) LIKE 'T017%' or SUBSTR(aRecEstrazione770.CD_TRATTAMENTO, 1, 4) LIKE 'R017%') and aRecEstrazione770.im_Lordo = aRecEstrazione770.im_netto THEN
                          aStringa2:=aStringa2 || LPAD((aRecEstrazione770.im_lordo*100), 12, '0');
                       ELSE
                        aStringa2:= aStringa2 ||  LPAD('0', 12, '0');
                       END IF;
                END IF;

                If aRecEstrazione770.IM_NON_SOGG_RIT is not null and aRecEstrazione770.IM_NON_SOGG_RIT > 0 then
                  IF (SUBSTR(aRecEstrazione770.CD_TRATTAMENTO, 1, 4) LIKE 'T017%' or SUBSTR(aRecEstrazione770.CD_TRATTAMENTO, 1, 4) LIKE 'R017%') and aRecEstrazione770.im_Lordo = aRecEstrazione770.im_netto THEN
                      aStringa:= aStringa1||aStringa2 || '0';
                      aStringa:= aStringa||LPAD('0', 12, '0');--somma non soggetta a ritenute
                  else
                      -- NON E' CM e Regime Forfettario e Legge 388 ma ha IM_NON_SOGG_RIT!=0
                      IF (SUBSTR(aRecEstrazione770.CD_TRATTAMENTO, 1, 4) NOT in ('T255','T256','T257','T259','T159','T155','T156','T157','T260','T091','T191')) then

                          aStringa:= aStringa1||aStringa2 || '8';
                          aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_NON_SOGG_RIT * 100), 12, '0');-- somma non soggetta a ritenute
                      else
                        -- CM e Regime Forfettario e Legge 388 ma ha IM_NON_SOGG_RIT!=0
                          IF (SUBSTR(aRecEstrazione770.CD_TRATTAMENTO, 1, 4) in ('T255','T256','T257','T259','T159','T155','T156','T157','T260','T091','T191')) then
                            aImportoLordo := aRecEstrazione770.IM_LORDO - aRecEstrazione770.IM_NON_SOGG_RIT;
                                            aStringa := LPAD((aImportoLordo * 100), 12, '0') || aStringa2;
                            aStringa:= aStringa || '7';
                            aStringa:= aStringa ||  LPAD(((aRecEstrazione770.IM_LORDO - aRecEstrazione770.IM_NON_SOGG_RIT) * 100), 12, '0');-- somma non soggetta a ritenute

                            aImportoLordo := aRecEstrazione770.IM_NON_SOGG_RIT;
                                            aStringa_sdoppia := LPAD((aImportoLordo * 100), 12, '0') || aStringa2;
                            aStringa_sdoppia:= aStringa_sdoppia || '8';
                            aStringa_sdoppia:= aStringa_sdoppia ||  LPAD((aRecEstrazione770.IM_NON_SOGG_RIT * 100), 12, '0');-- somma non soggetta a ritenute
                          else
                            aStringa:= aStringa1||aStringa2  || '0';
                            aStringa:= aStringa ||  LPAD('0', 12, '0');--somma non soggetta a ritenute
                          end if;
                      end if;
                  end if;
                  Else
                  -- CM e Regime Forfettario e Legge 388
                      IF (SUBSTR(aRecEstrazione770.CD_TRATTAMENTO, 1, 4) in ('T255','T256','T257','T259','T159','T155','T156','T157','T260','T091','T191')) then
              aStringa:= aStringa1||aStringa2  || '7';
              aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_LORDO * 100), 12, '0');-- somma non soggetta a ritenute
              else
                      aStringa:= aStringa1||aStringa2  || '0';
                    aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_NON_SOGG_RIT * 100), 12, '0');-- somma non soggetta a ritenute
                    End if;
                  End if;
                  aStringa:= aStringa ||  LPAD((aRecEstrazione770.IMPONIBILE_FI * 100), 12, '0');-- imponibile
                  if aStringa_sdoppia is not null then
                    aStringa_sdoppia:= aStringa_sdoppia ||  LPAD((aRecEstrazione770.IMPONIBILE_FI * 100), 12, '0');-- imponibile
                  end if;
            else --'SY'
                   aStringa:= aStringa ||  LPAD('0', 12, '0');--lordo
                   aStringa:= aStringa ||  LPAD('0', 12, '0'); -- somma no rit a regime convezionale
                   aStringa:= aStringa || '0';
                   aStringa:= aStringa ||  LPAD('0', 12, '0');-- somma non soggetta a ritenute
                   aStringa:= aStringa ||  LPAD('0', 12, '0');-- imponibile
            end if;

            aStringa_interna_sdoppia := aStringa;

            aStringa := '';

            if(aRecEstrazione770.cd_quadro='SC') then
               IF  aRecEstrazione770.TI_RITENUTA = 'A' THEN
                    aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_RITENUTE * 100), 12, '0');  -- Ritenute a titolo di acconto
                    aStringa:= aStringa ||  LPAD('0', 12, '0');
               ELSE
                   aStringa:= aStringa ||  LPAD('0', 12, '0');
                   aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_RITENUTE * 100), 12, '0');   -- Ritenute a titolo di imposta
               END if;
            else -- SY
              aStringa:= aStringa ||  LPAD('0', 12, '0');-- Ritenute a titolo di acconto
              aStringa:= aStringa ||  LPAD('0', 12, '0');-- Ritenute a titolo di imposta
            end if;

             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Ritenute sospese
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Addizionale regionale a titolo d'acconto
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Addizionale regionale a titolo d'imposta
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Addizionale regionale sospesa
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Addizionale comunale a titolo d'acconto
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Addizionale comunale a titolo d'imposta
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Addizionale comunale sospesa
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Imponibile anni precedenti
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Ritenute anni precedenti
             if(aRecEstrazione770.cd_quadro='SC') then
                aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_NON_SOGG_INPS * 100), 12, '0');-- Spese Rimborsate(quota esente inps)
             else
                 aStringa:= aStringa ||  LPAD('0', 12, '0');-- Spese Rimborsate(quota esente inps)
             end if;
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Ritenute Rimborsate

             --???????
             if(aRecEstrazione770.cd_quadro='SC' and nvl(aRecEstrazione770.IM_CONTRIBUTI_ENTE,0)!= 0) then --da testare 21/02/2017
                --aStringa:= aStringa || 'A';--RPAD(' ', 1, ' ');-- codice ente previdenziale ??
                aStringa:=  aStringa || RPAD(SUBSTR('80078750587', 1, 16), 16, ' ');   -- codice fiscale  ente previdenziale
                aStringa:=  aStringa || RPAD('Istituto Nazionale Previdenza Sociale - INPS', 40, ' ');-- denominazione ente previdenziale ??
              else
                --aStringa:= aStringa || RPAD(' ', 1, ' ');-- codice ente previdenziale ??
                aStringa:= aStringa || RPAD(' ', 16, ' ');-- CF ente previdenziale ??
                aStringa:= aStringa || RPAD(' ', 40, ' ');-- denominazione ente previdenziale ??

              end if;
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- codice azienda ??
             aStringa:= aStringa || RPAD(' ', 1, ' ');-- categoria ??
             if(aRecEstrazione770.cd_quadro='SC') then
                 aStringa:= aStringa || LPAD((aRecEstrazione770.IM_CONTRIBUTI_ENTE * 100), 12, '0')||'+';-- Contributi a carico Ente
                 aStringa:= aStringa || LPAD((aRecEstrazione770.IM_CONTRIBUTI * 100), 12, '0')||'+';-- Contributi a carico Percipiente
                 aStringa:= aStringa ||' ';-- altri contributi ???-cambiato su scarto di data manager
                 aStringa:= aStringa || LPAD('0', 12, '0')||'+'; -- importo altri contributi
                 aStringa:= aStringa || LPAD((aRecEstrazione770.IM_CONTRIBUTI_ENTE* 100)+(aRecEstrazione770.IM_CONTRIBUTI * 100), 12, '0')||'+';-- contributi dovuti ???
                 aStringa:= aStringa || LPAD((aRecEstrazione770.IM_CONTRIBUTI_ENTE* 100)+(aRecEstrazione770.IM_CONTRIBUTI * 100), 12, '0')||'+';-- contributi versati???
             else --SY
                  aStringa:= aStringa || LPAD('0', 12, '0')||'+';
                  aStringa:= aStringa || LPAD('0', 12, '0')||'+';
                  aStringa:= aStringa ||' ';-- altri contributi ??? --cambiato su scarto di data manager
                  aStringa:= aStringa || LPAD('0', 12, '0')||'+';
                  aStringa:= aStringa || LPAD('0', 12, '0')||'+';
                  aStringa:= aStringa || LPAD('0', 12, '0')||'+';
             end if;

             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Somme corrisposte prima della data del fallimento
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Somme corrisposte dal curatore/commissario

             aStringa:= aStringa ||  RPAD(' ', 16, ' ');-- Redditi erogati da altri soggetti
             aStringa:= aStringa ||  LPAD('0', 120, '0');-- numerici non valorizzati

             aStringa:= aStringa || RPAD(' ', 16, ' ');-- CF operazioni straordinarie ??
             aStringa:= aStringa || RPAD(' ', 3200, ' '); -- spazio
             aStringa:= aStringa || RPAD(' ', 1, ' ');-- flag validazione
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- codice caricamento
             aStringa:= aStringa || RPAD(' ', 7, ' '); -- codice utente

             aStringa:= aStringa || RPAD(' ', 75, ' ');-- spazio
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 1
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 2
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 3

             aStringa_finale := aStringa_prima_sdoppia||aStringa_interna_sdoppia||aStringa;

             --pipe.send_message('sc1');
             IF LENGTH(aStringa_finale) != 4000 THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Errore in lunghezza file in output tipo record 770 lunghezza sc1 ' || LENGTH(aStringa_finale));
             END IF;
               IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                                 aRecBframeBlob.path,
                                 aRecBframeBlob.filename,
                                 mioCLOB,
                                 aStringa_finale);
                if aStringa_sdoppia is not null then
                   IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                                     aRecBframeBlob.path,
                                     aRecBframeBlob.filename,
                                     mioCLOB,
                                     aStringa_anag);
                    aStringa_finale := aStringa_prima_sdoppia||aStringa_sdoppia||aStringa;
                    IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                                     aRecBframeBlob.path,
                                     aRecBframeBlob.filename,
                                     mioCLOB,
                                     aStringa_finale);
                end if;

      -- nuovo sc2 21/02/2017
       if(aRecEstrazione770.cd_quadro='SY') then
             aStringa:=NULL;

             aStringa:= ' SC2';
             aStringa:= aStringa || '80054330586     ';  -- codice fiscale dichiatrante
             aStringa:= aStringa || '                ';  -- codice fiscale sostituto

             IF aTi_entita = 'F' THEN   --persona fisica
                aStringa:=aStringa || '1';
                aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');         -- codice fiscale
                aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 16), 16, ' ');   -- filler
                aStringa:= aStringa || '00';  -- progressivo rapporto Numerico di 2
                aStringa:= aStringa || '0';   -- Filler di 1 - vale zero
                aStringa:= aStringa || '0001';   -- Tipo posizione numerico di 4 - vale uno per il primo modulo??
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aCognome), 1, 60), 60, ' ');        -- cognome
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aNome), 1, 20), 20, ' ');           -- nome
             ELSE    --persona giuridica
                aStringa:=aStringa || '2';
                aStringa:=aStringa || RPAD(SUBSTR(aCodiceFiscale, 1, 16), 16, ' ');   -- codice fiscale
                aStringa:=aStringa || RPAD(SUBSTR(aSpazi, 1, 16), 16, ' ');   -- filler
                aStringa:= aStringa || '00';  -- progressivo rapporto Numerico di 2
                aStringa:= aStringa || '0';   -- Filler di 1 - vale zero
                aStringa:= aStringa || '0001';   -- Tipo posizione numerico di 4 - vale uno per il primo modulo??
                aStringa:=aStringa || RPAD(SUBSTR(Upper(aRagioneSociale), 1, 60), 60, ' ');    -- denominazione
                aStringa:= aStringa || RPAD(' ', 20, ' '); -- nome
             end if;

              aStringa:= aStringa || RPAD(' ', 16, ' ');-- CF operazioni straordinarie pignoramenti??
              aStringa:= aStringa || RPAD(' ', 16, ' ');-- CF operazioni esproprio ??

             -- pignoramenti   ????
             --*****

           if(aRecEstrazione770.cd_quadro='SY') then
                   aStringa:=aStringa || RPAD(SUBSTR(aRecEstrazione770.cf_pi_pignorato, 1, 16), 16, ' ');   -- codice fiscale pignorato
                   aStringa:= aStringa ||  LPAD(((aRecEstrazione770.im_Lordo -aRecEstrazione770.IM_NON_SOGG_RIT)* 100), 12, '0');   -- somma erogata
                   aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_RITENUTE * 100), 12, '0');  -- ritenuta operata
                   aStringa:= aStringa ||  LPAD((aRecEstrazione770.IM_NON_SOGG_RIT * 100), 12, '0');  -- ritenuta operata
             else
                 aStringa:= aStringa || RPAD(' ', 16, ' ');-- CF  pignorato??
                 aStringa:= aStringa ||  LPAD('0', 12, '0');-- Somme erogate
                 aStringa:= aStringa ||  LPAD('0', 12, '0');-- ritenute
                 aStringa:= aStringa ||  LPAD('0', 12, '0');-- Somme erogate non tassate
             end if;
             -- pignoramenti  riservato soggetto erogatore ????
             aStringa:= aStringa || RPAD(' ', 16, ' ');-- CF  pignorato??
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Somme erogate
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- ritenute
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Somme erogate non tassate

             -- somme corrisposta a titolo di indennità  ??
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Somme erogate
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- ritenute

             -- somme corrisposta altre indennità  ??
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Somme erogate
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- ritenute

             -- somme corrisposta esproprio  ??
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Somme erogate
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- ritenute

             -- somme corrisposta  altre indennità  e interessi??
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- Somme erogate
             aStringa:= aStringa ||  LPAD('0', 12, '0');-- ritenute

             aStringa:= aStringa || RPAD(' ', 3490, ' '); -- spazio
             aStringa:= aStringa || RPAD(' ', 1, ' ');-- flag validazione
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- codice caricamento
             aStringa:= aStringa || RPAD(' ', 7, ' '); -- codice utente

             aStringa:= aStringa || RPAD(' ', 74, ' ');-- spazio
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 1
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 2
             aStringa:= aStringa || RPAD(' ', 10, ' ');-- sezionamento 3
             --pipe.send_message('sc1');
             IF LENGTH(aStringa) != 4000 THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Errore in lunghezza file in output tipo record 770 lunghezza sc2 ' || LENGTH(aStringa));
             END IF;
                  IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                                 aRecBframeBlob.path,
                                 aRecBframeBlob.filename,
                                 mioCLOB,
                                 aStringa);
           end if;
      -- fine nuovo sc2
      ELSE
       --pipe.send_message('else nome '||aRecEstrazione770.nome||' '||aRecEstrazione770.CD_NAZIONE_770);
          --pipe.send_message('tipo A');
            IF aRecEstrazione770.TI_ENTITA IS NULL THEN
               aTi_entita :='G';
            ELSE
               aTi_entita:= aRecEstrazione770.TI_ENTITA;
            END IF;

            IF aRecEstrazione770.CODICE_FISCALE IS NULL THEN
               IF  aTi_entita = 'G' THEN
                  IF aRecEstrazione770.PARTITA_IVA IS NULL THEN
                      aContatore := aContatore + 1;
                      aCodiceFiscale := 'ESTERO*' || LPAD (aContatore, 9, '0');
                  ELSE
                      aCodiceFiscale := aRecEstrazione770.PARTITA_IVA;
                  END IF;
               ELSE
                  aContatore := aContatore + 1;
                  aCodiceFiscale := 'ESTERO*' || LPAD (aContatore, 9, '0');
               END IF;
            ELSE
               aCodiceFiscale := aRecEstrazione770.CODICE_FISCALE;
            END IF;

            IF aRecEstrazione770.COGNOME IS NULL THEN
               aCognome :=' ';
            ELSE
               aCognome:= aRecEstrazione770.COGNOME;
            END IF;

            IF aRecEstrazione770.NOME IS NULL THEN
               aNome :=' ';
            ELSE
               aNome:= aRecEstrazione770.NOME;
            END IF;

            IF aRecEstrazione770.TI_SESSO IS NULL THEN
               aSesso :=' ';
            ELSE
               aSesso:= aRecEstrazione770.TI_SESSO;
            END IF;

            IF aRecEstrazione770.DT_NASCITA IS NULL THEN
               aDataNascita:=0;
            ELSE
               aDataNascita:=TO_CHAR(aRecEstrazione770.DT_NASCITA, 'DD') ||
                             TO_CHAR(aRecEstrazione770.DT_NASCITA, 'MM') ||
                             TO_CHAR(aRecEstrazione770.DT_NASCITA, 'YYYY');
            END IF;

            IF aRecEstrazione770.DS_COMUNE_NASCITA IS NULL THEN
               aComuneNascita := ' ';
            ELSE
               aComuneNascita:= aRecEstrazione770.DS_COMUNE_NASCITA;
            END IF;

            IF aRecEstrazione770.CD_PROVINCIA_NASCITA IS NULL THEN
               aProvinciaNascita := ' ';
            ELSE
               aProvinciaNascita:= aRecEstrazione770.CD_PROVINCIA_NASCITA;
            END IF;

            IF aRecEstrazione770.CD_NAZIONE_770 IS NULL Or aRecEstrazione770.CD_NAZIONE_770='*' THEN
               aCittadinanzaStatoEstero := '0';
            ELSE
               aCittadinanzaStatoEstero:= aRecEstrazione770.CD_NAZIONE_770;
            END IF;

            IF aRecEstrazione770.RAGIONE_SOCIALE IS NULL THEN
               aRagioneSociale:= ' ';
            ELSE
               aRagioneSociale:= aRecEstrazione770.RAGIONE_SOCIALE;
            END IF;

            IF aRecEstrazione770.DS_COMUNE_FISCALE IS NULL THEN
               aComuneDomicilio := ' ';
            ELSE
               aComuneDomicilio:= aRecEstrazione770.DS_COMUNE_FISCALE;
            END IF;

            IF aRecEstrazione770.CD_PROVINCIA_FISCALE IS NULL THEN
               aProvinciaDomicilio := ' ';
            ELSE
               aProvinciaDomicilio:= aRecEstrazione770.CD_PROVINCIA_FISCALE;
            END IF;

            IF aRecEstrazione770.VIA_NUM_FISCALE IS NULL THEN
               aIndirizzo := ' ';
            ELSE
               aIndirizzo:= aRecEstrazione770.VIA_NUM_FISCALE;
            END IF;

            IF aRecEstrazione770.DS_COMUNE_FISCALE IS NULL THEN
               aCodiceComuneDomicilio := ' ';
            ELSE
               aCodiceComuneDomicilio:= aRecEstrazione770.DS_COMUNE_FISCALE;
            END IF;

            IF aRecEstrazione770.ID_FISCALE_ESTERO IS NULL THEN
               aIdentificativoFiscale := ' ';
            ELSE
               aIdentificativoFiscale:= aRecEstrazione770.ID_FISCALE_ESTERO;
            END IF;
            if (aRecEstrazione770.CF_PI_PIGNORATO is null) then
               aCodiceFiscalePignorato := ' ';
            else
               aCodiceFiscalePignorato := aRecEstrazione770.CF_PI_PIGNORATO;
            end if;
            if (aRecEstrazione770.CD_ANAG is null) then
               aCdAnag:= ' ';
            else
               aCdAnag := aRecEstrazione770.CD_ANAG;
            end if;

      END IF;


   END LOOP;

   CLOSE gen_cur;
   END;
END scriviFileQuadroSCSY;

-- =================================================================================================

END; -- PACKAGE END;
/
