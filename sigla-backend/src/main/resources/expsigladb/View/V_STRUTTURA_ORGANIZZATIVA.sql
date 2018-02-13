--------------------------------------------------------
--  DDL for View V_STRUTTURA_ORGANIZZATIVA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_STRUTTURA_ORGANIZZATIVA" ("ESERCIZIO", "CD_ROOT", "CD_CDS", "CD_UNITA_ORGANIZZATIVA", "CD_CENTRO_RESPONSABILITA", "CD_CDR_AFFERENZA", "CD_TIPO_UNITA", "CD_TIPO_LIVELLO", "FL_UO_CDS", "FL_CDR_UO") AS 
  (
select
--
-- Date: 02/11/2005
-- Version: 1.0
--
-- Vista della struttura organizzativa CDS/UO/CDR
-- La vista controlla la validita di STO
--
-- History:
--
-- Date: 02/11/2005
-- Version: 1.0
-- Creazione
--
--
-- Body:
--
 a.ESERCIZIO
,a.CD_UNITA_ORGANIZZATIVA   CD_ROOT
,a.CD_UNITA_ORGANIZZATIVA   CD_CDS
,null                       CD_UNITA_ORGANIZZATIVA
,null                       CD_CENTRO_RESPONSABILITA
,null                       CD_CDR_AFFERENZA
,a.CD_TIPO_UNITA            CD_TIPO_UNITA
,'CDS'                      CD_TIPO_LIVELLO
,'N' FL_UO_CDS
,'N' FL_CDR_UO
From V_UNITA_ORGANIZZATIVA_VALIDA a, TIPO_UNITA_ORGANIZZATIVA b
Where a.fl_cds = 'Y'
And   a.cd_tipo_unita = b.cd_tipo_unita
Union
Select
 a.ESERCIZIO
,a.CD_UNITA_ORGANIZZATIVA   CD_ROOT
,a.CD_UNITA_PADRE           CD_CDS
,a.CD_UNITA_ORGANIZZATIVA   CD_UNITA_ORGANIZZATIVA
,null                       CD_CENTRO_RESPONSABILITA
,null                       CD_CDR_AFFERENZA
,a.CD_TIPO_UNITA            CD_TIPO_UNITA
,'UO'                       CD_TIPO_LIVELLO
,a.FL_UO_CDS                FL_UO_CDS
,'N'                        FL_CDR_UO
From V_UNITA_ORGANIZZATIVA_VALIDA a, TIPO_UNITA_ORGANIZZATIVA b
Where  a.fl_cds = 'N'
And    a.cd_tipo_unita = b.cd_tipo_unita
Union
Select
 a.ESERCIZIO
,a.CD_CENTRO_RESPONSABILITA CD_ROOT
,b.CD_UNITA_PADRE           CD_CDS
,a.CD_UNITA_ORGANIZZATIVA   CD_UNITA_ORGANIZZATIVA
,a.CD_CENTRO_RESPONSABILITA CD_CENTRO_RESPONSABILITA
,Nvl(a.CD_CDR_AFFERENZA, a.CD_CENTRO_RESPONSABILITA)  CD_CDR_AFFERENZA
,'CDR'                      CD_TIPO_UNITA
,'CDR'                      CD_TIPO_LIVELLO
,b.FL_UO_CDS                FL_UO_CDS
,Decode(cd_proprio_cdr, '000', 'Y', 'N') FL_CDR_UO
From V_CDR_VALIDO a, UNITA_ORGANIZZATIVA b
Where a.cd_unita_organizzativa = b.cd_unita_organizzativa)
;
