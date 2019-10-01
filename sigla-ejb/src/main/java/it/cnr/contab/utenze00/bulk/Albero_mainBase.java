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

package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Albero_mainBase extends Albero_mainKey implements Keyed {
	// BUSINESS_PROCESS VARCHAR(200)
	private java.lang.String business_process;

	// CD_ACCESSO VARCHAR(20)
	private java.lang.String cd_accesso;

	// CD_NODO_PADRE VARCHAR(100)
	private java.lang.String cd_nodo_padre;

	// CD_PROPRIO_NODO VARCHAR(100)
	private java.lang.String cd_proprio_nodo;

	// DS_NODO VARCHAR(200)
	private java.lang.String ds_nodo;

	// FL_TERMINALE CHAR(1)
	private java.lang.Boolean fl_terminale;

	// LIVELLO DECIMAL(2,0)
	private java.lang.Integer livello;

	// PG_ORDINAMENTO DECIMAL(10,0)
	private java.lang.Long pg_ordinamento;

	// URL_ICONA VARCHAR(200)
	private java.lang.String url_icona;
	
	// URL_ICONA_OPEN VARCHAR(200)
	private java.lang.String url_icona_open;

public Albero_mainBase() {
	super();
}
public Albero_mainBase(java.lang.String cd_nodo) {
	super(cd_nodo);
}
/* 
 * Getter dell'attributo business_process
 */
public java.lang.String getBusiness_process() {
	return business_process;
}
/* 
 * Getter dell'attributo cd_accesso
 */
public java.lang.String getCd_accesso() {
	return cd_accesso;
}
/* 
 * Getter dell'attributo cd_nodo_padre
 */
public java.lang.String getCd_nodo_padre() {
	return cd_nodo_padre;
}
/* 
 * Getter dell'attributo cd_proprio_nodo
 */
public java.lang.String getCd_proprio_nodo() {
	return cd_proprio_nodo;
}
/* 
 * Getter dell'attributo ds_nodo
 */
public java.lang.String getDs_nodo() {
	return ds_nodo;
}
/* 
 * Getter dell'attributo fl_terminale
 */
public java.lang.Boolean getFl_terminale() {
	return fl_terminale;
}
/* 
 * Getter dell'attributo livello
 */
public java.lang.Integer getLivello() {
	return livello;
}
/* 
 * Getter dell'attributo pg_ordinamento
 */
public java.lang.Long getPg_ordinamento() {
	return pg_ordinamento;
}
/* 
 * Getter dell'attributo url_icona
 */
public java.lang.String getUrl_icona() {
	return url_icona;
}
/* 
 * Setter dell'attributo business_process
 */
public void setBusiness_process(java.lang.String business_process) {
	this.business_process = business_process;
}
/* 
 * Setter dell'attributo cd_accesso
 */
public void setCd_accesso(java.lang.String cd_accesso) {
	this.cd_accesso = cd_accesso;
}
/* 
 * Setter dell'attributo cd_nodo_padre
 */
public void setCd_nodo_padre(java.lang.String cd_nodo_padre) {
	this.cd_nodo_padre = cd_nodo_padre;
}
/* 
 * Setter dell'attributo cd_proprio_nodo
 */
public void setCd_proprio_nodo(java.lang.String cd_proprio_nodo) {
	this.cd_proprio_nodo = cd_proprio_nodo;
}
/* 
 * Setter dell'attributo ds_nodo
 */
public void setDs_nodo(java.lang.String ds_nodo) {
	this.ds_nodo = ds_nodo;
}
/* 
 * Setter dell'attributo fl_terminale
 */
public void setFl_terminale(java.lang.Boolean fl_terminale) {
	this.fl_terminale = fl_terminale;
}
/* 
 * Setter dell'attributo livello
 */
public void setLivello(java.lang.Integer livello) {
	this.livello = livello;
}
/* 
 * Setter dell'attributo pg_ordinamento
 */
public void setPg_ordinamento(java.lang.Long pg_ordinamento) {
	this.pg_ordinamento = pg_ordinamento;
}
/* 
 * Setter dell'attributo url_icona
 */
public void setUrl_icona(java.lang.String url_icona) {
	this.url_icona = url_icona;
}
	/**
	 * @return
	 */
	public java.lang.String getUrl_icona_open() {
		return url_icona_open;
	}

	/**
	 * @param string
	 */
	public void setUrl_icona_open(java.lang.String string) {
		url_icona_open = string;
	}

}
