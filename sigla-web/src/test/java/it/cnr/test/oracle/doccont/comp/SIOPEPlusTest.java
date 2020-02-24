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

import it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.test.oracle.DeploymentsOracle;
import it.cnr.test.util.TestUserContext;
import it.siopeplus.Mandato;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.junit.Test;

import javax.ejb.EJB;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SIOPEPlusTest extends DeploymentsOracle {
    @EJB
    private CRUDComponentSession crudComponentSession;
    @EJB
    private DistintaCassiereComponentSession distintaCassiereComponentSession;

    @Test
    @OperateOnDeployment(TEST_ORACLE)
    public void testMandatoFlusso() throws Exception {
        final TestUserContext testUserContext = new TestUserContext();
        V_mandato_reversaleBulk v_mandato_reversaleBulk =
                Optional.ofNullable(
                        crudComponentSession.findByPrimaryKey(testUserContext, new V_mandato_reversaleBulk(2019, "MAN", "000", new Long(1)))
                )
                        .filter(V_mandato_reversaleBulk.class::isInstance)
                        .map(V_mandato_reversaleBulk.class::cast)
                        .orElse(null);
        final Mandato mandato = distintaCassiereComponentSession.creaMandatoFlussoSiopeplus(testUserContext, v_mandato_reversaleBulk);
        assertEquals(BigDecimal.valueOf(2259.24), mandato.getImportoMandato());
    }
}
