package it.cnr.contab.doccont00.core.bulk;

import java.util.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;

public class ImpegnoHome extends ObbligazioneHome {
public ImpegnoHome(Class clazz, java.sql.Connection conn) {
	super(clazz, conn);
}
public ImpegnoHome(Class clazz, java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(clazz, conn, persistentCache);
}
public ImpegnoHome(java.sql.Connection conn) {
	super(ImpegnoBulk.class, conn);
}
public ImpegnoHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(ImpegnoBulk.class, conn, persistentCache);
}
/**
 * Metodo per selezionare le obbligazioni su partita di giro (di tipo IMP o OBB_PGIRO).
 *
 * @return sql il risultato della selezione
 */
public SQLBuilder createSQLBuilder() 
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause( "AND", "fl_pgiro", sql.EQUALS, Boolean.FALSE );

//	SimpleFindClause clause1 = new SimpleFindClause( "AND", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_IMP );
//	SimpleFindClause clause2 = new SimpleFindClause( "OR", "cd_tipo_documento_cont", sql.EQUALS, Numerazione_doc_contBulk.TIPO_OBB_PGIRO );
//	CompoundFindClause clause = new CompoundFindClause( clause1, clause2 );
//	sql.addClause( clause );
	return sql;
}

public SQLBuilder selectVoce_fByClause( ImpegnoBulk bulk, Voce_fHome home, Voce_fBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CNR );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE );
	sql.addClause("AND", "ti_voce", SQLBuilder.EQUALS, Voce_fHome.TIPO_SOTTOARTICOLO );
	// selezionando solo la parte 1 e' implicito che non siano partite di giro (fl_pgiro='N')		
	sql.addClause("AND", "cd_parte", SQLBuilder.EQUALS, Elemento_voceHome.PARTE_1 );
	if ( bulk.getCds() != null && bulk.getCds().getCd_tipo_unita()!=null && !bulk.getCds().getCd_tipo_unita().equalsIgnoreCase( Tipo_unita_organizzativaHome.TIPO_UO_SAC ) )
		sql.addClause("AND", "fl_voce_sac", SQLBuilder.EQUALS, new Boolean( false) );		
	sql.addClause( clause );
	return sql;
		
}

/**
 *  Recupera l'elemento voce di tipo categoria, da cui discende la voce
 *  passata come parametro, questo elemento voce verrà inserito in testata
 *  all'impegno per bilancio ente corrispondente alla voce inserita nel
 *  dettaglio della scadenza
 * @param voce
 * @return
 * @throws PersistencyException
 */
public Elemento_voceBulk findElementoVoceFor(Voce_fBulk voce) throws PersistencyException {

	if (voce==null)
		return null;

	Elemento_voceBulk elVoce = null;
	PersistentHome dettHome = getHomeCache().getHome(Elemento_voceBulk.class);
	SQLBuilder sql = dettHome.createSQLBuilder();
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, voce.getEsercizio() );		
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, Elemento_voceHome.APPARTENENZA_CNR );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, Elemento_voceHome.GESTIONE_SPESE );

	if (voce.getCd_categoria().equals(Voce_fHome.CATEGORIA1_SPESE_CNR)) {
	
		sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, Elemento_voceHome.TIPO_CATEGORIA );
		sql.addSQLClause("AND", "substr('"+voce.getCd_voce()+"',1,length(cd_elemento_voce))=cd_elemento_voce");
	
	}
	else {
		sql.addClause("AND", "cd_elemento_voce", SQLBuilder.EQUALS, voce.getCd_titolo_capitolo());
	}

	java.util.List lista =  dettHome.fetchAll(sql);
	if ( lista.size() > 0 )
		return (Elemento_voceBulk) lista.get(0);
	return null;
}
}
