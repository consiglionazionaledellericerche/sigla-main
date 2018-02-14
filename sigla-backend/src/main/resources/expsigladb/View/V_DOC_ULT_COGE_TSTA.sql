--------------------------------------------------------
--  DDL for View V_DOC_ULT_COGE_TSTA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_ULT_COGE_TSTA" ("ESERCIZIO", "CD_CDS", "TI_APPARTENENZA", "CD_UNITA_ORGANIZZATIVA", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "CD_TIPO_DOCUMENTO_CONT", "TI_MAN_REV", "CD_TERZO", "STATO", "STATO_COGE", "PG_DOCUMENTO_CONT", "DT_EMISSIONE_DOCUMENTO_CONT", "DT_ESITO_DOCUMENTO_CONT", "IM_DOCUMENTO") AS 
  (
select
--
-- Date: 15/01/2003
-- Version: 1.8
--
-- Vista di estrazione della testata dei documenti autorizzatori a fini COGE
--
-- History:
--
-- Date: 05/03/2002
-- Version: 1.0
-- Creazione
--
-- Date: 06/05/2002
-- Version: 1.1
-- Estrazione di UO e CDS origine
--
-- Date: 08/05/2002
-- Version: 1.2
-- Imposta condizione su stato di pagamento del mandato
--
-- Date: 28/05/2002
-- Version: 1.3
-- Estrazione dello stato del mandato
--
-- Date: 30/05/2002
-- Version: 1.4
-- Aggiunto campo ti_man_rev per discriminare tra tipi  di mandati e reversali
-- Aggiunto stato_coge
--
-- Date: 31/05/2002
-- Version: 1.5
-- Identificazione appartenenza del documento
--
-- Date: 06/06/2002
-- Version: 1.6
-- Aggiunta data emissione/pagamento
--
-- Date: 03/07/2002
-- Version: 1.7
-- Fix errore di estrazione reversali ente
--
-- Date: 15/01/2003
-- Version: 1.8
-- Estratto il terzo del mandato
--
-- Body:
--
a.esercizio,
a.cd_cds,
decode(b.cd_tipo_unita,'ENTE','C','D'),
a.cd_unita_organizzativa,
a.CD_CDS_ORIGINE,
a.CD_UO_ORIGINE,
a.cd_tipo_documento_cont,
a.ti_mandato,
c.cd_terzo,
a.stato,
a.stato_coge,
a.pg_mandato,
a.dt_emissione,
a.dt_pagamento,
a.im_mandato
from mandato a, unita_organizzativa b, mandato_terzo c
where
     b.cd_unita_organizzativa = a.cd_cds
 and c.cd_cds = a.cd_cds
 and c.esercizio = a.esercizio
 and c.pg_mandato = a.pg_mandato
union
select
a.esercizio,
a.cd_cds,
decode(b.cd_tipo_unita,'ENTE','C','D'),
a.cd_unita_organizzativa,
a.CD_CDS_ORIGINE,
a.CD_UO_ORIGINE,
a.cd_tipo_documento_cont,
a.ti_reversale,
c.cd_terzo,
a.stato,
a.stato_coge,
a.pg_reversale,
a.dt_emissione,
a.dt_incasso,
a.im_reversale
from reversale a, unita_organizzativa b, reversale_terzo c
where
     b.cd_unita_organizzativa = a.cd_cds
 and c.cd_cds = a.cd_cds
 and c.esercizio = a.esercizio
 and c.pg_reversale = a.pg_reversale
)
;

   COMMENT ON TABLE "V_DOC_ULT_COGE_TSTA"  IS 'Vista di estrazione della testata dei documenti autorizzatori a fini COGE';
