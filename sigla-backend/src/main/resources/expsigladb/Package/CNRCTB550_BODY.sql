--------------------------------------------------------
--  DDL for Package Body CNRCTB550
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB550" AS

-- =================================================================================================
-- Calcolo della deduzione teorica annua FAMILY
-- =================================================================================================
FUNCTION calcolaDeduzioneTeoricaFamily
   (
    aCdAnag ANAGRAFICO.cd_anag%TYPE,
    inCdsMcarriera MINICARRIERA.cd_cds%TYPE,
    inUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
    inPgMinicarriera MINICARRIERA.pg_minicarriera%Type
   ) Return Number IS

   aImportoDeduzione number(15,4);

   aDataRifDa DATE;
   aDataRifA DATE;
   aDifferenzaMesi INTEGER;

   aDataDa DATE;
   aDataA DATE;

   memTiPersona CARICO_FAMILIARE_ANAG.ti_persona%TYPE;
   memCodiceFiscale CARICO_FAMILIARE_ANAG.codice_fiscale%TYPE;
   aChiaveTecnica CARICO_FAMILIARE_ANAG.codice_fiscale%TYPE;
   aContatore INTEGER;
   aNumero INTEGER;

   aRecCaricoFamAnag CARICO_FAMILIARE_ANAG%ROWTYPE;
   aRecDetrazioniFamiliari DETRAZIONI_FAMILIARI%ROWTYPE;

   gen_cur_car GenericCurTyp;

   -- Definizione tabella PL/SQL di appoggio calcolato detrazioni familiari

   indice BINARY_INTEGER;
   indice1 BINARY_INTEGER;

   tabella_num_car_fam CNRCTB545.numCaricoFamTab;
   tabella_car_fam_ok CNRCTB545.caricoFamTab;
   tabella_car_fam_tmp CNRCTB545.caricoFamTab;

   aDtCompetenzaDa DATE;
   aDtCompetenzaA DATE;
   aDtRegistrazione DATE;
   outTiPersona CHAR(1);
   Esci Exception;
Begin
   aImportoDeduzione := 0;
   IF aOrigineCompenso!=CNRCTB545.isCompensoConguaglio THEN
     aDtCompetenzaDa :=IBMUTL001.getFirstDayOfMonth(TO_DATE('0101' || (inEsercizioMcarriera), 'DDMMYYYY'));
     aDtCompetenzaA :=IBMUTL001.getLastDayOfMonth(TO_DATE('3112' || (inEsercizioMcarriera), 'DDMMYYYY'));
   Else
     aDtCompetenzaDa :=IBMUTL001.getFirstDayOfMonth(TO_DATE('0101' || aRecCompenso.esercizio, 'DDMMYYYY'));
     aDtCompetenzaA :=IBMUTL001.getLastDayOfMonth(TO_DATE('3112' || aRecCompenso.esercizio, 'DDMMYYYY'));
   End If;
   -------------------------------------------------------------------------------------------------
   -- Controllo che esistano carichi familiari

   IF (CNRCTB080.chkEsisteDetrazFam (aCdAnag,
                                     aDtCompetenzaDa,
                                     aDtCompetenzaA) = 0) THEN
       Return aImportoDeduzione;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione variabili per il calcolo

   -- Azzeramento delle matrici dei carichi familiari (familiari e numero degli stessi per periodo)

   tabella_num_car_fam.DELETE;
   tabella_car_fam_ok.DELETE;
   tabella_car_fam_tmp.DELETE;

   indice:=0;
   indice1:=0;
   aContatore:=0;
   aChiaveTecnica:='TEMPKEY';

   -- Composizione dell'intervallo date in base alla competenza economica del compenso normalizzando le
   -- date di inizio e fine di competenza economica del compenso rispettivamente alla corrispondente data
   -- di inizio e fine mese

   --aDataRifDa:=IBMUTL001.getFirstDayOfMonth(aDtCompetenzaDa);
   --aDataRifA:=IBMUTL001.getLastDayOfMonth(aDtCompetenzaA);
   --aDifferenzaMesi:=IBMUTL001.getMonthsBetween(aDataRifDa, aDataRifA);

   aDataRifDa := aDtCompetenzaDa;
   aDataRifA  := aDtCompetenzaA;
   aDifferenzaMesi:=IBMUTL001.getMonthsBetween(aDataRifDa, aDataRifA);
   -- Costruzione a zero della matrice numero familiari per periodo (tutti i mesi)
   For indice IN 1 .. aDifferenzaMesi Loop
     IF indice = 1 THEN
        aDataDa:=aDataRifDa;
     ELSE
        aDataDa:=IBMUTL001.getAddMonth(aDataRifDa, indice - 1);
     END IF;
     aDataA:=IBMUTL001.getLastDayOfMonth(aDataDa);
     tabella_num_car_fam(indice).tDataDa:=aDataDa;
     tabella_num_car_fam(indice).tDataA:=aDataA;
     tabella_num_car_fam(indice).tNAltro:=0;
     tabella_num_car_fam(indice).tNConiuge:=0;
     tabella_num_car_fam(indice).tNFiglio:=0;
   End Loop;

   -- Azzeramento campi di memoria per loop sui carichi familiari

   memTiPersona:=NULL;
   memCodiceFiscale:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Ciclo lettura carichi familiari. Si esclude il carico convenzionale per la gestione delle
   -- detrazioni per lavoro assimilato. L'intervallo data di validit� costretto nel periodo di
   -- competenza economica del compenso

   BEGIN

      OPEN gen_cur_car FOR

           SELECT *
           FROM   CARICO_FAMILIARE_ANAG
           WHERE  cd_anag = aCdAnag AND
                  ti_persona != 'L' AND
                  (
                      (dt_ini_validita <= aDataRifDa AND
                       dt_fin_validita >= aDataRifDa)
                   OR
                      (dt_ini_validita <= aDataRifA AND
                       dt_fin_validita >= aDataRifA)
                  )
           ORDER BY ti_persona, codice_fiscale, dt_ini_validita;

      LOOP

         FETCH gen_cur_car INTO
               aRecCaricoFamAnag;

         EXIT WHEN gen_cur_car%NOTFOUND;
         Begin
           --Controllo se �n Conguaglio e se la data di fine validit�el carico �nterna all'anno
           --in tal caso non ha diritto
           If aOrigineCompenso=CNRCTB545.isCompensoConguaglio And
              aRecCaricoFamAnag.dt_fin_validita < aDtCompetenzaA Then
             Raise Esci;
           End If;
           aContatore:=aContatore + 1;
           -- Normalizzazione date, costrizione nel periodo di competenza economica del compenso

           aDataDa:=IBMUTL001.getFirstDayOfMonth(aRecCaricoFamAnag.dt_ini_validita);
           aDataA:=IBMUTL001.getLastDayOfMonth(aRecCaricoFamAnag.dt_fin_validita);

           IF aDataDa < aDataRifDa THEN
              aDataDa:=aDataRifDa;
           END IF;
           IF aDataA > aDataRifA THEN
              aDataA:=aDataRifA;
           END IF;
           aRecCaricoFamAnag.dt_ini_validita:=aDataDa;
           aRecCaricoFamAnag.dt_fin_validita:=aDataA;
           -- Se il campo del codice fiscale non �alorizzato allora si considera ogni record come
           -- distinto dagli altri

           IF aRecCaricoFamAnag.codice_fiscale IS NULL THEN
              aRecCaricoFamAnag.codice_fiscale:=aChiaveTecnica || LPAD(aContatore,5,0);
           END IF;

           -- Se prima volta azzero tabella tmp

           IF memCodiceFiscale IS NULL THEN
              tabella_car_fam_tmp.DELETE;
              indice1:=0;
              memTiPersona:=aRecCaricoFamAnag.ti_persona;
              memCodiceFiscale:=aRecCaricoFamAnag.codice_fiscale;
           END IF;

           -- Il record letto �dentico al precedente

           IF (aRecCaricoFamAnag.ti_persona = memTiPersona AND
               aRecCaricoFamAnag.codice_fiscale = memCodiceFiscale) THEN
              riempiCarFamTmp (indice1,
                               aRecCaricoFamAnag,
                               tabella_car_fam_tmp);
           ELSE
              scaricaCarFamTmp(indice,
                               tabella_car_fam_ok,
                               tabella_car_fam_tmp);
              tabella_car_fam_tmp.DELETE;
              indice1:=0;
              memTiPersona:=aRecCaricoFamAnag.ti_persona;
              memCodiceFiscale:=aRecCaricoFamAnag.codice_fiscale;
              riempiCarFamTmp (indice1,
                               aRecCaricoFamAnag,
                               tabella_car_fam_tmp);
           END IF;
         Exception
           When Esci Then
             Null;
         End;
      END LOOP;

      Close gen_cur_car;
      If aContatore > 0 Then
        scaricaCarFamTmp(indice,
                         tabella_car_fam_ok,
                         tabella_car_fam_tmp);
      End If;
      -- Aggiornamento della matrice numeri persone per periodo
      Begin
       If  tabella_car_fam_ok.COUNT > 0 Then
         For indice IN tabella_car_fam_ok.FIRST .. tabella_car_fam_ok.LAST Loop
           For indice1 IN tabella_num_car_fam.FIRST .. tabella_num_car_fam.LAST Loop
             If (tabella_car_fam_ok(indice).tDataDa <= tabella_num_car_fam(indice1).tDataDa AND
                 tabella_car_fam_ok(indice).tDataA >= tabella_num_car_fam(indice1).tDataA ) THEN
                 If tabella_car_fam_ok(indice).tTiPersona = 'A' THEN
                   tabella_num_car_fam(indice1).tNAltro:=tabella_num_car_fam(indice1).tNAltro + 1;
                 Elsif tabella_car_fam_ok(indice).tTiPersona = 'C' THEN
                   tabella_num_car_fam(indice1).tNConiuge:=tabella_num_car_fam(indice1).tNConiuge + 1;
                 Elsif tabella_car_fam_ok(indice).tTiPersona = 'F' THEN
                   tabella_num_car_fam(indice1).tNFiglio:=tabella_num_car_fam(indice1).tNFiglio + 1;
                 End If;
               End If;
            End Loop;
         End Loop;
       End If;
      End;
   End;
   -- Calcolo delle deduzioni
   Begin
    If  tabella_car_fam_ok.COUNT > 0 Then
     For indice In tabella_car_fam_ok.FIRST .. tabella_car_fam_ok.LAST Loop
      For indice1 IN tabella_num_car_fam.FIRST .. tabella_num_car_fam.LAST Loop
       If (tabella_car_fam_ok(indice).tDataDa <= tabella_num_car_fam(indice1).tDataDa AND
           tabella_car_fam_ok(indice).tDataA >= tabella_num_car_fam(indice1).tDataA ) Then
         Begin
           If tabella_car_fam_ok(indice).tTiPersona = 'A' Then
             outTiPersona:='A';
           Elsif tabella_car_fam_ok(indice).tTiPersona = 'C' Then
             outTiPersona:='C';
           Elsif tabella_car_fam_ok(indice).tTiPersona = 'F' Then
             If tabella_car_fam_ok(indice).tFlHandicap = 'Y' Then
               outTiPersona:='H';
             Elsif tabella_car_fam_ok(indice).tDtMinoreTre Is Not Null And
                tabella_car_fam_ok(indice).tDtMinoreTre > tabella_num_car_fam(indice1).tDataDA Then
               outTiPersona:='B';
             Elsif tabella_car_fam_ok(indice).tFlPrimoFiglioMancaCon = 'Y' Then
               outTiPersona:='M';
             Else
               outTiPersona:='F';
             End If;
           End If;
         End;
         If outTiPersona = 'A' Then
            aImportoDeduzione:= aImportoDeduzione + ROUND(((glbDFamilyAltro/12) * tabella_car_fam_ok(indice).tPrcCarico / 100),4);
         Elsif outTiPersona = 'C' THEN
            aImportoDeduzione:= aImportoDeduzione + ROUND(((glbDFamilyConiuge/12) * tabella_car_fam_ok(indice).tPrcCarico / 100),4);
         Elsif outTiPersona = 'F' Then
            aImportoDeduzione:= aImportoDeduzione + ROUND(((glbDFamilyFiglio/12) * tabella_car_fam_ok(indice).tPrcCarico / 100),4);
         Elsif outTiPersona = 'B' Then
            aImportoDeduzione:= aImportoDeduzione + ROUND(((glbDFamilyFiglioMenoTre/12) * tabella_car_fam_ok(indice).tPrcCarico / 100),4);
         Elsif outTiPersona = 'M' Then
            aImportoDeduzione:= aImportoDeduzione + ROUND(((glbDFamilyFiglioSenzaConiuge/12) * tabella_car_fam_ok(indice).tPrcCarico / 100),4);
         Elsif outTiPersona = 'H' Then
            aImportoDeduzione:= aImportoDeduzione + ROUND(((glbDFamilyFiglioHandicap/12) * tabella_car_fam_ok(indice).tPrcCarico / 100),4);
         End If;
       End If;
      End Loop;
     End Loop;
    End If;
   End;
   Return aImportoDeduzione;
END calcolaDeduzioneTeoricaFamily;

PROCEDURE verificaCoerenzaFatturaEle
(
    inIdPaeseEle DOCUMENTO_ELE_TESTATA.id_paese%TYPE,
    inIdCodiceEle DOCUMENTO_ELE_TESTATA.id_codice%TYPE,
    inIdentificativoSdiEle DOCUMENTO_ELE_TESTATA.identificativo_sdi%TYPE,
    inProgressivoEle DOCUMENTO_ELE_TESTATA.progressivo%TYPE)
    IS

    imponiIva   CONTRIBUTO_RITENUTA.IMPONIBILE%TYPE:=0;
    imponiRit   CONTRIBUTO_RITENUTA.IMPONIBILE%TYPE:=0;
    imponiCassa CONTRIBUTO_RITENUTA.IMPONIBILE%TYPE:=0;
    impostaIva   CONTRIBUTO_RITENUTA.AMMONTARE%TYPE:=0;
    impostaRit   CONTRIBUTO_RITENUTA.AMMONTARE%TYPE:=0;
    impostaCassa CONTRIBUTO_RITENUTA.AMMONTARE%TYPE:=0;

    imponiIvaEle   CONTRIBUTO_RITENUTA.IMPONIBILE%TYPE:=0;
    imponiRitEle   CONTRIBUTO_RITENUTA.IMPONIBILE%TYPE:=0;
    imponiCassaEle CONTRIBUTO_RITENUTA.IMPONIBILE%TYPE:=0;
    impostaIvaEle   CONTRIBUTO_RITENUTA.AMMONTARE%TYPE:=0;
    impostaRitEle   CONTRIBUTO_RITENUTA.AMMONTARE%TYPE:=0;
    impostaCassaEle CONTRIBUTO_RITENUTA.AMMONTARE%TYPE:=0;

    totFattura      DOCUMENTO_ELE_TESTATA.IMPORTO_DOCUMENTO%TYPE:=0;
BEGIN

     FOR REC_CR IN (select cr.cd_contributo_ritenuta, tcr.cd_classificazione_cori, cr.imponibile, cr.ammontare
                    from contributo_ritenuta cr, tipo_contributo_ritenuta tcr
                    where cr.cd_contributo_ritenuta = tcr.cd_contributo_ritenuta
                      and cr.dt_ini_validita = tcr.dt_ini_validita
                      and cr.cd_cds = aRecCompenso.cd_cds
                      and cr.cd_unita_organizzativa = aRecCompenso.cd_unita_organizzativa
                      and cr.esercizio = aRecCompenso.esercizio
                      and cr.pg_compenso = aRecCompenso.pg_compenso)
     LOOP
--pipe.send_message('rec_cr.cd_classificazione_cori = '||rec_cr.cd_classificazione_cori);

           if rec_cr.cd_classificazione_cori = cnrctb545.isCoriFiscale then
               imponiRit := imponiRit + rec_cr.imponibile;
               impostaRit := impostaRit + rec_cr.ammontare;
           elsif rec_cr.cd_classificazione_cori = cnrctb545.isCoriIva then
               imponiIva := imponiIva + rec_cr.imponibile;
               impostaIva := impostaIva + rec_cr.ammontare;
           elsif rec_cr.cd_classificazione_cori = cnrctb545.isCoriRivalsa then
               imponiCassa := imponiCassa + rec_cr.imponibile;
               impostaCassa := impostaCassa + rec_cr.ammontare;
           end if;
--pipe.send_message('impostaRit = '||impostaRit);
---pipe.send_message('impostaIva = '||impostaIva);
--pipe.send_message('impostaCassa = '||impostaCassa);

     END LOOP;


     FOR REC_ELE IN (select tipo_riga,  importo
                    from documento_ele_tributi
                    where id_paese = inIdPaeseEle
                      and id_codice = inIdCodiceEle
                      and identificativo_sdi = inIdentificativoSdiEle
                      and progressivo = inProgressivoEle)
     LOOP
--pipe.send_message('rec_ele.tipo_riga = '||rec_ele.tipo_riga);

           if rec_ele.tipo_riga = 'RIT' then
               imponiRitEle := imponiRitEle + 0;
               impostaRitEle := impostaRitEle + rec_ele.importo;
           elsif rec_ele.tipo_riga = 'CAS' then
               imponiCassaEle := imponiCassaEle + 0;
               impostaCassaEle := impostaCassaEle + rec_ele.importo;
           end if;
--pipe.send_message('impostaRitEle = '||impostaRitEle);
--pipe.send_message('impostaCassaEle = '||impostaCassaEle);
     END LOOP;


     if impostaRit != impostaRitEle then
        IBMERR001.RAISE_ERR_GENERICO('L''importo della ritenuta calcolato nel compenso: ' || ltrim(to_char(impostaRit,'999g999g999g990d00')) || ' e'' diverso da quello presente nella fattura elettronica:  ' || ltrim(to_char(impostaRitEle,'999g999g999g990d00')));
     elsif impostaCassa != impostaCassaEle then
        IBMERR001.RAISE_ERR_GENERICO('L''importo della cassa calcolato nel compenso: ' || ltrim(to_char(impostaCassa,'999g999g999g990d00')) || ' e'' diverso da quello presente nella fattura elettronica:  ' || ltrim(to_char(impostaCassaEle,'999g999g999g990d00')));
     end if;


END;

-- =================================================================================================
-- Lettura di un compenso esistente e calcolo dei suoi dettagli; CONTRIBUTO_RITENUTA e CONTRIBUTO_RITENUTA_DET
-- =================================================================================================
PROCEDURE elaboraCompenso
   (
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompenso COMPENSO.pg_compenso%TYPE,
    inCopiaCdsCompenso COMPENSO.cd_cds%TYPE,
    inCopiaUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inCopiaEsercizioCompenso COMPENSO.esercizio%TYPE,
    inCopiaPgCompenso COMPENSO.pg_compenso%TYPE,
    inCdsMcarriera MINICARRIERA.cd_cds%TYPE,
    inUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
    inPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE,
    inIdPaeseEle DOCUMENTO_ELE_TESTATA.id_paese%TYPE,
    inIdCodiceEle DOCUMENTO_ELE_TESTATA.id_codice%TYPE,
    inIdentificativoSdiEle DOCUMENTO_ELE_TESTATA.identificativo_sdi%TYPE,
    inProgressivoEle DOCUMENTO_ELE_TESTATA.progressivo%TYPE

   ) IS
   imCORIPercipiente NUMBER(15,2);
   imCORIEnte NUMBER(15,2);
   aRecMontanti MONTANTI%ROWTYPE;
   eseguiLock CHAR(1);
   aInserimentoModifica CHAR(1);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   dataOdierna:=sysdate;
   eseguiLock:='Y';
   IF inCopiaPgCompenso IS NULL THEN
      aInserimentoModifica:='I';
   ELSE
      aInserimentoModifica:='M';
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Lettura dati di base legati al compenso in calcolo, testata compenso e anagrafico comprensivo,
   -- eventualmente, di quelli originali in caso di modifica. I dati sono memorizzati rispettivamente
   -- in aRecCompenso, aRecAnagrafico e aRecCompensoOri, aRecAnagraficoOri

   getDatiBaseCompenso(inCdsCompenso,
                       inUoCompenso,
                       inEsercizioCompenso,
                       inPgCompenso,
                       inCopiaCdsCompenso,
                       inCopiaUoCompenso,
                       inCopiaEsercizioCompenso,
                       inCopiaPgCompenso,
                       eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Lettura dati relativi ai montanti per il soggetto anagrafico di riferimento e costruzione della
   -- matrice degli stessi. Se non sono definiti i montanti �ornato un record con i valori a zero.
   -- I valori dei montanti sono ridotti di quanto calcolato dal compenso origine in caso di modifica
   -- se coincidono il riferimento all'anagrafico e quello del tipo compenso dipendente o altro

   aRecMontanti:=CNRCTB080.getMontanti(aRecCompenso.esercizio,
                                       aRecAnagrafico.cd_anag,
                                       eseguiLock);
   costruisciTabMontanti(aRecMontanti);
   IF (aRecCompensoOri.cd_cds IS NOT NULL AND
       --aRecCompenso.ti_anagrafico = 'A' AND -- anche per i DIP devo aggiornare i montanti (solo per la RIDUZIONE ERARIALE)
       aRecCompenso.ti_anagrafico = aRecCompensoOri.ti_anagrafico AND
       aRecAnagrafico.cd_anag = aRecAnagraficoOri.cd_anag) THEN
      modificaTabMontanti(aRecCompensoOri,
                          '-');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Recupero degli accantonamenti delle addizionali territorio. I valori tornati sono ridotti di
   -- quanto calcolato sul compenso origine in caso di modifica se coincide il riferimento all'anagrafico

   CNRCTB552.getRateizzaAddTerritorio(aRecCompenso.esercizio - 1,
                                      aRecAnagrafico.cd_anag,
                                      eseguiLock,
                                      aRecRateizzaClassificCoriC0,
                                      aRecRateizzaClassificCoriP0,
                                      aRecRateizzaClassificCoriR0);

   IF (aRecCompensoOri.cd_cds IS NOT NULL AND
       aRecCompenso.ti_anagrafico = 'A' AND
       aRecCompenso.ti_anagrafico = aRecCompensoOri.ti_anagrafico AND
       aRecAnagrafico.cd_anag = aRecAnagraficoOri.cd_anag) THEN
      CNRCTB552.modRateizzaAddTerritorio(aRecCompensoOri,
                                         '-',
                                         aRecRateizzaClassificCoriC0,
                                         aRecRateizzaClassificCoriP0,
                                         aRecRateizzaClassificCoriR0);
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Recupero degli accanti delle addizionali comunali. I valori tornati sono ridotti di
   -- quanto calcolato sul compenso origine in caso di modifica se coincide il riferimento all'anagrafico

   CNRCTB553.getAccontoAddTerritorio(aRecCompenso.esercizio,
                                      aRecAnagrafico.cd_anag,
                                      eseguiLock,
                                      aRecAccontoClassificCoriC0);

   IF (aRecCompensoOri.cd_cds IS NOT NULL AND
       aRecCompenso.ti_anagrafico = 'A' AND
       aRecCompenso.ti_anagrafico = aRecCompensoOri.ti_anagrafico AND
       aRecAnagrafico.cd_anag = aRecAnagraficoOri.cd_anag) THEN
      CNRCTB553.modAccontoAddTerritorio(aRecCompensoOri,
                                         '-',
                                         aRecAccontoClassificCoriC0);
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Recupero dati del tipo trattamento indicato nel compenso

   aRecTipoTrattamento:=CNRCTB545.getTipoTrattamento (aRecCompenso.cd_trattamento,
                                                      aRecCompenso.dt_registrazione);

   -------------------------------------------------------------------------------------------------
   -- Verifica ammissibilit�el compenso rispetto al tipo trattamento

   chkCompensoTipoTrattamento(aInserimentoModifica);

   -------------------------------------------------------------------------------------------------
   -- Verifica ammissibilit�el compenso rispetto al rapporto

   IF aOrigineCompenso!=CNRCTB545.isCompensoConguaglio THEN
      IF CNRCTB080.chkEsisteTerzoPerCompenso(aRecCompenso.cd_terzo,
                                             aRecCompenso.cd_tipo_rapporto,
                                             aRecCompenso.ti_anagrafico,
                                             aRecCompenso.dt_registrazione,
                                             aRecCompenso.dt_da_competenza_coge,
                                             aRecCompenso.dt_a_competenza_coge) = 0 THEN
         IBMERR001.RAISE_ERR_GENERICO('Terzo ' || aRecCompenso.cd_terzo || ' inesistente. ' ||
                                      'Il terzo e/o il rapporto ' || aRecCompenso.cd_tipo_rapporto ||
                                      'risultano cessati o non contigui alle date di riferimento del compenso');
      END IF;

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Attivazione del calcolo delle detrazioni familiari e personali

   attivaDetrazioni;

   -------------------------------------------------------------------------------------------------
   -- Costruzione e verifica della matrice per il calcolo compensi

   CostruisciTabellaCori;
   VerificaTabellaCori;

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione del numero di giorni di riferimento al compenso

   IF aOrigineCompenso!=CNRCTB545.isCompensoConguaglio THEN
      aRecCompenso.numero_giorni:=IBMUTL001.getDaysBetween(aRecCompenso.dt_da_competenza_coge,
                                                           aRecCompenso.dt_a_competenza_coge);
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione calcolo cori nella matrice di calcolo compensi

   IF aRecCompenso.ti_anagrafico = 'D' THEN
      calcolaCoriDipendente;
   ELSE
      calcolaCoriAltro(inCdsMcarriera,
                       inUoMcarriera,
                       inEsercizioMcarriera,
                       inPgMinicarriera);
   END IF;
   -------------------------------------------------------------------------------------------------
   -- Scrittura del dettaglio del compenso in calcolo

   scriviDettaglioCompenso;

   IF inIdPaeseEle is not null and inIdCodiceEle is not null and inIdentificativoSdiEle is not null and inProgressivoEle is not null then
       verificaCoerenzaFatturaEle(inIdPaeseEle,inIdCodiceEle,inIdentificativoSdiEle,inProgressivoEle);
   END IF;

END elaboraCompenso;


-- =================================================================================================
-- Lettura dati di base legati alla missione in calcolo; testata missione, anagrafico, base
-- inquadramento e quote esenti
-- =================================================================================================
PROCEDURE getDatiBaseCompenso
   (
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompenso COMPENSO.pg_compenso%TYPE,
    inCopiaCdsCompenso COMPENSO.cd_cds%TYPE,
    inCopiaUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inCopiaEsercizioCompenso COMPENSO.esercizio%TYPE,
    inCopiaPgCompenso COMPENSO.pg_compenso%TYPE,
    eseguiLock CHAR
   ) IS

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Azzeramento variabili

   aRecCompenso:=NULL;
   aRecCompensoOri:=NULL;
   aRecAnagrafico:=NULL;
   aRecAnagraficoOri:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Lettura compenso ed anagrafico

   -- Lettura compenso in calcolo

   aRecCompenso:=CNRCTB545.getCompenso (inCdsCompenso,
                                        inUoCompenso,
                                        inEsercizioCompenso,
                                        inPgCompenso,
                                        eseguiLock
                                       );

   -- Lettura compenso originale perima della modifica se sono in modifica

   IF inCopiaPgCompenso IS NOT NULL THEN
      aRecCompensoOri:=CNRCTB545.getCompenso (inCopiaCdsCompenso,
                                              inCopiaUoCompenso,
                                              inCopiaEsercizioCompenso,
                                              inCopiaPgCompenso,
                                              eseguiLock
                                             );
   END IF;

   -- Recupero dei dati dell'anagrafico associato al terzo del compenso

   aRecAnagrafico:=CNRCTB080.getAnag (aRecCompenso.cd_terzo);
   IF aRecCompensoOri.cd_cds IS NOT NULL THEN
      aRecAnagraficoOri:=CNRCTB080.getAnag (aRecCompensoOri.cd_terzo);
   END IF;

   -- Recupero configurazione deduzione IRPEF

   CNRCTB545.getConfigDeduzioneIrpef(inEsercizioCompenso,
                                     glbBaseDeduzione,
                                     glbQuotaFissaDeduzione,
                                     glbQuotaVariabileDeduzione);

   -- Recupero configurazione deduzione FAMILY

   CNRCTB545.getConfigDeduzioneFamily(inEsercizioCompenso,
                                      glbBaseDeduzioneFamily,
                                      glbDFamilyConiuge,
                                      glbDFamilyFiglio,
                                      glbDFamilyAltro,
                                      glbDFamilyFiglioMenoTre,
                                      glbDFamilyFiglioSenzaConiuge,
                                      glbDFamilyFiglioHandicap);

   -- Recupero della gestione no tax area per il soggetto anagrafico in elaborazione

   glbFlNoTaxArea:=CNRCTB080.getAnagFlNoTaxArea(inEsercizioCompenso,
                                                aRecAnagrafico.cd_anag);
   glbFlNoFamilyArea:=CNRCTB080.getAnagFlNoFamilyArea(inEsercizioCompenso,
                                                      aRecAnagrafico.cd_anag);

-- Recupero delle gestioni detrazioni per il soggetto anagrafico in elaborazione

   glbFlNoDetrazioniAltre:=CNRCTB080.getAnagFlNoDetrazioniAltre(inEsercizioCompenso,
                                                                aRecAnagrafico.cd_anag);
   glbFlNoDetrazioniFamily:=CNRCTB080.getAnagFlNoDetrazioniFamily(inEsercizioCompenso,
                                                                  aRecAnagrafico.cd_anag);
   glbFlDetrazioniAltriTipi:=CNRCTB080.getAnagFlDetrazioniAltriTipi(inEsercizioCompenso,
                                                                  aRecAnagrafico.cd_anag);
   glbFlNoCreditoIrpef:=CNRCTB080.getAnagFlNoCreditoIrpef(inEsercizioCompenso,
                                                          aRecAnagrafico.cd_anag);
   glbFlNoCreditoCuneoIrpef:=CNRCTB080.getAnagFlNoCreditoCuneoIrpef(inEsercizioCompenso,
                                                          aRecAnagrafico.cd_anag);
   glbFlNoDetrCuneoIrpef:=CNRCTB080.getAnagFlNoDetrCuneoIrpef(inEsercizioCompenso,
                                                          aRecAnagrafico.cd_anag);

   glbRedditoComplessivo:=cnrctb080.getAnagRedditoComplessivo(inEsercizioCompenso,
                                                              aRecAnagrafico.cd_anag);
   glbRedditoAbitazPrincipale:=cnrctb080.getAnagRedditoAbitazPrincipale(inEsercizioCompenso,
                                                              aRecAnagrafico.cd_anag);

   glbImponibilePagatoDip := cnrctb080.getImponibilePagatoDip(inEsercizioCompenso,
                                                    aRecAnagrafico.cd_anag);

   glbImponibilePagatoAltro := getImponibPagNonMcarrieraAltro(inEsercizioCompenso,
                                                           aRecCompenso.cd_terzo);
End getDatiBaseCompenso;


-- =================================================================================================
-- Valorizzazione della matrice per la memorizzazione dei montanti definiti per un soggetto anagrafico.
-- =================================================================================================
PROCEDURE costruisciTabMontanti
   (
    aRecMontanti MONTANTI%ROWTYPE
   ) IS
   i BINARY_INTEGER;
   aNomeDip VARCHAR2(100);
   aNomeAltro VARCHAR2(100);
   aMontanteLordoDip NUMBER(15,2);
   aMontanteDip NUMBER(15,2);
   aImDeduzioneDip NUMBER(15,2);
   aMontanteLordoAltro NUMBER(15,2);
   aMontanteAltro NUMBER(15,2);
   aImDeduzioneAltro NUMBER(15,2);
   aImDeduzioneFamilyAltro NUMBER(15,2);
   aMontanteLordoOcca NUMBER(15,2);
   aMontanteOcca NUMBER(15,2);
BEGIN

   -- Azzeramento matrice

   tabella_montanti.DELETE;

   -- Ciclo costruzione matrice

   FOR i IN 1 .. 9

   LOOP

      BEGIN

         IF    i = 1 THEN
               aNomeDip:='IRPEF_DIPENDENTI';
               aNomeAltro:='IRPEF_ALTRI';
               aMontanteLordoDip:=aRecMontanti.irpef_lordo_dipendenti;
               aMontanteDip:=aRecMontanti.irpef_dipendenti;
               aImDeduzioneDip:=aRecMontanti.deduzione_irpef_dipendenti;
               aMontanteLordoAltro:=aRecMontanti.irpef_lordo_altri;
               aMontanteAltro:=aRecMontanti.irpef_altri;
               aMontanteLordoOcca:=0;
               aMontanteOcca:=0;
               aImDeduzioneAltro:=aRecMontanti.deduzione_irpef_altri;
               aImDeduzioneFamilyAltro:=aRecMontanti.deduzione_family_altri;
         ELSIF i = 2 THEN
               aNomeDip:='INPS_DIPENDENTI';
               aNomeAltro:='INPS_ALTRI';
               aMontanteLordoDip:=aRecMontanti.inps_dipendenti;
               aMontanteDip:=aRecMontanti.inps_dipendenti;
               aImDeduzioneDip:=0;
               aMontanteLordoAltro:=aRecMontanti.inps_altri;
               aMontanteAltro:=aRecMontanti.inps_altri;
               aMontanteLordoOcca:=aRecMontanti.inps_occasionali;
               aMontanteOcca:=aRecMontanti.inps_occasionali;
               aImDeduzioneAltro:=0;
               aImDeduzioneFamilyAltro:=0;
         ELSIF i = 3 THEN
               aNomeDip:='INPS_TESORO_DIPENDENTI';
               aNomeAltro:=NULL;
               aMontanteLordoDip:=aRecMontanti.inps_tesoro_dipendenti;
               aMontanteDip:=aRecMontanti.inps_tesoro_dipendenti;
               aImDeduzioneDip:=0;
               aMontanteLordoAltro:=0;
               aMontanteAltro:=0;
               aMontanteLordoOcca:=0;
               aMontanteOcca:=0;
               aImDeduzioneAltro:=0;
               aImDeduzioneFamilyAltro:=0;
         ELSIF i = 4 THEN
               aNomeDip:=NULL;
               aNomeAltro:='INAIL_ALTRI';
               aMontanteLordoDip:=0;
               aMontanteDip:=0;
               aImDeduzioneDip:=0;
               aMontanteLordoAltro:=aRecMontanti.inail_altri;
               aMontanteAltro:=aRecMontanti.inail_altri;
               aMontanteLordoOcca:=0;
               aMontanteOcca:=0;
               aImDeduzioneAltro:=0;
               aImDeduzioneFamilyAltro:=0;
         ELSIF i = 5 THEN
               aNomeDip:='RIDUZ_DIPENDENTI';
               aNomeAltro:='RIDUZ_ALTRI';
               aMontanteLordoDip:=aRecMontanti.riduz_dipendenti;
               aMontanteDip:=aRecMontanti.riduz_dipendenti;
               aImDeduzioneDip:=0;
               aMontanteLordoAltro:=aRecMontanti.riduz_altri;
               aMontanteAltro:=aRecMontanti.riduz_altri;
               aMontanteLordoOcca:=0;
               aMontanteOcca:=0;
               aImDeduzioneAltro:=0;
               aImDeduzioneFamilyAltro:=0;
         ELSIF i = 6 THEN
               aNomeDip:='FONDO_FS_DIPENDENTI';
               aNomeAltro:=NULL;
               aMontanteLordoDip:=aRecMontanti.fondo_fs_dipendenti;
               aMontanteDip:=aRecMontanti.fondo_fs_dipendenti;
               aImDeduzioneDip:=0;
               aMontanteLordoAltro:=0;
               aMontanteAltro:=0;
               aMontanteLordoOcca:=0;
               aMontanteOcca:=0;
               aImDeduzioneAltro:=0;
               aImDeduzioneFamilyAltro:=0;
         ELSIF i = 7 THEN
               aNomeDip:='INPS_DIPENDENTI';
               aNomeAltro:='INPS_OCCASIONALI';
               aMontanteLordoDip:=aRecMontanti.inps_dipendenti;
               aMontanteDip:=aRecMontanti.inps_dipendenti;
               aImDeduzioneDip:=0;
               aMontanteLordoAltro:=aRecMontanti.inps_occasionali;
               aMontanteAltro:=aRecMontanti.inps_occasionali;
               aMontanteLordoOcca:=aRecMontanti.inps_altri;
               aMontanteOcca:=aRecMontanti.inps_altri;
               aImDeduzioneAltro:=0;
               aImDeduzioneFamilyAltro:=0;
         ELSIF i = 8 THEN
               aNomeDip:=null;
               aNomeAltro:='INPGI_ALTRI';
               aMontanteLordoDip:=0;
               aMontanteDip:=0;
               aImDeduzioneDip:=0;
               aMontanteLordoAltro:=aRecMontanti.inpgi_altri;
               aMontanteAltro:=aRecMontanti.inpgi_altri;
               aMontanteLordoOcca:=0;
               aMontanteOcca:=0;
               aImDeduzioneAltro:=0;
               aImDeduzioneFamilyAltro:=0;
          ELSIF i = 9 THEN
               aNomeDip:=null;
               aNomeAltro:='ENPAPI_ALTRI';
               aMontanteLordoDip:=0;
               aMontanteDip:=0;
               aImDeduzioneDip:=0;
               aMontanteLordoAltro:=aRecMontanti.enpapi_altri;
               aMontanteAltro:=aRecMontanti.enpapi_altri;
               aMontanteLordoOcca:=0;
               aMontanteOcca:=0;
               aImDeduzioneAltro:=0;
               aImDeduzioneFamilyAltro:=0;
         END IF;

         tabella_montanti(i).tNomeDip:=aNomeDip;
         tabella_montanti(i).tValoreLordoDip:=aMontanteLordoDip;
         tabella_montanti(i).tValoreNettoDip:=aMontanteDip;
         tabella_montanti(i).tImDeduzioneDip:=aImDeduzioneDip;
         tabella_montanti(i).tNomeAltro:=aNomeAltro;
         tabella_montanti(i).tValoreLordoAltro:=aMontanteLordoAltro;
         tabella_montanti(i).tValoreNettoAltro:=aMontanteAltro;
         tabella_montanti(i).tValoreLordoOcca:=aMontanteLordoOcca;
         tabella_montanti(i).tValoreNettoOcca:=aMontanteOcca;
         tabella_montanti(i).tImDeduzioneAltro:=aImDeduzioneAltro;
         tabella_montanti(i).tImDeduzioneFamilyAltro:=aImDeduzioneFamilyAltro;

      END;

   END LOOP;

END costruisciTabMontanti;

-- =================================================================================================
-- I valori dei montanti sono ridotti di quanto calcolato dal compenso origine in caso di modifica se
-- coincidono il riferimento all'anagrafico e quello del tipo compenso dipendente o altro
-- =================================================================================================
PROCEDURE modificaTabMontanti
   (
    aRecCompenso COMPENSO%ROWTYPE,
    segno CHAR
   ) IS
   cvCdContributoRitenuta CONTRIBUTO_RITENUTA.cd_contributo_ritenuta%TYPE;
   cvImponibileLordo CONTRIBUTO_RITENUTA.imponibile_lordo%TYPE;
   cvImponibile CONTRIBUTO_RITENUTA.imponibile%TYPE;
   cvImDeduzioneIrpef CONTRIBUTO_RITENUTA.im_deduzione_irpef%TYPE;
   cvImDeduzioneFamily CONTRIBUTO_RITENUTA.im_deduzione_family%TYPE;
   cvPgClassificazioneMontanti TIPO_CONTRIBUTO_RITENUTA.pg_classificazione_montanti%TYPE;
   aRecParametriCNR PARAMETRI_CNR%ROWTYPE;
   gen_cur GenericCurTyp;
   cvImponibileOcca CONTRIBUTO_RITENUTA.imponibile%TYPE;
   cvImponibileLordoOcca CONTRIBUTO_RITENUTA.imponibile_lordo%TYPE;
BEGIN
   aRecParametriCNR := getParametriCNR(aRecCompenso.Esercizio);
   -- Distinguo se �ipendente o altro
   If aRecCompenso.ti_anagrafico = 'A' then
      --cvImponibileOcca := aRecCompenso.im_lordo_percipiente - aRecCompenso.quota_esente_inps;
      --cvImponibileLordoOcca := aRecCompenso.im_lordo_percipiente - aRecCompenso.quota_esente_inps;
      --per gestire il fatto che se ho una quota esente cori (es. bollo) devo escluderla dal
      --montante occa e non posso inserirla nuovamente nella quota esente inps (che gi�e lo escluderebbe)
      --perch�ltrimenti l'imponibile inps la decurta due volte
      cvImponibileOcca := aRecCompenso.im_lordo_percipiente - aRecCompenso.quota_esente_inps - aRecCompenso.quota_esente;
      cvImponibileLordoOcca := aRecCompenso.im_lordo_percipiente - aRecCompenso.quota_esente_inps - aRecCompenso.quota_esente;
      BEGIN

         OPEN gen_cur FOR

              SELECT DISTINCT A.cd_contributo_ritenuta,
                     A.imponibile_lordo,
                     A.imponibile,
                     A.im_deduzione_irpef,
                     A.im_deduzione_family,
                     B.pg_classificazione_montanti
              FROM   CONTRIBUTO_RITENUTA A,
                     TIPO_CONTRIBUTO_RITENUTA B
              WHERE  A.cd_cds = aRecCompenso.cd_cds AND
                     A.cd_unita_organizzativa = aRecCompenso.cd_unita_organizzativa AND
                     A.esercizio = aRecCompenso.esercizio AND
                     A.pg_compenso = aRecCompenso.pg_compenso AND
                     A.ti_ente_percipiente = 'P' AND
                     B.cd_contributo_ritenuta = A.cd_contributo_ritenuta AND
                     B.dt_ini_validita = A.dt_ini_validita AND
                     B.pg_classificazione_montanti IS NOT NULL AND
                     B.fl_scrivi_montanti = 'Y';

         LOOP

            FETCH gen_cur INTO
                  cvCdContributoRitenuta,
                  cvImponibileLordo,
                  cvImponibile,
                  cvImDeduzioneIrpef,
                  cvImDeduzioneFamily,
                  cvPgClassificazioneMontanti;

            EXIT WHEN gen_cur%NOTFOUND;
            IF segno = '-' THEN
               cvImponibileLordo:=cvImponibileLordo * -1;
               cvImponibile:=cvImponibile * -1;
               cvImDeduzioneIrpef:=cvImDeduzioneIrpef * -1;
               cvImDeduzioneFamily:=cvImDeduzioneFamily * -1;
               cvImponibileLordoOcca:=cvImponibileLordoOcca * -1;
               cvImponibileOcca:=cvImponibileOcca * -1;
            END IF;
            --send_message('aRecCompenso.im_lordo_percipiente '||aRecCompenso.im_lordo_percipiente);
            If aRecCompenso.cd_tipo_rapporto = aRecParametriCNR.Cd_Tipo_Rapporto Then
              tabella_montanti(cvPgClassificazioneMontanti).tValoreLordoAltro:=
                             tabella_montanti(cvPgClassificazioneMontanti).tValoreLordoAltro +
                               cvImponibileLordoOcca;
              tabella_montanti(cvPgClassificazioneMontanti).tValoreNettoAltro:=
                             tabella_montanti(cvPgClassificazioneMontanti).tValoreNettoAltro +
                               cvImponibileOcca;
              tabella_montanti(cvPgClassificazioneMontanti).tImDeduzioneAltro:=
                             tabella_montanti(cvPgClassificazioneMontanti).tImDeduzioneAltro + cvImDeduzioneIrpef;
              tabella_montanti(cvPgClassificazioneMontanti).tImDeduzioneFamilyAltro:=
                             tabella_montanti(cvPgClassificazioneMontanti).tImDeduzioneFamilyAltro + cvImDeduzioneFamily;
            Else
              tabella_montanti(cvPgClassificazioneMontanti).tValoreLordoAltro:=
                             tabella_montanti(cvPgClassificazioneMontanti).tValoreLordoAltro + cvImponibileLordo;
              tabella_montanti(cvPgClassificazioneMontanti).tValoreNettoAltro:=
                             tabella_montanti(cvPgClassificazioneMontanti).tValoreNettoAltro + cvImponibile;
              tabella_montanti(cvPgClassificazioneMontanti).tImDeduzioneAltro:=
                             tabella_montanti(cvPgClassificazioneMontanti).tImDeduzioneAltro + cvImDeduzioneIrpef;
              tabella_montanti(cvPgClassificazioneMontanti).tImDeduzioneFamilyAltro:=
                             tabella_montanti(cvPgClassificazioneMontanti).tImDeduzioneFamilyAltro + cvImDeduzioneFamily;
            End If;
         END LOOP;

         CLOSE gen_cur;
      END;
   Else --aRecCompenso.ti_anagrafico = 'D'
        -- per i dip aggiorno solo la riduzione erariale
--pipe.send_message('entrata in modificaTabMontanti D');

      BEGIN
         OPEN gen_cur FOR

              SELECT DISTINCT A.cd_contributo_ritenuta,
                     A.imponibile_lordo,
                     A.imponibile,
                     B.pg_classificazione_montanti
              FROM   CONTRIBUTO_RITENUTA A,
                     TIPO_CONTRIBUTO_RITENUTA B
              WHERE  A.cd_cds = aRecCompenso.cd_cds AND
                     A.cd_unita_organizzativa = aRecCompenso.cd_unita_organizzativa AND
                     A.esercizio = aRecCompenso.esercizio AND
                     A.pg_compenso = aRecCompenso.pg_compenso AND
                     A.ti_ente_percipiente = 'P' AND
                     B.cd_contributo_ritenuta = A.cd_contributo_ritenuta AND
                     B.dt_ini_validita = A.dt_ini_validita AND
                     B.pg_classificazione_montanti IS NOT NULL AND
                     B.fl_scrivi_montanti = 'Y' AND
                     B.pg_classificazione_montanti in('3', '5');
         LOOP

            FETCH gen_cur INTO
                  cvCdContributoRitenuta,
                  cvImponibileLordo,
                  cvImponibile,
                  cvPgClassificazioneMontanti;

            EXIT WHEN gen_cur%NOTFOUND;
            IF segno = '-' THEN
               cvImponibileLordo:=cvImponibileLordo * -1;
               cvImponibile:=cvImponibile * -1;
            END IF;
            --send_message('aRecCompenso.im_lordo_percipiente '||aRecCompenso.im_lordo_percipiente);
--pipe.send_message('tabella_montanti(cvPgClassificazioneMontanti).tValoreNettoAltro ='||tabella_montanti(cvPgClassificazioneMontanti).tValoreNettoAltro );
--pipe.send_message('cvImponibile ='||cvImponibile );
            tabella_montanti(cvPgClassificazioneMontanti).tValoreLordoDip:=
                             tabella_montanti(cvPgClassificazioneMontanti).tValoreLordoDip + cvImponibileLordo;
            tabella_montanti(cvPgClassificazioneMontanti).tValoreNettoDip:=
                             tabella_montanti(cvPgClassificazioneMontanti).tValoreNettoDip + cvImponibile;
         END LOOP;

         CLOSE gen_cur;
      END;
   End if;

   RETURN;

END modificaTabMontanti;

-- =================================================================================================
-- Verifica ammissibilit�el compenso rispetto al tipo trattamento
-- =================================================================================================
PROCEDURE chkCompensoTipoTrattamento
   (
    aInserimentoModifica CHAR
   ) IS

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Gestione differenziata per inserimento e modifica

   IF aInserimentoModifica = 'I' THEN

      -- Il compenso �enerato da conguaglio

      IF    aRecCompenso.fl_compenso_conguaglio = 'Y' THEN
            aOrigineCompenso:=CNRCTB545.isCompensoConguaglio;

      -- Il compenso �n senza calcoli

      ELSIF aRecTipoTrattamento.fl_senza_calcoli = 'Y' THEN
            aOrigineCompenso:=CNRCTB545.isCompensoSenzaCalcoli;

      -- Il compenso �enerato da missione

      ELSIF aRecCompenso.fl_diaria = 'Y' THEN
            aOrigineCompenso:=CNRCTB545.isCompensoMissione;

      -- Il compenso �enerato da minicarriera

      ELSIF aRecCompenso.fl_compenso_minicarriera = 'Y' THEN
            aOrigineCompenso:=CNRCTB545.isCompensoMinicarriera;

      ELSE
            aOrigineCompenso:=CNRCTB545.isCompensoNormale;
      END IF;

   ELSE

      aOrigineCompenso:=CNRCTB545.getTipoOrigineCompenso(aRecCompenso.cd_cds,
                                                         aRecCompenso.cd_unita_organizzativa,
                                                         aRecCompenso.esercizio,
                                                         aRecCompenso.pg_compenso);
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Controlli

   -- Compenso da conguaglio

   IF    aOrigineCompenso=CNRCTB545.isCompensoConguaglio THEN

         IF aRecTipoTrattamento.fl_default_conguaglio = 'N' THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Il trattamento indicato per un compenso da conguaglio deve avere l''attributo ' ||
                'fl_default_conguaglio valorizzato a Y');
         END IF;

         IF aRecTipoTrattamento.fl_senza_calcoli = 'Y' THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Il trattamento indicato per un compenso da conguaglio non pu�evedere la gestione senza calcoli');
         END IF;

         IF aInserimentoModifica = 'M' THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Non �ossibile l''attivazione della modifica su di un compenso da conguaglio');
         END IF;

   -- Compenso da missione

   ELSIF aOrigineCompenso=CNRCTB545.isCompensoMissione THEN

         IF (aRecTipoTrattamento.fl_diaria = 'N' OR
             aRecCompenso.fl_diaria = 'N') THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Un compenso da missione deve presentare, anche nel trattamento associato, l''attributo ' ||
                'fl_diaria valorizzato a Y');
         END IF;

         IF aRecTipoTrattamento.fl_senza_calcoli = 'Y' THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Il trattamento indicato per un compenso da missione non deve prevedere la gestione senza calcoli');
         END IF;

   -- Compenso da minicarriera

   ELSIF aOrigineCompenso=CNRCTB545.isCompensoMinicarriera THEN

         IF aRecTipoTrattamento.fl_senza_calcoli = 'Y' THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Il trattamento indicato per un compenso da minicarriera non deve prevedere la gestione senza calcoli');
         END IF;

   /* Deve essere consentita la modifica per il ricalcolo dell'acconto add com
         IF aInserimentoModifica = 'M' THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Non �ossibile l''attivazione della modifica su di un compenso da minicarriera');
         END IF; */

   -- Compenso senza calcoli

   ELSIF aOrigineCompenso=CNRCTB545.isCompensoSenzaCalcoli THEN

         IF (aRecTipoTrattamento.fl_senza_calcoli = 'N' OR
             aRecCompenso.fl_senza_calcoli = 'N') THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Un compenso senza calcoli deve presentare, anche nel trattamento associato, l''attributo ' ||
                'fl_senza_calcoli valorizzato a Y');
         END IF;

   END IF;

