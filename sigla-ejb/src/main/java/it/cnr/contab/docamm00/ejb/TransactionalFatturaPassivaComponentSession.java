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

import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

public class TransactionalFatturaPassivaComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements FatturaPassivaComponentSession {
    public it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk addebitaDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk param1, java.util.List param2, java.util.Hashtable param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk) invoke("addebitaDettagli", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void aggiornaStatoDocumentiAmministrativi(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, java.lang.String param3, java.lang.Integer param4, java.lang.Long param5, java.lang.String param6) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("aggiornaStatoDocumentiAmministrativi", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3,
                    param4,
                    param5,
                    param6});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk calcoloConsuntivi(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk) invoke("calcoloConsuntivi", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk cercaCambio(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk) invoke("cercaCambio", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdC(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cercaDettagliFatturaPerNdC", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdD(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cercaDettagliFatturaPerNdD", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator cercaFatturaPerNdC(it.cnr.jada.UserContext param0, CompoundFindClause compoundfindclause, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cercaFatturaPerNdC", new Object[]{
                    param0,
                    compoundfindclause,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator cercaFatturaPerNdD(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cercaFatturaPerNdD", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("cercaObbligazioni", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk completaEnte(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk) invoke("completaEnte", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk completaFornitore(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, it.cnr.contab.anagraf00.core.bulk.TerzoBulk param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk) invoke("completaFornitore", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, java.util.Collection param2, it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk) invoke("contabilizzaDettagliSelezionati", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void controllaQuadraturaConti(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("controllaQuadraturaConti", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void controllaQuadraturaObbligazioni(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("controllaQuadraturaObbligazioni", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("creaConBulk", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk eliminaLetteraPagamentoEstero(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk) invoke("eliminaLetteraPagamentoEstero", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void eliminaRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("eliminaRiga", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.util.Vector estraeSezionali(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (java.util.Vector) invoke("estraeSezionali", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.anagraf00.core.bulk.TerzoBulk) invoke("findCessionario", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.util.List findDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
        try {
            return (java.util.List) invoke("findDettagli", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.IntrospectionException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.util.Collection findListabanche(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
        try {
            return (java.util.Collection) invoke("findListabanche", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.util.Collection findListabancheuo(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
        try {
            return (java.util.Collection) invoke("findListabancheuo", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator findNotaDiCreditoFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("findNotaDiCreditoFor", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator findNotaDiDebitoFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("findNotaDiDebitoFor", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator findObbligazioniFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, java.math.BigDecimal param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("findObbligazioniFor", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk findTerzoUO(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.anagraf00.core.bulk.TerzoBulk) invoke("findTerzoUO", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk findUOEnte(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk) invoke("findUOEnte", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public boolean hasFatturaPassivaARowNotInventoried(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((Boolean) invoke("hasFatturaPassivaARowNotInventoried", new Object[]{
                    param0,
                    param1})).booleanValue();
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public Boolean ha_beniColl(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((Boolean) invoke("ha_beniColl", new Object[]{
                    param0,
                    param1}));
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void inserisciRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("inserisciRiga", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public boolean isBeneServizioPerSconto(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((Boolean) invoke("isBeneServizioPerSconto", new Object[]{
                    param0,
                    param1})).booleanValue();
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.bulk.OggettoBulk) invoke("modificaConBulk", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void protocolla(it.cnr.jada.UserContext param0, java.sql.Timestamp param1, java.lang.Long param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("protocolla", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void rimuoviDaAssociazioniInventario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk param1, it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("rimuoviDaAssociazioniInventario", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk) invoke("riportaAvanti", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaIndietro(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk) invoke("riportaIndietro", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void rollbackToSavePoint(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("rollbackToSavePoint", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk selezionaValutaDiDefault(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk) invoke("selezionaValutaDiDefault", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.jada.util.RemoteIterator selectBeniFor(it.cnr.jada.UserContext param0, Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.jada.util.RemoteIterator) invoke("selectBeniFor", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk setContoEnteIn(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1, java.util.List param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk) invoke("setContoEnteIn", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void setSavePoint(it.cnr.jada.UserContext param0, java.lang.String param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("setSavePoint", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk stornaDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1, java.util.List param2, java.util.Hashtable param3) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk) invoke("stornaDettagli", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk update(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk) invoke("update", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk) invoke("updateImportoAssociatoDocAmm", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void validaFattura(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("validaFattura", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void validaRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("validaRiga", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void verificaEsistenzaEdAperturaInventario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("verificaEsistenzaEdAperturaInventario", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public boolean verificaStatoEsercizio(it.cnr.jada.UserContext param0, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((Boolean) invoke("verificaStatoEsercizio", new Object[]{
                    param0,
                    param1})).booleanValue();
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    //aggiunto per testare l'esercizio della data competenza da/a
    public boolean isEsercizioChiusoPerDataCompetenza(it.cnr.jada.UserContext param0, Integer param1, java.lang.String param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((Boolean) invoke("isEsercizioChiusoPerDataCompetenza", new Object[]{
                    param0,
                    param1,
                    param2})).booleanValue();
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void rimuoviDaAssociazioniInventario(it.cnr.jada.UserContext param0, it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("rimuoviDaAssociazioniInventario", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.util.Collection findListabanchedett(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException {
        try {
            return (java.util.Collection) invoke("findListabanchedett", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }


        }
    }

    public it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.anagraf00.core.bulk.TerzoBulk) invoke("findCessionario", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public List findListaFattureSIP(UserContext userContext, String query,
                                    String dominio, String uo, String terzo, String voce, String cdr,
                                    String gae, String tipoRicerca, Timestamp data_inizio, Timestamp data_fine) throws ComponentException,
            RemoteException {
        try {
            return ((List) invoke("findListaFattureSIP", new Object[]{
                    userContext, query, dominio, uo, terzo, voce, cdr, gae, tipoRicerca, data_inizio, data_fine}));
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }


        }
    }

    public java.util.List findManRevRigaCollegati(UserContext param0, Fattura_passiva_rigaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
        try {
            return (java.util.List) invoke("findManRevRigaCollegati", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.PersistencyException ex) {
                throw ex;
            } catch (it.cnr.jada.persistency.IntrospectionException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public OggettoBulk rebuildDocumento(UserContext param0, OggettoBulk param1)
            throws ComponentException, RemoteException {
        try {
            return (OggettoBulk) invoke("rebuildDocumento", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public Fattura_passivaBulk ricercaFatturaTrovato(it.cnr.jada.UserContext userContext,
                                                     Long esercizio, String cd_cds, String cd_unita_organizzativa,
                                                     Long pg_fattura) throws ComponentException, RemoteException {
        try {
            return (Fattura_passivaBulk) invoke("ricercaFatturaTrovato", new Object[]{
                    userContext,
                    esercizio,
                    cd_cds,
                    cd_unita_organizzativa,
                    pg_fattura});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public Fattura_passivaBulk ricercaFatturaByKey(it.cnr.jada.UserContext userContext,
                                                   Long esercizio, String cd_cds, String cd_unita_organizzativa,
                                                   Long pg_fattura) throws ComponentException, RemoteException {
        try {
            return (Fattura_passivaBulk) invoke("ricercaFatturaByKey", new Object[]{
                    userContext,
                    esercizio,
                    cd_cds,
                    cd_unita_organizzativa,
                    pg_fattura});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk> ricercaFattureTrovato(it.cnr.jada.UserContext userContext,
                                                                                                      Long trovato) throws ComponentException, RemoteException {
        try {
            return (java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk>) invoke("ricercaFattureTrovato", new Object[]{
                    userContext,
                    trovato});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public TrovatoBulk ricercaDatiTrovato(it.cnr.jada.UserContext userContext, Long trovato) throws ComponentException, java.rmi.RemoteException, PersistencyException {
        try {
            return (TrovatoBulk) invoke("ricercaDatiTrovato", new Object[]{userContext, trovato});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public TrovatoBulk ricercaDatiTrovatoValido(it.cnr.jada.UserContext userContext, Long trovato) throws ComponentException, java.rmi.RemoteException, PersistencyException {
        try {
            return (TrovatoBulk) invoke("ricercaDatiTrovatoValido", new Object[]{userContext, trovato});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void inserisciProgUnivoco(UserContext userContext,
                                     ElaboraNumUnicoFatturaPBulk lancio) throws ComponentException,
            RemoteException, PersistencyException {
        try {
            invoke("inserisciProgUnivoco", new Object[]{userContext, lancio});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }

    }

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk caricaAllegatiBulk(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk) invoke("caricaAllegatiBulk", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void validaFatturaPerCompenso(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            invoke("validaFatturaPerCompenso", new Object[]{
                    param0,
                    param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk valorizzaInfoDocEle(
            it.cnr.jada.UserContext param0,
            it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk) invoke(
                    "valorizzaInfoDocEle", new Object[]{param0, param1});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public void aggiornaObblSuCancPerCompenso(UserContext param0,
                                              Fattura_passivaBulk param1, java.util.Vector param2,
                                              OptionRequestParameter param3)
            throws ComponentException, RemoteException {
        try {
            invoke("aggiornaObblSuCancPerCompenso", new Object[]{
                    param0,
                    param1,
                    param2,
                    param3});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk eliminaLetteraPagamentoEstero(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, boolean param2) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk) invoke("eliminaLetteraPagamentoEstero", new Object[]{
                    param0,
                    param1,
                    param2});
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public boolean isAttivoSplitPayment(UserContext param0, Timestamp param1) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((Boolean) invoke("isAttivoSplitPayment", new Object[]{
                    param0,
                    param1})).booleanValue();
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public List<EvasioneOrdineRigaBulk> findContabilizzaRigaByClause(UserContext userContext,
                                                                     Fattura_passiva_rigaBulk fatturaPassivaRiga,
                                                                     CompoundFindClause findclause)
            throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return ((List<EvasioneOrdineRigaBulk>) invoke("findContabilizzaRigaByClause", new Object[]{
                    userContext,
                    fatturaPassivaRiga,
                    findclause}));
        } catch (java.rmi.RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new java.rmi.RemoteException("Uncaugth exception", ex);
            }
        }
    }

	@Override
	public boolean isAttivoSplitPaymentProf(UserContext param0,
			Timestamp param1) throws PersistencyException,
			ComponentException, RemoteException {
	       try {
	            return ((Boolean) invoke("isAttivoSplitPaymentProf", new Object[]{
	                    param0,
	                    param1})).booleanValue();
	        } catch (java.rmi.RemoteException e) {
	            throw e;
	        } catch (java.lang.reflect.InvocationTargetException e) {
	            try {
	                throw e.getTargetException();
	            } catch (it.cnr.jada.comp.ComponentException ex) {
	                throw ex;
	            } catch (Throwable ex) {
	                throw new java.rmi.RemoteException("Uncaugth exception", ex);
	            }
	        }
	}
}