package it.cnr.contab.fondecon00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_reintegro_fondoKey extends OggettoBulk implements KeyedPersistent {

	// PAR_NUM DECIMAL(5,0) NOT NULL
	private java.lang.Integer par_num;

	// PG_CALL DECIMAL(15,0) NOT NULL
	private java.lang.Long pg_call;
public Vsx_reintegro_fondoKey() {
	super();
}
public Vsx_reintegro_fondoKey(Long pg_call, Integer par_num) {
	super();
	this.pg_call = pg_call;
	this.par_num = par_num;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Vsx_reintegro_fondoKey)) return false;
	Vsx_reintegro_fondoKey k = (Vsx_reintegro_fondoKey)o;
	if(!compareKey(getPg_call(),k.getPg_call())) return false;
	if(!compareKey(getPar_num(),k.getPar_num())) return false;
	return true;
}
/* 
 * Getter dell'attributo par_num
 */
public java.lang.Integer getPar_num() {
	return par_num;
}
/* 
 * Getter dell'attributo pg_call
 */
public java.lang.Long getPg_call() {
	return pg_call;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getPg_call())+
		calculateKeyHashCode(getPar_num());
}
/* 
 * Setter dell'attributo par_num
 */
public void setPar_num(java.lang.Integer par_num) {
	this.par_num = par_num;
}
/* 
 * Setter dell'attributo pg_call
 */
public void setPg_call(java.lang.Long pg_call) {
	this.pg_call = pg_call;
}
}
