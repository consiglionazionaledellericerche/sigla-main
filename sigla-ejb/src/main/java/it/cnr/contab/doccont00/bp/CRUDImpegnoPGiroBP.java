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

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' Impegno Partita di Giro.
 */
public class CRUDImpegnoPGiroBP extends CRUDVirtualObbligazioneBP {
	private boolean flNuovaGestionePg = false;
	
public CRUDImpegnoPGiroBP() {
	super();
}
public CRUDImpegnoPGiroBP(String function) {
	super(function);
}
/**
 *	Metodo per disabilitare tutti i campi, nel caso l'impegno pgiro sia stato cancellato ( come se fosse in stato di visualizzazione )
 */
public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW)
	{
		ImpegnoPGiroBulk impegno = (ImpegnoPGiroBulk)getModel();
		/*Modifica per documenti contabili "CORI-D" creati in automatico*/
		//if ( impegno != null && (impegno.getDt_cancellazione() != null || (impegno.getAssociazione() != null && impegno.getAssociazione().getAccertamento().getDt_cancellazione() != null)))
		if ( impegno == null )
			return;
		if ( impegno.getDt_cancellazione() != null )
		{
			setStatus(VIEW);
			//il corrispondente accertamento pgiro è stato cancellato
			//if(impegno.getDt_cancellazione() != null)
				setMessage("L'Annotazione di Spesa su Partita di Giro è stata cancellata. Non consentita la modifica.");
			//else
				//setMessage("La modifica non è consentita: l'Annotazione di Entrata su Partita di Giro collegata a questa Annotazione di Spesa è stata cancellata.");							

		}
/*		else if ( "Y".equals(impegno.getRiportato()) )
		{
			setStatus(VIEW);
			setMessage("L'Annotazione di Spesa su Partita di Giro è stata riportata all'esercizio successivo. Non consentita la modifica.");
		}*/
		else if ( "N".equals( impegno.getRiportato()) && impegno.getAssociazione() != null && impegno.getAssociazione().getAccertamento() != null &&
			  "Y".equals(impegno.getAssociazione().getAccertamento().getRiportato()))
		{
			//setStatus(VIEW);
			setMessage("L'Annotazione collegata di Entrata su Partita di Giro è stata riportata all'esercizio successivo. Non consentita la modifica.");
		}
		
		
	}
}
/**
 * Gestisce l'annullamento di un impegno su partita di giro.
 * @param context contesto dell'azione
 */
public void delete(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	int crudStatus = getModel().getCrudStatus();
	try {
			getModel().setToBeUpdated();
			setModel( context, ((it.cnr.contab.doccont00.ejb.ObbligazionePGiroComponentSession) createComponentSession()).annullaObbligazione(context.getUserContext(),(ImpegnoPGiroBulk)getModel()));
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
	
	if (((ImpegnoPGiroBulk) getModel()).getObbligazione_scadenzarioColl().size() == 0)
		return null;
	return (Obbligazione_scadenzarioBulk)((ImpegnoPGiroBulk) getModel()).getObbligazione_scadenzarioColl().get(0);
}
/**
 * Inizializza il modello per la modifica.
 * @param context Il contesto dell'azione
 * @param bulk L'oggetto bulk in uso
 * @return Oggetto Bulk L'oggetto bulk inizializzato
 */
public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
	try {
		it.cnr.jada.ejb.CRUDComponentSession compSession = (getUserTransaction() == null) ?
																			createComponentSession() :
																			getVirtualComponentSession(context, false); //responsabilità setSafePoint(true) demandata all'init del bp
		return compSession.inizializzaBulkPerModifica(
								context.getUserContext(),
								bulk.initializeForEdit(this,context));
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
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
	if ( getModel() != null && Numerazione_doc_contBulk.TIPO_IMP_RES.equals(((ImpegnoPGiroBulk)getModel()).getCd_tipo_documento_cont()))
		isResiduo = true;
	return super.isDeleteButtonEnabled() && !isResiduo;
}
public boolean isRiportaAvantiButtonEnabled() 
{
	ImpegnoPGiroBulk doc = ((ImpegnoPGiroBulk)getModel());

	//nel bilancio ENTE le pgiro si portano avanti singolarmente
	if ( doc!= null && doc.getCd_uo_ente() != null && doc.getCd_uo_ente().equals( doc.getCd_unita_organizzativa()))
		return super.isRiportaAvantiButtonEnabled();
	else	
		return super.isRiportaAvantiButtonEnabled() && !doc.isControparteRiportatata();

}
public boolean isRiportaIndietroButtonEnabled() 
{
	ImpegnoPGiroBulk doc = ((ImpegnoPGiroBulk)getModel());

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
 * Metodo per selezionare la scadenza dell'obbligazione.
 * @param scadenza La scadenza dell'obbligazione
 * @param context Il contesto dell'azione
 */
public void selezionaScadenza(it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk scadenza, ActionContext context) {}
/**
 * Metodo per aggiornare l'impegno.
 * @param context Il contesto dell'azione
 */
public void update(ActionContext context) throws it.cnr.jada.action.BusinessProcessException 
{
	//se provengo da BP dei doc amm imposto il flag fromDocAmm a true
	if ( IDocumentoAmministrativoBP.class.isAssignableFrom( getParent().getClass()))
		((ImpegnoPGiroBulk)getModel()).setFromDocAmm( true );
	else
		((ImpegnoPGiroBulk)getModel()).setFromDocAmm( false );

	super.update( context );
}
public boolean isFlNuovaGestionePg() {
	return flNuovaGestionePg;
}
public void setFlNuovaGestionePg(boolean flNuovaGestionePg) {
	this.flNuovaGestionePg = flNuovaGestionePg;
}

}
