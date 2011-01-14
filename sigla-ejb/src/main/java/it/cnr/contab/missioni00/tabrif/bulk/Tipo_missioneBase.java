package it.cnr.contab.missioni00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_missioneBase extends Tipo_missioneKey implements Keyed {
	// DS_TIPO_MISSIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_missione;

public Tipo_missioneBase() {
	super();
}
public Tipo_missioneBase(java.lang.String cd_tipo_missione) {
	super(cd_tipo_missione);
}
/* 
 * Getter dell'attributo ds_tipo_missione
 */
public java.lang.String getDs_tipo_missione() {
	return ds_tipo_missione;
}
/* 
 * Setter dell'attributo ds_tipo_missione
 */
public void setDs_tipo_missione(java.lang.String ds_tipo_missione) {
	this.ds_tipo_missione = ds_tipo_missione;
}
}
