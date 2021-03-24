--------------------------------------------------------
--  DDL for Package Body CNRCTB900
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB900" AS

function isAnagCervello(inRepID NUMBER, inEsercizio NUMBER, inAnag NUMBER) return varchar2 is
isCerv char(1);
conta  NUMBER;
Begin
    --Leggo tutti i trattamenti presenti in ESTRAZIONE_CUD_DETT e se uno di essi ha il flag dei cervelli
    -- restituisco 'Y' (c'è anche il conguaglio)

    Select Count(1)
    Into conta
    From ESTRAZIONE_CUD_DETT A
    Where A.id_estrazione = inRepID
      And A.esercizio = inEsercizio
      And A.cd_anag = inAnag
      And A.cd_trattamento In (Select t.cd_trattamento
                               From tipo_trattamento t
                               Where To_Char(t.dt_fin_validita,'yyyy') >= inEsercizio
                                 And t.fl_agevolazioni_cervelli = 'Y');
    If conta = 0 Then
       isCerv := 'N';
    Else
       isCerv := 'Y';
    End If;
    return isCerv;
End;

function isAnagRientroLav30(inRepID NUMBER, inEsercizio NUMBER, inAnag NUMBER) return varchar2 is
isRientroLav30 char(1);
conta  NUMBER;
Begin
    --Leggo tutti i trattamenti presenti in ESTRAZIONE_CUD_DETT e se uno di essi ha il flag dei cervelli
    -- restituisco 'Y' (c'è anche il conguaglio)

    Select Count(1)
    Into conta
    From ESTRAZIONE_CUD_DETT A
    Where A.id_estrazione = inRepID
      And A.esercizio = inEsercizio
      And A.cd_anag = inAnag
      And A.cd_trattamento In (Select t.cd_trattamento
                               From tipo_trattamento t, trattamento_cori c, tipo_contributo_ritenuta cr
                               Where t.cd_trattamento = C.CD_TRATTAMENTO
                     and To_Char(t.dt_fin_validita,'yyyy') >= inEsercizio
                     and c.cd_contributo_ritenuta = cr.cd_contributo_ritenuta
                     And t.fl_agevolazioni_rientro_lav = 'Y'
                     and CR.CD_CLASSIFICAZIONE_CORI = 'FI'
                     and CR.FL_CREDITO_IRPEF = 'N'
                     and substr(calcolo_imponibile,3,2) = '70');

    If conta = 0 Then
       isRientroLav30 := 'N';
    Else
       isRientroLav30 := 'Y';
    End If;
    return isRientroLav30;
End;

function isAnagRientroLav20(inRepID NUMBER, inEsercizio NUMBER, inAnag NUMBER) return varchar2 is
isRientroLav20 char(1);
conta  NUMBER;
Begin
    --Leggo tutti i trattamenti presenti in ESTRAZIONE_CUD_DETT e se uno di essi ha il flag dei cervelli
    -- restituisco 'Y' (c'è anche il conguaglio)

    Select Count(1)
    Into conta
    From ESTRAZIONE_CUD_DETT A
    Where A.id_estrazione = inRepID
      And A.esercizio = inEsercizio
      And A.cd_anag = inAnag
      And A.cd_trattamento In (Select t.cd_trattamento
                               From tipo_trattamento t, trattamento_cori c, tipo_contributo_ritenuta cr
                               Where t.cd_trattamento = C.CD_TRATTAMENTO
                     and To_Char(t.dt_fin_validita,'yyyy') >= inEsercizio
                     and c.cd_contributo_ritenuta = cr.cd_contributo_ritenuta
                     And t.fl_agevolazioni_rientro_lav = 'Y'
                     and CR.CD_CLASSIFICAZIONE_CORI = 'FI'
                     and CR.FL_CREDITO_IRPEF = 'N'
                     and substr(calcolo_imponibile,3,2) = '80');

    If conta = 0 Then
       isRientroLav20 := 'N';
    Else
       isRientroLav20 := 'Y';
    End If;
    return isRientroLav20;
End;
-- =================================================================================================
-- Controllo congruenza parametri in input
-- =================================================================================================
PROCEDURE chkParametriInput
   (
    inEsercizio NUMBER,
    inCdAnag VARCHAR2,
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
       inCdAnag IS NULL OR
       inRepID IS NULL OR
       inUtente IS NULL) THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Valorizzazione parametri in input errata. ' ||
          'Un identificativo (esercizio, codice anagrafico, identificativo report, id utente) risulta non valorizzato');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Verifica esistenza codice anagrafico

   -- Non faccio nulla se è richiesta l'estrazione per tutte le anagrafiche

   IF inCdAnag = '%' THEN
      RETURN;
   END IF;

   -- Verifica esistenza del codice anagrafico

   BEGIN

      SELECT COUNT(*) INTO numero
      FROM   ANAGRAFICO
      WHERE  cd_anag = inCdAnag;

      IF numero = 0 THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Codice anagrafico ' || inCdAnag || ' inesistente');
      END IF;

   END;

END chkParametriInput;

-- =================================================================================================
-- Memorizzazione parametri di uso della procedura di stampa
-- =================================================================================================
PROCEDURE valorizzaParametri
   (
    inEsercizio NUMBER
   ) IS

   i BINARY_INTEGER;

   aRecUnitaOrganizzativa UNITA_ORGANIZZATIVA%ROWTYPE;
   aRecTerzo TERZO%ROWTYPE;
   aRecVAnagraficoTerzo V_ANAGRAFICO_TERZO%ROWTYPE;

BEGIN

   ------------------------------------------------------------------------------------------------
   -- Azzeramento tabella parametri

   parametri_tab.DELETE;

   ------------------------------------------------------------------------------------------------
   -- Lettura del codice anagrafico e terzo corrispondente al CNR

   aRecUnitaOrganizzativa:=CNRCTB020.getUoEnte(inEsercizio);
   CNRCTB080.getTerzoPerUO(aRecUnitaOrganizzativa.cd_unita_organizzativa,
                           aRecTerzo.cd_terzo);

   BEGIN

      SELECT * INTO aRecVAnagraficoTerzo
      FROM   V_ANAGRAFICO_TERZO
      WHERE  cd_terzo = aRecTerzo.cd_terzo;

   END;

   ------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   FOR i IN 1 .. 22

   LOOP

      BEGIN

         -- Memorizzazione del nome dell'ENTE dichiarante

         IF    i = 1 THEN

               parametri_tab(i).stringa:=aRecVAnagraficoTerzo.ragione_sociale;

         -- Memorizzazione del codice fiscale dell'ENTE dichiarante

         ELSIF i = 2 THEN

               parametri_tab(i).stringa:=aRecVAnagraficoTerzo.codice_fiscale;
               IF parametri_tab(i).stringa IS NULL THEN
                  parametri_tab(i).stringa:='CF INESISTENTE';
               END IF;

         -- Memorizzazione del comune dell'ENTE dichiarante

         ELSIF i = 3 THEN

               parametri_tab(i).stringa:=aRecVAnagraficoTerzo.ds_comune_fiscale;

         -- Memorizzazione della provincia dell'ENTE dichiarante

         ELSIF i = 4 THEN

               parametri_tab(i).stringa:=aRecVAnagraficoTerzo.cd_provincia_fiscale;

         -- Memorizzazione del CAP dell'ENTE dichiarante

         ELSIF i = 5 THEN

               parametri_tab(i).stringa:=aRecVAnagraficoTerzo.cap_comune_fiscale;

         -- Memorizzazione indirizzo dell'ENTE dichiarante

         ELSIF i = 6 THEN

               IF aRecVAnagraficoTerzo.num_civico_fiscale IS NOT NULL THEN
                  parametri_tab(i).stringa:=aRecVAnagraficoTerzo.via_fiscale  || ', ' ||
                                            aRecVAnagraficoTerzo.num_civico_fiscale;
               ELSE
                  parametri_tab(i).stringa:=aRecVAnagraficoTerzo.via_fiscale;
               END IF;

        -- Memorizzazione telefono dell'ENTE dichiarante

         ELSIF i = 7 THEN

               BEGIN

                  SELECT riferimento INTO parametri_tab(i).stringa
                  FROM   TELEFONO
                  WHERE  cd_terzo = aRecTerzo.cd_terzo AND
                         ti_riferimento = 'T' AND
                         rownum = 1;

               EXCEPTION

                  WHEN no_data_found THEN
                       parametri_tab(i).stringa:=NULL;

               END;

        -- Memorizzazione fax dell'ENTE dichiarante

         ELSIF i = 8 THEN

               BEGIN

                  SELECT riferimento INTO parametri_tab(i).stringa
                  FROM   TELEFONO
                  WHERE  cd_terzo = aRecTerzo.cd_terzo AND
                         ti_riferimento = 'F' AND
                         rownum = 1;

               EXCEPTION

                  WHEN no_data_found THEN
                       parametri_tab(i).stringa:=NULL;

               END;

        -- Memorizzazione mail dell'ENTE dichiarante

         ELSIF i = 9 THEN

               BEGIN

                  SELECT riferimento INTO parametri_tab(i).stringa
                  FROM   TELEFONO
                  WHERE  cd_terzo = aRecTerzo.cd_terzo AND
                         ti_riferimento = 'E' AND
                         rownum = 1;

               EXCEPTION

                  WHEN no_data_found THEN
                       parametri_tab(i).stringa:=NULL;

               END;

        -- Memorizzazione RAPPRESENTANTE_FISCALE dell'ENTE dichiarante

         ELSIF i = 10 THEN

               IF aRecVAnagraficoTerzo.pg_rapp_legale IS NULL THEN
                  parametri_tab(i).stringa:=NULL;
               ELSE

                  BEGIN

                     SELECT LTRIM(RTRIM(cognome || ' ' || nome)) INTO parametri_tab(i).stringa
                     FROM   RAPPRESENTANTE_LEGALE
                     WHERE  pg_rapp_legale = aRecVAnagraficoTerzo.pg_rapp_legale;

                  EXCEPTION

                     WHEN no_data_found THEN
                          parametri_tab(i).stringa:=NULL;

                  END;

               END IF;

        -- Memorizzazione STATO_CONTABILE_LIQUIDAZIONE_CORI dell'Ateneo

         ELSIF i = 11 THEN

               parametri_tab(i).stringa:=CNRCTB015.getVal01PerChiave(inEsercizio,
                                                                     'GESTIONE_CORI_SPECIALE',
                                                                     'STATO_LIQUIDA_CORI');

        -- Memorizzazione dell'eventuale annotazione di default per il CUD

         ELSIF i = 12 THEN

               BEGIN

                  SELECT ds_nota INTO parametri_tab(i).stringa
                  FROM   NOTE_ANAGRAFICO
                  WHERE  esercizio = inEsercizio AND
                         cd_anag = 0 AND
                         tipo_nota = 'CUD';

               EXCEPTION

                  WHEN no_data_found THEN
                       parametri_tab(i).stringa:=NULL;

               END;

        -- Annotazione di default per CUD con uso di trattamenti con aliquota_max

         ELSIF i = 13 THEN

               parametri_tab(i).stringa:='IRPEF calcolata applicando l''aliquota marginale richiesta dal percipiente';

        -- Annotazione di default per CUD con addizionali regionali o comunali totalmente trattenute

         ELSIF i = 14 THEN

               parametri_tab(i).stringa:='Gli importi delle addizionali sono stati interamente trattenuti';

        -- Annotazione di default per CUD con addizionali regionali o comunali accantonate

         ELSIF i = 15 THEN

               parametri_tab(i).stringa:='Gli importi delle addizionali sono stati accantonati';

        -- Annotazione di default per CUD a soggetti non residenti

         ELSIF i = 16 THEN

               parametri_tab(i).stringa:='IRPEF calcolata applicando ritenuta a titolo d''imposta del 30% ' ||
                                         'prevista per i soggetti non residenti in Italia';

        -- Annotazione di default per indicare gli intervalli temporali di riferimento dei periodi compenso

         ELSIF i = 17 THEN

               parametri_tab(i).stringa:='I giorni indicati al punto 6 sono stati determinati tenendo conto ' ||
                                         'dei seguenti periodi di competenza:';

        -- Annotazione di default prima parte quota deduzione standard

         ELSIF i = 18 THEN

               parametri_tab(i).stringa:='La quota di deduzione riconosciuta su tutte le tipologie di reddito ' ||
                                         'ai sensi dell''art. 10 bis comma 1 del TUIR è pari a euro ';

        -- Annotazione di default seconda parte quota deduzione standard

         ELSIF i = 19 THEN

               parametri_tab(i).stringa:=', la quota di deduzione riconosciuta sui redditi di lavoro dipendente ' ||
                                         'o assimilati ai sensi dell''art. 10 bis comma 2 del TUIR è pari a euro ';

        -- Annotazione prima parte quota deduzione fissa intera

         ELSIF i = 20 THEN

               parametri_tab(i).stringa:='Su richiesta del sostituito la deduzione riconosciuta su tutte le ' ||
                                         'tipologie di reddito ai sensi dell''art. 10 bis comma 1 del TUIR, è ' ||
                                         'stata attribuita interamente per euro ';

        -- Annotazione seconda parte quota deduzione fissa intera

         ELSIF i = 21 THEN

               parametri_tab(i).stringa:=', la quota di deduzione riconosciuta sui redditi di lavoro dipendente ' ||
                                         'o assimilati ai sensi dell''art. 10 bis comma 2 del TUIR è pari a euro ';
        -- Annotazione seconda parte quota deduzione fissa intera

         ELSIF i = 22 THEN

               parametri_tab(i).stringa:='Le detrazioni di cui ai punti 33 e 34 sono state determinate tenendo ' ||
                                         'conto degli altri redditi comunicati dal sostituito pari a euro ';

         END IF;

      END;

   END LOOP;

END valorizzaParametri;


-- =================================================================================================
-- Estrazione dati per stampa CUD (Inserimento record in ESTRAZIONE_CUD_DETT)
-- =================================================================================================
PROCEDURE inserisciDatiCUDDett
   (
    inEsercizio NUMBER,
    aCdAnag NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   aNumero INTEGER;

   aRecEstraiImponibileCudV V_ESTRAI_IMPONIBILE_CUD%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale di lettura record per inserimento in ESTRAZIONE_CUD_DETT

   BEGIN

      IF aCdAnag IS NULL THEN

         OPEN gen_cur FOR

              SELECT *
              FROM   V_ESTRAI_IMPONIBILE_CUD
              WHERE  esercizio_configurazione = inEsercizio AND
                     esercizio_pagamento = inEsercizio and
                     (fl_compenso_conguaglio = 'Y'
                     OR
                     fl_senza_calcoli = 'Y'
                     OR
                     (fl_compenso_conguaglio = 'N' AND fl_senza_calcoli = 'N' AND IM_LORDO_PERCIP_COMPENSO > 0.01)
                     )
              FOR UPDATE OF pg_compenso NOWAIT;

      ELSE

         OPEN gen_cur FOR

              SELECT *
              FROM   V_ESTRAI_IMPONIBILE_CUD
              WHERE  cd_anag = aCdAnag AND
                     esercizio_configurazione = inEsercizio AND
                     esercizio_pagamento = inEsercizio and
                     (fl_compenso_conguaglio = 'Y'
                     OR
                     fl_senza_calcoli = 'Y'
                     OR
                     (fl_compenso_conguaglio = 'N' AND fl_senza_calcoli = 'N' AND IM_LORDO_PERCIP_COMPENSO > 0.01)
                     )
              FOR UPDATE OF pg_compenso NOWAIT;

      END IF;

      LOOP

         FETCH gen_cur INTO
               aRecEstraiImponibileCudV;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Inserimento record su ESTRAZIONE_CUD_DETT

         INSERT INTO ESTRAZIONE_CUD_DETT
                (id_estrazione,
                 esercizio,
                 cd_cds_compenso,
                 cd_uo_compenso,
                 esercizio_compenso,
                 pg_compenso,
                 esercizio_pagamento,
                 fl_senza_calcoli,
                 fl_compenso_conguaglio,
                 fl_incluso_in_conguaglio,
                 fl_ultimo_conguaglio,
                 dacr_conguaglio,
                 cd_cds_conguaglio,
                 cd_uo_conguaglio,
                 esercizio_conguaglio,
                 pg_conguaglio,
                 cd_trattamento,
                 fl_detrazioni,
                 fl_aliquota_max,
                 fl_tassazione_separata,
                 fl_compenso_minicarriera,
                 fl_recupero_rate,
                 fl_cococo,
                 fl_ritenuta_non_residenti,
                 cd_tipologia_reddito,
                 ds_tipologia_reddito,
                 cd_anag,
                 cd_terzo,
                 dt_cmp_da_compenso,
                 dt_cmp_a_compenso,
                 im_lordo_percip_compenso,
                 quota_esente_compenso,
                 im_no_fiscale_compenso,
                 imponibile_fisc_lordo_compenso,
                 imponibile_fisc_netto_compenso,
                 im_deduzione_compenso,
                 detraz_pe_netto_compenso,
                 detraz_la_netto_compenso,
                 detraz_co_netto_compenso,
                 detraz_fi_netto_compenso,
                 detraz_al_netto_compenso,
                 fl_escludi_qvaria_deduzione,
                 fl_intera_qfissa_deduzione,
                 imponibile_fisc_lordo_contrib,
                 imponibile_fisc_netto_contrib,
                 im_deduzione_contrib,
                 im_riduz_contrib,
                 im_previd_percip_contrib,
                 im_inail_percip_contrib,
                 im_irpef_lordo_contrib,
                 im_irpef_netto_contrib,
                 im_addreg_contrib,
                 im_addpro_contrib,
                 im_addcom_contrib,
                 primo_acconto_irpef_contrib,
                 secondo_acconto_irpef_contrib,
                 dt_cmp_da_esterno,
                 dt_cmp_a_esterno,
                 imponibile_fiscale_esterno,
                 im_irpef_esterno,
                 im_addreg_esterno,
                 im_addpro_esterno,
                 im_addcom_esterno,
                 detraz_pe_esterno,
                 detraz_la_esterno,
                 detraz_co_esterno,
                 detraz_fi_esterno,
                 detraz_al_esterno,
                 im_deduzione_dovuto_conguaglio,
                 im_deduzione_goduto_conguaglio,
                 im_ritenute_irpef_dovuto_cong,
                 im_addreg_dovuto_cong,
                 im_addpro_dovuto_cong,
                 im_addcom_dovuto_cong,
                 detraz_pe_dovuto_conguaglio,
                 detraz_la_dovuto_conguaglio,
                 detraz_co_dovuto_conguaglio,
                 detraz_fi_dovuto_conguaglio,
                 detraz_al_dovuto_conguaglio,
                 im_ritenute_irpef_goduto_cong,
                 im_addreg_goduto_cong,
                 im_addpro_goduto_cong,
                 im_addcom_goduto_cong,
                 detraz_pe_goduto_conguaglio,
                 detraz_la_goduto_conguaglio,
                 detraz_co_goduto_conguaglio,
                 detraz_fi_goduto_conguaglio,
                 detraz_al_goduto_conguaglio,
                 dacr,
                 utcr,
                 duva,
                 utuv,
                 pg_ver_rec,
                 im_previd_ente_contrib,
                 imponibile_previd_contrib,
                 im_deduzione_family,
                 im_ded_family_dovuto_cong,
                         im_ded_family_goduto_cong,
                         im_irpef_sospeso,
                         fl_compenso_missione,
                         im_redd_non_tassati_per_conv,
                         im_redd_esenti_per_legge,
                         im_bonus_erogato,
                         im_bonus_dovuto_cong,
                         im_bonus_goduto_cong,
                         cd_categoria,
                         DETRAZIONE_RID_CUNEO_NETTO,
                         im_rid_cuneo_erogato,
                         im_credito_irpef_dovuto,
                         im_credito_irpef_goduto,
                         im_detr_rid_cuneo_dovuto_cong,
                         im_detr_rid_cuneo_goduto_cong)
         VALUES (inRepID,
                 inEsercizio,
                 aRecEstraiImponibileCudV.cd_cds_compenso,
                 aRecEstraiImponibileCudV.cd_uo_compenso,
                 aRecEstraiImponibileCudV.esercizio_compenso,
                 aRecEstraiImponibileCudV.pg_compenso,
                 aRecEstraiImponibileCudV.esercizio_pagamento,
                 aRecEstraiImponibileCudV.fl_senza_calcoli,
                 aRecEstraiImponibileCudV.fl_compenso_conguaglio,
                 'N',
                 'N',
                 TO_DATE(NULL),
                 NULL,
                 NULL,
                 TO_NUMBER(NULL),
                 TO_NUMBER(NULL),
                 aRecEstraiImponibileCudV.cd_trattamento,
                 aRecEstraiImponibileCudV.fl_detrazioni,
                 'N',
                 aRecEstraiImponibileCudV.fl_tassazione_separata,
                 aRecEstraiImponibileCudV.fl_compenso_minicarriera,
                 aRecEstraiImponibileCudV.fl_recupero_rate,
                 aRecEstraiImponibileCudV.fl_cococo,
                 'N',
                 aRecEstraiImponibileCudV.cd_tipologia_reddito,
                 aRecEstraiImponibileCudV.ds_tipologia_reddito,
                 aRecEstraiImponibileCudV.cd_anag,
                 aRecEstraiImponibileCudV.cd_terzo,
                 aRecEstraiImponibileCudV.dt_cmp_da_compenso,
                 aRecEstraiImponibileCudV.dt_cmp_a_compenso,
                 aRecEstraiImponibileCudV.im_lordo_percip_compenso,
                 aRecEstraiImponibileCudV.quota_esente_compenso,
                 aRecEstraiImponibileCudV.im_no_fiscale_compenso,
                 aRecEstraiImponibileCudV.imponibile_fisc_lordo_compenso,
                 aRecEstraiImponibileCudV.imponibile_fisc_netto_compenso,
                 aRecEstraiImponibileCudV.im_deduzione_compenso,
                 aRecEstraiImponibileCudV.detraz_pe_netto_compenso,
                 aRecEstraiImponibileCudV.detraz_la_netto_compenso,
                 aRecEstraiImponibileCudV.detraz_co_netto_compenso,
                 aRecEstraiImponibileCudV.detraz_fi_netto_compenso,
                 aRecEstraiImponibileCudV.detraz_al_netto_compenso,
                 aRecEstraiImponibileCudV.fl_escludi_qvaria_deduzione,
                 aRecEstraiImponibileCudV.fl_intera_qfissa_deduzione,
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
                 0,
                 0,
                 TO_DATE(NULL),
                 TO_DATE(NULL),
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
                 0,
                 0,
                 0,
                 0,
                 0,
                 0,
                 0,
                 0,
                 dataOdierna,
                 inUtente,
                 dataOdierna,
                 inUtente,
                 1,
                 0,
                 0,
                 0,          --verrà valorizzato da CONTRIBUTO_RITENUTA
                 0,          --verrà valorizzato da CONGUAGLIO
                 0,          --verrà valorizzato da CONGUAGLIO
                 0,      --verrà valorizzato da CONTRIBUTO_RITENUTA o da CONGUAGLIO
                 aRecEstraiImponibileCudV.fl_compenso_missione,
                         aRecEstraiImponibileCudV.im_redd_non_tassati_per_conv,
                         aRecEstraiImponibileCudV.im_redd_esenti_per_legge,
                         0,
                         0,
                         0,
                         aRecEstraiImponibileCudV.cd_categoria,
                         aRecEstraiImponibileCudV.DETRAZIONE_RID_CUNEO_NETTO,
                 0,
                 0,
                 0,
                 0,
                 0);
      END LOOP;

      CLOSE gen_cur;

   END;


END inserisciDatiCUDDett;

-- =================================================================================================
-- Estrazione dati per stampa CUD (Aggiornamento record in ESTRAZIONE_CUD_DETT da CONTRIBUTO_RITENUTA)
-- =================================================================================================
PROCEDURE upgDatiCUDDettCori
   (
    inEsercizio NUMBER,
    inRepID INTEGER
   ) IS

   aRecEstrazioneCudDett ESTRAZIONE_CUD_DETT%ROWTYPE;
   aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE;
   aRecTipoContributoRitenuta TIPO_CONTRIBUTO_RITENUTA%ROWTYPE;

   gen_cur GenericCurTyp;

   DATA_INIZIO_RID_CUNEO DATE;

BEGIN

   DATA_INIZIO_RID_CUNEO := CNRCTB015.getDt01PerChiave('0', 'RIDUZIONE_CUNEO_DL_3_2020', 'DATA_INIZIO');


   -------------------------------------------------------------------------------------------------
   -- Recupero delle informazioni relativi agli importi a carico percipiente (INPS, INAIL, RIDUZ e IRPEF)
   -- a carico percepiente

   BEGIN

      OPEN gen_cur FOR

           SELECT A.cd_cds_compenso,
                  A.cd_uo_compenso,
                  A.esercizio_compenso,
                  A.pg_compenso,
                  A.cd_trattamento,
                  B.cd_contributo_ritenuta,
                  B.ti_ente_percipiente,
                  B.imponibile_lordo,
                  B.imponibile,
                  B.im_deduzione_irpef,
                  B.im_deduzione_family,
                  B.ammontare_lordo,
                  B.ammontare,
                  B.im_cori_sospeso,
                  C.fl_assistenza_fiscale,
                  C.cd_classificazione_cori,
                  C.pg_classificazione_montanti,
                  C.fl_scrivi_montanti,
                  C.fl_credito_irpef,
                  a.dt_cmp_da_compenso,
                  a.dt_cmp_a_compenso,
                  c.dt_ini_validita
           FROM   ESTRAZIONE_CUD_DETT A,
                  CONTRIBUTO_RITENUTA B,
                  TIPO_CONTRIBUTO_RITENUTA C
           WHERE  A.id_estrazione = inRepID AND
                  A.esercizio = inEsercizio AND
                  B.cd_cds = A.cd_cds_compenso AND
                  B.cd_unita_organizzativa = A.cd_uo_compenso AND
                  B.esercizio = A.esercizio_compenso AND
                  B.pg_compenso = A.pg_compenso AND
                  C.cd_contributo_ritenuta = B.cd_contributo_ritenuta AND
                  C.dt_ini_validita = B.dt_ini_validita;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCudDett.cd_cds_compenso,
               aRecEstrazioneCudDett.cd_uo_compenso,
               aRecEstrazioneCudDett.esercizio_compenso,
               aRecEstrazioneCudDett.pg_compenso,
               aRecEstrazioneCudDett.cd_trattamento,
               aRecContributoRitenuta.cd_contributo_ritenuta,
               aRecContributoRitenuta.ti_ente_percipiente,
               aRecContributoRitenuta.imponibile_lordo,
               aRecContributoRitenuta.imponibile,
               aRecContributoRitenuta.im_deduzione_irpef,
               aRecContributoRitenuta.im_deduzione_family,
               aRecContributoRitenuta.ammontare_lordo,
               aRecContributoRitenuta.ammontare,
               aRecContributoRitenuta.im_cori_sospeso,
               aRecTipoContributoRitenuta.fl_assistenza_fiscale,
               aRecTipoContributoRitenuta.cd_classificazione_cori,
               aRecTipoContributoRitenuta.pg_classificazione_montanti,
               aRecTipoContributoRitenuta.fl_scrivi_montanti,
               aRecTipoContributoRitenuta.fl_credito_irpef,
               aRecEstrazioneCudDett.dt_cmp_da_compenso,
               aRecEstrazioneCudDett.dt_cmp_a_compenso,
               aRecTipoContributoRitenuta.dt_ini_validita;

         EXIT WHEN gen_cur%NOTFOUND;

         aRecEstrazioneCudDett.imponibile_fisc_lordo_contrib:=0;
         aRecEstrazioneCudDett.imponibile_fisc_netto_contrib:=0;
         aRecEstrazioneCudDett.im_deduzione_contrib:=0;
         aRecEstrazioneCudDett.im_deduzione_family:=0;
         aRecEstrazioneCudDett.im_riduz_contrib:=0;
         aRecEstrazioneCudDett.im_previd_percip_contrib:=0;
         aRecEstrazioneCudDett.im_previd_ente_contrib:=0;
         aRecEstrazioneCudDett.im_inail_percip_contrib:=0;
         aRecEstrazioneCudDett.im_irpef_lordo_contrib:=0;
         aRecEstrazioneCudDett.im_irpef_netto_contrib:=0;
         aRecEstrazioneCudDett.im_irpef_sospeso:=0;
         aRecEstrazioneCudDett.im_addreg_contrib:=0;
         aRecEstrazioneCudDett.im_addpro_contrib:=0;
         aRecEstrazioneCudDett.im_addcom_contrib:=0;
         aRecEstrazioneCudDett.primo_acconto_irpef_contrib:=0;
         aRecEstrazioneCudDett.secondo_acconto_irpef_contrib:=0;
         aRecEstrazioneCudDett.imponibile_previd_contrib:=0;
         aRecEstrazioneCudDett.fl_ritenuta_non_residenti:='N';
         aRecEstrazioneCudDett.im_bonus_erogato:=0;
         aRecEstrazioneCudDett.im_rid_cuneo_erogato:=0;

         -- Normalizzazione importi ----------------------------------------------------------------

         -- Parte percipiente

         IF aRecContributoRitenuta.ti_ente_percipiente = 'P' THEN

            -- Riduzione erariale, acconti assistenza fiscale e irpef con dettaglio per stranieri
--pipe.send_message('aRecTipoContributoRitenuta.cd_classificazione_cori = '||aRecTipoContributoRitenuta.cd_classificazione_cori);
--pipe.send_message('aRecTipoContributoRitenuta.fl_credito_irpef = '||aRecTipoContributoRitenuta.fl_credito_irpef);
            IF    aRecTipoContributoRitenuta.cd_classificazione_cori = CNRCTB545.isCoriFiscale THEN
                  IF (aRecTipoContributoRitenuta.pg_classificazione_montanti IS NOT NULL AND
                      aRecTipoContributoRitenuta.pg_classificazione_montanti = 5) THEN
                     aRecEstrazioneCudDett.im_riduz_contrib:=aRecContributoRitenuta.ammontare_lordo;
                  ELSIF aRecTipoContributoRitenuta.fl_credito_irpef = 'Y' then
                    if DATA_INIZIO_RID_CUNEO > aRecTipoContributoRitenuta.dt_ini_validita then
                     aRecEstrazioneCudDett.im_bonus_erogato:=-aRecContributoRitenuta.ammontare_lordo;
                    else
                     aRecEstrazioneCudDett.im_rid_cuneo_erogato:=-aRecContributoRitenuta.ammontare_lordo;
                    end if;
--pipe.send_message('aRecEstrazioneCudDett.im_bonus_erogato = '||aRecEstrazioneCudDett.im_bonus_erogato);
                  ELSE
                             aRecEstrazioneCudDett.im_deduzione_family:=aRecContributoRitenuta.im_deduzione_family;
                     IF aRecTipoContributoRitenuta.fl_assistenza_fiscale = 'Y' Then
                        --QUESTI CAMPI VALORIZZATI NON VENGONO PIù UTILIZZATI, IN OGNI CASO I CORI PER L'ASSISTENZA
                        --FISCALE VANNO ESCLUSI PER IL CALCOLO DELL'AMMONTARE
                        IF    (aRecContributoRitenuta.cd_contributo_ritenuta = 'IRPEF730I' OR
                               aRecContributoRitenuta.cd_contributo_ritenuta = 'IRPEF730IC') THEN
                              aRecEstrazioneCudDett.primo_acconto_irpef_contrib:=aRecContributoRitenuta.ammontare_lordo;
                        ELSIF (aRecContributoRitenuta.cd_contributo_ritenuta = 'IRPEF730II' OR
                               aRecContributoRitenuta.cd_contributo_ritenuta = 'IRP730ICO') THEN
                              aRecEstrazioneCudDett.secondo_acconto_irpef_contrib:=aRecContributoRitenuta.ammontare_lordo;
                        END IF;
                     ELSE
                        --IF  aRecContributoRitenuta.cd_contributo_ritenuta = 'IRPEFSTRA' Then

                        --I CORI FISCALI NON A SCAGLIONI DOVREBBERO ESSERE SOLO QUELLI DEI NON RESIDENTI
                        --CI SONO ANCHE NON RESIDENTI CHE HANNO L'IRPEF A SCAGLIONI (BORS,ASS), MA QUESTI NON MI
                        --INTERESSA ETICHETTARLI COME NON RESIDENTI
                        If CNRCTB545.getIsIrpefScaglioni(aRecTipoContributoRitenuta.cd_classificazione_cori,
                                                         aRecTipoContributoRitenuta.pg_classificazione_montanti,
                                                         aRecTipoContributoRitenuta.fl_scrivi_montanti) = 'N' Then
                            aRecEstrazioneCudDett.fl_ritenuta_non_residenti:='Y';
                        END IF;
                        aRecEstrazioneCudDett.imponibile_fisc_lordo_contrib:=aRecContributoRitenuta.imponibile_lordo;
                        aRecEstrazioneCudDett.imponibile_fisc_netto_contrib:=aRecContributoRitenuta.imponibile;
                        aRecEstrazioneCudDett.im_deduzione_contrib:=aRecContributoRitenuta.im_deduzione_irpef;
                        aRecEstrazioneCudDett.im_irpef_lordo_contrib:=aRecContributoRitenuta.ammontare_lordo;
                        aRecEstrazioneCudDett.im_irpef_netto_contrib:=aRecContributoRitenuta.ammontare;
                        aRecEstrazioneCudDett.im_irpef_sospeso:=aRecContributoRitenuta.im_cori_sospeso;
                     END IF;
                  END IF;

            -- Previdenziale

            ELSIF aRecTipoContributoRitenuta.cd_classificazione_cori = CNRCTB545.isCoriPrevid THEN
                  aRecEstrazioneCudDett.im_previd_percip_contrib:=aRecContributoRitenuta.ammontare_lordo;
                  -- Se è stato superato lo scaglione (cioè esiste il record in contributo_ritenuta_det),
                  -- l'imponibile previdenziale deve essere preso da li, perchè su contributo_ritenuta c'è
                  -- l'intero importo e non solo quello realmente imponibile
                  Select Nvl(Sum(imponibile),0)
                  Into aRecEstrazioneCudDett.imponibile_previd_contrib
                  From contributo_ritenuta_det
                  Where cd_cds = aRecEstrazioneCudDett.cd_cds_compenso
                    And cd_unita_organizzativa = aRecEstrazioneCudDett.cd_uo_compenso
                    And esercizio = aRecEstrazioneCudDett.esercizio_compenso
                    And pg_compenso = aRecEstrazioneCudDett.pg_compenso
                    And cd_contributo_ritenuta = aRecContributoRitenuta.cd_contributo_ritenuta
                    And ti_ente_percipiente = aRecContributoRitenuta.ti_ente_percipiente;

                  If aRecEstrazioneCudDett.imponibile_previd_contrib = 0 Then
                     aRecEstrazioneCudDett.imponibile_previd_contrib:=aRecContributoRitenuta.imponibile;
                  End If;
            -- Inail

            ELSIF aRecTipoContributoRitenuta.cd_classificazione_cori = CNRCTB545.isCoriInail THEN
                  aRecEstrazioneCudDett.im_inail_percip_contrib:=aRecContributoRitenuta.ammontare_lordo;

            -- Addizionale Regionale

            ELSIF aRecTipoContributoRitenuta.cd_classificazione_cori = CNRCTB545.isCoriAddReg THEN
                  aRecEstrazioneCudDett.im_addreg_contrib:=aRecContributoRitenuta.ammontare_lordo;

            -- Addizionale Provinciale

            ELSIF aRecTipoContributoRitenuta.cd_classificazione_cori = CNRCTB545.isCoriAddPro THEN
                  aRecEstrazioneCudDett.im_addpro_contrib:=aRecContributoRitenuta.ammontare_lordo;

            -- Addizionale Comunale

            ELSIF aRecTipoContributoRitenuta.cd_classificazione_cori = CNRCTB545.isCoriAddCom THEN
                  aRecEstrazioneCudDett.im_addcom_contrib:=aRecContributoRitenuta.ammontare_lordo;

            END IF;

         -- Parte ente

         ELSE

            -- Previdenziale

            IF aRecTipoContributoRitenuta.cd_classificazione_cori = CNRCTB545.isCoriPrevid THEN
               aRecEstrazioneCudDett.im_previd_ente_contrib:=aRecContributoRitenuta.ammontare_lordo;
            END IF;

         END IF;

         -- Aggiornamento ESTRAZIONE_CUD_DETT ------------------------------------------------------

         BEGIN

            IF aRecEstrazioneCudDett.fl_ritenuta_non_residenti ='Y' THEN

               UPDATE ESTRAZIONE_CUD_DETT
               SET    fl_ritenuta_non_residenti = aRecEstrazioneCudDett.fl_ritenuta_non_residenti,
                      imponibile_fisc_lordo_contrib = imponibile_fisc_lordo_contrib +
                                                      aRecEstrazioneCudDett.imponibile_fisc_lordo_contrib,
                      imponibile_fisc_netto_contrib = imponibile_fisc_netto_contrib +
                                                      aRecEstrazioneCudDett.imponibile_fisc_netto_contrib,
                      im_deduzione_contrib = im_deduzione_contrib + aRecEstrazioneCudDett.im_deduzione_contrib,
                      im_deduzione_family = im_deduzione_family + aRecEstrazioneCudDett.im_deduzione_family,
                      im_riduz_contrib = im_riduz_contrib + aRecEstrazioneCudDett.im_riduz_contrib,
                      im_previd_percip_contrib = im_previd_percip_contrib +
                                                 aRecEstrazioneCudDett.im_previd_percip_contrib,
                      im_previd_ente_contrib = im_previd_ente_contrib +
                                               aRecEstrazioneCudDett.im_previd_ente_contrib,
                      im_inail_percip_contrib = im_inail_percip_contrib + aRecEstrazioneCudDett.im_inail_percip_contrib,
                      im_irpef_lordo_contrib = im_irpef_lordo_contrib + aRecEstrazioneCudDett.im_irpef_lordo_contrib,
                      im_irpef_netto_contrib = im_irpef_netto_contrib + aRecEstrazioneCudDett.im_irpef_netto_contrib,
                      im_irpef_sospeso = im_irpef_sospeso + aRecEstrazioneCudDett.im_irpef_sospeso,
                      im_addreg_contrib = im_addreg_contrib + aRecEstrazioneCudDett.im_addreg_contrib,
                      im_addpro_contrib = im_addpro_contrib + aRecEstrazioneCudDett.im_addpro_contrib,
                      im_addcom_contrib = im_addcom_contrib + aRecEstrazioneCudDett.im_addcom_contrib,
                      primo_acconto_irpef_contrib = primo_acconto_irpef_contrib +
                                                    aRecEstrazioneCudDett.primo_acconto_irpef_contrib,
                      secondo_acconto_irpef_contrib = secondo_acconto_irpef_contrib +
                                                      aRecEstrazioneCudDett.secondo_acconto_irpef_contrib,
                      imponibile_previd_contrib = imponibile_previd_contrib +
                                                  aRecEstrazioneCudDett.imponibile_previd_contrib,
                      im_bonus_erogato = im_bonus_erogato + aRecEstrazioneCudDett.im_bonus_erogato,
                      im_rid_cuneo_erogato = im_rid_cuneo_erogato + aRecEstrazioneCudDett.im_rid_cuneo_erogato
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio AND
                      cd_cds_compenso = aRecEstrazioneCudDett.cd_cds_compenso AND
                      cd_uo_compenso = aRecEstrazioneCudDett.cd_uo_compenso AND
                      esercizio_compenso = aRecEstrazioneCudDett.esercizio_compenso AND
                      pg_compenso = aRecEstrazioneCudDett.pg_compenso;

            ELSE

               UPDATE ESTRAZIONE_CUD_DETT
               SET    imponibile_fisc_lordo_contrib = imponibile_fisc_lordo_contrib +
                                                      aRecEstrazioneCudDett.imponibile_fisc_lordo_contrib,
                      imponibile_fisc_netto_contrib = imponibile_fisc_netto_contrib +
                                                      aRecEstrazioneCudDett.imponibile_fisc_netto_contrib,
                      im_deduzione_contrib = im_deduzione_contrib + aRecEstrazioneCudDett.im_deduzione_contrib,
                      im_deduzione_family = im_deduzione_family + aRecEstrazioneCudDett.im_deduzione_family,
                      im_riduz_contrib = im_riduz_contrib + aRecEstrazioneCudDett.im_riduz_contrib,
                      im_previd_percip_contrib = im_previd_percip_contrib +
                                                 aRecEstrazioneCudDett.im_previd_percip_contrib,
                      im_previd_ente_contrib = im_previd_ente_contrib +
                                               aRecEstrazioneCudDett.im_previd_ente_contrib,
                      im_inail_percip_contrib = im_inail_percip_contrib + aRecEstrazioneCudDett.im_inail_percip_contrib,
                      im_irpef_lordo_contrib = im_irpef_lordo_contrib + aRecEstrazioneCudDett.im_irpef_lordo_contrib,
                      im_irpef_netto_contrib = im_irpef_netto_contrib + aRecEstrazioneCudDett.im_irpef_netto_contrib,
                      im_irpef_sospeso = im_irpef_sospeso + aRecEstrazioneCudDett.im_irpef_sospeso,
                      im_addreg_contrib = im_addreg_contrib + aRecEstrazioneCudDett.im_addreg_contrib,
                      im_addpro_contrib = im_addpro_contrib + aRecEstrazioneCudDett.im_addpro_contrib,
                      im_addcom_contrib = im_addcom_contrib + aRecEstrazioneCudDett.im_addcom_contrib,
                      primo_acconto_irpef_contrib = primo_acconto_irpef_contrib +
                                                    aRecEstrazioneCudDett.primo_acconto_irpef_contrib,
                      secondo_acconto_irpef_contrib = secondo_acconto_irpef_contrib +
                                                      aRecEstrazioneCudDett.secondo_acconto_irpef_contrib,
                      imponibile_previd_contrib = imponibile_previd_contrib +
                                                  aRecEstrazioneCudDett.imponibile_previd_contrib,
                      im_bonus_erogato = im_bonus_erogato + aRecEstrazioneCudDett.im_bonus_erogato,
                      im_rid_cuneo_erogato = im_rid_cuneo_erogato + aRecEstrazioneCudDett.im_rid_cuneo_erogato
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio AND
                      cd_cds_compenso = aRecEstrazioneCudDett.cd_cds_compenso AND
                      cd_uo_compenso = aRecEstrazioneCudDett.cd_uo_compenso AND
                      esercizio_compenso = aRecEstrazioneCudDett.esercizio_compenso AND
                      pg_compenso = aRecEstrazioneCudDett.pg_compenso;
            END IF;

         END;

      END LOOP;

      CLOSE gen_cur;

   END;

END upgDatiCUDDettCori;

-- =================================================================================================
-- Estrazione dati per stampa CUD (Aggiornamento record in ESTRAZIONE_CUD_DETT da CONTRIBUTO_RITENUTA)
-- =================================================================================================
PROCEDURE upgDatiCUDDettCong
   (
    inEsercizio NUMBER,
    inRepID INTEGER
   ) IS

   aRecEstrazioneCudDett ESTRAZIONE_CUD_DETT%ROWTYPE;
   aRecConguaglio CONGUAGLIO%ROWTYPE;

   aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE;
   aRecAssCompensoConguaglio ASS_COMPENSO_CONGUAGLIO%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Recupero delle informazioni relativi ai conguagli. Evidenzio i compensi che sono anche conguagli
   -- riportando le informazioni prese dai conguagli stessi

   BEGIN

      OPEN gen_cur FOR

           SELECT A.cd_cds_compenso,
                  A.cd_uo_compenso,
                  A.esercizio_compenso,
                  A.pg_compenso,
                  B.cd_cds,
                  B.cd_unita_organizzativa,
                  B.esercizio,
                  B.pg_conguaglio,
                  B.dacr,
                  B.dt_da_competenza_esterno,
                  B.dt_a_competenza_esterno,
                  B.imponibile_fiscale_esterno,
                  B.im_irpef_esterno,
                  B.im_addreg_esterno,
                  B.im_addprov_esterno,
                  B.im_addcom_esterno,
                  B.detrazioni_la_esterno,
                  B.detrazioni_pe_esterno,
                  B.detrazioni_co_esterno,
                  B.detrazioni_fi_esterno,
                  B.detrazioni_al_esterno,
                  B.im_deduzione_dovuto,
                  B.im_deduzione_goduto,
                  B.im_irpef_dovuto,
                  B.im_addreg_dovuto,
                  B.im_addprov_dovuto,
                  B.im_addcom_dovuto,
                  B.detrazioni_la_dovuto,
                  B.detrazioni_pe_dovuto,
                  B.detrazioni_co_dovuto,
                  B.detrazioni_fi_dovuto,
                  B.detrazioni_al_dovuto,
                  B.im_irpef_goduto,
                  B.im_addreg_goduto,
                  B.im_addprov_goduto,
                  B.im_addcom_goduto,
                  B.detrazioni_la_goduto,
                  B.detrazioni_pe_goduto,
                  B.detrazioni_co_goduto,
                  B.detrazioni_fi_goduto,
                  B.detrazioni_al_goduto,
                  B.im_deduzione_family_dovuto,
                  B.im_deduzione_family_goduto,
                  B.detrazione_rid_cuneo_dovuto,
                  B.detrazione_rid_cuneo_goduto,
                  B.im_bonus_irpef_dovuto,
                  B.im_bonus_irpef_goduto,
                  B.im_credito_irpef_dovuto,
                  B.im_credito_irpef_goduto
           FROM   ESTRAZIONE_CUD_DETT A, CONGUAGLIO B
           WHERE  A.id_estrazione = inRepID AND
                  A.esercizio = inEsercizio AND
                  A.fl_compenso_conguaglio = 'Y' AND
                  B.cd_cds_compenso = A.cd_cds_compenso AND
                  B.cd_uo_compenso = A.cd_uo_compenso AND
                  B.esercizio_compenso = A.esercizio_compenso AND
                  B.pg_compenso = A.pg_compenso;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCudDett.cd_cds_compenso,
               aRecEstrazioneCudDett.cd_uo_compenso,
               aRecEstrazioneCudDett.esercizio_compenso,
               aRecEstrazioneCudDett.pg_compenso,
               aRecConguaglio.cd_cds,
               aRecConguaglio.cd_unita_organizzativa,
               aRecConguaglio.esercizio,
               aRecConguaglio.pg_conguaglio,
               aRecConguaglio.dacr,
               aRecConguaglio.dt_da_competenza_esterno,
               aRecConguaglio.dt_a_competenza_esterno,
               aRecConguaglio.imponibile_fiscale_esterno,
               aRecConguaglio.im_irpef_esterno,
               aRecConguaglio.im_addreg_esterno,
               aRecConguaglio.im_addprov_esterno,
               aRecConguaglio.im_addcom_esterno,
               aRecConguaglio.detrazioni_la_esterno,
               aRecConguaglio.detrazioni_pe_esterno,
               aRecConguaglio.detrazioni_co_esterno,
               aRecConguaglio.detrazioni_fi_esterno,
               aRecConguaglio.detrazioni_al_esterno,
               aRecConguaglio.im_deduzione_dovuto,
               aRecConguaglio.im_deduzione_goduto,
               aRecConguaglio.im_irpef_dovuto,
               aRecConguaglio.im_addreg_dovuto,
               aRecConguaglio.im_addprov_dovuto,
               aRecConguaglio.im_addcom_dovuto,
               aRecConguaglio.detrazioni_la_dovuto,
               aRecConguaglio.detrazioni_pe_dovuto,
               aRecConguaglio.detrazioni_co_dovuto,
               aRecConguaglio.detrazioni_fi_dovuto,
               aRecConguaglio.detrazioni_al_dovuto,
               aRecConguaglio.im_irpef_goduto,
               aRecConguaglio.im_addreg_goduto,
               aRecConguaglio.im_addprov_goduto,
               aRecConguaglio.im_addcom_goduto,
               aRecConguaglio.detrazioni_la_goduto,
               aRecConguaglio.detrazioni_pe_goduto,
               aRecConguaglio.detrazioni_co_goduto,
               aRecConguaglio.detrazioni_fi_goduto,
               aRecConguaglio.detrazioni_al_goduto,
               aRecConguaglio.im_deduzione_family_dovuto,
               aRecConguaglio.im_deduzione_family_goduto,
               aRecConguaglio.detrazione_rid_cuneo_dovuto,
               aRecConguaglio.detrazione_rid_cuneo_goduto,
               aRecConguaglio.im_bonus_irpef_dovuto,
               aRecConguaglio.im_bonus_irpef_goduto,
               aRecConguaglio.im_credito_irpef_dovuto,
               aRecConguaglio.im_credito_irpef_goduto;
         EXIT WHEN gen_cur%NOTFOUND;

         BEGIN

            UPDATE ESTRAZIONE_CUD_DETT
            SET    fl_incluso_in_conguaglio = 'Y',
                   dacr_conguaglio = aRecConguaglio.dacr,
                   cd_cds_conguaglio = aRecConguaglio.cd_cds,
                   cd_uo_conguaglio = aRecConguaglio.cd_unita_organizzativa,
                   esercizio_conguaglio = aRecConguaglio.esercizio,
                   pg_conguaglio = aRecConguaglio.pg_conguaglio,
                   dt_cmp_da_esterno = aRecConguaglio.dt_da_competenza_esterno,
                   dt_cmp_a_esterno = aRecConguaglio.dt_a_competenza_esterno,
                   imponibile_fiscale_esterno = aRecConguaglio.imponibile_fiscale_esterno,
                   im_irpef_esterno = aRecConguaglio.im_irpef_esterno,
                   im_addreg_esterno = aRecConguaglio.im_addreg_esterno,
                   im_addpro_esterno = aRecConguaglio.im_addprov_esterno,
                   im_addcom_esterno = aRecConguaglio.im_addcom_esterno,
                   detraz_pe_esterno = aRecConguaglio.detrazioni_pe_esterno,
                   detraz_la_esterno = aRecConguaglio.detrazioni_la_esterno,
                   detraz_co_esterno = aRecConguaglio.detrazioni_co_esterno,
                   detraz_fi_esterno = aRecConguaglio.detrazioni_fi_esterno,
                   detraz_al_esterno = aRecConguaglio.detrazioni_al_esterno,
                   im_deduzione_dovuto_conguaglio = aRecConguaglio.im_deduzione_dovuto,
                   im_deduzione_goduto_conguaglio = aRecConguaglio.im_deduzione_goduto,
                   im_ritenute_irpef_dovuto_cong = aRecConguaglio.im_irpef_dovuto,
                   im_addreg_dovuto_cong = aRecConguaglio.im_addreg_dovuto,
                   im_addpro_dovuto_cong = aRecConguaglio.im_addprov_dovuto,
                   im_addcom_dovuto_cong = aRecConguaglio.im_addcom_dovuto,
                   detraz_pe_dovuto_conguaglio = aRecConguaglio.detrazioni_pe_dovuto,
                   detraz_la_dovuto_conguaglio = aRecConguaglio.detrazioni_la_dovuto,
                   detraz_co_dovuto_conguaglio = aRecConguaglio.detrazioni_co_dovuto,
                   detraz_fi_dovuto_conguaglio = aRecConguaglio.detrazioni_fi_dovuto,
                   detraz_al_dovuto_conguaglio = aRecConguaglio.detrazioni_al_dovuto,
                   im_ritenute_irpef_goduto_cong = aRecConguaglio.im_irpef_goduto,
                   im_addreg_goduto_cong = aRecConguaglio.im_addreg_goduto,
                   im_addpro_goduto_cong = aRecConguaglio.im_addprov_goduto,
                   im_addcom_goduto_cong = aRecConguaglio.im_addcom_goduto,
                   detraz_pe_goduto_conguaglio = aRecConguaglio.detrazioni_pe_goduto,
                   detraz_la_goduto_conguaglio = aRecConguaglio.detrazioni_la_goduto,
                   detraz_co_goduto_conguaglio = aRecConguaglio.detrazioni_co_goduto,
                   detraz_fi_goduto_conguaglio = aRecConguaglio.detrazioni_fi_goduto,
                   detraz_al_goduto_conguaglio = aRecConguaglio.detrazioni_al_goduto,
                   im_ded_family_dovuto_cong = aRecConguaglio.im_deduzione_family_dovuto,
                       im_ded_family_goduto_cong = aRecConguaglio.im_deduzione_family_goduto,
                       im_bonus_dovuto_cong = aRecConguaglio.im_bonus_irpef_dovuto,
                       im_bonus_goduto_cong = aRecConguaglio.im_bonus_irpef_goduto,
                       im_credito_irpef_dovuto = aRecConguaglio.im_credito_irpef_dovuto,
                       im_credito_irpef_goduto = aRecConguaglio.im_credito_irpef_goduto,
                       im_detr_rid_cuneo_dovuto_cong = aRecConguaglio.detrazione_rid_cuneo_dovuto,
                       im_detr_rid_cuneo_goduto_cong = aRecConguaglio.detrazione_rid_cuneo_goduto
            WHERE  id_estrazione = inRepID AND
                   esercizio = inEsercizio AND
                   cd_cds_compenso = aRecEstrazioneCudDett.cd_cds_compenso AND
                   cd_uo_compenso = aRecEstrazioneCudDett.cd_uo_compenso AND
                   esercizio_compenso = aRecEstrazioneCudDett.esercizio_compenso AND
                   pg_compenso = aRecEstrazioneCudDett.pg_compenso;

         END;

      END LOOP;

      CLOSE gen_cur;

   END;

   -------------------------------------------------------------------------------------------------
   -- Recupero delle informazioni relativi ai conguagli. Evidenzio i compensi che sono stati compresi
   -- in un conguaglio siano essi compensi-congualio o no

   BEGIN

      OPEN gen_cur FOR

           SELECT A.cd_cds_compenso,
                  A.cd_uo_compenso,
                  A.esercizio_compenso,
                  A.pg_compenso,
                  B.cd_cds_conguaglio,
                  B.cd_uo_conguaglio,
                  B.esercizio_conguaglio,
                  B.pg_conguaglio
           FROM   ESTRAZIONE_CUD_DETT A, ASS_COMPENSO_CONGUAGLIO B
           WHERE  A.id_estrazione = inRepID AND
                  A.esercizio = inEsercizio AND
                  A.fl_compenso_conguaglio != 'Y' AND
                  B.cd_cds_compenso = A.cd_cds_compenso AND
                  B.cd_uo_compenso = A.cd_uo_compenso AND
                  B.esercizio_compenso = A.esercizio_compenso AND
                  B.pg_compenso = A.pg_compenso AND
                  EXISTS
                     (SELECT 1
                      FROM   CONGUAGLIO C, ESTRAZIONE_CUD_DETT D
                      WHERE  C.cd_cds = B.cd_cds_conguaglio AND
                             C.cd_unita_organizzativa = B.cd_uo_conguaglio AND
                             C.esercizio = B.esercizio_conguaglio AND
                             C.pg_conguaglio = B.pg_conguaglio AND
                             D.id_estrazione = inRepID AND
                             D.esercizio = inEsercizio AND
                             D.fl_compenso_conguaglio = 'Y' AND
                             D.cd_cds_compenso = C.cd_cds_compenso AND
                             D.cd_uo_compenso = C.cd_uo_compenso AND
                             D.esercizio_compenso = C.esercizio_compenso AND
                             D.pg_compenso = C.pg_compenso);


      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCudDett.cd_cds_compenso,
               aRecEstrazioneCudDett.cd_uo_compenso,
               aRecEstrazioneCudDett.esercizio_compenso,
               aRecEstrazioneCudDett.pg_compenso,
               aRecAssCompensoConguaglio.cd_cds_conguaglio,
               aRecAssCompensoConguaglio.cd_uo_conguaglio,
               aRecAssCompensoConguaglio.esercizio_conguaglio,
               aRecAssCompensoConguaglio.pg_conguaglio;

         EXIT WHEN gen_cur%NOTFOUND;

         BEGIN

            UPDATE ESTRAZIONE_CUD_DETT
            SET    fl_incluso_in_conguaglio = 'Y',
                   cd_cds_conguaglio = aRecAssCompensoConguaglio.cd_cds_conguaglio,
                   cd_uo_conguaglio = aRecAssCompensoConguaglio.cd_uo_conguaglio,
                   esercizio_conguaglio = aRecAssCompensoConguaglio.esercizio_conguaglio,
                   pg_conguaglio = aRecAssCompensoConguaglio.pg_conguaglio
            WHERE  id_estrazione = inRepID AND
                   esercizio = inEsercizio AND
                   cd_cds_compenso = aRecEstrazioneCudDett.cd_cds_compenso AND
                   cd_uo_compenso = aRecEstrazioneCudDett.cd_uo_compenso AND
                   esercizio_compenso = aRecEstrazioneCudDett.esercizio_compenso AND
                   pg_compenso = aRecEstrazioneCudDett.pg_compenso;

         END;

      END LOOP;

      CLOSE gen_cur;

   END;

   -------------------------------------------------------------------------------------------------
   -- Definisco, se vi sono conguagli, l'ultimo costruito temporalmente

   BEGIN

      OPEN gen_cur FOR

           SELECT A.cd_cds_compenso,
                  A.cd_uo_compenso,
                  A.esercizio_compenso,
                  A.pg_compenso
           FROM   ESTRAZIONE_CUD_DETT A
           WHERE  A.id_estrazione = inRepID AND
                  A.esercizio = inEsercizio AND
                  A.fl_compenso_conguaglio = 'Y' AND
                  (A.cd_anag, A.dacr_conguaglio) =
                      (SELECT B.cd_anag, MAX(B.dacr_conguaglio)
                       FROM   ESTRAZIONE_CUD_DETT B
                       WHERE  B.id_estrazione = A.id_estrazione AND
                              B.esercizio = A.esercizio AND
                              B.fl_compenso_conguaglio = A.fl_compenso_conguaglio AND
                              B.cd_anag = A.cd_anag
                       GROUP BY B.cd_anag);

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCudDett.cd_cds_compenso,
               aRecEstrazioneCudDett.cd_uo_compenso,
               aRecEstrazioneCudDett.esercizio_compenso,
               aRecEstrazioneCudDett.pg_compenso;

         EXIT WHEN gen_cur%NOTFOUND;

         BEGIN

            UPDATE ESTRAZIONE_CUD_DETT
            SET    fl_ultimo_conguaglio = 'Y',
                   fl_incluso_in_conguaglio = 'N'
            WHERE  id_estrazione = inRepID AND
                   esercizio = inEsercizio AND
                   cd_cds_compenso = aRecEstrazioneCudDett.cd_cds_compenso AND
                   cd_uo_compenso = aRecEstrazioneCudDett.cd_uo_compenso AND
                   esercizio_compenso = aRecEstrazioneCudDett.esercizio_compenso AND
                   pg_compenso = aRecEstrazioneCudDett.pg_compenso;

         END;

      END LOOP;

      CLOSE gen_cur;

   END;

END upgDatiCUDDettCong;

-- =================================================================================================
-- Aggiornamento annotazioni CUD
-- =================================================================================================
PROCEDURE inserisciCUDAnnotazioni
   (
    inRepID INTEGER,
    inEsercizio NUMBER,
    aCdAnag NUMBER,
    aPgOrdineAnnotazione INTEGER,
    aPgTipoAnnotazione INTEGER,
    aCdTipologiaReddito VARCHAR2,
    aDataRifDa DATE,
    aDsTipologiaReddito VARCHAR2,
    aImponibile NUMBER,
    aDataRifA DATE,
    inUtente VARCHAR2
   ) IS

BEGIN

   BEGIN

      INSERT INTO ESTRAZIONE_CUD_ANNOTAZIONI
             (id_estrazione,
              esercizio,
              dip_cd_anag,
              pg_ordine_annotazione,
              pg_tipo_annotazione,
              cd_tipologia_reddito,
              data_rif_da,
              ds_tipologia_reddito,
              imponibile,
              data_rif_a,
              dacr,
              utcr,
              duva,
              utuv,
              pg_ver_rec)
      VALUES (inRepID,
              inEsercizio,
              aCdAnag,
              aPgOrdineAnnotazione,
              aPgTipoAnnotazione,
              aCdTipologiaReddito,
              aDataRifDa,
              aDsTipologiaReddito,
              aImponibile,
              aDataRifA,
              dataOdierna,
              inUtente,
              dataOdierna,
              inUtente,
              1);

   EXCEPTION

      WHEN dup_val_on_index THEN

           IF aPgOrdineAnnotazione = 1 THEN

              UPDATE ESTRAZIONE_CUD_ANNOTAZIONI
              SET    imponibile = imponibile + aImponibile
              WHERE  id_estrazione = inRepID AND
                     esercizio = inEsercizio AND
                     dip_cd_anag = aCdAnag AND
                     pg_ordine_annotazione = aPgOrdineAnnotazione AND
                     pg_tipo_annotazione = aPgTipoAnnotazione AND
                     cd_tipologia_reddito = aCdTipologiaReddito AND
                     data_rif_da = aDataRifDa;

           END IF;

   END;

END inserisciCUDAnnotazioni;

-- =================================================================================================
-- Aggregazione dati in ESTRAZIONE_CUD
-- =================================================================================================
PROCEDURE aggregaDatiCUD
   (
    inEsercizio NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   ok_data_stampa DATE;
   aStringaTelFax VARCHAR2(500);

   aImponibile NUMBER(15,2);
   aPgOrdineAnnotazione INTEGER;
   aDataRifDa DATE;
   aDataRifA DATE;
   aCdTipologiaRedditoDef VARCHAR2(10);
   aDsTipologiaRedditoDef VARCHAR2(300);

   memCdAnag ESTRAZIONE_CUD_DETT.cd_anag%TYPE;
   isScritto CHAR(1);

   mem_imponibile_si_detr NUMBER(15,2);

   aRecEstrazioneCudDett ESTRAZIONE_CUD_DETT%ROWTYPE;
   aRecEstrazioneCud ESTRAZIONE_CUD%ROWTYPE;

   add_reg_anno_prec NUMBER(15,2);
   add_com_anno_prec NUMBER(15,2);

   i BINARY_INTEGER;
   isCervello VARCHAR2(1);
   isRientroLav30 VARCHAR2(1);
   isRientroLav20 VARCHAR2(1);
   imponibile_non_tassato  NUMBER(15,2):=0;
   detrazione_rid_cuneo  NUMBER(15,2):=0;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazione variabili

   memCdAnag:=NULL;

   -- Data stampa CUD

   IF TO_CHAR(dataOdierna, 'YYYY') = inEsercizio THEN
      ok_data_stampa:=TRUNC(dataOdierna);
   ELSE
      IF TRUNC(dataOdierna) >= TO_DATE('1503' || TO_CHAR(dataOdierna, 'YYYY'), 'DDMMYYYY') THEN
          ok_data_stampa:=TO_DATE('1503' || TO_CHAR(dataOdierna, 'YYYY') ,'DDMMYYYY');
      ELSE
          ok_data_stampa:=TRUNC(dataOdierna);
      END IF;
   END IF;

   -- Stringa telefono fax

   aStringaTelFax:=NULL;
   IF parametri_tab(7).stringa IS NOT NULL THEN
      aStringaTelFax:='T ' || parametri_tab(7).stringa || ' ';
   END IF;
   IF parametri_tab(8).stringa IS NOT NULL THEN
      aStringaTelFax:=aStringaTelFax || 'F ' || parametri_tab(8).stringa;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Aggregazione dati di ESTRAZIONE_CUD_DETT in ESTRAZIONE_CUD. Si elaborano i soli importi relativi
   -- agli imponibili fiscali, distinti per l'associazione o meno a detrazioni, e gli acconti IRPEF.
   -- Si evidenzia anche il dettaglio per codice e descrizione tipologia reddito per la costruzione del
   -- dettaglio da esporre nelle annotazioni.

   BEGIN

      -- Si determina il valore della tipologia delle annotazioni

      aPgOrdineAnnotazione:=1;
      aDataRifDa:=TO_DATE('01011900','DDMMYYYY');
      aDataRifA:=NULL;

      -- Si evidenzia anche il dettaglio per codice e descrizione tipologia reddito

      OPEN gen_cur FOR

           SELECT A.cd_anag,
                  A.cd_terzo,
                  A.fl_tassazione_separata,
                  A.cd_tipologia_reddito,
                  A.ds_tipologia_reddito,
                  A.fl_ritenuta_non_residenti,
                  A.cd_categoria,
                  SUM(DECODE(A.fl_detrazioni,'Y', A.imponibile_fisc_lordo_contrib, 0)),
                  SUM(DECODE(A.fl_detrazioni,'Y', 0, A.imponibile_fisc_lordo_contrib)),
                  SUM(A.primo_acconto_irpef_contrib),
                  SUM(A.secondo_acconto_irpef_contrib),
                  SUM(A.detrazione_rid_cuneo_netto),
                  0,
                  0,
                  0
           FROM   ESTRAZIONE_CUD_DETT A
           WHERE  A.id_estrazione = inRepID AND
                  A.esercizio = inEsercizio AND
                  A.fl_compenso_conguaglio = 'N'
           GROUP BY A.cd_anag,
                    A.cd_terzo,
                    A.fl_tassazione_separata,
                    A.cd_tipologia_reddito,
                    A.ds_tipologia_reddito,
                    A.fl_ritenuta_non_residenti,
                    A.cd_categoria,
                    0,
                    0,
                    0
           ORDER BY A.cd_anag,
                    A.cd_terzo,
                    A.fl_tassazione_separata,
                    A.cd_tipologia_reddito,
                    A.ds_tipologia_reddito,
                    A.fl_ritenuta_non_residenti,
                    A.cd_categoria;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCudDett.cd_anag,
               aRecEstrazioneCudDett.cd_terzo,
               aRecEstrazioneCudDett.fl_tassazione_separata,
               aRecEstrazioneCudDett.cd_tipologia_reddito,
               aRecEstrazioneCudDett.ds_tipologia_reddito,
               aRecEstrazioneCudDett.fl_ritenuta_non_residenti,
               aRecEstrazioneCudDett.cd_categoria,
               aRecEstrazioneCud.imponibile_si_detr,
               aRecEstrazioneCud.imponibile_no_detr,
               aRecEstrazioneCud.primo_acconto_irpef,
               aRecEstrazioneCud.secondo_acconto_irpef,
               aRecEstrazioneCud.ult_detr_cuneo_fisc,
               aRecEstrazioneCud.imponibile_si_detr_tassep,
               aRecEstrazioneCud.imponibile_no_detr_tassep,
               aRecEstrazioneCud.imponibile_non_residenti;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Primo anagrafico in elaborazione

         IF memCdAnag IS NULL THEN
            memCdAnag:=aRecEstrazioneCudDett.cd_anag;
            isScritto:='N';
         END IF;

         -- Il codice anagrafico in lettura è diverso dal precedente

         IF memCdAnag != aRecEstrazioneCudDett.cd_anag THEN
            memCdAnag:=aRecEstrazioneCudDett.cd_anag;
            isScritto:='N';
         END IF;

         -- Gestione imponibile tassazione separata

         IF aRecEstrazioneCudDett.fl_tassazione_separata = 'Y' THEN
            aRecEstrazioneCud.imponibile_si_detr_tassep:=aRecEstrazioneCud.imponibile_si_detr;
            aRecEstrazioneCud.imponibile_si_detr:=0;
            aRecEstrazioneCud.imponibile_no_detr_tassep:=aRecEstrazioneCud.imponibile_no_detr;
            aRecEstrazioneCud.imponibile_no_detr:=0;
         END IF;

      -- Gestione per residenti esteri

         IF aRecEstrazioneCudDett.fl_ritenuta_non_residenti = 'Y' Then
            If aRecEstrazioneCud.imponibile_si_detr != 0 Then
               aRecEstrazioneCud.imponibile_non_residenti:=aRecEstrazioneCud.imponibile_si_detr;
               aRecEstrazioneCud.imponibile_si_detr:=0;
            Else
               aRecEstrazioneCud.imponibile_non_residenti:=aRecEstrazioneCud.imponibile_no_detr;
               aRecEstrazioneCud.imponibile_no_detr:=0;
            End If;
         END IF;

         -- Aggiornamento dati da ESTRAZIONE_CUD_DETT su ESTRAZIONE_CUD inclusi dati di testata

         BEGIN

            IF isScritto = 'N' THEN
               isScritto:= 'Y';

                   -- i campi successivi non sono più utilizzati, quindi li valorizzo a zero
                   add_reg_anno_prec := 0;
               add_com_anno_prec := 0;

               INSERT INTO ESTRAZIONE_CUD
                      (id_estrazione,
                       esercizio,
                       datlav_codice_fiscale,
                       datlav_nome,
                       datlav_cognome,
                       datlav_comune,
                       datlav_provincia,
                       datlav_cap,
                       datlav_indirizzo,
                       datlav_telefono,
                       datlav_mail,
                       dip_cd_anag,
                       dip_rp_reddito_complessivo,
                       dip_rp_no_tax_area,
                       dip_rp_deduzione_fissa_intera,
                       imponibile_si_detr,
                       imponibile_no_detr,
                       fl_applic_aliquota_max,
                       fl_no_applic_deduzione,
                       gg_periodo_lavoro,
                       ritenute_irpef,
                       addizionale_regionale,
                       addizionale_comunale,
                       primo_acconto_irpef,
                       secondo_acconto_irpef,
                       deduzione_dovuta,
                       imponibile_irpef,
                       imposta_lorda,
                       detrazioni_lavoro_dip,
                       detrazioni_familiari,
                       totale_detrazioni,
                       imponibile_cong_si_detr,
                       imponibile_cong_no_detr,
                       rigo_cococo,
                       dt_inizio_cococo,
                       dt_fine_cococo,
                       compensi_corrisposti_cococo,
                       contributi_dovuti_cococo,
                       contributi_trattenuti_cococo,
                       contributi_versati_cococo,
                       imponibile_si_detr_tassep,
                       imponibile_no_detr_tassep,
                       ritenute_irpef_tassep,
                       annotazioni_2,
                       data_stampa,
                       firma_datlav,
                       fl_ha_conguagli,
                       fl_soggetto_estero,
                       add_reg_accantonato,
                       add_com_accantonato,
                       dacr,
                       utcr,
                       duva,
                       utuv,
                       pg_ver_rec,
                       cnr_data_assunzione,
                       cnr_data_cessazione,
                       cnr_detrazione_coniuge,
                       cnr_detrazione_figli,
                       cnr_detrazioni_tassep,
                       cnr_gg_periodo_tassep,
                       cnr_tipologia_reddito_punto_1,
                       cnr_tempo_determinato,
                       cnr_tipologia_reddito_punto_2,
                       cnr_deduzione_fissa_3000,
                       cnr_deduzione_fissa_4500,
                       cnr_posizione_inail,
                       add_reg_anno_precedente,
                       add_com_anno_precedente,
                       im_deduzione_family,
                       imponibile_non_residenti,
                       ritenute_non_residenti,
                       im_irpef_sospeso,
                       im_redd_non_tassati_per_conv,
                       im_redd_esenti_per_legge,
                       im_bonus_erogato,
                       cd_categoria,
                       ult_detr_cuneo_fisc,
                       tratt_int_erog_cuneo_fisc,
                       tratt_int_cong_cuneo_fisc)
               VALUES (inRepID,
                       inEsercizio ,
                       parametri_tab(2).stringa,
                       TO_CHAR(NULL),
                       parametri_tab(1).stringa,
                       SUBSTR(parametri_tab(3).stringa,1,60),
                       SUBSTR(parametri_tab(4).stringa,1,4),
                       SUBSTR(parametri_tab(5).stringa,1,5),
                       parametri_tab(6).stringa,
                       SUBSTR(aStringaTelFax, 1, 50),
                       SUBSTR(parametri_tab(8).stringa,1,50),
                       aRecEstrazioneCudDett.cd_anag,
                       0,
                       'N',
                       'N',
                       aRecEstrazioneCud.imponibile_si_detr,
                       aRecEstrazioneCud.imponibile_no_detr,
                       'N',
                       'N',
                       0,
                       0,
                       0,
                       0,
                       aRecEstrazioneCud.primo_acconto_irpef,
                       aRecEstrazioneCud.secondo_acconto_irpef,
                       0,
                       0,
                       0,
                       0,
                       0,
                       0,
                       0,
                       0,
                       1,
                       TO_DATE(NULL),
                       TO_DATE(NULL),
                       0,
                       0,
                       0,
                       0,
                       aRecEstrazioneCud.imponibile_si_detr_tassep,
                       aRecEstrazioneCud.imponibile_no_detr_tassep,
                       0,
                       parametri_tab(12).stringa,
                       ok_data_stampa,
                       parametri_tab(10).stringa,
                       'N',
                       'N',
                       0,
                       0,
                       dataOdierna,
                       inUtente,
                       dataOdierna,
                       inUtente,
                       1,
                       TO_DATE(NULL),
                       TO_DATE(NULL),
                       0,
                       0,
                       0,
                       0,
                       TO_CHAR(NULL),
                       TO_CHAR(NULL),
                       TO_CHAR(NULL),
                       0,
                       0,
                       TO_CHAR(NULL),
                       add_reg_anno_prec,
                       add_com_anno_prec,
                       0,
                       aRecEstrazioneCud.imponibile_non_residenti,
                       0,
                       0,
                       0,
                       0,
                       0,
                       aRecEstrazioneCudDett.cd_categoria,
                       0,
                       0,
                       0);
            ELSE

               UPDATE ESTRAZIONE_CUD
               SET    imponibile_si_detr = imponibile_si_detr + aRecEstrazioneCud.imponibile_si_detr,
                      imponibile_no_detr = imponibile_no_detr + aRecEstrazioneCud.imponibile_no_detr,
                      primo_acconto_irpef = primo_acconto_irpef + aRecEstrazioneCud.primo_acconto_irpef,
                      secondo_acconto_irpef = secondo_acconto_irpef + aRecEstrazioneCud.secondo_acconto_irpef,
                      imponibile_si_detr_tassep = imponibile_si_detr_tassep + aRecEstrazioneCud.imponibile_si_detr_tassep,
                      imponibile_no_detr_tassep = imponibile_no_detr_tassep + aRecEstrazioneCud.imponibile_no_detr_tassep,
                      imponibile_non_residenti = imponibile_non_residenti + aRecEstrazioneCud.imponibile_non_residenti
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio AND
                      dip_cd_anag = aRecEstrazioneCudDett.cd_anag AND
                      rigo_cococo = 1;

            END IF;

         END;

         -- Aggiornamento dati da ESTRAZIONE_CUD_DETT su ESTRAZIONE_CUD_ANNOTAZIONI

         BEGIN

            FOR i IN 1 .. 2

            LOOP

               IF    i = 1 THEN
                     aImponibile:=aRecEstrazioneCud.imponibile_si_detr;
               ELSIF i = 2 THEN
                     aImponibile:=aRecEstrazioneCud.imponibile_no_detr;
               END IF;

               IF aImponibile != 0 THEN
                  inserisciCUDAnnotazioni(inRepID,
                                          inEsercizio,
                                          aRecEstrazioneCudDett.cd_anag,
                                          aPgOrdineAnnotazione,
                                          i,
                                          aRecEstrazioneCudDett.cd_tipologia_reddito,
                                          aDataRifDa,
                                          aRecEstrazioneCudDett.ds_tipologia_reddito,
                                          aImponibile,
                                          aDataRifA,
                                          inUtente);
               END IF;

            END LOOP;

         END;

      END LOOP;

      CLOSE gen_cur;

   END;

   -- Se il terzo in esame è un cervellone, devo ricalcolare l'imponibile (leggendolo anche dal conguaglio)
   -- poichè il conguaglio stesso potrebbe averlo modificato
   -- Inoltre, sempre per i cervelloni, calcolo l'imponibile non tassato come il 90% del lordo percipiente (preso dai compensi)
   OPEN gen_cur FOR
           SELECT Distinct A.cd_anag
           FROM   ESTRAZIONE_CUD_DETT A
           WHERE  A.id_estrazione = inRepID AND
                  A.esercizio = inEsercizio;

   LOOP
         FETCH gen_cur Into
               aRecEstrazioneCudDett.cd_anag;

         Exit WHEN gen_cur%NOTFOUND;

         isCervello := isAnagCervello(inRepID, inEsercizio, aRecEstrazioneCudDett.cd_anag);
         isRientroLav30 := isAnagRientroLav30(inRepID, inEsercizio, aRecEstrazioneCudDett.cd_anag);
         isRientroLav20 := isAnagRientroLav20(inRepID, inEsercizio, aRecEstrazioneCudDett.cd_anag);

         If isCervello = 'N' and isRientroLav30 = 'N' and isRientroLav20 = 'N' Then
            Null;
         Else
--pipe.send_message('anag = '||aRecEstrazioneCudDett.cd_anag);
            -- calcolo l'imponibile
            Begin
               Select --Sum(DECODE(A.fl_detrazioni,'Y', Decode(A.fl_tassazione_separata,'N',A.imponibile_fisc_lordo_compenso, 0),0)),
                      --Sum(DECODE(A.fl_detrazioni,'Y', Decode(A.fl_tassazione_separata,'N',0,A.imponibile_fisc_lordo_compenso),0)),
                      --Sum(DECODE(A.fl_detrazioni,'Y', 0, Decode(A.fl_tassazione_separata,'N',A.imponibile_fisc_lordo_compenso, 0))),
                      --Sum(DECODE(A.fl_detrazioni,'Y', 0, Decode(A.fl_tassazione_separata,'N',0,A.imponibile_fisc_lordo_compenso)))
                      --Il conguaglio deve avere in configurazione_cud il flag fl_detrazioni = 'N' (ad es. per il calcolo
                      --dei giorni lavorati) e quindi l'imponibile che leggo dal conguaglio non può tener conto di tale flag
                      Sum(Decode(A.fl_tassazione_separata,'N',A.imponibile_fisc_lordo_compenso, 0)),
                      Sum(Decode(A.fl_tassazione_separata,'N',0,A.imponibile_fisc_lordo_compenso))
               Into aRecEstrazioneCud.imponibile_si_detr,
                    aRecEstrazioneCud.imponibile_si_detr_tassep
                    --aRecEstrazioneCud.imponibile_no_detr,
                    --aRecEstrazioneCud.imponibile_no_detr_tassep
               From   ESTRAZIONE_CUD_DETT A
               Where  A.id_estrazione = inRepID AND
                      A.esercizio = inEsercizio And
                      A.cd_anag = aRecEstrazioneCudDett.cd_anag And
                      --A.fl_compenso_conguaglio = 'N' And
                      (A.fl_incluso_in_conguaglio = 'N'
                        Or
                       A.fl_ultimo_conguaglio = 'Y');
            End;

            -- calcolo l'imponibile non tassato
            imponibile_non_tassato :=0;

            Begin
               Select NVL(Sum(A.im_lordo_percip_compenso), 0) - NVL(Sum(A.quota_esente_compenso), 0)
               Into imponibile_non_tassato --aRecEstrazioneCud.imponibile_non_tassato_cerv
               From   ESTRAZIONE_CUD_DETT A
               Where  A.id_estrazione = inRepID AND
                      A.esercizio = inEsercizio And
                      A.cd_anag = aRecEstrazioneCudDett.cd_anag And
                      A.fl_compenso_conguaglio = 'N';
            End;

            If isCervello = 'Y' then
                 aRecEstrazioneCud.imponibile_non_tassato_cerv := imponibile_non_tassato * 90/100;
                 aRecEstrazioneCud.imponi_non_tassato_rientro_lav := 0;
            elsif isRientroLav30 = 'Y' then
                 aRecEstrazioneCud.imponi_non_tassato_rientro_lav := imponibile_non_tassato * 70/100;
                 aRecEstrazioneCud.imponibile_non_tassato_cerv := 0;
            elsif isRientroLav20 = 'Y' then
                 aRecEstrazioneCud.imponi_non_tassato_rientro_lav := imponibile_non_tassato * 80/100;
                 aRecEstrazioneCud.imponibile_non_tassato_cerv := 0;
            end if;


            UPDATE ESTRAZIONE_CUD
               SET    imponibile_si_detr = aRecEstrazioneCud.imponibile_si_detr,
                      --imponibile_no_detr = aRecEstrazioneCud.imponibile_no_detr
                      imponibile_si_detr_tassep = imponibile_si_detr_tassep + aRecEstrazioneCud.imponibile_si_detr_tassep,
                      --imponibile_no_detr_tassep = imponibile_no_detr_tassep + aRecEstrazioneCud.imponibile_no_detr_tassep
                      imponibile_non_tassato_cerv = aRecEstrazioneCud.imponibile_non_tassato_cerv,
                      imponi_non_tassato_rientro_lav = aRecEstrazioneCud.imponi_non_tassato_rientro_lav
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio AND
                      dip_cd_anag = aRecEstrazioneCudDett.cd_anag AND
                      rigo_cococo = 1;
         End If;
  End Loop;

   -------------------------------------------------------------------------------------------------
   -- Aggregazione dati di ESTRAZIONE_CUD_DETT in ESTRAZIONE_CUD. Estrazione ritenute, detrazioni,
   -- deduzioni ed importi esterni.
   -- Sono gestite le seguenti regole:
   -- a) In caso di tassazione separata elabora solo l'ammontare IRPEF relativo
   -- b) In caso di non tassazione separata gli importi sono presi dai compensi se non associati ad
   --    alcun conguaglio altrimenti sono presi dal conguaglio. In questo caso:
   --    irpef lorda = CONGUAGLIO.im_irpef_dovuto
   --    detrazioni = CONGUAGLIO.detrazioni_XX_dovuto
   --    irpef netta = irpef lorda - detrazioni

   -- a) I dati esterni sono azzerati se non siamo in presenza di un ultimo conguaglio (per
   --    ora gestisco il solo imponibile fiscale esterno)

   -- b) Le detrazioni normali (non _dovute) sono azzerate se il compenso e un
   --    conguaglio o risulta incluso in un conguaglio.
   -- c) Le detrazioni dovute (conguaglio) sono azzerate se il compenso non e un
   --    conguaglio o se risulta incluso in un conguaglio.
   -- d) E' un caso particolare di compenso senza calcoli il cui valore deve essere
   --    considerato unan detrazione personale.
   -- e) Per le ritenute IRPEF + ADDREG + ADDCOM si fanno due calcoli:
   --    1) Sommo i dati di contributo e ritenuta azzerando i valori se negativi ed
   --       il compenso associato non e un conguaglio.
   --    2) Sommo i dati di contributo e ritenuta solo per i compensi non di tipo
   --       conguaglio e se non sono stati inclusi in un conguaglio + i dati da
   --       conguaglio solo se il compenso e un conguaglio e non e stato incluso
   --       in altro conguaglio

   BEGIN

      -- Si determina il valore della tipologia delle annotazioni

      aPgOrdineAnnotazione:=1;
      aDataRifDa:=TO_DATE('01011900','DDMMYYYY');
      aDataRifA:=NULL;
      aCdTipologiaRedditoDef:='ZZZZZZ';
      aDsTipologiaRedditoDef:='Imponibile esterno';

      -- Ciclo principale di aggiornamento

      OPEN gen_cur FOR

           SELECT cd_anag,
                  fl_detrazioni,
                  fl_compenso_conguaglio,
                  fl_incluso_in_conguaglio,
                  fl_ultimo_conguaglio,
                  fl_tassazione_separata,
                  fl_cococo,
                  im_lordo_percip_compenso,
                  quota_esente_compenso,
                  imponibile_previd_contrib,
                  im_previd_percip_contrib,
                  im_previd_ente_contrib,
                  im_deduzione_dovuto_conguaglio,
                  im_ritenute_irpef_dovuto_cong,
                  im_addreg_dovuto_cong,
                  im_addpro_dovuto_cong,
                  im_addcom_dovuto_cong,
                  detraz_pe_dovuto_conguaglio,
                  detraz_la_dovuto_conguaglio,
                  detraz_co_dovuto_conguaglio,
                  detraz_fi_dovuto_conguaglio,
                  detraz_al_dovuto_conguaglio,
                  imponibile_fiscale_esterno,
                  im_deduzione_compenso,
                  im_irpef_lordo_contrib,
                  im_irpef_netto_contrib,
                  im_addreg_contrib,
                  im_addpro_contrib,
                  im_addcom_contrib,
                  detraz_pe_netto_compenso,
                  detraz_la_netto_compenso,
                  detraz_co_netto_compenso,
                  detraz_fi_netto_compenso,
                  detraz_al_netto_compenso,
                  fl_escludi_qvaria_deduzione,
                  fl_intera_qfissa_deduzione,
                  im_deduzione_family,
                          im_ded_family_dovuto_cong,
                          fl_ritenuta_non_residenti,
                          im_irpef_sospeso,
                          im_redd_non_tassati_per_conv,
                  im_redd_esenti_per_legge,
                  im_bonus_erogato,
                  im_bonus_dovuto_cong,
                  im_rid_cuneo_erogato,
                  im_credito_irpef_dovuto,
                  im_credito_irpef_goduto,
                  im_detr_rid_cuneo_dovuto_cong
           FROM   ESTRAZIONE_CUD_DETT A
           WHERE  id_estrazione = inRepID AND
                  esercizio = inEsercizio;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCudDett.cd_anag,
               aRecEstrazioneCudDett.fl_detrazioni,
               aRecEstrazioneCudDett.fl_compenso_conguaglio,
               aRecEstrazioneCudDett.fl_incluso_in_conguaglio,
               aRecEstrazioneCudDett.fl_ultimo_conguaglio,
               aRecEstrazioneCudDett.fl_tassazione_separata,
               aRecEstrazioneCudDett.fl_cococo,
               aRecEstrazioneCudDett.im_lordo_percip_compenso,
               aRecEstrazioneCudDett.quota_esente_compenso,
               aRecEstrazioneCudDett.imponibile_previd_contrib,
               aRecEstrazioneCudDett.im_previd_percip_contrib,
               aRecEstrazioneCudDett.im_previd_ente_contrib,
               aRecEstrazioneCudDett.im_deduzione_dovuto_conguaglio,
               aRecEstrazioneCudDett.im_ritenute_irpef_dovuto_cong,
               aRecEstrazioneCudDett.im_addreg_dovuto_cong,
               aRecEstrazioneCudDett.im_addpro_dovuto_cong,
               aRecEstrazioneCudDett.im_addcom_dovuto_cong,
               aRecEstrazioneCudDett.detraz_pe_dovuto_conguaglio,
               aRecEstrazioneCudDett.detraz_la_dovuto_conguaglio,
               aRecEstrazioneCudDett.detraz_co_dovuto_conguaglio,
               aRecEstrazioneCudDett.detraz_fi_dovuto_conguaglio,
               aRecEstrazioneCudDett.detraz_al_dovuto_conguaglio,
               aRecEstrazioneCudDett.imponibile_fiscale_esterno,
               aRecEstrazioneCudDett.im_deduzione_compenso,
               aRecEstrazioneCudDett.im_irpef_lordo_contrib,
               aRecEstrazioneCudDett.im_irpef_netto_contrib,
               aRecEstrazioneCudDett.im_addreg_contrib,
               aRecEstrazioneCudDett.im_addpro_contrib,
               aRecEstrazioneCudDett.im_addcom_contrib,
               aRecEstrazioneCudDett.detraz_pe_netto_compenso,
               aRecEstrazioneCudDett.detraz_la_netto_compenso,
               aRecEstrazioneCudDett.detraz_co_netto_compenso,
               aRecEstrazioneCudDett.detraz_fi_netto_compenso,
               aRecEstrazioneCudDett.detraz_al_netto_compenso,
               aRecEstrazioneCudDett.fl_escludi_qvaria_deduzione,
               aRecEstrazioneCudDett.fl_intera_qfissa_deduzione,
               aRecEstrazioneCudDett.im_deduzione_family,
               aRecEstrazioneCudDett.im_ded_family_dovuto_cong,
               aRecEstrazioneCudDett.fl_ritenuta_non_residenti,
               aRecEstrazioneCudDett.im_irpef_sospeso,
               aRecEstrazioneCudDett.im_redd_non_tassati_per_conv,
               aRecEstrazioneCudDett.im_redd_esenti_per_legge,
               aRecEstrazioneCudDett.im_bonus_erogato,
               aRecEstrazioneCudDett.im_bonus_dovuto_cong,
               aRecEstrazioneCudDett.im_rid_cuneo_erogato,
               aRecEstrazioneCudDett.im_credito_irpef_dovuto,
               aRecEstrazioneCudDett.im_credito_irpef_goduto,
               aRecEstrazioneCudDett.im_detr_rid_cuneo_dovuto_cong;

         EXIT WHEN gen_cur%NOTFOUND;

         BEGIN

            -- Azzeramento variabili ---------------------------------------------------------------

            aRecEstrazioneCud.ritenute_irpef_tassep:=0;
            aRecEstrazioneCud.cnr_detrazioni_tassep:=0;

            aRecEstrazioneCud.imposta_lorda:=0;
            aRecEstrazioneCud.detrazioni_lavoro_dip:=0;
            aRecEstrazioneCud.detrazioni_familiari:=0;
            aRecEstrazioneCud.cnr_detrazione_coniuge:=0;
            aRecEstrazioneCud.cnr_detrazione_figli:=0;
            aRecEstrazioneCud.totale_detrazioni:=0;
            aRecEstrazioneCud.ritenute_irpef:=0;
            aRecEstrazioneCud.deduzione_dovuta:=0;
            aRecEstrazioneCud.im_deduzione_family:=0;

            aRecEstrazioneCud.addizionale_regionale:=0;
            aRecEstrazioneCud.addizionale_comunale:=0;

            aRecEstrazioneCud.imponibile_cong_si_detr:=0;
            aRecEstrazioneCud.imponibile_cong_no_detr:=0;

            aRecEstrazioneCud.imponibile_si_detr:=0;
            aRecEstrazioneCud.imponibile_no_detr:=0;

            aRecEstrazioneCud.compensi_corrisposti_cococo:=0;
            aRecEstrazioneCud.contributi_dovuti_cococo:=0;
            aRecEstrazioneCud.contributi_trattenuti_cococo:=0;
            aRecEstrazioneCud.contributi_versati_cococo:=0;

            aRecEstrazioneCud.dip_rp_deduzione_fissa_intera:='N';

                aRecEstrazioneCud.imponibile_non_residenti:=0;
                aRecEstrazioneCud.ritenute_non_residenti:=0;

                aRecEstrazioneCud.im_irpef_sospeso:=0;

                aRecEstrazioneCud.im_redd_non_tassati_per_conv := aRecEstrazioneCudDett.im_redd_non_tassati_per_conv;
                aRecEstrazioneCud.im_redd_esenti_per_legge := aRecEstrazioneCudDett.im_redd_esenti_per_legge;

                aRecEstrazioneCud.im_bonus_erogato:=0;
                aRecEstrazioneCud.ult_detr_cuneo_fisc:=0;
                aRecEstrazioneCud.tratt_int_erog_cuneo_fisc:=0;
                aRecEstrazioneCud.tratt_int_cong_cuneo_fisc:=0;

            -- Sistemazione dati estratti ----------------------------------------------------------


            -- In caso di NON RESIDENTI valorizzo il solo ammontare IRPEF.
            If aRecEstrazioneCudDett.fl_ritenuta_non_residenti = 'Y' THEN

               aRecEstrazioneCud.ritenute_non_residenti:=aRecEstrazioneCudDett.im_irpef_netto_contrib;

            -- In caso di tassazione separata valorizzo il solo ammontare IRPEF.
            -- Per il CNR aggiunta l'estrazione delle detrazioni
            Elsif aRecEstrazioneCudDett.fl_tassazione_separata = 'Y' THEN

               aRecEstrazioneCud.ritenute_irpef_tassep:=aRecEstrazioneCudDett.im_irpef_netto_contrib;

               -- Elaborazione compensi non inclusi in alcun conguaglio

               IF (aRecEstrazioneCudDett.fl_compenso_conguaglio = 'N' AND
                   aRecEstrazioneCudDett.fl_incluso_in_conguaglio = 'N') THEN
                  aRecEstrazioneCud.cnr_detrazioni_tassep:=aRecEstrazioneCudDett.detraz_pe_netto_compenso +
                                                           aRecEstrazioneCudDett.detraz_la_netto_compenso +
                                                           aRecEstrazioneCudDett.detraz_co_netto_compenso +
                                                           aRecEstrazioneCudDett.detraz_fi_netto_compenso +
                                                           aRecEstrazioneCudDett.detraz_al_netto_compenso;
               END IF;

            -- Gestione per non tassazione separata.
            -- Per CNR aggiunti i campi di detrazione coniuge e figli + altri

            Else

               -- Elaborazione compensi non inclusi in alcun conguaglio

               IF (aRecEstrazioneCudDett.fl_compenso_conguaglio = 'N' AND
                   aRecEstrazioneCudDett.fl_incluso_in_conguaglio = 'N') THEN
                  aRecEstrazioneCud.imposta_lorda:=aRecEstrazioneCudDett.im_irpef_lordo_contrib;
                  aRecEstrazioneCud.detrazioni_lavoro_dip:=aRecEstrazioneCudDett.detraz_pe_netto_compenso +
                                                           aRecEstrazioneCudDett.detraz_la_netto_compenso;
                  aRecEstrazioneCud.detrazioni_familiari:=aRecEstrazioneCudDett.detraz_co_netto_compenso +
                                                          aRecEstrazioneCudDett.detraz_fi_netto_compenso +
                                                          aRecEstrazioneCudDett.detraz_al_netto_compenso;
                  aRecEstrazioneCud.cnr_detrazione_coniuge:=aRecEstrazioneCudDett.detraz_co_netto_compenso;
                  aRecEstrazioneCud.cnr_detrazione_figli:=aRecEstrazioneCudDett.detraz_fi_netto_compenso +
                                                          aRecEstrazioneCudDett.detraz_al_netto_compenso;
                  aRecEstrazioneCud.totale_detrazioni:=aRecEstrazioneCud.detrazioni_lavoro_dip +
                                                       aRecEstrazioneCud.detrazioni_familiari;
                  aRecEstrazioneCud.ritenute_irpef:=aRecEstrazioneCudDett.im_irpef_netto_contrib;
                  aRecEstrazioneCud.deduzione_dovuta:=aRecEstrazioneCudDett.im_deduzione_compenso;
                  aRecEstrazioneCud.im_deduzione_family:=aRecEstrazioneCudDett.im_deduzione_family;
                  aRecEstrazioneCud.addizionale_regionale:=aRecEstrazioneCudDett.im_addreg_contrib;
                  aRecEstrazioneCud.addizionale_comunale:=aRecEstrazioneCudDett.im_addcom_contrib;
                  aRecEstrazioneCud.im_irpef_sospeso:=aRecEstrazioneCudDett.im_irpef_sospeso;
                  aRecEstrazioneCud.im_bonus_erogato:=aRecEstrazioneCudDett.im_bonus_erogato;

               END IF;

               -- Elaborazione compensi conguaglio (solo ultimo)

               IF (aRecEstrazioneCudDett.fl_compenso_conguaglio = 'Y' AND
                   aRecEstrazioneCudDett.fl_ultimo_conguaglio = 'Y') THEN
                  aRecEstrazioneCud.imposta_lorda:=aRecEstrazioneCudDett.im_ritenute_irpef_dovuto_cong;
                  aRecEstrazioneCud.detrazioni_lavoro_dip:=aRecEstrazioneCudDett.detraz_pe_dovuto_conguaglio +
                                                           aRecEstrazioneCudDett.detraz_la_dovuto_conguaglio;
                  aRecEstrazioneCud.detrazioni_familiari:=aRecEstrazioneCudDett.detraz_co_dovuto_conguaglio +
                                                          aRecEstrazioneCudDett.detraz_fi_dovuto_conguaglio +
                                                          aRecEstrazioneCudDett.detraz_al_dovuto_conguaglio;
                  aRecEstrazioneCud.cnr_detrazione_coniuge:=aRecEstrazioneCudDett.detraz_co_dovuto_conguaglio;
                  aRecEstrazioneCud.cnr_detrazione_figli:=aRecEstrazioneCudDett.detraz_fi_dovuto_conguaglio +
                                                          aRecEstrazioneCudDett.detraz_al_dovuto_conguaglio;
                  aRecEstrazioneCud.totale_detrazioni:=aRecEstrazioneCud.detrazioni_lavoro_dip +
                                                       aRecEstrazioneCud.detrazioni_familiari;
                  aRecEstrazioneCud.ritenute_irpef:=aRecEstrazioneCud.imposta_lorda -
                                                    aRecEstrazioneCud.totale_detrazioni;
                  aRecEstrazioneCud.deduzione_dovuta:=aRecEstrazioneCudDett.im_deduzione_dovuto_conguaglio;
                  aRecEstrazioneCud.im_deduzione_family:=aRecEstrazioneCudDett.im_ded_family_dovuto_cong;
                  aRecEstrazioneCud.addizionale_regionale:=aRecEstrazioneCudDett.im_addreg_dovuto_cong;
                  aRecEstrazioneCud.addizionale_comunale:=aRecEstrazioneCudDett.im_addcom_dovuto_cong;
                  aRecEstrazioneCud.im_irpef_sospeso:=aRecEstrazioneCudDett.im_irpef_sospeso;
                  aRecEstrazioneCud.im_bonus_erogato:=aRecEstrazioneCudDett.im_bonus_dovuto_cong;

                  aRecEstrazioneCud.ult_detr_cuneo_fisc:=aRecEstrazioneCudDett.im_detr_rid_cuneo_dovuto_cong;
                  aRecEstrazioneCud.tratt_int_erog_cuneo_fisc:=aRecEstrazioneCudDett.im_credito_irpef_dovuto;
                  if aRecEstrazioneCudDett.im_credito_irpef_goduto - aRecEstrazioneCudDett.im_credito_irpef_dovuto > 0 then
                    aRecEstrazioneCud.tratt_int_cong_cuneo_fisc:=aRecEstrazioneCudDett.im_credito_irpef_goduto - aRecEstrazioneCudDett.im_credito_irpef_dovuto;
                  else
                    aRecEstrazioneCud.tratt_int_cong_cuneo_fisc:=0;
                  end if;
                  IF aRecEstrazioneCudDett.fl_detrazioni = 'Y' THEN
                     aRecEstrazioneCud.imponibile_cong_si_detr:=aRecEstrazioneCudDett.imponibile_fiscale_esterno;
                     aRecEstrazioneCud.imponibile_si_detr:=aRecEstrazioneCudDett.imponibile_fiscale_esterno;
                  ELSE
                     aRecEstrazioneCud.imponibile_cong_no_detr:=aRecEstrazioneCudDett.imponibile_fiscale_esterno;
                     aRecEstrazioneCud.imponibile_no_detr:=aRecEstrazioneCudDett.imponibile_fiscale_esterno;
                  END IF;
                  IF aRecEstrazioneCudDett.fl_intera_qfissa_deduzione = 'Y' THEN
                     aRecEstrazioneCud.dip_rp_deduzione_fissa_intera:= 'Y';
                  END IF;
               END IF;

            End If;

            -- Dati relativi a cococo indifferenti alla gestione per tassazione separata

            IF (aRecEstrazioneCudDett.fl_compenso_conguaglio = 'N'
                --AND aRecEstrazioneCudDett.fl_cococo = 'Y'    -- ANCHE GLI ASSEGNI
                ) THEN
               IF aRecEstrazioneCudDett.im_previd_percip_contrib != 0 THEN
                  aRecEstrazioneCud.compensi_corrisposti_cococo:=aRecEstrazioneCudDett.imponibile_previd_contrib;
                  aRecEstrazioneCud.contributi_dovuti_cococo:=aRecEstrazioneCudDett.im_previd_percip_contrib +
                                                              aRecEstrazioneCudDett.im_previd_ente_contrib;
                  aRecEstrazioneCud.contributi_trattenuti_cococo:=aRecEstrazioneCudDett.im_previd_percip_contrib;
                  aRecEstrazioneCud.contributi_versati_cococo:=aRecEstrazioneCudDett.im_previd_percip_contrib +
                                                               aRecEstrazioneCudDett.im_previd_ente_contrib;
               END IF;
            END IF;


            -- Aggiornamento ESTRAZIONE_CUD --------------------------------------------------------

            IF (aRecEstrazioneCud.ritenute_irpef_tassep != 0 OR
                aRecEstrazioneCud.cnr_detrazioni_tassep != 0 OR
                aRecEstrazioneCud.imposta_lorda != 0 OR
                aRecEstrazioneCud.detrazioni_lavoro_dip != 0 OR
                aRecEstrazioneCud.detrazioni_familiari != 0 OR
                aRecEstrazioneCud.cnr_detrazione_coniuge != 0 OR
                aRecEstrazioneCud.cnr_detrazione_figli != 0 OR
                aRecEstrazioneCud.totale_detrazioni != 0 OR
                aRecEstrazioneCud.ritenute_irpef != 0 OR
                aRecEstrazioneCud.deduzione_dovuta != 0 Or
                aRecEstrazioneCud.im_deduzione_family != 0 OR
                aRecEstrazioneCud.addizionale_regionale != 0 OR
                aRecEstrazioneCud.addizionale_comunale != 0 OR
                aRecEstrazioneCud.imponibile_cong_si_detr != 0 OR
                aRecEstrazioneCud.imponibile_cong_no_detr != 0 OR
                aRecEstrazioneCud.imponibile_si_detr != 0 OR
                aRecEstrazioneCud.imponibile_no_detr != 0 OR
                aRecEstrazioneCud.compensi_corrisposti_cococo != 0 OR
                aRecEstrazioneCud.contributi_dovuti_cococo != 0 OR
                aRecEstrazioneCud.contributi_trattenuti_cococo != 0 OR
                aRecEstrazioneCud.contributi_versati_cococo != 0 Or
                aRecEstrazioneCud.ritenute_non_residenti != 0 or
                aRecEstrazioneCud.im_irpef_sospeso != 0 or
                aRecEstrazioneCud.im_redd_non_tassati_per_conv != 0 or
                aRecEstrazioneCud.im_redd_esenti_per_legge != 0 or
                aRecEstrazioneCud.im_bonus_erogato != 0 or
                aRecEstrazioneCud.ult_detr_cuneo_fisc != 0 or
                aRecEstrazioneCud.tratt_int_erog_cuneo_fisc != 0 or
                aRecEstrazioneCud.tratt_int_cong_cuneo_fisc != 0) THEN

               IF aRecEstrazioneCud.dip_rp_deduzione_fissa_intera = 'Y' THEN
                  UPDATE ESTRAZIONE_CUD
                  SET    imponibile_si_detr = imponibile_si_detr + aRecEstrazioneCud.imponibile_cong_si_detr,
                         imponibile_no_detr = imponibile_no_detr + aRecEstrazioneCud.imponibile_cong_no_detr,
                         ritenute_irpef = ritenute_irpef + aRecEstrazioneCud.ritenute_irpef,
                         ritenute_irpef_tassep = ritenute_irpef_tassep + aRecEstrazioneCud.ritenute_irpef_tassep,
                         addizionale_regionale = addizionale_regionale + aRecEstrazioneCud.addizionale_regionale,
                         addizionale_comunale = addizionale_comunale + aRecEstrazioneCud.addizionale_comunale,
                         deduzione_dovuta = deduzione_dovuta + aRecEstrazioneCud.deduzione_dovuta,
                         im_deduzione_family = im_deduzione_family + aRecEstrazioneCud.im_deduzione_family,
                         detrazioni_lavoro_dip = detrazioni_lavoro_dip + aRecEstrazioneCud.detrazioni_lavoro_dip,
                         detrazioni_familiari = detrazioni_familiari + aRecEstrazioneCud.detrazioni_familiari,
                         totale_detrazioni = totale_detrazioni + aRecEstrazioneCud.totale_detrazioni,
                         imponibile_cong_si_detr = imponibile_cong_si_detr + aRecEstrazioneCud.imponibile_cong_si_detr,
                         imponibile_cong_no_detr = imponibile_cong_no_detr + aRecEstrazioneCud.imponibile_cong_no_detr,
                         imposta_lorda = imposta_lorda + aRecEstrazioneCud.imposta_lorda,
                         compensi_corrisposti_cococo = compensi_corrisposti_cococo +
                                                       aRecEstrazioneCud.compensi_corrisposti_cococo,
                         contributi_dovuti_cococo = contributi_dovuti_cococo +
                                                    aRecEstrazioneCud.contributi_dovuti_cococo,
                         contributi_trattenuti_cococo = contributi_trattenuti_cococo +
                                                        aRecEstrazioneCud.contributi_trattenuti_cococo,
                         contributi_versati_cococo = contributi_versati_cococo +
                                                     aRecEstrazioneCud.contributi_versati_cococo,
                         dip_rp_deduzione_fissa_intera = aRecEstrazioneCud.dip_rp_deduzione_fissa_intera,
                         cnr_detrazioni_tassep = cnr_detrazioni_tassep + aRecEstrazioneCud.cnr_detrazioni_tassep,
                         cnr_detrazione_coniuge = cnr_detrazione_coniuge + aRecEstrazioneCud.cnr_detrazione_coniuge,
                         cnr_detrazione_figli = cnr_detrazione_figli + aRecEstrazioneCud.cnr_detrazione_figli,
                         ritenute_non_residenti = ritenute_non_residenti + aRecEstrazioneCud.ritenute_non_residenti,
                         im_irpef_sospeso = im_irpef_sospeso + aRecEstrazioneCud.im_irpef_sospeso,
                         im_redd_non_tassati_per_conv  = im_redd_non_tassati_per_conv + aRecEstrazioneCud.im_redd_non_tassati_per_conv,
                         im_redd_esenti_per_legge  = im_redd_esenti_per_legge + aRecEstrazioneCud.im_redd_esenti_per_legge,
                         im_bonus_erogato = im_bonus_erogato + aRecEstrazioneCud.im_bonus_erogato,
                         ult_detr_cuneo_fisc = ult_detr_cuneo_fisc + aRecEstrazioneCud.ult_detr_cuneo_fisc,
                         tratt_int_erog_cuneo_fisc = tratt_int_erog_cuneo_fisc + aRecEstrazioneCud.tratt_int_erog_cuneo_fisc,
                         tratt_int_cong_cuneo_fisc = tratt_int_cong_cuneo_fisc + aRecEstrazioneCud.tratt_int_cong_cuneo_fisc
                  WHERE  id_estrazione = inRepID AND
                         esercizio = inEsercizio AND
                         dip_cd_anag = aRecEstrazioneCudDett.cd_anag AND
                         rigo_cococo = 1;

               ELSE
                  UPDATE ESTRAZIONE_CUD
                  SET    imponibile_si_detr = imponibile_si_detr + aRecEstrazioneCud.imponibile_cong_si_detr,
                         imponibile_no_detr = imponibile_no_detr + aRecEstrazioneCud.imponibile_cong_no_detr,
                         ritenute_irpef = ritenute_irpef + aRecEstrazioneCud.ritenute_irpef,
                         ritenute_irpef_tassep = ritenute_irpef_tassep + aRecEstrazioneCud.ritenute_irpef_tassep,
                         addizionale_regionale = addizionale_regionale + aRecEstrazioneCud.addizionale_regionale,
                         addizionale_comunale = addizionale_comunale + aRecEstrazioneCud.addizionale_comunale,
                         deduzione_dovuta = deduzione_dovuta + aRecEstrazioneCud.deduzione_dovuta,
                         im_deduzione_family = im_deduzione_family + aRecEstrazioneCud.im_deduzione_family,
                         detrazioni_lavoro_dip = detrazioni_lavoro_dip + aRecEstrazioneCud.detrazioni_lavoro_dip,
                         detrazioni_familiari = detrazioni_familiari + aRecEstrazioneCud.detrazioni_familiari,
                         totale_detrazioni = totale_detrazioni + aRecEstrazioneCud.totale_detrazioni,
                         imponibile_cong_si_detr = imponibile_cong_si_detr + aRecEstrazioneCud.imponibile_cong_si_detr,
                         imponibile_cong_no_detr = imponibile_cong_no_detr + aRecEstrazioneCud.imponibile_cong_no_detr,
                         imposta_lorda = imposta_lorda + aRecEstrazioneCud.imposta_lorda,
                         compensi_corrisposti_cococo = compensi_corrisposti_cococo +
                                                       aRecEstrazioneCud.compensi_corrisposti_cococo,
                         contributi_dovuti_cococo = contributi_dovuti_cococo +
                                                    aRecEstrazioneCud.contributi_dovuti_cococo,
                         contributi_trattenuti_cococo = contributi_trattenuti_cococo +
                                                        aRecEstrazioneCud.contributi_trattenuti_cococo,
                         contributi_versati_cococo = contributi_versati_cococo +
                                                     aRecEstrazioneCud.contributi_versati_cococo,
                         cnr_detrazioni_tassep = cnr_detrazioni_tassep + aRecEstrazioneCud.cnr_detrazioni_tassep,
                         cnr_detrazione_coniuge = cnr_detrazione_coniuge + aRecEstrazioneCud.cnr_detrazione_coniuge,
                         cnr_detrazione_figli = cnr_detrazione_figli + aRecEstrazioneCud.cnr_detrazione_figli,
                         ritenute_non_residenti = ritenute_non_residenti + aRecEstrazioneCud.ritenute_non_residenti,
                         im_irpef_sospeso = im_irpef_sospeso + aRecEstrazioneCud.im_irpef_sospeso,
                         im_redd_non_tassati_per_conv  = im_redd_non_tassati_per_conv + aRecEstrazioneCud.im_redd_non_tassati_per_conv,
                         im_redd_esenti_per_legge  = im_redd_esenti_per_legge + aRecEstrazioneCud.im_redd_esenti_per_legge,
                         im_bonus_erogato = im_bonus_erogato + aRecEstrazioneCud.im_bonus_erogato,
                         ult_detr_cuneo_fisc = ult_detr_cuneo_fisc + aRecEstrazioneCud.ult_detr_cuneo_fisc,
                         tratt_int_erog_cuneo_fisc = tratt_int_erog_cuneo_fisc + aRecEstrazioneCud.tratt_int_erog_cuneo_fisc,
                         tratt_int_cong_cuneo_fisc = tratt_int_cong_cuneo_fisc + aRecEstrazioneCud.tratt_int_cong_cuneo_fisc
                  WHERE  id_estrazione = inRepID AND
                         esercizio = inEsercizio AND
                         dip_cd_anag = aRecEstrazioneCudDett.cd_anag AND
                         rigo_cococo = 1;

               END IF;
            END IF;

            -- Aggiornamento ESTRAZIONE_CUD_ANNOTAZIONI per imponibile fiscale esterno -------------

            IF aRecEstrazioneCudDett.imponibile_fiscale_esterno != 0 THEN

               IF aRecEstrazioneCud.imponibile_cong_si_detr != 0 THEN
                  i:=1;
                  aImponibile:=aRecEstrazioneCud.imponibile_cong_si_detr;
               ELSE
                  i:=2;
                  aImponibile:=aRecEstrazioneCud.imponibile_cong_no_detr;
               END IF;

               inserisciCUDAnnotazioni(inRepID,
                                       inEsercizio,
                                       aRecEstrazioneCudDett.cd_anag,
                                       aPgOrdineAnnotazione,
                                       i,
                                       aCdTipologiaRedditoDef,
                                       aDataRifDa,
                                       aDsTipologiaRedditoDef,
                                       aImponibile,
                                       aDataRifA,
                                       inUtente);

            END IF;

         END;

      END LOOP;

      CLOSE gen_cur;

   END;

   -------------------------------------------------------------------------------------------------
   -- Calcolo imponibile_IRPEF

   UPDATE ESTRAZIONE_CUD
   SET    imponibile_IRPEF = imponibile_si_detr + imponibile_no_detr - deduzione_dovuta - im_deduzione_family
   WHERE  id_estrazione = inRepID AND
          esercizio = inEsercizio;
