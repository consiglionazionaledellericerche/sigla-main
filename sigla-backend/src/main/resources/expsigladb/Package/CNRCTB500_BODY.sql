--------------------------------------------------------
--  DDL for Package Body CNRCTB500
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB500" AS

--==================================================================================================
-- Ritorna un record della tabella MISSIONE eventualmente con lock
--==================================================================================================
FUNCTION getMissione
   (
    aCdCds MISSIONE.cd_cds%TYPE,
    aCdUo MISSIONE.cd_unita_organizzativa%TYPE,
    aEsercizio MISSIONE.esercizio%TYPE,
    aPgMissione MISSIONE.pg_missione%TYPE,
    eseguiLock CHAR
   ) RETURN MISSIONE%ROWTYPE IS
   aRecMissione MISSIONE%ROWTYPE;

BEGIN

    IF eseguiLock = 'Y' THEN

       SELECT * INTO aRecMissione
       FROM   MISSIONE
       WHERE  cd_cds = aCdCds AND
              cd_unita_organizzativa = aCdUo AND
              esercizio = aEsercizio AND
              pg_missione = aPgMissione
       FOR UPDATE NOWAIT;

   ELSE

       SELECT * INTO aRecMissione
       FROM   MISSIONE
       WHERE  cd_cds = aCdCds AND
              cd_unita_organizzativa = aCdUo AND
              esercizio = aEsercizio AND
              pg_missione = aPgMissione;

   END IF;

   RETURN aRecMissione;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
                  ('Missione U.O. ' || aCdUo || ' NUMERO ' || aEsercizio || '/' || aPgMissione ||
                   ' non trovata');

END getMissione;


--==================================================================================================
-- Ritorna un record della tabella MISSIONE_DIARIA
--==================================================================================================
FUNCTION getMissioneDiaria
   (
    aRecMissioneTappa MISSIONE_TAPPA%ROWTYPE,
    aCdGruppoInquadramento VARCHAR2,
    aDataRif DATE
   ) RETURN MISSIONE_DIARIA%ROWTYPE IS
   aRecMissioneDiaria MISSIONE_DIARIA%ROWTYPE;
   divisaDefault VARCHAR2(100);
   operazioneCambio CHAR;
   aCambio NUMBER(15,4);

BEGIN
	BEGIN
	    SELECT * INTO aRecMissioneDiaria
	    FROM   MISSIONE_DIARIA
	    WHERE  pg_nazione = aRecMissioneTappa.pg_nazione AND
	           cd_gruppo_inquadramento = aCdGruppoInquadramento AND
	           dt_inizio_validita <= aDataRif AND
	           dt_fine_validita >= aDataRif;

	    EXCEPTION
	    WHEN no_data_found THEN
	        IBMERR001.RAISE_ERR_GENERICO
	                  ('DIARIA per nazione ' || aRecMissioneTappa.pg_nazione ||
	                   ' gruppo ministeriale di inquadramento ' || aCdGruppoInquadramento ||
	                   ' data riferimento ' || TO_CHAR(aDataRif, 'DD/MM/YYYY') ||
					   ' non trovata');
	END;

	--
	-- Se l'importo della diaria e' in valuta straniera lo converto nella valuta corrente
	--

	-- Ricerco la valuta corrente
    BEGIN
	    SELECT cd_chiave_secondaria into divisaDefault
	    FROM   CONFIGURAZIONE_CNR
	    WHERE  esercizio = 0 AND
	           cd_unita_funzionale = '*' AND
			   cd_chiave_primaria = 'CD_DIVISA';

		EXCEPTION
	    WHEN no_data_found THEN
	        IBMERR001.RAISE_ERR_GENERICO('Impossibile recurerare la divisa di default!');
	END;

	-- Ricorda che : CD_DIVISA (della diaria) = CD_DIVISA_TAPPA (della tappa)

	-- L'importo della diaria e' gia' nella valuta corrente
	if(aRecMissioneDiaria.cd_divisa = divisaDefault) then
	    return aRecMissioneDiaria;
	end if;

    -- Ricerco la valuta con cui e' espresso l'importo della diaria
	BEGIN
	    SELECT fl_calcola_con_diviso INTO operazioneCambio
	    FROM   DIVISA
	    WHERE  cd_divisa = aRecMissioneDiaria.cd_divisa AND
	           dt_cancellazione IS NULL;

		EXCEPTION
	    WHEN no_data_found THEN
	        IBMERR001.RAISE_ERR_GENERICO
	                  ('Impossibile trovare la divisa della diaria ' || aRecMissioneDiaria.cd_divisa ||
					   ' nella tabella DIVISA');
	END;

    -- Converto l'importo della diaria nella valuta corrente
	if(operazioneCambio = 'Y') then
		aRecMissioneDiaria.im_diaria := aRecMissioneDiaria.im_diaria / aRecMissioneTappa.cambio_tappa;
	else
		aRecMissioneDiaria.im_diaria := aRecMissioneDiaria.im_diaria * aRecMissioneTappa.cambio_tappa;
	end if;

  RETURN aRecMissioneDiaria;

