package it.cnr.contab.bollo00.bp;

import java.util.Optional;

import it.cnr.contab.bollo00.bulk.Atto_bolloBulk;
import it.cnr.jada.action.ActionContext;

/**
 * BP che gestisce l'annullamento massivo di documenti contabili
 */

public class CRUDAttoBolloBP extends it.cnr.jada.util.action.SimpleCRUDBP {

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
}
