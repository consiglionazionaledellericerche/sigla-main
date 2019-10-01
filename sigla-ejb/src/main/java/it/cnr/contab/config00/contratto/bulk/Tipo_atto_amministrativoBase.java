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
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_atto_amministrativoBase extends Tipo_atto_amministrativoKey implements Keyed {
//    DS_TIPO_ATTO VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_atto;
 
//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;

//	FL_NON_DEFINITO CHAR(1) NOT NULL
    private java.lang.Boolean fl_non_definito;
 
	public Tipo_atto_amministrativoBase() {
		super();
	}
	public Tipo_atto_amministrativoBase(java.lang.String cd_tipo_atto) {
		super(cd_tipo_atto);
	}
	public java.lang.String getDs_tipo_atto() {
		return ds_tipo_atto;
	}
	public void setDs_tipo_atto(java.lang.String ds_tipo_atto)  {
		this.ds_tipo_atto=ds_tipo_atto;
	}
	public java.lang.Boolean getFl_cancellato () {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
	/**
	 * @return
	 */
	public java.lang.Boolean getFl_non_definito() {
		return fl_non_definito;
	}
	
	/**
	 * @param boolean1
	 */
	public void setFl_non_definito(java.lang.Boolean boolean1) {
		fl_non_definito = boolean1;
	}

}