END getMissioneDiaria;

--==================================================================================================
-- Ritorna un record della tabella MISSIONE_QUOTA_ESENTE
--==================================================================================================
FUNCTION getMissioneQuotaEsente
   (
    aTiItaliaEstero CHAR,
    aDataRif DATE
   ) RETURN MISSIONE_QUOTA_ESENTE%ROWTYPE IS
   aRecMissioneQuotaEsente MISSIONE_QUOTA_ESENTE%ROWTYPE;

BEGIN

    SELECT * INTO aRecMissioneQuotaEsente
    FROM   MISSIONE_QUOTA_ESENTE
    WHERE  ti_italia_estero = aTiItaliaEstero AND
           dt_inizio_validita <= aDataRif AND
           dt_fine_validita >= aDataRif;

   RETURN aRecMissioneQuotaEsente;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO
                  ('QUOTA ESENTE per area geografica ' ||  aTiItaliaEstero ||
                   ' data riferimento ' || TO_CHAR(aDataRif, 'DD/MM/YYYY') ||
                   ' non trovata');

END getMissioneQuotaEsente;

--==================================================================================================
-- Ritorna un record della tabella MISSIONE_QUOTA_RIMBORSO
--==================================================================================================
FUNCTION getMissioneQuotaRimborso
   (
    aRecMissioneTappa MISSIONE_TAPPA%ROWTYPE,
    aCdGruppoInquadramento VARCHAR2,
    aCdAreaEstera VARCHAR2,
    aDataRif DATE
   ) RETURN MISSIONE_QUOTA_RIMBORSO%ROWTYPE IS

   aRecMissioneQuotaRimborso MISSIONE_QUOTA_RIMBORSO%ROWTYPE;
   divisaDefault VARCHAR2(100);
   operazioneCambio CHAR;
   aCambio NUMBER(15,4);

BEGIN
	BEGIN
	    SELECT * INTO aRecMissioneQuotaRimborso
	    FROM   MISSIONE_QUOTA_RIMBORSO
	    WHERE  cd_area_estera = aCdAreaEstera AND
	           cd_gruppo_inquadramento = aCdGruppoInquadramento AND
	           dt_inizio_validita <= aDataRif AND
	           dt_fine_validita >= aDataRif;

	    EXCEPTION
	    WHEN no_data_found THEN
	        IBMERR001.RAISE_ERR_GENERICO
	                  ('QUOTA RIMBORSO per area estera ' || aCdAreaEstera ||
	                   ' gruppo di inquadramento ' || aCdGruppoInquadramento ||
	                   ' data riferimento ' || TO_CHAR(aDataRif, 'DD/MM/YYYY') ||
					   ' non trovata');
	END;

  RETURN aRecMissioneQuotaRimborso;

END getMissioneQuotaRimborso;

