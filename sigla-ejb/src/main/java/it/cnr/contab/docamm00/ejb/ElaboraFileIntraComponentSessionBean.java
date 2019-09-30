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

package it.cnr.contab.docamm00.ejb;
import it.cnr.contab.docamm00.comp.ElaboraFileIntraComponent;
import it.cnr.contab.docamm00.docs.bulk.VIntrastatBulk;
import it.cnr.contab.docamm00.docs.bulk.VSpesometroNewBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v2.DatiFatturaType;

import java.rmi.RemoteException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBElement;


@Stateless(name="CNRDOCAMM00_EJB_ElaboraFileIntraComponentSession")
public class ElaboraFileIntraComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ElaboraFileIntraComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.docamm00.comp.ElaboraFileIntraComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new ElaboraFileIntraComponentSessionBean();
	}
	public List EstraiLista(UserContext param0, OggettoBulk param1)
			throws ComponentException, PersistencyException,
			IntrospectionException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			List result = ((ElaboraFileIntraComponent)componentObj).EstraiLista(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public List SezioneDueAcquisti(UserContext param0, OggettoBulk param1)
			throws ComponentException, PersistencyException,
			IntrospectionException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			List result = ((ElaboraFileIntraComponent)componentObj).SezioneDueAcquisti(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public List SezioneQuattroAcquisti(UserContext param0, OggettoBulk param1)
	throws ComponentException, PersistencyException,
	IntrospectionException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			List result = ((ElaboraFileIntraComponent)componentObj).SezioneQuattroAcquisti(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public List SezioneDueVendite(UserContext param0, OggettoBulk param1)
			throws ComponentException, PersistencyException,
			IntrospectionException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			List result = ((ElaboraFileIntraComponent)componentObj).SezioneDueVendite(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public List SezioneQuattroVendite(UserContext param0, OggettoBulk param1)
			throws ComponentException, PersistencyException,
			IntrospectionException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			List result = ((ElaboraFileIntraComponent)componentObj).SezioneQuattroVendite(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public List SezioneTreAcquisti(UserContext param0, OggettoBulk param1)
			throws ComponentException, PersistencyException,
			IntrospectionException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			List result = ((ElaboraFileIntraComponent)componentObj).SezioneTreAcquisti(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public List SezioneTreVendite(UserContext param0, OggettoBulk param1)
			throws ComponentException, PersistencyException,
			IntrospectionException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			List result = ((ElaboraFileIntraComponent)componentObj).SezioneTreVendite(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public List SezioneUnoAcquisti(UserContext param0, OggettoBulk param1)
			throws ComponentException, PersistencyException,
			IntrospectionException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			List result = ((ElaboraFileIntraComponent)componentObj).SezioneUnoAcquisti(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public List SezioneUnoVendite(UserContext param0, OggettoBulk param1)
			throws ComponentException, PersistencyException,
			IntrospectionException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			List result = ((ElaboraFileIntraComponent)componentObj).SezioneUnoVendite(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
public List EstraiListaIntra12(UserContext param0, OggettoBulk param1)
throws ComponentException, PersistencyException,
IntrospectionException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		List result = ((ElaboraFileIntraComponent)componentObj).EstraiListaIntra12(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public void confermaElaborazione(UserContext param0,VIntrastatBulk bulk)
		throws ComponentException, PersistencyException,
		IntrospectionException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		 ((ElaboraFileIntraComponent)componentObj).confermaElaborazione(param0,bulk);
		component_invocation_succes(param0,componentObj);
	
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public java.util.Date recuperoMaxDtPagamentoLiq(UserContext param0, OggettoBulk bulk)
throws ComponentException, IntrospectionException, RemoteException {
pre_component_invocation(param0,componentObj);
try {
	java.util.Date result=((ElaboraFileIntraComponent)componentObj).recuperoMaxDtPagamentoLiq(param0, bulk);
	component_invocation_succes(param0,componentObj);
	return result;
} catch(it.cnr.jada.comp.ComponentException e) {
component_invocation_failure(param0,componentObj);
throw e;
} catch(RuntimeException e) {
throw uncaughtRuntimeException(param0,componentObj,e);
} catch(Error e) {
throw uncaughtError(param0,componentObj,e);
}
}
public List EstraiBlacklist(UserContext param0, OggettoBulk param1,OggettoBulk param2)		
	throws ComponentException, 
	IntrospectionException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			List result = ((ElaboraFileIntraComponent)componentObj).EstraiBlacklist(param0,param1,param2);
			component_invocation_succes(param0,componentObj);
			return result;
		
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
@Override
public JAXBElement<DatiFatturaType> creaDatiFatturaType(UserContext param0,
		VSpesometroNewBulk param1) throws ComponentException,
		IntrospectionException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		JAXBElement<DatiFatturaType> result = ((ElaboraFileIntraComponent)componentObj).creaDatiFatturaType(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}	
}
