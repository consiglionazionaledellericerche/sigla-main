package it.cnr.contab.config00.latt.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_tipo_la_cdrBulk extends Ass_tipo_la_cdrBase {

	protected it.cnr.contab.config00.sto.bulk.CdrBulk centro_responsabilita;
public Ass_tipo_la_cdrBulk() {
	super();
}
public Ass_tipo_la_cdrBulk(java.lang.String cd_centro_responsabilita,java.lang.String cd_tipo_linea_attivita) {
	super(cd_centro_responsabilita,cd_tipo_linea_attivita);
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
