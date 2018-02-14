--------------------------------------------------------
--  DDL for View VP_DPDG_LA_CDR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_DPDG_LA_CDR" ("ESERCIZIO", "CD_CDR_ROOT", "DS_CDR_ROOT", "STATO_PDG_ROOT", "ANNOTAZIONI_PDG_ROOT", "CD_CDS", "DS_CDS", "CD_TIPO_UNITA", "CD_RESPONSABILE_CDS", "CD_RESPONSABILE_AMM_CDS", "CD_UNITA_ORGANIZZATIVA", "DS_UNITA_ORGANIZZATIVA", "FL_UO_CDS", "FL_RUBRICA", "FL_PRESIDENTE_AREA", "CD_AREA_RICERCA", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_PROPRIO_CDR", "CD_RESPONSABILE_CDR", "INDIRIZZO", "CD_CDR_AFFERENZA", "STATO_PIANO", "ANNOTAZIONI", "FL_RIBALTATO_SU_AREA", "CD_LINEA_ATTIVITA", "CD_TIPO_LINEA_ATTIVITA", "DENOMINAZIONE", "CD_GRUPPO_LINEA_ATTIVITA", "CD_FUNZIONE", "CD_NATURA", "DS_LINEA_ATTIVITA", "CD_CDR_COLLEGATO", "CD_LA_COLLEGATO", "PG_RISULTATO", "CD_TIPO_RISULTATO", "DS_RISULTATO", "QUANTITA", "DS_FUNZIONE", "DS_NATURA", "RICAVI_A1", "RICAVI_A2", "RICAVI_A3", "ENTRATE_A1", "ENTRATE_A2", "ENTRATE_A3", "COSTI_A1", "COSTI_A2", "COSTI_A3", "SPESE_A1", "SPESE_A2", "SPESE_A3") AS 
  select distinct
--
-- Date: 19/12/2002
-- Version: 1.3
--
-- Vista di stampa dei dettagli CDR aggregati per cdr di primo livello (o resp. di area) elinea di attivita
--
-- History:
--
-- Date: 14/11/2001
-- Version: 1.0
-- Creazione
--
-- Date: 18/11/2001
-- Version: 1.1
-- Eliminazione esercizio da STO
--
-- Date: 16/11/2002
-- Version: 1.2
-- Risitemazione estrazione dei risultati da join diretta con il risultato
--
-- Date: 19/12/2002
-- Version: 1.3
-- Fix errore su moltiplicazione totali per numero di risultati (n. 413)
-- Introduzione di riga con importi (e pg_risultato a null) e righe senza importi e risultati
--
-- Body:
--
a.ESERCIZIO,
b.CD_CENTRO_RESPONSABILITA,
b.DS_CDR,
c.STATO,
c.ANNOTAZIONI,
a.CD_CDS,
a.DS_CDS,
a.CD_TIPO_UNITA,
a.CD_RESPONSABILE_CDS,
a.CD_RESPONSABILE_AMM_CDS,
a.CD_UNITA_ORGANIZZATIVA,
a.DS_UNITA_ORGANIZZATIVA,
a.FL_UO_CDS,
a.FL_RUBRICA,
a.FL_PRESIDENTE_AREA,
a.CD_AREA_RICERCA,
a.CD_CENTRO_RESPONSABILITA,
a.DS_CDR,
a.CD_PROPRIO_CDR,
a.CD_RESPONSABILE_CDR,
a.INDIRIZZO,
a.CD_CDR_AFFERENZA,
a.STATO_PIANO,
a.ANNOTAZIONI,
a.FL_RIBALTATO_SU_AREA,
a.CD_LINEA_ATTIVITA,
a.CD_TIPO_LINEA_ATTIVITA,
a.DENOMINAZIONE,
a.CD_GRUPPO_LINEA_ATTIVITA,
a.CD_FUNZIONE,
a.CD_NATURA,
a.DS_LINEA_ATTIVITA,
a.CD_CDR_COLLEGATO,
a.CD_LA_COLLEGATO,
h.PG_RISULTATO,
h.CD_TIPO_RISULTATO,
h.DS_RISULTATO,
h.QUANTITA,
a.DS_FUNZIONE,
a.DS_NATURA,
0,0,0,0,0,0,0,0,0,0,0,0
from
 vp_dpdg_cdr a,
 risultato h,
 cdr b,
 pdg_preventivo c
where
 (
         a.cd_cdr_afferenza = b.cd_centro_responsabilita
      or (a.cd_centro_responsabilita = b.cd_centro_responsabilita)
	 )
 and
  (
      b.livello = 1
   or a.cd_tipo_unita = 'AREA'
 )
 and c.esercizio = a.esercizio
 and c.cd_centro_responsabilita = b.cd_centro_responsabilita
 and h.cd_centro_responsabilita = a.cd_centro_responsabilita
 and h.cd_linea_attivita = a.cd_linea_attivita
