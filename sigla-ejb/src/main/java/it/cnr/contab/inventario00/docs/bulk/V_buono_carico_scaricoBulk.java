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
* Date 17/07/2006
*/
package it.cnr.contab.inventario00.docs.bulk;

import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_buono_carico_scaricoBulk extends OggettoBulk implements Persistent {
//    DS_BUONO_CARICO_SCARICO VARCHAR(100) NOT NULL
	private java.lang.String ds_buono_carico_scarico;
 
//    DATA_REGISTRAZIONE TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp data_registrazione;
 
//    CD_TIPO_CARICO_SCARICO VARCHAR(10) NOT NULL
	private java.lang.String cd_tipo_carico_scarico;
 
//    PROVENIENZA VARCHAR(100)
	private java.lang.String provenienza;
	private Id_inventarioBulk inventario;	
	protected Tipo_carico_scaricoBulk tipoMovimento;
	private java.lang.Long pg_inventario;
	private java.lang.Long nr_inventario;
	private java.lang.String ti_documento;
	private java.lang.Integer esercizio;
	private java.lang.Long pg_buono_c_s;
    private java.lang.Long cd_barre;
	public V_buono_carico_scaricoBulk() {
		super();
	}
	public java.lang.String getDs_buono_carico_scarico () {
		return ds_buono_carico_scarico;
	}
	public void setDs_buono_carico_scarico(java.lang.String ds_buono_carico_scarico)  {
		this.ds_buono_carico_scarico=ds_buono_carico_scarico;
	}
	public java.sql.Timestamp getData_registrazione () {
		return data_registrazione;
	}
	public void setData_registrazione(java.sql.Timestamp data_registrazione)  {
		this.data_registrazione=data_registrazione;
	}

	public java.lang.String getProvenienza () {
		return provenienza;
	}
	public void setProvenienza(java.lang.String provenienza)  {
		this.provenienza=provenienza;
	}
	public void setNr_inventario(java.lang.Long nr_inventario)  {
		this.nr_inventario=nr_inventario;
	}
		
	public void setTi_documento(java.lang.String ti_documento)  {
		this.ti_documento=ti_documento;
	}
	public java.lang.String getTi_documento () {
		return ti_documento;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setPg_buono_c_s(java.lang.Long pg_buono_c_s)  {
		this.pg_buono_c_s=pg_buono_c_s;
	}
	public java.lang.Long getPg_buono_c_s () {
		return pg_buono_c_s;
	}
	
	public java.lang.Long getNr_inventario() {
		return nr_inventario;
	}

	public java.lang.Long getCd_barre() {
		return cd_barre;
	}
	public void setCd_barre(java.lang.Long cd_barre) {
		this.cd_barre = cd_barre;
	}
	public Id_inventarioBulk getInventario() {
		return inventario;
	}
	public void setInventario(Id_inventarioBulk inventario) {
		this.inventario = inventario;
	}
	public Tipo_carico_scaricoBulk getTipoMovimento() {
		return tipoMovimento;
	}
	public void setTipoMovimento(Tipo_carico_scaricoBulk tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}
	public void setCd_tipo_carico_scarico(java.lang.String cd_tipo_carico_scarico) {
		this.getTipoMovimento().setCd_tipo_carico_scarico(cd_tipo_carico_scarico);
	}
	public java.lang.String getCd_tipo_carico_scarico() {
		it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk tipoMovimento = this.getTipoMovimento();
		if (tipoMovimento == null)
			return null;
		return tipoMovimento.getCd_tipo_carico_scarico();
	}
	public void setPg_inventario(java.lang.Long pg_inventario) {
		this.getInventario().setPg_inventario(pg_inventario);
	}
	public java.lang.Long getPg_inventario() {
		it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario = this.getInventario();
		if (inventario == null)
			return null;
		return inventario.getPg_inventario();
	}
}