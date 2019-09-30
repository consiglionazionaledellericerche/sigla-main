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

package it.cnr.contab.missioni00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import it.cnr.contab.missioni00.comp.AnticipoComponent;
@Stateless(name="CNRMISSIONI00_EJB_AnticipoComponentSession")
public class AnticipoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AnticipoComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.missioni00.comp.AnticipoComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}
	
	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AnticipoComponentSessionBean();
	}
	public it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((AnticipoComponent)componentObj).cercaObbligazioni(param0,param1);
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
	public it.cnr.contab.missioni00.docs.bulk.AnticipoBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.missioni00.docs.bulk.AnticipoBulk result = ((AnticipoComponent)componentObj).completaTerzo(param0,param1,param2);
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
	public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((AnticipoComponent)componentObj).creaConBulk(param0,param1,param2);
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
	public it.cnr.contab.missioni00.docs.bulk.AnticipoBulk creaRimborsoCompleto(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.missioni00.docs.bulk.AnticipoBulk result = ((AnticipoComponent)componentObj).creaRimborsoCompleto(param0,param1);
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
	public java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.util.Collection result = ((AnticipoComponent)componentObj).findListabanche(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public it.cnr.contab.missioni00.docs.bulk.AnticipoBulk gestisciCambioDataRegistrazione(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.sql.SQLException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.missioni00.docs.bulk.AnticipoBulk result = ((AnticipoComponent)componentObj).gestisciCambioDataRegistrazione(param0,param1);
			component_invocation_succes(param0,componentObj);
			return result;
		} catch(it.cnr.jada.comp.NoRollbackException e) {
			component_invocation_succes(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.comp.ComponentException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(it.cnr.jada.persistency.PersistencyException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(java.sql.SQLException e) {
			component_invocation_failure(param0,componentObj);
			throw e;
		} catch(RuntimeException e) {
			throw uncaughtRuntimeException(param0,componentObj,e);
		} catch(Error e) {
			throw uncaughtError(param0,componentObj,e);
		}
	}
	public boolean isAnticipoAnnullato(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnticipoComponent)componentObj).isAnticipoAnnullato(param0,param1);
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
	public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((AnticipoComponent)componentObj).modificaConBulk(param0,param1,param2);
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
	public it.cnr.contab.missioni00.docs.bulk.AnticipoBulk riportaRimborsoAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.missioni00.docs.bulk.AnticipoBulk result = ((AnticipoComponent)componentObj).riportaRimborsoAvanti(param0,param1);
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
	public it.cnr.contab.missioni00.docs.bulk.AnticipoBulk riportaRimborsoIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.missioni00.docs.bulk.AnticipoBulk result = ((AnticipoComponent)componentObj).riportaRimborsoIndietro(param0,param1);
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
	public void rollbackToSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((AnticipoComponent)componentObj).rollbackToSavePoint(param0,param1);
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
	public void setSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((AnticipoComponent)componentObj).setSavePoint(param0,param1);
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
	public void validaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1,it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((AnticipoComponent)componentObj).validaObbligazione(param0,param1,param2);
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
