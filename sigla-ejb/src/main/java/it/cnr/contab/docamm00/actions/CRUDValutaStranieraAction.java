package it.cnr.contab.docamm00.actions;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
/**
 * Insert the type's description here.
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.BulkBP;


public class CRUDValutaStranieraAction extends it.cnr.jada.util.action.CRUDAction {
	/**
 * CRUDFatturaAttivaAction constructor comment.
 */
public CRUDValutaStranieraAction() {
	super();
}

/**
 * Gestisce un HookForward di ritorno da un risultato di una ricerca (SelezionatoreBP)
 */
public Forward doRiportaSelezione(ActionContext context)
    throws java.rmi.RemoteException {

	return super.doRiportaSelezione(context);
	
    //try {
        //HookForward caller = (HookForward) context.getCaller();
        //DivisaBulk selezione = (DivisaBulk) caller.getParameter("focusedElement");
        //if (selezione != null) {
            //String cd_euro = null;
            //try {
                //cd_euro =
                    //(
                        //(it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it
                            //.ibm
                            //.bframe
                            //.util
                            //.ejb
                            //.EJBCommonServices
                            //.createEJB(
                                //"CNRCONFIG00_EJB_Configurazione_cnrComponentSession",
                                //it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class))
                            //.getVal01(
                        //context.getUserContext(),
                        //new Integer(0),
                        //"*",
                        //"CD_DIVISA",
                        //"EURO");
            //} catch (javax.ejb.EJBException e) {
                //return handleException(context,e);
            //} catch (java.rmi.RemoteException e) {
                //return handleException(context,e);
            //}
            //if (selezione.getCd_divisa().equals(cd_euro))
                //throw new it.cnr.jada.comp.ApplicationException(
                    //"Non e' possibile modificare la valuta di default");
            //getBusinessProcess(context).edit(context, selezione);
         //}
        //return context.findDefaultForward();
    //} catch (Exception e) {
        //return handleException(context, e);
    //}

}
}