--------------------------------------------------------
--  DDL for Package Body CNRCTB250
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB250" AS

-- =================================================================================================
-- Guscio per gestione liquidazione IVA di massa in batch
-- =================================================================================================
PROCEDURE job_liquidazione_massa
   (
    job NUMBER, pg_exec NUMBER, next_date DATE,
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inTipoReport VARCHAR2,
    inRistampa VARCHAR2,
    repID INTEGER,
    logid INTEGER,
    msgError VARCHAR2,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    inTipoImpegno VARCHAR2 Default Null
   ) IS
   aStringa VARCHAR2(50);
   aDescJob VARCHAR2(100);
   i BINARY_INTEGER;

BEGIN
   -- Lancio start esecuzione log

   aStringa:=msgError;

   IF inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA THEN
     aDescJob := 'Liquidazione iva centro per tutte le UO';
   Else
     aDescJob := 'Liquidazione provvisoria iva centro per tutte le UO';
   End If;

   IBMUTL210.logStartExecutionUpd(pg_exec, LOG_TIPO_LIQIVAMAS, job, 'Richiesta utente:' || id_utente,
                                  aDescJob||'. Start:' || TO_CHAR(sysdate,'YYYY/MM/DD HH-MI-SS'));

   BEGIN

     stampeIvaInterna(inCdCdsOrigine, inCdUoOrigine, inEsercizio, inCdTipoSezionale, inDataInizio,
                      inDataFine, inTipoStampa, inTipoRegistro, inTipoReport, inRistampa,
                      repID, logid, aStringa, id_utente, inGruppoReport,inTipoImpegno);
     COMMIT;

     -- Messaggio di operazione completata ad utente

     IBMUTL205.LOGINF(aDescJob,
                      aDescJob||' '||TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS'),
                      'Operazione completata con successo',id_utente);

   EXCEPTION

      when others then
           rollback;
      -- Messaggio di attenzione ad utente

      IBMUTL205.LOGWAR(aDescJob||' '|| errori_tab.COUNT,
                       aDescJob||' '|| TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS') ||
                       ' (pg_exec=' || pg_exec || ')',DBMS_UTILITY.FORMAT_ERROR_STACK,id_utente);

      -- Scrittura degli eventuali altri errori

      IF errori_tab.COUNT > 0 THEN

         FOR i IN errori_tab.FIRST .. errori_tab.LAST

         LOOP

            IBMUTL200.LOGWAR(pg_exec,
                             aDescJob||' - Dettaglio errori',
                             'Errore : ' || errori_tab(i).tStringaErr,
                             'Codice UO = ' || errori_tab(i).tStringaUo || ' ' ||
                             'Tipo Liquidazione ' || errori_tab(i).tStringaTipo);

         END LOOP;

      END IF;

   END;

END job_liquidazione_massa;


-- =================================================================================================
-- Main stampa generica report IVA
-- =================================================================================================
PROCEDURE StampeIVA
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inTipoReport VARCHAR2,
    inRistampa VARCHAR2,
    repID INTEGER,
    logid INTEGER,
    msgError IN OUT VARCHAR2,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    inTipoImpegno VARCHAR2 Default Null
   ) IS
   aProcedure VARCHAR2(2000);
   aDescJob VARCHAR2(2000);
   aDtInizio VARCHAR2(50);
   aDtFine VARCHAR2(50);
   aStringa VARCHAR2(50);


BEGIN
   aStringa:=msgError;
   -------------------------------------------------------------------------------------------------
   -- La liquidazione IVA di massa attiva la gestione batch

   IF inTipoStampa IN (TI_ELAB_LIQUIDAZIONE_MASSA, TI_ELAB_LIQUIDAZIONE_MASSA_PRV) THEN

      aDtInizio:='TO_DATE(' || '''' || TO_CHAR(inDataInizio,'DDMMYYYY') || '''' || ',''DDMMYYYY'')';
      aDtFine:='TO_DATE(' || '''' || TO_CHAR(inDataFine,'DDMMYYYY') || '''' || ',''DDMMYYYY'')';

      aProcedure:='CNRCTB250.job_liquidazione_massa(job, pg_exec, next_date, ' ||
                  '''' || inCdCdsOrigine || ''',' ||
                  '''' || inCdUoOrigine || ''',' ||
                  inEsercizio || ', ''' || inCdTipoSezionale || ''',' ||
                  aDtInizio || ',' || aDtFine || ',''' || inTipoStampa || ''',' ||
                  '''' || inTipoRegistro  || ''',' ||
                  '''' || inTipoReport  || ''',' ||
                  '''' || inRistampa  || ''',' ||
                  repID || ',' || logid || ',''' || aStringa || ''',' ||
                  '''' || id_utente  || ''',' ||
                  '''' || inGruppoReport  || ''',' ||
                  '''' || inTipoImpegno  || ''');';

     IF inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA THEN
       aDescJob := 'Liquidazione iva centro per tutte le UO';
     Else
       aDescJob := 'Liquidazione provvisoria iva centro per tutte le UO';
     End If;

     IBMUTL210.CREABATCHDINAMICO(aDescJob, aProcedure, id_utente);

     IBMUTL001.deferred_commit;

     IBMERR001.RAISE_ERR_GENERICO
         ('Operazione sottomessa per esecuzione. Al completamento l''utente riceverא un messaggio di notifica ' ||
          'dello stato dell''operazione');

   Else
      stampeIvaInterna(inCdCdsOrigine, inCdUoOrigine, inEsercizio, inCdTipoSezionale, inDataInizio,
                       inDataFine, inTipoStampa, inTipoRegistro, inTipoReport, inRistampa,
                       repID, logid, msgError, id_utente, inGruppoReport, inTipoImpegno);
   END IF;

END StampeIVA;

-- =================================================================================================
-- Main stampa generica report IVA
-- =================================================================================================
PROCEDURE StampeIVAInterna
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inTipoReport VARCHAR2,
    inRistampa VARCHAR2,
    repID INTEGER,
    logid INTEGER,
    msgError IN OUT VARCHAR2,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    inTipoImpegno VARCHAR2 Default Null
   ) IS
   isLiquidIvaInterfaccia INTEGER;
   aTipoReportStato VARCHAR2(50);

   aEsercizioReale NUMBER(4);
   UOENTE unita_organizzativa%rowtype:= CNRCTB020.GETUOENTE(inEsercizio);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione del nome report per la selezione e scrittura in REPORT_STATO

   IF    inTipoStampa = TI_ELAB_REGISTRO_IVA THEN
         IF inTipoRegistro = TI_TIPO_REGISTRO_ACQ THEN
            aTipoReportStato:=CNRCTB255.TI_REGISTRO_ACQUISTI;
         ELSE
            aTipoReportStato:=CNRCTB255.TI_REGISTRO_VENDITE;
         END IF;
   ELSIF inTipoStampa = TI_ELAB_RIEPILOGATIVO_UO THEN
         IF inTipoRegistro = TI_TIPO_REGISTRO_ACQ THEN
            aTipoReportStato:=CNRCTB255.TI_RIEPILOGATIVO_ACQUISTI;
         ELSE
            aTipoReportStato:=CNRCTB255.TI_RIEPILOGATIVO_VENDITE;
         END IF;
   ELSIF inTipoStampa = TI_ELAB_RIEPILOGATIVO_CENTRO THEN
         aTipoReportStato:=CNRCTB255.TI_RIEPILOGATIVO_CENTRO;
   ELSIF inTipoStampa = TI_ELAB_IVA_DIFFERITA THEN
         aTipoReportStato:=CNRCTB255.TI_ESIGIBILITA_DIFFERITA;
   ELSIF (inTipoStampa = TI_ELAB_LIQUIDAZIONE OR
          inTipoStampa = TI_ELAB_LIQUIDAZIONE_DEF OR
          inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA OR
          inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA_PRV) THEN
         aTipoReportStato:=CNRCTB255.TI_LIQUIDAZIONE;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione dell'identificativo dell'esercizio per gestire la liquidazione nell'esercizio
   -- successivo rispetto a quanto indicato da periodo di riferimento. Solo le funzioni di liquidazione
   -- del mese di dicembre sono eseguite con esercizio successivo

   aEsercizioReale:=inEsercizio;
   IF aTipoReportStato=CNRCTB255.TI_LIQUIDAZIONE THEN
      IF inEsercizio != TO_NUMBER(TO_CHAR(inDataFine,'YYYY')) THEN
         aEsercizioReale:=TO_NUMBER(TO_CHAR(inDataFine,'YYYY'));
      END IF;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Verifica l'esecuzione della Liquidazione IVA per interfaccia
   -- 1) E' richiesta la stampa di una liquidazione
   -- 2) Il cds richiedente ט presente nella tabella LIQUID_IVA_INTERF_CDS
   -- L'esercizio di riferimento ט sempre quello reale, solo la liquidazione ט scritta in quello successivo

   isLiquidIvaInterfaccia:=0;

   IF (inTipoStampa = TI_ELAB_LIQUIDAZIONE OR
       (inTipoStampa = TI_ELAB_LIQUIDAZIONE_DEF AND
        inCdCdsOrigine != UOENTE.cd_unita_padre)) THEN

      SELECT COUNT(*) INTO isLiquidIvaInterfaccia
      FROM   DUAL
      WHERE  EXISTS
             (SELECT 1
              FROM   LIQUID_IVA_INTERF_CDS
              WHERE  cd_cds = inCdCdsOrigine AND
                     esercizio = aEsercizioReale);

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Costruzione della stampa predefinita

   IF isLiquidIvaInterfaccia = 1 THEN

      CNRCTB240.elaboraLiquidInterf(inCdCdsOrigine,
                                    inCdUoOrigine,
                                    inEsercizio,
                                    inCdTipoSezionale,
                                    inDataInizio,
                                    inDataFine,
                                    inTipoStampa,
                                    inTipoRegistro,
                                    inTipoReport,
                                    inRistampa,
                                    repID,
                                    msgError,
                                    id_utente,
                                    inGruppoReport,
                                    aEsercizioReale,
                                    aTipoReportStato);
   ELSE

      creaReportPredefinito(inCdCdsOrigine,
                            inCdUoOrigine,
                            inEsercizio,
                            inCdTipoSezionale,
                            inDataInizio,
                            inDataFine,
                            inTipoStampa,
                            inTipoRegistro,
                            inTipoReport,
                            inRistampa,
                            repID,
                            msgError,
                            id_utente,
                            inGruppoReport,
                            aEsercizioReale,
                            aTipoReportStato,
                            inTipoImpegno);

   END IF;

END StampeIVAInterna;

-- =================================================================================================
-- Elaborazione dei diversi report relativi all'IVA
-- =================================================================================================
PROCEDURE creaReportPredefinito
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inTipoReport VARCHAR2,
    inRistampa VARCHAR2,
    repID INTEGER,
    msg_out IN OUT VARCHAR2,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    aEsercizioReale NUMBER,
    aTipoReportStato VARCHAR2,
    inTipoImpegno VARCHAR2 Default Null
   ) IS
   isElaborazioneVuota CHAR(1);
   UOENTE unita_organizzativa%rowtype:= CNRCTB020.GETUOENTE(inEsercizio);
   aRecUnitaOrganizzativa UNITA_ORGANIZZATIVA%ROWTYPE;
   repID_loop INTEGER;
   gen_cv GenericCurTyp;
BEGIN
 IF   ((inTipoStampa = TI_ELAB_REGISTRO_IVA or aTipoReportStato = CNRCTB255.TI_RIEPILOGATIVO_ACQUISTI or aTipoReportStato = CNRCTB255.TI_RIEPILOGATIVO_VENDITE)
     and inRistampa = 'N' and inCdUoOrigine=UOENTE.cd_unita_organizzativa ) then

     OPEN gen_cv FOR

                    SELECT DISTINCT A.cd_unita_organizzativa,
                           A.cd_unita_padre
                    FROM   UNITA_ORGANIZZATIVA A,SEZIONALE B
                    WHERE  A.livello = 2 AND
                           A.cd_tipo_unita != CNRCTB001.TIPO_ENTE AND
                           A.esercizio_inizio <= inEsercizio AND
                           A.esercizio_fine >= inEsercizio AND
                           B.cd_cds = A.cd_unita_padre AND
                           B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                           B.esercizio = aEsercizioReale AND
                           B.ti_fattura = 'T' AND
                           (inTipoStampa != TI_ELAB_REGISTRO_IVA OR B.cd_tipo_sezionale = inCdTipoSezionale) and
                           NOT EXISTS
                                (SELECT 1
                         FROM   TIPO_SEZIONALE C, REPORT_STATO D
                         WHERE
                                C.cd_tipo_sezionale = inCdTipoSezionale AND
                                d.cd_tipo_sezionale = c.cd_tipo_sezionale and
                                D.cd_cds = B.cd_cds AND
                                D.cd_unita_organizzativa = B.cd_unita_organizzativa  AND
                                D.tipo_report = aTipoReportStato AND
                                D.dt_inizio = inDataInizio AND
                                D.dt_fine = inDataFine AND
                                D.stato IN ('B', 'C'))
                    ORDER BY A.cd_unita_organizzativa;

               LOOP

                  FETCH gen_cv INTO
                        aRecUnitaOrganizzativa.cd_unita_organizzativa,
                        aRecUnitaOrganizzativa.cd_unita_padre;

                  EXIT WHEN gen_cv%NOTFOUND;


   -------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri di base alla procedura. Funzione comune a tutti i report

   valorizzaParametri(aRecUnitaOrganizzativa.cd_unita_padre,
                      aRecUnitaOrganizzativa.cd_unita_organizzativa,
                      aEsercizioReale,
                      inCdTipoSezionale,
                      inDataInizio,
                      inDataFine,
                      inTipoStampa,
                      inTipoRegistro,
                      inGruppoReport);

   -------------------------------------------------------------------------------------------------
   -- Settaggio variabili

   gDataOdierna:=SYSDATE;
   isElaborazioneVuota:='N';

   -------------------------------------------------------------------------------------------------
   -- Attivazione controlli
   -- Le funzioni di controllo operano sempre con quanto passato dal client nel paramentro aCdTipoSezionale

     chkAmmettiElaborazione(aRecUnitaOrganizzativa.cd_unita_padre,
                            aRecUnitaOrganizzativa.cd_unita_organizzativa,
                            inEsercizio,
                            inCdTipoSezionale,
                            inDataInizio,
                            inDataFine,
                            inTipoStampa,
                            inTipoRegistro,
                            inTipoReport,
                            inGruppoReport,
                            aEsercizioReale,
                            aTipoReportStato);

   -- Controlli relativi alla gestione per report vuoto. La gestione si applica solo a stampe registri e
   -- riepilogativi definitivi


     isElaborazioneVuota:=chkRegistraReportVuoto(aRecUnitaOrganizzativa.cd_unita_padre,
                                                 aRecUnitaOrganizzativa.cd_unita_organizzativa,
                                                 inEsercizio,
                                                 inCdTipoSezionale,
                                                 inDataInizio,
                                                 inDataFine,
                                                 inTipoStampa,
                                                 inTipoRegistro,
                                                 inTipoReport,
                                                 inRistampa,
                                                 id_utente,
                                                 inGruppoReport,
                                                 aTipoReportStato);


    If (inTipoStampa = TI_ELAB_REGISTRO_IVA AND  inTipoRegistro = 'A' And inTipoReport = 'D' And inRistampa = 'N') THEN

         -- Assegnazione della data di esigibilita IVA per fatture passive non divenute esigibili nell'arco di 1 Anno
         -- in sede di stampa registri IVA
         CNRCTB260.insDataDiffFatturePassAuto(aRecUnitaOrganizzativa.cd_unita_padre,
                                aRecUnitaOrganizzativa.cd_unita_organizzativa,
                                inEsercizio,
                                inCdTipoSezionale,
                                inDataInizio,
                                inDataFine);
     End If;

   -------------------------------------------------------------------------------------------------
   -- Verifico se l'elaborazione ט vuota

  IF isElaborazioneVuota = 'N' THEN
        select IBMSEQ00_STAMPA.nextval into repID_loop from dual;
   -------------------------------------------------------------------------------------------------
   --  Ciclo per l'esecuzione delle query di valorizzazione dei campi per la stampa del report
   --  generico IVA. Differenziati per stampa
   	  IF    inTipoStampa = TI_ELAB_REGISTRO_IVA THEN
         stampaRegistri(aRecUnitaOrganizzativa.cd_unita_padre, aRecUnitaOrganizzativa.cd_unita_organizzativa, inEsercizio, inCdTipoSezionale,
                        inDataInizio, inDataFine, inTipoStampa, inTipoRegistro,
                        inTipoReport, inRistampa, repID_loop, msg_out, id_utente,
                        inGruppoReport, aTipoReportStato);
      ELSIF inTipoStampa = TI_ELAB_RIEPILOGATIVO_UO THEN
         stampaRiepilogativi(aRecUnitaOrganizzativa.cd_unita_padre, aRecUnitaOrganizzativa.cd_unita_organizzativa, inEsercizio, inCdTipoSezionale,
                             inDataInizio, inDataFine, inTipoStampa, inTipoRegistro,
                             inTipoReport, inRistampa, repID_loop, msg_out, id_utente,
                             inGruppoReport, aTipoReportStato);
      END IF;
  end if;

end loop;
close gen_cv;

