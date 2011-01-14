/*
 * Created on Feb 22, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.cnr.contab.util00.ejb;

import javax.ejb.Remote;

/**
 * @author mincarnato
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@Remote
public interface ProcedureComponentSession extends it.cnr.jada.ejb.GenericComponentSession {
	 void aggiornaApprovazioneFormale(it.cnr.jada.UserContext param0, java.util.List param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	 void aggiornaApprovazioneFormale(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	 void aggiornaApponiVisto(it.cnr.jada.UserContext param0, java.util.List param1, it.cnr.contab.config00.sto.bulk.DipartimentoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	 void aggiornaApponiVisto(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.contab.config00.sto.bulk.DipartimentoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	 it.cnr.contab.preventvar00.bulk.Var_bilancioBulk salvaDefinitivo(it.cnr.jada.UserContext param0,it.cnr.contab.preventvar00.bulk.Var_bilancioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
