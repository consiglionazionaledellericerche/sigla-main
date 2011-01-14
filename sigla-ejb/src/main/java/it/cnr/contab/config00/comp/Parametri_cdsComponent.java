/*
 * Created on Feb 23, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.cnr.contab.config00.comp;

import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;

/**
 * @author rpagano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Parametri_cdsComponent extends CRUDComponent {
	public Parametri_cdsBulk getParametriCds(UserContext userContext, String cd_cds, Integer esercizio) throws ComponentException{
		try{
			Parametri_cdsHome home = (Parametri_cdsHome)getHome(userContext, Parametri_cdsBulk.class);
			Parametri_cdsBulk bulk = (Parametri_cdsBulk)home.findByPrimaryKey(new Parametri_cdsBulk(cd_cds, esercizio));
			getHomeCache(userContext).fetchAll(userContext,home);
			return bulk;
		}catch(it.cnr.jada.persistency.PersistencyException ex){
			throw handleException(ex);
		}
	}
}