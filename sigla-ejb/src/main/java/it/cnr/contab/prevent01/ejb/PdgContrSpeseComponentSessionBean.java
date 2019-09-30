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

package it.cnr.contab.prevent01.ejb;
import java.rmi.RemoteException;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.doccont00.comp.AccertamentoComponent;
import it.cnr.contab.prevent01.bulk.Contrattazione_speseVirtualBulk;
import it.cnr.contab.prevent01.bulk.Pdg_approvato_dip_areaBulk;
import it.cnr.contab.prevent01.comp.PdgAggregatoModuloComponent;
import it.cnr.contab.prevent01.comp.PdgContrSpeseComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.NoRollbackException;
import it.cnr.jada.persistency.PersistencyException;

import javax.annotation.PostConstruct;
import javax.ejb.*;
@Stateless(name="CNRPREVENT01_EJB_PdgContrSpeseComponentSession")
public class PdgContrSpeseComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements PdgContrSpeseComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.prevent01.comp.PdgContrSpeseComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
	return new PdgContrSpeseComponentSessionBean();
}
public it.cnr.jada.bulk.OggettoBulk inizializzaDettagliBulkPerModifica(it.cnr.jada.UserContext param0, Contrattazione_speseVirtualBulk param1, Pdg_approvato_dip_areaBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((PdgContrSpeseComponent)componentObj).inizializzaDettagliBulkPerModifica(param0,param1,param2);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public boolean isApprovatoDefinitivo(it.cnr.jada.UserContext param0) throws ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		boolean result = ((PdgContrSpeseComponent)componentObj).isApprovatoDefinitivo(param0);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public CdrBulk caricaCdrAfferenzaDaUo(UserContext param0, Unita_organizzativaBulk param1) throws ComponentException, RemoteException, PersistencyException {
	pre_component_invocation(param0,componentObj);
	try {
		CdrBulk result = ((PdgContrSpeseComponent)componentObj).caricaCdrAfferenzaDaUo(param0,param1);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public Integer livelloContrattazioneSpese(UserContext param0) throws ComponentException, PersistencyException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		Integer result = ((PdgContrSpeseComponent)componentObj).livelloContrattazioneSpese(param0);
		component_invocation_succes(param0,componentObj);
		return result;
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public void approvaDefinitivamente(it.cnr.jada.UserContext param0) throws ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		((PdgContrSpeseComponent)componentObj).approvaDefinitivamente(param0);
		component_invocation_succes(param0,componentObj);
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
public void undoApprovazioneDefinitiva(it.cnr.jada.UserContext param0) throws ComponentException, RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		((PdgContrSpeseComponent)componentObj).undoApprovazioneDefinitiva(param0);
		component_invocation_succes(param0,componentObj);
	} catch(it.cnr.jada.comp.NoRollbackException e) {
		component_invocation_succes(param0,componentObj);
		throw e;
	} catch(it.cnr.jada.comp.ComponentException e) {
		component_invocation_failure(param0,componentObj);
		throw e;
	} catch(RuntimeException e) {
		throw uncaughtRuntimeException(param0,componentObj,e);
	} catch(Error e) {
		throw uncaughtError(param0,componentObj,e);
	}
}
}