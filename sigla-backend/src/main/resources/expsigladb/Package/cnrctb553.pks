CREATE OR REPLACE PACKAGE CNRCTB553 AS
-- =================================================================================================
--
-- CNRCTB553 - Gestione Acconto per le addizionali
--
-- Date: 27/02/2007
-- Version: 1.0
--
-- Dependency: CNRCTB 545 IBMUTL 001
--
-- History:
--
--
-- Date: 15/05/2007
-- Version: 1.1
--
-- Gestione degli acconti nei conguagli
--
-- =================================================================================================
--
-- Constants
--

--
-- Variabili globali
--
   dataOdierna DATE;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;


   CONST_ACCONTO_ADD_COM CONSTANT VARCHAR2(50) := 'ACCONTO_ADD_COM';
   CONST_PERIODO_VALIDITA CONSTANT VARCHAR2(50) := 'PERIODO_VALIDITA';
--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------
-- Inserimento di un record in ACCONTO_CLASSIFIC_CORI

   Procedure insAccontoClassificCori
      (
       aRecAccontoClassificCori ACCONTO_CLASSIFIC_CORI%Rowtype
      );

   Procedure calcolaAddTerritorioAcconto
      (
       inEsercizio  ACCONTO_CLASSIFIC_CORI.esercizio%Type,
       inPerc	    NUMBER,
       inUtente     ACCONTO_CLASSIFIC_CORI.utuv%Type
      );

   Procedure calcolaImponibileAnnoPrec
      (
       inEsercizio NUMBER,
       inRepID     INTEGER,
       inUtente    VARCHAR2
      );

-- Ritorna i record di ACCONTO_CLASSIFIC_CORI per addizionali territorio

   PROCEDURE getAccontoAddTerritorio
      (
       aEsercizio NUMBER,
       aCdAnag NUMBER,
       eseguiLock CHAR,
       aRecAccontoClassificCoriC0 IN OUT ACCONTO_CLASSIFIC_CORI%Rowtype
      );

-- I valori dei montanti sono ridotti di quanto calcolato dal compenso origine in caso di modifica se
-- coincidono il riferimento all'anagrafico e quello del tipo compenso dipendente o altro

   PROCEDURE modAccontoAddTerritorio
      (
       aRecCompenso COMPENSO%ROWTYPE,
       segno CHAR,
       aRecAccontoClassificCoriC0 IN OUT ACCONTO_CLASSIFIC_CORI%Rowtype
      );

-- Calcolo dell'importo della rata di addebito delle addizionali territorio in rateizzazione

   FUNCTION calcolaAddComAccontoRata
      (
       isOrigineCompenso INTEGER,
       cdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE,
       aEsercizio NUMBER,
       aDataRifDa DATE,
       aDataRifA DATE,
       aRecAccontoClassificCoriC0 IN OUT ACCONTO_CLASSIFIC_CORI%Rowtype
      ) RETURN NUMBER;

   FUNCTION getMinDataAcconto
      (
       aEsercizio NUMBER
      )
       Return DATE;

   FUNCTION getMaxDataAcconto
      (
       aEsercizio NUMBER
      )
       Return DATE;

End CNRCTB553;
/


CREATE OR REPLACE PACKAGE BODY CNRCTB553 As

-- ==================================================================================================
-- Inserimento di un record in ACCONTO_CLASSIFIC_CORI
-- ==================================================================================================
PROCEDURE insAccontoClassificCori
   (
    aRecAccontoClassificCori ACCONTO_CLASSIFIC_CORI%Rowtype
   ) IS

