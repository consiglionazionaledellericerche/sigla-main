package it.cnr.contab.progettiric00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;

public class ProgettoPianoEconomicoVoceBilancioCRUDController extends SimpleProgettoPianoEconomicoVoceBilancioCRUDController {
	public ProgettoPianoEconomicoVoceBilancioCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	@Override
	public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
		super.validateForDelete(context, detail);

		if (!detail.isToBeCreated()) {
			try {
				if (Optional.ofNullable(getParentController())
						.flatMap(el->Optional.ofNullable(el.getParentController()))
						.flatMap(el->Optional.ofNullable(el.getModel()))
			            .filter(ProgettoBulk.class::isInstance)
			            .map(ProgettoBulk.class::cast)
			            .map(el -> el.isROProgettoForStato())
			            .orElse(Boolean.FALSE))
		        	Optional.ofNullable(detail).filter(Ass_progetto_piaeco_voceBulk.class::isInstance).map(Ass_progetto_piaeco_voceBulk.class::cast)
		        	.filter(el->el.getSaldoSpesa().getAssestato().compareTo(BigDecimal.ZERO)==0)
		        	.orElseThrow(()->new ValidationException("Non è possibile disassociare voci di bilancio movimentate. E' possibile solo tramite rimodulazione!"));

				Utility.createProgettoRicercaComponentSession().validaCancellazioneVoceAssociataPianoEconomico(
					context.getUserContext(),
					(Progetto_piano_economicoBulk)getParentModel(),
					detail);
			} catch (ComponentException e) {
				throw new ValidationException(e.getMessage());
			} catch (RemoteException e) {
				throw new ValidationException(e.getMessage());
			}
		}
	}
}