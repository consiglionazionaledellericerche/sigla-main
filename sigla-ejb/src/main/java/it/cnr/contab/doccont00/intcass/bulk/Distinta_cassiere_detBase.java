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

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Distinta_cassiere_detBase extends Distinta_cassiere_detKey implements Keyed {
	// PG_MANDATO DECIMAL(10,0)
	private java.lang.Long pg_mandato;

	// PG_REVERSALE DECIMAL(10,0)
	private java.lang.Long pg_reversale;
	
	private java.lang.String cd_cds_origine;

public Distinta_cassiere_detBase() {
	super();
}
public Distinta_cassiere_detBase(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,Integer esercizio,java.lang.Long pg_dettaglio,java.lang.Long pg_distinta) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_dettaglio,pg_distinta);
}
/* 
 * Getter dell'attributo pg_mandato
 */
public java.lang.Long getPg_mandato() {
	return pg_mandato;
}
/* 
 * Getter dell'attributo pg_reversale
 */
public java.lang.Long getPg_reversale() {
	return pg_reversale;
}
/* 
 * Setter dell'attributo pg_mandato
 */
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.pg_mandato = pg_mandato;
}
/* 
 * Setter dell'attributo pg_reversale
 */
public void setPg_reversale(java.lang.Long pg_reversale) {
	this.pg_reversale = pg_reversale;
}
public java.lang.String getCd_cds_origine() {
	return cd_cds_origine;
}
public void setCd_cds_origine(java.lang.String cd_cds_origine) {
	this.cd_cds_origine = cd_cds_origine;
}
}
