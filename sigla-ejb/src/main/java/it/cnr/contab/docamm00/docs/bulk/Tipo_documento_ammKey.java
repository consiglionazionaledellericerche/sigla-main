package it.cnr.contab.docamm00.docs.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_documento_ammKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_DOCUMENTO_AMM VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_documento_amm;

/* 
 * Getter dell'attributo cd_tipo_documento_amm
 */
public java.lang.String getCd_tipo_documento_amm() {
	return cd_tipo_documento_amm;
}

/* 
 * Setter dell'attributo cd_tipo_documento_amm
 */
public void setCd_tipo_documento_amm(java.lang.String cd_tipo_documento_amm) {
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
}

public Tipo_documento_ammKey() {
	super();
}


public Tipo_documento_ammKey(java.lang.String cd_tipo_documento_amm) {
	super();
	this.cd_tipo_documento_amm = cd_tipo_documento_amm;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_documento_ammKey)) return false;
	Tipo_documento_ammKey k = (Tipo_documento_ammKey)o;
	if(!compareKey(getCd_tipo_documento_amm(),k.getCd_tipo_documento_amm())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_documento_amm());
}

}
