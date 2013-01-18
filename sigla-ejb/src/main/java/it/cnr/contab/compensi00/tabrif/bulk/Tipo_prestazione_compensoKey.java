package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_prestazione_compensoKey extends OggettoBulk implements KeyedPersistent {

	// CD_TI_PRESTAZIONE VARCHAR(1) NOT NULL (PK)
	private java.lang.String cd_ti_prestazione;

public Tipo_prestazione_compensoKey() {
	super();
}
public Tipo_prestazione_compensoKey(java.lang.String cd_ti_prestazione) {
	this.cd_ti_prestazione = cd_ti_prestazione;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_prestazione_compensoKey)) return false;
	Tipo_prestazione_compensoKey k = (Tipo_prestazione_compensoKey)o;
	if(!compareKey(getCd_ti_prestazione(),k.getCd_ti_prestazione())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_ti_prestazione
 */
public java.lang.String getCd_ti_prestazione() {
	return cd_ti_prestazione;
}

/* 
 * Setter dell'attributo cd_ti_prestazione
 */
public void setCd_ti_prestazione(java.lang.String cd_ti_prestazione) {
	this.cd_ti_prestazione = cd_ti_prestazione;
}
}