END chkCompensoTipoTrattamento;


-- =================================================================================================
-- Azzeramento delle detrazioni familiari e personali
-- =================================================================================================
PROCEDURE attivaDetrazioni
   IS

BEGIN

   -- Per compenso senza calcolo leggo quanto valorizzato dall'utente e non attivo il calcolo delle
   -- detrazioni
   IF aRecTipoTrattamento.fl_senza_calcoli = 'Y' THEN
      calcolaDetrazFamiliari:='N';
      calcolaDetrazPersonali:='N';
      calcolaDetrazAltriTipi:='N';
      aImDetrazioniPe:=aRecCompenso.detrazioni_personali;
      aImDetrazioniLa:=aRecCompenso.detrazioni_la;
      aImDetrazioniCo:=aRecCompenso.detrazione_coniuge;
      aImDetrazioniFi:=aRecCompenso.detrazione_figli;
      aImDetrazioniAl:=aRecCompenso.detrazione_altri;
      aImDetrazioniRidCuneo:=aRecCompenso.detrazione_riduzione_cuneo;
      aImDetrazioniPeNetto:=aRecCompenso.detrazioni_personali_netto;
      aImDetrazioniLaNetto:=aRecCompenso.detrazioni_la_netto;
      aImDetrazioniCoNetto:=aRecCompenso.detrazione_coniuge_netto;
      aImDetrazioniFiNetto:=aRecCompenso.detrazione_figli_netto;
      aImDetrazioniAlNetto:=aRecCompenso.detrazione_altri_netto;
      aImDetrazioniRidCuneoNetto:=aRecCompenso.detrazione_rid_cuneo_netto;
      RETURN;
   END IF;

   -- In tutti gli altri casi azzero le variabili che memorizzano le detrazioni

   calcolaDetrazFamiliari:='N';
   calcolaDetrazPersonali:='N';
   calcolaDetrazAltriTipi:='N';
   aImDetrazioniPe:=0;
   aImDetrazioniLa:=0;
   aImDetrazioniCo:=0;
   aImDetrazioniFi:=0;
   aImDetrazioniAl:=0;
   aImDetrazioniRidCuneo := 0;
   aImDetrazioniPeNetto:=0;
   aImDetrazioniLaNetto:=0;
   aImDetrazioniCoNetto:=0;
   aImDetrazioniFiNetto:=0;
   aImDetrazioniAlNetto:=0;
   aImDetrazioniRidCuneoNetto := 0;

   -- Se si tratta di compenso da conguaglio non attivo le detrazione che sono calcolate dal
   -- conguaglio stesso
   IF aOrigineCompenso=CNRCTB545.isCompensoConguaglio THEN
      RETURN;
   END IF;

   -- Attivo il calcolo delle detrazioni solo se il compenso �a minicarriera
   IF aOrigineCompenso!=CNRCTB545.isCompensoMinicarriera THEN
      RETURN;
   END IF;

   -- Attivo il calcolo delle detrazioni solo se il compenso non �egistrato a soggetti dipendenti e il
   -- trattamento ne prevede la gestione

   IF aRecCompenso.ti_anagrafico = 'A' THEN
      IF aRecTipoTrattamento.fl_detrazioni_familiari = 'Y' THEN
         calcolaDetrazFamiliari:='Y';
      END IF;
      IF aRecTipoTrattamento.fl_detrazioni_dipendente = 'Y' THEN
         calcolaDetrazPersonali:='Y';
      Else
         If aRecTipoTrattamento.fl_detrazioni_altre = 'Y' Then
             calcolaDetrazAltriTipi:='Y';
         End If;
      END IF;
   END IF;

  -- Attivo il calcolo delle detrazioni solo se nei parametri ne �revista la gestione

END attivaDetrazioni;

-- =================================================================================================
-- Valorizzazione della matrice per il calcolo dei dettagli CORI di un compenso
-- =================================================================================================
PROCEDURE costruisciTabellaCori
   IS
   i BINARY_INTEGER;
   aRecVTrattamento V_TIPO_TRATTAMENTO_TIPO_CORI%ROWTYPE;
   gen_cur GenericCurTyp;

BEGIN

   -- Valorizzazione variabili

   tabella_cori.DELETE;
   tabella_cori_det.DELETE;
   i:=0;

   BEGIN

      -- Estrazione dei contributi ritenuta associati al tipo trattamento letto dal compenso
      -- Lettura storica dalla vista in base alla data di registrazione del compenso

      OPEN gen_cur FOR

           SELECT *
           FROM   V_TIPO_TRATTAMENTO_TIPO_CORI
           WHERE  cd_trattamento = aRecTipoTrattamento.cd_trattamento AND
                  dt_ini_val_trattamento = aRecTipoTrattamento.dt_ini_validita AND
                  dt_fin_val_trattamento = aRecTipoTrattamento.dt_fin_validita AND
                  ((TI_CASSA_COMPETENZA = 'CA' AND
                  dt_ini_val_tratt_cori <= aRecCompenso.dt_registrazione AND
                  dt_fin_val_tratt_cori >= aRecCompenso.dt_registrazione AND
                  dt_ini_val_tipo_cori <= aRecCompenso.dt_registrazione AND
                  dt_fin_val_tipo_cori >= aRecCompenso.dt_registrazione ) OR
                  (TI_CASSA_COMPETENZA = 'CO' AND
                   (dt_ini_val_tratt_cori <= aRecCompenso.dt_da_competenza_coge AND
                   dt_fin_val_tratt_cori >= aRecCompenso.dt_a_competenza_coge AND
                   dt_ini_val_tipo_cori <= aRecCompenso.dt_da_competenza_coge AND
                   dt_fin_val_tipo_cori >= aRecCompenso.dt_a_competenza_coge ) or
                   (aOrigineCompenso=CNRCTB545.isCompensoConguaglio AND FL_CREDITO_IRPEF = 'Y' and
                    ((aRecCompenso.dt_da_competenza_coge between dt_ini_val_tratt_cori  and dt_fin_val_tratt_cori ) or
                     (aRecCompenso.dt_a_competenza_coge between dt_ini_val_tratt_cori  and dt_fin_val_tratt_cori )) and
                    ((aRecCompenso.dt_da_competenza_coge between dt_ini_val_tipo_cori  and dt_fin_val_tipo_cori ) or
                     (aRecCompenso.dt_a_competenza_coge between dt_ini_val_tipo_cori  and dt_fin_val_tipo_cori )))))
           ORDER BY cd_trattamento,
                    id_riga;

      LOOP

         FETCH gen_cur INTO aRecVTrattamento;

         EXIT WHEN gen_cur%NOTFOUND;
             i:=i + 1;

             tabella_cori(i).tCdCori:=aRecVTrattamento.cd_cori;
             tabella_cori(i).tDtIniValCori:=aRecVTrattamento.dt_ini_val_tipo_cori;
             tabella_cori(i).tTiCassaCompetenza:=aRecVTrattamento.ti_cassa_competenza;
             tabella_cori(i).tPrecisione:=aRecVTrattamento.precisione;
             tabella_cori(i).tPgClassificazioneMontanti:=aRecVTrattamento.pg_classificazione_montanti;
             tabella_cori(i).tCdClassificazioneCori:=aRecVTrattamento.cd_classificazione_cori;
             tabella_cori(i).tFlScriviMontanti:=aRecVTrattamento.fl_scrivi_montanti;
             tabella_cori(i).tIdRiga:=aRecVTrattamento.id_riga;
             tabella_cori(i).tSegno:=aRecVTrattamento.segno;
             tabella_cori(i).tCalcoloImponibile:=aRecVTrattamento.calcolo_imponibile;
             tabella_cori(i).tFlSospensioneIrpef:=aRecVTrattamento.fl_sospensione_irpef;
             tabella_cori(i).tFlCreditoIrpef:=aRecVTrattamento.fl_credito_irpef;
             tabella_cori(i).tMontante:=NULL;
             tabella_cori(i).tImponibileLordo:=NULL;
             tabella_cori(i).tImDeduzioneIrpef:=NULL;
             tabella_cori(i).tImDeduzioneFamily:=NULL;
             tabella_cori(i).tImponibileNetto:=NULL;
             tabella_cori(i).tAliquotaEnte:=NULL;
             tabella_cori(i).tBaseCalcoloEnte:=NULL;
             tabella_cori(i).tAmmontareEnteLordo:=NULL;
             tabella_cori(i).tAmmontareEnte:=NULL;
             tabella_cori(i).tAliquotaPercip:=NULL;
             tabella_cori(i).tBaseCalcoloPercip:=NULL;
             tabella_cori(i).tAmmontarePercipLordo:=NULL;
             tabella_cori(i).tAmmontarePercip:=NULL;
      END LOOP;
      -- close cursore

      CLOSE gen_cur;

   END;

END costruisciTabellaCori;

-- =================================================================================================
-- Verifica consistenza dei dati della matrice per il calcolo dei dettagli CORI di un compenso
-- =================================================================================================
PROCEDURE verificaTabellaCori
   IS
   i BINARY_INTEGER;
   conta NUMBER;
   aNumeroBase NUMBER(15,2);
   aNumeroIntero NUMBER(15,2);
   aBloccoCalcolo VARCHAR2(5);

BEGIN

   conta:=tabella_cori.COUNT;

   -- Errore in estrazione specifiche dei cori definiti per il trattamento in esame

   IF conta = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO
                   ('Nessun record valido di specifica contributi e ritenute per il trattamento ' ||
                    aRecTipoTrattamento.cd_trattamento);
   END IF;

   -- Errore nella definizione delle regole di calcolo

   FOR i IN tabella_cori.FIRST .. tabella_cori.LAST

   LOOP

      -- Errata sequenza tra elementi in matrice e id riga del calcolo

      IF LPAD(i,3,0) != tabella_cori(i).tIdRiga and tabella_cori(i).tFlCreditoIrpef != 'Y' THEN
         IBMERR001.RAISE_ERR_GENERICO
                      ('Errore in sequenza dell''algoritmo di calcolo definito in TRATTAMENTO_CORI ' ||
                       CHR(10) || 'Sequenza ' || LPAD(i,3,0) || ' Id riga ' || tabella_cori(i).tIdRiga);
      END IF;

      -- Errata valorizzazione delle formule di calcolo

      IF tabella_cori(i).tCalcoloImponibile != '000' THEN

         -- Errata dimensione della formula di calcolo

         aNumeroBase:=(LENGTH(tabella_cori(i).tCalcoloImponibile) / 5);
         aNumeroIntero:=TRUNC(aNumeroBase);
         IF aNumeroBase != aNumeroIntero THEN
            IBMERR001.RAISE_ERR_GENERICO
                         ('Errore di dimensione (non divisibile per 5) della formula di calcolo in TRATTAMENTO_CORI ' ||
                          CHR(10) || 'id riga ' || tabella_cori(i).tIdRiga);
         END IF;

         FOR j IN 1 .. (LENGTH(tabella_cori(i).tCalcoloImponibile) / 5)

         LOOP

            aBloccoCalcolo:=SUBSTR(tabella_cori(i).tCalcoloImponibile, ((j * 5) - 5 + 1), 5);

            -- Segno non correttamente definito

            IF  (SUBSTR(aBloccoCalcolo,1,1) != '+' AND
                 SUBSTR(aBloccoCalcolo,1,1) != '-') THEN
                IBMERR001.RAISE_ERR_GENERICO
                             ('Errore in valorizzazione del segno nella formula di calcolo in TRATTAMENTO_CORI ' ||
                              CHR(10) || 'id riga ' || tabella_cori(i).tIdRiga);
            END IF;

            -- Riferimento al valore ente, percepiente o entrambi non correttamente definito

            IF  (SUBSTR(aBloccoCalcolo,5,1) != 'E' AND
                 SUBSTR(aBloccoCalcolo,5,1) != 'P' AND
                 SUBSTR(aBloccoCalcolo,5,1) != '*' And
                 SUBSTR(aBloccoCalcolo,5,1) != '%') THEN
                IBMERR001.RAISE_ERR_GENERICO
                             ('Errore in valorizzazione del riferimento a ente, percepiente o entrambi nella ' ||
                              'formula di calcolo in TRATTAMENTO_CORI ' ||
                              CHR(10) || 'id riga ' || tabella_cori(i).tIdRiga);
            END IF;

            -- Errato puntatore id riga della formula

      If SUBSTR(aBloccoCalcolo,5,1) != '%' Then
              IF (LPAD(conta,3,0) < SUBSTR(aBloccoCalcolo,2,3) OR
                  tabella_cori(i).tIdRiga <= SUBSTR(aBloccoCalcolo,2,3)) THEN
                  IBMERR001.RAISE_ERR_GENERICO
                               ('Errore in valorizzazione del riferimento id riga nella ' ||
                                'formula di calcolo in TRATTAMENTO_CORI ' ||
                                CHR(10) || 'id riga ' || tabella_cori(i).tIdRiga ||
                                ' formula ' || SUBSTR(aBloccoCalcolo,2,3));
              END IF;
      End If;
         END LOOP;

      END IF;

   END LOOP;
END verificaTabellaCori;

-- =================================================================================================
-- Calcolo del singolo contributo/ritenuta
-- =================================================================================================
PROCEDURE calcolaCoriDipendente
   IS
   i BINARY_INTEGER;
   aBloccoCalcolo VARCHAR2(5);
   aImponibileLordo NUMBER(15,2);
   aImponibileNetto NUMBER(15,2);
   aMontanteLordo NUMBER(15,2);
   aMontanteNetto NUMBER(15,2);
   aImDeduzione NUMBER(15,2);
   aCdRegione COMPENSO.cd_regione_add%TYPE;
   aCdProvincia COMPENSO.cd_provincia_add%TYPE;
   aPgComune COMPENSO.pg_comune_add%TYPE;
   aAliquotaIrpefAnag SCAGLIONE.aliquota%TYPE;
   isTassazioneSeparata CHAR(1);

   aImportoMaxRifScaglione NUMBER(15,2);
   aImponibileBlocco NUMBER(15,2);
   resto NUMBER(15,2);
   aTiEntePercip SCAGLIONE.ti_ente_percipiente%TYPE;

   aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE;
   aRecTmpScaglioneImponibile V_SCAGLIONE%ROWTYPE;
   aRecScaglioneImponibile V_PRE_SCAGLIONE%ROWTYPE;

   gen_cur_a GenericCurTyp;

BEGIN

   -- Nella gestione per dipendenti non si calcola mai la deduzione per i CORI IRPEF a scaglione
   -- Si predispone comunque il sistema per la valorizzazione corretta delle variabili

   aImponibileLordo:=0;
   aImponibileNetto:=0;
   aMontanteLordo:=0;
   aMontanteNetto:=0;
   aImDeduzione:=0;
   aAliquotaIrpefAnag:=0;

   BEGIN

      FOR i IN tabella_cori.FIRST .. tabella_cori.LAST

      LOOP

         -- Valorizzazione dei parametri di regione, provincia e comune

         getDatiTerritorio (aCdRegione,
                            aCdProvincia,
                            aPgComune,
                            i);

         -- Determino il valore dell'imponibile per ogni singolo cori in trattamento

         aImponibileLordo:=getImponibileCori(i);

         -- Prima veniva fatto in getImponibileCori ma �tato spostato per gestire la quota esente inps per i non dip
         -- Eseguo la normalizzazone dell'imponibile in base alla precisione indicata nel trattamento.
         -- Prendo in considerazione solo la precisione pari a 1.
         IF (tabella_cori(i).tPrecisione IS NOT NULL AND
             tabella_cori(i).tPrecisione = 1) THEN
             aImponibileLordo:=ROUND(aImponibileLordo);
         END IF;

         aImponibileNetto:=aImponibileLordo;

         -- Determino il montante di riferimento. Per i dipendenti si usa sempre il valore presente
         -- nei montanti che sono statici. Si recuperano sia il montante lordo che quello netto

          getImportoMontanteDip(tabella_cori(i).tPgClassificazioneMontanti,
                               aMontanteLordo,
                               aMontanteNetto,
                               aImDeduzione);

         -- Controllo se si tratta di compenso da minicarriera gestito a tassazione separata

         isTassazioneSeparata:='N';

         IF (tabella_cori(i).tPgClassificazioneMontanti IS NOT NULL AND
                (
                 CNRCTB545.getIsIrpefScaglioni(tabella_cori(i).tCdClassificazioneCori,
                                               tabella_cori(i).tPgClassificazioneMontanti,
                                               tabella_cori(i).tFlScriviMontanti) = 'Y'
                )
            ) THEN

            IF (aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
                aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong) THEN
               IF aRecCompenso.fl_compenso_mcarriera_tassep = 'Y' THEN
                  IF aRecCompenso.aliquota_irpef_tassep = 0 THEN
                     IBMERR001.RAISE_ERR_GENERICO
                        ('Prima di procedere al calcolo di un compenso derivante da minicarrera a tassazione separata ' ||
                         'deve essere stata calcolata l''aliquota media sulla minicarriera stessa');
                  END IF;
                  isTassazioneSeparata:='Y';
               END IF;
            END IF;

         END IF;

         -- Lettura degli scaglioni uso come valore minimo e massimo di riferimento quanto presente
         -- sulla tabella MONTANTI. Si usa per convenzione sempre il valore netto
         aRecVPreScaglione:=CNRCTB545.getScaglione(tabella_cori(i).tCdCori,
                                                   aRecTipoTrattamento.ti_anagrafico,
                                                   aRecCompenso.dt_registrazione,
                                                   aMontanteNetto,
                                                   aAliquotaIrpefAnag,
                                                   aCdRegione,
                                                   aCdProvincia,
                                                   aPgComune);

         aImportoMaxRifScaglione:=aMontanteNetto + aImponibileLordo;

         -- Solo per la RIDUZIONE ERARIALE �revista l'operativit�u pi� scaglioni
         -- In tutti gli altri casi �oluto che si prende lo scaglione massimo
         IF tabella_cori(i).tPgClassificazioneMontanti is null or
            tabella_cori(i).tPgClassificazioneMontanti not in('3', '5') or
            (tabella_cori(i).tPgClassificazioneMontanti in('3', '5') and
              aRecVPreScaglione.im_superiore >= aImportoMaxRifScaglione)  Then
--pipe.send_message('IF = '||tabella_cori(i).tCdCori);
             -------------------------------------------------------------------------------------------
             -- Aggiornamento matrice di calcolo cori.
             -- Valorizzazione di imponibile e montante. Imponibile �l vero valore dell'imponibile mentre
             -- montante �uello utilizzato per il recupero dell'aliquota dagli scaglioni
             tabella_cori(i).tImponibileLordo:=aImponibileLordo;
             tabella_cori(i).tImponibileNetto:=aImponibileNetto;
             tabella_cori(i).tImDeduzioneIrpef:=aImDeduzione;
             tabella_cori(i).tImDeduzioneFamily:=0;
             IF aMontanteNetto = 0 THEN
                tabella_cori(i).tMontante:=aImponibileNetto;
             ELSE
                tabella_cori(i).tMontante:=aMontanteNetto;
             END IF;

             -- Rimanenti informazioni

             IF (aRecVPreScaglione.ti_ente_percipiente = 'P'  OR
                 aRecVPreScaglione.ti_ente_percipiente = '*') THEN
                IF aRecCompenso.fl_senza_calcoli = 'Y' THEN
                   tabella_cori(i).tAliquotaPercip:=0;
                   tabella_cori(i).tBaseCalcoloPercip:=0;
                   tabella_cori(i).tAmmontarePercipLordo:=0;
                   tabella_cori(i).tAmmontarePercip:=0;
                ELSE
                   IF isTassazioneSeparata = 'Y' THEN
                      tabella_cori(i).tAliquotaPercip:=aRecCompenso.aliquota_irpef_tassep;
                   ELSE
                      tabella_cori(i).tAliquotaPercip:=aRecVPreScaglione.aliquota_percip;
                   END IF;
                   tabella_cori(i).tBaseCalcoloPercip:=aRecVPreScaglione.base_calcolo_percip;
                   tabella_cori(i).tAmmontarePercipLordo:=ROUND(aImponibileNetto * (aRecVPreScaglione.base_calcolo_percip / 100) *
                                                                (tabella_cori(i).tAliquotaPercip / 100),2);
                   tabella_cori(i).tAmmontarePercip:=tabella_cori(i).tAmmontarePercipLordo;
                END IF;
             ELSE
                IF (aRecVPreScaglione.ti_ente_percipiente = 'E'  OR
                    aRecVPreScaglione.ti_ente_percipiente = '*') THEN
                   IF aRecCompenso.fl_senza_calcoli = 'Y' THEN
                      tabella_cori(i).tAliquotaEnte:=0;
                      tabella_cori(i).tBaseCalcoloEnte:=0;
                      tabella_cori(i).tAmmontareEnteLordo:=0;
                      tabella_cori(i).tAmmontareEnte:=0;
                   ELSE
                      tabella_cori(i).tAliquotaEnte:=aRecVPreScaglione.aliquota_ente;
                      tabella_cori(i).tBaseCalcoloEnte:=aRecVPreScaglione.base_calcolo_ente;
                      tabella_cori(i).tAmmontareEnteLordo:=ROUND(aImponibileNetto * (aRecVPreScaglione.base_calcolo_ente / 100) *
                                                                 (aRecVPreScaglione.aliquota_ente / 100),2);
                      tabella_cori(i).tAmmontareEnte:=tabella_cori(i).tAmmontareEnteLordo;
                   END IF;
                END IF;
             END IF;
         ELSE -- Solo per la RIDUZIONE ERARIALE �revista l'operativit�u pi� scaglioni
              -- entro SOLO SE tabella_cori(i).tPgClassificazioneMontanti = '5' and aRecVPreScaglione.im_superiore < aImportoMaxRifScaglione
--pipe.send_message('ELSE = '||tabella_cori(i).tCdCori);
            -- scrivo l'importo del primo scaglione. Si distinguono gli importi di avvio in base al fatto
            -- che il cori in elaborazione �nnualizzato o meno
            aImponibileBlocco:=aRecVPreScaglione.im_superiore - aMontanteNetto;
            resto:=aImponibileNetto - aImponibileBlocco;

            costruisciTabellaCoriDet(i,
                                     aImponibileBlocco,
                                     aRecVPreScaglione);
            BEGIN
               OPEN gen_cur_a FOR
                    SELECT *
                    FROM   V_SCAGLIONE
                    WHERE  cd_contributo_ritenuta = tabella_cori(i).tCdCori AND
                           (ti_anagrafico = aRecCompenso.ti_anagrafico OR
                            ti_anagrafico = '*' ) AND
                           dt_inizio_validita <=aRecCompenso.dt_registrazione AND
                           dt_fine_validita >= aRecCompenso.dt_registrazione AND
                           cd_regione = aCdRegione AND
                           cd_provincia = aCdProvincia AND
                           pg_comune = aPgComune AND
                           im_inferiore >= aRecVPreScaglione.im_superiore AND
                           im_inferiore <= aImportoMaxRifScaglione
                    ORDER BY 4;
               LOOP

                  FETCH gen_cur_a INTO aRecTmpScaglioneImponibile;

                  EXIT WHEN gen_cur_a%NOTFOUND;

                  IF    (aRecTmpScaglioneImponibile.aliquota_ente > 0 AND
                         aRecTmpScaglioneImponibile.aliquota_percip > 0) THEN
                        aTiEntePercip:='*';
                  ELSIF (aRecTmpScaglioneImponibile.aliquota_ente > 0 AND
                         aRecTmpScaglioneImponibile.aliquota_percip = 0) THEN
                        aTiEntePercip:='E';
                  ELSIF (aRecTmpScaglioneImponibile.aliquota_ente = 0 AND
                         aRecTmpScaglioneImponibile.aliquota_percip > 0) THEN
                        aTiEntePercip:='P';
                  END IF;

                  aRecScaglioneImponibile.cd_contributo_ritenuta:=aRecTmpScaglioneImponibile.cd_contributo_ritenuta;
                  aRecScaglioneImponibile.ti_anagrafico:=aRecTmpScaglioneImponibile.ti_anagrafico;
                  aRecScaglioneImponibile.dt_inizio_validita:=aRecTmpScaglioneImponibile.dt_inizio_validita;
                  aRecScaglioneImponibile.im_inferiore:=aRecTmpScaglioneImponibile.im_inferiore;
                  aRecScaglioneImponibile.cd_regione:=aRecTmpScaglioneImponibile.cd_regione;
                  aRecScaglioneImponibile.cd_provincia:=aRecTmpScaglioneImponibile.cd_provincia;
                  aRecScaglioneImponibile.pg_comune:=aRecTmpScaglioneImponibile.pg_comune;
                  aRecScaglioneImponibile.ti_ente_percipiente:=aTiEntePercip;
                  aRecScaglioneImponibile.im_superiore:=aRecTmpScaglioneImponibile.im_superiore;
                  aRecScaglioneImponibile.aliquota_ente:=aRecTmpScaglioneImponibile.aliquota_ente;
                  aRecScaglioneImponibile.aliquota_percip:=aRecTmpScaglioneImponibile.aliquota_percip;
                  aRecScaglioneImponibile.base_calcolo_ente:=aRecTmpScaglioneImponibile.base_calcolo_ente;
                  aRecScaglioneImponibile.base_calcolo_percip:=aRecTmpScaglioneImponibile.base_calcolo_percip;
                  aRecScaglioneImponibile.dt_fine_validita:=aRecTmpScaglioneImponibile.dt_fine_validita;

                  IF aImportoMaxRifScaglione < aRecScaglioneImponibile.im_superiore THEN
                     aImponibileBlocco:=resto;
                  ELSE
                     aImponibileBlocco:=aRecScaglioneImponibile.im_superiore -
                                        aRecScaglioneImponibile.im_inferiore +
                                        0.01;
                     resto:=resto - aImponibileBlocco;
                  END IF;

                  costruisciTabellaCoriDet(i,
                                           aImponibileBlocco,
                                           aRecScaglioneImponibile);

               END LOOP;

               CLOSE gen_cur_a;

            END;

            -- Aggiornamento matrice di calcolo cori.
             -- Valorizzazione di imponibile e montante. Imponibile �l vero valore dell'imponibile mentre
             -- montante �uello utilizzato per il recupero dell'aliquota dagli scaglioni
             tabella_cori(i).tImponibileLordo:=aImponibileLordo;
             tabella_cori(i).tImponibileNetto:=aImponibileNetto;
             tabella_cori(i).tImDeduzioneIrpef:=aImDeduzione;
             tabella_cori(i).tImDeduzioneFamily:=0;
             IF aMontanteNetto = 0 THEN
                tabella_cori(i).tMontante:=aImponibileNetto;
             ELSE
                tabella_cori(i).tMontante:=aImportoMaxRifScaglione;
             END IF;

         END IF;  --Solo per la RIDUZIONE ERARIALE �revista l'operativit�u pi� scaglioni
--pipe.send_message('tabella_cori(i).tCdCori = '||tabella_cori(i).tCdCori);
--pipe.send_message('tabella_cori(i).tImponibileNetto = '||tabella_cori(i).tImponibileNetto);
--pipe.send_message('tabella_cori(i).tAliquotaPercip = '||tabella_cori(i).tAliquotaPercip);
--pipe.send_message('tabella_cori(i).tAmmontarePercip = '||tabella_cori(i).tAmmontarePercip);
--pipe.send_message('tabella_cori(i).tMontante = '||tabella_cori(i).tMontante);
      END LOOP;

   END;