End aggregaDatiCUD;

-- =================================================================================================
-- Inserimento dati anagrafici in ESTRAZIONE_CUD
-- =================================================================================================
PROCEDURE insDatiAnagCud
   (
    inEsercizio NUMBER,
    inRepID INTEGER
   ) IS

   cv_ti_entita CHAR(1);
   cv_ti_entita_fisica CHAR(1);
   cv_ti_entita_giuridica CHAR(1);
   cv_cognome VARCHAR2(100);
   cv_nome VARCHAR2(100);
   cv_ragione_sociale VARCHAR2(100);
   cv_fl_applic_aliquota_max CHAR(1);
   cv_via_fiscale VARCHAR2(150);
   cv_num_civico_fiscale VARCHAR2(10);
   cv_ti_italiano_estero VARCHAR2(1);
   cv_uo VARCHAR2(30);

   memAnnotazioni ESTRAZIONE_CUD.annotazioni_2%TYPE;
   memCdAnag ESTRAZIONE_CUD.dip_cd_anag%TYPE;

   aImAddRegRate NUMBER(15,2);
   aImAddComRate NUMBER(15,2);

   aRecEstrazioneCud ESTRAZIONE_CUD%ROWTYPE;
   aRecAnagraficoEsercizio ANAGRAFICO_ESERCIZIO%ROWTYPE;
   aRecRateizzaClassificCori RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

