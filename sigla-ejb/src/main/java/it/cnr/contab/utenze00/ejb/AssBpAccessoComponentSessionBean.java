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

package it.cnr.contab.utenze00.ejb;

import it.cnr.contab.utenze00.comp.AssBpAccessoComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.util.Map;

@Stateless(name = "CNRUTENZE00_EJB_AssBpAccessoComponentSession")
public class AssBpAccessoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AssBpAccessoComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
        return new AssBpAccessoComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new AssBpAccessoComponent();
    }

    public java.util.List findAccessoByBP(it.cnr.jada.UserContext param0, String param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.List result = ((AssBpAccessoComponent) componentObj).findAccessoByBP(param0, param1);
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

    public it.cnr.contab.utenze00.bulk.AssBpAccessoBulk finAssBpAccesso(it.cnr.jada.UserContext param0, String param1, String param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.utenze00.bulk.AssBpAccessoBulk result = ((AssBpAccessoComponent) componentObj).finAssBpAccesso(param0, param1, param2);
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

    @Override
    public Map<String, String> findDescrizioneBP(UserContext param0) throws ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            Map<String, String> result = ((AssBpAccessoComponent) componentObj).findDescrizioneBP(param0);
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
