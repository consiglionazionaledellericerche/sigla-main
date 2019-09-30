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

import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_gestBulk;
import it.cnr.contab.pdg01.bulk.Pdg_variazione_riga_spesa_gestBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

@Remote
public interface PdGVariazioniComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession {
it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk salvaDefinitivo(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk approva(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk respingi(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void inizializzaSommeCdR(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void controllaBilancioPreventivoCdsApprovato(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.sto.bulk.CdrBulk cercaCdrPrimoLivello(it.cnr.jada.UserContext param0,it.cnr.contab.config00.sto.bulk.CdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaAssociazioneCDRPerCancellazione(it.cnr.jada.UserContext param0, it.cnr.contab.pdg00.cdip.bulk.Ass_pdg_variazione_cdrBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String controllaTotPropostoEntrataSpesa(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaForPrintRiepilogo(it.cnr.jada.UserContext param0,it.cnr.jada.persistency.sql.CompoundFindClause param1,it.cnr.jada.bulk.OggettoBulk param2,it.cnr.jada.bulk.OggettoBulk param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isCdsAbilitatoAdApprovare(it.cnr.jada.UserContext param0,String param1, it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
String getDesTipoVariazione(it.cnr.jada.UserContext param0,it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk statoPrecedente(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk apponiVistoDipartimento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.config00.sto.bulk.DipartimentoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaVariazioniForApposizioneVisto(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
CompoundFindClause aggiornaClausole(UserContext context ,Pdg_variazioneBulk pdg,String tipo) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaVariazioniForDocumentale(it.cnr.jada.UserContext param0, it.cnr.jada.persistency.sql.CompoundFindClause param1, it.cnr.jada.bulk.OggettoBulk param2, String param3, Boolean param4) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void archiviaVariazioneDocumentale(it.cnr.jada.UserContext userContext,	Pdg_variazioneBulk bulk) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
byte[] lanciaStampa(it.cnr.jada.UserContext userContext,Integer esercizio,Integer pg_variazione,String tipo_variazione) throws PersistencyException, ComponentException,java.rmi.RemoteException, javax.ejb.EJBException;
void aggiornaDataFirma(UserContext userContext, Integer esercizio,Integer numeroVariazione) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isVariazioneFromLiquidazioneIvaDaModificare(UserContext userContext, Pdg_variazioneBulk variazione) throws ComponentException,java.rmi.RemoteException;
Pdg_variazione_riga_spesa_gestBulk recuperoRigaLiquidazioneIva(UserContext userContext, Ass_pdg_variazione_cdrBulk ass) throws ComponentException;
boolean isRigaLiquidazioneIva(UserContext userContext, Pdg_variazione_riga_gestBulk riga) throws ComponentException;
}
