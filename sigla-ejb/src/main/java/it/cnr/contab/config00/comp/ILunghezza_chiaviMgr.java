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
import it.cnr.jada.comp.ComponentException;
public interface ILunghezza_chiaviMgr
{




/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di un Capoconto
 */
        public String formatCapocontoKey (UserContext userContext,String key,Integer esercizio) throws ComponentException;

/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di un CONTO
 */
        public String formatContoKey (UserContext userContext,String key,Integer esercizio) throws ComponentException;

/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta una generica chiave
 */
        public String formatKey (UserContext userContext,String key,Integer esercizio,String tabella,Integer livello,String attributo) throws ComponentException;

/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      aggiunge caratteri '0' a sinistra di un astringa fino a raggiungere la lunghezza specificata
 */
        public String leftPadding (String key,int keyLen);


/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      Estrae da una chiave completa di una organizzazione i primi n caratteri che corrispondono al codice CDS
 */
        public String extractCdsKey (UserContext userContext,String key) throws ComponentException;

/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      estrae da una chiave completa di una organizzazione gli n caratteri che corrispondono al codice UO
 */
        public String extractUoKey (UserContext userContext,String key) throws ComponentException;

/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di un Cdr
 */
        public String formatCdrKey (UserContext userContext,String key,Integer livello) throws ComponentException;

/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di un Cds
 */
        public String formatCdsKey (UserContext userContext,String key) throws ComponentException;

/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di una LINEA ATTIVITA'
 */
        public String formatLinea_attivitaKey (UserContext userContext,String key) throws ComponentException;

/**
  *  normale
  *    PreCondition:
  *      .
  *    PostCondition:
  *      formatta la chiave di una Unita Organizzativa
 */
        public String formatUoKey (UserContext userContext,String key) throws ComponentException;
}
