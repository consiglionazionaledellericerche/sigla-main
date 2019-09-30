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

package it.cnr.contab.docamm00.tabrif.bulk;

import java.lang.String;
import java.util.Dictionary;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.action.*;

public class Tipo_movimento_inventarioBulk extends Tipo_movimento_inventarioBase {

	public final static String CARICO      = "C";
	public final static String SCARICO      = "S";
	
	public final static Dictionary CARICO_SCARICO;

	static {
		
		CARICO_SCARICO = new it.cnr.jada.util.OrderedHashtable();
		CARICO_SCARICO.put(CARICO,"Carico");
		CARICO_SCARICO.put(SCARICO,"Scarico");
	};

public Tipo_movimento_inventarioBulk() {
	super();
}
public Tipo_movimento_inventarioBulk(java.lang.String cd_tipo_movimento_inventario) {
	super(cd_tipo_movimento_inventario);
}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi italiano/estero.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_carico_scaricoKeys() {
		return CARICO_SCARICO;
	}
public OggettoBulk initialize(CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	setFlag_carico_scarico(CARICO);
	return this;
}
}
