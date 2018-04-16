--------------------------------------------------------
--  DDL for Package Body CNRCTB260
--------------------------------------------------------
  CREATE OR REPLACE PACKAGE BODY "CNRCTB260" AS
--==================================================================================================
-- Inserimento record per titolo report (comune a tutte le stampe IVA)
--==================================================================================================
PROCEDURE insTitColReportGenerico
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    aTitoloReport1 VARCHAR2,
    aTitoloReport2 VARCHAR2,
    aTitoloReport3 VARCHAR2,
    aDenominazioneEnte VARCHAR2,
    aPIvaEnte VARCHAR2,
    aEsercizioRif INTEGER,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2,
    aTipoReport VARCHAR2,
    aTipoRegistro VARCHAR2
   ) IS
   aStringaPartitaIva VARCHAR2(50);

BEGIN

   IF aPIvaEnte IS NOT NULL THEN
      aStringaPartitaIva:='Partita Iva ' || aPIvaEnte;
   ELSE
      aStringaPartitaIva:=NULL;
   END IF;

   -- Inserimento in base dati

   BEGIN

      INSERT INTO REPORT_GENERICO
             (id,
              chiave,
              tipo,
              sequenza,
              descrizione,
              attributo_1,
              attributo_2,
              attributo_3,
              importo_20,
              attributo_36,
              attributo_37,
              attributo_38,
              attributo_39,
              attributo_40)
      VALUES (repID,
              gruppoStm,
              'R',
              aSequenza,
              sottoGruppoStm,
              aTitoloReport1,
              aTitoloReport2,
              aTitoloReport3,
              aEsercizioRif,
              aDenominazioneEnte,
              aStringaPartitaIva,
              aTipoReport,
              aTipoRegistro,
              descrizioneGruppoStm);

   EXCEPTION

      WHEN DUP_VAL_ON_INDEX THEN
           IBMERR001.RAISE_ERR_GENERICO ('Chiave duplicata in inserimento titolo report su tabella REPORT_GENERICO');

   END;

END insTitColReportGenerico;

--==================================================================================================
-- Inserimento del frontespizio per il riepilogo sezionali in caso di stampa multipla degli stessi
--==================================================================================================
PROCEDURE insProspettiPerRegistri
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    aPasso INTEGER,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCodiceSezionale VARCHAR2,
    aDescrizioneSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoReport VARCHAR2,
    aTipoRegistro VARCHAR2,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2
   ) IS
   cv_min_fattura VARCHAR2(10);
   cv_max_fattura VARCHAR2(10);
   gen_cv GenericCurTyp;

BEGIN


   BEGIN

      -- Recupero intervallo numeri fattura per stampa copertina in caso di stampa multipla dei registri

      IF aPasso = 2 THEN

         IF aTipoRegistro = 'A' THEN

            OPEN gen_cv FOR

                 SELECT MIN(RPAD(protocollo_iva_generale,10,' ')),
                        MAX(RPAD(protocollo_iva_generale,10,' '))
                 FROM   V_REGISTRO_IVA_ACQUISTI
                 WHERE  cd_cds_origine = aCdCdsOrigine AND
                        cd_uo_origine = aCdUoOrigine AND
                        esercizio = aEsercizio AND
                        cd_tipo_sezionale = aCodiceSezionale AND
                        (data_registrazione BETWEEN aDataInizio AND aDataFine);
         ELSE

            OPEN gen_cv FOR

                 SELECT MIN(RPAD(protocollo_iva_generale,10,' ')),
                        MAX(RPAD(protocollo_iva_generale,10,' '))
                 FROM   V_REGISTRO_IVA_VENDITE
                 WHERE  cd_cds_origine = aCdCdsOrigine AND
                        cd_uo_origine = aCdUoOrigine AND
                        esercizio = aEsercizio AND
                        cd_tipo_sezionale = aCodiceSezionale AND
                        (data_registrazione BETWEEN aDataInizio AND aDataFine);

         END IF;

      END IF;

      LOOP

         FETCH gen_cv INTO
               cv_min_fattura,
               cv_max_fattura;

         EXIT WHEN gen_cv%NOTFOUND;

         aSequenza:=aSequenza + 20;

         BEGIN

            INSERT INTO VP_STM_REGISTRO_IVA
                   (id_report,
                    gruppo,
                    tipologia_riga,
                    sequenza,
                    sottogruppo,
                    cd_cds,
                    cd_uo,
                    cd_cds_origine,
                    cd_tipo_sezionale,
                    ti_fattura,
                    provvisorio_definitivo,
                    acquisto_vendita,
                    descrizione_gruppo)
            VALUES (repID,
                    gruppoStm,
                    'D',
                    aSequenza,
                    sottoGruppoStm,
                    cv_min_fattura,
                    cv_max_fattura,
                    'REGISTRATE DALL''UNITA'' SEZIONALE',
                    aCodiceSezionale,
                    aDescrizioneSezionale,
                    aTipoReport,
                    aTipoRegistro,
                    descrizioneGruppoStm);

         EXCEPTION

            WHEN DUP_VAL_ON_INDEX THEN
                 IBMERR001.RAISE_ERR_GENERICO
                    ('Chiave duplicata in inserimento riepilogo sezionali per registro su tabella REPORT_GENERICO');
         END;

      END LOOP;

      CLOSE gen_cv;

   END;

END insProspettiPerRegistri;

-- =================================================================================================
-- Stampa corpo del registro IVA sia acquisti che vendite
-- =================================================================================================
PROCEDURE insFatturePerRegistri
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    aPasso INTEGER,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCodiceSezionale VARCHAR2,
    aDescrizioneSezionale VARCHAR2,
    aTipoIstituzCommerc CHAR,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    aRistampa VARCHAR2,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2,
    aMonetaItalia VARCHAR2,
    aTipoReportStato VARCHAR2
   ) IS
   eseguiLock CHAR(1);
   sottoGruppoStm_new CHAR(1);
   descrizioneGruppoStm_new VARCHAR2(100);
   scritto CHAR(1);
   gen_cv GenericCurTyp;
   tiIntraBollaDoganale CHAR(1);
   importoIvaIndetraibile NUMBER(15,2);
   c_iva_esigibile  NUMBER(15,2);
BEGIN

   -----------------------------------------------------------------------------------------------
   -- Lettura fatture e scrittura in REPORT GENERICO dell'elenco fatture per stampa registri

   IF aTipoRegistro = 'A' THEN

      IF (aTipoReport = 'D' AND
          aRistampa = 'N') THEN

         -- Prima di inserire le fatture passive legate al sezionale, nella tabella REPORT_GENERICO
         -- bisogna protocollare le stesse.

         insProtFatturePassAuto(aCdCdsOrigine,
                                aCdUoOrigine,
                                aEsercizio,
                                aCodiceSezionale,
                                aTipoIstituzCommerc,
                                aTipoReport,
                                aDataInizio,
                                aDataFine,
                                'FATTURA_P');

       END IF;

       IF aRistampa = 'N' THEN

         -- STAMPA NORMALE
         OPEN gen_cv FOR

              SELECT *
              FROM   V_REGISTRO_IVA_ACQUISTI
              WHERE  cd_cds_origine = aCdCdsOrigine AND
                     cd_uo_origine  = aCdUoOrigine AND
                     esercizio = aEsercizio AND
                     cd_tipo_sezionale  = aCodiceSezionale AND
                     (data_registrazione BETWEEN aDataInizio AND aDataFine)
              ORDER BY cd_cds_origine,
                       cd_uo_origine,
                       esercizio,
                       data_registrazione,
                       numero_progressivo;

      ELSE

         -- RISTAMPA

         OPEN gen_cv FOR

              SELECT B.*
              FROM   REPORT_DETTAGLIO A,
                     V_REGISTRO_IVA_ACQUISTI B
              WHERE  A.cd_cds = aCdCdsOrigine AND
                     A.cd_unita_organizzativa = aCdUoOrigine AND
                     A.esercizio = aEsercizio AND
                     A.tipo_report = aTipoReportStato AND
                     A.cd_tipo_sezionale = aCodiceSezionale AND
                     A.data_inizio = aDataInizio AND
                     A.data_fine = aDataFine AND
                     B.cd_cds = A.cd_cds_altro AND
                     B.cd_unita_organizzativa = A.cd_uo_altro AND
                     B.esercizio = A.esercizio AND
                     B.cd_tipo_sezionale = A.cd_tipo_sezionale AND
                     B.cd_cds_origine = A.cd_cds AND
                     B.cd_uo_origine = A.cd_unita_organizzativa AND
                     B.numero_progressivo = A.pg_documento
              ORDER BY B.cd_cds_origine,
                       B.cd_uo_origine,
                       B.esercizio,
                       B.data_registrazione,
                       B.numero_progressivo;
      END IF;

   ELSE

      IF (aTipoReport = 'D' AND
          aRistampa = 'N') THEN

         InsProtFatturePassAuto(aCdCdsOrigine,
                                aCdUoOrigine,
                                aEsercizio,
                                aCodiceSezionale,
                                aTipoIstituzCommerc,
                                aTipoReport,
                                aDataInizio,
                                aDataFine,
                                'AUTOFATTURA');

      END IF;

      IF aRistampa = 'N' THEN

         -- STAMPA NORMALE

         OPEN gen_cv FOR

              SELECT *
              FROM   V_REGISTRO_IVA_VENDITE
              WHERE  cd_cds_origine = aCdCdsOrigine AND
                     cd_uo_origine = aCdUoOrigine AND
                     esercizio = aEsercizio AND
                     cd_tipo_sezionale = aCodiceSezionale AND
                     (data_emissione BETWEEN aDataInizio AND aDataFine)
              ORDER BY cd_cds_origine,
                       cd_uo_origine,
                       esercizio,
                       data_emissione,
                       protocollo_iva_generale;
      ELSE

         -- RISTAMPA

         OPEN gen_cv FOR

              SELECT B.*
              FROM   REPORT_DETTAGLIO A,
                     V_REGISTRO_IVA_VENDITE B
              WHERE  A.cd_cds = aCdCdsOrigine AND
                     A.cd_unita_organizzativa = aCdUoOrigine AND
                     A.esercizio = aEsercizio AND
                     A.tipo_report = aTipoReportStato AND
                     A.cd_tipo_sezionale = aCodiceSezionale AND
                     A.data_inizio = aDataInizio AND
                     A.data_fine = aDataFine AND
                     B.cd_cds = A.cd_cds_altro AND
                     B.cd_unita_organizzativa = A.cd_uo_altro AND
                     B.esercizio = A.esercizio AND
                     B.cd_tipo_sezionale = A.cd_tipo_sezionale AND
                     B.cd_cds_origine = A.cd_cds AND
                     B.cd_uo_origine = A.cd_unita_organizzativa AND
                     B.numero_progressivo = A.pg_documento
              ORDER BY B.cd_cds_origine,
                       B.cd_uo_origine,
                       B.esercizio,
                       B.data_emissione,
                       B.protocollo_iva_generale;

      END IF;

   END IF;

   -- fetch del cursore fatture e insert in REPORT_GENERICO

   LOOP

      FETCH gen_cv INTO
            cv_cd_cds,
            cv_cd_uo,
            cv_esercizio,
            cv_cd_cds_origine,
            cv_cd_uo_origine,
            cv_cd_tipo_sezionale,
            cv_ti_fattura,
            cv_data_registrazione,
            cv_numero_progressivo,
            cv_data_emissione,
            cv_numero_fattura,
            cv_protocollo_iva,
            cv_protocollo_iva_gen,
            cv_comm_ist_testata,
            cv_codice_anagrafico,
            cv_ragione_sociale,
            cv_imponibile_dettaglio,
            cv_iva_dettaglio,
            cv_iva_indetraibile_dettaglio,
            cv_totale_dettaglio,
            cv_comm_ist_dettaglio,
            cv_codice_iva,
            cv_percentuale_iva,
            cv_descrizione_iva,
            cv_fl_iva_detraibile,
            cv_percentuale_iva_detraibile,
            cv_gruppo_iva,
            cv_descrizione_gruppo_iva,
            cv_intra_ue,
            cv_bolla_doganale,
            cv_spedizioniere,
            cv_codice_valuta,
            cv_importo_valuta,
            cv_esigibilita_diff,
            cv_data_esigibilita_diff,
            cv_tipo_documento_ft_pas,
            cv_extra_ue,
            cv_split_payment;

      EXIT WHEN gen_cv%NOTFOUND;
      aSequenza:=aSequenza + 20;

      -- Normalizza esposizione degli importi in divisa estera

      IF cv_codice_valuta = aMonetaItalia THEN
         cv_codice_valuta:=NULL;
         cv_importo_valuta:=0;
      END IF;

      -- Valorizzazione del flag intra o bolla doganale gestito solo per registro acquisti

      IF aTipoRegistro = 'A' THEN
         IF    cv_intra_ue = 'Y' THEN
               tiIntraBollaDoganale:='I';
         ELSIF cv_bolla_doganale = 'Y' THEN
               tiIntraBollaDoganale:='B';
         ELSIF cv_spedizioniere = 'Y' THEN
               tiIntraBollaDoganale:='S';
         Elsif cv_extra_ue ='Y' Then
               tiIntraBollaDoganale:='E';
         else
               tiIntraBollaDoganale:='N';
         END IF;
      ELSE
         IF    cv_intra_ue = 'Y' THEN
               tiIntraBollaDoganale:='I';
         ELSE
               tiIntraBollaDoganale:='N';
         END IF;
      END IF;

      -- Normalizzazione del valore di iva indetraibile per l'arrotondamento corretto
      -- in base al fatto che l'esercizio sia euro o meno
      c_iva_esigibile:=0;
      IF aTipoRegistro = 'A' THEN
         cv_iva_indetraibile_dettaglio:=TRUNC(cv_iva_indetraibile_dettaglio,2);

            If cv_esigibilita_diff = 'N' THEN
               c_iva_esigibile:=cv_iva_dettaglio;
            ELSE
               IF (cv_data_esigibilita_diff IS NOT NULL AND
                   (cv_data_esigibilita_diff BETWEEN aDataInizio AND aDataFine)) THEN
                     c_iva_esigibile:=cv_iva_dettaglio;
               END IF;
            END IF;
      END IF;
      BEGIN

         INSERT INTO VP_STM_REGISTRO_IVA
                (id_report,
                 gruppo,
                 tipologia_riga,
                 sequenza,
                 sottogruppo,
                 cd_cds,
                 cd_uo,
                 esercizio,
                 cd_cds_origine,
                 cd_uo_origine,
                 cd_tipo_sezionale,
                 ti_fattura,
                 data_registrazione,
                 numero_progressivo,
                 data_emissione,
                 numero_fattura,
                 protocollo_iva,
                 protocollo_iva_generale,
                 comm_ist_testata,
                 codice_anagrafico,
                 ragione_sociale,
                 imponibile,
                 iva,
                 iva_indetraibile,
                 totale,
                 comm_ist_dettaglio,
                 codice_iva,
                 percentuale_iva,
                 descrizione_iva,
                 fl_iva_detraibile,
                 percentuale_iva_detraibile,
                 gruppo_iva,
                 descrizione_gruppo_iva,
                 intra_ue,
                 codice_valuta,
                 importo_valuta,
                 esigibilita_diff,
                 data_esigibilita_diff,
                 provvisorio_definitivo,
                 acquisto_vendita,
                 descrizione_gruppo,
                 iva_esigibile,
                 fl_split_payment)
         VALUES (repID,
                 gruppoStm,
                 'D',
                 aSequenza,
                 sottoGruppoStm,
                 cv_cd_cds,
                 cv_cd_uo,
                 cv_esercizio,
                 cv_cd_cds_origine,
                 cv_cd_uo_origine,
                 cv_cd_tipo_sezionale,
                 cv_ti_fattura,
                 cv_data_registrazione,
                 cv_numero_progressivo,
                 cv_data_emissione,
                 cv_numero_fattura,
                 cv_protocollo_iva,
                 cv_protocollo_iva_gen,
                 cv_comm_ist_testata,
                 cv_codice_anagrafico,
                 cv_ragione_sociale,
                 cv_imponibile_dettaglio,
                 cv_iva_dettaglio,
                 cv_iva_indetraibile_dettaglio,
                 cv_totale_dettaglio,
                 cv_comm_ist_dettaglio,
                 cv_codice_iva,
                 cv_percentuale_iva,
                 cv_descrizione_iva,
                 cv_fl_iva_detraibile,
                 cv_percentuale_iva_detraibile,
                 cv_gruppo_iva,
                 cv_descrizione_gruppo_iva,
                 tiIntraBollaDoganale,
                 cv_codice_valuta,
                 cv_importo_valuta,
                 cv_esigibilita_diff,
                 cv_data_esigibilita_diff,
                 aTipoReport,
                 aTipoRegistro,
                 descrizioneGruppoStm,
                 c_iva_esigibile,
                 cv_split_payment);

      EXCEPTION

         WHEN DUP_VAL_ON_INDEX THEN
              IBMERR001.RAISE_ERR_GENERICO
                 ('Chiave duplicata in inserimento dettaglio fatture per stampa registro su tabella REPORT_GENERICO');

      END;

   END LOOP;

   CLOSE gen_cv;

   -------------------------------------------------------------------------------------------------
   -- Scrittura su REPORT_GENERICO del riepilogo codici iva e gruppi

   sottoGruppoStm_new:='R';
   descrizioneGruppoStm_new:='RIEPILOGO CODICI IVA GRUPPO B';
   aSequenza:=aSequenza + 20;

   insRiepilogoIvaPerRegistri(repID,
                              aSequenza,
                              aPasso,
                              aEsercizio,
                              gruppoStm,
                              sottoGruppoStm_new,
                              descrizioneGruppoStm_new,
                              aCdCdsOrigine,
                              aCdUoOrigine,
                              aCodiceSezionale);


END insFatturePerRegistri;

-- =================================================================================================
-- Assegnazione del numero di protocollo IVA per fatture passive e autofatture in sede di stampa
-- registri IVA
-- =================================================================================================
PROCEDURE insProtFatturePassAuto
   (
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aTiIstituzCommerc CHAR,
    aTipoReport VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTiDocumento VARCHAR2
   ) IS
   aProtocolloFT NUMBER(10);
   aProtocolloNC NUMBER(10);
   aProtocolloND NUMBER(10);
   aProtocolloPG NUMBER(10);
   aProtocolloTmp NUMBER(10);
   aProtocolloGenTmp NUMBER(10);
   aStatement VARCHAR2(2000);
   aTiStatementSql VARCHAR2(2);
   eseguiLock CHAR(1);
   curs_doc GenericCurTyp;

BEGIN
-- Protocollo a -1 indica sezionale non definito o non compatibile
   aProtocolloFT:=-1;
   aProtocolloNC:=-1;
   aProtocolloND:=-1;
   aProtocolloPG:=-1;
   aTiStatementSql:='01';

   -- Il lock sui sezionali e fatture è fatto solo per registro definitivo

   IF aTipoReport = 'D' THEN
      eseguiLock:='Y';
   ELSE
      eseguiLock:='N';
   END IF;

   ----------------------------------------------------------------------------------------------
   -- Recupero del valore corrente dei protocolli di numerazione per il sezionale in elaborazione.

   CNRCTB120.getProtCorrenteSezionale(aCdCdsOrigine,
                                      aCdUoOrigine,
                                      aEsercizio,
                                      aCdTipoSezionale,
                                      eseguiLock,
                                      aProtocolloFT,
                                      aProtocolloNC,
                                      aProtocolloND,
                                      aProtocolloPG);

   -- Verifico l'esistenza di tutti i tipi di sezionali relativi al codice sezionale in esame.
   -- Nessun protocollo può valere -1

   IF (aProtocolloFT = -1 OR
       aProtocolloNC = -1 OR
       aProtocolloND = -1 OR
       aProtocolloPG = -1) THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Per il sezionale ' || aCdTipoSezionale || ' manca la definizione di qualche tipologia in numerazione');
   END IF;

   ----------------------------------------------------------------------------------------------
   -- Ciclo di protocollazione documenti.

   -- Composizione dello statement

   aStatement:=componiStatementSql(aCdCdsOrigine,
                                   aCdUoOrigine,
                                   aEsercizio,
                                   aCdTipoSezionale,
                                   aTiIstituzCommerc,
                                   aTipoReport,
                                   aDataInizio,
                                   aDataFine,
                                   aTiDocumento,
                                   eseguiLock,
                                   aTiStatementSql);

   BEGIN

      OPEN curs_doc FOR
           aStatement;

      LOOP

         FETCH curs_doc INTO
               cv_cd_cds,
               cv_cd_uo,
               cv_esercizio,
               cv_numero_progressivo,
               cv_data_registrazione,
               cv_ti_fattura;

         EXIT WHEN curs_doc%NOTFOUND;

         -- Incremento il protocollo iva dipendente dal tipo documento

         IF    cv_ti_fattura = CNRCTB100.TI_FATT_FATTURA THEN
               aProtocolloFT:=aProtocolloFT + 1;
               aProtocolloTmp:=aProtocolloFT;
         ELSIF cv_ti_fattura = CNRCTB100.TI_FATT_NOTA_C THEN
               aProtocolloNC:=aProtocolloNC + 1;
               aProtocolloTmp:=aProtocolloNC;
         ELSIF cv_ti_fattura = CNRCTB100.TI_FATT_NOTA_D THEN
               aProtocolloND:=aProtocolloND + 1;
               aProtocolloTmp:=aProtocolloND;
         END IF;

         -- Incremento il protocollo iva generale

         aProtocolloPG:=aProtocolloPG + 1;
         aProtocolloGenTmp:=aProtocolloPG;

         -- Aggiornamento del protocollo iva sul documento

         IF    aTiDocumento ='AUTOFATTURA' THEN

               UPDATE AUTOFATTURA
               SET    protocollo_iva = aProtocolloTmp,
                      protocollo_iva_generale = aProtocolloGenTmp
               WHERE  cd_cds = cv_cd_cds AND
                      cd_unita_organizzativa = cv_cd_uo AND
                      esercizio = cv_esercizio AND
                      pg_autofattura = cv_numero_progressivo;

         ELSIF aTiDocumento ='FATTURA_P' THEN

               UPDATE FATTURA_PASSIVA
               SET    protocollo_iva = aProtocolloTmp,
                      protocollo_iva_generale = aProtocolloGenTmp
               WHERE  cd_cds = cv_cd_cds AND
                      cd_unita_organizzativa = cv_cd_uo AND
                      esercizio = cv_esercizio AND
                      pg_fattura_passiva = cv_numero_progressivo;

         END IF;

         -- Aggiornamento del valore corrente per il sezionale in elaborazione

         IF aTipoReport = 'D' THEN

            CNRCTB120.upgSezionalePgCorrente(aCdCdsOrigine,
                                             aCdUoOrigine,
                                             aEsercizio,
                                             aCdTipoSezionale,
                                             aProtocolloFT,
                                             aProtocolloNC,
                                             aProtocolloND,
                                             aProtocolloPG);
         END IF;

      END LOOP;

      CLOSE curs_doc;

   END;

