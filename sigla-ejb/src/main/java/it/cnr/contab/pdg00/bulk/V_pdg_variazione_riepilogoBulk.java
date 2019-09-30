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
 * Date 13/02/2008
 */
package it.cnr.contab.pdg00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_pdg_variazione_riepilogoBulk extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0) NOT NULL
	private java.lang.Integer esercizio;
 
//    PG_VARIAZIONE_PDG DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_variazione_pdg;
 
//    TI_GESTIONE CHAR(1) NOT NULL
	private java.lang.String ti_gestione;
 
//    CD_CDR_ASSEGNATARIO VARCHAR(30) NOT NULL
	private java.lang.String cd_cdr_assegnatario;
 
//    DS_CDR VARCHAR(300) NOT NULL
	private java.lang.String ds_cdr;
 
//    CD_DIPARTIMENTO VARCHAR(30)
	private java.lang.String cd_dipartimento;
 
//    DS_DIPARTIMENTO VARCHAR(300)
	private java.lang.String ds_dipartimento;
 
//    IM_TOTALE DECIMAL(22,0)
	private java.math.BigDecimal im_totale;
 
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Long getPg_variazione_pdg() {
		return pg_variazione_pdg;
	}
	public void setPg_variazione_pdg(java.lang.Long pg_variazione_pdg)  {
		this.pg_variazione_pdg=pg_variazione_pdg;
	}
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione)  {
		this.ti_gestione=ti_gestione;
	}
	public java.lang.String getCd_cdr_assegnatario() {
		return cd_cdr_assegnatario;
	}
	public void setCd_cdr_assegnatario(java.lang.String cd_cdr_assegnatario)  {
		this.cd_cdr_assegnatario=cd_cdr_assegnatario;
	}
	public java.lang.String getDs_cdr() {
		return ds_cdr;
	}
	public void setDs_cdr(java.lang.String ds_cdr)  {
		this.ds_cdr=ds_cdr;
	}
	public java.lang.String getCd_dipartimento() {
		return cd_dipartimento;
	}
	public void setCd_dipartimento(java.lang.String cd_dipartimento)  {
		this.cd_dipartimento=cd_dipartimento;
	}
	public java.lang.String getDs_dipartimento() {
		return ds_dipartimento;
	}
	public void setDs_dipartimento(java.lang.String ds_dipartimento)  {
		this.ds_dipartimento=ds_dipartimento;
	}
	public java.math.BigDecimal getIm_totale() {
		return im_totale;
	}
	public void setIm_totale(java.math.BigDecimal im_totale)  {
		this.im_totale=im_totale;
	}
}