--------------------------------------------------------
--  DDL for View V_ASS_INCARICO_ATTIVITA_REPLIM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ASS_INCARICO_ATTIVITA_REPLIM" ("ESERCIZIO", "ESERCIZIO_LIMITE", "CD_TIPO_INCARICO", "DS_TIPO_INCARICO", "CD_TIPO_ATTIVITA", "DS_TIPO_ATTIVITA", "TIPO_NATURA", "CD_TIPO_LIMITE", "IMPORTO_LIMITE", "IMPORTO_UTILIZZATO", "IMPORTO_UTILIZZATO_CALCOLATO", "FL_RAGGIUNTO_LIMITE") AS 
  ( 
Select ASS_INCARICO_ATTIVITA.esercizio, 
       V_REPERTORIO_LIMITI.esercizio_limite, 
       ASS_INCARICO_ATTIVITA.cd_tipo_incarico, 
       TIPO_INCARICO.ds_tipo_incarico, 
       ASS_INCARICO_ATTIVITA.cd_tipo_attivita, 
       TIPO_ATTIVITA.ds_tipo_attivita, 
       ASS_INCARICO_ATTIVITA.tipo_natura, 
       V_REPERTORIO_LIMITI.cd_tipo_limite, 
       V_REPERTORIO_LIMITI.importo_limite, 
       V_REPERTORIO_LIMITI.importo_utilizzato, 
       V_REPERTORIO_LIMITI.importo_utilizzato_calcolato, 
       V_REPERTORIO_LIMITI.fl_raggiunto_limite 
From   ASS_INCARICO_ATTIVITA, V_REPERTORIO_LIMITI, TIPO_ATTIVITA, TIPO_INCARICO 
Where  ASS_INCARICO_ATTIVITA.cd_tipo_limite = V_REPERTORIO_LIMITI.cd_tipo_limite 
And    ASS_INCARICO_ATTIVITA.cd_tipo_attivita = TIPO_ATTIVITA.cd_tipo_attivita 
And    ASS_INCARICO_ATTIVITA.cd_tipo_incarico = TIPO_INCARICO.cd_tipo_incarico )
;
