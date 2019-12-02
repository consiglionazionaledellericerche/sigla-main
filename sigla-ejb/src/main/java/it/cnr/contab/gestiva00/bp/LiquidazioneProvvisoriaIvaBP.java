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

package it.cnr.contab.gestiva00.bp;
import it.cnr.contab.gestiva00.ejb.*;
import it.cnr.contab.gestiva00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.*;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class LiquidazioneProvvisoriaIvaBP extends LiquidazioneIvaBP {

	private int status = SEARCH;
	//private final SimpleDetailCRUDController dettaglio = new SimpleDetailCRUDController("liquidazione_iva", Liquidazione_ivaBulk.class,"liquidazione_iva",this);	

public LiquidazioneProvvisoriaIvaBP() {
	this("");
}

public LiquidazioneProvvisoriaIvaBP(String function) {
	super(function+"Tr");
}

/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Liquidazione_provvisoria_ivaVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		return createNewBulk(context);
	} catch(Exception e) {
		throw handleException(e);
	}
}

/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Liquidazione_provvisoria_ivaVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Liquidazione_provvisoria_ivaVBulk bulk = new Liquidazione_provvisoria_ivaVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		bulk = (Liquidazione_provvisoria_ivaVBulk)bulk.initializeForSearch(this,context);
		setLiquidato(false);

		
		//bulk.setTipi_sezionali(createComponentSession().selectTipi_sezionaliByClause(context.getUserContext(),bulk,new it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk(),null));
		//bulk.setSezionali(createComponentSession().selectSezionaliByClause(context.getUserContext(),bulk,new it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk(),null));

		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}

protected it.cnr.jada.util.jsp.Button[] createToolbar() {
	it.cnr.jada.util.jsp.Button[] toolbar = new it.cnr.jada.util.jsp.Button[2];
	int i = 0;
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.startSearch");
	toolbar[i++] = new it.cnr.jada.util.jsp.Button(it.cnr.jada.util.Config.getHandler().getProperties(getClass()),"CRUDToolbar.reset");
	return toolbar;
}

protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
	resetTabs();
	resetForSearch(context);
}

/**
 * Inzializza il ricevente nello stato di SEARCH.
 */
public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setModel(context,createEmptyModelForSearch(context));
		setStatus(SEARCH);
		setDirty(false);
		resetChildren(context);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}

/**
	 * Imposta come attivi i tab di default.
	 *
	 * @param context <code>ActionContext</code>
	 */

public void resetTabs() {
    setTab("tab", "tabEsigDetr"); 
}
}