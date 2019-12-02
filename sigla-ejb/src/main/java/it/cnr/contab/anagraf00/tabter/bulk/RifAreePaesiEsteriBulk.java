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
 * Date 12/09/2011
 */
package it.cnr.contab.anagraf00.tabter.bulk;
import it.cnr.contab.missioni00.tabrif.bulk.TipoAreaGeografica;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.action.CRUDBP;
public class RifAreePaesiEsteriBulk extends RifAreePaesiEsteriBase {
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RIF_AREE_PAESI_ESTERI
	 **/
	public RifAreePaesiEsteriBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RIF_AREE_PAESI_ESTERI
	 **/
	public RifAreePaesiEsteriBulk(java.lang.String cd_area_estera) {
		super(cd_area_estera);
	}
	public java.util.Dictionary getTi_italia_esteroKeys() {
		return TipoAreaGeografica.TIPI_AREA_GEOGRAFICA;
	}
}