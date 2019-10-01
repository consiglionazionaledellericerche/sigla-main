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

package it.cnr.contab.inventario00.actions;

import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.inventario00.bp.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (27/11/2001 12.21.44)
 * @author: Roberto Fantino
 */
public class CRUDUbicazioneAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * CRUDUbicazioneAction constructor comment.
 */
public CRUDUbicazioneAction() {
	super();
}
/**
  *  Gestisce il comando azzeramento del searchtool del Nodo Padre.
  * Quando si azzera il searchTool per la ricerca dell'Ubicazione padre,
  * ripulisce la descrizione dell'Ubicazione in canna.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param ubicazione il <code>Ubicazione_beneBulk</code> l'Ubicazione che sui sta creando.
  *
  * @return forward <code>Forward</code>
**/ 
public Forward doBlankSearchFind_nodo_padre(ActionContext context, 
	Ubicazione_beneBulk ubicazione) 
	throws java.rmi.RemoteException {
		
	try {
		
		ubicazione.setNodoPadre(new Ubicazione_beneBulk());
		
		return context.findDefaultForward();
		
	} catch(Exception e) {
		return handleException(context,e);
	}
}
/**
  *  E' stata generata la richiesta di cercare una Ubicazione che sia padre della ubicazione 
  *	che si sta creando.
  *	Il metodo antepone alla descrizione specificata dall'utente, quella dell'Ubicazione selezionata
  *	come padre.
  *	In caso di modifica di una ubicazione esistente sul DB, il sistema controlla che l'ubicazione
  *	selezionata dall'utente non sia la stessa che sta modificando.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
 
public Forward doBringBackSearchFind_nodo_padre(ActionContext context, Ubicazione_beneBulk ubicazione, Ubicazione_beneBulk ubicazione_padre) throws java.rmi.RemoteException {

	if(ubicazione_padre != null){
		// L'utente ha selezionato come Ubicazione padre l'ubicazione che sta modificando
		if (ubicazione_padre.getCd_ubicazione().equals(ubicazione.getCd_ubicazione())){
			setErrorMessage(context,"Attenzione: non è possibile selezionare come padre l'ubicazione stessa");
			return context.findDefaultForward();
		}
		ubicazione.setDs_ubicazione_bene(ubicazione_padre.getDs_ubicazione_bene()+" - "+ubicazione.getDs_ubicazione_bene());
		ubicazione.setNodoPadre(ubicazione_padre);
	}

	return context.findDefaultForward();
}
/**
  *  E' stata generata la richiesta di cercare una Ubicazione che sia padre della ubicazione 
  *	che si sta creando.
  *	Il metodo controlla se l'utente ha indicato nel campo codice dell'Ubicazione padre un 
  *	valore: in caso affermativo, esegue una ricerca mirata per trovare esattamente il codice 
  *	indicato; altrimenti, apre un <code>SelezionatoreListaAlberoBP</code> che permette all'utente 
  *	di cercare il nodo padre scorrendo le ubicazioni secondo i vari livelli.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  *
  * @return forward <code>Forward</code>
**/
public it.cnr.jada.action.Forward doSearchFind_nodo_padre(ActionContext context) {

	try{
		
		CRUDUbicazioneBP bp = (CRUDUbicazioneBP)getBusinessProcess(context);
		Ubicazione_beneBulk ubicazione = (Ubicazione_beneBulk)bp.getModel();
		
		String cd = null;

		if (ubicazione.getNodoPadre() != null)
			cd = ubicazione.getNodoPadre().getCd_ubicazione();
			
		if (cd != null){
			if (cd.equals(ubicazione.getCd_ubicazione())){
				return handleException(context, new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile indicare come nodo padre l'ubicazione corrente"));
			} else{
				// L'utente ha indicato un codice da cercare: esegue una ricerca mirata.
				return search(context, getFormField(context, "main.find_nodo_padre"),null);
			}
		}
		
		it.cnr.jada.util.RemoteIterator roots = bp.getUbicazioniTree(context).getChildren(context,null);
		// Non ci sono Ubicazioni disponibili ad essere utiilzzate come nodo padre
		if (roots.countElements()==0){
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context, roots);
			setErrorMessage(context,"Attenzione: non sono state trovate Ubicazioni disponibili");
			return context.findDefaultForward();
		}else {
			// Apre un Selezionatore ad Albero per cercare le Ubicazioni selezionando i vari livelli
			SelezionatoreListaAlberoBP slaBP = (SelezionatoreListaAlberoBP)context.createBusinessProcess("MioSelezionatoreListaAlberoBP");
			slaBP.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Ubicazione_beneBulk.class));
			slaBP.setRemoteBulkTree(context,bp.getUbicazioniTree(context),roots);
			HookForward hook = (HookForward)context.addHookForward("seleziona",this,"doBringBackSearchResult");
			hook.addParameter("field",getFormField(context,"main.find_nodo_padre"));
			context.addBusinessProcess(slaBP);
			return slaBP;
		}
	} catch(Throwable e){
		return handleException(context, e);
	}
}

}
