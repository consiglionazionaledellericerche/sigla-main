--------------------------------------------------------
--  DDL for Package Body CNRCTB610
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB610" AS

--=============================================================================
--					INIZIO PACKAGE BODY
--=============================================================================

PROCEDURE liquidazMassivaMinicarriere
   (
    pgCall    NUMBER,
    cdUO      VARCHAR2,
    cdCds     VARCHAR2,
    esercizio NUMBER,
    aUtente   VARCHAR2
   ) IS

   cd_trattamento_inail VARCHAR2(100);
   cd_voce_iva VARCHAR2(100);
   cd_regione_irap VARCHAR2(10);
   aRecCompenso COMPENSO%ROWTYPE;
   aRecRataMinicarriera MINICARRIERA_RATA%ROWTYPE;
   aRecMinicarriera MINICARRIERA%ROWTYPE;
   aRecLiquidazRateMinicarriera VSX_LIQUIDAZIONE_RATE%ROWTYPE;
   gen_cur_rate_minicarriera GenericCurTyp;
   aRecScadenzaObbligazione OBBLIGAZIONE_SCADENZARIO%ROWTYPE;
   msgRata VARCHAR2(50);

BEGIN

   -- Ricerco il trattamento INAIL di default (inizializzazione, se necessaria,
   -- del campo "cd_tipologia_rischio" del compenso)
   cd_trattamento_inail := getCdTrattamentoInail();

   -- Ricerco la voce IVA di default (inizializzazione, se necessaria,
   -- del campo "cd_voce_iva" del compenso)
   cd_voce_iva := getCdVoceIva();

   -- Ricerco la regione IRAP (inizializzazione, se necessaria,
   -- del campo "cd_regione_irap" del compenso)
   cd_regione_irap := getCdRegioneIrap(cdUO);

   gPgLogCreazioneCompenso := ibmutl200.LOGSTART(tipoLog,'Liquidazione massiva minicarriere - Creazione Compenso', null, aUtente, null, null);

   BEGIN

      -- Metto lock alla numerazione del compenso e dell'obbligazione

      setLockTabelleNumerazione(cdCds, cdUO, esercizio, aUtente);

      -- Leggo le rate da processare (per ogni rata andro' a creare un compenso)

       OPEN gen_cur_rate_minicarriera FOR

            SELECT *
            FROM   VSX_LIQUIDAZIONE_RATE
            WHERE  pg_call = pgCall
            ORDER BY cd_cds,
                     cd_unita_organizzativa,
                     esercizio,
                     pg_minicarriera,
                     pg_rata;

       LOOP

          BEGIN -- SAVEPOINT

             SAVEPOINT inizioGestioneRata;

             descErrore:='';
             msgRata:='';
             flgFaseCreazione := true;

             FETCH gen_cur_rate_minicarriera INTO
                   aRecLiquidazRateMinicarriera;

             EXIT WHEN gen_cur_rate_minicarriera%NOTFOUND;

             msgRata := 'Minicarriera n.' || aRecLiquidazRateMinicarriera.pg_minicarriera ||
                        ', Rata n. ' || aRecLiquidazRateMinicarriera.pg_rata;

             -- Leggo Mincarriera

             BEGIN

                descErrore:=msgRata || '- Errore nella ricerca della Minicarriera.';

                aRecMinicarriera:=CNRCTB600.getMinicarriera(aRecLiquidazRateMinicarriera.cd_cds,
                                                            aRecLiquidazRateMinicarriera.cd_unita_organizzativa,
                                                            aRecLiquidazRateMinicarriera.esercizio,
                                                            aRecLiquidazRateMinicarriera.pg_minicarriera,
                                                            'Y');

                descErrore:='';

             END;

             -- Leggo Rata Minicarriera

             BEGIN

                descErrore:=msgRata || ' - Errore nella ricerca della Rata.';

                aRecRataMinicarriera := getRataMinicarriera(aRecLiquidazRateMinicarriera.cd_cds,
                                                            aRecLiquidazRateMinicarriera.cd_unita_organizzativa,
                                                            aRecLiquidazRateMinicarriera.esercizio,
                                                            aRecLiquidazRateMinicarriera.pg_minicarriera,
                                                            aRecLiquidazRateMinicarriera.pg_rata,
                                                            'Y');

                descErrore:='';

             END;

             BEGIN

                descErrore:=msgRata || ' - Errore nella creazione compenso.';

                -- Inizializzazione, inserimento, elaborazione del compenso da rata minicarriera

                aRecCompenso:=creaCompensoRataMinicarriera(aRecMinicarriera,
                                                           aRecRataMinicarriera,
                                                           cd_trattamento_inail,
                                                           cd_voce_iva,
                                                           cd_regione_irap,
                                                           esercizio);

                -- Rileggo il compenso appena inserito ed elaborato (conterra' gli importi correttamente calcolati)

                aRecCompenso := CNRCTB545.getCompenso(aRecCompenso.cd_cds,
                                                      aRecCompenso.cd_unita_organizzativa,
                                                      aRecCompenso.esercizio,
                                                      aRecCompenso.pg_compenso,
                                                      'Y');

                descErrore:='';

             END;

             BEGIN

                descErrore:=msgRata || ' - Errore nell''aggiornamento dei montanti.';

                -- Aggiornamento dei montanti per Compenso

                CNRCTB550.aggiornaMontanti(aRecCompenso.cd_cds,
                                           aRecCompenso.cd_unita_organizzativa,
                                           aRecCompenso.esercizio,
                                           aRecCompenso.pg_compenso,
                                           null,
                                           null,
                                           null,
                                           null);

                descErrore:='';

             END;

             -- Crea Obbligazione

             BEGIN

                descErrore:=msgRata || ' - Errore nella creazione '||cnrutil.getLabelObbligazioneMin()||'.';

                creaObbligazionePerCompenso(aRecCompenso,
                                            aRecScadenzaObbligazione,
                                            aRecLiquidazRateMinicarriera.cd_elemento_voce,
                                            aRecLiquidazRateMinicarriera.esercizio_competenza,
                                            aRecLiquidazRateMinicarriera.ti_appartenenza,
                                            aRecLiquidazRateMinicarriera.ti_gestione,
                                            aRecLiquidazRateMinicarriera.cd_voce,
                                            aRecLiquidazRateMinicarriera.cd_centro_responsabilita,
                                            aRecLiquidazRateMinicarriera.cd_linea_attivita);

                descErrore:='';

             END;

             -- Aggiorno compenso con dati obbligazione

             BEGIN

                descErrore:=msgRata || ' - Errore nell''aggiornamento del compenso con dati '||cnrutil.getLabelObbligazioneMin()||'.';

                aggiornaCompensoObbligazione(aRecCompenso, aRecScadenzaObbligazione);

                descErrore:='';

             END;

             -- Aggiornamento della rata della minicarriera con i dati del compenso appena creato

             BEGIN

                descErrore:=msgRata || ' - Errore nell''aggiornamento della rata della minicarriera con dati compenso.';

                aggiornaRataMinicarriera(aRecCompenso, aRecRataMinicarriera);

                descErrore:='';

             END;

             -- Aggiornamento del campo STATO_ASS_COMPENSO della minicarriera

             BEGIN

                descErrore:=msgRata || ' - Errore nell''aggiornamento della minicarriera con dati compenso.';

                aggiornaMinicarriera(aRecMinicarriera);

                descErrore:='';

             END;

             -- Liquidazione compenso

             BEGIN

                flgFaseCreazione:=false;

                -- Scrivo solo una volta nella tabella della testata il log relativo alla liquidazione

                if (gPgLogLiquidazioneCompenso=0) then
                   gPgLogLiquidazioneCompenso := ibmutl200.LOGSTART(tipoLog,
                                                                    'Liquidazione massiva minicarriere - Liquidazione Compenso',
                                                                    null, aUtente, null, null);

                end if;

                descErrore:=msgRata || ' - Errore durante la liquidazione del compenso n. ' || aRecCompenso.pg_compenso;

                CNRCTB560.contabilizzaCompensoCOFI(aRecCompenso.cd_cds,
                                                   aRecCompenso.cd_unita_organizzativa,
                                                   aRecCompenso.esercizio,
                                                   aRecCompenso.esercizio,
                                                   aRecCompenso.pg_compenso,
                                                   aRecCompenso.utuv);

                descErrore:='';

             END;

             pgDocAmm:=pgDocAmm+1;

             ibmutl200.logInf(gPgLogCreazioneCompenso, msgRata, 'Creato con successo il compenso n. ' ||
                              aRecCompenso.pg_compenso || ' per il creditore n. ' ||
                              aRecCompenso.cd_terzo || ' - '||cnrutil.getLabelObbligazione()||' n. ' ||
                              aRecScadenzaObbligazione.pg_obbligazione || ' data ' ||
                              aRecScadenzaObbligazione.dt_scadenza || '.', '');

             ibmutl200.logInf(gPgLogLiquidazioneCompenso, msgRata, 'Liquidato con successo il compenso n. ' ||
                              aRecCompenso.pg_compenso, getDocContCompenso(aRecCompenso));

          EXCEPTION when others THEN

                    IF (flgFaseCreazione) THEN
                       ibmutl200.logInf(gPgLogCreazioneCompenso, descErrore, SUBSTR(SQLERRM,1,200), '');
                    ELSE
                       ibmutl200.logInf(gPgLogLiquidazioneCompenso, descErrore, SUBSTR(SQLERRM,1,200), '');
                    END IF;

                    ROLLBACK TO SAVEPOINT inizioGestioneRata;


          END;   -- SAVEPOINT

       END LOOP;

       CLOSE gen_cur_rate_minicarriera;

       -- Cancello dalla vista VSX_LIQUIDAZIONE_RATE le rate per le quali sono stati generati dei compensi

       cancellaRateMinicarriere(pgCall);

       aggiornaUltimoPgDocAmm(cdCds, cdUo, esercizio, aUtente);

       -- COMMIT;

    EXCEPTION when others THEN
              ibmutl200.logInf(gPgLogCreazioneCompenso, 'Liquidazione massiva Minicarriere', SUBSTR(SQLERRM,1,200), '');

    END;

END liquidazMassivaMinicarriere;

--
-- Inizializzazione, inserimento, elaborazione del compenso da rata minicarriera
--
FUNCTION creaCompensoRataMinicarriera
   (
   		aRecMinicarriera MINICARRIERA%ROWTYPE,
   		aRecRataMinicarriera MINICARRIERA_RATA%ROWTYPE,
		cd_trattamento_inail VARCHAR2,
		cd_voce_iva VARCHAR2,
		cd_regione_irap VARCHAR2,
		esercizio NUMBER
   )RETURN COMPENSO%ROWTYPE IS

   aRecCompenso COMPENSO%ROWTYPE;
   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;
   inPgCompenso COMPENSO.pg_compenso%TYPE;
   pgComuneFiscale NUMBER;
   cdProvincia VARCHAR2(10);
   cdRegione VARCHAR2(10);
   esercizioFineRata NUMBER;
BEGIN

   aRecCompenso:=NULL;

   -- Recupero regione, provincia e comune fiscale dal terzo della minicarriera
   getTerzoMinicarriera(aRecMinicarriera.cd_terzo, pgComuneFiscale, cdProvincia, cdRegione);

   -- Inizializzazione terzo compenso
   aRecCompenso.ti_anagrafico:=aRecMinicarriera.ti_anagrafico;
   aRecCompenso.cd_terzo:=aRecMinicarriera.cd_terzo;
   aRecCompenso.nome:=aRecMinicarriera.nome;
   aRecCompenso.cognome:=aRecMinicarriera.cognome;
   aRecCompenso.ragione_sociale:=aRecMinicarriera.ragione_sociale;
   aRecCompenso.codice_fiscale:=aRecMinicarriera.codice_fiscale;
   aRecCompenso.partita_iva:=aRecMinicarriera.partita_iva;
   aRecCompenso.cd_termini_pag:=aRecMinicarriera.cd_termini_pag;
   aRecCompenso.cd_modalita_pag:=aRecMinicarriera.cd_modalita_pag;
   aRecCompenso.pg_banca:=aRecMinicarriera.pg_banca;
   aRecCompenso.cd_tipo_rapporto:=aRecMinicarriera.cd_tipo_rapporto;

   -- Inizializzazione testata compenso
   aRecCompenso.pg_compenso:=pgDocAmm;
   aRecCompenso.cd_cds:=aRecMinicarriera.cd_cds;
   aRecCompenso.cd_unita_organizzativa:=aRecMinicarriera.cd_unita_organizzativa;
   aRecCompenso.esercizio:=esercizio;
   aRecCompenso.cd_cds_origine:=aRecMinicarriera.cd_cds;
   aRecCompenso.cd_uo_origine:=aRecMinicarriera.cd_unita_organizzativa;

   aRecCompenso.dt_registrazione:=trunc(CNRCTB008.getTimestampContabile(esercizio, dataOdierna));

   aRecCompenso.ds_compenso:='COMPENSO DA RATA N. ' || aRecRataMinicarriera.pg_rata || ' - MINICARRIERA N. ' ||
                             aRecRataMinicarriera.pg_minicarriera;
   aRecCompenso.cd_trattamento:=aRecMinicarriera.cd_trattamento;
   aRecCompenso.fl_senza_calcoli:='N';
   aRecCompenso.fl_diaria:='N';
   aRecCompenso.stato_cofi:='C';
   aRecCompenso.stato_coge:='N';
   aRecCompenso.stato_coan:='N';
   aRecCompenso.ti_associato_manrev:='N';
   aRecCompenso.dt_da_competenza_coge:=aRecRataMinicarriera.dt_inizio_rata;
   aRecCompenso.dt_a_competenza_coge:=aRecRataMinicarriera.dt_fine_rata;
   aRecCompenso.stato_pagamento_fondo_eco:='N';
   aRecCompenso.im_totale_compenso:=0;
   aRecCompenso.im_lordo_percipiente:=aRecRataMinicarriera.im_rata;
   aRecCompenso.im_netto_percipiente:=0;
   aRecCompenso.im_cr_percipiente:=0;
   aRecCompenso.im_cr_ente:=0;
   aRecCompenso.quota_esente:=0;
   aRecCompenso.quota_esente_no_iva:=0;
   aRecCompenso.im_no_fiscale:=0;
   aRecCompenso.imponibile_fiscale:=0;
   aRecCompenso.imponibile_iva:=0;
   aRecCompenso.pg_comune_add:=pgComuneFiscale;
   aRecCompenso.cd_provincia_add:=cdProvincia;
   aRecCompenso.cd_regione_add:=cdRegione;
   aRecCompenso.dacr:=aRecRataMinicarriera.dacr;
   aRecCompenso.utcr:=aRecRataMinicarriera.utcr;
   aRecCompenso.duva:=aRecRataMinicarriera.duva;
   aRecCompenso.utuv:=aRecRataMinicarriera.utuv;
   aRecCompenso.pg_ver_rec:=1;
   aRecCompenso.detrazioni_personali:=0;
   aRecCompenso.detrazioni_la:=0;
   aRecCompenso.detrazione_coniuge:=0;
   aRecCompenso.detrazione_figli:=0;
   aRecCompenso.detrazione_altri:=0;
   aRecCompenso.detrazione_riduzione_cuneo:=0;
   aRecCompenso.detrazioni_personali_netto:=0;
   aRecCompenso.detrazioni_la_netto:=0;
   aRecCompenso.detrazione_coniuge_netto:=0;
   aRecCompenso.detrazione_figli_netto:=0;
   aRecCompenso.detrazione_altri_netto:=0;
   aRecCompenso.detrazione_rid_cuneo_netto := 0;
   aRecCompenso.imponibile_inail:=0;
   aRecCompenso.fl_generata_fattura:='N';
   aRecCompenso.fl_compenso_stipendi:='N';
   aRecCompenso.fl_compenso_conguaglio:='N';
   aRecCompenso.fl_compenso_minicarriera:='Y';
   aRecCompenso.aliquota_irpef_da_missione:=0;
   aRecCompenso.im_deduzione_irpef:=0;
   aRecCompenso.imponibile_fiscale_netto:=0;
   aRecCompenso.numero_giorni:=0;
   aRecCompenso.fl_escludi_qvaria_deduzione:=aRecMinicarriera.fl_escludi_qvaria_deduzione;
   aRecCompenso.fl_intera_qfissa_deduzione:='N';
   aRecCompenso.im_detrazione_personale_anag:=0;
   aRecCompenso.fl_recupero_rate:='N';
   aRecCompenso.fl_accantona_add_terr:='N';

   esercizioFineRata := TO_NUMBER(TO_CHAR(aRecRataMinicarriera.dt_fine_rata, 'YYYY'));
   if(aRecMinicarriera.fl_tassazione_separata = 'Y' and
      esercizioFineRata < aRecMinicarriera.ESERCIZIO) then
      aRecCompenso.aliquota_irpef_tassep:=aRecMinicarriera.aliquota_irpef_media;
      aRecCompenso.fl_compenso_mcarriera_tassep:=aRecMinicarriera.fl_tassazione_separata;
   else
      aRecCompenso.aliquota_irpef_tassep:=0;
      aRecCompenso.fl_compenso_mcarriera_tassep:='N';
   end if;

   BEGIN
      -- Recupero il Tipo Trattamento indicato nella minicarriera
	  aRecTipoTrattamento := CNRCTB545.getTipoTrattamento (aRecMinicarriera.cd_trattamento,
   	                                                   	   aRecMinicarriera.dt_registrazione
        												  );

      IF aRecTipoTrattamento.ti_commerciale = 'Y' THEN
         aRecCompenso.ti_istituz_commerc:='C';
      ELSE
         aRecCompenso.ti_istituz_commerc:='I';
      END IF;
   END;

   -- Inizializzazione dati liquidazione
   aRecCompenso:=CNRCTB610.inizializzaDatiLiquidazione(cd_trattamento_inail,
   													   cd_voce_iva,
                                                       cd_regione_irap,
                                                       aRecCompenso);
  -- Inserimento ed elaborazione del compenso
   CNRCTB545.insCompenso(aRecCompenso);

   CNRCTB550.elaboraCompenso(aRecCompenso.cd_Cds,
                             aRecCompenso.cd_unita_organizzativa,
                             aRecCompenso.esercizio,
                             aRecCompenso.pg_compenso,
                             NULL,
                             NULL,
                             TO_NUMBER(NULL),
                             TO_NUMBER(NULL),
                             aRecMinicarriera.cd_cds,
                             aRecMinicarriera.cd_unita_organizzativa,
                             aRecMinicarriera.esercizio,
                             aRecMinicarriera.pg_minicarriera,
                             NULL,
                             NULL,
                             TO_NUMBER(NULL),
                             TO_NUMBER(NULL)
                            );

   RETURN aRecCompenso;
END creaCompensoRataMinicarriera;

--
-- Inizializzazione della regione_irap, tipologia_rischio, voce_iva del
-- compenso a seconda del codice trattamento
--
FUNCTION inizializzaDatiLiquidazione
   (
	   		cd_trattamento_inail VARCHAR2,
			cd_voce_iva VARCHAR2,
			cd_regione_irap VARCHAR2,
			aRecCompenso COMPENSO%ROWTYPE
   )RETURN COMPENSO%ROWTYPE IS

   aRecCoriCompenso V_TIPO_TRATTAMENTO_TIPO_CORI%ROWTYPE;
   aCompenso COMPENSO%ROWTYPE;
   gen_cur_cori_trattamento GenericCurTyp;

BEGIN
	  aCompenso:=aRecCompenso;

      -- Leggo i Co.Ri.
      OPEN gen_cur_cori_trattamento FOR

           SELECT *
           FROM   V_TIPO_TRATTAMENTO_TIPO_CORI
           WHERE  cd_trattamento = aRecCompenso.cd_trattamento AND
                  dt_ini_val_tipo_cori <= aRecCompenso.dt_registrazione AND
                  dt_fin_val_tipo_cori >= aRecCompenso.dt_registrazione AND
		          (cd_classificazione_cori=CNRCTB545.isCoriIrap OR
				   cd_classificazione_cori=CNRCTB545.isCoriInail OR
                   cd_classificazione_cori=CNRCTB545.isCoriIva);

           LOOP

             FETCH gen_cur_cori_trattamento INTO
                   aRecCoriCompenso;

             EXIT WHEN gen_cur_cori_trattamento%NOTFOUND;

             IF (aRecCoriCompenso.cd_classificazione_cori = CNRCTB545.isCoriIrap) THEN
			 	aCompenso.cd_regione_irap:= cd_regione_irap;
             ELSIF aRecCoriCompenso.cd_classificazione_cori = CNRCTB545.isCoriInail THEN
			 	aCompenso.cd_tipologia_rischio:=cd_trattamento_inail;
             ELSIF aRecCoriCompenso.cd_classificazione_cori = CNRCTB545.isCoriIva THEN
			 	aCompenso.cd_voce_iva:=cd_voce_iva;
             END IF;

           END LOOP;

      CLOSE gen_cur_cori_trattamento;

    RETURN aCompenso;

END inizializzaDatiLiquidazione;

--
-- Inizializzazione, inserimento dell'obbligazione per compenso
--
PROCEDURE creaObbligazionePerCompenso
   (
  		aRecCompenso COMPENSO%ROWTYPE,
        aRecScadenzaObbligazione IN OUT OBBLIGAZIONE_SCADENZARIO%ROWTYPE,
		cd_elemento_voce VARCHAR2,
		esercizio_competenza NUMBER,
		ti_appartenenza VARCHAR2,
		ti_gestione VARCHAR2,
		cd_voce VARCHAR2,
		cd_centro_responsabilita VARCHAR2,
		cd_linea_attivita VARCHAR2
   ) IS

   aRecObbligazione OBBLIGAZIONE%ROWTYPE;
   aRecScadVoce OBBLIGAZIONE_SCAD_VOCE%ROWTYPE;
   listaDettagliObbligazione CNRCTB035.scadVoceListS;

BEGIN

   -- Inizializzo testata obbligazione
   aRecObbligazione.cd_cds:=aRecCompenso.cd_cds;
   aRecObbligazione.esercizio:=aRecCompenso.esercizio;
   aRecObbligazione.cd_tipo_documento_cont:= cdTipoDocumentoCont;
   aRecObbligazione.cd_unita_organizzativa:=aRecCompenso.cd_unita_organizzativa;
   aRecObbligazione.cd_cds_origine:=aRecCompenso.cd_cds;
   aRecObbligazione.cd_uo_origine:=aRecCompenso.cd_unita_organizzativa;
   aRecObbligazione.ti_appartenenza:='D';
   aRecObbligazione.ti_gestione:='S';
   aRecObbligazione.cd_elemento_voce:=cd_elemento_voce;
   aRecObbligazione.dt_registrazione:=trunc(CNRCTB008.getTimestampContabile(aRecCompenso.esercizio, dataOdierna));
   aRecObbligazione.ds_obbligazione:=cnrutil.getLabelObbligazione()||' per compenso';
   aRecObbligazione.cd_terzo:=aRecCompenso.cd_terzo;
   aRecObbligazione.im_obbligazione:=aRecCompenso.im_totale_compenso;
   aRecObbligazione.im_costi_anticipati:=0;
   aRecObbligazione.esercizio_competenza:=esercizio_competenza;
   aRecObbligazione.stato_obbligazione:='D';
   aRecObbligazione.fl_calcolo_automatico:='Y';
   aRecObbligazione.fl_spese_costi_altrui:='N';
   aRecObbligazione.fl_pgiro:='N';
   aRecObbligazione.riportato:='N';
   aRecObbligazione.dacr:=aRecCompenso.dacr;
   aRecObbligazione.utcr:=aRecCompenso.utcr;
   aRecObbligazione.duva:=aRecCompenso.duva;
   aRecObbligazione.utuv:=aRecCompenso.utuv;
   aRecObbligazione.pg_ver_rec:=aRecCompenso.pg_ver_rec;

   -- Inizializzo scadenza obbligazione
   aRecScadenzaObbligazione.cd_cds:=aRecCompenso.cd_cds;
   aRecScadenzaObbligazione.esercizio:=aRecCompenso.esercizio;
   aRecScadenzaObbligazione.pg_obbligazione_scadenzario:=1;
   aRecScadenzaObbligazione.dt_scadenza:=dataOdierna;
   aRecScadenzaObbligazione.ds_scadenza:='Scadenza per compenso';
   aRecScadenzaObbligazione.im_scadenza:=aRecCompenso.im_totale_compenso;
   aRecScadenzaObbligazione.im_associato_doc_amm:=aRecCompenso.im_totale_compenso;
   aRecScadenzaObbligazione.im_associato_doc_contabile:=0;
   aRecScadenzaObbligazione.dacr:=aRecCompenso.dacr;
   aRecScadenzaObbligazione.utcr:=aRecCompenso.utcr;
   aRecScadenzaObbligazione.duva:=aRecCompenso.duva;
   aRecScadenzaObbligazione.utuv:=aRecCompenso.utuv;
   aRecScadenzaObbligazione.pg_ver_rec:=aRecCompenso.pg_ver_rec;

   -- Inizializzo dettaglio scadenza obbligazione
   aRecScadVoce.cd_cds:=aRecCompenso.cd_cds;
   aRecScadVoce.esercizio:=aRecCompenso.esercizio;
   aRecScadVoce.pg_obbligazione_scadenzario:=1;
   aRecScadVoce.ti_appartenenza:=ti_appartenenza;
   aRecScadVoce.ti_gestione:=ti_gestione;
   aRecScadVoce.cd_voce:=cd_voce;
   aRecScadVoce.cd_centro_responsabilita:=cd_centro_responsabilita;
   aRecScadVoce.cd_linea_attivita:=cd_linea_attivita;
   aRecScadVoce.im_voce:=aRecCompenso.im_totale_compenso;
   aRecScadVoce.dacr:=aRecCompenso.dacr;
   aRecScadVoce.utcr:=aRecCompenso.utcr;
   aRecScadVoce.duva:=aRecCompenso.duva;
   aRecScadVoce.utuv:=aRecCompenso.utuv;
   aRecScadVoce.pg_ver_rec:=aRecCompenso.pg_ver_rec;
   listaDettagliObbligazione(1):=aRecScadVoce;

   -- Creazione obbligazione e aggiornamento saldi
   CNRCTB030.creaObbligazione(true, aRecObbligazione, aRecScadenzaObbligazione, listaDettagliObbligazione);

END creaObbligazionePerCompenso;

--
--	Lettura della rata di minicarriera per la quale generare un compenso
--
FUNCTION getRataMinicarriera
   (
     aCdCds VSX_LIQUIDAZIONE_RATE.cd_cds%TYPE,
     aCdUo VSX_LIQUIDAZIONE_RATE.cd_unita_organizzativa%TYPE,
     aEsercizio VSX_LIQUIDAZIONE_RATE.esercizio%TYPE,
     aPgMinicarriera VSX_LIQUIDAZIONE_RATE.pg_minicarriera%TYPE,
     aPgRata VSX_LIQUIDAZIONE_RATE.pg_rata%TYPE,
     eseguiLock CHAR
   ) RETURN MINICARRIERA_RATA%ROWTYPE IS
   aRecRataMinicarriera MINICARRIERA_RATA%ROWTYPE;

BEGIN

    IF eseguiLock = 'Y' THEN
       SELECT * INTO aRecRataMinicarriera
       FROM   MINICARRIERA_RATA
       WHERE  cd_cds = aCdCds AND
              cd_unita_organizzativa = aCdUo AND
              esercizio = aEsercizio AND
              pg_minicarriera = aPgMinicarriera AND
			  pg_rata = aPgRata
       FOR UPDATE NOWAIT;
   ELSE
       SELECT * INTO aRecRataMinicarriera
       FROM   MINICARRIERA_RATA
       WHERE  cd_cds = aCdCds AND
              cd_unita_organizzativa = aCdUo AND
              esercizio = aEsercizio AND
              pg_minicarriera = aPgMinicarriera AND
              pg_rata = aPgRata;
   END IF;

   RETURN aRecRataMinicarriera;

   EXCEPTION
    WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
                  ('Rata n. ' || aPgRata || 'della Minicarriera U.O. ' || aCdUo ||
                   ' numero ' || aEsercizio || '/' || aPgMinicarriera ||
                   ' non trovata');
END getRataMinicarriera;

--
--	Lettura da configurazione del Codice Trattamento INAIL
--
FUNCTION getCdTrattamentoInail RETURN VARCHAR2 IS
  cdTrattamentoInail VARCHAR2(100);
BEGIN
	BEGIN
	    SELECT val01 into cdTrattamentoInail
	    FROM   CONFIGURAZIONE_CNR
	    WHERE  esercizio = 0 AND
	           cd_unita_funzionale = '*' AND
	           cd_chiave_primaria = 'TIPO_TRATTAMENTO_SPECIALE' AND
               cd_chiave_secondaria = 'TRATTAMENTO_INAIL';

		EXCEPTION
	    WHEN no_data_found THEN
	        IBMERR001.RAISE_ERR_GENERICO('Impossibile recurerare il trattamento INAIL di default!');
	END;
	RETURN cdTrattamentoInail;

END getCdTrattamentoInail;

--
--	Lettura da configurazione del Codice Trattamento IVA
--
FUNCTION getCdVoceIva RETURN VARCHAR2 IS
  cd_voce_iva VARCHAR2(100);
BEGIN
	 BEGIN
	    SELECT val01 into cd_voce_iva
	    FROM   CONFIGURAZIONE_CNR
	    WHERE  esercizio = 0 AND
	           cd_unita_funzionale = '*' AND
	           cd_chiave_primaria = 'VOCE_IVA_SPECIALE' AND
               cd_chiave_secondaria = 'VOCE_IVA';

		EXCEPTION
	    WHEN no_data_found THEN
	        IBMERR001.RAISE_ERR_GENERICO('Impossibile recurerare la voce IVA di default!');
	END;
	RETURN cd_voce_iva;

END getCdVoceIva;

--
--	Lettura della Regione IRAP
--
FUNCTION getCdRegioneIrap
    (
	  cdUO VARCHAR2
	) RETURN VARCHAR2 IS
    cd_regione_irap VARCHAR2(10);
BEGIN
   BEGIN
	 SELECT D.CD_REGIONE into cd_regione_irap
	 FROM TERZO A,
	 	  COMUNE B,
		  PROVINCIA C,
		  REGIONE D
	 WHERE A.CD_UNITA_ORGANIZZATIVA = cdUO AND
	 	   B.PG_COMUNE = A.PG_COMUNE_SEDE AND
		   C.CD_PROVINCIA = B.CD_PROVINCIA AND
		   D.CD_REGIONE = C.CD_REGIONE;

	EXCEPTION
    WHEN no_data_found THEN
		 cd_regione_irap := '*';
   END;
   RETURN cd_regione_irap;

END getCdRegioneIrap;

--
-- Lettura del terzo della minicarriera
--
PROCEDURE getTerzoMinicarriera
   (
       cdTerzo MINICARRIERA.CD_TERZO%TYPE,
       pgComuneFiscale IN OUT NUMBER,
       cdProvincia IN OUT VARCHAR2,
       cdRegione IN OUT VARCHAR2
   ) IS
BEGIN
	BEGIN

        SELECT C.pg_comune_fiscale, NVL(F.cd_provincia,'*'), NVL(G.cd_regione,'*') INTO
		       pgComuneFiscale, cdProvincia, cdRegione
        FROM   ANAGRAFICO C,
               TERZO D,
               COMUNE E,
               PROVINCIA F,
               REGIONE G
        WHERE  D.cd_terzo = cdTerzo AND
		       D.cd_anag = C.cd_anag AND
               E.pg_comune = C.pg_comune_fiscale AND
               E.ti_italiano_estero = 'I' AND
               F.cd_provincia (+) = E.cd_provincia AND
               G.cd_regione (+) = F.cd_regione
        UNION ALL
        SELECT C.pg_comune_fiscale, NVL(F.cd_provincia,'*'), NVL(G.cd_regione,'*')
        FROM   ANAGRAFICO C,
               TERZO D,
               COMUNE E,
               COMUNE E1,
               PROVINCIA F,
               REGIONE G
        WHERE  D.cd_terzo = cdTerzo AND
		       D.cd_anag = C.cd_anag AND
               E.pg_comune = C.pg_comune_fiscale AND
               E.ti_italiano_estero = 'E' AND
               E1.pg_comune = D.pg_comune_sede AND
               F.cd_provincia (+) = E1.cd_provincia AND
               G.cd_regione (+) = F.cd_regione;

	    EXCEPTION
	    WHEN no_data_found THEN
	        IBMERR001.RAISE_ERR_GENERICO('Impossibile recurerare il TERZO ' || cdTerzo);
		WHEN others THEN
	        IBMERR001.RAISE_ERR_GENERICO('Impossibile recurerare il TERZO ' || cdTerzo);
	END;
END getTerzoMinicarriera;

--
--	Cancello dalla vista VSX_LIQUIDAZIONE_RATE le rate per le quali sono
--	stati generati dei compensi
--
PROCEDURE cancellaRateMinicarriere
   (
       pgCall NUMBER
   ) IS
BEGIN
   DELETE FROM VSX_LIQUIDAZIONE_RATE
   WHERE pg_call = pgCall;
END cancellaRateMinicarriere;

--
--	Aggiorno la rata con i dati del compenso appena creato
--
PROCEDURE aggiornaRataMinicarriera
   (
       aRecCompenso COMPENSO%ROWTYPE,
	   aRecRataMinicarriera	MINICARRIERA_RATA%ROWTYPE
   ) IS
BEGIN
   UPDATE MINICARRIERA_RATA
   SET    stato_ass_compenso = 'T',
          cd_cds_compenso = aRecCompenso.cd_cds,
          cd_uo_compenso = aRecCompenso.cd_unita_organizzativa,
          esercizio_compenso = aRecCompenso.esercizio,
          pg_compenso = aRecCompenso.pg_compenso
   WHERE  cd_cds = aRecRataMinicarriera.cd_cds AND
          cd_unita_organizzativa = aRecRataMinicarriera.cd_unita_organizzativa AND
          esercizio = aRecRataMinicarriera.esercizio AND
          pg_rata = aRecRataMinicarriera.pg_rata AND
          pg_minicarriera = aRecRataMinicarriera.pg_minicarriera;
END aggiornaRataMinicarriera;

--
-- Aggiornamento del campo STATO_ASS_COMPENSO della minicarriera
--
PROCEDURE aggiornaMinicarriera
   (
	   aRecMinicarriera	MINICARRIERA%ROWTYPE
   ) IS
   statoAssCompenso CHAR := 'P';
   nRateNonAssociate NUMBER := 0;
BEGIN
    -- Verifico se tutte le rate della minicarriera sono legate a compenso
	-- SI -> STATO_ASS_COMPENSO della minicarriera = T
	-- NO -> STATO_ASS_COMPENSO della minicarriera = P
    SELECT count(*) INTO nRateNonAssociate
   	FROM   MINICARRIERA_RATA
	WHERE  stato_ass_compenso = 'N' AND
   	       cd_cds = aRecMinicarriera.cd_cds AND
           cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
           esercizio = aRecMinicarriera.esercizio AND
           pg_minicarriera = aRecMinicarriera.pg_minicarriera;

    if(nRateNonAssociate = 0) then
	  statoAssCompenso:='T';
	end if;

	-- Tabella MINICARRIERA - aggiorno il campo relativo all'associazione con il compenso
    UPDATE MINICARRIERA
    SET    stato_ass_compenso = statoAssCompenso
    WHERE  cd_cds = aRecMinicarriera.cd_cds AND
           cd_unita_organizzativa = aRecMinicarriera.cd_unita_organizzativa AND
           esercizio = aRecMinicarriera.esercizio AND
           pg_minicarriera = aRecMinicarriera.pg_minicarriera;

END aggiornaMinicarriera;

--
-- Aggiorno compenso con dati obbligazione
--
PROCEDURE aggiornaCompensoObbligazione
    (
	    aRecCompenso COMPENSO%ROWTYPE,
		aRecScadenzaObbligazione OBBLIGAZIONE_SCADENZARIO%ROWTYPE
	) IS
BEGIN
   UPDATE COMPENSO
   SET    cd_cds_obbligazione = aRecScadenzaObbligazione.cd_cds,
          esercizio_obbligazione = aRecScadenzaObbligazione.ESERCIZIO,
          esercizio_ori_obbligazione = aRecScadenzaObbligazione.ESERCIZIO_ORIGINALE,
          pg_obbligazione = aRecScadenzaObbligazione.pg_obbligazione,
          pg_obbligazione_scadenzario = aRecScadenzaObbligazione.pg_obbligazione_scadenzario
   WHERE  cd_cds = aRecCompenso.cd_cds AND
          cd_unita_organizzativa = aRecCompenso.cd_unita_organizzativa AND
          esercizio = aRecCompenso.esercizio AND
          pg_compenso = aRecCompenso.pg_compenso;
END	aggiornaCompensoObbligazione;

--
-- Metto lock alla numerazione del compenso e dell'obbligazione
--
PROCEDURE setLockTabelleNumerazione
    (
	 	cdCds      VARCHAR2,
		cdUO       VARCHAR2,
		aEsercizio NUMBER,
		aUtente    VARCHAR2
	) IS
    pgDocCont NUMBER;
BEGIN
   pgDocAmm:=0;

   -- Inizializzazione progressivo compenso. Ogni volta che creo un compenso
   -- (ad ogni loop) incremento questo contatore
   pgDocAmm:=CNRCTB100.getNextNum( cdCds, aEsercizio, cdUO, cdTipoDocumentoAmm,
   								   aUtente, dataOdierna);

   -- Metto un lock alla tabella. Il numero verra' gestito dalla procedura
   -- di creazione dell'obbligazione
   pgDocCont := CNRCTB018.getNextNumDocCont(cdTipoDocumentoCont, aEsercizio, cdCds, aUtente);

END	setLockTabelleNumerazione;

--
-- Recupero i numeri dei documenti contabili generati dalla liquidazione
-- del compenso per scriverli nel Log (campo Note)
--
FUNCTION getDocContCompenso
    (
	  aRecCompenso COMPENSO%ROWTYPE
	) RETURN VARCHAR2 IS
    strDocumenti VARCHAR2(4000);
	aRecDocumento V_DOC_CONT_COMP%ROWTYPE;
    gen_cur_doc_cont_compenso GenericCurTyp;
BEGIN
	 strDocumenti:='';

	 OPEN gen_cur_doc_cont_compenso FOR

       SELECT *
       FROM   V_DOC_CONT_COMP
	   WHERE  CD_CDS_COMPENSO = aRecCompenso.cd_cds AND
	 	      CD_UO_COMPENSO = aRecCompenso.cd_unita_organizzativa AND
		      ESERCIZIO_COMPENSO = aRecCompenso.esercizio AND
		      PG_COMPENSO = aRecCompenso.pg_compenso;

       LOOP
          FETCH gen_cur_doc_cont_compenso INTO
                aRecDocumento;

          EXIT WHEN gen_cur_doc_cont_compenso%NOTFOUND;

		  if(aRecDocumento.tipo_doc_cont = docMandato) then
		       strDocumenti := strDocumenti || 'Man:' || aRecDocumento.pg_doc_cont || ' ';
		  end if;
		  if(aRecDocumento.tipo_doc_cont = docReversale) then
		       strDocumenti := strDocumenti || 'Rev:' || aRecDocumento.pg_doc_cont || ' ';
		  end if;
       END LOOP;

     CLOSE gen_cur_doc_cont_compenso;

     RETURN strDocumenti;

END getDocContCompenso;

--
-- Ripristino l'ultimo prograssivo nella tabella NUMERAZIONE_DOC_AMM.
-- Al termine di ogni ciclo del loop incremento il progressivo del
-- compenso per poterlo aasegnare al successivo. Se pero' non ho piu' rate
-- da elaborare devo riaggiornare la tabella di numerazione diminuendo il
-- corrente di 1
--
PROCEDURE aggiornaUltimoPgDocAmm
    (
	 	cdCds      VARCHAR2,
		cdUO       VARCHAR2,
		aEsercizio NUMBER,
		aUtente    VARCHAR2
	) IS
BEGIN

 UPDATE NUMERAZIONE_DOC_AMM SET
        corrente=pgDocAmm-1,
	    utuv=aUtente,
	    duva=sysdate,
	    pg_ver_rec=pg_ver_rec+1
  WHERE ESERCIZIO = aEsercizio AND
        cd_cds = cdCds AND
		cd_unita_organizzativa = cdUo AND
		cd_tipo_documento_amm = cdTipoDocumentoAmm;
END aggiornaUltimoPgDocAmm;

END;
