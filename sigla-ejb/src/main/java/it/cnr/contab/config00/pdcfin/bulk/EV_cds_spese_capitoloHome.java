package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;
/**
 * Home che gestisce i capitoli di spesa del CDS
 */
public class EV_cds_spese_capitoloHome extends Elemento_voceHome {
	public EV_cds_spese_capitoloHome(java.sql.Connection conn) {
	super(EV_cds_spese_capitoloBulk.class,conn);
}
public EV_cds_spese_capitoloHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(EV_cds_spese_capitoloBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti gli elementi voce quelli relativi ai capitoli di spesa
 * del CDS
 * @return SQLBuilder 
 */

public SQLBuilder createSQLBuilder() 
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, APPARTENENZA_CDS );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, GESTIONE_SPESE );
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, TIPO_CAPITOLO );
	sql.openParenthesis(FindClause.AND);
	sql.addClause(FindClause.OR, "cd_parte", SQLBuilder.ISNULL,null );			
	sql.addClause(FindClause.OR, "cd_parte", SQLBuilder.EQUALS, PARTE_1 );
	sql.closeParenthesis();
	return sql;
}
/**
 * Carica in una hashtable l'elenco di Funzioni presenti nel database
 * @return it.cnr.jada.util.OrderedHashtable
 */

public it.cnr.jada.util.OrderedHashtable loadFunzioni( EV_cds_spese_capitoloBulk  bulk) throws it.cnr.jada.persistency.IntrospectionException, it.cnr.jada.persistency.PersistencyException
{
	return new FunzioneHome( getConnection()).loadFunzioni();
	
}
/**
 * Carica in una hashtable l'elenco di Tipologie di CDS  presenti nel database
 * @return it.cnr.jada.util.OrderedHashtable
 */

public it.cnr.jada.util.OrderedHashtable loadTipiCds( EV_cds_spese_capitoloBulk bulk ) throws it.cnr.jada.persistency.IntrospectionException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.comp.ApplicationException 
{
	return new Tipo_unita_organizzativaHome( getConnection()).loadTipologiaCdsKeys();
}
/**
 * Restituisce il SQLBuilder per selezionare i Titoli di Spesa del Cds Parte 1 per l'esercizio di scrivania
 * @param bulk bulk ricevente
 * @param home home del bulk su cui si cerca
 * @param bulkClause è l'istanza di bulk che ha indotto le clauses 
 * @param clause clause che arrivano dalle properties (form collegata al search tool) 
 * @return it.cnr.jada.persistency.sql.SQLBuilder
 */

public SQLBuilder selectElemento_padreByClause(EV_cds_spese_capitoloBulk bulk,Elemento_voceHome home,it.cnr.jada.bulk.OggettoBulk bulkClause,CompoundFindClause clause) throws java.lang.reflect.InvocationTargetException,IllegalAccessException, it.cnr.jada.persistency.PersistencyException {
	SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "ti_appartenenza", SQLBuilder.EQUALS, APPARTENENZA_CDS );
	sql.addClause("AND", "ti_gestione", SQLBuilder.EQUALS, GESTIONE_SPESE );
	sql.addClause("AND", "cd_parte", SQLBuilder.EQUALS, PARTE_1 );				
	sql.addClause("AND", "ti_elemento_voce", SQLBuilder.EQUALS, (String) Elemento_voceHome.getTipoPadre(APPARTENENZA_CDS,GESTIONE_SPESE, TIPO_CAPITOLO ));
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, bulk.getEsercizio() );		
	sql.addClause( clause);
	return sql;
}
}
