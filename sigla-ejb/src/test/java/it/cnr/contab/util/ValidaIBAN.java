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

package it.cnr.contab.util;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.jada.bulk.ValidationException;
import org.junit.Test;

public class ValidaIBAN {

    @Test
    public void verificaIBAN() {
        BancaBulk bancaBulk = new BancaBulk();
        NazioneBulk nazioneBulk = new NazioneBulk();
        nazioneBulk.setCd_nazione("IT");
        nazioneBulk.setCd_iso("IT");
        nazioneBulk.setStruttura_iban("NN;A;NNNNN;NNNNN;CCCCCCCCCCCC");
        bancaBulk.setNazione_iban(nazioneBulk);
        bancaBulk.setCodice_iban_parte1("10");
        bancaBulk.setCodice_iban_parte2("H");
        bancaBulk.setCodice_iban_parte3("03062");
        bancaBulk.setCodice_iban_parte4("34210");
        bancaBulk.setCodice_iban_parte5("000000192294");

        bancaBulk.setCodice_iban("IT10H0306234210000000192294");
        try {
            bancaBulk.validaIban();
        } catch (ValidationException e) {
            e.printStackTrace();
        }

    }

}
