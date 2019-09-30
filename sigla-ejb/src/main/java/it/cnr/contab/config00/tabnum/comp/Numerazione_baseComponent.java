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

import java.io.Serializable;

import it.cnr.contab.config00.tabnum.bulk.Numerazione_baseBulk;
import it.cnr.contab.config00.tabnum.bulk.Numerazione_baseHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OutdatedResourceException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
/**
 * Insert the type's description here.
 * Creation date: (20/06/2001 14:30:43)
 * @author: CNRADM
 */
public class Numerazione_baseComponent 
	extends it.cnr.jada.comp.GenericComponent
	implements INumerazione_baseMgr, Cloneable, Serializable{
/**
 * Numerazione_baseComponent constructor comment.
 */
public Numerazione_baseComponent() {
	super();
}
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
public Long creaNuovoProgressivo(UserContext userContext,Integer esercizio,String tabella,String colonna,String user) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.bulk.BusyResourceException {
	try {
		Numerazione_baseHome home = (Numerazione_baseHome) getHome(userContext, Numerazione_baseBulk.class);
		Numerazione_baseBulk numerazione = (Numerazione_baseBulk)home.findByPrimaryKey(new Numerazione_baseBulk(colonna,esercizio,tabella));
		if (numerazione == null) {
			numerazione = new it.cnr.contab.config00.tabnum.bulk.Numerazione_baseBulk();
			numerazione.setColonna(colonna);
			numerazione.setEsercizio(esercizio);
			numerazione.setTabella(tabella);
			numerazione.setUser(user);
			numerazione.setCd_iniziale("1");
			numerazione.setCd_massimo("99999999");
			numerazione.setCd_corrente("1");
			home.insert(numerazione, userContext);
			return new Long(1);
		}
		Long cd_corrente = new Long(Long.parseLong(numerazione.getCd_corrente())+1);
		long cd_massimo = Long.parseLong(numerazione.getCd_massimo());
		if (cd_corrente.longValue() >= cd_massimo)
			throw new NumerazioneEsauritaException();
		numerazione.setCd_corrente(cd_corrente.toString());
		numerazione.setUser(user);
		home.lock(numerazione);
		home.update(numerazione, userContext);
		return cd_corrente;
	}catch(NumerazioneEsauritaException e) {
		throw e;
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new it.cnr.jada.bulk.BusyResourceException("Numeratori occupati, riprovare!");		
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	} catch (OutdatedResourceException e) {
		throw new it.cnr.jada.bulk.BusyResourceException("Numeratori occupati, riprovare!");		
	}
}
public Long creaNuovoProgressivoTemp(UserContext userContext,Integer esercizio,String tabella,String colonna,String user) throws it.cnr.jada.comp.ComponentException,it.cnr.jada.bulk.BusyResourceException {
	try {
		it.cnr.contab.config00.tabnum.bulk.Numerazione_baseHome home = new it.cnr.contab.config00.tabnum.bulk.Numerazione_baseHome(getConnection(userContext));
		it.cnr.contab.config00.tabnum.bulk.Numerazione_baseBulk numerazione = (it.cnr.contab.config00.tabnum.bulk.Numerazione_baseBulk)home.findByPrimaryKey(new it.cnr.contab.config00.tabnum.bulk.Numerazione_baseKey(colonna,esercizio,tabella+"$"));
		if (numerazione == null) {
			numerazione = new it.cnr.contab.config00.tabnum.bulk.Numerazione_baseBulk();
			numerazione.setColonna(colonna);
			numerazione.setEsercizio(esercizio);
			numerazione.setTabella(tabella+"$");
			numerazione.setUser(user);
			numerazione.setCd_iniziale("-1");
			numerazione.setCd_massimo("-99999999");
			numerazione.setCd_corrente("-1");
			home.insert(numerazione, userContext);
			return new Long(1);
		}
		Long cd_corrente = new Long(Long.parseLong(numerazione.getCd_corrente())-1);
		long cd_massimo = Long.parseLong(numerazione.getCd_massimo());
		if (cd_corrente.longValue() <= cd_massimo)
			throw new NumerazioneEsauritaException();
		numerazione.setCd_corrente(cd_corrente.toString());
		numerazione.setUser(user);
		home.lock(numerazione);
		home.update(numerazione, userContext);
		return cd_corrente;
	}catch(NumerazioneEsauritaException e) {
		throw e;
	} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new it.cnr.jada.bulk.BusyResourceException("Numeratori occupati, riprovare!");		
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	} catch (OutdatedResourceException e) {
		throw new it.cnr.jada.bulk.BusyResourceException("Numeratori occupati, riprovare!");		
	}
}
}
