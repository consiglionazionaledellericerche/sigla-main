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

import it.cnr.contab.inventario00.docs.bulk.Trasferimento_inventarioBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.bp.CRUDTrasferimentoInventarioBP;
import it.cnr.contab.inventario01.actions.CRUDScaricoBuonoAction;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
/**
 * Insert the type's description here.
 * Creation date: (26/07/2004 15.14.25)
 * @author: Gennaro Borriello
 */
public class CRUDTrasferimentoInventarioAction extends CRUDScaricoBuonoAction {
/**
 * TrasferimentoInventarioAction constructor comment.
 */
public CRUDTrasferimentoInventarioAction() {
	super();
}
/**
 * Gestisce la ricerca di una Categoria
 */

public it.cnr.jada.action.Forward doBlankSearchFindUoDestinazione(it.cnr.jada.action.ActionContext context, it.cnr.contab.inventario00.docs.bulk.Trasferimento_inventarioBulk bulk) {

    try {
        fillModel(context);
        it.cnr.jada.util.action.FormField field = getFormField(context, "main.findUoDestinazione");        
		
        bulk.setUo_destinazione(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk());

        bulk.setInventario_destinazione(null);
        blankSearch(context, field, createEmptyModelForSearchTool(context, field));

        return context.findDefaultForward();
    } catch (Throwable t) {
        return handleException(context, t);
    }
}
/**
  * Gestisce il risultato di una ricerca sul Nuovo Bene Padre:
  *	   in una operazione di trasferimento Intra-Inventario, si è selezionato
  *	  un nuovo bene padre per un bene.
  *	  Se il bene che si sta trasferendo è un bene principale con accessori, l'indicazione
  *	  del nuovo bene padre viene trasferita anche ai beni accessori.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param bene il <code>Inventario_beniBulk</code> bene che deve essere trasferito
  * @param nuovo_padre il <code>Inventario_beniBulk</code> nuovo bene padre selezionat dall'utente.
  *
  * @return forward <code>Forward</code>
**/
public Forward doBringBackSearchFind_nuovo_bene_padre(
	ActionContext context, 
	Inventario_beniBulk bene,
	Inventario_beniBulk nuovo_padre) {
	try {
		fillModel(context);
		if (nuovo_padre!=null){
			CRUDTrasferimentoInventarioBP bp = (CRUDTrasferimentoInventarioBP)getBusinessProcess(context);
			bp.checkNuovoBenePadreAlreadySelected(context, nuovo_padre);
			bene.setNuovo_bene_padre(nuovo_padre);
		}
		return context.findDefaultForward();	
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
/**
  * Gestisce il risultato di una ricerca sulla Unita Organizzativa di Destinazione:
  *	permette di visdualizzare l'Inventario associato alla UO selezionata.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param bulk il <code>Trasferimento_inventarioBulk</code> il bulk che contiene le informazioni relative all'operazione di trasferimento
  * @param uo_dest la <code>Unita_organizzativaBulk</code> uo di destinazione selezionata dall'utente.
  *
  * @return forward <code>Forward</code>
**/
public Forward doBringBackSearchFindUoDestinazione(
	ActionContext context, 
	it.cnr.contab.inventario00.docs.bulk.Trasferimento_inventarioBulk bulk,
	it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo_dest) {

	try {
		fillModel(context);
		if (uo_dest != null){
			it.cnr.jada.UserContext userContext = context.getUserContext();
			CRUDTrasferimentoInventarioBP bp = (CRUDTrasferimentoInventarioBP)getBusinessProcess(context);
			
			it.cnr.contab.inventario00.ejb.IdInventarioComponentSession h = (it.cnr.contab.inventario00.ejb.IdInventarioComponentSession)
														it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
																	"CNRINVENTARIO00_EJB_IdInventarioComponentSession",
																	it.cnr.contab.inventario00.ejb.IdInventarioComponentSession.class);
			it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inv_destinazione = h.findInventarioFor(
																					userContext,
																					uo_dest.getCd_unita_padre(),
																					uo_dest.getCd_unita_organizzativa(),
																					false);

			if (inv_destinazione == null){
				throw new it.cnr.jada.bulk.ValidationException("Attenzione: l'Unità Organizzativa selezionata non è associata ad alcun Inventario.\nOperazione non possibile.");
			}
			else if (!h.isAperto(userContext, inv_destinazione, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext))){
				throw new it.cnr.jada.bulk.ValidationException("Attenzione: l'Inventario associato alla UO selezionata non è aperto.\nOperazione non possibile.");							
			}

			bulk.setUo_destinazione(uo_dest);
			bulk.setInventario_destinazione(inv_destinazione);
		}
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
	
}
/**
 * Trasferimento intra Inventario - Bene Accessorio
 *	L'utente ha selezionato un bene accessorio per una operazione di trasferimento intra
 *	inventario ed ha selezionato il flag "Trasferisci come bene principale".
 *	Viene disabilitato il search-tool per la ricerca di un nuovo bene padre.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 *
 * @return Forward
**/
public Forward doOnFlTrasfComePrincChange(ActionContext context) throws it.cnr.jada.comp.ApplicationException{

	
	CRUDTrasferimentoInventarioBP bp = (CRUDTrasferimentoInventarioBP)getBusinessProcess(context);	
	Inventario_beniBulk bene = (Inventario_beniBulk)bp.getDettController().getModel();
	
	try {		
		fillModel(context);
		if (bene.getFl_trasf_come_principale().booleanValue()){
			bene.setNuovo_bene_padre(null);
		} else {
			bene.setNuovo_bene_padre(new Inventario_beniBulk());
		}
		
		return context.findDefaultForward();
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
	public Forward doOnData_registrazioneChange(ActionContext context)  {
		try{
			CRUDTrasferimentoInventarioBP bp = (CRUDTrasferimentoInventarioBP)context.getBusinessProcess();
			bp.getDettController().removeAll(context);
			fillModel(context);
		
			Buono_carico_scaricoBulk model=(Buono_carico_scaricoBulk)bp.getModel();
			bp.setModel(context,model);
			return context.findDefaultForward();
		}catch (Throwable t) {
		    return handleException(context, t);
		}
	}

/**
 * Trasferimento extra Inventario
 *	L'utente ha selezionato il flag "Trasferisci tutti i beni", 
 *	 per una operazione di trasferimento ad un altro Inventario.
 *	Vengono cancellati tutti i beni eventualmente selezionati in precedenza e viene disabilitata
 *	 la possibilità di aggiungere beni.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 *
 * @return Forward
**/
public Forward doOnFlTrasferisciTuttiChange(ActionContext context)
    throws it.cnr.jada.comp.ApplicationException {

    CRUDTrasferimentoInventarioBP bp = (CRUDTrasferimentoInventarioBP) getBusinessProcess(context);
    Trasferimento_inventarioBulk buonoT = (Trasferimento_inventarioBulk) bp.getModel();
    try {
        fillModel(context);
 	if (buonoT.getFl_scarica_tutti().booleanValue()) 
		return openConfirm(context,"Attenzione si stanno scaricando tutti i beni dell'inventario. Si vuole continuare?",it.cnr.jada.util.action.OptionBP.CONFIRM_YES_NO,"doConfermaScarico");
	else {
        bp.getDettController().setEnabled(true);
    }
        return context.findDefaultForward();
    } catch (Throwable e) {
        return handleException(context, e);
    }
}
public Forward doConfermaScarico(ActionContext context,int option) {
	CRUDTrasferimentoInventarioBP bp = (CRUDTrasferimentoInventarioBP) getBusinessProcess(context);
	Trasferimento_inventarioBulk buonoT = (Trasferimento_inventarioBulk) bp.getModel();
	try {
		if (option == it.cnr.jada.util.action.OptionBP.YES_BUTTON) {
			return basicDoConfermaScarico(context);
		}else
		buonoT.setFl_scarica_tutti(new Boolean(false));
	} catch (Throwable t) {
		return handleException(context, t);
	}
	return context.findDefaultForward();
}
public Forward basicDoConfermaScarico(ActionContext context) 
	{

	CRUDTrasferimentoInventarioBP bp = (CRUDTrasferimentoInventarioBP) getBusinessProcess(context);
	Trasferimento_inventarioBulk buonoT = (Trasferimento_inventarioBulk) bp.getModel();
	try {
		   fillModel(context);
	   	   doRemoveAllFromCRUD(context, "main.DettController");
		   bp.getDettController().setEnabled(false);
		
		   return context.findDefaultForward();
	   } catch (Throwable e) {
		   return handleException(context, e);
	   }
}	
/**
 * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
 */
public Forward doTab(ActionContext context,String tabName,String pageName) {
	
	CRUDTrasferimentoInventarioBP bp = (CRUDTrasferimentoInventarioBP)getBusinessProcess(context);
	Trasferimento_inventarioBulk buonoT =(Trasferimento_inventarioBulk)bp.getModel();
	try	{
		fillModel( context );
		if ("tabTrasferimentoInventarioTestata".equalsIgnoreCase(bp.getTab(tabName))) {
			if (buonoT.isTrasferimentoExtraInv() && (buonoT.getUo_destinazione() == null || buonoT.getUo_destinazione().getCd_unita_organizzativa() == null)){
	        	throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare una UO Destinazione.");
			}
			if (buonoT.isTrasferimentoExtraInv()  && buonoT.getInventario_destinazione() == null)			
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare inventario destinazione");	
		
		}

		return super.doTab( context, tabName, pageName );
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
