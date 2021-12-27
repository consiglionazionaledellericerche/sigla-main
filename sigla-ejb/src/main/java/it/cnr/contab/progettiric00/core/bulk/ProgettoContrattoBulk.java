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

package it.cnr.contab.progettiric00.core.bulk;

public class ProgettoContrattoBulk extends ProgettoGestBulk {

    private String cd_cup;

    public ProgettoContrattoBulk() {
        super();
    }

    public ProgettoContrattoBulk(Integer esercizio, Integer pg_progetto, String tipo_fase) {
        super(esercizio, pg_progetto, tipo_fase);
    }

    public String getCd_cup() {
        return cd_cup;
    }

    public void setCd_cup(String cd_cup) {
        this.cd_cup = cd_cup;
    }
}