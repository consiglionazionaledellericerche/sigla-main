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

package it.cnr.contab.doccont00.ejb;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

import javax.ejb.Remote;

import it.cnr.contab.doccont00.core.DatiFinanziariScadenzeDTO;

@Remote
public interface ObbligazioneComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession, it.cnr.jada.ejb.PrintComponentSession {
void aggiornaCogeCoanInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk aggiornaScadenzaSuccessivaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void cancellaObbligazioneProvvisoria(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk confermaObbligazioneProvvisoria(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.ordine.bulk.OrdineBulk findOrdineFor(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk generaDettagliScadenzaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List generaProspettoSpeseObbligazione(it.cnr.jada.UserContext param0,java.util.List param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk listaCapitoliPerCdsVoce(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Vector listaCdrPerCapitoli(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Vector listaLineeAttivitaPerCapitoliCdr(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void lockScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk stornaObbligazioneDefinitiva(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void verificaNuovaLineaAttivita(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.WorkpackageBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void verificaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk verificaScadenzarioObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.config00.esercizio.bulk.EsercizioBulk verificaStatoEsercizio(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.Integer param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void verificaTestataObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaContratto(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1, it.cnr.contab.config00.contratto.bulk.ContrattoBulk param2, it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException;
it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk riportaSelezioneVoci(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1, java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk creaScadenzaResiduale(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk validaImputazioneFinanziaria(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1, java.math.BigDecimal param2)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1, DatiFinanziariScadenzeDTO dati)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomaticoLight(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1, DatiFinanziariScadenzeDTO dati)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException;
it.cnr.jada.bulk.PrimaryKeyHashtable getOldRipartizioneCdrVoceLinea(it.cnr.jada.UserContext userContext, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaIncaricoRepertorio(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1, it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk param2, it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException;
List<it.cnr.contab.prevent00.bulk.V_assestatoBulk> listaAssestatoSpese (it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException;
boolean existAssElementoVoceNew(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk param1) throws RemoteException,it.cnr.jada.comp.ComponentException;
void callRiportaAvantiRequiresNew(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportaIndietroRequiresNew(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
