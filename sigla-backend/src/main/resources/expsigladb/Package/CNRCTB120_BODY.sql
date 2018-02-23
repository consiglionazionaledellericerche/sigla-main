--------------------------------------------------------
--  DDL for Package Body CNRCTB120
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB120" IS

--==================================================================================================
-- Lettura di una testata di fattura passiva dai riferimento documento del terzo
--==================================================================================================
FUNCTION getTstFatturaDaRifTerzo
   (
    aCdTerzoFat VARCHAR2,
    aEsercizioFat NUMBER,
    aTiFat VARCHAR2,
    aNumeroFat VARCHAR2,
    eseguiLock CHAR
   ) RETURN FATTURA_PASSIVA%ROWTYPE IS
   aRecFatturaPassiva FATTURA_PASSIVA%ROWTYPE;

BEGIN

   BEGIN
      IF eseguiLock = 'Y' THEN

         SELECT * INTO aRecFatturaPassiva
         FROM   FATTURA_PASSIVA
         WHERE  cd_terzo = aCdTerzoFat AND
							  esercizio_fattura_fornitore = aEsercizioFat AND
                ti_fattura = aTiFat AND
                nr_fattura_fornitore = aNumeroFat
         				FOR UPDATE NOWAIT;
      ELSE
         SELECT * INTO aRecFatturaPassiva
         FROM   FATTURA_PASSIVA
         WHERE  cd_terzo = aCdTerzoFat AND
								esercizio_fattura_fornitore = aEsercizioFat AND
                ti_fattura = aTiFat AND
                nr_fattura_fornitore = aNumeroFat;
      END IF;

      RETURN aRecFatturaPassiva;

   EXCEPTION
      WHEN NO_DATA_FOUND then
           IBMERR001.RAISE_ERR_GENERICO
              ('Riferimento a fattura fornitore, terzo: '|| aCdTerzoFat ||' numero: ' || aEsercizioFat || '/' || aNumeroFat ||
               'non trovato');
   END;

END getTstFatturaDaRifTerzo;

--==================================================================================================
-- Ritorna un codice di sezionale acquisti ordinario
--==================================================================================================
FUNCTION getSezionaleOrdinarioAcq
   (
    aCdCds VARCHAR2,
    aUo VARCHAR2,
    aTipoIstitComm VARCHAR2,
    aEsercizio NUMBER
   ) RETURN SEZIONALE.cd_tipo_sezionale%TYPE IS
   aCdSezionale SEZIONALE.cd_tipo_sezionale%TYPE;
   aTipoFattura CHAR(1);

BEGIN

   aTipoFattura:=CNRCTB100.TI_FATT_FATTURA;

   SELECT A.cd_tipo_sezionale INTO aCdSezionale
   FROM   SEZIONALE A,
          TIPO_SEZIONALE B
   WHERE  A.cd_cds = aCdCds AND
          A.cd_unita_organizzativa = aUo AND
          A.ti_fattura = aTipoFattura AND
          A.esercizio = aEsercizio AND
          B.cd_tipo_sezionale = A.cd_tipo_sezionale AND
          B.ti_Acquisti_vendite = 'A' AND
          B.ti_istituz_commerc = aTipoIstitComm AND
          B.fl_ordinario = 'Y';

   RETURN aCdSezionale;

   EXCEPTION
      WHEN NO_DATA_FOUND then
           IBMERR001.RAISE_ERR_GENERICO
              ('Non trovato il riferimento al sezionale in generazione della fattura');

END getSezionaleOrdinarioAcq;

--==================================================================================================
-- Ritorna un record di TIPO_SEZIONALE dato un sezionale
--==================================================================================================
FUNCTION getTipoSezionale
   (
    aCdSezionale VARCHAR2
   ) RETURN TIPO_SEZIONALE%ROWTYPE IS
   aRecTipoSezionale TIPO_SEZIONALE%ROWTYPE;

BEGIN

   SELECT * INTO aRecTipoSezionale
   FROM   TIPO_SEZIONALE
   WHERE  cd_tipo_sezionale = aCdSezionale;

   RETURN aRecTipoSezionale;

EXCEPTION

   WHEN NO_DATA_FOUND THEN
        IBMERR001.RAISE_ERR_GENERICO
           ('Non trovato alcun riferimento al TIPO_SEZIONALE per il sezionale ' ||  aCdSezionale);

END getTipoSezionale;

