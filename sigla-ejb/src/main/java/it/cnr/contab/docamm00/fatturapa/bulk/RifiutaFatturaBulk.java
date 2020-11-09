/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.docamm00.fatturapa.bulk;

import it.cnr.jada.bulk.OggettoBulk;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

public class RifiutaFatturaBulk extends OggettoBulk {
    public final static Map<String,String> motivoRifiutoKeys = Arrays.asList(MotivoRifiutoType.values())
            .stream()
            .collect(Collectors.toMap(
                    MotivoRifiutoType::label,
                    MotivoRifiutoType::label,
                    (oldValue, newValue) -> oldValue,
                    Hashtable::new
            ));

    private String message;
    private Timestamp dataRicezione;
    private Timestamp dataLimite;
    public RifiutaFatturaBulk() {
        super();
    }

    public RifiutaFatturaBulk(Timestamp dataRicezione, Timestamp dataLimite) {
        this.dataRicezione = dataRicezione;
        this.dataLimite = dataLimite;
    }

    public boolean isMotivoLibero() {
        return this.dataRicezione.before(dataLimite);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum MotivoRifiutoType {
        A("fattura riferita ad una operazione che non è stata posta in essere in favore del soggetto destinatario della trasmissione del documento"),
        B("omessa o errata indicazione del Codice identificativo di Gara (CIG) o del Codice unico di Progetto (CUP), da riportare in fattura nell’apposito campo previsto nel tracciato XML"),
        C("omessa o errata indicazione del codice di repertorio per i dispositivi medici e per i farmaci"),
        D("omessa o errata indicazione del codice di Autorizzazione all’immissione in commercio (AIC) e del corrispondente quantitativo da riportare in fattura per i farmaci"),
        E("omessa o errata indicazione del numero e data della Determinazione Dirigenziale d’impegno di spesa per le fatture emesse nei confronti delle Regioni e degli enti locali");

        private final String label;

        MotivoRifiutoType(String label) {
            this.label = label;
        }

        public String label() {
            return label;
        }
    }
}