--==================================================================================================
-- Calcola ore di una tappa
--==================================================================================================
FUNCTION calcolaOreTappa
   (
    aDataInizio DATE,
    aDataFine DATE
   ) RETURN NUMBER IS
   aNumeroOre NUMBER(4,2);
   oreInizio INTEGER;
   oreFine INTEGER;
   minutiInizio INTEGER;
   minutiFine INTEGER;


BEGIN

   aNumeroOre:=0;

   BEGIN

      -- La tappa è di 24 ore

      IF (aDataFine - aDataInizio) = 1 THEN
         aNumeroOre:=24;

      -- Controllo se la tappa ha data inizio e fine nello stesso giorno

      ELSE

         IF    TO_NUMBER(TO_CHAR(aDataInizio,'MI')) < 16 THEN
               oreInizio:=TO_NUMBER(TO_CHAR(aDataInizio,'HH24'));
         ELSIF TO_NUMBER(TO_CHAR(aDataInizio,'MI')) > 46 THEN
               oreInizio:=(TO_NUMBER(TO_CHAR(aDataInizio,'HH24')) + 1);
         ELSE  oreInizio:=(TO_NUMBER(TO_CHAR(aDataInizio,'HH24')) + 0.5);
         END IF;

         IF    TO_NUMBER(TO_CHAR(aDataFine,'MI')) < 16 THEN
               oreFine:=TO_NUMBER(TO_CHAR(aDataFine,'HH24'));
         ELSIF TO_NUMBER(TO_CHAR(aDataFine,'MI')) > 46 THEN
               oreFine:=(TO_NUMBER(TO_CHAR(aDataFine,'HH24')) + 1);
         ELSE  oreFine:=(TO_NUMBER(TO_CHAR(aDataFine,'HH24')) + 0.5);
         END IF;

         IF (TRUNC(aDataInizio) = TRUNC(aDataFine)) THEN
            aNumeroOre:=oreFine - oreInizio;
         ELSE
            aNumeroOre:=24 - oreInizio + oreFine;
         END IF;

      END IF;

   END;

   RETURN aNUmeroOre;

END calcolaOreTappa;

--==================================================================================================
-- Calcola minuti di una tappa per verificare se è consentito il trattamento alternativo
--==================================================================================================
FUNCTION calcolaMinutiTappa
   (
    aDataInizio DATE,
    aDataFine DATE
   ) RETURN NUMBER IS

   aNumeroMinuti NUMBER(6);
   minutiInizio INTEGER;
   minutiFine INTEGER;


BEGIN

   aNumeroMinuti:=0;

   BEGIN

         minutiInizio:=(TO_NUMBER(TO_CHAR(aDataInizio,'HH24')) * 60) + TO_NUMBER(TO_CHAR(aDataInizio,'MI'));
         minutiFine:=(TO_NUMBER(TO_CHAR(aDataFine,'HH24')) * 60) + TO_NUMBER(TO_CHAR(aDataFine,'MI'));

         IF (TRUNC(aDataInizio) = TRUNC(aDataFine)) THEN
            aNumeroMinuti:=minutiFine - minutiInizio;
         ELSE
         		aNumeroMinuti:=1440 - minutiInizio + minutiFine;
         END IF;

   END;
   RETURN aNumeroMinuti;

END calcolaMinutiTappa;

--==================================================================================================
-- Cancellazione di tutte le righe, di tipo diaria o spesa, per una data missione
--==================================================================================================
PROCEDURE delMissioneDettaglio
   (
    aCdCds VARCHAR2,
    aCdUnitaOrganizzativa VARCHAR2,
    aEsercizio NUMBER,
    aPgMissione NUMBER,
    aTipoRiga MISSIONE_DETTAGLIO.ti_spesa_diaria%TYPE
   )IS

BEGIN

   DELETE FROM MISSIONE_DETTAGLIO
   WHERE  cd_cds = aCdCds AND
          cd_unita_organizzativa = aCdUnitaOrganizzativa AND
          esercizio = aEsercizio AND
          pg_missione = aPgMissione AND
          ti_spesa_diaria = aTipoRiga;

END delMissioneDettaglio;