ANAG NUMBER;

   gen_cur GenericCurTyp;

BEGIN
   -------------------------------------------------------------------------------------------------
   -- Aggiornamento dati dei soggetti anagrafici presenti in ESTRAZIONE_CUD

   BEGIN

      OPEN gen_cur FOR

           SELECT A.dip_cd_anag,
                  B.ti_entita,
                  B.ti_entita_fisica,
                  B.ti_entita_giuridica,
                  B.cognome,
                  B.nome,
                  B.ragione_sociale,
                  B.codice_fiscale,
                  B.ti_sesso,
                  TO_CHAR(B.dt_nascita,'DD'),
                  TO_CHAR(B.dt_nascita,'MM'),
                  TO_CHAR(B.dt_nascita,'YYYY'),
                  C.ds_comune,
                  C.cd_provincia,
                  D.ds_comune,
                  D.cd_provincia,
                  D.cd_catastale,
                  D.ti_italiano_estero,
                  E.ds_nota,
                  DECODE(B.aliquota_fiscale, NULL, 'N', 'Y'),
                  B.matricola_inail,
                  Substr(B.cap_comune_fiscale,1,5),
                  B.via_fiscale,
                  B.num_civico_fiscale
           FROM   ESTRAZIONE_CUD A, ANAGRAFICO B,
                  COMUNE C, COMUNE D, NOTE_ANAGRAFICO E
           WHERE  A.id_estrazione = inRepID AND
                  A.esercizio = inEsercizio AND
                  B.cd_anag = A.dip_cd_anag AND
                  C.pg_comune = B.pg_comune_nascita AND
                  D.pg_comune = B.pg_comune_fiscale AND
                  E.esercizio (+) = A.esercizio AND
                  E.cd_anag (+) = A.dip_cd_anag AND
                  (E.tipo_nota = 'CUD' OR
                   E.tipo_nota IS NULL);

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCud.dip_cd_anag,
               cv_ti_entita,
               cv_ti_entita_fisica,
               cv_ti_entita_giuridica,
               cv_cognome,
               cv_nome,
               cv_ragione_sociale,
               aRecEstrazioneCud.dip_codice_fiscale,
               aRecEstrazioneCud.dip_sesso,
               aRecEstrazioneCud.dip_gg_nascita,
               aRecEstrazioneCud.dip_mm_nascita,
               aRecEstrazioneCud.dip_aa_nascita,
               aRecEstrazioneCud.dip_com_nascita,
               aRecEstrazioneCud.dip_prv_nascita,
               aRecEstrazioneCud.dip_com_domfisc,
               aRecEstrazioneCud.dip_prv_domfisc,
               aRecEstrazioneCud.dip_cod_com_domfisc,
               cv_ti_italiano_estero,
               aRecEstrazioneCud.annotazioni_2,
               cv_fl_applic_aliquota_max,
               aRecEstrazioneCud.cnr_posizione_inail,
               aRecEstrazioneCud.dip_cap_domfisc,
               cv_via_fiscale,
               cv_num_civico_fiscale;

         EXIT WHEN gen_cur%NOTFOUND;

       -- Poichè esistono dati sporchi sul db (residenza anagrafico diversa da residenza terzo)
       -- e poichè nei compensi e nell'acconto prendiamo la residenza dal terzo, anche nel CUD
       -- i dati della residenza li prendiamo dal terzo e poichè potrebbero esserci più terzi validi
       -- per la stessa anagrafica, prendiamo il terzo di ESTRAZIONE_CUD
       Begin
          Select c.ds_comune, c.cd_provincia, c.cd_catastale, substr(t.cap_comune_sede,1,5), t.via_sede, t.numero_civico_sede, c.ti_italiano_estero
            Into aRecEstrazioneCud.dip_com_domfisc, aRecEstrazioneCud.dip_prv_domfisc, aRecEstrazioneCud.dip_cod_com_domfisc,
                 aRecEstrazioneCud.dip_cap_domfisc, cv_via_fiscale, cv_num_civico_fiscale, cv_ti_italiano_estero
            From terzo t, comune c
              Where t.pg_comune_sede = c.pg_comune
                And t.cd_anag = aRecEstrazioneCud.dip_cd_anag
                --And t.dt_fine_rapporto Is Null
                And t.cd_terzo In (Select e.cd_terzo
                                   From ESTRAZIONE_CUD_DETT e
                                 Where id_estrazione = inRepID
                                   And esercizio = inEsercizio
                                   And cd_anag = aRecEstrazioneCud.dip_cd_anag)
              And Rownum < 2;
       Exception
            When NO_DATA_FOUND then
                  IBMERR001.RAISE_ERR_GENERICO('Terzo non trovato per Anagrafico:'||aRecEstrazioneCud.dip_cd_anag);
              When Too_Many_Rows then
                  IBMERR001.RAISE_ERR_GENERICO('Esistono più terzi validi per Anagrafico:'||aRecEstrazioneCud.dip_cd_anag);
         End;
         -- Aggiornamento dati da ESTRAZIONE_CUD_DETT su ESTRAZIONE_CUD inclusi dati di testata

         aRecEstrazioneCud.dip_cognome:=NULL;
         aRecEstrazioneCud.dip_nome:=NULL;
         --03/03/2009 - Prendiamo sempre il Cognome ed il Nome, così se non sono valorizzati il CUD
         --             ci segnala l'errore e possiamo correggerlo in SIGLA
       /*
         IF    cv_ti_entita = 'U' THEN
               aRecEstrazioneCud.dip_cognome:=cv_ragione_sociale;
         ELSIF cv_ti_entita = 'G' THEN
               aRecEstrazioneCud.dip_cognome:=cv_ragione_sociale;
         ELSIF cv_ti_entita = 'F' THEN
               IF cv_ti_entita_fisica = 'D' THEN
                  aRecEstrazioneCud.dip_cognome:=cv_ragione_sociale;
               ELSE
                  aRecEstrazioneCud.dip_cognome:=cv_cognome;
                  aRecEstrazioneCud.dip_nome:=cv_nome;
               END IF;
         ELSIF cv_ti_entita = 'D' THEN
               IF cv_ragione_sociale IS NOT NULL THEN
                  aRecEstrazioneCud.dip_cognome:=cv_ragione_sociale;
               ELSE
                  aRecEstrazioneCud.dip_cognome:=cv_cognome;
                  aRecEstrazioneCud.dip_nome:=cv_nome;
               END IF;
         END IF;
         */
         aRecEstrazioneCud.dip_cognome:=cv_cognome;
         aRecEstrazioneCud.dip_nome:=cv_nome;

       -- Se il comune della residenza fiscale è estero, si prende il comune della UO
       If cv_ti_italiano_estero != 'I' Then
         Begin
            --prendo la UO sulla quale il terzo ha il reddito maggiore
            Select cd_uo_compenso
            Into cv_uo
            From ( Select cd_uo_compenso, Nvl(Sum(imponibile_fisc_lordo_contrib),0) somma
                   From ESTRAZIONE_CUD_DETT
                   Where id_estrazione = inRepID
                     And esercizio = inEsercizio
                     And cd_anag = aRecEstrazioneCud.dip_cd_anag
                   Group By cd_uo_compenso
                   Order By somma Desc)
            Where Rownum <2;

                  Select c.ds_comune, c.cd_provincia, c.cd_catastale, substr(t.cap_comune_sede,1,5), t.via_sede, t.numero_civico_sede
            Into aRecEstrazioneCud.dip_com_domfisc, aRecEstrazioneCud.dip_prv_domfisc, aRecEstrazioneCud.dip_cod_com_domfisc,
                 aRecEstrazioneCud.dip_cap_domfisc, cv_via_fiscale, cv_num_civico_fiscale
            From terzo t, comune c
            Where t.pg_comune_sede = c.pg_comune
              And cd_unita_organizzativa = cv_uo;
         Exception
               When Others Then Null;
         End;
         End If;

         IF cv_num_civico_fiscale IS NOT NULL THEN
            cv_via_fiscale:=cv_via_fiscale || ', ' || cv_num_civico_fiscale;
         END IF;
         -- Normalizzazione estrazioni per congruenza alla dimensione su db

         memAnnotazioni:=LTRIM(RTRIM(aRecEstrazioneCud.annotazioni_2));

         BEGIN
            IF memAnnotazioni IS NULL THEN

               UPDATE ESTRAZIONE_CUD
               SET    dip_cognome = aRecEstrazioneCud.dip_cognome,
                      dip_nome = aRecEstrazioneCud.dip_nome,
                      dip_codice_fiscale = aRecEstrazioneCud.dip_codice_fiscale,
                      dip_sesso = aRecEstrazioneCud.dip_sesso,
                      dip_gg_nascita = aRecEstrazioneCud.dip_gg_nascita,
                      dip_mm_nascita = aRecEstrazioneCud.dip_mm_nascita,
                      dip_aa_nascita = aRecEstrazioneCud.dip_aa_nascita,
                      dip_com_nascita = aRecEstrazioneCud.dip_com_nascita,
                      dip_prv_nascita = aRecEstrazioneCud.dip_prv_nascita,
                      dip_com_domfisc = aRecEstrazioneCud.dip_com_domfisc,
                      dip_prv_domfisc = aRecEstrazioneCud.dip_prv_domfisc,
                      dip_cod_com_domfisc = aRecEstrazioneCud.dip_cod_com_domfisc,
                      fl_applic_aliquota_max = cv_fl_applic_aliquota_max,
                      cnr_posizione_inail = aRecEstrazioneCud.cnr_posizione_inail,
                      dip_cap_domfisc = aRecEstrazioneCud.dip_cap_domfisc,
                      dip_ind_domfisc = cv_via_fiscale
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio AND
                      dip_cd_anag = aRecEstrazioneCud.dip_cd_anag;

            ELSE
               UPDATE ESTRAZIONE_CUD
               SET    dip_cognome = aRecEstrazioneCud.dip_cognome,
                      dip_nome = aRecEstrazioneCud.dip_nome,
                      dip_codice_fiscale = aRecEstrazioneCud.dip_codice_fiscale,
                      dip_sesso = aRecEstrazioneCud.dip_sesso,
                      dip_gg_nascita = aRecEstrazioneCud.dip_gg_nascita,
                      dip_mm_nascita = aRecEstrazioneCud.dip_mm_nascita,
                      dip_aa_nascita = aRecEstrazioneCud.dip_aa_nascita,
                      dip_com_nascita = aRecEstrazioneCud.dip_com_nascita,
                      dip_prv_nascita = aRecEstrazioneCud.dip_prv_nascita,
                      dip_com_domfisc = aRecEstrazioneCud.dip_com_domfisc,
                      dip_prv_domfisc = aRecEstrazioneCud.dip_prv_domfisc,
                      dip_cod_com_domfisc = aRecEstrazioneCud.dip_cod_com_domfisc,
                      annotazioni_2 = DECODE(annotazioni_2, NULL,
                                             aRecEstrazioneCud.annotazioni_2,
                                             annotazioni_2 || ' ' || chr(10) || aRecEstrazioneCud.annotazioni_2),
                      fl_applic_aliquota_max = cv_fl_applic_aliquota_max,
                      cnr_posizione_inail = aRecEstrazioneCud.cnr_posizione_inail,
                      dip_cap_domfisc = aRecEstrazioneCud.dip_cap_domfisc,
                      dip_ind_domfisc = cv_via_fiscale
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio AND
                      dip_cd_anag = aRecEstrazioneCud.dip_cd_anag;

            END IF;

         END;

      END LOOP;

      CLOSE gen_cur;
   END;

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento flag anagrafici su ESTRAZIONE_CUD

   BEGIN

      -- Gestione campo soggetto_estero ------------------------------------------------------------

      UPDATE ESTRAZIONE_CUD A
      SET    A.fl_soggetto_estero = 'Y'
      WHERE  EXISTS
             (SELECT 1
              FROM   ESTRAZIONE_CUD_DETT B
              WHERE  B.id_estrazione = A.id_estrazione AND
                     B.esercizio = A.esercizio AND
                     B.cd_anag = A.dip_cd_anag AND
                     B.fl_ritenuta_non_residenti = 'Y') AND
             A.id_estrazione = inRepID AND
             A.esercizio = inEsercizio;

      -- Recupero informazioni da ANAGRAFICO_ESERCIZIO (NO_TAX_AREA) -------------------------------

      BEGIN

         OPEN gen_cur FOR

              SELECT A.dip_cd_anag,
                     A.esercizio,
                     NVL(B.fl_notaxarea,'N'),
                     Nvl(B.im_reddito_complessivo,0),
                     Nvl(B.im_reddito_abitaz_princ,0)
              FROM   ESTRAZIONE_CUD A, ANAGRAFICO_ESERCIZIO B
              WHERE  A.id_estrazione = inRepID AND
                     A.esercizio = inEsercizio AND
                     B.cd_anag = A.dip_cd_anag AND
                     B.esercizio = A.esercizio;

         LOOP

            FETCH gen_cur INTO
                  aRecEstrazioneCud.dip_cd_anag,
                  aRecEstrazioneCud.esercizio,
                  aRecAnagraficoEsercizio.fl_notaxarea,
                  aRecEstrazioneCud.altri_redditi,
                  aRecEstrazioneCud.reddito_abitaz_princ;

            EXIT WHEN gen_cur%NOTFOUND;

            IF aRecAnagraficoEsercizio.fl_notaxarea = 'Y' THEN

               UPDATE ESTRAZIONE_CUD
               SET    dip_rp_no_tax_area = 'Y',
                      fl_no_applic_deduzione = 'Y'
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio AND
                      dip_cd_anag = aRecEstrazioneCud.dip_cd_anag AND
                      rigo_cococo = 1;

            END IF;
            UPDATE ESTRAZIONE_CUD
               SET altri_redditi = aRecEstrazioneCud.altri_redditi,
                   reddito_abitaz_princ = aRecEstrazioneCud.reddito_abitaz_princ
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio AND
                      dip_cd_anag = aRecEstrazioneCud.dip_cd_anag AND
                      rigo_cococo = 1;

         END LOOP;

         CLOSE gen_cur;

      END;

      -- Recupero informazioni di accantonamento delle addizionali territorio ----------------------

      BEGIN

         memCdAnag:=NULL;

         OPEN gen_cur FOR

              SELECT A.dip_cd_anag,
                     A.esercizio,
                     B.cd_classificazione_cori,
                     B.im_da_rateizzare
              FROM   ESTRAZIONE_CUD A, RATEIZZA_CLASSIFIC_CORI B
              WHERE  A.id_estrazione = inRepID AND
                     A.esercizio = inEsercizio AND
                     B.cd_anag = A.dip_cd_anag AND
                     B.esercizio = A.esercizio AND
                     EXISTS
                        (SELECT 1
                         FROM   ESTRAZIONE_CUD_DETT C
                         WHERE  C.id_estrazione = A.id_estrazione AND
                                C.esercizio = A.esercizio AND
                                C.cd_anag = A.dip_cd_anag AND
                                C.cd_cds_conguaglio = B.cd_cds_conguaglio AND
                                C.cd_uo_conguaglio = B.cd_uo_conguaglio AND
                                C.esercizio_conguaglio = B.esercizio AND
                                C.pg_conguaglio = B.pg_conguaglio AND
                                C.fl_ultimo_conguaglio = 'Y')
              UNION ALL
              SELECT A.dip_cd_anag,
                     A.esercizio,
                     B.cd_classificazione_cori,
                     B.im_da_rateizzare
              FROM   ESTRAZIONE_CUD A, RATEIZZA_CLASSIFIC_CORI_S B
              WHERE  A.id_estrazione = 48 AND
                     A.esercizio = 2003 AND
                     B.cd_anag = A.dip_cd_anag AND
                     B.esercizio = A.esercizio AND
                     EXISTS
                        (SELECT 1
                         FROM   ESTRAZIONE_CUD_DETT C
                         WHERE  C.id_estrazione = A.id_estrazione AND
                                C.esercizio = A.esercizio AND
                                C.cd_anag = A.dip_cd_anag AND
                                C.cd_cds_conguaglio = B.cd_cds_conguaglio AND
                                C.cd_uo_conguaglio = B.cd_uo_conguaglio AND
                                C.esercizio_conguaglio = B.esercizio AND
                                C.pg_conguaglio = B.pg_conguaglio AND
                                C.fl_ultimo_conguaglio = 'Y')
              ORDER BY 1;

         LOOP

            FETCH gen_cur INTO
                  aRecEstrazioneCud.dip_cd_anag,
                  aRecEstrazioneCud.esercizio,
                  aRecRateizzaClassificCori.cd_classificazione_cori,
                  aRecRateizzaClassificCori.im_da_rateizzare;

            EXIT WHEN gen_cur%NOTFOUND;

            IF memCdAnag IS NULL THEN
               memCdAnag:=aRecEstrazioneCud.dip_cd_anag;
               aImAddRegRate:=0;
               aImAddComRate:=0;
            END IF;

            IF memCdAnag != aRecEstrazioneCud.dip_cd_anag THEN

               UPDATE ESTRAZIONE_CUD
               SET    add_reg_accantonato = aImAddRegRate,
                      add_com_accantonato = aImAddComRate
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio AND
                      dip_cd_anag = memCdAnag AND
                      rigo_cococo = 1;

               memCdAnag:=aRecEstrazioneCud.dip_cd_anag;
               aImAddRegRate:=0;
               aImAddComRate:=0;

            END IF;

            IF    aRecRateizzaClassificCori.cd_classificazione_cori = CNRCTB545.isCoriAddReg THEN
                  aImAddRegRate:=aRecRateizzaClassificCori.im_da_rateizzare;
            ELSIF aRecRateizzaClassificCori.cd_classificazione_cori = CNRCTB545.isCoriAddCom THEN
                  aImAddComRate:=aRecRateizzaClassificCori.im_da_rateizzare;
            END IF;

         END LOOP;

         UPDATE ESTRAZIONE_CUD
         SET    add_reg_accantonato = aImAddRegRate,
                add_com_accantonato = aImAddComRate
         WHERE  id_estrazione = inRepID AND
                esercizio = inEsercizio AND
                dip_cd_anag = memCdAnag AND
                rigo_cococo = 1;

         CLOSE gen_cur;
      END;

      -- Recupero informazioni di recupero delle addizionali territorio rateizzate nell'esercizio precedente --

      BEGIN

         memCdAnag:=NULL;

         OPEN gen_cur FOR

              SELECT A.dip_cd_anag,
                     A.esercizio,
                     B.cd_classificazione_cori,
                     B.im_rateizzato
              FROM   ESTRAZIONE_CUD A, RATEIZZA_CLASSIFIC_CORI B
              WHERE  A.id_estrazione = inRepID AND
                     A.esercizio = inEsercizio AND
                     B.cd_anag = A.dip_cd_anag AND
                     B.esercizio = (A.esercizio - 1)
              ORDER BY 1;

         LOOP

            FETCH gen_cur INTO
                  aRecEstrazioneCud.dip_cd_anag,
                  aRecEstrazioneCud.esercizio,
                  aRecRateizzaClassificCori.cd_classificazione_cori,
                  aRecRateizzaClassificCori.im_rateizzato;

            EXIT WHEN gen_cur%NOTFOUND;

            IF memCdAnag IS NULL THEN
               memCdAnag:=aRecEstrazioneCud.dip_cd_anag;
               aImAddRegRate:=0;
               aImAddComRate:=0;
            END IF;

            IF memCdAnag != aRecEstrazioneCud.dip_cd_anag THEN

               UPDATE ESTRAZIONE_CUD
               SET    add_reg_anno_precedente = aImAddRegRate,
                      add_com_anno_precedente = aImAddComRate
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio AND
                      dip_cd_anag = memCdAnag AND
                      rigo_cococo = 1;

               memCdAnag:=aRecEstrazioneCud.dip_cd_anag;
               aImAddRegRate:=0;
               aImAddComRate:=0;

            END IF;

            IF    aRecRateizzaClassificCori.cd_classificazione_cori = CNRCTB545.isCoriAddReg THEN
                  aImAddRegRate:=aRecRateizzaClassificCori.im_rateizzato;
            ELSIF aRecRateizzaClassificCori.cd_classificazione_cori = CNRCTB545.isCoriAddCom THEN
                  aImAddComRate:=aRecRateizzaClassificCori.im_rateizzato;
            END IF;

         END LOOP;

         UPDATE ESTRAZIONE_CUD
         SET    add_reg_anno_precedente = aImAddRegRate,
                add_com_anno_precedente = aImAddComRate
         WHERE  id_estrazione = inRepID AND
                esercizio = inEsercizio AND
                dip_cd_anag = memCdAnag AND
                rigo_cococo = 1;

         CLOSE gen_cur;
      END;

   END;
