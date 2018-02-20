package it.cnr.contab.messaggio00.bulk;

import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class MessaggioHome extends BulkHome {
public MessaggioHome(java.sql.Connection conn) {
	super(MessaggioBulk.class,conn);
}
public MessaggioHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(MessaggioBulk.class,conn,persistentCache);
}
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException,it.cnr.jada.comp.ComponentException {
	MessaggioBulk messaggio = (MessaggioBulk)bulk;
	if (messaggio.getPg_messaggio() == null)
		messaggio.setPg_messaggio(new Long(this.fetchNextSequenceValue(userContext,"CNRSEQ00_PG_MESSAGGIO").longValue()));
}
/**
  *	Ritorna TRUE se il messaggio <messaggio> è stato visionato dall'utente,
  * collegato ed individuato tramite lo usercontext <usercontext>, 
  * FALSE altrimenti
  *  
  *  Parametri:
  *	 - usercontext
  *  - messaggio
  *
**/
public boolean isMessaggioVisionato(UserContext usercontext, MessaggioBulk messaggio) throws java.sql.SQLException{

	PersistentHome messaggio_visionatoHome = getHomeCache().getHome( Messaggio_visionatoBulk.class);
	SQLBuilder sql = messaggio_visionatoHome.createSQLBuilder();
	sql.addSQLClause("AND","PG_MESSAGGIO",sql.EQUALS,messaggio.getPg_messaggio());
	sql.addSQLClause("AND","CD_UTENTE",sql.EQUALS,CNRUserContext.getUser(usercontext));

	return sql.executeExistsQuery(getConnection());
}
public SQLBuilder selectJobsToDelete() {

	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("PARAMETRI_ENTE");
	sql.addSQLClause("AND", "PARAMETRI_ENTE.ATTIVO", SQLBuilder.EQUALS, "Y");
	sql.addSQLClause("AND", "TRUNC(SYSDATE - MESSAGGIO.DUVA) > Nvl(PARAMETRI_ENTE.CANCELLA_STAMPE,30)");		
	return sql;
}
public void deleteRiga(MessaggioBulk bulk) throws PersistencyException{
	delete(bulk, null);
}
}
