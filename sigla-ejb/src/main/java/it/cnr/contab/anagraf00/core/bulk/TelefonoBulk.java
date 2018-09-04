package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi alla tabella Telefono
 */

public class TelefonoBulk extends TelefonoBase {

	public static final String TEL   = "T";
	public static final String EMAIL = "E";
	public static final String PEC = "P";
	public static final String FAX   = "F";
	
	private TerzoBulk terzo;
public TelefonoBulk() {
	super();
}
public TelefonoBulk(java.lang.Integer cd_terzo,java.lang.Long pg_riferimento) {
	super(cd_terzo,pg_riferimento);
	setTerzo(new it.cnr.contab.anagraf00.core.bulk.TerzoBulk(cd_terzo));
}
public java.lang.Integer getCd_terzo() {
	it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo = this.getTerzo();
	if (terzo == null)
		return null;
	return terzo.getCd_terzo();
}
	/**
	 * Restituisce il <code>TerzoBulk</code> a cui è associato l'oggetto.
	 *
	 * @return it.cnr.contab.anagraf00.core.bulk.TerzoBulk
	 *
	 * @see setTerzo
	 */

	public TerzoBulk getTerzo() {
		return terzo;
	}
public void setCd_terzo(java.lang.Integer cd_terzo) {
	this.getTerzo().setCd_terzo(cd_terzo);
}
	/**
	 * Imposta il <code>TerzoBulk</code> a cui è associato l'oggetto.
	 *
	 * @param newTerzo Il terzo da associare.
	 *
	 * @see getTerzo
	 */

	public void setTerzo(TerzoBulk newTerzo) {
		terzo = newTerzo;
	}
public void validate(OggettoBulk terzo) throws ValidationException {
	if (getDs_riferimento() == null)
		throw new ValidationException("E' necessario specificare una descrizione");
	if (getRiferimento() == null)
		throw new ValidationException("E' necessario specificare un riferimento");
}
}
