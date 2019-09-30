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

package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Inventario_beniKey extends OggettoBulk implements KeyedPersistent {
	// NR_INVENTARIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long nr_inventario;

	// PG_INVENTARIO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_inventario;

	// PROGRESSIVO DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long progressivo;

public Inventario_beniKey() {
	super();
}
public Inventario_beniKey(java.lang.Long nr_inventario,java.lang.Long pg_inventario,java.lang.Long progressivo) {
	super();
	this.nr_inventario = nr_inventario;
	this.pg_inventario = pg_inventario;
	this.progressivo = progressivo;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Inventario_beniKey)) return false;
	Inventario_beniKey k = (Inventario_beniKey)o;
	if(!compareKey(getNr_inventario(),k.getNr_inventario())) return false;
	if(!compareKey(getPg_inventario(),k.getPg_inventario())) return false;
	if(!compareKey(getProgressivo(),k.getProgressivo())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Inventario_beniKey)) return false;
	Inventario_beniKey k = (Inventario_beniKey)o;
	if(!compareKey(getNr_inventario(),k.getNr_inventario())) return false;
	if(!compareKey(getPg_inventario(),k.getPg_inventario())) return false;
	if(!compareKey(getProgressivo(),k.getProgressivo())) return false;
	return true;
}
/* 
 * Getter dell'attributo nr_inventario
 */
public java.lang.Long getNr_inventario() {
	return nr_inventario;
}
/* 
 * Getter dell'attributo pg_inventario
 */
public java.lang.Long getPg_inventario() {
	return pg_inventario;
}
/* 
 * Getter dell'attributo progressivo
 */
public java.lang.Long getProgressivo() {
	return progressivo;
}
public int hashCode() {
	return
		calculateKeyHashCode(getNr_inventario())+
		calculateKeyHashCode(getPg_inventario())+
		calculateKeyHashCode(getProgressivo());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getNr_inventario())+
		calculateKeyHashCode(getPg_inventario())+
		calculateKeyHashCode(getProgressivo());
}
/* 
 * Setter dell'attributo nr_inventario
 */
public void setNr_inventario(java.lang.Long nr_inventario) {
	this.nr_inventario = nr_inventario;
}
/* 
 * Setter dell'attributo pg_inventario
 */
public void setPg_inventario(java.lang.Long pg_inventario) {
	this.pg_inventario = pg_inventario;
}
/* 
 * Setter dell'attributo progressivo
 */
public void setProgressivo(java.lang.Long progressivo) {
	this.progressivo = progressivo;
}
}