END InsProtFatturePassAuto;

-- =================================================================================================
-- Composizione di statement sql di tipo dinamico
-- aTiStatementSql = '01' --> SELECT per aggiornamento protocolli sui documenti
-- =================================================================================================
FUNCTION componiStatementSql
   (
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aTiIstituzCommerc CHAR,
    aTipoReport VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTiDocumento VARCHAR2,
    eseguiLock CHAR,
    aTiStatementSql VARCHAR2
   ) RETURN VARCHAR2 IS
   aStatement VARCHAR2(2000);
   aDataDa VARCHAR2(100);
   aDataA VARCHAR2(100);

BEGIN

   ----------------------------------------------------------------------------------------------
   -- SELECT per aggiornamento protocolli sui documenti

   IF    aTiStatementSql = '01' THEN

         -- Composizione date

         aDataDa:='TO_DATE (' || '''' || TO_CHAR(aDataInizio,'DDMMYYYY') || '''' || ',''DDMMYYYY'')';
         aDataA:='TO_DATE (' || '''' || TO_CHAR(aDataFine,'DDMMYYYY') || '''' || ',''DDMMYYYY'')';

         IF    aTiDocumento ='AUTOFATTURA' THEN

               aStatement:='SELECT A.cd_cds, A.cd_unita_organizzativa, A.esercizio, ' ||
                                  'A.pg_autofattura, A.dt_registrazione, A.ti_fattura ' ||
                           'FROM   AUTOFATTURA A ';
         ELSIF aTiDocumento ='FATTURA_P' THEN
               aStatement:='SELECT A.cd_cds, A.cd_unita_organizzativa, A.esercizio, ' ||
                                  'A.pg_fattura_passiva, A.dt_registrazione, A.ti_fattura ' ||
                           'FROM   FATTURA_PASSIVA A ';
         END IF;

         aStatement:=aStatement ||
                    'WHERE  A.cd_cds_origine = ' || '''' || aCdCdsOrigine || '''' || ' AND ' ||
                           'A.cd_uo_origine = ' || '''' || aCdUoOrigine || '''' || ' AND ' ||
                           'A.esercizio = ' || aEsercizio  || ' AND ' ||
                           'A.cd_tipo_sezionale = ' || '''' || aCdTipoSezionale || '''' || ' AND ' ||
                           '(A.dt_registrazione BETWEEN ' || aDataDa || ' AND ' ||  aDataA || ') AND ' ||
                           'A.stato_cofi != ' || '''A''' || ' AND ';

         -- Controllo il tipo commerciale o meno del sezionale sul documento

         IF aTiIstituzCommerc = 'C' THEN
            aStatement:=aStatement ||
                        '(A.ti_istituz_commerc = ' ||  '''C''' || ' OR ' ||
                         'A.ti_istituz_commerc = ' ||  '''P''' || ')';
         ELSE
            aStatement:=aStatement || 'A.ti_istituz_commerc = ' ||  '''I''';
         END IF;

         aStatement:=aStatement || ' ' || 'ORDER BY 1, 2, 3, 5, 4';

   END IF;

   -- Aggiungo l'eventuale istruzione per lock

   IF aTipoReport = 'D' THEN
      aStatement:=aStatement || ' FOR UPDATE NOWAIT';
   END IF;

   RETURN aStatement;

END componiStatementSql;

-- =================================================================================================
-- Inserimento dati delle fatture nel report generico
-- =================================================================================================
PROCEDURE insRiepilogoIvaPerRegistri
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    aPasso INTEGER,
    aEsercizio NUMBER,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aCdTipoSezionale VARCHAR2
   ) IS
   progressivo INTEGER;
   scritto CHAR(1);
   gen_cv GenericCurTyp;
   gen_cv_a GenericCurTyp;
   imponibile_totale NUMBER(15,2);
   iva_totale NUMBER(15,2);
   iva_indetraibile_totale NUMBER(15,2);
   totale_totale NUMBER(15,2);
   iva_esigibile  NUMBER(15,2):=0;
BEGIN

   imponibile_totale:=0;
   iva_totale:=0;
   iva_indetraibile_totale:=0;
   totale_totale:=0;

   -------------------------------------------------------------------------------------------------
   -- Scrittura su REPORT_GENERICO del riepilogo codici iva e gruppi

   BEGIN

   -- Aggregazione per codice Sezionale e Gruppo

      IF aCdTipoSezionale IS NULL THEN

         OPEN gen_cv FOR

              SELECT gruppo_iva,
                     descrizione_gruppo_iva,
                     provvisorio_definitivo,
                     acquisto_vendita,
                     SUM(imponibile),
                     SUM(iva),
                     SUM(iva_indetraibile),
                     SUM(totale),
                     Sum(iva_esigibile)
              FROM   VP_STM_REGISTRO_IVA
              WHERE  id_report = repID AND
                     gruppo = 'B' AND
                     tipologia_riga = 'D'
              GROUP BY gruppo_iva,
                       descrizione_gruppo_iva,
                       provvisorio_definitivo,
                       acquisto_vendita;

      ELSE

         OPEN gen_cv FOR

              SELECT GRUPPO_IVA,
                     descrizione_gruppo_iva,
                     provvisorio_definitivo,
                     acquisto_vendita,
                     SUM(imponibile),
                     SUM(iva),
                     SUM(iva_indetraibile),
                     SUM(totale),
                     Sum(iva_esigibile)
              FROM   VP_STM_REGISTRO_IVA
              WHERE  id_report = repID AND
                     gruppo = 'B' AND
                     tipologia_riga = 'D' AND
                     cd_tipo_sezionale = aCdTipoSezionale
              GROUP BY gruppo_iva,
                       descrizione_gruppo_iva,
                       provvisorio_definitivo,
                       acquisto_vendita;
      END IF;

      -- fetch del cursore importi raggruppati per gruppo

      LOOP

         FETCH gen_cv INTO
               cv_gruppo_iva,
               cv_descrizione_gruppo_iva,
               cv_provvisorio_definitivo,
               cv_acquisto_vendita,
               cv_imponibile_dettaglio,
               cv_iva_dettaglio,
               cv_iva_indetraibile_dettaglio,
               cv_totale_dettaglio,
               cv_iva_esigibile;

         EXIT WHEN gen_cv%NOTFOUND;

         -- ciclo per recupero importi per codice iva in base al gruppo

         IF aCdTipoSezionale IS NULL THEN

            OPEN gen_cv_a FOR

                 SELECT codice_iva,
                        descrizione_iva,
                        gruppo_iva,
                        provvisorio_definitivo,
                        acquisto_vendita,
                        SUM(imponibile),
                        SUM(iva),
                        SUM(iva_indetraibile),
                        SUM(totale),
                        Sum(iva_esigibile)
                 FROM   VP_STM_REGISTRO_IVA
                 WHERE  id_report = repID AND
                        gruppo = 'B' AND
                        tipologia_riga = 'D' AND
                        gruppo_iva = cv_gruppo_iva
                 GROUP BY codice_iva,
                          descrizione_iva,
                          gruppo_iva,
                          provvisorio_definitivo,
                          acquisto_vendita
                 ORDER BY codice_iva;

         ELSE

            OPEN gen_cv_a FOR

                 SELECT codice_iva,
                        descrizione_iva,
                        gruppo_iva,
                        provvisorio_definitivo,
                        acquisto_vendita,
                        SUM(imponibile),
                        SUM(iva),
                        SUM(iva_indetraibile),
                        SUM(totale),
                        Sum(iva_esigibile)
                 FROM   VP_STM_REGISTRO_IVA
                 WHERE  id_report = repID AND
                        gruppo = 'B' AND
                        tipologia_riga = 'D' AND
                        cd_tipo_sezionale = aCdTipoSezionale AND
                        gruppo_iva = cv_gruppo_iva
                 GROUP BY codice_iva,
                          descrizione_iva,
                          gruppo_iva,
                          provvisorio_definitivo,
                          acquisto_vendita
                 ORDER BY codice_iva;

         END IF;

         -- Inserimento su REPORT_GENERICO del dettaglio per codice IVA

         LOOP

            aSequenza:=aSequenza + 20;

            FETCH gen_cv_a INTO
                  cv_codice_iva_a,
                  cv_descrizione_iva_a,
                  cv_gruppo_iva_a,
                  cv_provvisorio_definitivo_a,
                  cv_acquisto_vendita_a,
                  cv_imponibile_a,
                  cv_iva_a,
                  cv_iva_indetraibile_a,
                  cv_totale_a,
                  cv_iva_esigibile;

            EXIT WHEN gen_cv_a%NOTFOUND;

            BEGIN

               INSERT INTO VP_STM_REGISTRO_IVA
                      (id_report,
                       gruppo,
                       tipologia_riga,
                       sequenza,
                       sottogruppo,
                       cd_cds_origine,
                       cd_uo_origine,
                       cd_tipo_sezionale,
                       imponibile,
                       iva,
                       iva_indetraibile,
                       totale,
                       provvisorio_definitivo,
                       acquisto_vendita,
                       descrizione_gruppo,
                       iva_esigibile)
               VALUES (repID,
                       gruppoStm,
                       'P',
                       aSequenza,
                       sottoGruppoStm,
                       cv_codice_iva_a,
                       cv_descrizione_iva_a,
                       aCdTipoSezionale,
                       cv_imponibile_a,
                       cv_iva_a,
                       cv_iva_indetraibile_a,
                       cv_totale_a,
                       cv_provvisorio_definitivo_a,
                       cv_acquisto_vendita_a,
                       descrizioneGruppoStm,
                       cv_iva_esigibile);

            EXCEPTION

               WHEN DUP_VAL_ON_INDEX THEN
                    IBMERR001.RAISE_ERR_GENERICO
                       ('Chiave duplicata in inserimento riepilogo per aliquota in stampa registro su tabella REPORT_GENERICO');

            END;

         END LOOP;

         -- close del cursore fatture

         CLOSE gen_cv_a;

         -- Inserimento del totale di gruppo

         aSequenza:=aSequenza + 20;

         BEGIN

            imponibile_totale:=imponibile_totale + cv_imponibile_dettaglio;
            iva_totale:=iva_totale + cv_iva_dettaglio;
            iva_indetraibile_totale:=iva_indetraibile_totale + cv_iva_indetraibile_dettaglio;
            totale_totale:=totale_totale + cv_totale_dettaglio;
            iva_esigibile:=iva_esigibile + cv_iva_esigibile;
            INSERT INTO VP_STM_REGISTRO_IVA
                   (id_report,
                    gruppo,
                    tipologia_riga,
                    sequenza,
                    sottogruppo,
                    cd_uo_origine,
                    cd_tipo_sezionale,
                    imponibile,
                    iva,
                    iva_indetraibile,
                    totale,
                    provvisorio_definitivo,
                    acquisto_vendita,
                    descrizione_gruppo,
                    iva_esigibile)
            VALUES (repID,
                    gruppoStm,
                    'T',
                    aSequenza,
                    sottoGruppoStm,
                    'TOTALE GRUPPO ' || cv_gruppo_iva || ' ' || cv_descrizione_gruppo_iva,
                    aCdTipoSezionale,
                    cv_imponibile_dettaglio,
                    cv_iva_dettaglio,
                    cv_iva_indetraibile_dettaglio,
                    cv_totale_dettaglio,
                    cv_provvisorio_definitivo,
                    cv_acquisto_vendita,
                    descrizioneGruppoStm,
                    cv_iva_esigibile);

         EXCEPTION

            WHEN DUP_VAL_ON_INDEX THEN
                 IBMERR001.RAISE_ERR_GENERICO
                    ('Chiave duplicata in inserimento riepilogo per aliquota in stampa registro su tabella REPORT_GENERICO');

         END;

      END LOOP;

      CLOSE gen_cv;

   END;

   -------------------------------------------------------------------------------------------------
   -- Scrittura totale generale del riepilogo

   aSequenza:=aSequenza + 20;

   BEGIN

      INSERT INTO VP_STM_REGISTRO_IVA
             (id_report,
              gruppo,
              tipologia_riga,
              sequenza,
              sottogruppo,
              cd_uo_origine,
              cd_tipo_sezionale,
              imponibile,
              iva,
              iva_indetraibile,
              totale,
              provvisorio_definitivo,
              acquisto_vendita,
              descrizione_gruppo,
              iva_esigibile)
      VALUES (repID,
              gruppoStm,
              'T',
              aSequenza,
              sottoGruppoStm,
              'TOTALE GENERALE',
              aCdTipoSezionale,
              imponibile_totale,
              iva_totale,
              iva_indetraibile_totale,
              totale_totale,
              cv_provvisorio_definitivo,
              cv_acquisto_vendita,
              descrizioneGruppoStm,
              iva_esigibile);

   EXCEPTION

      WHEN DUP_VAL_ON_INDEX THEN
           IBMERR001.RAISE_ERR_GENERICO
              ('Chiave duplicata in inserimento riepilogo per aliquota in stampa registro su tabella REPORT_GENERICO');

   END;

END insRiepilogoIvaPerRegistri;

-- =================================================================================================
-- Inserimento nella tabella REPORT DETTAGLIO delle fatture oggetto di stampa definitiva
-- =================================================================================================
PROCEDURE insDettaglioPerRegistri
   (
    repID INTEGER,
    aPasso INTEGER,
    descrizioneSezionale VARCHAR2,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCodiceSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    aUtente VARCHAR2,
    aTipoReportStato VARCHAR2
   ) IS
   dataOdierna DATE;
   aRecReportDettaglio REPORT_DETTAGLIO%ROWTYPE;
   gen_cv GenericCurTyp;

BEGIN

   aRecReportDettaglio:=NULL;
   dataOdierna:=sysdate;
   aRecReportDettaglio.tipo_report:=aTipoReportStato;
   aRecReportDettaglio.data_inizio:=aDataInizio;
   aRecReportDettaglio.data_fine:=aDataFine;
   aRecReportDettaglio.dacr:=dataOdierna;
   aRecReportDettaglio.utcr:=aUtente;
   aRecReportDettaglio.duva:=dataOdierna;
   aRecReportDettaglio.utuv:=aUtente;
   aRecReportDettaglio.pg_ver_rec:=1;

   -------------------------------------------------------------------------------------------------
   -- Lettura fatture e scrittura in REPORT DETTAGLIO dell'elenco fatture per stampa registri

   BEGIN

      IF aTipoRegistro = 'A' THEN

         -- Registro IVA acquisti

         OPEN gen_cv FOR

              SELECT DISTINCT cd_cds,
                     cd_unita_organizzativa,
                     cd_cds_origine,
                     cd_uo_origine,
                     esercizio,
                     cd_tipo_sezionale,
                     CNRCTB100.TI_FATTURA_PASSIVA,
                     data_registrazione,
                     numero_progressivo,
                     data_emissione,
                     protocollo_iva_generale
              FROM   V_REGISTRO_IVA_ACQUISTI
              WHERE  cd_cds_origine = aCdCdsOrigine AND
                     cd_uo_origine  = aCdUoOrigine AND
                     esercizio = aEsercizio AND
                     cd_tipo_sezionale  = aCodiceSezionale AND
                     (data_registrazione BETWEEN aDataInizio AND aDataFine)
              ORDER BY cd_cds_origine,
                       cd_uo_origine,
                       esercizio,
                       data_registrazione,
                       numero_progressivo;

      ELSE

         -- Registro IVA Vendite

         OPEN gen_cv FOR

              SELECT DISTINCT cd_cds,
                     cd_unita_organizzativa,
                     cd_cds_origine,
                     cd_uo_origine,
                     esercizio,
                     cd_tipo_sezionale,
                     DECODE(tipo_documento_ft_pas, NULL, CNRCTB100.TI_FATTURA_ATTIVA, CNRCTB100.TI_AUTOFATTURA),
                     data_registrazione,
                     numero_progressivo,
                     data_emissione,
                     protocollo_iva_generale
              FROM   V_REGISTRO_IVA_VENDITE
              WHERE  cd_cds_origine = aCdCdsOrigine AND
                     cd_uo_origine = aCdUoOrigine AND
                     esercizio = aEsercizio AND
                     cd_tipo_sezionale = aCodiceSezionale AND
                     (data_emissione BETWEEN aDataInizio AND aDataFine)
              ORDER BY cd_cds_origine,
                       cd_uo_origine,
                       esercizio,
                       data_emissione,
                       protocollo_iva_generale;

      END IF;

      -- fetch del cursore fatture e insert in REPORT_DETTAGLIO

      LOOP

         FETCH gen_cv INTO
               cv_cd_cds,
               cv_cd_uo,
               cv_cd_cds_origine,
               cv_cd_uo_origine,
               cv_esercizio,
               cv_cd_tipo_sezionale,
               cv_tipo_documento,
               cv_data_registrazione,
               cv_numero_progressivo,
               cv_data_emissione,
               cv_protocollo_iva_gen;

         EXIT WHEN gen_cv%NOTFOUND;

         aRecReportDettaglio.cd_cds:=cv_cd_cds_origine;
         aRecReportDettaglio.cd_unita_organizzativa:=cv_cd_uo_origine;
         aRecReportDettaglio.esercizio:=cv_esercizio;
         aRecReportDettaglio.cd_tipo_sezionale:=cv_cd_tipo_sezionale;
         aRecReportDettaglio.ti_documento:=cv_tipo_documento;
         aRecReportDettaglio.pg_documento:=cv_numero_progressivo;
         aRecReportDettaglio.cd_cds_altro:=cv_cd_cds;
         aRecReportDettaglio.cd_uo_altro:=cv_cd_uo;

         CNRCTB255.insReportDettaglio(aRecReportDettaglio);

      END LOOP;

      CLOSE gen_cv;

   END;

END insDettaglioPerRegistri;

