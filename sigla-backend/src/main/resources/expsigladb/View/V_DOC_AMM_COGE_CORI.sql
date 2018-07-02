--------------------------------------------------------
--  DDL for View V_DOC_AMM_COGE_CORI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DOC_AMM_COGE_CORI" ("CD_TIPO_DOCUMENTO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_NUMERO_DOCUMENTO", "CD_TERZO", "CD_TERZO_CORI", "DT_DA_COMPETENZA_COGE", "DT_A_COMPETENZA_COGE", "CD_CONTRIBUTO_RITENUTA", "TI_ENTE_PERCEPIENTE", "TI_ISTITUZ_COMMERC", "AMMONTARE", "FL_PGIRO", "ESERCIZIO_EV", "TI_APPARTENENZA_EV", "TI_GESTIONE_EV", "CD_ELEMENTO_VOCE_EV", "STATO_COGE_DOCAMM", "STATO_COGE_DOCCONT") AS 
  (
select -- Estrazione IVA fattura passiva
--
-- Date: 18/07/2006
-- Version: 1.9
--
-- Vista di estrazione dei dettagli cori di documento amministrativo con valorizzazioni a fini COGE
--
-- History:
--
-- Date: 15/05/2002
-- Version: 1.0
-- Creazione
--
-- Date: 16/05/2002
-- Version: 1.1
-- L'iva commerciale intra ue/san marino non deve essere considerata in economica
--
-- Date: 10/06/2002
-- Version: 1.2
-- Sistemata estrazione acc/obb su CORI compenso
--
-- Date: 11/06/2002
-- Version: 1.3
-- Fix errore estrazione CORI Fattura attiva e passiva
--
-- Date: 31/10/2002
-- Version: 1.4
-- Estrazione del fl_pgiro del primo documento collegato a fattura attiva e passiva per gestione in deroga dei cori
-- Richiesta CINECA 30/10/2002
--
-- Date: 13/11/2002
-- Version: 1.5
-- Estratto il terzo relativo al CORI oltre che a quello del documento, perchè può essere diverso da quello del documento
--
-- Date: 28/11/2002
-- Version: 1.6
-- Aggiunto il tipo istituzionale/commerciale
--
-- Date: 03/12/2002
-- Version: 1.7
-- Iva istituzionale filtrata
--
-- Date: 15/07/2003
-- Version: 1.8
-- L'iva commerciale viene esclusa solo quando esiste l'autofattura
--
-- Date: 18/07/2006
-- Version: 1.9
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
    ,a.CD_TERZO
    ,a.DT_DA_COMPETENZA_COGE
    ,a.DT_A_COMPETENZA_COGE
	,f.val01
	,'E'
    ,b.TI_ISTITUZ_COMMERC
    ,sum(b.IM_IVA)
    ,decode(b.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
from FATTURA_PASSIVA a, FATTURA_PASSIVA_RIGA b, OBBLIGAZIONE c, ACCERTAMENTO d, CONFIGURAZIONE_CNR f,voce_iva
where
     b.cd_cds = a.cd_cds
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.esercizio = a.esercizio
 and b.pg_fattura_passiva = a.pg_fattura_passiva
 and f.cd_chiave_primaria = 'CORI_SPECIALE'
 and f.cd_chiave_secondaria = 'IVA'
 and f.esercizio = 0
 and f.cd_unita_funzionale = '*'
 and b.im_iva <> 0
 -- Iva istituzionale va a costo
 and b.ti_istituz_commerc = 'C'
 -- escluso solo i dettagli con autofattura = NO
 and voce_iva.cd_voce_iva=b.cd_voce_iva
 and ( not exists (select 1 from autofattura where
                                   esercizio=a.esercizio
                               and cd_cds_ft_passiva=a.cd_cds
                               and cd_uo_ft_passiva=a.cd_unita_organizzativa
                               and pg_fattura_passiva=a.pg_fattura_passiva)
     or
      (exists (select 1 from autofattura where
                                   esercizio=a.esercizio
                               and cd_cds_ft_passiva=a.cd_cds
                               and cd_uo_ft_passiva=a.cd_unita_organizzativa
                               and pg_fattura_passiva=a.pg_fattura_passiva)
       and voce_iva.fl_autofattura='N' and
       a.fl_intra_ue='N' and a.fl_extra_ue = 'N'  and a.fl_san_marino_senza_iva = 'N' and a.fl_split_payment ='N' ))
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
    ,a.PG_FATTURA_PASSIVA
    ,a.CD_TERZO
    ,a.CD_TERZO
    ,a.DT_DA_COMPETENZA_COGE
    ,a.DT_A_COMPETENZA_COGE
    ,f.val01
	,decode(b.TI_ISTITUZ_COMMERC,'I','P','E')
    ,b.TI_ISTITUZ_COMMERC
    ,decode(b.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
union all
  select  -- Estrazione IVA fattura attiva
    'FATTURA_A'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_FATTURA_ATTIVA
    ,a.CD_TERZO
    ,a.CD_TERZO
    ,a.DT_DA_COMPETENZA_COGE
    ,a.DT_A_COMPETENZA_COGE
    ,f.val01
    ,'E'
    ,'C'
    ,sum(b.IM_IVA)
    ,decode(b.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
from FATTURA_ATTIVA a, FATTURA_ATTIVA_RIGA b, OBBLIGAZIONE c, ACCERTAMENTO d, CONFIGURAZIONE_CNR f
where
     b.cd_cds = a.cd_cds
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.esercizio = a.esercizio
 and b.pg_fattura_attiva = a.pg_fattura_attiva
 and f.cd_chiave_primaria = 'CORI_SPECIALE'
 and f.cd_chiave_secondaria = 'IVA'
 and f.esercizio = 0
 and f.cd_unita_funzionale = '*'
 and b.im_iva <> 0
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
    ,a.CD_TERZO
    ,a.DT_DA_COMPETENZA_COGE
    ,a.DT_A_COMPETENZA_COGE
    ,f.val01
    ,'E'
    ,decode(b.pg_obbligazione,null,d.fl_pgiro,c.fl_pgiro)
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCAMM,c.STATO_COGE_DOCAMM)
    ,DECODE(b.pg_obbligazione,null,d.STATO_COGE_DOCCONT,c.STATO_COGE_DOCCONT)
union all
 select  -- Estrazione CORI COMPENSO
    'COMPENSO'
    ,a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.PG_COMPENSO
    ,a.CD_TERZO
    ,decode(b.pg_obbligazione,null,d.cd_terzo,c.cd_terzo)
    ,a.DT_DA_COMPETENZA_COGE
    ,a.DT_A_COMPETENZA_COGE
    ,b.cd_contributo_ritenuta
    ,b.ti_ente_percipiente
    ,a.TI_ISTITUZ_COMMERC
    ,sum(b.ammontare)
    ,'N' -- Nono esiste una gestione speciale economica per i CORI del compenso relativamente alle partite di giro
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,DECODE(a.pg_obbligazione,null,null,obb_comp.STATO_COGE_DOCAMM)
    ,DECODE(a.pg_obbligazione,null,null,obb_comp.STATO_COGE_DOCCONT)
 from COMPENSO a, CONTRIBUTO_RITENUTA b, OBBLIGAZIONE c, ACCERTAMENTO d, OBBLIGAZIONE obb_comp
  where
     b.cd_cds = a.cd_cds
 and b.cd_unita_organizzativa = a.cd_unita_organizzativa
 and b.esercizio = a.esercizio
 and b.pg_compenso = a.pg_compenso
 and b.ammontare <> 0
 and c.cd_cds (+)= b.cd_cds
 and c.esercizio (+)= b.esercizio
 and c.esercizio_originale (+)= b.esercizio_ori_obbligazione
 and c.pg_obbligazione (+)= b.pg_obbligazione
 and obb_comp.cd_cds (+)= a.CD_CDS_OBBLIGAZIONE
 and obb_comp.esercizio (+)= a.esercizio_obbligazione
 and obb_comp.esercizio_originale (+)= a.esercizio_ori_obbligazione
 and obb_comp.pg_obbligazione (+)= a.pg_obbligazione
 and d.cd_cds (+)= b.cd_cds
 and d.esercizio (+)= b.esercizio
 and d.esercizio_originale (+)= b.esercizio_ori_accertamento
 and d.pg_accertamento (+)= b.pg_accertamento
 group by
     a.CD_CDS
    ,a.CD_UNITA_ORGANIZZATIVA
    ,a.ESERCIZIO
    ,a.pg_compenso
    ,a.CD_TERZO
    ,decode(b.pg_obbligazione,null,d.cd_terzo,c.cd_terzo)
    ,a.DT_DA_COMPETENZA_COGE
    ,a.DT_A_COMPETENZA_COGE
    ,b.cd_contributo_ritenuta
    ,b.ti_ente_percipiente
    ,a.TI_ISTITUZ_COMMERC
    ,decode(b.pg_obbligazione,null,d.ESERCIZIO,c.esercizio)
    ,decode(b.pg_obbligazione,null,d.TI_APPARTENENZA,c.TI_APPARTENENZA)
    ,decode(b.pg_obbligazione,null,d.TI_GESTIONE,c.TI_GESTIONE)
    ,decode(b.pg_obbligazione,null,d.CD_ELEMENTO_VOCE,c.CD_ELEMENTO_VOCE)
    ,DECODE(a.pg_obbligazione,null,null,obb_comp.STATO_COGE_DOCAMM)
    ,DECODE(a.pg_obbligazione,null,null,obb_comp.STATO_COGE_DOCCONT)
);

   COMMENT ON TABLE "V_DOC_AMM_COGE_CORI"  IS 'Vista di estrazione dei dettagli cori di documento amministrativo con valorizzazioni a fini COGE';
