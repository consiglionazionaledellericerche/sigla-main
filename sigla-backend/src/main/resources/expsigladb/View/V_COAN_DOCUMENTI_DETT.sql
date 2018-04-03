--------------------------------------------------------
--  DDL for View V_COAN_DOCUMENTI_DETT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_COAN_DOCUMENTI_DETT" ("CD_TIPO_DOCUMENTO_AMM", "CD_TERZO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_NUMERO_DOCUMENTO", "TI_FATTURA", "PROGRESSIVO_RIGA", "TI_ISTITUZ_COMMERC", "CD_CDS_OBB_ACC", "ESERCIZIO_OBB_ACC", "ESERCIZIO_ORI_OBB_ACC", "PG_OBB_ACC", "PG_OBB_ACC_SCADENZARIO", "CD_VOCE_IVA", "IM_DIPONIBILE_NC", "IM_IMPONIBILE", "IM_IVA", "PERCENTUALE_DETRAIBILITA", "IM_IVA_DETRAIBILE", "FL_OBBIGAZIONE", "CD_CDS_ASSNCNA_FIN", "CD_UO_ASSNCNA_FIN", "ESERCIZIO_ASSNCNA_FIN", "PG_FATTURA_ASSNCNA_FIN", "PG_RIGA_ASSNCNA_FIN") AS 
  select
--
-- Date: 18/07/2006
-- Version: 1.12
--
-- Vista di estrazione del dettaglio dei documenti gestiti nella movimentazione coan
--
-- History:
--
-- Date: 03/04/2002
-- Version: 1.0
-- Creazione
--
-- Date: 04/06/2002
-- Version: 1.1
-- Introduzione dei compensi
--
-- Date: 10/06/2002
-- Version: 1.2
-- Introduzione della gestione per il collegamento tra Documento e (OBBLIGAZIONE o ACCERTAMENTO)
--
-- Date: 12/06/2002
-- Version: 1.3
-- Introduzione del Flag per stabilire se esiste un legame con obbligazione o con accertamenti
--
-- Date: 24/06/2002
-- Version: 1.4
-- Introdzione dei campi per la gestione delle NC associate ad un diverso documento
--
-- Date: 01/07/2002
-- Version: 1.5
-- Fix : Modificata Join tra v_coan_documenti FAT  e documento_generico_riga RIGA introducendo l'uguaglianza FAT.cd_tipo_documento_Amm  = RIGA.cd_tipo_documento_Amm
--
-- Date: 29/07/2002
-- Version: 1.7
-- Introdotta Missione
--
-- Date: 13/06/2003
-- Version: 1.8
-- Ottimizzazione
--
-- Date: 09/09/2003
-- Version: 1.9
-- Estrazione del valore assoluto del totale compenso
--
-- Date: 27/01/2004
-- Version: 1.10
-- Gestione missioni e compensi con anticipo > dell'importo della missione  o compenso
--
-- Date: 17/02/2004
-- Version: 1.11
-- Fix errore calcolo del'iva detraibile attraverso la percentuale di detraibilità su fatture attive e passive
--
-- Date: 18/07/2006
-- Version: 1.12
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
 'FATTURA_A'
