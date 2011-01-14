package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Dichiarazione_intento
 */

public class Dichiarazione_intentoBulk extends Dichiarazione_intentoBase {

	private AnagraficoBulk anagrafico;
public Dichiarazione_intentoBulk() {
	super();
}
public Dichiarazione_intentoBulk(java.lang.Integer cd_anag,java.lang.Integer esercizio) {
	super(cd_anag,esercizio);
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
}
