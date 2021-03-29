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

package it.cnr.contab.reports.ejb;

import javax.ejb.Remote;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;

import java.rmi.RemoteException;

@Remote
public interface OfflineReportComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
void addJob(it.cnr.jada.UserContext param0,it.cnr.contab.reports.bulk.Print_spoolerBulk param1,it.cnr.jada.bulk.BulkList param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void deleteJobs(it.cnr.jada.UserContext param0,it.cnr.contab.reports.bulk.Print_spoolerBulk[] param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator queryJobs(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void cancellaSchedulazione(it.cnr.jada.UserContext userContext, Long pgStampa, String indirizzoEMail)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Print_spoolerBulk findPrintSpooler(it.cnr.jada.UserContext userContext, Long pgStampa)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String getLastServerActive(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean controllaStampeInCoda(it.cnr.jada.UserContext param0, it.cnr.contab.reports.bulk.Print_spoolerBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Print_spoolerBulk getJobWaitToJsoDS(it.cnr.jada.UserContext param0) throws RemoteException, it.cnr.jada.comp.ComponentException;
}