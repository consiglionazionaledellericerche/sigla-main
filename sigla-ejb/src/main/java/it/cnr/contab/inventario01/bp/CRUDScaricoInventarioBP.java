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

/*
 * Created on Jan 19, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.inventario01.bp;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.BitSet;

import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.inventario00.bp.RigheInvDaFatturaCRUDController;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Trasferimento_inventarioBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.EmptyRemoteIterator;
import it.cnr.jada.util.action.AbstractDetailCRUDController;
import it.cnr.jada.util.action.RemoteDetailCRUDController;
import it.cnr.jada.util.action.Selection;
import it.cnr.jada.util.action.SelectionIterator;
import it.cnr.jada.util.action.SelectionListener;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDScaricoInventarioBP extends CRUDCaricoScaricoInventarioBP  implements SelectionListener{
	
	public CRUDScaricoInventarioBP() {
	super("Tr");
	setTab("tab","tabScaricoInventarioTestata");
}

public CRUDScaricoInventarioBP(String function) {
	super(function+"Tr");
}

private it.cnr.jada.persistency.sql.CompoundFindClause clauses;
/* Controller utilizzato per la visualizzazione delle righe di Fattura Attiva,
 *	nel caso di creazione del buono di Scarico da Fattura Attiva. 
*/ 
private final SimpleDetailCRUDController dettagliFattura = new it.cnr.jada.util.action.SimpleDetailCRUDController("DettagliFattura",Fattura_attiva_rigaBulk.class,"dettagliFatturaColl",this,true);

private final SimpleDetailCRUDController dettagliDocumento = new it.cnr.jada.util.action.SimpleDetailCRUDController("DettagliDocumento",Documento_generico_rigaBulk.class,"dettagliDocumentoColl",this,true);

