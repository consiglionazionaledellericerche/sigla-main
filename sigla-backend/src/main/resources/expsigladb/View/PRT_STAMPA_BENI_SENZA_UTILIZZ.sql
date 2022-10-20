--------------------------------------------------------
--  DDL for View PRT_STAMPA_BENI_SENZA_UTILIZZ
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_STAMPA_BENI_SENZA_UTILIZZ" ("CD_UNITA_ORGANIZZATIVA", "DATA_REGISTRAZIONE", "PG_INVENTARIO", "NR_INVENTARIO", "PROGRESSIVO","ETICHETTA") AS
  SELECT
--
-- Date: 14/4/2004
-- Version: 1.2
--
-- Stampa beni senza utilizzatori
--
--
-- History:
--
-- Date: 4/12/2003
-- Version: 1.0
-- Creazione
--
-- Date: 9/1/2004
-- Version: 1.1
-- Inserita data_registrazione da tabella buono_carico_scarico
--
-- Date: 14/4/2004
-- Version: 1.2
-- Corretta join tra buono_carico_scarico_dett e buono_carico_scarico,
-- Aggiunto filtro cespiti totalmente scaricati e filtro progressivo = '0'
--
-- Date: 01/09/2022
-- Version: 1.3
-- Inserita Etichetta bene
--
-- Body:
--
d.CD_UNITA_ORGANIZZATIVA,
c.DATA_REGISTRAZIONE,
a.PG_INVENTARIO, a.NR_INVENTARIO, a.PROGRESSIVO,
d.ETICHETTA
from buono_carico_scarico_dett a,
buono_carico_scarico c, inventario_beni d
where
a.PG_BUONO_C_S = c.PG_BUONO_C_S and
a.PG_INVENTARIO = c.PG_INVENTARIO and
a.ESERCIZIO = c.ESERCIZIO and
a.PG_INVENTARIO = d.PG_INVENTARIO and
a.NR_INVENTARIO = d.NR_INVENTARIO and
a.PROGRESSIVO = '0' and
a.PROGRESSIVO = d.PROGRESSIVO and
a.TI_DOCUMENTO = c.TI_DOCUMENTO and
d.FL_TOTALMENTE_SCARICATO = 'N' and
not exists
(select  * from inventario_utilizzatori_la b
where a.PG_INVENTARIO = b.PG_INVENTARIO and
a.NR_INVENTARIO = b.NR_INVENTARIO and
b.PROGRESSIVO = '0' and
a.PROGRESSIVO = b.PROGRESSIVO)
order by d.CD_UNITA_ORGANIZZATIVA
;

   COMMENT ON TABLE "PRT_STAMPA_BENI_SENZA_UTILIZZ"  IS 'Stampa beni senza utilizzatori';
