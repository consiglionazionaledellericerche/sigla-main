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

package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Classificazione_montantiBase extends Classificazione_montantiKey implements Keyed {
	// DS_CLASSIFICAZIONE_MONTANTI VARCHAR(100) NOT NULL
	private java.lang.String ds_classificazione_montanti;

public Classificazione_montantiBase() {
	super();
}
public Classificazione_montantiBase(java.lang.Long pg_classificazione_montanti) {
	super(pg_classificazione_montanti);
}
/* 
 * Getter dell'attributo ds_classificazione_montanti
 */
public java.lang.String getDs_classificazione_montanti() {
	return ds_classificazione_montanti;
}
/* 
 * Setter dell'attributo ds_classificazione_montanti
 */
public void setDs_classificazione_montanti(java.lang.String ds_classificazione_montanti) {
	this.ds_classificazione_montanti = ds_classificazione_montanti;
}
}
