package it.cnr.contab.inventario00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_ammortamentoKey extends OggettoBulk implements KeyedPersistent {
	// CD_TIPO_AMMORTAMENTO VARCHAR(10) NOT NULL (PK)
	private java.lang.String cd_tipo_ammortamento;

	// TI_AMMORTAMENTO CHAR(1) NOT NULL (PK)
	private java.lang.String ti_ammortamento;

public Tipo_ammortamentoKey() {
	super();
}
public Tipo_ammortamentoKey(java.lang.String cd_tipo_ammortamento) {
	super();
	this.cd_tipo_ammortamento = cd_tipo_ammortamento;
}
public Tipo_ammortamentoKey(java.lang.String cd_tipo_ammortamento,java.lang.String ti_ammortamento) {
	super();
	this.cd_tipo_ammortamento = cd_tipo_ammortamento;
	this.ti_ammortamento = ti_ammortamento;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Tipo_ammortamentoKey)) return false;
	Tipo_ammortamentoKey k = (Tipo_ammortamentoKey)o;
	if(!compareKey(getCd_tipo_ammortamento(),k.getCd_tipo_ammortamento())) return false;
	if(!compareKey(getTi_ammortamento(),k.getTi_ammortamento())) return false;
	return true;
}
/* 
 * Getter dell'attributo cd_tipo_ammortamento
 */
public java.lang.String getCd_tipo_ammortamento() {
	return cd_tipo_ammortamento;
}
/* 
 * Getter dell'attributo ti_ammortamento
 */
public java.lang.String getTi_ammortamento() {
	return ti_ammortamento;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getCd_tipo_ammortamento())+
		calculateKeyHashCode(getTi_ammortamento());
}
/* 
 * Setter dell'attributo cd_tipo_ammortamento
 */
public void setCd_tipo_ammortamento(java.lang.String cd_tipo_ammortamento) {
	this.cd_tipo_ammortamento = cd_tipo_ammortamento;
}
/* 
 * Setter dell'attributo ti_ammortamento
 */
public void setTi_ammortamento(java.lang.String ti_ammortamento) {
	this.ti_ammortamento = ti_ammortamento;
}
}
