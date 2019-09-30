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

package it.cnr.contab.prevent01.ejb;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.prevent01.bulk.Contrattazione_speseVirtualBulk;
import it.cnr.contab.prevent01.bulk.Pdg_approvato_dip_areaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.rmi.RemoteException;

import javax.ejb.Remote;
@Remote
public interface PdgContrSpeseComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
    OggettoBulk inizializzaDettagliBulkPerModifica(UserContext usercontext, Contrattazione_speseVirtualBulk contr_spese, Pdg_approvato_dip_areaBulk pdg_dip_area) throws ComponentException, RemoteException;
    boolean isApprovatoDefinitivo(UserContext usercontext) throws ComponentException, RemoteException;
	CdrBulk caricaCdrAfferenzaDaUo(UserContext userContext, Unita_organizzativaBulk uo) throws ComponentException, RemoteException, PersistencyException;
	Integer livelloContrattazioneSpese(UserContext userContext) throws ComponentException, PersistencyException, RemoteException;
	void approvaDefinitivamente(it.cnr.jada.UserContext param0) throws ComponentException, RemoteException;
	void undoApprovazioneDefinitiva(it.cnr.jada.UserContext param0) throws ComponentException, RemoteException;
}