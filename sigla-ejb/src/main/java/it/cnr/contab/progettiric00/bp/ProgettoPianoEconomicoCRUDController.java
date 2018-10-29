package it.cnr.contab.progettiric00.bp;

import java.math.BigDecimal;
import java.nio.file.DirectoryStream.Filter;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.progettiric00.core.bulk.Ass_progetto_piaeco_voceBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
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

	@Override
	public OggettoBulk removeDetail(OggettoBulk oggettobulk, int i) {
		Progetto_piano_economicoBulk pianoEco = (Progetto_piano_economicoBulk)oggettobulk;
		BulkList<Ass_progetto_piaeco_voceBulk> vociToBeDelete = new BulkList<Ass_progetto_piaeco_voceBulk>(pianoEco.getVociBilancioAssociate());
		Optional.ofNullable(vociToBeDelete).map(el->el.stream()).orElse(Stream.empty())
		.forEach(e->{
			e.setToBeDeleted();
			pianoEco.removeFromVociBilancioAssociate(pianoEco.getVociBilancioAssociate().indexOf(e));
		});
		return super.removeDetail(oggettobulk, i);
	}
	
	@Override
	public int addDetail(OggettoBulk oggettobulk) throws BusinessProcessException {
		Progetto_piano_economicoBulk pianoEco = (Progetto_piano_economicoBulk)oggettobulk;
		pianoEco.setIm_entrata(BigDecimal.ZERO);
		pianoEco.setFl_ctrl_disp(Boolean.TRUE);
		return super.addDetail(oggettobulk);
	}

}
