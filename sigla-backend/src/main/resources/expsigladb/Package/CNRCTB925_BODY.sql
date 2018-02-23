--------------------------------------------------------
--  DDL for Package Body CNRCTB925
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB925" AS

-- =================================================================================================
-- Controllo congruenza parametri in input
-- =================================================================================================
PROCEDURE chkParametriInput
   (
    inEsercizio NUMBER,
    inMese NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

Begin

   -------------------------------------------------------------------------------------------------
   -- Verifica congruenza parametri.
   --   aEsercizio, aMese, repID e aUtente non possono essere nulli

   IF (inEsercizio IS NULL Or
       inMese IS NULL OR
       inRepID IS NULL OR
       inUtente IS NULL) THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Valorizzazione parametri in input errata. ' ||
          'Un identificativo (esercizio, mese, identificativo report, id utente) risulta non valorizzato');
   END IF;

End chkParametriInput;

-- =================================================================================================
-- Memorizzazione parametri di uso della procedura di stampa
-- =================================================================================================
PROCEDURE valorizzaParametri
   (
    inEsercizio NUMBER
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

         IF    i = 1 THEN
		null;
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
-- Inserimento record in ESTRAZ_INPS_MENS_DETT
-- =================================================================================================
PROCEDURE insEstrazioneInpsMensileDett
   (
    aRecEstrazioneInpsMensileDett ESTRAZ_INPS_MENS_DETT%ROWTYPE
   ) IS

BEGIN

   INSERT INTO ESTRAZ_INPS_MENS_DETT
          (id_estrazione,
           esercizio,
           mese,
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
           tipo_rapporto,
           codice_attivita,
  	   altra_assic,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec,
           ammontare_cori_dett_ente,
           ammontare_cori_dett_perc,
           codice_comune)
   VALUES (aRecEstrazioneInpsMensileDett.id_estrazione,
           aRecEstrazioneInpsMensileDett.esercizio,
           aRecEstrazioneInpsMensileDett.mese,
           aRecEstrazioneInpsMensileDett.cd_cds_compenso,
           aRecEstrazioneInpsMensileDett.cd_uo_compenso,
           aRecEstrazioneInpsMensileDett.esercizio_compenso,
           aRecEstrazioneInpsMensileDett.pg_compenso,
           aRecEstrazioneInpsMensileDett.esercizio_pagamento,
           aRecEstrazioneInpsMensileDett.fl_senza_calcoli,
           aRecEstrazioneInpsMensileDett.stato_cofi,
           aRecEstrazioneInpsMensileDett.dt_registrazione_compenso,
           aRecEstrazioneInpsMensileDett.dt_cmp_da_compenso,
           aRecEstrazioneInpsMensileDett.dt_cmp_a_compenso,
           aRecEstrazioneInpsMensileDett.dt_emissione_mandato,
           aRecEstrazioneInpsMensileDett.dt_trasmissione_mandato,
           aRecEstrazioneInpsMensileDett.dt_pagamento_mandato,
           aRecEstrazioneInpsMensileDett.ti_anagrafico,
           aRecEstrazioneInpsMensileDett.cd_anag,
           aRecEstrazioneInpsMensileDett.cd_terzo,
           aRecEstrazioneInpsMensileDett.cd_trattamento,
           aRecEstrazioneInpsMensileDett.cd_contributo_ritenuta,
           aRecEstrazioneInpsMensileDett.ds_contributo_ritenuta,
           aRecEstrazioneInpsMensileDett.pg_riga,
           aRecEstrazioneInpsMensileDett.imponibile_cori,
           aRecEstrazioneInpsMensileDett.imponibile_cori_dett,
           aRecEstrazioneInpsMensileDett.aliquota_cori_dett,
           aRecEstrazioneInpsMensileDett.ammontare_cori_dett,
           aRecEstrazioneInpsMensileDett.tipo_rapporto,
           aRecEstrazioneInpsMensileDett.codice_attivita,
           aRecEstrazioneInpsMensileDett.altra_assic,
           aRecEstrazioneInpsMensileDett.dacr,
           aRecEstrazioneInpsMensileDett.utcr,
           aRecEstrazioneInpsMensileDett.duva,
           aRecEstrazioneInpsMensileDett.utuv,
           aRecEstrazioneInpsMensileDett.pg_ver_rec,
           aRecEstrazioneInpsMensileDett.ammontare_cori_dett_ente,
           aRecEstrazioneInpsMensileDett.ammontare_cori_dett_perc,
           aRecEstrazioneInpsMensileDett.codice_comune);

END insEstrazioneInpsMensileDett;

-- =================================================================================================
-- Inserimento record in ESTRAZ_INPS_MENS
-- =================================================================================================

PROCEDURE insEstrazioneInpsMensile
   (
    aRecEstrazioneInpsMensile ESTRAZ_INPS_MENS%Rowtype
   ) IS

BEGIN

   INSERT INTO ESTRAZ_INPS_MENS
          (id_estrazione,
           esercizio,
           mese,
           cd_anag,
           chiave_inps,
           progressivo,
           tipo_rapporto,
           codice_attivita,
           imponibile,
           segno_imponibile,
           aliquota,
           dt_inizio_attivita,
           dt_fine_attivita,
  	   altra_assic,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec,
           contributi_dovuti,
	   segno_contributi_dovuti,
	   contributi_trattenuti,
	   segno_contributi_trattenuti,
	   contributi_versati,
	   segno_contributi_versati,
	   codice_comune)
   VALUES (aRecEstrazioneInpsMensile.id_estrazione,
           aRecEstrazioneInpsMensile.esercizio,
           aRecEstrazioneInpsMensile.mese,
           aRecEstrazioneInpsMensile.cd_anag,
           aRecEstrazioneInpsMensile.chiave_inps,
           aRecEstrazioneInpsMensile.progressivo,
           aRecEstrazioneInpsMensile.tipo_rapporto,
           aRecEstrazioneInpsMensile.codice_attivita,
           aRecEstrazioneInpsMensile.imponibile,
           aRecEstrazioneInpsMensile.segno_imponibile,
           aRecEstrazioneInpsMensile.aliquota,
           aRecEstrazioneInpsMensile.dt_inizio_attivita,
           aRecEstrazioneInpsMensile.dt_fine_attivita,
           aRecEstrazioneInpsMensile.altra_assic,
           aRecEstrazioneInpsMensile.dacr,
           aRecEstrazioneInpsMensile.utcr,
           aRecEstrazioneInpsMensile.duva,
           aRecEstrazioneInpsMensile.utuv,
           aRecEstrazioneInpsMensile.pg_ver_rec,
           aRecEstrazioneInpsMensile.contributi_dovuti,
	   aRecEstrazioneInpsMensile.segno_contributi_dovuti,
	   aRecEstrazioneInpsMensile.contributi_trattenuti,
	   aRecEstrazioneInpsMensile.segno_contributi_trattenuti,
	   aRecEstrazioneInpsMensile.contributi_versati,
	   aRecEstrazioneInpsMensile.segno_contributi_versati,
	   aRecEstrazioneInpsMensile.codice_comune);

END insEstrazioneInpsMensile;

-- =================================================================================================
-- Estrazione dati per file INPS (Inserimento record in ESTRAZIONE_INPS_MENSILE_DETT)
-- =================================================================================================
PROCEDURE inserisciDatiINPSMensileDett
   (
    inEsercizio NUMBER,
    inMese NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   memChiaveCompenso VARCHAR2(500);
   memPgRiga NUMBER(10);
   memTiCoriEPPrec CHAR(1);

   aRecVEstraiDatiInps V_ESTRAI_DATI_INPS%ROWTYPE;
   aRecEstrazioneInpsMensileDett ESTRAZ_INPS_MENS_DETT%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializza variabili

   memChiaveCompenso:=NULL;
   memPgRiga:=NULL;
   memTiCoriEPPrec:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale di lettura record per inserimento in ESTRAZIONE_INPS_MENSILE_DETT

   BEGIN

      IF parametri_tab(2).stringa = CNRCTB575.STATO_LIQUID_CORI_INV THEN

	    --UTILIZZATA SEMPRE V_ESTRAI_DATI_INPS, AGGIUNTA CONDIZIONE SUL MESE
	    Open gen_cur For

                 SELECT *
                 FROM   V_ESTRAI_DATI_INPS
                 WHERE  esercizio_pagamento = inEsercizio And
                        dt_trasmissione_mandato IS Not Null And
			To_Char(dt_trasmissione_mandato,'MM') = inMese
                 ORDER BY cd_cds_compenso,
                          cd_uo_compenso,
                          esercizio_compenso,
                          pg_compenso,
		          pg_riga,
		          ti_ente_percipiente
                 FOR UPDATE OF pg_compenso NOWAIT;
      ELSE

	    Open gen_cur For

                 SELECT *
                 FROM   V_ESTRAI_DATI_INPS
                 WHERE  esercizio_pagamento = inEsercizio And
                        To_Char(dt_emissione_mandato,'MM') = inMese
                 ORDER BY cd_cds_compenso,
                          cd_uo_compenso,
                          esercizio_compenso,
                          pg_compenso,
		          pg_riga,
		          ti_ente_percipiente
                 FOR UPDATE OF pg_compenso NOWAIT;
      END IF;

      LOOP

         FETCH gen_cur INTO
               aRecVEstraiDatiInps;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Prima volta

         IF  memChiaveCompenso IS NULL THEN
             aRecEstrazioneInpsMensileDett:=NULL;
             memChiaveCompenso:=aRecVEstraiDatiInps.chiave_compenso;
             memPgRiga:=aRecVEstraiDatiInps.pg_riga;
             memTiCoriEPPrec:=TICORIPERCIP;
         END IF;

         -- Se il compenso letto è diverso dal precedente o se il progressivo di riga è diverso dal
         -- precedente allora scrivo il compenso in ESTRAZ_INPS_MENS_DETT

         IF (memChiaveCompenso != aRecVEstraiDatiInps.chiave_compenso OR
             memPgRiga != aRecVEstraiDatiInps.pg_riga) THEN
               insEstrazioneInpsMensileDett(aRecEstrazioneInpsMensileDett);
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

         If aRecVEstraiDatiInps.ti_ente_percipiente = TICORIENTE Then

            aRecEstrazioneInpsMensileDett:=NULL;
            aRecEstrazioneInpsMensileDett.id_estrazione:=inRepID;
            aRecEstrazioneInpsMensileDett.esercizio:=inEsercizio;
            aRecEstrazioneInpsMensileDett.mese:=inMese;
            aRecEstrazioneInpsMensileDett.cd_cds_compenso:=aRecVEstraiDatiInps.cd_cds_compenso;
            aRecEstrazioneInpsMensileDett.cd_uo_compenso:=aRecVEstraiDatiInps.cd_uo_compenso;
            aRecEstrazioneInpsMensileDett.esercizio_compenso:=aRecVEstraiDatiInps.esercizio_compenso;
            aRecEstrazioneInpsMensileDett.pg_compenso:=aRecVEstraiDatiInps.pg_compenso;
            aRecEstrazioneInpsMensileDett.esercizio_pagamento:=aRecVEstraiDatiInps.esercizio_pagamento;
            aRecEstrazioneInpsMensileDett.fl_senza_calcoli:=aRecVEstraiDatiInps.fl_senza_calcoli;
            aRecEstrazioneInpsMensileDett.stato_cofi:=aRecVEstraiDatiInps.stato_cofi;
            aRecEstrazioneInpsMensileDett.dt_registrazione_compenso:=aRecVEstraiDatiInps.dt_registrazione_compenso;
            aRecEstrazioneInpsMensileDett.dt_cmp_da_compenso:=TRUNC(aRecVEstraiDatiInps.dt_cmp_da_compenso);
            aRecEstrazioneInpsMensileDett.dt_cmp_a_compenso:=TRUNC(aRecVEstraiDatiInps.dt_cmp_a_compenso);
            aRecEstrazioneInpsMensileDett.dt_emissione_mandato:=TRUNC(aRecVEstraiDatiInps.dt_emissione_mandato);
            aRecEstrazioneInpsMensileDett.dt_trasmissione_mandato:=TRUNC(aRecVEstraiDatiInps.dt_trasmissione_mandato);
            aRecEstrazioneInpsMensileDett.dt_pagamento_mandato:=TRUNC(aRecVEstraiDatiInps.dt_pagamento_mandato);
            aRecEstrazioneInpsMensileDett.ti_anagrafico:=aRecVEstraiDatiInps.ti_anagrafico;
            aRecEstrazioneInpsMensileDett.cd_anag:=aRecVEstraiDatiInps.cd_anag;
            aRecEstrazioneInpsMensileDett.cd_terzo:=aRecVEstraiDatiInps.cd_terzo;
            aRecEstrazioneInpsMensileDett.cd_trattamento:=aRecVEstraiDatiInps.cd_trattamento;
            aRecEstrazioneInpsMensileDett.cd_contributo_ritenuta:=aRecVEstraiDatiInps.cd_contributo_ritenuta;
            aRecEstrazioneInpsMensileDett.ds_contributo_ritenuta:=aRecVEstraiDatiInps.ds_contributo_ritenuta;
            aRecEstrazioneInpsMensileDett.pg_riga:=aRecVEstraiDatiInps.pg_riga;
            aRecEstrazioneInpsMensileDett.imponibile_cori:=aRecVEstraiDatiInps.imponibile;
            If aRecVEstraiDatiInps.pg_riga = 0 Then
               --aRecEstrazioneInpsDett.imponibile_cori_dett:=aRecVEstraiDatiInps.imponibile;
               aRecEstrazioneInpsMensileDett.imponibile_cori_dett:=aRecEstrazioneInpsMensileDett.imponibile_cori;
               aRecEstrazioneInpsMensileDett.aliquota_cori_dett:=aRecVEstraiDatiInps.aliquota;
               aRecEstrazioneInpsMensileDett.ammontare_cori_dett:=aRecVEstraiDatiInps.ammontare;
               aRecEstrazioneInpsMensileDett.ammontare_cori_dett_ente:=aRecVEstraiDatiInps.ammontare;
            Else
               aRecEstrazioneInpsMensileDett.imponibile_cori_dett:=aRecVEstraiDatiInps.imponibile_det;
               aRecEstrazioneInpsMensileDett.aliquota_cori_dett:=aRecVEstraiDatiInps.aliquota_det;
               aRecEstrazioneInpsMensileDett.ammontare_cori_dett:=aRecVEstraiDatiInps.ammontare_det;
               aRecEstrazioneInpsMensileDett.ammontare_cori_dett_ente:=aRecVEstraiDatiInps.ammontare_det;
            End If;
            aRecEstrazioneInpsMensileDett.tipo_rapporto:=aRecVEstraiDatiInps.cd_rapporto_inps;
            aRecEstrazioneInpsMensileDett.codice_attivita:=aRecVEstraiDatiInps.cd_attivita_inps;

            -- Per i vecchi compensi in cui non sono valorizzate queste informazioni,
            -- aggiunti valori fissi per non modificare la base dati
            /* ormai non serve più
            If aRecEstrazioneInpsMensileDett.tipo_rapporto Is Null Then
               If aRecVEstraiDatiInps.cd_tipo_rapporto = 'COLL' Then
                  aRecEstrazioneInpsMensileDett.tipo_rapporto:='11';
                  aRecEstrazioneInpsMensileDett.codice_attivita:='17';
               Else
                  aRecEstrazioneInpsMensileDett.tipo_rapporto:='09';
                  aRecEstrazioneInpsMensileDett.codice_attivita:=Null;
               End If;
            End If;
            */
            --Aggiunto il comune presso il quale il lavoratore svolge la propria attività
            --Se il comune è in Italia, viene inserito il codice catastale
            --Se il comune è estero, viene inserito il codice fisco dello stato estero
            If aRecVEstraiDatiInps.pg_comune_inps Is Not Null Then
               Declare
                 italiano_estero   VARCHAR2(1);
                 cod_com	   VARCHAR2(10);
                 cod_naz	   VARCHAR2(10);
               Begin
                  Select c.ti_italiano_estero, c.cd_catastale, n.cd_catastale
                  Into italiano_estero, cod_com, cod_naz
                  From comune c, nazione n
                  Where c.pg_comune = aRecVEstraiDatiInps.pg_comune_inps
                    And c.pg_nazione = n.pg_nazione;

                  If italiano_estero = 'I' Then
                      aRecEstrazioneInpsMensileDett.codice_comune:=cod_com;
                  Else
                      aRecEstrazioneInpsMensileDett.codice_comune:=cod_naz;
                  End If;
               Exception
                  When No_Data_Found Then
                        aRecEstrazioneInpsMensileDett.codice_comune:=Null;
               End;
            Else
               aRecEstrazioneInpsMensileDett.codice_comune:=Null;
            End If;
            aRecEstrazioneInpsMensileDett.dacr:=dataOdierna;
            aRecEstrazioneInpsMensileDett.utcr:=inUtente;
            aRecEstrazioneInpsMensileDett.duva:=dataOdierna;
            aRecEstrazioneInpsMensileDett.utuv:=inUtente;
            aRecEstrazioneInpsMensileDett.pg_ver_rec:=1;
         Else  --gli imponibili non vanno sommati
            If aRecVEstraiDatiInps.pg_riga = 0 Then
               aRecEstrazioneInpsMensileDett.aliquota_cori_dett:=aRecEstrazioneInpsMensileDett.aliquota_cori_dett +
                                                          aRecVEstraiDatiInps.aliquota;
               aRecEstrazioneInpsMensileDett.ammontare_cori_dett:=aRecEstrazioneInpsMensileDett.ammontare_cori_dett +
                                                           aRecVEstraiDatiInps.ammontare;
	       aRecEstrazioneInpsMensileDett.ammontare_cori_dett_perc:= aRecVEstraiDatiInps.ammontare;
            Else
               aRecEstrazioneInpsMensileDett.aliquota_cori_dett:=aRecEstrazioneInpsMensileDett.aliquota_cori_dett +
                                                          aRecVEstraiDatiInps.aliquota_det;
               aRecEstrazioneInpsMensileDett.ammontare_cori_dett:=aRecEstrazioneInpsMensileDett.ammontare_cori_dett +
                                                           aRecVEstraiDatiInps.ammontare_det;
	       aRecEstrazioneInpsMensileDett.ammontare_cori_dett_perc:= aRecVEstraiDatiInps.ammontare_det;
            End If;
            aRecEstrazioneInpsMensileDett.altra_assic := aRecVEstraiDatiInps.cd_altra_ass_inps;
            -- Per i vecchi compensi in cui non e' valorizzato il campo altre_assic,
            -- aggiunto valore fisso per non modificare la base dati
            -- ma solo per le aliquote ridotte
            If aRecEstrazioneInpsMensileDett.altra_assic Is Null Then
               If (aRecEstrazioneInpsMensileDett.aliquota_cori_dett * 100) < 1780 Then
                  If (aRecEstrazioneInpsMensileDett.aliquota_cori_dett * 100) = 1500 Then
                      aRecEstrazioneInpsMensileDett.altra_assic:='002';
                  Else
                      aRecEstrazioneInpsMensileDett.altra_assic:='201';
                  End If;
               Else
                  aRecEstrazioneInpsMensileDett.altra_assic:=Null;
               End If;
            End If;
            --Per i vecchi compensi in cui c'e' ancora l'aliquota 17,80 e 18,80, forziamo
            --i valori richiesti dall'INPS
            If (aRecEstrazioneInpsMensileDett.aliquota_cori_dett * 100) = 1780 Then
                   aRecEstrazioneInpsMensileDett.aliquota_cori_dett := (1800 / 100);
            End If;
            If (aRecEstrazioneInpsMensileDett.aliquota_cori_dett * 100) = 1880 Then
                   aRecEstrazioneInpsMensileDett.aliquota_cori_dett := (1900 / 100);
            End If;
         End If;

      END LOOP;

      CLOSE gen_cur;

      -- Scrittura ultimo record
      -- ma solo se la fetch ha restituito dei valori
      If memChiaveCompenso Is Not Null Then
          insEstrazioneInpsMensileDett(aRecEstrazioneInpsMensileDett);
      End If;

   END;
END inserisciDatiINPSMensileDett;

-- =================================================================================================
-- Inserimento dati per FILE INPS - Aggregazione dati in ESTRAZ_INPS_MENS
-- =================================================================================================
PROCEDURE aggregaDatiInpsMensile
   (
    inEsercizio NUMBER,
    inMese NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   memCdAnag NUMBER(8);
   memTipoRapporto VARCHAR2(2);
   memAliquota NUMBER;
   memImportoConSegno NUMBER;
   memContribDovutiConSegno  NUMBER;
   memContribTrattenutiConSegno  NUMBER;
   memContribVersatiConSegno  NUMBER;

   aRecEstrazioneINPSMensileDett ESTRAZ_INPS_MENS_DETT%ROWTYPE;
   aRecEstrazioneINPSMensile ESTRAZ_INPS_MENS%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN
   -------------------------------------------------------------------------------------------------
   -- Inizializzazione variabili

   memCdAnag:=NULL;

   BEGIN
         OPEN gen_cur FOR

              SELECT *
              FROM   ESTRAZ_INPS_MENS_DETT
              WHERE  id_estrazione = inRepID AND
                     esercizio = inEsercizio And
                     mese = inMese
              ORDER BY cd_anag,
                       tipo_rapporto,
                       aliquota_cori_dett;
      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneINPSMensileDett;

         EXIT WHEN gen_cur%NOTFOUND;

         -- E' la prima volta o il soggetto anagrafico letto è diverso dal precedente --------------
 	 -- Devo inserire la riga ogni volta che cambia l'anagrafica, il tipo rapporto o l'aliquota

 	 -- Devo costruire il record la prima volta oppure
 	 -- se cambia l'anagrafica, il tipo rapporto o l'aliquota
         If (memCdAnag IS NULL Or
             memCdAnag != aRecEstrazioneINPSMensileDett.cd_anag Or
             memTipoRapporto != aRecEstrazioneINPSMensileDett.tipo_rapporto Or
             memAliquota != aRecEstrazioneINPSMensileDett.aliquota_cori_dett) THEN

	     -- inserisco il record solo se ci sono dati
	     If memCdAnag Is Not Null Then
                  insEstrazioneInpsMensile(aRecEstrazioneInpsMensile);
             End If;
             -- Costruisco il nuovo record

             If (memCdAnag IS Null Or memCdAnag != aRecEstrazioneINPSMensileDett.cd_anag) Then
                 aRecEstrazioneInpsMensile.progressivo:= 1;
             Else
                 aRecEstrazioneInpsMensile.progressivo:=aRecEstrazioneINPSMensile.progressivo + 1;
             End If;

             memCdAnag:=aRecEstrazioneINPSMensileDett.cd_anag;
             memTipoRapporto:=aRecEstrazioneINPSMensileDett.tipo_rapporto;
             memAliquota:=aRecEstrazioneINPSMensileDett.aliquota_cori_dett;
             memImportoConSegno := 0;
             memContribDovutiConSegno :=0;
             memContribTrattenutiConSegno :=0;
             memContribVersatiConSegno :=0;

             aRecEstrazioneINPSMensile.id_estrazione:=aRecEstrazioneINPSMensileDett.id_estrazione;
             aRecEstrazioneINPSMensile.esercizio:=aRecEstrazioneINPSMensileDett.esercizio;
             aRecEstrazioneINPSMensile.mese:=aRecEstrazioneINPSMensileDett.mese;
             aRecEstrazioneINPSMensile.cd_anag:=aRecEstrazioneINPSMensileDett.cd_anag;
             aRecEstrazioneINPSMensile.chiave_inps:='999999999999999';

             aRecEstrazioneINPSMensile.tipo_rapporto:=aRecEstrazioneINPSMensileDett.tipo_rapporto;
             aRecEstrazioneINPSMensile.codice_attivita:=aRecEstrazioneINPSMensileDett.codice_attivita;
             aRecEstrazioneINPSMensile.codice_comune:=aRecEstrazioneINPSMensileDett.codice_comune;
             If Nvl(aRecEstrazioneINPSMensileDett.imponibile_cori_dett,0) >= 0 Then
                 aRecEstrazioneINPSMensile.segno_imponibile:= '+';
             Else
                 aRecEstrazioneINPSMensile.segno_imponibile:= '-';
             End If;
	     aRecEstrazioneINPSMensile.imponibile:=Abs(aRecEstrazioneINPSMensileDett.imponibile_cori_dett);
	     memImportoConSegno := aRecEstrazioneINPSMensileDett.imponibile_cori_dett;
             aRecEstrazioneINPSMensile.aliquota:=aRecEstrazioneINPSMensileDett.aliquota_cori_dett;

             -- Contributi Dovuti
             If Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_ente,0) +
                Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_perc,0)  >= 0 Then
                 aRecEstrazioneINPSMensile.segno_contributi_dovuti:= '+';
             Else
                 aRecEstrazioneINPSMensile.segno_contributi_dovuti:= '-';
             End If;
	     aRecEstrazioneINPSMensile.contributi_dovuti:=Abs(Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_ente,0) +
                					      Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_perc,0));
	     memContribDovutiConSegno := Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_ente,0) +
               			    	 Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_perc,0);

	     -- Contributi Trattenuti
	     If Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_perc,0)  >= 0 Then
                 aRecEstrazioneINPSMensile.segno_contributi_trattenuti:= '+';
             Else
                 aRecEstrazioneINPSMensile.segno_contributi_trattenuti:= '-';
             End If;
	     aRecEstrazioneINPSMensile.contributi_trattenuti:=Abs(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_perc);
	     memContribTrattenutiConSegno := Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_perc,0);

	     -- Contributi Versati sono uguali a quelli Dovuti
	     aRecEstrazioneINPSMensile.segno_contributi_versati := aRecEstrazioneINPSMensile.segno_contributi_dovuti;
	     aRecEstrazioneINPSMensile.contributi_versati := aRecEstrazioneINPSMensile.contributi_dovuti;

             --le due date seguenti vengono aggiornate succesivamente
             aRecEstrazioneINPSMensile.dt_inizio_attivita:=Null;
             aRecEstrazioneINPSMensile.dt_fine_attivita:=Null;
             aRecEstrazioneINPSMensile.altra_assic:=aRecEstrazioneINPSMensileDett.altra_assic;
             aRecEstrazioneInpsMensile.dacr:=dataOdierna;
             aRecEstrazioneInpsMensile.utcr:=inUtente;
             aRecEstrazioneInpsMensile.duva:=dataOdierna;
             aRecEstrazioneInpsMensile.utuv:=inUtente;
             aRecEstrazioneInpsMensile.pg_ver_rec:=1;
         Else
             aRecEstrazioneINPSMensile.imponibile:= memImportoConSegno + aRecEstrazioneINPSMensileDett.imponibile_cori_dett;
             If aRecEstrazioneINPSMensile.imponibile >= 0 Then
                 aRecEstrazioneINPSMensile.segno_imponibile:= '+';
             Else
                 aRecEstrazioneINPSMensile.segno_imponibile:= '-';
             End If;
             memImportoConSegno := aRecEstrazioneINPSMensile.imponibile;
	     aRecEstrazioneINPSMensile.imponibile:=Abs( aRecEstrazioneINPSMensile.imponibile);

	     -- Contributi Dovuti
	     aRecEstrazioneINPSMensile.contributi_dovuti:= memContribDovutiConSegno +
	     						   Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_ente,0) +
                					   Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_perc,0);
             If aRecEstrazioneINPSMensile.contributi_dovuti >= 0 Then
                 aRecEstrazioneINPSMensile.segno_contributi_dovuti:= '+';
             Else
                 aRecEstrazioneINPSMensile.segno_contributi_dovuti:= '-';
             End If;
             memContribDovutiConSegno := aRecEstrazioneINPSMensile.contributi_dovuti;
	     aRecEstrazioneINPSMensile.contributi_dovuti:=Abs( aRecEstrazioneINPSMensile.contributi_dovuti);

	     -- Contributi Trattenuti
	     aRecEstrazioneINPSMensile.contributi_trattenuti:= memContribTrattenutiConSegno +
                					   Nvl(aRecEstrazioneINPSMensileDett.ammontare_cori_dett_perc,0);
             If aRecEstrazioneINPSMensile.contributi_trattenuti >= 0 Then
                 aRecEstrazioneINPSMensile.segno_contributi_trattenuti:= '+';
             Else
                 aRecEstrazioneINPSMensile.segno_contributi_trattenuti:= '-';
             End If;
             memContribTrattenutiConSegno := aRecEstrazioneINPSMensile.contributi_trattenuti;
	     aRecEstrazioneINPSMensile.contributi_trattenuti:=Abs( aRecEstrazioneINPSMensile.contributi_trattenuti);

	     -- Contributi Versati sono uguali a quelli Dovuti
	     aRecEstrazioneINPSMensile.segno_contributi_versati := aRecEstrazioneINPSMensile.segno_contributi_dovuti;
	     aRecEstrazioneINPSMensile.contributi_versati := aRecEstrazioneINPSMensile.contributi_dovuti;

	 End If;

      END LOOP;

      CLOSE gen_cur;

      -- Scrittura ultimo record
      -- ma solo se la fetch ha restituito dei valori
      If memCdAnag IS Not Null Then
          insEstrazioneInpsMensile(aRecEstrazioneInpsMensile);
      End If;
   END;
