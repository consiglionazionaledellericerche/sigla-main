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

import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.doccont00.ejb.ObbligazionePGiroComponentSession;
import it.cnr.contab.docamm00.bp.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' Impegno da bilancio Ente.
 */
public class CRUDImpegnoBilEnteBP extends CRUDVirtualObbligazioneBP {
public CRUDImpegnoBilEnteBP() {
	super();
}
public CRUDImpegnoBilEnteBP(String function) {
	super(function);
}
/**
 *	Metodo per disabilitare tutti i campi, nel caso l'impegno sia stato cancellato ( come se fosse in stato di visualizzazione )
 */
public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW)
	{
		ImpegnoBulk impegno = (ImpegnoBulk)getModel();
		if ( impegno == null )
			return;
		if ( impegno.getDt_cancellazione() != null )
		{
			setStatus(VIEW);
			setMessage("L'Impegno su bilancio Ente è stata cancellato. Non consentita la modifica.");
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
			setModel( context, ((it.cnr.contab.doccont00.ejb.ObbligazioneBilEnteComponentSession) createComponentSession()).annullaObbligazione(context.getUserContext(),(ImpegnoBulk)getModel()));
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
	
	if (((ImpegnoBulk) getModel()).getObbligazione_scadenzarioColl().size() == 0)
		return null;
	return (Obbligazione_scadenzarioBulk)((ImpegnoBulk) getModel()).getObbligazione_scadenzarioColl().get(0);
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
//
//	Abilito il bottone di ELIMINA solo se non si tratta di un resisuo
//

public boolean isDeleteButtonEnabled()
{
	boolean isResiduo = false;
	if ( getModel() != null && Numerazione_doc_contBulk.TIPO_IMP_RES.equals(((ImpegnoBulk)getModel()).getCd_tipo_documento_cont()))
		isResiduo = true;
	return super.isDeleteButtonEnabled() && !isResiduo;
}
public boolean isRiportaAvantiButtonEnabled() 
{
	ImpegnoBulk doc = ((ImpegnoBulk)getModel());

	//nel bilancio ENTE gli impegni si portano avanti singolarmente
	if ( doc!= null && doc.getCd_uo_ente() != null && doc.getCd_uo_ente().equals( doc.getCd_unita_organizzativa()))
		return super.isRiportaAvantiButtonEnabled();
	else	
		return super.isRiportaAvantiButtonEnabled() && !doc.isControparteRiportatata();

}
public boolean isRiportaIndietroButtonEnabled() 
{
	ImpegnoBulk doc = ((ImpegnoBulk)getModel());

	//nel bilancio ENTE gli impegni si portano avanti singolarmente
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
		((ImpegnoBulk)getModel()).setFromDocAmm( true );
	else
		((ImpegnoBulk)getModel()).setFromDocAmm( false );

	super.update( context );
}
public void caricaElementoVoce(ActionContext context, Voce_fBulk voce) throws it.cnr.jada.action.BusinessProcessException {
	ImpegnoBulk imp = ((ImpegnoBulk)getModel());
	try {
		imp.setElemento_voce(((it.cnr.contab.doccont00.ejb.ObbligazioneBilEnteComponentSession) createComponentSession()).findElementoVoceFor(context.getUserContext(),voce));
	} catch(Exception e) {
			throw handleException(e);
	}
}
}
