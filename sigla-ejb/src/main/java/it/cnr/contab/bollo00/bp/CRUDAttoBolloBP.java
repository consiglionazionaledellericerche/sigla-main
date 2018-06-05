package it.cnr.contab.bollo00.bp;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import it.cnr.contab.bollo00.bulk.Atto_bolloBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.spring.service.StorePath;
import it.cnr.si.spring.storage.StorageService;
import it.cnr.contab.util00.bp.AllegatiCRUDBP;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;

/**
 * BP che gestisce gli atti da assoggettare a bollo virtuale
 */
public class CRUDAttoBolloBP extends AllegatiCRUDBP<AllegatoGenericoBulk, Atto_bolloBulk> {
	private static final long serialVersionUID = 1L;
    private boolean allegatiCollapse = true;

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
				Collectors.joining(StorageService.SUFFIX)
		);
	}

	@Override
	protected Class<AllegatoGenericoBulk> getAllegatoClass() {
		return AllegatoGenericoBulk.class;
	}
	
	public boolean isAllegatiCollapse() {
		return allegatiCollapse;
	}
	
	public void setAllegatiCollapse(boolean allegatiCollapse) {
		this.allegatiCollapse = allegatiCollapse;
	}
	
	@Override
	public OggettoBulk initializeModelForEdit(ActionContext actioncontext, OggettoBulk oggettobulk)	throws BusinessProcessException {
		oggettobulk = super.initializeModelForEdit(actioncontext, oggettobulk);
		Optional.ofNullable(oggettobulk)
			.filter(Atto_bolloBulk.class::isInstance)
			.map(Atto_bolloBulk.class::cast)
			.filter(el->el.getEsercizio_contratto()!=null&&el.getPg_contratto()!=null&&el.getStato_contratto()!=null)
			.ifPresent(el->el.setFlContrattoRegistrato(Boolean.TRUE));
		return oggettobulk;
	}
	
	@Override
	public void validate(ActionContext context) throws ValidationException {
		super.validate(context);
		if (Optional.ofNullable(getModel()).filter(Atto_bolloBulk.class::isInstance).isPresent()) {
			Atto_bolloBulk atto = (Atto_bolloBulk)getModel();
			if (!Optional.ofNullable(atto.getContratto()).map(ContrattoBulk::getPg_contratto).isPresent()) 
				atto.getArchivioAllegati().stream()
						.map(AllegatoGenericoBulk.class::cast)
						.findAny()
						.orElseThrow(()->new ValidationException("Inserire almeno un allegato o indicare il contratto di riferimento!"));
			
			if (Optional.ofNullable(atto.getDt_provv())
					.filter(el->el.after(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate()))
					.isPresent())
				throw new ValidationException("Non è possibile inserire una data protocollo successiva alla data odierna!");
		}
	}	
}
