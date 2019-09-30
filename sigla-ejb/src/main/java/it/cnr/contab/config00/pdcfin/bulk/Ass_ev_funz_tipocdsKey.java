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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ev_funz_tipocdsKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_UNITA VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_tipo_unita;

	// CD_CONTO VARCHAR(20) NOT NULL (PK)
	private java.lang.String cd_conto;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// CD_FUNZIONE VARCHAR(2) NOT NULL (PK)
	private java.lang.String cd_funzione;

public Ass_ev_funz_tipocdsKey() {
	super();
}
public Ass_ev_funz_tipocdsKey(java.lang.String cd_conto,java.lang.String cd_funzione,java.lang.String cd_tipo_unita,java.lang.Integer esercizio) {
	super();
	this.cd_conto = cd_conto;
	this.cd_funzione = cd_funzione;
	this.cd_tipo_unita = cd_tipo_unita;
	this.esercizio = esercizio;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_ev_funz_tipocdsKey)) return false;
	Ass_ev_funz_tipocdsKey k = (Ass_ev_funz_tipocdsKey)o;
	if(!compareKey(getCd_conto(),k.getCd_conto())) return false;
	if(!compareKey(getCd_funzione(),k.getCd_funzione())) return false;
	if(!compareKey(getCd_tipo_unita(),k.getCd_tipo_unita())) return false;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_conto
 */
public java.lang.String getCd_conto() {
	return cd_conto;
}
/* 
 * Getter dell'attributo cd_funzione
 */
public java.lang.String getCd_funzione() {
	return cd_funzione;
}
/* 
 * Getter dell'attributo cd_tipo_unita
 */
public java.lang.String getCd_tipo_unita() {
	return cd_tipo_unita;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_conto())+
		calculateKeyHashCode(getCd_funzione())+
		calculateKeyHashCode(getCd_tipo_unita())+
		calculateKeyHashCode(getEsercizio());
}
/* 
 * Setter dell'attributo cd_conto
 */
public void setCd_conto(java.lang.String cd_conto) {
	this.cd_conto = cd_conto;
}
/* 
 * Setter dell'attributo cd_funzione
 */
public void setCd_funzione(java.lang.String cd_funzione) {
	this.cd_funzione = cd_funzione;
}
/* 
 * Setter dell'attributo cd_tipo_unita
 */
public void setCd_tipo_unita(java.lang.String cd_tipo_unita) {
	this.cd_tipo_unita = cd_tipo_unita;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
}
