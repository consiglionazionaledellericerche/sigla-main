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
 * Date 20/06/2013
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class MandatoSiopeCupKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdCds;
	private java.lang.Integer esercizio;
	private java.lang.Long pgMandato;
	private java.lang.Integer esercizioObbligazione;
	private java.lang.Integer esercizioOriObbligazione;
	private java.lang.Long pgObbligazione;
	private java.lang.Long pgObbligazioneScadenzario;
	private java.lang.String cdCdsDocAmm;
	private java.lang.String cdUoDocAmm;
	private java.lang.Integer esercizioDocAmm;
	private java.lang.String cdTipoDocumentoAmm;
	private java.lang.Long pgDocAmm;
	private java.lang.Integer esercizioSiope;
	private java.lang.String tiGestione;
	private java.lang.String cdSiope;
	private java.lang.String cdCup;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MANDATO_SIOPE_CUP
	 **/
	public MandatoSiopeCupKey() {
		super();
	}
	public MandatoSiopeCupKey(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgMandato, java.lang.Integer esercizioObbligazione, java.lang.Integer esercizioOriObbligazione, java.lang.Long pgObbligazione, java.lang.Long pgObbligazioneScadenzario, java.lang.String cdCdsDocAmm, java.lang.String cdUoDocAmm, java.lang.Integer esercizioDocAmm, java.lang.String cdTipoDocumentoAmm, java.lang.Long pgDocAmm, java.lang.Integer esercizioSiope, java.lang.String tiGestione, java.lang.String cdSiope, java.lang.String cdCup) {
		super();
		this.cdCds=cdCds;
		this.esercizio=esercizio;
		this.pgMandato=pgMandato;
		this.esercizioObbligazione=esercizioObbligazione;
		this.esercizioOriObbligazione=esercizioOriObbligazione;
		this.pgObbligazione=pgObbligazione;
		this.pgObbligazioneScadenzario=pgObbligazioneScadenzario;
		this.cdCdsDocAmm=cdCdsDocAmm;
		this.cdUoDocAmm=cdUoDocAmm;
		this.esercizioDocAmm=esercizioDocAmm;
		this.cdTipoDocumentoAmm=cdTipoDocumentoAmm;
		this.pgDocAmm=pgDocAmm;
		this.esercizioSiope=esercizioSiope;
		this.tiGestione=tiGestione;
		this.cdSiope=cdSiope;
		this.cdCup=cdCup;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof MandatoSiopeCupKey)) return false;
		MandatoSiopeCupKey k = (MandatoSiopeCupKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getPgMandato(), k.getPgMandato())) return false;
		if (!compareKey(getEsercizioObbligazione(), k.getEsercizioObbligazione())) return false;
		if (!compareKey(getEsercizioOriObbligazione(), k.getEsercizioOriObbligazione())) return false;
		if (!compareKey(getPgObbligazione(), k.getPgObbligazione())) return false;
		if (!compareKey(getPgObbligazioneScadenzario(), k.getPgObbligazioneScadenzario())) return false;
		if (!compareKey(getCdCdsDocAmm(), k.getCdCdsDocAmm())) return false;
		if (!compareKey(getCdUoDocAmm(), k.getCdUoDocAmm())) return false;
		if (!compareKey(getEsercizioDocAmm(), k.getEsercizioDocAmm())) return false;
		if (!compareKey(getCdTipoDocumentoAmm(), k.getCdTipoDocumentoAmm())) return false;
		if (!compareKey(getPgDocAmm(), k.getPgDocAmm())) return false;
		if (!compareKey(getEsercizioSiope(), k.getEsercizioSiope())) return false;
		if (!compareKey(getTiGestione(), k.getTiGestione())) return false;
		if (!compareKey(getCdSiope(), k.getCdSiope())) return false;
		if (!compareKey(getCdCup(), k.getCdCup())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getPgMandato());
		i = i + calculateKeyHashCode(getEsercizioObbligazione());
		i = i + calculateKeyHashCode(getEsercizioOriObbligazione());
		i = i + calculateKeyHashCode(getPgObbligazione());
		i = i + calculateKeyHashCode(getPgObbligazioneScadenzario());
		i = i + calculateKeyHashCode(getCdCdsDocAmm());
		i = i + calculateKeyHashCode(getCdUoDocAmm());
		i = i + calculateKeyHashCode(getEsercizioDocAmm());
		i = i + calculateKeyHashCode(getCdTipoDocumentoAmm());
		i = i + calculateKeyHashCode(getPgDocAmm());
		i = i + calculateKeyHashCode(getEsercizioSiope());
		i = i + calculateKeyHashCode(getTiGestione());
		i = i + calculateKeyHashCode(getCdSiope());
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
	 * Restituisce il valore di: [pgMandato]
	 **/
	public void setPgMandato(java.lang.Long pgMandato)  {
		this.pgMandato=pgMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgMandato]
	 **/
	public java.lang.Long getPgMandato() {
		return pgMandato;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioObbligazione]
	 **/
	public void setEsercizioObbligazione(java.lang.Integer esercizioObbligazione)  {
		this.esercizioObbligazione=esercizioObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioObbligazione]
	 **/
	public java.lang.Integer getEsercizioObbligazione() {
		return esercizioObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizioOriObbligazione]
	 **/
	public void setEsercizioOriObbligazione(java.lang.Integer esercizioOriObbligazione)  {
		this.esercizioOriObbligazione=esercizioOriObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioOriObbligazione]
	 **/
	public java.lang.Integer getEsercizioOriObbligazione() {
		return esercizioOriObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazione]
	 **/
	public void setPgObbligazione(java.lang.Long pgObbligazione)  {
		this.pgObbligazione=pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazione]
	 **/
	public java.lang.Long getPgObbligazione() {
		return pgObbligazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgObbligazioneScadenzario]
	 **/
	public void setPgObbligazioneScadenzario(java.lang.Long pgObbligazioneScadenzario)  {
		this.pgObbligazioneScadenzario=pgObbligazioneScadenzario;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgObbligazioneScadenzario]
	 **/
	public java.lang.Long getPgObbligazioneScadenzario() {
		return pgObbligazioneScadenzario;
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
	 * Restituisce il valore di: [esercizioSiope]
	 **/
	public void setEsercizioSiope(java.lang.Integer esercizioSiope)  {
		this.esercizioSiope=esercizioSiope;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizioSiope]
	 **/
	public java.lang.Integer getEsercizioSiope() {
		return esercizioSiope;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiGestione]
	 **/
	public void setTiGestione(java.lang.String tiGestione)  {
		this.tiGestione=tiGestione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiGestione]
	 **/
	public java.lang.String getTiGestione() {
		return tiGestione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdSiope]
	 **/
	public void setCdSiope(java.lang.String cdSiope)  {
		this.cdSiope=cdSiope;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdSiope]
	 **/
	public java.lang.String getCdSiope() {
		return cdSiope;
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
}