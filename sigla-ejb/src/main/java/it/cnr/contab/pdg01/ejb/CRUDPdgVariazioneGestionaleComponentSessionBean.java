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

package it.cnr.contab.pdg01.ejb;

import java.rmi.RemoteException;

import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.comp.CRUDPdgVariazioneGestionaleComponent;
import it.cnr.contab.preventvar00.comp.VarBilancioComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
@Stateless(name="CNRPDG01_EJB_CRUDPdgVariazioneGestionaleComponentSession")
public class CRUDPdgVariazioneGestionaleComponentSessionBean extends it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSessionBean implements CRUDPdgVariazioneGestionaleComponentSession {
@PostConstruct
	public void ejbCreate() {
	componentObj = new CRUDPdgVariazioneGestionaleComponent();
}
public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
	return new CRUDPdgVariazioneGestionaleComponentSessionBean();
}

public void aggiungiDettaglioVariazione(it.cnr.jada.UserContext param0, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1, it.cnr.contab.prevent00.bulk.V_assestatoBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		((CRUDPdgVariazioneGestionaleComponent)componentObj).aggiungiDettaglioVariazione(param0,param1,param2);
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
public it.cnr.jada.bulk.OggettoBulk generaVariazioneBilancio(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((CRUDPdgVariazioneGestionaleComponent)componentObj).generaVariazioneBilancio(param0,param1);
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
public it.cnr.jada.bulk.OggettoBulk esitaVariazioneBilancio(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.jada.bulk.OggettoBulk result = ((CRUDPdgVariazioneGestionaleComponent)componentObj).esitaVariazioneBilancio(param0,param1);
		if (((Pdg_variazioneBulk)result).isErroreEsitaVariazioneBilancio())
			component_invocation_failure(param0,componentObj);
		else
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
public it.cnr.contab.prevent00.bulk.V_assestatoBulk trovaAssestato(it.cnr.jada.UserContext param0,it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
	pre_component_invocation(param0,componentObj);
	try {
		it.cnr.contab.prevent00.bulk.V_assestatoBulk result = ((CRUDPdgVariazioneGestionaleComponent)componentObj).trovaAssestato(param0,param1);
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
public void allineaSaldiVariazioneApprovata(UserContext param0, Ass_pdg_variazione_cdrBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException,RemoteException {
	pre_component_invocation(param0,componentObj);
	try {
		((CRUDPdgVariazioneGestionaleComponent)componentObj).allineaSaldiVariazioneApprovata(param0,param1);
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
	public Pdg_variazioneBulk generaVariazioneAutomaticaDaObbligazione(it.cnr.jada.UserContext param0,ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			Pdg_variazioneBulk result = ((CRUDPdgVariazioneGestionaleComponent)componentObj).generaVariazioneAutomaticaDaObbligazione(param0,param1);
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
