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
 * Date 15/12/2010
 */
package it.cnr.contab.config00.pdcfin.bulk;
import it.cnr.jada.persistency.Keyed;

import java.math.BigDecimal;

public class LimiteSpesaClassBase extends LimiteSpesaClassKey implements Keyed {
	private java.math.BigDecimal im_limite_assestato;

	public LimiteSpesaClassBase() {
		super();
	}

	public LimiteSpesaClassBase(Integer id_classificazione, String cdCds) {
		super(id_classificazione, cdCds);
	}

	public BigDecimal getIm_limite_assestato() {
		return im_limite_assestato;
	}

	public void setIm_limite_assestato(BigDecimal im_limite_assestato) {
		this.im_limite_assestato = im_limite_assestato;
	}
}