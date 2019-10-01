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

package it.cnr.contab.pdg00.cdip.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Costo_del_dipendenteBase extends Costo_del_dipendenteKey implements Keyed {
	// CD_LIVELLO_1 VARCHAR(3)
	private java.lang.String cd_livello_1;

	// CD_LIVELLO_2 VARCHAR(3)
	private java.lang.String cd_livello_2;

	// CD_LIVELLO_3 VARCHAR(3)
	private java.lang.String cd_livello_3;

	// CD_PROFILO DECIMAL(4,0)
	private java.lang.Integer cd_profilo;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;

	// DS_PROFILO VARCHAR(100)
	private java.lang.String ds_profilo;

	// DT_SCAD_CONTRATTO TIMESTAMP
	private java.sql.Timestamp dt_scad_contratto;

	// DT_SCARICO TIMESTAMP
	private java.sql.Timestamp dt_scarico;

	// IM_A1 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_a1;

	// IM_A2 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_a2;

	// IM_A3 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_a3;

	// IM_ONERI_CNR_A1 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_oneri_cnr_a1;

	// IM_ONERI_CNR_A2 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_oneri_cnr_a2;

	// IM_ONERI_CNR_A3 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_oneri_cnr_a3;

	// IM_TFR_A1 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_tfr_a1;

	// IM_TFR_A2 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_tfr_a2;

	// IM_TFR_A3 DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_tfr_a3;

	// NOMINATIVO VARCHAR(60)
	private java.lang.String nominativo;

	// TI_RAPPORTO VARCHAR(5) NOT NULL
	private java.lang.String ti_rapporto;

	// ORIGINE_FONTI VARCHAR(3) NULL
	private java.lang.String origine_fonti;
	
	// FL_RAPPORTO13 VARCHAR(1) NOT NULL
	private java.lang.Boolean fl_rapporto13;

public Costo_del_dipendenteBase() {
	super();
}
public Costo_del_dipendenteBase(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String id_matricola,java.lang.Integer mese,java.lang.String ti_appartenenza,java.lang.String ti_gestione,java.lang.String ti_prev_cons) {
	super(cd_elemento_voce,esercizio,id_matricola,mese,ti_appartenenza,ti_gestione,ti_prev_cons);
}
/* 
 * Getter dell'attributo cd_livello_1
 */
public java.lang.String getCd_livello_1() {
	return cd_livello_1;
}
/* 
 * Getter dell'attributo cd_livello_2
 */
public java.lang.String getCd_livello_2() {
	return cd_livello_2;
}
/* 
 * Getter dell'attributo cd_livello_3
 */
public java.lang.String getCd_livello_3() {
	return cd_livello_3;
}
/* 
 * Getter dell'attributo cd_profilo
 */
public java.lang.Integer getCd_profilo() {
	return cd_profilo;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo ds_profilo
 */
public java.lang.String getDs_profilo() {
	return ds_profilo;
}
/* 
 * Getter dell'attributo dt_scad_contratto
 */
public java.sql.Timestamp getDt_scad_contratto() {
	return dt_scad_contratto;
}
/* 
 * Getter dell'attributo dt_scarico
 */
public java.sql.Timestamp getDt_scarico() {
	return dt_scarico;
}
/* 
 * Getter dell'attributo im_a1
 */
public java.math.BigDecimal getIm_a1() {
	return im_a1;
}
/* 
 * Getter dell'attributo im_a2
 */
public java.math.BigDecimal getIm_a2() {
	return im_a2;
}
/* 
 * Getter dell'attributo im_a3
 */
public java.math.BigDecimal getIm_a3() {
	return im_a3;
}
/* 
 * Getter dell'attributo im_oneri_cnr_a1
 */
public java.math.BigDecimal getIm_oneri_cnr_a1() {
	return im_oneri_cnr_a1;
}
/* 
 * Getter dell'attributo im_oneri_cnr_a2
 */
public java.math.BigDecimal getIm_oneri_cnr_a2() {
	return im_oneri_cnr_a2;
}
/* 
 * Getter dell'attributo im_oneri_cnr_a3
 */
public java.math.BigDecimal getIm_oneri_cnr_a3() {
	return im_oneri_cnr_a3;
}
/* 
 * Getter dell'attributo im_tfr_a1
 */
public java.math.BigDecimal getIm_tfr_a1() {
	return im_tfr_a1;
}
/* 
 * Getter dell'attributo im_tfr_a2
 */
public java.math.BigDecimal getIm_tfr_a2() {
	return im_tfr_a2;
}
/* 
 * Getter dell'attributo im_tfr_a3
 */
public java.math.BigDecimal getIm_tfr_a3() {
	return im_tfr_a3;
}
/* 
 * Getter dell'attributo nominativo
 */
public java.lang.String getNominativo() {
	return nominativo;
}
/* 
 * Getter dell'attributo ti_rapporto
 */
public java.lang.String getTi_rapporto() {
	return ti_rapporto;
}
/* 
 * Setter dell'attributo cd_livello_1
 */
public void setCd_livello_1(java.lang.String cd_livello_1) {
	this.cd_livello_1 = cd_livello_1;
}
/* 
 * Setter dell'attributo cd_livello_2
 */
public void setCd_livello_2(java.lang.String cd_livello_2) {
	this.cd_livello_2 = cd_livello_2;
}
/* 
 * Setter dell'attributo cd_livello_3
 */
public void setCd_livello_3(java.lang.String cd_livello_3) {
	this.cd_livello_3 = cd_livello_3;
}
/* 
 * Setter dell'attributo cd_profilo
 */
public void setCd_profilo(java.lang.Integer cd_profilo) {
	this.cd_profilo = cd_profilo;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo ds_profilo
 */
public void setDs_profilo(java.lang.String ds_profilo) {
	this.ds_profilo = ds_profilo;
}
/* 
 * Setter dell'attributo dt_scad_contratto
 */
public void setDt_scad_contratto(java.sql.Timestamp dt_scad_contratto) {
	this.dt_scad_contratto = dt_scad_contratto;
}
/* 
 * Setter dell'attributo dt_scarico
 */
public void setDt_scarico(java.sql.Timestamp dt_scarico) {
	this.dt_scarico = dt_scarico;
}
/* 
 * Setter dell'attributo im_a1
 */
public void setIm_a1(java.math.BigDecimal im_a1) {
	this.im_a1 = im_a1;
}
/* 
 * Setter dell'attributo im_a2
 */
public void setIm_a2(java.math.BigDecimal im_a2) {
	this.im_a2 = im_a2;
}
/* 
 * Setter dell'attributo im_a3
 */
public void setIm_a3(java.math.BigDecimal im_a3) {
	this.im_a3 = im_a3;
}
/* 
 * Setter dell'attributo im_oneri_cnr_a1
 */
public void setIm_oneri_cnr_a1(java.math.BigDecimal im_oneri_cnr_a1) {
	this.im_oneri_cnr_a1 = im_oneri_cnr_a1;
}
/* 
 * Setter dell'attributo im_oneri_cnr_a2
 */
public void setIm_oneri_cnr_a2(java.math.BigDecimal im_oneri_cnr_a2) {
	this.im_oneri_cnr_a2 = im_oneri_cnr_a2;
}
/* 
 * Setter dell'attributo im_oneri_cnr_a3
 */
public void setIm_oneri_cnr_a3(java.math.BigDecimal im_oneri_cnr_a3) {
	this.im_oneri_cnr_a3 = im_oneri_cnr_a3;
}
/* 
 * Setter dell'attributo im_tfr_a1
 */
public void setIm_tfr_a1(java.math.BigDecimal im_tfr_a1) {
	this.im_tfr_a1 = im_tfr_a1;
}
/* 
 * Setter dell'attributo im_tfr_a2
 */
public void setIm_tfr_a2(java.math.BigDecimal im_tfr_a2) {
	this.im_tfr_a2 = im_tfr_a2;
}
/* 
 * Setter dell'attributo im_tfr_a3
 */
public void setIm_tfr_a3(java.math.BigDecimal im_tfr_a3) {
	this.im_tfr_a3 = im_tfr_a3;
}
/* 
 * Setter dell'attributo nominativo
 */
public void setNominativo(java.lang.String nominativo) {
	this.nominativo = nominativo;
}
/* 
 * Setter dell'attributo ti_rapporto
 */
public void setTi_rapporto(java.lang.String ti_rapporto) {
	this.ti_rapporto = ti_rapporto;
}
public java.lang.String getOrigine_fonti() {
	return origine_fonti;
}
public void setOrigine_fonti(java.lang.String origine_fonti) {
	this.origine_fonti = origine_fonti;
}
public java.lang.Boolean getFl_rapporto13() {
	return fl_rapporto13;
}
public void setFl_rapporto13(java.lang.Boolean fl_rapporto13) {
	this.fl_rapporto13 = fl_rapporto13;
}
}
