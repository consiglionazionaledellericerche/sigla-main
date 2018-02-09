CREATE OR REPLACE FUNCTION         getFlFaiReversale
--==================================================================================================
--
-- Date: 15/10/2002
-- Version: 1.0
--
-- Ritorna l'indicazione se, per il mandato da generare sul documento, deve essere anche generata
-- una reversale in automatico
--
--
-- History:
--
-- Date: 15/10/2002
-- Version: 1.0
--
-- Creazione funzione
--
--
-- Body:
--
--==================================================================================================
   (
    aTiFattura VARCHAR2,
    aTiIstituzCommerc VARCHAR2,
    aTiBeneServizio VARCHAR2,
    aFlSanMarinoSenzaIva VARCHAR2,
    aFlIntraUe VARCHAR2,
    aFlSplitPayment VARCHAR2,
    aflregistro VARCHAR2
   ) RETURN VARCHAR2 IS
   aValoreRitorno VARCHAR2(1);

BEGIN

   aValoreRitorno:='N';

   -------------------------------------------------------------------------------------------------
   -- Composizione del valore di ritorno

   -- Si torna l'indicazione della generazione di una reversale solo se la fattura soddisfa le
   -- seguenti condizioni:
   -- 1) Il tipo fattura ?attura.
   -- 2) La fattura ?stituzionale
   -- 3) La fattura ?iferita a beni
   -- 4) La fattura ?i tipo San Marino senza IVA
   -- 5) La fattura ?i tipo  intra UE
   -- 6) Inizialmente utilizzato solo per identificare le fatture sul registro servizi non residenti-(valori possibili,'Y'/'N'),
   -- utilizzato anche nel caso di Beni per capire se non ?tato utilizzato il registro 'a/ist' verificandolo con ti_bene_servizi sul sezionale !='*'
   -- veniva erroneamente registrata la reversale

   IF (aTiFattura = 'F' AND
       aTiIstituzCommerc = 'I' AND
       aTiBeneServizio = 'B' AND
       ((aFlSanMarinoSenzaIva = 'Y' OR
        aFlIntraUe = 'Y')) and aflregistro != '*') THEN
       aValoreRitorno:='Y';
       -- Tipo sezionale FL_SERVIZI_NON_RESIDENTI = Si
   Elsif (aTiFattura = 'F' AND
       aTiIstituzCommerc = 'I' AND
       aFlSplitPayment = 'Y') THEN
       aValoreRitorno:='Y';
   Elsif (aTiFattura = 'F' and aflregistro = 'Y') Then
        aValoreRitorno:='Y';
   End IF;
   RETURN aValoreRitorno;

END getFlFaiReversale;
/


