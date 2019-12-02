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

/*
* Created by Generator 1.0
* Date 19/10/2005
*/
package it.cnr.contab.prevent01.bulk;
import java.util.Hashtable;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.util.OrderedHashtable;
import it.cnr.jada.util.action.CRUDBP;
public class Pdg_esercizioBulk extends Pdg_esercizioBase {

	private it.cnr.contab.config00.sto.bulk.CdrBulk cdr;

	private static OrderedHashtable statoKeys;
	private static OrderedHashtable statoSenzaContrattazioneKeys;
	private static Hashtable prossimoStato;
	private static Hashtable precedenteStato;
	final public static String STATO_APERTURA_CDR 		= "AC";
	final public static String STATO_PRECHIUSURA_CDR 	= "PC";
	final public static String STATO_CHIUSURA_CDR 		= "CC";
	final public static String STATO_IN_ESAME_CDR 		= "IE";
	final public static String STATO_ESAMINATO_CDR 		= "EC";
	final public static String STATO_APPROVAZIONE_CDR 	= "AP";
	final public static String STATO_APERTURA_GESTIONALE_CDR 	= "AG";
	final public static String STATO_CHIUSURA_GESTIONALE_CDR 	= "CG";

	public Pdg_esercizioBulk() {
		super();
	}
	public Pdg_esercizioBulk(java.lang.Integer esercizio, java.lang.String cd_centro_responsabilita) {
		super(esercizio, cd_centro_responsabilita);
		setCdr(new CdrBulk(cd_centro_responsabilita));
	}

	/**
	 * Metodo statico che permette di recuperare il valore dei possibili stati 
	 * che può assumere un esercizio contabile.
	 * @return prossimoStato Variabile di tipo <code>Hashtable</code> che 
	 * 						 contiene codici e relative descrizioni degli stati.
	 */
	public static java.util.Hashtable getProssimoStato() 
	{
		if (prossimoStato == null)
		{
			prossimoStato = new Hashtable();
			prossimoStato.put( STATO_APERTURA_CDR, STATO_PRECHIUSURA_CDR );
			prossimoStato.put( STATO_PRECHIUSURA_CDR, STATO_CHIUSURA_CDR );
			prossimoStato.put( STATO_CHIUSURA_CDR, STATO_IN_ESAME_CDR );
			prossimoStato.put( STATO_IN_ESAME_CDR, STATO_ESAMINATO_CDR );			
			prossimoStato.put( STATO_IN_ESAME_CDR, STATO_ESAMINATO_CDR );			
			prossimoStato.put( STATO_ESAMINATO_CDR, STATO_APPROVAZIONE_CDR );			
			prossimoStato.put( STATO_APPROVAZIONE_CDR, STATO_APERTURA_GESTIONALE_CDR );			
			prossimoStato.put( STATO_APERTURA_GESTIONALE_CDR, STATO_CHIUSURA_GESTIONALE_CDR );			
		}	
		return prossimoStato;
	}

	public static java.util.Hashtable getPrecedenteStato() 
	{
		if (precedenteStato == null)
		{
			precedenteStato = new Hashtable();
			precedenteStato.put( STATO_PRECHIUSURA_CDR, STATO_APERTURA_CDR );
			precedenteStato.put( STATO_CHIUSURA_CDR, STATO_PRECHIUSURA_CDR );
			precedenteStato.put( STATO_IN_ESAME_CDR, STATO_CHIUSURA_CDR );
			precedenteStato.put( STATO_ESAMINATO_CDR, STATO_IN_ESAME_CDR );			
			precedenteStato.put( STATO_APPROVAZIONE_CDR, STATO_ESAMINATO_CDR );			
			precedenteStato.put( STATO_APERTURA_GESTIONALE_CDR, STATO_APPROVAZIONE_CDR );			
			precedenteStato.put( STATO_CHIUSURA_GESTIONALE_CDR, STATO_APERTURA_GESTIONALE_CDR );			
		}	
		return precedenteStato;
	}

	public OrderedHashtable getStatoKeys() {
		if (statoKeys == null)
		{
			statoKeys = new OrderedHashtable();
			statoKeys.put(STATO_APERTURA_CDR, "Apertura del CDR");	
			statoKeys.put(STATO_PRECHIUSURA_CDR, "Prechiusura del CDR");	
			statoKeys.put(STATO_CHIUSURA_CDR, "Chiusura del CDR");
			statoKeys.put(STATO_IN_ESAME_CDR, "In esame dal centro");
			statoKeys.put(STATO_ESAMINATO_CDR, "Esaminato dal centro");
			statoKeys.put(STATO_APPROVAZIONE_CDR, "Approvazione del CDR");
			statoKeys.put(STATO_APERTURA_GESTIONALE_CDR, "Apertura Gestionale del CDR");
			statoKeys.put(STATO_CHIUSURA_GESTIONALE_CDR, "Chiusura Gestionale del CDR");
		}
		return statoKeys;
	}
	public OrderedHashtable getStatoSenzaContrattazioneKeys() {
		if (statoSenzaContrattazioneKeys == null)
		{
			statoSenzaContrattazioneKeys = new OrderedHashtable();
			statoSenzaContrattazioneKeys.put(STATO_APERTURA_CDR, "Apertura del CDR");	
			statoSenzaContrattazioneKeys.put(STATO_PRECHIUSURA_CDR, "Prechiusura del CDR");	
			statoSenzaContrattazioneKeys.put(STATO_CHIUSURA_CDR, "Chiusura del CDR");
			statoSenzaContrattazioneKeys.put(STATO_APERTURA_GESTIONALE_CDR, "Apertura Gestionale del CDR");
			statoSenzaContrattazioneKeys.put(STATO_CHIUSURA_GESTIONALE_CDR, "Chiusura Gestionale del CDR");
		}
		return statoSenzaContrattazioneKeys;
	}
	public it.cnr.contab.config00.sto.bulk.CdrBulk getCdr() {
		return cdr;
	}
	public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
		this.getCdr().setCd_centro_responsabilita(cd_centro_responsabilita);
	}

	public String getCd_centro_responsabilita() {
		it.cnr.contab.config00.sto.bulk.CdrBulk cdr = this.getCdr();
		if (cdr == null)
			return null;
		return getCdr().getCd_centro_responsabilita();
	}

	public void setCdr(it.cnr.contab.config00.sto.bulk.CdrBulk newCdr) {
		cdr = newCdr;
	}

	public boolean isROStato()
	{
		return true;
	}
}