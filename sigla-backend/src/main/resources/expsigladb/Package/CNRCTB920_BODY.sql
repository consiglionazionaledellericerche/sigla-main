--------------------------------------------------------
--  DDL for Package Body CNRCTB920
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB920" AS

-- =================================================================================================
-- Controllo congruenza parametri in input
-- =================================================================================================
PROCEDURE chkParametriInput
   (
    inEsercizio NUMBER,
    inCdCds VARCHAR2,
    inCdUo VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   numero INTEGER;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Verifica congruenza parametri.
   --   aEsercizio, inCdCds, inCdUo, repID e aUtente non possono essere nulli

   IF (inEsercizio IS NULL OR
       inCdCds IS NULL OR
       inCdUo IS NULL OR
       inRepID IS NULL OR
       inUtente IS NULL) THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Valorizzazione parametri in input errata. ' ||
          'Un identificativo (esercizio, codice cds, codice uo, identificativo report, id utente) risulta non valorizzato');
   END IF;

END chkParametriInput;

-- =================================================================================================
-- Memorizzazione parametri di uso della procedura di stampa
-- =================================================================================================
PROCEDURE valorizzaParametri
   (
    inEsercizio NUMBER,
    inCdCds VARCHAR2,
    inCdUo VARCHAR2
   ) IS

   i BINARY_INTEGER;

   aRecUnitaOrganizzativa UNITA_ORGANIZZATIVA%ROWTYPE;

BEGIN

   ------------------------------------------------------------------------------------------------
   -- Azzeramento tabella parametri

   parametri_tab.DELETE;

   ------------------------------------------------------------------------------------------------
   -- Valorizzazione parametri

   FOR i IN 1 .. 2

   LOOP

      BEGIN

         -- Memorizzazione della descrizione dell'unità organizzativa

         IF    i = 1 THEN

               aRecUnitaOrganizzativa:=CNRCTB020.getUOValida(inEsercizio,
                                                             inCdUo);

               IF aRecUnitaOrganizzativa.cd_tipo_unita = CNRCTB020.TIPO_SAC THEN
                  parametri_tab(i).intero:=0;
                  aRecUnitaOrganizzativa:=CNRCTB020.getCDSValido(inEsercizio,
                                                                 inCdCdS);
               ELSE
                  parametri_tab(i).intero:=1;
               END IF;

               parametri_tab(i).stringa:=aRecUnitaOrganizzativa.ds_unita_organizzativa;

        -- Memorizzazione STATO_CONTABILE_LIQUIDAZIONE_CORI dell'Ateneo

         ELSIF i = 2 THEN

               parametri_tab(i).stringa:=CNRCTB015.getVal01PerChiave(inEsercizio,
                                                                     'GESTIONE_CORI_SPECIALE',
                                                                     'STATO_LIQUIDA_CORI');

         END IF;

      END;

   END LOOP;

END valorizzaParametri;

-- =================================================================================================
-- Inserimento record in ESTRAZIONE_INPS_DETT)
-- =================================================================================================
PROCEDURE insEstrazioneInpsDett
   (
    aRecEstrazioneInpsDett ESTRAZIONE_INPS_DETT%ROWTYPE
   ) IS

BEGIN

   INSERT INTO ESTRAZIONE_INPS_DETT
          (id_estrazione,
           esercizio,
           cd_cds_compenso,
           cd_uo_compenso,
           esercizio_compenso,
           pg_compenso,
           esercizio_pagamento,
           fl_senza_calcoli,
           stato_cofi,
           dt_registrazione_compenso,
           dt_cmp_da_compenso,
           dt_cmp_a_compenso,
           dt_emissione_mandato,
           dt_trasmissione_mandato,
           dt_pagamento_mandato,
           ti_anagrafico,
           cd_anag,
           cd_terzo,
           cd_trattamento,
           cd_contributo_ritenuta,
           ds_contributo_ritenuta,
           pg_riga,
           imponibile_cori,
           imponibile_cori_dett,
           aliquota_cori_dett,
           ammontare_cori_dett,
           cd_cds_versamento,
           esercizio_versamento,
           cd_uo_versamento,
           pg_liquidazione_versamento,
           cd_gruppo_cr_versamento,
           im_liquidato_versamento,
           dt_emissione_versamento,
           dt_trasmissione_versamento,
           dt_pagamento_versamento,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec)
   VALUES (aRecEstrazioneInpsDett.id_estrazione,
           aRecEstrazioneInpsDett.esercizio,
           aRecEstrazioneInpsDett.cd_cds_compenso,
           aRecEstrazioneInpsDett.cd_uo_compenso,
           aRecEstrazioneInpsDett.esercizio_compenso,
           aRecEstrazioneInpsDett.pg_compenso,
           aRecEstrazioneInpsDett.esercizio_pagamento,
           aRecEstrazioneInpsDett.fl_senza_calcoli,
           aRecEstrazioneInpsDett.stato_cofi,
           aRecEstrazioneInpsDett.dt_registrazione_compenso,
           aRecEstrazioneInpsDett.dt_cmp_da_compenso,
           aRecEstrazioneInpsDett.dt_cmp_a_compenso,
           aRecEstrazioneInpsDett.dt_emissione_mandato,
           aRecEstrazioneInpsDett.dt_trasmissione_mandato,
           aRecEstrazioneInpsDett.dt_pagamento_mandato,
           aRecEstrazioneInpsDett.ti_anagrafico,
           aRecEstrazioneInpsDett.cd_anag,
           aRecEstrazioneInpsDett.cd_terzo,
           aRecEstrazioneInpsDett.cd_trattamento,
           aRecEstrazioneInpsDett.cd_contributo_ritenuta,
           aRecEstrazioneInpsDett.ds_contributo_ritenuta,
           aRecEstrazioneInpsDett.pg_riga,
           aRecEstrazioneInpsDett.imponibile_cori,
           aRecEstrazioneInpsDett.imponibile_cori_dett,
           aRecEstrazioneInpsDett.aliquota_cori_dett,
           aRecEstrazioneInpsDett.ammontare_cori_dett,
           aRecEstrazioneInpsDett.cd_cds_versamento,
           aRecEstrazioneInpsDett.esercizio_versamento,
           aRecEstrazioneInpsDett.cd_uo_versamento,
           aRecEstrazioneInpsDett.pg_liquidazione_versamento,
           aRecEstrazioneInpsDett.cd_gruppo_cr_versamento,
           aRecEstrazioneInpsDett.im_liquidato_versamento,
           aRecEstrazioneInpsDett.dt_emissione_versamento,
           aRecEstrazioneInpsDett.dt_trasmissione_versamento,
           aRecEstrazioneInpsDett.dt_pagamento_versamento,
           aRecEstrazioneInpsDett.dacr,
           aRecEstrazioneInpsDett.utcr,
           aRecEstrazioneInpsDett.duva,
           aRecEstrazioneInpsDett.utuv,
           aRecEstrazioneInpsDett.pg_ver_rec);

END insEstrazioneInpsDett;

