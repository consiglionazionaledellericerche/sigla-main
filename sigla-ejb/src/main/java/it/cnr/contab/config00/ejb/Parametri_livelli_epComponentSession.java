package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface Parametri_livelli_epComponentSession
extends it.cnr.jada.ejb.CRUDComponentSession 
{
	boolean isParametriLivelliEcoEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcep.cla.bulk.Parametri_livelli_epBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isParametriLivelliPatEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcep.cla.bulk.Parametri_livelli_epBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcep.cla.bulk.Parametri_livelli_epBulk getParametriLivelli(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	/**
	 * Restituisce la descrizione del livello di Entrata/Spesa definito nei Parametri CNR
	 * @param userContext
	 * @param esercizio
	 * @return
	 * @throws ComponentException
	 */
	String getDescrizioneLivello(it.cnr.jada.UserContext param0, Integer param1, String param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;			
	}