else

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri di base alla procedura. Funzione comune a tutti i report

   valorizzaParametri(inCdCdsOrigine,
                      inCdUoOrigine,
                      aEsercizioReale,
                      inCdTipoSezionale,
                      inDataInizio,
                      inDataFine,
                      inTipoStampa,
                      inTipoRegistro,
                      inGruppoReport);

   -------------------------------------------------------------------------------------------------
   -- Settaggio variabili

   gDataOdierna:=SYSDATE;
   isElaborazioneVuota:='N';

   -------------------------------------------------------------------------------------------------
   -- Attivazione controlli
   -- Le funzioni di controllo operano sempre con quanto passato dal client nel paramentro aCdTipoSezionale

   -- Controllo ammissibilitא della elaborazione. La ristampa non ט controllata in quanto giא filtrata dal client

   IF inRistampa = 'N' THEN

      chkAmmettiElaborazione(inCdCdsOrigine,
                             inCdUoOrigine,
                             inEsercizio,
                             inCdTipoSezionale,
                             inDataInizio,
                             inDataFine,
                             inTipoStampa,
                             inTipoRegistro,
                             inTipoReport,
                             inGruppoReport,
                             aEsercizioReale,
                             aTipoReportStato);

   END IF;

   -- Controlli relativi alla gestione per report vuoto. La gestione si applica solo a stampe registri e
   -- riepilogativi definitivi

  IF (inTipoStampa = TI_ELAB_REGISTRO_IVA OR
      inTipoStampa = TI_ELAB_RIEPILOGATIVO_UO Or
      (inTipoStampa = TI_ELAB_IVA_DIFFERITA And inCdCdsOrigine != UOENTE.cd_unita_padre)) THEN

     isElaborazioneVuota:=chkRegistraReportVuoto(inCdCdsOrigine,
                                                 inCdUoOrigine,
                                                 inEsercizio,
                                                 inCdTipoSezionale,
                                                 inDataInizio,
                                                 inDataFine,
                                                 inTipoStampa,
                                                 inTipoRegistro,
                                                 inTipoReport,
                                                 inRistampa,
                                                 id_utente,
                                                 inGruppoReport,
                                                 aTipoReportStato);

   END IF;
     If (inTipoStampa = TI_ELAB_REGISTRO_IVA AND  inTipoRegistro = 'A' And inTipoReport = 'D' And inRistampa = 'N') THEN

         -- Assegnazione della data di esigibilita IVA per fatture passive non divenute esigibili nell'arco di 1 Anno
         -- in sede di stampa registri IVA
         CNRCTB260.insDataDiffFatturePassAuto(inCdCdsOrigine,
                                inCdUoOrigine,
                                inEsercizio,
                                inCdTipoSezionale,
                                inDataInizio,
                                inDataFine);
     End If;
   -------------------------------------------------------------------------------------------------
   -- Verifico se l'elaborazione ט vuota

   IF isElaborazioneVuota = 'Y' THEN
      msg_out:='La funzione di stampa ' || aTipoReportStato || ' non ha trovato alcun elemento da elaborare.';
      IF inTipoReport = 'D' THEN
         msg_out:=msg_out || 'Creata entrata in REPORT_STATO per elaborazione definitiva';
      END IF;
      RETURN;
   END IF;

   -------------------------------------------------------------------------------------------------
   --  Ciclo per l'esecuzione delle query di valorizzazione dei campi per la stampa del report
   --  generico IVA. Differenziati per stampa

   IF    inTipoStampa = TI_ELAB_REGISTRO_IVA THEN
         stampaRegistri(inCdCdsOrigine, inCdUoOrigine, inEsercizio, inCdTipoSezionale,
                        inDataInizio, inDataFine, inTipoStampa, inTipoRegistro,
                        inTipoReport, inRistampa, repID, msg_out, id_utente,
                        inGruppoReport, aTipoReportStato);
   ELSIF inTipoStampa = TI_ELAB_RIEPILOGATIVO_UO THEN
         stampaRiepilogativi(inCdCdsOrigine, inCdUoOrigine, inEsercizio, inCdTipoSezionale,
                             inDataInizio, inDataFine, inTipoStampa, inTipoRegistro,
                             inTipoReport, inRistampa, repID, msg_out, id_utente,
                             inGruppoReport, aTipoReportStato);
   ELSIF inTipoStampa = TI_ELAB_RIEPILOGATIVO_CENTRO THEN
         stampaRiepilogativiCentro(inCdCdsOrigine, inCdUoOrigine, inEsercizio, inCdTipoSezionale,
                                   inDataInizio, inDataFine, inTipoStampa, inTipoRegistro,
                                   inTipoReport, inRistampa, repID, msg_out, id_utente,
                                   inGruppoReport, aTipoReportStato);
   ELSIF inTipoStampa = TI_ELAB_IVA_DIFFERITA THEN
         stampaIvaDifferita (inCdCdsOrigine, inCdUoOrigine, inEsercizio, inCdTipoSezionale,
                             inDataInizio, inDataFine, inTipoStampa, inTipoRegistro,
                             inTipoReport, inRistampa, repID, id_utente,
                             inGruppoReport, aTipoReportStato);
   ELSIF (inTipoStampa = TI_ELAB_LIQUIDAZIONE OR
          inTipoStampa = TI_ELAB_LIQUIDAZIONE_DEF) THEN
         stampaLiquidazione(inCdCdsOrigine, inCdUoOrigine, inEsercizio, inCdTipoSezionale,
                            inDataInizio, inDataFine, inTipoStampa, inTipoRegistro,
                            inTipoReport, inRistampa, repID, id_utente,
                            inGruppoReport, aEsercizioReale, aTipoReportStato,inTipoImpegno);
   ELSIF (inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA OR
          inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA_PRV) THEN
         elaboraLiquidazioneMassa(inCdCdsOrigine, inCdUoOrigine, inEsercizio, inCdTipoSezionale,
                                  inDataInizio, inDataFine, inTipoStampa, inTipoRegistro,
                                  inTipoReport, inRistampa, repID, id_utente,
                                  inGruppoReport, aEsercizioReale, aTipoReportStato,inTipoImpegno);

   END IF;
end if;
END creaReportPredefinito;

-- =================================================================================================
-- Controllo ammissibilitא ad una elaborazione IVA
-- =================================================================================================
PROCEDURE chkAmmettiElaborazione
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inTipoReport VARCHAR2,
    inGruppoReport VARCHAR2,
    aEsercizioReale NUMBER,
    aTipoReportStato VARCHAR2
   ) IS
   flEsistePeriodo INTEGER;
   flEsistePeriodoPrecedente INTEGER;
   flEsistePeriodoPrima INTEGER;
   flEsistePeriodoDopo INTEGER;
   flUoHaSezionali CHAR(1);
   i INTEGER;
   j INTEGER;
   contaSezionali INTEGER;
   aMessaggio1 VARCHAR2(100);
   aMessaggio2 VARCHAR2(100);
   aMessaggioSezionale VARCHAR2(10);
   aTipoReportStatoAltro VARCHAR2(50);
   aCodiceSezionaleAltro VARCHAR2(10);
   aCdCdsAltro VARCHAR2(30);
   aCdUoAltro VARCHAR2(30);
   aGruppoReportAltro VARCHAR2(10);
   aStringa VARCHAR2(2000);
   aErrore CHAR(1);

   aRecUnitaOrganizzativa UNITA_ORGANIZZATIVA%ROWTYPE;
   gen_cv GenericCurTyp;
   UOENTE unita_organizzativa%rowtype:= CNRCTB020.GETUOENTE(inEsercizio);

BEGIN

   flEsistePeriodo:=0;
   flEsistePeriodoPrecedente:=0;
   flEsistePeriodoPrima:=0;
   flEsistePeriodoDopo:=0;
   IF inCdTipoSezionale = '*' THEN
      aMessaggioSezionale:='TUTTI';
   ELSE
      aMessaggioSezionale:=inCdTipoSezionale;
   END IF;
   aMessaggio1:='Elaborazione interrotta. ';
   IF inTipoStampa in (TI_ELAB_LIQUIDAZIONE_MASSA, TI_ELAB_LIQUIDAZIONE_MASSA_PRV) THEN
      aMessaggio2:='La funzione di stampa ' || inTipoStampa || ' per il sezionale ' || aMessaggioSezionale || ' della UO '||inCdUoOrigine;
   ELSE
      aMessaggio2:='La funzione di stampa ' || aTipoReportStato || ' per il sezionale ' || aMessaggioSezionale || ' della UO '||inCdUoOrigine;
   END IF;
   -------------------------------------------------------------------------------------------------
   -- Ritorna l'esistenza di record nella tabella REPORT_STATO per un dato aCdTipoSezionale nelle
   -- seguenti condizioni:
   -- 1) Esiste entrata per periodo uguale a quello in input
   -- 2) Esiste entrata per periodo precedente a quello in input (mese precedente)
   -- 3) Esiste entrata per qualsiasi periodo
   -- Si esclude l'elaborazione della liquidazione di massa

   IF inTipoStampa NOT IN (TI_ELAB_LIQUIDAZIONE_MASSA, TI_ELAB_LIQUIDAZIONE_MASSA_PRV) THEN

      CNRCTB255.getStatoReportStatoSiCdSez(inCdCdsOrigine,
                                           inCdUoOrigine,
                                           inCdTipoSezionale,
                                           inDataInizio,
                                           inDataFine,
                                           inGruppoReport,
                                           aTipoReportStato,
                                           flEsistePeriodo,
                                           flEsistePeriodoPrecedente,
                                           flEsistePeriodoPrima,
                                           flEsistePeriodoDopo);

   END IF;


   -------------------------------------------------------------------------------------------------
   -- Attivazione controlli generali

   -- Elaborazione provvisioria --------------------------------------------------------------------

   IF inTipoReport = 'P' THEN

      -- Non ט ammessa l'elaborazione provvisoria se esiste, nello stesso periodo, l'elaborazione definitiva

      IF (inTipoStampa = TI_ELAB_REGISTRO_IVA OR
          inTipoStampa = TI_ELAB_RIEPILOGATIVO_CENTRO OR
          inTipoStampa = TI_ELAB_IVA_DIFFERITA OR
          (inTipoStampa = TI_ELAB_RIEPILOGATIVO_UO AND
           inCdTipoSezionale = '*')) THEN

         IF flEsistePeriodo = 1 THEN
            IBMERR001.RAISE_ERR_GENERICO
               (aMessaggio1 || aMessaggio2 || ' risulta essere in stato definitivo per il periodo richiesto. '  ||
                'Eseguire la funzione di ristampa.');
         END IF;

      END IF;

      -- La liquidazione di massa non puע essere provvisoria

      IF inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA THEN
         IBMERR001.RAISE_ERR_GENERICO
               (aMessaggio1 || aMessaggio2 || ' non ט ammessa in forma provvisoria');
      END IF;

      RETURN;

   Else --Elaborazione definitiva
      -- La liquidazione di massa provvisoria non puע essere definitiva
      IF inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA_PRV THEN
         IBMERR001.RAISE_ERR_GENERICO
               (aMessaggio1 || aMessaggio2 || ' non ט ammessa in forma definitiva');
      END IF;
   END IF;

   -- Elaborazione definitiva ----------------------------------------------------------------------

   -- Se esiste una entrata per il periodo in input si solleva errore

   IF inTipoStampa NOT IN  (TI_ELAB_LIQUIDAZIONE_MASSA, TI_ELAB_LIQUIDAZIONE_MASSA_PRV) THEN

      IF flEsistePeriodo = 1 THEN
         IBMERR001.RAISE_ERR_GENERICO
            (aMessaggio1 || aMessaggio2 || ' risulta essere in stato definitivo per il periodo richiesto');
      END IF;

      -- Non esiste una entrata per il periodo precedente. Se vi sono comunque delle entrate sulla tabella
      -- REPORT_STATO si solleva errore.
      -- In questo caso, allo stato attuale, non si distingue se le entrate trovate sono future.
     if(inTipoStampa!=TI_ELAB_RIEPILOGATIVO_CENTRO) then
        IF (flEsistePeriodoPrecedente = 0 AND
            (flEsistePeriodoPrima = 1 OR
             flEsistePeriodoDopo = 1)) THEN
           IBMERR001.RAISE_ERR_GENERICO
            (aMessaggio1 || aMessaggio2 || ' non risulta presente in stato definitivo per il periodo precedente ' ||
             'mentre esistono altri periodi valorizzati');
        END IF;
    end if;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Attivazione controlli specifici per elaborazioni definitive

   -- Nel caso in cui sia richiesta una stampa registri definitiva: --------------------------------
   -- - Se esiste liquidazione definitiva nel periodo corrente o in periodi successivi sollevo errore.
   -- - Se non esiste liquidazione definitiva per il periodo precedente ed esistono stampe di altri registri
   --   definitivi in periodi diversi dal corrente sollevo errore

   IF inTipoStampa = TI_ELAB_REGISTRO_IVA THEN
      aGruppoReportAltro:=NULL;

      aCodiceSezionaleAltro:='*';
      aTipoReportStatoAltro:=CNRCTB255.TI_LIQUIDAZIONE;
      IF inTipoRegistro = TI_TIPO_REGISTRO_ACQ THEN
         IF gsezio_acq_tab(1).stringa3 = 'C' THEN
            aGruppoReportAltro:=TI_LIQ_IVA_COMMERC;
         ELSE
            IF    gsezio_acq_tab(1).stringa4 = 'Y' And gsezio_acq_tab(1).stringa6 != 'S' THEN
                  aGruppoReportAltro:=TI_LIQ_IVA_ISTSMSI;
            ELSIF gsezio_acq_tab(1).stringa5 = 'Y' And  gsezio_acq_tab(1).stringa6 = 'B' Then
                  aGruppoReportAltro:=TI_LIQ_IVA_ISTINTR;
                  --RP
            ELSIF (gsezio_acq_tab(1).stringa7 = 'Y' Or gsezio_acq_tab(1).stringa5 = 'Y' Or gsezio_acq_tab(1).stringa4 = 'Y') And gsezio_acq_tab(1).stringa6 = 'S'  THEN
                  aGruppoReportAltro:=TI_LIQ_IVA_ISTSNR;
            ELSIF (gsezio_acq_tab(1).stringa8 = 'Y')  THEN
                  aGruppoReportAltro:=TI_LIQ_IVA_ISTSPLIT;
            END IF;
         END IF;
      ELSE
         aGruppoReportAltro:=TI_LIQ_IVA_COMMERC;
      END IF;
      IF aGruppoReportAltro IS NOT NULL THEN

         CNRCTB255.getStatoReportStatoSiCdSez(inCdCdsOrigine,
                                              inCdUoOrigine,
                                              aCodiceSezionaleAltro,
                                              inDataInizio,
                                              inDataFine,
                                              aGruppoReportAltro,
                                              aTipoReportStatoAltro,
                                              flEsistePeriodo,
                                              flEsistePeriodoPrecedente,
                                              flEsistePeriodoPrima,
                                              flEsistePeriodoDopo);

         IF (flEsistePeriodo = 1 OR
             flEsistePeriodoDopo = 1) THEN
            IBMERR001.RAISE_ERR_GENERICO
               (aMessaggio1 || aMessaggio2 || ' non ט ammessa in quanto esistono liquidazioni definitive nel periodo ' ||
                'corrente o in quelli successivi');
         END IF;

         IF flEsistePeriodoPrecedente = 0 THEN
            Declare
              CONTA NUMBER := 0;
            Begin
              SELECT count(0) into conta
              FROM   REPORT_STATO
              WHERE  cd_cds = inCdCdsOrigine AND
                     cd_unita_organizzativa = inCdUoOrigine AND
                     cd_tipo_sezionale = aCodiceSezionaleAltro AND
                     tipo_report = aTipoReportStato
                     and rownum < 2;

              If conta > 0 Then --Non ט la prima stampa.....quindi controllo
                CNRCTB255.getStatoReportStatoNoCdSez(inCdCdsOrigine,
                                                     inCdUoOrigine,
                                                     inDataInizio,
                                                     inDataFine,
                                                     inGruppoReport,
                                                     aTipoReportStato,
                                                     flEsistePeriodo,
                                                     flEsistePeriodoPrecedente,
                                                     flEsistePeriodoPrima,
                                                     flEsistePeriodoDopo);

                IF (flEsistePeriodoPrecedente = 1 OR
                    flEsistePeriodoPrima = 1 OR
                    flEsistePeriodoDopo = 1) THEN
                   IBMERR001.RAISE_ERR_GENERICO
                      (aMessaggio1 || aMessaggio2 || ' non è ammessa in quanto non esiste liquidazione definitiva per ' ||
                       'il periodo precedente ed esistono stampe registri definitivi in periodi diversi dal corrente.');
                END IF;
              End if;
            End;
         END IF;

      END IF;

   END IF;

   -- Per le liquidazioni non ente e quella di massa si verifica che non sia giא stata elaborata ---
   -- la liquidazione al centro

   IF (inTipoStampa IN (TI_ELAB_LIQUIDAZIONE_MASSA, TI_ELAB_LIQUIDAZIONE_MASSA_PRV) OR
       (inTipoStampa = TI_ELAB_LIQUIDAZIONE_DEF AND
        inCdCdsOrigine <> UOENTE.cd_unita_padre)) THEN

      aCdCdsAltro:=UOENTE.cd_unita_padre;
      aCdUoAltro:=UOENTE.cd_unita_organizzativa;

      CNRCTB255.getStatoReportStatoNoGruppo(aCdCdsAltro,
                                            aCdUoAltro,
                                            inCdTipoSezionale,
                                            inDataInizio,
                                            inDataFine,
                                            aTipoReportStato,
                                            flEsistePeriodo,
                                            flEsistePeriodoPrecedente,
                                            flEsistePeriodoPrima,
                                            flEsistePeriodoDopo);

      -- Se esiste una entrata per il periodo in input si solleva errore

      IF flEsistePeriodo = 1 THEN
         IBMERR001.RAISE_ERR_GENERICO
            (aMessaggio1 || aMessaggio2 || ' non è ammessa in quanto esiste la liquidazione centro in stato ' ||
             'definitivo per il periodo richiesto');
      END IF;

      -- Non esiste una entrata per il periodo precedente. Se vi sono comunque delle entrate sulla tabella
      -- REPORT_STATO si solleva errore.
      -- In questo caso, allo stato attuale, non si distingue se le entrate trovate sono future.

      IF (flEsistePeriodoPrecedente = 0 AND
          (flEsistePeriodoPrima = 1 OR
           flEsistePeriodoDopo = 1)) THEN
         IBMERR001.RAISE_ERR_GENERICO
            (aMessaggio1 || aMessaggio2 || ' non ט ammessa in quanto la liquidazione centro, pur non risultando ' ||
             'presente in stato definitivo per il periodo precedente, ט definita  in altri periodi contabili');
      END IF;

   END IF;

   -- Nel caso in cui sia richiesta la stampa definitiva di un riepilogativo, dell'iva differita o di
   -- una liquidazione per un cd_cds diverso dall'ente, allora si deve controllare se tutti i registri
   -- per il mese corrente risultano stampati in modo definitivo
   -- Non si ammette l'elaborazione se non sono definiti sezionali per la U.O.

   IF (inTipoStampa = TI_ELAB_RIEPILOGATIVO_UO OR
       inTipoStampa = TI_ELAB_IVA_DIFFERITA OR
       (inTipoStampa = TI_ELAB_LIQUIDAZIONE_DEF AND
        inCdCdsOrigine <> UOENTE.cd_unita_padre)) THEN

      -- Ciclo su array sezionali di acquisto e vendita  -------------------------------------------

      FOR i IN 1 .. 2

      LOOP

         IF i = 1 THEN
            contaSezionali:=gsezio_acq_tab.COUNT;
            aTipoReportStatoAltro:=CNRCTB255.TI_REGISTRO_ACQUISTI;
         ELSE
            contaSezionali:=gsezio_ven_tab.COUNT;
            aTipoReportStatoAltro:=CNRCTB255.TI_REGISTRO_VENDITE;
         END IF;

         IF contaSezionali > 0 THEN
            Declare
               aSezionaliError varchar2(255) := null;
               contaError number := 0;
            Begin
               FOR j IN 1 .. contaSezionali

               LOOP

                  IF i = 1 THEN
                     aCodiceSezionaleAltro:=gsezio_acq_tab(j).stringa1;
                  ELSE
                     aCodiceSezionaleAltro:=gsezio_ven_tab(j).stringa1;
                  END IF;

                  CNRCTB255.getStatoReportStatoSiCdSez(inCdCdsOrigine,
                                                    inCdUoOrigine,
                                                    aCodiceSezionaleAltro,
                                                    inDataInizio,
                                                    inDataFine,
                                                    inGruppoReport,
                                                    aTipoReportStatoAltro,
                                                    flEsistePeriodo,
                                                    flEsistePeriodoPrecedente,
                                                    flEsistePeriodoPrima,
                                                    flEsistePeriodoDopo);

                  IF flEsistePeriodo = 0 THEN
                     contaError := contaError + 1;
                     If aSezionaliError is null Then
                        aSezionaliError := aCodiceSezionaleAltro;
                     Else
                        aSezionaliError := aSezionaliError||','||aCodiceSezionaleAltro;
                     End If;
                  END IF;

               END LOOP;

               If aSezionaliError is not null Then
                  IBMERR001.RAISE_ERR_GENERICO
                     (aMessaggio1 || aMessaggio2 || ' non è ammessa in quanto i seguenti sezionali ('||aSezionaliError||
                      ') non risultano in stato stampa registri definitiva');
               Elsif contaError > 0 Then
                  IBMERR001.RAISE_ERR_GENERICO
                     (aMessaggio1 || aMessaggio2 || ' non è ammessa in quanto tutti i sezionali '||
                     'non risultano in stato stampa registri definitiva');
               End If;
            End;
         ELSE

            flUoHaSezionali:=CNRCTB120.getUoHaSezionali(inCdCdsOrigine,
                                                        inCdUoOrigine,
                                                        aEsercizioReale);

            IF flUoHaSezionali = 'N' THEN

               IBMERR001.RAISE_ERR_GENERICO
                  (aMessaggio1 || aMessaggio2 || ' non è ammessa in quanto non è stato definito alcun sezionale ' ||
                   'per la U.O. ' || inCdUoOrigine);

            END IF;

         END IF;

      END LOOP;

   END IF;

   -- Nel caso in cui sia richiesta la stampa definitiva di una liquidazione per l'ente si verifica che
   -- siano state fatte le liquidazioni definitive per tutte le uo presenti nel sistema

   IF (inTipoStampa = TI_ELAB_LIQUIDAZIONE_DEF AND
       inCdCdsOrigine = UOENTE.cd_unita_padre) THEN

      BEGIN

         aStringa:=NULL;
         aErrore:='N';

         OPEN gen_cv FOR

              SELECT *
              FROM   UNITA_ORGANIZZATIVA A
              WHERE  A.livello = 2 AND
                     A.cd_tipo_unita != 'ENTE' AND
                     A.esercizio_inizio <= aEsercizioReale AND
                     A.esercizio_fine >= aEsercizioReale AND
                     EXISTS
                        (SELECT 1
                         FROM   SEZIONALE B
                         WHERE  B.cd_cds = A.cd_unita_padre AND
                                B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                                B.esercizio = aEsercizioReale) AND
                     NOT EXISTS
                        (SELECT 1
                         FROM   LIQUIDAZIONE_IVA L
                         WHERE  L.cd_cds = A.cd_unita_padre AND
                                L.esercizio = inEsercizio AND
                                L.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                                L.tipo_liquidazione = inGruppoReport AND
                                L.dt_inizio = inDataInizio AND
                                L.dt_fine = inDataFine AND
                                L.report_id = 0 AND
                                L.stato = 'D') AND
                     NOT EXISTS
                        (SELECT 1
                         FROM   LIQUID_IVA_INTERF_CDS C
                         WHERE  C.cd_cds = A.cd_unita_padre AND
                                C.esercizio = aEsercizioReale)
              ORDER BY A.cd_unita_organizzativa;

         LOOP

            FETCH gen_cv INTO
                  aRecUnitaOrganizzativa;

            EXIT WHEN gen_cv%NOTFOUND;

            aErrore:='Y';

            IF aStringa IS NULL THEN
               aStringa:=aRecUnitaOrganizzativa.cd_unita_organizzativa;
            ELSE
               IF LENGTH(aStringa) < 1980 THEN
                  aStringa:=aStringa || '/' || aRecUnitaOrganizzativa.cd_unita_organizzativa;
               END IF;
            END IF;

         END LOOP;

         CLOSE gen_cv;

         IF aErrore = 'Y' THEN
            IBMERR001.RAISE_ERR_GENERICO
               (aMessaggio1 || aMessaggio2 || ' non è ammessa in quanto non è stata fatta la liquidazione ' ||
                'definita per le seguenti UO ' || aStringa);
         END IF;

      END;

   END IF;

   -- Nel caso in cui sia richiesta la liquidazione di massa per l'ente si verifica che tutti i registri
   -- iva siano presenti in stato definitivo

   IF inTipoStampa IN (TI_ELAB_LIQUIDAZIONE_MASSA, TI_ELAB_LIQUIDAZIONE_MASSA_PRV) THEN

      BEGIN

         aStringa:=NULL;
         aErrore:='N';

         OPEN gen_cv FOR

              SELECT *
              FROM   UNITA_ORGANIZZATIVA A
              WHERE  A.livello = 2 AND
                     A.cd_tipo_unita != 'ENTE' AND
                     A.esercizio_inizio <= aEsercizioReale AND
                     A.esercizio_fine >= aEsercizioReale AND
                     NOT EXISTS
                        (SELECT 1
                         FROM   SEZIONALE B, TIPO_SEZIONALE C, REPORT_STATO D
                         WHERE  B.cd_cds = A.cd_unita_padre AND
                                B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                                B.esercizio = aEsercizioReale AND
                                B.ti_fattura = 'T' AND
                                C.cd_tipo_sezionale = B.cd_tipo_sezionale AND
                                (
                                     C.ti_istituz_commerc = 'C'
                                 OR
                                     (C.ti_istituz_commerc = 'I' AND
                                      C.fl_san_marino_senza_iva = 'Y' AND
                                      C.ti_bene_servizio = 'B')
                                 OR
                                     (C.ti_istituz_commerc = 'I' AND
                                      C.fl_intra_ue = 'Y' AND
                                      C.ti_bene_servizio = 'B')
                                 OR
                                     (C.ti_istituz_commerc = 'I' AND
                                     (C.fl_intra_ue = 'Y' Or c.fl_extra_ue ='Y' Or C.fl_san_marino_senza_iva = 'Y' ) And
                                      C.ti_bene_servizio = 'S')
                                 OR
                                     (C.ti_istituz_commerc = 'I' AND
                                      C.fl_split_payment = 'Y')
                                ) AND
                                D.cd_cds = B.cd_cds AND
                                D.cd_unita_organizzativa = B.cd_unita_organizzativa  AND
                                D.tipo_report = CNRCTB255.TI_REGISTRO_ACQUISTI AND
                                D.dt_inizio = inDataInizio AND
                                D.dt_fine = inDataFine AND
                                D.stato IN ('B', 'C')) AND
                     NOT EXISTS
                        (SELECT 1
                         FROM   LIQUID_IVA_INTERF_CDS C
                         WHERE  C.cd_cds = A.cd_unita_padre AND
                                C.esercizio = aEsercizioReale)
              ORDER BY A.cd_unita_organizzativa;

         LOOP

            FETCH gen_cv INTO
                  aRecUnitaOrganizzativa;

            EXIT WHEN gen_cv%NOTFOUND;

            aErrore:='Y';

            IF aStringa IS NULL THEN
               aStringa:=aRecUnitaOrganizzativa.cd_unita_organizzativa;
            ELSE
               IF LENGTH(aStringa) < 1980 THEN
                  aStringa:=aStringa || '/' || aRecUnitaOrganizzativa.cd_unita_organizzativa;
               END IF;
            END IF;

         END LOOP;

         CLOSE gen_cv;

         IF aErrore = 'Y' THEN
            IBMERR001.RAISE_ERR_GENERICO
               (aMessaggio1 || aMessaggio2 || ' non è ammessa in quanto non è stata fatta la stampa definitiva ' ||
                'dei registri per le seguenti UO ' || aStringa);
         END IF;

      END;

   END IF;

