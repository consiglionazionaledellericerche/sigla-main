package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface AssManualeMandatoReversaleComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
java.util.Collection loadReversaliAssociate(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List loadReversaliDisponibili(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
