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

public class Categoria_beneBase extends Categoria_beneKey implements Keyed {
	// DS_CATEGORIA_BENE VARCHAR(100) NOT NULL
	private java.lang.String ds_categoria_bene;

/* 
 * Getter dell'attributo ds_categoria_bene
 */
public java.lang.String getDs_categoria_bene() {
	return ds_categoria_bene;
}

/* 
 * Setter dell'attributo ds_categoria_bene
 */
public void setDs_categoria_bene(java.lang.String ds_categoria_bene) {
	this.ds_categoria_bene = ds_categoria_bene;
}

public Categoria_beneBase() {
	super();
}

public Categoria_beneBase(java.lang.String cd_categoria_bene) {
	super(cd_categoria_bene);
}
}