End insDatiAnagCud;

-- =================================================================================================
-- Aggiornamento del numero di giorni lavorati in ESTRAZIONI_CUD
-- =================================================================================================
PROCEDURE scriviGiorniLavoro
   (
    inEsercizio NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2,
    aCdAnag NUMBER
   ) IS

   numeroGG_CREDITO_1_SEM INTEGER;
   numeroGG_CREDITO_2_SEM INTEGER;
   numeroGG INTEGER;
   numeroGGTassep INTEGER;
   aImponibile NUMBER(15,2);
   aPgOrdineAnnotazione INTEGER;
   aPgTipoAnnotazione INTEGER;
   aCdTipologiaRedditoDate VARCHAR2(10);
   aDsTipologiaRedditoDate VARCHAR2(300);

   aDataMinGenerale DATE;
   aDataMaxGenerale DATE;
   aDataMinCococo DATE;
   aDataMaxCococo DATE;
   aDataFineAnno DATE;

   i BINARY_INTEGER;

   DATA_INIZIO_RID_CUNEO DATE;

BEGIN

      DATA_INIZIO_RID_CUNEO := CNRCTB015.getDt01PerChiave('0', 'RIDUZIONE_CUNEO_DL_3_2020', 'DATA_INIZIO');


   -------------------------------------------------------------------------------------------------
   -- Valorizzazione costanti

   aImponibile:=0;
   aPgOrdineAnnotazione:=3;
   aPgTipoAnnotazione:=0;
   aCdTipologiaRedditoDate:='DATE';
   aDsTipologiaRedditoDate:='INTERVALLO DATE';
   aDataFineAnno:=TO_DATE('3112' || inEsercizio,'DDMMYYYY');

   tabella_date_ok.DELETE;
   tabella_date_ok_tutte.DELETE;
   tabella_date_ok_cococo.DELETE;
   tabella_date_ok_tassep.DELETE;

   -- Azzeramento tabella date negative per recupero rate se non ci sono intervalli ordinari

   IF tabella_date.COUNT = 0 THEN
      tabella_date_neg.DELETE;
   END IF;
   If tabella_date_tutte.COUNT = 0 THEN
      tabella_date_tutte_neg.DELETE;
   END IF;
   IF tabella_date_cococo.COUNT = 0 THEN
      tabella_date_neg_cococo.DELETE;
   END IF;
   IF tabella_date_tassep.COUNT = 0 THEN
      tabella_date_neg_tassep.DELETE;
   END IF;


   -------------------------------------------------------------------------------------------------
   -- Normalizzazione delle date per includere il recupero delle rate ed eliminare periodi di eventuale
   -- sovrapposizione

   -- Date compensi
   IF tabella_date.COUNT > 0 THEN
      CNRCTB650.scaricaRecuperoRate(tabella_date,
                                    tabella_date_neg);
      CNRCTB650.normalizzaMatriceDate(tabella_date,
                                      tabella_date_ok);
      CNRCTB650.chkSpuriRecuperoRate(tabella_date_neg);
   END IF;