--==================================================================================================
-- Elimina una intera fattura
--==================================================================================================
PROCEDURE eliminaFatturaPassiva
   (
    aCdCds FATTURA_PASSIVA.cd_cds%TYPE,
    aCdUo FATTURA_PASSIVA.cd_unita_organizzativa%TYPE,
    aEsercizio FATTURA_PASSIVA.esercizio%TYPE,
    aPgFattura FATTURA_PASSIVA.pg_fattura_passiva%TYPE
   ) IS

BEGIN

   DELETE FROM FATTURA_PASSIVA_RIGA
   WHERE cd_cds = aCdCds AND
         cd_unita_organizzativa = aCdUo AND
         esercizio = aEsercizio AND
         pg_fattura_passiva = aPgFattura;


   DELETE FROM FATTURA_PASSIVA
   WHERE cd_cds = aCdCds AND
         cd_unita_organizzativa = aCdUo AND
         esercizio = aEsercizio AND
         pg_fattura_passiva = aPgFattura;

END eliminaFatturaPassiva;


 procedure creaFattura(aFatt in out fattura_passiva%rowtype, aListRighe in out CNRCTB100.fattPassRigaList) is
  aNextNum number;
 begin
  aNextNum:=CNRCTB100.getNextNum(aFatt.cd_cds, aFatt.esercizio, aFatt.cd_unita_organizzativa, CNRCTB100.TI_FATTURA_PASSIVA, aFatt.utcr, aFatt.dacr);
  aFatt.pg_fattura_passiva:=aNextNum;
  aFatt.duva:=aFatt.dacr;
  aFatt.utuv:=aFatt.utcr;
  aFatt.pg_ver_rec:=1;
  CNRCTB100.INS_FATTURA_PASSIVA(aFatt);
  for i in 1 .. aListRighe.count loop
   aListRighe(i).pg_fattura_passiva:=aFatt.pg_fattura_passiva;
   aListRighe(i).progressivo_riga:=i;
   aListRighe(i).duva:=aFatt.dacr;
   aListRighe(i).utuv:=aFatt.utcr;
   aListRighe(i).pg_ver_rec:=1;
   CNRCTB100.INS_FATTURA_PASSIVA_RIGA(aListRighe(i));
  end loop;
 end;

-- =================================================================================================
-- Lettura del protocollo iva corrente dai diversi sezionali dati gli identificativi della UO,
-- l'esercizio e un tipo sezionale.
-- =================================================================================================
PROCEDURE getProtCorrenteSezionale
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    eseguiLock CHAR,
    aProtCorrenteFT IN OUT NUMBER,
    aProtCorrenteNC IN OUT NUMBER,
    aProtCorrenteND IN OUT NUMBER,
    aProtCorrentePG IN OUT NUMBER
   ) IS

   aRecSezionale SEZIONALE%ROWTYPE;
   gen_cv GenericCurTyp;

BEGIN

   aProtCorrenteFT:=-1;
   aProtCorrenteNC:=-1;
   aProtCorrenteND:=-1;
   aProtCorrentePG:=-1;

   BEGIN

      IF eseguiLock = 'Y' THEN

         OPEN gen_cv FOR

              SELECT *
              FROM   SEZIONALE
              WHERE  cd_cds = aCdCds AND
                     cd_unita_organizzativa = aCdUo AND
                     esercizio = aEsercizio AND
                     cd_tipo_sezionale = aCdTipoSezionale
              FOR UPDATE NOWAIT;

      ELSE

         OPEN gen_cv FOR

              SELECT *
              FROM   SEZIONALE
              WHERE  cd_cds = aCdCds AND
                     cd_unita_organizzativa = aCdUo AND
                     esercizio = aEsercizio AND
                     cd_tipo_sezionale = aCdTipoSezionale;

      END IF;

      LOOP

         FETCH gen_cv INTO
               aRecSezionale;

         EXIT WHEN gen_cv%NOTFOUND;

         IF    aRecSezionale.ti_fattura = 'F' THEN
               aProtCorrenteFT:=aRecSezionale.corrente;
         ELSIF aRecSezionale.ti_fattura = 'C' THEN
               aProtCorrenteNC:=aRecSezionale.corrente;
         ELSIF aRecSezionale.ti_fattura = 'D' THEN
               aProtCorrenteND:=aRecSezionale.corrente;
         ELSIF aRecSezionale.ti_fattura = 'T' THEN
               aProtCorrentePG:=aRecSezionale.corrente;
         END IF;

      END LOOP;

      CLOSE gen_cv;

   END;

END getProtCorrenteSezionale;

