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

package it.cnr.contab.ordmag.magazzino.ejb;

import it.cnr.contab.inventario00.docs.bulk.Transito_beni_ordiniBulk;
import it.cnr.contab.ordmag.magazzino.bulk.*;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.RemoteIterator;

import javax.ejb.Remote;
import java.rmi.RemoteException;
import java.util.List;

@Remote
public interface TransitoBeniOrdiniComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
    public Transito_beni_ordiniBulk gestioneTransitoInventario(UserContext userContext, MovimentiMagBulk movimentoCarico)  throws ComponentException, PersistencyException, RemoteException, ApplicationException;
}