END chkAmmettiElaborazione;


-- =================================================================================================
-- Controlla i sezionali vuoti e, per essi, genera una entrata in report_stato
-- =================================================================================================
FUNCTION chkRegistraReportVuoto
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inTipoReport VARCHAR2,
    inRistampa VARCHAR2,
    idUtente VARCHAR2,
    inGruppoReport VARCHAR2,
    aTipoReportStato VARCHAR2
   ) RETURN CHAR IS
   i INTEGER;
   contaSezionali INTEGER;
   aCodiceSezionale VARCHAR2(10);
   isStatoSezionale INTEGER;
   isSezionaleVuoto CHAR(1);
   isSezionalePieno CHAR(1);
   aRecReportStato REPORT_STATO%ROWTYPE;

BEGIN

   isSezionaleVuoto:='N';
   isSezionalePieno:='N';

   BEGIN

      ----------------------------------------------------------------------------------------------
      -- Determino il numero di sezionali da elaborare

      IF inTipoRegistro = 'A' THEN
         contaSezionali:=gsezio_acq_tab.COUNT;
      ELSE
         contaSezionali:=gsezio_ven_tab.COUNT;
      END IF;

      IF contaSezionali > 0 THEN
         FOR j IN 1 .. contaSezionali

         LOOP

            IF inTipoRegistro = 'A' THEN
               aCodiceSezionale:=gsezio_acq_tab(j).stringa1;
               isStatoSezionale:=gsezio_acq_tab(j).intero;
            ELSE
               aCodiceSezionale:=gsezio_ven_tab(j).stringa1;
               isStatoSezionale:=gsezio_ven_tab(j).intero;
            END IF;

            -- Il report, per il sezionale in elaborazione, ט vuoto

            IF isStatoSezionale = 0 THEN
               isSezionaleVuoto:='Y';
            ELSE
               isSezionalePieno:='Y';
            END IF;

         END LOOP;

      END IF;

   END;

   IF (isSezionaleVuoto = 'Y' AND
       isSezionalePieno = 'N') THEN

       -- Inserimento dell'entrata in REPORT_STATO per registro vuoto solo se non ristampa

       IF (inTipoReport = 'D' AND
           inRistampa = 'N') THEN
          CNRCTB255.inserisciInReportStato(inCdCdsOrigine,
                                           inCdUoOrigine,
                                           inEsercizio,
                                           inCdTipoSezionale,
                                           inDataInizio,
                                           inDataFine,
                                           inTipoRegistro,
                                           inGruppoReport,
                                           aTipoReportStato,
                                           idUtente);
      END IF;

      RETURN 'Y';
   ELSE
      RETURN 'N';
   END IF;

END chkRegistraReportVuoto;

-- =================================================================================================
-- Stampa dei Registri IVA
-- =================================================================================================
procedure stampaRegistri
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inTipoReport VARCHAR2,
    inRistampa VARCHAR2,
    repID INTEGER,
    msg_out IN OUT VARCHAR2,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    aTipoReportStato VARCHAR2
   ) IS
   aTitoloReport1 VARCHAR2(200);
   aTitoloReport2 VARCHAR2(200);
   aTitoloReport3 VARCHAR2(200);
   lnumeroSezionali INTEGER;
   lcodiceSezionale VARCHAR2(10);
   ldescrizioneSezionale VARCHAR2(100);
   lTipoCommercIstituz CHAR(1);
   lgruppoStm CHAR(1);
   lsottoGruppoStm CHAR(1);
   ldescrizioneGruppoStm CHAR(100);
   lrigaStampa VARCHAR2(1000);
   lSequenza INTEGER;
   aEsercizioRif INTEGER;

BEGIN

   lSequenza:=0;
    -------------------------------------------------------------------------------------------------
   -- Costruzione del titolo report

   -- Intestazione dell'unita organizzativa

   aTitoloReport1:='U.0. ' || inCdUoOrigine || ' ' || gparametri_tab(7).stringa1;

   -- Descrizione del registro

   aTitoloReport2:='REGISTRO IVA ';

   IF inTipoRegistro = 'A' THEN
      aTitoloReport2:=aTitoloReport2 || 'ACQUISTI ';
      lnumeroSezionali:=gsezio_acq_tab.COUNT;
      lcodiceSezionale:=gsezio_acq_tab(1).stringa1;
      ldescrizioneSezionale:=gsezio_acq_tab(1).stringa2;
   ELSE
      aTitoloReport2:=aTitoloReport2 || 'VENDITE ';
      lnumeroSezionali:=gsezio_ven_tab.COUNT;
      lcodiceSezionale:=gsezio_ven_tab(1).stringa1;
      ldescrizioneSezionale:=gsezio_ven_tab(1).stringa2;
   END IF;

   IF inTipoReport = 'P' THEN
      aTitoloReport2:=aTitoloReport2 || 'PROVVISORIO ';
   END IF;

   aTitoloReport2:=aTitoloReport2 || 'DAL ' ||
                   TO_CHAR(inDataInizio, 'DD/MM/YYYY') || ' AL ' ||
                   TO_CHAR(inDataFine, 'DD/MM/YYYY') || ' ';
   aTitoloReport3:='SEZIONALE ' || inCdTipoSezionale;
   IF inTipoRegistro = 'A' THEN
      aTitoloReport3:=aTitoloReport3 || ' ' || gparametri_tab(4).stringa1;
   ELSE
      aTitoloReport3:=aTitoloReport3 || ' ' || gparametri_tab(3).stringa1;
   END IF;

   aEsercizioRif:=TO_NUMBER(TO_CHAR(inDataFine,'YYYY'));

   -----------------------------------------------------------------------------------------------
   --  Ciclo per l'esecuzione delle query di valorizzazione dei campi per la stampa

   FOR lPasso IN 1 .. 9

   LOOP

      lSequenza:=lSequenza + 20;

      IF    lPasso = 1 THEN
            lrigaStampa:='Memorizzazione titolo report';
            lgruppoStm:='-';
            lsottoGruppoStm:='-';
            ldescrizioneGruppoStm:='TITOLO REPORT';
      ELSIF lPasso = 2 THEN
            lrigaStampa:='GRUPPO A - Memorizzazione copertina del riepilogo sezionali in stampa';
            lgruppoStm:='A';
            lsottoGruppoStm:='A';
            ldescrizioneGruppoStm:='TITOLO GRUPPO A';
      ELSIF lPasso = 3 THEN
            lrigaStampa:='GRUPPO B - Stampa del registro IVA e dei riepiloghi per aliquota';
            lgruppoStm:='B';
            lsottoGruppoStm:='D';
            ldescrizioneGruppoStm:='CORPO GRUPPO B';
      ELSIF lPasso = 4 THEN
            lrigaStampa:='GRUPPO C - Eventuale riepilogo generale per aliquota';
            lgruppoStm:='C';
            lsottoGruppoStm:='C';
            ldescrizioneGruppoStm:='CORPO GRUPPO C';
      ELSIF lPasso = 5 THEN
            lrigaStampa:='GRUPPO D - Riporto dati dichiarazione di intento su registro Vendite definitivo';
            lgruppoStm:='D';
            lsottoGruppoStm:=NULL;
            ldescrizioneGruppoStm:='CORPO GRUPPO D';
      ELSIF lPasso = 6 THEN
            lrigaStampa:='GRUPPO E - Riporto dati revoca dichiarazione di intento su registro Vendite definitivo';
            lgruppoStm:='E';
            lsottoGruppoStm:=NULL;
            ldescrizioneGruppoStm:='CORPO GRUPPO E';
      ELSIF lPasso = 7 THEN
            lrigaStampa:='GRUPPO F - Stampa Annotazioni libere';
            lgruppoStm:='F';
            lsottoGruppoStm:=NULL;
            ldescrizioneGruppoStm:='CORPO GRUPPO F';
      ELSIF lPasso = 8 THEN
            lrigaStampa:='GRUPPO G - Inserimento fatture in REPORT_DETTAGLIO';
            lgruppoStm:='G';
            lsottoGruppoStm:=NULL;
            ldescrizioneGruppoStm:='CORPO GRUPPO G';
      ELSIF lPasso = 9 THEN
            lrigaStampa:='GRUPPO H - Inserimento riga in REPORT_STATO';
            lgruppoStm:='H';
            lsottoGruppoStm:=NULL;
            ldescrizioneGruppoStm:='CORPO GRUPPO H';
      END IF;

      -------------------------------------------------------------------------------------------
      -- Memorizzazione del titolo report

      IF    lPasso = 1 THEN

            CNRCTB260.insTitColReportGenerico(repID,
                                              lSequenza,
                                              aTitoloReport1,
                                              aTitoloReport2,
                                              aTitoloReport3,
                                              gparametri_tab(8).stringa1,
                                              gparametri_tab(8).stringa2,
                                              aEsercizioRif,
                                              lgruppoStm,
                                              lsottoGruppoStm,
                                              ldescrizioneGruppoStm,
                                              inTipoReport,
                                              inTipoRegistro);

      -------------------------------------------------------------------------------------------
      -- Memorizzazione copertina del riepilogo sezionali in stampa

      ELSIF lPasso = 2 THEN

            IF lnumeroSezionali > 1  THEN

               -- Valorizzazione del titolo della copertina

               aTitoloReport1:='FRONTESPIZIO DEL REGISTRO ';

               IF inTipoRegistro = 'A' THEN
                  aTitoloReport1:=aTitoloReport1 || 'ACQUISTI ';
               ELSE
                  aTitoloReport1:=aTitoloReport1 || 'VENDITE ';
               END IF;

               aTitoloReport1:=aTitoloReport1 || 'DAL ' ||
                               TO_CHAR(inDataInizio, 'DD/MM/YYYY') || ' AL ' ||
                               TO_CHAR(inDataFine, 'DD/MM/YYYY');
               aTitoloReport2:=' RIASSUNTIVO DELLE FATTURE REGISTRATE DALLE UNITA'' SEZIONALI DI SEGUITO SPECIFICATE';
               aTitoloReport3:=NULL;

               CNRCTB260.insTitColReportGenerico(repID,
                                                 lSequenza,
                                                 aTitoloReport1,
                                                 aTitoloReport2,
                                                 aTitoloReport3,
                                                 gparametri_tab(8).stringa1,
                                                 gparametri_tab(8).stringa2,
                                                 aEsercizioRif,
                                                 lgruppoStm,
                                                 lsottoGruppoStm,
                                                 ldescrizioneGruppoStm,
                                                 inTipoReport,
                                                 inTipoRegistro);

               -- Valorizzazione del dettaglio della copertina del riepilogo sezionali

               BEGIN

                  ldescrizioneGruppoStm:='CORPO GRUPPO A';

                  FOR i IN 1 .. lnumeroSezionali

                  LOOP

                     IF inTipoRegistro = 'A' THEN
                        lcodiceSezionale:=gsezio_acq_tab(i).stringa1;
                        ldescrizioneSezionale:=gsezio_acq_tab(i).stringa2;
                        lTipoCommercIstituz:=gsezio_acq_tab(i).stringa3;
                     ELSE
                        lcodiceSezionale:=gsezio_ven_tab(i).stringa1;
                        ldescrizioneSezionale:=gsezio_ven_tab(i).stringa2;
                        lTipoCommercIstituz:=gsezio_ven_tab(i).stringa3;
                     END IF;

                     lSequenza:=lSequenza + 20;

                     CNRCTB260.insProspettiPerRegistri(repID,
                                                       lSequenza,
                                                       lPasso,
                                                       inCdCdsOrigine,
                                                       inCdUoOrigine,
                                                       inEsercizio,
                                                       lcodiceSezionale,
                                                       ldescrizioneSezionale,
                                                       inDataInizio,
                                                       inDataFine,
                                                       inTipoReport,
                                                       inTipoRegistro,
                                                       lgruppoStm,
                                                       lsottoGruppoStm,
                                                       ldescrizioneGruppoStm);

                  END LOOP;

               END;

            END IF;

      -------------------------------------------------------------------------------------------
      -- Stampa del registro IVA e dei riepiloghi correlati

      ELSIF lPasso = 3 THEN

            lSequenza:=lSequenza + 20;

            -- Selezione ed intestazione del registro

            BEGIN

               FOR i IN 1 .. lnumeroSezionali

               LOOP

                  IF inTipoRegistro = 'A' THEN
                     lcodiceSezionale:=gsezio_acq_tab(i).stringa1;
                     ldescrizioneSezionale:=gsezio_acq_tab(i).stringa2;
                     lTipoCommercIstituz:=gsezio_acq_tab(i).stringa3;
                  ELSE
                     lcodiceSezionale:=gsezio_ven_tab(i).stringa1;
                     ldescrizioneSezionale:=gsezio_ven_tab(i).stringa2;
                     lTipoCommercIstituz:=gsezio_ven_tab(i).stringa3;
                  END IF;

                  lSequenza:=lSequenza + 20;

                  CNRCTB260.insFatturePerRegistri(repID,
                                                  lSequenza,
                                                  lPasso,
                                                  inCdCdsOrigine,
                                                  inCdUoOrigine,
                                                  inEsercizio,
                                                  lcodiceSezionale,
                                                  ldescrizioneSezionale,
                                                  lTipoCommercIstituz,
                                                  inDataInizio,
                                                  inDataFine,
                                                  inTipoRegistro,
                                                  inTipoReport,
                                                  inRistampa,
                                                  lgruppoStm,
                                                  lsottoGruppoStm,
                                                  ldescrizioneGruppoStm,
                                                  gparametri_tab(2).stringa1,
                                                  aTipoReportStato);

               END LOOP;

            END;

      -------------------------------------------------------------------------------------------
      -- Stampa del riepilogo per aliquota (solo in presenza di stampa per piש sezionali

      ELSIF lPasso = 4 THEN

            IF lnumeroSezionali > 1 THEN

               lSequenza:=lSequenza + 20;

               CNRCTB260.insRiepilogoIvaPerRegistri(repID,
                                                    lSequenza,
                                                    lPasso,
                                                    inEsercizio,
                                                    lgruppoStm,
                                                    lsottoGruppoStm,
                                                    ldescrizioneGruppoStm,
                                                    inCdCdsOrigine,
                                                    inCdUoOrigine,
                                                    inCdTipoSezionale);

            END IF;

      -------------------------------------------------------------------------------------------
      -- Stampa del Riporto dati dichiarazione di intento su registro Vendite definitivo
      -- ALLO STATO ATTUALE SOSPESA

      ELSIF lPasso = 5 THEN

            NULL;

--            IF aTipoReport = 'D' THEN
--
--               CNRCTB260.insRiportoDatiIntento(repID,
--                                               lSequenza,
--                                               lPasso,
--                                               inCdCdsOrigine,
--                                               inCdUoOrigine,
--                                               inEsercizio,
--                                               aCodiceSezionale,
--                                               inDataInizio,
--                                               inDataFine,
--                                               inTipoRegistro,
--                                               inRistampa,
--                                               lgruppoStm,
--                                               lsottoGruppoStm,
--                                               ldescrizioneGruppoStm);
--
--            END IF;

      -------------------------------------------------------------------------------------------
      -- Stampa del Riporto dati revoca dichiarazione di intento su registro Vendite definitivo
      -- ALLO STATO ATTUALE SOSPESA

      ELSIF lPasso = 6 THEN

            NULL;

--            IF aTipoReport = 'D' THEN
--
--               CNRCTB260.insRevocaDatiIntento(repID,
--                                              lSequenza,
--                                              lPasso,
--                                              aCdCdsOrigine,
--                                              aCdUoOrigine,
--                                              aEsercizio,
--                                              aCodiceSezionale,
--                                              aDataInizio,
--                                              aDataFine,
--                                              aTipoRegistro,
--                                              aRistampa,
--                                              lgruppoStm,
--                                              lsottoGruppoStm,
--                                              ldescrizioneGruppoStm);
--
--            END IF;

      -------------------------------------------------------------------------------------------
      -- Stampa delle annotazioni libere
      -- ALLO STATO ATTUALE SOSPESA

      ELSIF lPasso = 7 THEN

            NULL;

--         IF aTipoReport = 'D' THEN
--             lSequenza:=lSequenza + 20;
--
--             -- Valorizzazione del dettaglio della copertina del riepilogo sezionali
--
--             BEGIN
--
--                FOR i IN 1 .. lnumeroSezionali
--
--                LOOP
--
--                   IF aTipoRegistro = 'A' THEN
--                      lcodiceSezionale:=gsezio_acq_tab(i).stringa1;
--                      prefissoSezionale:=gsezio_acq_tab(i).stringa2;
--                      descrizioneSezionale:=gsezio_acq_tab(i).stringa3;
--                   ELSE
--                      lcodiceSezionale:=gsezio_ven_tab(i).stringa1;
--                      prefissoSezionale:=gsezio_ven_tab(i).stringa2;
--                      descrizioneSezionale:=gsezio_ven_tab(i).stringa3;
--                   END IF;
--
--                   lSequenza:=lSequenza + 20;
--
--                   Cnrctb260.insAnnotazioniLibere
--                           (
--                            repID,
--                            lSequenza,
--                            lPasso,
--                            lcodiceSezionale,
--                            prefissoSezionale,
--                            descrizioneSezionale,
--                            aCodiceEsercizio,
--                            aDataInizio,
--                            aDataFine,
--                            aTipoRegistro,
--                            lgruppoStm,
--                            lsottoGruppoStm,
--                            ldescrizioneGruppoStm
--                           );
--
--
--                END LOOP;
--
--             END;
--
--         END IF;

      -------------------------------------------------------------------------------------------
      -- Inserimento nella tabella REPORT DETTAGLIO delle fatture (solo per i registri definitivi)

      ELSIF lPasso = 8 THEN

            IF (inTipoReport = 'D' AND inRistampa = 'N') THEN

               BEGIN

                  FOR i IN 1 .. lnumeroSezionali

                  LOOP

                     IF inTipoRegistro = 'A' THEN
                        lcodiceSezionale:=gsezio_acq_tab(i).stringa1;
                        ldescrizioneSezionale:=gsezio_acq_tab(i).stringa2;
                        lTipoCommercIstituz:=gsezio_acq_tab(i).stringa3;
                     ELSE
                        lcodiceSezionale:=gsezio_ven_tab(i).stringa1;
                        ldescrizioneSezionale:=gsezio_ven_tab(i).stringa2;
                        lTipoCommercIstituz:=gsezio_ven_tab(i).stringa3;
                     END IF;

                     CNRCTB260.insDettaglioPerRegistri(repID,
                                                       lPasso,
                                                       ldescrizioneSezionale,
                                                       inCdCdsOrigine,
                                                       inCdUoOrigine,
                                                       inEsercizio,
                                                       lcodiceSezionale,
                                                       inDataInizio,
                                                       inDataFine,
                                                       inTipoRegistro,
                                                       id_utente,
                                                       aTipoReportStato);

                END LOOP;

          END;

      END IF;

      -------------------------------------------------------------------------------------------
      -- Inserimento nella tabella REPORT STATO di una riga (solo per i registri definitivi)

      ELSIF lPasso = 9 THEN

         IF (inTipoReport = 'D' AND inRistampa = 'N') THEN

            CNRCTB255.inserisciInReportStato(inCdCdsOrigine,
                                             inCdUoOrigine,
                                             inEsercizio,
                                             lCodiceSezionale,
                                             inDataInizio,
                                             inDataFine,
                                             inTipoRegistro,
                                             inGruppoReport,
                                             aTipoReportStato,
                                             id_utente);

         END IF;

      END IF;

   END LOOP;

END stampaRegistri;

-- =================================================================================================
-- Stampa dei Riepilogativi IVA
-- =================================================================================================
PROCEDURE stampaRiepilogativi
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inTipoReport VARCHAR2,
    inRistampa VARCHAR2,
    repID INTEGER,
    msg_out IN OUT VARCHAR2,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    aTipoReportStato VARCHAR2
   ) IS
   aTitoloReport1 VARCHAR2(200);
   aTitoloReport2 VARCHAR2(200);
   aTitoloReport3 VARCHAR2(200);
   lPasso INTEGER;
   lSequenza INTEGER;
   lnumeroSezionali INTEGER;
   ldesTipoRegistro VARCHAR2(30);
   lrigaStampa VARCHAR2(1000);
   lgruppoStm CHAR(1);
   lsottoGruppoStm CHAR(1);
   ldescrizioneGruppoStm CHAR(100);
   lcodiceSezionale VARCHAR2(10);
   ldescrizioneSezionale VARCHAR2(100);
   lTipoCommercIstituz CHAR(1);
   lSezionaleVuoto INTEGER;
   cv_cd_tipo_sezionale  VARCHAR2(30);
   lnumeroMese INTEGER;
   contaSezionali INTEGER;
   aEsercizioRif INTEGER;

   gen_cv GenericCurTyp;

