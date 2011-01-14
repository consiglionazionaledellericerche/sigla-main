package it.cnr.contab.docamm00.comp;

import javax.ejb.Remote;

import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk;
/**
 * Insert the type's description here.
 * Creation date: (2/13/2002 2:18:48 PM)
 * @author: Roberto Peli
 */
@Remote
public interface DocumentoAmministrativoComponentSession {
/**
 * Insert the method's description here.
 * Creation date: (2/13/2002 2:19:18 PM)
 */
public IDocumentoAmministrativoRigaBulk update(
	it.cnr.jada.UserContext param0, 
	IDocumentoAmministrativoRigaBulk param1)
	throws	it.cnr.jada.comp.ComponentException,
			java.rmi.RemoteException;
public IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(
	it.cnr.jada.UserContext userContext, 
	IScadenzaDocumentoContabileBulk scadenza)
	throws	it.cnr.jada.comp.ComponentException, 
			java.rmi.RemoteException;
}
