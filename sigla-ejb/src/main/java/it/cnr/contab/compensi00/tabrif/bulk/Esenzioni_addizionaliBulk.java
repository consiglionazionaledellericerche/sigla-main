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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 13/06/2007
 */
package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.SimpleBulkList;
public class Esenzioni_addizionaliBulk extends Esenzioni_addizionaliBase {
	public Esenzioni_addizionaliBulk() {
		super();
	}
	public Esenzioni_addizionaliBulk(java.lang.String cd_catastale) {
		super(cd_catastale);
	}
	private SimpleBulkList dettagli= new SimpleBulkList();
	public SimpleBulkList getDettagli() {
		return dettagli;
	}
	public void setDettagli(SimpleBulkList list) {
		dettagli = list;
	}
	public Esenzioni_addizionaliBulk removeFromDettagli( int indiceDiLinea ) {

		Esenzioni_addizionaliBulk element = (Esenzioni_addizionaliBulk)dettagli.get(indiceDiLinea);

		return (Esenzioni_addizionaliBulk)dettagli.remove(indiceDiLinea);
	}

	public int addToDettagli (Esenzioni_addizionaliBulk nuova)
	{
			getDettagli().add(nuova);
			return getDettagli().size()-1;
	}
}