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

package it.cnr.contab.pdg00.comp;

import it.cnr.contab.pdg00.bulk.Pdg_preventivoBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Vector;
public interface IPdGPreventivoTestataMgr 
{


/** 
  *  default
  *    PreCondition:
  *      Viene richiesto l'annullamento dello scarico dei costi del dipendente per un pdg
  *    PostCondition:
  *      Viene invocata la stored procedure ORACLEannullaCDPSuPdg
 */
public abstract Pdg_preventivoBulk annullaCDPSuPdg (UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException;
/** 
  *  Utente AC
  *    PreCondition:
  *      l'utente appartiene al CDR Amministrazione Centrale 
  *    PostCondition:
  *      Viene restituito un istanza di Pdg_preventivoBulk per il CDR 00 dell UO CDS SAC e l'elenco di tutti i CDR che possiedono PDG
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione verificata
  *    PostCondition:
  *      Viene restituito un'istanza di Pdg_preventivoBulk che per il CDR specificato più l'elenco dei CDR con livello di responsabilià inferiore al CDR specificato (listaCdrPdGPerUtente)
  *  Utente associato a CDR senza PDG
  *    PreCondition:
  *      Il cdr dell'utente non possiede un PDG
  *    PostCondition:
  *      Viene generata una ApplicationException con il messaggio "Il CdR non ha un piano di gestione associato!"
 */
public abstract Pdg_preventivoBulk caricaPdg (UserContext userContext,CdrBulk cdr) throws ComponentException;
/** 
  *  eliminazione dettagli a partire dalla linea di attivià
  *    PreCondition:
  *      Viene richiesta l'eliminazione dei dettagli di spesa ed entrata che corrispondono ad esercizio, CdR e LA indicati.
  *    PostCondition:
  *      Vengono lanciate due delete una per i dettagli di spesa ed una per quelli di entrata per i record che soddisfano le condizioni.
 */
public abstract Pdg_preventivoBulk delDetByLA (UserContext userContext,Pdg_preventivoBulk pdg) throws ApplicationException,ComponentException;
/** 
  *  stato PDG = A
  *    PreCondition:
  *      pdg.stato = 'A' e
  *      confrontaLivelloResponsabilita(user.cdr,pdg.cdr) == 0
  *    PostCondition:
  *      ritorna true
  *  stato PDG = B
  *    PreCondition:
  *      pdg.stato = B e
  *      confrontaLivelloResponsabilita(user.cdr,pdg.cdr) == 0
  *    PostCondition:
  *      ritorna true
  *  stato PDG = D
  *    PreCondition:
  *      pdg.stato = D e
  *      confrontaLivelloResponsabilita(user.cdr,pdg.cdr) >= 0
  *    PostCondition:
  *      ritorna true
  *  stato PDG = E
  *    PreCondition:
  *      pdg.stato = E e
  *      confrontaLivelloResponsabilita(user.cdr,pdg.cdr) >= 0
  *    PostCondition:
  *      
  *  stato PDG = Ci
  *    PreCondition:
  *      pdg.stato = Ci e
  *      confrontaLivelloResponsabilita(user.cdr,pdg.cdr) == 0
  *    PostCondition:
  *      
  *  Tutti i controlli superati
  *    PreCondition:
  *      Non è verificata nessun'altra precondizione
  *    PostCondition:
  *      ritorna false
 */
public abstract boolean isDettagliPdGModificabili (UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException;
/** 
  *  Richiesto stato B, pdg dipendenti non chiusi
  *    PreCondition:
  *      nuovoStato = B e
  *      qualche pdg di listaPdGDipendenti(pdg) ha STATO diverso da C o F  
  *      
  *    PostCondition:
  *      genera una ApplicationException con messaggio "I PDG dei CDR di livello inferiore non sono stati ancora chiusi"
  *  Richiesto stato C, spese scaricate aperte
  *    PreCondition:
  *      nuovoStato = C e
  *      qualche dettagli del PDG con spese altrui e spese scaricate ha STATO = X
  *      
  *    PostCondition:
  *      genera una ApplicationException con messaggio "Esiste qualche spesa scaricata verso altro CDR o UO non ancora confermata o annullata"
  *  Richiesto stato C, pdg dipendenti non chiusi
  *    PreCondition:
  *      nuovoStato = C e
  *      pdg.STATO <> Ci e
  *      qualche pdg di listaPdGDipendenti(pdg) ha STATO diverso da C o F
  *      
  *    PostCondition:
  *      genera una ApplicationException con messaggio "I PDG dei CDR di livello inferiore non sono stati ancora chiusi"
  *  Richiesto stato Ci (C0,C1,C2), utente non abilitato
  *    PreCondition:
  *      nuovoStato = Ci e
  *      confrontaLivelloResponsabilita(user.cdr,pdg.cdr) = 0
  *    PostCondition:
  *      genera una ApplicationException con messaggio "Utente non abilitato ad operare sul PDG richiesto"
  *  Richiesto stato D da C, Pdg di livello superiore in stato errato
  *    PreCondition:
  *      getPdGDipendenza(pdg).STATO <> E
  *      
  *    PostCondition:
  *      genera una ApplicationException con messaggio "Il PDG del <CDR superiore> deve essere in stato E"
  *  Richiesto stato F, Pdg di livello superiore non chiusi
  *    PreCondition:
  *      nuovoStato = F e
  *      qualche pdg di listaPdGDipendenti(pdg) ha STATO diverso da F
  *      
  *    PostCondition:
  *      genera una ApplicationException con messaggio "I PDG dei CDR di livello inferiore non sono stati ancora chiusi"
  *  Tutti i controlli superati
  *    PreCondition:
  *      Tutte i controlli di validità sullo stato e sul livello di abilitazione dell'utente sono stati superati
  *    PostCondition:
  *      Modifica lo stato del PDG con nuovoStato
  *  Stato non compatibile
  *    PreCondition:
  *      isStatoCompatibile(stato attuale pdg,getLivelloResponsabilitaCDR(pdg.cdr),pdg.STATO)  = true
  *    PostCondition:
  *      genera una ApplicationException con messaggio "Stato non compatibile con l'attuale stato del PDG"
  *  Utente con livello di responsabilità non sufficiente
  *    PreCondition:
  *      confrontaLivelloResponsabilita(user.cdr,pdg.cdr) < 0
  *    PostCondition:
  *      genera una ApplicationException con messaggio "Utente non abilitato ad operare sul PDG richiesto"
  *  Richiesto stato C da stato Ci, utente con livello di responsabilità non sufficiente
  *    PreCondition:
  *      confrontaLivelloResponsabilita(user.cdr,pdg.cdr) <= 0
  *    PostCondition:
  *      genera una ApplicationException con messaggio "Utente non abilitato ad operare sul PDG richiesto"
  *  Richiesto stato B, spese scaricate aperte
  *    PreCondition:
  *      qualche dettagli del PDG con spese altrui e spese scaricate ha STATO = X
  *      
  *    PostCondition:
  *      genera una ApplicationException con messaggio "Esiste qualche spesa scaricata verso altro CDR o UO non ancora confermata o annullata"
  *  Richiesto stato Ci (C0,C1,C2), Pdg di livello inferiore in stato errato
  *    PreCondition:
  *      nuovoStato = Ci e
  *      getPdGDipendenza(pdg).STATO <> Ci
  *      
  *    PostCondition:
  *      genera una ApplicationException con messaggio "Il PDG del <CDR superiore> deve essere in stato Ci"
  *  Richiesto stato D da E, Pdg dipendenti non chiusi
  *    PreCondition:
  *      pdg.STATO = E
  *      nuovoStato = D e
  *      qualche pdg di listaPdGDipendenti(pdg) ha STATO diverso da C o F 
  *      
  *    PostCondition:
  *      genera una ApplicationException con messaggio "I PDG dei CDR di livello inferiore non sono stati ancora chiusi"
  *  Richiesto stato F, aggregato non confermato
  *    PreCondition:
  *      stato = F e
  *      aggregatoPdg.stato = A
  *    PostCondition:
  *      genera una ApplicationException con messaggio "Il pdg aggregato del CDR di I livello non è stato ancora confermato"
  *  Richiesto stato F, quadratura pdg aggregato fallita
  *    PreCondition:
  *      stato = F e
  *      aggregatoPdg.stato = B e
  *      le viste V_DPDG_AGGREGATO_ETR_DET_D e V_DPDG_AGGREGATO_SPE_DET_D non sono vuote con le clausole su ESERCIZIO e CD_CENTRO_RESPONSABILITA
  *    PostCondition:
  *      genera una DiscrepanzeAggregatoException passando l'elenco di Pdg_aggregato_etr_detBulk o Pdg_aggregato_spe_detBulk istanziati dalle 2 viste e che rappresentano l'entità della discrepanza tra PDG aggregato e PDG Preventivo
  *  Richiesto stato E
  *    PreCondition:
  *      pdg.STATO = C
  *      nuovoStato = E e
  *      qualche pdg di listaPdGDipendenti(pdg) ha STATO diverso da E o F
  *    PostCondition:
  *      genera una ApplicationException con messaggio "I PdG dei livelli di responsabilità superiori devono essere in stato E"
  *  Richiesto stato C da stato A o B, costi dei dipendenti non scaricati
  *    PreCondition:
  *      stato = C e
  *      aggregatoPdg.stato = A o B o D o E e
  *      checkScaricoCDPCompleto
  *    PostCondition:
  *      genera una ApplicationException con messaggio "Costi del personale non ancora scaricati completamente!"
  *  Richiesto stato C da un qualsiasi stato, quadratura costi senza spese fallita
  *    PreCondition:
  *      nuovo stato = C e
  *      stato corrente = qualsiasi stato A,B,D,E,Ci e
  *      checkQuadraturaRicaviFigurativi genera un'eccezione
  *    PostCondition:
  *      Viene lasciata uscire l'eccezione
  *  Richiesto stato F, Pdg aggregato non chiuso
  *    PreCondition:
  *      Richiesto stato F, Pdg aggregato non chiuso
  *    PostCondition:
  *      Viene richiamata la stored procedure CNRCTB050.checkAggregatoChiuso
  *  Richiesto stato F, Pdg di Cdr responsabile di unità organizzativa. Il bilancio preventivo dell'ente è approvato
  *    PreCondition:
  *      Richiesto stato F, Pdg di Cdr responsabile di unità organizzativa. Il bilancio preventivo dell'ente è approvato
  *    PostCondition:
  *      Viene richiamata la stored procedure CNRCTB055.creaRipartEntrateCnr
 */
public abstract it.cnr.jada.bulk.OggettoBulk modificaStatoPdG (UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException,DiscrepanzeAggregatoException;
/** 
  *  Tutti controlli superati
  *    PreCondition:
  *      Viene richiesto il ribaltamento dei costi del Piano di Gestione del CdR specificato all'area di ricerca a cui afferisce. Il bilancio del CNR è già stato approvato
  *    PostCondition:
  *      La procedura Oracle CNRCTB053.ribaltaSuAreaPDG viene eseguita per l'anno di esercizio ed il CdR specificati.
 */
public abstract Pdg_preventivoBulk ribaltaCostiPdGArea (UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException;
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto l'esecuzione dello scarico dei costi del dipendente per un pdg
  *    PostCondition:
  *      Viene invocata la stored procedure CNRCTB060.scaricaCDPSuPdg
 */
public abstract Pdg_preventivoBulk scaricaCDPSuPdg (UserContext userContext,Pdg_preventivoBulk pdg) throws ComponentException;
}