END calcolaCoriDipendente;

-- =================================================================================================
-- Calcolo del singolo contributo/ritenuta
-- =================================================================================================
PROCEDURE calcolaCoriAltro
   (
    inCdsMcarriera MINICARRIERA.cd_cds%TYPE,
    inUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
    inPgMinicarriera MINICARRIERA.pg_minicarriera%TYPE
    ) IS
   i BINARY_INTEGER;

   aCdRegione COMPENSO.cd_regione_add%TYPE;
   aCdProvincia COMPENSO.cd_provincia_add%TYPE;
   aPgComune COMPENSO.pg_comune_add%TYPE;
   aCreditoIrpef  NUMBER(15,2);
   CREDITO_PAREGGIO_DETRAZIONI VARCHAR2(1);
   aImponibileLordoCori NUMBER(15,2);
   aImponibileNettoCori NUMBER(15,2);
   aMontanteLordoCori NUMBER(15,2);
   aMontanteNettoCori NUMBER(15,2);
   aMontanteDeduzione NUMBER(15,2);
   aMontanteDeduzioneFamily NUMBER(15,2);
   aImDeduzioneCori NUMBER(15,2);
   aImDeduzioneLordoCori NUMBER(15,2);
   aImDeduzioneFamilyCori NUMBER(15,2);
   aImDeduzioneFamilyLordoCori NUMBER(15,2);

   aMontanteLordoCoriOcca NUMBER(15,2);
   aMontanteNettoCoriOcca NUMBER(15,2);

   isRilevaAnnualizzato CHAR(1);
   isScaglioneSpeciale char(1):=null;
   aImportoAccessoScaglione NUMBER(15,2);
   aImportoMaxRifScaglione NUMBER(15,2);
   aImportoAccessoDetrazioni NUMBER(15,2);
   aAliquotaIrpefAnag SCAGLIONE.aliquota%TYPE;
   aAliquotaAppo  SCAGLIONE.aliquota%TYPE;
   aBloccoCalcolo VARCHAR2(5);
   aImponibileBlocco NUMBER(15,2);
   compenso_attuale NUMBER(15,2);
   resto NUMBER(15,2);
   aTiEntePercip SCAGLIONE.ti_ente_percipiente%TYPE;

   aNumGGTotaleMcarriera INTEGER;
   aNumGGProprioMcarriera INTEGER;

   aNumMMTotaleMcarriera INTEGER;
   aNumMMProprioMcarriera INTEGER;
   aNumMMEsercizio INTEGER;

   aRecScaglioneMontante V_PRE_SCAGLIONE%ROWTYPE;
   aRecScaglioneMontanteAppo V_PRE_SCAGLIONE%ROWTYPE;
   aRecTmpScaglioneImponibile V_SCAGLIONE%ROWTYPE;
   aRecScaglioneImponibile V_PRE_SCAGLIONE%ROWTYPE;
   aRecVoceIva VOCE_IVA%ROWTYPE;
   aRecTipologiaRischio TIPOLOGIA_RISCHIO%ROWTYPE;
   aRecParametriCNR PARAMETRI_CNR%ROWTYPE;

   dataInizioGestioneCuneoFiscale date;

   IM_PAGAMENTO_ESTERNO_ALTRI NUMBER := 0;

   --aImpostaPerCreditoIrpef NUMBER(15,2):=0;
   aNumGGTotMinPerCredito INTEGER:=0;

   gen_cur_a GenericCurTyp;

   pDataInizioMinicarrieraPerCred date;
   pDataFineMinicarrieraPerCred date;
    aNumGGTotaleMcarrieraPerCred  INTEGER:=0;
    aNumGGProprioMcarrieraPerCred  INTEGER:=0;

Begin
   dataOdierna:=Sysdate;
   aRecParametriCNR := getParametriCNR(aRecCompenso.Esercizio);
   BEGIN

      ----------------------------------------------------------------------------------------------
      -- Loop sulla matrice dei codici contributo/ritenuta da elaborare per il trattamento in gestione

      FOR i IN tabella_cori.FIRST .. tabella_cori.LAST

      Loop
      isScaglioneSpeciale:=null;
--pipe.send_message('tabella_cori(i).tCdClassificazioneCori = '||tabella_cori(i).tCdClassificazioneCori);
         If tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriPrevid  Then
           Select Nvl(Sum(Nvl(A.IM_PAGAMENTO,0)),0) IM_PAGAMENTO_ESTERNO
           Into IM_PAGAMENTO_ESTERNO_ALTRI
           From PAGAMENTO_ESTERNO A
           Where A.cd_anag = (Select cd_anag
                              From TERZO
                              Where cd_terzo = aRecCompenso.cd_terzo)
             And A.dt_pagamento <= aRecCompenso.dt_registrazione
             And To_Char(A.dt_pagamento,'yyyy') = To_Char(aRecCompenso.dt_registrazione,'yyyy')
             And A.cd_tipo_rapporto != aRecParametriCNR.Cd_Tipo_Rapporto;
         Else
           IM_PAGAMENTO_ESTERNO_ALTRI := 0;
         End If;
         ----------------------------------------------------------------------------------------------
         -- Azzeramento variabili per il calcolo

         aImponibileLordoCori:=0;
         aImponibileNettoCori:=0;
         aMontanteLordoCori:=0;
         aMontanteNettoCori:=0;
         aMontanteLordoCoriOcca:=0;
         aMontanteNettoCoriOcca:=0;
         aMontanteDeduzione:=0;
         aMontanteDeduzioneFamily:=0;
         aImDeduzioneCori:=0;
         aImDeduzioneLordoCori:=0;
         aImDeduzioneFamilyCori:=0;
         aImDeduzioneFamilyLordoCori:=0;
         aTiEntePercip:=NULL;
         aNumGGTotaleMcarriera:=0;
         aNumGGProprioMcarriera:=0;
         aNumMMTotaleMcarriera:=0;
         aNumMMProprioMcarriera:=0;
         aNumMMEsercizio:=0;
         compenso_attuale := 0;
         -------------------------------------------------------------------------------------------
         -- Valorizzazione dei parametri di regione, provincia e comune

         getDatiTerritorio(aCdRegione,
                           aCdProvincia,
                           aPgComune,
                           i);

         -------------------------------------------------------------------------------------------
         -- Determino il valore dell'imponibile reale per ogni singolo cori in trattamento.

         aImponibileLordoCori:=getImponibileCori(i);
--pipe.send_message('aImponibileLordoCori = '||aImponibileLordoCori);
         If tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriPrevid Then
           aImponibileLordoCori := aImponibileLordoCori - aRecCompenso.quota_esente_inps;
         End If;

         -- Prima veniva fatto in getImponibileCori ma �tato spostato per gestire la quota esente inps per i non dip
         -- Eseguo la normalizzazone dell'imponibile in base alla precisione indicata nel trattamento.
         -- Prendo in considerazione solo la precisione pari a 1.
         IF (tabella_cori(i).tPrecisione IS NOT NULL AND
             tabella_cori(i).tPrecisione = 1) THEN
             aImponibileLordoCori:=ROUND(aImponibileLordoCori);
         END IF;
--pipe.send_message('aImponibileLordoCori dopo arrot = '||aImponibileLordoCori);
         --se �n compenso da conguaglio ed �n cervellone occorre sottrarre il tot inps ed il tot inail
         If aOrigineCompenso=CNRCTB545.isCompensoConguaglio And
            (aRecTipoTrattamento.fl_agevolazioni_cervelli = 'Y'
             Or aRecCompenso.aliquota_fiscale = 100
             --or aRecTipoTrattamento.cd_trattamento in ('T162','T163','CONGL20','CONGL30')
             or aRecTipoTrattamento.fl_agevolazioni_rientro_lav = 'Y') Then
           aImponibileLordoCori:=aImponibileLordoCori - aRecCompenso.im_cr_percipiente;
           if (aImponibileLordoCori<0) then
             aImponibileLordoCori:=0;
            end if;
         End If;

         aImponibileNettoCori:=aImponibileLordoCori;

         -------------------------------------------------------------------------------------------
         -- Determino il montante di riferimento. Vale zero se il codice contributo ritenuta
         -- non �estito a montante.

         getImportoMontanteAltro(tabella_cori(i).tPgClassificazioneMontanti,
                                 aMontanteLordoCori,
                                 aMontanteNettoCori,
                                 aMontanteDeduzione,
                                 aMontanteDeduzioneFamily,
                                 aMontanteLordoCoriOcca,
                                 aMontanteNettoCoriOcca);
         compenso_attuale := aImponibileLordoCori;

         If tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriPrevid Then
           If aRecCompenso.cd_tipo_rapporto = aRecParametriCNR.cd_tipo_rapporto Then
             If (aRecParametriCNR.IMPORTO_FRANCHIGIA_OCCA - aMontanteLordoCori) > 0 Then
               If(aImponibileLordoCori - (aRecParametriCNR.IMPORTO_FRANCHIGIA_OCCA - aMontanteLordoCori)) > 0 Then
                 aImponibileLordoCori := aImponibileLordoCori -(aRecParametriCNR.IMPORTO_FRANCHIGIA_OCCA -
                                                              aMontanteLordoCori);
                 aImponibileNettoCori := aImponibileNettoCori -(aRecParametriCNR.IMPORTO_FRANCHIGIA_OCCA -
                                                              aMontanteLordoCori);
               Else
                 aImponibileLordoCori := 0;
                 aImponibileNettoCori := 0;
               End If;
             End If;
           End If;
         End If;

         If tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriPrevid  Then
           --send_message('aImponibileLordoCori :'||aImponibileLordoCori);
           If aRecCompenso.cd_tipo_rapporto = aRecParametriCNR.Cd_Tipo_Rapporto Then
             If (aMontanteNettoCori + compenso_attuale - aRecParametriCNR.IMPORTO_FRANCHIGIA_OCCA) > 0 Then
               aMontanteNettoCori := (compenso_attuale - aImponibileLordoCori) +
                                     aMontanteNettoCori -
                                     aRecParametriCNR.IMPORTO_FRANCHIGIA_OCCA +
                                     aMontanteNettoCoriOcca +
                                     IM_PAGAMENTO_ESTERNO_ALTRI;
             Else
               aMontanteNettoCori := 0;
             End If;
           Else

              If (aMontanteNettoCoriOcca -
                 aRecParametriCNR.IMPORTO_FRANCHIGIA_OCCA > 0) Then
                aMontanteNettoCori := (aMontanteNettoCoriOcca -
                                       aRecParametriCNR.IMPORTO_FRANCHIGIA_OCCA) +
                                      aMontanteNettoCori +
                                      (compenso_attuale - aImponibileLordoCori) +
                                      IM_PAGAMENTO_ESTERNO_ALTRI;
              Else
                aMontanteNettoCori := aMontanteNettoCori + IM_PAGAMENTO_ESTERNO_ALTRI;
              End If;
           End If;
         End If;

         -------------------------------------------------------------------------------------------
         -- Definisco se per il codice contributo ritenuta in elaborazione rileva la gestione
         -- annualizzata. Sono gestite le ipotesi di annualizzazione, aliquota in anagrafico e compensi
         -- a tassazione separata.
         -- Sono gestite le seguenti eccezioni nella gestione dell'annualizzazione:
         -- - Compensi senza calcoli non rilevano mai,
         -- - Compensi da conguaglio pu�levare il solo caso di aliquota in anagrafico.

         isRilevaAnnualizzato:=getRilevaAnnualizzato(i);

         -------------------------------------------------------------------------------------------
         -- Recupero dello scaglione di riferimento al calcolo. L'operazione procede nelle seguenti fasi
         -- 1) Determino il valore massimo complessivo di riferimento per l'ingresso allo scaglione.
         --    Questo �l valore lordo dell'accesso allo scaglione.
         --    -- Se il calcolo �er conguaglio vale il valore complessivo dell'imponibile lordo cori
         --    -- Se il cori in calcolo �nnualizzato si calcola tale valore. Tale calcolo �iversificato
         --       se l'origine del compenso �a minicarriera
         --    -- Negli altri casi corrisponde al valore lordo del montante sommato all'imponibilo lordo cori
         -- 2) Si procede al calcolo dell'eventuale deduzione
         -- 3) Si sottrae il valore della deduzione dall'imponibile netto cori (prima uguale al lordo)
         -- 4) Si sottrae il valore della deduzione dall'importo accesso scaglione
--pipe.send_message('isRilevaAnnualizzato = '||isRilevaAnnualizzato);
         -- Determinazione del valore massimo complessivo di accesso allo scaglione (valore lordo)
         IF aOrigineCompenso=CNRCTB545.isCompensoConguaglio THEN
            aImportoMaxRifScaglione:=aImponibileLordoCori;
         Else
            --aggiunto isRilevaAnnualizzato = '1' perch�nche se c'�'aliquota marginale
            --aImportoMaxRifScaglione deve essere calcolato come isRilevaAnnualizzato = '2'
            IF isRilevaAnnualizzato = '2' Or isRilevaAnnualizzato = '1' THEN
               IF (aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
                   aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong) Then
                  aImportoMaxRifScaglione:=CNRCTB600.getImRateDiTerzoInEsercizio(aRecAnagrafico,
                                                                                 aRecCompenso,
                                                                                 inCdsMcarriera,
                                                                                 inUoMcarriera,
                                                                                 inEsercizioMcarriera,
                                                                                 inPgMinicarriera);
--pipe.send_message('1 - aImportoMaxRifScaglione = '||aImportoMaxRifScaglione);
                  CNRCTB600.getNumeroGGRateMcarriera(aRecAnagrafico,
                                                     aRecCompenso,
                                                     inCdsMcarriera,
                                                     inUoMcarriera,
                                                     inEsercizioMcarriera,
                                                     inPgMinicarriera,
                                                     aNumGGTotaleMcarriera,
                                                     aNumGGProprioMcarriera);

                  dataInizioGestioneCuneoFiscale := CNRCTB015.getDt01PerChiave('0', 'RIDUZIONE_CUNEO_DL_3_2020', 'DATA_INIZIO');

                  pDataInizioMinicarrieraPerCred := TO_DATE('0101' || aRecCompenso.esercizio,'DDMMYYYY');
                  pDataFineMinicarrieraPerCred := TO_DATE('3112' || aRecCompenso.esercizio,'DDMMYYYY');

                  if dataInizioGestioneCuneoFiscale > pDataInizioMinicarrieraPerCred then
                    if aRecCompenso.dt_da_competenza_coge < dataInizioGestioneCuneoFiscale then
                        CNRCTB600.getNumeroGGRateMcarriera(aRecAnagrafico,
                                                         aRecCompenso,
                                                         inCdsMcarriera,
                                                         inUoMcarriera,
                                                         inEsercizioMcarriera,
                                                         inPgMinicarriera,
                                                         pDataInizioMinicarrieraPerCred,
                                                         dataInizioGestioneCuneoFiscale - 1,
                                                         aNumGGTotaleMcarrieraPerCred,
                                                         aNumGGProprioMcarrieraPerCred);

                    else
                        CNRCTB600.getNumeroGGRateMcarriera(aRecAnagrafico,
                                                         aRecCompenso,
                                                         inCdsMcarriera,
                                                         inUoMcarriera,
                                                         inEsercizioMcarriera,
                                                         inPgMinicarriera,
                                                         dataInizioGestioneCuneoFiscale,
                                                         pDataFineMinicarrieraPerCred,
                                                         aNumGGTotaleMcarrieraPerCred,
                                                         aNumGGProprioMcarrieraPerCred);
                    end if;
                  else
                    aNumGGTotaleMcarrieraPerCred := aNumGGTotaleMcarriera;
                    aNumGGProprioMcarrieraPerCred := aNumGGProprioMcarriera;
                  end if;

               Else
                  If isRilevaAnnualizzato = '2' Then
                       aImportoMaxRifScaglione:=ROUND(
                                                   (aImponibileLordoCori /
                                                    IBMUTL001.getDaysCommBetween
                                                         (aRecCompenso.dt_da_competenza_coge,
                                                          aRecCompenso.dt_a_competenza_coge
                                                         ) *
                                                      360
                                                    ),2
                                                 );
--pipe.send_message('2 - aImportoMaxRifScaglione = '||aImportoMaxRifScaglione);
                  Else
                      aImportoMaxRifScaglione:=aMontanteNettoCori + aImponibileLordoCori;
--pipe.send_message('3 - aImportoMaxRifScaglione = '||aImportoMaxRifScaglione);
                  End If;
               END IF;
            Elsif isRilevaAnnualizzato = '3' Then
               --per la tassazione separata devo prendere solo l'importo corrente
               aImportoMaxRifScaglione:=aImponibileLordoCori;
--pipe.send_message('4 - aImportoMaxRifScaglione = '||aImportoMaxRifScaglione);
            Else
               aImportoMaxRifScaglione:=aMontanteNettoCori + aImponibileLordoCori;
--pipe.send_message('5 - aImportoMaxRifScaglione = '||aImportoMaxRifScaglione);
            END IF;
         END IF;
         -- Calcolo l'eventuale deduzione. Per i compensi da minicarriera annualizzati il calcolo
         -- dell'imponibile totale �atto con la deduzione lorda
         CNRCTB600.getNumeroMMRateMcarriera(aRecAnagrafico,
                                            aRecCompenso,
                                            inCdsMcarriera,
                                            inUoMcarriera,
                                            inEsercizioMcarriera,
                                            inPgMinicarriera,
                                            aNumMMTotaleMcarriera,
                                            aNumMMProprioMcarriera,
                                            aNumMMEsercizio,
                                            aOrigineCompenso);

         calcolaDeduzione(i,
                          aRecAnagrafico,
                          inCdsMcarriera,
                          inUoMcarriera,
                          inEsercizioMcarriera,
                          inPgMinicarriera,
                          isRilevaAnnualizzato,
                          aImportoMaxRifScaglione,
                          aImponibileLordoCori,
                          aMontanteDeduzione,
                          aMontanteDeduzioneFamily,
                          aNumGGTotaleMcarriera,
                          aNumGGProprioMcarriera,
                          aNumMMTotaleMcarriera,
                          aNumMMProprioMcarriera,
                          aNumMMEsercizio,
                          aImDeduzioneCori,
                          aImDeduzioneLordoCori,
                          aImDeduzioneFamilyCori,
                          aImDeduzioneFamilyLordoCori);

         --Controllo che l'imponibile non diventi negativo, nel caso diminuisco la deduzione Family
         --fino al massimo consentito
         If aImponibileNettoCori - aImDeduzioneCori - aImDeduzioneFamilyCori < 0 Then
           aImDeduzioneFamilyCori := aImponibileNettoCori - aImDeduzioneCori;
         End If;
         aImponibileNettoCori:=aImponibileNettoCori - aImDeduzioneCori - aImDeduzioneFamilyCori;
         IF (isRilevaAnnualizzato = '2' AND
               (aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
                aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong)
            ) THEN
            aImportoMaxRifScaglione:=aImportoMaxRifScaglione - aImDeduzioneLordoCori - aImDeduzioneFamilyLordoCori;
         ELSE
            aImportoMaxRifScaglione:=aImportoMaxRifScaglione - aImDeduzioneCori - aImDeduzioneFamilyCori;
         END IF;

         -------------------------------------------------------------------------------------------
         -- Determino il valore di ingresso per la ricerca dello scaglione.

         aImportoAccessoScaglione:=0;
         aAliquotaIrpefAnag:=0;

         -- i cori INAIL, IVA e addizionali territorio da rateizzazione non sono gestiti sulla tabella
         -- scaglioni ma i valori estratti sono portati sulla struttura dello stesso per congruenza
         -- della procedura
         IF    tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriIva THEN
               aRecVoceIva:=CNRCTB545.getVoceIva(aRecCompenso.cd_voce_iva);
               aRecScaglioneMontante:=NULL;
               aRecScaglioneMontante.cd_contributo_ritenuta:=aRecVoceIva.cd_voce_iva;

               if aRecCompenso.fl_split_payment = 'Y' and aRecCompenso.TI_ISTITUZ_COMMERC = 'I' THEN
                   aRecScaglioneMontante.ti_ente_percipiente:='*';
                   aRecScaglioneMontante.aliquota_ente:=aRecVoceIva.percentuale;
                   aRecScaglioneMontante.aliquota_percip:=aRecVoceIva.percentuale;
                   aRecScaglioneMontante.base_calcolo_percip:=100;
               else
                 aRecScaglioneMontante.ti_ente_percipiente:='E';
                 aRecScaglioneMontante.aliquota_ente:=aRecVoceIva.percentuale;
               end if;
               aRecScaglioneMontante.base_calcolo_ente:=100;

               --aRecScaglioneMontante.ti_ente_percipiente:='E';
               --aRecScaglioneMontante.aliquota_ente:=aRecVoceIva.percentuale;
               --aRecScaglioneMontante.base_calcolo_ente:=100;
               aRecScaglioneMontante.im_inferiore:=0;
               aRecScaglioneMontante.im_superiore:=9999999999999;
         ELSIF tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriInail THEN
               aRecTipologiaRischio:=CNRCTB545.getTipologiaRischio(aRecCompenso.cd_tipologia_rischio,
                                                                   aRecCompenso.dt_registrazione);
               aRecScaglioneMontante:=NULL;
               aRecScaglioneMontante.cd_contributo_ritenuta:=aRecTipologiaRischio.cd_tipologia_rischio;
               aRecScaglioneMontante.ti_ente_percipiente:='*';
               if aRecTipoTrattamento.fl_solo_inail_ente = 'Y' THEN
                 aRecScaglioneMontante.aliquota_ente:=aRecTipologiaRischio.aliquota_ente + aRecTipologiaRischio.aliquota_percipiente;
                 aRecScaglioneMontante.aliquota_percip:=0;
               else
                 aRecScaglioneMontante.aliquota_ente:=aRecTipologiaRischio.aliquota_ente;
                 aRecScaglioneMontante.aliquota_percip:=aRecTipologiaRischio.aliquota_percipiente;
               end if;
               aRecScaglioneMontante.base_calcolo_ente:=100;
               aRecScaglioneMontante.base_calcolo_percip:=100;
               aRecScaglioneMontante.im_inferiore:=0;
               aRecScaglioneMontante.im_superiore:=9999999999999;
         ELSIF CNRCTB545.getIsAddTerritorioRecRate(tabella_cori(i).tCdClassificazioneCori) = 'Y' THEN
               aRecScaglioneMontante:=NULL;
               aRecScaglioneMontante.cd_contributo_ritenuta:=tabella_cori(i).tCdCori;
               aRecScaglioneMontante.ti_ente_percipiente:='P';
               aRecScaglioneMontante.aliquota_percip:=0;
               aRecScaglioneMontante.base_calcolo_percip:=100;
               aRecScaglioneMontante.im_inferiore:=0;
               aRecScaglioneMontante.im_superiore:=9999999999999;
         ELSIF tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriAddComAcconto THEN
               aRecScaglioneMontante:=NULL;
               aRecScaglioneMontante.cd_contributo_ritenuta:=tabella_cori(i).tCdCori;
               aRecScaglioneMontante.ti_ente_percipiente:='P';
               aRecScaglioneMontante.aliquota_percip:=0;
               aRecScaglioneMontante.base_calcolo_percip:=100;
               aRecScaglioneMontante.im_inferiore:=0;
               aRecScaglioneMontante.im_superiore:=9999999999999;
         ELSE
          If tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriAddCom And
             aImportoMaxRifScaglione <= CNRCTB545.getEsenzioniAddcom(aPgComune,dataOdierna).importo Then
               aRecScaglioneMontante:=NULL;
               aRecScaglioneMontante.cd_contributo_ritenuta:=tabella_cori(i).tCdCori;
               aRecScaglioneMontante.ti_ente_percipiente:='P';
               aRecScaglioneMontante.aliquota_percip:=0;
               aRecScaglioneMontante.base_calcolo_percip:=100;
               aRecScaglioneMontante.im_inferiore:=0;
               aRecScaglioneMontante.im_superiore:=9999999999999;
          Else
            IF    aOrigineCompenso=CNRCTB545.isCompensoConguaglio THEN
                  IF isRilevaAnnualizzato = '1' THEN
                     aImportoAccessoScaglione:=0;
                     aAliquotaIrpefAnag:=aRecAnagrafico.aliquota_fiscale;
                  ELSE
                     aImportoAccessoScaglione:=0;
                  END IF;
            ELSIF (aRecCompenso.fl_senza_calcoli = 'Y' OR
                   isRilevaAnnualizzato = '2' OR
                   isRilevaAnnualizzato = '3' OR
                   isRilevaAnnualizzato = '4') THEN
                  aImportoAccessoScaglione:=0;
            ELSIF isRilevaAnnualizzato = '1' THEN
                  aImportoAccessoScaglione:=0;
                  aAliquotaIrpefAnag:=aRecAnagrafico.aliquota_fiscale;
            ELSE
                  aImportoAccessoScaglione:=aMontanteNettoCori;
            END IF;
--pipe.send_message('aImportoAccessoScaglione: 1:'||aImportoAccessoScaglione);

            -- Lettura degli scaglioni (uno solo, se si opera su pi� scaglioni �l primo)

            /* In genere aImportoAccessoScaglione �empre 0.
               Per l'add reg, se la regione prevede la gestione dell'aliq. max per l'intero importo,
               gli passo l'importo effettivo, altrimenti 0*/
            If tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriAddReg then
               if(cnrctb545.getIsRegConAliqMax(aCdRegione) = 'Y' ) Then
                      --devo prendere gi�o scaglione corretto (dovendo prendere solo quello massimo)
                      aImportoAccessoScaglione := aImportoMaxRifScaglione;
               ELSIF (aImportoMaxRifScaglione <= getRegConScaglioneSpe(aCdRegione,dataOdierna).im_superiore) Then
                   aRecScaglioneMontante:=NULL;
                   aRecScaglioneMontante.pg_comune:=aPgComune;
                   aRecScaglioneMontante.cd_contributo_ritenuta:=tabella_cori(i).tCdCori;
                   aRecScaglioneMontante.cd_regione:=aCdRegione;
                   aRecScaglioneMontante.cd_provincia:=aCdProvincia;
                   aRecScaglioneMontante.ti_anagrafico:='*';
                   aRecScaglioneMontante.ti_ente_percipiente:='P';
                   aRecScaglioneMontante.aliquota_percip:=getRegConScaglioneSpe(aCdRegione,dataOdierna).aliquota;
                   aRecScaglioneMontante.base_calcolo_percip:=100;
                   aRecScaglioneMontante.im_inferiore:=0;
                   aRecScaglioneMontante.im_superiore:=9999999999999;
                   isScaglioneSpeciale:='S';
               end if;
            End If;
            /* In genere aImportoAccessoScaglione �empre 0.
               Per l'add com, se il comune prevede la gestione dell'aliq. max per l'intero importo,
               gli passo l'importo effettivo, altrimenti 0*/
            If tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriAddCom And
               cnrctb545.getIsComConAliqMax(aPgComune) = 'Y' Then
                      --devo prendere gi�o scaglione corretto (dovendo prendere solo quello massimo)
                      aImportoAccessoScaglione := aImportoMaxRifScaglione;
            End If;

            aRecScaglioneMontante:=CNRCTB545.getScaglione(tabella_cori(i).tCdCori,
                                                          aRecTipoTrattamento.ti_anagrafico,
                                                          aRecCompenso.dt_registrazione,
                                                          aImportoAccessoScaglione,
                                                          aAliquotaIrpefAnag,
                                                          aCdRegione,
                                                          aCdProvincia,
                                                          aPgComune);
            -- se  aAliquotaIrpefAnag �alorizzata, devo controllare che non sia minore di quella
            -- che verrebbe applicata se non valorizzata
            If isRilevaAnnualizzato = '1' And aAliquotaIrpefAnag != 0 Then
                 aAliquotaAppo:=0;
                 aRecScaglioneMontanteAppo:=CNRCTB545.getScaglione(tabella_cori(i).tCdCori,
                                                          aRecTipoTrattamento.ti_anagrafico,
                                                          aRecCompenso.dt_registrazione,
                                                          aImportoMaxRifScaglione,
                                                          aAliquotaAppo,
                                                          aCdRegione,
                                                          aCdProvincia,
                                                          aPgComune);
                 If aAliquotaIrpefAnag < aRecScaglioneMontanteAppo.aliquota_percip Then
                      IBMERR001.RAISE_ERR_GENERICO
                         ('L''aliquota presente nei dati Anagrafici del percipiente �inore di quella dovuta.');
                 End If;
            End If;
          End If;
         END IF;  -- fine di   IF tabella_cori(i).tCdClassificazioneCori = ....

         -------------------------------------------------------------------------------------------
         -- Calcolo delle detrazioni personali e familiari. Non eseguite mai dal conguaglio che provvede
         -- in modo autonomo al calcolo

         -- Normalizzo importoAccessoScaglione per il calcolo delle detrazioni nel caso di annualizzato = 1
         -- o 2 oppure per conguaglio
         --credo non serva
         IF    isRilevaAnnualizzato = '1' THEN
               aImportoAccessoScaglione:=aRecScaglioneMontante.im_inferiore;
         ELSIF (isRilevaAnnualizzato = '2' OR
                aOrigineCompenso=CNRCTB545.isCompensoConguaglio) THEN
               aImportoAccessoScaglione:=aImportoMaxRifScaglione;
         END IF;

         -- Memorizzo importo per accesso alle detrazioni (imponibile o montante IRPEF)
         aImportoAccessoDetrazioni:=aImportoMaxRifScaglione;
--pipe.send_message('aImportoAccessoDetrazioni = '||aImportoAccessoDetrazioni);
   --solo per il cori irpef a scaglioni calcolo il reddito complessivo e lo memorizzo anche per il calcolo del credito
   IF CNRCTB545.getIsIrpefScaglioni(tabella_cori(i).tCdClassificazioneCori,
                                    tabella_cori(i).tPgClassificazioneMontanti,
                                    tabella_cori(i).tFlScriviMontanti) = 'Y' Then


         aTotRedditoComplessivo := aImportoAccessoDetrazioni
                                   + glbRedditoComplessivo
                                   + glbImponibilePagatoAltro
                                   + glbImponibilePagatoDip;
--pipe.send_message('aImportoAccessoDetrazioni = '||aImportoAccessoDetrazioni);
--pipe.send_message('glbRedditoComplessivo = '||glbRedditoComplessivo);
--pipe.send_message('glbImponibilePagatoAltro = '||glbImponibilePagatoAltro);
--pipe.send_message('glbImponibilePagatoDip = '||glbImponibilePagatoDip);
--pipe.send_message('aTotRedditoComplessivo = '||aTotRedditoComplessivo);
   END IF;

         calcolaDetrazioni(i,
                           aTotRedditoComplessivo, --aImportoAccessoDetrazioni,
                           inCdsMcarriera,
                           inUoMcarriera,
                           inEsercizioMcarriera,
                           inPgMinicarriera,
                           isRilevaAnnualizzato,
                           aNumMMTotaleMcarriera,
                           aNumMMEsercizio);

         -------------------------------------------------------------------------------------------
         -- Lettura degli scaglioni, verifico che il montante arricchito dell'imponibile non determini
         -- un nuovo scaglione

         -- L'imponibile o il trattamento non prevedono l'operativit�u pi� scaglioni

         IF (aRecCompenso.fl_senza_calcoli = 'Y' Or
             --esiste l'aliquota nell'anagrafico ed �alida (cio�resente tra gli scaglioni)
             --(isRilevaAnnualizzato = '1' And aAliquotaIrpefAnag != 0) Or
             isRilevaAnnualizzato = '1' Or
             isRilevaAnnualizzato = '3' OR
             isRilevaAnnualizzato = '4' OR
             aRecScaglioneMontante.im_superiore >= aImportoMaxRifScaglione) THEN

            -- Valorizzazione di imponibile e montante. Imponibile �l vero valore dell'imponibile mentre
            -- montante �uello utilizzato per il recupero dell'aliquota dagli scaglioni
            tabella_cori(i).tImponibileLordo:=aImponibileLordoCori;
            tabella_cori(i).tImponibileNetto:=aImponibileNettoCori;
            tabella_cori(i).tImDeduzioneIrpef:=aImDeduzioneCori;
            tabella_cori(i).tImDeduzioneFamily:=aImDeduzioneFamilyCori;
            tabella_cori(i).tMontante:=aImportoAccessoScaglione;

            IF (aRecScaglioneMontante.ti_ente_percipiente = 'P' OR
                aRecScaglioneMontante.ti_ente_percipiente = '*') THEN
               IF aRecCompenso.fl_senza_calcoli = 'Y' THEN
                  tabella_cori(i).tAliquotaPercip:=0;
                  tabella_cori(i).tBaseCalcoloPercip:=0;
                  tabella_cori(i).tAmmontarePercipLordo:=0;
                  tabella_cori(i).tAmmontarePercip:=0;
               ELSE
                  IF    isRilevaAnnualizzato = '3' THEN
                        tabella_cori(i).tAliquotaPercip:=aRecCompenso.aliquota_irpef_tassep;
                  ELSIF isRilevaAnnualizzato = '4' THEN
                        tabella_cori(i).tAliquotaPercip:=aRecCompenso.aliquota_irpef_da_missione;
                  ELSE
                     tabella_cori(i).tAliquotaPercip:=aRecScaglioneMontante.aliquota_percip;
                  END IF;
                  tabella_cori(i).tBaseCalcoloPercip:=aRecScaglioneMontante.base_calcolo_percip;

                  -- Gestione del calcolo addebito recupero rate su addizionali territorio. Attivato solo
                  -- per compensi da conguaglio e da minicarriera non a tassazione separata

                  If CNRCTB545.getIsAddTerritorioRecRate(tabella_cori(i).tCdClassificazioneCori) = 'Y' THEN
                     IF (aRecCompenso.fl_compenso_mcarriera_tassep = 'N' AND
                         (aOrigineCompenso=CNRCTB545.isCompensoConguaglio OR
                          aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
                          aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong)) THEN
                        tabella_cori(i).tAmmontarePercipLordo:=CNRCTB552.calcolaAddTerritorioRecRate(aOrigineCompenso,
                                                                                                     tabella_cori(i).tCdClassificazioneCori,
                                                                                                     aRecCompenso.esercizio,
                                                                                                     aRecCompenso.dt_da_competenza_coge,
                                                                                                     aRecCompenso.dt_a_competenza_coge,
                                                                                                     aRecRateizzaClassificCoriC0,
                                                                                                     aRecRateizzaClassificCoriP0,
                                                                                                     aRecRateizzaClassificCoriR0);
                     ELSE
                        tabella_cori(i).tAmmontarePercipLordo:=0;
                     END IF;
                  Elsif tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriAddComAcconto THEN
                     IF (aRecCompenso.fl_compenso_mcarriera_tassep = 'N' AND
                         (aOrigineCompenso=CNRCTB545.isCompensoConguaglio OR
                          aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
                          aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong)) THEN
                        tabella_cori(i).tAmmontarePercipLordo:=CNRCTB553.calcolaAddComAccontoRata(aOrigineCompenso,
                                                                                                     tabella_cori(i).tCdClassificazioneCori,
                                                                                                     aRecCompenso.esercizio,
                                                                                                     aRecCompenso.dt_da_competenza_coge,
                                                                                                     aRecCompenso.dt_a_competenza_coge,
                                                                                                     aRecAccontoClassificCoriC0);
                     ELSE
                        tabella_cori(i).tAmmontarePercipLordo:=0;
                     END IF;

