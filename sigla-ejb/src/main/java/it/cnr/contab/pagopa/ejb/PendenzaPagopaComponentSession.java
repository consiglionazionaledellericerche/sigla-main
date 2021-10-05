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
import it.cnr.contab.doccont00.intcass.giornaliera.MovimentoContoEvidenzaBulk;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.contab.pagopa.model.Pendenza;
import it.cnr.contab.pagopa.model.pagamento.NotificaPagamento;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

import javax.ejb.Remote;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;

/**
 * Remote interface for Enterprise Bean: CNRBOLLO00_EJB_TipoAttoBolloComponentSession
 */
@Remote
public interface PendenzaPagopaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
    public PendenzaPagopaBulk generaPosizioneDebitoria(UserContext userContext, IDocumentoAmministrativoBulk documentoAmministrativoBulk, Timestamp dataScadenza, String descrizione, BigDecimal importoScadenza, TerzoBulk terzoBulk) throws RemoteException,  ComponentException;
    public byte[] stampaAvviso(UserContext userContext, PendenzaPagopaBulk pendenzaPagopaBulk) throws RemoteException,  ComponentException;
    public byte[] stampaRt(UserContext userContext, PendenzaPagopaBulk pendenzaPagopaBulk) throws RemoteException,  ComponentException;
    public Pendenza getPendenza(UserContext userContext, String numeroAvviso) throws RemoteException,  ComponentException, IntrospectionException, PersistencyException;
    public NotificaPagamento notificaPagamento(UserContext userContext, NotificaPagamento notificaPagamento, String iuv) throws RemoteException,  ComponentException, IntrospectionException, PersistencyException;
    public RemoteIterator cercaPagamenti(UserContext aUC, PendenzaPagopaBulk pendenzaPagopaBulk) throws RemoteException, ComponentException;
    public void riconciliaIncassoPagopa(UserContext userContext, MovimentoContoEvidenzaBulk movimentoContoEvidenzaBulk) throws RemoteException, ComponentException ;
}
