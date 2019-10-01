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

package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
public class TrovatoBulk extends it.cnr.jada.bulk.OggettoBulk {

	private CdsBulk cds;

	private Unita_organizzativaBulk uo;

	private java.lang.Long pg_trovato;

	private java.lang.String titolo;
	
	private java.lang.String inventore;
	
//	private java.lang.Long nsrif;
	
	public TrovatoBulk() {
		super();
	}
	public void setCds(CdsBulk cds) {
		this.cds = cds;
	}
	public CdsBulk getCds() {
		return cds;
	}
	public void setUo(Unita_organizzativaBulk uo) {
		this.uo = uo;
	}
	public Unita_organizzativaBulk getUo() {
		return uo;
	}
	public void setPg_trovato(java.lang.Long pg_trovato) {
		this.pg_trovato = pg_trovato;
	}
	public java.lang.Long getPg_trovato() {
		return pg_trovato;
	}
	public void setTitolo(java.lang.String titolo) {
		this.titolo = titolo;
	}
	public java.lang.String getTitolo() {
		return titolo;
	}
//	public java.lang.Long getNsrif() {
//		return nsrif;
//	}
//	public void setNsrif(java.lang.Long nsrif) {
//		this.nsrif = nsrif;
//	}
	public java.lang.String getInventore() {
		return inventore;
	}
	public void setInventore(java.lang.String inventore) {
		this.inventore = inventore;
	}
}
