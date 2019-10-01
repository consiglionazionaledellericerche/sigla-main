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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Voce_fBase extends Voce_fKey implements Keyed {
	// CD_CATEGORIA VARCHAR(50)
	private java.lang.String cd_categoria;

	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// CD_CENTRO_RESPONSABILITA VARCHAR(30)
	private java.lang.String cd_centro_responsabilita;

	// CD_FUNZIONE VARCHAR(2)
	private java.lang.String cd_funzione;

	// CD_NATURA VARCHAR(1)
	private java.lang.String cd_natura;

	// CD_PARTE VARCHAR(50)
	private java.lang.String cd_parte;

	// CD_PROPRIO_VOCE VARCHAR(50)
	private java.lang.String cd_proprio_voce;

	// CD_SEZIONE_CAPITOLO VARCHAR(50)
	private java.lang.String cd_sezione_capitolo;

	// CD_TITOLO_CAPITOLO VARCHAR(50)
	private java.lang.String cd_titolo_capitolo;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cd_unita_organizzativa;

	// CD_VOCE_PADRE VARCHAR(50)
	private java.lang.String cd_voce_padre;

	// DS_VOCE VARCHAR(300)
	private java.lang.String ds_voce;

	// FL_MASTRINO CHAR(1)
	private java.lang.Boolean fl_mastrino;

	// LIVELLO DECIMAL(2,0)
	private java.lang.Integer livello;

	// TI_VOCE CHAR(1)
	private java.lang.String ti_voce;

	// CD_ELEMENTO_VOCE VARCHAR(50)
	private java.lang.String cd_elemento_voce;

public Voce_fBase() {
	super();
}
public Voce_fBase(java.lang.String cd_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_voce,esercizio,ti_appartenenza,ti_gestione);
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
 * Getter dell'attributo cd_centro_responsabilita
 */
public java.lang.String getCd_centro_responsabilita() {
	return cd_centro_responsabilita;
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
 * Getter dell'attributo cd_sezione_capitolo
 */
public java.lang.String getCd_sezione_capitolo() {
	return cd_sezione_capitolo;
}
/* 
 * Getter dell'attributo cd_titolo_capitolo
 */
public java.lang.String getCd_titolo_capitolo() {
	return cd_titolo_capitolo;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
/* 
 * Getter dell'attributo cd_voce_padre
 */
public java.lang.String getCd_voce_padre() {
	return cd_voce_padre;
}
/* 
 * Getter dell'attributo ds_voce
 */
public java.lang.String getDs_voce() {
	return ds_voce;
}
/* 
 * Getter dell'attributo fl_mastrino
 */
public java.lang.Boolean getFl_mastrino() {
	return fl_mastrino;
}
/* 
 * Getter dell'attributo livello
 */
public java.lang.Integer getLivello() {
	return livello;
}
/* 
 * Getter dell'attributo ti_voce
 */
public java.lang.String getTi_voce() {
	return ti_voce;
}
/* 
 * Getter dell'attributo cd_elemento_voce
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
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
 * Setter dell'attributo cd_centro_responsabilita
 */
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.cd_centro_responsabilita = cd_centro_responsabilita;
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
 * Setter dell'attributo cd_sezione_capitolo
 */
public void setCd_sezione_capitolo(java.lang.String cd_sezione_capitolo) {
	this.cd_sezione_capitolo = cd_sezione_capitolo;
}
/* 
 * Setter dell'attributo cd_titolo_capitolo
 */
public void setCd_titolo_capitolo(java.lang.String cd_titolo_capitolo) {
	this.cd_titolo_capitolo = cd_titolo_capitolo;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
/* 
 * Setter dell'attributo cd_voce_padre
 */
public void setCd_voce_padre(java.lang.String cd_voce_padre) {
	this.cd_voce_padre = cd_voce_padre;
}
/* 
 * Setter dell'attributo ds_voce
 */
public void setDs_voce(java.lang.String ds_voce) {
	this.ds_voce = ds_voce;
}
/* 
 * Setter dell'attributo fl_mastrino
 */
public void setFl_mastrino(java.lang.Boolean fl_mastrino) {
	this.fl_mastrino = fl_mastrino;
}
/* 
 * Setter dell'attributo livello
 */
public void setLivello(java.lang.Integer livello) {
	this.livello = livello;
}
/* 
 * Setter dell'attributo ti_voce
 */
public void setTi_voce(java.lang.String ti_voce) {
	this.ti_voce = ti_voce;
}
/* 
 * Setter dell'attributo cd_elemento_voce
 */
public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
	this.cd_elemento_voce = cd_elemento_voce;
}
}
