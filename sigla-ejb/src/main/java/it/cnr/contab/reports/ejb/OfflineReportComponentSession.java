package it.cnr.contab.reports.ejb;

import javax.ejb.Remote;

import it.cnr.contab.reports.bulk.Print_spoolerBulk;
@Remote
public interface OfflineReportComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
void addJob(it.cnr.jada.UserContext param0,it.cnr.contab.reports.bulk.Print_spoolerBulk param1,it.cnr.jada.bulk.BulkList param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void deleteJobs(it.cnr.jada.UserContext param0,it.cnr.contab.reports.bulk.Print_spoolerBulk[] param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator queryJobs(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void cancellaSchedulazione(it.cnr.jada.UserContext userContext, Long pgStampa, String indirizzoEMail)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Print_spoolerBulk findPrintSpooler(it.cnr.jada.UserContext userContext, Long pgStampa)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String getLastServerActive(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean controllaStampeInCoda(it.cnr.jada.UserContext param0, it.cnr.contab.reports.bulk.Print_spoolerBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}