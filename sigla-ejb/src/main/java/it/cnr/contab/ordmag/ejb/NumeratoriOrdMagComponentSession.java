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

package it.cnr.contab.ordmag.ejb;

import javax.ejb.Remote;

import it.cnr.contab.ordmag.anag00.NumerazioneMagBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;

@Remote
public interface NumeratoriOrdMagComponentSession  extends it.cnr.jada.ejb.CRUDComponentSession {
java.lang.Long getNextPG(it.cnr.jada.UserContext param0,NumerazioneMagBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Integer getNextPG(it.cnr.jada.UserContext param0,NumerazioneOrdBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
