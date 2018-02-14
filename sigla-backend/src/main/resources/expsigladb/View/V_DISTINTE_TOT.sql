--------------------------------------------------------
--  DDL for View V_DISTINTE_TOT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DISTINTE_TOT" ("ESERCIZIO", "CDS", "UO", "PG_DISTINTA", "DT_EMISSIONE", "DT_INVIO", "PG_DISTINTA_DEF", "TOT_MANDATI", "TOT_REVERSALI", "TOT_PAGATO", "TOT_INCASSATO", "DIFF_PAGATO", "DIFF_INCASSATO", "TIPO") AS 
  select d.esercizio,d.cd_cds,d.cd_unita_organizzativa,d.pg_distinta,d.dt_EMISSIONE,d.dt_invio,d.pg_distinta_def,
	nvl((SELECT SUM(IM_MANDATO) FROM
			MANDATO A, DISTINTA_CASSIERE_DET B
			WHERE
			B.PG_MANDATO IS NOT NULL AND
			A.DT_ANNULLAMENTO IS NULL AND
			A.CD_CDS = B.CD_CDS_ORIGINE AND
			A.ESERCIZIO = B.ESERCIZIO AND
			A.PG_MANDATO = B.PG_MANDATO and
            d.esercizio = b.esercizio and
            d.cd_cds = b.cd_cds  and
            d.cd_unita_organizzativa = b.cd_unita_organizzativa  and
            d.pg_distinta = b.pg_distinta),0) tot_mandati,
        nvl((SELECT SUM(IM_REVERSALE) FROM
            REVERSALE A, DISTINTA_CASSIERE_DET B
            WHERE
            B.PG_REVERSALE IS NOT NULL AND
            A.DT_ANNULLAMENTO IS NULL AND
            A.CD_CDS = B.CD_CDS_ORIGINE AND
            A.ESERCIZIO = B.ESERCIZIO AND
            A.PG_REVERSALE = B.PG_REVERSALE and
            d.esercizio = b.esercizio and
            d.cd_cds = b.cd_cds  and
            d.cd_unita_organizzativa = b.cd_unita_organizzativa  and
            d.pg_distinta = b.pg_distinta),0) tot_reversali,
            nvl((SELECT SUM(IM_PAGATO) FROM
			MANDATO A, DISTINTA_CASSIERE_DET B
			WHERE
			B.PG_MANDATO IS NOT NULL AND
			A.DT_ANNULLAMENTO IS NULL AND
			A.CD_CDS = B.CD_CDS_ORIGINE AND
			A.ESERCIZIO = B.ESERCIZIO AND
			A.PG_MANDATO = B.PG_MANDATO and
            d.esercizio = b.esercizio and
            d.cd_cds = b.cd_cds  and
            d.cd_unita_organizzativa = b.cd_unita_organizzativa  and
            d.pg_distinta = b.pg_distinta),0) tot_pagato,
           nvl((SELECT SUM(IM_INCASSATO) FROM
            REVERSALE A, DISTINTA_CASSIERE_DET B
            WHERE
            B.PG_REVERSALE IS NOT NULL AND
            A.DT_ANNULLAMENTO IS NULL AND
            A.CD_CDS = B.CD_CDS_ORIGINE AND
            A.ESERCIZIO = B.ESERCIZIO AND
            A.PG_REVERSALE = B.PG_REVERSALE and
            d.esercizio = b.esercizio and
            d.cd_cds = b.cd_cds  and
            d.cd_unita_organizzativa = b.cd_unita_organizzativa  and
            d.pg_distinta = b.pg_distinta),0) tot_incassato,
				nvl((SELECT SUM(IM_MANDATO) FROM
			MANDATO A, DISTINTA_CASSIERE_DET B
			WHERE
			B.PG_MANDATO IS NOT NULL AND
			A.DT_ANNULLAMENTO IS NULL AND
			A.CD_CDS = B.CD_CDS_ORIGINE AND
			A.ESERCIZIO = B.ESERCIZIO AND
			A.PG_MANDATO = B.PG_MANDATO and
            d.esercizio = b.esercizio and
            d.cd_cds = b.cd_cds  and
            d.cd_unita_organizzativa = b.cd_unita_organizzativa  and
            d.pg_distinta = b.pg_distinta),0)- nvl((SELECT SUM(IM_PAGATO) FROM
			MANDATO A, DISTINTA_CASSIERE_DET B
			WHERE
			B.PG_MANDATO IS NOT NULL AND
			A.DT_ANNULLAMENTO IS NULL AND
			A.CD_CDS = B.CD_CDS_ORIGINE AND
			A.ESERCIZIO = B.ESERCIZIO AND
			A.PG_MANDATO = B.PG_MANDATO and
            d.esercizio = b.esercizio and
            d.cd_cds = b.cd_cds  and
            d.cd_unita_organizzativa = b.cd_unita_organizzativa  and
            d.pg_distinta = b.pg_distinta),0) diff_pagato,
            nvl((SELECT SUM(IM_REVERSALE) FROM
            REVERSALE A, DISTINTA_CASSIERE_DET B
            WHERE
            B.PG_REVERSALE IS NOT NULL AND
            A.DT_ANNULLAMENTO IS NULL AND
            A.CD_CDS = B.CD_CDS_ORIGINE AND
            A.ESERCIZIO = B.ESERCIZIO AND
            A.PG_REVERSALE = B.PG_REVERSALE and
            d.esercizio = b.esercizio and
            d.cd_cds = b.cd_cds  and
            d.cd_unita_organizzativa = b.cd_unita_organizzativa  and
            d.pg_distinta = b.pg_distinta),0)- nvl((SELECT SUM(IM_INCASSATO) FROM
            REVERSALE A, DISTINTA_CASSIERE_DET B
            WHERE
            B.PG_REVERSALE IS NOT NULL AND
            A.DT_ANNULLAMENTO IS NULL AND
            A.CD_CDS = B.CD_CDS_ORIGINE AND
            A.ESERCIZIO = B.ESERCIZIO AND
            A.PG_REVERSALE = B.PG_REVERSALE and
            d.esercizio = b.esercizio and
            d.cd_cds = b.cd_cds  and
            d.cd_unita_organizzativa = b.cd_unita_organizzativa  and
            d.pg_distinta = b.pg_distinta),0) diff_incassato,
          decode(d.fl_flusso,'Y','Flusso',decode(d.fl_sepa,'Y','Estera',decode(d.fl_annulli,'Y','Annulli','Altro'))) tipo
          from
          distinta_cassiere d,parametri_cnr
          where d.esercizio =parametri_cnr.esercizio
          and fl_tesoreria_unica ='Y'
          group by d.esercizio,d.cd_cds,d.cd_unita_organizzativa,d.pg_distinta,d.dt_EMISSIONE,d.dt_invio,d.pg_distinta_def,decode(d.fl_flusso,'Y','Flusso',decode(d.fl_sepa,'Y','Estera',decode(d.fl_annulli,'Y','Annulli','Altro')))
          order  by d.esercizio,d.cd_cds,d.cd_unita_organizzativa,d.pg_distinta,d.dt_EMISSIONE,d.dt_invio,d.pg_distinta_def,decode(d.fl_flusso,'Y','Flusso',decode(d.fl_sepa,'Y','Estera',decode(d.fl_annulli,'Y','Annulli','Altro')));
