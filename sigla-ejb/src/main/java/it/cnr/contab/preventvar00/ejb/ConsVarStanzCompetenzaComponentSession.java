package it.cnr.contab.preventvar00.ejb;

import java.rmi.RemoteException;

import javax.ejb.Remote;

import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_var_pdggBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;
@Remote
public interface ConsVarStanzCompetenzaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk findParametriLivelli(it.cnr.jada.UserContext param0, java.lang.Integer param1)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	java.lang.String getDsLivelloClassificazione(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	java.lang.String getDsLivelloClassificazione(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2, java.lang.Integer param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	RemoteIterator findVariazioni(UserContext param0, V_cons_var_pdggBulk param1) throws PersistencyException, IntrospectionException, ComponentException, RemoteException;
}
