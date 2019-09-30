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

import java.util.Optional;
import java.util.TreeMap;

import it.cnr.contab.gestiva00.core.bulk.Liquidazione_ivaBulk;
import it.cnr.contab.gestiva00.core.bulk.Liquidazione_massa_ivaVBulk;
import it.cnr.contab.gestiva00.core.bulk.Stampa_registri_ivaVBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class LiquidazioneMassaIvaBP extends LiquidazioneIvaBP {

	private int status = SEARCH;
	private final SimpleDetailCRUDController UoLiquidazioniProvvisorie = new SimpleDetailCRUDController("Uo con Liquidazioni Provvisorie", Liquidazione_ivaBulk.class,"liquidazioniProvvisorie",this);
	private final SimpleDetailCRUDController UoLiquidazioniDefinitive = new SimpleDetailCRUDController("Uo con Liquidazioni Definitive", Liquidazione_ivaBulk.class,"liquidazioniDefinitive",this);

	public LiquidazioneMassaIvaBP() {
	this("");
}
public LiquidazioneMassaIvaBP(String function) {
	super(function+"Tr");
}
public Liquidazione_massa_ivaVBulk aggiornaProspetti(ActionContext context,Liquidazione_massa_ivaVBulk bulk) throws BusinessProcessException {
	try {
		bulk.setProspetti_stampati(createComponentSession().selectProspetti_stampatiByClause(context.getUserContext(),bulk,new Liquidazione_ivaBulk(),null));
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}
public Stampa_registri_ivaVBulk aggiornaRegistriStampati(
	ActionContext context,
	Stampa_registri_ivaVBulk model)
	throws Throwable {
	
	return model;
}
/**
 * Invocato per creare un modello vuoto da usare su una nuova richiesta di ricerca.
 */
public Liquidazione_massa_ivaVBulk createEmptyModelForSearch(ActionContext context) throws BusinessProcessException {

	try {
		return createNewBulk(context);
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public Liquidazione_massa_ivaVBulk createNewBulk(ActionContext context) throws BusinessProcessException {
	try {
		Liquidazione_massa_ivaVBulk bulk = new Liquidazione_massa_ivaVBulk();
		bulk.setUser(context.getUserInfo().getUserid());
		bulk = (Liquidazione_massa_ivaVBulk)bulk.initializeForSearch(this,context);
		
		return bulk;
	} catch(Exception e) {
		throw handleException(e);
	}
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
	setTab("tab", "tabUoLiqProvvisorie");
	resetForSearch(context);
}
public boolean isBulkPrintable() {
	
	return false;
}
public boolean isPrintButtonEnabled() {

	return false;
}
public boolean isPrintButtonHidden() {

	return getPrintbp() == null;
}
public boolean isStartSearchButtonEnabled() {

	return true;
}
/**
 * Inzializza il ricevente nello stato di SEARCH.
 */
public void resetForSearch(it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	try {
		setModel(context,createEmptyModelForSearch(context));
		setStatus(SEARCH);
		resetChildren(context);
	} catch(Throwable e) {
		throw new it.cnr.jada.action.BusinessProcessException(e);
	}
}
public boolean isTabLiquidazioniVisible() {
	return isTabLiquidazioniProvvisorieVisible() || isTabLiquidazioniDefinitiveVisible();
}

public boolean isTabLiquidazioniProvvisorieVisible() {
	Liquidazione_massa_ivaVBulk model = (Liquidazione_massa_ivaVBulk)this.getModel();
  	return model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null &&
		   model.getMese().equals(model.getNextMeseForLiquidazioneDefinitiva());
}

public boolean isTabLiquidazioniDefinitiveVisible() {
	Liquidazione_massa_ivaVBulk model = (Liquidazione_massa_ivaVBulk)this.getModel();
	return (model!=null && model.isLiquidazione_commerciale() && model.getMese()!=null	&&
			model.getLiquidazioniDefinitive()!=null && !model.getLiquidazioniDefinitive().isEmpty());
}

public String[][] getTabs() {
	TreeMap<Integer, String[]> hash = new TreeMap<Integer, String[]>();
	int i=0;

	if (isTabLiquidazioniProvvisorieVisible())
		hash.put(i++, new String[]{ "tabUoLiqProvvisorie", "Provvisorie", "/gestiva00/tab_uo_liqprv.jsp" });
	if (isTabLiquidazioniDefinitiveVisible())
		hash.put(i++, new String[]{"tabUoLiqDefinitive", "Definitive","/gestiva00/tab_uo_liqdef.jsp" });
	
	String[][] tabs = new String[i][3];
	for (int j = 0; j < i; j++) {
		tabs[j]=new String[]{hash.get(j)[0],hash.get(j)[1],hash.get(j)[2]};
	}
	return tabs;		
}
public SimpleDetailCRUDController getUoLiquidazioniProvvisorie() {
	return UoLiquidazioniProvvisorie;
}
public SimpleDetailCRUDController getUoLiquidazioniDefinitive() {
	return UoLiquidazioniDefinitive;
}
public void inizializzaMese(ActionContext context) throws BusinessProcessException {
	try {
		Liquidazione_massa_ivaVBulk model = (Liquidazione_massa_ivaVBulk)this.getModel();
		this.aggiornaProspetti(context,model);
		this.setModel(context, Utility.createLiquidIvaInterfComponentSession().inizializzaMese(context.getUserContext(), model));
		if (isTabLiquidazioniProvvisorieVisible())
			setTab("tab", "tabUoLiqProvvisorie");
		else if (isTabLiquidazioniDefinitiveVisible())
			setTab("tab", "tabUoLiqDefinitive");
	} catch(Exception e) {
		throw handleException(e);
	}	
}
}
