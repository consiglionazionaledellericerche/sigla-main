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
 * Formattatore di numeri positivi decimali
 */

public class PositiveDecimalFormat extends java.text.DecimalFormat {
public PositiveDecimalFormat() {
	super();
}

public PositiveDecimalFormat(String pattern) {
	super(pattern);
}

public PositiveDecimalFormat(String pattern, java.text.DecimalFormatSymbols symbols) {
	super(pattern, symbols);
}

    /**
     * Operazioni di parse
     *
     * @exception ParseException if the specified string is invalid.
     */
    public Number parse(String text) throws java.text.ParseException {

	    Number obj = super.parse(text);
		if (obj != null) {
			if (((Number)obj).intValue() < 0)
				throw new it.cnr.jada.bulk.ValidationParseException("sono ammessi solo valori positivi!", 0);
		}
		
		return obj;
    }

    /**
     * Operazioni di parse
     *
     * @exception ParseException if the specified string is invalid.
     */
    public Object parseObject(String text) throws java.text.ParseException {

	    Object obj = super.parseObject(text);
	    if (obj instanceof java.math.BigDecimal) {
		    if (((java.math.BigDecimal)obj).signum() < 0)
				throwValitationException();
		} else if (obj instanceof java.math.BigInteger) {
		    if (((java.math.BigInteger)obj).signum() < 0)
				throwValitationException();
	    } else if (obj instanceof Long) {
			if (((Long)obj).longValue() < 0)
				throwValitationException();
		} else if (obj instanceof Integer) {
			if (((Integer)obj).intValue() < 0)
				throwValitationException();
		} else if (obj instanceof Float) {
			if (((Float)obj).floatValue() < 0)
				throwValitationException();
		} else if (obj instanceof Double) {
			if (((Double)obj).doubleValue() < 0)
				throwValitationException();
		} else if (obj instanceof Byte) {
			if (((Byte)obj).byteValue() < 0)
				throwValitationException();
		} else if (obj instanceof Short) {
			if (((Short)obj).shortValue() < 0)
				throwValitationException();
		}	
		return obj;
    }

    private void throwValitationException() throws it.cnr.jada.bulk.ValidationParseException {
	    throw new it.cnr.jada.bulk.ValidationParseException("sono ammessi solo valori positivi!", 0);
    }
}