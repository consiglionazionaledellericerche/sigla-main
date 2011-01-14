/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 13/09/2007
 */
package it.cnr.contab.anagraf00.tabrif.bulk;
import java.sql.Connection;
import java.sql.Timestamp;

import it.cnr.contab.inventario01.bulk.Inventario_beni_apgBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
public class EcfHome extends BulkHome {
	public EcfHome(Connection conn) {
		super(EcfBulk.class, conn);
	}
	public EcfHome(Connection conn, PersistentCache persistentCache) {
		super(EcfBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk)throws PersistencyException, it.cnr.jada.comp.ComponentException {
		EcfBulk ecf= (EcfBulk)oggettobulk;
		try {
		((EcfBulk)oggettobulk).setProg(
		new Long(
		((Long)findAndLockMax( ecf, "prog", new Long(0) )).longValue()+1));
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
		 throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
		} 
		super.initializePrimaryKeyForInsert(usercontext, ecf);
		}
	public Long getMaxProg_estrazione(UserContext usercontext)throws PersistencyException{
		  
		Long Max_prog=null;
		EcfBulk bulk =new EcfBulk();	
		Max_prog = ((Long) findMax(bulk,"prog_estrazione",new Long(0))).longValue()+1;
		
		return Max_prog;
	}
}