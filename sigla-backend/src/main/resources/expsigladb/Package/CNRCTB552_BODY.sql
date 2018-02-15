--------------------------------------------------------
--  DDL for Package Body CNRCTB552
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB552" AS

-- ==================================================================================================
-- Ritorna i record di RATEIZZA_CLASSIFIC_CORI per addizionali territorio
-- ==================================================================================================
PROCEDURE getRateizzaAddTerritorio
   (
    aEsercizio NUMBER,
    aCdAnag NUMBER,
    eseguiLock CHAR,
    aRecRateizzaClassificCoriC0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
    aRecRateizzaClassificCoriP0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
    aRecRateizzaClassificCoriR0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE
   ) IS

   isTemporaneo CHAR(1);
   aCdClassificazioneCoriBase CLASSIFICAZIONE_CORI.cd_classificazione_cori%TYPE;
   j BINARY_INTEGER;

   aRecRateizzaClassificCoriBase RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

BEGIN

   isTemporaneo:='N';

   FOR j IN 1 .. 3

   LOOP

      IF    j = 1 THEN
            aCdClassificazioneCoriBase:=CNRCTB545.isCoriAddCom;
      ELSIF j = 2 THEN
            aCdClassificazioneCoriBase:=CNRCTB545.isCoriAddPro;
      ELSIF j = 3 THEN
            aCdClassificazioneCoriBase:=CNRCTB545.isCoriAddReg;
      END IF;

      aRecRateizzaClassificCoriBase:=CNRCTB545.getRateizzaClassificCori(aEsercizio,
                                                                        aCdAnag,
                                                                        isTemporaneo,
                                                                        aCdClassificazioneCoriBase,
                                                                        eseguiLock);

      IF    j = 1 THEN
            aRecRateizzaClassificCoriC0:=aRecRateizzaClassificCoriBase;
      ELSIF j = 2 THEN
            aRecRateizzaClassificCoriP0:=aRecRateizzaClassificCoriBase;
      ELSIF j = 3 THEN
            aRecRateizzaClassificCoriR0:=aRecRateizzaClassificCoriBase;
      END IF;

   END LOOP;

END getRateizzaAddTerritorio;

-- =================================================================================================
-- I valori dei montanti sono ridotti di quanto calcolato dal compenso origine in caso di modifica se
-- coincidono il riferimento all'anagrafico e quello del tipo compenso dipendente o altro
-- =================================================================================================
PROCEDURE modRateizzaAddTerritorio
   (
    aRecCompenso COMPENSO%ROWTYPE,
    segno CHAR,
    aRecRateizzaClassificCoriC0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
    aRecRateizzaClassificCoriP0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
    aRecRateizzaClassificCoriR0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE
   ) IS
   cvCdContributoRitenuta CONTRIBUTO_RITENUTA.cd_contributo_ritenuta%TYPE;
   cvCdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE;
   cvAmmontare CONTRIBUTO_RITENUTA.ammontare%TYPE;

   gen_cur GenericCurTyp;

BEGIN

   BEGIN

      OPEN gen_cur FOR

           SELECT DISTINCT A.cd_contributo_ritenuta,
                  B.cd_classificazione_cori,
                  A.ammontare
           FROM   CONTRIBUTO_RITENUTA A,
                  TIPO_CONTRIBUTO_RITENUTA B
           WHERE  A.cd_cds = aRecCompenso.cd_cds AND
                  A.cd_unita_organizzativa = aRecCompenso.cd_unita_organizzativa AND
                  A.esercizio = aRecCompenso.esercizio AND
                  A.pg_compenso = aRecCompenso.pg_compenso AND
                  A.ti_ente_percipiente = 'P' AND
                  B.cd_contributo_ritenuta = A.cd_contributo_ritenuta AND
                  B.dt_ini_validita = A.dt_ini_validita AND
                  B.pg_classificazione_montanti IS NULL AND
                  (B.cd_classificazione_cori = CNRCTB545.isCoriAddRegRecRate OR
                   B.cd_classificazione_cori = CNRCTB545.isCoriAddProRecRate OR
                   B.cd_classificazione_cori = CNRCTB545.isCoriAddComRecRate);

      LOOP

         FETCH gen_cur INTO
               cvCdContributoRitenuta,
               cvCdClassificazioneCori,
               cvAmmontare;

         EXIT WHEN gen_cur%NOTFOUND;

         IF segno = '-' THEN
            cvAmmontare:=cvAmmontare * -1;
         END IF;

         IF    cvCdClassificazioneCori = CNRCTB545.isCoriAddRegRecRate THEN
               aRecRateizzaClassificCoriR0.im_rateizzato:=(aRecRateizzaClassificCoriR0.im_rateizzato + cvAmmontare);
         ELSIF cvCdClassificazioneCori = CNRCTB545.isCoriAddProRecRate THEN
               aRecRateizzaClassificCoriP0.im_rateizzato:=(aRecRateizzaClassificCoriP0.im_rateizzato + cvAmmontare);
         ELSIF cvCdClassificazioneCori = CNRCTB545.isCoriAddComRecRate THEN
               aRecRateizzaClassificCoriC0.im_rateizzato:=(aRecRateizzaClassificCoriC0.im_rateizzato + cvAmmontare);
         END IF;

      END LOOP;

      CLOSE gen_cur;

   END;