-- =================================================================================================
-- Stampa RIEPILOGATIVO per UO con dettaglio per sezionale
-- =================================================================================================
PROCEDURE insFatturePerRiepilogativi
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    aPasso INTEGER,
    descrizioneSezionale VARCHAR2,
    aTiIstituzCommerc CHAR,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2
   ) AS
   gen_cv GenericCurTyp;
   gen_cv_a GenericCurTyp;
   gen_cv_b GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale - aggregazione per sezionale

   IF aTipoRegistro = 'A' THEN

      OPEN gen_cv FOR

           SELECT cd_cds_origine,
                  cd_uo_origine,
                  esercizio,
                  cd_tipo_sezionale,
                  SUM(imponibile_dettaglio),
                  SUM(iva_dettaglio),
                  SUM(TRUNC(iva_indetraibile_dettaglio,2)),
                  SUM(totale_dettaglio),
                  SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(imponibile_dettaglio,0), 0)) imponibile_split_payment,
                  SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(iva_dettaglio,0), 0)) iva_split_payment
           FROM   V_REGISTRO_IVA_ACQUISTI
           WHERE  cd_cds_origine = aCdCdsOrigine AND
                  cd_uo_origine = aCdUoOrigine AND
                  esercizio = aEsercizio AND
                  cd_tipo_Sezionale = aCdTipoSezionale AND
                  (data_registrazione BETWEEN aDataInizio AND aDataFine) AND
                  comm_ist_dettaglio = aTiIstituzCommerc
           GROUP BY cd_cds_origine,
                    cd_uo_origine,
                    esercizio,
                    cd_tipo_sezionale
           ORDER BY cd_tipo_sezionale;

   ELSE

      OPEN gen_cv FOR

           SELECT cd_cds_origine,
                  cd_uo_origine,
                  esercizio,
                  cd_tipo_sezionale,
                  SUM(imponibile_dettaglio),
                  SUM(iva_dettaglio),
                  0,
                  SUM(totale_dettaglio),
                  SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(imponibile_dettaglio,0), 0)) imponibile_split_payment,
                  SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(iva_dettaglio,0), 0)) iva_split_payment
           FROM   V_REGISTRO_IVA_VENDITE
           WHERE  cd_cds_origine = aCdCdsOrigine AND
                  cd_uo_origine = aCdUoOrigine AND
                  esercizio = aEsercizio AND
                  cd_tipo_Sezionale = aCdTipoSezionale AND
                  (data_emissione BETWEEN aDataInizio AND aDataFine)
           GROUP BY cd_cds_origine,
                    cd_uo_origine,
                    esercizio,
                    cd_tipo_sezionale,
                    0
           ORDER BY cd_tipo_sezionale;

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Fetch del cursore sezionali e inizio ciclo di aggregazione per gruppo IVA

   BEGIN

      LOOP

         FETCH gen_cv INTO
               cv_cd_cds_origine,
               cv_cd_uo_origine,
               cv_esercizio,
               cv_cd_tipo_sezionale,
               cv_imponibile_dettaglio,
               cv_iva_dettaglio,
               cv_iva_indetraibile_dettaglio,
               cv_totale_dettaglio,
               cv_imponibile_split_payment,
               cv_iva_split_payment;

         EXIT WHEN gen_cv%NOTFOUND;

         -- Lettura raggruppamento per GRUPPO

         IF aTipoRegistro = 'A' THEN

            OPEN gen_cv_a FOR

                 SELECT gruppo_iva,
                        descrizione_gruppo_iva,
                        SUM(imponibile_dettaglio),
                        SUM(iva_dettaglio),
                        SUM(TRUNC(iva_indetraibile_dettaglio,2)),
                        SUM(totale_dettaglio),
                        SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(imponibile_dettaglio,0), 0)) imponibile_split_payment,
                        SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(iva_dettaglio,0), 0)) iva_split_payment
                 FROM   V_REGISTRO_IVA_ACQUISTI
                 WHERE  cd_cds_origine = aCdCdsOrigine AND
                        cd_uo_origine = aCdUoOrigine AND
                        esercizio = aEsercizio AND
                        cd_tipo_sezionale = aCdTipoSezionale AND
                        (data_registrazione BETWEEN aDataInizio AND aDataFine) AND
                        comm_ist_dettaglio = aTiIstituzCommerc
                 GROUP BY gruppo_iva,
                          descrizione_gruppo_iva
                 ORDER BY gruppo_iva;

         ELSE

            OPEN gen_cv_a FOR

                 SELECT GRUPPO_IVA,
                        descrizione_gruppo_iva,
                        SUM(imponibile_dettaglio),
                        SUM(iva_dettaglio),
                        0,
                        SUM(totale_dettaglio),
                        SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(imponibile_dettaglio,0), 0)) imponibile_split_payment,
                        SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(iva_dettaglio,0), 0)) iva_split_payment
                 FROM   V_REGISTRO_IVA_VENDITE
                 WHERE  cd_cds_origine = aCdCdsOrigine AND
                        cd_uo_origine = aCdUoOrigine AND
                        esercizio = aEsercizio AND
                        cd_tipo_sezionale = aCdTipoSezionale AND
                        (data_emissione BETWEEN aDataInizio AND aDataFine)
                 GROUP BY GRUPPO_IVA,
                          descrizione_gruppo_iva,
                          0
                 ORDER BY gruppo_iva;

         END IF;

         -------------------------------------------------------------------------------------------
         -- Fetch del cursore gruppi iva e inizio ciclo di aggregazione per codice IVA

         LOOP

            FETCH gen_cv_a INTO
                  cv_gruppo_iva_a,
                  cv_descrizione_gruppo_iva_a,
                  cv_imponibile_a,
                  cv_iva_a,
                  cv_iva_indetraibile_a,
                  cv_totale_a,
                  cv_imponibile_split_payment_a,
                  cv_iva_split_payment_a;

            EXIT WHEN gen_cv_a%NOTFOUND;

            -- ciclo per recupero importi per codice iva in base al gruppo

            IF aTipoRegistro = 'A' THEN

               OPEN gen_cv_b FOR

                    SELECT codice_iva,
                           descrizione_iva,
                           gruppo_iva,
                           SUM(imponibile_dettaglio),
                           SUM(iva_dettaglio),
                           SUM(TRUNC(iva_indetraibile_dettaglio,2)),
                           SUM(totale_dettaglio),
                           SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(imponibile_dettaglio,0), 0)) imponibile_split_payment,
                           SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(iva_dettaglio,0), 0)) iva_split_payment
                    FROM   V_REGISTRO_IVA_ACQUISTI
                    WHERE  cd_cds_origine = aCdCdsOrigine AND
                           cd_uo_origine = aCdUoOrigine AND
                           esercizio = aEsercizio AND
                           cd_tipo_sezionale = aCdTipoSezionale AND
                           (data_registrazione BETWEEN aDataInizio AND aDataFine) AND
                           comm_ist_dettaglio = aTiIstituzCommerc AND
                           gruppo_iva = cv_gruppo_iva_a
                    GROUP BY codice_iva,
                             descrizione_iva,
                             gruppo_iva
                    ORDER BY codice_iva;

            ELSE

               OPEN gen_cv_b FOR

                    SELECT codice_iva,
                           descrizione_iva,
                           gruppo_iva,
                           SUM(imponibile_dettaglio),
                           SUM(iva_dettaglio),
                           0,
                           SUM(totale_dettaglio),
                           SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(imponibile_dettaglio,0), 0)) imponibile_split_payment,
                           SUM(DECODE(FL_SPLIT_PAYMENT, 'Y', nvl(iva_dettaglio,0), 0)) iva_split_payment
                    FROM   V_REGISTRO_IVA_VENDITE
                    WHERE  cd_cds_origine = aCdCdsOrigine AND
                           cd_uo_origine = aCdUoOrigine AND
                           esercizio = aEsercizio AND
                           cd_tipo_sezionale = aCdTipoSezionale AND
                           (data_emissione BETWEEN aDataInizio AND aDataFine) AND
                           gruppo_iva = cv_gruppo_iva_a
                    GROUP BY codice_iva,
                             descrizione_iva,
                             gruppo_iva,
                             0
                    ORDER BY codice_iva;

            END IF;

            -- Fetch cursore codici iva e inserimento su REPORT_GENERICO del dettaglio

            LOOP

               FETCH gen_cv_b INTO
                     cv_codice_iva_b,
                     cv_descrizione_iva_b,
                     cv_gruppo_iva_b,
                     cv_imponibile_b,
                     cv_iva_b,
                     cv_iva_indetraibile_b,
                     cv_totale_b,
                     cv_imponibile_split_payment_b,
                     cv_iva_split_payment_b;

               EXIT WHEN gen_cv_b%NOTFOUND;

               aSequenza:=aSequenza + 20;

               BEGIN

                  INSERT INTO VP_STM_RIEPILOGATIVO_UO
                         (id_report,
                          gruppo,
                          tipologia_riga,
                          sequenza,
                          sottogruppo,
                          codice_sezionale,
                          codice_gruppo_iva,
                          codice_iva,
                          sezionale_ds,
                          iva_ds,
                          imponibile,
                          iva,
                          iva_indetraibile,
                          totale,
                          acquisti_vendite,
                          descrizione_gruppo,
                          imponibile_split_payment,
                          iva_split_payment
                          )
                  VALUES (repID,
                          gruppoStm,
                          'D',
                          aSequenza,
                          sottoGruppoStm,
                          aCdTipoSezionale,
                          cv_gruppo_iva_b,
                          cv_codice_iva_b,
                          aCdTipoSezionale,
                          SUBSTR('CODICE IVA ' || cv_codice_iva_b || ' ' || cv_descrizione_iva_b,1,200),
                          cv_imponibile_b,
                          cv_iva_b,
                          cv_iva_indetraibile_b,
                          cv_totale_b,
                          aTipoRegistro,
                          descrizioneGruppoStm,
                          cv_imponibile_split_payment_b,
                          cv_iva_split_payment_b);

               EXCEPTION

                  WHEN DUP_VAL_ON_INDEX THEN
                       IBMERR001.RAISE_ERR_GENERICO
                          ('Chiave duplicata in inserimento riepilogo iva su tabella REPORT_GENERICO');

               END;

            END LOOP;

            CLOSE gen_cv_b;

            -- Inserimento del totale di gruppo

            aSequenza:=aSequenza + 20;

            BEGIN

               INSERT INTO VP_STM_RIEPILOGATIVO_UO
                      (id_report,
                       gruppo,
                       tipologia_riga,
                       sequenza,
                       sottogruppo,
                       codice_gruppo_iva,
                       iva_ds,
                       imponibile,
                       iva,
                       iva_indetraibile,
                       totale,
                       acquisti_vendite,
                       descrizione_gruppo,
                       imponibile_split_payment,
                       iva_split_payment)
               VALUES (repID,
                       gruppoStm,
                       'P',
                       aSequenza,
                       sottoGruppoStm,
                       cv_gruppo_iva_a,
                       SUBSTR('TOTALE GRUPPO' || cv_gruppo_iva_a || ' ' || cv_descrizione_gruppo_iva_a,1,200),
                       cv_imponibile_a,
                       cv_iva_a,
                       cv_iva_indetraibile_a,
                       cv_totale_a,
                       aTipoRegistro,
                       descrizioneGruppoStm,
                       cv_imponibile_split_payment_a,
                       cv_iva_split_payment_a);

            EXCEPTION

               WHEN DUP_VAL_ON_INDEX THEN
                    IBMERR001.RAISE_ERR_GENERICO
                       ('Chiave duplicata in inserimento riepilogo iva su tabella REPORT_GENERICO');

            END;

         END LOOP;

         CLOSE gen_cv_a;

         -- Inserimento del totale per sezionale

         aSequenza:=aSequenza + 20;

         BEGIN

            INSERT INTO VP_STM_RIEPILOGATIVO_UO
                   (id_report,
                    gruppo,
                    tipologia_riga,
                    sequenza,
                    sottogruppo,
                    codice_sezionale,
                    iva_ds,
                    imponibile,
                    iva,
                    iva_indetraibile,
                    totale,
                    acquisti_vendite,
                    descrizione_gruppo,
                    imponibile_split_payment,
                    iva_split_payment)
            VALUES (repID,
                    gruppoStm,
                    'T',
                    aSequenza,
                    sottoGruppoStm,
                    aCdTipoSezionale,
                    'TOTALE SEZIONALE ' || ' - ' || descrizioneSezionale,
                    cv_imponibile_dettaglio,
                    cv_iva_dettaglio,
                    cv_iva_indetraibile_dettaglio,
                    cv_totale_dettaglio,
                    aTipoRegistro,
                    descrizioneGruppoStm,
                    cv_imponibile_split_payment,
                    cv_iva_split_payment);

         EXCEPTION

            WHEN DUP_VAL_ON_INDEX THEN
                 IBMERR001.RAISE_ERR_GENERICO
                    ('Chiave duplicata in inserimento riepilogo iva su tabella REPORT_GENERICO');

         END;

      END LOOP;

      CLOSE gen_cv;

   END;

END insFatturePerRiepilogativi;


-- =================================================================================================
-- Stampa del registro riepilogativo senza dettaglio per sezionale. Si esegue solo se risultano
-- elaborati più di un sezionale
-- =================================================================================================
PROCEDURE insTotaliPerRiepilogativi
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    aPasso INTEGER,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2
   ) AS
   gen_cv GenericCurTyp;
   gen_cv_a GenericCurTyp;
   gen_cv_b GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale - aggregazione per gruppi iva

   BEGIN

      OPEN gen_cv_a FOR

           SELECT codice_gruppo_iva,
                  iva_ds,
                  acquisti_vendite,
                  SUM(imponibile),
                  SUM(iva),
                  SUM(iva_indetraibile),
                  SUM(totale),
                  SUM(imponibile_split_payment),
                  SUM(iva_split_payment)
           FROM   VP_STM_RIEPILOGATIVO_UO
           WHERE  id_report = repID AND
                  gruppo = 'B' AND
                  tipologia_riga = 'P'
           GROUP BY codice_gruppo_iva,
                    iva_ds,
                    acquisti_vendite
           ORDER BY codice_gruppo_iva;

      -- fetch del cursore gruppi iva e inizio ciclo di aggregazione per codice IVA

      LOOP

         FETCH gen_cv_a INTO
               cv_gruppo_iva_a,
               cv_descrizione_gruppo_iva_a,
               cv_acquisto_vendita_a,
               cv_imponibile_a,
               cv_iva_a,
               cv_iva_indetraibile_a,
               cv_totale_a,
               cv_imponibile_split_payment_a,
               cv_iva_split_payment_a;

         EXIT WHEN gen_cv_a%NOTFOUND;

         -- ciclo per recupero importi per codice iva in base al gruppo

         OPEN gen_cv_b FOR

              SELECT codice_iva,
                     iva_ds,
                     acquisti_vendite,
                     SUM(imponibile),
                     SUM(iva),
                     SUM(iva_indetraibile),
                     SUM(totale),
                     SUM(imponibile_split_payment),
                     SUM(iva_split_payment)
              FROM   VP_STM_RIEPILOGATIVO_UO
              WHERE  id_report = repID AND
                     gruppo = 'B' AND
                     tipologia_riga = 'D' AND
                     codice_gruppo_iva = cv_gruppo_iva_a
              GROUP BY codice_iva,
                       iva_ds,
                       acquisti_vendite
              ORDER BY codice_iva;

         -- Fetch cursore codici iva e inserimento su REPORT_GENERICO del dettaglio

         LOOP

            FETCH gen_cv_b INTO
                  cv_codice_iva_b,
                  cv_descrizione_iva_b,
                  cv_acquisto_vendita_b,
                  cv_imponibile_b,
                  cv_iva_b,
                  cv_iva_indetraibile_b,
                  cv_totale_b,
                  cv_imponibile_split_payment_b,
                  cv_iva_split_payment_b;

            EXIT WHEN gen_cv_b%NOTFOUND;

            aSequenza:=aSequenza + 20;

            BEGIN

               INSERT INTO VP_STM_RIEPILOGATIVO_UO
                      (id_report,
                       gruppo,
                       tipologia_riga,
                       sequenza,
                       sottogruppo,
                       codice_iva,
                       iva_ds,
                       imponibile,
                       iva,
                       iva_indetraibile,
                       totale,
                       acquisti_vendite,
                       descrizione_gruppo,
                       imponibile_split_payment,
                       iva_split_payment)
               VALUES (repID,
                       gruppoStm,
                       'D',
                       aSequenza,
                       sottoGruppoStm,
                       cv_codice_iva_b,
                       cv_descrizione_iva_b,
                       cv_imponibile_b,
                       cv_iva_b,
                       cv_iva_indetraibile_b,
                       cv_totale_b,
                       cv_acquisto_vendita_b,
                       descrizioneGruppoStm,
                       cv_imponibile_split_payment_b,
                       cv_iva_split_payment_b);

            EXCEPTION

               WHEN DUP_VAL_ON_INDEX THEN
                    IBMERR001.RAISE_ERR_GENERICO
                       ('Chiave duplicata in inserimento riepilogo iva su tabella REPORT_GENERICO' );

            END;

         END LOOP;

         CLOSE gen_cv_b;

         -- Inserimento del totale di gruppo

         aSequenza:=aSequenza + 20;

         BEGIN

            INSERT INTO VP_STM_RIEPILOGATIVO_UO
                   (id_report,
                    gruppo,
                    tipologia_riga,
                    sequenza,
                    sottogruppo,
                    codice_gruppo_iva,
                    iva_ds,
                    imponibile,
                    iva,
                    iva_indetraibile,
                    totale,
                    acquisti_vendite,
                    descrizione_gruppo,
                    imponibile_split_payment,
                    iva_split_payment)
            VALUES (repID,
                    gruppoStm,
                    'T',
                    aSequenza,
                    sottoGruppoStm,
                    cv_gruppo_iva_a,
                    cv_descrizione_gruppo_iva_a,
                    cv_imponibile_a,
                    cv_iva_a,
                    cv_iva_indetraibile_a,
                    cv_totale_a,
                    cv_acquisto_vendita_a,
                    descrizioneGruppoStm,
                    cv_imponibile_split_payment_a,
                    cv_iva_split_payment_a);

         EXCEPTION

            WHEN DUP_VAL_ON_INDEX THEN
                 IBMERR001.RAISE_ERR_GENERICO
                    ('Chiave duplicata in inserimento riepilogo iva su tabella REPORT_GENERICO' );

         END;

      END LOOP;

      CLOSE gen_cv_a;

      -- inserimento del totale generale

      OPEN gen_cv FOR

           SELECT acquisti_vendite,
                  SUM(imponibile),
                  SUM(iva),
                  SUM(iva_indetraibile),
                  SUM(totale),
                  SUM(imponibile_split_payment),
                  SUM(iva_split_payment)
           FROM   VP_STM_RIEPILOGATIVO_UO
           WHERE  id_report = repID AND
                  gruppo = 'C' AND
                  tipologia_riga = 'T'
           GROUP BY acquisti_vendite;

      -- Fetch cursore codici iva e inserimento su REPORT_GENERICO del dettaglio

      LOOP

         FETCH gen_cv INTO
               cv_acquisto_vendita,
               cv_imponibile_dettaglio,
               cv_iva_dettaglio,
               cv_iva_indetraibile_dettaglio,
               cv_totale_dettaglio,
               cv_imponibile_split_payment,
               cv_iva_split_payment;

         EXIT WHEN gen_cv%NOTFOUND;

         aSequenza:=aSequenza + 20;

         BEGIN

            INSERT INTO VP_STM_RIEPILOGATIVO_UO
                   (id_report,
                    gruppo,
                    tipologia_riga,
                    sequenza,
                    sottogruppo,
                    iva_ds,
                    imponibile,
                    iva,
                    iva_indetraibile,
                    totale,
                    acquisti_vendite,
                    descrizione_gruppo,
                    imponibile_split_payment,
                    iva_split_payment)
            VALUES (repID,
                    gruppoStm,
                    'T',
                    aSequenza,
                    sottoGruppoStm,
                    'TOTALE GENERALE',
                    cv_imponibile_dettaglio,
                    cv_iva_dettaglio,
                    cv_iva_indetraibile_dettaglio,
                    cv_totale_dettaglio,
                    cv_acquisto_vendita,
                    descrizioneGruppoStm,
                    cv_imponibile_split_payment,
                    cv_iva_split_payment);

         EXCEPTION

            WHEN DUP_VAL_ON_INDEX THEN
                 IBMERR001.RAISE_ERR_GENERICO
                    ('Chiave duplicata in inserimento riepilogo iva su tabella REPORT_GENERICO');

         END;

      END LOOP;

      CLOSE gen_cv;

   END;

END insTotaliPerRiepilogativi;

-- =================================================================================================
-- Inserimento dati delle fatture IVA differita nel report generico
-- =================================================================================================
PROCEDURE insFattureIvaDifferita
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    descrizioneSezionale VARCHAR2,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    aRistampa VARCHAR2,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2
   ) IS
   gen_cv GenericCurTyp;
   acq_ven   VARCHAR2(1);
   UOENTE unita_organizzativa%rowtype:= CNRCTB020.GETUOENTE(aEsercizio);