End aggregaDatiInpsMensile;

-- =================================================================================================
-- Inserimento dati anagrafici in ESTRAZ_INPS_MENS
-- =================================================================================================
PROCEDURE insDatiAnagInpsMensile
   (
    inEsercizio NUMBER,
    inMese NUMBER,
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
   cv_dip_cap_domfisc VARCHAR(20);

   memCdAnag ESTRAZ_INPS_MENS.cd_anag%TYPE;

   aImAddRegRate NUMBER(15,2);
   aImAddComRate NUMBER(15,2);

   aRecEstrazioneINPSMensile ESTRAZ_INPS_MENS%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento dati dei soggetti anagrafici presenti in ESTRAZ_INPS_MENS

   BEGIN

      OPEN gen_cur FOR

           SELECT A.cd_anag,
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
                  DECODE(B.aliquota_fiscale, NULL, 'N', 'Y'),
                  B.cap_comune_fiscale,
                  B.via_fiscale,
                  B.num_civico_fiscale
           FROM   ESTRAZ_INPS_MENS A, ANAGRAFICO B,
                  COMUNE C, COMUNE D
           WHERE  A.id_estrazione = inRepID AND
                  A.esercizio = inEsercizio And
                  A.mese = inMese And
                  B.cd_anag = A.cd_anag AND
                  C.pg_comune = B.pg_comune_nascita AND
                  D.pg_comune = B.pg_comune_fiscale;
      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneINPSMensile.cd_anag,
               cv_ti_entita,
               cv_ti_entita_fisica,
               cv_ti_entita_giuridica,
               cv_cognome,
               cv_nome,
               cv_ragione_sociale,
               aRecEstrazioneINPSMensile.dip_codice_fiscale,
               aRecEstrazioneINPSMensile.dip_sesso,
               aRecEstrazioneINPSMensile.dip_gg_nascita,
               aRecEstrazioneINPSMensile.dip_mm_nascita,
               aRecEstrazioneINPSMensile.dip_aa_nascita,
               aRecEstrazioneINPSMensile.dip_com_nascita,
               aRecEstrazioneINPSMensile.dip_prv_nascita,
               aRecEstrazioneINPSMensile.dip_com_domfisc,
               aRecEstrazioneINPSMensile.dip_prv_domfisc,
               aRecEstrazioneINPSMensile.dip_cod_com_domfisc,
               cv_fl_applic_aliquota_max,
               cv_dip_cap_domfisc,
               cv_via_fiscale,
               cv_num_civico_fiscale;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Aggiornamento dati da ESTRAZ_INPS_MENS_DETT su ESTRAZ_INPS_MENS inclusi dati di testata

         aRecEstrazioneINPSMensile.dip_cognome:=NULL;
         aRecEstrazioneINPSMensile.dip_nome:=NULL;

         IF    cv_ti_entita = 'U' THEN
               aRecEstrazioneINPSMensile.dip_cognome:=cv_ragione_sociale;
         ELSIF cv_ti_entita = 'G' THEN
               aRecEstrazioneINPSMensile.dip_cognome:=cv_ragione_sociale;
         ELSIF cv_ti_entita = 'F' Then
               -- anche per cv_ti_entita_fisica = 'D' deve essere passato il cognome ed il nome
               --IF cv_ti_entita_fisica = 'D' THEN
                  --aRecEstrazioneINPSMensile.dip_cognome:=cv_ragione_sociale;
               --ELSE
                  aRecEstrazioneINPSMensile.dip_cognome:=cv_cognome;
                  aRecEstrazioneINPSMensile.dip_nome:=cv_nome;
               --END IF;
         ELSIF cv_ti_entita = 'D' THEN
               IF cv_ragione_sociale IS NOT NULL THEN
                  aRecEstrazioneINPSMensile.dip_cognome:=cv_ragione_sociale;
               ELSE
                  aRecEstrazioneINPSMensile.dip_cognome:=cv_cognome;
                  aRecEstrazioneINPSMensile.dip_nome:=cv_nome;
               END IF;
         END IF;

         IF cv_num_civico_fiscale IS NOT NULL THEN
            cv_via_fiscale:=cv_via_fiscale || ', ' || cv_num_civico_fiscale;
         END IF;

	 If Length(Nvl(cv_dip_cap_domfisc,'')) = 5 Then
	      aRecEstrazioneINPSMensile.dip_cap_domfisc := cv_dip_cap_domfisc;
	 Else
	      aRecEstrazioneINPSMensile.dip_cap_domfisc := Null;
	 End If;
         -- Normalizzazione estrazioni per congruenza alla dimensione su db

         BEGIN

               UPDATE ESTRAZ_INPS_MENS
               SET    dip_codice_fiscale = aRecEstrazioneINPSMensile.dip_codice_fiscale,
               	      dip_cognome = aRecEstrazioneINPSMensile.dip_cognome,
                      dip_nome = aRecEstrazioneINPSMensile.dip_nome,
                      dip_sesso = aRecEstrazioneINPSMensile.dip_sesso,
                      dip_gg_nascita = aRecEstrazioneINPSMensile.dip_gg_nascita,
                      dip_mm_nascita = aRecEstrazioneINPSMensile.dip_mm_nascita,
                      dip_aa_nascita = aRecEstrazioneINPSMensile.dip_aa_nascita,
                      dip_com_nascita = aRecEstrazioneINPSMensile.dip_com_nascita,
                      dip_prv_nascita = aRecEstrazioneINPSMensile.dip_prv_nascita,
                      dip_cap_domfisc = aRecEstrazioneINPSMensile.dip_cap_domfisc,
                      dip_ind_domfisc = cv_via_fiscale,
                      dip_com_domfisc = aRecEstrazioneINPSMensile.dip_com_domfisc,
                      dip_prv_domfisc = aRecEstrazioneINPSMensile.dip_prv_domfisc,
                      dip_cod_com_domfisc = aRecEstrazioneINPSMensile.dip_cod_com_domfisc
               WHERE  id_estrazione = inRepID AND
                      esercizio = inEsercizio And
                      mese =inMese And
                      cd_anag = aRecEstrazioneINPSMensile.cd_anag;
         END;
      END LOOP;

      CLOSE gen_cur;

   END;

END insDatiAnagInpsMensile;

-- =================================================================================================
-- Aggiornamento del numero di giorni lavorati
-- =================================================================================================
PROCEDURE scriviGiorniLavoro
   (
    inEsercizio NUMBER,
    inMese NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2,
    aCdAnag NUMBER
   ) IS

   aDataMinGenerale DATE;
   aDataMaxGenerale DATE;
   aDataFineAnno DATE;

   i BINARY_INTEGER;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Valorizzazione costanti

   aDataFineAnno:=TO_DATE('3112' || inEsercizio,'DDMMYYYY');

   tabella_date_ok_tutte.DELETE;

   -- Azzeramento tabella date negative per recupero rate se non ci sono intervalli ordinari

   If tabella_date_tutte.COUNT = 0 THEN
      tabella_date_tutte_neg.DELETE;
   END IF;
   -------------------------------------------------------------------------------------------------
   -- Normalizzazione delle date per includere il recupero delle rate ed eliminare periodi di eventuale
   -- sovrapposizione

-- Date tutti i compensi

   IF tabella_date_tutte.COUNT > 0 THEN
      CNRCTB650.scaricaRecuperoRate(tabella_date_tutte,
                                    tabella_date_tutte_neg);
      CNRCTB650.normalizzaMatriceDate(tabella_date_tutte,
                                      tabella_date_ok_tutte);
      CNRCTB650.chkSpuriRecuperoRate(tabella_date_tutte_neg);
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Calcolo della minima e massima data dei compensi ed aggiornamento delle annotazioni (DATE COMPENSI)
   BEGIN

      aDataMinGenerale:=NULL;		  -- devo considerare tutti i compensi (anche a tassazione separata)
      aDataMaxGenerale:=NULL;             -- devo considerare tutti i compensi (anche a tassazione separata)

      IF tabella_date_ok_tutte.COUNT > 0 THEN

         FOR i IN tabella_date_ok_tutte.FIRST .. tabella_date_ok_tutte.LAST

         LOOP

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
         -- 27/09/2012 MD - Eliminata normalizzazione date
         /*
         IF aDataMinGenerale > aDataFineAnno THEN
            aDataMinGenerale:=aDataFineAnno;
         END IF;

         IF aDataMaxGenerale > aDataFineAnno THEN
            aDataMaxGenerale:=aDataFineAnno;
         END IF;
         */

      END IF;

   END;

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento ESTRAZ_INPS_MENS per il periodo di competenza dei compensi

   BEGIN

      UPDATE ESTRAZ_INPS_MENS
      SET    cnr_data_assunzione = aDataMinGenerale,
             dt_inizio_attivita = aDataMinGenerale,
             cnr_data_cessazione = aDataMaxGenerale,
             dt_fine_attivita = aDataMaxGenerale
      WHERE  id_estrazione = inRepID AND
             esercizio = inEsercizio And
             mese = inMese And
             cd_anag = aCdAnag;

   END;

END scriviGiorniLavoro;

-- =================================================================================================
-- Calcolo del numero di giorni di riferimento
-- =================================================================================================
PROCEDURE calcolaGiorniLavoro
   (
    inEsercizio NUMBER,
    inMese NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   dataDa DATE;
   dataA DATE;
   memCdAnag ESTRAZ_INPS_MENS_DETT.cd_anag%TYPE;

   i BINARY_INTEGER;
   k BINARY_INTEGER;

   aRecEstrazioneINPSMensile ESTRAZ_INPS_MENS%ROWTYPE;
   aRecEstrazioneINPSMensileDett ESTRAZ_INPS_MENS_DETT%ROWTYPE;

   gen_cur GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   BEGIN
      memCdAnag:=NULL;

      -- Apertura cursore e determinazione degli intervalli date di lavoro a parità di codice e tipo anagrafico
      OPEN gen_cur FOR

           SELECT A.cd_anag,
                  B.fl_recupero_rate,
                  A.dt_cmp_da_compenso,
                  A.dt_cmp_a_compenso
           FROM   ESTRAZ_INPS_MENS_DETT A,
                  COMPENSO B
           WHERE  A.id_estrazione = inRepID AND
                  A.esercizio = inEsercizio And
                  A.mese = inMese And
                  B.cd_cds = A.cd_cds_compenso AND
                  B.cd_unita_organizzativa = A.cd_uo_compenso AND
                  B.esercizio = A.esercizio_compenso AND
                  B.pg_compenso = A.pg_compenso
           ORDER BY cd_anag;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneINPSMensileDett.cd_anag,
               aRecEstrazioneINPSMensileDett.fl_recupero_rate,
               aRecEstrazioneINPSMensileDett.dt_cmp_da_compenso,
               aRecEstrazioneINPSMensileDett.dt_cmp_a_compenso;

         EXIT WHEN gen_cur%NOTFOUND;

         -- E' la prima volta che leggo un record dalla fetch

         IF memCdAnag IS NULL THEN
            tabella_date_tutte.DELETE;
            tabella_date_tutte_neg.DELETE;

            i:=0;
            memCdAnag:=aRecEstrazioneINPSMensileDett.cd_anag;
         END IF;

         IF memCdAnag != aRecEstrazioneINPSMensileDett.cd_anag THEN

            scriviGiorniLavoro(inEsercizio,
                               inMese,
                               inRepID,
                               inUtente,
                               memCdAnag);

            tabella_date_tutte.DELETE;
            tabella_date_tutte_neg.DELETE;

            i:=0;
            memCdAnag:=aRecEstrazioneINPSMensileDett.cd_anag;

         END IF;

         -- Aggiornamento della tabella di memorizzazione delle date lavorate

            dataDa:= aRecEstrazioneINPSMensileDett.dt_cmp_da_compenso;
            dataA:= aRecEstrazioneINPSMensileDett.dt_cmp_a_compenso;

            IF dataDa IS NOT NULL THEN
               If aRecEstrazioneINPSMensileDett.fl_recupero_rate= 'N' THEN
                     CNRCTB545.componiMatriceDate(tabella_date_tutte,
                                                  dataDa,
                                                  dataA,
                                                  'N',
                                                  'Y');

               Else
                     CNRCTB545.componiMatriceDate(tabella_date_tutte_neg,
                                                  dataDa,
                                                  dataA,
                                                  'N',
                                                  'Y');
               End If;
            END IF;
      END LOOP;

      scriviGiorniLavoro(inEsercizio,
                         inMese,
                         inRepID,
                         inUtente,
                         memCdAnag);

      CLOSE gen_cur;

   END;

END calcolaGiorniLavoro;

-- =================================================================================================
-- Scrittura dei file
-- =================================================================================================
PROCEDURE scriviFileOutput
   (
    inEsercizio NUMBER,
    inMese NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2
   ) IS

   mioCLOB CLOB;
   mioCLOBAnag CLOB;
   aStringaChiave1 VARCHAR2(5);
   aStringaChiave2 VARCHAR2(8);
   aStringaChiave3 VARCHAR2(6);
   aStringa VARCHAR2(1000);
   aDifferenza INTEGER;

   aStringaImporto VARCHAR2(10);

   i BINARY_INTEGER;

   aRecBframeBlob BFRAME_BLOB%ROWTYPE;
   aRecBframeBlobAnag BFRAME_BLOB%ROWTYPE;
   aRecEstrazioneInpsMensile ESTRAZ_INPS_MENS%ROWTYPE;

   gen_cur GenericCurTyp;

   anag NUMBER;
BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazione CLOB per dati Contabili
   aRecBframeBlob.cd_tipo:=IDTIPOBLOB;
   aRecBframeBlob.path:='fileoutINPS/' || inEsercizio || Lpad(inMese,2,'0')|| '/';
   aRecBframeBlob.filename:='FILEOUT_INPS_' || inRepID || '.DAT';
   aRecBframeBlob.ti_visibilita:='U';
   aRecBframeBlob.ds_file:='FILE OUTPUT ESTRAZIONE INPS';
   aRecBframeBlob.ds_utente:='FILE DETTAGLI CONTABILI';

   -- Inizializzazione CLOB per dati Anagrafici
   aRecBframeBlobAnag.cd_tipo:=IDTIPOBLOB;
   aRecBframeBlobAnag.path:='fileoutINPS/' || inEsercizio || Lpad(inMese,2,'0')|| '/';
   aRecBframeBlobAnag.filename:='FILEOUT_INPS_' || inRepID || '_ANAG.DAT';
   aRecBframeBlobAnag.ti_visibilita:='U';
   aRecBframeBlobAnag.ds_file:='FILE OUTPUT ESTRAZIONE INPS';
   aRecBframeBlobAnag.ds_utente:='FILE DETTAGLI ANAGRAFICI';

   IBMUTL005.ShIniCBlob(aRecBframeBlob.cd_tipo,
                        aRecBframeBlob.path,
                        aRecBframeBlob.filename,
                        aRecBframeBlob.ti_visibilita,
                        aRecBframeBlob.ds_file,
                        aRecBframeBlob.ds_utente,
                        inUtente,
                        mioCLOB);

   IBMUTL005.ShIniCBlob(aRecBframeBlobAnag.cd_tipo,
                        aRecBframeBlobAnag.path,
                        aRecBframeBlobAnag.filename,
                        aRecBframeBlobAnag.ti_visibilita,
                        aRecBframeBlobAnag.ds_file,
                        aRecBframeBlobAnag.ds_utente,
                        inUtente,
                        mioCLOBAnag);
   -------------------------------------------------------------------------------------------------
   -- Scrittura CLOB (file output estrazione INPS)

   BEGIN

      -- Valorizzazione costanti della chiave file -------------------------------------------------

      aStringaChiave2:='94109411';

      -- Ciclo elaborazione dati INPS --------------------------------------------------------------

      OPEN gen_cur FOR

           SELECT *
           FROM   ESTRAZ_INPS_MENS
           WHERE  id_estrazione = inRepID AND
                  esercizio = inEsercizio And
                  mese = inMese And
                  Nvl(imponibile,0) > 0 And
                  (aliquota > 0 Or segno_imponibile = '-' or nvl(contributi_versati,0) != 0)
           ORDER BY cd_anag;

      LOOP

         FETCH gen_cur INTO
               aRecEstrazioneInpsMensile;

         EXIT WHEN gen_cur%NOTFOUND;

         -- Creazione di due diversi file (anagrafico e dettaglio)

         FOR i IN 1 .. 2

         LOOP

            aStringa:=NULL;

            -- Dettaglio anagrafico ----------------------------------------------------------------

            IF i = 1 Then
             --il record anagrafico lo metto una sola volta
             If anag Is Null Or aRecEstrazioneInpsMensile.cd_anag != anag Then
               -- Chiave

               aStringaChiave1:='1010 ';
               aStringaChiave3:=LPAD((300000 + aRecEstrazioneInpsMensile.cd_anag),6,'0');
               aStringa:=aStringaChiave1 || aStringaChiave2 || aStringaChiave3;

               -- Cognome

               IF aRecEstrazioneInpsMensile.dip_cognome IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_cognome), 1, 24), 24, ' ');
               END IF;

               -- Nome

               IF aRecEstrazioneInpsMensile.dip_nome IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_nome), 1, 24), 24, ' ');
               END IF;

               -- Data assunzione (data minima di inizio competenza dei compensi elaborati)

               IF aRecEstrazioneInpsMensile.cnr_data_assunzione IS NULL THEN
                  aStringa:=aStringa || LPAD('0', 8, '0');
               ELSE
                  aStringa:=aStringa ||
                            TO_CHAR(aRecEstrazioneInpsMensile.cnr_data_assunzione, 'DD') ||
                            TO_CHAR(aRecEstrazioneInpsMensile.cnr_data_assunzione, 'MM') ||
                            TO_CHAR(aRecEstrazioneInpsMensile.cnr_data_assunzione, 'YYYY');
               END IF;

               -- Codice fiscale

               IF aRecEstrazioneInpsMensile.dip_codice_fiscale IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 16, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_codice_fiscale), 1, 16), 16, ' ');
               END IF;

               -- Data di nascita

               IF (aRecEstrazioneInpsMensile.dip_gg_nascita IS NULL OR
                   aRecEstrazioneInpsMensile.dip_mm_nascita IS NULL OR
                   aRecEstrazioneInpsMensile.dip_aa_nascita IS NULL) THEN
                  aStringa:=aStringa || LPAD('0', 8, '0');
               ELSE
                  aStringa:=aStringa || LPAD(aRecEstrazioneInpsMensile.dip_gg_nascita, 2, '0') ||
                                        LPAD(aRecEstrazioneInpsMensile.dip_mm_nascita, 2, '0') ||
                                        LPAD(aRecEstrazioneInpsMensile.dip_aa_nascita, 4, '0');
               END IF;

               -- Comune di nascita

               IF aRecEstrazioneInpsMensile.dip_com_nascita IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_com_nascita), 1, 24), 24, ' ');
               END IF;

               -- Provincia di nascita

               IF aRecEstrazioneInpsMensile.dip_prv_nascita IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 3, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_prv_nascita), 1, 3), 3, ' ');
               END IF;

               -- Sesso

               IF aRecEstrazioneInpsMensile.dip_sesso IS NULL THEN
                  aStringa:=aStringa || ' ';
               ELSE
                  aStringa:=aStringa || Upper(aRecEstrazioneInpsMensile.dip_sesso);
               END IF;

               -- Provincia di residenza

               IF aRecEstrazioneInpsMensile.dip_prv_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 3, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_prv_domfisc), 1, 3), 3, ' ');
               END IF;

               -- Data cessazione (data massima di fine competenza dei compensi elaborati)

               IF aRecEstrazioneInpsMensile.cnr_data_cessazione IS NULL THEN
                  aStringa:=aStringa || LPAD('0', 8, '0');
               ELSE
                  aStringa:=aStringa ||
                            TO_CHAR(aRecEstrazioneInpsMensile.cnr_data_cessazione, 'DD') ||
                            TO_CHAR(aRecEstrazioneInpsMensile.cnr_data_cessazione, 'MM') ||
                            TO_CHAR(aRecEstrazioneInpsMensile.cnr_data_cessazione, 'YYYY');
               END IF;

               -- Filler

               aStringa:=aStringa || RPAD(' ', 2, ' ');

               -- Indirizzo del domicilio fiscale

               IF aRecEstrazioneInpsMensile.dip_ind_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_ind_domfisc), 1, 24), 24, ' ');
               END IF;

               -- CAP del domicilio fiscale

               IF aRecEstrazioneInpsMensile.dip_cap_domfisc IS NULL THEN
                  aStringa:=aStringa || LPAD('0', 5, '0');
               ELSE
                  IF IBMUTL001.isNumeric(aRecEstrazioneInpsMensile.dip_cap_domfisc) = 'Y' THEN
                     aStringa:=aStringa || LPAD(aRecEstrazioneInpsMensile.dip_cap_domfisc, 5, '0');
                  ELSE
                     aStringa:=aStringa || LPAD('0', 5, '0');
                  END IF;
               END IF;

               -- Provincia del domicilio fiscale

               IF aRecEstrazioneInpsMensile.dip_prv_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 3, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_prv_domfisc), 1, 3), 3, ' ');
               END IF;

               -- Comune del domicilio fiscale

               IF aRecEstrazioneInpsMensile.dip_com_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_com_domfisc), 1, 24), 24, ' ');
               END IF;

               IF aRecEstrazioneInpsMensile.dip_com_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 24, ' ');
               ELSE
                  IF LENGTH(aRecEstrazioneInpsMensile.dip_com_domfisc) > 24 THEN
                     aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_com_domfisc), 25, 24), 24, ' ');
                  ELSE
                     aStringa:=aStringa || RPAD(' ', 24, ' ');
                  END IF;
               END IF;

               -- Qualifica 770/SA

               aStringa:=aStringa || RPAD(' ', 5, ' ');

               -- Codice comune

               IF aRecEstrazioneInpsMensile.dip_cod_com_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 4, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_cod_com_domfisc), 1, 4), 4, ' ');
               END IF;

               -- Provincia lavoro

               aStringa:=aStringa || LPAD('9', 6, '9');

	       -- Indirizzo Domicilio Fiscale completo solo per la stampa delle Etichette
               IF aRecEstrazioneInpsMensile.dip_ind_domfisc IS NULL THEN
                  aStringa:=aStringa || RPAD(' ', 100, ' ');
               ELSE
                  aStringa:=aStringa || RPAD(SUBSTR(Upper(aRecEstrazioneInpsMensile.dip_ind_domfisc), 1, 100), 100, ' ');
               END IF;

               IF LENGTH(aStringa) != 335 THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Errore in lunghezza file in output tipo record 1010 lunghezza ' || LENGTH(aStringa));
               END IF;

                        IBMUTL005.ShPutLine(aRecBframeBlobAnag.cd_tipo,
 		                           aRecBframeBlobAnag.path,
                                           aRecBframeBlobAnag.filename,
                                           mioCLOBAnag,
                                           aStringa);
	     End If;
	     anag := aRecEstrazioneInpsMensile.cd_anag;
            -- Dettaglio importi -------------------------------------------------------------------
            ELSE
               -- Chiave

               aStringaChiave1:='50';
               aStringaChiave3:=LPAD((300000 + aRecEstrazioneInpsMensile.cd_anag),6,'0');
               aStringa:=aStringaChiave2 || aStringaChiave3;

               -- Campi contabili

               aStringa:=aStringa ||
                         LPAD(inEsercizio, 4, '0') ||
                         LPAD(inMese, 2, '0') ||
                         LPAD(aRecEstrazioneInpsMensile.chiave_inps, 15, '9') ||
                         LPAD('9', 2, '9') ||
                         aStringaChiave1 ||
                         LPAD(aRecEstrazioneInpsMensile.progressivo, 2, '0') ||
                         Rpad(Nvl(aRecEstrazioneInpsMensile.tipo_rapporto,' '),2,' ') ||
                         Rpad(Nvl(aRecEstrazioneInpsMensile.codice_attivita,'0'),2,'0') ||
                         Lpad((aRecEstrazioneInpsMensile.imponibile * 100), 9, '0') ||
                         Nvl(aRecEstrazioneInpsMensile.segno_imponibile, ' ') ||
                         Lpad((aRecEstrazioneInpsMensile.aliquota * 100), 4, '0') ||
                         Lpad(To_Char(aRecEstrazioneInpsMensile.dt_inizio_attivita,'ddmmyyyy'), 8, '0') ||
                         Lpad(To_Char(aRecEstrazioneInpsMensile.dt_fine_attivita,'ddmmyyyy'), 8, '0') ||
			 Lpad('0', 9, '0') ||  --importo agevolazione
                         ' ' ||                --segno importo agevolazione
                         Lpad('0', 2, '0') ||  --tipo agevolazione
                         Lpad('0', 2, '0') ||  --codice calamita' naturale
                         Lpad(Nvl(aRecEstrazioneInpsMensile.altra_assic,'0'),3,'0') ||
                         Lpad('0', 3, '0') ||  --codice certificazione
                         Lpad((aRecEstrazioneInpsMensile.contributi_dovuti * 100), 9, '0') ||
                         Nvl(aRecEstrazioneInpsMensile.segno_contributi_dovuti, ' ') ||
                         Lpad((aRecEstrazioneInpsMensile.contributi_trattenuti * 100), 9, '0') ||
                         Nvl(aRecEstrazioneInpsMensile.segno_contributi_trattenuti, ' ') ||
                         Lpad((aRecEstrazioneInpsMensile.contributi_versati * 100), 9, '0') ||
                         Nvl(aRecEstrazioneInpsMensile.segno_contributi_versati, ' ') ||
                         Rpad(Substr(Nvl(aRecEstrazioneInpsMensile.codice_comune,' '),1,4),4,' ') ||
                         Rpad(' ', 119, ' ') ||  --spazio
                         'N' ||
                         'I';

               IF LENGTH(aStringa) != 250 THEN
                  IBMERR001.RAISE_ERR_GENERICO
                     ('Errore in lunghezza file in output tipo record 50 lunghezza ' || LENGTH(aStringa));
               END IF;
                       IBMUTL005.ShPutLine(aRecBframeBlob.cd_tipo,
 		                           aRecBframeBlob.path,
                                           aRecBframeBlob.filename,
                                           mioCLOB,
                                           aStringa);
            END IF;

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
   IBMUTL005.ShCloseClob(aRecBframeBlobAnag.cd_tipo,
 		   	 aRecBframeBlobAnag.path,
                         aRecBframeBlobAnag.filename,
			 mioCLOBAnag);