-- Date tutti i compensi

   IF tabella_date_tutte.COUNT > 0 THEN
      CNRCTB650.scaricaRecuperoRate(tabella_date_tutte,
                                    tabella_date_tutte_neg);
      CNRCTB650.normalizzaMatriceDate(tabella_date_tutte,
                                      tabella_date_ok_tutte);
      CNRCTB650.chkSpuriRecuperoRate(tabella_date_tutte_neg);
   END IF;

   -- Date cococo

   IF tabella_date_cococo.COUNT > 0 THEN
      CNRCTB650.scaricaRecuperoRate(tabella_date_cococo,
                                    tabella_date_neg_cococo);
      CNRCTB650.normalizzaMatriceDate(tabella_date_cococo,
                                      tabella_date_ok_cococo);
      CNRCTB650.chkSpuriRecuperoRate(tabella_date_neg_cococo);
   END IF;

   -- Date tassazione separata

   IF tabella_date_tassep.COUNT > 0 THEN
      CNRCTB650.scaricaRecuperoRate(tabella_date_tassep,
                                    tabella_date_neg_tassep);
      CNRCTB650.normalizzaMatriceDate(tabella_date_tassep,
                                      tabella_date_ok_tassep);
      CNRCTB650.chkSpuriRecuperoRate(tabella_date_neg_tassep);
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Calcolo del numero di giorni lavorativi (preso solo dai compensi non a tassazione separata)
   BEGIN

      numeroGG_CREDITO_1_SEM :=0;
      numeroGG_CREDITO_2_SEM :=0;
      numeroGG:=0;                 -- devo considerare solo i compensi non a tassazione separata

      IF tabella_date_ok.COUNT > 0 THEN

         FOR i IN tabella_date_ok.FIRST .. tabella_date_ok.LAST

         Loop
            -- Calcolo del numero dei giorni
            numeroGG:=(numeroGG + (tabella_date_ok(i).tDataA - tabella_date_ok(i).tDataDa + 1));
            IF DATA_INIZIO_RID_CUNEO > tabella_date_ok(i).tDataDa THEN
                IF DATA_INIZIO_RID_CUNEO < tabella_date_ok(i).tDataA THEN
                  numeroGG_CREDITO_1_SEM := numeroGG_CREDITO_1_SEM+ (DATA_INIZIO_RID_CUNEO - tabella_date_ok(i).tDataDa );
                  numeroGG_CREDITO_2_SEM := numeroGG_CREDITO_2_SEM+ (tabella_date_ok(i).tDataA - DATA_INIZIO_RID_CUNEO + 1 );
                ELSE
                  numeroGG_CREDITO_1_SEM := numeroGG_CREDITO_1_SEM+ (tabella_date_ok(i).tDataA - tabella_date_ok(i).tDataDa + 1);
                END IF;
            ELSE
              numeroGG_CREDITO_2_SEM := numeroGG_CREDITO_2_SEM+ (tabella_date_ok(i).tDataA - tabella_date_ok(i).tDataDa + 1 );
            END IF;
         End Loop;

         IF numeroGG_CREDITO_1_SEM > 181 THEN
            numeroGG_CREDITO_1_SEM := 181;
         END IF;

         IF numeroGG > maxGGAnno THEN
            numeroGG:=maxGGAnno;
         END IF;
         numeroGG_CREDITO_2_SEM := numeroGG - numeroGG_CREDITO_1_SEM;
      END IF;

   END;
   -------------------------------------------------------------------------------------------------
   -- Calcolo della minima e massima data dei compensi ed aggiornamento delle annotazioni (DATE COMPENSI)
   BEGIN

      aDataMinGenerale:=NULL;         -- devo considerare tutti i compensi (anche a tassazione separata)
      aDataMaxGenerale:=NULL;             -- devo considerare tutti i compensi (anche a tassazione separata)

      IF tabella_date_ok_tutte.COUNT > 0 THEN

         FOR i IN tabella_date_ok_tutte.FIRST .. tabella_date_ok_tutte.LAST

         LOOP

            -- Scrittura su tabella ESTRAZIONE_CUD_ANNOTAZIONI degli intervalli temporali considerati

            inserisciCUDAnnotazioni(inRepID,
                                    inEsercizio,
                                    aCdAnag,
                                    aPgOrdineAnnotazione,
                                    aPgTipoAnnotazione,
                                    aCdTipologiaRedditoDate,
                                    tabella_date_ok_tutte(i).tDataDa,
                                    aDsTipologiaRedditoDate,
                                    aImponibile,
                                    tabella_date_ok_tutte(i).tDataA,
                                    inUtente);

            -- Determino intervallo minino e massimo delle date compensi per CNR

            IF aDataMinGenerale IS NULL THEN
               aDataMinGenerale:=tabella_date_ok_tutte(i).tDataDa;
               aDataMaxGenerale:=tabella_date_ok_tutte(i).tDataA;
            ELSE
               IF tabella_date_ok_tutte(i).tDataDa < aDataMinGenerale THEN
                  aDataMinGenerale:=tabella_date_ok_tutte(i).tDataDa;
               END IF;
               IF tabella_date_ok_tutte(i).tDataA > aDataMaxGenerale THEN
                  aDataMaxGenerale:=tabella_date_ok_tutte(i).tDataA;
               END IF;
            END IF;

         END LOOP;

         -- Normalizzazione date generali al 31/12/XXXX

         IF aDataMinGenerale > aDataFineAnno THEN
            aDataMinGenerale:=aDataFineAnno;
         END IF;

         IF aDataMaxGenerale > aDataFineAnno THEN
            aDataMaxGenerale:=aDataFineAnno;
         END IF;

      END IF;

   END;

   -------------------------------------------------------------------------------------------------
   -- Calcolo del numero di giorni lavorativi (DATE COMPENSI A TASSAZIONE SEPARATA)
   /*
   BEGIN
      numeroGGTassep:=0;
      IF tabella_date_ok_tassep.COUNT > 0 THEN
         FOR i IN tabella_date_ok_tassep.FIRST .. tabella_date_ok_tassep.LAST
         LOOP
            -- Calcolo del numero dei giorni
            numeroGGTassep:=(numeroGGTassep + (tabella_date_ok_tassep(i).tDataA - tabella_date_ok_tassep(i).tDataDa + 1));
         END LOOP;
         IF numeroGGTassep > maxGGAnno THEN
            numeroGGTassep:=maxGGAnno;
         END IF;
      END IF;
   END;
   */
   -- Modificato: deve essere il max anno e non il numero di giorni
   BEGIN
      numeroGGTassep:=0;
      IF tabella_date_ok_tassep.COUNT > 0 THEN
         FOR i IN tabella_date_ok_tassep.FIRST .. tabella_date_ok_tassep.LAST
         LOOP
            -- Calcolo L'anno massimo
            If numeroGGTassep = 0 Or numeroGGTassep < To_Char(tabella_date_ok_tassep(i).tDataA,'yyyy') Then
                numeroGGTassep:=To_Char(tabella_date_ok_tassep(i).tDataA,'yyyy');
            End If;
         END LOOP;
         IF numeroGGTassep > inEsercizio Then
            numeroGGTassep:=inEsercizio;
         END IF;
      END IF;
   END;
   -------------------------------------------------------------------------------------------------
   -- Calcolo dell'intervallo minimo e massimo dei cococo (DATE COCOCO)

   BEGIN

      aDataMinCococo:=NULL;
      aDataMaxCococo:=NULL;

      IF tabella_date_ok_cococo.COUNT > 0 THEN

         FOR i IN tabella_date_ok_cococo.FIRST .. tabella_date_ok_cococo.LAST

         LOOP

            IF aDataMinCococo IS NULL THEN
               aDataMinCococo:=tabella_date_ok_cococo(i).tDataDa;
               aDataMaxCococo:=tabella_date_ok_cococo(i).tDataA;
            ELSE
               IF tabella_date_ok_cococo(i).tDataDa < aDataMinCococo THEN
                  aDataMinCococo:=tabella_date_ok_cococo(i).tDataDa;
               END IF;
               IF tabella_date_ok_cococo(i).tDataA > aDataMaxCococo THEN
                  aDataMaxCococo:=tabella_date_ok_cococo(i).tDataA;
               END IF;
            END IF;

         END LOOP;

         -- Normalizzazione date cococo al 31/12/XXXX

         IF aDataMinCococo > aDataFineAnno THEN
            aDataMinCococo:=aDataFineAnno;
         END IF;

         IF aDataMaxCococo > aDataFineAnno THEN
            aDataMaxCococo:=aDataFineAnno;
         END IF;

      END IF;

   END;

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento estrazione CUD

   BEGIN

      UPDATE ESTRAZIONE_CUD
      SET    gg_periodo_lavoro = numeroGG,
             gg_periodo_lavoro_sem1 = numeroGG_CREDITO_1_SEM,
             gg_periodo_lavoro_sem2 = numeroGG_CREDITO_2_SEM,
             dt_inizio_cococo = aDataMinCococo,
             dt_fine_cococo = aDataMaxCococo,
             cnr_data_assunzione = aDataMinGenerale,
             cnr_data_cessazione = aDataMaxGenerale
      WHERE  id_estrazione = inRepID AND
             esercizio = inEsercizio AND
             dip_cd_anag = aCdAnag AND
             rigo_cococo = 1;

   END;

