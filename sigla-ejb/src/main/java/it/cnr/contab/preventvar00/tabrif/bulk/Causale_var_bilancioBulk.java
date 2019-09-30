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

package it.cnr.contab.preventvar00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Causale_var_bilancioBulk extends Causale_var_bilancioBase {

	// TI_CAUSALE
	public final static java.lang.String UTENTE = "U";
	public final static java.lang.String SISTEMA = "S";

	public final static java.util.Dictionary TI_CAUSALE;

	static{
		TI_CAUSALE = new it.cnr.jada.util.OrderedHashtable();
		TI_CAUSALE.put(SISTEMA,"Sistema");
		TI_CAUSALE.put(UTENTE,"Utente");
	}



public Causale_var_bilancioBulk() {
	super();
}
public Causale_var_bilancioBulk(java.lang.String cd_causale_var_bilancio) {
	super(cd_causale_var_bilancio);
}
/**
 * <!-- @TODO: da completare -->
 * Restituisce il valore della proprietà 'ti_causaleKeys'
 *
 * @return Il valore della proprietà 'ti_causaleKeys'
 */
public java.util.Dictionary getTi_causaleKeys() {

	return TI_CAUSALE;
}
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {

	super.initializeForInsert(bp,context);
	setTi_causale(UTENTE);

	return this;
}
}
