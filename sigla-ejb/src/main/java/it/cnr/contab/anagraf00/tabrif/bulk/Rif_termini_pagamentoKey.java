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

public class Rif_termini_pagamentoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TERMINI_PAG VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_termini_pag;

public Rif_termini_pagamentoKey() {
	super();
}
public Rif_termini_pagamentoKey(java.lang.String cd_termini_pag) {
	super();
	this.cd_termini_pag = cd_termini_pag;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Rif_termini_pagamentoKey)) return false;
	Rif_termini_pagamentoKey k = (Rif_termini_pagamentoKey)o;
	if(!compareKey(getCd_termini_pag(),k.getCd_termini_pag())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_termini_pag
 */
public java.lang.String getCd_termini_pag() {
	return cd_termini_pag;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_termini_pag());
}
/* 
 * Setter dell'attributo cd_termini_pag
 */
public void setCd_termini_pag(java.lang.String cd_termini_pag) {
	this.cd_termini_pag = cd_termini_pag;
}
}
