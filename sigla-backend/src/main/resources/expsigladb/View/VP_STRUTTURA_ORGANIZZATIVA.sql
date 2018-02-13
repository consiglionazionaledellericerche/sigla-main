--------------------------------------------------------
--  DDL for View VP_STRUTTURA_ORGANIZZATIVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_STRUTTURA_ORGANIZZATIVA" ("ESERCIZIO", "CD_CDS", "CD_STO", "DS_STO", "CD_PROPRIO_STO", "CD_TIPO_UNITA", "DS_TIPO_UNITA", "FL_UO_CDS", "FL_RUBRICA", "FL_CDS", "LIVELLO", "PRC_COPERTURA_OBBLIG_2", "PRC_COPERTURA_OBBLIG_3", "CD_RESPONSABILE", "DS_RESPONSABILE", "CD_RESPONSABILE_AMM", "DS_RESPONSABILE_AMM", "CD_AREA_RICERCA", "INDIRIZZO", "CD_CDR_AFFERENZA") AS 
  (  
select  
--  
-- Date: 04/12/2001  
-- Version: 1.2  
--  
-- Vista di stampa della struttura organizzativa CDS/UO/CDR  
-- La vista controlla la validita di STO  
--
-- History:  
--  
-- Date: 13/11/2001  
-- Version: 1.0  
-- Carezione  
--  
-- Date: 23/11/2001  
-- Version: 1.1  
-- Eliminazione esercizio da STO
--
-- Date: 04/12/2001  
-- Version: 1.2  
-- Aggiunto il codice del CDS di afferenza
--
-- Body:  
--  
 a.ESERCIZIO
,a.CD_UNITA_ORGANIZZATIVA  
,a.CD_UNITA_ORGANIZZATIVA  
,a.DS_UNITA_ORGANIZZATIVA  
,a.CD_PROPRIO_UNITA  
,a.CD_TIPO_UNITA  
,b.DS_TIPO_UNITA  
,'N'  
,'N'  
,a.FL_CDS  
,a.LIVELLO  
,a.PRC_COPERTURA_OBBLIG_2  
,a.PRC_COPERTURA_OBBLIG_3  
,a.CD_RESPONSABILE  
,c.NOME||' '||c.COGNOME  
,0  
,null  
,null  
,null  
,null  
from V_UNITA_ORGANIZZATIVA_VALIDA a, TIPO_UNITA_ORGANIZZATIVA b, V_ANAGRAFICO_TERZO c where  
    a.fl_cds = 'Y'  
and a.cd_tipo_unita = b.cd_tipo_unita  
and c.cd_terzo = a.cd_responsabile  
union  
select  
 a.ESERCIZIO
,a.CD_UNITA_PADRE  
,a.CD_UNITA_ORGANIZZATIVA  
,a.DS_UNITA_ORGANIZZATIVA  
,a.CD_PROPRIO_UNITA  
,a.CD_TIPO_UNITA  
,b.DS_TIPO_UNITA  
,a.FL_UO_CDS  
,a.FL_RUBRICA  
,'N'  
,a.LIVELLO  
,0  
,0  
,a.CD_RESPONSABILE  
,c.NOME||' '||c.COGNOME  
,a.CD_RESPONSABILE_AMM  
,d.NOME||' '||d.COGNOME  
,a.CD_AREA_RICERCA  
,null  
,null  
from V_UNITA_ORGANIZZATIVA_VALIDA a, 
     TIPO_UNITA_ORGANIZZATIVA b, 
     V_ANAGRAFICO_TERZO c, 
     V_ANAGRAFICO_TERZO d
where  
    a.fl_cds = 'N'  
and a.cd_tipo_unita = b.cd_tipo_unita  
and c.cd_terzo = a.cd_responsabile  
and d.cd_terzo(+) = a.cd_responsabile_amm  
union  
select  
 a.ESERCIZIO 
,d.CD_UNITA_PADRE  
,a.CD_CENTRO_RESPONSABILITA  
,a.DS_CDR  
,a.CD_PROPRIO_CDR  
,'CDR'  
,'Centro di responsabilita'  
,'N'  
,'N'  
,'N'  
,10+a.LIVELLO  
,0  
,0  
,a.CD_RESPONSABILE  
,c.NOME||' '||c.COGNOME  
,0  
,''  
,null  
,a.INDIRIZZO  
,a.CD_CDR_AFFERENZA  
from 
 V_CDR_VALIDO a, 
 V_ANAGRAFICO_TERZO c, 
 UNITA_ORGANIZZATIVA d -- Unita organizzativa di afferenza
where  
     c.cd_terzo = a.cd_responsabile  
 and d.cd_unita_organizzativa = a.cd_unita_organizzativa
);

   COMMENT ON TABLE "VP_STRUTTURA_ORGANIZZATIVA"  IS 'Vista di stampa della struttura organizzativa CDS/UO/CDR
La vista controlla la validita di STO';
