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

package it.cnr.contab.web.rest.exception;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import it.cnr.contab.web.rest.config.FatturaAttivaCodiciEnum;

import javax.ws.rs.core.Response.Status;

public class FatturaAttivaException extends RestException {
	private static final long serialVersionUID = 1L;
	private final FatturaAttivaCodiciEnum fatturaAttivaCodiciEnum;
	
	public FatturaAttivaException(Status status, String message,
			FatturaAttivaCodiciEnum fatturaAttivaCodiciEnum) {
		super(status, message);
		this.fatturaAttivaCodiciEnum = fatturaAttivaCodiciEnum;
	}
	
	public static FatturaAttivaException newInstance(Status status, FatturaAttivaCodiciEnum fatturaAttivaCodiciEnum) {
		return new FatturaAttivaException(status, fatturaAttivaCodiciEnum.getMessage(),fatturaAttivaCodiciEnum);
	}
	
	@Override
	public Map<String, Serializable> getErrorMap() {
		if (Optional.ofNullable(getMessage()).isPresent()) {
			Map<String, Serializable> result = new HashMap<String, Serializable>();
			result.put("codice", fatturaAttivaCodiciEnum.getCodice());
			result.put("message", getMessage());
			return result;
		}
		return fatturaAttivaCodiciEnum.getErrorMap();
	}
}
