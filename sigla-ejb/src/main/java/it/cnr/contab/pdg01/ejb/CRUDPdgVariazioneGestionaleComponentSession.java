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

package it.cnr.contab.pdg01.ejb;

import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

import java.rmi.RemoteException;

import javax.ejb.Remote;

@Remote
public interface CRUDPdgVariazioneGestionaleComponentSession extends it.cnr.contab.pdg00.ejb.PdGVariazioniComponentSession {
	void aggiungiDettaglioVariazione(it.cnr.jada.UserContext param0, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1, it.cnr.contab.prevent00.bulk.V_assestatoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk generaVariazioneBilancio(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	it.cnr.jada.bulk.OggettoBulk esitaVariazioneBilancio(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;	
	it.cnr.contab.prevent00.bulk.V_assestatoBulk trovaAssestato(it.cnr.jada.UserContext param0, it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	void allineaSaldiVariazioneApprovata(UserContext userContext, Ass_pdg_variazione_cdrBulk ass) throws ComponentException, RemoteException;
	Pdg_variazioneBulk generaVariazioneAutomaticaDaObbligazione(UserContext userContext, ObbligazioneBulk obbligazione) throws ComponentException, RemoteException;
}
