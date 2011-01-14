package it.cnr.contab.anagraf00.tabter.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Gestione dei dati relativi ai comuni esteri nella tabella Provincia
 */

public class ProvinciaBulk extends ProvinciaBase {

	private RegioneBulk regione;
/**
 * 
 */
public ProvinciaBulk() {}
public ProvinciaBulk(java.lang.String cd_provincia) {
	super(cd_provincia);
}
public java.lang.String getCd_regione() {
	it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk regione = this.getRegione();
	if (regione == null)
		return null;
	return regione.getCd_regione();
}
/**
 * Insert the method's description here.
 * Creation date: (18/04/2001 12:27:07)
 * @return it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk
 */
public RegioneBulk getRegione() {
	return regione;
}
public void setCd_regione(java.lang.String cd_regione) {
	this.getRegione().setCd_regione(cd_regione);
}
/**
 * Insert the method's description here.
 * Creation date: (18/04/2001 12:27:07)
 * @param newRegione it.cnr.contab.anagraf00.tabter.bulk.RegioneBulk
 */
public void setRegione(RegioneBulk newRegione) {
	regione = newRegione;
}
}
