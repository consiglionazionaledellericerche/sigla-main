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

package it.cnr.contab.anagraf00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Rif_inquadramentoBase extends Rif_inquadramentoKey implements Keyed {
	// CD_GRUPPO_INQUADRAMENTO VARCHAR(10)
	private java.lang.String cd_gruppo_inquadramento;

	// CD_LIVELLO VARCHAR(10)
	private java.lang.String cd_livello;

	// CD_PROFILO VARCHAR(10)
	private java.lang.String cd_profilo;

	// CD_PROGRESSIONE VARCHAR(10)
	private java.lang.String cd_progressione;

	// DS_INQUADRAMENTO VARCHAR(300)
	private java.lang.String ds_inquadramento;

	// TI_DIPENDENTE_ALTRO CHAR(1) NOT NULL
	private java.lang.String ti_dipendente_altro;

public Rif_inquadramentoBase() {
	super();
}
public Rif_inquadramentoBase(java.lang.Long pg_rif_inquadramento) {
	super(pg_rif_inquadramento);
}
/* 
 * Getter dell'attributo cd_gruppo_inquadramento
 */
public java.lang.String getCd_gruppo_inquadramento() {
	return cd_gruppo_inquadramento;
}
/* 
 * Getter dell'attributo cd_livello
 */
public java.lang.String getCd_livello() {
	return cd_livello;
}
/* 
 * Getter dell'attributo cd_profilo
 */
public java.lang.String getCd_profilo() {
	return cd_profilo;
}
/* 
 * Getter dell'attributo cd_progressione
 */
public java.lang.String getCd_progressione() {
	return cd_progressione;
}
/* 
 * Getter dell'attributo ds_inquadramento
 */
public java.lang.String getDs_inquadramento() {
	return ds_inquadramento;
}
/* 
 * Getter dell'attributo ti_dipendente_altro
 */
public java.lang.String getTi_dipendente_altro() {
	return ti_dipendente_altro;
}
/* 
 * Setter dell'attributo cd_gruppo_inquadramento
 */
public void setCd_gruppo_inquadramento(java.lang.String cd_gruppo_inquadramento) {
	this.cd_gruppo_inquadramento = cd_gruppo_inquadramento;
}
/* 
 * Setter dell'attributo cd_livello
 */
public void setCd_livello(java.lang.String cd_livello) {
	this.cd_livello = cd_livello;
}
/* 
 * Setter dell'attributo cd_profilo
 */
public void setCd_profilo(java.lang.String cd_profilo) {
	this.cd_profilo = cd_profilo;
}
/* 
 * Setter dell'attributo cd_progressione
 */
public void setCd_progressione(java.lang.String cd_progressione) {
	this.cd_progressione = cd_progressione;
}
/* 
 * Setter dell'attributo ds_inquadramento
 */
public void setDs_inquadramento(java.lang.String ds_inquadramento) {
	this.ds_inquadramento = ds_inquadramento;
}
/* 
 * Setter dell'attributo ti_dipendente_altro
 */
public void setTi_dipendente_altro(java.lang.String ti_dipendente_altro) {
	this.ti_dipendente_altro = ti_dipendente_altro;
}
}