-- =================================================================================================
-- Estrazione dati per stampa CUD (Inserimento record in ESTRAZIONE_INPS_DETT)
-- =================================================================================================
PROCEDURE inserisciDatiINPSDett
   (
    inEsercizio NUMBER,
    inCdCds VARCHAR2,
    inCdUo VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   memChiaveCompenso VARCHAR2(500);
   memPgRiga NUMBER(10);
   memTiCoriEPPrec CHAR(1);

   aRecVEstraiDatiInps V_ESTRAI_DATI_INPS%ROWTYPE;
   aRecEstrazioneInpsDett ESTRAZIONE_INPS_DETT%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializza variabili

   memChiaveCompenso:=NULL;
   memPgRiga:=NULL;
   memTiCoriEPPrec:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale di lettura record per inserimento in ESTRAZIONE_INPS_DETT distinto per CDS SAC
   -- o altro

   BEGIN

      IF parametri_tab(2).stringa = CNRCTB575.STATO_LIQUID_CORI_INV THEN

         IF parametri_tab(1).intero = 0 THEN

            OPEN gen_cur FOR

                 SELECT *
                 FROM   V_ESTRAI_DATI_INPS
                 WHERE  cd_cds_compenso = inCdCds AND
                        esercizio_pagamento = inEsercizio AND
                        dt_trasmissione_mandato IS NOT NULL
                 ORDER BY cd_cds_compenso,
                          cd_uo_compenso,
                          esercizio_compenso,
                          pg_compenso,
		          pg_riga,
		          ti_ente_percipiente
                 FOR UPDATE OF pg_compenso NOWAIT;

         ELSE

            OPEN gen_cur FOR

                 SELECT *
                 FROM   V_ESTRAI_DATI_INPS
                 WHERE  cd_cds_compenso = inCdCds AND
                        cd_uo_compenso = inCdUo AND
                        esercizio_pagamento = inEsercizio AND
                        dt_trasmissione_mandato IS NOT NULL
                 ORDER BY cd_cds_compenso,
                          cd_uo_compenso,
                          esercizio_compenso,
                          pg_compenso,
		          pg_riga,
		          ti_ente_percipiente
                 FOR UPDATE OF pg_compenso NOWAIT;

         END IF;

      ELSE

         IF parametri_tab(1).intero = 0 THEN

            OPEN gen_cur FOR

                 SELECT *
                 FROM   V_ESTRAI_DATI_INPS
                 WHERE  cd_cds_compenso = inCdCds AND
                        esercizio_pagamento = inEsercizio
                 ORDER BY cd_cds_compenso,
                          cd_uo_compenso,
                          esercizio_compenso,
                          pg_compenso,
		          pg_riga,
		          ti_ente_percipiente
                 FOR UPDATE OF pg_compenso NOWAIT;

         ELSE

            OPEN gen_cur FOR

                 SELECT *
                 FROM   V_ESTRAI_DATI_INPS
                 WHERE  cd_cds_compenso = inCdCds AND
                        cd_uo_compenso = inCdUo AND
                        esercizio_pagamento = inEsercizio
                 ORDER BY cd_cds_compenso,
                          cd_uo_compenso,
                          esercizio_compenso,
                          pg_compenso,
		          pg_riga,
		          ti_ente_percipiente
                 FOR UPDATE OF pg_compenso NOWAIT;

         END IF;

      END IF;

      LOOP

         FETCH gen_cur INTO
               aRecVEstraiDatiInps;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Prima volta

         IF  memChiaveCompenso IS NULL THEN
             aRecEstrazioneInpsDett:=NULL;
             memChiaveCompenso:=aRecVEstraiDatiInps.chiave_compenso;
             memPgRiga:=aRecVEstraiDatiInps.pg_riga;
             memTiCoriEPPrec:=TICORIPERCIP;
         END IF;

         -- Se il compenso letto è diverso dal precedente o se il progressivo di riga è diverso dal
         -- precedente allora scrivo il compenso in ESTRAZIONE_INPS_DETT

         IF (memChiaveCompenso != aRecVEstraiDatiInps.chiave_compenso OR
             memPgRiga != aRecVEstraiDatiInps.pg_riga) THEN
            insEstrazioneInpsDett(aRecEstrazioneInpsDett);
            memChiaveCompenso:=aRecVEstraiDatiInps.chiave_compenso;
            memPgRiga:=aRecVEstraiDatiInps.pg_riga;
         END IF;

         -- Controllo sequenza record ENTE --> PERCIPIENTE

         IF aRecVEstraiDatiInps.ti_ente_percipiente = memTiCoriEPPrec THEN
            IBMERR001.RAISE_ERR_GENERICO
               ('Errore in sequenza record. Un CORI ente non è precedute da uno percipiente o viceversa ' || CHR(10) ||
                'Compenso UO ' || aRecVEstraiDatiInps.cd_uo_compenso ||
                ' esercizio ' || aRecVEstraiDatiInps.esercizio_compenso ||
                ' progressivo ' || aRecVEstraiDatiInps.pg_compenso);
         END IF;

         memTiCoriEPPrec:=aRecVEstraiDatiInps.ti_ente_percipiente;

         -- Costruzione del record

         IF aRecVEstraiDatiInps.ti_ente_percipiente = TICORIENTE THEN

            aRecEstrazioneInpsDett:=NULL;
            aRecEstrazioneInpsDett.id_estrazione:=inRepID;
            aRecEstrazioneInpsDett.esercizio:=inEsercizio;
            aRecEstrazioneInpsDett.cd_cds_compenso:=aRecVEstraiDatiInps.cd_cds_compenso;
            aRecEstrazioneInpsDett.cd_uo_compenso:=aRecVEstraiDatiInps.cd_uo_compenso;
            aRecEstrazioneInpsDett.esercizio_compenso:=aRecVEstraiDatiInps.esercizio_compenso;
            aRecEstrazioneInpsDett.pg_compenso:=aRecVEstraiDatiInps.pg_compenso;
            aRecEstrazioneInpsDett.esercizio_pagamento:=aRecVEstraiDatiInps.esercizio_pagamento;
            aRecEstrazioneInpsDett.fl_senza_calcoli:=aRecVEstraiDatiInps.fl_senza_calcoli;
            aRecEstrazioneInpsDett.stato_cofi:=aRecVEstraiDatiInps.stato_cofi;
            aRecEstrazioneInpsDett.dt_registrazione_compenso:=aRecVEstraiDatiInps.dt_registrazione_compenso;
            aRecEstrazioneInpsDett.dt_cmp_da_compenso:=TRUNC(aRecVEstraiDatiInps.dt_cmp_da_compenso);
            aRecEstrazioneInpsDett.dt_cmp_a_compenso:=TRUNC(aRecVEstraiDatiInps.dt_cmp_a_compenso);
            aRecEstrazioneInpsDett.dt_emissione_mandato:=TRUNC(aRecVEstraiDatiInps.dt_emissione_mandato);
            aRecEstrazioneInpsDett.dt_trasmissione_mandato:=TRUNC(aRecVEstraiDatiInps.dt_trasmissione_mandato);
            aRecEstrazioneInpsDett.dt_pagamento_mandato:=TRUNC(aRecVEstraiDatiInps.dt_pagamento_mandato);
            aRecEstrazioneInpsDett.ti_anagrafico:=aRecVEstraiDatiInps.ti_anagrafico;
            aRecEstrazioneInpsDett.cd_anag:=aRecVEstraiDatiInps.cd_anag;
            aRecEstrazioneInpsDett.cd_terzo:=aRecVEstraiDatiInps.cd_terzo;
            aRecEstrazioneInpsDett.cd_trattamento:=aRecVEstraiDatiInps.cd_trattamento;
            aRecEstrazioneInpsDett.cd_contributo_ritenuta:=aRecVEstraiDatiInps.cd_contributo_ritenuta;
            aRecEstrazioneInpsDett.ds_contributo_ritenuta:=aRecVEstraiDatiInps.ds_contributo_ritenuta;
            aRecEstrazioneInpsDett.pg_riga:=aRecVEstraiDatiInps.pg_riga;
            aRecEstrazioneInpsDett.imponibile_cori:=aRecVEstraiDatiInps.imponibile;
            --Per la Gestione degli OCCA ci sono tre casi:
            --1) Compensi OCCA normali (rapporto OCCA e fl_senza_calcoli = 'N')
            --2) Compensi OCCA registrati nel periodo transitorio (rapporto OCCA e fl_senza_calcoli = 'S')
            --3) Conguagli (rapporto OCCA, fl_senza_calcoli = 'S' e compenso.im_netto_percipiente < 0)
            --Nel caso 3) bisogna sempre sottrarre l'intera franchigia di 5000 euro
	    --Nel caso 2) bisogna controllare se lo stesso terzo ha un conguaglio perche' in tal caso
	    --            non si deve togliere la franchigia; se non ha conguagli, si deve togliere la franchigia
	    --            solo se ammontare è > 0 e imponibile > 5000
	    --Nel caso 1) bisogna prendere l'imponibile così come sta
	    Declare
	       rapporto	  	 VARCHAR2(10);
	       fl_sc	   	 VARCHAR2(1);
	       im_netto_perc	 NUMBER:=0;
	       rec_parametri_cnr parametri_cnr%Rowtype;
	    Begin
	       Select c.cd_tipo_rapporto, c.fl_senza_calcoli, c.im_netto_percipiente
	       Into rapporto, fl_sc, im_netto_perc
	       From compenso c
	       Where c.cd_cds = aRecVEstraiDatiInps.cd_cds_compenso
	         And c.cd_unita_organizzativa = aRecVEstraiDatiInps.cd_uo_compenso
	         And c.esercizio = aRecVEstraiDatiInps.esercizio_compenso
	         And c.pg_compenso = aRecVEstraiDatiInps.pg_compenso;
	         If rapporto != 'OCCA' Or  fl_sc = 'N' Then  -- non devo fare nulla
	            Null;
	         Else
	            rec_parametri_cnr := cnrutl001.getRecParametriCnr(inEsercizio);
	            --Devo vedere se e' un conguaglio
	            If Nvl(im_netto_perc,0) < 0 Then --è un conguaglio
	               If aRecEstrazioneInpsDett.imponibile_cori > Nvl(rec_parametri_cnr.importo_franchigia_occa,0) Then
	                   aRecEstrazioneInpsDett.imponibile_cori :=
	                      aRecEstrazioneInpsDett.imponibile_cori - Nvl(rec_parametri_cnr.importo_franchigia_occa,0);
	               End If;
	            Else
	                 --Non e' un Conguaglio ma devo vedere se togliere la franchigia
			 Declare
			    contacong NUMBER:= 0;
			    contaOccaSCNoCong NUMBER:= 0;
			 Begin
			    Select Count(1)
			    Into contacong
			    From compenso c
			    Where c.cd_terzo = aRecVEstraiDatiInps.cd_terzo
			      And to_char(c.dt_emissione_mandato,'YYYY') = inEsercizio
			      And c.cd_tipo_rapporto ='OCCA'
			      And c.fl_senza_calcoli = 'Y'
			      And c.im_netto_percipiente < 0;

			      If contacong > 0 Then
			         Null;
			      Else
			        --esiste solo un caso ed e' registrato male (hanno gia' tolto
				--loro la franchigia) quindi non la tolgo
			          --If aRecEstrazioneInpsDett.imponibile_cori > Nvl(rec_parametri_cnr.importo_franchigia_occa,0) Then
			          --    aRecEstrazioneInpsDett.imponibile_cori :=
			          --        aRecEstrazioneInpsDett.imponibile_cori - Nvl(rec_parametri_cnr.importo_franchigia_occa,0);
			          --End If;
			         Null;
			      End If;
			 End;
		    End If;
	         End If;
	    End;

            IF aRecVEstraiDatiInps.pg_riga = 0 THEN
               --aRecEstrazioneInpsDett.imponibile_cori_dett:=aRecVEstraiDatiInps.imponibile;
               aRecEstrazioneInpsDett.imponibile_cori_dett:=aRecEstrazioneInpsDett.imponibile_cori;
               aRecEstrazioneInpsDett.aliquota_cori_dett:=aRecVEstraiDatiInps.aliquota;
               aRecEstrazioneInpsDett.ammontare_cori_dett:=aRecVEstraiDatiInps.ammontare;
            ELSE
               aRecEstrazioneInpsDett.imponibile_cori_dett:=aRecVEstraiDatiInps.imponibile_det;
               aRecEstrazioneInpsDett.aliquota_cori_dett:=aRecVEstraiDatiInps.aliquota_det;
               aRecEstrazioneInpsDett.ammontare_cori_dett:=aRecVEstraiDatiInps.ammontare_det;
            END IF;
            aRecEstrazioneInpsDett.im_liquidato_versamento:=0;
            aRecEstrazioneInpsDett.dacr:=dataOdierna;
            aRecEstrazioneInpsDett.utcr:=inUtente;
            aRecEstrazioneInpsDett.duva:=dataOdierna;
            aRecEstrazioneInpsDett.utuv:=inUtente;
            aRecEstrazioneInpsDett.pg_ver_rec:=1;
         ELSE

            IF aRecVEstraiDatiInps.pg_riga = 0 THEN
               aRecEstrazioneInpsDett.aliquota_cori_dett:=aRecEstrazioneInpsDett.aliquota_cori_dett +
                                                          aRecVEstraiDatiInps.aliquota;
               aRecEstrazioneInpsDett.ammontare_cori_dett:=aRecEstrazioneInpsDett.ammontare_cori_dett +
                                                           aRecVEstraiDatiInps.ammontare;
            ELSE
               aRecEstrazioneInpsDett.aliquota_cori_dett:=aRecEstrazioneInpsDett.aliquota_cori_dett +
                                                          aRecVEstraiDatiInps.aliquota_det;
               aRecEstrazioneInpsDett.ammontare_cori_dett:=aRecEstrazioneInpsDett.ammontare_cori_dett +
                                                           aRecVEstraiDatiInps.ammontare_det;
            END IF;

         END IF;

      END LOOP;

      CLOSE gen_cur;

      -- Scrittura ultimo record
      -- ma solo se la fetch ha restituito dei valori
      If memChiaveCompenso Is Not Null Then
          insEstrazioneInpsDett(aRecEstrazioneInpsDett);
      End If;

   END;