,fat.CD_TERZO
,riga.CD_CDS
,riga.CD_UNITA_ORGANIZZATIVA
,riga.ESERCIZIO
,riga.PG_FATTURA_ATTIVA PG_NUMERO_DOCUMENTO
,fat.TI_FATTURA
,riga.PROGRESSIVO_RIGA
,'C' TI_ISTITUZ_COMMERC
,NVL(riga.CD_CDS_OBBLIGAZIONE, riga.CD_CDS_ACCERTAMENTO)
,NVL(riga.ESERCIZIO_OBBLIGAZIONE, riga.ESERCIZIO_ACCERTAMENTO)
,NVL(riga.ESERCIZIO_ORI_OBBLIGAZIONE, riga.ESERCIZIO_ORI_ACCERTAMENTO)
,NVL(riga.PG_OBBLIGAZIONE, riga.PG_ACCERTAMENTO)
,NVL(riga.PG_OBBLIGAZIONE_SCADENZARIO, riga.PG_ACCERTAMENTO_SCADENZARIO)
,riga.CD_VOCE_IVA
,riga.IM_DIPONIBILE_NC
,riga.IM_IMPONIBILE
,riga.IM_IVA
,iva.PERCENTUALE_DETRAIBILITA
,decode(iva.FL_DETRAIBILE,'Y',riga.IM_IVA*iva.PERCENTUALE_DETRAIBILITA/100,0) IM_IVA_DETRAIBILE
,decode(NVL(riga.CD_CDS_OBBLIGAZIONE, '-9'),'-9','N','Y')
,riga.CD_CDS_ASSNCNA_FIN
,riga.CD_UO_ASSNCNA_FIN
,riga.ESERCIZIO_ASSNCNA_FIN
,riga.PG_FATTURA_ASSNCNA_FIN
,riga.PG_RIGA_ASSNCNA_FIN
from fattura_attiva fat
,fattura_attiva_riga riga
,voce_iva iva
where
    fat.CD_CDS                 = riga.CD_CDS
and fat.CD_UNITA_ORGANIZZATIVA = riga.CD_UNITA_ORGANIZZATIVA
and fat.ESERCIZIO              = riga.ESERCIZIO
and fat.PG_FATTURA_ATTIVA    = riga.PG_FATTURA_ATTIVA
and riga.CD_VOCE_IVA           = iva.CD_VOCE_IVA
UNION ALL
select
 'FATTURA_P'
,fat.CD_TERZO
,riga.CD_CDS
,riga.CD_UNITA_ORGANIZZATIVA
,riga.ESERCIZIO
,riga.PG_FATTURA_PASSIVA PG_NUMERO_DOCUMENTO
,fat.TI_FATTURA
,riga.PROGRESSIVO_RIGA
,riga.TI_ISTITUZ_COMMERC
,NVL(riga.CD_CDS_OBBLIGAZIONE, riga.CD_CDS_ACCERTAMENTO)
,NVL(riga.ESERCIZIO_OBBLIGAZIONE, riga.ESERCIZIO_ACCERTAMENTO)
,NVL(riga.ESERCIZIO_ORI_OBBLIGAZIONE, riga.ESERCIZIO_ORI_ACCERTAMENTO)
,NVL(riga.PG_OBBLIGAZIONE, riga.PG_ACCERTAMENTO)
,NVL(riga.PG_OBBLIGAZIONE_SCADENZARIO, riga.PG_ACCERTAMENTO_SCADENZARIO)
,riga.CD_VOCE_IVA
,riga.IM_DIPONIBILE_NC
,riga.IM_IMPONIBILE
,riga.IM_IVA
,iva.PERCENTUALE_DETRAIBILITA
,decode(iva.FL_DETRAIBILE,'Y',riga.IM_IVA*iva.PERCENTUALE_DETRAIBILITA/100,0) IM_IVA_DETRAIBILE
,decode(NVL(riga.CD_CDS_OBBLIGAZIONE, '-9'),'-9','N','Y')
,riga.CD_CDS_ASSNCNA_FIN
,riga.CD_UO_ASSNCNA_FIN
,riga.ESERCIZIO_ASSNCNA_FIN
,riga.PG_FATTURA_ASSNCNA_FIN
,riga.PG_RIGA_ASSNCNA_FIN
from fattura_passiva fat
,fattura_passiva_riga riga
,voce_iva iva
where
    fat.CD_CDS                 = riga.CD_CDS
and fat.CD_UNITA_ORGANIZZATIVA = riga.CD_UNITA_ORGANIZZATIVA
and fat.ESERCIZIO              = riga.ESERCIZIO
and fat.PG_FATTURA_PASSIVA     = riga.PG_FATTURA_PASSIVA
and riga.CD_VOCE_IVA           = iva.CD_VOCE_IVA
UNION ALL
select
 fat.CD_TIPO_DOCUMENTO_AMM CD_TIPO_DOCUMENTO_AMM
