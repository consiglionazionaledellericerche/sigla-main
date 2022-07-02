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

package it.cnr.contab.config00.ejb;

import it.cnr.contab.config00.comp.Unita_organizzativaComponent;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.jada.UserContext;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name = "CNRCONFIG00_EJB_Unita_organizzativaComponentSession")
public class Unita_organizzativaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements Unita_organizzativaComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
        return new Unita_organizzativaComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new it.cnr.contab.config00.comp.Unita_organizzativaComponent();
    }

    public it.cnr.jada.bulk.OggettoBulk findUOByCodice(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.bulk.OggettoBulk result = ((Unita_organizzativaComponent) componentObj).findUOByCodice(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public it.cnr.jada.bulk.OggettoBulk getUoEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.bulk.OggettoBulk result = ((Unita_organizzativaComponent) componentObj).getUoEnte(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.lang.String getIndirizzoUnitaOrganizzativa(it.cnr.jada.UserContext param0, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1, boolean param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.lang.String result = ((Unita_organizzativaComponent) componentObj).getIndirizzoUnitaOrganizzativa(param0, param1, param2);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.util.List findListaUOWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3, String param4) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.List result = ((Unita_organizzativaComponent) componentObj).findListaUOWS(param0, param1, param2, param3, param4);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.util.List<CdsBulk> findListaCds(it.cnr.jada.UserContext param0, int param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.List<CdsBulk> result = ((Unita_organizzativaComponent) componentObj).findListaCds(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }
}
