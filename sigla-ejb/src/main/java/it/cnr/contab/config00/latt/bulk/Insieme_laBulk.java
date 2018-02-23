package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Insieme_laBulk extends Insieme_laBase {

	private it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita;
public Insieme_laBulk() {
	super();
}
public Insieme_laBulk(java.lang.String cd_centro_responsabilita,java.lang.String cd_insieme_la) {
	super(cd_centro_responsabilita,cd_insieme_la);
	setCentro_responsabilita(new it.cnr.contab.config00.sto.bulk.CdrBulk(cd_centro_responsabilita));
}
public java.lang.String getCd_centro_responsabilita() {
	it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita = this.getCentro_responsabilita();
	if (centro_responsabilita == null)
		return null;
	return centro_responsabilita.getCd_centro_responsabilita();
}
/**
 * Restituisce il valore della proprietà 'centro_responsabilita'
 *
 * @return Il valore della proprietà 'centro_responsabilita'
 */
public it.cnr.contab.config00.sto.bulk.CdrBulk getCentro_responsabilita() {
	return centro_responsabilita;
}
public void setCd_centro_responsabilita(java.lang.String cd_centro_responsabilita) {
	this.getCentro_responsabilita().setCd_centro_responsabilita(cd_centro_responsabilita);
}
/**
 * Imposta il valore della proprietà 'centro_responsabilita'
 *
 * @param newCentro_responsabilita	Il valore da assegnare a 'centro_responsabilita'
 */
public void setCentro_responsabilita(it.cnr.contab.config00.sto.bulk.CdrBulk newCentro_responsabilita) {
	centro_responsabilita = newCentro_responsabilita;
}
}
