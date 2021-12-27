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
 * Un BusinessProcess controller che permette di effettuare operazioni di EDIT su istanze di 
 *	Inventario_beniBulk per la modifica dei Beni presenti in Inventario.
 *	Non è possibile Creare o Cancellare Beni con questo controller, che ha l'unico scopo di
 *	permettere la modifica delle proprietà dei Beni, come ad es. la <I>Categoria</I> di appartenenza
 *	l'<I>Ubicazione</I>, la <I>Descrizione</I>, il <I>Valore Iniziale</I>, gli <I>Utilizzatori</I>, ecc.
 *	Le operazioni di Creazione, (Carico) e Cancellazione, (Scarico), sono competenza di
 *	CRUDCaricoInventarioBP, per il Carico e CRUDScaricoInventarioBP, per lo Scarico.
**/

import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk;
import it.cnr.contab.inventario00.docs.bulk.Transito_beni_ordiniBulk;
import it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.contab.inventario01.ejb.TransactionalBuonoCaricoScaricoComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.action.Config;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.SimpleCRUDBP;
import it.cnr.jada.util.action.SimpleDetailCRUDController;
import it.cnr.jada.util.jsp.Button;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;

public class CRUDTransitoBeniOrdiniBP extends SimpleCRUDBP {
	private boolean isVisualizzazione=false;

	private boolean isPubblicazione = false;

	protected Button[] createToolbar()
	{



		Button[] toolbar = super.createToolbar();
		return toolbar;
	}


public CRUDTransitoBeniOrdiniBP() {
	super("M");
	setTab("tab","tabInventarioBeni");
//	setTab("tabInventarioBeniUtilizzatori","tabInventarioBeniUtilizzatori");
}

public CRUDTransitoBeniOrdiniBP(String function) {
	super(function);
}
/**
  *  Crea una toolbar in aggiunta alla normale toolbar del CRUD.
  *	La nuova toolbar è stata costruita per mostrare il tasto "Visualizza accessori".
  *
  * @return toolbar i <code>Button[]</code> pulsanti creati
**/ 
protected Button[] createViewAccessoriToolbar() {

	Button[] toolbar = new Button[1];
	int i = 0;
//	toolbar[i++] = new Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.viewAccessori");
	return toolbar;
}
/**
 * Restituisce il valore della proprietà 'utilizzatori'
 *
 * @return utilizzatori <code>SimpleDetailCRUDController</code> il valore
 */

protected void init(Config config, ActionContext context) throws BusinessProcessException {

	super.init(config,context);
	resetTabs();
}
/**
 * Reimplementato per consentire di abilitare o disabilitare il campo <I>Collocazione</I>,
 *	in base alla Categoria Gruppo Inventario alla quale appartiene il Bene che si sta modificando.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
 * @param bulk il <code>OggettoBulk</code> Bene che si sta modificando
 *
 * @return bene il <code>OggettoBulk</code> inizializzato per la modifica
**/
public OggettoBulk initializeModelForEdit(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
	
	String cd_pubblicazioni = null;
	try {
		Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk)bulk;
		bulk = super.initializeModelForEdit(context, bulk);

		return bulk;
	} catch(Throwable e) {
		throw new BusinessProcessException(e);
	}
}
	protected void initialize(it.cnr.jada.action.ActionContext context)
			throws it.cnr.jada.action.BusinessProcessException {
		resetForSearch(context);
	}
/**
  * Nascondo il pulsante di "Nuovo", nel form di Gestione del Bene
  *	Non è possibile creare un Bene, se non con un Buono di Carico
  *
  * @return <code>boolean</code> TRUE
**/
public boolean isNewButtonHidden() {
	
	return true;
}
/**
 * Restituisce TRUE se il bene NON è soggetto ad Ammortamento. E' l'utente a decidere, nel
 *	tab "Ammortamento", se un bene è soggetto ad ammortamento oppure no, selezionando
 *	il flag <I>Soggetto ad Ammortamento</I>.
 *
 * @return <code>boolean</code>
 */
public boolean isNonAmmortizzato() {

	Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk)getModel();
	if (bene!=null && bene.getFl_ammortamento() != null){
		return !bene.getFl_ammortamento().booleanValue();
	}
	
	return false;
}
/**
 * Restituisce true se la Categoria Gruppo alla quale appartiene il Bene, NON è soggetta
 *	ad ammortamento (CATEGORIA_GRUPPPO_INVENT.FL_AMMORTAMENTO==FALSE)
 *
 * @return <code>boolean</code>
 */
public boolean isNotAmmortizzabile() {

	Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk)getModel();
	if (bene!=null && bene.getMovimentiMag() != null && bene.getMovimentiMag().getLottoMag().getBeneServizio().getCategoria_gruppo().getFl_ammortamento() != null){
		return !bene.getMovimentiMag().getLottoMag().getBeneServizio().getCategoria_gruppo().getFl_ammortamento().booleanValue();
	}
	
	return true;
}
/**
 * Restituisce TRUE se la Categoria Gruppo Inventario alla quale appartiene il bene è una
 *	Pubblicazione.
 *
 * @return <code>boolean</code>
 */
public boolean isPubblicazione() {
	
	return isPubblicazione;
}
/**
 * Determina se il campo relativo al <code>Valore Iniziale</code> è READONLY.
 * Oltre alle normali condizioni, restituisce <code>true</code>, se il bene proviene da migrazione, (FL_MIGRATO='Y').
 *
 * @return boolean
 */
