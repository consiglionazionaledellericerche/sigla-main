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

package it.cnr.contab.spring.service;

import it.cnr.contab.spring.ldap.Person;
import it.cnr.contab.spring.ldap.PersonRepository;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.util.ejb.EJBCommonServices;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.util.Optional;

@Service
@Profile("ldap-master")
public class LDAPService implements InitializingBean {
    @Autowired
    PersonRepository personRepository;
    private GestioneLoginComponentSession gestioneLoginComponent;

    public void setGestioneLoginComponent(
            GestioneLoginComponentSession gestioneLoginComponent) {
        this.gestioneLoginComponent = gestioneLoginComponent;
    }

    public Optional<Person> findPersonById(String uid) {
        return personRepository
                .findByUid(uid)
                .stream()
                .findAny();
    }

    public void save(Person person) {
        personRepository.save(person);
    }

    public String getLdapUserFromMatricola(UserContext userContext, Integer matricola) throws ComponentException, RemoteException {
        return personRepository
                .findByMatricola(String.valueOf(matricola))
                .stream()
                .map(Person::getUid)
                .findAny()
                .orElseThrow(() -> new ComponentException("User not found for matricola:" + matricola));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.gestioneLoginComponent = Optional.ofNullable(EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession"))
                .filter(GestioneLoginComponentSession.class::isInstance)
                .map(GestioneLoginComponentSession.class::cast)
                .orElseThrow(() -> new DetailedRuntimeException("cannot find ejb CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession"));
    }
}
