--------------------------------------------------------
--  DDL for View V_PROG_COM_MOD
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_PROG_COM_MOD" ("ESERCIZIO", "CD_PROGETTO", "DS_PROGETTO", "CD_COMMESSA", "DS_COMMESSA", "CD_MODULO", "DS_MODULO", "CD_LINEA_ATTIVITA", "DS_LINEA_ATTIVITA", "DENOMINAZIONE_LA", "CDR", "CD_NATURA", "DS_NATURA", "PG_PROGETTO", "TI_GESTIONE", "FLG_PDG") AS
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
-- Date: 19/09/2005
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunta la selezione del progetto/commessa/modulo per anno
--
-- Body:
--
        V_LINEA_ATTIVITA_VALIDA.esercizio,
        PROGETTO.cd_progetto cd_progetto,
        PROGETTO.ds_progetto ds_progetto,
        COM.cd_progetto cd_commessa,
        COM.ds_progetto ds_commessa,
        MODU.cd_progetto cd_modulo,
        MODU.ds_progetto ds_modulo,
        V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita,
        V_LINEA_ATTIVITA_VALIDA.ds_linea_attivita,
        V_LINEA_ATTIVITA_VALIDA.denominazione,
        V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita,
        V_LINEA_ATTIVITA_VALIDA.cd_natura,
        NATURA.ds_natura,
        V_LINEA_ATTIVITA_VALIDA.pg_progetto,
        V_LINEA_ATTIVITA_VALIDA.ti_gestione,
        'Y'
From
 V_LINEA_ATTIVITA_VALIDA,
 PROGETTO,
 PROGETTO COM,
 PROGETTO MODU,
 NATURA,
 parametri_cnr