END inserisciDatiINPSDett;

-- =================================================================================================
-- Estrazione dati per stampa INPS (Aggiornamento record in ESTRAZIONE_INPS_DETT per versamenti)
-- =================================================================================================
PROCEDURE upgDatiINPSDettVersamento
   (
    inEsercizio NUMBER,
    inRepID INTEGER
   ) IS

   aDtEmissione DATE;
   aDtTrasmissione DATE;
   aDtPagamento DATE;

   aRecEstrazioneINPSDett ESTRAZIONE_INPS_DETT%ROWTYPE;
   aRecLiquidGruppoCori LIQUID_GRUPPO_CORI%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Recupero delle informazioni relativi ai versamenti

   BEGIN

      OPEN gen_cur FOR

           SELECT A.cd_cds_compenso,
                  A.cd_uo_compenso,
                  A.esercizio_compenso,
                  A.pg_compenso,
                  A.cd_contributo_ritenuta,
                  C.cd_cds,
                  C.esercizio,
                  C.cd_unita_organizzativa,
                  C.pg_liquidazione,
                  C.im_liquidato,
                  C.fl_accentrato,
                  C.stato,
                  C.cd_gruppo_cr,
                  C.cd_cds_doc,
                  C.esercizio_doc,
                  C.pg_doc,
                  C.cd_cds_rev,
                  C.esercizio_rev,
                  C.pg_rev
           FROM   ESTRAZIONE_INPS_DETT A,
                  LIQUID_GRUPPO_CORI_DET B,
                  LIQUID_GRUPPO_CORI C
           WHERE  A.id_estrazione = inRepID AND
                  A.esercizio = inEsercizio AND
                  B.cd_cds = A.cd_cds_compenso AND
                  B.cd_unita_organizzativa = A.cd_uo_compenso AND
                  B.esercizio_contributo_ritenuta = A.esercizio_compenso AND
                  B.pg_compenso = A.pg_compenso AND
                  B.cd_contributo_ritenuta = A.cd_contributo_ritenuta AND
                  C.cd_cds = B.cd_cds AND
                  C.esercizio = B.esercizio AND
                  C.cd_unita_organizzativa = B.cd_unita_organizzativa AND
                  C.pg_liquidazione = B.pg_liquidazione AND
                  C.cd_cds_origine = B.cd_cds_origine AND
                  C.cd_uo_origine = B.cd_uo_origine AND
                  C.pg_liquidazione_origine = B.pg_liquidazione_origine AND
                  C.cd_gruppo_cr = B.cd_gruppo_cr AND
                  C.cd_regione = B.cd_regione AND
                  C.pg_comune = B.pg_comune;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneINPSDett.cd_cds_compenso,
               aRecEstrazioneINPSDett.cd_uo_compenso,
               aRecEstrazioneINPSDett.esercizio_compenso,
               aRecEstrazioneINPSDett.pg_compenso,
               aRecEstrazioneINPSDett.cd_contributo_ritenuta,
               aRecLiquidGruppoCori.cd_cds,
               aRecLiquidGruppoCori.esercizio,
               aRecLiquidGruppoCori.cd_unita_organizzativa,
               aRecLiquidGruppoCori.pg_liquidazione,
               aRecLiquidGruppoCori.im_liquidato,
               aRecLiquidGruppoCori.fl_accentrato,
               aRecLiquidGruppoCori.stato,
               aRecLiquidGruppoCori.cd_gruppo_cr,
               aRecLiquidGruppoCori.cd_cds_doc,
               aRecLiquidGruppoCori.esercizio_doc,
               aRecLiquidGruppoCori.pg_doc,
               aRecLiquidGruppoCori.cd_cds_rev,
               aRecLiquidGruppoCori.esercizio_rev,
               aRecLiquidGruppoCori.pg_rev;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Recupero date emissione, trasmissione e pagamento di mandato o reversale associati
         -- alla liquidazione cori

         BEGIN

            IF aRecLiquidGruppoCori.pg_doc IS NOT NULL THEN

               SELECT dt_emissione, TRUNC(dt_trasmissione), TRUNC(dt_pagamento)
                      INTO aDtEmissione, aDtTrasmissione, aDtPagamento
               FROM   MANDATO
               WHERE  cd_cds = aRecLiquidGruppoCori.cd_cds_doc AND
                      esercizio = aRecLiquidGruppoCori.esercizio_doc AND
                      pg_mandato = aRecLiquidGruppoCori.pg_doc;

            END IF;

            IF (aRecLiquidGruppoCori.pg_doc IS NULL AND
                aRecLiquidGruppoCori.pg_rev IS NOT NULL) THEN

               SELECT dt_emissione, TRUNC(dt_trasmissione), TRUNC(dt_incasso)
                      INTO aDtEmissione, aDtTrasmissione, aDtPagamento
               FROM   REVERSALE
               WHERE  cd_cds = aRecLiquidGruppoCori.cd_cds_rev AND
                      esercizio = aRecLiquidGruppoCori.esercizio_rev AND
                      pg_reversale = aRecLiquidGruppoCori.pg_rev;

            END IF;

         EXCEPTION

            WHEN no_data_found THEN
                 aDtEmissione:=NULL;
                 aDtTrasmissione:=NULL;
                 aDtPagamento:=NULL;

         END;

         -- Aggiornamento dati versamento su tabella ESTRAZIONE_INPS_DETT

         BEGIN

            UPDATE ESTRAZIONE_INPS_DETT
            SET    cd_cds_versamento = aRecLiquidGruppoCori.cd_cds,
                   esercizio_versamento = aRecLiquidGruppoCori.esercizio,
                   cd_uo_versamento = aRecLiquidGruppoCori.cd_unita_organizzativa,
                   pg_liquidazione_versamento = aRecLiquidGruppoCori.pg_liquidazione,
                   cd_gruppo_cr_versamento = aRecLiquidGruppoCori.cd_gruppo_cr,
                   im_liquidato_versamento = aRecLiquidGruppoCori.im_liquidato,
                   dt_emissione_versamento = aDtEmissione,
                   dt_trasmissione_versamento = aDtTrasmissione,
                   dt_pagamento_versamento = aDtPagamento
            WHERE  id_estrazione = inRepID AND
                   esercizio = inEsercizio AND
                   cd_cds_compenso = aRecEstrazioneINPSDett.cd_cds_compenso AND
                   cd_uo_compenso = aRecEstrazioneINPSDett.cd_uo_compenso AND
                   esercizio_compenso = aRecEstrazioneINPSDett.esercizio_compenso AND
                   pg_compenso = aRecEstrazioneINPSDett.pg_compenso;

         END;

      END LOOP;

      CLOSE gen_cur;

   END;

