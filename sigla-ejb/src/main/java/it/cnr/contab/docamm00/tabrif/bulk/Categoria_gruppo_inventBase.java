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

package it.cnr.contab.docamm00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Categoria_gruppo_inventBase extends Categoria_gruppo_inventKey implements Keyed {
	// CD_CATEGORIA_PADRE VARCHAR(10)
	private java.lang.String cd_categoria_padre;

	// CD_PROPRIO VARCHAR(4) NOT NULL
	private java.lang.String cd_proprio;

	// DATA_CANCELLAZIONE TIMESTAMP
	private java.sql.Timestamp data_cancellazione;

	// DS_CATEGORIA_GRUPPO VARCHAR(100) NOT NULL
	private java.lang.String ds_categoria_gruppo;

	// FL_AMMORTAMENTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_ammortamento;

	// FL_GESTIONE_INVENTARIO CHAR(1) NOT NULL
	private java.lang.Boolean fl_gestione_inventario;

	// FL_GESTIONE_MAGAZZINO CHAR(1) NOT NULL
	private java.lang.Boolean fl_gestione_magazzino;

	// LIVELLO DECIMAL(2,0) NOT NULL
	private java.lang.Integer livello;
	// FL_GESTIONE_TARGA CHAR(1) NOT NULL
	private java.lang.Boolean fl_gestione_targa;

	private java.lang.Boolean fl_gestione_seriale;


public Categoria_gruppo_inventBase() {
	super();
}
public Categoria_gruppo_inventBase(java.lang.String cd_categoria_gruppo) {
	super(cd_categoria_gruppo);
}
/* 
 * Getter dell'attributo cd_categoria_padre
 */
public java.lang.String getCd_categoria_padre() {
	return cd_categoria_padre;
}
/* 
 * Getter dell'attributo cd_proprio
 */
public java.lang.String getCd_proprio() {
	return cd_proprio;
}
/* 
 * Getter dell'attributo data_cancellazione
 */
public java.sql.Timestamp getData_cancellazione() {
	return data_cancellazione;
}
/* 
 * Getter dell'attributo ds_categoria_gruppo
 */
public java.lang.String getDs_categoria_gruppo() {
	return ds_categoria_gruppo;
}
/* 
 * Getter dell'attributo fl_ammortamento
 */
public java.lang.Boolean getFl_ammortamento() {
	return fl_ammortamento;
}
/* 
 * Getter dell'attributo fl_gestione_inventario
 */
public java.lang.Boolean getFl_gestione_inventario() {
	return fl_gestione_inventario;
}
/* 
 * Getter dell'attributo fl_gestione_magazzino
 */
public java.lang.Boolean getFl_gestione_magazzino() {
	return fl_gestione_magazzino;
}
/* 
 * Getter dell'attributo livello
 */
public java.lang.Integer getLivello() {
	return livello;
}
/* 
 * Setter dell'attributo cd_categoria_padre
 */
public void setCd_categoria_padre(java.lang.String cd_categoria_padre) {
	this.cd_categoria_padre = cd_categoria_padre;
}
/* 
 * Setter dell'attributo cd_proprio
 */
public void setCd_proprio(java.lang.String cd_proprio) {
	this.cd_proprio = cd_proprio;
}
/* 
 * Setter dell'attributo data_cancellazione
 */
public void setData_cancellazione(java.sql.Timestamp data_cancellazione) {
	this.data_cancellazione = data_cancellazione;
}
/* 
 * Setter dell'attributo ds_categoria_gruppo
 */
public void setDs_categoria_gruppo(java.lang.String ds_categoria_gruppo) {
	this.ds_categoria_gruppo = ds_categoria_gruppo;
}
/* 
 * Setter dell'attributo fl_ammortamento
 */
public void setFl_ammortamento(java.lang.Boolean fl_ammortamento) {
	this.fl_ammortamento = fl_ammortamento;
}
/* 
 * Setter dell'attributo fl_gestione_inventario
 */
public void setFl_gestione_inventario(java.lang.Boolean fl_gestione_inventario) {
	this.fl_gestione_inventario = fl_gestione_inventario;
}
/* 
 * Setter dell'attributo fl_gestione_magazzino
 */
public void setFl_gestione_magazzino(java.lang.Boolean fl_gestione_magazzino) {
	this.fl_gestione_magazzino = fl_gestione_magazzino;
}
/* 
 * Setter dell'attributo livello
 */
public void setLivello(java.lang.Integer livello) {
	this.livello = livello;
}
public java.lang.Boolean getFl_gestione_targa() {
	return fl_gestione_targa;
}
public void setFl_gestione_targa(java.lang.Boolean fl_gestione_targa) {
	this.fl_gestione_targa = fl_gestione_targa;
}
public java.lang.Boolean getFl_gestione_seriale() {
	return fl_gestione_seriale;
}
public void setFl_gestione_seriale(java.lang.Boolean fl_gestione_seriale) {
	this.fl_gestione_seriale = fl_gestione_seriale;
}
}
