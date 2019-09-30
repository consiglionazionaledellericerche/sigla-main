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

package it.cnr.contab.prevent00.comp;

import it.cnr.contab.prevent00.bulk.Pdg_aggregato_etr_detBulk;
import it.cnr.contab.prevent00.bulk.Pdg_aggregato_spe_detBulk;

import it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.util.Vector;
public interface IPdgAggregatoMgr extends ICRUDMgr
{

/** 
  *  default
  *    PreCondition:
  *      Viene richiesto il caricamento della testata di un pdg aggregato
  *    PostCondition:
  *      Viene cercato e caricato un Pdg_aggregatoBulk usando l'esercizio definito da userContext e il cdr specificato
 */

public abstract OggettoBulk caricaPdg_aggregato(it.cnr.jada.UserContext param0,Pdg_aggregatoBulk pdg_aggregato) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Dettaglio entrate modificato non esistente
  *    PreCondition:
  *      Viene richiesto il caricamento di un dettaglio di un Pdg_aggregato_etr_detBulk e 
  *      non esistono i corrispondenti dati modificati (TI_AGGREGATO = 'M')
  *    PostCondition:
  *      Viene restituito un'istanza di Pdg_aggregato_etr_det_inizialeBulk inizializzato creando un nuovo Pdg_aggregato_etr_detBulk con le stessa chiave (e ti_aggregato = "M") e gli stessi importi del dettaglio passato
  *  Dettaglio spese modificato non esistente
  *    PreCondition:
  *      Viene richiesto il caricamento di un dettaglio di un Pdg_aggregato_spe_detBulk e 
  *      non esistono i corrispondenti dati modificati (TI_AGGREGATO = 'M')
  *    PostCondition:
  *      Viene restituito un'istanza di Pdg_aggregato_spe_det_inizialeBulk inizializzato creando un nuovo Pdg_aggregato_spe_detBulk con le stessa chiave (e ti_aggregato = "M") e gli stessi importi del dettaglio passato
  *  Dettaglio entrate modificato esistente
  *    PreCondition:
  *      Viene richiesto il caricamento di un dettaglio di un Pdg_aggregato_etr_detBulk e 
  *      esistono i corrispondenti dati modificati (TI_AGGREGATO = 'M')
  *    PostCondition:
  *      Viene restituito un'istanza di Pdg_aggregato_etr_det_inizialeBulk con il Pdg_aggregato_etr_detBulk corrispondente
  *  Dettaglio spese modificato esistente
  *    PreCondition:
  *      Viene richiesto il caricamento di un dettaglio di un Pdg_aggregato_spe_detBulk e 
  *      esistono i corrispondenti dati modificati (TI_AGGREGATO = 'M')
  *    PostCondition:
  *      Viene restituito un'istanza di Pdg_aggregato_spe_det_inizialeBulk con il Pdg_aggregato_spe_detBulk corrispondente
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Utente non AC
  *    PreCondition:
  *      L'utente non è l'Amministrazione Centrale (utente.cdr.livello <> 1 o utente.cdr.unita_organizzativa.cd_tipo_unita <> 'ENTE')
  *    PostCondition:
  *      Ritorna false
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondizione è verificata
  *    PostCondition:
  *      Ritorna true
 */

public abstract boolean isPdGAggregatoModificabile(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondition è verificata
  *    PostCondition:
  *      Dal dettaglio di spesa o entrata specificato viene estratto il dettaglio contenente gli importi modificati e quest'ultimo viene reso persistente.
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Testata già chiusa
  *    PreCondition:
  *      Si è richiesto un salvataggio di una riga di un pdg_aggregato e la testata è già in stato "B"
  *    PostCondition:
  *      Viene generata una ApplicationException con messaggio "Il piano di gestione aggregato è stato già confermato e non può più essere modificato"
  *  Lock testata non riuscito
  *    PreCondition:
  *      Si è richiesto un salvataggio di una riga di un pdg_aggregato e la testata è stata già lockata da qualche altro utente
  *    PostCondition:
  *      Viene generata una it.cnr.jada.bulk.BusyResourceException
  *  Tutti i controlli superati
  *    PreCondition:
  *      Nessun'altra precondition è verificata
  *    PostCondition:
  *      Viene salvato la riga di dettaglio contenente i dati modificati (etr_modificato per Pdg_aggregato_etr_det_inizialeBulk, spe_modificato per Pdg_aggregato_spe_det_inizialeBulk)
 */

public abstract OggettoBulk modificaStatoPdg_aggregato(it.cnr.jada.UserContext param0,it.cnr.contab.prevent00.bulk.Pdg_aggregatoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
