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

package it.cnr.contab.prevent00.bp;

import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.prevent00.bulk.Pdg_aggregato_spe_detBulk;
import it.cnr.jada.util.action.AbstractPrintBP;
import it.cnr.jada.util.jsp.*;

/**
 * Gestisce le operazioni CRUD sul dettaglio Spese del PdG aggregato adattando ed 
 * 		implementando it.cnr.jada.util.action.SimpleCRUDBP.
 * 					   
 * @author: Vincenzo Bisquadro
 */
public class CRUDSpeDetPdGAggregatoBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private it.cnr.contab.config00.sto.bulk.CdrBulk cdr;

/**
 * Costruttore di CRUDSpeDetPdGAggregatoBP cui viene passata la funzione in ingresso.
 *
 * @param function java.lang.String
 */
public CRUDSpeDetPdGAggregatoBP(String function) {
	super(function);
}
/**
 * Costruttore di CRUDSpeDetPdGAggregatoBP cui vengono passati funzione e cdr in ingresso.
 *
 * @param function java.lang.String
 *
 * @param cdr it.cnr.contab.config00.sto.bulk.CdrBulk
 */
public CRUDSpeDetPdGAggregatoBP(String function, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) {
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
 * Restituisce il cdr interessato al dettaglio Spese cui afferiscono i PdG in questione. 
 *
 * @return it.cnr.contab.config00.sto.bulk.CdrBulk
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
	setTab("tabCostiSpese","tabCostiConSpese");
	setTab("tabCostiSpese2","tabCosti2");
	setTab("tabCostiSpese3","tabCosti3");
}

/**
 * Setta il centro di responsabilità cui afferiscono i PdG in questione.
 * 
 * @param newCdr it.cnr.contab.config00.sto.bulk.CdrBulk
 *
 * @ see getCdr()
 */
public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
		cdr = newCdr;
	}

}
