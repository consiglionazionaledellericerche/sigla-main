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
 * Date 21/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;
public class Blt_regole_diariaBulk extends Blt_regole_diariaBase {
	/**
	 * [BLT_ACCORDI Contiene la descrizione dettagliata e le caratteristiche principali dell'Accordo Bilaterale tra CNR e Ente omologo straniero da gestire]
	 **/
	private Blt_accordiBulk bltAccordi =  new Blt_accordiBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_REGOLE_DIARIA
	 **/
	public Blt_regole_diariaBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_REGOLE_DIARIA
	 **/
	public Blt_regole_diariaBulk(java.lang.String cdAccordo, java.lang.Integer pgRegola) {
		super(cdAccordo, pgRegola);
		setBltAccordi( new Blt_accordiBulk(cdAccordo) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Contiene la descrizione dettagliata e le caratteristiche principali dell'Accordo Bilaterale tra CNR e Ente omologo straniero da gestire]
	 **/
	public Blt_accordiBulk getBltAccordi() {
		return bltAccordi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Contiene la descrizione dettagliata e le caratteristiche principali dell'Accordo Bilaterale tra CNR e Ente omologo straniero da gestire]
	 **/
	public void setBltAccordi(Blt_accordiBulk bltAccordi)  {
		this.bltAccordi=bltAccordi;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAccordo]
	 **/
	public java.lang.String getCdAccordo() {
		Blt_accordiBulk bltAccordi = this.getBltAccordi();
		if (bltAccordi == null)
			return null;
		return getBltAccordi().getCd_accordo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAccordo]
	 **/
	public void setCdAccordo(java.lang.String cdAccordo)  {
		this.getBltAccordi().setCd_accordo(cdAccordo);
	}
}