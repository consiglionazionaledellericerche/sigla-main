/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import java.rmi.RemoteException;
import java.sql.Connection;

import javax.ejb.EJBException;

import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneHome;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class CausaleSpesaOrdHome extends BulkHome {
	public CausaleSpesaOrdHome(Connection conn) {
		super(CausaleSpesaOrdBulk.class, conn);
	}
	public CausaleSpesaOrdHome(Connection conn, PersistentCache persistentCache) {
		super(CausaleSpesaOrdBulk.class, conn, persistentCache);
	}	
}