END modRateizzaAddTerritorio;

-- =================================================================================================
-- Calcolo dell'importo della rata di addebito delle addizionali territorio in rateizzazione
-- =================================================================================================
FUNCTION calcolaAddTerritorioRecRate
   (
    isOrigineCompenso INTEGER,
    cdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE,
    aEsercizio NUMBER,
    aDataRifDa DATE,
    aDataRifA DATE,
    aRecRateizzaClassificCoriC0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
    aRecRateizzaClassificCoriP0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
    aRecRateizzaClassificCoriR0 IN OUT RATEIZZA_CLASSIFIC_CORI%ROWTYPE
   ) RETURN NUMBER IS

   aImporto NUMBER(15,2);
   aImportoMax NUMBER(15,2);
   aNumeroMesi INTEGER;
   aNumeroGiorniBase INTEGER;
   aNumeroGiorni INTEGER;
   aRecRateizzaClassificCoriBase RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Normalizzazione del record di RATEIZZA_CLASSIF_CORI opportuno

   IF    cdClassificazioneCori = CNRCTB545.isCoriAddRegRecRate THEN
         aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriR0;
   ELSIF cdClassificazioneCori = CNRCTB545.isCoriAddProRecRate THEN
         aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriP0;
   ELSIF cdClassificazioneCori = CNRCTB545.isCoriAddComRecRate THEN
         aRecRateizzaClassificCoriBase:=aRecRateizzaClassificCoriC0;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Calcolo dell'importo della rata di addizionale da addebitare. Se non vi è nulla di residuo
   -- torno 0. Il calcolo è fatto per undicesimi o in base ai giorni veri a seconda che l'intervallo
   -- sia o meno mensile

   IF (aRecRateizzaClassificCoriBase.im_da_rateizzare = 0 OR
       aRecRateizzaClassificCoriBase.im_da_rateizzare = aRecRateizzaClassificCoriBase.im_rateizzato) THEN
      aImporto:=0;
   ELSE

      IF isOrigineCompenso=CNRCTB545.isCompensoConguaglio THEN
         aImporto:=aRecRateizzaClassificCoriBase.im_da_rateizzare - aRecRateizzaClassificCoriBase.im_rateizzato;
      ELSE
         aImportoMax:=aRecRateizzaClassificCoriBase.im_da_rateizzare - aRecRateizzaClassificCoriBase.im_rateizzato;

         IF IBMUTL001.isDifferenzaMensile(aDataRifDa, aDataRifA) = IBMUTL001.isIntervalloDateMensile THEN
            aNumeroMesi:=IBMUTL001.getMonthsBetween(aDataRifDa, aDataRifA);
            aImporto:=ROUND(((aRecRateizzaClassificCoriBase.im_da_rateizzare / 11) * aNumeroMesi),2);
         ELSE
            aNumeroGiorniBase:=IBMUTL001.getDaysBetween(TO_DATE('0101' || aEsercizio,'DDMMYYYY'),
                                                        TO_DATE('3011' || aEsercizio,'DDMMYYYY'));
            aNumeroGiorni:=IBMUTL001.getDaysBetween(aDataRifDa,
                                                    aDataRifA);
            aImporto:=ROUND(((aRecRateizzaClassificCoriBase.im_da_rateizzare / aNumeroGiorniBase) * aNumeroGiorni),2);
         END IF;

         IF aImporto > aImportoMax THEN
            aImporto:=aImportoMax;
         END IF;
      END IF;
   END IF;

   RETURN aImporto;

END calcolaAddTerritorioRecRate;

END;
