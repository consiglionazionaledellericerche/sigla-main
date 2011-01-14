package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface Parametri_livelliComponentSession
extends it.cnr.jada.ejb.CRUDComponentSession 
{
	boolean isParametriLivelliEtrEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean isParametriLivelliSpeEnabled(it.cnr.jada.UserContext param0, it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk getParametriLivelli(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	/**
	 * Restituisce la descrizione del livello di Entrata/Spesa definito nei Parametri CNR
	 * @param userContext
	 * @param esercizio
	 * @return
	 * @throws ComponentException
	 */
	String getDescrizioneLivello(it.cnr.jada.UserContext param0, Integer param1, String param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;			
	String getDescrizioneLivelloContrSpese(it.cnr.jada.UserContext param0, Integer param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
