package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface Parametri_cnrComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	boolean isLivelloPdgDecisionaleSpeEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.bulk.Parametri_cnrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isLivelloPdgDecisionaleEtrEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.bulk.Parametri_cnrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isLivelloPdgContrattazioneSpeEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.bulk.Parametri_cnrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.bulk.Parametri_cnrBulk getParametriCnr(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isCofogObbligatorio(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
