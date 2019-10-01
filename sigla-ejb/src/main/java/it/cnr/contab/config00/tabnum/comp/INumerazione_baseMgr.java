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

package it.cnr.contab.config00.tabnum.comp;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
public interface INumerazione_baseMgr
{





/**
 * Crea un nuovo progressivo per la colonna e la tabella specificata.
 *
 * Pre-post-conditions:
 *
 * Nome: Primo progressivo
 * Pre: Nella tabella NUMERAZIONE_BASE non esiste nessuna riga per la chiave 
 * 			(esercizio,tabella,colonna)
 * Post: Viene inserita una nuova riga con la chiave specificata e con i 
 *			seguenti valori:
 *			CD_INIZIALE="1"
 *			CD_MASSIMO="99999999"
 *			CD_CORRENTE="1"
 *			e viene restituito il valore 1;
 * 
 * Nome: Progressivo massimo superato
 * Pre: Nella tabella NUMERAZIONE_BASE esiste una riga per la chiave 
 * 			(esercizio,tabella,colonna) e il valore di CD_CORRENTE è uguale o
 *			maggiore di CD_MASSIMO
 * Post: Viene generata una NumerazioneEsauritaException
 * 
 * Nome: Risorsa occupata
 * Pre: Nella tabella NUMERAZIONE_BASE esiste una riga per la chiave 
 * 			(esercizio,tabella,colonna) e il valore di CD_CORRENTE è minore 
 * 			di CD_MASSIMO ma il record è già stato lockato da un altro utente
 * Post: Viene generata una ComponentException con deatil la BusyResourceException
 * 			che ha provocato il tentativo di lock fallito
 *
 * Nome: Tutti i controlli superati
 * Pre: Nella tabella NUMERAZIONE_BASE esiste una riga per la chiave 
 * 			(esercizio,tabella,colonna) e il valore di CD_CORRENTE è minore 
 * 			di CD_MASSIMO
 * Post: Viene incrementato di uno il valore di CD_MASSIMO e restituito
 * 			il nuovo progressivo
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 * @param	esercizio l'esercizio cui fa riferimento il progressivo
 * @param 	tabella Il nome della tabella per cui creare il progressivo
 * @param	colonna Il nome della colonna per cui creare il progressivo
 * @param 	user Lo userid dell'utente per cui è stato richiesto il progressivo
 * @return	Il nuovo progressivo.
 */

public abstract java.lang.Long creaNuovoProgressivo(it.cnr.jada.UserContext param0,java.lang.Integer param1,java.lang.String param2,java.lang.String param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.bulk.BusyResourceException;
}
