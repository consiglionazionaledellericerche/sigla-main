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

package it.cnr.contab.util;
/**
 * Formattatore di importi a 2 cifre decimali positivi
 */

public class Importo2CifrePositivoFormat extends Importo2CifreFormat {
public Importo2CifrePositivoFormat() {
	super();
}

public Object parseObject(String source) throws java.text.ParseException{

	Object obj = super.parseObject(source);
	if (obj != null && obj instanceof java.math.BigDecimal) {
		java.math.BigDecimal bd = (java.math.BigDecimal)obj;
		if (bd.signum() < 0)
			throw new it.cnr.jada.bulk.ValidationParseException("sono ammessi solo valori positivi!", 0);
	}
	return obj;
	
}
}