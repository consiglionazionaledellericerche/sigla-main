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

package it.cnr.contab.pdg00.ejb;

import javax.ejb.Remote;

@Remote
public interface RicostruzioneResiduiComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession  {
it.cnr.contab.config00.sto.bulk.CdrBulk findCdrUo(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.sto.bulk.CdrBulk findCdr(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isUOScrivaniaEnte(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
public it.cnr.contab.pdg00.bulk.Pdg_residuoBulk calcolaDispCassaPerCds (it.cnr.jada.UserContext userContext, it.cnr.contab.pdg00.bulk.Pdg_residuoBulk residuo) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
public boolean isCdrSAC(it.cnr.jada.UserContext userContext, it.cnr.contab.config00.sto.bulk.CdrBulk cdr) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException, it.cnr.jada.persistency.PersistencyException;
public it.cnr.contab.pdg00.bulk.Pdg_residuoBulk caricaDettagliFiltrati(it.cnr.jada.UserContext userContext, it.cnr.jada.persistency.sql.CompoundFindClause clauses, it.cnr.contab.pdg00.bulk.Pdg_residuoBulk testata) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
