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

package it.cnr.contab.doccont00.ejb;

import javax.ejb.Remote;

@Remote
public interface AccertamentoAbstractComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession {
void aggiornaCogeCoanInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void lockScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3, Boolean aggiornaCalcoloAutomatico) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void rollbackToSavePoint(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void setSavePoint(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1, java.math.BigDecimal param2)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException;
}