END scriviGiorniLavoro;

-- =================================================================================================
-- Calcolo del numero di giorni di riferimento
-- =================================================================================================
PROCEDURE calcolaGiorniLavoro
   (
    inEsercizio NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   retCode INTEGER;
   scritto CHAR(1);
   dataDa DATE;
   dataA DATE;
   memCdAnag ESTRAZIONE_CUD_DETT.cd_anag%TYPE;

   i BINARY_INTEGER;
   k BINARY_INTEGER;

   aRecEstrazioneCud ESTRAZIONE_CUD%ROWTYPE;
   aRecEstrazioneCudDett ESTRAZIONE_CUD_DETT%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Calcolo ed aggiornamento del periodo di lavoro in giorni su ESTRAZIONE_CUD

   BEGIN

      -- Calcolo il numero massimo di giorni per un dato esercizio

      maxGGAnno:=IBMUTL001.getDaysBetween(TO_DATE('0101' || inEsercizio,'DDMMYYYY'),
                                          TO_DATE('3112' || inEsercizio,'DDMMYYYY'));

      If maxGGAnno > 365 Then
           maxGGAnno := 365;
      End If;

      -- Azzeramento variabili

      memCdAnag:=NULL;


      -- Apertura cursore e determinazione degli intervalli date di lavoro a parità di codice e tipo anagrafico

      OPEN gen_cur FOR

           SELECT cd_anag,
                  fl_cococo,
                  fl_recupero_rate,
                  fl_tassazione_separata,
                  fl_compenso_missione,
                  im_redd_non_tassati_per_conv,
                  im_redd_esenti_per_legge,
                  fl_ritenuta_non_residenti,
                  DECODE(fl_detrazioni, 'Y', dt_cmp_da_compenso, Decode(cd_trattamento,'T098',dt_cmp_da_compenso,Null)),
                  DECODE(fl_detrazioni, 'Y', dt_cmp_a_compenso, Decode(cd_trattamento,'T098',dt_cmp_a_compenso,Null)),
                  DECODE(fl_ultimo_conguaglio, 'Y', dt_cmp_da_esterno, NULL),
                  DECODE(fl_ultimo_conguaglio, 'Y', dt_cmp_a_esterno, NULL)
           FROM   ESTRAZIONE_CUD_DETT
           WHERE  id_estrazione = inRepID AND
                  esercizio = inEsercizio
           ORDER BY cd_anag;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCudDett.cd_anag,
               aRecEstrazioneCudDett.fl_cococo,
               aRecEstrazioneCudDett.fl_recupero_rate,
               aRecEstrazioneCudDett.fl_tassazione_separata,
               aRecEstrazioneCudDett.fl_compenso_missione,
               aRecEstrazioneCudDett.im_redd_non_tassati_per_conv,
               aRecEstrazioneCudDett.im_redd_esenti_per_legge,
               aRecEstrazioneCudDett.fl_ritenuta_non_residenti,
               aRecEstrazioneCudDett.dt_cmp_da_compenso,
               aRecEstrazioneCudDett.dt_cmp_a_compenso,
               aRecEstrazioneCudDett.dt_cmp_da_esterno,
               aRecEstrazioneCudDett.dt_cmp_a_esterno;

         EXIT WHEN gen_cur%NOTFOUND;

         -- E' la prima volta che leggo un record dalla fetch

         IF memCdAnag IS NULL THEN
            tabella_date.DELETE;
            tabella_date_neg.DELETE;
            tabella_date_tutte.DELETE;
            tabella_date_tutte_neg.DELETE;
            tabella_date_cococo.DELETE;
            tabella_date_neg_cococo.DELETE;
            tabella_date_tassep.DELETE;
            tabella_date_neg_tassep.DELETE;
            i:=0;
            memCdAnag:=aRecEstrazioneCudDett.cd_anag;
         END IF;

         -- I riferimenti anagrafici letti sono diversi da quelli precedenti.
         -- Scarico il numero di giorni e lo aggiorno in ESTRAZIONE_CUD.
         -- Si azzera la tabella di riferimento delle date lavorate per riferimento anagrafico

         IF memCdAnag != aRecEstrazioneCudDett.cd_anag THEN

            scriviGiorniLavoro(inEsercizio,
                               inRepID,
                               inUtente,
                               memCdAnag);

            tabella_date.DELETE;
            tabella_date_neg.DELETE;
            tabella_date_tutte.DELETE;
            tabella_date_tutte_neg.DELETE;
            tabella_date_cococo.DELETE;
            tabella_date_neg_cococo.DELETE;
            tabella_date_tassep.DELETE;
            tabella_date_neg_tassep.DELETE;

            i:=0;
            memCdAnag:=aRecEstrazioneCudDett.cd_anag;

         END IF;

         -- Aggiornamento della tabella di memorizzazione delle date lavorate

         FOR j IN 1 .. 2

         LOOP

            IF j = 1 THEN
               dataDa:= aRecEstrazioneCudDett.dt_cmp_da_compenso;
               dataA:= aRecEstrazioneCudDett.dt_cmp_a_compenso;
            ELSE
               dataDa:= aRecEstrazioneCudDett.dt_cmp_da_esterno;
               dataA:=aRecEstrazioneCudDett.dt_cmp_a_esterno;
            END IF;

            IF dataDa IS NOT NULL THEN
               IF aRecEstrazioneCudDett.fl_recupero_rate= 'N' THEN
                  IF aRecEstrazioneCudDett.fl_tassazione_separata = 'Y' THEN
                     If (aRecEstrazioneCudDett.fl_compenso_missione = 'N' and
                         aRecEstrazioneCudDett.im_redd_non_tassati_per_conv = 0 and
                         aRecEstrazioneCudDett.im_redd_esenti_per_legge = 0 and
                         aRecEstrazioneCudDett.fl_ritenuta_non_residenti = 'N') then
                        CNRCTB545.componiMatriceDate(tabella_date_tassep,
                                                     dataDa,
                                                     dataA,
                                                     'N',
                                                     'Y');

                        CNRCTB545.componiMatriceDate(tabella_date_tutte,
                                                     dataDa,
                                                     dataA,
                                                     'N',
                                                     'Y');
                     End If;
                  ELSE
                     If (aRecEstrazioneCudDett.fl_compenso_missione = 'N' and
                         aRecEstrazioneCudDett.im_redd_non_tassati_per_conv = 0 and
                         aRecEstrazioneCudDett.im_redd_esenti_per_legge = 0 and
                         aRecEstrazioneCudDett.fl_ritenuta_non_residenti = 'N') then
                        CNRCTB545.componiMatriceDate(tabella_date,
                                                     dataDa,
                                                     dataA,
                                                     'N',
                                                     'Y');

                        CNRCTB545.componiMatriceDate(tabella_date_tutte,
                                                     dataDa,
                                                     dataA,
                                                     'N',
                                                     'Y');
                     End If;
                     IF aRecEstrazioneCudDett.fl_cococo = 'Y' THEN
                        CNRCTB545.componiMatriceDate(tabella_date_cococo,
                                                     dataDa,
                                                     dataA,
                                                     'N',
                                                     'Y');
                     END IF;
                  END IF;
               Else
--pipe.send_message('aRecEstrazioneCudDett.cd_anag = '||aRecEstrazioneCudDett.cd_anag);
                  IF aRecEstrazioneCudDett.fl_tassazione_separata = 'Y' THEN
                     If (aRecEstrazioneCudDett.fl_compenso_missione = 'N' and
                         aRecEstrazioneCudDett.im_redd_non_tassati_per_conv = 0 and
                         aRecEstrazioneCudDett.im_redd_esenti_per_legge = 0 and
                         aRecEstrazioneCudDett.fl_ritenuta_non_residenti = 'N') then
                        CNRCTB545.componiMatriceDate(tabella_date_neg_tassep,
                                                     dataDa,
                                                     dataA,
                                                     'N',
                                                     'Y');

                        CNRCTB545.componiMatriceDate(tabella_date_tutte_neg,
                                                     dataDa,
                                                     dataA,
                                                     'N',
                                                     'Y');
                     End If;
                  ELSE
                     If (aRecEstrazioneCudDett.fl_compenso_missione = 'N' and
                         aRecEstrazioneCudDett.im_redd_non_tassati_per_conv = 0 and
                         aRecEstrazioneCudDett.im_redd_esenti_per_legge = 0 and
                         aRecEstrazioneCudDett.fl_ritenuta_non_residenti = 'N') then
                        CNRCTB545.componiMatriceDate(tabella_date_neg,
                                                     dataDa,
                                                     dataA,
                                                     'N',
                                                     'Y');

                        CNRCTB545.componiMatriceDate(tabella_date_tutte_neg,
                                                     dataDa,
                                                     dataA,
                                                     'N',
                                                     'Y');
                     End If;
                     IF aRecEstrazioneCudDett.fl_cococo = 'Y' THEN
                        CNRCTB545.componiMatriceDate(tabella_date_neg_cococo,
                                                     dataDa,
                                                     dataA,
                                                     'N',
                                                     'Y');
                     END IF;
                  END IF;
               END IF;
            END IF;

         END LOOP;

      END LOOP;

      scriviGiorniLavoro(inEsercizio,
                         inRepID,
                         inUtente,
                         memCdAnag);

      CLOSE gen_cur;

   END;

END calcolaGiorniLavoro;

-- =================================================================================================
-- Inserimento dati annotazioni in ESTRAZIONE_CUD
-- =================================================================================================
PROCEDURE insDatiAnnotazioniCud
   (
    inEsercizio VARCHAR2,
    inRepID INTEGER
   ) IS

   memAnnotazione ESTRAZIONE_CUD.annotazioni_1%TYPE;
   memAnnotazioneDate ESTRAZIONE_CUD.annotazioni_1%TYPE;

   scrittoArt1 CHAR(1);
   scrittoArt2 CHAR(1);
   scrittoPeriodoGG CHAR(1);
   aDataDaTmp DATE;
   aDataATmp DATE;
   imDeduzione1 NUMBER(15,2);
   imDeduzione2 NUMBER(15,2);
   aStringaImporto VARCHAR2(50);
   aStringaImporto1 VARCHAR2(50);
   aTrnslFrom VARCHAR2(50);
   aTrnslTo VARCHAR2(50);

   aRecEstrazioneCud ESTRAZIONE_CUD%ROWTYPE;
   aRecEstrazioneCudAnnotaz ESTRAZIONE_CUD_ANNOTAZIONI%ROWTYPE;

   gen_cur GenericCurTyp;
   gen_cur_a GenericCurTyp;

BEGIN

   aTrnslFrom:='0123456789,.';
   aTrnslTo:='0123456789.,';

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale di recupero delle annotazioni recupero dati di ESTRAZIONE_CUD

   BEGIN

      OPEN gen_cur FOR

           SELECT dip_cd_anag,
                  dip_rp_reddito_complessivo,
                  dip_rp_no_tax_area,
                  dip_rp_deduzione_fissa_intera,
                  fl_applic_aliquota_max,
                  fl_no_applic_deduzione,
                  gg_periodo_lavoro,
                  deduzione_dovuta,
                  imponibile_si_detr,
                  imponibile_no_detr,
                  addizionale_regionale,
                  addizionale_comunale,
                  fl_soggetto_estero
           FROM   ESTRAZIONE_CUD
           WHERE  id_estrazione = inRepID AND
                  esercizio = inEsercizio;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCud.dip_cd_anag,
               aRecEstrazioneCud.dip_rp_reddito_complessivo,
               aRecEstrazioneCud.dip_rp_no_tax_area,
               aRecEstrazioneCud.dip_rp_deduzione_fissa_intera,
               aRecEstrazioneCud.fl_applic_aliquota_max,
               aRecEstrazioneCud.fl_no_applic_deduzione,
               aRecEstrazioneCud.gg_periodo_lavoro,
               aRecEstrazioneCud.deduzione_dovuta,
               aRecEstrazioneCud.imponibile_si_detr,
               aRecEstrazioneCud.imponibile_no_detr,
               aRecEstrazioneCud.addizionale_regionale,
               aRecEstrazioneCud.addizionale_comunale,
               aRecEstrazioneCud.fl_soggetto_estero;

         EXIT WHEN gen_cur%NOTFOUND;

         memAnnotazione:=NULL;
         scrittoArt1:='N';
         scrittoArt2:='N';
         scrittoPeriodoGG:='N';
         memAnnotazioneDate:=NULL;
         aDataDaTmp:=NULL;
         aDataATmp:=NULL;
         imDeduzione1:=0;
         imDeduzione2:=0;

         -- Annotazioni per tipologia reddito ---------------------------------------------------

         BEGIN

            OPEN gen_cur_a FOR

                 SELECT *
                 FROM   ESTRAZIONE_CUD_ANNOTAZIONI
                 WHERE  id_estrazione = inRepID AND
                        esercizio = inEsercizio AND
                        dip_cd_anag = aRecEstrazioneCud.dip_cd_anag AND
                        pg_ordine_annotazione = 1
                 ORDER BY pg_tipo_annotazione,
                          cd_tipologia_reddito;

            LOOP

               FETCH gen_cur_a INTO
                     aRecEstrazioneCudAnnotaz;

               EXIT WHEN gen_cur_a%NOTFOUND;

               IF aRecEstrazioneCudAnnotaz.pg_tipo_annotazione = 1 THEN
                  IF scrittoArt1 = 'N' THEN
                     scrittoArt1:= 'Y';
                     memAnnotazione:=memAnnotazione ||
                                     'L''importo indicato al punto 1 della parte B dati fiscali è così composto: ' || CHR(10);
                  END IF;
                  aStringaImporto:=LTRIM(TO_CHAR(aRecEstrazioneCudAnnotaz.imponibile,'9999G999G999G999D99'));
                  IF LENGTH(aStringaImporto) - INSTR(aStringaImporto,',',-1) != 2 THEN
                     aStringaImporto:=TRANSLATE(aStringaImporto,aTrnslFrom,aTrnslTo);
                  END IF;
                  IF LENGTH(aStringaImporto) = 3 THEN
                     aStringaImporto:='0' || aStringaImporto;
                  END IF;
                  memAnnotazione:=memAnnotazione || 'Redditi per ' || aRecEstrazioneCudAnnotaz.ds_tipologia_reddito ||
                                  ' euro ' || aStringaImporto || CHR(10);

               ELSE
                  IF scrittoArt2 = 'N' THEN
                     scrittoArt2:= 'Y';
                     memAnnotazione:=memAnnotazione ||
                                     'L''importo indicato al punto 2 della parte B dati fiscali è così composto: ' || CHR(10);
                  END IF;
                  aStringaImporto:=LTRIM(TO_CHAR(aRecEstrazioneCudAnnotaz.imponibile,'9999G999G999G999D99'));
                  IF LENGTH(aStringaImporto) - INSTR(aStringaImporto,',',-1) != 2 THEN
                     aStringaImporto:=TRANSLATE(aStringaImporto,aTrnslFrom,aTrnslTo);
                  END IF;
                  IF LENGTH(aStringaImporto) = 3 THEN
                     aStringaImporto:='0' || aStringaImporto;
                  END IF;
                  memAnnotazione:=memAnnotazione || 'Redditi per ' || aRecEstrazioneCudAnnotaz.ds_tipologia_reddito ||
                                  ' euro ' || aStringaImporto || CHR(10);
               END IF;

            END LOOP;

            CLOSE gen_cur_a;

         END;

         -- Annotazioni per gestiona aliquota_max -----------------------------------------------

         BEGIN

            IF aRecEstrazioneCud.fl_applic_aliquota_max = 'Y' THEN
               memAnnotazione:=memAnnotazione || parametri_tab(13).stringa || CHR(10);
            END IF;

         END;

         -- Annotazioni per intervallo date giorni lavoro ---------------------------------------

         BEGIN

            IF (aRecEstrazioneCud.gg_periodo_lavoro < maxGGAnno AND
                aRecEstrazioneCud.gg_periodo_lavoro != 0 ) THEN

               OPEN gen_cur_a FOR

                    SELECT *
                    FROM   ESTRAZIONE_CUD_ANNOTAZIONI
                    WHERE  id_estrazione = inRepID AND
                           esercizio = inEsercizio AND
                           dip_cd_anag = aRecEstrazioneCud.dip_cd_anag AND
                           pg_ordine_annotazione = 3
                    ORDER BY data_rif_da;

               LOOP

                  FETCH gen_cur_a INTO
                        aRecEstrazioneCudAnnotaz;

                  EXIT WHEN gen_cur_a%NOTFOUND;

                  -- E' la prima volta che leggo un intervallo temporale

                  IF scrittoPeriodoGG = 'N' THEN
                     scrittoPeriodoGG:= 'Y';
                     memAnnotazione:=memAnnotazione || parametri_tab(17).stringa || CHR(10);
                     aDataDaTmp:=aRecEstrazioneCudAnnotaz.data_rif_da;
                     aDataATmp:=aRecEstrazioneCudAnnotaz.data_rif_a;
                  ELSE

                     -- Controllo intervalli temporali contigui

                     IF aRecEstrazioneCudAnnotaz.data_rif_da = aDataATmp + 1 THEN
                        aDataATmp:=aRecEstrazioneCudAnnotaz.data_rif_a;
                     ELSE
                        IF memAnnotazioneDate IS NULL THEN
                           memAnnotazioneDate:=memAnnotazioneDate || 'Dal ' || TO_CHAR(aDataDaTmp,'DD/MM/YYYY') ||
                                              ' Al ' || TO_CHAR(aDataATmp,'DD/MM/YYYY');
                           aDataDaTmp:=aRecEstrazioneCudAnnotaz.data_rif_da;
                           aDataATmp:=aRecEstrazioneCudAnnotaz.data_rif_a;
                        ELSE
                           memAnnotazioneDate:=memAnnotazioneDate || ', Dal ' || TO_CHAR(aDataDaTmp,'DD/MM/YYYY') ||
                                               ' Al ' || TO_CHAR(aDataATmp,'DD/MM/YYYY');
                           aDataDaTmp:=aRecEstrazioneCudAnnotaz.data_rif_da;
                           aDataATmp:=aRecEstrazioneCudAnnotaz.data_rif_a;
                        END IF;
                     END IF;
                  END IF;

               END LOOP;

               CLOSE gen_cur_a;

               -- Scrittura ultimo record tmp delle annotazioni date

               IF memAnnotazioneDate IS NULL THEN
                  memAnnotazioneDate:=memAnnotazioneDate || 'Dal ' || TO_CHAR(aDataDaTmp,'DD/MM/YYYY') ||
                                      ' Al ' || TO_CHAR(aDataATmp,'DD/MM/YYYY');
               ELSE
                  memAnnotazioneDate:=memAnnotazioneDate || ', Dal ' || TO_CHAR(aDataDaTmp,'DD/MM/YYYY') ||
                                      ' Al ' || TO_CHAR(aDataATmp,'DD/MM/YYYY');
               END IF;

               memAnnotazione:=memAnnotazione || memAnnotazioneDate || CHR(10);

            END IF;

         END;

         -- Annotazioni per gestione deduzione --------------------------------------------------

         BEGIN

            IF aRecEstrazioneCud.deduzione_dovuta != 0 THEN

               IF aRecEstrazioneCud.dip_rp_deduzione_fissa_intera = 'N' THEN

                  imDeduzione1:=ROUND((aRecEstrazioneCud.deduzione_dovuta * 40 / 100),2);
                  imDeduzione2:=aRecEstrazioneCud.deduzione_dovuta - imDeduzione1;

                  aStringaImporto:=LTRIM(TO_CHAR(imDeduzione1,'9999G999G999G999D99'));
                  IF LENGTH(aStringaImporto) - INSTR(aStringaImporto,',',-1) != 2 THEN
                     aStringaImporto:=TRANSLATE(aStringaImporto,aTrnslFrom,aTrnslTo);
                  END IF;
                  IF LENGTH(aStringaImporto) = 3 THEN
                     aStringaImporto:='0' || aStringaImporto;
                  END IF;
                  aStringaImporto1:=LTRIM(TO_CHAR(imDeduzione2,'9999G999G999G999D99'));
                  IF LENGTH(aStringaImporto1) - INSTR(aStringaImporto1,',',-1) != 2 THEN
                     aStringaImporto1:=TRANSLATE(aStringaImporto1,aTrnslFrom,aTrnslTo);
                  END IF;
                  IF LENGTH(aStringaImporto1) = 3 THEN
                     aStringaImporto1:='0' || aStringaImporto1;
                  END IF;

                  memAnnotazione:=memAnnotazione ||
                                  parametri_tab(18).stringa || aStringaImporto ||
                                  parametri_tab(19).stringa || aStringaImporto1 || CHR(10);

               ELSE

                  IF aRecEstrazioneCud.deduzione_dovuta > 3000 THEN
                     imDeduzione1:=3000;
                     imDeduzione2:=aRecEstrazioneCud.deduzione_dovuta - imDeduzione1;
                  ELSE
                     imDeduzione1:=aRecEstrazioneCud.deduzione_dovuta;
                     imDeduzione2:=0;
                  END IF;

                  aStringaImporto:=LTRIM(TO_CHAR(imDeduzione1,'9999G999G999G999D99'));
                  IF LENGTH(aStringaImporto) - INSTR(aStringaImporto,',',-1) != 2 THEN
                     aStringaImporto:=TRANSLATE(aStringaImporto,aTrnslFrom,aTrnslTo);
                  END IF;
                  IF LENGTH(aStringaImporto) = 3 THEN
                     aStringaImporto:='0' || aStringaImporto;
                  END IF;
                  aStringaImporto1:=LTRIM(TO_CHAR(imDeduzione2,'9999G999G999G999D99'));
                  IF LENGTH(aStringaImporto1) - INSTR(aStringaImporto1,',',-1) != 2 THEN
                     aStringaImporto1:=TRANSLATE(aStringaImporto1,aTrnslFrom,aTrnslTo);
                  END IF;
                  IF LENGTH(aStringaImporto1) = 3 THEN
                     aStringaImporto1:='0' || aStringaImporto1;
                  END IF;

                  memAnnotazione:=memAnnotazione ||
                                  parametri_tab(20).stringa || aStringaImporto ||
                                  parametri_tab(21).stringa || aStringaImporto1 || CHR(10);

               END IF;

            END IF;

         END;

         -- Annotazioni per gestione reddito complessivo ----------------------------------------

         BEGIN

            IF aRecEstrazioneCud.dip_rp_reddito_complessivo >
               (aRecEstrazioneCud.imponibile_si_detr + aRecEstrazioneCud.imponibile_no_detr) THEN

               aStringaImporto:=LTRIM(TO_CHAR(aRecEstrazioneCud.dip_rp_reddito_complessivo,'9999G999G999G999D99'));
               IF LENGTH(aStringaImporto) - INSTR(aStringaImporto,',',-1) != 2 THEN
                  aStringaImporto:=TRANSLATE(aStringaImporto,aTrnslFrom,aTrnslTo);
               END IF;
               IF LENGTH(aStringaImporto) = 3 THEN
                  aStringaImporto:='0' || aStringaImporto;
               END IF;

               memAnnotazione:=memAnnotazione || parametri_tab(22).stringa || aStringaImporto || CHR(10);

            END IF;

         END;

         -- Annotazioni per gestione soggetto estero --------------------------------------------

         BEGIN

            IF aRecEstrazioneCud.fl_soggetto_estero = 'Y' THEN

               memAnnotazione:=memAnnotazione || parametri_tab(16).stringa || CHR(10);

            END IF;

         END;

         -- Annotazioni per gestione addizionali regionali e comunali ---------------------------

         BEGIN

            IF (aRecEstrazioneCud.addizionale_regionale != 0 OR
                aRecEstrazioneCud.addizionale_comunale != 0) THEN

               IF (aRecEstrazioneCud.add_reg_accantonato = 0 AND
                   aRecEstrazioneCud.add_com_accantonato = 0) THEN
                  memAnnotazione:=memAnnotazione || parametri_tab(14).stringa || CHR(10);
               END IF;

            END IF;

         END;

         -- Aggiornamento attributo annotazioni_1 di ESTRAZIONE_CUD -----------------------------

         BEGIN

            UPDATE ESTRAZIONE_CUD
            SET    annotazioni_1 = memAnnotazione
            WHERE  id_estrazione = inRepID AND
                   esercizio = inEsercizio AND
                   dip_cd_anag = aRecEstrazioneCud.dip_cd_anag AND
                   rigo_cococo = 1;

         END;

      END LOOP;

      CLOSE gen_cur;

   END;

END insDatiAnnotazioniCud;

-- =================================================================================================
-- Determino la tipologia di reddito principale per CNR
-- =================================================================================================
PROCEDURE calcolaTipologiaReddito
   (
    inEsercizio VARCHAR2,
    inRepID INTEGER
   ) IS

   memImponibile1 NUMBER(15,2);
   memImponibile2 NUMBER(15,2);

   aRecEstrazioneCud ESTRAZIONE_CUD%ROWTYPE;
   aRecEstrazioneCudAnnotaz ESTRAZIONE_CUD_ANNOTAZIONI%ROWTYPE;

   gen_cur GenericCurTyp;
   gen_cur_a GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale di recupero delle annotazioni recupero dati di ESTRAZIONE_CUD

   BEGIN

      OPEN gen_cur FOR

           SELECT dip_cd_anag,
                  dip_rp_reddito_complessivo,
                  dip_rp_no_tax_area,
                  dip_rp_deduzione_fissa_intera,
                  fl_applic_aliquota_max,
                  fl_no_applic_deduzione,
                  deduzione_dovuta,
                  imponibile_si_detr,
                  imponibile_no_detr
           FROM   ESTRAZIONE_CUD
           WHERE  id_estrazione = inRepID AND
                  esercizio = inEsercizio;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCud.dip_cd_anag,
               aRecEstrazioneCud.dip_rp_reddito_complessivo,
               aRecEstrazioneCud.dip_rp_no_tax_area,
               aRecEstrazioneCud.dip_rp_deduzione_fissa_intera,
               aRecEstrazioneCud.fl_applic_aliquota_max,
               aRecEstrazioneCud.fl_no_applic_deduzione,
               aRecEstrazioneCud.deduzione_dovuta,
               aRecEstrazioneCud.imponibile_si_detr,
               aRecEstrazioneCud.imponibile_no_detr;

         EXIT WHEN gen_cur%NOTFOUND;

         aRecEstrazioneCud.cnr_tipologia_reddito_punto_1:=NULL;
         aRecEstrazioneCud.cnr_tempo_determinato:='001';
         aRecEstrazioneCud.cnr_tipologia_reddito_punto_2:=NULL;
         aRecEstrazioneCud.cnr_deduzione_fissa_3000:=0;
         aRecEstrazioneCud.cnr_deduzione_fissa_4500:=0;
         memImponibile1:=0;
         memImponibile2:=0;

         -- Calcola tipologia di reddito --------------------------------------------------------

         BEGIN

            OPEN gen_cur_a FOR

                 SELECT *
                 FROM   ESTRAZIONE_CUD_ANNOTAZIONI
                 WHERE  id_estrazione = inRepID AND
                        esercizio = inEsercizio AND
                        dip_cd_anag = aRecEstrazioneCud.dip_cd_anag AND
                        pg_ordine_annotazione = 1
                 ORDER BY pg_tipo_annotazione,
                          cd_tipologia_reddito;

            LOOP

               FETCH gen_cur_a INTO
                     aRecEstrazioneCudAnnotaz;

               EXIT WHEN gen_cur_a%NOTFOUND;

               IF aRecEstrazioneCudAnnotaz.pg_tipo_annotazione = 1 THEN
                  IF ABS(aRecEstrazioneCudAnnotaz.imponibile) > memImponibile1 THEN
                     aRecEstrazioneCud.cnr_tipologia_reddito_punto_1:=aRecEstrazioneCudAnnotaz.cd_tipologia_reddito;
                     memImponibile1:=ABS(aRecEstrazioneCudAnnotaz.imponibile);
                  END IF;
               ELSE
                  IF ABS(aRecEstrazioneCudAnnotaz.imponibile) > memImponibile2 THEN
                     aRecEstrazioneCud.cnr_tipologia_reddito_punto_2:=aRecEstrazioneCudAnnotaz.cd_tipologia_reddito;
                     memImponibile2:=ABS(aRecEstrazioneCudAnnotaz.imponibile);
                  END IF;
               END IF;

            END LOOP;

            CLOSE gen_cur_a;

         END;

         -- Gestione deduzione ------------------------------------------------------------------

         BEGIN

            IF aRecEstrazioneCud.deduzione_dovuta != 0 THEN

               -- Se esistono solo imponibili per puto 2 tutto è sempre gestito nelle 3000

               IF (aRecEstrazioneCud.imponibile_si_detr = 0 AND
                   aRecEstrazioneCud.imponibile_no_detr != 0) THEN

                  IF aRecEstrazioneCud.deduzione_dovuta > 3000 THEN
                     aRecEstrazioneCud.cnr_deduzione_fissa_3000:=3000;
                     aRecEstrazioneCud.cnr_deduzione_fissa_4500:=aRecEstrazioneCud.deduzione_dovuta -
                                                                 aRecEstrazioneCud.cnr_deduzione_fissa_3000;
                  ELSE
                     aRecEstrazioneCud.cnr_deduzione_fissa_3000:=aRecEstrazioneCud.deduzione_dovuta;
                     aRecEstrazioneCud.cnr_deduzione_fissa_4500:=0;
                  END IF;

               -- Esistono imponibili per punto 1

               ELSE

                  IF aRecEstrazioneCud.dip_rp_deduzione_fissa_intera = 'N' THEN

                     aRecEstrazioneCud.cnr_deduzione_fissa_3000:=ROUND((aRecEstrazioneCud.deduzione_dovuta * 40 / 100),2);
                     aRecEstrazioneCud.cnr_deduzione_fissa_4500:=aRecEstrazioneCud.deduzione_dovuta -
                                                                 aRecEstrazioneCud.cnr_deduzione_fissa_3000;

                  ELSE

                     IF aRecEstrazioneCud.deduzione_dovuta > 3000 THEN
                        aRecEstrazioneCud.cnr_deduzione_fissa_3000:=3000;
                        aRecEstrazioneCud.cnr_deduzione_fissa_4500:=aRecEstrazioneCud.deduzione_dovuta -
                                                                    aRecEstrazioneCud.cnr_deduzione_fissa_3000;
                     ELSE
                        aRecEstrazioneCud.cnr_deduzione_fissa_3000:=aRecEstrazioneCud.deduzione_dovuta;
                        aRecEstrazioneCud.cnr_deduzione_fissa_4500:=0;
                     END IF;

                  END IF;

               END IF;

            END IF;

         END;

         -- Aggiornamento attributi CNR di ESTRAZIONE_CUD ---------------------------------------

         BEGIN

            UPDATE ESTRAZIONE_CUD
            SET    cnr_tipologia_reddito_punto_1 = aRecEstrazioneCud.cnr_tipologia_reddito_punto_1,
                   cnr_tempo_determinato = aRecEstrazioneCud.cnr_tempo_determinato,
                   cnr_tipologia_reddito_punto_2 = aRecEstrazioneCud.cnr_tipologia_reddito_punto_2,
                   cnr_deduzione_fissa_3000 = aRecEstrazioneCud.cnr_deduzione_fissa_3000,
                   cnr_deduzione_fissa_4500 = aRecEstrazioneCud.cnr_deduzione_fissa_4500
            WHERE  id_estrazione = inRepID AND
                   esercizio = inEsercizio AND
                   dip_cd_anag = aRecEstrazioneCud.dip_cd_anag AND
                   rigo_cococo = 1;

         END;

      END LOOP;

      CLOSE gen_cur;

   END;

END calcolaTipologiaReddito;

-- =================================================================================================
-- Determino l'acconto per l'addizionale comunale
-- =================================================================================================
PROCEDURE calcolaAccontoAddCom
   (
    inEsercizio VARCHAR2,
    inRepID INTEGER
   ) IS

   acconto_es_suc            NUMBER(15,2):=0;
   cod_com_acc_es_suc        VARCHAR2(10);
   pg_com_acc_es_suc         NUMBER(10);
   data_acc_es_suc           DATE;
   esenzione_add_com_es_suc  NUMBER(15,2):=0;
   acconto_es                NUMBER(15,2):=0;
   cod_com_acc_es            VARCHAR2(10);
   esenzione_add_com_saldo   NUMBER(15,2):=0;
   data_ultimo_cong          DATE;
   pg_com_domfisc            NUMBER(10);

   aRecEstrazioneCud ESTRAZIONE_CUD%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   BEGIN
      OPEN gen_cur FOR

           SELECT dip_cd_anag, dip_cod_com_domfisc
           FROM   ESTRAZIONE_CUD
           WHERE  id_estrazione = inRepID AND
                  esercizio = inEsercizio;
      LOOP
         FETCH gen_cur INTO
               aRecEstrazioneCud.dip_cd_anag,
               aRecEstrazioneCud.dip_cod_com_domfisc;

         EXIT WHEN gen_cur%NOTFOUND;
         --Prendo l'acconto calcolato per l'esercizio successivo
       Begin
             Select Nvl(a.im_acconto_calcolato,0), c.cd_catastale, a.pg_comune, a.dacr
             Into acconto_es_suc, cod_com_acc_es_suc, pg_com_acc_es_suc, data_acc_es_suc
             From acconto_classific_cori a, comune c
             Where a.pg_comune = c.pg_comune
               And a.esercizio = inEsercizio+1
               And a.cd_anag = aRecEstrazioneCud.dip_cd_anag
               And a.cd_classificazione_cori = CNRCTB545.isCoriAddCom;
         Exception
             When No_Data_Found Then
                acconto_es_suc := 0;
                cod_com_acc_es_suc := aRecEstrazioneCud.dip_cod_com_domfisc;
                Begin
                  Select pg_comune
                  Into pg_com_acc_es_suc
                  From comune
                  Where cd_catastale = cod_com_acc_es_suc
                    And ti_italiano_estero = 'I';
                Exception
                    When No_Data_Found Then
                        pg_com_acc_es_suc := Null;
                    When Too_Many_Rows Then
                        pg_com_acc_es_suc := Null;
                End;
                data_acc_es_suc := dataOdierna;
         End;
         --Prendo l'eventuale esenzione dei comuni presi in considerazione per acconto esercizio successivo
         --Se ho calcolato l'acconto, prendo l'esenzione del comune presente nell'acconto in data di creazione dell'acconto
         --Se non ho calcolato l'acconto, prendo l'esenzione del comune presente in anagrafica in data odierna
         If pg_com_acc_es_suc Is Not Null Then
             esenzione_add_com_es_suc := CNRCTB545.getEsenzioniAddcom(pg_com_acc_es_suc,data_acc_es_suc).importo;
         End If;
         --Prendo l'acconto trattenuto per l'esercizio dei dati del CUD
       Begin
             Select Nvl(a.im_acconto_trattenuto,0), c.cd_catastale
             Into acconto_es, cod_com_acc_es
             From acconto_classific_cori a, comune c
             Where a.pg_comune = c.pg_comune
               And a.esercizio = inEsercizio
               And a.cd_anag = aRecEstrazioneCud.dip_cd_anag
               And a.cd_classificazione_cori = CNRCTB545.isCoriAddCom;
         Exception
             When No_Data_Found Then
                acconto_es := 0;
                cod_com_acc_es := aRecEstrazioneCud.dip_cod_com_domfisc;
         End;
       --Prendo l'eventuale esenzione dei comuni presi in considerazione per il saldo addiz. com. (data ultimo cong)
       --
       Begin
          Select dacr_conguaglio
          Into data_ultimo_cong
          From estrazione_cud_dett
          Where id_estrazione = inRepID
              And esercizio = inEsercizio
              And cd_anag = aRecEstrazioneCud.dip_cd_anag
              And fl_compenso_conguaglio = 'Y'
              And fl_ultimo_conguaglio = 'Y';
         Exception
            When No_Data_Found Then
               data_ultimo_cong := dataOdierna;
            When Too_Many_Rows Then
               data_ultimo_cong := dataOdierna;
         End;
         Begin
            Select pg_comune
            Into pg_com_domfisc
            From comune
            Where cd_catastale = aRecEstrazioneCud.dip_cod_com_domfisc
              And ti_italiano_estero = 'I';
         Exception
               When No_Data_Found Then
                    pg_com_domfisc := Null;
               When Too_Many_Rows Then
                    pg_com_domfisc := Null;
         End;
         If pg_com_domfisc Is Not Null Then
            esenzione_add_com_saldo := CNRCTB545.getEsenzioniAddcom(pg_com_domfisc,data_ultimo_cong).importo;
         End If;
         Begin
            UPDATE ESTRAZIONE_CUD
            SET    im_acconto_add_com_es_suc = acconto_es_suc,
                   dip_cod_com_acc_es_suc = cod_com_acc_es_suc,
                   im_esenz_add_com_es_suc = esenzione_add_com_es_suc,
                   im_acconto_add_com = acconto_es,
                   dip_cod_com_acc_es = cod_com_acc_es,
                   im_esenz_add_com_es = esenzione_add_com_saldo
            WHERE  id_estrazione = inRepID AND
                   esercizio = inEsercizio AND
                   dip_cd_anag = aRecEstrazioneCud.dip_cd_anag AND
                   rigo_cococo = 1;
         End;
      END LOOP;

      CLOSE gen_cur;

   END;

   --Blocco aggiunto per sanare casi sporchi
   --Esistono diversi conguagli non contabilizzati che quindi non vengono inseriti nel cud (il che è giusto)
   --però se per essi esiste una rateizzazione (calcolata con il conguaglio), essa non viene presa
   /*
   Declare
      add_reg_rat  NUMBER(15,2):=0;
      add_com_rat  NUMBER(15,2):=0;
   Begin
     Open gen_cur FOR

        SELECT dip_cd_anag
        FROM   ESTRAZIONE_CUD
        WHERE  id_estrazione = inRepID AND
               esercizio = inEsercizio And
               dip_cd_anag In (8392,11751,12083,15256,20210,22150,25626,26295,48489,48576,60281,64656,66401,
                               68623,79333,80356,91528,91529,91530,91532,93966,94693,102150) And
               Nvl(add_reg_accantonato,0) = 0 And
               Nvl(add_com_accantonato,0) = 0;
     Loop
         FETCH gen_cur INTO
               aRecEstrazioneCud.dip_cd_anag;

         EXIT WHEN gen_cur%NOTFOUND;

         --Prendo le addizionali rateizzate
       Begin
             Select Nvl(im_da_rateizzare,0)
             Into add_reg_rat
             From rateizza_classific_cori
             Where esercizio = inEsercizio
               And cd_anag = aRecEstrazioneCud.dip_cd_anag
               And cd_classificazione_cori = CNRCTB545.isCoriAddReg;
         Exception
             When No_Data_Found Then
                 Null;
         End;
         Begin
             Select Nvl(im_da_rateizzare,0)
             Into add_com_rat
             From rateizza_classific_cori
             Where esercizio = inEsercizio
               And cd_anag = aRecEstrazioneCud.dip_cd_anag
               And cd_classificazione_cori = CNRCTB545.isCoriAddCom;
         Exception
             When No_Data_Found Then
                 Null;
         End;
         Begin
            UPDATE ESTRAZIONE_CUD
            SET    add_reg_accantonato = add_reg_rat,
                   add_com_accantonato = add_com_rat
            WHERE  id_estrazione = inRepID AND
                   esercizio = inEsercizio AND
                   dip_cd_anag = aRecEstrazioneCud.dip_cd_anag AND
                   rigo_cococo = 1;
         End;
     End Loop;

     Close gen_cur;

   End;
   */
END calcolaAccontoAddCom;

-- =================================================================================================
-- Creazione File output
-- =================================================================================================
PROCEDURE scriviFileOutput
   (
    inEsercizio NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   mioCLOB CLOB;
   aStringaChiave1 VARCHAR2(5);
   aStringaChiave2 VARCHAR2(8);
   aStringaChiave3 VARCHAR2(6);
   aStringa VARCHAR2(1000);
   aSalta VARCHAR2(1):='N';
   aDifferenza INTEGER;

   aStringaImporto VARCHAR2(10);

   i BINARY_INTEGER;

   aRecBframeBlob BFRAME_BLOB%ROWTYPE;
   aRecEstrazioneCud ESTRAZIONE_CUD%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazione CLOB

   aRecBframeBlob.cd_tipo:=IDTIPOBLOB;
   aRecBframeBlob.path:='fileoutCUD/' || inEsercizio || '/';
   aRecBframeBlob.filename:='FILEOUT_CUD_' || inRepID || '.DAT';
   aRecBframeBlob.ti_visibilita:='U';
   aRecBframeBlob.ds_file:='FILE OUTPUT ESTRAZIONE CUD';
   aRecBframeBlob.ds_utente:='FILE OUTPUT ESTRAZIONE CUD';

   IBMUTL005.ShIniCBlob(aRecBframeBlob.cd_tipo,
                        aRecBframeBlob.path,
                        aRecBframeBlob.filename,
                        aRecBframeBlob.ti_visibilita,
                        aRecBframeBlob.ds_file,
                        aRecBframeBlob.ds_utente,
                        inUtente,
                        mioCLOB);

   -------------------------------------------------------------------------------------------------
   -- Scrittura CLOB (file output estrazione CUD)

   BEGIN

      -- Valorizzazione costanti della chiave file -------------------------------------------------

      aStringaChiave2:='94109411';

      -- Ciclo elaborazione dati CUD ---------------------------------------------------------------
      -- Escluse le anagrafiche che non hanno imponibile fiscale/previdenziale
      OPEN gen_cur FOR

           SELECT *
           FROM   ESTRAZIONE_CUD
           WHERE  id_estrazione = inRepID
             AND  esercizio = inEsercizio
             AND  (IMPONIBILE_NO_DETR != 0
                   OR IMPONIBILE_SI_DETR != 0
                   OR IMPONIBILE_NO_DETR_TASSEP != 0
                   OR IMPONIBILE_NON_RESIDENTI != 0
                   OR IMPONIBILE_SI_DETR_TASSEP != 0
                   OR COMPENSI_CORRISPOSTI_COCOCO != 0
                   OR IM_REDD_NON_TASSATI_PER_CONV != 0
                   OR IM_REDD_ESENTI_PER_LEGGE != 0)
           ORDER BY dip_codice_fiscale;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneCud;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Creazione di due tipi record (anagrafico e dettaglio)

         FOR i IN 1 .. 3

         LOOP

            aStringa:=NULL;
            aSalta := 'N';
            -- Dettaglio anagrafico ----------------------------------------------------------------

            IF i = 1 THEN

               -- Chiave

               aStringaChiave1:='1010 ';
               aStringaChiave3:=LPAD((300000 + aRecEstrazioneCud.dip_cd_anag),6,'0');
               aStringa:=aStringaChiave1 || aStringaChiave2 || aStringaChiave3;

               -- Cognome

               IF aRecEstrazioneCud.dip_cognome IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_cognome), 1, 24), 24, ' ');
               END IF;

               -- Nome

               IF aRecEstrazioneCud.dip_nome IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_nome), 1, 24), 24, ' ');
               END IF;

               -- Data assunzione (data minima di inizio competenza dei compensi elaborati)

               IF aRecEstrazioneCud.cnr_data_assunzione IS NULL THEN
                  aStringa:=aStringa || LPAD('0', 8, '0');
               ELSE
                  aStringa:=aStringa ||
                            TO_CHAR(aRecEstrazioneCud.cnr_data_assunzione, 'DD') ||
                            TO_CHAR(aRecEstrazioneCud.cnr_data_assunzione, 'MM') ||
                            TO_CHAR(aRecEstrazioneCud.cnr_data_assunzione, 'YYYY');
               END IF;

               -- Codice fiscale

               IF aRecEstrazioneCud.dip_codice_fiscale IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 16, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_codice_fiscale), 1, 16), 16, ' ');
               END IF;

               -- Data di nascita

               IF (aRecEstrazioneCud.dip_gg_nascita IS NULL OR
                   aRecEstrazioneCud.dip_mm_nascita IS NULL OR
                   aRecEstrazioneCud.dip_aa_nascita IS NULL) THEN
                  aStringa:=aStringa || LPAD('0', 8, '0');
               ELSE
                  aStringa:=aStringa || LPAD(aRecEstrazioneCud.dip_gg_nascita, 2, '0') ||
                                        LPAD(aRecEstrazioneCud.dip_mm_nascita, 2, '0') ||
                                        LPAD(aRecEstrazioneCud.dip_aa_nascita, 4, '0');
               END IF;

               -- Comune di nascita

               IF aRecEstrazioneCud.dip_com_nascita IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_com_nascita), 1, 24), 24, ' ');
               END IF;

               -- Provincia di nascita

               IF aRecEstrazioneCud.dip_prv_nascita IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 3, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_prv_nascita), 1, 3), 3, ' ');
               END IF;

               -- Sesso

               IF aRecEstrazioneCud.dip_sesso IS NULL THEN
                  aStringa:=aStringa || ' ';
               ELSE
                  aStringa:=aStringa || Upper(aRecEstrazioneCud.dip_sesso);
               END IF;

               -- Provincia di residenza

               IF aRecEstrazioneCud.dip_prv_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 3, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_prv_domfisc), 1, 3), 3, ' ');
               END IF;

               -- Data cessazione (data massima di fine competenza dei compensi elaborati)

               IF aRecEstrazioneCud.cnr_data_cessazione IS NULL THEN
                  aStringa:=aStringa || LPAD('0', 8, '0');
               ELSE
                  aStringa:=aStringa ||
                            TO_CHAR(aRecEstrazioneCud.cnr_data_cessazione, 'DD') ||
                            TO_CHAR(aRecEstrazioneCud.cnr_data_cessazione, 'MM') ||
                            TO_CHAR(aRecEstrazioneCud.cnr_data_cessazione, 'YYYY');
               END IF;

               -- Filler

               aStringa:=aStringa || RPAD(' ', 2, ' ');

               -- Indirizzo del domicilio fiscale

               IF aRecEstrazioneCud.dip_ind_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_ind_domfisc), 1, 24), 24, ' ');
               END IF;

               -- CAP del domicilio fiscale

               IF aRecEstrazioneCud.dip_cap_domfisc IS NULL THEN
                  aStringa:=aStringa || LPAD('0', 5, '0');
               ELSE
                  IF IBMUTL001.isNumeric(aRecEstrazioneCud.dip_cap_domfisc) = 'Y' THEN
                     aStringa:=aStringa || LPAD(aRecEstrazioneCud.dip_cap_domfisc, 5, '0');
                  ELSE
                     aStringa:=aStringa || LPAD('0', 5, '0');
                  END IF;
               END IF;

               -- Provincia del domicilio fiscale

               IF aRecEstrazioneCud.dip_prv_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 3, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_prv_domfisc), 1, 3), 3, ' ');
               END IF;

               -- Comune del domicilio fiscale

               IF aRecEstrazioneCud.dip_com_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_com_domfisc), 1, 24), 24, ' ');
               END IF;

               IF aRecEstrazioneCud.dip_com_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  IF LENGTH(aRecEstrazioneCud.dip_com_domfisc) > 24 THEN
                     aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_com_domfisc), 25, 24), 24, ' ');
                  ELSE
                     aStringa:=aStringa || RPAD(' ', 24, ' ');
                  END IF;
               END IF;

               -- Qualifica 770/SA

               aStringa:=aStringa || RPAD(' ', 5, ' ');

               -- Codice comune alla data 01/01/esercizio
               --(è il comune con cui è stato calcolato l'acconto per l'esercizio a cui si riferisce il CUD
               --e se non c'è nessun acconto è quello dell'anagrafica)

               IF aRecEstrazioneCud.dip_cod_com_acc_es IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 4, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_cod_com_acc_es), 1, 4), 4, ' ');
               END IF;

               -- Provincia lavoro

               aStringa:=aStringa || LPAD('9', 6, '9');

             -- Indirizzo Domicilio Fiscale completo solo per la stampa delle Etichette
               IF aRecEstrazioneCud.dip_ind_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 100, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_ind_domfisc), 1, 100), 100, ' ');
               END IF;

             -- Codice comune con cui è stato calcolato l'acconto addizionale comunale per l'esercizio successivo
             -- Altrimenti è quello dell'anagrafica

               IF aRecEstrazioneCud.dip_cod_com_acc_es_suc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 4, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_cod_com_acc_es_suc), 1, 4), 4, ' ');
               END IF;

             -- Codice comune alla data 31/12/esercizio (uguale a Codice comune del domicilio fiscale)

               IF aRecEstrazioneCud.dip_cod_com_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 4, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneCud.dip_cod_com_domfisc), 1, 4), 4, ' ');
               END IF;

               IF LENGTH(aStringa) != 343 THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Errore in lunghezza file in output tipo record 1010 lunghezza ' || LENGTH(aStringa));
               END IF;

            -- Dettaglio importi -------------------------------------------------------------------

            Elsif i = 2 Then
               -- per i terzi che hanno compensi solo da non residenti (solo cococo) devo prendere solo i dati inps
               -- quindi passo anche il record 2010

               If aRecEstrazioneCud.fl_soggetto_estero = 'Y' And
                  Nvl(aRecEstrazioneCud.imponibile_si_detr,0) = 0 And
                  Nvl(aRecEstrazioneCud.imponibile_no_detr,0) = 0 Then

                  -- Chiave

                  aStringaChiave1:='2010 ';
                  aStringaChiave3:=LPAD((300000 + aRecEstrazioneCud.dip_cd_anag),6,'0');
                  aStringa:=aStringaChiave1 || aStringaChiave2 || aStringaChiave3;

                  -- Campi contabili

                  aStringa:=aStringa ||
                            LPAD('0', 159, '0') ||
                            LPAD((aRecEstrazioneCud.compensi_corrisposti_cococo * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.contributi_dovuti_cococo * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.contributi_trattenuti_cococo * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.contributi_versati_cococo * 100), 10, '0') ||
                            LPAD('0', 10, '0') ||
                            LPAD(' ', 5, ' ') ||
                            LPAD('0', 41, '0') ||
                            LPAD(' ', 1, ' ') ||
                            LPAD('0', 18, '0') ||
                            LPAD(' ', 5, ' ') ||
                            LPAD('0', 40, '0') ||
                            LPAD(' ', 16, ' ') ||
                            LPAD('0', 20, '0');

                  --aSalta := 'Y';
               Else

                  -- Chiave

                  aStringaChiave1:='2010 ';
                  aStringaChiave3:=LPAD((300000 + aRecEstrazioneCud.dip_cd_anag),6,'0');
                  aStringa:=aStringaChiave1 || aStringaChiave2 || aStringaChiave3;

                  -- Campi contabili

                  aStringa:=aStringa ||
                            LPAD((aRecEstrazioneCud.imponibile_si_detr * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.ritenute_irpef * 100), 10, '0');
                  If aRecEstrazioneCud.addizionale_regionale - aRecEstrazioneCud.add_reg_accantonato > 0 Then
                     aStringa:=aStringa ||Lpad(((aRecEstrazioneCud.addizionale_regionale -
                                                 aRecEstrazioneCud.add_reg_accantonato) * 100), 10, '0');
                  Else
                     aStringa:=aStringa ||Lpad('0',10,'0');
                  End If;

                  --SE L'ADDIZIONALE COMUNALE DOVUTA - QUELLA RATEIZZATA - ACCONTO TRATTENUTO < 0 PASSO ZERO
                  If  aRecEstrazioneCud.addizionale_comunale - aRecEstrazioneCud.add_com_accantonato - Nvl(aRecEstrazioneCud.im_acconto_add_com,0) > 0 Then
                     aStringa:=aStringa ||Lpad(((aRecEstrazioneCud.addizionale_comunale -
                                                 aRecEstrazioneCud.add_com_accantonato -
                                                 Nvl(aRecEstrazioneCud.im_acconto_add_com,0)) * 100), 10, '0');
                  Else
                     aStringa:=aStringa ||Lpad('0',10,'0');
                  End If;

                  aStringa:=aStringa ||
                            LPAD(((aRecEstrazioneCud.imponibile_si_detr_tassep +
                                   aRecEstrazioneCud.imponibile_no_detr_tassep) * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.ritenute_irpef_tassep * 100), 10, '0') ||
                            Lpad((aRecEstrazioneCud.detrazioni_lavoro_dip * 100), 10, '0') ||
                            Lpad((aRecEstrazioneCud.cnr_detrazione_coniuge * 100), 10, '0') || --LPAD((aRecEstrazioneCud.im_deduzione_family * 100), 10, '0') ||
                            Lpad((aRecEstrazioneCud.cnr_detrazione_figli * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.cnr_detrazioni_tassep * 100), 10, '0') ||
                            LPAD(aRecEstrazioneCud.gg_periodo_lavoro, 3, '0') ||
                            LPAD(aRecEstrazioneCud.cnr_gg_periodo_tassep, 8, '0') ||
                            LPAD((aRecEstrazioneCud.add_reg_accantonato * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.add_com_accantonato * 100), 10, '0') ||
                            LPAD('0', 4, '0') ||
                            LPAD('0', 4, '0') ||
                            LPAD((aRecEstrazioneCud.add_reg_anno_precedente * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.add_com_anno_precedente * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.compensi_corrisposti_cococo * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.contributi_dovuti_cococo * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.contributi_trattenuti_cococo * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.contributi_versati_cococo * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.imposta_lorda * 100), 10, '0') ||
                            RPAD(NVL(aRecEstrazioneCud.cd_categoria,' '), 5, ' ') || --CATEGORIE PARTICOLARI
                            LPAD((aRecEstrazioneCud.imponibile_no_detr * 100), 10, '0');

                  IF aRecEstrazioneCud.cnr_tipologia_reddito_punto_1 IS NULL THEN
                     aStringa:=aStringa || RPAD(' ', 3, ' ');
                  ELSE
                     aStringa:=aStringa || RPAD(SUBSTR(aRecEstrazioneCud.cnr_tipologia_reddito_punto_1, 1, 3), 3, ' ');
                  END IF;

                  aStringa:=aStringa || aRecEstrazioneCud.cnr_tempo_determinato;

                  IF aRecEstrazioneCud.cnr_tipologia_reddito_punto_2 IS NULL THEN
                     aStringa:=aStringa || RPAD(' ', 3, ' ');
                  ELSE
                     aStringa:=aStringa || RPAD(SUBSTR(aRecEstrazioneCud.cnr_tipologia_reddito_punto_2, 1, 3), 3, ' ');
                  END IF;

                  IF aRecEstrazioneCud.fl_applic_aliquota_max = 'N' THEN
                     aStringa:=aStringa || '0';
                  ELSE
                     aStringa:=aStringa || '1';
                  END IF;

                /*
                  IF (aRecEstrazioneCud.cnr_deduzione_fissa_3000 + aRecEstrazioneCud.cnr_deduzione_fissa_4500) > 0 THEN
                     aStringa:=aStringa || '0';
                  ELSE
                     aStringa:=aStringa || '1';
                  END IF;

                  aStringa:=aStringa ||
                            LPAD((aRecEstrazioneCud.cnr_deduzione_fissa_3000 * 100), 10, '0') ||
                            LPAD((aRecEstrazioneCud.cnr_deduzione_fissa_4500 * 100), 10, '0') ||
                            ' ';
                */
                aStringa:=aStringa ||
                          LPAD('0', 1, '0') ||
                            LPAD('0', 10, '0') ||
                            LPAD('0', 10, '0');

                  aStringa:=aStringa ||
                            Lpad(' ', 1, ' ');

                  IF aRecEstrazioneCud.cnr_posizione_inail IS NULL THEN
                     aStringa:=aStringa || LPAD('0', 10, '0');
                  ELSE
                     IF IBMUTL001.isNumeric(aRecEstrazioneCud.cnr_posizione_inail) = 'Y' THEN
                        aStringa:=aStringa || LPAD(SUBSTR(aRecEstrazioneCud.cnr_posizione_inail,1,10), 10, '0');
                     ELSE
                        aStringa:=aStringa || LPAD('0', 10, '0');
                     END IF;
                  END IF;

                  aStringa:=aStringa ||
                            LPAD('0', 4, '0') ||
                            LPAD('0', 4, '0') ||
                            LPAD(' ', 4, ' ');

                  /*
                  IF aRecEstrazioneCud.dt_inizio_cococo Is Not Null THEN
                     aStringa:=aStringa || '3';
                  ELSE
                     aStringa:=aStringa || ' ';
                  END IF;
                  */
                  aStringa:=aStringa || ' ';

                  aStringa:=aStringa ||
                                 Lpad('0',10,'0') ||
                                 Lpad('0',10,'0') ||
                                 Lpad('0',10,'0') ||
                                 Lpad('0',10,'0') ||
                                 Lpad(' ',16,' ') ||
                                 Lpad('0',10,'0');

                  aStringa:=aStringa ||Lpad((aRecEstrazioneCud.im_acconto_add_com_es_suc * 100), 10, '0');
                  aStringa:=aStringa ||LPAD(aRecEstrazioneCud.gg_periodo_lavoro_sem1, 3, '0') ||
                                       LPAD(aRecEstrazioneCud.gg_periodo_lavoro_sem2, 3, '0');

                  IF LENGTH(aStringa) != 380 Then
                     IBMERR001.RAISE_ERR_GENERICO
                        ('Errore in lunghezza file in output tipo record 2010 lunghezza ' || LENGTH(aStringa));
                  END IF;
               End If;
          Else         -- i = 3

               -- Chiave

               aStringaChiave1:='2020 ';
               aStringaChiave3:=LPAD((300000 + aRecEstrazioneCud.dip_cd_anag),6,'0');
               aStringa:=aStringaChiave1 || aStringaChiave2 || aStringaChiave3;

             -- Campi contabili

             -- se il terzo è SOLO non residente (per come abbiamo inteso noi i non residenti)i primi cinque campi sono a zero
             If aRecEstrazioneCud.fl_soggetto_estero = 'Y' And
                  Nvl(aRecEstrazioneCud.imponibile_si_detr,0) = 0 And
                  Nvl(aRecEstrazioneCud.imponibile_no_detr,0) = 0 Then

                  aStringa:=aStringa || LPAD('0', 10, '0')||
                                        LPAD('0', 10, '0')||
                                        LPAD('0', 10, '0')||
                                        LPAD('0', 10, '0')||
                                        LPAD('0', 10, '0');
         Else
                  --IF aggiunto per sanare casi sporchi
              --Esistono diversi conguagli non contabilizzati che quindi non vengono inseriti nel cud (il che è giusto)
              --però se per essi esiste l'acconto, poichè aRecEstrazioneCud.addizionale_comunale è <0, invece dell'acconto
              --viene passato zero
                  --If aRecEstrazioneCud.dip_cd_anag In(20210,60281,64656,66401,79333,80356,91528,91530,91532) Then
                  --     aStringa:=aStringa ||
                  --             Lpad((Nvl(aRecEstrazioneCud.im_acconto_add_com,0) * 100), 10, '0');
                  --Else
                       --SE DOVUTO - ACCONTO >= 0 PASSO L'ACCONTO COSì COME CALCOLATO ALTRIMENTI LO FORZO UGUALE AL SALDO
                       If  aRecEstrazioneCud.addizionale_comunale - Nvl(aRecEstrazioneCud.im_acconto_add_com,0) < 0 Then
                           If aRecEstrazioneCud.addizionale_comunale > 0 Then
                                 aStringa:=aStringa ||
                                           Lpad((aRecEstrazioneCud.addizionale_comunale * 100), 10, '0');
                           Else
                                 aStringa:=aStringa || Lpad('0', 10, '0');
                           End If;
                       Else
                           aStringa:=aStringa ||
                            Lpad((Nvl(aRecEstrazioneCud.im_acconto_add_com,0) * 100), 10, '0');
                       End If;
                  --End If;
                  aStringa:=aStringa ||
                            LPAD('0', 10, '0') ||          -- ULTERIORE DETRAZIONE 1380
                            Lpad((Nvl(aRecEstrazioneCud.im_esenz_add_com_es,0) * 100), 10, '0')||
                            Lpad((Nvl(aRecEstrazioneCud.im_esenz_add_com_es_suc,0) * 100), 10, '0')||
                            Lpad((Nvl(aRecEstrazioneCud.reddito_abitaz_princ,0) * 100), 10, '0');
         End If;

         -- se il terzo è residente i successivi due campi sono a zero
         aStringa:=aStringa ||
                   Lpad((Nvl(aRecEstrazioneCud.imponibile_non_residenti,0) * 100), 10, '0')||
                   Lpad((Nvl(aRecEstrazioneCud.ritenute_non_residenti,0) * 100), 10, '0');

         aStringa:=aStringa || LPAD('0', 10, '0')||
                               LPAD('0', 10, '0');

         aStringa:=aStringa ||Lpad((Nvl(aRecEstrazioneCud.altri_redditi,0) * 100), 10, '0');

         aStringa:=aStringa || LPAD('0', 10, '0');                                                                                                                                  -- ADD. COM. ACCONTO ESERCIZIO SOSPESA
         aStringa:=aStringa ||Lpad((Nvl(aRecEstrazioneCud.im_irpef_sospeso,0) * 100), 10, '0');       -- RITENUTA IRPEF SOSPESA
         aStringa:=aStringa || LPAD('0', 10, '0')||
                               LPAD('0', 10, '0')||
                               LPAD('0', 10, '0')||
                               LPAD('0', 10, '0')||
                               LPAD('0', 10, '0')||
                               LPAD('0', 10, '0')||
                               LPAD('0', 10, '0')||
                               LPAD('0', 10, '0');
         IF Nvl(aRecEstrazioneCud.im_irpef_sospeso,0) != 0 THEN
                aStringa:=aStringa ||'3';
         ELSE
                aStringa:=aStringa ||'0';
         END IF;

         aStringa:=aStringa ||Lpad((Nvl(aRecEstrazioneCud.imponibile_non_tassato_cerv,0) * 100), 10, '0');

         IF LTRIM(RTRIM(aRecEstrazioneCud.annotazioni_2)) IS NOT NULL THEN
             aStringa:=aStringa ||'Y';
         ELSE
             aStringa:=aStringa ||'N';
         END IF;

         aStringa:=aStringa ||Lpad((Nvl(aRecEstrazioneCud.im_redd_esenti_per_legge,0) * 100), 10, '0');     --  REDDITI ESENTI PER LEGGE (ASSEGNI DI RICERCA)
         aStringa:=aStringa ||Lpad((Nvl(aRecEstrazioneCud.im_redd_non_tassati_per_conv,0) * 100), 10, '0');       --  REDDITI NON TASSATI PER CONVENZIONE
         aStringa:=aStringa ||Lpad((Nvl(aRecEstrazioneCud.imponi_non_tassato_rientro_lav,0) * 100), 10, '0');  -- IMPONIBILE NON TASSATO RIENTRO DEI LAVORATORI

         IF aRecEstrazioneCud.cnr_tipologia_reddito_punto_2 IS NULL THEN
              aStringa:=aStringa || RPAD(' ', 3, ' ');
         ELSE
              aStringa:=aStringa || RPAD(SUBSTR(aRecEstrazioneCud.cnr_tipologia_reddito_punto_2, 1, 3), 3, ' ');
         END IF;

         aStringa:=aStringa ||Lpad((Nvl(aRecEstrazioneCud.im_bonus_erogato,0) * 100), 10, '0');       --  IMPORTO BONUS EROGATO
         aStringa:=aStringa ||Lpad((Nvl(aRecEstrazioneCud.ult_detr_cuneo_fisc,0) * 100), 10, '0');    --  ULTERIORE DETRAZIONE CUNEO FISCALE...SOLO LA PARTE DI DETRAZIONE
         aStringa:=aStringa ||Lpad((Nvl(aRecEstrazioneCud.tratt_int_erog_cuneo_fisc,0) * 100), 10, '0');    --  TRATTAMENTO INTEGRATIVO EROGATO CUNEO FISCALE...SOLO LA PARTE DI TRATTAMENTO INTEGRATIVO(NON DETRAZIONE) EROGATA, INCLUSI I CONGUAGLI
         aStringa:=aStringa ||Lpad((Nvl(aRecEstrazioneCud.tratt_int_cong_cuneo_fisc,0) * 100), 10, '0');    --  TRATTAMENTO INTEGRATIVO RECUPERATO ENTRO CONGUAGLIO CUNEO FISCALE...SOLO LA PARTE DI CONGUAGLIO TRATTAMENTO INTEGRATIVO(NON DETRAZIONE)

         IF LENGTH(aStringa) != 304 Then
            IBMERR001.RAISE_ERR_GENERICO
               ('Errore in lunghezza file in output tipo record 2020 lunghezza ' || LENGTH(aStringa));
         END IF;

      END IF;   -- i=....

      -- Scrittura CLOB
          If aSalta = 'N' Then
                aDifferenza:= 500 - LENGTH(aStringa);

                aStringa:=aStringa || LPAD(' ', aDifferenza, ' ');

                IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
                                aRecBframeBlob.path,
                                    aRecBframeBlob.filename,
                                    mioCLOB,
                                    aStringa);
            End If;

         END LOOP;

      END LOOP;

      CLOSE gen_cur;

   END;

   -------------------------------------------------------------------------------------------------
   -- Chiusura CLOB

   IBMUTL005.ShCloseClob(aRecBframeBlob.cd_tipo,
                   aRecBframeBlob.path,
                         aRecBframeBlob.filename,
                   mioCLOB);

