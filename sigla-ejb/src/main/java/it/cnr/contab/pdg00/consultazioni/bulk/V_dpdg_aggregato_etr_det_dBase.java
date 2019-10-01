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
* Date 13/04/2005
*/
package it.cnr.contab.pdg00.consultazioni.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Keyed;
import it.cnr.jada.persistency.Persistent;
public class V_dpdg_aggregato_etr_det_dBase extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL
	private java.lang.String cd_centro_responsabilita;
 
//    CD_NATURA VARCHAR(1) NOT NULL
	private java.lang.String cd_natura;
 
//    CD_ELEMENTO_VOCE VARCHAR(20) NOT NULL
	private java.lang.String cd_elemento_voce;
 
//    IM_RA_RCE DECIMAL(15,2)
	private java.math.BigDecimal im_ra_rce;
 
//    IM_RB_RSE DECIMAL(15,2)
	private java.math.BigDecimal im_rb_rse;
 
//    IM_RC_ESR DECIMAL(15,2)
	private java.math.BigDecimal im_rc_esr;
 
	public V_dpdg_aggregato_etr_det_dBase() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.String getCd_centro_responsabilita () {
		return cd_centro_responsabilita;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita)  {
		this.cd_centro_responsabilita=cd_centro_responsabilita;
	}
	public java.lang.String getCd_natura () {
		return cd_natura;
	}
	public void setCd_natura(java.lang.String cd_natura)  {
		this.cd_natura=cd_natura;
	}
	public java.lang.String getCd_elemento_voce () {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce)  {
		this.cd_elemento_voce=cd_elemento_voce;
	}
	public java.math.BigDecimal getIm_ra_rce () {
		return im_ra_rce;
	}
	public void setIm_ra_rce(java.math.BigDecimal im_ra_rce)  {
		this.im_ra_rce=im_ra_rce;
	}
	public java.math.BigDecimal getIm_rb_rse () {
		return im_rb_rse;
	}
	public void setIm_rb_rse(java.math.BigDecimal im_rb_rse)  {
		this.im_rb_rse=im_rb_rse;
	}
	public java.math.BigDecimal getIm_rc_esr () {
		return im_rc_esr;
	}
	public void setIm_rc_esr(java.math.BigDecimal im_rc_esr)  {
		this.im_rc_esr=im_rc_esr;
	}
}