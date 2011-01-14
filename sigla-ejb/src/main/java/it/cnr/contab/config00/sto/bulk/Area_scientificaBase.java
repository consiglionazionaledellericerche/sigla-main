package it.cnr.contab.config00.sto.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Area_scientificaBase extends Area_scientificaKey implements Keyed {
	// DS_AREA_SCIENTIFICA VARCHAR(80)
	private java.lang.String ds_area_scientifica;

public Area_scientificaBase() {
	super();
}
public Area_scientificaBase(java.lang.String cd_area_scientifica) {
	super(cd_area_scientifica);
}
/* 
 * Getter dell'attributo ds_area_scientifica
 */
public java.lang.String getDs_area_scientifica() {
	return ds_area_scientifica;
}
/* 
 * Setter dell'attributo ds_area_scientifica
 */
public void setDs_area_scientifica(java.lang.String ds_area_scientifica) {
	this.ds_area_scientifica = ds_area_scientifica;
}
}
