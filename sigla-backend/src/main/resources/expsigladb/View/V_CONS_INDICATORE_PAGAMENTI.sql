--------------------------------------------------------
--  DDL for View V_CONS_INDICATORE_PAGAMENTI
--------------------------------------------------------
CREATE OR REPLACE FORCE VIEW "V_CONS_INDICATORE_PAGAMENTI" ("ESERCIZIO", "TRIMESTRE", "TIPO_RIGA", "CD_TERZO",
"TIPO_DOCUMENTO", "ESERCIZIO_DOCUMENTO", "UO_DOCUMENTO", "NUMERO_DOCUMENTO", "IMPORTO_DOCUMENTO",
"DATA_SCADENZA", "DATA_TRASMISSIONE", "DATA_RICEZIONE", "DATA_REGISTRAZIONE", "DATA_LIQUIDAZIONE",
"DIFFERENZA_GIORNI", "IMPORTO_PAGATO", "IMPORTO_PESATO", "INDICE_PAGAMENTI", "CD_CDS_OBBLIGAZIONE",
"ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO")
AS
  (SELECT to_number(to_char(data_scadenza,'yyyy')) esercizio,
	   round((to_number(to_char(data_scadenza,'mm')+1)/3),0) trimestre, 'DETAIL' tipo_riga,
	   cd_terzo, tipo_documento, esercizio_documento, uo_documento, numero_documento, importo_documento,
       data_scadenza, data_trasmissione, data_ricezione, data_registrazione, data_liquidazione,
       differenza_giorni, importo_pagato, importo_pesato, NULL indice_pagamenti,
       cd_cds_obbligazione, esercizio_obbligazione, esercizio_ori_obbligazione, pg_obbligazione, PG_OBBLIGAZIONE_SCADENZARIO
FROM V_INDICATORE_PAGAMENTI_DETAIL
UNION ALL
SELECT to_number(to_char(data_scadenza,'yyyy')) esercizio,
	   round((to_number(to_char(data_scadenza,'mm')+1)/3),0) trimestre, 'SUM_TRIMESTRE' tipo_riga,
	   NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
	   sum(importo_pagato), sum(importo_pesato), sum(importo_pesato)/sum(importo_pagato),
	   NULL, NULL, NULL, NULL, NULL
FROM V_INDICATORE_PAGAMENTI_DETAIL
GROUP BY to_number(to_char(data_scadenza,'yyyy')), round((to_number(to_char(data_scadenza,'mm')+1)/3),0)
UNION ALL
SELECT to_number(to_char(data_scadenza,'yyyy')) esercizio,
	   round((to_number(to_char(data_scadenza,'mm')+1)/3),0) trimestre, 'SUM_TRIMESTRE_UO' tipo_riga,
	   NULL, NULL, NULL, UO_DOCUMENTO, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
	   sum(importo_pagato), sum(importo_pesato), sum(importo_pesato)/sum(importo_pagato),
	   NULL, NULL, NULL, NULL, NULL
FROM V_INDICATORE_PAGAMENTI_DETAIL
GROUP BY to_number(to_char(data_scadenza,'yyyy')), round((to_number(to_char(data_scadenza,'mm')+1)/3),0), UO_DOCUMENTO
UNION ALL
SELECT to_number(to_char(data_scadenza,'yyyy')) esercizio, NULL trimestre, 'SUM_ANNO' tipo_riga,
	   NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
	   sum(importo_pagato), sum(importo_pesato), sum(importo_pesato)/sum(importo_pagato),
	   NULL, NULL, NULL, NULL, NULL
FROM V_INDICATORE_PAGAMENTI_DETAIL
GROUP BY to_number(to_char(data_scadenza,'yyyy'))
UNION ALL
SELECT to_number(to_char(data_scadenza,'yyyy')) esercizio, NULL trimestre, 'SUM_ANNO_UO' tipo_riga,
	   NULL, NULL, NULL, UO_DOCUMENTO, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,
	   sum(importo_pagato), sum(importo_pesato), sum(importo_pesato)/sum(importo_pagato),
	   NULL, NULL, NULL, NULL, NULL
FROM V_INDICATORE_PAGAMENTI_DETAIL
GROUP BY to_number(to_char(data_scadenza,'yyyy')), UO_DOCUMENTO)
/
