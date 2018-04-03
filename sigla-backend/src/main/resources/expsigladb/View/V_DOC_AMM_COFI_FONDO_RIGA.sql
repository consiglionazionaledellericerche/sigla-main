--------------------------------------------------------
--  DDL for View V_DOC_AMM_COFI_FONDO_RIGA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_COFI_FONDO_RIGA" ("CD_TIPO_DOCUMENTO_AMM", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "ESERCIZIO", "PG_DOCUMENTO", "CD_CDS_OBBLIGAZIONE", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "IM_SCADENZA") AS 
  (
select distinct
--
-- Date: 18/07/2006
-- Version: 1.7
--
-- Vista di estrazione dei dettagli di documento amministrativo con valorizzazioni a fini COFI tramite FONDO ECONOMALE
--
-- History:
--
-- Date: 14/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 15/05/2002
-- Version: 1.1
-- Vengono filtrate note di credito debito e fatture collegate a note
--
-- Date: 17/06/2002
-- Version: 1.2
-- Aggiunta gestione COMPENSO e MISSIONE
--
-- Date: 18/06/2002
-- Version: 1.3
-- Aggiunto ANTICIPO
--
-- Date: 15/10/2002
-- Version: 1.4
-- Fix join tra testata e righe del doc generico
--
-- Date: 06/11/2002
-- Version: 1.5
-- Tolto Istituzionale/Commerciale dall'interfaccia della vista perchè non rilevante ai fini dell'estrazione righe doamm per fondo
--
-- Date: 16/04/2002
-- Version: 1.6
-- Fix recupero docamm e scadenze di obb. collegate (due righe che insistono sulla stessa scadenza contano 1)
--
-- Date: 18/07/2006
-- Version: 1.7
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
     'FATTURA_P'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.CD_CDS_ORIGINE
    ,a.CD_UO_ORIGINE
    ,a.ESERCIZIO
    ,a.PG_FATTURA_PASSIVA
    ,b.CD_CDS_OBBLIGAZIONE
    ,b.ESERCIZIO_OBBLIGAZIONE
    ,b.ESERCIZIO_ORI_OBBLIGAZIONE
    ,b.PG_OBBLIGAZIONE
    ,b.PG_OBBLIGAZIONE_SCADENZARIO
	,c.im_scadenza
from FATTURA_PASSIVA a, FATTURA_PASSIVA_RIGA b, OBBLIGAZIONE_SCADENZARIO c
where
     b.cd_cds = a.cd_cds
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.esercizio = a.esercizio
 and a.ti_fattura='F'
 and b.pg_fattura_passiva = a.pg_fattura_passiva
 and c.cd_cds = b.cd_cds_obbligazione
 and c.esercizio = b.esercizio_obbligazione
 and c.esercizio_originale = b.esercizio_ori_obbligazione
 and c.pg_obbligazione = b.pg_obbligazione
 and c.pg_obbligazione_scadenzario = b.pg_obbligazione_scadenzario
 and not exists ( -- Esclude fatture collegate a note di credito o debito
  select 1 from fattura_passiva_riga where
      CD_CDS_ASSNCNA_FIN = a.cd_cds
  and CD_UO_ASSNCNA_FIN = a.cd_unita_organizzativa
  and ESERCIZIO_ASSNCNA_FIN = a.esercizio
  and PG_FATTURA_ASSNCNA_FIN=a.pg_fattura_passiva
 )
union all
 select distinct -- Estrazione documento generico
     a.cd_tipo_documento_amm
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.CD_CDS_ORIGINE
    ,a.CD_UO_ORIGINE
    ,a.ESERCIZIO
    ,a.PG_DOCUMENTO_GENERICO
    ,b.CD_CDS_OBBLIGAZIONE
    ,b.ESERCIZIO_OBBLIGAZIONE
    ,b.ESERCIZIO_ORI_OBBLIGAZIONE
    ,b.PG_OBBLIGAZIONE
    ,b.PG_OBBLIGAZIONE_SCADENZARIO
    ,c.IM_SCADENZA
from DOCUMENTO_GENERICO a, DOCUMENTO_GENERICO_RIGA b, OBBLIGAZIONE_SCADENZARIO c
where
     b.cd_cds = a.cd_cds
 and a.cd_tipo_documento_amm in ('GENERICO_S')
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.esercizio = a.esercizio
 and b.pg_documento_generico = a.pg_documento_generico
 and b.cd_tipo_documento_amm = a.cd_tipo_documento_amm
 and c.cd_cds = b.cd_cds_obbligazione
 and c.esercizio = b.esercizio_obbligazione
 and c.esercizio_originale = b.esercizio_ori_obbligazione
 and c.pg_obbligazione = b.pg_obbligazione
 and c.pg_obbligazione_scadenzario = b.pg_obbligazione_scadenzario
union all
 select -- Estrazione documento generico
     'MISSIONE'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_MISSIONE
    ,a.CD_CDS_OBBLIGAZIONE
    ,a.ESERCIZIO_OBBLIGAZIONE
    ,a.ESERCIZIO_ORI_OBBLIGAZIONE
    ,a.PG_OBBLIGAZIONE
    ,a.PG_OBBLIGAZIONE_SCADENZARIO
    ,c.IM_SCADENZA
from MISSIONE_RIGA a, OBBLIGAZIONE_SCADENZARIO c
where
     c.cd_cds = a.cd_cds_obbligazione
 and c.esercizio = a.esercizio_obbligazione
 and c.esercizio_originale = a.esercizio_ori_obbligazione
 and c.pg_obbligazione = a.pg_obbligazione
 and c.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
union all
 select -- Estrazione documento generico
     'ANTICIPO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_ANTICIPO
    ,a.CD_CDS_OBBLIGAZIONE
    ,a.ESERCIZIO_OBBLIGAZIONE
    ,a.ESERCIZIO_ORI_OBBLIGAZIONE
    ,a.PG_OBBLIGAZIONE
    ,a.PG_OBBLIGAZIONE_SCADENZARIO
    ,c.IM_SCADENZA
from ANTICIPO a, OBBLIGAZIONE_SCADENZARIO c
where
     c.cd_cds = a.cd_cds_obbligazione
 and c.esercizio = a.esercizio_obbligazione
 and c.esercizio_originale = a.esercizio_ori_obbligazione
 and c.pg_obbligazione = a.pg_obbligazione
 and c.pg_obbligazione_scadenzario = a.pg_obbligazione_scadenzario
);

   COMMENT ON TABLE "V_DOC_AMM_COFI_FONDO_RIGA"  IS 'Vista di estrazione dei dettagli di documento amministrativo con valorizzazioni a fini COFI tramite FONDO ECONOMALE';