--pipe.send_message('aImDetrazioniPe = '||aImDetrazioniPe);
                  --Elsif tabella_cori(i).tFlCreditoIrpef = 'Y' THEN
                  Elsif CNRCTB545.IsCoriCreditoIrpef(tabella_cori(i).tCdCori) = 'Y' then
                     aCreditoIrpef := 0;
                     CREDITO_PAREGGIO_DETRAZIONI := 'N';
                     IF (aRecCompenso.fl_compenso_mcarriera_tassep = 'N'
                         AND
                         --per il conguaglio viene chiamata direttamente dal conguaglio
                         aOrigineCompenso!=CNRCTB545.isCompensoConguaglio
                         AND
                         (
                          aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
                          aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong
                         )
                         --AND
                         --aImpostaPerCreditoIrpef > aImDetrazioniPe
                        ) THEN

                                    calcolaCreditoIrpef(--i,
                                                                                   aTotRedditoComplessivo, --aImportoAccessoDetrazioni,
                                                                                   aNumGGTotMinPerCredito,
                                                                                   aRecCompenso,
                                                                                   tabella_cori(i).tCdCori,
                                                                                   aCreditoIrpef,
                                                                                   CREDITO_PAREGGIO_DETRAZIONI);
                     END IF;
                     tabella_cori(i).tAmmontarePercipLordo := aCreditoIrpef;
                     tabella_cori(i).tCreditoPareggioDetrazioni := CREDITO_PAREGGIO_DETRAZIONI;
                  --*/
                  Else

                     tabella_cori(i).tAmmontarePercipLordo:=ROUND(aImponibileNettoCori *
                                                                  (aRecScaglioneMontante.base_calcolo_percip / 100) *
                                                                  (tabella_cori(i).tAliquotaPercip / 100),2);

                  End If;

                  tabella_cori(i).tAmmontarePercip:=tabella_cori(i).tAmmontarePercipLordo;

                  --CONSERVO PER IL CALCOLO DEL CREDITO IRPEF il totale giorni minicarriera valorizzato solo per irpef a scaglioni
                  --(il cori IRPEF viene elaborato prima del credito)
                  IF CNRCTB545.getIsIrpefScaglioni(tabella_cori(i).tCdClassificazioneCori,
                                                   tabella_cori(i).tPgClassificazioneMontanti,
                                                   tabella_cori(i).tFlScriviMontanti) = 'Y' THEN
                         -- per il momento non verifichiamo l'imposta
                         --aImpostaPerCreditoIrpef := tabella_cori(i).tAmmontarePercipLordo;
                         aNumGGTotMinPerCredito:= aNumGGTotaleMcarrieraPerCred;
                  END IF;

               END IF;
            END IF;

            IF (aRecScaglioneMontante.ti_ente_percipiente = 'E' OR
                aRecScaglioneMontante.ti_ente_percipiente = '*') THEN
               IF aRecCompenso.fl_senza_calcoli = 'Y' THEN
                  tabella_cori(i).tAliquotaEnte:=0;
                  tabella_cori(i).tBaseCalcoloEnte:=0;
                  tabella_cori(i).tAmmontareEnteLordo:=0;
                  tabella_cori(i).tAmmontareEnte:=0;
               ELSE
                  tabella_cori(i).tAliquotaEnte:=aRecScaglioneMontante.aliquota_ente;
                  tabella_cori(i).tBaseCalcoloEnte:=aRecScaglioneMontante.base_calcolo_ente;
                  tabella_cori(i).tAmmontareEnteLordo:=ROUND(aImponibileNettoCori *
                                                             (aRecScaglioneMontante.base_calcolo_ente / 100) *
                                                             (aRecScaglioneMontante.aliquota_ente / 100),2);
                  tabella_cori(i).tAmmontareEnte:=tabella_cori(i).tAmmontareEnteLordo;
               END IF;
            END IF;

         -- L'imponibile o il trattamento prevedono l'operativit�u pi� scaglioni

         ELSE

            -- scrivo l'importo del primo scaglione. Si distinguono gli importi di avvio in base al fatto
            -- che il cori in elaborazione �nnualizzato o meno

            IF (isRilevaAnnualizzato = '2' OR
                aOrigineCompenso=CNRCTB545.isCompensoConguaglio) THEN
               aImponibileBlocco:=aRecScaglioneMontante.im_superiore;
               resto:=aImportoAccessoScaglione - aImponibileBlocco;
            ELSE
               aImponibileBlocco:=aRecScaglioneMontante.im_superiore - aMontanteNettoCori;
               resto:=aImponibileNettoCori - aImponibileBlocco;
            END IF;

            costruisciTabellaCoriDet(i,
                                     aImponibileBlocco,
                                     aRecScaglioneMontante);

            BEGIN
               OPEN gen_cur_a FOR

                    SELECT *
                    FROM   V_SCAGLIONE
                    WHERE  cd_contributo_ritenuta = tabella_cori(i).tCdCori AND
                           (ti_anagrafico = aRecCompenso.ti_anagrafico OR
                            ti_anagrafico = '*' ) AND
                           dt_inizio_validita <=aRecCompenso.dt_registrazione AND
                           dt_fine_validita >= aRecCompenso.dt_registrazione AND
                           cd_regione = aCdRegione AND
                           cd_provincia = aCdProvincia AND
                           pg_comune = aPgComune AND
                           im_inferiore >= aRecScaglioneMontante.im_superiore AND
                           im_inferiore <= aImportoMaxRifScaglione
                    ORDER BY 4;
               LOOP
                  FETCH gen_cur_a INTO aRecTmpScaglioneImponibile;
                  EXIT WHEN gen_cur_a%NOTFOUND;
                  --send_message('aRecScaglioneMontante.im_superiore: '||aRecScaglioneMontante.im_superiore);
                  --send_message('aImportoMaxRifScaglione '||aImportoMaxRifScaglione);

                  IF    (aRecTmpScaglioneImponibile.aliquota_ente > 0 AND
                         aRecTmpScaglioneImponibile.aliquota_percip > 0) THEN
                        aTiEntePercip:='*';
                  ELSIF (aRecTmpScaglioneImponibile.aliquota_ente > 0 AND
                         aRecTmpScaglioneImponibile.aliquota_percip = 0) THEN
                        aTiEntePercip:='E';
                  ELSIF (aRecTmpScaglioneImponibile.aliquota_ente = 0 AND
                         aRecTmpScaglioneImponibile.aliquota_percip > 0) THEN
                        aTiEntePercip:='P';
                  END IF;

                  aRecScaglioneImponibile.cd_contributo_ritenuta:=aRecTmpScaglioneImponibile.cd_contributo_ritenuta;
                  aRecScaglioneImponibile.ti_anagrafico:=aRecTmpScaglioneImponibile.ti_anagrafico;
                  aRecScaglioneImponibile.dt_inizio_validita:=aRecTmpScaglioneImponibile.dt_inizio_validita;
                  aRecScaglioneImponibile.im_inferiore:=aRecTmpScaglioneImponibile.im_inferiore;
                  aRecScaglioneImponibile.cd_regione:=aRecTmpScaglioneImponibile.cd_regione;
                  aRecScaglioneImponibile.cd_provincia:=aRecTmpScaglioneImponibile.cd_provincia;
                  aRecScaglioneImponibile.pg_comune:=aRecTmpScaglioneImponibile.pg_comune;
                  aRecScaglioneImponibile.ti_ente_percipiente:=aTiEntePercip;
                  aRecScaglioneImponibile.im_superiore:=aRecTmpScaglioneImponibile.im_superiore;
                  aRecScaglioneImponibile.aliquota_ente:=aRecTmpScaglioneImponibile.aliquota_ente;
                  if(isScaglioneSpeciale is not null and (isRilevaAnnualizzato = '2' OR
                      aOrigineCompenso=CNRCTB545.isCompensoConguaglio)) then
                    aRecScaglioneImponibile.aliquota_percip:= aRecScaglioneMontante.aliquota_percip;
                  else
                    aRecScaglioneImponibile.aliquota_percip:=aRecTmpScaglioneImponibile.aliquota_percip;
                  end if;
                  aRecScaglioneImponibile.base_calcolo_ente:=aRecTmpScaglioneImponibile.base_calcolo_ente;
                  aRecScaglioneImponibile.base_calcolo_percip:=aRecTmpScaglioneImponibile.base_calcolo_percip;
                  aRecScaglioneImponibile.dt_fine_validita:=aRecTmpScaglioneImponibile.dt_fine_validita;

                  IF aImportoMaxRifScaglione < aRecScaglioneImponibile.im_superiore THEN
                     aImponibileBlocco:=resto;
                  ELSE
                     aImponibileBlocco:=aRecScaglioneImponibile.im_superiore -
                                        aRecScaglioneImponibile.im_inferiore +
                                        0.01;
                     resto:=resto - aImponibileBlocco;
                  END IF;

                  costruisciTabellaCoriDet(i,
                                           aImponibileBlocco,
                                           aRecScaglioneImponibile);
                END LOOP;
                CLOSE gen_cur_a;
            END;

            -- Memorizzo imponibile reale e montante di riferimento
            tabella_cori(i).tImponibileLordo:=aImponibileLordoCori;
            tabella_cori(i).tImponibileNetto:=aImponibileNettoCori;
            tabella_cori(i).tImDeduzioneIrpef:=aImDeduzioneCori;
            tabella_cori(i).tImDeduzioneFamily:=aImDeduzioneFamilyCori;
            tabella_cori(i).tMontante:=aImportoAccessoScaglione;

            -- Ricalcolo aliquota per annualizzato

            IF isRilevaAnnualizzato = '2' THEN
               tabella_cori(i).tAliquotaPercip:=((tabella_cori(i).tAmmontarePercipLordo / tabella_cori(i).tMontante) * 100);
               tabella_cori(i).tAmmontarePercipLordo:=ROUND(aImponibileNettoCori * (tabella_cori(i).tAliquotaPercip / 100),2);
               tabella_cori(i).tAmmontarePercip:=tabella_cori(i).tAmmontarePercipLordo;
            END IF;

            --CONSERVO PER IL CALCOLO DEL CREDITO IRPEF il totale giorni minicarriera valorizzato solo per irpef a scaglioni
            --(il cori IRPEF viene elaborato prima del credito)
            IF CNRCTB545.getIsIrpefScaglioni(tabella_cori(i).tCdClassificazioneCori,
                                             tabella_cori(i).tPgClassificazioneMontanti,
                                             tabella_cori(i).tFlScriviMontanti) = 'Y' THEN
                  -- per il momento non verifichiamo l'imposta
                  --aImpostaPerCreditoIrpef := tabella_cori(i).tAmmontarePercipLordo;
                  aNumGGTotMinPerCredito:= aNumGGTotaleMcarrieraPerCred;
--pipe.send_message('aNumGGTotMinPerCredito = '||aNumGGTotMinPerCredito);
            END IF;

         END IF;

      END LOOP;

   END;

END calcolaCoriAltro;

-- =================================================================================================
-- Ritorna le informazioni relative alle modalit�i calcolo del cori come eccezioni rispetto alla
-- normale gestione. Sono previste le seguenti gestioni
-- 1) Compenso senza calcoli. Ignora gestione per aliquota in anagrafico o annualizzazione.
-- 2) Aliquota fiscale valorizzata in anagrafico. Si accede alla tabella scaglioni con l'aliquota
--    recuperata indipendentemente dal montante
-- 3) Annualizzazione.
-- Le gestioni 2 e 3 si applicano solo ad IRPEF a scaglioni ed addizionali comunali, provinciali e
-- regionali.
-- =================================================================================================
FUNCTION getRilevaAnnualizzato
   (
    indice BINARY_INTEGER
   )RETURN CHAR IS
   isAnnualizzato CHAR(1);
   isTipoCoriIrpefScaglioni CHAR(1);
   isTipoCoriTerritorio CHAR(1);

BEGIN

   isAnnualizzato:='N';

   -- Per il compenso senza calcoli si ignora gestione per aliquota in anagrafico o annualizzazione

   IF aRecCompenso.fl_senza_calcoli = 'Y' THEN
      RETURN isAnnualizzato;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Controllo se si tratta di contributo ritenuta che identifica irpef a scaglioni o addizionali
   -- per territorio (comunali, provinciali e regionali)

   IF tabella_cori(indice).tPgClassificazioneMontanti IS NOT NULL THEN
      isTipoCoriIrpefScaglioni:=CNRCTB545.getIsIrpefScaglioni(tabella_cori(indice).tCdClassificazioneCori,
                                                              tabella_cori(indice).tPgClassificazioneMontanti,
                                                              tabella_cori(indice).tFlScriviMontanti);
   ELSE
      isTipoCoriIrpefScaglioni:='N';
   END IF;

   isTipoCoriTerritorio:=CNRCTB545.getIsAddTerritorio(tabella_cori(indice).tCdClassificazioneCori);

   -------------------------------------------------------------------------------------------------
   -- Controllo se deve essere attivata la gestione per aliquota in anagrafico o l'annualizzazione
   -- Per ora si esclude la gestione delle addizionali per territorio

   IF isTipoCoriIrpefScaglioni = 'Y' THEN

      -- Gestione per annualizzazione

      IF aRecTipoTrattamento.fl_irpef_annualizzata = 'Y' THEN
         isAnnualizzato:='2';
      END IF;

      -- Gestione per aliquota in anagrafico, comanda rispetto all'annualizzazione

      IF (aRecAnagrafico.aliquota_fiscale IS NOT NULL AND
          aRecAnagrafico.aliquota_fiscale > 0) THEN
         isAnnualizzato:='1';
      END IF;

      -- In caso di conguaglio o di gestioni per territorio si verifica se deve essere attivata la
      -- gestione di aliquota in anagrafico.
      -- Se tale gestione non �ttiva si ritorna 'N' in quanto, in questi casi, non deve essere
      -- gestita l'annualizzazione.

      IF (aOrigineCompenso=CNRCTB545.isCompensoConguaglio OR
          isTipoCoriTerritorio = 'Y') THEN
         IF isAnnualizzato = '1' THEN
            RETURN isAnnualizzato;
         ELSE
           isAnnualizzato:='N';
           RETURN isAnnualizzato;
         END IF;
      END IF;

      -- Gestione compensi da minicarriere a tassazione separata, comanda rispetto all'annualizzazione

      IF (aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
          aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong) THEN
         IF aRecCompenso.fl_compenso_mcarriera_tassep = 'Y' THEN
            IF aRecCompenso.aliquota_irpef_tassep = 0 THEN
               IBMERR001.RAISE_ERR_GENERICO
                  ('Prima di procedere al calcolo di un compenso derivante da minicarrera a tassazione separata ' ||
                   'deve essere stata calcolata l''aliquota media sulla minicarriera stessa');
            END IF;

            isAnnualizzato:='3';
         END IF;
      END IF;

      -- Gestione compensi da missione con lordizzazione

      IF (aOrigineCompenso=CNRCTB545.isCompensoMissione OR
          aOrigineCompenso=CNRCTB545.isCompensoMissioneAssCong) THEN
         IF aRecCompenso.aliquota_irpef_da_missione > 0 THEN
            isAnnualizzato:='4';
         END IF;
      END IF;

   END IF;

   RETURN isAnnualizzato;

END getRilevaAnnualizzato;


-- =================================================================================================
-- Calcolo della deduzione
-- =================================================================================================
PROCEDURE calcolaDeduzione
   (
    indice BINARY_INTEGER,
    inAnagrafico ANAGRAFICO%Rowtype,
    inCdsMcarriera MINICARRIERA.cd_cds%TYPE,
    inUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
    inPgMinicarriera MINICARRIERA.pg_minicarriera%Type,
    inRilevaAnnualizzato CHAR,
    inImportoRiferimento NUMBER,
    inImponibileLordo NUMBER,
    aTotDeduzioneGoduta NUMBER,
    aTotDeduzioneFamilyGoduta NUMBER,
    aNumGGTotaleMcarriera INTEGER,
    aNumGGProprioMcarriera INTEGER,
    aNumMMTotaleMcarriera INTEGER,
    aNumMMProprioMcarriera INTEGER,
    aNumMMEsercizio INTEGER,
    aImDeduzioneCori IN OUT NUMBER,
    aImDeduzioneLordoCori IN OUT NUMBER,
    aImDeduzioneFamilyCori IN OUT NUMBER,
    aImDeduzioneFamilyLordoCori IN OUT NUMBER
   ) IS

   aGGAnno INTEGER;
   aNumeroGG INTEGER;
   aNumeroMM INTEGER;
   aNumeroMMCompenso INTEGER;
   aMMAnno INTEGER;
   aValoreMaxDeduzione NUMBER(15,2);

   aDeduzioneTeorica NUMBER(15,2);
   aCoefficente NUMBER(15,4);

   aDeduzioneFamilyAnnua NUMBER(15,4);
   aDeduzioneFamilyPesata NUMBER(15,4);
   aCoefficenteFamily NUMBER(15,4);

   aDeduzione NUMBER(15,2);
   aDeduzioneLordo NUMBER(15,2);

   aDeduzioneFamily NUMBER(15,2);
   aDeduzioneFamilyLordo NUMBER(15,2);
   noDeduzioneIrpef Exception;
   noDeduzioneFamily Exception;
BEGIN

   aDeduzioneTeorica:=0;
   aCoefficente:=0;

   aDeduzioneFamilyAnnua:=0;
   aDeduzioneFamilyPesata:=0;
   aCoefficenteFamily:=0;

   aDeduzione:=0;
   aDeduzioneLordo:=0;
   aDeduzioneFamily:=0;
   aDeduzioneFamilyLordo:=0;

   IF aRecCompenso.fl_escludi_qvaria_deduzione = 'Y' THEN
      aValoreMaxDeduzione:=glbQuotaFissaDeduzione;
   ELSE
      aValoreMaxDeduzione:=(glbQuotaFissaDeduzione + glbQuotaVariabileDeduzione);
   END IF;
   BEGIN

      ----------------------------------------------------------------------------------------------
      -- Esclusione dal calcolo della deduzione

      -- Non si ha calcolo della deduzione in caso di compenso senza calcoli, di compensi con trattamento
      -- che prevede la gestione dell'aliquota in anagrafico, di compensi da minicarriera a tassazione
      -- separata e se il soggetto anagrafico ha scelto di non attivare la deduzione

      -- Non viene effettuato il calcolo delle deduzioni se nei parametri non �ttiva la gestione
      -- per l'esercizio di registrazione (aggiunta per gli adeguamenti alla Finanziaria 2007)
      -- (informazione gi�resente in glbFlNoTaxArea e glbFlNoFamilyArea)

      IF (aOrigineCompenso=CNRCTB545.isCompensoSenzaCalcoli OR
          inRilevaAnnualizzato = '1' OR
          inRilevaAnnualizzato = '3') Then
          If glbFlNoTaxArea = 'Y' Then
            aImDeduzioneCori:=aDeduzione;
            aImDeduzioneLordoCori:=aDeduzioneLordo;
          End If;
          If glbFlNoFamilyArea = 'Y' Then
            aImDeduzioneFamilyCori:=aDeduzioneFamily;
            aImDeduzioneFamilyLordoCori:=aDeduzioneFamilyLordo;
          End If;
          If glbFlNoFamilyArea = 'Y' And glbFlNoTaxArea = 'Y' Then
           Return;
          End If;
      END IF;
      -- Prima calcolo le deduzioni IRPEF
      If glbFlNoTaxArea != 'Y' Then
       Begin
        -- Non si ha calcolo della deduzione se non in presenza di cori IRPEF

        IF (tabella_cori(indice).tPgClassificazioneMontanti IS NULL OR
            (
                CNRCTB545.getIsIrpefScaglioni(tabella_cori(indice).tCdClassificazioneCori,
                                              tabella_cori(indice).tPgClassificazioneMontanti,
                                              tabella_cori(indice).tFlScriviMontanti) = 'N'
            )
           ) THEN
           aImDeduzioneCori:=aDeduzione;
           aImDeduzioneLordoCori:=aDeduzioneLordo;
           Raise noDeduzioneIrpef;
           --RETURN;
        END IF;

        -- Non si ha calcolo se la deduzione goduta �uperiore al massimale
        -- Il controllo si esegue solo in caso di compenso da minicarriera non annualizzato

        IF (inRilevaAnnualizzato!='2' AND
              (aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
               aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong)
           ) Then
           IF aTotDeduzioneGoduta >= aValoreMaxDeduzione THEN
              aImDeduzioneCori:=aDeduzione;
              aImDeduzioneLordoCori:=aDeduzioneLordo;
              Raise noDeduzioneIrpef;
              --RETURN;
           END IF;
        END IF;

        ----------------------------------------------------------------------------------------------
        -- Calcolo della deduzione. Si applica solo a compensi per conguaglio e da minicarriera

        IF (aOrigineCompenso=CNRCTB545.isCompensoConguaglio OR
            aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
            aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong) THEN

           -- Determinazione giorni. Se il compenso �a conguaglio o da minicarriera senza annualizzazione
           -- si mantiene il calcolo standard altrimenti i giorni di riferimento sono quelli complessivi delle
           -- rate di minicarriera

           aGGAnno:=IBMUTL001.getDaysBetween(TO_DATE('0101' || aRecCompenso.esercizio,'DDMMYYYY'),
                                             TO_DATE('3112' || aRecCompenso.esercizio,'DDMMYYYY'));

           IF (inRilevaAnnualizzato = '2' AND
                  (aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
                   aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong)
              ) THEN
              aNumeroGG:=aNumGGTotaleMcarriera;
           ELSE
              aNumeroGG:=aRecCompenso.numero_giorni;
           END IF;

           IF aNumeroGG > aGGAnno THEN
              aNumeroGG:=aGGAnno;
           END IF;

           -- Calcolo ammontare della deduzione teorica
           -- Se fl_escludi_qvaria_deduzione = 'Y' la deduzione �onteggiata solo sulla quota fissa.
           -- Se fl_intera_qfissa_deduzione = 'Y' la quota fissa della deduzione �alcolata in modo non
           -- pesato ai giorni di competenza del compenso

           IF aRecCompenso.fl_escludi_qvaria_deduzione = 'Y' THEN

              IF aRecCompenso.fl_intera_qfissa_deduzione = 'Y' THEN
                 aDeduzioneTeorica:=glbQuotaFissaDeduzione;
              ELSE
                 aDeduzioneTeorica:=((glbQuotaFissaDeduzione * aNumeroGG) / aGGAnno);
              END IF;

           ELSE

              IF aRecCompenso.fl_intera_qfissa_deduzione = 'Y' THEN
                 aDeduzioneTeorica:=(glbQuotaFissaDeduzione + ((glbQuotaVariabileDeduzione * aNumeroGG) / aGGAnno));
              ELSE
                 aDeduzioneTeorica:=(((glbQuotaFissaDeduzione + glbQuotaVariabileDeduzione) * aNumeroGG) / aGGAnno);
              END IF;

           END IF;

           -- Calcolo del coefficente. Si guarda al risultato di questo rapporto
           -- -- Se �aggiore o uguale a 1 la deduzione spetta per intero
           -- -- Se �n valore compreso tra 0 e 1 la deduzione spettante sar�ari alla percentuale che si
           --    ottiene dalle prime 4 cifre decimali del risultato applicata alla deduzione.
           -- -- Se �n valore negativo o pari a zero non spetta alcuna deduzione
           --
           -- Si modifica il calcolo della deduzione reale per il caso di compenso da minicarriera con annualizzazione
           -- e si calcolano entrambi i valori della deduzione prima e dopo la normalizzazione con il compenso (rata) in
           -- calcolo

           aCoefficente:=TRUNC(((glbBaseDeduzione + aDeduzioneTeorica - inImportoRiferimento) / glbBaseDeduzione),4);

           IF    aCoefficente >= 1 THEN
                 aDeduzione:=aDeduzioneTeorica;
                 aDeduzioneLordo:=aDeduzioneTeorica;
           ELSIF aCoefficente <= 0 THEN
                 aDeduzione:=0;
                 aDeduzioneLordo:=0;
           ELSE
                 IF (inRilevaAnnualizzato = '2' AND
                       (aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
                        aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong)
                    ) THEN
                    IF aNumeroGG = 0 THEN
                       aDeduzione:=0;
                    ELSE
                       aDeduzione:=ROUND(
                                          (
                                             (aDeduzioneTeorica * aCoefficente * aRecCompenso.numero_giorni) /
                                              aNumeroGG
                                            ),2
                                         );
                    END IF;
                    IF aTotDeduzioneGoduta >= aValoreMaxDeduzione THEN
                       aDeduzione:=0;
                    END IF;
                    aDeduzioneLordo:=ROUND((aDeduzioneTeorica * aCoefficente),2);
                 ELSE
                    aDeduzione:=ROUND((aDeduzioneTeorica * aCoefficente),2);
                    aDeduzioneLordo:=ROUND((aDeduzioneTeorica * aCoefficente),2);
                 END IF;
           END IF;

           -- Normalizzo la deduzione all'importo dell'imponibile cori lordo

           IF inImponibileLordo < aDeduzione THEN
              aDeduzione:=inImponibileLordo;
           END IF;

           -- Verifico che la deduzione calcolata non porti il goduto della stessa a superare il valore massimo
           -- Il controllo si esegue solo se non si tratta di compenso da conguaglio

           IF aOrigineCompenso!=CNRCTB545.isCompensoConguaglio THEN
              IF (aTotDeduzioneGoduta + aDeduzione) > aValoreMaxDeduzione THEN
                 aDeduzione:=aValoreMaxDeduzione - aTotDeduzioneGoduta;
                 IF aDeduzione < 0 THEN
                    aDeduzione:=0;
                 END IF;
              END IF;
           END IF;
        END IF;
        aImDeduzioneCori:=aDeduzione;
        aImDeduzioneLordoCori:=aDeduzioneLordo;
       Exception
         When noDeduzioneIrpef Then
           Null;
       End;
      End If; --  glbFlNoTaxArea != 'Y'
      -- Ora calcolo le deduzioni FAMILY
      If glbFlNoFamilyArea != 'Y' Then
       Begin
        If aOrigineCompenso=CNRCTB545.isCompensoConguaglio And
           CNRCTB080.getAnagImDeduzioniFam(aRecCompenso.esercizio,inAnagrafico.cd_anag) > 0 Then
          aImDeduzioneFamilyCori:=CNRCTB080.getAnagImDeduzioniFam(aRecCompenso.esercizio,inAnagrafico.cd_anag);
          aImDeduzioneFamilyLordoCori:=CNRCTB080.getAnagImDeduzioniFam(aRecCompenso.esercizio,inAnagrafico.cd_anag);
          Raise noDeduzioneFamily;
        End If;
        -- Non si ha calcolo della deduzione se non in presenza di cori IRPEF
        IF (tabella_cori(indice).tPgClassificazioneMontanti IS NULL OR
            (
                CNRCTB545.getIsIrpefScaglioni(tabella_cori(indice).tCdClassificazioneCori,
                                              tabella_cori(indice).tPgClassificazioneMontanti,
                                              tabella_cori(indice).tFlScriviMontanti) = 'N'
            )
           ) Then
           -- Marco 24/08/2005 Gestione dell'imponibile per il calcolo delle addizionali
           If Not(aOrigineCompenso=CNRCTB545.isCompensoConguaglio And
             (tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriAddReg Or
              tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriAddCom)) Then
             aImDeduzioneFamilyCori:=aDeduzioneFamily;
             aImDeduzioneFamilyLordoCori:=aDeduzioneFamilyLordo;
             Raise noDeduzioneFamily;
           End If;
        END IF;
        -- Non si ha calcolo se la deduzione goduta �uperiore al massimale
        -- Il controllo si esegue solo in caso di compenso da minicarriera non annualizzato

        IF (inRilevaAnnualizzato!='2' AND
              (aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
               aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong)
           ) Then
           IF aTotDeduzioneFamilyGoduta >= aValoreMaxDeduzione THEN
              aImDeduzioneFamilyCori:=aDeduzioneFamily;
              aImDeduzioneFamilyLordoCori:=aDeduzioneFamilyLordo;
              Raise noDeduzioneFamily;
           END IF;
        END IF;
        ----------------------------------------------------------------------------------------------
        -- Calcolo della deduzione. Si applica solo a compensi per conguaglio e da minicarriera

        IF (aOrigineCompenso=CNRCTB545.isCompensoConguaglio OR
            aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
            aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong) THEN

           -- Determinazione giorni. Se il compenso �a conguaglio o da minicarriera senza annualizzazione
           -- si mantiene il calcolo standard altrimenti i giorni di riferimento sono quelli complessivi delle
           -- rate di minicarriera

           aMMAnno:=IBMUTL001.getMonthsBetween(TO_DATE('0101' || aRecCompenso.esercizio,'DDMMYYYY'),
                                               TO_DATE('3112' || aRecCompenso.esercizio,'DDMMYYYY'));

           IF (inRilevaAnnualizzato = '2' AND
                  (aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
                   aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong)
              ) THEN
              aNumeroMM:=aNumMMTotaleMcarriera;
           Else
              /*In questo caso i Mesi sono clcolati in base all'esercizio del conguaglio */
              aNumeroMM:=aNumMMEsercizio;
           END IF;

           IF aNumeroMM > aMMAnno THEN
              aNumeroMM:=aMMAnno;
           END IF;
           aNumeroMMCompenso:=Trunc(IBMUTL001.getMonthsBetween(aRecCompenso.dt_da_competenza_coge,
                                                         aRecCompenso.dt_a_competenza_coge));
           IF aNumeroMMCompenso > aMMAnno THEN
              aNumeroMMCompenso:=aMMAnno;
           END IF;
           -- Calcolo ammontare della deduzione teorica
           -- pesato ai giorni di competenza del compenso

           aDeduzioneFamilyAnnua:= calcolaDeduzioneTeoricaFamily(inAnagrafico.cd_anag,
                                                            inCdsMcarriera,
                                                            inUoMcarriera,
                                                            inEsercizioMcarriera,
                                                            inPgMinicarriera);
           --pipe.send_message('aTotDeduzioneFamilyGoduta '||aTotDeduzioneFamilyGoduta);
           --pipe.send_message('DTA: '||aDeduzioneFamilyAnnua);
           aDeduzioneFamilyPesata := aDeduzioneFamilyAnnua * aNumeroMM/aMMAnno;
           --pipe.send_message('DTP: '||aDeduzioneFamilyPesata);

           -- Calcolo del coefficente. Si guarda al risultato di questo rapporto
           -- -- Se �aggiore o uguale a 1 la deduzione spetta per intero
           -- -- Se �n valore compreso tra 0 e 1 la deduzione spettante sar�ari alla percentuale che si
           --    ottiene dalle prime 4 cifre decimali del risultato applicata alla deduzione.
           -- -- Se �n valore negativo o pari a zero non spetta alcuna deduzione
           --
           -- Si modifica il calcolo della deduzione reale per il caso di compenso da minicarriera con annualizzazione
           -- e si calcolano entrambi i valori della deduzione prima e dopo la normalizzazione con il compenso (rata) in
           -- calcolo

           aCoefficenteFamily:=TRUNC(((glbBaseDeduzioneFamily + aDeduzioneFamilyPesata - inImportoRiferimento) / glbBaseDeduzioneFamily),4);

           IF    aCoefficenteFamily >= 1 THEN
                 aDeduzioneFamily:=aDeduzioneFamilyPesata;
                 aDeduzioneFamilyLordo:=aDeduzioneFamilyPesata;
           ELSIF aCoefficenteFamily <= 0 THEN
                 aDeduzioneFamily:=0;
                 aDeduzioneFamilyLordo:=0;
           Else
                 If (inRilevaAnnualizzato = '2' AND
                       (aOrigineCompenso=CNRCTB545.isCompensoMinicarriera OR
                        aOrigineCompenso=CNRCTB545.isCompensoMinicarrieraAssCong)
                    ) THEN
                    IF aNumeroMM = 0 THEN
                       aDeduzioneFamily:=0;
                    ELSE
                       aDeduzioneFamily:=ROUND(
                                          (
                                             (aDeduzioneFamilyPesata *
                                              aCoefficenteFamily *
                                              aNumeroMMCompenso) /
                                              aNumeroMM
                                            ),2
                                         );
                    END IF;
                    IF aTotDeduzioneFamilyGoduta >= aDeduzioneFamilyPesata THEN
                      aDeduzioneFamily:=0;
                    Else
                      If aDeduzioneFamily + aTotDeduzioneFamilyGoduta > aDeduzioneFamilyPesata Then
                        aDeduzioneFamily:= aDeduzioneFamilyPesata - aTotDeduzioneFamilyGoduta;
                      End If;
                    END IF;
                    aDeduzioneFamilyLordo:=ROUND((aDeduzioneFamilyPesata * aCoefficenteFamily),2);
                 ELSE
                    aDeduzioneFamily:=ROUND((aDeduzioneFamilyPesata * aCoefficenteFamily),2);
                    aDeduzioneFamilyLordo:=ROUND((aDeduzioneFamilyPesata * aCoefficenteFamily),2);
                 END IF;
           END IF;

           -- Normalizzo la deduzione all'importo dell'imponibile cori lordo

           IF inImponibileLordo < aDeduzioneFamily THEN
              aDeduzioneFamily:=inImponibileLordo;
           END IF;

           -- Verifico che la deduzione calcolata non porti il goduto della stessa a superare il valore massimo
           -- Il controllo si esegue solo se non si tratta di compenso da conguaglio

           IF aOrigineCompenso!=CNRCTB545.isCompensoConguaglio THEN
              IF (aTotDeduzioneFamilyGoduta + aDeduzioneFamily) > aDeduzioneFamilyPesata THEN
                 aDeduzioneFamily:=aDeduzioneFamilyPesata - aTotDeduzioneFamilyGoduta;
                 IF aDeduzioneFamily < 0 THEN
                    aDeduzioneFamily:=0;
                 END IF;
              END IF;
           END IF;
        END IF;
        aImDeduzioneFamilyCori:=aDeduzioneFamily;
        aImDeduzioneFamilyLordoCori:=aDeduzioneFamilyLordo;
       Exception
         When noDeduzioneFamily Then
           Null;
       End;
      End If; --  glbFlNoFamilyArea != 'Y'
   END;

END calcolaDeduzione;


-- =================================================================================================
-- Valorizza i dati per contributi ritenuta definiti per territorio. Funzione comune a tutti i calcoli
-- di contributo_ritenuta
-- =================================================================================================
PROCEDURE getDatiTerritorio
   (
    aCdRegione IN OUT COMPENSO.cd_regione_add%TYPE,
    aCdProvincia IN OUT COMPENSO.cd_provincia_add%TYPE,
    aPgComune IN OUT COMPENSO.pg_comune_add%TYPE,
    indice BINARY_INTEGER
   ) IS

BEGIN

   -- Default variabili

   aCdRegione:='*';
   aCdProvincia:='*';
   aPgComune:=0;

   BEGIN

      -- Valorizzazione dei dati di territorio per i contributi ritenuta per i quali questa
      -- gestione �ilevante

      IF tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriIrap THEN
         aCdRegione:=aRecCompenso.cd_regione_irap;
      END IF;

      IF tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriAddReg THEN
         aCdRegione:=aRecCompenso.cd_regione_add;
      END IF;

      IF tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriAddPro THEN
         aCdProvincia:=aRecCompenso.cd_provincia_add;
      END IF;

      IF tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriAddCom THEN
         aPgComune:=aRecCompenso.pg_comune_add;
      END IF;

   END;

END getDatiTerritorio;


-- =================================================================================================
-- Calcolo imponibile per ogni singolo cori del trattamento
-- =================================================================================================
FUNCTION getImponibileCori
   (
    indice BINARY_INTEGER
   ) RETURN NUMBER IS
   j BINARY_INTEGER;
   aImponibileBaseCori NUMBER(15,2);
   aImponibileRealeCori NUMBER(15,2);
   operatore NUMBER(15,2);
   aBloccoCalcolo VARCHAR2(5);

BEGIN

   aImponibileBaseCori:=0;
   aImponibileRealeCori:=0;

   Begin
      ----------------------------------------------------------------------------------------------
      -- Se il cori in elaborazione �i tipo INAIL ed �tato valorizzato l'imponibile da parte
      -- dell'utente ritorno come imponibile quello indicato dallo stesso utente e si applicato il
      -- calcolo ordinario

      IF (tabella_cori(indice).tCdClassificazioneCori = CNRCTB545.isCoriInail AND
          aRecCompenso.imponibile_inail > 0) THEN
         RETURN aRecCompenso.imponibile_inail;
      END IF;

      -- Se il cori �n credito irpef valorizzo l'imponibile direttamente a 0
      IF CNRCTB545.IsCoriCreditoIrpef(tabella_cori(indice).tCdCori) = 'Y' THEN
      --IF tabella_cori(indice).tFlCreditoIrpef = 'Y'  THEN
         RETURN aImponibileRealeCori;
      END IF;

      ----------------------------------------------------------------------------------------------
      -- Determino l'imponibile di base

      aImponibileBaseCori:=CNRCTB545.getImponibileBaseCori(aRecCompenso,
                                                           tabella_cori(indice).tCdClassificazioneCori);

      -- Se la regola di calcolo indica un algoritmo eseguo lo stesso

      IF tabella_cori(indice).tCalcoloImponibile = '000' THEN

         aImponibileRealeCori:=aImponibileBaseCori;

      ELSE

         aImponibileRealeCori:=aImponibileBaseCori;

         FOR j IN 1 .. (LENGTH(tabella_cori(indice).tCalcoloImponibile) / 5)

         LOOP

            aBloccoCalcolo:=SUBSTR(tabella_cori(indice).tCalcoloImponibile, ((j * 5) - 5 + 1), 5);
            IF SUBSTR(aBloccoCalcolo,1,1) = '+' THEN
               operatore:=1;
            ELSE
               operatore:=-1;
            END IF;

            IF    SUBSTR(aBloccoCalcolo,5,1) = 'P' THEN
                  aImponibileRealeCori:=
                      (
                         aImponibileRealeCori +
                         (NVL(tabella_cori(TO_NUMBER(SUBSTR(aBloccoCalcolo,2,3))).tAmmontarePercip,0) * operatore)
                      );
            ELSIF SUBSTR(aBloccoCalcolo,5,1) = 'E' THEN
                  aImponibileRealeCori:=
                     (
                         aImponibileRealeCori +
                         (NVL(tabella_cori(TO_NUMBER(SUBSTR(aBloccoCalcolo,2,3))).tAmmontareEnte,0) * operatore)
                     );
            ELSIF SUBSTR(aBloccoCalcolo,5,1) = '%' Then
                  aImponibileRealeCori:=
                     (
                         aImponibileRealeCori +
                         (aImponibileRealeCori * (NVL(To_Number(SUBSTR(aBloccoCalcolo,2,3)),0)) / 100 * operatore)
                     );
            ELSE  aImponibileRealeCori:=
                     (
                         aImponibileRealeCori +
                         (NVL(tabella_cori(TO_NUMBER(SUBSTR(aBloccoCalcolo,2,3))).tAmmontarePercip,0) * operatore) +
                         (NVL(tabella_cori(TO_NUMBER(SUBSTR(aBloccoCalcolo,2,3))).tAmmontareEnte,0) * operatore)
                     );

            END IF;

         END LOOP;

      END IF;

   END;

   RETURN aImponibileRealeCori;

END getImponibileCori;


-- =================================================================================================
-- Ritorna il montante di riferimento per ogni singolo cori del trattamento (dipendenti)
-- =================================================================================================
PROCEDURE getImportoMontanteDip
   (
    aClassificazioneMontanti TIPO_CONTRIBUTO_RITENUTA.pg_classificazione_montanti%TYPE,
    aMontanteLordo IN OUT NUMBER,
    aMontanteNetto IN OUT NUMBER,
    aImDeduzione IN OUT NUMBER
   ) IS

BEGIN

   BEGIN

      -- E' previsto il riferimento ad un montante

      IF aClassificazioneMontanti IS NOT NULL THEN
         aMontanteLordo:=tabella_montanti(aClassificazioneMontanti).tValoreLordoDip;
         aMontanteNetto:=tabella_montanti(aClassificazioneMontanti).tValoreNettoDip;
         aImDeduzione:=tabella_montanti(aClassificazioneMontanti).tImDeduzioneDip;
      END IF;

   END;

END getImportoMontanteDip;

-- =================================================================================================
-- Ritorna il montante di riferimento per ogni singolo cori del trattamento (altro)
-- =================================================================================================
PROCEDURE getImportoMontanteAltro
   (
    aClassificazioneMontanti TIPO_CONTRIBUTO_RITENUTA.pg_classificazione_montanti%TYPE,
    aMontanteLordo IN OUT NUMBER,
    aMontanteNetto IN OUT NUMBER,
    aImDeduzione IN OUT NUMBER,
    aImDeduzioneFamily IN OUT NUMBER,
    aMontanteLordoOcca IN OUT NUMBER,
    aMontanteNettoOcca IN OUT NUMBER) IS

BEGIN

   BEGIN

      -- E' previsto il riferimento ad un montante

      IF aClassificazioneMontanti IS NOT NULL THEN
         aMontanteLordo:=tabella_montanti(aClassificazioneMontanti).tValoreLordoAltro;
         aMontanteNetto:=tabella_montanti(aClassificazioneMontanti).tValoreNettoAltro;
         aImDeduzione:=tabella_montanti(aClassificazioneMontanti).tImDeduzioneAltro;
         aImDeduzioneFamily:=tabella_montanti(aClassificazioneMontanti).tImDeduzioneFamilyAltro;
         aMontanteNettoOcca := tabella_montanti(aClassificazioneMontanti).tValoreNettoOcca;
         aMontanteLordoOcca := tabella_montanti(aClassificazioneMontanti).tValoreLordoOcca;
      END IF;

   END;

END getImportoMontanteAltro;



-- =================================================================================================
-- Valorizzazione della matrice per il calcolo dei dettagli CORI di un compenso (dettaglio)
-- =================================================================================================
PROCEDURE costruisciTabellaCoriDet
   (
    indice BINARY_INTEGER,
    aImponibile CONTRIBUTO_RITENUTA_DET.imponibile%TYPE,
    aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE
   ) IS
   k BINARY_INTEGER;

