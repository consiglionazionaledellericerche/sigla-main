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

package it.cnr.contab.pagopa.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;

import java.util.Optional;

public class PendenzaPagopaBulk extends PendenzaPagopaBase {
	private static final long serialVersionUID = 1L;

	public static final String TIPO_POSIZIONE_CREDITORIA = "C";
	public static final String TIPO_POSIZIONE_DEBITORIA = "D";
	public static final String STATO_VALIDO = "VAL";
	public static final String STATO_CHIUSO = "CHI";
	public static final String STATO_ANNULLATO = "ANN";
	private Unita_organizzativaBulk unitaOrganizzativa;
	protected TerzoBulk terzo;
	private TipoPendenzaPagopaBulk tipoScadenzaPagopa;


	public PendenzaPagopaBulk() {
		super();
	}

	public PendenzaPagopaBulk(Long id) {
		super(id);
	}

	public Unita_organizzativaBulk getUnitaOrganizzativa() {
		return unitaOrganizzativa;
	}
	
	public void setUnitaOrganizzativa(Unita_organizzativaBulk unitaOrganizzativa) {
		this.unitaOrganizzativa = unitaOrganizzativa;
	}
	
	@Override
	public String getCdUnitaOrganizzativa() {
		return Optional.ofNullable(getUnitaOrganizzativa())
				.map(Unita_organizzativaBulk::getCd_unita_organizzativa)
				.orElse(null);
	}
	
	@Override
	public void setCdUnitaOrganizzativa(String cdUnitaOrganizzativa) {
		Optional.ofNullable(getUnitaOrganizzativa()).ifPresent(el->el.setCd_unita_organizzativa(cdUnitaOrganizzativa));
	}
	
	public TipoPendenzaPagopaBulk getTipoScadenzaPagopa() {
		return tipoScadenzaPagopa;
	}

	public void setTipoScadenzaPagopa(TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk) {
		this.tipoScadenzaPagopa = tipoPendenzaPagopaBulk;
	}

	public TerzoBulk getTerzo() {
		return terzo;
	}

	public void setTerzo(TerzoBulk terzo) {
		this.terzo = terzo;
	}

	@Override
	public Integer getIdTipoScadenzaPagopa() {
		return Optional.ofNullable(getTipoScadenzaPagopa())
					.map(TipoPendenzaPagopaBulk::getId)
					.orElse(null);
	}
	
	@Override
	public void setIdTipoScadenzaPagopa(Integer idTipoScadenzaPagopa) {
		Optional.ofNullable(getTipoScadenzaPagopa()).ifPresent(el->el.setId(idTipoScadenzaPagopa));
	}
	public void setCd_terzo(java.lang.Integer cd_terzo) {
		Optional.ofNullable(getTerzo()).ifPresent(el->el.setCd_terzo(cd_terzo));
	}
	public java.lang.Integer getCd_terzo() {
		return Optional.ofNullable(getTerzo())
				.map(TerzoBulk::getCd_terzo)
				.orElse(null);
	}
	public Boolean isPendenzaNonModificabile(){
		if (getStato() != null && (getStato().equals(STATO_ANNULLATO) || getStato().equals(STATO_CHIUSO))){
			return true;
		}
		return false;
	}

}