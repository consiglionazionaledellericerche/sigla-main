package it.cnr.contab.anagraf00.bp;

import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.comp.*;
/**
 * Insert the type's description here.
 * Creation date: (30/10/2002 11.01.35)
 * @author: CNRADM
 */
public class CRUDAbiCabBP extends it.cnr.jada.util.action.SimpleCRUDBP {
/**
 * CRUDAbiCabBP constructor comment.
 */
public CRUDAbiCabBP() {
	super();
}
/**
 * CRUDAbiCabBP constructor comment.
 * @param function java.lang.String
 */
public CRUDAbiCabBP(String function) {
	super(function);
}
public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW){
		AbicabBulk abiCab= (AbicabBulk)getModel();
		if (abiCab!=null && abiCab.isCancellatoLogicamente()) {
			setStatus(VIEW);
		}
	}
}
public void findCaps(ActionContext context) throws BusinessProcessException {

	try{
		it.cnr.contab.anagraf00.ejb.AbiCabComponentSession session = (it.cnr.contab.anagraf00.ejb.AbiCabComponentSession)createComponentSession();
		AbicabBulk abicab = session.findCaps(context.getUserContext(), (AbicabBulk)getModel());
		setModel(context, abicab);

	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}catch(ComponentException ex){
		throw handleException(ex);
	}
}
public boolean isCancellatoLogicamente(ActionContext context) throws BusinessProcessException {

	try{
		it.cnr.contab.anagraf00.ejb.AbiCabComponentSession session = (it.cnr.contab.anagraf00.ejb.AbiCabComponentSession)createComponentSession();
		return session.isCancellatoLogicamente(context.getUserContext(), (AbicabBulk)getModel());

	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}catch(ComponentException ex){
		throw handleException(ex);
	}
}
}
