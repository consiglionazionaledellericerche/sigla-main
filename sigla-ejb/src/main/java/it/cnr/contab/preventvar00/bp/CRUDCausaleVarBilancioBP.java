package it.cnr.contab.preventvar00.bp;
import it.cnr.contab.preventvar00.tabrif.bulk.*;

/**
 * Business Process di gestione delle causali di variazione di bilancio preventivo
 */

public class CRUDCausaleVarBilancioBP extends it.cnr.jada.util.action.SimpleCRUDBP {
public CRUDCausaleVarBilancioBP() {
	super();
}

public CRUDCausaleVarBilancioBP(String function) {
	super(function);
}

public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW){
		Causale_var_bilancioBulk causale = (Causale_var_bilancioBulk)bulk;
		if ( causale != null && causale.getTi_causale().equals(Causale_var_bilancioBulk.SISTEMA)) {
			setStatus(VIEW);
			setMessage("Causale non modificabile.");
		}
	}
}
}