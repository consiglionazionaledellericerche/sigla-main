package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Categoria_beneKey extends OggettoBulk implements KeyedPersistent {
	// CD_CATEGORIA_BENE VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_categoria_bene;

/* 
 * Getter dell'attributo cd_categoria_bene
 */
public java.lang.String getCd_categoria_bene() {
	return cd_categoria_bene;
}

/* 
 * Setter dell'attributo cd_categoria_bene
 */
public void setCd_categoria_bene(java.lang.String cd_categoria_bene) {
	this.cd_categoria_bene = cd_categoria_bene;
}

public Categoria_beneKey() {
	super();
}


public Categoria_beneKey(java.lang.String cd_categoria_bene) {
	super();
	this.cd_categoria_bene = cd_categoria_bene;
}

public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Categoria_beneKey)) return false;
	Categoria_beneKey k = (Categoria_beneKey)o;
	if(!compareKey(getCd_categoria_bene(),k.getCd_categoria_bene())) return false;
	return true;
}

public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_categoria_bene());
}

}
