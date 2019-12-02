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
 * Gestione dei dati relativi alla tabella Rappresentante_legale
 */

public class Rappresentante_legaleBulk extends Rappresentante_legaleBase {

	private it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune_nascita;
	public Rappresentante_legaleBulk() {
		super();
	}
public Rappresentante_legaleBulk(java.lang.Long pg_rapp_legale) {
	super(pg_rapp_legale);
}
	/**
	 * Restituisce il <code>ComuneBulk</code> descrittivo del comune di nascita.
	 *
	 * @return it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk
	 *
	 * @see setComune_nascita
	 */

	public it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk getComune_nascita() {
		return comune_nascita;
	}
public java.lang.Long getPg_comune_nascita() {
	it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk comune_nascita = this.getComune_nascita();
	if (comune_nascita == null)
		return null;
	return comune_nascita.getPg_comune();
}
	/**
	 * Indica quando comune_nascita deve essere read only.
	 *
	 * @return boolean
	 */

	public boolean isROds_comune_nascita() {
		return comune_nascita == null || comune_nascita.getCrudStatus() == OggettoBulk.NORMAL;
	}
	/**
	 * Imposta il <code>ComuneBulk</code> descrittivo del comune di nascita.
	 *
	 * @param newComune_nascita il comune.
	 *
	 * @see getComune_nascita
	 */

	public void setComune_nascita(it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk newComune_nascita) {
		comune_nascita = newComune_nascita;
	}
public void setPg_comune_nascita(java.lang.Long pg_comune_nascita) {
	this.getComune_nascita().setPg_comune(pg_comune_nascita);
}
	public void validate() throws it.cnr.jada.bulk.ValidationException {
		super.validate();

//	MANCA L'ATTIRIBUTO SESSO PER UNA VERIFICA COMPLETA
//
//		if( getCognome() != null
//		 && getNome() != null
//		 && getDt_nascita() != null
//		 && anagBulk.getTi_sesso() != null
//		 && getPg_comune_nascita() != null
//		 && getCodice_fiscale() != null
//		) {
//				it.cnr.contab.anagraf00.util.CodiceFiscaleControllo.parseCodiceFiscale(
//								anagBulk.getCognome(),
//								anagBulk.getNome(),
//								("" + dataNascita.get(GregorianCalendar.YEAR)).substring(2),
//								"" + dataNascita.get(GregorianCalendar.MONTH),
//								"" + dataNascita.get(GregorianCalendar.DAY_OF_MONTH),
//								anagBulk.getTi_sesso(),
//								"" + anagBulk.getPg_comune_nascita(),
//								anagBulk.getCodice_fiscale()
//				);
//		}

		if(getCodice_fiscale() != null
			&& (getCodice_fiscale().length() != 16
				|| !it.cnr.contab.anagraf00.util.CodiceFiscaleControllo.checkCC(getCodice_fiscale())
				)
			)
				throw new it.cnr.jada.bulk.ValidationException("Codice fiscale inserito errato.");
	}
}
