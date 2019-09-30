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
 * Date 09/09/2010
 */
package it.cnr.contab.doccont00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class MandatoCupBase extends MandatoCupKey implements Keyed {
//    IMPORTO DECIMAL(22,0) NOT NULL
	private java.math.BigDecimal importo;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: MANDATO_CUP
	 **/
	public MandatoCupBase() {
		super();
	}
	public MandatoCupBase(java.lang.String cdCds, java.lang.Integer esercizio, java.lang.Long pgMandato, java.lang.Integer esercizioObbligazione, java.lang.Integer esercizioOriObbligazione, java.lang.Long pgObbligazione, java.lang.Long pgObbligazioneScadenzario, java.lang.String cdCdsDocAmm, java.lang.String cdUoDocAmm, java.lang.Integer esercizioDocAmm, java.lang.String cdTipoDocumentoAmm, java.lang.Long pgDocAmm, java.lang.String cdCup) {
		super(cdCds, esercizio, pgMandato, esercizioObbligazione, esercizioOriObbligazione, pgObbligazione, pgObbligazioneScadenzario, cdCdsDocAmm, cdUoDocAmm, esercizioDocAmm, cdTipoDocumentoAmm, pgDocAmm, cdCup);
	}
	public java.math.BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(java.math.BigDecimal importo) {
		this.importo = importo;
	}
	
}