BEGIN

   lSequenza:=0;
   lnumeroMese:=TO_NUMBER(TO_CHAR(inDataInizio,'MM'));
   contaSezionali:=0;

   ------------------------------------------------------------------------------------------------
   -- Costruzione del titolo report

   -- Intestazione della UO proprietaria del registro

   aTitoloReport1:='U.0. ' || inCdUoOrigine || ' ' || gparametri_tab(7).stringa1;


   -- Descrizione del registro

   aTitoloReport2:='REGISTRO RIEPILOGATIVO ';

   IF inTipoRegistro = 'A' THEN
      lnumeroSezionali:=gsezio_acq_tab.COUNT;
      aTitoloReport2:=aTitoloReport2 || 'ACQUISTI ';
      ldesTipoRegistro:=CNRCTB255.TI_REGISTRO_ACQUISTI;
   ELSE
      lnumeroSezionali:=gsezio_ven_tab.COUNT;
      aTitoloReport2:=aTitoloReport2 || 'VENDITE ';
      ldesTipoRegistro:=CNRCTB255.TI_REGISTRO_VENDITE;
   END IF;

   IF inTipoReport = 'P' THEN
      aTitoloReport2:=aTitoloReport2 || 'PROVVISORIO PERIODO DAL ' ||
                      TO_CHAR(inDataInizio, 'DD/MM/YYYY') || ' AL ' ||
                      TO_CHAR(inDataFine, 'DD/MM/YYYY');
   ELSE
      aTitoloReport2:=aTitoloReport2 || 'MESE DI ' || gmesi_tab(lnumeroMese).stringa1;
   END IF;

   aTitoloReport3:=NULL;

   aEsercizioRif:=TO_NUMBER(TO_CHAR(inDataFine,'YYYY'));

   ------------------------------------------------------------------------------------------------
   --  Ciclo per l'esecuzione delle query di valorizzazione dei campi per la stampa

   FOR lPasso IN 1 .. 6

   LOOP

      lSequenza:=lSequenza + 20;

      IF    lPasso = 1 THEN
            lrigaStampa:='Memorizzazione titolo report';
            lgruppoStm:='A';
            lsottoGruppoStm:='A';
            ldescrizioneGruppoStm:='TITOLO REPORT';
      ELSIF lPasso = 2 THEN
            lrigaStampa:='Memorizzazione titolo della prima seziona del riepilogativo';
            lgruppoStm:='B';
            lsottoGruppoStm:='B';
            ldescrizioneGruppoStm:='TITOLO GRUPPO B';
            aTitoloReport1:='RIEPILOGO CODICI IVA PER SINGOLO SEZIONALE CON TOTALI DI GRUPPO';
            aTitoloReport2:=NULL;
            aTitoloReport2:=NULL;
      ELSIF lPasso = 3 THEN
            lrigaStampa:='GRUPPO B - Riepilogativo con dettaglio per sezionale ';
            lgruppoStm:='B';
            lsottoGruppoStm:='B';
            ldescrizioneGruppoStm:='CORPO GRUPPO B';
      ELSIF lPasso = 4 THEN
            lrigaStampa:='Memorizzazione titolo della seconda sezione del riepilogativo';
            lgruppoStm:='C';
            lsottoGruppoStm:='C';
            ldescrizioneGruppoStm:='TITOLO GRUPPO C';
            aTitoloReport1:='RIEPILOGO CODICI IVA PER TUTTI I SEZIONALI CON TOTALI DI GRUPPO';
            aTitoloReport2:=NULL;
            aTitoloReport2:=NULL;
      ELSIF lPasso = 5 THEN
            lrigaStampa:='GRUPPO C - Riepilogativo con dettaglio per sezionale ';
            lgruppoStm:='C';
            lsottoGruppoStm:='C';
            ldescrizioneGruppoStm:='CORPO GRUPPO C';
      ELSIF lPasso = 6 THEN
            lrigaStampa:='GRUPPO D - Inserimento riga in REPORT_STATO';
            lgruppoStm:='D';
            lsottoGruppoStm:=NULL;
            ldescrizioneGruppoStm:='CORPO GRUPPO D';
      END IF;

      ---------------------------------------------------------------------------------------------
      -- Memorizzazione del titolo report o del riepilogativo

      IF    (lPasso = 1 OR
             lPasso = 2 OR
             lPasso = 4) THEN

            CNRCTB260.insTitColReportGenerico(repID,
                                              lSequenza,
                                              aTitoloReport1,
                                              aTitoloReport2,
                                              aTitoloReport3,
                                              gparametri_tab(8).stringa1,
                                              gparametri_tab(8).stringa2,
                                              aEsercizioRif,
                                              lgruppoStm,
                                              lsottoGruppoStm,
                                              ldescrizioneGruppoStm,
                                              inTipoReport,
                                              inTipoRegistro);

      ---------------------------------------------------------------------------------------------
      -- Stampa del registro riepilogativo con dettaglio per sezionale

      ELSIF lPasso = 3 THEN

            IF inTipoReport = 'P' THEN

               -- Ciclo principale per estrazione totali per sezionale (STAMPA PROVVISORIA)

               BEGIN

                  FOR i IN 1 .. lnumeroSezionali

                  LOOP

                     IF inTipoRegistro = 'A' THEN
                        lcodiceSezionale:=gsezio_acq_tab(i).stringa1;
                        ldescrizioneSezionale:=gsezio_acq_tab(i).stringa2;
                        lTipoCommercIstituz:=gsezio_acq_tab(i).stringa3;
                        lSezionaleVuoto:=gsezio_acq_tab(i).intero;
                     ELSE
                        lcodiceSezionale:=gsezio_ven_tab(i).stringa1;
                        ldescrizioneSezionale:=gsezio_ven_tab(i).stringa2;
                        lTipoCommercIstituz:=gsezio_ven_tab(i).stringa3;
                        lSezionaleVuoto:=gsezio_ven_tab(i).intero;
                     END IF;

                     -- L'estrazione ט eseguita solo per i sezionali valorizzati

                     IF lSezionaleVuoto > 0 THEN

                        lSequenza:=lSequenza + 20;
                        contaSezionali:=contaSezionali + 1;

                        CNRCTB260.insFatturePerRiepilogativi(repID,
                                                             lSequenza,
                                                             lPasso,
                                                             ldescrizioneSezionale,
                                                             lTipoCommercIstituz,
                                                             inCdCdsOrigine,
                                                             inCdUoOrigine,
                                                             inEsercizio,
                                                             lcodiceSezionale,
                                                             inDataInizio,
                                                             inDataFine,
                                                             inTipoRegistro,
                                                             inTipoReport,
                                                             lgruppoStm,
                                                             lsottoGruppoStm,
                                                             ldescrizioneGruppoStm);

                     END IF;

                  END LOOP;

               END;

            ELSE

               -- Ciclo principale per estrazione totali per sezionale (STAMPA DEFINITIVA)

               OPEN gen_cv FOR

                    SELECT cd_tipo_sezionale
                    FROM   REPORT_DETTAGLIO
                    WHERE  cd_cds = inCdCdsOrigine AND
                           cd_unita_organizzativa = inCdUoOrigine AND
                           esercizio = inEsercizio AND
                           data_inizio = inDataInizio AND
                           data_fine = inDataFine AND
                           tipo_report = ldesTipoRegistro
                    GROUP BY cd_tipo_sezionale
                    ORDER BY cd_tipo_sezionale;

               LOOP

                  FETCH gen_cv INTO
                        cv_cd_tipo_sezionale;

                  EXIT WHEN gen_cv%NOTFOUND;

                  BEGIN

                     SELECT ds_tipo_sezionale,
                            ti_istituz_commerc INTO ldescrizioneSezionale, lTipoCommercIstituz
                     FROM   TIPO_SEZIONALE
                     WHERE  cd_tipo_sezionale = cv_cd_tipo_sezionale;

                     lSequenza:=lSequenza + 20;
                     contaSezionali:=contaSezionali + 1;

                     CNRCTB260.insFatturePerRiepilogativi(repID,
                                                          lSequenza,
                                                          lPasso,
                                                          ldescrizioneSezionale,
                                                          lTipoCommercIstituz,
                                                          inCdCdsOrigine,
                                                          inCdUoOrigine,
                                                          inEsercizio,
                                                          cv_cd_tipo_sezionale,
                                                          inDataInizio,
                                                          inDataFine,
                                                          inTipoRegistro,
                                                          inTipoReport,
                                                          lgruppoStm,
                                                          lsottoGruppoStm,
                                                          ldescrizioneGruppoStm);

                  END;

               END LOOP;

               CLOSE gen_cv;

            END IF;

      ---------------------------------------------------------------------------------------------
      -- Stampa del registro riepilogativo senza dettaglio per sezionale. Si esegue solo se risultano
      -- elaborati piש di un sezionale

      ELSIF lPasso = 5 THEN

            IF contaSezionali > 0 THEN

               lSequenza:=lSequenza + 20;

               CNRCTB260.insTotaliPerRiepilogativi(repID,
                                                   lSequenza,
                                                   lPasso,
                                                   lgruppoStm,
                                                   lsottoGruppoStm,
                                                   ldescrizioneGruppoStm);

            END IF;

      ---------------------------------------------------------------------------------------------
      -- Inserimento nella tabella REPORT STATO di una riga (solo per i registri riepilogativi definitivi)

      ELSIF lPasso = 6 THEN

            IF (inTipoReport = 'D' AND inRistampa = 'N' ) THEN

               CNRCTB255.inserisciInReportStato(inCdCdsOrigine,
                                                inCdUoOrigine,
                                                inEsercizio,
                                                inCdTipoSezionale,
                                                inDataInizio,
                                                inDataFine,
                                                inTipoRegistro,
                                                inGruppoReport,
                                                aTipoReportStato,
                                                id_utente);

            END IF;

      END IF;

   END LOOP;

END stampaRiepilogativi;

-- =================================================================================================
-- Stampa dei Riepilogativi IVA del CENTRO
-- =================================================================================================
PROCEDURE stampaRiepilogativiCentro
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inTipoReport VARCHAR2,
    inRistampa VARCHAR2,
    repID INTEGER,
    msg_out IN OUT VARCHAR2,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    aTipoReportStato VARCHAR2
   ) IS
   aTitoloReport1 VARCHAR2(200);
   aTitoloReport2 VARCHAR2(200);
   aTitoloReport3 VARCHAR2(200);
   aNumeroMese INTEGER;
   aPasso INTEGER;
   aSequenza INTEGER;
   aGruppoStm CHAR(1);
   aSottoGruppoStm CHAR(1);
   aDescrizioneGruppoStm CHAR(100);
   aTipoDocumentoReportStato CHAR(1);
   stringaErrore VARCHAR2(2000);
   aEsercizioRif INTEGER;
   aSequenzaUo INTEGER;

   aRecTipoSezionale TIPO_SEZIONALE%ROWTYPE;
   aRecUnitaOrganizzativa UNITA_ORGANIZZATIVA%ROWTYPE;

   gen_cv GenericCurTyp;

