--------------------------------------------------------
--  DDL for View V_ASS_DOC_CONTABILI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ASS_DOC_CONTABILI" ("CD_CDS", "ESERCIZIO", "CD_TIPO_DOCUMENTO_CONT", "PG_DOCUMENTO_CONT", "FL_CON_MAN_PRC", "CD_CDS_COLL", "ESERCIZIO_COLL", "CD_TIPO_DOCUMENTO_CONT_COLL", "PG_DOCUMENTO_CONT_COLL", "PG_VER_REC") AS 
  SELECT
--
-- Date: 27/05/2003
-- Version: 1.6
--
-- Estrae tutte le associazioni fra mandati e reversali e fra mandati e mandati
-- Gestisce sia il caso che esista il mandato principale sia che non esista
--
-- History:
--
-- Date: 08/07/2002
-- Version: 1.0
-- Creazione
--
-- Date: 17/09/2002
-- Version: 1.1
-- Gestione dei mandati/reversali cori senza mandato principale
--
-- Date: 18/09/2002
-- Version: 1.2
-- Ottimizzazione della vista
--
-- Date: 19/09/2002
-- Version: 1.3
-- Estrazione dei soli documenti virtualemente o via compenso
--
-- Date: 10/12/2002
-- Version: 1.4
-- Eliminata distinct di troppo
--
-- Date: 15/05/2003
-- Version: 1.5
-- Estrazione reversali in ASS_DOC_CONT_NMP non collegate a mandato principale
--
-- Date: 27/05/2003
-- Version: 1.6
-- Ottimizzazione modifica del 15-05-2003
--
-- Body:
--
CD_CDS,
ESERCIZIO,
'MAN',
PG_MANDATO,
'Y',
CD_CDS_COLL,
ESERCIZIO_COLL,
'MAN',
PG_MANDATO_COLL,
PG_VER_REC
FROM
ASS_MANDATO_MANDATO
UNION ALL
SELECT
CD_CDS_MANDATO,
ESERCIZIO_MANDATO,
'MAN',
PG_MANDATO,
'Y',
CD_CDS_REVERSALE,
ESERCIZIO_REVERSALE,
'REV',
PG_REVERSALE,
PG_VER_REC
FROM
ASS_MANDATO_REVERSALE
WHERE
TI_ORIGINE = 'S'
UNION ALL
SELECT
CD_CDS_REVERSALE,
ESERCIZIO_REVERSALE,
'REV',
PG_REVERSALE,
'Y',
CD_CDS_MANDATO,
ESERCIZIO_MANDATO,
'MAN',
PG_MANDATO,
PG_VER_REC
FROM
ASS_MANDATO_REVERSALE
WHERE
TI_ORIGINE = 'E'
UNION ALL
SELECT
 b.CD_CDS,
 b.esercizio,
'MAN',
 b.PG_MANDATO,
'Y',
 d.CD_CDS_DOC,
 d.ESERCIZIO_DOC,
'REV',
 d.PG_DOC,
 b.PG_VER_REC
FROM
 MANDATO_RIGA b, ASS_COMP_DOC_CONT_NMP d
WHERE
     b.stato <> 'A'
 and b.cd_tipo_documento_amm = 'COMPENSO'
 and d.pg_compenso = b.pg_doc_amm
 and d.esercizio_compenso = b.esercizio_doc_amm
 and d.cd_uo_compenso = b.cd_uo_doc_amm
 and d.cd_cds_compenso = b.cd_cds_doc_amm
 and d.cd_tipo_doc = 'R'
 and exists (select 1 from compenso c where
     b.esercizio_doc_amm = c.esercizio
 and b.cd_uo_doc_amm = c.cd_unita_organizzativa
 and b.cd_cds_doc_amm = c.cd_cds
 and b.pg_doc_amm = c.pg_compenso
)
UNION ALL
SELECT
a.CD_CDS_DOC,
a.ESERCIZIO_DOC,
decode(a.cd_tipo_doc,'M','MAN','REV'),
a.PG_DOC,
'N',
b.CD_CDS_DOC,
b.ESERCIZIO_DOC,
decode(b.cd_tipo_doc,'M','MAN','REV'),
b.PG_DOC,
a.PG_VER_REC
FROM
 ASS_COMP_DOC_CONT_NMP a, ASS_COMP_DOC_CONT_NMP b
WHERE
 not (
   a.pg_doc = b.pg_doc
   and a.esercizio_doc = b.esercizio_doc
   and a.cd_cds_doc = b.cd_cds_doc
   and a.cd_tipo_doc = b.cd_tipo_doc
 )
 and a.cd_cds_compenso = b.cd_cds_compenso
 and a.esercizio_compenso = b.esercizio_compenso
 and a.cd_uo_compenso = b.cd_uo_compenso
 and a.pg_compenso = b.pg_compenso
;

   COMMENT ON TABLE "V_ASS_DOC_CONTABILI"  IS 'Estrae tutte le associazioni fra mandati e reversali e fra mandati e mandati
Gestisce sia il caso che esista il mandato principale sia che non esista';
