package it.cnr.contab.config00.comp;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.utenze00.bulk.LockedObjectBulk;
import it.cnr.contab.utenze00.bulk.LockedObjectHome;
import it.cnr.contab.utenze00.bulk.SessionTraceBulk;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

public class LockObjectComponent extends CRUDComponent {

	public OggettoBulk riempiListaUtenti(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException{
		LockedObjectBulk lockedObject = (LockedObjectBulk)oggettoBulk;
		try {
			lockedObject.setUtenti(new BulkList(((LockedObjectHome)getHome(userContext, LockedObjectBulk.class)).findUtenti(userContext, lockedObject)));
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return lockedObject;
	}
	
	public OggettoBulk riempiListaOggetti(UserContext userContext, OggettoBulk oggettoBulk) throws ComponentException{
		LockedObjectBulk lockedObject = (LockedObjectBulk)oggettoBulk;
		try {
			lockedObject.setOggetti(new BulkList(((LockedObjectHome)getHome(userContext, LockedObjectBulk.class)).findOggetti(userContext, lockedObject)));
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		return lockedObject;
	}	
	@SuppressWarnings("unchecked")
	public void terminaSessioni(UserContext userContext, BulkList utentiSelezionati) throws ComponentException{
		try {
			LockedObjectHome lockedObjectHome = (LockedObjectHome)getHome(userContext, LockedObjectBulk.class);
			for (Iterator<UtenteBulk> iterator = utentiSelezionati.iterator(); iterator.hasNext();) {
				UtenteBulk utente = iterator.next();
				List<SessionTraceBulk> lockedObjects = lockedObjectHome.findLockedObjectsForUser(utente);
				for (Iterator<SessionTraceBulk> iterator2 = lockedObjects.iterator(); iterator2.hasNext();) {
					SessionTraceBulk result = iterator2.next();
					URL url = new URL(result.getServer_url());
					try{
						url.openConnection().getContent();
					}catch(Exception e){
					}
				}
			}
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (MalformedURLException e) {
			throw new ComponentException(e);
		}
	}	
}
