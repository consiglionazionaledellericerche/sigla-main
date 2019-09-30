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

import java.util.Iterator;

import it.cnr.jada.bulk.BulkList;

public class Blt_autorizzati_dettBulk extends Blt_autorizzati_dettBase {
	private BulkList bltVisiteColl = new BulkList();
	/**
	 * [BLT_PROGETTI null]
	 **/
	private Blt_autorizzatiBulk bltAutorizzati =  new Blt_autorizzatiBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_PROGRAMMA_VISITE
	 **/
	public Blt_autorizzati_dettBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_PROGRAMMA_VISITE
	 **/
	public Blt_autorizzati_dettBulk(java.lang.String cdAccordo, java.lang.String cdProgetto, java.lang.Integer cdTerzo,  java.lang.Long pgAutorizzazione) {
		super(cdAccordo, cdProgetto, cdTerzo, pgAutorizzazione);
		setBltAutorizzati( new Blt_autorizzatiBulk(cdAccordo,cdProgetto,cdTerzo) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public Blt_autorizzatiBulk getBltAutorizzati() {
		return bltAutorizzati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setBltAutorizzati(Blt_autorizzatiBulk bltAutorizzati)  {
		this.bltAutorizzati=bltAutorizzati;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAccordo]
	 **/
	public java.lang.String getCdAccordo() {
		Blt_autorizzatiBulk bltAutorizzati = this.getBltAutorizzati();
		if (bltAutorizzati == null)
			return null;
		return getBltAutorizzati().getCdAccordo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAccordo]
	 **/
	public void setCdAccordo(java.lang.String cdAccordo)  {
		this.getBltAutorizzati().setCdAccordo(cdAccordo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProgetto]
	 **/
	public java.lang.String getCdProgetto() {
		Blt_autorizzatiBulk bltAutorizzati = this.getBltAutorizzati();
		if (bltAutorizzati == null)
			return null;
		return getBltAutorizzati().getCdProgetto();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProgetto]
	 **/
	public void setCdProgetto(java.lang.String cdProgetto)  {
		this.getBltAutorizzati().setCdProgetto(cdProgetto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public Integer getCdTerzo() {
		Blt_autorizzatiBulk bltAutorizzati = this.getBltAutorizzati();
		if (bltAutorizzati == null)
			return null;
		return getBltAutorizzati().getCdTerzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(Integer cdTerzo) {
		this.getBltAutorizzati().setCdTerzo(cdTerzo);
	}

	public BulkList getBltVisiteColl() {
		return bltVisiteColl;
	}
	public void setBltVisiteColl(BulkList bltVisiteColl) {
		this.bltVisiteColl = bltVisiteColl;
	}
	public Blt_visiteBulk getBltVisitaValida() {
		for (Iterator iterator = getBltVisiteColl().iterator(); iterator.hasNext();) {
			Blt_visiteBulk visita = (Blt_visiteBulk) iterator.next();
			if (!visita.isVisitaAnnullata())
				return visita;
		}
		return null;
	}
	public java.lang.Boolean getFlVisitaEffettuata() {
		return getBltVisitaValida()!=null;
	}
}