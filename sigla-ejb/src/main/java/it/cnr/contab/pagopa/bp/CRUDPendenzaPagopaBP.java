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

package it.cnr.contab.pagopa.bp;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.docamm00.bp.IDocumentoAmministrativoBP;
import it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * Business Process che gestisce le attività di CRUD per l'entita' Impegno Partita di Giro.
 */
public class CRUDPendenzaPagopaBP extends SimpleCRUDBP {

public CRUDPendenzaPagopaBP() {
	super();
}
public CRUDPendenzaPagopaBP(String function) {
	super(function);
}
/**
 *	Metodo per disabilitare tutti i campi, nel caso l'impegno pgiro sia stato cancellato ( come se fosse in stato di visualizzazione )
 */
public void basicEdit(ActionContext context, OggettoBulk bulk, boolean doInitializeForEdit) throws BusinessProcessException {
	
	super.basicEdit(context, bulk, doInitializeForEdit);

	if (getStatus()!=VIEW)
	{
		PendenzaPagopaBulk pendenzaPagopaBulk = (PendenzaPagopaBulk)getModel();
		/*Modifica per documenti contabili "CORI-D" creati in automatico*/
		//if ( impegno != null && (impegno.getDt_cancellazione() != null || (impegno.getAssociazione() != null && impegno.getAssociazione().getAccertamento().getDt_cancellazione() != null)))
		if ( pendenzaPagopaBulk == null )
			return;
		if ( pendenzaPagopaBulk.isPendenzaNonModificabile())
		{
			setStatus(VIEW);
				setMessage("La pendenza PagoPA è chiusa, non consentita la modifica.");

		}
	}
}
/**
 * Gestisce l'annullamento di un impegno su partita di giro.
 * @param context contesto dell'azione
 */
public void delete(ActionContext context) throws BusinessProcessException {
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
	return super.initializeModelForEdit(context,bulk);
}
protected void initialize(ActionContext actioncontext)
		throws BusinessProcessException {
	// TODO Auto-generated method stub
	super.initialize(actioncontext);
	try {
		Parametri_cnrBulk parCnr = Utility.createParametriCnrComponentSession().getParametriCnr(actioncontext.getUserContext(), CNRUserContext.getEsercizio(actioncontext.getUserContext())); 
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
/**
 * Metodo per selezionare la scadenza dell'obbligazione.
 * @param scadenza La scadenza dell'obbligazione
 * @param context Il contesto dell'azione
 */
public void selezionaScadenza(Obbligazione_scadenzarioBulk scadenza, ActionContext context) {}
/**
 * Metodo per aggiornare l'impegno.
 * @param context Il contesto dell'azione
 */
public void update(ActionContext context) throws BusinessProcessException
{
	//se provengo da BP dei doc amm imposto il flag fromDocAmm a true
	if ( IDocumentoAmministrativoBP.class.isAssignableFrom( getParent().getClass()))
		((ImpegnoPGiroBulk)getModel()).setFromDocAmm( true );
	else
		((ImpegnoPGiroBulk)getModel()).setFromDocAmm( false );

	super.update( context );
}
}
