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

package it.cnr.contab.doccont00.core.bulk;

/**
 * Insert the type's description here.
 * Creation date: (01/08/2001 11.33.04)
 * @author: Ilaria Gorla
 */
public class V_pdg_obbligazione_speKey extends it.cnr.jada.bulk.OggettoBulk implements it.cnr.jada.persistency.KeyedPersistent{

	// TI_APPARTENENZA CHAR(1) NOT NULL
	private java.lang.String ti_appartenenza;

	// CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;

	// ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

	// ESERCIZIO_RES DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_res;

	// CD_LINEA_ATTIVITA VARCHAR(10) NOT NULL
	private java.lang.String cd_linea_attivita;

	// CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL
	private java.lang.String cd_centro_responsabilita;

	// STATO CHAR(1) NOT NULL
	private java.lang.String stato;

	// CATEGORIA DETTAGLIO VARCHAR(3) NOT NULL
	private java.lang.String categoria_dettaglio;

	// TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;

public V_pdg_obbligazione_speKey() {
	super();
}
public V_pdg_obbligazione_speKey(java.lang.String categoria_dettaglio,java.lang.String cd_centro_responsabilita,java.lang.String cd_elemento_voce,java.lang.String cd_linea_attivita,java.lang.Integer esercizio,java.lang.String stato,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	this.categoria_dettaglio = categoria_dettaglio;
	this.cd_centro_responsabilita = cd_centro_responsabilita;
	this.cd_elemento_voce = cd_elemento_voce;
	this.cd_linea_attivita = cd_linea_attivita;
	this.esercizio = esercizio;
	this.stato = stato;
	this.ti_appartenenza = ti_appartenenza;
	this.ti_gestione = ti_gestione;
}
/**
 * Insert the method's description here.
 * Creation date: (11/09/2001 11.29.30)
 * @return java.lang.String
 */
public java.lang.String getCategoria_dettaglio() {
	return categoria_dettaglio;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @return java.lang.String
 */
public java.lang.String getCd_centro_responsabilita() {
	return cd_centro_responsabilita;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @return java.lang.String
 */
public java.lang.String getCd_elemento_voce() {
	return cd_elemento_voce;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @return java.lang.String
 */
public java.lang.String getCd_linea_attivita() {
	return cd_linea_attivita;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @return java.lang.Integer
 */
public java.lang.Integer getEsercizio() {
	return esercizio;
}
/**
 * @return
 */
public java.lang.Integer getEsercizio_res() {
	return esercizio_res;
}
/**
 * Insert the method's description here.
 * Creation date: (11/09/2001 11.29.30)
 * @return java.lang.String
 */
public java.lang.String getStato() {
	return stato;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @return java.lang.String
 */
public java.lang.String getTi_appartenenza() {
	return ti_appartenenza;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @return java.lang.String
 */
public java.lang.String getTi_gestione() {
	return ti_gestione;
}
/**
 * Insert the method's description here.
 * Creation date: (11/09/2001 11.29.30)
 * @param newCategoria_dettaglio java.lang.String
 */
public void setCategoria_dettaglio(java.lang.String newCategoria_dettaglio) {
	categoria_dettaglio = newCategoria_dettaglio;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @param newCd_centro_responsabilita java.lang.String
 */
public void setCd_centro_responsabilita(java.lang.String newCd_centro_responsabilita) {
	cd_centro_responsabilita = newCd_centro_responsabilita;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @param newCd_elemento_voce java.lang.String
 */
public void setCd_elemento_voce(java.lang.String newCd_elemento_voce) {
	cd_elemento_voce = newCd_elemento_voce;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @param newCd_linea_attivita java.lang.String
 */
public void setCd_linea_attivita(java.lang.String newCd_linea_attivita) {
	cd_linea_attivita = newCd_linea_attivita;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @param newEsercizio java.lang.Integer
 */
public void setEsercizio(java.lang.Integer newEsercizio) {
	esercizio = newEsercizio;
}
/**
 * @param integer
 */
public void setEsercizio_res(java.lang.Integer integer) {
	esercizio_res = integer;
}
/**
 * Insert the method's description here.
 * Creation date: (11/09/2001 11.29.30)
 * @param newStato java.lang.String
 */
public void setStato(java.lang.String newStato) {
	stato = newStato;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @param newTi_appartenenza java.lang.String
 */
public void setTi_appartenenza(java.lang.String newTi_appartenenza) {
	ti_appartenenza = newTi_appartenenza;
}
/**
 * Insert the method's description here.
 * Creation date: (01/08/2001 15:25:03)
 * @param newTi_gestione java.lang.String
 */
public void setTi_gestione(java.lang.String newTi_gestione) {
	ti_gestione = newTi_gestione;
}
}
