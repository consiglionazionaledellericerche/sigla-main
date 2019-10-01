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

package it.cnr.contab.doccont00.singconto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class V_voce_f_sing_contoBase extends OggettoBulk  implements Persistent {
	// CD_CATEGORIA VARCHAR(50)
	private java.lang.String cd_categoria;

	// CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;

	// CD_CDS_PROPRIO VARCHAR(30)
	private java.lang.String cd_cds_proprio;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;

	// CD_FUNZIONE VARCHAR(2)
	private java.lang.String cd_funzione;

	// CD_NATURA VARCHAR(1)
	private java.lang.String cd_natura;

	// CD_PARTE VARCHAR(50)
	private java.lang.String cd_parte;

	// CD_PROPRIO_VOCE VARCHAR(50)
	private java.lang.String cd_proprio_voce;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;

	// CD_VOCE VARCHAR(50) NOT NULL
	private java.lang.String cd_voce;

	// DS_ELEMENTO_VOCE VARCHAR(100)
	private java.lang.String ds_elemento_voce;

	// DS_VOCE VARCHAR(300)
	private java.lang.String ds_voce;

	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

	// FL_PARTITA_GIRO CHAR(1)
	private java.lang.Boolean fl_partita_giro;

	// FL_VOCE_SAC CHAR(1) NOT NULL
	private java.lang.Boolean fl_voce_sac;

	// TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;

	// TI_COMPETENZA_RESIDUO CHAR(1) NOT NULL
	private java.lang.String ti_competenza_residuo;

	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;

public V_voce_f_sing_contoBase() {
	super();
}
/* 
 * Getter dell'attributo cd_categoria
 */
public java.lang.String getCd_categoria() {
	return cd_categoria;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_cds_proprio
 */
public java.lang.String getCd_cds_proprio() {
	return cd_cds_proprio;
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/* 
 * Getter dell'attributo cd_funzione
 */
public java.lang.String getCd_funzione() {
	return cd_funzione;
}
/* 
 * Getter dell'attributo cd_natura
 */
public java.lang.String getCd_natura() {
	return cd_natura;
}
/* 
 * Getter dell'attributo cd_parte
 */
public java.lang.String getCd_parte() {
	return cd_parte;
}
/* 
 * Getter dell'attributo cd_proprio_voce
 */
public java.lang.String getCd_proprio_voce() {
	return cd_proprio_voce;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo cd_voce
 */
public java.lang.String getCd_voce() {
	return cd_voce;
}
/* 
 * Getter dell'attributo ds_elemento_voce
 */
public java.lang.String getDs_elemento_voce() {
	return ds_elemento_voce;
}
/* 
 * Getter dell'attributo ds_voce
 */
public java.lang.String getDs_voce() {
	return ds_voce;
}
/* 
 * Getter dell'attributo esercizio
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo fl_partita_giro
 */
public java.lang.Boolean getFl_partita_giro() {
	return fl_partita_giro;
}
/* 
 * Getter dell'attributo fl_voce_sac
 */
public java.lang.Boolean getFl_voce_sac() {
	return fl_voce_sac;
}
/* 
 * Getter dell'attributo ti_appartenenza
 */
public java.lang.String getTi_appartenenza() {
	return ti_appartenenza;
}
/* 
 * Getter dell'attributo ti_competenza_residuo
 */
public java.lang.String getTi_competenza_residuo() {
	return ti_competenza_residuo;
}
/* 
 * Getter dell'attributo ti_gestione
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
/* 
 * Setter dell'attributo cd_categoria
 */
public void setCd_categoria(java.lang.String cd_categoria) {
	this.cd_categoria = cd_categoria;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_cds_proprio
 */
public void setCd_cds_proprio(java.lang.String cd_cds_proprio) {
	this.cd_cds_proprio = cd_cds_proprio;
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
/* 
 * Setter dell'attributo cd_funzione
 */
public void setCd_funzione(java.lang.String cd_funzione) {
	this.cd_funzione = cd_funzione;
}
/* 
 * Setter dell'attributo cd_natura
 */
public void setCd_natura(java.lang.String cd_natura) {
	this.cd_natura = cd_natura;
}
/* 
 * Setter dell'attributo cd_parte
 */
public void setCd_parte(java.lang.String cd_parte) {
	this.cd_parte = cd_parte;
}
/* 
 * Setter dell'attributo cd_proprio_voce
 */
public void setCd_proprio_voce(java.lang.String cd_proprio_voce) {
	this.cd_proprio_voce = cd_proprio_voce;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo cd_voce
 */
public void setCd_voce(java.lang.String cd_voce) {
	this.cd_voce = cd_voce;
}
/* 
 * Setter dell'attributo ds_elemento_voce
 */
public void setDs_elemento_voce(java.lang.String ds_elemento_voce) {
	this.ds_elemento_voce = ds_elemento_voce;
}
/* 
 * Setter dell'attributo ds_voce
 */
public void setDs_voce(java.lang.String ds_voce) {
	this.ds_voce = ds_voce;
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo fl_partita_giro
 */
public void setFl_partita_giro(java.lang.Boolean fl_partita_giro) {
	this.fl_partita_giro = fl_partita_giro;
}
/* 
 * Setter dell'attributo fl_voce_sac
 */
public void setFl_voce_sac(java.lang.Boolean fl_voce_sac) {
	this.fl_voce_sac = fl_voce_sac;
}
/* 
 * Setter dell'attributo ti_appartenenza
 */
public void setTi_appartenenza(java.lang.String ti_appartenenza) {
	this.ti_appartenenza = ti_appartenenza;
}
/* 
 * Setter dell'attributo ti_competenza_residuo
 */
public void setTi_competenza_residuo(java.lang.String ti_competenza_residuo) {
	this.ti_competenza_residuo = ti_competenza_residuo;
}
/* 
 * Setter dell'attributo ti_gestione
 */
public void setTi_gestione(java.lang.String ti_gestione) {
	this.ti_gestione = ti_gestione;
}
}