BEGIN

   k:=tabella_cori_det.COUNT + 1;
   tabella_cori_det(k).tCdCori:=tabella_cori(indice).tCdCori;
   tabella_cori_det(k).tTiCassaCompetenza:=tabella_cori(indice).tTiCassaCompetenza;
   tabella_cori_det(k).tPrecisione:=tabella_cori(indice).tPrecisione;
   tabella_cori_det(k).tPgClassificazioneMontanti:=tabella_cori(indice).tPgClassificazioneMontanti;
   tabella_cori_det(k).tCdClassificazioneCori:=tabella_cori(indice).tCdClassificazioneCori;
   tabella_cori_det(k).tFlScriviMontanti:=tabella_cori(indice).tFlScriviMontanti;
   tabella_cori_det(k).tIdRiga:=tabella_cori(indice).tIdRiga;
   tabella_cori_det(k).tSegno:=tabella_cori(indice).tSegno;
   tabella_cori_det(k).tCalcoloImponibile:=tabella_cori(indice).tCalcoloImponibile;
   tabella_cori_det(k).tImponibileNetto:=NULL;
   tabella_cori_det(k).tAliquotaEnte:=NULL;
   tabella_cori_det(k).tBaseCalcoloEnte:=NULL;
   tabella_cori_det(k).tAmmontareEnte:=NULL;
   tabella_cori_det(k).tAliquotaPercip:=NULL;
   tabella_cori_det(k).tBaseCalcoloPercip:=NULL;
   tabella_cori_det(k).tAmmontarePercip:=NULL;

   tabella_cori_det(k).tImponibileNetto:=aImponibile;

   IF (aRecVPreScaglione.ti_ente_percipiente = 'P' OR
       aRecVPreScaglione.ti_ente_percipiente = '*') THEN
      tabella_cori_det(k).tAliquotaPercip:=aRecVPreScaglione.aliquota_percip;
      tabella_cori_det(k).tBaseCalcoloPercip:=aRecVPreScaglione.base_calcolo_percip;
      tabella_cori_det(k).tAmmontarePercip:=ROUND(aImponibile * (aRecVPreScaglione.base_calcolo_percip / 100) *
                                                  (aRecVPreScaglione.aliquota_percip / 100),2);
      tabella_cori(indice).tAliquotaPercip:=0;
      tabella_cori(indice).tBaseCalcoloPercip:=0;
      tabella_cori(indice).tAmmontarePercipLordo:=NVL(tabella_cori(indice).tAmmontarePercipLordo,0) +
                                                  tabella_cori_det(k).tAmmontarePercip;
      tabella_cori(indice).tAmmontarePercip:=tabella_cori(indice).tAmmontarePercipLordo;
   END IF;

   IF (aRecVPreScaglione.ti_ente_percipiente = 'E' OR
       aRecVPreScaglione.ti_ente_percipiente = '*') THEN
      tabella_cori_det(k).tAliquotaEnte:=aRecVPreScaglione.aliquota_ente;
      tabella_cori_det(k).tBaseCalcoloEnte:=aRecVPreScaglione.base_calcolo_ente;
      tabella_cori_det(k).tAmmontareEnte:=ROUND(aImponibile * (aRecVPreScaglione.base_calcolo_ente / 100) *
                                                (aRecVPreScaglione.aliquota_ente / 100),2);
      tabella_cori(indice).tAliquotaEnte:=0;
      tabella_cori(indice).tBaseCalcoloEnte:=0;
      tabella_cori(indice).tAmmontareEnteLordo:=NVL(tabella_cori(indice).tAmmontareEnteLordo,0) +
                                                tabella_cori_det(k).tAmmontareEnte;
      tabella_cori(indice).tAmmontareEnte:=tabella_cori(indice).tAmmontareEnteLordo;
   END IF;

END costruisciTabellaCoriDet;

-- =================================================================================================
-- Calcolo delle detrazioni familiari e personali
-- =================================================================================================
PROCEDURE calcolaDetrazioni
   (
    indice BINARY_INTEGER,
    aImportoRiferimento NUMBER,
    inCdsMcarriera MINICARRIERA.cd_cds%TYPE,
    inUoMcarriera MINICARRIERA.cd_unita_organizzativa%TYPE,
    inEsercizioMcarriera MINICARRIERA.esercizio%TYPE,
    inPgMinicarriera MINICARRIERA.pg_minicarriera%Type,
    inRilevaAnnualizzato CHAR,
    aNumMMTotaleMcarriera INTEGER,
    aNumMMEsercizio INTEGER
   ) IS
   aDataRif DATE;

   aNumeroMM INTEGER;
   aNumeroMMCompenso INTEGER;
   aMMAnno INTEGER;

   aImDetrazioniCoPesata NUMBER(15,4);
   aImDetrazioniFiPesata NUMBER(15,4);
   aImDetrazioniAlPesata NUMBER(15,4);
   aImDetrazioniRidCuneoPesata number(15,4);
   --aTotRedditoComplessivo  NUMBER(15,2);
Begin

   -- La data di accesso agli scaglioni delle detrazioni �mpostata di default pari a quella di
   -- registrazione del compenso mentre per la tassazione separata �ari alla data convenzionale
   -- del 31/12/(ESERCIZIOMINICARRIERA - 1)
   aDataRif:=aRecCompenso.dt_registrazione;
   IF (aRecCompenso.fl_compenso_minicarriera = 'Y' AND
       aRecCompenso.fl_compenso_mcarriera_tassep = 'Y' AND
       aRecCompenso.aliquota_irpef_tassep > 0) THEN
      aDataRif:=TO_DATE('3112' || (inEsercizioMcarriera - 1), 'DDMMYYYY');
   END IF;
   --aTotRedditoComplessivo := aImportoRiferimento
   --                          + glbRedditoComplessivo
    --                         + glbRedditoAbitazPrincipale;
--pipe.send_message('aTotRedditoComplessivo = '||aTotRedditoComplessivo);
   IF CNRCTB545.getIsIrpefScaglioni(tabella_cori(indice).tCdClassificazioneCori,
                                    tabella_cori(indice).tPgClassificazioneMontanti,
                                    tabella_cori(indice).tFlScriviMontanti) = 'Y' Then

      IF glbFlNoDetrazioniFamily != 'Y' And calcolaDetrazFamiliari = 'Y' THEN
         calcolaDetrazioniFam(aImportoRiferimento, --aTotRedditoComplessivo, --aImportoRiferimento,
                              aRecAnagrafico.cd_anag,
                              aRecCompenso.dt_da_competenza_coge,
                              aRecCompenso.dt_a_competenza_coge,
                              aDataRif,
                              aImDetrazioniCo,
                              aImDetrazioniFi,
                              aImDetrazioniAl,
                              aImDetrazioniFiS);
      END IF;

      If glbFlNoDetrazioniAltre != 'Y' And calcolaDetrazPersonali = 'Y' Then
             calcolaDetrazioniPer(aImportoRiferimento, --aTotRedditoComplessivo,  --aImportoRiferimento,
                              aRecAnagrafico.cd_anag,
                              aRecCompenso.dt_da_competenza_coge,
                              aRecCompenso.dt_a_competenza_coge,
                              aDataRif,
                              aRecCompenso.esercizio,
                              aImDetrazioniPe);
      End If;
      If glbFlDetrazioniAltriTipi = 'Y' And calcolaDetrazAltriTipi = 'Y' Then
             calcolaDetrazioniAltriTipi(aImportoRiferimento, --aTotRedditoComplessivo,   --aImportoRiferimento,
                              aRecAnagrafico.cd_anag,
                              aRecCompenso.dt_da_competenza_coge,
                              aRecCompenso.dt_a_competenza_coge,
                              aDataRif,
                              aRecCompenso.esercizio,
                              aImDetrazioniLa);
      End If;
   END IF;

END calcolaDetrazioni;

-- =================================================================================================
-- Calcolo delle detrazioni familiari
-- =================================================================================================
/* prima della Finanziaria 2007
PROCEDURE calcolaDetrazioniFam
   (
    aImportoRiferimento NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE,
    aDtCompetenzaDa DATE,
    aDtCompetenzaA DATE,
    aDtRegistrazione DATE,
    aImportoDetrazCo IN OUT NUMBER,
    aImportoDetrazFi IN OUT NUMBER,
    aImportoDetrazAl IN OUT NUMBER
   ) IS
   aDataRifDa DATE;
   aDataRifA DATE;
   aDifferenzaMesi INTEGER;
   mesiAnticipo INTEGER;
   aDataDa DATE;
   aDataA DATE;
   aNumeroRifPersona INTEGER;
   aImportoDetrazione number(15,2);
   memTiPersona CARICO_FAMILIARE_ANAG.ti_persona%TYPE;
   memCodiceFiscale CARICO_FAMILIARE_ANAG.codice_fiscale%TYPE;
   aChiaveTecnica CARICO_FAMILIARE_ANAG.codice_fiscale%TYPE;
   aContatore INTEGER;
   aNumero INTEGER;

   aRecCaricoFamAnag CARICO_FAMILIARE_ANAG%ROWTYPE;
   aRecDetrazioniFamiliari DETRAZIONI_FAMILIARI%ROWTYPE;

   gen_cur_car GenericCurTyp;

   -- Definizione tabella PL/SQL di appoggio calcolato detrazioni familiari

   indice BINARY_INTEGER;
   indice1 BINARY_INTEGER;

   tabella_num_car_fam CNRCTB545.numCaricoFamTab;
   tabella_car_fam_ok CNRCTB545.caricoFamTab;
   tabella_car_fam_tmp CNRCTB545.caricoFamTab;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Controllo che esistano carichi familiari

   IF (CNRCTB080.chkEsisteDetrazFam (aCdAnag,
                                     aDtCompetenzaDa,
                                     aDtCompetenzaA) = 0) THEN
     Return;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione variabili per il calcolo

   -- Azzeramento delle matrici dei carichi familiari (familiari e numero degli stessi per periodo)

   tabella_num_car_fam.DELETE;
   tabella_car_fam_ok.DELETE;
   tabella_car_fam_tmp.DELETE;

   indice:=0;
   indice1:=0;
   aContatore:=0;
   aChiaveTecnica:='TEMPKEY';

   -- Composizione dell'intervallo date in base alla competenza economica del compenso normalizzando le
   -- date di inizio e fine di competenza economica del compenso rispettivamente alla corrispondente data
   -- di inizio e fine mese

   aDataRifDa:=IBMUTL001.getFirstDayOfMonth(aDtCompetenzaDa);
   aDataRifA:=IBMUTL001.getLastDayOfMonth(aDtCompetenzaA);
   aDifferenzaMesi:=IBMUTL001.getMonthsBetween(aDataRifDa, aDataRifA);

   -- Costruzione a zero della matrice numero familiari per periodo (mese)

   FOR indice IN 1 .. aDifferenzaMesi

   LOOP

      IF indice = 1 THEN
         aDataDa:=aDataRifDa;
      ELSE
         aDataDa:=IBMUTL001.getAddMonth(aDataRifDa, indice - 1);
      END IF;
      aDataA:=IBMUTL001.getLastDayOfMonth(aDataDa);

      tabella_num_car_fam(indice).tDataDa:=aDataDa;
      tabella_num_car_fam(indice).tDataA:=aDataA;
      tabella_num_car_fam(indice).tNAltro:=0;
      tabella_num_car_fam(indice).tNConiuge:=0;
      tabella_num_car_fam(indice).tNFiglio:=0;

   END LOOP;

   -- Azzeramento campi di memoria per loop sui carichi familiari

   memTiPersona:=NULL;
   memCodiceFiscale:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Ciclo lettura carichi familiari. Si esclude il carico convenzionale per la gestione delle
   -- detrazioni per lavoro assimilato. L'intervallo data di validit� costretto nel periodo di
   -- competenza economica del compenso

   BEGIN

      OPEN gen_cur_car FOR

           SELECT *
           FROM   CARICO_FAMILIARE_ANAG
           WHERE  cd_anag = aCdAnag AND
                  ti_persona != 'L' AND
                  (
                      (dt_ini_validita <= aDataRifDa AND
                       dt_fin_validita >= aDataRifDa)
                   OR
                      (dt_ini_validita <= aDataRifA AND
                       dt_fin_validita >= aDataRifA)
                  )
           ORDER BY ti_persona, codice_fiscale, dt_ini_validita;

      LOOP

         FETCH gen_cur_car INTO
               aRecCaricoFamAnag;

         EXIT WHEN gen_cur_car%NOTFOUND;

         aContatore:=aContatore + 1;

         -- Normalizzazione date, costrizione nel periodo di competenza economica del compenso

         aDataDa:=IBMUTL001.getFirstDayOfMonth(aRecCaricoFamAnag.dt_ini_validita);
         aDataA:=IBMUTL001.getLastDayOfMonth(aRecCaricoFamAnag.dt_fin_validita);
         IF aDataDa < aDataRifDa THEN
            aDataDa:=aDataRifDa;
         END IF;
         IF aDataA > aDataRifA THEN
            aDataA:=aDataRifA;
         END IF;
         aRecCaricoFamAnag.dt_ini_validita:=aDataDa;
         aRecCaricoFamAnag.dt_fin_validita:=aDataA;

         -- Se il campo del codice fiscale non �alorizzato allora si considera ogni record come
         -- distinto dagli altri

         IF aRecCaricoFamAnag.codice_fiscale IS NULL THEN
            aRecCaricoFamAnag.codice_fiscale:=aChiaveTecnica || LPAD(aContatore,5,0);
         END IF;

         -- Se prima volta azzero tabella tmp

         IF memCodiceFiscale IS NULL THEN
            tabella_car_fam_tmp.DELETE;
            indice1:=0;
            memTiPersona:=aRecCaricoFamAnag.ti_persona;
            memCodiceFiscale:=aRecCaricoFamAnag.codice_fiscale;
         END IF;

         -- Il record letto �dentico al precedente

         IF (aRecCaricoFamAnag.ti_persona = memTiPersona AND
             aRecCaricoFamAnag.codice_fiscale = memCodiceFiscale) THEN
            riempiCarFamTmp (indice1,
                             aRecCaricoFamAnag,
                             tabella_car_fam_tmp);
         ELSE
            scaricaCarFamTmp(indice,
                             tabella_car_fam_ok,
                             tabella_car_fam_tmp);
            tabella_car_fam_tmp.DELETE;
            indice1:=0;
            memTiPersona:=aRecCaricoFamAnag.ti_persona;
            memCodiceFiscale:=aRecCaricoFamAnag.codice_fiscale;
            riempiCarFamTmp (indice1,
                             aRecCaricoFamAnag,
                             tabella_car_fam_tmp);
         END IF;

      END LOOP;

      CLOSE gen_cur_car;

      scaricaCarFamTmp(indice,
                       tabella_car_fam_ok,
                       tabella_car_fam_tmp);

      -- Aggiornamento della matrice numeri persone per periodo

      BEGIN

         FOR indice IN tabella_car_fam_ok.FIRST .. tabella_car_fam_ok.LAST

         LOOP

            FOR indice1 IN tabella_num_car_fam.FIRST .. tabella_num_car_fam.LAST

            LOOP

               IF (tabella_car_fam_ok(indice).tDataDa <= tabella_num_car_fam(indice1).tDataDa AND
                   tabella_car_fam_ok(indice).tDataA >= tabella_num_car_fam(indice1).tDataA ) THEN
                   IF    tabella_car_fam_ok(indice).tTiPersona = 'A' THEN
                         tabella_num_car_fam(indice1).tNAltro:=tabella_num_car_fam(indice1).tNAltro + 1;
                   ELSIF tabella_car_fam_ok(indice).tTiPersona = 'C' THEN
                         tabella_num_car_fam(indice1).tNConiuge:=tabella_num_car_fam(indice1).tNConiuge + 1;
                   ELSIF tabella_car_fam_ok(indice).tTiPersona = 'F' THEN
                         tabella_num_car_fam(indice1).tNFiglio:=tabella_num_car_fam(indice1).tNFiglio + 1;
                   END IF;
               END IF;

            END LOOP;

         END LOOP;

      END;

   END;

      -- Calcolo delle detrazioni


   BEGIN

      FOR indice IN tabella_car_fam_ok.FIRST .. tabella_car_fam_ok.LAST

      LOOP

         FOR indice1 IN tabella_num_car_fam.FIRST .. tabella_num_car_fam.LAST

         LOOP

            IF (tabella_car_fam_ok(indice).tDataDa <= tabella_num_car_fam(indice1).tDataDa AND
                tabella_car_fam_ok(indice).tDataA >= tabella_num_car_fam(indice1).tDataA ) THEN

               BEGIN

                  IF    tabella_car_fam_ok(indice).tTiPersona = 'A' THEN
                        aNumero:=1;
                  ELSIF tabella_car_fam_ok(indice).tTiPersona = 'C' THEN
                        aNumero:=1;
                  ELSIF tabella_car_fam_ok(indice).tTiPersona = 'F' THEN
                        aNumero:=tabella_num_car_fam(indice1).tNFiglio;
                  END IF;


                  SELECT * into aRecDetrazioniFamiliari
                  FROM   DETRAZIONI_FAMILIARI
                  WHERE  ti_persona = tabella_car_fam_ok(indice).tTiPersona AND
                         numero = aNumero AND
                         dt_inizio_validita <= aDtRegistrazione AND
                         dt_fine_validita >= aDtRegistrazione AND
                         im_inferiore <= aImportoRiferimento AND
                         im_superiore >= aImportoRiferimento;

                EXCEPTION

                   WHEN no_data_found THEN
                        IBMERR001.RAISE_ERR_GENERICO
                        ('Record scaglione per detrazioni familiari non definito');

                END;

                aImportoDetrazione:= ROUND(((aRecDetrazioniFamiliari.im_detrazione / 12) * tabella_car_fam_ok(indice).tPrcCarico / 100),2);

                IF    tabella_car_fam_ok(indice).tTiPersona = 'A' THEN
                      aImportoDetrazAl:=aImportoDetrazAl + aImportoDetrazione;
                ELSIF tabella_car_fam_ok(indice).tTiPersona = 'C' THEN
                      aImportoDetrazCo:=aImportoDetrazCo + aImportoDetrazione;
                ELSIF tabella_car_fam_ok(indice).tTiPersona = 'F' THEN
                      aImportoDetrazFi:=aImportoDetrazFi + aImportoDetrazione;
                END IF;

            END IF;

         END LOOP;

      END LOOP;

   END;

END calcolaDetrazioniFam;
*/
-- =================================================================================================
-- Valorizzazione della tabella temporanea di cumulo dei carichi familiari
-- =================================================================================================
PROCEDURE riempiCarFamTmp
   (
    indice1 IN OUT INTEGER,
    aRecCaricoFamAnag CARICO_FAMILIARE_ANAG%ROWTYPE,
    tabella_car_fam_tmp IN OUT CNRCTB545.caricoFamTab
    ) IS

BEGIN

   indice1:=indice1 + 1;

   tabella_car_fam_tmp(indice1).tTiPersona:=aRecCaricoFamAnag.ti_persona;
   tabella_car_fam_tmp(indice1).tCodiceFiscale:=aRecCaricoFamAnag.codice_fiscale;
   tabella_car_fam_tmp(indice1).tDataDa:=aRecCaricoFamAnag.dt_ini_validita;
   tabella_car_fam_tmp(indice1).tDataA:=aRecCaricoFamAnag.dt_fin_validita;
   tabella_car_fam_tmp(indice1).tPrcCarico:=aRecCaricoFamAnag.prc_carico;
   tabella_car_fam_tmp(indice1).tFlPrimoFiglio:=aRecCaricoFamAnag.fl_primo_figlio;
   tabella_car_fam_tmp(indice1).tFlHandicap:=aRecCaricoFamAnag.fl_handicap;
   tabella_car_fam_tmp(indice1).tDtMinoreTre:=aRecCaricoFamAnag.dt_fine_figlio_ha_treanni;
   tabella_car_fam_tmp(indice1).tFlPrimoFiglioMancaCon:=aRecCaricoFamAnag.fl_primo_figlio_manca_con;
   tabella_car_fam_tmp(indice1).tNumeroMesi:=IBMUTL001.getMonthsBetween(tabella_car_fam_tmp(indice1).tDataDa,
                                                                        tabella_car_fam_tmp(indice1).tDataA);
--pipe.send_message('tabella_car_fam_tmp');
--pipe.send_message('tTiPersona = '||tabella_car_fam_tmp(indice1).tTiPersona);
--pipe.send_message('tDataDa = '||tabella_car_fam_tmp(indice1).tDataDa);
--pipe.send_message('tDataA = '||tabella_car_fam_tmp(indice1).tDataA);

END riempiCarFamTmp;

-- =================================================================================================
-- Scarico della tabella temporanea di cumulo dei carichi familiari su quella effettiva con normalizzazione
-- delle date a 12 mesi
-- =================================================================================================
PROCEDURE scaricaCarFamTmp
   (
    indice IN OUT INTEGER,
    tabella_car_fam_ok IN OUT CNRCTB545.caricoFamTab,
    tabella_car_fam_tmp IN OUT CNRCTB545.caricoFamTab
    ) IS
   k BINARY_INTEGER;
   aNumeroMesi INTEGER;

BEGIN

   FOR k IN tabella_car_fam_tmp.FIRST .. tabella_car_fam_tmp.LAST

   LOOP

      -- Normalizzazione date a 12 mesi

      IF tabella_car_fam_tmp(k).tNumeroMesi > 12 THEN
         tabella_car_fam_tmp(k).tNumeroMesi:=12;
         tabella_car_fam_tmp(k).tDataA:=(IBMUTL001.getAddMonth(tabella_car_fam_tmp(k).tDataDa, tabella_car_fam_tmp(k).tNumeroMesi) - 1);
      END IF;

      IF k > 1 THEN
         RETURN;
      END IF;

      indice:=indice + 1;

      tabella_car_fam_ok(indice).tTiPersona:=tabella_car_fam_tmp(k).tTiPersona;
      tabella_car_fam_ok(indice).tCodiceFiscale:=tabella_car_fam_tmp(k).tCodiceFiscale;
      tabella_car_fam_ok(indice).tDataDa:=tabella_car_fam_tmp(k).tDataDa;
      tabella_car_fam_ok(indice).tDataA:=tabella_car_fam_tmp(k).tDataA;
      tabella_car_fam_ok(indice).tPrcCarico:=tabella_car_fam_tmp(k).tPrcCarico;
      tabella_car_fam_ok(indice).tFlPrimoFiglio:=tabella_car_fam_tmp(k).tFlPrimoFiglio;
      tabella_car_fam_ok(indice).tFlPrimoFiglioMancaCon:=tabella_car_fam_tmp(k).tFlPrimoFiglioMancaCon;
      tabella_car_fam_ok(indice).tFlHandicap:=tabella_car_fam_tmp(k).tFlHandicap;
      tabella_car_fam_ok(indice).tDtMinoreTre:=tabella_car_fam_tmp(k).tDtMinoreTre;
      tabella_car_fam_ok(indice).tNumeroMesi:=tabella_car_fam_tmp(k).tNumeroMesi;
   END LOOP;


END scaricaCarFamTmp;

-- =================================================================================================
-- Calcolo delle detrazioni personali
-- =================================================================================================
/* prima della Finanziaria 2007
PROCEDURE calcolaDetrazioniPer
   (
    aImportoRiferimento NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE,
    aDtCompetenzaDa DATE,
    aDtCompetenzaA DATE,
    aDtRegistrazione DATE,
    aEsercizio COMPENSO.esercizio%TYPE,
    aImportoDetrazPe IN OUT NUMBER
   ) IS
   aNumeroGG INTEGER;
   aGGAnno INTEGER;
   importoDetrazione NUMBER(15,2);
   aRecDetrazioniLavoro DETRAZIONI_LAVORO%ROWTYPE;
   aEsercizioRif COMPENSO.esercizio%TYPE;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Determinazione giorni

   aEsercizioRif:=aEsercizio;
   IF aEsercizioRif != TO_NUMBER(TO_CHAR(aDtRegistrazione,'YYYY')) THEN
      aEsercizioRif:= TO_NUMBER(TO_CHAR(aDtRegistrazione,'YYYY'));
   END IF;

   aGGAnno:=IBMUTL001.getDaysBetween(TO_DATE('0101' || aEsercizioRif,'DDMMYYYY'),
                                     TO_DATE('3112' || aEsercizioRif,'DDMMYYYY'));
   aNumeroGG:=IBMUTL001.getDaysBetween(aDtCompetenzaDa, aDtCompetenzaA);
   IF aNumeroGG > aGGAnno THEN
      aNumeroGG:=aGGAnno;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Calcolo delle detrazioni

   BEGIN

      SELECT * INTO aRecDetrazioniLavoro
      FROM   DETRAZIONI_LAVORO
      WHERE  ti_lavoro = 'D' AND
             dt_inizio_validita <= aDtRegistrazione AND
             dt_fine_validita >= aDtRegistrazione AND
             im_inferiore <= aImportoRiferimento AND
             im_superiore >= aImportoRiferimento;

   EXCEPTION

      WHEN no_data_found THEN
           IBMERR001.RAISE_ERR_GENERICO
              ('Record scaglione per detrazioni personali non definito');

   END;

   IF aNumeroGG = aGGAnno THEN
      aImportoDetrazPe:=aRecDetrazioniLavoro.im_detrazione;
   ELSE
      aImportoDetrazPe:=ROUND(((aRecDetrazioniLavoro.im_detrazione / aGGAnno) * aNumeroGG),2);
   END IF;

END calcolaDetrazioniPer;
*/
-- =================================================================================================
-- Scrittura dettaglio compenso in calcolo
-- =================================================================================================
PROCEDURE scriviDettaglioCompenso
   IS
   i BINARY_INTEGER;
   K BINARY_INTEGER;
   totNettizza NUMBER(15,2);
   aTotDetrazioni NUMBER(15,2);

   imCORIPercipiente NUMBER(15,2);
   imCORIEnte NUMBER(15,2);
   imAggiungoNettoPercip NUMBER(15,2);
   aImponibileIrpefLordo NUMBER(15,2);
   aImponibileIrpefNetto NUMBER(15,2);
   aImportoDeduzioneIrpef NUMBER(15,2);
   aImportoIrpef NUMBER(15,2);

   -- Variabili per controllo ammissibilit�el compenso

   imCORIPercipPiu NUMBER(15,2);
   imCORIPercipMeno NUMBER(15,2);
   contaCORIPercipPiu INTEGER;
   contaCORIPercipMeno INTEGER;

   imCORIEntePiu NUMBER(15,2);
   imCORIEnteMeno NUMBER(15,2);
   contaCORIEntePiu INTEGER;
   contaCORIEnteMeno INTEGER;

   SCRIVI_CUNEO VARCHAR2(1) := 'S';
   imTotaleCompenso NUMBER(15,2);
   imNettoCompenso NUMBER(15,2);
   imNettoCompensoCalcolato NUMBER(15,2);

   aDataSospensioneMin  date;
   aDataSospensioneMax  date;

   aStrDefault VARCHAR2(100);

   aRiga CONTRIBUTO_RITENUTA_DET.pg_riga%TYPE;

   aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE;
   aRecContributoRitenutaDet CONTRIBUTO_RITENUTA_DET%ROWTYPE;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Determino il totale delle detrazioni

   aImDetrazioniPeNetto:=aImDetrazioniPe;
   aImDetrazioniLaNetto:=aImDetrazioniLa;
   aImDetrazioniCoNetto:=aImDetrazioniCo;
   aImDetrazioniFiNetto:=aImDetrazioniFi;
   aImDetrazioniAlNetto:=aImDetrazioniAl;
   aImDetrazioniRidCuneoNetto:=aImDetrazioniRidCuneo;
   aTotDetrazioni:=aImDetrazioniPe + aImDetrazioniLa + aImDetrazioniCo + aImDetrazioniFi + aImDetrazioniAl + aImDetrazioniRidCuneo;

   -- Cancellazione dei dettagli preesistenti

   CNRCTB545.cancellaDetCompenso(aRecCompenso.cd_cds,
                                 aRecCompenso.cd_unita_organizzativa,
                                 aRecCompenso.esercizio,
                                 aRecCompenso.pg_compenso);

   -- Azzeramento variabili

   aRiga:=0;
   imCORIPercipiente:=0;
   imCORIEnte:=0;
   imAggiungoNettoPercip:=0;

   imCORIPercipPiu:=0;
   imCORIPercipMeno:=0;
   contaCORIPercipPiu:=0;
   contaCORIPercipMeno:=0;

   imCORIEntePiu:=0;
   imCORIEnteMeno:=0;
   contaCORIEntePiu:=0;
   contaCORIEnteMeno:=0;

   imTotaleCompenso:=0;
   imNettoCompenso:=0;
   imNettoCompensoCalcolato:=0;


   aRecContributoRitenuta:=NULL;
   aRecContributoRitenutaDet:=NULL;

   -- Azzeramento dati per imponibile fiscale

   aImponibileIrpefLordo:=0;
   aImponibileIrpefNetto:=0;
   aImportoDeduzioneIrpef:=0;
   aImportoIrpef:=0;

   aDataSospensioneMin := Trunc(CNRCTB015.getDt01PerChiave('SOSPENSIONE_IRPEF', 'PERIODO_VALIDITA'));
   aDataSospensioneMax := Trunc(CNRCTB015.getDt02PerChiave('SOSPENSIONE_IRPEF', 'PERIODO_VALIDITA'));

   BEGIN

      ----------------------------------------------------------------------------------------------
    -- SEGNA DI EVITARE DI SCRIVERE IL CUNEO PER IL PAREGGIO DETRAZIONI
      k := 0;
      if aTotDetrazioni <= 0 then
          FOR K IN tabella_cori.FIRST .. tabella_cori.LAST LOOP
            IF tabella_cori(K).tCreditoPareggioDetrazioni = 'Y' THEN
                SCRIVI_CUNEO := 'N';
            END IF;
          END LOOP;
      end if;
      -- Scrittura CONTRIBUTO_RITENUTA
      FOR i IN tabella_cori.FIRST .. tabella_cori.LAST
      LOOP
         IF tabella_cori(i).tCreditoPareggioDetrazioni IS NULL OR tabella_cori(i).tCreditoPareggioDetrazioni = 'N' OR SCRIVI_CUNEO = 'S' THEN
             aRecContributoRitenuta.cd_cds:=aRecCompenso.cd_cds;
             aRecContributoRitenuta.cd_unita_organizzativa:=aRecCompenso.cd_unita_organizzativa;
             aRecContributoRitenuta.esercizio:=aRecCompenso.esercizio;
             aRecContributoRitenuta.pg_compenso:=aRecCompenso.pg_compenso;
             aRecContributoRitenuta.cd_contributo_ritenuta:=tabella_cori(i).tCdCori;
             aRecContributoRitenuta.dt_ini_validita:=tabella_cori(i).tDtIniValCori;
             aRecContributoRitenuta.montante:=tabella_cori(i).tMontante;
             aRecContributoRitenuta.imponibile_lordo:=tabella_cori(i).tImponibileLordo;
             aRecContributoRitenuta.im_deduzione_irpef:=tabella_cori(i).tImDeduzioneIrpef;
             aRecContributoRitenuta.im_deduzione_family:=tabella_cori(i).tImDeduzioneFamily;
             aRecContributoRitenuta.imponibile:=tabella_cori(i).tImponibileNetto;
             aRecContributoRitenuta.stato_cofi_cr:='N';
             aRecContributoRitenuta.dacr:=dataOdierna;
             aRecContributoRitenuta.utcr:=aRecCompenso.utcr;
             aRecContributoRitenuta.duva:=dataOdierna;
             aRecContributoRitenuta.utuv:=aRecCompenso.utuv;
             aRecContributoRitenuta.pg_ver_rec:=aRecCompenso.pg_ver_rec;
             aRecContributoRitenuta.fl_credito_pareggio_detrazioni := tabella_cori(i).tCreditoPareggioDetrazioni;
             -- Parte percipiente ----------------------------------------------------------------------
             IF tabella_cori(i).tAmmontarePercip IS NOT NULL AND aRecTipoTrattamento.fl_solo_inail_ente != 'Y' THEN
                aRecContributoRitenuta.ti_ente_percipiente:='P';
                aRecContributoRitenuta.aliquota:=tabella_cori(i).tAliquotaPercip;
                aRecContributoRitenuta.base_calcolo:=tabella_cori(i).tBaseCalcoloPercip;
                aRecContributoRitenuta.ammontare_lordo:=tabella_cori(i).tAmmontarePercipLordo;
                -- Se il CORI rappresenta IRPEF a scaglioni allora
                -- -- memorizzo dati per imponibile fiscale per compenso
                -- -- nettizzo IRPEF delle detrazioni. Se il valore �egativo normalizzo a zero

                IF CNRCTB545.getIsIrpefScaglioni(tabella_cori(i).tCdClassificazioneCori,
                                                 tabella_cori(i).tPgClassificazioneMontanti,
                                                 tabella_cori(i).tFlScriviMontanti) = 'Y' THEN

                   aImponibileIrpefLordo:=aRecContributoRitenuta.imponibile_lordo;
                   aImponibileIrpefNetto:=aRecContributoRitenuta.imponibile;
                   aImportoDeduzioneIrpef:=aRecContributoRitenuta.im_deduzione_irpef;

                   tabella_cori(i).tAmmontarePercip:=tabella_cori(i).tAmmontarePercip - aTotDetrazioni;
                   aImportoIrpef:=tabella_cori(i).tAmmontarePercip;
                   IF tabella_cori(i).tAmmontarePercip < 0 THEN
                      totNettizza:=tabella_cori(i).tAmmontarePercip * -1;
                      tabella_cori(i).tAmmontarePercip:=0;
                      k := 0;
                      FOR K IN tabella_cori.FIRST .. tabella_cori.LAST LOOP
                        IF tabella_cori(K).tCreditoPareggioDetrazioni = 'Y' THEN
                            IF totNettizza <= tabella_cori(K).tAmmontarePercip * -1 THEN
                                tabella_cori(K).tAmmontarePercip := totNettizza * -1;
                                tabella_cori(K).tAmmontarePercipLordo := totNettizza * -1;
                            END IF;
                        END IF;
                      END LOOP;

                      nettizzaDetrazioni(totNettizza);
                   ELSE
    -- SEGNA DI EVITARE DI SCRIVERE IL CUNEO PER IL PAREGGIO DETRAZIONI
                      k := 0;
                      FOR K IN tabella_cori.FIRST .. tabella_cori.LAST LOOP
                        IF tabella_cori(K).tCreditoPareggioDetrazioni = 'Y' THEN
                          SCRIVI_CUNEO := 'N';
                        END IF;
                      END LOOP;
                   END IF;
                END IF;

                -- Scrittura importo netto
                -- Se il compenso �a conguaglio nel caso in cui l'importo irpef sia = 0 non si deve
                -- calcolare nulla per addizionali territorio

                IF (aOrigineCompenso = CNRCTB545.isCompensoConguaglio AND
                    CNRCTB545.getIsAddTerritorio(tabella_cori(i).tCdClassificazioneCori) = 'Y') THEN

                   IF aImportoIrpef = 0 THEN
                      tabella_cori(i).tImponibileLordo:=0;
                      tabella_cori(i).tImponibileNetto:=0;
                      tabella_cori(i).tAmmontarePercipLordo:=0;
                      tabella_cori(i).tAmmontarePercip:=0;
                      aRecContributoRitenuta.imponibile_lordo:=tabella_cori(i).tImponibileLordo;
                      aRecContributoRitenuta.imponibile:=tabella_cori(i).tImponibileNetto;
                      aRecContributoRitenuta.ammontare_lordo:=tabella_cori(i).tAmmontarePercipLordo;
                   END IF;

                END IF;

                aRecContributoRitenuta.ammontare:=tabella_cori(i).tAmmontarePercip;

    --pipe.send_message('aRecContributoRitenuta.cd_contributo_ritenuta '||aRecContributoRitenuta.cd_contributo_ritenuta);
    --pipe.send_message('aRecContributoRitenuta.ammontare '||aRecContributoRitenuta.ammontare);


          -- Dopo aver calcolato l'ammontare netto
          -- Se il terzo �oggetto a sospensione e se il CORI �oggetto a sospensione
          -- viene spostato l'ammontare nell'importo sospeso e impostato a zero l'ammontare stesso
          If (aOrigineCompenso != CNRCTB545.isCompensoConguaglio And
              tabella_cori(i).tFlSospensioneIrpef Is Not Null And
              tabella_cori(i).tFlSospensioneIrpef = 'Y') Then
             If aRecAnagrafico.fl_sospensione_irpef = 'Y' And
                dataOdierna >= aDataSospensioneMin And
                dataOdierna <= aDataSospensioneMax Then
                    aRecContributoRitenuta.im_cori_sospeso := aRecContributoRitenuta.ammontare;
                    aRecContributoRitenuta.ammontare:= 0;
             Else
                aRecContributoRitenuta.im_cori_sospeso := 0;
             End If;
          Else
             aRecContributoRitenuta.im_cori_sospeso := 0;
          End If;

                -- Totalizzazione del valore complessivo dei CORI carico percipiente

                imCORIPercipiente:=imCORIPercipiente + aRecContributoRitenuta.ammontare;
                IF aRecContributoRitenuta.ammontare < 0 THEN
                   imCORIPercipMeno:=imCORIPercipMeno + aRecContributoRitenuta.ammontare;
                   contaCORIPercipMeno:=contaCORIPercipMeno + 1;
                END IF;
                IF aRecContributoRitenuta.ammontare > 0 THEN
                   imCORIPercipPiu:=imCORIPercipPiu + aRecContributoRitenuta.ammontare;
                   contaCORIPercipPiu:=contaCORIPercipPiu + 1;
                END IF;

                -- scrittura record CONTRIBUTO_RITENUTA

                CNRCTB545.insContributoRitenuta (aRecContributoRitenuta);

             END IF;

             -- Parte ente -----------------------------------------------------------------------------

             IF tabella_cori(i).tAmmontareEnte IS NOT NULL THEN
                aRecContributoRitenuta.ti_ente_percipiente:='E';
                aRecContributoRitenuta.aliquota:=tabella_cori(i).tAliquotaEnte;
                aRecContributoRitenuta.base_calcolo:=tabella_cori(i).tBaseCalcoloEnte;
                aRecContributoRitenuta.ammontare_lordo:=tabella_cori(i).tAmmontareEnteLordo;
                aRecContributoRitenuta.ammontare:=tabella_cori(i).tAmmontareEnte;

                -- Totalizzazione del valore complessivo dei CORI carico ente.

                imCORIEnte:=imCORIEnte + tabella_cori(i).tAmmontareEnte;
                IF tabella_cori(i).tAmmontareEnte < 0 THEN
                   imCORIEnteMeno:=imCORIEnteMeno + tabella_cori(i).tAmmontareEnte;
                   contaCORIEnteMeno:=contaCORIEnteMeno + 1;
                END IF;
                IF tabella_cori(i).tAmmontareEnte > 0 THEN
                   imCORIEntePiu:=imCORIEntePiu + tabella_cori(i).tAmmontareEnte;
                   contaCORIEntePiu:=contaCORIEntePiu + 1;
                END IF;

                -- Totalizzazione degli importi di rivalsa e iva da sommare al netto percipiente

                IF (tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriIva OR
                    tabella_cori(i).tCdClassificazioneCori = CNRCTB545.isCoriRivalsa) THEN
                   imAggiungoNettoPercip:=imAggiungoNettoPercip + tabella_cori(i).tAmmontareEnte;
                END IF;

          -- La sospensione �alida solo per i cori a carico percipiente
          aRecContributoRitenuta.im_cori_sospeso := 0;

                -- scrittura record CONTRIBUTO_RITENUTA

                CNRCTB545.insContributoRitenuta(aRecContributoRitenuta);
             END IF;
        END IF;
      END LOOP;

      ----------------------------------------------------------------------------------------------
      -- Scrittura CONTRIBUTO_RITENUTA_DET

      IF tabella_cori_det.COUNT > 0 THEN

         FOR i IN tabella_cori_det.FIRST .. tabella_cori_det.LAST

         LOOP

            aRecContributoRitenutaDet.cd_cds:=aRecCompenso.cd_cds;
            aRecContributoRitenutaDet.cd_unita_organizzativa:=aRecCompenso.cd_unita_organizzativa;
            aRecContributoRitenutaDet.esercizio:=aRecCompenso.esercizio;
            aRecContributoRitenutaDet.pg_compenso:=aRecCompenso.pg_compenso;
            aRecContributoRitenutaDet.cd_contributo_ritenuta:=tabella_cori_det(i).tCdCori;
            aRecContributoRitenutaDet.imponibile:=tabella_cori_det(i).tImponibileNetto;
            aRecContributoRitenutaDet.dacr:=dataOdierna;
            aRecContributoRitenutaDet.utcr:=aRecCompenso.utcr;
            aRecContributoRitenutaDet.duva:=dataOdierna;
            aRecContributoRitenutaDet.utuv:=aRecCompenso.utuv;
            aRecContributoRitenutaDet.pg_ver_rec:=aRecCompenso.pg_ver_rec;

            -- Parte percipiente

            IF tabella_cori_det(i).tAmmontarePercip IS NOT NULL THEN
               aRiga:=aRiga + 1;
               aRecContributoRitenutaDet.pg_riga:=aRiga;
               aRecContributoRitenutaDet.ti_ente_percipiente:='P';
               aRecContributoRitenutaDet.aliquota:=tabella_cori_det(i).tAliquotaPercip;
               aRecContributoRitenutaDet.base_calcolo:=tabella_cori_det(i).tBaseCalcoloPercip;
               aRecContributoRitenutaDet.ammontare:=tabella_cori_det(i).tAmmontarePercip;

               CNRCTB545.insContributoRitenutaDet (aRecContributoRitenutaDet);
            END IF;

            -- Parte ente

            IF tabella_cori_det(i).tAmmontareEnte IS NOT NULL Then
               --in una sola riga di tabella_cori_det ci sono sia i dati a carico ente che a carico percipiente
               --quindi non incremento aRiga se ho gia' inserito la riga relativa per il Percipiente
               if tabella_cori_det(i).tAmmontarePercip IS NULL then
                   aRiga:=aRiga + 1;
               end if;
               aRecContributoRitenutaDet.pg_riga:=aRiga;
               aRecContributoRitenutaDet.ti_ente_percipiente:='E';
               aRecContributoRitenutaDet.aliquota:=tabella_cori_det(i).tAliquotaEnte;
               aRecContributoRitenutaDet.base_calcolo:=tabella_cori_det(i).tBaseCalcoloEnte;
               aRecContributoRitenutaDet.ammontare:=tabella_cori_det(i).tAmmontareEnte;

               CNRCTB545.insContributoRitenutaDet (aRecContributoRitenutaDet);
            END IF;

         END LOOP;

      END IF;

      ----------------------------------------------------------------------------------------------
      -- Totalizzazione dati del compenso

      imTotaleCompenso:=aRecCompenso.im_lordo_percipiente + imCORIEnte;
      imNettoCompenso:=aRecCompenso.im_lordo_percipiente - imCORIPercipiente + imAggiungoNettoPercip;
      imNettoCompensoCalcolato:=imTotaleCompenso - imCORIEntePiu - imCORIPercipPiu;

      ----------------------------------------------------------------------------------------------
      -- Controlli di ammissibilit�el compenso

      -- I controlli non si applicano al compenso da conguaglio ed al compenso senza calcoli

      IF (aOrigineCompenso != CNRCTB545.isCompensoConguaglio AND
          aRecCompenso.fl_senza_calcoli = 'N') THEN

         aStrDefault:='Impossibile procedere al salvataggio del compenso. ';

         LOOP

            -- Il totale compenso �egativo. Si accetta solo il caso di compenso senza mandato principale
            -- per la restituzione dei carichi ente all'Ente:
            -- 1) Importo lordo percipiente = 0
            -- 2) Non esistono CORI Ente positivi
            -- 3) Il totale compenso �ari alla sommatoria dei CORI Ente negativi
            -- 4) Non rileva l'esistenza di CORI Percipiente

            IF    imTotaleCompenso < 0 THEN

                  IF (aRecCompenso.im_lordo_percipiente = 0 AND
                      contaCORIEntePiu = 0 AND
                      contaCoriEnteMeno > 0 AND
                      imTotaleCompenso = imCORIEnte) THEN
                     EXIT;
                  END IF;

                  IBMERR001.RAISE_ERR_GENERICO
                     (aStrDefault || 'Il totale compenso �egativo e non sono state rispettate le regole per ' ||
                      'la gestione di compensi senza mandato principale per la restituzione di carichi ente ' ||
                      'all''Ente (importo lordo percipiente diverso da 0 e/o esistono CORI Ente positivi)');

            -- Il totale compenso �ari a zero. Si accettano solo i casi di compenso senza mandato principale
            -- per la gestione del percipiente:
            -- 1) Importo lordo percipiente = 0
            -- 2) I Cori Ente non devono esistere o se esistono il numero dei CORI Ente positivi deve essere
            --    uguale a quello dei negativi e i loro valori assoluti sono a coppie uguali.
            -- 3) Non rileva l'esistenza di CORI Percipiente

            ELSIF imTotaleCompenso = 0 THEN

                  IF aRecCompenso.im_lordo_percipiente = 0 THEN
                     IF contaCORIEntePiu = 0 THEN
                        IF contaCORIEnteMeno = 0 THEN
                           EXIT;
                        END IF;
                     ELSE
                        IF contaCORIEntePiu = contaCORIEnteMeno THEN
                           IF checkCoppieValoriCoriEnte(aRecCompenso.fl_senza_calcoli,
                                                        aRecCompenso.cd_cds,
                                                        aRecCompenso.cd_unita_organizzativa,
                                                        aRecCompenso.esercizio,
                                                        aRecCompenso.pg_compenso) = 'Y' THEN
                              EXIT;
                           END IF;
                        END IF;
                     END IF;
                  END IF;

                  IBMERR001.RAISE_ERR_GENERICO
                     (aStrDefault || 'Il totale compenso �ari a zero e non sono state rispettate le regole per ' ||
                      'la gestione di compensi senza mandato principale per la gestione del percipiente ' ||
                      '(importo lordo percipiente diverso da 0 e/o il numero dei CORI Ente positivi non coincide con ' ||
                      'quelli negativi e/o i valori assoluti dei CORI Ente positivi e negativi presi a coppie non ' ||
                      'coincidono)');

            -- Il totale compenso �aggiore di zero. Si accettano solo i casi in cui il netto calcolato del mandato
            -- principale risulta maggiore o uguale a zero o, se minore di zero, allora l'importo totale del compenso
            -- meno i carichi ente positivi deve essere maggiore o uguale a zero

            ELSIF imTotaleCompenso > 0 THEN

                  IF imNettoCompensoCalcolato >= 0 THEN
                     EXIT;
                  ELSE
                     IF (imTotaleCompenso - imCORIEntePiu) >= 0 THEN
                        EXIT;
                     END IF;
                  END IF;

                  IBMERR001.RAISE_ERR_GENERICO
                     (aStrDefault || 'Il totale compenso �aggiore di zero e non sono state rispettate le regole per ' ||
                      'la gestione di compensi con mandato principale ' ||
                      '(importo totale del compenso - CORI Ente positivi - CORI Percipiente positivi) >= 0 oppure, ' ||
                      'se minore di zero, (importo totale del compenso - CORI Ente positivi) >= 0');

            END IF;

         END LOOP;

      END IF;
