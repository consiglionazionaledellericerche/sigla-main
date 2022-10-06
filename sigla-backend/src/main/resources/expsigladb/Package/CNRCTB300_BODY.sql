--------------------------------------------------------
--  DDL for Package Body CNRCTB300
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB300" AS

-- =================================================================================================
-- Main procedura
-- =================================================================================================
PROCEDURE aggDaSospesoRiscontro
   (
    inCDSManrev VARCHAR2,
    inEsercizioManrev NUMBER,
    inPgManrev NUMBER,
    inTipoManrev VARCHAR2,
    inUtente VARCHAR2
   ) IS

BEGIN

   NULL;

END aggDaSospesoRiscontro;

-- =================================================================================================
-- Main procedura
-- =================================================================================================
PROCEDURE leggiMandatoReversale
   (
    inCDSManrev VARCHAR2,
    inEsercizioManrev NUMBER,
    inPgManrev NUMBER,
    inTipoManrev VARCHAR2,
    inAzione VARCHAR2,
    inUtente VARCHAR2
   ) IS
   aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE;
   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   aUtente:=inUtente;
   dataOdierna:=sysdate;

   -- CONTROLLI BLOCCANTI INIZIALI

   checkIniziale(
    inCDSManrev,
    inEsercizioManrev,
    inPgManrev,
    inTipoManrev,
    inAzione,
    inUtente
   );

   -------------------------------------------------------------------------------------------------
   -- Lettura dei record di Mandato o Reversale in input con dettaglio riga

   BEGIN

      IF inTipoManrev = 'MAN' THEN

         OPEN gen_cur FOR

              SELECT *
              FROM   V_MANDATO_UPG_STATO_DOCAMM
              WHERE  cd_cds_mandato = inCDSManrev AND
                     esercizio_mandato = inEsercizioManrev AND
                     pg_mandato = inPgManrev
              ORDER BY cd_cds_docamm,
                       cd_uo_docamm,
                       esercizio_docamm,
                       cd_tipo_docamm,
                       pg_docamm;

      ELSE

         OPEN gen_cur FOR

              SELECT *
              FROM   V_REVERSALE_UPG_STATO_DOCAMM
              WHERE  cd_cds_reversale = inCDSManrev AND
                     esercizio_reversale = inEsercizioManrev AND
                     pg_reversale = inPgManrev
              ORDER BY cd_cds_docamm,
                       cd_uo_docamm,
                       esercizio_docamm,
                       cd_tipo_docamm,
                       pg_docamm;

      END IF;

      LOOP

         FETCH gen_cur INTO aRecManrev;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Aggiornamento dei documenti amministrativi associati al mandato reversale a seguito del
         -- suo inserimento o annullamento

         aggiornaStatoDocamm(aRecManrev,
                             inTipoManrev,
                             inAzione);

         -- Gestione effetti collaterali di inserimento/annullamento riga di mandato o reversale
         effCollRigaManRev(aRecManrev,
                       inTipoManrev,
                       inAzione);

      END LOOP;

      CLOSE gen_cur;

   END;

   effCollFinale(
    inCDSManrev,
    inEsercizioManrev,
    inPgManrev,
    inTipoManrev,
    inAzione,
    inUtente
   );

END leggiMandatoReversale;

-- =================================================================================================
-- Aggiornamento dei documenti amministrativi associati al mandato reversale a seguito del suo
-- inserimento o annullamento
-- =================================================================================================
PROCEDURE aggiornaStatoDocamm
   (
    aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE,
    inTipoManrev VARCHAR2,
    inAzione VARCHAR2
   ) IS
   aStatement VARCHAR2(10000);
   aUpdateClause VARCHAR2(2000);

   cv_cd_cds DOCUMENTO_GENERICO_RIGA.cd_cds%TYPE;
   cv_cd_uo DOCUMENTO_GENERICO_RIGA.cd_unita_organizzativa%TYPE;
   cv_esercizio DOCUMENTO_GENERICO_RIGA.esercizio%TYPE;
   cv_tipo_docamm DOCUMENTO_GENERICO_RIGA.cd_tipo_documento_amm%TYPE;
   cv_pg_docamm DOCUMENTO_GENERICO_RIGA.pg_documento_generico%TYPE;
   cv_pg_riga DOCUMENTO_GENERICO_RIGA.progressivo_riga%TYPE;
   cv_pg_ver_rec_det DOCUMENTO_GENERICO_RIGA.pg_ver_rec%TYPE;
   cv_pg_ver_rec_tes DOCUMENTO_GENERICO_RIGA.pg_ver_rec%TYPE;
   cv_stato_cofi_det DOCUMENTO_GENERICO_RIGA.stato_cofi%TYPE;
   cv_stato_cofi_tes DOCUMENTO_GENERICO_RIGA.stato_cofi%TYPE;
   cv_terzo DOCUMENTO_GENERICO_RIGA.cd_terzo%TYPE;
   comp Compenso%Rowtype;
   gen_cur_a GenericCurTyp;
   liquidato NUMBER:=0;
   aFlGestioneIvaDifferita CHAR(1);
   conta NUMBER :=0;
