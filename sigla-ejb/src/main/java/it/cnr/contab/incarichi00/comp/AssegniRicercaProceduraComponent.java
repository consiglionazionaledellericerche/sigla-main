package it.cnr.contab.incarichi00.comp;

import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_attivitaBulk;
import it.cnr.contab.incarichi00.tabrif.bulk.Tipo_incaricoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class AssegniRicercaProceduraComponent extends IncarichiProceduraComponent {
	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = (SQLBuilder)super.selectBase(userContext, clauses, bulk);

		sql.addTableToHeader("TIPO_ATTIVITA");
		sql.addSQLJoin("INCARICHI_PROCEDURA.CD_TIPO_ATTIVITA","TIPO_ATTIVITA.CD_TIPO_ATTIVITA");
		sql.addSQLClause(FindClause.AND, "TIPO_ATTIVITA.TIPO_ASSOCIAZIONE", SQLBuilder.EQUALS, Tipo_attivitaBulk.ASS_ASSEGNI_RICERCA);

		sql.addTableToHeader("TIPO_INCARICO");
		sql.addSQLJoin("INCARICHI_PROCEDURA.CD_TIPO_INCARICO","TIPO_INCARICO.CD_TIPO_INCARICO");
		sql.addSQLClause(FindClause.AND, "TIPO_INCARICO.TIPO_ASSOCIAZIONE", SQLBuilder.EQUALS, Tipo_incaricoBulk.ASS_ASSEGNI_RICERCA);

		return sql;
	}
	public SQLBuilder selectTipo_attivitaByClause (UserContext userContext, OggettoBulk bulk, Tipo_attivitaBulk tipo_attivita, CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		    clause = tipo_attivita.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_attivita).createSQLBuilder();
		sql.addClause(FindClause.AND, "tipo_associazione", SQLBuilder.EQUALS, Tipo_incaricoBulk.ASS_ASSEGNI_RICERCA);
		sql.addClause(FindClause.AND, "fl_cancellato", SQLBuilder.EQUALS, Boolean.FALSE);
		if (clause != null) 
		  sql.addClause(clause);
		sql.addOrderBy("CD_TIPO_ATTIVITA");
		return sql;
	}
	public SQLBuilder selectBaseTipo_incaricoByClause (UserContext userContext, OggettoBulk bulk, Tipo_incaricoBulk tipo_incarico, CompoundFindClause clause)	throws ComponentException, PersistencyException
	{
		if (clause == null) 
		    clause = tipo_incarico.buildFindClauses(null);
		SQLBuilder sql = getHome(userContext, tipo_incarico).createSQLBuilder();
		sql.addClause(FindClause.AND, "tipo_associazione", SQLBuilder.EQUALS, Tipo_incaricoBulk.ASS_ASSEGNI_RICERCA);
		sql.addClause(FindClause.AND, "fl_cancellato", SQLBuilder.EQUALS, Boolean.FALSE);
		if (clause != null) 
		  sql.addClause(clause);
		sql.addOrderBy("CD_TIPO_INCARICO");
		return sql;
	}
}
