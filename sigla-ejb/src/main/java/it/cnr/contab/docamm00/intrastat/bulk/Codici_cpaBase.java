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
 * Date 18/02/2010
 */
package it.cnr.contab.docamm00.intrastat.bulk;
import it.cnr.jada.persistency.Keyed;
public class Codici_cpaBase extends Codici_cpaKey implements Keyed {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
	private java.lang.String cd_cpa;
 
//    DS_CPA VARCHAR(500) NOT NULL
	private java.lang.String ds_cpa;
 
//    LIVELLO DECIMAL(22,0) NOT NULL
	private java.lang.Long livello;
 
//    DACR TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp dacr;
 
//    UTCR VARCHAR(20) NOT NULL
	private java.lang.String utcr;
 
//    DUVA TIMESTAMP(7) NOT NULL
	private java.sql.Timestamp duva;
 
//    UTUV VARCHAR(20) NOT NULL
	private java.lang.String utuv;
 
//    PG_VER_REC DECIMAL(22,0) NOT NULL
	private java.lang.Long pg_ver_rec;
 
// FL_UTILIZZABILE CHAR(1) NOT NULL
	private java.lang.Boolean fl_utilizzabile;

// TI_BENE_SERVIZIO CHAR(1) NOT NULL
	private java.lang.String ti_bene_servizio;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: Codici_cpa
	 **/
	public Codici_cpaBase() {
		super();
	}
	public Codici_cpaBase(java.lang.Long id_cpa) {
		super(id_cpa);
	}
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

	public java.lang.String getCd_cpa() {
		return cd_cpa;
	}

	public void setCd_cpa(java.lang.String cd_cpa)  {
		this.cd_cpa=cd_cpa;
	}
	
	public java.lang.String getDs_cpa() {
		return ds_cpa;
	}
	
	public void setDs_cpa(java.lang.String ds_cpa)  {
		this.ds_cpa=ds_cpa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [livello]
	 **/
	public java.lang.Long getLivello() {
		return livello;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [livello]
	 **/
	public void setLivello(java.lang.Long livello)  {
		this.livello=livello;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dacr]
	 **/
	public java.sql.Timestamp getDacr() {
		return dacr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dacr]
	 **/
	public void setDacr(java.sql.Timestamp dacr)  {
		this.dacr=dacr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [utcr]
	 **/
	public java.lang.String getUtcr() {
		return utcr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [utcr]
	 **/
	public void setUtcr(java.lang.String utcr)  {
		this.utcr=utcr;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [duva]
	 **/
	public java.sql.Timestamp getDuva() {
		return duva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [duva]
	 **/
	public void setDuva(java.sql.Timestamp duva)  {
		this.duva=duva;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [utuv]
	 **/
	public java.lang.String getUtuv() {
		return utuv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [utuv]
	 **/
	public void setUtuv(java.lang.String utuv)  {
		this.utuv=utuv;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pg_ver_rec]
	 **/
	public java.lang.Long getPg_ver_rec() {
		return pg_ver_rec;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pg_ver_rec]
	 **/
	public void setPg_ver_rec(java.lang.Long pg_ver_rec)  {
		this.pg_ver_rec=pg_ver_rec;
	}
	public java.lang.Boolean getFl_utilizzabile() {
		return fl_utilizzabile;
	}
	public void setFl_utilizzabile(java.lang.Boolean fl_utilizzabile) {
		this.fl_utilizzabile = fl_utilizzabile;
	}
	public java.lang.String getTi_bene_servizio() {
		return ti_bene_servizio;
	}
	public void setTi_bene_servizio(java.lang.String ti_bene_servizio) {
		this.ti_bene_servizio = ti_bene_servizio;
	}
}