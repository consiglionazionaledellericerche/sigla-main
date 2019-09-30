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

package it.cnr.contab.compensi00.ejb;


import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_crBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_cr_detBulk;
import it.cnr.jada.UserContext;
@Remote
public interface GruppoCRComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	void CreaperTutteUOSAC(UserContext context, Gruppo_crBulk bulk)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Gruppo_cr_detBulk completaTerzo(UserContext context, Gruppo_cr_detBulk bulk,TerzoBulk terzo)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	List findListaBanche (UserContext userContext,Gruppo_cr_detBulk det) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	Collection findModalitaOptions (UserContext userContext,Gruppo_cr_detBulk det) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void validaCancellazioneDettaglio(UserContext userContext, Gruppo_cr_detBulk det) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean controllaEsistenzaGruppo(UserContext userContext, Gruppo_crBulk gruppo) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