BEGIN

   -------------------------------------------------------------------------------------------------
   -- Azzeramento variabili controllo rottura di documento

   mem_cd_cds:=NULL;
   mem_cd_uo:=NULL;
   mem_esercizio:=NULL;
   mem_tipo_docamm:=NULL;
   mem_pg_docamm:=NULL;
   mem_stato_cofi_tes:=NULL;

   ----------------------------------------------------------------------------------------------
   -- Composizione della query di selezione delle righe dei documenti amministrativi associati
   -- alla riga di mandato/reversale in lettura

   aStatement:=componiLeggiDocamm (aRecManrev,
                                   inTipoManrev);

   BEGIN

      ----------------------------------------------------------------------------------------------
      -- Lettura dei dati del documento amministrativo per aggiornamento degli attributi associati
      -- all'inserimento o annullamento di un mandato o reversale

      OPEN gen_cur_a FOR
           aStatement;

      LOOP

         FETCH gen_cur_a INTO
               cv_cd_cds,
               cv_cd_uo,
               cv_esercizio,
               cv_tipo_docamm,
               cv_pg_docamm,
               cv_stato_cofi_tes,
               cv_pg_riga,
               cv_stato_cofi_det;

         EXIT WHEN gen_cur_a%NOTFOUND;

         BEGIN
            ----------------------------------------------------------------------------------------
            -- Controllo rottura per documento amministrativo per aggiornamento testata dello stesso

            -- E' la prima volta che entro nel ciclo

            IF mem_cd_cds IS NULL THEN
               mem_cd_cds:=cv_cd_cds;
               mem_cd_uo:=cv_cd_uo;
               mem_esercizio:=cv_esercizio;
               mem_tipo_docamm:=cv_tipo_docamm;
               mem_pg_docamm:=cv_pg_docamm;
               mem_stato_cofi_tes:=cv_stato_cofi_tes;
            END IF;

            -- Se vi ? differenza aggiorno testata documento amministrativo

            IF (mem_cd_cds != cv_cd_cds OR
                mem_cd_uo != cv_cd_uo OR
                mem_esercizio != cv_esercizio OR
                mem_tipo_docamm != cv_tipo_docamm OR
                mem_pg_docamm != cv_pg_docamm) THEN
               aggiornaStatoTestataDocamm(aRecManrev,inTipoManrev,inAzione);
               mem_cd_cds:=cv_cd_cds;
               mem_cd_uo:=cv_cd_uo;
               mem_esercizio:=cv_esercizio;
               mem_tipo_docamm:=cv_tipo_docamm;
               mem_pg_docamm:=cv_pg_docamm;
               mem_stato_cofi_tes:=cv_stato_cofi_tes;
            END IF;

            ----------------------------------------------------------------------------------------
            -- Aggiornamento righe del documento amministrativo solo per i documenti che hanno RIGHE

            IF CNRCTB100.GETTABELLADETT(cv_tipo_docamm) IS NOT NULL THEN
               IF inAzione = 'I' THEN
                  aUpdateClause:='stato_cofi = ''P'',  ti_associato_manrev = ''T''';
               ELSE
                  IF (aRecManrev.ti_mandato = 'A' OR
                      mem_tipo_docamm = CNRCTB100.TI_GEN_REINTEGRO_FONDO OR
                      mem_tipo_docamm = CNRCTB100.TI_GEN_CORI_ACC_ENTRATA OR
                      mem_tipo_docamm = CNRCTB100.TI_GEN_CORI_ACC_SPESA OR
                      mem_tipo_docamm = CNRCTB100.TI_GENERICO_TRASF_S OR
                      mem_tipo_docamm = CNRCTB100.TI_GEN_CORI_VER_SPESA OR
                      mem_tipo_docamm = CNRCTB100.TI_GEN_CORI_VER_ENTRATA OR
                      mem_tipo_docamm = CNRCTB100.TI_GEN_IVA_ENTRATA OR
                      (aRecManrev.ti_mandato = 'R' AND
                       mem_tipo_docamm = CNRCTB100.TI_GENERICO_REGOLA_E)
                     ) THEN
                     aUpdateClause:='stato_cofi = ''A'', dt_cancellazione = ' || IBMUTL001.ASDYNDATE(dataOdierna);
                  ELSIF inAzione = 'A' THEN
                    aUpdateClause:='stato_cofi = ''C'',  ti_associato_manrev = ''N''';
                  ELSE
                     IF cv_stato_cofi_det = 'P' THEN
                        aUpdateClause:='stato_cofi = ''C''';
                     ELSE
                        aUpdateClause:='stato_cofi = ''C''';
                     END IF;
                  END IF;
               END IF;
                   If mem_tipo_docamm = CNRCTB100.TI_FATTURA_PASSIVA And inAzione = 'A' THEN
                        aFlGestioneIvaDifferita:=CNRCTB120.getFtDiffConIvaPassiva(mem_cd_cds,
                                                                     mem_cd_uo,
                                                                     mem_esercizio,
                                                                     mem_pg_docamm,
                                                                     cv_pg_riga,
                                                                     'Y');

                        If aFlGestioneIvaDifferita='Y' THEN
                                aUpdateClause:=aUpdateClause || ', data_esigibilita_iva = NULL';
                        End If;


                   End If;
                   If mem_tipo_docamm = CNRCTB100.TI_FATTURA_ATTIVA And inAzione = 'I' THEN
                        aFlGestioneIvaDifferita:=CNRCTB120.getFtAttivaDiffConIva(mem_cd_cds,
                                                                     mem_cd_uo,
                                                                     mem_esercizio,
                                                                     mem_pg_docamm,
                                                                     cv_pg_riga,
                                                                     'Y');
                        If aFlGestioneIvaDifferita='Y' Then
                                 Select Count(0) Into conta
                                 From fattura_attiva_riga
                                 WHERE
                                 cd_cds = mem_cd_cds AND
                                 cd_unita_organizzativa = mem_cd_uo AND
                                 esercizio = mem_esercizio AND
                                 pg_fattura_attiva = mem_pg_docamm And
                                 progressivo_riga = cv_pg_riga And
                                 data_esigibilita_iva is Null;
                                 If(conta !=0) Then
                                        aUpdateClause:=aUpdateClause || ', data_esigibilita_iva = '|| IBMUTL001.ASDYNDATE(aRecManrev.dt_emissione_mandato);
                                 End If;
                        End If;

                   End If;
                   If mem_tipo_docamm = CNRCTB100.TI_FATTURA_ATTIVA And inAzione = 'A' THEN
                        aFlGestioneIvaDifferita:=CNRCTB120.getFtAttivaDiffConIva(mem_cd_cds,
                                                                     mem_cd_uo,
                                                                     mem_esercizio,
                                                                     mem_pg_docamm,
                                                                     cv_pg_riga,
                                                                     'Y');
                          SELECT Count(0) Into liquidato
                           FROM  REPORT_DETTAGLIO D
                        WHERE
         d.esercizio    = mem_esercizio and
         d.cd_cds_altro = mem_cd_cds and
         d.cd_uo_altro  = mem_cd_uo and
         d.pg_documento = mem_pg_docamm and
         d.pg_riga_documento =cv_pg_riga And
                                 D.tipo_report = 'LIQUIDAZIONE_VENDITE' ;

                        If aFlGestioneIvaDifferita='Y' And liquidato =0 Then
                                aUpdateClause:=aUpdateClause || ', data_esigibilita_iva = NULL';
                        End If;

                   End If;
                   CNRCTB100.UPDATEDOCAMMRIGA(cv_tipo_docamm,
                                  cv_cd_cds,
                                  cv_esercizio,
                                  cv_cd_uo,
                                  cv_pg_docamm,
                                  cv_pg_riga,
                                  aUpdateClause,
                                  Null,
                                  aUtente,
                                  dataOdierna);
          End If;

           If (mem_tipo_docamm = CNRCTB100.TI_COMPENSO  And  inAzione = 'A') Then

                Select * Into comp
                From compenso
                   Where
                   esercizio = mem_esercizio And
                   cd_cds    = mem_cd_cds And
                   cd_unita_organizzativa = mem_cd_uo And
                   pg_compenso = mem_pg_docamm;
                If comp.fl_liquidazione_differita = 'Y' Then
                        Update fattura_passiva_riga Set data_esigibilita_iva = Null,utuv=aUtente,
                                  duva=dataOdierna
                           Where
                               (esercizio,cd_cds,cd_unita_organizzativa,pg_fattura_passiva)In
                                (Select a.esercizio,a.cd_cds,a.cd_unita_organizzativa,a.pg_fattura_passiva
                                From fattura_passiva a
                                Where
                                a.esercizio = mem_esercizio And
                                a.cd_cds    = mem_cd_cds And
                                a.cd_unita_organizzativa = mem_cd_uo And
                                a.ESERCIZIO_FATTURA_FORNITORE=Comp.ESERCIZIO_FATTURA_FORNITORE And
                                a.DT_FATTURA_FORNITORE=Comp.DT_FATTURA_FORNITORE And
                                a.NR_FATTURA_FORNITORE=Comp.NR_FATTURA_FORNITORE And
                                a.DT_REGISTRAZIONE=Comp.DT_REGISTRAZIONE And
                          a.fl_liquidazione_differita ='Y');
                End If;
             End IF;

         END;

      END LOOP;

      CLOSE gen_cur_a;

      -- Aggiornamento testata dell'ultimo documento amministrativo elaborato

      aggiornaStatoTestataDocamm(aRecManrev,inTipoManrev,inAzione);

   END;

