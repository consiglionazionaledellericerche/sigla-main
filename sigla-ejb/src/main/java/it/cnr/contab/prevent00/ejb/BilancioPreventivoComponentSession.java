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

package it.cnr.contab.prevent00.ejb;

import javax.ejb.Remote;

@Remote
public interface BilancioPreventivoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk approvaBilancioPreventivo(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk caricaBilancioPreventivo(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk cercaCdsEnte(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk cercaCdsEnte(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void creaStanziamentiInizialiCNR(it.cnr.jada.UserContext param0,short param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void creaStanziamentiInizialiCdS(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdsBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk predisponeBilancioPreventivoCNR(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk predisponeBilancioPreventivoCdS(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Bilancio_preventivoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
