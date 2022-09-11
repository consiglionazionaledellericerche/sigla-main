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

import it.cnr.contab.docamm00.comp.FatturaPassivaComponent;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.TrovatoBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

@Stateless(name = "CNRDOCAMM00_EJB_FatturaPassivaComponentSession")
public class FatturaPassivaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements FatturaPassivaComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
        return new FatturaPassivaComponentSessionBean();
    }

    @PostConstruct
    public void ejbCreate() {

        componentObj = new it.cnr.contab.docamm00.comp.FatturaPassivaComponent();
    }

    public it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk addebitaDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk param1, java.util.List param2, java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk result = ((FatturaPassivaComponent) componentObj).addebitaDettagli(param0, param1, param2, param3);
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

    public void aggiornaStatoDocumentiAmministrativi(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, java.lang.String param3, java.lang.Integer param4, java.lang.Long param5, java.lang.String param6) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).aggiornaStatoDocumentiAmministrativi(param0, param1, param2, param3, param4, param5, param6);
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

    public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk calcoloConsuntivi(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk result = ((FatturaPassivaComponent) componentObj).calcoloConsuntivi(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk cercaCambio(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk result = ((FatturaPassivaComponent) componentObj).cercaCambio(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdC(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaPassivaComponent) componentObj).cercaDettagliFatturaPerNdC(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdD(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaPassivaComponent) componentObj).cercaDettagliFatturaPerNdD(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator cercaFatturaPerNdC(it.cnr.jada.UserContext param0, CompoundFindClause compoundfindclause, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaPassivaComponent) componentObj).cercaFatturaPerNdC(param0, compoundfindclause, param1);
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

    public it.cnr.jada.util.RemoteIterator cercaFatturaPerNdD(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaPassivaComponent) componentObj).cercaFatturaPerNdD(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaPassivaComponent) componentObj).cercaObbligazioni(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk completaEnte(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk result = ((FatturaPassivaComponent) componentObj).completaEnte(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk completaFornitore(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, it.cnr.contab.anagraf00.core.bulk.TerzoBulk param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk result = ((FatturaPassivaComponent) componentObj).completaFornitore(param0, param1, param2);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, java.util.Collection param2, it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk result = ((FatturaPassivaComponent) componentObj).contabilizzaDettagliSelezionati(param0, param1, param2, param3);
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

    public void controllaQuadraturaConti(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).controllaQuadraturaConti(param0, param1);
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

    public void controllaQuadraturaObbligazioni(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).controllaQuadraturaObbligazioni(param0, param1);
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

    public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.bulk.OggettoBulk result = ((FatturaPassivaComponent) componentObj).creaConBulk(param0, param1, param2);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk eliminaLetteraPagamentoEstero(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk result = ((FatturaPassivaComponent) componentObj).eliminaLetteraPagamentoEstero(param0, param1);
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

    public void eliminaRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).eliminaRiga(param0, param1);
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

    public java.util.Vector estraeSezionali(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.Vector result = ((FatturaPassivaComponent) componentObj).estraeSezionali(param0, param1);
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

    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.anagraf00.core.bulk.TerzoBulk result = ((FatturaPassivaComponent) componentObj).findCessionario(param0, param1);
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

    public java.util.List findDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.List result = ((FatturaPassivaComponent) componentObj).findDettagli(param0, param1);
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
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public java.util.Collection findListabanche(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.Collection result = ((FatturaPassivaComponent) componentObj).findListabanche(param0, param1);
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

    public java.util.Collection findListabancheuo(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.Collection result = ((FatturaPassivaComponent) componentObj).findListabancheuo(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator findNotaDiCreditoFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaPassivaComponent) componentObj).findNotaDiCreditoFor(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator findNotaDiDebitoFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaPassivaComponent) componentObj).findNotaDiDebitoFor(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator findObbligazioniFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, java.math.BigDecimal param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaPassivaComponent) componentObj).findObbligazioniFor(param0, param1, param2);
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

    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk findTerzoUO(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.anagraf00.core.bulk.TerzoBulk result = ((FatturaPassivaComponent) componentObj).findTerzoUO(param0, param1);
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

    public it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk findUOEnte(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk result = ((FatturaPassivaComponent) componentObj).findUOEnte(param0, param1);
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

    public boolean hasFatturaPassivaARowNotInventoried(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = ((FatturaPassivaComponent) componentObj).hasFatturaPassivaARowNotInventoried(param0, param1);
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

    public Boolean ha_beniColl(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            Boolean result = ((FatturaPassivaComponent) componentObj).ha_beniColl(param0, param1);
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

    public void inserisciRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).inserisciRiga(param0, param1);
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

    public boolean isBeneServizioPerSconto(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = ((FatturaPassivaComponent) componentObj).isBeneServizioPerSconto(param0, param1);
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

    public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.bulk.OggettoBulk result = ((FatturaPassivaComponent) componentObj).modificaConBulk(param0, param1, param2);
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

    public void protocolla(it.cnr.jada.UserContext param0, java.sql.Timestamp param1, java.lang.Long param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).protocolla(param0, param1, param2);
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

    public void rimuoviDaAssociazioniInventario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk param1, it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).rimuoviDaAssociazioniInventario(param0, param1, param2);
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

    public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk result = ((FatturaPassivaComponent) componentObj).riportaAvanti(param0, param1, param2);
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

    public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaIndietro(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk result = ((FatturaPassivaComponent) componentObj).riportaIndietro(param0, param1);
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

    public void rollbackToSavePoint(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).rollbackToSavePoint(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk selezionaValutaDiDefault(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk result = ((FatturaPassivaComponent) componentObj).selezionaValutaDiDefault(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk setContoEnteIn(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1, java.util.List param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk result = ((FatturaPassivaComponent) componentObj).setContoEnteIn(param0, param1, param2);
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

    public void setSavePoint(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).setSavePoint(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk stornaDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1, java.util.List param2, java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk result = ((FatturaPassivaComponent) componentObj).stornaDettagli(param0, param1, param2, param3);
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

    public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk update(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk result = ((FatturaPassivaComponent) componentObj).update(param0, param1);
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

    public it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk result = ((FatturaPassivaComponent) componentObj).updateImportoAssociatoDocAmm(param0, param1);
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

    public void validaFattura(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).validaFattura(param0, param1);
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

    public void validaRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).validaRiga(param0, param1);
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

    public void verificaEsistenzaEdAperturaInventario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).verificaEsistenzaEdAperturaInventario(param0, param1);
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

    public boolean verificaStatoEsercizio(it.cnr.jada.UserContext param0, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = ((FatturaPassivaComponent) componentObj).verificaStatoEsercizio(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator selectBeniFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaPassivaComponent) componentObj).selectBeniFor(param0, param1);
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

    //aggiunto per testare l'esercizio della data competenza da/a
    public boolean isEsercizioChiusoPerDataCompetenza(it.cnr.jada.UserContext param0, Integer param1, java.lang.String param2) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = ((FatturaPassivaComponent) componentObj).isEsercizioChiusoPerDataCompetenza(param0, param1, param2);
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

    public void rimuoviDaAssociazioniInventario(it.cnr.jada.UserContext param0, it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).rimuoviDaAssociazioniInventario(param0, param1);
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

    public java.util.Collection findListabanchedett(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.Collection result = ((FatturaPassivaComponent) componentObj).findListabanchedett(param0, param1);
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

    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.anagraf00.core.bulk.TerzoBulk result = ((FatturaPassivaComponent) componentObj).findCessionario(param0, param1);
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

    public java.util.List findListaFattureSIP(UserContext param0, String query, String dominio, String uo, String terzo, String voce, String cdr, String gae, String tipoRicerca, Timestamp data_inizio, Timestamp data_fine) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.List result = ((FatturaPassivaComponent) componentObj).findListaFattureSIP(param0, query, dominio, uo, terzo, voce, cdr, gae, tipoRicerca, data_inizio, data_fine);
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

    public java.util.List findManRevRigaCollegati(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.List result = ((FatturaPassivaComponent) componentObj).findManRevRigaCollegati(param0, param1);
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
        } catch (it.cnr.jada.persistency.IntrospectionException e) {
            component_invocation_failure(param0, componentObj);
            throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(param0, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(param0, componentObj, e);
        }
    }

    public it.cnr.jada.bulk.OggettoBulk rebuildDocumento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.jada.bulk.OggettoBulk result = ((FatturaPassivaComponent) componentObj).rebuildDocumento(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk ricercaFatturaTrovato(it.cnr.jada.UserContext param0, Long esercizio, String cd_cds, String cd_unita_organizzativa, Long pg_fattura) throws it.cnr.jada.comp.ComponentException, RemoteException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk result = ((FatturaPassivaComponent) componentObj).ricercaFatturaTrovato(param0, esercizio, cd_cds, cd_unita_organizzativa, pg_fattura);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk ricercaFatturaByKey(it.cnr.jada.UserContext param0, Long esercizio, String cd_cds, String cd_unita_organizzativa, Long pg_fattura) throws it.cnr.jada.comp.ComponentException, RemoteException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk result = ((FatturaPassivaComponent) componentObj).ricercaFatturaByKey(param0, esercizio, cd_cds, cd_unita_organizzativa, pg_fattura);
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

    public TrovatoBulk ricercaDatiTrovato(it.cnr.jada.UserContext userContext, Long trovato) throws ComponentException, java.rmi.RemoteException, PersistencyException {
        pre_component_invocation(userContext, componentObj);
        try {
            TrovatoBulk trovatoBulk = ((FatturaPassivaComponent) componentObj).ricercaDatiTrovato(userContext, trovato);
            component_invocation_succes(userContext, componentObj);
            return trovatoBulk;
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

    public TrovatoBulk ricercaDatiTrovatoValido(it.cnr.jada.UserContext userContext, Long trovato) throws ComponentException, java.rmi.RemoteException, PersistencyException {
        pre_component_invocation(userContext, componentObj);
        try {
            TrovatoBulk trovatoBulk = ((FatturaPassivaComponent) componentObj).ricercaDatiTrovatoValido(userContext, trovato);
            component_invocation_succes(userContext, componentObj);
            return trovatoBulk;
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

    public java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk> ricercaFattureTrovato(it.cnr.jada.UserContext param0, Long param1) throws it.cnr.jada.comp.ComponentException, RemoteException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
        try {
            java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk> result = ((FatturaPassivaComponent) componentObj).ricercaFattureTrovato(param0, param1);
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

    public void inserisciProgUnivoco(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.ElaboraNumUnicoFatturaPBulk param1) throws ComponentException, RemoteException, PersistencyException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).inserisciProgUnivoco(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk caricaAllegatiBulk(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk result = ((FatturaPassivaComponent) componentObj).caricaAllegatiBulk(param0, param1);
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

    public void validaFatturaPerCompenso(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).validaFatturaPerCompenso(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk valorizzaInfoDocEle(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1)
            throws it.cnr.jada.DetailedRuntimeException,
            it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk result = ((FatturaPassivaComponent) componentObj)
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

    public void aggiornaObblSuCancPerCompenso(UserContext param0,
                                              Fattura_passivaBulk param1, java.util.Vector param2,
                                              OptionRequestParameter param3)
            throws ComponentException, RemoteException {
        pre_component_invocation(param0, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).aggiornaObblSuCancPerCompenso(param0, param1, param2, param3);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk eliminaLetteraPagamentoEstero(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, boolean param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk result = ((FatturaPassivaComponent) componentObj).eliminaLetteraPagamentoEstero(param0, param1, param2);
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

    public boolean isAttivoSplitPayment(UserContext param0, Timestamp param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = (((FatturaPassivaComponent) componentObj).isAttivoSplitPayment(param0, param1));
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

    public List<EvasioneOrdineRigaBulk> findRicercaOrdiniByClause(UserContext userContext,
                                                                     Fattura_passivaBulk fatturaPassiva,
                                                                     CompoundFindClause findclause)
            throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(userContext, componentObj);
        try {
            List<EvasioneOrdineRigaBulk> result = (((FatturaPassivaComponent) componentObj).findRicercaOrdiniByClause(userContext, fatturaPassiva, findclause));
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
    public boolean isAttivoSplitPaymentProf(UserContext param0, Timestamp param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
        try {
            boolean result = (((FatturaPassivaComponent) componentObj).isAttivoSplitPaymentProf(param0, param1));
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
    public void valorizzaDatiDaOrdini(UserContext userContext, Fattura_passivaBulk fattura)
    throws ComponentException, RemoteException {
        pre_component_invocation(userContext, componentObj);
        try {
            ((FatturaPassivaComponent) componentObj).valorizzaDatiDaOrdini(userContext, fattura);
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
