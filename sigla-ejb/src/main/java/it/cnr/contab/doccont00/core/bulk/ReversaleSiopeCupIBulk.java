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
 * Date 07/05/2007
 */
package it.cnr.contab.doccont00.core.bulk;


public class ReversaleSiopeCupIBulk extends ReversaleSiopeCupBulk {

	
	protected Reversale_siopeIBulk reversale_siopeI;
	public ReversaleSiopeCupIBulk() {
		super();
	}

	public ReversaleSiopeCupIBulk(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgReversale, java.lang.Integer esercizioAccertamento, java.lang.Integer esercizioOriAccertamento, java.lang.Long pgAccertamento, java.lang.Long pgAccertamentoScadenzario, java.lang.String cdCdsDocAmm, java.lang.String cdUoDocAmm, java.lang.Integer esercizioDocAmm, java.lang.String cdTipoDocumentoAmm, java.lang.Long pgDocAmm, java.lang.Integer esercizioSiope, java.lang.String tiGestione, java.lang.String cdSiope, java.lang.String cdCup) {
		super(cdCds, esercizio, pgReversale, esercizioAccertamento, esercizioOriAccertamento, pgAccertamento, pgAccertamentoScadenzario, cdCdsDocAmm, cdUoDocAmm, esercizioDocAmm, cdTipoDocumentoAmm, pgDocAmm, esercizioSiope, tiGestione, cdSiope, cdCup);
	}

	public Reversale_siopeBulk getReversaleSiope() {
		return getReversale_siopeI();
	}	
	public void setReversaleSiope(Reversale_siopeBulk siope) {
		setReversale_siopeI((Reversale_siopeIBulk)siope);
	}

	public Reversale_siopeIBulk getReversale_siopeI() {
		return reversale_siopeI;
	}

	public void setReversale_siopeI(Reversale_siopeIBulk reversale_siopeI) {
		this.reversale_siopeI = reversale_siopeI;
	}

	

	
	
}