END upgDatiINPSDettVersamento;

-- =================================================================================================
-- Composizione matrice versamenti INPS
-- =================================================================================================
PROCEDURE creaMatriceVersamentiINPS
   (
    inEsercizio NUMBER,
    inRepID INTEGER
   ) IS

   aDataRif DATE;

   i BINARY_INTEGER;
   k BINARY_INTEGER;

   aRecEstrazioneINPSDett ESTRAZIONE_INPS_DETT%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializza matrice versamenti

   BEGIN

      FOR i IN 1 .. 12

      LOOP

         versamenti_tab(i).tDtVersamento:=NULL;
         versamenti_tab(i).tNumVersamenti:=0;
         versamenti_tab(i).tImVersato:=0;
         versamenti_tab(i).tImDovuto:=0;

      END LOOP;

   END;

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale di lettura ESTRAZIONE_INPS_DETT per valorizzare la matrice dei versamenti

   BEGIN

      IF parametri_tab(2).stringa = CNRCTB575.STATO_LIQUID_CORI_INV THEN

         OPEN gen_cur FOR

              SELECT DISTINCT cd_cds_versamento,
                     esercizio_versamento,
                     cd_uo_versamento,
                     pg_liquidazione_versamento,
                     im_liquidato_versamento,
                     dt_trasmissione_versamento
              FROM   ESTRAZIONE_INPS_DETT
              WHERE  id_estrazione = inRepID AND
                     esercizio = inEsercizio AND
                     dt_trasmissione_versamento IS NOT NULL
              ORDER BY dt_trasmissione_versamento;

      ELSE

         OPEN gen_cur FOR

              SELECT DISTINCT cd_cds_versamento,
                     esercizio_versamento,
                     cd_uo_versamento,
                     pg_liquidazione_versamento,
                     im_liquidato_versamento,
                     dt_emissione_versamento
              FROM   ESTRAZIONE_INPS_DETT
              WHERE  id_estrazione = inRepID AND
                     esercizio = inEsercizio AND
                     dt_emissione_versamento IS NOT NULL
              ORDER BY dt_emissione_versamento;

      END IF;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneINPSDett.cd_cds_versamento,
               aRecEstrazioneINPSDett.esercizio_versamento,
               aRecEstrazioneINPSDett.cd_uo_versamento,
               aRecEstrazioneINPSDett.pg_liquidazione_versamento,
               aRecEstrazioneINPSDett.im_liquidato_versamento,
               aDataRif;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Determino il mese di riferimento. E' pari allo stesso mese per giorni > di 15 altrimenti mese = mese -1.
         -- Correggo il mese mantenendolo pari a 1 se la data di riferimento è inferiore al 16/01/inEsercizio.
         -- Correggo il mese mantenendolo pari a 12 se la data di riferimento è maggiore del 31/12/inEsercizio.

         IF aDataRif > TO_DATE('3112' || inEsercizio, 'DDMMYYYY') THEN
            k:=12;
         ELSE
            IF IBMUTL001.getDayOfDate(aDataRif) > 15 THEN
               k:=IBMUTL001.getMonthOfDate(aDataRif);
            ELSE
               IF aDataRif < TO_DATE('1601' || inEsercizio,'DDMMYYYY') THEN
                  k:=1;
               ELSE
                  k:=IBMUTL001.getMonthOfDate(aDataRif) - 1;
               END IF;
            END IF;
         END IF;

         -- Valorizzo la matrice dei versamenti

         IF versamenti_tab(k).tDtVersamento IS NULL THEN
            versamenti_tab(k).tDtVersamento:=aDataRif;
            versamenti_tab(k).tNumVersamenti:=1;
         ELSE
            versamenti_tab(k).tNumVersamenti:=versamenti_tab(k).tNumVersamenti + 1;
            IF versamenti_tab(k).tDtVersamento < aDataRif THEN
               versamenti_tab(k).tDtVersamento:=aDataRif;
            END IF;
         END IF;

         versamenti_tab(k).tImVersato:=versamenti_tab(k).tImVersato + aRecEstrazioneINPSDett.im_liquidato_versamento;

      END LOOP;

      CLOSE gen_cur;

   END;

