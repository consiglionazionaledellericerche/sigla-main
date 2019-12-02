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
package it.cnr.contab.doccont00.core.bulk;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Accertamento_vincolo_perenteKey extends OggettoBulk implements KeyedPersistent {
	private static final long serialVersionUID = 1L;

	// ESERCIZIO DECIMAL(4,0) NOT NULL (PK)
	private java.lang.Integer esercizio;

	// PG_VARIAZIONE DECIMAL(10,0) NOT NULL (PK)
	private java.lang.Long pg_variazione;
	
	// CD_CDS_ACCERTAMENTO VARCHAR(30)
	private java.lang.String cd_cds_accertamento;

	// ESERCIZIO_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_accertamento;

	// ESERCIZIO_ORI_ACCERTAMENTO DECIMAL(4,0)
	private java.lang.Integer esercizio_ori_accertamento;

	// PG_ACCERTAMENTO DECIMAL(10,0)
	private java.lang.Long pg_accertamento;

	public Accertamento_vincolo_perenteKey() {
		super();
	}
	
	public Accertamento_vincolo_perenteKey(java.lang.Integer esercizio,java.lang.Long pg_variazione,java.lang.String cd_cds_accertamento,java.lang.Integer esercizio_accertamento,java.lang.Integer esercizio_ori_accertamento,java.lang.Long pg_accertamento) {
		super();
		this.esercizio = esercizio;
		this.pg_variazione = pg_variazione;
		this.cd_cds_accertamento = cd_cds_accertamento;	
		this.esercizio_accertamento = esercizio_accertamento;
		this.esercizio_ori_accertamento = esercizio_ori_accertamento;
		this.pg_accertamento = pg_accertamento;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Accertamento_vincolo_perenteKey)) return false;
		Accertamento_vincolo_perenteKey k = (Accertamento_vincolo_perenteKey)o;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getPg_variazione(),k.getPg_variazione())) return false;			
		if(!compareKey(getCd_cds_accertamento(),k.getCd_cds_accertamento())) return false;
		if(!compareKey(getEsercizio_accertamento(),k.getEsercizio_accertamento())) return false;
		if(!compareKey(getEsercizio_ori_accertamento(),k.getEsercizio_ori_accertamento())) return false;
		if(!compareKey(getPg_accertamento(),k.getPg_accertamento())) return false;
		return true;
	}

	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getEsercizio())+
			calculateKeyHashCode(getPg_variazione())+
			calculateKeyHashCode(getCd_cds_accertamento())+
			calculateKeyHashCode(getEsercizio_accertamento())+
			calculateKeyHashCode(getEsercizio_ori_accertamento())+
			calculateKeyHashCode(getPg_accertamento());
	}

	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	
	public void setEsercizio(java.lang.Integer esercizio) {
		this.esercizio = esercizio;
	}

	public java.lang.Long getPg_variazione() {
		return pg_variazione;
	}

	public void setPg_variazione(java.lang.Long pg_variazione) {
		this.pg_variazione = pg_variazione;
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
