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

package it.cnr.contab.compensi00.bp;

import java.rmi.RemoteException;

import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.contab.compensi00.ejb.GruppoCRComponentSession;
import it.cnr.contab.compensi00.bp.CRUDDettagliGruppoBP;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.jsp.Button;

/**
 * Insert the type's description here.
 * Creation date: (03/12/2001 14.29.38)
 * @author: Paola sala
 */
public class CRUDGruppoCRBP extends it.cnr.jada.util.action.SimpleCRUDBP {

public CRUDGruppoCRBP() {
	super();

}
public CRUDGruppoCRBP(String function) {
	super(function);
}

public GruppoCRComponentSession createComponentSession()
throws BusinessProcessException
{
	return (GruppoCRComponentSession)super.createComponentSession("CNRCOMPENSI00_EJB_GruppoCRComponentSession", GruppoCRComponentSession.class);
}
public void CreaperTutteUO(UserContext context) throws BusinessProcessException{
	try {
	Gruppo_crBulk bulk=(Gruppo_crBulk)getModel();
	
	Gruppo_cr_uoBulk bulk_uo=new Gruppo_cr_uoBulk();
	bulk_uo.setGruppo(bulk);
	
	if (bulk.getCd_gruppo_cr()!=null)		
		createComponentSession().CreaperTutteUOSAC(context, bulk);
	else
		throw new ApplicationException("Specificare un valore per il gruppo");
}catch(it.cnr.jada.comp.ComponentException ex){
	throw handleException(ex);
}catch(java.rmi.RemoteException ex){
	throw handleException(ex);
}
}

private CRUDDettagliGruppoBP dettagliGruppoBP=new CRUDDettagliGruppoBP( "dettagliCRUDController", Gruppo_cr_detBulk.class, "dettagli_col", this){
	public void validateForDelete(ActionContext context, OggettoBulk detail) throws ValidationException {
		validaCancellazioneDettaglio(context, (Gruppo_cr_detBulk)detail);
	}
	protected void validate(ActionContext context, OggettoBulk detail) throws ValidationException {
		validaDettaglio(context,(Gruppo_cr_detBulk)detail);
	}
};

public CRUDDettagliGruppoBP getDettagliGruppoBP() {
	return dettagliGruppoBP;
}

public boolean isAssociaButtonEnabled() {
	Gruppo_crBulk bulk = (Gruppo_crBulk) getModel();
	if (bulk != null && bulk.getCd_gruppo_cr()!=null && bulk.getFl_accentrato()!=null && bulk.getFl_accentrato() && bulk.getCrudStatus()!=OggettoBulk.TO_BE_CREATED)
	  return true;
	return false;
}
public void findListaBanche(ActionContext context,boolean valorizza) throws BusinessProcessException{
	java.util.List coll;
	try{
		Gruppo_cr_detBulk det = (Gruppo_cr_detBulk)getDettagliGruppoBP().getModel();
		if (det.getModalitaPagamento() != null && det.getTerzo()!=null) {			
			 coll =createComponentSession().findListaBanche(context.getUserContext(), det);
//			Assegno di default la prima banca tra quelle selezionate
			 if(coll == null || coll.isEmpty())
				 det.setBanca(null);
			 else
				 if (valorizza)
					 det.setBanca((it.cnr.contab.anagraf00.core.bulk.BancaBulk)new java.util.Vector(coll).firstElement());
		}else
			det.setBanca(null);
	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
public void findListaModalita(ActionContext context,boolean valorizza) throws BusinessProcessException{
	java.util.Collection coll;
	try{
		Gruppo_cr_detBulk det = (Gruppo_cr_detBulk)getDettagliGruppoBP().getModel();
		coll =createComponentSession().findModalitaOptions(context.getUserContext(), det);
//		Assegno di default la prima modalità tra quelle selezionate
		 if(coll == null || coll.isEmpty())
			 det.setModalitaPagamento(null);
		 else
			 if (valorizza)
				 det.setModalitaPagamento((Rif_modalita_pagamentoBulk)new java.util.Vector(coll).firstElement());
	}catch(it.cnr.jada.comp.ComponentException ex){
		throw handleException(ex);
	}catch(java.rmi.RemoteException ex){
		throw handleException(ex);
	}
}
public void validaCancellazioneDettaglio(ActionContext context, Gruppo_cr_detBulk det) throws ValidationException {
	try {
		createComponentSession().validaCancellazioneDettaglio(context.getUserContext(), det);
	} catch (Throwable e) {
		throw new ValidationException(e.getMessage());
	}
}
public void validaDettaglio(ActionContext context, Gruppo_cr_detBulk det) throws ValidationException {
//		try {		
//			GruppoCRComponentSession comp = (GruppoCRComponentSession)createComponentSession();			
//			Gruppo_cr_detBulk nuovo= comp.completaTerzo(context.getUserContext(),det, det.getTerzo());
//			det.setModalitaOptions(nuovo.getModalitaOptions());
//			det.setBancaOptions(nuovo.getBancaOptions());
//			findListaModalita(context,false);
//			findListaBanche(context,false);
			if(det.getRegione()==null|| det.getCd_regione()==null)
				throw new ValidationException("Inserire la Regione");
			if(det.getComune()==null|| det.getPg_comune()==null)
				throw new ValidationException("Inserire il Comune");
			if(det.getTerzo()==null|| det.getCd_terzo_versamento()==null)
				throw new ValidationException("Inserire il Terzo");
			if(det.getModalitaPagamento()==null || det.getCd_modalita_pagamento()==null)
				throw new ValidationException("Inserire la modalita di Pagamento");		
			if(det.getBanca()==null || det.getPg_banca()==null)
				throw new ValidationException("Inserire le coordinate bancarie");
//		} catch (BusinessProcessException e) {
//			throw new ValidationException("Errore nella validazione");	
//		} catch (ComponentException e) {
//			throw new ValidationException(e.getMessage());
//		} catch (RemoteException e) {
//			handleException(e);
//		}
}
public boolean controllaEsistenzaGruppo(ActionContext context, Gruppo_crBulk gruppo) throws ComponentException, BusinessProcessException, RemoteException {
	GruppoCRComponentSession comp = (GruppoCRComponentSession)createComponentSession();
	return comp.controllaEsistenzaGruppo(context.getUserContext(),gruppo);	
	
}
protected it.cnr.jada.util.jsp.Button[] createToolbar() 
{		
	Button[] toolbar = super.createToolbar();
	Button[] newToolbar = new Button[ toolbar.length + 1];
	int i;
	for ( i = 0; i < toolbar.length; i++ )
		newToolbar[i] = toolbar[i];
	newToolbar[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.Associa");		
	return newToolbar;
}
}