-- =================================================================================================
-- Aggiornamento del valore corrente sul sezionale
-- =================================================================================================
PROCEDURE upgSezionalePgCorrente
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER,
    aCdTipoSezionale VARCHAR2,
    aProtocolloFT NUMBER,
    aProtocolloNC NUMBER,
    aProtocolloND NUMBER,
    aProtocolloPG NUMBER
   ) IS
   i BINARY_INTEGER;
   aProtocolloTmp NUMBER(10);
   aTipoFattura CHAR(1);

BEGIN

   FOR i IN 1 .. 4

   LOOP

      IF    i = 1 THEN
            aProtocolloTmp:=aProtocolloFT;
            aTipoFattura:=CNRCTB100.TI_FATT_FATTURA;
      ELSIF i = 2 THEN
            aProtocolloTmp:=aProtocolloNC;
            aTipoFattura:=CNRCTB100.TI_FATT_NOTA_C;
      ELSIF i = 3 THEN
            aProtocolloTmp:=aProtocolloND;
            aTipoFattura:=CNRCTB100.TI_FATT_NOTA_D;
      ELSIF i = 4 THEN
            aProtocolloTmp:=aProtocolloPG;
            aTipoFattura:='T';
      END IF;

      UPDATE  SEZIONALE
      SET     corrente = aProtocolloTmp
      WHERE   cd_cds = aCdCds AND
              cd_unita_organizzativa = aCdUo AND
              esercizio = aEsercizio AND
              cd_tipo_sezionale = aCdTipoSezionale AND
              ti_fattura = aTipoFattura;

   END LOOP;

END upgSezionalePgCorrente;

-- =================================================================================================
-- Ritorna Y o N a seconda che la U.O. abbia o meno definito i sezionali
-- =================================================================================================
FUNCTION getUoHaSezionali
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER
   ) RETURN VARCHAR2 IS
   flHaSezionali CHAR(1);
   aNumeroSezionali INTEGER;

BEGIN

   flHaSezionali:='N';

   BEGIN

      SELECT COUNT(*) INTO aNumeroSezionali
      FROM   SEZIONALE
      WHERE  cd_cds = aCdCds AND
             cd_unita_organizzativa = aCdUo AND
             esercizio = aEsercizio;

   END;

   IF aNumeroSezionali > 0 THEN
      flHaSezionali:='Y';
   END IF;

   RETURN flHaSezionali;

END getUoHaSezionali;

-- =================================================================================================
-- Recupero dell'informazione che una fattura attiva è gestita ad esigibilità differita con i dati
-- relativi alla sua inclusione in una stampa definitiva di registri IVA o liquidazione
-- =================================================================================================
FUNCTION getFtAttivaDiffConIva
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER,
    aPgFattura NUMBER,
    aPgriga NUMBER,
    eseguiLock VARCHAR2
   ) RETURN VARCHAR2 IS
   attivaGestioneDifferita CHAR(1);
   aDtInizioStmRegistro DATE;
   aDtInizioStmLiquidazione DATE;
   aRecFatturaAttiva FATTURA_ATTIVA%ROWTYPE;

BEGIN

   attivaGestioneDifferita:='N';
   aDtInizioStmRegistro:=NULL;
   aDtInizioStmLiquidazione:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Recupero della testata della fattura attiva in elaborazione

   BEGIN

      SELECT * INTO aRecFatturaAttiva
      FROM   FATTURA_ATTIVA
      WHERE  cd_cds = aCdCds AND
             cd_unita_organizzativa = aCdUo AND
             esercizio = aEsercizio AND
             pg_fattura_attiva = aPgFattura
      FOR UPDATE NOWAIT;

   EXCEPTION

      WHEN no_data_found THEN
           IBMERR001.RAISE_ERR_GENERICO ('Non trovato il riferimento alla fattura attiva');

   END;

   -------------------------------------------------------------------------------------------------
   -- Controllo l'attivazione della gestione iva differita

   -- Se la fattura non è per iva differita esco

   IF aRecFatturaAttiva.fl_liquidazione_differita = 'N' THEN
      RETURN attivaGestioneDifferita;
   END IF;

   -- Verifico se la fattura è già stata stampata sul registro IVA o è stata liquidata

   CNRCTB255.getIsFatturaElaborataIva(aRecFatturaAttiva.cd_cds_origine,
                                      aRecFatturaAttiva.cd_uo_origine,
                                      aRecFatturaAttiva.esercizio,
                                      aRecFatturaAttiva.pg_fattura_attiva,
                                      aPgRiga,
                                      CNRCTB100.TI_FATTURA_ATTIVA,
                                      aRecFatturaAttiva.cd_cds,
                                      aRecFatturaAttiva.cd_unita_organizzativa,
                                      aDtInizioStmRegistro,
                                      aDtInizioStmLiquidazione);

   -- Se la fattura è già stata liquidata non attivo la gestione per iva differita

   IF aDtInizioStmLiquidazione IS NOT NULL THEN
      RETURN attivaGestioneDifferita;
   END IF;

   -- Attivo la gestione

   attivaGestioneDifferita:='Y';

   RETURN attivaGestioneDifferita;

