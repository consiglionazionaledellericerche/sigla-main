package it.cnr.contab.gestiva00.core.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Vp_liquid_iva_annualeKey extends OggettoBulk  implements KeyedPersistent {

	// ESERCIZIO DECIMAL(20,3)
	private Integer esercizio;

	// ID DECIMAL(22,0) NOT NULL
	private Long id;

	// SEQUENZA DECIMAL(22,0) NOT NULL
	private Long sequenza;

	// TIPO CHAR(1) NOT NULL
	private java.lang.String tipo;

public Vp_liquid_iva_annualeKey() {
	super();
}
public Vp_liquid_iva_annualeKey(
	Integer esercizio,
	Long id,
	Long sequenza,
	String tipo) {

	super();
	
	this.esercizio = esercizio;
	this.id = id;
	this.sequenza = sequenza;
	this.tipo = tipo;
}
public boolean equalsByPrimaryKey(Object o) {
	if (this == o) return true;
	if (!(o instanceof Vp_liquid_iva_annualeKey)) return false;
	Vp_liquid_iva_annualeKey k = (Vp_liquid_iva_annualeKey)o;
	if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
	if(!compareKey(getId(),k.getId())) return false;
	if(!compareKey(getSequenza(),k.getSequenza())) return false;
	if(!compareKey(getTipo(),k.getTipo())) return false;
	return true;
}
/* 
 * Getter dell'attributo esercizio
 */
public Integer getEsercizio() {
	return esercizio;
}
/* 
 * Getter dell'attributo id
 */
public Long getId() {
	return id;
}
/* 
 * Getter dell'attributo sequenza
 */
public Long getSequenza() {
	return sequenza;
}
/* 
 * Getter dell'attributo tipo
 */
public java.lang.String getTipo() {
	return tipo;
}
public int primaryKeyHashCode() {
	return
		calculateKeyHashCode(getId())+
		calculateKeyHashCode(getEsercizio())+
		calculateKeyHashCode(getSequenza())+
		calculateKeyHashCode(getTipo());
}
/* 
 * Setter dell'attributo esercizio
 */
public void setEsercizio(Integer esercizio) {
	this.esercizio = esercizio;
}
/* 
 * Setter dell'attributo id
 */
public void setId(Long id) {
	this.id = id;
}
/* 
 * Setter dell'attributo sequenza
 */
public void setSequenza(Long sequenza) {
	this.sequenza = sequenza;
}
/* 
 * Setter dell'attributo tipo
 */
public void setTipo(java.lang.String tipo) {
	this.tipo = tipo;
}
}
