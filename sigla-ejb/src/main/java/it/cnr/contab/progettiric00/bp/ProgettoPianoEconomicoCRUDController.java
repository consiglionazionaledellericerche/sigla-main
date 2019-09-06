package it.cnr.contab.progettiric00.bp;

import java.rmi.RemoteException;
import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;

public class ProgettoPianoEconomicoCRUDController extends SimpleProgettoPianoEconomicoCRUDController {

	public ProgettoPianoEconomicoCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException
	{
		if (!detail.isToBeCreated()) {
			Optional<Progetto_piano_economicoBulk> opt = Optional.ofNullable(detail).filter(Progetto_piano_economicoBulk.class::isInstance)
																 .map(Progetto_piano_economicoBulk.class::cast);
			if (opt.filter(el->el.isROProgettoPianoEconomico()).isPresent())
				throw new ValidationException("Eliminazione non possibile! Il progetto per l'anno di riferimento "
								+ opt.get().getEsercizio_piano() + " risulta essere stato confermato in PdgP!");
			try {
				Utility.createProgettoRicercaComponentSession().validaCancellazionePianoEconomicoAssociato(
					context.getUserContext(),
					(ProgettoBulk)getParentModel(),
					detail);
			} catch (ComponentException e) {
				throw new ValidationException(e.getMessage());
			} catch (RemoteException e) {
				throw new ValidationException(e.getMessage());
			}
		}
	}
}