,riga.CD_TERZO
,riga.CD_CDS
,riga.CD_UNITA_ORGANIZZATIVA
,riga.ESERCIZIO
,riga.PG_DOCUMENTO_GENERICO PG_NUMERO_DOCUMENTO
,NULL
,riga.PROGRESSIVO_RIGA
,'C' TI_ISTITUZ_COMMERC
,NVL(riga.CD_CDS_OBBLIGAZIONE, riga.CD_CDS_ACCERTAMENTO)
,NVL(riga.ESERCIZIO_OBBLIGAZIONE, riga.ESERCIZIO_ACCERTAMENTO)
,Nvl(riga.ESERCIZIO_ORI_OBBLIGAZIONE, riga.ESERCIZIO_ORI_ACCERTAMENTO)
,NVL(riga.PG_OBBLIGAZIONE, riga.PG_ACCERTAMENTO)
,NVL(riga.PG_OBBLIGAZIONE_SCADENZARIO, riga.PG_ACCERTAMENTO_SCADENZARIO)
,'0'
,0
,riga.IM_RIGA
,0 IM_IVA
,0
,0 IM_IVA_DETRAIBILE
,decode(NVL(riga.CD_CDS_OBBLIGAZIONE, '-9'),'-9','N','Y')
,NULL
,NULL
,to_number(NULL)
,to_number(NULL)
,to_number(NULL)
from
 documento_generico fat
,documento_generico_riga riga
where
    fat.CD_CDS                 = riga.CD_CDS
and fat.CD_UNITA_ORGANIZZATIVA = riga.CD_UNITA_ORGANIZZATIVA
and fat.ESERCIZIO              = riga.ESERCIZIO
and fat.cd_tipo_documento_Amm  = riga.cd_tipo_documento_Amm
and fat.PG_DOCUMENTO_GENERICO    = riga.PG_DOCUMENTO_GENERICO
UNION ALL
select
 'COMPENSO'
,comp.CD_TERZO
,comp.CD_CDS
,comp.CD_UNITA_ORGANIZZATIVA
,comp.ESERCIZIO
,comp.PG_COMPENSO
,null
,0
,'C'
,NVL(riga.CD_CDS_OBBLIGAZIONE, comp.CD_CDS_ACCERTAMENTO)
,NVL(riga.ESERCIZIO_OBBLIGAZIONE, comp.ESERCIZIO_ACCERTAMENTO)
,Nvl(riga.ESERCIZIO_ORI_OBBLIGAZIONE, comp.ESERCIZIO_ORI_ACCERTAMENTO)
,NVL(riga.PG_OBBLIGAZIONE, comp.PG_ACCERTAMENTO)
,NVL(riga.PG_OBBLIGAZIONE_SCADENZARIO, comp.PG_ACCERTAMENTO_SCADENZARIO)
,null
,0
,abs(nvl(comp.IM_TOTALE_COMPENSO, riga.IM_TOTALE_RIGA_COMPENSO))
,0
,0
,0
,decode(NVL(riga.CD_CDS_OBBLIGAZIONE, '-9'),'-9','N','Y')
,NULL
,NULL
,to_number(NULL)
,to_number(NULL)
,to_number(NULL)
from
    compenso comp, compenso_riga riga
where comp.cd_cds = riga.cd_cds (+)
and   comp.cd_unita_organizzativa = riga.cd_unita_organizzativa (+)
and   comp.esercizio = riga.esercizio (+)
and   comp.pg_compenso = riga.pg_compenso (+)
and  (riga.cd_cds_obbligazione is not null or 
      comp.cd_cds_accertamento is not null)
UNION ALL
select
 'MISSIONE'
