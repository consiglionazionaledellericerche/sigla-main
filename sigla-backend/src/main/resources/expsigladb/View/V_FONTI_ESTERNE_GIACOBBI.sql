--------------------------------------------------------
--  DDL for View V_FONTI_ESTERNE_GIACOBBI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_FONTI_ESTERNE_GIACOBBI" ("ESERCIZIO_COMPETENZA", "ESERCIZIO_RESIDUO", "CDS", "NATURA_DEI_FONDI", "COMPETENZA_PREVISIONE_ASS", "COMPENTENZA_IMPEGNATO", "COMPENTENZA_DISPONIBILITA", "IMPEGNATO_RESIDUI", "DISPONIBILITA_RESIDUE") AS 
  ( select
ESERCIZIO   esercizio_competenza ,
ESERCIZIO_RES esercizio_residuo,
substr( v.CD_CENTRO_RESPONSABILITA,1,3)cds,
decode(N.TIPO, 'FES','Fonti Esterne', 'FIN', 'Fonti interne') natura_dei_fondi,
(sum(IM_STANZ_INIZIALE_A1+VARIAZIONI_PIU-VARIAZIONI_MENO))
competenza_previsione_ass,
sum(IM_OBBL_ACC_COMP) compentenza_impegnato ,
(sum(IM_STANZ_INIZIALE_A1+VARIAZIONI_PIU-VARIAZIONI_MENO))-sum(IM_OBBL_ACC_COMP)
compentenza_disponibilita,
sum(IM_OBBL_RES_IMP +
V.IM_OBBL_RES_PRO+V.VAR_PIU_OBBL_RES_PRO-V.VAR_MENO_OBBL_RES_PRO)
impegnato_residui,
(sum(IM_STANZ_RES_IMPROPRIO+VAR_PIU_STANZ_RES_IMP-VAR_MENO_STANZ_RES_IMP+VAR_MENO_OBBL_RES_PRO-VAR_PIU_OBBL_RES_PRO)-sum(IM_OBBL_RES_IMP))
disponibilita_residue
from voce_f_saldi_cdr_linea v, linea_attivita l, natura n where L.CD_CENTRO_RESPONSABILITA = V.CD_CENTRO_RESPONSABILITA and L.CD_LINEA_ATTIVITA = V.CD_LINEA_ATTIVITA and L.CD_NATURA = N.CD_NATURA and v.ti_gestione = 'S'
and N.TIPO ='FES'
group by ESERCIZIO    ,
ESERCIZIO_RES,
substr( v.CD_CENTRO_RESPONSABILITA,1,3), decode(N.TIPO, 'FES','Fonti Esterne', 'FIN', 'Fonti interne')) ;
