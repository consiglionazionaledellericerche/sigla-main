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

package it.cnr.contab.config00.latt.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RisultatoBase extends RisultatoKey implements Keyed {
	// CD_TIPO_RISULTATO VARCHAR(10)
	private java.lang.String cd_tipo_risultato;

/* 
 * Getter dell'attributo cd_tipo_risultato
 */
public java.lang.String getCd_tipo_risultato() {
	return cd_tipo_risultato;
}

/* 
 * Setter dell'attributo cd_tipo_risultato
 */
public void setCd_tipo_risultato(java.lang.String cd_tipo_risultato) {
	this.cd_tipo_risultato = cd_tipo_risultato;
}
	// DS_RISULTATO VARCHAR(1000)
	private java.lang.String ds_risultato;

/* 
 * Getter dell'attributo ds_risultato
 */
public java.lang.String getDs_risultato() {
	return ds_risultato;
}

/* 
 * Setter dell'attributo ds_risultato
 */
public void setDs_risultato(java.lang.String ds_risultato) {
	this.ds_risultato = ds_risultato;
}
	// QUANTITA DECIMAL(22,0)
	private java.math.BigDecimal quantita;

/* 
 * Getter dell'attributo quantita
 */
public java.math.BigDecimal getQuantita() {
	return quantita;
}

/* 
 * Setter dell'attributo quantita
 */
public void setQuantita(java.math.BigDecimal quantita) {
	this.quantita = quantita;
}

public RisultatoBase() {
	super();
}

public RisultatoBase(java.lang.String cd_centro_responsabilita,java.lang.String cd_linea_attivita,java.lang.Long pg_risultato) {
	super(cd_centro_responsabilita,cd_linea_attivita,pg_risultato);
}
}
