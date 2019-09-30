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
* Date 22/11/2005
*/
package it.cnr.contab.pdg01.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;

public class V_cons_pdgp_pdgg_etrBulk extends OggettoBulk implements Persistent {
	public V_cons_pdgp_pdgg_etrBulk() {
		super();
	}
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    CD_CENTRO_RESPONSABILITA VARCHAR(30) NOT NULL
	private java.lang.String cd_centro_responsabilita;
 
//    DS_CDR VARCHAR(300) NOT NULL
	private java.lang.String ds_cdr;
 
//    PG_PROGETTO DECIMAL(10,0) NOT NULL
	private java.lang.Integer pg_progetto;
 
//	  CD_PROGETTO VARCHAR(30) NOT NULL
    private java.lang.String cd_progetto;

//    DS_PROGETTO VARCHAR(433)
	private java.lang.String ds_progetto;
 
//    CD_NATURA VARCHAR(1) NOT NULL
	private java.lang.String cd_natura;
 
//    ID_CLASSIFICAZIONE DECIMAL(7,0) NOT NULL
	private java.lang.Integer id_classificazione;
 
//    CD_CLASSIFICAZIONE VARCHAR(34)
	private java.lang.String cd_classificazione;
 
//    DS_CLASSIFICAZIONE VARCHAR(250) NOT NULL
	private java.lang.String ds_classificazione;
 
//    CD_CDS_AREA VARCHAR(30) NOT NULL
	private java.lang.String cd_cds_area;
 
//    PG_DETTAGLIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_dettaglio;
 
//	  CD_TERZO DECIMAL(8,0) NOT NULL
	private java.lang.Integer cd_terzo;
 
//	  DS_DETTAGLIO VARCHAR(300) NOT NULL
    private java.lang.String ds_dettaglio;
 
//	  IM_ENTRATA_TOT DECIMAL(15,2) NOT NULL
    private java.math.BigDecimal im_entrata_tot_pdgp;

//    IM_ENTRATA_PDGP DECIMAL(15,2)
	private java.math.BigDecimal im_entrata_pdgp;
 
//    IM_ENTRATA_PDGG DECIMAL(15,2)
	private java.math.BigDecimal im_entrata_pdgg;
 
//    IM_INCASSI_PDGG DECIMAL(15,2)
	private java.math.BigDecimal im_incassi_pdgg;
 
//	  IM_ENTRATA_DA_RIP DECIMAL(15,2)
	private java.math.BigDecimal im_entrata_da_rip;

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
	public java.lang.String getDs_cdr () {
		return ds_cdr;
	}
	public void setDs_cdr(java.lang.String ds_cdr)  {
		this.ds_cdr=ds_cdr;
	}
	public java.lang.Integer getPg_progetto () {
		return pg_progetto;
	}
	public void setPg_progetto(java.lang.Integer pg_progetto)  {
		this.pg_progetto=pg_progetto;
	}
	public java.lang.String getCd_progetto () {
		return cd_progetto;
	}
	public void setCd_progetto(java.lang.String cd_progetto)  {
		this.cd_progetto=cd_progetto;
	}
	public java.lang.String getDs_progetto () {
		return ds_progetto;
	}
	public void setDs_progetto(java.lang.String ds_progetto)  {
		this.ds_progetto=ds_progetto;
	}
	public java.lang.String getCd_natura () {
		return cd_natura;
	}
	public void setCd_natura(java.lang.String cd_natura)  {
		this.cd_natura=cd_natura;
	}
	public java.lang.Integer getId_classificazione () {
		return id_classificazione;
	}
	public void setId_classificazione(java.lang.Integer id_classificazione)  {
		this.id_classificazione=id_classificazione;
	}
	public java.lang.String getCd_classificazione () {
		return cd_classificazione;
	}
	public void setCd_classificazione(java.lang.String cd_classificazione)  {
		this.cd_classificazione=cd_classificazione;
	}
	public java.lang.String getDs_classificazione () {
		return ds_classificazione;
	}
	public void setDs_classificazione(java.lang.String ds_classificazione)  {
		this.ds_classificazione=ds_classificazione;
	}
	public java.lang.String getCd_cds_area () {
		return cd_cds_area;
	}
	public void setCd_cds_area(java.lang.String cd_cds_area)  {
		this.cd_cds_area=cd_cds_area;
	}
	public java.lang.Long getPg_dettaglio () {
		return pg_dettaglio;
	}
	public void setPg_dettaglio(java.lang.Long pg_dettaglio)  {
		this.pg_dettaglio=pg_dettaglio;
	}
	public java.lang.Integer getCd_terzo () {
		return cd_terzo;
	}
	public void setCd_terzo(java.lang.Integer cd_terzo)  {
		this.cd_terzo=cd_terzo;
	}
	public java.lang.String getDs_dettaglio () {
		return ds_dettaglio;
	}
	public void setDs_dettaglio(java.lang.String ds_dettaglio)  {
		this.ds_dettaglio=ds_dettaglio;
	}
	public java.math.BigDecimal getIm_entrata_tot_pdgp () {
		return im_entrata_tot_pdgp;
	}
	public void setIm_entrata_tot_pdgp(java.math.BigDecimal im_entrata_tot_pdgp)  {
		this.im_entrata_tot_pdgp=im_entrata_tot_pdgp;
	}
	public java.math.BigDecimal getIm_entrata_pdgp () {
		return im_entrata_pdgp;
	}
	public void setIm_entrata_pdgp(java.math.BigDecimal im_entrata_pdgp)  {
		this.im_entrata_pdgp=im_entrata_pdgp;
	}
	public java.math.BigDecimal getIm_entrata_pdgg () {
		return im_entrata_pdgg;
	}
	public void setIm_entrata_pdgg(java.math.BigDecimal im_entrata_pdgg)  {
		this.im_entrata_pdgg=im_entrata_pdgg;
	}
	public java.math.BigDecimal getIm_incassi_pdgg () {
		return im_incassi_pdgg;
	}
	public void setIm_incassi_pdgg(java.math.BigDecimal im_incassi_pdgg)  {
		this.im_incassi_pdgg=im_incassi_pdgg;
	}
	public java.math.BigDecimal getIm_entrata_da_rip() {
		return im_entrata_da_rip;
	}
	public void setIm_entrata_da_rip(java.math.BigDecimal decimal) {
		im_entrata_da_rip = decimal;
	}
}