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

package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rif_termini_pagamentoBase extends Rif_termini_pagamentoKey implements Keyed {
	// DS_TERMINI_PAG VARCHAR(100) NOT NULL
	private java.lang.String ds_termini_pag;

public Rif_termini_pagamentoBase() {
	super();
}
public Rif_termini_pagamentoBase(java.lang.String cd_termini_pag) {
	super(cd_termini_pag);
}
/* 
 * Getter dell'attributo ds_termini_pag
 */
public java.lang.String getDs_termini_pag() {
	return ds_termini_pag;
}
/* 
 * Setter dell'attributo ds_termini_pag
 */
public void setDs_termini_pag(java.lang.String ds_termini_pag) {
	this.ds_termini_pag = ds_termini_pag;
}
}
