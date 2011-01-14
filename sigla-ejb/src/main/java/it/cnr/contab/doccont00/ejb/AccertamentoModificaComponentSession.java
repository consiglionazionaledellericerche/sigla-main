/*
 * Created on Jun 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface AccertamentoModificaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	void cancellaVariazioneTemporanea(it.cnr.jada.UserContext param0, it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
}
