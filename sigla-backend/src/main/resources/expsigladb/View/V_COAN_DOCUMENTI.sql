--------------------------------------------------------
--  DDL for View V_COAN_DOCUMENTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_COAN_DOCUMENTI" ("CD_TIPO_DOCUMENTO_AMM", "CD_TERZO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "PG_NUMERO_DOCUMENTO", "TI_FATTURA", "IM_TOTALE_FATTURA", "IM_TOTALE_IMPONIBILE", "IM_TOTALE_IVA", "STATO_COFI", "TI_ISTITUZ_COMMERC") AS 
  SELECT
--
-- Date: 28/01/2004
-- Version: 1.9
--
-- Vista di estrazione dei documenti gestiti nella movimentazione coan
--
-- History:
--
-- Date: 03/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 07/05/2002
-- Version: 1.1
-- Eliminazione dalla vista della Join con la tabella scrittura_analitica
--
-- Date: 07/05/2002
-- Version: 1.2
-- Sdoppiamento stati coge/coan
--
-- Date: 28/05/2002
-- Version: 1.3
-- Aggiunto cds e uo origine del documento
--
-- Date: 04/06/2002
-- Version: 1.4
-- Introduzione Compensi
--
-- Date: 29/07/2002
-- Version: 1.6
-- Introduzione di MIssione
--
-- Date: 09/09/2003
-- Version: 1.7
-- Totale compenso estratto in valore assoluto
--
-- Date: 27/01/2004
-- Version: 1.8
-- Introduzione dello stato cofi su vista
--
-- Date: 28/01/2004
-- Version: 1.9
-- Controllo che pg del documento sia > 0 (come per l'economica)
--
-- Date: 03/05/2005
-- Version:
-- Aggiunto il campo Istituzionale/Commerciale per archiviazione saldi
--
-- Body:
--
       'FATTURA_A' CD_TIPO_DOCUMENTO_AMM
       ,fat.CD_TERZO
    ,fat.CD_CDS
    ,fat.CD_UNITA_ORGANIZZATIVA
    ,fat.ESERCIZIO
       ,fat.CD_CDS_ORIGINE
       ,fat.CD_UO_ORIGINE
    ,fat.PG_FATTURA_ATTIVA PG_NUMERO_DOCUMENTO
    ,fat.TI_FATTURA
    ,fat.IM_TOTALE_FATTURA
    ,fat.IM_TOTALE_IMPONIBILE
    ,fat.IM_TOTALE_IVA
    ,fat.STATO_COFI
    ,'C' TI_ISTITUZ_COMMERC
 from fattura_attiva fat
 WHERE     FAT.STATO_COAN IN ('N','R')
       AND FAT.pg_fattura_attiva > 0
 UNION
 select 'FATTURA_P' CD_TIPO_DOCUMENTO_AMM
        ,fat.CD_TERZO
        ,fat.CD_CDS
        ,fat.CD_UNITA_ORGANIZZATIVA
        ,fat.ESERCIZIO
           ,fat.CD_CDS_ORIGINE
           ,fat.CD_UO_ORIGINE
        ,fat.PG_FATTURA_PASSIVA PG_NUMERO_DOCUMENTO
     ,fat.TI_FATTURA
        ,fat.IM_TOTALE_FATTURA
        ,fat.IM_TOTALE_IMPONIBILE
           ,fat.IM_TOTALE_IVA
        ,fat.STATO_COFI
     ,FAT.TI_ISTITUZ_COMMERC
 from fattura_passiva fat
 WHERE     FAT.STATO_COAN IN ('N','R')
       AND FAT.pg_fattura_passiva > 0
 UNION
 select fat.CD_TIPO_DOCUMENTO_AMM CD_TIPO_DOCUMENTO_AMM
        ,TO_NUMBER(NULL) CD_TERZO
        ,fat.CD_CDS
        ,fat.CD_UNITA_ORGANIZZATIVA
        ,fat.ESERCIZIO
           ,fat.CD_CDS_ORIGINE
           ,fat.CD_UO_ORIGINE
        ,fat.PG_DOCUMENTO_GENERICO PG_NUMERO_DOCUMENTO
        ,null TI_FATTURA
        ,fat.IM_TOTALE
        ,0.
        ,0
        ,fat.STATO_COFI
     ,FAT.TI_ISTITUZ_COMMERC
 from documento_generico fat
 WHERE     FAT.STATO_COAN IN ('N','R')
       AND FAT.pg_documento_generico > 0
 UNION
 select 'COMPENSO' CD_TIPO_DOCUMENTO_AMM
        ,com.CD_TERZO
        ,com.CD_CDS
        ,com.CD_UNITA_ORGANIZZATIVA
        ,com.ESERCIZIO
           ,com.CD_CDS_ORIGINE
           ,com.CD_UO_ORIGINE
        ,com.PG_COMPENSO PG_NUMERO_DOCUMENTO
        ,null TI_FATTURA
        ,abs(com.IM_TOTALE_COMPENSO)
        ,com.IMPONIBILE_FISCALE
        ,com.IMPONIBILE_IVA
        ,com.STATO_COFI
     ,COM.TI_ISTITUZ_COMMERC
 from compenso com
 WHERE     COM.STATO_COAN IN ('N','R')
       AND COM.pg_compenso > 0
 UNION
 select 'MISSIONE' CD_TIPO_DOCUMENTO_AMM
        ,mis.CD_TERZO
        ,mis.CD_CDS
        ,mis.CD_UNITA_ORGANIZZATIVA
        ,mis.ESERCIZIO
           ,mis.CD_CDS
           ,mis.CD_UNITA_ORGANIZZATIVA
        ,mis.PG_MISSIONE PG_NUMERO_DOCUMENTO
        ,null TI_FATTURA
        ,mis.IM_TOTALE_MISSIONE
        ,0
        ,0
        ,mis.STATO_COFI
     ,MIS.TI_ISTITUZ_COMMERC
 from missione mis
 WHERE      MIS.STATO_COAN IN ('N','R')
        AND MIS.pg_missione > 0
;
