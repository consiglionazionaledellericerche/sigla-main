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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class AbilUtenteUopOperKey extends OggettoBulk implements KeyedPersistent {
	private java.lang.String cdUtente;
	private java.lang.String cdUnitaOperativa;
	private java.lang.String cdTipoOperazione;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABIL_UTENTE_UOP_OPER
	 **/
	public AbilUtenteUopOperKey() {
		super();
	}
	public AbilUtenteUopOperKey(java.lang.String cdUtente, java.lang.String cdUnitaOperativa, java.lang.String cdTipoOperazione) {
		super();
		this.cdUtente=cdUtente;
		this.cdUnitaOperativa=cdUnitaOperativa;
		this.cdTipoOperazione=cdTipoOperazione;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof AbilUtenteUopOperKey)) return false;
		AbilUtenteUopOperKey k = (AbilUtenteUopOperKey) o;
		if (!compareKey(getCdUtente(), k.getCdUtente())) return false;
		if (!compareKey(getCdUnitaOperativa(), k.getCdUnitaOperativa())) return false;
		if (!compareKey(getCdTipoOperazione(), k.getCdTipoOperazione())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdUtente());
		i = i + calculateKeyHashCode(getCdUnitaOperativa());
		i = i + calculateKeyHashCode(getCdTipoOperazione());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUtente]
	 **/
	public void setCdUtente(java.lang.String cdUtente)  {
		this.cdUtente=cdUtente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUtente]
	 **/
	public java.lang.String getCdUtente() {
		return cdUtente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(java.lang.String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public java.lang.String getCdUnitaOperativa() {
		return cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOperazione]
	 **/
	public void setCdTipoOperazione(java.lang.String cdTipoOperazione)  {
		this.cdTipoOperazione=cdTipoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOperazione]
	 **/
	public java.lang.String getCdTipoOperazione() {
		return cdTipoOperazione;
	}
}