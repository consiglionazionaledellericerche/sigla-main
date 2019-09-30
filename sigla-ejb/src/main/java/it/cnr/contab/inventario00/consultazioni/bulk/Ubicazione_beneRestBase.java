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

package it.cnr.contab.inventario00.consultazioni.bulk;

import it.cnr.jada.persistency.Keyed;

public class Ubicazione_beneRestBase extends Ubicazione_beneRestKey implements Keyed {
	// CD_UBICAZIONE_PADRE VARCHAR(30)
	private java.lang.String cd_ubicazione_padre;
	//	CD_CDS_PADRE VARCHAR(30)
	private java.lang.String cd_cds_padre;
//	CD_UO_PADRE VARCHAR(30)
	private java.lang.String cd_uo_padre;
	
	// CD_UBICAZIONE_PROPRIA VARCHAR(5) NOT NULL
	private java.lang.String cd_ubicazione_propria;

	// DS_UBICAZIONE_BENE VARCHAR(300) NOT NULL
	private java.lang.String ds_ubicazione_bene;

	// FL_UBICAZIONE_DEFAULT CHAR(1) NOT NULL
	private java.lang.Boolean fl_ubicazione_default;

	// LIVELLO DECIMAL(2,0) NOT NULL
	private java.lang.Integer livello;

public Ubicazione_beneRestBase() {
	super();
}
public Ubicazione_beneRestBase(java.lang.String cd_cds,java.lang.String cd_ubicazione,java.lang.String cd_unita_organizzativa) {
	super(cd_cds,cd_ubicazione,cd_unita_organizzativa);
}
/* 
 * Getter dell'attributo cd_ubicazione_padre
 */
public java.lang.String getCd_ubicazione_padre() {
	return cd_ubicazione_padre;
}
/* 
 * Getter dell'attributo cd_ubicazione_propria
 */
public java.lang.String getCd_ubicazione_propria() {
	return cd_ubicazione_propria;
}
/* 
 * Getter dell'attributo ds_ubicazione_bene
 */
public java.lang.String getDs_ubicazione_bene() {
	return ds_ubicazione_bene;
}
/* 
 * Getter dell'attributo fl_ubicazione_default
 */
public java.lang.Boolean getFl_ubicazione_default() {
	return fl_ubicazione_default;
}
/* 
 * Getter dell'attributo livello
 */
public java.lang.Integer getLivello() {
	return livello;
}
/* 
 * Setter dell'attributo cd_ubicazione_padre
 */
public void setCd_ubicazione_padre(java.lang.String cd_ubicazione_padre) {
	this.cd_ubicazione_padre = cd_ubicazione_padre;
}
/* 
 * Setter dell'attributo cd_ubicazione_propria
 */
public void setCd_ubicazione_propria(java.lang.String cd_ubicazione_propria) {
	this.cd_ubicazione_propria = cd_ubicazione_propria;
}
/* 
 * Setter dell'attributo ds_ubicazione_bene
 */
public void setDs_ubicazione_bene(java.lang.String ds_ubicazione_bene) {
	this.ds_ubicazione_bene = ds_ubicazione_bene;
}
/* 
 * Setter dell'attributo fl_ubicazione_default
 */
public void setFl_ubicazione_default(java.lang.Boolean fl_ubicazione_default) {
	this.fl_ubicazione_default = fl_ubicazione_default;
}
/* 
 * Setter dell'attributo livello
 */
	public void setLivello(java.lang.Integer livello) {
		this.livello = livello;
	}
	public java.lang.String getCd_cds_padre() {
		return cd_cds_padre;
	}
	public java.lang.String getCd_uo_padre() {
		return cd_uo_padre;
	}
	
	public void setCd_cds_padre(java.lang.String string) {
		cd_cds_padre = string;
	}
	
	public void setCd_uo_padre(java.lang.String string) {
		cd_uo_padre = string;
	}

}
