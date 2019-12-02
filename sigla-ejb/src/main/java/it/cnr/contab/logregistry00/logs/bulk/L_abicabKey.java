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

package it.cnr.contab.logregistry00.logs.bulk;

import it.cnr.contab.logregistry00.core.bulk.OggettoLogBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class L_abicabKey extends OggettoLogBulk  implements KeyedPersistent {
	// ABI CHAR(5) NOT NULL (PK)
	private java.lang.String abi;

	// CAB CHAR(5) NOT NULL (PK)
	private java.lang.String cab;

public L_abicabKey() {
	super();
}
public L_abicabKey(java.lang.String abi,java.lang.String cab,java.math.BigDecimal pg_storico_) {
	super();
	this.abi = abi;
	this.cab = cab;
	this.pg_storico_ = pg_storico_;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof L_abicabKey)) return false;
	L_abicabKey k = (L_abicabKey)o;
	if(!compareKey(getAbi(),k.getAbi())) return false;
	if(!compareKey(getCab(),k.getCab())) return false;
	if(!compareKey(getPg_storico_(),k.getPg_storico_())) return false;
	return true;
}
/* 
 * Getter dell'attributo abi
 */
public java.lang.String getAbi() {
	return abi;
}
/* 
 * Getter dell'attributo cab
 */
public java.lang.String getCab() {
	return cab;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getAbi())+
		calculateKeyHashCode(getCab())+
		calculateKeyHashCode(getPg_storico_());
}
/* 
 * Setter dell'attributo abi
 */
public void setAbi(java.lang.String abi) {
	this.abi = abi;
}
/* 
 * Setter dell'attributo cab
 */
public void setCab(java.lang.String cab) {
	this.cab = cab;
}
}
