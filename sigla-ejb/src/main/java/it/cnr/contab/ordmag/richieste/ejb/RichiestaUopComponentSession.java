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

package it.cnr.contab.ordmag.richieste.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.Remote;

import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Remote
public interface RichiestaUopComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	Boolean isUtenteAbilitatoRichiesta(UserContext usercontext, RichiestaUopBulk richiesta) throws RemoteException,ComponentException, PersistencyException, EJBException;
	Boolean isUtenteAbilitatoValidazioneRichiesta(UserContext usercontext, RichiestaUopBulk richiesta) throws RemoteException,ComponentException, PersistencyException, EJBException;
	RichiestaUopBulk completaRichiesta(UserContext userContext, RichiestaUopBulk richiesta) throws RemoteException,ComponentException, PersistencyException;
}