END aggiornaStatoDocamm;

-- =================================================================================================
-- Composizione della query di selezione delle righe dei documenti amministrativi associati alla
-- riga di mandato/reversale in lettura
-- =================================================================================================
FUNCTION componiLeggiDocamm
   (
    aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE,
    inTipoManrev VARCHAR2
   ) RETURN VARCHAR2 IS
   aStatement VARCHAR2(10000);
   aTestata VARCHAR2(40);
   aRiga VARCHAR2(40);
   dettString VARCHAR2(2000);
   dettStringTerzo VARCHAR2(1000);
   aTabPrimiDoc VARCHAR2(40);
   aJoinForKey VARCHAR2(2000);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Composizione dello statement di SELECT parte variabile in dipendenza del tipo di documento
   -- amministrativo in elaborazione

   aTestata:=CNRCTB100.GETTABELLA(aRecManrev.cd_tipo_docamm);
   aRiga:=CNRCTB100.GETTABELLADETT(aRecManrev.cd_tipo_docamm);
   dettString:=NULL;
   dettStringTerzo:=NULL;

   -- Documenti amministrativi con sola testata ----------------------------------------------------

   IF aRiga IS NULL THEN
      dettString:='NULL, NULL';
      aTabPrimiDoc:=aTestata;
      aJoinForKey:=CNRCTB100.getTstaFromCondForKey(aRecManrev.cd_tipo_docamm,
                                                   aRecManrev.cd_cds_docamm,
                                                   aRecManrev.esercizio_docamm,
                                                   aRecManrev.cd_uo_docamm,
                                                   aRecManrev.pg_docamm);

   -- Documenti amministrativi con testata e dettagli ----------------------------------------------

   ELSE

      dettString:=aRiga || '.' || CNRCTB100.GETNOMEPGDETT(aRecManrev.cd_tipo_docamm) || ',' ||
                  aRiga || '.stato_cofi';
      aTabPrimiDoc:=aRiga;
      aJoinForKey:=CNRCTB100.getTstaRigaFromCondForKey(aRecManrev.cd_tipo_docamm,
                                                       aRecManrev.cd_cds_docamm,
                                                       aRecManrev.esercizio_docamm,
                                                       aRecManrev.cd_uo_docamm,
                                                       aRecManrev.pg_docamm);

      -- Se si tratta di documento generico si aggiunge la condizione che il terzo della riga
      -- deve corrispondere a quello del mandato o reversale tranne il caso in cui l'anagrafico del
      -- documento sia un diversi, che lo stesso sia gestito tramite fondo economale o che la
      -- riga di generico sia stata gestita con cessione di credito

      IF CNRCTB100.isInTabellaGenerico(aRecManrev.cd_tipo_docamm) = 'Y' THEN
         dettStringTerzo:='(' ||
                              '(' ||
                                  aRiga || '.cd_terzo = ' || '''' || aRecManrev.cd_terzo_mandato || '''' ||
                              ') OR ' ||
                              '(' ||
                                  'EXISTS ' ||
                                    '( SELECT 1 ' ||
                                      'FROM   TERZO B, ANAGRAFICO C ' ||
                          'WHERE  B.cd_terzo = ' || aRiga || '.cd_terzo ' || ' AND ' ||
                                             'C.cd_anag = B.cd_anag AND ' ||
                                             'C.ti_entita = ' || ' ''D'' ' ||
                                    ') ' ||
                              ') OR '  ||
                              '(' ||
                                  aRiga || '.cd_tipo_documento_amm = ' ||
                                  '''' || CNRCTB100.TI_GEN_REINTEGRO_FONDO || '''' ||
                              ') OR '  ||
                              '(' ||
                                  aRiga || '.cd_tipo_documento_amm = ' ||
                                  '''' || CNRCTB100.TI_GEN_CORI_ACC_ENTRATA || '''' ||
                              ') OR '  ||
                              '(' ||
                                  aRiga || '.cd_tipo_documento_amm = ' ||
                                  '''' || CNRCTB100.TI_GEN_CORI_ACC_SPESA || '''' ||
                              ') OR ' ||
                              '(' ||
                                  aTestata || '.stato_pagamento_fondo_eco IN ( ' || ' ''A'',''R'' )' ||
                              ') OR ' ||
                              '(' ||
                                  aRiga || '.cd_terzo_cessionario IS NOT NULL AND ' ||
                                  aRiga || '.cd_terzo_cessionario = ' ||
                                  '''' || aRecManrev.cd_terzo_mandato || '''' ||
                              ')' ||
                           ')';
      END IF;

   END IF;

   aStatement:= 'SELECT ' ||
                aTestata || '.cd_cds,' ||
                aTestata || '.cd_unita_organizzativa,' ||
                aTestata || '.esercizio, ' ||
                '''' || aRecManrev.cd_tipo_docamm || ''',' ||
                aTestata || '.' || CNRCTB100.GETNOMEPG(aRecManrev.cd_tipo_docamm) || ',' ||
                aTestata || '.stato_cofi,' ||
                dettString ||
                aJoinForkey;


   -- Differenzio per gestione mandati o reversali

   IF inTipoManrev = 'MAN' Then
      aStatement:=aStatement || ' AND ((' ||
                  aTabPrimiDoc||'.cd_cds_obbligazione = ' || '''' || aRecManrev.cd_cds_mandato || '''' || ' AND ' ||
                  aTabPrimiDoc||'.esercizio_obbligazione = ' || aRecManrev.esercizio_mandato || ' AND ' ||
                  aTabPrimiDoc||'.esercizio_ori_obbligazione = ' || aRecManrev.esercizio_ori_obbligazione || ' AND ' ||
                  aTabPrimiDoc||'.pg_obbligazione = ' || aRecManrev.pg_obbligazione || ' AND ' ||
                  aTabPrimiDoc||'.pg_obbligazione_scadenzario = ' || aRecManrev.pg_obbligazione_scadenzario;

      If aRecManrev.cd_tipo_docamm = 'FATTURA_P' Then
   aStatement:=aStatement || ' AND '||
               'Nvl('||aTestata||'.fl_da_ordini,''N'') = ''N''  OR  Nvl('||aTestata||'.fl_da_ordini,''N'') = ''Y''  AND '||
         aTabPrimiDoc||'.PROGRESSIVO_RIGA in (Select FATTURA_ORDINE.PROGRESSIVO_RIGA '||
                                             'From ORDINE_ACQ_CONSEGNA, FATTURA_ORDINE '||
                                             'Where ORDINE_ACQ_CONSEGNA.cd_cds = FATTURA_ORDINE.cd_cds_ordine '||
                                             ' And   ORDINE_ACQ_CONSEGNA.cd_unita_operativa = FATTURA_ORDINE.cd_unita_operativa '||
                                             ' And   ORDINE_ACQ_CONSEGNA.esercizio = FATTURA_ORDINE.esercizio_ordine '||
                                             ' And   ORDINE_ACQ_CONSEGNA.cd_numeratore = FATTURA_ORDINE.cd_numeratore '||
                                             ' And   ORDINE_ACQ_CONSEGNA.numero = FATTURA_ORDINE.numero '||
                                             ' And   ORDINE_ACQ_CONSEGNA.riga = FATTURA_ORDINE.riga '||
                                             ' And   ORDINE_ACQ_CONSEGNA.consegna = FATTURA_ORDINE.consegna '||
                                             ' And   ORDINE_ACQ_CONSEGNA.cd_cds_obbl = ' || '''' || aRecManrev.cd_cds_mandato || '''' ||
                                             ' And   ORDINE_ACQ_CONSEGNA.esercizio_obbl = ' || aRecManrev.esercizio_mandato ||
                                             ' And   ORDINE_ACQ_CONSEGNA.esercizio_orig_obbl = ' || aRecManrev.esercizio_ori_obbligazione ||
                                             ' And   ORDINE_ACQ_CONSEGNA.pg_obbligazione = ' || aRecManrev.pg_obbligazione ||
                                             ' And   ORDINE_ACQ_CONSEGNA.pg_obbligazione_scad = '|| aRecManrev.pg_obbligazione_scadenzario ||
                                             ' And   FATTURA_ORDINE.CD_CDS = '||aTabPrimiDoc||'.CD_CDS '||
                                             ' And   FATTURA_ORDINE.CD_UNITA_ORGANIZZATIVA = '||aTabPrimiDoc||'.CD_UNITA_ORGANIZZATIVA '||
                                             ' And   FATTURA_ORDINE.ESERCIZIO = '||aTabPrimiDoc||'.ESERCIZIO '||
                                             ' And   FATTURA_ORDINE.PG_FATTURA_PASSIVA = '||aTabPrimiDoc||'.PG_FATTURA_PASSIVA))) ';
      Else
        aStatement:=aStatement || '))';
      End If;
   ELSE
      aStatement:=aStatement || ' AND ' ||
                  aTabPrimiDoc||'.cd_cds_accertamento = ' || '''' || aRecManrev.cd_cds_mandato || '''' || ' AND ' ||
                  aTabPrimiDoc||'.esercizio_accertamento = ' || aRecManrev.esercizio_mandato || ' AND ' ||
                  aTabPrimiDoc||'.esercizio_ori_accertamento = ' || aRecManrev.esercizio_ori_obbligazione || ' AND ' ||
                  aTabPrimiDoc||'.pg_accertamento = ' || aRecManrev.pg_obbligazione || ' AND ' ||
                  aTabPrimiDoc||'.pg_accertamento_scadenzario = ' || aRecManrev.pg_obbligazione_scadenzario;
   END IF;

   -- Aggiungo la clausola di filtro sul terzo

   IF dettStringTerzo IS NOT NULL THEN
      aStatement:=aStatement || ' AND ' || dettStringTerzo;
   END IF;

   -- Inserisco LOCK

   aStatement:=aStatement || ' FOR UPDATE NOWAIT';

   RETURN aStatement;

