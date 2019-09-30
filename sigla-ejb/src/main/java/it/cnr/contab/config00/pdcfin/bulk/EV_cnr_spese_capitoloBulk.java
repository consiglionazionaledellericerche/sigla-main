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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.util.*;

/**
 * Elemento_voceBulk che rappresenta i capitoli di spesa del CNR
 */

 public class EV_cnr_spese_capitoloBulk extends Elemento_voceBulk {
	
//	protected String cd_sezione;
//	protected it.cnr.jada.util.OrderedHashtable sezioniKeys;
	
	protected Elemento_voceBulk titolo_padre;
	
public EV_cnr_spese_capitoloBulk() {
	super();
	setTi_appartenenza(Elemento_voceHome.APPARTENENZA_CNR);
	setTi_gestione(Elemento_voceHome.GESTIONE_SPESE);
	setTi_elemento_voce(Elemento_voceHome.TIPO_CAPITOLO);
	setCd_parte(Elemento_voceHome.PARTE_1);
	titolo_padre     = new Elemento_voceBulk();
	elemento_padre   = new Elemento_voceBulk();
}
public EV_cnr_spese_capitoloBulk(java.lang.String cd_elemento_voce,java.lang.Integer esercizio,java.lang.String ti_appartenenza,java.lang.String ti_gestione) {
	super(cd_elemento_voce,esercizio,ti_appartenenza,ti_gestione);
}
/**
 * Carica il titolo di spesa del cnr associato al capitolo. 
 * Questo metodo richiamato dal framework dopo il caricamento di un capitolo di spesa del cnr
 * @param broker it.cnr.jada.persistency.Broker broker che esegue la quesry per caricarsi il titolo
 */

public void fetchedFrom(it.cnr.jada.persistency.Broker broker)  throws IntrospectionException, FetchException 
{
	super.fetchedFrom(broker);

	if ( getCd_elemento_padre() == null )
		return;
	//elimino la categoria per risalire al titolo
	String cd_titolo_padre = getCd_elemento_padre().substring(0, getCd_elemento_padre().length() - 2 );
	// Elemento_voceKey evKey = new Elemento_voceKey( ti_appartenenza, cd_titolo_padre, esercizio, ti_gestione);
	Elemento_voceKey evKey = new Elemento_voceKey( cd_titolo_padre, getEsercizio(), getTi_appartenenza(), getTi_gestione() );
	titolo_padre = (Elemento_voceBulk)broker.getCache().get(evKey);
	if (titolo_padre == null) 
	{
		titolo_padre = new Elemento_voceBulk( cd_titolo_padre, getEsercizio(), getTi_appartenenza(), getTi_gestione() );
		broker.getCache().put(broker.getIntrospector().getOid(titolo_padre),titolo_padre);
		broker.getCache().addToFetchQueue(broker.getIntrospector(),titolo_padre); 
	}
}
/**
 * @return it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public Elemento_voceBulk getTitolo_padre() {
	return titolo_padre;
}
/**
 * Metodo per inizializzare l'oggetto bulk.
 */
public OggettoBulk initializeForInsert(it.cnr.jada.util.action.CRUDBP bp,it.cnr.jada.action.ActionContext context) {
	super.initializeForInsert(bp, context);
	setFl_voce_personale( new Boolean(false) );
	setFl_partita_giro( new Boolean(false));
	return this;
}
/**
 * Determina quando abilitare o meno nell'interfaccia utente il campo titolo
 * @return boolean true quando il campo deve essere disabilitato
 */

public boolean isROTitolo_padre() {
	return titolo_padre == null || titolo_padre.getCrudStatus() == NORMAL;
}
/**
 * @param newTitolo_padre it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk
 */
public void setTitolo_padre(Elemento_voceBulk newTitolo_padre) {
	titolo_padre = newTitolo_padre;
}
/**
 * Esegue la validazione formale dei campi di input
 */

public void validate() throws ValidationException 
{
	super.validate();
	if ( titolo_padre == null || isNullOrEmpty( titolo_padre.getCd_elemento_voce() ))
		throw new ValidationException( "Inserire il codice titolo. " );
//	if ( isNullOrEmpty( cd_sezione ))
//		throw new ValidationException( "Selezionare la sezione. " );		
		
}
}