END scriviFileOutput;

-- =================================================================================================
-- Estrazione dati per stampa INPS mensile
-- =================================================================================================
PROCEDURE estrazioneDatiINPSMensile
   (
    inEsercizio NUMBER,
    inMese NUMBER,
    inRepID INTEGER,
    inUtente VARCHAR2,
    pg_exec NUMBER
   ) IS

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazione variabili

   dataOdierna:=sysdate;

   -------------------------------------------------------------------------------------------------
   -- Inserimento in ESTRAZIONE_INPS_MENSILE_DETT dei compensi oggetto di esposizione nel GLA. Caricamento
   -- dei dati relativi alla testata del compenso

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS mensile - Dettaglio elaborazione',
                    'FASE 1',
                    'FASE 1 - Inserimento compensi in ESTRAZ_INPS_MENS_DETT');

   inserisciDatiINPSMensileDett(inEsercizio,
                        inMese,
                        inRepID,
                        inUtente);

   COMMIT;
   -------------------------------------------------------------------------------------------------
   -- Aggregazione dati di ESTRAZ_INPS_MENS_DETT in ESTRAZ_INPS_MENS.

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS mensile - Dettaglio elaborazione',
                    'FASE 2',
                    'FASE 2 - Aggregazione dati di ESTRAZ_INPS_MENS_DETT in ESTRAZ_INPS_MENS');

   aggregaDatiInpsMensile(inEsercizio,
   			  inMese,
                  	  inRepID,
                  	  inUtente);

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento dati anagrafici.

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS mensile - Dettaglio elaborazione',
                    'FASE 3',
                    'FASE 3 - Aggiornamento dati anagrafici in ESTRAZ_INPS_MENS');

   insDatiAnagInpsMensile(inEsercizio,
   			  inMese,
                  	  inRepID);

  -------------------------------------------------------------------------------------------------
   -- Calcolo del periodo di attivita'

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS mensile - Dettaglio elaborazione',
                    'FASE 4',
                    'FASE 4 - Calcolo giorni lavoro');

   calcolaGiorniLavoro(inEsercizio,
   	               inMese,
                       inRepID,
                       inUtente);

   -------------------------------------------------------------------------------------------------
   -- Scrittura dei due file INPS (anagrafico e contabile)

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS mensile - Dettaglio elaborazione',
                    'FASE 5',
                    'FASE 5 - creazione CLOB (file output)');

   scriviFileOutput(inEsercizio,
   		    inMese,
                    inRepID,
                    inUtente);

   IBMUTL200.LOGINF(pg_exec,
                    'Estrazione dati INPS mensile - Dettaglio elaborazione',
                    'FASE 6',
                    'FASE 6 - Fine caricamento');

