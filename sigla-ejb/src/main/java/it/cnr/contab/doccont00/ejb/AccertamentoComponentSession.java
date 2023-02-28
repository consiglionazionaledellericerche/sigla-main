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

import javax.ejb.Remote;

import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
@Remote
public interface AccertamentoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession, it.cnr.jada.ejb.PrintComponentSession {
void aggiornaCogeCoanInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void aggiornaSaldiInDifferita(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1,java.util.Map param2,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.AccertamentoBulk aggiornaScadenzarioSuccessivoAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.AccertamentoBulk annullaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void callRiportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk cancellaDettagliScadenze(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.AccertamentoBulk creaAccertamentoDiSistema(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.MandatoAccreditamento_rigaBulk param1,it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.AccertamentoBulk generaDettagliScadenzaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Vector listaCodiciNaturaPerCapitolo(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Vector listaLineeAttivitaPerCapitolo(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void lockScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk modificaScadenzaInAutomatico(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1,java.math.BigDecimal param2,boolean param3, Boolean aggiornaCalcoloAutomatico) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void verificaAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.AccertamentoBulk verificaScadenzarioAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.AccertamentoBulk verificaScadenzarioAccertamento(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param1, boolean param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Vector listaCdrPerCapitoli (it.cnr.jada.UserContext aUC,it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaContratto(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1, it.cnr.contab.config00.contratto.bulk.ContrattoBulk param2, it.cnr.jada.persistency.sql.CompoundFindClause param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk sdoppiaScadenzaInAutomatico(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1, java.math.BigDecimal param2)  throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException,it.cnr.jada.persistency.PersistencyException;
PrimaryKeyHashtable getOldRipartizioneCdrVoceLinea(it.cnr.jada.UserContext userContext, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.AccertamentoBulk aggiornaAccertamentiTemporanei(UserContext param0, it.cnr.contab.doccont00.core.bulk.AccertamentoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
}
