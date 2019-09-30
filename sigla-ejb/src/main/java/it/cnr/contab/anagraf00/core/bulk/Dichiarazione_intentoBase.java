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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Dichiarazione_intentoBase extends Dichiarazione_intentoKey implements Keyed {
	// DT_COMUNICAZIONE_DIC TIMESTAMP
	private java.sql.Timestamp dt_comunicazione_dic;

	// DT_COMUNICAZIONE_REV TIMESTAMP
	private java.sql.Timestamp dt_fine_val_dich;

	// DT_FIN_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_fin_validita;

	// DT_INI_VALIDITA TIMESTAMP NOT NULL
	private java.sql.Timestamp dt_ini_validita;

	// TIMESTAMP
	private java.sql.Timestamp dt_inizio_val_dich;

	// ID_DICHIARAZIONE VARCHAR(20) NOT NULL
	private java.lang.String id_dichiarazione;
	
	private java.lang.Boolean fl_acquisti;
	
	private java.lang.Boolean fl_importazioni;
	
	private java.lang.Integer anno_dich;
	
	private java.lang.Integer anno_rif;
	
	private java.lang.Integer num_dich;
	
	private java.math.BigDecimal im_limite_op;
	
	private java.math.BigDecimal im_limite_sing_op;
	
public Dichiarazione_intentoBase() {
	super();
}
public Dichiarazione_intentoBase(java.lang.Integer cd_anag,java.lang.Integer esercizio,java.lang.Integer progr) {
	super(cd_anag,esercizio,progr);
}
public java.sql.Timestamp getDt_comunicazione_dic() {
	return dt_comunicazione_dic;
}
public void setDt_comunicazione_dic(java.sql.Timestamp dt_comunicazione_dic) {
	this.dt_comunicazione_dic = dt_comunicazione_dic;
}
public java.sql.Timestamp getDt_fine_val_dich() {
	return dt_fine_val_dich;
}
public void setDt_fine_val_dich(java.sql.Timestamp dt_fine_val_dich) {
	this.dt_fine_val_dich = dt_fine_val_dich;
}
public java.sql.Timestamp getDt_fin_validita() {
	return dt_fin_validita;
}
public void setDt_fin_validita(java.sql.Timestamp dt_fin_validita) {
	this.dt_fin_validita = dt_fin_validita;
}
public java.sql.Timestamp getDt_ini_validita() {
	return dt_ini_validita;
}
public void setDt_ini_validita(java.sql.Timestamp dt_ini_validita) {
	this.dt_ini_validita = dt_ini_validita;
}
public java.sql.Timestamp getDt_inizio_val_dich() {
	return dt_inizio_val_dich;
}
public void setDt_inizio_val_dich(java.sql.Timestamp dt_inizio_val_dich) {
	this.dt_inizio_val_dich = dt_inizio_val_dich;
}
public java.lang.String getId_dichiarazione() {
	return id_dichiarazione;
}
public void setId_dichiarazione(java.lang.String id_dichiarazione) {
	this.id_dichiarazione = id_dichiarazione;
}
public java.lang.Boolean getFl_acquisti() {
	return fl_acquisti;
}
public void setFl_acquisti(java.lang.Boolean fl_acquisti) {
	this.fl_acquisti = fl_acquisti;
}
public java.lang.Boolean getFl_importazioni() {
	return fl_importazioni;
}
public void setFl_importazioni(java.lang.Boolean fl_importazioni) {
	this.fl_importazioni = fl_importazioni;
}
public java.lang.Integer getAnno_dich() {
	return anno_dich;
}
public void setAnno_dich(java.lang.Integer anno_dich) {
	this.anno_dich = anno_dich;
}
public java.lang.Integer getAnno_rif() {
	return anno_rif;
}
public void setAnno_rif(java.lang.Integer anno_rif) {
	this.anno_rif = anno_rif;
}
public java.lang.Integer getNum_dich() {
	return num_dich;
}
public void setNum_dich(java.lang.Integer num_dich) {
	this.num_dich = num_dich;
}
public java.math.BigDecimal getIm_limite_op() {
	return im_limite_op;
}
public void setIm_limite_op(java.math.BigDecimal im_limite_op) {
	this.im_limite_op = im_limite_op;
}
public java.math.BigDecimal getIm_limite_sing_op() {
	return im_limite_sing_op;
}
public void setIm_limite_sing_op(java.math.BigDecimal im_limite_sing_op) {
	this.im_limite_sing_op = im_limite_sing_op;
}

}
