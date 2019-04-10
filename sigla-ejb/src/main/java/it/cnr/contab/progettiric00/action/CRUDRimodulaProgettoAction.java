package it.cnr.contab.progettiric00.action;

import java.util.Optional;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.progettiric00.bp.RimodulaProgettiRicercaBP;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoPianoEconomicoCRUDController;
import it.cnr.contab.progettiric00.bp.RimodulaProgettoPianoEconomicoVoceBilancioCRUDController;
import it.cnr.contab.progettiric00.bp.TestataProgettiRicercaBP;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.Forward;
import it.cnr.jada.util.action.CRUDController;

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
			if (Optional.ofNullable(progetto).isPresent()) {
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
				rimodulazione.setProgetto(progetto);
				if (!bp.isSearching()) {
					bp.initializeProgetto(context, rimodulazione);
					bp.setDirty(true);
				}
			}
			return context.findDefaultForward();
		}catch (Exception ex) {
			return handleException(context, ex);
		}
	}

    public it.cnr.jada.action.Forward doBringBackSearchVoce_piano(ActionContext context, Progetto_piano_economicoBulk progettoPiaeco, Voce_piano_economico_prgBulk vocePiaeco) throws java.rmi.RemoteException {
    	try {
    		RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP) getBusinessProcess(context);
	        progettoPiaeco.setVoce_piano_economico(vocePiaeco);
	        bp.caricaVociPianoEconomicoAssociate(context,progettoPiaeco);
	        return context.findDefaultForward();
        } catch (Throwable e) {
            return handleException(context, e);
        }	        
    }
    
	public Forward doBringBackSearchElemento_voce(ActionContext context, Ass_progetto_piaeco_voceBulk assProgettoPiaecoVoce, Elemento_voceBulk elementoVoce) {
		try {
			fillModel(context);
			if (Optional.ofNullable(elementoVoce).isPresent()) {
				assProgettoPiaecoVoce.setElemento_voce(elementoVoce);
				RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)getBusinessProcess(context);
				bp.initializeAssProgettoPiaecoVoce(context, assProgettoPiaecoVoce);
				bp.setDirty(true);
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
			if (crudController instanceof RimodulaProgettoPianoEconomicoCRUDController) 
				((RimodulaProgettoPianoEconomicoCRUDController)crudController).undoRemove(actioncontext);
			else if (crudController instanceof RimodulaProgettoPianoEconomicoVoceBilancioCRUDController) 
				((RimodulaProgettoPianoEconomicoVoceBilancioCRUDController)crudController).undoRemove(actioncontext);
			return actioncontext.findDefaultForward();
		} catch(Exception e) {
			return handleException(actioncontext,e);
		}
	}	
}