END creaMatriceVersamentiINPS;

-- =================================================================================================
-- Inserimento di un record in VP_STM_ESTRAZIONE_INPS
-- =================================================================================================
PROCEDURE insRecordStampaINPS
   (
    aRecVpStmEstrazioneINPS VP_STM_ESTRAZIONE_INPS%ROWTYPE,
    aSequenza IN OUT INTEGER
   ) IS

BEGIN

   aSequenza:=aSequenza + 1;

   INSERT INTO VP_STM_ESTRAZIONE_INPS
          (id_report,
           gruppo,
           tipologia_riga,
           sequenza,
           titolo,
           esercizio,
           cd_uo_descrizione,
           cd_anag,
           cognome_nome,
           comune_data_nascita,
           codice_fiscale,
           indirizzo,
           cap_comune_residenza,
           attivita_inps,
           altra_forma_ass_inps,
           cd_cds_compenso,
           cd_uo_compenso,
           esercizio_compenso,
           pg_compenso,
           v_mese_pagamento,
           v_data_versamento,
           v_numero_versamenti,
           v_importo_versato,
           v_importo_dovuto,
           v_importo_saldo,
           d_mese_compenso,
           d_imponibile_previdenziale,
           d_tipo_cori_inps,
           d_fl_senza_calcoli,
           d_imponibile_aliquota,
           d_aliquota,
           d_contributo_dovuto,
           d_dt_attivita_da,
           d_dt_attivita_a,
           d_dt_versamento)
   VALUES (aRecVpStmEstrazioneINPS.id_report,
           aRecVpStmEstrazioneINPS.gruppo,
           aRecVpStmEstrazioneINPS.tipologia_riga,
           aSequenza,
           aRecVpStmEstrazioneINPS.titolo,
           aRecVpStmEstrazioneINPS.esercizio,
           aRecVpStmEstrazioneINPS.cd_uo_descrizione,
           aRecVpStmEstrazioneINPS.cd_anag,
           aRecVpStmEstrazioneINPS.cognome_nome,
           aRecVpStmEstrazioneINPS.comune_data_nascita,
           aRecVpStmEstrazioneINPS.codice_fiscale,
           aRecVpStmEstrazioneINPS.indirizzo,
           aRecVpStmEstrazioneINPS.cap_comune_residenza,
           aRecVpStmEstrazioneINPS.attivita_inps,
           aRecVpStmEstrazioneINPS.altra_forma_ass_inps,
           aRecVpStmEstrazioneINPS.cd_cds_compenso,
           aRecVpStmEstrazioneINPS.cd_uo_compenso,
           aRecVpStmEstrazioneINPS.esercizio_compenso,
           aRecVpStmEstrazioneINPS.pg_compenso,
           aRecVpStmEstrazioneINPS.v_mese_pagamento,
           aRecVpStmEstrazioneINPS.v_data_versamento,
           aRecVpStmEstrazioneINPS.v_numero_versamenti,
           aRecVpStmEstrazioneINPS.v_importo_versato,
           aRecVpStmEstrazioneINPS.v_importo_dovuto,
           aRecVpStmEstrazioneINPS.v_importo_saldo,
           aRecVpStmEstrazioneINPS.d_mese_compenso,
           aRecVpStmEstrazioneINPS.d_imponibile_previdenziale,
           aRecVpStmEstrazioneINPS.d_tipo_cori_inps,
           aRecVpStmEstrazioneINPS.d_fl_senza_calcoli,
           aRecVpStmEstrazioneINPS.d_imponibile_aliquota,
           aRecVpStmEstrazioneINPS.d_aliquota,
           aRecVpStmEstrazioneINPS.d_contributo_dovuto,
           aRecVpStmEstrazioneINPS.d_dt_attivita_da,
           aRecVpStmEstrazioneINPS.d_dt_attivita_a,
           aRecVpStmEstrazioneINPS.d_dt_versamento);

END insRecordStampaINPS;