union all
select
a.ESERCIZIO,
b.CD_CENTRO_RESPONSABILITA,
b.DS_CDR,
c.STATO,
c.ANNOTAZIONI,
a.CD_CDS,
a.DS_CDS,
a.CD_TIPO_UNITA,
a.CD_RESPONSABILE_CDS,
a.CD_RESPONSABILE_AMM_CDS,
a.CD_UNITA_ORGANIZZATIVA,
a.DS_UNITA_ORGANIZZATIVA,
a.FL_UO_CDS,
a.FL_RUBRICA,
a.FL_PRESIDENTE_AREA,
a.CD_AREA_RICERCA,
a.CD_CENTRO_RESPONSABILITA,
a.DS_CDR,
a.CD_PROPRIO_CDR,
a.CD_RESPONSABILE_CDR,
a.INDIRIZZO,
a.CD_CDR_AFFERENZA,
a.STATO_PIANO,
a.ANNOTAZIONI,
a.FL_RIBALTATO_SU_AREA,
a.CD_LINEA_ATTIVITA,
a.CD_TIPO_LINEA_ATTIVITA,
a.DENOMINAZIONE,
a.CD_GRUPPO_LINEA_ATTIVITA,
a.CD_FUNZIONE,
a.CD_NATURA,
a.DS_LINEA_ATTIVITA,
a.CD_CDR_COLLEGATO,
a.CD_LA_COLLEGATO,
to_number(null),
null,
null,
to_number(null),
a.DS_FUNZIONE,
a.DS_NATURA,
sum(a.IM_RA_RCE+a.IM_RB_RSE),
sum(a.IM_RD_A2_RICAVI),
sum(a.IM_RF_A3_RICAVI),
sum(a.IM_RA_RCE+a.IM_RC_ESR),
sum(a.IM_RE_A2_ENTRATE),
sum(a.IM_RG_A3_ENTRATE),
sum(a.IM_RH_CCS_COSTI+a.IM_RM_CSS_AMMORTAMENTI+a.IM_RN_CSS_RIMANENZE+a.IM_RO_CSS_ALTRI_COSTI+a.IM_RP_CSS_VERSO_ALTRO_CDR),
sum(a.IM_RAA_A2_COSTI_FINALI+a.IM_RAB_A2_COSTI_ALTRO_CDR),
sum(a.IM_RAH_A3_COSTI_FINALI+a.IM_RAI_A3_COSTI_ALTRO_CDR),
sum(a.IM_RI_CCS_SPESE_ODC+a.IM_RJ_CCS_SPESE_ODC_ALTRA_UO+a.IM_RK_CCS_SPESE_OGC+a.IM_RL_CCS_SPESE_OGC_ALTRA_UO+a.IM_RQ_SSC_COSTI_ODC+a.IM_RR_SSC_COSTI_ODC_ALTRA_UO+a.IM_RS_SSC_COSTI_OGC+a.IM_RT_SSC_COSTI_OGC_ALTRA_UO),
sum(a.IM_RAC_A2_SPESE_ODC+a.IM_RAD_A2_SPESE_ODC_ALTRA_UO+a.IM_RAE_A2_SPESE_OGC+a.IM_RAF_A2_SPESE_OGC_ALTRA_UO),
sum(a.IM_RAL_A3_SPESE_ODC+a.IM_RAM_A3_SPESE_ODC_ALTRA_UO+a.IM_RAN_A3_SPESE_OGC+a.IM_RAO_A3_SPESE_OGC_ALTRA_UO)
from
 vp_dpdg_cdr a,
 cdr b,
 pdg_preventivo c
where
 (
         a.cd_cdr_afferenza = b.cd_centro_responsabilita
      or (a.cd_centro_responsabilita = b.cd_centro_responsabilita)
	 )
 and
  (
      b.livello = 1
   or a.cd_tipo_unita = 'AREA'
 )
 and c.esercizio = a.esercizio
 and c.cd_centro_responsabilita = b.cd_centro_responsabilita
group by
a.ESERCIZIO,
b.CD_CENTRO_RESPONSABILITA,
b.DS_CDR,
c.STATO,
c.ANNOTAZIONI,
a.CD_CDS,
a.DS_CDS,
a.CD_TIPO_UNITA,
a.CD_RESPONSABILE_CDS,
a.CD_RESPONSABILE_AMM_CDS,
a.CD_UNITA_ORGANIZZATIVA,
a.DS_UNITA_ORGANIZZATIVA,
a.FL_UO_CDS,
a.FL_RUBRICA,
a.FL_PRESIDENTE_AREA,
a.CD_AREA_RICERCA,
a.CD_CENTRO_RESPONSABILITA,
a.DS_CDR,
a.CD_PROPRIO_CDR,
a.CD_RESPONSABILE_CDR,
a.INDIRIZZO,
a.CD_CDR_AFFERENZA,
a.STATO_PIANO,
a.ANNOTAZIONI,
a.FL_RIBALTATO_SU_AREA,
a.CD_LINEA_ATTIVITA,
a.CD_TIPO_LINEA_ATTIVITA,
a.DENOMINAZIONE,
a.CD_GRUPPO_LINEA_ATTIVITA,
a.CD_FUNZIONE,
a.CD_NATURA,
a.DS_LINEA_ATTIVITA,
a.CD_CDR_COLLEGATO,
a.CD_LA_COLLEGATO,
a.DS_FUNZIONE,
a.DS_NATURA
;

   COMMENT ON TABLE "VP_DPDG_LA_CDR"  IS 'Vista di stampa dei dettagli CDR aggregati per cdr di primo livello (o resp. di area) elinea di attività';