/*
 * Controller utilizzato per la visualizzazioner dei beni legati ad una Riga di Fattura.
*/ 	 
private final RigheInvDaFatturaCRUDController righeInventarioDaFattura = new RigheInvDaFatturaCRUDController("RigheInventarioDaFattura",Inventario_beniBulk.class,"beniAssociati","CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",dettagliFattura){
	protected it.cnr.jada.util.RemoteIterator createRemoteIterator(ActionContext context) {
		try{
			if (isBy_fattura() && isInserting())
				return selectBeniAssociatibyClause(context);
			else
				return new it.cnr.jada.util.EmptyRemoteIterator();
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

	public void validate(ActionContext context,OggettoBulk model) throws ValidationException {			
		validateDettagliDaFattura(context,model);
	}
};
private final RigheInvDaFatturaCRUDController righeInventarioDaDocumento = new RigheInvDaFatturaCRUDController("RigheInventarioDaDocumento",Inventario_beniBulk.class,"beniAssociati","CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",dettagliDocumento){
	protected it.cnr.jada.util.RemoteIterator createRemoteIterator(ActionContext context) {
		try{
			if (isBy_documento() && isInserting())
				return selectBeniAssociatiDocbyClause(context);
			else
				return new it.cnr.jada.util.EmptyRemoteIterator();
		} catch (BusinessProcessException e){
			return null;
		}
			
	}	
	public void removeAll(it.cnr.jada.action.ActionContext context) throws ValidationException,it.cnr.jada.action.BusinessProcessException {
		eliminaBeniAssociatiDocConBulk(context);
		reset(context);
	}
	protected void removeDetails(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws BusinessProcessException {
		eliminaBeniAssociatiDocConBulk(context, details);
	}

	public void validate(ActionContext context,OggettoBulk model) throws ValidationException {			
		validateDettagliDaDocumento(context,model);
	}
};

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	resetTabs();
}
public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
	if (bulk instanceof Trasferimento_inventarioBulk) {
		bulk = super.initializeModelForEdit(context, bulk);
		return bulk;
	}
		Buono_carico_scaricoBulk testata = (Buono_carico_scaricoBulk)bulk;
		testata.setTi_documento("S");	
		PrimaryKeyHashtable righeFatturaHash = null;
		try {
			if (by_fattura){
				righeFatturaHash = ((Buono_carico_scaricoBulk)bulk).getDettagliRigheHash();
			}
			else if (by_documento){
				righeFatturaHash = ((Buono_carico_scaricoBulk)bulk).getDettagliRigheDocHash();
			}
			bulk = super.initializeModelForEdit(context, testata);
			if (by_fattura){
				((Buono_carico_scaricoBulk)bulk).setDettagliRigheHash(righeFatturaHash);
			}else if (by_documento){
				((Buono_carico_scaricoBulk)bulk).setDettagliRigheDocHash(righeFatturaHash);
			}
			return bulk;
		} catch(Throwable e) {
			throw new it.cnr.jada.action.BusinessProcessException(e);
		}
		
}
public OggettoBulk initializeModelForInsert(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
			Buono_carico_scaricoBulk testata = (Buono_carico_scaricoBulk)bulk;
			testata.setTi_documento("S");
			bulk = super.initializeModelForInsert(context, testata);
			return bulk;
}
public OggettoBulk initializeModelForFreeSearch(
	ActionContext actioncontext,
	OggettoBulk oggettobulk)
	throws BusinessProcessException {
		Buono_carico_scaricoBulk testata = (Buono_carico_scaricoBulk)oggettobulk;
		testata.setTi_documento("S");
		oggettobulk = super.initializeModelForFreeSearch(actioncontext, testata);
		return oggettobulk;
}

public OggettoBulk initializeModelForSearch(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
			Buono_carico_scaricoBulk testata = (Buono_carico_scaricoBulk)bulk;
			testata.setTi_documento("S");
			bulk = super.initializeModelForSearch(context, testata);
			return bulk;
}
public void resetForSearch(ActionContext context) throws BusinessProcessException {
	super.resetForSearch(context);
	resetTabs();
}
public void resetTabs() {
	setTab("tab","tabScaricoInventarioTestata");
}

 protected final RigheInvDaFatturaCRUDController dettController = new RigheInvDaFatturaCRUDController("DettController",Inventario_beniBulk.class,"DettagliScarico","CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",this){
	
	protected void validate(ActionContext context,OggettoBulk bulk) throws ValidationException {
		if (bulk instanceof Inventario_beniBulk)
		validaDettaglioScarico(context, bulk);
	}
	protected it.cnr.jada.util.RemoteIterator createRemoteIterator(ActionContext context) {
		try{				
			if (!isBy_fattura() && (isInserting()||isEditing()))
				return selectDettagliScaricobyClause(context);
			else return new it.cnr.jada.util.EmptyRemoteIterator();
		} catch (BusinessProcessException e){
			return null;
		}
			
	}
	public void removeAll(it.cnr.jada.action.ActionContext context) throws ValidationException,it.cnr.jada.action.BusinessProcessException {
		eliminaDettagliScaricoConBulk(context);
		reset(context);
	}
	protected void removeDetails(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws BusinessProcessException {
		eliminaDettagliScaricoConBulk(context, details);
	}

};
/* Controller utilizzato per la visualizzazione dei dettagli del Buono  di Scarico 
 *	in fase di modifica. Permette di lavorare sulla tabella BUONO_CARICO_SCARICO_DETT.
 *	In fase di creazione verrà utilizzto il controller dettagliCRUDController, (v.)
*/ 
private final RemoteDetailCRUDController editDettController = new RemoteDetailCRUDController("editDettController",Buono_carico_scarico_dettBulk.class,"EditDettagliScarico","CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",this) {
	protected it.cnr.jada.util.RemoteIterator createRemoteIterator(ActionContext context) {
		try{
			if (isInserting())
				return new it.cnr.jada.util.EmptyRemoteIterator();
			else
				return selectEditDettagliScaricobyClause(context);
		} catch (BusinessProcessException e){
			return null;
		}
			
	}
};
/**
 * Cerca tutti i beni associati ad una riga di Fattura Attiva
 *	Il Buono di Scarico che si sta creando è stato generato da una Fattura Attiva. E' stata
 *	generata la richiesta di visualizzare i beni associati ad una riga di dettaglio della Fattura.
 *	Invoca il metodo <code>BuonoScaricoComponent.selectBeniAssociatiByClause</code>
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 *
 * @return <code>RemoteIterator</code> l'Iteratore sui beni.
**/
private it.cnr.jada.util.RemoteIterator selectBeniAssociatibyClause(ActionContext context) throws BusinessProcessException{

	
	try {
		it.cnr.jada.persistency.sql.CompoundFindClause clauses = ((RemoteDetailCRUDController)getRigheInventarioDaFattura()).getFilter();
		it.cnr.jada.UserContext userContext = context.getUserContext();
		Buono_carico_scaricoBulk buonoS = (Buono_carico_scaricoBulk)getModel();
		if (getDettagliFattura().getModel().getClass().equals(Fattura_attiva_rigaIBulk.class)){
			Class bulkClass = Inventario_beniBulk.class;
			Fattura_attiva_rigaIBulk rigaFattura = (Fattura_attiva_rigaIBulk)getDettagliFattura().getModel();
		return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,buonoS,rigaFattura,bulkClass,clauses);
		}
		else{
			Nota_di_credito_rigaBulk nota = (Nota_di_credito_rigaBulk)getDettagliFattura().getModel();
			Class bulkClass = Inventario_beniBulk.class;
		return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,buonoS,nota,bulkClass,clauses);
		}
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 	
	
}
private it.cnr.jada.util.RemoteIterator selectBeniAssociatiDocbyClause(ActionContext context) throws BusinessProcessException{

	
	try {
		it.cnr.jada.persistency.sql.CompoundFindClause clauses = ((RemoteDetailCRUDController)getRigheInventarioDaDocumento()).getFilter();
		it.cnr.jada.UserContext userContext = context.getUserContext();
		Buono_carico_scaricoBulk buonoS = (Buono_carico_scaricoBulk)getModel();
		Class bulkClass = Inventario_beniBulk.class;
		return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,buonoS,getDettagliDocumento().getModel(),bulkClass,clauses);
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 	
	
}
/**
 * Cerca tutti i beni del Buono di Scarico.
 *	E' stata generata la richiesta di visualizzare i beni del Buono di Scarico.
 *	Invoca il metodo <code>BuonoScaricoComponent.selectBeniAssociatiByClause</code>
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 *
 * @return <code>RemoteIterator</code> l'Iteratore sui beni.
**/
private it.cnr.jada.util.RemoteIterator selectDettagliScaricobyClause(ActionContext context) throws BusinessProcessException{

	
	try {
		it.cnr.jada.persistency.sql.CompoundFindClause clauses = ((RemoteDetailCRUDController)getRigheInventarioDaFattura()).getFilter();
		it.cnr.jada.UserContext userContext = context.getUserContext();
		Buono_carico_scaricoBulk buonoS = (Buono_carico_scaricoBulk)getModel();
		Fattura_attiva_rigaIBulk rigaFattura = (Fattura_attiva_rigaIBulk)getDettagliFattura().getModel();
		Class bulkClass = Inventario_beniBulk.class;

		
		return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectBeniAssociatiByClause(userContext,buonoS,rigaFattura,bulkClass,clauses);		
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 	
	
}
private void eliminaBeniAssociatiConBulk(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
	
	
	try {
		if(getDettagliFattura().getModel()!=null )
			((BuonoCaricoScaricoComponentSession)createComponentSession()).eliminaBeniAssociatiConBulk(
			context.getUserContext(),
			(Buono_carico_scaricoBulk)getModel(),
			getDettagliFattura().getModel());
		
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}  catch (Throwable t){
		throw handleException(t);
	}
}
	
/**
 * Gestisce l'operazione di eliminazione alcuni beni associati ad una riga di Fattura Attiva.
 *	L'utente sta creando un Buono di Scarico da una Fattura Attiva, ed ha generato la richiesta 
 *	di cancellare alcuni dei beni finora associati ad un dettaglio della Fattura.
 *	Utilizza il metodo <code>BuonoScaricoComponent.eliminaBeniAssociatiConBulk(UserContext, Buono_scaricoBulk, OggettoBulk[], Fattura_attiva_rigaIBulk)</code>.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta
 * @param details <code>OggettoBulk[]</code> i beni selezionati dall'utente che andranno eliminati dal Buono di Scarico
**/
private void eliminaBeniAssociatiConBulk(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws it.cnr.jada.action.BusinessProcessException{

	try {
		if (isBy_fattura()){
			for (int i = 0;i<details.length;i++){
				Inventario_beniBulk dett = (Inventario_beniBulk)details[i];
				if (dett.isBeneAccessorio() && (dett.isTotalmenteScaricato()))
					throw handleException(new ValidationException("Attenzione: durante l'associazione di un Buono di Scarico ad una Fattura Attiva\n " +
						"Non è possibile eliminare beni accessori.\n Il bene " + dett.getNumeroBeneCompleto() + " non può essere eliminato direttamente."));
			}
		}
		((BuonoCaricoScaricoComponentSession)createComponentSession()).eliminaBeniAssociatiConBulk(
			context.getUserContext(),
			(Buono_carico_scaricoBulk)getModel(),
			details,
			getDettagliFattura().getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 
	
}
	
/**
 * Gestisce l'operazione di eliminazione di tutti i beni del Buono di Scarico.
 *	L'utente sta creando un Buono diretto, (non da Fattura Attiva), ed ha generato la richiesta di cancellare tutti 
 *	i beni finora selezionati.
 *	Invoca il metodo <code>BuonoScaricoComponent.eliminaBeniAssociatiConBulk(UserContext, Buono_scaricoBulk, Fattura_attiva_rigaIBulk)</code>.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta
**/
private void eliminaDettagliScaricoConBulk(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
	
	
	try {
		((BuonoCaricoScaricoComponentSession)createComponentSession()).eliminaBeniAssociatiConBulk(
			context.getUserContext(),
			(Buono_carico_scaricoBulk)getModel(),
			getDettagliFattura().getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch (Throwable t){
		throw handleException(t);
	}
}
	
/**
 * Gestisce l'operazione di eliminazione alcuni beni.
 *	L'utente sta creando un Buono diretto, (non da Fattura Attiva), ed ha generato la richiesta 
 *	di cancellare alcuni dei beni finora scaricati.
 *	Utilizza il metodo <code>BuonoCaricoScaricoComponentSession.eliminaBeniAssociatiConBulk(UserContext, Buono_carico_scaricoBulk, OggettoBulk[], Fattura_attiva_rigaIBulk)</code>.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta
 * @param details <code>OggettoBulk[]</code> i beni selezionati dall'utente che andranno eliminati dal Buono di Scarico
**/
private void eliminaDettagliScaricoConBulk(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws it.cnr.jada.action.BusinessProcessException{

	try {		
		
		((BuonoCaricoScaricoComponentSession)createComponentSession()).eliminaBeniAssociatiConBulk(
			context.getUserContext(),
			(Buono_carico_scaricoBulk)getModel(),
			details,
			getDettagliFattura().getModel());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 
	
}
/**
 * Insert the method's description here.
 * Creation date: (12/09/2002 11.06.12)
 * @return it.cnr.jada.persistency.sql.CompoundFindClause
 */
public it.cnr.jada.persistency.sql.CompoundFindClause getClauses() {
	return clauses;
}

/**
 * Restituisce il valore della proprietà 'DettController'
 *
 * @return <code>RemoteDetailCRUDController</code> il valore della proprietà 'DettController'
 */
public final RemoteDetailCRUDController getDettController() {
	return dettController;
}

private String getIntervallo(Buono_carico_scaricoBulk buonoS){
	
	String intervallo = "";
	if (buonoS.getBuono_carico_scarico_dettColl() != null){
		intervallo = (buonoS.getBuono_carico_scarico_dettColl().size()+1) + "-" + (buonoS.getBuono_carico_scarico_dettColl().size()+1);
	}
	return intervallo;
}
/**
 * E' stata generata la richiesta di cercare i beni disponibili per una operazione d Scarico.
 *	Vengono invocati due diversi metodi della Component <i>BuonoScaricoComponent</i>, a seconda
 *	che il Buono di Scarico sia creato da una Fattura Attiva oppure no:
 *		- invoca il metodo <code>BuonoScaricoComponent.cercaBeniAssociabili</code>, se 
 *			proviene da Fattura Attiva;
 *		- invoca il metodo <code>BuonoScaricoComponent.cercaBeniScaricabili</code>, altrimenti.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param no_accessori <code>boolean</code> indica se escludere i beni accessori dalla ricerca
 * @param beni_da_escludere <code>SimpleBulkList</code> i beni da escludere dalla ricerca, magari perchè già selezionati
 * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
 *
 * @return l'Iteratore <code>RemoteIterator</code> sui beni trovati.
**/
public it.cnr.jada.util.RemoteIterator getListaBeniDaScaricare(
		it.cnr.jada.UserContext userContext, 
		boolean no_accessori, 
		SimpleBulkList beni_da_escludere,
		SimpleBulkList beni_da_escludere_princ,
		it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws BusinessProcessException, java.rmi.RemoteException, it.cnr.jada.comp.ComponentException {

		if (isBy_fattura()){
			Fattura_attiva_rigaIBulk riga_fattura = (Fattura_attiva_rigaIBulk)getDettagliFattura().getModel();	
			return ((BuonoCaricoScaricoComponentSession)createComponentSession()).cercaBeniAssociabili(userContext, (Buono_carico_scaricoBulk)this.getModel(), riga_fattura, clauses);
		} else
		if (isBy_documento())		
			return ((BuonoCaricoScaricoComponentSession)createComponentSession()).cercaBeniAssociabili(userContext, (Buono_carico_scaricoBulk)this.getModel(),(Documento_generico_rigaBulk) getDettagliDocumento().getModel(), clauses);
		else
			return ((BuonoCaricoScaricoComponentSession)createComponentSession()).cercaBeniScaricabili(userContext, (Buono_carico_scaricoBulk)this.getModel(),no_accessori,beni_da_escludere,clauses);
	}

/**
 * Abilito il pulsane "Nuovo" della finestra dei dettagli.
 *	Permette di nascondere il pulsane "Nuovo" della finestra dei dettagli del Buono di Scarico:
 *	restituisce TRUE se il bene della riga di dettagio è un bene accessorio NON è un bene 
 *	accessorio.
 * 
 * @return <code>boolean</code> lo stato del pulsante.
 */
public boolean isBottoneAggiungiBeneEnabled() {
	boolean isButtonVisible =true;
	Inventario_beniBulk bene = (Inventario_beniBulk)getRigheInventarioDaFattura().getModel();
	if(bene!=null && bene.isTotalmenteScaricato())
	  isButtonVisible = (bene != null)?!bene.isBeneAccessorio():true;

	return isInserting() && isButtonVisible;
}
/**
 * Abilito il pulsane "Cancella" della finestra dei dettagli.
 *	Permette di nascondere il pulsane "Cancella" della finestra dei dettagli del Buono di Scarico:
 *	restituisce TRUE se ci sono beni nella collezione dei beni da scaricare.
 *	accessorio.
 * 
 * @return <code>boolean</code> lo stato del pulsante.
 */
public boolean isBottoneEliminaBeneEnabled() {

	// Abilito il bottone se sono in modifica
	
	if(this.isEditable()){
		Buono_carico_scaricoBulk buonoS = (Buono_carico_scaricoBulk)this.getModel();
		if ((buonoS.getBuono_carico_scarico_dettColl() != null) && (buonoS.getBuono_carico_scarico_dettColl().size()!=0))
			return true;
	}
	return false;	// disabilito
}

public boolean isBottoneScaricaBeneEnabled() {
	return true;
}
/**
 * Disabilito il tasto Cancella, in quanto NON è possibile cancellare un Buono di Scarico
 * 	una volta creato.
 * 
 * @return <code>boolean</code> lo stato del pulsante.
 */
public boolean isDeleteButtonEnabled() {
	if (isVisualizzazione())
		return false;	
	/*Inventario_beniBulk dettScarico = (Inventario_beniBulk)getDettController().getModel();
	if ((dettScarico != null)&& (dettScarico.getFl_totalmente_scaricato()!=null)&&(dettScarico.getFl_totalmente_scaricato().booleanValue())||isBy_fattura())
		return false; // disabilito
	else */
		return true;// abilito	
}
/**
 * Nascondo il tasto Cancella, in quanto NON è possibile cancellare un Buono di Scarico
 * 	una volta creato.
 * 
 * @return <code>boolean</code> lo stato del pulsante.
 */
public boolean isDeleteButtonHidden() {
	if (isBy_fattura()||isBy_documento())
		return true;
	return false;
}

public boolean isFlagScaricoTotaleRO() {
	if (isInserting()) 
		return false;
	else
		return true;
}

public boolean isModValore_unitario(){
	return isNonIniziatoAmmortamento() && (!isValoreScaricatoRO());
		//rp 15/12/2006 abilitati tutti alla modifica
		//&& isAmministratore();	      
}
public boolean isNonIniziatoAmmortamento() {

	Buono_carico_scarico_dettBulk dett = (Buono_carico_scarico_dettBulk)getEditDettController().getModel();
	if (dett!=null &&  dett.getBene().getValore_ammortizzato() != null){
		return  dett.getBene().getValore_ammortizzato().compareTo(new BigDecimal(0))==0 ;//&& dett.getBene().isCancellabile();
	}

	return false;
}
/**
 * Disabilita il campo relativo alla proprietà <i>Valore Scaricato</i>.
 *	Restituisce TRUE, (il campo viene disabilitato), se ho scelto di scaricare il bene in 
 *	modo totale.
 *
 * @return <code>boolean</code> lo stato del campo.
**/
public boolean isValoreScaricatoRO() {

	// Disabilito il campo se ho scelto Scarico Totale
			Inventario_beniBulk dettScarico = (Inventario_beniBulk)getDettController().getModel();
		if ((dettScarico != null)&&(dettScarico.getFl_totalmente_scaricato()!=null)&&(dettScarico.getFl_totalmente_scaricato().booleanValue()))
			return true; // disabilito
		else
			return false;	// abilito
}


public void rimuoviDettagliDaScaricare(it.cnr.jada.UserContext userContext, Selection sel) throws it.cnr.jada.comp.ComponentException{

	Buono_carico_scaricoBulk buonoS = (Buono_carico_scaricoBulk)getModel();
	for (SelectionIterator i = sel.reverseIterator();i.hasNext();) {
		Inventario_beniBulk dettScarico = (Inventario_beniBulk)buonoS.getBuono_carico_scarico_dettColl().remove(i.nextIndex());
		setDirty(true);
	}
}




/**
 * Insert the method's description here.
 * Creation date: (12/09/2002 11.06.12)
 * @param newClauses it.cnr.jada.persistency.sql.CompoundFindClause
 */
public void setClauses(it.cnr.jada.persistency.sql.CompoundFindClause newClauses) {
	clauses = newClauses;
}

/**
 * Valida i dettagli del Buono di Scarico.
 *	L'utente sta creando un Buono di Scarico: il metodo controlla che tutti i campi obbligatori
 *	siano stati correttamente inseriti.
 *	Le proprietà che vengono controllate sono:
 *	<dl>
 *	<dt><b>Valore da scaricare</b>
 *	<dd>Controlla che sia stato inserito il valore dello scarico e che il valore inserito sia 
 *		congruente con il valore del bene. Se il bene è totalmente scaricato, il controllo 
 *		non viene eseguito, in quanto il valore da scaricare sarà il valore del bene stesso.  
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta 
 * @param bulk il <code>OggettoBulk</code> bene scaricato.
**/ 
public void validaDettaglioScarico(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws ValidationException{

	Inventario_beniBulk bene_scaricato = (Inventario_beniBulk)bulk;
	if (isInserting()){
		if (!bene_scaricato.getFl_totalmente_scaricato().booleanValue()){
			
			// Controllo che sia stato specificato un valore nel campo 'Valore Da Scaricare'
			if (bene_scaricato.getValore_unitario() == null || bene_scaricato.getValore_unitario().compareTo(new java.math.BigDecimal(0))==0)
				throw new ValidationException("Attenzione: specificare un valore da Scaricare");
				
			
			// Controllo sul valore da scaricare 
			int result = bene_scaricato.getValore_unitario().compareTo(bene_scaricato.getValoreBene());

			// valore da scaricare > valore bene
			if (result >0){
				throw new ValidationException("Attenzione: il valore indicato nel campo 'Valore Scaricato' del bene nr. " + bene_scaricato.getNumeroBeneCompleto() + "\n non può essere superiore al valore del bene stesso");
			}

			// valore da scaricare = valore bene - questa operazione porta a 0 il valore del bene: 
			//	il bene deve essere scaricato totalmente
			if (result == 0){
				throw new ValidationException("Attenzione: la variazione per il bene nr. " + bene_scaricato.getNumeroBeneCompleto() + "\n porta il valore del bene a 0.\nQuesta operazione è possibile solo scaricando totalmente il bene");
			}


			// Buono da Fattura: controllo che sia stato specif. un Valore Alienazione
			if (isBy_fattura() && !bene_scaricato.isBeneAccessorio() && bene_scaricato.getValore_alienazione()==null){
				throw new ValidationException("Attenzione: specificare un Valore Alienazione");
			}
		}
/* da verificare*/
		try {
			((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeneScaricato(
				context.getUserContext(),
				(Buono_carico_scaricoBulk)getModel(),
				bene_scaricato,
				null);
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw new ValidationException();
		} catch(java.rmi.RemoteException e) {
			throw new ValidationException();
		} catch (BusinessProcessException e){
			throw new ValidationException();
		}
		}
}

/**
 * E' stata generata la richiesta di cercare i beni disponibili per una operazione d Scarico.
 *	Vengono invocati due diversi metodi della Component <i>BuonoScaricoComponent</i>, a seconda
 *	che il Buono di Scarico sia creato da una Fattura Attiva oppure no:
 *		- invoca il metodo <code>BuonoScaricoComponent.cercaBeniAssociabili</code>, se 
 *			proviene da Fattura Attiva;
 *		- invoca il metodo <code>BuonoScaricoComponent.cercaBeniScaricabili</code>, altrimenti.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 * @param no_accessori <code>boolean</code> indica se escludere i beni accessori dalla ricerca
 * @param beni_da_escludere <code>SimpleBulkList</code> i beni da escludere dalla ricerca, magari perchè già selezionati
 * @param clauses <code>CompoundFindClause</code> le clausole della selezione.
 *
 * @return l'Iteratore <code>RemoteIterator</code> sui beni trovati.
**/
public it.cnr.jada.util.RemoteIterator getListaBeniDaScaricare(
	it.cnr.jada.UserContext userContext, 
	boolean no_accessori, 
	SimpleBulkList beni_da_escludere,
	it.cnr.jada.persistency.sql.CompoundFindClause clauses) throws BusinessProcessException, java.rmi.RemoteException, it.cnr.jada.comp.ComponentException {
	if (isBy_fattura()){
		if (getDettagliFattura().getModel() instanceof Fattura_attiva_rigaIBulk) {
			Fattura_attiva_rigaIBulk riga_fattura = (Fattura_attiva_rigaIBulk)getDettagliFattura().getModel();	
			return ((BuonoCaricoScaricoComponentSession)createComponentSession()).cercaBeniAssociabili(userContext, (Buono_carico_scaricoBulk)this.getModel(), riga_fattura, clauses);
		}else{
			Nota_di_credito_rigaBulk nota = (Nota_di_credito_rigaBulk)getDettagliFattura().getModel();	
			return ((BuonoCaricoScaricoComponentSession)createComponentSession()).cercaBeniAssociabili(userContext, (Buono_carico_scaricoBulk)this.getModel(), nota, clauses);
			
		}
	}else if (isBy_documento()){
		return ((BuonoCaricoScaricoComponentSession)createComponentSession()).cercaBeniAssociabili(userContext, (Buono_carico_scaricoBulk)this.getModel(), (Documento_generico_rigaBulk)getDettagliDocumento().getModel(), clauses);
	}
	else{
		return ((BuonoCaricoScaricoComponentSession)createComponentSession()).getListaBeniDaScaricare(userContext, (Buono_carico_scaricoBulk)this.getModel(), no_accessori,beni_da_escludere, clauses);
	}
}
/**
 * Cerca tutti i dettagli di un Buono di Scarico.
 *	E' stata generata la richiesta di visualizzare i dettagli di un Buono di Scarico, per una 
 *	operazione di "Modifica".
 *	Le operazioni di ricerca dei dettagli di un Buono di Scarico differiscono a seconda che si
 *	stiano effettuando operazioni di "Creazione", (solo la prima volta), piuttosto che di "Modifica".
 *	Invoca il metodo <code>BuonoScaricoComponent.selectEditDettagliScarico</code>
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta.
 *
 * @return <code>RemoteIterator</code> l'Iteratore sui beni.
**/
private it.cnr.jada.util.RemoteIterator selectEditDettagliScaricobyClause(ActionContext context) throws BusinessProcessException{

	
	try {
		it.cnr.jada.persistency.sql.CompoundFindClause clauses = ((RemoteDetailCRUDController)getEditDettController()).getFilter();
		it.cnr.jada.UserContext userContext = context.getUserContext();
		Buono_carico_scaricoBulk buonoS = (Buono_carico_scaricoBulk)getModel();		
		Class bulkClass = Buono_carico_scarico_dettBulk.class;

		
		return ((BuonoCaricoScaricoComponentSession)createComponentSession()).selectEditDettagliScarico(userContext,buonoS,bulkClass,clauses);		
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 
}
public String getLabelData_registrazione(){
	return "Data Scarico";
}
/** 
 * E' stata generata la richiesta di effettuare uno scarico totale di un bene.
 *	Controlla che non siano presenti nella lista dei beni scaricati, beni accessori del 
 *	bene selezionato. Viene invocato il metodo <code>BuonoScaricoComponent.checkBeniAccessoriAlreadyExistFor</code>
 * 
 * @param context la <code>ActionContext</code> che ha generato la richiesta.
 * @param buonoS <code>Buono_scaricoBulk</code> il Buono di Scarico.
 * @param bene_padre il <code>Inventario_beniBulk</code> bene da scaricare totalmente.
**/
public void checkBeniAccessoriAlreadyExistFor(ActionContext context,Buono_carico_scaricoBulk buonoS, Inventario_beniBulk bene_padre) throws BusinessProcessException {
	try {
		((BuonoCaricoScaricoComponentSession)createComponentSession()).checkBeniAccessoriAlreadyExistFor(
			context.getUserContext(),
			(Buono_carico_scaricoBulk)getModel(),
			bene_padre);
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch (BusinessProcessException e){
		throw handleException(e);
	}	
}
/**
 *	E' stata generata la richiesta di scaricare totalmente, (cancellazione logica), un bene
 *	dall'Inventario.
 *	Invoca i metodi:
 *		- <i>BuonoCaricoScaricoComponent.scaricaBeniAccessoriFor</i>, (solo se scarica_accessori è TRUE);
 *		- <i>BuonoCaricoScaricoComponent.modificaBeneScaricato</i>.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param bene il <code>Inventario_beniBulk</code> bene 	
 * @param selected_righe_fatt <code>List</code> le righe di Fattura Attiva alle quali è eventualmente associato il bene
 * @param scarica_accessori	<code>boolen</code> indica se scaricare anche i beni accessori
**/
public void scaricoTotale(it.cnr.jada.UserContext userContext, Inventario_beniBulk bene,java.util.List selected_righe_fatt, boolean scarica_accessori) throws BusinessProcessException {

	try {
		Buono_carico_scaricoBulk bulk = (Buono_carico_scaricoBulk)getModel();
		if (scarica_accessori){
			((BuonoCaricoScaricoComponentSession)createComponentSession()).scaricaBeniAccessoriFor(
				userContext,
				bulk,
				bene,
				selected_righe_fatt);
		}
				
		((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeneScaricato(
			userContext,
			bulk,
			bene,
			getDettagliFattura().getModel());
		
		
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch (BusinessProcessException e){
		throw handleException(e);
	}	
	
}
/**
 *	Elimina i beni accessori di un bene di riferimento.
 *	E' stata generata la richiesta di eliminare dal Buono di Scarico i beni accessori di un 
 *	dato bene . 
 *  Invoca il metodo <code>BuonoScaricoComponent.annullaScaricaBeniAccessoriFor</code>.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param bene il <code>Inventario_beniBulk</code> bene di riferimento
 * @param selected_righe_fatt il <code>List</code> le righe di Fattura Attiva alle quali il bene è eventualmente associato.
**/ 
public void rimuoviBeniAccessoriFor(it.cnr.jada.UserContext userContext, Inventario_beniBulk bene_padre, java.util.List selected_righe_fatt) throws it.cnr.jada.comp.ComponentException, BusinessProcessException{

 	try {
		((BuonoCaricoScaricoComponentSession)createComponentSession()).annullaScaricaBeniAccessoriFor(
			userContext,
			(Buono_carico_scaricoBulk)getModel(),
			bene_padre,
			selected_righe_fatt);
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch (BusinessProcessException e){
		throw handleException(e);
	}	
}
/**
 *	Aggiorna un bene sulla tabella temporanea.
 *	E' stata generata la richiesta di aggiornare un bene: la richiesta viene generata in seguito 
 *	ad una modifica fatta dall'utente sul bene che si sta scaricando.
 *  Invoca il metodo <code>BuonoCaricoScaricoComponent.modificaBeneScaricato</code>.
 *
 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
 * @param bene il <code>Inventario_beniBulk</code> bene 
**/
public Buono_carico_scaricoBulk modificaBeneScaricato(it.cnr.jada.UserContext userContext, Inventario_beniBulk bene, OggettoBulk riga) throws BusinessProcessException {

	try {
		Buono_carico_scaricoBulk bulk=(Buono_carico_scaricoBulk)getModel();
		((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeneScaricato(
			userContext,
			bulk,
			bene,
			riga);
		return bulk;
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch (BusinessProcessException e){
		throw handleException(e);
	}	
}
/**
 * 
 */
public void modificaBeneAssociatoConBulk(ActionContext context, OggettoBulk bulk) throws BusinessProcessException{
	try {
		if (isBy_fattura()){
			((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeneAssociatoConBulk(
			context.getUserContext(),
			(Buono_carico_scaricoBulk)getModel(),
			getDettagliFattura().getModel(),
			(Inventario_beniBulk)bulk);
		}else if (isBy_documento()){
			((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeneAssociatoConBulk(
			context.getUserContext(),
			(Buono_carico_scaricoBulk)getModel(),
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
 * Cerca tutti i beni accessori di un Bene.
 *	Invoca il metodo <code>getBeniAccessoriFor</code>.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 * @param benePrincipale il <code>Inventario_beniBulk</code> bene di cui si vogliono conoscere gli accessori.
 *
 * @return la <code>List</code> lista dei beni trovati.
**/
public java.util.List getBeniAccessoriFor(it.cnr.jada.UserContext userContext, Inventario_beniBulk benePrincipale) throws BusinessProcessException, java.rmi.RemoteException, it.cnr.jada.comp.ComponentException{
	
	return ((BuonoCaricoScaricoComponentSession)createComponentSession()).getBeniAccessoriFor(userContext, benePrincipale);
	
}

/**
 * E' stata generata la richiesta di annullare tutte le operazioni effettuate sul Buono di 
 *	Scarico Inventario.
 *	Viene invocato il metodo <code>BuonoScaricoComponent.annullaModificaScaricoBeni</code>
 *
 *	@param context la <code>ActionContext</code> che ha generato la richiesta.
 */
public void clearSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		((BuonoCaricoScaricoComponentSession)createComponentSession()).annullaModificaScaricoBeni(
			context.getUserContext());
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 
}

/* (non-Javadoc)
 * @see it.cnr.jada.util.action.SelectionListener#deselectAll(it.cnr.jada.action.ActionContext)
 */
public void deselectAll(ActionContext actioncontext) {}

/**
 * Restituisce la Selezione fatta sul Controller dei Beni Associati.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta
 * @param beni <code>OggettoBulk[]</code> i beni selezionati dall'utente.
 * @param currentSelection la <code>BitSet</code> selezione attuale.
 *
 * @return currentSelection la <code>BitSet</code> selezione attuale.
**/
public java.util.BitSet getSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet currentSelection) {
	
	return currentSelection;
}
/**
 * initializeSelection method comment.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta
**/ 
public void initializeSelection(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
	
		((BuonoCaricoScaricoComponentSession)createComponentSession()).inizializzaBeniDaScaricare(
			context.getUserContext()
			);
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} 
}
public void selectAll(ActionContext actioncontext) throws BusinessProcessException {
	java.util.List righe_fattura = null;
	try {
		if (isBy_fattura()){
			righe_fattura = getDettagliFattura().getSelectedModels(actioncontext);
		}else if (isBy_documento())		
			righe_fattura = getDettagliDocumento().getSelectedModels(actioncontext);
		((BuonoCaricoScaricoComponentSession)createComponentSession()).scaricaTuttiBeni(
				actioncontext.getUserContext(),
			(Buono_carico_scaricoBulk)getModel(),
			righe_fattura,
			getClauses());
		setClauses(null);
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}catch(ValidationException e) {
		throw handleException(e);
	}	
}
public void scaricaTuttiDef(ActionContext context) throws ComponentException, RemoteException, BusinessProcessException{
((it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)createComponentSession()).scaricaTuttiBeniDef(context.getUserContext(), (Buono_carico_scaricoBulk)getModel());
}
public BitSet setSelection(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk[] bulks, java.util.BitSet oldSelection, java.util.BitSet newSelection) throws it.cnr.jada.action.BusinessProcessException {
	try {
		if (isBy_fattura()){
			((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeniScaricatiPerAssocia(
				context.getUserContext(),
			   (Buono_carico_scaricoBulk)getModel(),
				getDettagliFattura().getSelectedModels(context),
				bulks,
				oldSelection,
				newSelection);
		}else if(isBy_documento()){
				((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeniScaricatiPerAssocia(
					context.getUserContext(),
				   (Buono_carico_scaricoBulk)getModel(),
					getDettagliDocumento().getSelectedModels(context),
					bulks,
					oldSelection,
					newSelection);			
		}
		else {
			((BuonoCaricoScaricoComponentSession)createComponentSession()).modificaBeniScaricati(
				context.getUserContext(),
				(Buono_carico_scaricoBulk)getModel(),
				bulks,
				oldSelection,
				newSelection);
		}
		return newSelection;
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	} catch (ValidationException e){
		throw handleException(e);
	}
}
/* r.p. Serve per riportare in inserimento la mappa dopo il salvataggio di un trasferimento
* Se il Buono di Scarico è creato da una Fattura Attiva, mantiene traccia
* della PrimaryKeyHashtable che contiene l'associazione tra le righe di Fattura e le righe
* di Inventario ad esse associate.
*
* @param context la <code>ActionContext</code> che ha generato la richiesta
* @param bulk <code>OggettoBulk</code> il Buono di Scarico.
*
* @return bulk <code>OggettoBulk</code> il Buono di Scarico.
**/
protected void basicEdit(it.cnr.jada.action.ActionContext context,OggettoBulk bulk,boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {

if (bulk instanceof Trasferimento_inventarioBulk){
reset(context);
}
else super.basicEdit(context, bulk, doInitializeForEdit);

}

public RemoteDetailCRUDController getEditDettController() {
	return editDettController;
}
/**
 * Restituisce il valore della proprietà 'dettagliFattura'
 *
 * @return <code>SimpleDetailCRUDController</code> il valore della proprietà 'dettagliFattura'
 */
public final SimpleDetailCRUDController getDettagliFattura() {
	return dettagliFattura;
}
/**
 * Restituisce il valore della proprietà 'righeInventarioDaFattura'
 *
 * @return <code>AbstractDetailCRUDController</code> il valore della proprietà 'righeInventarioDaFattura'
 */
public final AbstractDetailCRUDController getRigheInventarioDaFattura() {
	return righeInventarioDaFattura;
}
public final AbstractDetailCRUDController getRigheInventarioDaDocumento() {
	return righeInventarioDaDocumento;
}
/**
 * Valida i dettagli associati alle righe di Fattura:
 * 	controlla che non ci siano beni di tipo diverso sulla stessa riga di Fattura.
 *
 * @param context la <code>ActionContext</code> che ha generato la richiesta 
 * @param bulk la <code>OggettoBulk</code> riga di Fattura.
**/ 
private void validateDettagliDaFattura(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws ValidationException{

	Inventario_beniBulk bene = (Inventario_beniBulk)bulk;
	if (getDettagliFattura().getModel() instanceof Fattura_attiva_rigaIBulk){
		if (!bene.isBeneAccessorio()){
			if (bene.getValore_alienazione_apg() == null || bene.getValore_alienazione_apg().compareTo(new java.math.BigDecimal(0))==0){
				throw new ValidationException("Attenzione: è necessario specificare un Valore Alienazione per ogni bene.\n Specificare un valore per il bene " + bene.getNumeroBeneCompleto());
			}
		}
	}
     else{
			if (!bene.isBeneAccessorio()){
				if (bene.getVariazione_meno() == null || bene.getVariazione_meno().compareTo(new java.math.BigDecimal(0))==0){
					throw new ValidationException("Attenzione: è necessario specificare un Valore di scarico per ogni bene.\n Specificare un valore per il bene " + bene.getNumeroBeneCompleto());
				}
			}	
	}
		
}

public SimpleDetailCRUDController getDettagliDocumento() {
	return dettagliDocumento;
}
private void eliminaBeniAssociatiDocConBulk(ActionContext context) throws it.cnr.jada.action.BusinessProcessException{
	
	
	try {
		if(getDettagliDocumento().getModel()!=null )
			((BuonoCaricoScaricoComponentSession)createComponentSession()).eliminaBeniAssociatiConBulk(
			context.getUserContext(),
			(Buono_carico_scaricoBulk)getModel(),
			getDettagliDocumento().getModel());
		
	} catch(it.cnr.jada.comp.ComponentException e) {
		throw handleException(e);
	} catch(java.rmi.RemoteException e) {
		throw handleException(e);
	}  catch (Throwable t){
		throw handleException(t);
	}
}
private void validateDettagliDaDocumento(it.cnr.jada.action.ActionContext context, it.cnr.jada.bulk.OggettoBulk bulk) throws ValidationException{
	Inventario_beniBulk bene = (Inventario_beniBulk)bulk;
			if (!bene.isBeneAccessorio()){
				if (bene.getVariazione_meno() == null || bene.getVariazione_meno().compareTo(new java.math.BigDecimal(0))==0){
					throw new ValidationException("Attenzione: è necessario specificare un Valore di scarico per ogni bene.\n Specificare un valore per il bene " + bene.getNumeroBeneCompleto());
				}
			}	
	}
private void eliminaBeniAssociatiDocConBulk(ActionContext context,it.cnr.jada.bulk.OggettoBulk[] details) throws it.cnr.jada.action.BusinessProcessException{
	try {
			if (isBy_documento()){
				for (int i = 0;i<details.length;i++){
					Inventario_beniBulk dett = (Inventario_beniBulk)details[i];
					if (dett.isBeneAccessorio() && (dett.isTotalmenteScaricato()))
						throw handleException(new ValidationException("Attenzione: durante l'associazione di un Buono di Scarico ad una Documento generico attivo \n " +
							"Non è possibile eliminare beni accessori.\n Il bene " + dett.getNumeroBeneCompleto() + " non può essere eliminato direttamente."));
				}
			}
			((BuonoCaricoScaricoComponentSession)createComponentSession()).eliminaBeniAssociatiConBulk(
				context.getUserContext(),
				(Buono_carico_scaricoBulk)getModel(),
				details,
				getDettagliDocumento().getModel());
		} catch(it.cnr.jada.comp.ComponentException e) {
			throw handleException(e);
		} catch(java.rmi.RemoteException e) {
			throw handleException(e);
		} 
  }
}