,miss.CD_TERZO
,miss.CD_CDS
,miss.CD_UNITA_ORGANIZZATIVA
,miss.ESERCIZIO
,miss.PG_MISSIONE
,null
,0
,miss.TI_ISTITUZ_COMMERC
,riga.CD_CDS_OBBLIGAZIONE
,riga.ESERCIZIO_OBBLIGAZIONE
,riga.ESERCIZIO_ORI_OBBLIGAZIONE
,riga.PG_OBBLIGAZIONE
,riga.PG_OBBLIGAZIONE_SCADENZARIO
,NULL
,0
,riga.IM_TOTALE_RIGA_MISSIONE
,0
,0
,0
,'Y'
,NULL
,NULL
,to_number(NULL)
,to_number(NULL)
,to_number(NULL)
from
 missione miss, missione_riga riga
where miss.cd_cds = riga.cd_cds
and   miss.cd_unita_organizzativa = riga.cd_unita_organizzativa
and   miss.esercizio = riga.esercizio
and   miss.pg_missione = riga.pg_missione
UNION ALL
-- Gestione missioni collegate ad anticipo con importo maggiore o uguale a quello della missione
select
 'MISSIONE'
,a.CD_TERZO
,a.CD_CDS
,a.CD_UNITA_ORGANIZZATIVA
,a.ESERCIZIO
,a.PG_MISSIONE
,null
,0
,a.TI_ISTITUZ_COMMERC
,b.CD_CDS_OBBLIGAZIONE
,b.ESERCIZIO_OBBLIGAZIONE
,b.ESERCIZIO_ORI_OBBLIGAZIONE
,b.PG_OBBLIGAZIONE
,b.PG_OBBLIGAZIONE_SCADENZARIO
,NULL
,0
,b.IM_ANTICIPO
,0
,0
,0
,'Y'
,NULL
,NULL
,to_number(NULL)
,to_number(NULL)
,to_number(NULL)
from
 missione a, anticipo b
where b.cd_cds = a.cd_cds_anticipo
  and b.cd_unita_organizzativa = a.cd_uo_anticipo
  and b.esercizio = a.esercizio_anticipo
  and b.pg_anticipo = a.pg_anticipo
UNION ALL
-- Gestione dei compensi collegati ad anticipo con importo maggiore o uguale al compenso
select
 'COMPENSO'
,a.CD_TERZO
,a.CD_CDS
,a.CD_UNITA_ORGANIZZATIVA
,a.ESERCIZIO
,a.PG_COMPENSO
,null
,0
,'C'
,c.CD_CDS_OBBLIGAZIONE
,c.ESERCIZIO_OBBLIGAZIONE
,c.ESERCIZIO_ORI_OBBLIGAZIONE
,c.PG_OBBLIGAZIONE
,c.PG_OBBLIGAZIONE_SCADENZARIO
,null
,0
,abs(a.IM_TOTALE_COMPENSO)
,0
,0
,0
,'Y'
,NULL
,NULL
,to_number(NULL)
,to_number(NULL)
,to_number(NULL)
from
    compenso a, missione b, anticipo c
where
     a.pg_missione is not null
 and a.pg_accertamento is null
 and not exists(select 1 from compenso_riga cr
                where cr.cd_cds = a.cd_cds
                AND   cr.cd_unita_organizzativa = a.cd_unita_organizzativa
                AND   cr.esercizio = a.esercizio
                AND   cr.pg_compenso = a.pg_compenso)
 and b.cd_cds = a.cd_cds_missione
 and b.cd_unita_organizzativa = a.cd_uo_missione
 and b.esercizio = a.esercizio_missione
 and b.pg_missione = a.pg_missione
 and c.cd_cds = b.cd_cds_anticipo
 and c.cd_unita_organizzativa = b.cd_uo_anticipo
 and c.esercizio = b.esercizio_anticipo
 and c.pg_anticipo = b.pg_anticipo;

   COMMENT ON TABLE "V_COAN_DOCUMENTI_DETT"  IS 'Vista di estrazione del dettaglio dei documenti gestiti nella movimentazione coan';
