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
import java.rmi.RemoteException;
import java.util.*;
 
import it.cnr.contab.inventario00.docs.bulk.*;
import it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.jsp.Button;
 
public class CRUDInventarioBeniBP extends SimpleCRUDBP {
	private boolean isVisualizzazione=false;
	
	private boolean isPubblicazione = false;

	private final SimpleDetailCRUDController vUtilizzatori = new SimpleDetailCRUDController("VUtilizzatori",Utilizzatore_CdrVBulk.class,"v_utilizzatoriColl",this){
		public void validate(ActionContext context,OggettoBulk model) throws ValidationException {
			validate_Percentuali_LA(context,model);
		}
	};
			
	protected it.cnr.jada.util.jsp.Button[] createToolbar() 
	{
		
		

		Button[] toolbar = super.createToolbar();
		Button[] newToolbar = new Button[ toolbar.length + 2];
		int i;
		for ( i = 0; i < toolbar.length; i++ )
			newToolbar[i] = toolbar[i];
		newToolbar[ i ] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.Dettagli");
		newToolbar[ i +1] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.Fattura_coll");
		return newToolbar;
	}
	private final SimpleDetailCRUDController utilizzatori = new SimpleDetailCRUDController("Utilizzatori",Inventario_utilizzatori_laBulk.class,"buono_cs_utilizzatoriColl",vUtilizzatori);	
	

public CRUDInventarioBeniBP() {
	super();
	setTab("tab","tabInventarioBeni");
	setTab("tabInventarioBeniUtilizzatori","tabInventarioBeniUtilizzatori");
	setTab("tabInventarioBeniProprieta","tabInventarioBeniProprieta");
	setTab("tabInventarioBeniAmmortamento","tabInventarioBeniAmmortamento");
}

public CRUDInventarioBeniBP(String function) {
	super(function);
}
/**
  *  Crea una toolbar in aggiunta alla normale toolbar del CRUD.
  *	La nuova toolbar è stata costruita per mostrare il tasto "Visualizza accessori".
  *
  * @return toolbar i <code>Button[]</code> pulsanti creati
**/ 
protected it.cnr.jada.util.jsp.Button[] createViewAccessoriToolbar() {

	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[1];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.viewAccessori");	
	return toolbar;
}
/**
 * Restituisce il valore della proprietà 'utilizzatori'
 *
 * @return utilizzatori <code>SimpleDetailCRUDController</code> il valore
 */
public final SimpleDetailCRUDController getUtilizzatori() {
	return utilizzatori;
}
/**
 * Restituisce il valore della proprietà 'vutilizzatori'
 *
 * @return Il valore della proprietà 'vutilizzatori'
 */
public final SimpleDetailCRUDController getVutilizzatori() {
	return vUtilizzatori;
}
/**
 * Imposta come attivi i tab di default.
 *
 * @param context <code>ActionContext</code>
 */

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	resetTabs();
	try {	
		BuonoCaricoScaricoComponentSession session = (BuonoCaricoScaricoComponentSession)createComponentSession("CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",BuonoCaricoScaricoComponentSession.class);
		setVisualizzazione(session.isEsercizioCOEPChiuso(context.getUserContext()));			
	} catch (ComponentException e) {
		throw handleException(e);
	} catch (RemoteException e) {
		throw handleException(e);
	}			
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
		Inventario_beniBulk bene = null;
		bulk = super.initializeModelForEdit(context, bulk);
		if (bulk instanceof ROWrapper){
			bene = (Inventario_beniBulk)((ROWrapper)bulk).getBulk();
		} else {
			bene = (Inventario_beniBulk)bulk;
		}
			
		//Inventario_beniBulk bene = (Inventario_beniBulk)
		
		it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk categoria_gruppo = bene.getCategoria_Bene();
		cd_pubblicazioni = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(context.getUserContext(), new Integer(0), "*", "CD_CATEGORIA_GRUPPO_SPECIALE", "PUBBLICAZIONI");
		if(cd_pubblicazioni != null && categoria_gruppo.getCd_categoria_padre()!=null){
			//setIsPubblicazione(categoria_gruppo.getCd_categoria_padre().equalsIgnoreCase(cd_pubblicazioni));
			bene.setPubblicazione(categoria_gruppo.getCd_categoria_padre().equalsIgnoreCase(cd_pubblicazioni));
		}
		else bene.setPubblicazione(false);
		
		return bulk;
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
/**
  * Nascondo il pulsante di "Elimina", nel form di Gestione del Bene
  *	Non è possibile cancellare un Bene presente sul DB, se non <I>scaricandolo totalmente</I>,
  *	con un Buono di Scarico.
  *
  * @return <code>boolean</code> TRUE
**/
public boolean isDeleteButtonHidden() {

	return true;
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

	Inventario_beniBulk bene = (Inventario_beniBulk)getModel();
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

	Inventario_beniBulk bene = (Inventario_beniBulk)getModel();
	if (bene!=null && bene.getCategoria_Bene() != null && bene.getCategoria_Bene().getFl_ammortamento() != null){
		return !bene.getCategoria_Bene().getFl_ammortamento().booleanValue();
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
	
	if (bulk instanceof Inventario_beniBulk){
		Inventario_beniBulk beneBulk = (Inventario_beniBulk)bulk;
		
		if (this.isEditing())
			return true;
		else
			return false;
	}

	return readonly;
		
}
public boolean isSaveButtonEnabled() {

	Inventario_beniBulk bene = (Inventario_beniBulk)getModel();

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

    Inventario_beniBulk bene = (Inventario_beniBulk) getModel();

    if (bene != null && bene.getNr_inventario() != null)
        return !bene.isBeneAccessorio();

    return false;
}
public boolean isDettagliButtonEnabled() {

	Inventario_beniBulk bene = (Inventario_beniBulk) getModel();

	if (bene != null && bene.getNr_inventario() != null && bene.getHa_dettagli() !=null)
	  if (bene.getHa_dettagli().booleanValue())
		return true;
	return false;
}
public boolean isFattura_collButtonEnabled() {

	Inventario_beniBulk bene = (Inventario_beniBulk) getModel();
	
	if (bene != null && bene.getNr_inventario() != null && bene.getDa_fattura()!=null )
	  if (bene.getDa_fattura().booleanValue())
		return true;
	return false;
}

/**
 * Imposta come attivi i tab di default.
 *
 * @param context <code>ActionContext</code>
 */

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
 * @param bulk il <code>OggettoBulk</code> Bene che si sta modificando
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
	writeViewAccessoriToolbar(writer);
	
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
