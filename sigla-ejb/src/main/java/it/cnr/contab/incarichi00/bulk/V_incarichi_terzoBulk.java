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
 * Date 11/01/2008
 */
package it.cnr.contab.incarichi00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_incarichi_terzoBulk extends OggettoBulk implements Persistent {
//    ESERCIZIO_LIMITE DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_limite;
 
//    CD_CDS VARCHAR(30) NOT NULL
	private java.lang.String cd_cds;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
 
//    CD_TERZO DECIMAL(8,0)
	private java.lang.Integer cd_terzo;
 
//    DS_TERZO VARCHAR(200) NOT NULL
	private java.lang.String ds_terzo;
 
//    CD_TIPO_LIMITE VARCHAR(5) NOT NULL
	private java.lang.String cd_tipo_limite;
 
//    IM_INCARICHI DECIMAL(22,0)
	private java.math.BigDecimal im_incarichi;
 
	public java.lang.Integer getEsercizio_limite() {
		return esercizio_limite;
	}
	public void setEsercizio_limite(java.lang.Integer esercizio_limite)  {
		this.esercizio_limite=esercizio_limite;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds)  {
		this.cd_cds=cd_cds;
	}
	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}
	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa)  {
		this.cd_unita_organizzativa=cd_unita_organizzativa;
	}
	public java.lang.Integer getCd_terzo() {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.String getDs_terzo() {
		return ds_terzo;
	}
	public void setDs_terzo(java.lang.String ds_terzo)  {
		this.ds_terzo=ds_terzo;
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
}