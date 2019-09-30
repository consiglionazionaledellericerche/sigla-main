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
 * Created on Mar 17, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent00.bulk;
import it.cnr.jada.persistency.Keyed;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Pdg_vincoloBase extends Pdg_vincoloKey implements Keyed {
	private static final long serialVersionUID = 1L;

	// IM_VINCOLO DECIMAL(15,2) NOT NULL
	private java.math.BigDecimal im_vincolo;
 
	// FL_ATTIVO CHAR(1) NOT NULL
	private java.lang.Boolean fl_attivo;

	// CD_CDS_ACCERTAMENTO VARCHAR(30)
	private java.lang.String cd_cds_accertamento;

	// ESERCIZIO_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_accertamento;

	// ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Long pg_accertamento;

	public Pdg_vincoloBase() {
		super();
	}
	
	public Pdg_vincoloBase(java.lang.Integer esercizio, java.lang.Integer esercizio_res, java.lang.String cd_centro_responsabilita, java.lang.String cd_linea_attivita, java.lang.String ti_appartenenza, java.lang.String ti_gestione, java.lang.String cd_elemento_voce, java.lang.Long pg_vincolo) {
		super(esercizio, esercizio_res, cd_centro_responsabilita, cd_linea_attivita, ti_appartenenza, ti_gestione, cd_elemento_voce, pg_vincolo);
	}

	public java.math.BigDecimal getIm_vincolo() {
		return im_vincolo;
	}
	
	public void setIm_vincolo(java.math.BigDecimal im_vincolo) {
		this.im_vincolo = im_vincolo;
	}
	
	public java.lang.Boolean getFl_attivo() {
		return fl_attivo;
	}
	
	public void setFl_attivo(java.lang.Boolean fl_attivo) {
		this.fl_attivo = fl_attivo;
	}
	
	public java.lang.String getCd_cds_accertamento() {
		return cd_cds_accertamento;
	}
	
	public void setCd_cds_accertamento(java.lang.String cd_cds_accertamento) {
		this.cd_cds_accertamento = cd_cds_accertamento;
	}

	public java.lang.Integer getEsercizio_accertamento() {
		return esercizio_accertamento;
	}
	
	public void setEsercizio_accertamento(java.lang.Integer esercizio_accertamento) {
		this.esercizio_accertamento = esercizio_accertamento;
	}
	
	public java.lang.Integer getEsercizio_ori_accertamento() {
		return esercizio_ori_accertamento;
	}
	
	public void setEsercizio_ori_accertamento(java.lang.Integer esercizio_ori_accertamento) {
		this.esercizio_ori_accertamento = esercizio_ori_accertamento;
	}
	
	public java.lang.Long getPg_accertamento() {
		return pg_accertamento;
	}
	
	public void setPg_accertamento(java.lang.Long pg_accertamento) {
		this.pg_accertamento = pg_accertamento;
	}
}