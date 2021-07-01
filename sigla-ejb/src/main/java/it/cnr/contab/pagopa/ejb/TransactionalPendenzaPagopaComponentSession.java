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

package it.cnr.contab.pagopa.ejb;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.contab.pagopa.model.Pendenza;
import it.cnr.contab.pagopa.model.pagamento.NotificaPagamento;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;

public class TransactionalPendenzaPagopaComponentSession extends it.cnr.jada.ejb.TransactionalCRUDComponentSession implements PendenzaPagopaComponentSession {
    public PendenzaPagopaBulk generaPosizioneDebitoria(UserContext userContext, IDocumentoAmministrativoBulk documentoAmministrativoBulk, Timestamp dataScadenza, String descrizione, BigDecimal importoScadenza, TerzoBulk terzoBulk) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (PendenzaPagopaBulk) invoke("generaPosizioneDebitoria", new Object[]{
                    userContext,
                    documentoAmministrativoBulk,
                    dataScadenza, descrizione, importoScadenza, terzoBulk});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public byte[] stampaAvviso(UserContext userContext, PendenzaPagopaBulk pendenzaPagopaBulk) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (byte[]) invoke("stampaAvviso", new Object[]{
                    userContext,
                    pendenzaPagopaBulk});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public byte[] stampaRt(UserContext userContext, PendenzaPagopaBulk pendenzaPagopaBulk) throws RemoteException, it.cnr.jada.comp.ComponentException {
        try {
            return (byte[]) invoke("stampaRt", new Object[]{
                    userContext,
                    pendenzaPagopaBulk});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public Pendenza getPendenza(UserContext userContext, String numeroAvviso) throws RemoteException, ComponentException, IntrospectionException, PersistencyException {
        try {
            return (Pendenza) invoke("getPendenza", new Object[]{
                    userContext,
                    numeroAvviso});
        } catch (RemoteException e) {
            throw e;
        } catch (java.lang.reflect.InvocationTargetException e) {
            try {
                throw e.getTargetException();
            } catch (it.cnr.jada.comp.ComponentException ex) {
                throw ex;
            } catch (Throwable ex) {
                throw new RemoteException("Uncaugth exception", ex);
            }
        }
    }

    public NotificaPagamento notificaPagamento(UserContext userContext, NotificaPagamento notificaPagamento, String iuv) throws RemoteException, ComponentException, IntrospectionException, PersistencyException{
            try {
                return (NotificaPagamento) invoke("notificaPagamento", new Object[]{
                        userContext,
                        notificaPagamento,
                        iuv});
            } catch (RemoteException e) {
                throw e;
            } catch (java.lang.reflect.InvocationTargetException e) {
                try {
                    throw e.getTargetException();
                } catch (it.cnr.jada.comp.ComponentException ex) {
                    throw ex;
                } catch (Throwable ex) {
                    throw new RemoteException("Uncaugth exception", ex);
                }
            }
        }
    public RemoteIterator cercaPagamenti(UserContext userContext, PendenzaPagopaBulk pendenzaPagopaBulk) throws RemoteException, ComponentException{
            try {
                return (RemoteIterator) invoke("cercaPagamenti", new Object[]{
                        userContext,
                        pendenzaPagopaBulk});
            } catch (RemoteException e) {
                throw e;
            } catch (java.lang.reflect.InvocationTargetException e) {
                try {
                    throw e.getTargetException();
                } catch (it.cnr.jada.comp.ComponentException ex) {
                    throw ex;
                } catch (Throwable ex) {
                    throw new RemoteException("Uncaugth exception", ex);
                }
            }
        }
    }
