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

/*
* Created by Generator 1.0
* Date 17/11/2005
*/
package it.cnr.contab.config00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Parametri_uoBase extends Parametri_uoKey implements Keyed {
//    FL_GESTIONE_MODULI CHAR(1) NOT NULL
	private java.lang.Boolean fl_gestione_moduli;
 
//    PG_MODULO_DEFAULT DECIMAL(10,0)
	private java.lang.Integer pg_modulo_default;
 
	public Parametri_uoBase() {
		super();
	}
	public Parametri_uoBase(java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio) {
		super(cd_unita_organizzativa, esercizio);
	}
	public java.lang.Boolean getFl_gestione_moduli () {
		return fl_gestione_moduli;
	}
	public void setFl_gestione_moduli(java.lang.Boolean fl_gestione_moduli)  {
		this.fl_gestione_moduli=fl_gestione_moduli;
	}
	public java.lang.Integer getPg_modulo_default () {
		return pg_modulo_default;
	}
	public void setPg_modulo_default(java.lang.Integer pg_modulo_default)  {
		this.pg_modulo_default=pg_modulo_default;
	}
}