--==================================================================================================
-- Inserimento righe in MISSIONE_DETTAGLIO
--==================================================================================================
PROCEDURE insMissioneDettaglio
   (
    aRecMissioneDettaglio MISSIONE_DETTAGLIO%ROWTYPE
   )IS

BEGIN

   INSERT INTO MISSIONE_DETTAGLIO
          (cd_cds,
           esercizio,
           cd_unita_organizzativa,
           pg_missione,
           dt_inizio_tappa,
           pg_riga,
           ti_spesa_diaria,
           cd_ti_spesa,
           ds_ti_spesa,
           fl_spesa_anticipata,
           ds_spesa,
           localita_spostamento,
           id_giustificativo,
           ds_giustificativo,
           ds_no_giustificativo,
           cd_ti_pasto,
           im_spesa_divisa,
           cd_divisa_spesa,
           cambio_spesa,
           im_spesa_euro,
           im_spesa_max_divisa,
           im_spesa_max,
           im_base_maggiorazione,
           percentuale_maggiorazione,
           im_maggiorazione,
           ti_auto,
           indennita_chilometrica,
           chilometri,
           im_totale_spesa,
           fl_diaria_manuale,
           im_diaria_lorda,
           im_diaria_netto,
           im_quota_esente,
           utcr,
           dacr,
           utuv,
           duva,
           pg_ver_rec,
           ti_cd_ti_spesa,
           im_maggiorazione_euro,
           im_rimborso)
   VALUES (aRecMissioneDettaglio.cd_cds,
           aRecMissioneDettaglio.esercizio,
           aRecMissioneDettaglio.cd_unita_organizzativa,
           aRecMissioneDettaglio.pg_missione,
           aRecMissioneDettaglio.dt_inizio_tappa,
           aRecMissioneDettaglio.pg_riga,
           aRecMissioneDettaglio.ti_spesa_diaria,
           aRecMissioneDettaglio.cd_ti_spesa,
           aRecMissioneDettaglio.ds_ti_spesa,
           aRecMissioneDettaglio.fl_spesa_anticipata,
           aRecMissioneDettaglio.ds_spesa,
           aRecMissioneDettaglio.localita_spostamento,
           aRecMissioneDettaglio.id_giustificativo,
           aRecMissioneDettaglio.ds_giustificativo,
           aRecMissioneDettaglio.ds_no_giustificativo,
           aRecMissioneDettaglio.cd_ti_pasto,
           aRecMissioneDettaglio.im_spesa_divisa,
           aRecMissioneDettaglio.cd_divisa_spesa,
           aRecMissioneDettaglio.cambio_spesa,
           aRecMissioneDettaglio.im_spesa_euro,
           aRecMissioneDettaglio.im_spesa_max_divisa,
           aRecMissioneDettaglio.im_spesa_max,
           aRecMissioneDettaglio.im_base_maggiorazione,
           aRecMissioneDettaglio.percentuale_maggiorazione,
           aRecMissioneDettaglio.im_maggiorazione,
           aRecMissioneDettaglio.ti_auto,
           aRecMissioneDettaglio.indennita_chilometrica,
           aRecMissioneDettaglio.chilometri,
           aRecMissioneDettaglio.im_totale_spesa,
           aRecMissioneDettaglio.fl_diaria_manuale,
           aRecMissioneDettaglio.im_diaria_lorda,
           aRecMissioneDettaglio.im_diaria_netto,
           aRecMissioneDettaglio.im_quota_esente,
           aRecMissioneDettaglio.utcr,
           aRecMissioneDettaglio.dacr,
           aRecMissioneDettaglio.utuv,
           aRecMissioneDettaglio.duva,
           aRecMissioneDettaglio.pg_ver_rec,
           aRecMissioneDettaglio.ti_cd_ti_spesa,
           aRecMissioneDettaglio.im_maggiorazione_euro,
           aRecMissioneDettaglio.im_rimborso);

END insMissioneDettaglio;


