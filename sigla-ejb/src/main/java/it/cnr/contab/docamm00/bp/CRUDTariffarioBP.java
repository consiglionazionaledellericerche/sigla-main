package it.cnr.contab.docamm00.bp;

import it.cnr.contab.docamm00.tabrif.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;

/**
 * Insert the type's description here.
 * Creation date: (18/10/2001 17.22.33)
 * @author: Vincenzo Bisquadro
 */
public class CRUDTariffarioBP extends it.cnr.jada.util.action.SimpleCRUDBP{
/**
 * CRUDTariffarioBP constructor comment.
 */
public CRUDTariffarioBP() {
	super();
}
/**
 * CRUDTariffarioBP constructor comment.
 * @param function java.lang.String
 */
public CRUDTariffarioBP(String function) {
	super(function);
}
protected void basicEdit(it.cnr.jada.action.ActionContext context, OggettoBulk bulk, boolean doInitializeForEdit)
    throws it.cnr.jada.action.BusinessProcessException {
    try {
        super.basicEdit(context, bulk, true);

        TariffarioBulk tariff = (TariffarioBulk) bulk;
        if (tariff.getDataFineValidita() != null) {
            setMessage("E' possibile modificare solo il record attivo!");
        }
    } catch (Throwable e) {
        throw new it.cnr.jada.action.BusinessProcessException(e);
    }
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() {
	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[7];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.eliminaTariffario");
	
	return toolbar;
}
public boolean isDeleteButtonEnabled() {
	return super.isDeleteButtonEnabled() && !isInputReadonly();
}
public boolean isEliminaTariffarioEnabled() {
	
	return !isInserting() && !isSearching();
}
public boolean isEliminaTariffarioHidden() {
	
	return super.isDeleteButtonHidden();
}
public boolean isInputReadonly() {
	return ((TariffarioBulk)getModel()).getDataFineValidita() != null;
}
public boolean isSaveButtonEnabled() {
	return super.isSaveButtonEnabled() && !isInputReadonly();
}
}