--pipe.send_message('aTotRedditoComplessivo nel COMPENSO= '||aTotRedditoComplessivo);
      ----------------------------------------------------------------------------------------------
      -- Aggiornamento del compenso per dati detrazioni

      BEGIN
         UPDATE COMPENSO
         SET    im_totale_compenso = imTotaleCompenso,
                im_netto_percipiente = imNettoCompenso,
                im_cr_percipiente = imCORIPercipiente,
                im_cr_ente = imCORIEnte,
                detrazioni_personali = aImDetrazioniPe,
                detrazioni_personali_netto = aImDetrazioniPeNetto,
                detrazioni_la = aImDetrazioniLa,
                detrazioni_la_netto = aImDetrazioniLaNetto,
                detrazione_coniuge = aImDetrazioniCo,
                detrazione_coniuge_netto = aImDetrazioniCoNetto,
                detrazione_figli = aImDetrazioniFi,
                detrazione_figli_netto = aImDetrazioniFiNetto,
                detrazione_altri = aImDetrazioniAl,
                detrazione_riduzione_cuneo = aImDetrazioniRidCuneo,
                detrazione_rid_cuneo_netto = aImDetrazioniRidCuneoNetto,
                detrazione_altri_netto = aImDetrazioniAlNetto,
                imponibile_fiscale = aImponibileIrpefLordo,
                im_deduzione_irpef = aImportoDeduzioneIrpef,
                imponibile_fiscale_netto = aImponibileIrpefNetto,
                numero_giorni = aRecCompenso.numero_giorni,
                im_reddito_complessivo = glbRedditoComplessivo,
                im_reddito_abitaz_princ = glbRedditoAbitazPrincipale,
                im_tot_reddito_complessivo = aTotRedditoComplessivo
         WHERE  cd_cds = aRecCompenso.cd_cds AND
                cd_unita_organizzativa = aRecCompenso.cd_unita_organizzativa AND
                esercizio = aRecCompenso.esercizio AND
                pg_compenso = aRecCompenso.pg_compenso;

      END;

   END;

END scriviDettaglioCompenso;

-- =================================================================================================
-- Controlla accoppiamenti di valore di
-- =================================================================================================
FUNCTION checkCoppieValoriCoriEnte
   (
    isCompensoSenzaCalcoli COMPENSO.fl_senza_calcoli%TYPE,
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompenso COMPENSO.pg_compenso%TYPE
    ) RETURN VARCHAR2 IS

   aEsito CHAR(1);

   i BINARY_INTEGER;
   j BINARY_INTEGER;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Gestione differenziata per compenso senza calcoli e non

   IF isCompensoSenzaCalcoli = 'Y' THEN

      BEGIN

         SELECT DECODE(COUNT(*),1,'N','Y') INTO aEsito
         FROM   DUAL
         WHERE  EXISTS
                (SELECT 1
                 FROM   CONTRIBUTO_RITENUTA A
                 WHERE  A.cd_cds = inCdsCompenso AND
                        A.cd_unita_organizzativa = inUoCompenso AND
                        A.esercizio = inEsercizioCompenso AND
                        A.pg_compenso = inPgCompenso AND
                        A.ti_ente_percipiente = 'E' AND
                        A.ammontare > 0 AND
                        NOT EXISTS
                           (SELECT 1
                            FROM   CONTRIBUTO_RITENUTA B
                            WHERE  B.cd_cds = A.cd_cds AND
                                   B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                                   B.esercizio = A.esercizio AND
                                   B.pg_compenso = A.pg_compenso AND
                                   B.ti_ente_percipiente = 'E' AND
                                   B.ammontare < 0 AND
                                   ABS(B.ammontare) = A.ammontare));

      END;

   ELSE

      aEsito:='Y';

      BEGIN

         FOR i IN tabella_cori.FIRST .. tabella_cori.LAST

         LOOP

            IF (tabella_cori(i).tAmmontareEnte IS NOT NULL AND
                tabella_cori(i).tAmmontareEnte > 0) THEN

               FOR j IN tabella_cori.FIRST .. tabella_cori.LAST

               LOOP

                  aEsito:='N';

                  IF (tabella_cori(j).tAmmontareEnte IS NOT NULL AND
                      tabella_cori(j).tAmmontareEnte < 0) THEN
                     IF tabella_cori(i).tAmmontareEnte = ABS(tabella_cori(j).tAmmontareEnte) THEN
                        aEsito:='Y';
                        EXIT;
                     END IF;
                  END IF;

               END LOOP;

               IF aEsito = 'N' THEN
                  EXIT;
               END IF;

            END IF;

         END LOOP;

       END;

   END IF;

   RETURN aEsito;

END checkCoppieValoriCoriEnte;

-- =================================================================================================
-- Determina il netto delle detrazioni personali e familiari se IRPEF risulta inferiore al totale
-- calcolato di queste
-- =================================================================================================
PROCEDURE nettizzaDetrazioni
   (
    aImporto NUMBER
   ) IS
   i BINARY_INTEGER;
   aImportoResto NUMBER(15,2);

BEGIN

   aImportoResto:=aImporto;

   BEGIN

      FOR i IN 1 .. 6

      LOOP

         IF    i = 1 THEN
               IF aImDetrazioniPeNetto > 0 THEN
                  aImDetrazioniPeNetto:=aImDetrazioniPeNetto - aImportoResto;
                  IF aImDetrazioniPeNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniPeNetto;
                     aImportoResto:=aImDetrazioniPeNetto * -1;
                     aImDetrazioniPeNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         ELSIF i = 2 THEN
               IF aImDetrazioniLaNetto > 0 THEN
                  aImDetrazioniLaNetto:=aImDetrazioniLaNetto - aImportoResto;
                  IF aImDetrazioniLaNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniLaNetto;
                     aImportoResto:=aImDetrazioniLaNetto * -1;
                     aImDetrazioniLaNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         ELSIF i = 3 THEN
               IF aImDetrazioniCoNetto > 0 THEN
                  aImDetrazioniCoNetto:=aImDetrazioniCoNetto - aImportoResto;
                  IF aImDetrazioniCoNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniCoNetto;
                     aImportoResto:=aImDetrazioniCoNetto * -1;
                     aImDetrazioniCoNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         ELSIF i = 4 THEN
               IF aImDetrazioniFiNetto > 0 THEN
                  aImDetrazioniFiNetto:=aImDetrazioniFiNetto - aImportoResto;
                  IF aImDetrazioniFiNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniFiNetto;
                     aImportoResto:=aImDetrazioniFiNetto * -1;
                     aImDetrazioniFiNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         ELSIF i = 5 THEN
               IF aImDetrazioniAlNetto > 0 THEN
                  aImDetrazioniAlNetto:=aImDetrazioniAlNetto - aImportoResto;
                  IF aImDetrazioniAlNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniAlNetto;
                     aImportoResto:=aImDetrazioniAlNetto * -1;
                     aImDetrazioniAlNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         ELSIF i = 6 THEN
               IF aImDetrazioniRidCuneoNetto > 0 THEN
                  aImDetrazioniRidCuneoNetto:=aImDetrazioniRidCuneoNetto - aImportoResto;
                  IF aImDetrazioniRidCuneoNetto < 0 THEN
                     --aImportoResto:=aImportoResto + aImDetrazioniAlNetto;
                     aImportoResto:=aImDetrazioniRidCuneoNetto * -1;
                     aImDetrazioniRidCuneoNetto:=0;
                  ELSE
                     aImportoResto:=0;
                     EXIT;
                  END IF;
               END IF;
         END IF;

      END LOOP;

   END;

END nettizzaDetrazioni;

-- =================================================================================================
-- Copia del compenso in modifica
-- =================================================================================================
PROCEDURE copiaCompenso
   (
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompenso COMPENSO.pg_compenso%TYPE,
    inCopiaCdsCompenso COMPENSO.cd_cds%TYPE,
    inCopiaUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inCopiaEsercizioCompenso COMPENSO.esercizio%TYPE,
    inCopiaPgCompenso COMPENSO.pg_compenso%TYPE
   ) IS
   flEsisteCompenso CHAR(1);

BEGIN

   -- Copia del compenso in caso di modifica. Si memorizza la catena dei dati
   -- di un compenso prima che questo sia modificato

   IF inCopiaPgCompenso IS NOT NULL THEN

      flEsisteCompenso:=CNRCTB545.chkEsisteCompenso(inCopiaCDSCompenso,
                                                    inCopiaUOCompenso,
                                                    inCopiaEsercizioCompenso,
                                                    inCopiaPgCompenso);

      IF flEsisteCompenso = 'N' THEN
         CNRCTB545.copiaCompenso(inCdsCompenso,
                                 inUoCompenso,
                                 inEsercizioCompenso,
                                 inPgCompenso,
                                 inCopiaCdsCompenso,
                                 inCopiaUoCompenso,
                                 inCopiaEsercizioCompenso,
                                 inCopiaPgCompenso);
      END IF;

   END IF;

END copiaCompenso;
Function getParametriCNR(inEsercizio  PARAMETRI_CNR.esercizio%Type)
  Return PARAMETRI_CNR%Rowtype Is
  rec_parametri_cnr PARAMETRI_CNR%Rowtype;
Begin
  Select * Into rec_parametri_cnr
  From Parametri_CNR
  Where Esercizio = inEsercizio;
  Return rec_parametri_cnr;
Exception
  When No_Data_Found Then
    IBMERR001.RAISE_ERR_GENERICO
    ('Non esistono i Parametri CNR per l''anno: '||inEsercizio);
End;
-- =================================================================================================
-- Aggiornamento dei montanti per i Pagamenti Esterni
-- =================================================================================================
PROCEDURE aggiornaMontantiPagEst
   (
    inEsercizio  MONTANTI.esercizio%TYPE,
    inCd_anag    MONTANTI.Cd_anag%TYPE,
    inUtente     MONTANTI.utuv%Type,
    inPagamento  MONTANTI.inps_occasionali%Type
   ) IS
   aRecMontanti MONTANTI%ROWTYPE;
   eseguiLock   CHAR(1) := 'Y';
Begin
  dataOdierna := Sysdate;
  aRecMontanti:=CNRCTB080.getMontanti(inEsercizio,
                                      inCd_anag,
                                      eseguiLock);
  IF aRecMontanti.pg_ver_rec IS NULL THEN
    aRecMontanti.dacr:=dataOdierna;
    aRecMontanti.utcr:=inUtente;
    aRecMontanti.duva:=dataOdierna;
    aRecMontanti.utuv:=inUtente;
    aRecMontanti.pg_ver_rec:=1;
    aRecMontanti.inps_occasionali := inPagamento;
    CNRCTB080.insMontanti(aRecMontanti);
  Else
    aRecMontanti.duva:=dataOdierna;
    aRecMontanti.utuv:=inUtente;
    aRecMontanti.pg_ver_rec:=aRecMontanti.pg_ver_rec + 1;
    aRecMontanti.inps_occasionali := aRecMontanti.inps_occasionali + inPagamento;
    CNRCTB080.upgMontantiAltri(aRecMontanti);
  End If;
END;

-- =================================================================================================
-- Aggiornamento dei montanti
-- =================================================================================================
PROCEDURE aggiornaMontanti
   (
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompenso COMPENSO.pg_compenso%TYPE,
    inCopiaCdsCompenso COMPENSO.cd_cds%TYPE,
    inCopiaUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inCopiaEsercizioCompenso COMPENSO.esercizio%TYPE,
    inCopiaPgCompenso COMPENSO.pg_compenso%TYPE
   ) IS
   aPasso BINARY_INTEGER;
   eseguiLock CHAR(1);
   segno CHAR(1);

   j BINARY_INTEGER;
   k BINARY_INTEGER;

   aRecMontanti MONTANTI%ROWTYPE;
   aRecCompensoMontante COMPENSO%ROWTYPE;
   aRecAnagMontante ANAGRAFICO%ROWTYPE;
   aRecRateizzaClassificCoriBase RATEIZZA_CLASSIFIC_CORI%ROWTYPE;
   aRecAccontoClassificCoriBase ACCONTO_CLASSIFIC_CORI%ROWTYPE;
   DataInizioFattElettronica DATE;

BEGIN
--pipe.send_message('AGGIORNA  MONTANTI');
   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   dataOdierna:=sysdate;
   eseguiLock:='Y';
   aPasso:=0;

   -------------------------------------------------------------------------------------------------
   -- Lettura dati di base legati al compenso in calcolo, testata compenso e anagrafico comprensivo,
   -- eventualmente, di quelli originali in caso di modifica. I dati sono memorizzati rispettivamente
   -- in aRecCompenso, aRecAnagrafico e aRecCompensoOri, aRecAnagraficoOri

   getDatiBaseCompenso(inCdsCompenso,
                       inUoCompenso,
                       inEsercizioCompenso,
                       inPgCompenso,
                       inCopiaCdsCompenso,
                       inCopiaUoCompenso,
                       inCopiaEsercizioCompenso,
                       inCopiaPgCompenso,
                       eseguiLock);

   -------------------------------------------------------------------------------------------------
   -- Il compenso da conguaglio non aggiorna i montanti e non genera fatture da compenso

   IF aRecCompenso.fl_compenso_conguaglio = 'Y' THEN
      RETURN;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Eventuale generazione della fattura da compenso
   -- A partire dalla data di avvio delle fatture elettroniche il compenso non dovr�i� generare la fattura

   DataInizioFattElettronica := CNRCTB015.getDt01PerChiave(aRecCompenso.Esercizio, 'FATTURAZIONE_ELETTRONICA', 'PASSIVA');

   if DataInizioFattElettronica is null then
         IBMERR001.RAISE_ERR_GENERICO
                     ('In CONFIGURAZIONE_CNR non risulta presente la data di avvio della fatturazione elettronica.');
   end if;

   if aRecCompenso.dt_registrazione < DataInizioFattElettronica then
         CNRCTB545.generaFatturaPassiva(aRecCompenso,
                                        aRecCompensoOri,
                                        aRecAnagrafico);
   end if;
   -------------------------------------------------------------------------------------------------
   -- Il compenso da minicarriera e a tassazione separata non aggiorna i montanti

   IF aRecCompenso.fl_compenso_minicarriera = 'Y' THEN
      IF (aRecCompenso.fl_compenso_mcarriera_tassep = 'Y' AND
          aRecCompenso.aliquota_irpef_tassep > 0) THEN
         RETURN;
      END IF;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Controllo se devo eseguire l'aggiornamento dei montanti.
   -- Se COMPENSO.pg_ver_rec �aggiore di 1 e sono vuoti i parametri del clone non faccio nulla;
   -- �tata chiamata la procedura di aggiornamento montanti da una modifica che non ha avuto alcuna
   -- influenza sui dati di calcolo del compenso

   IF (aRecCompenso.pg_ver_rec > 1 AND
       inCopiaPgCompenso IS NULL) THEN
      RETURN;
   END IF;


   -------------------------------------------------------------------------------------------------
   -- Ciclo di aggiornamento montanti e rateizzazione addizionali territorio

   FOR aPasso IN 1 .. 2

   LOOP

      aRecCompensoMontante:=NULL;
      aRecAnagMontante:=NULL;

      IF    aPasso = 1 THEN
            IF inCopiaPgCompenso IS NOT NULL THEN
               aRecCompensoMontante:=aRecCompensoOri;
               aRecAnagMontante:=aRecAnagraficoOri;
               segno:='-';
            END IF;
      ELSIF aPasso = 2 THEN
            IF (inCopiaPgCompenso IS NULL OR
                inCopiaPgCompenso != inPgCompenso) THEN
               aRecCompensoMontante:=aRecCompenso;
               aRecAnagMontante:=aRecAnagrafico;
               segno:='+';
            END IF;
      END IF;

      -- Lettura dati relativi ai montanti per il soggetto anagrafico di riferimento e costruzione della
      -- matrice degli stessi. Se non sono definiti i montanti �ornato un record con i valori a zero.
      -- I valori dei montanti sono ridotti di quanto calcolato dal compenso origine in caso di modifica
      -- se coincidono il riferimento all'anagrafico e quello del tipo compenso dipendente o altro

      IF (aRecCompensoMontante.ti_anagrafico IS NOT NULL AND
          aRecCompensoMontante.ti_anagrafico = 'A') THEN

         aRecMontanti:=CNRCTB080.getMontanti(aRecCompensoMontante.esercizio,
                                             aRecAnagMontante.cd_anag,
                                             eseguiLock);
         costruisciTabMontanti(aRecMontanti);

         modificaTabMontanti (aRecCompensoMontante,
                              segno);

         -- Ciclo di aggiornamento tabella montante

         FOR k IN 1 .. 9

         Loop
--pipe.send_message('K = '||K);
--pipe.send_message('tabella_montanti(k).tValoreNettoAltro '||tabella_montanti(k).tValoreNettoAltro);
            IF    k = 1 THEN
                  aRecMontanti.irpef_altri:=tabella_montanti(k).tValoreNettoAltro;
                  aRecMontanti.irpef_lordo_altri:=tabella_montanti(k).tValoreLordoAltro;
                  aRecMontanti.deduzione_irpef_altri:=tabella_montanti(k).tImDeduzioneAltro;
                  aRecMontanti.deduzione_family_altri:=tabella_montanti(k).tImDeduzioneFamilyAltro;
            ELSIF k = 2 THEN
                  aRecMontanti.inps_altri:=tabella_montanti(k).tValoreNettoAltro;
            ELSIF k = 4 THEN
                  aRecMontanti.inail_altri:=tabella_montanti(k).tValoreNettoAltro;
            ELSIF k = 5 THEN
                  aRecMontanti.riduz_altri:=tabella_montanti(k).tValoreNettoAltro;
            ELSIF k = 7 THEN
                  aRecMontanti.inps_occasionali:=tabella_montanti(k).tValoreNettoAltro;
            ELSIF k = 8 THEN
                  aRecMontanti.inpgi_altri:=tabella_montanti(k).tValoreNettoAltro;
            ELSIF k = 9 THEN
                  aRecMontanti.enpapi_altri:=tabella_montanti(k).tValoreNettoAltro;
            END IF;

         END LOOP;

         IF aRecMontanti.pg_ver_rec IS NULL THEN
            aRecMontanti.dacr:=dataOdierna;
            aRecMontanti.utcr:=aRecCompensoMontante.utuv;
            aRecMontanti.duva:=dataOdierna;
            aRecMontanti.utuv:=aRecCompensoMontante.utuv;
            aRecMontanti.pg_ver_rec:=1;
            CNRCTB080.insMontanti(aRecMontanti);
         ELSE
            aRecMontanti.duva:=dataOdierna;
            aRecMontanti.utuv:=aRecCompensoMontante.utuv;
            aRecMontanti.pg_ver_rec:=aRecMontanti.pg_ver_rec + 1;
            CNRCTB080.upgMontantiAltri(aRecMontanti);
         END IF;

         -- Recupero degli accantonamenti delle addizionali territorio. I valori tornati sono ridotti di
         -- quanto calcolato sul compenso origine in caso di modifica se coincide il riferimento all'anagrafico

         CNRCTB552.getRateizzaAddTerritorio(aRecCompensoMontante.esercizio - 1,
                                            aRecAnagMontante.cd_anag,
                                            eseguiLock,
                                            aRecRateizzaClassificCoriC0,
                                            aRecRateizzaClassificCoriP0,
                                            aRecRateizzaClassificCoriR0);

         CNRCTB552.modRateizzaAddTerritorio(aRecCompensoMontante,
                                            segno,
                                            aRecRateizzaClassificCoriC0,
                                            aRecRateizzaClassificCoriP0,
                                            aRecRateizzaClassificCoriR0);



         FOR j IN 1 .. 3

         LOOP

            IF    j = 1 THEN
                  aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriC0;
            ELSIF j = 2 THEN
                  aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriP0;
            ELSIF j = 3 THEN
                  aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriR0;
            END IF;

            IF (aRecRateizzaClassificCoriBase.cd_cds_conguaglio IS NOT NULL AND
                aRecRateizzaClassificCoriBase.im_da_rateizzare > 0) THEN

               IF (aRecRateizzaClassificCoriBase.im_rateizzato < 0 OR
                   aRecRateizzaClassificCoriBase.im_rateizzato > aRecRateizzaClassificCoriBase.im_da_rateizzare) THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Inconsistenza nella valorizzazione dell''importo rateizzato in aggiornamento di RATEIZZA_CLASSIFIC_CORI');
               END IF;

               UPDATE RATEIZZA_CLASSIFIC_CORI
               SET    im_rateizzato = aRecRateizzaClassificCoriBase.im_rateizzato,
                      duva = dataOdierna,
                      utuv = aRecCompensoMontante.utuv,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  esercizio = aRecRateizzaClassificCoriBase.esercizio AND
                      cd_anag = aRecRateizzaClassificCoriBase.cd_anag AND
                      cd_classificazione_cori = aRecRateizzaClassificCoriBase.cd_classificazione_cori AND
                      fl_temporaneo = aRecRateizzaClassificCoriBase.fl_temporaneo;

            END IF;

         END LOOP;

         -- Recupero degli acconti delle addizionali comunali. I valori tornati sono ridotti di
         -- quanto calcolato sul compenso origine in caso di modifica se coincide il riferimento all'anagrafico

         CNRCTB553.getAccontoAddTerritorio(aRecCompensoMontante.esercizio,
                                            aRecAnagMontante.cd_anag,
                                            eseguiLock,
                                            aRecAccontoClassificCoriC0);

         CNRCTB553.modAccontoAddTerritorio(aRecCompensoMontante,
                                            segno,
                                            aRecAccontoClassificCoriC0);

         aRecAccontoClassificCoriBase:=aRecAccontoClassificCoriC0;

         If aRecAccontoClassificCoriBase.im_acconto_calcolato > 0 Then
               IF (aRecAccontoClassificCoriBase.im_acconto_trattenuto < 0 OR
                   aRecAccontoClassificCoriBase.im_acconto_trattenuto > aRecAccontoClassificCoriBase.im_acconto_calcolato) THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Inconsistenza nella valorizzazione dell''importo dell''acconto in aggiornamento di ACCONTO_CLASSIFIC_CORI');
               END IF;

               UPDATE ACCONTO_CLASSIFIC_CORI
               SET    im_acconto_trattenuto = aRecAccontoClassificCoriBase.im_acconto_trattenuto,
                      duva = dataOdierna,
                      utuv = aRecCompensoMontante.utuv,
                      pg_ver_rec = pg_ver_rec + 1
               WHERE  esercizio = aRecAccontoClassificCoriBase.esercizio AND
                      cd_anag = aRecAccontoClassificCoriBase.cd_anag AND
                      cd_classificazione_cori = aRecAccontoClassificCoriBase.cd_classificazione_cori;
         End If;
      ELSIF (aRecCompensoMontante.ti_anagrafico IS NOT NULL AND
             aRecCompensoMontante.ti_anagrafico = 'D') THEN

         aRecMontanti:=CNRCTB080.getMontanti(aRecCompensoMontante.esercizio,
                                             aRecAnagMontante.cd_anag,
                                             eseguiLock);
         costruisciTabMontanti(aRecMontanti);

         modificaTabMontanti (aRecCompensoMontante,
                              segno);

         -- Ciclo di aggiornamento tabella montante

         aRecMontanti.riduz_dipendenti:=tabella_montanti(5).tValoreNettoDip;

--pipe.send_message('tabella_montanti(5).tNomeDip '||tabella_montanti(5).tNomeDip);
--pipe.send_message('tabella_montanti(5).tValoreNettoDip '||tabella_montanti(5).tValoreNettoDip);

         IF aRecMontanti.pg_ver_rec IS NULL THEN
            aRecMontanti.dacr:=dataOdierna;
            aRecMontanti.utcr:=aRecCompensoMontante.utuv;
            aRecMontanti.duva:=dataOdierna;
            aRecMontanti.utuv:=aRecCompensoMontante.utuv;
            aRecMontanti.pg_ver_rec:=1;
            CNRCTB080.insMontanti(aRecMontanti);
         ELSE
            aRecMontanti.duva:=dataOdierna;
            aRecMontanti.utuv:=aRecCompensoMontante.utuv;
            aRecMontanti.pg_ver_rec:=aRecMontanti.pg_ver_rec + 1;
            CNRCTB080.upgMontantiDip(aRecMontanti);
         END IF;

      END IF;  -- aRecCompensoMontante.ti_anagrafico

   END LOOP;

END aggiornaMontanti;

-- =================================================================================================
-- Aggiornamento del compenso per conferma dettagli cori in senza calcoli
-- =================================================================================================
PROCEDURE aggCompensoSenzaCalcoli
   (
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompenso COMPENSO.pg_compenso%TYPE
   ) IS
   eseguiLock CHAR(1);
   aCopiaCdsCompenso COMPENSO.cd_cds%TYPE;
   aCopiaUoCompenso COMPENSO.cd_unita_organizzativa%TYPE;
   aCopiaEsercizioCompenso COMPENSO.esercizio%TYPE;
   aCopiaPgCompenso COMPENSO.pg_compenso%TYPE;
   imCoriEnte NUMBER(15,2);
   imCoriPercip NUMBER(15,2);
   aAmmontareNetto NUMBER(15,2);
   tipo_cori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%type;

   aStrDefault VARCHAR2(100);

   -- Variabili per controllo ammissibilit�el compenso

   imCORIPercipPiu NUMBER(15,2);
   imCORIPercipMeno NUMBER(15,2);
   contaCORIPercipPiu INTEGER;
   contaCORIPercipMeno INTEGER;

   imCORIEntePiu NUMBER(15,2);
   imCORIEnteMeno NUMBER(15,2);
   contaCORIEntePiu INTEGER;
   contaCORIEnteMeno INTEGER;
   imAggiungoNettoPercip NUMBER(15,2);
   imTotaleCompenso NUMBER(15,2);
   imNettoCompenso NUMBER(15,2);
   imNettoCompensoCalcolato NUMBER(15,2);

   aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Memorizzazione parametri generali della procedura

   aCopiaCdsCompenso:=NULL;
   aCopiaUoCompenso:=NULL;
   aCopiaEsercizioCompenso:=NULL;
   aCopiaPgCompenso:=NULL;
   imCoriEnte:=0;
   imCoriPercip:=0;

   imCORIPercipPiu:=0;
   imCORIPercipMeno:=0;
   contaCORIPercipPiu:=0;
   contaCORIPercipMeno:=0;

   imCORIEntePiu:=0;
   imCORIEnteMeno:=0;
   contaCORIEntePiu:=0;
   contaCORIEnteMeno:=0;

   imTotaleCompenso:=0;
   imNettoCompenso:=0;
   imNettoCompensoCalcolato:=0;
   imAggiungoNettoPercip:=0;
   tipo_cori :=null;

   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Lettura dati di base legati al compenso in calcolo

   getDatiBaseCompenso (inCdsCompenso,
                        inUoCompenso,
                        inEsercizioCompenso,
                        inPgCompenso,
                        aCopiaCdsCompenso,
                        aCopiaUoCompenso,
                        aCopiaEsercizioCompenso,
                        aCopiaPgCompenso,
                        eseguiLock);

    BEGIN

       OPEN gen_cur FOR

            SELECT *
            FROM   CONTRIBUTO_RITENUTA
            WHERE  cd_cds = aRecCompenso.cd_cds AND
                   cd_unita_organizzativa = aRecCompenso.cd_unita_organizzativa AND
                   esercizio = aRecCompenso.esercizio AND
                   pg_compenso = aRecCompenso.pg_compenso;

       LOOP

          FETCH gen_cur INTO aRecContributoRitenuta;

          EXIT WHEN gen_cur%NOTFOUND;

          aAmmontareNetto:=aRecContributoRitenuta.ammontare_lordo;

          UPDATE CONTRIBUTO_RITENUTA
          SET    ammontare = aAmmontareNetto,
                 imponibile_lordo = aRecContributoRitenuta.imponibile
          WHERE  cd_cds = aRecContributoRitenuta.cd_cds AND
                 cd_unita_organizzativa = aRecContributoRitenuta.cd_unita_organizzativa AND
                 esercizio = aRecContributoRitenuta.esercizio AND
                 pg_compenso = aRecContributoRitenuta.pg_compenso AND
                 cd_contributo_ritenuta = aRecContributoRitenuta.cd_contributo_ritenuta AND
                 ti_ente_percipiente = aRecContributoRitenuta.ti_ente_percipiente;

          IF aRecContributoRitenuta.ti_ente_percipiente = 'E' THEN
             imCoriEnte:=imCoriEnte + aAmmontareNetto;
             IF aAmmontareNetto < 0 THEN
                imCORIEnteMeno:=imCORIEnteMeno + aAmmontareNetto;
                contaCORIEnteMeno:=contaCORIEnteMeno + 1;
             END IF;
             IF aAmmontareNetto > 0 THEN
                imCORIEntePiu:=imCORIEntePiu + aAmmontareNetto;
                contaCORIEntePiu:=contaCORIEntePiu + 1;
             END IF;
          ELSE
             imCoriPercip:=imCoriPercip + aAmmontareNetto;
             IF aAmmontareNetto < 0 THEN
                imCORIPercipMeno:=imCORIPercipMeno + aAmmontareNetto;
                contaCORIPercipMeno:=contaCORIPercipMeno + 1;
             END IF;
             IF aAmmontareNetto > 0 THEN
                imCORIPercipPiu:=imCORIPercipPiu + aAmmontareNetto;
                contaCORIPercipPiu:=contaCORIPercipPiu + 1;
             END IF;
          END IF;
