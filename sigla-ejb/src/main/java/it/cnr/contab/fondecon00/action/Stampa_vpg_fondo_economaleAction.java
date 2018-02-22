package it.cnr.contab.fondecon00.action;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.fondecon00.bp.FondoEconomaleBP;
import it.cnr.contab.fondecon00.core.bulk.*;
import it.cnr.contab.reports.bp.ParametricPrintBP;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (13/03/2003 11.06.11)
 * @author: Gennaro Borriello
 */
public class Stampa_vpg_fondo_economaleAction extends it.cnr.contab.reports.action.ParametricPrintAction {
/**
 * Stampa_vpg_fondo_economaleAction constructor comment.
 */
public Stampa_vpg_fondo_economaleAction() {
	super();
}
/**
 * <!-- @TODO: da completare -->
 * Gestisce una richiesta di azzeramento del searchtool "scadenza_ricerca"
 *
 * @param context	L'ActionContext della richiesta
 * @param fondo	L'OggettoBulk padre del searchtool
 * @return Il Forward alla pagina di risposta
 * @throws RemoteException	Se si verifica qualche eccezione di sistema per cui non è possibile effettuare l'operazione
 */
public Forward doFreeSearchFondoForPrint(ActionContext context)	throws java.rmi.RemoteException {
	
	
	ParametricPrintBP bp = (ParametricPrintBP)context.getBusinessProcess();
	Stampa_vpg_fondo_economaleBulk stampa = (Stampa_vpg_fondo_economaleBulk)bp.getModel();
	
	it.cnr.jada.util.action.FormField field = getFormField(context,"main.fondoForPrint");


	TerzoBulk economo = new TerzoBulk();
	economo.setAnagrafico(new AnagraficoBulk());
	stampa.getFondoForPrint().setEconomo(economo);

	stampa.getFondoForPrint().setMandato(new it.cnr.contab.doccont00.core.bulk.MandatoIBulk());
	
	return freeSearch(context,field, stampa.getFondoForPrint());
}
}
