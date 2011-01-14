package it.cnr.contab.config00.ejb;

import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;

import javax.ejb.Remote;
@Remote
public interface LockObjectSession extends it.cnr.jada.ejb.CRUDComponentSession{

	public OggettoBulk riempiListaUtenti(UserContext userContext, OggettoBulk model) throws it.cnr.jada.comp.ComponentException;
	public OggettoBulk riempiListaOggetti(UserContext userContext, OggettoBulk model) throws it.cnr.jada.comp.ComponentException;
	public void terminaSessioni(UserContext userContext, BulkList utentiSelezionati) throws it.cnr.jada.comp.ComponentException;

}
