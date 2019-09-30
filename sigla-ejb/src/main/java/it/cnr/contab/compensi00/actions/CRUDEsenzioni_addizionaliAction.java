/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.compensi00.actions;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Iterator;
import it.cnr.contab.compensi00.bp.CRUDEsenzioni_addizionaliBP;
import it.cnr.contab.compensi00.tabrif.bulk.Esenzioni_addizionaliBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Forward;
import it.cnr.jada.action.HookForward;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SelezionatoreListaBP;
import it.cnr.jada.util.upload.UploadedFile;

public class CRUDEsenzioni_addizionaliAction  extends it.cnr.jada.util.action.CRUDAction {

	public CRUDEsenzioni_addizionaliAction() {
		super();
	}
	public Forward doCarica(ActionContext context) {

		try{
			fillModel(context);
			CRUDEsenzioni_addizionaliBP bp = (CRUDEsenzioni_addizionaliBP)getBusinessProcess(context);
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
		
		CRUDEsenzioni_addizionaliBP bp = (CRUDEsenzioni_addizionaliBP)getBusinessProcess(actioncontext);
		try {
			if (bp.getDettagliCRUDController().countDetails()!=0){
				for(Iterator i=bp.getDettagliCRUDController().getDetails().iterator();i.hasNext();){
					Esenzioni_addizionaliBulk nuova=(Esenzioni_addizionaliBulk)i.next();
					if (nuova.getNota() !=null) 
						throw new it.cnr.jada.comp.ApplicationException("Esistono note valorizzate, verificare prima di procedere al salvataggio.");
				}
					
				bp.Aggiornamento(actioncontext.getUserContext(), (Esenzioni_addizionaliBulk)bp.getModel());
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
		CRUDEsenzioni_addizionaliBP bp = (CRUDEsenzioni_addizionaliBP)getBusinessProcess(actioncontext);
		try {
			for(Iterator i=bp.getDettagliCRUDController().getDetails().iterator();i.hasNext();){
				Esenzioni_addizionaliBulk esenzioni_addizionali=(Esenzioni_addizionaliBulk)i.next();
				esenzioni_addizionali.setToBeDeleted();
				bp.createComponentSession().eliminaConBulk(actioncontext.getUserContext(),esenzioni_addizionali);
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

		CRUDEsenzioni_addizionaliBP bp = (CRUDEsenzioni_addizionaliBP)getBusinessProcess(context);
		Esenzioni_addizionaliBulk add = (Esenzioni_addizionaliBulk)bp.getModel();
		try{	
			it.cnr.jada.util.RemoteIterator ri = ((it.cnr.contab.compensi00.ejb.Esenzioni_addizionaliComponentSession)bp.createComponentSession()).cerca(context.getUserContext(),null,add);
			ri = it.cnr.jada.util.ejb.EJBCommonServices.openRemoteIterator(context,ri);
			if (ri.countElements() == 0) {
				it.cnr.jada.util.ejb.EJBCommonServices.closeRemoteIterator(context,ri);
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: Nessun dato disponibile.");
			}
			SelezionatoreListaBP nbp = (SelezionatoreListaBP)context.createBusinessProcess("Selezionatore");
			nbp.setIterator(context,ri);
			nbp.disableSelection();
			nbp.setBulkInfo(it.cnr.jada.bulk.BulkInfo.getBulkInfo(Esenzioni_addizionaliBulk.class));
			HookForward hook = (HookForward)context.findForward("seleziona");		
			return context.addBusinessProcess(nbp);		
		}
		catch (Throwable e){		
			return handleException(context,e);
		}
	}
}