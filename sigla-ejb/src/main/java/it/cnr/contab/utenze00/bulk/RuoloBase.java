package it.cnr.contab.utenze00.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class RuoloBase extends RuoloKey implements Keyed {
	// CD_CDS VARCHAR(30)
	private java.lang.String cd_cds;

	// DS_RUOLO VARCHAR(200)
	private java.lang.String ds_ruolo;

	// TIPO VARCHAR(6)
	private java.lang.String tipo;

public RuoloBase() {
	super();
}
public RuoloBase(java.lang.String cd_ruolo) {
	super(cd_ruolo);
}
/* 
 * Getter dell'attributo cd_cds
 */
public java.lang.String getCd_cds() {
	return cd_cds;
}
/* 
 * Getter dell'attributo ds_ruolo
 */
public java.lang.String getDs_ruolo() {
	return ds_ruolo;
}
/* 
 * Setter dell'attributo cd_cds
 */
public void setCd_cds(java.lang.String cd_cds) {
	this.cd_cds = cd_cds;
}
/* 
 * Setter dell'attributo ds_ruolo
 */
public void setDs_ruolo(java.lang.String ds_ruolo) {
	this.ds_ruolo = ds_ruolo;
}
	/**
	 * @return
	 */
	public java.lang.String getTipo() {
		return tipo;
	}

	/**
	 * @param string
	 */
	public void setTipo(java.lang.String string) {
		tipo = string;
	}

}
