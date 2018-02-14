--------------------------------------------------------
--  DDL for View V_CONS_REGISTRO_ACCERTAMENTI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_REGISTRO_ACCERTAMENTI" ("ESERCIZIO", "CD_DIP", "DS_DIP", "CD_PROG", "DS_PROG", "CD_COMMESSA", "DS_COMMESSA", "CD_MODULO", "DS_MODULO", "CDS", "UO", "CDR", "GAE", "DS_GAE", "ELEMENTO_VOCE", "ESER_ORIGINE_RICOSTRUITA", "ESERCIZIO_ORIGINALE", "PG_ACCERTAMENTO", "DS_ACCERTAMENTO", "IM_RESIDUO_INIZIALE", "IM_VARIAZIONI", "IM_ACCERTATO", "IM_ASSOCIATO_DOC_AMM", "IM_ASSOCIATO_DOC_CONTABILE") AS 
  Select RIEPILOGO.ESERCIZIO,
       RIEPILOGO.CD_DIPARTIMENTO,
       RIEPILOGO.DS_DIPARTIMENTO,
       RIEPILOGO.CD_PROGETTO,
       RIEPILOGO.DS_PROGETTO,
       RIEPILOGO.CD_COMMESSA,
       RIEPILOGO.DS_COMMESSA,
       RIEPILOGO.CD_MODULO,
       RIEPILOGO.DS_MODULO,
       RIEPILOGO.CD_CDS_ORIGINE,
       RIEPILOGO.CD_UO_ORIGINE,
       RIEPILOGO.CD_CENTRO_RESPONSABILITA,
       RIEPILOGO.CD_LINEA_ATTIVITA,
       RIEPILOGO.DS_GAE,
       RIEPILOGO.CD_ELEMENTO_VOCE,
       RIEPILOGO.ESER_ORIGINE_RICOSTRUITA,
       RIEPILOGO.ESERCIZIO_ORIGINALE,
       RIEPILOGO.PG_ACCERTAMENTO,
       RIEPILOGO.DS_ACCERTAMENTO,
       Sum(RIEPILOGO.IM_VOCE) - Nvl((Select Sum(IM_MODIFICA)
                                     From   ACCERTAMENTO_MOD_VOCE OMV, ACCERTAMENTO_MODIFICA OM
                                     Where  OM.CD_CDS              = RIEPILOGO.CD_CDS And
                                            OM.ESERCIZIO           = RIEPILOGO.ESERCIZIO And
                                            OM.ESERCIZIO_ORIGINALE = RIEPILOGO.ESERCIZIO_ORIGINALE And
                                            OM.PG_ACCERTAMENTO     = RIEPILOGO.PG_ACCERTAMENTO And
                                            OMV.CD_CDS             = OM.CD_CDS And
                                            OMV.ESERCIZIO          = OM.ESERCIZIO And
                                            OMV.PG_MODIFICA        = OM.PG_MODIFICA And
                                            OMV.CD_CENTRO_RESPONSABILITA = RIEPILOGO.CD_CENTRO_RESPONSABILITA And
                                            OMV.CD_LINEA_ATTIVITA = RIEPILOGO.CD_LINEA_ATTIVITA And
                                            OM.PG_MODIFICA > 0), 0), -- IM_RESIDUO_INIZIALE
       Nvl((Select Sum(IM_MODIFICA)
            From   ACCERTAMENTO_MOD_VOCE OMV, ACCERTAMENTO_MODIFICA OM
            Where  OM.CD_CDS              = RIEPILOGO.CD_CDS And
                   OM.ESERCIZIO           = RIEPILOGO.ESERCIZIO And
                   OM.ESERCIZIO_ORIGINALE = RIEPILOGO.ESERCIZIO_ORIGINALE And
                   OM.PG_ACCERTAMENTO     = RIEPILOGO.PG_ACCERTAMENTO And
                   OMV.CD_CDS             = OM.CD_CDS And
                   OMV.ESERCIZIO          = OM.ESERCIZIO And
                   OMV.PG_MODIFICA        = OM.PG_MODIFICA And
                   OMV.CD_CENTRO_RESPONSABILITA = RIEPILOGO.CD_CENTRO_RESPONSABILITA And
                   OMV.CD_LINEA_ATTIVITA = RIEPILOGO.CD_LINEA_ATTIVITA And
                   OM.PG_MODIFICA > 0), 0), -- IM_VARIAZIONI,
       Sum(RIEPILOGO.IM_VOCE), -- IM_ACCERTATO
       Sum(RIEPILOGO.IM_ASSOCIATO_DOC_AMM),
       Sum(RIEPILOGO.IM_ASSOCIATO_DOC_CONTABILE)