BEGIN

      INSERT INTO ACCONTO_CLASSIFIC_CORI
             (ESERCIZIO,
              CD_ANAG,
              CD_CLASSIFICAZIONE_CORI,
              IMPONIBILE,
  	      ALIQUOTA,
  	      IM_ACCONTO_CALCOLATO,
              IM_ACCONTO_TRATTENUTO,
              PG_COMUNE,
              DACR,
              UTCR,
              DUVA,
              UTUV,
              PG_VER_REC)
      VALUES (aRecAccontoClassificCori.esercizio,
              aRecAccontoClassificCori.cd_anag,
              aRecAccontoClassificCori.cd_classificazione_cori,
              aRecAccontoClassificCori.imponibile,
              aRecAccontoClassificCori.aliquota,
              aRecAccontoClassificCori.im_acconto_calcolato,
              aRecAccontoClassificCori.im_acconto_trattenuto,
              aRecAccontoClassificCori.pg_comune,
              aRecAccontoClassificCori.dacr,
              aRecAccontoClassificCori.utcr,
              aRecAccontoClassificCori.duva,
              aRecAccontoClassificCori.utuv,
              aRecAccontoClassificCori.pg_ver_rec);

END insAccontoClassificCori;

/* Per il calcolo dell'imponibile su cui calcolare l'acconto (imponibile lordo - family)
   Procedura che richiama le stese procedure del CUD e conserva i dati in ESTRAZIONE_CUD */
Procedure calcolaImponibileAnnoPrec
   (
    inEsercizio NUMBER,
    inRepID     INTEGER,
    inUtente    VARCHAR2
   ) Is
   gen_cur_anag GenericCurTyp;
   aCd_anag	 anagrafico.cd_anag%Type;
Begin
      cnrctb900.valorizzaParametri(inEsercizio-1);
      Open gen_cur_anag For

	   Select Distinct t.cd_anag
	   From MINICARRIERA m, MINICARRIERA_RATA R, TERZO t, RATEIZZA_CLASSIFIC_CORI c
	   Where M.CD_CDS = R.CD_CDS
	     And M.CD_UNITA_ORGANIZZATIVA = R.CD_UNITA_ORGANIZZATIVA
	     And M.ESERCIZIO = R.ESERCIZIO
	     And M.PG_MINICARRIERA = R.PG_MINICARRIERA
	     And t.cd_terzo = m.cd_terzo
	     And t.cd_anag = c.cd_anag
	     And c.ESERCIZIO = inEsercizio - 1
	     And c.cd_classificazione_cori = 'C0'
	     And c.fl_temporaneo = 'N'
	     And m.ESERCIZIO <= inEsercizio
	     And m.stato NOT IN ('S','C')
	     And R.STATO_ASS_COMPENSO = 'N'
	     And m.cd_trattamento In (Select a.cd_trattamento
	     			   From ass_ti_rapp_ti_tratt A
	     			   Where a.cd_tipo_rapporto In('COLL','BORS'))             -- T098 ha ASS
	     And m.cd_trattamento In (Select e.cd_trattamento
	     			   From config_estrazione_cud e
	     			   Where e.esercizio = inEsercizio - 1);
      Loop
         Fetch gen_cur_anag Into aCd_anag;

         Exit When gen_cur_anag %NOTFOUND;
            -------------------------------------------------------------------------------------------------
   	    -- Inserimento in ESTRAZIONE_CUD_DETT dei compensi oggetto di esposizione sul CUD. Caricamento
   	    -- dei dati relativi alla testata del compenso
	    cnrctb900.inserisciDatiCUDDett(inEsercizio-1,
                        aCd_anag,
                        inRepID,
                        inUtente);


      End Loop;
      Close gen_cur_anag;
      -------------------------------------------------------------------------------------------------
      -- Inserimento in ESTRAZIONE_CUD_DETT dei compensi oggetto di esposizione sul CUD. Caricamento
      -- dei dati relativi alla testata del compenso

      cnrctb900.upgDatiCUDDettCori(inEsercizio-1,
                                   inRepID);

      --COMMIT;
      -------------------------------------------------------------------------------------------------
      -- Inserimento in ESTRAZIONE_CUD_DETT dei compensi oggetto di esposizione sul CUD. Caricamento
      -- dei dati relativi alla testata del compenso

      cnrctb900.upgDatiCUDDettCong(inEsercizio-1,
                                   inRepID);
      -------------------------------------------------------------------------------------------------
      -- Aggregazione dati di ESTRAZIONE_CUD_DETT in ESTRAZIONE_CUD.

      cnrctb900.aggregaDatiCud(inEsercizio-1,
                               inRepID,
                               inUtente);

