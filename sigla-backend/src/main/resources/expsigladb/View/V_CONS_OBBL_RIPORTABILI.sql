--------------------------------------------------------
--  DDL for View V_CONS_OBBL_RIPORTABILI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_OBBL_RIPORTABILI" ("CD_LINEA_ATTIVITA", "CD_PROGETTO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CENTRO_RESPONSABILITA", "ESERCIZIO_ORIGINALE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "ESERCIZIO", "STATO_OBBLIGAZIONE", "RIPORTATO", "DS_OBBLIGAZIONE", "IM_SCADVOCE", "CD_ELEMENTO_VOCE") AS 
  select
--
-- Date: 09/11/2006
-- Version: 1.2
--
-- Consultazione obbligazioni riportabili
--
--
-- History:
--
-- Date: 16/6/2005
-- Version: 1.0
-- Creazione
--
-- Date: 18/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 09/11/2006
-- Version: 1.2
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
osv.CD_LINEA_ATTIVITA, pg.CD_PROGETTO, o.CD_CDS, o.CD_UNITA_ORGANIZZATIVA, osv.CD_CENTRO_RESPONSABILITA,
o.ESERCIZIO_ORIGINALE, o.PG_OBBLIGAZIONE, os.PG_OBBLIGAZIONE_SCADENZARIO, o.ESERCIZIO,
o.STATO_OBBLIGAZIONE, o.RIPORTATO, o.DS_OBBLIGAZIONE, NVL(osv.IM_VOCE, os.IM_SCADENZA), o.CD_ELEMENTO_VOCE
from
obbligazione_scadenzario os, obbligazione o, obbligazione_scad_voce osv, linea_attivita la, progetto_gest pg
where os.IM_ASSOCIATO_DOC_CONTABILE < os.IM_SCADENZA
and o.cd_cds = os.cd_cds
and o.esercizio = os.esercizio
and o.esercizio_originale = os.esercizio_originale
and o.pg_obbligazione = os.pg_obbligazione
and o.dt_cancellazione is null
and o.stato_obbligazione <> 'S'
AND o.cd_tipo_documento_cont in( 'OBB','OBB_RESIM')
And o.pg_obbligazione > 0
and os.cd_cds = osv.cd_cds (+)
and os.esercizio = osv.esercizio (+)
and os.esercizio_originale = osv.esercizio_originale (+)
and os.pg_obbligazione = osv.pg_obbligazione (+)
and os.pg_obbligazione_scadenzario = osv.pg_obbligazione_scadenzario (+)
and nvl(osv.cd_centro_responsabilita, '.') = la.cd_centro_responsabilita (+)
and nvl(osv.cd_linea_attivita, '.') = la.cd_linea_attivita (+)
and nvl(la.pg_progetto, -1) = pg.pg_progetto (+)
And (pg.esercizio Is Null Or pg.esercizio = o.esercizio);
