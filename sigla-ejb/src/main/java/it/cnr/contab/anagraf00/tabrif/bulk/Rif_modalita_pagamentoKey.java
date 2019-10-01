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

public class Rif_modalita_pagamentoKey extends OggettoBulk implements KeyedPersistent {
	// CD_MODALITA_PAG VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_modalita_pag;

public Rif_modalita_pagamentoKey() {
	super();
}
public Rif_modalita_pagamentoKey(java.lang.String cd_modalita_pag) {
	super();
	this.cd_modalita_pag = cd_modalita_pag;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Rif_modalita_pagamentoKey)) return false;
	Rif_modalita_pagamentoKey k = (Rif_modalita_pagamentoKey)o;
	if(!compareKey(getCd_modalita_pag(),k.getCd_modalita_pag())) return false;
	return true;
}
@Override
public boolean equals(Object obj) {
	// TODO Auto-generated method stub
	return equalsByPrimaryKey(obj);
}
/* 
 * Getter dell'attributo cd_modalita_pag
 */
public java.lang.String getCd_modalita_pag() {
	return cd_modalita_pag;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_modalita_pag());
}
/* 
 * Setter dell'attributo cd_modalita_pag
 */
public void setCd_modalita_pag(java.lang.String cd_modalita_pag) {
	this.cd_modalita_pag = cd_modalita_pag;
}
}
