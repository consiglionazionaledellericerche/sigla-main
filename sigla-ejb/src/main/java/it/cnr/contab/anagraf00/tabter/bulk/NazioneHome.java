package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class NazioneHome extends BulkHome {
public NazioneHome(java.sql.Connection conn) {
	super(NazioneBulk.class,conn);
}
public NazioneHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(NazioneBulk.class,conn,persistentCache);
}
/**
 * Viene letta da db lanazione corrispondente al tipo passato
 * Utile solo per ricercare la Nazione Italia e la Nazione Indifferente
 * Inutile negli altri casi
*/
public NazioneBulk findNazione(String tipoNazione) throws PersistencyException{
	
	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "ti_nazione", sql.EQUALS, tipoNazione);

	NazioneBulk nazione = null;

	Broker broker = createBroker(sql);
	if (broker.next())
		nazione = (NazioneBulk)fetch(broker);
	broker.close();

	return nazione;
}
/**
 * Imposta il pg_nazione di un oggetto <code>NazioneBulk</code>.
 *
**/
public void initializePrimaryKeyForInsert(UserContext userContext, OggettoBulk nazione) throws it.cnr.jada.persistency.PersistencyException {

	try {
		Long maxValue = (Long)findAndLockMax(nazione,"pg_nazione",new Long(0));
		Long x = new Long(maxValue.longValue()+1);

		((NazioneBulk)nazione).setPg_nazione(x);

	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new it.cnr.jada.persistency.PersistencyException(e);
	}
}
}
