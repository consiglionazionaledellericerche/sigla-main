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

package it.cnr.contab.doccont00.core.bulk;

public class Mandato_terzoIBulk extends Mandato_terzoBulk {
    MandatoIBulk mandatoI;

    public Mandato_terzoIBulk() {
        super();
    }

    public Mandato_terzoIBulk(String cd_cds, Integer cd_terzo, Integer esercizio, Long pg_mandato) {
        super(cd_cds, cd_terzo, esercizio, pg_mandato);
    }

    /**
     * @return it.cnr.contab.doccont00.core.bulk.MandatoIBulk
     */
    public MandatoBulk getMandato() {
        return mandatoI;
    }

    /**
     * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoIBulk
     */
    public void setMandato(MandatoBulk newMandato) {
        setMandatoI((MandatoIBulk) newMandato);
    }

    /**
     * @return it.cnr.contab.doccont00.core.bulk.MandatoIBulk
     */
    public MandatoIBulk getMandatoI() {
        return mandatoI;
    }

    /**
     * @param newMandatoI it.cnr.contab.doccont00.core.bulk.MandatoIBulk
     */
    public void setMandatoI(MandatoIBulk newMandatoI) {
        mandatoI = newMandatoI;
    }
}
