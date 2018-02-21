package it.cnr.contab.prevent00.bp;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.prevent00.bulk.Pdg_aggregato_etr_detBulk;
import it.cnr.jada.util.action.AbstractPrintBP;
import it.cnr.jada.util.jsp.*;

/**
 * Gestisce le operazioni CRUD sul dettaglio Entrate del PdG aggregato adattando ed 
 * 		implementando {@link it.cnr.jada.util.action.SimpleCRUDBP }.
 * 					   
 * @author: Vincenzo Bisquadro
 */
public class CRUDEtrDetPdGAggregatoBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private it.cnr.contab.config00.sto.bulk.CdrBulk cdr;
/**
 * Costruttore di CRUDEtrDetPdGAggregatoBP cui viene passata la funzione 
 * 		come parametro.
 *
 * @param function java.lang.String
 */
public CRUDEtrDetPdGAggregatoBP(String function) {
	super(function);
}
/**
 * Costruttore di CRUDEtrDetPdGAggregatoBP cui vingono passati funzione e cdr
 * 		come parametri.
 *
 * @param function java.lang.String
 *
 * @param cdr it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public CRUDEtrDetPdGAggregatoBP(String function, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) {
	this(function);
	setCdr(cdr);
}
/**
 * Visualizza i soli pulsanti "Cerca" e "Salva" (inizialmente disabilitato) nella Toolbar.
 *
 * @return it.cnr.jada.util.jsp.Button[] toolbar
 */
protected it.cnr.jada.util.jsp.Button[] createToolbar() {
	Button[] toolbar = new Button[6];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.search");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.freeSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.save");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.bringBack");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.print");
	return toolbar;
}
/**
 * Restituisce il cdr interessato al dettaglio Entrate. 
 *
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk cdr
 *
 * @see setCdr()
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
	return cdr;
}
protected void initialize(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	if (cdr != null)
		resetForSearch(context);
}
protected void resetTabs(it.cnr.jada.action.ActionContext context) {
	setTab("tabPreventivoSpe","tabEsercizio");
	setTab("tabPreventivoEtr","tabEsercizio");
	setTab("tabCostiSpese","tabCostiConSpese");
	setTab("tabCostiSpese2","tabCosti2");
	setTab("tabCostiSpese3","tabCosti3");
}
/**
 * Setta il centro di responsabilità.
 * 
 * @param newCdr it.cnr.contab.config00.sto.bulk.CdrBulk
 *
 * @ see getCdr()
 */
public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
	cdr = newCdr;
}
}
