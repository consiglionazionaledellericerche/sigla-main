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
 * Date 01/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
public class Blt_programma_visiteBulk extends Blt_programma_visiteBase {
	/**
	 * [BLT_PROGETTI null]
	 **/
	private Blt_progettiBulk bltProgetti =  new Blt_progettiBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_PROGRAMMA_VISITE
	 **/
	public Blt_programma_visiteBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_PROGRAMMA_VISITE
	 **/
	public Blt_programma_visiteBulk(java.lang.String cdAccordo, java.lang.String cdProgetto, java.lang.Long pgRecord) {
		super(cdAccordo, cdProgetto, pgRecord);
		setBltProgetti( new Blt_progettiBulk(cdAccordo,cdProgetto) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public Blt_progettiBulk getBltProgetti() {
		return bltProgetti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setBltProgetti(Blt_progettiBulk bltProgetti)  {
		this.bltProgetti=bltProgetti;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAccordo]
	 **/
	public java.lang.String getCdAccordo() {
		Blt_progettiBulk bltProgetti = this.getBltProgetti();
		if (bltProgetti == null)
			return null;
		return getBltProgetti().getCd_accordo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAccordo]
	 **/
	public void setCdAccordo(java.lang.String cdAccordo)  {
		this.getBltProgetti().setCd_accordo(cdAccordo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProgetto]
	 **/
	public java.lang.String getCdProgetto() {
		Blt_progettiBulk bltProgetti = this.getBltProgetti();
		if (bltProgetti == null)
			return null;
		return getBltProgetti().getCd_progetto();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProgetto]
	 **/
	public void setCdProgetto(java.lang.String cdProgetto)  {
		this.getBltProgetti().setCd_progetto(cdProgetto);
	}
}