END componiLeggiDocamm;

-- =================================================================================================
-- Composizione della query di selezione dei diversi valori assunti da stato_cofi nelle righe dei
-- documenti amministrativi per l'aggiornamento relativo della testata
-- =================================================================================================
PROCEDURE aggiornaStatoTestataDocamm
   (
    aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE,
    inTipoManrev VARCHAR2,
    inAzione VARCHAR2
   ) IS
   aStatement VARCHAR2(10000);
   aStatementRighe VARCHAR2(10000);
   aBaseStatement VARCHAR2(2000);
   aSetClause VARCHAR2(2000);
   aTestata VARCHAR2(40);
   aRiga VARCHAR2(40);

   cv_stato_cofi_in DOCUMENTO_GENERICO_RIGA.stato_cofi%TYPE;
   cv_stato_cofi_out DOCUMENTO_GENERICO_RIGA.stato_cofi%TYPE;
   cv_ti_associato_manrev_in FATTURA_PASSIVA_RIGA.ti_associato_manrev%TYPE;
   cv_ti_associato_manrev_out FATTURA_PASSIVA_RIGA.ti_associato_manrev%TYPE;
   cv_cd_cds_obbacr OBBLIGAZIONE.cd_cds%TYPE;
   cv_esercizio_obbacr OBBLIGAZIONE.esercizio%TYPE;
   cv_esercizio_ori_obbacr OBBLIGAZIONE.esercizio_originale%TYPE;
   cv_pg_obbacr OBBLIGAZIONE.pg_obbligazione%TYPE;
   cv_pg_scad_obbacr OBBLIGAZIONE_SCADENZARIO.pg_obbligazione_scadenzario%TYPE;
   cv_importo DOCUMENTO_GENERICO_RIGA.im_riga%TYPE;

   isContabilizzato BOOLEAN;
   isPagato BOOLEAN;
   isCancellato BOOLEAN;
   isAssociatoManrev BOOLEAN;
   isNotAssociatoManrev BOOLEAN;
   eseguiLock CHAR(1);

   gen_cur_b GenericCurTyp;