--==================================================================================================
-- Estrazione dati da tabelle trascodifica per missione
--==================================================================================================
FUNCTION getFirstTabMissione
   (
    idTabella VARCHAR2,
    aCodice VARCHAR2,
    tiAreaGeografica VARCHAR2,
    pgNazione NUMBER,
    pgRifInquadramento NUMBER,
    aData  DATE
   )
   RETURN VARCHAR2 IS
   retCode VARCHAR2(200);
   i BINARY_INTEGER;
   gen_cv GenericCurTyp;

BEGIN

   i:=0;

   BEGIN

      -- Tabella MISSIONE_TIPO_SPESA

      IF    idTabella = '01' THEN

            OPEN gen_cv FOR

                 SELECT cd_ti_spesa || ti_area_geografica || pg_nazione || pg_rif_inquadramento ||
                        TO_CHAR(dt_inizio_validita,'DDMMYYYY') || TO_CHAR(dt_fine_validita,'DDMMYYYY')
                 FROM   MISSIONE_TIPO_SPESA
                 WHERE  cd_ti_spesa = aCodice AND
						dt_cancellazione IS NULL AND
                        dt_inizio_validita <= aData AND
                        dt_fine_validita >= aData AND
                        (
                         ti_area_geografica = tiAreaGeografica OR
                         ti_area_geografica = '*'
                        ) AND
                        (
                         pg_nazione = pgNazione OR
                         pg_nazione = 0
                        ) AND
                        (
                         pg_rif_inquadramento = pgRifInquadramento OR
                         pg_rif_inquadramento = 0
                        )
                 ORDER BY pg_rif_inquadramento desc,
                          pg_nazione desc,
                          ti_area_geografica desc;

      -- Tabella MISSIONE_TIPO_PASTO

      ELSIF idTabella = '02' THEN

            OPEN gen_cv FOR

                 SELECT cd_ti_pasto || ti_area_geografica || pg_nazione || pg_rif_inquadramento ||
                        TO_CHAR(dt_inizio_validita,'DDMMYYYY') || TO_CHAR(dt_fine_validita,'DDMMYYYY')
                 FROM   MISSIONE_TIPO_PASTO
                 WHERE  cd_ti_pasto = aCodice AND
						dt_cancellazione IS NULL AND
                        dt_inizio_validita <= aData AND
                        dt_fine_validita >= aData AND
                        (
                         ti_area_geografica = tiAreaGeografica OR
                         ti_area_geografica = '*'
                        ) AND
                        (
                         pg_nazione = pgNazione OR
                         pg_nazione = 0
                        ) AND
                        (
                         pg_rif_inquadramento = pgRifInquadramento OR
                         pg_rif_inquadramento = 0
                        )
                 ORDER BY pg_rif_inquadramento desc,
                          pg_nazione desc,
                          ti_area_geografica desc;

      -- Tabella MISSIONE_RIMBORSO_KM

      ELSIF idTabella = '03' THEN

            OPEN gen_cv FOR

                 SELECT ti_auto || ti_area_geografica || pg_nazione ||
                        TO_CHAR(dt_inizio_validita,'DDMMYYYY') || TO_CHAR(dt_fine_validita,'DDMMYYYY')
                 FROM   MISSIONE_RIMBORSO_KM
                 WHERE  ti_auto = aCodice AND
						dt_cancellazione IS NULL AND
                        dt_inizio_validita <= aData AND
                        dt_fine_validita >= aData AND
                        (
                         ti_area_geografica = tiAreaGeografica OR
                         ti_area_geografica = '*'
                        ) AND
                        (
                         pg_nazione = pgNazione OR
                         pg_nazione = 0
                        )
                 ORDER BY pg_nazione desc,
                          ti_area_geografica desc;

      END IF;

      LOOP

         FETCH gen_cv INTO retCode;

         EXIT WHEN gen_cv%NOTFOUND;

         i:=i + 1;

         IF i = 1 THEN
            CLOSE gen_cv;
            RETURN retCode;
         END IF;

      END LOOP;

      CLOSE gen_cv;

      RETURN retCode;

   EXCEPTION

      WHEN no_data_found THEN
           RETURN NULL;

   END;

END getFirstTabMissione;

END;
