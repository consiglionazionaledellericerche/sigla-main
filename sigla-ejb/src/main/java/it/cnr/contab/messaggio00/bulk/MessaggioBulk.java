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

package it.cnr.contab.messaggio00.bulk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.cnr.contab.web.rest.model.ProgressivoVersioneDTO;

import java.sql.Timestamp;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessaggioBulk extends MessaggioBase implements ProgressivoVersioneDTO {

    public static final String ICONA_MESSAGGIO_VISIONATO = "<img align=middle class=Button src=img/book_opened.gif>";
    public static final String ICONA_MESSAGGIO_NON_VISIONATO = "<img align=middle class=Button src=img/book_closed.gif>";
    private Boolean visionato = Boolean.FALSE;
    private Boolean letto = Boolean.FALSE;
    private String iconaOpenClose;

    public MessaggioBulk() {
        super();
    }

    public MessaggioBulk(java.lang.Long pg_messaggio) {
        super(pg_messaggio);
    }

    public String getIconaOpenClose() {
        if (getVisionato().booleanValue())
            return ICONA_MESSAGGIO_VISIONATO;
        else
            return ICONA_MESSAGGIO_NON_VISIONATO;
    }

    public Timestamp getData_creazione() {
        return getDacr();
    }

    public Boolean getVisionato() {
        return visionato;
    }

    public void setVisionato(Boolean boolean1) {
        visionato = boolean1;
    }

    public Boolean getLetto() {
        return letto;
    }

    public void setLetto(Boolean boolean1) {
        letto = boolean1;
    }

    @Override
    public Long getPgVerRec() {
        return getPg_ver_rec();
    }

    @Override
    public void setPgVerRec(Long l) {
        setPg_ver_rec(l);
    }
}
