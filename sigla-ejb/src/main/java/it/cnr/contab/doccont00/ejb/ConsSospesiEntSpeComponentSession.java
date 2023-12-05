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

import java.rmi.RemoteException;

import javax.ejb.Remote;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.ejb.GenericComponentSession;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
@Remote
public interface ConsSospesiEntSpeComponentSession extends GenericComponentSession {
	it.cnr.jada.util.RemoteIterator findConsSospesiSpesa(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4) throws ComponentException, RemoteException, IntrospectionException;
	it.cnr.jada.util.RemoteIterator findConsSospesiEntrata(UserContext param0,String param1,String param2,CompoundFindClause param3,CompoundFindClause param4) throws ComponentException, RemoteException, IntrospectionException;
	}

