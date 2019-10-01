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

package it.cnr.contab.utenze.comp;

import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.Deployments;
import it.cnr.contab.util.TestUserContext;
import it.cnr.jada.ejb.CRUDComponentSession;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class LoginComponentTest extends Deployments {
    @EJB
    private CRUDComponentSession crudComponentSession;
    @EJB
    private GestioneLoginComponentSession loginComponentSession;

    @Test
    @OperateOnDeployment(TEST_H2)
    @InSequence(1)
    public void testEsercizio() throws Exception {
        UtenteBulk utente = Optional.ofNullable(crudComponentSession.findByPrimaryKey(new TestUserContext(), new UtenteBulk("TEST")))
                .filter(UtenteBulk.class::isInstance)
                .map(UtenteBulk.class::cast)
                .orElse(null);
        assertEquals(true, Optional.ofNullable(utente).isPresent());
        assertEquals("Utenza di TEST", utente.getDs_utente());

        java.lang.Integer[] listaEsercizio = loginComponentSession.listaEserciziPerUtente(new TestUserContext(), utente);
        java.lang.Integer[] espected = {
                Integer.valueOf(System.getProperty("liquibase.bootstrap.esercizio"))
        };
        assertArrayEquals(espected, listaEsercizio);
    }
}
