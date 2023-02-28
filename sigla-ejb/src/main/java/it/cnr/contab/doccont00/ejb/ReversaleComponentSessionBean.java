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

import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoEntrataBulk;
import it.cnr.contab.doccont00.comp.ReversaleComponent;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.text.ParseException;

@Stateless(name = "CNRDOCCONT00_EJB_ReversaleComponentSession")
public class ReversaleComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements ReversaleComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
        return new ReversaleComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {
        componentObj = new it.cnr.contab.doccont00.comp.ReversaleComponent();
    }

    public it.cnr.contab.doccont00.core.bulk.ReversaleBulk aggiungiDocAttivi(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1, java.util.List param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.ReversaleBulk result = ((ReversaleComponent) componentObj).aggiungiDocAttivi(param0, param1, param2);
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

    public boolean isChiudibileReversaleProvvisoria(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = ((ReversaleComponent) componentObj).isChiudibileReversaleProvvisoria(param0, param1);
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

    public boolean isRevProvvLiquidCoriCentroAperta(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = ((ReversaleComponent) componentObj).isRevProvvLiquidCoriCentroAperta(param0, param1);
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

    public it.cnr.contab.doccont00.core.bulk.ReversaleBulk annullaReversale(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.ReversaleBulk result = ((ReversaleComponent) componentObj).annullaReversale(param0, param1);
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

    public it.cnr.contab.doccont00.core.bulk.ReversaleBulk annullaReversale(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1, boolean param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.ReversaleBulk result = ((ReversaleComponent) componentObj).annullaReversale(param0, param1, param2);
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

    public void annullaReversaleDiIncassoIVA(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((ReversaleComponent) componentObj).annullaReversaleDiIncassoIVA(param0, param1);
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

    public void annullaReversaleDiRegolarizzazione(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((ReversaleComponent) componentObj).annullaReversaleDiRegolarizzazione(param0, param1);
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

    public void annullaReversaleDiTrasferimento(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((ReversaleComponent) componentObj).annullaReversaleDiTrasferimento(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator cercaSospesi(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = ((ReversaleComponent) componentObj).cercaSospesi(param0, param1, param2);
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

    public it.cnr.contab.doccont00.core.bulk.ReversaleBulk creaReversaleDiIncassoIVA(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.ReversaleBulk result = ((ReversaleComponent) componentObj).creaReversaleDiIncassoIVA(param0, param1);
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

    public it.cnr.contab.doccont00.core.bulk.ReversaleBulk creaReversaleDiRegolarizzazione(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.ReversaleBulk result = ((ReversaleComponent) componentObj).creaReversaleDiRegolarizzazione(param0, param1);
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

    public it.cnr.contab.doccont00.core.bulk.ReversaleBulk creaReversaleDiTrasferimento(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.MandatoAccreditamentoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.ReversaleBulk result = ((ReversaleComponent) componentObj).creaReversaleDiTrasferimento(param0, param1);
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

    public it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.bulk.OggettoBulk result = ((ReversaleComponent) componentObj).inizializzaBulkPerStampa(param0, param1);
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

    public it.cnr.contab.doccont00.core.bulk.ReversaleBulk listaDocAttivi(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.ReversaleBulk result = ((ReversaleComponent) componentObj).listaDocAttivi(param0, param1);
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

    public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.bulk.OggettoBulk result = ((ReversaleComponent) componentObj).stampaConBulk(param0, param1);
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

    public java.lang.Boolean isCollegamentoSiopeCompleto(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.lang.Boolean result = ((ReversaleComponent) componentObj).isCollegamentoSiopeCompleto(param0, param1);
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

    public it.cnr.contab.doccont00.core.bulk.ReversaleBulk setCodiciSIOPECollegabili(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.ReversaleBulk result = ((ReversaleComponent) componentObj).setCodiciSIOPECollegabili(param0, param1);
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

    public it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk setCodiciSIOPECollegabili(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk result = ((ReversaleComponent) componentObj).setCodiciSIOPECollegabili(param0, param1);
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

    public byte[] lanciaStampa(it.cnr.jada.UserContext userContext, String cds, Integer esercizio, Long pgReversale) throws PersistencyException, ComponentException, RemoteException, ParseException {
        pre_component_invocation(userContext, componentObj);
        try {
            byte[] result = ((ReversaleComponent) componentObj).lanciaStampa(userContext, cds, esercizio, pgReversale);
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

    @Override
    public ReversaleBulk annullaReversale(UserContext param0, ReversaleBulk param1,
                                          boolean param2, boolean param3) throws ComponentException,
            RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.ReversaleBulk result = ((ReversaleComponent) componentObj).annullaReversale(param0, param1, param2, param3);
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

    @Override
    public String isAnnullabile(UserContext param0, ReversaleBulk param1)
            throws ComponentException, RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            java.lang.String result = ((ReversaleComponent) componentObj).isAnnullabile(param0, param1);
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

    public java.lang.Boolean esisteAnnullodaRiemettereNonCollegato(UserContext param0, Integer param1, String param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.lang.Boolean result = ((ReversaleComponent) componentObj).esisteAnnullodaRiemettereNonCollegato(param0, param1, param2);
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

    public java.lang.Boolean isReversaleCollegataAnnullodaRiemettere(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.lang.Boolean result = ((ReversaleComponent) componentObj).isReversaleCollegataAnnullodaRiemettere(param0, param1);
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

    public java.lang.Boolean isReversaleCORINonAssociataMandato(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ReversaleBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.lang.Boolean result = ((ReversaleComponent) componentObj).isReversaleCORINonAssociataMandato(param0, param1);
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

    @Override
    public IDocumentoAmministrativoEntrataBulk getDocumentoAmministrativoEntrataBulk(UserContext param0, Reversale_rigaBulk param1) throws ComponentException, RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            IDocumentoAmministrativoEntrataBulk result = ((ReversaleComponent) componentObj).getDocumentoAmministrativoEntrataBulk(param0, param1);
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
