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

package it.cnr.contab.pdg00.ejb;

import javax.ejb.Remote;

import it.cnr.contab.pdg00.cdip.bulk.V_cnr_estrazione_coriBulk;
import it.cnr.contab.pdg00.cdip.bulk.V_stipendi_cofi_dettBulk;
@Remote
public interface ElaboraFileStipendiComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	V_stipendi_cofi_dettBulk cercaFileStipendi(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	V_stipendi_cofi_dettBulk elaboraFile(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	V_stipendi_cofi_dettBulk annullaElaborazione(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;	
	it.cnr.jada.bulk.OggettoBulk cercaBatch(it.cnr.jada.UserContext param0,V_stipendi_cofi_dettBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	V_cnr_estrazione_coriBulk cercaCnrEstrazioneCori(it.cnr.jada.UserContext param0,V_cnr_estrazione_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	V_cnr_estrazione_coriBulk elaboraStralcioMensile(it.cnr.jada.UserContext param0,V_cnr_estrazione_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
	boolean esisteStralcioNegativo(it.cnr.jada.UserContext param0,V_cnr_estrazione_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
