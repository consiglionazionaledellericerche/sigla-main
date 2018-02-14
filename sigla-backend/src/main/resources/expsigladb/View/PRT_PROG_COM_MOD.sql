--------------------------------------------------------
--  DDL for View PRT_PROG_COM_MOD
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PRT_PROG_COM_MOD" ("ESERCIZIO", "CD_PROGETTO", "DS_PROGETTO", "CD_COMMESSA", "DS_COMMESSA", "CD_MODULO", "DS_MODULO", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "DENOMINAZIONE_LA", "CDR", "CD_NATURA", "DS_NATURA", "PG_PROGETTO", "TI_GESTIONE", "FLG_PDG", "FLG_IMPEGNO") AS 
  Select
--
-- Date: 09/11/2006
-- Version: 1.1
--
-- Vista utile alla stampa "Elenco Gruppi di Azione per Progetto/Commessa/Modulo"
--
-- History:
--
--
-- Date: 27/09/2005
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
Distinct obbligazione_scad_voce.esercizio,
         v_prog_com_mod.CD_PROGETTO, v_prog_com_mod.DS_PROGETTO, v_prog_com_mod.CD_COMMESSA,
         v_prog_com_mod.DS_COMMESSA, v_prog_com_mod.CD_MODULO, v_prog_com_mod.DS_MODULO,
         v_prog_com_mod.CD_LINEA_ATTIVITA, v_prog_com_mod.DS_LINEA_ATTIVITA, v_prog_com_mod.DENOMINAZIONE_LA,
         v_prog_com_mod.CDR, v_prog_com_mod.CD_NATURA, v_prog_com_mod.DS_NATURA,
         v_prog_com_mod.PG_PROGETTO, v_prog_com_mod.TI_GESTIONE, v_prog_com_mod.FLG_PDG,
         'Y'
 From obbligazione_scad_voce,v_prog_com_mod
 Where obbligazione_scad_voce.esercizio = v_prog_com_mod.esercizio
 And obbligazione_scad_voce.cd_centro_responsabilita = v_prog_com_mod.cdr
 And obbligazione_scad_voce.cd_linea_attivita = v_prog_com_mod.cd_linea_attivita
 And obbligazione_scad_voce.ti_gestione = v_prog_com_mod.ti_gestione
 And obbligazione_scad_voce.cd_cds = substr(v_prog_com_mod.cdr,1,3)
Union
Select Distinct v_prog_com_mod.esercizio,
       v_prog_com_mod.CD_PROGETTO, v_prog_com_mod.DS_PROGETTO, v_prog_com_mod.CD_COMMESSA,
       v_prog_com_mod.DS_COMMESSA, v_prog_com_mod.CD_MODULO, v_prog_com_mod.DS_MODULO,
       v_prog_com_mod.CD_LINEA_ATTIVITA, v_prog_com_mod.DS_LINEA_ATTIVITA, v_prog_com_mod.DENOMINAZIONE_LA,
       v_prog_com_mod.CDR, v_prog_com_mod.CD_NATURA, v_prog_com_mod.DS_NATURA,
       v_prog_com_mod.PG_PROGETTO, v_prog_com_mod.TI_GESTIONE, v_prog_com_mod.FLG_PDG,
       'N'
 From v_prog_com_mod
 Where Not Exists (Select 1 From obbligazione_scad_voce
                   Where obbligazione_scad_voce.esercizio = v_prog_com_mod.esercizio
                   And obbligazione_scad_voce.cd_centro_responsabilita = v_prog_com_mod.cdr
                   And obbligazione_scad_voce.cd_linea_attivita = v_prog_com_mod.cd_linea_attivita
                   And obbligazione_scad_voce.ti_gestione = v_prog_com_mod.ti_gestione
                   And obbligazione_scad_voce.cd_cds = substr(v_prog_com_mod.cdr,1,3))
Union
Select Distinct accertamento_scad_voce.esercizio,
       v_prog_com_mod.CD_PROGETTO, v_prog_com_mod.DS_PROGETTO, v_prog_com_mod.CD_COMMESSA,
       v_prog_com_mod.DS_COMMESSA, v_prog_com_mod.CD_MODULO, v_prog_com_mod.DS_MODULO,
       v_prog_com_mod.CD_LINEA_ATTIVITA, v_prog_com_mod.DS_LINEA_ATTIVITA, v_prog_com_mod.DENOMINAZIONE_LA,
       v_prog_com_mod.CDR, v_prog_com_mod.CD_NATURA, v_prog_com_mod.DS_NATURA,
       v_prog_com_mod.PG_PROGETTO, v_prog_com_mod.TI_GESTIONE, v_prog_com_mod.FLG_PDG,
       'Y'
 From accertamento_scad_voce,v_prog_com_mod
 Where accertamento_scad_voce.esercizio = v_prog_com_mod.esercizio
 And accertamento_scad_voce.cd_centro_responsabilita = v_prog_com_mod.cdr
 And accertamento_scad_voce.cd_linea_attivita = v_prog_com_mod.cd_linea_attivita
 And accertamento_scad_voce.cd_cds = substr(v_prog_com_mod.cdr,1,3)
Union
Select Distinct v_prog_com_mod.esercizio,
       v_prog_com_mod.CD_PROGETTO, v_prog_com_mod.DS_PROGETTO, v_prog_com_mod.CD_COMMESSA,
       v_prog_com_mod.DS_COMMESSA, v_prog_com_mod.CD_MODULO, v_prog_com_mod.DS_MODULO,
       v_prog_com_mod.CD_LINEA_ATTIVITA, v_prog_com_mod.DS_LINEA_ATTIVITA, v_prog_com_mod.DENOMINAZIONE_LA,
       v_prog_com_mod.CDR, v_prog_com_mod.CD_NATURA, v_prog_com_mod.DS_NATURA,
       v_prog_com_mod.PG_PROGETTO, v_prog_com_mod.TI_GESTIONE, v_prog_com_mod.FLG_PDG,
       'N'
 From v_prog_com_mod
 Where Not Exists (Select 1 from accertamento_scad_voce
                   Where accertamento_scad_voce.esercizio = v_prog_com_mod.esercizio
                   And accertamento_scad_voce.cd_centro_responsabilita = v_prog_com_mod.cdr
                   And accertamento_scad_voce.cd_linea_attivita = v_prog_com_mod.cd_linea_attivita
                   And accertamento_scad_voce.cd_cds = substr(v_prog_com_mod.cdr,1,3));
