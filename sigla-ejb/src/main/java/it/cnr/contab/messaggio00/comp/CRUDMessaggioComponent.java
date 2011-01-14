package it.cnr.contab.messaggio00.comp;

import java.sql.SQLException;

import it.cnr.contab.messaggio00.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

/**
 * Insert the type's description here.
 * Creation date: (04/09/2002 13:17:51)
 * @author: CNRADM
 */
public class CRUDMessaggioComponent extends it.cnr.jada.comp.CRUDComponent implements ICRUDMessaggioMgr {
/**
 * CRUDMessaggioComponent constructor comment.
 */
public CRUDMessaggioComponent() {
	super();
}
public void setMessaggioVisionato(UserContext userContext, MessaggioBulk messaggio) throws ComponentException {
	try {
		String user = it.cnr.contab.utenze00.bp.CNRUserContext.getUser(userContext);
		try {
			Messaggio_visionatoBulk messaggio_visionato = new Messaggio_visionatoBulk(user,messaggio.getPg_messaggio());
			messaggio_visionato.setUser(user);
			insertBulk(userContext,messaggio_visionato);
			} catch(it.cnr.jada.persistency.sql.DuplicateKeyException e) {
		}
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
public boolean isMessaggioVisionato(UserContext userContext, MessaggioBulk messaggio) throws ComponentException{
	try{
		MessaggioHome home = (MessaggioHome)getHome(userContext, messaggio);
		return home.isMessaggioVisionato(userContext, messaggio);
	} catch (SQLException e) {
		throw handleException(e);
	}
}
}
