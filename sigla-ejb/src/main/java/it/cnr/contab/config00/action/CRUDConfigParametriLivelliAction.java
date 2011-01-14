/*
 * Created on Sep 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.action;

import it.cnr.contab.config00.bp.CRUDConfigParametriLivelliBP;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.CRUDAction;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDConfigParametriLivelliAction extends CRUDAction {

	/**
	 * Construtor from superclass
	 *
	 */
	public CRUDConfigParametriLivelliAction() {
		super();
	}
	
	/**
	 * Viene richiamato nel momento in cui viene cambiato il livello delle Entrate
	 */
	public Forward doCambiaLivelliEntrata(ActionContext context) {
		try {	
			CRUDConfigParametriLivelliBP bp = (CRUDConfigParametriLivelliBP)getBusinessProcess(context);
			Parametri_livelliBulk parLiv = (Parametri_livelliBulk)bp.getModel();
			Integer livOld = parLiv.getLivelli_entrata();
			try {
				fillModel( context );
				if (!bp.isSearching())
					parLiv.validaLivelliValorizzati();

				return context.findDefaultForward();
			} catch(ValidationException e) {
				parLiv.setLivelli_entrata(livOld);
				bp.setModel(context,parLiv);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}

	/**
	 * Viene richiamato nel momento in cui viene cambiato il livello delle Spese
	 */
	public Forward doCambiaLivelliSpesa(ActionContext context) {
		try {	
			CRUDConfigParametriLivelliBP bp = (CRUDConfigParametriLivelliBP)getBusinessProcess(context);
			Parametri_livelliBulk parLiv = (Parametri_livelliBulk)bp.getModel();
			Integer livOld = parLiv.getLivelli_spesa();
			try {
				fillModel( context );
				if (!bp.isSearching())
					parLiv.validaLivelliValorizzati();

				return context.findDefaultForward();
			} catch(ValidationException e) {
				parLiv.setLivelli_spesa(livOld);
				bp.setModel(context,parLiv);
				throw e;
			}
		} catch(Throwable e) {
			return handleException(context, e);
		}
	}
}
