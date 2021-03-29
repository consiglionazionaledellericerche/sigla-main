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

package it.cnr.test.oracle.doccont.comp;

import it.cnr.contab.doccont00.core.bulk.MandatoComunicaDatiBulk;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.test.oracle.DeploymentsOracle;
import it.cnr.test.util.TestUserContext;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.junit.Test;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ComunicaDatiPagamentiTest extends DeploymentsOracle {

    @EJB
    CRUDComponentSession crudComponentSession;

    @Test
    @OperateOnDeployment(TEST_ORACLE)
    public void test() throws Exception {
        final TestUserContext testUserContext = new TestUserContext();
        final MandatoComunicaDatiBulk mandatoComunicaDatiBulk = new MandatoComunicaDatiBulk();
        mandatoComunicaDatiBulk.setEsercizio(2020);
        mandatoComunicaDatiBulk.setCd_cds("035");
        mandatoComunicaDatiBulk.setPg_mandato(new Long(3146));

        List<MandatoComunicaDatiBulk> dati =
                crudComponentSession.find(testUserContext, MandatoComunicaDatiBulk.class, "recuperoDati", testUserContext, mandatoComunicaDatiBulk, null, null);
        assertNotNull(dati);
        assertTrue(!dati.isEmpty());
    }
}