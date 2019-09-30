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
 * Date 22/02/2008
 */
package it.cnr.contab.inventario00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class V_quote_utilizzatoriBulk  extends OggettoBulk implements Persistent {
//  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    QUOTE DECIMAL(22,0)
	private java.math.BigDecimal quote;
 
//    CATEGORIA VARCHAR(10) NOT NULL
	private java.lang.String categoria;
 
//    DS_CATEGORIA VARCHAR(100) NOT NULL
	private java.lang.String ds_categoria;
 
//    CDR VARCHAR(30) NOT NULL
	private java.lang.String cdr;
 
//    DS_CDR VARCHAR(300) NOT NULL
	private java.lang.String ds_cdr;
 
//    PERC_CDR DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal perc_cdr;
 
//    GAE VARCHAR(10) NOT NULL
	private java.lang.String gae;
 
//    DS_GAE VARCHAR(300)
	private java.lang.String ds_gae;
 
//    PERC_GAE DECIMAL(5,2) NOT NULL
	private java.math.BigDecimal perc_gae;
 
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	
	public java.lang.String getCategoria() {
		return categoria;
	}
	public void setCategoria(java.lang.String categoria)  {
		this.categoria=categoria;
	}
	public java.lang.String getDs_categoria() {
		return ds_categoria;
	}
	public void setDs_categoria(java.lang.String ds_categoria)  {
		this.ds_categoria=ds_categoria;
	}
	public java.lang.String getCdr() {
		return cdr;
	}
	public void setCdr(java.lang.String cdr)  {
		this.cdr=cdr;
	}
	public java.lang.String getDs_cdr() {
		return ds_cdr;
	}
	public void setDs_cdr(java.lang.String ds_cdr)  {
		this.ds_cdr=ds_cdr;
	}
	public java.math.BigDecimal getPerc_cdr() {
		return perc_cdr;
	}
	public void setPerc_cdr(java.math.BigDecimal perc_cdr)  {
		this.perc_cdr=perc_cdr;
	}
	public java.lang.String getGae() {
		return gae;
	}
	public void setGae(java.lang.String gae)  {
		this.gae=gae;
	}
	public java.lang.String getDs_gae() {
		return ds_gae;
	}
	public void setDs_gae(java.lang.String ds_gae)  {
		this.ds_gae=ds_gae;
	}
	public java.math.BigDecimal getPerc_gae() {
		return perc_gae;
	}
	public void setPerc_gae(java.math.BigDecimal perc_gae)  {
		this.perc_gae=perc_gae;
	}
	public java.math.BigDecimal getQuote() {
		return quote;
	}
	public void setQuote(java.math.BigDecimal quote) {
		this.quote = quote;
	}
	public V_quote_utilizzatoriBulk() {
		super();
	}
	
}