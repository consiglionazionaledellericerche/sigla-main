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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Gruppo_crBase extends Gruppo_crKey implements Keyed {

	// DS_GRUPPO_CR VARCHAR(10) NOT NULL
	private java.lang.String ds_gruppo_cr;

	// FL_ACCENTRATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_accentrato;

	private java.lang.Boolean fl_anno_prec;
	
	private java.lang.Boolean fl_f24online;
	
	private java.lang.Boolean fl_f24online_previd;
	
	private java.lang.String cd_tributo_erario;
	
	private java.lang.String cd_tipo_riga_f24;
	
	private java.lang.String cd_matricola_inps;
	
public Gruppo_crBase() {
	super();
}
public Gruppo_crBase(java.lang.Integer esercizio, java.lang.String cd_gruppo_cr) {
	super(esercizio, cd_gruppo_cr);
}
/* 
 * Getter dell'attributo ds_gruppo_cr
 */
public java.lang.String getDs_gruppo_cr() {
	return ds_gruppo_cr;
}
/* 
 * Getter dell'attributo fl_accentrato
 */
public java.lang.Boolean getFl_accentrato() {
	return fl_accentrato;
}
/* 
 * Setter dell'attributo ds_gruppo_cr
 */
public void setDs_gruppo_cr(java.lang.String ds_gruppo_cr) {
	this.ds_gruppo_cr = ds_gruppo_cr;
}
/* 
 * Setter dell'attributo fl_accentrato
 */
public void setFl_accentrato(java.lang.Boolean fl_accentrato) {
	this.fl_accentrato = fl_accentrato;
}
public java.lang.Boolean getFl_anno_prec() {
	return fl_anno_prec;
}
public void setFl_anno_prec(java.lang.Boolean fl_anno_prec) {
	this.fl_anno_prec = fl_anno_prec;
}
public java.lang.Boolean getFl_f24online() {
	return fl_f24online;
}
public void setFl_f24online(java.lang.Boolean fl_f24online) {
	this.fl_f24online = fl_f24online;
}
public java.lang.Boolean getFl_f24online_previd() {
	return fl_f24online_previd;
}
public void setFl_f24online_previd(java.lang.Boolean fl_f24online_previd) {
	this.fl_f24online_previd = fl_f24online_previd;
}
public java.lang.String getCd_tributo_erario() {
	return cd_tributo_erario;
}
public void setCd_tributo_erario(java.lang.String cd_tributo_erario) {
	this.cd_tributo_erario = cd_tributo_erario;
}
public java.lang.String getCd_tipo_riga_f24() {
	return cd_tipo_riga_f24;
}
public void setCd_tipo_riga_f24(java.lang.String cd_tipo_riga_f24) {
	this.cd_tipo_riga_f24 = cd_tipo_riga_f24;
}
public java.lang.String getCd_matricola_inps() {
	return cd_matricola_inps;
}
public void setCd_matricola_inps(java.lang.String cd_matricola_inps) {
	this.cd_matricola_inps = cd_matricola_inps;
}
}
