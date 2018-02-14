package it.cnr.contab.bollo00.bp;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import it.cnr.contab.bollo00.bulk.Atto_bolloBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.contab.spring.storage.SiglaStorageService;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;

/**
 * BP che gestisce gli atti da assoggettare a bollo virtuale
 */
public class CRUDAttoBolloBP extends AllegatiCRUDBP<AllegatoGenericoBulk, Atto_bolloBulk> {
	private static final long serialVersionUID = 1L;

	public CRUDAttoBolloBP() {
		super();
	}

	public CRUDAttoBolloBP(String function) {
		super(function);
	}
	
	public void validateFogliRighe(ActionContext context) {
		Atto_bolloBulk model = (Atto_bolloBulk)this.getModel();
		if (model.getTipoAttoBollo()!=null && model.getTipoAttoBollo().isCalcoloPerFoglio()) {
			if (Optional.ofNullable(model.getNumRighe()).filter(el->el>0).isPresent()) {
				model.setNumDettagli(model.getNumRighe()/model.getTipoAttoBollo().getRigheFoglio()+
						(model.getNumRighe()%model.getTipoAttoBollo().getRigheFoglio()>0?1:0));
			} else if (Optional.ofNullable(model.getNumDettagli()).filter(el->el>0).isPresent()) {
				model.setNumRighe(0);
			}
		}
	}

	protected String getStorePath(Atto_bolloBulk allegatoParentBulk, boolean create) throws BusinessProcessException {
		return Arrays.asList(
				SpringUtil.getBean(StorePath.class).getPathComunicazioniDal(),
				Optional.ofNullable(allegatoParentBulk.getCdUnitaOrganizzativa())
						.orElse(""),
				"Atti Bollo Virtuale",
				Optional.ofNullable(allegatoParentBulk.getEsercizio())
						.map(esercizio -> String.valueOf(esercizio))
						.orElse("0"),
				String.valueOf(allegatoParentBulk.getId())
		).stream().collect(
				Collectors.joining(SiglaStorageService.SUFFIX)
		);
	}

	@Override
	protected Class<AllegatoGenericoBulk> getAllegatoClass() {
		return AllegatoGenericoBulk.class;
	}
}
