--------------------------------------------------------
--  DDL for View V_CONTRATTO_ANAG
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONTRATTO_ANAG" ("CD_CDS", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "STATO", "PG_CONTRATTO", "FIG_GIUR_EST", "TI_ENTITA", "NOME", "COGNOME", "RAGIONE_SOCIALE", "PARTITA_IVA", "NATURA_CONTABILE", "OGGETTO", "IM_CONTRATTO_ATTIVO", "IM_CONTRATTO_PASSIVO", "DS_TIPO_CONTRATTO", "DS_TIPO_ATTO", "DS_PROC_AMM", "DS_ORGANO") AS 
  select unita_organizzativa.CD_UNITA_PADRE, 
	   contratto.CD_UNITA_ORGANIZZATIVA,
	   contratto.ESERCIZIO,
	   contratto.STATO,
	   contratto.PG_CONTRATTO,
	   CONTRATTO.FIG_GIUR_EST,
	   anagrafico.TI_ENTITA, 
	   anagrafico.NOME, 
	   anagrafico.COGNOME,
	   anagrafico.RAGIONE_SOCIALE,
	   anagrafico.PARTITA_IVA,	
	   contratto.NATURA_CONTABILE,
	   contratto.OGGETTO,
	   contratto.IM_CONTRATTO_ATTIVO,
	   contratto.IM_CONTRATTO_PASSIVO,
	   tipo_contratto.DS_TIPO_CONTRATTO,
	   tipo_atto_amministrativo.DS_TIPO_ATTO,
	   procedure_amministrative.DS_PROC_AMM,
	   organo.DS_ORGANO	     
from   contratto, 
	   anagrafico, 
	   terzo, 
	   unita_organizzativa,
	   tipo_atto_amministrativo,
	   tipo_contratto,
	   organo,
	   procedure_amministrative	   
	   where  contratto.FIG_GIUR_EST 	 		   = terzo.CD_TERZO
and	   terzo.CD_ANAG			 		   = anagrafico.CD_ANAG
and	   contratto.CD_UNITA_ORGANIZZATIVA    = unita_organizzativa.CD_UNITA_ORGANIZZATIVA
and    contratto.CD_TIPO_CONTRATTO		  = tipo_contratto.CD_TIPO_CONTRATTO(+)
and	   contratto.CD_TIPO_ATTO			   = tipo_atto_amministrativo.CD_TIPO_ATTO(+)
and    contratto.CD_ORGANO				   = organo.CD_ORGANO(+)
and    contratto.CD_PROC_AMM			   = procedure_amministrative.CD_PROC_AMM(+)
ORDER  BY unita_organizzativa.CD_UNITA_PADRE, CONTRATTO.ESERCIZIO
;
