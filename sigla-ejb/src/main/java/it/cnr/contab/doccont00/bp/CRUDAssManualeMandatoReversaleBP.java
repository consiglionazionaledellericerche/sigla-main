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

package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

/**
 * Insert the type's description here.
 * Creation date: (20/11/2002 12.28.04)
 * @author: Roberto Fantino
 */
public class CRUDAssManualeMandatoReversaleBP extends it.cnr.jada.util.action.SimpleCRUDBP {

	private final SimpleDetailCRUDController reversaliAssociateCRUDController = new SimpleDetailCRUDController("reversaliAssociateCRUDController", ReversaleIBulk.class, "reversaliAssociate", this);
	private final SimpleDetailCRUDController reversaliDisponibiliCRUDController = new SimpleDetailCRUDController("reversaliDisponibiliCRUDController", ReversaleIBulk.class, "reversaliDisponibili", this);
/**
 * CRUDMandatoReversaliBP constructor comment.
 */
public CRUDAssManualeMandatoReversaleBP() {
	super();
}
/**
 * CRUDMandatoReversaliBP constructor comment.
 * @param function java.lang.String
 */
public CRUDAssManualeMandatoReversaleBP(String function) {
	super(function);
}
/**
 * Insert the method's description here.
 * Creation date: (20/11/2002 17.34.50)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final SimpleDetailCRUDController getReversaliAssociateCRUDController() {
	return reversaliAssociateCRUDController;
}
/**
 * Insert the method's description here.
 * Creation date: (20/11/2002 17.34.50)
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController
 */
public final SimpleDetailCRUDController getReversaliDisponibiliCRUDController() {
	return reversaliDisponibiliCRUDController;
}
/**
 *	Nascondo il bottone di cancellazione
 */
public boolean isDeleteButtonHidden() {
	return true;
}
/**
 *	Nascondo il bottone Nuovo
 */
public boolean isNewButtonHidden() {
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (20/11/2002 12.30.16)
 * @return boolean
 */
public boolean isROMandato() {
	return !isSearching();
}
public void reset(ActionContext context) throws BusinessProcessException {
	super.resetForSearch(context);
}
}
