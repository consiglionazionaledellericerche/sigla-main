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

package it.cnr.contab.cori00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_liquidazione_coriKey extends it.cnr.jada.bulk.OggettoBulk implements it.cnr.jada.persistency.KeyedPersistent {

	// PAR_NUM DECIMAL(5,0) NOT NULL
	private java.lang.Integer par_num;

	// PG_CALL DECIMAL(15,0) NOT NULL
	private java.lang.Long pg_call;
public Vsx_liquidazione_coriKey() {
	super();
}

public Vsx_liquidazione_coriKey(Long pg_call, Integer par_num) {
	super();
	this.pg_call = pg_call;
	this.par_num = par_num;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Vsx_liquidazione_coriKey)) return false;
	Vsx_liquidazione_coriKey k = (Vsx_liquidazione_coriKey)o;
	if(!compareKey(getPg_call(),k.getPg_call())) return false;
	if(!compareKey(getPar_num(),k.getPar_num())) return false;
	return true;
}
/**
 * Restituisce il valore della proprietà 'par_num'
 *
 * @return par_num <code>Integer</code> il valore della proprietà par_num
 */
public java.lang.Integer getPar_num() {
	return par_num;
}
/**
 * Restituisce il valore della proprietà 'pg_call'
 *
 * @return pg_call il <code>Long</code> valore della proprietà pg_call
 */
public java.lang.Long getPg_call() {
	return pg_call;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_call())+
		calculateKeyHashCode(getPar_num());
}
/**
 * Imposta il valore della proprietà 'par_num'
 *
 * @param newPar_num <code>Integer</code> il nuovo valore
 */
public void setPar_num(java.lang.Integer newPar_num) {
	par_num = newPar_num;
}
/**
 * Imposta il valore della proprietà 'pg_call'
 *
 * @param newPg_call <code>Long</code> il nuovo valore
 */
public void setPg_call(java.lang.Long newPg_call) {
	pg_call = newPg_call;
}
}
