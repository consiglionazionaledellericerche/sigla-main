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

public class Tipo_rapportoBase extends Tipo_rapportoKey implements Keyed {
	// DS_TIPO_RAPPORTO VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_rapporto;

	// TI_DIPENDENTE_ALTRO CHAR(1) NOT NULL
	private java.lang.String ti_dipendente_altro;

	// TI_RAPPORTO_ALTRO CHAR(1)
	private java.lang.String ti_rapporto_altro;
	private Boolean fl_inquadramento;
	private Boolean fl_bonus;
	private String cd_trattamento;
	// FL_VISIBILE_A_TUTTI CHAR(1) NOT NULL
	private java.lang.Boolean fl_visibile_a_tutti;

public Tipo_rapportoBase() {
	super();
}
public Tipo_rapportoBase(java.lang.String cd_tipo_rapporto) {
	super(cd_tipo_rapporto);
}
/* 
 * Getter dell'attributo ds_tipo_rapporto
 */
public java.lang.String getDs_tipo_rapporto() {
	return ds_tipo_rapporto;
}
/* 
 * Getter dell'attributo ti_dipendente_altro
 */
public java.lang.String getTi_dipendente_altro() {
	return ti_dipendente_altro;
}
/* 
 * Getter dell'attributo ti_rapporto_altro
 */
public java.lang.String getTi_rapporto_altro() {
	return ti_rapporto_altro;
}
/* 
 * Setter dell'attributo ds_tipo_rapporto
 */
public void setDs_tipo_rapporto(java.lang.String ds_tipo_rapporto) {
	this.ds_tipo_rapporto = ds_tipo_rapporto;
}
/* 
 * Setter dell'attributo ti_dipendente_altro
 */
public void setTi_dipendente_altro(java.lang.String ti_dipendente_altro) {
	this.ti_dipendente_altro = ti_dipendente_altro;
}
/* 
 * Setter dell'attributo ti_rapporto_altro
 */
public void setTi_rapporto_altro(java.lang.String ti_rapporto_altro) {
	this.ti_rapporto_altro = ti_rapporto_altro;
}
public Boolean getFl_inquadramento() {
	return fl_inquadramento;
}
public void setFl_inquadramento(Boolean fl_inquadramento) {
	this.fl_inquadramento = fl_inquadramento;
}
public Boolean getFl_bonus() {
	return fl_bonus;
}
public void setFl_bonus(Boolean fl_bonus) {
	this.fl_bonus = fl_bonus;
}
public String getCd_trattamento() {
	return cd_trattamento;
}
public void setCd_trattamento(String cd_trattamento) {
	this.cd_trattamento = cd_trattamento;
}
public java.lang.Boolean getFl_visibile_a_tutti() {
	return fl_visibile_a_tutti;
}
public void setFl_visibile_a_tutti(java.lang.Boolean fl_visibile_a_tutti) {
	this.fl_visibile_a_tutti = fl_visibile_a_tutti;
}

}
