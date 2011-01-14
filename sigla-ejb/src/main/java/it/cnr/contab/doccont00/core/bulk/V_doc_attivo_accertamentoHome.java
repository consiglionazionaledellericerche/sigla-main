package it.cnr.contab.doccont00.core.bulk;

import java.util.*;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_doc_attivo_accertamentoHome extends BulkHome {
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_doc_attivo_accertamentoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 */
public V_doc_attivo_accertamentoHome(java.sql.Connection conn) {
	super(V_doc_attivo_accertamentoBulk.class,conn);
}
/**
 * <!-- @TODO: da completare -->
 * Costruisce un V_doc_attivo_accertamentoHome
 *
 * @param conn	La java.sql.Connection su cui vengono effettuate le operazione di persistenza
 * @param persistentCache	La PersistentCache in cui vengono cachati gli oggetti persistenti caricati da questo Home
 */
public V_doc_attivo_accertamentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(V_doc_attivo_accertamentoBulk.class,conn,persistentCache);
}
/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param bulk	
 * @return 
 * @throws PersistencyException	
 */
public java.util.Hashtable loadTipoDocumentoKeys( V_doc_attivo_accertamentoBulk bulk ) throws PersistencyException
{
	SQLBuilder sql = getHomeCache().getHome( Tipo_documento_ammBulk.class ).createSQLBuilder();
	sql.addClause( "AND", "ti_entrata_spesa", sql.EQUALS, "E" );
	List result = getHomeCache().getHome( Tipo_documento_ammBulk.class ).fetchAll( sql );
	Hashtable ht = new Hashtable();
	Tipo_documento_ammBulk tipo;
	for (Iterator i = result.iterator(); i.hasNext(); )
	{
		tipo = (Tipo_documento_ammBulk) i.next();
		ht.put( tipo.getCd_tipo_documento_amm(), tipo.getDs_tipo_documento_amm());
	}	
	return ht;
}
}