Begin
Dbms_Output.put_line('diff '||aCdTipoSezionale);
   -- Dettaglio per SEZIONE I (EMESSE NEL PERIODO)
   IF    gruppoStm = 'A' THEN
         -- ***********************************************************************************************************
         -- Attenzione: inTipoRegistro = '*', ma poiche' non sono gestite le differite per gli acquisti, lasciamo cosi'
         -- ***********************************************************************************************************
         IF aTipoRegistro = 'A' Then

            OPEN gen_cv FOR

                 Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'A'
                 FROM   V_STM_IVA_DIFF_PASSIVA
                 WHERE  cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        esercizio = aEsercizio AND
                        cd_tipo_sezionale = aCdTipoSezionale AND
                        (data_registrazione BETWEEN aDataInizio AND aDataFine)
               Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                  ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_registrazione,
                          numero_progressivo;

         ELSE

            OPEN gen_cv For

                 Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'V'
                 FROM   V_STM_IVA_DIFF_ATTIVA
                 WHERE  cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        esercizio = aEsercizio AND
                        cd_tipo_sezionale = aCdTipoSezionale AND
                        (data_emissione BETWEEN aDataInizio AND aDataFine)
                Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                 ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_emissione,
                          protocollo_iva;
         END IF;

   -- Dettaglio per SEZIONE II (EMESSE NELL'ESERCIZIO E DIVENUTE ESIGIBILI NEL PERIODO)

   ELSIF gruppoStm = 'B' THEN

         IF aTipoRegistro = 'A' THEN

            OPEN gen_cv FOR

                  Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'A'
                 FROM   V_STM_IVA_DIFF_PASSIVA
                 WHERE  data_registrazione <= aDataFine AND
                        cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        cd_tipo_sezionale = aCdTipoSezionale And
                        To_Char(data_registrazione,'YYYY') = aEsercizio And
                        data_esigibilita_diff IS NOT NULL AND
                        (data_esigibilita_diff BETWEEN aDataInizio AND aDataFine)
                Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                  ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_registrazione,
                          numero_progressivo;
         ELSE

            OPEN gen_cv FOR
    /*
                 SELECT V_STM_IVA_DIFF_ATTIVA.*,'V'
                 FROM   V_STM_IVA_DIFF_ATTIVA
                 WHERE  data_emissione <= aDataFine AND
                        cd_cds_origine = aCdCdsOrigine AND
                        cd_uo_origine = aCdUoOrigine AND
                        cd_tipo_sezionale = aCdTipoSezionale AND
                        data_esigibilita_diff IS NOT NULL AND
                        (
                            (data_esigibilita_diff BETWEEN aDataInizio AND aDataFine)
                         OR
                            (
                             data_esigibilita_diff < aDataInizio AND
                             NOT EXISTS
                                 (SELECT 1
                                  FROM   REPORT_DETTAGLIO D
                                  WHERE  D.cd_cds = cd_cds_origine AND
                                         D.cd_unita_organizzativa = cd_uo_origine AND
                                         D.esercizio = esercizio AND
                                         D.tipo_report = 'LIQUIDAZIONE_VENDITE' AND
                                         D.pg_documento = numero_fattura)
                            )
                        )
                 ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_emissione,
                          protocollo_iva;
    */
      Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'V'
                 FROM   V_STM_IVA_DIFF_ATTIVA
                 WHERE  data_emissione <= aDataFine And
      cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        cd_tipo_sezionale = aCdTipoSezionale And
                        To_Char(data_emissione,'YYYY') = aEsercizio And
                        data_esigibilita_diff IS NOT NULL AND
                        (
                            (data_esigibilita_diff BETWEEN aDataInizio AND aDataFine)
                         OR
                            (
                             data_esigibilita_diff < aDataInizio AND
                             Exists
                                 (SELECT 1
                                  FROM   REPORT_DETTAGLIO D
                                  WHERE  D.cd_cds = cd_cds_origine AND
                                         D.cd_unita_organizzativa = cd_uo_origine AND
                                         D.esercizio = V_STM_IVA_DIFF_ATTIVA.esercizio AND
                                         D.pg_documento = numero_progressivo And
                                         D.tipo_report = 'LIQUIDAZIONE_VENDITE' And
                                         D.TI_DOCUMENTO = 'FATTURA_A' And
                                         d.pg_riga_documento = progressivo_riga And
           D.DATA_INIZIO >= aDataInizio And
                       D.DATA_FINE <= aDataFine)
                            )
                        )
                 Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                 ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_emissione,
                          protocollo_iva;
         END IF;

   -- Dettaglio per SEZIONE III (EMESSE IN ESERCIZI PRECEDENTI E DIVENUTE ESIGIBILI NEL PERIODO)
   ELSIF gruppoStm = 'C' THEN

         IF aTipoRegistro = 'A' THEN

               Open gen_cv FOR
      Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'A'
                 FROM   V_STM_IVA_DIFF_PASSIVA
                 WHERE  data_registrazione <= aDataFine And
                  cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        cd_tipo_sezionale = aCdTipoSezionale And
                        To_Char(data_registrazione,'YYYY') < aEsercizio And
                        data_esigibilita_diff IS NOT NULL AND
                        (
                            (data_esigibilita_diff BETWEEN aDataInizio AND aDataFine
                            -- *RP
                            And data_esigibilita_diff-data_emissione <365
                            )
                         OR
                            (
                             data_esigibilita_diff < aDataInizio AND
                             Exists
                                 (SELECT 1
                                  FROM   REPORT_DETTAGLIO D
                                  WHERE  D.cd_cds = cd_cds_origine AND
                                         D.cd_unita_organizzativa = cd_uo_origine AND
                                         D.esercizio = V_STM_IVA_DIFF_PASSIVA.esercizio AND
                                         D.pg_documento = numero_progressivo And
                                         D.tipo_report = 'LIQUIDAZIONE_ACQUISTI' And
                                         D.TI_DOCUMENTO = 'FATTURA_P' And
                                         d.pg_riga_documento = progressivo_riga And
           D.DATA_INIZIO >= aDataInizio And
                       D.DATA_FINE <= aDataFine)
                            )
                        )
                 Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                  ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_registrazione,
                          numero_progressivo;

         ELSE

            OPEN gen_cv FOR
      Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'V'
                 FROM   V_STM_IVA_DIFF_ATTIVA
                 WHERE  data_emissione <= aDataFine And
                  cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        cd_tipo_sezionale = aCdTipoSezionale And
                        To_Char(data_emissione,'YYYY') < aEsercizio And
                        data_esigibilita_diff IS NOT NULL AND
                        (
                            (data_esigibilita_diff BETWEEN aDataInizio AND aDataFine)
                         OR
                            (
                             data_esigibilita_diff < aDataInizio AND
                             Exists
                                 (SELECT 1
                                  FROM   REPORT_DETTAGLIO D
                                  WHERE  D.cd_cds = cd_cds_origine AND
                                         D.cd_unita_organizzativa = cd_uo_origine AND
                                         D.esercizio = V_STM_IVA_DIFF_ATTIVA.esercizio AND
                                         D.pg_documento = numero_progressivo And
                                         D.tipo_report = 'LIQUIDAZIONE_VENDITE' And
                                         D.TI_DOCUMENTO = 'FATTURA_A' And
                                         d.pg_riga_documento = progressivo_riga And
           D.DATA_INIZIO >= aDataInizio And
                       D.DATA_FINE <= aDataFine)
                            )
                        )
                 Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                 ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_emissione,
                          protocollo_iva;
         END IF;

   -- Dettaglio per SEZIONE IV (EMESSE NELL'ESERCIZIO E NON ANCORA ESIGIBILI)

   ELSIF gruppoStm = 'D' THEN

         IF aTipoRegistro = 'A' THEN

            OPEN gen_cv FOR

                  Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'A'
                 FROM   V_STM_IVA_DIFF_PASSIVA
                 WHERE  data_registrazione <= aDataFine AND
                        cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        cd_tipo_sezionale = aCdTipoSezionale And
                        To_Char(data_registrazione,'YYYY') = aEsercizio And
                        (data_esigibilita_diff IS NULL OR
                         data_esigibilita_diff > aDataFine)
                 Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                  ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_registrazione,
                          numero_progressivo;

         ELSE

            OPEN gen_cv FOR

                  Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'V'
                 FROM   V_STM_IVA_DIFF_ATTIVA
                 WHERE  data_emissione <= aDataFine AND
      cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        cd_tipo_sezionale = aCdTipoSezionale And
                        To_Char(data_emissione,'YYYY') = aEsercizio And
                        (data_esigibilita_diff IS NULL OR
                         data_esigibilita_diff > aDataFine)
                Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                 ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_emissione,
                          protocollo_iva;

         END IF;
   -- Dettaglio per SEZIONE V (EMESSE IN ESERCIZI PRECEDENTI E NON ANCORA ESIGIBILI)

   ELSIF gruppoStm = 'E' THEN

         IF aTipoRegistro = 'A' THEN

            OPEN gen_cv FOR

                  Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'A'
                 FROM   V_STM_IVA_DIFF_PASSIVA
                 WHERE  data_registrazione <= aDataFine AND
                        cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        cd_tipo_sezionale = aCdTipoSezionale And
                         To_Char(data_registrazione,'YYYY') < aEsercizio And
                        (data_esigibilita_diff IS NULL OR
                         data_esigibilita_diff > aDataFine)
                Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                  ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_registrazione,
                          numero_progressivo;

         ELSE

            OPEN gen_cv FOR
                  Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'V'
                 FROM   V_STM_IVA_DIFF_ATTIVA
                 WHERE  data_emissione <= aDataFine AND
      cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        cd_tipo_sezionale = aCdTipoSezionale And
                        To_Char(data_emissione,'YYYY') < aEsercizio And
                        (data_esigibilita_diff IS NULL OR
                         data_esigibilita_diff > aDataFine)
                 Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                 ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_emissione,
                          protocollo_iva;

         END IF;
         ELSIF gruppoStm = 'F' Then
         -- Solo per Acquisti viene passato il gruppo F
          IF aTipoRegistro = 'A' THEN

            OPEN gen_cv FOR

               Select ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE, Sum(IMPONIBILE_DETTAGLIO),Sum(IVA_DETTAGLIO),Sum( IVA_INDETRAIBILE_DETTAGLIO),Sum(TOTALE_DETTAGLIO),
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF,'A'
                 FROM   V_STM_IVA_DIFF_PASSIVA
                 WHERE  data_registrazione <= aDataFine And
                  cd_cds_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_cds_origine,aCdCdsOrigine) And
                        cd_uo_origine = Decode(aCdCdsOrigine,UOENTE.cd_unita_padre,cd_uo_origine,aCdUoOrigine) And
                        cd_tipo_sezionale = aCdTipoSezionale And
                        To_Char(data_EMISSIONE,'YYYY') < aEsercizio And
                        data_esigibilita_diff IS NOT NULL AND
                        data_esigibilita_diff BETWEEN aDataInizio AND aDataFine   And
                        data_esigibilita_diff-data_emissione >=365
               Group By ESERCIZIO, CD_CDS, CD_UNITA_ORGANIZZATIVA, CD_CDS_ORIGINE, CD_UO_ORIGINE,
                        CD_TIPO_SEZIONALE, TI_FATTURA, DATA_REGISTRAZIONE, NUMERO_PROGRESSIVO, DATA_EMISSIONE,
                        NUMERO_FATTURA, PROTOCOLLO_IVA, PROTOCOLLO_IVA_GENERALE, COMM_IST_TESTATA, CODICE_ANAGRAFICO,
                        RAGIONE_SOCIALE,
                        COMM_IST_DETTAGLIO, CODICE_IVA, PERCENTUALE_IVA, DESCRIZIONE_IVA, FL_IVA_DETRAIBILE,
                        PERCENTUALE_IVA_DETRAIBILE, INTRA_UE, BOLLA_DOGANALE, SPEDIZIONIERE, ESIGIBILITA_DIFF,
                        DATA_ESIGIBILITA_DIFF
                  ORDER BY cd_tipo_sezionale,
                          esercizio,
                          data_registrazione,
                          numero_progressivo;
         End IF;
   END IF;

   -- fetch del cursore fatture e insert in REPORT_GENERICO

   LOOP

      FETCH gen_cv INTO
            cv_esercizio,
            cv_cd_cds,
            cv_cd_uo,
            cv_cd_cds_origine,
            cv_cd_uo_origine,
            cv_cd_tipo_sezionale,
            cv_ti_fattura,
            cv_data_registrazione,
            cv_numero_progressivo,
            cv_data_emissione,
            cv_numero_fattura,
            cv_protocollo_iva,
            cv_protocollo_iva_gen,
            cv_comm_ist_testata,
            cv_codice_anagrafico,
            cv_ragione_sociale,
            cv_imponibile_dettaglio,
            cv_iva_dettaglio,
            cv_iva_indetraibile_dettaglio,
            cv_totale_dettaglio,
            cv_comm_ist_dettaglio,
            cv_codice_iva,
            cv_percentuale_iva,
            cv_descrizione_iva,
            cv_fl_iva_detraibile,
            cv_percentuale_iva_detraibile,
            cv_intra_ue,
            cv_bolla_doganale,
            cv_spedizioniere,
            cv_esigibilita_diff,
            cv_data_esigibilita_diff,
            cv_acquisto_vendita;

      EXIT WHEN gen_cv%NOTFOUND;

      aSequenza:=aSequenza + 20;

      cv_iva_indetraibile_dettaglio:=ROUND(cv_iva_indetraibile_dettaglio,2);

      BEGIN

         INSERT INTO VP_STM_IVA_DIFFERITA
                (id_report,
                 sezione,
                 tipologia_riga,
                 sequenza,
                 sottogruppo,
                 titolo_uo,
                 titolo_report,
                 cd_cds_origine,
                 cd_uo_origine,
                 esercizio,
                 cd_tipo_sezionale,
                 data_registrazione,
                 numero_progressivo,
                 data_emissione,
                 numero_fattura,
                 tipo,
                 protocollo_iva,
                 protocollo_iva_gen,
                 codice_terzo,
                 ragione_sociale,
                 imponibile,
                 iva,
                 iva_indetraibile,
                 totale,
                 codice_iva,
                 esigibilita_diff,
                 data_esigibilita,
                 provvisorio_definitivo,
                 acquisto_vendita,
                 descrizione_gruppo)
         VALUES (repID,
                 gruppoStm,
                 'D',
                 aSequenza,
                 sottoGruppoStm,
                 NULL,
                 NULL,
                 cv_cd_cds_origine,
                 cv_cd_uo_origine,
                 cv_esercizio,
                 cv_cd_tipo_sezionale,
     cv_data_registrazione,
                 cv_numero_progressivo,
                 cv_data_emissione,
                 cv_numero_fattura,
                 cv_ti_fattura,
                 cv_protocollo_iva,
                 cv_protocollo_iva_gen,
                 cv_codice_anagrafico,
                 cv_ragione_sociale,
                 cv_imponibile_dettaglio,
                 cv_iva_dettaglio,
                 cv_iva_indetraibile_dettaglio,
                 cv_totale_dettaglio,
                 cv_codice_iva,
                 cv_esigibilita_diff,
                 cv_data_esigibilita_diff,
                 aTipoReport,
                 --aTipoRegistro,
                 cv_acquisto_vendita,
                 descrizioneGruppoStm);

      EXCEPTION

         WHEN DUP_VAL_ON_INDEX THEN
              IBMERR001.RAISE_ERR_GENERICO
                 ('Chiave duplicata in inserimento dettagli iva differita su tabella REPORT_GENERICO' );

      END;

   END LOOP;

   CLOSE gen_cv;

END insFattureIvaDifferita;

-- =================================================================================================
-- Inserimento totali per sezionale
-- =================================================================================================
PROCEDURE insTotaliSezionaleIvaDifferita
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2
   ) IS
   gen_cv GenericCurTyp;

BEGIN

   -- Calcolo dei totali per ogni sezione

   OPEN gen_cv FOR

        SELECT cd_tipo_sezionale,
               provvisorio_definitivo,
               acquisto_vendita,
               MAX(sequenza),
               SUM(imponibile),
               SUM(iva),
               SUM(iva_indetraibile),
               SUM(totale)
        FROM   VP_STM_IVA_DIFFERITA
        WHERE  id_report = repID AND
               sezione = gruppoStm AND
               tipologia_riga = 'D'
        GROUP BY cd_tipo_sezionale,
                 provvisorio_definitivo,
                 acquisto_vendita;

   -- fetch del cursore fatture e insert in REPORT_GENERICO

   LOOP

      FETCH gen_cv INTO
            cv_cd_tipo_sezionale,
            cv_provvisorio_definitivo,
            cv_acquisto_vendita,
            cv_imponibile_dettaglio,
            cv_iva_dettaglio,
            cv_iva_indetraibile_dettaglio,
            cv_totale_dettaglio;

      EXIT WHEN gen_cv%NOTFOUND;

      cv_sequenza:=cv_sequenza + 1;

      BEGIN

         INSERT INTO VP_STM_IVA_DIFFERITA
                (id_report,
                 sezione,
                 tipologia_riga,
                 sequenza,
                 sottogruppo,
                 ragione_sociale,
                 imponibile,
                 iva,
                 iva_indetraibile,
                 totale,
                 provvisorio_definitivo,
                 acquisto_vendita,
                 descrizione_gruppo)
         VALUES (repID,
                 gruppoStm,
                 'I',
                 cv_sequenza,
                 sottoGruppoStm,
                 'TOTALE SEZIONALE ' || cv_cd_tipo_sezionale,
                 cv_imponibile_dettaglio,
                 cv_iva_dettaglio,
                 cv_iva_indetraibile_dettaglio,
                 cv_totale_dettaglio,
                 cv_provvisorio_definitivo,
                 cv_acquisto_vendita,
                 descrizioneGruppoStm);
      EXCEPTION

         WHEN DUP_VAL_ON_INDEX THEN
              IBMERR001.RAISE_ERR_GENERICO
                 ('Chiave duplicata in inserimento totali per sezionale di iva differita su tabella REPORT_GENERICO' );

      END;

   END LOOP;

   CLOSE gen_cv;

END insTotaliSezionaleIvaDifferita;

-- =================================================================================================
-- Inserimento totali delle fatture IVA differita nel report generico
-- =================================================================================================
PROCEDURE insTotaliIvaDifferita
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2
   ) IS
   gen_cv GenericCurTyp;

BEGIN

   -- Calcolo dei totali per ogni sezione

   OPEN gen_cv FOR

        SELECT acquisto_vendita,
               provvisorio_definitivo,
               SUM(imponibile),
               SUM(iva),
               SUM(iva_indetraibile),
               SUM(totale)
        FROM   VP_STM_IVA_DIFFERITA
        WHERE  id_report = repID AND
               sezione = gruppoStm AND
               tipologia_riga = 'D'
        GROUP BY acquisto_vendita,
                 provvisorio_definitivo;

   LOOP

      FETCH gen_cv INTO
            cv_acquisto_vendita,
            cv_provvisorio_definitivo,
            cv_imponibile_dettaglio,
            cv_iva_dettaglio,
            cv_iva_indetraibile_dettaglio,
            cv_totale_dettaglio;

      EXIT WHEN gen_cv%NOTFOUND;

      aSequenza:=aSequenza + 20;

      BEGIN

         INSERT INTO VP_STM_IVA_DIFFERITA
                (id_report,
                 sezione,
                 tipologia_riga,
                 sequenza,
                 sottogruppo,
                 ragione_sociale,
                 imponibile,
                 iva,
                 iva_indetraibile,
                 totale,
                 provvisorio_definitivo,
                 acquisto_vendita,
                 descrizione_gruppo)
         VALUES (repID,
                 gruppoStm,
                 'P',
                 aSequenza,
                 sottoGruppoStm,
                 descrizioneGruppoStm,
                 cv_imponibile_dettaglio,
                 cv_iva_dettaglio,
                 cv_iva_indetraibile_dettaglio,
                 cv_totale_dettaglio,
                 cv_provvisorio_definitivo,
                 cv_acquisto_vendita,
                 descrizioneGruppoStm);
      EXCEPTION

         WHEN DUP_VAL_ON_INDEX THEN
              IBMERR001.RAISE_ERR_GENERICO
                 ('Chiave duplicata in inserimento totali di sezione per iva differita su tabella REPORT_GENERICO' );

      END;

   END LOOP;

   CLOSE gen_cv;

END insTotaliIvaDifferita;
-- =================================================================================================
-- Inserimento totali delle fatture IVA differita raggruppate per codice IVA nel report generico
-- =================================================================================================
PROCEDURE insTotaliIvaDiffPerCodIVA
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2
   ) IS
   gen_cv GenericCurTyp;

BEGIN

   -- Calcolo dei totali per ogni sezione e per codice iva

   OPEN gen_cv FOR

        SELECT acquisto_vendita,
               provvisorio_definitivo,
               codice_iva,
               SUM(imponibile),
               SUM(iva),
               SUM(iva_indetraibile),
               SUM(totale)
        FROM   VP_STM_IVA_DIFFERITA
        WHERE  id_report = repID AND
               sezione = gruppoStm AND
               tipologia_riga = 'D'
        GROUP BY acquisto_vendita,
                 provvisorio_definitivo,
                 codice_iva;

   LOOP

      FETCH gen_cv INTO
            cv_acquisto_vendita,
            cv_provvisorio_definitivo,
            cv_codice_iva,
            cv_imponibile_dettaglio,
            cv_iva_dettaglio,
            cv_iva_indetraibile_dettaglio,
            cv_totale_dettaglio;

      EXIT WHEN gen_cv%NOTFOUND;

      aSequenza:=aSequenza + 20;

      BEGIN

         INSERT INTO VP_STM_IVA_DIFFERITA
                (id_report,
                 sezione,
                 tipologia_riga,
                 sequenza,
                 sottogruppo,
                 ragione_sociale,
                 imponibile,
                 iva,
                 iva_indetraibile,
                 totale,
                 provvisorio_definitivo,
                 acquisto_vendita,
                 codice_iva,
                 descrizione_gruppo)
         VALUES (repID,
                 gruppoStm,
                 'C',
                 aSequenza,
                 sottoGruppoStm,
                 descrizioneGruppoStm,
                 cv_imponibile_dettaglio,
                 cv_iva_dettaglio,
                 cv_iva_indetraibile_dettaglio,
                 cv_totale_dettaglio,
                 cv_provvisorio_definitivo,
                 cv_acquisto_vendita,
                 cv_codice_iva,
                 descrizioneGruppoStm);
      EXCEPTION

         WHEN DUP_VAL_ON_INDEX THEN
              IBMERR001.RAISE_ERR_GENERICO
                 ('Chiave duplicata in inserimento totali di sezione per iva differita su tabella REPORT_GENERICO' );

      END;

   END LOOP;

   CLOSE gen_cv;

END insTotaliIvaDiffPerCodIVA;

-- =================================================================================================
-- Inserimento righe di riepilogo IVA differita nel report generico
-- =================================================================================================
PROCEDURE insRiepilogoIvaDifferita
   (
    repID INTEGER,
    aSequenza IN OUT INTEGER,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    totaleImponibile NUMBER,
    totaleIva NUMBER,
    totaleIvaIndetraibile NUMBER,
    totaleFattura NUMBER,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2
   ) AS
   descrizioneTotale VARCHAR2(100);
   imponibileEsigibile NUMBER(15,2);
   ivaEsigibile NUMBER(15,2);
   ivaIndetraibileEsigibile NUMBER(15,2);
   totaleEsigibile NUMBER(15,2);
   gen_cv GenericCurTyp;

