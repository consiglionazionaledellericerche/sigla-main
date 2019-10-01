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

import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.missioni00.comp.MissioneComponent;
import it.cnr.contab.missioni00.docs.bulk.AnticipoBulk;
import it.cnr.contab.missioni00.docs.bulk.MissioneBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Stateless(name = "CNRMISSIONI00_EJB_MissioneComponentSession")
public class MissioneComponentSessionBean extends
        it.cnr.jada.ejb.CRUDComponentSessionBean implements
        MissioneComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance()
            throws javax.ejb.EJBException {
        return new MissioneComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new it.cnr.contab.missioni00.comp.MissioneComponent();
    }

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaCompensoPhisically(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .cancellaCompensoPhisically(param0, param1);
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaDiariaPhisically(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .cancellaDiariaPhisically(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaRimborsoPhisically(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .cancellaRimborsoPhisically(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk cancellaTappePhisically(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .cancellaTappePhisically(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
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
            it.cnr.jada.util.RemoteIterator result = ((MissioneComponent) componentObj)
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk completaTerzo(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,
            it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
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

    public it.cnr.jada.bulk.OggettoBulk creaConBulk(
            it.cnr.jada.UserContext param0,
            it.cnr.jada.bulk.OggettoBulk param1,
            it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.bulk.OggettoBulk result = ((MissioneComponent) componentObj)
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

    public it.cnr.contab.docamm00.tabrif.bulk.CambioBulk findCambio(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk param1,
            java.sql.Timestamp param2)
            throws it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.tabrif.bulk.CambioBulk result = ((MissioneComponent) componentObj)
                    .findCambio(param0, param1, param2);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.util.Collection findInquadramenti(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.Collection result = ((MissioneComponent) componentObj)
                    .findInquadramenti(param0, param1);
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk findInquadramentiETipiTrattamento(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .findInquadramentiETipiTrattamento(param0, param1);
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

    public java.util.Collection findListabanche(it.cnr.jada.UserContext param0,
                                                it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.Collection result = ((MissioneComponent) componentObj)
                    .findListabanche(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.util.Collection findTipi_rapporto(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.Collection result = ((MissioneComponent) componentObj)
                    .findTipi_rapporto(param0, param1);
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

    public java.util.Collection findTipi_trattamento(
            it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.Collection result = ((MissioneComponent) componentObj)
                    .findTipi_trattamento(param0, param1);
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk generaDiaria(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .generaDiaria(param0, param1);
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk generaRimborso(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .generaRimborso(param0, param1);
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
            it.cnr.jada.bulk.OggettoBulk result = ((MissioneComponent) componentObj)
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk inizializzaDivisaCambioPerRimborsoKm(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk param1)
            throws it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            javax.ejb.EJBException, javax.ejb.EJBException,
            it.cnr.jada.bulk.ValidationException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .inizializzaDivisaCambioPerRimborsoKm(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (javax.ejb.EJBException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public boolean isMissioneAnnullata(it.cnr.jada.UserContext param0,
                                       it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = ((MissioneComponent) componentObj)
                    .isMissioneAnnullata(param0, param1);
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk loadCompenso(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .loadCompenso(param0, param1);
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
            it.cnr.jada.bulk.OggettoBulk result = ((MissioneComponent) componentObj)
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

    public void rollbackToSavePoint(it.cnr.jada.UserContext param0,
                                    java.lang.String param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((MissioneComponent) componentObj).rollbackToSavePoint(param0,
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk setDivisaCambio(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,
            it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param2)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .setDivisaCambio(param0, param1, param2);
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk setNazioneDivisaCambioItalia(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,
            it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param2)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .setNazioneDivisaCambioItalia(param0, param1, param2);
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

    public void setSavePoint(it.cnr.jada.UserContext param0,
                             java.lang.String param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((MissioneComponent) componentObj).setSavePoint(param0, param1);
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
            it.cnr.jada.bulk.OggettoBulk result = ((MissioneComponent) componentObj)
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

    public void updateAnticipo(it.cnr.jada.UserContext param0,
                               it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((MissioneComponent) componentObj).updateAnticipo(param0, param1);
            component_invocation_succes(param0, componentObj);
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk updateCompenso(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException,
            it.cnr.jada.persistency.PersistencyException,
            javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .updateCompenso(param0, param1);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public void validaEsercizioDataRegistrazione(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((MissioneComponent) componentObj)
                    .validaEsercizioDataRegistrazione(param0, param1);
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

    public it.cnr.contab.missioni00.docs.bulk.MissioneBulk validaMassimaliSpesa(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,
            it.cnr.contab.missioni00.docs.bulk.Missione_dettaglioBulk param2)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException,
            it.cnr.jada.persistency.PersistencyException,
            it.cnr.jada.bulk.ValidationException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.missioni00.docs.bulk.MissioneBulk result = ((MissioneComponent) componentObj)
                    .validaMassimaliSpesa(param0, param1, param2);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (javax.ejb.EJBException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.bulk.ValidationException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
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
            ((MissioneComponent) componentObj).validaObbligazione(param0,
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
                            it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((MissioneComponent) componentObj).validaTerzo(param0, param1);
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
                           it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1,
                           boolean param2) throws it.cnr.jada.comp.ComponentException,
            javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            int result = ((MissioneComponent) componentObj).validaTerzo(param0,
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

    public boolean isDiariaEditable(it.cnr.jada.UserContext param0,
                                    it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param1)
            throws javax.ejb.EJBException, it.cnr.jada.comp.ComponentException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = ((MissioneComponent) componentObj)
                    .isDiariaEditable(param0, param1);
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
                                     it.cnr.contab.missioni00.docs.bulk.MissioneBulk param1)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = ((MissioneComponent) componentObj)
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

    public List recuperoTipi_pasto(UserContext aUC, Timestamp dataTappa, Long inquadramento, NazioneBulk nazione, String tipoPasto, CompoundFindClause clauses) throws ComponentException, java.rmi.RemoteException, PersistencyException {
        pre_component_invocation(aUC, componentObj);
        try {
            java.util.List result = ((MissioneComponent) componentObj)
                    .recuperoTipi_pasto(aUC, dataTappa, inquadramento, nazione, tipoPasto, clauses);
            component_invocation_succes(aUC, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(aUC, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(aUC, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(aUC, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(aUC, componentObj, e);
        }
    }

    public java.util.List recuperoTipiSpesa(UserContext aUC, Timestamp dataInizioTappa, Long nazione, Long inquadramento, Boolean rimborsoAmmissibile, String cdTipoSpesa) throws ComponentException, java.rmi.RemoteException, PersistencyException {
        pre_component_invocation(aUC, componentObj);
        try {
            java.util.List result = ((MissioneComponent) componentObj)
                    .recuperoTipiSpesa(aUC, dataInizioTappa, nazione, inquadramento, rimborsoAmmissibile, cdTipoSpesa);
            component_invocation_succes(aUC, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(aUC, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(aUC, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(aUC, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(aUC, componentObj, e);
        }
    }

    public java.util.List findListaMissioniSIP(UserContext param0,
                                               String query, String dominio, String uo, String terzo, String voce,
                                               String cdr, String gae, String tipoRicerca, Timestamp data_inizio,
                                               Timestamp data_fine) throws ComponentException,
            java.rmi.RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.List result = ((MissioneComponent) componentObj)
                    .findListaMissioniSIP(param0, query, dominio, uo, terzo,
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

    public void archiviaStampa(UserContext userContext, Date fromDate, Date untilDate, MissioneBulk missioneBulk, Integer... years) throws ComponentException,
            javax.ejb.EJBException {
        pre_component_invocation(userContext, componentObj);
        try {
            ((MissioneComponent) componentObj).archiviaStampa(userContext, fromDate, untilDate, missioneBulk, years);
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

    public it.cnr.contab.config00.bulk.Parametri_cnrBulk parametriCnr(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.config00.bulk.Parametri_cnrBulk result = ((MissioneComponent) componentObj).parametriCnr(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (javax.ejb.EJBException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.math.BigDecimal calcolaMinutiTappa(it.cnr.jada.UserContext param0,
                                                   it.cnr.contab.missioni00.docs.bulk.Missione_tappaBulk param1) throws it.cnr.jada.comp.ComponentException,
            javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.math.BigDecimal result = ((MissioneComponent) componentObj).calcolaMinutiTappa(param0,
                    param1);
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

    public java.math.BigDecimal recuperoCambio(it.cnr.jada.UserContext param0,
                                               String divisa, Timestamp dataInizioMissione) throws it.cnr.jada.comp.ComponentException,
            javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.math.BigDecimal result = ((MissioneComponent) componentObj).recuperoCambio(param0,
                    divisa, dataInizioMissione);
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

    public DivisaBulk recuperoDivisa(it.cnr.jada.UserContext param0, Long nazione, String gruppoInquadramento, Timestamp dataInizioMissione) throws it.cnr.jada.comp.ComponentException {
        pre_component_invocation(param0, componentObj);
        try {
            DivisaBulk result = ((MissioneComponent) componentObj).recuperoDivisa(param0, nazione, gruppoInquadramento, dataInizioMissione);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (javax.ejb.EJBException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public DivisaBulk getDivisaDefault(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
        try {
            DivisaBulk result = ((MissioneComponent) componentObj).getDivisaDefault(param0);
            component_invocation_succes(param0, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(param0, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (javax.ejb.EJBException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public SQLBuilder selectTipo_spesaByClause(UserContext aUC, Timestamp dataTappa, Long inquadramento, NazioneBulk nazione, Boolean ammissibileConRimborso, String tipoSpesa, CompoundFindClause clauses) throws ComponentException, PersistencyException {

        pre_component_invocation(aUC, componentObj);
        try {
            SQLBuilder result = ((MissioneComponent) componentObj).selectTipo_spesaByClause(aUC, dataTappa, inquadramento, nazione, ammissibileConRimborso, tipoSpesa, clauses);
            component_invocation_succes(aUC, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(aUC, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(aUC, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(aUC, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(aUC, componentObj, e);
        }
    }

    public SQLBuilder selectTipo_pastoByClause(UserContext aUC, Timestamp dataTappa, Long inquadramento, NazioneBulk nazione, String tipoPasto, CompoundFindClause clauses) throws ComponentException, PersistencyException {
        pre_component_invocation(aUC, componentObj);
        try {
            SQLBuilder result = ((MissioneComponent) componentObj).selectTipo_pastoByClause(aUC, dataTappa, inquadramento, nazione, tipoPasto, clauses);
            component_invocation_succes(aUC, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(aUC, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(aUC, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(aUC, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(aUC, componentObj, e);
        }
    }

    public SQLBuilder selectTipo_autoByClause(UserContext aUC, Timestamp dataTappa, NazioneBulk nazione, String tipoAuto, CompoundFindClause clauses) throws ComponentException {
        pre_component_invocation(aUC, componentObj);
        try {
            SQLBuilder result = ((MissioneComponent) componentObj).selectTipo_autoByClause(aUC, dataTappa, nazione, tipoAuto, clauses);
            component_invocation_succes(aUC, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(aUC, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(aUC, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(aUC, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(aUC, componentObj, e);
        }
    }

    public Obbligazione_scadenzarioBulk recuperoObbligazioneDaGemis(UserContext aUC, MissioneBulk missione) throws ComponentException, java.rmi.RemoteException {
        pre_component_invocation(aUC, componentObj);
        try {
            Obbligazione_scadenzarioBulk result = ((MissioneComponent) componentObj).recuperoObbligazioneDaGemis(aUC, missione);
            component_invocation_succes(aUC, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(aUC, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(aUC, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(aUC, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(aUC, componentObj, e);
        }
    }

    public AnticipoBulk recuperoAnticipoDaGemis(UserContext aUC, MissioneBulk missione) throws ComponentException, java.rmi.RemoteException {
        pre_component_invocation(aUC, componentObj);
        try {
            AnticipoBulk result = ((MissioneComponent) componentObj).recuperoAnticipoDaGemis(aUC, missione);
            component_invocation_succes(aUC, componentObj);
            return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(aUC, componentObj);
            throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(aUC, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(aUC, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(aUC, componentObj, e);
        }
    }
    public MissioneBulk caricaTerzoInModificaMissione(UserContext userContext, MissioneBulk missioneBulk) throws ComponentException,
            javax.ejb.EJBException {
        pre_component_invocation(userContext, componentObj);
        try {
            MissioneBulk result = ((MissioneComponent) componentObj).caricaTerzoInModificaMissione(userContext, missioneBulk);
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
    public void cancellazioneMissioneDaGemis(UserContext userContext, Long idRimborsoMissioneGemis)  throws ComponentException,
    javax.ejb.EJBException {
    	pre_component_invocation(userContext, componentObj);
    	try {
    		((MissioneComponent) componentObj).cancellazioneMissioneDaGemis(userContext, idRimborsoMissioneGemis);
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
}
