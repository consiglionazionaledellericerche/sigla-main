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
  *  Questa classe gestisce le operazioni di business relative all'associazione di 
  *	una Fattura Passiva a dei beni esistenti nel DB.
**/

import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.contab.inventario01.bulk.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssBeneFatturaBP extends SimpleCRUDBP implements SelectionListener{	
	
	private Boolean perAumentoValore = new Boolean(false);

	private Boolean perAumentoValoreDoc = new Boolean(false);
	
	private Boolean daDocumento = new Boolean(false);
	
	private final SimpleDetailCRUDController dettagliFattura = new it.cnr.jada.util.action.SimpleDetailCRUDController("DettagliFattura",Fattura_passiva_rigaBulk.class,"dettagliFatturaColl",this,true);
	
	private final SimpleDetailCRUDController dettagliDocumento = new it.cnr.jada.util.action.SimpleDetailCRUDController("DettagliDocumento",Documento_generico_rigaBulk.class,"dettagliDocumentoColl",this,true);
	
	private final RemoteDetailCRUDController righeInventarioDaFattura = new RemoteDetailCRUDController("RigheInventarioDaFattura",Inventario_beniBulk.class,"beniAssociati","CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",dettagliFattura){
		protected it.cnr.jada.util.RemoteIterator createRemoteIterator(ActionContext context) {
			try{
				return selectBeniAssociatibyClause(context);
			} catch (BusinessProcessException e){
				return null;
			}
			
		}
		public void removeAll(it.cnr.jada.action.ActionContext context) throws ValidationException,it.cnr.jada.action.BusinessProcessException {
			eliminaBeniAssociatiConBulk(context);
			reset(context);
		}
		protected void removeDetails(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws BusinessProcessException {
			eliminaBeniAssociatiConBulk(context, details);
		}
		public void save(ActionContext context,OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {
			modificaBeneAssociatoConBulk(context, bulk);
			resync(context);
		}
		protected void validate(ActionContext context,OggettoBulk bulk) throws ValidationException {
			validaAssocia(context, bulk);
		}
	};
	private final RemoteDetailCRUDController righeInventarioDaDocumento = new RemoteDetailCRUDController("RigheInventarioDaDocumento",Inventario_beniBulk.class,"beniAssociati","CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",dettagliDocumento){
		protected it.cnr.jada.util.RemoteIterator createRemoteIterator(ActionContext context) {
			try{
				return selectBeniAssociatiDocbyClause(context);
			} catch (BusinessProcessException e){
				return null;
			}
			
		}
		public void removeAll(it.cnr.jada.action.ActionContext context) throws ValidationException,it.cnr.jada.action.BusinessProcessException {
			eliminaBeniAssociatiConBulk(context);
			reset(context);
		}
		protected void removeDetails(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws BusinessProcessException {
			eliminaBeniAssociatiConBulk(context, details);
		}
		public void save(ActionContext context,OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {
			modificaBeneAssociatoConBulk(context, bulk);
			resync(context);
		}
		protected void validate(ActionContext context,OggettoBulk bulk) throws ValidationException {
			validaAssocia(context, bulk);
		}
	};

	private final RemoteDetailCRUDController righeDaFattura = new RemoteDetailCRUDController("RigheDaFattura",Buono_carico_scarico_dettBulk.class,"buoniAssociati","CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",dettagliFattura){
		protected it.cnr.jada.util.RemoteIterator createRemoteIterator(ActionContext context) {
			try{
				return selectBeniAssociatibyClause(context);
			} catch (BusinessProcessException e){
				return null;
			}
				
		}
		public void removeAll(it.cnr.jada.action.ActionContext context) throws ValidationException,it.cnr.jada.action.BusinessProcessException {
			eliminaBuoniAssociatiConBulk(context);
			reset(context);
		}
		protected void removeDetails(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws BusinessProcessException {
			eliminaBuoniAssociatiConBulk(context, details);
		}
		public void save(ActionContext context,OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {
			modificaBeneAssociatoConBulk(context, bulk);
			resync(context);
		}

		@Override
		public void writeHTMLToolbar(PageContext pagecontext, boolean canAddToCRUD, boolean canFilter, boolean canRemoveFromCRUD, boolean closedToolbar, boolean openToolbar) throws IOException, ServletException {
			super.writeHTMLToolbar(pagecontext, canAddToCRUD, canFilter, canRemoveFromCRUD, false, openToolbar);
			final Button button = new Button(it.cnr.jada.util.Config.getHandler().getProperties(AssBeneFatturaBP.class), "Toolbar.filtraPrezzoUnitario");
			button.writeToolbarButton(pagecontext.getOut(), isGrowable(), HttpActionContext.isFromBootstrap(pagecontext));
			closeButtonGROUPToolbar(pagecontext);
		}
	};
	private final RemoteDetailCRUDController righeDaDocumento = new RemoteDetailCRUDController("RigheDaDocumento",Buono_carico_scarico_dettBulk.class,"buoniAssociati","CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",dettagliDocumento){
		protected it.cnr.jada.util.RemoteIterator createRemoteIterator(ActionContext context) {
			try{
				return selectBeniAssociatiDocbyClause(context);
			} catch (BusinessProcessException e){
				return null;
			}
				
		}
		public void removeAll(it.cnr.jada.action.ActionContext context) throws ValidationException,it.cnr.jada.action.BusinessProcessException {
			eliminaBuoniAssociatiConBulk(context);
			reset(context);
		}
		protected void removeDetails(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws BusinessProcessException {
			eliminaBuoniAssociatiConBulk(context, details);
		}
		public void save(ActionContext context,OggettoBulk bulk) throws it.cnr.jada.action.BusinessProcessException {
			modificaBeneAssociatoConBulk(context, bulk);
			resync(context);
		}
	};
	// Controller sulla proprietà buono_carico: permette di utilizzare le property dell'oggetto
	//	senza doverle rimappare
	private final CompoundPropertyController buonoController = new CompoundPropertyController("test_buono", Buono_carico_scaricoBulk.class,"test_buono",this);
/**
 * CRUDAssBeneFatturaBP constructor comment.
 */
public AssBeneFatturaBP() {
	super();
	setTab("tab","tabTestata");
	setTab("tabDettaglio","tab_ass_bene_fattura_per_aumentoDett");
}
/**
 * CRUDAssBeneFatturaBP constructor comment.
 * @param function java.lang.String
 */
public AssBeneFatturaBP(String function) {
	super(function);
	
	setTab("tab","tabTestata");
	setTab("tabDettaglio","tab_ass_bene_fattura_per_aumentoDett");
}
/**
  *  Metodo richiesto dall'interfaccia SelectionListener.
  *	L'utente sta associando dei beni già presenti sul DB alle righe di una Fattura Passiva.
  *	Il metodo è invocato dal FrameWork tutte le volte che si richiede una operazione di 
  *	"Aggiungi nuovo bene" da associare alla riga di Fattura.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
**/
public void clearSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		((BuonoCaricoScaricoComponentSession)createComponentSession()).annullaModificaBeniAssociati(
			context.getUserContext());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException ejbe){
		throw handleException(ejbe);
	} 
}
/**
  * Crea la {@link BuonoCaricoScaricoComponentSession } da usare per effettuare operazioni.
  *	Si è resa necessaria la sua implementazione, poichè il BP è un BulkBP piuttosto
  *	che un SimpleCRUDBP.
  *
**/
public BuonoCaricoScaricoComponentSession createComponentSession() throws BusinessProcessException {
	return (BuonoCaricoScaricoComponentSession)createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession", BuonoCaricoScaricoComponentSession.class);
}
/**
  *  Crea la toolbar in sostituzione alla normale toolbar del CRUD.
  *	Il BusinessProcess, infatti, non è di tipo CRUDBP, ma è di tipo BulkBP; questo comporta 
  *	che la normale barra degli strumenti non viene creata automaticamente e, quindi, 
  *	bisogna provvedere.
  *
  * @return toolbar i <code>Button[]</code> pulsanti creati
**/ 
protected it.cnr.jada.util.jsp.Button[] createToolbar() {
	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[2];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.bringBack");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(it.cnr.jada.util.action.CRUDBP.class),"CRUDToolbar.undoBringBack");
	return toolbar;
}
/**
  * Metodo richiesto dall'interfaccia <code>SelectionListener</code>.
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta  
**/
public void deselectAll(it.cnr.jada.action.ActionContext context) {}
/**
  * Gestisce l'operazione di eliminazione di tutti i beni associati ad una data riga di Fattura.
  * Invoca il metodo <code>BuonoCaricoScaricoComponent.eliminaBeniAssociatiConBulk(UserContext, Ass_inv_bene_fatturaBulk, Fattura_passiva_rigaIBulk)</code>
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
**/
private void eliminaBeniAssociatiConBulk(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
	
	
	try {
		((BuonoCaricoScaricoComponentSession)createComponentSession()).eliminaBeniAssociatiConBulk(
			context.getUserContext(),
			getModel(),
			getDettagliFattura().getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	} catch (Throwable t){
		throw handleException(t);
	}
}
private void eliminaBuoniAssociatiConBulk(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
	
	
	try {
		((BuonoCaricoScaricoComponentSession)createComponentSession()).eliminaBuoniAssociatiConBulk(
			context.getUserContext(),
			(Ass_inv_bene_fatturaBulk)getModel(),
			getDettagliFattura().getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	} catch (Throwable t){
		throw handleException(t);
	}
}

private void eliminaBuoniAssociatiConBulk(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws it.cnr.jada.action.BusinessProcessException{

	try {			
		((BuonoCaricoScaricoComponentSession)createComponentSession()).eliminaBuoniAssociatiConBulk(
			context.getUserContext(),
			(Ass_inv_bene_fatturaBulk)getModel(),
			details,
			getDettagliFattura().getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	}
	
}

		
/**
  * Gestisce l'operazione di eliminazione di alcuni beni associati ad una data riga di Fattura.
  * Invoca il metodo <code>BuonoCaricoScaricoComponent.eliminaBeniAssociatiConBulk(UserContext, Ass_inv_bene_fatturaBulk, OggettoBulk[], Fattura_passiva_rigaIBulk)</code>  
  *
  * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
  * @param details i <code>OggettoBulk[]</code> beni da eliminare, selezionati dall'utente
**/
private void eliminaBeniAssociatiConBulk(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws it.cnr.jada.action.BusinessProcessException{

	try {		
		
		((BuonoCaricoScaricoComponentSession)createComponentSession()).eliminaBeniAssociatiConBulk(
			context.getUserContext(),
			(Ass_inv_bene_fatturaBulk)getModel(),
			details,
			getDettagliFattura().getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	}
	
}
	
public it.cnr.jada.util.RemoteIterator find(it.cnr.jada.action.ActionContext actionContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.jada.bulk.OggettoBulk context, String property) throws it.cnr.jada.action.BusinessProcessException {
	return null;
}
/** 
  * Permette di reindirizzare la chiamata ad una pagina jsp piuttosto che ad un'altra.
  * Se isPerAumentoValore == TRUE, vuol dire che la chiamata è stata fatta dalla fattura
  *	per una operazione di Aumento valore su Beni, quindi
  *	sarà visualizzata la pagina corrispondente.
  *
  * @return <code>Forward</code>
**/  

public Forward findDefaultForward() {
	
	if (isPerAumentoValore()){
		return findForward("perAumentoValore");
	}
	else if (isPerAumentoValoreDoc()){
		return findForward("perAumentoValoreDoc");
	}else if(isDaDocumento())
		return findForward("daDocumento");
	else{
		return findForward("default");
	}
}
public SearchProvider getBeneSearchProvider(ActionContext context){

	
	return new SearchProvider() {
			public it.cnr.jada.util.RemoteIterator search(it.cnr.jada.action.ActionContext context,it.cnr.jada.persistency.sql.CompoundFindClause clauses,it.cnr.jada.bulk.OggettoBulk prototype) throws it.cnr.jada.action.BusinessProcessException {				
				Ass_inv_bene_fatturaBulk associa =(Ass_inv_bene_fatturaBulk)getModel();
				
				try{
					return getListaBeni(context.getUserContext(), false, associa.getBuoniColl(), clauses);
				} catch (Throwable t){
					return null;
				}
				
			}
		};
}
public OggettoBulk getBringBackModel() {
	return getModel();
}
/**
 * Insert the method's description here.
 * Creation date: (28/06/2004 12.17.57)
 * @return it.cnr.jada.util.action.CompoundPropertyController
 */
public final it.cnr.jada.util.action.CompoundPropertyController getBuonoController() {
	return buonoController;
}

public final SimpleDetailCRUDController getDettagliFattura() {
	return dettagliFattura;
}
/**
  *  Costruisce un iteratore sui beni disponibili per l'associazione ad una riga di Fattura.
  *
  * @param userContext il <code>UserContext</code> che ha generato la richiesta
  * @param no_accessori <code>boolean</code> indica se includere i beni accessori nella ricerca
  * @param beni_da_escludere la <code>BulkList</code> lista di beni già selezionati
  * @param clauses le <code>CompoundFindClause</code> clausole della ricerca selezionate dall'utente
**/
public it.cnr.jada.util.RemoteIterator getListaBeni(
	it.cnr.jada.UserContext userContext, 
	boolean no_accessori, 
	BulkList beni_da_escludere,
	it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws BusinessProcessException {

	try{
		
		return ((BuonoCaricoScaricoComponentSession)createComponentSession()).getListaBeni(userContext, (Ass_inv_bene_fatturaBulk)this.getModel(), no_accessori,beni_da_escludere, clauses);
		
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	}
}


public final AbstractDetailCRUDController getRigheDaFattura() {
	return righeDaFattura;
}

public java.util.List getRigheDaFatturaDetails(
	Ass_inv_bene_fatturaBulk associativa,
	Fattura_passiva_rigaBulk riga_selezionata) {
	
	if (associativa != null && riga_selezionata != null){
		BulkList list = (BulkList)associativa.getDettagliRigheHash().get(riga_selezionata);
		return list;
	}	
	else{
		return null;
	}
}
public final AbstractDetailCRUDController getRigheInventarioDaFattura() {
	return righeInventarioDaFattura;
}

public java.util.List getRigheInventarioDaFatturaDetails(
	Ass_inv_bene_fatturaBulk associativa,
	Fattura_passiva_rigaBulk riga_selezionata) {
	
	if (associativa != null && riga_selezionata != null){
		BulkList list = (BulkList)associativa.getDettagliRigheHash().get(riga_selezionata);
		return list;
	}	
	else{
		return null;
	}
}

/**
 * Restituisce la Selezione fatta sul Controller dei Beni Associati
 */
public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) {
	
	return currentSelection;
}
/**
 * Imposta come attivi i tab di default.
 *
 * @param context <code>ActionContext</code>
 */
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);	
	resetTabs();
}
/**
 * initializeSelection method comment.
 */
public void initializeSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		((BuonoCaricoScaricoComponentSession)createComponentSession()).inizializzaBeniAssociatiPerModifica(
			context.getUserContext());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException ejbe) {
		throw handleException(ejbe);
	}
}
/**
  *	Abilito il bottone di "Riporta"
**/
public boolean isBringbackButtonEnabled() {
	return true;
}
/**
  *	Rendo visibile il bottone di Riporta
**/
public boolean isBringbackButtonHidden() {
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (24/06/2004 11.48.10)
 * @return java.lang.Boolean
 */
public boolean isPerAumentoValore() {

	if (perAumentoValore == null)
		return false;
		
	return perAumentoValore.booleanValue();
}
/**
  *	Abilito il bottone di "Annulla Riporta"
**/
public boolean isUndoBringBackButtonEnabled() {
	
	return true;
}
/**
  *	Rendo visibile il bottone di "Annulla Riporta" 
**/
public boolean isUndoBringBackButtonHidden() {
	
	return false;
}
/**
 * 
 */
public void modificaBeneAssociatoConBulk(ActionContext context, OggettoBulk bulk) throws BusinessProcessException{
	try {		
		if (!isDaDocumento()){
			((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeneAssociatoConBulk(
			context.getUserContext(),
			getModel(),
			getDettagliFattura().getModel(),
			(Inventario_beniBulk)bulk);
		}else{
			((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeneAssociatoConBulk(
					context.getUserContext(),
					getModel(),
					getDettagliDocumento().getModel(),
					(Inventario_beniBulk)bulk);
		}
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	} 	
	
}
/**
 * Imposta come attivi i tab di default.
 *
 * @param context <code>ActionContext</code>
 */

public void resetTabs() {
	setTab("tab","tabTestata");
}
/**
 * selectAll method comment.
 */
public void selectAll(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {			
		selectAll(context,null);
}
/**
 * selectAll method comment.
 */
public void selectAll(it.cnr.jada.action.ActionContext context, CompoundFindClause clause) throws it.cnr.jada.action.BusinessProcessException {
	
	try {	
		if (!isDaDocumento()){		
			((BuonoCaricoScaricoComponentSession)createComponentSession()).associaTuttiBeni(
			context.getUserContext(),
			(Ass_inv_bene_fatturaBulk)getModel(),
			getDettagliFattura().getSelectedModels(context),
			clause);
		}else{
			((BuonoCaricoScaricoComponentSession)createComponentSession()).associaTuttiBeni(
		  	context.getUserContext(),
			(Ass_inv_bene_fatturaBulk)getModel(),
			getDettagliDocumento().getSelectedModels(context),
			clause);
		}
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	} catch(ValidationException e) {
		throw handleException(e);
	}
}
/**
 * 
 */
private it.cnr.jada.util.RemoteIterator selectBeniAssociatibyClause(ActionContext context) throws BusinessProcessException{
	try {
		it.cnr.jada.persistency.sql.CompoundFindClause clauses = ((RemoteDetailCRUDController)getRigheInventarioDaFattura()).getFilter();
		it.cnr.jada.UserContext userContext = context.getUserContext();
		Ass_inv_bene_fatturaBulk associaBulk = (Ass_inv_bene_fatturaBulk)getModel();
		Fattura_passiva_rigaIBulk rigaFattura=null;
		Fattura_attiva_rigaIBulk fattura_attiva=null;
		Nota_di_credito_rigaBulk nota=null;
		Nota_di_debito_rigaBulk notadeb=null;
		if (getDettagliFattura().getModel() instanceof Fattura_passiva_rigaIBulk)
			rigaFattura = (Fattura_passiva_rigaIBulk)getDettagliFattura().getModel();
		else if (getDettagliFattura().getModel() instanceof Nota_di_credito_rigaBulk)
			nota =(Nota_di_credito_rigaBulk)getDettagliFattura().getModel();
		else if (getDettagliFattura().getModel() instanceof Fattura_attiva_rigaIBulk)
			fattura_attiva =(Fattura_attiva_rigaIBulk)getDettagliFattura().getModel();
		else
			notadeb =(Nota_di_debito_rigaBulk)getDettagliFattura().getModel();
		
		Class bulkClass;
		if (associaBulk.isPerAumentoValore())
			 bulkClass = Inventario_beniBulk.class;
		else
			 bulkClass = Buono_carico_scarico_dettBulk.class;
		
		// R.P. MODIFICA BENI ASSOCIATI(IN SOSPESO) 
		if (rigaFattura != null && rigaFattura.getFattura_passiva().getHa_beniColl().booleanValue()){		
		   ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiForModifica(userContext,associaBulk,getDettagliFattura().getModel());
			return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,associaBulk,getDettagliFattura().getModel(),bulkClass,clauses);
		}
		else  if (nota != null && nota.getFattura_passiva().getHa_beniColl().booleanValue()){
		   ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiForModifica(userContext,associaBulk,getDettagliFattura().getModel());
				return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,associaBulk,getDettagliFattura().getModel(),bulkClass,clauses);
		}
		else  if (notadeb != null && notadeb.getFattura_passiva().getHa_beniColl().booleanValue()){
					((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiForModifica(userContext,associaBulk,getDettagliFattura().getModel());
					return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,associaBulk,getDettagliFattura().getModel(),bulkClass,clauses);		
		}	
		else  if (fattura_attiva != null && fattura_attiva.getFattura_attiva().getHa_beniColl().booleanValue()){
			((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiForModifica(userContext,associaBulk,getDettagliFattura().getModel());
			return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,associaBulk,getDettagliFattura().getModel(),bulkClass,clauses);
		}
		//FINE
		else
				return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,associaBulk,getDettagliFattura().getModel(),bulkClass,clauses);
		  		
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	}
}
/**
 * 
 */
public void setModel(ActionContext context,OggettoBulk newModel) throws BusinessProcessException {

	Ass_inv_bene_fatturaBulk associaBulk = (Ass_inv_bene_fatturaBulk)newModel;
	try {
			BuonoCaricoScaricoComponentSession component = createComponentSession();
			if (getModel() == null)
				associaBulk.setInventario(component.caricaInventario(context.getUserContext()));
		} catch(javax.ejb.EJBException e) {
			throw handleException(e);
		} catch(java.rmi.RemoteException e) {
			throw handleException(e);
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (Throwable t){
			throw handleException(t);
		}
	super.setModel(context,associaBulk);
}
/**
 * Insert the method's description here.
 * Creation date: (24/06/2004 11.48.10)
 * @param newPerAumentoValore java.lang.Boolean
 */
public void setPerAumentoValore(java.lang.Boolean newPerAumentoValore) {
	perAumentoValore = newPerAumentoValore;
}
/**
 * setSelection method comment.
 */
public java.util.BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {
	try {
		if (!daDocumento){
		((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeniAssociati(
			context.getUserContext(),
			(Ass_inv_bene_fatturaBulk)getModel(),
			getDettagliFattura().getSelectedModels(context),
			bulks,
			oldSelection,
			newSelection);
		}else{
			((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeniAssociati(
				context.getUserContext(),
				(Ass_inv_bene_fatturaBulk)getModel(),
				getDettagliDocumento().getSelectedModels(context),
				bulks,
				oldSelection,
				newSelection);
		}
		
		return newSelection;
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	} catch (ValidationException e){
		throw handleException(e);
	}
}
/**
 * Valida i beni associati ad una riga di Fattura.
 *	L'utente sta associando dei beni ad una riga di Fattura. Se la fattura è <code>per aumento valore</code>,
 *	 viene controllato che l'utente abbia inserito un valore valido nel campo variazione piu.
 *	Le proprietà che vengono controllate sono:
 *	<dl>
 *	<dt><b>Valore da caricare</b>
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta 
 * @param bulk il <code>OggettoBulk</code> bene scaricato.
**/ 
private void validaAssocia(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws ValidationException{

	if (isPerAumentoValore()||isPerAumentoValoreDoc()){
		Inventario_beniBulk bene = (Inventario_beniBulk)bulk;
		if (bene!=null)
		if ((bene.getVariazione_piu() == null )||(bene.getVariazione_piu().compareTo(new java.math.BigDecimal(0))==0)||(bene.getValore_alienazione_apg().compareTo(new java.math.BigDecimal(0))==0))
		{
			throw new ValidationException("Attenzione: specificare un valore da Caricare");
		}
	}		
 }
/** 
 *  Richiede l'ID univoco di Transazione.
 * E' stato richiesto di recuperare/generare l'identificativo di transazione.
 * Viene richiesto l'ID e, se questo non esiste, verrà generato, se force = TRUE
 *
 * @param aUC lo <code>UserContext</code> che ha generato la richiesta.
 * @param force <code>boolean</code> il flag che indica se forzare la generazione dell'ID.
 *
 * @return <code>String</code> l'ID di transazione richiesto.
**/
public String getLocalTransactionID(it.cnr.jada.UserContext aUC, boolean force) throws BusinessProcessException {

	Ass_inv_bene_fatturaBulk associaBulk = (Ass_inv_bene_fatturaBulk)getModel();
			
	if (associaBulk != null && associaBulk.getLocal_transactionID()!= null)
		return associaBulk.getLocal_transactionID();
	else if (force){
		try{
			
			return ((BuonoCaricoScaricoComponentSession)createComponentSession()).getLocalTransactionID(aUC, force);
			
		} catch(javax.ejb.EJBException e) {
			throw handleException(e);
		} catch(java.rmi.RemoteException e) {
			throw handleException(e);
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	return null;
	
}

public final SimpleDetailCRUDController getDettagliDocumento() {
	return dettagliDocumento;
}
public boolean isPerAumentoValoreDoc() {

	if (perAumentoValoreDoc == null)
		return false;
		
	return perAumentoValoreDoc.booleanValue();
}
public void setPerAumentoValoreDoc(java.lang.Boolean newPerAumentoValore) {
	daDocumento=newPerAumentoValore;
	perAumentoValoreDoc = newPerAumentoValore;
}
public RemoteDetailCRUDController getRigheInventarioDaDocumento() {
	return righeInventarioDaDocumento;
}
public RemoteDetailCRUDController getRigheDaDocumento() {
	return righeDaDocumento;
}
public Boolean isDaDocumento() {
	if (daDocumento == null)
		return false;
		
	return daDocumento.booleanValue();
}
public void setDaDocumento(Boolean daDocumento) {
	this.daDocumento = daDocumento;
}
private it.cnr.jada.util.RemoteIterator selectBeniAssociatiDocbyClause(ActionContext context) throws BusinessProcessException{
	try {
		it.cnr.jada.persistency.sql.CompoundFindClause clauses = ((RemoteDetailCRUDController)getRigheInventarioDaDocumento()).getFilter();
		it.cnr.jada.UserContext userContext = context.getUserContext();
		Ass_inv_bene_fatturaBulk associaBulk = (Ass_inv_bene_fatturaBulk)getModel();
			Documento_generico_rigaBulk	riga=null;
		if (isDaDocumento())
				riga = (Documento_generico_rigaBulk)getDettagliDocumento().getModel();
		Class bulkClass;
		if (associaBulk.isPerAumentoValoreDoc())
			 bulkClass = Inventario_beniBulk.class;
		else
			 bulkClass = Buono_carico_scarico_dettBulk.class;
		
		// R.P. MODIFICA BENI ASSOCIATI(IN SOSPESO) 
		if (riga != null && riga.getDocumento_generico().getHa_beniColl().booleanValue()){		
		   ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiForModifica(userContext,associaBulk,getDettagliDocumento().getModel());
			return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,associaBulk,getDettagliDocumento().getModel(),bulkClass,clauses);
		}
		//FINE
		else
				return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,associaBulk,getDettagliDocumento().getModel(),bulkClass,clauses);
		  		
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch(javax.ejb.EJBException e) {
		throw handleException(e);
	}
}
}
