--------------------------------------------------------
--  DDL for View V_DISP_CASSA_CNR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DISP_CASSA_CNR" ("ESERCIZIO", "CD_CDS", "IM_DISPONIBILTA_CASSA") AS 
  Select /*+ optimizer_features_enable('10.1.0') */
 a.ESERCIZIO,
 a.CD_CDS,
 disp_cassa_mandati(esercizio,cd_cds) IM_DISP_CASSA
FROM    ESERCIZIO a
WHERE
 exists ( select 1 from v_unita_organizzativa_valida where
               esercizio = a.esercizio
		   and cd_tipo_unita = 'ENTE'
		   and cd_unita_organizzativa = a.cd_cds
        ) ;
