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

package it.cnr.contab.progettiric00.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

/**
 * Bean implementation class for Enterprise Bean: CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession
 */
@Stateless(name = "CNRPROGETTIRIC00_EJB_ProgettoRicercaPadreComponentSession")
public class ProgettoRicercaPadreComponentSessionBean extends it.cnr.contab.progettiric00.ejb.ProgettoRicercaComponentSessionBean implements ProgettoRicercaPadreComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
        return new ProgettoRicercaComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new it.cnr.contab.progettiric00.comp.ProgettoRicercaPadreComponent();
    }

    public void aggiornaGECO(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((it.cnr.contab.progettiric00.comp.ProgettoRicercaPadreComponent) componentObj).aggiornaGECO(param0);
            component_invocation_succes(param0, componentObj);
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

	public void aggiornaGECODipartimenti(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			((it.cnr.contab.progettiric00.comp.ProgettoRicercaPadreComponent) componentObj).aggiornaGECODipartimenti(param0);
			component_invocation_succes(param0, componentObj);
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

	public void cancellaProgettoSIP(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			((it.cnr.contab.progettiric00.comp.ProgettoRicercaPadreComponent) componentObj).cancellaProgettoSIP(param0);
			component_invocation_succes(param0, componentObj);
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
