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
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 24/04/2013
 */
package it.cnr.contab.varstanz00.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;

public class V_var_stanz_resBulk extends OggettoBulk implements Persistent {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_VAR_STANZ_RES
	 **/
	public V_var_stanz_resBulk() {
		super();
	}
//  ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;

//  ESERCIZIO_RES DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio_res;

//  PG_VARIAZIONE DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_variazione;

//  DS_VARIAZIONE VARCHAR(1000) NOT NULL
	private java.lang.String ds_variazione;

//  CD_CDR_PROPONENTE VARCHAR(30) NOT NULL
	private java.lang.String cd_cdr_proponente;

//  DS_CDR_PROPONENTE VARCHAR(300) NOT NULL
	private java.lang.String ds_cdr_proponente;

//  CD_CDR_ASSEGNATARIO VARCHAR(30) NOT NULL
	private java.lang.String cd_cdr_assegnatario;

//  DS_CDR_ASSEGNATARIO VARCHAR(300) NOT NULL
	private java.lang.String ds_cdr_assegnatario;

//  IM_VARIAZIONE DECIMAL(22,0)
	private java.lang.Long im_variazione;

//  IM_SPESA DECIMAL(15,2)
	private java.math.BigDecimal im_spesa;

//  STATO VARCHAR(3) NOT NULL
	private java.lang.String stato;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: V_VAR_STANZ_RES
	 **/

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioRes]
	 **/
	public java.lang.Integer getEsercizio_res() {
		return esercizio_res;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioRes]
	 **/
	public void setEsercizio_res(java.lang.Integer esercizio_res)  {
		this.esercizio_res=esercizio_res;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgVariazione]
	 **/
	public java.lang.Long getPg_variazione() {
		return pg_variazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgVariazione]
	 **/
	public void setPg_variazione(java.lang.Long pg_variazione)  {
		this.pg_variazione=pg_variazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsVariazione]
	 **/
	public java.lang.String getDs_variazione() {
		return ds_variazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsVariazione]
	 **/
	public void setDs_variazione(java.lang.String ds_variazione)  {
		this.ds_variazione=ds_variazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdrProponente]
	 **/
	public java.lang.String getCd_cdr_proponente() {
		return cd_cdr_proponente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdrProponente]
	 **/
	public void setCd_cdr_proponente(java.lang.String cd_cdr_proponente)  {
		this.cd_cdr_proponente=cd_cdr_proponente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCdrProponente]
	 **/
	public java.lang.String getDs_cdr_proponente() {
		return ds_cdr_proponente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCdrProponente]
	 **/
	public void setDs_cdr_proponente(java.lang.String ds_cdr_proponente)  {
		this.ds_cdr_proponente=ds_cdr_proponente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCdrAssegnatario]
	 **/
	public java.lang.String getCd_cdr_assegnatario() {
		return cd_cdr_assegnatario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdrAssegnatario]
	 **/
	public void setCd_cdr_assegnatario(java.lang.String cd_cdr_assegnatario)  {
		this.cd_cdr_assegnatario=cd_cdr_assegnatario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsCdrAssegnatario]
	 **/
	public java.lang.String getDs_cdr_assegnatario() {
		return ds_cdr_assegnatario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsCdrAssegnatario]
	 **/
	public void setDs_cdr_assegnatario(java.lang.String ds_cdr_assegnatario)  {
		this.ds_cdr_assegnatario=ds_cdr_assegnatario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imVariazione]
	 **/
	public java.lang.Long getIm_variazione() {
		return im_variazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imVariazione]
	 **/
	public void setIm_variazione(java.lang.Long im_variazione)  {
		this.im_variazione=im_variazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [imSpesa]
	 **/
	public java.math.BigDecimal getIm_spesa() {
		return im_spesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [imSpesa]
	 **/
	public void setIm_spesa(java.math.BigDecimal im_spesa)  {
		this.im_spesa=im_spesa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [stato]
	 **/
	public java.lang.String getStato() {
		return stato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [stato]
	 **/
	public void setStato(java.lang.String stato)  {
		this.stato=stato;
	}
}