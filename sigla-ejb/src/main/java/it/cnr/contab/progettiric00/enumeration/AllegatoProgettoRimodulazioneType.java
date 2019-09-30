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

package it.cnr.contab.progettiric00.enumeration;

public enum AllegatoProgettoRimodulazioneType {
	RIMODULAZIONE("Rimodulazione", "D:sigla_progetti_attachment:rimodulazione_attestato"),
	PROROGA("Proroga", "D:sigla_progetti_attachment:rimodulazione_proroga"),
	AUTOMATICO("Stampa Automatica Rimodulazione", "D:sigla_progetti_attachment:rimodulazione_stampa_automatica"),
	GENERICO("Allegato Generico", "D:sigla_progetti_attachment:rimodulazione_allegato_generico");

    private final String label, value;

    private AllegatoProgettoRimodulazioneType(String label, String value) {
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
        for (AllegatoProgettoRimodulazioneType esito : AllegatoProgettoRimodulazioneType.values()) {
            if (esito.label.equals(label))
                return esito.value;
        }
        throw new IllegalArgumentException("Esito no found for label: " + label);
    }
}