BEGIN

   FOR i IN 1 .. 7

   LOOP

      aSequenza:=aSequenza + 20;
      imponibileEsigibile:=0;
      ivaEsigibile:=0;
      ivaIndetraibileEsigibile:=0;
      totaleEsigibile:=0;

      -- Totale fatture attive e passive emesse

      IF    i = 1 THEN
            -- ***********************************************************************************************************
            -- Attenzione: aTipoRegistro = '*', ma poiche' non sono gestite le differite per gli acquisti, lasciamo cosi'
            -- ***********************************************************************************************************
            descrizioneTotale:='RIEPILOGO';


            imponibileEsigibile:=Nvl(totaleImponibile,0);
            ivaEsigibile:=Nvl(totaleIva,0);
            ivaIndetraibileEsigibile:=Nvl(totaleIvaIndetraibile,0);
            totaleEsigibile:=Nvl(totaleFattura,0);

            BEGIN

               INSERT INTO VP_STM_IVA_DIFFERITA
                      (id_report,
                       sezione,
                       tipologia_riga,
                       sequenza,
                       sottogruppo,
                       ragione_sociale,
                       imponibile,
                       iva,
                       iva_indetraibile,
                       totale,
                       provvisorio_definitivo,
                       acquisto_vendita,
                       descrizione_gruppo)
               VALUES (repID,
                       gruppoStm,
                       'T',
                       aSequenza,
                       sottoGruppoStm,
                       descrizioneTotale,
                       totaleImponibile,
                       totaleIva,
                       totaleIvaIndetraibile,
                       totaleFattura,
                       aTipoReport,
                       aTipoRegistro,
                       descrizioneGruppoStm);
            END;

      -- Totale prima sezione

      ELSIF i = 2 THEN

            If aTipoRegistro = 'A' THEN
               descrizioneTotale:='TOTALE FATTURE RICEVUTE NEL PERIODO';
            ELSE
               descrizioneTotale:='TOTALE FATTURE EMESSE NEL PERIODO';
            END IF;

            OPEN gen_cv FOR

              Select Nvl(SUM(Nvl(imponibile,0)),0),
                        Nvl(SUM(Nvl(iva,0)),0),
                        Nvl(SUM(Nvl(iva_indetraibile,0)),0),
                        Nvl(SUM(Nvl(totale,0)),0)
                 FROM   VP_STM_IVA_DIFFERITA
                 WHERE  id_report = repID AND
                        sezione = 'A' AND
                        tipologia_riga = 'P';

            -- fetch del cursore fatture e insert in REPORT_GENERICO

            LOOP

               FETCH gen_cv INTO
                     cv_imponibile_dettaglio,
                     cv_iva_dettaglio,
                     cv_iva_indetraibile_dettaglio,
                     cv_totale_dettaglio;

               EXIT WHEN gen_cv%NOTFOUND;

               aSequenza:=aSequenza + 20;

               imponibileEsigibile:=Nvl(imponibileEsigibile,0) - Nvl(cv_imponibile_dettaglio,0);
               ivaEsigibile:=Nvl(ivaEsigibile,0) - Nvl(cv_iva_dettaglio,0);
               ivaIndetraibileEsigibile:=Nvl(ivaIndetraibileEsigibile,0) - ROUND(Nvl(cv_iva_indetraibile_dettaglio,0),2);
               totaleEsigibile:=Nvl(totaleEsigibile,0) - Nvl(cv_totale_dettaglio,0);

               BEGIN

                  INSERT INTO VP_STM_IVA_DIFFERITA
                         (id_report,
                          sezione,
                          tipologia_riga,
                          sequenza,
                          sottogruppo,
                          ragione_sociale,
                          imponibile,
                          iva,
                          iva_indetraibile,
                          totale,
                          provvisorio_definitivo,
                          acquisto_vendita,
                          descrizione_gruppo)
                  VALUES (repID,
                          gruppoStm,
                          'T',
                          aSequenza,
                          sottoGruppoStm,
                          descrizioneTotale,
                          cv_imponibile_dettaglio,
                          cv_iva_dettaglio,
                          cv_iva_indetraibile_dettaglio,
                          cv_totale_dettaglio,
                          aTipoReport,
                          aTipoRegistro,
                          descrizioneGruppoStm);
               END;

            END LOOP;

            CLOSE gen_cv;

      -- Totale seconda sezione

      ELSIF i = 3 THEN


            If aTipoRegistro = 'A' THEN
               descrizioneTotale:='TOTALE FATTURE RICEVUTE NELL''ESERCIZIO, DIVENUTE ESIGIBILI NEL PERIODO';
            ELSE
                descrizioneTotale:='TOTALE FATTURE EMESSE NELL''ESERCIZIO, DIVENUTE ESIGIBILI NEL PERIODO';
            END IF;

            OPEN gen_cv FOR

              Select Nvl(SUM(Nvl(imponibile,0)),0),
                        Nvl(SUM(Nvl(iva,0)),0),
                        Nvl(SUM(Nvl(iva_indetraibile,0)),0),
                        Nvl(SUM(Nvl(totale,0)),0)
                 FROM   VP_STM_IVA_DIFFERITA
                 WHERE  id_report = repID AND
                        sezione = 'B' AND
                        tipologia_riga = 'P';

            -- fetch del cursore fatture e insert in REPORT_GENERICO

            LOOP

               FETCH gen_cv INTO
                     cv_imponibile_dettaglio,
                     cv_iva_dettaglio,
                     cv_iva_indetraibile_dettaglio,
                     cv_totale_dettaglio;

               EXIT WHEN gen_cv%NOTFOUND;

               aSequenza:=aSequenza + 20;

               imponibileEsigibile:=Nvl(imponibileEsigibile,0) + Nvl(cv_imponibile_dettaglio,0);
               ivaEsigibile:=Nvl(ivaEsigibile,0) + Nvl(cv_iva_dettaglio,0);
               ivaIndetraibileEsigibile:=Nvl(ivaIndetraibileEsigibile,0) + ROUND(Nvl(cv_iva_indetraibile_dettaglio,0),2);
               totaleEsigibile:=Nvl(totaleEsigibile,0) + Nvl(cv_totale_dettaglio,0);

               BEGIN

                  INSERT INTO VP_STM_IVA_DIFFERITA
                         (id_report,
                          sezione,
                          tipologia_riga,
                          sequenza,
                          sottogruppo,
                          ragione_sociale,
                          imponibile,
                          iva,
                          iva_indetraibile,
                          totale,
                          provvisorio_definitivo,
                          acquisto_vendita,
                          descrizione_gruppo)
                  VALUES (repID,
                          gruppoStm,
                          'T',
                          aSequenza,
                          sottoGruppoStm,
                          descrizioneTotale,
                          cv_imponibile_dettaglio,
                          cv_iva_dettaglio,
                          cv_iva_indetraibile_dettaglio,
                          cv_totale_dettaglio,
                          aTipoReport,
                          aTipoRegistro,
                          descrizioneGruppoStm);

               END;

            END LOOP;

            CLOSE gen_cv;
      ELSIF i = 4 THEN
            If aTipoRegistro = 'A' THEN
               descrizioneTotale:='TOTALE FATTURE RICEVUTE IN ESERCIZI PRECEDENTI, DIVENUTE ESIGIBILI NEL PERIODO';
            ELSE
              descrizioneTotale:='TOTALE FATTURE EMESSE IN ESERCIZI PRECEDENTI, DIVENUTE ESIGIBILI NEL PERIODO';
            END IF;


            OPEN gen_cv FOR

           Select Nvl(SUM(Nvl(imponibile,0)),0),
                        Nvl(SUM(Nvl(iva,0)),0),
                        Nvl(SUM(Nvl(iva_indetraibile,0)),0),
                        Nvl(SUM(Nvl(totale,0)),0)
                 FROM   VP_STM_IVA_DIFFERITA
                 WHERE  id_report = repID AND
                        sezione = 'C' AND
                        tipologia_riga = 'P';

            -- fetch del cursore fatture e insert in REPORT_GENERICO

            LOOP

               FETCH gen_cv INTO
                     cv_imponibile_dettaglio,
                     cv_iva_dettaglio,
                     cv_iva_indetraibile_dettaglio,
                     cv_totale_dettaglio;

               EXIT WHEN gen_cv%NOTFOUND;

               aSequenza:=aSequenza + 20;

               imponibileEsigibile:=Nvl(imponibileEsigibile,0) + Nvl(cv_imponibile_dettaglio,0);
               ivaEsigibile:=Nvl(ivaEsigibile,0) + Nvl(cv_iva_dettaglio,0);
               ivaIndetraibileEsigibile:=Nvl(ivaIndetraibileEsigibile,0) + ROUND(Nvl(cv_iva_indetraibile_dettaglio,0),2);
               totaleEsigibile:=Nvl(totaleEsigibile,0) + Nvl(cv_totale_dettaglio,0);

               BEGIN

                  INSERT INTO VP_STM_IVA_DIFFERITA
                         (id_report,
                          sezione,
                          tipologia_riga,
                          sequenza,
                          sottogruppo,
                          ragione_sociale,
                          imponibile,
                          iva,
                          iva_indetraibile,
                          totale,
                          provvisorio_definitivo,
                          acquisto_vendita,
                          descrizione_gruppo)
                  VALUES (repID,
                          gruppoStm,
                          'T',
                          aSequenza,
                          sottoGruppoStm,
                          descrizioneTotale,
                          cv_imponibile_dettaglio,
                          cv_iva_dettaglio,
                          cv_iva_indetraibile_dettaglio,
                          cv_totale_dettaglio,
                          aTipoReport,
                          aTipoRegistro,
                          descrizioneGruppoStm);

               END;

            END LOOP;

            CLOSE gen_cv;
      ELSIF i = 5 THEN
             If aTipoRegistro = 'A' THEN
               descrizioneTotale:='TOTALE FATTURE RICEVUTE NELL''ESERCIZIO, NON ANCORA ESIGIBILI';
            ELSE
              descrizioneTotale:='TOTALE FATTURE EMESSE NELL''ESERCIZIO, NON ANCORA ESIGIBILI';
            END IF;


            OPEN gen_cv FOR

          Select Nvl(SUM(Nvl(imponibile,0)),0),
                        Nvl(SUM(Nvl(iva,0)),0),
                        Nvl(SUM(Nvl(iva_indetraibile,0)),0),
                        Nvl(SUM(Nvl(totale,0)),0)
                 FROM   VP_STM_IVA_DIFFERITA
                 WHERE  id_report = repID AND
                        sezione = 'D' AND
                        tipologia_riga = 'P';

            -- fetch del cursore fatture e insert in REPORT_GENERICO

            LOOP

               FETCH gen_cv INTO
                     cv_imponibile_dettaglio,
                     cv_iva_dettaglio,
                     cv_iva_indetraibile_dettaglio,
                     cv_totale_dettaglio;

               EXIT WHEN gen_cv%NOTFOUND;

               aSequenza:=aSequenza + 20;

               imponibileEsigibile:=Nvl(imponibileEsigibile,0) + Nvl(cv_imponibile_dettaglio,0);
               ivaEsigibile:=Nvl(ivaEsigibile,0) + Nvl(cv_iva_dettaglio,0);
               ivaIndetraibileEsigibile:=Nvl(ivaIndetraibileEsigibile,0) + ROUND(Nvl(cv_iva_indetraibile_dettaglio,0),2);
               totaleEsigibile:=Nvl(totaleEsigibile,0) + Nvl(cv_totale_dettaglio,0);

               BEGIN

                  INSERT INTO VP_STM_IVA_DIFFERITA
                         (id_report,
                          sezione,
                          tipologia_riga,
                          sequenza,
                          sottogruppo,
                          ragione_sociale,
                          imponibile,
                          iva,
                          iva_indetraibile,
                          totale,
                          provvisorio_definitivo,
                          acquisto_vendita,
                          descrizione_gruppo)
                  VALUES (repID,
                          gruppoStm,
                          'T',
                          aSequenza,
                          sottoGruppoStm,
                          descrizioneTotale,
                          cv_imponibile_dettaglio,
                          cv_iva_dettaglio,
                          cv_iva_indetraibile_dettaglio,
                          cv_totale_dettaglio,
                          aTipoReport,
                          aTipoRegistro,
                          descrizioneGruppoStm);

               END;

            END LOOP;

            CLOSE gen_cv;

      ELSIF i = 6 THEN
            If aTipoRegistro = 'A' THEN
               descrizioneTotale:='TOTALE FATTURE RICEVUTE IN ESERCIZI PRECEDENTI, NON ANCORA ESIGIBILI';
            ELSE
               descrizioneTotale:='TOTALE FATTURE EMESSE IN ESERCIZI PRECEDENTI, NON ANCORA ESIGIBILI';
            END IF;

            OPEN gen_cv FOR
                       Select Nvl(SUM(Nvl(imponibile,0)),0),
                        Nvl(SUM(Nvl(iva,0)),0),
                        Nvl(SUM(Nvl(iva_indetraibile,0)),0),
                        Nvl(SUM(Nvl(totale,0)),0)
                 FROM   VP_STM_IVA_DIFFERITA
                 WHERE  id_report = repID AND
                        sezione = 'E' AND
                        tipologia_riga = 'P';

            -- fetch del cursore fatture e insert in REPORT_GENERICO

            LOOP

               FETCH gen_cv INTO
                     cv_imponibile_dettaglio,
                     cv_iva_dettaglio,
                     cv_iva_indetraibile_dettaglio,
                     cv_totale_dettaglio;

               EXIT WHEN gen_cv%NOTFOUND;

               aSequenza:=aSequenza + 20;

               imponibileEsigibile:=Nvl(imponibileEsigibile,0) + Nvl(cv_imponibile_dettaglio,0);
               ivaEsigibile:=Nvl(ivaEsigibile,0) + Nvl(cv_iva_dettaglio,0);
               ivaIndetraibileEsigibile:=Nvl(ivaIndetraibileEsigibile,0) + ROUND(Nvl(cv_iva_indetraibile_dettaglio,0),2);
               totaleEsigibile:=Nvl(totaleEsigibile,0) + Nvl(cv_totale_dettaglio,0);

            BEGIN

               INSERT INTO VP_STM_IVA_DIFFERITA
                      (id_report,
                       sezione,
                       tipologia_riga,
                       sequenza,
                       sottogruppo,
                       ragione_sociale,
                       imponibile,
                       iva,
                       iva_indetraibile,
                       totale,
                       provvisorio_definitivo,
                       acquisto_vendita,
                       descrizione_gruppo)
               VALUES (repID,
                       gruppoStm,
                       'T',
                       aSequenza,
                       sottoGruppoStm,
                       descrizioneTotale,
                       imponibileEsigibile,
                       ivaEsigibile,
                       ivaIndetraibileEsigibile,
                       totaleEsigibile,
                       aTipoReport,
                       aTipoRegistro,
                       descrizioneGruppoStm);
            END;

            END LOOP;

            CLOSE gen_cv;
      ELSIF i = 7 Then
      -- RP Gestito solo per gli Acquisti Fatture divenute esigibile perchè trascorso 1 anno
            If aTipoRegistro = 'A' THEN
               descrizioneTotale:='TOTALE FATTURE RICEVUTE IN ESERCIZI PRECEDENTI, DIVENUTE ESIGIBILI D''UFFUCIO';
            End IF;

            OPEN gen_cv FOR

                 Select Nvl(SUM(Nvl(imponibile,0)),0),
                        Nvl(SUM(Nvl(iva,0)),0),
                        Nvl(SUM(Nvl(iva_indetraibile,0)),0),
                        Nvl(SUM(Nvl(totale,0)),0)
                 FROM   VP_STM_IVA_DIFFERITA
                 WHERE  id_report = repID AND
                        sezione = 'F' AND
                        tipologia_riga = 'P';

            -- fetch del cursore fatture e insert in REPORT_GENERICO

            LOOP

               FETCH gen_cv INTO
                     cv_imponibile_dettaglio,
                     cv_iva_dettaglio,
                     cv_iva_indetraibile_dettaglio,
                     cv_totale_dettaglio;

               EXIT WHEN gen_cv%NOTFOUND;

               aSequenza:=aSequenza + 20;

               imponibileEsigibile:=Nvl(imponibileEsigibile,0) + Nvl(cv_imponibile_dettaglio,0);
               ivaEsigibile:=Nvl(ivaEsigibile,0) + Nvl(cv_iva_dettaglio,0);
               ivaIndetraibileEsigibile:=Nvl(ivaIndetraibileEsigibile,0) + ROUND(Nvl(cv_iva_indetraibile_dettaglio,0),2);
               totaleEsigibile:=Nvl(totaleEsigibile,0) + Nvl(cv_totale_dettaglio,0);

            BEGIN

               INSERT INTO VP_STM_IVA_DIFFERITA
                      (id_report,
                       sezione,
                       tipologia_riga,
                       sequenza,
                       sottogruppo,
                       ragione_sociale,
                       imponibile,
                       iva,
                       iva_indetraibile,
                       totale,
                       provvisorio_definitivo,
                       acquisto_vendita,
                       descrizione_gruppo)
               VALUES (repID,
                       gruppoStm,
                       'T',
                       aSequenza,
                       sottoGruppoStm,
                       descrizioneTotale,
                       imponibileEsigibile,
                       ivaEsigibile,
                       ivaIndetraibileEsigibile,
                       totaleEsigibile,
                       aTipoReport,
                       aTipoRegistro,
                       descrizioneGruppoStm);
            END;

            END LOOP;

            CLOSE gen_cv;
      END IF;

   END LOOP;

EXCEPTION

   WHEN DUP_VAL_ON_INDEX THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Chiave duplicata in inserimento riepilogo iva differita su tabella REPORT_GENERICO' );

END insRiepilogoIvaDifferita;

-- =============================================================================
-- Inserimento Riporto dati dichiarazione di intento su registro Vendite
-- =============================================================================
-- procedure insRiportoDatiIntento
--    (
--     repID INTEGER,
--     aSequenza IN OUT INTEGER,
--     aPasso INTEGER,
--     aCdCdsOrigine VARCHAR2,
--     aCdUoOrigine VARCHAR2,
--     aEsercizio NUMBER,
--     aCdTipoSezionale VARCHAR2,
--     aDataInizio DATE,
--     aDataFine DATE,
--     aTipoRegistro VARCHAR2,
--     aRistampa VARCHAR2,
--     gruppoStm CHAR,
--     sottoGruppoStm CHAR,
--     descrizioneGruppoStm VARCHAR2
--    ) AS
--
--    gen_cv GenericCurTyp;
--    tipo VARCHAR2(10);
-- BEGIN
--
--    IF aPasso = 5 THEN
--       IF aTipoRegistro = 'V' THEN
--          IF aRistampa = 'N' THEN
--             BEGIN
--                IF (aDataFine - aDataInizio) > 30 THEN
--                   tipo:= 'TRIMESTRE';
--                ELSE
--                  tipo:= 'MESE';
--                END IF;
--                IF tipo = 'MESE' THEN
--                   OPEN gen_cv FOR
--                        SELECT LTRIM(RTRIM(B.COGNOME || ' ' || nome || ragione_sociale)),
--                               A.ID_DICHIARAZIONE,
--                               A.DT_INI_VALIDITA,
--                               DT_COMUNICAZIONE_DIC
--                        FROM   DICHIARAZIONE_INTENTO A, ANAGRAFICO B
--                        WHERE  A.CD_ANAG = B.CD_ANAG AND
--                               A.DT_INSERIMENTO_REGISTRO IS NULL AND
--                               (
--                                   (A.DT_COMUNICAZIONE_DIC BETWEEN aDataInizio AND aDataFine) OR
--                                   (A.DT_COMUNICAZIONE_DIC BETWEEN aDataInizio-31 AND aDataInizio-1)
--                                );
--
--
--                ELSE
--                   OPEN gen_cv FOR
--                        SELECT LTRIM(RTRIM(cognome || ' ' || nome || ragione_sociale)),
--                               ID_dichiarazione,
--                               A.DT_INI_VALIDITA,
--                               dt_comunicazione_dic
--                        FROM   DICHIARAZIONE_INTENTO A, ANAGRAFICO B
--                        WHERE  A.CD_ANAG = B.CD_ANAG AND
--                               A.DT_inserimento_registro IS NULL AND
--                               (
--                                   (A.dt_comunicazione_dic BETWEEN aDataInizio AND aDataFine) OR
--                                   (A.dt_comunicazione_dic BETWEEN aDataInizio-91 AND aDataInizio-1)
--                               );
--                END IF;
--         -- Inizio ciclo
--                LOOP
--                   FETCH gen_cv INTO
--                         cv_ragione_sociale,
--                         cv_numero_dichiarazione,
--                         cv_data_inizio_validita,
--                         cv_data_comunicazione_dic;
--                   EXIT WHEN gen_cv%NOTFOUND;
--                   aSequenza:=aSequenza + 20;
--                BEGIN
--                      INSERT INTO REPORT_GENERICO
--                             (id,
--                              chiave,
--                              tipo,
--                              sequenza,
--                              descrizione,
--                              attributo_1,
--                              attributo_4,
--                              data_1,
--                              data_2,
--                              attributo_20
--                             )
--                      VALUES (repID,
--                              gruppoStm,
--                              'D',
--                              aSequenza,
--                              sottoGruppoStm,
--                              cv_ragione_sociale,
--                              cv_numero_dichiarazione,
--                              cv_data_inizio_validita,
--                              cv_data_comunicazione_dic,
--                              descrizioneGruppoStm
--                             );
--                      IF tipo = 'MESE' THEN
--                         UPDATE DICHIARAZIONE_INTENTO
--                         SET    dt_inserimento_registro = aDataFine
--                    WHERE  dt_inserimento_registro IS NULL AND
--                                (
--                                   (dt_comunicazione_dic BETWEEN aDataInizio AND aDataFine) OR
--                                   (dt_comunicazione_dic BETWEEN aDataInizio-31 AND aDataInizio-1)
--                                 );
--                      ELSE
--                        UPDATE DICHIARAZIONE_INTENTO
--                         SET    dt_inserimento_registro = aDataFine
--                         WHERE  dt_inserimento_registro IS NULL AND
--                                (
--                                (dt_comunicazione_dic BETWEEN aDataInizio AND aDataFine) OR
--                                 (dt_comunicazione_dic BETWEEN aDataInizio-91 AND aDataInizio-1)
--                                );
--                      END IF;
--                EXCEPTION
--                     WHEN DUP_VAL_ON_INDEX THEN
--                           Ibmerr001.RAISE_ERR_GENERICO ('Riporto dati dichiarazione di intento, già stampati.' );
--               END;
--                END LOOP;
--                -- close cursore
--                CLOSE gen_cv;
--             END;
--          -- se è una ristampa considera solo quelli già aggiornati alla data fine periodo
--          ELSE
--             BEGIN
--               OPEN gen_cv FOR
--                     SELECT LTRIM(RTRIM(cognome || ' ' || nome || ragione_sociale)),
--                            id_dichiarazione,
--                            a.DT_INI_VALIDITA,
--                            dt_comunicazione_dic
--                     FROM   DICHIARAZIONE_INTENTO A, ANAGRAFICO B
--                     WHERE  A.cd_anag = B.cd_anag AND
--                            A.ESERCIZIO = aEsercizio AND
--                            A.dt_inserimento_registro = aDataFine ;
--                LOOP
--                   FETCH gen_cv INTO
--                         cv_ragione_sociale,
--                         cv_numero_dichiarazione,
--                         cv_data_inizio_validita,
--                         cv_data_comunicazione_dic;
--
--                   EXIT WHEN gen_cv%NOTFOUND;
--
--                   aSequenza:=aSequenza + 20;
--                BEGIN
--                      INSERT INTO REPORT_GENERICO
--                             (id,
--                              chiave,
--                              tipo,
--                              sequenza,
--                              descrizione,
--                              attributo_1,
--                              attributo_4,
--                              data_1,
--                              data_2,
--                              attributo_20
--                             )
--                      VALUES (repID,
--                              gruppoStm,
--                              'D',
--                              aSequenza,
--                              sottoGruppoStm,
--                              cv_ragione_sociale,
--                              cv_numero_dichiarazione,
--                              cv_data_inizio_validita,
--                              cv_data_comunicazione_dic,
--                              descrizioneGruppoStm
--                             );
--                EXCEPTION
--                     WHEN DUP_VAL_ON_INDEX THEN
--                           Ibmerr001.RAISE_ERR_GENERICO ('Dichiarazione di intento, già stampata' );
--                END;
--                END LOOP;
--                -- close cursore
--                CLOSE gen_cv;
--             END;
--          END IF;
--       END IF;
--    END IF;
-- END insRiportoDatiIntento;

