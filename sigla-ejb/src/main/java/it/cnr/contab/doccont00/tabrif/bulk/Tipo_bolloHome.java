package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_bolloHome extends BulkHome {
public Tipo_bolloHome(java.sql.Connection conn) {
	super(Tipo_bolloBulk.class,conn);
}
public Tipo_bolloHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_bolloBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti i tipi di bollo quelli 
 * che non sono stati (logicamente) cancellati.
 * @return SQLBuilder 
 */
public SQLBuilder createSQLBuilder( )	
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "fl_cancellato", SQLBuilder.EQUALS, new Boolean(false) );
	return sql; 
}
public Tipo_bolloBulk findTipoBolloDefault( String ti_entrata_spesa ) throws PersistencyException, it.cnr.jada.comp.ApplicationException	
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND", "fl_default", sql.EQUALS, new Boolean(true));
	if ( ti_entrata_spesa.equals( Tipo_bolloBulk.TIPO_ENTRATA ))
		sql.addClause( "AND", "ti_entrata_spesa", sql.NOT_EQUALS, Tipo_bolloBulk.TIPO_SPESA);
	else 	if ( ti_entrata_spesa.equals( Tipo_bolloBulk.TIPO_SPESA ))
		sql.addClause( "AND", "ti_entrata_spesa", sql.NOT_EQUALS, Tipo_bolloBulk.TIPO_ENTRATA);	
	java.util.List result = fetchAll( sql );
	if ( result.size() == 0 )
		throw new it.cnr.jada.comp.ApplicationException( "Non esiste il tipo bollo di default" );
	return (Tipo_bolloBulk) result.get(0); 
}
public Tipo_bolloBulk findTipoBolloEsente( ) throws PersistencyException, it.cnr.jada.comp.ApplicationException	
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "classe_tipo_bollo", SQLBuilder.EQUALS, Tipo_bolloBulk.CLASSE_ESENTE );
	java.util.List result = fetchAll( sql );
	if ( result.size() == 0 )
		throw new it.cnr.jada.comp.ApplicationException( "Non esiste il tipo bollo con classe ESENTE" );
	return (Tipo_bolloBulk) result.get(0); 
}
}
