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
* Date 07/08/2006
*/
package it.cnr.contab.pdg00.cdip.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_stipendi_cofi_dettBase extends OggettoBulk implements Persistent {
//    ESERCIZIO DECIMAL(4,0)
	private java.lang.Integer esercizio;
 
//    MESE DECIMAL(2,0)
	private java.lang.Integer mese;
 
//    TIPO_FLUSSO VARCHAR(1)
	private java.lang.String tipo_flusso;
 
//    ENTRATA_SPESA CHAR(1)
	private java.lang.String entrata_spesa;
 
	public V_stipendi_cofi_dettBase() {
		super();
	}
	public java.lang.Integer getEsercizio () {
		return esercizio;
	}
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	public java.lang.Integer getMese () {
		return mese;
	}
	public void setMese(java.lang.Integer mese)  {
		this.mese=mese;
	}
	public java.lang.String getTipo_flusso () {
		return tipo_flusso;
	}
	public void setTipo_flusso(java.lang.String tipo_flusso)  {
		this.tipo_flusso=tipo_flusso;
	}
	public java.lang.String getEntrata_spesa () {
		return entrata_spesa;
	}
	public void setEntrata_spesa(java.lang.String entrata_spesa)  {
		this.entrata_spesa=entrata_spesa;
	}
}