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

package it.cnr.contab.inventario00.bp;

/**
 * Un BusinessProcess controller che permette di effettuare operazioi di CRUD su istanze di 
 *	Tipo_ammortamentoBulk per la gestione dei Tipi Ammortamento e dell'associazione con le 
 *	Categorie Gruppo Inventario.
**/

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;

import it.cnr.contab.inventario00.docs.bulk.*;
import it.cnr.contab.inventario00.comp.*;
import it.cnr.contab.inventario00.ejb.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;
 
public class CRUDTipoAmmortamentoBP extends it.cnr.jada.util.action.SimpleCRUDBP implements SelectionListener{

	private final SimpleDetailCRUDController catBeni  = new SimpleDetailCRUDController("catBeni",Categoria_gruppo_inventBulk.class,"catBeni",this,true);
	private final SimpleDetailCRUDController catBeniDisponibili = new SimpleDetailCRUDController("catBeniDisponibili",Categoria_gruppo_inventBulk.class,"catBeniDisponibili",this,true);

	/* Controller utilizzato per la visualizzazione delle Categorie/Gruppo associate
	 *	al Tipo Ammortamento. Permette di lavorare sulla tabella ASS_TIPO_AMM_CAT_GRUP_INV
	 *	(e, quindi con la tabella d'appoggio ASS_TIPO_AMM_CAT_GRUP_INV_PAG). 
	*/ 
	private final RemoteDetailCRUDController gruppiController = new RemoteDetailCRUDController("gruppiController",Categoria_gruppo_inventBulk.class,"gruppi","CNRINVENTARIO00_EJB_Tipo_ammortamentoComponentSession",this) {
		protected it.cnr.jada.util.RemoteIterator createRemoteIterator(it.cnr.jada.action.ActionContext context) {
			try{				
				return selectGruppibyClause(context);				
			} catch (it.cnr.jada.action.BusinessProcessException e){
				return null;
			}
				
		}
		public void removeAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.bulk.ValidationException,it.cnr.jada.action.BusinessProcessException {
			eliminaGruppiConBulk(context);
			reset(context);
		}
		protected void removeDetails(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws it.cnr.jada.action.BusinessProcessException {
			eliminaGruppiConBulk(context, details);
		}
		protected void validate(it.cnr.jada.action.ActionContext context,OggettoBulk bulk) throws it.cnr.jada.bulk.ValidationException {}
	};
public CRUDTipoAmmortamentoBP() {
	super("Tr");
}
public CRUDTipoAmmortamentoBP(String function) {
	super(function+"Tr");
}
/**
 * E' stata generata la richiesta di una ricerca sulle Categorie Gruppo Inventario, per selezionare 
 *	quelle da associare al Tipo Ammortamento. Il metodo provvede a resettare le eventuali 
 *	selezioni fatte in precedenza.
 *
 *	@param context la <code>ActionContext</code> che ha generato la richiesta.
 */
public void clearSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		((Tipo_ammortamentoComponentSession)createComponentSession()).annullaModificaGruppi(
			context.getUserContext());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 
}
/**
  *  Crea una toolbar in aggiunta alla normale toolbar del CRUD.
  *	La nuova toolbar è stata costruita per mostrare il tasto "Riassocia".
  *
  * @return toolbar i <code>Button[]</code> pulsanti creati
**/
protected it.cnr.jada.util.jsp.Button[] createRiassociaToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.riassocia");	
	return toolbar;
}
/**
  * Metodo richiesto dall'interfaccia <code>SelectionListener</code>.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
**/
public void deselectAll(it.cnr.jada.action.ActionContext context) {}
/**
  * Gestisce l'operazione di eliminazione di tutti i gruppi associati al Tipo Ammortamento.
  *	Utilizza il metodo <code>Tipo_ammortamentoComponent.eliminaGruppiConBulk(UserContext, Tipo_ammortamentoBulk)</code>.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
**/
private void eliminaGruppiConBulk(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
	
	
	try {
		((Tipo_ammortamentoComponentSession)createComponentSession()).eliminaGruppiConBulk(
			context.getUserContext(),
			(Tipo_ammortamentoBulk)getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch (Throwable t){
		throw handleException(t);
	}
}
	
/**
  * Gestisce l'operazione di eliminazione di alcuni gruppi associati al Tipo Ammortamento.
  *	Utilizza il metodo <code>Tipo_ammortamentoComponent.eliminaGruppiConBulk(UserContext, Tipo_ammortamentoBulk, OggettoBulk[])</code>.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
**/
private void eliminaGruppiConBulk(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws it.cnr.jada.action.BusinessProcessException{

	try {		
		
		((Tipo_ammortamentoComponentSession)createComponentSession()).eliminaGruppiConBulk(
			context.getUserContext(),
			(Tipo_ammortamentoBulk)getModel(),
			details);
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 
	
}
	
/**
 * Restituisce un oggetto SearchProvider, che sarà utilizzato nella ricerca dei 
 * 	Tipi Ammortamento disponibili per l'operazione di RIASSOCIA.
 *
 * @param context il <code>ActionContext</code> che ha generato la richiesta
 *
 * @return <code>SearchProvider</code>
**/
public SearchProvider getAmmortamentoSearchProvider(ActionContext context){

	return new SearchProvider() {
			public it.cnr.jada.util.RemoteIterator search(it.cnr.jada.action.ActionContext context,it.cnr.jada.persistency.sql.CompoundFindClause clauses,it.cnr.jada.bulk.OggettoBulk prototype) throws it.cnr.jada.action.BusinessProcessException {				
				Tipo_ammortamentoBulk tipo_ammortamento =(Tipo_ammortamentoBulk)getModel();
				try{
					return ((Tipo_ammortamentoComponentSession)createComponentSession()).getAmmortamentoRemoteIteratorPerRiassocia(context.getUserContext(), 
							(Tipo_ammortamentoBulk)getModel(), 
							Tipo_ammortamentoBulk.class, 
							clauses);
				} catch (Throwable t){
					return null;
				}
				
			}
		};
}
/**
 * Restituisce il valore della proprietà 'catBeni'
 *
 * @return Il valore della proprietà 'catBeni'
 */
public final SimpleDetailCRUDController getCatBeni() {
	return catBeni;
}
/**
 * Restituisce il valore della proprietà 'catBeniDisponibili'
 *
 * @return Il valore della proprietà 'catBeniDisponibili'
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCatBeniDisponibili() {
	return catBeniDisponibili;
}
/**
 * Restituisce il valore della proprietà 'gruppiController'
 *
 * @return Il valore della proprietà 'gruppiController'
 */
public final RemoteDetailCRUDController getGruppiController() {
	return gruppiController;
}
/**
 * Restituisce la Selezione fatta sul Controller dei Gruppi
 */
public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) {
	
	return currentSelection;
}
/**
 * initializeSelection method comment.
 */
public void initializeSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		((Tipo_ammortamentoComponentSession)createComponentSession()).inizializzaGruppiPerModifica(
			context.getUserContext());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 
}
/**
 * Abilita il pulsante di "Associa", solo se si è in modifica di un Tipo Ammortamento già creato.
 *	Restituisce TRUE se il BusinessProcess è <code>isEditable</code>
 *
 * @return <code>boolean</code>
 */
public boolean isBottoneAssociaEnabled() {

	// Abilito il bottone se sono in modifica
	if(this.isEditable()){
		return (true);
	}
	return (false);	// disabilito
}

public boolean isBottoneImpostaCatBeniEnabled() {

	if(this.isEditable() && ((Tipo_ammortamentoBulk)this.getModel()).getCatBeni() != null &&
		((Tipo_ammortamentoBulk)this.getModel()).getCatBeni().size()!=0){
			return (true); //abilitato
	}
	return (false);	// disabilitato
}
/**
 * Abilita il pulsante di "Elimina".
 *	Il pulsante è abilitato solo se si è in modifica di un Tipo Ammortamento già creato.
 *	Restituisce TRUE se il BusinessProcess è <code>isEditable</code>
 *
 * @return <code>boolean</code>
 *
**/ 
public boolean isBottoneRimuoviEnabled() {

	// Abilito il bottone se sono in modifica
	if(this.isEditable() && ((Tipo_ammortamentoBulk)this.getModel()).getCatBeni() != null &&
		((Tipo_ammortamentoBulk)this.getModel()).getCatBeni().size()!=0){
			return (true);
	}
	return (false);	// disabilito
}
/**
 * Abilita il pulsante di "Riassocia", solo se si è in modifica di un Tipo Ammortamento già creato.
 *	Restituisce TRUE se il BusinessProcess è <code>isEditable</code>
 *
 * @return <code>boolean</code>
 *
**/
public boolean isRiassociaButtonEnabled() {
	
	return	isEditing() && getModel() != null;
}
/**
 * Nasconde il pulsante di "Riassocia".
 *	Il pulsante viene nascosto quando si è in ricerca di un Tipo Ammortamento.
 *	Restituisce TRUE se il BusinessProcess è <code>isSearching</code>
 *
 * @return <code>boolean</code>
**/ 
public boolean isRiassociaButtonHidden() {
	
	return isSearching();
}
/**
 * Associa tutti i gruppi.
 *	L'utente, dopo aver efettuato una ricerca per individuare tutte le Categorie Gruppo Inventario 
 *	disponibili ad essere associate al Tipo Ammortamento, decide di associare tutte i gruppi 
 *	trovati. Il metodo invoca <code>Tipo_ammortamentoComponent.associaTuttiGruppi(UserContext, Tipo_ammortamentoBulk, List)</code>.
 *
 * @param context il <code>ActionContext</code> che ha generato la richiesta
 */
public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	
	try {
		((Tipo_ammortamentoComponentSession)createComponentSession()).associaTuttiGruppi(
			context.getUserContext(),
			(Tipo_ammortamentoBulk)getModel(),
			null);

	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}  
}
/**
 * Carica i gruppi associati al Tipo Ammortamento.
 *	E' stata generata la richiesta di cercare tutte le Categorie Gruppo Inventario associate
 *	al Tipo Ammortamento. 
 *	Il metodo invoca <code>Tipo_ammortamentoComponent.selectGruppiByClause(UserContext, Tipo_ammortamentoBulk, Class, CompoundFindClause)</code>.
 *
 * @param context il <code>ActionContext</code> che ha generato la richiesta.
 *
 * @return <code>RemoteIterator</code> l'Iterator sui gruppi selezionati.
 */
private it.cnr.jada.util.RemoteIterator selectGruppibyClause(ActionContext context) throws BusinessProcessException{

	
	try {
		it.cnr.jada.persistency.sql.CompoundFindClause clauses = ((RemoteDetailCRUDController)getGruppiController()).getFilter();
		it.cnr.jada.UserContext userContext = context.getUserContext();
		Tipo_ammortamentoBulk tipo_ammortamento = (Tipo_ammortamentoBulk)getModel();		
		Class bulkClass = Categoria_gruppo_inventBulk.class;

		
		return ((Tipo_ammortamentoComponentSession)createComponentSession()).selectGruppiByClause(userContext,tipo_ammortamento,bulkClass,clauses);		
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 	
	
}
/**
 * Inserisce i grupppi temporanei.
 * 	E' stata generata la richiesta di riportare i gruppi selezionati dall'utente nella tabella 
 *	temporanea ASS_TIPO_AMM_CAT_GRUP_INV_APG.
 *	Il metodo invoca <code>Tipo_ammortamentoComponent.modificaGruppi(UserContext, Tipo_ammortamentoBulk, OggettoBulk[], BitSet, BitSet)</code>.
 *
 * @param context il <code>ActionContext</code> che ha generato la richiesta.
 * @param bulks <code>OggettoBulk[]</code> i gruppi selezionati dall'utente.
 * @param oldSelection la <code>BitSet</code> selezione precedente.
 * @param newSelection la <code>BitSet</code> selezione attuale.
 *
 * @return <code>BitSet</code> la selezione dell'utente.
**/ 
public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, 
		it.cnr.jada.bulk.OggettoBulk[] bulks, 
		java.util.BitSet oldSelection, 
		java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {
			
	try {		
		((Tipo_ammortamentoComponentSession)createComponentSession()).modificaGruppi(
			context.getUserContext(),
			(Tipo_ammortamentoBulk)getModel(),
			bulks,
			oldSelection,
			newSelection);
		return newSelection;
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 
}
/**
 * Scrive la toolbar contenente il tasto di "Riassocia"
 *
 * @param writer <code>JspWriter</code>
**/
public void writeRiassociaToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException,javax.servlet.ServletException {

	if (!isSearching()) {
		openToolbar(writer);
		it.cnr.jada.util.jsp.JSPUtils.toolbar(writer,createRiassociaToolbar(),this, this.getParentRoot().isBootstrap());
		closeToolbar(writer);
	}
}
/**
 * Aggiunge alla normale toolbar di CRUD, la toolbar per l'operazione di "Riassocia", 
 *	(v. metodo createRiassociaToolbar()).
 *
 * @param writer <code>JspWriter</code>
**/
public void writeToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException,javax.servlet.ServletException {

	super.writeToolbar(writer);
	writeRiassociaToolbar(writer);
}
}
