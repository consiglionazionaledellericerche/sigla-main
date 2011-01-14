package it.cnr.contab.reports.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Print_spooler_paramBase extends Print_spooler_paramKey implements Keyed {
	// VALORE_PARAM VARCHAR(300)
	private java.lang.String valoreParam;

	// PARAM_TYPE VARCHAR(100)
	private java.lang.String paramType;
	
public Print_spooler_paramBase() {
	super();
}
public Print_spooler_paramBase(java.lang.String nome_param,java.lang.Long pg_stampa) {
	super(nome_param,pg_stampa);
}
/* 
 * Getter dell'attributo valore_param
 */
public java.lang.String getValoreParam() {
	return valoreParam;
}
/* 
 * Setter dell'attributo valore_param
 */
public void setValoreParam(java.lang.String valore_param) {
	this.valoreParam = valore_param;
}
public java.lang.String getParamType() {
	return paramType;
}
public void setParamType(java.lang.String param_type) {
	this.paramType = param_type;
}
}