BEGIN

   ------------------------------------------------------------------------------------------------
   -- Valorizzazione variabili

   aSequenza:=0;
   aSequenzaUo:=0;
   aNumeroMese:=TO_NUMBER(TO_CHAR(inDataInizio,'MM'));
   stringaErrore:=NULL;

   -- Recupero dati del sezionale ט sempre elaborato un unico sezionale per volta ------------------

   aRecTipoSezionale:=CNRCTB120.getTipoSezionale(inCdTipoSezionale);
   aTipoDocumentoReportStato:=NULL;
   IF aRecTipoSezionale.ti_istituz_commerc = 'C' THEN
      aTipoDocumentoReportStato:=TI_LIQ_IVA_COMMERC;
   ELSE
      IF    aRecTipoSezionale.fl_san_marino_senza_iva = 'Y' And aRecTipoSezionale.TI_BENE_SERVIZIO !='S' THEN
            aTipoDocumentoReportStato:=TI_LIQ_IVA_ISTSMSI;
      ELSIF aRecTipoSezionale.fl_intra_ue = 'Y' And aRecTipoSezionale.TI_BENE_SERVIZIO ='B' THEN
            aTipoDocumentoReportStato:=TI_LIQ_IVA_ISTINTR;
      Elsif (aRecTipoSezionale.fl_intra_ue = 'Y' Or aRecTipoSezionale.fl_extra_ue = 'Y' Or aRecTipoSezionale.fl_san_marino_senza_iva = 'Y' ) And  aRecTipoSezionale.TI_BENE_SERVIZIO ='S' Then
        aTipoDocumentoReportStato:=TI_LIQ_IVA_ISTSNR;
      END IF;
   END IF;

   -- Costruzione del titolo report ---------------------------------------------------------------

   -- Intestazione della UO proprietaria del registro

   aTitoloReport1:='U.0. ' || inCdUoOrigine || ' ' || gparametri_tab(7).stringa1;

   -- Descrizione del registro

   aTitoloReport2:='REGISTRO RIEPILOGATIVO CENTRO ';

   IF inTipoReport = 'P' THEN
      aTitoloReport2:=aTitoloReport2 || 'PROVVISORIO PERIODO DAL ' ||
                      TO_CHAR(inDataInizio, 'DD/MM/YYYY') || ' AL ' ||
                      TO_CHAR(inDataFine, 'DD/MM/YYYY');
   ELSE
      if('0101'||TO_CHAR(inDataInizio,'yyyy')= TO_CHAR(inDataInizio, 'DDMMYYYY') and
         '3112'||TO_CHAR(inDataFine,'yyyy')  = TO_CHAR(inDataFine, 'DDMMYYYY')) then
          aTitoloReport2:=aTitoloReport2 || 'ANNUALE ';
      else
         aTitoloReport2:=aTitoloReport2 || 'MESE DI ' || gmesi_tab(aNumeroMese).stringa1;
      end if;
   END IF;

   aTitoloReport2:=SUBSTR(aTitoloReport2 || ' PER SEZIONALE '  || inCdTipoSezionale || ' - ' ||
                          aRecTipoSezionale.ds_tipo_sezionale,1,190);

   aTitoloReport3:=NULL;

   aEsercizioRif:=TO_NUMBER(TO_CHAR(inDataFine,'YYYY'));

   ------------------------------------------------------------------------------------------------
   --  Ciclo per l'esecuzione delle query di valorizzazione dei campi per la stampa

   FOR aPasso IN 1 .. 6

   LOOP

      aSequenza:=aSequenza + 20;

      IF    aPasso = 1 THEN
            aGruppoStm:='A';
            aSottoGruppoStm:='A';
            aDescrizioneGruppoStm:='TITOLO REPORT';
      ELSIF aPasso = 2 THEN
            aGruppoStm:='B';
            aSottoGruppoStm:='B';
            aDescrizioneGruppoStm:='TITOLO GRUPPO B';
            aTitoloReport1:='RIEPILOGO CODICI IVA PER SINGOLO SEZIONALE CON TOTALI DI GRUPPO';
            aTitoloReport2:=NULL;
            aTitoloReport2:=NULL;
      ELSIF aPasso = 3 THEN
            aGruppoStm:='B';
            aSottoGruppoStm:='B';
            aDescrizioneGruppoStm:='CORPO GRUPPO B';
      ELSIF aPasso = 4 THEN
            aGruppoStm:='C';
            aSottoGruppoStm:='C';
            aDescrizioneGruppoStm:='TITOLO GRUPPO C';
            aTitoloReport1:='RIEPILOGO CODICI IVA PER TUTTI I SEZIONALI CON TOTALI DI GRUPPO';
            aTitoloReport2:=NULL;
            aTitoloReport2:=NULL;
      ELSIF aPasso = 5 THEN
            aGruppoStm:='C';
            aSottoGruppoStm:='C';
            aDescrizioneGruppoStm:='CORPO GRUPPO C';
      ELSIF aPasso = 6 THEN
            aGruppoStm:='D';
            aSottoGruppoStm:=NULL;
            aDescrizioneGruppoStm:='CORPO GRUPPO D';
      END IF;

      ---------------------------------------------------------------------------------------------
      -- Memorizzazione del titolo report o del riepilogativo

      IF    (aPasso = 1 OR
             aPasso = 2 OR
             aPasso = 4) THEN

            CNRCTB260.insTitColReportGenerico(repID,
                                              aSequenza,
                                              aTitoloReport1,
                                              aTitoloReport2,
                                              aTitoloReport3,
                                              gparametri_tab(8).stringa1,
                                              gparametri_tab(8).stringa2,
                                              aEsercizioRif,
                                              aGruppoStm,
                                              aSottoGruppoStm,
                                              aDescrizioneGruppoStm,
                                              inTipoReport,
                                              inTipoRegistro);

      ---------------------------------------------------------------------------------------------
      -- Stampa del registro riepilogativo a  rottura di unitא organizzativa

      ELSIF aPasso = 3 THEN

            -- Ciclo principale di estrazione delle U.0. che possono essere oggetto di elaborazione IVA

            BEGIN

               OPEN gen_cv FOR

                    SELECT A.cd_unita_organizzativa,
                           A.ds_unita_organizzativa,
                           A.cd_unita_padre
                    FROM   UNITA_ORGANIZZATIVA A
                    WHERE  A.livello = 2 AND
                           A.cd_tipo_unita != CNRCTB001.TIPO_ENTE AND
                           A.esercizio_inizio <= inEsercizio AND
                           A.esercizio_fine >= inEsercizio AND
                           NOT EXISTS
                               (SELECT 1
                                FROM   LIQUID_IVA_INTERF_CDS B
                                WHERE  B.esercizio = inEsercizio AND
                                       B.cd_cds = A.cd_unita_padre)
                    ORDER BY A.cd_unita_organizzativa;

               LOOP

                  FETCH gen_cv INTO
                        aRecUnitaOrganizzativa.cd_unita_organizzativa,
                        aRecUnitaOrganizzativa.ds_unita_organizzativa,
                        aRecUnitaOrganizzativa.cd_unita_padre;

                  EXIT WHEN gen_cv%NOTFOUND;

                  -- Inserimento dei dati sulla tabella report generico ---------------------------

                  aSequenza:=aSequenza + 20;

                  CNRCTB265.insFatturePerRiepilogoCentro(repID,
                                                         aSequenza,
                                                         aSequenzaUo,
                                                         aRecUnitaOrganizzativa.cd_unita_padre,
                                                         aRecUnitaOrganizzativa.cd_unita_organizzativa,
                                                         aRecUnitaOrganizzativa.ds_unita_organizzativa,
                                                         inEsercizio,
                                                         inCdTipoSezionale,
                                                         aRecTipoSezionale.ds_tipo_sezionale,
                                                         aRecTipoSezionale.ti_acquisti_vendite,
                                                         aTipoDocumentoReportStato,
                                                         inDataInizio,
                                                         inDataFine,
                                                         inTipoReport,
                                                         aGruppoStm,
                                                         aSottoGruppoStm,
                                                         aDescrizioneGruppoStm,
                                                         stringaErrore);

               END LOOP;

               IF inTipoReport = 'D' Then
                If inRistampa = 'N' Then
                  IF stringaErrore IS NOT NULL THEN
                     IBMERR001.RAISE_ERR_GENERICO
                        ('Impossibile procedere con la stampa definitiva del riepilogativo centro. ' ||
                         'Le seguenti UO non hanno il sezionale ' || inCdTipoSezionale || ' in stato definitivo ' ||
                         stringaErrore);
                  END IF;
                End If;
               END IF;

            END;

      ---------------------------------------------------------------------------------------------
      -- Stampa del registro riepilogativo senza dettaglio per sezionale.

      ELSIF aPasso = 5 THEN

            aSequenza:=aSequenza + 20;

            CNRCTB265.insTotaliPerRiepilogoCentro(repID,
                                                  inCdCdsOrigine,
                                                  inCdUoOrigine,
                                                  inEsercizio,
                                                  aSequenza,
                                                  inTipoReport,
                                                  aGruppoStm,
                                                  aSottoGruppoStm,
                                                  aDescrizioneGruppoStm);

      ---------------------------------------------------------------------------------------------
      -- Inserimento nella tabella REPORT STATO di una riga (solo per i registri riepilogativi definitivi)

      ELSIF aPasso = 6 THEN

            IF (inTipoReport = 'D' AND inRistampa = 'N' ) THEN

               CNRCTB255.inserisciInReportStato(inCdCdsOrigine,
                                                inCdUoOrigine,
                                                inEsercizio,
                                                inCdTipoSezionale,
                                                inDataInizio,
                                                inDataFine,
                                                inTipoRegistro,
                                                inGruppoReport,
                                                aTipoReportStato,
                                                id_utente);

            END IF;

      END IF;

   END LOOP;


END stampaRiepilogativiCentro;


-- =================================================================================================
-- Stampa IVA ad esigibilitא differita
-- =================================================================================================
PROCEDURE stampaIvaDifferita
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inTipoReport VARCHAR2,
    inRistampa VARCHAR2,
    repID INTEGER,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    aTipoReportStato VARCHAR2
   ) IS
   aTitoloReport1 VARCHAR2(200);
   aTitoloReport2 VARCHAR2(200);
   aTitoloReport3 VARCHAR2(200);
   lda_i BINARY_INTEGER;
   la_i BINARY_INTEGER;
   ldescrizioneSezionale VARCHAR2(100);
   lSezionaleVuoto INTEGER;
   lrigaStampa VARCHAR2(1000);
   lgruppoStm CHAR(1);
   lsottoGruppoStm CHAR(1);
   ldescrizioneGruppoStm VARCHAR2(100);
   ldescTotaleStm VARCHAR2(100);
   lPasso NUMBER;
   lSequenza INTEGER;
   lnumeroSezionali_ven INTEGER;
   lnumeroSezionali_acq INTEGER;
   lnumeroSezionali INTEGER;
   lcodiceSezionale VARCHAR2(10);
   cv_totale_imponibile NUMBER(15,2);
   cv_totale_iva NUMBER(15,2);
   cv_totale_iva_indet NUMBER(15,2);
   cv_totale_fattura NUMBER(15,2);
   aTotaleImponibile NUMBER(15,2);
   aTotaleIva NUMBER(15,2);
   aTotaleIvaIndetraibile NUMBER(15,2);
   aTotaleFattura NUMBER(15,2);
   aEsercizioRif INTEGER;

   gen_cv GenericCurTyp;
   UOENTE unita_organizzativa%rowtype:= CNRCTB020.GETUOENTE(inEsercizio);
BEGIN

   lSequenza:=0;
   aTotaleImponibile:=0;
   aTotaleIva:=0;
   aTotaleIvaIndetraibile:=0;
   aTotaleFattura:=0;

   lnumeroSezionali_acq:=gsezio_acq_tab.COUNT;
   lnumeroSezionali_ven:=gsezio_ven_tab.COUNT;
   lnumeroSezionali:=lnumeroSezionali_acq+lnumeroSezionali_ven;

   -------------------------------------------------------------------------------------------------
   -- Definizione del numero di passi da eseguire nella stampa IVA differita
   -- Nella stampa delle sole fatture divenute esigibili per ateneo si espone la sola sezione II
   -- Nella stampa del riepilogo iva ad esigibilitא differita per riepilogo si esegue anche il passo
   -- delle totalizzazioni per sezionale all'interno di ogni sezione in stampa

   IF    inTipoStampa = TI_ELAB_IVA_DIFFERITA_CENTRO THEN
         lda_i:=1;
         la_i:=5;
   ELSIF inTipoStampa = TI_ELAB_IVA_DIFFERITA_II THEN
         lda_i:=2;
         la_i:=2;
   Else   --sembra l'unica gestita
         lda_i:=1;
          IF inTipoRegistro = 'A' Then
          -- Aggiunta sezione fatture divenute esigibile perchט trascorso 1 anno dalla data emissione fornitore
                la_i:=6;
          Else
                la_i:=5;
          End If;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Costruzione del titolo report
   -- ***********************************************************************************************************
   -- Attenzione: inTipoRegistro = '*', ma poiche' non sono gestite le differite per gli acquisti, lasciamo cosi'
   -- ***********************************************************************************************************
   -- Intestazione della UO proprietaria del registro

   aTitoloReport1:='U.O.: ' || inCdUoOrigine || ' ' || gparametri_tab(7).stringa1;

   IF inTipoStampa = TI_ELAB_IVA_DIFFERITA_II THEN
      aTitoloReport2:='STAMPA FATTURE DIVENUTE ESIGIBILI - SITUAZIONE AL ';
   ELSE
      aTitoloReport2:='RIEPILOGO OPERAZIONI ';
      IF inTipoRegistro = 'A' THEN
         IF inTipoStampa = TI_ELAB_IVA_DIFFERITA_CENTRO THEN
            aTitoloReport2:= aTitoloReport2 || 'PASSIVE CON IVA AD ESIGIBILITA DIFFERITA CENTRO - SITUAZIONE AL ';
         ELSE
            aTitoloReport2:= aTitoloReport2 || 'PASSIVE CON IVA AD ESIGIBILITA DIFFERITA - SITUAZIONE AL ';
         END IF;
      ELSE
         IF inTipoStampa = TI_ELAB_IVA_DIFFERITA_CENTRO THEN
            aTitoloReport2:= aTitoloReport2 || 'ATTIVE CON IVA AD ESIGIBILITA DIFFERITA CENTRO - SITUAZIONE AL ';
         ELSE
            aTitoloReport2:= aTitoloReport2 || 'ATTIVE CON IVA AD ESIGIBILITA DIFFERITA - SITUAZIONE AL ';
         END IF;
      END IF;
   END IF;
   aTitoloReport2:=aTitoloReport2 || TO_CHAR(inDataFine,'DD/MM/YYYY');
   aTitoloReport3:=NULL;

   aEsercizioRif:=TO_NUMBER(TO_CHAR(inDataFine,'YYYY'));

   -------------------------------------------------------------------------------------------------
   --  Ciclo per l'esecuzione delle query di valorizzazione dei campi per la stampa

   FOR lPasso IN 1 .. 4

   LOOP

      lSequenza:=lSequenza + 20;

      IF    lPasso = 1 THEN
            lrigaStampa:='Memorizzazione titolo report';
            lgruppoStm:='A';
            lsottoGruppoStm:='A';
            ldescrizioneGruppoStm:='TITOLO REPORT';
      ELSIF lPasso = 2 THEN
            lrigaStampa:='Stampa delle fatture con IVA differita e totale parziale';
      ELSIF lPasso = 3 THEN
            lrigaStampa:='Generazione delle righe del riepilogo';
            lgruppoStm:='T';
            lsottoGruppoStm:='T';
            ldescrizioneGruppoStm:='TOTALE REPORT';
      END IF;

      ----------------------------------------------------------------------------------------------
      -- Memorizzazione del titolo report

      IF    lPasso = 1 THEN

            Cnrctb260.insTitColReportGenerico(repID,
                                              lSequenza,
                                              aTitoloReport1,
                                              aTitoloReport2,
                                              aTitoloReport3,
                                              gparametri_tab(8).stringa1,
                                              gparametri_tab(8).stringa2,
                                              aEsercizioRif,
                                              lgruppoStm,
                                              lsottoGruppoStm,
                                              ldescrizioneGruppoStm,
                                              inTipoReport,
                                              inTipoRegistro);


      ----------------------------------------------------------------------------------------------
      -- Stampa dei riepilogativi IVA nelle cinque sezioni

      ELSIF lPasso = 2 THEN

            FOR i IN lda_i .. la_i

            LOOP

               IF    i = 1 THEN
                     lgruppoStm:='A';
                     lsottoGruppoStm:='A';
                     ldescrizioneGruppoStm:='CORPO SEZIONE I';
                     ldescTotaleStm:='TOTALE PRIMA SEZIONE';
               ELSIF i = 2 THEN
                     lgruppoStm:='B';
                     lsottoGruppoStm:='B';
                     ldescrizioneGruppoStm:='CORPO SEZIONE II';
                     ldescTotaleStm:='TOTALE SECONDA SEZIONE';
               ELSIF i = 3 THEN
                     lgruppoStm:='C';
                     lsottoGruppoStm:='C';
                     ldescrizioneGruppoStm:='CORPO SEZIONE III';
                     ldescTotaleStm:='TOTALE TERZA SEZIONE';
               ELSIF i = 4 THEN
                     lgruppoStm:='D';
                     lsottoGruppoStm:='D';
                     ldescrizioneGruppoStm:='CORPO SEZIONE IV';
                     ldescTotaleStm:='TOTALE QUARTA SEZIONE';
               ELSIF i = 5 THEN
                     lgruppoStm:='E';
                     lsottoGruppoStm:='E';
                     ldescrizioneGruppoStm:='CORPO SEZIONE V';
                     ldescTotaleStm:='TOTALE QUINTA SEZIONE';
                ELSIF i = 6 THEN
                     lgruppoStm:='F';
                     lsottoGruppoStm:='F';
                     ldescrizioneGruppoStm:='CORPO SEZIONE VI';
                     ldescTotaleStm:='TOTALE SESTA SEZIONE';
               END IF;

               -- Loop per ogni sezione per recupero dettaglio fatture

               BEGIN

                  FOR j IN 1 .. lnumeroSezionali

                  Loop
                     If inTipoRegistro = 'A' THEN
                        lcodiceSezionale:=gsezio_acq_tab(j).stringa1;
                        ldescrizioneSezionale:=gsezio_acq_tab(j).stringa2;
                        lSezionaleVuoto:=gsezio_acq_tab(j).intero;
                     ELSE
                        lcodiceSezionale:=gsezio_ven_tab(j).stringa1;
                        ldescrizioneSezionale:=gsezio_ven_tab(j).stringa2;
                        lSezionaleVuoto:=gsezio_ven_tab(j).intero;
                     END IF;
                     If (lSezionaleVuoto > 0 And inCdCdsOrigine <> UOENTE.cd_unita_padre) Or  inCdCdsOrigine = UOENTE.cd_unita_padre THEN

                        lSequenza:=lSequenza + 20;

                        CNRCTB260.insFattureIvaDifferita(repID,
                                                         lSequenza,
                                                         ldescrizioneSezionale,
                                                         inCdCdsOrigine,
                                                         inCdUoOrigine,
                                                         inEsercizio,
                                                         lcodiceSezionale,
                                                         inDataInizio,
                                                         inDataFine,
                                                         inTipoRegistro,
                                                         inTipoReport,
                                                         inRistampa,
                                                         lgruppoStm,
                                                         lsottoGruppoStm,
                                                         ldescrizioneGruppoStm);

                     END IF;

                  END LOOP;

                  -- Calcolo dei totali per seszionale all'interno di una sezione

                  IF inTipoStampa = TI_ELAB_IVA_DIFFERITA_CENTRO THEN

                     lSequenza:=lSequenza + 20;

                     CNRCTB260.insTotaliSezionaleIvaDifferita(repID,
                                                              lSequenza,
                                                              lgruppoStm,
                                                              lsottoGruppoStm,
                                                              ldescTotaleStm);

                  END IF;

                  -- Calcolo del totale per sezione e codice iva

                  lSequenza:=lSequenza + 20;

      CNRCTB260.insTotaliIvaDiffPerCodIVA(repID,
                                                  lSequenza,
                                                  lgruppoStm,
                                                  lsottoGruppoStm,
                                                  ldescTotaleStm);

                  -- Calcolo del totale per sezione

                  lSequenza:=lSequenza + 20;

                  CNRCTB260.insTotaliIvaDifferita(repID,
                                                  lSequenza,
                                                  lgruppoStm,
                                                  lsottoGruppoStm,
                                                  ldescTotaleStm);

               END;

            END LOOP;

      ----------------------------------------------------------------------------------------------
      -- Generazione delle righe del riepilogo

      ELSIF lPasso = 3 THEN

            -- Ciclo di lettura importi complessivi emesso del periodo

            FOR i IN 1 .. lnumeroSezionali

            LOOP

               BEGIN

                  IF inTipoRegistro = 'A' THEN

                     lcodiceSezionale:=gsezio_acq_tab(i).stringa1;

                     OPEN gen_cv FOR

                          SELECT SUM(imponibile_dettaglio),
                                 SUM(iva_dettaglio),
                                 SUM(TRUNC(iva_indetraibile_dettaglio,2)),
                                 SUM(totale_dettaglio)
                          FROM   V_TOTALE_IVA_ACQUISTI
                          WHERE  cd_cds_origine = inCdCdsOrigine AND
                                 cd_uo_origine = inCdUoOrigine AND
                                 esercizio = inEsercizio AND
                                 cd_tipo_sezionale = lcodiceSezionale AND
                                (data_registrazione BETWEEN inDataInizio AND inDataFine);

                  ELSE

                     lcodiceSezionale:=gsezio_ven_tab(i).stringa1;

                     OPEN gen_cv FOR

                          SELECT SUM(imponibile_dettaglio),
                                 SUM(iva_dettaglio),
                                 SUM(iva_indetraibile_dettaglio),
                                 SUM(totale_dettaglio)
                          FROM   V_TOTALE_IVA_VENDITE
                          WHERE  cd_cds_origine = inCdCdsOrigine AND
                                 cd_uo_origine = inCdUoOrigine AND
                                 esercizio = inEsercizio AND
                                 cd_tipo_sezionale = lcodiceSezionale AND
                                 (data_emissione BETWEEN inDataInizio AND inDataFine);

                  END IF;

                  LOOP

                     FETCH gen_cv INTO
                           cv_totale_imponibile,
                           cv_totale_iva,
                           cv_totale_iva_indet,
                           cv_totale_fattura;

                     EXIT WHEN gen_cv%NOTFOUND;

                     atotaleImponibile:=atotaleImponibile + cv_totale_imponibile;
                     atotaleIva:=atotaleIva + cv_totale_iva;
                     atotaleIvaIndetraibile:=atotaleIvaIndetraibile + ROUND(cv_totale_iva_indet,2);
                     atotaleFattura:=atotaleFattura + cv_totale_fattura;

                  END LOOP;

                  CLOSE gen_cv;

               END;

            END LOOP;

            lSequenza:=lSequenza + 20;

            CNRCTB260.insRiepilogoIvaDifferita(repID,
                                               lSequenza,
                                               inTipoRegistro,
                                               inTipoReport,
                                               atotaleImponibile,
                                               atotaleIva,
                                               atotaleIvaIndetraibile,
                                               atotaleFattura,
                                               lgruppoStm,
                                               lsottoGruppoStm,
                                               ldescTotaleStm);

      ---------------------------------------------------------------------------------------------
      -- Inserimento nella tabella REPORT STATO di una riga (solo per i registri riepilogativi definitivi)

      ELSIF lPasso = 4 THEN

            IF (inTipoReport = 'D' AND inRistampa = 'N' ) THEN

               CNRCTB255.inserisciInReportStato(inCdCdsOrigine,
                                                inCdUoOrigine,
                                                inEsercizio,
                                                inCdTipoSezionale,
                                                inDataInizio,
                                                inDataFine,
                                                inTipoRegistro,
                                                inGruppoReport,
                                                aTipoReportStato,
                                                id_utente);

            END IF;

      END IF;

   END LOOP;

