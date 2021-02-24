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

import javax.ejb.Remote;

import it.cnr.contab.prevent01.bulk.Pdg_esercizioBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.UserContext;

import java.util.List;

@Remote
public interface ProgettoRicercaComponentSession extends it.cnr.jada.ejb.CRUDComponentSession,it.cnr.jada.ejb.PrintComponentSession {
it.cnr.jada.util.RemoteIterator getChildren(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator getChildrenWorkpackage(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator getChildrenForSip(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk getParent(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isLeaf(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk getParentForSip(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isLeafForSip(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.progettiric00.core.bulk.ProgettoBulk cercaWorkpackages(it.cnr.jada.UserContext param0,it.cnr.contab.progettiric00.core.bulk.ProgettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaCancellazioneUoAssociata(it.cnr.jada.UserContext param0, it.cnr.contab.progettiric00.core.bulk.ProgettoBulk param1, it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaCancellazionePianoEconomicoAssociato(it.cnr.jada.UserContext param0, it.cnr.contab.progettiric00.core.bulk.ProgettoBulk param1, it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaCancellazioneVoceAssociataPianoEconomico(it.cnr.jada.UserContext param0, it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk param1, it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.progettiric00.core.bulk.ProgettoBulk initializePianoEconomico(it.cnr.jada.UserContext param0, it.cnr.contab.progettiric00.core.bulk.ProgettoBulk param1, boolean param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaPianoEconomico(it.cnr.jada.UserContext param0,it.cnr.contab.progettiric00.core.bulk.Progetto_rimodulazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Pdg_esercizioBulk getPdgEsercizio(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void removePianoEconomico(it.cnr.jada.UserContext param0,it.cnr.contab.progettiric00.core.bulk.ProgettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaConBulk(UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, boolean param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
List<ProgettoBulk> getAllChildren(it.cnr.jada.UserContext param0, ProgettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}