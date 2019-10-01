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

package it.cnr.contab.bilaterali00.ejb;

import javax.ejb.Remote;

/**
 * Remote interface for Enterprise Bean: CNRBILATERALI00_EJB_BltAccordiComponentSession
 */
@Remote
public interface BltAccordiComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk setComuneEnteDiAppartenenza(it.cnr.jada.UserContext param0,it.cnr.contab.bilaterali00.bulk.Blt_autorizzatiBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk setComuneEnteResponsIta(it.cnr.jada.UserContext param0,it.cnr.contab.bilaterali00.bulk.Blt_progettiBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
