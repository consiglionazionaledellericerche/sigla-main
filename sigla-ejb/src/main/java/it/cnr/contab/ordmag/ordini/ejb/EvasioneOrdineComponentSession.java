/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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

package it.cnr.contab.ordmag.ordini.ejb;

import java.util.List;

import javax.ejb.Remote;

import it.cnr.contab.ordmag.magazzino.bulk.BollaScaricoMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
@Remote
public interface EvasioneOrdineComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	EvasioneOrdineBulk cercaOrdini(it.cnr.jada.UserContext param0,EvasioneOrdineBulk evasioneOrdine) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	List<BollaScaricoMagBulk> evadiOrdine(UserContext userContext, EvasioneOrdineBulk evasioneOrdine) throws ComponentException, PersistencyException,java.rmi.RemoteException;
}