END stampaIvaDifferita;

-- =================================================================================================
-- Elaborazione della LIQUIDAZIONE (provvisoria o definitiva) di massa (centro per tutte le UO)
-- =================================================================================================
PROCEDURE elaboraLiquidazioneMassa
   (
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoStampa VARCHAR2,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    aRistampa VARCHAR2,
    repID INTEGER,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    aEsercizioReale NUMBER,
    aTipoReportStato VARCHAR2,
    inTipoImpegno VARCHAR2 Default Null
   ) IS
   i BINARY_INTEGER;
   j BINARY_INTEGER;
   aGruppoReport CHAR(1);
   chkEsisteLiqDef CHAR(1);

   aRecUnitaOrganizzativa UNITA_ORGANIZZATIVA%ROWTYPE;
   aRecLiquidazioneIvaBase LIQUIDAZIONE_IVA%ROWTYPE;
   aRecLiquidazioneIva LIQUIDAZIONE_IVA%ROWTYPE;

   gen_cv GenericCurTyp;

BEGIN
   -- Azzeramento array errori

   errori_tab.DELETE;

   j:=0;

   --cancello eventuale elaborazione precedente
   If aTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA_PRV Then
     delete LIQUIDAZIONE_IVA
     Where report_id = repID
     and   report_id != 0
     and   stato = 'P';
   End If;

   --------------------------------------------------------------------------------------------------
   -- Azzera il record della liquidazione di default

   aRecLiquidazioneIvaBase:=NULL;
   aRecLiquidazioneIvaBase.esercizio:=aEsercizio;
   aRecLiquidazioneIvaBase.dt_inizio:=aDataInizio;
   aRecLiquidazioneIvaBase.dt_fine:=aDataFine;
   aRecLiquidazioneIvaBase.report_id:=repID;
   if aTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA Then
     aRecLiquidazioneIvaBase.stato:='D';
   Else
     aRecLiquidazioneIvaBase.stato:='P';
   End If;
   aRecLiquidazioneIvaBase.iva_vendite:=0;
   aRecLiquidazioneIvaBase.iva_vendite_diff:=0;
   aRecLiquidazioneIvaBase.iva_vend_diff_esig:=0;
   aRecLiquidazioneIvaBase.iva_autofatt:=0;
   aRecLiquidazioneIvaBase.iva_intraue:=0;
   aRecLiquidazioneIvaBase.iva_debito:=0;
   aRecLiquidazioneIvaBase.iva_acquisti:=0;
   aRecLiquidazioneIvaBase.iva_acq_non_detr:=0;
   aRecLiquidazioneIvaBase.iva_acquisti_diff:=0;
   aRecLiquidazioneIvaBase.iva_acq_diff_esig:=0;
   aRecLiquidazioneIvaBase.iva_credito:=0;
   aRecLiquidazioneIvaBase.var_imp_per_prec:=0;
   aRecLiquidazioneIvaBase.iva_non_vers_per_prec:=0;
   aRecLiquidazioneIvaBase.iva_deb_cred_per_prec:=0;
   aRecLiquidazioneIvaBase.cred_iva_comp_detr:=0;
   aRecLiquidazioneIvaBase.iva_deb_cred:=0;
   aRecLiquidazioneIvaBase.int_deb_liq_trim:=0;
   aRecLiquidazioneIvaBase.cred_iva_spec_detr:=0;
   aRecLiquidazioneIvaBase.acconto_iva_vers:=0;
   aRecLiquidazioneIvaBase.iva_da_versare:=0;
   aRecLiquidazioneIvaBase.iva_versata:=0;
   aRecLiquidazioneIvaBase.cred_iva_infrann_rimb:=0;
   aRecLiquidazioneIvaBase.cred_iva_infrann_comp:=0;
   aRecLiquidazioneIvaBase.iva_credito_no_prorata:=0;
   aRecLiquidazioneIvaBase.perc_prorata_detraibile:=0;
   aRecLiquidazioneIvaBase.dacr:=gDataOdierna;
   aRecLiquidazioneIvaBase.utcr:=id_utente;
   aRecLiquidazioneIvaBase.duva:=gDataOdierna;
   aRecLiquidazioneIvaBase.utuv:=id_utente;
   aRecLiquidazioneIvaBase.pg_ver_rec:=1;
   aRecLiquidazioneIvaBase.iva_liq_esterna:=0;

   --------------------------------------------------------------------------------------------------
   -- Ciclo principale di lettura delle UO su cui eseguire la liquidazione

   BEGIN
      OPEN gen_cv FOR

           SELECT *
           FROM   UNITA_ORGANIZZATIVA A
           WHERE  A.livello = 2 AND
                  A.cd_tipo_unita != 'ENTE' AND
                  A.esercizio_inizio <= aEsercizioReale AND
                  A.esercizio_fine >= aEsercizioReale AND
                  NOT EXISTS
                      (SELECT 1
                       FROM   LIQUID_IVA_INTERF_CDS C
                       WHERE  C.cd_cds = A.cd_unita_padre AND
                              C.esercizio = aEsercizioReale)
           ORDER BY A.cd_unita_organizzativa;

      LOOP

         FETCH gen_cv INTO
               aRecUnitaOrganizzativa;

         EXIT WHEN gen_cv%NOTFOUND;
         -- Valorizzo i riferimenti cds e Uo della liquidazione

         aRecLiquidazioneIvaBase.cd_cds:=aRecUnitaOrganizzativa.cd_unita_padre;
         aRecLiquidazioneIvaBase.cd_unita_organizzativa:=aRecUnitaOrganizzativa.cd_unita_organizzativa;

         --------------------------------------------------------------------------------------------
         -- Per ogni UO calcolo le cinque tipologie di Liquidazione

         FOR i IN 1 .. 5

         Loop
            IF    i = 1 THEN
                  aGruppoReport:=TI_LIQ_IVA_COMMERC;
            ELSIF i = 2 THEN
                  aGruppoReport:=TI_LIQ_IVA_ISTINTR;
            ELSIF i = 3 THEN
                  aGruppoReport:=TI_LIQ_IVA_ISTSMSI;
            ELSIF i = 4 THEN
                  aGruppoReport:=TI_LIQ_IVA_ISTSNR;
            ELSIF i = 5 THEN
                  aGruppoReport:=TI_LIQ_IVA_ISTSPLIT;
            END IF;

            -- Per la liquidazione provvisoria massiva il calcolo ט solo per il commerciale
            If i > 1 and aTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA_PRV Then
              exit;
            End If;

            aRecLiquidazioneIvaBase.tipo_liquidazione:=aGruppoReport;

            -- Verifico che non esiste liquidazione definitiva quindi procedo al calcolo

            chkEsisteLiqDef:=CNRCTB260.chkEsisteLiquidazioneDef(aRecLiquidazioneIvaBase.cd_cds,
                                                                aRecLiquidazioneIvaBase.cd_unita_organizzativa,
                                                                aEsercizio,
                                                                aDataInizio,
                                                                aDataFine,
                                                                aGruppoReport);

            IF chkEsisteLiqDef = 'N' THEN

               BEGIN

                  -- Creo lo liquidazione vuota

                  CNRCTB265.insLiquidazioneIva(aRecLiquidazioneIvaBase);

                  -- Elabora il calcolo della liquidazione

                  calcolaLiquidazione(aRecLiquidazioneIvaBase.cd_cds, aRecLiquidazioneIvaBase.cd_unita_organizzativa,
                                      aEsercizio, aCdTipoSezionale, aDataInizio, aDataFine, aTipoRegistro,
                                      aTipoReport, aRistampa, repID, id_utente, aGruppoReport, aEsercizioReale);

                  If aTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA Then
                    -- Scrittura record di liquidazione definitivo

                    liquidazioneDefinitiva(aRecLiquidazioneIvaBase.cd_cds, aRecLiquidazioneIvaBase.cd_unita_organizzativa,
                                           aEsercizio, aCdTipoSezionale, aDataInizio, aDataFine, aTipoStampa,
                                           aTipoRegistro, aTipoReport, aRistampa, repID, id_utente,
                                           aGruppoReport, aTipoReportStato);

                    BEGIN

                      SELECT * INTO aRecLiquidazioneIva
                      FROM   LIQUIDAZIONE_IVA A
                      WHERE  A.cd_cds = aRecLiquidazioneIvaBase.cd_cds AND
                             A.cd_unita_organizzativa = aRecLiquidazioneIvaBase.cd_unita_organizzativa AND
                             A.esercizio = aEsercizio AND
                             A.dt_inizio = aDataInizio AND
                             A.dt_fine = aDataFine AND
                             A.tipo_liquidazione = aGruppoReport AND
                             A.report_id = 0;

                    EXCEPTION

                      WHEN NO_DATA_FOUND THEN
                          IBMERR001.RAISE_ERR_GENERICO
                              ('Liquidazione iva del mese di ' || TO_CHAR(aDataInizio,'mm') || ' non presente');

                    END;

                    -- Contabilizzazione della liquidazione

                    CNRCTB270.contabilLiquidIva(aRecLiquidazioneIva.cd_cds, aEsercizio,
                                                aRecLiquidazioneIva.cd_unita_organizzativa,
                                                aDataInizio, aDataFine, id_utente, aGruppoReport,inTipoImpegno);

                  End If;

                  -- Elimino quanto registrato sulla tabella di appoggio nel calcolo della liquidazione

                  DELETE FROM REPORT_GENERICO
                  WHERE  ID = repID;


                  COMMIT;

               EXCEPTION

                  WHEN others THEN
                       j:=j + 1;
                       errori_tab(j).tStringaUo:=aRecLiquidazioneIvaBase.cd_unita_organizzativa;
                       errori_tab(j).tStringaTipo:=aGruppoReport;
                       errori_tab(j).tStringaErr:=SUBSTR(DBMS_UTILITY.FORMAT_ERROR_STACK,1,2000);
                       ROLLBACK;
               END;

            END IF;

         END LOOP;

      END LOOP;

   END;

   -- Se ho trovato errori faccio la raise di un errore generico

   IF errori_tab.COUNT > 0 THEN
     If aTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA Then
       IBMERR001.RAISE_ERR_GENERICO
          ('Liquidazione iva di massa con errori');
     Else
       IBMERR001.RAISE_ERR_GENERICO
          ('Liquidazione provvisoria iva di massa con errori');
     End If;
   END IF;

   -- Aggiornamento a stato C dei record di REPORT_STATO riferiti alla stampa registri IVA di base
   -- alla liquidazione
   If aTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA Then
     CNRCTB255.upgStatoRegistriIva(aEsercizioReale, aDataInizio, aDataFine, id_utente);
   End If;
END elaboraLiquidazioneMassa;

-- =================================================================================================
-- Stampa Liquidazione IVA
-- =================================================================================================
PROCEDURE stampaLiquidazione
   (
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoStampa VARCHAR2,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    aRistampa VARCHAR2,
    repID INTEGER,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    aEsercizioReale NUMBER,
    aTipoReportStato VARCHAR2,
    inTipoImpegno VARCHAR2 Default Null
   ) IS
   aRecLiquidazioneIva LIQUIDAZIONE_IVA%ROWTYPE;

BEGIN

   ------------------------------------------------------------------------------------------------
   -- Elabora il calcolo della liquidazione

   calcolaLiquidazione(aCdCdsOrigine, aCdUoOrigine, aEsercizio, aCdTipoSezionale,
                       aDataInizio, aDataFine, aTipoRegistro, aTipoReport,
                       aRistampa, repID, id_utente, inGruppoReport, aEsercizioReale);

   ------------------------------------------------------------------------------------------------
   -- Scrittura record di liquidazione definitivo

   IF (aTipoStampa=TI_ELAB_LIQUIDAZIONE_DEF AND
       aTipoReport ='D') THEN
      liquidazioneDefinitiva(aCdCdsOrigine, aCdUoOrigine, aEsercizio,
                             aCdTipoSezionale, aDataInizio, aDataFine, aTipoStampa,
                             aTipoRegistro, aTipoReport, aRistampa, repID, id_utente,
                             inGruppoReport, aTipoReportStato);

      BEGIN

         SELECT * INTO aRecLiquidazioneIva
         FROM   LIQUIDAZIONE_IVA A
         WHERE  A.cd_cds = aCdCdsOrigine AND
                A.cd_unita_organizzativa = aCdUoOrigine AND
                A.esercizio = aEsercizio AND
                A.dt_inizio = aDataInizio AND
                A.dt_fine = aDataFine AND
                A.tipo_liquidazione = inGruppoReport AND
                A.report_id = 0;

      EXCEPTION

         WHEN NO_DATA_FOUND THEN
              IBMERR001.RAISE_ERR_GENERICO
                 ('Liquidazione iva del mese di ' || TO_CHAR(aDataInizio,'mm') || ' non presente');

      END;

      -- Contabilizzazione della liquidazione

      CNRCTB270.contabilLiquidIva(aCdCdsOrigine,
                                  aEsercizio,
                                  aCdUoOrigine,
                                  aDataInizio,
                                  aDataFine,
                                  id_utente,
                                  inGruppoReport,
                                  inTipoImpegno);

   END IF;

END stampaLiquidazione;

-- =================================================================================================
-- Calcolo della Liquidazione IVA
--- ================================================================================================
PROCEDURE calcolaLiquidazione
   (
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    aRistampa VARCHAR2,
    repID INTEGER,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    aEsercizioReale NUMBER
   ) IS
   aPasso NUMBER;
   aGruppoStm CHAR(1);
   aSottoGruppoStm CHAR(1);
   aDescrizioneGruppoStm CHAR(100);
   aNumeroSezionali INTEGER;
   aCodiceSezionale VARCHAR2(10);
   i BINARY_INTEGER;
   j BINARY_INTEGER;
   isElaboraOk CHAR(1);
   UOENTE unita_organizzativa%rowtype:= CNRCTB020.GETUOENTE(aEsercizio);
