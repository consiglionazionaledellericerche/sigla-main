package it.cnr.contab.docamm00.docs.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Numerazione_doc_ammBase extends Numerazione_doc_ammKey implements KeyedPersistent {
	// CORRENTE DECIMAL(10,0) NOT NULL
	private java.lang.Long corrente;

public Numerazione_doc_ammBase() {
	super();
}
public Numerazione_doc_ammBase(java.lang.String cd_cds,java.lang.String cd_tipo_documento_amm,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio) {
	super(cd_cds,cd_tipo_documento_amm,cd_unita_organizzativa,esercizio);
}
/* 
 * Getter dell'attributo corrente
 */
public java.lang.Long getCorrente() {
	return corrente;
}
/* 
 * Setter dell'attributo corrente
 */
public void setCorrente(java.lang.Long corrente) {
	this.corrente = corrente;
}
}
