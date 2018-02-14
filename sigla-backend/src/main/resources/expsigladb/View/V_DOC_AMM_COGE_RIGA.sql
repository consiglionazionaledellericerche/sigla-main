--------------------------------------------------------
--  DDL for View V_DOC_AMM_COGE_RIGA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_COGE_RIGA" ("CD_TIPO_DOCUMENTO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_NUMERO_DOCUMENTO", "CD_TERZO", "PG_RIGA", "CD_BENE_SERVIZIO", "DT_DA_COMPETENZA_COGE", "DT_A_COMPETENZA_COGE", "TI_ISTITUZ_COMMERC", "IM_TOTALE_DIVISA", "IM_IMPONIBILE", "IM_IVA", "IM_DIPONIBILE_NC", "ESERCIZIO_DOC", "CD_CDS_DOC", "ESERCIZIO_ORI_DOC", "PG_DOC", "FL_PGIRO", "ESERCIZIO_EV", "TI_APPARTENENZA_EV", "TI_GESTIONE_EV", "CD_ELEMENTO_VOCE_EV", "IS_DOC_OBB", "ESERCIZIO_DOC_ORI_RIPORTO", "CD_CDS_DOC_ORI_RIPORTO", "ESERCIZIO_DOC_ORI_ORI_RIPORTO", "PG_DOC_ORI_RIPORTO", "STATO_COGE_DOCAMM", "STATO_COGE_DOCCONT") AS 
  (
select -- Estrazione fattura passiva
--
-- Date: 18/07/2006
-- Version: 2.9
--
-- Vista di estrazione dei dettagli di documento amministrativo con valorizzazioni a fini COGE
--
-- History:
--
-- Date: 16/02/2002
-- Version: 1.0
-- Creazione
--
-- Date: 04/03/2002
-- Version: 1.1
-- Aggiunta gestione documento generico
--
-- Date: 15/05/2002
-- Version: 1.2
-- Scorporo gestione IVA in vista CORI e aggiunto COMPENSO
--
-- Date: 19/05/2002
-- Version: 1.3
-- Gestione sconti/abbuoni attraverso il tipo di bene servizio su fattura passiva
-- Eliminati i generici di trasferimento - non vanno in COEP direttamente
--
-- Date: 20/05/2002
-- Version: 1.4
-- Corretta gestione dell'iva nel caso di fattura istituzionale
--
-- Date: 21/05/2002
-- Version: 1.5
-- Aggiunto il progressivo di riga nel caso di fatture attive per beni durevoli
-- Sistemato il problema relativo agli accertamenti su ente per la parte entrate
--
-- Date: 21/05/2002
-- Version: 1.6
-- Sistemata gestione collegamento con obbligazione e accertamento
--
-- Date: 22/05/2002
-- Version: 1.7
-- Estrae tutti i dettagli di documenti senza filtri speciali
--
-- Date: 27/05/2002
-- Version: 1.8
-- Estensioni per identificazione pgiro
--
-- Date: 30/05/2002
-- Version: 1.9
-- Aggiunto l'ANTICIPO
--
-- Date: 07/06/2002
-- Version: 2.0
-- Introduzione della missione
--
-- Date: 07/06/2002
-- Version: 2.1
-- Aggiunta date competenza coge su MISSIONE
-- Aggiunto RIMBORSO
--
-- Date: 12/06/2002
-- Version: 2.2
-- Fix di join su generico (mancava il tipo)
--
-- Date: 07/11/2002
-- Version: 2.3
-- Fix data competenza coge in ANTICIPO e RIMBORSO
--
-- Date: 28/11/2002
-- Version: 2.4
-- Gestione flag commerciale/istituzionale per generico e compenso
--
-- Date: 12/12/2002
-- Version: 2.5
-- Gestione estrazione conto da anticipo nel caso in cui la missione non abbia riferimenti a obbligazione
-- Gestione estrazione conto da anticipo nel caso in cui il compenso collegato a missione non abbia riferimenti a obbligazione
--
-- Date: 15/07/2003
-- Version: 2.6
-- Escluse le righe annullate di fatture e generici
--
-- Date: 30/01/2004
-- Version: 2.7
-- Introdotti i nuovi campi:  IS_DOC_OBB, ESERCIZIO_DOC_ORI_RIPORTO, CD_CDS_DOC_ORI_RIPORTO, PG_DOC_ORI_RIPORTO
-- ceh identificano rispettivamente il tipo di documento (obbligazione/accertamento), e l'obbligazione di origine nel caso
-- l'obbligazione risulti riportata a nuovo esercizio
--
-- Date: 05/03/2004
-- Version: 2.8
-- L'importo IVA delle fatture PASSIVE istituzionali non deve ritornare 0
--
-- Date: 18/07/2006
-- Version: 2.9
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
     'FATTURA_P'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_FATTURA_PASSIVA
    ,a.CD_TERZO
    ,0 -- pg riga non supportato in fattura passiva
	,b.cd_bene_servizio
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,b.DT_DA_COMPETENZA_COGE
    ,b.DT_A_COMPETENZA_COGE
    ,b.TI_ISTITUZ_COMMERC
    ,sum(b.IM_TOTALE_DIVISA)
    ,sum(decode(b.TI_ISTITUZ_COMMERC,'I',b.IM_IMPONIBILE+b.IM_IVA,b.IM_IMPONIBILE)) -- Se istituzionale l'IVA va sull'imp.
    ,sum(b.IM_IVA)
    ,sum(b.IM_DIPONIBILE_NC)
    ,decode(b.pg_obbligazione,null,b.esercizio_accertamento,b.esercizio_obbligazione)
    ,decode(b.pg_obbligazione,null,b.cd_cds_accertamento,b.cd_cds_obbligazione)
    ,decode(b.pg_obbligazione,null,b.esercizio_ori_accertamento,b.esercizio_ori_obbligazione)
    ,decode(b.pg_obbligazione,null,b.pg_accertamento,b.pg_obbligazione)
    ,decode(b.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,decode(b.pg_obbligazione,null,'N','Y')
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
from FATTURA_PASSIVA a, FATTURA_PASSIVA_RIGA b, OBBLIGAZIONE c, ACCERTAMENTO d, BENE_SERVIZIO f
where
     b.cd_cds = a.cd_cds
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.esercizio = a.esercizio
 and b.pg_fattura_passiva = a.pg_fattura_passiva
 and b.stato_cofi <> 'A'
 and c.cd_cds (+)= b.cd_cds_obbligazione
 and c.esercizio (+)= b.esercizio_obbligazione
 and c.esercizio_originale (+)= b.esercizio_ori_obbligazione
 and c.pg_obbligazione (+)= b.pg_obbligazione
 and d.cd_cds (+)= b.cd_cds_accertamento
 and d.esercizio (+)= b.esercizio_accertamento
 and d.esercizio_originale (+)= b.esercizio_ori_accertamento
 and d.pg_accertamento (+)= b.pg_accertamento
 and b.cd_bene_servizio = f.cd_bene_servizio
 group by
     a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_FATTURA_PASSIVA
    ,a.CD_TERZO
	,b.cd_bene_servizio
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,b.DT_DA_COMPETENZA_COGE
    ,b.DT_A_COMPETENZA_COGE
    ,b.TI_ISTITUZ_COMMERC
    ,decode(b.pg_obbligazione,null,b.esercizio_accertamento,b.esercizio_obbligazione)
    ,decode(b.pg_obbligazione,null,b.cd_cds_accertamento,b.cd_cds_obbligazione)
    ,decode(b.pg_obbligazione,null,b.esercizio_ori_accertamento,b.esercizio_ori_obbligazione)
    ,decode(b.pg_obbligazione,null,b.pg_accertamento,b.pg_obbligazione)
    ,decode(b.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,decode(b.pg_obbligazione,null,'N','Y')
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
union all
  select -- Estrazione fattura attiva
     'FATTURA_A'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_FATTURA_ATTIVA
    ,a.CD_TERZO
    ,decode(a.TI_CAUSALE_EMISSIONE,'B',b.progressivo_riga,null)  -- pg riga supportato solo per vendita di bene durevole
    ,null -- Bene servizio non supportato in fattura attiva
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,b.DT_DA_COMPETENZA_COGE
    ,b.DT_A_COMPETENZA_COGE
    ,'C'
    ,sum(b.IM_TOTALE_DIVISA)
    ,sum(b.IM_IMPONIBILE)
    ,sum(b.IM_IVA)
    ,sum(b.IM_DIPONIBILE_NC)
    ,decode(b.pg_obbligazione,null,b.esercizio_accertamento,b.esercizio_obbligazione)
    ,decode(b.pg_obbligazione,null,b.cd_cds_accertamento,b.cd_cds_obbligazione)
    ,decode(b.pg_obbligazione,null,b.esercizio_ori_accertamento,b.esercizio_ori_obbligazione)
    ,decode(b.pg_obbligazione,null,b.pg_accertamento,b.pg_obbligazione)
    ,decode(b.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,decode(b.pg_obbligazione,null,'N','Y')
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
from FATTURA_ATTIVA a, FATTURA_ATTIVA_RIGA b, OBBLIGAZIONE c, ACCERTAMENTO d
where
     b.cd_cds = a.cd_cds
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.esercizio = a.esercizio
 and b.pg_fattura_attiva = a.pg_fattura_attiva
 and b.stato_cofi <> 'A'
 and c.cd_cds (+)= b.cd_cds_obbligazione
 and c.esercizio (+)= b.esercizio_obbligazione
 and c.esercizio_originale (+)= b.esercizio_ori_obbligazione
 and c.pg_obbligazione (+)= b.pg_obbligazione
 and d.cd_cds (+)= b.cd_cds_accertamento
 and d.esercizio (+)= b.esercizio_accertamento
 and d.esercizio_originale (+)= b.esercizio_ori_accertamento
 and d.pg_accertamento (+)= b.pg_accertamento
 group by
     a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_FATTURA_ATTIVA
    ,a.CD_TERZO
    ,decode(a.TI_CAUSALE_EMISSIONE,'B',b.progressivo_riga,null)
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,b.DT_DA_COMPETENZA_COGE
    ,b.DT_A_COMPETENZA_COGE
    ,decode(b.pg_obbligazione,null,b.esercizio_accertamento,b.esercizio_obbligazione)
    ,decode(b.pg_obbligazione,null,b.cd_cds_accertamento,b.cd_cds_obbligazione)
    ,decode(b.pg_obbligazione,null,b.esercizio_ori_accertamento,b.esercizio_ori_obbligazione)
    ,decode(b.pg_obbligazione,null,b.pg_accertamento,b.pg_obbligazione)
    ,decode(b.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,decode(b.pg_obbligazione,null,'N','Y')
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
union all
select -- Estrazione documento generico
     a.cd_tipo_documento_amm
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_DOCUMENTO_GENERICO
    ,b.CD_TERZO
    ,b.progressivo_riga -- pg riga non supportato in fattura passiva
	,null -- codice bene servizio non supportato per generico
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,b.DT_DA_COMPETENZA_COGE
    ,b.DT_A_COMPETENZA_COGE
    ,a.ti_istituz_commerc
    ,sum(b.IM_RIGA_DIVISA)
    ,sum(b.IM_RIGA)
    ,0
    ,0
    ,decode(b.pg_obbligazione,null,b.esercizio_accertamento,b.esercizio_obbligazione)
    ,decode(b.pg_obbligazione,null,b.cd_cds_accertamento,b.cd_cds_obbligazione)
    ,decode(b.pg_obbligazione,null,b.esercizio_ori_accertamento,b.esercizio_ori_obbligazione)
    ,decode(b.pg_obbligazione,null,b.pg_accertamento,b.pg_obbligazione)
    ,decode(b.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,decode(b.pg_obbligazione,null,'N','Y')
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
from DOCUMENTO_GENERICO a, DOCUMENTO_GENERICO_RIGA b, OBBLIGAZIONE c, ACCERTAMENTO d
where
     b.cd_cds = a.cd_cds
 and b.cd_tipo_documento_amm = a.cd_tipo_documento_amm
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.esercizio = a.esercizio
 and b.pg_documento_generico = a.pg_documento_generico
 and b.stato_cofi <> 'A'
 and c.cd_cds (+)= b.cd_cds_obbligazione
 and c.esercizio (+)= b.esercizio_obbligazione
 and c.esercizio_originale (+)= b.esercizio_ori_obbligazione
 and c.pg_obbligazione (+)= b.pg_obbligazione
 and d.cd_cds (+)= b.cd_cds_accertamento
 and d.esercizio (+)= b.esercizio_accertamento
 and d.esercizio_originale (+)= b.esercizio_ori_accertamento
 and d.pg_accertamento (+)= b.pg_accertamento
 group by
     a.cd_tipo_documento_amm
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_DOCUMENTO_GENERICO
    ,b.CD_TERZO
    ,b.progressivo_riga
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,b.DT_DA_COMPETENZA_COGE
    ,b.DT_A_COMPETENZA_COGE
    ,a.ti_istituz_commerc
    ,decode(b.pg_obbligazione,null,b.esercizio_accertamento,b.esercizio_obbligazione)
    ,decode(b.pg_obbligazione,null,b.cd_cds_accertamento,b.cd_cds_obbligazione)
    ,decode(b.pg_obbligazione,null,b.esercizio_ori_accertamento,b.esercizio_ori_obbligazione)
    ,decode(b.pg_obbligazione,null,b.pg_accertamento,b.pg_obbligazione)
    ,decode(b.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,decode(b.pg_obbligazione,null,'N','Y')
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,decode(b.pg_obbligazione,null,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
union all
 select -- Estrazione anticipo
     'ANTICIPO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_ANTICIPO
    ,a.CD_TERZO
    ,0 -- pg riga non supportato in fattura passiva
	,null -- fl_bene/servizio non supportato per COMPENSO
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,a.dt_da_competenza_coge
    ,a.dt_a_competenza_coge
    ,'I'
    ,a.IM_ANTICIPO_DIVISA
    ,a.IM_ANTICIPO
    ,0
    ,0
    ,a.esercizio_obbligazione
    ,a.cd_cds_obbligazione
    ,a.esercizio_ori_obbligazione
    ,a.pg_obbligazione
    ,c.fl_pgiro
    ,c.esercizio
    ,c.TI_APPARTENENZA
    ,c.TI_GESTIONE
    ,c.CD_ELEMENTO_VOCE
    ,'Y'
    ,c.esercizio_ori_riporto
    ,c.cd_cds_ori_riporto
    ,c.esercizio_ori_ori_riporto
    ,c.pg_obbligazione_ori_riporto
    ,c.stato_coge_docamm
    ,c.stato_coge_doccont
 from ANTICIPO a, OBBLIGAZIONE c
 where
     c.cd_cds (+)= a.cd_cds_obbligazione
 and c.esercizio (+)= a.esercizio_obbligazione
 and c.esercizio_originale (+)= a.esercizio_ori_obbligazione
 and c.pg_obbligazione (+)= a.pg_obbligazione
union all
 select -- Estrazione rimborso
     'RIMBORSO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_RIMBORSO
    ,a.CD_TERZO
    ,0 -- pg riga non supportato in fattura passiva
	,null -- fl_bene/servizio non supportato per COMPENSO
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,a.dt_da_competenza_coge
    ,a.dt_a_competenza_coge
    ,'I'
    ,0
    ,a.IM_RIMBORSO
    ,0
    ,0
    ,a.esercizio_accertamento
    ,a.cd_cds_accertamento
    ,a.esercizio_ori_accertamento
    ,a.pg_accertamento
    ,d.fl_pgiro
    ,d.ESERCIZIO
    ,d.TI_APPARTENENZA
    ,d.TI_GESTIONE
    ,d.CD_ELEMENTO_VOCE
    ,'N'
    ,d.esercizio_ori_riporto
    ,d.cd_cds_ori_riporto
    ,d.esercizio_ori_ori_riporto
    ,d.pg_accertamento_ori_riporto
    ,d.stato_coge_docamm
    ,d.stato_coge_doccont
 from RIMBORSO a, ACCERTAMENTO d
 where
     d.esercizio (+)= a.esercizio_accertamento
 and d.cd_cds (+)= a.cd_cds_accertamento
 and d.esercizio_originale (+)= a.esercizio_ori_accertamento
 and d.pg_accertamento (+)= a.pg_accertamento
 union all
 select -- Estrazione missione definitiva senza compenso e (senza anticipo o con anticipo minore di dell'importo della missione)
     'MISSIONE'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_MISSIONE
    ,a.CD_TERZO
    ,0 -- pg riga non supportato in fattura passiva
	,null -- fl_bene/servizio non supportato per COMPENSO
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,a.DT_INIZIO_MISSIONE --DT_DA_COMPETENZA_COGE
    ,a.DT_FINE_MISSIONE --DT_A_COMPETENZA_COGE
    ,'I'
    ,a.IM_TOTALE_MISSIONE
    ,a.IM_TOTALE_MISSIONE
    ,0
    ,0
    ,a.esercizio_obbligazione
    ,a.cd_cds_obbligazione
    ,a.esercizio_ori_obbligazione
    ,a.pg_obbligazione
    ,c.fl_pgiro
    ,c.esercizio
    ,c.TI_APPARTENENZA
    ,c.TI_GESTIONE
    ,c.CD_ELEMENTO_VOCE
    ,'Y'
    ,c.esercizio_ori_riporto
    ,c.cd_cds_ori_riporto
    ,c.esercizio_ori_ori_riporto
    ,c.pg_obbligazione_ori_riporto
    ,c.stato_coge_docamm
    ,c.stato_coge_doccont
 from MISSIONE a, OBBLIGAZIONE c
 where
     a.pg_obbligazione is not null
 and c.cd_cds = a.cd_cds_obbligazione
 and c.esercizio = a.esercizio_obbligazione
 and c.esercizio_originale = a.esercizio_ori_obbligazione
 and c.pg_obbligazione = a.pg_obbligazione
 and a.fl_associato_compenso = 'N'
 and a.ti_provvisorio_definitivo = 'D'
union all
 select -- Estrazione missione definitiva senza compenso e con anticipo maggiore o uguale alla missione
     'MISSIONE'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_MISSIONE
    ,a.CD_TERZO
    ,0 -- pg riga non supportato in fattura passiva
	,null -- fl_bene/servizio non supportato per COMPENSO
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,a.DT_INIZIO_MISSIONE --DT_DA_COMPETENZA_COGE
    ,a.DT_FINE_MISSIONE --DT_A_COMPETENZA_COGE
    ,'I'
    ,a.IM_TOTALE_MISSIONE
    ,a.IM_TOTALE_MISSIONE
    ,0
    ,0
    ,b.esercizio_obbligazione
    ,b.cd_cds_obbligazione
    ,b.esercizio_ori_obbligazione
    ,b.pg_obbligazione
    ,c.fl_pgiro
    ,c.esercizio
    ,c.TI_APPARTENENZA
    ,c.TI_GESTIONE
    ,c.CD_ELEMENTO_VOCE
    ,'Y'
    ,c.esercizio_ori_riporto
    ,c.cd_cds_ori_riporto
    ,c.esercizio_ori_ori_riporto
    ,c.pg_obbligazione_ori_riporto
    ,c.stato_coge_docamm
    ,c.stato_coge_doccont
 from MISSIONE a, ANTICIPO b, OBBLIGAZIONE c
 where
     a.pg_obbligazione is null
 and b.cd_cds = a.cd_cds_anticipo
 and b.cd_unita_organizzativa = a.cd_uo_anticipo
 and b.esercizio = a.esercizio_anticipo
 and b.pg_anticipo = a.pg_anticipo
 and c.cd_cds = b.cd_cds_obbligazione
 and c.esercizio = b.esercizio_obbligazione
 and c.esercizio_originale = b.esercizio_ori_obbligazione
 and c.pg_obbligazione = b.pg_obbligazione
 and a.fl_associato_compenso = 'N'
 and a.ti_provvisorio_definitivo = 'D'
 union all
 select -- Estrazione compenso non associato a missione o associato a missione e con obbligazione
     'COMPENSO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_COMPENSO
    ,a.CD_TERZO
    ,0 -- pg riga non supportato in fattura passiva
	,null -- fl_bene/servizio non supportato per COMPENSO
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,a.DT_DA_COMPETENZA_COGE
    ,a.DT_A_COMPETENZA_COGE
    ,a.ti_istituz_commerc
    ,0
    ,a.IM_LORDO_PERCIPIENTE
    ,0
    ,0
    ,decode(a.pg_obbligazione,null,a.esercizio_accertamento,a.esercizio_obbligazione)
    ,decode(a.pg_obbligazione,null,a.cd_cds_accertamento,a.cd_cds_obbligazione)
    ,decode(a.pg_obbligazione,null,a.esercizio_ori_accertamento,a.esercizio_ori_obbligazione)
    ,decode(a.pg_obbligazione,null,a.pg_accertamento,a.pg_obbligazione)
    ,decode(a.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(a.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(a.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(a.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(a.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,decode(a.pg_obbligazione,null,'N','Y')
    ,decode(a.pg_obbligazione,null,d.esercizio_ori_riporto,c.esercizio_ori_riporto)
    ,decode(a.pg_obbligazione,null,d.cd_cds_ori_riporto,c.cd_cds_ori_riporto)
    ,decode(a.pg_obbligazione,null,d.esercizio_ori_ori_riporto,c.esercizio_ori_ori_riporto)
    ,decode(a.pg_obbligazione,null,d.pg_accertamento_ori_riporto,c.pg_obbligazione_ori_riporto)
    ,DECODE(a.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(a.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
 from COMPENSO a, OBBLIGAZIONE c, ACCERTAMENTO d
 where
 (
        a.pg_missione is null
	 or a.pg_missione is not null and a.pg_obbligazione is not null
 )
 and c.cd_cds (+)= a.cd_cds_obbligazione
 and c.esercizio (+)= a.esercizio_obbligazione
 and c.esercizio_originale (+)= a.esercizio_ori_obbligazione
 and c.pg_obbligazione (+)= a.pg_obbligazione
 and d.cd_cds (+)= a.cd_cds_accertamento
 and d.esercizio (+)= a.esercizio_accertamento
 and d.esercizio_originale (+)= a.esercizio_ori_accertamento
 and d.pg_accertamento (+)= a.pg_accertamento
 union all
 select -- Estrazione compenso associato a missione e senza obbligazione (anticipo >= totale missione)
     'COMPENSO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_COMPENSO
    ,a.CD_TERZO
    ,0 -- pg riga non supportato in fattura passiva
	,null -- fl_bene/servizio non supportato per COMPENSO
--  AGGREGATI DI RIGA PER FORNITORE - VOCE DEL PIANO (E o S)
    ,a.DT_DA_COMPETENZA_COGE
    ,a.DT_A_COMPETENZA_COGE
    ,a.ti_istituz_commerc
    ,0
    ,a.IM_LORDO_PERCIPIENTE
    ,0
    ,0
    ,c.esercizio_obbligazione
    ,c.cd_cds_obbligazione
    ,c.esercizio_ori_obbligazione
    ,c.pg_obbligazione
    ,d.fl_pgiro
    ,d.esercizio
    ,d.TI_APPARTENENZA
    ,d.TI_GESTIONE
    ,d.CD_ELEMENTO_VOCE
    ,'Y'
    ,d.esercizio_ori_riporto
    ,d.cd_cds_ori_riporto
    ,d.esercizio_ori_ori_riporto
    ,d.pg_obbligazione_ori_riporto
    ,d.stato_coge_docamm
    ,d.stato_coge_doccont
 from COMPENSO a, MISSIONE b, ANTICIPO c, OBBLIGAZIONE d
 where
     a.pg_missione is not null
 and a.pg_obbligazione is null
 and b.cd_cds = a.cd_cds_missione
 and b.cd_unita_organizzativa = a.cd_uo_missione
 and b.esercizio = a.esercizio_missione
 and b.pg_missione = a.pg_missione
 and c.cd_cds = b.cd_cds_anticipo
 and c.cd_unita_organizzativa = b.cd_uo_anticipo
 and c.esercizio = b.esercizio_anticipo
 and c.pg_anticipo = b.pg_anticipo
 and d.cd_cds = c.cd_cds_obbligazione
 and d.esercizio = c.esercizio_obbligazione
 and d.esercizio_originale = c.esercizio_ori_obbligazione
 and d.pg_obbligazione 			= c.pg_obbligazione
 );

   COMMENT ON TABLE "V_DOC_AMM_COGE_RIGA"  IS 'Vista di estrazione dei dettagli di documento amministrativo con valorizzazioni a fini COGE';
