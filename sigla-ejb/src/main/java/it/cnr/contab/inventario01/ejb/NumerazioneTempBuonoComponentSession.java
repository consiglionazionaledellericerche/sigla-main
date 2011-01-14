package it.cnr.contab.inventario01.ejb;

import javax.ejb.Remote;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
@Remote
public interface NumerazioneTempBuonoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	java.lang.Long getNextTempPG(it.cnr.jada.UserContext param0,Buono_carico_scaricoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}