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

package it.cnr.contab.pagopa.ejb;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.contab.pagopa.comp.PendenzaPagopaComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Bean implementation class for Enterprise Bean: CNRBOLLO00_EJB_TipoAttoBolloComponentSession
 */
@Stateless(name="CNRPAGOPA_EJB_ScadenzaPagopaComponentSession")
public class PendenzaPagopaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements PendenzaPagopaComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new PendenzaPagopaComponent();
	}
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new PendenzaPagopaComponentSessionBean();
	}

    public PendenzaPagopaBulk generaPosizioneDebitoria(UserContext userContext, IDocumentoAmministrativoBulk documentoAmministrativoBulk, Timestamp dataScadenza, String descrizione, BigDecimal importoScadenza, TerzoBulk terzoBulk) throws ComponentException, javax.ejb.EJBException{
        pre_component_invocation(userContext, componentObj);
        try {
            PendenzaPagopaBulk result = ((PendenzaPagopaComponent)componentObj).generaPosizioneDebitoria(userContext, documentoAmministrativoBulk, dataScadenza, descrizione, importoScadenza, terzoBulk);
            component_invocation_succes(userContext, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
        }
    }


}
