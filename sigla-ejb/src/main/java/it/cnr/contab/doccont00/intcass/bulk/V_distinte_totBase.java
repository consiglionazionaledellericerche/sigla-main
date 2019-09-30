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
* Date 27/04/2006
*/
package it.cnr.contab.doccont00.intcass.bulk;
import it.cnr.jada.bulk.OggettoBulk;

import it.cnr.jada.persistency.Persistent;
public class V_distinte_totBase extends OggettoBulk implements Persistent {

	private java.lang.String cds;
 
	private java.lang.Long esercizio;
 
	private java.lang.String uo;
 
	private java.lang.String tipo;
 
	private java.math.BigDecimal tot_pagato;

	private java.math.BigDecimal tot_incassato;
	
	private java.math.BigDecimal tot_mandati;

	private java.math.BigDecimal tot_reversali;
	
	private java.math.BigDecimal diff_pagato;

	private java.math.BigDecimal diff_incassato;
 
	private java.sql.Timestamp dt_emissione;
//    PG_DISTINTA DECIMAL(22,0)
	private java.lang.Long pg_distinta;

//  PG_DISTINTA_DEF DECIMAL(22,0)
	private java.lang.Long pg_distinta_def;
 
//    DT_INVIO_DIS TIMESTAMP(7)
	private java.sql.Timestamp dt_invio;
 
	public V_distinte_totBase() {
		super();
	}

	public java.lang.String getCds() {
		return cds;
	}

	public void setCds(java.lang.String cds) {
		this.cds = cds;
	}

	public java.lang.Long getEsercizio() {
		return esercizio;
	}

	public void setEsercizio(java.lang.Long esercizio) {
		this.esercizio = esercizio;
	}

	public java.lang.String getUo() {
		return uo;
	}

	public void setUo(java.lang.String uo) {
		this.uo = uo;
	}

	public java.lang.String getTipo() {
		return tipo;
	}

	public void setTipo(java.lang.String tipo) {
		this.tipo = tipo;
	}

	public java.math.BigDecimal getTot_pagato() {
		return tot_pagato;
	}

	public void setTot_pagato(java.math.BigDecimal tot_pagato) {
		this.tot_pagato = tot_pagato;
	}

	public java.math.BigDecimal getTot_incassato() {
		return tot_incassato;
	}

	public void setTot_incassato(java.math.BigDecimal tot_incassato) {
		this.tot_incassato = tot_incassato;
	}

	public java.math.BigDecimal getTot_mandati() {
		return tot_mandati;
	}

	public void setTot_mandati(java.math.BigDecimal tot_mandati) {
		this.tot_mandati = tot_mandati;
	}

	public java.math.BigDecimal getTot_reversali() {
		return tot_reversali;
	}

	public void setTot_reversali(java.math.BigDecimal tot_reversali) {
		this.tot_reversali = tot_reversali;
	}

	public java.math.BigDecimal getDiff_pagato() {
		return diff_pagato;
	}

	public void setDiff_pagato(java.math.BigDecimal diff_pagato) {
		this.diff_pagato = diff_pagato;
	}

	public java.math.BigDecimal getDiff_incassato() {
		return diff_incassato;
	}

	public void setDiff_incassato(java.math.BigDecimal diff_incassato) {
		this.diff_incassato = diff_incassato;
	}

	public java.sql.Timestamp getDt_emissione() {
		return dt_emissione;
	}

	public void setDt_emissione(java.sql.Timestamp dt_emissione) {
		this.dt_emissione = dt_emissione;
	}

	public java.lang.Long getPg_distinta() {
		return pg_distinta;
	}

	public void setPg_distinta(java.lang.Long pg_distinta) {
		this.pg_distinta = pg_distinta;
	}

	public java.lang.Long getPg_distinta_def() {
		return pg_distinta_def;
	}

	public void setPg_distinta_def(java.lang.Long pg_distinta_def) {
		this.pg_distinta_def = pg_distinta_def;
	}

	public java.sql.Timestamp getDt_invio() {
		return dt_invio;
	}

	public void setDt_invio(java.sql.Timestamp dt_invio) {
		this.dt_invio = dt_invio;
	}
	
}