END getFtAttivaDiffConIva;
-- =================================================================================================
-- Recupero dell'informazione che una fattura attiva è gestita ad esigibilità differita con i dati
-- relativi alla sua inclusione in una stampa definitiva di registri IVA o liquidazione
-- =================================================================================================
FUNCTION getFtDiffConIvaPassiva
   (
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aEsercizio NUMBER,
    aPgFattura NUMBER,
    aPgRiga NUMBER,
    eseguiLock VARCHAR2
   ) RETURN VARCHAR2 IS
   attivaGestioneDifferita CHAR(1);
   aDtInizioStmRegistro DATE;
   aDtInizioStmLiquidazione DATE;
   aRecFattura FATTURA_PASSIVA%ROWTYPE;

BEGIN

   attivaGestioneDifferita:='N';
   aDtInizioStmRegistro:=NULL;
   aDtInizioStmLiquidazione:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Recupero della testata della fattura  in elaborazione

   BEGIN

      SELECT * INTO aRecFattura
      FROM   FATTURA_PASSIVA
      WHERE  cd_cds = aCdCds AND
             cd_unita_organizzativa = aCdUo AND
             esercizio = aEsercizio AND
             pg_fattura_passiva = aPgFattura
      FOR UPDATE NOWAIT;

   EXCEPTION

      WHEN no_data_found THEN
           IBMERR001.RAISE_ERR_GENERICO ('Non trovato il riferimento alla fattura passiva');

   END;

   -------------------------------------------------------------------------------------------------
   -- Controllo l'attivazione della gestione iva differita

   -- Se la fattura non è per iva differita esco

   IF aRecFattura.fl_liquidazione_differita = 'N' THEN
      RETURN attivaGestioneDifferita;
   END IF;

   -- Verifico se la fattura è già stata stampata sul registro IVA o è stata liquidata

   CNRCTB255.getIsFatturaElaborataIva(aRecFattura.cd_cds_origine,
                                      aRecFattura.cd_uo_origine,
                                      aRecFattura.esercizio,
                                      aRecFattura.pg_fattura_passiva,
                                      aPgRiga,
                                      CNRCTB100.TI_FATTURA_PASSIVA,
                                      aRecFattura.cd_cds,
                                      aRecFattura.cd_unita_organizzativa,
                                      aDtInizioStmRegistro,
                                      aDtInizioStmLiquidazione);

   -- Se la fattura è già stata liquidata non attivo la gestione per iva differita

   IF aDtInizioStmLiquidazione IS NOT NULL THEN
      RETURN attivaGestioneDifferita;
   END IF;

   -- Attivo la gestione

   attivaGestioneDifferita:='Y';

   RETURN attivaGestioneDifferita;

END getFtDiffConIvaPassiva;
--==================================================================================================
-- Lettura di una testata di fattura passiva dai riferimento al compenso
--==================================================================================================
FUNCTION getFatturaRiferimento
   (	 aEsercizio NUMBER,
       aCdCds VARCHAR2,
       aCdUo VARCHAR2,
       aCompenso NUMBER,
       eseguiLock CHAR
   ) RETURN FATTURA_PASSIVA%ROWTYPE IS
   aRecFatturaPassiva FATTURA_PASSIVA%ROWTYPE;
BEGIN
		 IF eseguiLock = 'Y' THEN
   	     SELECT * INTO aRecFatturaPassiva
         FROM   FATTURA_PASSIVA
         WHERE  esercizio_compenso  = aEsercizio AND
							  cds_compenso = aCdCds AND
                uo_compenso = aCdUo AND
                pg_compenso = aCompenso
         				FOR UPDATE NOWAIT;
      ELSE
         SELECT * INTO aRecFatturaPassiva
           FROM   FATTURA_PASSIVA
           WHERE  esercizio_compenso  = aEsercizio AND
							  cds_compenso = aCdCds AND
                uo_compenso = aCdUo AND
                pg_compenso = aCompenso;
      END IF;

    RETURN aRecFatturaPassiva;
	 --Non intercetto il no_data_Found perchè se non presenti i riferimenti al compenso richiamo la vecchia CNRCTB120.getTstFatturaDaRifTerzo

END getFatturaRiferimento;
END;
