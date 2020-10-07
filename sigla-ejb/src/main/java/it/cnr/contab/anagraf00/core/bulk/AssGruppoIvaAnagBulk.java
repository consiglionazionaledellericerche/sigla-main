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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.OggettoBulk;

/**
 * Gestione dei dati relativi alla tabella Anagrafico/Terzo
 */

public class AssGruppoIvaAnagBulk extends AssGruppoIvaAnagBase {
	private AnagraficoBulk anagrafico;
	private AnagraficoBulk anagraficoGruppoIva;

	public AssGruppoIvaAnagBulk() {
		super();
	}

	public AssGruppoIvaAnagBulk(Integer cd_anag, Integer cd_anag_gr_iva) {
		super(cd_anag, cd_anag_gr_iva);
	}

	/**
	 * Restituisce l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
	 *
	 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 *
	 * @see setAnagrafico
	 */

	public AnagraficoBulk getAnagrafico() {
		return anagrafico;
	}

	public void setAnagrafico(AnagraficoBulk newAnagrafico) {
		anagrafico = newAnagrafico;
	}

	public AnagraficoBulk getAnagraficoGruppoIva() {
		return anagraficoGruppoIva;
	}

	public void setAnagraficoGruppoIva(AnagraficoBulk anagraficoGruppoIva) {
		this.anagraficoGruppoIva = anagraficoGruppoIva;
	}

	public OggettoBulk initialize(it.cnr.jada.util.action.CRUDBP bp, it.cnr.jada.action.ActionContext context) {

		this.setStato("INS");

		return this;
	}

	@Override
	public Integer getCd_anag() {
		if (getAnagrafico()==null) return null;
		return getAnagrafico().getCd_anag();
	}
	
	@Override
	public Integer getCd_anag_gr_iva() {
		if (getAnagraficoGruppoIva()==null) return null;
		return getAnagraficoGruppoIva().getCd_anag();
	}
	
	@Override
	public void setCd_anag(Integer cd_anag) {
		if (getAnagrafico()!=null) 
			getAnagrafico().setCd_anag(cd_anag);
	}	

	@Override
	public void setCd_anag_gr_iva(Integer cd_anag_gr_iva) {
		if (getAnagraficoGruppoIva()!=null)
			getAnagraficoGruppoIva().setCd_anag(cd_anag_gr_iva);
	}	
}
