--------------------------------------------------------
--  DDL for View V_ANALITICA_LDA_COMRES_PDG_SPE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_ANALITICA_LDA_COMRES_PDG_SPE" ("ESERCIZIO", "CD_CENTRO_RESPONSABILITA", "TI_APPARTENENZA", "TI_GESTIONE", "CD_ELEMENTO_VOCE", "CD_LINEA_ATTIVITA", "CD_FUNZIONE", "CD_NATURA", "DS_LINEA_ATTIVITA", "DS_VOCE", "IM_RI_CCS_SPESE_ODC_I", "IM_RK_CCS_SPESE_OGC_I", "IM_RQ_SSC_COSTI_ODC_I", "IM_RS_SSC_COSTI_OGC_I", "IM_RU_SPESE_COSTI_ALTRUI_I", "IM_RAC_A2_SPESE_ODC_I", "IM_RAE_A2_SPESE_OGC_I", "IM_RAG_A2_SPESE_COSTI_ALTRUI_I", "IM_RAL_A3_SPESE_ODC_I", "IM_RAN_A3_SPESE_OGC_I", "IM_RAP_A3_SPESE_COSTI_ALTRUI_I", "IM_RH_CCS_COSTI_I", "IM_RM_CSS_AMMORTAMENTI_I", "IM_RN_CSS_RIMANENZE_I", "IM_RO_CSS_ALTRI_COSTI_I", "IM_RP_CSS_VERSO_ALTRO_CDR_I", "IM_RAA_A2_COSTI_FINALI_I", "IM_RAB_A2_COSTI_ALTRO_CDR_I", "IM_RAH_A3_COSTI_FINALI_I", "IM_RAI_A3_COSTI_ALTRO_CDR_I", "IM_RI_CCS_SPESE_ODC_V", "IM_RK_CCS_SPESE_OGC_V", "IM_RQ_SSC_COSTI_ODC_V", "IM_RS_SSC_COSTI_OGC_V", "IM_RU_SPESE_COSTI_ALTRUI_V", "IM_RAC_A2_SPESE_ODC_V", "IM_RAE_A2_SPESE_OGC_V", "IM_RAG_A2_SPESE_COSTI_ALTRUI_V", "IM_RAL_A3_SPESE_ODC_V", "IM_RAN_A3_SPESE_OGC_V", "IM_RAP_A3_SPESE_COSTI_ALTRUI_V", "IM_RH_CCS_COSTI_V", "IM_RM_CSS_AMMORTAMENTI_V", "IM_RN_CSS_RIMANENZE_V", "IM_RO_CSS_ALTRI_COSTI_V", "IM_RP_CSS_VERSO_ALTRO_CDR_V", "IM_RAA_A2_COSTI_FINALI_V", "IM_RAB_A2_COSTI_ALTRO_CDR_V", "IM_RAH_A3_COSTI_FINALI_V", "IM_RAI_A3_COSTI_ALTRO_CDR_V", "IM_RESIDUO") AS 
  Select ESERCIZIO, CD_CENTRO_RESPONSABILITA, TI_APPARTENENZA, TI_GESTIONE, CD_ELEMENTO_VOCE, CD_LINEA_ATTIVITA,
       CD_FUNZIONE, CD_NATURA, DS_LINEA_ATTIVITA, DS_ELEMENTO_VOCE,
       -- spese iniziali anno 1
       Nvl(Sum(IM_RI_CCS_SPESE_ODC_I), 0),
       Nvl(Sum(IM_RK_CCS_SPESE_OGC_I), 0),
       Nvl(Sum(IM_RQ_SSC_COSTI_ODC_I), 0),
       Nvl(Sum(IM_RS_SSC_COSTI_OGC_I), 0),
       Nvl(Sum(IM_RU_SPESE_COSTI_ALTRUI_I), 0),
       -- spese iniziali anno 2
       Nvl(Sum(IM_RAC_A2_SPESE_ODC_I), 0),
       Nvl(Sum(IM_RAE_A2_SPESE_OGC_I), 0),
       Nvl(Sum(IM_RAG_A2_SPESE_COSTI_ALTRUI_I), 0),
       -- spese iniziali anno 3
       Nvl(Sum(IM_RAL_A3_SPESE_ODC_I), 0),
       Nvl(Sum(IM_RAN_A3_SPESE_OGC_I), 0),
       Nvl(Sum(IM_RAP_A3_SPESE_COSTI_ALTRUI_I), 0),
       -- costi iniziali anno 1
       Nvl(Sum(IM_RH_CCS_COSTI_I), 0),
       Nvl(Sum(IM_RM_CSS_AMMORTAMENTI_I), 0),
       Nvl(Sum(IM_RN_CSS_RIMANENZE_I), 0),
       Nvl(Sum(IM_RO_CSS_ALTRI_COSTI_I), 0),
       Nvl(Sum(IM_RP_CSS_VERSO_ALTRO_CDR_I), 0),
       -- costi iniziali anno 2
       Nvl(Sum(IM_RAA_A2_COSTI_FINALI_I), 0),
       Nvl(Sum(IM_RAB_A2_COSTI_ALTRO_CDR_I), 0),
       -- costi iniziali anno 3
       Nvl(Sum(IM_RAH_A3_COSTI_FINALI_I), 0),
       Nvl(Sum(IM_RAI_A3_COSTI_ALTRO_CDR_I), 0),
       -- spese variate anno 1
       Nvl(Sum(IM_RI_CCS_SPESE_ODC_V), 0),
       Nvl(Sum(IM_RK_CCS_SPESE_OGC_V), 0),
       Nvl(Sum(IM_RQ_SSC_COSTI_ODC_V), 0),
       Nvl(Sum(IM_RS_SSC_COSTI_OGC_V), 0),
       Nvl(Sum(IM_RU_SPESE_COSTI_ALTRUI_V), 0),
       -- spese variate anno 2
       Nvl(Sum(IM_RAC_A2_SPESE_ODC_V), 0),
       Nvl(Sum(IM_RAE_A2_SPESE_OGC_V), 0),
       Nvl(Sum(IM_RAG_A2_SPESE_COSTI_ALTRUI_V), 0),
       -- spese variate anno 3
       Nvl(Sum(IM_RAL_A3_SPESE_ODC_V), 0),
       Nvl(Sum(IM_RAN_A3_SPESE_OGC_V), 0),
       Nvl(Sum(IM_RAP_A3_SPESE_COSTI_ALTRUI_V), 0),
       -- costi variati anno 1
       Nvl(Sum(IM_RH_CCS_COSTI_V), 0),
       Nvl(Sum(IM_RM_CSS_AMMORTAMENTI_V), 0),
       Nvl(Sum(IM_RN_CSS_RIMANENZE_V), 0),
       Nvl(Sum(IM_RO_CSS_ALTRI_COSTI_V), 0),
       Nvl(Sum(IM_RP_CSS_VERSO_ALTRO_CDR_V), 0),
       -- costi variati anno 2
       Nvl(Sum(IM_RAA_A2_COSTI_FINALI_V), 0),
       Nvl(Sum(IM_RAB_A2_COSTI_ALTRO_CDR_V), 0),
       -- costi variati anno 3
       Nvl(Sum(IM_RAH_A3_COSTI_FINALI_V), 0),
       Nvl(Sum(IM_RAI_A3_COSTI_ALTRO_CDR_V), 0),
       --importo residuo
       Nvl(Sum(IM_RESIDUO), 0)
