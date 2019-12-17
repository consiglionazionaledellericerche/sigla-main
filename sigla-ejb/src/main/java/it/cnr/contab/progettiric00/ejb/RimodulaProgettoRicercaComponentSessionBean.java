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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import it.cnr.contab.progettiric00.comp.RimodulaProgettoRicercaComponent;
import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.jada.bulk.OggettoBulk;
@Stateless(name="CNRPROGETTIRIC00_EJB_RimodulaProgettoRicercaComponentSession")
public class RimodulaProgettoRicercaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements RimodulaProgettoRicercaComponentSession{
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.progettiric00.comp.RimodulaProgettoRicercaComponent();
	}

	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws EJBException {
		return new RimodulaProgettoRicercaComponentSessionBean();
	}

	public Progetto_rimodulazioneBulk salvaDefinitivo(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			Progetto_rimodulazioneBulk result = ((RimodulaProgettoRicercaComponent)componentObj).salvaDefinitivo(param0,param1);
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
	public Progetto_rimodulazioneBulk approva(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1,OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			Progetto_rimodulazioneBulk result = ((RimodulaProgettoRicercaComponent)componentObj).approva(param0,param1,param2);
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
	public Progetto_rimodulazioneBulk respingi(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			Progetto_rimodulazioneBulk result = ((RimodulaProgettoRicercaComponent)componentObj).respingi(param0,param1);
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
	public Progetto_rimodulazioneBulk valida(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			Progetto_rimodulazioneBulk result = ((RimodulaProgettoRicercaComponent)componentObj).valida(param0,param1);
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
	public List<OggettoBulk> constructVariazioniBilancio(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			List<OggettoBulk> result = ((RimodulaProgettoRicercaComponent)componentObj).constructVariazioniBilancio(param0,param1);
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
	public Progetto_rimodulazioneBulk rebuildRimodulazione(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			Progetto_rimodulazioneBulk result = ((RimodulaProgettoRicercaComponent)componentObj).rebuildRimodulazione(param0,param1);
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
	public Progetto_rimodulazioneBulk riportaDefinitivo(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			Progetto_rimodulazioneBulk result = ((RimodulaProgettoRicercaComponent)componentObj).riportaDefinitivo(param0,param1);
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
}