-- =============================================================================
-- Inserimento Riporto dati revoca dichiarazione di intento su registro Vendite
-- =============================================================================
-- procedure insRevocaDatiIntento
--    (
--     repID INTEGER,
--     aSequenza IN OUT INTEGER,
--     aPasso INTEGER,
--  aCdCdsOrigine VARCHAR2,
--  aCdUoOrigine VARCHAR2,
--  aEsercizio NUMBER,
--  aCdTipoSezionale VARCHAR2,
--     aDataInizio DATE,
--     aDataFine DATE,
--     aTipoRegistro VARCHAR2,
--     aRistampa VARCHAR2,
--     gruppoStm CHAR,
--     sottoGruppoStm CHAR,
--     descrizioneGruppoStm VARCHAR2
--    )  AS
--    retCode INTEGER;
--    gen_cv GenericCurTyp;
--    tipo VARCHAR2(10);
--
-- BEGIN
--
--  IF aPasso = 6 THEN
--
--   IF aTipoRegistro = 'V' THEN
--
--    IF aRistampa = 'N' THEN
--
--      BEGIN
--
--  IF (aDataFine - aDataInizio)>30 THEN
--     tipo:= 'TRIMESTRE';
--  ELSE
--     tipo:= 'MESE';
--  END IF;
--
--  IF tipo = 'MESE' THEN
--
--         OPEN gen_cv FOR
--
--              SELECT DECODE(ragione_sociale, ' ', cognome || ' ' || nome, ragione_sociale) AS denominazione,
--                     id_dichiarazione,
--                     dt_ini_validita,
--                     dt_comunicazione_rev
--              FROM   DICHIARAZIONE_INTENTO A, ANAGRAFICO B
--              WHERE  A.cd_anag = B.cd_anag AND
--                     A.DT_REVOCA_REGISTRO IS NULL AND
--                     ((A.dt_comunicazione_rev BETWEEN aDataInizio AND aDataFine) OR
--                      (A.dt_comunicazione_rev BETWEEN aDataInizio-31 AND aDataInizio-1));
--
--  ELSE
--
--         OPEN gen_cv FOR
--
--              SELECT DECODE(ragione_sociale, ' ', cognome || ' ' || nome, ragione_sociale) AS denominazione,
--                     id_dichiarazione,
--                     dt_ini_validita,
--                     A.dt_comunicazione_rev
--              FROM   DICHIARAZIONE_INTENTO A, ANAGRAFICO B
--              WHERE  A.cd_anag = B.cd_anag AND
--                     A.dt_revoca_registro IS NULL AND
--                     ((A.dt_comunicazione_rev BETWEEN aDataInizio AND aDataFine) OR
--                       (A.dt_comunicazione_rev BETWEEN aDataInizio-91 AND aDataInizio-1));
--
--  END IF;
--
--
--        LOOP
--
--          FETCH gen_cv INTO
--                cv_ragione_sociale,
--                cv_numero_dichiarazione,
--                cv_data_inizio_validita,
--                cv_data_comunicazione_rev;
--
--          EXIT WHEN gen_cv%NOTFOUND;
--
--          aSequenza:=aSequenza + 20;
--
--
--          BEGIN
--
--             INSERT INTO REPORT_GENERICO
--                   (id,
--                    chiave,
--                    tipo,
--                    sequenza,
--                    descrizione,
--                    attributo_1,
--                    attributo_4,
--                    data_1,
--                    data_2,
--                    attributo_20
--                   )
--            VALUES (repID,
--                    gruppoStm,
--                    'E',
--                    aSequenza,
--                    'Revoca dichiarazione d''intento',
--                    cv_ragione_sociale,
--                    cv_numero_dichiarazione,
--                    cv_data_inizio_validita,
--                    cv_data_comunicazione_rev,
--                    descrizioneGruppoStm
--                   );
--
--          IF tipo = 'MESE' THEN
--
--
--      UPDATE DICHIARAZIONE_INTENTO
--      SET dt_revoca_registro = aDataFine
--              WHERE dt_revoca_registro IS NULL AND
--                  ((dt_comunicazione_rev BETWEEN aDataInizio AND aDataFine) OR
--                       (dt_comunicazione_rev BETWEEN aDataInizio-31 AND aDataInizio-1));
--
--    ELSE
--
--      UPDATE DICHIARAZIONE_INTENTO
--      SET dt_revoca_registro = aDataFine
--              WHERE dt_revoca_registro IS NULL AND
--                  ((dt_comunicazione_rev BETWEEN aDataInizio AND aDataFine) OR
--                       (dt_comunicazione_rev BETWEEN aDataInizio-91 AND aDataInizio-1));
--
--    END IF;
--
--
--          EXCEPTION
--
--             WHEN DUP_VAL_ON_INDEX THEN
--                  Ibmerr001.RAISE_ERR_GENERICO ('Revoca dichiarazione di intento, già stampata ');
--          END;
--
--       END LOOP;
--
--       -- close cursore
--       CLOSE gen_cv;
--
--      END;
--
--    -- se è una ristampa considera solo quelli già aggiornati alla data fine periodo
--    ELSE
--
--      BEGIN
--
--         OPEN gen_cv FOR
--
--              SELECT DECODE(ragione_sociale, ' ', cognome || ' ' || nome, ragione_sociale) AS denominazione,
--                     id_dichiarazione,
--                     dt_ini_validita,
--                     dt_comunicazione_rev
--              FROM   DICHIARAZIONE_INTENTO A, ANAGRAFICO B
--              WHERE  A.cd_anag = B.cd_anag AND
--                     A.ESERCIZIO = aEsercizio AND
--                     A.dt_revoca_registro = aDataFine ;
--
--        LOOP
--
--          FETCH gen_cv INTO
--                cv_ragione_sociale,
--                cv_numero_dichiarazione,
--                cv_data_inizio_validita,
--                cv_data_comunicazione_rev;
--
--          EXIT WHEN gen_cv%NOTFOUND;
--
--          aSequenza:=aSequenza + 20;
--
--
--          BEGIN
--
--             INSERT INTO REPORT_GENERICO
--                   (id,
--                    chiave,
--                    tipo,
--                    sequenza,
--                    descrizione,
--                    attributo_1,
--                    attributo_4,
--                    data_1,
--                    data_2,
--                    attributo_20
--                   )
--            VALUES (repID,
--                    gruppoStm,
--                    'E',
--                    aSequenza,
--                    'Revoca dichiarazione d''intento',
--                    cv_ragione_sociale,
--                    cv_numero_dichiarazione,
--                    cv_data_inizio_validita,
--                    cv_data_comunicazione_rev,
--                    descrizioneGruppoStm
--                   );
--
--
--          EXCEPTION
--
--             WHEN DUP_VAL_ON_INDEX THEN
--                  Ibmerr001.RAISE_ERR_GENERICO ('Revoca dichiarazione di intento, già stampata'  );
--          END;
--       END LOOP;
--       -- close cursore
--       CLOSE gen_cv;
--     END;
--    END IF;
--  END IF;
-- END IF;
-- END insRevocaDatiIntento;
--
--
--
--  =============================================================================
--  Stampa Annotazioni libere
--  =============================================================================
--  FUNCTION insAnnotazioniLibere
--     (
--     repID INTEGER,
--     aSequenza IN OUT INTEGER,
--     aPasso INTEGER,
--     codiceSezionale VARCHAR2,
--     prefissoSezionale VARCHAR2,
--     descrizioneSezionale VARCHAR2,
--     aCodiceEsercizio VARCHAR2,
--     aDataInizio DATE,
--     aDataFine DATE,
--     aTipoRegistro VARCHAR2,
--     gruppoStm CHAR,
--     sottoGruppoStm CHAR,
--     descrizioneGruppoStm VARCHAR2
--    ) RETURN NUMBER AS
--    retCode INTEGER;
--    gen_cv GenericCurTyp;
--
-- BEGIN
--
--  IF aPasso = 7 THEN
--
--    BEGIN
--
--         OPEN gen_cv FOR
--
--              SELECT note
--              FROM   NOTE_REGISTRO_IVA
--              WHERE  esercizio = aCodiceEsercizio AND
--                     sezionale = codiceSezionale AND
--                     SUBSTR(tipo_registro,1,1) = aTipoRegistro AND
--                     (data_inizio BETWEEN aDataInizio AND aDataFine) AND
--                     (data_fine BETWEEN aDataInizio AND aDataFine) AND
--                     note IS NOT NULL;
--
--        LOOP
--
--          FETCH gen_cv INTO
--                cv_note;
--
--          EXIT WHEN gen_cv%NOTFOUND;
--
--          aSequenza:=aSequenza + 20;
--
--          BEGIN
--
--             INSERT INTO REPORT_GENERICO
--                   (id,
--                    chiave,
--                    tipo,
--                    sequenza,
--                    descrizione,
--                    attributo_1,
--                    attributo_3,
--                    attributo_20
--                   )
--            VALUES (repID,
--                    gruppoStm,
--                    'F',
--                    aSequenza,
--                    'Annotazioni Libere',
--                    cv_note,
--                    codiceSezionale,
--                    descrizioneGruppoStm
--                   );
--
--          EXCEPTION
--
--             WHEN dup_val_on_index THEN
--                  errMsg := substr(SQLERRM, 1, 200);
--                  ROLLBACK;
--                  ibmerr001.RAISE_ERR_GENERICO
--                    (errMsg || CHR(10) || 'PASSO ' || aPasso ||
--                     ' - Chiave duplicata in inserimento annotazioni libere');
--                  RETURN 1;
--
--             WHEN others THEN
--                  errMsg := substr(SQLERRM, 1, 200);
--                  ROLLBACK;
--                  ibmerr001.RAISE_ERR_GENERICO
--                     (errMsg || CHR(10) || 'Passo ' || aPasso ||
--                     ' - Errore generico in inserimento annotazioni libere');
--                 RETURN 1;
--
--          END;
--
--       END LOOP;
--
--       -- close cursore
--
--       CLOSE gen_cv;
--
--    EXCEPTION
--
--       WHEN others THEN
--            errMsg := substr(SQLERRM, 1, 200);
--            ROLLBACK;
--            ibmerr001.RAISE_ERR_GENERICO
--               (
--                errMsg || CHR(10) || 'Passo ' || aPasso ||
--                ' - Errore generico in select annotazioni libere');
--            RETURN 1;
--
--    END;
--
--  END IF;
--
-- RETURN 0;
--
-- END insAnnotazioniLibere;

-- =================================================================================================
-- Lettura liquidazione. Sulla tabella REPORT_GENERICO si memorizzano:
-- 1 dati letti dalla liquidazione e non calcolati dal programma
-- 2 dati calcolati dal programma
-- =================================================================================================
PROCEDURE letturaLiquidazione
   (
    repID INTEGER,
    aCdCds VARCHAR2,
    aEsercizio NUMBER,
    aCdUo VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2,
    inGruppoReport VARCHAR2
   ) IS
   aRecLiquidazioneIva LIQUIDAZIONE_IVA%ROWTYPE;
   gen_cv GenericCurTyp;

BEGIN

   ----------------------------------------------------------------------------------------------
   -- Lettura dati della liquidazione e memorizzazione di quelli non calcolati dalla procedura.
   -- Sia quella provvisoria che quella definitiva utilizzato il riferimento repID

   BEGIN

      OPEN gen_cv FOR

           SELECT NVL(var_imp_per_prec,0),
                  NVL(iva_non_vers_per_prec,0),
                  NVL(iva_deb_cred_per_prec,0),
                  NVL(cred_iva_comp_detr,0),
                  NVL(int_deb_liq_trim,0),
                  NVL(cred_iva_spec_detr,0),
                  NVL(acconto_iva_vers,0),
                  nvl(iva_liq_esterna,0)
           FROM   LIQUIDAZIONE_IVA
           WHERE  cd_cds = aCdCds AND
                  esercizio = aEsercizio AND
                  cd_unita_organizzativa = aCdUo AND
                  tipo_liquidazione = inGruppoReport AND
                  dt_inizio = aDataInizio AND
                  dt_fine = aDataFine AND
                  report_id = repID;

      LOOP

         FETCH gen_cv INTO
               aRecLiquidazioneIva.var_imp_per_prec,
               aRecLiquidazioneIva.iva_non_vers_per_prec,
               aRecLiquidazioneIva.iva_deb_cred_per_prec,
               aRecLiquidazioneIva.cred_iva_comp_detr,
               aRecLiquidazioneIva.int_deb_liq_trim,
               aRecLiquidazioneIva.cred_iva_spec_detr,
               aRecLiquidazioneIva.acconto_iva_vers,
               aRecLiquidazioneIva.iva_liq_esterna;

         EXIT WHEN gen_cv%NOTFOUND;

         ----------------------------------------------------------------------------------------
         -- Inserimento record relativo ai dati letti dalla liquidazione e non calcolati dalla procedura
         -- e preparazione record per il calcolato dal sistema

         BEGIN

            -- Inserimento record relativo ai dati letti dalla liquidazione e non calcolati dalla procedura

            INSERT INTO REPORT_GENERICO
                   (id,
                    chiave,
                    tipo,
                    sequenza,
                    descrizione,
                    importo_1,
                    importo_2,
                    importo_3,
                    importo_4,
                    importo_5,
                    importo_6,
                    importo_7,
                    importo_16,
                    attributo_40)
            VALUES (repID,
                    gruppoStm,
                    'D',
                    1,
                    sottoGruppoStm,
                    aRecLiquidazioneIva.var_imp_per_prec,
                    aRecLiquidazioneIva.iva_non_vers_per_prec,
                    aRecLiquidazioneIva.iva_deb_cred_per_prec,
                    aRecLiquidazioneIva.cred_iva_comp_detr,
                    aRecLiquidazioneIva.int_deb_liq_trim,
                    aRecLiquidazioneIva.cred_iva_spec_detr,
                    aRecLiquidazioneIva.acconto_iva_vers,
                    aRecLiquidazioneIva.iva_liq_esterna,
                    descrizioneGruppoStm);

            -- Inserimento (azzeramento) record dell'attuale liquidazione (calcolato dal sistema)

            INSERT INTO VP_LIQUIDAZIONE_IVA
                   (id_report,
                    chiave,
                    tipo,
                    sequenza,
                    descrizione,
                    rp_iva_ven,
                    rp_iva_ven_diff,
                    rp_iva_ven_diff_esig,
                    rp_iva_ven_diff_es_prec_esig,
                    rp_iva_ven_autofatt,
                    rp_iva_ven_intraue,
                    rp_iva_acq,
                    rp_iva_acq_non_detr,
                    rp_iva_acq_diff,
                    rp_iva_acq_diff_esig,
                    rp_iva_acq_diff_es_prec_esig,
                    rp_iva_non_vers_per_prec,
                    rp_iva_deb_cred_per_prec,
                    rp_iva_cred_no_prorata,
                    rp_perc_prorata_detraibile,
                    rp_gestione_prorata,
                    rp_esercizio_euro,
                    rp_ds_gruppo_stampa,
                    rp_iva_liq_esterna,
                    rp_iva_acq_split_payment,
                    rp_iva_ven_split_payment)
            VALUES (repID,
                    gruppoStm,
                    'D',
                    2,
                    sottoGruppoStm,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    aRecLiquidazioneIva.iva_non_vers_per_prec,
                    0,
                    0,
                    0,
                    NULL,
                    NULL,
                    descrizioneGruppoStm,
                    aRecLiquidazioneIva.iva_liq_esterna,
                    0,
                    0);

         EXCEPTION

            WHEN DUP_VAL_ON_INDEX THEN
                 IBMERR001.RAISE_ERR_GENERICO
                    ('Chiave duplicata in inserimento riepilogo iva su tabella REPORT_GENERICO');
         END;

      END LOOP;

      CLOSE gen_cv;

   END;

END letturaLiquidazione;

-- =================================================================================================
-- Valorizzazione elementi della liquidazione calcolati dal sistema
-- =================================================================================================
PROCEDURE calcolaLiquidazione
   (
    repID INTEGER,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    acquistiVendite INTEGER,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2,
    aEsercizioEuro VARCHAR2,
    aGestioneProrata VARCHAR2,
    aEsercizioReale NUMBER,
    inGruppoReport VARCHAR2
   ) IS

   aDataInizioLiqPrec DATE;
   aDataFineLiqPrec DATE;
   aEsercizioFineLiqPrec NUMBER(4);
   aCreDebPrecedente NUMBER(15,2);
   aIvaCreditoPeriodoPrec NUMBER(15,2);
   aPercentualeProrata NUMBER(15,2);
   aEsercizioPerProrata NUMBER(4);

   aRecLiquidazioneIva LIQUIDAZIONE_IVA%ROWTYPE;
   aRecReportLiquidazioneIva VP_LIQUIDAZIONE_IVA%ROWTYPE;

   gen_cv GenericCurTyp;

Begin
   -------------------------------------------------------------------------------------------------
   -- Lettura dell'eventuale credito/debito dalla precedente liquidazione.
   -- Si legge il campo LIQUIDAZIONE_IVA.iva_da_versare della liquidazione definitiva precedente al
   -- periodo in gestione. Si mantiene il campo solo se positivo (credito) o negativo <= 26 euro
   -- (debito non versato)

   aDataFineLiqPrec:=aDataInizio - 1;
   aDataInizioLiqPrec:=IBMUTL001.getFirstDayOfMonth(aDataFineLiqPrec);
   aEsercizioFineLiqPrec:=TO_NUMBER(TO_CHAR(aDataFineLiqPrec,'YYYY'));

   -- L'esercizio è corretto per gestire il fatto che il mese di dicembre è sempre liquidato
   -- nell'esercizio successivo

   IF TO_CHAR(aDataFineLiqPrec,'MM') = '12' and aEsercizioFineLiqPrec=2015 THEN
          aEsercizioFineLiqPrec:=aEsercizioFineLiqPrec;
   else
        IF TO_CHAR(aDataFineLiqPrec,'MM') = '12' then
           aEsercizioFineLiqPrec:=aEsercizioFineLiqPrec + 1;
        end if;
   END IF;

   -- Lettura dell'eventuale credito precedente

   BEGIN

      SELECT NVL(iva_da_versare,0) INTO aCreDebPrecedente
      FROM   LIQUIDAZIONE_IVA
      WHERE  cd_cds = aCdCdsOrigine AND
             esercizio = aEsercizioFineLiqPrec AND
             cd_unita_organizzativa = aCdUoOrigine AND
             tipo_liquidazione = inGruppoReport AND
             dt_inizio = aDataInizioLiqPrec AND
             dt_fine = aDataFineLiqPrec AND
             stato = 'D';

   EXCEPTION

      WHEN NO_DATA_FOUND THEN
           aCreDebPrecedente:=0;

   END;

   aIvaCreditoPeriodoPrec:=0;

   -- Controllo del valore del credito del periodo precedente. L'eventuale debito inferiore a 26 euro
   -- non è memorizzato in quanto comunque versato dalle singole Uo.

   IF    aCreDebPrecedente > 0 THEN
         aIvaCreditoPeriodoPrec:=aCreDebPrecedente;
   END IF;

   UPDATE VP_LIQUIDAZIONE_IVA
   SET    rp_iva_deb_cred_per_prec = aIvaCreditoPeriodoPrec
   WHERE  id_report = repID AND
          chiave = gruppoStm AND
          tipo = 'D' AND
          sequenza = 2;

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale di lettura dei dati da calcolare per la liquidazione (no esigibilità differita)

   BEGIN

      IF acquistiVendite = 1 THEN

         OPEN gen_cv FOR

              SELECT cd_cds_origine,
                     cd_uo_origine,
                     esercizio,
                     cd_tipo_sezionale,
                     ti_fattura,
                     data_registrazione,
                     sezione_liquidazione,
                     ti_istituz_commerc,
                     SUM(NVL(iva_dettaglio,0)),
                     SUM(TRUNC(iva_indetraibile_dettaglio,2)),
                     esigibilita_diff,
                     tipo_documento_ft_pas,
                     tipo_autofattura,
                     fl_split_payment
              FROM   V_LIQUIDAZIONE_IVA_ACQUISTI
              WHERE  cd_cds_origine = aCdCdsOrigine AND
                     cd_uo_origine = aCdUoOrigine AND
                     esercizio = aEsercizioReale AND
                     cd_tipo_sezionale = aCdTipoSezionale AND
                     data_registrazione >= aDataInizio AND
                     data_registrazione <= aDataFine
              GROUP BY cd_cds_origine,
                       cd_uo_origine,
                       esercizio,
                       cd_tipo_sezionale,
                       ti_fattura,
                       data_registrazione,
                       sezione_liquidazione,
                       ti_istituz_commerc,
                       esigibilita_diff,
                       tipo_documento_ft_pas,
                       tipo_autofattura,
                       fl_split_payment;

      ELSE

         OPEN gen_cv FOR

              SELECT cd_cds_origine,
                     cd_uo_origine,
                     esercizio,
                     cd_tipo_sezionale,
                     ti_fattura,
                     data_registrazione,
                     sezione_liquidazione,
                     ti_istituz_commerc,
                     SUM(NVL(iva_dettaglio,0)),
                     0,
                     esigibilita_diff,
                     tipo_documento_ft_pas,
                     tipo_autofattura,
                     fl_split_payment
              FROM   V_LIQUIDAZIONE_IVA_VENDITE
              WHERE  cd_cds_origine = aCdCdsOrigine AND
                     cd_uo_origine = aCdUoOrigine AND
                     esercizio = aEsercizioReale AND
                     cd_tipo_sezionale = aCdTipoSezionale AND
                     data_emissione >= aDataInizio AND
                     data_emissione <= aDataFine
              GROUP BY cd_cds_origine,
                       cd_uo_origine,
                       esercizio,
                       cd_tipo_sezionale,
                       ti_fattura,
                       data_registrazione,
                       sezione_liquidazione,
                       ti_istituz_commerc,
                       0,
                       esigibilita_diff,
                       tipo_documento_ft_pas,
                       tipo_autofattura,
                       fl_split_payment;

      END IF;

      LOOP

         FETCH gen_cv INTO
               cv_cd_cds_origine,
               cv_cd_uo_origine,
               cv_esercizio,
               cv_cd_tipo_sezionale,
               cv_ti_fattura,
               cv_data_registrazione,
               cv_sezione_liquidazione,
               cv_comm_ist_dettaglio,
               cv_iva_dettaglio,
               cv_iva_indetraibile_dettaglio,
               cv_esigibilita_diff,
               cv_tipo_documento_ft_pas,
               cv_tipo_autofattura,
               cv_fl_split_payment;

         EXIT WHEN gen_cv%NOTFOUND;
