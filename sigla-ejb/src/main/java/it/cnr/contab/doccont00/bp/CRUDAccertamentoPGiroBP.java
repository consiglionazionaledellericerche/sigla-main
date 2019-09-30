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

package it.cnr.contab.doccont00.bp;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;

/**
 * Business Process che gestisce le attività di CRUD per l'entita' Accertamento Partita di Giro.
 */

public class CRUDAccertamentoPGiroBP extends CRUDVirtualAccertamentoBP {
	private boolean flNuovaGestionePg = false;
	
public CRUDAccertamentoPGiroBP() {
	super();	
}
public CRUDAccertamentoPGiroBP(String function) {
	super(function);
}
/**
 *	Metodo per disabilitare tutti i campi, nel caso l'accertamento pgiro sia stato cancellato ( come se fosse in stato di visualizzazione )
 */
public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW)
	{
		AccertamentoPGiroBulk accert_pgiro = (AccertamentoPGiroBulk)getModel();
		/*Modifica per documenti contabili "CORI-D" creati in automatico*/
		//if ( accert_pgiro != null && (accert_pgiro.getDt_cancellazione() != null || (accert_pgiro.getAssociazione() != null && accert_pgiro.getAssociazione().getImpegno().getDt_cancellazione() != null)))
		if ( accert_pgiro == null )
			return;
		if ( accert_pgiro.getDt_cancellazione() != null )
		{
			setStatus(VIEW);
			//il corrispondente impegno è stato cancellato
			//if(accert_pgiro.getDt_cancellazione() != null)
				setMessage("L'Annotazione d'Entrata su Partita di Giro è stata cancellata. Non consentita la modifica.");
			//else
			//	setMessage("La modifica non è consentita: l'Annotazione di Spesa su Partita di Giro collegata a questa Annotazione di Entrata è stata cancellata.");			
		}
		/*
		else if ( "Y".equals(accert_pgiro.getRiportato()) )
		{
			setStatus(VIEW);
			setMessage("L'Annotazione d'Entrata su Partita di Giro è stata riportata all'esercizio successivo. Non consentita la modifica.");
		}*/
		else if ( "N".equals( accert_pgiro.getRiportato()) && accert_pgiro.getAssociazione() != null && accert_pgiro.getAssociazione().getImpegno() != null &&
			  "Y".equals(accert_pgiro.getAssociazione().getImpegno().getRiportato()))
		{
			//setStatus(VIEW);
			setMessage("L'Annotazione collegata di Spesa su Partita di Giro è stata riportata all'esercizio successivo. Non consentita la modifica.");
		}
	}
}
/**
 * Gestisce l'annullamento di un Accertamento su Partita di Giro.
 * @param context contesto dell'azione
 */
public void delete(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	int crudStatus = getModel().getCrudStatus();
	try {
			getModel().setToBeUpdated();
			setModel( context, ((it.cnr.contab.doccont00.ejb.AccertamentoPGiroComponentSession) createComponentSession()).annullaAccertamento(context.getUserContext(),(AccertamentoPGiroBulk)getModel()));
			setStatus(VIEW);			
		} catch(Exception e) {
			getModel().setCrudStatus(crudStatus);
			throw handleException(e);
	}
}
/**
 * getBringBackModel method comment.
 */
public OggettoBulk getBringBackModel() {
	
	if (((AccertamentoPGiroBulk) getModel()).getAccertamento_scadenzarioColl().size() == 0)
		return null;
	return (Accertamento_scadenzarioBulk)((AccertamentoPGiroBulk) getModel()).getAccertamento_scadenzarioColl().get(0);
}
/**
 * Inizializza il modello per la fase di modifica.
 * @param context <code>ActionContext</code> in uso.
 * @param bulk <code>OggettoBulk</code> in uso.
 * @return OggettoBulk L'oggetto inizializzato.
 */
public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
	try {
		it.cnr.jada.ejb.CRUDComponentSession compSession = (getUserTransaction() == null) ?
																			createComponentSession() :
																			getVirtualComponentSession(context, false);
		return compSession.inizializzaBulkPerModifica(
								context.getUserContext(),
								bulk.initializeForEdit(this,context));
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
@Override
protected void initialize(ActionContext actioncontext)
		throws BusinessProcessException {
	// TODO Auto-generated method stub
	super.initialize(actioncontext);
	try {
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())); 
		setFlNuovaGestionePg(parCnr.getFl_nuova_gestione_pg().booleanValue());
		
	}catch(Throwable throwable){
        throw new BusinessProcessException(throwable);
    }
}
//
//	Abilito il bottone di ELIMINA solo se non si tratta di un resisuo
//

public boolean isDeleteButtonEnabled()
{
	boolean isResiduo = false;
	if ( getModel() != null && Numerazione_doc_contBulk.TIPO_ACR_RES.equals(((AccertamentoPGiroBulk)getModel()).getCd_tipo_documento_cont()))
		isResiduo = true;
	return super.isDeleteButtonEnabled() && !isResiduo;
}
public boolean isRiportaAvantiButtonEnabled() 
{
	AccertamentoPGiroBulk doc = ((AccertamentoPGiroBulk)getModel());

	//nel bilancio ENTE le pgiro si portano avanti singolarmente
	if ( doc!= null && doc.getCd_uo_ente() != null && doc.getCd_uo_ente().equals( doc.getCd_unita_organizzativa()))
		return super.isRiportaAvantiButtonEnabled();
	else	
		return super.isRiportaAvantiButtonEnabled() && !doc.isControparteRiportatata();

}
public boolean isRiportaIndietroButtonEnabled() 
{
	AccertamentoPGiroBulk doc = ((AccertamentoPGiroBulk)getModel());

	//nel bilancio ENTE le pgiro si portano avanti singolarmente
	if ( doc!= null && doc.getCd_uo_ente() != null && doc.getCd_uo_ente().equals( doc.getCd_unita_organizzativa()))
		return super.isRiportaIndietroButtonEnabled();
	else	
		return !isRiportaIndietroButtonHidden() &&
					isEditing() &&
					!isDirty() &&
					doc != null &&
					(doc.isDocRiportato() || doc.isControparteRiportatata());				
}
/**
 * Metodo per selezionare la scadenza dell'accertamento.
 * @param scadenza La scadenza dell'accertamento
 * @param context Il contesto dell'azione
 */
public void selezionaScadenza( it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk scadenza, ActionContext context ) {}
/**
 * Metodo per aggiornare l'accertamento.
 * @param context Il contesto dell'azione
 */
public void update(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	//se provengo da BP dei doc amm imposto il flag fromDocAmm a true
	if ( IDocumentoAmministrativoBP.class.isAssignableFrom( getParent().getClass()))
		((AccertamentoPGiroBulk)getModel()).setFromDocAmm( true );
	else
		((AccertamentoPGiroBulk)getModel()).setFromDocAmm( false );

	super.update( context );
}
public boolean isFlNuovaGestionePg() {
	return flNuovaGestionePg;
}
public void setFlNuovaGestionePg(boolean flNuovaGestionePg) {
	this.flNuovaGestionePg = flNuovaGestionePg;
}

}
