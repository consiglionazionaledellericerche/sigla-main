--------------------------------------------------------
--  DDL for View V_INCARICHI_COLLABORAZIONE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INCARICHI_COLLABORAZIONE" ("ESERCIZIO", "PG_PROCEDURA", "CD_CDS", "DS_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "STATO", "OGGETTO", "NR_CONTRATTI", "DT_PUBBLICAZIONE", "DT_FINE_PUBBLICAZIONE", "DT_SCADENZA") AS 
  (SELECT esercizio, pg_procedura, cd_cds, cds.ds_unita_organizzativa,
           uo.cd_unita_organizzativa, uo.ds_unita_organizzativa, stato,
           oggetto, nr_contratti, dt_pubblicazione, dt_fine_pubblicazione,
           dt_fine_pubblicazione
--
-- Date: 26/10/2007
-- Version: 1.0
--
-- Vista richieste collaborazioni utilizzata per ricerca delle competenze
-- necessarie all''interno del CNR, utile alla estrazione in file XML
--
-- History:
--
-- Date: 26/10/2007
-- Version: 1.0
-- Creazione
--
-- Body:
--
    FROM   incarichi_procedura,
           unita_organizzativa cds,
           unita_organizzativa uo
     WHERE incarichi_procedura.cd_cds = cds.cd_unita_organizzativa
       AND incarichi_procedura.cd_unita_organizzativa =
                                                     uo.cd_unita_organizzativa
       AND stato IN ('PU', 'PD', 'IN')
       AND dt_pubblicazione IS NOT NULL)
   ORDER BY 1, 2 ;
