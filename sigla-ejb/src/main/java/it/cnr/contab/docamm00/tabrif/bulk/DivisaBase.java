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

package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class DivisaBase extends DivisaKey implements Keyed {
	// DS_DIVISA VARCHAR(50) NOT NULL
	private java.lang.String ds_divisa;

/* 
 * Getter dell'attributo ds_divisa
 */
public java.lang.String getDs_divisa() {
	return ds_divisa;
}

/* 
 * Setter dell'attributo ds_divisa
 */
public void setDs_divisa(java.lang.String ds_divisa) {
	this.ds_divisa = ds_divisa;
}
	// DT_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp dt_cancellazione;

/* 
 * Getter dell'attributo dt_cancellazione
 */
public java.sql.Timestamp getDt_cancellazione() {
	return dt_cancellazione;
}

/* 
 * Setter dell'attributo dt_cancellazione
 */
public void setDt_cancellazione(java.sql.Timestamp dt_cancellazione) {
	this.dt_cancellazione = dt_cancellazione;
}
	// FL_CALCOLA_CON_DIVISO CHAR(1) NOT NULL
	private java.lang.Boolean fl_calcola_con_diviso;

/* 
 * Getter dell'attributo fl_calcola_con_diviso
 */
public java.lang.Boolean getFl_calcola_con_diviso() {
	return fl_calcola_con_diviso;
}

/* 
 * Setter dell'attributo fl_calcola_con_diviso
 */
public void setFl_calcola_con_diviso(java.lang.Boolean fl_calcola_con_diviso) {
	this.fl_calcola_con_diviso = fl_calcola_con_diviso;
}
	// PRECISIONE DECIMAL(5,4) NOT NULL
	private java.math.BigDecimal precisione;

/* 
 * Getter dell'attributo precisione
 */
public java.math.BigDecimal getPrecisione() {
	return precisione;
}

/* 
 * Setter dell'attributo precisione
 */
public void setPrecisione(java.math.BigDecimal precisione) {
	this.precisione = precisione;
}

public DivisaBase() {
	super();
}

public DivisaBase(java.lang.String cd_divisa) {
	super(cd_divisa);
}
}
