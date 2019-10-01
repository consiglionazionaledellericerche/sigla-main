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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Modalita_pagamentoBase extends Modalita_pagamentoKey implements Keyed {
	// CD_TERZO_DELEGATO DECIMAL(8,0)
	private java.lang.Integer cd_terzo_delegato;

public Modalita_pagamentoBase() {
	super();
}
public Modalita_pagamentoBase(java.lang.String cd_modalita_pag,java.lang.Integer cd_terzo) {
	super(cd_modalita_pag,cd_terzo);
}
/* 
 * Getter dell'attributo cd_terzo_delegato
 */
public java.lang.Integer getCd_terzo_delegato() {
	return cd_terzo_delegato;
}
/* 
 * Setter dell'attributo cd_terzo_delegato
 */
public void setCd_terzo_delegato(java.lang.Integer cd_terzo_delegato) {
	this.cd_terzo_delegato = cd_terzo_delegato;
}
}
