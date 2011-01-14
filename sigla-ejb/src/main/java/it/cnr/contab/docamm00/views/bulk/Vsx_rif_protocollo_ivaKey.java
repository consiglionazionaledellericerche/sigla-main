package it.cnr.contab.docamm00.views.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vsx_rif_protocollo_ivaKey extends OggettoBulk implements KeyedPersistent {

	// PAR_NUM DECIMAL(5,0) NOT NULL
	private java.lang.Integer par_num;

	// PG_CALL DECIMAL(15,0) NOT NULL
	private java.lang.Long pg_call;
public Vsx_rif_protocollo_ivaKey() {
	super();
}
public Vsx_rif_protocollo_ivaKey(Long pg_call, Integer par_num) {
	super();
	this.pg_call = pg_call;
	this.par_num = par_num;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Vsx_rif_protocollo_ivaKey)) return false;
	Vsx_rif_protocollo_ivaKey k = (Vsx_rif_protocollo_ivaKey)o;
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
