package it.cnr.contab.inventario00.consultazioni.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ubicazione_beneRestKey extends OggettoBulk implements KeyedPersistent {
	// CD_UBICAZIONE VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_ubicazione;

	// CD_CDS VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_cds;

	// CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL (PK)
	private java.lang.String cd_unita_organizzativa;

public Ubicazione_beneRestKey() {
	super();
}
public Ubicazione_beneRestKey(java.lang.String cd_cds,java.lang.String cd_ubicazione,java.lang.String cd_unita_organizzativa) {
	super();
	this.cd_cds = cd_cds;
	this.cd_ubicazione = cd_ubicazione;
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
public boolean equals(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ubicazione_beneRestKey)) return false;
	Ubicazione_beneRestKey k = (Ubicazione_beneRestKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_ubicazione(),k.getCd_ubicazione())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	return true;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ubicazione_beneRestKey)) return false;
	Ubicazione_beneRestKey k = (Ubicazione_beneRestKey)o;
	if(!compareKey(getCd_cds(),k.getCd_cds())) return false;
	if(!compareKey(getCd_ubicazione(),k.getCd_ubicazione())) return false;
	if(!compareKey(getCd_unita_organizzativa(),k.getCd_unita_organizzativa())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo cd_ubicazione
 */
public java.lang.String getCd_ubicazione() {
	return cd_ubicazione;
}
/* 
 * Getter dell'attributo cd_unita_organizzativa
 */
public java.lang.String getCd_unita_organizzativa() {
	return cd_unita_organizzativa;
}
public int hashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_ubicazione())+
		calculateKeyHashCode(getCd_unita_organizzativa());
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_cds())+
		calculateKeyHashCode(getCd_ubicazione())+
		calculateKeyHashCode(getCd_unita_organizzativa());
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo cd_ubicazione
 */
public void setCd_ubicazione(java.lang.String cd_ubicazione) {
	this.cd_ubicazione = cd_ubicazione;
}
/* 
 * Setter dell'attributo cd_unita_organizzativa
 */
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.cd_unita_organizzativa = cd_unita_organizzativa;
}
}
