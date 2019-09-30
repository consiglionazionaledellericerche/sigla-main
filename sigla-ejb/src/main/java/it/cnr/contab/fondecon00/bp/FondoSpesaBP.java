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
import it.cnr.contab.fondecon00.ejb.FondoEconomaleComponentSession;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.OggettoBulk;

public class FondoSpesaBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private Fondo_economaleBulk fondoEconomaleCorrente = null;
public FondoSpesaBP() {
	super();
}
public FondoSpesaBP(Fondo_economaleBulk fondo, String function) {
	this(function);
	setFondoEconomaleCorrente(fondo);
}
public FondoSpesaBP(String function) {
	super(function);
}
/**
 * Crea un OggettoBulk vuoto della classe compatibile con la CRUDComponentSession del ricevente
 */
public OggettoBulk createNewBulk(ActionContext context) throws BusinessProcessException {

	Fondo_spesaBulk fs = (Fondo_spesaBulk)super.createNewBulk(context);
	fs.setFondo_economale(getFondoEconomaleCorrente());
	return fs;
}
public OggettoBulk createNewSearchBulk(ActionContext context) throws BusinessProcessException {

	Fondo_spesaBulk fs = (Fondo_spesaBulk)super.createNewSearchBulk(context);
	fs.setFondo_economale(getFondoEconomaleCorrente());
	return fs;
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 2:48:11 PM)
 * @return it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk getFondoEconomaleCorrente() {
	return fondoEconomaleCorrente;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {

	super.init(config,context);
	setStatus(SEARCH);
}
public boolean isDeleteButtonEnabled() {

	Fondo_spesaBulk spesa =	(Fondo_spesaBulk)getModel();
	return super.isDeleteButtonEnabled() &&
			getModel() != null && 
			!spesa.isSpesa_reintegrata();
}
/**
 * Insert the method's description here.
 * Creation date: (04/06/2001 11:45:16)
 * @return boolean
 */
public boolean isInputReadonly() {
	return isEditing();
}
public boolean isNewButtonEnabled() {

	Fondo_spesaBulk spesa = (Fondo_spesaBulk)getModel();
	return super.isNewButtonEnabled() && getModel() != null &&
			spesa.getFondo_economale() != null &&
			!spesa.getFondo_economale().isChiuso();
}
public boolean isSaveButtonEnabled() {

	Fondo_economaleBulk fondo = ((Fondo_spesaBulk)getModel()).getFondo_economale();
	return super.isSaveButtonEnabled() && getModel() != null &&
			getModel().getCrudStatus() != OggettoBulk.NORMAL &&
			fondo != null &&
			!fondo.isChiuso();
}
/**
 * Insert the method's description here.
 * Creation date: (4/23/2002 2:48:11 PM)
 * @param newFondoEconomaleCorrente it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk
 */
public void setFondoEconomaleCorrente(it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk newFondoEconomaleCorrente) {
	fondoEconomaleCorrente = newFondoEconomaleCorrente;
}
}
