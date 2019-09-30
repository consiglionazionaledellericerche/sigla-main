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
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;
public interface ITipologia_linea_attivitaMgr
{


/** 
  *  Default
  *    PreCondition:
  *      L'utente ha annullato l'associazione tar cdr e una tipologia linea attività
  *    PostCondition:
  *		 Effettua un rollback al savepoint impostato in inizializzaCdrAssociati
 */

public abstract void annullaModificaCdrAssociati(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesta l'associazione di una tipologia linea attività con tutti i cdr disponibili
  *    PostCondition:
  *		 Inserisce nella tabella ASS_TIPO_LA_CDR tante righe quanti sono i cdr mancanti nella associazione
  *		 con la tipologia linea attività specificata;
  *		 Per ogni cdr associato crea una linea attività secondo le specifiche di modificaCdrAssociati
 */

public abstract void associaTuttiCdr(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesto l'elenco dei cdr associabli ad una tipologia linea attivita
  *    PostCondition:
  *		 Effettua una query sulla tabella dei cdr che NON compaiono nella tabella ASS_TIPO_LA_CDR per
  *		 la tipologia linea attività specificata per l'esercizio di scrivania
 */

public abstract it.cnr.jada.util.RemoteIterator cercaCdrAssociabili(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesto la creazione di una nuova tipologia linea attivita e delle associazioni della stessa con uno o più CDR
  *    PostCondition:
  *		 Imposta ti_tipo_la = 'C'
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Default
  *    PreCondition:
  *      L'utente ha richiesto l'inserimento di una tipologia linea attivita
  *    PostCondition:
  *		 Il tipo linea attività viene inizializzato con CD_CDR_CREATORE uguale al
  *		 codice del CDR dell'utente
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Default
  *    PreCondition:
  *      L'utente ha richiesto la modifica di una tipologia linea attivita
  *    PostCondition:
  *		 Viene posto un lock sulla tipologia linea attività specificata
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerModifica(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Default
  *    PreCondition:
  *      L'utente ha richiesto l'associazione di nuovi cdr ad una tipologia linea attività
  *    PostCondition:
  *		 Inizializza un SAVEPOINT sulla transazione utente per poter eventualmente annullare
  *		 le modifiche alle associazioni
 */

public abstract void inizializzaCdrAssociatiPerModifica(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk param1) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesta la modifica delle associazioni di una tipologia linea attività con uno
  * 	 o più cdr.
  *    PostCondition:
  *		 Per ogni cdr specificato viene eliminata o creata una associazione con la tipologia
  * 	 linea attività specificata. Per ogni nuova associazione creata viene inserita una linea
  * 	 di attività con le seguenti informazioni:
  *			CD_LINEA_ATTIVITA = "C"+tipo_la.CD_TIPO_LINEA_ATTIVITA
  *			CD_CENTRO_RESPONSABILITA = CD_CDR (dal cdr corrente)
  *			CD_TIPO_LINEA_ATTIVITA = tipo_la.CD_TIPO_LINEA_ATTIVITA
  *			DENOMINAZIONE = tipo_la.DS_TIPO_LINEA_ATTIVITA
  *			CD_GRUPPO_LINEA_ATTIVITA = NULL
  *			CD_FUNZIONE = tipo_la.CD_FUNZIONE
  *			CD_NATURA = tipo_la.CD_NATURA
  *			DS_LINEA_ATTIVITA = NULL
  *			CD_CDR_COLLEGATO = NULL
  *			CD_LA_COLLEGATO = NULL
  *			ESERCIZIO_INIZIO = esercizio di scrivania
  *			ESERCIZIO_FINE = 2100
  *			CD_INSIEME_LA = NULL
  *			TI_GESTIONE = tipo_la.TI_GESTIONE
 */

public abstract void modificaCdrAssociati(it.cnr.jada.UserContext param0,it.cnr.contab.config00.latt.bulk.Tipo_linea_attivitaBulk param1,it.cnr.jada.bulk.OggettoBulk[] param2,java.util.BitSet param3,java.util.BitSet param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  Default
  *    PreCondition:
  *      Viene richiesto il salvataggio di una tipologia di linea attivita e delle associazioni della stessa con uno o più CDR
  *    PostCondition:
  *		 Per ogni nuova associazione con un cdr invoca creaLineaAttivitaComune
  *		 Rende persistenti la nuova tipologia e le associazioni con i cdr.
 */
public OggettoBulk modificaConBulk(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException;
}
