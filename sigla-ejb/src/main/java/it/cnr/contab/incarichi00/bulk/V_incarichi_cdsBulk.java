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
 * Date 08/01/2008
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.BulkCollection;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_incarichi_cdsBulk extends OggettoBulk implements Persistent {

	private BulkList incarichi_x_uoColl = new BulkList();

//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//  DS_CDS VARCHAR(300) NOT NULL
	private java.lang.String ds_cds;

//    ESERCIZIO_LIMITE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_limite;
 
//    CD_TIPO_LIMITE VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_limite;
 
//    IM_INCARICHI DECIMAL(22,0)
	private java.math.BigDecimal im_incarichi;
 
	private java.math.BigDecimal prc_utilizzato;

	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getDs_cds() {
		return ds_cds;
	}
	public void setDs_cds(java.lang.String ds_cds)  {
		this.ds_cds=ds_cds;
	}
	public java.lang.Integer getEsercizio_limite() {
		return esercizio_limite;
	}
	public void setEsercizio_limite(java.lang.Integer esercizio_limite)  {
		this.esercizio_limite=esercizio_limite;
	}
	public java.lang.String getCd_tipo_limite() {
		return cd_tipo_limite;
	}
	public void setCd_tipo_limite(java.lang.String cd_tipo_limite)  {
		this.cd_tipo_limite=cd_tipo_limite;
	}
	public java.math.BigDecimal getIm_incarichi() {
		return im_incarichi;
	}
	public void setIm_incarichi(java.math.BigDecimal im_incarichi)  {
		this.im_incarichi=im_incarichi;
	}
	public BulkList getIncarichi_x_uoColl() {
		return incarichi_x_uoColl;
	}
	public void setIncarichi_x_uoColl(BulkList incarichi_x_uoColl) {
		this.incarichi_x_uoColl = incarichi_x_uoColl;
	}
	public BulkCollection[] getBulkLists() {
		 return new it.cnr.jada.bulk.BulkCollection[] { 
				 incarichi_x_uoColl };
	}
	public java.math.BigDecimal getPrc_utilizzato() {
		return prc_utilizzato;
	}
	public void setPrc_utilizzato(java.math.BigDecimal prc_utilizzato) {
		this.prc_utilizzato = prc_utilizzato;
	}
}