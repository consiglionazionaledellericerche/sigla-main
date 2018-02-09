CREATE OR REPLACE PROCEDURE SPG_DPDG_CDR_PRIMO
--
-- Date: 23/02/2003
-- Version: 1.0
--
-- Protocollo VPG per stampa massiva di mandati
--
--
-- History:
--
-- Date: 23/02/2003
-- Version: 1.0
-- Creazione
--
-- Body:
--
(
 tc in out IBMPRT000.t_cursore,
 aControllo varchar2,
 aEsercizio in number
) is
 aId number;
 i number;
begin
 select IBMSEQ00_CR_PACKAGE.nextval into aId from dual;

 i:=0;
 for aCdrPrimo in (select * from v_cdr_valido where
                       esercizio = aEsercizio
				   and (livello = 1 or cd_cdr_afferenza is null)
				  ) loop
  for aSpe in (select * from vp_dpdg_spe_cdr_primo where
                   esercizio = aEsercizio
			   and cd_centro_responsabilita = aCdrPrimo.cd_centro_responsabilita
			  ) loop
   i:=i+1;
   insert into vpg_dpdg_cdr_primo (
     ID
    ,CHIAVE
    ,TIPO
    ,SEQUENZA
    ,DESCRIZIONE
    ,ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,DS_CDR
    ,CD_UNITA_ORGANIZZATIVA
    ,DS_UNITA_ORGANIZZATIVA
    ,CD_CDS
    ,DS_CDS
    ,STATO
    ,ANNOTAZIONI
    ,FL_RIBALTATO_SU_AREA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,DS_ELEMENTO_VOCE
    ,CD_TITOLO
    ,DS_TITOLO
    ,CD_CAPOCONTO
    ,DS_CAPOCONTO
    ,CD_FUNZIONE
    ,DS_FUNZIONE
    ,CD_NATURA
    ,DS_NATURA
    ,IM_RH_CCS_COSTI
    ,IM_RI_CCS_SPESE_ODC
    ,IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,IM_RK_CCS_SPESE_OGC
    ,IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,IM_RM_CSS_AMMORTAMENTI
    ,IM_RN_CSS_RIMANENZE
    ,IM_RO_CSS_ALTRI_COSTI
    ,IM_RP_CSS_VERSO_ALTRO_CDR
    ,IM_RQ_SSC_COSTI_ODC
    ,IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,IM_RS_SSC_COSTI_OGC
    ,IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,IM_RU_SPESE_COSTI_ALTRUI
    ,IM_RV_PAGAMENTI
    ,IM_RAA_A2_COSTI_FINALI
    ,IM_RAB_A2_COSTI_ALTRO_CDR
    ,IM_RAC_A2_SPESE_ODC
    ,IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,IM_RAE_A2_SPESE_OGC
    ,IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,IM_RAH_A3_COSTI_FINALI
    ,IM_RAI_A3_COSTI_ALTRO_CDR
    ,IM_RAL_A3_SPESE_ODC
    ,IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,IM_RAN_A3_SPESE_OGC
    ,IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,IM_RA_RCE
    ,IM_RB_RSE
    ,IM_RC_ESR
    ,IM_RD_A2_RICAVI
    ,IM_RE_A2_ENTRATE
    ,IM_RF_A3_RICAVI
    ,IM_RG_A3_ENTRATE
   ) values (
     aId
	,'*'
    ,aControllo
    ,i
    ,'*'
    ,aSpe.ESERCIZIO
    ,aSpe.CD_CENTRO_RESPONSABILITA
    ,aSpe.DS_CDR
    ,aSpe.CD_UNITA_ORGANIZZATIVA
    ,aSpe.DS_UNITA_ORGANIZZATIVA
    ,aSpe.CD_CDS
    ,aSpe.DS_CDS
    ,aSpe.STATO
    ,aSpe.ANNOTAZIONI
    ,aSpe.FL_RIBALTATO_SU_AREA
    ,aSpe.TI_APPARTENENZA
    ,aSpe.TI_GESTIONE
    ,aSpe.CD_ELEMENTO_VOCE
    ,aSpe.DS_ELEMENTO_VOCE
    ,aSpe.CD_TITOLO
    ,aSpe.DS_TITOLO
    ,aSpe.CD_CAPOCONTO
    ,aSpe.DS_CAPOCONTO
    ,aSpe.CD_FUNZIONE
    ,aSpe.DS_FUNZIONE
    ,aSpe.CD_NATURA
    ,aSpe.DS_NATURA
    ,aSpe.IM_RH_CCS_COSTI
    ,aSpe.IM_RI_CCS_SPESE_ODC
    ,aSpe.IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,aSpe.IM_RK_CCS_SPESE_OGC
    ,aSpe.IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,aSpe.IM_RM_CSS_AMMORTAMENTI
    ,aSpe.IM_RN_CSS_RIMANENZE
    ,aSpe.IM_RO_CSS_ALTRI_COSTI
    ,aSpe.IM_RP_CSS_VERSO_ALTRO_CDR
    ,aSpe.IM_RQ_SSC_COSTI_ODC
    ,aSpe.IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,aSpe.IM_RS_SSC_COSTI_OGC
    ,aSpe.IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,aSpe.IM_RU_SPESE_COSTI_ALTRUI
    ,aSpe.IM_RV_PAGAMENTI
    ,aSpe.IM_RAA_A2_COSTI_FINALI
    ,aSpe.IM_RAB_A2_COSTI_ALTRO_CDR
    ,aSpe.IM_RAC_A2_SPESE_ODC
    ,aSpe.IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,aSpe.IM_RAE_A2_SPESE_OGC
    ,aSpe.IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,aSpe.IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,aSpe.IM_RAH_A3_COSTI_FINALI
    ,aSpe.IM_RAI_A3_COSTI_ALTRO_CDR
    ,aSpe.IM_RAL_A3_SPESE_ODC
    ,aSpe.IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,aSpe.IM_RAN_A3_SPESE_OGC
    ,aSpe.IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,aSpe.IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,0
    ,0
    ,0
    ,0
    ,0
    ,0
    ,0
   );
  end loop;
  for aEtr in (select * from vp_dpdg_etr_cdr_primo where
                   esercizio = aEsercizio
			   and cd_centro_responsabilita = aCdrPrimo.cd_centro_responsabilita
			  ) loop
   i:=i+1;
   insert into vpg_dpdg_cdr_primo (
     ID
    ,CHIAVE
    ,TIPO
    ,SEQUENZA
    ,DESCRIZIONE
    ,ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,DS_CDR
    ,CD_UNITA_ORGANIZZATIVA
    ,DS_UNITA_ORGANIZZATIVA
    ,CD_CDS
    ,DS_CDS
    ,STATO
    ,ANNOTAZIONI
    ,FL_RIBALTATO_SU_AREA
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,DS_ELEMENTO_VOCE
    ,CD_TITOLO
    ,DS_TITOLO
    ,CD_CAPOCONTO
    ,DS_CAPOCONTO
    ,CD_FUNZIONE
    ,DS_FUNZIONE
    ,CD_NATURA
    ,DS_NATURA
    ,IM_RH_CCS_COSTI
    ,IM_RI_CCS_SPESE_ODC
    ,IM_RJ_CCS_SPESE_ODC_ALTRA_UO
    ,IM_RK_CCS_SPESE_OGC
    ,IM_RL_CCS_SPESE_OGC_ALTRA_UO
    ,IM_RM_CSS_AMMORTAMENTI
    ,IM_RN_CSS_RIMANENZE
    ,IM_RO_CSS_ALTRI_COSTI
    ,IM_RP_CSS_VERSO_ALTRO_CDR
    ,IM_RQ_SSC_COSTI_ODC
    ,IM_RR_SSC_COSTI_ODC_ALTRA_UO
    ,IM_RS_SSC_COSTI_OGC
    ,IM_RT_SSC_COSTI_OGC_ALTRA_UO
    ,IM_RU_SPESE_COSTI_ALTRUI
    ,IM_RV_PAGAMENTI
    ,IM_RAA_A2_COSTI_FINALI
    ,IM_RAB_A2_COSTI_ALTRO_CDR
    ,IM_RAC_A2_SPESE_ODC
    ,IM_RAD_A2_SPESE_ODC_ALTRA_UO
    ,IM_RAE_A2_SPESE_OGC
    ,IM_RAF_A2_SPESE_OGC_ALTRA_UO
    ,IM_RAG_A2_SPESE_COSTI_ALTRUI
    ,IM_RAH_A3_COSTI_FINALI
    ,IM_RAI_A3_COSTI_ALTRO_CDR
    ,IM_RAL_A3_SPESE_ODC
    ,IM_RAM_A3_SPESE_ODC_ALTRA_UO
    ,IM_RAN_A3_SPESE_OGC
    ,IM_RAO_A3_SPESE_OGC_ALTRA_UO
    ,IM_RAP_A3_SPESE_COSTI_ALTRUI
    ,IM_RA_RCE
    ,IM_RB_RSE
    ,IM_RC_ESR
    ,IM_RD_A2_RICAVI
    ,IM_RE_A2_ENTRATE
    ,IM_RF_A3_RICAVI
    ,IM_RG_A3_ENTRATE
   ) values (
     aId
    ,'*'
    ,aControllo
    ,i
    ,'*'
    ,aEtr.ESERCIZIO
    ,aEtr.CD_CENTRO_RESPONSABILITA
    ,aEtr.DS_CDR
    ,aEtr.CD_UNITA_ORGANIZZATIVA
    ,aEtr.DS_UNITA_ORGANIZZATIVA
    ,aEtr.CD_CDS
    ,aEtr.DS_CDS
    ,aEtr.STATO
    ,aEtr.ANNOTAZIONI
    ,aEtr.FL_RIBALTATO_SU_AREA
    ,aEtr.TI_APPARTENENZA
    ,aEtr.TI_GESTIONE
    ,aEtr.CD_ELEMENTO_VOCE
    ,aEtr.DS_ELEMENTO_VOCE
    ,aEtr.CD_TITOLO
    ,aEtr.DS_TITOLO
    ,aEtr.CD_CAPOCONTO
    ,aEtr.DS_CAPOCONTO
    ,aEtr.CD_FUNZIONE
    ,aEtr.DS_FUNZIONE
    ,aEtr.CD_NATURA
    ,aEtr.DS_NATURA
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
	,0
    ,aEtr.IM_RA_RCE
    ,aEtr.IM_RB_RSE
    ,aEtr.IM_RC_ESR
    ,aEtr.IM_RD_A2_RICAVI
    ,aEtr.IM_RE_A2_ENTRATE
    ,aEtr.IM_RF_A3_RICAVI
    ,aEtr.IM_RG_A3_ENTRATE
   );
  end loop;
 end loop;

 open tc for
  select * from VPG_DPDG_CDR_PRIMO where id = aId;

-- close tc; --- ELIMINARE PER RICHIAMARE DA CR !!!

end;
/


