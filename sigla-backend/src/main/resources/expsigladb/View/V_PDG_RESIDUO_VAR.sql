--------------------------------------------------------
--  DDL for View V_PDG_RESIDUO_VAR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PDG_RESIDUO_VAR" ("ESERCIZIO", "CD_DIPARTIMENTO", "DS_DIPARTIMENTO", "CD_PROGETTO", "DS_PROGETTO", "CD_COMMESSSA", "DS_COMMESSSA", "CD_MODULO", "DS_MODULO", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CENTRO_RESPONSABILITA", "DS_CDR", "CD_LINEA_ATTIVITA", "LA_DENOMINAZIONE", "CD_ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "COD_CLA_S", "CLA_S_DESCRIZIONE", "STATO_DETTAGLIO", "STATO_RESIDUO", "IM_MASSA_SPENDIBILE", "IMPORTO_INIZIALE", "IMPORTO_FINALE") AS 
  select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista di estrazione dei dati della ricostruzione dei residui per l'anno 2005
-- con traccia delle variazioni
-- utili alla stampa "Stampa Analitica delle Risorse provenienti dall'esercizio 2004"
--
-- History:
--
-- Date: 27/07/2005
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
distinct
 PDG_RESIDUO_DET.esercizio ,
 PROGETTO.cd_dipartimento,
 DIPARTIMENTO.ds_dipartimento,
 PROGETTO.cd_progetto cd_progetto,
 PROGETTO.ds_progetto ds_progetto,
 COM.cd_progetto cd_commesssa,
 COM.ds_progetto ds_commesssa,
 MODU.cd_progetto cd_modulo,
 MODU.ds_progetto ds_modulo,
 substr(CDR.cd_unita_organizzativa, 1,3) CDS,
 CDR.cd_unita_organizzativa,
 LINEA_ATTIVITA.cd_centro_responsabilita,
 CDR.ds_cdr,
 PDG_RESIDUO_DET.cd_linea_attivita,
 LINEA_ATTIVITA.denominazione,
 PDG_RESIDUO_DET.cd_elemento_voce,
 ELEMENTO_VOCE.ds_elemento_voce ,
 ELEMENTO_VOCE.cod_cla_s,
 CLASSIFICAZIONE_SPESE.descrizione,
 PDG_RESIDUO_DET.stato stato_dettaglio,
 PDG_RESIDUO.stato stato_residuo,
 PDG_RESIDUO.im_massa_spendibile,
 a.importo_iniziale,
 b.importo_finale
from    DIPARTIMENTO,
 LINEA_ATTIVITA,
 PROGETTO_GEST PROGETTO,
 PROGETTO_GEST COM,
 PROGETTO_GEST MODU,
 PDG_RESIDUO_DET,
 ELEMENTO_VOCE,
 CDR,
 PDG_RESIDUO,
 CLASSIFICAZIONE_SPESE,
 (select sum(im_residuo) importo_iniziale,CD_cdr_linea,CD_LINEA_ATTIVITA,CD_ELEMENTO_VOCE
  from PDG_RESIDUO_DET_S
  where pg_storico_ = (select Min(dett.pg_storico_)
         from PDG_RESIDUO_DET_S dett
         Where dett.CD_CENTRO_RESPONSABILITA = PDG_RESIDUO_DET_S.CD_CENTRO_RESPONSABILITA
           And dett.CD_LINEA_ATTIVITA = PDG_RESIDUO_DET_S.CD_LINEA_ATTIVITA
           And dett.CD_ELEMENTO_VOCE = PDG_RESIDUO_DET_S.CD_ELEMENTO_VOCE)
         group by CD_cdr_linea,CD_LINEA_ATTIVITA,CD_ELEMENTO_VOCE) a,
 (select sum(im_residuo) importo_finale,CD_cdr_linea,CD_LINEA_ATTIVITA,CD_ELEMENTO_VOCE
  from PDG_RESIDUO_DET_S
  where pg_storico_ = (select Max(dett.pg_storico_)
         from PDG_RESIDUO_DET_S dett
         Where dett.CD_CENTRO_RESPONSABILITA = PDG_RESIDUO_DET_S.CD_CENTRO_RESPONSABILITA
           And dett.CD_LINEA_ATTIVITA = PDG_RESIDUO_DET_S.CD_LINEA_ATTIVITA
           And dett.CD_ELEMENTO_VOCE = PDG_RESIDUO_DET_S.CD_ELEMENTO_VOCE)
        group by CD_cdr_linea,CD_LINEA_ATTIVITA,CD_ELEMENTO_VOCE) b
where PROGETTO.cd_dipartimento      = DIPARTIMENTO.cd_dipartimento
  and   LINEA_ATTIVITA.PG_PROGETTO     = MODU.PG_PROGETTO
  And   MODU.ESERCIZIO                 = PDG_RESIDUO_DET.esercizio
  And   MODU.ESERCIZIO_PROGETTO_PADRE  = COM.ESERCIZIO
  And   MODU.PG_PROGETTO_PADRE         = COM.PG_PROGETTO
  And   COM.ESERCIZIO_PROGETTO_PADRE   = PROGETTO.ESERCIZIO
  And   COM.PG_PROGETTO_PADRE          = PROGETTO.PG_PROGETTO
  and   PDG_RESIDUO_DET.cd_linea_attivita      = LINEA_ATTIVITA.cd_linea_attivita
  and   PDG_RESIDUO_DET.cd_elemento_voce      = ELEMENTO_VOCE.cd_elemento_voce
  and   PDG_RESIDUO_DET.esercizio     = ELEMENTO_VOCE.esercizio
  and   PDG_RESIDUO_DET.cd_cdr_linea       = LINEA_ATTIVITA.cd_centro_responsabilita
  and   LINEA_ATTIVITA.cd_centro_responsabilita       = CDR.cd_centro_responsabilita
  and  a.CD_cdr_linea     = LINEA_ATTIVITA.cd_centro_responsabilita
  and  a.CD_LINEA_ATTIVITA       = PDG_RESIDUO_DET.cd_linea_attivita
  and  a.CD_ELEMENTO_VOCE        = PDG_RESIDUO_DET.cd_elemento_voce
  and  b.CD_cdr_linea      = LINEA_ATTIVITA.cd_centro_responsabilita
  and  b.CD_LINEA_ATTIVITA              = PDG_RESIDUO_DET.cd_linea_attivita
  and  b.CD_ELEMENTO_VOCE       = PDG_RESIDUO_DET.cd_elemento_voce
  and   PDG_RESIDUO.esercizio              = PDG_RESIDUO_DET.esercizio
  and   PDG_RESIDUO.cd_centro_responsabilita     = PDG_RESIDUO_DET.cd_centro_responsabilita
  and   ELEMENTO_VOCE.esercizio_cla_s(+)      = CLASSIFICAZIONE_SPESE.esercizio
  and   ELEMENTO_VOCE.cod_cla_s(+)                 = CLASSIFICAZIONE_SPESE.codice_cla_s
order  by PROGETTO.cd_dipartimento,PROGETTO.cd_progetto ,COM.cd_progetto ,MODU.cd_progetto;
