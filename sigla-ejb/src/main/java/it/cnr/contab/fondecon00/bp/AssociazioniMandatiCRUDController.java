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

import it.cnr.contab.fondecon00.core.bulk.Fondo_economaleBulk;
import it.cnr.jada.bulk.OggettoBulk;

/**
 * Insert the type's description here.
 * Creation date: (6/4/2002 10:31:43 AM)
 * @author: Roberto Peli
 */
public class AssociazioniMandatiCRUDController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
/**
 * AssociazioniMandatiCRUDController constructor comment.
 * @param name java.lang.String
 * @param modelClass java.lang.Class
 * @param listPropertyName java.lang.String
 * @param parent it.cnr.jada.util.action.FormController
 */
public AssociazioniMandatiCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
	super(name, modelClass, listPropertyName, parent);
}
/**
 * AssociazioniMandatiCRUDController constructor comment.
 * @param name java.lang.String
 * @param modelClass java.lang.Class
 * @param listPropertyName java.lang.String
 * @param parent it.cnr.jada.util.action.FormController
 * @param multiSelection boolean
 */
public AssociazioniMandatiCRUDController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent, boolean multiSelection) {
	super(name, modelClass, listPropertyName, parent, multiSelection);
}
public boolean isGrowable() {
	
	Fondo_economaleBulk fondo = (Fondo_economaleBulk)getParentModel();
	return	super.isGrowable() &&
			(fondo.getCrudStatus() == OggettoBulk.NORMAL ||
			 fondo.getCrudStatus() == OggettoBulk.TO_BE_UPDATED) &&
			!fondo.isChiuso() &&
			!fondo.isOnlyForClose();
}
public boolean isShrinkable() {
	return super.isShrinkable() && isGrowable();
}
}
