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

package it.cnr.contab.docamm00.ejb;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.Remote;
import javax.xml.datatype.XMLGregorianCalendar;

import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;


@Remote
public interface FatturaAttivaSingolaComponentSession extends it.cnr.contab.docamm00.comp.DocumentoAmministrativoComponentSession, it.cnr.jada.ejb.CRUDComponentSession {
it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk addebitaDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk param1,java.util.List param2,java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void aggiornaStatoDocumentiAmministrativi(it.cnr.jada.UserContext param0,java.lang.String param1,java.lang.String param2,java.lang.String param3,java.lang.Integer param4,java.lang.Long param5,java.lang.String param6) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void annullaSelezionePerStampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk calcoloConsuntivi(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Long callGetPgPerProtocolloIVA(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Long callGetPgPerStampa(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void cancellaDatiPerProtocollazioneIva(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.lang.Long param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void cancellaDatiPerStampaIva(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.lang.Long param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_accertamentiVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk cercaCambio(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdC(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdD(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaFatturaPerNdC(it.cnr.jada.UserContext param0, CompoundFindClause compoundfindclause, it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaFatturaPerNdD(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk completaCliente(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk completaTerzo(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,it.cnr.contab.anagraf00.core.bulk.TerzoBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.util.Collection param2,it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void controllaQuadraturaAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void eliminaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean esistonoDatiPerProtocollazioneIva(it.cnr.jada.UserContext param0,java.lang.Long param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
boolean esistonoDatiPerStampaIva(it.cnr.jada.UserContext param0,java.lang.Long param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
java.util.Vector estraeSezionali(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.Vector estraeSezionaliPerRistampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.util.Vector param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator findAccertamentiFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.math.BigDecimal param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
java.util.Collection findListabanche(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
java.util.Vector findListabancheuo(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator findNotaDiCreditoFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator findNotaDiDebitoFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_IBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.tabrif.bulk.TariffarioBulk findTariffario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
boolean hasFatturaAttivaARowNotInventoried(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void inizializzaSelezionePerStampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void inserisciRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.lang.Integer modificaSelezionePerStampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4,java.lang.Long param5,java.lang.Integer param6,java.lang.Long param7,java.sql.Timestamp param8) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void protocolla(it.cnr.jada.UserContext param0,java.sql.Timestamp param1,java.lang.Long param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1,it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaIndietro(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void rollbackToSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_protocollabileVBulk selezionaTuttiPerStampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_protocollabileVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_ristampabileVBulk selezionaTuttiPerStampa(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_amm_ristampabileVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk setContoEnteIn(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1,java.util.List param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void setSavePoint(it.cnr.jada.UserContext param0,java.lang.String param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk stornaDettagli(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_attivaBulk param1,java.util.List param2,java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk update(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(it.cnr.jada.UserContext param0,it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaFattura(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void validaRiga(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void verificaEsistenzaEdAperturaInventario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean verificaStatoEsercizio(it.cnr.jada.UserContext param0,it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean isEsercizioChiusoPerDataCompetenza(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2) throws ComponentException, PersistencyException,java.rmi.RemoteException;
Boolean ha_beniColl(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
void rimuoviDaAssociazioniInventario(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaIBulk param1,it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param2) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator selectBeniFor(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk rebuildDocumento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
byte[] lanciaStampa(it.cnr.jada.UserContext userContext,java.lang.Long pg_stampa) throws PersistencyException, ComponentException,java.rmi.RemoteException;
it.cnr.jada.bulk.OggettoBulk completaOggetto(it.cnr.jada.UserContext aUC,it.cnr.jada.bulk.OggettoBulk oggetto) throws PersistencyException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
boolean verificaDuplicati(it.cnr.jada.UserContext aUC, it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk fatturaAttiva)throws PersistencyException,it.cnr.jada.comp.ComponentException,java.rmi.RemoteException;
java.util.List findListaModalitaPagamentoWS(it.cnr.jada.UserContext userContext,String terzo,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaBancheWS(it.cnr.jada.UserContext userContext,String terzo,String modalita,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaRigheperNCWS(it.cnr.jada.UserContext userContext,String uo,String terzo,String ti_causale,String esercizio,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk ricercaFattura(it.cnr.jada.UserContext userContext,Long esercizio,String cd_cds,String cd_unita_organizzativa,Long pg_fattura)throws ComponentException,java.rmi.RemoteException,PersistencyException;
it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk ricercaFatturaTrovato(it.cnr.jada.UserContext userContext,Long esercizio,String cd_cds,String cd_unita_organizzativa,Long pg_fattura)throws ComponentException,java.rmi.RemoteException,PersistencyException;
it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk ricercaFatturaByKey(it.cnr.jada.UserContext userContext,Long esercizio,String cd_cds,String cd_unita_organizzativa,Long pg_fattura)throws ComponentException,java.rmi.RemoteException,PersistencyException;
java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk> ricercaFattureTrovato(it.cnr.jada.UserContext userContext,Long trovato)throws ComponentException,java.rmi.RemoteException,PersistencyException;
java.util. List recuperoScadVoce(it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk bulk) throws ComponentException,java.rmi.RemoteException;
java.util.List findManRevRigaCollegati(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_attiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
java.util.List findListaBeneServizioWS(it.cnr.jada.UserContext userContext,String query,String tipo,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaModalitaIncassoWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaModalitaErogazioneWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaCodiciCpaWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaNazioneWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaNomenclaturaCombinataWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaNaturaTransazioneWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaModalitaTrasportoWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaCondizioneConsegnaWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.util.List findListaProvinciaWS(it.cnr.jada.UserContext userContext,String query,String dominio,String tipoRicerca)throws ComponentException,java.rmi.RemoteException;
java.lang.Long inserisciDatiPerStampaIva(it.cnr.jada.UserContext userContext,Long esercizio,String cd_cds,String cd_unita_organizzativa,java.lang.Long pg_fattura) throws PersistencyException, ComponentException,java.rmi.RemoteException;
@Deprecated
Nota_di_credito_attivaBulk generaNotaCreditoAutomatica(it.cnr.jada.UserContext userContext, Fattura_attiva_IBulk fa, Integer esercizio) throws PersistencyException, ComponentException,java.rmi.RemoteException;
Fattura_attiva_IBulk ricercaFatturaSDI(it.cnr.jada.UserContext userContext, String codiceInvioSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
@Deprecated
Fattura_attiva_IBulk aggiornaDatiFatturaSDI(it.cnr.jada.UserContext userContext, String codiceInvioSdi, String statoInvioSdi, String noteInvioSdi, javax.xml.datatype.XMLGregorianCalendar dataConsegnaSdi, boolean stornaFattura) throws PersistencyException, ComponentException,java.rmi.RemoteException;
@Deprecated
Fattura_attiva_IBulk aggiornaDatiFatturaSDI(it.cnr.jada.UserContext userContext, Fattura_attiva_IBulk fatturaAttiva, String statoInvioSdi, String noteInvioSdi, javax.xml.datatype.XMLGregorianCalendar dataConsegnaSdi, boolean stornaFattura) throws PersistencyException, ComponentException,java.rmi.RemoteException;
Fattura_attivaBulk aggiornaFatturaPredispostaAllaFirma(UserContext userContext, Fattura_attivaBulk fatturaAttiva) throws PersistencyException, ComponentException,java.rmi.RemoteException;
Fattura_attivaBulk aggiornaFatturaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva) throws PersistencyException, ComponentException,java.rmi.RemoteException;
public void preparaProtocollazioneEProtocolla(UserContext userContext, Long pgProtocollazione, Integer offSet, Long pgStampa, java.sql.Timestamp dataStampa,Fattura_attivaBulk fattura) throws PersistencyException, ComponentException,java.rmi.RemoteException;
boolean isAttivoSplitPayment(UserContext userContext, Timestamp dt_registrazione)throws PersistencyException, ComponentException,java.rmi.RemoteException;
java.util.List recuperoFattureElettronicheSenzaNotificaConsegna(UserContext userContext, Unita_organizzativaBulk unita_organizzativaBulk) throws PersistencyException, ComponentException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
Fattura_attivaBulk recuperoFatturaElettronicaDaNomeFile(UserContext userContext, String nomeFileInvioSdi) throws PersistencyException, ComponentException,it.cnr.jada.persistency.IntrospectionException,java.rmi.RemoteException;
Fattura_attivaBulk aggiornaFatturaRicevutaConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, XMLGregorianCalendar dataConsegnaSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
Fattura_attivaBulk aggiornaFatturaRifiutataDestinatarioSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
Fattura_attivaBulk aggiornaFatturaScartoSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceInvioSdi, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
Fattura_attivaBulk aggiornaFatturaMancataConsegnaInvioSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, String codiceSdi, String noteInvioSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
Fattura_attivaBulk aggiornaFatturaDecorrenzaTerminiSDI(UserContext userContext, Fattura_attivaBulk fattura, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
Fattura_attivaBulk aggiornaFatturaEsitoAccettatoSDI(UserContext userContext, Fattura_attivaBulk fattura) throws PersistencyException, ComponentException,java.rmi.RemoteException;
Fattura_attivaBulk ricercaFatturaDaCodiceSDI(UserContext userContext, String codiceInvioSdi) throws PersistencyException, ComponentException, java.rmi.RemoteException;
Fattura_attivaBulk aggiornaFatturaTrasmissioneNonRecapitataSDI(UserContext userContext, Fattura_attivaBulk fattura, String codiceInvioSdi, String noteSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
Fattura_attivaBulk aggiornaFatturaConsegnaSDI(UserContext userContext, Fattura_attivaBulk fatturaAttiva, Date dataConsegnaSdi) throws PersistencyException, ComponentException,java.rmi.RemoteException;
BigDecimal getImportoBolloVirtuale(UserContext aUC, Fattura_attivaBulk fattura) throws ComponentException,java.rmi.RemoteException;
void controlliGestioneBolloVirtuale(UserContext aUC, Fattura_attivaBulk fatturaAttiva, BulkList dettaglio) throws ApplicationException, ComponentException,java.rmi.RemoteException;
String recuperoEmailUtente(UserContext aUC, Fattura_attivaBulk fatturaAttiva) throws ApplicationException, ComponentException,java.rmi.RemoteException;
void gestioneAvvisoInvioMailFattureAttive(UserContext aUC) throws ApplicationException, ComponentException,java.rmi.RemoteException;
}
