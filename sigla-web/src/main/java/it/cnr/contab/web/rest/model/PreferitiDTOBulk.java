/*
 * Copyright (C) 2022  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.web.rest.model;

import java.util.Objects;

public class PreferitiDTOBulk {
    private final String cdNodo;
    private final String descrizione;

    public PreferitiDTOBulk(String cdNodo, String descrizione) {
        this.cdNodo = cdNodo;
        this.descrizione = descrizione;
    }

    public String getCdNodo() {
        return cdNodo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PreferitiDTOBulk that = (PreferitiDTOBulk) o;
        return cdNodo.equals(that.cdNodo) && descrizione.equals(that.descrizione);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cdNodo, descrizione);
    }
}