End calcolaImponibileAnnoPrec;

Procedure calcolaAddTerritorioAcconto
    (
       inEsercizio  ACCONTO_CLASSIFIC_CORI.esercizio%Type,
       inPerc	    NUMBER,
       inUtente     ACCONTO_CLASSIFIC_CORI.utuv%Type
    ) Is

    gen_cur_anag GenericCurTyp;
    aCd_anag	 anagrafico.cd_anag%Type;
    aRecAccontoClassificCori ACCONTO_CLASSIFIC_CORI%ROWTYPE;
    aCdRegione  REGIONE.cd_regione%TYPE;
    aCdProvincia PROVINCIA.cd_provincia%TYPE;
    aPgComune	 ACCONTO_CLASSIFIC_CORI.pg_comune%Type;
    aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE;
    aTiAnag	varchar2(1);
    aAliq  ACCONTO_CLASSIFIC_CORI.aliquota%Type;
    aCdCori	V_PRE_SCAGLIONE.cd_contributo_ritenuta%Type:='ADDCOM';
    inRepID  	INTEGER;
    imponibileLordoAnnoPrec	number(15,2);
    im_deduzione_family		number(15,2);
    imponibileNettoAnnoPrec	number(15,2);
    contaAcconto		number;
Begin
      dataOdierna := Sysdate;
      aCdRegione:='*';
      aCdProvincia:='*';
      aTiAnag:='*';
      aAliq:=0;

     Select Count(1)
     Into contaAcconto
     From ACCONTO_CLASSIFIC_CORI
     Where esercizio = inEsercizio;

     If contaAcconto > 0 Then
         IBMERR001.RAISE_ERR_GENERICO('Calcolo Acconto gi? effettuato in precedenza. E'' solo possibile visualizzare i dettagli.');
     End If;

     If inPerc is null then
         IBMERR001.RAISE_ERR_GENERICO('Percentuale con cui effettuare il calcolo dell''Acconto non valorizzata.');
     End If;

      select CNRSEQ00_PG_ESTR_FISC.nextval Into inRepID from Dual;

      --Procedura che conserva in ESTRAZIONE_CUD gli imponibili di tutti i terzi interessati
      calcolaImponibileAnnoPrec(inEsercizio,inRepID,inUtente);

      Open gen_cur_anag For

	   Select Distinct t.cd_anag
	   From MINICARRIERA m, MINICARRIERA_RATA R, TERZO t, RATEIZZA_CLASSIFIC_CORI c
	   Where M.CD_CDS = R.CD_CDS
	     And M.CD_UNITA_ORGANIZZATIVA = R.CD_UNITA_ORGANIZZATIVA
	     And M.ESERCIZIO = R.ESERCIZIO
	     And M.PG_MINICARRIERA = R.PG_MINICARRIERA
	     And t.cd_terzo = m.cd_terzo
	     And t.cd_anag = c.cd_anag
	     And c.ESERCIZIO = inEsercizio - 1
	     And c.cd_classificazione_cori = 'C0'
	     And c.fl_temporaneo = 'N'
	     And m.ESERCIZIO <= inEsercizio
	     And m.stato NOT IN ('S','C')
	     And R.STATO_ASS_COMPENSO = 'N'
	     And m.cd_trattamento In (Select a.cd_trattamento
	     			   From ass_ti_rapp_ti_tratt A
	     			   Where a.cd_tipo_rapporto In('COLL','BORS'))             -- T098 ha ASS
	     And m.cd_trattamento In (Select e.cd_trattamento
	     			   From config_estrazione_cud e
	     			   Where e.esercizio = inEsercizio - 1);
	     --And t.cd_anag Not In (101496);

      Loop

         Fetch gen_cur_anag Into aCd_anag;

         Exit When gen_cur_anag %NOTFOUND;

	 --prendo il comune del domicilio fiscale
	 --aPgComune := cnrctb080.getAnagDaCdAnag(aCd_anag).pg_comune_fiscale;
	 --prendo il comune dal terzo perch? potrebbe essere diverso da quello dell'anagrafico
	 --(anche i compensi lo prendono dal terzo)
	 aPgComune := cnrctb080.getTerzoDaCdAnag(aCd_anag).pg_comune_sede;

	 If aPgComune Is Not Null Then

	     aRecAccontoClassificCori.esercizio := inEsercizio;
	     aRecAccontoClassificCori.cd_anag := aCd_anag;
	     aRecAccontoClassificCori.cd_classificazione_cori := cnrctb545.isCoriAddCom;

	     Select Nvl(Sum(imponibile_si_detr),0), Nvl(Sum(im_deduzione_family),0), Nvl(Sum(imponibile_irpef),0)
   	     Into imponibileLordoAnnoPrec, im_deduzione_family, imponibileNettoAnnoPrec
   	     From   ESTRAZIONE_CUD
   	     Where  id_estrazione = inRepID
     	       And  esercizio = inEsercizio-1
     	       And  dip_cd_anag = aCd_anag
     	       And  rigo_cococo = 1;

	     --solo se l'imponibile netto ? >0 calcolo l'acconto
	     If imponibileNettoAnnoPrec > 0 Then

  	        aRecAccontoClassificCori.imponibile := imponibileLordoAnnoPrec - im_deduzione_family;

                -- solo se l'imponibile ? maggiore dell'eventuale esenzione calcolo l'acconto
                If aRecAccontoClassificCori.imponibile > CNRCTB545.getEsenzioniAddcom(aPgComune,dataOdierna).importo Then

                   -- Se per il comune ? prevista l'applicazione dell'aliquota massima prendo direttamente lo scaglione passando l'imponibile
                   If cnrctb545.getIsComConAliqMax(aPgComune) = 'Y' then
	                     --prendo l'aliquota associata
	                     aRecVPreScaglione:=CNRCTB545.getScaglione(aCdCori,
                                                       	 aTiAnag,
                                                       	 dataOdierna,
                                                       	 aRecAccontoClassificCori.imponibile,
                                                       	 aAliq,
                                                       	 aCdRegione,
                                                       	 aCdProvincia,
                                                         aPgComune);
                             --inserisco solo se l'aliquota ? > 0
                             If aRecVPreScaglione.aliquota_percip > 0 Then
	                                aRecAccontoClassificCori.aliquota := aRecVPreScaglione.aliquota_percip;
                                  aRecAccontoClassificCori.im_acconto_calcolato := ((aRecAccontoClassificCori.imponibile * aRecAccontoClassificCori.aliquota / 100) * inPerc)/100;
                                  aRecAccontoClassificCori.im_acconto_trattenuto := 0;
                                  aRecAccontoClassificCori.pg_comune := aPgComune;
                                  aRecAccontoClassificCori.dacr := dataOdierna;
                                  aRecAccontoClassificCori.utcr := inUtente;
                                  aRecAccontoClassificCori.duva := dataOdierna;
                                  aRecAccontoClassificCori.utuv := inUtente;
                                  aRecAccontoClassificCori.pg_ver_rec := 1;

	                                insAccontoClassificCori(aRecAccontoClassificCori);
	                           End If;
	                 Else
	                    Declare
	                      aImportoAccesso NUMBER(15,2);
	                      aImponibileBlocco NUMBER(15,2);
	                      resto NUMBER(15,2);
	                      aImportoAddCom NUMBER(15,2);

	                      gen_cur GenericCurTyp;
	                      aRecTmpScaglioneImponibile V_SCAGLIONE%ROWTYPE;
	                    Begin
	                    	 aImportoAccesso:=0;
	                    	 aImportoAddCom:=0;
	                       -- Recupero del primo scaglione
                         aRecVPreScaglione:=CNRCTB545.getScaglione(aCdCori,
                                                                   aTiAnag,
                                                                   dataOdierna,
                                                                   aImportoAccesso,
                                                                   aAliq,
                                                                   aCdRegione,
                                                                   aCdProvincia,
                                                                   aPgComune);

                         -- L'accesso agli scaglioni successivi ? svolto solo se l'importo massimo di riferimento ? maggiore del
                         -- valore dell'importo superiore altrimenti si determina il valore dell'irpef sul primo scaglione

                         IF aRecVPreScaglione.im_superiore >= aRecAccontoClassificCori.imponibile THEN
                             --inserisco solo se l'aliquota ? > 0
                             If aRecVPreScaglione.aliquota_percip > 0 Then
	                                aRecAccontoClassificCori.aliquota := aRecVPreScaglione.aliquota_percip;
                                  aRecAccontoClassificCori.im_acconto_calcolato := ((aRecAccontoClassificCori.imponibile * aRecAccontoClassificCori.aliquota / 100) * inPerc)/100;
                                  aRecAccontoClassificCori.im_acconto_trattenuto := 0;
                                  aRecAccontoClassificCori.pg_comune := aPgComune;
                                  aRecAccontoClassificCori.dacr := dataOdierna;
                                  aRecAccontoClassificCori.utcr := inUtente;
                                  aRecAccontoClassificCori.duva := dataOdierna;
                                  aRecAccontoClassificCori.utuv := inUtente;
                                  aRecAccontoClassificCori.pg_ver_rec := 1;

	                                insAccontoClassificCori(aRecAccontoClassificCori);
	                           End If;
                         ELSE

                            -- Valorizzazione importo ADD COM per il primo scaglione

                            aImponibileBlocco:=aRecVPreScaglione.im_superiore;
                            resto:=aRecAccontoClassificCori.imponibile - aImponibileBlocco;
                            aImportoAddCom:=ROUND((aImponibileBlocco * (aRecVPreScaglione.aliquota_percip / 100)),2);

                            -- Valorizzazione importo ADD COM per i restanti scaglioni
                            BEGIN
                               OPEN gen_cur FOR

                                    SELECT *
                                    FROM   V_SCAGLIONE
                                    WHERE  cd_contributo_ritenuta = aCdCori AND
                                           (ti_anagrafico = aTiAnag OR
                                            ti_anagrafico = '*' ) AND
                                           dt_inizio_validita <= dataOdierna AND
                                           dt_fine_validita >= dataOdierna AND
                                           cd_regione = aCdRegione AND
                                           cd_provincia = aCdProvincia AND
                                           pg_comune = aPgComune AND
                                           im_inferiore >= aRecVPreScaglione.im_superiore AND
                                           im_inferiore <= aRecAccontoClassificCori.imponibile
                                    ORDER BY 4;

                               LOOP

                                  FETCH gen_cur INTO aRecTmpScaglioneImponibile;

                                  EXIT WHEN gen_cur%NOTFOUND;

                                  IF aRecAccontoClassificCori.imponibile < aRecTmpScaglioneImponibile.im_superiore THEN
                                     aImponibileBlocco:=resto;
                                  ELSE
                                     aImponibileBlocco:=aRecTmpScaglioneImponibile.im_superiore -
                                                        aRecTmpScaglioneImponibile.im_inferiore +
                                                        0.01;
                                     resto:=resto - aImponibileBlocco;
                                  END IF;

                                  aImportoAddCom:=aImportoAddCom + ROUND(aImponibileBlocco * (aRecTmpScaglioneImponibile.aliquota_percip / 100),2);

                               END LOOP;

                               CLOSE gen_cur;
                            END;

                            aRecAccontoClassificCori.aliquota := aRecVPreScaglione.aliquota_percip;
                            aRecAccontoClassificCori.im_acconto_calcolato := (aImportoAddCom * inPerc)/100;
                            aRecAccontoClassificCori.im_acconto_trattenuto := 0;
                            aRecAccontoClassificCori.pg_comune := aPgComune;
                            aRecAccontoClassificCori.dacr := dataOdierna;
                            aRecAccontoClassificCori.utcr := inUtente;
                            aRecAccontoClassificCori.duva := dataOdierna;
                            aRecAccontoClassificCori.utuv := inUtente;
                            aRecAccontoClassificCori.pg_ver_rec := 1;

	                          insAccontoClassificCori(aRecAccontoClassificCori);

                         END IF;
	                    End;
	                 End if;    --If cnrctb545.getIsComConAliqMax(aPgComune) = 'Y' then
	            End If;   --If aRecAccontoClassificCori.imponibile > CNRCTB545.getEsenzioniAddcom(aPgComune,dataOdierna).importo Then
	     End If;    --If imponibileNettoAnnoPrec > 0 Then
	  End If;  --If aPgComune Is Not Null Then
   End Loop;

   Close gen_cur_anag;