public boolean isROvaloreIniziale() {

	OggettoBulk bulk = getModel();
	
	if (bulk == null)
		return true;
		
	boolean readonly= isSearching()
					|| isInputReadonly()
					|| (bulk instanceof ROWrapper);
	
	if (bulk instanceof Transito_beni_ordiniBulk){
		Transito_beni_ordiniBulk beneBulk = (Transito_beni_ordiniBulk)bulk;
		
		if (this.isEditing())
			return true;
		else
			return false;
	}

	return readonly;
		
}
public boolean isSaveButtonEnabled() {

	Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk)getModel();

	if (!isViewing() && bene != null){
		return (bene.getCrudStatus() != OggettoBulk.UNDEFINED
			&& bene.getCrudStatus() != OggettoBulk.TO_BE_CREATED);
	}

	return false;
}
/**
  * Abilito il pulsante di "Visualizza accessori", nel form di Gestione del Bene.
  *	Restituisce TRUE se il bene NON è a sua volta un bene accessorio.
  *
  * @return <code>boolean</code> TRUE
**/
public boolean isViewAccessoriButtonEnabled() {

	Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk) getModel();

    return false;
}
public boolean isDettagliButtonEnabled() {

	Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk) getModel();

	return false;
}
public boolean isFattura_collButtonEnabled() {

	Transito_beni_ordiniBulk bene = (Transito_beni_ordiniBulk) getModel();
	
//	if (bene != null && bene.getNr_inventario() != null && bene.getDa_fattura()!=null )
//	  if (bene.getDa_fattura().booleanValue())
//		return true;
	return false;
}

public void resetTabs() {
	setTab("tab","tabInventarioBeniTestata");
}
/**
 * Imposta il valore della proprietà 'isPubblicazione'
 *
 * @param newIsPubblicazione <code>boolean</code> il nuovo valore
 */
public void setIsPubblicazione(boolean newIsPubblicazione) {
	
	 isPubblicazione = newIsPubblicazione;	 
}
/**
 * Metodo invocato dal SimpleDetailCRUDController vUtilizzatori quando si passa
 * da un CdR utilizzatore all'altro.
 * Controlla se la percentuale di utilizzo delle linee di attività, per 
 * CdR, è valida.
 *
 * @param context il <code>ActionContext</code> che contiene le informazioni relative alla richiesta
**/
public void validate_Percentuali_LA(ActionContext context,OggettoBulk model) throws ValidationException {

	Utilizzatore_CdrVBulk cdrUtilizzatore = (Utilizzatore_CdrVBulk)model;

	SimpleBulkList utilizzatori_LA = cdrUtilizzatore.getBuono_cs_utilizzatoriColl();
	java.math.BigDecimal percentuale_utilizzo_LA = new java.math.BigDecimal("0");
	Vector cdr = new Vector();
	java.math.BigDecimal cento = new java.math.BigDecimal(100);
	if (utilizzatori_LA.size()>0){
		Iterator i = utilizzatori_LA.iterator();
		while (i.hasNext()){
			Inventario_utilizzatori_laBulk utilizzatore_LA = (Inventario_utilizzatori_laBulk)i.next();
			
			// Controlla che sia stata specificata la line di Attività
			if (utilizzatore_LA.getLinea_attivita()==null || utilizzatore_LA.getLinea_attivita().getCd_linea_attivita() == null){
				throw new ValidationException ("GAE non valido. Indicare sia il codice del GAE, sia la sua percentuale di utilizzo");
			}
			
			// Controlla che non vi siano Linee di Attività DUPLICATE
			if (BulkCollections.containsByPrimaryKey(cdr,utilizzatore_LA.getLinea_attivita())){
				throw new ValidationException ("GAE duplicato. Non è possibile indicare più volte uno stesso GAE");
			}
			else {
				cdr.add(utilizzatore_LA.getLinea_attivita());
			}
			
			// Controlla che sia stata indicata una PERCENTUALE DI UTILIZZO VALIDA per Linea di Attività
			if (utilizzatore_LA.getPercentuale_utilizzo_la()!=null){
				percentuale_utilizzo_LA = percentuale_utilizzo_LA.add(utilizzatore_LA.getPercentuale_utilizzo_la());
			}
			else{
				throw new ValidationException ("La percentuale di utilizzo per i GAE non può essere nulla");
			}
		}

		// Controlla che il totale delle percentuali di utilizzo delle Linee di Attività sia 100
		if (percentuale_utilizzo_LA.compareTo(cento)!=0)
			throw new ValidationException ("La percentuale di utilizzo per i GAE non è valida");
	}
}
/**
 * Aggiunge alla normale toolbar di CRUD, la toolbar per la visualizzazione dei beni 
 *	accessori, (v. metodo createViewAccessoriToolbar()).
 *
 * @param writer <code>JspWriter</code>
**/
public void writeToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException,javax.servlet.ServletException {
	
	//super.writeToolbar(writer);
	openToolbar(writer);
	it.cnr.jada.util.jsp.JSPUtils.toolbar(writer,createToolbar(),this, this.getParentRoot().isBootstrap());
	closeToolbar(writer);
//	writeViewAccessoriToolbar(writer);
	
}
/**
 * Scrive la toolbar contenente il tasto di "Visualizza accessori"
 *
 * @param writer <code>JspWriter</code>
**/
public void writeViewAccessoriToolbar(javax.servlet.jsp.JspWriter writer) throws java.io.IOException,javax.servlet.ServletException {

	if (!isSearching()) {
		openToolbar(writer);
		it.cnr.jada.util.jsp.JSPUtils.toolbar(writer,createViewAccessoriToolbar(),this, this.getParentRoot().isBootstrap());
		closeToolbar(writer);
	}
}

public boolean isVisualizzazione() {
	return isVisualizzazione;
}

public void setVisualizzazione(boolean b) {
	isVisualizzazione = b;
}
public boolean isEditable() {
		return !isVisualizzazione()&&super.isEditable();
}
}
