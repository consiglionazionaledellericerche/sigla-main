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
 * Date 02/07/2012
 */
package it.cnr.contab.bilaterali00.bulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class BltIstitutiBulk extends BltIstitutiBase {
	/**
	 * [COMUNE Codifica dei comuni italiani e delle città  estere.E' definito un dialogo utente per popolare le città  estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	private ComuneBulk comune =  new ComuneBulk();
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_ISTITUTI
	 **/
	public BltIstitutiBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_ISTITUTI
	 **/
	public BltIstitutiBulk(java.lang.String cdCentroResponsabilita) {
		super(cdCentroResponsabilita);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codifica dei comuni italiani e delle città  estere.E' definito un dialogo utente per popolare le città  estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	public ComuneBulk getComune() {
		return comune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codifica dei comuni italiani e delle città  estere.E' definito un dialogo utente per popolare le città  estere; per i comuni italiani si prevede il recupero in sede di migrazione]
	 **/
	public void setComune(ComuneBulk comune)  {
		this.comune=comune;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [pgComune]
	 **/
	public java.lang.Long getPgComune() {
		ComuneBulk comune = this.getComune();
		if (comune == null)
			return null;
		return getComune().getPg_comune();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [pgComune]
	 **/
	public void setPgComune(java.lang.Long pgComune)  {
		this.getComune().setPg_comune(pgComune);
	}
}