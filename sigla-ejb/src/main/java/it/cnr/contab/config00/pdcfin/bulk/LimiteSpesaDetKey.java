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
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class LimiteSpesaDetKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.Integer esercizio;
	private java.lang.String cd_cds;
	private java.lang.Integer esercizio_voce;
	private java.lang.String ti_appartenenza;
	private java.lang.String ti_gestione;
	private java.lang.String cd_elemento_voce;
	private java.lang.String fonte;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LIMITE_SPESA_DET
	 **/
	public LimiteSpesaDetKey() {
		super();
	}
	public LimiteSpesaDetKey(java.lang.Integer esercizio, java.lang.String Cd_cds, java.lang.Integer esercizio_voce, java.lang.String tiAppartenenza, java.lang.String tiGestione, java.lang.String cdElementoVoce, java.lang.String fonte) {
		super();
		this.esercizio=esercizio;
		this.cd_cds=Cd_cds;
		this.esercizio_voce=esercizio_voce;
		this.ti_appartenenza=tiAppartenenza;
		this.ti_gestione=tiGestione;
		this.cd_elemento_voce=cdElementoVoce;
		this.fonte=fonte;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof LimiteSpesaDetKey)) return false;
		LimiteSpesaDetKey k = (LimiteSpesaDetKey) o;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCd_cds(), k.getCd_cds())) return false;
		if (!compareKey(getEsercizio_voce(), k.getEsercizio_voce())) return false;
		if (!compareKey(getTi_appartenenza(), k.getTi_appartenenza())) return false;
		if (!compareKey(getTi_gestione(), k.getTi_gestione())) return false;
		if (!compareKey(getCd_elemento_voce(), k.getCd_elemento_voce())) return false;
		if (!compareKey(getFonte(), k.getFonte())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCd_cds());
		i = i + calculateKeyHashCode(getEsercizio_voce());
		i = i + calculateKeyHashCode(getTi_appartenenza());
		i = i + calculateKeyHashCode(getTi_gestione());
		i = i + calculateKeyHashCode(getCd_elemento_voce());
		i = i + calculateKeyHashCode(getFonte());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public void setEsercizio(java.lang.Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [fonte]
	 **/
	public void setFonte(java.lang.String fonte)  {
		this.fonte=fonte;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [fonte]
	 **/
	public java.lang.String getFonte() {
		return fonte;
	}
	public java.lang.String getCd_cds() {
		return cd_cds;
	}
	public void setCd_cds(java.lang.String cd_cds) {
		this.cd_cds = cd_cds;
	}
	public java.lang.String getTi_appartenenza() {
		return ti_appartenenza;
	}
	public void setTi_appartenenza(java.lang.String ti_appartenenza) {
		this.ti_appartenenza = ti_appartenenza;
	}
	public java.lang.String getTi_gestione() {
		return ti_gestione;
	}
	public void setTi_gestione(java.lang.String ti_gestione) {
		this.ti_gestione = ti_gestione;
	}
	public java.lang.String getCd_elemento_voce() {
		return cd_elemento_voce;
	}
	public void setCd_elemento_voce(java.lang.String cd_elemento_voce) {
		this.cd_elemento_voce = cd_elemento_voce;
	}
	public java.lang.Integer getEsercizio_voce() {
		return esercizio_voce;
	}
	
	public void setEsercizio_voce(java.lang.Integer esercizio_voce) {
		this.esercizio_voce = esercizio_voce;
	}
}