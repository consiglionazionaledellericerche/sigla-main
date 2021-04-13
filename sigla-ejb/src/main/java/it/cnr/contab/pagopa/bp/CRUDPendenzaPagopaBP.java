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
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.contab.doccont00.core.bulk.ImpegnoPGiroBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.contab.pagopa.comp.PendenzaPagopaComponent;
import it.cnr.contab.pagopa.ejb.PendenzaPagopaComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleCRUDBP;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

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
	return super.getBringBackModel();
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
	return super.isDeleteButtonEnabled();
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
	super.update( context );
}
	public void visualizzaAvvisoPagamento(ActionContext actioncontext) throws Exception {
		PendenzaPagopaBulk pendenzaPagopaBulk = (PendenzaPagopaBulk)getModel();
		byte [] stampaAvviso = ((PendenzaPagopaComponentSession)createComponentSession()).stampaAvviso(actioncontext.getUserContext(), pendenzaPagopaBulk);
		ByteArrayInputStream is = new ByteArrayInputStream(stampaAvviso);
		if (is != null){
			((HttpActionContext)actioncontext).getResponse().setContentType("application/pdf");
			OutputStream os = ((HttpActionContext)actioncontext).getResponse().getOutputStream();
			((HttpActionContext)actioncontext).getResponse().setDateHeader("Expires", 0);
			byte[] buffer = new byte[((HttpActionContext)actioncontext).getResponse().getBufferSize()];
			int buflength;
			while ((buflength = is.read(buffer)) > 0) {
				os.write(buffer,0,buflength);
			}
			is.close();
			os.flush();
		}
	}
	public boolean isVisualizzaAvvisoPagamentoButtonHidden() {
		PendenzaPagopaBulk pendenzaPagopaBulk = (PendenzaPagopaBulk)getModel();
		return (pendenzaPagopaBulk == null || pendenzaPagopaBulk.getCdAvviso() == null);
	}
	protected it.cnr.jada.util.jsp.Button[] createToolbar() {
		it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[10];
		int i = 0;
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.search");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.startSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.freeSearch");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.new");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.save");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.delete");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.bringBack");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.undoBringBack");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.print");
		toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.avviso");

		return toolbar;
	}

}
