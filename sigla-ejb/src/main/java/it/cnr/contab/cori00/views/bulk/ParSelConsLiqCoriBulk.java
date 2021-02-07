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

package it.cnr.contab.cori00.views.bulk;

import it.cnr.contab.cori00.docs.bulk.Liquid_coriBulk;

/**
 * OggettoBulk costruito per visualizzare i dati provenienti dalla vista <code>VSX_LIQUIDAZIONE_CORI</code> 
 *
*/

public class ParSelConsLiqCoriBulk extends VConsLiqCoriBulk {

public ParSelConsLiqCoriBulk() {
	super();
}
	private java.lang.Long pgInizio;
	private java.lang.Long pgFine;
	private Liquid_coriBulk daLiquidazione;
	private Liquid_coriBulk aLiquidazione;

	public Liquid_coriBulk getDaLiquidazione() {
		return daLiquidazione;
	}

	public void setDaLiquidazione(Liquid_coriBulk daLiquidazione) {
		this.daLiquidazione = daLiquidazione;
	}

	public Liquid_coriBulk getaLiquidazione() {
		return aLiquidazione;
	}

	public void setaLiquidazione(Liquid_coriBulk aLiquidazione) {
		this.aLiquidazione = aLiquidazione;
	}

	public Long getPgInizio() {
		return pgInizio;
	}

	public void setPgInizio(Long pgInizio) {
		this.pgInizio = pgInizio;
	}

	public Long getPgFine() {
		return pgFine;
	}

	public void setPgFine(Long pgFine) {
		this.pgFine = pgFine;
	}
}