From
     (Select PDG_PREVENTIVO_SPE_DET.ESERCIZIO,
             PDG_PREVENTIVO_SPE_DET.CD_CENTRO_RESPONSABILITA,
             PDG_PREVENTIVO_SPE_DET.TI_APPARTENENZA,
             PDG_PREVENTIVO_SPE_DET.TI_GESTIONE,
             PDG_PREVENTIVO_SPE_DET.CD_ELEMENTO_VOCE,
             PDG_PREVENTIVO_SPE_DET.CD_LINEA_ATTIVITA,
             LINEA_ATTIVITA.CD_FUNZIONE,
             LINEA_ATTIVITA.CD_NATURA,
             LINEA_ATTIVITA.DS_LINEA_ATTIVITA,
             ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
             -- spese iniziali anno 1
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC, 0)           IM_RI_CCS_SPESE_ODC_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC, 0)           IM_RK_CCS_SPESE_OGC_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC, 0)           IM_RQ_SSC_COSTI_ODC_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC, 0)           IM_RS_SSC_COSTI_OGC_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI, 0)      IM_RU_SPESE_COSTI_ALTRUI_I,
             -- spese iniziali anno 2
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RAC_A2_SPESE_ODC, 0)           IM_RAC_A2_SPESE_ODC_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RAE_A2_SPESE_OGC, 0)           IM_RAE_A2_SPESE_OGC_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RAG_A2_SPESE_COSTI_ALTRUI, 0)  IM_RAG_A2_SPESE_COSTI_ALTRUI_I,
             -- spese iniziali anno 3
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RAL_A3_SPESE_ODC, 0)           IM_RAL_A3_SPESE_ODC_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RAN_A3_SPESE_OGC, 0)           IM_RAN_A3_SPESE_OGC_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RAP_A3_SPESE_COSTI_ALTRUI, 0)  IM_RAP_A3_SPESE_COSTI_ALTRUI_I,
             -- costi iniziali anno 1
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RH_CCS_COSTI, 0)               IM_RH_CCS_COSTI_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RM_CSS_AMMORTAMENTI, 0)        IM_RM_CSS_AMMORTAMENTI_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RN_CSS_RIMANENZE, 0)           IM_RN_CSS_RIMANENZE_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RO_CSS_ALTRI_COSTI, 0)         IM_RO_CSS_ALTRI_COSTI_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RP_CSS_VERSO_ALTRO_CDR, 0)     IM_RP_CSS_VERSO_ALTRO_CDR_I,
             -- costi iniziali anno 2
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RAA_A2_COSTI_FINALI, 0)        IM_RAA_A2_COSTI_FINALI_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RAB_A2_COSTI_ALTRO_CDR, 0)     IM_RAB_A2_COSTI_ALTRO_CDR_I,
             -- costi iniziali anno 3
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RAH_A3_COSTI_FINALI, 0)        IM_RAH_A3_COSTI_FINALI_I,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        PDG_PREVENTIVO_SPE_DET.IM_RAI_A3_COSTI_ALTRO_CDR, 0)     IM_RAI_A3_COSTI_ALTRO_CDR_I,
             -- spese variate anno 1
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RI_CCS_SPESE_ODC)           IM_RI_CCS_SPESE_ODC_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RK_CCS_SPESE_OGC)           IM_RK_CCS_SPESE_OGC_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RQ_SSC_COSTI_ODC)           IM_RQ_SSC_COSTI_ODC_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RS_SSC_COSTI_OGC)           IM_RS_SSC_COSTI_OGC_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RU_SPESE_COSTI_ALTRUI)      IM_RU_SPESE_COSTI_ALTRUI_V,
             -- spese variate anno 2
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RAC_A2_SPESE_ODC)           IM_RAC_A2_SPESE_ODC_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RAE_A2_SPESE_OGC)           IM_RAE_A2_SPESE_OGC_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RAG_A2_SPESE_COSTI_ALTRUI)  IM_RAG_A2_SPESE_COSTI_ALTRUI_V,
             -- spese variate anno 3
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RAL_A3_SPESE_ODC)           IM_RAL_A3_SPESE_ODC_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RAN_A3_SPESE_OGC)           IM_RAN_A3_SPESE_OGC_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RAP_A3_SPESE_COSTI_ALTRUI)  IM_RAP_A3_SPESE_COSTI_ALTRUI_V,
             -- costi variati anno 1
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RH_CCS_COSTI)               IM_RH_CCS_COSTI_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RM_CSS_AMMORTAMENTI)        IM_RM_CSS_AMMORTAMENTI_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RN_CSS_RIMANENZE)           IM_RN_CSS_RIMANENZE_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RO_CSS_ALTRI_COSTI)         IM_RO_CSS_ALTRI_COSTI_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RP_CSS_VERSO_ALTRO_CDR)     IM_RP_CSS_VERSO_ALTRO_CDR_V,
             -- costi variati anno 2
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RAA_A2_COSTI_FINALI)        IM_RAA_A2_COSTI_FINALI_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RAB_A2_COSTI_ALTRO_CDR)     IM_RAB_A2_COSTI_ALTRO_CDR_V,
             -- costi variati anno 3
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RAH_A3_COSTI_FINALI)        IM_RAH_A3_COSTI_FINALI_V,
             Decode(PDG_PREVENTIVO_SPE_DET.PG_VARIAZIONE_PDG, Null,
                        0, PDG_PREVENTIVO_SPE_DET.IM_RAI_A3_COSTI_ALTRO_CDR)     IM_RAI_A3_COSTI_ALTRO_CDR_V,
             -- importo residuo
             0 IM_RESIDUO
      From
           PDG_PREVENTIVO_SPE_DET,
           LINEA_ATTIVITA,
           ELEMENTO_VOCE
      Where
           PDG_PREVENTIVO_SPE_DET.STATO = 'Y' And
           PDG_PREVENTIVO_SPE_DET.TI_GESTIONE = 'S' And
           PDG_PREVENTIVO_SPE_DET.TI_APPARTENENZA='D' And