END scriviFileOutput;

-- =================================================================================================
-- Inserimento CARICHI FAMILIARI
-- =================================================================================================
PROCEDURE inserisciCarichiFamiliari
   (
    inEsercizio NUMBER,
    inRepID INTEGER
   ) IS

   NUMERO_MESI    NUMBER(2):=0;
   DT_INIZIO      DATE;
   DT_FINE        DATE;
   MESE_INIZIO    NUMBER(2);
   MESE_FINE    NUMBER(2);
   TIPO         VARCHAR2(7);
   FL_MINORE_3_ANNI  VARCHAR2(1);
BEGIN
   for rec in (select esercizio,ID_ESTRAZIONE, dip_cd_anag,
                          DIP_CODICE_FISCALE,DIP_COGNOME,DIP_NOME,
                      nvl(DETRAZIONI_FAMILIARI,0) DETRAZIONI_FAMILIARI,
                      nvl(CNR_DETRAZIONE_CONIUGE,0) CNR_DETRAZIONE_CONIUGE,
                      nvl(CNR_DETRAZIONE_FIGLI,0) CNR_DETRAZIONE_FIGLI
               from ESTRAZIONE_CUD
               where esercizio = inEsercizio
               and id_estrazione = inRepID
               and (nvl(DETRAZIONI_FAMILIARI,0) !=0 or
                    nvl(CNR_DETRAZIONE_CONIUGE,0) !=0 or
                    nvl(CNR_DETRAZIONE_FIGLI,0) !=0))   loop

        IF REC.CNR_DETRAZIONE_CONIUGE != 0 THEN
           for con in (select CODICE_FISCALE,TI_PERSONA,PRC_CARICO,DT_INI_VALIDITA,DT_FIN_VALIDITA
                       from CARICO_FAMILIARE_ANAG
                       where CD_ANAG = REC.DIP_CD_ANAG
                         AND TI_PERSONA = 'C'
                         AND TO_CHAR(DT_INI_VALIDITA,'YYYY') <= inEsercizio
                         AND TO_CHAR(DT_FIN_VALIDITA,'YYYY') >= inEsercizio
                         AND ROWNUM < 2) LOOP

               IF TO_CHAR(CON.DT_INI_VALIDITA,'YYYY') < inEsercizio THEN
                    DT_INIZIO := TO_DATE('01/01/'||inEsercizio,'DD/MM/YYYY');
                    MESE_INIZIO := 1;
               ELSE
                    DT_INIZIO := CON.DT_INI_VALIDITA;
                    MESE_INIZIO := TO_NUMBER(TO_CHAR(CON.DT_INI_VALIDITA,'MM'));
               END IF;

               IF TO_CHAR(CON.DT_FIN_VALIDITA,'YYYY') > inEsercizio THEN
                    DT_FINE := TO_DATE('31/12/'||inEsercizio,'DD/MM/YYYY');
                    MESE_FINE := 12;
               ELSE
                    DT_FINE := CON.DT_FIN_VALIDITA;
                    MESE_FINE := TO_NUMBER(TO_CHAR(CON.DT_FIN_VALIDITA,'MM'));
               END IF;

               NUMERO_MESI := MESE_FINE - MESE_INIZIO + 1;

               INSERT INTO ESTRAZIONE_CUD_CARICHI_FAM (ID_ESTRAZIONE,ESERCIZIO,DIP_CD_ANAG,DIP_CODICE_FISCALE,DIP_COGNOME,DIP_NOME,DT_NASCITA_FIGLIO,
                                                     TIPOLOGIA_CARICO,CODICE_FISCALE_CARICO,NOMINATIVO_CARICO,ANNO_DETRAZIONE,PRC_CARICO,
                                                 NUMERO_MESI_CARICO,FL_MINORE_3_ANNI)
                  VALUES (REC.ID_ESTRAZIONE,REC.ESERCIZIO,LPAD((300000 + rec.dip_cd_anag),6,'0'),REC.DIP_CODICE_FISCALE,REC.DIP_COGNOME,REC.DIP_NOME,NULL,
                          'CONIUGE',CON.CODICE_FISCALE,NULL,REC.ESERCIZIO,CON.PRC_CARICO,
                        NUMERO_MESI,Null);

           END LOOP;
        end if;
        If  REC.CNR_DETRAZIONE_FIGLI != 0 Then
           for fig in (select CODICE_FISCALE,TI_PERSONA,PRC_CARICO,DT_INI_VALIDITA,DT_FIN_VALIDITA,
                              FL_HANDICAP, DT_FINE_FIGLIO_HA_TREANNI,DT_NASCITA_FIGLIO
                       from CARICO_FAMILIARE_ANAG
                       where CD_ANAG = REC.DIP_CD_ANAG
                         AND TI_PERSONA In ('F','A')
                         AND TO_CHAR(DT_INI_VALIDITA,'YYYY') <= inEsercizio
                         AND TO_CHAR(DT_FIN_VALIDITA,'YYYY') >= inEsercizio) LOOP

               IF TO_CHAR(FIG.DT_INI_VALIDITA,'YYYY') < inEsercizio THEN
                    DT_INIZIO := TO_DATE('01/01/'||inEsercizio,'DD/MM/YYYY');
                    MESE_INIZIO := 1;
               ELSE
                    DT_INIZIO := FIG.DT_INI_VALIDITA;
                    MESE_INIZIO := TO_NUMBER(TO_CHAR(FIG.DT_INI_VALIDITA,'MM'));
               END IF;

               IF TO_CHAR(FIG.DT_FIN_VALIDITA,'YYYY') > inEsercizio THEN
                    DT_FINE := TO_DATE('31/12/'||inEsercizio,'DD/MM/YYYY');
                    MESE_FINE := 12;
               ELSE
                    DT_FINE := FIG.DT_FIN_VALIDITA;
                    MESE_FINE := TO_NUMBER(TO_CHAR(FIG.DT_FIN_VALIDITA,'MM'));
               END IF;

               NUMERO_MESI := MESE_FINE - MESE_INIZIO + 1;

               If FIG.TI_PERSONA = 'F' Then
                  If FIG.FL_HANDICAP = 'N' Then
                      TIPO := 'FIGLI';
                  Else
                      TIPO := 'FGINVA';
                  End If;
                  If To_Char(FIG.DT_FINE_FIGLIO_HA_TREANNI,'YYYY') < inEsercizio Then
                     FL_MINORE_3_ANNI := 'N';
                  Else
                     FL_MINORE_3_ANNI := 'S';
                  End If;
               Else
                      TIPO := 'ALTROFA';
                      FL_MINORE_3_ANNI:= Null;
               End If;

               INSERT INTO ESTRAZIONE_CUD_CARICHI_FAM (ID_ESTRAZIONE,ESERCIZIO,DIP_CD_ANAG,DIP_CODICE_FISCALE,DIP_COGNOME,DIP_NOME,DT_NASCITA_FIGLIO,
                                    TIPOLOGIA_CARICO,CODICE_FISCALE_CARICO,NOMINATIVO_CARICO,ANNO_DETRAZIONE,PRC_CARICO,
                                    NUMERO_MESI_CARICO,FL_MINORE_3_ANNI)
                  VALUES (REC.ID_ESTRAZIONE,REC.ESERCIZIO,LPAD((300000 + rec.dip_cd_anag),6,'0'),REC.DIP_CODICE_FISCALE,REC.DIP_COGNOME,REC.DIP_NOME,FIG.DT_NASCITA_FIGLIO,
                          TIPO,FIG.CODICE_FISCALE,NULL,REC.ESERCIZIO,FIG.PRC_CARICO,
                        NUMERO_MESI,FL_MINORE_3_ANNI);

           END LOOP;
        End If;

   end loop;
