--------------------------------------------------------
--  DDL for View V_AVANZO_CASSA_CDS
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_AVANZO_CASSA_CDS" ("ESERCIZIO", "CD_CDS", "IM_AVANZO") AS 
  (
--
-- Date: 26/06/2003
-- Version: 1.10
--
-- Estrae l'avanzo di cassa del CDS
--
-- History:
--
-- Date: 13/01/2004
-- Version: 1.0
-- Creazione
--
-- Body:
--
    SELECT   esercizio, cd_cds, SUM (im_avanzo)
        FROM (SELECT esercizio esercizio, cd_cds cd_cds,
                     im_cassa_iniziale im_avanzo
                FROM esercizio
               WHERE cd_cds != '999'
              UNION ALL
              SELECT esercizio esercizio,
                     cnrutl001.getcdsfromcdr (cd_centro_responsabilita)
                                                                       cd_cds,
                     DECODE (ti_gestione, 'E', 1, -1) * im_pagamenti_incassi
                FROM voce_f_saldi_cdr_linea
               WHERE cnrutl001.getcdsfromcdr (cd_centro_responsabilita) !=
                                                                         '999')
    GROUP BY esercizio, cd_cds);

   COMMENT ON TABLE "V_AVANZO_CASSA_CDS"  IS 'Estrae l''avanzo di cassa del CDS';
