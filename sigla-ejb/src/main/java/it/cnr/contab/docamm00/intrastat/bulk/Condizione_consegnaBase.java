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

package it.cnr.contab.docamm00.intrastat.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Condizione_consegnaBase extends Condizione_consegnaKey implements Keyed {
	// DS_CONDIZIONE_CONSEGNA VARCHAR(300) NOT NULL
	private java.lang.String ds_condizione_consegna;
	private java.lang.String cd_gruppo;
public Condizione_consegnaBase() {
	super();
}
public Condizione_consegnaBase(java.lang.String cd_incoterm,java.lang.Integer esercizio) {
	super(cd_incoterm,esercizio);
}
/* 
 * Getter dell'attributo ds_condizione_consegna
 */
public java.lang.String getDs_condizione_consegna() {
	return ds_condizione_consegna;
}
/* 
 * Setter dell'attributo ds_condizione_consegna
 */
public void setDs_condizione_consegna(java.lang.String ds_condizione_consegna) {
	this.ds_condizione_consegna = ds_condizione_consegna;
}
public void setCd_gruppo(java.lang.String cd_gruppo) {
	this.cd_gruppo = cd_gruppo;
}
public java.lang.String getCd_gruppo() {
	return cd_gruppo;
}
}
