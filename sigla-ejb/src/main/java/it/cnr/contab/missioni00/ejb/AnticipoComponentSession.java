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

package it.cnr.contab.missioni00.ejb;

import it.cnr.contab.docamm00.ejb.IDocumentoAmministrativoSpesaComponentSession;

import javax.ejb.Remote;

@Remote
public interface AnticipoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, IDocumentoAmministrativoSpesaComponentSession {
it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.AnticipoBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.AnticipoBulk creaRimborsoCompleto(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.AnticipoBulk gestisciCambioDataRegistrazione(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.sql.SQLException,java.rmi.RemoteException;
boolean isAnticipoAnnullato(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.AnticipoBulk riportaRimborsoAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.missioni00.docs.bulk.AnticipoBulk riportaRimborsoIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.missioni00.docs.bulk.AnticipoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void rollbackToSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void setSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1,it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
