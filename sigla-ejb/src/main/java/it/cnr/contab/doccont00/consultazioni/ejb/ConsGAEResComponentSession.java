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

package it.cnr.contab.doccont00.consultazioni.ejb;

import javax.ejb.Remote;

@Remote
public interface ConsGAEResComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.jada.util.RemoteIterator findConsultazioneResEtr(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;		
	it.cnr.jada.util.RemoteIterator findConsultazioneResSpe(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,it.cnr.jada.persistency.sql.CompoundFindClause param3,it.cnr.jada.persistency.sql.CompoundFindClause param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;		
}
