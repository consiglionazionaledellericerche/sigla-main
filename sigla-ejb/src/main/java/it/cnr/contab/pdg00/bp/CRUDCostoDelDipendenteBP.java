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

package it.cnr.contab.pdg00.bp;

import it.cnr.contab.pdg00.cdip.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

/**
 * Business Process per la gestione dei Costi del dipendente (Pannello di modifica dei costi originali non ripartiti del dipedente)
 */

public class CRUDCostoDelDipendenteBP extends it.cnr.jada.util.action.SimpleCRUDBP {
	private final SimpleDetailCRUDController costi_per_elemento_voce = new SimpleDetailCRUDController("costi_per_elemento_voce",Costo_del_dipendenteBulk.class,"costi_per_elemento_voce",this) {
		protected void validate(ActionContext context,OggettoBulk bulk) throws ValidationException {
			((Costo_del_dipendenteBulk)bulk).validate();
		}	
	};
	private boolean mensile;
public CRUDCostoDelDipendenteBP() {
	super();
}
public CRUDCostoDelDipendenteBP(String function) {
	super(function);
	//costi_per_elemento_voce.setReadonly(false);
	//costi_per_elemento_voce.setEnabled(false);
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'costi_per_elemento_voce'
 *
 * @return Il valore della proprietà 'costi_per_elemento_voce'
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCosti_per_elemento_voce() {
	return costi_per_elemento_voce;
}
protected void init(it.cnr.jada.action.Config config,it.cnr.jada.action.ActionContext context) throws it.cnr.jada.action.BusinessProcessException {
	setMensile("true".equalsIgnoreCase(config.getInitParameter("mensile")));
	super.init(config,context);
}
public OggettoBulk initializeModelForSearch(ActionContext context,OggettoBulk bulk) throws BusinessProcessException {
	V_dipendenteBulk dipendente = (V_dipendenteBulk)super.initializeModelForSearch(context,bulk);
	if (!mensile)
		dipendente.setMese(new Integer(0));
	return dipendente;
}
public boolean isDeleteButtonHidden() {
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (03/10/2002 15:26:48)
 * @return boolean
 */
public boolean isMensile() {
	return mensile;
}
public boolean isNewButtonHidden() {
	return true;
}
public void reset(ActionContext context) throws BusinessProcessException {
	resetForSearch(context);
}
/**
 * Insert the method's description here.
 * Creation date: (03/10/2002 15:26:48)
 * @param newMensile boolean
 */
public void setMensile(boolean newMensile) {
	mensile = newMensile;
}
}
