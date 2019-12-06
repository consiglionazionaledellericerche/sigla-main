--------------------------------------------------------
--  DDL for View V_PDG_PDG_SAUOP
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_PDG_SAUOP" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "STATO", "ANNOTAZIONI", "FL_RIBALTATO_SU_AREA", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS
  select
--
-- Date: 05/12/2001
-- Version: 1.2
--
-- Ritorna la testata del PdG del CdR che gestisce le page del personale
-- La vista non verifica la validita dell'STO
--
-- History:
--
-- Date: 18/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 08/11/2001
-- Version: 1.1
-- Eliminazione esercizio da STO
--
-- Date: 05/12/2001
-- Version: 1.2
-- Cambiata entry in CONFIGURAZIONE CNR per identificazione CDR gestione personale
--
-- Body:
--
pdg.ESERCIZIO,
pdg.CD_CENTRO_RESPONSABILITA,
pdg.STATO, pdg.ANNOTAZIONI,
pdg.FL_RIBALTATO_SU_AREA,
pdg.DACR, pdg.UTCR,
pdg.DUVA,
pdg.UTUV,
pdg.PG_VER_REC
from
 pdg_preventivo pdg
where pdg.CD_CENTRO_RESPONSABILITA = CNRCTB020.getCdCDRPersonale(pdg.ESERCIZIO);

   COMMENT ON TABLE "V_PDG_PDG_SAUOP"  IS 'Ritorna la testata del PdG del CdR che gestisce le page del personale
La vista non verifica la validita dell''STO';