-- =================================================================================================
-- Inserimento dati per stampa INPS - Tabella VP_STM_ESTRAZIONE_INPS (DETTAGLIO COMPENSI)
-- =================================================================================================
PROCEDURE insDatiDettStampaINPS
   (
    inEsercizio NUMBER,
    inCdCds VARCHAR2,
    inCdUo VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   memCdAnag NUMBER(8);
   aPgStampa NUMBER;
   aPgReportGenerico NUMBER;
   aStringa VARCHAR2(200);
   aSequenza INTEGER;
   totImponibileINPS NUMBER(15,2);
   totDovutoINPS NUMBER(15,2);
   totVersatoINPS NUMBER(15,2);
   totSaldoINPS NUMBER(15,2);
   aDtMax DATE;
   aDtRif DATE;

   i BINARY_INTEGER;

   aRecEstrazioneINPSDett ESTRAZIONE_INPS_DETT%ROWTYPE;
   aRecVAnagraficoTutto V_ANAGRAFICO_TUTTO%ROWTYPE;
   aRecVpStmEstrazioneINPS VP_STM_ESTRAZIONE_INPS%ROWTYPE;
   aRecVpStmEstrazioneINPSBase VP_STM_ESTRAZIONE_INPS%ROWTYPE;
   aRecPrintPriority PRINT_PRIORITY%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazione variabili

   memCdAnag:=NULL;
   aSequenza:=100;
   aDtMax:=TO_DATE('3112' || inEsercizio,'DDMMYYYY');

   BEGIN

      SELECT pg_stampa.nextval INTO aPgStampa
      FROM   DUAL;

      SELECT IBMSEQ00_STAMPA.nextval INTO aPgReportGenerico
      FROM   DUAL;

   END;

   aRecVpStmEstrazioneINPSBase:=NULL;
   aRecVpStmEstrazioneINPSBase.id_report:=aPgReportGenerico;
   aRecVpStmEstrazioneINPSBase.gruppo:='B';
   aRecVpStmEstrazioneINPSBase.tipologia_riga:=TIRIGA_DET;
   aRecVpStmEstrazioneINPSBase.titolo:='ESTRAZIONE DATI INPS';
   aRecVpStmEstrazioneINPSBase.esercizio:=inEsercizio;
   IF parametri_tab(1).intero = 0 THEN
      aRecVpStmEstrazioneINPSBase.cd_uo_descrizione:=inCdCDS || ' ' || parametri_tab(1).stringa;
   ELSE
      aRecVpStmEstrazioneINPSBase.cd_uo_descrizione:=inCdUo || ' ' || parametri_tab(1).stringa;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale di lettura ESTRAZIONE_INPS_DETT per la scrittura dei dettagli per anagrafico
   -- in VP_STM_ESTRAZIONE_INPS e la valorizzazione dell'importo dovuto sulla matrice dei versamenti

   BEGIN

      -- Inizializzazione variabili

      IF parametri_tab(2).stringa = CNRCTB575.STATO_LIQUID_CORI_INV THEN

         OPEN gen_cur FOR

              SELECT *
              FROM   ESTRAZIONE_INPS_DETT
              WHERE  id_estrazione = inRepID AND
                     esercizio = inEsercizio
              ORDER BY cd_anag,
                       TO_CHAR(dt_trasmissione_mandato,'YYYYMM'),
                       cd_contributo_ritenuta,
                       dt_cmp_da_compenso;

      ELSE

         OPEN gen_cur FOR

              SELECT *
              FROM   ESTRAZIONE_INPS_DETT
              WHERE  id_estrazione = inRepID AND
                     esercizio = inEsercizio
              ORDER BY cd_anag,
                       TO_CHAR(dt_emissione_mandato,'YYYYMM'),
                       cd_contributo_ritenuta,
                       dt_cmp_da_compenso;

      END IF;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneINPSDett;

         EXIT WHEN gen_cur%NOTFOUND;

         -- E' la prima volta o il soggetto anagrafico letto è diverso dal precedente --------------

         IF (memCdAnag IS NULL OR
             memCdAnag != aRecEstrazioneINPSDett.cd_anag) THEN

            -- Scrittura totali dettaglio B

            IF memCdAnag IS NOT NULL THEN
               aRecVpStmEstrazioneINPS.tipologia_riga:=TIRIGA_TOT;
               aRecVpStmEstrazioneINPS.cd_cds_compenso:=NULL;
               aRecVpStmEstrazioneINPS.cd_uo_compenso:=NULL;
               aRecVpStmEstrazioneINPS.esercizio_compenso:=NULL;
               aRecVpStmEstrazioneINPS.pg_compenso:=NULL;
               aRecVpStmEstrazioneINPS.d_mese_compenso:='Totale per percipiente';
               aRecVpStmEstrazioneINPS.d_imponibile_previdenziale:=totImponibileINPS;
               aRecVpStmEstrazioneINPS.d_tipo_cori_inps:=NULL;
               aRecVpStmEstrazioneINPS.d_fl_senza_calcoli:=NULL;
               aRecVpStmEstrazioneINPS.d_imponibile_aliquota:=NULL;
               aRecVpStmEstrazioneINPS.d_aliquota:=NULL;
               aRecVpStmEstrazioneINPS.d_contributo_dovuto:=totDovutoINPS;
               aRecVpStmEstrazioneINPS.d_dt_attivita_da:=NULL;
               aRecVpStmEstrazioneINPS.d_dt_attivita_a:=NULL;
               aRecVpStmEstrazioneINPS.d_dt_versamento:=NULL;
               insRecordStampaINPS(aRecVpStmEstrazioneINPS,
                                   aSequenza);
            END IF;

            -- Costruzione del record base ed azzeramento variabili di totale

            aRecVpStmEstrazioneINPS:=aRecVpStmEstrazioneINPSBase;
            memCdAnag:=aRecEstrazioneINPSDett.cd_anag;
            totImponibileINPS:=0;
            totDovutoINPS:=0;

            BEGIN

               SELECT * INTO aRecVAnagraficoTutto
               FROM   V_ANAGRAFICO_TUTTO
               WHERE  cd_anag = aRecEstrazioneINPSDett.cd_anag;

            END;

            aRecVpStmEstrazioneINPS.cd_anag:=aRecVAnagraficoTutto.cd_anag;
            aRecVpStmEstrazioneINPS.cognome_nome:=LTRIM(RTRIM(aRecVAnagraficoTutto.cognome || ' ' ||
                                                              aRecVAnagraficoTutto.nome || ' ' ||
                                                              aRecVAnagraficoTutto.ragione_sociale));

            aStringa:=NULL;
            IF aRecVAnagraficoTutto.ds_comune_nascita IS NOT NULL THEN
               aStringa:=aStringa || aRecVAnagraficoTutto.ds_comune_nascita || ' ';
            END IF;
            IF aRecVAnagraficoTutto.cd_provincia_nascita IS NOT NULL THEN
               aStringa:=aStringa || '(' || aRecVAnagraficoTutto.cd_provincia_nascita || ') ';
            END IF;
            IF aRecVAnagraficoTutto.dt_nascita IS NOT NULL THEN
               aStringa:=aStringa || TO_CHAR(aRecVAnagraficoTutto.dt_nascita,'DD/MM/YYYY');
            END IF;
            aRecVpStmEstrazioneINPS.comune_data_nascita:=aStringa;

            aRecVpStmEstrazioneINPS.codice_fiscale:=aRecVAnagraficoTutto.codice_fiscale;

            aStringa:=NULL;
            IF aRecVAnagraficoTutto.via_fiscale IS NOT NULL THEN
               aStringa:=aStringa || aRecVAnagraficoTutto.via_fiscale || ' ';
            END IF;
            IF aRecVAnagraficoTutto.num_civico_fiscale IS NOT NULL THEN
               aStringa:=aStringa || aRecVAnagraficoTutto.num_civico_fiscale;
            END IF;
            aRecVpStmEstrazioneINPS.indirizzo:=aStringa;

            aStringa:=NULL;
            IF aRecVAnagraficoTutto.cap_comune_fiscale IS NOT NULL THEN
               aStringa:=aStringa || aRecVAnagraficoTutto.cap_comune_fiscale || ' - ';
            END IF;
            IF aRecVAnagraficoTutto.ds_comune_fiscale IS NOT NULL THEN
               aStringa:=aStringa || aRecVAnagraficoTutto.ds_comune_fiscale || ' ';
            END IF;
            IF aRecVAnagraficoTutto.cd_provincia_fiscale IS NOT NULL THEN
               aStringa:=aStringa || '(' || aRecVAnagraficoTutto.cd_provincia_fiscale || ') ';
            END IF;
            aRecVpStmEstrazioneINPS.cap_comune_residenza:=aStringa;

            aStringa:=NULL;
            IF aRecVAnagraficoTutto.cd_attivita_inps IS NOT NULL THEN
               aStringa:=aStringa || aRecVAnagraficoTutto.cd_attivita_inps || ' - ';
            END IF;
            IF aRecVAnagraficoTutto.ds_attivita_inps IS NOT NULL THEN
               aStringa:=aStringa || aRecVAnagraficoTutto.ds_attivita_inps;
            END IF;
            aRecVpStmEstrazioneINPS.attivita_inps:=aStringa;

            aStringa:=NULL;
            IF aRecVAnagraficoTutto.altra_ass_previd_inps IS NOT NULL THEN
               aStringa:=aStringa || aRecVAnagraficoTutto.altra_ass_previd_inps || ' - ';
            END IF;
            IF aRecVAnagraficoTutto.ds_altra_ass_previd_inps IS NOT NULL THEN
               aStringa:=aStringa || aRecVAnagraficoTutto.ds_altra_ass_previd_inps;
            END IF;
            aRecVpStmEstrazioneINPS.altra_forma_ass_inps:=aStringa;

         END IF;

         -- Scrittura dati da ESTRAZIONE_INPS_DETT -------------------------------------------------

         aRecVpStmEstrazioneINPS.cd_cds_compenso:=aRecEstrazioneINPSDett.cd_cds_compenso;
         aRecVpStmEstrazioneINPS.cd_uo_compenso:=aRecEstrazioneINPSDett.cd_uo_compenso;
         aRecVpStmEstrazioneINPS.esercizio_compenso:=aRecEstrazioneINPSDett.esercizio_compenso;
         aRecVpStmEstrazioneINPS.pg_compenso:=aRecEstrazioneINPSDett.pg_compenso;
         IF parametri_tab(2).stringa = CNRCTB575.STATO_LIQUID_CORI_INV THEN
            aRecVpStmEstrazioneINPS.d_mese_compenso:=TO_CHAR(aRecEstrazioneINPSDett.dt_trasmissione_mandato,'MM');
         ELSE
            aRecVpStmEstrazioneINPS.d_mese_compenso:=TO_CHAR(aRecEstrazioneINPSDett.dt_emissione_mandato,'MM');
         END IF;
         aRecVpStmEstrazioneINPS.d_imponibile_previdenziale:=aRecEstrazioneINPSDett.imponibile_cori;
         aRecVpStmEstrazioneINPS.d_tipo_cori_inps:=aRecEstrazioneINPSDett.cd_contributo_ritenuta || ' - ' ||
                                                   aRecEstrazioneINPSDett.ds_contributo_ritenuta;
         aRecVpStmEstrazioneINPS.d_fl_senza_calcoli:=aRecEstrazioneINPSDett.fl_senza_calcoli;
         aRecVpStmEstrazioneINPS.d_imponibile_aliquota:=aRecEstrazioneINPSDett.imponibile_cori_dett;
         aRecVpStmEstrazioneINPS.d_aliquota:=aRecEstrazioneINPSDett.aliquota_cori_dett;
         aRecVpStmEstrazioneINPS.d_contributo_dovuto:=aRecEstrazioneINPSDett.ammontare_cori_dett;
         aRecVpStmEstrazioneINPS.d_dt_attivita_da:=aRecEstrazioneINPSDett.dt_cmp_da_compenso;
         aRecVpStmEstrazioneINPS.d_dt_attivita_a:=aRecEstrazioneINPSDett.dt_cmp_a_compenso;
         IF parametri_tab(2).stringa = CNRCTB575.STATO_LIQUID_CORI_INV THEN
            IF aRecEstrazioneINPSDett.dt_trasmissione_versamento IS NOT NULL THEN
               aRecVpStmEstrazioneINPS.d_dt_versamento:=aRecEstrazioneINPSDett.dt_trasmissione_versamento;
            ELSE
               aRecVpStmEstrazioneINPS.d_dt_versamento:=NULL;
            END IF;
         ELSE
            IF aRecEstrazioneINPSDett.dt_emissione_versamento IS NOT NULL THEN
               aRecVpStmEstrazioneINPS.d_dt_versamento:=aRecEstrazioneINPSDett.dt_emissione_versamento;
            ELSE
               aRecVpStmEstrazioneINPS.d_dt_versamento:=NULL;
            END IF;
         END IF;

         insRecordStampaINPS(aRecVpStmEstrazioneINPS,
                             aSequenza);

         -- Memorizzazione importi totali ----------------------------------------------------------

         totImponibileINPS:=totImponibileINPS + aRecEstrazioneINPSDett.imponibile_cori;
         totDovutoINPS:=totDovutoINPS + aRecEstrazioneINPSDett.ammontare_cori_dett;

         -- Memorizzazione del dovuto su matrice versamenti ----------------------------------------

         IF parametri_tab(2).stringa = CNRCTB575.STATO_LIQUID_CORI_INV THEN
            aDtRif:= aRecEstrazioneINPSDett.dt_trasmissione_mandato;
         ELSE
            aDtRif:= aRecEstrazioneINPSDett.dt_emissione_mandato;
         END IF;

         IF aDtRif > aDtMax THEN
            aDtRif:=aDtMax;
         END IF;

         i:=IBMUTL001.getMonthOfDate(aDtRif);
         versamenti_tab(i).tImDovuto:=versamenti_tab(i).tImDovuto + aRecEstrazioneINPSDett.ammontare_cori_dett;

      END LOOP;

      CLOSE gen_cur;

      -- Scrittura ultimo totale
      -- ma solo se la fetch ha restituito dei valori
      If memCdAnag IS Not Null Then
       aRecVpStmEstrazioneINPS.tipologia_riga:=TIRIGA_TOT;
       aRecVpStmEstrazioneINPS.cd_cds_compenso:=NULL;
       aRecVpStmEstrazioneINPS.cd_uo_compenso:=NULL;
       aRecVpStmEstrazioneINPS.esercizio_compenso:=NULL;
       aRecVpStmEstrazioneINPS.pg_compenso:=NULL;
       aRecVpStmEstrazioneINPS.d_mese_compenso:='Totale per percipiente';
       aRecVpStmEstrazioneINPS.d_imponibile_previdenziale:=totImponibileINPS;
       aRecVpStmEstrazioneINPS.d_tipo_cori_inps:=NULL;
       aRecVpStmEstrazioneINPS.d_fl_senza_calcoli:=NULL;
       aRecVpStmEstrazioneINPS.d_imponibile_aliquota:=NULL;
       aRecVpStmEstrazioneINPS.d_aliquota:=NULL;
       aRecVpStmEstrazioneINPS.d_contributo_dovuto:=totDovutoINPS;
       aRecVpStmEstrazioneINPS.d_dt_attivita_da:=NULL;
       aRecVpStmEstrazioneINPS.d_dt_attivita_a:=NULL;
       aRecVpStmEstrazioneINPS.d_dt_versamento:=NULL;
       insRecordStampaINPS(aRecVpStmEstrazioneINPS,
                           aSequenza);
      End If;

   END;

   -------------------------------------------------------------------------------------------------
   -- Scarico in VP_STM_ESTRAZIONE_INPS della matrice versamenti

   BEGIN

      -- Inizializzazione variabili

      aSequenza:=1;
      totDovutoINPS:=0;
      totVersatoINPS:=0;
      totSaldoINPS:=0;

      aRecVpStmEstrazioneINPSBase.gruppo:='A';
      aRecVpStmEstrazioneINPSBase.tipologia_riga:=TIRIGA_DET;
      aRecVpStmEstrazioneINPSBase.cd_anag:=0;


      -- Ciclo scarico versamenti

      FOR i IN 1 .. 12

      LOOP

         aRecVpStmEstrazioneINPS:=aRecVpStmEstrazioneINPSBase;
         aRecVpStmEstrazioneINPS.v_mese_pagamento:=LPAD(i,2,0);
         aRecVpStmEstrazioneINPS.v_data_versamento:=versamenti_tab(i).tDtVersamento;
         aRecVpStmEstrazioneINPS.v_numero_versamenti:=versamenti_tab(i).tNumVersamenti;
         aRecVpStmEstrazioneINPS.v_importo_versato:=versamenti_tab(i).tImVersato;
         aRecVpStmEstrazioneINPS.v_importo_dovuto:=versamenti_tab(i).tImDovuto;
         aRecVpStmEstrazioneINPS.v_importo_saldo:=aRecVpStmEstrazioneINPS.v_importo_versato -
                                                  aRecVpStmEstrazioneINPS.v_importo_dovuto;

         insRecordStampaINPS(aRecVpStmEstrazioneINPS,
                             aSequenza);

         totDovutoINPS:=totDovutoINPS + aRecVpStmEstrazioneINPS.v_importo_dovuto;
         totVersatoINPS:=totVersatoINPS + aRecVpStmEstrazioneINPS.v_importo_versato;
         totSaldoINPS:=totSaldoINPS + aRecVpStmEstrazioneINPS.v_importo_saldo;

      END LOOP;

      -- Totale finale

      aRecVpStmEstrazioneINPS:=aRecVpStmEstrazioneINPSBase;
      aRecVpStmEstrazioneINPS.tipologia_riga:=TIRIGA_TOT;
      aRecVpStmEstrazioneINPS.v_mese_pagamento:='Totale';
      aRecVpStmEstrazioneINPS.v_importo_versato:=totVersatoINPS;
      aRecVpStmEstrazioneINPS.v_importo_dovuto:=totDovutoINPS;
      aRecVpStmEstrazioneINPS.v_importo_saldo:=totSaldoINPS;

      insRecordStampaINPS(aRecVpStmEstrazioneINPS,
                          aSequenza);

   END;

   -------------------------------------------------------------------------------------------------
   -- Accodamento stampa
   BEGIN

      aRecPrintPriority:=CNRCTB890.getPrintPriority(IDNOMESTAMPA);

       CNRCTB890.insPrintSpooler(aPgStampa,
                                0,
                                aRecPrintPriority.ds_report,
                                IDNOMESTAMPA,
                                aRecPrintPriority.priority,
                                CNRCTB890.STM_VISIBILE_UTENTE,
                                inUtente,
                                aPgReportGenerico,
                                inUtente,
                                SYSDATE);

       CNRCTB890.insPrintSpoolerParam(aPgStampa,
       				     'id',
       				     'java.lang.Long',
                                     aPgReportGenerico,
                                     inUtente,
                                     SYSDATE);

   END;
End insDatiDettStampaINPS;

-- =================================================================================================
-- Estrazione dati per stampa INPS
-- =================================================================================================
PROCEDURE estrazioneDatiINPS
   (
    inEsercizio NUMBER,
    inCdCds VARCHAR2,
    inCdUo VARCHAR2,
    inRepID INTEGER,
    inUtente VARCHAR2,
    pg_exec NUMBER
   ) IS

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazione variabili

   dataOdierna:=sysdate;

   -------------------------------------------------------------------------------------------------
   -- Inserimento in ESTRAZIONE_INPS_DETT dei compensi oggetto di esposizione sulla stampa.

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS - Dettaglio elaborazione',
                    'FASE 1',
                    'FASE 1 - Inserimento compensi in ESTRAZIONE_INPS_DETT');

   inserisciDatiINPSDett(inEsercizio,
                         inCdCds,
                         inCdUo,
                         inRepID,
                         inUtente);

   COMMIT;

   -------------------------------------------------------------------------------------------------
   -- Inserimento in ESTRAZIONE_INPS_DETT dei compensi oggetto di esposizione sulla stampa. Recupero
   -- dei dati relativi ai versamenti

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS - Dettaglio elaborazione',
                    'FASE 2',
                    'FASE 2 - Aggiornamento dati versamenti su compensi presenti in ESTRAZIONE_INPS_DETT');

   upgDatiINPSDettVersamento(inEsercizio,
                             inRepID);

   COMMIT;

   -------------------------------------------------------------------------------------------------
   -- Composizione matrice versamenti INPS

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS - Dettaglio elaborazione',
                    'FASE 3',
                    'FASE 3 - Composizione matrice versamenti INPS');

   creaMatriceVersamentiINPS(inEsercizio,
                             inRepID);

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione dati per stampa su VP_STM_ESTRAZIONE_INPS

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS - Dettaglio elaborazione',
                    'FASE 4',
                    'FASE 4 - Valorizzazione dati per stampa su VP_STM_ESTRAZIONE_INPS');

   insDatiDettStampaINPS(inEsercizio,
                         inCdCds,
                         inCdUo,
                         inRepID,
                         inUtente);

   -------------------------------------------------------------------------------------------------
   -- Accodamento stampa

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS - Dettaglio elaborazione',
                    'FASE 5',
                    'FASE 5 - Stampa accodata');

