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

package it.cnr.contab.utenze00.bp;

/**
 * Classe che definisce il contesto REST per i servizi JSON
 */

public class RESTUserContext extends CNRUserContext implements it.cnr.jada.UserContext {
	private static final long serialVersionUID = 1L;
	
	public RESTUserContext() {
		this(null, null, null, null);
	}
	public RESTUserContext(Integer esercizio,
			String cd_unita_organizzativa, String cd_cds, String cd_cdr) {
		super("RESTUSER", null, esercizio, cd_unita_organizzativa, cd_cds, cd_cdr);
	}		
}