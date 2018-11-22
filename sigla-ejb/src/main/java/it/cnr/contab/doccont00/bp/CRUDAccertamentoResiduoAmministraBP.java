package it.cnr.contab.doccont00.bp;

import java.math.BigDecimal;
import java.util.Optional;

import it.cnr.contab.doccont00.core.bulk.AccertamentoResiduoBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;

public class CRUDAccertamentoResiduoAmministraBP extends
		CRUDAccertamentoResiduoBP {
	
	public CRUDAccertamentoResiduoAmministraBP() {
		super();
	}

	public CRUDAccertamentoResiduoAmministraBP(String function) {
		super(function);
	}

	@Override
	public boolean isStatoVisibile() {
		return true;
	}
	
	@Override
	public boolean isROStato() {
		return false;
	}
    
    @Override
    public void update(ActionContext actioncontext) throws BusinessProcessException {
    	Optional.ofNullable(this.getModel()).map(AccertamentoResiduoBulk.class::cast).ifPresent(acc->{
    		if (!acc.isStatoParzialmenteInesigibile() && !acc.isStatoInesigibile() && !acc.isStatoDubbio()) {
    			acc.getPdgVincoliColl().stream().forEach(e->{
    				e.setFl_attivo(Boolean.FALSE);
    				e.setToBeUpdated();
    			});
    			acc.getAccertamentoVincoliPerentiColl().stream().forEach(e->{
    				e.setIm_vincolo(BigDecimal.ZERO);
    				e.setToBeUpdated();
    			});
    		}
    	});
    	super.update(actioncontext);
    }
}
