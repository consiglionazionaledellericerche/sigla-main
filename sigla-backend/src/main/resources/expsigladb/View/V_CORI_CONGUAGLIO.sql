--------------------------------------------------------
--  DDL for View V_CORI_CONGUAGLIO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CORI_CONGUAGLIO" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "PG_COMPENSO", "CD_CONTRIBUTO_RITENUTA", "TI_ENTE_PERCIPIENTE", "DT_INI_VALIDITA", "MONTANTE", "IMPONIBILE_LORDO", "IMPONIBILE", "DEDUZIONE", "DEDUZIONE_FAMILY", "ALIQUOTA", "BASE_CALCOLO", "AMMONTARE_LORDO", "AMMONTARE", "CORI_SOSPESO", "CD_CLASSIFICAZIONE_CORI", "PG_CLASSIFICAZIONE_MONTANTI", "FL_SCRIVI_MONTANTI", "FL_CREDITO_PAREGGIO_DETRAZIONI") AS
  SELECT
--==================================================================================================
--
-- Date: 11/12/2003
-- Version: 1.5
--
-- Vista di estrazione dei record di CONTRIBUTO_RITENUTA che sono da recuperare per il conguaglio.
-- IRPEF a scaglioni e addizionali comunali, provinciali e regionali
--
-- History:
--
-- Date: 16/07/2002
-- Version: 1.0
--
-- Creazione vista
--
-- Date: 19/07/2002
-- Version: 1.1
--
-- Inserito filtro per recupero della sola IRPEF a scaglioni
--
-- Date: 28/08/2002
-- Version: 1.2
--
-- Inserito recupero anche delle addizionali relative al territorio (comune, provincia e regione)
--
-- Date: 27/01/2003
-- Version: 1.3
--
-- Inserito recupero degli attributi di imponibile lordo e deduzione per adeguamento alla finanziaria 2003
--
-- Date: 09/12/2003
-- Version: 1.4
--
-- Inserito recupero anche delle addizionali relative al territorio (comune, provincia e regione) da rateizzazione
--
-- Date: 11/12/2003
-- Version: 1.5
--
-- Utilizzo del FORCE per la creazione della vista che utilizza STP in PACK
--
-- Date: 03/03/2005
-- Version: 1.6
--
-- Aggiunto il campo per la Family Area
--
-- Date: 01/08/2006
-- Version: 1.7
--
-- Aggiunte le condizioni su 'getIsCoriPrevid' e 'getIsCoriInail' per la nuova "Gestione dei Cervelli"
--
-- Date: 10/05/2007
-- Version: 1.8
--
-- Aggiunte le condizioni su 'getIsAddTerritorioAcconto' per la nuova "Gestione degli acconti per le
-- Addizionali comunali"
--
-- Date: 25/05/2014
-- Version: 1.9
--
-- Adeguamenti relativi al Bonus DL 66/2014
-- Aggiunta la condizione su 'IsCoriCreditoIrpef' per la gestione del Credito Irpef
--
-- Body:
--
--==================================================================================================
       A.cd_cds,
       A.cd_unita_organizzativa,
       A.esercizio,
       A.pg_compenso,
       A.cd_contributo_ritenuta,
       A.ti_ente_percipiente,
       A.dt_ini_validita,
       A.montante,
       A.imponibile_lordo,
       A.imponibile,
       A.im_deduzione_irpef,
       A.im_deduzione_family,
       A.aliquota,
       A.base_calcolo,
       A.ammontare_lordo,
       A.ammontare,
       A.im_cori_sospeso,
       B.cd_classificazione_cori,
       B.pg_classificazione_montanti,
       B.fl_scrivi_montanti,
       a.fl_credito_pareggio_detrazioni
FROM   CONTRIBUTO_RITENUTA A,
       TIPO_CONTRIBUTO_RITENUTA B
WHERE  B.cd_contributo_ritenuta = A.cd_contributo_ritenuta AND
       B.dt_ini_validita = A.dt_ini_validita AND
       (
           (CNRCTB545.getIsIrpefScaglioni(B.cd_classificazione_cori,
                                          B.pg_classificazione_montanti,
                                          B.fl_scrivi_montanti) = 'Y')
        OR
           (CNRCTB545.getIsAddTerritorio(B.cd_classificazione_cori) = 'Y')
        OR
           (CNRCTB545.getIsAddTerritorioRecRate(B.cd_classificazione_cori) = 'Y')
        OR
           (CNRCTB545.getIsAddTerritorioAcconto(B.cd_classificazione_cori) = 'Y')
        OR
           (CNRCTB545.getIsCoriPrevid(B.cd_classificazione_cori) = 'Y')
        OR
           (CNRCTB545.getIsCoriInail(B.cd_classificazione_cori) = 'Y')
        OR
           (CNRCTB545.IsCoriCreditoIrpef(A.cd_contributo_ritenuta) = 'Y')
       );
