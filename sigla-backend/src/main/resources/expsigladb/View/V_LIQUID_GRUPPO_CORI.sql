--------------------------------------------------------
--  DDL for View V_LIQUID_GRUPPO_CORI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_GRUPPO_CORI" ("ESERCIZIO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "PG_LIQUIDAZIONE", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "PG_LIQUIDAZIONE_ORIGINE", "CD_GRUPPO_CR", "DS_GRUPPO_CR", "TERZO_VERSAMENTO", "FL_ACCENTRATO", "CD_REGIONE", "PG_COMUNE", "IM_LIQUIDATO", "PG_DOC") AS 
  select
--
-- Date: 25/02/2003
-- Version: 1.3
--
-- View di estrazione
--
-- History:
--
-- Date: 14/06/2002
-- Version: 1.0
-- Creazione vista
--
-- Date: 25/06/2002
-- Version: 1.1
-- Revisione tabelle
--
-- Date: 23/07/2002
-- Version: 1.2
-- Eliminato TIPO_CR dalla JOIN
--
-- Date: 25/02/2003
-- Version: 1.3
-- Flag accentrato letto dal liquid_gruppo_cori
--
-- Date: 15/04/2005
-- Version: 1.4
-- Aggiunto il pg_doc. Dopo aver liquidato, l'utente vede il numero del doc. creato.
--
-- Body:
--
  a.ESERCIZIO,
  a.CD_CDS,
  a.CD_UNITA_ORGANIZZATIVA,
  a.PG_LIQUIDAZIONE,
  a.CD_CDS_ORIGINE,
  a.CD_UO_ORIGINE,
  a.PG_LIQUIDAZIONE_ORIGINE,
  a.CD_GRUPPO_CR,
  b.DS_GRUPPO_CR,
  c.CD_TERZO_VERSAMENTO || ' - ' || decode(nvl(f.RAGIONE_SOCIALE,'-1'),'-1',f.COGNOME || ' ' || f.NOME,f.RAGIONE_SOCIALE),
  a.FL_ACCENTRATO,
  a.CD_REGIONE,
  a.PG_COMUNE,
  nvl(a.IM_LIQUIDATO,0),
  a.PG_DOC
from liquid_gruppo_cori a,
	 gruppo_cr b,
	 gruppo_cr_det c,
	 terzo e,
	 anagrafico f
where a.ESERCIZIO = b.ESERCIZIO
and   a.CD_GRUPPO_CR = b.CD_GRUPPO_CR
and	  a.ESERCIZIO = c.ESERCIZIO
and	  a.CD_GRUPPO_CR = c.CD_GRUPPO_CR
and	  a.CD_REGIONE = c.CD_REGIONE
and	  a.PG_COMUNE = c.PG_COMUNE
and	  c.CD_TERZO_VERSAMENTO = e.CD_TERZO
and   e.CD_ANAG = f.CD_ANAG;
