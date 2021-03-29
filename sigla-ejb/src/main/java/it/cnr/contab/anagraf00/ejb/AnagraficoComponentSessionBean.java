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

package it.cnr.contab.anagraf00.ejb;
import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateless;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import it.cnr.contab.anagraf00.comp.AnagraficoComponent;
import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk;
import it.cnr.contab.compensi00.comp.CompensoComponent;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;

@Stateless(name="CNRANAGRAF00_EJB_AnagraficoComponentSession")
public class AnagraficoComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements AnagraficoComponentSession {
@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.anagraf00.comp.AnagraficoComponent();
	}
	@Remove
	public void ejbRemove() throws javax.ejb.EJBException {
		componentObj.release();
	}

	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
		return new AnagraficoComponentSessionBean();
	}
	public java.lang.String calcolaCodiceFiscale(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.lang.String result = ((AnagraficoComponent)componentObj).calcolaCodiceFiscale(param0,param1);
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
	public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagraficoEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk result = ((AnagraficoComponent)componentObj).getAnagraficoEnte(param0);
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
	public it.cnr.contab.anagraf00.core.bulk.TerzoBulk getDefaultTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.anagraf00.core.bulk.TerzoBulk result = ((AnagraficoComponent)componentObj).getDefaultTerzo(param0,param1);
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
	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((AnagraficoComponent)componentObj).inizializzaBulkPerStampa(param0,param1);
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
	public it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk setComune_fiscale(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk result = ((AnagraficoComponent)componentObj).setComune_fiscale(param0,param1,param2);
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
	public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((AnagraficoComponent)componentObj).stampaConBulk(param0,param1);
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
	public java.util.List bulkForSIP(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.util.List result = ((AnagraficoComponent)componentObj).bulkForSIP(param0,param1);
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
	public void eliminaBulkForSIP(it.cnr.jada.UserContext param0,String param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((AnagraficoComponent)componentObj).eliminaBulkForSIP(param0,param1);
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
	
	public boolean isItalianoEsteroModificabile(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnagraficoComponent)componentObj).isItalianoEsteroModificabile(param0,param1);
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
	public void checkConiugeAlreadyExistFor(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1, it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((AnagraficoComponent)componentObj).checkConiugeAlreadyExistFor(param0,param1,param2);
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
	public boolean isGestiteDeduzioniIrpef(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnagraficoComponent)componentObj).isGestiteDeduzioniIrpef(param0);
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
	public boolean isGestiteDeduzioniFamily(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnagraficoComponent)componentObj).isGestiteDeduzioniFamily(param0);
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
	public boolean isGestiteDetrazioniAltre(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnagraficoComponent)componentObj).isGestiteDetrazioniAltre(param0);
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
	public boolean isGestiteDetrazioniFamily(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnagraficoComponent)componentObj).isGestiteDetrazioniFamily(param0);
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
	public boolean esisteConiugeValido(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1, it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnagraficoComponent)componentObj).esisteConiugeValido(param0,param1,param2);
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
	public boolean esisteFiglioValido(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnagraficoComponent)componentObj).esisteFiglioValido(param0,param1);
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
	public java.util.List EstraiLista(it.cnr.jada.UserContext param0,Long param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			java.util.List result = ((AnagraficoComponent)componentObj).EstraiLista(param0,param1);
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
	
	public boolean esisteAltroFamValido(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnagraficoComponent)componentObj).esisteAltroFamValido(param0,param1);
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
	
	public Long Max_prog_estrazione(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			Long result = ((AnagraficoComponent)componentObj).Max_prog_estrazione(param0);
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
	public void Popola_ecf(it.cnr.jada.UserContext param0,Long param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			((AnagraficoComponent)componentObj).Popola_ecf(param0,param1);
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
	public Timestamp findMaxDataCompValida(it.cnr.jada.UserContext param0,AnagraficoBulk param1) throws IntrospectionException,PersistencyException,it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {
		pre_component_invocation(param0,componentObj);
		try {
			Timestamp result = ((AnagraficoComponent)componentObj).findMaxDataCompValida(param0,param1);
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
	public boolean verificaStrutturaPiva(UserContext param0,AnagraficoBulk anagrafico) throws ComponentException,ValidationException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnagraficoComponent)componentObj).verificaStrutturaPiva(param0,anagrafico);
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
	public void checkCaricoAlreadyExistFor(UserContext param0,
			AnagraficoBulk param1, Carico_familiare_anagBulk param2)
			throws ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			((AnagraficoComponent)componentObj).checkCaricoAlreadyExistFor(param0,param1,param2);
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
	public boolean isGestitoCreditoIrpef(UserContext param0)
			throws ComponentException, RemoteException {
		pre_component_invocation(param0,componentObj);
		try {
			boolean result = ((AnagraficoComponent)componentObj).isGestitoCreditoIrpef(param0);
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
	public void controllaUnicitaCaricoInAnnoImposta(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1, it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk param2) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {	pre_component_invocation(param0,componentObj);
		try {
			((AnagraficoComponent)componentObj).controllaUnicitaCaricoInAnnoImposta(param0,param1,param2);
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
	public void aggiornaDatiAce(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,javax.ejb.EJBException {	pre_component_invocation(param0,componentObj);
		try {
			((AnagraficoComponent)componentObj).aggiornaDatiAce(param0,param1);
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