END estrazioneDatiINPSMensile;

-- =================================================================================================
-- Lancio estrazione INPS mensile
-- =================================================================================================
PROCEDURE estrazioneINPSMensileInterna
   (
    inEsercizio NUMBER,
    inMese NUMBER,
    inRepID INTEGER,
    inMsgError IN OUT VARCHAR2,
    inUtente VARCHAR2,
    pg_exec NUMBER
   ) IS

Begin

   -------------------------------------------------------------------------------------------------
   -- Controllo congruenza parametri in input

   chkParametriInput(inEsercizio,
                     inMese,
                     inRepID,
                     inUtente);

   -- Valorizzazione parametri utilizzati nell'estrazione
   valorizzaParametri(inEsercizio);

   -------------------------------------------------------------------------------------------------
   -- Estrazione dati per INPS

   estrazioneDatiINPSMensile(inEsercizio,
                     inMese,
                     inRepID,
                     inUtente,
                     pg_exec);


END  estrazioneINPSMensileInterna;

-- =================================================================================================
-- Guscio per gestione estrazione INPS mensile in batch
-- =================================================================================================
PROCEDURE job_estrazioneINPSmensile
   (
    job NUMBER,
    pg_exec NUMBER,
    next_date DATE,
    inEsercizio NUMBER,
    inMese NUMBER,
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
                                  'Estrazione dati INPS mensile. Start:' || TO_CHAR(sysdate,'YYYY/MM/DD HH-MI-SS'));

   BEGIN

      estrazioneINPSMensileInterna(inEsercizio,
                           inMese,
                           inRepID,
                           aStringa,
                           inUtente,
                           pg_exec);

      COMMIT;

      -- Messaggio di operazione completata ad utente

      IBMUTL205.LOGINF('Estrazione dati INPS mensile',
                       'Estrazione dati INPS mensile ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS'),
                       'Operazione completata con successo',
                       inUtente);

   EXCEPTION

      WHEN others THEN
           ROLLBACK;

      -- Messaggio di attenzione ad utente

      IBMUTL205.LOGWAR('Estrazione dati INPS mensile ' || errori_tab.COUNT,
                       'Estrazione dati INPS mensile ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                       '(pg_exec = ' || pg_exec || ')', DBMS_UTILITY.FORMAT_ERROR_STACK, inUtente);

      -- Scrittura degli eventuali altri errori

      IF errori_tab.COUNT > 0 THEN

         FOR i IN errori_tab.FIRST .. errori_tab.LAST

         LOOP

            IBMUTL200.LOGWAR(pg_exec,
                             'Estrazione dati INPS mensile - Dettaglio errori',
                             'Errore : ' || errori_tab(i).tStringaErr,
                             'Identificativo = ' || errori_tab(i).tStringaKey);

         END LOOP;

      END IF;

   END;

END job_estrazioneINPSmensile;

-- =================================================================================================
-- Main estrazione INPS Mensile Standard
-- =================================================================================================
PROCEDURE estrazioneINPSmensile
   (
    inEsercizio NUMBER,
    inMese      NUMBER,
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
   -- Attivazione della gestione batch per estrazione INPS Mensile

   aProcedure:='CNRCTB925.job_estrazioneINPSmensile(job, ' ||
                                            'pg_exec, ' ||
                                            'next_date, ' ||
                                            inEsercizio || ',''' ||
                                            inMese || ''',' ||
                                            inRepID || ',''' ||
                                            aStringa || ''',' ||
                                            '''' || inUtente || ''');';

   IBMUTL210.CREABATCHDINAMICO('Estrazione dati INPS Mensile',
                               aProcedure,
                               inUtente);

   IBMUTL001.deferred_commit;

   IBMERR001.RAISE_ERR_GENERICO
      ('Operazione sottomessa per esecuzione. Al completamento l''utente riceverà un messaggio di notifica ' ||
       'dello stato dell''operazione');


END estrazioneINPSmensile;

-- =================================================================================================

END; -- PACKAGE END;
