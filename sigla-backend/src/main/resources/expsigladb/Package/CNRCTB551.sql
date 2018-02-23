--------------------------------------------------------
--  DDL for Package CNRCTB551
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB551" AS
--==================================================================================================
--
-- CNRCTB551 - Calcolo e scrittura COMPENSI (Elaborazione imponibile fiscale per compensi da minicariera
--                                           con trattamento annualizzato)
--
-- Date: 02/11/2004
-- Version: 1.2
--
-- Dependency: CNRCTB 080/545/600 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 24/03/2003
-- Version: 1.0
--
-- Richiesta CINECA n.541. Revisione del calcolo dell'aliquota fiscale media in pagamento compensi da
-- minicarriera. Modifica metodo getImRateDiTerzoInEsercizio, non si esporta più semplicemente il valore
-- totale delle rate di minicarriera con scadenza nell'esercizio per un dato soggetto anagrafico ma
-- a rottura di ogni trattamento, si procede al calcolo del vero imponibile fiscale.
--
-- Date: 28/03/2003
-- Version: 1.1
--
-- Fix errore interno in calcolo compenso, l'imponibile era preso dal compenso in calcolo e non dal
-- valore esterno.
--
-- Date: 02/11/2004
-- Version: 1.2
--
-- Rilascio errore CINECA n. 850. errore in esegui calcolo di compenso da minicarriera di un terzo
-- avente anche una minicarriera con trattamento cessato.
--
-- Date: 01/08/2006
-- Version: 2.8
--
-- Aggiunta la nuova "Gestione dei Cervelli"
--==================================================================================================
--
-- Constants
--

--
-- Variabili globali
--

   dataOdierna DATE;

   -- Memorizza la testata del compenso in elaborazione

   aRecCompenso COMPENSO%ROWTYPE;

   -- Memorizza l'anagrafico di riferimento per il terzo beneficiario del compenso

   aRecAnagrafico ANAGRAFICO%ROWTYPE;

   -- Memorizza il tipo trattamento per il compenso in elaborazione

   aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE;

   -- Definizione tabella PL/SQL di appoggio per calcolato contributi e ritenute

   tabella_cori CNRCTB545.insiemeCoriTab;
   tabella_cori_det CNRCTB545.insiemeCoriTab;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures
--

----------------------------------------------------------------------------------------------------
-- MAIN PROCEDURE
----------------------------------------------------------------------------------------------------

-- Determinazione dell'imponibile fiscale al netto delle eventuali previdenziali

   PROCEDURE calcolaImponibileIrpef
      (
       inRecAnagrafico ANAGRAFICO%ROWTYPE,
       inRecCompenso COMPENSO%ROWTYPE,
       inCdTrattamento VARCHAR2,
       inImportoLordoIrpef NUMBER,
       aImponibileFiscale IN OUT NUMBER
      );

----------------------------------------------------------------------------------------------------
-- ROUTINE COMUNI
----------------------------------------------------------------------------------------------------

-- Valorizzazione delle matrici per il calcolo dei dettagli di un compenso; CONTRIBUTO_RITENUTA e
-- CONTRIBUTO_RITENUTA_DET

   PROCEDURE costruisciTabellaCori;

   PROCEDURE costruisciTabellaCoriDet
      (
       indice BINARY_INTEGER,
       aImponibile CONTRIBUTO_RITENUTA_DET.imponibile%TYPE,
       aRecVPreScaglione V_PRE_SCAGLIONE%ROWTYPE
      );

-- Verifica consistenza dei dati della matrice per il calcolo dei dettagli di un compenso

   PROCEDURE verificaTabellaCori;

-- Calcola CORI per trattamenti altro

   PROCEDURE calcolaCoriAltro
      (
       inImportoLordoIrpef NUMBER,
       aImponibileFiscale IN OUT NUMBER
      );

-- Valorizza i dati per contributi ritenuta definiti per territorio

   PROCEDURE getDatiTerritorio
      (
       aCdRegione IN OUT COMPENSO.cd_regione_add%TYPE,
       aCdProvincia IN OUT COMPENSO.cd_provincia_add%TYPE,
       aPgComune IN OUT COMPENSO.pg_comune_add%TYPE,
       indice BINARY_INTEGER
      );

-- Calcolo imponibile per ogni singolo cori del trattamento

   FUNCTION getImponibileCori
      (
       indice BINARY_INTEGER,
       aImponibile NUMBER
      )RETURN NUMBER;

END CNRCTB551;
