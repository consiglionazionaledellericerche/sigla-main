--------------------------------------------------------
--  DDL for View VP_UTENTE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_UTENTE" ("CD_UTENTE", "DS_UTENTE", "FL_UTENTE_TEMPL", "COGNOME", "CD_GESTORE", "FL_PASSWORD_CHANGE", "CD_CDR", "DS_CDR", "CD_CDS_CONFIGURATORE", "DS_CDS_CONFIGURATORE", "DT_INIZIO_VALIDITA", "DT_FINE_VALIDITA", "TI_UTENTE", "NOME", "INDIRIZZO", "DUVA", "CD_UTENTE_TEMPL", "DT_ULTIMA_VAR_PASSWORD", "UTUV", "DACR", "UTCR") AS 
  select 
-- 
-- Date: 22/03/2002 
-- Version: 1.1
-- 
-- Vista di stampa degli utenti e informazioni correlate 
-- 
-- History: 
-- 
-- Date: 20/03/2002 
-- Version: 1.0 
-- Carezione 
-- 
-- Date: 22/03/2002 
-- Version: 1.1
-- Fix errore su estrazione utenti template
--
-- Body: 
-- 
     aDest.CD_UTENTE 
    ,aDest.DS_UTENTE 
    ,aDest.FL_UTENTE_TEMPL 
    ,aDest.COGNOME 
    ,aDest.CD_GESTORE 
    ,aDest.FL_PASSWORD_CHANGE 
    ,aDest.CD_CDR 
    ,aCDR.DS_CDR 
    ,aDest.CD_CDS_CONFIGURATORE 
    ,aCDSConf.DS_UNITA_ORGANIZZATIVA 
    ,aDest.DT_INIZIO_VALIDITA 
    ,aDest.DT_FINE_VALIDITA 
    ,aDest.TI_UTENTE 
    ,aDest.NOME 
    ,aDest.INDIRIZZO 
    ,aDest.DUVA 
    ,aDest.CD_UTENTE_TEMPL 
    ,aDest.DT_ULTIMA_VAR_PASSWORD 
    ,aDest.UTUV 
    ,aDest.DACR 
    ,aDest.UTCR 
from utente aDest, cdr aCDR, unita_organizzativa aCDSConf 
where 
     aCDR.CD_CENTRO_RESPONSABILITA (+)= aDest.CD_CDR 
 and ( 
            aDest.CD_CDS_CONFIGURATORE = '*' and aCDSConf.cd_tipo_unita = 'ENTE' and aCDSConf.fl_cds ='Y' -- serve ad estrarre un solo record da UO nel caso non valga la cond. di join
         or aDest.CD_CDS_CONFIGURATORE is null and aCDSConf.cd_tipo_unita = 'ENTE' and aCDSConf.fl_cds ='Y' -- serve ad estrarre un solo record da UO nel caso non valga la cond. di join
	     or aCDSConf.CD_UNITA_ORGANIZZATIVA = aDest.CD_CDS_CONFIGURATORE
	 );

   COMMENT ON TABLE "VP_UTENTE"  IS 'Vista di stampa degli utenti e informazioni correlate';
