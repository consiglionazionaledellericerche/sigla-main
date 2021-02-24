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

package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.util.action.*;

/**
 * Gestione dei dati relativi alla tabella Comune
 */

public class ComuneBulk extends ComuneBase {

	private ProvinciaBulk provincia;
	private NazioneBulk nazione;

	private Boolean da_inserire = false;

	public Boolean getDa_inserire() {
		return da_inserire;
	}

	public void setDa_inserire(Boolean da_inserire) {
		this.da_inserire = da_inserire;
	}

	public static final String COMUNE_ITALIANO = "I";
	public static final String COMUNE_ESTERO   = "E";

	public static final String CODICE_CATASTALE_ESTERO= "*";
	
	protected static final long PG_PRIMO_COMUNE_ESTERO = 40000;

	public final static it.cnr.jada.util.OrderedHashtable TI_ITALIANO_ESTERO = new it.cnr.jada.util.OrderedHashtable();
	static {
		TI_ITALIANO_ESTERO.put(COMUNE_ITALIANO, "Italiano");
		TI_ITALIANO_ESTERO.put(COMUNE_ESTERO, "Estero");
	}
/**
 * 
 */
public ComuneBulk() {
	super();
}
public ComuneBulk(java.lang.Long pg_comune) {
	super(pg_comune);
}
public java.lang.String getCd_provincia() {
	it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk provincia = this.getProvincia();
	if (provincia == null)
		return null;
	return provincia.getCd_provincia();
}
	/**
	 * Restituisce la nazione associata al comune.
	 *
	 * @return <code>it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk</code>
	 *
	 * @see setNazione
	 */

	public NazioneBulk getNazione() {
		return nazione;
	}
public java.lang.Long getPg_nazione() {
	it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk nazione = this.getNazione();
	if (nazione == null)
		return null;
	return nazione.getPg_nazione();
}
	/**
	 * Restituisce la provincia associata al comune.
	 *
	 * @return <code>it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk</code>
	 *
	 * @see setProvincia
	 */

	public ProvinciaBulk getProvincia() {
		return provincia;
	}
public java.util.Dictionary getTi_italiano_esteroKeys() {
	return TI_ITALIANO_ESTERO;
}
/**
  * Inizializza l'Oggetto Bulk COMUNE
  * Questo metodo è chiamato sia dall'initializeForInsert()
  * sia dall'initializeForSearch()
  *
 */

public OggettoBulk initialize(CRUDBP bp, ActionContext context) {

	super.initialize(bp, context);
	setNazione(new NazioneBulk());
	setProvincia(new ProvinciaBulk());
	return this;
}
public boolean isEstero(){
	return false;
}
public boolean isItaliano(){
	return false;
}
public boolean isROCdProvincia() {
	return getProvincia()==null || getProvincia().getCrudStatus()==NORMAL;
}
public boolean isROPgNazione() {
	return getNazione()==null || getNazione().getCrudStatus()==NORMAL;
}
public void setCd_provincia(java.lang.String cd_provincia) {
	this.getProvincia().setCd_provincia(cd_provincia);
}
	/**
	 * Imposta la nazione associata al comune.
	 *
	 * @param newNazione <code>it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk</code>
	 *
	 * @see getNazione
	 */

	public void setNazione(NazioneBulk newNazione) {
		nazione = newNazione;
	}
public void setPg_nazione(java.lang.Long pg_nazione) {
	this.getNazione().setPg_nazione(pg_nazione);
}
	/**
	 * Imposta la provincia associata al comune.
	 *
	 * @param newProvincia <code>it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk</code>
	 *
	 * @see getProvincia
	 */

	public void setProvincia(ProvinciaBulk newProvincia) {
		provincia = newProvincia;
	}
}
