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
public class Tipo_norma_perlaBase extends Tipo_norma_perlaKey implements Keyed {
//    DS_TIPO_NORMA VARCHAR(50) NOT NULL
	private java.lang.String ds_tipo_norma;
 
//    CD_TIPO_NORMA_PERLA VARCHAR(3) NOT NULL
	private java.lang.String cd_tipo_norma_perla;

//    NUMERO_TIPO_NORMA VARCHAR(30) NOT NULL
	private java.lang.String numero_tipo_norma;
	
//    DT_TIPO_NORMA TIMESTAMP(7)
	private java.sql.Timestamp dt_tipo_norma;

//    ARTICOLO_TIPO_NORMA VARCHAR(30) NOT NULL
	private java.lang.String articolo_tipo_norma;

//    COMMA_TIPO_NORMA VARCHAR(30) NOT NULL
	private java.lang.String comma_tipo_norma;
	
//    TIPO_ASSOCIAZIONE VARCHAR(3) NOT NULL
	private java.lang.String tipo_associazione;

	public Tipo_norma_perlaBase() {
		super();
	}
	public Tipo_norma_perlaBase(java.lang.String cd_tipo_norma) {
		super(cd_tipo_norma);
	}
	public java.lang.String getDs_tipo_norma() {
		return ds_tipo_norma;
	}
	public void setDs_tipo_norma(java.lang.String ds_tipo_norma)  {
		this.ds_tipo_norma=ds_tipo_norma;
	}
	public java.lang.String getCd_tipo_norma_perla() {
		return cd_tipo_norma_perla;
	}
	public void setCd_tipo_norma_perla(java.lang.String cd_tipo_norma_perla) {
		this.cd_tipo_norma_perla = cd_tipo_norma_perla;
	}
	public java.lang.String getNumero_tipo_norma() {
		return numero_tipo_norma;
	}
	public void setNumero_tipo_norma(java.lang.String numero_tipo_norma) {
		this.numero_tipo_norma = numero_tipo_norma;
	}
	public java.sql.Timestamp getDt_tipo_norma() {
		return dt_tipo_norma;
	}
	public void setDt_tipo_norma(java.sql.Timestamp dt_tipo_norma) {
		this.dt_tipo_norma = dt_tipo_norma;
	}
	public java.lang.String getArticolo_tipo_norma() {
		return articolo_tipo_norma;
	}
	public void setArticolo_tipo_norma(java.lang.String articolo_tipo_norma) {
		this.articolo_tipo_norma = articolo_tipo_norma;
	}
	public java.lang.String getComma_tipo_norma() {
		return comma_tipo_norma;
	}
	public void setComma_tipo_norma(java.lang.String comma_tipo_norma) {
		this.comma_tipo_norma = comma_tipo_norma;
	}
	public java.lang.String getTipo_associazione() {
		return tipo_associazione;
	}
	public void setTipo_associazione(java.lang.String tipo_associazione) {
		this.tipo_associazione = tipo_associazione;
	}
}