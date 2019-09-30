/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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