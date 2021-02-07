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

package it.cnr.contab.anagraf00.ejb;

import java.sql.Timestamp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;

import javax.ejb.Remote;

@Remote
public interface AnagraficoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
java.lang.String calcolaCodiceFiscale(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk getAnagraficoEnte(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.TerzoBulk getDefaultTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk setComune_fiscale(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1,it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List bulkForSIP(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void eliminaBulkForSIP(it.cnr.jada.UserContext param0, String param1)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isItalianoEsteroModificabile(it.cnr.jada.UserContext param0,it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void checkConiugeAlreadyExistFor(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1, it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk param2)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isGestiteDeduzioniIrpef(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isGestiteDeduzioniFamily(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isGestiteDetrazioniAltre(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isGestiteDetrazioniFamily(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean esisteConiugeValido(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1, it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean esisteFiglioValido(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean esisteAltroFamValido(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List EstraiLista(UserContext userContext,Long prog_estrazione)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Long Max_prog_estrazione(UserContext userContext)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void Popola_ecf(UserContext userContext,Long prog_estrazione)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Timestamp findMaxDataCompValida(UserContext param0,AnagraficoBulk param1) throws IntrospectionException,PersistencyException, ComponentException,java.rmi.RemoteException;
boolean verificaStrutturaPiva(UserContext userContext, AnagraficoBulk anagrafico) throws ComponentException, it.cnr.jada.bulk.ValidationException,java.rmi.RemoteException;
void checkCaricoAlreadyExistFor(UserContext userContext,
		AnagraficoBulk anagrafico, Carico_familiare_anagBulk carico)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void controllaUnicitaCaricoInAnnoImposta(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1, it.cnr.contab.anagraf00.core.bulk.Carico_familiare_anagBulk param2)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isGestitoCreditoIrpef(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void aggiornaDatiAce(it.cnr.jada.UserContext param0, it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk param1)throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
