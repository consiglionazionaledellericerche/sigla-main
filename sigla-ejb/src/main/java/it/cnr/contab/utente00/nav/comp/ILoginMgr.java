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

package it.cnr.contab.utente00.nav.comp;

import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.util.RemoteIterator;

public interface ILoginMgr
{


//^^@@
/** 
  *  normale
  *    PreCondition:
  *      Una richiesta viene fatto per la modifica della password. L'utente è chiesto di scrivere la password vecchia e due volte la password nuova.
  *      
  *      Dato un'oggetto UtenteBulk che contiene le informazione dell'utente e la password vecchia (criptata) che ha digitato l'utente, e la password nuova (criptata) che ha digitata l'utente,
  *    PostCondition:
  *      La password vecchia viene confrontata con la password in base dati.
  *      Se questo controllo ha un esito positivo, opzionalmente si fa un controllo della password nuova nel confronto degli standard di password (troppo corta, troppa semplice, ecc.)
  *      Se questo controllo ha un esito positivo, si modifica UtenteBulk scrivendo la password nuova in base dati.
  *      Viene restituito l'oggetto UtenteBulk modificato.
 */
//^^@@
public abstract UtenteBulk cambiaPassword (UserContext userContext,UtenteBulk utente,String nuovoPassword) throws it.cnr.jada.comp.ComponentException;
//^^@@
/** 
  *  normale
  *    PreCondition:
  *      Dato un'istanza di UTENTE specificata dall'parametro UtenteBulk un'istanza di ALBERO_MAIN specificata dall'attributo CD_NODO, e un numero di livelli specificato dal parametro num_livelli
  *    PostCondition:
  *      sarà restituito un'oggetto ALBERO_MAINBULK che contiene le informazioni per l'istanza ALBERO_MAIN che corrisponde al CD_NODO, più la gerarchia dell'albero di funzioni a cui l'utente ha accesso per numero di livelli NUM_LIVELLI e cominciando dal nodo CD_NODO
 */
//^^@@
public abstract Albero_mainBulk generaAlberoPerUtente (UserContext userContext,UtenteBulk utente,String cd_unita_organizzativa,String cd_nodo,short num_livelli) throws it.cnr.jada.comp.ComponentException;
//^^@@
/** 
  *  normale
  *    PreCondition:
  *      Dato in input un'oggetto UtenteBulk che rappresenta l'utente, 
  *		 un CD_NODO che rappresenta il nodo a cui l'utente ha richiesto accesso e
  *		 un CD_UNITA_ORGANIZZATIVA 
  *    PostCondition:
  *      Se un controllo dei template, ruoli, e accessi abilitati per 
  *		 l'utente dimostra l'abilitazione al nodo richiesto, 
  *		 il metodo restituisce l'istanza di Albero_mainBulk che contiene
  *		 il nodo richiesto
 */
//^^@@
public abstract Albero_mainBulk validaNodoPerUtente(
    UserContext userContext,
    UtenteBulk utente,
    String cd_unita_organizzativa,
    String cd_nodo)
    throws it.cnr.jada.comp.ComponentException;
//^^@@
/** 
  *  normale
  *    PreCondition:
  *      Dato un oggetto che contiene il CD_UTENTE e la password digitati dall'utente che richiede accesso all'applicazione
  *    PostCondition:
  *      Se l'utente esiste in base dati, se la password corrisponde a quella in base dati, sarà restituito l'oggetto UtenteBulk.
  *      Altrimenti si fa un Throw di un'eccezione.
 */
//^^@@
public abstract UtenteBulk validaUtente (UserContext userContext,UtenteBulk utente) throws it.cnr.jada.comp.ComponentException;

//^^@@
/** 
  *  utente amministratore o superutente
  *    PreCondition:
  *      Dato un'istanza di UTENTE amminstratore o superutente 
  *		specificata dal parametro UtenteBulk 
  *    PostCondition:
  *      sarà restituito un array di tutti gli esercizi disponibili
  *  normale
  *    PreCondition:
  *      Dato un'istanza di UTENTE specificata dal parametro UtenteBulk 
  *    PostCondition:
  *      sarà restituito un array degli esercizi su cui l'utente
  * 	 possiede almeno un accesso su qualche unita organizzativa
  *		 (possiede direttamente o tramite i ruoli o l'utente template)
 */
//^^@@
public Integer[] listaEserciziPerUtente(
    UserContext userContext,
    UtenteBulk utente)
    throws it.cnr.jada.comp.ComponentException;

//^^@@
/** 
  *  normale
  *    PreCondition:
  *      Dato un'istanza di UTENTE COMUNE specificata dal parametro UtenteBulk
  * 	 e un esercizio
  *    PostCondition:
  *      sarà restituito un iteratore sulla collezione di unità organizzative
  *		 per cui l'utente possiede almeno un accesso per l'esercizio specificato
  *		 (possiede direttamente o tramite i ruoli o l'utente template). Per ogni
  *		 unita organizzativa CDS presente nella collezione devono essere presenti
  *		 tutte le unita organizzative del CDS a cui fa capo.
 */
//^^@@
public abstract RemoteIterator listaUOPerUtente(
    UserContext userContext,
    UtenteBulk utente,
    Integer esercizio)
    throws it.cnr.jada.comp.ComponentException;

//^^@@
/** 
  *  normale
  *    PreCondition:
  *      Dato un'istanza di UTENTE COMUNE specificata dal parametro UtenteBulk,
  * 	 un esercizio, il codice di una unita organizzativa e il nome di un
  *		 BusinessProcess
  *    PostCondition:
  *      sarà restituita la modalità di visualizzazione per il BusinessProcess
  * 	 in base agli accessi posseduti dall'utente. La modalità di visualizzazione
  *		 è determinata dai nodi di ALBERO_MAIN disponibili all'utente
  *		 in base ai suoi accessi che posseggono il BusinessProcess specificato.
  * 	 In caso di più nodi disponibili viene restituita la modalità di 
  *		 visualizzazione meno restrittiva
 */
//^^@@
public String validaBPPerUtente(
    UserContext userContext,
    UtenteBulk utente,
    String cd_unita_organizzativa,
    String bp)
    throws it.cnr.jada.comp.ComponentException;

boolean controllaAccesso(UserContext userContext,String cd_accesso) throws it.cnr.jada.comp.ComponentException;

void leggiMessaggi(UserContext userContext,it.cnr.contab.messaggio00.bulk.MessaggioBulk[] messaggi) throws it.cnr.jada.comp.ComponentException;

RemoteIterator listaMessaggi(UserContext userContext,String server_url) throws it.cnr.jada.comp.ComponentException;

void notificaMessaggi(UserContext userContext,String server_url) throws it.cnr.jada.comp.ComponentException;

void registerUser(UserContext userContext,String id_clone) throws it.cnr.jada.comp.ComponentException;

void unregisterUser(UserContext userContext) throws it.cnr.jada.comp.ComponentException;

void unregisterUsers(String id_clone) throws it.cnr.jada.comp.ComponentException;
}
