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

package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vp_liquid_iva_annualeKey extends OggettoBulk  implements KeyedPersistent {

	// ESERCIZIO DECIMAL(20,3)
	private Integer esercizio;

	// ID DECIMAL(22,0) NOT NULL
	private Long id;

	// SEQUENZA DECIMAL(22,0) NOT NULL
	private Long sequenza;

	// TIPO CHAR(1) NOT NULL
	private java.lang.String tipo;

public Vp_liquid_iva_annualeKey() {
	super();
}
public Vp_liquid_iva_annualeKey(
	Integer esercizio,
	Long id,
	Long sequenza,
	String tipo) {

	super();
	
	this.esercizio = esercizio;
	this.id = id;
	this.sequenza = sequenza;
	this.tipo = tipo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Vp_liquid_iva_annualeKey)) return false;
	Vp_liquid_iva_annualeKey k = (Vp_liquid_iva_annualeKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getId(),k.getId())) return false;
	if(!compareKey(getSequenza(),k.getSequenza())) return false;
	if(!compareKey(getTipo(),k.getTipo())) return false;
	return true;
}
/* 
 * Getter dell'attributo esercizio
 */
public Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo id
 */
public Long getId() {
	return id;
}
/* 
 * Getter dell'attributo sequenza
 */
public Long getSequenza() {
	return sequenza;
}
/* 
 * Getter dell'attributo tipo
 */
public java.lang.String getTipo() {
	return tipo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getId())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getSequenza())+
		calculateKeyHashCode(getTipo());
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo id
 */
public void setId(Long id) {
	this.id = id;
}
/* 
 * Setter dell'attributo sequenza
 */
public void setSequenza(Long sequenza) {
	this.sequenza = sequenza;
}
/* 
 * Setter dell'attributo tipo
 */
public void setTipo(java.lang.String tipo) {
	this.tipo = tipo;
}
}