--           PDG_PREVENTIVO_SPE_DET.CATEGORIA_DETTAGLIO='SIN' And
--           PDG_PREVENTIVO_SPE_DET.ORIGINE='DIR' And
           PDG_PREVENTIVO_SPE_DET.ORIGINE!='PDV' And
           --Join tra "PDG_PREVENTIVO_SPE_DET" e "LINEA_ATTIVITA"
           LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA = PDG_PREVENTIVO_SPE_DET.CD_CENTRO_RESPONSABILITA And
           LINEA_ATTIVITA.CD_LINEA_ATTIVITA = PDG_PREVENTIVO_SPE_DET.CD_LINEA_ATTIVITA And
           --Join tra "PDG_PREVENTIVO_SPE_DET" e "ELEMENTO_VOCE"
           ELEMENTO_VOCE.ESERCIZIO = PDG_PREVENTIVO_SPE_DET.ESERCIZIO And
           ELEMENTO_VOCE.TI_GESTIONE = PDG_PREVENTIVO_SPE_DET.TI_GESTIONE And
           ELEMENTO_VOCE.TI_APPARTENENZA = PDG_PREVENTIVO_SPE_DET.TI_APPARTENENZA And
           ELEMENTO_VOCE.CD_ELEMENTO_VOCE = PDG_PREVENTIVO_SPE_DET.CD_ELEMENTO_VOCE
      Union All
      Select PDG_RESIDUO_DET.ESERCIZIO,
             PDG_RESIDUO_DET.CD_CDR_LINEA CD_CENTRO_RESPONSABILITA,
             PDG_RESIDUO_DET.TI_APPARTENENZA,
             PDG_RESIDUO_DET.TI_GESTIONE,
             PDG_RESIDUO_DET.CD_ELEMENTO_VOCE,
             PDG_RESIDUO_DET.CD_LINEA_ATTIVITA,
             LINEA_ATTIVITA.CD_FUNZIONE,
             LINEA_ATTIVITA.CD_NATURA,
             LINEA_ATTIVITA.DS_LINEA_ATTIVITA,
             ELEMENTO_VOCE.DS_ELEMENTO_VOCE,
             -- spese iniziali anno 1
             0 IM_RI_CCS_SPESE_ODC_I,
             0 IM_RK_CCS_SPESE_OGC_I,
             0 IM_RQ_SSC_COSTI_ODC_I,
             0 IM_RS_SSC_COSTI_OGC_I,
             0 IM_RU_SPESE_COSTI_ALTRUI_I,
             -- spese iniziali anno 2
             0 IM_RAC_A2_SPESE_ODC_I,
             0 IM_RAE_A2_SPESE_OGC_I,
             0 IM_RAG_A2_SPESE_COSTI_ALTRUI_I,
             -- spese iniziali anno 3
             0 IM_RAL_A3_SPESE_ODC_I,
             0 IM_RAN_A3_SPESE_OGC_I,
             0 IM_RAP_A3_SPESE_COSTI_ALTRUI_I,
             -- costi iniziali anno 1
             0 IM_RH_CCS_COSTI_I,
             0 IM_RM_CSS_AMMORTAMENTI_I,
             0 IM_RN_CSS_RIMANENZE_I,
             0 IM_RO_CSS_ALTRI_COSTI_I,
             0 IM_RP_CSS_VERSO_ALTRO_CDR_I,
             -- costi iniziali anno 2
             0 IM_RAA_A2_COSTI_FINALI_I,
             0 IM_RAB_A2_COSTI_ALTRO_CDR_I,
             -- costi iniziali anno 3
             0 IM_RAH_A3_COSTI_FINALI_I,
             0 IM_RAI_A3_COSTI_ALTRO_CDR_I,
             -- spese variate anno 1
             0 IM_RI_CCS_SPESE_ODC_V,
             0 IM_RK_CCS_SPESE_OGC_V,
             0 IM_RQ_SSC_COSTI_ODC_V,
             0 IM_RS_SSC_COSTI_OGC_V,
             0 IM_RU_SPESE_COSTI_ALTRUI_V,
             -- spese variate anno 2
             0 IM_RAC_A2_SPESE_ODC_V,
             0 IM_RAE_A2_SPESE_OGC_V,
             0 IM_RAG_A2_SPESE_COSTI_ALTRUI_V,
             -- spese variate anno 3
             0 IM_RAL_A3_SPESE_ODC_V,
             0 IM_RAN_A3_SPESE_OGC_V,
             0 IM_RAP_A3_SPESE_COSTI_ALTRUI_V,
             -- costi variati anno 1
             0 IM_RH_CCS_COSTI_V,
             0 IM_RM_CSS_AMMORTAMENTI_V,
             0 IM_RN_CSS_RIMANENZE_V,
             0 IM_RO_CSS_ALTRI_COSTI_V,
             0 IM_RP_CSS_VERSO_ALTRO_CDR_V,
             -- costi variati anno 2
             0 IM_RAA_A2_COSTI_FINALI_V,
             0 IM_RAB_A2_COSTI_ALTRO_CDR_V,
             -- costi variati anno 3
             0 IM_RAH_A3_COSTI_FINALI_V,
             0 IM_RAI_A3_COSTI_ALTRO_CDR_V,
             -- importo residuo
             PDG_RESIDUO_DET.IM_RESIDUO
      From
           PDG_RESIDUO_DET,
           LINEA_ATTIVITA,
           ELEMENTO_VOCE
      Where
           PDG_RESIDUO_DET.STATO = 'I' And
           PDG_RESIDUO_DET.TI_GESTIONE = 'S' And
           PDG_RESIDUO_DET.TI_APPARTENENZA='D' And
           --Join tra "PDG_RESIDUO_DET" e "LINEA_ATTIVITA"
           LINEA_ATTIVITA.CD_CENTRO_RESPONSABILITA = PDG_RESIDUO_DET.CD_CDR_LINEA And
           LINEA_ATTIVITA.CD_LINEA_ATTIVITA = PDG_RESIDUO_DET.CD_LINEA_ATTIVITA And
           --Join tra "PDG_PREVENTIVO_SPE_DET" e "ELEMENTO_VOCE"
           ELEMENTO_VOCE.ESERCIZIO = PDG_RESIDUO_DET.ESERCIZIO And
           ELEMENTO_VOCE.TI_GESTIONE = PDG_RESIDUO_DET.TI_GESTIONE And
           ELEMENTO_VOCE.TI_APPARTENENZA = PDG_RESIDUO_DET.TI_APPARTENENZA And
           ELEMENTO_VOCE.CD_ELEMENTO_VOCE = PDG_RESIDUO_DET.CD_ELEMENTO_VOCE)
Group By
         ESERCIZIO,
         CD_CENTRO_RESPONSABILITA,
         CD_LINEA_ATTIVITA,
         TI_APPARTENENZA,
         TI_GESTIONE,
         CD_ELEMENTO_VOCE,
         CD_FUNZIONE,
         CD_NATURA,
         DS_LINEA_ATTIVITA,
         DS_ELEMENTO_VOCE;
