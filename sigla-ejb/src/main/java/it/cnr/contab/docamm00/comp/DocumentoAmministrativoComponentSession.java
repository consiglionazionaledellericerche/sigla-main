/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
