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

package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Carico_familiare_anag
 */

public class Carico_familiare_anagBulk extends Carico_familiare_anagBase {

	private AnagraficoBulk anagrafico;

	public final static java.util.Dictionary PERSONA;

	public final static String ALTRO   = "A";
	public final static String CONIUGE = "C";
	public final static String FIGLIO  = "F";
	
	static {
		PERSONA = new it.cnr.jada.util.OrderedHashtable();
		PERSONA.put(ALTRO,"Altro");
		PERSONA.put(CONIUGE,"Coniuge");
		PERSONA.put(FIGLIO,"Figlio");
	}
public Carico_familiare_anagBulk() {
	super();
}
public Carico_familiare_anagBulk(java.lang.Integer cd_anag,java.sql.Timestamp dt_ini_validita,java.lang.Long pg_carico_anag) {
	super(cd_anag,dt_ini_validita,pg_carico_anag);
}
	/**
	 * Restituisce l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
	 *
	 * @return it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk
	 *
	 * @see setAnagrafico
	 */

	public AnagraficoBulk getAnagrafico() {
		return anagrafico;
	}
public java.lang.Integer getCd_anag() {
	it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk anagrafico = this.getAnagrafico();
	if (anagrafico == null)
		return null;
	return anagrafico.getCd_anag();
}
	/**
	 * Restituisce il <code>Dictionary</code> per la gestione dei tipi di familiari.
	 *
	 * @return java.util.Dictionary
	 */

	public java.util.Dictionary getTi_personaKeys() {
		return PERSONA;
	}
public boolean isFiglio() {
	return FIGLIO.equalsIgnoreCase(getTi_persona());
}
public boolean isConiuge() {
	return CONIUGE.equalsIgnoreCase(getTi_persona());
}
public boolean isAltro() {
	return ALTRO.equalsIgnoreCase(getTi_persona());
}
public boolean isNotFiglio() {
	return !isFiglio();
}
	/**
	 * Imposta l'<code>AnagraficoBulk</code> a cui l'oggetto è correlato.
	 *
	 * @param newAnagrafico Anagrafica di riferimento.
	 *
	 * @see getAnagrafico
	 */

	public void setAnagrafico(AnagraficoBulk newAnagrafico) {
		anagrafico = newAnagrafico;
	}
public void setCd_anag(java.lang.Integer cd_anag) {
	this.getAnagrafico().setCd_anag(cd_anag);
}
public void validate(OggettoBulk parent) throws ValidationException {
	if (getTi_persona() == null)
		throw new ValidationException("Attenzione: è necessario specificare il tipo di persona");

	if (getCodice_fiscale() == null)
		throw new ValidationException("Attenzione: è necessario specificare il Codice Fiscale");
		
	if (getDt_ini_validita() == null || getDt_fin_validita() == null )
		throw new ValidationException("Attenzione: è necessario specificare sia la data inizio che la data fine validità.");		
		
	if (isFiglio() && getDt_nascita_figlio() == null)
		throw new ValidationException("Attenzione: è necessario specificare la data di nascita del figlio");
	if (isFiglio() && getDt_nascita_figlio()!=null && 
		getDt_ini_validita() != null && 
		getDt_ini_validita().before(getDt_nascita_figlio())){
			throw new ValidationException("Carichi familiari: data inizio validità minore della data di nascita del figlio.");
	}
	/* Carichi Familiari verifica data inizio validità corretta */
	if (getDt_ini_validita() != null && 
		getDt_fin_validita() != null &&
		getDt_ini_validita().after(getDt_fin_validita()))
		throw new ValidationException("Carichi familiari: data inizio validità maggiore della data di fine validità.");

	/* Carichi Familiari verifica esattezza carattere di controllo del codice fiscale */
	if (getCodice_fiscale() != null &&
		(getCodice_fiscale().length() != 16 ||
		 !it.cnr.contab.anagraf00.util.CodiceFiscaleControllo.checkCC(getCodice_fiscale())))
		throw new ValidationException("Carichi familiari: codice fiscale inserito errato.");

	/* Carichi Familiari verifica percentuale corretta */
	if (getPrc_carico() == null ||
		getPrc_carico().scale() > 2 ||
		getPrc_carico().longValue() > 100)
		throw new ValidationException("Carichi familiari: percentuale di carico non valida.");
	if (isFiglio() && getPrc_carico() != null && 
			getPrc_carico().compareTo(new java.math.BigDecimal(0))!=0 && 
			getPrc_carico().compareTo(new java.math.BigDecimal(50))!=0 &&
			getPrc_carico().compareTo(new java.math.BigDecimal(100))!=0 )
		throw new ValidationException("Carichi familiari: percentuale di carico non valida per il figlio. Consentite solo 0%, 50%, 100%.");

	if (isConiuge() && getPrc_carico() != null && 
				getPrc_carico().compareTo(new java.math.BigDecimal(0))!=0 && 
				getPrc_carico().compareTo(new java.math.BigDecimal(100))!=0 )
			throw new ValidationException("Carichi familiari: percentuale di carico non valida per il coniuge. Consentite solo 0%, 100%.");

	/* Carichi Familiari verifica esattezza carattere di controllo del codice fiscale */
	if (getCodice_fiscale_altro_gen() != null &&
		(getCodice_fiscale_altro_gen().length() != 16 ||
		 !it.cnr.contab.anagraf00.util.CodiceFiscaleControllo.checkCC(getCodice_fiscale_altro_gen())))
		throw new ValidationException("Carichi familiari: codice fiscale altro genitore inserito errato.");
	
	if (getCodice_fiscale().equals(anagrafico.getCodice_fiscale()))
		throw new ValidationException("Attenzione: non è possibile inserire un carico con lo stesso Codice Fiscale dell'anagrafico!");
	
	if (isFiglio() && getCodice_fiscale_altro_gen() != null && getCodice_fiscale_altro_gen().equals(anagrafico.getCodice_fiscale()))
		throw new ValidationException("Attenzione: non è possibile inserire il Codice fiscale dell'altro genitore uguale a quello dell'anagrafico stesso!");
	
	if (isFiglio() && getCodice_fiscale_altro_gen() != null && getCodice_fiscale_altro_gen().equals(getCodice_fiscale()))
		throw new ValidationException("Attenzione: non è possibile inserire il Codice fiscale dell'altro genitore uguale a quello del figlio!");
	
}
}
