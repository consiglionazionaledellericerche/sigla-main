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
 * Date 09/09/2010
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class ReversaleCupKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.Integer esercizio;
	private java.lang.Long pgReversale;
	private java.lang.Integer esercizioAccertamento;
	private java.lang.Integer esercizioOriAccertamento;
	private java.lang.Long pgAccertamento;
	private java.lang.Long pgAccertamentoScadenzario;
	private java.lang.String cdCdsDocAmm;
	private java.lang.String cdUoDocAmm;
	private java.lang.Integer esercizioDocAmm;
	private java.lang.String cdTipoDocumentoAmm;
	private java.lang.Long pgDocAmm;
	private java.lang.String cdCup;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: REVERSALE_CUP
	 **/
	public ReversaleCupKey() {
		super();
	}
	public ReversaleCupKey(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgReversale, java.lang.Integer esercizioAccertamento, java.lang.Integer esercizioOriAccertamento, java.lang.Long pgAccertamento, java.lang.Long pgAccertamentoScadenzario, java.lang.String cdCdsDocAmm, java.lang.String cdUoDocAmm, java.lang.Integer esercizioDocAmm, java.lang.String cdTipoDocumentoAmm, java.lang.Long pgDocAmm, java.lang.String cdCup) {
		super();
		this.cdCds=cdCds;
		this.esercizio=esercizio;
		this.pgReversale=pgReversale;
		this.esercizioAccertamento=esercizioAccertamento;
		this.esercizioOriAccertamento=esercizioOriAccertamento;
		this.pgAccertamento=pgAccertamento;
		this.pgAccertamentoScadenzario=pgAccertamentoScadenzario;
		this.cdCdsDocAmm=cdCdsDocAmm;
		this.cdUoDocAmm=cdUoDocAmm;
		this.esercizioDocAmm=esercizioDocAmm;
		this.cdTipoDocumentoAmm=cdTipoDocumentoAmm;
		this.pgDocAmm=pgDocAmm;
		this.cdCup=cdCup;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof ReversaleCupKey)) return false;
		ReversaleCupKey k = (ReversaleCupKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPgReversale(), k.getPgReversale())) return false;
		if (!compareKey(getEsercizioAccertamento(), k.getEsercizioAccertamento())) return false;
		if (!compareKey(getEsercizioOriAccertamento(), k.getEsercizioOriAccertamento())) return false;
		if (!compareKey(getPgAccertamento(), k.getPgAccertamento())) return false;
		if (!compareKey(getPgAccertamentoScadenzario(), k.getPgAccertamentoScadenzario())) return false;
		if (!compareKey(getCdCdsDocAmm(), k.getCdCdsDocAmm())) return false;
		if (!compareKey(getCdUoDocAmm(), k.getCdUoDocAmm())) return false;
		if (!compareKey(getEsercizioDocAmm(), k.getEsercizioDocAmm())) return false;
		if (!compareKey(getCdTipoDocumentoAmm(), k.getCdTipoDocumentoAmm())) return false;
		if (!compareKey(getPgDocAmm(), k.getPgDocAmm())) return false;
		if (!compareKey(getCdCup(), k.getCdCup())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPgReversale());
		i = i + calculateKeyHashCode(getEsercizioAccertamento());
		i = i + calculateKeyHashCode(getEsercizioOriAccertamento());
		i = i + calculateKeyHashCode(getPgAccertamento());
		i = i + calculateKeyHashCode(getPgAccertamentoScadenzario());
		i = i + calculateKeyHashCode(getCdCdsDocAmm());
		i = i + calculateKeyHashCode(getCdUoDocAmm());
		i = i + calculateKeyHashCode(getEsercizioDocAmm());
		i = i + calculateKeyHashCode(getCdTipoDocumentoAmm());
		i = i + calculateKeyHashCode(getPgDocAmm());
		i = i + calculateKeyHashCode(getCdCup());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public void setCdCds(java.lang.String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public java.lang.String getCdCds() {
		return cdCds;
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
	 * Restituisce il valore di: [cdCdsDocAmm]
	 **/
	public void setCdCdsDocAmm(java.lang.String cdCdsDocAmm)  {
		this.cdCdsDocAmm=cdCdsDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCdsDocAmm]
	 **/
	public java.lang.String getCdCdsDocAmm() {
		return cdCdsDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUoDocAmm]
	 **/
	public void setCdUoDocAmm(java.lang.String cdUoDocAmm)  {
		this.cdUoDocAmm=cdUoDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUoDocAmm]
	 **/
	public java.lang.String getCdUoDocAmm() {
		return cdUoDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioDocAmm]
	 **/
	public void setEsercizioDocAmm(java.lang.Integer esercizioDocAmm)  {
		this.esercizioDocAmm=esercizioDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioDocAmm]
	 **/
	public java.lang.Integer getEsercizioDocAmm() {
		return esercizioDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoDocumentoAmm]
	 **/
	public void setCdTipoDocumentoAmm(java.lang.String cdTipoDocumentoAmm)  {
		this.cdTipoDocumentoAmm=cdTipoDocumentoAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoDocumentoAmm]
	 **/
	public java.lang.String getCdTipoDocumentoAmm() {
		return cdTipoDocumentoAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgDocAmm]
	 **/
	public void setPgDocAmm(java.lang.Long pgDocAmm)  {
		this.pgDocAmm=pgDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgDocAmm]
	 **/
	public java.lang.Long getPgDocAmm() {
		return pgDocAmm;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCup]
	 **/
	public void setCdCup(java.lang.String cdCup)  {
		this.cdCup=cdCup;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCup]
	 **/
	public java.lang.String getCdCup() {
		return cdCup;
	}
	public java.lang.Long getPgReversale() {
		return pgReversale;
	}
	public void setPgReversale(java.lang.Long pgReversale) {
		this.pgReversale = pgReversale;
	}
	public java.lang.Integer getEsercizioAccertamento() {
		return esercizioAccertamento;
	}
	public void setEsercizioAccertamento(java.lang.Integer esercizioAccertamento) {
		this.esercizioAccertamento = esercizioAccertamento;
	}
	public java.lang.Integer getEsercizioOriAccertamento() {
		return esercizioOriAccertamento;
	}
	public void setEsercizioOriAccertamento(
			java.lang.Integer esercizioOriAccertamento) {
		this.esercizioOriAccertamento = esercizioOriAccertamento;
	}
	public java.lang.Long getPgAccertamento() {
		return pgAccertamento;
	}
	public void setPgAccertamento(java.lang.Long pgAccertamento) {
		this.pgAccertamento = pgAccertamento;
	}
	public java.lang.Long getPgAccertamentoScadenzario() {
		return pgAccertamentoScadenzario;
	}
	public void setPgAccertamentoScadenzario(
			java.lang.Long pgAccertamentoScadenzario) {
		this.pgAccertamentoScadenzario = pgAccertamentoScadenzario;
	}
}