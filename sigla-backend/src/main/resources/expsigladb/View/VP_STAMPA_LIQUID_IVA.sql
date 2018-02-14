--------------------------------------------------------
--  DDL for View VP_STAMPA_LIQUID_IVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_STAMPA_LIQUID_IVA" ("TIPO_LIQUIDAZIONE", "CD_CDS", "ESERCIZIO", "CD_UNITA_ORGANIZZATIVA", "DT_INIZIO", "DT_FINE", "REPORT_ID", "STATO", "ANNOTAZIONI", "DT_VERSAMENTO", "COD_AZIENDA", "CAB", "CD_CDS_OBB_ACCENTR", "ESERCIZIO_OBB_ACCENTR", "ESERCIZIO_ORI_OBB_ACCENTR", "PG_OBB_ACCENTR", "IVA_VENDITE", "IVA_VENDITE_DIFF", "IVA_VEND_DIFF_ESIG", "IVA_AUTOFATT", "IVA_INTRAUE", "IVA_DEBITO", "IVA_ACQUISTI", "IVA_ACQ_NON_DETR", "IVA_ACQUISTI_DIFF", "IVA_ACQ_DIFF_ESIG", "IVA_CREDITO", "VAR_IMP_PER_PREC", "IVA_NON_VERS_PER_PREC", "IVA_DEB_CRED_PER_PREC", "CRED_IVA_COMP_DETR", "IVA_DEB_CRED", "INT_DEB_LIQ_TRIM", "CRED_IVA_SPEC_DETR", "ACCONTO_IVA_VERS", "IVA_DA_VERSARE", "IVA_VERSATA", "CRED_IVA_INFRANN_RIMB", "CRED_IVA_INFRANN_COMP", "CD_TIPO_DOCUMENTO", "CD_CDS_DOC_AMM", "CD_UO_DOC_AMM", "ESERCIZIO_DOC_AMM", "PG_DOC_AMM", "IVA_CREDITO_NO_PRORATA", "PERC_PRORATA_DETRAIBILE", "RAGIONE_SOCIALE", "CODICE_FISCALE", "PARTITA_IVA", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC", "ABI") AS 
  select
--
-- Date: 19/07/2006
-- Version: 1.1
--
-- Vista di stampa della liquidazione IVA
--
-- History:
--
-- Date: 26/02/2003
-- Version: 1.0
-- Creazione Vista
--
-- Date: 19/07/2006
-- Version: 1.1
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
--
     aDest.TIPO_LIQUIDAZIONE
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.DT_INIZIO
    ,aDest.DT_FINE
    ,aDest.REPORT_ID
    ,aDest.STATO
    ,aDest.ANNOTAZIONI
    ,aDest.DT_VERSAMENTO
    ,aDest.COD_AZIENDA
    ,aDest.CAB
    ,aDest.CD_CDS_OBB_ACCENTR
    ,aDest.ESERCIZIO_OBB_ACCENTR
    ,aDest.ESERCIZIO_ORI_OBB_ACCENTR
    ,aDest.PG_OBB_ACCENTR
    ,aDest.IVA_VENDITE
    ,aDest.IVA_VENDITE_DIFF
    ,aDest.IVA_VEND_DIFF_ESIG
    ,aDest.IVA_AUTOFATT
    ,aDest.IVA_INTRAUE
    ,aDest.IVA_DEBITO
    ,aDest.IVA_ACQUISTI
    ,aDest.IVA_ACQ_NON_DETR
    ,aDest.IVA_ACQUISTI_DIFF
    ,aDest.IVA_ACQ_DIFF_ESIG
    ,aDest.IVA_CREDITO
    ,aDest.VAR_IMP_PER_PREC
    ,aDest.IVA_NON_VERS_PER_PREC
    ,aDest.IVA_DEB_CRED_PER_PREC
    ,aDest.CRED_IVA_COMP_DETR
    ,aDest.IVA_DEB_CRED
    ,aDest.INT_DEB_LIQ_TRIM
    ,aDest.CRED_IVA_SPEC_DETR
    ,aDest.ACCONTO_IVA_VERS
    ,aDest.IVA_DA_VERSARE
    ,aDest.IVA_VERSATA
    ,aDest.CRED_IVA_INFRANN_RIMB
    ,aDest.CRED_IVA_INFRANN_COMP
    ,aDest.CD_TIPO_DOCUMENTO
    ,aDest.CD_CDS_DOC_AMM
    ,aDest.CD_UO_DOC_AMM
    ,aDest.ESERCIZIO_DOC_AMM
    ,aDest.PG_DOC_AMM
    ,aDest.IVA_CREDITO_NO_PRORATA
    ,aDest.PERC_PRORATA_DETRAIBILE
	,aA.ragione_sociale
	,'codice fiscale '||aA.codice_fiscale
	,'partita iva '||aA.partita_iva
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.ABI
from liquidazione_iva aDest, configurazione_cnr aC, anagrafico aA where
        aC.esercizio = 0
    and aC.cd_chiave_primaria = 'COSTANTI'
    and aC.cd_chiave_secondaria = 'CODICE_ANAG_ENTE'
    and aA.cd_anag = aC.im01;

   COMMENT ON TABLE "VP_STAMPA_LIQUID_IVA"  IS 'Vista di stampa della liquidazione IVA';
