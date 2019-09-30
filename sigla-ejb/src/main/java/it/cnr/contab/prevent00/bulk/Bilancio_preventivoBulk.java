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

package it.cnr.contab.prevent00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Bilancio_preventivoBulk extends Bilancio_preventivoBase 
{
    public final static String STATO_A = "A";		// Iniziale
    public final static String STATO_B = "B";		// Prodotto (Predisposto)
    public final static String STATO_C = "C";	    // Approvato

	private final static java.util.Dictionary statiKeys;
 
	static 
	{
		statiKeys = new it.cnr.jada.util.OrderedHashtable();
		statiKeys.put(STATO_A,"A - Iniziale");
		statiKeys.put(STATO_B,"B - Predisposto");
		statiKeys.put(STATO_C,"C - Approvato");		
	}
	
    public final static String ENTE = "ENTE";		// Cds ENTE	 
public Bilancio_preventivoBulk() {
	super();
}
public Bilancio_preventivoBulk(java.lang.String cd_cds,java.lang.Integer esercizio,java.lang.String ti_appartenenza) {
	super(cd_cds,esercizio,ti_appartenenza);
}
/**
 * Controllo di abilitazione dei pulsanti di accesso ai dettagli spese/entrate bilancio CDS
 *
 *
 * @param bp business process corrente
 * @return true se sono da abilitare i pulsanti di accesso ai dettagli
 */
public boolean areBottoniDettagliCdsEnabled(it.cnr.contab.prevent00.bp.CRUDBilancioPrevCdsBP bp) 
{
	// Modifica dell' 11/01/2002 - L'accesso ai dettagli è sempre abilitato
    return (true);
}
/**
 * Controllo di abilitazione dei pulsanti di accesso ai dettagli spese/entrate bilancio CNR
 *
 *
 * @param bp business process corrente
 * @return true se sono da abilitare i pulsanti di accesso ai dettagli
 */

public boolean areBottoniDettagliCnrEnabled(it.cnr.contab.prevent00.bp.CRUDBilancioPrevCnrBP bp) 
{
	// Abilito il bottone sempre
	return (true);
}
/**
 * Restituisce il dizionario {@link java.util.Dictionary } degli stati.
 * 
 * @return java.util.Dictionary statiKeys
 */
public java.util.Dictionary getStatiKeys() 
{
	return statiKeys;
}
/**
 * Controllo di abilitazione del pulsante di approvazione del bilancio CDS
 *
 *
 * @param bp business process corrente
 * @return true il pusante è da abilitare
 */
 
public boolean isBottoneApprovaCdsEnabled(it.cnr.contab.prevent00.bp.CRUDBilancioPrevCdsBP bp) 
{
	// Abilito il bottone se sono in modifica con stato bilancio = B 
	if(bp.isEditable() && getStato().equals("B"))
	{
		return (true);
	}
	
	return (false);	// disabilito
}
/**
 * Controllo di abilitazione del pulsante di approvazione del bilancio CNR
 *
 *
 * @param bp business process corrente
 * @return true il pusante è da abilitare
 */

public boolean isBottoneApprovaCnrEnabled(it.cnr.contab.prevent00.bp.CRUDBilancioPrevCnrBP bp) 
{
	// Abilito il bottone se sono in modifica con stato bilancio = B 
	if(bp.isEditable() && getStato().equals("B"))
	{
		return (true);
	}
	
	return (false);	// disabilito
}
/**
 * Controllo di abilitazione del pulsante di produzione del bilancio CDS
 *
 *
 * @param bp business process corrente
 * @return true il pusante è da abilitare
 */

public boolean isBottoneProduciCdsEnabled(it.cnr.contab.prevent00.bp.CRUDBilancioPrevCdsBP bp) 
{

	// Abilito il bottone se sono in modifica con stato bilancio = A 
	if(bp.isEditable() && getStato().equals("A"))
	{
		return (true);
	}
	
	return (false);	// disabilito
}
/**
 * Controllo di abilitazione del pulsante di produzione del bilancio CDS
 *
 *
 * @param bp business process corrente
 * @return true il pusante è da abilitare
 */

public boolean isBottoneProduciCnrEnabled(it.cnr.contab.prevent00.bp.CRUDBilancioPrevCnrBP bp) 
{

	// Abilito il bottone se sono in modifica con stato bilancio = A 
	if(bp.isEditable() && getStato().equals("A"))
	{
		return (true);
	}
	
	return (false);	// disabilito
}
/**
 * Controllo di effettuabilità dell'approvazione approvazione del bilancio
 * Nel caso non sia possibile solleva un'eccezione
 *
 * @param bp business process corrente
 */

public void verificaSeAmmessaApprovazione() throws it.cnr.jada.action.MessageToUser
{
	if(!(STATO_B).equals(getStato()))
	{ throw new it.cnr.jada.action.MessageToUser( "Il bilancio non e' nello stato predisposto !" ); };
}
}
