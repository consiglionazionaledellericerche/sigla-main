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

package it.cnr.contab.fondecon00.bp;

import it.cnr.contab.fondecon00.core.bulk.*;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.SimpleDetailCRUDController;

public class FondoEconomaleBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private final AssociazioniMandatiCRUDController associazioniMandati = 
		new AssociazioniMandatiCRUDController(
									"AssociazioniMandati",
									Ass_fondo_eco_mandatoBulk.class,
									"associazioni_mandati",
									this);
public FondoEconomaleBP() {
	super();
}
public FondoEconomaleBP(String function) {
	super(function);
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 3:10:11 PM)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getAssociazioniMandati() {
	return associazioniMandati;
}
public boolean isNewButtonEnabled() {

	Fondo_economaleBulk fondo = (Fondo_economaleBulk)getModel();
	return super.isNewButtonEnabled() && 
			(fondo != null && !fondo.isOnlyForClose());
}
public void reset(ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	if (it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()).intValue() != 
		Fondo_spesaBulk.getDateCalendar(null).get(java.util.Calendar.YEAR))
		resetForSearch(context);
	else
		super.reset(context);
}
protected void resetTabs(ActionContext context) {

	setTab("tab", "tabFondoEconomale");
	setTab("subtab", "tabFondoEconomaleEconomo");
}
}
