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

import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.TrovatoBulk;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;

import javax.ejb.Remote;
import java.sql.Timestamp;
import java.util.List;

@Remote
public interface FatturaPassivaComponentSession extends it.cnr.contab.docamm00.comp.DocumentoAmministrativoComponentSession, it.cnr.jada.ejb.CRUDComponentSession, IDocumentoAmministrativoSpesaComponentSession {
    it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk addebitaDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk param1, java.util.List param2, java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void aggiornaStatoDocumentiAmministrativi(it.cnr.jada.UserContext param0, java.lang.String param1, java.lang.String param2, java.lang.String param3, java.lang.Integer param4, java.lang.Long param5, java.lang.String param6) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk calcoloConsuntivi(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk cercaCambio(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdC(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.util.RemoteIterator cercaDettagliFatturaPerNdD(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.util.RemoteIterator cercaFatturaPerNdC(it.cnr.jada.UserContext param0, CompoundFindClause compoundfindclause, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.util.RemoteIterator cercaFatturaPerNdD(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.util.RemoteIterator cercaObbligazioni(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk completaEnte(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk completaFornitore(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, it.cnr.contab.anagraf00.core.bulk.TerzoBulk param2) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk contabilizzaDettagliSelezionati(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, java.util.Collection param2, it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk param3) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void controllaQuadraturaConti(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void controllaQuadraturaObbligazioni(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk eliminaLetteraPagamentoEstero(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void eliminaRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.util.Vector estraeSezionali(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.util.List findDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, java.rmi.RemoteException;

    java.util.Collection findListabanche(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, java.rmi.RemoteException;

    java.util.Collection findListabancheuo(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, java.rmi.RemoteException;

    it.cnr.jada.util.RemoteIterator findNotaDiCreditoFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.util.RemoteIterator findNotaDiDebitoFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.util.RemoteIterator findObbligazioniFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, java.math.BigDecimal param2) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.anagraf00.core.bulk.TerzoBulk findTerzoUO(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk findUOEnte(it.cnr.jada.UserContext param0, java.lang.Integer param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    boolean hasFatturaPassivaARowNotInventoried(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    Boolean ha_beniColl(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void inserisciRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    boolean isBeneServizioPerSconto(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void protocolla(it.cnr.jada.UserContext param0, java.sql.Timestamp param1, java.lang.Long param2) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void rimuoviDaAssociazioniInventario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk param1, it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param2) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void rimuoviDaAssociazioniInventario(it.cnr.jada.UserContext param0, it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter param2) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaIndietro(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void rollbackToSavePoint(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk selezionaValutaDiDefault(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk setContoEnteIn(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1, java.util.List param2) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void setSavePoint(it.cnr.jada.UserContext param0, java.lang.String param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk stornaDettagli(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk param1, java.util.List param2, java.util.Hashtable param3) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk update(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(it.cnr.jada.UserContext param0, it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void validaFattura(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void validaRiga(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void verificaEsistenzaEdAperturaInventario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.jada.util.RemoteIterator selectBeniFor(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    boolean verificaStatoEsercizio(it.cnr.jada.UserContext param0, it.cnr.contab.config00.esercizio.bulk.EsercizioBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    boolean isEsercizioChiusoPerDataCompetenza(it.cnr.jada.UserContext param0, java.lang.Integer param1, java.lang.String param2) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, java.rmi.RemoteException;

    java.util.Collection findListabanchedett(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, java.rmi.RemoteException;

    it.cnr.contab.anagraf00.core.bulk.TerzoBulk findCessionario(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.util.List findListaFattureSIP(UserContext userContext, String query, String dominio, String uo, String terzo, String voce, String cdr, String gae, String tipoRicerca, Timestamp data_inizio, Timestamp data_fine) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    java.util.List findManRevRigaCollegati(UserContext param0, Fattura_passiva_rigaBulk param1) throws it.cnr.jada.comp.ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, java.rmi.RemoteException;

    it.cnr.jada.bulk.OggettoBulk rebuildDocumento(it.cnr.jada.UserContext param0, it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk ricercaFatturaTrovato(it.cnr.jada.UserContext userContext, Long esercizio, String cd_cds, String cd_unita_organizzativa, Long pg_fattura) throws ComponentException, java.rmi.RemoteException, PersistencyException;

    it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk ricercaFatturaByKey(it.cnr.jada.UserContext userContext, Long esercizio, String cd_cds, String cd_unita_organizzativa, Long pg_fattura) throws ComponentException, java.rmi.RemoteException, PersistencyException;

    java.util.List<it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk> ricercaFattureTrovato(it.cnr.jada.UserContext userContext, Long trovato) throws ComponentException, java.rmi.RemoteException, PersistencyException;

    TrovatoBulk ricercaDatiTrovato(it.cnr.jada.UserContext userContext, Long trovato) throws ComponentException, java.rmi.RemoteException, PersistencyException;

    TrovatoBulk ricercaDatiTrovatoValido(it.cnr.jada.UserContext userContext, Long trovato) throws ComponentException, java.rmi.RemoteException, PersistencyException;

    void inserisciProgUnivoco(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.ElaboraNumUnicoFatturaPBulk lancio) throws ComponentException, java.rmi.RemoteException, PersistencyException;

    Fattura_passivaBulk caricaAllegatiBulk(UserContext userContext, Fattura_passivaBulk fattura) throws ComponentException, java.rmi.RemoteException;

    void validaFatturaPerCompenso(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk valorizzaInfoDocEle(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    void aggiornaObblSuCancPerCompenso(UserContext userContext, Fattura_passivaBulk fatturaPassiva, java.util.Vector scadenzeDaCancellare, OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk eliminaLetteraPagamentoEstero(it.cnr.jada.UserContext param0, it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk param1, boolean param2) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

    boolean isAttivoSplitPayment(UserContext userContext, Timestamp dt_registrazione) throws PersistencyException, ComponentException, java.rmi.RemoteException;

    List<EvasioneOrdineRigaBulk> findRicercaOrdiniByClause(UserContext userContext, Fattura_passivaBulk fatturaPassiva, CompoundFindClause findclause) throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;
    
    boolean isAttivoSplitPaymentProf(UserContext userContext, Timestamp dt_registrazione) throws PersistencyException, ComponentException, java.rmi.RemoteException;
    void valorizzaDatiDaOrdini(UserContext userContext, Fattura_passivaBulk fattura)  throws it.cnr.jada.comp.ComponentException, java.rmi.RemoteException;

}
