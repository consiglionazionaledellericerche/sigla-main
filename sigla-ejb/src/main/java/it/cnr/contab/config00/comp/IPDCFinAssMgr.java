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

package it.cnr.contab.config00.comp;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface IPDCFinAssMgr extends it.cnr.jada.comp.ICRUDMgr
{


/**
 * Esegue una operazione di creazione di una associazione fra un Elemento_voceBulk e un altro Elemento_voceBulk oppure
 * fra un Elemento_vocebulk e una Voce_epBulk
 *
 * Pre-post-conditions:
 *
 * Nome: Creazione di una Ass_ev_evBulk
 * Pre:  La richiesta di creazione di una associazione fra un Elemento_voceBulk e un altro Elemento_voceBulk
 *       è stata generata
 * Post: Un Ass_ev_evBulk e' stato creato 
 *
 * Nome: Creazione di una associazione tra spesa CNR ed entrata CDS
 * Pre:   Le seguenti regole di associazione sono verificate:
 *			Titolo '1' entrata Cds => Titolo '3' spesa CNR avente articolo(natura) '1' e '3' con capitolo 'CdS non Area'.
 *			Titolo '2' entrata Cds => Titolo '1' e '3' spesa CNR avente articolo(natura) '2' con capitolo 'Tutti i CdS'.
 *			Titolo '3' entrata Cds => Titolo '1' e '2' spesa CNR avente articolo(natura) '1','3',4' e '5' con capitolo 'Tutti i CdS'.
 *			Titolo '4' entrata Cds => Titolo '3' spesa CNR avente articolo(natura) '1' e '3' con capitolo 'CdS Area'.
 *          Titolo '4' entrata Cds => Titolo '3' spesa CNR avente articolo(natura) '4' e '5' con capitolo 'Tutti i CdS'.
 *          Titolo '5' entrata Cds => Titolo '5' spesa CNR con capitolo 'Tutti i CdS'.
 *        Non esiste un'altra associazione della spesa CDS con altra entrata CNR
 * Post:
 *		  Una Ass_cap_spesa_Cnr_natura_cap_entrata_CdsBulk è stata creata
 *
 * Nome: Creazione di una associazione tra titolo CNR e titolo CDS
 * Pre:   Le seguenti regole di associazione sono verificate:
 *          Non esiste un'altra associazione del titolo CDS con altro titolo CNR
 *          Non esiste un'altra associazione del titolo CNR con altro titolo CDS
 * Post:
 *		  Una Ass_titolo_Cnr_titolo_CdsBulk è stata creata
 *
 * Nome: Creazione di una Ass_ev_evBulk - errore
 * Pre:  La richiesta di creazione di una associazione fra un Elemento_voceBulk e un altro Elemento_voceBulk
 *       è stata generata e uno dei due elementi non e' piu' presente o e' stato modificato da un altro utente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * Nome: Creazione di una Ass_ev_voce_epBulk
 * Pre:  La richiesta di creazione di una associazione fra un Elemento_voceBulk e una Voce_epBulk
 *       è stata generata
 * Post: Un Ass_ev_voce_epBulk e' stato creato 
 *
 * Nome: Creazione di una Ass_ev_voce_epBulk - errore
 * Pre:  La richiesta di creazione di una associazione fra un Elemento_voceBulk e una Voce_epBulk
 *       è stata generata e uno dei due elementi non e' piu' presente o e' stato modificato da un altro utente
 * Post: Viene generata una ComponentException che ha come dettaglio l'ApplicationException che descrive l'errore da
 *       visualizzare all'utente
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	bulk l'Ass_ev_evBulk o Ass_ev_voceepBulk  che deve essere creato
 * @return	l'Ass_ev_evBulk o Ass_ev_voceepBulk risultante dopo l'operazione di creazione.
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}