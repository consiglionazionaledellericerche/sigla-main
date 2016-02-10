package it.cnr.contab.config00.action;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;

/**
 * Azione che gestisce le richieste relative alla Gestione Unita' Organizzativa
 */
public class CRUDConfigPdCCNREntrateCapitoloAction extends it.cnr.jada.util.action.CRUDAction {
	public CRUDConfigPdCCNREntrateCapitoloAction() {
		super();
	}
	
	public Forward doBringBackSearchFind_elemento_padre(ActionContext context, Elemento_voceBulk elemento_voce, Elemento_voceBulk elemento_padre)	throws java.rmi.RemoteException {
		if (elemento_padre != null) {
			elemento_voce.setElemento_padre(elemento_padre);
			elemento_voce.setFl_partita_giro(elemento_padre.getFl_partita_giro());
		}
		return context.findDefaultForward();	
	}

}
