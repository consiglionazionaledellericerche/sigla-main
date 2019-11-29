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

public enum StatoVariazioneSostituzione {
    DA_VARIARE("DA VARIARE", "DA_VARIARE"),
    DA_SOSTITUIRE("DA SOSTITUIRE", "DA_SOSTITUIRE"),
    VARIAZIONE_DEFINITIVA("VARIAZIONE DEFINITIVA", "VARIAZIONE_DEFINITIVA"),
    SOSTITUZIONE_DEFINITIVA("SOSTITUZIONE DEFINITIVA", "SOSTITUZIONE_DEFINITIVA"),
    VARIAZIONE_TRASMESSA("VARIAZIONE TRASMESSA", "VARIAZIONE_TRASMESSA"),
    SOSTITUZIONE_TRASMESSA("SOSTITUZIONE TRASMESSA", "SOSTITUZIONE_TRASMESSA"),
    VARIATO("VARIATO", "VARIATO"),
    ANNULLATO_PER_SOSTITUZIONE("ANNULLATO PER SOSTITUZIONE", "ANNULLATO_PER_SOSTITUZIONE"),
    SOSTITUITO("SOSTITUITO", "SOSTITUITO"),
    NON_VARIATO("NON VARIATO", "NON_VARIATO"),
    NON_SOSTITUITO("NON SOSTITUITO", "NON_SOSTITUITO");

    private final String label, value;

    StatoVariazioneSostituzione(String label, String value) {
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
        for (StatoVariazioneSostituzione esito : StatoVariazioneSostituzione.values()) {
            if (esito.label.equals(label))
                return esito.value;
        }
        throw new IllegalArgumentException("Stato variazione sostituzione no found for label: " + label);
    }

    public final static Map<String,String> KEYS = Arrays.asList(StatoVariazioneSostituzione.values())
            .stream()
            .collect(Collectors.toMap(
                    StatoVariazioneSostituzione::value,
                    StatoVariazioneSostituzione::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));
}