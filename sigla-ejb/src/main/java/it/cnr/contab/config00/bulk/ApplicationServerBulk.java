/*
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 31/07/2008
 */
package it.cnr.contab.config00.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class ApplicationServerBulk extends ApplicationServerBase {
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: APPLICATION_SERVER
	 **/
	public ApplicationServerBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: APPLICATION_SERVER
	 **/
	public ApplicationServerBulk(java.lang.String hostname, java.lang.String session_id) {
		super(hostname, session_id);
	}
	public OggettoBulk initializeForInsert(CRUDBP bp, ActionContext context) {
		setAttivo(new Boolean(false));
		setLogin(new Boolean(false));
		return this;
	}
}