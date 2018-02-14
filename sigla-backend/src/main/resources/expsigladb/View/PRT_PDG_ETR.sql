--------------------------------------------------------
--  DDL for View PRT_PDG_ETR
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_PDG_ETR" ("ESERCIZIO", "PESO_DIPARTIMENTO", "CD_DIPARTIMENTO", "DS_DIPARTIMENTO", "CD_PROGETTO", "DS_PROGETTO", "CD_COMMESSA", "DS_COMMESSA", "CD_MODULO", "DS_MODULO", "TIPO_PROGETTO", "DS_TIPO_PROGETTO", "CDS", "DS_CDS", "UO", "TIPO_UO", "DS_UO", "CDR", "CD_LINEA_ATTIVITA", "TITOLO", "DS_TITOLO", "CODICE_CLAS_ENTRATA", "DS_CLASSIFICAZIONE_ENTRATA", "ELEMENTO_VOCE", "DS_ELEMENTO_VOCE", "ORIGINE", "CATEGORIA_DETTAGLIO", "NATURA", "DS_NATURA", "PG_ENTRATA", "FL_RIBALTATO", "IM_ENTRATE_RICAVI", "IM_ENTRATE_SENZA_RICAVI") AS 
  Select
--
-- Date: 26/04/2005
-- Version: 1.0
--
-- Vista per la stampa del PDG per Dipartimento/Istituto parte entrata
--
-- History:
--
-- Date: 26/04/2005
-- Version: 1.0
-- Creazione
--
-- Body:
--
Esercizio,
Peso_Dipartimento,
Cd_Dipartimento,
Ds_Dipartimento,
Cd_Progetto,
Ds_Progetto,
Cd_Commessa,
Ds_Commessa,
Cd_Modulo,
Ds_Modulo,
Tipo_Progetto,
Ds_Tipo_Progetto,
Cds,
Ds_Cds,
Uo,
Tipo_Uo,
Ds_Uo,
Cdr,
Cd_Linea_Attivita,
Titolo,
'Titolo'||' '||TITOLO||' '||'-'||' '||DS_TITOLO,
TITOLO||'.'||CODICE_CLAS_Entrata,
Ds_Classificazione_Entrata,
Elemento_Voce,
Ds_Elemento_Voce,
Origine,
Categoria_Dettaglio,
Natura,
Ds_Natura,
Pg_Entrata,
Fl_Ribaltato,
Im_Entrate_Ricavi,
Im_Entrate_Senza_Ricavi
From V_Pdg_Etr
;

   COMMENT ON TABLE "PRT_PDG_ETR"  IS 'Vista per la stampa del PDG per Dipartimento/Istituto parte entrata';
