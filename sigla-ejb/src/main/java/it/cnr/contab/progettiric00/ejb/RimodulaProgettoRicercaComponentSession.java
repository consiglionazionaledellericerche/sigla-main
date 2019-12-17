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

package it.cnr.contab.progettiric00.ejb;

import java.util.List;

import javax.ejb.Remote;

import it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;

@Remote
public interface RimodulaProgettoRicercaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	Progetto_rimodulazioneBulk salvaDefinitivo(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Progetto_rimodulazioneBulk approva(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1,OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Progetto_rimodulazioneBulk respingi(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Progetto_rimodulazioneBulk riportaDefinitivo(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Progetto_rimodulazioneBulk riportaProvvisorio(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Progetto_rimodulazioneBulk valida(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	List<OggettoBulk> constructVariazioniBilancio(it.cnr.jada.UserContext param0,Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Progetto_rimodulazioneBulk rebuildRimodulazione(UserContext userContext, Progetto_rimodulazioneBulk rimodulazione) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}