package it.cnr.contab.progettiric00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;

public class ProgettoPianoEconomicoVoceBilancioCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {

	public ProgettoPianoEconomicoVoceBilancioCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException
	{
		Progetto_piano_economicoBulk pianoEco = (Progetto_piano_economicoBulk)getParentModel();
		Ass_progetto_piaeco_voceBulk assVocePiaeco = (Ass_progetto_piaeco_voceBulk)detail;
		if (assVocePiaeco.getProgetto_piano_economico().getVoce_piano_economico().getFl_link_vocibil_associate()) {
			if (assVocePiaeco.getElemento_voce().getCd_voce_piano()!=null)
				throw new ValidationException("Scollegamento non possibile! La voce di bilancio "+assVocePiaeco.getElemento_voce().getCd_elemento_voce()+" del "+assVocePiaeco.getElemento_voce().getEsercizio()+" deve essere collegata obbligatoriamente alla voce del piano economico!");
		}

		if (!detail.isToBeCreated()) {
			try {
				Utility.createProgettoRicercaComponentSession().validaCancellazioneVoceAssociataPianoEconomico(
					context.getUserContext(),
					pianoEco,
					detail);
			} catch (ComponentException e) {
				throw new ValidationException(e.getMessage());
			} catch (RemoteException e) {
				throw new ValidationException(e.getMessage());
			}
		}
	}
}
