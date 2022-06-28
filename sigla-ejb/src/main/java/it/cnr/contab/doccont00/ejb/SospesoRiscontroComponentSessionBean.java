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

package it.cnr.contab.doccont00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.docamm00.comp.FatturaAttivaSingolaComponent;
import it.cnr.contab.doccont00.comp.SospesoRiscontroComponent;
import it.cnr.contab.doccont00.intcass.giornaliera.MovimentoContoEvidenzaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.NoRollbackException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
@Stateless(name="CNRDOCCONT00_EJB_SospesoRiscontroComponentSession")
public class SospesoRiscontroComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements SospesoRiscontroComponentSession{
@PostConstruct
	public void ejbCreate() {
	componentObj = new SospesoRiscontroComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new SospesoRiscontroComponentSessionBean();
}
public void cambiaStato(UserContext param0, java.util.Collection param1, String param2, String param3) throws ComponentException, EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((SospesoRiscontroComponent)componentObj).cambiaStato(param0,param1,param2,param3);
		component_invocation_succes(param0,componentObj);
	} catch(NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public OggettoBulk inizializzaBulkPerStampa(UserContext param0, OggettoBulk param1) throws ComponentException, EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		OggettoBulk result = ((SospesoRiscontroComponent)componentObj).inizializzaBulkPerStampa(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public OggettoBulk stampaConBulk(UserContext param0, OggettoBulk param1) throws ComponentException, EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		OggettoBulk result = ((SospesoRiscontroComponent)componentObj).stampaConBulk(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public RemoteIterator cercaSospesiPerStato(UserContext usercontext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk, String statoForSearch) throws ComponentException, EJBException{
    pre_component_invocation(usercontext, componentObj);
    try{
        RemoteIterator remoteiterator = ((SospesoRiscontroComponent)componentObj).cercaSospesiPerStato(usercontext, compoundfindclause, oggettobulk, statoForSearch);
        component_invocation_succes(usercontext, componentObj);
        return remoteiterator;
    }catch(NoRollbackException norollbackexception){
        component_invocation_succes(usercontext, componentObj);
        throw norollbackexception;
    }catch(ComponentException componentexception){
        component_invocation_failure(usercontext, componentObj);
        throw componentexception;
    }catch(RuntimeException runtimeexception){
        throw uncaughtRuntimeException(usercontext, componentObj, runtimeexception);
    }catch(Error error){
        throw uncaughtError(usercontext, componentObj, error);
    }
}
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Integer caricamentoRigaGiornaleCassa(UserContext param0, boolean tesoreriaUnica, EnteBulk cdsEnte, MovimentoContoEvidenzaBulk riga) throws ComponentException, EJBException, it.cnr.jada.persistency.PersistencyException {
		pre_component_invocation(param0, componentObj);
		try {
			Integer caricato = ((SospesoRiscontroComponent) componentObj).caricamentoRigaGiornaleCassa(param0, tesoreriaUnica, cdsEnte, riga);
			component_invocation_succes(param0, componentObj);
			return caricato;
		} catch (NoRollbackException e) {
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

}
