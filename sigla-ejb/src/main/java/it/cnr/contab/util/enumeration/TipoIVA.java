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

import java.util.Dictionary;

public enum TipoIVA {
    COMMERCIALE("Commerciale", "C"),
    ISTITUZIONALE("Istituzionale", "I");

    public final static Dictionary TipoIVAKeys = new it.cnr.jada.util.OrderedHashtable();
    static {
        for (TipoIVA tipoIVA : TipoIVA.values()) {
            TipoIVAKeys.put(tipoIVA.value(), tipoIVA.label());
        }
    }
    private final String label, value;

    private TipoIVA(String label, String value) {
        this.value = value;
        this.label = label;
    }

    public String value() {
        return value;
    }

    public String label() {
        return label;
    }

    public static TipoIVA getValueFrom(String value) {
        for (TipoIVA tipoIVA : TipoIVA.values()) {
            if (tipoIVA.value.equals(value))
                return tipoIVA;
        }
        throw new IllegalArgumentException("Tipo IVA not found for value: " + value);
    }
}
