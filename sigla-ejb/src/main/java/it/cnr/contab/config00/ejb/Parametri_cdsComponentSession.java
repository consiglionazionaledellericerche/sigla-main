package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

@Remote
public interface Parametri_cdsComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	it.cnr.contab.config00.bulk.Parametri_cdsBulk getParametriCds(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
