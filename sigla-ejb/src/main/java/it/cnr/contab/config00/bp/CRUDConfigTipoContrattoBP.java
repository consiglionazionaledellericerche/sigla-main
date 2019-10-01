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

/*
 * Created on Apr 15, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.config00.bp;

import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDConfigTipoContrattoBP extends SimpleCRUDBP {

	/**
	 * 
	 */
	public CRUDConfigTipoContrattoBP() {
		super();
	}

	/**
	 * @param s
	 */
	public CRUDConfigTipoContrattoBP(String s) {
		super(s);
	}
	/*
	 *  (non-Javadoc)
	 * @see it.cnr.jada.util.action.CRUDBP#basicEdit(it.cnr.jada.action.ActionContext, it.cnr.jada.bulk.OggettoBulk, boolean)
	 */
	public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {	
		super.basicEdit(context, bulk, doInitializeForEdit);
		if (getStatus()!=VIEW){
			ICancellatoLogicamente bulkCancellato= (ICancellatoLogicamente)getModel();
			if (bulkCancellato!=null && bulkCancellato.isCancellatoLogicamente()) {
				setStatus(VIEW);
			}			
		}
	}
}
