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
 * Date 29/11/2006
 */
package it.cnr.contab.config00.geco.bulk;
import it.cnr.jada.persistency.Keyed;
public class Geco_dipartimentiBase extends Geco_dipartimentiKey implements Keyed {
//    id_dip DECIMAL(10,0) NOT NULL
	private java.lang.Long id_dip;
 
//    descrizione VARCHAR(255) NOT NULL
	private java.lang.String descrizione;
 
//    data_istituzione TIMESTAMP(7)
	private java.sql.Timestamp data_istituzione;
 
//    ordine DECIMAL(10,0) NOT NULL
	private java.lang.Long ordine;
 
	public Geco_dipartimentiBase() {
		super();
	}
	public Geco_dipartimentiBase(java.lang.String cod_dip,java.lang.Long esercizio) {
		super(cod_dip,esercizio);
	}
	public java.lang.Long getId_dip() {
		return id_dip;
	}
	public void setId_dip(java.lang.Long id_dip)  {
		this.id_dip=id_dip;
	}
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
	}
	public java.sql.Timestamp getData_istituzione() {
		return data_istituzione;
	}
	public void setData_istituzione(java.sql.Timestamp data_istituzione)  {
		this.data_istituzione=data_istituzione;
	}
	public java.lang.Long getOrdine() {
		return ordine;
	}
	public void setOrdine(java.lang.Long ordine)  {
		this.ordine=ordine;
	}
}