package it.cnr.contab.progettiric00.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Optional;

import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;

public class ProgettoPianoEconomicoCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {

	public ProgettoPianoEconomicoCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
		super(name, modelClass, listPropertyName, parent);
	}

	public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException
	{
		if (!detail.isToBeCreated()) {
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

	@Override
	public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
		Progetto_piano_economicoBulk pianoEco = (Progetto_piano_economicoBulk)oggettobulk;
		pianoEco.setIm_entrata(BigDecimal.ZERO);
		pianoEco.setFl_ctrl_disp(Boolean.TRUE);
		return super.addDetail(oggettobulk);
	}
}
