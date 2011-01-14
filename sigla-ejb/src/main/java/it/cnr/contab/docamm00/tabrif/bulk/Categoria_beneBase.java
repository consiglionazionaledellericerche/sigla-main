package it.cnr.contab.docamm00.tabrif.bulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Categoria_beneBase extends Categoria_beneKey implements Keyed {
	// DS_CATEGORIA_BENE VARCHAR(100) NOT NULL
	private java.lang.String ds_categoria_bene;

/* 
 * Getter dell'attributo ds_categoria_bene
 */
public java.lang.String getDs_categoria_bene() {
	return ds_categoria_bene;
}

/* 
 * Setter dell'attributo ds_categoria_bene
 */
public void setDs_categoria_bene(java.lang.String ds_categoria_bene) {
	this.ds_categoria_bene = ds_categoria_bene;
}

public Categoria_beneBase() {
	super();
}

public Categoria_beneBase(java.lang.String cd_categoria_bene) {
	super(cd_categoria_bene);
}
}