Dbms_Output.put_line( cv_cd_cds_origine||' '||
               cv_cd_uo_origine||' '||
               cv_esercizio||' '||
               cv_cd_tipo_sezionale||' '||
               cv_ti_fattura||' '||
               cv_data_registrazione||' '||
               cv_sezione_liquidazione||' '||
               cv_iva_dettaglio||' '||
               cv_tipo_documento_ft_pas||' '||
               cv_fl_split_payment);
         -- Azzeramento importi iva se gestione commerciale o istituzionale

         IF acquistiVendite = 1 THEN
            IF inGruppoReport = 'C' THEN
               IF cv_comm_ist_dettaglio = 'I' THEN
                  cv_iva_dettaglio:=0;
                  cv_iva_indetraibile_dettaglio:=0;
               END IF;
            ELSE
               IF cv_comm_ist_dettaglio = 'C' THEN
                  cv_iva_dettaglio:=0;
                  cv_iva_indetraibile_dettaglio:=0;
               END IF;
            END IF;
         END IF;

         -- Aggiornamento dei dati letti dalla liquidazione calcolati dalla procedura

         aRecReportLiquidazioneIva:=NULL;
         aRecReportLiquidazioneIva.rp_iva_ven:=0;
         aRecReportLiquidazioneIva.rp_iva_ven_diff:=0;
         aRecReportLiquidazioneIva.rp_iva_ven_diff_esig:=0;
         aRecReportLiquidazioneIva.rp_iva_ven_diff_es_prec_esig:=0;
         aRecReportLiquidazioneIva.rp_iva_ven_autofatt:=0;
         aRecReportLiquidazioneIva.rp_iva_ven_intraue:=0;
         aRecReportLiquidazioneIva.rp_iva_ven_split_payment:=0;
         aRecReportLiquidazioneIva.rp_iva_acq:=0;
         aRecReportLiquidazioneIva.rp_iva_acq_non_detr:=0;
         aRecReportLiquidazioneIva.rp_iva_acq_diff:=0;
         aRecReportLiquidazioneIva.rp_iva_acq_diff_esig:=0;
         aRecReportLiquidazioneIva.rp_iva_acq_diff_es_prec_esig:=0;
         aRecReportLiquidazioneIva.rp_iva_acq_split_payment:=0;

         -- Gestione speciale per le liquidazioni istituzionali dove iva acquisti deve essere
         -- considerata come debito
         -- RP inGruppoReport = 'X'
         IF (inGruppoReport = 'I' OR
             inGruppoReport = 'S' Or
             inGruppoReport = 'X' Or
             inGruppoReport = 'P') THEN
            IF cv_sezione_liquidazione = 'AF' THEN
               aRecReportLiquidazioneIva.rp_iva_ven:=cv_iva_dettaglio;
            END IF;
         ELSE
            IF    cv_sezione_liquidazione = 'VF' THEN
                  aRecReportLiquidazioneIva.rp_iva_ven:=cv_iva_dettaglio;
                  IF cv_esigibilita_diff = 'Y' THEN
                     aRecReportLiquidazioneIva.rp_iva_ven_diff:=cv_iva_dettaglio;
                  END IF;
            ELSIF cv_sezione_liquidazione = 'VA' THEN
                  if cv_fl_split_payment = 'Y' Then
                    aRecReportLiquidazioneIva.rp_iva_ven_split_payment:=cv_iva_dettaglio;
                  else
                    aRecReportLiquidazioneIva.rp_iva_ven_autofatt:=cv_iva_dettaglio;
                  end if;
--                  IF cv_tipo_autofattura = '001' THEN
--                     aRecReportLiquidazioneIva.rp_iva_ven_autofatt:=cv_iva_dettaglio;
--                  ELSE
--                     aRecReportLiquidazioneIva.rp_iva_ven_intraue:=cv_iva_dettaglio;
--                  END IF;
                  IF cv_esigibilita_diff = 'Y' THEN
                     aRecReportLiquidazioneIva.rp_iva_ven_diff:=cv_iva_dettaglio;
                  END IF;
            ELSIF cv_sezione_liquidazione = 'AF' THEN
                  aRecReportLiquidazioneIva.rp_iva_acq:=cv_iva_dettaglio;
                  IF cv_esigibilita_diff = 'Y' THEN
                     aRecReportLiquidazioneIva.rp_iva_acq_diff:=cv_iva_dettaglio;
                  ELSE
                     aRecReportLiquidazioneIva.rp_iva_acq_non_detr:=cv_iva_indetraibile_dettaglio;
                  END IF;
                  IF cv_fl_split_payment = 'Y' Then
                     aRecReportLiquidazioneIva.rp_iva_acq_split_payment:=cv_iva_dettaglio;
                  END IF;
            END IF;
         END IF;

         BEGIN
            UPDATE VP_LIQUIDAZIONE_IVA
            SET    rp_iva_ven = rp_iva_ven + aRecReportLiquidazioneIva.rp_iva_ven,
                   rp_iva_ven_diff = rp_iva_ven_diff + aRecReportLiquidazioneIva.rp_iva_ven_diff,
                   rp_iva_ven_diff_esig = rp_iva_ven_diff_esig + aRecReportLiquidazioneIva.rp_iva_ven_diff_esig,
                   rp_iva_ven_diff_es_prec_esig = rp_iva_ven_diff_es_prec_esig + aRecReportLiquidazioneIva.rp_iva_ven_diff_es_prec_esig,
                   rp_iva_ven_autofatt = rp_iva_ven_autofatt + aRecReportLiquidazioneIva.rp_iva_ven_autofatt,
                   rp_iva_ven_intraue = rp_iva_ven_intraue + aRecReportLiquidazioneIva.rp_iva_ven_intraue,
                   rp_iva_ven_split_payment = rp_iva_ven_split_payment + aRecReportLiquidazioneIva.rp_iva_ven_split_payment,
                   rp_iva_acq = rp_iva_acq + aRecReportLiquidazioneIva.rp_iva_acq,
                   rp_iva_acq_non_detr = rp_iva_acq_non_detr + aRecReportLiquidazioneIva.rp_iva_acq_non_detr,
                   rp_iva_acq_diff = rp_iva_acq_diff + aRecReportLiquidazioneIva.rp_iva_acq_diff,
                   rp_iva_acq_diff_esig = rp_iva_acq_diff_esig + aRecReportLiquidazioneIva.rp_iva_acq_diff_esig,
                   rp_iva_acq_diff_es_prec_esig = rp_iva_acq_diff_es_prec_esig + aRecReportLiquidazioneIva.rp_iva_acq_diff_es_prec_esig,
                   rp_iva_acq_split_payment = rp_iva_acq_split_payment + aRecReportLiquidazioneIva.rp_iva_acq_split_payment
            WHERE  id_report = repID AND
                   chiave = gruppoStm AND
                   tipo = 'D' AND
                   sequenza = 2;

         END;

      END LOOP;

      CLOSE gen_cv;

   END;

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale di lettura dei dati da calcolare per la liquidazione (si esigibilità differita)
   -- Si prelevano tutti i record con data esigibilità IVA inferiore alla data di fine, la vista
   -- V_LIQUID_IVA_VENDITE_ESIG garantisce il processo dei soli record mai inclusi in precedenti liquidazioni

   BEGIN

      IF acquistiVendite = 1 Then
         OPEN gen_cv FOR

              SELECT cd_cds_origine,
                     cd_uo_origine,
                     esercizio,
                     cd_tipo_sezionale,
                     ti_fattura,
                     data_registrazione,
                     sezione_liquidazione,
                     SUM(NVL(iva_dettaglio,0))
              FROM   V_LIQUID_IVA_ACQUISTI_ESIG
              WHERE  cd_cds_origine = aCdCdsOrigine AND
                     cd_uo_origine = aCdUoOrigine AND
                     cd_tipo_sezionale = aCdTipoSezionale AND
                     data_esigibilita IS NOT NULL AND
                     data_esigibilita <= aDataFine
              GROUP BY cd_cds_origine,
                     cd_uo_origine,
                     esercizio,
                     cd_tipo_sezionale,
                     ti_fattura,
                     data_registrazione,
                     sezione_liquidazione;
        Elsif acquistiVendite = 2 Then
         OPEN gen_cv FOR

              SELECT cd_cds_origine,
                     cd_uo_origine,
                     esercizio,
                     cd_tipo_sezionale,
                     ti_fattura,
                     data_registrazione,
                     sezione_liquidazione,
                     SUM(NVL(iva_dettaglio,0))
              FROM   V_LIQUID_IVA_VENDITE_ESIG
              WHERE  cd_cds_origine = aCdCdsOrigine AND
                     cd_uo_origine = aCdUoOrigine AND
                     cd_tipo_sezionale = aCdTipoSezionale AND
                     data_esigibilita IS NOT NULL AND
                     data_esigibilita <= aDataFine
              GROUP BY cd_cds_origine,
                     cd_uo_origine,
                     esercizio,
                     cd_tipo_sezionale,
                     ti_fattura,
                     data_registrazione,
                     sezione_liquidazione;
         End If;

         LOOP

            FETCH gen_cv INTO
                  cv_cd_cds_origine,
                  cv_cd_uo_origine,
                  cv_esercizio,
                  cv_cd_tipo_sezionale,
                  cv_ti_fattura,
                  cv_data_registrazione,
                  cv_sezione_liquidazione,
                  cv_iva_dettaglio;

            EXIT WHEN gen_cv%NOTFOUND;
Dbms_Output.put_line('esig '||cv_cd_cds_origine||' '||
               cv_cd_uo_origine||' '||
               cv_esercizio||' '||
               cv_cd_tipo_sezionale||' '||
               cv_ti_fattura||' '||
               cv_data_registrazione||' '||
               cv_sezione_liquidazione||' '||
               cv_iva_dettaglio);
            -- Aggiornamento ai dati letti dalla liquidazione calcolati dalla procedura

            aRecReportLiquidazioneIva:=NULL;
            aRecReportLiquidazioneIva.rp_iva_ven_diff_esig:=0;
            aRecReportLiquidazioneIva.rp_iva_ven_diff_es_prec_esig:=0;
            aRecReportLiquidazioneIva.rp_iva_acq_diff_esig:=0;
            aRecReportLiquidazioneIva.rp_iva_acq_diff_es_prec_esig:=0;

            IF    cv_sezione_liquidazione = 'VE' Then
                  If cv_esercizio != aEsercizio Then
                      aRecReportLiquidazioneIva.rp_iva_ven_diff_es_prec_esig:=cv_iva_dettaglio;
                  Else
                      aRecReportLiquidazioneIva.rp_iva_ven_diff_esig:=cv_iva_dettaglio;
                  End If;
            ELSIF cv_sezione_liquidazione = 'AC' Then
                  If cv_esercizio != aEsercizio Then
                      aRecReportLiquidazioneIva.rp_iva_acq_diff_es_prec_esig:=cv_iva_dettaglio;
                  Else
                      aRecReportLiquidazioneIva.rp_iva_acq_diff_esig:=cv_iva_dettaglio;
                  End If;
            END IF;

            BEGIN

               UPDATE VP_LIQUIDAZIONE_IVA
               SET    rp_iva_ven_diff_esig = rp_iva_ven_diff_esig + aRecReportLiquidazioneIva.rp_iva_ven_diff_esig,
                      rp_iva_ven_diff_es_prec_esig = rp_iva_ven_diff_es_prec_esig + aRecReportLiquidazioneIva.rp_iva_ven_diff_es_prec_esig,
                      rp_iva_acq_diff_esig = rp_iva_acq_diff_esig + aRecReportLiquidazioneIva.rp_iva_acq_diff_esig,
                      rp_iva_acq_diff_es_prec_esig = rp_iva_acq_diff_es_prec_esig + aRecReportLiquidazioneIva.rp_iva_acq_diff_es_prec_esig
               WHERE  id_report = repID AND
                      chiave = gruppoStm AND
                      tipo = 'D' AND
                      sequenza = 2;

            END;

         END LOOP;

         CLOSE gen_cv;

     -- END IF;

   END;

   -------------------------------------------------------------------------------------------------
   -- Calcolo del prorata

   BEGIN

      aPercentualeProrata:=NULL;

      IF (aGestioneProrata = 'Y' AND
          inGruppoReport = CNRCTB250.TI_LIQ_IVA_COMMERC) THEN
         aEsercizioPerProrata:=(aEsercizioReale - 1);

         BEGIN

            SELECT percentuale INTO aPercentualeProrata
            FROM   PRO_RATA
            WHERE  esercizio = aEsercizioPerProrata AND
                   cd_attivita_commerciale = '*';

         EXCEPTION

            WHEN no_data_found THEN
                 aPercentualeProrata:=NULL;

         END;

      END IF;

      UPDATE VP_LIQUIDAZIONE_IVA
      SET    rp_gestione_prorata = aGestioneProrata,
             rp_esercizio_euro = aEsercizioEuro,
             rp_perc_prorata_detraibile = aPercentualeProrata
      WHERE  id_report = repID AND
             chiave = gruppoStm AND
             tipo = 'D' AND
             sequenza = 2;

   END;

END calcolaLiquidazione;


-- =================================================================================================
-- Valorizzazione elementi della liquidazione calcolati dal sistema
-- =================================================================================================
PROCEDURE calcolaLiquidazioneEnte
   (
    repID INTEGER,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2,
    aEsercizioEuro VARCHAR2,
    aGestioneProrata VARCHAR2,
    aEsercizioReale NUMBER,
    inGruppoReport VARCHAR2
   ) IS

   aDataInizioLiqPrec DATE;
   aDataFineLiqPrec DATE;
   aEsercizioFineLiqPrec NUMBER(4);
   aCreDebPrecedente NUMBER(15,2);
   aIvaCreditoPeriodoPrec NUMBER(15,2);
   aPercentualeProrata NUMBER(15,2);
   aEsercizioPerProrata NUMBER(4);

   aRecLiquidazioneIva LIQUIDAZIONE_IVA%ROWTYPE;
   UOENTE unita_organizzativa%rowtype:= CNRCTB020.GETUOENTE(aEsercizio);
   gen_cv GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Lettura dell'eventuale credito/debito dalla precedente liquidazione.
   -- Si legge il campo LIQUIDAZIONE_IVA.iva_da_versare della liquidazione definitiva precedente al
   -- periodo in gestione. Si mantiene il campo solo se positivo (credito) o negativo <= 50.000 o
   -- 26 euro (debito non versato)
   --

   aDataFineLiqPrec:=aDataInizio - 1;
   aDataInizioLiqPrec:=IBMUTL001.getFirstDayOfMonth(aDataFineLiqPrec);
   aEsercizioFineLiqPrec:=TO_NUMBER(TO_CHAR(aDataFineLiqPrec,'YYYY'));

   -- L'esercizio è corretto per gestire il fatto che il mese di dicembre è sempre liquidato
   -- nell'esercizio successivo


  IF TO_CHAR(aDataFineLiqPrec,'MM') = '12' and aEsercizioFineLiqPrec=2015 THEN
          aEsercizioFineLiqPrec:=aEsercizioFineLiqPrec;
   else
        IF TO_CHAR(aDataFineLiqPrec,'MM') = '12' then
           aEsercizioFineLiqPrec:=aEsercizioFineLiqPrec + 1;
        end if;
   END IF;

   -- Lettura dell'eventuale credito del mese precedente

   BEGIN

      SELECT NVL(iva_da_versare,0) INTO aCreDebPrecedente
      FROM   LIQUIDAZIONE_IVA
      WHERE  cd_cds = aCdCdsOrigine AND
             esercizio = aEsercizioFineLiqPrec AND
             cd_unita_organizzativa = aCdUoOrigine AND
             tipo_liquidazione = inGruppoReport AND
             dt_inizio = aDataInizioLiqPrec AND
             dt_fine = aDataFineLiqPrec AND
             stato = 'D';

   EXCEPTION

        WHEN NO_DATA_FOUND THEN
             aCreDebPrecedente:=0;

   END;

   aIvaCreditoPeriodoPrec:=0;

   -- Controllo del valore del credito, debito del periodo precedente

   IF    aCreDebPrecedente > 0 THEN
         aIvaCreditoPeriodoPrec:=aCreDebPrecedente;
   ELSIF aCreDebPrecedente < 0 THEN
         IF aCreDebPrecedente >=-26 THEN
            aIvaCreditoPeriodoPrec:=aCreDebPrecedente;
         END IF;
   END IF;

   UPDATE VP_LIQUIDAZIONE_IVA
   SET    rp_iva_deb_cred_per_prec = aIvaCreditoPeriodoPrec
   WHERE  id_report = repID AND
          chiave = gruppoStm AND
          tipo = 'D' AND
          sequenza = 2;

   -------------------------------------------------------------------------------------------------
   -- Lettura dati della liquidazione e memorizzazione di quelli non calcolati dalla procedura (no differite)
   -- Si include ora anche la gestione dell'iva indetraibile solo per gli acquisti non differiti

   BEGIN

      OPEN gen_cv FOR

           SELECT *
           FROM   LIQUIDAZIONE_IVA
           WHERE  report_id = 0 AND
                  cd_cds !=UOENTE.cd_unita_padre and
                  stato = 'D' AND
                  esercizio = aEsercizio AND
                  tipo_liquidazione = inGruppoReport AND
                  dt_inizio = aDataInizio AND
                  dt_fine = aDataFine;

      LOOP

         FETCH gen_cv INTO
               aRecLiquidazioneIva;

         EXIT WHEN gen_cv%NOTFOUND;

         -- Aggiornamento dei dati letti dalle liquidazioni delle diverse UO

         BEGIN

            UPDATE VP_LIQUIDAZIONE_IVA
            SET    rp_iva_ven = rp_iva_ven + aRecLiquidazioneIva.iva_vendite,
                   rp_iva_ven_diff = rp_iva_ven_diff + aRecLiquidazioneIva.iva_vendite_diff,
                   rp_iva_ven_diff_esig = rp_iva_ven_diff_esig + aRecLiquidazioneIva.iva_vend_diff_esig,
                   rp_iva_ven_diff_es_prec_esig = rp_iva_ven_diff_es_prec_esig + aRecLiquidazioneIva.iva_vend_diff_es_prec_esig,
                   rp_iva_ven_autofatt = rp_iva_ven_autofatt + aRecLiquidazioneIva.iva_autofatt,
                   rp_iva_ven_intraue = rp_iva_ven_intraue + aRecLiquidazioneIva.iva_intraue,
                   rp_iva_ven_split_payment = rp_iva_ven_split_payment + aRecLiquidazioneIva.iva_ven_split_payment,
                   rp_iva_acq = rp_iva_acq + aRecLiquidazioneIva.iva_acquisti,
                   rp_iva_acq_non_detr = rp_iva_acq_non_detr + aRecLiquidazioneIva.iva_acq_non_detr,
                   rp_iva_acq_diff = rp_iva_acq_diff + aRecLiquidazioneIva.iva_acquisti_diff,
                   rp_iva_acq_diff_esig = rp_iva_acq_diff_esig + aRecLiquidazioneIva.iva_acq_diff_esig,
                   rp_iva_acq_diff_es_prec_esig = rp_iva_acq_diff_es_prec_esig + aRecLiquidazioneIva.iva_acq_diff_es_prec_esig,
                   rp_iva_acq_split_payment = rp_iva_acq_split_payment + aRecLiquidazioneIva.iva_acq_split_payment,
                   RP_IVA_LIQ_ESTERNA = nvl(RP_IVA_LIQ_ESTERNA,0) +nvl(aRecLiquidazioneIva.IVA_LIQ_ESTERNA ,0)
            WHERE  id_report = repID AND
                   chiave = gruppoStm AND
                   tipo = 'D' AND
                   sequenza = 2;

         END;

      END LOOP;

      CLOSE gen_cv;

   END;

   -------------------------------------------------------------------------------------------------
   -- Attivazione del calcolo del prorata

   BEGIN

      aPercentualeProrata:=NULL;

      IF (aGestioneProrata = 'Y' AND
          inGruppoReport = CNRCTB250.TI_LIQ_IVA_COMMERC) THEN

         aEsercizioPerProrata:=(aEsercizioReale - 1);

         BEGIN

            SELECT percentuale INTO aPercentualeProrata
            FROM   PRO_RATA
            WHERE  esercizio = aEsercizioPerProrata AND
                   cd_attivita_commerciale = '*';

         EXCEPTION

            WHEN no_data_found THEN
                 aPercentualeProrata:=NULL;

         END;

      END IF;

      UPDATE VP_LIQUIDAZIONE_IVA
      SET    rp_gestione_prorata = aGestioneProrata,
             rp_esercizio_euro = aEsercizioEuro,
             rp_perc_prorata_detraibile = aPercentualeProrata
      WHERE  id_report = repID AND
             chiave = gruppoStm AND
             tipo = 'D' AND
             sequenza = 2;

   END;

END calcolaLiquidazioneEnte;