BEGIN

   -------------------------------------------------------------------------------------------------
   --  Ciclo per l'esecuzione delle query di calcolo della Liquidazione IVA

   FOR aPasso IN 1 .. 3

   LOOP

      IF    aPasso = 1 THEN
            aGruppoStm:='A';
            aSottoGruppoStm:='A';
            aDescrizioneGruppoStm:='CORPO GRUPPO A';
      ELSIF aPasso = 2 THEN
            aGruppoStm:='A';
            aSottoGruppoStm:='A';
            aDescrizioneGruppoStm:='CORPO GRUPPO A';
      ELSIF aPasso = 3 THEN
            aGruppoStm:='A';
            aSottoGruppoStm:='A';
            aDescrizioneGruppoStm:='CORPO GRUPPO A';
      END IF;

      ----------------------------------------------------------------------------------------------
      -- Lettura dati da liquidazione e creazione record di appoggio su REPORT_GENERICO

      IF    aPasso = 1 THEN

            CNRCTB260.letturaLiquidazione(repID,
                                          aCdCdsOrigine,
                                          aEsercizio,
                                          aCdUoOrigine,
                                          aDataInizio,
                                          aDataFine,
                                          aTipoRegistro,
                                          aTipoReport,
                                          aGruppoStm,
                                          aSottoGruppoStm,
                                          aDescrizioneGruppoStm,
                                          inGruppoReport);

      ----------------------------------------------------------------------------------------------
      -- Calcolo importi per la liquidazione distinguo tra liquidazione dell'ente o altro

      ELSIF aPasso = 2 THEN

            -- Calcola liquidazione ente -----------------------------------------------------------

            IF aCdCdsOrigine =UOENTE.cd_unita_padre THEN

               CNRCTB260.calcolaLiquidazioneEnte(repID,
                                                 aCdCdsOrigine,
                                                 aCdUoOrigine,
                                                 aEsercizio,
                                                 aDataInizio,
                                                 aDataFine,
                                                 aTipoRegistro,
                                                 aTipoReport,
                                                 aGruppoStm,
                                                 aSottoGruppoStm,
                                                 aDescrizioneGruppoStm,
                                                 gparametri_tab(5).stringa1,
                                                 gparametri_tab(6).stringa1,
                                                 aEsercizioReale,
                                                 inGruppoReport);
            ELSE

            -- Calcola liquidazione UO -------------------------------------------------------------

               BEGIN

                  FOR i IN 1 .. 2

                  LOOP

                     IF i = 1 THEN
                        aNumeroSezionali:=gsezio_acq_tab.COUNT;
                     ELSE
                        aNumeroSezionali:=gsezio_ven_tab.COUNT;
                     END IF;

                     IF aNumeroSezionali > 0 THEN

                        FOR j IN 1 .. aNumeroSezionali

                        LOOP

                           isElaboraOk:='N';

                           IF i = 1 THEN

                              aCodiceSezionale:=gsezio_acq_tab(j).stringa1;

                              IF    inGruppoReport = TI_LIQ_IVA_COMMERC THEN
                                    IF  gsezio_acq_tab(j).stringa3 = 'C' THEN
                                        isElaboraOk:='Y';
                                    END IF;
                              ELSIF inGruppoReport = TI_LIQ_IVA_ISTINTR THEN
                                    IF  (gsezio_acq_tab(j).stringa3 = 'I' AND
                                         gsezio_acq_tab(j).stringa5 = 'Y' And
                                         gsezio_acq_tab(j).stringa6 = 'B') THEN
                                        isElaboraOk:='Y';
                                    END IF;
                              ELSIF inGruppoReport = TI_LIQ_IVA_ISTSMSI THEN
                                    IF  (gsezio_acq_tab(j).stringa3 = 'I' AND
                                         gsezio_acq_tab(j).stringa4 = 'Y' And
                                         gsezio_acq_tab(j).stringa6 = 'B') THEN
                                        isElaboraOk:='Y';
                                    END IF;
                              ELSIF inGruppoReport = TI_LIQ_IVA_ISTSNR THEN
                                    IF  (gsezio_acq_tab(j).stringa3 = 'I' And    -- istituzionale
                                         gsezio_acq_tab(j).stringa6 = 'S' And    -- ti_bene_servizio
                                        (gsezio_acq_tab(j).stringa5 = 'Y' Or     -- intra_ue
                                         gsezio_acq_tab(j).stringa4 = 'Y' Or     -- san marino senza iva
                                         gsezio_acq_tab(j).stringa7 = 'Y')) Then -- extra_ue
                                        isElaboraOk:='Y';
                                    END IF;
                              ELSIF inGruppoReport = TI_LIQ_IVA_ISTSPLIT THEN
                                    IF  (gsezio_acq_tab(j).stringa3 = 'I' And    -- istituzionale
                                         gsezio_acq_tab(j).stringa8 = 'Y')  Then -- split payment
                                        isElaboraOk:='Y';
                                    END IF;
                              END IF;

                           ELSE
            aCodiceSezionale:=gsezio_ven_tab(j).stringa1;

                              IF    inGruppoReport = TI_LIQ_IVA_COMMERC THEN
                                    IF  gsezio_ven_tab(j).stringa3 = 'C' THEN
                                        isElaboraOk:='Y';
                                    END IF;
                              ELSIF inGruppoReport = TI_LIQ_IVA_ISTINTR THEN
                                    IF  (gsezio_ven_tab(j).stringa3 = 'I' AND
                                         gsezio_ven_tab(j).stringa5 = 'Y' And
                                         gsezio_ven_tab(j).stringa6 = 'B') THEN
                                        isElaboraOk:='Y';
                                    END IF;
                              ELSIF inGruppoReport = TI_LIQ_IVA_ISTSMSI THEN
                                    IF  (gsezio_ven_tab(j).stringa3 = 'I' AND
                                         gsezio_ven_tab(j).stringa4 = 'Y' And
                                         gsezio_ven_tab(j).stringa6 = 'B') THEN
                                        isElaboraOk:='Y';
                                    END IF;
                              Elsif inGruppoReport = TI_LIQ_IVA_ISTSNR Then
                                    IF  (gsezio_ven_tab(j).stringa3 = 'I' And    -- istituzionale
                                         gsezio_ven_tab(j).stringa6 = 'S' And    -- ti_bene_servizio
                                        (gsezio_ven_tab(j).stringa5 = 'Y' Or     -- intra_ue
                                         gsezio_ven_tab(j).stringa4 = 'Y' Or     -- san marino senza iva
                                         gsezio_ven_tab(j).stringa7 = 'Y')) Then -- extra_ue
                                        isElaboraOk:='Y';
                                    END IF;
                              ELSIF inGruppoReport = TI_LIQ_IVA_ISTSPLIT THEN
                                    IF  (gsezio_ven_tab(j).stringa3 = 'I' And    -- istituzionale
                                         gsezio_ven_tab(j).stringa8 = 'Y')  Then -- split payment
                                        isElaboraOk:='Y';
                                    END IF;
                              END IF;
                              --aCodiceSezionale:=gsezio_ven_tab(j).stringa1;
                              --isElaboraOk:='Y';

                           END IF;

                           IF isElaboraOk = 'Y' THEN

                              CNRCTB260.calcolaLiquidazione(repID,
                                                            aCdCdsOrigine,
                                                            aCdUoOrigine,
                                                            aEsercizio,
                                                            aCodiceSezionale,
                                                            aDataInizio,
                                                            aDataFine,
                                                            aTipoRegistro,
                                                            aTipoReport,
                                                            i,
                                                            aGruppoStm,
                                                            aSottoGruppoStm,
                                                            aDescrizioneGruppoStm,
                                                            gparametri_tab(5).stringa1,
                                                            gparametri_tab(6).stringa1,
                                                            aEsercizioReale,
                                                            inGruppoReport);

                           END IF;

                        END LOOP;

                     END IF;

                  END LOOP;

               END;

            END IF;

      -------------------------------------------------------------------------------------------
      -- Scrittura su LIQUIDAZIONE_IVA dei dati di liquidazione IVA calcolati

      ELSIF aPasso = 3 THEN

            CNRCTB260.scritturaLiquidazione(repID,
                                            aCdCdsOrigine,
                                            aEsercizio,
                                            aCdUoOrigine,
                                            aDataInizio,
                                            aDataFine,
                                            aTipoRegistro,
                                            aTipoReport,
                                            aGruppoStm,
                                            aSottoGruppoStm,
                                            aDescrizioneGruppoStm,
                                            inGruppoReport);

      END IF;

   END LOOP;

END calcolaLiquidazione;


-- =================================================================================================
-- Aggiornamento dati per Liquidazione definitiva
-- =================================================================================================
PROCEDURE liquidazioneDefinitiva
   (
    aCdCdsOrigine VARCHAR2,
    aCdUoOrigine VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aTipoStampa VARCHAR2,
    aTipoRegistro VARCHAR2,
    aTipoReport VARCHAR2,
    aRistampa VARCHAR2,
    repID INTEGER,
    id_utente VARCHAR2,
    inGruppoReport VARCHAR2,
    aTipoReportStato VARCHAR2
   ) IS
   aPasso NUMBER;
   aNumeroSezionali INTEGER;
   aCodiceSezionale VARCHAR2(10);
   aTipoRegistroAltro CHAR(1);

   i BINARY_INTEGER;
   j BINARY_INTEGER;

BEGIN
   -------------------------------------------------------------------------------------------------
   -- Ciclo per l'esecuzione delle query di valorizzazione dei campi per l'aggiornamento della
   -- liquidazione definitiva

   FOR aPasso IN 1 .. 3

   LOOP

      ----------------------------------------------------------------------------------------------
      -- Crea record liquidazione definitiva

      IF    aPasso = 1 THEN

            CNRCTB260.insRecLiquidazioneDef(repID,
                                            aCdCdsOrigine,
                                            aCdUoOrigine,
                                            aEsercizio,
                                            aDataInizio,
                                            aDataFine,
                                            id_utente,
                                            inGruppoReport);

      ----------------------------------------------------------------------------------------------
      -- Aggiornamento fatture a esigibilitא differita su tabella REPORT_DETTAGLIO

      ELSIF aPasso = 2 THEN

            BEGIN

               FOR i IN 1 .. 2

               LOOP

                  IF i = 1 THEN
                     aNumeroSezionali:=gsezio_acq_tab.COUNT;
                  ELSE
                     aNumeroSezionali:=gsezio_ven_tab.COUNT;
                  END IF;

                  IF aNumeroSezionali > 0 THEN

                     FOR j IN 1 .. aNumeroSezionali

                     LOOP

                         IF i = 1 THEN
                            aCodiceSezionale:=gsezio_acq_tab(j).stringa1;
                            aTipoRegistroAltro:='A';
                         ELSE
                            aCodiceSezionale:=gsezio_ven_tab(j).stringa1;
                            aTipoRegistroAltro:='V';
                         END IF;

                         If (i = 1 And
                             (
                             (inGruppoReport = 'C' And gsezio_acq_tab(j).stringa3 = 'C')
                             Or
                             (inGruppoReport = 'I' And gsezio_acq_tab(j).stringa3 = 'I' And gsezio_acq_tab(j).stringa5 = 'Y' And gsezio_acq_tab(j).stringa6 = 'B')
                             Or
                             (inGruppoReport = 'S' And gsezio_acq_tab(j).stringa3 = 'I' And gsezio_acq_tab(j).stringa4 = 'Y' And gsezio_acq_tab(j).stringa6 = 'B')
                              Or
                             -- Servizi non residenti ,istituzionale ,(intra_ue o extra_ue),servizi anche per i servizi san marino senza iva
                             (inGruppoReport = 'X' And gsezio_acq_tab(j).stringa3 = 'I' And
                             (gsezio_acq_tab(j).stringa5 = 'Y' Or gsezio_acq_tab(j).stringa7 = 'Y' Or gsezio_acq_tab(j).stringa4 = 'Y') And gsezio_acq_tab(j).stringa6 = 'S')
                             )
                              Or
                             -- Servizi istituzionale split payment
                             (inGruppoReport = 'P' And gsezio_acq_tab(j).stringa3 = 'I' And
                             gsezio_acq_tab(j).stringa8 = 'Y')
                            )
                            Or
                            (i = 2 And
                             (
                             (inGruppoReport = 'C' And gsezio_ven_tab(j).stringa3 = 'C')
                             Or
                             (inGruppoReport = 'I' And gsezio_ven_tab(j).stringa3 = 'I' And gsezio_ven_tab(j).stringa5 = 'Y' And gsezio_ven_tab(j).stringa6 = 'B')
                             Or
                             (inGruppoReport = 'S' And gsezio_ven_tab(j).stringa3 = 'I' And gsezio_ven_tab(j).stringa4 = 'Y' And gsezio_ven_tab(j).stringa6 = 'B')
                             Or
                             -- Servizi non residenti ,istituzionale ,(intra_ue o extra_ue),servizi  anche per i servizi san marino senza iva
                             (inGruppoReport = 'X' And gsezio_ven_tab(j).stringa3 = 'I' And
                             (gsezio_ven_tab(j).stringa5 = 'Y' Or gsezio_ven_tab(j).stringa7 = 'Y' Or gsezio_ven_tab(j).stringa4 = 'Y') And gsezio_ven_tab(j).stringa6 = 'S')
                             )
                              Or
                             -- Servizi istituzionale split payment
                             (inGruppoReport = 'P' And gsezio_ven_tab(j).stringa3 = 'I' And
                             gsezio_ven_tab(j).stringa8 = 'Y')
                            )  Then

                                 CNRCTB260.insDettaglioPerLiquidazione(repID,
                                                                       aCdCdsOrigine,
                                                                       aCdUoOrigine,
                                                                       aEsercizio,
                                                                       aCodiceSezionale,
                                                                       aDataInizio,
                                                                       aDataFine,
                                                                       aTipoRegistroAltro,
                                                                       id_utente,
                                                                       inGruppoReport);
                         End If;

                     END LOOP;

                  END IF;

               END LOOP;

            END;

      ---------------------------------------------------------------------------------------------
      -- Inserimento nella tabella REPORT STATO di una riga (solo per liquidazione definitiva)

      ELSIF aPasso = 3 THEN

            CNRCTB255.inserisciInReportStato(aCdCdsOrigine,
                                             aCdUoOrigine,
                                             aEsercizio,
                                             aCdTipoSezionale,
                                             aDataInizio,
                                             aDataFine,
                                             aTipoRegistro,
                                             inGruppoReport,
                                             aTipoReportStato,
                                             id_utente);

      END IF;

   END LOOP;

END liquidazioneDefinitiva;

-- =================================================================================================
-- Memorizzazione parametri di uso della procedura di stampa
-- =================================================================================================
PROCEDURE valorizzaParametri
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inDataInizio DATE,
    inDataFine DATE,
    inTipoStampa VARCHAR2,
    inTipoRegistro VARCHAR2,
    inGruppoReport VARCHAR2
   ) IS
   i BINARY_INTEGER;
   j BINARY_INTEGER;
   lerrMsgNotFound VARCHAR2(200);
   ltrovatiRecord INTEGER;
   ltrovatiRecordEsig INTEGER;
   aStatement VARCHAR2(2000);
   aCdAnag ANAGRAFICO.cd_anag%TYPE;

   aRecTipoSezionale TIPO_SEZIONALE%ROWTYPE;
   aRecAnagrafico ANAGRAFICO%ROWTYPE;

   gen_cv GenericCurTyp;
   UOENTE unita_organizzativa%rowtype:= CNRCTB020.GETUOENTE(inEsercizio);
BEGIN
   -------------------------------------------------------------------------------------------------
   -- Azzeramento tabella generale dei parametri

   gparametri_tab.DELETE;
   gsezio_acq_tab.DELETE;
   gsezio_ven_tab.DELETE;
   gtrimestri_tab.DELETE;
   gmesi_tab.DELETE;

   -------------------------------------------------------------------------------------------------
   -- Riempimento della tabella mesi e trimestri

   CalcolaMesiTrimestri;

   ------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri generali

   FOR i IN 1 .. 8

   LOOP

      BEGIN

         -- Memorizzazione codice IVA di riferimento per righe di tipo istituzionale per fatturazione
         -- passiva promiscua. NON RICHIESTO ALLO STATO ATTUALE

         IF    i = 1 THEN

               ----------------------------------------------------------------------
               -- Tabella gparametri_tab
               -- riga 1 = codice IVA istituzionale per promiscuo
               -- riga 1 stringa 1 = CODICE_IVA_PROMISCUO_ISTITUZIONALE = NULL
               -- riga 1 stringa 2 = DESCR_CODICE_IVA_PROMISCUO_ISTITUZIONALE = NULL
               -- riga 1 stringa 3 = VALORE = NULL
               -- riga 1 stringa 4 = NULL
               ----------------------------------------------------------------------

               lerrMsgNotFound:='Identificativo codice IVA istituzionale per promiscuo non trovato';
