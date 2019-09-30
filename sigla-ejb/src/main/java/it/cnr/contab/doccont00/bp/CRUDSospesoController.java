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
import it.cnr.jada.bulk.*;

public class CRUDSospesoController extends it.cnr.jada.util.action.SimpleDetailCRUDController {
public CRUDSospesoController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent) {
	super(name, modelClass, listPropertyName, parent);
}
public CRUDSospesoController(String name, Class modelClass, String listPropertyName, it.cnr.jada.util.action.FormController parent, boolean multiSelection) {
	super(name, modelClass, listPropertyName, parent, multiSelection);
}
public OggettoBulk removeDetail(int index) {
	try {
		if ( getParentModel() instanceof MandatoIBulk )
		{
			MandatoIBulk mandato = (MandatoIBulk) getParentModel();
			Sospeso_det_uscBulk sospeso_det_usc = (Sospeso_det_uscBulk)mandato.removeFromSospeso_det_uscColl( index );
			return sospeso_det_usc;
		}
		else if ( getParentModel() instanceof ReversaleIBulk )
		{
			ReversaleIBulk reversale = (ReversaleIBulk) getParentModel();
			Sospeso_det_etrBulk sospeso_det_etr = (Sospeso_det_etrBulk)reversale.removeFromSospeso_det_etrColl( index );
			return sospeso_det_etr;
		}
		else
			return null;
			
	} catch(Exception e) {
		throw new it.cnr.jada.util.IntrospectionError(e.getMessage());
	}
}
}
