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
 * Formattatore di importi percentuali
 */

public class PercentFormat extends java.text.Format {

	protected static int defaultPrecision = 2;
	
	private static final java.text.Format defaultFormat = new java.text.DecimalFormat("0.00");
public PercentFormat() {
	super();
}
/**
  * Effettua la formattazione
  */
public StringBuffer format(Object obj, StringBuffer toAppendTo, java.text.FieldPosition pos) {
	if (obj != null) {
		getFormat().format(obj,toAppendTo,pos);
		toAppendTo.append('%');
	}
	return toAppendTo;
}
/**
 * Insert the method's description here.
 * Creation date: (12/17/2002 10:31:45 AM)
 * @return int
 */
public java.text.Format getFormat() {
	return defaultFormat;
}
/**
 * Insert the method's description here.
 * Creation date: (12/17/2002 10:31:45 AM)
 * @return int
 */
public int getPrecision() {
	return defaultPrecision;
}
/**
  * Effettua le operazioni di parse
  */
public Object parseObject(String source, java.text.ParsePosition status) {
	if (source == null) {
		status.setIndex(1);
		return null;
	}
	source = source.trim();
	if (source.length() == 0)
		return null;
	if (source.endsWith("%"))
		source = source.substring(0,source.length()-1);
	Number number = (Number)getFormat().parseObject(source,status);
	if (number == null) return null;
	java.math.BigDecimal bd;
	if (number instanceof java.math.BigDecimal)
		bd = (java.math.BigDecimal)number;
	else if (number instanceof java.math.BigInteger)
		bd = new java.math.BigDecimal((java.math.BigInteger)number);
	else if (number instanceof Long)
		bd = java.math.BigDecimal.valueOf(number.longValue());
	else 
		bd = new java.math.BigDecimal(number.doubleValue());
	bd = bd.setScale(getPrecision(),bd.ROUND_HALF_UP);
	return bd;
}
}
