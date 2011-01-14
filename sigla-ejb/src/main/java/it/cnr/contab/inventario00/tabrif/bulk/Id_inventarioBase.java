package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Id_inventarioBase extends Id_inventarioKey implements Keyed {
	// CD_INVENTARIO_ORIGINE VARCHAR(20)
	private java.lang.String cd_inventario_origine;

	// DS_INVENTARIO VARCHAR(100) NOT NULL
	private java.lang.String ds_inventario;

	// NR_INVENTARIO_INIZIALE DECIMAL(10,0) NOT NULL
	private java.lang.Long nr_inventario_iniziale;

public Id_inventarioBase() {
	super();
}
public Id_inventarioBase(java.lang.Long pg_inventario) {
	super(pg_inventario);
}
/* 
 * Getter dell'attributo cd_inventario_origine
 */
public java.lang.String getCd_inventario_origine() {
	return cd_inventario_origine;
}
/* 
 * Getter dell'attributo ds_inventario
 */
public java.lang.String getDs_inventario() {
	return ds_inventario;
}
/* 
 * Getter dell'attributo nr_inventario_iniziale
 */
public java.lang.Long getNr_inventario_iniziale() {
	return nr_inventario_iniziale;
}
/* 
 * Setter dell'attributo cd_inventario_origine
 */
public void setCd_inventario_origine(java.lang.String cd_inventario_origine) {
	this.cd_inventario_origine = cd_inventario_origine;
}
/* 
 * Setter dell'attributo ds_inventario
 */
public void setDs_inventario(java.lang.String ds_inventario) {
	this.ds_inventario = ds_inventario;
}
/* 
 * Setter dell'attributo nr_inventario_iniziale
 */
public void setNr_inventario_iniziale(java.lang.Long nr_inventario_iniziale) {
	this.nr_inventario_iniziale = nr_inventario_iniziale;
}
}
