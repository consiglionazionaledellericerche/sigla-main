package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoHome;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoHome;
import it.cnr.jada.persistency.*;

import java.util.Collection;
/**
 * Insert the type's description here.
 * Creation date: (26/07/2004 16.16.03)
 * @author: Gennaro Borriello
 */
public class Trasferimento_inventarioHome extends Buono_carico_scaricoHome {
/**
 * Trasferimento_inventarioHome constructor comment.
 * @param conn java.sql.Connection
 */
public Trasferimento_inventarioHome(java.sql.Connection conn) {
	super(conn);
}
/**
 * Trasferimento_inventarioHome constructor comment.
 * @param conn java.sql.Connection
 * @param persistentCache it.cnr.jada.persistency.PersistentCache
 */
public Trasferimento_inventarioHome(java.sql.Connection conn, it.cnr.jada.persistency.PersistentCache persistentCache) {
	super(conn, persistentCache);
}
/**
 *
*/ 
public Collection findTipoMovimentiCarico(Trasferimento_inventarioBulk bulk, Tipo_carico_scaricoHome home, Tipo_carico_scaricoBulk clause) throws PersistencyException, IntrospectionException {

	return home.findTipoMovimentiPerTrasferimento(bulk, Tipo_carico_scaricoBulk.TIPO_CARICO);

}
/**
 *
*/ 
public Collection findTipoMovimentiScarico(Trasferimento_inventarioBulk bulk, Tipo_carico_scaricoHome home, Tipo_carico_scaricoBulk clause) throws PersistencyException, IntrospectionException {

	return home.findTipoMovimentiPerTrasferimento(bulk, Tipo_carico_scaricoBulk.TIPO_SCARICO);
}
}
