--------------------------------------------------------
--  DDL for View V_DOC_AMM_COGE_TSTA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_COGE_TSTA" ("CD_TIPO_DOCUMENTO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "PG_NUMERO_DOCUMENTO", "TI_FATTURA", "FL_CONGELATA", "DT_DA_COMPETENZA_COGE", "DT_A_COMPETENZA_COGE", "TI_CAUSALE_EMISSIONE", "FL_INTRA_UE", "FL_SAN_MARINO_CON_IVA", "FL_SAN_MARINO_SENZA_IVA", "FL_ASSOCIATO_COMPENSO", "DT_REGISTRAZIONE", "DT_SCADENZA", "CD_TERZO", "IM_TOTALE_IMPONIBILE_DIVISA", "IM_TOTALE_IMPONIBILE", "IM_TOTALE_IVA", "IM_TOTALE_FATTURA", "CD_DIVISA", "CAMBIO", "STATO_COGE", "STATO_COAN", "STATO_COFI", "STATO_PAGAMENTO_FONDO_ECO", "TI_BENE_SERVIZIO", "TI_ISTITUZ_COMMERC", "ESERCIZIO_LETTERA", "PG_LETTERA", "IM_PAGAMENTO", "IM_COMMISSIONI", "UTUV", "DUVA") AS 
  (
select
--
-- Date: 09/09/2003
-- Version: 3.3
--
-- Vista di estrazione della testata dei documenti amministrativi con valorizzazioni a fini COGE
--
-- History:
--
-- Date: 16/02/2002
-- Version: 1.0
-- Creazione
--
-- Date: 04/03/2002
-- Version: 1.1
-- Introduzione del documento generico
--
-- Date: 07/03/2002
-- Version: 1.2
-- Eliminazione della join con la tabella TIPO_DOCUMENTO_AMM
--
-- Date: 09/03/2002
-- Version: 1.3
-- Introduzione del campo STATO_COGE, FL_MODIFICA_COGE
--
-- Date: 06/05/2002
-- Version: 1.4
-- Estrazione UO e CDS origine
--
-- Date: 07/05/2002
-- Version: 1.5
-- Sdoppiamento stati coge/coan
--
-- Date: 08/05/2002
-- Version: 1.6
-- Aggiunto stato di collegamento a fondo economale
--
-- Date: 15/05/2002
-- Version: 1.7
-- Aggiunto il COMPENSO
--
-- Date: 16/05/2002
-- Version: 1.8
-- Aggiunti flag intra ue e san marino per la non contab. econ. IVA sulla fattura passiva
--
-- Date: 20/05/2002
-- Version: 1.9
-- Aggiunta causale emissione su fatture attive per determinare gli impatti economici di vendita di bene durevole (inv.)
--
-- Date: 30/05/2002
-- Version: 2.0
-- Aggiunta gestione dell'anticipo su missione
--
-- Date: 31/05/2002
-- Version: 2.1
-- Aggiunta gestione lettera
--
-- Date: 31/05/2002
-- Version: 2.2
-- Estrazione informazioni sulla lettera di pag. esetero
--
-- Date: 06/06/2002
-- Version: 2.3
-- Aggiunto lo stato COFI in interfaccia
--
-- Date: 07/06/2002
-- Version: 2.4
-- introduzione della missione e rimborso
--
-- Date: 14/06/2002
-- Version: 2.5
-- Aggiunto flag "associato compenso"
--
-- Date: 17/07/2002
-- Version: 2.6
-- Estrazione di duva e utuv del documento
--
-- Date: 29/07/2002
-- Version: 2.7
-- Fix estrazione stato COAN
--
-- Date: 18/10/2002
-- Version: 2.8
-- Corretto tipo di documento RIMBORSO
--
-- Date: 10/02/2003
-- Version: 2.9
-- Estrazione dei soli documenti amministrativi con PG maggiore di 0
--
-- Date: 10/07/2003
-- Version: 3.0
-- Aggiunta la data da-a competenza coge della testata del documento
--
-- Date: 07/08/2003
-- Version: 3.1
-- Aggiunto il tipo bene servizio per la fattura passiva
--
-- Date: 07/08/2003
-- Version: 3.2
-- Estratti ti_istituz_commerc (per doc che lo supportano)/fl_san_marino_senza_iva (fatture passive)
--
-- Date: 09/09/2003
-- Version: 3.3
-- Estratto il campo fl_congelata per le fatture
--
-- Body:
--
     'FATTURA_P'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
	,a.CD_CDS_ORIGINE
	,a.CD_UO_ORIGINE
    ,a.PG_FATTURA_PASSIVA
    ,a.TI_FATTURA
	,a.FL_CONGELATA
	,a.dt_da_competenza_coge
	,a.dt_a_competenza_coge
    ,null
    ,a.fl_intra_ue
	,a.fl_san_marino_con_iva
	,a.fl_san_marino_senza_iva
    ,'N'
    ,a.DT_REGISTRAZIONE
    ,a.DT_SCADENZA
    ,a.CD_TERZO
    ,a.IM_TOTALE_IMPONIBILE_DIVISA
    ,a.IM_TOTALE_IMPONIBILE
    ,a.IM_TOTALE_IVA
    ,a.IM_TOTALE_FATTURA
    ,a.CD_DIVISA
    ,a.CAMBIO
	,a.STATO_COGE
	,a.STATO_COAN
	,a.STATO_COFI
	,a.STATO_PAGAMENTO_FONDO_ECO
	,a.TI_BENE_SERVIZIO
	,a.TI_ISTITUZ_COMMERC
	,a.ESERCIZIO_LETTERA
	,a.PG_LETTERA
    ,b.im_pagamento
    ,b.im_commissioni
	,a.utuv
	,a.duva
from FATTURA_PASSIVA a, LETTERA_PAGAM_ESTERO b
 where
       b.cd_cds(+)= a.cd_cds
   and b.cd_unita_organizzativa(+)=a.cd_unita_organizzativa
   and b.esercizio(+)=a.esercizio_lettera
   and b.pg_lettera(+)=a.pg_lettera
   and a.pg_fattura_passiva >= 0
union all
  select
     'FATTURA_A'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
	,a.CD_CDS_ORIGINE
	,a.CD_UO_ORIGINE
    ,a.PG_FATTURA_ATTIVA
    ,a.TI_FATTURA
	,a.FL_CONGELATA
	,a.dt_da_competenza_coge
	,a.dt_a_competenza_coge
    ,a. TI_CAUSALE_EMISSIONE
    ,'N'
    ,'N'
	,'N'
	,'N'
    ,a.DT_REGISTRAZIONE
    ,a.DT_SCADENZA
    ,a.CD_TERZO
    ,0 -- a.IM_TOTALE_IMPONIBILE_DIVISA
    ,a.IM_TOTALE_IMPONIBILE
    ,a.IM_TOTALE_IVA
    ,a.IM_TOTALE_FATTURA
    ,a.CD_DIVISA
    ,a.CAMBIO
	,a.STATO_COGE
	,a.STATO_COAN
	,a.STATO_COFI
	,'N' -- fattura attiva non gestita tramite fondo economale
	,null
	,'C'
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,a.utuv
	,a.duva
from FATTURA_ATTIVA a
   where a.pg_fattura_attiva >= 0
union all
  select -- Seleziona GENERICO
     a.cd_tipo_documento_amm
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
	,a.CD_CDS_ORIGINE
	,a.CD_UO_ORIGINE
    ,a.PG_DOCUMENTO_GENERICO
    ,null  -- ti_fattura
    ,null -- fl_congelata
	,a.dt_da_competenza_coge
	,a.dt_a_competenza_coge
    ,null  -- ti_causale_emissione
    ,'N'
	,'N'
    ,'N'
    ,'N'
    ,a.DATA_REGISTRAZIONE
    ,TO_DATE(null)  -- dt_scadenza
    ,0
    ,0
    ,a.IM_TOTALE
    ,0
    ,a.IM_TOTALE
    ,a.CD_DIVISA
    ,a.CAMBIO
	,a.STATO_COGE
	,a.STATO_COAN
	,a.STATO_COFI
	,a.STATO_PAGAMENTO_FONDO_ECO
	,null
	,a.TI_ISTITUZ_COMMERC
    ,a.esercizio_lettera
	,a.pg_lettera
    ,b.im_pagamento
    ,b.im_commissioni
	,a.utuv
	,a.duva
from DOCUMENTO_GENERICO a, lettera_pagam_estero b where
       b.cd_cds(+)=a.cd_cds
   and b.cd_unita_organizzativa(+)=a.cd_unita_organizzativa
   and b.esercizio(+)=a.esercizio_lettera
   and b.pg_lettera(+)=a.pg_lettera
   and a.pg_documento_generico >= 0
union all
  select -- Seleziona COMPENSO
     'COMPENSO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
	,a.CD_CDS_ORIGINE
	,a.CD_UO_ORIGINE
    ,a.PG_COMPENSO
    ,null -- ti_fattura
    ,null -- fl_congelata
	,a.dt_da_competenza_coge
	,a.dt_a_competenza_coge
    ,null -- ti_causale_emissione
    ,'N'
	,'N'
	,'N'
	,'N'
    ,a.DT_REGISTRAZIONE
    ,TO_DATE(null)
    ,cd_terzo
    ,0
    ,a.IM_TOTALE_COMPENSO
    ,0
    ,a.IM_TOTALE_COMPENSO
    ,null -- divisa
    ,0 -- cambio
	,a.STATO_COGE
	,a.STATO_COAN
	,a.STATO_COFI
	,a.STATO_PAGAMENTO_FONDO_ECO
	,null
    ,a.TI_ISTITUZ_COMMERC
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,a.utuv
	,a.duva
from COMPENSO a
   where a.pg_compenso >= 0
union all
  select -- Seleziona ANTICIPO
     'ANTICIPO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
	,a.CD_CDS_ORIGINE
	,a.CD_UO_ORIGINE
    ,a.PG_ANTICIPO
    ,null -- ti_fattura
    ,null -- fl_congelata
	,a.dt_da_competenza_coge
	,a.dt_a_competenza_coge
    ,null -- ti_causale_emissione
    ,'N'
	,'N'
	,'N'
	,'N'
    ,a.DT_REGISTRAZIONE
    ,TO_DATE(null)
    ,cd_terzo
    ,a.IM_ANTICIPO_DIVISA
    ,a.IM_ANTICIPO
    ,0
    ,a.IM_ANTICIPO
    ,a.CD_DIVISA
    ,a.CAMBIO
	,a.STATO_COGE
	,a.STATO_COAN
	,a.STATO_COFI
	,a.STATO_PAGAMENTO_FONDO_ECO
	,null
	,null
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,a.utuv
	,a.duva
from ANTICIPO a
   where a.pg_anticipo >= 0
union all
  select -- Seleziona RIMBORSO
     'RIMBORSO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
	,a.CD_CDS_ORIGINE
	,a.CD_UO_ORIGINE
    ,a.PG_RIMBORSO
    ,null -- ti_fattura
    ,null -- fl_congelata
	,a.dt_da_competenza_coge
	,a.dt_a_competenza_coge
    ,null -- ti_causale_emissione
    ,'N'
	,'N'
	,'N'
	,'N'
    ,a.DT_REGISTRAZIONE
    ,TO_DATE(null)
    ,cd_terzo
    ,0
    ,a.IM_RIMBORSO
    ,0
    ,a.IM_RIMBORSO
    ,null -- divisa
    ,0 -- cambio
	,a.STATO_COGE
	,a.STATO_COAN
	,a.STATO_COFI
	,'N' -- rimborso non gestito tramite fondo economale
	,null
	,null
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,a.utuv
	,a.duva
from RIMBORSO a
   where a.pg_rimborso >= 0
union all
  select -- Seleziona MISSIONE
     'MISSIONE'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
	,a.CD_CDS
	,a.CD_UNITA_ORGANIZZATIVA
    ,a.PG_MISSIONE
    ,null -- ti_fattura
    ,null -- fl_congelata
	,a.dt_inizio_missione
	,a.dt_fine_missione
    ,null -- ti_causale_emissione
    ,'N'
	,'N'
	,'N'
	,FL_ASSOCIATO_COMPENSO
    ,a.DT_REGISTRAZIONE
    ,TO_DATE(null)
    ,cd_terzo
    ,a.IM_TOTALE_MISSIONE
    ,a.IM_TOTALE_MISSIONE
    ,0
    ,a.IM_TOTALE_MISSIONE
    ,null -- divisa
    ,0 -- cambio
	,a.STATO_COGE
	,a.STATO_COAN
	,a.STATO_COFI
	,a.STATO_PAGAMENTO_FONDO_ECO
	,null
	,a.TI_ISTITUZ_COMMERC
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,TO_NUMBER(null)
	,a.utuv
	,a.duva
from MISSIONE a
   where a.pg_missione >= 0
) ;

   COMMENT ON TABLE "V_DOC_AMM_COGE_TSTA"  IS 'Vista di estrazione della testata dei documenti amministrativi con valorizzazioni a fini COGE';
