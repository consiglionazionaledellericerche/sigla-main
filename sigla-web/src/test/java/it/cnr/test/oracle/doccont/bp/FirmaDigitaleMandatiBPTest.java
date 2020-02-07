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

package it.cnr.test.oracle.doccont.bp;

import it.cnr.contab.doccont00.bp.FirmaDigitaleMandatiBP;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.intcass.bulk.StatoTrasmissione;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.si.spring.storage.StorageDriver;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.StoreService;
import it.cnr.test.oracle.DeploymentsOracle;
import it.cnr.test.util.MockActionContext;
import it.cnr.test.util.TestUserContext;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.junit.Test;

import javax.ejb.EJB;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FirmaDigitaleMandatiBPTest extends DeploymentsOracle {
    @EJB
    private CRUDComponentSession crudComponentSession;

    @Test
    @OperateOnDeployment(TEST_ORACLE)
    public void testPredisponiMandato() throws Exception {
        final TestUserContext testUserContext = new TestUserContext();
        V_mandato_reversaleBulk v_mandato_reversaleBulk =
                Optional.ofNullable(
                        crudComponentSession.findByPrimaryKey(testUserContext,
                                new V_mandato_reversaleBulk(2019, "MAN", "000", new Long(1)))
                )
                        .filter(V_mandato_reversaleBulk.class::isInstance)
                        .map(V_mandato_reversaleBulk.class::cast)
                        .orElse(null);
        FirmaDigitaleMandatiBP firmaDigitaleMandatiBP = new FirmaDigitaleMandatiBP();
        firmaDigitaleMandatiBP.predisponi(new MockActionContext(new TestUserContext()), v_mandato_reversaleBulk, new SimpleDateFormat("yyyy/MM/dd"));

        StoreService storeService = SpringUtil.getBean("storeService", StoreService.class);
        final StorageObject storageObjectByPath = storeService.getStorageObjectByPath(
                v_mandato_reversaleBulk.getStorePath().concat(StorageDriver.SUFFIX).concat(v_mandato_reversaleBulk.getCMISName())
        );
        assertNotNull(storageObjectByPath);
        assertEquals(MandatoBulk.STATO_TRASMISSIONE_PREDISPOSTO, Optional.ofNullable(
                        crudComponentSession.findByPrimaryKey(testUserContext,v_mandato_reversaleBulk))
                        .filter(V_mandato_reversaleBulk.class::isInstance)
                        .map(V_mandato_reversaleBulk.class::cast)
                        .orElse(null).getStato_trasmissione());

    }
}
