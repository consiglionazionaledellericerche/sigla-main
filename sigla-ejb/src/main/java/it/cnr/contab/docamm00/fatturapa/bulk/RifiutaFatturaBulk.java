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
import it.cnr.jada.bulk.ValidationException;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;
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

    private String messageOption;
    private String messageText;

    private Timestamp dataRicezione;
    private Timestamp dataLimite;
    private DocumentoEleTestataBulk documentoEleTestataBulk;
    private String emailPEC;
    private String note;
    private boolean messageOptionSelected = Boolean.FALSE;
    private boolean nota = Boolean.FALSE;

    public RifiutaFatturaBulk() {
        super();
    }

    public RifiutaFatturaBulk(String emailPEC, DocumentoEleTestataBulk documentoEleTestataBulk, boolean nota) {
        this.documentoEleTestataBulk = documentoEleTestataBulk;
        this.emailPEC = emailPEC;
        this.nota = nota;
    }

    public RifiutaFatturaBulk(Timestamp dataRicezione, Timestamp dataLimite) {
        this.dataRicezione = dataRicezione;
        this.dataLimite = dataLimite;
    }

    public boolean isDecorrenzaTermini() {
        return Optional.ofNullable(this.documentoEleTestataBulk)
                        .map(DocumentoEleTestataBulk::isRicevutaDecorrenzaTermini)
                        .orElse(Boolean.FALSE);
    }

    public boolean isMotivoLibero() {
        return this.dataRicezione.before(dataLimite);
    }

    public boolean isMessageOptionSelected() {
        return this.messageOptionSelected;
    }

    public void setMessageOptionSelected(boolean messageOptionSelected) {
        this.messageOptionSelected = messageOptionSelected;
    }

    public String getMessage() {

        return Optional.ofNullable(this.messageText).orElse(this.messageOption);
    }

    public String getMessageOption() {
        return messageOption;
    }

    public void setMessageOption(String messageOption) {
        this.messageOption = messageOption;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getEmailPEC() {
        return emailPEC;
    }

    public void setEmailPEC(String emailPEC) {
        this.emailPEC = emailPEC;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setNota(boolean nota) {
        this.nota = nota;
    }

    public boolean isNota() {
        return nota;
    }

    @Override
    public void validate() throws ValidationException {
        super.validate();
        if (Optional.ofNullable(documentoEleTestataBulk).isPresent()) {
            Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
            if (!p.matcher(Optional.ofNullable(getEmailPEC()).orElse("")).find()) {
                throw new ValidationException("Inserire un indirizzo PEC valido!");
            }
        }
        if (!Optional.ofNullable(getMessage()).filter(s -> s.trim().length() > 0 ).isPresent()) {
            throw new ValidationException("Inserire il Motivo del Rifiuto!");
        }
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
