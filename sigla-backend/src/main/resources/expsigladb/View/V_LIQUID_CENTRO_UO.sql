--------------------------------------------------------
--  DDL for View V_LIQUID_CENTRO_UO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_LIQUID_CENTRO_UO" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_LIQUIDAZIONE", "CD_GRUPPO_CR", "CD_REGIONE", "PG_COMUNE", "CD_CDS_ORIGINE", "CD_UO_ORIGINE", "PG_LIQUIDAZIONE_ORIGINE", "IM_LIQUIDATO", "FL_ACCENTRATO", "CD_CDS_DOC", "ESERCIZIO_DOC", "PG_DOC", "CD_CDS_OBB_ACCENTR", "ESERCIZIO_OBB_ACCENTR", "ESERCIZIO_ORI_OBB_ACCENTR", "PG_OBB_ACCENTR", "PG_GRUPPO_CENTRO", "STATO", "DACR", "UTCR", "UTUV", "DUVA", "PG_VER_REC") AS 
  select
--
-- Date: 19/07/2006
-- Version: 1.3
--
-- View di estrazione dei dettagli CORI accentrati della liquidazione per UO
--
-- History:
--
-- Date: 21/02/2003
-- Version: 1.0
-- Creazione vista
--
-- Date: 24/02/2003
-- Version: 1.2
-- Adeguamento interfaccia a tabella liquid_gruppo_cori
--
-- Date: 19/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Body:
--
 gc.CD_CDS_LC,
 gc.CD_UO_LC,
 gc.ESERCIZIO,
 gc.PG_LC,
 gc.cd_gruppo_cr,
 gc.cd_regione,
 gc.pg_comune,
 lgc.CD_CDS_ORIGINE,
 lgc.CD_UO_ORIGINE,
 lgc.PG_LIQUIDAZIONE_ORIGINE,
 lgc.IM_LIQUIDATO,
 lgc.FL_ACCENTRATO,
 lgc.CD_CDS_DOC,
 lgc.ESERCIZIO_DOC,
 lgc.PG_DOC,
 lgc.CD_CDS_OBB_ACCENTR,
 lgc.ESERCIZIO_OBB_ACCENTR,
 lgc.ESERCIZIO_ORI_OBB_ACCENTR,
 lgc.PG_OBB_ACCENTR,
 lgc.PG_GRUPPO_CENTRO,
 lgc.STATO,
 lgc.DACR,
 lgc.UTCR,
 lgc.UTUV,
 lgc.DUVA,
 lgc.PG_VER_REC
from
   liquid_gruppo_centro gc,
   liquid_gruppo_cori lgc
where
     gc.esercizio 		  = lgc.esercizio
 and gc.cd_gruppo_cr 	  = lgc.cd_gruppo_cr
 and gc.cd_regione 		  = lgc.cd_regione
 and gc.pg_comune 		  = lgc.pg_comune
 and gc.pg_gruppo_centro  = lgc.pg_gruppo_centro
 and lgc.cd_unita_organizzativa = lgc.cd_uo_origine;

   COMMENT ON TABLE "V_LIQUID_CENTRO_UO"  IS 'View di estrazione dei dettagli CORI accentrati della liquidazione per UO';
