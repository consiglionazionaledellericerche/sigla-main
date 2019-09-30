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
 * Date 27/11/2006
 */
package it.cnr.contab.progettiric00.geco.bulk;
import it.cnr.jada.persistency.Keyed;
public class Geco_progetto_rstlBase extends Geco_progetto_rstlKey implements Keyed {
//    cod_prog VARCHAR(10) NOT NULL
	private java.lang.String cod_prog;
 
//    id_dip DECIMAL(10,0)
	private java.lang.Long id_dip;
 
//    tipo_prog DECIMAL(10,0)
	private java.lang.Long tipo_prog;
 
//    descr_prog VARCHAR(255) NOT NULL
	private java.lang.String descr_prog;
 
//    data_istituzione_prog TIMESTAMP(7)
	private java.sql.Timestamp data_istituzione_prog;
 
//    ordine_prog DECIMAL(10,0)
	private java.lang.Long ordine_prog;
 
	public Geco_progetto_rstlBase() {
		super();
	}
	public Geco_progetto_rstlBase(java.lang.Long id_prog, java.lang.Long esercizio, java.lang.String fase) {
		super(id_prog, esercizio, fase);
	}
	public java.lang.String getCod_prog() {
		return cod_prog;
	}
	public void setCod_prog(java.lang.String cod_prog)  {
		this.cod_prog=cod_prog;
	}
	public java.lang.Long getId_dip() {
		return id_dip;
	}
	public void setId_dip(java.lang.Long id_dip)  {
		this.id_dip=id_dip;
	}
	public java.lang.Long getTipo_prog() {
		return tipo_prog;
	}
	public void setTipo_prog(java.lang.Long tipo_prog)  {
		this.tipo_prog=tipo_prog;
	}
	public java.lang.String getDescr_prog() {
		return descr_prog;
	}
	public void setDescr_prog(java.lang.String descr_prog)  {
		this.descr_prog=descr_prog;
	}
	public java.sql.Timestamp getData_istituzione_prog() {
		return data_istituzione_prog;
	}
	public void setData_istituzione_prog(java.sql.Timestamp data_istituzione_prog)  {
		this.data_istituzione_prog=data_istituzione_prog;
	}
	public java.lang.Long getOrdine_prog() {
		return ordine_prog;
	}
	public void setOrdine_prog(java.lang.Long ordine_prog)  {
		this.ordine_prog=ordine_prog;
	}
}