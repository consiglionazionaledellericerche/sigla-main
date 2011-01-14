package it.cnr.contab.compensi00.actions;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Iterator;
import it.cnr.contab.compensi00.bp.CRUDAddizionaliBP;
import it.cnr.contab.compensi00.tabrif.bulk.AddizionaliBulk;
import it.cnr.contab.inventario00.bp.CRUDInventarioBeniBP;
import it.cnr.contab.inventario00.consultazioni.bulk.V_cons_registro_inventarioBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.upload.UploadedFile;

public class CRUDAddizionaliAction  extends it.cnr.jada.util.action.CRUDAction {

	public CRUDAddizionaliAction() {
		super();
	}
	public Forward doCarica(ActionContext context) {

		try{
			fillModel(context);
			CRUDAddizionaliBP bp = (CRUDAddizionaliBP)getBusinessProcess(context);
			it.cnr.jada.action.HttpActionContext httpContext = (it.cnr.jada.action.HttpActionContext)context;
			
			UploadedFile file =httpContext.getMultipartParameter("file");
			
			if (file == null || file.getName().equals("")){
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: selezionare un File da caricare.");
			}
			if(file.getFile().getAbsolutePath().endsWith(".xls"))
				bp.doCarica(context,file.getFile());
			else
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: estensione File da caricare errata.");	
			return context.findDefaultForward();
		}
		catch(Throwable ex){
			return handleException(context, ex);
		}
	}
	
	public Forward doSalva(ActionContext actioncontext) throws RemoteException{
		
		CRUDAddizionaliBP bp = (CRUDAddizionaliBP)getBusinessProcess(actioncontext);
		try {
			if (bp.getDettagliCRUDController().countDetails()!=0){
				bp.Aggiornamento_scaglione(actioncontext.getUserContext(), (AddizionaliBulk)bp.getModel());
				bp.getDettagliCRUDController().removeAll(actioncontext);
			}else
				throw new it.cnr.jada.comp.ApplicationException("Non ci sono dati da salvare.");
		} catch (ComponentException e) {
			handleException(actioncontext,e);	
		}catch (BusinessProcessException e) {
			handleException(actioncontext,e);		
		} catch (ValidationException e) {
			handleException(actioncontext,e);
		}
		
		return actioncontext.findDefaultForward();
	}

	public Forward doCloseForm(ActionContext actioncontext) throws BusinessProcessException {
		CRUDAddizionaliBP bp = (CRUDAddizionaliBP)getBusinessProcess(actioncontext);
		try {
			for(Iterator i=bp.getDettagliCRUDController().getDetails().iterator();i.hasNext();){
				AddizionaliBulk addizionale=(AddizionaliBulk)i.next();
				addizionale.setToBeDeleted();
				bp.createComponentSession().eliminaConBulk(actioncontext.getUserContext(),addizionale);
			}
		} catch (ComponentException e) {
			handleException(actioncontext,e);
		} catch (RemoteException e) {
			handleException(actioncontext,e);			
		}
		bp.setDirty(false);
		return super.doCloseForm(actioncontext);
	}
	public Forward doEstrai(ActionContext context) {	

		CRUDAddizionaliBP bp = (CRUDAddizionaliBP)getBusinessProcess(context);
		AddizionaliBulk add = (AddizionaliBulk)bp.getModel();
		try{	
			it.cnr.jada.util.RemoteIterator ri = ((it.cnr.contab.compensi00.ejb.AddizionaliComponentSession)bp.createComponentSession()).cerca(context.getUserContext(),null,add);
			ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
			if (ri.countElements() == 0) {
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile.");
			}
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
			nbp.setIterator(context,ri);
			nbp.disableSelection();
			nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(AddizionaliBulk.class));
			HookForward hook = (HookForward)context.findForward("seleziona");		
			return context.addBusinessProcess(nbp);		
		}
		catch (Throwable e){		
			return handleException(context,e);
		}
	 }
}