CREATE OR REPLACE FUNCTION getModPagCessionario
--==================================================================================================
--
-- Date: 08/11/2002
-- Version: 1.0
--
-- Ritorna la prima modalit? di pagamento definita sul cessionario compatibile con il tipo
-- pagamento in input
--
-- History:
--
-- Date: 08/11/2002
-- Version: 1.0
--
-- Creazione function
--
-- Body:
--
--==================================================================================================
   (
    aCdTerzo NUMBER,
    aTiPagamento VARCHAR2
   ) RETURN VARCHAR2 IS
   aCdModalitaPag MODALITA_PAGAMENTO.cd_modalita_pag%TYPE;

BEGIN

   aCdModalitaPag:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Composizione del valore di ritorno

   FOR aTempModPag IN
       (
        SELECT DISTINCT A.cd_modalita_pag
        FROM   RIF_MODALITA_PAGAMENTO B,
               MODALITA_PAGAMENTO A
        WHERE  B.ti_pagamento = aTiPagamento AND
               A.cd_modalita_pag = B.cd_modalita_pag AND
               A.cd_terzo = aCdTerzo AND
               A.cd_terzo_delegato IS NULL AND
               A.dacr =
                   (SELECT MAX(A1.dacr)
                    FROM   RIF_MODALITA_PAGAMENTO B1,
                           MODALITA_PAGAMENTO A1
                    WHERE  B1.ti_pagamento = aTiPagamento AND
                           A1.cd_modalita_pag = B1.cd_modalita_pag AND
                           A1.cd_terzo = aCdTerzo AND
                           A1.cd_terzo_delegato IS NULL)
        ORDER BY A.cd_modalita_pag ASC
       )
   LOOP

      aCdModalitaPag:=aTempModPag.cd_modalita_pag;
      EXIT;

   END LOOP;

   RETURN aCdModalitaPag;

END getModPagCessionario;
/


