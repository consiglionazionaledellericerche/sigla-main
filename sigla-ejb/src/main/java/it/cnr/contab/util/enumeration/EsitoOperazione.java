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

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

public enum EsitoOperazione {
    ACQUISITO("ACQUISITO", "ACQUISITO"),
    NON_ACQUISITO("NON ACQUISITO", "NON_ACQUISITO"),
    VARIATO("VARIATO", "VARIATO"),
    NON_VARIATO("NON VARIATO", "NON_VARIATO"),
    ANNULLATO("ANNULLATO", "ANNULLATO"),
    NON_ANNULLATO("NON ANNULLATO", "NON_ANNULLATO"),
    SOSTITUITO("SOSTITUITO", "SOSTITUITO"),
    NON_SOSTITUITO("NON SOSTITUITO", "NON_SOSTITUITO"),
    RISCOSSO("RISCOSSO", "RISCOSSO"),
    PAGATO("PAGATO", "PAGATO"),
    STORNATO("STORNATO", "STORNATO"),
    REGOLARIZZATO("REGOLARIZZATO", "REGOLARIZZATO"),
    NON_REGOLARIZZATO("NON REGOLARIZZATO", "NON_REGOLARIZZATO"),
    NON_ESEGUIBILE("NON ESEGUIBILE", "NON_ESEGUIBILE");

    private final String label, value;

    EsitoOperazione(String label, String value) {
        this.value = value;
        this.label = label;
    }

    public String value() {
        return value;
    }

    public String label() {
        return label;
    }

    public static String getValueFromLabel(String label) {
        for (EsitoOperazione esito : EsitoOperazione.values()) {
            if (esito.label.equals(label))
                return esito.value;
        }
        throw new IllegalArgumentException("Esito no found for label: " + label);
    }

    public final static Map<String,String> KEYS = Arrays.asList(EsitoOperazione.values())
            .stream()
            .collect(Collectors.toMap(
                    EsitoOperazione::value,
                    EsitoOperazione::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));
}