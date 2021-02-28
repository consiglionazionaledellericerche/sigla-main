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

package it.cnr.contab.pagopa.bulk;

import it.cnr.jada.bulk.ValidationException;

import java.util.Optional;

public class TipoPendenzaPagopaBulk extends TipoPendenzaPagopaBase {
	private static final long serialVersionUID = 1L;
	public TipoPendenzaPagopaBulk() {
		super();
	}

	public TipoPendenzaPagopaBulk(Integer id) {
		super(id);
	}
	
	@Override
	public void validate() throws ValidationException {
        if (getApplicationCode() && !Optional.ofNullable(getApplicationCodeDefault()).isPresent())
        	throw new ValidationException("Nel caso di gestione dell'application code è necessario indicare l'application code di default");
		super.validate();
	}
}