package it.cnr.contab.progettiric00.action;

import it.cnr.contab.doccont00.bp.CRUDObbligazioneBP;
import it.cnr.contab.progettiric00.bp.RimodulaProgettiRicercaBP;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoCRUDController;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoPianoEconomicoCRUDController;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoPianoEconomicoVoceBilancioCRUDController;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDController;
import it.cnr.jada.util.action.OptionBP;

/**
 * Azione che gestisce le richieste relative alla Rimodulazione Gestione Progetto Risorse
 * (Progetto)
 */
public class CRUDRimodulaProgettoAction extends CRUDAbstractProgettoAction {

    public CRUDRimodulaProgettoAction() {
        super();
    }

	public Forward doBringBackSearchFind_progetto(ActionContext context, Progetto_rimodulazioneBulk rimodulazione, ProgettoBulk progetto) {
		try {
			fillModel(context);
			if (progetto!=null) {
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
				if (bp.isSearching())
					rimodulazione.setProgetto(progetto);
				else {
					bp.initializeProgetto(context, rimodulazione, progetto);
					bp.setDirty(true);
				}
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}
	
	public Forward doUndoRemoveFromCRUD(ActionContext actioncontext, String s)
	{
		try 
		{
			fillModel( actioncontext );
			CRUDController crudController = getController(actioncontext, s);
			if (crudController instanceof RimodulaProgettoCRUDController) 
				((RimodulaProgettoCRUDController)crudController).undoRemove(actioncontext);
			return actioncontext.findDefaultForward();
		} catch(Exception e) {
			return handleException(actioncontext,e);
		}
	}	
}