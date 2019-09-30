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

import javax.ejb.Remote;

@Remote
public interface PdgAggregatoModuloComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession{
it.cnr.contab.config00.sto.bulk.CdrBulk cdrFromUserContext(it.cnr.jada.UserContext userContext) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaStatoPdg_aggregato(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isPdGAggregatoModificabile(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.prevent01.bulk.Pdg_moduloBulk findAndInsertBulkForMacro(it.cnr.jada.UserContext param0,it.cnr.contab.prevent01.bulk.Pdg_moduloBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk scaricaDipendentiSuPdGP(it.cnr.jada.UserContext userContext, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk annullaScaricaDipendentiSuPdGP(it.cnr.jada.UserContext userContext, it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk getCdrPdGP(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaCancellazionePdgModulo(it.cnr.jada.UserContext userContext, it.cnr.contab.prevent01.bulk.Pdg_moduloBulk pdg_modulo) throws  it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void stampaBilancioCallAggiornaDati(it.cnr.jada.UserContext userContext, it.cnr.contab.prevent01.bulk.Stampa_pdgp_bilancioBulk bulk, boolean aggPrevAC, boolean aggResiduiAC, boolean aggPrevAP, boolean aggResiduiAP, boolean aggCassaAC) throws it.cnr.jada.comp.ComponentException;
void stampaRendicontoCallAggiornaDati(it.cnr.jada.UserContext userContext, it.cnr.contab.prevent01.bulk.Stampa_pdgp_bilancioBulk bulk, boolean aggCompAC, boolean aggResiduiAC, boolean aggCassaAC, boolean aggCompAP, boolean aggResiduiAP, boolean aggCassaAP) throws it.cnr.jada.comp.ComponentException;
}
