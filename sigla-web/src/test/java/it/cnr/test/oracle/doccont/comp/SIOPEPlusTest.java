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

import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.ejb.DistintaCassiereComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.enumeration.EsitoOperazione;
import it.cnr.contab.web.rest.exception.RestException;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.BulkLoaderIterator;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.test.oracle.DeploymentsOracle;
import it.siopeplus.Mandato;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class SIOPEPlusTest extends DeploymentsOracle {
    private static final Logger logger = LoggerFactory.getLogger(SIOPEPlusTest.class);
    @EJB
    private CRUDComponentSession crudComponentSession;
    @EJB
    private DistintaCassiereComponentSession distintaCassiereComponentSession;

    @Test
    @OperateOnDeployment(TEST_ORACLE)
    public void testMandatoFlusso() throws Exception {
        final CNRUserContext testUserContext = new CNRUserContext();
        final CompoundFindClause compoundFindClause = new CompoundFindClause();
        compoundFindClause.addClause(FindClause.AND, "esercizio", SQLBuilder.GREATER_EQUALS, LocalDate.now().getYear() - 1);
        compoundFindClause.addClause(FindClause.AND, "cd_tipo_documento_cont", SQLBuilder.EQUALS, Numerazione_doc_contBulk.TIPO_MAN);
        compoundFindClause.addClause(FindClause.AND, "esitoOperazione", SQLBuilder.EQUALS, EsitoOperazione.ACQUISITO.value());
        compoundFindClause.addClause(FindClause.AND, "pg_documento_cont", SQLBuilder.LESS_EQUALS, 10000);

        BulkLoaderIterator remoteIterator =
                Optional.ofNullable(crudComponentSession.cerca(
                                testUserContext,
                                compoundFindClause,
                                new V_mandato_reversaleBulk()))
                        .filter(BulkLoaderIterator.class::isInstance)
                        .map(BulkLoaderIterator.class::cast)
                        .orElseThrow(() -> new RestException(Response.Status.INTERNAL_SERVER_ERROR, "Cannot create remote iterator"));
        Optional.ofNullable(remoteIterator)
                .ifPresent(iterator -> {
                    try {
                        iterator.open(testUserContext);
                        while (iterator.hasMoreElements()) {
                            try {
                                final V_mandato_reversaleBulk bulk = Optional.ofNullable(iterator.nextElement())
                                        .filter(V_mandato_reversaleBulk.class::isInstance)
                                        .map(V_mandato_reversaleBulk.class::cast)
                                        .orElseThrow(() -> new DetailedRuntimeException("Cannot find element"));
                                logger.info("Mandato {}/{}/{}/{}", bulk.getEsercizio(), bulk.getCd_cds(), bulk.getCd_tipo_documento_cont(), bulk.getPg_documento_cont());
                                final Mandato mandato = distintaCassiereComponentSession.creaMandatoFlussoSiopeplus(testUserContext, bulk);
                                assertEquals(
                                        bulk.getIm_documento_cont().setScale(2, RoundingMode.HALF_UP),
                                        BigDecimal.valueOf(mandato.getInformazioniBeneficiario()
                                                        .stream()
                                                        .collect(Collectors.summingDouble(value -> value.getImportoBeneficiario().doubleValue())))
                                                .setScale(2, RoundingMode.HALF_UP)
                                );
                                assertEquals(
                                        bulk.getIm_documento_cont().setScale(2, RoundingMode.HALF_UP),
                                        mandato.getImportoMandato().setScale(2, RoundingMode.HALF_UP)
                                );
                            } catch (ComponentException | RemoteException e) {
                                if (e.getMessage().contains("CIG")) {
                                    logger.error(e.getMessage());
                                } else {
                                    throw new DetailedRuntimeException(e);
                                }
                            }
                        }
                    } catch (ComponentException | RemoteException e) {
                        throw new DetailedRuntimeException(e);
                    } finally {
                        iterator.ejbRemove();
                    }
                });
    }
}
