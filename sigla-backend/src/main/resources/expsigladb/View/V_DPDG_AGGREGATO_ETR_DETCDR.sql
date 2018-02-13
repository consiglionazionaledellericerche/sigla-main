--------------------------------------------------------
--  DDL for View V_DPDG_AGGREGATO_ETR_DETCDR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_DPDG_AGGREGATO_ETR_DETCDR" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "CD_NATURA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "TI_AGGREGATO", "IM_RA_RCE", "IM_RB_RSE", "IM_RC_ESR", "IM_RD_A2_RICAVI", "IM_RE_A2_ENTRATE", "IM_RF_A3_RICAVI", "IM_RG_A3_ENTRATE", "DACR", "UTCR", "DUVA", "UTUV", "PG_VER_REC") AS 
  (select   /*+ use_hash(r,cr) */
--
-- Date: 09/07/2002
-- Version: 1.6
--
-- Vista di aggregazione entrate del PDG per natura/capitolo dato il CDR.
-- La vista non si preoccupa di verificare la validita STO
--
-- History:
--
-- Date: 15/09/2001
-- Version: 1.0
-- Creazione
--
-- Date: 02/10/2001
-- Version: 1.1
-- Fix errori
--
-- Date: 10/10/2001
-- Version: 1.2
-- Ottimizzazione performance attraverso hints
--
-- Date: 22/10/2001
-- Version: 1.3
-- Aggiunta di ti_aggregato = 'D' per compatibilita con PDG_AGGREGATO_ETR_DET
--
-- Date: 30/10/2001
-- Version: 1.4
-- Fix errore su filtraggio ricavi figurativi
--
-- Date: 08/11/2001
-- Version: 1.5
-- Eliminazione esercizio da STO
--
-- Date: 09/07/2002
-- Version: 1.6
-- Eliminata join con linea di attività (la natura è estratta dal dettaglio)
-- Filtrati i dettagli di natura 5 in entrata che non devono entrare a far parte della contrattazione
-- Tali dettagli esistono solo sulle aree per ribaltamento dei costi sulle aree stesse in fase di chiusura
-- in F dei pdg afferenti all'area
--
-- Date: 28/07/2005
-- Version: 1.7
-- Inseriti nel calcolo solo le Variazioni ai Piani di Gestione con  stato Approvato (APP)
--
-- Body:
--
R.ESERCIZIO
,CR.CD_CENTRO_RESPONSABILITA
,R.CD_NATURA
,R.TI_APPARTENENZA
,R.TI_GESTIONE
,R.CD_ELEMENTO_VOCE
,'D'
,sum(IM_RA_RCE)
,sum(IM_RB_RSE)
,sum(IM_RC_ESR)
,sum(IM_RD_A2_RICAVI)
,sum(IM_RE_A2_ENTRATE)
,sum(IM_RF_A3_RICAVI)
,sum(IM_RG_A3_ENTRATE)
,NULL
,NULL
,NULL
,NULL
,0
from
CDR CR, -- CDR collegato ad R
PDG_PREVENTIVO_ETR_DET R, -- PDG
PDG_VARIAZIONE V, -- PDG variazione per CDR CR
CDR CDR_C -- CDR PDG servito (costo senza spese)
where
    R.cd_centro_responsabilita=CR.cd_centro_responsabilita
and R.esercizio_pdg_variazione = V.esercizio (+)
and R.pg_variazione_pdg = V.pg_variazione_pdg (+)
and (
     V.stato Is Null Or V.stato = 'APP'
)
and R.cd_natura <> '5'
and (
(
R.cd_centro_responsabilita_clgs is not null -- Dettaglio di carico con indicazione del centro servito
and R.categoria_dettaglio = 'CAR' -- Epurazione dei ricavi figurativi
and CDR_C.cd_centro_responsabilita = R.cd_centro_responsabilita_clgs
and R.stato = 'Y'
and
not (
CR.livello = 2 and CDR_C.livello = 1 and CR.cd_cdr_afferenza = CDR_C.cd_centro_responsabilita
or CR.livello = 1 and CDR_C.livello = 2 and CR.cd_centro_responsabilita = CDR_C.cd_cdr_afferenza
or CR.livello = 2 and CDR_C.livello = 2 and CR.cd_cdr_afferenza = CDR_C.cd_cdr_afferenza
)
)
or
(
R.categoria_dettaglio = 'SIN'
and CDR_C.cd_centro_responsabilita = R.cd_centro_responsabilita -- server per non moltiplicare le righe per CDR_C
)
)
group by
 R.ESERCIZIO
,CR.CD_CENTRO_RESPONSABILITA
,R.CD_NATURA
,R.TI_APPARTENENZA
,R.TI_GESTIONE
,R.CD_ELEMENTO_VOCE
);

   COMMENT ON TABLE "V_DPDG_AGGREGATO_ETR_DETCDR"  IS 'Vista di aggregazione entrate del PDG per natura/capitolo dato il CDR
La vista non si preoccupa di verificare la validità STO
La vista non include le righe provenienti da Variazioni ai PDG con stato diverso da ''Approvato''';
