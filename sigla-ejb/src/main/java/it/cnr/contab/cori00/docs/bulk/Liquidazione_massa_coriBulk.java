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

package it.cnr.contab.cori00.docs.bulk;

import it.cnr.contab.logs.bulk.Batch_log_rigaBulk;
import it.cnr.jada.bulk.BulkList;

public class Liquidazione_massa_coriBulk extends it.cnr.jada.bulk.OggettoBulk {
/**
 * Liquidazione_massa_ivaVBulk constructor comment.
 */
public Liquidazione_massa_coriBulk() {
	super();
}
	private java.lang.Integer esercizio;
 
	private java.sql.Timestamp data_da;
 
	private java.sql.Timestamp data_a;
	
	private Boolean da_esercizio_precedente;

	private java.lang.Integer pg_exec; 
	
	private String note;
	
	private BulkList batch_log_riga = new BulkList();
	
	/**
	 * @return
	 */
	public java.sql.Timestamp getData_a() {
		return data_a;
	}

	/**
	 * @return
	 */
	public java.sql.Timestamp getData_da() {
		return data_da;
	}

	/**
	 * @return
	 */
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * @param timestamp
	 */
	public void setData_a(java.sql.Timestamp timestamp) {
		data_a = timestamp;
	}

	/**
	 * @param timestamp
	 */
	public void setData_da(java.sql.Timestamp timestamp) {
		data_da = timestamp;
	}

	/**
	 * @param integer
	 */
	public void setEsercizio(java.lang.Integer integer) {
		esercizio = integer;
	}

	/**
	 * @return
	 */
	public Boolean getDa_esercizio_precedente() {
		return da_esercizio_precedente;
	}

	/**
	 * @param boolean1
	 */
	public void setDa_esercizio_precedente(Boolean boolean1) {
		da_esercizio_precedente = boolean1;
	}

	public it.cnr.jada.bulk.BulkList getBatch_log_riga() {
		return batch_log_riga;
	}
	public void setBatch_log_riga(it.cnr.jada.bulk.BulkList newBatch_log_riga) {
		batch_log_riga = newBatch_log_riga;
	}
	public Batch_log_rigaBulk removeFromBatch_log_riga(int index) {
		Batch_log_rigaBulk dett = (Batch_log_rigaBulk)batch_log_riga.remove(index);
		return dett;
	}
	/**
	 * @return
	 */
	public java.lang.Integer getPg_exec() {
		return pg_exec;
	}

	/**
	 * @param integer
	 */
	public void setPg_exec(java.lang.Integer integer) {
		pg_exec = integer;
	}

	/**
	 * @return
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param string
	 */
	public void setNote(String string) {
		note = string;
	}

}