--
--             SELECT A.VAL01 INTO gparametri_tab(i).stringa1
--             FROM   CONFIGURAZIONE_CNR A
--             WHERE  A.CD_CHIAVE_PRIMARIA = 'CODICE_IVA_PROMISCUO_ISTITUZIONALE';
--
--
--             SELECT A.VAL01 INTO gparametri_tab(i).stringa2
--             FROM   CONFIGURAZIONE_CNR A
--             WHERE  A.CD_CHIAVE_SECONDARIA = 'DESCR_CODICE_IVA_PROMISCUO_ISTITUZIONALE';

               gparametri_tab(i).stringa1:='';
               gparametri_tab(i).stringa2:='';
               gparametri_tab(i).stringa3:=gparametri_tab(i).stringa1;
               gparametri_tab(i).stringa4:=gparametri_tab(i).stringa2;

         -- Memorizzazione identificativo della moneta ITALIA per la sua esclusione in sede di
         -- stampa dei registri

         ELSIF i = 2 THEN

               ----------------------------------------------------------------------
               -- Tabella gparametri_tab
               -- riga 2 = Identificativo moneta ITALIA
               -- riga 2 stringa 1 = 'EURO'
               ----------------------------------------------------------------------

               lerrMsgNotFound:='Identificativo moneta ITALIA non trovato';

               gparametri_tab(i).stringa1:= 'EURO';

         -- Determinazione dei riferimenti ai sezionali (VENDITE)

         ELSIF i = 3 THEN
               IF (inTipoRegistro = 'V' OR inTipoRegistro = '*') THEN
                  j:=0;
                  lerrMsgNotFound:='Riferimenti ai sezionali vendita in stampa non trovati';

                  -- Esame del parametro in input e memorizzazione dei registri in input
                  -- Estrazione di tutti i sezionali di interesse

                  aStatement:=componiStatementSezionale(inCdCdsOrigine,
                                                        inCdUoOrigine,
                                                        inEsercizio,
                                                        inCdTipoSezionale,
                                                        inTipoStampa,
                                                        'V',
                                                        inGruppoReport);

                  BEGIN

                     OPEN gen_cv FOR
                          aStatement;

                     LOOP

                        FETCH gen_cv INTO
                              aRecTipoSezionale.cd_tipo_sezionale,
                              aRecTipoSezionale.ds_tipo_sezionale,
                              aRecTipoSezionale.ti_istituz_commerc,
                              aRecTipoSezionale.fl_san_marino_senza_iva,
                              aRecTipoSezionale.fl_intra_ue,
                              aRecTipoSezionale.ti_bene_servizio,
                              aRecTipoSezionale.fl_extra_ue,
                              aRecTipoSezionale.fl_split_payment;

                        EXIT WHEN gen_cv%NOTFOUND;

                        ltrovatiRecord:=0;
                        ltrovatiRecordEsig:=0;

                        -- Memorizzazione della descrizione del sezionale di ingresso.

                        IF inCdTipoSezionale = aRecTipoSezionale.cd_tipo_sezionale THEN
                           gparametri_tab(i).stringa1:=NVL(aRecTipoSezionale.ds_tipo_sezionale,' ');
                        ELSE
                           gparametri_tab(i).stringa1:='TUTTI I SEZIONALI';
                        END IF;

                        -- Verifica che per quel sezionale esistano record in stampa si esclude l'elaborazione
                        -- della liquidazione di massa e la gestione delle liquidazioni al centro

                        IF (
                              (inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA OR
                               inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA_PRV OR
                               inTipoStampa = TI_ELAB_LIQUIDAZIONE OR
                               inTipoStampa = TI_ELAB_LIQUIDAZIONE_DEF
                              ) AND
                             inCdCdsOrigine = UOENTE.cd_unita_padre
                           ) THEN

                           ltrovatiRecord:=0;
                           ltrovatiRecordEsig:=0;

                        ELSE

                           BEGIN

                              SELECT COUNT(*) INTO ltrovatiRecord
                              FROM   DUAL
                              WHERE  EXISTS
                                        (SELECT 1
                                         FROM   V_REGISTRO_IVA_VENDITE
                                         WHERE  cd_cds_origine = inCdCdsOrigine AND
                                                cd_uo_origine = inCdUoOrigine AND
                                                esercizio = inEsercizio AND
                                                cd_tipo_sezionale = aRecTipoSezionale.cd_tipo_sezionale  AND
                                                (data_emissione BETWEEN inDataInizio AND inDataFine));

                              IF (inTipoStampa = 'DIFFERITA' OR
                                  inTipoStampa = 'LIQUIDAZIONE' OR
                                  inTipoStampa = 'LIQUIDAZIONE_DEF') THEN

                                 SELECT COUNT(*) INTO ltrovatiRecordEsig
                                 FROM   DUAL
                                 WHERE  EXISTS
                                           (SELECT 1
                                            FROM   V_LIQUID_IVA_VENDITE_ESIG
                                            WHERE  cd_cds_origine = inCdCdsOrigine AND
                                                   cd_uo_origine = inCdUoOrigine AND
                                                   esercizio = inEsercizio AND
                                                   cd_tipo_sezionale = aRecTipoSezionale.cd_tipo_sezionale AND
                                                   (
                                                       (data_esigibilita IS NULL AND
                                                        data_emissione <= inDataFine)
                                                    OR
                                                       (data_esigibilita IS NOT NULL)
                                                   )
                                            );

                              END IF;

                           END;

                        END IF;

                        -- Si memorizzano i sezionali da elaborare sulla matrice dei sezionali di vendita alla riga J°
                        -- Si memorizza il fatto che il sezionale contenga o meno valori contabili

                        j:=j + 1;
                        gsezio_ven_tab(j).stringa1:=aRecTipoSezionale.cd_tipo_sezionale;
                        gsezio_ven_tab(j).stringa2:=aRecTipoSezionale.ds_tipo_sezionale;
                        gsezio_ven_tab(j).stringa3:=aRecTipoSezionale.ti_istituz_commerc;
                        gsezio_ven_tab(j).stringa4:=aRecTipoSezionale.fl_san_marino_senza_iva;
                        gsezio_ven_tab(j).stringa5:=aRecTipoSezionale.fl_intra_ue;
                        gsezio_ven_tab(j).stringa6:=aRecTipoSezionale.ti_bene_servizio;
                        -- RP
                        gsezio_ven_tab(j).stringa7:=aRecTipoSezionale.fl_extra_ue;
                        gsezio_ven_tab(j).stringa8:=aRecTipoSezionale.fl_split_payment;
                        gsezio_ven_tab(j).intero:=0;

                        IF (ltrovatiRecord + ltrovatiRecordEsig) > 0 THEN
                           gsezio_ven_tab(j).intero:=1;
                        END IF;

                     END LOOP;

                     CLOSE gen_cv;

                  END;

               END IF;

         -- Determinazione dei riferimenti ai sezionali (ACQUISTI)

         ELSIF i = 4 THEN

               IF (inTipoRegistro = 'A' OR inTipoRegistro = '*') THEN
                  j:=0;
                  lerrMsgNotFound:='Riferimenti ai sezionali acquisti in stampa non trovati';

                  -- Esame del parametro in input e memorizzazione dei registri in input
                  -- Estrazione di tutti i sezionali di interesse

                  aStatement:=componiStatementSezionale(inCdCdsOrigine,
                                                        inCdUoOrigine,
                                                        inEsercizio,
                                                        inCdTipoSezionale,
                                                        inTipoStampa,
                                                        'A',
                                                        inGruppoReport);
                  BEGIN

                     OPEN gen_cv FOR
                          aStatement;

                     LOOP

                        FETCH gen_cv INTO
                              aRecTipoSezionale.cd_tipo_sezionale,
                              aRecTipoSezionale.ds_tipo_sezionale,
                              aRecTipoSezionale.ti_istituz_commerc,
                              aRecTipoSezionale.fl_san_marino_senza_iva,
                              aRecTipoSezionale.fl_intra_ue,
                              aRecTipoSezionale.ti_bene_servizio,
                              aRecTipoSezionale.fl_extra_ue,
                              aRecTipoSezionale.fl_split_payment;

                        EXIT WHEN gen_cv%NOTFOUND;

                        ltrovatiRecord:=0;
                        ltrovatiRecordEsig:=0;

                        -- Memorizzazione della descrizione del sezionale di ingresso.

                        IF inCdTipoSezionale = aRecTipoSezionale.cd_tipo_sezionale THEN
                           gparametri_tab(i).stringa1:=NVL(aRecTipoSezionale.ds_tipo_sezionale,' ');
                        ELSE
                           gparametri_tab(i).stringa1:='TUTTI I SEZIONALI';
                        END IF;

                        -- Verifica che per quel sezionale esistano record in stampa si esclude l'elaborazione
                        -- della liquidazione di massa e la gestione delle liquidazioni al centro

                        IF (
                              (inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA OR
                               inTipoStampa = TI_ELAB_LIQUIDAZIONE_MASSA_PRV OR
                               inTipoStampa = TI_ELAB_LIQUIDAZIONE OR
                               inTipoStampa = TI_ELAB_LIQUIDAZIONE_DEF
                              ) AND
                             inCdCdsOrigine = UOENTE.cd_unita_padre
                           ) THEN

                           ltrovatiRecord:=0;
                           ltrovatiRecordEsig:=0;

                        ELSE

                           BEGIN

                              SELECT COUNT(*) INTO ltrovatiRecord
                              FROM   DUAL
                              WHERE  EXISTS
                                        (SELECT 1
                                         FROM   V_REGISTRO_IVA_ACQUISTI
                                         WHERE  cd_cds_origine = inCdCdsOrigine AND
                                                cd_uo_origine = inCdUoOrigine AND
                                                esercizio = inEsercizio AND
                                                cd_tipo_sezionale = aRecTipoSezionale.cd_tipo_sezionale  AND
                                                (data_registrazione BETWEEN inDataInizio AND inDataFine));

                           END;
                              IF (inTipoStampa = 'DIFFERITA' OR
                                  inTipoStampa = 'LIQUIDAZIONE' OR
                                  inTipoStampa = 'LIQUIDAZIONE_DEF') THEN

                                 SELECT COUNT(*) INTO ltrovatiRecordEsig
                                 FROM   DUAL
                                 WHERE  EXISTS
                                           (SELECT 1
                                            FROM   V_LIQUID_IVA_ACQUISTI_ESIG
                                            WHERE  cd_cds_origine = inCdCdsOrigine AND
                                                   cd_uo_origine = inCdUoOrigine AND
                                                   esercizio = inEsercizio AND
                                                   cd_tipo_sezionale = aRecTipoSezionale.cd_tipo_sezionale AND
                                                   (
                                                       (data_esigibilita IS NULL AND
                                                        data_emissione <= inDataFine)
                                                    OR
                                                       (data_esigibilita IS NOT NULL)
                                                   )
                                            );
                              END IF;

                       END IF;

                        -- Si memorizzano i sezionali da elaborare sulla matrice dei sezionali di vendita alla riga J°
                        -- Si memorizza il fatto che il sezionale contenga o meno valori contabili

                        j:=j + 1;
                        gsezio_acq_tab(j).stringa1:=aRecTipoSezionale.cd_tipo_sezionale;
                        gsezio_acq_tab(j).stringa2:=aRecTipoSezionale.ds_tipo_sezionale;
                        gsezio_acq_tab(j).stringa3:=aRecTipoSezionale.ti_istituz_commerc;
                        gsezio_acq_tab(j).stringa4:=aRecTipoSezionale.fl_san_marino_senza_iva;
                        gsezio_acq_tab(j).stringa5:=aRecTipoSezionale.fl_intra_ue;
                        gsezio_acq_tab(j).stringa6:=aRecTipoSezionale.ti_bene_servizio;
                        -- RP
                        gsezio_acq_tab(j).stringa7:=aRecTipoSezionale.fl_extra_ue;
                        gsezio_acq_tab(j).stringa8:=aRecTipoSezionale.fl_split_payment;
                        gsezio_acq_tab(j).intero:=0;

                        IF (ltrovatiRecord + ltrovatiRecordEsig) > 0 THEN
                           gsezio_acq_tab(j).intero:=1;
                        END IF;

                     END LOOP;

                     CLOSE gen_cv;

                  END;

               END IF;

         -- Indicatore di esercizio di riferimento in Euro o meno

         ELSIF i = 5 THEN

               gparametri_tab(i).stringa1:='Y';

         -- Indicatore di prevista gestione del prorata

         ELSIF i = 6 THEN

               lerrMsgNotFound:='Identificativo attivazione prorata non trovato';

               gparametri_tab(i).stringa1:=CNRCTB015.getVal01PerChiave(inEsercizio,
                                                                       K_PRIMA_GSTIVA,
                                                                       K_SECONDA_PRORATA_GSTIVA);

         -- Decodifica unitא organizzativa

         ELSIF i = 7 THEN

               lerrMsgNotFound:='Identificativo nome unita organizzativa non trovato';

               SELECT A.ds_unita_organizzativa INTO gparametri_tab(i).stringa1
               FROM   UNITA_ORGANIZZATIVA A
               WHERE  A.cd_unita_organizzativa = inCdUoOrigine;

         -- Recupero dati dell'anagrafico ente per intestazione

         ELSIF i = 8 THEN

               lerrMsgNotFound:='Identificativo anagrafico ente non trovato';

               aCdAnag:=CNRCTB015.getIm01PerChiave(CNRCTB080.CODICE_ANAG_ENTE_KEY1,
                                                   CNRCTB080.CODICE_ANAG_ENTE_KEY2);

               aRecAnagrafico:=CNRCTB080.getAnagDaCdAnag(aCdAnag);
               gparametri_tab(i).stringa1:=aRecAnagrafico.ragione_sociale;
               gparametri_tab(i).stringa2:=aRecAnagrafico.partita_iva;

         END IF;

      EXCEPTION
         WHEN NO_DATA_FOUND Then
              IBMERR001.RAISE_ERR_GENERICO (lerrMsgNotFound);
      END;

   END LOOP;

END valorizzaParametri;

-- =================================================================================================
-- Composizione statement per selezione su SEZIONALE JOIN TIPO_SEZIONALE
-- =================================================================================================
FUNCTION componiStatementSezionale
   (
    inCdCdsOrigine VARCHAR2,
    inCdUoOrigine VARCHAR2,
    inEsercizio NUMBER,
    inCdTipoSezionale VARCHAR2,
    inTipoStampa VARCHAR2,
    aTipoRegistro VARCHAR2,
    inGruppoReport VARCHAR2
   ) RETURN VARCHAR2 IS
   aStatement VARCHAR2(2000);

BEGIN
   IF inCdTipoSezionale = '*' THEN

      -- E' richiesta l'elaborazione per tutti i sezionali

      aStatement:='SELECT DISTINCT A.cd_tipo_sezionale, B.ds_tipo_sezionale, B.ti_istituz_commerc, ' ||
                  'B.fl_san_marino_senza_iva, B.fl_intra_ue, B.ti_bene_servizio, b.fl_extra_ue, ' ||
                  'b.fl_split_payment ' ||
                  'FROM   SEZIONALE A, TIPO_SEZIONALE B ' ||
                  'WHERE  A.cd_cds = ' || '''' || inCdCdsOrigine || '''' || ' AND ' ||
                         'A.cd_unita_organizzativa = ' || '''' || inCdUoOrigine || '''' || ' AND ' ||
                         'A.esercizio = ' || inEsercizio  || ' AND ' ||
                         'B.cd_tipo_sezionale = A.cd_tipo_sezionale ' || ' AND ' ||
                         'B.ti_acquisti_vendite = ' || '''' || aTipoRegistro || '''';

      -- Si aggiunge il filtro sul gruppo sezionali in elaborazione in caso di riepilogativi per UO e liquidazione

      IF (inTipoStampa = TI_ELAB_RIEPILOGATIVO_UO OR
          inTipoStampa = TI_ELAB_LIQUIDAZIONE OR
          inTipoStampa = TI_ELAB_LIQUIDAZIONE_DEF) THEN
         IF    inGruppoReport = 'C' THEN
               aStatement:=aStatement || ' AND ' ||
                           'B.ti_istituz_commerc = ' || '''C''';
         ELSIF inGruppoReport = 'I' THEN
               aStatement:=aStatement || ' AND ' ||
                           'B.ti_istituz_commerc = ' || '''I''' || ' AND ' ||
                           'B.fl_intra_ue = ' || '''Y''' || ' AND ' ||
                           'B.ti_bene_servizio = ' || '''B''';
         ELSIF inGruppoReport = 'S' THEN
               aStatement:=aStatement || ' AND ' ||
                           'B.ti_istituz_commerc = ' || '''I''' || ' AND ' ||
                           'B.fl_san_marino_senza_iva = ' || '''Y''' || ' AND ' ||
                           'B.ti_bene_servizio = ' || '''B''';
         Elsif inGruppoReport = 'X' THEN
               aStatement:=aStatement || ' AND ' ||
                           'B.ti_istituz_commerc = ' || '''I''' || ' AND ' ||
                           '(B.fl_intra_ue = ' || '''Y''' || ' OR ' ||
                           ' B.fl_san_marino_senza_iva = ' || '''Y''' || ' OR ' ||
                           ' B.fl_extra_ue = ' || '''Y''' || ') AND ' ||
                           'B.ti_bene_servizio = ' || '''S''';
         Elsif inGruppoReport = 'P' THEN
               aStatement:=aStatement || ' AND ' ||
                           'B.ti_istituz_commerc = ' || '''I''' || ' AND ' ||
                           'B.fl_split_payment = ' || '''Y''';
         END IF;
      END IF;

      -- Si estraggono tutti i sezionali in caso di liquidazione di massa

      IF inTipoStampa IN (TI_ELAB_LIQUIDAZIONE_MASSA, TI_ELAB_LIQUIDAZIONE_MASSA_PRV) THEN
         aStatement:=aStatement || ' AND ' ||
                     '( ' ||
                          'B.ti_istituz_commerc = ' || '''C''' || ' OR ' ||
                            '(' ||
                                'B.ti_istituz_commerc = ' || '''I''' || ' AND ' ||
                                'B.fl_intra_ue = ' || '''Y''' || ' AND ' ||
                                'B.ti_bene_servizio = ' || '''B''' ||
                            ')' || ' OR ' ||
                            '(' ||
                                'B.ti_istituz_commerc = ' || '''I''' || ' AND ' ||
                                'B.fl_san_marino_senza_iva = ' || '''Y''' || ' AND ' ||
                                'B.ti_bene_servizio = ' || '''B''' ||
                            ')' || ' OR ' ||
                            '(' ||
                                'B.ti_istituz_commerc = ' || '''I''' || ' AND ' ||
                                '(B.fl_intra_ue = ' || '''Y''' || ' OR B.fl_extra_ue = ' || '''Y''' || ' OR B.fl_san_marino_senza_iva = ' || '''Y''' || ') AND ' ||
                                'B.ti_bene_servizio = ' || '''S''' ||
                            ')' || ' OR ' ||
                            '(' ||
                                'B.ti_istituz_commerc = ' || '''I''' || ' AND ' ||
                                'B.fl_split_payment = ' || '''Y'''||
                            ')' ||
                     ')';
      END IF;

      -- Si aggiunge il filtro sui soli registri commerciali in caso di stampa iva differita

      IF inTipoStampa = TI_ELAB_IVA_DIFFERITA THEN
         aStatement:=aStatement || ' AND ' ||
                     'B.ti_istituz_commerc = ' || '''C''';
      END IF;

   ELSE
      -- Si processa un singolo sezionale

      aStatement:='SELECT DISTINCT A.cd_tipo_sezionale, B.ds_tipo_sezionale, B.ti_istituz_commerc, ' ||
                  'B.fl_san_marino_senza_iva, B.fl_intra_ue, B.ti_bene_servizio, b.fl_extra_ue, ' ||
                  'b.fl_split_payment ' ||
                  'FROM   SEZIONALE A, TIPO_SEZIONALE B ' ||
                  'WHERE  A.cd_cds = ' || '''' || inCdCdsOrigine || '''' || ' AND ' ||
                         'A.cd_unita_organizzativa = ' || '''' || inCdUoOrigine || '''' || ' AND ' ||
                         'A.esercizio = ' || inEsercizio  || ' AND ' ||
                         'A.cd_tipo_sezionale = ' || '''' || inCdTipoSezionale || '''' || ' AND ' ||
                         'B.cd_tipo_sezionale = A.cd_tipo_sezionale ' || ' AND ' ||
                         'B.ti_acquisti_vendite = ' || '''' || aTipoRegistro || '''';

   END IF;

   RETURN aStatement;

END componiStatementSezionale;

-- =================================================================================================
-- Aggiornamento dello stato del registro da B ad C
-- =================================================================================================
PROCEDURE conferma_registro
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER,
    aCodiceSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aUtente VARCHAR2
   ) IS
   aTipoAzione CHAR(1);

BEGIN

   aTipoAzione:='C';

   CNRCTB265.confermaAnnullaRegistro(aCdCds,
                                     aCdUo,
                                     aEsercizio,
                                     aCodiceSezionale,
                                     aDataInizio,
                                     aDataFine,
                                     aUtente,
                                     aTipoAzione);

END CONFERMA_REGISTRO;

-- =================================================================================================
-- Annullamento della stampa definitiva di un registro IVA
-- =================================================================================================
PROCEDURE annulla_registro
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER,
    aCodiceSezionale VARCHAR2,
    aDataInizio DATE,
    aDataFine DATE,
    aUtente VARCHAR2
   ) IS
   aTipoAzione CHAR(1);

BEGIN

   aTipoAzione:='A';

   CNRCTB265.confermaAnnullaRegistro(aCdCds,
                                     aCdUo,
                                     aEsercizio,
                                     aCodiceSezionale,
                                     aDataInizio,
                                     aDataFine,
                                     aUtente,
                                     aTipoAzione);

END annulla_registro;

-- =================================================================================================
-- Costruisci array mesi e trimestri
-- =================================================================================================
PROCEDURE CalcolaMesiTrimestri
   AS
   idMese VARCHAR2(20);
   idTrimestre VARCHAR2(20);
   i BINARY_INTEGER;

BEGIN

   FOR i IN 1 .. 12

   LOOP

      BEGIN

         IF    i = 1 THEN
               idMese:='GENNAIO';
               idTrimestre:='I TRIMESTRE';
         ELSIF i = 2 THEN
               idMese:='FEBBRAIO';
               idTrimestre:='I TRIMESTRE';
         ELSIF i = 3 THEN
               idMese:='MARZO';
               idTrimestre:='I TRIMESTRE';
         ELSIF i = 4 THEN
               idMese:='APRILE';
               idTrimestre:='II TRIMESTRE';
         ELSIF i = 5 THEN
               idMese:='MAGGIO';
               idTrimestre:='II TRIMESTRE';
         ELSIF i = 6 THEN
               idMese:='GIUGNO';
               idTrimestre:='II TRIMESTRE';
         ELSIF i = 7 THEN
               idMese:='LUGLIO';
               idTrimestre:='III TRIMESTRE';
         ELSIF i = 8 THEN
               idMese:='AGOSTO';
               idTrimestre:='III TRIMESTRE';
         ELSIF i = 9 THEN
               idMese:='SETTEMBRE';
               idTrimestre:='III TRIMESTRE';
         ELSIF i = 10 THEN
               idMese:='OTTOBRE';
               idTrimestre:='IV TRIMESTRE';
         ELSIF i = 11 THEN
               idMese:='NOVEMBRE';
               idTrimestre:='IV TRIMESTRE';
         ELSIF i = 12 THEN
               idMese:='DICEMBRE';
               idTrimestre:='IV TRIMESTRE';
         END IF;

         gmesi_tab(i).stringa1:=idMese;
         gtrimestri_tab(i).stringa1:=idTrimestre;

      END;

   END LOOP;

END CalcolaMesiTrimestri;

END CNRCTB250; -- PACKAGE END;
