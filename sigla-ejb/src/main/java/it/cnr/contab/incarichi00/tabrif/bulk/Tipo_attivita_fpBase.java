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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_attivita_fpBase extends Tipo_attivita_fpKey implements Keyed {
//    DS_TIPO_ATTIVITA VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_attivita;
 
//    LIVELLO NUMBER(2) NOT NULL
	private java.lang.Integer livello;

//    CD_TIPO_ATTIVITA_PADRE NUMBER(2)
	private java.lang.String cd_tipo_attivita_padre;

//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;
 
	public Tipo_attivita_fpBase() {
		super();
	}
	public Tipo_attivita_fpBase(java.lang.String cd_tipo_attivita) {
		super(cd_tipo_attivita);
	}
	public java.lang.String getDs_tipo_attivita() {
		return ds_tipo_attivita;
	}
	public void setDs_tipo_attivita(java.lang.String ds_tipo_attivita)  {
		this.ds_tipo_attivita=ds_tipo_attivita;
	}
	public java.lang.Integer getLivello() {
		return livello;
	}
	public void setLivello(java.lang.Integer livello) {
		this.livello = livello;
	}
    public java.lang.String getCd_tipo_attivita_padre() {
		return cd_tipo_attivita_padre;
	}
    public void setCd_tipo_attivita_padre(java.lang.String cd_tipo_attivita_padre) {
		this.cd_tipo_attivita_padre = cd_tipo_attivita_padre;
	}
	public java.lang.Boolean getFl_cancellato() {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
}