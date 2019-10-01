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

package it.cnr.contab.util.enumeration;

public enum TipoDebitoSIOPE {
    COMMERCIALE("COMMERCIALE", "C"),
    NON_COMMERCIALE("NON COMMERCIALE", "N"),
    IVA("IVA", "I");

    private final String label, value;

    private TipoDebitoSIOPE(String label, String value) {
        this.value = value;
        this.label = label;
    }

    public String value() {
        return value;
    }

    public String label() {
        return label;
    }

    public static TipoDebitoSIOPE getValueFrom(String value) {
        for (TipoDebitoSIOPE esito : TipoDebitoSIOPE.values()) {
            if (esito.value.equals(value))
                return esito;
        }
        throw new IllegalArgumentException("TipoDebitoSIOPE no found for value: " + value);
    }
}
