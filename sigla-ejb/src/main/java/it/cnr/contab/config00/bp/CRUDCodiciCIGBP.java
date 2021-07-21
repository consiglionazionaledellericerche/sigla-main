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
 * Created on Feb 23, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.cnr.contab.config00.bp;


import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.util.ICancellatoLogicamente;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.action.SimpleCRUDBP;

/**
 * @author mspasiano
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CRUDCodiciCIGBP extends SimpleCRUDBP {
	private static final long serialVersionUID = -4170076026547194358L;

	public CRUDCodiciCIGBP() {
		super();
	}
	
	public CRUDCodiciCIGBP(String function)  throws BusinessProcessException{
		super(function);
	}

	public void basicEdit(it.cnr.jada.action.ActionContext context,it.cnr.jada.bulk.OggettoBulk bulk, boolean doInitializeForEdit) throws it.cnr.jada.action.BusinessProcessException {	
		super.basicEdit(context, bulk, doInitializeForEdit);
		if (getStatus()!=VIEW){
			ICancellatoLogicamente bulkCancellato= (ICancellatoLogicamente)getModel();
			if (bulkCancellato!=null && bulkCancellato.isCancellatoLogicamente()) {
				setStatus(VIEW);
			}			
		}
	}
	@Override
	public void validate(ActionContext actioncontext)
			throws ValidationException {
		super.validate(actioncontext);
		CigBulk bulk=(CigBulk)this.getModel();
		if ( bulk.getCdCig()==null) 
			throw new ValidationException("E' necessario inserire il Codice");
		if ( bulk.getCdCig().length()!=10) 
			throw new ValidationException("La lunghezza del Codice non è valida");
		for (int i = 0;i < bulk.getCdCig().length();i++)
			if (!Character.isLetterOrDigit(bulk.getCdCig().charAt(i)))
				throw new ValidationException( "Il codice cig può essere composto solo da cifre o lettere e non può contenere spazi o caratteri speciali." );
	}
}