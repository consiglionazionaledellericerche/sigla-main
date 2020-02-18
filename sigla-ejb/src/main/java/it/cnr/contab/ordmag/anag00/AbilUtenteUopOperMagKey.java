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
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;
public class AbilUtenteUopOperMagKey extends OggettoBulk implements KeyedPersistent {
	private String cdUtente;
	private String cdUnitaOperativa;
	private String cdTipoOperazione;
	private String cdCds;
	private String cdMagazzino;
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABIL_UTENTE_UOP_OPER_MAG
	 **/
	public AbilUtenteUopOperMagKey() {
		super();
	}
	public AbilUtenteUopOperMagKey(String cdUtente, String cdUnitaOperativa, String cdTipoOperazione, String cdCds, String cdMagazzino) {
		super();
		this.cdUtente=cdUtente;
		this.cdUnitaOperativa=cdUnitaOperativa;
		this.cdTipoOperazione=cdTipoOperazione;
		this.cdCds=cdCds;
		this.cdMagazzino=cdMagazzino;
	}
	public boolean equalsByPrimaryKey(Object o) {
		if (this== o) return true;
		if (!(o instanceof AbilUtenteUopOperMagKey)) return false;
		AbilUtenteUopOperMagKey k = (AbilUtenteUopOperMagKey) o;
		if (!compareKey(getCdUtente(), k.getCdUtente())) return false;
		if (!compareKey(getCdUnitaOperativa(), k.getCdUnitaOperativa())) return false;
		if (!compareKey(getCdTipoOperazione(), k.getCdTipoOperazione())) return false;
		if (!compareKey(getCdCds(), k.getCdCds())) return false;
		if (!compareKey(getCdMagazzino(), k.getCdMagazzino())) return false;
		return true;
	}
	public int primaryKeyHashCode() {
		int i = 0;
		i = i + calculateKeyHashCode(getCdUtente());
		i = i + calculateKeyHashCode(getCdUnitaOperativa());
		i = i + calculateKeyHashCode(getCdTipoOperazione());
		i = i + calculateKeyHashCode(getCdCds());
		i = i + calculateKeyHashCode(getCdMagazzino());
		return i;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUtente]
	 **/
	public void setCdUtente(String cdUtente)  {
		this.cdUtente=cdUtente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUtente]
	 **/
	public String getCdUtente() {
		return cdUtente;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOperativa]
	 **/
	public void setCdUnitaOperativa(String cdUnitaOperativa)  {
		this.cdUnitaOperativa=cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOperativa]
	 **/
	public String getCdUnitaOperativa() {
		return cdUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTipoOperazione]
	 **/
	public void setCdTipoOperazione(String cdTipoOperazione)  {
		this.cdTipoOperazione=cdTipoOperazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTipoOperazione]
	 **/
	public String getCdTipoOperazione() {
		return cdTipoOperazione;
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
}