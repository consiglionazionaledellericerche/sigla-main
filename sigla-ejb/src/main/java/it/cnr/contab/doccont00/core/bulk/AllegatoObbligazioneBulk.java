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

package it.cnr.contab.doccont00.core.bulk;

import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.contab.util00.bulk.storage.AllegatoGenericoBulk;
import it.cnr.si.spring.storage.annotation.StoragePolicy;
import it.cnr.si.spring.storage.annotation.StorageProperty;
import it.cnr.si.spring.storage.converter.Converter;

import java.util.Date;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Optional;

public class AllegatoObbligazioneBulk extends AllegatoGenericoBulk {
	private static final long serialVersionUID = 1L;

	private static final java.util.Dictionary ti_allegatiKeys =  new it.cnr.jada.util.OrderedHashtable();
	private static final java.util.Dictionary ti_allegatiSenzaRiaccertamentoKeys =  new it.cnr.jada.util.OrderedHashtable();

	private java.util.Dictionary tipoAllegatiKeys;

	final public static String TIPO_RIACCERTAMENTO_RESIDUI = "RIACCERTAMENTO_RESIDUI";
	final public static String TIPO_DETERMINA = "DETERMINA";

	static {
		ti_allegatiSenzaRiaccertamentoKeys.put(TIPO_DETERMINA,"Determina");

		ti_allegatiKeys.put(TIPO_RIACCERTAMENTO_RESIDUI,"Riaccertamento Residui");
		ti_allegatiKeys.put(TIPO_DETERMINA,"Determina");
	}

	private Integer esercizioDiAppartenenza; 

	private Date determinaDataProtocollo;

	private String tipoAllegato;

	public AllegatoObbligazioneBulk() {
		super();
	}

	public AllegatoObbligazioneBulk(StorageObject storageObject) {
		super(storageObject.getKey());
	}

	public AllegatoObbligazioneBulk(String storageKey) {
		super(storageKey);
	}

	public void setEsercizioDiAppartenenza(Integer esercizioDiAppartenenza) {
		this.esercizioDiAppartenenza = esercizioDiAppartenenza;
	}
	
	public Integer getEsercizioDiAppartenenza() {
		return esercizioDiAppartenenza;
	}

	public Date getDeterminaDataProtocollo() {
		return determinaDataProtocollo;
	}

	public void setDeterminaDataProtocollo(Date determinaDataProtocollo) {
		this.determinaDataProtocollo = determinaDataProtocollo;
	}

	public String getTipoAllegato() {
		return tipoAllegato;
	}

	public void setTipoAllegato(String tipoAllegato) {
		this.tipoAllegato = tipoAllegato;
	}

	public java.util.Dictionary getTi_allegatoKeys() {
		return Optional.ofNullable(this.getTipoAllegatiKeys()).orElse(ti_allegatiKeys);
	}

	public boolean isTypeEnabled(){
		return !isToBeCreated();
	}

	public boolean isTipoRiaccertamentoResidui() {
		return TIPO_RIACCERTAMENTO_RESIDUI.equals(this.getTipoAllegato());
	}

	public boolean isTipoDetermina() {
		return TIPO_DETERMINA.equals(this.getTipoAllegato());
	}

	public Dictionary getTipoAllegatiKeys() {
		return tipoAllegatiKeys;
	}

	public void setTipoAllegatiKeys(Dictionary tipoAllegatiKeys) {
		this.tipoAllegatiKeys = tipoAllegatiKeys;
	}

	public void setTipoAllegatiAllKeys() {
		this.setTipoAllegatiKeys(ti_allegatiKeys);
	}

	public void setTipoAllegatiSenzaRiaccertamentoKeys() {
		this.setTipoAllegatiKeys(ti_allegatiSenzaRiaccertamentoKeys);
	}
}