BEGIN

   isContabilizzato:=FALSE;
   isPagato:=FALSE;
   isCancellato:=FALSE;
   isAssociatoManrev:=FALSE;
   isNotAssociatoManrev:=FALSE;
   cv_stato_cofi_out:=NULL;
   cv_ti_associato_manrev_out:=NULL;
   eseguiLock:='Y';

   aTestata:=CNRCTB100.GETTABELLA(mem_tipo_docamm);
   aRiga:=CNRCTB100.GETTABELLADETT(mem_tipo_docamm);

   -------------------------------------------------------------------------------------------------
   -- Composizione dello statement di SELECT di lettura dei valori di stato_cofi assunti dalle righe
   -- di dettaglio del documento amministrativo in elaborazione

   IF aRiga IS NOT NULL THEN
      aStatement:='SELECT DISTINCT stato_cofi, ti_associato_manrev FROM ' ||
                   aRiga || ' ';
      aBaseStatement:=CNRCTB100.getRigaWhereCondForKey(mem_tipo_docamm,
                                                       mem_cd_cds,
                                                       mem_esercizio,
                                                       mem_cd_uo,
                                                       mem_pg_docamm,
                                                       NULL);
      aStatement:=aStatement || aBaseStatement;
      if inTipoManRev = 'MAN' then
     aStatementRighe:='SELECT cd_cds_obbligazione, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione, pg_obbligazione_scadenzario, im_riga FROM '||aRiga || ' ';
      else
     aStatementRighe:='SELECT cd_cds_accertamento, esercizio_accertamento, esercizio_ori_accertamento, pg_accertamento, pg_accertamento_scadenzario, im_riga FROM '||aRiga || ' ';
      end if;
      aStatementRighe:=aStatementRighe || aBaseStatement;
   ELSE
      aStatement:='SELECT DISTINCT stato_cofi, ti_associato_manrev FROM ' ||
                  aTestata || ' ';
      aBaseStatement:=CNRCTB100.getTstaWhereCondForKey(mem_tipo_docamm,
                                                       mem_cd_cds,
                                                       mem_esercizio,
                                                       mem_cd_uo,
                                                       mem_pg_docamm);
      aStatement:=aStatement || aBaseStatement;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento dello stato_cofi e di eventuali altri attributi della testata del documento
   -- amministrativo associato al mandato o reversale in elaborazone

   BEGIN

      OPEN gen_cur_b FOR
           aStatement;

      LOOP

         FETCH gen_cur_b INTO
               cv_stato_cofi_in,
               cv_ti_associato_manrev_in;

         EXIT WHEN gen_cur_b%NOTFOUND;

         BEGIN

            -- Valorizzazione dello stato di associato a mandati e reversali da impostare in testata
            -- del documento amministrativo

            IF cv_ti_associato_manrev_in = 'T' THEN
               isAssociatoManrev:=TRUE;
            ELSE
               IF aRiga IS NULL THEN
                  isAssociatoManrev:=TRUE;
               ELSE
                  isNotAssociatoManrev:=TRUE;
               END IF;
            END IF;

            -- Valorizzazione dello stato_cofi da impostare in testata del documento amministrativo

            IF    cv_stato_cofi_in = 'C' THEN
                  isContabilizzato:=TRUE;
            ELSIF cv_stato_cofi_in = 'P' THEN
                  isPagato:=TRUE;
            ELSIF cv_stato_cofi_in = 'A' THEN
                  isCancellato:=TRUE;
            END IF;

         END;

      END LOOP;

      CLOSE gen_cur_b;

      -- Se la gestione ? per inserimento di mandato/reversale lo stato di cancellato
      -- non conta; se la gestione ? per annullamento lo stato di cancellato conta solo
      -- se ? l'unico estratto

      IF aRiga IS NULL THEN
         IF inAzione = 'I' THEN
            cv_stato_cofi_out:='P';
         ELSE
            cv_stato_cofi_out:='C';
         END IF;
      ELSE
         IF inAzione = 'I' THEN
            IF    (isContabilizzato=TRUE AND
                   isPagato=TRUE) THEN
                   cv_stato_cofi_out:='Q';
            ELSIF (isContabilizzato=FALSE AND
                   isPagato=TRUE) THEN
                  cv_stato_cofi_out:='P';
            END IF;
         ELSE
            IF isCancellato=TRUE THEN
               IF (isContabilizzato=FALSE AND
                   isPagato=FALSE) THEN
                  cv_stato_cofi_out:='A';
                END IF;
            END IF;
            IF    (isContabilizzato=TRUE AND
                   isPagato=TRUE) THEN
                  cv_stato_cofi_out:='Q';
            ELSIF (isContabilizzato=TRUE AND
                   isPagato=FALSE) THEN
                  cv_stato_cofi_out:='C';
            ELSIF (isContabilizzato=FALSE AND
                   isPagato=TRUE) THEN
                  cv_stato_cofi_out:='P';
            END IF;
         END IF;
      END IF;

      -- Gestione dell'associazione on mandati o reversali

      IF isAssociatoManrev = TRUE THEN
         IF isNotAssociatoManrev = TRUE THEN
            cv_ti_associato_manrev_out:='P';
         ELSE
            cv_ti_associato_manrev_out:='T';
         END IF;
      ELSE
         cv_ti_associato_manrev_out:='N';
      END IF;

   END;

   -------------------------------------------------------------------------------------------------
   -- Composizione dello statement di UPDATE della testata del documento amministrativo


   -- Composizione della parte set dello statement di UPDATE comune a tutte le gestioni

   IF cv_stato_cofi_out = 'A' THEN
      aSetClause:='stato_cofi = ''' || cv_stato_cofi_out || ''', dt_cancellazione = ' ||
                  IBMUTL001.ASDYNDATE(dataOdierna);
   ELSE
      aSetClause:='stato_cofi = ''' || cv_stato_cofi_out || ''', ti_associato_manrev = ''' ||
                  cv_ti_associato_manrev_out || '''';
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento della testata del documento amministrativo

   BEGIN

      CNRCTB100.updateDocAmm(mem_tipo_docamm,
                             mem_cd_cds,
                             mem_esercizio,
                             mem_cd_uo,
                             mem_pg_docamm,
                             aSetClause,
                             NULL,
                             aUtente,
                             dataOdierna);
   END;

   -------------------------------------------------------------------------------------------------
   -- Aggiorna il saldo di collegamento con documenti amministrativi della scadenza di obbligazione o
   -- accertamento

   IF cv_stato_cofi_out = 'A' and aRiga is not null and aRiga = 'DOCUMENTO_GENERICO_RIGA' THEN

      OPEN gen_cur_b FOR
           aStatementRighe;

      LOOP

         FETCH gen_cur_b INTO
               cv_cd_cds_obbacr,
               cv_esercizio_obbacr,
               cv_esercizio_ori_obbacr,
               cv_pg_obbacr,
               cv_pg_scad_obbacr,
               cv_importo;

         EXIT WHEN gen_cur_b%NOTFOUND;

         BEGIN

            if inTipoManRev = 'MAN' then
               if not (mem_tipo_docamm = CNRCTB100.TI_GEN_REINTEGRO_FONDO) then
                  CNRCTB035.aggiornaSaldoDocammObb(cv_cd_cds_obbacr,
                                                   cv_esercizio_obbacr,
                                                   cv_esercizio_ori_obbacr,
                                                   cv_pg_obbacr,
                                                   cv_pg_scad_obbacr,
                                                   0-cv_importo,
                                                   aUtente);
               end if;
            else
               CNRCTB035.aggiornaSaldoDocammAcc(cv_cd_cds_obbacr,
                                                cv_esercizio_obbacr,
                                                cv_esercizio_ori_obbacr,
                                                cv_pg_obbacr,
                                                cv_pg_scad_obbacr,
                                                0-cv_importo,
                                                aUtente);
            end if;

         END;

      END LOOP;

      CLOSE gen_cur_b;

      -- Attiva gestione degli effetti collaterali del documento amministrativo

      effCollAnnDocGen(aRecManrev,
                       inTipoManrev);

   END IF;

END aggiornaStatoTestataDocamm;

-- =================================================================================================
-- Gestione effetti collaterali all'annullamento del documento amministrativo
-- =================================================================================================
PROCEDURE effCollAnnDocGen
   (
    aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE,
    inTipoManrev varchar2
   ) IS
  aObb obbligazione%rowtype;
  aAcc accertamento%rowtype;
  aObbN obbligazione%rowtype;
  aAccN accertamento%rowtype;
 begin
   -- Se i versamenti sono fatti su pratiche riportate, queste vengono deriportate
   if aRecManrev.cd_tipo_docamm in (CNRCTB100.TI_GEN_CORI_VER_ENTRATA, CNRCTB100.TI_GEN_CORI_VER_SPESA ) THEN
   if aRecManrev.cd_tipo_mandato = 'MAN' then
       select * into aObbN from obbligazione where
            cd_cds=aRecManrev.cd_cds_mandato
          and esercizio = aRecManrev.esercizio_mandato
          and esercizio_originale = aRecManrev.esercizio_ori_obbligazione
          and pg_obbligazione =aRecManrev.pg_obbligazione
       for update nowait;
    if aObbN.pg_obbligazione_ori_riporto is not null then
       CNRCTB046.ANNULLARIPPGIROCDS(aObbN,dataOdierna,aUtente);
    end if;
   elsif aRecManrev.cd_tipo_mandato = 'REV' then
    select * into aAccN from accertamento where
         cd_cds=aRecManrev.cd_cds_mandato
       and esercizio = aRecManrev.esercizio_mandato
       and esercizio_originale = aRecManrev.esercizio_ori_obbligazione
       and pg_accertamento =aRecManrev.pg_obbligazione
       for update nowait;
    if aAccN.pg_accertamento_ori_riporto is not null then
       CNRCTB046.ANNULLARIPPGIROCDS(aAccN,dataOdierna,aUtente);
    end if;
   end if;
   end if;

   -- Non vengono effettuate operazioni automatiche se non sugli accantonamenti
   if aRecManrev.cd_tipo_docamm in (CNRCTB100.TI_GEN_CORI_ACC_ENTRATA, CNRCTB100.TI_GEN_CORI_ACC_SPESA ) THEN

   -- In caso di annullamento del documento amministrativo collegato
   -- Aggiorna le annotazioni a livello di importo associato a doc amministrativi
   -- Si assume che le scadenze di annotazione siano totalemente collegate al documento in processo
   -- e quindi il loro importo viene portato a 0
    if inTipoManrev = 'MAN' then
     CNRCTB035.annullaObbligazione(
      aRecManrev.cd_cds_mandato,
      aRecManrev.esercizio_mandato,
      aRecManrev.esercizio_ori_obbligazione,
      aRecManrev.pg_obbligazione,
      aUtente
   );
  else

     -- se la reversale ? quella di accantonamento CORI al centro, l'operazione viene bloccata
     declare
    aLACC accertamento%rowtype;
   begin
    select * into aLACC from accertamento a where
           cd_cds = aRecManrev.cd_cds_mandato
         and esercizio = aRecManrev.esercizio_mandato
         and esercizio_originale = aRecManrev.esercizio_ori_obbligazione
         and pg_accertamento = aRecManrev.pg_obbligazione
         and exists (select 1 from ass_obb_acr_pgiro la, liquid_gruppo_centro l where
                    la.cd_cds = a.cd_cds
          and la.esercizio = a.esercizio
          and la.esercizio_ori_accertamento = a.esercizio_originale
          and la.pg_accertamento = a.pg_accertamento
          and l.cd_cds_obb_accentr = la.cd_cds
          and l.esercizio_obb_accentr = la.esercizio
          and l.esercizio_ori_obb_accentr = la.esercizio_ori_obbligazione
          and l.pg_obb_accentr = la.pg_obbligazione
     );
    IBMERR001.RAISE_ERR_GENERICO('La Reversale di accantonamento CORI per versamento centralizzato non é cancellabile');
   exception when NO_DATA_FOUND then
      null;
   end;

     -- se la reversale ? quella di accantonamento LIQUIDAZIONE IVA al centro, l'operazione viene bloccata
     declare
    aLACC accertamento%rowtype;
   begin
    select * into aLACC from accertamento a where
           cd_cds = aRecManrev.cd_cds_mandato
         and esercizio = aRecManrev.esercizio_mandato
         and esercizio_originale = aRecManrev.esercizio_ori_obbligazione
         and pg_accertamento = aRecManrev.pg_obbligazione
         and exists (select 1 from ass_obb_acr_pgiro la, liquidazione_iva_centro l where
                    la.cd_cds = a.cd_cds
          and la.esercizio = a.esercizio
          and la.esercizio_ori_accertamento = a.esercizio_originale
          and la.pg_accertamento = a.pg_accertamento
          and l.cd_cds_obb_accentr = la.cd_cds
          and l.esercizio_obb_accentr = la.esercizio
          and l.esercizio_ori_obb_accentr = la.esercizio_ori_obbligazione
          and l.pg_obb_accentr = la.pg_obbligazione
     );
    IBMERR001.RAISE_ERR_GENERICO('La Reversale di accantonamento liquidazione IVA centro non é cancellabile');
   exception when NO_DATA_FOUND then
      null;
   end;

     CNRCTB035.annullaAccertamento(
      aRecManrev.cd_cds_mandato,
      aRecManrev.esercizio_mandato,
      aRecManrev.esercizio_ori_obbligazione,
      aRecManrev.pg_obbligazione,
      aUtente
   );
  end if;
   end if;

 end effCollAnnDocGen;



PROCEDURE effCollRigaManRev(aRecManrev V_MANDATO_UPG_STATO_DOCAMM%ROWTYPE,
                       inTipoManrev varchar2,
                       inAzione varchar2
            ) is
begin
 -- Tipo di documento reversale
 if inTipoManrev = 'REV' then
  -- Azione di annullamento
  if inAzione = 'A' then
   -- Non possibile annullare chiusura fondo economale
   if aRecManrev.cd_tipo_docamm = CNRCTB100.TI_GEN_CHIUSURA_FONDO then
    IBMERR001.RAISE_ERR_GENERICO('Il mandato di chiusura del fondo economale non é annullabile');
   end if;
  end if; -- Fine Annullamento
 end if; -- Fine Reversale

 -- Tipo di documento mandato
 if inTipoManrev = 'MAN' then
  -- Azione di annullamento
  if inAzione = 'A' then
   -- Eliminazione liquidazione IVA
   begin
    for aLIVAC in (select * from liquidazione_iva where
                              esercizio_doc_amm = aRecManrev.esercizio_docamm
              and cd_tipo_documento = aRecManrev.cd_tipo_docamm
              and cd_cds_doc_amm = aRecManrev.cd_cds_docamm
              and cd_uo_doc_amm = aRecManrev.cd_uo_docamm
              and pg_doc_amm = aRecManrev.pg_docamm
           for update nowait
  ) loop
     IBMERR001.RAISE_ERR_GENERICO('Operazione non permessa');
    end loop;
   end;
  end if; -- Fine Annullamento
 end if; -- Fine mandato
end effCollRigaManRev;

PROCEDURE effCollFinale (
    inCDSManrev VARCHAR2,
    inEsercizioManrev NUMBER,
    inPgManrev NUMBER,
    inTipoManrev VARCHAR2,
    inAzione VARCHAR2,
    inUtente VARCHAR2
) is
 aCount number;
 aMan mandato%rowtype;
 aCompenso compenso%rowtype;
 aCodaUpdateCompenso varchar2(500);
 aAccScadCen accertamento_scadenzario%rowtype;
 aAccCen accertamento%rowtype;
 aDocGen documento_generico%rowtype;
begin
 aCodaUpdateCompenso:='';
 -- Se annullo un mandato o reversale CORI senza mandato principale devo riportare in stato C il compenso se non ? gi? in stato C
 if inAzione = 'A' then
  -- Le azioni di annullamento della liquidazione del compenso vanno fatte solo per compensi senza mandato principale
  for aVASSComp in (select * from ass_comp_doc_cont_nmp where
            cd_cds_doc = inCDSManrev
      and esercizio_doc = inEsercizioManrev
      and cd_tipo_doc = decode(inTipoManrev,'MAN','M','R')
        and pg_doc = inPgManrev
  for update nowait
  ) loop
   begin
    select * into aCompenso from compenso where
          cd_cds = aVASSComp.cd_cds_compenso
    and esercizio = aVASSComp.esercizio_compenso
    and cd_unita_organizzativa = aVASSComp.cd_uo_compenso
    and pg_compenso = aVASSComp.pg_compenso
  for update nowait;
  -- Se il compenso ? collegato a accertamento al centro con generico di recupero crediti da terzi, devo annullare tale accertamento ed eliminare il generico
  if
         aCompenso.stato_cofi = CNRCTB100.STATO_COM_COFI_TOT_MR
       and aCompenso.pg_doc_genrc is not null
  then
      select * into aDocGen from documento_generico where
                            cd_tipo_documento_amm = aCompenso.cd_tipo_doc_genrc
                              and cd_cds = aCompenso.cd_cds_doc_genrc
                              and esercizio = aCompenso.esercizio_doc_genrc
                              and cd_unita_organizzativa = aCompenso.cd_uo_doc_genrc
                              and pg_documento_generico = aCompenso.pg_doc_genrc
                for update nowait;
      if aCompenso.pg_accertamento is not null then
           -- Verifico che la pratica non sia con reversale al centro altrimenti sollevo errore
       aAccCen.cd_cds:=aCompenso.cd_cds_accertamento;
       aAccCen.esercizio:=aCompenso.esercizio_accertamento;
       aAccCen.esercizio_originale:=aCompenso.esercizio_ori_accertamento;
       aAccCen.pg_accertamento:=aCompenso.pg_accertamento;
       CNRCTB035.LOCKDOCFULL(aAccCen);
       if aAccCen.riportato='Y' then
            IBMERR001.RAISE_ERR_GENERICO('L''accertamento di recupero crediti da terzi risulta riportato a nuovo esercizio');
       end if;
           select * into aAccScadCen from accertamento_scadenzario where
               cd_cds =aAccCen.cd_cds
           and esercizio =aAccCen.esercizio
           and esercizio_originale =aAccCen.esercizio_originale
           and pg_accertamento =aAccCen.pg_accertamento;
       if aAccScadCen.im_associato_doc_contabile = 0 then
        update accertamento_scadenzario set
         im_associato_doc_amm = 0,
       duva = dataOdierna,
       utuv = aUtente,
       pg_ver_rec=pg_ver_rec+1
        where
               cd_cds =aAccCen.cd_cds
           and esercizio =aAccCen.esercizio
           and esercizio_originale =aAccCen.esercizio_originale
           and pg_accertamento =aAccCen.pg_accertamento;
            if aAccCen.dt_cancellazione is null then
             -- Elimino accertamento al centro e cancello il documento generico
             CNRCTB035.annullaAccertamento(
              aAccCen.cd_cds,
              aAccCen.esercizio,
        aAccCen.esercizio_originale,
              aAccCen.pg_accertamento,
              aUtente
           );
       -- Toglie in COMPENSO i riferimenti alla pratica al centro
           CNRCTB100.updateDocAmm (CNRCTB100.TI_COMPENSO,
                              aCompenso.cd_cds,
                              aCompenso.esercizio,
                              aCompenso.cd_unita_organizzativa,
                              aCompenso.pg_compenso,
                              ' esercizio_accertamento = null, cd_cds_accertamento = null, esercizio_ori_accertamento = null, pg_accertamento = null, pg_accertamento_scadenzario = null, cd_tipo_doc_genrc=null,cd_cds_doc_genrc = null, esercizio_doc_genrc = null,cd_uo_doc_genrc=null,pg_doc_genrc=null ',
                              NULL,
                              aUtente,
                              dataOdierna
                             );
             -- Elimina fisicamente il documento_generico
         delete from documento_generico_riga where
                            cd_tipo_documento_amm=aCompenso.cd_tipo_doc_genrc
                              and cd_cds=aCompenso.cd_cds_doc_genrc
                              and esercizio = aCompenso.esercizio_doc_genrc
                              and cd_unita_organizzativa = aCompenso.cd_uo_doc_genrc
                              and pg_documento_generico = aCompenso.pg_doc_genrc;
         delete from documento_generico where
                            cd_tipo_documento_amm=aCompenso.cd_tipo_doc_genrc
                              and cd_cds=aCompenso.cd_cds_doc_genrc
                              and esercizio = aCompenso.esercizio_doc_genrc
                              and cd_unita_organizzativa = aCompenso.cd_uo_doc_genrc
                              and pg_documento_generico = aCompenso.pg_doc_genrc;
        end if;
       else
            IBMERR001.RAISE_ERR_GENERICO('L''accertamento al centro per recupero crediti da terzi:'||aAccScadCen.pg_accertamento||' es.'||aAccScadCen.esercizio||' collegato a compenso:'||aCompenso.pg_compenso||' uo:'||aCompenso.cd_unita_organizzativa||' es:'||aCompenso.esercizio||' risulta associato a reversale.');
       end if;
      end if;
  end if;
  if
         aCompenso.stato_cofi = CNRCTB100.STATO_COM_COFI_TOT_MR
    then
        CNRCTB100.updateDocAmm (CNRCTB100.TI_COMPENSO,
                              aCompenso.cd_cds,
                              aCompenso.esercizio,
                              aCompenso.cd_unita_organizzativa,
                              aCompenso.pg_compenso,
                              ' stato_cofi = '''||CNRCTB100.STATO_COM_COFI_CONT||'''',
                              NULL,
                              aUtente,
                              dataOdierna
                             );
  end if;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Compenso n.'||aVASSComp.pg_compenso||' cds '||aVASSComp.cd_cds_compenso||' uo '||aVASSComp.cd_uo_compenso||' es.'||aVASSComp.esercizio_compenso||' non trovato');
   end;
   delete from ass_comp_doc_cont_nmp where
            cd_cds_doc = inCDSManrev
      and esercizio_doc = inEsercizioManrev
      and cd_tipo_doc = decode(inTipoManrev,'MAN','M','R')
        and pg_doc = inPgManrev;
   exit;
  end loop;
 end if;

 -- Tipo di documento mandato
 if inTipoManrev = 'MAN' then
  -- Azione di annullamento
  if inAzione = 'A' then
   -- Se il mandato ? di reintegro di fondo economale
   begin
   select 1 into aCount from dual where exists (select 1 from fondo_spesa where
                              esercizio_mandato = inEsercizioManrev
              and cd_cds_mandato = inCDSManrev
              and pg_mandato = inPgManrev
             );
   begin
    select * into aMan from mandato where
                              esercizio = inEsercizioManrev
              and cd_cds = inCDSManrev
              and pg_mandato = inPgManrev
      for update nowait;
    CNRCTB130.ANNULLAREINTEGROSPESEFONDO(
                      aMan.cd_cds,
            aMan.esercizio,
            aMan.pg_mandato,
            aMan.im_mandato,
            aUtente,
            dataOdierna
        );
   exception when NO_DATA_FOUND then
      IBMERR001.RAISE_ERR_GENERICO('Documento autorizatorio non trovato');
   end ;
   exception when NO_DATA_FOUND then
  null;
   end;
   -- Eliminazione mandato di liquidazione flusso stipendiale non permesso
   begin
    for aStipCofi in (select * from stipendi_cofi where
                              esercizio_mandato = inEsercizioManrev
              and cd_cds_mandato = inCDSManrev
              and pg_mandato = inPgManrev
  ) loop
      IBMERR001.RAISE_ERR_GENERICO('Il mandato di liquidazione mensile degli stipendi non é annullabile');
    end loop;
   end;
   -- Eliminazione liquidazione CORI (tranne che liquidazione accentrata)
   begin
    for aLGC in (select * from liquid_gruppo_cori where
                              esercizio_doc = inEsercizioManrev
              and cd_cds_doc = inCDSManrev
              and pg_doc = inPgManrev
  ) loop
     CNRCTB570.annullaLiquidazione(aLGC, aUtente);
    end loop;
   end;
  end if; -- Fine Annullamento
 end if; -- Fine mandato

 -- Tipo di documento reversale
 if inTipoManrev = 'REV' then
  -- Azione di annullamento
  if inAzione = 'A' then
   -- Eliminazione della reversale sciolta su gruppo negativo in UO di versamento accentrato (fix errore 722)
   begin
    for aLGC in (select * from liquid_gruppo_cori where
                              esercizio_rev = inEsercizioManrev
              and cd_cds_rev = inCDSManrev
              and pg_rev = inPgManrev
  ) loop
     IBMERR001.RAISE_ERR_GENERICO('La reversale risulta collegata ad una liquidazione CORI del centro. Non é possibile annullarla');
    end loop;
   end;
  end if; -- Fine Annullamento
 end if; -- Fine reversale

end effCollFinale;


PROCEDURE checkIniziale(
    inCDSManrev VARCHAR2,
    inEsercizioManrev NUMBER,
    inPgManrev NUMBER,
    inTipoManrev VARCHAR2,
    inAzione VARCHAR2,
    inUtente VARCHAR2
) is
 aMan mandato%rowtype;
 aRev reversale%rowtype;
 aCount number;
begin
 -- Tipo di documento mandato
 if inTipoManrev = 'MAN' then
  -- Leggo il mandato
  begin
   select * into aMan from mandato where
              esercizio  =inEsercizioManrev
      and cd_cds = inCDSManrev
      and pg_mandato = inPgManrev
   for update nowait;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Mandato non trovato');
  end;
  -- Azione di annullamento
  if inAzione = 'A' then
   -- Se il mandato ? di reintegro di fondo economale
   begin
   select 1 into aCount from dual where exists (select 1 from fondo_spesa where
                              esercizio_mandato = aMan.esercizio
              and cd_cds_mandato = aMan.cd_cds
              and pg_mandato = aMan.pg_mandato
             );
     -- Se il mandato ? di reintegro e di regolarizzazione non ? possibile annullarlo
   if aMan.ti_mandato = CNRCTB038.TI_MAN_REG then
      IBMERR001.RAISE_ERR_GENERICO('Il mandato di reintegro generato in chiusura del fondo economale non é annullabile');
   end if;
   exception when NO_DATA_FOUND then
  null;
   end;

   -- Eliminazione mandato di liquidazione flusso stipendiale non permesso
   begin
    for aStipCofi in (select * from stipendi_cofi where
                              esercizio_mandato = aMan.esercizio
              and cd_cds_mandato = aMan.cd_cds
              and pg_mandato = aMan.pg_mandato
  ) loop
      IBMERR001.RAISE_ERR_GENERICO('Il mandato di liquidazione mensile degli stipendi non é annullabile');
    end loop;
   end;
  end if; -- Fine Annullamento
 end if; -- Fine mandato
 end checkIniziale;

 procedure checkDocAmmCambiato (aTiDocAmm varchar2,aCdCds varchar2,aEs number,aCdUo varchar2,aPgDocAmm number, aPgVerRec number) is
  aNewPgVerRec number;
 begin
  CNRCTB100.LOCKDOCAMM(aTiDocAmm,aCdCds,aEs,aCdUo,aPgDocAmm);
  begin
   select distinct pg_ver_rec into aNewPgVerRec from v_doc_passivo where
        CD_CDS = aCdCds
    AND CD_UNITA_ORGANIZZATIVA = aCdUo
  AND ESERCIZIO = aEs
  AND CD_TIPO_DOCUMENTO_AMM = aTiDocAmm
  AND PG_DOCUMENTO_AMM = aPgDocAmm;
  exception when NO_DATA_FOUND then
   begin
    select distinct pg_ver_rec into aNewPgVerRec from v_doc_attivo where
         CD_CDS = aCdCds
     AND CD_UNITA_ORGANIZZATIVA = aCdUo
   AND ESERCIZIO = aEs
   AND CD_TIPO_DOCUMENTO_AMM = aTiDocAmm
   AND PG_DOCUMENTO_AMM = aPgDocAmm;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Documento amministrativo non trovato ('||aEs||'/'||aCdCds||'/'||aCdUo||'/'||aTiDocAmm||'/'||aPgDocAmm||')');
   end;
  end;
  if
      aNewPgVerRec is null
   or aPgVerRec is null
   or aNewPgVerRec <> aPgVerRec
  then
   IBMERR001.RAISE_ERR_GENERICO('Risorsa non piu'' valida');
  end if;
 end;

END;
