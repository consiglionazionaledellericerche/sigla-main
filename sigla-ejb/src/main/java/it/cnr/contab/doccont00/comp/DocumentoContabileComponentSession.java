package it.cnr.contab.doccont00.comp;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.*;

import java.util.*;

import javax.ejb.Remote;
/**
 * Insert the type's description here.
 * Creation date: (2/8/2002 1:09:44 PM)
 * @author: Simonetta Costa
 */
@Remote
public interface DocumentoContabileComponentSession {
public void aggiornaCogeCoanInDifferita(
	UserContext userContext, 
	IDocumentoContabileBulk docContabile,
	Map values)
	throws	it.cnr.jada.comp.ComponentException,
			java.rmi.RemoteException;
public void aggiornaSaldiInDifferita( UserContext userContext, IDocumentoContabileBulk docContabile,  Map values, OptionRequestParameter param ) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
public void callRiportaAvanti (UserContext userContext,IDocumentoContabileBulk doc) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
public void callRiportaIndietro (UserContext userContext,IDocumentoContabileBulk doc) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
public void lockScadenza(
	UserContext userContext,
	IScadenzaDocumentoContabileBulk scadenza)
	throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
