/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
