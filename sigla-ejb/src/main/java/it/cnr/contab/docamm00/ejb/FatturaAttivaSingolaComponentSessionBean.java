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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.xml.datatype.XMLGregorianCalendar;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.comp.FatturaAttivaSingolaComponent;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk;
import it.cnr.contab.docamm00.ejb.FatturaAttivaSingolaComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

@Stateless(name = "CNRDOCAMM00_EJB_FatturaAttivaSingolaComponentSession")
public class FatturaAttivaSingolaComponentSessionBean extends it.cnr.jada.ejb.CRUDComponentSessionBean implements FatturaAttivaSingolaComponentSession {
    public static it.cnr.jada.ejb.CRUDComponentSessionBean newInstance() throws javax.ejb.EJBException {
        return new FatturaAttivaSingolaComponentSessionBean();
    }

    @PostConstruct
	public void ejbCreate() {
	componentObj = new it.cnr.contab.docamm00.comp.FatturaAttivaSingolaComponent();
    }

    public it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk addebitaDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk param1, java.util.List param2, java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).addebitaDettagli(param0, param1, param2, param3);
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
            ((FatturaAttivaSingolaComponent) componentObj).aggiornaStatoDocumentiAmministrativi(param0, param1, param2, param3, param4, param5, param6);
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

    public void annullaSelezionePerStampa(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).annullaSelezionePerStampa(param0, param1);
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
            it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk result = ((FatturaAttivaSingolaComponent) componentObj).calcoloConsuntivi(param0, param1);
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

    public java.lang.Long callGetPgPerProtocolloIVA(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.lang.Long result = ((FatturaAttivaSingolaComponent) componentObj).callGetPgPerProtocolloIVA(param0);
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

    public void preparaProtocollazioneEProtocolla(UserContext userContext, Long pgProtocollazione, Integer offSet, Long pgStampa, java.sql.Timestamp dataStampa, Fattura_attivaBulk fattura) throws PersistencyException, ComponentException {
        pre_component_invocation(userContext, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).preparaProtocollazioneEProtocolla(userContext, pgProtocollazione, offSet, pgStampa, dataStampa, fattura);
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

    public java.lang.Long callGetPgPerStampa(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.lang.Long result = ((FatturaAttivaSingolaComponent) componentObj).callGetPgPerStampa(param0);
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

    public void cancellaDatiPerProtocollazioneIva(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1, java.lang.Long param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).cancellaDatiPerProtocollazioneIva(param0, param1, param2);
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

    public void cancellaDatiPerStampaIva(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1, java.lang.Long param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).cancellaDatiPerStampaIva(param0, param1, param2);
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

    public it.cnr.jada.util.RemoteIterator cercaAccertamenti(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_accertamentiVBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaAttivaSingolaComponent) componentObj).cercaAccertamenti(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk cercaCambio(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).cercaCambio(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdC(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaAttivaSingolaComponent) componentObj).cercaDettagliFatturaPerNdC(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdD(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaAttivaSingolaComponent) componentObj).cercaDettagliFatturaPerNdD(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator cercaFatturaPerNdC(it.cnr.jada.UserContext param0, CompoundFindClause compoundfindclause, it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaAttivaSingolaComponent) componentObj).cercaFatturaPerNdC(param0, compoundfindclause, param1);
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

    public it.cnr.jada.util.RemoteIterator cercaFatturaPerNdD(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaAttivaSingolaComponent) componentObj).cercaFatturaPerNdD(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk completaCliente(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).completaCliente(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk completaTerzo(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1, it.cnr.contab.anagraf00.core.bulk.TerzoBulk param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).completaTerzo(param0, param1, param2);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1, java.util.Collection param2, it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).contabilizzaDettagliSelezionati(param0, param1, param2, param3);
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

    public void controllaQuadraturaAccertamenti(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).controllaQuadraturaAccertamenti(param0, param1);
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
            it.cnr.jada.bulk.OggettoBulk result = ((FatturaAttivaSingolaComponent) componentObj).creaConBulk(param0, param1, param2);
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

    public void eliminaRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).eliminaRiga(param0, param1);
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

    public boolean esistonoDatiPerProtocollazioneIva(it.cnr.jada.UserContext param0, java.lang.Long param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            boolean result = ((FatturaAttivaSingolaComponent) componentObj).esistonoDatiPerProtocollazioneIva(param0, param1);
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

    public boolean esistonoDatiPerStampaIva(it.cnr.jada.UserContext param0, java.lang.Long param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            boolean result = ((FatturaAttivaSingolaComponent) componentObj).esistonoDatiPerStampaIva(param0, param1);
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

    public java.util.Vector estraeSezionali(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.Vector result = ((FatturaAttivaSingolaComponent) componentObj).estraeSezionali(param0, param1);
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

    public java.util.Vector estraeSezionaliPerRistampa(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1, java.util.Vector param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.Vector result = ((FatturaAttivaSingolaComponent) componentObj).estraeSezionaliPerRistampa(param0, param1, param2);
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

    public it.cnr.jada.util.RemoteIterator findAccertamentiFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1, java.math.BigDecimal param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaAttivaSingolaComponent) componentObj).findAccertamentiFor(param0, param1, param2);
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

    public java.util.List findDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findDettagli(param0, param1);
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

    public java.util.Collection findListabanche(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.Collection result = ((FatturaAttivaSingolaComponent) componentObj).findListabanche(param0, param1);
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

    public java.util.Vector findListabancheuo(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.Vector result = ((FatturaAttivaSingolaComponent) componentObj).findListabancheuo(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator findNotaDiCreditoFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaAttivaSingolaComponent) componentObj).findNotaDiCreditoFor(param0, param1);
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

    public it.cnr.jada.util.RemoteIterator findNotaDiDebitoFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaAttivaSingolaComponent) componentObj).findNotaDiDebitoFor(param0, param1);
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

    public it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk findTariffario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk result = ((FatturaAttivaSingolaComponent) componentObj).findTariffario(param0, param1);
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

    public boolean hasFatturaAttivaARowNotInventoried(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            boolean result = ((FatturaAttivaSingolaComponent) componentObj).hasFatturaAttivaARowNotInventoried(param0, param1);
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

    public void inizializzaSelezionePerStampa(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).inizializzaSelezionePerStampa(param0, param1);
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

    public void inserisciRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).inserisciRiga(param0, param1);
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

    public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.bulk.OggettoBulk result = ((FatturaAttivaSingolaComponent) componentObj).modificaConBulk(param0, param1, param2);
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

    public java.lang.Integer modificaSelezionePerStampa(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1, it.cnr.jada.bulk.OggettoBulk[] param2, java.util.BitSet param3, java.util.BitSet param4, java.lang.Long param5, java.lang.Integer param6, java.lang.Long param7, java.sql.Timestamp param8) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.lang.Integer result = ((FatturaAttivaSingolaComponent) componentObj).modificaSelezionePerStampa(param0, param1, param2, param3, param4, param5, param6, param7, param8);
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
            ((FatturaAttivaSingolaComponent) componentObj).protocolla(param0, param1, param2);
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
            it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk result = ((FatturaAttivaSingolaComponent) componentObj).riportaAvanti(param0, param1, param2);
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
            it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk result = ((FatturaAttivaSingolaComponent) componentObj).riportaIndietro(param0, param1);
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
            ((FatturaAttivaSingolaComponent) componentObj).rollbackToSavePoint(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_protocollabileVBulk selezionaTuttiPerStampa(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_protocollabileVBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_protocollabileVBulk result = ((FatturaAttivaSingolaComponent) componentObj).selezionaTuttiPerStampa(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_ristampabileVBulk selezionaTuttiPerStampa(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_ristampabileVBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_ristampabileVBulk result = ((FatturaAttivaSingolaComponent) componentObj).selezionaTuttiPerStampa(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk setContoEnteIn(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1, java.util.List param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).setContoEnteIn(param0, param1, param2);
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
            ((FatturaAttivaSingolaComponent) componentObj).setSavePoint(param0, param1);
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

    public it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk stornaDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1, java.util.List param2, java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).stornaDettagli(param0, param1, param2, param3);
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
            it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk result = ((FatturaAttivaSingolaComponent) componentObj).update(param0, param1);
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
            it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk result = ((FatturaAttivaSingolaComponent) componentObj).updateImportoAssociatoDocAmm(param0, param1);
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

    public void validaFattura(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).validaFattura(param0, param1);
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

    public void validaRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).validaRiga(param0, param1);
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

    public void verificaEsistenzaEdAperturaInventario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).verificaEsistenzaEdAperturaInventario(param0, param1);
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
            boolean result = ((FatturaAttivaSingolaComponent) componentObj).verificaStatoEsercizio(param0, param1);
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
    public boolean isEsercizioChiusoPerDataCompetenza(it.cnr.jada.UserContext param0, Integer param1, java.lang.String param2) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            boolean result = (((FatturaAttivaSingolaComponent) componentObj).isEsercizioChiusoPerDataCompetenza(param0, param1, param2));
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
            Boolean result = ((FatturaAttivaSingolaComponent) componentObj).ha_beniColl(param0, param1);
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

    public void rimuoviDaAssociazioniInventario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk param1, it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param2) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).rimuoviDaAssociazioniInventario(param0, param1, param2);
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

    public it.cnr.jada.util.RemoteIterator selectBeniFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.util.RemoteIterator result = ((FatturaAttivaSingolaComponent) componentObj).selectBeniFor(param0, param1);
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

    public it.cnr.jada.bulk.OggettoBulk rebuildDocumento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.bulk.OggettoBulk result = ((FatturaAttivaSingolaComponent) componentObj).rebuildDocumento(param0, param1);
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
            it.cnr.jada.util.RemoteIterator result = ((FatturaAttivaSingolaComponent) componentObj).cercaObbligazioni(param0, param1);
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

    public byte[] lanciaStampa(it.cnr.jada.UserContext param0, Long pg_stampa) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
	try {
            byte[] result = ((FatturaAttivaSingolaComponent) componentObj).lanciaStampa(param0, pg_stampa);
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

    public it.cnr.jada.bulk.OggettoBulk completaOggetto(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.jada.bulk.OggettoBulk result = ((FatturaAttivaSingolaComponent) componentObj).completaOggetto(param0, param1);
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

    public boolean VerificaDuplicati(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
	try {
            boolean result = ((FatturaAttivaSingolaComponent) componentObj).VerificaDuplicati(param0, param1);
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

    public java.util.List findListaModalitaPagamentoWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3, String param4) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaModalitaPagamentoWS(param0, param1, param2, param3, param4);
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

    public java.util.List findListaBancheWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3, String param4, String param5) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaBancheWS(param0, param1, param2, param3, param4, param5);
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

    public java.util.List findListaRigheperNCWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3, String param4, String param5,
                                                String param6, String param7) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaRigheperNCWS(param0, param1, param2, param3, param4, param5, param6, param7);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk ricercaFattura(it.cnr.jada.UserContext param0, Long esercizio, String cd_cds, String cd_unita_organizzativa, Long pg_fattura) throws it.cnr.jada.comp.ComponentException, RemoteException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).ricercaFattura(param0, esercizio, cd_cds, cd_unita_organizzativa, pg_fattura);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk ricercaFatturaTrovato(it.cnr.jada.UserContext param0, Long esercizio, String cd_cds, String cd_unita_organizzativa, Long pg_fattura) throws it.cnr.jada.comp.ComponentException, RemoteException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).ricercaFatturaTrovato(param0, esercizio, cd_cds, cd_unita_organizzativa, pg_fattura);
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

    public it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk ricercaFatturaByKey(it.cnr.jada.UserContext param0, Long esercizio, String cd_cds, String cd_unita_organizzativa, Long pg_fattura) throws it.cnr.jada.comp.ComponentException, RemoteException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).ricercaFatturaByKey(param0, esercizio, cd_cds, cd_unita_organizzativa, pg_fattura);
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

    public java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk> ricercaFattureTrovato(it.cnr.jada.UserContext param0, Long param1) throws it.cnr.jada.comp.ComponentException, RemoteException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk> result = ((FatturaAttivaSingolaComponent) componentObj).ricercaFattureTrovato(param0, param1);
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

    public java.util.List recuperoScadVoce(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException, RemoteException {
        pre_component_invocation(param0, componentObj);
		try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).recuperoScadVoce(param0, bulk);
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

    public java.util.List findManRevRigaCollegati(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findManRevRigaCollegati(param0, param1);
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

    public java.util.List findListaBeneServizioWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3, String param4) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaBeneServizioWS(param0, param1, param2, param3, param4);
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

    public java.util.List findListaModalitaIncassoWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaModalitaIncassoWS(param0, param1, param2, param3);
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

    public java.util.List findListaModalitaErogazioneWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaModalitaErogazioneWS(param0, param1, param2, param3);
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

    public java.util.List findListaCodiciCpaWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaCodiciCpaWS(param0, param1, param2, param3);
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

    public java.util.List findListaNazioneWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaNazioneWS(param0, param1, param2, param3);
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

    public java.util.List findListaNomenclaturaCombinataWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaNomenclaturaCombinataWS(param0, param1, param2, param3);
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

    public java.util.List findListaNaturaTransazioneWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaNaturaTransazioneWS(param0, param1, param2, param3);
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

    public java.util.List findListaCondizioneConsegnaWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaCondizioneConsegnaWS(param0, param1, param2, param3);
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

    public java.util.List findListaModalitaTrasportoWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaModalitaTrasportoWS(param0, param1, param2, param3);
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

    public java.util.List findListaProvinciaWS(it.cnr.jada.UserContext param0, String param1, String param2, String param3) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            java.util.List result = ((FatturaAttivaSingolaComponent) componentObj).findListaProvinciaWS(param0, param1, param2, param3);
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

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public java.lang.Long inserisciDatiPerStampaIva(it.cnr.jada.UserContext param0, Long esercizio, String cd_cds, String cd_unita_organizzativa, Long pg_fattura) throws it.cnr.jada.comp.ComponentException, javax.ejb.EJBException, it.cnr.jada.persistency.PersistencyException {
        pre_component_invocation(param0, componentObj);
	try {
            java.lang.Long result = ((FatturaAttivaSingolaComponent) componentObj).inserisciDatiPerStampaIva(param0, esercizio, cd_cds, cd_unita_organizzativa, pg_fattura);
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

    public Nota_di_credito_attivaBulk generaNotaCreditoAutomatica(it.cnr.jada.UserContext param0, Fattura_attiva_IBulk fa, Integer esercizio) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(param0, componentObj);
	try {
            it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).generaNotaCreditoAutomatica(param0, fa, esercizio);
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

    public Fattura_attivaBulk ricercaFatturaDaCodiceSDI(UserContext userContext, String codiceInvioSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).ricercaFatturaDaCodiceSDI(userContext, codiceInvioSdi);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public Fattura_attiva_IBulk ricercaFatturaSDI(UserContext userContext, String codiceInvioSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attiva_IBulk result = ((FatturaAttivaSingolaComponent) componentObj).ricercaFatturaSDI(userContext, codiceInvioSdi);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public Fattura_attivaBulk aggiornaFatturaScartoSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceInvioSdi, String noteSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaFatturaScartoSDI(userContext, fattura, codiceInvioSdi, noteSdi);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}

    }

    public Fattura_attivaBulk aggiornaFatturaEsitoAccettatoSDI(UserContext userContext, Fattura_attivaBulk fattura) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaFatturaEsitoAccettatoSDI(userContext, fattura);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public Fattura_attivaBulk aggiornaFatturaDecorrenzaTerminiSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaFatturaDecorrenzaTerminiSDI(userContext, fattura, noteSdi);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public Fattura_attivaBulk aggiornaFatturaRifiutataDestinatarioSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaFatturaRifiutataDestinatarioSDI(userContext, fattura, noteSdi);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public Fattura_attivaBulk aggiornaFatturaConsegnaSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, Date dataConsegnaSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaFatturaConsegnaSDI(userContext, fatturaAttiva, dataConsegnaSdi);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public Fattura_attivaBulk aggiornaFatturaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaFatturaInvioSDI(userContext, fatturaAttiva);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}

    }

    public Fattura_attivaBulk aggiornaFatturaPredispostaAllaFirma(UserContext userContext, Fattura_attivaBulk fatturaAttiva) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaFatturaPredispostaAllaFirma(userContext, fatturaAttiva);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}

    }

    public Fattura_attivaBulk aggiornaFatturaMancataConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, String noteInvioSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaFatturaMancataConsegnaInvioSDI(userContext, fatturaAttiva, codiceSdi, noteInvioSdi);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public Fattura_attivaBulk aggiornaFatturaRicevutaConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, XMLGregorianCalendar dataConsegnaSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaFatturaRicevutaConsegnaInvioSDI(userContext, fatturaAttiva, codiceSdi, dataConsegnaSdi);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public Fattura_attivaBulk recuperoFatturaElettronicaDaNomeFile(UserContext userContext, String nomeFileInvioSdi) throws PersistencyException, ComponentException, it.cnr.jada.persistency.IntrospectionException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).recuperoFatturaElettronicaDaNomeFile(userContext, nomeFileInvioSdi);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public java.util.List recuperoFattureElettronicheSenzaNotificaConsegna(UserContext userContext, Unita_organizzativaBulk unita_organizzativaBulk) throws PersistencyException, ComponentException, it.cnr.jada.persistency.IntrospectionException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            List result = ((FatturaAttivaSingolaComponent) componentObj).recuperoFattureElettronicheSenzaNotificaConsegna(userContext, unita_organizzativaBulk);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public Fattura_attiva_IBulk aggiornaDatiFatturaSDI(UserContext userContext, String codiceInvioSdi, String statoInvioSdi, String noteInvioSdi, XMLGregorianCalendar dataConsegnaSdi, boolean stornaFattura) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attiva_IBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaDatiFatturaSDI(userContext, codiceInvioSdi, statoInvioSdi, noteInvioSdi, dataConsegnaSdi, stornaFattura);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public Fattura_attiva_IBulk aggiornaDatiFatturaSDI(UserContext userContext, Fattura_attiva_IBulk fatturaAttiva, String statoInvioSdi, String noteInvioSdi, XMLGregorianCalendar dataConsegnaSdi, boolean stornaFattura) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attiva_IBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaDatiFatturaSDI(userContext, fatturaAttiva, statoInvioSdi, noteInvioSdi, dataConsegnaSdi, stornaFattura);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}
    }

    public boolean isAttivoSplitPayment(UserContext param0, Timestamp param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            boolean result = (((FatturaAttivaSingolaComponent) componentObj).isAttivoSplitPayment(param0, param1));
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

    public Fattura_attivaBulk aggiornaFatturaTrasmissioneNonRecapitataSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceInvioSdi, String noteSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            Fattura_attivaBulk result = ((FatturaAttivaSingolaComponent) componentObj).aggiornaFatturaTrasmissioneNonRecapitataSDI(userContext, fattura, codiceInvioSdi, noteSdi);
            component_invocation_succes(userContext, componentObj);
		return result;
        } catch (it.cnr.jada.comp.NoRollbackException e) {
            component_invocation_succes(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.comp.ComponentException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (it.cnr.jada.persistency.PersistencyException e) {
            component_invocation_failure(userContext, componentObj);
		throw e;
        } catch (RuntimeException e) {
            throw uncaughtRuntimeException(userContext, componentObj, e);
        } catch (Error e) {
            throw uncaughtError(userContext, componentObj, e);
	}

    }

    public BigDecimal getImportoBolloVirtuale(UserContext userContext, Fattura_attivaBulk fattura) throws ComponentException, java.rmi.RemoteException {
        pre_component_invocation(userContext, componentObj);
	try {
            BigDecimal result = ((FatturaAttivaSingolaComponent) componentObj).getImportoBolloVirtuale(userContext, fattura);
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

    public void controlliGestioneBolloVirtuale(UserContext param0, Fattura_attivaBulk fatturaAttiva, BulkList dettaglio) throws ComponentException, java.rmi.RemoteException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).controlliGestioneBolloVirtuale(param0, fatturaAttiva, dettaglio);
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

    public String recuperoEmailUtente(UserContext param0, Fattura_attivaBulk fatturaAttiva) throws ComponentException, java.rmi.RemoteException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            String res = ((FatturaAttivaSingolaComponent) componentObj).recuperoEmailUtente(param0, fatturaAttiva);
            component_invocation_succes(param0, componentObj);
		return res;
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

    public void gestioneAvvisoInvioMailFattureAttive(UserContext param0) throws ComponentException, java.rmi.RemoteException, javax.ejb.EJBException {
        pre_component_invocation(param0, componentObj);
	try {
            ((FatturaAttivaSingolaComponent) componentObj).gestioneAvvisoInvioMailFattureAttive(param0);
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


}