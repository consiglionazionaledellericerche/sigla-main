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

package it.cnr.contab.compensi00.ejb;

import it.cnr.contab.compensi00.comp.CompensoComponent;
import it.cnr.contab.compensi00.docs.bulk.BonusBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Acconto_classific_coriBulk;
import it.cnr.contab.docamm00.docs.bulk.TrovatoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

@Stateless(name = "CNRCOMPENSI00_EJB_CompensoComponentSession")
public class CompensoComponentSessionBean extends
		it.cnr.jada.ejb.CRUDComponentSessionBean implements
		CompensoComponentSession {
	@PostConstruct
	public void ejbCreate() {
		componentObj = new it.cnr.contab.compensi00.comp.CompensoComponent();
	}

	public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance()
			throws javax.ejb.EJBException {
		return new CompensoComponentSessionBean();
	}

	public void aggiornaMontanti(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			((CompensoComponent) componentObj).aggiornaMontanti(param0, param1);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk aggiornaObbligazione(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.aggiornaObbligazione(param0, param1, param2);
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

	public java.lang.Long assegnaProgressivo(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			java.lang.Long result = ((CompensoComponent) componentObj)
					.assegnaProgressivo(param0, param1);
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

	public java.lang.Long assegnaProgressivoTemporaneo(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			java.lang.Long result = ((CompensoComponent) componentObj)
					.assegnaProgressivoTemporaneo(param0, param1);
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

	public it.cnr.jada.util.RemoteIterator cercaObbligazioni(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.jada.util.RemoteIterator result = ((CompensoComponent) componentObj)
					.cercaObbligazioni(param0, param1);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk completaTerzo(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.completaTerzo(param0, param1, param2);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk confermaModificaCORI(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk param2)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.confermaModificaCORI(param0, param1, param2);
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

	public it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk conguaglioAssociatoACompenso(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk result = ((CompensoComponent) componentObj)
					.conguaglioAssociatoACompenso(param0, param1);
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

	public it.cnr.jada.bulk.OggettoBulk creaConBulk(
			it.cnr.jada.UserContext param0,
			it.cnr.jada.bulk.OggettoBulk param1,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((CompensoComponent) componentObj)
					.creaConBulk(param0, param1, param2);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk doContabilizzaCompensoCofi(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.doContabilizzaCompensoCofi(param0, param1);
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

	public it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk doElaboraCUD(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk result = ((CompensoComponent) componentObj)
					.doElaboraCUD(param0, param1);
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

	public it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk doElaboraINPSMensile(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk result = ((CompensoComponent) componentObj)
					.doElaboraINPSMensile(param0, param1);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk elaboraScadenze(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2,
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.elaboraScadenze(param0, param1, param2, param3);
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

	public void eliminaCompensoTemporaneo(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			java.lang.Long param2) throws it.cnr.jada.comp.ComponentException,
			javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			((CompensoComponent) componentObj).eliminaCompensoTemporaneo(
					param0, param1, param2);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk eliminaObbligazione(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.eliminaObbligazione(param0, param1);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk eseguiCalcolo(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.eseguiCalcolo(param0, param1);
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

	public java.util.List findListaBanche(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			java.util.List result = ((CompensoComponent) componentObj)
					.findListaBanche(param0, param1);
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

	public java.util.Collection findModalita(it.cnr.jada.UserContext param0,
			it.cnr.jada.bulk.OggettoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			java.util.Collection result = ((CompensoComponent) componentObj)
					.findModalita(param0, param1);
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

	public java.util.Collection findTermini(it.cnr.jada.UserContext param0,
			it.cnr.jada.bulk.OggettoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			java.util.Collection result = ((CompensoComponent) componentObj)
					.findTermini(param0, param1);
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

	public java.util.Collection findTipiRapporto(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			java.util.Collection result = ((CompensoComponent) componentObj)
					.findTipiRapporto(param0, param1);
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

	public java.util.Collection findTipiTrattamento(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			java.util.Collection result = ((CompensoComponent) componentObj)
					.findTipiTrattamento(param0, param1);
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

	public java.util.Collection findTipiPrestazioneCompenso(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			java.util.Collection result = ((CompensoComponent) componentObj)
					.findTipiPrestazioneCompenso(param0, param1);
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
	public java.sql.Timestamp getDataOdierna(it.cnr.jada.UserContext param0)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			java.sql.Timestamp result = ((CompensoComponent) componentObj)
					.getDataOdierna(param0);
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

	public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(
			it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((CompensoComponent) componentObj)
					.inizializzaBulkPerStampa(param0, param1);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerMinicarriera(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param2,
			java.util.List param3) throws it.cnr.jada.comp.ComponentException,
			javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.inizializzaCompensoPerMinicarriera(param0, param1, param2,
							param3);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerMissione(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.missioni00.docs.bulk.MissioneBulk param2)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.inizializzaCompensoPerMissione(param0, param1, param2);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk inserisciCompenso(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.inserisciCompenso(param0, param1);
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

	public boolean isCompensoAnnullato(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			boolean result = ((CompensoComponent) componentObj)
					.isCompensoAnnullato(param0, param1);
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

	public java.util.List loadDocContAssociati(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			java.util.List result = ((CompensoComponent) componentObj)
					.loadDocContAssociati(param0, param1);
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

	public it.cnr.jada.bulk.OggettoBulk modificaConBulk(
			it.cnr.jada.UserContext param0,
			it.cnr.jada.bulk.OggettoBulk param1,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((CompensoComponent) componentObj)
					.modificaConBulk(param0, param1, param2);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk onTipoTrattamentoChange(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.onTipoTrattamentoChange(param0, param1);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk reloadCompenso(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.reloadCompenso(param0, param1);
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

	public it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk resyncScadenza(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk result = ((CompensoComponent) componentObj)
					.resyncScadenza(param0, param1);
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

	public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1,
			it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk result = ((CompensoComponent) componentObj)
					.riportaAvanti(param0, param1, param2);
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

	public void rollbackToSavePoint(it.cnr.jada.UserContext param0,
			java.lang.String param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			((CompensoComponent) componentObj).rollbackToSavePoint(param0,
					param1);
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

	public void setSavePoint(it.cnr.jada.UserContext param0,
			java.lang.String param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			((CompensoComponent) componentObj).setSavePoint(param0, param1);
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

	public it.cnr.jada.bulk.OggettoBulk stampaConBulk(
			it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.jada.bulk.OggettoBulk result = ((CompensoComponent) componentObj)
					.stampaConBulk(param0, param1);
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

	public it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk result = ((CompensoComponent) componentObj)
					.updateImportoAssociatoDocAmm(param0, param1);
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

	public void validaObbligazione(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1,
			it.cnr.jada.bulk.OggettoBulk param2)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			((CompensoComponent) componentObj).validaObbligazione(param0,
					param1, param2);
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

	public void validaTerzo(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			((CompensoComponent) componentObj).validaTerzo(param0, param1);
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

	public int validaTerzo(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			boolean param2) throws it.cnr.jada.comp.ComponentException,
			javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			int result = ((CompensoComponent) componentObj).validaTerzo(param0,
					param1, param2);
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

	public it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk doElabora770(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk result = ((CompensoComponent) componentObj)
					.doElabora770(param0, param1);
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

	public boolean isTerzoCervellone(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			boolean result = ((CompensoComponent) componentObj)
					.isTerzoCervellone(param0, param1);
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

	// aggiunto per testare l'esercizio della data competenza da/a
	public boolean isEsercizioChiusoPerDataCompetenza(
			it.cnr.jada.UserContext param0, Integer param1,
			java.lang.String param2)
			throws it.cnr.jada.comp.ComponentException,
			it.cnr.jada.persistency.PersistencyException,
			javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			boolean result = ((CompensoComponent) componentObj)
					.isEsercizioChiusoPerDataCompetenza(param0, param1, param2);
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

	public boolean verificaEsistenzaCompenso(UserContext param0,
			CompensoBulk param1) throws ComponentException, RemoteException {
		pre_component_invocation(param0, componentObj);
		try {
			boolean result = ((CompensoComponent) componentObj)
					.verificaEsistenzaCompenso(param0, param1);
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

	public boolean isCompensoValido(UserContext param0, CompensoBulk param1)
			throws ComponentException, RemoteException {
		pre_component_invocation(param0, componentObj);
		try {
			boolean result = ((CompensoComponent) componentObj)
					.isCompensoValido(param0, param1);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk onTipoRapportoInpsChange(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.DetailedRuntimeException,
			it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.onTipoRapportoInpsChange(param0, param1);
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

	public boolean isAccontoAddComOkPerContabil(it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			boolean result = ((CompensoComponent) componentObj)
					.isAccontoAddComOkPerContabil(param0, param1);
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

	public boolean isGestiteDeduzioniIrpef(it.cnr.jada.UserContext param0)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			boolean result = ((CompensoComponent) componentObj)
					.isGestiteDeduzioniIrpef(param0);
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

	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk completaIncarico(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk param2)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.completaIncarico(param0, param1, param2);
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

	public BigDecimal prendiUtilizzato(it.cnr.jada.UserContext param0,
			it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			BigDecimal result = ((CompensoComponent) componentObj)
					.prendiUtilizzato(param0, param1);
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

	public Acconto_classific_coriBulk doCalcolaAccontoAddCom(
			it.cnr.jada.UserContext param0, Acconto_classific_coriBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException,
			RemoteException {
		pre_component_invocation(param0, componentObj);
		try {
			Acconto_classific_coriBulk result = ((CompensoComponent) componentObj)
					.doCalcolaAccontoAddCom(param0, param1);
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

	public java.util.List findListaCompensiSIP(UserContext param0,
			String query, String dominio, String uo, String terzo, String voce,
			String cdr, String gae, String tipoRicerca, Timestamp data_inizio,
			Timestamp data_fine) throws ComponentException,
			java.rmi.RemoteException {
		pre_component_invocation(param0, componentObj);
		try {
			java.util.List result = ((CompensoComponent) componentObj)
					.findListaCompensiSIP(param0, query, dominio, uo, terzo,
							voce, cdr, gae, tipoRicerca, data_inizio, data_fine);
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

	public CompensoBulk inizializzaCompensoPerBonus(UserContext param0,
			CompensoBulk param1, BonusBulk param2) throws ComponentException,
			RemoteException {
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.inizializzaCompensoPerBonus(param0, param1, param2);
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

	public boolean isSospensioneIrpefOkPerContabil(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			boolean result = ((CompensoComponent) componentObj)
					.isSospensioneIrpefOkPerContabil(param0, param1);
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
	
	public void archiviaStampa(UserContext userContext, Date fromDate, Date untilDate, CompensoBulk compensoBulk, Integer... years)throws ComponentException,
	javax.ejb.EJBException {
		pre_component_invocation(userContext, componentObj);
		try {
			((CompensoComponent) componentObj).archiviaStampa(userContext, fromDate, untilDate, compensoBulk, years);
			component_invocation_succes(userContext, componentObj);
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
	
	public CompensoBulk ricercaCompensoTrovato(it.cnr.jada.UserContext userContext,Long esercizio,String cd_cds,String cd_unita_organizzativa,Long pg_compenso)throws ComponentException,java.rmi.RemoteException,PersistencyException{
		pre_component_invocation(userContext, componentObj);
		try {
			CompensoBulk result = ((CompensoComponent) componentObj).ricercaCompensoTrovato(userContext, esercizio, cd_cds, cd_unita_organizzativa, pg_compenso);
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
	
	public CompensoBulk ricercaCompensoByKey(it.cnr.jada.UserContext userContext,Long esercizio,String cd_cds,String cd_unita_organizzativa,Long pg_compenso)throws ComponentException,java.rmi.RemoteException,PersistencyException{
		pre_component_invocation(userContext, componentObj);
		try {
			CompensoBulk result = ((CompensoComponent) componentObj).ricercaCompensoByKey(userContext, esercizio, cd_cds, cd_unita_organizzativa, pg_compenso);
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

	public java.util.List<CompensoBulk> ricercaCompensiTrovato(it.cnr.jada.UserContext userContext,Long trovato)throws ComponentException,java.rmi.RemoteException,PersistencyException{
		pre_component_invocation(userContext, componentObj);
		try {
			List<CompensoBulk> result = ((CompensoComponent) componentObj).ricercaCompensiTrovato(userContext, trovato);
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
	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerFattura(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,
			it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param2) 
			throws it.cnr.jada.comp.ComponentException,
			javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.inizializzaCompensoPerFattura(param0, param1, param2);
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
	public it.cnr.contab.compensi00.docs.bulk.CompensoBulk valorizzaInfoDocEle(
			it.cnr.jada.UserContext param0,
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1)
			throws it.cnr.jada.DetailedRuntimeException,
			it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
		pre_component_invocation(param0, componentObj);
		try {
			it.cnr.contab.compensi00.docs.bulk.CompensoBulk result = ((CompensoComponent) componentObj)
					.valorizzaInfoDocEle(param0, param1);
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