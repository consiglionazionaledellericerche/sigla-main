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

package it.cnr.contab.coepcoan00.ejb;

import it.cnr.contab.coepcoan00.comp.ScritturaPartitaDoppiaComponent;
import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.rmi.RemoteException;

@Stateless(name = "CNRCOEPCOAN00_EJB_ScritturaPartitaDoppiaComponentSession")
public class ScritturaPartitaDoppiaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ScritturaPartitaDoppiaComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
        return new ScritturaPartitaDoppiaComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new it.cnr.contab.coepcoan00.comp.ScritturaPartitaDoppiaComponent();
    }

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.bulk.OggettoBulk result = ((ScritturaPartitaDoppiaComponent) componentObj).inizializzaBulkPerStampa(param0, param1);
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

    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.bulk.OggettoBulk result = ((ScritturaPartitaDoppiaComponent) componentObj).stampaConBulk(param0, param1);
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

	public Scrittura_partita_doppiaBulk proposeScritturaPartitaDoppia(UserContext param0, IDocumentoCogeBulk param1) throws ComponentException, RemoteException {
		pre_component_invocation(param0, componentObj);
		try {
			Scrittura_partita_doppiaBulk result = ((ScritturaPartitaDoppiaComponent) componentObj).proposeScritturaPartitaDoppia(param0, param1);
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
