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

package it.cnr.contab.compensi00.ejb;
import it.cnr.contab.compensi00.docs.bulk.BonusBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Acconto_classific_coriBulk;
import it.cnr.contab.docamm00.ejb.IDocumentoAmministrativoSpesaComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.Remote;
@Remote
public interface CompensoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession, it.cnr.jada.ejb.PrintComponentSession, IDocumentoAmministrativoSpesaComponentSession {
void aggiornaMontanti(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk aggiornaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Long assegnaProgressivo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Long assegnaProgressivoTemporaneo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk confermaModificaCORI(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.compensi00.docs.bulk.Contributo_ritenutaBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.ConguaglioBulk conguaglioAssociatoACompenso(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk doContabilizzaCompensoCofi(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk doElaboraCUD(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.EstrazioneCUDVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk elaboraScadenze(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param2,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void eliminaCompensoTemporaneo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,java.lang.Long param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk eliminaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk eseguiCalcolo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaBanche(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findModalita(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTermini(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTipiRapporto(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTipiTrattamento(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Collection findTipiPrestazioneCompenso(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.sql.Timestamp getDataOdierna(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerMinicarriera(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.compensi00.docs.bulk.MinicarrieraBulk param2,java.util.List param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerMissione(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.missioni00.docs.bulk.MissioneBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk inserisciCompenso(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isCompensoAnnullato(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List loadDocContAssociati(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk onTipoTrattamentoChange(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk onTipoRapportoInpsChange(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk reloadCompenso(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk resyncScadenza(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void rollbackToSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void setSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaObbligazione(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param1,it.cnr.jada.bulk.OggettoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
int validaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,boolean param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk doElabora770(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.Estrazione770Bulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk doElaboraINPSMensile(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.EstrazioneINPSMensileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean verificaEsistenzaCompenso(UserContext param0, CompensoBulk param1) throws ComponentException, RemoteException;
boolean isCompensoValido(UserContext param0, CompensoBulk param1) throws ComponentException, RemoteException;
boolean isTerzoCervellone(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isEsercizioChiusoPerDataCompetenza(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2) throws ComponentException, it.cnr.jada.persistency.PersistencyException, java.rmi.RemoteException;
boolean isAccontoAddComOkPerContabil(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isGestiteDeduzioniIrpef(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk completaIncarico(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
public BigDecimal prendiUtilizzato(it.cnr.jada.UserContext param0,it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_annoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
Acconto_classific_coriBulk doCalcolaAccontoAddCom(it.cnr.jada.UserContext param0,Acconto_classific_coriBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaCompensiSIP(UserContext userContext,String query,String dominio,String uo,String terzo,String tipoRicerca,String string, String string2, String string3, Timestamp data_inizio,Timestamp data_fine)throws ComponentException,java.rmi.RemoteException;
CompensoBulk inizializzaCompensoPerBonus(UserContext userContext,CompensoBulk compenso, BonusBulk bonus) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isSospensioneIrpefOkPerContabil(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void archiviaStampa(UserContext userContext, Date fromDate, Date untilDate, CompensoBulk compensoBulk, Integer... years)throws ComponentException,java.rmi.RemoteException;
CompensoBulk ricercaCompensoTrovato(it.cnr.jada.UserContext userContext,Long esercizio,String cd_cds,String cd_unita_organizzativa,Long pg_compenso)throws ComponentException,java.rmi.RemoteException,PersistencyException;
CompensoBulk ricercaCompensoByKey(it.cnr.jada.UserContext userContext,Long esercizio,String cd_cds,String cd_unita_organizzativa,Long pg_compenso)throws ComponentException,java.rmi.RemoteException,PersistencyException;
java.util.List<CompensoBulk> ricercaCompensiTrovato(it.cnr.jada.UserContext userContext,Long trovato)throws ComponentException,java.rmi.RemoteException,PersistencyException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk inizializzaCompensoPerFattura(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1,it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.compensi00.docs.bulk.CompensoBulk valorizzaInfoDocEle(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.docs.bulk.CompensoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;

}
