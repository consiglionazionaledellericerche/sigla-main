--------------------------------------------------------
--  DDL for Package CNRMAR050
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRMAR050" as
--
-- CNRMAR050 - Package di controllo di quadratura del fondo economale
-- Date: 16/10/2003
-- Version: 1.0
--
-- Dependency:
--
-- History:
--
-- Date: 16/10/2003
-- Version: 1.0
-- Creazione
--
-- Constants
--
TIPO_LOG_CHK_FONDO CONSTANT VARCHAR2(20):='CHK_FONDO_ECO';
--NOTA_CREDITO CONSTANT CHAR(1):='C';
--STATO_ANNULLATO CONSTANT CHAR(1):='A';
--STATO_DEFINITIVO CONSTANT CHAR(1):='D';
-- Tipi di disallineamenti supportati

D_FONDO CONSTANT VARCHAR2(10):='D_FONDO';
D_FONDO000 CONSTANT VARCHAR2(70):='D_FONDO000-IM_TOT_SPESE<>SUM(SPESE)-SUM(REINTEGRI)';
D_FONDO005 CONSTANT VARCHAR2(70):='D_FONDO005-IM_TOT_NET_SPESE<>SUM(NETTO_SPESE)';
D_FONDO010 CONSTANT VARCHAR2(70):='D_FONDO010-IM_TOT_REINTEGRI<>SUM(REINTEGRI)';
D_FONDO015 CONSTANT VARCHAR2(70):='D_FONDO015-IM_AMM_INIZIALE<>MANDATOAPERTURA.IM_AMNDATO';
D_FONDO020 CONSTANT VARCHAR2(70):='D_FONDO020-IM_AMM_FONDO<>MAN_APE.IM_MAN+SUM(MAN_REI.IM_MAN)';
D_FONDO025 CONSTANT VARCHAR2(70):='D_FONDO025-IM_RES_FONDO<>MAN_APE+TOT_MAN_REI.IM_MAN-SPESE+REIN';

D_SPESA000 CONSTANT VARCHAR2(70):='D_SPESA000-STATO_PAGAMENTO_FONDO';
D_SPESA005 CONSTANT VARCHAR2(70):='D_SPESA005-DATA_PAGAMENTO_FONDO';
--
--
-- Parametri:
-- aEs -> Esercizio
-- aCDS -> cd_cds
--
-- Functions e Procedures
--
 procedure job_mar_primi00(job number, pg_exec number, next_date date, aEsercizio number, aCdCds varchar2);

 function MSG_DIS_PRIMI(aTipo varchar2,aFondo fondo_Economale%rowtype,aNota varchar2) return varchar2;

end;
