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

public class V_dipendenteBase extends V_dipendenteKey implements Persistent {
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

	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

	// ID_MATRICOLA VARCHAR(10) NOT NULL
	private java.lang.String id_matricola;

	// MESE DECIMAL(2,0) NOT NULL
	private java.lang.Integer mese;

	// NOMINATIVO VARCHAR(60)
	private java.lang.String nominativo;

	// TI_PREV_CONS VARCHAR(10) NOT NULL
	private java.lang.String ti_prev_cons;

	// ORIGINE_FONTI VARCHAR(3) NULL
	private java.lang.String origine_fonti;
	
public V_dipendenteBase() {
	super();
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
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo id_matricola
 */
public java.lang.String getId_matricola() {
	return id_matricola;
}
/* 
 * Getter dell'attributo mese
 */
public java.lang.Integer getMese() {
	return mese;
}
/* 
 * Getter dell'attributo nominativo
 */
public java.lang.String getNominativo() {
	return nominativo;
}
/* 
 * Getter dell'attributo ti_prev_cons
 */
public java.lang.String getTi_prev_cons() {
	return ti_prev_cons;
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
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo id_matricola
 */
public void setId_matricola(java.lang.String id_matricola) {
	this.id_matricola = id_matricola;
}
/* 
 * Setter dell'attributo mese
 */
public void setMese(java.lang.Integer mese) {
	this.mese = mese;
}
/* 
 * Setter dell'attributo nominativo
 */
public void setNominativo(java.lang.String nominativo) {
	this.nominativo = nominativo;
}
/* 
 * Setter dell'attributo ti_prev_cons
 */
public void setTi_prev_cons(java.lang.String ti_prev_cons) {
	this.ti_prev_cons = ti_prev_cons;
}
public java.lang.String getOrigine_fonti() {
	return origine_fonti;
}
public void setOrigine_fonti(java.lang.String origine_fonti) {
	this.origine_fonti = origine_fonti;
}
}