End calcolaAddTerritorioAcconto;

-- ==================================================================================================
-- Ritorna i record di ACCONTO_CLASSIFIC_CORI per addizionali territorio
-- ==================================================================================================
PROCEDURE getAccontoAddTerritorio
   (
    aEsercizio NUMBER,
    aCdAnag NUMBER,
    eseguiLock CHAR,
    aRecAccontoClassificCoriC0 IN OUT ACCONTO_CLASSIFIC_CORI%Rowtype
   ) IS

   isTemporaneo CHAR(1);
   aCdClassificazioneCoriBase CLASSIFICAZIONE_CORI.cd_classificazione_cori%TYPE;
   j BINARY_INTEGER;

   aRecAccontoClassificCoriBase ACCONTO_CLASSIFIC_CORI%ROWTYPE;

BEGIN

   isTemporaneo:='N';

      aCdClassificazioneCoriBase:=CNRCTB545.isCoriAddCom;

      aRecAccontoClassificCoriBase:=CNRCTB545.getAccontoClassificCori(aEsercizio,
                                                                      aCdAnag,
                                                                      isTemporaneo,
                                                                      aCdClassificazioneCoriBase,
                                                                      eseguiLock);
      aRecAccontoClassificCoriC0:=aRecAccontoClassificCoriBase;

