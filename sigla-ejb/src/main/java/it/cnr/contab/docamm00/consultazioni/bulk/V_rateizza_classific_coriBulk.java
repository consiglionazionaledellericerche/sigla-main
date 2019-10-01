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
* Date 27/10/2005
*/
package it.cnr.contab.docamm00.consultazioni.bulk;

import java.util.Dictionary;

import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;
public class V_rateizza_classific_coriBulk extends OggettoBulk implements Persistent {
//	ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;

//	CD_ANAG DECIMAL(8,0)
	private java.lang.Integer cd_anag;

//	DENOMINAZIONE VARCHAR(101)
	private java.lang.String denominazione;
	
//	DS_CLASSIFICAZIONE_CORI VARCHAR(101)
	private java.lang.String ds_classificazione_cori;	

//  IM_DA_RATEIZZARE(15,2)
	private java.math.BigDecimal im_da_rateizzare;
	
//  IM_RATEIZZATO(15,2)
	private java.math.BigDecimal im_rateizzato;
	
//  IM_RESIDUO DECIMAL(15,2)
	private java.math.BigDecimal im_residuo;	
	
//	CD_CDS_CONGUAGLIO VARCHAR(30) NOT NULL
  	private java.lang.String cd_cds_conguaglio;

//	CD_UO_CONGUAGLIO VARCHAR(30) NOT NULL
  	private java.lang.String cd_uo_conguaglio;

//	PG_CONGUAGLIO DECIMAL(10,0)
	private java.lang.Integer pg_conguaglio;
  	
	public V_rateizza_classific_coriBulk() {
		super();
	}

	public java.lang.Integer getEsercizio () {
			return esercizio;
	}
	public void setEsercizio(java.lang.Integer int1) {
		esercizio = int1;
	}
	public java.lang.String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(java.lang.String string) {
		denominazione = string;
	}

	public java.lang.Integer getCd_anag() {
		return cd_anag;
	}

	public void setCd_anag(java.lang.Integer cd_anag) {
		this.cd_anag = cd_anag;
	}

	public java.lang.String getCd_cds_conguaglio() {
		return cd_cds_conguaglio;
	}

	public void setCd_cds_conguaglio(java.lang.String cd_cds_conguaglio) {
		this.cd_cds_conguaglio = cd_cds_conguaglio;
	}

	public java.lang.String getCd_uo_conguaglio() {
		return cd_uo_conguaglio;
	}

	public void setCd_uo_conguaglio(java.lang.String cd_uo_conguaglio) {
		this.cd_uo_conguaglio = cd_uo_conguaglio;
	}

	public java.lang.String getDs_classificazione_cori() {
		return ds_classificazione_cori;
	}

	public void setDs_classificazione_cori(java.lang.String ds_classificazione_cori) {
		this.ds_classificazione_cori = ds_classificazione_cori;
	}

	public java.math.BigDecimal getIm_da_rateizzare() {
		return im_da_rateizzare;
	}

	public void setIm_da_rateizzare(java.math.BigDecimal im_da_rateizzare) {
		this.im_da_rateizzare = im_da_rateizzare;
	}

	public java.math.BigDecimal getIm_rateizzato() {
		return im_rateizzato;
	}

	public void setIm_rateizzato(java.math.BigDecimal im_rateizzato) {
		this.im_rateizzato = im_rateizzato;
	}

	public java.math.BigDecimal getIm_residuo() {
		return im_residuo;
	}

	public void setIm_residuo(java.math.BigDecimal im_residuo) {
		this.im_residuo = im_residuo;
	}

	public java.lang.Integer getPg_conguaglio() {
		return pg_conguaglio;
	}

	public void setPg_conguaglio(java.lang.Integer pg_conguaglio) {
		this.pg_conguaglio = pg_conguaglio;
	}
}