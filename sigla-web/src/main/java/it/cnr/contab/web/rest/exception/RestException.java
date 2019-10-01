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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

public class RestException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	private Status status;
	public RestException(Status status, String message) {
		super(message);
		this.status = status;
	}
	public Status getStatus() {
		return status;
	}

	public Map<String, Serializable> getErrorMap() {
		return Collections.singletonMap("message", getMessage());
	}	
	
}
