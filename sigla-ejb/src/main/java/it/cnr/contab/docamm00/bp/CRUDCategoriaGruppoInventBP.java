package it.cnr.contab.docamm00.bp;


/**
 * <!-- @TODO: da completare -->
 */

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.*;
import it.cnr.jada.util.action.*;
/**
 * Gestisce le catene di elementi correlate con la fattura passiva in uso.
 */
public class CRUDCategoriaGruppoInventBP extends SimpleCRUDBP {
	
public CRUDCategoriaGruppoInventBP() {
	super();
}

public CRUDCategoriaGruppoInventBP(String function) throws BusinessProcessException{
	super(function);
}

/**
 * <!-- @TODO: da completare -->
 * 
 *
 * @param context	L'ActionContext della richiesta
 * @return 
 * @throws ComponentException	
 */
public RemoteBulkTree getCategoriaGruppoInventTree(ActionContext context) throws it.cnr.jada.comp.ComponentException{
  return
  
	new RemoteBulkTree() {
	  public RemoteIterator getChildren(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  RemoteIterator ri = ((CategoriaGruppoInventComponentSession)createComponentSession()).getChildren(context.getUserContext(),bulk);
		  return it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context, ri);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}

	  }

	  public OggettoBulk getParent(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return ((CategoriaGruppoInventComponentSession)createComponentSession()).getParent(context.getUserContext(),bulk);
		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}
	  }

	  public boolean isLeaf(ActionContext context, OggettoBulk bulk) throws java.rmi.RemoteException {
		try{
		  return ((CategoriaGruppoInventComponentSession)createComponentSession()).isLeaf(context.getUserContext(),bulk);
  		}catch(it.cnr.jada.comp.ComponentException ex){
			throw new java.rmi.RemoteException("Component Exception",ex);
		}catch(it.cnr.jada.action.BusinessProcessException ex){
			throw new java.rmi.RemoteException("BusinessProcess Exception",ex);
		}
	  }
	};
}
}