END estrazioneDatiINPS;

-- =================================================================================================
-- Lancio estrazione INPS
-- =================================================================================================
PROCEDURE estrazioneINPSInterna
   (
    inEsercizio NUMBER,
    inCdCds VARCHAR2,
    inCdUo VARCHAR2,
    inRepID INTEGER,
    inMsgError IN OUT VARCHAR2,
    inUtente VARCHAR2,
    pg_exec NUMBER
   ) IS

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Controllo congruenza parametri in input

   chkParametriInput(inEsercizio,
                     inCdCds,
                     inCdUo,
                     inRepID,
                     inUtente);

   -------------------------------------------------------------------------------------------------
   -- Lettura parametri di base alla procedura.

   valorizzaParametri(inEsercizio,
                      inCdCds,
                      inCdUo);

   -------------------------------------------------------------------------------------------------
   -- Estrazione dati per INPS.

   estrazioneDatiINPS(inEsercizio,
                      inCdCds,
                      inCdUo,
                      inRepID,
                      inUtente,
                      pg_exec);


END  estrazioneINPSInterna;

-- =================================================================================================
-- Guscio per gestione estrazione INPS in batch
-- =================================================================================================
PROCEDURE job_estrazioneINPS
   (
    job NUMBER,
    pg_exec NUMBER,
    next_date DATE,
    inEsercizio NUMBER,
    inCdCds VARCHAR2,
    inCdUo VARCHAR2,
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
                                  'Estrazione dati INPS. Start:' || TO_CHAR(sysdate,'YYYY/MM/DD HH-MI-SS'));

   BEGIN

      estrazioneINPSInterna(inEsercizio,
                            inCdCds,
                            inCdUo,
                            inRepID,
                            aStringa,
                            inUtente,
                            pg_exec);

      COMMIT;

      -- Messaggio di operazione completata ad utente

      IBMUTL205.LOGINF('Estrazione dati INPS',
                       'Estrazione dati INPS ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS'),
                       'Operazione completata con successo. ' ||
                       'Per la gestione della stampa entrare nella funzione Code di stampa',
                       inUtente);

   EXCEPTION

      WHEN others THEN
           ROLLBACK;

      -- Messaggio di attenzione ad utente

      IBMUTL205.LOGWAR('Estrazione dati INPS ' || errori_tab.COUNT,
                       'Estrazione dati INPS ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                       '(pg_exec = ' || pg_exec || ')', DBMS_UTILITY.FORMAT_ERROR_STACK, inUtente);

      -- Scrittura degli eventuali altri errori

      IF errori_tab.COUNT > 0 THEN

         FOR i IN errori_tab.FIRST .. errori_tab.LAST

         LOOP

            IBMUTL200.LOGWAR(pg_exec,
                             'Estrazione dati INPS - Dettaglio errori',
                             'Errore : ' || errori_tab(i).tStringaErr,
                             'Identificativo = ' || errori_tab(i).tStringaKey);

         END LOOP;

      END IF;

   END;

END job_estrazioneINPS;

-- =================================================================================================
-- Main estrazione INPS
-- =================================================================================================
PROCEDURE estrazioneINPS
   (
    inEsercizio NUMBER,
    inCdCds VARCHAR2,
    inCdUo VARCHAR2,
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
   -- Attivazione della gestione batch per estrazione INPS

   aProcedure:='CNRCTB920.job_estrazioneINPS(job, ' ||
                                             'pg_exec, ' ||
                                             'next_date, ' ||
                                             inEsercizio || ',''' ||
                                             inCdCds || ''',' ||
                                             '''' || inCdUo || ''',' ||
                                             inRepID || ',''' ||
                                             aStringa || ''',' ||
                                             '''' || inUtente || ''');';

   IBMUTL210.CREABATCHDINAMICO('Estrazione dati INPS',
                               aProcedure,
                               inUtente);

   IBMUTL001.deferred_commit;

   IBMERR001.RAISE_ERR_GENERICO
      ('Operazione sottomessa per esecuzione. Al completamento l''utente riceverà un messaggio di notifica ' ||
       'dello stato dell''operazione');


END estrazioneINPS;

-- =================================================================================================

END; -- PACKAGE END;