END getAccontoAddTerritorio;

-- =================================================================================================
-- I valori dei montanti sono ridotti di quanto calcolato dal compenso origine in caso di modifica se
-- coincidono il riferimento all'anagrafico e quello del tipo compenso dipendente o altro
-- =================================================================================================
PROCEDURE modAccontoAddTerritorio
   (
    aRecCompenso COMPENSO%ROWTYPE,
    segno CHAR,
    aRecAccontoClassificCoriC0 IN OUT ACCONTO_CLASSIFIC_CORI%Rowtype
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
                  B.cd_classificazione_cori = CNRCTB545.isCoriAddComAcconto;

      LOOP

         FETCH gen_cur INTO
               cvCdContributoRitenuta,
               cvCdClassificazioneCori,
               cvAmmontare;

         EXIT WHEN gen_cur%NOTFOUND;

         IF segno = '-' THEN
            cvAmmontare:=cvAmmontare * -1;
         END IF;

         IF    cvCdClassificazioneCori = CNRCTB545.isCoriAddComAcconto THEN
               aRecAccontoClassificCoriC0.im_acconto_trattenuto:=(aRecAccontoClassificCoriC0.im_acconto_trattenuto + cvAmmontare);
         END IF;

      END LOOP;

      CLOSE gen_cur;

   END;

END modAccontoAddTerritorio;

FUNCTION getMinDataAcconto
  (aEsercizio NUMBER)Return DATE IS
   minData DATE;

BEGIN
   Begin
     select To_Date(To_Char(DT01,'DDMM')||aEsercizio,'DDMMYYYY')
     into minData
     from configurazione_cnr
     Where esercizio = aEsercizio
      and cd_unita_funzionale = '*'
	  and cd_chiave_primaria = CONST_ACCONTO_ADD_COM
      and cd_chiave_secondaria = CONST_PERIODO_VALIDITA;

     If minData is null then
         IBMERR001.RAISE_ERR_GENERICO('Data minima di validit? per Acconto Addizionale comunale non specificata in configurazione CNR');
     End If;

     RETURN minData;

   Exception
      When No_Data_Found Then
         IBMERR001.RAISE_ERR_GENERICO('Periodo di validit? per Acconto Addizionale comunale non specificata in configurazione CNR');
   End;

END getMinDataAcconto;

FUNCTION getMaxDataAcconto
  (aEsercizio NUMBER)Return DATE IS
   maxData DATE;

BEGIN
   Begin
     select To_Date(To_Char(DT02,'DDMM')||aEsercizio,'DDMMYYYY')
     into maxData
     from configurazione_cnr
     Where esercizio = aEsercizio
      and cd_unita_funzionale = '*'
	  and cd_chiave_primaria = CONST_ACCONTO_ADD_COM
      and cd_chiave_secondaria = CONST_PERIODO_VALIDITA;

     If maxData is null then
         IBMERR001.RAISE_ERR_GENERICO('Data massima di validit? per Acconto Addizionale comunale non specificata in configurazione CNR');
     End If;

     RETURN maxData;

   Exception
      When No_Data_Found Then
         IBMERR001.RAISE_ERR_GENERICO('Periodo di validit? per Acconto Addizionale comunale non specificata in configurazione CNR');
   End;

END getMaxDataAcconto;
-- =================================================================================================
-- Calcolo dell'importo della rata di addebito dell'acconto addizionale comunale
-- =================================================================================================
FUNCTION calcolaAddComAccontoRata
   (
    isOrigineCompenso INTEGER,
    cdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE,
    aEsercizio NUMBER,
    aDataRifDa DATE,
    aDataRifA  DATE,
    aRecAccontoClassificCoriC0 IN OUT ACCONTO_CLASSIFIC_CORI%Rowtype
   ) RETURN NUMBER IS

   aImporto NUMBER(15,2);
   aImportoMax NUMBER(15,2);
   aNumeroMesi INTEGER;
   aDataMinAcc DATE;
   aDataMaxAcc DATE;
   aRecAccontoClassificCoriBase ACCONTO_CLASSIFIC_CORI%ROWTYPE;

BEGIN
   dataOdierna := Sysdate;

   IF cdClassificazioneCori = CNRCTB545.isCoriAddComAcconto THEN
         aRecAccontoClassificCoriBase:=aRecAccontoClassificCoriC0;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Calcolo dell'importo della rata di addizionale da addebitare. Se non vi ? nulla di residuo
   -- torno 0. Anche se ? stato gi? fatto un conguaglio torno 0.

   IF (aRecAccontoClassificCoriBase.im_acconto_calcolato = 0 Or
       aRecAccontoClassificCoriBase.im_acconto_calcolato = aRecAccontoClassificCoriBase.im_acconto_trattenuto Or
       aRecAccontoClassificCoriBase.pg_conguaglio Is Not Null) THEN
      aImporto:=0;
   ELSE

      IF isOrigineCompenso=CNRCTB545.isCompensoConguaglio THEN
         aImporto:=aRecAccontoClassificCoriBase.im_acconto_calcolato - aRecAccontoClassificCoriBase.im_acconto_trattenuto;
      ELSE
         aImportoMax:=aRecAccontoClassificCoriBase.im_acconto_calcolato - aRecAccontoClassificCoriBase.im_acconto_trattenuto;

	 aDataMinAcc := getMinDataAcconto(aEsercizio);
	 aDataMaxAcc := getMaxDataAcconto(aEsercizio);

	 If Trunc(dataOdierna) >= Trunc(aDataMinAcc) And
	    Trunc(dataOdierna) <= Trunc(aDataMaxAcc) Then
	      aNumeroMesi:=IBMUTL001.getMonthsBetween(aDataRifDa, aDataRifA);
              aImporto:=ROUND(((aRecAccontoClassificCoriBase.im_acconto_calcolato / 9) * aNumeroMesi),2);
	 Else
	      aImporto:=0;
	 End If;

         IF aImporto > aImportoMax THEN
            aImporto:=aImportoMax;
         END IF;
      END IF;
   END IF;

   RETURN aImporto;

END calcolaAddComAccontoRata;

END;
/


