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
* Date 18/02/2005
*/
package it.cnr.contab.config00.sto.bulk;
import it.cnr.jada.persistency.Keyed;
public class DipartimentoBase extends DipartimentoKey implements Keyed {
//    DS_DIPARTIMENTO VARCHAR(300) NOT NULL
	private java.lang.String ds_dipartimento;
 
//    DT_ISTITUZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dt_istituzione;
 
//    DS_DEL_IST VARCHAR(300) NOT NULL
	private java.lang.String ds_del_ist;
 
//    CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;
 
//    DT_SOPPRESSIONE TIMESTAMP(7)
	private java.sql.Timestamp dt_soppressione;
 
//    DS_DEL_SOPPR VARCHAR(300)
	private java.lang.String ds_del_soppr;
 
//  ID_DIPARTIMENTO DECIMAL(8,0) NOT NULL
	private java.lang.Integer id_dipartimento;
	
	public DipartimentoBase() {
		super();
	}
	public DipartimentoBase(java.lang.String cd_dipartimento) {
		super(cd_dipartimento);
	}
	public java.lang.String getDs_dipartimento () {
		return ds_dipartimento;
	}
	public void setDs_dipartimento(java.lang.String ds_dipartimento)  {
		this.ds_dipartimento=ds_dipartimento;
	}
	public java.sql.Timestamp getDt_istituzione () {
		return dt_istituzione;
	}
	public void setDt_istituzione(java.sql.Timestamp dt_istituzione)  {
		this.dt_istituzione=dt_istituzione;
	}
	public java.lang.String getDs_del_ist () {
		return ds_del_ist;
	}
	public void setDs_del_ist(java.lang.String ds_del_ist)  {
		this.ds_del_ist=ds_del_ist;
	}
	public java.lang.Integer getCd_terzo () {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.sql.Timestamp getDt_soppressione () {
		return dt_soppressione;
	}
	public void setDt_soppressione(java.sql.Timestamp dt_soppressione)  {
		this.dt_soppressione=dt_soppressione;
	}
	public java.lang.String getDs_del_soppr () {
		return ds_del_soppr;
	}
	public void setDs_del_soppr(java.lang.String ds_del_soppr)  {
		this.ds_del_soppr=ds_del_soppr;
	}
	public java.lang.Integer getId_dipartimento() {
		return id_dipartimento;
	}
	public void setId_dipartimento(java.lang.Integer id_dipartimento) {
		this.id_dipartimento = id_dipartimento;
	}
}