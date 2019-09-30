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
 * Formattatore di importi
 */

public abstract class ImportoFormat extends java.text.Format implements it.cnr.jada.bulk.StyledFormat {

    protected static final java.text.Format format = new java.text.DecimalFormat("#,##0.00");
    private int precision = 0;
    private int round = java.math.BigDecimal.ROUND_HALF_UP;

    public ImportoFormat() {
        super();
    }

    /**
     * Effettua la formattazione
     */
    public StringBuffer format(Object obj, StringBuffer toAppendTo, java.text.FieldPosition pos) {
        if (obj != null) {
            getFormat().format(obj, toAppendTo, pos);
            String symbol = getSymbol();
            if (symbol != null) {
                toAppendTo.append(' ');
                toAppendTo.append(symbol);
            }
        }
        return toAppendTo;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'format'
     *
     * @return Il valore della proprietà 'format'
     */
    public java.text.Format getFormat() {
        return format;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'precision'
     *
     * @return Il valore della proprietà 'precision'
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'precision'
     *
     * @param newPrecision Il valore da assegnare a 'precision'
     */
    public void setPrecision(int newPrecision) {
        precision = newPrecision;
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'round'
     *
     * @return Il valore della proprietà 'round'
     */
    public int getRound() {
        return round;
    }

    /**
     * <!-- @TODO: da completare -->
     * Imposta il valore della proprietà 'round'
     *
     * @param newRound Il valore da assegnare a 'round'
     */
    public void setRound(int newRound) {
        round = newRound;
    }

    /**
     * Restituisce "text-align: right"
     *
     * @return String
     */
    public String getStyle(Object value) {
        return "text-align: right";
    }

    /**
     * <!-- @TODO: da completare -->
     * Restituisce il valore della proprietà 'symbol'
     *
     * @return Il valore della proprietà 'symbol'
     */
    public String getSymbol() {
        return null;
    }

    /**
     * Effettua le operazioni di parse
     */
    public Object parseObject(String source, java.text.ParsePosition status) {
        status.setIndex(source == null ? 1 : source.length());
        if (source == null || source.equals("")) return null;
        source = source.trim();
        if (source.length() == 0) return null;
        if (source.endsWith(String.valueOf(getSymbol()))) {
            source = source.substring(0, source.length() - 1);
            source = source.trim();
        }
        Number number;
        try {
            number = (Number) getFormat().parseObject(source);
        } catch (java.text.ParseException pe) {
            status.setIndex(0);
            return null;
        } catch (java.lang.NumberFormatException pe) {
            status.setIndex(0);
            return null;
        }
        if (number == null) return null;
        java.math.BigDecimal bd;
        if (number instanceof java.math.BigDecimal)
            bd = (java.math.BigDecimal) number;
        else if (number instanceof java.math.BigInteger)
            bd = new java.math.BigDecimal((java.math.BigInteger) number);
        else if (number instanceof Long)
            bd = java.math.BigDecimal.valueOf(number.longValue());
        else
            bd = new java.math.BigDecimal(number.doubleValue());
        //if (bd.signum() < 0)
        //status.setIndex(0);
        bd = bd.setScale(getPrecision(), getRound());
        return bd;
    }
}