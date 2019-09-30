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

package it.cnr.contab.docamm00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_stm_paramin_ft_attivaKey extends OggettoBulk implements KeyedPersistent {

	// ID_REPORT DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal id_report;
public V_stm_paramin_ft_attivaKey() {
	super();
}
public V_stm_paramin_ft_attivaKey(java.math.BigDecimal id_report) {
	super();
	this.id_report = id_report;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof V_stm_paramin_ft_attivaKey)) return false;
	V_stm_paramin_ft_attivaKey k = (V_stm_paramin_ft_attivaKey)o;
	if(!compareKey(getId_report(),k.getId_report())) return false;
	return true;
}
/* 
 * Getter dell'attributo id_report
 */
public java.math.BigDecimal getId_report() {
	return id_report;
}
public int primaryKeyHashCode() {
	return calculateKeyHashCode(getId_report());
}
/* 
 * Setter dell'attributo id_report
 */
public void setId_report(java.math.BigDecimal id_report) {
	this.id_report = id_report;
}
}
