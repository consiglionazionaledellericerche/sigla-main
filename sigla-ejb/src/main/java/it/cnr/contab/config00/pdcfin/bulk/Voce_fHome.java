package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.contab.doccont00.core.bulk.Ass_partita_giroBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_fHome extends BulkHome {
		public final static String TIPO_ARTICOLO = "A" ;
		public final static String TIPO_SOTTOARTICOLO = "E" ;
		public final static String CATEGORIA1_SPESE_CNR = "1" ;
		public final static int LIVELLO_SOTTOARTICOLO_SPESE_CNR = 7;
protected Voce_fHome(Class clazz,java.sql.Connection connection) {
	super(clazz,connection);
}
protected Voce_fHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
	super(clazz,connection,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_fHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public Voce_fHome(java.sql.Connection conn) {
	super(Voce_fBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un Voce_fHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public Voce_fHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Voce_fBulk.class,conn,persistentCache);
}
public Voce_fBulk findVoce_fFor(Ass_partita_giroBulk ass_pgiro, String cd_uo) 
	throws PersistencyException {

	SQLBuilder sql = createSQLBuilder();
	/***** Err. 826 - BORRIELLO: Aggiunta clausola relativa all'esercizio per la ricerca della voce_f *********/
	sql.addClause("AND","esercizio",sql.EQUALS, ass_pgiro.getEsercizio() );
	/*****/
	
	sql.addClause("AND","ti_appartenenza",sql.EQUALS, ass_pgiro.getTi_appartenenza() );
	sql.addClause("AND","ti_gestione",sql.EQUALS, ass_pgiro.getTi_gestione() );
	sql.addClause("AND","cd_titolo_capitolo",sql.EQUALS, ass_pgiro.getCd_voce() );
	sql.addClause("AND","fl_mastrino",sql.EQUALS, new Boolean(true) );
	if ( cd_uo != null ) //CNR
		sql.addClause("AND","cd_unita_organizzativa",sql.EQUALS, cd_uo);
	
	java.util.List result = fetchAll( sql );
	if (result.size() > 0)
		return (Voce_fBulk) result.get(0);		

	return null;
}
public Persistent findByElementoVoce(Integer esercizio, String ti_appartenenza, String ti_gestione,String cd_unita_organizzativa, String cd_elemento_voce, String cd_funzione) throws PersistencyException {
	Voce_fBulk voce = null;
	PersistentHome home = getHomeCache().getHome(Voce_fBulk.class);	
	SQLBuilder sql = createSQLBuilder();	
	sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,esercizio);
	sql.addSQLClause("AND","TI_APPARTENENZA",SQLBuilder.EQUALS,ti_appartenenza);	
	sql.addSQLClause("AND","TI_GESTIONE",SQLBuilder.EQUALS,ti_gestione);
	sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,cd_unita_organizzativa);
	sql.addSQLClause("AND","CD_ELEMENTO_VOCE",SQLBuilder.EQUALS,cd_elemento_voce);
	sql.addSQLClause("AND","CD_FUNZIONE",SQLBuilder.EQUALS,cd_funzione);
	sql.addClause("AND","fl_mastrino",sql.EQUALS, new Boolean(true) );
	SQLBroker broker = home.createBroker(sql);
	if (broker.next())
	  voce = (Voce_fBulk)broker.fetch(Voce_fBulk.class);
	return voce;  
}
}																								