From (Select ACCERTAMENTO.ESERCIZIO,
             DIPARTIMENTO.CD_DIPARTIMENTO,
             DIPARTIMENTO.DS_DIPARTIMENTO,
             PROGETTO.CD_PROGETTO     CD_PROGETTO,
             PROGETTO.DS_PROGETTO     DS_PROGETTO,
             COM.CD_PROGETTO          CD_COMMESSA,
             COM.DS_PROGETTO          DS_COMMESSA,
             MODU.CD_PROGETTO         CD_MODULO,
             MODU.DS_PROGETTO         DS_MODULO,
             ACCERTAMENTO.CD_CDS_ORIGINE,
             ACCERTAMENTO.CD_UO_ORIGINE,
             ACCERTAMENTO_SCAD_VOCE.CD_CENTRO_RESPONSABILITA,
             ACCERTAMENTO_SCAD_VOCE.CD_LINEA_ATTIVITA,
             Nvl(LINEA_ATTIVITA.ds_linea_attivita,
             LINEA_ATTIVITA.denominazione) DS_GAE,
             CD_ELEMENTO_VOCE,
             (Select ACC_INT.ESERCIZIO_ORIGINALE
              From   ACCERTAMENTO ACC_INT
              Where  ACC_INT.CD_CDS              = ACCERTAMENTO.CD_CDS And
                     ACC_INT.ESERCIZIO           = ACCERTAMENTO.ESERCIZIO And
                     ACC_INT.ESERCIZIO_ORIGINALE = ACCERTAMENTO.ESERCIZIO_ORIGINALE And
                     ACC_INT.PG_ACCERTAMENTO     = ACCERTAMENTO.PG_ACCERTAMENTO And
                     ACCERTAMENTO.CD_CDS_ORIGINE In ('110', '111')
              Union
              Select To_Number(Substr(To_Char(ACC_INT.PG_ACCERTAMENTO), 1, 4))
              From   ACCERTAMENTO ACC_INT
              Where  ACC_INT.CD_CDS              = ACCERTAMENTO.CD_CDS And
                     ACC_INT.ESERCIZIO           = ACCERTAMENTO.ESERCIZIO And
                     ACC_INT.ESERCIZIO_ORIGINALE = ACCERTAMENTO.ESERCIZIO_ORIGINALE And
                     ACC_INT.PG_ACCERTAMENTO     = ACCERTAMENTO.PG_ACCERTAMENTO And
                     ACCERTAMENTO.CD_CDS_ORIGINE Not In ('110', '111') And
                     ACC_INT.PG_ACCERTAMENTO Between 200500000 And 3000000001
              Union
              Select ACC_INT.ESERCIZIO_ORIGINALE
              From   ACCERTAMENTO ACC_INT
              Where  ACC_INT.CD_CDS              = ACCERTAMENTO.CD_CDS And
                     ACC_INT.ESERCIZIO           = ACCERTAMENTO.ESERCIZIO And
                     ACC_INT.ESERCIZIO_ORIGINALE = ACCERTAMENTO.ESERCIZIO_ORIGINALE And
                     ACC_INT.PG_ACCERTAMENTO     = ACCERTAMENTO.PG_ACCERTAMENTO And
                     ACCERTAMENTO.CD_CDS_ORIGINE Not In ('110', '111') And
                     ACC_INT.PG_ACCERTAMENTO Not Between 200500000 And 3000000001) ESER_ORIGINE_RICOSTRUITA,
             ACCERTAMENTO.CD_CDS,
             ACCERTAMENTO.ESERCIZIO_ORIGINALE,
             ACCERTAMENTO.PG_ACCERTAMENTO, DS_ACCERTAMENTO,
             Nvl(IM_VOCE, 0) - Nvl((Select Sum(IM_MODIFICA)
                                    From   ACCERTAMENTO_MOD_VOCE OMV, ACCERTAMENTO_MODIFICA OM
                                    Where  OM.CD_CDS              = ACCERTAMENTO.CD_CDS And
                                           OM.ESERCIZIO           = ACCERTAMENTO.ESERCIZIO And
                                           OM.ESERCIZIO_ORIGINALE = ACCERTAMENTO.ESERCIZIO_ORIGINALE And
                                           OM.PG_ACCERTAMENTO     = ACCERTAMENTO.PG_ACCERTAMENTO And
                                           OMV.CD_CDS             = OM.CD_CDS And
                                           OMV.ESERCIZIO          = OM.ESERCIZIO And
                                           OMV.PG_MODIFICA        = OM.PG_MODIFICA And
                                           OMV.CD_CENTRO_RESPONSABILITA = ACCERTAMENTO_SCAD_VOCE.CD_CENTRO_RESPONSABILITA And
                                           OMV.CD_LINEA_ATTIVITA = ACCERTAMENTO_SCAD_VOCE.CD_LINEA_ATTIVITA And
                                           OM.PG_MODIFICA > 0), 0), -- IM_RESIDUO_INIZIALE
                Nvl((Select Sum(IM_MODIFICA)
                 From   ACCERTAMENTO_MOD_VOCE OMV, ACCERTAMENTO_MODIFICA OM
                 Where  OM.CD_CDS              = ACCERTAMENTO.CD_CDS And
                        OM.ESERCIZIO           = ACCERTAMENTO.ESERCIZIO And
                        OM.ESERCIZIO_ORIGINALE = ACCERTAMENTO.ESERCIZIO_ORIGINALE And
                        OM.PG_ACCERTAMENTO     = ACCERTAMENTO.PG_ACCERTAMENTO And
                        OMV.CD_CDS             = OM.CD_CDS And
                        OMV.ESERCIZIO          = OM.ESERCIZIO And
                        OMV.PG_MODIFICA        = OM.PG_MODIFICA And
                        OMV.CD_CENTRO_RESPONSABILITA = ACCERTAMENTO_SCAD_VOCE.CD_CENTRO_RESPONSABILITA And
                        OMV.CD_LINEA_ATTIVITA = ACCERTAMENTO_SCAD_VOCE.CD_LINEA_ATTIVITA And
                        OM.PG_MODIFICA > 0), 0), -- IM_VARIAZIONI,
             IM_VOCE, -- IM_ACCERTATO
             Nvl((Select IM_VOCE
              From   ACCERTAMENTO_SCADENZARIO ACC_SCAD_INT
              Where  ACC_SCAD_INT.CD_CDS                      = ACCERTAMENTO_SCAD_VOCE.CD_CDS And
                     ACC_SCAD_INT.ESERCIZIO                   = ACCERTAMENTO_SCAD_VOCE.ESERCIZIO And
                     ACC_SCAD_INT.ESERCIZIO_ORIGINALE         = ACCERTAMENTO_SCAD_VOCE.ESERCIZIO_ORIGINALE And
                     ACC_SCAD_INT.PG_ACCERTAMENTO             = ACCERTAMENTO_SCAD_VOCE.PG_ACCERTAMENTO And
                     ACC_SCAD_INT.PG_ACCERTAMENTO_SCADENZARIO = ACCERTAMENTO_SCAD_VOCE.PG_ACCERTAMENTO_SCADENZARIO And
                     ACC_SCAD_INT.IM_SCADENZA = ACC_SCAD_INT.IM_ASSOCIATO_DOC_AMM), 0) IM_ASSOCIATO_DOC_AMM,
             Nvl((Select IM_VOCE
              From   ACCERTAMENTO_SCADENZARIO ACC_SCAD_INT
              Where  ACC_SCAD_INT.CD_CDS                      = ACCERTAMENTO_SCAD_VOCE.CD_CDS And
                     ACC_SCAD_INT.ESERCIZIO                   = ACCERTAMENTO_SCAD_VOCE.ESERCIZIO And
                     ACC_SCAD_INT.ESERCIZIO_ORIGINALE         = ACCERTAMENTO_SCAD_VOCE.ESERCIZIO_ORIGINALE And
                     ACC_SCAD_INT.PG_ACCERTAMENTO             = ACCERTAMENTO_SCAD_VOCE.PG_ACCERTAMENTO And
                     ACC_SCAD_INT.PG_ACCERTAMENTO_SCADENZARIO = ACCERTAMENTO_SCAD_VOCE.PG_ACCERTAMENTO_SCADENZARIO And
                     ACC_SCAD_INT.IM_SCADENZA = ACC_SCAD_INT.IM_ASSOCIATO_DOC_CONTABILE), 0) IM_ASSOCIATO_DOC_CONTABILE
              From   ACCERTAMENTO, ACCERTAMENTO_SCAD_VOCE, LINEA_ATTIVITA,
                     progetto, Progetto com, Progetto modu, DIPARTIMENTO
              Where  ACCERTAMENTO.ESERCIZIO >= 2007 And
                     ACCERTAMENTO.ESERCIZIO_ORIGINALE < ACCERTAMENTO.ESERCIZIO And
                     ACCERTAMENTO.TI_GESTIONE        = 'E' And
                     ACCERTAMENTO_SCAD_VOCE.CD_CDS                      = ACCERTAMENTO.CD_CDS And
                     ACCERTAMENTO_SCAD_VOCE.ESERCIZIO                   = ACCERTAMENTO.ESERCIZIO And
                     ACCERTAMENTO_SCAD_VOCE.ESERCIZIO_ORIGINALE         = ACCERTAMENTO.ESERCIZIO_ORIGINALE And
                     ACCERTAMENTO_SCAD_VOCE.PG_ACCERTAMENTO             = ACCERTAMENTO.PG_ACCERTAMENTO And
                     ACCERTAMENTO_SCAD_VOCE.cd_centro_responsabilita    = LINEA_ATTIVITA.cd_centro_responsabilita And
                     ACCERTAMENTO_SCAD_VOCE.cd_LINEA_aTTIVITA           = LINEA_ATTIVITA.cd_LINEA_aTTIVITA And
                     LINEA_ATTIVITA.PG_PROGETTO (+)  = modu.PG_PROGETTO AND
                     modu.ESERCIZIO                  = ACCERTAMENTO.ESERCIZIO_ORIGINALE And
                     MODU.TIPO_FASE                  = 'X' And
                     modu.ESERCIZIO_PROGETTO_PADRE   = com.ESERCIZIO And
                     modu.PG_PROGETTO_PADRE          = com.PG_PROGETTO And
                     MODU.TIPO_FASE_PROGETTO_PADRE   = COM.TIPO_FASE And
                     com.ESERCIZIO_PROGETTO_PADRE    = progetto.ESERCIZIO And
                     com.PG_PROGETTO_PADRE           = progetto.PG_PROGETTO And
                     COM.TIPO_FASE_PROGETTO_PADRE    = PROGETTO.TIPO_FASE And
                     PROGETTO.CD_DIPARTIMENTO        = DIPARTIMENTO.CD_DIPARTIMENTO) RIEPILOGO
Group By RIEPILOGO.ESERCIZIO,
         RIEPILOGO.CD_DIPARTIMENTO,
         RIEPILOGO.DS_DIPARTIMENTO,
         RIEPILOGO.CD_PROGETTO,
         RIEPILOGO.DS_PROGETTO,
         RIEPILOGO.CD_COMMESSA,
         RIEPILOGO.DS_COMMESSA,
         RIEPILOGO.CD_MODULO,
         RIEPILOGO.DS_MODULO,
         RIEPILOGO.CD_CDS_ORIGINE,
         RIEPILOGO.CD_UO_ORIGINE,
         RIEPILOGO.CD_CENTRO_RESPONSABILITA,
         RIEPILOGO.CD_LINEA_ATTIVITA,
         RIEPILOGO.DS_GAE,
         RIEPILOGO.CD_ELEMENTO_VOCE,
         RIEPILOGO.ESER_ORIGINE_RICOSTRUITA,
         RIEPILOGO.CD_CDS,
         RIEPILOGO.ESERCIZIO_ORIGINALE,
         RIEPILOGO.PG_ACCERTAMENTO,
         RIEPILOGO.DS_ACCERTAMENTO;
