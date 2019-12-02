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

package it.cnr.contab.compensi00.ejb;

import javax.ejb.Remote;

import it.cnr.contab.compensi00.tabrif.bulk.Esenzioni_addizionaliBulk;
import it.cnr.jada.UserContext;
@Remote
public interface Esenzioni_addizionaliComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	Esenzioni_addizionaliBulk verifica_aggiornamento(UserContext usercontext, Esenzioni_addizionaliBulk bulk) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void Aggiornamento(UserContext usercontext, Esenzioni_addizionaliBulk bulk) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
		void cancella_pendenti(UserContext usercontext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
