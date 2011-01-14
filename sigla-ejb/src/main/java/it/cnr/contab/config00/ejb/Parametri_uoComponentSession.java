package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface Parametri_uoComponentSession 
extends it.cnr.jada.ejb.CRUDComponentSession 
{
	boolean isFlGestioneModuliEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.bulk.Parametri_uoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.bulk.Parametri_uoBulk getParametriUo(it.cnr.jada.UserContext param0, java.lang.Integer param1, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