-- Totalizzazione degli importi di rivalsa e iva da sommare al netto percipiente
          select cd_classificazione_cori into tipo_cori from TIPO_CONTRIBUTO_RITENUTA where
          (    (tipo_contributo_ritenuta.cd_contributo_ritenuta =
                                    aRecContributoRitenuta.cd_contributo_ritenuta
            )
        AND (tipo_contributo_ritenuta.dt_ini_validita =
                                           aRecContributoRitenuta.dt_ini_validita
            )
       );
           IF aRecContributoRitenuta.ti_ente_percipiente = 'E' THEN
              IF (tipo_cori = CNRCTB545.isCoriIva OR
                  tipo_cori = CNRCTB545.isCoriRivalsa) THEN
                 imAggiungoNettoPercip:=imAggiungoNettoPercip + aAmmontareNetto;
              END IF;
           end if;
      END LOOP;

      CLOSE gen_cur;

   END;

   -------------------------------------------------------------------------------------------------
   -- Totalizzazione dati del compenso

   imTotaleCompenso:=aRecCompenso.im_lordo_percipiente + imCORIEnte;
   imNettoCompenso:=aRecCompenso.im_lordo_percipiente - imCORIPercip+imAggiungoNettoPercip;
   imNettoCompensoCalcolato:=imTotaleCompenso - imCORIEntePiu - imCORIPercipPiu;

   -------------------------------------------------------------------------------------------------
   -- Controlli di ammissibilit�el compenso

   aStrDefault:='Impossibile procedere al salvataggio del compenso. ';

   BEGIN

      LOOP

         -- Il totale compenso �egativo. Si accetta solo il caso di compenso senza mandato principale
         -- per la restituzione dei carichi ente all'Ente:
         -- 1) Importo lordo percipiente = 0
         -- 2) Non esistono CORI Ente positivi
         -- 3) Il totale compenso �ari alla sommatoria dei CORI Ente negativi
         -- 4) Non rileva l'esistenza di CORI Percipiente

         IF    imTotaleCompenso < 0 THEN

               IF (aRecCompenso.im_lordo_percipiente = 0 AND
                   contaCORIEntePiu = 0 AND
                   contaCoriEnteMeno > 0 AND
                   imTotaleCompenso = imCORIEnte) THEN
                  EXIT;
               END IF;

               IBMERR001.RAISE_ERR_GENERICO
                  (aStrDefault || 'Il totale compenso �egativo e non sono state rispettate le regole per ' ||
                   'la gestione di compensi senza mandato principale per la restituzione di carichi ente ' ||
                   'all''Ente (importo lordo percipiente diverso da 0 e/o esistono CORI Ente positivi)');

         -- Il totale compenso �ari a zero. Si accettano solo i casi di compenso senza mandato principale
         -- per la gestione del percipiente:
         -- 1) Importo lordo percipiente = 0
         -- 2) I Cori Ente non devono esistere o se esistono il numero dei CORI Ente positivi deve essere
         --    uguale a quello dei negativi e i loro valori assoluti sono a coppie uguali.
         -- 3) Non rileva l'esistenza di CORI Percipiente

         ELSIF imTotaleCompenso = 0 THEN

               IF aRecCompenso.im_lordo_percipiente = 0 THEN
                  IF contaCORIEntePiu = 0 THEN
                     IF contaCORIEnteMeno = 0 THEN
                        EXIT;
                     END IF;
                  ELSE
                     IF contaCORIEntePiu = contaCORIEnteMeno THEN
                        IF checkCoppieValoriCoriEnte(aRecCompenso.fl_senza_calcoli,
                                                     aRecCompenso.cd_cds,
                                                     aRecCompenso.cd_unita_organizzativa,
                                                     aRecCompenso.esercizio,
                                                     aRecCompenso.pg_compenso) = 'Y' THEN
                           EXIT;
                        END IF;
                     END IF;
                  END IF;
               END IF;

               IBMERR001.RAISE_ERR_GENERICO
                  (aStrDefault || 'Il totale compenso �ari a zero e non sono state rispettate le regole per ' ||
                   'la gestione di compensi senza mandato principale per la gestione del percipiente ' ||
                   '(importo lordo percipiente diverso da 0 e/o il numero dei CORI Ente positivi non coincide con ' ||
                   'quelli negativi e/o i valori assoluti dei CORI Ente positivi e negativi presi a coppie non ' ||
                   'coincidono)');

         -- Il totale compenso �aggiore di zero. Si accettano solo i casi in cui il netto calcolato del mandato
         -- principale risulta maggiore o uguale a zero o, se minore di zero, allora l'importo totale del compenso
         -- meno i carichi ente positivi deve essere maggiore o uguale a zero

         ELSIF imTotaleCompenso > 0 THEN

               IF imNettoCompensoCalcolato >= 0 THEN
                  EXIT;
               ELSE
                  IF (imTotaleCompenso - imCORIEntePiu) >= 0 THEN
                      EXIT;
                  END IF;
               END IF;

               IBMERR001.RAISE_ERR_GENERICO
                  (aStrDefault || 'Il totale compenso �aggiore di zero e non sono state rispettate le regole per ' ||
                   'la gestione di compensi con mandato principale ' ||
                   '(importo totale del compenso - CORI Ente positivi - CORI Percipiente positivi) >= 0 oppure, ' ||
                   'se minore di zero, (importo totale del compenso - CORI Ente positivi) >= 0');

         END IF;

      END LOOP;

   END;

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento del compenso

   UPDATE COMPENSO
   SET    im_totale_compenso = imTotaleCompenso,
          im_netto_percipiente = imNettoCompenso,
          im_cr_percipiente = imCoriPercip,
          im_cr_ente = imCoriEnte
   WHERE  cd_cds = aRecCompenso.cd_cds AND
          cd_unita_organizzativa = aRecCompenso.cd_unita_organizzativa AND
          esercizio = aRecCompenso.esercizio AND
          pg_compenso = aRecCompenso.pg_compenso;

END aggCompensoSenzaCalcoli;
-- =================================================================================================
-- Controllo eliminazione del compenso
-- =================================================================================================
PROCEDURE eseguiDelCompenso
   (
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompenso COMPENSO.pg_compenso%TYPE,
    statoCancella IN OUT NUMBER
   ) IS
   aTipoChiamata CHAR(1);
   aStatoCancellazione INTEGER;

BEGIN

   aTipoChiamata:='C';
   aStatoCancellazione:=cancellaCompenso(inCdsCompenso,
                                         inUoCompenso,
                                         inEsercizioCompenso,
                                         inPgCompenso,
                                         aTipoChiamata);

   statoCancella:=aStatoCancellazione;

END eseguiDelCompenso;

-- =================================================================================================
-- Cancellazione del compenso
-- =================================================================================================
FUNCTION cancellaCompenso
   (
    inCdsCompenso COMPENSO.cd_cds%TYPE,
    inUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    inEsercizioCompenso COMPENSO.esercizio%TYPE,
    inPgCompenso COMPENSO.pg_compenso%TYPE,
    idChiamante CHAR
   ) RETURN INTEGER IS
   eseguiLock CHAR(1);
   aTabellaAssociazione VARCHAR2(30);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione variabili

   eseguiLock:='Y';

   -------------------------------------------------------------------------------------------------
   -- Lettura del compenso

   aRecCompenso:=CNRCTB545.getCompenso (inCdsCompenso,
                                        inUoCompenso,
                                        inEsercizioCompenso,
                                        inPgCompenso,
                                        eseguiLock);

   -- Il compenso risulta essere gi�n stato annullato

   IF aRecCompenso.stato_cofi = 'A' THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Il compenso selezionato risulta essere gi�tato annullato in data ' ||
          TO_CHAR(aRecCompenso.dt_cancellazione,'DD/MM/YYYY'));
   END IF;

   -- Il compenso risulta essere associato a mandati e/o reversali

   IF aRecCompenso.stato_cofi = 'P' THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Impossibile eliminare un compenso che risulta pagato. Occorre prima annullare ' ||
          'il mandato o la reversale principale associata');
   END IF;

   -- Il compenso risulta essere associato ad una spesa del fondo economale

   IF aRecCompenso.stato_pagamento_fondo_eco = 'R' THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Impossibile eliminare un compenso che risulta associato ad una spesa su fondo economale.');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Controllo eventuali associazioni del compenso ad altri concetti amministrativi

   -- Definisco la tipologia origine del compenso

   aOrigineCompenso:=CNRCTB545.getTipoOrigineCompenso(aRecCompenso.cd_cds,
                                                      aRecCompenso.cd_unita_organizzativa,
                                                      aRecCompenso.esercizio,
                                                      aRecCompenso.pg_compenso);

   -- Il compenso risulta essere generato da conguaglio

   IF aOrigineCompenso = CNRCTB545.isCompensoConguaglio THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('I compensi generati da conguaglio possono essere eliminati solo dal pannello di gestione conguaglio');
   END IF;

   -- Il compenso risulta essere associato a missione. Segnalo errore solo se arrivo da compenso

   IF idChiamante = 'C' THEN
      IF (aOrigineCompenso = CNRCTB545.isCompensoMissione OR
          aOrigineCompenso = CNRCTB545.isCompensoMissioneAssCong) THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('I compensi associati a missione possono essere eliminati solo dal pannello di gestione della missione');
      END IF;
   END IF;

   -- Il compenso �ssociato a minicarriera allora sgancio la stessa

   IF (aOrigineCompenso = CNRCTB545.isCompensoMinicarriera OR
       aOrigineCompenso = CNRCTB545.isCompensoMinicarrieraAssCong) THEN

      CNRCTB600.sganciaAssCompensoMCarriera(inCdsCompenso,
                                            inUoCompenso,
                                            inEsercizioCompenso,
                                            inPgCompenso);

   END IF;

   -- Ricalcolo i montanti per l'eliminazione del compenso

   --IF aRecCompenso.ti_anagrafico = 'A' THEN
   -- Devo aggiornare i montanti anche per i DIP (solo la riduzione erariale)
      aggiornaMontanti(inCdsCompenso,
                       inUoCompenso,
                       inEsercizioCompenso,
                       inPgCompenso,
                       inCdsCompenso,
                       inUoCompenso,
                       inEsercizioCompenso,
                       inPgCompenso);
   --END IF;

   -- Ritorna lo stato di cancellazione.
   -- 2 = Cancellazione Fisica
   -- 1 = Cancellazione Logica
   -- 3 = Cancellazione Logica con messaggio per ora questa parte �asciata al client

   IF (aRecCompenso.ti_associato_manrev = 'N' AND
         (
            (aRecCompenso.stato_coge = 'N' OR
             aRecCompenso.stato_coge = 'X') AND
            (aRecCompenso.stato_coan = 'N' OR
             aRecCompenso.stato_coan = 'X')
          )
      ) THEN
      RETURN CNRCTB545.isCancellazioneFisica;
   ELSE
      RETURN CNRCTB545.isCancellazioneLogica;
   END IF;

END cancellaCompenso;

-- =================================================================================================
-- La vecchia Calcolo delle detrazioni familiari la utilizzo per il calcolo della teorica per ogni componente
-- =================================================================================================
PROCEDURE calcolaDetrazioniFam
   (
    aImportoRiferimento NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE,
    aDtCompetenzaDa DATE,
    aDtCompetenzaA DATE,
    aDtRegistrazione DATE,
    aImportoDetrazCo IN OUT NUMBER,
    aImportoDetrazFi IN OUT NUMBER,
    aImportoDetrazAl IN OUT NUMBER,
    aImportoDetrazFiS In Out NUMBER,
    esisteFiSenzaConiuge CHAR
   ) IS
   aDataRifDa DATE;
   aDataRifA DATE;
   aDifferenzaMesi INTEGER:=0;
   aMesiValidiCo INTEGER:=0;

   mesiAnticipo INTEGER;

   aDataDa DATE;
   aDataA DATE;
   aNumeroRifPersona INTEGER;
   aImportoDetrazione number(15,2);
   memTiPersona CARICO_FAMILIARE_ANAG.ti_persona%TYPE;
   memCodiceFiscale CARICO_FAMILIARE_ANAG.codice_fiscale%TYPE;
   aChiaveTecnica CARICO_FAMILIARE_ANAG.codice_fiscale%TYPE;
   aContatore INTEGER;
   aNumero INTEGER;
   aNumeroFi INTEGER:=0;
   aNumeroFiOld INTEGER:=0;
   aNumeroFiTotPerConguaglio INTEGER:=0;
   aNumeroCo INTEGER:=0;
   aNumeroAl INTEGER:=0;

   aRecCaricoFamAnag CARICO_FAMILIARE_ANAG%ROWTYPE;
   aRecDetrazioniFamiliari DETRAZIONI_FAMILIARI%ROWTYPE;

   gen_cur_car GenericCurTyp;

   -- Definizione tabella PL/SQL di appoggio calcolato detrazioni familiari

   indice BINARY_INTEGER;
   indice1 BINARY_INTEGER;

   tabella_num_car_fam CNRCTB545.numCaricoFamTab;
   tabella_car_fam_ok CNRCTB545.caricoFamTab;
   tabella_car_fam_tmp CNRCTB545.caricoFamTab;

   outTiPersona CHAR(1);
   aImportoDetrazTeoricaCo  NUMBER:=0;
   aImportoDetrazTeoricaFi  NUMBER:=0;
   aImportoDetrazTeoricaAl  NUMBER:=0;
   aImportoDetrazEffettivaCo  NUMBER(15,2):=0;
   aImportoDetrazEffettivaFi  NUMBER(15,2):=0;
   aImportoDetrazEffettivaAl  NUMBER(15,2):=0;

   aImportoDetrazTeoricaFiOld  NUMBER:=0;

   aImpDetrTeorPercFi   NUMBER(15,2):=0;
   aCoefficienteFi  NUMBER(15,4);
   aImpDetrTeorPercCo   NUMBER(15,2):=0;
   aCoefficienteCo  NUMBER(15,4);
   aImpDetrTeorPercAl   NUMBER(15,2):=0;
   aCoefficienteAl  NUMBER(15,4);

   aImpDetrTeorPercFiS   NUMBER(15,2):=0;
   aImportoDetrazTeoricaFiS  NUMBER:=0;

   --memorizzo i dati della tabella DETRAZIONI_FAMILIARI
   ImpDetrCo   NUMBER(15,2):=0;
   ImpMaggCo   NUMBER(15,2):=0;
   ImpMoltipCo   NUMBER(15,2):=0;
   ImpNumCo    NUMBER(15,2):=0;
   ImpDenCo  NUMBER(15,2):=0;
   ImpNumFi    NUMBER(15,2):=0;
   ImpDenFi  NUMBER(15,2):=0;
   ImpMoltipFi   NUMBER(15,2):=0;
   ImpNumAl    NUMBER(15,2):=0;
   ImpDenAl  NUMBER(15,2):=0;

   Esci    Exception;

   inEsercizioMcarriera NUMBER;
Begin
   -------------------------------------------------------------------------------------------------
   -- Controllo che esistano carichi familiari

   IF (CNRCTB080.chkEsisteDetrazFam (aCdAnag,
                                     aDtCompetenzaDa,
                                     aDtCompetenzaA) = 0) THEN
     Return;
   END IF;

   -- Controllo che i carichi familiari abbiano tutti il codice fiscale

   IF (CNRCTB080.chkEsisteCaricoFamNonValido (aCdAnag,
                                              aDtCompetenzaDa,
                                              aDtCompetenzaA) > 0) THEN
     IBMERR001.RAISE_ERR_GENERICO
       ('Non �ossibile proseguire! Esistono Carichi di Famiglia per i quali non �tato inserito il Codice Fiscale');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione variabili per il calcolo

   -- Azzeramento delle matrici dei carichi familiari (familiari e numero degli stessi per periodo)

   tabella_num_car_fam.DELETE;
   tabella_car_fam_ok.DELETE;
   tabella_car_fam_tmp.DELETE;

   indice:=0;
   indice1:=0;
   aContatore:=0;
   aChiaveTecnica:='TEMPKEY';

   -- Composizione dell'intervallo date in base alla competenza economica del compenso normalizzando le
   -- date di inizio e fine di competenza economica del compenso rispettivamente alla corrispondente data
   -- di inizio e fine mese

   aDataRifDa:=IBMUTL001.getFirstDayOfMonth(aDtCompetenzaDa);
   aDataRifA:=IBMUTL001.getLastDayOfMonth(aDtCompetenzaA);
   aDifferenzaMesi:=IBMUTL001.getMonthsBetween(aDataRifDa, aDataRifA);

   -- Costruzione a zero della matrice numero familiari per periodo (mese)
   FOR indice IN 1 .. aDifferenzaMesi

   LOOP

      IF indice = 1 THEN
         aDataDa:=aDataRifDa;
      ELSE
         aDataDa:=IBMUTL001.getAddMonth(aDataRifDa, indice - 1);
      END IF;
      aDataA:=IBMUTL001.getLastDayOfMonth(aDataDa);

      tabella_num_car_fam(indice).tDataDa:=aDataDa;
      tabella_num_car_fam(indice).tDataA:=aDataA;
      tabella_num_car_fam(indice).tNAltro:=0;
      tabella_num_car_fam(indice).tNConiuge:=0;
      tabella_num_car_fam(indice).tNFiglio:=0;

   END LOOP;

   -- Azzeramento campi di memoria per loop sui carichi familiari

   memTiPersona:=NULL;
   memCodiceFiscale:=NULL;

   --solo in caso di conguaglio, calcolo il numero di figli totali nell'anno
   --tale numero serve sia per il calcolo della deduzione teorica, sia per la formiula di calcolo
   --di quella spettante
   If aOrigineCompenso=CNRCTB545.isCompensoConguaglio Then
      Begin
         Select Count(1)
         Into aNumeroFiTotPerConguaglio
         From CARICO_FAMILIARE_ANAG
         Where  cd_anag = aCdAnag AND
                ti_persona = 'F' AND
                (
                 To_Char(dt_ini_validita,'yyyy')<= To_Char(aDtRegistrazione,'yyyy')
                  And
                 To_Char(dt_fin_validita,'yyyy')>= To_Char(aDtRegistrazione,'yyyy')
                 );
      Exception
          When No_Data_Found Then
              aNumeroFiTotPerConguaglio := 0;
      End;
   End If;
   -------------------------------------------------------------------------------------------------
   -- Ciclo lettura carichi familiari. Si esclude il carico convenzionale per la gestione delle
   -- detrazioni per lavoro assimilato. L'intervallo data di validit� costretto nel periodo di
   -- competenza economica del compenso
   BEGIN
      OPEN gen_cur_car FOR

           SELECT *
           FROM   CARICO_FAMILIARE_ANAG
           WHERE  cd_anag = aCdAnag AND
                  ti_persona != 'L' And
                  (
                      (dt_ini_validita <= aDataRifDa AND
                       dt_fin_validita >= aDataRifDa)
                   OR
                      (dt_ini_validita <= aDataRifA AND
                       dt_fin_validita >= aDataRifA)
                   OR
                      (dt_ini_validita >= aDataRifDa AND
                       dt_fin_validita <= aDataRifA)
                  )
           ORDER BY ti_persona, codice_fiscale, dt_ini_validita;

      LOOP
         FETCH gen_cur_car INTO
               aRecCaricoFamAnag;

         EXIT WHEN gen_cur_car%NOTFOUND;
   Begin
     --Controllo se �n Conguaglio e se la data di fine validit�el carico �nterna all'anno
           --in tal caso non ha diritto
           /*
           If aOrigineCompenso=CNRCTB545.isCompensoConguaglio And
              aRecCaricoFamAnag.dt_fin_validita < aDtCompetenzaA Then
             Raise Esci;
           End If;
     */
           aContatore:=aContatore + 1;

           -- Normalizzazione date, costrizione nel periodo di competenza economica del compenso

           aDataDa:=IBMUTL001.getFirstDayOfMonth(aRecCaricoFamAnag.dt_ini_validita);
           aDataA:=IBMUTL001.getLastDayOfMonth(aRecCaricoFamAnag.dt_fin_validita);
           IF aDataDa < aDataRifDa THEN
              aDataDa:=aDataRifDa;
           END IF;
           IF aDataA > aDataRifA THEN
              aDataA:=aDataRifA;
           END IF;
           aRecCaricoFamAnag.dt_ini_validita:=aDataDa;
           aRecCaricoFamAnag.dt_fin_validita:=aDataA;

           -- Se il campo del codice fiscale non �alorizzato allora si considera ogni record come
           -- distinto dagli altri

           IF aRecCaricoFamAnag.codice_fiscale IS NULL THEN
              aRecCaricoFamAnag.codice_fiscale:=aChiaveTecnica || LPAD(aContatore,5,0);
           END IF;

           -- Se prima volta azzero tabella tmp

           IF memCodiceFiscale IS NULL THEN
              tabella_car_fam_tmp.DELETE;
              indice1:=0;
              memTiPersona:=aRecCaricoFamAnag.ti_persona;
              memCodiceFiscale:=aRecCaricoFamAnag.codice_fiscale;
           END IF;

           -- Il record letto �dentico al precedente

           IF (aRecCaricoFamAnag.ti_persona = memTiPersona AND
               aRecCaricoFamAnag.codice_fiscale = memCodiceFiscale) THEN
              riempiCarFamTmp (indice1,
                               aRecCaricoFamAnag,
                               tabella_car_fam_tmp);
           ELSE
              scaricaCarFamTmp(indice,
                               tabella_car_fam_ok,
                               tabella_car_fam_tmp);
              tabella_car_fam_tmp.DELETE;
              indice1:=0;
              memTiPersona:=aRecCaricoFamAnag.ti_persona;
              memCodiceFiscale:=aRecCaricoFamAnag.codice_fiscale;
              riempiCarFamTmp (indice1,
                               aRecCaricoFamAnag,
                               tabella_car_fam_tmp);
           END IF;
         Exception
           When Esci Then
             Null;
         End;
      END LOOP;

      CLOSE gen_cur_car;

      If aContatore > 0 Then
        scaricaCarFamTmp(indice,
                         tabella_car_fam_ok,
                         tabella_car_fam_tmp);
      End If;

      -- Aggiornamento della matrice numeri persone per periodo

      Begin
       If  tabella_car_fam_ok.COUNT > 0 Then
         For indice IN tabella_car_fam_ok.FIRST .. tabella_car_fam_ok.LAST Loop
            For indice1 IN tabella_num_car_fam.FIRST .. tabella_num_car_fam.LAST Loop
               IF (tabella_car_fam_ok(indice).tDataDa <= tabella_num_car_fam(indice1).tDataDa AND
                   tabella_car_fam_ok(indice).tDataA >= tabella_num_car_fam(indice1).tDataA ) THEN
                   IF    tabella_car_fam_ok(indice).tTiPersona = 'A' THEN
                         tabella_num_car_fam(indice1).tNAltro:=tabella_num_car_fam(indice1).tNAltro + 1;
                   ELSIF tabella_car_fam_ok(indice).tTiPersona In ('C','G') THEN
                         tabella_num_car_fam(indice1).tNConiuge:=tabella_num_car_fam(indice1).tNConiuge + 1;
                   ELSIF tabella_car_fam_ok(indice).tTiPersona = 'F' THEN
                         tabella_num_car_fam(indice1).tNFiglio:=tabella_num_car_fam(indice1).tNFiglio + 1;
                   END IF;
               END IF;
            End Loop;
         End Loop;
       End If;
      End;

   END;

      -- Calcolo delle detrazioni teoriche per ogni carico di famiglia
   Begin
    If  tabella_car_fam_ok.COUNT > 0 Then
    --invertiti i due loop......
      For indice1 IN tabella_num_car_fam.FIRST .. tabella_num_car_fam.LAST Loop
        For indice IN tabella_car_fam_ok.FIRST .. tabella_car_fam_ok.LAST Loop
            IF (tabella_car_fam_ok(indice).tDataDa <= tabella_num_car_fam(indice1).tDataDa AND
                tabella_car_fam_ok(indice).tDataA >= tabella_num_car_fam(indice1).tDataA ) Then

          Begin
            If tabella_car_fam_ok(indice).tTiPersona = 'A' Then
              outTiPersona:='A';
            Elsif tabella_car_fam_ok(indice).tTiPersona In ('C','G') Then
              outTiPersona:='C';
            Elsif tabella_car_fam_ok(indice).tTiPersona = 'F' Then
              If tabella_car_fam_ok(indice).tDtMinoreTre Is Not Null And
                 tabella_car_fam_ok(indice).tDtMinoreTre > tabella_num_car_fam(indice1).tDataDA Then
                If tabella_car_fam_ok(indice).tFlHandicap = 'Y' Then
                    outTiPersona:='K';
                Else
                    outTiPersona:='B';
                End If;
              Elsif tabella_car_fam_ok(indice).tFlHandicap = 'Y' Then
                outTiPersona:='H';
              Else
                outTiPersona:='F';
              End If;
            End If;
          End;

              Begin

                  IF    tabella_car_fam_ok(indice).tTiPersona = 'A' THEN
                        aNumero:=1;
                        aNumeroAl:=tabella_num_car_fam(indice1).tNAltro;
                  ELSIF tabella_car_fam_ok(indice).tTiPersona In ('C','G') THEN
                        aNumero:=1;
                        aNumeroCo:=tabella_num_car_fam(indice1).tNConiuge;
                  ELSIF tabella_car_fam_ok(indice).tTiPersona = 'F' Then
                        aNumero:=tabella_num_car_fam(indice1).tNFiglio;
                        aNumeroFi:=tabella_num_car_fam(indice1).tNFiglio;       -- Numero tot figli
                  END IF;

     If aNumeroCo > 1 Then
          IBMERR001.RAISE_ERR_GENERICO
                      ('Non �ossibile proseguire! Esistono pi� Carichi di Famiglia di tipo "Coniuge" validi per il Terzo in oggetto');
     End If;


                  SELECT * into aRecDetrazioniFamiliari
                  FROM   DETRAZIONI_FAMILIARI
                  WHERE  ti_persona = outTiPersona AND
                         numero = 1 AND
                         dt_inizio_validita <= aDtRegistrazione AND
                         dt_fine_validita >= aDtRegistrazione AND
                         im_inferiore <= aImportoRiferimento AND
                         im_superiore >= aImportoRiferimento;
                Exception
                   WHEN no_data_found THEN
                        IBMERR001.RAISE_ERR_GENERICO
                        ('Record scaglione per detrazioni familiari non definito');
                   When Too_Many_Rows Then
                        IBMERR001.RAISE_ERR_GENERICO
                        ('Esistono Record scaglione per detrazioni familiari doppi');
                End;

    If outTiPersona In ('F','B','H','K') Then
        --sono uguali per tutti
        If tabella_car_fam_ok(indice).tPrcCarico Not In (0,50,100) Then
             IBMERR001.RAISE_ERR_GENERICO
                        ('Esistono Percentuali di Carico per Carichi di Famiglia di tipo "Figlio" non consentite');
        End If;
        ImpNumFi := aRecDetrazioniFamiliari.numeratore;
        ImpDenFi := aRecDetrazioniFamiliari.denominatore;
        ImpMoltipFi := aRecDetrazioniFamiliari.moltiplicatore;
        If aOrigineCompenso!=CNRCTB545.isCompensoConguaglio THEN
          If tabella_num_car_fam(indice1).tNFiglio <= 3 Then
             aImpDetrTeorPercFi:= ROUND((aRecDetrazioniFamiliari.im_detrazione * tabella_car_fam_ok(indice).tPrcCarico / 100),2);
          Else
             aImpDetrTeorPercFi:= ROUND(((aRecDetrazioniFamiliari.im_detrazione + aRecDetrazioniFamiliari.im_maggiorazione) * tabella_car_fam_ok(indice).tPrcCarico / 100),2);
          End If;
        Else
          If aNumeroFiTotPerConguaglio <= 3 Then
             aImpDetrTeorPercFi:= ROUND((aRecDetrazioniFamiliari.im_detrazione * tabella_car_fam_ok(indice).tPrcCarico / 100),2);
          Else
             aImpDetrTeorPercFi:= ROUND(((aRecDetrazioniFamiliari.im_detrazione + aRecDetrazioniFamiliari.im_maggiorazione) * tabella_car_fam_ok(indice).tPrcCarico / 100),2);
          End If;
        End If;

        aImportoDetrazTeoricaFiOld:= aImportoDetrazTeoricaFi;
        aImportoDetrazTeoricaFi := aImportoDetrazTeoricaFi + (aImpDetrTeorPercFi/12);

              If aOrigineCompenso!=CNRCTB545.isCompensoConguaglio THEN
            --se il numero dei figli �ambiato applico la formula
            --(forse si pu�iminare anche per i singoli compensi)
            If aNumeroFiOld > 0 And aNumeroFi > aNumeroFiOld Then
                aCoefficienteFi := Trunc(((ImpNumFi + (ImpMoltipFi*(aNumeroFiOld -1)))- aImportoRiferimento)/(ImpNumFi + (ImpMoltipFi*(aNumeroFiOld -1))),4);

                If aCoefficienteFi <= 0 Or aCoefficienteFi = 1 Then
                          aImportoDetrazFi := 0;
                      Else
                              aImportoDetrazFi := aCoefficienteFi * aImportoDetrazTeoricaFiOld;
                      End If;

           aImportoDetrazTeoricaFi := (aImpDetrTeorPercFi/12);
            End If;
            aNumeroFiOld := aNumeroFi;
         End If;
         If aOrigineCompenso=CNRCTB545.isCompensoConguaglio
            And esisteFiSenzaConiuge = 'Y' And tabella_car_fam_ok(indice).tFlPrimoFiglioMancaCon = 'Y' Then
            If aNumeroFiTotPerConguaglio <= 3 Then
             aImpDetrTeorPercFiS:= ROUND((aRecDetrazioniFamiliari.im_detrazione * tabella_car_fam_ok(indice).tPrcCarico / 100),2);
            Else
             aImpDetrTeorPercFiS:= ROUND(((aRecDetrazioniFamiliari.im_detrazione + aRecDetrazioniFamiliari.im_maggiorazione) * tabella_car_fam_ok(indice).tPrcCarico / 100),2);
            End If;
            aImportoDetrazTeoricaFiS := aImportoDetrazTeoricaFiS + (aImpDetrTeorPercFiS/12);
         End If;
--pipe.send_message('aImportoDetrazTeoricaFi '||aImportoDetrazTeoricaFi);
    Elsif outTiPersona = 'C' Then
        aMesiValidiCo:=aMesiValidiCo+1;
        ImpDetrCo := aRecDetrazioniFamiliari.im_detrazione;
        ImpMaggCo := aRecDetrazioniFamiliari.im_maggiorazione;
        --se �n conguaglio la maggiorazione non va rapportata al periodo
        If aOrigineCompenso=CNRCTB545.isCompensoConguaglio Then
            aImportoDetrazTeoricaCo := aImportoDetrazTeoricaCo + ((ImpDetrCo /*+ ImpMaggCo*/)/12);
        Else
            aImportoDetrazTeoricaCo := aImportoDetrazTeoricaCo + ((ImpDetrCo + ImpMaggCo)/12);
        End If;
        ImpMoltipCo := aRecDetrazioniFamiliari.moltiplicatore;
        ImpNumCo := aRecDetrazioniFamiliari.numeratore;
        ImpDenCo := aRecDetrazioniFamiliari.denominatore;
--pipe.send_message('aImportoDetrazTeoricaCo '||aImportoDetrazTeoricaCo);
    Elsif outTiPersona = 'A' Then
        ImpNumAl := aRecDetrazioniFamiliari.numeratore;
        ImpDenAl := aRecDetrazioniFamiliari.denominatore;
        aImpDetrTeorPercAl:= ROUND((aRecDetrazioniFamiliari.im_detrazione * tabella_car_fam_ok(indice).tPrcCarico / 100),2);
        aImportoDetrazTeoricaAl := aImportoDetrazTeoricaAl + (aImpDetrTeorPercAl/12);
    End If;

            END IF;

         END LOOP;

      END LOOP;

      --Ho le detrazioni teoriche, ora calcolo quelle effettive
      --Per i figli

      If aNumeroFi > 0 Then
          If aOrigineCompenso!=CNRCTB545.isCompensoConguaglio THEN
             aCoefficienteFi := Trunc(((ImpNumFi + (ImpMoltipFi*(aNumeroFi -1)))- aImportoRiferimento)/(ImpNumFi + (ImpMoltipFi*(aNumeroFi -1))),4);
          Else    --�n conguaglio
             aCoefficienteFi := Trunc(((ImpNumFi + (ImpMoltipFi*(aNumeroFiTotPerConguaglio -1)))- aImportoRiferimento)/(ImpNumFi + (ImpMoltipFi*(aNumeroFiTotPerConguaglio -1))),4);
          End If;
          If aCoefficienteFi <= 0 Or aCoefficienteFi = 1 Then
             aImportoDetrazFi := 0;
          Else
             aImportoDetrazFi := aImportoDetrazFi + (aCoefficienteFi * aImportoDetrazTeoricaFi);
          End If;
--pipe.send_message('aCoefficienteFi '||aCoefficienteFi);
--pipe.send_message('aImportoDetrazFi '||aImportoDetrazFi);
          --calcolo aImportoDetrazFiS
          If aOrigineCompenso=CNRCTB545.isCompensoConguaglio And esisteFiSenzaConiuge = 'Y' Then
            If aCoefficienteFi <= 0 Or aCoefficienteFi = 1 Then
          aImportoDetrazFiS := 0;
            Else
                aImportoDetrazFiS := aImportoDetrazFiS + (aCoefficienteFi * aImportoDetrazTeoricaFiS);
            End If;
          End If;
      End If;
      --Per il coniuge
      -- devo dividere la detrazione effettiva per 12 e non quella teorica, quindi poi devo moltiplicare
      -- per aDifferenzaMesi
      If aNumeroCo > 0 Then
        If aImportoRiferimento <= 15000 Then
            aCoefficienteCo := Trunc(aImportoRiferimento/ImpDenCo,4);
            If aCoefficienteCo = 1 Then
                aImportoDetrazCo := 690;
            Elsif aCoefficienteCo = 0 Then
                aImportoDetrazCo := 0;
            Else
                --se �n conguaglio la maggiorazione non va rapportata al periodo
    If aOrigineCompenso=CNRCTB545.isCompensoConguaglio Then
                   aImportoDetrazCo := aImportoDetrazTeoricaCo - ((ImpMoltipCo * aCoefficienteCo)/12*aMesiValidiCo) + ImpMaggCo;
                Else
                   aImportoDetrazCo := aImportoDetrazTeoricaCo - ((ImpMoltipCo * aCoefficienteCo)/12*aMesiValidiCo);
                End If;
            End If;
        Elsif aImportoRiferimento <= 40000 Then
      --se �n conguaglio la maggiorazione non va rapportata al periodo
      If aOrigineCompenso=CNRCTB545.isCompensoConguaglio Then
                aImportoDetrazCo := aImportoDetrazTeoricaCo + ImpMaggCo;
            Else
                aImportoDetrazCo := aImportoDetrazTeoricaCo;
            End If;
        Elsif aImportoRiferimento <= 80000 Then
      aCoefficienteCo := Trunc((ImpNumCo - aImportoRiferimento)/ImpDenCo,4);
      If aCoefficienteCo = 0 Then
                aImportoDetrazCo := 0;
            Else
                --se �n conguaglio la maggiorazione non va rapportata al periodo
    If aOrigineCompenso=CNRCTB545.isCompensoConguaglio Then
                   aImportoDetrazCo := aImportoDetrazTeoricaCo * aCoefficienteCo + ImpMaggCo;
                Else
                   aImportoDetrazCo := aImportoDetrazTeoricaCo * aCoefficienteCo;
                End If;
            End If;
        Else
            aImportoDetrazCo := 0;
        End If;
      End If;

      --Per altri familiari a carico
      If aNumeroAl > 0 Then
        aCoefficienteAl := Trunc((ImpNumAl - aImportoRiferimento)/ImpDenAl,4);

        If aCoefficienteAl <= 0 Or aCoefficienteAl = 1 Then
            aImportoDetrazAl := 0;
        Else
            aImportoDetrazAl := aImportoDetrazTeoricaAl * aCoefficienteAl;
        End If;
      End If;

    End If;               --If  tabella_car_fam_ok.COUNT > 0 Then
   END;

END calcolaDetrazioniFam;

-- =================================================================================================
-- Calcolo delle detrazioni personali
-- =================================================================================================
PROCEDURE calcolaDetrazioniPer
   (
    aImportoRiferimento NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE,
    aDtCompetenzaDa DATE,
    aDtCompetenzaA DATE,
    aDtRegistrazione DATE,
    aEsercizio COMPENSO.esercizio%TYPE,
    aImportoDetrazPe IN OUT NUMBER
   ) IS
   aNumeroGG INTEGER;
   aGGAnno INTEGER;
   importoDetrazione NUMBER(15,2);
   aRecDetrazioniLavoro DETRAZIONI_LAVORO%ROWTYPE;
   aEsercizioRif COMPENSO.esercizio%TYPE;

   aCoefficientePe  NUMBER(15,4);
BEGIN

   -------------------------------------------------------------------------------------------------
   -- Determinazione giorni

   aEsercizioRif:=aEsercizio;
   IF aEsercizioRif != TO_NUMBER(TO_CHAR(aDtRegistrazione,'YYYY')) THEN
      aEsercizioRif:= TO_NUMBER(TO_CHAR(aDtRegistrazione,'YYYY'));
   END IF;

   aGGAnno:=IBMUTL001.getDaysBetween(TO_DATE('0101' || aEsercizioRif,'DDMMYYYY'),
                                     TO_DATE('3112' || aEsercizioRif,'DDMMYYYY'));
   aNumeroGG:=IBMUTL001.getDaysBetween(aDtCompetenzaDa, aDtCompetenzaA);
   IF aNumeroGG > aGGAnno THEN
      aNumeroGG:=aGGAnno;
   END IF;
   -------------------------------------------------------------------------------------------------
   -- Calcolo delle detrazioni

   BEGIN

      SELECT * INTO aRecDetrazioniLavoro
      FROM   DETRAZIONI_LAVORO
      WHERE  ti_lavoro = 'D' AND
             dt_inizio_validita <= aDtRegistrazione AND
             dt_fine_validita >= aDtRegistrazione AND
             im_inferiore <= aImportoRiferimento AND
             im_superiore >= aImportoRiferimento;

   EXCEPTION

      WHEN no_data_found THEN
           IBMERR001.RAISE_ERR_GENERICO
              ('Record scaglione per detrazioni personali non definito');

   END;
/* PRIMA DELLA MODIFICA DETRAZIONE A PARTIRE DAL 01/01/2014
   If aImportoRiferimento <= 8000 Then
        aImportoDetrazPe := aRecDetrazioniLavoro.im_detrazione;
   Elsif aImportoRiferimento <= 15000 Then
         aCoefficientePe := Trunc((aRecDetrazioniLavoro.numeratore - aImportoRiferimento)/aRecDetrazioniLavoro.denominatore,4);
         aImportoDetrazPe :=
           aRecDetrazioniLavoro.im_detrazione +
           (aRecDetrazioniLavoro.moltiplicatore * aCoefficientePe);
   Elsif aImportoRiferimento <= 55000 Then
         aCoefficientePe := Trunc((aRecDetrazioniLavoro.numeratore - aImportoRiferimento)/aRecDetrazioniLavoro.denominatore,4);
         aImportoDetrazPe :=
          (aRecDetrazioniLavoro.im_detrazione * aCoefficientePe);
         --aggiungo l'eventuale maggiorazione ma solo se non �n conguaglio
         --(perch�ltrimenti verrebbe aggiunta per ogni intervallo del conguaglio)
         If aOrigineCompenso!=CNRCTB545.isCompensoConguaglio Then
            aImportoDetrazPe := aImportoDetrazPe + aRecDetrazioniLavoro.im_maggiorazione;
         End If;
   Else
         aImportoDetrazPe := 0;
   End If;
 */
   If aRecDetrazioniLavoro.moltiplicatore = 1 and aRecDetrazioniLavoro.numeratore = 1 Then
          aImportoDetrazPe := aRecDetrazioniLavoro.im_detrazione;
   Elsif aRecDetrazioniLavoro.moltiplicatore = 1 Then
          aCoefficientePe := Trunc((aRecDetrazioniLavoro.numeratore - aImportoRiferimento)/aRecDetrazioniLavoro.denominatore,4);
          aImportoDetrazPe := (aRecDetrazioniLavoro.im_detrazione * aCoefficientePe);
          --aggiungo l'eventuale maggiorazione ma solo se non �n conguaglio
          --(perch�ltrimenti verrebbe aggiunta per ogni intervallo del conguaglio)
          If aOrigineCompenso!=CNRCTB545.isCompensoConguaglio Then
              aImportoDetrazPe := aImportoDetrazPe + aRecDetrazioniLavoro.im_maggiorazione;
          End If;
   Else
          aCoefficientePe := Trunc((aRecDetrazioniLavoro.numeratore - aImportoRiferimento)/aRecDetrazioniLavoro.denominatore,4);
          aImportoDetrazPe := aRecDetrazioniLavoro.im_detrazione +
                              (aRecDetrazioniLavoro.moltiplicatore * aCoefficientePe);
   End If;
   if aImportoRiferimento between aRecDetrazioniLavoro.im_inf_incr_fisso and aRecDetrazioniLavoro.im_sup_incr_fisso Then
      aImportoDetrazPe := aImportoDetrazPe + aRecDetrazioniLavoro.im_incr_fisso;
   end if;
   --Rapporto la detrazione al numero di giorni effettivo
   IF aNumeroGG = aGGAnno THEN
      aImportoDetrazPe:=aImportoDetrazPe;
   ELSE
      aImportoDetrazPe:=ROUND(((aImportoDetrazPe / aGGAnno) * aNumeroGG),2);
   END IF;

