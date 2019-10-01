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
