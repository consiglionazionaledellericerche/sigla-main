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

import it.cnr.contab.coepcoan00.comp.ScritturaPartitaDoppiaFromDocumentoComponent;
import it.cnr.contab.coepcoan00.core.bulk.IDocumentoCogeBulk;
import it.cnr.contab.coepcoan00.core.bulk.Scrittura_partita_doppiaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

@Stateless(name = "CNRCOEPCOAN00_EJB_ScritturaPartitaDoppiaFromDocumentoComponentSession")
public class ScritturaPartitaDoppiaFromDocumentoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ScritturaPartitaDoppiaFromDocumentoComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
        return new ScritturaPartitaDoppiaFromDocumentoComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new ScritturaPartitaDoppiaFromDocumentoComponent();
    }

	public void removeScrittura(UserContext param0, Scrittura_partita_doppiaBulk param1) throws ComponentException {
		pre_component_invocation(param0, componentObj);
		try {
			((ScritturaPartitaDoppiaFromDocumentoComponent) componentObj).removeScrittura(param0, param1);
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
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public List<IDocumentoCogeBulk> getAllDocumentiCogeDaContabilizzare(UserContext param0, Integer param1, String param2) throws ComponentException, PersistencyException {
		pre_component_invocation(param0, componentObj);
		try {
			List<IDocumentoCogeBulk>  result = ((ScritturaPartitaDoppiaFromDocumentoComponent) componentObj).getAllDocumentiCogeDaContabilizzare(param0, param1, param2);
			component_invocation_succes(param0, componentObj);
			return result;
		} catch (it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0, componentObj);
			throw e;
		} catch (ComponentException e) {
			component_invocation_failure(param0, componentObj);
			throw e;
		} catch (RuntimeException e) {
			throw uncaughtRuntimeException(param0, componentObj, e);
		} catch (Error e) {
			throw uncaughtError(param0, componentObj, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void loadScritturePatrimoniali(UserContext param0, List<IDocumentoCogeBulk> param1) {
		pre_component_invocation(param0, componentObj);
		try {
			((ScritturaPartitaDoppiaFromDocumentoComponent) componentObj).loadScritturePatrimoniali(param0, param1);
			component_invocation_succes(param0, componentObj);
		} catch (RuntimeException e) {
			throw uncaughtRuntimeException(param0, componentObj, e);
		} catch (Error e) {
			throw uncaughtError(param0, componentObj, e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void loadScritturaPatrimoniale(UserContext param0, IDocumentoCogeBulk param1) throws ComponentException{
		pre_component_invocation(param0, componentObj);
		try {
			((ScritturaPartitaDoppiaFromDocumentoComponent) componentObj).loadScritturaPatrimoniale(param0, param1);
			component_invocation_succes(param0, componentObj);
		} catch (ComponentException e) {
			component_invocation_failure(param0, componentObj);
			throw e;
		} catch (RuntimeException e) {
			throw uncaughtRuntimeException(param0, componentObj, e);
		} catch (Error e) {
			throw uncaughtError(param0, componentObj, e);
		}
	}
}
