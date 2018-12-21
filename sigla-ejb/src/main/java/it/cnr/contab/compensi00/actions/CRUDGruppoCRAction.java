package it.cnr.contab.compensi00.actions;

import java.rmi.RemoteException;
import java.util.Iterator;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.compensi00.bp.*;
import it.cnr.contab.compensi00.ejb.*;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.util.action.FormBP;
/**
 * Insert the type's description here.
 * Creation date: (06/06/2002 16.33.36)
 * @author: Roberto Fantino
 */
public class CRUDGruppoCRAction extends it.cnr.jada.util.action.CRUDAction {

public CRUDGruppoCRAction() {
	super();
}
/**
 * Insert the method's description here.
 * Creation date: (05/06/2002 13.32.14)
 * @return it.cnr.jada.action.Forward
 */

public Forward doAddToCRUDMain_DettagliCRUDController(ActionContext context){
	try {
		CRUDGruppoCRBP bp =(CRUDGruppoCRBP)getBusinessProcess(context);
		
		Gruppo_crBulk gruppo = (Gruppo_crBulk)bp.getModel();
		if (gruppo!=null && gruppo.getCd_gruppo_cr()!=null &&  gruppo.getDs_gruppo_cr()!=null )
			if (gruppo.getCrudStatus()==OggettoBulk.TO_BE_CREATED && bp.controllaEsistenzaGruppo(context,gruppo))
				throw new ApplicationException("Gruppo già esistente!");
			//else if (gruppo.getCrudStatus()==OggettoBulk.TO_BE_CREATED && !bp.controllaEsistenzaGruppo(context,gruppo))
				//throw new ApplicationException("Creare prima il gruppo!");
			else 
				getController(context,"main.DettagliCRUDController").add(context);
		else
			throw new ApplicationException("Inserire o ricercare un Gruppo valido");
	} catch(Throwable e) {
		return handleException(context,e);
	}
	return context.findDefaultForward();
}
public Forward doCreaTutte(ActionContext context){
	try{
		CRUDGruppoCRBP bp = (CRUDGruppoCRBP)getBusinessProcess(context);
		bp.CreaperTutteUO(context.getUserContext());
		setMessage(context, FormBP.WARNING_MESSAGE, "Operazione completata.");
		return context.findDefaultForward();
	}catch(Throwable ex){
		return handleException(context, ex);
	}
	
	}

public Forward doBringBackSearchTerzo(ActionContext context,Gruppo_cr_detBulk dett,TerzoBulk terzo) throws RemoteException {
	try{
			fillModel(context);
		if (terzo!=null) {
				CRUDGruppoCRBP crud = (CRUDGruppoCRBP)getBusinessProcess(context);
				GruppoCRComponentSession comp = (GruppoCRComponentSession)crud.createComponentSession();
				Gruppo_cr_detBulk nuovo = comp.completaTerzo(context.getUserContext(),dett, terzo);
				dett.setModalitaOptions(nuovo.getModalitaOptions());
				dett.setBancaOptions(nuovo.getBancaOptions());
				dett.setTerzo(nuovo.getTerzo());	
				crud.findListaModalita(context,true);
				crud.findListaBanche(context,true);
		} 
		return context.findDefaultForward();
	} catch(Exception e) {
		return handleException(context,e);
	}
}
public Forward doBlankSearchTerzo(ActionContext context, 
		Gruppo_cr_detBulk dett) 
throws java.rmi.RemoteException {
	
	try {
		dett.setTerzo(new TerzoBulk());
		dett.setBancaOptions(null);
		dett.setBanca(new BancaBulk());
		dett.setModalitaOptions(null);
		dett.setModalitaPagamento(new Rif_modalita_pagamentoBulk());
	return context.findDefaultForward();
		
	} catch(Exception e) {
		return handleException(context,e);
	}
}
public Forward doOnModalitaPagamentoChange(ActionContext context) {

	try {
		fillModel(context);
		CRUDGruppoCRBP bp = (CRUDGruppoCRBP)getBusinessProcess(context);
		bp.findListaBanche(context,true);

		return context.findDefaultForward();

	}catch (Throwable ex) {
		return handleException(context, ex);
	}
}
public Forward doSearchListaBanche(ActionContext context) {
	
	CRUDGruppoCRBP bp =(CRUDGruppoCRBP)getBusinessProcess(context);
	Gruppo_cr_detBulk gruppo_det = (Gruppo_cr_detBulk)bp.getDettagliGruppoBP().getModel();
	if(gruppo_det!=null && gruppo_det.getCd_terzo_versamento()!=null){
		String columnSet = gruppo_det.getModalitaPagamento().getTiPagamentoColumnSet();
		return search(context, getFormField(context, "main.dettagliCRUDController.listaBanche"), columnSet);
	}
	else
		return context.findDefaultForward();
}
public Forward doSalva(ActionContext context){
	try {
		fillModel(context);
		
		CRUDGruppoCRBP bp = (CRUDGruppoCRBP)getBusinessProcess(context);
		getController(context,"main.dettagliCRUDController").validate(context);
		if(bp.getDettagliGruppoBP()!=null && bp.getDettagliGruppoBP().countDetails()!=0) 
	    for(Iterator i=bp.getDettagliGruppoBP().getDetails().iterator();i.hasNext();){
		  Gruppo_cr_detBulk det=(Gruppo_cr_detBulk)i.next();
		  bp.validaDettaglio(context, det);
		 }
		return super.doSalva(context);
	}
	catch(ValidationException e) 
	{
		getBusinessProcess(context).setErrorMessage(e.getMessage());
		return context.findDefaultForward();
	}		
	catch(Throwable e) 
	{
		return handleException(context,e);
	}
	
}
public Forward doSelezionaTipoFlusso(ActionContext context) {	

	try {		
		CRUDGruppoCRBP bp = (CRUDGruppoCRBP)getBusinessProcess(context);	
		Gruppo_crBulk gruppo = (Gruppo_crBulk)bp.getModel();
		
		fillModel(context);
		if(gruppo.getCd_tipo_riga_f24()!=Gruppo_crBulk.INPS && gruppo.getCd_matricola_inps()!=null)
			gruppo.setCd_matricola_inps(null);
 		return context.findDefaultForward();
	 
	} catch(Throwable e) {
		return handleException(context,e);
	}
}
}
