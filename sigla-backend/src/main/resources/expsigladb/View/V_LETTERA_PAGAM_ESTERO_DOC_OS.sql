--------------------------------------------------------
--  DDL for View V_LETTERA_PAGAM_ESTERO_DOC_OS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LETTERA_PAGAM_ESTERO_DOC_OS" ("ESERCIZIO", "CD_CDS", "TI_APPARTENENZA", "TI_GESTIONE", "CD_VOCE", "COMPETENZE_RESIDUI", "IM_VOCE") AS 
  SELECT
--
-- Date: 18/07/2006
-- Version: 1.2
--
-- Estrae informazioni sul numero di documento, importo spaccato per dettaglio obbligazione e stato del documento collegato alla lettera di pagamento estero
-- che puo essere una fattura passiva o un generico di spesa
--
-- History:
--
-- Date: 27/01/2003
-- Version: 1.0
-- Creazione
--
-- Date: 07/03/2003
-- Version: 1.1
-- Fix su errore di estrazione competenza/residui (gli impegni 2003 hanno il flag riportato a N)
-- Filtro solo fatture non annullate
--
-- Date: 18/07/2006
-- Version: 1.2
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
 ESERCIZIO,
 CD_CDS,
 TI_APPARTENENZA,
 TI_GESTIONE,
 CD_VOCE,
 COMPETENZE_RESIDUI,
 sum(IM_VOCE)
from (select
 b.ESERCIZIO,
 b.CD_CDS,
-- b.CD_UNITA_ORGANIZZATIVA,
-- a.PG_LETTERA,
-- 'FATTURA_P',
-- a.PG_FATTURA_PASSIVA,
-- a.STATO_COFI,
 d.ti_appartenenza,
 d.ti_gestione,
 d.cd_voce,
 decode(e.cd_tipo_documento_cont ,'IMP_RES','R','C') competenze_residui,
 d.im_voce
from
 lettera_pagam_estero b, fattura_passiva a, fattura_passiva_riga c, obbligazione_scad_voce d, obbligazione e
where
     a.esercizio_lettera = b.esercizio
 and a.cd_cds = b.cd_cds
 and a.cd_unita_organizzativa = b.cd_unita_organizzativa
 and a.pg_lettera = b.pg_lettera
 and c.cd_cds = a.cd_cds
 and c.cd_unita_organizzativa = a.cd_unita_organizzativa
 and c.esercizio = a.esercizio
 and c.pg_fattura_passiva = a.pg_fattura_passiva
 and d.cd_cds = c.cd_cds_obbligazione
 and d.esercizio = c.esercizio_obbligazione
 and d.esercizio_originale = c.esercizio_ori_obbligazione
 and d.pg_obbligazione = c.pg_obbligazione
 and d.pg_obbligazione_scadenzario = c.pg_obbligazione_scadenzario
 and e.cd_cds = d.cd_cds
 and e.esercizio = d.esercizio
 and e.esercizio_originale = d.esercizio_originale
 and e.pg_obbligazione = d.pg_obbligazione
 and a.stato_cofi not in ('P','A') -- non pagate non annullate
union all
select
 b.ESERCIZIO,
 b.CD_CDS,
 d.ti_appartenenza,
 d.ti_gestione,
 d.cd_voce,
 decode(e.cd_tipo_documento_cont ,'IMP_RES','R','C') competenze_residui,
 d.im_voce
from
 lettera_pagam_estero b, documento_generico a,documento_generico_riga c, obbligazione_scad_voce d, obbligazione e
where
     a.esercizio_lettera = b.esercizio
 and a.cd_cds = b.cd_cds
 and a.cd_unita_organizzativa = b.cd_unita_organizzativa
 and a.pg_lettera = b.pg_lettera
 and c.cd_cds = a.cd_cds
 and c.cd_unita_organizzativa = a.cd_unita_organizzativa
 and c.esercizio = a.esercizio
 and c.cd_tipo_documento_amm = a.cd_tipo_documento_amm
 and c.pg_documento_generico = a.pg_documento_generico
 and d.cd_cds = c.cd_cds_obbligazione
 and d.esercizio = c.esercizio_obbligazione
 and d.esercizio_originale = c.esercizio_ori_obbligazione
 and d.pg_obbligazione = c.pg_obbligazione
 and d.pg_obbligazione_scadenzario = c.pg_obbligazione_scadenzario
 and e.cd_cds = d.cd_cds
 and e.esercizio = d.esercizio
 and e.esercizio_originale = d.esercizio_originale
 and e.pg_obbligazione = d.pg_obbligazione
 and a.stato_cofi not in ('P','A') -- non pagate non annullate
)
group by
 ESERCIZIO,
 CD_CDS,
 TI_APPARTENENZA,
 TI_GESTIONE,
 CD_VOCE,
 COMPETENZE_RESIDUI;

   COMMENT ON TABLE "V_LETTERA_PAGAM_ESTERO_DOC_OS"  IS 'Estrae informazioni sul numero di documento, importo spaccato per dettaglio obbligazione e stato del documento collegato alla lettera di pagamento estero
che puo essere una fattura passiva o un generico di spesa';
