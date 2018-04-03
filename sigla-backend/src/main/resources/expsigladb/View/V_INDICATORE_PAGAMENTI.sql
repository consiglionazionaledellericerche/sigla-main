--------------------------------------------------------
--  DDL for View V_INDICATORE_PAGAMENTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_INDICATORE_PAGAMENTI" ("ESERCIZIO", "GIORNI", "TRIMESTRE") AS 
  select esercizio,round(sum(tot_pesato)/sum(tot_pagato)) gg,trimestre
from
(select m.esercizio,
SUM((TRUNC(M.DT_TRASMISSIONE)-f.dt_scadenza)*mr.im_mandato_riga) tot_pesato,sum(mr.im_mandato_riga) tot_pagato , round((to_number(to_char(M.DT_TRASMISSIONE,'mm')+1)/3),0) trimestre
from contratto c,
     obbligazione o,
     obbligazione_scadenzario os ,
     fattura_passiva f,
     mandato_riga mr,
     mandato m
where C.ESERCIZIO (+) = O.ESERCIZIO_CONTRATTO
and C.STATO(+) = O.STATO_CONTRATTO
and C.PG_CONTRATTO(+) = O.PG_CONTRATTO
and (c.cd_tipo_contratto not in( 'AL', 'CONL', 'CSPP','QUOTE', 'COND', 'COMPR', 'PART', 'LOC_P', 'COST', 'INCEN', 'CSPE','LOC','TRASF','INTP') or
O.ESERCIZIO_CONTRATTO is null)
and O.ESERCIZIO = OS.ESERCIZIO
and O.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE
and O.CD_CDS = OS.CD_CDS
and O.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE
--and f.cd_unita_organizzativa not in('000.406')
and exists (select 1 from fattura_passiva_riga fr
where
f.FL_FATTURA_COMPENSO ='N'  and
OS.ESERCIZIO = FR.ESERCIZIO_OBBLIGAZIONE
and OS.ESERCIZIO_ORIGINALE = FR.ESERCIZIO_ORI_OBBLIGAZIONE
and OS.CD_CDS = FR.CD_CDS_OBBLIGAZIONE
and OS.PG_OBBLIGAZIONE = FR.PG_OBBLIGAZIONE
and OS.PG_OBBLIGAZIONE_SCADENZARIO = FR.PG_OBBLIGAZIONE_SCADENZARIO
and FR.ESERCIZIO = F.ESERCIZIO
and FR.CD_CDS = F.CD_CDS
and FR.CD_UNITA_ORGANIZZATIVA = F.CD_UNITA_ORGANIZZATIVA
and FR.PG_FATTURA_PASSIVA = F.PG_FATTURA_PASSIVA)
and os.ESERCIZIO = MR.ESERCIZIO_OBBLIGAZIONE
and os.CD_CDS = MR.CD_CDS
and OS.ESERCIZIO_ORIGINALE = MR.ESERCIZIO_ORI_OBBLIGAZIONE
and OS.PG_OBBLIGAZIONE = MR.PG_OBBLIGAZIONE
and OS.PG_OBBLIGAZIONE_SCADENZARIO = MR.PG_OBBLIGAZIONE_SCADENZARIO
and MR.CD_CDS = M.CD_CDS
and MR.ESERCIZIO = M.ESERCIZIO
and MR.PG_MANDATO = M.PG_MANDATO
and M.ESERCIZIO >= 2015
and M.STATO !='A'
and F.STATO_COFI !='A'
and M.TI_MANDATO = 'P'
and im_mandato!=0
and mr.cd_tipo_documento_amm ='FATTURA_P'
and f.dt_scadenza is not null
and m.DT_TRASMISSIONE is not null
and nvl(F.STATO_LIQUIDAZIONE,'LIQ')='LIQ'
and f.ti_fattura='F'
group by
m.esercizio,round((to_number(to_char(M.DT_TRASMISSIONE,'mm')+1)/3),0)
union all
select
m.esercizio,SUM((TRUNC(M.DT_TRASMISSIONE)-f.dt_scadenza)*mr.im_mandato_riga)tot_pesato,sum(mr.im_mandato_riga) tot_pagato , round((to_number(to_char(M.DT_TRASMISSIONE,'mm')+1)/3),0) trimestre
from contratto c,
     obbligazione o,
     obbligazione_scadenzario os ,
     fattura_passiva f,
     mandato_riga mr,
     mandato m
where C.ESERCIZIO (+) = O.ESERCIZIO_CONTRATTO
and C.STATO(+) = O.STATO_CONTRATTO
and C.PG_CONTRATTO(+) = O.PG_CONTRATTO
and (c.cd_tipo_contratto not in( 'AL', 'CONL', 'CSPP','QUOTE', 'COND', 'COMPR', 'PART', 'LOC_P', 'COST', 'INCEN', 'CSPE','LOC','TRASF','INTP') or
O.ESERCIZIO_CONTRATTO is null)
and O.ESERCIZIO = OS.ESERCIZIO
and O.ESERCIZIO_ORIGINALE = OS.ESERCIZIO_ORIGINALE
and O.CD_CDS = OS.CD_CDS
and O.PG_OBBLIGAZIONE = OS.PG_OBBLIGAZIONE
and exists
(select 1 from compenso, compenso_riga cr
where
compenso.CD_CDS = cr.CD_CDS AND 
compenso.CD_UNITA_ORGANIZZATIVA = cr.CD_UNITA_ORGANIZZATIVA AND 
compenso.ESERCIZIO = cr.ESERCIZIO AND
compenso.PG_COMPENSO = cr.PG_COMPENSO AND
compenso.FL_GENERATA_FATTURA ='Y' and
OS.ESERCIZIO = cr.ESERCIZIO_OBBLIGAZIONE
and OS.ESERCIZIO_ORIGINALE = cr.ESERCIZIO_ORI_OBBLIGAZIONE
and OS.CD_CDS = cr.CD_CDS_OBBLIGAZIONE
and OS.PG_OBBLIGAZIONE = cr.PG_OBBLIGAZIONE
and OS.PG_OBBLIGAZIONE_SCADENZARIO = cr.PG_OBBLIGAZIONE_SCADENZARIO
and compenso.ESERCIZIO = F.ESERCIZIO_COMPENSO
and compenso.CD_CDS = F.CDs_compenso
and compenso.CD_UNITA_ORGANIZZATIVA = F.uo_compenso
and compenso.PG_COMPENSO = F.PG_COMPENSO
and f.FL_FATTURA_COMPENSO ='Y')
and os.ESERCIZIO = MR.ESERCIZIO_OBBLIGAZIONE
and os.CD_CDS = MR.CD_CDS
and OS.ESERCIZIO_ORIGINALE = MR.ESERCIZIO_ORI_OBBLIGAZIONE
and OS.PG_OBBLIGAZIONE = MR.PG_OBBLIGAZIONE
and OS.PG_OBBLIGAZIONE_SCADENZARIO = MR.PG_OBBLIGAZIONE_SCADENZARIO
and MR.CD_CDS = M.CD_CDS
and MR.ESERCIZIO = M.ESERCIZIO
and MR.PG_MANDATO = M.PG_MANDATO
and M.ESERCIZIO >= 2015
and M.STATO !='A'
and F.STATO_COFI !='A'
and M.TI_MANDATO = 'P'
and im_mandato!=0
and mr.cd_tipo_documento_amm ='COMPENSO'
and f.dt_scadenza is not null
and m.DT_TRASMISSIONE is not null
--and f.cd_unita_organizzativa not in('000.406')
and nvl(F.STATO_LIQUIDAZIONE,'LIQ')='LIQ'
and f.ti_fattura='F'
group by
m.esercizio,round((to_number(to_char(M.DT_TRASMISSIONE,'mm')+1)/3),0))
group by esercizio,trimestre
order  by esercizio,trimestre ASC;
