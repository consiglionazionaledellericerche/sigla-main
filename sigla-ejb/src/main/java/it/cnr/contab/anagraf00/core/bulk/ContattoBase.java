package it.cnr.contab.anagraf00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class ContattoBase extends ContattoKey implements Keyed {
	// DS_CONTATTO VARCHAR(100) NOT NULL
	private java.lang.String ds_contatto;

	// EMAIL VARCHAR(50)
	private java.lang.String email;

	// FAX VARCHAR(50)
	private java.lang.String fax;

	// MOTIVO VARCHAR(40)
	private java.lang.String motivo;

	// TELEFONO VARCHAR(50)
	private java.lang.String telefono;

public ContattoBase() {
	super();
}
public ContattoBase(java.lang.Integer cd_terzo,java.lang.Long pg_contatto) {
	super(cd_terzo,pg_contatto);
}
/* 
 * Getter dell'attributo ds_contatto
 */
public java.lang.String getDs_contatto() {
	return ds_contatto;
}
/* 
 * Getter dell'attributo email
 */
public java.lang.String getEmail() {
	return email;
}
/* 
 * Getter dell'attributo fax
 */
public java.lang.String getFax() {
	return fax;
}
/* 
 * Getter dell'attributo motivo
 */
public java.lang.String getMotivo() {
	return motivo;
}
/* 
 * Getter dell'attributo telefono
 */
public java.lang.String getTelefono() {
	return telefono;
}
/* 
 * Setter dell'attributo ds_contatto
 */
public void setDs_contatto(java.lang.String ds_contatto) {
	this.ds_contatto = ds_contatto;
}
/* 
 * Setter dell'attributo email
 */
public void setEmail(java.lang.String email) {
	this.email = email;
}
/* 
 * Setter dell'attributo fax
 */
public void setFax(java.lang.String fax) {
	this.fax = fax;
}
/* 
 * Setter dell'attributo motivo
 */
public void setMotivo(java.lang.String motivo) {
	this.motivo = motivo;
}
/* 
 * Setter dell'attributo telefono
 */
public void setTelefono(java.lang.String telefono) {
	this.telefono = telefono;
}
}
