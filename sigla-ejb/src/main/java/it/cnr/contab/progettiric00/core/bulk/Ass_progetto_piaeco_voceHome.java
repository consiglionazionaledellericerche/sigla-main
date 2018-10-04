package it.cnr.contab.progettiric00.core.bulk;

import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class Ass_progetto_piaeco_voceHome extends BulkHome {

	public Ass_progetto_piaeco_voceHome(java.sql.Connection conn) {
		super(Ass_progetto_piaeco_voceBulk.class,conn);
	}
	
	public Ass_progetto_piaeco_voceHome(java.sql.Connection conn,PersistentCache persistentCache) {
		super(Ass_progetto_piaeco_voceBulk.class,conn,persistentCache);
	}

	public java.util.Collection findAssProgettoPiaecoVoceList( java.lang.Integer pgProgetto, java.lang.String cdUnitaOrganizzativa, java.lang.String cdVocePiano, java.lang.Integer esercizioPiano ) throws PersistencyException 
	{
		SQLBuilder sql = this.createSQLBuilder();
		sql.addClause(FindClause.AND, "pg_progetto",SQLBuilder.EQUALS,pgProgetto);
		sql.addClause(FindClause.AND, "cd_unita_organizzativa",SQLBuilder.EQUALS,cdUnitaOrganizzativa);
		sql.addClause(FindClause.AND, "cd_voce_piano",SQLBuilder.EQUALS,cdVocePiano);
		sql.addClause(FindClause.AND, "esercizio_piano",SQLBuilder.EQUALS,esercizioPiano);
		return this.fetchAll(sql);
	}
}
