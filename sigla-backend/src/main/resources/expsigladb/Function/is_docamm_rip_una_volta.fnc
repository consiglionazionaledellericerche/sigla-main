CREATE OR REPLACE function IS_DOCAMM_RIP_UNA_VOLTA(
--
-- Date: 05/03/2004
-- Version: 1.1
--
-- Vista di controllo che il documento amministrativo specificato sia stato riportato a nuovo esercizio e solo dall'anno di creazione al successivo
--
-- History:
--
-- Date: 19/02/2004
-- Version: 1.0
-- Creazione
--
-- Date: 05/03/2004
-- Version: 1.1
-- Fi xerrore mancanza distinct
--
-- Body:
--
 aCD_TIPO_DOCUMENTO varchar2,
 aCD_CDS varchar2,
 aCD_UNITA_ORGANIZZATIVA varchar2,
 aESERCIZIO number,
 aPG_NUMERO_DOCUMENTO number
) return char is
 aNum number;
begin
 -- Non esistono collegamenti a doc contabili in esercizi > esercizio doc + 1
 begin
   select distinct 1 into aNum from V_DOC_AMM_COGE_RIGA b where
            b.CD_TIPO_DOCUMENTO=aCD_TIPO_DOCUMENTO
	    AND b.CD_CDS=aCD_CDS
	    AND b.CD_UNITA_ORGANIZZATIVA=aCD_UNITA_ORGANIZZATIVA
	    AND b.ESERCIZIO=aESERCIZIO
		AND b.PG_NUMERO_DOCUMENTO=aPG_NUMERO_DOCUMENTO
		AND b.ESERCIZIO_DOC > aESERCIZIO + 1;
  return 'N';
 exception when NO_DATA_FOUND then
  null;
 end;
 -- Esiste almeno un collegamento a doc contabile in esercizio successivo a quello del documento
 begin
  select distinct 1 into aNum from V_DOC_AMM_COGE_RIGA c where
            c.CD_TIPO_DOCUMENTO=aCD_TIPO_DOCUMENTO
	    AND c.CD_CDS=aCD_CDS
	    AND c.CD_UNITA_ORGANIZZATIVA=aCD_UNITA_ORGANIZZATIVA
	    AND c.ESERCIZIO=aESERCIZIO
		AND c.PG_NUMERO_DOCUMENTO=aPG_NUMERO_DOCUMENTO
		AND c.ESERCIZIO_DOC = aESERCIZIO + 1;
  return 'Y';
 exception when NO_DATA_FOUND then
  return 'N';
 end;
end;
/