Where
	V_LINEA_ATTIVITA_VALIDA.ESERCIZIO = parametri_cnr.esercizio and
	parametri_cnr.fl_nuovo_pdg ='N' and
		V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO = MODU.PG_PROGETTO
  And V_LINEA_ATTIVITA_VALIDA.ESERCIZIO = MODU.ESERCIZIO
  And MODU.TIPO_FASE = 'X'
  And MODU.ESERCIZIO_PROGETTO_PADRE = COM.ESERCIZIO
  And MODU.PG_PROGETTO_PADRE        = COM.PG_PROGETTO
  And MODU.TIPO_FASE_PROGETTO_PADRE = COM.TIPO_FASE
  And COM.ESERCIZIO_PROGETTO_PADRE  = PROGETTO.ESERCIZIO
  And COM.PG_PROGETTO_PADRE         = PROGETTO.PG_PROGETTO
  And COM.TIPO_FASE_PROGETTO_PADRE  = PROGETTO.TIPO_FASE
  And V_LINEA_ATTIVITA_VALIDA.cd_natura   = NATURA.cd_natura
  And (Exists (Select 1 From pdg_preventivo_spe_det
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
       Or
       Exists (Select 1 from pdg_preventivo_etr_det
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
        Or
       Exists (Select 1 from pdg_modulo_spese_gest
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
        Or
       Exists (Select 1 from pdg_modulo_entrate_gest
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
        )
Union
Select 
        V_LINEA_ATTIVITA_VALIDA.esercizio,
        PROGETTO.cd_progetto cd_progetto,
        PROGETTO.ds_progetto ds_progetto,
        COM.cd_progetto cd_commessa,
        COM.ds_progetto ds_commessa,
        MODU.cd_progetto cd_modulo,
        MODU.ds_progetto ds_modulo,
        V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita,
        V_LINEA_ATTIVITA_VALIDA.ds_linea_attivita,
        V_LINEA_ATTIVITA_VALIDA.denominazione,
        V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita,
        V_LINEA_ATTIVITA_VALIDA.cd_natura,
        NATURA.ds_natura,
        V_LINEA_ATTIVITA_VALIDA.pg_progetto,
        V_LINEA_ATTIVITA_VALIDA.ti_gestione,
        'N'
From
 V_LINEA_ATTIVITA_VALIDA,
 PROGETTO,
 PROGETTO COM,
 PROGETTO MODU,
 NATURA,
 parametri_cnr
Where
	V_LINEA_ATTIVITA_VALIDA.ESERCIZIO = parametri_cnr.esercizio and
	parametri_cnr.fl_nuovo_pdg ='N' and
      V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO = MODU.PG_PROGETTO
  And V_LINEA_ATTIVITA_VALIDA.ESERCIZIO = MODU.ESERCIZIO
  And MODU.TIPO_FASE = 'X'
  And MODU.ESERCIZIO_PROGETTO_PADRE = COM.ESERCIZIO
  And MODU.PG_PROGETTO_PADRE        = COM.PG_PROGETTO
  And MODU.TIPO_FASE_PROGETTO_PADRE = COM.TIPO_FASE
  And COM.ESERCIZIO_PROGETTO_PADRE  = PROGETTO.ESERCIZIO
  And COM.PG_PROGETTO_PADRE         = PROGETTO.PG_PROGETTO
  And COM.TIPO_FASE_PROGETTO_PADRE  = PROGETTO.TIPO_FASE
  And V_LINEA_ATTIVITA_VALIDA.cd_natura   = NATURA.cd_natura
  And (Not Exists (Select 1 From pdg_preventivo_spe_det
                   Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
                   And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
                   And cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
       And
       Not Exists (Select 1 From pdg_preventivo_etr_det
                   Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
                   And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
                   And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
			and
       not Exists (Select 1 from pdg_modulo_spese_gest
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
       and
       not Exists (Select 1 from pdg_modulo_entrate_gest
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
        )
  union
  Select 
        V_LINEA_ATTIVITA_VALIDA.esercizio,
        PROGETTO.cd_progetto cd_progetto,
        PROGETTO.ds_progetto ds_progetto,
        COM.cd_progetto cd_commessa,
        COM.ds_progetto ds_commessa,
        null cd_modulo,
        null ds_modulo,
        V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita,
        V_LINEA_ATTIVITA_VALIDA.ds_linea_attivita,
        V_LINEA_ATTIVITA_VALIDA.denominazione,
        V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita,
        V_LINEA_ATTIVITA_VALIDA.cd_natura,
        NATURA.ds_natura,
        V_LINEA_ATTIVITA_VALIDA.pg_progetto,
        V_LINEA_ATTIVITA_VALIDA.ti_gestione,
        'Y'
From
 V_LINEA_ATTIVITA_VALIDA,
 PROGETTO COM,
 PROGETTO ,
 NATURA ,
 parametri_cnr
Where
	V_LINEA_ATTIVITA_VALIDA.ESERCIZIO = parametri_cnr.esercizio and
	parametri_cnr.fl_nuovo_pdg ='Y' and
	 V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO = COM.pg_progetto
  And V_LINEA_ATTIVITA_VALIDA.ESERCIZIO = COM.esercizio
  And COM.TIPO_FASE = 'X'
  And COM.ESERCIZIO_PROGETTO_PADRE  = PROGETTO.ESERCIZIO
  And COM.PG_PROGETTO_PADRE         = PROGETTO.PG_PROGETTO
  And COM.TIPO_FASE_PROGETTO_PADRE  = PROGETTO.TIPO_FASE
  And V_LINEA_ATTIVITA_VALIDA.cd_natura   = NATURA.cd_natura
  And (Exists (Select 1 From pdg_preventivo_spe_det
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
       Or
       Exists (Select 1 from pdg_preventivo_etr_det
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
          Or
       Exists (Select 1 from pdg_modulo_spese_gest
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
        Or
       Exists (Select 1 from pdg_modulo_entrate_gest
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
        )
Union
Select 
        V_LINEA_ATTIVITA_VALIDA.esercizio,
        PROGETTO.cd_progetto cd_progetto,
        PROGETTO.ds_progetto ds_progetto,
        COM.cd_progetto cd_commessa,
        COM.ds_progetto ds_commessa,
      	null cd_modulo,
        null ds_modulo,
        V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita,
        V_LINEA_ATTIVITA_VALIDA.ds_linea_attivita,
        V_LINEA_ATTIVITA_VALIDA.denominazione,
        V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita,
        V_LINEA_ATTIVITA_VALIDA.cd_natura,
        NATURA.ds_natura,
        V_LINEA_ATTIVITA_VALIDA.pg_progetto,
        V_LINEA_ATTIVITA_VALIDA.ti_gestione,
        'N'
From
 V_LINEA_ATTIVITA_VALIDA,
 PROGETTO,
 PROGETTO COM,
 NATURA ,
 parametri_cnr
Where
	V_LINEA_ATTIVITA_VALIDA.ESERCIZIO = parametri_cnr.esercizio and
	parametri_cnr.fl_nuovo_pdg ='Y' and
	 V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO = COM.pg_progetto
  And V_LINEA_ATTIVITA_VALIDA.ESERCIZIO = COM.esercizio
  And COM.TIPO_FASE = 'X'
  And COM.ESERCIZIO_PROGETTO_PADRE  = PROGETTO.ESERCIZIO
  And COM.PG_PROGETTO_PADRE         = PROGETTO.PG_PROGETTO
  And COM.TIPO_FASE_PROGETTO_PADRE  = PROGETTO.TIPO_FASE
  And V_LINEA_ATTIVITA_VALIDA.cd_natura   = NATURA.cd_natura
  And (Not Exists (Select 1 From pdg_preventivo_spe_det
                   Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
                   And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
                   And cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
       And
       Not Exists (Select 1 From pdg_preventivo_etr_det
                   Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
                   And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
                   And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
         and
       not Exists (Select 1 from pdg_modulo_spese_gest
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
       and
       not Exists (Select 1 from pdg_modulo_entrate_gest
               Where esercizio = V_LINEA_ATTIVITA_VALIDA.esercizio
               And   cd_centro_responsabilita = V_LINEA_ATTIVITA_VALIDA.cd_centro_responsabilita
               And   cd_linea_attivita = V_LINEA_ATTIVITA_VALIDA.cd_linea_attivita)
        );
