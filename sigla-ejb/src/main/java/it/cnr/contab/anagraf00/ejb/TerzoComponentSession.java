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

package it.cnr.contab.anagraf00.ejb;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.Remote;

@Remote
public interface TerzoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.jada.util.RemoteIterator cercaBanchePerTerzoCessionario(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaModalita_pagamento_disponibiliByClause(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.TerzoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.TerzoBulk cercaTerzoPerUnitaOrganizzativa(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.TerzoBulk inizializzaTerzoPerUnitaOrganizzativa(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.TerzoBulk setComune_sede(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.TerzoBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaTerziSIP(it.cnr.jada.UserContext param0,String param1,String param2,String param3,String param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaTerziSIP_rendicontazione(it.cnr.jada.UserContext param0,String param1,String param2,String param3,String param4, Timestamp timestamp, Timestamp timestamp2,String Dip) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findNazioniIban(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.BancaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
TerzoBulk completaTerzo(UserContext userContext, TerzoBulk terzo) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
RemoteIterator cercaTerziPerUnitaOrganizzativa(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param1) throws ComponentException, RemoteException, PersistencyException;
}