End calcolaDetrazioniPer;

-- =================================================================================================
-- Calcolo altri tipi di detrazioni
-- =================================================================================================
PROCEDURE calcolaDetrazioniAltriTipi
   (
    aImportoRiferimento NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE,
    aDtCompetenzaDa DATE,
    aDtCompetenzaA DATE,
    aDtRegistrazione DATE,
    aEsercizio COMPENSO.esercizio%TYPE,
    aImportoDetrazLa IN OUT NUMBER
   ) Is
   aDataRifDa DATE;
   aDataRifA DATE;
   aDifferenzaMesi INTEGER;
   aMMAnno INTEGER;

   importoDetrazione NUMBER(15,2);
   aRecDetrazioniLavoro DETRAZIONI_LAVORO%ROWTYPE;

   aCoefficienteLa  NUMBER(15,4);
Begin

   aDataRifDa:=IBMUTL001.getFirstDayOfMonth(aDtCompetenzaDa);
   aDataRifA:=IBMUTL001.getLastDayOfMonth(aDtCompetenzaA);
   aDifferenzaMesi:=IBMUTL001.getMonthsBetween(aDataRifDa, aDataRifA);

   aMMAnno:=IBMUTL001.getMonthsBetween(TO_DATE('0101' || aRecCompenso.esercizio,'DDMMYYYY'),
                                       TO_DATE('3112' || aRecCompenso.esercizio,'DDMMYYYY'));

   If aDifferenzaMesi > aMMAnno Then
      aDifferenzaMesi := aMMAnno;
   End If;

   -- Calcolo delle detrazioni

   BEGIN

      SELECT * INTO aRecDetrazioniLavoro
      FROM   DETRAZIONI_LAVORO
      WHERE  ti_lavoro = 'A' AND
             dt_inizio_validita <= aDtRegistrazione AND
             dt_fine_validita >= aDtRegistrazione AND
             im_inferiore <= aImportoRiferimento AND
             im_superiore >= aImportoRiferimento;

   EXCEPTION

      WHEN no_data_found THEN
           IBMERR001.RAISE_ERR_GENERICO
              ('Record scaglione per altri tipi di detrazioni non definito');

   END;

   /* Finanziaria 2007
   If aImportoRiferimento <= 4800 Then
        aImportoDetrazLa := aRecDetrazioniLavoro.im_detrazione;
   Elsif aImportoRiferimento <= 55000 Then
    aCoefficienteLa := Trunc((aRecDetrazioniLavoro.numeratore - aImportoRiferimento)/aRecDetrazioniLavoro.denominatore,4);
  aImportoDetrazLa :=
       aRecDetrazioniLavoro.im_detrazione * aCoefficienteLa;
   Else
    aImportoDetrazLa := 0;
   End If;
   */

   If aRecDetrazioniLavoro.moltiplicatore = 1 and aRecDetrazioniLavoro.numeratore = 1 Then
          aImportoDetrazLa := aRecDetrazioniLavoro.im_detrazione;
   Elsif aRecDetrazioniLavoro.moltiplicatore = 1 Then
          aCoefficienteLa := Trunc((aRecDetrazioniLavoro.numeratore - aImportoRiferimento)/aRecDetrazioniLavoro.denominatore,4);
          aImportoDetrazLa := (aRecDetrazioniLavoro.im_detrazione * aCoefficienteLa);
   Else
          aCoefficienteLa := Trunc((aRecDetrazioniLavoro.numeratore - aImportoRiferimento)/aRecDetrazioniLavoro.denominatore,4);
          aImportoDetrazLa := aRecDetrazioniLavoro.im_detrazione +
                              (aRecDetrazioniLavoro.moltiplicatore * aCoefficienteLa);
   End If;
   if aImportoRiferimento between aRecDetrazioniLavoro.im_inf_incr_fisso and aRecDetrazioniLavoro.im_sup_incr_fisso Then
      aImportoDetrazLa := aImportoDetrazLa + aRecDetrazioniLavoro.im_incr_fisso;
   end if;

   If aDifferenzaMesi = aMMAnno Then
      aImportoDetrazLa:=aImportoDetrazLa;
   Else
      aImportoDetrazLa:= ROUND(((aImportoDetrazLa / aMMAnno) * aDifferenzaMesi),2);
   End If;
End calcolaDetrazioniAltriTipi;

-- =================================================================================================
-- Calcolo del credito irpef
-- =================================================================================================
PROCEDURE calcolaCreditoIrpef
      (
       --indice BINARY_INTEGER,
       aImportoRiferimento IN NUMBER,
       inNumGGTotMinPerCredito IN INTEGER,
       aRecCompenso IN COMPENSO%ROWTYPE,
       cdCori IN TIPO_CONTRIBUTO_RITENUTA.CD_CONTRIBUTO_RITENUTA%TYPE,
       aCredito IN OUT NUMBER,
       PAREGGIO_DETRAZIONI IN OUT VARCHAR2) IS

   aCreditoCorrente NUMBER(15,2);
   aCreditoArretrato NUMBER(15,2);
   aCreditoArretratoTot NUMBER(15,2);
   aCreditoGiaCalcolato NUMBER(15,2);
   aCreditoTotAnno NUMBER(15,2);
   aCoefficiente    NUMBER(15,4);

   aEsercizioRif COMPENSO.esercizio%TYPE;
   aRecCreditoIrpef CREDITO_IRPEF%ROWTYPE;

   inDtDaCompCreditoCorrente COMPENSO.dt_da_competenza_coge%TYPE;
   inDtACompCreditoCorrente COMPENSO.dt_a_competenza_coge%TYPE;

   aGGAnno INTEGER;
   aNumeroGG INTEGER;
   aNumeroGGEsclusi INTEGER;

   aGiorniArrMax INTEGER;
   aNumeroGGArretrato INTEGER;
   --aNumeroGGRateNonAssComp INTEGER;
   aNumeroGGRateTotCreditoCorr INTEGER;
   aCreditoBase NUMBER(15,2) := 0;
   dataInizioGestioneCuneoFiscale date;
BEGIN
   aCredito :=0;
   aCreditoCorrente :=0;
   aCreditoArretrato :=0;
   aCreditoArretratoTot:=0;
   aCreditoGiaCalcolato:=0;
   aCreditoTotAnno:=0;
   aCoefficiente:=1;

   aNumeroGGEsclusi :=0;

   aGiorniArrMax :=0;
   aNumeroGGArretrato :=0;
   --aNumeroGGRateNonAssComp :=0;
   aNumeroGGRateTotCreditoCorr:=0;

 BEGIN
--pipe.send_message('entro in calcolaCreditoIrpef ');
--pipe.send_message('aImportoRiferimento = '||aImportoRiferimento);
--pipe.send_message('glbFlNoCreditoIrpef = '||glbFlNoCreditoIrpef);
   --�revista la gestione ed il terzo non ha chiesto che non sia applicato
   dataInizioGestioneCuneoFiscale := CNRCTB015.getDt01PerChiave('0', 'RIDUZIONE_CUNEO_DL_3_2020', 'DATA_INIZIO');

   If ((aRecCompenso.dt_a_competenza_coge < dataInizioGestioneCuneoFiscale and glbFlNoCreditoIrpef != 'Y') or
        (aRecCompenso.dt_a_competenza_coge >= dataInizioGestioneCuneoFiscale and glbFlNoCreditoCuneoIrpef != 'Y') or
        (aRecCompenso.dt_a_competenza_coge >= dataInizioGestioneCuneoFiscale and glbFlNoDetrCuneoIrpef != 'Y'))Then
        -------------------------------------------------------------------------------------------------
        -- Determinazione giorni

        aEsercizioRif:=aRecCompenso.Esercizio;
        IF aEsercizioRif != TO_NUMBER(TO_CHAR(aRecCompenso.dt_registrazione,'YYYY')) THEN
              RETURN;
        END IF;
        -------------------------------------------------------------------------------------------------
        -- Leggo parametri per credito
        BEGIN
           SELECT * INTO aRecCreditoIrpef
           FROM   CREDITO_IRPEF
           WHERE  dt_inizio_validita <= aRecCompenso.dt_da_competenza_coge AND
                  dt_fine_validita >= aRecCompenso.dt_a_competenza_coge AND
                  im_inferiore <= aImportoRiferimento AND
                  im_superiore >= aImportoRiferimento;

           PAREGGIO_DETRAZIONI := aRecCreditoIrpef.fl_pareggio_detrazioni;
        EXCEPTION
           WHEN no_data_found THEN
--pipe.send_message('Nessuna riga in CREDITO_IRPEF');
                RETURN;
           WHEN OTHERS THEN
              IBMERR001.RAISE_ERR_GENERICO ('Errore nel recupero dei dati per il calcolo del credito irpef.');
        END;
        aGGAnno:=IBMUTL001.getDaysBetween(aRecCreditoIrpef.dt_inizio_validita,
                                                aRecCreditoIrpef.dt_fine_validita);

--pipe.send_message('aRecCreditoIrpef.im_credito = '||aRecCreditoIrpef.im_credito);
        IF aRecCreditoIrpef.im_credito = 0 then
            RETURN;
        END IF;
        IF ((aRecCompenso.dt_da_competenza_coge >= aRecCreditoIrpef.dt_inizio_applicazione
             AND
             aRecCompenso.dt_da_competenza_coge <= aRecCreditoIrpef.dt_fine_validita)
           OR
            (aRecCompenso.dt_a_competenza_coge <= aRecCreditoIrpef.dt_fine_validita
             AND
             aRecCompenso.dt_a_competenza_coge >= aRecCreditoIrpef.dt_inizio_applicazione)
             OR
            (aRecCompenso.dt_da_competenza_coge <= aRecCreditoIrpef.dt_fine_validita
             AND
             aRecCompenso.dt_a_competenza_coge >= aRecCreditoIrpef.dt_inizio_applicazione)) THEN

             --Calcolo coefficiente
             If aRecCreditoIrpef.fl_applica_formula = 'Y' then
                 aCoefficiente := TRUNC(((aRecCreditoIrpef.im_superiore - aImportoRiferimento)/(aRecCreditoIrpef.im_superiore - aRecCreditoIrpef.im_inferiore)),4);
             end if;
--pipe.send_message('aCoefficiente = '||aCoefficiente);
             -- calcolo il credito totale rapportato agli effettivi giorni che lavorer�ell'anno
             aCreditoTotAnno:= ROUND(((aRecCreditoIrpef.im_credito/aGGAnno)* inNumGGTotMinPerCredito),2);
--pipe.send_message('aCreditoTotAnno = '||aCreditoTotAnno);
             -- applico il coefficiente
             aCreditoTotAnno := aCreditoTotAnno * aCoefficiente;
             aCreditoTotAnno := aCreditoTotAnno + aRecCreditoIrpef.im_credito_base;

--pipe.send_message('aCreditoTotAnno con coefficiente = '||aCreditoTotAnno);
             -- prima di calcolare il credito controllo che non abbia gi�icevuto tutto il credito
             aCreditoGiaCalcolato:= getCreditoGiaCalcolato(aRecCompenso.cd_cds,
                                                           aRecCompenso.cd_unita_organizzativa,
                                                           aRecCompenso.esercizio,
                                                           aRecCompenso.pg_compenso,
                                                           aRecCompenso.cd_terzo,
                                                           cdCori);
--pipe.send_message('aCreditoGiaCalcolato = '||aCreditoGiaCalcolato);
--pipe.send_message('ABS(aCreditoGiaCalcolato) = '||ABS(aCreditoGiaCalcolato));
             If ABS(aCreditoGiaCalcolato) < aCreditoTotAnno then

                 --se la competenza del compenso �nche esterna al periodo di applicazione del credito la riconduco ad esso
                 if aRecCompenso.dt_da_competenza_coge < aRecCreditoIrpef.dt_inizio_applicazione then
                     inDtDaCompCreditoCorrente := aRecCreditoIrpef.dt_inizio_applicazione;
                 else
                     inDtDaCompCreditoCorrente := aRecCompenso.dt_da_competenza_coge;
                 end if;
                 if aRecCompenso.dt_a_competenza_coge > aRecCreditoIrpef.dt_fine_validita then
                     inDtACompCreditoCorrente := aRecCreditoIrpef.dt_fine_validita;
                 else
                     inDtACompCreditoCorrente := aRecCompenso.dt_a_competenza_coge;
                 end if;

                 aNumeroGG:=IBMUTL001.getDaysBetween(inDtDaCompCreditoCorrente, inDtACompCreditoCorrente);
                 IF aNumeroGG > aGGAnno THEN
                    aNumeroGG:=aGGAnno;
                 END IF;

                 -- calcolo il numero di giorni del compenso che non sono inclusi nel calcolo del credito corrente
                 -- ma devono esserlo nel calcolo del credito arretrato
                 if aRecCompenso.dt_da_competenza_coge < aRecCreditoIrpef.dt_inizio_applicazione and
                    aRecCompenso.dt_da_competenza_coge >= aRecCreditoIrpef.dt_inizio_validita then
                     aNumeroGGEsclusi:=IBMUTL001.getDaysBetween(aRecCompenso.dt_da_competenza_coge, (aRecCreditoIrpef.dt_inizio_applicazione-1));
                 elsif aRecCompenso.dt_da_competenza_coge < aRecCreditoIrpef.dt_inizio_applicazione and
                    aRecCompenso.dt_da_competenza_coge < aRecCreditoIrpef.dt_inizio_validita then
                     aNumeroGGEsclusi:=IBMUTL001.getDaysBetween(aRecCreditoIrpef.dt_inizio_validita, (aRecCreditoIrpef.dt_inizio_applicazione-1));
                 end if;
--pipe.send_message('aNumeroGG = '||aNumeroGG);
--pipe.send_message('aNumeroGGEsclusi = '||aNumeroGGEsclusi);
                -- aCredito sar�a somma di aCreditoCorrente (credito per il periodo di applicazione) +
                -- aCreditoArretrato (credito per i mesi del periodo compreso dalla validit�ll'applicazione)

                aCreditoCorrente := ROUND(((aRecCreditoIrpef.im_credito /aGGAnno) * aNumeroGG),2);
                if aRecCreditoIrpef.im_credito_base != 0 then
                 aCreditoBase := ROUND(((aRecCreditoIrpef.im_credito_base /aGGAnno) * aNumeroGG),2);
                end if;

--pipe.send_message('aCreditoCorrente = '||aCreditoCorrente);
                -- calcolo aCreditoArretrato solo se non �tato DIP dalla data di inizio applicazione
                -- alla data di inizio competenza del compenso che sto pagando
                -- perch�n questo caso diamo per scontato che abbia gi�ecuperato il credito arretrato
                declare
                   conta INTEGER:=0;
                begin
                   select count(1)
                   into conta
                   from terzo, rapporto
                   where terzo.cd_anag = rapporto.cd_anag
                     and rapporto.cd_tipo_rapporto = 'DIP'
                     and rapporto.dt_fin_validita <= aRecCompenso.dt_da_competenza_coge
                     and rapporto.dt_fin_validita >= aRecCreditoIrpef.dt_inizio_applicazione
                     and terzo.cd_terzo = aRecCompenso.cd_terzo;

                   If conta = 0 then
                       -- calcolo il nro di giorni per cui aveva diritto al credito dalla data di inizio validit�
                       -- alla data di inizio applicazione ma non lo ha percepito (perch�on esisteva ancora la legge)
                       -- ngg di compensi da minicarriera pagati il cui trattamento ha tra i cori anche CREIRPEF
                       aNumeroGGArretrato := getNumeroGGCompensiCreditoArr(aRecCompenso.cd_terzo,
                                                                           aRecCompenso.Esercizio,
                                                                           aRecCreditoIrpef.dt_inizio_validita,
                                                                           aRecCreditoIrpef.dt_inizio_applicazione);
                       --a questo numero devo aggiungere eventuali giorni del compenso corrente che sono esclusi dal calcolo del credito corrente
                       --se pago aprile e maggio insieme, i giorni di maggio sono nel corrente, ma quelli di aprile devo prenderli nell'arretrato
--pipe.send_message('aNumeroGGArretrato = '||aNumeroGGArretrato);
                       aNumeroGGArretrato := aNumeroGGArretrato + aNumeroGGEsclusi;
--pipe.send_message('aNumeroGGArretrato con esclusi = '||aNumeroGGArretrato);
                       aGiorniArrMax:=IBMUTL001.getDaysBetween(aRecCreditoIrpef.dt_inizio_validita,(aRecCreditoIrpef.dt_inizio_applicazione-1));

                       if aNumeroGGArretrato > aGiorniArrMax then
                          aNumeroGGArretrato := aGiorniArrMax;
                       end if;
--pipe.send_message('aGiorniArrMax = '||aGiorniArrMax);
--pipe.send_message('aNumeroGGArretrato definitivo = '||aNumeroGGArretrato);
                       if aNumeroGGArretrato > 0 then
                           -- calcolo l'arretrato totale
                           aCreditoArretratoTot := ROUND(((aRecCreditoIrpef.im_credito/aGGAnno)* aNumeroGGArretrato),2);
                           -- per calcolare quanto di tale credito arretrato devo calcolare nel compenso attuale,
                           -- divido aCreditoArretratoTot per il numero di gg delle rate, sia pagate che non pagate, delle minicarriere
                           -- a partire dalla data di inizio applicazione del credito e fino alla fine validit�on scadenza nell�anno
                           -- e lo moltiplico per la competenza del compenso corrente.
--pipe.send_message('aCreditoArretratoTot = '||aCreditoArretratoTot);
                           --CNRCTB600.getNumeroGGRateNonAssCompenso(aRecCompenso.cd_terzo,
                             --                                      aRecCompenso.esercizio,
                               --                                    aNumeroGGRateNonAssComp);

                           CNRCTB600.getNumeroGGRateInPeriodo(aRecCompenso.cd_terzo,
                                                              aRecCompenso.esercizio,
                                                              aRecCreditoIrpef.dt_inizio_applicazione,
                                                              aRecCreditoIrpef.dt_fine_validita,
                                                              aNumeroGGRateTotCreditoCorr);
--pipe.send_message('aNumeroGGRateTotCreditoCorr = '||aNumeroGGRateTotCreditoCorr);
                           -- se non �ncora stata salvata la minicarriera aNumeroGGRateTotCreditoCorr = 0 e v�n errore
                           -- perch�erca di dividere per zero
                           If aNumeroGGRateTotCreditoCorr > 0 then
                                aCreditoArretrato := ROUND(((aCreditoArretratoTot/aNumeroGGRateTotCreditoCorr)* aNumeroGG),2);
                           else
                                aCreditoArretrato := 0;
                           end if;
                       else
                           aCreditoArretrato := 0;
                       end if;
                   else
                       aCreditoArretrato := 0;
                   end if;
                end;
/* modifica per riduzione cuneo fiscale */
                aCreditoArretrato := 0;
/* fine modifica per riduzione cuneo fiscale */

--pipe.send_message('aCreditoArretrato = '||aCreditoArretrato);
                aCredito := aCreditoCorrente + aCreditoArretrato;
--pipe.send_message('aCredito = '||aCredito);
--pipe.send_message('aCoefficiente = '||aCoefficiente);
                aCredito := ROUND((aCredito * aCoefficiente),2);
                aCredito := aCredito + aCreditoBase;
--pipe.send_message('aCredito NEW = '||aCredito);
           End If;  -- If aCreditoGiaCalcolato  < aCreditoTotAnno then
        END IF;
        aCredito := arrotondaCredito(aCredito,
                                     inDtDaCompCreditoCorrente,
                                     inDtACompCreditoCorrente,
                                     aRecCreditoIrpef);
--pipe.send_message('aCredito arrotondato = '||aCredito);
--pipe.send_message('ABS(aCreditoGiaCalcolato)  + aCredito  = '||(ABS(aCreditoGiaCalcolato) + aCredito) );
        IF ABS(aCreditoGiaCalcolato)  + aCredito  > aCreditoTotAnno then
             aCredito := aCreditoTotAnno - ABS(aCreditoGiaCalcolato);
             if (aCredito < 0 AND aOrigineCompenso!=CNRCTB545.isCompensoConguaglio) then
                aCredito := 0;
             end if;
--pipe.send_message('aCredito FINE = '||aCredito);
        END IF;
        if aRecCreditoIrpef.fl_detrazione = 'Y' THEN
            if  aRecCompenso.dt_a_competenza_coge >= dataInizioGestioneCuneoFiscale and glbFlNoDetrCuneoIrpef != 'Y' then
                aImDetrazioniRidCuneo := aCredito;
            end if;
            aCredito := 0;
        else
           If ((aRecCompenso.dt_a_competenza_coge < dataInizioGestioneCuneoFiscale and glbFlNoCreditoIrpef = 'Y') or
                (aRecCompenso.dt_a_competenza_coge >= dataInizioGestioneCuneoFiscale and glbFlNoCreditoCuneoIrpef = 'Y') )Then
               aCredito := 0;
           END IF;
        END IF;
  End If;   -- FINE  If glbFlNoCreditoIrpef != 'Y' Then
  aCredito:= aCredito * (-1);
  END;
END calcolaCreditoIrpef;

FUNCTION getNumeroGGCompensiCreditoArr
   ( aTerzo  COMPENSO.cd_terzo%TYPE,
     aEsercizio COMPENSO.esercizio%TYPE,
     aDtInizioValCredito  CREDITO_IRPEF.dt_inizio_validita%TYPE,
     aDtInizioAppCredito  CREDITO_IRPEF.dt_inizio_applicazione%TYPE
   ) RETURN NUMBER IS

    aGiorniArr INTEGER;
    dt_inizio_competenza  COMPENSO.DT_DA_COMPETENZA_COGE%TYPE;
    dt_fine_competenza    COMPENSO.DT_A_COMPETENZA_COGE%TYPE;

    tabella_date CNRCTB545.intervalloDateTab;
    tabella_date_ok CNRCTB545.intervalloDateTab;

    gen_cur GenericCurTyp;

BEGIN
  aGiorniArr :=0;
  tabella_date.DELETE;
  tabella_date_ok.DELETE;

  OPEN gen_cur FOR
       SELECT COMPENSO.dt_da_competenza_coge,
              COMPENSO.dt_a_competenza_coge
       FROM COMPENSO,MANDATO,MANDATO_RIGA
       WHERE MANDATO_RIGA.ESERCIZIO_OBBLIGAZIONE = COMPENSO.ESERCIZIO_OBBLIGAZIONE
         And   MANDATO_RIGA.PG_OBBLIGAZIONE = COMPENSO.PG_OBBLIGAZIONE
         And   MANDATO_RIGA.PG_OBBLIGAZIONE_SCADENZARIO = COMPENSO.PG_OBBLIGAZIONE_SCADENZARIO
         And   MANDATO_RIGA.CD_CDS_DOC_AMM = COMPENSO.CD_CDS
         And   MANDATO_RIGA.CD_UO_DOC_AMM = COMPENSO.CD_UNITA_ORGANIZZATIVA
         And   MANDATO_RIGA.ESERCIZIO_DOC_AMM = COMPENSO.ESERCIZIO
         AND   MANDATO_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE = COMPENSO.ESERCIZIO_ORI_OBBLIGAZIONE
         And   MANDATO_RIGA.PG_DOC_AMM = COMPENSO.PG_COMPENSO
         AND   MANDATO.CD_CDS = MANDATO_RIGA.CD_CDS
         And   MANDATO.ESERCIZIO = MANDATO_RIGA.ESERCIZIO
         And   MANDATO.PG_MANDATO = MANDATO_RIGA.PG_MANDATO
         AND   COMPENSO.STATO_COFI  =  'P'
         AND   MANDATO.STATO  !=  'A'
         AND   MANDATO.DT_TRASMISSIONE  IS NOT NULL
         AND   FL_COMPENSO_MINICARRIERA = 'Y'
         AND   MANDATO.ESERCIZIO = aEsercizio
         AND   COMPENSO.CD_TERZO = aTerzo
         AND   COMPENSO.DT_DA_COMPETENZA_COGE <= aDtInizioAppCredito
         AND   COMPENSO.DT_A_COMPETENZA_COGE >= aDtInizioValCredito
         AND   COMPENSO.CD_TRATTAMENTO IN (SELECT CD_TRATTAMENTO
                                           FROM V_TIPO_TRATTAMENTO_TIPO_CORI
                                           WHERE FL_CREDITO_IRPEF = 'Y'
                                             AND DT_INI_VAL_TRATT_CORI>= aDtInizioValCredito);

  LOOP

          FETCH gen_cur INTO
                dt_inizio_competenza,
                dt_fine_competenza;

          EXIT WHEN gen_cur%NOTFOUND;

              -- Composizione della matrice delle date per il calcolo dei giorni reali di competenza

              If dt_inizio_competenza < aDtInizioValCredito then
                 dt_inizio_competenza := aDtInizioValCredito;
              end if;
              If dt_fine_competenza > aDtInizioAppCredito then
                 dt_fine_competenza := aDtInizioAppCredito;
              end if;

              CNRCTB545.componiMatriceDate(tabella_date,
                                           dt_inizio_competenza,
                                           dt_fine_competenza,
                                           'N',
                                           'N');
  END LOOP;
  CLOSE gen_cur;

  IF tabella_date.COUNT > 0 THEN
      CNRCTB600.normalizzaMatriceDate(tabella_date,
                                      tabella_date_ok);

      aGiorniArr:=CNRCTB545.getGiorniMatriceDate(tabella_date_ok);

  END IF;

  RETURN aGiorniArr;

END getNumeroGGCompensiCreditoArr;

-- Ritorna il reddito di quanto gi�agato non da minicarriera (per il calcolo del reddito complessivo)
-- solo imponibile fiscale per irpef a scaglioni
-- quella conservata sul compenso �olo quella a scaglioni

FUNCTION getImponibPagNonMcarrieraAltro
   (
    aEsercizio NUMBER,
    aTerzo TERZO.cd_terzo%TYPE
   ) RETURN NUMBER IS
   aImponibilePagato NUMBER(15,2);

BEGIN
   aImponibilePagato:=0;

      SELECT nvl(sum(imponibile_fiscale_netto),0)
      into aImponibilePagato
       FROM COMPENSO,MANDATO,MANDATO_RIGA
       WHERE MANDATO_RIGA.ESERCIZIO_OBBLIGAZIONE = COMPENSO.ESERCIZIO_OBBLIGAZIONE
         And   MANDATO_RIGA.PG_OBBLIGAZIONE = COMPENSO.PG_OBBLIGAZIONE
         And   MANDATO_RIGA.PG_OBBLIGAZIONE_SCADENZARIO = COMPENSO.PG_OBBLIGAZIONE_SCADENZARIO
         And   MANDATO_RIGA.CD_CDS_DOC_AMM = COMPENSO.CD_CDS
         And   MANDATO_RIGA.CD_UO_DOC_AMM = COMPENSO.CD_UNITA_ORGANIZZATIVA
         And   MANDATO_RIGA.ESERCIZIO_DOC_AMM = COMPENSO.ESERCIZIO
         AND   MANDATO_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE = COMPENSO.ESERCIZIO_ORI_OBBLIGAZIONE
         And   MANDATO_RIGA.PG_DOC_AMM = COMPENSO.PG_COMPENSO
         AND   MANDATO.CD_CDS = MANDATO_RIGA.CD_CDS
         And   MANDATO.ESERCIZIO = MANDATO_RIGA.ESERCIZIO
         And   MANDATO.PG_MANDATO = MANDATO_RIGA.PG_MANDATO
         AND   COMPENSO.STATO_COFI  =  'P'
         AND   MANDATO.STATO  !=  'A'
         AND   MANDATO.DT_TRASMISSIONE  IS NOT NULL
         AND   FL_COMPENSO_MINICARRIERA = 'N'
         AND   MANDATO.ESERCIZIO = aEsercizio
         AND   COMPENSO.CD_TERZO = aTerzo;

      -- per prendere anche l'imposta dovrei andare sui dettagli
      -- ma per il momento si �eciso di non considerarla
      /*
      SELECT nvl(sum(CONTRIBUTO_RITENUTA.imponibile_fiscale_netto),0)
      into aImponibilePagato
       FROM COMPENSO, CONTRIBUTO_RITENUTA, TIPO_CONTRIBUTO_RITENUTA, MANDATO, MANDATO_RIGA
       WHERE   COMPENSO.CD_CDS = CONTRIBUTO_RITENUTA.CD_CDS
         And   COMPENSO.CD_UNITA_ORGANIZZATIVA = CONTRIBUTO_RITENUTA.CD_UNITA_ORGANIZZATIVA
         And   COMPENSO.ESERCIZIO = CONTRIBUTO_RITENUTA.ESERCIZIO
         And   COMPENSO.PG_COMPENSO = CONTRIBUTO_RITENUTA.PG_COMPENSO
         And   CONTRIBUTO_RITENUTA.CD_CONTRIBUTO_RITENUTA = TIPO_CONTRIBUTO_RITENUTA.CD_CONTRIBUTO_RITENUTA
         And   CONTRIBUTO_RITENUTA.DT_INI_VALIDITA = TIPO_CONTRIBUTO_RITENUTA.DT_INI_VALIDITA
         And   MANDATO_RIGA.ESERCIZIO_OBBLIGAZIONE = COMPENSO.ESERCIZIO_OBBLIGAZIONE
         And   MANDATO_RIGA.PG_OBBLIGAZIONE = COMPENSO.PG_OBBLIGAZIONE
         And   MANDATO_RIGA.PG_OBBLIGAZIONE_SCADENZARIO = COMPENSO.PG_OBBLIGAZIONE_SCADENZARIO
         And   MANDATO_RIGA.CD_CDS_DOC_AMM = COMPENSO.CD_CDS
         And   MANDATO_RIGA.CD_UO_DOC_AMM = COMPENSO.CD_UNITA_ORGANIZZATIVA
         And   MANDATO_RIGA.ESERCIZIO_DOC_AMM = COMPENSO.ESERCIZIO
         AND   MANDATO_RIGA.ESERCIZIO_ORI_OBBLIGAZIONE = COMPENSO.ESERCIZIO_ORI_OBBLIGAZIONE
         And   MANDATO_RIGA.PG_DOC_AMM = COMPENSO.PG_COMPENSO
         AND   MANDATO.CD_CDS = MANDATO_RIGA.CD_CDS
         And   MANDATO.ESERCIZIO = MANDATO_RIGA.ESERCIZIO
         And   MANDATO.PG_MANDATO = MANDATO_RIGA.PG_MANDATO
         AND   COMPENSO.STATO_COFI  =  'P'
         AND   MANDATO.STATO  !=  'A'
         AND   MANDATO.DT_TRASMISSIONE  IS NOT NULL
         AND   COMPENSO.FL_COMPENSO_MINICARRIERA = 'N'
         AND
         ---
         aCdClassifCori = isCoriFiscale AND
       aPgClassifMontanti = 1 AND
       aFlScriviMontanti = 'Y')
         ---
         AND   MANDATO.ESERCIZIO = aEsercizio
         AND   COMPENSO.CD_TERZO = aTerzo;
       */

   RETURN aImponibilePagato;

END getImponibPagNonMcarrieraAltro;

-- Ritorna il Credito gi�alcolato anche per compensi non ancora pagati

FUNCTION getCreditoGiaCalcolato
   (
    aCdCdsCompenso COMPENSO.cd_cds%TYPE,
    aCdUoCompenso COMPENSO.cd_unita_organizzativa%TYPE,
    aEsercizioCompenso COMPENSO.esercizio%TYPE,
    aPgCompenso COMPENSO.pg_compenso%TYPE,
    aTerzo TERZO.cd_terzo%TYPE,
    cdCori TIPO_CONTRIBUTO_RITENUTA.CD_CONTRIBUTO_RITENUTA%TYPE
   ) RETURN NUMBER IS
   aCredito NUMBER(15,2);

BEGIN
   aCredito:=0;

      select nvl(sum(cr.ammontare),0)
      into aCredito
      from compenso c, contributo_ritenuta cr, tipo_contributo_ritenuta tcr
      where c.cd_cds = cr.cd_cds
        and c.cd_unita_organizzativa = cr.cd_unita_organizzativa
        and c.esercizio = cr.esercizio
        and c.pg_compenso = cr.pg_compenso
        and cr.cd_contributo_ritenuta = tcr.cd_contributo_ritenuta
        and cr.dt_ini_validita = tcr.dt_ini_validita
        and c.esercizio = aEsercizioCompenso
        and c.stato_cofi != 'A'
        and c.cd_terzo = aTerzo
        and tcr.fl_credito_irpef = 'Y'
        and tcr.cd_contributo_ritenuta = cdCori
        and (c.cd_cds, c.cd_unita_organizzativa, c.esercizio, c.pg_compenso) not in
             (SELECT compenso.cd_cds, compenso.cd_unita_organizzativa, compenso.esercizio, compenso.pg_compenso
              FROM COMPENSO
              WHERE  compenso.cd_cds = aCdCdsCompenso AND
                     compenso.cd_unita_organizzativa = aCdUoCompenso AND
                     compenso.esercizio = aEsercizioCompenso AND
                     compenso.pg_compenso = aPgCompenso);

   RETURN aCredito;

END getCreditoGiaCalcolato;

FUNCTION arrotondaCredito
   (
    aCredito  CREDITO_IRPEF.im_credito%TYPE,
    DtDaComp COMPENSO.DT_DA_COMPETENZA_COGE%TYPE,
    DtAComp  COMPENSO.DT_A_COMPETENZA_COGE%TYPE,
    aRecCreditoIrpef  CREDITO_IRPEF%ROWTYPE
   ) RETURN NUMBER IS

   importoCreditoMensile number;
   aDifferenzaMesi integer;
   CreditoArr CREDITO_IRPEF.im_credito%TYPE;
BEGIN
   CreditoArr := aCredito;
   aDifferenzaMesi := MONTHS_BETWEEN(aRecCreditoIrpef.dt_fine_validita, aRecCreditoIrpef.dt_inizio_validita);
   importoCreditoMensile := aRecCreditoIrpef.im_credito / aDifferenzaMesi;
   IF ((IBMUTL001.getDaysBetween(DtDaComp,DtAComp) between 30 and 31)
        and
       (ABS(aCredito - importoCreditoMensile) < 2)
      ) THEN
        CreditoArr := importoCreditoMensile;
   ELSIF ((IBMUTL001.getDaysBetween(DtDaComp,DtAComp) between 60 and 62)
        and
       (ABS(aCredito - importoCreditoMensile * 2) < 4)
      ) THEN
        CreditoArr := importoCreditoMensile * 2;
   ELSIF ((IBMUTL001.getDaysBetween(DtDaComp,DtAComp) between 90 and 92)
        and
       (ABS(aCredito - importoCreditoMensile * 3) < 5)
      ) THEN
        CreditoArr := importoCreditoMensile * 3;
   ELSIF ((IBMUTL001.getDaysBetween(DtDaComp,DtAComp) between 120 and 123)
        and
       (ABS(aCredito - importoCreditoMensile * 4) < 7)
      ) THEN
        CreditoArr := importoCreditoMensile * 4;
   ELSIF ((IBMUTL001.getDaysBetween(DtDaComp,DtAComp) between 150 and 154)
        and
       (ABS(aCredito - importoCreditoMensile * 5) < 9)
      ) THEN
        CreditoArr := importoCreditoMensile * 5;
   ELSIF ((IBMUTL001.getDaysBetween(DtDaComp,DtAComp) between 180 and 185)
        and
       (ABS(aCredito - importoCreditoMensile * 6) < 10)
      ) THEN
        CreditoArr := importoCreditoMensile * 6;
   ELSIF ((IBMUTL001.getDaysBetween(DtDaComp,DtAComp) between 210 and 215)
        and
       (ABS(aCredito - importoCreditoMensile * 7) < 12)
      ) THEN
        CreditoArr := importoCreditoMensile * 7;
   ELSIF ((IBMUTL001.getDaysBetween(DtDaComp,DtAComp) between 240 and 246)
        and
       (ABS(aCredito - importoCreditoMensile * 8) < 14)
      ) THEN
        CreditoArr := importoCreditoMensile * 8;

   END IF;
    RETURN CreditoArr;
END arrotondaCredito;
FUNCTION getRegConScaglioneSpe
      (
       acdRegione regione.cd_regione%type,
       aDataOdierna DATE
      ) RETURN scaglione%Rowtype Is

   aScaglioneADDREGSPE scaglione%ROWTYPE;

BEGIN
    aScaglioneADDREGSPE:=NULL;

    Select *
    Into aScaglioneADDREGSPE
    From   scaglione
    Where  cd_regione = acdRegione
      and TI_ENTE_PERCIPIENTE ='P'
      and PG_COMUNE = 0
      and CD_PROVINCIA ='*'
      and CD_CONTRIBUTO_RITENUTA='ADDREGSPE'
      and ti_anagrafico = '*'
      And Trunc(dt_inizio_validita) <= Trunc(aDataOdierna)
      And Trunc(dt_fine_validita) >= Trunc(aDataOdierna);

   RETURN aScaglioneADDREGSPE;

EXCEPTION

   WHEN No_Data_Found THEN
        aScaglioneADDREGSPE.cd_regione:=acdRegione;
        aScaglioneADDREGSPE.dt_inizio_validita:=aDataOdierna;
        aScaglioneADDREGSPE.dt_fine_validita:=aDataOdierna;
        aScaglioneADDREGSPE.im_superiore:=0;
        aScaglioneADDREGSPE.aliquota:=0;
        aScaglioneADDREGSPE.PG_COMUNE:=0;
        aScaglioneADDREGSPE.CD_PROVINCIA:='*';
        aScaglioneADDREGSPE.ti_anagrafico:='*';
        RETURN aScaglioneADDREGSPE;
   WHEN Too_Many_Rows THEN
        aScaglioneADDREGSPE.cd_regione:=acdRegione;
        aScaglioneADDREGSPE.dt_inizio_validita:=aDataOdierna;
        aScaglioneADDREGSPE.dt_fine_validita:=aDataOdierna;
        aScaglioneADDREGSPE.im_superiore:=0;
        aScaglioneADDREGSPE.aliquota:=0;
        aScaglioneADDREGSPE.PG_COMUNE:=0;
        aScaglioneADDREGSPE.CD_PROVINCIA:='*';
        aScaglioneADDREGSPE.ti_anagrafico:='*';
        RETURN aScaglioneADDREGSPE;
END getRegConScaglioneSpe;

END;