END inserisciCarichiFamiliari;

-- =================================================================================================
-- Estrazione dati per stampa CUD
-- =================================================================================================
PROCEDURE estrazioneDatiCUD
   (
    inEsercizio NUMBER,
    aCdAnag NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2,
    pg_exec NUMBER
   ) IS

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazione variabili

   --dataOdierna:=sysdate;

   -------------------------------------------------------------------------------------------------
   -- Inserimento in ESTRAZIONE_CUD_DETT dei compensi oggetto di esposizione sul CUD. Caricamento
   -- dei dati relativi alla testata del compenso

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 1',
                    'FASE 1 - Inserimento compensi in ESTRAZIONE_CUD_DETT');

   inserisciDatiCUDDett(inEsercizio,
                        aCdAnag,
                        inRepID,
                        inUtente);

   -------------------------------------------------------------------------------------------------
   -- Inserimento in ESTRAZIONE_CUD_DETT dei compensi oggetto di esposizione sul CUD. Caricamento
   -- dei dati relativi alla testata del compenso

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 2',
                    'FASE 2 - Aggiornamento dati contributo ritenuta su compensi presenti ' ||
                    'in ESTRAZIONE_CUD_DETT');

   upgDatiCUDDettCori(inEsercizio,
                      inRepID);

   COMMIT;

   -------------------------------------------------------------------------------------------------
   -- Inserimento in ESTRAZIONE_CUD_DETT dei compensi oggetto di esposizione sul CUD. Caricamento
   -- dei dati relativi alla testata del compenso

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 3',
                    'FASE 3 - Aggiornamento dati conguaglio su compensi presenti in ESTRAZIONE_CUD_DETT');

   upgDatiCUDDettCong(inEsercizio,
                      inRepID);


   -------------------------------------------------------------------------------------------------
   -- Aggregazione dati di ESTRAZIONE_CUD_DETT in ESTRAZIONE_CUD.

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 4',
                    'FASE 4 - Aggiornamento dati in tabella ESTRAZIONE_CUD (imponibile)');

   aggregaDatiCud(inEsercizio,
                  inRepID,
                  inUtente);

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento dati anagrafici.

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 5',
                    'FASE 5 - Aggiornamento dati anagrafici');

   insDatiAnagCud(inEsercizio,
                  inRepID);

   -------------------------------------------------------------------------------------------------
   -- Calcolo del numero di giorni di riferimento

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 6',
                    'FASE 6 - Calcolo giorni lavoro');

   calcolaGiorniLavoro(inEsercizio,
                       inRepID,
                       inUtente);

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento dati su annotazioni

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 7',
                    'FASE 7 - Inserimento dettaglio annotazioni');

   insDatiAnnotazioniCud(inEsercizio,
                         inRepID);

   -------------------------------------------------------------------------------------------------
   -- Determino la tipologia di reddito preponderante per CNR.

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 8',
                    'FASE 8 - Determino la tipologia di reddito principale');

   calcolaTipologiaReddito(inEsercizio,
                           inRepID);

   -------------------------------------------------------------------------------------------------
   -- Determino l'acconto per l'addizionale comunale

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 9',
                    'FASE 9 - Determino Acconto Addizionale Comunale');

   calcolaAccontoAddCom(inEsercizio,
                        inRepID);
   -------------------------------------------------------------------------------------------------
   -- Scrittura del file di output

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 10',
                    'FASE 10 - creazione CLOB (file output)');

   scriviFileOutput(inEsercizio,
                    inRepID,
                    inUtente);

-------------------------------------------------------------------------------------------------
   -- Inserimento carichi familiari in ESTRAZIONE_CUD_CARICHI_FAM

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 10',
                    'FASE 10 - Inserimento Carichi Familiari');

   inserisciCarichiFamiliari(inEsercizio,
                             inRepID);

-------------------------------------------------------------------------------------------------

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati CUD - Dettaglio elaborazione',
                    'FASE 11',
                    'FASE 11 - Fine caricamento');

END estrazioneDatiCUD;

-- =================================================================================================
-- Lancio estrazione CUD
-- =================================================================================================
PROCEDURE estrazioneCUDInterna
   (
    inEsercizio NUMBER,
    inCdAnag VARCHAR2,
    inRepID INTEGER,
    inMsgError IN OUT VARCHAR2,
    inUtente VARCHAR2,
    pg_exec NUMBER
   ) IS

   aCdAnag NUMBER(8);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Controllo congruenza parametri in input

   chkParametriInput(inEsercizio,
                     inCdAnag,
                     inRepID,
                     inUtente);

   -------------------------------------------------------------------------------------------------
   -- Valorizza parametri

   -- Normalizzazione del codice anagrafico

   aCdAnag:=NULL;
   IF inCdAnag != '%' THEN
      aCdAnag:=inCdAnag;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Lettura parametri di base alla procedura.

   valorizzaParametri(inEsercizio);

   -------------------------------------------------------------------------------------------------
   -- Estrazione dati per CUD.

   estrazioneDatiCUD(inEsercizio,
                     aCdAnag,
                     inRepID,
                     inUtente,
                     pg_exec);


END  estrazioneCUDInterna;

-- =================================================================================================
-- Guscio per gestione estrazione CUD in batch
-- =================================================================================================
PROCEDURE job_estrazioneCUD
   (
    job NUMBER,
    pg_exec NUMBER,
    next_date DATE,
    inEsercizio NUMBER,
    inCdAnag VARCHAR2,
    inRepID INTEGER,
    inMsgError VARCHAR2,
    inUtente VARCHAR2
   ) IS
   aStringa VARCHAR2(2000);
   i BINARY_INTEGER;

BEGIN

   -- Lancio start esecuzione log

   aStringa:=inMsgError;

   IBMUTL210.logStartExecutionUpd(pg_exec, IDTIPOLOG, job, 'Richista utente:' || inUtente,
                                  'Estrazione dati CUD. Start:' || TO_CHAR(sysdate,'YYYY/MM/DD HH-MI-SS'));

   BEGIN

      estrazioneCUDInterna(inEsercizio,
                           inCdAnag,
                           inRepID,
                           aStringa,
                           inUtente,
                           pg_exec);

      COMMIT;

      -- Messaggio di operazione completata ad utente

      IBMUTL205.LOGINF('Estrazione dati CUD',
                       'Estrazione dati CUD ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS'),
                       'Operazione completata con successo',
                       inUtente);

   EXCEPTION

      WHEN others THEN
           ROLLBACK;

      -- Messaggio di attenzione ad utente

      IBMUTL205.LOGWAR('Estrazione dati CUD ' || errori_tab.COUNT,
                       'Estrazione dati CUD ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                       '(pg_exec = ' || pg_exec || ')', DBMS_UTILITY.FORMAT_ERROR_STACK, inUtente);

      -- Scrittura degli eventuali altri errori

      IF errori_tab.COUNT > 0 THEN

         FOR i IN errori_tab.FIRST .. errori_tab.LAST

         LOOP

            IBMUTL200.LOGWAR(pg_exec,
                             'Estrazione dati CUD - Dettaglio errori',
                             'Errore : ' || errori_tab(i).tStringaErr,
                             'Identificativo = ' || errori_tab(i).tStringaKey);

         END LOOP;

      END IF;

   END;

END job_estrazioneCUD;

-- =================================================================================================
-- Main estrazione CUD Standard
-- =================================================================================================
PROCEDURE estrazioneCUD
   (
    inEsercizio NUMBER,
    inCdAnag VARCHAR2,
    inRepID INTEGER,
    inMsgError IN OUT VARCHAR2,
    inUtente VARCHAR2
   ) IS

   aProcedure VARCHAR2(2000);
   aStringa VARCHAR2(2000);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Valorizza parametri

   aStringa:=inMsgError;

   -------------------------------------------------------------------------------------------------
   -- Attivazione della gestione batch per estrazione CUD

   aProcedure:='CNRCTB900.job_estrazioneCud(job, ' ||
                                            'pg_exec, ' ||
                                            'next_date, ' ||
                                            inEsercizio || ',''' ||
                                            inCdAnag || ''',' ||
                                            inRepID || ',''' ||
                                            aStringa || ''',' ||
                                            '''' || inUtente || ''');';

   IBMUTL210.CREABATCHDINAMICO('Estrazione dati CUD',
                               aProcedure,
                               inUtente);

   IBMUTL001.deferred_commit;

   IBMERR001.RAISE_ERR_GENERICO
      ('Operazione sottomessa per esecuzione. Al completamento l''utente riceverà un messaggio di notifica ' ||
       'dello stato dell''operazione');


END estrazioneCUD;

-- =================================================================================================

END; -- PACKAGE END;
/
