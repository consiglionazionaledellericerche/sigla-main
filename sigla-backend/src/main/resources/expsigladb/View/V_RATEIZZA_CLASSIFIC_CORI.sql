--------------------------------------------------------
--  DDL for View V_RATEIZZA_CLASSIFIC_CORI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_RATEIZZA_CLASSIFIC_CORI" ("ESERCIZIO", "CD_ANAG", "DENOMINAZIONE", "DS_CLASSIFICAZIONE_CORI", "IM_DA_RATEIZZARE", "IM_RATEIZZATO", "IM_RESIDUO", "CD_CDS_CONGUAGLIO", "CD_UO_CONGUAGLIO", "PG_CONGUAGLIO") AS 
  Select
--
-- Date: 06/08/2008
-- Version: 1.0
--
-- View per la consultazione delle Rateizzazioni delle Addizionali
--
-- History:
-- Date: 06/08/2008
-- Version: 1.0
-- Creazione
--
-- Body:
	R.esercizio, R.cd_anag,	decode(A.cognome,Null,A.ragione_sociale,A.cognome||' '||A.nome),
	C.ds_classificazione_cori, R.im_da_rateizzare, R.im_rateizzato, NVL(R.im_da_rateizzare,0) - NVL(R.im_rateizzato,0),
	R.cd_cds_conguaglio, R.cd_uo_conguaglio, R.pg_conguaglio
From   RATEIZZA_CLASSIFIC_CORI R, CLASSIFICAZIONE_CORI C, ANAGRAFICO A
Where  R.cd_anag = A.cd_anag
  and  R.cd_classificazione_cori = c.cd_classificazione_cori
Order By CD_ANAG, DS_CLASSIFICAZIONE_CORI;
