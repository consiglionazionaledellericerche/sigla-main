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

package it.cnr.contab.docamm00.actions;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
/**
 * CRUDCategoriaGruppoInventAction
 * Creation date: (9/5/2001 5:02:18 PM)
 * @author: Ardire Alfonso
 */
import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.docamm00.ejb.*;
import it.cnr.jada.action.*;

public class CRUDCategoriaGruppoInventAction extends it.cnr.jada.util.action.CRUDAction {
/**
 * costruttore
 */
public CRUDCategoriaGruppoInventAction() {
	super();
}

/**
* Controllo effettuato dopo la selezione del nodo padre (categoria)
* Creation date: (27/11/2001 17.04.56)
* @param context it.cnr.jada.action.ActionContext
* @return it.cnr.jada.action.Forward
*/
public it.cnr.jada.action.Forward doBringBackSearchFind_nodo_padre(ActionContext context, 
		Categoria_gruppo_inventBulk cgi, 
		Categoria_gruppo_inventBulk cgiPadre) throws java.rmi.RemoteException {

    //HookForward caller= (HookForward) context.getCaller();
    //if (caller.getParameter("focusedElement") instanceof Categoria_gruppo_inventBulk) {
        //Categoria_gruppo_inventBulk cgi= (Categoria_gruppo_inventBulk) ((CRUDCategoriaGruppoInventBP) getBusinessProcess(context)).getModel();
        //Categoria_gruppo_inventBulk cgiPadre= (Categoria_gruppo_inventBulk) caller.getParameter("focusedElement");

        if (cgiPadre != null) {
	        //controlla che venga selezionata una categoria
            if (cgiPadre.getLivello().equals(new Integer(1))) {
                setErrorMessage(context, "Il nodo padre selezionato non è valido.La struttura può avere solo due livelli!");
                return context.findDefaultForward();
            }
            //Non è possibile selezionare lo stesso elemento come padre
            if (cgiPadre.getCd_categoria_gruppo().equals(cgi.getCd_categoria_gruppo())) {
                setErrorMessage(context, "Non è possibile selezionare lo stesso elemento come padre.");
                return context.findDefaultForward();
            }
            //----cgi.setDs_categoria_gruppo(cgiPadre.getDs_categoria_gruppo() +(cgi.getDs_categoria_gruppo()!=null?" - "+ cgi.getDs_categoria_gruppo():""));
            
            //imposta i flag di gestione ereditati
            cgi.setFl_gestione_inventario(cgiPadre.getFl_gestione_inventario());
            cgi.setFl_gestione_magazzino(cgiPadre.getFl_gestione_magazzino());
            cgi.setFl_ammortamento(cgiPadre.getFl_ammortamento());

            cgi.setNodoPadre(cgiPadre);
            //----cgi.setVoce_f(cgiPadre.getVoce_f());

        }

        return context.findDefaultForward();

        //return super.doBringBackSearchResult(context, (it.cnr.jada.util.action.FormField) caller.getParameter("field"), cgiPadre);
    //} else
        //return super.doBringBackSearchResult(context);
}
/**
* Controllo effettuato sulla ricerca del nodo padre (categoria)
* Creation date: (27/11/2001 17.04.56)
* @param context it.cnr.jada.action.ActionContext
* @return it.cnr.jada.action.Forward
*/
 
public it.cnr.jada.action.Forward doSearchFind_nodo_padre(ActionContext context) {
	try{
		
		CRUDCategoriaGruppoInventBP bp = (CRUDCategoriaGruppoInventBP)getBusinessProcess(context);

		it.cnr.jada.util.RemoteIterator roots = bp.getCategoriaGruppoInventTree(context).getChildren(context,bp.getModel());
		//controllo che ci siano dei padri
		if (roots.countElements()==0){
			it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,roots);
			setErrorMessage(context,"Non ci sono padri!");
			return context.findDefaultForward();
		}else {
			
			it.cnr.jada.util.action.SelezionatoreListaAlberoBP slaBP = (it.cnr.jada.util.action.SelezionatoreListaAlberoBP)context.createBusinessProcess("MioSelezionatoreListaAlberoBP");
			slaBP.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Categoria_gruppo_inventBulk.class));
			slaBP.setRemoteBulkTree(context,bp.getCategoriaGruppoInventTree(context),roots);
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
