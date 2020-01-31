/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 03/10/2017
 */
package it.cnr.contab.ordmag.magazzino.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class LottoMagKey extends OggettoBulk implements KeyedPersistent {
	private String cdCds;
	private String cdMagazzino;
	private Integer esercizio;
	private String cdNumeratoreMag;
	private Integer pgLotto;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: LOTTO_MAG
	 **/
	public LottoMagKey() {
		super();
	}
	public LottoMagKey(String cdCds, String cdMagazzino, Integer esercizio, String cdNumeratoreMag, Integer pgLotto) {
		super();
		this.cdCds=cdCds;
		this.cdMagazzino=cdMagazzino;
		this.esercizio=esercizio;
		this.cdNumeratoreMag=cdNumeratoreMag;
		this.pgLotto=pgLotto;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof LottoMagKey)) return false;
		LottoMagKey k = (LottoMagKey) o;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdMagazzino(), k.getCdMagazzino())) return false;
		if (!compareKey(getEsercizio(), k.getEsercizio())) return false;
		if (!compareKey(getCdNumeratoreMag(), k.getCdNumeratoreMag())) return false;
		if (!compareKey(getPgLotto(), k.getPgLotto())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdMagazzino());
		i = i + calculateKeyHashCode(getEsercizio());
		i = i + calculateKeyHashCode(getCdNumeratoreMag());
		i = i + calculateKeyHashCode(getPgLotto());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdCds]
	 **/
	public void setCdCds(String cdCds)  {
		this.cdCds=cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdCds]
	 **/
	public String getCdCds() {
		return cdCds;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdMagazzino]
	 **/
	public void setCdMagazzino(String cdMagazzino)  {
		this.cdMagazzino=cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdMagazzino]
	 **/
	public String getCdMagazzino() {
		return cdMagazzino;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [esercizio]
	 **/
	public void setEsercizio(Integer esercizio)  {
		this.esercizio=esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [esercizio]
	 **/
	public Integer getEsercizio() {
		return esercizio;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdNumeratoreMag]
	 **/
	public void setCdNumeratoreMag(String cdNumeratoreMag)  {
		this.cdNumeratoreMag=cdNumeratoreMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdNumeratoreMag]
	 **/
	public String getCdNumeratoreMag() {
		return cdNumeratoreMag;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgLotto]
	 **/
	public void setPgLotto(Integer pgLotto)  {
		this.pgLotto=pgLotto;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgLotto]
	 **/
	public Integer getPgLotto() {
		return pgLotto;
	}
}