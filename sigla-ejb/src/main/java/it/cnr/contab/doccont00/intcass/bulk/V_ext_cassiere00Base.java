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

package it.cnr.contab.doccont00.intcass.bulk;

import java.sql.Timestamp;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_ext_cassiere00Base extends OggettoBulk implements Persistent {
	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

	// NOME_FILE VARCHAR(20) NOT NULL
	private java.lang.String nome_file;
	
	private Timestamp data_inizio_rif;
	
	private Timestamp data_fine_rif;

public V_ext_cassiere00Base() {
	super();
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo nome_file
 */
public java.lang.String getNome_file() {
	return nome_file;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo nome_file
 */
public void setNome_file(java.lang.String nome_file) {
	this.nome_file = nome_file;
}
public Timestamp getData_inizio_rif() {
	return data_inizio_rif;
}
public void setData_inizio_rif(Timestamp data_inizio_rif) {
	this.data_inizio_rif = data_inizio_rif;
}
public Timestamp getData_fine_rif() {
	return data_fine_rif;
}
public void setData_fine_rif(Timestamp data_fine_rif) {
	this.data_fine_rif = data_fine_rif;
}
}
