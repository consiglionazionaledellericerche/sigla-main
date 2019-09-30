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

package it.cnr.contab.utenze00.bp;

/**
 * Business Process che gestisce l'attività di Gestione Utente Comune e Gestione Template di Utente: in particolare 
 * gestisce i quattro dettagli relativi agli Accessi/Ruoli gia' assegnati all'Utente e agli Accessi/Ruoli ancora disponibili
 *	
 */



import java.rmi.RemoteException;

import it.cnr.contab.utente00.ejb.UtenteComponentSession;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.utenze00.bulk.*;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.ejb.Parametri_enteComponentSession;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;

public class CRUDUtenzaAmministratoreBP extends SimpleCRUDBP {
public CRUDUtenzaAmministratoreBP() throws BusinessProcessException {
	super();
	setTab("tab","tabUtenza");
}
public CRUDUtenzaAmministratoreBP( String function ) throws BusinessProcessException {
	super( function );
	setTab("tab","tabUtenza");
}
/**
 * Esegue il reset della password
 */

public void resetPassword( ActionContext context ) throws it.cnr.jada.action.BusinessProcessException {
	try 
	{
		UtenteBulk utente = (UtenteBulk)getModel();
		setModel(context,((UtenteComponentSession)createComponentSession()).resetPassword(context.getUserContext(),utente));
	} catch(Exception e) {
		throw handleException(e);
	}
}
public Boolean isAutenticazioneLdap(UserContext uc) {
	Parametri_enteBulk par=null;
	try {
		par = ((Parametri_enteComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_enteComponentSession",Parametri_enteComponentSession.class)).getParametriEnte(uc);
	} catch (ComponentException e) {
	} catch (RemoteException e) {
	}
	if (par!=null)
		return par.getFl_autenticazione_ldap();
	return null;
}
}
