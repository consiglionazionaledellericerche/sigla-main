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
 * Created by BulkGenerator 1.5 [30/07/2008]
 * Date 17/10/2008
 */
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class PreferitiKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cd_utente;
	private java.lang.String business_process;
	private java.lang.String ti_funzione;
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Table name: PREFERITI
	 **/
	public PreferitiKey() {
		super();
	}
	public PreferitiKey(java.lang.String cd_utente, java.lang.String business_process, java.lang.String ti_funzione) {
		super();
		this.cd_utente=cd_utente;
		this.business_process=business_process;
		this.ti_funzione=ti_funzione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof PreferitiKey)) return false;
		PreferitiKey k = (PreferitiKey) o;
		if (!compareKey(getCd_utente(), k.getCd_utente())) return false;
		if (!compareKey(getBusiness_process(), k.getBusiness_process())) return false;
		if (!compareKey(getTi_funzione(), k.getTi_funzione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCd_utente());
		i = i + calculateKeyHashCode(getBusiness_process());
		i = i + calculateKeyHashCode(getTi_funzione());
		return i;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [cd_utente]
	 **/
	public void setCd_utente(java.lang.String cd_utente)  {
		this.cd_utente=cd_utente;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [cd_utente]
	 **/
	public java.lang.String getCd_utente() {
		return cd_utente;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [business_process]
	 **/
	public void setBusiness_process(java.lang.String business_process)  {
		this.business_process=business_process;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [business_process]
	 **/
	public java.lang.String getBusiness_process() {
		return business_process;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Restituisce il valore di: [ti_funzione]
	 **/
	public void setTi_funzione(java.lang.String ti_funzione)  {
		this.ti_funzione=ti_funzione;
	}
	/**
	 * Created by BulkGenerator 1.5 [30/07/2008]
	 * Setta il valore di: [ti_funzione]
	 **/
	public java.lang.String getTi_funzione() {
		return ti_funzione;
	}
}