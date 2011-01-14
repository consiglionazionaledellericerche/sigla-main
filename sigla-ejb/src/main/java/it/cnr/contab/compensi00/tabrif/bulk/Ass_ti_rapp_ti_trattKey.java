package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ti_rapp_ti_trattKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_RAPPORTO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_rapporto;

	// CD_TRATTAMENTO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_trattamento;

public Ass_ti_rapp_ti_trattKey() {
	super();
}
public Ass_ti_rapp_ti_trattKey(java.lang.String cd_tipo_rapporto,java.lang.String cd_trattamento) {
	this.cd_tipo_rapporto = cd_tipo_rapporto;
	this.cd_trattamento = cd_trattamento;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Ass_ti_rapp_ti_trattKey)) return false;
	Ass_ti_rapp_ti_trattKey k = (Ass_ti_rapp_ti_trattKey)o;
	if(!compareKey(getCd_tipo_rapporto(),k.getCd_tipo_rapporto())) return false;
	if(!compareKey(getCd_trattamento(),k.getCd_trattamento())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_rapporto
 */
public java.lang.String getCd_tipo_rapporto() {
	return cd_tipo_rapporto;
}
/* 
 * Getter dell'attributo cd_trattamento
 */
public java.lang.String getCd_trattamento() {
	return cd_trattamento;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_rapporto())+
		calculateKeyHashCode(getCd_trattamento());
}
/* 
 * Setter dell'attributo cd_tipo_rapporto
 */
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.cd_tipo_rapporto = cd_tipo_rapporto;
}
/* 
 * Setter dell'attributo cd_trattamento
 */
public void setCd_trattamento(java.lang.String cd_trattamento) {
	this.cd_trattamento = cd_trattamento;
}
}
