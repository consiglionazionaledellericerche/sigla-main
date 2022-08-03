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

package it.cnr.contab.logs.ejb;

import it.cnr.contab.logs.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.ejb.CRUDComponentSession;
import it.cnr.jada.util.*;

import javax.ejb.EJBObject;
import javax.ejb.Remote;


import java.rmi.RemoteException;
@Remote
public interface BatchControlComponentSession extends CRUDComponentSession{
	
	Batch_controlBulk attivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk) throws RemoteException, ComponentException;

	Batch_controlBulk disattivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk) throws RemoteException, ComponentException;
	
    RemoteIterator listaBatch_control_jobs(UserContext usercontext) throws RemoteException, ComponentException;
    
    Batch_controlBulk inizializzaParametri(UserContext usercontext, Batch_controlBulk batch_controlbulk) throws RemoteException, ComponentException;

    OggettoBulk creaConBulkRequiresNew(UserContext usercontext, OggettoBulk oggettoBulk) throws RemoteException, ComponentException;
    OggettoBulk modificaConBulkRequiresNew(UserContext usercontext, OggettoBulk oggettoBulk) throws RemoteException, ComponentException;
}