-- =================================================================================================
-- Scrittura liquidazione. Sulla tabella LIQUIDAZIONE_IVA si memorizzano i soli valori calcolati dalla procedura
-- =================================================================================================
PROCEDURE scritturaLiquidazione
   (
    repID INTEGER,
    aCdCds VARCHAR2,
    aEsercizio NUMBER,
    aCdUo VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    gruppoStm CHAR,
    sottoGruppoStm CHAR,
    descrizioneGruppoStm VARCHAR2,
    inGruppoReport VARCHAR2
   ) IS
   aRecReportLiquidazioneIva VP_LIQUIDAZIONE_IVA%ROWTYPE;
   aRecLiquidazioneIva LIQUIDAZIONE_IVA%ROWTYPE;

   gen_cv GenericCurTyp;

   ivaAcquisti NUMBER(15,2);
   ivaAcquistiNoProrata NUMBER(15,2);
   ivaVendite NUMBER(15,2);
   ivaDebitoCredito NUMBER(15,2);
   ivaDaVersare NUMBER(15,2);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Lettura dati della liquidazione calcolata dalla procedura

   BEGIN

      OPEN gen_cv FOR

           SELECT *
           FROM   VP_LIQUIDAZIONE_IVA
           WHERE  id_report = repID AND
                  chiave = gruppoStm AND
                  tipo = 'D' AND
                  sequenza = 2;

      LOOP

         FETCH gen_cv INTO
               aRecReportLiquidazioneIva;

         EXIT WHEN gen_cv%NOTFOUND;
         -------------------------------------------------------------------------------------------
         -- Iva acquisti

         -- Controllo se deve essere attivata la gestione del pro-rata

         ivaAcquistiNoProrata:=(aRecReportLiquidazioneIva.rp_iva_acq -
                                aRecReportLiquidazioneIva.rp_iva_acq_non_detr -
                                aRecReportLiquidazioneIva.rp_iva_acq_diff +
                                aRecReportLiquidazioneIva.rp_iva_acq_diff_esig +
                                aRecReportLiquidazioneIva.rp_iva_acq_diff_es_prec_esig);

         IF (aRecReportLiquidazioneIva.rp_gestione_prorata = 'N' OR
             aRecReportLiquidazioneIva.rp_perc_prorata_detraibile IS NULL) THEN
            ivaAcquisti:=ivaAcquistiNoProrata;
         ELSE
            IF aRecReportLiquidazioneIva.rp_esercizio_euro = 'Y' THEN
               ivaAcquisti:=ROUND((ivaAcquistiNoProrata * aRecReportLiquidazioneIva.rp_perc_prorata_detraibile / 100),2);
            ELSE
               ivaAcquisti:=ROUND((ivaAcquistiNoProrata * aRecReportLiquidazioneIva.rp_perc_prorata_detraibile / 100));
            END IF;
         END IF;

         -------------------------------------------------------------------------------------------
         -- Iva vendite

         ivaVendite:=(aRecReportLiquidazioneIva.rp_iva_ven +
                      aRecReportLiquidazioneIva.rp_iva_ven_split_payment -
                      aRecReportLiquidazioneIva.rp_iva_ven_diff +
                      aRecReportLiquidazioneIva.rp_iva_ven_diff_esig +
                      aRecReportLiquidazioneIva.rp_iva_ven_diff_es_prec_esig +
                      aRecReportLiquidazioneIva.rp_iva_ven_autofatt +
                      aRecReportLiquidazioneIva.rp_iva_ven_intraue);


         -------------------------------------------------------------------------------------------
         -- Aggiornamento del record di liquidazione

         -- Rileggo la liquidazione per il recupero delle informazioni imputate manualmente

         BEGIN

            SELECT * INTO aRecLiquidazioneIva
            FROM   LIQUIDAZIONE_IVA A
            WHERE  A.cd_cds = aCdCds AND
                   A.cd_unita_organizzativa = aCdUo AND
                   A.esercizio = aEsercizio AND
                   A.dt_inizio = aDataInizio AND
                   A.dt_fine = aDataFine AND
                   A.tipo_liquidazione = inGruppoReport AND
                   A.report_id = repID;

         END;

         -- Calcolo attributi iva_deb_cred e iva_da_versare

         ivaDebitoCredito:=ivaAcquisti - ivaVendite + aRecReportLiquidazioneIva.rp_iva_deb_cred_per_prec +
                           NVL(aRecLiquidazioneIva.var_imp_per_prec,0) -
                           NVL(aRecLiquidazioneIva.iva_non_vers_per_prec,0) +
                           NVL(aRecLiquidazioneIva.iva_deb_cred_per_prec,0) +
                           NVL(aRecLiquidazioneIva.cred_iva_comp_detr,0)+
                           NVL(aRecReportLiquidazioneIva.rp_iva_liq_esterna,0);

         ivaDaVersare:=ivaDebitoCredito - NVL(aRecLiquidazioneIva.int_deb_liq_trim,0) +
                       NVL(aRecLiquidazioneIva.cred_iva_spec_detr,0) +
                       NVL(aRecLiquidazioneIva.acconto_iva_vers,0);
         BEGIN

            UPDATE LIQUIDAZIONE_IVA
            SET    iva_vendite = aRecReportLiquidazioneIva.rp_iva_ven,
                   iva_vendite_diff = aRecReportLiquidazioneIva.rp_iva_ven_diff,
                   iva_vend_diff_esig = aRecReportLiquidazioneIva.rp_iva_ven_diff_esig,
                   iva_vend_diff_es_prec_esig = aRecReportLiquidazioneIva.rp_iva_ven_diff_es_prec_esig,
                   iva_autofatt = aRecReportLiquidazioneIva.rp_iva_ven_autofatt,
                   iva_intraue = aRecReportLiquidazioneIva.rp_iva_ven_intraue,
                   iva_debito = ivaVendite,
                   iva_acquisti = aRecReportLiquidazioneIva.rp_iva_acq,
                   iva_acq_non_detr = aRecReportLiquidazioneIva.rp_iva_acq_non_detr,
                   iva_acquisti_diff = aRecReportLiquidazioneIva.rp_iva_acq_diff,
                   iva_acq_diff_esig = aRecReportLiquidazioneIva.rp_iva_acq_diff_esig,
                   iva_acq_diff_es_prec_esig = aRecReportLiquidazioneIva.rp_iva_acq_diff_es_prec_esig,
                   iva_credito_no_prorata = ivaAcquistiNoProrata,
                   perc_prorata_detraibile = aRecReportLiquidazioneIva.rp_perc_prorata_detraibile,
                   iva_credito = ivaAcquisti,
                   iva_non_vers_per_prec = aRecReportLiquidazioneIva.rp_iva_non_vers_per_prec,
                   iva_deb_cred_per_prec = aRecReportLiquidazioneIva.rp_iva_deb_cred_per_prec,
                   iva_deb_cred = ivaDebitoCredito,
                   iva_da_versare = ivaDaVersare,
                   iva_versata = 0,
                   iva_liq_esterna = aRecReportLiquidazioneIva.rp_iva_liq_esterna,
                   iva_acq_split_payment = aRecReportLiquidazioneIva.rp_iva_acq_split_payment,
                   iva_ven_split_payment = aRecReportLiquidazioneIva.rp_iva_ven_split_payment
            WHERE  cd_cds = aCdCds AND
                   esercizio = aEsercizio AND
                   cd_unita_organizzativa = aCdUo AND
                   tipo_liquidazione = inGruppoReport AND
                   dt_inizio = aDataInizio AND
                   dt_fine = aDataFine AND
                   report_id = repID;

         END;

      END LOOP;

      CLOSE gen_cv;

   END;

END scritturaLiquidazione;

-- =================================================================================================
-- Creazione record di liquidazione definitiva
-- =================================================================================================
PROCEDURE insRecLiquidazioneDef
   (
    repID INTEGER,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aDataInizio DATE,
    aDataFine DATE,
    utente VARCHAR2,
    inGruppoReport VARCHAR2
   ) IS

BEGIN

   -----------------------------------------------------------------------------------------------
   -- Scrittura del record di liquidazione definitivo

   INSERT INTO LIQUIDAZIONE_IVA
          (cd_cds,
           esercizio,
           cd_unita_organizzativa,
           dt_inizio,
           dt_fine,
           report_id,
           stato,
           annotazioni,
           dt_versamento,
           cod_azienda,
           cab,
           iva_vendite,
           iva_vendite_diff,
           iva_vend_diff_esig,
           iva_autofatt,
           iva_intraue,
           iva_debito,
           iva_acquisti,
           iva_acq_non_detr,
           iva_acquisti_diff,
           iva_acq_diff_esig,
           iva_credito,
           var_imp_per_prec,
           iva_non_vers_per_prec,
           iva_deb_cred_per_prec,
           cred_iva_comp_detr,
           iva_deb_cred,
           int_deb_liq_trim,
           cred_iva_spec_detr,
           acconto_iva_vers,
           iva_da_versare,
           iva_versata,
           cred_iva_infrann_rimb,
           cred_iva_infrann_comp,
           cd_tipo_documento,
           cd_cds_doc_amm,
           cd_uo_doc_amm,
           esercizio_doc_amm,
           pg_doc_amm,
           iva_credito_no_prorata,
           perc_prorata_detraibile,
           dacr,
           utuv,
           utcr,
           duva,
           pg_ver_rec,
           abi,
           tipo_liquidazione,
           iva_vend_diff_es_prec_esig,
           iva_acq_diff_es_prec_esig,
           iva_liq_esterna,
           iva_acq_split_payment,
           iva_ven_split_payment)
   SELECT  A.cd_cds,
           A.esercizio,
           A.cd_unita_organizzativa,
           A.dt_inizio,
           A.dt_fine,
           -9,
           'D',
           A.annotazioni,
           A.dt_versamento,
           A.cod_azienda,
           A.cab,
           NVL(A.iva_vendite,0),
           NVL(A.iva_vendite_diff,0),
           NVL(A.iva_vend_diff_esig,0),
           NVL(A.iva_autofatt,0),
           NVL(A.iva_intraue,0),
           NVL(A.iva_debito,0),
           NVL(A.iva_acquisti,0),
           NVL(A.iva_acq_non_detr,0),
           NVL(A.iva_acquisti_diff,0),
           NVL(A.iva_acq_diff_esig,0),
           NVL(A.iva_credito,0),
           NVL(A.var_imp_per_prec,0),
           NVL(A.iva_non_vers_per_prec,0),
           NVL(A.iva_deb_cred_per_prec,0),
           NVL(A.cred_iva_comp_detr,0),
           NVL(A.iva_deb_cred,0),
           NVL(A.int_deb_liq_trim,0),
           NVL(A.cred_iva_spec_detr,0),
           NVL(A.acconto_iva_vers,0),
           NVL(A.iva_da_versare,0),
           DECODE(SIGN(NVL(A.iva_da_versare,0)),-1,ABS(NVL(A.iva_da_versare,0)),0),
           NVL(A.cred_iva_infrann_rimb,0),
           NVL(A.cred_iva_infrann_comp,0),
           A.cd_tipo_documento,
           A.cd_cds_doc_amm,
           A.cd_uo_doc_amm,
           A.esercizio_doc_amm,
           A.pg_doc_amm,
           NVL(A.iva_credito_no_prorata,0),
           NVL(A.perc_prorata_detraibile,0),
           A.dacr,
           A.utuv,
           A.utcr,
           A.duva,
           1,
           A.abi,
           inGruppoReport,
           NVL(A.iva_vend_diff_es_prec_esig,0),
           NVL(A.iva_acq_diff_es_prec_esig,0),
           nvl(a.iva_liq_esterna,0),
           nvl(a.iva_acq_split_payment,0),
           nvl(a.iva_ven_split_payment,0)
   FROM    LIQUIDAZIONE_IVA A
   WHERE   A.cd_cds = aCdCdsOrigine AND
           A.esercizio = aEsercizio AND
           A.cd_unita_organizzativa = aCdUoOrigine AND
           A.dt_inizio = aDataInizio AND
           A.dt_fine = aDataFine AND
           A.tipo_liquidazione = inGruppoReport AND
           A.report_id = repID;

   DELETE FROM LIQUIDAZIONE_IVA
   WHERE  cd_cds = aCdCdsOrigine AND
          esercizio = aEsercizio AND
          cd_unita_organizzativa = aCdUoOrigine AND
          dt_inizio = aDataInizio AND
          dt_fine = aDataFine AND
          tipo_liquidazione = inGruppoReport AND
          report_id = repID;

   UPDATE LIQUIDAZIONE_IVA
   SET    report_id = 0
   WHERE  cd_cds = aCdCdsOrigine AND
          esercizio = aEsercizio AND
          cd_unita_organizzativa = aCdUoOrigine AND
          dt_inizio = aDataInizio AND
          dt_fine = aDataFine AND
          tipo_liquidazione = inGruppoReport AND
          report_id = -9;

EXCEPTION

   WHEN DUP_VAL_ON_INDEX THEN
        IBMERR001.RAISE_ERR_GENERICO ('Chiave duplicata in inserimento liquidazione IVA definitiva');

END insRecLiquidazioneDef;

-- =================================================================================================
-- Inserimento nella tabella REPORT DETTAGLIO del dettaglio fatture con esigibilità differita che
-- sono state oggetto di liquidazione definitiva
-- =================================================================================================
PROCEDURE insDettaglioPerLiquidazione
   (
    repID INTEGER,
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    utente VARCHAR2,
    inGruppoReport VARCHAR2
   ) IS
   descTipoReport VARCHAR2(100);
   gen_cv GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Lettura fatture e scrittura in REPORT DETTAGLIO dell'elenco fatture ad esigibilità differita
   -- divenute esigibili ed elaborate in una liquidazione definitiva.
   -- Si prelevano tutti i record con data esigibilità IVA inferiore alla data di fine, la vista
   -- V_LIQUID_IVA_VENDITE_ESIG garantisce il processo dei soli record mai inclusi in precedenti liquidazioni

   IF aTipoRegistro = 'A' THEN
      descTipoReport:=CNRCTB255.TI_LIQUIDAZIONE_ACQ_ESIGIB;
   ELSE
      descTipoReport:=CNRCTB255.TI_LIQUIDAZIONE_VEN_ESIGIB;
   END IF;

   -- La gestione è attualmente attiva solo per registri IVA vendite

   IF aTipoRegistro = 'V' THEN

      OPEN gen_cv FOR

           SELECT DISTINCT cd_cds,
                  cd_unita_organizzativa,
                  esercizio,
                  numero_progressivo,
                  cd_cds_origine,
                  cd_uo_origine,
                  cd_tipo_sezionale,
                  ti_fattura,
                  pg_riga_documento
           FROM   V_LIQUID_IVA_VENDITE_ESIG
           WHERE  data_esigibilita IS NOT NULL AND
--                  data_esigibilita >= aDataInizio AND
                  data_esigibilita <= aDataFine AND
                  cd_cds_origine = aCdCdsOrigine AND
                  cd_uo_origine = aCdUoOrigine AND
                  cd_tipo_sezionale = aCdTipoSezionale
           ORDER BY 1,2,3;

      LOOP

         FETCH gen_cv INTO
               cv_cd_cds,
               cv_cd_uo,
               cv_esercizio,
               cv_numero_progressivo,
               cv_cd_cds_origine,
               cv_cd_uo_origine,
               cv_cd_tipo_sezionale,
               cv_ti_fattura,
               CV_riga_documento;

         EXIT WHEN gen_cv%NOTFOUND;

         BEGIN

            INSERT INTO REPORT_DETTAGLIO
                   (cd_cds,
                    cd_unita_organizzativa,
                    esercizio,
                    tipo_report,
                    data_inizio,
                    data_fine,
                    cd_tipo_sezionale,
                    ti_documento,
                    pg_documento,
                    cd_cds_altro,
                    cd_uo_altro,
                    dacr,
                    duva,
                    utuv,
                    utcr,
                    pg_ver_rec,
                    pg_riga_documento)
            VALUES (cv_cd_cds_origine,
                    cv_cd_uo_origine,
                    cv_esercizio,
                    descTipoReport,
                    aDataInizio,
                    aDataFine,
                    cv_cd_tipo_sezionale,
                    CNRCTB100.TI_FATTURA_ATTIVA,
                    cv_numero_progressivo,
                    cv_cd_cds,
                    cv_cd_uo,
                    sysdate,
                    sysdate,
                    utente,
                    utente,
                    1,
                    CV_riga_documento);

         EXCEPTION

            WHEN DUP_VAL_ON_INDEX THEN
                 IBMERR001.RAISE_ERR_GENERICO
                    ('Chiave duplicata in inserimento estremi fattura ad esigibilità differita in REPORT_DETTAGLIO '  ||
                     'FATTURA ' || cv_cd_cds || '/' || cv_cd_uo || '-' || cv_esercizio || '/' || cv_numero_progressivo);
         END;

      END LOOP;

      CLOSE gen_cv;
   Else
     OPEN gen_cv FOR

           SELECT DISTINCT cd_cds,
                  cd_unita_organizzativa,
                  esercizio,
                  numero_progressivo,
                  cd_cds_origine,
                  cd_uo_origine,
                  cd_tipo_sezionale,
                  ti_fattura,
                  pg_riga_documento
           FROM   V_LIQUID_IVA_ACQUISTI_ESIG
           WHERE  data_esigibilita IS NOT NULL AND
--                  data_esigibilita >= aDataInizio AND
                  data_esigibilita <= aDataFine AND
                  cd_cds_origine = aCdCdsOrigine AND
                  cd_uo_origine = aCdUoOrigine AND
                  cd_tipo_sezionale = aCdTipoSezionale
           ORDER BY 1,2,3;

      LOOP

         FETCH gen_cv INTO
               cv_cd_cds,
               cv_cd_uo,
               cv_esercizio,
               cv_numero_progressivo,
               cv_cd_cds_origine,
               cv_cd_uo_origine,
               cv_cd_tipo_sezionale,
               cv_ti_fattura,
               cv_riga_documento;

         EXIT WHEN gen_cv%NOTFOUND;

         BEGIN

            INSERT INTO REPORT_DETTAGLIO
                   (cd_cds,
                    cd_unita_organizzativa,
                    esercizio,
                    tipo_report,
                    data_inizio,
                    data_fine,
                    cd_tipo_sezionale,
                    ti_documento,
                    pg_documento,
                    cd_cds_altro,
                    cd_uo_altro,
                    dacr,
                    duva,
                    utuv,
                    utcr,
                    pg_ver_rec,
                    pg_riga_documento)
            VALUES (cv_cd_cds_origine,
                    cv_cd_uo_origine,
                    cv_esercizio,
                    descTipoReport,
                    aDataInizio,
                    aDataFine,
                    cv_cd_tipo_sezionale,
                    CNRCTB100.TI_FATTURA_PASSIVA,
                    cv_numero_progressivo,
                    cv_cd_cds,
                    cv_cd_uo,
                    sysdate,
                    sysdate,
                    utente,
                    utente,
                    1,
                    cv_riga_documento);

         EXCEPTION

            WHEN DUP_VAL_ON_INDEX THEN
                 IBMERR001.RAISE_ERR_GENERICO
                    ('Chiave duplicata in inserimento estremi fattura ad esigibilità differita in REPORT_DETTAGLIO '  ||
                     'FATTURA ' || cv_cd_cds || '/' || cv_cd_uo || '-' || cv_esercizio || '/' || cv_numero_progressivo);
         END;

      END LOOP;
   END IF;

END insDettaglioPerLiquidazione;

-- =================================================================================================
-- Ritorna l'esistenza o meno di una liquidazione definitiva
-- =================================================================================================
FUNCTION chkEsisteLiquidazioneDef
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER,
    aDataInizio DATE,
    aDataFine DATE,
    aGruppoReport VARCHAR2
   ) RETURN VARCHAR2 IS
   flEsisteLiq NUMBER;

BEGIN

   SELECT COUNT(*) INTO flEsisteLiq
   FROM   DUAL
   WHERE  EXISTS
             (SELECT 1
              FROM   LIQUIDAZIONE_IVA A
              WHERE  A.cd_cds = aCdCds AND
                     A.cd_unita_organizzativa = aCdUo AND
                     A.esercizio = aEsercizio AND
                     A.dt_inizio = aDataInizio AND
                     A.dt_fine = aDataFine AND
                     A.tipo_liquidazione = aGruppoReport AND
                     A.report_id = 0);

   IF flEsisteLiq = 0 THEN
      RETURN 'N';
   ELSE
      RETURN 'Y';
   END IF;

END chkEsisteLiquidazioneDef;

-- ==========================================================================================================
-- Assegnazione della data di esigibilita IVA per fatture passive non divenute esigibili nell'arco di 1 Anno
-- in sede di stampa registri IVA
-- ==========================================================================================================
PROCEDURE InsDataDiffFatturePassAuto
   (
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE ) IS
   gen_cv GenericCurTyp;
BEGIN
    Open gen_cv FOR
           SELECT A.cd_cds, A.cd_unita_organizzativa, A.esercizio, A.pg_fattura_passiva
                  From   FATTURA_PASSIVA A
                  Where
                           A.cd_cds_origine = aCdCdsOrigine  AND
                           A.cd_uo_origine =  aCdUoOrigine  AND
                           A.esercizio_fattura_fornitore = aEsercizio-1   And
                           A.cd_tipo_sezionale =  aCdTipoSezionale And
                           To_Char(A.dt_fattura_fornitore,'mmyyyy')<= To_Char(aDataInizio,'mm')||To_Char(aDataInizio,'yyyy')-1 And
                           A.stato_cofi != 'A' AND
                           A.fl_liquidazione_differita='Y' And
                           A.ti_istituz_commerc ='C';

      LOOP

         FETCH gen_cv INTO
               cv_cd_cds,
               cv_cd_uo,
               cv_esercizio,
               cv_numero_progressivo;

         EXIT WHEN gen_cv%NOTFOUND;

               UPDATE FATTURA_PASSIVA_riga
               SET    data_esigibilita_iva = aDataFine
               WHERE  cd_cds = cv_cd_cds AND
                      cd_unita_organizzativa = cv_cd_uo AND
                      esercizio = cv_esercizio AND
                      pg_fattura_passiva = cv_numero_progressivo And
                      data_esigibilita_iva Is Null;

      END LOOP;

      CLOSE gen_cv;
End InsDataDiffFatturePassAuto;

END Cnrctb260; -- PACKAGE END;
